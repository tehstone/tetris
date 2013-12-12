/* 
 * TCSS 305 – Autumn 2013 
 * Assignment 6 - Tetris 
 */ 
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;

/**
 * The JPanel at the heart of Power Paint.
 * 
 * @author jrsto674
 * @version 11/16/2013
 */
@SuppressWarnings("serial")
public class NextPiecePanel extends JPanel {

    /** Size of each block, in pixels. */
    private static final int BLOCK_SIZE = 25;
    
    /** Height of this panel, in blocks. */
    private static final int PANEL_HEIGHT = 4;
    
    /** Height of this panel, in blocks. */
    private static final int PANEL_WIDTH = 8;

    /** The Tetris board that this panel represents. */
    private Board myBoard;

    /**
     * Constructor for TetrisPanel.
     * 
     * @param theBoard The Tetris Board whose next piece will be
     * displayed on this panel.
     */
    public NextPiecePanel(final Board theBoard) {
        super();
        myBoard = theBoard;
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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < PANEL_HEIGHT; i++) {
            final int spacing = i * BLOCK_SIZE;
            final Line2D line = 
                    new Line2D.Double(0, spacing, PANEL_WIDTH * BLOCK_SIZE, spacing);
            g2d.draw(line);
        }
        for (int i = 0; i < PANEL_WIDTH; i++) {
            final int spacing = i * BLOCK_SIZE;
            final Line2D line = 
                    new Line2D.Double(spacing, 0, spacing, PANEL_HEIGHT * BLOCK_SIZE);
            g2d.draw(line);
        }
        
        final Piece piece = myBoard.getNextPiece();

        final int[][] currentPiece = ((AbstractPiece) piece).getRotation();
        final int pieceSize = 4;
        for (int row = 0; row < pieceSize; row++) {
            for (int column = 0; column < pieceSize; column++) {
                int drawColumn = column;
                for (int block = 0; block < currentPiece.length; block++) {
                    if (currentPiece[block][1] == row 
                            && currentPiece[block][0] == column) {
                        
                        if (((AbstractPiece) piece).getBlock().equals(Block.I)) {
                            drawColumn--;
                        }
                        
                        g2d.setColor(Color.WHITE);
                        final Rectangle2D rect = 
                                new Rectangle2D.Double((pieceSize * BLOCK_SIZE) 
                                                       - (drawColumn * BLOCK_SIZE), 
                                                       (pieceSize * BLOCK_SIZE) 
                                                       - ((row + 1) * BLOCK_SIZE),
                                                       BLOCK_SIZE, 
                                                       BLOCK_SIZE);
                        g2d.fill(rect);
                        
                        g2d.setColor(((AbstractPiece) piece).getBlock().getColor());
                        rect.setRect((pieceSize * BLOCK_SIZE) 
                                     - (drawColumn * BLOCK_SIZE), 
                                     (pieceSize * BLOCK_SIZE) 
                                     - ((row + 1) * BLOCK_SIZE),
                                     BLOCK_SIZE - 1, 
                                     BLOCK_SIZE - 1);
                        g2d.fill(rect);
                        
                        g2d.setColor(Color.BLACK);
                        rect.setRect((pieceSize * BLOCK_SIZE) 
                                     - (drawColumn * BLOCK_SIZE), 
                                     (pieceSize * BLOCK_SIZE) - ((row + 1) * BLOCK_SIZE),
                                     BLOCK_SIZE, 
                                     BLOCK_SIZE);
                        g2d.draw(rect);
                    }
                }
            }
        }
    }
}