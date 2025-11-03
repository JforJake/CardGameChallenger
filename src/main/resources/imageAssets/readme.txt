Any resources such as .fxml, images, .css files or anything that is used on the main needs to be
stored in the ../resources file because this file has privilege access from java fx to
let the code read files in the computer

The .fxml files are generated from scene Builder and they need to access their own controller with the following
command: fx:controller="com.main.mainmenu.menu.controller.sceneCreditsController" which is set up at the end
of <AnchorPane ... >

The CSSStyles file is where our .css files are stored to set up same style for all buttons