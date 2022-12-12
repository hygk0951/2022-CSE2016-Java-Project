public class Main {
    public static void main(String[] args) {
        CardDeck myCardDeck = new CardDeck();

        for(int i=0; i<52; i++){
            Card selected = myCardDeck.newCard();
            System.out.print(selected.getSymbol());
            System.out.println(selected.getNumber());
        }

        Frame GameView = new Frame();
    }
}