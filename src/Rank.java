public class Rank implements Comparable {
    String nickName = "";
    int score = 0;

    Rank(String nickName, int score) {
        this.nickName = nickName;
        if (score > 0) this.score = score;
    }

    // 점수 높은순으로 정렬
    @Override
    public int compareTo(Object o) {
        return ((Rank)o).score - this.score;
    }
}
