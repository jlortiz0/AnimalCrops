package knightminer.animalcrops.core;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.blocks.AnemonemalBlock;
import knightminer.animalcrops.blocks.AnimalCropsBlock;
import knightminer.animalcrops.blocks.AnimalShroomBlock;
import knightminer.animalcrops.blocks.entity.AnimalCropsBlockEntity;
import knightminer.animalcrops.items.AnimalPollenItem;
import knightminer.animalcrops.items.AnimalSeedsItem;
import knightminer.animalcrops.json.ConfigCondition;
import knightminer.animalcrops.json.RandomAnimalLootFunction;
import knightminer.animalcrops.json.SetAnimalLootFunction;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public class Registration {

  private static final AbstractBlock.Settings cropProps = AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().strength(0).sounds(BlockSoundGroup.CROP).noCollision();
  private static final AbstractBlock.Settings cropNetherProps = AbstractBlock.Settings.of(Material.PLANT, DyeColor.RED).ticksRandomly().strength(0).sounds(BlockSoundGroup.NETHER_WART).noCollision();

  public static final Block crops = register(Registry.BLOCK, new AnimalCropsBlock(cropProps, AnimalTags.ANIMAL_CROPS), "crops"),
          anemonemal = register(Registry.BLOCK, new AnemonemalBlock(cropProps, AnimalTags.ANEMONEMAL, () -> Fluids.WATER, FluidTags.WATER), "anemonemal"),
          shrooms = register(Registry.BLOCK, new AnimalShroomBlock(cropNetherProps, AnimalTags.ANIMAL_SHROOMS), "shrooms"),
          magnemone = register(Registry.BLOCK, new AnemonemalBlock(cropNetherProps, AnimalTags.MAGNEMONES, () -> Fluids.LAVA, FluidTags.LAVA), "magnemones");

  private static final Item.Settings itemProps = (new Item.Settings()).group(ItemGroup.MATERIALS);
  public static final AnimalSeedsItem seeds = register(Registry.ITEM, new AnimalSeedsItem(crops, itemProps), "seeds"),
          spores = register(Registry.ITEM, new AnimalSeedsItem(shrooms, itemProps), "spores"),
          anemonemalSeeds = register(Registry.ITEM, new AnimalSeedsItem(anemonemal, itemProps), "anemonemal"),
          magnemoneSpores = register(Registry.ITEM, new AnimalSeedsItem(magnemone, itemProps), "magnemone");
  public static final AnimalPollenItem pollen = register(Registry.ITEM, new AnimalPollenItem(itemProps), "pollen");

  public static final BlockEntityType<AnimalCropsBlockEntity> cropsTE = register(Registry.BLOCK_ENTITY_TYPE, "crops", FabricBlockEntityTypeBuilder.create(
          AnimalCropsBlockEntity::new, crops, anemonemal, shrooms, magnemone).build());

  // subclass to prevent conflict with non-Forge registries and object holder
  public static class Loot {
    public static LootFunctionType setAnimalFunction;
    public static LootFunctionType randomAnimalFunction;
    public static LootConditionType configCondition;
  }

  public static void register() {
    CompostingChanceRegistry.INSTANCE.add(seeds, 0.5f);
    CompostingChanceRegistry.INSTANCE.add(anemonemalSeeds, 0.5f);
    CompostingChanceRegistry.INSTANCE.add(spores, 0.5f);
    CompostingChanceRegistry.INSTANCE.add(magnemoneSpores, 0.5f);
    CompostingChanceRegistry.INSTANCE.add(pollen, 0.5f);
    Loot.setAnimalFunction = register(Registry.LOOT_FUNCTION_TYPE, "set_animal", new LootFunctionType(SetAnimalLootFunction.SERIALIZER));
    Loot.randomAnimalFunction = register(Registry.LOOT_FUNCTION_TYPE, "random_animal", new LootFunctionType(RandomAnimalLootFunction.SERIALIZER));
    Loot.configCondition = register(Registry.LOOT_CONDITION_TYPE, "config", new LootConditionType(ConfigCondition.SERIALIZER));
  }


  /* Helper functions */

  /**
   * Gets a resource location in the animal crops domain
   * @param name  Resource path
   * @return  Animal Crops resource location
   */
  public static Identifier getResource(String name) {
    return new Identifier(AnimalCrops.modID, name);
  }

  /**
   * Helper method to register an entry, setting the registry name
   * @param registry  Registry to use
   * @param value     Value to register
   * @param name      Registry name, will be namespaced under AnimalCrops
   * @param <V>  Value tag
   * @param <T>  Registry tag
   */
  private static <T> T register(Registry<? super T> registry, T value, String name) {
      return register(registry, name, value);
  }

  /**
   * Registers a value to a vanilla registry
   * @param registry  Registry instance
   * @param name      Name to register
   * @param value     Value to register
   * @param <T>       Value tag
   * @return  Registered value
   */
  private static <T> T register(Registry<? super T> registry, String name, T value) {
    return Registry.register(registry, getResource(name), value);
  }
}
