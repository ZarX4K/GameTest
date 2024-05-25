package org.example;

import org.example.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic extends JPanel implements Runnable {
    private final ArrayList<Wall> walls;
    private final ArrayList<Bullet> bullets;
    private Heartz heartz;
    private Heartz heartz2;
    private Heartz heartz3;
    BackGround backGround;
    StartGamePic startGamePic;
    EndGamePic endGamePic;
    KeyReader keyReader = new KeyReader();
    private final Font customFont = new Font("Arial", Font.BOLD, 16);
    public int gameState = 1;
    private boolean gameStarted = false;
    Thread gameThread;
    long currentTime;
    double delta = 0;
    int fps = 60;
    long lastTime = System.nanoTime();
    double drawInterval = 1000000000 / fps;
    public int width = 1080, height = 720;
    int secondsPassed;
    int startCount;
    int spawnRate = 27;
    Timer timer;
    Player player = new Player(this, keyReader, 200, 200, "Player.gif", "PlayerHit.gif");
    private Image DarknessImage;
    private int darknessWidth;
    private int darknessHeight;

    public GameLogic() {
        this.walls = new ArrayList<>();
        this.bullets = new ArrayList<>();
        backGround = new BackGround(this);
        startGamePic = new StartGamePic(this);
        endGamePic = new EndGamePic(this);
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
        addKeyListener(keyReader);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    startTimer();
                    gameState = 2;
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && gameState == 3) {
                    resetGame();
                }
            }
        });
    }

    public void initialize() {
        createMaze();
        wallActivation();
        heartz = new Heartz(980, 1, "Heartz.gif");
        heartz2 = new Heartz(980, 1, "Heartz2.gif");
        heartz3 = new Heartz(980, 1, "Heartz3.gif");
    }
    private void createMaze() {
        walls.add(new Wall(135, 51,  "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(600, 90,  "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(673, 523, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(843, 10,  "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(80, 423,  "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(984, 586, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(380, 236, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(864, 321, "Rock.gif", "RockFlash.gif"));
        walls.add(new Wall(300, 521, "Rock.gif", "RockFlash.gif"));
    }

private void wallActivation(){
     int activationTime = 60;
    for (Wall wall : walls) {
        wall.setActivationTime(activationTime);
        activationTime += 1;
    }
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == 2) {
            backGround.draw(g);
            player.draw(g);
            for (Wall wall : walls) {
                wall.draw(g);
            }
            for (Bullet bullet : bullets) {
                g.drawImage(bullet.getImage(), bullet.getCoord().x, bullet.getCoord().y,null);
            }
            g.setFont(customFont);
            g.setColor(Color.WHITE);
            g.drawString("Time Alive: " + getSecondsPassed(), 960, 100);
            g.drawString("Health: " + player.getLives(), 970, 80);
            if (player.getLives() > 6) {
                g.drawImage(heartz.getImage(), heartz.getX(), heartz.getY(), null);
            } else if (player.getLives() > 3) {
                g.drawImage(heartz2.getImage(), heartz2.getX(), heartz2.getY(), null);
            } else if (player.getLives() > 0) {
                g.drawImage(heartz3.getImage(), heartz3.getX(), heartz3.getY(), null);
            }
            if (secondsPassed > 119) {

                DarknessImage = new ImageIcon(getClass().getResource("/Darkness.png")).getImage();
                darknessWidth = DarknessImage.getWidth(null);
                darknessHeight = DarknessImage.getHeight(null);
                int darknessX = player.getCoord().x + player.getWidth() / 2 - darknessWidth / 2;
                int darknessY = player.getCoord().y + player.getHeight() / 2 - darknessHeight / 2;
                g.drawImage(DarknessImage, darknessX, darknessY, this);


            }

        } else if (gameState == 3) {
            endGamePic.draw(g);
        } else {
            startGamePic.draw(g);
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


    public void update() {
        if (!gameStarted) {
            return;
        }

        ArrayList<Bullet> rocketsToRemove = new ArrayList<>();
        controlledMove();
        for (Bullet bullet : bullets) {
            bullet.move();
            if (player.isCollided(bullet.getRectangle()) && player.canBeHit()) {
                player.hit();
                System.out.println("Collision");
                rocketsToRemove.add(bullet);
            }
        }
        bullets.removeAll(rocketsToRemove);
        spawnInterval();
        if (player.getLives() <= 0) {
            gameState = 3;
        }
        repaint();
    }

    private void controlledMove() {
        int newX = player.getCoord().x;
        int newY = player.getCoord().y;

        if (keyReader.upPressed) newY -= player.getSpeed();
        if (keyReader.downPressed) newY += player.getSpeed();
        if (keyReader.leftPressed) newX -= player.getSpeed();
        if (keyReader.rightPressed) newX += player.getSpeed();
        if (keyReader.unstuckPressed){
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
            player.setCoord(new Coordinates(newX,player.getCoord().y));
        }
        if (!checkCollisions(player.getCoord().x, newY)) {
            newY = Math.max(0, Math.min(newY, height - player.getHeight()));
            player.setCoord(new Coordinates(player.getCoord().x,newY));
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
                repaint();
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
        repaint();
        stopTimer();
    }
}
