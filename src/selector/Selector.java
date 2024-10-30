package selector;

import base.Individual;
import base.Random;
import base.TriFunction;

public interface Selector extends TriFunction<Individual[], Integer, Individual, Individual> {
}

