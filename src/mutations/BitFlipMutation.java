package mutations;

import base.Random;

public class BitFlipMutation implements Mutator {
    private final Random random;

    public BitFlipMutation(Random random) {
        this.random = random;
    }

    @Override
    public void accept(int[] arr) {
        arr[random.nextInt(0, arr.length)] ^= 1;
    }
}

