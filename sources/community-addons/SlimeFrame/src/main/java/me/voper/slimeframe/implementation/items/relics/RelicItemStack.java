package me.voper.slimeframe.implementation.items.relics;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.HeadTextures;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
@ParametersAreNonnullByDefault
public class RelicItemStack extends SlimefunItemStack {

    private final Relic.Era relicEra;
    private final SlimefunItemStack[] commonDrops;
    private final SlimefunItemStack[] uncommonDrops;
    private final SlimefunItemStack rareDrop;

    public RelicItemStack(ItemStack item, String name, Relic.Era era, SlimefunItemStack[] commonDrops, SlimefunItemStack[] uncommonDrops, SlimefunItemStack rareDrop) {
        super("WF_" + era.name() + "_" + ChatColor.stripColor(name).split(" ")[1], item, (itemMeta -> {
            ChatColor nameColor = switch (era) {
                case LITH -> ChatColor.WHITE;
                case MESO -> ChatColor.YELLOW;
                case NEO -> ChatColor.GREEN;
                default -> ChatColor.LIGHT_PURPLE;
            };

            itemMeta.setDisplayName(nameColor + name);
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.AQUA + "Reactants: " + ChatColor.WHITE + "0/10");
            lore.add(ChatColor.AQUA + "Refinement: " + ChatColor.WHITE + Relic.Refinement.INTACT.name());
            lore.add("");
            lore.add(Colors.BRONZE + String.valueOf(ChatColor.BOLD) + "Common rewards:");
            for (SlimefunItemStack commonDrop : commonDrops) {
                lore.add(ChatColor.WHITE + ChatColor.stripColor(commonDrop.getDisplayName()));
            }
            lore.add("");
            lore.add(Colors.SILVER + String.valueOf(ChatColor.BOLD) + "Uncommon rewards:");
            for (SlimefunItemStack uncommonDrop : uncommonDrops) {
                lore.add(ChatColor.WHITE + ChatColor.stripColor(uncommonDrop.getDisplayName()));
            }
            lore.add("");
            lore.add(Colors.GOLD_2 + String.valueOf(ChatColor.BOLD) + "Rare reward:");
            lore.add(ChatColor.WHITE + ChatColor.stripColor(rareDrop.getDisplayName()));
            itemMeta.setLore(lore);
        }));

        this.relicEra = era;
        this.commonDrops = commonDrops;
        this.uncommonDrops = uncommonDrops;
        this.rareDrop = rareDrop;
    }

    public RelicItemStack(String name, Relic.Era era, SlimefunItemStack[] commonDrops, SlimefunItemStack[] uncommonDrops, SlimefunItemStack rareDrop) {
        this(getTextureByEra(era), name, era, commonDrops, uncommonDrops, rareDrop);
    }

    private static ItemStack getTextureByEra(Relic.Era era) {
        return switch (era) {
            case LITH -> HeadTextures.getSkull(HeadTextures.LITH_RELIC);
            case MESO -> HeadTextures.getSkull(HeadTextures.MESO_RELIC);
            case NEO -> HeadTextures.getSkull(HeadTextures.NEO_RELIC);
            default -> HeadTextures.getSkull(HeadTextures.AXI_RELIC);
        };
    }

}
