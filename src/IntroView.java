// View

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class IntroView extends JFrame implements ViewBehavior {

    public IntroView(ControlManager controlManager, String title, int width, int height) {
        this.controlManager = controlManager;
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);
        initializeComponents();
    }

    public ControlManager controlManager;
    public JPanel welcomeSignPanel = new JPanel();
    public JLabel welcomeSignLabel = new JLabel("Welcome to Black Jack ♠");
    public JPanel timerPanel = new JPanel();
    public JLabel playerNameLabel = new JLabel("<html>Player Name<br>(8글자 이하)</html>");
    public JTextField playerNameField = new JTextField();
    public JCheckBox soundCheckBox = new JCheckBox("Sound", true);
    public JCheckBox timerCheckBox = new JCheckBox("Timer", true);
    public JLabel timerLabel = new JLabel("30");
    public JButton timerDecreaseButton = new JButton("◀");
    public JButton timerIncreaseButton = new JButton("▶");
    public JPanel introButtonsPanel = new JPanel();
    public JButton introButton[];

    public void initializeComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        // NORTH - Welcome 문구 등록
        welcomeSignLabel.setFont(new Font("Serif", Font.BOLD, 25));
        welcomeSignPanel.add(welcomeSignLabel);
        add(welcomeSignPanel);

        // CENTER - 타이머 등록
        timerLabel.setHorizontalAlignment(JTextField.CENTER);
        soundCheckBox.addActionListener(controlManager);
        timerCheckBox.addActionListener(controlManager);
        timerDecreaseButton.addActionListener(controlManager);
        timerIncreaseButton.addActionListener(controlManager);

        timerPanel.add(playerNameLabel);
        timerPanel.add(playerNameField);
        playerNameLabel.setFont(new Font("HYGothic-Medium", Font.PLAIN, 14));
        playerNameField.setPreferredSize(new Dimension(100, 20));
        timerPanel.add(soundCheckBox);
        timerPanel.add(timerCheckBox);
        timerPanel.add(timerDecreaseButton);
        timerPanel.add(timerLabel);
        timerPanel.add(timerIncreaseButton);
        soundCheckBox.setFont(new Font("console", Font.PLAIN, 18));
        timerCheckBox.setFont(new Font("console", Font.PLAIN, 18));
        timerDecreaseButton.setFont(new Font("console", Font.PLAIN, 12));
        timerLabel.setFont(new Font("console", Font.PLAIN, 18));
        timerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        timerIncreaseButton.setFont(new Font("console", Font.PLAIN, 12));
        add(timerPanel);

        // SOUTH - 각 버튼(플레이하기, 규칙설명, 랭킹보드) 등록
        introButtonsPanel.setLayout(new GridLayout(1, 3));
        introButton = new JButton[3];
        for (int i = 0; i < introButton.length; i++) {
            introButton[i] = new JButton();
            introButtonsPanel.add(introButton[i]);
        }
        introButton[0].setText("Rule");
        introButton[0].setFont(new Font("serif", Font.PLAIN, 20));
        introButton[1].setText("Play");
        introButton[1].setFont(new Font("serif", Font.PLAIN, 20));
        introButton[2].setText("Ranking Board");
        introButton[2].setFont(new Font("serif", Font.PLAIN, 18));

        // 규칙설명 버튼
        introButton[0].addActionListener(controlManager);
        // 플레이 버튼
        introButton[1].addActionListener(controlManager);
        // 랭킹보드 버튼
        introButton[2].addActionListener(controlManager);
        add(introButtonsPanel);
    }
}
