package knightminer.animalcrops.plugins.jei;

import knightminer.animalcrops.AnimalCrops;
import knightminer.animalcrops.core.Registration;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

/** Subtype interpretable registration mostly */
public class JEIPlugin implements REIClientPlugin {

	public Identifier getPluginUid() {
		return new Identifier(AnimalCrops.modID, "rei");
	}

	private record checkIdentPredicate(Identifier ident) implements Predicate<EntryStack<?>> {
		public boolean test(EntryStack<?> e) {
			return e.getIdentifier().equals(ident);
		}
	}

	private void registerCollapse(CollapsibleEntryRegistry registry, String... name) {
		for (String n : name) {
			Identifier i = Registration.getResource(n);
			registry.group(i, new TranslatableText("item." + i.getNamespace() + "." + i.getPath() + ".default"), new checkIdentPredicate(i));
		}
	}

	@Override
	public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
		registerCollapse(registry, "seeds", "anemonemal", "spores", "magnemone");
	}
}
