package org.example.logic;

import org.example.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class BackGround {
    GameLogic gamePanel;
    BufferedImage image;
    BufferedImage image2;
    BufferedImage image3;
    int width = 1080;
    int height = 720;

    public BackGround(GameLogic gamePanel) {
        this.gamePanel = gamePanel;
        getImage();
    }

    private void getImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass2.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/BackGrGrass3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        int secondsPassed = gamePanel.getSecondsPassed();

        if (secondsPassed < 60) {
            g.drawImage(image, 0, 0, width, height, null);
        } else if (secondsPassed < 120) {
            g.drawImage(image2, 0, 0, width, height, null);
        } else {
            g.drawImage(image3, 0, 0, width, height, null);
        }
    }
}
