package knightminer.animalcrops.blocks;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.blocks.entity.AnimalCropsBlockEntity;
import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.core.Utils;
import knightminer.animalcrops.items.AnimalSeedsItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Base crop logic, used for plains crops directly
 */
public class AnimalCropsBlock extends CropBlock implements BlockEntityProvider {

	private final TagKey<EntityType<?>> tag;

	public AnimalCropsBlock(AbstractBlock.Settings props, TagKey<EntityType<?>> tag) {
		super(props);
		this.tag = tag;
	}

	/* Crop properties */

	@Override
	public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isIn(AnimalTags.CROP_SOIL);
	}


	/**
	 * Gets the seed item for this crop
	 * @return  Item for this crop
	 */
	@Override
	protected AnimalSeedsItem getSeedsItem() {
		return Registration.seeds;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		Optional<RegistryEntryList.Named<EntityType<?>>> ls = Registry.ENTITY_TYPE.getEntryList(tag);
		if (ls.isEmpty()) {
			return;
		}
		Iterator<RegistryEntry<EntityType<?>>> i = ls.get().stream().iterator();
		while (i.hasNext()) {
			Optional<Identifier> ent = i.next().getKey().map(k -> k.getValue());
			if (ent.isEmpty()) continue;
			ItemStack is = new ItemStack(this.getSeedsItem());
			Utils.setEntityId(is, ent.get());
			stacks.add(is);
		}
	}

	/* Seed logic */

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AnimalCropsBlockEntity(pos, state);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		// set the crop's entity
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof AnimalCropsBlockEntity animal) {
			Optional<Identifier> ent = Utils.getEntityID(stack.getNbt());
			ent.ifPresent(animal::setEntity);
			if (ent.isEmpty()) {
				world.breakBlock(pos, false);
				dropStack(world, pos, new ItemStack(Registration.pollen));
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public void onStateReplaced(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
		// if the block changed, spawn the animal
		if (state.getBlock() != newState.getBlock()) {
			// assuming we have the tile entity to use
			if (getAge(state) >= getMaxAge()) {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof AnimalCropsBlockEntity animal) {
					animal.spawnAnimal();
				}
			}
			super.onStateReplaced(state, level, pos, newState, isMoving);
			// otherwise, if the age lowered from max, spawn the animal
			// for right click harvest
		} else if (state.getBlock() == this && getAge(state) >= getMaxAge() && getAge(newState) < getMaxAge()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof AnimalCropsBlockEntity animal) {
				animal.spawnAnimal();
			}
		}
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		BlockEntity te = world.getBlockEntity(pos);
		ItemStack stack = new ItemStack(getSeedsItem());
		if(te != null && te instanceof AnimalCropsBlockEntity be) {
			be.getEntityId().ifPresent(id->Utils.setEntityId(stack, id));
		}
		return stack;
	}

	/* Bonemeal */

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return AnimalCrops.config.canBonemeal;
	}

	@Override
	protected int getGrowthAmount(World level) {
		return level.random.nextInt(1, 3);
	}

	@Override
	public void applyGrowth(World world, BlockPos pos, BlockState state) {
		super.applyGrowth(world, pos, state);
		if (this.getMaxAge() <= getAge(state) + 1) {
			world.setBlockState(pos, this.withAge(0), 2);
		}
	}
}
