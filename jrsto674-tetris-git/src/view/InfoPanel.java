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

/**
 * The JPanel at the heart of Power Paint.
 * 
 * @author jrsto674
 * @version 11/16/2013
 */
@SuppressWarnings("serial")
public class InfoPanel extends JPanel {

    /** Font size for text in this panel. */
    private static final int FONT_SIZE = 18;
    
    /** Strings used in this panel. */
    private static final String[] LABELS = 
    {"Left: ", "Right: ", "Down: ", "Drop: ", 
        "Rotate: ", "Pause: ", "End Game: ", "New Game: "};
    
    /** Strings used in this panel. */
    private static final String[] KEY_VALUES = 
    {"Left Arrow", "Right Arrow", "Down Arrow", "Up Arrow", "Spacebar ", "P", "Escape", "N"};

    /**
     * Constructor for TetrisPanel.
     */
    public InfoPanel() {
        super();
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

        final Font font = new Font(Font.SANS_SERIF,
                                   Font.BOLD + Font.CENTER_BASELINE,
                                   FONT_SIZE);
        g2d.setFont(font);
        
        for (int i = LABELS.length - 1; i >= 0; i--) {
            final FontRenderContext render_context = g2d.getFontRenderContext();
            final GlyphVector glyph_vector = 
                    font.createGlyphVector(render_context, LABELS[i] + KEY_VALUES[i]);
            final Rectangle2D visual_bounds = glyph_vector.getVisualBounds().getBounds();
            
            final int x = (int) ((getWidth() - visual_bounds.getWidth()) / 2
                        - visual_bounds.getX());

            g2d.drawString(LABELS[i] + KEY_VALUES[i], x, (i + 1) * FONT_SIZE);
        }
        
        
    }
}