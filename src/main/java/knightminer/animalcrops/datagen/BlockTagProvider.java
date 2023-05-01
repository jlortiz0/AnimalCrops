package knightminer.animalcrops.datagen;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.AnimalTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class BlockTagProvider extends FabricTagProvider<Block> {
	public BlockTagProvider(FabricDataGenerator generator) {
		super(generator, Registry.BLOCK, "Block tags");
	}

	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(AnimalTags.CROP_SOIL).addOptionalTag(BlockTags.DIRT).add(Blocks.FARMLAND);
		getOrCreateTagBuilder(AnimalTags.SHROOM_SOIL).add(Blocks.SOUL_SOIL, Blocks.SOUL_SAND);

		getOrCreateTagBuilder(AnimalTags.DROPS_GRASS).add(Blocks.GRASS, Blocks.FERN, Blocks.TALL_GRASS, Blocks.LARGE_FERN);
		getOrCreateTagBuilder(AnimalTags.DROPS_NETHER).add(Blocks.NETHER_SPROUTS);
		getOrCreateTagBuilder(AnimalTags.DROPS_SEA).add(Blocks.SEAGRASS, Blocks.TALL_SEAGRASS);
	}
}
