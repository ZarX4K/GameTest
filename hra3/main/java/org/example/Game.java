package org.example;

import org.example.logic.Direction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Game {
    GameLogic logic;

    public Game() {
        logic = new GameLogic();
        GameGraphics graphic = new GameGraphics(logic);
        logic.initialize();
        graphic.render(logic);
        boolean isGameOver = false;


       graphic.addKeyListener(new KeyListener() {
            public boolean upPressed, downPressed, leftPressed, rightPressed;

            @Override
            public void keyTyped(KeyEvent e) {

            }

                @Override
                public void keyPressed(KeyEvent e) {
                    logic.handleKeyEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                            upPressed = true;
                        }
                     else if (e.getKeyCode() == KeyEvent.VK_D) {
                            rightPressed = true;
                        }
                 else if (e.getKeyCode() == KeyEvent.VK_A) {
                            leftPressed = true;
                        }
                 else if (e.getKeyCode() == KeyEvent.VK_S) {
                            downPressed = true;
                        }
                }

           public boolean isUpPressed() {
               return upPressed;
           }

           public boolean isDownPressed() {
               return downPressed;
           }

           public boolean isLeftPressed() {
               return leftPressed;
           }

           public boolean isRightPressed() {
               return rightPressed;
           }

           @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    upPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    rightPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    leftPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    downPressed = false;
                }


            }
        });



        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.update();
                graphic.render(logic);


            }
        });

        timer.start();
        /*
        ;
        while (!isGameOver){
            logic.update();
            graphic.render();
        }
         */
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Game();
            }
        });
    }

    private void controlledMove(Direction direction) {
        if (!logic.predictCollision(direction)) {
            logic.movePlayer(direction);
        }
    }


    public GameLogic getLogic() {
        return logic;
    }


}
