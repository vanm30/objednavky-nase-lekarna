package cz.naseLekarna.gui.newOrder;


import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.text.StringEscapeUtils;

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
    public Button addRecept;
    @FXML
    public Button addPripravek;
    @FXML
    public VBox errorBox;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public StackPane node;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();

    /**
     * If some items are stored in new Order - load all items.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //Load Items
        if (storage.newOrder.orderedPripravekList.size() > 0) {
            for (int i = 0; i < storage.newOrder.orderedPripravekList.size(); i++) {
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemPripravek.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemsField.getChildren().add(0, gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemPripravek");
                final TextField z = (TextField) gridPane.lookup("#itemPripravekAmount");
                y.setText(storage.newOrder.orderedPripravekList.get(i).getName());
                z.setText(String.valueOf(storage.newOrder.orderedPripravekList.get(i).getAmount()));
            }
        }
        if (storage.newOrder.orderedReceptList.size() > 0) {
            for (int i = 0; i < storage.newOrder.orderedReceptList.size(); i++) {
                GridPane gridPane = null;
                try {
                    gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemRecept.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemsField.getChildren().add(0, gridPane);
                final TextField y = (TextField) gridPane.lookup("#itemRecept");
                y.setText(storage.newOrder.orderedReceptList.get(i).getCode());
            }
        }
        if (storage.newOrder.orderedPripravekList.size() == 0 && storage.newOrder.orderedReceptList.size() == 0) {
            try {
                addPripravek();
                addRecept();
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
    public void addRecept() throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemRecept.fxml"));
        itemsField.getChildren().add(0, gridPane);
    }

    /**
     * Button adds item (Pripravek) to order
     *
     * @throws Exception
     */
    public void addPripravek() throws Exception {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/newOrder/itemPripravek.fxml"));
        itemsField.getChildren().add(0, gridPane);
    }

    /**
     * Method saves items locally
     */
    public void saveItems() {
        storage.newOrder.orderedReceptList.clear();
        storage.newOrder.orderedPripravekList.clear();

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                if (!x.getText().isEmpty()){
                    storage.newOrder.orderedReceptList.add(new ItemRecept(x.getText().toUpperCase()));
                }
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    if (!z.getText().isEmpty() && !y.getText().isEmpty()){
                        storage.newOrder.orderedPripravekList.add(new ItemPripravek(Integer.parseInt(z.getText()), y.getText()));
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
        saveItems();
        //Form check
        int fail = 0;
        ArrayList<String> mistakes = new ArrayList<String>();

        if (itemsField.getChildren().size() < 2 ||(storage.newOrder.orderedReceptList.size() == 0 && storage.newOrder.orderedPripravekList.size() == 0)) {
            addRecept.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color: #409988; -fx-background-radius: 5;");
            addPripravek.setStyle("-fx-border-color: red;-fx-border-radius: 5 ;-fx-background-color: #409988; -fx-background-radius: 5;");
            mistakes.add("Musíte zadat alespoň jednu položku.");
            fail++;
        } else {
            addRecept.setStyle("-fx-background-color: #409988; -fx-background-radius: 5;");
            addPripravek.setStyle("-fx-background-color: #409988; -fx-background-radius: 5;");
        }
        if (fail>0) {
            errorBox.getChildren().clear();
            mistakes.forEach(mistake -> {
                Label label = new Label();
                label.setText(mistake);
                errorBox.getChildren().add(label);
            });
            return;
        }

        for (int i = 0; i < itemsField.getChildren().size(); i++) {
            final TextField x = (TextField) itemsField.getChildren().get(i).lookup("#itemRecept");
            if (x != null) {
                if (!x.getText().isEmpty() && (!Validator.isAlphaNumeric(x.getText()) || x.getText().length() != 12)) {
                    x.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                    fail++;
                } else {
                    x.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                }
            } else {
                final TextField y = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravek");
                final TextField z = (TextField) itemsField.getChildren().get(i).lookup("#itemPripravekAmount");
                if (y != null) {
                    if (!z.getText().isEmpty() && !Validator.isNumeric(z.getText())) {
                        z.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
                        fail++;
                    } else {
                        z.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                    }
                }
            }
        }
        if (fail>0) {
            mistakes.add("Prosím zadejte správné informace \n v označených buňkách.");
            errorBox.getChildren().clear();
            mistakes.forEach(mistake -> {
                Label label = new Label();
                label.setText(mistake);
                errorBox.getChildren().add(label);
            });
            return;
        }
        //exec new page
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/newOrder/orderInfo.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
    }


    public void backToOrderList() throws IOException {
            MainController.getMainController().mainStackPane.getChildren().clear();
            VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
            MainController.getMainController().mainStackPane.getChildren().add(vBox);
            MainController.getMainController().mainLabel.setText("Aktivní objednávky");
    }

    public void test(KeyEvent keyEvent) {
        if  (keyEvent.getCode() == KeyCode.ESCAPE){
            System.out.println(MainController.getMainController().mainStackPane.getChildren().get(0).getId());
        }
    }
}
