package base;

public class PopulationSize {
    private final Constraint constraint;
    private int current;

    public PopulationSize(Constraint constraint) {
        this.constraint = constraint;
        this.current = constraint.minValue;
    }

    public int minValue() {
        return constraint.minValue;
    }

    public int maxValue() {
        return constraint.maxValue;
    }

    public int maxBound() {
        return constraint.maxBound;
    }

    public int range() {
        return constraint.range;
    }

    public int get() {
        return current;
    }

    public void mutate(
            double previousFitness,
            double nextFitness
    ) {
        if (nextFitness > previousFitness) {
            current = Math.max(current / 2, constraint.minValue);
        } else {
            current = Math.min(current * 2, constraint.maxValue);
        }
    }
}
