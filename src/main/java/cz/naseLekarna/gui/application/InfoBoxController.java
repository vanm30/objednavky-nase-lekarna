package cz.naseLekarna.gui.application;

import cz.naseLekarna.gui.mainMenu.HomeViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class InfoBoxController {

    private static InfoBoxController infoBoxController;

    public InfoBoxController() {
        infoBoxController = this;
    }

    public static InfoBoxController getInfoBoxController() {
        return infoBoxController;
    }

    public Label infoText;
    public GridPane infoBox;




    public void closeInfoBox(ActionEvent actionEvent) {
        HomeViewController.getOrderController().orders.setDisable(false);
        ((StackPane) infoBox.getParent()).getChildren().remove(infoBox);
    }
}
