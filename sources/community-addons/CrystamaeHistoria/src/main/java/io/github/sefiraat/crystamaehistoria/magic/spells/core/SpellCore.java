package io.github.sefiraat.crystamaehistoria.magic.spells.core;

import io.github.sefiraat.crystamaehistoria.magic.CastInformation;
import com.github.drakescraft_labs.slimefun4.libraries.dough.collections.Pair;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Consumer;

public class SpellCore {

    private final double cooldownSeconds;
    private final double range;
    private final int crystaCost;
    private final double damageAmount;
    private final double knockbackAmount;
    private final double healAmount;
    private final double projectileAoeRange;
    private final double projectileKnockbackAmount;
    private final int numberOfTicks;
    private final int tickInterval;
    private final boolean cooldownDivided;
    private final boolean rangeMultiplied;
    private final boolean crystaMultiplied;
    private final boolean damageMultiplied;
    private final boolean knockbackMultiplied;
    private final boolean healMultiplied;
    private final boolean projectileAoeMultiplied;
    private final boolean projectileKnockbackMultiplied;
    private final boolean numberOfTicksMultiplied;
    private final boolean tickIntervalMultiplied;
    private final boolean isInstantCast;
    private final Consumer<CastInformation> instantCastEvent;
    private final boolean isProjectileSpell;
    private final boolean isProjectileVsEntitySpell;
    private final boolean isProjectileVsBlockSpell;
    private final Consumer<CastInformation> fireProjectileEvent;
    private final Consumer<CastInformation> beforeProjectileHitEvent;
    private final Consumer<CastInformation> projectileHitEvent;
    private final Consumer<CastInformation> projectileHitBlockEvent;
    private final Consumer<CastInformation> afterProjectileHitEvent;
    private final boolean isTickingSpell;
    private final Consumer<CastInformation> tickEvent;
    private final Consumer<CastInformation> afterAllTicksEvent;
    private final boolean isDamagingSpell;
    private final boolean isHealingSpell;
    private final boolean isEffectingSpell;
    private final boolean amplificationMultiplied;
    private final boolean effectDurationMultiplied;
    private final Map<PotionEffectType, Pair<Integer, Integer>> positiveEffectPairMap;
    private final Map<PotionEffectType, Pair<Integer, Integer>> negativeEffectPairMap;


    @ParametersAreNonnullByDefault
    public SpellCore(SpellCoreBuilder spellCoreBuilder) {
        this.cooldownSeconds = spellCoreBuilder.getCooldownSeconds();
        this.range = spellCoreBuilder.getRange();
        this.crystaCost = spellCoreBuilder.getCrystaCost();
        this.damageAmount = spellCoreBuilder.getDamageAmount();
        this.knockbackAmount = spellCoreBuilder.getKnockbackAmount();
        this.healAmount = spellCoreBuilder.getHealAmount();
        this.projectileAoeRange = spellCoreBuilder.getProjectileAoeRange();
        this.projectileKnockbackAmount = spellCoreBuilder.getProjectileKnockbackAmount();
        this.numberOfTicks = spellCoreBuilder.getNumberOfTicks();
        this.tickInterval = spellCoreBuilder.getTickInterval();
        this.cooldownDivided = spellCoreBuilder.isCooldownDivided();
        this.rangeMultiplied = spellCoreBuilder.isRangeMultiplied();
        this.crystaMultiplied = spellCoreBuilder.isCrystaMultiplied();
        this.damageMultiplied = spellCoreBuilder.isDamageMultiplied();
        this.knockbackMultiplied = spellCoreBuilder.isKnockbackMultiplied();
        this.healMultiplied = spellCoreBuilder.isHealMultiplied();
        this.projectileAoeMultiplied = spellCoreBuilder.isProjectileAoeMultiplied();
        this.projectileKnockbackMultiplied = spellCoreBuilder.isProjectileKnockbackMultiplied();
        this.numberOfTicksMultiplied = spellCoreBuilder.isNumberOfTicksMultiplied();
        this.tickIntervalMultiplied = spellCoreBuilder.isTickIntervalMultiplied();
        this.isInstantCast = spellCoreBuilder.isInstantCast();
        this.instantCastEvent = spellCoreBuilder.getInstantCastEvent();
        this.isProjectileSpell = spellCoreBuilder.isProjectileSpell();
        this.isProjectileVsEntitySpell = spellCoreBuilder.isProjectileVsEntitySpell();
        this.isProjectileVsBlockSpell = spellCoreBuilder.isProjectileVsBlockSpell();
        this.fireProjectileEvent = spellCoreBuilder.getFireProjectileEvent();
        this.beforeProjectileHitEvent = spellCoreBuilder.getBeforeProjectileHitEvent();
        this.projectileHitEvent = spellCoreBuilder.getProjectileHitEvent();
        this.projectileHitBlockEvent = spellCoreBuilder.getProjectileHitBlockEvent();
        this.afterProjectileHitEvent = spellCoreBuilder.getAfterProjectileHitEvent();
        this.isTickingSpell = spellCoreBuilder.isTickingSpell();
        this.tickEvent = spellCoreBuilder.getTickEvent();
        this.afterAllTicksEvent = spellCoreBuilder.getAfterAllTicksEvent();
        this.isDamagingSpell = spellCoreBuilder.isDamagingSpell();
        this.isHealingSpell = spellCoreBuilder.isHealingSpell();
        this.isEffectingSpell = spellCoreBuilder.isEffectingSpell();
        this.amplificationMultiplied = spellCoreBuilder.isAmplificationMultiplied();
        this.effectDurationMultiplied = spellCoreBuilder.isEffectDurationMultiplied();
        this.positiveEffectPairMap = spellCoreBuilder.getPositiveEffectPairMap();
        this.negativeEffectPairMap = spellCoreBuilder.getNegativeEffectPairMap();
    }

    public double getCooldownSeconds() { return cooldownSeconds; }
    public double getRange() { return range; }
    public int getCrystaCost() { return crystaCost; }
    public double getDamageAmount() { return damageAmount; }
    public double getKnockbackAmount() { return knockbackAmount; }
    public double getHealAmount() { return healAmount; }
    public double getProjectileAoeRange() { return projectileAoeRange; }
    public double getProjectileKnockbackAmount() { return projectileKnockbackAmount; }
    public int getNumberOfTicks() { return numberOfTicks; }
    public int getTickInterval() { return tickInterval; }
    public boolean isCooldownDivided() { return cooldownDivided; }
    public boolean isRangeMultiplied() { return rangeMultiplied; }
    public boolean isCrystaMultiplied() { return crystaMultiplied; }
    public boolean isDamageMultiplied() { return damageMultiplied; }
    public boolean isKnockbackMultiplied() { return knockbackMultiplied; }
    public boolean isHealMultiplied() { return healMultiplied; }
    public boolean isProjectileAoeMultiplied() { return projectileAoeMultiplied; }
    public boolean isProjectileKnockbackMultiplied() { return projectileKnockbackMultiplied; }
    public boolean isNumberOfTicksMultiplied() { return numberOfTicksMultiplied; }
    public boolean isTickIntervalMultiplied() { return tickIntervalMultiplied; }
    public boolean isInstantCast() { return isInstantCast; }
    public Consumer<CastInformation> getInstantCastEvent() { return instantCastEvent; }
    public boolean isProjectileSpell() { return isProjectileSpell; }
    public boolean isProjectileVsEntitySpell() { return isProjectileVsEntitySpell; }
    public boolean isProjectileVsBlockSpell() { return isProjectileVsBlockSpell; }
    public Consumer<CastInformation> getFireProjectileEvent() { return fireProjectileEvent; }
    public Consumer<CastInformation> getBeforeProjectileHitEvent() { return beforeProjectileHitEvent; }
    public Consumer<CastInformation> getProjectileHitEvent() { return projectileHitEvent; }
    public Consumer<CastInformation> getProjectileHitBlockEvent() { return projectileHitBlockEvent; }
    public Consumer<CastInformation> getAfterProjectileHitEvent() { return afterProjectileHitEvent; }
    public boolean isTickingSpell() { return isTickingSpell; }
    public Consumer<CastInformation> getTickEvent() { return tickEvent; }
    public Consumer<CastInformation> getAfterAllTicksEvent() { return afterAllTicksEvent; }
    public boolean isDamagingSpell() { return isDamagingSpell; }
    public boolean isHealingSpell() { return isHealingSpell; }
    public boolean isEffectingSpell() { return isEffectingSpell; }
    public boolean isAmplificationMultiplied() { return amplificationMultiplied; }
    public boolean isEffectDurationMultiplied() { return effectDurationMultiplied; }
    public Map<PotionEffectType, Pair<Integer, Integer>> getPositiveEffectPairMap() { return positiveEffectPairMap; }
    public Map<PotionEffectType, Pair<Integer, Integer>> getNegativeEffectPairMap() { return negativeEffectPairMap; }

}
