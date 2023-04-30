package knightminer.animalcrops.datagen;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.AnimalTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.tag.BlockTags;

import javax.annotation.Nullable;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public BlockTagProvider(FabricDataGenerator generator) {
		super(generator);
	}

	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(AnimalTags.CROP_SOIL).add(Blocks.GRASS_BLOCK, Blocks.PODZOL);
		getOrCreateTagBuilder(AnimalTags.SHROOM_SOIL).add(Blocks.SOUL_SOIL, Blocks.SOUL_SAND);
	}
}
