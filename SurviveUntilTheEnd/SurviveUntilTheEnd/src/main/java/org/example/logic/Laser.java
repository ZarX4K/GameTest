package org.example.logic;

import javax.swing.*;
import java.awt.*;

public class Laser extends Entity {
    private boolean active;
    private long activationTime;
    private Point target;
    private int speed = 5;
    private String direction;

    public Laser(int x, int y, String url, int width, int height) {
        super(x, y, url);
        this.image = new ImageIcon(getClass().getResource("/" + url)).getImage();
        this.active = false;
        this.width = width;
        this.height = height;
    }

    public void activate(Point target, String direction) {
        this.active = true;
        this.target = target;
        this.direction = direction;
        this.activationTime = System.currentTimeMillis();
    }

    public void move() {
        if (active) {
            switch (direction) {
                case "Zhora":
                    coord.y += speed;
                    if (coord.y >= target.y) {
                        coord.y = target.y;
                        deactivateAfterDelay();
                    }
                    break;
                case "Zleva":
                    coord.x += speed;
                    if (coord.x >= target.x) {
                        coord.x = target.x;
                        deactivateAfterDelay();
                    }
                    break;
                case "Zdola":
                    coord.y -= speed;
                    if (coord.y <= target.y) {
                        coord.y = target.y;
                        deactivateAfterDelay();
                    }
                    break;
                case "Zprava":
                    coord.x -= speed;
                    if (coord.x <= target.x) {
                        coord.x = target.x;
                        deactivateAfterDelay();
                    }
                    break;
            }
        }
    }

    private void deactivateAfterDelay() {
        Timer timer = new Timer(5000, e -> deactivate());
        timer.setRepeats(false);
        timer.start();
    }

    public Rectangle getRectangle() {
        return new Rectangle(coord.x, coord.y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
