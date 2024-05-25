package org.example.logic;

import org.example.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackGround {
    GameLogic gamePanel;
    BufferedImage image;
    BufferedImage image2;
    BufferedImage image3;
    int width = 1080;
    int height = 720;
    long startTime;
    int SecondLevel = 60;
    int ThirdLevel = 120;

    public BackGround(GameLogic gamePanel) {
        this.gamePanel = gamePanel;
        getImage();
        startTime = System.currentTimeMillis();

    }

    private void getImage() {
        try {
            image = ImageIO.read(new File("src/main/resources/BackGrGrass.png"));
            image2 = ImageIO.read(new File("src/main/resources/BackGrGrass2.png"));
            image3 = ImageIO.read(new File("src/main/resources/BackGrGrass3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        int secondsPassed = gamePanel.getSecondsPassed();

        if (secondsPassed < SecondLevel) {
            g.drawImage(image, 0, 0, width, height, null);
        } else if (secondsPassed < ThirdLevel) {
            g.drawImage(image2, 0, 0, width, height, null);
        } else {
            g.drawImage(image3, 0, 0, width, height, null);
        }
    }
}