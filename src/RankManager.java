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
            addToRankingBoard(new Rank(" .. ", 0));
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
        JLabel rankingJLabel = new JLabel("");
        String rankingString = "<html>";
        for (int i = 0; i < rankingBoard.size(); i++) {
            rankingString += (i + 1) + " - " + rankingBoard.get(i).nickName + " " + rankingBoard.get(i).score + "<br>";
        }
        rankingString += "</html>";
        rankingJLabel.setFont(new Font("HYGothic-Medium", Font.PLAIN, 14));
        rankingJLabel.setText(rankingString);
        JOptionPane.showMessageDialog(null, rankingJLabel, "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }
}

