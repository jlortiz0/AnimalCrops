package knightminer.animalcrops.client;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.blocks.entity.AnimalCropsBlockEntity;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.core.Utils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class ClientEvents implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		FabricLoader.getInstance().getModContainer(AnimalCrops.modID).ifPresent(modContainer -> {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(AnimalCrops.modID, "simple_crops"), modContainer, ResourcePackActivationType.NORMAL);
		});
		BlockEntityRendererRegistry.register(Registration.cropsTE, RenderAnimalCrops::new);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Registration.crops, Registration.anemonemal, Registration.shrooms, Registration.magnemone);
		ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> {
			if (world == null || pos == null) {
				return -1;
			}
			BlockEntity te = world.getBlockEntity(pos);
			if (te != null && te instanceof AnimalCropsBlockEntity be) {
				return getEggColor(be.getEntityId().orElse(""), index);
			}
			return -1;
		}, Registration.crops, Registration.anemonemal, Registration.shrooms, Registration.magnemone);
		ColorProviderRegistry.ITEM.register((stack, index) -> getEggColor(Utils.getEntityID(stack.getNbt()).orElse(""), index),
				Registration.seeds, Registration.anemonemalSeeds, Registration.shrooms, Registration.magnemone);
	}

	/* Helper functions */

	/**
	 * Gets the egg color for the given NBT
	 * @param tags   NBT, from either a item stack or a tile entiy
	 * @param index  Tint index to use
	 * @return  Egg color for the given tags and index
	 */
	@SuppressWarnings("Convert2MethodRef")
	private static int getEggColor(String entityId, int index) {
		return EntityType.get(entityId)
				.map(SpawnEggItem::forEntity)
				.map(egg->egg.getColor(index))
				.orElse(-1);
	}
}
