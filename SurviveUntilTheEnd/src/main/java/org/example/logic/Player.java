package org.example.logic;

import org.example.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Player extends Entity {
    private final GameLogic gameLogic;
    private final KeyReader keyReader;
    private int lives;
    public int width = 64;
    public int height = 64;
    public int speed = 8;
    public Image hitImage;
    private long lastHitTime;
    private final long hitDelay = 1000;

    public Player(GameLogic gameLogic, KeyReader keyReader, int x, int y, String url, String hitUrl) {
        super(x, y, url);
        this.gameLogic = gameLogic;
        this.keyReader = keyReader;
        this.hitImage = new ImageIcon(getClass().getResource("/" + hitUrl)).getImage();
        this.lives = 10;
    }

    public void draw(Graphics g) {
        if (canBeHit()) {
            g.drawImage(image, coord.x, coord.y, width, height, null);
        } else {
            g.drawImage(hitImage, coord.x, coord.y, width, height, null);
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
        return new Rectangle(coord.x, coord.y, width, height);
    }

    public boolean isCollided(Rectangle otherObject) {
        return getRectangle().intersects(otherObject);
    }
}
