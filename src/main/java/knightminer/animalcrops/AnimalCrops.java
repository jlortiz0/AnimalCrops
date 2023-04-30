package knightminer.animalcrops;

import knightminer.animalcrops.core.AnimalTags;
import knightminer.animalcrops.core.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnimalCrops implements ModInitializer {
	public static final String modID = "animalcrops";
	public static final Logger log = LogManager.getLogger(modID);

	public static Configuration config;

	@Override
	public void onInitialize() {
		AnimalTags.init();
		AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
	}
}
