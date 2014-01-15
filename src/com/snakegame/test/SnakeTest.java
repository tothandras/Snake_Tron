package com.snakegame.test;

import com.snakegame.Direction;
import com.snakegame.Fruits;
import com.snakegame.Snake;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class SnakeTest {

    @Test
    public void testGetScore() throws Exception {
        /** Mindenhol gyumolcs van, akarhova mozdul a kigyo, novekszik */
        Fruits fruits = new Fruits(10, 10, 100, 100 * 100);
        Snake snake = new Snake(Color.RED, fruits, 10, 10, 100);

        Assert.assertTrue(snake.getScore() == 0);
        snake.move(Direction.SOUTH);
        snake.move(Direction.WEST);
        Assert.assertTrue(snake.getScore() == 2);
    }

    @Test
    public void testMove() {
        int boardSize = 100;

        Fruits fruits = new Fruits(10, 10, boardSize, 1);
        /** Fej: (boardSize - 12, boardSize / 2) */
        Snake redSnake = new Snake(Color.RED, fruits, 10, 10, boardSize);
        /** Fej: (11, boardSize / 2) */
        Snake blueSnake = new Snake(Color.BLUE, fruits, 10, 10, boardSize);

        redSnake.move(Direction.WEST);
        Assert.assertTrue(redSnake.isBodyPart(new Point(boardSize - 13, boardSize / 2)));
        redSnake.move(Direction.SOUTH);
        Assert.assertTrue(redSnake.isBodyPart(new Point(boardSize - 13, boardSize / 2 + 1)));

        Assert.assertFalse(blueSnake.move(Direction.EAST));
        Assert.assertTrue(blueSnake.isBodyPart(new Point(12, boardSize / 2)));
        blueSnake.move(Direction.NORTH);
        Assert.assertTrue(blueSnake.isBodyPart(new Point(12, boardSize / 2 - 1)));

        Assert.assertFalse(blueSnake.isBodyPart(new Point(12, boardSize / 2 + 1)));

    }
}
