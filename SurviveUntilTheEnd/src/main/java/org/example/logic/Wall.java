    package org.example.logic;

    import javax.swing.*;
    import java.awt.*;

    public class Wall {
        private boolean active;
        private boolean flashing;
        public int x;
        public int y;
        public int width = 90;
        public int height = 90;
        private long activationTime;
        private long flashStartTime;
        public Image flashImage;
        public Image image;

        public Wall(int x, int y, String url, String flashUrl) {
            this.x = x;
            this.y = y;
            this.image = new ImageIcon(getClass().getResource("/" + url)).getImage();
            this.flashImage = new ImageIcon(getClass().getResource("/" + flashUrl)).getImage();
            this.active = false;
            this.flashing = false;
        }

        public void setActivationTime(long activationTime) {
            this.activationTime = activationTime;
        }

        public void activate(long currentTime) {
            if (!active && !flashing && currentTime >= activationTime) {
                this.flashing = true;
                this.flashStartTime = currentTime;
            }
            if (flashing && (currentTime - flashStartTime) >= 3) {
                this.flashing = false;
                this.active = true;
            }
        }

        public boolean isActive() {
            return active;
        }

        public boolean isFlashing() {
            return flashing;
        }

        public void draw(Graphics g) {
            if (flashing) {
                g.drawImage(flashImage, x, y, width, height, null);
            } else if (active) {
                g.drawImage(image, x, y, width, height, null);
            }
        }

        public Rectangle getRectangle() {
            return new Rectangle(x, y, width, height);
        }

        public Image getImage() {
            return image;
        }

    }
