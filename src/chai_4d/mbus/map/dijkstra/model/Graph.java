package chai_4d.mbus.map.dijkstra.model;

import java.util.List;
import java.util.Map;

public class Graph
{
    private final Map<Integer, Point> points;
    private final List<Line> lines;

    public Graph(Map<Integer, Point> points, List<Line> lines)
    {
        this.points = points;
        this.lines = lines;
    }

    public Map<Integer, Point> getPoints()
    {
        return points;
    }

    public List<Line> getLines()
    {
        return lines;
    }
}
