package knightminer.animalcrops.items;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Configuration;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.core.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Item to convert mobs into seeds
 */
public class AnimalPollenItem extends Item {

  public AnimalPollenItem(Item.Settings props) {
    super(props);
  }

  @Override
  public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
    EntityType<?> type = entity.getType();
    if (type.isIn(AnimalTags.POLLEN_REACTIVE)) {
      // next, check which type of seed we are grabbing
      Item item = null;
      if (type.isIn(AnimalTags.ANIMAL_CROPS)) {
        item = Registration.seeds;
      }
      else if (type.isIn(AnimalTags.ANEMONEMAL)) {
        item = Registration.anemonemalSeeds;
      }
      else if (type.isIn(AnimalTags.ANIMAL_SHROOMS)) {
        item = Registration.spores;
      }
      else if (type.isIn(AnimalTags.MAGNEMONES)) {
        item = Registration.magnemoneSpores;
      }
      // its possible the type matches none because someone used tags wrongly
      if (item != null) {
        // create the seed item
        ItemStack seeds = new ItemStack(item);
        Utils.setEntityId(seeds, Objects.requireNonNull(type.getUntranslatedName()).toString());
        player.setStackInHand(hand, Utils.fillContainer(player, stack, seeds));

        // effects
        entity.playSound(SoundEvents.BLOCK_NETHER_WART_BREAK, 1.0F, 0.8F);
        spawnEntityParticles(entity, ParticleTypes.MYCELIUM, 15);
        switch (AnimalCrops.config.pollenAction) {
          case CONSUME -> {
            // spawn death particles and remove the entity
            spawnEntityParticles(entity, ParticleTypes.POOF, 20);
            if (!entity.getWorld().isClient) {
              entity.remove(Entity.RemovalReason.KILLED);
            }
          }
          case DAMAGE -> {
            entity.damage(DamageSource.CACTUS, 4);
            spawnEntityParticles(entity, ParticleTypes.DAMAGE_INDICATOR, 2);
          }
        }
        return ActionResult.SUCCESS;
      }
    }

    // tell the player why nothing happened
    player.sendMessage(new TranslatableText(this.getTranslationKey() + ".invalid", type.getTranslationKey()), true);
    return ActionResult.SUCCESS;
  }

  /**
   * Spawns particles around an entity, relative to its size
   * @param entity    Entity
   * @param particle  Particle to spawn
   * @param count     Number to spawn
   */
  private static void spawnEntityParticles(LivingEntity entity, ParticleEffect particle, int count) {
    World world = entity.getWorld();
    for(int k = 0; k < count; k++) {
      double speedX = world.random.nextGaussian() * 0.02D;
      double speedY = world.random.nextGaussian() * 0.02D;
      double speedZ = world.random.nextGaussian() * 0.02D;
      world.addParticle(particle, entity.getParticleX(1.0D), entity.getRandomBodyY(), entity.getParticleZ(1.0D), speedX, speedY, speedZ);
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World level, List<Text> tooltip, TooltipContext flagIn) {
    tooltip.add(new TranslatableText(this.getTranslationKey() + ".tooltip"));
  }

  /** Valid actions for spores, as set by the config */
  public enum Action {
    /** Entity is consumed in a cloud of smoke */
    CONSUME,
    /** Entity takes damage, but remains in the world */
    DAMAGE,
    /** No action against entity */
    NONE
  }
}
