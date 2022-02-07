package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */

public class MainController implements Initializable {

    private static MainController mainController;
    @FXML
    public Button menuButton;
    @FXML
    public GridPane gridPane;
    @FXML
    public StackPane mainStackPane;
    @FXML
    public Label mainLabel;

    public MainController() {
        mainController = this;
    }
    public static MainController getMainController() {
        return mainController;
    }

    /**
     * This method is called when inilializing this Controller. It puts homeView.fxml to main stack pane.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox vBox = null;
        try {
            vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(vBox);
    }

    /**
     * This method opens popup menu when pressing button.
     *
     * @param actionEvent
     * @throws Exception
     */
    public void openMenu(ActionEvent actionEvent) throws Exception {
        VBox vbox = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
        gridPane.add(vbox, 0, 1);
    }

    /**
     * This method closes popup menu.
     */
    public void closeMenu() {
        gridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 0);
    }

}
