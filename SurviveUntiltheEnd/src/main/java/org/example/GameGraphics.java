package org.example;

import org.example.logic.Bullet;
import org.example.logic.Wall;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameGraphics extends JFrame {
    private GameLogic gameLogic;
    private BackGround backGround;
    private EndGamePic endGamePic;
    private StartGamePic startGamePic;
    private Game.KeyReader keyReader;

    public GameGraphics(Game.KeyReader keyReader) {
        this.keyReader = keyReader;
        setTitle("Survive Until The End");
        setSize(1080, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(new ImageIcon("src/main/resources/gameLogo.png").getImage());

        gameLogic = new GameLogic(keyReader);
        backGround = new BackGround(gameLogic);
        endGamePic = new EndGamePic(gameLogic);
        startGamePic = new StartGamePic(gameLogic);

        GamePanel gamePanel = new GamePanel(gameLogic, backGround, endGamePic, startGamePic);
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
        private final BackGround backGround;
        private final EndGamePic endGamePic;
        private final StartGamePic startGamePic;

        public GamePanel(GameLogic gameLogic, BackGround backGround, EndGamePic endGamePic, StartGamePic startGamePic) {
            this.gameLogic = gameLogic;
            this.backGround = backGround;
            this.endGamePic = endGamePic;
            this.startGamePic = startGamePic;
            setPreferredSize(new Dimension(gameLogic.width, gameLogic.height));
            setBackground(Color.black);
            setDoubleBuffered(true);

            setFocusable(true);
            addKeyListener(keyReader);
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
                backGround.draw(g);
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
                endGamePic.draw(g);
            } else {
                startGamePic.draw(g);
            }
        }

    }

    private class BackGround {
        private BufferedImage firstLevelImage;
        private BufferedImage secondLevelImage;
        private BufferedImage thirdLevelImage;
        private GameLogic gameLogic;

        public BackGround(GameLogic gameLogic) {
            this.gameLogic = gameLogic;
            getImage();
        }



        public void getImage() {
            try {
                firstLevelImage = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass.png"));
                secondLevelImage = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass2.png"));
                thirdLevelImage = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass3.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void draw(Graphics g) {
            int secondsPassed = gameLogic.getSecondsPassed();

            if (secondsPassed < 60) {
                g.drawImage(firstLevelImage, 0, 0, getWidth(), getHeight(), null);
            } else if (secondsPassed < 120) {
                g.drawImage(secondLevelImage, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.drawImage(thirdLevelImage, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    private class EndGamePic {
        private BufferedImage endImage1;
        private BufferedImage endImage2;

        private int startCount;
        private final int switchRate = 60;
        private GameLogic gameLogic;

        public EndGamePic(GameLogic gameLogic) {
            this.gameLogic = gameLogic;
            getImage();
        }

        public void getImage() {
            try {
                endImage1 = ImageIO.read(getClass().getResourceAsStream("/GameEnd1.png"));
                endImage2 = ImageIO.read(getClass().getResourceAsStream("/GameEnd2.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void draw(Graphics g) {
            startCount++;
            if (startCount >= switchRate * 2) {
                startCount = 0;
            }
            if (startCount < switchRate) {
                g.drawImage(endImage1, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.drawImage(endImage2, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    private class StartGamePic {
        private BufferedImage startImage1;
        private BufferedImage startImage2;
        private int startCount;
        private final int switchRate = 60;
        private GameLogic gameLogic;

        public StartGamePic(GameLogic gameLogic) {
            this.gameLogic = gameLogic;
            getImage();
        }

        public void getImage() {
            try {
                startImage1 = ImageIO.read(getClass().getResourceAsStream("/background1.png"));
                startImage2 = ImageIO.read(getClass().getResourceAsStream("/background2.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void draw(Graphics g) {
            startCount++;
            if (startCount >= switchRate * 2) {
                startCount = 0;
            }
            if (startCount < switchRate) {
                g.drawImage(startImage1, 0, 0, getWidth(),getHeight() , null);
            } else {
                g.drawImage(startImage2, 0, 0, getWidth(), getHeight(), null);
            }
        }

    }

}

