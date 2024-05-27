package org.example;

import org.example.logic.Bullet;
import org.example.logic.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
    public class GameGraphics extends JFrame {
        private GameLogic gameLogic;

        public GameGraphics() {
            setTitle("Survive Until The End");
            setSize(1080, 720);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            setIconImage(new ImageIcon("src/main/resources/gameLogo.png").getImage());

            gameLogic = new GameLogic();

            GamePanel gamePanel = new GamePanel(gameLogic);
            add(gamePanel);
            gameLogic.setGamePanel(gamePanel);

            gameLogic.initialize();
            gameLogic.startGameThread();

            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private class GamePanel extends JPanel {
            private final GameLogic gameLogic;

            public GamePanel(GameLogic gameLogic) {
                this.gameLogic = gameLogic;
                setPreferredSize(new Dimension(gameLogic.width, gameLogic.height));
                setBackground(Color.black);
                setDoubleBuffered(true);

                // Add key and mouse listeners here
                setFocusable(true);
                addKeyListener(gameLogic.getKeyReader());
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (gameLogic.getGameState() == 1 && !gameLogic.isGameStarted()) {
                            gameLogic.startGame();
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gameLogic.getGameState() == 2) {
                    gameLogic.getBackGround().draw(g);
                    gameLogic.getPlayer().draw(g);
                    for (Wall wall : gameLogic.getWalls()) {
                        wall.draw(g);
                    }
                    for (Bullet bullet : gameLogic.getBullets()) {
                        g.drawImage(bullet.getImage(), bullet.getCoord().x, bullet.getCoord().y, null);
                    }
                    g.setFont(gameLogic.getCustomFont());
                    g.setColor(Color.WHITE);
                    g.drawString("Time Alive: " + gameLogic.getSecondsPassed(), 960, 100);
                    g.drawString("Health: " + gameLogic.getPlayer().getLives(), 970, 80);
                    if (gameLogic.getPlayer().getLives() > 6) {
                        g.drawImage(gameLogic.getHeartz().getImage(), gameLogic.getHeartz().getX(), gameLogic.getHeartz().getY(), null);
                    } else if (gameLogic.getPlayer().getLives() > 3) {
                        g.drawImage(gameLogic.getHeartzHalf().getImage(), gameLogic.getHeartzHalf().getX(), gameLogic.getHeartzHalf().getY(), null);
                    } else if (gameLogic.getPlayer().getLives() > 0) {
                        g.drawImage(gameLogic.getHeartzLast().getImage(), gameLogic.getHeartzLast().getX(), gameLogic.getHeartzLast().getY(), null);
                    }
                    if (gameLogic.getSecondsPassed() > 119) {
                        gameLogic.setDarknessImage(new ImageIcon(getClass().getResource("/Darkness.png")).getImage());
                        int darknessWidth = gameLogic.getDarknessImage().getWidth(null);
                        int darknessHeight = gameLogic.getDarknessImage().getHeight(null);
                        int darknessX = gameLogic.getPlayer().getCoord().x + gameLogic.getPlayer().getWidth() / 2 - darknessWidth / 2;
                        int darknessY = gameLogic.getPlayer().getCoord().y + gameLogic.getPlayer().getHeight() / 2 - darknessHeight / 2;
                        g.drawImage(gameLogic.getDarknessImage(), darknessX, darknessY, this);
                    }
                } else if (gameLogic.getGameState() == 3) {
                    gameLogic.getEndGamePic().draw(g);
                } else {
                    gameLogic.getStartGamePic().draw(g);
                }
            }
        }
    }