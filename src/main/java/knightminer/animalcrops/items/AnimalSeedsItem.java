package knightminer.animalcrops.items;

import knightminer.animalcrops.blocks.AnimalCropsBlock;
import knightminer.animalcrops.core.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item that allows planting the crop
 */
public class AnimalSeedsItem extends BlockItem {

	public AnimalSeedsItem(Block crops, Item.Settings props) {
		super(crops, props);
	}

	// restore default, we call seeds seeds and crops crops

	@Override
	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}

	@Override
	public Text getName(ItemStack stack) {
    return Utils.getEntityID(stack.getNbt())
                .map(Registry.ENTITY_TYPE::get)
                .map(EntityType::getTranslationKey)
                .map((key) -> new TranslatableText(this.getTranslationKey(), new TranslatableText(key)))
                .orElseGet(() -> new TranslatableText(this.getTranslationKey() + ".default"));
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World level, List<Text> tooltip, TooltipContext flagIn) {
    super.appendTooltip(stack, level, tooltip, flagIn);
    tooltip.add(new TranslatableText(this.getTranslationKey() + ".tooltip"));
  }

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> items) {
	}
}
