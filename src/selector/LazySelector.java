package selector;

import base.Individual;
import base.Random;

public class LazySelector implements Selector {
    private final Random random;

    public LazySelector(Random random) {
        this.random = random;
    }

    @Override
    public Individual apply(Individual[] livePopulation, Integer size, Individual otherParent) {
        var best = livePopulation[random.nextInt(0, size)];
        var usedSelectionAttempts = otherParent.selectionPressure;
        for (var i = 0; i < usedSelectionAttempts; i++) {
            var curr = livePopulation[random.nextInt(0, size)];
            if (curr.fitness() > best.fitness() && curr != otherParent) {
                best = curr;
            }
        }
        return best;
    }
}
