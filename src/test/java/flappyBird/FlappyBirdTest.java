package flappyBird;

import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class FlappyBirdTest {
    @Test
    public void createBird() {
        Bird bird = new Bird(200, 200);

        // bird should start at (0, 0)
        assertEquals(0, bird.getPx());
        assertEquals(200, bird.getPy());

        // bird should not be moving at the start
        assertEquals(0, bird.getVx());
        assertEquals(0, bird.getVy());
    }

    @Test
    public void birdVelocityUpdatesPosition() {
        Bird bird = new Bird(500, 500);

        bird.setVx(10);
        bird.setVy(10);
        assertEquals(10, bird.getVx());
        assertEquals(10, bird.getVy());

        assertEquals(0, bird.getPx());
        assertEquals(200, bird.getPy());

        bird.move();

        assertEquals(10, bird.getPx());
        assertEquals(210, bird.getPy());
    }

    @Test
    public void moveBirdOffScreen() {
        Bird bird = new Bird(200, 200);
        bird.setVx(-10);
        bird.move();
        assertEquals(0, bird.getPx());
    }

    @Test
    public void createObstacle() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        Obstacle.generateRandomObstacle(obstacles);
        assertEquals(2, obstacles.size());
    }

    @Test
    public void moveObstacle() {
        Obstacle obstacle = new Obstacle(
                200, 200, 50, 100,
                Color.BLACK, 0
        );
        obstacle.move();
        assertEquals(996, obstacle.getPx());
    }

    @Test
    public void obstacleOutOfBounds() {
        Obstacle obstacle = new Obstacle(
                200, 200, 50, 100,
                Color.BLACK, 0
        );
        obstacle.setPx(-700);
        assertTrue(obstacle.isOutOfBounds());
    }

    @Test
    public void twoObjectIntersection() {
        Bird bird = new Bird(200, 200);
        Obstacle obstacle = new Obstacle(
                200, 200, 50, 100,
                Color.BLACK, 0
        );
        assertFalse(bird.intersects(obstacle));
        obstacle.setPx(0);
        obstacle.setPy(200);
        assertTrue(bird.intersects(obstacle));
    }

    @Test
    public void isOutOfBounds() {
        Obstacle obstacle = new Obstacle(200, 200, 50, 100, Color.BLACK, 0);
        obstacle.setPx(-700);
        assertTrue(obstacle.isOutOfBounds());
        obstacle.setPx(500);
        assertFalse(obstacle.isOutOfBounds());
    }

    @Test
    public void pauseButton() {
        JButton pause = new JButton();
        JLabel score = new JLabel();
        GameDisplay court = new GameDisplay(score);
        court.pauseLabelController(pause);
        court.pauseToggler(pause);
        assertEquals("Unpause", pause.getText());
    }

    @Test
    public void removeObstacles() {
        JLabel score = new JLabel();
        GameDisplay court = new GameDisplay(score);
        ArrayList<Obstacle> obstacles = court.getObstacles();
        Obstacle.generateRandomObstacle(obstacles);
        for (Obstacle obstacle : obstacles) {
            obstacle.setPx(-100000);
            System.out.println(obstacle.getPx());
        }
        court.tick(); // generates another 2 obstacles
        assertEquals(2, obstacles.size());
    }

    @Test
    public void testReset() {
        JLabel score = new JLabel();
        GameDisplay court = new GameDisplay(score);
        court.reset(); // should generate 2 objects
        assertEquals(2, court.getObstacles().size());
    }

    @Test
    public void obstacleScoreUpdate() {
        JLabel score = new JLabel();
        GameDisplay court = new GameDisplay(score);
        Bird bird = court.getBird();
        ArrayList<Obstacle> obstacles = court.getObstacles();
        obstacles.add(new Obstacle(1, 10, 0, 0, Color.BLACK, 0));
        obstacles.add(new Obstacle(1, 10, 350, 0, Color.BLACK, 0));
        assertEquals(0, court.getScore());
        for (Obstacle obstacle : obstacles) {
            obstacle.setPx(200);
        }
        bird.setPx(250);
        court.tick();
        assertEquals(100, court.getScore());
    }

    @Test
    public void createCoin() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        Obstacle.generateRandomObstacle(obstacles);
        ArrayList<Coin> coins = new ArrayList<>();
        Coin.generateRandomCoin(coins, obstacles);
        assertEquals(5, coins.size());
        for (Coin coin : coins) {
            for (Obstacle obstacle : obstacles) {
                assertFalse(coin.intersects(obstacle));
            }
        }
    }

    @Test
    public void coinScoreUpdate() {
        JLabel score = new JLabel();
        GameDisplay court = new GameDisplay(score);
        Bird bird = court.getBird();
        ArrayList<Coin> coins = court.getCoins();
        Coin c = new Coin(10, 10);
        coins.add(c);
        bird.setPx(10);
        bird.setPy(10);
        court.tick();
        assertEquals(10, court.getScore());
    }

}
