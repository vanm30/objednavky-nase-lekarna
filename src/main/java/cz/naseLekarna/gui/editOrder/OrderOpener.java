package cz.naseLekarna.gui.editOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.chainsaw.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
    public VBox listPripravek;
    @FXML
    public VBox listRecept;
    @FXML
    public HBox orderInfo;
    @FXML
    public Label name;
    @FXML
    public Label orderNumber;
    @FXML
    public Label orderID;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();

    /**
     * This order opens clicked order to edit it.
     * @param mouseEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void openOrder(MouseEvent mouseEvent) throws IOException, ExecutionException, InterruptedException {
        firebaseService.getInfoForEdit(orderID.getText());
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/editOrder/editOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().clear();
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Editace Objednávky");
    }

    public void openOrdertoo(TouchEvent touchEvent) {
    }
}
