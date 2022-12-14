package cz.naseLekarna.controllers.editOrder;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 08.02.2022
 */
public class EditInfoController implements Initializable {

    private static EditInfoController editController;

    public EditInfoController() {
        editController = this;
    }

    public static EditInfoController getEditController() {
        return editController;
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
    public DatePicker dateBegin;
    @FXML
    public ChoiceBox<String> pickUpOption;
    @FXML
    public DatePicker dateEnd;
    public DatePicker datePickUp;
    @FXML
    public TextArea notes;
    @FXML
    public TextField orderNumber;
    @FXML
    public ChoiceBox<String> stateChoose;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Editace objednávky");
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);

        //options
        pickUpOption.getItems().add("Osobní");
        pickUpOption.getItems().add("Rozvoz");

        stateChoose.getItems().add("Připraveno");
        stateChoose.getItems().add("Objednáno");
        stateChoose.getItems().add("Neobjednáno");

        if (storage.editedOrder.getOrderNumber() != null) {
            orderNumber.setText(String.valueOf(storage.editedOrder.getOrderNumber()));
        }
        if (storage.editedOrder.getCustomer().getName() != null) {
            name.setText(storage.editedOrder.getCustomer().getName());
        }
        if (storage.editedOrder.getCustomer().getPhoneNumber() != null) {
            phoneNumber.setText(String.valueOf(storage.editedOrder.getCustomer().getPhoneNumber()));
        }
        if (storage.editedOrder.getCustomer().getStreet() != null) {
            street.setText(storage.editedOrder.getCustomer().getStreet());
        }
        if (storage.editedOrder.getCustomer().getCity() != null) {
            city.setText(storage.editedOrder.getCustomer().getCity());
        }
        if (storage.editedOrder.getDateBegin() != null) {
            dateBegin.setValue(storage.editedOrder.getDateBegin());
        }
        if (storage.editedOrder.getOrderPickupInfo() != null) {
            pickUpOption.setValue(storage.editedOrder.getOrderPickupInfo());
        }
        if (storage.editedOrder.getState() != null) {
            stateChoose.setValue(storage.editedOrder.getState());
        }
        if (storage.editedOrder.getDateEnd() != null) {
            dateEnd.setValue(storage.editedOrder.getDateEnd());
        }
        if (storage.editedOrder.getDatePickUp() != null) {
            datePickUp.setValue(storage.editedOrder.getDatePickUp());
        }
        if (storage.editedOrder.getNotes() != null) {
            notes.setText(storage.editedOrder.getNotes());
        }
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void saveEdit(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        String checkedPhoneNumber;
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();
        if (name.getText().isEmpty() && orderNumber.getText().isEmpty()) {
            name.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            orderNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím vyplňte alespoň jeden záznam \n v sekci \"Základní údaje\"");
            fail++;
        } else if (!orderNumber.getText().isEmpty() && !Validator.isNumeric(orderNumber.getText())) {
            orderNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("V poli \"Číslo lístku\" může být pouze číslo.");
            fail++;
        } else {
            name.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            orderNumber.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        }
        if (!phoneNumber.getText().isEmpty()) {
            if (phoneNumber.getText().length() != 9 || !Validator.isNumeric(phoneNumber.getText())) {
                phoneNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné telefonní číslo.");
                fail++;
            } else  phoneNumber.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        }
        if (!street.getText().isEmpty()){
            if (!Validator.isAlphaNumericWithSpace(street.getText())){
                street.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platnou adresu.");
                fail++;
            } else  street.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        }
        if (!city.getText().isEmpty()){
            if (!Validator.isAlphaNumeric(city.getText())){
                city.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné město.");
                fail++;
            } else  city.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        }

        if (fail>0) {
            validator.displayInfo(mistakes, true);
            return;
        }

        Map<String, Object> docData = new HashMap<>();
        if (!orderNumber.getText().isEmpty()){
            docData.put("orderNumber", orderNumber.getText());
        } else docData.put("orderNumber", null);
        if (phoneNumber.getText().isEmpty()) {
            checkedPhoneNumber = "";
        } else checkedPhoneNumber = phoneNumber.getText();

        docData.put("customer", new HashMap<String, Object>() {
                    {
                        put("name", name.getText());
                        put("phoneNumber", checkedPhoneNumber);
                        put("street", street.getText());
                        put("city", city.getText());
                    }
                }
        );
            docData.put("itemPripravekList", storage.editedOrder.getOrderedProductList());


            docData.put("itemReceptList", storage.editedOrder.getOrderedPrescriptionList());

        docData.put("dateBegin", dateBegin.getValue().toString());
        docData.put("orderPickUpInfo", pickUpOption.getValue());
        docData.put("dateEnd", dateEnd.getValue().toString());
        docData.put("datePickUp", datePickUp.getValue().toString());
        docData.put("state", stateChoose.getValue());
        docData.put("notes", notes.getText());
        firebaseService.updateOrder(docData);

        mainController.mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/views/mainMenu/homeView.fxml"));
        mainController.mainStackPane.getChildren().add(vBox);
    }

    public void finishTask(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        firebaseService.deleteOrder();
        mainController.changeScene("mainMenu/homeView");
    }


    public void editItems(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("editOrder/editItems");
    }

    public void cancelEdit(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("mainMenu/homeView");
    }
}
