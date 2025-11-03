package game.general;

import game.entity.Entity;
import game.entity.Player;
import cs.main.menu;
import game.tile.TileManager;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    /**
     *
     */
    final int originalTileSize = 64; // 64bit tiles

    public final int tileSize = (int) (originalTileSize * menu.scale); // 64 bit scaled by 1.5
    public final int screenWidth = menu.resolution[0];
    public final int screenHeight = menu.resolution[1];

    // WORLD SETTINGS
    public final int maxWorldCol = 87;
    public final int maxWorldRow = 108;

    // setup assets
    AssetSetter aSetter = new AssetSetter(this);

    // establishes UI
    public UI ui = new UI(this);

    // sets up npcs
    public Entity[] npc = new Entity[6];

    // Game States
    public final int playState = 0;
    public final int diceRoll = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public int gameState = playState;
    public Boolean gameLoopRunning = true;

    // FPS
    public int fps = menu.fpsSetting;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameTime;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setNPC();
        gameState = playState;
    }

    public void startGameTime() {
        gameTime = new Thread(this);
        gameTime.start();
    }

    public void pauseGameLoop() {
        gameLoopRunning = false;
        gameTime.stop();
    }

    public void unpauseGameLoop() {
        SwingUtilities.invokeLater(() -> {
            JFrame newFrame = new JFrame("Board Game Challenger");
            newFrame.setResizable(false);
            newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GamePanel gamePanel = new GamePanel(); // Reinitialize GamePanel
            newFrame.add(gamePanel);
            newFrame.pack();
            newFrame.setVisible(true);

            // Restart the game loop
            new Thread(gamePanel).start();
        });
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameTime != null) {
            repaint(); // draw screen with new information

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void launchJavaFXGame(int npcIndex, int gameIndex) {
        pauseGameLoop();

        SwingUtilities.invokeLater(() -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                JFXPanel jfx = new JFXPanel();
                topFrame.dispose();
                npc[npcIndex].launchGame(gameIndex, jfx);
            }
        });
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update();
                }
            }
        }
        if (gameState == dialogueState) {
            player.speak();
        }
    }

    public void stop(){
        int face = (int)(Math.random() *3) + 1;
        ui.stopDice(face);
        player.rollDice(999);
        ui.stop = false;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        update();
        tileM.draw(g2);

        //draws npcs
        for (Entity entity : npc) {
            if (entity != null) {
                entity.draw(g2);
            }
        }
        player.draw(g2);

        // UI
        try {
            ui.draw(g2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        g2.dispose();
    }
}
