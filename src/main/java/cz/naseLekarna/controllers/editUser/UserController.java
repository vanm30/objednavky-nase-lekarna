package cz.naseLekarna.controllers.editUser;


import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserController {

    public Text id;
    public Text name;
    private static UserController userController;

    public UserController() {
        userController = this;
    }

    public static UserController getUserController() {
        return userController;
    }

    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();
    Validator validator = new Validator();
    Storage storage = Storage.getStorage();


    public void deleteUser(ActionEvent actionEvent) throws ExecutionException, InterruptedException, IOException {
        firebaseService.deleteUser(id.getText());
        mainController.changeScene("lists/userList");
    }

    public void saveUser(ActionEvent actionEvent) throws ExecutionException, InterruptedException, IOException {
        if (name.getText().isEmpty()) {
            validator.displayInfo(new ArrayList<>(Collections.singleton("Pro uložení zadejte uživatelské jméno.")),true);
            return;
        }
        if (name.getText().equals(id.getText())){
            return;
        }
        if (!firebaseService.userNameExists(name.getText())){
            Map<String, Object> docData = new HashMap<>();
            docData.put("username", name.getText());
            firebaseService.updateUser(id.getText(),name.getText(),docData);
            validator.displayInfo(new ArrayList<String>(Collections.singleton("Uživatel uložen.")),false);
        } else {
            validator.displayInfo(new ArrayList<String>(Collections.singleton("Uživtelské jméno již existuje.")),true);
        }

        mainController.changeScene("lists/userList");
    }

    public void editUser(ActionEvent actionEvent) throws IOException {
        storage.editedUserId = id.getText();
        mainController.changeScene("editUser/editUser");
    }




}
