package flappyBird;

public abstract class GeneratedObj extends GameObj {
    public static final int VEL_X = -5;
    public static final int COURT_WIDTH = 2000;
    public static final int COURT_HEIGHT = 400;

    public GeneratedObj(int posX, int posY, int velY, int width, int height) {
        super(VEL_X, velY, posX, posY, width, height, COURT_WIDTH, COURT_HEIGHT);
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + 1) + min;
    }

    @Override
    public void move() {
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());
    }

    @Override
    public void clip() {
    }

}
