package com.snakegame;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.List;

/**
 * Snake - Tron jatek
 *
 * @author Toth Andras - O8POUA
 * @version 1.0
 */
public class Game extends JFrame implements WindowListener {
    private GamePanel gamePanel;
    private LeaderBoardData leaderBoardData;

    public Game() {
        super("SNAKE | TRON");

        leaderBoardData = new LeaderBoardData();
        try {
            leaderBoardData = new LeaderBoardData();
            File leaderBoardFile = new File("players.dat");
            if (leaderBoardFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(leaderBoardFile));
                leaderBoardData.players = (List<Player>) ois.readObject();
                ois.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        makeGUI();
        addWindowListener(this);
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {
        new Game();
    }

    /**
     * Ballitaspanel megnyitasa.
     */
    private class settings implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Settings settingsFrame = new Settings(gamePanel);
            settingsFrame.setSize(new Dimension(400, 220));
            settingsFrame.setLocationRelativeTo(null);
            settingsFrame.setResizable(false);
            settingsFrame.setVisible(true);
        }
    }

    /**
     * Ranglista megnyitasa.
     */
    private class leaderBoard implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame board = new JFrame();
            JTable table = new JTable(leaderBoardData);
            TableRowSorter<LeaderBoardData> tableRowSorter = new TableRowSorter<LeaderBoardData>(leaderBoardData);
            table.setRowSorter(tableRowSorter);
            JScrollPane jScrollPane = new JScrollPane(table);

            board.setSize(new Dimension(500, 200));
            board.setLocationRelativeTo(null);
            board.setResizable(false);
            board.setLayout(new BorderLayout());
            board.add(jScrollPane, BorderLayout.CENTER);

            board.setVisible(true);
        }
    }

    /**
     * Felhasznaloi felulet letrehozasa.
     * A betutipust es a gombokhoz hasznalt kepeket fajlbol tolti be.
     */
    private void makeGUI() {
        gamePanel = new GamePanel();
        gamePanel.setLeaderBoard(leaderBoardData);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/com/Unibody8Pro-Regular.ttf"));
            font = font.deriveFont(Font.PLAIN, 24);
        } catch (Exception e) {
            e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, 24);
        }

        JPanel top = new JPanel(new FlowLayout());

        JLabel logo = new JLabel("SNAKE", JLabel.LEFT);
        logo.setFont(font);

        JButton settingsButton = new JButton("");
        settingsButton.setIcon(new ImageIcon(getClass().getResource("/com/settings.png")));
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusable(false);
        settingsButton.addActionListener(new settings());
        settingsButton.setSize(new Dimension(30, 30));

        JButton leaderBoardButton = new JButton("");
        leaderBoardButton.setIcon(new ImageIcon(getClass().getResource("/com/leaderboard.png")));
        leaderBoardButton.setBorderPainted(false);
        leaderBoardButton.setContentAreaFilled(false);
        leaderBoardButton.setFocusable(false);
        leaderBoardButton.addActionListener(new leaderBoard());
        leaderBoardButton.setSize(new Dimension(30, 30));

        top.add(logo);
        top.add(Box.createRigidArea(new Dimension(380, 30)));
        top.add(settingsButton);
        top.add(leaderBoardButton);

        container.add(top, BorderLayout.NORTH);
        container.add(gamePanel, BorderLayout.CENTER);
    }

    public void windowActivated(WindowEvent e) {
        gamePanel.resumeGame();
    }

    public void windowDeactivated(WindowEvent e) {
        gamePanel.pauseGame();
    }


    public void windowDeiconified(WindowEvent e) {
        gamePanel.resumeGame();
    }

    public void windowIconified(WindowEvent e) {
        gamePanel.pauseGame();
    }

    /**
     * Uj eredmenyek elmentese fajlba.
     */
    public void windowClosing(WindowEvent e) {
        gamePanel.stopGame();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("players.dat"));
            oos.writeObject(leaderBoardData.players);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

}
