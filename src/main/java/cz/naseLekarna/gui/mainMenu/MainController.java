package cz.naseLekarna.gui.mainMenu;

import cz.naseLekarna.gui.lists.OrderListController;
import cz.naseLekarna.system.Storage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */

public class MainController implements Initializable {

    private static MainController mainController;

    public MainController() {
        mainController = this;
    }

    public static MainController getMainController() {
        return mainController;
    }

    @FXML
    public Button menuButton;
    @FXML
    public GridPane gridPane;
    @FXML
    public StackPane mainStackPane;
    @FXML
    public Label mainLabel;
    @FXML
    public Button removeMenu;
    @FXML
    public TextField searchBar;

    Storage storage = Storage.getStorage();

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
            vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(vBox);
        removeMenu.setVisible(false);
        searchBar.setVisible(false);
    }

    /**
     * This method opens popup menu when pressing button.
     *
     * @throws Exception
     */
    public void openMenu() throws Exception {
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/menu.fxml"));
        mainStackPane.getChildren().add(vBox);
        vBox.setTranslateX(-219);


        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(vBox);

        slide.setToX(0);
        slide.play();

        vBox.setTranslateX(219);

        slide.setOnFinished(event -> {
            menuButton.setVisible(false);
            removeMenu.setVisible(true);
        });
    }

    /**
     * This method closes popup menu.
     */
    public void closeMenu() throws IOException {
        VBox vBox = (VBox) mainStackPane.lookup("#menuBox");
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(vBox);

        slide.setToX(-219);
        slide.play();

        vBox.setTranslateX(0);

        slide.setOnFinished(event -> {
            mainStackPane.getChildren().remove(vBox);
            menuButton.setVisible(true);
            removeMenu.setVisible(false);
        });
    }

    /**
     * This method opens home view.
     * @throws IOException
     */
    public void switchToHome() throws IOException {
        VBox vBox = (VBox) mainStackPane.lookup("#menuBox");
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(vBox);

        slide.setToX(-219);
        slide.play();

        vBox.setTranslateX(0);

        slide.setOnFinished(event -> {
            mainStackPane.getChildren().clear();
            VBox homeView = null;
            try {
                homeView = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainStackPane.getChildren().add(homeView);
            mainLabel.setText("Aktivní Objednávky");
            menuButton.setVisible(true);
            removeMenu.setVisible(false);
        });
    }

    /**
     * This method opens settings.
     * @throws IOException
     */
    public void switchToSettings() throws IOException {
        VBox vBox = (VBox) mainStackPane.lookup("#menuBox");
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(vBox);

        slide.setToX(-219);
        slide.play();

        vBox.setTranslateX(0);

        slide.setOnFinished(event -> {
            mainStackPane.getChildren().clear();
            VBox settings = null;
            try {
                settings = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/settings.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainStackPane.getChildren().add(settings);
            mainLabel.setText("Nastavení");
            menuButton.setVisible(true);
            removeMenu.setVisible(false);
        });
    }

    public void startSearch(ActionEvent actionEvent) {

        if (mainLabel.isVisible()){
            mainLabel.setVisible(false);
            searchBar.setVisible(true);
        } else{
            mainLabel.setVisible(true);
            searchBar.setVisible(false);
        }
    }


    public void search() {
        if (searchBar.getText().isEmpty()){
            OrderListController.getOrderListController().loadOrders();
            return;
        }

        if(!storage.getActiveOrders().isEmpty()){
            ArrayList<String> names = storage.orderNames;
            ArrayList<Integer> numbers = storage.orderNumbers;
            String search = searchBar.getText();

            Set<Object> searchedNames = new HashSet<Object>();

            for (Object name : names) {
                if (name.toString().contains(search)) {
                    searchedNames.add(name);
                }
            }
            for (Object number : numbers){
                if (search.equals(number.toString())) {
                    searchedNames.add(number);
                }
            }
            if (!searchedNames.isEmpty()){
                OrderListController.getOrderListController().searchOrders(searchedNames);
            } else {
                OrderListController.getOrderListController().activeOrders.getChildren().clear();
            }
        }
    }
}
