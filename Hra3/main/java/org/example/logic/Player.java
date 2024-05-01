package org.example.logic;

import org.example.GameGraphics;
import org.example.GameLogic;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {
    public boolean dead = false;
    public int timeToAlive = 2000;
   ;
    private int lives;
    KeyReader keyReader;
    public int speed = 6;
    GameLogic logic;


    public Player(int x, int y, String url, KeyReader keyReader) {
        super(x, y, url);
        this.keyReader = keyReader;
        this.lives = 10;

    }

        public void update() {
                if (keyReader.upPressed == true) {
                    coord.y -= speed;
                }
                if (keyReader.downPressed == true) {
                    coord.y += speed;
                }
                if (keyReader.leftPressed == true) {
                    coord.x -= speed;
                }
                if (keyReader.rightPressed == true) {
                    coord.x += speed;
                }
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
