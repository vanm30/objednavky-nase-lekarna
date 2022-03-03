package cz.naseLekarna.gui.logIn;

import cz.naseLekarna.gui.application.StageController;
import cz.naseLekarna.system.FirebaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 03.03.2022
 */
public class LogInController {

    private static LogInController logInController;

    @FXML
    public VBox loginBackground;
    @FXML
    public TextField userName;
    @FXML
    public PasswordField password;
    @FXML
    public VBox errorBox;

    public LogInController() {
        logInController = this;
    }

    public static LogInController getLogInController() {
        return logInController;
    }


    StageController stageController = StageController.getStageController();
    FirebaseService firebaseService = new FirebaseService();


    public void newUser() throws IOException {
        stageController.mainStage.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/adminLogin.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }

    public void logIn() throws NoSuchAlgorithmException, ExecutionException, InterruptedException, IOException {
        errorBox.getChildren().clear();
        if (userName.getText().trim().isEmpty()){
            Label error = new Label();
            error.setText("Zadejte uživatelské jméno.");
            errorBox.getChildren().add(error);
        }
        if (password.getText().isEmpty()){
            Label error = new Label();
            error.setText("Zadejte heslo.");
            errorBox.getChildren().add(error);
        }
        if (!userName.getText().trim().isEmpty() && !password.getText().isEmpty()){
            Boolean result = firebaseService.validateLogin(userName.getText(),password.getText());
            if (result){
                stageController.mainStage.getChildren().clear();
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
                stageController.mainStage.getChildren().add(gridPane);

            } else {
                errorBox.getChildren().clear();
                Label error = new Label();
                error.setText("Nesprávné uživatelské jméno nebo heslo.");
                errorBox.getChildren().add(error);
            }
        }


    }

    public void next() throws IOException {
        stageController.mainStage.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/newEmpForm.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }

    public void finish() {

    }

    public void back() throws IOException {
        stageController.mainStage.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/logIn.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }
}
