import base.Individual;
import base.TriConsumer;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ma {

    public static void main(String... args) {
        var chromosomeSize = 100;
        for (var s = 1; s < chromosomeSize; s++) {
            System.out.println(s);
            TriConsumer<Individual, Individual, Individual> crossover = (parent1, parent2, child) -> {
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
            };

            var parent1 = new Individual(e -> 0D, IntStream.range(0, chromosomeSize).toArray(), s, 0, 0);
            var parent2 = new Individual(e -> 0D, IntStream.range(0, chromosomeSize).boxed().sorted((a, b) -> b - a).mapToInt(e -> e).toArray(), s, 0, 0);
            var child = new Individual(e -> 0D, new int[chromosomeSize], s, 0, 0);

            crossover.consume(parent1, parent2, child);
            System.out.println(Arrays.toString(parent1.chromosomes));
            System.out.println(Arrays.toString(parent2.chromosomes));
            System.out.println(Arrays.toString(child.chromosomes));
        }
    }

    public static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) if (array[i] == value) return i;
        return -1;
    }
}
