package cz.naseLekarna.controllers.lists;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
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
    MainController mainController = MainController.getMainController();

    /**
     * Method loads new order creation and writes data of selected customer
     *
     * @throws IOException
     */
    public void writeData() throws IOException, ExecutionException, InterruptedException {
        int result = firebaseService.getCustomerInfo(phoneNumber.getText());
        if (result == 2){
            mainController.changeScene("newOrder/orderInfo");
        }
        if (result == 1){
            mainController.changeScene("editCustomer/editCustomer");
        }
    }
}
