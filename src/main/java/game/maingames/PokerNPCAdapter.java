package game.maingames;

import game.general.GamePanel;
import javafx.application.Platform;
import javafx.stage.Stage;

public class PokerNPCAdapter {
    private GamePanel gp;

    public PokerNPCAdapter(GamePanel gp) {
        this.gp = gp;
    }

    //
    private int npcMoney;

    public PokerNPCAdapter(GamePanel gp, int initialNPCMoney) {
        this.npcMoney = initialNPCMoney;
        // Rest of constructor
    }

    public int getNPCRemainingMoney() {
        // Calculate and return remaining money after the game
        return npcMoney;
    }



    //

    public void startGame() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            Poker pokerGame = new Poker(); // Assuming similar structure to Blackjack
            try {
                pokerGame.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}