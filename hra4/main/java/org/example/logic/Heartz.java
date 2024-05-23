package org.example.logic;

import java.awt.*;

public class Heartz extends Entity{
    public Heartz(int x, int y, String url) {
        super(x, y, url);
    }
    public int getX() {
        return coord.x;
    }


    public int getY() {
        return coord.y;
    }

    public Image getImage() {
        return image;
    }

}
