package game.general;

import game.maingames.Blackjack;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, shiftPressed, pPressed, zPressed, ePressed, mPressed, qPressed, escPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode();


        // PlayState
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_SHIFT) {
                shiftPressed = true;
            }
            if (code == KeyEvent.VK_B) {
                /*System.out.println("B Pressed");
                Blackjack play = new Blackjack();
                Stage stage = new Stage();
                stage.setTitle("Blackjack");
                play.start(stage);*/
            }



            // enter dice roll state
            if (code == KeyEvent.VK_P) {
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.diceRoll;
                }
            }

            // enter pause state
            if (code == KeyEvent.VK_ESCAPE) {
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.pauseState;
                }
            }

            if (code == KeyEvent.VK_Q) {
                qPressed = true;
                if (gp.ui.currentDialogue != null) {
                    //gp.quitDialogue();
                }
            }

            // gives ePressed = true. will initiate dialogue if player is colliding with npc
            if (code == KeyEvent.VK_E) {
                ePressed = true;
            }

            if (code == KeyEvent.VK_Q) {
                qPressed = true;
            }
            // personal debugger
            if (code == KeyEvent.VK_M) {
                mPressed = true;
            }


        }

        // pause state
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_ESCAPE){
                gp.gameState = gp.playState;
            }
        }

        // dice roll state
        else if(gp.gameState == gp.diceRoll) {
            if(code == KeyEvent.VK_E){
                ePressed = true;
                System.out.println("E Success");
                gp.stop();
                System.out.println("E pressed");
            }
        }

        // dialogue state
        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_E) {
                gp.ui.isDialogueFinished = true;
            }
        }


    } // end of key pressed

    //Dice State

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
        if (code == KeyEvent.VK_P) {
            pPressed = false;
        }
        if (code == KeyEvent.VK_Z) {
            zPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
        if (code == KeyEvent.VK_M) {
            mPressed = false;
        }


    } // end of key released

}
