package cz.naseLekarna.gui.application;

import cz.naseLekarna.gui.mainMenu.HomeViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class InfoBoxController {

    private static InfoBoxController infoBoxController;

    public InfoBoxController() {
        infoBoxController = this;
    }

    public static InfoBoxController getInfoBoxController() {
        return infoBoxController;
    }

    public VBox errorBox;
    public GridPane infoBox;




    public void closeInfoBox(ActionEvent actionEvent) {
        HomeViewController.getOrderController().orders.setDisable(false);
        ((StackPane) infoBox.getParent()).getChildren().remove(infoBox);
    }
}
