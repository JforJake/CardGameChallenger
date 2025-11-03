package game.maingames;

import game.general.GamePanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BlackjackNPCAdapter {
    private boolean npcOutOfMoney = false;
    private final GamePanel gp;
    private final int npcMoney;

    public BlackjackNPCAdapter(GamePanel gp, int initialNPCMoney) {
        this.gp = gp;
        this.npcMoney = initialNPCMoney;
    }

    public int getNPCRemainingMoney() {
        // Calculate and return remaining money after the game
        return npcMoney;
    }

    public void startGame(JFXPanel jfx) {
        // Run the JavaFX application on the JavaFX Application Thread
        System.out.println("Blackjack Start Called");
        System.out.println("JFXPanel Initialized");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runlater ran");
                //initFX(jfx);
            }
        });
        if (npcMoney <= 0) {
            npcOutOfMoney = true;
        }
    }

    public boolean isNPCOutOfMoney() {
        return npcOutOfMoney;
    }
}
