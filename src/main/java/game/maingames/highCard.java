package game.maingames;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static cs.main.menu.playerMoney;

public class highCard extends Application {

    private List<String> deck;
    private int playerScore = 0;
    private int botScore = 0;
    private Label playerScoreLabel;
    private Label botScoreLabel;
    private ImageView playerCardView;
    private ImageView botCardView;
    private Button drawButton;
    private Button restartButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("High Card Game");

        initializeDeck();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        HBox cardArea = new HBox(20);
        cardArea.setAlignment(Pos.CENTER);

        playerCardView = new ImageView();
        botCardView = new ImageView();
        playerCardView.setFitWidth(150);
        playerCardView.setFitHeight(218);
        botCardView.setFitWidth(150);
        botCardView.setFitHeight(218);

        cardArea.getChildren().addAll(playerCardView, botCardView);

        playerScoreLabel = new Label("Player Score: 0");
        botScoreLabel = new Label("Bot Score: 0");

        drawButton = new Button("Draw Card");
        drawButton.setOnAction(e -> playRound());

        restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartGame());

        root.getChildren().addAll(cardArea, playerScoreLabel, botScoreLabel, drawButton, restartButton);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + "_of_" + suit);
            }
        }
        Collections.shuffle(deck);
    }

    private void playRound() {
        if (deck.isEmpty()) {
            endGame();
            return;
        }

        String playerCard = drawCard();
        String botCard = drawCard();

        playerCardView.setImage(new Image(getClass().getResourceAsStream("/cards/" + playerCard + ".png")));
        botCardView.setImage(new Image(getClass().getResourceAsStream("/cards/" + botCard + ".png")));

        int comparison = compareCards(playerCard, botCard);
        if (comparison > 0) {
            playerScore++;
        } else if (comparison < 0) {
            botScore++;
        }

        updateScores();

        if (deck.isEmpty()) {
            endGame();
        }
    }

    private String drawCard() {
        return deck.remove(deck.size() - 1);
    }

    private int compareCards(String card1, String card2) {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};
        String rank1 = card1.split("_")[0];
        String rank2 = card2.split("_")[0];
        return Integer.compare(java.util.Arrays.asList(ranks).indexOf(rank1),
                java.util.Arrays.asList(ranks).indexOf(rank2));
    }

    private void updateScores() {
        playerScoreLabel.setText("Player Score: " + playerScore);
        botScoreLabel.setText("Bot Score: " + botScore);
    }

    private void endGame() {
        drawButton.setDisable(true);
        String result;
        if (playerScore > botScore) {
            result = "You win!";
            playerMoney += 10;
        } else if (playerScore < botScore) {
             result = "Bot wins!";
        } else {
            result = "It's a tie!";
        }
        playerScoreLabel.setText(result);
        botScoreLabel.setText("");
    }

    private void restartGame() {
        playerScore = 0;
        botScore = 0;
        initializeDeck();
        updateScores();

        playerCardView.setImage(null);
        botCardView.setImage(null);

        drawButton.setDisable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
