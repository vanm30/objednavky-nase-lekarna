package cz.naseLekarna.gui;


import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.ItemPripravek;
import cz.naseLekarna.system.ItemRecept;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 25.11.2021
 */
public class NewOrderController {

    private static NewOrderController newOrderController;

    @FXML
    public VBox itemsField;
    @FXML
    public Button addRecept;
    @FXML
    public Button addPripravek;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    boolean fail = false;

    public NewOrderController() {
        newOrderController = this;
    }

    public static NewOrderController getNewOrderController() {
        return newOrderController;
    }


    /**
     * Button takes user to new customer order creation
     * @param actionEvent
     * @throws Exception
     */
    public void createOrder(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/addInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Nový Pacient");
        CustomerInfoController.getCustomerInfoController().dateBegin.setValue(LocalDate.now());
    }

    /**
     * Button takes user to Saved Users list and lets them choose.
     * @param actionEvent
     * @throws IOException
     */
    public void fromDatabase(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/customerList.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Seznam Pacientů");
    }

    /**
     * Button takes user back to Order List
     * @param actionEvent
     * @throws Exception
     */
    public void backToOrderList(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }


    /**
     * Button finished order and uploads data to Firebase
     * @param actionEvent
     * @throws Exception
     */
    public void finishOrder(ActionEvent actionEvent) throws Exception {

        if (itemsField.getChildren().isEmpty()) {
            addRecept.setStyle("-fx-border-color: red;-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
            addPripravek.setStyle("-fx-border-color: red;-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
            return;
        }

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                if (x.getText().isEmpty()) {
                    x.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                    fail = true;
                } else {
                    x.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    fail = false;
                }
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    if (y.getText().isEmpty()) {
                        y.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail = true;
                    } else {
                        y.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                        fail = false;
                    }
                    if (z.getText().isEmpty()) {
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail = true;
                    } else {
                        z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                        fail = false;
                    }
                    if (!CustomerInfoController.isNumeric(z.getText())) {
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail = true;
                    } else {
                        z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                        fail = false;
                    }
                }
            }
        }

        if (fail) {
            return;
        }

        saveItems();
        //Save customer
        if (storage.customer.getSave()) {
            firebaseService.addUser();
        }
        //Save order
        firebaseService.addOrder();

        storage.order.orderedPripravekList.clear();
        storage.order.orderedReceptList.clear();

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }

    /**
     * Button adds item to order
     * @param actionEvent
     * @throws Exception
     */
    public void addRecept(ActionEvent actionEvent) throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemRecept.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    /**
     * Button adds item to order
     * @param actionEvent
     * @throws Exception
     */
    public void addPripravek(ActionEvent actionEvent) throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemPripravek.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    /**
     * Button takes user back to customer info in new order. Info is loaded if saved.
     * @param actionEvent
     * @throws Exception
     */
    public void backToAddInfo(ActionEvent actionEvent) throws Exception {
        saveItems();
        //New Scene
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/addInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Nový Pacient");

        //Fills customer info
        CustomerInfoController.getCustomerInfoController().name.setText(storage.customer.getName());
        CustomerInfoController.getCustomerInfoController().phoneNumber.setText(String.valueOf(storage.customer.getPhoneNumber()));
        CustomerInfoController.getCustomerInfoController().street.setText(storage.customer.getStreet());
        CustomerInfoController.getCustomerInfoController().city.setText(storage.customer.getCity());
        if (storage.customer.getSave()) {
            CustomerInfoController.getCustomerInfoController().addToDatabase.setSelected(true);
        }

        //Fills order info
        CustomerInfoController.getCustomerInfoController().dateBegin.setValue(LOCAL_DATE(storage.order.getDateBegin()));
        CustomerInfoController.getCustomerInfoController().pickUpOption.setValue(storage.order.getOrderPickupInfo());
        CustomerInfoController.getCustomerInfoController().dateEnd.setValue(LOCAL_DATE(storage.order.getDateEnd()));
        CustomerInfoController.getCustomerInfoController().notes.setText(storage.order.getNotes());
    }

    /**
     * Method converts String to LocalDate
     * @param dateString
     * @return date in LocalDate type
     */
    public static LocalDate LOCAL_DATE(String dateString) {
        LocalDate local_date = LocalDate.parse(dateString);
        return local_date;
    }

    /**
     * Method saves items locally
     */
    public void saveItems() {
        storage.order.orderedReceptList.clear();
        storage.order.orderedPripravekList.clear();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                storage.order.orderedReceptList.add(new ItemRecept(x.getText()));
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    storage.order.orderedPripravekList.add(new ItemPripravek(Integer.parseInt(z.getText()), y.getText()));
                }
            }
        }

    }
}
