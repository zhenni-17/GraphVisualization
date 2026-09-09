package Visualizer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    protected static final int WIDTH = 800;
    protected static final int HEIGHT = 600;
    protected static final Color BACKGROUND_COLOR = Color.black;
    private JLabel modeLabel;
    private static final JLabel algorithmDisplayLabel;
    private Graph graphPanel;
    protected static Mode mode = Mode.ADD_A_VERTEX;
    protected static Algorithm algorithm = null;

    static {
        algorithmDisplayLabel = new JLabel();
        algorithmDisplayLabel.setName("Display");
        algorithmDisplayLabel.setText("Please choose a starting vertex");
        algorithmDisplayLabel.setForeground(Color.white);
        algorithmDisplayLabel.setBackground(new Color(50, 50, 50));
        algorithmDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        algorithmDisplayLabel.setVerticalAlignment(SwingConstants.CENTER);
        algorithmDisplayLabel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        algorithmDisplayLabel.setVisible(false);
        algorithmDisplayLabel.setOpaque(true);
        algorithmDisplayLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        algorithmDisplayLabel.setPreferredSize(new Dimension(WIDTH, 40));
    }

    public MainFrame() {
        super("Graph-Algorithms Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
        setLayout(new BorderLayout());
        setResizable(false);

        setModeJLabel();
        this.add(algorithmDisplayLabel, BorderLayout.SOUTH);
        setJMenu();

        add(this.graphPanel = new Graph(), BorderLayout.CENTER);

        setVisible(true);
    }

    private void setJMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newReset = new JMenuItem("New");
        newReset.setName("New");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("Exit");

        fileMenu.add(newReset);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu modeMenu = new JMenu("Mode");
        menuBar.add(modeMenu);

        JMenuItem addAVertex = new JMenuItem("Add a Vertex");
        JMenuItem addAnEdge = new JMenuItem("Add an Edge");
        JMenuItem editEdgeWeight = new JMenuItem("Edit Edge Weight");
        JMenuItem removeAVertex = new JMenuItem("Remove a Vertex");
        JMenuItem removeAnEdge = new JMenuItem("Remove an Edge");
        JMenuItem none = new JMenuItem("None");

        modeMenu.add(addAVertex);
        modeMenu.add(addAnEdge);
        modeMenu.add(editEdgeWeight);
        modeMenu.add(removeAVertex);
        modeMenu.add(removeAnEdge);
        modeMenu.addSeparator();
        modeMenu.add(none);

        JMenu algorithmsMenu = new JMenu("Algorithms");
        menuBar.add(algorithmsMenu);

        JMenuItem DFS = new JMenuItem("Depth-First Search");
        JMenuItem BFS = new JMenuItem("Breadth-First Search");
        JMenuItem Dijkstras = new JMenuItem("Dijkstra's Algorithm");
        JMenuItem Prims = new JMenuItem("Prim's Algorithm");
        JMenuItem TopologicalSort = new JMenuItem("Topological Sort");
        JMenuItem ConnectedComponents = new JMenuItem("Connected Components");

        algorithmsMenu.add(DFS);
        algorithmsMenu.add(BFS);
        algorithmsMenu.add(Dijkstras);
        algorithmsMenu.add(Prims);
        algorithmsMenu.addSeparator();
        algorithmsMenu.add(TopologicalSort);
        algorithmsMenu.add(ConnectedComponents);

        addAVertex.addActionListener(e -> {
            mode = Mode.ADD_A_VERTEX;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            Graph.edgeVertices.clear();
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });
        
        addAnEdge.addActionListener(e -> {
            mode = Mode.ADD_AN_EDGE;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });
        
        editEdgeWeight.addActionListener(e -> {
            mode = Mode.EDIT_EDGE_WEIGHT;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            Graph.edgeVertices.clear();
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });
        
        none.addActionListener(e -> {
            mode = Mode.NONE;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            Graph.edgeVertices.clear();
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });
        
        removeAVertex.addActionListener(e -> {
            mode = Mode.REMOVE_A_VERTEX;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            Graph.edgeVertices.clear();
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });
        
        removeAnEdge.addActionListener(e -> {
            mode = Mode.REMOVE_AN_EDGE;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            Graph.edgeVertices.clear();
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });

        newReset.addActionListener(e -> {
            this.remove(this.graphPanel);
            this.repaint();
            this.revalidate();
            this.add(this.graphPanel = new Graph());

            Edge.edges.clear();
            Vertex.vertices.clear();
            Graph.edgeVertices.clear();
            Graph.availableEdges.clear();

            mode = Mode.ADD_A_VERTEX;
            changeTextForModeLabel("Current Mode -> " + mode.getDescription());
            algorithmDisplayLabel.setVisible(false);
            algorithmDisplayLabel.setText("Please choose a starting vertex");
        });
        
        exit.addActionListener(e -> this.dispose());

        DFS.addActionListener(e -> {
            none.doClick();
            algorithmDisplayLabel.setVisible(true);
            algorithm = Algorithm.DFS;
        });
        BFS.addActionListener(e -> {
            none.doClick();
            algorithmDisplayLabel.setVisible(true);
            algorithm = Algorithm.BFS;
        });
        Dijkstras.addActionListener(e -> {
            none.doClick();
            algorithmDisplayLabel.setVisible(true);
            algorithm = Algorithm.DIJKSTRAS;
        });
        Prims.addActionListener(e -> {
            none.doClick();
            algorithmDisplayLabel.setVisible(true);
            algorithm = Algorithm.PRIMS;
        });
        TopologicalSort.addActionListener(e -> {
            none.doClick();
            algorithmDisplayLabel.setVisible(true);
            algorithm = Algorithm.TOPOLOGICAL_SORT;
            algorithmDisplayLabel.setText("Click a vertex to start Topological Sort");
        });
        ConnectedComponents.addActionListener(e -> {
            none.doClick();
            algorithmDisplayLabel.setVisible(true);
            algorithm = Algorithm.CONNECTED_COMPONENTS;
            algorithmDisplayLabel.setText("Click a vertex to find Connected Components");
        });
    }

    private void changeTextForModeLabel(String newText) {
        this.modeLabel.setText(newText);
        modeLabel.setSize(modeLabel.getPreferredSize());
        this.modeLabel.setLocation(MainFrame.WIDTH - modeLabel.getWidth() - 20, 0);
    }

    private void setModeJLabel() {
        modeLabel = new JLabel();
        this.add(modeLabel, BorderLayout.NORTH);
        modeLabel.setName("Mode");
        modeLabel.setText("Current Mode -> Add a Vertex");
        modeLabel.setOpaque(true);
        modeLabel.setForeground(Vertex.VERTEX_COLOR);
        modeLabel.setBackground(MainFrame.BACKGROUND_COLOR);
        modeLabel.setLayout(new FlowLayout(FlowLayout.CENTER));
        modeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        modeLabel.setPreferredSize(new Dimension(WIDTH, 30));
    }

    public static JLabel getAlgorithmDisplayLabel() {
        return algorithmDisplayLabel;
    }
}