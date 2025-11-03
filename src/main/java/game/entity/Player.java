package game.entity;

import game.general.GamePanel;
import game.general.KeyHandler;
import game.general.UI;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    KeyHandler keyH;
    UI ui;

    private final int baseSpeed = 10;

    public final int screenX;
    public final int screenY;

    private Boolean firstMove;
    public int npcIndex;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        System.out.println(screenX + " " + screenY);

        solidArea = new Rectangle();
        solidArea.setLocation(new Point(30, 65));
        solidArea.setSize(new Dimension(35, 25));

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        //System.out.println("Point of Rectangle: " + solidArea.getLocation() + "\nSize of Rectangle: " + solidArea.getSize());

        firstMove = true;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        gp.gameState = gp.playState;
        worldX = gp.tileSize * 14;
        worldY = gp.tileSize * 11;
        speed = baseSpeed;
        direction[1] = "right";
        horizontal = "right";
    }



    public void getPlayerImage() {
        try {
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Right/frame0000.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Right/frame0001.png")));
            right3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Right/frame0002.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Left/frame0000.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Left/frame0001.png")));
            left3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Left/frame0002.png")));
            dice1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/dice/dice1.png")));
            dice2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/dice/dice2.png")));
            dice3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/dice/dice3.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed) {
            direction[0] = "up";
        }
        if (keyH.leftPressed) {
            direction[1] = "left";
            horizontal = "left";
        }
        if (keyH.downPressed) {
            direction[0] = "down";
        }
        if (keyH.rightPressed) {
            direction[1] = "right";
            horizontal = "right";
        }
        if (keyH.shiftPressed) {
            speed = (int) (1.5 * baseSpeed);
        }
        if (!(keyH.shiftPressed)) {
            speed = baseSpeed;
        }
        if (keyH.upPressed && !keyH.downPressed) {
            direction[0] = "up";
        }
        if (keyH.leftPressed && !keyH.rightPressed) {
            direction[1] = "left";
            horizontal = "left";
        }
        if (keyH.downPressed && !keyH.upPressed) {
            direction[0] = "down";
        }
        if (keyH.rightPressed && !keyH.leftPressed) {
            direction[1] = "right";
            horizontal = "right";
        }
        if (!keyH.downPressed && !keyH.upPressed) {
            direction[0] = "none";
        }
        if (!keyH.rightPressed && !keyH.leftPressed) {
            direction[1] = "none";
        }
        if (keyH.pPressed) {
            rollDice(1);
        }

        // personal debugger to show location in world
        if(keyH.mPressed){
            System.out.println(worldX + " " + worldY);
        }


        // Check tile collision
        collisionOn[0] = false;
        collisionOn[1] = false;
        collisionOn[2] = false;
        collisionOn[3] = false;
        gp.cChecker.checkTile(this);

        // check npc collision
        npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        if (keyH.ePressed && npcIndex < gp.npc.length) interactNPC(npcIndex);

        // If  collisions false, player can move
        if (direction[0].compareTo("none") != 0 && direction[1].compareTo("none") != 0 ) {
            speed = (int) (speed * 0.8);
        }
        if (!(collisionOn[0]) && direction[0].compareTo("up") == 0) {
            worldY -= speed;
            moving = true;
        }
        if (!(collisionOn[1]) && direction[1].compareTo("right") == 0) {
            worldX += speed;
            moving = true;
        }
        if (!(collisionOn[2]) && direction[0].compareTo("down") == 0) {
            worldY += speed;
            moving = true;
        }
        if (!(collisionOn[3]) && direction[1].compareTo("left") == 0) {
            worldX -= speed;
            moving = true;
        }
        if (direction[0].compareTo("none") == 0 && direction[1].compareTo("none") == 0) {
            moving = false;
        }

        spriteCounter++;
        if (spriteCounter > 4) {
            if (moving) {
                if (spriteNum == 1 && firstMove) {
                    spriteNum = 2;
                    firstMove = false;
                } else if (spriteNum == 1) {
                    spriteNum = 3;
                    firstMove = true;
                } else {
                    spriteNum = 1;
                }
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public BufferedImage generateFace(){
        int face = (int)(Math.random() *3) + 1;
        BufferedImage diceFace = switch (face){
            case 1 -> dice1;
            case 2 -> dice2;
            case 3 -> dice3;
            default -> null;
        };
        return diceFace;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (horizontal) {
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public void rollDice(int i){
        if(i != 999)
            gp.gameState = gp.diceRoll;
    }

    public void interactNPC (int i){
        if (i != 999) {
            gp.gameState = gp.dialogueState;
            gp.npc[i].speak();
        }
    }
}
