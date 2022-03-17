package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.ItemPripravek;
import cz.naseLekarna.system.ItemRecept;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 09.02.2022
 */
public class EditItemsController {

    private static EditItemsController editItemsController;
    @FXML
    public VBox itemsField;

    Storage storage = Storage.getStorage();

    public EditItemsController() {
        editItemsController = this;
    }

    public static EditItemsController getEditItemsController() {
        return editItemsController;
    }


    public void addPripravek(ActionEvent actionEvent) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemPripravek.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    public void addRecept(ActionEvent actionEvent) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemRecept.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    public void finishItemsEdit(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
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

        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/editOrder.fxml"));
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
