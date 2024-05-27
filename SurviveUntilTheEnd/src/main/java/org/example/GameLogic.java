package org.example;

import org.example.logic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameLogic implements Runnable {
    private final ArrayList<Wall> walls;
    private final ArrayList<Bullet> bullets;
    private Heartz heartz;
    private Heartz heartzHalf;
    private Heartz heartzLast;
    private BackGround backGround;
    private StartGamePic startGamePic;
    private EndGamePic endGamePic;
    private KeyReader keyReader;
    private final Font customFont = new Font("Arial", Font.BOLD, 16);
    private int gameState = 1;
    private boolean gameStarted = false;
    private Thread gameThread;
    private long currentTime;
    private double delta = 0;
    private int fps = 60;
    private long lastTime = System.nanoTime();
    private double drawInterval = 1000000000 / fps;
    public int width = 1080, height = 720;
    private int secondsPassed;
    private int startCount;
    private int spawnRate = 27;
    private Timer timer;
    private Player player;
    private Image DarknessImage;
    private JPanel gamePanel;

    public GameLogic() {
        this.walls = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }

    public void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initialize() {
        keyReader = new KeyReader();
        player = new Player(this, keyReader, 200, 200, "Player.gif", "PlayerHit.gif");
        backGround = new BackGround(this);
        backGround.getImage();
        startGamePic = new StartGamePic(this);
        startGamePic.getImage();
        endGamePic = new EndGamePic(this);
        endGamePic.getImage();
        createMaze();
        wallActivation();
        heartz = new Heartz(980, 1, "Heartz.gif");
        heartzHalf = new Heartz(980, 1, "Heartz2.gif");
        heartzLast = new Heartz(980, 1, "Heartz3.gif");

        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(keyReader);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    startTimer();
                    gameState = 2;
                }
            }
        });
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && gameState == 3) {
                    resetGame();
                }
            }
        });
    }




    private void createMaze() {
        walls.add(new Wall(135, 51, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(600, 90, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(673, 523, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(843, 10, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(80, 423, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(984, 586, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(380, 236, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(864, 321, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(300, 521, "Rock.gif", "RockFlash.gif"));
    }

    private void wallActivation() {
        int activationTime = 60;
        for (Wall wall : walls) {
            wall.setActivationTime(activationTime);
            activationTime += 1;
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                for (Wall wall : walls) {
                    wall.activate(secondsPassed);
                }
            }
        });
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }

    public void startGame() {
        gameStarted = true;
        gameState = 2;
        startTimer();
    }

    public void update() {
        if (!gameStarted) {
            gamePanel.repaint();//ikdyz hra nezacala prekrelsi aby se nacetl startObrazek
            return;
        }

        ArrayList<Bullet> rocketsToRemove = new ArrayList<>();
        controlledMove();
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.move();
            if (player.isCollided(bullet.getRectangle()) && player.canBeHit()) {
                player.hit();
                System.out.println("Collision");
                bulletIterator.remove();
            }
        }
        spawnInterval();
        if (player.getLives() <= 0) {
            gameState = 3;
        }
        gamePanel.repaint();
    }

    private void controlledMove() {
        int newX = player.getCoord().x;
        int newY = player.getCoord().y;

        if (keyReader.upPressed) newY -= player.getSpeed();
        if (keyReader.downPressed) newY += player.getSpeed();
        if (keyReader.leftPressed) newX -= player.getSpeed();
        if (keyReader.rightPressed) newX += player.getSpeed();
        if (keyReader.unstuckPressed) {
            newX = 500;
            newY = 350;
        }

        if (keyReader.rightPressed && keyReader.upPressed) {
            newX += player.getSpeed() - 10;
            newY -= player.getSpeed() - 10;
        }
        if (keyReader.leftPressed && keyReader.upPressed) {
            newX -= player.getSpeed() - 10;
            newY -= player.getSpeed() - 10;
        }
        if (keyReader.leftPressed && keyReader.downPressed) {
            newX -= player.getSpeed() - 10;
            newY += player.getSpeed() - 10;
        }
        if (keyReader.rightPressed && keyReader.downPressed) {
            newX += player.getSpeed() - 10;
            newY += player.getSpeed() - 10;
        }

        if (!checkCollisions(newX, player.getCoord().y)) {
            newX = Math.max(0, Math.min(newX, width - player.getWidth()));
            player.setCoord(new Coordinates(newX, player.getCoord().y));
        }
        if (!checkCollisions(player.getCoord().x, newY)) {
            newY = Math.max(0, Math.min(newY, height - player.getHeight()));
            player.setCoord(new Coordinates(player.getCoord().x, newY));
        }
    }

    private boolean checkCollisions(int x, int y) {
        Rectangle playerRect = new Rectangle(x, y, player.getWidth(), player.getHeight());
        for (Wall wall : walls) {
            if (!wall.isActive()) {
                continue;
            }
            Rectangle wallRect = wall.getRectangle();
            if (playerRect.intersects(wallRect)) {
                return true;
            }
        }
        return false;
    }

    private void spawnRocket() {
        Random rando = new Random();
        int randPick = rando.nextInt(4) + 1;
        Random random = new Random();
        for (int i = 0; i < randPick; i++) {
            int randCorner = random.nextInt(4);

            Point point = null;
            switch (randCorner) {
                case 0:
                    point = pointToEnemy(player.getCoord().x, player.getCoord().y, 0, 0, 6);
                    bullets.add(new Bullet(0, 0, point.x, point.y, "Bullet.gif"));
                    break;
                case 1:
                    point = pointToEnemy(player.getCoord().x, player.getCoord().y, 1080, 0, 6);
                    bullets.add(new Bullet(1080, 0, point.x, point.y, "Bullet.gif"));
                    break;
                case 2:
                    point = pointToEnemy(player.getCoord().x, player.getCoord().y, 0, 720, 6);
                    bullets.add(new Bullet(0, 720, point.x, point.y, "Bullet.gif"));
                    break;
                case 3:
                    point = pointToEnemy(player.getCoord().x, player.getCoord().y, 1080, 720, 6);
                    bullets.add(new Bullet(1080, 720, point.x, point.y, "Bullet.gif"));
                    break;
            }
        }
    }

    public Point pointToEnemy(int enemyX, int enemyY, int coordX, int coordY, double speed) {
        double angle = Math.atan2(enemyY - coordY, enemyX - coordX);
        return new Point((int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle)));
    }

    public void spawnInterval() {
        startCount++;
        if (startCount == spawnRate) {
            spawnRocket();
            startCount = 0;
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                delta--;
            }
        }
    }

    public void resetGame() {
        gameState = 1;
        gameStarted = false;
        player = new Player(this, keyReader, 500, 500, "Player.gif", "PlayerHit.gif");
        walls.clear();
        bullets.clear();
        secondsPassed = 0;
        startCount = 0;
        initialize();
        stopTimer();
        gamePanel.repaint();
    }

    public KeyReader getKeyReader() {
        return keyReader;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getGameState() {
        return gameState;
    }

    public BackGround getBackGround() {
        return backGround;
    }

    public StartGamePic getStartGamePic() {
        return startGamePic;
    }

    public EndGamePic getEndGamePic() {
        return endGamePic;
    }

    public Font getCustomFont() {
        return customFont;
    }

    public Player getPlayer() {
        return player;
    }

    public Image getDarknessImage() {
        return DarknessImage;
    }

    public void setDarknessImage(Image darknessImage) {
        DarknessImage = darknessImage;
    }

    public Heartz getHeartz() {
        return heartz;
    }

    public Heartz getHeartzHalf() {
        return heartzHalf;
    }

    public Heartz getHeartzLast() {
        return heartzLast;
    }
}
