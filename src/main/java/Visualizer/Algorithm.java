package Visualizer;

import Algorithms.*;

public enum Algorithm {

    BFS(new BFSAlgorithm()),
    DFS(new DFSAlgorithm()),
    DIJKSTRAS(new DijkstrasAlgorithm()),
    PRIMS(new PrimsAlgorithm()),
    TOPOLOGICAL_SORT(new TopologicalSortAlgorithm()),
    CONNECTED_COMPONENTS(new ConnectedComponentsAlgorithm());

    private final GraphAlgorithm algorithmInstance;

    Algorithm(GraphAlgorithm algorithmInstance) {
        this.algorithmInstance = algorithmInstance;
    }

    public GraphAlgorithm getAlgorithmInstance() {
        return algorithmInstance;
    }
}
