package me.char321.sfadvancements.vanilla;

import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.util.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.roxeez.advancement.AdvancementManager;
import net.roxeez.advancement.display.FrameType;
import net.roxeez.advancement.display.Icon;
import net.roxeez.advancement.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VanillaHook {
    private AdvancementManager vanillaManager;
    /** True solo tras {@link #pushVanillaAdvancements()} sin excepción (createAll cargó los logros en Bukkit). */
    private boolean initialized = false;

    public boolean isVanillaIntegrationReady() {
        return initialized;
    }

    public void init() {
        if (initialized) {
            return;
        }
        this.vanillaManager = new AdvancementManager(SFAdvancements.instance());
        pushVanillaAdvancements();
        Utils.listen(new PlayerJoinListener());
        Utils.listen(new AdvancementListener());
        initialized = true;
    }

    public void reload() {
        if (!initialized) {
            init();
            return;
        }

        pushVanillaAdvancements();

        for (Player p : Bukkit.getOnlinePlayers()) {
            syncProgress(p);
        }
    }

    private void pushVanillaAdvancements() {
        vanillaManager.clearAdvancements();
        registerGroups(vanillaManager);
        registerAdvancements(vanillaManager);
        vanillaManager.createAll(true);
    }

    private static void registerGroups(AdvancementManager manager) {
        for (AdvancementGroup group : SFAdvancements.getRegistry().getAdvancementGroups()) {
            manager.register(context -> {
                net.roxeez.advancement.Advancement vadvancement = new net.roxeez.advancement.Advancement(Utils.keyOf(group.getId()));

                vadvancement.setDisplay(display -> {
                    ItemStack item = group.getDisplayItem();
                    String background = group.getBackground();
                    ItemMeta meta = item.getItemMeta();
                    //noinspection DataFlowIssue
                    display.setTitle(meta.getDisplayName());
                    List<String> lore = meta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }
                    display.setDescription(String.join("\n", lore));
                    display.setIcon(new Icon(item));
                    display.setFrame(FrameType.valueOf(group.getFrameType()));
                    display.setBackground(NamespacedKey.minecraft("textures/block/" + background.toLowerCase() + ".png"));
                    display.setAnnounce(false);
                });

                vadvancement.addCriteria("impossible", TriggerType.IMPOSSIBLE, a -> {});

                return vadvancement;
            });
        }
    }

    private static void registerAdvancements(AdvancementManager manager) {
        for (Map.Entry<NamespacedKey, Advancement> entry : SFAdvancements.getRegistry().getAdvancements().entrySet()) {
            registerAdvancement(manager, entry.getValue());
        }
    }

    private static void registerAdvancement(AdvancementManager manager, Advancement advancement) {
        if (manager == null) return;
        if (advancement == null) return;

        //TODO optimize
        if (manager.getAdvancements().stream().anyMatch(vadv -> vadv.getKey().equals(advancement.getKey()))) return;
        //do i even need to do this?
        if (manager.getAdvancements().stream().noneMatch(vadv -> vadv.getKey().equals(advancement.getParent()))) {
            Advancement parent = Utils.fromKey(advancement.getParent());
            if (parent != null) {
                registerAdvancement(manager, parent);
            }
        }

        manager.register(context -> {
            net.roxeez.advancement.Advancement vadvancement = new net.roxeez.advancement.Advancement(advancement.getKey());

            vadvancement.setDisplay(display -> {
                ItemStack item = advancement.getDisplay();
                ItemMeta meta = item.getItemMeta();
                BaseComponent title;
                String description;
                //noinspection DataFlowIssue
                if (meta.hasDisplayName()) {
                    title = new TextComponent(meta.getDisplayName());
                } else {
                    title = Utils.getItemName(item);
                }
                description = getDescriptionFor(meta.getLore(), advancement);
                display.setTitle(title);
                display.setDescription(description);
                display.setIcon(new Icon(item));
                display.setFrame(FrameType.valueOf(advancement.getFrameType()));
                display.setHidden(advancement.isHidden());
                display.setAnnounce(false);
            });

            vadvancement.setParent(advancement.getParent());
            vadvancement.addCriteria("impossible", TriggerType.IMPOSSIBLE, a -> {});

            return vadvancement;
        });
    }

    private static String getDescriptionFor(List<String> lore, Advancement adv) {
        lore = lore == null ? new ArrayList<>() : new ArrayList<>(lore);
        for (int i = lore.size() - 1; i >= 0; i--) {
            if ("%criteria%".equals(lore.get(i))) {
                lore.remove(i);
                lore.addAll(i, getCriteriaLore(adv));
                return String.join("\n", lore);
            }
        }
        return String.join("\n", lore);
    }

    private static List<String> getCriteriaLore(Advancement adv) {
        List<String> res = new ArrayList<>();
        for (Criterion criterion : adv.getCriteria()) {
            res.add("§7" + criterion.getName());
        }
        return res;
    }

    public void syncProgress(Player p) {
        if (!initialized) {
            return;
        }
        for (AdvancementGroup group : SFAdvancements.getRegistry().getAdvancementGroups()) {
            complete(p, Utils.keyOf(group.getId()));
        }
        for (Advancement adv : SFAdvancements.getRegistry().getAdvancements().values()) {
            if (SFAdvancements.getAdvManager().isCompleted(p, adv)) {
                complete(p, adv.getKey());
            } else {
                revoke(p, adv.getKey());
            }
        }
    }

    public void complete(Player p, NamespacedKey key) {
        if (!initialized) {
            return;
        }
        org.bukkit.advancement.Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            SFAdvancements.logger().fine("Omitiendo complete: logro vanilla no cargado " + key);
            return;
        }
        Utils.runSync(() -> p.getAdvancementProgress(advancement).awardCriteria("impossible"));
    }

    public void revoke(Player p, NamespacedKey key) {
        if (!initialized) {
            return;
        }
        org.bukkit.advancement.Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            SFAdvancements.logger().fine("Omitiendo revoke: logro vanilla no cargado " + key);
            return;
        }
        Utils.runSync(() -> p.getAdvancementProgress(advancement).revokeCriteria("impossible"));

    }
}
