package knightminer.animalcrops.core;

import knightminer.animalcrops.items.AnimalPollenItem;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Random;

@Config(name = "animalcrops")
public class Configuration implements ConfigData {

	// types
	public boolean animalCrops = true;
	public boolean anemonemals = true;
	public boolean animalShrooms = true;
	public boolean magnemones = true;
	// general
	public boolean canBonemeal = true;
	public AnimalPollenItem.Action pollenAction = AnimalPollenItem.Action.DAMAGE;
	// grass drops
	public boolean dropAnimalPollen = true;

	public boolean renderCropEntity = true;
	public boolean renderAnemonemalEntity = true;
	public boolean renderShroomEntity = true;
	public boolean renderMagnemoneEntity = true;

	public boolean shouldRenderEntity(Block block) {
		if (block == Registration.crops) {
			return renderCropEntity;
		}
		if (block == Registration.anemonemal) {
			return renderAnemonemalEntity;
		}
		if (block == Registration.shrooms) {
			return renderShroomEntity;
		}
		if (block == Registration.magnemone) {
			return renderMagnemoneEntity;
		}
		// fallback in case some other mod extends this
		return true;
	}

	public static Identifier getRandomValue(TagKey<EntityType<?>> tag, Random random) {
		RegistryEntryList.Named<EntityType<?>> ls = Registry.ENTITY_TYPE.getEntryList(tag).get();
		if (ls == null || ls.size() == 0) {
			return null;
		}
		return ls.get(random.nextInt(ls.size())).getKey().map(k -> k.getValue()).orElse(null);
	}
}
