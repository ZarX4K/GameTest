package org.example;

import javax.swing.*;

public class GameGraphics extends JFrame {
    public GameGraphics(){
        setTitle("Survive Until The End");
        setSize(1080, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setIconImage(new ImageIcon("src/main/resources/gameLogo.png").getImage());
        GameLogic  gameLogic = new GameLogic ();
        gameLogic.initialize();
        add(gameLogic);
        gameLogic.startGameThread();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
