package cz.naseLekarna.controllers.mainMenu;

import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.Order;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 18.11.2021
 */
public class HomeViewController implements Initializable {

    private static HomeViewController openNewOrder;

    public HomeViewController() {
        openNewOrder = this;
    }

    public static HomeViewController getOrderController() {
        return openNewOrder;
    }

    @FXML
    public VBox orderBackground;
    @FXML
    public VBox orders;

    Storage storage = Storage.getStorage();
    MainController mainController = MainController.getMainController();

    /**
     * When Home view is opened, it also opens order list.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Aktivní objednávky");
        //set visibility
        mainController.searchButton.setVisible(true);
        mainController.searchBar.setVisible(false);

        StackPane stackPane = null;
        try {
            stackPane = FXMLLoader.load(getClass().getResource("/views/lists/orderList.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderBackground.getChildren().add(stackPane);
    }

    /**
     * Method takes user to new order creation
     *
     * @param actionEvent
     * @throws Exception
     */
    public void newOrder(ActionEvent actionEvent) throws Exception {
        storage.newOrder = new Order();
        storage.newOrder.setCustomer(new Customer());
        mainController.changeScene("newOrder/addItems");
    }
}
