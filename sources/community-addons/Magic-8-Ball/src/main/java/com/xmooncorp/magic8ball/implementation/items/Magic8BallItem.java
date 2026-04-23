package com.xmooncorp.magic8ball.implementation.items;

import com.xmooncorp.magic8ball.Magic8Ball;
import com.xmooncorp.magic8ball.core.ConfigBasedLocalization;
import com.xmooncorp.magic8ball.utils.LocationUtils;
import com.xmooncorp.magic8ball.utils.compatibility.VersionedParticle;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class Magic8BallItem extends SlimefunItem implements Listener {

    private final Random random = new Random(System.currentTimeMillis());
    ConfigBasedLocalization localization = Magic8Ball.instance().localization();

    private final String[] affirmative = localization.getStringList("responses.affirmative").toArray(new String[0]);
    private final String[] noncommittal = localization.getStringList("responses.noncommittal").toArray(new String[0]);
    private final String[] negative = localization.getStringList("responses.negative").toArray(new String[0]);

    public Magic8BallItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onPlayerInteractBlock());
    }

    @Override
    public void preRegister(){
        Magic8Ball plugin = Magic8Ball.instance();
        plugin.getServer().getPluginManager().registerEvents(this, Magic8Ball.instance());
    }

    public void sendRandom8BallMessage(@Nonnull Player player, @Nullable Block block) {
        String[] randomMessage = getRandomMessage();
        String color = "§r";
        Sound sound = Sound.INTENTIONALLY_EMPTY;
        Particle particle = Particle.PORTAL;
        switch (randomMessage[0]) {
            case "affirmative" -> {
                color = "§a";
                sound = Sound.ENTITY_VILLAGER_YES;
                particle = VersionedParticle.TOTEM_OF_UNDYING;
            }
            case "noncommittal" -> {
                color = "§7";
                sound = Sound.ENTITY_VILLAGER_TRADE;
                particle = VersionedParticle.CRIT;
            }
            case "negative" -> {
                color = "§c";
                sound = Sound.ENTITY_VILLAGER_NO;
                particle = VersionedParticle.ENCHANTED_HIT;
            }
        }
        sendActionBarMessage(player, color + randomMessage[1]);
        playSoundAtLocation(player.getWorld(), player.getLocation(), sound);

        Location handLocation = LocationUtils.getFrontSide(LocationUtils.getRightSide(player.getEyeLocation(), 0.325).subtract(0, 0.7, 0), 0.6);
        if (block != null) {
            createParticleAtLocation(block.getWorld(), block.getLocation(), particle);
        } else {
            createParticleAtLocation(player.getWorld(), handLocation, particle);
        }
    }

    @Nonnull
    private String[] getRandomMessage() {
        switch (random.nextInt(3)) {
            case 0 -> {
                return new String [] {"affirmative", affirmative[random.nextInt(affirmative.length)] };
            }
            case 1 -> {
                return new String [] {"noncommittal", noncommittal[random.nextInt(noncommittal.length)] };
            }
            case 2 -> {
                return new String [] {"negative", negative[random.nextInt(negative.length)] };
            }
            default -> {
                return new String[2];
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void sendActionBarMessage(@Nonnull Player player, @Nonnull String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
    }

    private void createParticleAtLocation(@Nonnull World world, @Nonnull Location location, @Nonnull Particle particle) {
        world.spawnParticle(particle, location, 20);
    }

    private void playSoundAtLocation(@Nonnull World world, @Nonnull Location location, @Nonnull Sound sound) {
        world.playSound(location, sound, 1, 0.65f);
    }

    @SuppressWarnings("unused")
    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();

        SlimefunItem itemHeld = SlimefunItem.getByItem(event.getItem());
        if (itemHeld != null && itemHeld.getId().equals(this.getId())) {
            sendRandom8BallMessage(player, null);
        }
    }

    private BlockUseHandler onPlayerInteractBlock () {
        return playerRightClickEvent -> {
            Player player = playerRightClickEvent.getPlayer();
            Optional<Block> block = playerRightClickEvent.getClickedBlock();
            block.ifPresent(value -> sendRandom8BallMessage(player, value));
        };
    }
}
