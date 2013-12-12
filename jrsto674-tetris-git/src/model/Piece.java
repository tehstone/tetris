/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;

/**
 * This interface defines the required operations of mutable Tetris pieces.
 * 
 * @author Alan Fowler
 * @version Autumn 2013
 */
public interface Piece {

    /** Default Color used for I-Pieces. */
    Color I = new Color(255, 255, 255);

    /** Default Color used for J-Pieces. */
    Color J = new Color(171, 171, 171);

    /** Default Color used for L-Pieces. */
    Color L = new Color(85, 85, 85);

    /** Default Color used for S-Pieces. */
    Color S = new Color(213, 213, 213);

    /** Default Color used for Z-Pieces. */
    Color Z = new Color(43, 43, 43);

    /** Default Color used for O-Pieces. */
    Color O = Color.BLACK;

    /** Default Color used for T-Pieces. */
    Color T = new Color(128, 128, 128);

    /** Shifts the piece one space to the left. */
    void moveLeft();

    /** Shifts the piece one space to the right. */
    void moveRight();

    /** Shifts the piece one space down. */
    void moveDown();

    /** Rotates the piece one quarter turn clockwise. */
    void rotate();

    /** @return the x coordinate of this Piece. */
    int getX();

    /** @return the y coordinate of this Piece. */
    int getY();
}
