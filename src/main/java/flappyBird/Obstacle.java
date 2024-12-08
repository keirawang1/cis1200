package flappyBird;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Obstacle extends GeneratedObj {
    // an obstacle is composed of a rectangle

    private final Color color;
    private static int id = 0; //tracks unique obstacle

    public Obstacle(int width, int height, int posY, int velY, Color color, int id) {
        super(posY, velY, width, height, id);
        this.color = color;
    }

    // GENERATE OBSTACLE -----------------------------------------------------------------------------
    //y velocity is either positive, negative, or 0.
    public static void generateRandomObstacle(ArrayList<Obstacle> obstacles) {
        int width = randomInt(200, 500);
        int gap = randomInt(80, 100);
        int posY = randomInt(-400, -750);
        int velY = -1;
        if (posY + 800 < 200) {
            velY = 1;
        }
        Color color = getRandomColor(colorList);
        obstacles.add(new Obstacle(width, 800, posY, velY, color, id));
        obstacles.add(new Obstacle(width, 800, posY + 800 + gap, velY, color, id));
        id++;
    }

    // COLORS -----------------------------------------------------------------------------
    public static Color getRandomColor(java.util.List<Color> colors) {
        if (colors == null || colors.isEmpty()) {
            throw new IllegalArgumentException("Color list cannot be null or empty.");
        }
        Random random = new Random();
        int index = random.nextInt(colors.size()); // Generate a random index
        return colors.get(index); // Return the color at the random index
    }

    static List<Color> colorList = List.of(
            new Color(155, 144, 0),
            new Color(98, 93, 64),
            new Color(68, 151, 119),
            new Color(107, 123, 98),
            new Color(187, 183, 131),
            new Color(127, 166, 135),
            new Color(157, 184, 144));

    //-----------------------------------------------------------------------------

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRoundRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),15, 15);
    }

    @Override
    public String toString() {
        return this.getPx() + ", " + this.getPy() + ", " + this.getWidth() + ", " + this.getHeight()
                + ", " + this.getVy() + ", " + this.color.toString() + ", " + this.getId();
    }

}
