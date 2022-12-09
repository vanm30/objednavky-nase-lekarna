package cz.naseLekarna.controllers.logIn;

import cz.naseLekarna.controllers.application.StageController;
import cz.naseLekarna.system.FirebaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 17.03.2022
 */
public class SignInController {

    private static SignInController signInController;

    public SignInController() {
        signInController = this;
    }

    public static SignInController getSignInController() {
        return signInController;
    }

    @FXML
    public PasswordField adminPassword;
    @FXML
    public TextField newUsername;
    @FXML
    public PasswordField newPassword;
    @FXML
    public VBox errorBox;
    @FXML
    public HBox passwordBox;
    @FXML
    public HBox userNameBox;
    @FXML
    public HBox passwordBox2;

    StageController stageController = StageController.getStageController();
    FirebaseService firebaseService = new FirebaseService();

    /**
     * This method takes user to new UserController form.
     * @throws IOException
     */
    public void next() throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {
        passwordBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        if (firebaseService.validateLogin("admin",adminPassword.getText())){
            stageController.mainStage.getChildren().clear();
            VBox vBox = FXMLLoader.load(getClass().getResource("/views/login/newUserForm.fxml"));
            stageController.mainStage.getChildren().add(vBox);
        } else {
            errorBox.getChildren().clear();
            Label label = new Label();
            passwordBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            label.setText("Heslo je neplatné.");
            errorBox.getChildren().add(label);
            new animatefx.animation.Shake(passwordBox).play();
        }
    }

    public void finish() throws NoSuchAlgorithmException, ExecutionException, InterruptedException, IOException {
        userNameBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        passwordBox2.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        String name = newUsername.getText().trim().toLowerCase();
        int fail = 0;
        errorBox.getChildren().clear();
        if (name.isEmpty()){
            fail++;
            Label error = new Label();
            userNameBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            error.setText("Zadejte uživatelské jméno.");
            errorBox.getChildren().add(error);
        }
        if (newPassword.getText().isEmpty()){
            passwordBox2.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            fail++;
            Label error = new Label();
            error.setText("Zadejte heslo.");
            errorBox.getChildren().add(error);
        }
        if (firebaseService.userNameExists(name)){
            userNameBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            fail++;
            Label error = new Label();
            error.setText("Uživatelské jméno je již používané.");
            errorBox.getChildren().add(error);
        }
        if (newPassword.getText().length() < 8){
            passwordBox2.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: red; -fx-border-radius: 10;");
            fail++;
            Label error = new Label();
            error.setText("Heslo je příliš slabé.");
            errorBox.getChildren().add(error);
        }
        if (fail<1){
            firebaseService.addUser(name,newPassword.getText());
            back();
        }
    }

    /**
     * This method takes user back to login screen.
     * @throws IOException
     */
    public void back() throws IOException {
        stageController.mainStage.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/views/logIn/logIn.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }

    public void focusPassword(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                newPassword.requestFocus();
            }
    }
}
