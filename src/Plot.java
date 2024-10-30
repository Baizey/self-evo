import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Plot extends JPanel {
    private final Point[] points;
    private int[] order;

    public Plot(Point[] points) {
        this.points = points;
        this.order = IntStream.range(0, points.length).toArray();
    }

    public void update(int[] order) {
        this.order = Arrays.copyOf(order, order.length);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        int padding = 40;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int minX = Arrays.stream(points).mapToInt(p -> p.x).min().orElse(0);
        int maxX = Arrays.stream(points).mapToInt(p -> p.x).max().orElse(1);
        int minY = Arrays.stream(points).mapToInt(p -> p.y).min().orElse(0);
        int maxY = Arrays.stream(points).mapToInt(p -> p.y).max().orElse(1);

        double xScale = (panelWidth - 2 * padding) / (double) (maxX - minX);
        double yScale = (panelHeight - 2 * padding) / (double) (maxY - minY);

        for (int i = 0; i < points.length; i++) {
            Point p = points[order[i]];
            int scaledX = (int) ((p.x - minX) * xScale + padding);
            int scaledY = (int) ((p.y - minY) * yScale + padding);

            g.fillOval(scaledX - 3, scaledY - 3, 6, 6);

            Point nextP = points[order[(i + 1) % points.length]];
            int scaledNextX = (int) ((nextP.x - minX) * xScale + padding);
            int scaledNextY = (int) ((nextP.y - minY) * yScale + padding);
            g.drawLine(scaledX, scaledY, scaledNextX, scaledNextY);
        }
    }

    public static Plot frame(String title, Point[] points) {
        var frame = new JFrame(title);
        var plot = new Plot(points);
        frame.add(plot);
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return plot;
    }
}
