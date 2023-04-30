package knightminer.animalcrops.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Configuration;
import knightminer.animalcrops.core.Registration;
import knightminer.animalcrops.core.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Sets an item's entity to random
 */
public class RandomAnimalLootFunction extends ConditionalLootFunction {

  private static final Map<String,TagKey<EntityType<?>> > TYPES = new HashMap<>();
  public static final Serializer SERIALIZER = new Serializer();

  private final String type;
  private final TagKey<EntityType<?>> tag;
  protected RandomAnimalLootFunction(LootCondition[] conditions, String type, TagKey<EntityType<?>> tag) {
    super(conditions);
    this.type = type;
    this.tag = tag;
  }

  @Override
  public LootFunctionType getType() {
    return Registration.Loot.randomAnimalFunction;
  }

  @Override
  protected ItemStack process(ItemStack stack, LootContext context) {
    Identifier type = Configuration.getRandomValue(tag, context.getRandom());
    // prevent crash if empty, the config condition should handle this though
    if (type == null) {
      AnimalCrops.log.error("Received empty animal list for {}, a condition is missing in the loot table", type);
    } else {
      Utils.setEntityId(stack, type);
    }
    return stack;
  }

  private static class Serializer extends ConditionalLootFunction.Serializer<RandomAnimalLootFunction> {
    @Override
    public void toJson(JsonObject json, RandomAnimalLootFunction randAnimal, JsonSerializationContext context) {
      super.toJson(json, randAnimal, context);
      json.addProperty("type", randAnimal.type);
    }

    @Nonnull
    @Override
    public RandomAnimalLootFunction fromJson(JsonObject json, JsonDeserializationContext ctx, LootCondition[] conditions) {
      String type = JsonHelper.getString(json, "type").toLowerCase(Locale.ROOT);
      TagKey<EntityType<?>> animalCropType = TYPES.get(type);
      if (animalCropType == null) {
        throw new JsonSyntaxException("Invalid animal tag '" + type + "'");
      }
      return new RandomAnimalLootFunction(conditions, type, animalCropType);
    }
  }

  /* Setup prop list */
  static {
    TYPES.put("crops",AnimalTags.DROPPABLE_ANIMAL_CROPS);
    TYPES.put("anemonemal", AnimalTags.DROPPABLE_ANEMONEMAL);
    TYPES.put("shrooms", AnimalTags.DROPPABLE_ANIMAL_SHROOMS);
    TYPES.put("magnemone", AnimalTags.DROPPABLE_MAGNEMONES);
  }
}
