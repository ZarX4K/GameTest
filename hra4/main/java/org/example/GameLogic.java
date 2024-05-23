package org.example;

import org.example.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic extends JPanel implements Runnable {
    private final ArrayList<Enemy> enemies;
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
    Player player = new Player(this, keyReader, 500, 500, "Player.gif", "PlayerHit.gif");


    public GameLogic() {
        this.enemies = new ArrayList<>();
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
        Wall wall1 = new Wall(400, 400,  "WallUp.png");
        Wall wall2 = new Wall(550, 450,  "WallVer.png");
        walls.add(wall1);
        walls.add(wall2);
        heartz = new Heartz(980, 1,  "Heartz.png");
        heartz2 = new Heartz(980, 1, "Heartz2.png");
        heartz3 = new Heartz(980, 1, "Heartz3.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == 2) {
            backGround.draw(g);
            player.draw(g);
            for (Wall wall : walls) {
 //              if (wall.isActive()) {
                g.drawImage(wall.getImage(),wall.getX(),wall.getY(),this);
 //                      }
            }
            for (Bullet rocket : bullets) {
                g.drawImage(rocket.getImage(), rocket.getCoord().x, rocket.getCoord().y, this);
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

    public void changeGameState() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameState = 2;
            }
        });
    }


    public void update() {
        ArrayList<Bullet> rocketsToRemove = new ArrayList<>();
        changeGameState();
        controlledMove();
        for (Bullet bullet : bullets) {
            bullet.move();
            if (player.isCollided(bullet.getRectangle()) && player.canBeHit()) {
                player.hit();
                rocketsToRemove.add(bullet);
            }
        }
        bullets.removeAll(rocketsToRemove);
        spawnInterval();
        if (player.getLives() <= 0) {
            gameState = 3;
        }
        repaint(); // premaluje hrace
    }

    private void controlledMove() {
        int newX = player.getX(); // Nová pozice X hráče
        int newY = player.getY(); // Nová pozice Y hráče

        if (keyReader.upPressed) newY -= player.getSpeed(); // Pokud je stisknuta klávesa nahoru, posune hráče nahoru
        if (keyReader.downPressed) newY += player.getSpeed(); // Pokud je stisknuta klávesa dolů, posune hráče dolů
        if (keyReader.leftPressed) newX -= player.getSpeed(); // Pokud je stisknuta klávesa doleva, posune hráče doleva
        if (keyReader.rightPressed) newX += player.getSpeed(); // Pokud je stisknuta klávesa doprava, posune hráče doprava

        if (keyReader.rightPressed && keyReader.upPressed) {
            newX += player.getSpeed() - 8; // Posune hráče doprava
            newY -= player.getSpeed() - 8; // Posune hráče nahoru
        }
        if (keyReader.leftPressed && keyReader.upPressed) {
            newX -= player.getSpeed() - 8; // Posune hráče doleva
            newY -= player.getSpeed() - 8; // Posune hráče nahoru
        }
        if (keyReader.leftPressed && keyReader.downPressed) {
            newX -= player.getSpeed() - 8; // Posune hráče doleva
            newY += player.getSpeed() - 8; // Posune hráče dolů
        }
        if (keyReader.rightPressed && keyReader.downPressed) {
            newX += player.getSpeed() - 8; // Posune hráče doprava
            newY += player.getSpeed() - 8; // Posune hráče dolů
        }

        Rectangle newPlayerRectangle = new Rectangle(newX, newY, player.getWidth(), player.getHeight());

        for (Wall wall : walls) { // Pro každou zeď ve seznamu zdí
            Rectangle wallRectangle = wall.getRectangle(); // Získá obdélník zdi

            // Kontroluje horizontální kolize
            if (newPlayerRectangle.intersects(wallRectangle)) { // Pokud se nový obdélník hráče protíná s obdélníkem zdi
                if (keyReader.leftPressed || keyReader.rightPressed) { // Pokud je stisknuta klávesa doleva nebo doprava
                    if (keyReader.leftPressed) { // Pokud je stisknuta klávesa doleva
                        newX = Math.max(newX, wall.getX() + wall.getWidth()); // Nové X bude maximem z aktuálního X a X pozice zdi + její šířka
                    }
                    if (keyReader.rightPressed) { // Pokud je stisknuta klávesa doprava
                        newX = Math.min(newX, wall.getX() - player.getWidth()); // Nové X bude minimem z aktuálního X a X pozice zdi - šířka hráče
                    }
                }

                // Aktualizuje pozici hráče po horizontální úpravě
                newPlayerRectangle.setLocation(newX, player.getY());
            }

            // Kontroluje vertikální kolize
            if (newPlayerRectangle.intersects(wallRectangle)) { // Pokud se nový obdélník hráče protíná s obdélníkem zdi
                if (keyReader.upPressed || keyReader.downPressed) { // Pokud je stisknuta klávesa nahoru nebo dolů
                    if (keyReader.upPressed) { // Pokud je stisknuta klávesa nahoru
                        newY = Math.max(newY, wall.getY() + wall.getHeight()); // Nové Y bude maximem z aktuálního Y a Y pozice zdi + její výška
                    }
                    if (keyReader.downPressed) { // Pokud je stisknuta klávesa dolů
                        newY = Math.min(newY, wall.getY() - player.getHeight()); // Nové Y bude minimem z aktuálního Y a Y pozice zdi - výška hráče
                    }
                }

                // Aktualizuje pozici hráče po vertikální úpravě
                newPlayerRectangle.setLocation(player.getX(), newY);
            }
        }

        // Aktualizuje pozici hráče
        player.setX(newX);
        player.setY(newY);
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
                    point = pointToEnemy(player.x, player.y, 0, 0, 6);
                    bullets.add(new Bullet(0, 0, point.x, point.y, "Bullet.png"));
                    break;

                case 1: // Pokud je vybrán roh 1
                    point = pointToEnemy(player.x, player.y, 1080, 0, 6);
                    bullets.add(new Bullet(1080, 0, point.x, point.y, "Bullet.png"));
                    break;
                case 2: // Pokud je vybrán roh 2
                    point = pointToEnemy(player.x, player.y, 0, 720, 6);
                    bullets.add(new Bullet(0, 720, point.x, point.y, "Bullet.png"));
                    break;
                case 3: // Pokud je vybrán roh 3
                    point = pointToEnemy(player.x, player.y, 1080, 720, 6);
                    bullets.add(new Bullet(1080, 720, point.x, point.y, "Bullet.png"));
                    break;

            }
        }
    }

    // Vypočítá bod směřující k nepřáteli
    public Point pointToEnemy(int enemyX, int enemyY, int coordX, int coordY, double speed) {
        double angle = Math.atan2(enemyY - coordY, enemyX - coordX); // Vypočítá úhel

        return new Point((int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle))); // Vrátí bod se souřadnicemi směrem k nepříteli
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
            currentTime = System.nanoTime(); // Získá aktuální čas

            delta += (currentTime - lastTime) / drawInterval; // Přičte k deltě rozdíl mezi aktuálním a posledním časem vyděleným intervaly vykreslování
            lastTime = currentTime; // Aktualizuje poslední čas

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
        enemies.clear();
        walls.clear();
        bullets.clear();
        secondsPassed = 0;
        startCount = 0;
        initialize();
        repaint();
        stopTimer();
    }
}
