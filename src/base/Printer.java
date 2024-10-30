package base;

import java.util.stream.Stream;

public class Printer {
    private final String header;
    private final long start;
    private long lineCount = 0;

    public Printer() {
        this.start = System.nanoTime();
        var headerInfo = String.join(" | ", Stream.of(
                pad("Time"),
                pad("Total population"),
                pad("Generation"),
                pad("Best fitness"),
                pad("Mutation rate"),
                pad("Gene splits"),
                pad("Selection pressure"),
                pad("Alive population"),
                pad("Stagnation")
        ).map(String::valueOf).toList());
        var filler = "#".repeat(headerInfo.length());
        this.header = filler + "\n" + headerInfo + "\n" + filler;
        log(header);
    }

    public void print(World world, double stagnation) {
        var best = world.getBest();
        lineCount++;
        if (lineCount % 20 == 0) {
            log(header);
        }

        var timeSpent = (((double) (System.nanoTime() - start)) / 1e9);
        var fitnessScore = best.fitness();
        var meanMutationRate = world.currentPopulation().mapToInt(e -> e.mutationRate).average().getAsDouble();
        var meanChromosomeSplits = world.currentPopulation().mapToInt(e -> e.chromosomeSplits).average().getAsDouble();
        var meanSelectionPressure = world.currentPopulation().mapToInt(e -> e.selectionPressure).average().getAsDouble();

        var str = String.join(" | ", Stream.of(
                pad(String.format("%.2f", timeSpent)),
                pad(world.totalPopulation()),
                pad(world.generations()),
                pad(fitnessScore),
                pad(String.format("%.2f", meanMutationRate)),
                pad(String.format("%.2f", meanChromosomeSplits)),
                pad(String.format("%.2f", meanSelectionPressure)),
                pad(world.populationSize()),
                pad((int) (stagnation * 100) + " %")
        ).map(String::valueOf).toList());
        log(str);
    }

    private void log(String s) {
        System.out.println(s);
    }

    private static String pad(Object obj) {
        var size = 20;
        var str = String.valueOf(obj);
        return " ".repeat(size - str.length()) + str;
    }

    public record GenerationInfo(
            double runtimeSeconds,
            double bestFitness,
            long totalPopulation,
            long totalGenerations,
            int populationSize,
            double meanMutationRate,
            double meanSelectionPressure,
            double meanChromosomeSplits
    ) {
        @Override
        public String toString() {
            return String.join(" | ", Stream.of(
                    pad(String.format("%.2f", runtimeSeconds)),
                    pad(totalPopulation),
                    pad(totalGenerations),
                    pad(bestFitness),
                    pad(String.format("%.2f", meanMutationRate)),
                    pad(String.format("%.2f", meanChromosomeSplits)),
                    pad(String.format("%.2f", meanSelectionPressure)),
                    pad(populationSize)
            ).map(String::valueOf).toList());
        }
    }
}

