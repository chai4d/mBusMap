package chai_4d.mbus.map.dijkstra.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chai_4d.mbus.map.dijkstra.model.Graph;
import chai_4d.mbus.map.dijkstra.model.Line;
import chai_4d.mbus.map.dijkstra.model.Point;

public class DijkstraAlgorithm
{
    // private final List<Point> points;
    private final List<Line> lines;
    private Set<Point> settledNodes;
    private Set<Point> unSettledNodes;
    private Map<Point, Point> predecessors;
    private Map<Point, Integer> distance;

    public DijkstraAlgorithm(Graph graph)
    {
        // create a copy of the array so that we can operate on this array
        // this.points = new ArrayList<Point>(graph.getPoints());
        this.lines = new ArrayList<Line>(graph.getLines());
    }

    public void execute(Point source)
    {
        settledNodes = new HashSet<Point>();
        unSettledNodes = new HashSet<Point>();
        distance = new HashMap<Point, Integer>();
        predecessors = new HashMap<Point, Point>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0)
        {
            Point node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Point node)
    {
        List<Point> adjacentNodes = getNeighbors(node);
        for (Point target : adjacentNodes)
        {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target))
            {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(Point node, Point target)
    {
        for (Line line : lines)
        {
            if (line.getSource().equals(node) && line.getDestination().equals(target))
            {
                return line.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Point> getNeighbors(Point node)
    {
        List<Point> neighbors = new ArrayList<Point>();
        for (Line line : lines)
        {
            if (line.getSource().equals(node) && !isSettled(line.getDestination()))
            {
                neighbors.add(line.getDestination());
            }
        }
        return neighbors;
    }

    private Point getMinimum(Set<Point> vertexes)
    {
        Point minimum = null;
        for (Point vertex : vertexes)
        {
            if (minimum == null)
            {
                minimum = vertex;
            }
            else
            {
                if (getShortestDistance(vertex) < getShortestDistance(minimum))
                {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Point vertex)
    {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Point destination)
    {
        Integer d = distance.get(destination);
        if (d == null)
        {
            return Integer.MAX_VALUE;
        }
        else
        {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Point> getPath(Point target)
    {
        LinkedList<Point> path = new LinkedList<Point>();
        Point step = target;
        // check if a path exists
        if (predecessors.get(step) == null)
        {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null)
        {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }
}
