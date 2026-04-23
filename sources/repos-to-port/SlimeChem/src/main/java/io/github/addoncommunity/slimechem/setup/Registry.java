package io.github.addoncommunity.slimechem.setup;

import io.github.addoncommunity.slimechem.implementation.atomic.Element;
import io.github.addoncommunity.slimechem.implementation.atomic.Molecule;
import io.github.addoncommunity.slimechem.implementation.atomic.isotopes.Isotope;
import io.github.addoncommunity.slimechem.implementation.attributes.Atom;
import io.github.addoncommunity.slimechem.implementation.attributes.Ingredient;
import io.github.addoncommunity.slimechem.implementation.generators.RTG;
import io.github.addoncommunity.slimechem.implementation.machines.ChemicalCombiner;
import io.github.addoncommunity.slimechem.implementation.machines.ChemicalDissolver;
import io.github.addoncommunity.slimechem.implementation.machines.Cyclotron;
import io.github.addoncommunity.slimechem.implementation.machines.NuclearFurnace;
import io.github.addoncommunity.slimechem.implementation.subatomic.Lepton;
import io.github.addoncommunity.slimechem.lists.Categories;
import io.github.addoncommunity.slimechem.objects.IngredientItem;
import io.github.addoncommunity.slimechem.objects.RadioactiveItem;
import io.github.addoncommunity.slimechem.utils.Util;
import io.github.addoncommunity.slimechem.SlimeChem;
import io.github.addoncommunity.slimechem.implementation.subatomic.Boson;
import io.github.addoncommunity.slimechem.implementation.subatomic.Nucleon;
import io.github.addoncommunity.slimechem.lists.Items;
import io.github.addoncommunity.slimechem.lists.RecipeTypes;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public final class Registry {
    
    @Getter
    private static final Map<SlimefunItem, Ingredient> ITEMS = new HashMap<>();
    @Getter
    private static final Map<String, Ingredient> IDS = new HashMap<>();
    @Getter
    private static final Set<Atom> radioactiveItems = new HashSet<>();

    public static void setup(SlimeChem plugin) {

        new ElementCategory(plugin).register(plugin);

        for (Element element : Element.values()) {
            if (element.isRadioactive()) {
                radioactiveItems.add(element);
                registerItem(new RadioactiveItem(Categories.ELEMENTS, element, RecipeType.NULL, null, Util.fromRadioactivityInt(element.getRadiationLevel())), plugin);
            } else {
                registerItem(new IngredientItem(Categories.ELEMENTS, element, RecipeType.NULL, null), plugin); //should later be changed to proton+neutron+electron recipe in fusion
            }
        }
        
        plugin.getLogger().log(Level.INFO, "Registered " + Element.values().length + " Elements!");

        final EnumMap<Element, Set<Isotope>> isotopes = Isotope.getIsotopes();
        int isocount = 0;
        for (Set<Isotope> isotopeSet : isotopes.values()) {
            for (Isotope isotope : isotopeSet) {
                if (isotope.isMainIsotope()) {
                    continue;
                }
                isocount++;
                List<Isotope> superIsotopes = new ArrayList<>();
                for (Set<Isotope> isotopeCollection : isotopes.values()) {
                    for (Isotope iso : isotopeCollection) {
                        iso.getDecayProduct().ifPresent(i -> {
                            if (i.equals(isotope)) {
                                superIsotopes.add(iso);
                            }
                        });
                    }
                }
                Util.trimList(superIsotopes, 9);

                ItemStack[] items;
                if (superIsotopes.size() > 0) {
                    items = new ItemStack[9];
                    for (int i = 0; i < items.length; i++) {
                        ItemStack stack;
                        try {
                            stack = superIsotopes.get(i).getItem();
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                        items[i] = stack;
                    }
                    if (items[items.length - 1] != null) {
                        items[items.length - 1] = new CustomItemStack(
                            Material.WHITE_DYE,
                            "&7And more..."
                        );
                    }
                } else {
                    items = null;
                }

                if (isotope.isRadioactive()) {
                    radioactiveItems.add(isotope);
                    registerItem(new RadioactiveItem(Categories.ISOTOPES, isotope, RecipeTypes.RTG, items, Util.fromRadioactivityInt(isotope.getRadiationLevel())), plugin);
                } else {
                    registerItem(new IngredientItem(Categories.ISOTOPES, isotope, RecipeTypes.RTG, items), plugin);
                }
            }
        }

        plugin.getLogger().log(Level.INFO, "Registered " + isocount + " Isotopes!");

        for (Molecule molecule : Molecule.values()) {
            registerItem(new IngredientItem(Categories.MOLECULES, molecule, RecipeTypes.COMBINER, molecule.getRecipe()), plugin);
        }
        
        plugin.getLogger().log(Level.INFO, "Registered " + Molecule.values().length + " Molecules!");

        for (Boson boson : Boson.values()) {
            new SlimefunItem(Categories.SUBATOMIC, boson.getItem(), RecipeType.NULL, new ItemStack[0]);
        }

        for (Lepton lepton : Lepton.values()) {
            new SlimefunItem(Categories.SUBATOMIC, lepton.getItem(), RecipeType.NULL, new ItemStack[0]);
        }

        for (Nucleon nucleon : Nucleon.values()) {
            new SlimefunItem(Categories.SUBATOMIC, nucleon.getItem(), RecipeType.NULL, new ItemStack[0]);
        }
        
        new ChemicalDissolver().register(plugin);
        new ChemicalCombiner().register(plugin);
        new NuclearFurnace().register(plugin);
        new RTG(Items.RTG_1, new ItemStack[0], false).register(plugin);
        new RTG(Items.RTG_2, new ItemStack[0], true).register(plugin);

        new Cyclotron().register(plugin);
    }

    private static void registerItem(@Nonnull IngredientItem item, @Nonnull SlimeChem plugin) {
        ITEMS.put(item, item.getIngredient());
        IDS.put(item.getId(), item.getIngredient());
        item.register(plugin);
    }

    private static void registerItem(@Nonnull RadioactiveItem item, @Nonnull SlimeChem plugin) {
        ITEMS.put(item, item.getIngredient());
        IDS.put(item.getId(), item.getIngredient());
        item.register(plugin);
    }

}
