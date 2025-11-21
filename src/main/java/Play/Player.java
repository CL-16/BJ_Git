package Play;

import java.util.Arrays;

public class Player {
    private Card[] player_hand;
    private int card_count;
    private int tokens;

    public Player() {
        tokens = 100;
        card_count = 0;
        player_hand = new Card[52];
    }

    public void take_card(Card card) {
        player_hand[card_count] = card;
        card_count++;
        System.out.println("Player Drew: " + card);
    }

    public int calc_total() {
        int total = 0;
        int total_aces = 0;
        for (int i = 0; i < card_count; i++) {
            total += player_hand[i].get_value();
            if (player_hand[i].get_number().equalsIgnoreCase("Ace"))
                total_aces++;
        }
        while (total > 21 && total_aces > 0) {
            total -= 10;
            total_aces--;
        }
        return total;
    }

    public Card getCard(int index) {
        return player_hand[index];
    }

    public Card[] getHand() {
        return Arrays.copyOf(player_hand, card_count);
    }
}
