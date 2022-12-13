// <View> of MVC

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class IntroView extends JFrame implements ViewBehavior {

    public IntroView(String title, int width, int height) {
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(false);
        initializeComponents();
    }

    public Panel welcomeSignPanel;
    public JLabel welcomeSignLabel;
    public Panel timerPanel;
    public JCheckBox timerCheckBox;
    public JTextField timerTextField;
    public JButton timerDecreaseButton;
    public JButton timerIncreaseButton;
    public Panel introButtonsPanel;
    public JButton introButton[];
    public ControlManager introControlManager = new ControlManager(this);

    public void initializeComponents() {
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        // NORTH - Welcome 문구 등록
        welcomeSignPanel = new Panel();
        welcomeSignLabel = new JLabel("Welcome to Black Jack game");
        welcomeSignLabel.setFont(new Font("Serif", Font.BOLD, 22));

        welcomeSignPanel.add(welcomeSignLabel);
        add(welcomeSignPanel);

        // CENTER - 타이머 등록
        timerPanel = new Panel();
        timerCheckBox = new JCheckBox("Timer", true);
        timerTextField = new JTextField("30", 4);
        timerTextField.setHorizontalAlignment(JTextField.CENTER);
        timerDecreaseButton = new JButton("-5");
        timerIncreaseButton = new JButton("+5");

        timerCheckBox.addActionListener(introControlManager);
        timerDecreaseButton.addActionListener(introControlManager);
        timerIncreaseButton.addActionListener(introControlManager);

        timerPanel.add(timerCheckBox);
        timerPanel.add(timerDecreaseButton);
        timerPanel.add(timerTextField);
        timerPanel.add(timerIncreaseButton);
        add(timerPanel);

        // SOUTH - 각 버튼(플레이하기, 규칙설명, 랭킹보드) 등록
        introButtonsPanel = new Panel();
        introButtonsPanel.setLayout(new GridLayout(1, 3));
        introButton = new JButton[3];
        for (int i = 0; i < introButton.length; i++) {
            introButton[i] = new JButton();
            introButtonsPanel.add(introButton[i]);
        }
        introButton[0].setText("Play");
        introButton[1].setText("Rule");
        introButton[2].setText("Ranking Board");

        // 플레이
        introButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // 규칙 설명
        introButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        add(introButtonsPanel);
    }
}
