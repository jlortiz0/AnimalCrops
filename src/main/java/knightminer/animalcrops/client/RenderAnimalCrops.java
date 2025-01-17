package knightminer.animalcrops.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.blocks.entity.AnimalCropsBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.Vec3d;

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
    EntityRenderer rend = mc.getEntityRenderDispatcher().getRenderer(entity);
    Vec3d vec3d = rend.getPositionOffset(entity, 0);
    stack.translate(vec3d.getX() + 0.5, vec3d.getY(), vec3d.getZ() + 0.5);
    float scale = age / 7f;
    stack.scale(-scale, -scale, scale);
    stack.translate(0, -1.5, 0);
    if (rend instanceof LivingEntityRenderer<?,?> lrend) {
      EntityModel model = lrend.getModel();
      model.child = entity.isBaby();
      model.animateModel(entity, 0, 0, 0);
      model.setAngles(entity, 0, 0, 0, 0, 0);
      RenderLayer renderLayer = model.getLayer(rend.getTexture(entity));
      model.render(stack, buffer.getBuffer(renderLayer), lighting, LivingEntityRenderer.getOverlay(entity, 0), 0.65f, 1f, 0.65f, 1f);
    }
    stack.pop();
  }
}
