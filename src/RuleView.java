// View

import javax.swing.*;

public class RuleView extends JFrame implements ViewBehavior {
    private JPanel ruleViewPanel;
    private JLabel ruleExplainTitleLabel;
    private JPanel ruleExplainContentsPanel;
    private JPanel ruleExplainTitlePanel;
    private JLabel ruleExplainText0;
    private JLabel ruleExplainCardValue;
    private JLabel cardCombinationLabel;
    private JLabel ruleExplainText1;
    private JButton goHomeButton;
    public JPanel cardCombinationArea;
    public JLabel cardCombinations;
    public JButton newCardButton;
    private JButton resetButton;
    public JLabel totalValue;
    public ControlManager controlManager;

    public RuleView(ControlManager controlManager, String title) {
        setContentPane(ruleViewPanel);
        this.controlManager = controlManager;
        setVisible(true);
        pack();
        setTitle(title);
        setResizable(false);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    public void initializeComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(controlManager);
        goHomeButton.addActionListener(controlManager);
        newCardButton.addActionListener(controlManager);
        resetButton.addActionListener(controlManager);
    }
}
