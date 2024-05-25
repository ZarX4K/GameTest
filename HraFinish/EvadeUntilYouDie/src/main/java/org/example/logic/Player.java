package org.example.logic;

import org.example.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Player {
    GameLogic gamePanel;
    KeyReader keyReader;
    public int x;
    public int y;
    private int lives;
    public int width = 64;
    public int height = 64;
    public int speed = 8;
    public Image image;
    public Image hitImage;

    private long lastHitTime;
    private final long hitDelay = 1000;

    public Player(GameLogic gamePanel, KeyReader keyReader, int x, int y, String url, String hitUrl) {
        this.gamePanel = gamePanel;
        this.keyReader = keyReader;
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(getClass().getResource("/" + url)).getImage();
        this.hitImage = new ImageIcon(getClass().getResource("/" + hitUrl)).getImage();
        this.lives = 99;
    }

    public void draw(Graphics g) {
        if (canBeHit()) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.drawImage(hitImage, x, y, width, height, null);
        }
    }

    public boolean canBeHit() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastHitTime) > hitDelay;
    }

    public void hit() {
        long currentTime = System.currentTimeMillis();
        if (canBeHit()) {
            lives--;
            lastHitTime = currentTime;
        }
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

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isCollided(Rectangle otherObject) {
        return getRectangle().intersects(otherObject);
    }
}
