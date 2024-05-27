package org.example.logic;

import org.example.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BackGround {
    private BufferedImage image;
    private BufferedImage image2;
    private BufferedImage image3;
    private final int width = 1080;
    private final int height = 720;
    private GameLogic gameLogic;

    public BackGround(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        getImage();
    }

    public void getImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass2.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        int secondsPassed = gameLogic.getSecondsPassed();

        if (secondsPassed < 60) {
            g.drawImage(image, 0, 0, width, height, null);
        } else if (secondsPassed < 120) {
            g.drawImage(image2, 0, 0, width, height, null);
        } else {
            g.drawImage(image3, 0, 0, width, height, null);
        }
    }
}
