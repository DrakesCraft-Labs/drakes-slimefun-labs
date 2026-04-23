package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.slimefun;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.groups.NestedItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.groups.SubItemGroup;
import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideImplementation;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import java.io.File;
import java.util.List;
import java.util.Optional;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.rykenslimefuncustomizer.bulit_in.JavaScriptEval;
import org.lins.mmmjjkx.rykenslimefuncustomizer.libraries.colors.CMIChatColor;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.ProjectAddon;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.script.ban.CommandSafe;
import org.lins.mmmjjkx.rykenslimefuncustomizer.utils.ExceptionHandler;

@SuppressWarnings("deprecation")
public class ItemGroupButton extends SubItemGroup {
    private final List<String> actions;
    private final ProjectAddon addon;

    public ItemGroupButton(
            ProjectAddon addon,
            NamespacedKey key,
            NestedItemGroup parent,
            ItemStack item,
            int tier,
            @Nullable List<String> actions) {
        super(key, parent, item, tier);

        this.addon = addon;
        this.actions = actions;
    }

    public void run(Player p, int slot, ItemStack clickedItem, ClickAction clickAction, SlimefunGuideMode mode) {
        if (actions != null) {
            for (String action : actions) {
                if (action.split(" ").length < 2) {
                    ExceptionHandler.handleWarning("Unknown action format found in item group button: " + action);
                    continue;
                }

                String type = action.split(" ")[0];
                String content = action.split(" ")[1];
                switch (type) {
                    case "link" -> {
                        p.sendMessage(CMIChatColor.translate("&eClick here: "));
                        TextComponent link = new TextComponent(content);
                        link.setColor(ChatColor.GRAY);

                        ClickEvent spigotClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, content);
                        link.setClickEvent(spigotClickEvent);

                        p.sendMessage(link);
                    }
                    case "console" -> {
                        if (CommandSafe.isBadCommand(content)) {
                            ExceptionHandler.handleDanger(
                                    "High-risk server operation detected in item group button. Please contact the author of the corresponding addon for processing!!!");
                            continue;
                        }
                        content = action.replace(type + " ", "");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content.replaceAll("%player%", p.getName()));
                    }
                    case "open_itemgroup" -> {
                        if (content.split(":").length < 2) {
                            ExceptionHandler.handleWarning(
                                    "Unknown NamespacedKey found in item group button: " + content);
                            continue;
                        }
                        String namespace = content.split(":")[0];
                        String key = content.split(":")[1];
                        int page = 1;
                        if (content.split(":").length > 2) {
                            try {
                                page = Integer.parseInt(content.split(":")[2]);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        Optional<PlayerProfile> Oprofile = PlayerProfile.find(p);
                        if (Oprofile.isEmpty()) {
                            ExceptionHandler.handleWarning(
                                    "Unable to retrieve PlayerProfile in item group button: " + p.getName());
                            continue;
                        }
                        PlayerProfile profile = Oprofile.get();
                        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
                            if (group.getKey().getNamespace().equals(namespace)
                                    && group.getKey().getKey().equals(key)) {
                                SlimefunGuideImplementation implementation =
                                        Slimefun.getRegistry().getSlimefunGuide(mode);
                                implementation.openItemGroup(profile, group, page);
                            }
                        }
                    }
                    case "display_slimefunitem" -> {
                        Optional<PlayerProfile> Oprofile = PlayerProfile.find(p);
                        if (Oprofile.isEmpty()) {
                            ExceptionHandler.handleWarning(
                                    "Unable to retrieve PlayerProfile in item group button: " + p.getName());
                            continue;
                        }
                        SlimefunItem item = SlimefunItem.getById(content);
                        if (item == null) {
                            ExceptionHandler.handleWarning(
                                    "Unknown SlimefunItem ID found in item group button: " + content);
                            continue;
                        }
                        PlayerProfile profile = Oprofile.get();
                        SlimefunGuideImplementation implementation =
                                Slimefun.getRegistry().getSlimefunGuide(mode);
                        implementation.displayItem(profile, item, true);
                    }
                    case "script" -> {
                        JavaScriptEval eval = null;
                        File file = new File(addon.getScriptsFolder(), content + ".js");
                        if (!file.exists()) {
                            ExceptionHandler.handleWarning(
                                    "Script execution issue found in item group button: File not found " + file.getName());
                        } else {
                            eval = new JavaScriptEval(file, addon);
                        }

                        if (eval != null) {
                            eval.evalFunction("onButtonGroupClick", p, slot, clickedItem, clickAction, mode);
                        }
                    }
                    default -> ExceptionHandler.handleWarning("Unknown action type found in item group button: " + action);
                }
            }
        }
    }
}
