package mutations;

import base.Random;

import java.util.function.Consumer;

public class SwapMutation implements Mutator {
    private final Random random;

    public SwapMutation(Random random) {
        this.random = random;
    }

    @Override
    public void accept(int[] arr) {
        var a = random.nextInt(0, arr.length);
        var b = random.nextInt(0, arr.length);
        var tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }
}


