package Play;

import java.util.Arrays;

public class Dealer {
    private int card_count;
    private Card[] dealer_hand;

    public Dealer() {
        card_count = 0;
        dealer_hand = new Card[11];
    }

    public void take_card(Card card) {
        dealer_hand[card_count] = card;
        card_count++;
        System.out.println("Dealer Drew: " + card);
    }

    public int calc_total() {
        int total = 0;
        int total_aces = 0;
        for (int i = 0; i < card_count; i++) {
            total += dealer_hand[i].get_value();
            if (dealer_hand[i].get_number().equalsIgnoreCase("Ace"))
                total_aces++;
        }
        while (total > 21 && total_aces > 0) {
            total -= 10;
            total_aces--;
        }
        return total;
    }

    public void play(Deck deck) {
        while (calc_total() < 17) {
            take_card(deck.deal_card());
        }
    }

    public Card getCard(int index) {
        return dealer_hand[index];
    }

    public Card[] getHand() {
        return Arrays.copyOf(dealer_hand, card_count);
    }
}
