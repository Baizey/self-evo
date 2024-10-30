package individual;

import base.Random;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class TSPIndividualGenerator implements IndividualGenerator {
    private final Random random;
    private final int chromosomeSize;
    private final Point[] points;
    private final double centerX;
    private final double centerY;

    public TSPIndividualGenerator(
            Random random,
            int chromosomeSize,
            Point[] points
    ) {
        this.random = random;
        this.chromosomeSize = chromosomeSize;
        this.points = points;

        centerX = Arrays.stream(points).mapToInt(e -> e.x).average().orElseThrow();
        centerY = Arrays.stream(points).mapToInt(e -> e.y).average().orElseThrow();
    }

    @Override
    public int[] get() {
        var r = random.nextDouble();
        if (r < .20) {
            return IntStream
                    .range(0, chromosomeSize).boxed()
                    .sorted((a1, b1) -> {
                        var p1 = points[a1];
                        var p2 = points[b1];
                        var angle1 = calculateAngle(p1.x, p1.y, centerX, centerY);
                        var angle2 = calculateAngle(p2.x, p2.y, centerX, centerY);
                        return Double.compare(angle1, angle2);
                    })
                    .mapToInt(e -> e).toArray();
        } else {
            var used = new boolean[chromosomeSize];
            var result = new int[chromosomeSize];
            var start = random.nextInt(0, points.length);
            result[0] = start;
            used[start] = true;


            Function<Integer, Integer> get = index -> {
                var point = points[index];
                double closest = Integer.MAX_VALUE;
                int closestIndex = -1;
                for (var i = 0; i < points.length; i++) {
                    if (i == index || used[i]) continue;
                    var dist = point.distance(points[i]);
                    if (closest > dist) {
                        closest = dist;
                        closestIndex = i;
                    }
                }
                return closestIndex;
            };
            for (var i = 0; i < chromosomeSize - 1; i++) {
                result[i + 1] = get.apply(result[i]);
                used[result[i + 1]] = true;
            }

            return result;
        }
    }

    private static double calculateAngle(double x, double y, double centerX, double centerY) {
        double angleDegrees = Math.toDegrees(Math.atan2(-(x - centerX), y - centerY));
        return angleDegrees < 0 ? angleDegrees + 360 : angleDegrees;
    }
}
