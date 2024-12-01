package flappyBird;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
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
    private int id = 0;
    private int lastObstacle = -1;
    private int tickCounter = 0;
    private boolean isGameOver = false;

    // Game constants
    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 400;
    public static final int BIRD_VELOCITY_X = 10;
    public static final int BIRD_VELOCITY_Y = 5;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 20;

    //SETTERS --------------------------------------------------------------------
    public void setBird(Bird bird) {
        this.bird = bird;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles.addAll(obstacles);
    }

    public void setTickCounter(int tickCounter) {
        this.tickCounter = tickCounter;
    }
    //-----------------------------------------------------------------------------

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
        obstacles.add(new Obstacle(2000, COURT_HEIGHT, width, 800, posY, velY, color, id));
        obstacles.add(new Obstacle(2000, COURT_HEIGHT, width, 800,
                posY + 800 + gap, velY, color, id));
        id++;
    }
    
    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */

    void tick() {
        if (playing && !isGameOver) {
            bird.move();
            scoreBoard.setText("Score: " + score);

            if (obstacles.size() > 0) {
                Obstacle lastOb = obstacles.get(obstacles.size() - 1);
                if (COURT_WIDTH - lastOb.getPx() - lastOb.getWidth() > 200) {
                    generateRandomObstacle();
                }
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
                    isGameOver = true;
                    break;
                }
                if (o.getPx() + o.getWidth() < bird.getPx()) {
                    if (lastObstacle < o.getId()) {    
                        score += 100;
                        lastObstacle = o.getId();
                    }
                }
                if (o.isOutOfBounds()) {
                    obstacleIterator.remove();
                }
            }
        }
        repaint();
        requestFocusInWindow();
        tickCounter++;
    }

    public void reset() {
        bird = new Bird(COURT_WIDTH, COURT_HEIGHT);
        obstacles = new ArrayList<>();
        generateRandomObstacle();
        playing = true;
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        score = 0;
        tickCounter = 0;
        isGameOver = false;
    }

    public void pauseLabelController(JButton button) {
        if (!playing) {
            button.setText("Unpause");
        }
        else {
            button.setText("Pause");
        }
    }

    public void pauseToggler(JButton button) {
        playing = !playing;
        pauseLabelController(button);
    }

    public void saveGame() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("gameState.txt"));
            writer.write("Bird: " + bird.toString() + "\n");
            writer.write("Score: " + score + "\n");
            writer.write("TickCounter: " + tickCounter + "\n");
            writer.write("Playing: " + playing + "\n");
            writer.write("GameOver: " + isGameOver + "\n");
            writer.write("Obstacles: " + obstacles.size() + "\n");
            for (Obstacle obstacle : obstacles) {
                writer.write(obstacle.toString() + "\n");
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error saving game" + e.getMessage());
        }
    }

    private static Color parseColor(String colorString) {
        String[] colors = colorString.split(",");
        int red = Integer.parseInt(colors[0].split("=")[1]);
        int green = Integer.parseInt(colors[1].split("=")[1]);
        String b = colors[2].split("=")[1];
        b = b.substring(0, b.length() - 1);
        int blue = Integer.parseInt(b);
        return new Color(red, green, blue);
    }

    public void loadGame() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("gameState.txt"));
            String birdPosition = reader.readLine();
            if (!birdPosition.startsWith("Bird: ")) {
                throw new IllegalArgumentException("Not a valid file");
            }
            birdPosition = birdPosition.substring("Bird: ".length());
            String[] birdPos = birdPosition.split(", ");
            int birdX = Integer.parseInt(birdPos[0]);
            int birdY = Integer.parseInt(birdPos[1]);
            Bird bird = new Bird(COURT_WIDTH, COURT_HEIGHT);
            bird.setPx(birdX);
            bird.setPy(birdY);

            String score = reader.readLine();
            if (!score.startsWith("Score: ")) {
                throw new IllegalArgumentException("Not a valid file");
            }
            score = score.substring("Score: ".length());
            int score1 = Integer.parseInt(score);

            String tickCount = reader.readLine();
            if (!tickCount.startsWith("TickCounter: ")) {
                throw new IllegalArgumentException("Not a valid file");
            }
            tickCount = tickCount.substring("TickCounter: ".length());
            int tick = Integer.parseInt(tickCount);

            String playing = reader.readLine();
            if (!playing.startsWith("Playing: ")) {
                throw new IllegalArgumentException("Not a valid file");
            }
            playing = playing.substring("Playing: ".length());
            if (playing.equals("true")) {
                this.playing = true;
            }
            else {
                this.playing = false;
            }
            System.out.println("isplaying = " + playing);
            System.out.println("this isplaying = " + this.playing);

            String gameOver = reader.readLine();
            if (!gameOver.startsWith("GameOver: ")) {
                throw new IllegalArgumentException("Not a valid file");
            }
            gameOver = gameOver.substring("GameOver: ".length());
            if (gameOver.equals("true")) {
                this.isGameOver = true;
            }
            else {
                this.isGameOver = false;
            }

            String obstacleCount = reader.readLine();
            if (!obstacleCount.startsWith("Obstacles: ")) {
                throw new IllegalArgumentException("Not a valid file");
            }
            obstacleCount = obstacleCount.substring("Obstacles: ".length());
            int count = Integer.parseInt(obstacleCount);
            ArrayList<Obstacle> obstacleList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String obstacle = reader.readLine();
                String[] fields = obstacle.split(", ");
                int obstacleX = Integer.parseInt(fields[0]);
                int obstacleY = Integer.parseInt(fields[1]);
                int obstacleWidth = Integer.parseInt(fields[2]);
                int obstacleHeight = Integer.parseInt(fields[3]);
                int obstacleVelY = Integer.parseInt(fields[4]);
                Color color = parseColor(fields[5]);
                int obstacleId = Integer.parseInt(fields[6]);
                Obstacle o = new Obstacle(COURT_WIDTH, COURT_HEIGHT, obstacleWidth, obstacleHeight,
                        obstacleY, obstacleVelY, color, obstacleId);
                o.setPx(obstacleX);
                obstacleList.add(o);
            }
            this.setBird(bird);
            this.setObstacles(obstacleList);
            this.setScore(score1);
            this.setTickCounter(tick);
            scoreBoard.setText("Score: " + score);
        }
        catch (IOException e) {
            throw new RuntimeException("Error loading game" + e.getMessage());
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
