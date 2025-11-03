package mainmenu;

import cs.main.menu;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

//This is a function called in controllers to access to a new .fxml and then switch to that scene
public class sceneSwitch {
    public sceneSwitch(AnchorPane pane, String fxml) throws IOException {
        AnchorPane nextPane = FXMLLoader.load(Objects.requireNonNull(menu.class.getResource(fxml)));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(nextPane);
    }
}
