package cz.naseLekarna.controllers.editOrder;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXML;
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

    public OrderOpener() {
        orderOpener = this;
    }

    public static OrderOpener getOrderOpener() {
        return orderOpener;
    }

    @FXML
    public VBox listProduct;
    @FXML
    public VBox listPrescription;
    @FXML
    public HBox orderInfo;
    @FXML
    public Label name;
    @FXML
    public Label orderNumber;
    @FXML
    public Label orderID;
    @FXML
    public Label product;
    @FXML
    public Label prescription;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();

    /**
     * This order opens clicked order to edit it.
     * @param mouseEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void openOrder(MouseEvent mouseEvent) throws IOException, ExecutionException, InterruptedException {
        firebaseService.getInfoForEdit(orderID.getText());
        mainController.changeScene("editOrder/editOrder");
    }

    public void openOrdertoo(TouchEvent touchEvent) {
    }
}
