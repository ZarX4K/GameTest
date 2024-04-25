package org.example;

import org.example.logic.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameLogic {
    public static int width = 1080;
    public static int height = 720;
    private final int ENEMY_STEPS = 5;
    private final int BALL_STEPS = 10;
    private final int ROCKET_VELOCITYY = 3;
    private final int ROCKET_VELOCITYX = 3;
    private Ball ball;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Wall> walls;
    private final ArrayList<Rocket> rockets;
    private Heartz heartz;


    public GameLogic() {
        this.ball = null;
        this.enemies = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.rockets = new ArrayList<>();

    }


    public void initialize() {

        ball = new Ball(500, 500, "Player.png");

        Wall wall1 = new Wall(250, 30, 250, 130, Color.BLACK);
        Wall wall2 = new Wall(100, 50, 150, 50, Color.BLACK);
        walls.add(wall1);
        walls.add(wall2);
        heartz = new Heartz(980, 1, "Heartz.png");

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


        //ball.move(2, Direction.RIGHT);
        for (Enemy enemy : enemies) {
            int differenceX = Math.abs(ball.getCoord().x - enemy.getCoord().x);
            int differenceY = Math.abs(ball.getCoord().y - enemy.getCoord().y);

            if (differenceX > differenceY) {
                // Direction LEFT, RIGHT
                if (ball.getCoord().x - enemy.getCoord().x > 0) {
                    // Direction RIGHT
                    enemy.move(ENEMY_STEPS, Direction.RIGHT);
                } else {
                    // Direction LEFT
                    enemy.move(ENEMY_STEPS, Direction.LEFT);
                }
            } else {
                // Direction UP, DOWN
                if (ball.getCoord().y - enemy.getCoord().y > 0) {
                    // Direction DOWN
                    enemy.move(ENEMY_STEPS, Direction.DOWN);
                } else {
                    // Direction UP
                    enemy.move(ENEMY_STEPS, Direction.UP);
                }
            }
            if (ball.dead) {
                ball.timeToAlive -= 20;
                System.out.println("ttl:" + ball.timeToAlive);
            }
            if (ball.dead && ball.timeToAlive < 0) {
                ball.dead = false;
                ball.timeToAlive = 2000;

            }
            for (Rocket rocket : rockets) {
                rocket.move();
                if (ball.isCollided(rocket.getRectangle()) && !ball.dead) {

                    System.out.println("Collision");
                    ball.hit();
                    ball.dead = true;

                }

            }


        }
        for (Wall wall : walls) {
            if (ball.isCollided(wall.getRectangle())) {
                wall.inactivate();
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
                moveRectangle = new Rectangle(ball.getX() + BALL_STEPS, ball.getY(), ball.getWidth(), ball.getHeight());
            }
            case LEFT -> {
                moveRectangle = new Rectangle(ball.getX() - BALL_STEPS, ball.getY(), ball.getWidth(), ball.getHeight());
            }
            case UP -> {
                moveRectangle = new Rectangle(ball.getX(), ball.getY() - BALL_STEPS, ball.getWidth(), ball.getHeight());
            }
            case DOWN -> {
                moveRectangle = new Rectangle(ball.getX(), ball.getY() + BALL_STEPS, ball.getWidth(), ball.getHeight());
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

    public ArrayList<Enemy> getEnemy() {
        return enemies;
    }

    public Ball getBall() {
        return ball;
    }


    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void movePlayer(Direction direction) {
        ball.move(BALL_STEPS, direction);

    }


    public ArrayList<Rocket> getRockets() {
        return rockets;
    }


}
