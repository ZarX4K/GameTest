package org.example.logic;

import org.example.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartGamePic {
    GameLogic gamePanel;
    BufferedImage image;
    BufferedImage image2;
    int width = 1080;
    int height = 720;
    int startCount;
    int spawnRate = 140;

    public StartGamePic(GameLogic gamePanel){
        this.gamePanel = gamePanel;
        getImage();
    }

    public void getImage(){
        try {
            image = ImageIO.read(new File("src/main/resources/background1.png"));;
            image2 = ImageIO.read(new File("src/main/resources/background2.png"));;

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g){
        startCount ++;
        if(startCount == spawnRate) {
            startCount = 0;
        }
        if(startCount > 0 && startCount < 40){
            g.drawImage(image,0, 0, width, height, null);

        } else {
            g.drawImage(image2,0, 0, width, height, null);
        }

    }

}
