package org.example.logic;

import java.awt.*;

public class Wall {
    private Coordinates coordStart;
    private Coordinates coordEnd;
    private Color color;
    private boolean active;

    public Wall(int x1, int y1, int x2, int y2, Color color) {
        this.color = color;
        this.coordStart = new Coordinates(x1, y1);
        this.coordEnd = new Coordinates(x2, y2);
        this.active = true;
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

    public Rectangle getRectangle () {
        return new Rectangle(coordStart.x, coordStart.y, coordEnd.x - coordStart.x+1, coordEnd.y - coordStart.y+1);
    }
}
