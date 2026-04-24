package io.github.sefiraat.crystamaehistoria.magic.spells.core;

import io.github.sefiraat.crystamaehistoria.magic.CastInformation;
import dev.drake.dough.collections.Pair;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SpellCoreBuilder {

    // All
    private final double cooldownSeconds;
    private final double range;
    private final int crystaCost;
    private final boolean cooldownDivided;
    private final boolean rangeMultiplied;
    private final boolean crystaMultiplied;
    private final Map<PotionEffectType, Pair<Integer, Integer>> positiveEffectPairMap = new HashMap<>();
    private final Map<PotionEffectType, Pair<Integer, Integer>> negativeEffectPairMap = new HashMap<>();
    private int particleNumber = 1;
    // Instant Casts
    private boolean isInstantCast;
    private Consumer<CastInformation> instantCastEvent;
    // Projectile Based Spells
    private boolean isProjectileSpell;
    private boolean isProjectileVsEntitySpell;
    private boolean isProjectileVsBlockSpell;
    private double projectileAoeRange;
    private double projectileKnockbackAmount;
    private boolean projectileAoeMultiplied;
    private boolean projectileKnockbackMultiplied;
    private Consumer<CastInformation> fireProjectileEvent;
    private Consumer<CastInformation> beforeProjectileHitEvent;
    private Consumer<CastInformation> projectileHitEvent;
    private Consumer<CastInformation> projectileHitBlockEvent;
    private Consumer<CastInformation> afterProjectileHitEvent;
    // Ticking Spells
    private boolean isTickingSpell;
    private int numberOfTicks;
    private int tickInterval;
    private boolean numberOfTicksMultiplied;
    private boolean tickIntervalMultiplied;
    private Consumer<CastInformation> tickEvent;
    private Consumer<CastInformation> afterAllTicksEvent;
    // Damaging Spells
    private boolean isDamagingSpell;
    private double damageAmount;
    private boolean damageMultiplied;
    private double knockbackAmount;
    private boolean knockbackMultiplied;
    // Healing Spells
    private boolean isHealingSpell;
    private double healAmount;
    private boolean healMultiplied;
    // Affecting spells
    private boolean isEffectingSpell;
    private boolean amplificationMultiplied;
    private boolean effectDurationMultiplied;

    public SpellCoreBuilder(double cooldownSeconds, boolean cooldownDivided, double range, boolean rangeMultiplied, int crystaCost, boolean crystaMultiplied) {
        this.cooldownSeconds = cooldownSeconds;
        this.cooldownDivided = cooldownDivided;
        this.range = range;
        this.rangeMultiplied = rangeMultiplied;
        this.crystaCost = crystaCost;
        this.crystaMultiplied = crystaMultiplied;
    }

    @Nonnull
    @CheckReturnValue
    public SpellCoreBuilder makeHealingSpell(double healAmount, boolean healMultiplied) {
        this.isHealingSpell = true;
        this.healAmount = healAmount;
        this.healMultiplied = healMultiplied;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public SpellCoreBuilder makeEffectingSpell(boolean effectAmplificationMultiplied, boolean effectDurationMultiplied) {
        this.isEffectingSpell = true;
        this.amplificationMultiplied = effectAmplificationMultiplied;
        this.effectDurationMultiplied = effectDurationMultiplied;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public SpellCoreBuilder makeDamagingSpell(double damageAmount, boolean damagePowerMultiplied, double knockbackAmount, boolean knockbackMultiplied) {
        this.isDamagingSpell = true;
        this.damageAmount = damageAmount;
        this.damageMultiplied = damagePowerMultiplied;
        this.knockbackAmount = knockbackAmount;
        this.knockbackMultiplied = knockbackMultiplied;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder makeInstantSpell(Consumer<CastInformation> instantCastMethod) {
        this.isInstantCast = true;
        this.instantCastEvent = instantCastMethod;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder makeProjectileSpell(
        Consumer<CastInformation> fireProjectileConsumer,
        double projectileAoeRange,
        boolean projectileAoeMultiplied,
        double projectileKnockbackAmount,
        boolean projectileKnockbackMultiplied
    ) {
        this.isProjectileSpell = true;
        this.fireProjectileEvent = fireProjectileConsumer;
        this.projectileAoeRange = projectileAoeRange;
        this.projectileAoeMultiplied = projectileAoeMultiplied;
        this.projectileKnockbackAmount = projectileKnockbackAmount;
        this.projectileKnockbackMultiplied = projectileKnockbackMultiplied;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder makeProjectileVsEntitySpell(
        Consumer<CastInformation> projectileHitConsumer
    ) {
        this.isProjectileVsEntitySpell = true;
        this.projectileHitEvent = projectileHitConsumer;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder makeProjectileVsBlockSpell(
        Consumer<CastInformation> projectileHitConsumer
    ) {
        this.isProjectileVsBlockSpell = true;
        this.projectileHitBlockEvent = projectileHitConsumer;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder addBeforeProjectileHitEntityEvent(Consumer<CastInformation> spellCastInformationConsumer) {
        this.beforeProjectileHitEvent = spellCastInformationConsumer;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder addAfterProjectileHitEntityEvent(Consumer<CastInformation> spellCastInformationConsumer) {
        this.afterProjectileHitEvent = spellCastInformationConsumer;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder addProjectileHitBlockEvent(Consumer<CastInformation> spellCastInformationConsumer) {
        this.projectileHitBlockEvent = spellCastInformationConsumer;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder makeTickingSpell(Consumer<CastInformation> spellCastInformationConsumer, int numberOfTicks, boolean ticksMultiplied, int tickInterval, boolean tickIntervalMultiplied) {
        this.tickEvent = spellCastInformationConsumer;
        this.isTickingSpell = true;
        this.numberOfTicks = numberOfTicks;
        this.numberOfTicksMultiplied = ticksMultiplied;
        this.tickInterval = tickInterval;
        this.tickIntervalMultiplied = tickIntervalMultiplied;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder addAfterTicksEvent(Consumer<CastInformation> spellCastInformationConsumer) {
        this.afterAllTicksEvent = spellCastInformationConsumer;
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public SpellCoreBuilder setNumberOfParticles(int particleNumber) {
        this.particleNumber = particleNumber;
        return this;
    }

    /**
     * @param potionEffectType  The {@link PotionEffectType} to apply.
     * @param level             The amplification of the effect. If multiple of the same effects are added, the cashedValues are combined.
     * @param durationInSeconds The duration of the effect in seconds. If multiple of the same effects are added, the highest is used.
     */
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder addPositiveEffect(PotionEffectType potionEffectType, int level, int durationInSeconds) {
        durationInSeconds = durationInSeconds * 20;
        positiveEffectPairMap.put(potionEffectType, new Pair<>(level, durationInSeconds));
        return this;
    }

    /**
     * @param potionEffectType  The {@link PotionEffectType} to apply.
     * @param level             The amplification of the effect. If multiple of the same effects are added, the cashedValues are combined.
     * @param durationInSeconds The duration of the effect. If multiple of the same effects are added, the highest is used.
     */
    @ParametersAreNonnullByDefault
    public SpellCoreBuilder addNegativeEffect(PotionEffectType potionEffectType, int level, int durationInSeconds) {
        durationInSeconds = durationInSeconds * 20;
        negativeEffectPairMap.put(potionEffectType, new Pair<>(level, durationInSeconds));
        return this;
    }

    @Nonnull
    public SpellCore build() {
        return new SpellCore(this);
    }

    public double getCooldownSeconds() { return cooldownSeconds; }
    public double getRange() { return range; }
    public int getCrystaCost() { return crystaCost; }
    public boolean isCooldownDivided() { return cooldownDivided; }
    public boolean isRangeMultiplied() { return rangeMultiplied; }
    public boolean isCrystaMultiplied() { return crystaMultiplied; }
    public Map<PotionEffectType, Pair<Integer, Integer>> getPositiveEffectPairMap() { return positiveEffectPairMap; }
    public Map<PotionEffectType, Pair<Integer, Integer>> getNegativeEffectPairMap() { return negativeEffectPairMap; }
    public int getParticleNumber() { return particleNumber; }
    public boolean isInstantCast() { return isInstantCast; }
    public Consumer<CastInformation> getInstantCastEvent() { return instantCastEvent; }
    public boolean isProjectileSpell() { return isProjectileSpell; }
    public boolean isProjectileVsEntitySpell() { return isProjectileVsEntitySpell; }
    public boolean isProjectileVsBlockSpell() { return isProjectileVsBlockSpell; }
    public double getProjectileAoeRange() { return projectileAoeRange; }
    public double getProjectileKnockbackAmount() { return projectileKnockbackAmount; }
    public boolean isProjectileAoeMultiplied() { return projectileAoeMultiplied; }
    public boolean isProjectileKnockbackMultiplied() { return projectileKnockbackMultiplied; }
    public Consumer<CastInformation> getFireProjectileEvent() { return fireProjectileEvent; }
    public Consumer<CastInformation> getBeforeProjectileHitEvent() { return beforeProjectileHitEvent; }
    public Consumer<CastInformation> getProjectileHitEvent() { return projectileHitEvent; }
    public Consumer<CastInformation> getProjectileHitBlockEvent() { return projectileHitBlockEvent; }
    public Consumer<CastInformation> getAfterProjectileHitEvent() { return afterProjectileHitEvent; }
    public boolean isTickingSpell() { return isTickingSpell; }
    public int getNumberOfTicks() { return numberOfTicks; }
    public int getTickInterval() { return tickInterval; }
    public boolean isNumberOfTicksMultiplied() { return numberOfTicksMultiplied; }
    public boolean isTickIntervalMultiplied() { return tickIntervalMultiplied; }
    public Consumer<CastInformation> getTickEvent() { return tickEvent; }
    public Consumer<CastInformation> getAfterAllTicksEvent() { return afterAllTicksEvent; }
    public boolean isDamagingSpell() { return isDamagingSpell; }
    public double getDamageAmount() { return damageAmount; }
    public boolean isDamageMultiplied() { return damageMultiplied; }
    public double getKnockbackAmount() { return knockbackAmount; }
    public boolean isKnockbackMultiplied() { return knockbackMultiplied; }
    public boolean isHealingSpell() { return isHealingSpell; }
    public double getHealAmount() { return healAmount; }
    public boolean isHealMultiplied() { return healMultiplied; }
    public boolean isEffectingSpell() { return isEffectingSpell; }
    public boolean isAmplificationMultiplied() { return amplificationMultiplied; }
    public boolean isEffectDurationMultiplied() { return effectDurationMultiplied; }
}
