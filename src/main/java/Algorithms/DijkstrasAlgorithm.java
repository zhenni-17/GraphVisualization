package Algorithms;

import Visualizer.Edge;
import Visualizer.Vertex;
import java.util.*;

public class DijkstrasAlgorithm implements GraphAlgorithm {
    @Override
    public String run(Map<Vertex, List<Edge>> graph, Vertex start) {
        Map<Vertex, Double> outputMap = new TreeMap<>();
        Map<Vertex, Double> distances = new HashMap<>();
        
        for (Vertex vertex: graph.keySet()) {
            if (!vertex.equals(start)) distances.put(vertex, Double.POSITIVE_INFINITY);
        }

        Set<Vertex> unprocessedVertices = new HashSet<>(distances.keySet());
        List<Edge> startVertexEdges = graph.get(start);

        for (Edge edge: startVertexEdges) {
            Vertex neighbor = edge.getVertex2();
            int weight = edge.getWeight();
            if (unprocessedVertices.contains(neighbor)) {
                double newDistance = (double) weight;
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                }
            }
        }

        while (!unprocessedVertices.isEmpty()) {
            Vertex current = findSmallestDistanceVertex(unprocessedVertices, distances);
            outputMap.put(current, distances.get(current));
            unprocessedVertices.remove(current);

            List<Edge> currentVertexEdges = graph.get(current);
            for (Edge edge: currentVertexEdges) {
                Vertex neighbor = edge.getVertex2();
                double weight = (double) edge.getWeight();
                if (unprocessedVertices.contains(neighbor)) {
                    double newDistance = distances.get(current) + weight;
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                    }
                }
            }
        }

        String shortestPaths = processDistances(outputMap);
        return shortestPaths.substring(0, shortestPaths.length() - 2);
    }

    private static Vertex findSmallestDistanceVertex(Set<Vertex> unprocessedVertices, Map<Vertex, Double> distances) {
        Vertex smallestVertex = null;
        double smallestDistance = Double.POSITIVE_INFINITY;

        for (Vertex vertex: unprocessedVertices) {
            if (distances.get(vertex) <= smallestDistance) {
                smallestVertex = vertex;
                smallestDistance = distances.get(vertex);
            }
        }
        return smallestVertex;
    }

    private String processDistances(Map<Vertex, Double> map) {
        String shortestPaths = "";
        for (Vertex vertex: map.keySet()) {
            Double weight = map.get(vertex);
            if (weight == Double.POSITIVE_INFINITY) {
                shortestPaths += vertex.getId() + "=" + weight + ", ";
            } else {
                shortestPaths += vertex.getId() + "=" + weight.intValue() + ", ";
            }
        }
        return shortestPaths;
    }
}