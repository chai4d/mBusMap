package chai_4d.mbus.map.dijkstra.model;

public class Line
{
    private final Point source;
    private final Point destination;
    private final double weight;

    public Line(Point source, Point destination, double weight)
    {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Point getSource()
    {
        return source;
    }

    public Point getDestination()
    {
        return destination;
    }

    public double getWeight()
    {
        return weight;
    }

    @Override
    public String toString()
    {
        return source + " -> " + destination;
    }
}
