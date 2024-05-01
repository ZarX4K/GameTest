package org.example.logic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyReader implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            upPressed = true;
            System.out.println("d");
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            downPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            upPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            downPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = false;
        }

    }
}
