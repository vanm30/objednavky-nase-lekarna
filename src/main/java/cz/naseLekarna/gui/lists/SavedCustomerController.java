package cz.naseLekarna.gui.lists;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.gui.newOrder.OrderInfoController;
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

    private SavedCustomerController savedCustomerController;

    public SavedCustomerController() {
        savedCustomerController = this;
    }

    public SavedCustomerController getSavedCustomerController() {
        return savedCustomerController;
    }

    @FXML
    public Label name;

    Storage storage = Storage.getStorage();


    /**
     * Method loads new order creation and writes data of selected customer
     *
     * @param mouseEvent
     * @throws IOException
     */
    public void writeData(MouseEvent mouseEvent) throws IOException {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/optionalInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Nový Pacient");
        OrderInfoController.getCustomerInfoController().dateBegin.setValue(LocalDate.now());

        List<Customer> list = storage.getActiveCustomers();
        for (Customer customer : list) {
            if (customer.getName() == name.getText()) {
                OrderInfoController.getCustomerInfoController().name.setText(customer.getName());
                OrderInfoController.getCustomerInfoController().phoneNumber.setText(String.valueOf(customer.getPhoneNumber()));
                OrderInfoController.getCustomerInfoController().street.setText(customer.getStreet());
                OrderInfoController.getCustomerInfoController().city.setText(customer.getCity());
            }
        }

    }

    public void writeDataToo(TouchEvent touchEvent) {
    }
}
