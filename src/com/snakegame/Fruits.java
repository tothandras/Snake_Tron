package com.snakegame;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Gyumolcs
 */
public class Fruits implements GameElement {
    private HashSet<Point> fruits;
    private int fruitCount;
    private int width;
    private int height;
    private int boardSize;

    public Fruits(int width, int height, int boardSize, int fruitCount) {
        this.boardSize = boardSize;
        this.width = width / boardSize;
        this.height = height / boardSize;
        this.fruitCount = fruitCount;
        this.fruits = new HashSet<Point>();

        addFruits();
    }

    /**
     * A gyomolcsoket mas nem modosithatja, csak olvashatja, ezert nem az eredeti tarolot adja vissza, hanem annak nem modosithato masolatat.
     * @return gyumolcsok
     */
    public Set<Point> getFruits() {
        return Collections.unmodifiableSet(fruits);
    }

    /**
     * Valaki megette a gyumolcsot, ezert torolni kell.
     * @param point Melyik gyumolcsot kell torolni?
     */
    public void removeFruit(Point point) {
        fruits.remove(point);
        addFruits();
    }

    /**
     * Uj gyumolcsok hozzaadasa.
     * Mivel a Set-hez nem adhatunk hozza ket gyumolcsot (pontot) egyezo koordinatakkal, de a veletlenszam generalhat olyan gyumolcsot ami mar letezik, ezert addig fut a ciklus amig a megfelelo szamu gyumolcs nincs a taroloban.
     * Alkalmas a tarolo kezdeti feltoltesere es ujratoltesere.
     */
    private void addFruits() {
        while (fruits.size() != fruitCount) {
            Point fruit = new Point(new Random().nextInt(boardSize), new Random().nextInt(boardSize));
            fruits.add(fruit);
        }
    }


    /**
     * Gyumolcsok kirajzolasa zold szinnel.
     * @param g mire kell kirajzolni?
     */
    @Override
    public void draw(Graphics g) {
        Color currentColor = g.getColor();
        g.setColor(Color.GREEN);
        for (Point fruit : fruits) {
            g.fillOval(fruit.x * width, fruit.y * height, width, height);
        }
        g.setColor(currentColor);
    }
}
