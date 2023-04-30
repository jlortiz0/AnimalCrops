package knightminer.animalcrops.core;

import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class Utils {
	public static final String ENTITY_TAG = "entity";

	private Utils() {}

  /**
   * Gets the entity ID from a set of tags
   * @param tags  Tag compound, from either a TE or a stack
   * @return  Entity resource location
   */
  public static Optional<Identifier> getEntityID(@Nullable NbtCompound tags) {
    // no tags? skip
    if (tags == null) {
      return Optional.empty();
    }

    // no entity? also give up
    if (!tags.contains(ENTITY_TAG, 8)) {
      return Optional.empty();
    }

    return Optional.of(new Identifier(tags.getString(ENTITY_TAG)));
  }

  /**
   * Sets the entity ID for a given NBT tag
   * @param stack   Stack to set NBT, will be modified
   * @param entity  Entity string
   * @return  Stack with NBT set
   */
  public static ItemStack setEntityId(ItemStack stack, @Nullable Identifier entity) {
    if(entity == null) {
      return stack;
    }
    stack.getOrCreateNbt().putString(ENTITY_TAG, entity.toString());
    return stack;
  }

  /**
   * Fills a stack of containers, shrinking it by 1
   * @param player     Player to give the item to and for creative checks
   * @param container  Container stack, may contain more than 1
   * @param filled     Filled container stack
   * @return  Filled stack if 1 container, leftover container if more than 1, dropping the filled
   */
  public static ItemStack fillContainer(PlayerEntity player, ItemStack container, ItemStack filled) {
    container = container.copy();
    if (!player.isCreative()) {
      container.decrement(1);
      if (container.isEmpty()) {
        return filled;
      }
    }
    if (!player.getInventory().insertStack(filled)) {
      player.dropItem(filled, false);
    }
    return container;
  }

  /**
   * Sets a slime's size using {@link SlimeEntity::setSize(int, boolean)}
   * @param slime  Slime instance
   * @param size   Slime size to use
   */
  public static void setSlimeSize(SlimeEntity slime, int size) {
    slime.setSize(size, true);
  }
}
