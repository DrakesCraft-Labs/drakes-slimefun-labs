/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package me.poma123.globalwarming.eventos;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class Silenciados {
    private final Set<UUID> silenciados = new HashSet<UUID>();
    private final File fichero;

    public Silenciados(@Nonnull GlobalWarmingPlugin plugin) {
        this.fichero = new File(plugin.getDataFolder(), "silenciados.yml");
        this.cargar();
    }

    private void cargar() {
        if (!this.fichero.exists()) {
            return;
        }
        YamlConfiguration datos = YamlConfiguration.loadConfiguration((File)this.fichero);
        for (String bruto : datos.getStringList("silenciados")) {
            try {
                this.silenciados.add(UUID.fromString(bruto));
            }
            catch (IllegalArgumentException illegalArgumentException) {}
        }
    }

    private void guardar() {
        YamlConfiguration datos = new YamlConfiguration();
        datos.set("silenciados", this.silenciados.stream().map(UUID::toString).toList());
        try {
            datos.save(this.fichero);
        }
        catch (IOException e) {
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, e, () -> "No se pudo guardar la lista de jugadores que silenciaron el clima");
        }
    }

    public boolean estaSilenciado(@Nonnull Player p) {
        return this.silenciados.contains(p.getUniqueId());
    }

    public boolean alternar(@Nonnull Player p) {
        boolean ahoraSilenciado;
        if (this.silenciados.remove(p.getUniqueId())) {
            ahoraSilenciado = false;
        } else {
            this.silenciados.add(p.getUniqueId());
            ahoraSilenciado = true;
        }
        this.guardar();
        return ahoraSilenciado;
    }
}

