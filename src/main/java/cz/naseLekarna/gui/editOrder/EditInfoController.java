package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.newOrder.CustomerInfoController;
import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 08.02.2022
 */
public class EditInfoController {
    
    private static EditInfoController editController;

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


    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();


    public EditInfoController(){
        editController = this;
    }
    public static EditInfoController getEditController(){
        return editController;
    }


    public void saveEdit(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        if (name.getText().isEmpty() || phoneNumber.getText().isEmpty() || street.getText().isEmpty() || city.getText().isEmpty() || dateBegin.getValue() == null || dateEnd.getValue() == null || pickUpOption.getValue() == null) {
            if (name.getText().isEmpty()) {
                name.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            } else name.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if (phoneNumber.getText().isEmpty()) {
                phoneNumber.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            } else
                phoneNumber.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if (street.getText().isEmpty()) {
                street.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            } else street.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if (city.getText().isEmpty()) {
                city.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            } else city.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
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

        if (!CustomerInfoController.isNumeric(phoneNumber.getText())) {
            phoneNumber.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            return;
        }

        Map<String, Object> docData = new HashMap<>();
        docData.put("customer", Arrays.asList(
                name.getText(),
                phoneNumber.getText(),
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
}
