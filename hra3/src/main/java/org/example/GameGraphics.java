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
    BufferedImage bi;
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
        setIconImage(new ImageIcon("src/main/resources/gameLogo.png").getImage());
        add(draw);
        try {
            bi= ImageIO.read(new File("src/main/resources/backGrGrass.png"));
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
            g.drawImage(bi,0 ,0,getWidth(),getHeight(),null);
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
                 else if (logic.getPlayer().getLives() > 3) {
                    g.drawImage(logic.getHeartz2().getImage(), logic.getHeartz2().getX(), logic.getHeartz2().getY(), null);
                }
                else if (logic.getPlayer().getLives() > 0) {
                    g.drawImage(logic.getHeartz3().getImage(), logic.getHeartz3().getX(), logic.getHeartz3().getY(), null);
                }
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
