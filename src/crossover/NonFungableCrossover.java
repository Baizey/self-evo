package crossover;

import base.Individual;

public class NonFungableCrossover implements Crossover {
    @Override
    public void consume(Individual parent1, Individual parent2, Individual child) {
        var chromosomeSize = child.chromosomes.length;
        var splits = child.chromosomeSplits;
        if (splits == 1) {
            System.arraycopy(parent1.chromosomes, 0, child.chromosomes, 0, chromosomeSize);
            return;
        }

        var size = chromosomeSize / splits;

        var c = child.chromosomes;

        var seen = new boolean[chromosomeSize];
        var i = 0;
        c[i] = parent1.chromosomes[i];
        seen[c[i]] = true;

        var chunk = 0;
        while (i < chromosomeSize - 1) {
            var p = ((chunk & 1) == 0) ? parent1.chromosomes : parent2.chromosomes;
            var pIndex = indexOf(p, c[i]);

            var count = 0;
            do {
                i++;
                while (seen[p[pIndex]]) {
                    pIndex = (pIndex + 1) % chromosomeSize;
                }
                count++;
                c[i] = p[pIndex];
                seen[p[pIndex]] = true;
            } while (i < chromosomeSize - 1 && count < size);

            chunk++;
        }
    }

    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) if (array[i] == value) return i;
        return -1;
    }
}
