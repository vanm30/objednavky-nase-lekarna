package cz.naseLekarna.controllers.editUser;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.User;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class EditUserController implements Initializable {

    public TextField username;
    public Label id;
    public ChoiceBox<String> role;
    private EditUserController editUserController;

    public EditUserController() {
        editUserController = this;
    }

    public EditUserController getEditUserController() {
        return editUserController;
    }

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        role.getItems().add("admin");
        role.getItems().add("zaměstnanec");
        mainController.mainLabel.setText("Správa zaměstnance");

        User user;
        try {
            user = firebaseService.getUser(storage.editedUserId);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        id.setText(user.userName);
        username.setText(user.id);
        if (user.getRole().equals("admin")) role.setValue("admin");
        else if (user.getRole().equals("employee")) role.setValue("zaměstnanec");;
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("editUser/changeUserPassword");
    }

    public void back(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("lists/userList");
    }

    public void save(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        ArrayList<String> mistakes = new ArrayList<>();
        if (username.getText().isEmpty()) {
            username.setStyle("-fx-border-color: red; -fx-border-radius: 10;-fx-background-radius: 10");
            mistakes.add("Pro uložení zadejte uživatelské jméno.");
//            validator.displayFail(new ArrayList<>(Collections.singleton("Pro uložení zadejte uživatelské jméno.")),true);
        }

        if (username.getText().equals(id.getText()) || !firebaseService.userNameExists(username.getText())){
            Map<String, Object> docData = new HashMap<>();
            docData.put("username", username.getText());
            if (role.getValue().equals("admin")) docData.put("role","admin");
            else if (role.getValue().equals("zaměstnanec")) docData.put("role","employee");
            firebaseService.updateUser(id.getText(),username.getText(),docData);
            validator.displayInfo(new ArrayList<String>(Collections.singleton("Uživatel uložen.")),false);
            mainController.changeScene("lists/userList");
        } else {
            validator.displayInfo(new ArrayList<String>(Collections.singleton("Uživtelské jméno již existuje.")),true);
        }
    }
}
