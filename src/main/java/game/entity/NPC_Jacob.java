package game.entity;

import game.general.GamePanel;
import game.maingames.BlackjackNPCAdapter;
import game.maingames.Poker;
import game.maingames.PokerNPCAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class NPC_Jacob extends Entity {
    private String npcPathway1;
    private String npcPathway2;

    public NPC_Jacob(GamePanel gp, int x, int y, String npc1, String npc2) {
        super(gp);
        this.gp = gp;

        worldX = x * gp.tileSize;
        worldY = y * gp.tileSize;

        changeNpcImage(npc1, npc2);
        getNpcImage();
        setDialogue();
    }

    public void changeNpcImage(String npc1, String npc2) {
        npcPathway1 = npc1;
        npcPathway2 = npc2;
    }

    public void setDialogue() {
        dialogues = new String[2];
        dialogues[0] = "Wanna play Blackjack?";
        dialogues[1] = "Feel like a game of Poker?";
    }

    public void getNpcImage() {
        try {
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(npcPathway1)));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(npcPathway2)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}