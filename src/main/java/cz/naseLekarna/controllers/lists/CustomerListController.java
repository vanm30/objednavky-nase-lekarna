package cz.naseLekarna.controllers.lists;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class CustomerListController implements Initializable {

    private static CustomerListController customerListController;

    public CustomerListController() {
        customerListController = this;
    }

    public static CustomerListController getCustomerListController() {
        return customerListController;
    }


    @FXML
    public VBox customers;
    @FXML
    public ScrollPane scrollPane;

    FirebaseService firebaseService = new FirebaseService();
    Storage storage = Storage.getStorage();
    MainController mainController = MainController.getMainController();

    /**
     * When initialized, load and list all saved customers
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Vyhledat zákazníka");
        //set visibility
        mainController.searchButton.setVisible(true);
        mainController.searchBar.setVisible(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        loadCustomers();
    }

    public void loadCustomers(){
        customers.getChildren().clear();
        try {
            firebaseService.loadCustomers();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (!storage.getActiveCustomers().isEmpty()) {
            List<Customer> list = storage.getActiveCustomers();

            for (Customer customer : list) {
                VBox vBox = null;
                try {
                    vBox = FXMLLoader.load(getClass().getResource("/views/newOrder/customer.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                customers.getChildren().add(vBox);
                final Label name = (Label) vBox.lookup("#name");
                final Label phoneNumber = (Label) vBox.lookup("#phoneNumber");
                final Label address = (Label) vBox.lookup("#address");
                String city = null;
                String street = null;
                if (customer.getCity() == null) city = " - ";
                else city = customer.getCity();
                if (customer.getStreet() == null) street = " - ";
                else street = customer.getStreet();
                name.setText(customer.getName());
                phoneNumber.setText(String.valueOf(customer.getPhoneNumber()));
                address.setText(street + ", " + city);
            }
        } else{
            Label label = new Label();
            label.setText("Žádní zákazníci");
            label.setId("empty");
            customers.getChildren().add(label);
        }
    }

    public void searchCustomers(Set<Customer> searched){
        customers.getChildren().clear();
        if (searched != null){
            for (Customer customer : searched) {
                VBox vBox = null;
                try {
                    vBox = FXMLLoader.load(getClass().getResource("/views/newOrder/customer.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                customers.getChildren().add(vBox);
                final Label name = (Label) vBox.lookup("#name");
                final Label phoneNumber = (Label) vBox.lookup("#phoneNumber");
                final Label address = (Label) vBox.lookup("#address");
                String city = null;
                String street = null;
                if (customer.getCity() == null) city = " - ";
                else city = customer.getCity();
                if (customer.getStreet() == null) street = " - ";
                else street = customer.getStreet();
                name.setText(customer.getName());
                phoneNumber.setText(String.valueOf(customer.getPhoneNumber()));
                address.setText(street + ", " + city);
            }
        }
    }

    public void addCustomer(ActionEvent actionEvent) throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getResource("/views/editCustomer/AddCustomer.fxml"));
        mainController.changeScene("editCustomer/AddCustomer");
    }
}
