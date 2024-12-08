package flappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Coin extends GeneratedObj {
    public static final String IMG_FILE = "files/coin.png";
    private static BufferedImage img;

    public Coin(int posX, int posY) {
        super(posX, posY, 0, 20, 20);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public static void generateRandomCoin(ArrayList<Coin> coins, ArrayList<Obstacle> obstacles) {
        int counter = 0;
        while (counter < 5) {
            int posX = randomInt(1000, 1500);
            int posY = randomInt(0, 400);
            Coin c = new Coin(posX, posY);
            boolean intersect = false;
            for (Obstacle o : obstacles) {
                if (c.intersects(o)) {
                    intersect = true;
                    break;
                }
            }
            if (!intersect) {
                coins.add(c);
                counter++;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public String toString() {
        return this.getPx() + ", " + this.getPy() + ", ";
    }
}
