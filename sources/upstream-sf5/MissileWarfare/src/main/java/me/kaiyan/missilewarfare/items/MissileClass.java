package me.kaiyan.missilewarfare.items;

/**
 * Represents the statistical attributes of a missile type.
 *
 * @author MissileWarfare contributors
 */
public class MissileClass {
    public double speed;
    public int range;
    public double power;
    public int accuracy;
    public int type;

    /**
     * Creates a new missile class with the given attributes.
     *
     * @param speed    the missile speed
     * @param range    the missile range (will be squared internally)
     * @param power    the explosive power
     * @param accuracy the accuracy radius in blocks
     * @param type     the missile type identifier
     */
    public MissileClass(double speed, int range, double power, int accuracy, int type) {
        this.speed = speed;
        this.range = range * range;
        this.power = power;
        this.accuracy = accuracy;
        this.type = type;
    }
}
