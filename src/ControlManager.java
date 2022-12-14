// Controller of MVC

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;

public class ControlManager implements ActionListener {
    IntroView introView;
    RuleView ruleView;
    GameView gameView;
    CardDeck practiceDeck;
    String cardColor = "";
    String cardList = "";

    int total = 0;
    int AceElevenCount = 0;

    public void setIntroView(IntroView frame) {
        introView = frame;
    }

    public void setRuleView(RuleView frame) {
        ruleView = frame;
    }

    public void setGameView(GameView frame) {
        gameView = frame;
    }

    ControlManager() {
        newCardDeck();
    }

    public void newCardDeck(){
        practiceDeck = new CardDeck();
    }

    public void actionPerformed(ActionEvent e) {
        // ActionEvent의 종류 구분
        switch (e.getActionCommand()) {
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
                GameView gameView = new GameView(this, "Play");
                setGameView(gameView);
                introView.setVisible(false);
                break;
            case "Rule":
                RuleView ruleView = new RuleView(this, "Rule");
                setRuleView(ruleView);
                introView.setVisible(false);
                break;
            case "Ranking Board":
                System.out.println("Ranking Board button pressed");
                break;
            case "새 카드 받기":
                try {
                    Card selected = practiceDeck.newCard();
                    char symbol = selected.getSymbol();
                    int number = selected.getNumber();

                    switch (number) {
                        case 1: // Ace가 나온경우
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
                            break;
                        case 11: // J = 74
                            total += 10;
                            number += 63;
                            break;
                        case 12: // Q = 81
                            total += 10;
                            number += 69;
                            break;
                        case 13: // K = 75
                            total += 10;
                            number += 62;
                            break;
                        default:
                            total += number;
                            break;
                    }
                    // J, Q, K, A 인 경우

                    if(symbol == '♥' || symbol == '◆') cardColor = "<font color='red'>";
                    else cardColor = "<font color='black'>";

                    if(number >= 11 || number == 1) cardList += cardColor + symbol + " " + (char)number + "</font> ";
                    else cardList += cardColor + symbol + " " + String.valueOf(number) + "</font> ";

                    this.ruleView.cardCombinations.setText("<html>"+cardList+"</html>");
                } catch (NullPointerException ne) {
                    this.ruleView.cardCombinations.setText("모든 카드가 소진되었습니다.");
                }
                // 21을 초과했을 때 에이스카드 하나를 11이 아닌 1로 계산해서 숫자를 낮출 수 있는지 검사
                if(total > 21 && AceElevenCount <= 0){
                    this.ruleView.newCardButton.setEnabled(false);
                    this.ruleView.totalValue.setText("Total : " + total + " You lose");
                }
                else if(total > 21 && AceElevenCount > 0){
                    AceElevenCount--;
                    total -= 10;
                    this.ruleView.totalValue.setText("Total : " + total);
                }
                else if(total == 21){
                    this.ruleView.newCardButton.setEnabled(false);
                    System.out.println("Black Jack!");
                    this.ruleView.totalValue.setText("Total : " + total + " Black Jack!");
                }
                else{
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
            case "Go Home":
                if(this.ruleView != null) this.ruleView.dispose();
                if(this.gameView != null) this.gameView.dispose();
                introView.setVisible(true);
                break;

            default:
                break;
        }
    }
}
