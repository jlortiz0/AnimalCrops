package knightminer.animalcrops.datagen;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.AnimalTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public EntityTypeTagProvider(FabricDataGenerator generator) {
		super(generator);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(AnimalTags.PLANTABLE).addTag(AnimalTags.ANIMAL_CROPS).addTag(AnimalTags.ANEMONEMAL).addTag(AnimalTags.ANIMAL_SHROOMS).addTag(AnimalTags.MAGNEMONES);

		// these tags directly contain things plantable but not droppable
		getOrCreateTagBuilder(AnimalTags.ANIMAL_CROPS).addTag(AnimalTags.DROPPABLE_ANIMAL_CROPS);
		getOrCreateTagBuilder(AnimalTags.ANEMONEMAL).addTag(AnimalTags.DROPPABLE_ANEMONEMAL);
		getOrCreateTagBuilder(AnimalTags.ANIMAL_SHROOMS).addTag(AnimalTags.DROPPABLE_ANIMAL_SHROOMS);
		getOrCreateTagBuilder(AnimalTags.MAGNEMONES).addTag(AnimalTags.DROPPABLE_MAGNEMONES);

		// overworld
		getOrCreateTagBuilder(AnimalTags.DROPPABLE_ANIMAL_CROPS).add(
				EntityType.CHICKEN, EntityType.COW, EntityType.MOOSHROOM, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP,
				EntityType.BEE, EntityType.CAT, EntityType.FOX, EntityType.OCELOT, EntityType.PARROT, EntityType.WOLF,
				EntityType.DONKEY, EntityType.HORSE, EntityType.MULE, EntityType.LLAMA,
				EntityType.PANDA, EntityType.POLAR_BEAR,
				EntityType.VILLAGER)
				.addOptional(new Identifier("waddles:adelie_penguin"));
		getOrCreateTagBuilder(AnimalTags.DROPPABLE_ANEMONEMAL).add(
				EntityType.COD, EntityType.PUFFERFISH, EntityType.SALMON, EntityType.TROPICAL_FISH,
				EntityType.DOLPHIN, EntityType.SQUID, EntityType.TURTLE);
		// nether
		getOrCreateTagBuilder(AnimalTags.DROPPABLE_ANIMAL_SHROOMS).add(EntityType.HOGLIN, EntityType.PIGLIN);
		getOrCreateTagBuilder(AnimalTags.DROPPABLE_MAGNEMONES).add(EntityType.STRIDER);

		// allows limiting pollen to just some mobs
		getOrCreateTagBuilder(AnimalTags.POLLEN_REACTIVE).addTag(AnimalTags.PLANTABLE);
	}
}