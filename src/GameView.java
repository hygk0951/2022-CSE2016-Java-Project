// View

import javax.swing.*;

public class GameView extends JFrame implements ViewBehavior {
    private JPanel gameViewPanel;
    public JLabel gameGuide;
    public JLabel playerCardList;
    public JLabel comCardList;
    public JLabel comCardTotal;
    public JLabel playerCardTotal;
    public JLabel comMoney;
    public JLabel playerMoney;
    private JButton goHomeButton;
    public JTextField bettingField;
    public JButton bettingButton;
    public JButton moreCardButton;
    public JButton stopButton;
    public JLabel totalBettingPrize;
    public ControlManager controlManager;

    public GameView(ControlManager controlManager, String title) {
        setContentPane(gameViewPanel);
        this.controlManager = controlManager;
        setVisible(true);
        pack();
        setTitle(title);
        setResizable(false);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(controlManager);
        goHomeButton.addActionListener(controlManager);
        bettingButton.addActionListener(controlManager);
        moreCardButton.addActionListener(controlManager);
        stopButton.addActionListener(controlManager);
    }
}
