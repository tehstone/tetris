/* 
 * TCSS 305 – Autumn 2013 
 * Assignment 6 - Tetris 
 */ 
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Board;

/**
 * Listener that handles Timer events and alerts the Board
 * that changes should be made.
 * 
 * @author jrsto674
 * @version 11/16/2013
 */
public class TimerListener implements ActionListener {

    /** The Tetris Board that this Timer represents. */
    private Board myBoard;
    
    /**
     * Constructor for this TimerListener.
     * 
     *  @param theBoard The Tetris Board this timer will
     *  be attached to.
     */
    public TimerListener(final Board theBoard) {
        super();
        myBoard = theBoard;
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myBoard.step();
    }
}
