package knightminer.animalcrops.blocks;

import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.items.AnimalSeedsItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

/**
 * Logic for nether crops
 */
public class AnimalShroomBlock extends AnimalCropsBlock {
	public AnimalShroomBlock(AbstractBlock.Settings props, TagKey<EntityType<?>> tag) {
		super(props, tag);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isIn(AnimalTags.SHROOM_SOIL);
	}

	// override to remove light check
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return this.canPlaceAt(state, world, pos);
	}

	@Override
	protected AnimalSeedsItem getSeedsItem() {
		return Registration.spores;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int i = this.getAge(state);
		if (i < this.getMaxAge() && random.nextInt(10) == 0) {
			state = state.with(AGE, i + 1);
			world.setBlockState(pos, state, 2);
		}
	}
}
