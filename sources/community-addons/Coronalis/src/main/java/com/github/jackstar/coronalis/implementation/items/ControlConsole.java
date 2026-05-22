package com.github.jackstar.coronalis.implementation.items;

import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.discovery.DiscoveryService;
import com.github.jackstar.coronalis.implementation.Items;
import com.github.jackstar.coronalis.implementation.data.CelestialTarget;
import com.github.jackstar.coronalis.implementation.data.CoronalisNetwork;
import com.github.jackstar.coronalis.implementation.data.TelescopeState;
import com.github.jackstar.coronalis.managers.AccessManager;
import com.github.jackstar.coronalis.managers.CosmicEventManager;
import com.github.jackstar.coronalis.managers.SoundManager;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.protection.Interaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Consola de Control del Observatorio Coronalis.
 *
 * <h3>Características:</h3>
 * <ul>
 *   <li><b>Sistema de tiers:</b> EASY → LEGENDARY con requisitos de telescopios escalonados.</li>
 *   <li><b>Auto-ticker:</b> alinea PID y correlaciona señales automáticamente cada 2s.</li>
 *   <li><b>Modo manual:</b> botones SLEW, REPAIR y CORRELATE operables por el jugador.</li>
 *   <li><b>Eventos cósmicos:</b> CosmicEventManager modifica XP, velocidad y bloqueos.</li>
 *   <li><b>Panel de evento cósmico:</b> muestra el evento activo en tiempo real.</li>
 *   <li><b>Fallas mecánicas:</b> MOTOR_STUCK / PID_OVERLOAD / BEARING_FAILURE.</li>
 * </ul>
 *
 * <h3>Layout del ChestMenu (54 slots):</h3>
 * <pre>
 *  Fila 0: [B][B][TELE][TARGET][TELM][PID][FAULT][EVENT][B]
 *  Fila 1: [B][T0][T1][T2][T3][T4][T5][T6][B]          ← objetivos
 *  Fila 2: [B][B][B][B][B][B][B][B][B]
 *  Fila 3: [B][SLEW][B][REPAIR][B][AUTO][B][CORR][B]
 *  Fila 4: [B][B][B][B][B][B][B][B][B]
 *  Fila 5: [B][B][B][IN][B][OUT][B][B][B]
 * </pre>
 */
public class ControlConsole extends SlimefunItem implements InventoryBlock, EnergyNetComponent {

    // ── Slots ────────────────────────────────────────────────────────────────

    private static final int INPUT_SLOT  = 48;
    private static final int OUTPUT_SLOT = 50;

    private static final int PANEL_TELESCOPES = 2;
    private static final int PANEL_TARGET     = 3;
    private static final int PANEL_TELEMETRY  = 4;
    private static final int PANEL_PID        = 5;
    private static final int PANEL_FAULT      = 6;
    private static final int PANEL_EVENT      = 7;   // ← NUEVO: evento cósmico

    private static final int BUTTON_SLEW      = 28;
    private static final int BUTTON_REPAIR    = 30;
    private static final int BUTTON_AUTO      = 32;  // ← NUEVO: toggle auto
    private static final int BUTTON_CORRELATE = 34;
    private static final int BUTTON_CALIBRATE = 38;
    private static final int BUTTON_INVITE    = 42;
    private static final int BUTTON_PASSWORD  = 44;

    private static final int[] BORDER_SLOTS = buildBorder();

    private static int[] buildBorder() {
        Set<Integer> active = new HashSet<>(Arrays.asList(
            PANEL_TELESCOPES, PANEL_TARGET, PANEL_TELEMETRY, PANEL_PID, PANEL_FAULT, PANEL_EVENT,
            INPUT_SLOT, OUTPUT_SLOT,
            BUTTON_SLEW, BUTTON_REPAIR, BUTTON_AUTO, BUTTON_CORRELATE,
            BUTTON_CALIBRATE, BUTTON_INVITE, BUTTON_PASSWORD
        ));
        for (CelestialTarget t : CelestialTarget.values()) active.add(t.menuSlot);
        List<Integer> border = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            if (!active.contains(i)) border.add(i);
        }
        return border.stream().mapToInt(Integer::intValue).toArray();
    }

    // ── Parámetros PID ───────────────────────────────────────────────────────

    private static final double KP               = 0.45;
    private static final double MAX_STEP_AZ      = 25.0;
    private static final double MAX_STEP_EL      = 20.0;
    private static final double ALIGNED_THRESHOLD = 0.1;

    // ── Parámetros de correlación ────────────────────────────────────────────

    /** Progreso base por clic manual (%). */
    private static final int MANUAL_BASE = 25;
    /** Progreso adicional por telescopio en manual. */
    private static final int MANUAL_PER_SCOPE = 5;
    /** Progreso base por tick automático (cada 2s). */
    private static final int AUTO_BASE = 8;
    /** Progreso adicional por telescopio en auto. */
    private static final int AUTO_PER_SCOPE = 3;

    /** 1 en N probabilidad de falla mecánica normal. */
    private static final int FAULT_CHANCE_NORMAL = 10;
    /** 1 en N probabilidad de falla con Tormenta Magnética activa. */
    private static final int FAULT_CHANCE_STORM  = 4;

    private static final String[] FAULT_TYPES = { "MOTOR_STUCK", "PID_OVERLOAD", "BEARING_FAILURE" };
    private static final int ENERGY_CAPACITY_J = 8192;
    private static final int JOULES_PER_SU = 4;
    private static final int ENERGY_IMPORT_PER_TICK_J = 512;

    // ── Registro de consolas activas ─────────────────────────────────────────

    /** Ubicaciones de todas las Consolas de Control activas en el servidor. */
    public static final Set<Location> ACTIVE_CONSOLES =
        Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final Random random = new Random();

    // ── Constructor ──────────────────────────────────────────────────────────

    public ControlConsole(@Nonnull ItemGroup itemGroup,
                          @Nonnull SlimefunItemStack item,
                          @Nonnull RecipeType recipeType,
                          @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    // ── Registro ─────────────────────────────────────────────────────────────

    @Override
    public void postRegister() {
        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                setupStaticMenu(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                Location loc = b.getLocation();
                ACTIVE_CONSOLES.add(loc);
                initBlockStorage(loc);
                Coronalis.instance().getNetworkRegistry().getOrCreate(loc);
                Coronalis.instance().getNetworkRegistry().rebuildNetwork(loc);
                updateMenuVisuals(menu, loc);
                registerHandlers(menu, loc);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(@Nonnull DirtyChestMenu menu,
                                                         @Nonnull ItemTransportFlow flow,
                                                         @Nullable ItemStack item) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return new int[]{ OUTPUT_SLOT };
                }
                if (flow != ItemTransportFlow.INSERT || item == null) {
                    return new int[0];
                }
                SlimefunItem sfIn = SlimefunItem.getByItem(item);
                return sfIn != null && sfIn.getId().equals("CORONALIS_DATA_CELL")
                    ? new int[]{ INPUT_SLOT }
                    : new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(@Nonnull ItemTransportFlow flow) {
                return new int[0];
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                if (!p.hasPermission("slimefun.inventory.bypass")
                    && !com.github.drakescraft_labs.slimefun4.implementation.Slimefun
                        .getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK)) {
                    return false;
                }
                Location loc = b.getLocation();
                AccessManager access = Coronalis.instance().getAccessManager();
                access.claimOwnership(loc, p);
                AccessManager.AccessResult result = access.checkAccess(loc, p);
                if (!access.isGranted(result)) {
                    access.showAccessDenied(p, loc, result);
                    return false;
                }
                if (!claimOperator(loc, p)) {
                    return false;
                }
                Coronalis.instance().getSoundManager().startAmbient(p);
                return true;
            }
        };
    }

    // ── Menú estático ────────────────────────────────────────────────────────

    private void setupStaticMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(ChestMenuUtils.getBackground(), BORDER_SLOTS);
        preset.drawBackground(ChestMenuUtils.getInputSlotTexture(),  new int[]{ INPUT_SLOT  - 1, INPUT_SLOT  + 1 });
        preset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), new int[]{ OUTPUT_SLOT - 1, OUTPUT_SLOT + 1 });

        for (CelestialTarget t : CelestialTarget.values()) {
            preset.addItem(t.menuSlot, new CustomItemStack(
                Material.ORANGE_CONCRETE,
                t.tier.color + "§l" + t.displayName,
                "§7Azimut: §e" + t.az + "°  §7Elevación: §e" + t.el + "°",
                "§7Descripción: §f" + t.description,
                "",
                t.tier.label,
                "§7Mínimo telescopios: §e" + t.getMinTelescopes(),
                "",
                "§dClic para fijar como objetivo."
            ));
        }
    }

    // ── Handlers de clic ─────────────────────────────────────────────────────

    private void registerHandlers(@Nonnull BlockMenu menu, @Nonnull Location loc) {
        for (CelestialTarget t : CelestialTarget.values()) {
            menu.addMenuClickHandler(t.menuSlot, (player, slot, item, action) -> {
                onSelectTarget(menu, loc, t, player);
                return false;
            });
        }
        menu.addMenuClickHandler(BUTTON_SLEW, (player, slot, item, action) -> {
            onManualSlew(menu, loc, player);
            return false;
        });
        menu.addMenuClickHandler(BUTTON_REPAIR, (player, slot, item, action) -> {
            onRepair(menu, loc, player);
            return false;
        });
        menu.addMenuClickHandler(BUTTON_AUTO, (player, slot, item, action) -> {
            onToggleAuto(menu, loc, player);
            return false;
        });
        menu.addMenuClickHandler(BUTTON_CORRELATE, (player, slot, item, action) -> {
            onCorrelate(menu, loc, player);
            return false;
        });
        menu.addMenuClickHandler(BUTTON_CALIBRATE, (player, slot, item, action) -> {
            onCalibrate(menu, loc, player);
            return false;
        });
        menu.addMenuClickHandler(BUTTON_INVITE, (player, slot, item, action) -> {
            Coronalis.instance().getAccessManager().promptInvite(player, loc);
            return false;
        });
        menu.addMenuClickHandler(BUTTON_PASSWORD, (player, slot, item, action) -> {
            Coronalis.instance().getAccessManager().promptSetPassword(player, loc);
            return false;
        });
    }

    // ── BlockStorage ─────────────────────────────────────────────────────────

    private static void initBlockStorage(@Nonnull Location loc) {
        if (BlockStorage.getLocationInfo(loc, "current_az") != null) return;
        BlockStorage.addBlockInfo(loc, "current_az",          "0.0");
        BlockStorage.addBlockInfo(loc, "current_el",          "0.0");
        BlockStorage.addBlockInfo(loc, "target_az",           "0.0");
        BlockStorage.addBlockInfo(loc, "target_el",           "0.0");
        BlockStorage.addBlockInfo(loc, "selected_target",     "Ninguno");
        BlockStorage.addBlockInfo(loc, "fault_state",         "NORMAL");
        BlockStorage.addBlockInfo(loc, "correlation_progress","0");
        BlockStorage.addBlockInfo(loc, "auto_mode",           "false");
        BlockStorage.addBlockInfo(loc, "connected_telescopes", String.valueOf(countTelescopesAt(loc)));
        BlockStorage.addBlockInfo(loc, "cc_operator",         "");
    }

    // ── Auto-ticker (llamado desde Coronalis#onEnable scheduler) ─────────────

    /**
     * Procesa todas las Consolas con modo automático activo.
     * Llamado cada 40 ticks (2 segundos) desde el hilo principal.
     */
    public static void tickAll() {
        Iterator<Location> it = ACTIVE_CONSOLES.iterator();
        while (it.hasNext()) {
            Location loc = it.next();
            if (loc.getWorld() == null || BlockStorage.check(loc.getBlock()) == null) {
                it.remove();
                continue;
            }
            if (!"true".equals(BlockStorage.getLocationInfo(loc, "auto_mode"))) continue;
            BlockMenu menu = BlockStorage.getInventory(loc);
            if (menu == null) continue;
            try {
                autoTick(loc, menu);
            } catch (Exception e) {
                Coronalis.instance().getLogger().log(
                    Level.WARNING, "[Coronalis] Error en auto-tick " + loc, e);
            }
        }
    }

    private static void autoTick(@Nonnull Location loc, @Nonnull BlockMenu menu) {
        if (!"NORMAL".equals(getStr(loc, "fault_state"))) return;
        CoronalisNetwork network = Coronalis.instance().getNetworkRegistry().getOrCreate(loc);
        importExternalEnergy(loc, network);
        if (network.getTelescopeCount() == 0 || network.getCalibratedCount() < network.getTelescopeCount()) {
            updateMenuVisuals(menu, loc);
            return;
        }

        double curAz = getDbl(loc, "current_az");
        double curEl = getDbl(loc, "current_el");
        double tarAz = getDbl(loc, "target_az");
        double tarEl = getDbl(loc, "target_el");

        double diffAz = tarAz - curAz;
        double diffEl = tarEl - curEl;
        double dist   = Math.sqrt(diffAz * diffAz + diffEl * diffEl);

        // 1. Si no está alineado: avanzar PID
        if (dist >= ALIGNED_THRESHOLD) {
            if (!network.drainSU(CoronalisNetwork.SU_COST_SLEW)) {
                Coronalis.instance().getSoundManager().playEnergyLow(loc);
                updateMenuVisuals(menu, loc);
                return;
            }
            double stepAz = clamp(diffAz * KP, -MAX_STEP_AZ, MAX_STEP_AZ);
            double stepEl = clamp(diffEl * KP, -MAX_STEP_EL, MAX_STEP_EL);
            BlockStorage.addBlockInfo(loc, "current_az", String.valueOf(round1(curAz + stepAz)));
            BlockStorage.addBlockInfo(loc, "current_el", String.valueOf(round1(curEl + stepEl)));
            updateMenuVisuals(menu, loc);
            return;
        }

        // 2. Alineado: intentar auto-correlación
        CosmicEventManager ev = Coronalis.instance().getCosmicEventManager();
        if (ev.isSignalBlocked()) { updateMenuVisuals(menu, loc); return; }

        ItemStack input = menu.getItemInSlot(INPUT_SLOT);
        SlimefunItem sfIn = SlimefunItem.getByItem(input);
        if (sfIn == null || !sfIn.getId().equals("CORONALIS_DATA_CELL")) return;

        int scopes = getInt(BlockStorage.getLocationInfo(loc, "connected_telescopes"), 0);
        if (!network.drainSU(CoronalisNetwork.SU_COST_CORRELATE)) {
            Coronalis.instance().getSoundManager().playEnergyLow(loc);
            updateMenuVisuals(menu, loc);
            return;
        }
        int inc    = (int) Math.round((AUTO_BASE + scopes * AUTO_PER_SCOPE) * ev.getCorrelationSpeedMult());
        int prog   = Math.min(100, getInt(BlockStorage.getLocationInfo(loc, "correlation_progress"), 0) + inc);
        BlockStorage.addBlockInfo(loc, "correlation_progress", String.valueOf(prog));

        if (prog >= 100) {
            finalizeCorrelation(loc, menu, null);
        } else {
            updateMenuVisuals(menu, loc);
        }
    }

    // ── Acciones manuales ────────────────────────────────────────────────────

    private void onSelectTarget(@Nonnull BlockMenu menu, @Nonnull Location loc,
                                @Nonnull CelestialTarget target, @Nonnull Player player) {
        touchOperator(loc, player);
        Coronalis.instance().getNetworkRegistry().rebuildNetwork(loc);
        int scopes = countTelescopesAt(loc);
        BlockStorage.addBlockInfo(loc, "connected_telescopes", String.valueOf(scopes));

        if (scopes < target.getMinTelescopes()) {
            player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            player.sendMessage("§5[Coronalis] §c¡Telescopios insuficientes! " + target.displayName
                + " requiere §e" + target.getMinTelescopes() + "§c. Detectados: §e" + scopes);
            return;
        }

        BlockStorage.addBlockInfo(loc, "target_az",       String.valueOf(target.az));
        BlockStorage.addBlockInfo(loc, "target_el",       String.valueOf(target.el));
        BlockStorage.addBlockInfo(loc, "selected_target", target.displayName);
        BlockStorage.addBlockInfo(loc, "correlation_progress", "0");

        player.playSound(loc, Sound.UI_BUTTON_CLICK, 1f, 1.5f);
        Coronalis.log("[Coronalis/Console] " + player.getName() + " target_lock "
            + target.displayName + " scopes=" + scopes + " loc=" + fmtLoc(loc));
        player.sendMessage("§5[Coronalis] §dObjetivo fijado: " + target.tier.color + "§l" + target.displayName
            + "  §7" + target.tier.label);

        Coronalis.instance().getDiscoveryService().tryDiscover(
            player, "first_target_lock", "Primer objetivo astronómico", "discovery-xp.first_target_lock");
        updateMenuVisuals(menu, loc);
    }

    private void onManualSlew(@Nonnull BlockMenu menu, @Nonnull Location loc, @Nonnull Player player) {
        touchOperator(loc, player);
        CoronalisNetwork network = Coronalis.instance().getNetworkRegistry().getOrCreate(loc);
        importExternalEnergy(loc, network);
        String fault = getStr(loc, "fault_state");
        if (!"NORMAL".equals(fault)) {
            player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            player.sendMessage("§5[Coronalis] §c¡ERROR DE OPERACIÓN! Falla activa: " + fault + ". Usa el botón de reparación.");
            return;
        }

        double curAz = getDbl(loc, "current_az");
        double curEl = getDbl(loc, "current_el");
        double tarAz = getDbl(loc, "target_az");
        double tarEl = getDbl(loc, "target_el");
        double diffAz = tarAz - curAz;
        double diffEl = tarEl - curEl;
        double dist = Math.sqrt(diffAz * diffAz + diffEl * diffEl);

        if (dist < ALIGNED_THRESHOLD) {
            player.playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
            Coronalis.instance().getSoundManager().playAt(loc, SoundManager.CoronalisSound.SLEW_DONE);
            player.sendMessage("§5[Coronalis] §aTelescopios perfectamente alineados en el objetivo.");
            Coronalis.instance().getDiscoveryService().tryDiscover(
                player, "tracking_lock", "Bloqueo de seguimiento (TRACKING)", "discovery-xp.tracking_lock");
            updateMenuVisuals(menu, loc);
            return;
        }

        if (!network.drainSU(CoronalisNetwork.SU_COST_SLEW)) {
            warnNoEnergy(player, loc, network, CoronalisNetwork.SU_COST_SLEW);
            updateMenuVisuals(menu, loc);
            return;
        }

        double stepAz = clamp(diffAz * KP, -MAX_STEP_AZ, MAX_STEP_AZ);
        double stepEl = clamp(diffEl * KP, -MAX_STEP_EL, MAX_STEP_EL);
        BlockStorage.addBlockInfo(loc, "current_az", String.valueOf(round1(curAz + stepAz)));
        BlockStorage.addBlockInfo(loc, "current_el", String.valueOf(round1(curEl + stepEl)));

        double newDist = round1(Math.sqrt(
            Math.pow(tarAz - getDbl(loc, "current_az"), 2) +
            Math.pow(tarEl - getDbl(loc, "current_el"), 2)));

        // Falla mecánica aleatoria
        boolean storm = Coronalis.instance().getCosmicEventManager().isFaultChanceBoosted();
        if (random.nextInt(storm ? FAULT_CHANCE_STORM : FAULT_CHANCE_NORMAL) == 0) {
            String faultType = FAULT_TYPES[random.nextInt(FAULT_TYPES.length)];
            BlockStorage.addBlockInfo(loc, "fault_state", faultType);
            player.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.5f);
            Coronalis.instance().getSoundManager().playFaultAlert(player);
            player.sendMessage("§5[Coronalis] §c🚨 ¡FALLA MECÁNICA! Sistema: §e" + faultType
                + (storm ? " §7(Tormenta Magnética activa)" : ""));
        } else {
            player.playSound(loc, Sound.BLOCK_IRON_TRAPDOOR_OPEN, 0.8f, 1.2f);
            Coronalis.instance().getSoundManager().playSlewSequence(loc, newDist);
            player.sendMessage("§5[Coronalis] §7PID ejecutado. Error restante: §c" + newDist + "°");
        }

        updateMenuVisuals(menu, loc);
    }

    private void onRepair(@Nonnull BlockMenu menu, @Nonnull Location loc, @Nonnull Player player) {
        touchOperator(loc, player);
        if ("NORMAL".equals(getStr(loc, "fault_state"))) {
            player.sendMessage("§5[Coronalis] §aTodos los sistemas operan dentro del rango normal.");
            return;
        }
        BlockStorage.addBlockInfo(loc, "fault_state", "NORMAL");
        player.playSound(loc, Sound.BLOCK_ANVIL_USE, 1f, 1f);
        Coronalis.instance().getSoundManager().play(player, SoundManager.CoronalisSound.CALIBRATE);
        player.sendMessage("§5[Coronalis] §aSistemas reparados y calibrados. Estado: §aNORMAL.");
        updateMenuVisuals(menu, loc);
    }

    private void onToggleAuto(@Nonnull BlockMenu menu, @Nonnull Location loc, @Nonnull Player player) {
        touchOperator(loc, player);
        boolean current = "true".equals(BlockStorage.getLocationInfo(loc, "auto_mode"));
        BlockStorage.addBlockInfo(loc, "auto_mode", current ? "false" : "true");
        player.sendMessage("§5[Coronalis] §dModo automático: " + (current ? "§cDESACTIVADO" : "§aACTIVADO"));
        if (!current) player.sendMessage("  §7La consola alineará y correlacionará señales automáticamente.");
        player.playSound(loc, Sound.BLOCK_LEVER_CLICK, 1f, current ? 0.7f : 1.3f);
        updateMenuVisuals(menu, loc);
    }

    private void onCorrelate(@Nonnull BlockMenu menu, @Nonnull Location loc, @Nonnull Player player) {
        touchOperator(loc, player);
        CoronalisNetwork network = Coronalis.instance().getNetworkRegistry().getOrCreate(loc);
        importExternalEnergy(loc, network);
        if (network.getTelescopeCount() == 0) {
            player.sendMessage("§5[Coronalis] §cNo hay radiotelescopios conectados por cable a esta consola.");
            return;
        }
        if (network.getCalibratedCount() < network.getTelescopeCount()) {
            player.sendMessage("§5[Coronalis] §cArray sin calibrar: §e" + network.getCalibratedCount()
                + "/" + network.getTelescopeCount() + " §cradiotelescopios listos. Usa §dCalibrar Array§c.");
            Coronalis.instance().getSoundManager().play(player, SoundManager.CoronalisSound.FAULT);
            updateMenuVisuals(menu, loc);
            return;
        }
        if (!"NORMAL".equals(getStr(loc, "fault_state"))) {
            player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            player.sendMessage("§5[Coronalis] §cCorrelación bloqueada por falla activa: " + getStr(loc, "fault_state"));
            return;
        }

        CosmicEventManager ev = Coronalis.instance().getCosmicEventManager();
        if (ev.isSignalBlocked()) {
            player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            player.sendMessage("§5[Coronalis] §c¡LLAMARADA SOLAR! Correlación imposible hasta que cese la interferencia.");
            return;
        }

        double dist = Math.sqrt(
            Math.pow(getDbl(loc, "target_az") - getDbl(loc, "current_az"), 2) +
            Math.pow(getDbl(loc, "target_el") - getDbl(loc, "current_el"), 2));
        if (dist >= ALIGNED_THRESHOLD) {
            player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            player.sendMessage("§5[Coronalis] §cSeñal incoherente. Error de apuntado: §e" + round1(dist)
                + "° §c(mínimo < " + ALIGNED_THRESHOLD + "°)");
            return;
        }

        ItemStack input = menu.getItemInSlot(INPUT_SLOT);
        SlimefunItem sfIn = SlimefunItem.getByItem(input);
        if (sfIn == null || !sfIn.getId().equals("CORONALIS_DATA_CELL")) {
            player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            player.sendMessage("§5[Coronalis] §cSe requiere una §eCelda de Datos Celestes §cen el slot de entrada.");
            return;
        }

        ItemStack output = menu.getItemInSlot(OUTPUT_SLOT);
        if (output != null && output.getType() != Material.AIR
                && output.getAmount() >= output.getMaxStackSize()) {
            player.sendMessage("§5[Coronalis] §cEl slot de salida está lleno.");
            return;
        }

        if (!network.drainSU(CoronalisNetwork.SU_COST_CORRELATE)) {
            warnNoEnergy(player, loc, network, CoronalisNetwork.SU_COST_CORRELATE);
            updateMenuVisuals(menu, loc);
            return;
        }

        int scopes = getInt(BlockStorage.getLocationInfo(loc, "connected_telescopes"), countTelescopesAt(loc));
        int inc    = (int) Math.round((MANUAL_BASE + scopes * MANUAL_PER_SCOPE) * ev.getCorrelationSpeedMult());
        int prog   = Math.min(100, getInt(BlockStorage.getLocationInfo(loc, "correlation_progress"), 0) + inc);
        BlockStorage.addBlockInfo(loc, "correlation_progress", String.valueOf(prog));

        if (prog >= 100) {
            finalizeCorrelation(loc, menu, player);
        } else {
            player.playSound(loc, Sound.BLOCK_BEACON_AMBIENT, 1f, 1.8f);
            Coronalis.instance().getSoundManager().playCorrelationTick(loc, prog);
            Coronalis.log("[Coronalis/Signal] " + player.getName() + " correlate progress="
                + prog + "% scopes=" + scopes + " SU=" + network.getSignalUnits() + "/" + network.getMaxSignalUnits());
            player.sendMessage("§5[Coronalis] §7Correlación: §b" + prog + "%  §7(+" + inc + "% con " + scopes + " telescopio(s))");
            updateMenuVisuals(menu, loc);
        }
    }

    private void onCalibrate(@Nonnull BlockMenu menu, @Nonnull Location loc, @Nonnull Player player) {
        touchOperator(loc, player);
        Coronalis.instance().getNetworkRegistry().rebuildNetwork(loc);
        CoronalisNetwork network = Coronalis.instance().getNetworkRegistry().getOrCreate(loc);
        importExternalEnergy(loc, network);
        if (network.getTelescopeCount() == 0) {
            player.sendMessage("§5[Coronalis] §cNo hay radiotelescopios conectados por cable.");
            Coronalis.instance().getSoundManager().play(player, SoundManager.CoronalisSound.FAULT);
            updateMenuVisuals(menu, loc);
            return;
        }
        if (!network.drainSU(CoronalisNetwork.SU_COST_CALIBRATE)) {
            warnNoEnergy(player, loc, network, CoronalisNetwork.SU_COST_CALIBRATE);
            updateMenuVisuals(menu, loc);
            return;
        }

        TelescopeState selected = null;
        TelescopeState.CalibParam selectedParam = null;
        for (TelescopeState state : network.getTelescopes().values()) {
            for (TelescopeState.CalibParam param : TelescopeState.CalibParam.values()) {
                if (state.get(param) < 100) {
                    selected = state;
                    selectedParam = param;
                    break;
                }
            }
            if (selected != null) break;
        }

        if (selected == null || selectedParam == null) {
            player.sendMessage("§5[Coronalis] §aTodos los radiotelescopios están calibrados al 100%.");
            Coronalis.instance().getSoundManager().play(player, SoundManager.CoronalisSound.ENERGY_FULL);
            updateMenuVisuals(menu, loc);
            return;
        }

        selected.advance(selectedParam, 20);
        selected.incrementCalibCount();
        int value = selected.get(selectedParam);
        Coronalis.instance().getSoundManager().playCalibStep(player, value);
        Coronalis.log("[Coronalis/Calib] " + player.getName() + " scope=" + fmtLoc(selected.getLoc())
            + " param=" + selectedParam.name() + " value=" + value + "% SU="
            + network.getSignalUnits() + "/" + network.getMaxSignalUnits());
        player.sendMessage("§5[Coronalis] §dCalibración " + selectedParam.label + "§7 en "
            + fmtLoc(selected.getLoc()) + ": " + TelescopeState.bar(value));

        if (selected.isFullyCalibrated()) {
            player.sendMessage("§5[Coronalis] §aRadiotelescopio sincronizado completamente con el array.");
            Coronalis.instance().getDiscoveryService().tryDiscover(
                player, "first_full_telescope_calibration", "Primera calibración completa", "discovery-xp.first_full_calibration");
        }
        updateMenuVisuals(menu, loc);
    }

    // ── Finalización de correlación ──────────────────────────────────────────

    /**
     * Completa la correlación: consume la Celda de Datos, produce el Eco de Fase
     * con lore enriquecido y entrega XP al jugador.
     *
     * @param player null si fue disparado por auto-tick.
     */
    static void finalizeCorrelation(@Nonnull Location loc, @Nonnull BlockMenu menu, @Nullable Player player) {
        String targetName = getStr(loc, "selected_target");
        CelestialTarget target = CelestialTarget.byName(targetName);

        // Consumir Celda de Datos
        ItemStack input = menu.getItemInSlot(INPUT_SLOT);
        if (input != null && input.getAmount() > 0) {
            input.setAmount(input.getAmount() - 1);
            menu.replaceExistingItem(INPUT_SLOT, input.getAmount() <= 0 ? null : input);
        }

        // Producir Eco de Fase con lore de datos
        ItemStack record = Items.CORONALIS_RECORD_DISCOVERED.clone();
        ItemMeta meta = record.getItemMeta();
        if (meta != null) {
            double phasePct = 97.0 + Math.random() * 3.0;
            List<String> lore = new ArrayList<>();
            lore.add("§8══════════════════════════════════");
            lore.add("§dObjeto: §e" + target.displayName);
            lore.add("§dAz/El: §e" + target.az + "° / " + target.el + "°");
            lore.add("§dFase: §b" + String.format("%.2f", phasePct) + "%");
            lore.add("§dDificultad: " + target.tier.label);
            lore.add("§dDescripción: §7" + target.description);
            lore.add("§8══════════════════════════════════");
            lore.add("§eClic derecho para analizar y recibir XP.");
            meta.setLore(lore);
            record.setItemMeta(meta);
        }

        ItemStack output = menu.getItemInSlot(OUTPUT_SLOT);
        if (output == null || output.getType() == Material.AIR) {
            menu.replaceExistingItem(OUTPUT_SLOT, record);
        } else {
            output.setAmount(output.getAmount() + 1);
            menu.replaceExistingItem(OUTPUT_SLOT, output);
        }

        BlockStorage.addBlockInfo(loc, "correlation_progress", "0");

        if (player != null) {
            player.playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
            Coronalis.instance().getSoundManager().playDiscovery(player, loc);
            Coronalis.log("[Coronalis/Signal] " + player.getName() + " finalized target="
                + target.displayName + " loc=" + fmtLoc(loc));
            player.sendMessage("");
            player.sendMessage("§5§l[✦ Coronalis] §r§a⭐ ¡Señal correlacionada con éxito!");
            player.sendMessage("§d  Objetivo: §e" + target.displayName + "  §7| " + target.tier.label);
            player.sendMessage("§7  Recoge el §eEco de Fase §7del slot de salida y analízalo.");
            player.sendMessage("");

            DiscoveryService ds = Coronalis.instance().getDiscoveryService();
            ds.tryDiscover(player, "first_correlation", "Primera correlación interferométrica", "discovery-xp.first_correlation");
            ds.tryDiscover(player, DiscoveryService.targetDiscoveryId(target.displayName), target.displayName,
                DiscoveryService.targetConfigPath(target.displayName));
        } else if (loc.getWorld() != null) {
            loc.getWorld().playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.7f, 1f);
        }

        updateMenuVisuals(menu, loc);
    }

    // ── Actualización del display ─────────────────────────────────────────────

    /**
     * Refresca todos los paneles y botones del menú con el estado actual del BlockStorage.
     * Llamado después de cada cambio de estado, también desde RadioTelescope.
     */
    static void updateMenuVisuals(@Nonnull BlockMenu menu, @Nonnull Location loc) {
        String targetName = getStr(loc, "selected_target");
        double curAz   = getDbl(loc, "current_az");
        double curEl   = getDbl(loc, "current_el");
        double tarAz   = getDbl(loc, "target_az");
        double tarEl   = getDbl(loc, "target_el");
        String fault   = getStr(loc, "fault_state");
        int    progress = getInt(BlockStorage.getLocationInfo(loc, "correlation_progress"), 0);
        CoronalisNetwork network = Coronalis.instance().getNetworkRegistry().getOrCreate(loc);
        importExternalEnergy(loc, network);
        int    scopes   = network.getTelescopeCount();
        BlockStorage.addBlockInfo(loc, "connected_telescopes", String.valueOf(scopes));
        boolean autoOn  = "true".equals(BlockStorage.getLocationInfo(loc, "auto_mode"));
        List<CoronalisNetwork> rivals = Coronalis.instance().getNetworkRegistry().findRivalNetworks(network);
        long interfering = rivals.stream()
            .filter(rival -> Coronalis.instance().getNetworkRegistry().isInterferingWith(network, rival))
            .count();

        double diffAz = tarAz - curAz;
        double diffEl = tarEl - curEl;
        double error  = round1(Math.sqrt(diffAz * diffAz + diffEl * diffEl));

        String stateStr;
        if (!"NORMAL".equals(fault)) {
            stateStr = "§cBLOQUEADO (" + fault + ")";
        } else if (error < ALIGNED_THRESHOLD && !"Ninguno".equals(targetName)) {
            stateStr = "§aSEGUIMIENTO (TRACKING)";
        } else if (error >= ALIGNED_THRESHOLD) {
            stateStr = "§eAPUNTANDO (SLEWING)";
        } else {
            stateStr = "§7EN ESPERA (IDLE)";
        }

        // Panel: Red de Antenas
        menu.replaceExistingItem(PANEL_TELESCOPES, new CustomItemStack(Material.BEACON,
            "§d📡 Red de Antenas Coronalis",
            "§7Telescopios cableados: §a" + scopes + "§7/§e" + CoronalisNetwork.MAX_TELESCOPES,
            "§7Calibrados: §b" + network.getCalibratedCount() + "§7/§b" + scopes,
            "§7Eficiencia: §e" + Math.round(network.getAverageCalibrationFactor() * 100.0) + "%",
            "§7Baseline interferométrico: §e" + (scopes * 15) + " m",
            "§7Modo: " + (autoOn ? "§aAUTOMÁTICO" : "§7MANUAL"),
            "§7Redes cercanas: §d" + rivals.size() + (interfering > 0 ? " §c(" + interfering + " interfiriendo)" : ""),
            "",
            "§7Velocidad correlación manual: §b+" + (MANUAL_BASE + scopes * MANUAL_PER_SCOPE) + "% / clic"
        ));

        // Panel: Objetivo
        CelestialTarget target = "Ninguno".equals(targetName) ? null : CelestialTarget.byName(targetName);
        menu.replaceExistingItem(PANEL_TARGET, new CustomItemStack(Material.COMPASS,
            "§d🎯 Objetivo Fijado",
            "§7Cuerpo: §6§l" + targetName,
            target != null ? target.tier.label : "§7—",
            "§7Az obj: §e" + round1(tarAz) + "°  §7El obj: §e" + round1(tarEl) + "°"
        ));

        // Panel: Telemetría
        menu.replaceExistingItem(PANEL_TELEMETRY, new CustomItemStack(Material.SPYGLASS,
            "§d📊 Telemetría (10Hz)",
            "§7Az actual: §e" + curAz + "°",
            "§7El actual: §e" + curEl + "°",
            "§7Energía SU: §b" + network.getSignalUnits() + "§7/§b" + network.getMaxSignalUnits()
                + " §8(" + Math.round(network.getSUPercent()) + "%)",
            "§7Buffer eléctrico: §e" + getExternalCharge(loc) + "§7/§e" + ENERGY_CAPACITY_J + " J",
            "§7Núcleos SU: §a" + network.getEnergyNodeCount(),
            "",
            "§7Error de apuntado: §c" + error + "°",
            "§7Estado: " + stateStr
        ));

        // Panel: PID
        menu.replaceExistingItem(PANEL_PID, new CustomItemStack(Material.REPEATER,
            "§d⚙️ Controlador PID",
            "§7Kp: §a0.45  §7Ki: §a0.02  §7Kd: §a0.15",
            "§7Paso máx Az: §e" + MAX_STEP_AZ + "°  §7El: §e" + MAX_STEP_EL + "°",
            "",
            "§7Salida PID: §b" + round1(error * KP) + " rad/s²"
        ));

        // Panel: Falla
        boolean hasFault = !"NORMAL".equals(fault);
        menu.replaceExistingItem(PANEL_FAULT, new CustomItemStack(
            hasFault ? Material.REDSTONE_BLOCK : Material.LIME_DYE,
            "§d🛠️ Diagnóstico Mecánico",
            "§7Estado: " + (hasFault ? "§c🚨 " + fault : "§aNORMAL"),
            hasFault ? "§7Usa §eReparar §7para resolver." : "§7Sin anomalías.",
            "",
            "§7Prob. falla: §e" + (Coronalis.instance().getCosmicEventManager().isFaultChanceBoosted()
                ? "§cALTA (Tormenta)" : "NORMAL")
        ));

        // Panel: Evento Cósmico ← NUEVO
        CosmicEventManager evMgr = Coronalis.instance().getCosmicEventManager();
        CosmicEventManager.CosmicEvent ev = evMgr.getActiveEvent();
        boolean evActive = evMgr.isEventActive();
        List<String> evLore = new ArrayList<>(Arrays.asList(
            "§d✧ Evento Cósmico Activo",
            evActive ? "§e⚡ " + ev.displayName : "§7Sin evento activo.",
            evActive ? "§7Tiempo restante: §b" + evMgr.getRemainingSeconds() + "s" : "§7Condiciones nominales."
        ));
        if (evActive && ev.xpMultiplier > 1.0)            evLore.add("§a↑ XP ×" + ev.xpMultiplier);
        if (evActive && ev.correlationSpeedMult > 1.0)    evLore.add("§b↑ Velocidad correlación ×" + ev.correlationSpeedMult);
        if (evActive && ev.blocksSignal)                   evLore.add("§c✗ Correlación BLOQUEADA");
        Material evMat = evActive ? Material.NETHER_STAR : Material.GRAY_DYE;
        menu.replaceExistingItem(PANEL_EVENT, new CustomItemStack(evMat, evLore));

        // Botón: Slew
        menu.replaceExistingItem(BUTTON_SLEW, new CustomItemStack(Material.LEVER,
            "§aIniciar Alineación (PID)",
            "§7Ajusta dinámicamente los motores hacia",
            "§7las coordenadas del objetivo fijado.",
            "",
            "§dClic para ejecutar un ciclo PID."
        ));

        // Botón: Reparar
        menu.replaceExistingItem(BUTTON_REPAIR, new CustomItemStack(Material.ANVIL,
            "§cCalibrar y Reparar Motores",
            "§7Resetea fallas mecánicas y recalibra el PID.",
            "",
            "§eClic para limpiar fallas activas."
        ));

        menu.replaceExistingItem(BUTTON_CALIBRATE, new CustomItemStack(Material.AMETHYST_SHARD,
            "§dCalibrar Array",
            "§7Ajusta Azimut, Elevación, Frecuencia,",
            "§7Fase y Ganancia de cada radiotelescopio.",
            "§7Costo: §b" + CoronalisNetwork.SU_COST_CALIBRATE + " SU §7por paso.",
            "",
            "§7Progreso global: §e" + Math.round(network.getAverageCalibrationFactor() * 100.0) + "%",
            "§dClic para calibrar el siguiente parámetro."
        ));

        menu.replaceExistingItem(BUTTON_INVITE, new CustomItemStack(Material.WRITABLE_BOOK,
            "§aInvitar Operador",
            "§7Solo el propietario puede invitar jugadores.",
            "§7El nombre se escribe por chat y se intercepta.",
            "",
            "§dClic para abrir flujo de invitación."
        ));

        menu.replaceExistingItem(BUTTON_PASSWORD, new CustomItemStack(Material.ENDER_EYE,
            "§6Contraseña de Terminal",
            "§7Permite que un invitado use §d/coronalis auth",
            "§7tras intentar abrir una consola protegida.",
            "§7Estado: " + (Coronalis.instance().getAccessManager().hasPassword(loc) ? "§aConfigurada" : "§cSin contraseña"),
            "",
            "§dClic para cambiarla o quitarla."
        ));

        // Botón: Auto ← NUEVO
        menu.replaceExistingItem(BUTTON_AUTO, new CustomItemStack(
            autoOn ? Material.LIME_CONCRETE : Material.GRAY_CONCRETE,
            autoOn ? "§a✅ Modo Automático: ON" : "§7⬜ Modo Automático: OFF",
            "§7Alinea PID y correlaciona señales",
            "§7automáticamente sin interacción manual.",
            "§7Velocidad auto: §b" + (AUTO_BASE + scopes * AUTO_PER_SCOPE) + "% / 2s",
            "",
            "§dClic para " + (autoOn ? "§cdesactivar" : "§aactivar") + "§d."
        ));

        // Botón: Correlación
        String bar = "§a" + "█".repeat(progress / 10) + "§8" + "█".repeat(10 - progress / 10);
        menu.replaceExistingItem(BUTTON_CORRELATE, new CustomItemStack(Material.BREWING_STAND,
            "§dCorrelación de Señal",
            "§7Combina fases de radio para procesar un Eco.",
            "",
            "§7Progreso: §b" + progress + "%",
            "§7" + bar,
            "§7Requiere: §eCelda de Datos Celestes §7en entrada.",
            "",
            "§dClic para correlacionar."
        ));
    }

    // ── Conteo de telescopios ─────────────────────────────────────────────────

    /**
     * Cuenta los CORONALIS_RADIO_TELESCOPE en un radio de 15 × 3 × 15 bloques.
     * Método público para RadioTelescope.
     */
    public static int countTelescopesAt(@Nonnull Location loc) {
        if (loc.getWorld() == null) return 0;
        return Coronalis.instance().getNetworkRegistry().getOrCreate(loc).getTelescopeCount();
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    @Nonnull
    private static String getStr(@Nonnull Location loc, @Nonnull String key) {
        String v = BlockStorage.getLocationInfo(loc, key);
        return v != null ? v : "";
    }

    private static double getDbl(@Nonnull Location loc, @Nonnull String key) {
        String v = BlockStorage.getLocationInfo(loc, key);
        if (v == null) return 0.0;
        try { return Double.parseDouble(v); } catch (NumberFormatException e) { return 0.0; }
    }

    private static int getInt(@Nullable String v, int def) {
        if (v == null) return def;
        try { return Integer.parseInt(v); } catch (NumberFormatException e) { return def; }
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private static boolean claimOperator(@Nonnull Location loc, @Nonnull Player player) {
        String operator = getStr(loc, "cc_operator");
        if (operator.isBlank() || operator.equals(player.getUniqueId().toString())
            || player.hasPermission("coronalis.admin")) {
            touchOperator(loc, player);
            return true;
        }
        player.sendMessage("§5[Coronalis] §cTerminal bloqueada por otro operador. Una consola no admite uso simultáneo.");
        player.sendMessage("§5[Coronalis] §7Pide al operador actual que cierre turno o espera a que libere el sistema.");
        Coronalis.instance().getSoundManager().play(player, SoundManager.CoronalisSound.FAULT);
        return false;
    }

    private static void touchOperator(@Nonnull Location loc, @Nonnull Player player) {
        BlockStorage.addBlockInfo(loc, "cc_operator", player.getUniqueId().toString());
    }

    private static void warnNoEnergy(@Nonnull Player player, @Nonnull Location loc,
                                     @Nonnull CoronalisNetwork network, int required) {
        player.sendMessage("§5[Coronalis] §cEnergía SU insuficiente: §e" + network.getSignalUnits()
            + "§7/§e" + required + " §cSU requeridas.");
        player.sendMessage("§5[Coronalis] §7Conecta §bNúcleos de Energía SU §7o una red eléctrica Slimefun a la consola.");
        Coronalis.instance().getSoundManager().playEnergyLow(loc);
    }

    private static void importExternalEnergy(@Nonnull Location loc, @Nonnull CoronalisNetwork network) {
        SlimefunItem item = BlockStorage.check(loc.getBlock());
        if (!(item instanceof ControlConsole console)) return;
        int charge = console.getCharge(loc);
        if (charge <= 0 || network.getSignalUnits() >= network.getMaxSignalUnits()) return;

        int usedJ = Math.min(charge, ENERGY_IMPORT_PER_TICK_J);
        int su = Math.max(1, usedJ / JOULES_PER_SU);
        network.addSU(su);
        console.removeCharge(loc, usedJ);
        Coronalis.log("[Coronalis/Energy] Importados " + usedJ + " J -> " + su
            + " SU en " + fmtLoc(loc));
    }

    private static int getExternalCharge(@Nonnull Location loc) {
        SlimefunItem item = BlockStorage.check(loc.getBlock());
        return item instanceof ControlConsole console ? console.getCharge(loc) : 0;
    }

    @Nonnull
    private static String fmtLoc(@Nonnull Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    // ── InventoryBlock ────────────────────────────────────────────────────────

    @Nonnull @Override
    public int[] getInputSlots()  { return new int[]{ INPUT_SLOT  }; }
    @Nonnull @Override
    public int[] getOutputSlots() { return new int[]{ OUTPUT_SLOT }; }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return ENERGY_CAPACITY_J;
    }
}
