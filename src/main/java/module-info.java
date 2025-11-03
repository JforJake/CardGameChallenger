module mainmenu {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.swing;


    opens mainmenu to javafx.fxml;
    exports mainmenu;
    exports mainmenu.controller;
    opens mainmenu.controller to javafx.fxml;
    exports cs.main;
    opens cs.main to javafx.fxml;
    exports game.general;
    exports game.entity;
    exports game.tile;
    exports game.maingames;
}