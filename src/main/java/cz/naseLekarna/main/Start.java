package cz.naseLekarna.main;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 08.11.2021
 */
public class Start extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/stage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("naše lékárna");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount =
                new FileInputStream("./objednavky-nase-lekarna-firebase-adminsdk-0xdci-ab4bce4c9d.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        BasicConfigurator.configure();
        launch(args);
    }

}
