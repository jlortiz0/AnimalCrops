package knightminer.animalcrops.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntryPoint implements DataGeneratorEntrypoint {

    private static class ItemTagProvider extends FabricTagProvider<Item> {
        public ItemTagProvider(FabricDataGenerator generator) {
            super(generator, Registry.ITEM, "Item tags");
        }

        @Override
        public void generateTags() {
            getOrCreateTagBuilder(TagKey.of(Registry.ITEM_KEY, new Identifier("c", "shears"))).add(Items.SHEARS);
        }
    }
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(BlockTagProvider::new);
        fabricDataGenerator.addProvider(EntityTypeTagProvider::new);
        fabricDataGenerator.addProvider(ItemTagProvider::new);
    }
}
