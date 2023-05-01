package knightminer.animalcrops.json;

import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;

import java.util.List;

public class LootManager {
    private static final Identifier GRASS_TABLE = Registration.getResource("blocks/grass_drops");
    private static final Identifier NETHER_TABLE = Registration.getResource("blocks/nether_grass_drops");
    private static final Identifier SEA_TABLE = Registration.getResource("blocks/sea_grass_drops");
    public static void checkSeedDrops(List<ItemStack> returnValue, BlockState blockState, net.minecraft.loot.LootManager lootManager, LootContext lootContext) {
        if (blockState.isIn(AnimalTags.DROPS_GRASS)) {
            returnValue.addAll(lootManager.getTable(GRASS_TABLE).generateLoot(lootContext));
        }
        if (blockState.isIn(AnimalTags.DROPS_NETHER)) {
            returnValue.addAll(lootManager.getTable(NETHER_TABLE).generateLoot(lootContext));
        }
        if (blockState.isIn(AnimalTags.DROPS_SEA)) {
            returnValue.addAll(lootManager.getTable(SEA_TABLE).generateLoot(lootContext));
        }
    }
}
