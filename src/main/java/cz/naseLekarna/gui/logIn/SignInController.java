package cz.naseLekarna.gui.logIn;

import cz.naseLekarna.gui.application.StageController;
import cz.naseLekarna.system.FirebaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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


    StageController stageController = StageController.getStageController();
    FirebaseService firebaseService = new FirebaseService();



    /**
     * This method takes user to new Employee form.
     * @throws IOException
     */
    public void next() throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {
        if (firebaseService.validateLogin("admin",adminPassword.getText())){
            stageController.mainStage.getChildren().clear();
            VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/newEmpForm.fxml"));
            stageController.mainStage.getChildren().add(vBox);
        } else {
            errorBox.getChildren().clear();
            Label label = new Label();
            label.setText("Heslo je neplatné.");
            errorBox.getChildren().add(label);
        }
    }

    public void finish() throws NoSuchAlgorithmException, ExecutionException, InterruptedException, IOException {
        firebaseService.addEmployee(newUsername.getText(),newPassword.getText());
        back();
    }

    /**
     * This method takes user back to login screen.
     * @throws IOException
     */
    public void back() throws IOException {
        stageController.mainStage.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/logIn.fxml"));
        stageController.mainStage.getChildren().add(vBox);
    }
}
