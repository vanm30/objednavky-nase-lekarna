package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class CustomerInfoController implements Initializable {

    @FXML public ChoiceBox<String> pickUpOption;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pickUpOption.getItems().add("Osobní");
        pickUpOption.getItems().add("Rozvoz");

    }

    public void back(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/orderList.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
    }

    public void addItems(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/newItemsInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Obsah Objednávky");
    }

}
