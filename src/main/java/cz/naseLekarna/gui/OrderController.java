package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 18.11.2021
 */
public class OrderController implements Initializable {

    @FXML
    public VBox orderBackground;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox vBox = null;
        try {
            vBox = FXMLLoader.load(getClass().getResource("/fxml/orderList.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderBackground.getChildren().add(vBox);
    }

    /*CONSTRUCTOR, GETTERS, SETTERS*/
    private static OrderController orderController;

    public OrderController() {
        orderController = this;
    }

    public static OrderController getOrderController() {
        return orderController;
    }

    public void newOrder(ActionEvent actionEvent) throws Exception {
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/addOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Nová Objednávka");
    }
}
