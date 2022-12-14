import javax.swing.*;

public class GameView extends JFrame implements ViewBehavior{
    private JPanel gameViewPanel;
    private JLabel gameGuide;
    private JLabel playerCardList;
    private JLabel comCardList;
    private JLabel comCardTotal;
    private JLabel playerCardTotal;
    private JLabel comMoney;
    private JLabel playerMoney;
    private JButton goHomeButton;
    private JTextField bettingField;
    private JButton bettingButton;
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
        goHomeButton.addActionListener(controlManager);
    }
}
