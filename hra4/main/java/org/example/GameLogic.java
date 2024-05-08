package org.example;

import org.example.logic.BackGround;
import org.example.logic.BackGroundStart;
import org.example.logic.Direction;
import org.example.logic.Enemy;
import org.example.logic.Heartz;
import org.example.logic.KeyReader;
import org.example.logic.Player;
import org.example.logic.Rocket;
import org.example.logic.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic  extends JPanel implements Runnable {
    public int width = 1080, height = 720;
    KeyReader keyReader = new KeyReader();
    Player player = new Player(this, keyReader, 500, 500, "Player.png");
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Wall> walls;
    private final ArrayList<Rocket> rockets;
    private Heartz heartz;
    private Heartz heartz2;
    private Heartz heartz3;

    Thread gamethread;
    long currentTime;
    double delta = 0;
    long lastTime = System.nanoTime();
    int fps = 60;
    double drawInterval = 1000000000/fps;

    private final int PLAYER_STEPS = 10;
    BackGround backGround;
    BackGroundStart backGroundStart;
    private final int ROCKET_VELOCITYY = 3;
    private final int ROCKET_VELOCITYX = 3;
    int startCount;
    int spawnRate = 27;
    private final Font customFont = new Font("Arial", Font.BOLD, 16);

    public int gameState = 1;

    public GameLogic (){
        this.enemies = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.rockets = new ArrayList<>();
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
        setDoubleBuffered(true);
        backGround = new BackGround(this);
        backGroundStart = new BackGroundStart(this);
        addKeyListener(keyReader);
        setFocusable(true);


    }

    public void initialize() {
        Wall wall1 = new Wall(250, 30, 250, 130, Color.BLACK);
        walls.add(wall1);
        heartz = new Heartz(980, 1, "Heartz.png");
        heartz2 = new Heartz(980, 1, "HeartHalf.png");
        heartz3 = new Heartz(980, 1, "HeartLast.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameState == 2){
            backGround.draw(g);
            player.draw(g);
            for (Wall wall : walls) {
                if (wall.isActive()) {
                    g.setColor(wall.getColor());
                    g.drawLine(wall.getCoordStart().x, wall.getCoordStart().y, wall.getCoordEnd().x, wall.getCoordEnd().y);
                }
            }
            for (Rocket rocket : rockets) {
                g.drawImage(rocket.getImage(), rocket.getCoord().x, rocket.getCoord().y, this);
            }
            g.setFont(customFont);
            g.drawString("Health: " + player.getLives(), 970, 80);
            if (player.getLives() > 6) {
                g.drawImage(heartz.getImage(), heartz.getX(), heartz.getY(), null);
            }
            else if (player.getLives() > 3) {
                g.drawImage(heartz2.getImage(), heartz2.getX(), heartz2.getY(), null);
            }
            else if (player.getLives() > 0) {
                g.drawImage(heartz3.getImage(), heartz3.getX(), heartz3.getY(), null);
            }
        } else {
            backGroundStart.draw(g);
            g.setFont(customFont);
            g.setColor(Color.WHITE);
            // g.drawString("PRESS ENTER TO START GAME ", 160,400);

            setLayout();
        }

    }

    public void setLayout(){
        JButton startButton = new JButton("START");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }



    public void changeGameState(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameState = 2; // Start the game when the mouse is clicked
            }});
    }

    public void update() {

        changeGameState();

        controlledMove();

        for (Rocket rocket : rockets) {
            rocket.move();
            if (player.isCollided(rocket.getRectangle()) && !player.dead) {

                System.out.println("Collision");
                player.hit();
                player.dead = true;

            }if (player.dead) {
                player.timeToAlive -= 2;
                //   System.out.println("ttl:" + player.timeToAlive);
            }
            if (player.dead && player.timeToAlive < 0) {
                player.dead = false;
                player.timeToAlive = 1500;
            }
        }
        spawnInterval();
    }

    private void controlledMove() {
        int newX = player.getX();
        int newY = player.getY();

        if (keyReader.upPressed) {
            newY -= player.getSpeed();
        }
        if (keyReader.downPressed) {
            newY += player.getSpeed();
        }
        if (keyReader.leftPressed) {
            newX -= player.getSpeed();
        }
        if (keyReader.rightPressed) {
            newX += player.getSpeed();
        }

        // Create a rectangle representing the player's new position
        Rectangle newPlayerRectangle = new Rectangle(newX, newY, player.getWidth(), player.getHeight());

        // Check collision with walls for the new position
        for (Wall wall : walls) {
            Rectangle wallRectangle = wall.getRectangle();
            if (newPlayerRectangle.intersects(wallRectangle)) {
                // Adjust new position to prevent collision with the wall
                if (keyReader.upPressed) {
                    newY = Math.max(newY, wallRectangle.y + wallRectangle.height);
                }
                if (keyReader.downPressed) {
                    newY = Math.min(newY, wallRectangle.y - player.getHeight());
                }
                if (keyReader.leftPressed) {
                    newX = Math.max(newX, wallRectangle.x + wallRectangle.width);
                }
                if (keyReader.rightPressed) {
                    newX = Math.min(newX, wallRectangle.x - player.getWidth());
                }
                break; // Exit the loop after handling collision with one wall
            }
        }

        // Update player's position
        player.setX(newX);
        player.setY(newY);
    }

    private void spawnRocket() {
        Random random = new Random();
        int randCorner;
        randCorner = random.nextInt(5);
        randCorner = 1;


        switch (randCorner) {

            case 1:
                Point point = pointToEnemy(player.x, player.y, 0, 0, 7);
                rockets.add(new Rocket(0, 0, point.x, point.y, "raketa.jpg"));
                System.out.println("leva horni strela");
                break;

            case 2:
                rockets.add(new Rocket(1080, 0, -ROCKET_VELOCITYY, ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("prava horni strela ");
                break;
            case 3:
                rockets.add(new Rocket(0, 720, ROCKET_VELOCITYY, -ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("leva dolni strela");
                break;
            case 4:
                rockets.add(new Rocket(1080, 720, -ROCKET_VELOCITYY, -ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("prava dolni strela");
                break;

        }
    }

    public Point pointToEnemy(int enemyX, int enemyY, int coordX, int coordY, double speed) {
        double angle = Math.atan2(enemyY - coordX, enemyX - coordY);

        return new Point((int)(speed * Math.cos(angle)), (int)(speed * Math.sin(angle)));
    }

    public void spawnInterval(){
        startCount ++;
        if(startCount == spawnRate){
            spawnRocket();
            startCount = 0;
        }
    }

    public void startGameThread(){
        gamethread = new Thread(this);
        gamethread.start();
    }

    @Override
    public void run() {
        while (gamethread != null){
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }
}
