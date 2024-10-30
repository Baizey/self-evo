package crossover;

import base.Individual;

public class FungableCrossover implements Crossover {
    @Override
    public void consume(Individual parent1, Individual parent2, Individual child) {
        var p1 = parent1.chromosomes;
        var p2 = parent2.chromosomes;
        var c = child.chromosomes;

        var splits = child.chromosomeSplits;
        var size = c.length / splits;

        for (var i = 0; i < size; i++) {
            var p = (i & 1) == 0 ? p1 : p2;
            System.arraycopy(p, i * size, c, i * size, size);
        }

        System.arraycopy(p1, splits * size, c, splits * size, c.length - splits * size);
    }
}

