package org.example.logic;

public class Enemy extends Entity {
    private int damage;

    public Enemy(int x, int y, String url, int damage) {
        super(x, y, url);
        this.damage = damage;
    }
}
