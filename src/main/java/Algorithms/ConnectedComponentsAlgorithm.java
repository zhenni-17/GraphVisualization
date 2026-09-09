package Algorithms;

import Visualizer.Edge;
import Visualizer.Vertex;

import java.util.*;

public class ConnectedComponentsAlgorithm implements GraphAlgorithm {
    @Override
    public String run(Map<Vertex, List<Edge>> graph, Vertex start) {
        StringBuilder result = new StringBuilder("Connected Components: ");
        Set<Vertex> visited = new HashSet<>();
        List<List<Vertex>> components = new ArrayList<>();
        
        for (Vertex vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                List<Vertex> component = new ArrayList<>();
                dfsComponent(graph, vertex, visited, component);
                if (!component.isEmpty()) {
                    components.add(component);
                }
            }
        }
        
        for (int i = 0; i < components.size(); i++) {
            result.append("Component ").append(i + 1).append(": ");
            List<Vertex> component = components.get(i);
            component.sort(Comparator.comparing(Vertex::getId));
            for (Vertex vertex : component) {
                result.append(vertex.getId()).append(", ");
            }
            result.append("| ");
        }
        
        String output = result.toString();
        return output.substring(0, output.length() - 3);
    }
    
    private void dfsComponent(Map<Vertex, List<Edge>> graph, Vertex vertex, 
                               Set<Vertex> visited, List<Vertex> component) {
        visited.add(vertex);
        component.add(vertex);
        
        for (Edge edge : graph.getOrDefault(vertex, Collections.emptyList())) {
            Vertex neighbor = edge.getVertex2();
            if (!visited.contains(neighbor)) {
                dfsComponent(graph, neighbor, visited, component);
            }
        }
    }
}