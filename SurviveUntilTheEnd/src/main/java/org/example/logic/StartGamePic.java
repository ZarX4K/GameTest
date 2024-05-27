package org.example.logic;

import org.example.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartGamePic {
    private BufferedImage image;
    private BufferedImage image2;
    private final int width = 1080;
    private final int height = 720;
    private int startCount;
    private final int switchRate = 60;
    private GameLogic gameLogic;

    public StartGamePic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        getImage();
    }

    public void getImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/background1.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/background2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        startCount++;
        if (startCount >= switchRate * 2) {
            startCount = 0;
        }
        if (startCount < switchRate) {
            g.drawImage(image, 0, 0, width, height, null);
        } else {
            g.drawImage(image2, 0, 0, width, height, null);
        }
    }
}
