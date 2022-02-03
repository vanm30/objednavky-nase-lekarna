package cz.naseLekarna.gui;


import cz.naseLekarna.system.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 25.11.2021
 */
public class NewOrderController implements Initializable {

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


    public void createOrder(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/addInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Nový Pacient");
        CustomerInfoController.getCustomerInfoController().dateBegin.setValue(LocalDate.now());
    }

    public void fromDatabase(ActionEvent actionEvent) {
        System.out.println("a");
        /*TODO*/
    }

    public void backToOrderList(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }


    public void finishOrder(ActionEvent actionEvent) throws Exception {

        if (itemsField.getChildren().isEmpty()){
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

        if (fail){
            return;
        }

        saveItems();
        //Save customer
        if (storage.customer.getSave()) {
            firebaseService.addUser();
        }
        //Save order
        firebaseService.addOrder();

        storage.itemReceptList.clear();
        storage.itemPripravekList.clear();

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox1 = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox1);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }

    public void addRecept(ActionEvent actionEvent) throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemRecept.fxml"));
        itemsField.getChildren().add(gridPane);
    }

    public void addPripravek(ActionEvent actionEvent) throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemPripravek.fxml"));
        itemsField.getChildren().add(gridPane);
    }

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

    public static LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    public void saveItems() {
        storage.itemReceptList.clear();
        storage.itemPripravekList.clear();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                storage.itemReceptList.add(new ItemRecept(x.getText()));
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    storage.itemPripravekList.add(new ItemPripravek(y.getText(), Integer.parseInt(z.getText())));
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
