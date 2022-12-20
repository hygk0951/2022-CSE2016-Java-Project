// Model

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class GameManager {

    ControlManager controlManager;

    Timer timer;
    TimerTask currentTask;

    CardDeck gameDeck;
    String gameStatus = "";

    boolean whosTurn = true;
    String playerName;
    boolean isTimerON = false;
    int timerTime = 0;
    boolean playerBet = false;
    int defaultMoney = 1000;
    int computerMoney = defaultMoney;
    int playerMoney = defaultMoney;
    int defaultBetting = 100;
    int playerBetting = 0;
    int defaultGameScore = 1000;
    int gameScore = defaultGameScore;
    boolean playerNeedMoreCard = false;

    ArrayList<Card> computerCards = new ArrayList<>();
    ArrayList<Card> playerCards = new ArrayList<>();

    GameManager(ControlManager controlManager) {
//        System.out.println("New GameManager : " + this);
        this.controlManager = controlManager;
        timer = new Timer();
    }

    public void playGame() {
        gameDeck = new CardDeck();
        playerName = controlManager.introView.playerNameField.getText();
        isTimerON = controlManager.introView.timerCheckBox.isSelected();
        timerTime = Integer.parseInt(controlManager.introView.timerLabel.getText()) + 1;
        controlManager.gameView.comMoney.setText("$ " + computerMoney);
        controlManager.gameView.playerMoney.setText("$ " + playerMoney);
        nextStatus();
    }

    public TimerTask getWelcomeTask() {
        TimerTask welcomeTask = new TimerTask() {
            int wait = 7;

            @Override
            public void run() {
                wait--;
                controlManager.gameView.gameGuide.setText("카드를 섞는중... " + (wait - 1));
                controlManager.gameView.bettingButton.setEnabled(false);
                controlManager.gameView.moreCardButton.setEnabled(false);
                controlManager.gameView.stopButton.setEnabled(false);
                controlManager.gameView.bettingField.setEnabled(false);
                controlManager.gameView.comCardList.setText("");
                controlManager.gameView.comCardTotal.setText("");
                controlManager.gameView.playerCardList.setText("");
                controlManager.gameView.playerCardTotal.setText("");
                controlManager.gameView.playerMoney.setText("$ " + playerMoney);
                controlManager.gameView.comMoney.setText("$ " + computerMoney);
                controlManager.gameView.totalBettingPrize.setText("총 배팅 (딜러 + 플레이어) : $ 0");
                computerCards.clear();
                playerCards.clear();
                // 기본 인트로 5초 경과시 버튼잠금 해제
                if (wait <= 0) {
                    controlManager.gameView.bettingButton.setEnabled(true);
                    controlManager.gameView.bettingField.setEnabled(true);
                    gameStatus = "betting";
                    nextStatus();
                    this.cancel();
                }
            }
        };
        currentTask = welcomeTask;
        return welcomeTask;
    }

    public TimerTask getFirstDrawTask() {
        TimerTask task = new TimerTask() {
            int wait = 9;

            @Override
            public void run() {
                controlManager.gameView.gameGuide.setText("배팅이 종료되었습니다. 첫 두장을 드로우 합니다... ");
                wait--;
                if (wait <= 0) {
                    gameStatus = "playerDrawTurn";
                    nextStatus();
                    this.cancel();
                } else {
                    if (wait == 7 || wait == 5 || wait == 3 || wait == 1) {
                        drawCard(whosTurn);
                        try {
                            controlManager.soundManager.playSound("draw");
                        } catch (Exception e) {
//                            if (controlManager.soundManager == null) System.out.println("Mute mode");
//                            else
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        currentTask = task;
        return task;
    }

    public TimerTask getPlayerDrawTask1(int timer) {
        TimerTask task = new TimerTask() {
            int wait = timer;

            @Override
            public void run() {
                if (sum(playerCards, playerCards.size()) < 21) {
                    if (isTimerON)
                        controlManager.gameView.gameGuide.setText("카드를 더 받으시겠습니까? (타이머 종료 = 안받기) " + (wait - 1));
                    else
                        controlManager.gameView.gameGuide.setText("카드를 더 받으시겠습니까? (타이머OFF)");
                } else if (sum(playerCards, playerCards.size()) == 21) {
                    if (isTimerON)
                        controlManager.gameView.gameGuide.setText("<html>축하합니다! 블랙잭입니다!<br>단, 딜러도 블랙잭이면 무승부입니다.<br>(타이머 종료 = 딜러 턴) " + (wait - 1) + "</html>");
                    else
                        controlManager.gameView.gameGuide.setText("<html>축하합니다! 블랙잭입니다!<br>단, 딜러도 블랙잭이면 무승부입니다.<br>(타이머OFF)</html>");
                } else {
                    if (isTimerON)
                        controlManager.gameView.gameGuide.setText("<html>더 이상 카드를 받을 수 없습니다.<br>단, 딜러가 21을 넘기면 무승부입니다.<br>(타이머 종료 = 딜러 턴) " + (wait - 1) + "</html>");
                    else
                        controlManager.gameView.gameGuide.setText("<html>더 이상 카드를 받을 수 없습니다.<br>단, 딜러가 21을 넘기면 무승부입니다.<br>(타이머OFF)</html>");
                    controlManager.gameView.moreCardButton.setEnabled(false);
                }
                wait--;
                // 시간초과
                if (isTimerON && wait <= 0) {
                    controlManager.gameView.moreCardButton.setEnabled(false);
                    controlManager.gameView.stopButton.setEnabled(false);
                    gameStatus = "computerDrawTurn";
                    nextStatus();
                    this.cancel();
                } else {

                }
            }
        };
        currentTask = task;
        return task;
    }

    public TimerTask getPlayerDrawTask2(int timer) {
        TimerTask task = new TimerTask() {
            int wait = timer;

            @Override
            public void run() {
                controlManager.gameView.gameGuide.setText("플레이어의 카드를 추가했습니다. " + (wait - 1));
                wait--;
                if (wait <= 0) {
                    gameStatus = "playerDrawTurn";
                    nextStatus();
                    this.cancel();
                } else {

                }
            }
        };
        currentTask = task;
        return task;
    }

    public TimerTask getComputerDrawTask1(int timer) {
        TimerTask task = new TimerTask() {
            int wait = timer;

            @Override
            public void run() {
                if (sum(computerCards, computerCards.size()) < 17) {
                    gameStatus = "giveComputerACard";
                    nextStatus();
                    this.cancel();
                } else {
                    controlManager.gameView.gameGuide.setText("딜러 카드의 총합이 17이상입니다. 딜러는 카드를 받지않습니다." + (wait - 1));
                    gameStatus = "openResult";
                }
                wait--;
                if (wait <= 0) {
                    nextStatus();
                    this.cancel();
                } else {

                }
            }
        };
        currentTask = task;
        return task;
    }

    public TimerTask getComputerDrawTask2(int timer) {
        TimerTask task = new TimerTask() {
            int wait = timer;

            @Override
            public void run() {
                controlManager.gameView.gameGuide.setText("딜러 카드의 총합이 16이하입니다. 딜러의 카드를 추가합니다. " + (wait - 1));
                wait--;
                if (wait <= 0) {
                    gameStatus = "openResult";
                    nextStatus();
                    this.cancel();
                } else {

                }
            }
        };
        currentTask = task;
        return task;
    }

    public TimerTask openResultTask(int timer) {
        TimerTask task = new TimerTask() {
            int wait = timer;
            int whoWin = 0; // 0 Computer 1 Player 2 Tie
            boolean blackjack = true;

            @Override
            public void run() {
                // 결과오픈
                // 블랙잭이 나온 경우
                if (sum(playerCards, playerCards.size()) == 21) {
                    // 컴퓨터도 블랙잭이 나온 경우 (무승부인 경우)
                    if (sum(computerCards, computerCards.size()) == 21) {
                        controlManager.gameView.gameGuide.setText("무승부 (플레이어와 딜러 모두 블랙잭) " + (wait - 1));
                        whoWin = 2;
                        // 플레이어만 블랙잭이 나온 경우
                    } else {
                        controlManager.gameView.gameGuide.setText("플레이어 승리 (블랙잭!) " + (wait - 1));
                        if (blackjack) {
                            gameScore += 1000;
                            blackjack = false;
                        }
                        whoWin = 1;
                    }
                } else if (sum(playerCards, playerCards.size()) > 21 && sum(computerCards, computerCards.size()) > 21) {
                    controlManager.gameView.gameGuide.setText("무승부 (플레이어와 딜러 모두 21초과) " + wait);
                    whoWin = 2;
                }
                // 플레이어가 21을 초과한 경우
                else if (sum(playerCards, playerCards.size()) > 21) {
                    controlManager.gameView.gameGuide.setText("딜러 승리 (플레이어 21초과) " + wait);
                    whoWin = 0;
                    // 컴퓨터가 21을 초과한 경우
                } else if (sum(computerCards, computerCards.size()) > 21) {
                    controlManager.gameView.gameGuide.setText("플레이어 승리 (딜러 21초과) " + wait);
                    whoWin = 1;
                    //무승부인 경우
                } else if (sum(playerCards, playerCards.size()) == sum(computerCards, computerCards.size())) {
                    controlManager.gameView.gameGuide.setText("무승부 (같은 값) " + wait);
                    whoWin = 2;
                    // 플레이어 승
                } else if (sum(playerCards, playerCards.size()) > sum(computerCards, computerCards.size())) {
                    controlManager.gameView.gameGuide.setText("플레이어 승리 " + wait);
                    whoWin = 1;
                    // 컴퓨터 승
                } else if (sum(playerCards, playerCards.size()) < sum(computerCards, computerCards.size())) {
                    controlManager.gameView.gameGuide.setText("딜러 승리 " + wait);
                    whoWin = 0;
                }
                if (wait <= 0) {
                    switch (whoWin) {
                        // 딜러가 이긴 경우
                        case 0:
                            computerMoney += (playerBetting * 2);
                            gameScore -= (playerBetting * 1.2);
                            break;
                        // 플레이어가 이긴 경우
                        case 1:
                            playerMoney += (playerBetting * 2);
                            gameScore += (playerBetting * 1.5);
                            break;
                        // 무승부인 경우
                        case 2:
//                            System.out.println("무승부");
                            computerMoney += playerBetting;
                            playerMoney += playerBetting;
                            break;
                    }
                    gameStatus = "welcome";
                    nextStatus();
                    this.cancel();
                } else {

                }
                wait--;
            }
        };
        currentTask = task;
        return task;
    }

    public TimerTask finalResultTask(int timer) {
        TimerTask task = new TimerTask() {
            int wait = timer;
            boolean addOnce = true;

            @Override
            public void run() {
                if (gameScore < 0) gameScore = 0;
                if (addOnce) {
                    controlManager.rankManager.addToRankingBoard(new Rank(playerName, gameScore));
                    addOnce = false;
                }
                if (playerMoney == 0) {
                    controlManager.gameView.playerMoney.setText("$ " + playerMoney);
                    controlManager.gameView.comMoney.setText("$ " + computerMoney);
                    controlManager.gameView.gameGuide.setText("패배했습니다. 최종스코어는..? " + (wait - 1));
                } else {
                    controlManager.gameView.playerMoney.setText("$ " + playerMoney);
                    controlManager.gameView.comMoney.setText("$ " + computerMoney);
                    controlManager.gameView.gameGuide.setText("승리했습니다! 최종스코어는..? " + (wait - 1));
                }
                wait--;
                if (wait <= 0) {
                    JLabel finalResult = new JLabel("닉네임 : " + playerName + ", 최종 스코어 : " + gameScore);
                    finalResult.setFont(new Font("HYGothic-Medium", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(null, finalResult, "최종 결과", JOptionPane.INFORMATION_MESSAGE);
                    this.cancel();
                }
            }
        };
        currentTask = task;
        return task;
    }

    public void drawCard(boolean whosTurn) {
        JLabel targetCardTotal;
        JLabel targetCardList;
        StringBuilder cardLists = new StringBuilder("<html>");
        // 컴퓨터 턴인경우
        if (whosTurn) {
            targetCardTotal = controlManager.gameView.comCardTotal;
            targetCardList = controlManager.gameView.comCardList;
            computerCards.add(gameDeck.newCard());
            for (int i = 0; i < computerCards.size(); i++) {
                if (i == 0) cardLists.append("?? ");
                else {
                    if (computerCards.get(i).getSymbol() == '♥' || computerCards.get(i).getSymbol() == '◆')
                        cardLists.append("<font color='red'>");
                    cardLists.append(computerCards.get(i).getSymbol());
                    switch (computerCards.get(i).getNumber()) {
                        case 1:
                            cardLists.append("A");
                            break;
                        case 11:
                            cardLists.append("J");
                            break;
                        case 12:
                            cardLists.append("Q");
                            break;
                        case 13:
                            cardLists.append("K");
                            break;
                        default:
                            cardLists.append(computerCards.get(i).getNumber());
                            break;
                    }

                    cardLists.append("  ");
                    if (computerCards.get(i).getSymbol() == '♥' || computerCards.get(i).getSymbol() == '◆')
                        cardLists.append("</font>");
                }
            }
            cardLists.append("</html>");
            if (computerCards.size() == 1)
                targetCardTotal.setText("Dealer : ?");
            else
                targetCardTotal.setText("Dealer : ? + " + (sum(computerCards, computerCards.size()) - sum(computerCards, 1)));
            targetCardList.setText(cardLists.toString());
        } else {
            targetCardTotal = controlManager.gameView.playerCardTotal;
            targetCardList = controlManager.gameView.playerCardList;
            playerCards.add(gameDeck.newCard());
            for (Card playerCard : playerCards) {
                if (playerCard.getSymbol() == '♥' || playerCard.getSymbol() == '◆')
                    cardLists.append("<font color='red'>");
                cardLists.append(playerCard.getSymbol());
                switch (playerCard.getNumber()) {
                    case 1:
                        cardLists.append("A");
                        break;
                    case 11:
                        cardLists.append("J");
                        break;
                    case 12:
                        cardLists.append("Q");
                        break;
                    case 13:
                        cardLists.append("K");
                        break;
                    default:
                        cardLists.append(playerCard.getNumber());
                }
                cardLists.append("  ");
                if (playerCard.getSymbol() == '♥' || playerCard.getSymbol() == '◆')
                    cardLists.append("</font>");
            }
            cardLists.append("</html>");
            targetCardTotal.setText("Player(" + playerName + ") : " + sum(playerCards, playerCards.size()));
            targetCardList.setText(cardLists.toString());
        }
        // 턴 넘겨주기
        this.whosTurn = !whosTurn;
    }

    public static int sum(ArrayList<Card> cardList, int size) {
        int sum = 0;
        int aceCount = 0;

        for (int i = 0; i < size; i++) {
            // J Q K인 경우
            if (cardList.get(i).getNumber() >= 11)
                sum += 10;
                // Ace인 경우
            else if (cardList.get(i).getNumber() == 1) {
                aceCount++;
                sum += 11;
            }
            // 일반 숫자인경우
            else sum += cardList.get(i).getNumber();
        }
        // 총 합이 21을 넘어갔을 경우, 11대신 1로 계산할 수 있는 Ace가 존재하는지 검사
        if (sum > 21 && aceCount > 0) {
            aceCount--;
            sum -= 10;
        }
        return sum;
    }

    public void OpenComputerCards() {
        StringBuilder cardLists = new StringBuilder("<html>");
        for (Card computerCard : computerCards) {
            if (computerCard.getSymbol() == '♥' || computerCard.getSymbol() == '◆')
                cardLists.append("<font color='red'>");
            cardLists.append(computerCard.getSymbol());
            switch (computerCard.getNumber()) {
                case 1:
                    cardLists.append("A");
                    break;
                case 11:
                    cardLists.append("J");
                    break;
                case 12:
                    cardLists.append("Q");
                    break;
                case 13:
                    cardLists.append("K");
                    break;
                default:
                    cardLists.append(computerCard.getNumber());
                    break;
            }
            cardLists.append("  ");
            if (computerCard.getSymbol() == '♥' || computerCard.getSymbol() == '◆')
                cardLists.append("</font>");
        }
        cardLists.append("</html>");
        controlManager.gameView.comCardList.setText(cardLists.toString());
        controlManager.gameView.comCardTotal.setText("Dealer : " + (sum(computerCards, computerCards.size())));
//        System.out.println(cardLists);
    }

    public void nextStatus() {
        // gameStatus ------
        // welcome = 플레이어 입장
        // betting = 배팅
        // firstDraw = 첫 두장 드로우 (플레이어, 컴퓨터)
        // playerDrawTurn = 추가 드로우 턴 (플레이어)
        // givePlayerACard = 플레이어에게 카드 지급
        // computerDrawTurn = 추가 드로우 턴 (컴퓨터)
        // giveComputerACard = 컴퓨터에게 카드 지급
        // openResult = 모든 결과 공개, 해당 판의 승무패 판단, 배팅결과 적용
        // finalResult = 최종 승리 or 패배 (한쪽이 배팅금액 소진)
        switch (gameStatus) {
            case "welcome":
                // 한쪽이 돈을 전부 소진한 경우
                if (playerMoney == 0 || computerMoney == 0) {
                    gameStatus = "finalResult";
                    nextStatus();
                } else {
                    timer.schedule(getWelcomeTask(), 0, 1000);
                    try {
                        controlManager.soundManager.playSound("shuffle");
                    } catch (Exception e) {
//                        if (controlManager.soundManager == null) System.out.println("Mute mode");
//                        else
                        e.printStackTrace();
                    }
                }
                break;
            case "betting":
                controlManager.gameView.gameGuide.setText("금액을 배팅해주세요.");
                break;
            case "firstDraw":
                if (!playerBet) playerBetting = defaultBetting;
                controlManager.gameView.totalBettingPrize.setText("총 배팅 (딜러 + 플레이어) : $ " + playerBetting * 2);
                playerMoney -= playerBetting;
                computerMoney -= playerBetting;
                controlManager.gameView.playerMoney.setText("$ " + playerMoney);
                controlManager.gameView.comMoney.setText("$ " + computerMoney);
                timer.schedule(getFirstDrawTask(), 0, 1000);
                break;
            case "playerDrawTurn":
                controlManager.gameView.moreCardButton.setEnabled(true);
                controlManager.gameView.stopButton.setEnabled(true);
                timer.schedule(getPlayerDrawTask1(controlManager.gameManager.timerTime), 0, 1000);
                break;
            case "givePlayerACard":
                timer.schedule(getPlayerDrawTask2(6), 0, 1000);
                drawCard(false);
                try {
                    controlManager.soundManager.playSound("draw");
                } catch (Exception e) {
                }
//                if (sum(playerCards, playerCards.size()) >= 21) {
//                    currentTask.cancel();
//                    gameStatus = "openResult";
//                    nextStatus();
//                }
                break;
            case "computerDrawTurn":
                timer.schedule(getComputerDrawTask1(6), 0, 1000);
                break;
            case "giveComputerACard":
                timer.schedule(getComputerDrawTask2(6), 0, 1000);
                drawCard(true);
                try {
                    controlManager.soundManager.playSound("draw");
                } catch (Exception e) {
                }
//                if (sum(playerCards, playerCards.size()) >= 21) {
//                    currentTask.cancel();
//                    gameStatus = "openResult";
//                    nextStatus();
//                }
                break;
            case "openResult":
                timer.schedule(openResultTask(6), 0, 1000);
                OpenComputerCards();
                break;
            case "finalResult":
                timer.schedule(finalResultTask(6), 0, 1000);
                break;
        }
    }
}