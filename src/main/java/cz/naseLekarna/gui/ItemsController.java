package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * @author Matěj Vaník
 * @created 01.02.2022
 */
public class ItemsController {

    private static ItemsController itemsController;

    @FXML
    public TextField itemRecept;
    @FXML
    public TextField itemPripravek;
    @FXML
    public TextField itemPripravekAmount;
    @FXML
    public Button deleteButton;

    public ItemsController(){
        itemsController = this;
    }

    public static ItemsController getItemsController(){
        return itemsController;
    }

    public void deleteItem(ActionEvent actionEvent) {
        GridPane gridPane = (GridPane) deleteButton.getParent();
        VBox vBox = (VBox) deleteButton.getParent().getParent();
        vBox.getChildren().remove(gridPane);


    }
}
