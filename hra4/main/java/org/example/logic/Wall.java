package org.example.logic;

import javax.swing.*;
import java.awt.*;

public class Wall {
    private Coordinates coordStart;
    private Coordinates coordEnd;
    private Color color;
    private boolean active;
    public Image image;
    private int width;
    private int height;

    public Wall(int x1, int y1, int x2, int y2, String url) {
        try {
            ImageIcon ii = new ImageIcon(getClass().getResource("/" + url));
            this.image = ii.getImage();
            System.out.println("Image loaded successfully: " + url);
        } catch (Exception e) {
            System.out.println("Failed to load image: " + url);
            e.printStackTrace();
        }
        this.coordStart = new Coordinates(x1, y1);
        this.coordEnd = new Coordinates(x2, y2);
        this.width = Math.abs(x2 - x1);
        this.height = Math.abs(y2 - y1);
        this.active = true;
    }

    public void draw(Graphics g) {
        if (image != null && active) {
            g.drawImage(image, coordStart.x, coordStart.y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.drawRect(coordStart.x, coordStart.y, width, height);
            g.drawString("Image not available", coordStart.x, coordStart.y + 15);
        }
    }

    public Coordinates getCoordStart() {
        return coordStart;
    }

    public Coordinates getCoordEnd() {
        return coordEnd;
    }

    public void inactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public Color getColor() {
        return color;
    }

    public Rectangle getRectangle() {
        return new Rectangle(coordStart.x, coordStart.y, width, height);
    }
}
