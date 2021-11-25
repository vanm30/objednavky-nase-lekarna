package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * @author Matěj Vaník
 * @created 25.11.2021
 */
public class NewOrderController {

    @FXML public VBox itemsField;
    @FXML public Button deleteButton;

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



    public void finishOrder(ActionEvent actionEvent) {

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
    }
}
