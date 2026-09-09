package Algorithms;

import Visualizer.Edge;
import Visualizer.Vertex;

import java.util.*;

public class AlgorithmSetter {

    private GraphAlgorithm algorithm;

    public void setAlgorithm(GraphAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String execute(Map<Vertex, List<Edge>> graph, Vertex start) {
        return this.algorithm.run(graph, start);
    }
}