package cz.naseLekarna.controllers.editOrder;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.Prescription;
import cz.naseLekarna.system.Product;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 09.02.2022
 */
public class EditItemsController implements Initializable {

    private static EditItemsController editItemsController;

    public EditItemsController() {
        editItemsController = this;
    }

    public static EditItemsController getEditItemsController() {
        return editItemsController;
    }

    @FXML
    public VBox itemsField;
    @FXML
    public Button addProduct;
    @FXML
    public Button addPrescription;
    @FXML
    public VBox errorBox;

    Storage storage = Storage.getStorage();
    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Editace položek");
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);

        if (storage.editedOrder.orderedProductList.size() > 0)  {
            for (int i = 0; i < storage.editedOrder.orderedProductList.size(); i++) {
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/product.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                itemsField.getChildren().add(0, gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemPripravek");
                final TextField z = (TextField) gridPane.lookup("#itemPripravekAmount");
                y.setText(storage.editedOrder.orderedProductList.get(i).getName());
                z.setText(String.valueOf(storage.editedOrder.orderedProductList.get(i).getAmount()));
            }
        }
        if (storage.editedOrder.orderedPrescriptionList.size() > 0) {
            for (int i = 0; i < storage.editedOrder.orderedPrescriptionList.size(); i++) {
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/prescription.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                itemsField.getChildren().add(0, gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemRecept");
                y.setText(storage.editedOrder.orderedPrescriptionList.get(i).getCode());
            }
        }
    }

    /**
     * This method add item (Pripravek) to item list.
     * @param actionEvent
     * @throws IOException
     */
    public void addProduct(ActionEvent actionEvent) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/product.fxml"));
        itemsField.getChildren().add(0,gridPane);
    }

    /**
     * This method adds item (Recept) to item list.
     * @param actionEvent
     * @throws IOException
     */
    public void addPrescription(ActionEvent actionEvent) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/views/newOrder/prescription.fxml"));
        itemsField.getChildren().add(0,gridPane);
    }

    /**
     * This method saves items and their info and updates it in edited Order in storage.
     * @param actionEvent
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void finishItemsEdit(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();

        if (itemsField.getChildren().size() < 2) {
            addPrescription.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color:  #409988; -fx-background-radius: 5;");
            addProduct.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color:  #409988; -fx-background-radius: 5;");
            mistakes.add("Musíte zadat alespoň jednu položku.");
            fail++;
        } else {
            addPrescription.setStyle("-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
            addProduct.setStyle("-fx-background-color: #5ead87#5ead87 #5ead87#5ead87; -fx-background-radius: 5;");
        }


        if (fail>0) {
            validator.displayInfo(mistakes, true);
            return;
        }

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            // if recept
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                if (x.getText().isEmpty() || !x.getText().isEmpty() && (!Validator.isAlphaNumeric(x.getText()) || x.getText().length() != 12)){
                    x.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                    fail++;
                } else {
                    x.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                }
            } else {
                // if pripravek
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    if (z.getText().isEmpty() || !z.getText().isEmpty() && !Validator.isNumeric(z.getText()) || z.getText().isEmpty() && !y.getText().isEmpty()){
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    } else {
                        z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    }
                    if (y.getText().isEmpty() || y.getText().isEmpty() && !z.getText().isEmpty()){
                        y.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    } else {
                        y.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    }
                }
            }
        }
        if (fail>0) {
            mistakes.add("Prosím zkontrolujte zvýrazněné buňky.");
            validator.displayInfo(mistakes, true);
            return;
        }

        //exec save
        storage.editedOrder.orderedPrescriptionList.clear();
        storage.editedOrder.getOrderedProductList().clear();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                storage.editedOrder.orderedPrescriptionList.add(new Prescription(x.getText().toUpperCase()));
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    storage.editedOrder.orderedProductList.add(new Product(Integer.parseInt(z.getText()), y.getText()));
                }
            }
        }
        mainController.changeScene("editOrder/editOrder");

    }

    public void back(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("editOrder/editOrder");
    }
}
