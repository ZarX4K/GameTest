package org.example.logic;

import javax.swing.*;
import java.awt.*;

public class Wall {
    private boolean active;
    public Image image;
    public int x;
    public int   y;
    public int width = 64;
    public int height = 64;
   

    public Wall(int x, int y, String url) {
        this.x = x;
        this.y = y;
        ImageIcon ii = new ImageIcon(getClass().getResource("/" + url));
        this.image = ii.getImage();
    }



 

    public void inactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
    public void draw (Graphics g){
        g.drawImage(image, x, y, width, height, null);
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }
    

    public Rectangle getRectangle(){
        return new Rectangle(x,y,width, height);
    }

    public Image getImage() {
        return image;
    }
}
