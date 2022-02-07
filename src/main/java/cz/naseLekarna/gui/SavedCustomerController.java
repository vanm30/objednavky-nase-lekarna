package cz.naseLekarna.gui;

import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Matěj Vaník
 * @created 07.02.2022
 */
public class SavedCustomerController {

    @FXML
    public Label name;

    private SavedCustomerController savedCustomerController;

    public SavedCustomerController() {
        savedCustomerController = this;
    }

    public SavedCustomerController getSavedCustomerController() {
        return savedCustomerController;
    }

    Storage storage = Storage.getStorage();


    /**
     * Method loads new order creation and writes data of selected customer
     * @param mouseEvent
     * @throws IOException
     */
    public void writeData(MouseEvent mouseEvent) throws IOException {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/addInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Nový Pacient");
        CustomerInfoController.getCustomerInfoController().dateBegin.setValue(LocalDate.now());

        List<Customer> list = storage.getActiveCustomers();
        for (Customer customer : list) {
            if (customer.getName() == name.getText()) {
                CustomerInfoController.getCustomerInfoController().name.setText(customer.getName());
                CustomerInfoController.getCustomerInfoController().phoneNumber.setText(String.valueOf(customer.getPhoneNumber()));
                CustomerInfoController.getCustomerInfoController().street.setText(customer.getStreet());
                CustomerInfoController.getCustomerInfoController().city.setText(customer.getCity());
            }
        }

    }

    public void writeDataToo(TouchEvent touchEvent) {
    }
}
