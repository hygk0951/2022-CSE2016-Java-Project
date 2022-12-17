// Model

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class GameManager {

    ControlManager controlManager;
    Timer timer;
    TimerTask currentTask;

    CardDeck gameDeck;
    // gameStatus ------
    // 0 = 플레이어 입장, 1 = 배팅, 2 = 첫 두장 드로우 (플레이어, 컴퓨터),
    // 3 = 추가 드로우 턴 (플레이어), 4 = 플레이어가 21을 넘긴경우
    // 5 = 추가 드로우 턴 (컴퓨터), (딜러가 21을 넘긴경우는 6에서 공개)
    // 6 = 모든 결과 공개, 해당 판의 승무패 판단, 배팅결과 적용,
    // 7 = 최종 승리 or 패배 (한쪽이 배팅금액 소진)
    int gameStatus = 0;

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

    ArrayList<Card> computerCards = new ArrayList<>();
    ArrayList<Card> playerCards = new ArrayList<>();

    GameManager(ControlManager controlManager) {
        System.out.println("New GameManager : " + this);
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
            int wait = 5;

            @Override
            public void run() {
                controlManager.gameView.gameGuide.setText("카드를 섞는중... " + (wait - 1));
                controlManager.gameView.bettingButton.setEnabled(false);
                controlManager.gameView.moreCardButton.setEnabled(false);
                controlManager.gameView.stopButton.setEnabled(false);
                controlManager.gameView.bettingField.setEnabled(false);
                wait--;
                // 기본 인트로 5초 경과시 버튼잠금 해제
                if (wait <= 0) {
                    controlManager.gameView.bettingButton.setEnabled(true);
                    controlManager.gameView.bettingField.setEnabled(true);
                    gameStatus = 1;
                    nextStatus();
                    this.cancel();
                }
            }
        };
        currentTask = welcomeTask;
        return welcomeTask;
    }

    public TimerTask getBettingTask(int timer) {
        TimerTask bettingTask = new TimerTask() {
            int wait = timer;

            @Override
            public void run() {
                controlManager.gameView.gameGuide.setText("금액을 배팅해주세요... " + (wait - 1));
                wait--;
                if (wait <= 0) {
                    gameStatus = 2;
                    nextStatus();
                    this.cancel();
                }
            }
        };
        currentTask = bettingTask;
        return bettingTask;
    }

    public TimerTask getFirstDrawTask() {
        TimerTask firstDrawTask = new TimerTask() {
            int wait = 11;

            @Override
            public void run() {
                if (playerBet) {
                    controlManager.gameView.gameGuide.setText("배팅이 종료되었습니다. 첫 두장을 드로우 합니다... " + (wait - 1));
                } else {
                    String timerOutExplain = "<html>";
                    timerOutExplain += "타이머가 경과해 기본금액이 배팅되었습니다.<br>첫 두장을 드로우 합니다... ";
                    timerOutExplain += String.valueOf(wait - 1);
                    timerOutExplain += "<br></html>";
                    controlManager.gameView.gameGuide.setText(timerOutExplain);
                    controlManager.gameView.bettingButton.setEnabled(false);
                    controlManager.gameView.bettingField.setEnabled(false);
                }
                wait--;
                if (wait <= 0) {
                    gameStatus = 3;
                    nextStatus();
                    this.cancel();
                } else {
                    switch (wait) {
                        case 9, 7, 5, 3 -> drawCard(whosTurn);
                    }
                }
            }
        };
        currentTask = firstDrawTask;
        return firstDrawTask;
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
                        case 1 -> cardLists.append("A");
                        case 11 -> cardLists.append("J");
                        case 12 -> cardLists.append("Q");
                        case 13 -> cardLists.append("K");
                        default -> cardLists.append(computerCards.get(i).getNumber());
                    }

                    cardLists.append("  ");
                    if (computerCards.get(i).getSymbol() == '♥' || computerCards.get(i).getSymbol() == '◆')
                        cardLists.append("</font>");
                }
            }
            cardLists.append("</html>");
            if (computerCards.size() == 1)
                targetCardTotal.setText("Value of Computer : ?");
            else
                targetCardTotal.setText("Value of Computer : ? + " + (sum(computerCards, computerCards.size()) - sum(computerCards, 1)));
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
                    case 1 -> cardLists.append("A");
                    case 11 -> cardLists.append("J");
                    case 12 -> cardLists.append("Q");
                    case 13 -> cardLists.append("K");
                    default -> cardLists.append(playerCard.getNumber());
                }
                cardLists.append("  ");
                if (playerCard.getSymbol() == '♥' || playerCard.getSymbol() == '◆')
                    cardLists.append("</font>");
            }
            cardLists.append("</html>");
            targetCardTotal.setText("Value of Player("+playerName + ") : " + sum(playerCards, playerCards.size()));
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
                case 1 -> cardLists.append("A");
                case 11 -> cardLists.append("J");
                case 12 -> cardLists.append("Q");
                case 13 -> cardLists.append("K");
                default -> cardLists.append(computerCard.getNumber());
            }
            cardLists.append("  ");
            if (computerCard.getSymbol() == '♥' || computerCard.getSymbol() == '◆')
                cardLists.append("</font>");
        }
        cardLists.append("</html>");
        controlManager.gameView.comCardList.setText(cardLists.toString());
        System.out.println(cardLists);
    }

    public void nextStatus() {
        // gameStatus ------
        // 0 = 플레이어 입장, 1 = 배팅, 2 = 첫 두장 드로우 (플레이어, 컴퓨터),
        // 3 = 추가 드로우 턴 (플레이어), 4 = 플레이어가 21을 넘긴경우
        // 5 = 추가 드로우 턴 (컴퓨터), (딜러가 21을 넘긴경우는 6에서 공개)
        // 6 = 모든 결과 공개, 해당 판의 승무패 판단, 배팅결과 적용,
        // 7 = 최종 승리 or 패배 (한쪽이 배팅금액 소진)
        boolean timerChecked = controlManager.introView.timerCheckBox.isSelected();
        switch (gameStatus) {
            case 0 -> timer.schedule(getWelcomeTask(), 0, 1000);
            case 1 -> {
//                if (timerChecked)
//                    // 금액을 배팅해주세요...(타이머숫자)
//                    timer.schedule(getBettingTask(timerTime), 0, 1000);
//                else
                    controlManager.gameView.gameGuide.setText("금액을 배팅해주세요.");
            }
            case 2 -> {
                if (!playerBet) playerBetting = defaultBetting;
                controlManager.gameView.totalBettingPrize.setText("총 배팅 금액 (플레이어 + 딜러) : " + playerBetting * 2);
                playerMoney -= playerBetting;
                computerMoney -= playerBetting;
                controlManager.gameView.playerMoney.setText("$ " + playerMoney);
                controlManager.gameView.comMoney.setText("$ " + computerMoney);
                timer.schedule(getFirstDrawTask(), 0, 1000);
            }
            case 3 -> {
                if (sum(computerCards, computerCards.size()) <= 16) {
                }
            }
        }
    }
}