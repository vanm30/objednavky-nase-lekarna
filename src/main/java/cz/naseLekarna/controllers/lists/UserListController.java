package cz.naseLekarna.controllers.lists;

import cz.naseLekarna.controllers.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.User;
import cz.naseLekarna.system.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class UserListController implements Initializable {

    private UserListController userListController;

    public UserListController() {
        userListController = this;
    }

    public UserListController getUserListController() {
        return userListController;
    }

    @FXML
    public VBox usersList;

    MainController mainController = MainController.getMainController();
    Storage storage = Storage.getStorage();
    Validator validator = new Validator();
    FirebaseService firebaseService = new FirebaseService();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Správa zaměstnanců");
        if (!storage.user.getRole().equals("admin")) {
            try {

                validator.displayInfo(new ArrayList<String>(Collections.singleton("K těmto úpravám je oprávněn pouze admin.")), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadUsers() throws IOException, ExecutionException, InterruptedException {
        ArrayList<User> users = firebaseService.getUsers();
        for (User user : users) {
            if (!user.id.equals("admin") && !storage.user.id.equals(user.id)){
                mainController.mainStackPane.getChildren().clear();
                Node node = FXMLLoader.load(getClass().getResource("/views/editUser/user.fxml"));
                final Text name = (Text) node.lookup("#name");
                final Text id = (Text) node.lookup("#id");
                name.setText(user.userName);
                id.setText(user.id);
                usersList.getChildren().add(node);
            }
        }
    }

    public void backToSetting() throws IOException {
        mainController.changeScene("mainMenu/settings");
    }
}
