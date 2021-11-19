package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author Matěj Vaník
 * @created 18.11.2021
 */
public class OrderController {



    public void back(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/orderList.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
    }
    public void newOrder() throws Exception{
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/addOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
    }



    public void createOrder(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/newOrderInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
    }

    public void fromDatabase(ActionEvent actionEvent) {
        System.out.println("a");
    }
}
