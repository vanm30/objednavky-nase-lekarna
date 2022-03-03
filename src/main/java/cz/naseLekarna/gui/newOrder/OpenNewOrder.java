package cz.naseLekarna.gui.newOrder;

import cz.naseLekarna.gui.mainMenu.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 18.11.2021
 */
public class OpenNewOrder implements Initializable {

    @FXML
    public VBox orderBackground;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StackPane stackPane = null;
        try {
            stackPane = FXMLLoader.load(getClass().getResource("/fxml/orderList.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderBackground.getChildren().add(stackPane);
    }


    private static OpenNewOrder openNewOrder;

    public OpenNewOrder() {
        openNewOrder = this;
    }

    public static OpenNewOrder getOrderController() {
        return openNewOrder;
    }

    /**
     * Method takes user to new order creation
     * @param actionEvent
     * @throws Exception
     */
    public void newOrder(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/addInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Nová Objednávka");
        CustomerInfoController.getCustomerInfoController().dateBegin.setValue(LocalDate.now());
    }
}
