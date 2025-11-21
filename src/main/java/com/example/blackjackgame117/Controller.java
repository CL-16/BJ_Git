package com.example.blackjackgame117;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import Play.Card;
import Play.Deck;
import Play.Dealer;
import Play.Player;

import payment.Payment;
import payment.Chip;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    // UI FROM FXML
    @FXML private Pane root;
    @FXML private Button startButton;
    @FXML private Button hitButton;
    @FXML private Button standButton;
    @FXML private Label resultText;

    // CHIP BETTING BUTTONS
    @FXML private Button redChipButton;
    @FXML private Button greenChipButton;
    @FXML private Button blueChipButton;
    @FXML private Button blackChipButton;
    @FXML private Button resetButton;
    @FXML private Button exitButton;

    @FXML private Label chipsLabel;
    @FXML private Label betLabel;

    @FXML private Label playerTotal;
    @FXML private Label dealerTotal;

    // OVERLAYS
    @FXML private Pane startOverlay;
    @FXML private Button bigStartButton;

    @FXML private Pane gameOverOverlay;
    @FXML private Button gameOverButton;
    @FXML private Label betHintLabel;


    // GAME OBJECTS
    private final List<CardView> playerCards = new ArrayList<>();
    private final List<CardView> dealerCards = new ArrayList<>();

    private Deck deck;
    private Dealer dealer;
    private Player player;

    // PAYMENT SYSTEM
    private Payment payment = new Payment(1000);
    private int currentBet = 0;

    @FXML
    public void initialize() {

        // TABLE BACKGROUND
        Image bgImage = new Image(getClass().getResource(
                "/com/example/blackjackgame117/CardGameTable.png"
        ).toExternalForm());

        root.setBackground(new Background(
                new BackgroundImage(
                        bgImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, false, true)
                )));

        hitButton.setVisible(false);
        hitButton.setManaged(false);
        standButton.setVisible(false);
        standButton.setManaged(false);

        startButton.setDisable(true);

        // CHIP BUTTON IMAGES
        setupChipButton(redChipButton,   Chip.RED,   "/com/example/blackjackgame117/images/red.png");
        setupChipButton(greenChipButton, Chip.GREEN, "/com/example/blackjackgame117/images/green.png");
        setupChipButton(blueChipButton,  Chip.BLUE,  "/com/example/blackjackgame117/images/blue.png");
        setupChipButton(blackChipButton, Chip.BLACK, "/com/example/blackjackgame117/images/black.png");

        resetButton.setOnAction(e -> handleResetBet());

        updateLabels();

        // Startup overlay
        startOverlay.toFront();
        startOverlay.setVisible(true);

        // Game over overlay hidden initially
        gameOverOverlay.setVisible(false);
        startButton.setVisible(false);
        startButton.setManaged(false);
    }

    // ---------------- START OVERLAY ----------------

    @FXML
    private void onBigStartClick() {

        FadeTransition fade = new FadeTransition(Duration.millis(600), startOverlay);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setOnFinished(e -> {
            startOverlay.setVisible(false);
            startOverlay.setOpacity(1.0);
            startButton.setVisible(true);
            startButton.setManaged(true);
            exitButton.setVisible(true);
            exitButton.setManaged(true);

        });

        fade.play();
    }

    // ---------------- CHIP SYSTEM ----------------

    private void setupChipButton(Button button, Chip chip, String path) {

        Image img = new Image(getClass().getResourceAsStream(path));
        javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(img);
        iv.setFitWidth(150);
        iv.setFitHeight(150);
        iv.setPreserveRatio(true);

        button.setGraphic(iv);
        button.setStyle("-fx-background-color: transparent;");

        // Hover animation
        addHoverAnimation(button);

        button.setOnAction(e -> {
            if (payment.addChip(chip)) {

                // NEW: clamp bet so it never exceeds available points
                payment.clampBet();

                updateLabels();
                startButton.setDisable(payment.getTotalAmount() == 0);
            }
        });
    }




    private void handleResetBet() {
        payment = new Payment(payment.getavailibleCoins()); // reset bet, keep coins
        updateLabels();
        startButton.setDisable(true);
    }

    private void updateLabels() {
        chipsLabel.setText("Chips: " + payment.getavailibleCoins());
        betLabel.setText("Bet: " + payment.getTotalAmount());
    }

    // ---------------- GAME START ----------------

    @FXML
    protected void onStartButtonClick() {

        // Clear previous cards
        for (CardView c : playerCards) root.getChildren().remove(c);
        for (CardView c : dealerCards) root.getChildren().remove(c);
        playerCards.clear();
        dealerCards.clear();

        resultText.setText("");

        payment.confirmBet();
        updateLabels();

        startButton.setVisible(false);
        startButton.setManaged(false);
        exitButton.setVisible(true);
        exitButton.setManaged(true);


        redChipButton.setDisable(true);
        greenChipButton.setDisable(true);
        blueChipButton.setDisable(true);
        blackChipButton.setDisable(true);
        resetButton.setDisable(true);

        hitButton.setVisible(true);
        hitButton.setManaged(true);
        hitButton.setDisable(false);

        standButton.setVisible(true);
        standButton.setManaged(true);
        standButton.setDisable(false);

        betHintLabel.setVisible(false);
        betHintLabel.setManaged(false);


        deck = new Deck();
        deck.shuffle();
        player = new Player();
        dealer = new Dealer();

        player.take_card(deck.deal_card());
        player.take_card(deck.deal_card());
        dealer.take_card(deck.deal_card());
        dealer.take_card(deck.deal_card());

        addPlayerCard(toCardName(player.getCard(0)));
        addPlayerCard(toCardName(player.getCard(1)));
        addDealerCard(toCardName(dealer.getCard(0)));
        addDealerCard("blue_Card_Back");

        updateTotals();
    }

    // ---------------- GAMEPLAY BUTTONS ----------------

    @FXML
    protected void onHitButtonClick() {
        Card card = deck.deal_card();
        player.take_card(card);
        addPlayerCard(toCardName(card));

        updateTotals();

        if (player.calc_total() > 21) {
            hitButton.setDisable(true);
            standButton.setDisable(true);
            revealDealerHand();
            showResult();
        }
    }

    @FXML
    protected void onStandButtonClick() {
        hitButton.setDisable(true);
        standButton.setDisable(true);

        revealDealerHand();
        dealer.play(deck);

        for (int i = 2; i < dealer.getHand().length; i++) {
            addDealerCard(toCardName(dealer.getCard(i)));
        }

        updateTotals();
        showResult();
    }

    // ---------------- CARD DISPLAY ----------------

    private void addPlayerCard(String name) {

        CardView card = new CardView(name);

        double cardWidth = 100;
        double spacing = -40;
        double y = 550;

        double totalCards = playerCards.size() + 1;
        double totalWidth = (cardWidth + spacing) * (totalCards - 1) + cardWidth;
        double startX = 600 - totalWidth / 2;

        for (int i = 0; i < playerCards.size(); i++) {
            CardView c = playerCards.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), c);
            tt.setToX(startX + i * (cardWidth + spacing) - c.getLayoutX());
            tt.play();
            c.setLayoutY(y);
        }

        card.setLayoutX(startX + playerCards.size() * (cardWidth + spacing));
        card.setLayoutY(y);

        playerCards.add(card);
        root.getChildren().add(card);
    }

    private void addDealerCard(String name) {

        CardView card = new CardView(name);

        double cardWidth = 100;
        double spacing = -50;
        double y = 60;

        double totalCards = dealerCards.size() + 1;
        double totalWidth = (cardWidth + spacing) * (totalCards - 1) + cardWidth;
        double startX = 600 - totalWidth / 2;

        for (int i = 0; i < dealerCards.size(); i++) {
            CardView c = dealerCards.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), c);
            tt.setToX(startX + i * (cardWidth + spacing) - c.getLayoutX());
            tt.play();
            c.setLayoutY(y);
        }

        card.setLayoutX(startX + dealerCards.size() * (cardWidth + spacing));
        card.setLayoutY(y);

        dealerCards.add(card);
        root.getChildren().add(card);
    }

    private void revealDealerHand() {
        if (!dealerCards.isEmpty()) {
            CardView backCard = dealerCards.get(dealerCards.size() - 1);
            root.getChildren().remove(backCard);
            dealerCards.remove(backCard);
        }

        addDealerCard(toCardName(dealer.getCard(1)));
    }

    // ---------------- WIN/LOSE LOGIC ----------------

    private void showResult() {
        int p = player.calc_total();
        int d = dealer.calc_total();

        String message;

        if (p > 21) {
            message = "You Lose! (Bust)";
            payment.loseBet();
        }
        else if (d > 21) {
            message = "You Win! Dealer Busts!";
            payment.winBet(1);
        }
        else if (p > d) {
            message = "You Win!";
            payment.winBet(1);
        }
        else if (p < d) {
            message = "You Lose!";
            payment.loseBet();
        }
        else {
            message = "Push!";
            payment.pushBet();
        }

        // ⬇⬇ ADD THIS RIGHT HERE ⬇⬇
        payment.clampBet();

        resultText.setText(message);
        updateLabels();


        resultText.setText(message);
        updateLabels();

        // GAME OVER CHECK
        if (payment.getavailibleCoins() <= 0) {

            // prepare overlay
            gameOverOverlay.setOpacity(0);
            gameOverOverlay.setVisible(true);
            gameOverOverlay.toFront();

            // fade in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), gameOverOverlay);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            return;
        }


        // Enable chip betting for the next round
        redChipButton.setDisable(false);
        greenChipButton.setDisable(false);
        blueChipButton.setDisable(false);
        blackChipButton.setDisable(false);
        resetButton.setDisable(false);

        // Play again
        startButton.setText("Play Again");
        startButton.setVisible(true);
        startButton.setManaged(true);
        startButton.setDisable(false);

        betHintLabel.setVisible(true);
        betHintLabel.setManaged(true);

    }

    @FXML
    private void onGameOverClick() {

        // Fade out the overlay
        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), gameOverOverlay);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {

            // Hide overlay fully
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setOpacity(1.0);

            // Reset UI
            resultText.setText("");

            payment = new Payment(1000);
            updateLabels();

            startButton.setText("Start Game");
            startButton.setVisible(true);
            startButton.setManaged(true);

            exitButton.setVisible(true);
            exitButton.setManaged(true);

            redChipButton.setDisable(false);
            greenChipButton.setDisable(false);
            blueChipButton.setDisable(false);
            blackChipButton.setDisable(false);
            resetButton.setDisable(false);
        });

        fadeOut.play();
    }


    private void updateTotals() {
        playerTotal.setText("Player: " + player.calc_total());
        dealerTotal.setText("Dealer: " + dealer.calc_total());
    }

    private void addHoverAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.15);
            button.setScaleY(1.15);
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }


    private String toCardName(Card card) {
        return card.get_suit().toLowerCase() + "_" + card.get_number().toLowerCase();
    }

    @FXML
    private void onExitButtonClick() {
        System.exit(0);
    }

}
