package org.example.logic;

import org.example.GameLogic;

import java.awt.*;

public class Ball extends Entity {
    public boolean dead = false;
    public int timeToAlive = 2000;
   ;
    private int lives;


    public Ball(int x, int y, String url) {
        super(x, y, url);
        this.lives = 10;

    }

    public int getX() {
        return coord.x;
    }

    public void setX(int x) {
        this.coord.x = x;
    }

    public int getY() {
        return coord.y;
    }

    public void setY(int y) {
        this.coord.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public boolean isCollided(Rectangle otherObject) {
        return getRectangle().intersects(otherObject);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;

    }

    public void hit() {
        lives--;

    }

}
