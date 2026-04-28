package dev.j3fftw.litexpansion;

import dev.j3fftw.extrautils.objects.DyeItem;
import dev.j3fftw.litexpansion.armor.AdvancedSolarHelmet;
import dev.j3fftw.litexpansion.armor.ElectricChestplate;
import dev.j3fftw.litexpansion.items.CargoConfigurator;
import dev.j3fftw.litexpansion.items.FoodSynthesizer;
import dev.j3fftw.litexpansion.items.GlassCutter;
import dev.j3fftw.litexpansion.items.MagThor;
import dev.j3fftw.litexpansion.items.MiningDrill;
import dev.j3fftw.litexpansion.items.Thorium;
import dev.j3fftw.litexpansion.machine.Converter;
import dev.j3fftw.litexpansion.machine.Macerator;
import dev.j3fftw.litexpansion.machine.MassFabricator;
import dev.j3fftw.litexpansion.machine.MultiFunctionalElectricStorageUnit;
import dev.j3fftw.litexpansion.machine.MultiFunctionalStorageUnit;
import dev.j3fftw.litexpansion.machine.Recycler;
import dev.j3fftw.litexpansion.machine.RubberSynthesizer;
import dev.j3fftw.litexpansion.machine.UUCrafter;
import dev.j3fftw.litexpansion.machine.generators.AdvancedSolarPanel;
import dev.j3fftw.litexpansion.machine.generators.Generator;
import dev.j3fftw.litexpansion.machine.multiblock.ManualMill;
import dev.j3fftw.litexpansion.machine.multiblock.MetalForge;
import dev.j3fftw.litexpansion.machine.multiblock.RefinedSmeltery;
import dev.j3fftw.litexpansion.weapons.NanoBlade;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreCrusher;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

final class ItemSetup {

    static final ItemSetup INSTANCE = new ItemSetup();
    private final ItemStack glass = new ItemStack(Material.GLASS);
    private final SlimefunAddon plugin = LiteXpansion.getInstance();
    private boolean initialised;
    private ItemStack rubberItem;

    private ItemSetup() {}

    public void init() {
        if (initialised) {
            return;
        }

        initialised = true;

        registerTools();
        registerMachines();
        registerRubber();
        registerMiscItems();
        registerEndgameItems();
        registerCarbonStuff();
        registerSolarPanels();
        registerSolarHelmets();
    }

    private void registerTools() {
        new CargoConfigurator().register(plugin);
        new GlassCutter().register(plugin);
        new MiningDrill(MiningDrill.Type.MINING).register(plugin);
        new MiningDrill(MiningDrill.Type.DIAMOND).register(plugin);
    }

    private void registerMachines() {
        new FoodSynthesizer().register(plugin);
        new Recycler().register(plugin);
        new MassFabricator().register(plugin);
        new RefinedSmeltery().register(plugin);
        new MetalForge().register(plugin);
        new MultiFunctionalElectricStorageUnit().register(plugin);
        new MultiFunctionalStorageUnit().register(plugin);
        new Generator().register(plugin);
        new ManualMill().register(plugin);
        new Macerator().register(plugin);
        new UUCrafter().register(plugin);
        new Converter().register(plugin);
    }

    // Register only if SlimyTreeTaps and SlimeTech do not exist
    private void registerRubber() {
        if (Bukkit.getPluginManager().isPluginEnabled("SlimyTreeTaps")) {
            this.rubberItem = SlimefunItem.getById("RUBBER").getItem();
        } else if (Bukkit.getPluginManager().isPluginEnabled("SlimeTech")) {
            this.rubberItem = SlimefunItem.getById("SLIMETECH_RUBBER").getItem();
        } else {
            this.rubberItem = Items.RUBBER.item();
            registerNonPlaceableItem(Items.RUBBER, RubberSynthesizer.RECIPE_TYPE, SlimefunItems.OIL_BUCKET.item());
            new RubberSynthesizer().register(plugin);
        }
    }

    private void registerMiscItems() {
        // Advanced Alloy
        registerNonPlaceableItem(Items.ADVANCED_ALLOY, RecipeType.COMPRESSOR, Items.MIXED_METAL_INGOT.item());

        // Mixed Metal Ingot
        registerItem(Items.MIXED_METAL_INGOT, MetalForge.RECIPE_TYPE,
            Items.REFINED_IRON.item(), Items.REFINED_IRON.item(), Items.REFINED_IRON.item(),
            SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.BRONZE_INGOT.item(), SlimefunItems.BRONZE_INGOT.item(),
            SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item(), SlimefunItems.TIN_INGOT.item()
        );

        // Reinforced glass
        registerNonPlaceableItem(Items.REINFORCED_GLASS, RecipeType.ENHANCED_CRAFTING_TABLE,
            glass, glass, glass,
            Items.ADVANCED_ALLOY.item(), glass, Items.ADVANCED_ALLOY.item(),
            glass, glass, glass
        );

        // Machine block
        registerItem(Items.MACHINE_BLOCK, MetalForge.RECIPE_TYPE,
            Items.REFINED_IRON.item(), Items.REFINED_IRON.item(), Items.REFINED_IRON.item(),
            Items.REFINED_IRON.item(), null, Items.REFINED_IRON.item(),
            Items.REFINED_IRON.item(), Items.REFINED_IRON.item(), Items.REFINED_IRON.item()
        );

        // Advanced Machine Block
        registerItem(Items.ADVANCED_MACHINE_BLOCK, MetalForge.RECIPE_TYPE,
            null, Items.ADVANCED_ALLOY.item(), null,
            Items.CARBON_PLATE.item(), Items.MACHINE_BLOCK.item(), Items.CARBON_PLATE.item(),
            null, Items.CARBON_PLATE.item(), null
        );

        registerNonPlaceableItem(Items.TIN_PLATE, MetalForge.RECIPE_TYPE, SlimefunItems.TIN_INGOT.item());

        registerNonPlaceableItem(Items.TIN_ITEM_CASING, ManualMill.RECIPE_TYPE, Items.TIN_PLATE.item());

        registerNonPlaceableItem(Items.UNINSULATED_TIN_CABLE, 3,
            ManualMill.RECIPE_TYPE, Items.TIN_ITEM_CASING.item()
        );

        registerNonPlaceableItem(Items.TIN_CABLE, RecipeType.ENHANCED_CRAFTING_TABLE,
            this.rubberItem, Items.UNINSULATED_TIN_CABLE.item()
        );

        registerNonPlaceableItem(Items.COPPER_PLATE, ManualMill.RECIPE_TYPE, SlimefunItems.COPPER_INGOT.item());

        registerNonPlaceableItem(Items.COPPER_ITEM_CASING, ManualMill.RECIPE_TYPE, Items.COPPER_PLATE.item());

        registerNonPlaceableItem(Items.UNINSULATED_COPPER_CABLE, 3,
            ManualMill.RECIPE_TYPE, Items.COPPER_ITEM_CASING.item()
        );

        registerNonPlaceableItem(Items.COPPER_CABLE, RecipeType.ENHANCED_CRAFTING_TABLE,
            this.rubberItem, Items.UNINSULATED_COPPER_CABLE.item()
        );

        registerItem(Items.RE_BATTERY, RecipeType.ENHANCED_CRAFTING_TABLE,
            null, Items.TIN_CABLE.item(), null,
            Items.TIN_ITEM_CASING.item(), new ItemStack(Material.REDSTONE), Items.TIN_ITEM_CASING.item(),
            Items.TIN_ITEM_CASING.item(), new ItemStack(Material.REDSTONE), Items.TIN_ITEM_CASING.item()
        );
        registerNonPlaceableItem(Items.GOLD_PLATE, MetalForge.RECIPE_TYPE, new ItemStack(Material.GOLD_INGOT));

        registerNonPlaceableItem(Items.GOLD_ITEM_CASING, ManualMill.RECIPE_TYPE, Items.GOLD_PLATE.item());

        registerNonPlaceableItem(Items.UNINSULATED_GOLD_CABLE, 3,
            ManualMill.RECIPE_TYPE, Items.GOLD_ITEM_CASING.item()
        );

        registerNonPlaceableItem(Items.GOLD_CABLE, RecipeType.ENHANCED_CRAFTING_TABLE,
            this.rubberItem, Items.UNINSULATED_GOLD_CABLE.item()
        );

        registerNonPlaceableItem(Items.IRON_PLATE, MetalForge.RECIPE_TYPE, new ItemStack(Material.IRON_INGOT));
        registerNonPlaceableItem(Items.IRON_ITEM_CASING, ManualMill.RECIPE_TYPE, Items.IRON_PLATE.item());
        registerNonPlaceableItem(Items.DIAMOND_PLATE, MetalForge.RECIPE_TYPE, new ItemStack(Material.DIAMOND));
        registerNonPlaceableItem(Items.THORIUM_PLATE, MetalForge.RECIPE_TYPE, Items.THORIUM.item());


        // Circuits
        registerNonPlaceableItem(Items.ELECTRONIC_CIRCUIT, RecipeType.ENHANCED_CRAFTING_TABLE,
            Items.COPPER_CABLE.item(), Items.COPPER_CABLE.item(), Items.COPPER_CABLE.item(),
            new ItemStack(Material.REDSTONE), Items.REFINED_IRON.item(), new ItemStack(Material.REDSTONE),
            Items.COPPER_CABLE.item(), Items.COPPER_CABLE.item(), Items.COPPER_CABLE.item()
        );

        registerNonPlaceableItem(Items.ADVANCED_CIRCUIT, RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack(Material.REDSTONE), new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE),
            new ItemStack(Material.GLOWSTONE_DUST), Items.ELECTRONIC_CIRCUIT.item(), new ItemStack(Material.GLOWSTONE_DUST),
            new ItemStack(Material.REDSTONE), new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE)
        );


        registerItem(Items.LAPOTRON_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack(Material.LAPIS_LAZULI), Items.ADVANCED_CIRCUIT.item(), new ItemStack(Material.LAPIS_LAZULI),
            new ItemStack(Material.LAPIS_LAZULI), SlimefunItems.POWER_CRYSTAL.item(), new ItemStack(Material.LAPIS_LAZULI),
            new ItemStack(Material.LAPIS_LAZULI), Items.ADVANCED_CIRCUIT.item(), new ItemStack(Material.LAPIS_LAZULI)
        );

        registerItem(Items.POWER_UNIT, RecipeType.ENHANCED_CRAFTING_TABLE,
            Items.RE_BATTERY.item(), Items.UNINSULATED_COPPER_CABLE.item(), Items.IRON_ITEM_CASING.item(),
            Items.RE_BATTERY.item(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.item(), SlimefunItems.ELECTRIC_MOTOR.item(),
            Items.RE_BATTERY.item(), Items.UNINSULATED_COPPER_CABLE.item(), Items.IRON_ITEM_CASING.item()
        );

        // Refined crap
        registerNonPlaceableItem(Items.REFINED_IRON, RefinedSmeltery.RECIPE_TYPE,
            new ItemStack(Material.IRON_INGOT)
        );

        // Dust smelting
        RecipeType.SMELTERY.register(new ItemStack[] {Items.LAPIS_DUST.item()},
            new ItemStack(Material.LAPIS_LAZULI)
        );
        RecipeType.SMELTERY.register(new ItemStack[] {Items.REDSTONE_DUST.item()},
            new ItemStack(Material.REDSTONE)
        );
        RecipeType.SMELTERY.register(new ItemStack[] {Items.DIAMOND_DUST.item()},
            new ItemStack(Material.DIAMOND)
        );
        RecipeType.SMELTERY.register(new ItemStack[] {Items.EMERALD_DUST.item()},
            new ItemStack(Material.EMERALD)
        );
        RecipeType.SMELTERY.register(new ItemStack[] {Items.QUARTZ_DUST.item()},
            new ItemStack(Material.QUARTZ)
        );
        RecipeType.SMELTERY.register(new ItemStack[] {Items.ANCIENT_DEBRIS_DUST.item()},
            new ItemStack(Material.NETHERITE_INGOT)
        );

        // Resources
        new MagThor().register(plugin);
        new Thorium().register(plugin);
    }

    private void registerEndgameItems() {
        registerNonPlaceableItem(Items.SCRAP, Recycler.RECIPE_TYPE, CustomItemStack.create(Material.COBBLESTONE,
            "\u00a77Any Item!"));
        new DyeItem(Items.LITEXPANSION, Items.UU_MATTER, MassFabricator.RECIPE_TYPE,
            createSingleItemRecipeCentered(Items.SCRAP.item())).register(plugin);
        new DyeItem(Items.LITEXPANSION, Items.IRIDIUM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.UU_MATTER.item(), Items.UU_MATTER.item(), Items.UU_MATTER.item(),
            null, Items.UU_MATTER.item(), null,
            Items.UU_MATTER.item(), Items.UU_MATTER.item(), Items.UU_MATTER.item()
        }).register(plugin);
        registerNonPlaceableItem(Items.IRIDIUM_PLATE, MetalForge.RECIPE_TYPE,
            Items.IRIDIUM.item(), Items.ADVANCED_ALLOY.item(), Items.IRIDIUM.item(),
            Items.ADVANCED_ALLOY.item(), new ItemStack(Material.DIAMOND), Items.ADVANCED_ALLOY.item(),
            Items.IRIDIUM.item(), Items.ADVANCED_ALLOY.item(), Items.IRIDIUM.item()
        );

        new NanoBlade().register(plugin);
        new ElectricChestplate().register(plugin);
    }

    private void registerCarbonStuff() {
        new DyeItem(Items.LITEXPANSION, Items.COAL_DUST, RecipeType.ORE_CRUSHER,
            createSingleItemRecipe(new ItemStack(Material.COAL))).register(plugin);
        new DyeItem(Items.LITEXPANSION, Items.RAW_CARBON_FIBRE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.COAL_DUST.item(), Items.COAL_DUST.item(), null,
            Items.COAL_DUST.item(), Items.COAL_DUST.item(), null,
            null, null, null
        }).register(plugin);

        new DyeItem(Items.LITEXPANSION, Items.RAW_CARBON_MESH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.RAW_CARBON_FIBRE.item(), Items.RAW_CARBON_FIBRE.item(), null,
            null, null, null,
            null, null, null
        }).register(plugin);

        registerNonPlaceableItem(Items.CARBON_PLATE, RecipeType.COMPRESSOR, Items.RAW_CARBON_MESH.item());
    }

    private void registerSolarPanels() {
        new AdvancedSolarPanel(AdvancedSolarPanel.Type.ADVANCED).register(plugin);
        new AdvancedSolarPanel(AdvancedSolarPanel.Type.HYBRID).register(plugin);
        new AdvancedSolarPanel(AdvancedSolarPanel.Type.ULTIMATE).register(plugin);
    }

    private void registerSolarHelmets() {
        new AdvancedSolarHelmet(AdvancedSolarHelmet.Type.ADVANCED).register(plugin);
        new AdvancedSolarHelmet(AdvancedSolarHelmet.Type.CARBONADO).register(plugin);
        new AdvancedSolarHelmet(AdvancedSolarHelmet.Type.ENERGIZED).register(plugin);
        new AdvancedSolarHelmet(AdvancedSolarHelmet.Type.HYBRID).register(plugin);
        new AdvancedSolarHelmet(AdvancedSolarHelmet.Type.ADVANCEDLX).register(plugin);
        new AdvancedSolarHelmet(AdvancedSolarHelmet.Type.ULTIMATE).register(plugin);
    }

    //Register Items
    private void registerItem(@Nonnull SlimefunItemStack result, @Nonnull RecipeType type,
                              @Nonnull ItemStack... items) {
        ItemStack[] recipe;
        if (items.length == 1) {
            recipe = new ItemStack[] {
                null, null, null,
                null, items[0], null,
                null, null, null
            };
            new SlimefunItem(Items.LITEXPANSION, result, type, recipe).register(plugin);

            return;
        }

        if (items.length < 9) {
            recipe = new ItemStack[9];
            System.arraycopy(items, 0, recipe, 0, items.length);
        } else {
            recipe = items;
        }

        new SlimefunItem(Items.LITEXPANSION, result, type, recipe).register(plugin);
    }

    private void registerNonPlaceableItem(@Nonnull SlimefunItemStack item, @Nonnull RecipeType type,
                                          @Nonnull ItemStack... items) {
        registerNonPlaceableItem(item, 1, type, items);
    }

    /**
     * Register an item with an output amount greater than 1
     */
    private void registerNonPlaceableItem(@Nonnull SlimefunItemStack item, int amount, @Nonnull RecipeType type,
                                          @Nonnull ItemStack... items) {
        ItemStack[] recipe;

        if (items.length < 9) {
            recipe = new ItemStack[9];
            System.arraycopy(items, 0, recipe, 0, items.length);
        } else {
            recipe = items;
        }

        new UnplaceableBlock(Items.LITEXPANSION, item, type, recipe, item.item().asQuantity(amount)).register(plugin);
    }


    /**
     * Builds and ItemStack array that centers
     * the item in the guide. Only to be
     * used for display recipes that have no effect,
     * i.e. UU Matter in the Mass Fabricator
     *
     * @param item is the item shown in the guide
     * @return a prebuilt recipe ItemStack array
     */
    private ItemStack[] createSingleItemRecipeCentered(ItemStack item) {
        return new ItemStack[] {null, null, null, null, item, null, null, null, null};
    }

    /**
     * Builds an ItemStack array for shapeless
     * recipe machines, like the {@link OreCrusher}
     *
     * @param item is the item shown in the guide
     * @return a prebuilt recipe ItemStack array
     */
    private ItemStack[] createSingleItemRecipe(ItemStack item) {
        return new ItemStack[] {item, null, null, null, null, null, null, null, null};
    }
}
