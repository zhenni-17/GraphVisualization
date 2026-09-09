package Algorithms;

import Visualizer.Edge;
import Visualizer.Vertex;

import java.util.*;

public class TopologicalSortAlgorithm implements GraphAlgorithm {
    @Override
    public String run(Map<Vertex, List<Edge>> graph, Vertex start) {
        if (hasCycle(graph)) {
            return "Topological Sort: Graph contains a cycle!";
        }
        
        StringBuilder result = new StringBuilder("Topological Sort: ");
        Map<Vertex, Integer> indegree = new HashMap<>();
        
        for (Vertex vertex : graph.keySet()) {
            indegree.put(vertex, 0);
        }
        
        for (List<Edge> edges : graph.values()) {
            for (Edge edge : edges) {
                Vertex target = edge.getVertex2();
                indegree.put(target, indegree.get(target) + 1);
            }
        }
        
        Queue<Vertex> queue = new LinkedList<>();
        for (Map.Entry<Vertex, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        List<Vertex> sortedVertices = new ArrayList<>();
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            sortedVertices.add(current);
            
            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                Vertex neighbor = edge.getVertex2();
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        for (Vertex vertex : sortedVertices) {
            result.append(vertex.getId()).append(" -> ");
        }
        
        return result.substring(0, result.length() - 4);
    }
    
    private boolean hasCycle(Map<Vertex, List<Edge>> graph) {
        Set<Vertex> visited = new HashSet<>();
        Set<Vertex> recursionStack = new HashSet<>();
        
        for (Vertex vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleUtil(graph, vertex, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasCycleUtil(Map<Vertex, List<Edge>> graph, Vertex vertex, 
                                  Set<Vertex> visited, Set<Vertex> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex);
        
        for (Edge edge : graph.getOrDefault(vertex, Collections.emptyList())) {
            Vertex neighbor = edge.getVertex2();
            if (!visited.contains(neighbor)) {
                if (hasCycleUtil(graph, neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }
        recursionStack.remove(vertex);
        return false;
    }
}