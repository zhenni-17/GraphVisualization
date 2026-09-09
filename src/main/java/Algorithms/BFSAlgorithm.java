package Algorithms;

import Visualizer.Edge;
import Visualizer.Vertex;

import java.util.*;

public class BFSAlgorithm implements GraphAlgorithm {

    @Override
    public String run(Map<Vertex, List<Edge>> graph, Vertex start) {
        String traversalPath = "BFS : ";

        Set<Vertex> visited = new HashSet<>();
        Queue<Vertex> queue = new LinkedList<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            traversalPath += processVertex(current);

            List<Edge> currentVertexEdges = graph.get(current);
            Collections.sort(currentVertexEdges);

            for (Edge edge: currentVertexEdges) {
                Vertex neighbor = edge.getVertex2();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return traversalPath.substring(0, traversalPath.length() - 4);
    }
}