package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
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

    /**
     *
     * @param actionEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void saveEdit(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        Integer checkedPhoneNumber;

        if (name.getText().isEmpty() && orderNumber.getText().isEmpty()) {
            name.setStyle("-fx-border-color: red");
            orderNumber.setStyle("-fx-border-color: red");
            return;
        }

        if (dateBegin.getValue() == null || dateEnd.getValue() == null || pickUpOption.getValue() == null) {
            if (dateBegin.getValue() == null) {
                dateBegin.setStyle("-fx-border-color: red");
            } else dateBegin.setStyle("-fx-border-color: transparent");
            if (dateEnd.getValue() == null) {
                dateEnd.setStyle("-fx-border-color: red");
            } else dateEnd.setStyle("-fx-border-color: transparent");
            if (pickUpOption.getValue() == null) {
                pickUpOption.setStyle("-fx-border-color: red");
            } else pickUpOption.setStyle("-fx-border-color: transparent");
            return;
        }
        if (!phoneNumber.getText().isEmpty()) {
            if (!Validator.isNumeric(phoneNumber.getText()) || phoneNumber.getText().length() != 9) {
                phoneNumber.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
                return;
            }
        }

        Map<String, Object> docData = new HashMap<>();
        docData.put("orderNumber", orderNumber.getText());

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

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }

    public void finishTask(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        firebaseService.deleteOrder();

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }


    public void editItems(ActionEvent actionEvent) throws IOException {
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/editItems.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(vBox);

        if (storage.editedOrder.orderedPripravekList.size() > 0) {
            for (int i = 0; i < storage.editedOrder.orderedPripravekList.size(); i++) {
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemPripravek.fxml"));
                EditItemsController.getEditItemsController().itemsField.getChildren().add(gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemPripravek");
                final TextField z = (TextField) gridPane.lookup("#itemPripravekAmount");
                y.setText(storage.editedOrder.orderedPripravekList.get(i).getName());
                z.setText(String.valueOf(storage.editedOrder.orderedPripravekList.get(i).getAmount()));
            }
        }
        if (storage.editedOrder.orderedReceptList.size() > 0) {
            for (int i = 0; i < storage.editedOrder.orderedReceptList.size(); i++) {
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemRecept.fxml"));
                EditItemsController.getEditItemsController().itemsField.getChildren().add(gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemRecept");
                y.setText(storage.editedOrder.orderedReceptList.get(i).getCode());
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
}
