package knightminer.animalcrops.client;

import com.mojang.blaze3d.systems.RenderSystem;
import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.blocks.entity.AnimalCropsBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class RenderAnimalCrops implements BlockEntityRenderer<AnimalCropsBlockEntity> {
  private static final MinecraftClient mc = MinecraftClient.getInstance();

  public RenderAnimalCrops(BlockEntityRendererFactory.Context context) {}

  @Override
  public void render(AnimalCropsBlockEntity be, float delta, MatrixStack stack, VertexConsumerProvider buffer, int lighting, int var6) {
    // check with the settings file to determine if this block renders its TE
    BlockState state = be.getCachedState();
    if (!AnimalCrops.config.shouldRenderEntity(state.getBlock())) {
        return;
    }
    int age = state.get(CropBlock.AGE);
    if (age == 0) {
        return;
    }

    LivingEntity entity = be.getEntity(true);
    if (entity == null) {
        return;
    }

    // its pretty easy, just draw the entity
    stack.push();
    stack.translate(0.5, 0, 0.5);
    float[] oldColor = RenderSystem.getShaderColor();
    RenderSystem.setShaderColor(0.65f, 1f, 0.65f, 1f);
    if(age < 7) {
        float scale = age / 7f;
        stack.scale(scale, scale, scale);
    }
    mc.getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, stack, buffer, lighting);
    RenderSystem.setShaderColor(oldColor[0], oldColor[1], oldColor[2], oldColor[3]);
    stack.pop();
  }
}
