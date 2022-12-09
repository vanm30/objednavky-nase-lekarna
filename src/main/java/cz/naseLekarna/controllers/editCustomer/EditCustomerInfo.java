package cz.naseLekarna.controllers.editCustomer;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class EditCustomerInfo implements Initializable {

    private static EditCustomerInfo editCustomerInfo;

    public EditCustomerInfo() {
        editCustomerInfo = this;
    }

    public static EditCustomerInfo getEditCustomerInfo() {
        return editCustomerInfo;
    }

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();

    @FXML
    public TextField name;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField street;
    @FXML
    public TextField city;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Editace Pacienta");
        if (storage.editedCustomer.getName() != null) {
            name.setText(String.valueOf(storage.editedCustomer.getName()));
        }
        if (storage.editedCustomer.getPhoneNumber() != null) {
            phoneNumber.setText(String.valueOf(storage.editedCustomer.getPhoneNumber()));
        }
        if (storage.editedCustomer.getStreet() != null) {
            street.setText(String.valueOf(storage.editedCustomer.getStreet()));
        }
        if (storage.editedCustomer.getCity() != null) {
            city.setText(String.valueOf(storage.editedCustomer.getCity()));
        }
    }

    public void save(ActionEvent actionEvent) throws ExecutionException, InterruptedException, IOException {
        //Form check
        name.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        phoneNumber.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        city.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        street.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();
        if (name.getText().isEmpty()){
            name.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím zadejte jméno zákazníka.");
            fail++;
        }
        if (phoneNumber.getText().isEmpty()){
            phoneNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím zadejte telefonní číslo zákazníka.");
            fail++;
        }
        if (street.getText().isEmpty()){
            street.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím zadejte ulici bydliště zákazníka.");
            fail++;
        }
        if (city.getText().isEmpty()){
            city.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím zadejte město zákazníka.");
            fail++;
        }

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
            if (!Validator.isAlphaNumericWithSpace(city.getText())){
                city.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné město.");
                fail++;
            }
        }
        if (!phoneNumber.getText().equals(storage.editedCustomer.getPhoneNumber()) &&  firebaseService.checkPhoneNumber(phoneNumber.getText())){
            mistakes.add("Zadané telefonní číslo již využívá jiný zákazník.");
            fail++;
        }

        if (fail>0) {
            validator.displayInfo(mistakes, true);
            return;
        }

        Map<String, Object> docData = new HashMap<>();
        docData.put("name", name.getText());
        docData.put("phoneNumber", phoneNumber.getText());
        docData.put("street", street.getText());
        docData.put("city", city.getText());

        firebaseService.deleteCustomer();
        firebaseService.addCustomer(docData, phoneNumber.getText());

        mainController.changeScene("lists/customerList");
        System.out.println("yo");
    }

    public void back(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("lists/customerList");
    }

    public void deleteCustomer(ActionEvent actionEvent) throws ExecutionException, InterruptedException, IOException {
        firebaseService.deleteCustomer();
        mainController.changeScene("lists/customerList");
    }
}
