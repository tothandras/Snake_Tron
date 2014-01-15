package com.snakegame.test;

import com.snakegame.Fruits;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class FruitsTest {

    private Fruits fruits;
    private int fruitCount = 945;

    @Before
    public void setUp() throws Exception {
        fruits = new Fruits(10, 10, 100, fruitCount);
    }

    @Test
    public void testGetFruits() throws Exception {
        Assert.assertTrue(fruits.getFruits().size() == fruitCount);
    }

    @Test
    public void testRemoveFruit() throws Exception {
        for (int i = 0; i <= 100; ++i) {
            for (int j = 0; j <= 100; ++j) {
                Point fruit = new Point(i, j);
                if (fruits.getFruits().contains(fruit)) {
                    fruits.removeFruit(fruit);
                }
                Assert.assertFalse(fruits.getFruits().contains(fruit));
                Assert.assertTrue(fruits.getFruits().size() == fruitCount);
            }
        }
    }
}