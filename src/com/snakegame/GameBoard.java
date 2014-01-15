package com.snakegame;

import java.awt.*;

/**
 * Jatektabla
 */
public class GameBoard implements GameElement{
    private int width;
    private int height;
    private int boardsize;

    public GameBoard(int width, int height, int boardsize) {
        this.width = width;
        this.height = height;
        this.boardsize = boardsize;
    }

    /**
     * Palya kirajzolasa.
     * @param g mire kell kirajzolni?
     */
    public void draw(Graphics g) {
        Color currentColor = g.getColor();

        g.setColor(Color.LIGHT_GRAY);

        int lineHeight = height/boardsize;
        for (int i=0; i<boardsize; ++i) {
            g.drawLine(0, i*lineHeight, width, i*lineHeight);
        }

        int lineWidth = width/boardsize;
        for (int i=0; i<boardsize; ++i) {
            g.drawLine(i*lineWidth, 0, i*lineWidth, height);
        }

        g.setColor(currentColor);
    }
}
