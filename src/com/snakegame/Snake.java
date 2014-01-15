package com.snakegame;

import java.awt.*;
import java.util.LinkedList;

/**
 * Kigyo
 */
public class Snake implements GameElement {

    private int width;
    private int height;
    private int boardSize;
    private LinkedList<Point> body;
    private Snake otherSnake;
    private boolean WALLS = false;

    private Color color;
    private Fruits fruits;
    private Direction direction;

    public Snake(Color color, Fruits fruits, int width, int height, int boardSize) {
        this.boardSize = boardSize;
        this.width = width / boardSize;
        this.height = height / boardSize;
        this.color = color;
        this.fruits = fruits;

        body = new LinkedList<Point>();
        if (color == Color.RED) {
            direction = Direction.WEST;
            body.addFirst(new Point(boardSize - 10, boardSize / 2));
            body.addFirst(new Point(boardSize - 11, boardSize / 2));
            /** Fej */
            body.addFirst(new Point(boardSize - 12, boardSize / 2));
        } else if (color == Color.BLUE) {
            direction = Direction.EAST;
            body.addFirst(new Point(9, boardSize / 2));
            body.addFirst(new Point(10, boardSize / 2));
            /** Fej */
            body.addFirst(new Point(11, boardSize / 2));
        }
    }

    public void setOtherSnake(Snake otherSnake) {
        this.otherSnake = otherSnake;
    }

    /**
     * Kigyo jelenlegi hossza - kezdeti hossz
     * @return pontszam
     */
    public int getScore() {
        return body.size() - 3;
    }

    /**
     * Kigyo mozgatasa.
     * @param moveDirection mozgatas iranya
     * @return tudott-e az adott iranyba mozogni
     */
    public boolean move(Direction moveDirection) {
        Point head = body.peekFirst();
        Point newPoint = null;

        switch (moveDirection) {
            case NORTH:
                if (direction != Direction.SOUTH) {
                    direction = Direction.NORTH;
                    newPoint = new Point(head.x, head.y - 1);
                } else newPoint = new Point(head.x, head.y + 1);
                break;
            case EAST:
                if (direction != Direction.WEST) {
                    direction = Direction.EAST;
                    newPoint = new Point(head.x + 1, head.y);
                } else newPoint = new Point(head.x - 1, head.y);
                break;
            case SOUTH:
                if (direction != Direction.NORTH) {
                    direction = Direction.SOUTH;
                    newPoint = new Point(head.x, head.y + 1);
                } else newPoint = new Point(head.x, head.y - 1);
                break;
            case WEST:
                if (direction != Direction.EAST) {
                    direction = Direction.WEST;
                    newPoint = new Point(head.x - 1, head.y);
                } else newPoint = new Point(head.x + 1, head.y);
                break;
            case NONE:
            default:
                break;
        }

        if (moveDirection != Direction.NONE) {

            if (!WALLS) {
                if (newPoint.x < 0) newPoint.x = boardSize - 1;
                if (newPoint.y < 0) newPoint.y = boardSize - 1;
                if (newPoint.x > boardSize - 1) newPoint.x = 0;
                if (newPoint.y > boardSize - 1) newPoint.y = 0;
            }

            if (!WALLS && isBodyPart(newPoint) || (otherSnake != null && otherSnake.isBodyPart(newPoint))) {
                return true;
            } else if (WALLS && isBodyPart(newPoint) || (otherSnake != null && otherSnake.isBodyPart(newPoint)) || newPoint.x < 0 || newPoint.y < 0 || newPoint.x > boardSize - 1 || newPoint.y > boardSize - 1) {
                return true;
            } else if (!fruits.getFruits().contains(newPoint)) {
                body.remove(body.peekLast());
            } else {
                fruits.removeFruit(newPoint);
            }

            body.addFirst(newPoint);
        }

        return false;
    }

    /**
     * Kigyo kirajzolasa
     * @param g mire kell kirajzolni?
     */
    @Override
    public void draw(Graphics g) {
        Color currentColor = g.getColor();
        g.setColor(color);
        for (Point point : body) {
            g.fillRect(point.x * width, point.y * height, width, height);
        }
        g.setColor(currentColor);
    }

    /**
     * Az adott ponton van-e testresz.
     * @param point adott pont a palyan
     * @return van-e testresz az adott ponton.
     */
    public boolean isBodyPart(Point point) {
        return body.contains(point);
    }

    /**
     * Vannak falak?
     * @param walls falak?
     */
    public void setWalls(boolean walls) {
        WALLS = walls;
    }
}