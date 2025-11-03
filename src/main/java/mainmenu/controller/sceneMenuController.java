package mainmenu.controller;

import game.general.GamePanel;
import cs.main.menu;
import mainmenu.sceneSwitch;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class sceneMenuController{

    // AnchorPane object called menuScenePane to have access to it when called the sceneSwitch function
    @FXML
    private AnchorPane menuScenePane;

    public JFrame window;

    //gameScene function once the "Start" button is pressed
    @FXML
    void gameScene() {
        SwingUtilities.invokeLater(() -> {
            window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("Board Game Challenger");
            GamePanel panel = new GamePanel();

            window.add(panel);

            window.pack();

            window.setLocationRelativeTo(null);
            window.setVisible(true);

            panel.setupGame();
            panel.startGameTime();
        });

        menu.bMusic.changeSong("/soundAspects/inGameMusic.wav");

        Stage stage = (Stage) menuScenePane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void how2play() throws IOException {
        new sceneSwitch(menuScenePane, "/cs/main/how2play.fxml");
    }
    //Function to go to the settings scene
    @FXML
    void settingsScene() throws IOException {
        new sceneSwitch(menuScenePane, "/cs/main/settings.fxml");
    }

    //Function to go to the credits scene
    @FXML
    void creditsScene() throws IOException {
        new sceneSwitch(menuScenePane, "/cs/main/credits.fxml");
    }

    //Once the quit button is pressed it will close the program and write on the command console "Program closed"
    @FXML
    void closeProgram() {
        System.out.println("Program closed");
        Stage stage = (Stage) menuScenePane.getScene().getWindow();
        stage.close();
    }
}
