package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Jatekpanel
 */
class GamePanel extends JPanel implements Runnable {

    /** Panel szelessege */
    private int PWIDTH;
    /** Panel magassaga */
    private int PHEIGHT;
    /** Palya merete */
    private int BOARDSIZE;

    /** Jatek sebessege (frissites sebessege) */
    private int GAMESPEED;
    /** Gyumolcsok szama */
    private int FRUITCOUNT;
    /** Van-e fal */
    private boolean WALLS;
    /** Jatek mod: egyjatekos, vagy ketjatekos */
    private GameMode GAMEMODE;


    private boolean snakeRedDead, snakeBlueDead;
    private int redWins, blueWins;
    private LeaderBoardData leaderBoardData;

    private Thread animator;

    private boolean running;

    private GameBoard gameBoard;
    private Snake snakeRed, snakeBlue;
    private Direction moveDirectionRed, moveDirectionBlue;
    private Direction pausedDirectionRed, pausedDirectionBlue;
    private Fruits fruits;
    private JLabel score;

    private boolean gameOver;

    private Graphics dbg;
    private Image dbImage;

    public GamePanel() {

        PWIDTH = 600;
        PHEIGHT = 600;
        BOARDSIZE = 50;
        GAMESPEED = 60;
        FRUITCOUNT = 1;
        WALLS = false;
        snakeBlueDead = false;
        snakeRedDead = false;
        gameOver = false;

        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT + 50));

        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/com/Unibody8Pro-Regular.ttf"));
            font = font.deriveFont(Font.PLAIN, 24);
        } catch (Exception e) {
            e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, 24);
        }
        score = new JLabel("", JLabel.CENTER);
        score.setFont(font);
        score.setForeground(Color.DARK_GRAY);

        add(score, BorderLayout.SOUTH);

        startNewGame(GameMode.SNAKE);
        setFocusable(true);
        requestFocus();
        keyListener();
    }

    /**
     * Billentyuzet leutesek kezelese.
     */
    private void keyListener() {
        addKeyListener(
                new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        int keyCode = e.getKeyCode();
                        if (keyCode == KeyEvent.VK_ESCAPE) {
                            if (moveDirectionRed != Direction.NONE) {
                                pauseGame();
                            } else {
                                resumeGame();
                            }
                        } else if (keyCode == KeyEvent.VK_UP) {
                            moveDirectionRed = Direction.NORTH;
                        } else if (keyCode == KeyEvent.VK_RIGHT) {
                            moveDirectionRed = Direction.EAST;
                        } else if (keyCode == KeyEvent.VK_DOWN) {
                            moveDirectionRed = Direction.SOUTH;
                        } else if (keyCode == KeyEvent.VK_LEFT) {
                            moveDirectionRed = Direction.WEST;
                        } else if (GAMEMODE == GameMode.TRON && keyCode == KeyEvent.VK_W) {
                            moveDirectionBlue = Direction.NORTH;
                        } else if (GAMEMODE == GameMode.TRON && keyCode == KeyEvent.VK_D) {
                            moveDirectionBlue = Direction.EAST;
                        } else if (GAMEMODE == GameMode.TRON && keyCode == KeyEvent.VK_S) {
                            moveDirectionBlue = Direction.SOUTH;
                        } else if (GAMEMODE == GameMode.TRON && keyCode == KeyEvent.VK_A) {
                            moveDirectionBlue = Direction.WEST;
                        }
                    }
                });
    }

    public void addNotify() {
        super.addNotify();
        startGame();
    }

    private void startGame() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    @Override
    public void run() {

        running = true;

        while (running) {
            gameUpdate();
            gameRender();
            paintScreen();
            try {
                Thread.sleep(GAMESPEED);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Jatekallas frissitese.
     */
    private void gameUpdate() {
        if (!gameOver) {
            snakeRedDead = snakeRed.move(moveDirectionRed);
            if (GAMEMODE == GameMode.TRON) {
                snakeBlueDead = snakeBlue.move(moveDirectionBlue);
            }
            gameOver = snakeRedDead || snakeBlueDead;
        } else {
            if (GAMEMODE == GameMode.SNAKE) {
                leaderBoardData.addPlayer(snakeRed.getScore(), GAMESPEED == 40 ? "slow" : GAMESPEED == 60 ? "normal" : GAMESPEED == 80 ? "fast" : "unknown", FRUITCOUNT, WALLS);
            }
            if (GAMEMODE == GameMode.TRON && JOptionPane.showConfirmDialog(this, (snakeRedDead && snakeBlueDead ? "Tie" : snakeBlueDead ? "Red player won" : "Blue player won") + "\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
                if (snakeRedDead && !snakeBlueDead) blueWins++;
                else if (snakeBlueDead && !snakeRedDead) redWins++;
                score.setText(blueWins + " - " + redWins);
                startNewGame(GAMEMODE);
            } else if (GAMEMODE == GameMode.SNAKE && JOptionPane.showConfirmDialog(this, "Your Score: " + snakeRed.getScore() + "\nDo you want to try again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
                startNewGame(GAMEMODE);
            } else {
                stopGame();
            }
        }
    }

    /**
     * Kepalkotas.
     */
    private void gameRender() {

        if (dbImage == null) {
            dbImage = createImage(PWIDTH, PHEIGHT);
            dbg = dbImage.getGraphics();
        }

        dbg.setColor(Color.white);
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

        gameBoard.draw(dbg);

        if (GAMEMODE == GameMode.SNAKE) {
            fruits.draw(dbg);
            snakeRed.draw(dbg);
            score.setText(Integer.toString(snakeRed.getScore()));
        } else {
            snakeRed.draw(dbg);
            snakeBlue.draw(dbg);
        }

    }

    /**
     * Kep kirajzolasa.
     */
    private void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (dbImage != null))
                g.drawImage(dbImage, 0, 0, null);
            if (g != null) g.dispose();
        } catch (Exception e) {
            System.out.println("Graphics context error: " + e);
        }
    }

    /**
     * Uj jatek inditasa aktualis beallitasok szerint.
     *
     * @param gameMode egyjatekos mod: snake (GameMode.SNAKE), vagy ketjatekos mod: tron (GameMode.TRON)
     */
    public void startNewGame(GameMode gameMode) {
        gameBoard = new GameBoard(PWIDTH, PHEIGHT, BOARDSIZE);

        if (gameMode == GameMode.TRON) {
            FRUITCOUNT = BOARDSIZE * BOARDSIZE;
            fruits = new Fruits(PWIDTH, PHEIGHT, BOARDSIZE, FRUITCOUNT);

            snakeRed = new Snake(Color.RED, fruits, PWIDTH, PHEIGHT, BOARDSIZE);
            snakeRed.setWalls(WALLS);
            moveDirectionRed = Direction.WEST;
            pausedDirectionRed = Direction.WEST;

            snakeBlue = new Snake(Color.BLUE, fruits, PWIDTH, PHEIGHT, BOARDSIZE);
            snakeBlue.setWalls(WALLS);
            moveDirectionBlue = Direction.EAST;
            pausedDirectionBlue = Direction.EAST;

            snakeBlue.setOtherSnake(snakeRed);
            snakeRed.setOtherSnake(snakeBlue);
        } else {
            fruits = new Fruits(PWIDTH, PHEIGHT, BOARDSIZE, FRUITCOUNT);
            snakeRed = new Snake(Color.RED, fruits, PWIDTH, PHEIGHT, BOARDSIZE);
            snakeRed.setWalls(WALLS);
            moveDirectionRed = Direction.WEST;
            pausedDirectionRed = Direction.WEST;
        }

        gameOver = false;
        running = true;
        GAMEMODE = gameMode;
    }

    /**
     * Szuneteltetes.
     */
    public void pauseGame() {
        pausedDirectionRed = moveDirectionRed;
        pausedDirectionBlue = moveDirectionBlue;
        moveDirectionRed = Direction.NONE;
        moveDirectionBlue = Direction.NONE;
    }

    /**
     * Folytatas szuneteltetesbol.
     */
    public void resumeGame() {
        moveDirectionRed = pausedDirectionRed;
        moveDirectionBlue = pausedDirectionBlue;
    }

    /**
     * Jatek vegleges leallitasa.
     */
    public void stopGame() {
        running = false;
    }

    /**
     * Az uj jatekban milyen gyorsan mozogjon a kigyo?
     *
     * @param gameSpeed jateksebesseg (frissites sebessege): 40 - lassu, 60 - normal, 80 - gyors
     */
    public void setGameSpeed(int gameSpeed) {
        GAMESPEED = gameSpeed;
    }

    /**
     * Az uj jatekban hany gyumolcs legyen?
     *
     * @param fruitCount gyumolcsok szama
     */
    public void setFruitCount(int fruitCount) {
        FRUITCOUNT = fruitCount;
    }

    /**
     * Az uj jatekban legyen-e fal?
     *
     * @param walls
     */
    public void setWalls(boolean walls) {
        WALLS = walls;
    }

    /**
     * Egy jatek befejeztekor ehhez adodik hozza egy uj eredmeny,
     *
     * @param leaderBoardData ranglista adatok taroloja
     */
    public void setLeaderBoard(LeaderBoardData leaderBoardData) {
        this.leaderBoardData = leaderBoardData;
    }
}