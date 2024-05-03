package org.example;

import org.example.logic.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameLogic {
    public static int width = 1080;
    public static int height = 720;
    private final int ENEMY_STEPS = 5;
    private final int PLAYER_STEPS = 10;
    private final int ROCKET_VELOCITYY = 3;
    private final int ROCKET_VELOCITYX = 3;
    private Player player;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Wall> walls;
    private final ArrayList<Rocket> rockets;
    private Heartz heartz;
    private Heartz heartz2;
    private Heartz heartz3;



    public void handleKeyEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                movePlayer(Direction.UP);
                break;
            case KeyEvent.VK_S:
                movePlayer(Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                movePlayer(Direction.LEFT);
                break;
            case KeyEvent.VK_D:
                movePlayer(Direction.RIGHT);
                break;
        }
    }


    public GameLogic() {
        this.player = null;
        this.enemies = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.rockets = new ArrayList<>();
        this.player = null;



    }


    public void initialize() {

        player = new Player(500, 500, "Player.png");

        Wall wall1 = new Wall(250, 30, 250, 130, Color.BLACK);
        //Wall wall2 = new Wall(100, 50, 150, 50, Color.BLACK);
        walls.add(wall1);
      //  walls.add(wall2);
        heartz = new Heartz(980, 1, "Heartz.png");
        heartz2 = new Heartz(980, 1, "HeartHalf.png");
        heartz3 = new Heartz(980, 1, "HeartLast.png");
        spawnRockets();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // for(i=0; i>0; i++)
                spawnRockets();
            }
        }, 2000, 1500);

    }

    public void update() {

        if (addKeyListener().upPressed) {
            player.move(PLAYER_STEPS, Direction.UP);
        }
        if (keyListener.downPressed) {
            player.move(PLAYER_STEPS, Direction.DOWN);
        }
        if (keyReader.leftPressed) {
            player.move(PLAYER_STEPS, Direction.LEFT);
        }
        if (keyReader.rightPressed) {
            player.move(PLAYER_STEPS, Direction.RIGHT);
        }
        //player.move(2, Direction.RIGHT);
        for (Enemy enemy : enemies) {
            int differenceX = Math.abs(player.getCoord().x - enemy.getCoord().x);
            int differenceY = Math.abs(player.getCoord().y - enemy.getCoord().y);

            if (differenceX > differenceY) {
                // Direction LEFT, RIGHT
                if (player.getCoord().x - enemy.getCoord().x > 0) {
                    // Direction RIGHT
                    enemy.move(ENEMY_STEPS, Direction.RIGHT);
                } else {
                    // Direction LEFT
                    enemy.move(ENEMY_STEPS, Direction.LEFT);
                }
            } else {
                // Direction UP, DOWN
                if (player.getCoord().y - enemy.getCoord().y > 0) {
                    // Direction DOWN
                    enemy.move(ENEMY_STEPS, Direction.DOWN);
                } else {
                    // Direction UP
                    enemy.move(ENEMY_STEPS, Direction.UP);
                }
            }



        }
        for (Wall wall : walls) {
            if (player.isCollided(wall.getRectangle())) {
                wall.inactivate();
            }
        }
        for (Rocket rocket : rockets) {
            rocket.move();
            if (player.isCollided(rocket.getRectangle()) && !player.dead) {

                System.out.println("Collision");
                player.hit();
                player.dead = true;

            }if (player.dead) {
                player.timeToAlive -= 20;
             //   System.out.println("ttl:" + player.timeToAlive);
            }
            if (player.dead && player.timeToAlive < 0) {
                player.dead = false;
                player.timeToAlive = 2000;
            }

        }

    }

    public void timer() {
        Timer delay = new Timer();
        delay.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) ;
            }
        }, 10000, 10000);




    }

    private void spawnRockets() {
        Random random = new Random();
        int randCorner;
        randCorner = random.nextInt(5);


        switch (randCorner) {

            case 1:
                rockets.add(new Rocket(0, 0, ROCKET_VELOCITYY, ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("leva horni strela");
                break;

            case 2:
                rockets.add(new Rocket(1080, 0, -ROCKET_VELOCITYY, ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("prava horni strela ");
                timer();
                break;
            case 3:
                rockets.add(new Rocket(0, 720, ROCKET_VELOCITYY, -ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("leva dolni strela");
                timer();
                break;
            case 4:
                rockets.add(new Rocket(1080, 720, -ROCKET_VELOCITYY, -ROCKET_VELOCITYX, "raketa.jpg"));
                System.out.println("prava dolni strela");
                timer();
                break;

        }

    }

    public boolean predictCollision(Direction direction) {
        Rectangle moveRectangle = new Rectangle();
        switch (direction) {
            case RIGHT -> {
                moveRectangle = new Rectangle(player.getX() + PLAYER_STEPS, player.getY(), player.getWidth(), player.getHeight());
            }
            case LEFT -> {
                moveRectangle = new Rectangle(player.getX() - PLAYER_STEPS, player.getY(), player.getWidth(), player.getHeight());
            }
            case UP -> {
                moveRectangle = new Rectangle(player.getX(), player.getY() - PLAYER_STEPS, player.getWidth(), player.getHeight());
            }
            case DOWN -> {
                moveRectangle = new Rectangle(player.getX(), player.getY() + PLAYER_STEPS, player.getWidth(), player.getHeight());
            }

        }
        for (Wall wall : walls) {
            if (moveRectangle.intersects(wall.getRectangle())) {
                return true;
            }
        }
        return false;


    }



    public Heartz getHeartz() {
        return heartz;
    }
    public Heartz getHeartz2() {
        return heartz2;
    }
    public Heartz getHeartz3() {
        return heartz3;
    }

    public ArrayList<Enemy> getEnemy() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }



    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void movePlayer(Direction direction) {
        player.move(PLAYER_STEPS, direction);

    }



    public ArrayList<Rocket> getRockets() {
        return rockets;
    }


    public void addKeyListener(KeyListener keyListener) {
    }
}
