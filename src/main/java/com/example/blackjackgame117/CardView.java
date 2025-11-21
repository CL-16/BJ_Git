package com.example.blackjackgame117;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a single card image displayed on the table.
 */
public class CardView extends ImageView {
    private String cardName;

    public CardView(String cardName) {
        this.cardName = cardName;
        Image cardImage = new Image(getClass().getResource(
                "/com/example/blackjackgame117/Card_Deck/" + cardName + ".png"
        ).toExternalForm());
        setImage(cardImage);
        setFitWidth(100);
        setFitHeight(150);
    }

    public void updateCard(String newCardName) {
        this.cardName = newCardName;
        Image image = new Image(getClass().getResource(
                "/com/example/blackjackgame117/Card_Deck/" + newCardName + ".png"
        ).toExternalForm());
        setImage(image);
    }

    public String getCardName() {
        return cardName;
    }
}
