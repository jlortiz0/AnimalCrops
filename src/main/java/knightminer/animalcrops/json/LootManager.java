package knightminer.animalcrops.json;

import knightminer.animalcrops.core.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.List;

public class LootManager {
    public static void checkSeedDrops(List<ItemStack> returnValue, BlockState blockState, net.minecraft.loot.LootManager lootManager, LootContext lootContext) {
        returnValue.addAll(lootManager.getTable(Registration.getResource("blocks/grass_drops")).generateLoot(lootContext));
        returnValue.addAll(lootManager.getTable(Registration.getResource("blocks/nether_grass_drops")).generateLoot(lootContext));
        returnValue.addAll(lootManager.getTable(Registration.getResource("blocks/sea_grass_drops")).generateLoot(lootContext));
    }
}
