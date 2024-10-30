package mutations;

import base.Random;

public class TriSwapMutation implements Mutator {
    private final Random random;

    public TriSwapMutation(Random random) {
        this.random = random;
    }

    @Override
    public void accept(int[] chromosomes) {
        final var a = random.nextInt(0, chromosomes.length);
        final var b = random.nextInt(0, chromosomes.length);
        final var r = random.nextDouble();
        {
            final var tmp = chromosomes[a];
            chromosomes[a] = chromosomes[b];
            chromosomes[b] = tmp;
        }
        if (r > .50) {
            final var a2 = (a + 1) % chromosomes.length;
            final var b2 = (b + 1) % chromosomes.length;
            final var tmp = chromosomes[a2];
            chromosomes[a2] = chromosomes[b2];
            chromosomes[b2] = tmp;
        }
        if (r > .75) {
            final var a3 = (a + 2) % chromosomes.length;
            final var b3 = (b + 2) % chromosomes.length;
            final var tmp = chromosomes[a3];
            chromosomes[a3] = chromosomes[b3];
            chromosomes[b3] = tmp;
        }
    }
}
