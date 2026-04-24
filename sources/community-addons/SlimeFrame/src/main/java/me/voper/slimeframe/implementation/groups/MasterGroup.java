package me.voper.slimeframe.implementation.groups;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.groups.FlexItemGroup;
import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.core.guide.GuideHistory;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import org.apache.commons.lang.Validate;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import lombok.Getter;
import lombok.Setter;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

@SuppressWarnings("deprecation")
public class MasterGroup extends FlexItemGroup {

    private static final int GROUP_SIZE = 36;

    @Setter
    private boolean visibility = true;
    @Getter
    private final MasterGroup parentGroup;
    private final List<ItemGroup> subGroups = new ArrayList<>();
    private final String name;

    public MasterGroup(NamespacedKey key, MasterGroup parent, ItemStack item, int tier, String name) {
        super(key, item, tier);
        this.parentGroup = parent;
        this.name = name;
        if (parent != null) {
            this.setVisibility(false);
            parent.addSubGroup(this);
        }
    }

    public MasterGroup(NamespacedKey key, MasterGroup parent, ItemStack item, String name) {
        this(key, parent, item, 3, name);
    }

    public MasterGroup(NamespacedKey key, ItemStack item, String name) {
        this(key, null, item, 3, name);
    }

    public MasterGroup(NamespacedKey key, String texture, String name) {
        this(key, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode(texture)), name), name);
    }

    public void addSubGroup(@Nonnull ItemGroup group) {
        Validate.notNull(group, "The sub item group cannot be null!");
        subGroups.add(group);
    }

    public void removeSubGroup(@Nonnull ItemGroup group) {
        Validate.notNull(group, "The sub item group cannot be null!");
        subGroups.remove(group);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        return visibility;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        openGuide(p, profile, mode, 1);
    }

    @ParametersAreNonnullByDefault
    private void openGuide(Player p, PlayerProfile profile, SlimefunGuideMode mode, int page) {
        GuideHistory history = profile.getGuideHistory();

        if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
            history.add(this, page);
        }

        ChestMenu menu = new ChestMenu(name);
        SurvivalSlimefunGuide guide = (SurvivalSlimefunGuide) Slimefun.getRegistry().getSlimefunGuide(mode);

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1));
        guide.createHeader(p, profile, menu);

        menu.addItem(1, new CustomItemStack(ChestMenuUtils.getBackButton(p, ChatColor.GRAY + Slimefun.getLocalization().getMessage(p, "guide.back.guide"))));
        menu.addMenuClickHandler(1, (pl, s, is, action) -> {

            if (hasMaster()) {
                guide.openItemGroup(profile, parentGroup, 1);
            } else {
                SlimefunGuide.openMainMenu(profile, mode, history.getMainMenuPage());
            }

            return false;
        });

        int index = 9;

        int target = (GROUP_SIZE * (page - 1)) - 1;

        while (target < (subGroups.size() - 1) && index < GROUP_SIZE + 9) {
            target++;

            ItemGroup itemGroup = subGroups.get(target);
            menu.addItem(index, itemGroup.getItem(p));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                SlimefunGuide.openItemGroup(profile, itemGroup, mode, 1);
                return false;
            });

            index++;
        }

        int pages = target == subGroups.size() - 1 ? page : (subGroups.size() - 1) / GROUP_SIZE + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openGuide(p, profile, mode, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openGuide(p, profile, mode, next);
            }

            return false;
        });

        menu.open(p);
    }

    public boolean hasMaster() {
        return parentGroup != null;
    }


}
