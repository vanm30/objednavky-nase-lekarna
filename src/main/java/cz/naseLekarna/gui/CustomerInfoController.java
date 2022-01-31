package cz.naseLekarna.gui;


import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class CustomerInfoController implements Initializable {

    private static CustomerInfoController customerInfoController;

    @FXML
    public ChoiceBox<String> pickUpOption;
    @FXML
    public TextField name;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField street;
    @FXML
    public TextField city;
    @FXML
    public TextField zip;
    @FXML
    public CheckBox addToDatabase;
    @FXML
    public DatePicker dateBegin;
    @FXML
    public DatePicker dateEnd;
    @FXML
    public TextArea notes;

    public Customer customer;
    public Order order;

    FirebaseService firebaseService = new FirebaseService();

    public CustomerInfoController() {
        customerInfoController = this;
    }

    public static CustomerInfoController getCustomerInfoController() {
        return customerInfoController;
    }

    /**
     * This method is run when initialing this Controller. It fills ChoiceBox with following options.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pickUpOption.getItems().add("Osobní");
        pickUpOption.getItems().add("Rozvoz");

    }

    /**
     * Method is called when back button is pressed. This method moves user to homeView.
     *
     * @param actionEvent
     * @throws Exception
     */
    public void backToAddOrder(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/addOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Nová Objednávka");
    }

    /**
     * This method switches view to newItemsInfo view.
     *
     * @param actionEvent
     * @throws Exception
     */
    public void addItems(ActionEvent actionEvent) throws Exception {
        //Save user
        customer = new Customer(
                name.getText(),
                Integer.parseInt(phoneNumber.getText()),
                street.getText(),
                city.getText(),
                Integer.parseInt(zip.getText()));
        if (addToDatabase.isSelected()) {
            customer.setSave(true);
        }

        //Save Order
        order = new Order(
                customer, dateBegin.getValue().toString(), pickUpOption.getValue(), dateEnd.getValue().toString(), notes.getText()
                );


        // New Scene
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/newItemsInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Obsah Objednávky");
    }

}
