package base;


import static java.lang.Math.*;
import static java.lang.Math.min;

public class Constraint {
    protected final Random random;
    public final int minValue;
    public final int maxValue;
    public final int range;
    public final int maxBound;

    private Constraint(
            Random random,
            int minValue,
            int maxValue,
            int range,
            int maxBound
    ) {
        this.random = random;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.range = range;
        this.maxBound = maxBound;
    }

    public Constraint(Random random, int min, int max) {
        this(random, min, max, max - min, max + 1);
        if (min > max) throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
    }

    public int getRandomValue() {
        return random.nextInt(minValue, maxBound);
    }

    public int mutate(int at) {
        var r = random.nextDouble();
        if (r < .33) return max(at / 2, minValue);
        if (r < .66) return max(1, min(at * 2, maxValue));
        return at;
    }
}

