package org.example;

import org.example.logic.Enemy;
import org.example.logic.Rocket;
import org.example.logic.Wall;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GameGraphics extends JFrame {

    Draw draw;
    GameLogic logic;
    private final Font customFont = new Font("Arial", Font.BOLD, 16);

    public GameGraphics(GameLogic logic) throws HeadlessException {
        BufferedImage bi;
        this.draw = new Draw();
        this.logic = logic;

        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Survive Until The End");
        setIconImage(new ImageIcon("src/main/resources/gameLogo.png").getImage());
        add(draw);
        try {
            bi= ImageIO.read(new File("src/main/resources/BackGrass.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void render(GameLogic logic) {
        this.logic = logic;
        repaint();
    }

    public class Draw extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(logic.getPlayer().getImage(), logic.getPlayer().getX(), logic.getPlayer().getY(), new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });

            g.setFont(customFont);
            g.drawString("Health: " + logic.getPlayer().getLives(), 970, 80);
            {
                if (logic.getPlayer().getLives() > 6) {
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
