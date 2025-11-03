package game.entity;

import game.general.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Dice extends Entity {

    public Dice(GamePanel gp) {
        super(gp);
    }

    public void loadDiceFaces(){
        try{
            dice1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/dice/dice1.png")));
            dice2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/dice/dice2.png")));
            dice3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/dice/dice3.png")));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}