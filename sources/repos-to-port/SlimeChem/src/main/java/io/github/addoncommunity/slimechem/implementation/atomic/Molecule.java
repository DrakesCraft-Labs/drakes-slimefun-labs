package io.github.addoncommunity.slimechem.implementation.atomic;

import io.github.addoncommunity.slimechem.implementation.attributes.Ingredient;
import io.github.addoncommunity.slimechem.utils.SubNum;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Enum of molecules: name, formula, ingredients
 * 
 * @author Seggan
 * @author Mooy1
 * 
 */
@Getter
public enum Molecule implements Ingredient {

    // Inorganic compounds

    // Radicals
    CARBONATE("Carbonate", new MoleculeIngredient(Element.CARBON), new MoleculeIngredient(Element.OXYGEN, 3)),
    SULFATE("Sulfate", new MoleculeIngredient(Element.SULFUR), new MoleculeIngredient(Element.OXYGEN, 4)),
    HYDROXIDE("Hydroxide", new MoleculeIngredient(Element.HYDROGEN), MoleculeIngredient.MONOXIDE),
    NITRATE("Nitrate", new MoleculeIngredient(Element.NITROGEN), new MoleculeIngredient(Element.OXYGEN, 3)),
    SILICATE("Silicate", new MoleculeIngredient(Element.SILICON), new MoleculeIngredient(Element.OXYGEN, 3)),

    // Oxides
    WATER("Water", new MoleculeIngredient(Element.HYDROGEN, 2), new MoleculeIngredient(Element.OXYGEN)),
    HYDROGEN_PEROXIDE("Hydrogen Peroxide", new MoleculeIngredient(Element.HYDROGEN, 2), MoleculeIngredient.DIOXIDE),
    CARBON_DIOXIDE("Carbon Dioxide", new MoleculeIngredient(Element.CARBON), MoleculeIngredient.DIOXIDE),
    SILICON_DIOXIDE("Silicon Dioxide", new MoleculeIngredient(Element.SILICON), MoleculeIngredient.DIOXIDE),

    // Ores

    // Iron
    IRON_III_OXIDE("Iron(III) Oxide (Hematite)", new MoleculeIngredient(Element.IRON, 2), new MoleculeIngredient(Element.OXYGEN, 3)),
    IRON_II_OXIDE("Iron(II,III) Oxide (Magnetite)", new MoleculeIngredient(Element.IRON, 3), new MoleculeIngredient(Element.OXYGEN, 4)),
    IRON_PERSULFIDE("Iron Persulfide (Pyrite)", new MoleculeIngredient(Element.IRON), new MoleculeIngredient(Element.SULFUR, 2)),
    COPPER_IRON_SULFIDE("Copper Iron Sulfide (Chalcopyrite)", new MoleculeIngredient(Element.COPPER), new MoleculeIngredient(Element.IRON),
        new MoleculeIngredient(Element.SULFUR)),
    // Gold
    GOLD_TELLURIDE("Gold Telluride (Calaverite)", new MoleculeIngredient(Element.GOLD), new MoleculeIngredient(Element.TELLURIUM, 2)),
    GOLD_ANTIMONIDE("Gold Antimonide (Aurostibite)", new MoleculeIngredient(Element.GOLD), new MoleculeIngredient(Element.ANTIMONY, 2)),
    // Redstone (assuming copper)
    COPPER_I_OXIDE("Copper(I) Oxide (Cuprite)", new MoleculeIngredient(Element.COPPER, 2), new MoleculeIngredient(Element.OXYGEN)),
    PENTACOPPER_IRON_TETRASULFIDE("Pentacopper(I) Iron(III) Tetrasulfide (Bornite)", new MoleculeIngredient(Element.COPPER, 5),
        new MoleculeIngredient(Element.IRON), new MoleculeIngredient(Element.SULFUR, 4)),
    COPPER_II_OXIDE("Copper(II) Oxide (Tenorite)", new MoleculeIngredient(Element.COPPER), new MoleculeIngredient(Element.OXYGEN)),
    // Netherite (assuming netherite is a seaborgium/gold alloy)
    SEABORGIUM_III_OXIDE("Seaborgium(III) Oxide", new MoleculeIngredient(Element.SEABORGIUM, 2), new MoleculeIngredient(Element.OXYGEN, 3)),
    // Emerald
    BERYLLIUM_ALUMINUM_CYCLOSILICATE("Beryllium Aluminum Cyclosilicate (Beryl)", new MoleculeIngredient(Element.BERYLLIUM, 3), new MoleculeIngredient(Element.ALUMINUM, 2), new MoleculeIngredient(Molecule.SILICATE, 6)),

    // Acids
    NITRIC_ACID("Nitric Acid", MoleculeIngredient.DIHYDROGEN, new MoleculeIngredient(Molecule.NITRATE)),
    SULFURIC_ACID("Sulfuric Acid", MoleculeIngredient.DIHYDROGEN, new MoleculeIngredient(Molecule.SULFATE)),
    CARBONIC_ACID("Carbonic Acid", MoleculeIngredient.DIHYDROGEN, new MoleculeIngredient(Molecule.CARBONATE)),

    // Organic compounds

    // Plant stuff
    CELLULOSE("Cellulose", new MoleculeIngredient(Element.CARBON, 6), new MoleculeIngredient(Element.HYDROGEN, 10),
        new MoleculeIngredient(Element.OXYGEN, 5)),
    SUCROSE("Sucrose", new MoleculeIngredient(Element.CARBON, 12), new MoleculeIngredient(Element.HYDROGEN, 22),
        new MoleculeIngredient(Element.OXYGEN, 11)),
    CHLOROPHYLL("Chlorophyll", new MoleculeIngredient(Element.CARBON, 55), new MoleculeIngredient(Element.HYDROGEN, 72),
        new MoleculeIngredient(Element.OXYGEN, 5), new MoleculeIngredient(Element.NITROGEN, 4), new MoleculeIngredient(Element.MAGNESIUM)),
    CAFFEINE("Caffeine", new MoleculeIngredient(Element.CARBON, 8), new MoleculeIngredient(Element.HYDROGEN, 10),
        new MoleculeIngredient(Element.NITROGEN, 4), new MoleculeIngredient(Element.OXYGEN, 2)),

    // Animal stuff

    // Using the amino acid methionine as a base; it's the first amino acid in all proteins
    PROTEIN("Protein", new MoleculeIngredient(Element.CARBON, 5), new MoleculeIngredient(Element.HYDROGEN, 11),
        new MoleculeIngredient(Element.NITROGEN), new MoleculeIngredient(Element.OXYGEN, 2), new MoleculeIngredient(Element.SULFUR))

    ;

    @Nonnull
    private final String name;
    @Nonnull
    private final String formula;
    @Nonnull
    private final MoleculeIngredient[] ingredients;
    @Nonnull
    private final SlimefunItemStack item;

    Molecule(@Nonnull String name, @Nonnull MoleculeIngredient... ingredients) {
        this.name = name;
        this.ingredients = ingredients;

        StringBuilder formula = new StringBuilder();

        for (MoleculeIngredient ingredient : ingredients) {
            formula.append(ingredient.getFormula());
        }

        this.formula = formula.toString();

        this.item = new SlimefunItemStack(
                "MOLECULE_" + this.name(),
                Material.DRAGON_BREATH,
                "&b" + name,
                "&7" + formula
        );
    }

    @Nonnull
    @Override
    public String getFormula(int i) {
        return (i != 1 ? "(" : "") + this.formula + (i != 1 ? ")" : "") + SubNum.fromInt(i);
    }

    public int size() {
        return this.ingredients.length;
    }

    @Nonnull
    public MoleculeIngredient getIngredient(int i) {
        return this.ingredients[i];
    }

    @Nonnull
    public ItemStack[] getRecipe() {
        ItemStack[] recipe = new ItemStack[9];

        for (int i = 0 ; i < size();) {
            ItemStack[] items = getIngredient(i).getNewItems();
            for (ItemStack item : items) {
                recipe[i] = item;
                i++;
                if (i == size()) break;
            }
        }

        return recipe;
    }
    
    @Nonnull
    @Override
    public MoleculeIngredient asIngredient(int amount) {
        return new MoleculeIngredient(this, amount);
    }

    @Nonnull
    @Override
    public MoleculeIngredient asIngredient() {
        return this.asIngredient(1);
    }
}
