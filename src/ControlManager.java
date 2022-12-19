// Controller

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Objects;

public class ControlManager implements ActionListener, WindowListener {

    // 프레임 좌상단 아이콘
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image img = toolkit.getImage("assets/spade.png");

    // View controller 연결 (View)
    IntroView introView;
    RuleView ruleView;
    GameView gameView;

    // Game manager 연결 (Model)
    GameManager gameManager;
    SoundManager soundManager;
    RankManager rankManager;

    boolean isSoundChecked;
    CardDeck practiceDeck;
    String cardColor = "";
    String cardList = "";

    int total = 0;
    int AceElevenCount = 0;

    public void setIntroView(IntroView frame) {
        introView = frame;
        introView.setIconImage(img);
    }

    public void setRuleView(RuleView frame) {
        ruleView = frame;
        ruleView.setIconImage(img);
    }

    public void setGameView(GameView frame) {
        gameView = frame;
        gameView.setIconImage(img);
    }

    ControlManager() throws Exception {
        soundManager = new SoundManager();
        rankManager = new RankManager();
        soundManager.playSound("bgm");
        newCardDeck();
    }

    public void newCardDeck() {
        practiceDeck = new CardDeck();
    }

    public void actionPerformed(ActionEvent e) {
        // ActionEvent의 종류 구분
        switch (e.getActionCommand()) {
            //
            case "BGM":
                isSoundChecked = introView.soundCheckBox.isSelected();
                System.out.println(isSoundChecked);
                if(isSoundChecked){
                    try {
                        soundManager.playSound("bgm");
                    } catch (Exception pse){
                    }
                }
                else{
                    soundManager.stopSound();
                }
                break;
            // Timer 체크박스를 누른 경우
            case "Timer":
                boolean isChecked = introView.timerCheckBox.isSelected();
                introView.timerDecreaseButton.setEnabled(isChecked);
                introView.timerIncreaseButton.setEnabled(isChecked);
                introView.timerLabel.setEnabled(isChecked);
                break;
            // - 를 누른경우
            case "◀":
                int timer = Integer.parseInt(introView.timerLabel.getText());
                if (timer > 10) introView.timerLabel.setText(String.valueOf(timer - 5));
                break;
            // + 를 누른경우
            case "▶":
                timer = Integer.parseInt(introView.timerLabel.getText());
                if (timer < 60) introView.timerLabel.setText(String.valueOf(timer + 5));
                break;
            case "Play":
                if (checkPlayerName()) {
                    GameView gameView = new GameView(this, "Play");
                    setGameView(gameView);
                    introView.setVisible(false);
                    gameManager = new GameManager(this);
                    gameManager.gameStatus = "welcome";
                    try {
                        gameManager.playGame();
                        soundManager = new SoundManager();
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                } else {

                }
                break;
            case "Rule":
                RuleView ruleView = new RuleView(this, "Rule");
                setRuleView(ruleView);
                introView.setVisible(false);
                break;
            case "Ranking Board":
                rankManager.printRankingBoard();
                break;
            case "새 카드 받기":
                try {
                    Card selected = practiceDeck.newCard();
                    char symbol = selected.getSymbol();
                    int number = selected.getNumber();

                    switch (number) {
                        case 1 -> { // Ace가 나온경우
                            // 10으로 계산했을때 21을 넘어서면 안된다.
                            if (total + 11 > 21) {
                                number = 1;
                                total += number;
                            } else {
                                AceElevenCount++;
                                number = 11;
                                total += number;
                            }
                            number = 65; // A
                        }
                        case 11 -> { // J = 74
                            total += 10;
                            number += 63;
                        }
                        case 12 -> { // Q = 81
                            total += 10;
                            number += 69;
                        }
                        case 13 -> { // K = 75
                            total += 10;
                            number += 62;
                        }
                        default -> total += number;
                    }
                    // J, Q, K, A 인 경우

                    if (symbol == '♥' || symbol == '◆') cardColor = "<font color='red'>";
                    else cardColor = "<font color='black'>";

                    if (number >= 11 || number == 1) cardList += cardColor + symbol + " " + (char) number + "</font> ";
                    else cardList += cardColor + symbol + " " + String.valueOf(number) + "</font> ";

                    this.ruleView.cardCombinations.setText("<html>" + cardList + "</html>");
                } catch (NullPointerException ne) {
                    this.ruleView.cardCombinations.setText("모든 카드가 소진되었습니다.");
                }
                // 21을 초과했을 때 에이스카드 하나를 11이 아닌 1로 계산해서 숫자를 낮출 수 있는지 검사
                if (total > 21 && AceElevenCount <= 0) {
                    this.ruleView.newCardButton.setEnabled(false);
                    this.ruleView.totalValue.setText("Total : " + total + " You lose");
                } else if (total > 21 && AceElevenCount > 0) {
                    AceElevenCount--;
                    total -= 10;
                    this.ruleView.totalValue.setText("Total : " + total);
                } else if (total == 21) {
                    this.ruleView.newCardButton.setEnabled(false);
                    System.out.println("Black Jack!");
                    this.ruleView.totalValue.setText("Total : " + total + " Black Jack!");
                } else {
                    this.ruleView.totalValue.setText("Total : " + total);
                }
                break;
            case "초기화":
                total = 0;
                AceElevenCount = 0;
                cardList = "";
                newCardDeck();
                this.ruleView.newCardButton.setEnabled(true);
                this.ruleView.cardCombinations.setText("");
                this.ruleView.totalValue.setText("Total : 0");
                break;
            case "배팅하기":
                try {
                    this.gameManager.playerBetting = Integer.parseInt(this.gameView.bettingField.getText());
                    if(this.gameManager.playerBetting <= 0) throw new NumberFormatException("minusNumber");
                    else if(this.gameManager.playerBetting > this.gameManager.playerMoney)
                        throw new NumberFormatException("overPlayerMoney");
                    else if(this.gameManager.playerBetting > this.gameManager.computerMoney)
                        throw new NumberFormatException("overComputerMoney");
                    this.gameManager.playerBet = true;
                    this.gameView.bettingButton.setEnabled(false);
                    this.gameView.bettingField.setEnabled(false);
                    this.gameManager.gameStatus = "firstDraw";
                    this.gameManager.currentTask.cancel();
                    this.gameManager.nextStatus();
                    this.gameManager.playerBet = true;
                } catch (NumberFormatException nfe) {
                    if (Objects.equals(nfe.getMessage(), "minusNumber"))
                        this.gameView.totalBettingPrize.setText("0보다 큰 금액을 입력해주세요.");
                    else if (Objects.equals(nfe.getMessage(), "overPlayerMoney"))
                        this.gameView.totalBettingPrize.setText("플레이어의 잔액보다 큰 금액을 배팅할 수 없습니다.");
                    else if (Objects.equals(nfe.getMessage(), "overComputerMoney"))
                        this.gameView.totalBettingPrize.setText("딜러의 잔액보다 큰 금액을 배팅할 수 없습니다.");
                    else
                        this.gameView.totalBettingPrize.setText("숫자가 아닙니다. 올바른 금액을 입력해주세요.");
                    this.gameView.bettingField.setText("");
                } catch (Exception ee){
                    ee.printStackTrace();
                }
                break;
            case "Go Home":
                if (this.ruleView != null) this.ruleView.dispose();
                if (this.gameView != null) this.gameView.dispose();
                if (gameManager != null) gameManager.timer.cancel();
                introView.setVisible(true);
                break;
            case "카드 받기":
                gameManager.playerNeedMoreCard = true;
                this.gameView.moreCardButton.setEnabled(false);
                this.gameView.stopButton.setEnabled(false);
                gameManager.currentTask.cancel();
                gameManager.gameStatus = "givePlayerACard";
                gameManager.nextStatus();
                break;
            case "멈추기":
                this.gameView.moreCardButton.setEnabled(false);
                this.gameView.stopButton.setEnabled(false);
                gameManager.currentTask.cancel();
                gameManager.gameStatus = "computerDrawTurn";
                gameManager.nextStatus();
                break;
            default:
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // 닫은 창이 인트로뷰가 아닌경우 (게임뷰 or 룰뷰인경우) 인트로뷰로 전환
        if (!e.getSource().getClass().getName().equals("Intro")) {
            introView.setVisible(true);
            if (gameManager != null) gameManager.timer.cancel();
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public boolean checkPlayerName() {
        String checkString = this.introView.playerNameField.getText();
        JLabel warning = new JLabel();
        warning.setFont(new Font("HYGothic-Medium", Font.PLAIN, 14));
        if (checkString.length() == 0) {
            warning.setText("플레이어 이름을 입력해주세요.");
            JOptionPane.showMessageDialog(null, warning, "주의", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (checkString.length() > 8) {
            warning.setText("플레이어 이름은 공백포함 최대 8글자입니다.");
            JOptionPane.showMessageDialog(null, warning, "주의", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }
}
