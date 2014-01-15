package com.snakegame;


import java.io.Serializable;
import java.util.Date;

/**
 * Jatekos: elert pontszam, jateksebessek, gyumolcsok szama, fal?, datum
 */
public class Player implements Serializable{

    int score;
    String gameSpeed;
    int fruitCount;
    boolean walls;
    Date date;

    public Player(int score, String gameSpeed, int fruitCount, boolean walls, Date date) {
        this.score = score;
        this.gameSpeed = gameSpeed;
        this.fruitCount = fruitCount;
        this.walls = walls;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public String getGameSpeed() {
        return gameSpeed;
    }

    public int getFruitCount() {
        return fruitCount;
    }

    public boolean isWalls() {
        return walls;
    }

    public Date getDate() {
        return date;
    }
}
