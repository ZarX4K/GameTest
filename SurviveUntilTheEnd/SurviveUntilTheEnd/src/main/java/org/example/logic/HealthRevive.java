package org.example.logic;

import javax.swing.*;
import java.awt.*;

public class HealthRevive extends Entity{
    public int width = 54;
    public int height = 54  ;
    private boolean active;
    private long activationTime;


    public HealthRevive(int x, int y, String url) {
        super(x, y, url);
        this.image = new ImageIcon(getClass().getResource("/" + url)).getImage();
        this.active = false;


    }
    public void activate(int currentTime) {
        if (currentTime >= activationTime) {
            this.active = true;
        }
    }
    public void setActivationTime(long activationTime) {
        this.activationTime = activationTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getRectangle() {
        return new Rectangle(coord.x, coord.y, width, height);
    }


    public boolean isActive() {
        return active;
    }

}
