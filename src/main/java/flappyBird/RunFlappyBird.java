package flappyBird;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RunFlappyBird implements Runnable {

    public static void applyFontToComponents(Font font, JComponent... components) {
        for (JComponent component : components) {
            component.setFont(font);
        }
    }

    public void run() {
        AudioPlayer.playMusic("files/music.wav");

        Color bgColor = new Color(226, 231, 224);
        Color sideColor = new Color(150, 168, 157);
        Font customFont = new Font("Arial", Font.PLAIN, 20);

        //font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("files/retrograde.otf"));
            customFont = customFont.deriveFont(15f); // Set font size
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        catch (FontFormatException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }


        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Flappy Bird");
        frame.setLocation(300, 300);
        frame.setBackground(bgColor);

        // bottom panel with score
        final JPanel score_panel = new JPanel();
        frame.add(score_panel, BorderLayout.SOUTH);
        score_panel.setBackground(sideColor);
        final JLabel scoreBoard = new JLabel();
        score_panel.add(scoreBoard);


        // Main playing area
        final GameDisplay court = new GameDisplay(scoreBoard);
        frame.add(court, BorderLayout.CENTER);
        court.setBackground(bgColor);

        //Top panel with pause and reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        control_panel.setBackground(sideColor);

        // Pause button
        final JButton pause = new JButton("Pause");
        pause.addActionListener(e -> {court.pauseToggler(pause) ;
            AudioPlayer.playEffect("files/press.wav");});
        pause.addKeyListener(new KeyAdapter()  {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                    court.pauseToggler(pause); }
        });
        control_panel.add(pause);

        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> { court.reset(); court.pauseLabelController(pause);
            AudioPlayer.playEffect("files/press.wav");});
        control_panel.add(reset);

        final JButton quit = new JButton("Save & Quit");
        quit.addActionListener(e -> { AudioPlayer.playEffect("files/press.wav");
            court.saveGame() ; System.exit(0);});
        control_panel.add(quit);

        applyFontToComponents(customFont, scoreBoard, reset, pause, quit);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    court.loadGame();
                    court.pauseLabelController(pause);
                }
                catch (RuntimeException e1) {
                    court.reset();
                }
            }
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    court.saveGame();
                }
                catch (RuntimeException e1) {
                    court.reset();
                }
            }
        });

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
