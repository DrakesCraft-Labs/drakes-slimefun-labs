package me.voper.slimeframe.implementation.groups;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.drakescraft_labs.slimefun4.api.items.groups.FlexItemGroup;
import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.core.guide.GuideHistory;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import me.voper.slimeframe.utils.Colors;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

@ParametersAreNonnullByDefault
public class TutorialsGroup extends FlexItemGroup {

    private static final int GUIDE_BACK = 1;

    private static final int RELICS_TUTORIAL = 9;
    private static final int GENERATORS_TUTORIAL = 10;
    private static final int SPECIAL_ORES_TUTORIAL = 11;

    private static final int[] FOOTER = new int[]{
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    private final boolean visibility = true;

    protected TutorialsGroup(NamespacedKey key, ItemStack item) {
        super(key, item);
        Groups.MAIN_GROUP.addSubGroup(this);
    }

    @Override
    public boolean isVisible(Player player, PlayerProfile playerProfile, SlimefunGuideMode slimefunGuideMode) {
        return visibility;
    }

    @Override
    public void open(Player player, PlayerProfile playerProfile, SlimefunGuideMode slimefunGuideMode) {
        this.openGuide(player, playerProfile, slimefunGuideMode, 1);
    }

    private void openGuide(Player player, PlayerProfile profile, SlimefunGuideMode mode, int page) {
        GuideHistory history = profile.getGuideHistory();
        if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
            history.add(this, page);
        }

        ChestMenu menu = new ChestMenu(Colors.CRAYOLA_BLUE + "SlimeFrame Tutorials");
        SurvivalSlimefunGuide guide = (SurvivalSlimefunGuide) Slimefun.getRegistry().getSlimefunGuide(mode);

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));
        guide.createHeader(player, profile, menu);

        for (int slot : FOOTER) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), (player1, i1, itemStack, clickAction) -> false);
        }

        // Back
        menu.replaceExistingItem(GUIDE_BACK, ChestMenuUtils.getBackButton(player, Slimefun.getLocalization().getMessage("guide.back.guide")));
        menu.addMenuClickHandler(GUIDE_BACK, (player1, slot, itemStack, clickAction) -> {
            SlimefunGuide.openItemGroup(profile, Groups.MAIN_GROUP, mode, 1);
            return false;
        });

        // Relics Tutorial
        menu.replaceExistingItem(RELICS_TUTORIAL, new CustomItemStack(Material.ENCHANTED_BOOK, Colors.BRONZE + "Relics Tutorial"));
        menu.addMenuClickHandler(RELICS_TUTORIAL, ((player1, i, itemStack, clickAction) -> {
            player1.getInventory().addItem(getRelicsTutorial());
            return false;
        }));

        // Generators Tutorial
        menu.replaceExistingItem(GENERATORS_TUTORIAL, new CustomItemStack(Material.ENCHANTED_BOOK, Colors.BRONZE + "Energy Generators Tutorial"));
        menu.addMenuClickHandler(GENERATORS_TUTORIAL, ((player1, i, itemStack, clickAction) -> {
            player1.getInventory().addItem(getGeneratorsTutorial());
            return false;
        }));
        menu.open(player);

        // Special Ores Farm
        menu.replaceExistingItem(SPECIAL_ORES_TUTORIAL, new CustomItemStack(Material.ENCHANTED_BOOK, Colors.BRONZE + "New Resources Farm Tutorial"));
        menu.addMenuClickHandler(SPECIAL_ORES_TUTORIAL, ((player1, i, itemStack, clickAction) -> {
            player1.getInventory().addItem(getSpecialOresTutorial());
            return false;
        }));
        menu.open(player);
    }

    @Nonnull
    private ItemStack getRelicsTutorial() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta itemMeta = (BookMeta) book.getItemMeta();
        itemMeta.setAuthor("Voper");
        itemMeta.setTitle(ChatColor.BLUE + "Relics Tutorial");

        ArrayList<BaseComponent[]> components = new ArrayList<>();
        components.add(new ComponentBuilder("What are Relics for?\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("Relics are used to obtain Prime components for crafting Prime-tier machines. Each relic has 3 Bronze rewards, 2 Silver rewards, and 1 Gold reward.").underlined(false).color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("How to obtain Relics?\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("You can check the Relics tab, but basically, there are four types of Relics: Lith, Meso, Neo, and Axi. You get Lith by fishing, Meso by killing mobs, Neo by breaking blocks, and Axi by placing blocks.").underlined(false).color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("How to open Relics?\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("Hold ONE Relic in your off hand and kill endermans until your Relic's reactant count reaches 10. After that, right-click with the Relic in hand to receive your reward.").underlined(false).color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("Bedrock Players\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("If you are a Bedrock player, the Relic must be in the first slot of your hotbar to obtain the reactants.").underlined(false).color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("Void Traces\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("Void Traces are used to refine Relics and increase chances of obtaining rarer items. You earn traces each time you open a Relic. To check your trace count, use /sframe traces.").underlined(false).color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("How to refine Relics?\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("To refine a Relic, use the command /sframe refine <refinement>. The chance of obtaining rarer items increases depending on the refinement level.").underlined(false).color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("Refinements\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("EXCEPTIONAL - requires 25 Void Traces\n\nFLAWLESS - requires 50 Void Traces\n\nRADIANT - requires 100 Void Traces").underlined(false).color(ChatColor.BLACK)
                .create());

        itemMeta.spigot().setPages(components);
        book.setItemMeta(itemMeta);
        return book;
    }

    @Nonnull
    private ItemStack getGeneratorsTutorial() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta itemMeta = (BookMeta) book.getItemMeta();
        itemMeta.setAuthor("Voper");
        itemMeta.setTitle(ChatColor.BLUE + "Energy Generators Tutorial");

        ArrayList<BaseComponent[]> components = new ArrayList<>();
        components.add(new ComponentBuilder("Cumulative Generators\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("Some of SlimeFrame's energy generators have an additional attribute: ").color(ChatColor.BLACK).underlined(false)
                .append("Bonus Energy.").bold(true)
                .create());

        components.add(new ComponentBuilder("Bonus Energy\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("The generator will check if the blocks around, above, and below it are also cumulative generators. ").color(ChatColor.BLACK).underlined(false)
                .append("For each cumulative generator found, the bonus energy will be added to the total generated energy.")
                .create());

        components.add(new ComponentBuilder("Example\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("If a generator has 1000J of bonus energy and 2000J of standard energy, and all the blocks around it are cumulative generators, the total generated energy will be 8000J (6 * 1000J + 2000J).").color(ChatColor.BLACK).underlined(false)
                .create());

        itemMeta.spigot().setPages(components);
        book.setItemMeta(itemMeta);
        return book;
    }

    @Nonnull
    private ItemStack getSpecialOresTutorial() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta itemMeta = (BookMeta) book.getItemMeta();
        itemMeta.setAuthor("Voper");
        itemMeta.setTitle(ChatColor.BLUE + "New Resources Farm Tutorial");

        ArrayList<BaseComponent[]> components = new ArrayList<>();
        components.add(new ComponentBuilder("New Ores\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("In the resources tab, you can find new \"ores\" obtained using the new pickaxe (Nosam Pickaxe). ").color(ChatColor.BLACK).underlined(false)
                .append("Currently, there's only one way to create a farm for these resources.")
                .create());

        components.add(new ComponentBuilder("The Auto Trader\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("To create a farm for these new ores, you'll need to build a new machine: the ").color(ChatColor.BLACK).underlined(false)
                .append("Auto Trader").bold(true)
                .append(". This machine can automatically trade with villagers.").bold(false)
                .create());

        components.add(new ComponentBuilder("The Contracts\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("For the Auto Trader to work, you'll need to put a valid contract inside it. ").color(ChatColor.BLACK).underlined(false)
                .append("These contracts can be obtained from the ")
                .append("Merchant Soul Contract.").bold(true)
                .create());

        components.add(new ComponentBuilder("Trading with Villagers\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("Once you have the previous items, you'll need to ").color(ChatColor.BLACK).underlined(false)
                .append("find a villager offering one of the new addon resources. ")
                .append("The chance is low, and the villager's profession doesn't matter; it can be any.")
                .create());

        components.add(new ComponentBuilder("Final Step\n\n").color(Colors.CRAYOLA_BLUE).underlined(true)
                .append("When you find a villager offering the required trade, ").color(ChatColor.BLACK).underlined(false)
                .append("right-click on them with the Merchant Contract in hand. ")
                .append("Attention: ").color(ChatColor.DARK_RED)
                .append("this will kill them!").color(ChatColor.BLACK)
                .create());

        components.add(new ComponentBuilder("With the valid contract and the Auto Trader in hand, simply insert it into ")
                .append("the machine and select the trade inside. After that, just supply the machine ")
                .append("with the necessary resources!")
                .create());

        itemMeta.spigot().setPages(components);
        book.setItemMeta(itemMeta);
        return book;
    }

}