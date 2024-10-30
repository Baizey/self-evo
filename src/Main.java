import base.*;
import crossover.NonFungableCrossover;
import fitness.TSPFitness;
import individual.TSPIndividualGenerator;
import mutations.TriSwapMutation;
import selector.LazySelector;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        final var random = new Random();
        final int chromosomeSize = 50;

        final int minX = 0;
        final int maxX = 1000;
        final int minY = 0;
        final int maxY = 1000;
        var points = IntStream
                .generate(() -> 0).limit(chromosomeSize)
                .mapToObj(ignored -> new Point(random.nextInt(minX, maxX), random.nextInt(minY, maxY)))
                .toArray(Point[]::new);

        final var populationSize = new PopulationSize(new Constraint(random, 1, chromosomeSize * 3));
        final var selectionPressure = new Constraint(random, 1, populationSize.maxValue() / 2);
        final var mutationRate = new Constraint(random, 1, chromosomeSize / 2);
        final var chromosomeSplits = new Constraint(random, 1, chromosomeSize / 3);

        final var individualGenerator = new TSPIndividualGenerator(random, chromosomeSize, points);
        final var selector = new LazySelector(random);
        final var crossover = new NonFungableCrossover();
        final var mutator = new TriSwapMutation(random);
        final var fitness = new TSPFitness(points);

        var world = new World(random,
                chromosomeSize, individualGenerator,
                populationSize, selectionPressure, chromosomeSplits, mutationRate,
                selector, crossover, mutator, fitness
        );

        var live = Plot.frame("Live", points);
        var best = Plot.frame("Best", points);
        var a = world.evolve(e -> {
            live.update(world.getBest().chromosomes);
            best.update(e);
        });
        System.out.println(Arrays.toString(a.chromosomes));
        System.out.println(Math.abs(a.fitness()));
        best.update(a.chromosomes);
    }
}

