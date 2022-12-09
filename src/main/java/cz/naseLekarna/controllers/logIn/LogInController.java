package cz.naseLekarna.controllers.logIn;

import cz.naseLekarna.controllers.Animation;
import cz.naseLekarna.controllers.application.StageController;
import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
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
    @FXML
    public HBox userNameBox;

    Storage storage = Storage.getStorage();
    StageController stageController = StageController.getStageController();
    FirebaseService firebaseService = new FirebaseService();
    Animation animation = new Animation();
    MainController m = MainController.getMainController();

    /**
     * This method tries to log user upon given login info.
     * @throws NoSuchAlgorithmException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public void logIn() throws NoSuchAlgorithmException, ExecutionException, InterruptedException, IOException {
        userNameBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        passwordBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        String name = userName.getText().trim().toLowerCase();
        //Form check
        errorBox.getChildren().clear();
        if (name.isEmpty()) {
            Label error = new Label();
            error.setText("Zadejte uživatelské jméno.");
            userNameBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            errorBox.getChildren().add(error);
        }
        if (password.getText().isEmpty()) {
            Label error = new Label();
            passwordBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            error.setText("Zadejte heslo.");
            errorBox.getChildren().add(error);
        }
        if (!name.isEmpty() && !password.getText().isEmpty()) {
            Boolean result = firebaseService.validateLogin(name, password.getText());
            //Logs in user
            if (result) {
                //New scene
                stageController.mainStage.getChildren().clear();
                GridPane gridPane = FXMLLoader.load(getClass().getResource("/views/mainMenu/main.fxml"));
                stageController.mainStage.getChildren().add(gridPane);

            } else {
                errorBox.getChildren().clear();
                Label error = new Label();
                passwordBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
                userNameBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
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
        VBox vBox = FXMLLoader.load(getClass().getResource("/views/login/adminLogin.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }

    public void focusPassword(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
           password.requestFocus();
        }
    }
}
