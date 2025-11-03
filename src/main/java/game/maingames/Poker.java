package game.maingames;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

import static cs.main.menu.playerMoney;


public class Poker extends Application {
    private static final String[] SUITS = {"hearts", "diamonds", "clubs", "spades"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};

    private List<Card> deck;
    private List<Card> communityCards;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private int pot = 0;
    private int dealerMoney = 10;
    private int currentBet = 0;
    private int smallBlind = 5;
    private int bigBlind = 10;
    private GameState gameState = GameState.PREFLOP;

    private Label potLabel, playerMoneyLabel, dealerMoneyLabel;
    private TextArea gameLog;
    private Button checkButton, callButton, betButton, foldButton, raiseButton;
    private TextField betAmountField;
    private HBox playerCardsDisplay, dealerCardsDisplay, communityCardsDisplay;

    //
    //private int playerMoney = 100;

    enum GameState {
        PREFLOP, FLOP, TURN, RIVER, SHOWDOWN
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Texas Hold'em Poker");

        try {
            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image(Objects.requireNonNull(Poker.class.getResourceAsStream("/table_background.png")), 800, 600, false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT
            );
            StackPane rootPane = new StackPane();
            rootPane.setBackground(new Background(backgroundImage));

            initializeGame(rootPane);
            Scene scene = new Scene(rootPane, 1024, 768);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeGame(StackPane rootPane) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        potLabel = createStyledLabel("Pot: $0", "18px");
        playerMoneyLabel = createStyledLabel("Your Stack: $" + playerMoney, "16px");
        dealerMoneyLabel = createStyledLabel("Dealer Stack: $" + dealerMoney, "16px");

        dealerCardsDisplay = new HBox(10);
        dealerCardsDisplay.setAlignment(Pos.CENTER);

        communityCardsDisplay = new HBox(10);
        communityCardsDisplay.setAlignment(Pos.CENTER);

        playerCardsDisplay = new HBox(10);
        playerCardsDisplay.setAlignment(Pos.CENTER);

        initializeControls();

        gameLog = new TextArea();
        gameLog.setEditable(false);
        gameLog.setPrefHeight(150);
        gameLog.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-control-inner-background: rgba(0, 0, 0, 0.5);");

        layout.getChildren().addAll(
                dealerMoneyLabel, dealerCardsDisplay,
                potLabel, communityCardsDisplay,
                playerMoneyLabel, playerCardsDisplay,
                createControlPanel(),
                gameLog
        );

        rootPane.getChildren().add(layout);

        startNewHand();
    }

    private Label createStyledLabel(String text, String fontSize) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: " + fontSize + "; -fx-font-weight: bold;");
        return label;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 8 16;
            -fx-border-radius: 4;
            -fx-background-radius: 4;
            """, color));
        return button;
    }

    private void initializeControls() {
        checkButton = createStyledButton("Check", "#4CAF50");
        checkButton.setOnAction(e -> handleCheck());

        callButton = createStyledButton("Call", "#2196F3");
        callButton.setOnAction(e -> handleCall());

        betButton = createStyledButton("Bet", "#FF9800");
        betButton.setOnAction(e -> handleBet());

        raiseButton = createStyledButton("Raise", "#9C27B0");
        raiseButton.setOnAction(e -> handleRaise());

        foldButton = createStyledButton("Fold", "#F44336");
        foldButton.setOnAction(e -> handleFold());

        betAmountField = new TextField();
        betAmountField.setPromptText("Bet amount");
        betAmountField.setPrefWidth(100);
    }

    private HBox createControlPanel() {
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(
                checkButton, callButton, betButton, raiseButton,
                betAmountField, foldButton
        );
        return controls;
    }

    private List<Card> createDeck() {
        List<Card> newDeck = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                newDeck.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(newDeck);
        return newDeck;
    }

    private void startNewHand() {
        deck = createDeck();
        communityCards = new ArrayList<>();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        pot = 0;
        currentBet = 0;
        gameState = GameState.PREFLOP;

        playerHand.add(deck.remove(deck.size() - 1));
        playerHand.add(deck.remove(deck.size() - 1));
        dealerHand.add(deck.remove(deck.size() - 1));
        dealerHand.add(deck.remove(deck.size() - 1));

        pot += smallBlind + bigBlind;
        playerMoney -= smallBlind;
        dealerMoney -= bigBlind;
        currentBet = bigBlind;

        updateDisplay();
        updateGameLog("New hand dealt. Small blind: $" + smallBlind + ", Big blind: $" + bigBlind);
    }

    private void handleCheck() {
        if (currentBet == 0) {
            updateGameLog("You check.");
            dealerAction();
        } else {
            updateGameLog("Cannot check - there is a bet to call.");
        }
    }

    private void handleCall() {
        int callAmount = currentBet;
        if (playerMoney >= callAmount) {
            playerMoney -= callAmount;
            pot += callAmount;
            updateGameLog("You call $" + callAmount);
            updateDisplay();
            dealerAction();
        } else {
            updateGameLog("Not enough money to call!");
        }
    }

    private void handleBet() {
        try {
            int betAmount = Integer.parseInt(betAmountField.getText());
            if (betAmount <= playerMoney && betAmount >= bigBlind) {
                playerMoney -= betAmount;
                pot += betAmount;
                currentBet = betAmount;
                updateGameLog("You bet $" + betAmount);
                updateDisplay();
                dealerAction();
            } else {
                updateGameLog("Invalid bet amount!");
            }
        } catch (NumberFormatException e) {
            updateGameLog("Please enter a valid bet amount!");
        }
    }

    private void handleRaise() {
        try {
            int raiseAmount = Integer.parseInt(betAmountField.getText());
            if (raiseAmount > currentBet && raiseAmount <= playerMoney) {
                playerMoney -= raiseAmount;
                pot += raiseAmount;
                currentBet = raiseAmount;
                updateGameLog("You raise to $" + raiseAmount);
                updateDisplay();
                dealerAction();
            } else {
                updateGameLog("Invalid raise amount!");
            }
        } catch (NumberFormatException e) {
            updateGameLog("Please enter a valid raise amount!");
        }
    }

    private void handleFold() {
        updateGameLog("You fold.");
        dealerMoney += pot;
        pot = 0;
        updateDisplay();
        startNewHand();
    }

    private void dealerAction() {
        Random random = new Random();
        int action = random.nextInt(3);

        if (currentBet > 0) {
            if (action == 0) {
                updateGameLog("Dealer folds.");
                playerMoney += pot;
                pot = 0;
                updateDisplay();
                startNewHand();
                return;
            } else {
                int callAmount = currentBet;
                dealerMoney -= callAmount;
                pot += callAmount;
                updateGameLog("Dealer calls $" + callAmount);
            }
        } else {
            if (action == 2) {
                int betAmount = bigBlind * (random.nextInt(3) + 1);
                dealerMoney -= betAmount;
                pot += betAmount;
                currentBet = betAmount;
                updateGameLog("Dealer bets $" + betAmount);
            } else {
                updateGameLog("Dealer checks.");
            }
        }

        advanceGameState();
    }

    private void advanceGameState() {
        switch (gameState) {
            case PREFLOP -> {
                communityCards.add(deck.remove(deck.size() - 1));
                communityCards.add(deck.remove(deck.size() - 1));
                communityCards.add(deck.remove(deck.size() - 1));
                gameState = GameState.FLOP;
                updateGameLog("Flop dealt.");
            }
            case FLOP -> {
                communityCards.add(deck.remove(deck.size() - 1));
                gameState = GameState.TURN;
                updateGameLog("Turn dealt.");
            }
            case TURN -> {
                communityCards.add(deck.remove(deck.size() - 1));
                gameState = GameState.RIVER;
                updateGameLog("River dealt.");
            }
            case RIVER -> {
                gameState = GameState.SHOWDOWN;
                updateGameLog("Showdown!");
                evaluateWinner();
            }
            case SHOWDOWN -> startNewHand();
        }

        updateDisplay();
    }

    private void evaluateWinner() {
        List<Card> playerCombined = new ArrayList<>(playerHand);
        playerCombined.addAll(communityCards);
        List<Card> dealerCombined = new ArrayList<>(dealerHand);
        dealerCombined.addAll(communityCards);

        int playerScore = handStrength(playerCombined);
        int dealerScore = handStrength(dealerCombined);

        if (playerScore > dealerScore) {
            updateGameLog("Player wins with a stronger hand!");
            playerMoney += pot;
        } else if (dealerScore > playerScore) {
            updateGameLog("Dealer wins with a stronger hand!");
            dealerMoney += pot;
        } else {
            updateGameLog("It's a tie!");
            playerMoney += pot / 2;
            dealerMoney += pot / 2;
        }

        pot = 0;
        updateDisplay();
        startNewHand();
    }

    private int handStrength(List<Card> hand) {
        // Sort and evaluate hand ranking based on poker rules

        Map<String, Integer> rankCount = new HashMap<>();
        Map<String, Integer> suitCount = new HashMap<>();

        for (Card card : hand) {
            rankCount.put(card.rank, rankCount.getOrDefault(card.rank, 0) + 1);
            suitCount.put(card.suit, suitCount.getOrDefault(card.suit, 0) + 1);
        }

        //flush
        boolean isFlush = suitCount.values().stream().anyMatch(count -> count >= 5);

        //straight
        List<Integer> rankValues = hand.stream()
                .map(card -> getRankValue(card.rank))
                .distinct()
                .sorted()
                .toList();

        boolean isStraight = false;
        for (int i = 0; i <= rankValues.size() - 5; i++) {
            if (rankValues.get(i + 4) - rankValues.get(i) == 4) {
                isStraight = true;
                break;
            }
        }

        // Calculate hand strength score
        if (isFlush && isStraight) return 9;          // Straight Flush
        if (rankCount.containsValue(4)) return 8;     // Four of a Kind
        if (rankCount.containsValue(3) && rankCount.containsValue(2)) return 7; // Full House
        if (isFlush) return 6;                        // Flush
        if (isStraight) return 5;                     // Straight
        if (rankCount.containsValue(3)) return 4;     // Three of a Kind
        if (Collections.frequency(new ArrayList<>(rankCount.values()), 2) == 2) return 3; // Two Pair
        if (rankCount.containsValue(2)) return 2;     // One Pair
        return 1;                                     // High Card
    }

    private int getRankValue(String rank) {
        // Assign values to ranks
        switch (rank) {
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "jack": return 11;
            case "queen": return 12;
            case "king": return 13;
            case "ace": return 14;
            default: throw new IllegalArgumentException("Invalid rank: " + rank);
        }
    }

    private void updateDisplay() {
        potLabel.setText("Pot: $" + pot);
        playerMoneyLabel.setText("Your Stack: $" + playerMoney);
        dealerMoneyLabel.setText("Dealer Stack: $" + dealerMoney);

        playerCardsDisplay.getChildren().clear();
        dealerCardsDisplay.getChildren().clear();
        communityCardsDisplay.getChildren().clear();

        displayCards(playerHand, playerCardsDisplay, false);
        displayCards(dealerHand, dealerCardsDisplay, true);
        displayCards(communityCards, communityCardsDisplay, false);
    }

    private void displayCards(List<Card> cards, HBox container, boolean faceDown) {
        for (Card card : cards) {
            String imagePath = faceDown ? "/cards/card_back.png" : String.format("/cards/%s_of_%s.png", card.rank, card.suit);
            try {
                ImageView cardView = new ImageView(new Image(
                        Poker.class.getResourceAsStream(imagePath), 72, 96, true, true));
                container.getChildren().add(cardView);
            } catch (Exception e) {
                System.err.println("Error loading card image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateGameLog(String message) {
        gameLog.appendText(message + "\n");
    }

    private static class Card {
        String rank, suit;
        Card(String rank, String suit) {
            this.rank = rank;
            this.suit = suit;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
