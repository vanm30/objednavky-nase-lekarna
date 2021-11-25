package cz.naseLekarna.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class OrderListController implements Initializable {


    private static OrderListController orderListController;

    public OrderListController() {
        orderListController = this;
    }

    public static OrderListController getOrderListController() {
        return orderListController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadList (){
        VBox vBox = null;
        try {
            vBox = FXMLLoader.load(getClass().getResource("/fxml/orderList.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        OrderController.getOrderController().orderBackground.getChildren().add(vBox);
    }

}
