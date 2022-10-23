package cz.naseLekarna.gui.editCustomer;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AddCustomerFromList {

    private static AddCustomerFromList addCustomerFromList;

    public AddCustomerFromList() {
        addCustomerFromList = this;
    }

    public static AddCustomerFromList getAddCustomerFromList() {
        return addCustomerFromList;
    }

    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();
    FirebaseService firebaseService = new FirebaseService();

    @FXML
    public TextField name;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField street;
    @FXML
    public TextField city;

    public void addCustomer(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();
        name.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        phoneNumber.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        street.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        city.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");

        if (phoneNumber.getText().isEmpty()){
            phoneNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím zadejte telefonní číslo zákazníka.");
            fail++;
        }
        if (name.getText().isEmpty()){
            name.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím zadejte jméno zákazníka.");
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
            if (!Validator.isAlphaNumeric(city.getText())){
                city.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné město.");
                fail++;
            }
        }

        if (fail>0) {
            validator.displayError(mistakes);
            return;
        }

        //exec save and upload
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", name.getText());
        docData.put("phoneNumber", phoneNumber.getText());
        docData.put("street", street.getText());
        docData.put("city", city.getText());

        Boolean result = firebaseService.addCustomer(docData,phoneNumber.getText());

        if (result){
            mainController.mainStackPane.getChildren().clear();
            VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/lists/customerList.fxml"));
            mainController.mainStackPane.getChildren().add(vBox);
            mainController.mainLabel.setText("Pacienti");
        } else {
            phoneNumber.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Telefonní číslo je již používané.");
            validator.displayError(mistakes);
        }
    }


    public void back(ActionEvent actionEvent) throws IOException {
        mainController.mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/lists/customerList.fxml"));
        mainController.mainStackPane.getChildren().add(vBox);
        mainController.mainLabel.setText("Pacienti");
    }
}
