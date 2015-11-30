package chai_4d.mbus.map.dijkstra.model;

import java.util.List;

public class Graph
{
    private final List<Point> points;
    private final List<Line> lines;

    public Graph(List<Point> points, List<Line> lines)
    {
        this.points = points;
        this.lines = lines;
    }

    public List<Point> getPoints()
    {
        return points;
    }

    public List<Line> getLines()
    {
        return lines;
    }
}
