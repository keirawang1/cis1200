package flappyBird;

public abstract class GeneratedObj extends GameObj{
    private int id;
    public static final int POS_X = 1001;
    public static final int VEL_X = -5;
    public static final int COURT_WIDTH = 2000;
    public static final int COURT_HEIGHT = 400;

    public int getId() {
        return id;
    }

    public GeneratedObj(int posY, int velY, int width, int height, int id) {
        super(VEL_X, velY, POS_X, posY, width, height, COURT_WIDTH, COURT_HEIGHT);
        this.id = id;
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
