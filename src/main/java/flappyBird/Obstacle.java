package flappyBird;

import java.awt.*;

public class Obstacle extends GameObj {
    // an obstacle is composed of a rectangle
    //y velocity is either positive, negative, or 0.

    public static int posX = 1001;
    public static final int VEL_X = -5;
    private final Color color;
    private int id;

    public Obstacle(int courtWidth, int courtHeight, int width, int height,
                    int posY, int velY, Color color, int id) {
        super(VEL_X, velY, posX, posY, width, height, courtWidth, courtHeight);
        this.color = color;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRoundRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),15, 15);
    }

    @Override
    public void move() {
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());
        //this.setHeight(this.getHeight() + this.getVy());
    }

    @Override
    public void clip() { }

    @Override
    public String toString() {
        return this.getPx() + ", " + this.getPy() + ", " + this.getWidth() + ", " + this.getHeight()
                + ", " + this.getVy() + ", " + this.color.toString() + ", " + this.getId();
    }

}
