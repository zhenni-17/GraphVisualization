package Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Vertex extends JPanel implements Comparable<Vertex> {

    protected static final int SIZE = 50;
    protected static final Color VERTEX_COLOR = Color.white;
    protected static final Color VISITED_COLOR = new Color(100, 255, 100);
    protected static final Color CURRENT_COLOR = new Color(255, 200, 50);
    protected static final Map<String, Vertex> vertices = new HashMap<>();
    private String id;
    private JLabel label;
    private int xLocation;
    private int yLocation;
    private Color currentColor = VERTEX_COLOR;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;

    public Vertex(int x, int y, String id) {
        this.id = id;
        this.xLocation = x;
        this.yLocation = y;
        vertices.put(this.id, this);
        this.label = new JLabel();
        this.label.setName("VertexLabel " + this.id);
        this.setLabel();

        this.setName("Vertex " + this.id);
        this.setBackground(MainFrame.BACKGROUND_COLOR);
        this.setOpaque(false);
        this.setLayout(null);
        this.setBounds(x, y, Vertex.SIZE, Vertex.SIZE);
        
        setupDragListener();
    }

    private void setupDragListener() {
        MouseAdapter dragAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (MainFrame.mode == Mode.NONE) {
                    isDragging = true;
                    dragOffsetX = e.getX();
                    dragOffsetY = e.getY();
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && MainFrame.mode == Mode.NONE) {
                    int newX = getX() + e.getX() - dragOffsetX;
                    int newY = getY() + e.getY() - dragOffsetY;
                    
                    newX = Math.max(0, Math.min(newX, getParent().getWidth() - SIZE));
                    newY = Math.max(0, Math.min(newY, getParent().getHeight() - SIZE));
                    
                    setLocation(newX, newY);
                    xLocation = newX;
                    yLocation = newY;
                    
                    for (Edge edge : Edge.edges) {
                        if (edge.getVertex1().equals(Vertex.this) || edge.getVertex2().equals(Vertex.this)) {
                            edge.updateBounds();
                        }
                    }
                    getParent().repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    isDragging = false;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        
        addMouseListener(dragAdapter);
        addMouseMotionListener(dragAdapter);
    }

    public void setColor(Color color) {
        this.currentColor = color;
        repaint();
    }

    public void resetColor() {
        this.currentColor = VERTEX_COLOR;
        repaint();
    }

    public int getXLocation() {
        return this.xLocation;
    }

    public int getYLocation() {
        return this.yLocation;
    }

    public String getId() {
        return this.id;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(currentColor);
        g.fillOval(0, 0, Vertex.SIZE, Vertex.SIZE);
        g.setColor(Color.black);
        g.drawOval(0, 0, Vertex.SIZE - 1, Vertex.SIZE - 1);
    }

    private void setLabel() {
        this.label.setText(this.id);
        this.label.setVerticalAlignment(JLabel.CENTER);
        this.label.setHorizontalAlignment(JLabel.CENTER);
        this.label.setSize(Vertex.SIZE, Vertex.SIZE);
        this.label.setForeground(Color.black);
        this.label.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(this.label);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vertex)) return false;
        Vertex otherVertex = (Vertex) other;
        return Objects.equals(this.id, (otherVertex).getId());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public int compareTo(Vertex otherVertex) {
        return String.valueOf(this.id).compareTo(otherVertex.getId());
    }
}