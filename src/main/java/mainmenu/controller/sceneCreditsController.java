package mainmenu.controller;

import mainmenu.sceneSwitch;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class sceneCreditsController {

    //Anchor pane object to get access to credits scene from sceneSwitch function
    @FXML
    private AnchorPane creditsScenePane;

    // goBack function, it goes back to menu.fxml when is pressed
    @FXML
    void goBack() throws IOException{
        new sceneSwitch(creditsScenePane, "menu.fxml");
    }

}

