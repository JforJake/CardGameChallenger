package game.entity;

import game.general.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class NPC_Boss extends Entity {
    private String npcPathway1;
    private String npcPathway2;
    private int money = 100000;

    public NPC_Boss(GamePanel gp, int x, int y, String npc1, String npc2) {
        super(gp);

        worldX = x * gp.tileSize;
        worldY = y * gp.tileSize;

        System.out.println(worldX + " " + worldY);
        isBoss = true;

        changeNpcImage(npc1, npc2);
        getNpcImage();
        setDialogue();
    }

    public void changeNpcImage(String npc1, String npc2) {
        npcPathway1 = npc1;
        npcPathway2 = npc2;
    }

    // sets dialogue setting for specified NPC
    public void setDialogue(){
        dialogues[0] = "Final Gamba?";
    }

    public void getNpcImage() {

        try {
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(npcPathway1)));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(npcPathway2)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int speak(){
        gp.ui.currentDialogue = dialogues[0];
        System.out.println("Boss Money: $" + money);
        return dialogueIndex;
    }

}
