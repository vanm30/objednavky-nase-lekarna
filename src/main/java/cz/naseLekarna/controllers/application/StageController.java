package cz.naseLekarna.controllers.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 02.03.2022
 */
public class StageController implements Initializable {

    private static StageController stageController;

    public StageController() {
        stageController = this;
    }

    public static StageController getStageController() {
        return stageController;
    }

    @FXML
    public StackPane mainStage;

    /**
     * Initializer for Main Stage of app.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Node node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/views/login/logIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.requestFocus();
        mainStage.getChildren().add(node);
    }

}
