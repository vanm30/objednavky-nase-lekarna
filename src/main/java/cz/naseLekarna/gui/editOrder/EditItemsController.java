package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.ItemPripravek;
import cz.naseLekarna.system.ItemRecept;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 09.02.2022
 */
public class EditItemsController {

    private static EditItemsController editItemsController;

    public EditItemsController() {
        editItemsController = this;
    }

    public static EditItemsController getEditItemsController() {
        return editItemsController;
    }

    @FXML
    public VBox itemsField;
    @FXML
    public Button addPripravek;
    @FXML
    public Button addRecept;
    @FXML
    public VBox errorBox;

    Storage storage = Storage.getStorage();

    /**
     * This method add item (Pripravek) to item list.
     * @param actionEvent
     * @throws IOException
     */
    public void addPripravek(ActionEvent actionEvent) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemPripravek.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    /**
     * This method adds item (Recept) to item list.
     * @param actionEvent
     * @throws IOException
     */
    public void addRecept(ActionEvent actionEvent) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemRecept.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    /**
     * This method saves items and their info and updates it in edited Order in storage.
     * @param actionEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void finishItemsEdit(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();

        if (itemsField.getChildren().isEmpty()) {
            addRecept.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
            addPripravek.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
            mistakes.add("Musíte zadat alespoň jednu položku.");
            fail++;
        } else {
            addRecept.setStyle("-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
            addPripravek.setStyle("-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
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
        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                if (x.getText().isEmpty() || !Validator.isAlphaNumeric(x.getText()) || x.getText().length() != 12) {
                    x.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                    fail++;
                } else {
                    x.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                }
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    if (y.getText().isEmpty()) {
                        y.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    } else {
                        y.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    }
                    if (z.getText().isEmpty() || !Validator.isNumeric(z.getText())) {
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    } else {
                        z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    }
                }
            }
        }
        if (fail>0) {
            mistakes.add("Prosím zadejte správné informace \n v označených buňkách.");
            errorBox.getChildren().clear();
            mistakes.forEach(mistake -> {
                Label label = new Label();
                label.setText(mistake);
                errorBox.getChildren().add(label);
            });
            return;
        }

        //exec save and upload
        storage.editedOrder.orderedReceptList.clear();
        storage.editedOrder.orderedPripravekList.clear();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                storage.editedOrder.orderedReceptList.add(new ItemRecept(x.getText()));
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    storage.editedOrder.orderedPripravekList.add(new ItemPripravek(Integer.parseInt(z.getText()), y.getText()));
                }
            }
        }

        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/editOrder/editOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(vBox);

        /*EditInfoController.getEditController().pickUpOption.getItems().add("Osobní");
        EditInfoController.getEditController().pickUpOption.getItems().add("Rozvoz");
        EditInfoController.getEditController().name.setText(storage.editedOrder.getCustomer().getName());
        EditInfoController.getEditController().phoneNumber.setText(String.valueOf(storage.editedOrder.getCustomer().getPhoneNumber()));
        EditInfoController.getEditController().street.setText(storage.editedOrder.getCustomer().getStreet());
        EditInfoController.getEditController().city.setText(storage.editedOrder.getCustomer().getCity());
        EditInfoController.getEditController().dateBegin.setValue(NewOrderController.LOCAL_DATE(storage.editedOrder.getDateBegin()));
        EditInfoController.getEditController().pickUpOption.setValue(storage.editedOrder.getOrderPickupInfo());
        EditInfoController.getEditController().dateEnd.setValue(NewOrderController.LOCAL_DATE(storage.editedOrder.getDateEnd()));
        EditInfoController.getEditController().notes.setText(storage.editedOrder.getNotes());*/
    }
}
