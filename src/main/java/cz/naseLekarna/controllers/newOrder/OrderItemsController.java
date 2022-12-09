package cz.naseLekarna.controllers.newOrder;


import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 25.11.2021
 */
public class OrderItemsController implements Initializable {

    private static OrderItemsController newOrderController;

    public OrderItemsController() {
        newOrderController = this;
    }

    public static OrderItemsController getNewOrderController() {
        return newOrderController;
    }

    @FXML
    public VBox itemsField;
    @FXML
    public Button addPrescription;
    @FXML
    public Button addProduct;
    @FXML
    public VBox errorBox;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public StackPane node;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();

    /**
     * If some items are stored in new Order - load all items.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Nová Objednávka");
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //Load Items
        if (storage.newOrder.orderedProductList.size() > 0) {
            for (int i = 0; i < storage.newOrder.orderedProductList.size(); i++) {
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/product.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemsField.getChildren().add(0, gridPane);
                final TextField y = (TextField) gridPane.lookup("#product");
                final TextField z = (TextField) gridPane.lookup("#productAmount");
                y.setText(storage.newOrder.orderedProductList.get(i).getName());
                z.setText(String.valueOf(storage.newOrder.orderedProductList.get(i).getAmount()));
            }
        }
        if (storage.newOrder.orderedPrescriptionList.size() > 0) {
            for (int i = 0; i < storage.newOrder.orderedPrescriptionList.size(); i++) {
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/prescription.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemsField.getChildren().add(0, gridPane);
                final TextField y = (TextField) gridPane.lookup("#prescription");
                y.setText(storage.newOrder.orderedPrescriptionList.get(i).getCode());
            }
        }
        if (storage.newOrder.orderedProductList.size() == 0 && storage.newOrder.orderedPrescriptionList.size() == 0) {
            try {
                addProduct();
                addPrescription();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Button adds item (Recept) to order
     *
     * @throws Exception
     */
    public void addPrescription() throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/prescription.fxml"));
        itemsField.getChildren().add(0, gridPane);
    }

    /**
     * Button adds item (Pripravek) to order
     *
     * @throws Exception
     */
    public void addProduct() throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/product.fxml"));
        itemsField.getChildren().add(0, gridPane);
    }

    /**
     * Method saves items locally
     */
    public void saveItems() {
        storage.newOrder.orderedPrescriptionList.clear();
        storage.newOrder.orderedProductList.clear();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#prescription");
            if (x != null) {
                if (!x.getText().isEmpty()){
                    storage.newOrder.orderedPrescriptionList.add(new Prescription(x.getText().toUpperCase()));
                }
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#product");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#productAmount");
                if (y != null) {
                    if (!z.getText().isEmpty() && !y.getText().isEmpty()){
                        storage.newOrder.orderedProductList.add(new Product(Integer.parseInt(z.getText()), y.getText()));
                    }
                }
            }
        }

    }

    /**
     * This method first validates form and then saves info. After, it switches to info form.
     * @param actionEvent
     * @throws IOException
     */
    public void nextToOrderInfo(ActionEvent actionEvent) throws IOException {
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            // if recept
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#prescription");
            if (x != null) {
                if (!x.getText().isEmpty() && (!Validator.isAlphaNumeric(x.getText()) || x.getText().length() != 12)) {
                    x.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                    fail++;
                } else {
                    x.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                }
            } else {
                // if pripravek
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#product");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#productAmount");
                if (y != null) {
                    y.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    if (!z.getText().isEmpty() && !Validator.isNumeric(z.getText())) {
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    }else if (z.getText().isEmpty() && !y.getText().isEmpty()) {
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    }else if (y.getText().isEmpty() && !z.getText().isEmpty()) {
                        y.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    } else {
                        y.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                        z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    }
                }
            }
        }
        if (fail>0) {
            mistakes.add("Prosím zadejte správné informace v označených buňkách.");
            validator.displayInfo(mistakes, true);
            return;
        }

        saveItems();

        if (itemsField.getChildren().size() < 2 ||(storage.newOrder.orderedPrescriptionList.size() == 0 && storage.newOrder.orderedProductList.size() == 0)) {
            addPrescription.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color: #409988; -fx-background-radius: 5;");
            addProduct.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color: #409988; -fx-background-radius: 5;");
            fail++;
        } else {
            addPrescription.setStyle("-fx-background-color: #409988; -fx-background-radius: 5;");
            addProduct.setStyle("-fx-background-color: #409988; -fx-background-radius: 5;");
        }
        if (fail>0) {
            mistakes.add("Prosím zadejte alespoň jednu položku.");
            validator.displayInfo(mistakes, true);
            return;
        }
        //exec new page
        mainController.changeScene("newOrder/orderInfo");
    }


    public void backToOrderList() throws IOException {
        mainController.changeScene("mainMenu/homeView");
    }
}
