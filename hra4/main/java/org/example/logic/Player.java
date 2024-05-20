package org.example.logic;

import org.example.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Player { //extends Entity {
    GameLogic gamePanel;
    KeyReader keyReader;
    public int x;
    public int   y;
    public boolean dead = false;
    public int timeToAlive = 2000;
    private int lives;
    public int width = 64;
    public int height = 64;
    public int speed = 6;
    public Image image;

    public Player(GameLogic  gamePanel, KeyReader keyReader, int x, int y, String url){
        this.gamePanel = gamePanel;
        this.keyReader = keyReader;
        this.x = x;
        this.y = y;
        ImageIcon ii = new ImageIcon(getClass().getResource("/" + url));
        this.image = ii.getImage();
        this.lives = 10;
    }



    public void draw (Graphics g){
        g.drawImage(image, x, y, width, height, null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public int getSpeed() {
        return speed;
    }



    public int getLives() {
        return lives;
    }



    public void hit() {
        lives--;

    }

    public Rectangle getRectangle(){
        return new Rectangle(x,y,width, height);
    }

    public boolean isCollided (Rectangle otherObject) {
        return getRectangle().intersects(otherObject);
    }

}
