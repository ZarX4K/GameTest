package org.example.logic;

import org.example.GameLogic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyReader implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed,unstuckPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W){
            upPressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            downPressed = true;
        }
         if(e.getKeyCode() == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            System.out.println("lalala");
        }
        if (e.getKeyCode() == KeyEvent.VK_R){
            unstuckPressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W){
            upPressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            downPressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_R){
            unstuckPressed = false;
        }
    }
}
