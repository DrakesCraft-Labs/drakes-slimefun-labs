package com.github.drakescraft_labs.slimefun4.core.commands.subcommands;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import dev.drake.dough.common.CommonPatterns;
import dev.drake.dough.common.PlayerList;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.core.commands.SlimefunCommand;
import com.github.drakescraft_labs.slimefun4.core.commands.SubCommand;
import com.github.drakescraft_labs.slimefun4.core.multiblocks.MultiBlockMachine;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

class GiveCommand extends SubCommand {

    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_ITEM = "%item%";
    private static final String PLACEHOLDER_AMOUNT = "%amount%";
    private static final String SFMASTER_CHEAT_PERMISSION = "slimefun.cheat.items";
    private static final String SFMASTER_ACTIVE_PERMISSION = "odysseia.sfmaster.active";
    private static final String SFMASTER_BYPASS_PERMISSION = "odysseia.sfmaster.bypass_marking";
    private static final String SFMASTER_MARKER_LORE = ChatColor.RED + "Generado por SFMaster - No comerciable";

    @ParametersAreNonnullByDefault
    GiveCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "give", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.cheat.items") || !(sender instanceof Player)) {
            if (args.length > 2) {
                Optional<Player> player = PlayerList.findByName(args[1]);

                if (player.isPresent()) {
                    Player p = player.get();

                    SlimefunItem sfItem = SlimefunItem.getById(args[2].toUpperCase(Locale.ROOT));

                    if (sfItem != null) {
                        giveItem(sender, p, sfItem, args);
                    } else {
                        Slimefun.getLocalization().sendMessage(sender, "messages.invalid-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, args[2]));
                    }
                } else {
                    Slimefun.getLocalization().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]));
                }
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    private void giveItem(CommandSender sender, Player p, SlimefunItem sfItem, String[] args) {
        if (sfItem instanceof MultiBlockMachine) {
            Slimefun.getLocalization().sendMessage(sender, "guide.cheat.no-multiblocks");
        } else {
            int amount = parseAmount(args);

            if (amount > 0) {
                Slimefun.getLocalization().sendMessage(p, "messages.given-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
                ItemStack grantedItem = new CustomItemStack(sfItem.getItem(), amount);
                markAsSfMasterItem(sender, p, grantedItem);
                Map<Integer, ItemStack> excess = p.getInventory().addItem(grantedItem);
                if (Slimefun.getCfg().getBoolean("options.drop-excess-sf-give-items") && !excess.isEmpty()) {
                    for (ItemStack is : excess.values()) {
                        p.getWorld().dropItem(p.getLocation(), is);
                    }
                }

                Slimefun.getLocalization().sendMessage(sender, "messages.give-item", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]).replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.invalid-amount", true, msg -> msg.replace(PLACEHOLDER_AMOUNT, args[3]));
            }
        }
    }

    private int parseAmount(String[] args) {
        int amount = 1;

        if (args.length == 4) {
            if (CommonPatterns.NUMERIC.matcher(args[3]).matches()) {
                amount = Integer.parseInt(args[3]);
            } else {
                return 0;
            }
        }

        return amount;
    }

    private void markAsSfMasterItem(CommandSender sender, Player recipient, ItemStack item) {
        boolean senderHasSfMaster = sender instanceof Player player
                && (player.hasPermission(SFMASTER_ACTIVE_PERMISSION) || player.hasPermission(SFMASTER_CHEAT_PERMISSION));
        boolean recipientHasSfMaster = recipient.hasPermission(SFMASTER_ACTIVE_PERMISSION) || recipient.hasPermission(SFMASTER_CHEAT_PERMISSION);
        boolean bypassMarking = sender instanceof Player player && player.hasPermission(SFMASTER_BYPASS_PERMISSION);

        if ((!senderHasSfMaster && !recipientHasSfMaster) || bypassMarking) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        NamespacedKey key = NamespacedKey.fromString("odysseia:sfmaster_item");
        if (key == null) {
            return;
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        if (!lore.contains(SFMASTER_MARKER_LORE)) {
            lore.add("");
            lore.add(SFMASTER_MARKER_LORE);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

}
