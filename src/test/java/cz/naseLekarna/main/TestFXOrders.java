package cz.naseLekarna.main;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;


import java.io.FileInputStream;


public class TestFXOrders extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FileInputStream serviceAccount =
                new FileInputStream("./objednavky-nase-lekarna-firebase-adminsdk-0xdci-ab4bce4c9d.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        BasicConfigurator.configure();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/main.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
    @Before
    public void setUp() throws Exception{

    }

    @After
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testOpen() {

    }

}
