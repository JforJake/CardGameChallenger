package game.general;

import game.entity.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setNPC(){
        gp.npc[0] = new NPC_Jacob(gp, 63, 11, "/NPC/JacobLeft/frame0000.png", "/NPC/JacobLeft/frame0001.png");
        gp.npc[1] = new NPC_Richard(gp, 54, 33, "/NPC/RichardLeft/frame0000.png", "/NPC/RichardLeft/frame0001.png");
        gp.npc[2] = new NPC_Uziah(gp, 54, 48, "/NPC/UziahLeft/frame0000.png", "/NPC/UziahLeft/frame0001.png");
        gp.npc[3] = new NPC_Dael(gp, 51, 60, "/NPC/DaelRight/frame0000.png", "/NPC/DaelRight/frame0001.png");
        gp.npc[4] = new NPC_Nick(gp, 41, 60, "/NPC/NickRight/frame0000.png", "/NPC/NickRight/frame0001.png");
        gp.npc[5] = new NPC_Boss(gp, 42, 78, "/NPC/defaultNPC/left1.png", "/NPC/defaultNPC/left2.png");
    }
}
