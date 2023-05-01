package knightminer.animalcrops.core;

import knightminer.animalcrops.AnimalCrops;
import net.minecraft.block.Block;
import net.minecraft.data.server.BlockTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/** Class holding all tags used by the mod */
public class AnimalTags {
	/** Static initialization */
	public static void init() {}

	// blocks

	/** Blocks valid for planting animal crops */
	public static final TagKey<Block> CROP_SOIL = blockTag("crops_soil");
	/** Blocks valid for planting animal spores */
	public static final TagKey<Block> SHROOM_SOIL = blockTag("shroom_soil");

	public static final TagKey<Block> DROPS_GRASS = blockTag("drops_grass");
	public static final TagKey<Block> DROPS_NETHER = blockTag("drops_nether_grass");
	public static final TagKey<Block> DROPS_SEA = blockTag("drops_sea_grass");

	// entities

	/** Entities that are able to be planted */
	public static final TagKey<EntityType<?>> PLANTABLE = entityTag("plantable");
	/** Entities for the overworld crop tag */
	public static final TagKey<EntityType<?>> ANIMAL_CROPS = entityTag("plantable/animal_crops");
	/** Entities for the underwater crop tag */
	public static final TagKey<EntityType<?>> ANEMONEMAL = entityTag("plantable/anemonemals");
	/** Entities for the nether crop tag */
	public static final TagKey<EntityType<?>> ANIMAL_SHROOMS = entityTag("plantable/animal_shrooms");
	/** Entities for the under lava crop tag */
	public static final TagKey<EntityType<?>> MAGNEMONES = entityTag("plantable/magnemones");
	/** Animal crop entities available as random drops */
	public static final TagKey<EntityType<?>> DROPPABLE_ANIMAL_CROPS = entityTag("droppable/animal_crops");
	/** Anemonemal entities available as random drops */
	public static final TagKey<EntityType<?>> DROPPABLE_ANEMONEMAL = entityTag("droppable/anemonemals");
	/** Animal shroom entities available as random drops */
	public static final TagKey<EntityType<?>> DROPPABLE_ANIMAL_SHROOMS = entityTag("droppable/animal_shrooms");
	/** Magnemone entities available as random drops */
	public static final TagKey<EntityType<?>> DROPPABLE_MAGNEMONES = entityTag("droppable/magnemones");
	/** Entities that pollen works on */
	public static final TagKey<EntityType<?>> POLLEN_REACTIVE = entityTag("pollen_reactive");

	/** Creates a tag for a block */
	private static TagKey<Block> blockTag(String name) {
		return TagKey.of(Registry.BLOCK_KEY, getResource(name));
	}

	/** Creates a tag for a entity tag */
	private static TagKey<EntityType<?>> entityTag(String name) {
		return TagKey.of(Registry.ENTITY_TYPE_KEY, getResource(name));
	}

	private static Identifier getResource(String name) {
		return new Identifier(AnimalCrops.modID, name);
	}

	public static void register() {}
}
