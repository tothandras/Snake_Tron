package com.snakegame;

import javax.swing.*;
import java.awt.event.*;

/**
 * Beallitaspanel
 */
public class Settings extends JDialog {
    private GamePanel gamePanel;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton slowRadioButton;
    private JRadioButton normalRadioButton;
    private JRadioButton fastRadioButton;
    private JSlider slider1;
    private JCheckBox checkBox1;
    private JRadioButton singlePlayerRadioButton;
    private JRadioButton multiplayerRadioButton;

    public Settings(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ButtonGroup gameSpeedGroup = new ButtonGroup();
        gameSpeedGroup.add(slowRadioButton);
        gameSpeedGroup.add(normalRadioButton);
        gameSpeedGroup.add(fastRadioButton);

        ButtonGroup gameModeGroup = new ButtonGroup();
        gameModeGroup.add(singlePlayerRadioButton);
        gameModeGroup.add(multiplayerRadioButton);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }

        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        gamePanel.setWalls(checkBox1.isSelected());
        if (slowRadioButton.isSelected()) {
            gamePanel.setGameSpeed(80);
        } else if (normalRadioButton.isSelected()) {
            gamePanel.setGameSpeed(60);
        } else if (fastRadioButton.isSelected()) {
            gamePanel.setGameSpeed(40);
        }
        if (singlePlayerRadioButton.isSelected()) {
            gamePanel.setFruitCount(slider1.getValue());
            gamePanel.startNewGame(GameMode.SNAKE);
        } else if (multiplayerRadioButton.isSelected()) {
            gamePanel.startNewGame(GameMode.TRON);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
