package flappyBird;

import java.awt.Graphics;

/**
 * An object in the game.
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class GameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private int px;
    private int py;

    /* Size of object, in pixels. */
    private final int width;
    private final int height;

    /* Velocity: number of pixels to move every time move() is called. */
    private int vx;
    private int vy;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private final int maxX;
    private final int maxY;

    private final int courtWidth;
    private final int courtHeight;

    /**
     * Constructor
     */
    public GameObj() {
        px = 1001;
        py = 200;
        width = 20;
        height = 20;
        courtWidth = 2000;
        courtHeight = 2000;
        maxX = courtWidth - width;
        maxY = courtHeight - height;
    }

    public GameObj(
            int vx, int vy, int px, int py, int width, int height, int courtWidth,
            int courtHeight
    ) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;

        // take the width and height into account when setting the bounds for
        // the upper left corner of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;

    }

    // **********************************************************************************
    // * GETTERS
    // **********************************************************************************
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getVx() {
        return this.vx;
    }

    public int getVy() {
        return this.vy;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    // **************************************************************************
    // * SETTERS
    // **************************************************************************
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    // **************************************************************************
    // * UPDATES AND OTHER METHODS
    // **************************************************************************

    /**
     * Prevents the object from going outside the bounds of the area
     * designated for the object (i.e. Object cannot go outside the active
     * area the user defines for it).
     */
    public void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    public void move() {
        this.px += this.vx;
        this.py += this.vy;

        clip();
    }

    // INTERSECT & OUT OF BOUNDS
    // -----------------------------------------------------------
    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px
                && this.py + this.height >= that.py
                && that.px + that.width >= this.px
                && that.py + that.height >= this.py);
    }

    public boolean isOutOfBounds() {
        return (this.px + this.width < 0);
    }

    public abstract void draw(Graphics g);
}