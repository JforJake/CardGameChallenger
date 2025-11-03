package cs.main;

import game.general.GamePanel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

//Main class to start the scene
public class menu extends Application {
    public static double scale = 1.5;
    public static int[] resolution = {1360, 768};
    public static int fpsSetting = 60;
    public static int playerMoney = 1000;
    public static BackgroundMusic bMusic;
    public static JFrame window;
    public static GamePanel panel;

    public static void main(String[] args) {
        //this makes the .java run as javaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //This will load the menu.fxml and set it as first scene
        FXMLLoader fxmlLoader = new FXMLLoader(menu.class.getResource("/cs/main/menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load());

        //sets the title of the scene and starts the menu scene
        primaryStage.setTitle("Card Challenger");
        primaryStage.setResizable(false);
        primaryStage.setScene(menuScene);
        primaryStage.show();
        bMusic = new BackgroundMusic("/soundAspects/menuMusic.wav");
        bMusic.play();
    }

    public static void updateResolution(int x, int y) {
        resolution[0] = x;
        resolution[1] = y;
    }
}
