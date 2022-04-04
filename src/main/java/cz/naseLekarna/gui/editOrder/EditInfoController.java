package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
    public ChoiceBox pickUpOption;
    @FXML
    public DatePicker dateEnd;
    @FXML
    public TextArea notes;
    @FXML
    public TextField orderNumber;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);


        pickUpOption.getItems().add("Osobní");
        pickUpOption.getItems().add("Rozvoz");

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
        if (storage.editedOrder.getDateEnd() != null) {
            dateEnd.setValue(storage.editedOrder.getDateEnd());
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

        Integer checkedPhoneNumber;

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
        } else  phoneNumber.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        if (!street.getText().isEmpty()){
            if (!Validator.isAlphaNumeric(street.getText())){
                street.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platnou adresu.");
                fail++;
            } else  street.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        } else  street.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        if (!city.getText().isEmpty()){
            if (!Validator.isAlphaNumeric(city.getText())){
                city.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                mistakes.add("Musíte zadat platné město.");
                fail++;
            } else  city.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        } else  city.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        if (fail>0) {
            return;
        }

        Map<String, Object> docData = new HashMap<>();
        if (!orderNumber.getText().isEmpty()){
            docData.put("orderNumber", orderNumber.getText());
        } else docData.put("orderNumber", null);
        if (phoneNumber.getText().isEmpty()) {
            checkedPhoneNumber = null;
        } else checkedPhoneNumber = Integer.parseInt(String.valueOf(phoneNumber.getText()));
        docData.put("customer", Arrays.asList(
                name.getText(),
                checkedPhoneNumber,
                street.getText(),
                city.getText()
        ));
        if (!storage.editedOrder.orderedPripravekList.isEmpty()) {
            docData.put("itemPripravekList", storage.editedOrder.getOrderedPripravekList());
        }
        if (!storage.editedOrder.orderedReceptList.isEmpty()) {
            docData.put("itemReceptList", storage.editedOrder.getOrderedReceptList());
        }
        docData.put("dateBegin", dateBegin.getValue().toString());
        docData.put("orderPickUpInfo", pickUpOption.getValue());
        docData.put("dateEnd", dateEnd.getValue().toString());
        docData.put("notes", notes.getText());


        firebaseService.updateOrder(docData);

        mainController.mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        mainController.mainStackPane.getChildren().add(vBox);
        mainController.mainLabel.setText("Aktivní objednávky");
    }

    public void finishTask(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        firebaseService.deleteOrder();

        mainController.mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        mainController.mainStackPane.getChildren().add(vBox);
        mainController.mainLabel.setText("Aktivní objednávky");
    }


    public void editItems(ActionEvent actionEvent) throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/editOrder/editItems.fxml"));
        mainController.mainStackPane.getChildren().clear();
        mainController.mainStackPane.getChildren().add(vBox);
        mainController.mainLabel.setText("Editace položek");

        if (storage.editedOrder.orderedPripravekList.size() > 0) {
            for (int i = 0; i < storage.editedOrder.orderedPripravekList.size(); i++) {
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemPripravek.fxml"));
                EditItemsController.getEditItemsController().itemsField.getChildren().add(gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemPripravek");
                final TextField z = (TextField) gridPane.lookup("#itemPripravekAmount");
                y.setText(storage.editedOrder.orderedPripravekList.get(i).getName());
                z.setText(String.valueOf(storage.editedOrder.orderedPripravekList.get(i).getAmount()));
            }
        }
        if (storage.editedOrder.orderedReceptList.size() > 0) {
            for (int i = 0; i < storage.editedOrder.orderedReceptList.size(); i++) {
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemRecept.fxml"));
                EditItemsController.getEditItemsController().itemsField.getChildren().add(gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemRecept");
                y.setText(storage.editedOrder.orderedReceptList.get(i).getCode());
            }
        }

    }
}
