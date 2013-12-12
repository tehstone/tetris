/* 
 * TCSS 305 – Autumn 2013 
 * Assignment 6 - Tetris 
 */ 
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board;

/**
 * Primary class running the GUI for a Tetris game.
 * 
 * @author jrsto674
 * @version 11/16/2013
 */
public class TetrisGUI implements Observer {

    /** Size of a block, in pixels. */
    private static final int BLOCK_SIZE = 25;

    /** Default size of the JFrame in pixels. */
    private static final Dimension DEFAULT_SIZE = new Dimension(500, 560);

    /** Minimum size of the JFrame in pixels. */
    private static final Dimension MINIMUM_SIZE = new Dimension(500, 560);

    /** Base width of the subpanels in the frame. */
    private static final int SUBPANEL_WIDTH = 100;

    /** Base width of the score panel. */
    private static final int SCORE_PANEL_HEIGHT = 115;

    /** Base width of the info panel. */
    private static final int INFO_PANEL_HEIGHT = 150;

    /** Padding between Panels on the EastBox. */
    private static final int PADDING = 10;

    /** Default delay between Timer events, in milliseconds. */
    private static final int DEFAULT_TIMER_SPEED = 800;

    /** Background color for the game panel and next piece panel. */
    private static final Color PANEL_COLOR = new Color(255, 96, 96);
    
    /** Width of a game board, in blocks. */
    private static final int BOARD_WIDTH = 10;
    
    /** Height of a game board, in blocks. */
    private static final int BOARD_HEIGHT = 20;

    /** JFrame that contains the Tetris game. */
    private JFrame myFrame;

    /** Tetris Board that handles all the backend of a Tetris game. */
    private Board myBoard;

    /** Panel on which the game is played. */
    private JPanel myGamePanel;

    /** Panel displaying the next piece. */
    private JPanel myNextPiecePanel;

    /** Panel displaying the current score and other related info. */
    private JPanel myScorePanel;

    /** Panel displaying key commands. */
    private JPanel myInfoPanel;

    /** A Timer that generates the TimerEvents that run the game. */
    private Timer myTimer;

    /** A toggle for game state. */
    private boolean myGamePaused;

    /** A toggle for game state. */
    private boolean myGameOver;

    /** Player that plays the theme song. */
    private Player myPlayer;

    /**
     * Constructor for TetrisGUI, uses a helper class to initialize all fields.
     */
    public TetrisGUI() {
        initializeFields();
    }

    /** 
     * Helper class for the constructor, initializes all fields and
     * attaches a KeyAdapter to the frame. 
     */
    private void initializeFields() {
        myFrame = new JFrame();
        myBoard = new Board();
        myBoard.addObserver(this);
        myTimer = new Timer(DEFAULT_TIMER_SPEED, new TimerListener(myBoard));
        myGamePanel = new TetrisPanel(myBoard);
        myNextPiecePanel = new NextPiecePanel(myBoard);
        myScorePanel = new ScorePanel(myBoard, myTimer, DEFAULT_TIMER_SPEED);
        myInfoPanel = new InfoPanel();
        myGamePaused = false;
        myGameOver = false;
        myPlayer = null;

        final MyKeyAdapter handler = new MyKeyAdapter();
        myFrame.addKeyListener(handler);
    }

    /** 
     * Sets up the two portions of the border layout in the JFrame
     * as well as the frame itself before starting the game.
     */
    public void start() {
        setupCenter();
        setupEast();

        myFrame.setTitle("TCSS 305 Tetris");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLocationByPlatform(true);
        myFrame.pack();
        myFrame.setMinimumSize(MINIMUM_SIZE);
        myFrame.setSize(DEFAULT_SIZE);
        myFrame.setVisible(true);

        myTimer.start();
        startMusic();
    }

    /** Builds the center panel which holds the game itself. */
    public void setupCenter() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        myGamePanel.setBackground(PANEL_COLOR);
        myGamePanel.setPreferredSize(
                                     new Dimension(myBoard.getWidth() * BLOCK_SIZE,
                                                   myBoard.getHeight() * BLOCK_SIZE));
        centerPanel.add(myGamePanel);
        myFrame.add(centerPanel);
    }

    /**
     * Sets up the east panel, which holds the next piece,
     * info, and score panels.
     */
    public void setupEast() {
        final JPanel eastPanel = new JPanel();
        eastPanel.setBackground(Color.BLACK);
        final Box eastBox = new Box(BoxLayout.PAGE_AXIS);
        myNextPiecePanel.setBackground(PANEL_COLOR);
        myNextPiecePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        myNextPiecePanel.setPreferredSize(
                                          new Dimension(SUBPANEL_WIDTH * 2,
                                                        SUBPANEL_WIDTH));
        eastBox.add(myNextPiecePanel);
        eastBox.add(Box.createVerticalStrut(PADDING * PADDING));
        myScorePanel.setBackground(Color.WHITE);
        myScorePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        myScorePanel.setPreferredSize(new Dimension(SUBPANEL_WIDTH, SCORE_PANEL_HEIGHT));
        eastBox.add(myScorePanel);
        eastBox.add(Box.createVerticalStrut(PADDING));
        myInfoPanel.setBackground(Color.WHITE);
        myInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        myInfoPanel.setPreferredSize(new Dimension(SUBPANEL_WIDTH, INFO_PANEL_HEIGHT));
        eastBox.add(myInfoPanel);
        eastPanel.add(eastBox);
        myFrame.add(eastPanel, BorderLayout.EAST);
    }

    @Override
    public void update(final Observable theObj, final Object theArg) {
        myGamePanel.repaint(); 
        myNextPiecePanel.repaint();
        myScorePanel.repaint();
        myGameOver = myBoard.isGameOver();
    }
    
    /** Helper method to start the music. */
    private void startMusic() {
        final Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
        final Format input2 = new AudioFormat(AudioFormat.MPEG);
        final Format output = new AudioFormat(AudioFormat.LINEAR);
        PlugInManager.addPlugIn(
                                "com.sun.media.codec.audio.mp3.JavaDecoder",
                                new Format[]{input1, input2},
                                new Format[]{output},
                                PlugInManager.CODEC
        );
        try {
            final File f = new File("support_files//tetris.mp3");
            myPlayer = Manager.createPlayer(new MediaLocator(f.toURI().toURL()));
        } catch (final NoPlayerException | IOException e) {
            e.printStackTrace();
        }
        if (myPlayer != null) {
            myPlayer.start();
        }
    }

    // Inner Class

    /**
     * Listens for mouse events relating to this panel.
     * @author jrsto674
     * @version 11/06/2013
     */
    public class MyKeyAdapter extends KeyAdapter {

        /** Constructor for this mouse adapter. */
        public MyKeyAdapter() {
            super();
        }

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (theEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                ((TetrisPanel) myGamePanel).setGameOver();
                myGameOver = true;
                myGamePaused = true;
                myGamePanel.repaint();
                myTimer.stop();
            }
            if (theEvent.getKeyCode() == KeyEvent.VK_P) {
                if (myGamePaused) {
                    myTimer.start();
                    myGamePaused = !myGamePaused;
                    ((TetrisPanel) myGamePanel).togglePaused();
                } else {
                    myTimer.stop();
                    myGamePaused = !myGamePaused;
                    ((TetrisPanel) myGamePanel).togglePaused();
                }
            }
            if (!myGamePaused) {
                switch (theEvent.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        myBoard.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        myBoard.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        myBoard.moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        myBoard.hardDrop();
                        break;
                    case KeyEvent.VK_R:
                        myBoard.rotate();
                        break;
                    case KeyEvent.VK_SPACE:
                        myBoard.rotate();
                        break;
                    default:
                        break;
                }
            }
            if (myGameOver && theEvent.getKeyCode() == KeyEvent.VK_N) {
                myBoard.newGame(BOARD_WIDTH, BOARD_HEIGHT, null);
                ((TetrisPanel) myGamePanel).reset();
                ((ScorePanel) myScorePanel).reset();
                myTimer.start();
                myGamePaused = false;
                myPlayer.stop();
                startMusic();
            }
            //System.out.println(myBoard.toString());
        }
    }
}
