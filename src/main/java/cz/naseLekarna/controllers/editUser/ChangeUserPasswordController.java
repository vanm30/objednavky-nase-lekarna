package cz.naseLekarna.controllers.editUser;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ChangeUserPasswordController implements Initializable {

    public PasswordField newPassword;
    public PasswordField adminPassword;
    public PasswordField newPassword2;
    public ProgressBar bar;
    public Label barLabel;
    public Label name;
    private ChangeUserPasswordController changeUserPasswordController;

    public ChangeUserPasswordController() {
        changeUserPasswordController = this;
    }

    public ChangeUserPasswordController getChangeUserPasswordController() {
        return changeUserPasswordController;
    }

    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();
    FirebaseService firebaseService = new FirebaseService();
    Storage storage= Storage.getStorage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Změna hesla");
        barLabel.setVisible(false);
        bar.setVisible(false);
        name.setText(storage.editedUserId);
    }

    public void check(KeyEvent keyEvent) {
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

    public void back(ActionEvent actionEvent) {
        mainController.mainStackPane.getChildren().clear();
        VBox settings = null;
        try {
            settings = FXMLLoader.load(getClass().getResource("/views/editUser/editUser.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.mainStackPane.getChildren().add(settings);
    }

    public void done(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {
        //Check
        ArrayList<String> mistakes = new ArrayList<String>();
        int fail = 0;
        adminPassword.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        newPassword2.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
        newPassword.setStyle("-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");

        if (adminPassword.getText().isEmpty()) {
            adminPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
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
            validator.displayInfo(mistakes, true);
            return;
        }

        if (!firebaseService.isPasswordCorrect(adminPassword.getText())) {
            adminPassword.setStyle("-fx-border-color: red;-fx-border-radius: 10;-fx-background-color: white; -fx-background-radius: 10;");
            mistakes.add("Vaše heslo se liší od zadaného hesla.");
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
            validator.displayInfo(mistakes, true);
            return;
        }

        //update password
        firebaseService.updatePassword(storage.editedUserId,newPassword.getText());

        //change scene
        mainController.changeScene("editUser/editUser");

        ArrayList<String> info = new ArrayList<String>();
        info.add("Heslo úspěšně změněno.");
        validator.displayInfo(info,false);
    }
}

