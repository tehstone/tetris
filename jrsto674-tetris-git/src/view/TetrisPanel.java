/* 
 * TCSS 305 – Autumn 2013 
 * Assignment 6 - Tetris 
 */ 
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPanel;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;

/**
 * The JPanel at the heart of Power Paint.
 * 
 * @author jrsto674
 * @version 11/01/2013
 */
@SuppressWarnings("serial")
public class TetrisPanel extends JPanel {

    /** Size of each block in pixels.*/
    private static final int BLOCK_SIZE = 25;

    /** Empty String. */
    private static final String NO_BLOCK = " ";

    /** Font Size for Paused and Game Over messages.  */
    private static final int FONT_SIZE = 38;

    /** Padding to place around text displays. */
    private static final int TEXT_PADDING = 5;

    /** Larger padding to place around text displays. */
    private static final int TEXT_PADDING2 = 15;
    
    /** String used for determining type of rectangle to draw. */
    private static final String FILL = "fill";
    
    /** String used for determining type of rectangle to draw. */
    private static final String DRAW = "draw";

    /** Board object that this panel uses. */
    private Board myBoard;

    /** Current game state. */
    private boolean myGamePaused;

    /** Current game state.  */
    private boolean myGameOver;

    /**
     * Constructor for TetrisPanel.
     * 
     * @param theBoard The Tetris Board whose elements will be
     * displayed on this panel.
     */
    public TetrisPanel(final Board theBoard) {
        super();
        myBoard = theBoard;
        myGamePaused = false;
        myGameOver = false;
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
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        final int boardHeight = myBoard.getHeight();
        final int boardWidth = myBoard.getWidth();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        if (myBoard.isGameOver()) {
            myGamePaused = true;
            myGameOver = true;
        }
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < Math.max(boardHeight, boardWidth); i++) {
            final int spacing = i * BLOCK_SIZE;
            g2d.draw(new Line2D.Double(0, spacing, boardWidth * BLOCK_SIZE, spacing));
            g2d.draw(new Line2D.Double(spacing, 0, spacing, boardHeight * BLOCK_SIZE));
        }
        final Piece current = (AbstractPiece) myBoard.getCurrentPiece();
        final int[][] currentPiece = ((AbstractPiece) current).getBoardCoordinates();
        for (int row = 0; row < boardHeight; row++) {
            for (int column = 0; column < boardWidth; column++) {
                for (int block = 0; block < currentPiece.length; block++) {
                    if (currentPiece[block][1] == row 
                            && currentPiece[block][0] == column) {
                        
                        g2d.setColor(Color.WHITE);
                        makeRect(g2d, column * BLOCK_SIZE,
                                 (boardHeight * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE), 
                                 BLOCK_SIZE, BLOCK_SIZE, FILL);
                        
                        g2d.setColor(((AbstractPiece) current).getBlock().getColor());
                        makeRect(g2d, column * BLOCK_SIZE,
                                 (boardHeight * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE), 
                                 BLOCK_SIZE - 1, BLOCK_SIZE - 1, FILL);
                        
                        g2d.setColor(Color.black);
                        makeRect(g2d, column * BLOCK_SIZE,
                                 (boardHeight * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE), 
                                 BLOCK_SIZE, BLOCK_SIZE, DRAW);
                    }
                }
            }
        }
        final List<Block[]> rowBlocks = myBoard.getFrozenBlocks();
        for (int row = 0; row < rowBlocks.size(); row++) {
            for (int column = 0; column < boardWidth; column++) {
                final Block block = rowBlocks.get(row)[column];
                if (!NO_BLOCK.equals(block.toString())) {
                    
                    g2d.setColor(Color.white);
                    makeRect(g2d, column * BLOCK_SIZE,
                             (boardHeight * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE), 
                             BLOCK_SIZE, BLOCK_SIZE, FILL);
                    
                    g2d.setColor(block.getColor());
                    makeRect(g2d, column * BLOCK_SIZE,
                             (boardHeight * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE), 
                             BLOCK_SIZE - 1, BLOCK_SIZE - 1, FILL);

                    g2d.setColor(Color.black);
                    makeRect(g2d, column * BLOCK_SIZE,
                             (boardHeight * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE), 
                             BLOCK_SIZE, BLOCK_SIZE, DRAW);
                }
            }
        }

        if (myGamePaused) {
            String pauseText = "Game Paused";
            if (myGameOver) {
                pauseText = "Game Over";
            }
            final Font font = new Font(Font.SANS_SERIF,
                                       Font.BOLD + Font.CENTER_BASELINE,
                                       FONT_SIZE);
            g2d.setFont(font);

            final FontRenderContext render_context = g2d.getFontRenderContext();
            final GlyphVector glyph_vector = font.createGlyphVector(render_context, pauseText);
            final Rectangle2D visual_bounds = glyph_vector.getVisualBounds().getBounds();

            final int x = (int) ((getWidth() - visual_bounds.getWidth()) / 2
                    - visual_bounds.getX());
            final int y = (int) ((getHeight() - visual_bounds.getHeight()) / 2
                    - visual_bounds.getY());

            final Rectangle2D rect = 
                    new Rectangle2D.Double(x - TEXT_PADDING,
                                           y - visual_bounds.getHeight() - TEXT_PADDING,
                                           visual_bounds.getWidth() + TEXT_PADDING2,
                                           visual_bounds.getHeight() + TEXT_PADDING2);
            g2d.setColor(Color.white);
            g2d.fill(rect);
            g2d.setColor(Color.black);
            g2d.drawString(pauseText, x, y);
        }
    }
    
    /**
     * Helper method for PaintComponent, draws most of its rectangles.
     * 
     *  @param theG2d The Graphics object this rectangle will be drawn to.
     *  @param theX The X coordinate of this rectangle.
     *  @param theY The Y coordinate of this rectangle.
     *  @param theWidth The width of this rectangle.
     *  @param theHeight The height of this rectangle.
     *  @param theType Either "fill" or "draw" depending on how the
     *  rectangle should be drawn.
     */
    private void makeRect(final Graphics2D theG2d, final double theX, 
                          final double theY, final double theWidth, 
                          final double theHeight, final String theType) {
        final Rectangle2D rect = 
                new Rectangle2D.Double(theX, theY, theWidth, theHeight);
        if (FILL.equals(theType)) {
            theG2d.fill(rect);
        } else {
            theG2d.draw(rect);
        }
    }

    /** Sets game over state to true. */
    public void setGameOver() {
        myGamePaused = true;
        myGameOver = true;
    }
    
    /** Toggles game state. */
    public void togglePaused() {
        myGamePaused = !myGamePaused;
        repaint();
    }
    
    /** Resets the panel for a new game. */
    public void reset() {
        myGamePaused = false;
        myGameOver = false;
    }

}

