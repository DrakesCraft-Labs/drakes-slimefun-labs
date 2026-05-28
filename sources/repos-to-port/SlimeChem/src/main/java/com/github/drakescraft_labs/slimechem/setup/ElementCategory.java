package com.github.drakescraft_labs.slimechem.setup;

import com.github.drakescraft_labs.slimechem.SlimeChem;
import com.github.drakescraft_labs.slimechem.implementation.atomic.Element;
import dev.drake.infinitylib.player.LeaveListener;
import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.api.items.groups.FlexItemGroup;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideImplementation;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunPlugin;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Periodic table flex category
 * 
 * @author Mooy1
 * 
 */
public final class ElementCategory extends FlexItemGroup {

    private final ItemStack BACKGROUND = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "");
    private final ChestMenu[] menus = new ChestMenu[4];
    private final Map<UUID, Integer> history = new HashMap<>();
    private final SlimefunGuideImplementation implementation = SlimefunPlugin.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE);
    
    public ElementCategory(SlimeChem plugin) {
        super(new NamespacedKey(plugin, "periodic_table"), new CustomItemStack(Material.DIAMOND, "Periodic Table"), 3);
        LeaveListener.add(this.history);
        this.menus[0] = makeTopMenu();
        this.menus[1] = makeBottomMenu();
        this.menus[2] = makeMetalMenu();
        this.menus[3] = makeRadioactiveMenu();
    }

    @Override
    public boolean isVisible(@Nonnull Player p, @Nonnull PlayerProfile profile, @Nonnull SlimefunGuideMode layout) {
        return layout != SlimefunGuideMode.CHEAT_MODE;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
        profile.getGuideHistory().add(this, 0);
        this.menus[this.history.getOrDefault(p.getUniqueId(), 0)].open(p);
    }
    
    @Nonnull
    private ChestMenu makeTopMenu() {
        ChestMenu menu = makeMenu("Periodic Table", 0);
        addElements(menu, 18, 1, 1);
        addElements(menu, 26, 2, 4);
        addElements(menu, 30, 5, 12);
        addElements(menu, 39, 13, 18);
        return menu;
    }
    
    @Nonnull
    private ChestMenu makeBottomMenu() {
        ChestMenu menu = makeMenu("Periodic Table", 1);
        addElements(menu, 9, 19, 20);
        addElementWithHandler(menu, 11, 30, 2, "Transition Metals");
        addElements(menu, 12, 31, 38);
        addElementWithHandler(menu, 20, 48, 2, "Transition Metals");
        addElements(menu, 21, 49, 56);
        addElementWithHandler(menu, 29, 80, 2, "Transition Metals");
        addElements(menu, 30, 81, 88);
        addElementWithHandler(menu, 38, 112, 2, "Transition Metals");
        addElements(menu, 39, 113, 118);
        addElements(menu, 46, 119, 120);
        return menu;
    }
    
    @Nonnull
    private ChestMenu makeMetalMenu() {
        ChestMenu menu = makeMenu("Transition Metals", 2);
        addElements(menu, 9, 21, 29);
        addElements(menu, 18, 39, 47);
        addElementWithHandler(menu, 27, 57, 3, "Lanthanoids & Actinoids");
        addElements(menu, 28, 72, 79);
        addElementWithHandler(menu, 36, 89, 3, "Lanthanoids & Actinoids");
        addElements(menu, 37, 104, 111);
        return menu;
    }
    
    @Nonnull
    private ChestMenu makeRadioactiveMenu() {
        ChestMenu menu = makeMenu("Lanthanoids & Actinoids", 3);
        addElements(menu, 9, 58, 71);
        addElements(menu, 27, 89, 103);
        return menu;
    }
    private static void addElements(ChestMenu menu, int slot, int min, int max) {
        for (int i = min ; i <= max ; i++, slot++) {
            menu.addItem(slot, makeItem(Objects.requireNonNull(Element.getByNumber(i))), ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    private void addElementWithHandler(@Nonnull ChestMenu menu, int slot, int element, int page, String name) {
        menu.addItem(slot, makeItem(Objects.requireNonNull(Element.getByNumber(element)), ChatColor.GREEN + "> Click to go to " + name), (player, i1, itemStack, clickAction) -> {
            this.menus[page].open(player);
            return false;
        });
    }
    
    @Nonnull
    private static ItemStack makeItem(Element e) {
        return new CustomItemStack(
                Objects.requireNonNull(Material.getMaterial(e.getSeries().getColor() + "_STAINED_GLASS_PANE")),
                ChatColor.AQUA + "" + e.getSymbol() + " " + e.getNumber(),
                ChatColor.AQUA + e.getName(),
                ChatColor.AQUA + "Mass: " + e.getMass()
        );
    }
    
    @Nonnull
    private static ItemStack makeItem(Element e, String lore) {
        return new CustomItemStack(
                Objects.requireNonNull(Material.getMaterial(e.getSeries().getColor() + "_STAINED_GLASS_PANE")),
                ChatColor.AQUA + "" + e.getSymbol() + " " + e.getNumber(),
                ChatColor.AQUA + e.getName(),
                ChatColor.AQUA + "Mass: " + e.getMass(),
                "",
                lore
        );
    }
    
    @Nonnull
    private ChestMenu makeMenu(@Nonnull String name, int page) {
        ChestMenu menu = new ChestMenu(name);
        
        menu.setEmptySlotsClickable(false);
        menu.setPlayerInventoryClickable(false);
        
        menu.addMenuCloseHandler(player -> this.history.put(player.getUniqueId(), page));
    
        for (int i = 0 ; i < 9 ; i++) {
            menu.addItem(i, this.BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
        }
    
        for (int i = 45 ; i < 54 ; i++) {
            menu.addItem(i, this.BACKGROUND, ChestMenuUtils.getEmptyClickHandler());
        }
        
        menu.addMenuOpeningHandler(player -> {
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
    
            menu.replaceExistingItem(0, ChestMenuUtils.getBackButton(player, "", ChatColor.GRAY + SlimefunPlugin.getLocalization().getMessage(player, "guide.back.guide")));
    
            menu.replaceExistingItem(45, ChestMenuUtils.getPreviousButton(player, page + 1, 4));
    
            menu.replaceExistingItem(53, ChestMenuUtils.getNextButton(player, page + 1, 4));
            
        });
        
        menu.addMenuClickHandler(0, (p, slot, item, action) -> {
            Optional<PlayerProfile> optional = PlayerProfile.find(p);
            optional.ifPresent(playerProfile -> playerProfile.getGuideHistory().goBack(this.implementation));
            return false;
        });
        
        menu.addMenuClickHandler(45, (p, i, itemStack, clickAction) -> {
            if (page > 0) {
                this.menus[page - 1].open(p);
            }
            return false;
        });
        
        menu.addMenuClickHandler(53, (p, i, itemStack, clickAction) -> {
            if (page < 3) {
                this.menus[page + 1].open(p);
            }
            return false;
        });
        
        return menu;
    }
}
