package knightminer.animalcrops.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import knightminer.animalcrops.json.LootManager;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AbstractBlock.class)
public class BlockLootMixin {
    @Inject(at=@At("TAIL"), method = "getDroppedStacks", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void checkSeedDrops(BlockState blockState, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info, Identifier lootTableId, LootContext lootContext, ServerWorld serverLevel)  {
        LootManager.checkSeedDrops(info.getReturnValue(), blockState, serverLevel.getServer().getLootManager(), lootContext);
    }
}
