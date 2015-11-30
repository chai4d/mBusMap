package chai_4d.mbus.map.dijkstra.model;

public class Line
{
    private final String id;
    private final Point source;
    private final Point destination;
    private final int weight;

    public Line(String id, Point source, Point destination, int weight)
    {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getId()
    {
        return id;
    }

    public Point getDestination()
    {
        return destination;
    }

    public Point getSource()
    {
        return source;
    }

    public int getWeight()
    {
        return weight;
    }

    @Override
    public String toString()
    {
        return source + " " + destination;
    }
}
