package cz.naseLekarna.gui.logIn;

import cz.naseLekarna.gui.application.StageController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    public LogInController() {
        logInController = this;
    }

    public static LogInController getLogInController() {
        return logInController;
    }

    @FXML
    public VBox logInBackground;
    @FXML
    public TextField userName;
    @FXML
    public PasswordField password;
    @FXML
    public VBox errorBox;
    @FXML
    public HBox passwordBox;

    Storage storage = Storage.getStorage();
    StageController stageController = StageController.getStageController();
    FirebaseService firebaseService = new FirebaseService();

    /**
     * This method tries to log user upon given login info.
     * @throws NoSuchAlgorithmException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public void logIn() throws NoSuchAlgorithmException, ExecutionException, InterruptedException, IOException {
        //Form check
        errorBox.getChildren().clear();
        if (userName.getText().trim().isEmpty()) {
            Label error = new Label();
            error.setText("Zadejte uživatelské jméno.");
            errorBox.getChildren().add(error);
        }
        if (password.getText().isEmpty()) {
            Label error = new Label();
            error.setText("Zadejte heslo.");
            errorBox.getChildren().add(error);
        }
        if (!userName.getText().trim().isEmpty() && !password.getText().isEmpty()) {
            Boolean result = firebaseService.validateLogin(userName.getText().trim(), password.getText());
            //Logs in user
            if (result) {
                //New scene
                stageController.mainStage.getChildren().clear();
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/main.fxml"));
                stageController.mainStage.getChildren().add(gridPane);

            } else {
                errorBox.getChildren().clear();
                Label error = new Label();
                error.setText("Nesprávné uživatelské jméno nebo heslo.");
                errorBox.getChildren().add(error);
                new animatefx.animation.Shake(passwordBox).play();
            }
        }
    }

    /**
     * This method opens new user form.
     * @throws IOException
     */
    public void newUser() throws IOException {
        stageController.mainStage.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/login/adminLogin.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }

    public void focusPassword(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
           password.requestFocus();
        }
    }
}
