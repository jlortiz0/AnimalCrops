package knightminer.animalcrops.core;

import knightminer.animalcrops.items.AnimalPollenItem;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import javax.annotation.Nullable;
import java.util.Random;

@Config(name = "animalcrops")
public class Configuration implements ConfigData {

	// types
	public boolean animalCrops;
	public boolean anemonemals;
	public boolean animalShrooms;
	public boolean magnemones;
	// general
	public boolean canBonemeal;
	public AnimalPollenItem.Action pollenAction;
	// grass drops
	public boolean dropAnimalPollen;

	/** Config setup for each type of animal crops */
	public static class AnimalCropType {
		private final TagKey<EntityType<?>> tag;
		private final BooleanValue drop;

		protected AnimalCropType(TagKey<EntityType<?>> tag, BooleanValue drop) {
			this.tag = tag;
			this.drop = drop;
		}

		/**
		 * Returns true if this type of animal crops drops
		 */
		public boolean doesDrop() {
			ITag<EntityType<?>> typeTag = ForgeRegistries.ENTITIES.tags().getTag(tag);
			return drop.get() && !typeTag.isBound() && !typeTag.isEmpty();
		}

		/** Gets a random value of this crop drop type */
		@Nullable
		public EntityType<?> getRandomValue(Random random) {
			if (doesDrop()) {
				return ForgeRegistries.ENTITIES.tags().getTag(tag).getRandomElement(random).get();
			}
			return null;
		}
	}
}
