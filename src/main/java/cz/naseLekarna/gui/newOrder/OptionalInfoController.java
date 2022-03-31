package cz.naseLekarna.gui.newOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 04.03.2022
 */
public class OptionalInfoController implements Initializable {

    private static OptionalInfoController optionalInfoController;

    public OptionalInfoController() {
        optionalInfoController = this;
    }

    public static OptionalInfoController getOptionalInfoController() {
        return optionalInfoController;
    }

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
    public TextArea notes;
    @FXML
    public VBox errorBox;
    @FXML
    public Label addToDbLabel;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();

    /**
     * This method checks if there is some info saved in storage and fills it.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (storage.newOrder.getCustomer().getName() != null) {
            name.setText(storage.newOrder.getCustomer().getName());
        }
        if (storage.newOrder.getCustomer().getPhoneNumber() != null) {
            phoneNumber.setText(String.valueOf(storage.newOrder.getCustomer().getPhoneNumber()));
        }
        if (storage.newOrder.getCustomer().getStreet() != null) {
            street.setText(storage.newOrder.getCustomer().getStreet());
        }
        if (storage.newOrder.getCustomer().getCity() != null) {
            city.setText(storage.newOrder.getCustomer().getCity());
        }
        if (storage.newOrder.getNotes() != null) {
            notes.setText(storage.newOrder.getNotes());
        }
        if (storage.user.settings.get(0).equals("1")){
            addToDatabase.setSelected(true);
        }
    }


    /**
     * This method finishes order by checking the form first, then sacing info to storage and uploading to Firestore. Then, scene is switched to home view.
     * @param actionEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void finishOrder(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();
        if (!phoneNumber.getText().isEmpty()) {
            if (phoneNumber.getText().length() != 9 || !Validator.isNumeric(phoneNumber.getText())) {
                phoneNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné telefonní číslo.");
                fail++;
            }
        }
        if (!street.getText().isEmpty()){
            if (!Validator.isAlphaNumericWithSpace(street.getText())){
                street.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platnou adresu.");
                fail++;
            }
        }
        if (!city.getText().isEmpty()){
            if (!Validator.isAlphaNumeric(city.getText())){
                city.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné město.");
                fail++;
            }
        }
        if (addToDatabase.isSelected()){
            if (name.getText().isEmpty()){
                mistakes.add("Pro přidání do databáze, zadejte jméno klieta.");
                fail++;
            }
            if (phoneNumber.getText().isEmpty()){
                mistakes.add("Pro přidání do databáze, zadejte telefonní číslo.");
                fail++;
            }
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

        //Save Info
        saveInfo();

        if (addToDatabase.isSelected()){
            storage.customer = new Customer();
            storage.customer.setName(name.getText());
            storage.customer.setPhoneNumber(Integer.valueOf((String) phoneNumber.getText()));
            if (!city.getText().isEmpty()){
                storage.customer.setCity(city.getText());
            } else storage.customer.setCity(null);
            if (!street.getText().isEmpty()) {
                storage.customer.setStreet(street.getText());
            } else storage.customer.setStreet(null);
            firebaseService.addCustomer();
        }

        //Upload order to firebase
        firebaseService.addOrder();

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");

    }

    /**
     * This method saves form info in storage and then switches to order infos scene.
     * @param actionEvent
     * @throws IOException
     */
    public void backToOrderInfo(ActionEvent actionEvent) throws IOException {
        saveInfo();
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/newOrder/orderInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
    }

    /**
     * This method saves info to storage.
     */
    public void saveInfo() {
        storage.newOrder.getCustomer().setName(name.getText());
        if (!phoneNumber.getText().isEmpty()) {
            storage.newOrder.getCustomer().setPhoneNumber(Integer.parseInt(phoneNumber.getText()));
        }
        storage.newOrder.getCustomer().setStreet(street.getText());
        storage.newOrder.getCustomer().setCity(city.getText());
        storage.newOrder.setNotes(notes.getText());
    }


    public void findCustomer(ActionEvent actionEvent) throws IOException {
        saveInfo();
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/lists/customerList.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
    }
}
