package knightminer.animalcrops.json;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import knightminer.animalcrops.blocks.entity.AnimalCropsBlockEntity;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.core.Utils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

import java.util.Set;

/**
 * Function to copy the animal from TE data to the seed item
 */
public class SetAnimalLootFunction extends ConditionalLootFunction {
  public static final Serializer SERIALIZER = new Serializer();

  protected SetAnimalLootFunction(LootCondition[] conditions) {
    super(conditions);
  }

  @Override
  public LootFunctionType getType() {
    return Registration.Loot.setAnimalFunction;
  }

  @Override
  protected ItemStack process(ItemStack stack, LootContext context) {
    BlockEntity te = context.get(LootContextParameters.BLOCK_ENTITY);
    if(te != null && te instanceof AnimalCropsBlockEntity be) {
      be.getEntityId().ifPresent(id->Utils.setEntityId(stack, id));
    }
    return stack;
  }

  @Override
  public Set<LootContextParameter<?>> getRequiredParameters() {
    return ImmutableSet.of(LootContextParameters.BLOCK_ENTITY);
  }

  private static class Serializer extends ConditionalLootFunction.Serializer<SetAnimalLootFunction> {
    @Override
    public SetAnimalLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
      return new SetAnimalLootFunction(conditions);
    }
  }
}
