package Play;

public class Deck {
    private Card[] cards;
    private int top_card;

    public Deck() {
        cards = new Card[52];
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] numbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10",
                "jack", "queen", "king", "ace"};

        int i = 0;
        for (String suit : suits) {
            for (String number : numbers) {
                int value;
                if (number.equals("jack") || number.equals("queen") || number.equals("king")) value = 10;
                else if (number.equals("ace")) value = 11;
                else value = Integer.parseInt(number);

                cards[i] = new Card(suit, number, value);
                i++;
            }
        }
    }

    public void shuffle() {
        for (int i = 0; i < cards.length; i++) {
            int random = (int) (Math.random() * (i + 1));
            Card temp = cards[i];
            cards[i] = cards[random];
            cards[random] = temp;
        }
    }

    public Card deal_card() {
        Card card = cards[top_card];
        top_card++;
        return card;
    }
}
