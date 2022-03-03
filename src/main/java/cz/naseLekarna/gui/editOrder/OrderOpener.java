package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.newOrder.NewOrderController;
import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 03.02.2022
 */
public class OrderOpener {

    private static OrderOpener orderOpener;
    public VBox listPripravek;
    public VBox listRecept;
    public HBox orderInfo;
    public Label name;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();

    public OrderOpener() {
        orderOpener = this;
    }

    public static OrderOpener getOrderOpener() {
        return orderOpener;
    }


    public void openOrder(MouseEvent mouseEvent) throws IOException, ExecutionException, InterruptedException {
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/editOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Editace Objednávky");
        firebaseService.getInfoForEdit(name.getText());

        EditInfoController.getEditController().pickUpOption.getItems().add("Osobní");
        EditInfoController.getEditController().pickUpOption.getItems().add("Rozvoz");
        EditInfoController.getEditController().name.setText(storage.editedOrder.getCustomer().getName());
        EditInfoController.getEditController().phoneNumber.setText(String.valueOf(storage.editedOrder.getCustomer().getPhoneNumber()));
        EditInfoController.getEditController().street.setText(storage.editedOrder.getCustomer().getStreet());
        EditInfoController.getEditController().city.setText(storage.editedOrder.getCustomer().getCity());
        EditInfoController.getEditController().dateBegin.setValue(NewOrderController.LOCAL_DATE(storage.editedOrder.getDateBegin()));
        EditInfoController.getEditController().pickUpOption.setValue(storage.editedOrder.getOrderPickupInfo());
        EditInfoController.getEditController().dateEnd.setValue(NewOrderController.LOCAL_DATE(storage.editedOrder.getDateEnd()));
        EditInfoController.getEditController().notes.setText(storage.editedOrder.getNotes());
    }

    public void openOrdertoo(TouchEvent touchEvent) {
    }


}
