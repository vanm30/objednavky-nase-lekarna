package cz.naseLekarna.gui;

import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Order;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class OrderListController implements Initializable {


    private static OrderListController orderListController;
    public VBox activeOrders;

    FirebaseService firebaseService = new FirebaseService();
    Storage storage = Storage.getStorage();

    public OrderListController() {
        orderListController = this;
    }

    public static OrderListController getOrderListController() {
        return orderListController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            firebaseService.loadOrders();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!storage.getActiveOrders().isEmpty()){
            List<Order> list = storage.getActiveOrders();
            for (Order order: list){
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/fxml/orderItem.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activeOrders.getChildren().add(gridPane);
                final Label name = (Label) gridPane.lookup("#name");
                final Label date = (Label) gridPane.lookup("#date");
                name.setText(order.getCustomer().getName());
                date.setText(order.getDateEnd());
            }
       }
    }

}
