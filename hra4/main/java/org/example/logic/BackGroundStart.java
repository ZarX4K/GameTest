package org.example.logic;

import org.example.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackGroundStart {
    GameLogic gamePanel;
    BufferedImage image;
    int width = 1080;
    int height = 720;

    public BackGroundStart(GameLogic gamePanel){
        this.gamePanel = gamePanel;
        getImage();
    }

    public void getImage(){
        try {
            image = ImageIO.read(new File("src/main/resources/background2.png"));;
            image = ImageIO.read(new File("src/main/resources/backgorund1.png"));;


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g){
        g.drawImage(image,0, 0, width, height, null);
    }
}
