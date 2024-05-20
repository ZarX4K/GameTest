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
    private final ArrayList<Rocket> rockets;
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
    Thread gamethread;
    long currentTime;
    double delta = 0;
    int fps = 60;
    long lastTime = System.nanoTime();
    double drawInterval = 1000000000 / fps;
    public int width = 1080, height = 720;
    int secondsPassed;
    int startCount;
    int spawnRate = 27;
    Player player = new Player(this, keyReader, 500, 500, "Player.gif");



    public GameLogic() {
        this.enemies = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.rockets = new ArrayList<>();
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
        walls.add(wall1);
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
 //               }
            }
            for (Rocket rocket : rockets) {
                g.drawImage(rocket.getImage(), rocket.getCoord().x, rocket.getCoord().y, this);
            }
            g.setFont(customFont);
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
            g.setFont(customFont);
            g.setColor(Color.WHITE);
        }
    }

    private void startTimer() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
            }
        });
        timer.start();
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }

    public void changeGameState() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameState = 2; // Start the game when the mouse is clicked
            }
        });
    }


    public void update() {
        ArrayList<Rocket> rocketsToRemove = new ArrayList<>();
        changeGameState();

        controlledMove();

        for (Rocket rocket : rockets) {
            rocket.move();
            if (player.isCollided(rocket.getRectangle()) && !player.dead) {

                System.out.println("Collision##############");
                player.hit();

                player.dead = true;
                rocketsToRemove.add(rocket);

            }
            if (player.dead) {
                player.timeToAlive -= 2;
                //   System.out.println("ttl:" + player.timeToAlive);
            }
            if (player.dead && player.timeToAlive < 0) {
                player.dead = false;
                player.timeToAlive = 1500;
            }
        }
        rockets.removeAll(rocketsToRemove);
        spawnInterval();
        if (player.getLives() <= 0) {
            gameState = 3;
        }
    }

    private void controlledMove() {
        int newX = player.getX();
        int newY = player.getY();

        if (keyReader.upPressed) newY -= player.getSpeed();
        if (keyReader.downPressed) newY += player.getSpeed();
        if (keyReader.leftPressed) newX -= player.getSpeed();
        if (keyReader.rightPressed) newX += player.getSpeed();

        if (keyReader.rightPressed && keyReader.upPressed) {
            newX += player.getSpeed() - 8;
            newY -= player.getSpeed() - 8;

        }
        if (keyReader.leftPressed && keyReader.upPressed) {
            newX -= player.getSpeed() - 8;
            newY -= player.getSpeed() - 8;

        }
        if (keyReader.leftPressed && keyReader.downPressed) {
            newX -= player.getSpeed() - 8;
            newY += player.getSpeed() - 8;

        }
        if (keyReader.rightPressed && keyReader.downPressed) {
            newX += player.getSpeed() - 8;
            newY += player.getSpeed() - 8;

        }


        // Create a rectangle representing the player's new position
        Rectangle newPlayerRectangle = new Rectangle(newX, newY, player.getWidth(), player.getHeight());

        // Check collision with walls for the new position
        for (Wall wall : walls) {
            Rectangle wallRectangle = wall.getRectangle();
            if (newPlayerRectangle.intersects(wallRectangle)) {
                // Adjust new position to prevent collision with the wall
                if (keyReader.upPressed) {
                    newY = Math.max(newY, wall.getY() + wall.getHeight());
                }
                if (keyReader.downPressed) {
                    newY = Math.min(newY, wall.getY() - player.getHeight());
                }
                if (keyReader.leftPressed) {
                    newX = Math.max(newX, wall.getX() + wall.getWidth());
                }
                if (keyReader.rightPressed) {
                    newX = Math.min(newX, wall.getX() - player.getWidth());
                }
            }
        }

        // Update player's position
        player.setX(newX);
        player.setY(newY);
    }

    private void spawnRocket() {

        Random rando = new Random();
        int randPick = rando.nextInt(4) + 1;
        Random random = new Random();
        for (int i = 0; i < randPick; i++) {
            int randCorner = random.nextInt(4); // Randomly select a corner

            Point point = null;
            switch (randCorner) {

                case 0:
                    point = pointToEnemy(player.x, player.y, 0, 0, 6);
                    rockets.add(new Rocket(0, 0, point.x, point.y, "Bullet.png"));
                    break;

                case 1:
                    point = pointToEnemy(player.x, player.y, 1080, 0, 6);
                    rockets.add(new Rocket(1080, 0, point.x, point.y, "Bullet.png"));
                    break;
                case 2:
                    point = pointToEnemy(player.x, player.y, 0, 720, 6);
                    rockets.add(new Rocket(0, 720, point.x, point.y, "Bullet.png"));
                    break;
                case 3:
                    point = pointToEnemy(player.x, player.y, 1080, 720, 6);
                    rockets.add(new Rocket(1080, 720, point.x, point.y, "Bullet.png"));
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
        gamethread = new Thread(this);
        gamethread.start();
    }

    @Override
    public void run() {
        while (gamethread != null) {
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
        gameState = 1; // Set to initial game state
        gameStarted = false; // Reset the game started flag
        player = new Player(this, keyReader, 500, 500, "Player.gif"); // Recreate the player
        enemies.clear(); // Clear enemies
        walls.clear(); // Clear walls
        rockets.clear(); // Clear rockets
        secondsPassed = 0; // Reset time
        startCount = 0; // Reset start count
        initialize(); // Reinitialize game objects
        repaint(); // Repaint the game screen
    }
}
