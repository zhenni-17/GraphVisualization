package Visualizer;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import Algorithms.*;

public class Graph extends JPanel implements MouseListener {

    protected static List<Vertex> edgeVertices = new ArrayList<>();
    protected static List<List<String>> availableEdges = new ArrayList<>();
    private Timer animationTimer;
    private List<String> animationSteps;
    private int currentStep;
    private boolean isAnimating = false;

    public Graph() {
        setName("Graph");
        setBackground(MainFrame.BACKGROUND_COLOR);
        setLayout(null);
        setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
        setLocation(0, 0);
        addMouseListener(this);
    }

    public void animateAlgorithm(String path) {
        if (isAnimating) return;
        
        animationSteps = new ArrayList<>();
        String[] steps = path.split(" -> ");
        if (steps.length == 0) return;
        
        if (path.startsWith("Connected Components") || path.contains("contains a cycle")) {
            MainFrame.getAlgorithmDisplayLabel().setText(path);
            return;
        }
        
        for (String step : steps) {
            animationSteps.add(step.trim());
        }
        
        currentStep = 0;
        isAnimating = true;
        
        for (Vertex vertex : Vertex.vertices.values()) {
            vertex.resetColor();
        }
        
        animationTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep >= animationSteps.size()) {
                    animationTimer.stop();
                    isAnimating = false;
                    MainFrame.getAlgorithmDisplayLabel().setText(path);
                    return;
                }
                
                String step = animationSteps.get(currentStep);
                for (Vertex vertex : Vertex.vertices.values()) {
                    if (vertex.getId().equals(step)) {
                        vertex.setColor(Vertex.CURRENT_COLOR);
                        Timer colorTimer = new Timer(300, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                vertex.setColor(Vertex.VISITED_COLOR);
                            }
                        });
                        colorTimer.setRepeats(false);
                        colorTimer.start();
                    }
                }
                
                currentStep++;
                repaint();
            }
        });
        animationTimer.setRepeats(true);
        animationTimer.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (MainFrame.mode == Mode.ADD_A_VERTEX) {
            boolean validPosition = validPlacementForVertex(e.getX(), e.getY());
            if (validPosition) {
                while (true) {
                    String input = JOptionPane.showInputDialog(this, "Enter the Vertex ID (Should be 1 char):",
                            "Vertex", JOptionPane.QUESTION_MESSAGE);
                    if (input == null) {
                        break;
                    } else {
                        input = input.trim();
                        if (input.length() == 1 && validVertexID(input)) {
                            int xValue = e.getX() - Vertex.SIZE / 2;
                            int yValue = e.getY() - Vertex.SIZE / 2;
                            createVertex(xValue, yValue, input);
                            return;
                        } else {
                            JOptionPane.showMessageDialog(this,
                                "Invalid vertex ID. Please use a single character that isn't already used.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cannot place vertex here. Too close to existing vertex or edge.",
                    "Invalid Position",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else if (MainFrame.mode == Mode.ADD_AN_EDGE) {
            Vertex vertex = clickedOnVertex(e.getX(), e.getY());

            if (vertex != null) {
                edgeVertices.add(vertex);
                if (edgeVertices.size() == 2) {
                    for (List<String> verticesOfAnEdge: availableEdges) {
                        String id1 = edgeVertices.get(0).getId();
                        String id2 = edgeVertices.get(1).getId();
                        if (verticesOfAnEdge.contains(id1) && verticesOfAnEdge.contains(id2)) {
                            edgeVertices.clear();
                            JOptionPane.showMessageDialog(this,
                                "Edge already exists between these vertices.",
                                "Duplicate Edge",
                                JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    
                    Vertex vertex1 = edgeVertices.get(0);
                    Vertex vertex2 = edgeVertices.get(1);
                    edgeVertices.clear();

                    List<String> newIdCouple = new ArrayList<>();
                    newIdCouple.add(vertex1.getId());
                    newIdCouple.add(vertex2.getId());
                    availableEdges.add(newIdCouple);

                    drawEdge(vertex1, vertex2);
                }
            }
        } else if (MainFrame.mode == Mode.REMOVE_A_VERTEX) {
            Vertex vertex = clickedOnVertex(e.getX(), e.getY());

            if (vertex != null) {
                String id = vertex.getId();
                Vertex.vertices.remove(id);

                List<Edge> edges = new ArrayList<>();

                for (Edge edge: Edge.edges) {
                    Vertex vertex1 = edge.getVertex1();
                    Vertex vertex2 = edge.getVertex2();
                    if (vertex.equals(vertex1) || vertex.equals(vertex2)) {
                        if (edge.getLabel() != null) this.remove(edge.getLabel());
                        this.remove(edge);
                        String id1 = vertex1.getId();
                        String id2 = vertex2.getId();
                        List<List<String>> newAvailableEdges = new ArrayList<>();
                        for (List<String> verticesOfAnEdge: availableEdges) {
                            if (!(verticesOfAnEdge.contains(id1) && verticesOfAnEdge.contains(id2))) {
                                newAvailableEdges.add(verticesOfAnEdge);
                            }
                        }
                        availableEdges = newAvailableEdges;
                    } else {
                        edges.add(edge);
                    }
                }

                Edge.edges = edges;
                this.remove(vertex);
                this.repaint();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No vertex found at this location.",
                    "Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (MainFrame.mode == Mode.REMOVE_AN_EDGE) {
            Edge edge = clickedOnEdge(e.getX(), e.getY());

            if (edge != null) {
                List<Edge> edges = new ArrayList<>();
                List<Edge> edgesToBeExcluded = new ArrayList<>();

                for (Edge otherEdge: Edge.edges) {
                    if (edge.equals(otherEdge)) {
                        this.remove(otherEdge);
                        if (otherEdge.getLabel() != null) {
                            this.remove(otherEdge.getLabel());
                        }
                        edgesToBeExcluded.add(otherEdge);
                    } else {
                        edges.add(otherEdge);
                    }
                }

                Edge.edges = edges;

                for (Edge excludedEdge: edgesToBeExcluded) removeEdgeFromStaticList(excludedEdge);

                this.repaint();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No edge found at this location.",
                    "Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (MainFrame.mode == Mode.NONE && MainFrame.getAlgorithmDisplayLabel().isVisible()) {
            Vertex vertex = clickedOnVertex(e.getX(), e.getY());

            if (vertex != null && !isAnimating) {
                AlgorithmSetter algorithmSetter = new AlgorithmSetter();
                algorithmSetter.setAlgorithm(MainFrame.algorithm.getAlgorithmInstance());

                Map<Vertex, List<Edge>> graph = createGraphDataStructure();

                if (MainFrame.algorithm == Algorithm.TOPOLOGICAL_SORT && !isDAG(graph)) {
                    String result = algorithmSetter.execute(graph, vertex);
                    MainFrame.getAlgorithmDisplayLabel().setText(result);
                    return;
                }

                String path = algorithmSetter.execute(graph, vertex);
                
                if (path.contains("cycle") || path.startsWith("Connected Components")) {
                    MainFrame.getAlgorithmDisplayLabel().setText(path);
                } else {
                    animateAlgorithm(path);
                }
            } else if (vertex == null) {
                JOptionPane.showMessageDialog(this,
                    "Please click on a vertex to start the algorithm.",
                    "No Vertex Selected",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean isDAG(Map<Vertex, List<Edge>> graph) {
        Set<Vertex> visited = new HashSet<>();
        Set<Vertex> recursionStack = new HashSet<>();
        
        for (Vertex vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleUtil(graph, vertex, visited, recursionStack)) {
                    return false;
                }
            }
        }
        return true;
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

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private static Vertex clickedOnVertex(int x, int y) {
        for (var entry: Vertex.vertices.entrySet()) {
            Vertex vertex = entry.getValue();
            int xLocation = vertex.getXLocation();
            int yLocation = vertex.getYLocation();

            if (x >= xLocation && x <= xLocation + Vertex.SIZE
                    && y >= yLocation && y <= yLocation + Vertex.SIZE) {
                return vertex;
            }
        }
        return null;
    }

    private static Edge clickedOnEdge(int x, int y) {
        for (Edge edge: Edge.edges) {
            int x1 = edge.getX();
            int y1 = edge.getY() + (edge.getTopEqualsLeft() ? 0 : edge.getHeight());
            int x2 = edge.getX() + edge.getWidth();
            int y2 = edge.getY() + (edge.getTopEqualsLeft() ? edge.getHeight() : 0);

            double dist = java.awt.geom.Line2D.ptLineDistSq((double) x1, (double) y1,
                    (double) x2, (double) y2,
                    (double) x, (double) y);

            if (dist < 5) return edge;
        }
        return null;
    }

    private static boolean validPlacementForVertex(int x, int y) {
        Edge edge = clickedOnEdge(x, y);
        if (edge != null) return false;

        for (Vertex vertex: Vertex.vertices.values()) {
            int xLocation = vertex.getXLocation();
            int yLocation = vertex.getYLocation();

            if (x >= xLocation - Vertex.SIZE / 2 && x <= xLocation + Vertex.SIZE + Vertex.SIZE / 2
                    && y >= yLocation - Vertex.SIZE / 2 && y <= yLocation + Vertex.SIZE + Vertex.SIZE / 2) {
                return false;
            }
        }
        return true;
    }

    private static boolean validVertexID(String userInput) {
        for (String id: Vertex.vertices.keySet()) {
            if (userInput.equals(id)) return false;
        }
        return true;
    }

    private void createVertex(int xValue, int yValue, String id) {
        JPanel vertex = new Vertex(xValue, yValue, id);
        this.add(vertex);
        vertex.repaint();
    }

    private void drawEdge(Vertex vertex1, Vertex vertex2) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter Weight:",
                    "Input", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                List<String> lastCouple = availableEdges.get(availableEdges.size() - 1);
                if (lastCouple.contains(vertex1.getId()) && lastCouple.contains(vertex2.getId())) {
                    availableEdges.remove(availableEdges.size() - 1);
                }
                return;
            } else {
                if (input.matches("(-?[1-9]\\d*|0)")) {
                    int weight = Integer.parseInt(input);
                    if (weight < 0) {
                        JOptionPane.showMessageDialog(this,
                            "Weight must be non-negative.",
                            "Invalid Weight",
                            JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    Edge edge1 = new Edge(vertex1, vertex2, weight);
                    Edge edge2 = new Edge(vertex2, vertex1, weight);

                    this.add(edge1);
                    this.add(edge2);
                    this.add(edge1.getLabel());

                    this.repaint();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Invalid weight. Please enter a valid integer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static void removeEdgeFromStaticList(Edge edge) {
        String id1 = edge.getVertex1().getId();
        String id2 = edge.getVertex2().getId();

        List<List<String>> newEdgesList = new ArrayList<>();

        for (List<String> otherEdge: availableEdges) {
            if ((!(otherEdge.contains(id1) && otherEdge.contains(id2)))) {
                newEdgesList.add(otherEdge);
            }
        }
        availableEdges = newEdgesList;
    }

    private static Map<Vertex, List<Edge>> createGraphDataStructure() {
        Map<Vertex, List<Edge>> output = new HashMap<>();

        for (Vertex vertex: Vertex.vertices.values()) {
            output.put(vertex, new ArrayList<>());
        }

        for (Edge edge: Edge.edges) {
            if (output.containsKey(edge.getVertex1())) {
                output.get(edge.getVertex1()).add(edge);
            }
        }
        return output;
    }
}