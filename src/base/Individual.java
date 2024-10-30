package base;

import java.util.function.Function;

import static java.lang.Double.NEGATIVE_INFINITY;

public class Individual {
    private double fitness = NEGATIVE_INFINITY;

    private final Function<int[], Double> fitnessEvaluator;
    public final int[] chromosomes;

    public int chromosomeSplits;
    public int mutationRate;
    public int selectionPressure;

    public double fitness() {
        if (fitness != NEGATIVE_INFINITY) return fitness;
        fitness = fitnessEvaluator.apply(chromosomes);
        return fitness;
    }

    public void resetFitness() {
        fitness = NEGATIVE_INFINITY;
    }

    public Individual(
            Function<int[], Double> fitnessEvaluator,
            int[] chromosomes,
            int geneSplits,
            int mutationRate,
            int selectionAttempts
    ) {
        this.fitnessEvaluator = fitnessEvaluator;
        this.chromosomes = chromosomes;
        this.chromosomeSplits = geneSplits;
        this.mutationRate = mutationRate;
        this.selectionPressure = selectionAttempts;
        resetFitness();
    }
}

