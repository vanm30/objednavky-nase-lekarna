package cz.naseLekarna.gui.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
        VBox vBox = null;
        try {
            vBox = FXMLLoader.load(getClass().getResource("/fxml/logIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.requestFocus();
        mainStage.getChildren().add(vBox);
    }
}
