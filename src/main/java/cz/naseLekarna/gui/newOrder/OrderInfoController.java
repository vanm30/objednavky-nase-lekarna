package cz.naseLekarna.gui.newOrder;


import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class OrderInfoController implements Initializable {

    private static OrderInfoController customerInfoController;

    public OrderInfoController() {
        customerInfoController = this;
    }

    public static OrderInfoController getCustomerInfoController() {
        return customerInfoController;
    }

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
    public CheckBox addToDatabase;
    @FXML
    public DatePicker dateBegin;
    @FXML
    public DatePicker dateEnd;
    @FXML
    public TextArea notes;
    @FXML
    public TextField orderNumber;
    @FXML
    public VBox errorBox;
    @FXML
    public Button forgetButton;
    @FXML
    public Button searchButton;

    Storage storage = Storage.getStorage();
    MainController mainController = MainController.getMainController();
    FirebaseService firebase = new FirebaseService();

    /**
     * This method is run when initialing this Controller. It fills ChoiceBox with following options. If user already filled some info, it will load stored info.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);
        if (storage.newOrder.isCustomerFromDb()){
            searchButton.setVisible(false);
            forgetButton.setVisible(true);
            name.setEditable(false);
        } else {
            searchButton.setVisible(true);
            forgetButton.setVisible(false);
            name.setEditable(true);
        }

        pickUpOption.getItems().add("Osobní");
        pickUpOption.getItems().add("Rozvoz");

        if (storage.newOrder.getOrderNumber() != null) {
            orderNumber.setText(String.valueOf(storage.newOrder.getOrderNumber()));
        }
        if (storage.newOrder.getCustomer().getName() != null) {
            name.setText(storage.getNewOrder().getCustomer().getName());
        }
        if (storage.newOrder.getDateBegin() != null) {
            dateBegin.setValue(storage.newOrder.getDateBegin());
        } else {
            dateBegin.setValue(LocalDate.now());
        }
        if (storage.newOrder.getDateEnd() != null) {
            dateEnd.setValue(storage.newOrder.getDateEnd());
        } else {
            dateEnd.setValue(LocalDate.now().plusDays(14));
        }
        if (storage.newOrder.getOrderPickupInfo() != null) {
            pickUpOption.setValue(storage.newOrder.getOrderPickupInfo());
        } else {
            pickUpOption.setValue("Osobní");
        }
    }

    /**
     * This method saves filled info to storage (stotage.newOrder).
     */
    public void saveOrderInfo() {
        if (!name.getText().isEmpty()) {
            storage.newOrder.getCustomer().setName(name.getText());
        } else storage.newOrder.getCustomer().setName(null);
        if (!orderNumber.getText().isEmpty()) {
            storage.newOrder.setOrderNumber(Integer.valueOf(orderNumber.getText()));
        } else storage.newOrder.setOrderNumber(null);

        storage.newOrder.setDateBegin(dateBegin.getValue());
        storage.newOrder.setDateEnd(dateEnd.getValue());
        storage.newOrder.setOrderPickupInfo(pickUpOption.getValue());
    }

    /**
     * This method calls saveOrderInfo() first. Then switches scene to Items info.
     * @param actionEvent
     * @throws IOException
     */
    public void backToItems(ActionEvent actionEvent) throws IOException {
        saveOrderInfo();
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/addItems.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
    }

    /**
     * This method first checks the form. If info is filled correctly, it saves the info and switches scene to Optional info form.
     * If the form is filled incorrectly, it highlights incorrect inputs.
     * @param actionEvent
     * @throws IOException
     */
    public void addOptionalInfo(ActionEvent actionEvent) throws IOException {
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();
        if (name.getText().isEmpty() && orderNumber.getText().isEmpty()) {
            name.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            orderNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím vyplňte alespoň jeden záznam \n v sekci \"Základní údaje\"");
            fail++;
        }else if (!orderNumber.getText().isEmpty() && !Validator.isNumeric(orderNumber.getText())) {
            orderNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("V poli \"Číslo lístku\" může být pouze číslo.");
            fail++;
        } else {
            name.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            orderNumber.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        }

        if (fail>0) {
            errorBox.getChildren().clear();
            mistakes.forEach(mistake -> {
                Label label = new Label();
                label.setText(mistake);
                errorBox.getChildren().add(label);
            });
            return;
        }

        //exec save and next page
        saveOrderInfo();
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/newOrder/optionalInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
    }
    public void searchCustomer(ActionEvent actionEvent) throws IOException {
        saveOrderInfo();
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/lists/customerList.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
    }

    public void forgetCustomer(ActionEvent actionEvent) throws IOException {
        firebase.forgetCustomerInfo();
        searchButton.setVisible(true);
        forgetButton.setVisible(false);
        name.setEditable(true);
        name.clear();

    }
}
