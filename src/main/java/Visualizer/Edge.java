package Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Edge extends JComponent implements Comparable<Edge> {
    private Vertex vertex1;
    private Vertex vertex2;
    private int weight;
    private JLabel label;
    private boolean topEqualsLeft;
    protected static List<Edge> edges = new ArrayList<>();

    public Edge(Vertex vertex1, Vertex vertex2, int weight) {
        this.setName("Edge <" + vertex1.getId() + " -> " + vertex2.getId() + ">");
        this.setBackground(MainFrame.BACKGROUND_COLOR);
        this.setLayout(null);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;

        this.topEqualsLeft = setEdgeBounds();
        setLabel();
        setupClickListener();

        edges.add(this);
    }

    private void setupClickListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (MainFrame.mode == Mode.EDIT_EDGE_WEIGHT) {
                    String input = JOptionPane.showInputDialog(
                        Edge.this.getParent(),
                        "Enter new weight for edge " + vertex1.getId() + " - " + vertex2.getId() + ":",
                        "Edit Edge Weight",
                        JOptionPane.QUESTION_MESSAGE
                    );
                    if (input != null && input.matches("(-?[1-9]\\d*|0)")) {
                        int newWeight = Integer.parseInt(input);
                        setWeight(newWeight);
                        Edge.this.getParent().repaint();
                    } else if (input != null) {
                        JOptionPane.showMessageDialog(
                            Edge.this.getParent(),
                            "Invalid weight. Please enter a valid integer.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }

    public void setWeight(int weight) {
        this.weight = weight;
        this.label.setText(String.valueOf(weight));
        this.label.setSize(label.getPreferredSize());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Vertex.VERTEX_COLOR);
        g2d.drawLine(0, topEqualsLeft ? 0 : this.getHeight(),
                this.getWidth(), topEqualsLeft ? this.getHeight() : 0);
    }

    private void setLabel() {
        this.label = new JLabel();
        this.label.setName("EdgeLabel <" + vertex1.getId() + " -> " + vertex2.getId() + ">");
        this.label.setText(String.valueOf(this.weight));
        this.label.setLocation(this.getX() + this.getWidth() / 2 - 10, this.getY() + this.getHeight() / 2 - 10);
        this.label.setSize(this.label.getPreferredSize());
        this.label.setForeground(new Color(255, 200, 50));
        this.label.setFont(new Font("Arial", Font.BOLD, 14));
        this.label.setBackground(new Color(0, 0, 0, 150));
        this.label.setOpaque(true);
    }

    public void updateBounds() {
        this.topEqualsLeft = setEdgeBounds();
        this.label.setLocation(this.getX() + this.getWidth() / 2 - 10, this.getY() + this.getHeight() / 2 - 10);
        repaint();
    }

    private boolean setEdgeBounds() {
        Vertex top;
        Vertex left;
        top = this.vertex1.getYLocation() < this.vertex2.getYLocation() ? vertex1 : vertex2;
        left = this.vertex1.getXLocation() < this.vertex2.getXLocation() ? vertex1 : vertex2;
        int extra = Vertex.SIZE / 2;

        int width = Math.abs(this.vertex1.getXLocation() - this.vertex2.getXLocation());
        int height = Math.abs(this.vertex1.getYLocation() - this.vertex2.getYLocation());

        this.setBounds(left.getXLocation() + extra - 2,
                top.getYLocation() + extra - 2,
                width + 4,
                height + 4);

        return top == left;
    }

    public JLabel getLabel() {
        return this.label;
    }

    public int getWeight() {
        return this.weight;
    }

    public Vertex getVertex1() {
        return this.vertex1;
    }

    public Vertex getVertex2() {
        return this.vertex2;
    }

    public boolean getTopEqualsLeft() {
        return this.topEqualsLeft;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Edge)) return false;
        Edge otherEdge = (Edge) other;

        return (this.vertex1.equals(otherEdge.vertex1) && this.vertex2.equals(otherEdge.vertex2)) ||
               (this.vertex1.equals(otherEdge.vertex2) && this.vertex2.equals(otherEdge.vertex1));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + vertex1.hashCode();
        result = 31 * result + vertex2.hashCode();
        return result;
    }

    @Override
    public int compareTo(Edge otherEdge) {
        return Integer.valueOf(this.weight).compareTo(otherEdge.getWeight());
    }
}