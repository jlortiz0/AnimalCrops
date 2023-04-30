package knightminer.animalcrops.client;

import com.google.common.collect.ImmutableSet;
import knightminer.animalcrops.AnimalCrops;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Resource pack to override fancy models with simple ones
 */
public class SimpleCropPack extends AbstractFileResourcePack implements ResourcePackProvider {
  /** Base pack name, used for the folder containing pack resources */
  private static final String FOLDER = "simple_crops";
  /** Namespaced pack name, used as the internal name passed to the resource pack loader */
  public static final String PACK_NAME = AnimalCrops.modID + ":" + FOLDER;
  /** Resource prefix for valid pack resources. Essentially checks that they are client side Animal Crops resources */
  private static final String RES_PREFIX = ResourceType.CLIENT_RESOURCES.getDirectory() + "/" + AnimalCrops.modID + "/";
  /** Replacement prefix for pack resources, to load from FOLDER */
  private static final String PATH_PREFIX = String.format("/%s%s/", RES_PREFIX, FOLDER);

  /** Pack instance as it behaves the same way every time and does not require additional resources */
  public static final SimpleCropPack INSTANCE = new SimpleCropPack();

  /**
   * Default constructor, private as this functions properly singleton
   */
  private SimpleCropPack() {
    // this file is not used, so giving the most useful path
    super(new File(PATH_PREFIX));
  }
  @Nonnull
  @Override
  public String getName() {
    return "Simple Animal Crops";
  }

  @Override
  public Set<String> getNamespaces(ResourceType type) {
    // only replace resources for animal crops
    return type == ResourceType.CLIENT_RESOURCES ? ImmutableSet.of(AnimalCrops.modID) : ImmutableSet.of();
  }

  @Override
  protected InputStream openFile(String name) throws IOException {
    // pack.mcmeta and pack.png are fetched without prefix, so pull from proper directory
    // everything else is prefixed, so prefix is trimmed
    if (name.equals("pack.mcmeta") || name.equals("pack.png") || name.startsWith(RES_PREFIX)) {
      InputStream stream = AnimalCrops.class.getResourceAsStream(getPath(name));
      if (stream != null) {
        return stream;
      }
    }

    throw new ResourceNotFoundException(this.base, name);
  }

  @Override
  protected boolean containsFile(String name) {
    if (!name.startsWith(RES_PREFIX)) {
      return false;
    }
    return AnimalCrops.class.getResource(getPath(name)) != null;
  }

  @Override
  public Collection<Identifier> findResources(ResourceType type, String domain, String path, int maxDepth, Predicate<String> filter) {
    // this method appears to only be called for fonts and GUIs, so just return an empty list as neither is used here
    return Collections.emptyList();
  }

  @Override
  public void close() {}

  /**
   * Gets the resource path for a given resource
   * @param name  Default resource path
   * @return  Resource with the new path, either prefixed or with the old prefix replaced
   */
  private static String getPath(String name) {
    // if not prefixed with animalcrops, append the full path
    // used for pack.mcmeta and pack.png
    if (!name.startsWith(RES_PREFIX)) {
      return PATH_PREFIX + name;
    }

    // goes from assets/animalcrops/<res> to assets/animalcrops/simple_crop/<res>
    return PATH_PREFIX + name.substring(RES_PREFIX.length());
  }


  /* Static functions */

  @Override
  public void register(Consumer<ResourcePackProfile> infoConsumer, ResourcePackProfile.Factory factory) {
    infoConsumer.accept(ResourcePackProfile.of(PACK_NAME, false, ()->this, factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_NONE));
  }
}
