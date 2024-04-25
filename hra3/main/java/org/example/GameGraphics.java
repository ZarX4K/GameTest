package org.example;

import org.example.logic.Enemy;
import org.example.logic.Rocket;
import org.example.logic.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class GameGraphics extends JFrame {
    Draw draw;
    GameLogic logic;
    private final Font customFont = new Font("Arial", Font.BOLD, 16);

    public GameGraphics(GameLogic logic) throws HeadlessException {

        this.draw = new Draw();
        this.logic = logic;

        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Survive Until The End");
        //setIconImage();

        add(draw);
    }

    public void render(GameLogic logic) {
        this.logic = logic;
        repaint();
    }

    public class Draw extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(logic.getBall().getImage(), logic.getBall().getX(), logic.getBall().getY(), new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
            g.setFont(customFont);
            g.drawString("Health: " + logic.getBall().getLives(), 970, 80);
            {
                if (logic.getBall().getLives() > 6) {
                    g.drawImage(logic.getHeartz().getImage(), logic.getHeartz().getX(), logic.getHeartz().getY(), null);
                }
                // else if (logic.getHeartz().getLives())
            }
            for (Wall wall : logic.getWalls()) {
                if (wall.isActive()) {
                    g.setColor(wall.getColor());
                    g.drawLine(wall.getCoordStart().x, wall.getCoordStart().y, wall.getCoordEnd().x, wall.getCoordEnd().y);
                }
            }
            for (Enemy enemy : logic.getEnemy())
                g.drawImage(enemy.getImage(), enemy.getCoord().x, enemy.getCoord().y, new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
            for (Rocket rocket : logic.getRockets()) {
                g.drawImage(rocket.getImage(), rocket.getCoord().x, rocket.getCoord().y, this);
            }
        }
    }
}
