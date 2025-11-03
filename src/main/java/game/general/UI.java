package game.general;

import cs.main.menu;
import java.awt.*;

public class UI{
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    public String currentDialogue = "Test Default";
    private int x, y, width, height;
    boolean stop = false;
    public int stateCounter = 0;
    public String currStatement;
    public boolean isDialogueFinished = false;
    public int i, j;

    public UI(GamePanel gp){
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    // draws specific UI screens depending on the current game state through if conditions
    public void draw(Graphics2D g2) throws InterruptedException {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        g2.setBackground(Color.black);

        // todo: create sub-window to draw a little overlay under text and make a bit more unique
        if (gp.gameState == gp.playState) {
            g2.clearRect(15, 5, 260, 45);
            g2.drawString("Money = " + menu.playerMoney, 20, 40);
        }

        // draws dice screen through method
        if(gp.gameState == gp.diceRoll){
            drawDiceScreen();
        }

        stateCounter++;
        if (gp.gameState == gp.dialogueState) {
            if (currStatement == null && stateCounter > 50) {
                i = gp.cChecker.checkEntity(gp.player, gp.npc);
                if (i < gp.npc.length) {
                    gp.npc[i].speak();
                    currStatement = currentDialogue; // Set current statement only once
                }
                stateCounter = 0; // Reset state counter
            } else if (currStatement != null) {
                drawDialogueScreen(currStatement); // Display the dialogue
                if (isDialogueFinished) {
                    //Dialogue is finished, launching game
                    gp.gameState = gp.playState;
                    gp.launchJavaFXGame(i, j);
                    isDialogueFinished = false;
                }
            }
        }

        // draws pause screen from method
        if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
    }

    // todo: center PAUSED text and possibly add an overlay to darken screen opacity
    public void drawPauseScreen(){
        String text = "PAUSED";
        int x;
        // centers text. if needed, can be moved to another method.
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth/2 - length/2;
        int y = gp.screenHeight/2;
        g2.drawString(text, x, y);
    }

    // draws dice screen to window
    public void drawDiceScreen(){
        //Window measurements
        int x = gp.screenWidth / 3 * 2;
        int y = gp.screenHeight / 6;
        int width = gp.tileSize * 4;
        int height = gp.tileSize * 3 + gp.tileSize/8;

        drawSubWindow(x, y, width, height);
        drawDice(x, y, width + 10, height + 5);
        g2.drawString("PRESS E TO STOP", x + 15, y + 70);
        // draw dice inside window
    }

    // draw dialogue screen by setting window and drawing text over window
    public void drawDialogueScreen(String sentence) {
        // setting window
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 5);
        int height = gp.screenHeight - (gp.tileSize * 4);
        drawSubWindow(x, y, width, height);

        // displaying text
        x += gp.tileSize;
        y += gp.tileSize;

        // detects \n command and draws dialogue till \n detected. after, increments y position by 40 and draws off new
        // values

        for (String line : sentence.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawDice(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        do{
            try {
                g2.drawImage(gp.player.generateFace(), x + 150, y + 100, width / 5, height / 4,  null);
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while(stop);

    }

    public void stopDice(int face){
        stop = true;
        // execute dice logic
        try {
            switch(face){
                case 1: g2.drawImage(gp.player.dice1, this.x + 150, this.y + 100, this.width / 5, this.height / 4,  null);
                    g2.drawString("Poker", this.x + 150, this.y + 120);
                    break;

                case 2: g2.drawImage(gp.player.dice2, this.x + 150, this.y + 100, this.width / 5, this.height / 4,  null);
                    g2.drawString("Blackjack", this.x + 150, this.y + 120);
                    break;

                case 3: g2.drawImage(gp.player.dice3, this.x + 150, this.y + 100, this.width / 5, this.height / 4,  null);
                    g2.drawString("HighCard", this.x + 150, this.y + 120);
                    break;

                default:
                    break;
            }// end of switch
            Thread.sleep(3000);
            gp.gameState = gp.playState;
            //gp.openGame(face);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}