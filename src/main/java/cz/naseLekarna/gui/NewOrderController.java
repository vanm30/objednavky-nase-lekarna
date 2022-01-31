package cz.naseLekarna.gui;

import cz.naseLekarna.system.FirebaseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Matěj Vaník
 * @created 25.11.2021
 */
public class NewOrderController {

    @FXML public VBox itemsField;
    @FXML public Button deleteButton;

    FirebaseService firebaseService = new FirebaseService();
    CustomerInfoController customerInfoController = CustomerInfoController.getCustomerInfoController();

    private static NewOrderController newOrderController;

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
        //Save customer
        if (customerInfoController.customer.getSave()){
            firebaseService.addUser();
        }

        //Save order
        firebaseService.addOrder();



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

    public void deleteItem(ActionEvent actionEvent) {
            GridPane gridPane = (GridPane) deleteButton.getParent();
            VBox vBox = (VBox) deleteButton.getParent().getParent();
            vBox.getChildren().remove(gridPane);


    }

    public void backToAddInfo(ActionEvent actionEvent) throws Exception{
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/addInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Nový Pacient");

        //Fills customer info
        CustomerInfoController.getCustomerInfoController().name.setText(customerInfoController.customer.getName());
        CustomerInfoController.getCustomerInfoController().phoneNumber.setText(String.valueOf(customerInfoController.customer.getPhoneNumber()));
        CustomerInfoController.getCustomerInfoController().street.setText(customerInfoController.customer.getStreet());
        CustomerInfoController.getCustomerInfoController().city.setText(customerInfoController.customer.getCity());
        CustomerInfoController.getCustomerInfoController().zip.setText(String.valueOf(customerInfoController.customer.getZip()));
        if(customerInfoController.customer.getSave()){
            CustomerInfoController.getCustomerInfoController().addToDatabase.setSelected(true);
        }

        //Fills order info
        CustomerInfoController.getCustomerInfoController().dateBegin.setValue(LOCAL_DATE(customerInfoController.order.getDateBegin()));
        CustomerInfoController.getCustomerInfoController().pickUpOption.setValue(customerInfoController.order.getOrderPickupInfo());
        CustomerInfoController.getCustomerInfoController().dateEnd.setValue(LOCAL_DATE(customerInfoController.order.getDateEnd()));
        CustomerInfoController.getCustomerInfoController().notes.setText(customerInfoController.order.getNotes());
    }

    public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }
}
