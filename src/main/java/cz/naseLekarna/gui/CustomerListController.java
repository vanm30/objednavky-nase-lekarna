package cz.naseLekarna.gui;

import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 07.02.2022
 */
public class CustomerListController implements Initializable {

    private static CustomerListController customerListController;

    @FXML
    public VBox customers;

    FirebaseService firebaseService = new FirebaseService();
    Storage storage = Storage.getStorage();

    public CustomerListController() {
        customerListController = this;
    }

    public static CustomerListController getCustomerListController() {
        return customerListController;
    }

    /**
     * When initialized, load and list all saved customers
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            firebaseService.loadUsers();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!storage.getActiveCustomers().isEmpty()) {
            List<Customer> list = storage.getActiveCustomers();

            for (Customer customer : list) {
                VBox vBox = null;
                try {
                    vBox = FXMLLoader.load(getClass().getResource("/fxml/customer.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                customers.getChildren().add(vBox);
                final Label name = (Label) vBox.lookup("#name");
                final Label phoneNumber = (Label) vBox.lookup("#phoneNumber");
                final Label address = (Label) vBox.lookup("#address");
                name.setText(customer.getName());
                phoneNumber.setText(String.valueOf(customer.getPhoneNumber()));
                address.setText(customer.getStreet() + ", " + customer.getCity());

            }
        }
    }

    /**
     * Back button.
     * @param actionEvent Takes user back.
     * @throws IOException
     */
    public void backToNewOrder(ActionEvent actionEvent) throws IOException {
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/addOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Nová Objednávka");
    }
}
