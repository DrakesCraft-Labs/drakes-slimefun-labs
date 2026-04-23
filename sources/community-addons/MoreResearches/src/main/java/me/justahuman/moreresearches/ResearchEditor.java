package me.justahuman.moreresearches;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.researches.Research;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.libraries.dough.skins.PlayerHead;
import com.github.drakescraft-labs.slimefun4.libraries.dough.skins.PlayerSkin;
import com.github.drakescraft-labs.slimefun4.utils.ChatUtils;
import com.github.drakescraft-labs.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class ResearchEditor {
    private static final Map<UUID, Consumer<String>> CALLBACKS = new HashMap<>();
    private static final int[] FOOTER_BACKGROUND = { 45, 47, 51, 53 };
    private static final int PAGE_SIZE = 45;

    // Head From: https://minecraft-heads.com/custom-heads/head/9382-red-x
    private static final ItemStack RED_X = PlayerHead.getItemStack(PlayerSkin.fromHashCode("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7"));
    // Head From: https://minecraft-heads.com/custom-heads/head/8768-black-plus
    private static final ItemStack BLACK_PLUS = PlayerHead.getItemStack(PlayerSkin.fromHashCode("9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777"));
    // Head From: https://minecraft-heads.com/custom-heads/head/21771-lime-checkmark
    private static final ItemStack LIME_CHECKMARK = PlayerHead.getItemStack(PlayerSkin.fromHashCode("a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6"));

    public static void openMainMenu(Player player) {
        openMainMenu(player, 1);
    }

    public static void openMainMenu(Player player, int page) {
        ChestMenu menu = new ChestMenu(Utils.translated("editor.main-page.title"));

        FileConfiguration config = MoreResearches.getInstance().getConfig();
        ConfigurationSection researches = config.getConfigurationSection("researches") == null
                ? config.createSection("researches")
                : config.getConfigurationSection("researches");

        List<String> researchIds = researches.getKeys(false).stream()
                .filter(key -> Objects.nonNull(researches.getConfigurationSection(key)))
                .collect(Collectors.toList());
        int researchCount = researchIds.size();
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, researchCount);
        int pages = (int) Math.max(1, Math.ceil(researchCount / (double) PAGE_SIZE));
        researchIds.sort(Comparator.naturalOrder());

        int i = 0;
        List<String> pageResearchIds = researchIds.subList(start, end);

        for (String researchId : pageResearchIds) {
            ConfigurationSection researchConfig = researches.getConfigurationSection(researchId);
            String displayName = researchConfig.getString("display-name", "Error: No Display Name Provided");
            List<String> itemIds = researchConfig.getStringList("slimefun-items");
            ItemStack display = itemIds.stream()
                    .map(SlimefunItem::getById)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(SlimefunItem::getItem)
                    .orElse(new ItemStack(Material.BARRIER));

            menu.addItem(i, new CustomItemStack(
                    display,
                    "&f" + displayName + " &7(&e" + researchId + "&7)",
                    Utils.translated("editor.main-page.edit-research")
            ));

            menu.addMenuClickHandler(i, (o1, o2, o3, o4) -> {
                openResearchEditor(player, researchId);
                return false;
            });

            i++;
        }

        ChestMenuUtils.drawBackground(menu, FOOTER_BACKGROUND);

        menu.addItem(46, ChestMenuUtils.getPreviousButton(player, page, pages));
        menu.addMenuClickHandler(46, (o1, o2, o3, o4) -> {
            if (page > 1) {
                openMainMenu(player, page - 1);
            }
            return false;
        });

        menu.addItem(48, Utils.translatedStack(RED_X, "editor.main-page.discard-changes"));
        menu.addMenuClickHandler(48, (o1, o2, o3, o4) -> {
            player.closeInventory();
            MoreResearches.getInstance().reloadConfig();
            Utils.loadResearches();
            return false;
        });

        menu.addItem(49, Utils.translatedStack(BLACK_PLUS, "editor.main-page.new-research"));
        menu.addMenuClickHandler(49, (o1, o2, o3, o4) -> {
            String randomId = UUID.randomUUID().toString();
            ConfigurationSection researchConfig = researches.createSection(randomId);
            researchConfig.set("legacy-id", 0);
            researchConfig.set("display-name", "[Set Me!]");
            researchConfig.set("exp-cost", 0);
            researchConfig.set("slimefun-items", new ArrayList<>());
            openResearchEditor(player, randomId);
            return false;
        });

        menu.addItem(50, Utils.translatedStack(LIME_CHECKMARK, "editor.main-page.save-changes"));
        menu.addMenuClickHandler(50, (o1, o2, o3, o4) -> {
            player.closeInventory();
            MoreResearches.getInstance().saveConfig();
            Utils.loadResearches();
            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(player, page, pages));
        menu.addMenuClickHandler(52, (o1, o2, o3, o4) -> {
            if (page < pages) {
                openMainMenu(player, page + 1);
            }
            return false;
        });

        menu.open(player);
    }

    public static void openResearchEditor(Player player, String researchId) {
        FileConfiguration config = MoreResearches.getInstance().getConfig();
        ConfigurationSection researchConfig = config.getConfigurationSection("researches." + researchId);
        if (researchConfig == null) {
            return;
        }

        ConfigurationSection researches = config.getConfigurationSection("researches");
        if (researches == null) {
            return;
        }

        ChestMenu menu = new ChestMenu(Utils.translated("editor.research.title"));

        menu.addItem(1, Utils.translatedStack(Material.ANVIL, "editor.research.edit-id", researchId));
        menu.addMenuClickHandler(1, (o1, o2, o3, o4) -> {
            player.closeInventory();
            Utils.send(player, "editor.research.edit-id.prompt");
            ChatUtils.awaitInput(player, newId -> {
                if (researches.getKeys(false).contains(newId)) {
                    Utils.send(player, "warnings.editor.existing-id");
                    menu.open(player);
                    return;
                } else if (newId.isBlank()) {
                    Utils.send(player, "warnings.editor.blank-id");
                    menu.open(player);
                    return;
                } else if (!newId.matches("^[a-z0-9_]+$")) {
                    Utils.send(player, "warnings.editor.invalid-id", newId);
                    menu.open(player);
                    return;
                }
                researches.set(newId, researchConfig);
                researches.set(researchId, null);
                openResearchEditor(player, newId);
            });
            return false;
        });

        menu.addItem(3, Utils.translatedStack(Material.CLOCK, "editor.research.edit-legacy-id", researchConfig.getInt("legacy-id", -1)));
        menu.addMenuClickHandler(3, (o1, o2, stack, o4) -> {
            player.closeInventory();
            Utils.send(player, "editor.research.edit-legacy-id.prompt");
            ChatUtils.awaitInput(player, newLegacyId -> {
                int legacyId;
                try {
                    legacyId = Integer.parseInt(newLegacyId);
                } catch (NumberFormatException e) {
                    Utils.send(player, "warnings.editor.invalid-number", newLegacyId);
                    menu.open(player);
                    return;
                }

                if (Slimefun.getRegistry().getResearches().stream().map(Research::getID).anyMatch(id -> id.equals(legacyId))
                        || researches.getValues(true).containsValue(legacyId)) {
                    Utils.send(player, "warnings.editor.existing-legacy-id");
                    menu.open(player);
                    return;
                }
                researchConfig.set("legacy-id", legacyId);
                Utils.updateStack(stack, "editor.research.edit-legacy-id", legacyId);
                menu.open(player);
            });
            return false;
        });

        menu.addItem(5, Utils.translatedStack(Material.NAME_TAG, "editor.research.edit-name", researchConfig.getString("display-name", "Error: No Display Name Provided")));
        menu.addMenuClickHandler(5, (o1, o2, stack, o4) -> {
            player.closeInventory();
            Utils.send(player, "editor.research.edit-name.prompt");
            ChatUtils.awaitInput(player, newName -> {
                if (newName.isBlank()) {
                    Utils.send(player, "warnings.editor.blank-name");
                    menu.open(player);
                    return;
                }
                researchConfig.set("display-name", newName);
                Utils.updateStack(stack, "editor.research.edit-name", newName);
                menu.open(player);
            });
            return false;
        });

        menu.addItem(7, Utils.translatedStack(Material.EXPERIENCE_BOTTLE, "editor.research.edit-exp-cost", researchConfig.getInt("exp-cost", 0)));
        menu.addMenuClickHandler(7, (o1, o2, stack, o4) -> {
            player.closeInventory();
            Utils.send(player, "editor.research.edit-exp-cost.prompt");
            ChatUtils.awaitInput(player, newExpCost -> {
                int expCost;
                try {
                    expCost = Integer.parseInt(newExpCost);
                } catch (NumberFormatException e) {
                    Utils.send(player, "warnings.editor.invalid-number", newExpCost);
                    menu.open(player);
                    return;
                }

                if (expCost < 0) {
                    Utils.send(player, "warnings.editor.negative-exp-cost");
                    menu.open(player);
                    return;
                }

                researchConfig.set("exp-cost", expCost);
                Utils.updateStack(stack, "editor.research.edit-exp-cost", expCost);
                menu.open(player);
            });
            return false;
        });

        menu.addItem(9, Utils.translatedStack(RED_X, "editor.research.delete"));
        menu.addMenuClickHandler(9, (o1, o2, o3, o4) -> {
            player.closeInventory();
            TextComponent message = new TextComponent(Utils.translated("editor.research.delete.prompt"));
            TextComponent confirm = new TextComponent(Utils.translated("editor.research.delete.confirm"));
            confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mr editor confirm"));
            confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                    new TextComponent(Utils.translated("editor.research.delete.confirm-hover"))
            }));

            TextComponent cancel = new TextComponent(Utils.translated("editor.research.delete.cancel"));
            cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mr cancel"));
            cancel.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                    new TextComponent(Utils.translated("editor.research.delete.cancel-hover"))
            }));

            message.addExtra(confirm);
            message.addExtra(cancel);
            player.spigot().sendMessage(message);

            CALLBACKS.put(player.getUniqueId(), result -> {
                if (result.equals("confirm")) {
                    researches.set(researchId, null);
                    openMainMenu(player);
                } else {
                    menu.open(player);
                }
            });

            return false;
        });

        menu.addItem(13, setupIdsStack(new ItemStack(Material.CHEST), researchConfig));
        menu.addMenuClickHandler(13, new ChestMenu.AdvancedMenuClickHandler() {
            @Override
            public boolean onClick(Player o1, int o2, ItemStack stack, ClickAction action) {
                if (action.isRightClicked() && action.isShiftClicked()) {
                    researchConfig.set("slimefun-items", new ArrayList<>());
                    setupIdsStack(stack, researchConfig);
                } else if (action.isRightClicked()) {
                    player.closeInventory();
                    Utils.send(player, "editor.research.edit-items.prompt-remove");
                    ChatUtils.awaitInput(player, id -> {
                        List<String> itemIds = researchConfig.getStringList("slimefun-items");
                        if (itemIds.remove(id)) {
                            researchConfig.set("slimefun-items", itemIds);
                            setupIdsStack(stack, researchConfig);
                        } else {
                            Utils.send(player, "warnings.editor.item-id-not-found");
                        }
                        menu.open(player);
                    });
                } else {
                    player.closeInventory();
                    Utils.send(player, "editor.research.edit-items.prompt-add");
                    ChatUtils.awaitInput(player, id -> {
                        if (SlimefunItem.getById(id) == null) {
                            Utils.send(player, "warnings.editor.invalid-item-id", id);
                            menu.open(player);
                            return;
                        }

                        List<String> itemIds = researchConfig.getStringList("slimefun-items");
                        if (itemIds.contains(id)) {
                            Utils.send(player, "warnings.editor.duplicate-item-id", id);
                            menu.open(player);
                            return;
                        }

                        itemIds.add(id);
                        researchConfig.set("slimefun-items", itemIds);
                        setupIdsStack(stack, researchConfig);
                        menu.open(player);
                    });
                }
                return false;
            }

            @Override
            public boolean onClick(InventoryClickEvent event, Player o2, int o3, ItemStack cursor, ClickAction action) {
                if (cursor != null && !cursor.getType().isAir()) {
                    Optional<String> id = Slimefun.getItemDataService().getItemData(cursor);
                    if (id.isEmpty()) {
                        Utils.send(player, "warnings.editor.not-slimefun-item");
                        return false;
                    }

                    List<String> itemIds = researchConfig.getStringList("slimefun-items");
                    if (itemIds.contains(id.get())) {
                        Utils.send(player, "warnings.editor.duplicate-item-id", id.get());
                        return false;
                    }

                    itemIds.add(id.get());
                    researchConfig.set("slimefun-items", itemIds);
                    setupIdsStack(event.getCurrentItem(), researchConfig);
                    return false;
                }
                return onClick(o2, o3, event.getCurrentItem(), action);
            }
        });

        menu.addItem(17,Utils.translatedStack(LIME_CHECKMARK, "editor.research.done"));
        menu.addMenuClickHandler(17, (o1, o2, o3, o4) -> {
            openMainMenu(player);
            return false;
        });

        menu.setEmptySlotsClickable(false);
        menu.setPlayerInventoryClickable(true);
        menu.open(player);
    }

    public static ItemStack setupIdsStack(ItemStack itemStack, ConfigurationSection researchConfig) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            String name = Utils.translated("editor.research.edit-items.name");
            List<String> idLore = Utils.translatedList("editor.research.edit-items.lore");
            int index = idLore.indexOf("{current}");
            idLore.remove(index);
            idLore.addAll(index, Utils.compressIds(researchConfig.getStringList("slimefun-items")));
            meta.setDisplayName(name);
            meta.setLore(idLore);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public static void handleCallback(Player player, String result) {
        if (CALLBACKS.containsKey(player.getUniqueId())) {
            CALLBACKS.remove(player.getUniqueId()).accept(result);
        }
    }
}
