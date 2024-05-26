package org.example.logic;

import java.awt.*;

public class Bullet extends Entity {
    private int velocityX;
    private int velocityY;


    public Bullet(int x, int y, int velocityX, int velocityY, String url) {
        super(x, y, url);
        this.velocityX = velocityX;
        this.velocityY = velocityY;

    }

    public void move() {
        coord.x += velocityX;
        coord.y += velocityY;
    }

    public Rectangle getRectangle(){
        return new Rectangle(coord.x,coord.y,width, height);
    }

}