package Algorithms;

import Visualizer.Edge;
import Visualizer.Vertex;

import java.util.*;

public class DFSAlgorithm implements GraphAlgorithm {

    @Override
    public String run(Map<Vertex, List<Edge>> graph, Vertex start) {
        if (graph.get(start).isEmpty()) {
            return "DFS -> " + start.getId();
        }

        String traversalPath = "DFS : ";
        Set<Vertex> visited = new HashSet<>();
        traversalPath += helper(graph, start, visited);

        for (Vertex vertex: graph.keySet()) {
            if (!visited.contains(vertex) && !graph.get(vertex).isEmpty()) {
                traversalPath += helper(graph, vertex, visited);
            }
        }

        return traversalPath.substring(0, traversalPath.length() - 4);
    }

    private String helper(Map<Vertex, List<Edge>> graph, Vertex vertex, Set<Vertex> visited) {
        String output = "";
        visited.add(vertex);
        output += processVertex(vertex);

        List<Edge> currentVertexEdges = graph.get(vertex);
        Collections.sort(currentVertexEdges);

        for (Edge edge: currentVertexEdges) {
            Vertex neighbor = edge.getVertex2();
            if (!visited.contains(neighbor)) {
                output += helper(graph, neighbor, visited);
            }
        }

        return output;
    }
}