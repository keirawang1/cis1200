package flappyBird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameDisplay extends JPanel {
    // the state of the game logic
    private Bird bird = new Bird(1000, 400); // the Black Square, keyboard control
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private boolean playing = true; // whether the game is running
    private int score = 0;
    private final JLabel scoreBoard;

    // Game constants
    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 400;
    public static final int BIRD_VELOCITY_X = 10;
    public static final int BIRD_VELOCITY_Y = 5;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 20;

    public GameDisplay(JLabel scoreBoard) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    bird.setVx(-BIRD_VELOCITY_X);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    bird.setVx(BIRD_VELOCITY_X);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    bird.setVy(BIRD_VELOCITY_Y);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    bird.setVy(-BIRD_VELOCITY_Y);
                }
            }

            public void keyReleased(KeyEvent e) {
                bird.setVx(0);
                bird.setVy(0);
            }
        });
        this.scoreBoard = scoreBoard;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + 1) + min;
    }

    public void generateRandomObstacle() {
        int width = randomInt(200, 500);
        int gap = randomInt(80, 100);
        int posY = randomInt(-400, -750);
        int velY = -1;
        if (posY + 800 < 200) {
            velY = 1;
        }
        Color color = getRandomColor(colorList);
        obstacles.add(new Obstacle(2000, COURT_HEIGHT, width, 800, posY, velY, color));
        obstacles.add(new Obstacle(2000, COURT_HEIGHT, width, 800,
                posY + 800 + gap, velY, color));

    }
    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    int tickCounter = 0;
    void tick() {
        if (playing) {
            bird.move();
            scoreBoard.setText("Score: " + score);

            if (tickCounter % 275 == 0) {
                generateRandomObstacle();
            }

            if (tickCounter % 2 == 0) {
                for (Obstacle obstacle : obstacles) {
                    obstacle.move();
                }
            }
            Iterator<Obstacle> obstacleIterator = obstacles.iterator();
            while (obstacleIterator.hasNext()) {
                Obstacle o = obstacleIterator.next();
                if (bird.intersects(o)) {
                    playing = false;
                    break;
                }
                if (o.getPx() + o.getWidth() < bird.getPx()) {
                    score += 50;
                }
                if (o.isOutOfBounds()) {
                    obstacleIterator.remove();
                }
            }
            repaint();
            requestFocusInWindow();
        }
        tickCounter++;
    }

    public void reset() {
        bird = new Bird(COURT_WIDTH, COURT_HEIGHT);
        obstacles = new ArrayList<>();
        playing = true;
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        score = 0;
    }

    public void pause(JButton button) {
        playing = !playing;
        if (!playing) {
            button.setText("Unpause");
        }
        else {
            button.setText("Pause");
        }
    }

    public int getScore() {
        return score;
    }


    public static Color getRandomColor(List<Color> colors) {
        if (colors == null || colors.isEmpty()) {
            throw new IllegalArgumentException("Color list cannot be null or empty.");
        }
        Random random = new Random();
        int index = random.nextInt(colors.size()); // Generate a random index
        return colors.get(index); // Return the color at the random index
    }

    List<Color> colorList = List.of(
            new Color(155, 144, 0),
            new Color(98, 93, 64),
            new Color(68, 151, 119),
            new Color(107, 123, 98),
            new Color(187, 183, 131),
            new Color(127, 166, 135),
            new Color(157, 184, 144));

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        bird.draw(g);
        obstacles.forEach(obstacle -> obstacle.draw(g));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
