package cz.naseLekarna.gui.lists;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.gui.newOrder.OptionalInfoController;
import cz.naseLekarna.gui.newOrder.OrderInfoController;
import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 07.02.2022
 */
public class SavedCustomerController {

    private SavedCustomerController savedCustomerController;

    public SavedCustomerController() {
        savedCustomerController = this;
    }

    public SavedCustomerController getSavedCustomerController() {
        return savedCustomerController;
    }

    @FXML
    public Label name;
    @FXML
    public Label phoneNumber;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();

    /**
     * Method loads new order creation and writes data of selected customer
     *
     * @param mouseEvent
     * @throws IOException
     */
    public void writeData(MouseEvent mouseEvent) throws IOException, ExecutionException, InterruptedException {
        firebaseService.getCustomerInfo(phoneNumber.getText());
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/newOrder/optionalInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Nový Pacient");
        OptionalInfoController.getOptionalInfoController().addToDatabase.setVisible(false);
        OptionalInfoController.getOptionalInfoController().addToDbLabel.setVisible(false);
    }

    public void writeDataToo(TouchEvent touchEvent) {
    }
}
