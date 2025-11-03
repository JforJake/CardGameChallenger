package mainmenu.controller;

import cs.main.menu;
import game.maingames.Blackjack;
import game.maingames.Poker;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import mainmenu.sceneSwitch;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.scene.control.Slider;

public class sceneSettingsController implements Initializable {

    @FXML
    public ChoiceBox<String> cRes;
    public ChoiceBox<String> cFPS;
    String[] resOptions = {"1920 x 1000", "1600 x 800", "1360 x 768", "1280 x 720", "800 x 600"};
    String[] fpsOptions = {"30", "60", "75", "120", "144"};
    public Slider volSlider = new Slider(0, 80, menu.bMusic.volume);

    @FXML
    private AnchorPane settingsScenePane;

    @FXML
    void gameMenuSwitch() throws IOException {
        new sceneSwitch(settingsScenePane, "menu.fxml");
    }

    @FXML
    void getRes() {
        String res = cRes.getSelectionModel().getSelectedItem();
        String[] parts = res.split(" x ");
        int giveX = Integer.parseInt(parts[0].trim());
        int giveY = Integer.parseInt(parts[1].trim());

        // Update resolution in menu
        menu.updateResolution(giveX, giveY);
    }

    @FXML
    void getFPS() {
        menu.fpsSetting = Integer.parseInt(cFPS.getSelectionModel().getSelectedItem());
    }

    @FXML
    void getVol() {
        menu.bMusic.changeVolume((int) volSlider.getValue());
    }

    @FXML
    void runBlackJack() {
        Stage stage = new Stage();
        Blackjack blackjack = new Blackjack();
        blackjack.start(stage);
    }

    @FXML
    void runPoker() {
        Stage stage = new Stage();
        Poker poker = new Poker();
        poker.start(stage);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cRes.getItems().addAll(resOptions);
        cRes.setValue(menu.resolution[0] + " x " + menu.resolution[1]);
        cFPS.getItems().addAll(fpsOptions);
        cFPS.setValue(menu.fpsSetting + "");
        volSlider.setValue(menu.bMusic.volume * 100);
    }
}