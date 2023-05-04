package knightminer.animalcrops.blocks;

import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.items.AnimalSeedsItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Common logic for water and lava crops
 */
public class AnemonemalBlock extends AnimalCropsBlock {
  private final Supplier<? extends Fluid> fluid;
  private final TagKey<Fluid> tag;
  public AnemonemalBlock(AbstractBlock.Settings props, TagKey<EntityType<?>> entityTag, Supplier<Fluid> fluid, TagKey<Fluid> tag) {
    super(props, entityTag);
    this.fluid = fluid;
    this.tag = tag;
  }

  @Override
  protected AnimalSeedsItem getSeedsItem() {
    return tag.equals(FluidTags.LAVA) ? Registration.magnemoneSpores : Registration.anemonemalSeeds;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
    return state.isSideSolidFullSquare(worldIn, pos, Direction.UP) && state.getBlock() != Blocks.MAGMA_BLOCK;
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext context) {
    FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
    return fluid.isIn(tag) && fluid.getHeight() == 8 ? super.getPlacementState(context) : null;
  }


  /* Fluid logic */

  @Override
  public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess world, BlockPos currentPos, BlockPos facingPos) {
    BlockState state = super.getStateForNeighborUpdate(stateIn, facing, facingState, world, currentPos, facingPos);
    if (!state.isAir()) {
      Fluid fluid = this.fluid.get();
      world.createAndScheduleFluidTick(currentPos, fluid, fluid.getTickRate(world));
    }

    return state;
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public FluidState getFluidState(BlockState state) {
    return fluid.get().getDefaultState();
  }
}
