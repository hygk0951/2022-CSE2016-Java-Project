public class Card {
    public static int SIZE_OF_ONE_SYMBOL = 13;
    private char symbol;    // S 스페이드, H 하트, D 다이아몬드, C 클로버
    private int number;     // 숫자 = 1~10, J(11), Q(12), K(13), A = 1 or 11
    public Card(char symbol, int number){
        this.symbol = symbol;
        this.number = number;
    }
    public Card(char symbol, char number){
        this.symbol = symbol;
        if(number != 'A')
            this.number = 10;
    }
    public char getSymbol() {
        return symbol;
    }
    public int getNumber() {
        return number;
    }
    public void drawCard() {

    }
}