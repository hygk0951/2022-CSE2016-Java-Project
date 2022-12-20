public class CardDeck {
    private int card_count;
    private Card[] deck = new Card[4 * Card.SIZE_OF_ONE_SYMBOL];

    private void createSuit(char which_symbol) {
        for (int i = 1; i <= Card.SIZE_OF_ONE_SYMBOL; i++) {
            deck[card_count] = new Card(which_symbol, i);
            card_count++;
        }
    }

    public CardDeck() {
        createSuit('S'); // 스페이드
        createSuit('H'); // 하트
        createSuit('C'); // 클로버
        createSuit('D'); // 다이아몬드
    }

    public Card newCard() {
        Card next_card = null;
        if (card_count != 0) {
            int index = (int) (Math.random() * card_count);
            next_card = deck[index];
            // 카드를 뽑은 위치부터 앞으로 당겨 준다.
            for (int i = index + 1; i < card_count; i++)
                deck[i - 1] = deck[i];
            card_count--;
//            System.out.println("남은 카드 갯수 : "+card_count);
        }
//        System.out.println(next_card.getSymbol()+" "+next_card.getNumber());
        return next_card;
    }
}