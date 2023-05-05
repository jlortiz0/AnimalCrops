package knightminer.animalcrops.blocks.entity;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.core.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ServerWorldAccess;

import javax.annotation.Nullable;
import java.util.Optional;

public class AnimalCropsBlockEntity extends BlockEntity {
	/** Tag to use to store the random direction */
	public static final String TAG_DIRECTION = "direction";

	/**
	 * Cached entity from {@link #getEntity(boolean)}.
	 * Typically null except on the client side (only cached during fancy crop rendering)
	 * */
	private LivingEntity entity;
	private Identifier entityId;
	private int direction;

	public AnimalCropsBlockEntity(BlockPos pos, BlockState state) {
		super(Registration.cropsTE, pos, state);
	}


	/* Getters and setters */

	/**
	 * Sets the entity into the TE
	 * @param entityID  Entity ID to set
	 */
	public void setEntity(Identifier entityID) {
		// only set if valid
		if (!entityValid(entityID)) {
			return;
		}

		this.entityId = entityID;
		this.direction = this.world.random.nextInt(4);
		this.markDirty();
	}

	public Optional<Identifier> getEntityId() {
		return Optional.ofNullable(this.entityId);
	}

	/**
	 * Gets the entity for this crop
	 * @param  cacheEntity  If true, pull the entity from the cache and cache the result. Typically only used clientside
	 * @return  The entity for this crop
	 */
	@SuppressWarnings("Convert2MethodRef")
	@Nullable
	public LivingEntity getEntity(boolean cacheEntity) {
		if (cacheEntity && entity != null) {
			return entity;
		}

		// create the entity from the tile data
		Entity created = Optional.ofNullable(Registry.ENTITY_TYPE.get(entityId)).map((type) -> type.create(world)).orElse(null);
		if (created == null) {
			return null;
		}

		// if the entity is not MobEntity, discard it
		// should not happen as all spawn eggs are MobEntity
		if (!(created instanceof LivingEntity entity)) {
			created.remove(Entity.RemovalReason.DISCARDED);
			AnimalCrops.log.error("Attempted to create invalid non-living entity " + created.getType());
			return null;
		}

		// set the age for ageable entities
		float angle = this.getAngle();
		entity.setYaw(angle);
		entity.prevYaw = angle;
		entity.prevHeadYaw = entity.headYaw = angle;
		entity.prevBodyYaw = entity.bodyYaw = angle;

		if (entity instanceof PassiveEntity mob) {
			mob.setBaby(true);
		}

		// cache the entity if requested
		if (cacheEntity) {
			this.entity = entity;
		}
		return entity;
	}

	/**
	 * Gets the angle for the given random crop direction
	 * @return  Angle in 90 degree increments
	 */
	public float getAngle() {
		int index = this.direction;
		if (index < 0 || index > 3) {
			return Direction.SOUTH.asRotation();
		}
		return Direction.byId(index).asRotation();
	}

	/**
	 * Spawns the current entity into the world then clears it
	 */
	public void spawnAnimal() {
		// if we have no entity, give up
		LivingEntity entity = getEntity(false);
		if(entity == null) {
			return;
		}

		// set position
		entity.setPos(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);

		// set entity data where relevant
		MobEntity mob = null;
		if (world instanceof ServerWorldAccess && entity instanceof MobEntity) {
			mob = (MobEntity) entity;
			mob.initialize((ServerWorldAccess) world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWN_EGG, null, null);
		}

		// slime sizes should not be bigger than 2
		if(entity instanceof SlimeEntity slime && slime.getSize() > 2) {
			Utils.setSlimeSize(slime, 2);
		}

		// spawn
		entity.world = world;
		world.spawnEntity(entity);
		if (mob != null) {
			mob.playAmbientSound();
		}
	}

	/* Helpers */

	/**
	 * Checks if the entity is valid for this crop block
	 * @param entityID  ID to check
	 * @return  True if the ID is valid, false otherwise
	 */
	private static boolean entityValid(Identifier loc) {
		if (loc != null && Registry.ENTITY_TYPE.containsId(loc)) {
			EntityType<?> type = Registry.ENTITY_TYPE.get(loc);
			return type != null && type.isIn(AnimalTags.PLANTABLE);
		}
		return false;
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("direction", this.direction);
		nbt.putString("entityType", this.entityId.toString());
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.direction = nbt.getInt("direction");
		this.entityId = new Identifier(nbt.getString("entityType"));
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}
}
