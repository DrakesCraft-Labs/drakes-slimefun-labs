package me.vaan.mapjammer.runnables;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.collections.Pair;
import me.vaan.mapjammer.implementation.Jammer;
import me.vaan.mapjammer.util.ConfigStorage;
import me.vaan.mapjammer.util.ShowHideInterface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

//Checks if players have the jammer and hides then if yes
public class CheckPlayers extends BukkitRunnable {

    private final ShowHideInterface showHideInterface;
    private final HashMap<String, Integer> playerMap = new HashMap<>();

    public CheckPlayers(ShowHideInterface showHideInterface) {
        this.showHideInterface = showHideInterface;
    }


    @Override
    public void run() {
        Collection<? extends Player> list = Bukkit.getOnlinePlayers();

        for (Player p : list) {
            if (p == null)
                continue;

            //hide player below underground-y
            if (ConfigStorage.UNDERGROUD_HIDE && p.getLocation().getBlockY() < ConfigStorage.UNDERGROUND_Y) {
                showHideInterface.hide(p);
                continue;
            }

            PlayerInventory inv = p.getInventory();
            Pair<Integer, Jammer> jamPair = getJammerPos(p);

            if (jamPair == null) {
                showHideInterface.show(p);
                continue;
            }

            Jammer j = jamPair.getSecondValue();
            int slot = jamPair.getFirstValue();
            ItemStack uncharge = inv.getItem(slot);

            if (j.removeItemCharge(uncharge, ConfigStorage.COST)) {
                showHideInterface.hide(p);
            }

        }
    }

    public Pair<Integer, Jammer> getJammerPos(Player p) {
        PlayerInventory pi = p.getInventory();
        String playerName = p.getName();

        //region Check cache
        Integer slot = playerMap.get(playerName);
        if (slot != null) {
            ItemStack jammerItem = pi.getItem(slot);
            Jammer possibleJammer = getJammer(jammerItem);
            //check if jammer is still there and if it has enough energy
            if (possibleJammer != null && possibleJammer.getItemCharge(jammerItem) != 0) {
                return new Pair<>(slot, possibleJammer);
            }
        }
        //endregion

        for (int i = 0; i < pi.getSize(); i++) {
            ItemStack item = pi.getItem(i);

            //If empty skip
            if (item == null)
                continue;

            //All jammers are compasses
            if (item.getType() != Material.COMPASS)
                continue;

            Jammer j = getJammer(item);
            if (j == null)
                continue;

            playerMap.put(playerName, i);
            return new Pair<>(i, j);
        }

        //Invalidate cache
        playerMap.put(playerName, null);
        return null;
    }

    public Jammer getJammer(ItemStack item) {
        if (item == null)
            return null;

        SlimefunItem sfi = SlimefunItem.getByItem(item);
        if (sfi == null)
            return null;

        if (sfi instanceof Jammer j)
            return j;

        return null;
    }
}
