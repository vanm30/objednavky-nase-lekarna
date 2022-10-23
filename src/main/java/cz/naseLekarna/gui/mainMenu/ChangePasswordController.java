package cz.naseLekarna.gui.mainMenu;

import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ChangePasswordController implements Initializable {

    private static ChangePasswordController changePasswordController;

    public ChangePasswordController() {
        changePasswordController = this;
    }

    public static ChangePasswordController getChangePasswordController() {
        return changePasswordController;
    }

    MainController mainController = MainController.getMainController();
    FirebaseService firebaseService = new FirebaseService();
    Validator validator = new Validator();

    @FXML
    public TextField oldPassword;
    @FXML
    public TextField newPassword;
    @FXML
    public TextField newPassword2;
    @FXML
    public Label barLabel;
    @FXML
    public ProgressBar bar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        barLabel.setVisible(false);
        bar.setVisible(false);
    }


    public void back(ActionEvent actionEvent) {
        mainController.mainStackPane.getChildren().clear();
        VBox settings = null;
        try {
            settings = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/settings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.mainStackPane.getChildren().add(settings);
        mainController.mainLabel.setText("Nastavení");
    }

    public void done(ActionEvent actionEvent) throws NoSuchAlgorithmException, ExecutionException, InterruptedException, IOException {
        //Check
        ArrayList<String> mistakes = new ArrayList<String>();
        int fail = 0;
        oldPassword.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        newPassword2.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        newPassword.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");

        if (oldPassword.getText().isEmpty()) {
            oldPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím vyplňte aktuální heslo.");
            fail++;
        }
        if (newPassword.getText().isEmpty()) {
            newPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím vyplňte nové heslo.");
            fail++;
        }
        if (newPassword2.getText().isEmpty()) {
            newPassword2.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Prosím ověřte nové heslo");
            fail++;
        }

        if (fail>0){
            validator.displayError(mistakes);
            return;
        }

        if (!firebaseService.isPasswordCorrect(oldPassword.getText())) {
            oldPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Aktuální heslo se liší od zadaného hesla.");
            fail++;
        }
        if (!newPassword.getText().equals(newPassword2.getText())){
            newPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            newPassword2.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Nové heslo a heslo k ověření se neshodují.");
            fail++;
        }
        if (newPassword.getText().length() < 8){
            newPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Nové heslo je příliš slabé.");
            fail++;
        }

        if (fail>0){
            validator.displayError(mistakes);
            return;
        }

        //update password
        firebaseService.updatePassword(newPassword.getText());

        //chage scene
        mainController.mainStackPane.getChildren().clear();
        VBox settings = null;
        try {
            settings = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/settings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.mainStackPane.getChildren().add(settings);
        mainController.mainLabel.setText("Nastavení");
        ArrayList<String> info = new ArrayList<String>();
        info.add("Heslo úspěšně změněno.");
        validator.displayError(info);
    }

    public void check() {
        if (newPassword.getText().isEmpty()){
            barLabel.setVisible(false);
            bar.setVisible(false);
            bar.setProgress(0);
        } else {
            barLabel.setVisible(true);
            bar.setVisible(true);
            if (newPassword.getText().length() < 8){
                    bar.setStyle("-fx-accent: red;");
                    barLabel.setText("Slabé heslo");
                    bar.setProgress(0.33);
            } else {
                if (!Validator.isAlphaNumericWithSpace(newPassword.getText())){
                    bar.setStyle("-fx-accent: green;");
                    barLabel.setText("Silné heslo");
                    bar.setProgress(1);
                } else {
                    bar.setStyle("-fx-accent: yellow;");
                    barLabel.setText("Středně silné heslo");
                    bar.setProgress(0.66);
                }
            }
        }
    }
}
