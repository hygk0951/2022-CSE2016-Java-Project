import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankManager {
    public static int RANKING_BOARD_SIZE = 5;
    public static List<Rank> rankingBoard = new ArrayList<Rank>();

    RankManager() {
        // initialize rank
        for (int i = 0; i < RANKING_BOARD_SIZE; i++) {
            addToRankingBoard(new Rank(" none ", -1));
        }
    }

    public void addToRankingBoard(Rank playerRank) {
        rankingBoard.add(playerRank);
        //sort
        Collections.sort(rankingBoard);
        if (rankingBoard.size() > 5) {
            rankingBoard.remove(rankingBoard.size() - 1);
        }
    }

    public void printRankingBoard() {
        JPanel rankingJPanel = new JPanel();

        JLabel rankingJLabel = new JLabel("");
        String rankingString = "<html>";
        for (int i = 0; i < rankingBoard.size(); i++) {
            rankingString += (i + 1) + "위 : " + rankingBoard.get(i).nickName + " " + rankingBoard.get(i).score + "<br>";
        }
        rankingString += "</html>";
        rankingJLabel.setFont(new Font("HYGothic-Medium", Font.PLAIN, 14));
        rankingJLabel.setText(rankingString);

        JLabel scoreExplain = new JLabel("<html>각 게임 별 스코어 변동 수치<br>* 기본점수 1000점 시작<br>" +
                "* 승리 : 배팅금액 x 1.5배만큼 점수 획득<br>" +
                "* 패배 : 배팅금액 x 1.2배만큼 점수 차감<br>" +
                "* 블랙잭으로 승리시 보너스 1000점 획득</html>");
        scoreExplain.setFont(new Font("HYGothic-Medium", Font.PLAIN, 14));

        rankingJPanel.setLayout(new GridLayout(2,1));
        rankingJPanel.add(scoreExplain);
        rankingJPanel.add(rankingJLabel);
        JOptionPane.showMessageDialog(null, rankingJPanel, "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }
}

