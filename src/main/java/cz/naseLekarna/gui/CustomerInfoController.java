package cz.naseLekarna.gui;


import cz.naseLekarna.system.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class CustomerInfoController implements Initializable {

    private static CustomerInfoController customerInfoController;

    @FXML
    public ChoiceBox<String> pickUpOption;
    @FXML
    public TextField name;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField street;
    @FXML
    public TextField city;
    @FXML
    public CheckBox addToDatabase;
    @FXML
    public DatePicker dateBegin;
    @FXML
    public DatePicker dateEnd;
    @FXML
    public TextArea notes;

    Storage storage = Storage.getStorage();

    public CustomerInfoController() {
        customerInfoController = this;
    }

    public static CustomerInfoController getCustomerInfoController() {
        return customerInfoController;
    }

    /**
     * This method is run when initialing this Controller. It fills ChoiceBox with following options.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pickUpOption.getItems().add("Osobní");
        pickUpOption.getItems().add("Rozvoz");

    }

    /**
     * Method is called when back button is pressed. This method moves user to homeView.
     *
     * @param actionEvent
     * @throws Exception
     */
    public void backToAddOrder(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().mainStackPane.getChildren().clear();
        StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/addOrder.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(stackPane);
        MainController.getMainController().mainLabel.setText("Nová Objednávka");
    }

    /**
     * This method switches view to newItemsInfo view.
     *
     * @param actionEvent
     * @throws Exception
     */
    public void addItems(ActionEvent actionEvent) throws Exception {

        if (name.getText().isEmpty() || phoneNumber.getText().isEmpty() || street.getText().isEmpty() || city.getText().isEmpty() || dateBegin.getValue() == null || dateEnd.getValue() == null || pickUpOption.getValue() == null){
            if(name.getText().isEmpty()){
                name.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            }else name.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if(phoneNumber.getText().isEmpty()){
                phoneNumber.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            }else phoneNumber.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if(street.getText().isEmpty()){
                street.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            }else street.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if(city.getText().isEmpty()){
                city.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            }else city.setStyle("-fx-border-color: transparent; -fx-border-radius: 10;-fx-background-radius: 10");
            if(dateBegin.getValue() == null){
                dateBegin.setStyle("-fx-border-color: red");
            }else dateBegin.setStyle("-fx-border-color: transparent");
            if(dateEnd.getValue() == null){
                dateEnd.setStyle("-fx-border-color: red");
            }else dateEnd.setStyle("-fx-border-color: transparent");
            if(pickUpOption.getValue() == null){
                pickUpOption.setStyle("-fx-border-color: red");
            }else pickUpOption.setStyle("-fx-border-color: transparent");
            return;
        }

        if (!isNumeric(phoneNumber.getText())){
            phoneNumber.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            return;
        }


            //Save user
            storage.customer = new Customer(
                    name.getText(),
                    Integer.parseInt(phoneNumber.getText()),
                    street.getText(),
                    city.getText()
            );
            if (addToDatabase.isSelected()) {
                storage.customer.setSave(true);
            }

            //Save Order
            storage.order = new Order(
                    storage.customer, dateBegin.getValue().toString(), pickUpOption.getValue(), dateEnd.getValue().toString(), notes.getText()
            );

            // New Scene
            MainController.getMainController().mainStackPane.getChildren().clear();
            StackPane stackPane = FXMLLoader.load(getClass().getResource("/fxml/newItemsInfo.fxml"));
            MainController.getMainController().mainStackPane.getChildren().add(stackPane);
            MainController.getMainController().mainLabel.setText("Obsah Objednávky");
            if (storage.itemPripravekList.size() > 0) {
                for (int i = 0; i < storage.itemPripravekList.size(); i++) {
                    GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemPripravek.fxml"));
                    NewOrderController.getNewOrderController().itemsField.getChildren().add(gridPane);
                    final TextField y = (TextField) gridPane.lookup("#itemPripravek");
                    final TextField z = (TextField) gridPane.lookup("#itemPripravekAmount");
                    y.setText(storage.itemPripravekList.get(i).getName());
                    z.setText(String.valueOf(storage.itemPripravekList.get(i).getAmount()));
                }
            }
            if (storage.itemReceptList.size() > 0) {
                for (int i = 0; i < storage.itemPripravekList.size(); i++) {
                    GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/itemRecept.fxml"));
                    NewOrderController.getNewOrderController().itemsField.getChildren().add(gridPane);
                    final TextField y = (TextField) gridPane.lookup("#itemRecept");
                    y.setText(storage.itemReceptList.get(i).getCode());
                }
            }

    }

    public static boolean isNumeric(String string) {
        int intValue;

        if(string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

}
