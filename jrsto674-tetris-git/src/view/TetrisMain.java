/* 
 * TCSS 305 – Autumn 2013 
 * Assignment 6 - Tetris 
 */ 
package view;

/**
 * Main class of the Tetris program, starts up the GUI.
 * @author jrsto674
 * @version 11/16/2013
 */
public final class TetrisMain {

    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * Creates a new TetrisGUI and calls its start() method.
     * @param theArgs Possible command line arguments.
     */
    public static void main(final String... theArgs) {
        new TetrisGUI().start();

    }

}
