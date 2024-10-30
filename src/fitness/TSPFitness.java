package fitness;

import java.awt.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class TSPFitness implements FitnessEvaluator {
    private final Point[] points;

    public TSPFitness(Point[] points) {
        this.points = points;
    }

    @Override
    public Double apply(int[] chromosomes) {
        double sum = sqrt(pow(
                        points[chromosomes[0]].x - points[chromosomes[chromosomes.length - 1]].x,
                        2
                ) + pow(
                        points[chromosomes[0]].y - points[chromosomes[chromosomes.length - 1]].y,
                        2
                )
        );
        for (var i = 1; i < chromosomes.length; i++) {
            var a = points[chromosomes[i - 1]];
            var b = points[chromosomes[i]];
            sum += sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2));
        }
        return -sum;
    }
}
