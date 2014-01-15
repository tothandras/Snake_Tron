package com.snakegame;

import javax.swing.table.AbstractTableModel;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ranglista adatok tarolasa.
 */
public class LeaderBoardData extends AbstractTableModel {

    List<Player> players;

    public LeaderBoardData() {
        players = new ArrayList<Player>();
    }

    public void addPlayer(int score, String gameSpeed, int fruitCount, boolean walls) {
        players.add(new Player(score, gameSpeed, fruitCount, walls, new Time(System.currentTimeMillis())));
    }

    @Override
    public int getRowCount() {
        return players.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Player player = players.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return player.getDate();
            case 1:
                return player.getScore();
            case 2:
                return player.getGameSpeed();
            case 3:
                return player.getFruitCount();
            default:
                return player.isWalls();
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Date";
            case 1:
                return "Score";
            case 2:
                return "Game speed";
            case 3:
                return "Fruit count";
            default:
                return "Wall";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Date.class;
            case 1:
                return Integer.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            default:
                return Boolean.class;
        }
    }
}
