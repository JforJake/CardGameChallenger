package game.maingames;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cs.main.menu.playerMoney;

public class Blackjack extends Application {
    private static final String[] SUITS = {"hearts", "diamonds", "clubs", "spades"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};
    private static final int[] VALUES = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Label playerMoneyLabel, playerHandLabel, dealerHandLabel;
    private TextArea gameLog;
    private Button dealButton, foldButton, betButton, hitButton, standButton;
    private HBox playerCardsDisplay, dealerCardsDisplay;

    private final int betAmount = 10;
    private int playerScore = 0;
    private int dealerScore = 0;
    private boolean playerStand = false;
   // private int playerMoney = 100;

    private int npcMoney;  // Tracks NPC's money
    private boolean gameOver = false;  // Tracks game end condition
    public Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    /*public Scene start(Stage primaryStage) {
        start(primaryStage);
        return scene;
    }*/

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blackjack");
        try {
            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/table_background.png")), 800, 600, false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT
            );
            StackPane rootPane = new StackPane();
            rootPane.setBackground(new Background(backgroundImage));

            initializeGame(rootPane);
            scene = new Scene(rootPane, 800, 600);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
        }

        // Test card image
        try {
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/2_of_hearts.png")));
        } catch (Exception e) {
            System.err.println("Error loading card images. Please check resource paths.");
            e.printStackTrace();
        }
    }

    private void initializeGame(StackPane rootPane) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        playerMoneyLabel = createStyledLabel("Money: $" + playerMoney, "18px");
        playerHandLabel = createStyledLabel("Your Hand: ", "16px");
        dealerHandLabel = createStyledLabel("Dealer's Hand: ", "16px");

        gameLog = new TextArea();
        gameLog.setEditable(false);
        gameLog.setPrefHeight(150);
        gameLog.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-control-inner-background: rgba(0, 0, 0, 0.5);");

        initializeButtons();

        playerCardsDisplay = new HBox(10);
        playerCardsDisplay.setAlignment(Pos.CENTER);
        dealerCardsDisplay = new HBox(10);
        dealerCardsDisplay.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                playerMoneyLabel, playerHandLabel, playerCardsDisplay,
                dealerHandLabel, dealerCardsDisplay,
                createButtonPanel(),
                gameLog
        );

        rootPane.getChildren().add(layout);
    }

    private Label createStyledLabel(String text, String fontSize) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: " + fontSize + "; -fx-font-weight: bold; -fx-text-fill: white;");
        return label;
    }

    private void initializeButtons() {
        dealButton = createStyledButton("Deal", "#4CAF50");
        dealButton.setOnAction(e -> dealCards());

        foldButton = createStyledButton("Fold", "#F44336");
        foldButton.setOnAction(e -> foldGame());
        foldButton.setDisable(true);

        betButton = createStyledButton("Bet $" + betAmount, "#2196F3");
        betButton.setOnAction(e -> placeBet());

        hitButton = createStyledButton("Hit", "#FF9800");
        hitButton.setOnAction(e -> hitPlayer());
        hitButton.setDisable(true);

        standButton = createStyledButton("Stand", "#673AB7");
        standButton.setOnAction(e -> standPlayer());
        standButton.setDisable(true);
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
                "-fx-font-size: 16px; -fx-padding: 10px 20px; " +
                        "-fx-text-fill: white; -fx-background-color: %s;", color));
        return button;
    }

    private HBox createButtonPanel() {
        HBox buttonPanel = new HBox(10);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.getChildren().addAll(dealButton, betButton, foldButton, hitButton, standButton);
        return buttonPanel;
    }

    private void dealCards() {
        deck = createDeck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        // Deal
        playerHand.add(deck.remove(deck.size() - 1));
        dealerHand.add(deck.remove(deck.size() - 1));
        playerHand.add(deck.remove(deck.size() - 1));
        dealerHand.add(deck.remove(deck.size() - 1));

        // Display
        displayCards(playerCardsDisplay, playerHand, false);
        displayDealerInitialCards();

        updateScoresAndLabels();
        updateGameLog("Cards dealt!");
        updateButtonStates(false, true, true, true);
    }

    private void displayDealerInitialCards() {
        dealerCardsDisplay.getChildren().clear();

        // Show first card face-up
        if (!dealerHand.isEmpty()) {
            ImageView firstCardImage = createCardImageView(dealerHand.get(0), false);
            dealerCardsDisplay.getChildren().add(firstCardImage);
            playCardAnimation(firstCardImage);
        }

        // Show remaining cards face-down
        for (int i = 1; i < dealerHand.size(); i++) {
            ImageView cardImage = createCardImageView(dealerHand.get(i), true);
            dealerCardsDisplay.getChildren().add(cardImage);
            playCardAnimation(cardImage);
        }
    }

    private ImageView createCardImageView(Card card, boolean faceDown) {
        Image cardImage = getCardImage(card, faceDown);
        ImageView imageView = new ImageView(cardImage);
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
        return imageView;
    }

    private void playCardAnimation(ImageView cardImage) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), cardImage);
        translateTransition.setFromX(0);
        translateTransition.setToX(10);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), cardImage);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private List<Card> createDeck() {
        List<Card> newDeck = new ArrayList<>();
        for (String suit : SUITS) {
            for (int i = 0; i < RANKS.length; i++) {
                newDeck.add(new Card(RANKS[i], suit, VALUES[i]));
            }
        }
        Collections.shuffle(newDeck);
        return newDeck;
    }

    private Image getCardImage(Card card, boolean faceDown) {
        try {
            String imagePath;
            if (faceDown) {
                imagePath = "/cards/card_back.png";
            } else {
                imagePath = String.format("/cards/%s_of_%s.png", card.rank.toLowerCase(), card.suit.toLowerCase());
            }
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Error loading card image: " + e.getMessage());
            // Return a default or error image
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cards/card_back.png")));
        }
    }

    private void displayCards(HBox cardsDisplay, List<Card> cards, boolean faceDown) {
        cardsDisplay.getChildren().clear();
        for (Card card : cards) {
            ImageView cardImage = createCardImageView(card, faceDown);
            cardsDisplay.getChildren().add(cardImage);
            playCardAnimation(cardImage);
        }
    }

    private void foldGame() {
        updateGameLog("You folded the game.");
        playerMoney -= betAmount; // Lose the bet amount
        playerMoneyLabel.setText("Money: $" + playerMoney);
        resetGame();
    }

    private void placeBet() {
        if (playerMoney >= betAmount) {
            playerMoney -= betAmount;
            playerMoneyLabel.setText("Money: $" + playerMoney);
            updateGameLog("You bet $" + betAmount + ".");
            dealButton.setDisable(false);
            betButton.setDisable(true);
        } else {
            updateGameLog("Not enough money to place bet!");
        }
    }

    private void hitPlayer() {
        Card newCard = deck.remove(deck.size() - 1);
        playerHand.add(newCard);
        displayCards(playerCardsDisplay, playerHand, false);
        calculatePlayerScore();

        if (playerScore > 21) {
            updateGameLog("You bust with " + playerScore + "! Dealer wins.");
            dealerWins();
        }
    }

    private void standPlayer() {
        playerStand = true;
        updateGameLog("You stand with " + playerScore + ".");

        // Reveal all dealer's cards
        displayCards(dealerCardsDisplay, dealerHand, false);

        // Dealer draws cards until reaching 17 or higher
        while (dealerScore < 17) {
            Card newCard = deck.remove(deck.size() - 1);
            dealerHand.add(newCard);
            displayCards(dealerCardsDisplay, dealerHand, false);
            calculateDealerScore();
            updateGameLog("Dealer draws: " + newCard.rank + " of " + newCard.suit);
        }

        determineWinner();
    }

    private void calculatePlayerScore() {
        playerScore = calculateHandScore(playerHand);
        updatePlayerHandLabel();
    }

    private void calculateDealerScore() {
        dealerScore = calculateHandScore(dealerHand);
        updateDealerHandLabel();
    }

    private int calculateHandScore(List<Card> hand) {
        int score = 0;
        int aceCount = 0;

        for (Card card : hand) {
            score += card.value;
            if (card.rank.equalsIgnoreCase("ace")) {
                aceCount++;
            }
        }

        // Adjust for aces
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

    private void updatePlayerHandLabel() {
        playerHandLabel.setText("Your Hand: " + playerScore);
    }

    private void updateDealerHandLabel() {
        if (playerStand) {
            dealerHandLabel.setText("Dealer's Hand: " + dealerScore);
        } else {
            // Show only the value of the face-up card during gameplay
            dealerHandLabel.setText("Dealer's Hand: showing " + dealerHand.get(0).value);
        }
    }

    private void determineWinner() {
        if (playerScore > 21) {
            updateGameLog("You bust! Dealer wins.");
            dealerWins();
        } else if (dealerScore > 21) {
            updateGameLog("Dealer busts with " + dealerScore + "! You win!");
            playerWins();
        } else if (playerScore > dealerScore) {
            updateGameLog("You win with " + playerScore + " against dealer's " + dealerScore + "!");
            playerWins();
        } else if (playerScore < dealerScore) {
            updateGameLog("Dealer wins with " + dealerScore + " against your " + playerScore + ".");
            dealerWins();
        } else {
            updateGameLog("Push! It's a tie at " + playerScore + ".");
            pushGame();
        }
    }

    private void playerWins() {
        playerMoney += betAmount * 2; // Win double the bet
        playerMoneyLabel.setText("Money: $" + playerMoney);
        resetGame();
    }

    //private void dealerWins() {
       // resetGame();
    //}
    private void dealerWins() {
        npcMoney += betAmount; // NPC gains the bet amount
        if (npcMoney <= 0) {
            // Set a flag or trigger win condition
            //gp.unpauseGameLoop();
            updateGameLog("You've depleted the dealer's money! You win!");
        }
        resetGame();
    }

    private void pushGame() {
        playerMoney += betAmount; // Return the bet
        playerMoneyLabel.setText("Money: $" + playerMoney);
        resetGame();
    }

    private void resetGame() {
        playerCardsDisplay.getChildren().clear();
        dealerCardsDisplay.getChildren().clear();
        updateButtonStates(false, false, false, false);
        betButton.setDisable(false);
        playerStand = false;
        playerScore = 0;
        dealerScore = 0;
        updatePlayerHandLabel();
        updateDealerHandLabel();

        if (playerMoney <= 0) {
            updateGameLog("Game Over! You're out of money!");
            betButton.setDisable(true);
        }
    }

    private void updateScoresAndLabels() {
        calculatePlayerScore();
        calculateDealerScore();
        updatePlayerHandLabel();
        updateDealerHandLabel();
    }

    private void updateButtonStates(boolean deal, boolean fold, boolean hit, boolean stand) {
        dealButton.setDisable(!deal);
        foldButton.setDisable(!fold);
        hitButton.setDisable(!hit);
        standButton.setDisable(!stand);
    }

    private void updateGameLog(String message) {
        gameLog.appendText(message + "\n");
        gameLog.setScrollTop(Double.MAX_VALUE); // Auto-scroll to bottom
    }

    private static class Card {
        String rank;
        String suit;
        int value;

        Card(String rank, String suit, int value) {
            this.rank = rank;
            this.suit = suit;
            this.value = value;
        }

        @Override
        public String toString() {
            return rank + " of " + suit;
        }
    }
}