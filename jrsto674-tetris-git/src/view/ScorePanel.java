/* 
 * TCSS 305 – Autumn 2013 
 * Assignment 6 - Tetris 
 */ 
package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board;

/**
 * The JPanel at the heart of Power Paint.
 * 
 * @author jrsto674
 * @version 11/16/2013
 */
@SuppressWarnings("serial")
public class ScorePanel extends JPanel {

    /** Font size for text in this panel. */
    private static final int FONT_SIZE = 18;
    
    /** Lines to clear per level. */
    private static final int LINES_PER_LEVEL = 10;
    
    /** Points awarded per line cleared. */
    private static final int POINTS_PER_LINE = 10;
    
    /** String array containing the info for this panel. */
    private static final String[] SCORE_INFO = {"10 per line cleared.", 
        "50 for clearing 4 lines.", 
        "100 for level up.", 
        "Score: ", 
        "Level: ", 
        "Lines to clear: "};

    /** Lines to be cleared simultaneously for score bonus. */
    private static final int TETRIS = 4;
    
    /** Speed change of timer per level in milliseconds. */
    private static final int DELAY_CHANGE = 75;
    
    /** Points awarded each level up. */
    private static final int LEVEL_BONUS = 100;
    
    /** Instance of Board which this panel represents. */
    private Board myBoard;
    
    /** Instance of Timer controlling speed of game this panel represents. */
    private Timer myTimer;
    
    /** Current speed of myTimer, in milliseconds. */
    private int myTimerSpeed;
    
    /** Current score of current game. */
    private int myScore;
    
    /** Current level of current game. */
    private int myLevel;
    
    /** Total number of lines cleared. */
    private int myLines;
    
    /** Number of lines with at least one frozen block on the Board. */
    private int myBoardSize;

    /**
     * Constructor for TetrisPanel.
     * 
     * @param theBoard Board object representing current game.
     * @param theTimer Timer object for current game.
     * @param theTimerSpeed Initial speed of theTimer.
     */
    public ScorePanel(final Board theBoard, final Timer theTimer, final int theTimerSpeed) {
        super();
        myBoard = theBoard;
        myTimer = theTimer;
        myTimerSpeed = theTimerSpeed;
        myScore = 0;
        myLevel = 1;
        myLines = 0;
        myBoardSize = 0;
    }



    /**
     * Draws all the necessary elements on the panel whenever
     * repaint is called elsewhere. First draws the grid, if
     * enabled, then the tempShape if any (tempShape is what
     * is drawn if the user is still dragging the mouse),
     * then iterates through the ArrayList of stored shapes
     * and draws each one using the appropriate color and
     * brush thickness.
     * 
     * @param theGraphics The Graphics object on which all other
     * elements will be drawn.
     */
    @Override 
    public void paintComponent(final Graphics theGraphics) { 
        checkState();
        
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        final Font font = new Font(Font.SANS_SERIF,
                                   Font.BOLD + Font.CENTER_BASELINE,
                                   FONT_SIZE);
        g2d.setFont(font);
        
        for (int i = 0; i <= 2; i++) {
            final FontRenderContext render_context = g2d.getFontRenderContext();
            final GlyphVector glyph_vector = 
                    font.createGlyphVector(render_context, SCORE_INFO[i]);
            final Rectangle2D visual_bounds = glyph_vector.getVisualBounds().getBounds();
            
            final int x = (int) ((getWidth() - visual_bounds.getWidth()) / 2
                        - visual_bounds.getX());

            g2d.drawString(SCORE_INFO[i], x, (i + 1) * FONT_SIZE);
        }
        
        int i = 2 + 1;
        final FontRenderContext render_context = g2d.getFontRenderContext();
        GlyphVector glyph_vector = 
                font.createGlyphVector(render_context, SCORE_INFO[i] + myScore);
        Rectangle2D visual_bounds = glyph_vector.getVisualBounds().getBounds();
        
        int x = (int) ((getWidth() - visual_bounds.getWidth()) / 2
                    - visual_bounds.getX());

        g2d.drawString(SCORE_INFO[i] + myScore, x, (i++ + 1) * FONT_SIZE);
                
        glyph_vector = 
                font.createGlyphVector(render_context, SCORE_INFO[i] + myLevel);
        visual_bounds = glyph_vector.getVisualBounds().getBounds();
        
        x = (int) ((getWidth() - visual_bounds.getWidth()) / 2
                    - visual_bounds.getX());

        g2d.drawString(SCORE_INFO[i] + myLevel, x, (i++ + 1) * FONT_SIZE);

        
        final int toClear = LINES_PER_LEVEL - (myLines % LINES_PER_LEVEL);
        glyph_vector = 
                font.createGlyphVector(render_context, SCORE_INFO[i] + toClear);
        visual_bounds = glyph_vector.getVisualBounds().getBounds();
        
        x = (int) ((getWidth() - visual_bounds.getWidth()) / 2
                    - visual_bounds.getX());

        g2d.drawString(SCORE_INFO[i] + toClear, x, (i++ + 1) * FONT_SIZE);
    }
    
    /**
     * Checks the current state of the board, 
     * awarding points and incrementing level
     * as necessary.
     */
    private void checkState() {
        final int newBoardSize = myBoard.getFrozenBlocks().size();
        if (newBoardSize > myBoardSize) {
            myBoardSize = newBoardSize;
        } else {
            final int cleared = myBoardSize - newBoardSize;
            myScore += cleared * POINTS_PER_LINE;
            if (cleared == TETRIS) {
                myScore += POINTS_PER_LINE;
            }
            myLines += cleared;
            if ((myLines / LINES_PER_LEVEL) + 1 > myLevel) {
                myLevel++;
                myTimer.setDelay(myTimerSpeed - DELAY_CHANGE);
                myScore += LEVEL_BONUS;
            }
            
            myBoardSize = newBoardSize;
        }
    }
    
    /** Resets this panel when a new game is started. */
    public void reset() {
        myLevel = 1;
        myLines = 0;
        myScore = 0;
        myBoardSize = 0;
    }
}