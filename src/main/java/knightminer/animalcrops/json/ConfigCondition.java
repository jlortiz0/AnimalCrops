package knightminer.animalcrops.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.Registration;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Conditions a loot table entry based on config
 */
public class ConfigCondition implements LootCondition {
  private static final Map<String,ConfigCondition> PROPS = new HashMap<>();
  public static final ConfigSerializer SERIALIZER = new ConfigSerializer();

  private final String name;
  private final BooleanSupplier supplier;
  private ConfigCondition(String name, BooleanSupplier supplier) {
    this.name = name;
    this.supplier = supplier;
  }

  @Override
  public LootConditionType getType() {
    return Registration.Loot.configCondition;
  }

  @Override
  public boolean test(LootContext lootContext) {
    return supplier.getAsBoolean();
  }

  public static class ConfigSerializer implements JsonSerializer<ConfigCondition> {
    @Override
    public ConfigCondition fromJson(JsonObject json, JsonDeserializationContext context) {
      String prop = JsonHelper.getString(json, "prop");
      ConfigCondition config = PROPS.get(prop.toLowerCase(Locale.ROOT));
      if (config == null) {
        throw new JsonSyntaxException("Invalid config property name '" + prop + "'");
      }
      return config;
    }

    @Override
    public void toJson(JsonObject json, ConfigCondition config, JsonSerializationContext context) {
      json.addProperty("prop", config.name);
    }
  }

  /* Setup prop list */
  private static void add(String name, BooleanSupplier supplier) {
    PROPS.put(name, new ConfigCondition(name, supplier));
  }

  static {
    add("seeds", () -> AnimalCrops.config.animalCrops);
    add("anemonemal", () -> AnimalCrops.config.anemonemals);
    add("shrooms", () -> AnimalCrops.config.animalShrooms);
    add("magnemone", () -> AnimalCrops.config.magnemones);
    add("pollen", () -> AnimalCrops.config.dropAnimalPollen);
  }
}
