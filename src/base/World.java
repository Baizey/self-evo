package base;

import crossover.Crossover;
import fitness.FitnessEvaluator;
import individual.IndividualGenerator;
import mutations.Mutator;
import selector.Selector;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static java.util.Comparator.comparingDouble;

public class World {
    private final Random random;
    private final double[][] mutationChances;
    private long generations = 0;
    private long totalPopulation;

    private final PopulationSize populationSize;
    private final Constraint selectionPressure;
    private final Constraint chromosomeSplits;
    private final Constraint mutationRate;

    private int currentPopulationSize;
    private Individual[] livePopulation;
    private Individual[] deadPopulation;

    private final int chromosomeSize;
    private final IndividualGenerator chromosomeProvider;

    private final Selector selector;
    private final Crossover crossover;
    private final Mutator mutator;
    public final FitnessEvaluator fitnessEvaluator;

    public World(
            Random random,
            int chromosomeSize,
            IndividualGenerator chromosomeProvider,
            PopulationSize populationSize,
            Constraint selectionPressure,
            Constraint geneSplits,
            Constraint mutationRate,
            Selector selector,
            Crossover crossover,
            Mutator mutator,
            FitnessEvaluator fitnessEvaluator
    ) {
        this.crossover = crossover;
        this.mutator = mutator;
        this.random = random;
        this.chromosomeSize = chromosomeSize;
        this.chromosomeProvider = chromosomeProvider;
        this.populationSize = populationSize;
        this.selectionPressure = selectionPressure;
        this.chromosomeSplits = geneSplits;
        this.mutationRate = mutationRate;
        this.selector = selector;
        this.fitnessEvaluator = fitnessEvaluator;
        this.currentPopulationSize = populationSize.maxValue();

        this.totalPopulation = populationSize.maxBound();
        this.livePopulation = Stream
                .generate(this::randomIndividual)
                .limit(this.populationSize.maxBound())
                .toArray(Individual[]::new);
        this.deadPopulation = Stream
                .generate(this::emptyIndividual)
                .limit(this.populationSize.maxBound())
                .toArray(Individual[]::new);

        mutationChances = new double[mutationRate.maxBound - mutationRate.minValue + 1][];
        for (var mutationChance = mutationRate.minValue; mutationChance < mutationRate.maxBound; mutationChance++) {
            double p = (double) mutationChance / this.chromosomeSize;
            double[] odds = new double[this.chromosomeSize + 1];

            odds[0] = Math.pow(1 - p, this.chromosomeSize);

            for (int k = 1; k <= this.chromosomeSize; k++) {
                odds[k] = odds[k - 1] * ((this.chromosomeSize - (k - 1)) * p) / (k * (1 - p));
            }

            for (int k = 1; k <= this.chromosomeSize; k++) {
                odds[k] += odds[k - 1];
            }

            mutationChances[mutationChance - mutationRate.minValue] = odds;
        }
    }

    public Individual evolve(Consumer<int[]> onUpdate) {
        final var printer = new Printer();
        final var bestChromosome = randomIndividual().chromosomes;
        var bestFitness = Double.NEGATIVE_INFINITY;

        final long maxStagnantGenerations = 10_000L * chromosomeSize * populationSize.maxValue();
        long stagnantPopulations = 0L;

        while (stagnantPopulations < maxStagnantGenerations) {
            evolveGeneration();

            var current = getBest();
            if (current.fitness() > bestFitness) {
                arraycopy(current.chromosomes, 0, bestChromosome, 0, chromosomeSize);
                bestFitness = current.fitness();
                stagnantPopulations = 0;
            } else {
                stagnantPopulations += currentPopulationSize;
            }

            if (generations == 10 || generations == 100 || generations % 1000 == 0) {
                onUpdate.accept(bestChromosome);
                var stagnation = stagnantPopulations / (double) maxStagnantGenerations;
                printer.print(this, stagnation);
            }
        }
        onUpdate.accept(bestChromosome);
        printer.print(this, 1);

        return new Individual(fitnessEvaluator, bestChromosome, 0, 0, 0);
    }

    public void evolveGeneration() {
        var prevBest = getBest().fitness();

        generations++;

        var nextPopulationSize = populationSize.get();

        var pressure = livePopulation[0];
        Arrays.stream(deadPopulation).limit(nextPopulationSize).parallel().forEach(child -> {
            var parent1 = selector.apply(livePopulation, currentPopulationSize, pressure);
            var parent2 = selector.apply(livePopulation, currentPopulationSize, parent1);

            child.resetFitness();
            child.mutationRate = mutationRate.mutate(parent1.mutationRate);
            child.chromosomeSplits = chromosomeSplits.mutate(parent1.chromosomeSplits);
            child.selectionPressure = selectionPressure.mutate(parent1.selectionPressure);

            crossover.consume(parent1, parent2, child);

            var childMutations = mutations(child);
            for (var i = 0; i < childMutations; i++)
                mutator.accept(child.chromosomes);
        });

        var tmp = livePopulation;
        livePopulation = deadPopulation;
        deadPopulation = tmp;

        totalPopulation += nextPopulationSize;
        currentPopulationSize = nextPopulationSize;

        var nextBest = getBest().fitness();

        populationSize.mutate(prevBest, nextBest);
    }

    public long generations() {
        return generations;
    }

    public long totalPopulation() {
        return totalPopulation;
    }

    public Stream<Individual> currentPopulation() {
        return Arrays.stream(livePopulation).limit(currentPopulationSize);
    }

    public int populationSize() {
        return currentPopulationSize;
    }

    public Individual getBest() {
        return Arrays
                .stream(livePopulation).limit(currentPopulationSize)
                .parallel()
                .max(comparingDouble(Individual::fitness)).orElseThrow();
    }

    private int mutations(Individual child) {
        var table = mutationChances[child.mutationRate - mutationRate.minValue];
        var r = random.nextDouble();
        for (var i = 0; i < table.length; i++)
            if (r < table[i]) return i;
        return 0;
    }

    private Individual emptyIndividual() {
        return new Individual(fitnessEvaluator, new int[chromosomeSize], 0, 0, 0);
    }

    private Individual randomIndividual() {
        var chromosome = chromosomeProvider.get();
        return new Individual(
                fitnessEvaluator,
                chromosome,
                chromosomeSplits.getRandomValue(),
                mutationRate.getRandomValue(),
                selectionPressure.getRandomValue()
        );
    }

}
