package base;

public class Random {
    private long seed;

    public Random() {
        this(System.nanoTime());
    }

    public Random(long seed) {
        this.seed = seed;
    }

    /**
     * Returns a random double between 0 and 1
     */
    public double nextDouble() {
        return (next() >>> 12) * 0x1.0p-51;
    }

    /**
     * Returns a random value between min and max [min;max)
     */
    public double nextDouble(double min, double max) {
        return min + nextDouble() * (max - min);
    }

    /**
     * Returns a random value between min and max [min;max)
     */
    public int nextInt(int min, int max) {
        return (int) (min + (next() % (max - min)));
    }

    private long next() {
        seed ^= (seed << 21);
        seed ^= (seed >>> 35);
        seed ^= (seed << 4);
        return Math.abs(seed);
    }
}
