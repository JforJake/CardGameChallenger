package game.entity;

import game.general.GamePanel;
import game.maingames.BlackjackNPCAdapter;
import game.maingames.PokerNPCAdapter;
import javafx.embed.swing.JFXPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public boolean isBoss = false;

    public BufferedImage left1, left2, left3, right1, right2, right3, dice1, dice2, dice3;
    public String[] direction = {"none", "none"};
    public String horizontal;
    public boolean moving = false;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean[] collisionOn = {false, false, false, false};

    String[] dialogues = new String [20];
    int dialogueIndex = 0;

    // can be shared with all npcs. super.speak() when calling to speak method
    public int speak() {
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        if (dialogueIndex < dialogues.length - 1) {
            dialogueIndex++;
        } else {
            dialogueIndex = 0;
        }
        return dialogueIndex;
    }

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image;

        if (spriteNum == 1) {
            image = left1;
        } else {
            image = left2;
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            if (isBoss) g2.drawImage(image, screenX - gp.tileSize, screenY  - gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, null);
            else g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void update() {
        spriteCounter++;
        if (spriteCounter > 20) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void launchGame(int j, JFXPanel jfx) {
        switch(dialogueIndex) {
            case 0: // Blackjack option
                //launchBlackjack(jfx);
                break;
            case 1: // Poker option
                //launchPoker(jfx);
                break;
        }
    }

    /*private void launchBlackjack(JFXPanel jfx) {
        try {
            BlackjackNPCAdapter blackjackGame = new BlackjackNPCAdapter(gp);
            blackjackGame.startGame(jfx);
        } catch (Exception e) {
            System.out.println("Error starting Blackjack: " + e.getMessage());
        }
    }

    private void launchPoker(JFXPanel jfx) {
        try {
            PokerNPCAdapter pokerGame = new PokerNPCAdapter(gp);
            pokerGame.startGame();
        } catch (Exception e) {
            System.out.println("Error starting Poker: " + e.getMessage());
        }
    }*/
}
