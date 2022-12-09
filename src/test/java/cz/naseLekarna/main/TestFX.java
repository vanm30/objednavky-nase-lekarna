package cz.naseLekarna.main;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import cz.naseLekarna.system.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;


import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class TestFX extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        String MainDb = "./objednavky-nase-lekarna-firebase-adminsdk-0xdci-ab4bce4c9d.json";
        String TestDb = "./testdb-d23a9-firebase-adminsdk-sxfwn-371008c132.json";
        FileInputStream serviceAccount =
                new FileInputStream(TestDb);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/application/stage.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @After
    public void tearDown() throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void createOrders() throws ExecutionException, InterruptedException {
        Storage storage = Storage.getStorage();
        FirebaseService firebaseService = new FirebaseService();

        for (int i = 1; i <= 100; i++){
            Customer customer = new Customer();
            storage.newOrder = new Order();
            List<Prescription> orderedReceptList = new ArrayList<>();
            List<Product> orderedPripravekList = new ArrayList<>();

            orderedPripravekList.add(new Product(1,"pripravek"));
            orderedReceptList.add(new Prescription("OIUZTGHNBGTR"));

            customer.setStreet("a");
            customer.setName("a");
            customer.setPhoneNumber("602847543");
            customer.setCity("a");
            storage.newOrder.setOrderNumber(i);
            storage.newOrder.setState("Připraveno");
            storage.newOrder.setOrderPickupInfo("Rozvoz");
            storage.newOrder.setDatePickUp(LocalDate.now().plusDays(2));
            storage.newOrder.setDateBegin(LocalDate.now());
            storage.newOrder.setDateEnd(LocalDate.now().plusDays(14));
            storage.newOrder.setCustomer(customer);
            storage.newOrder.setNotes("test");
            storage.newOrder.PrescriptionList(orderedReceptList);
            storage.newOrder.setOrderedProductList(orderedPripravekList);


            firebaseService.addOrder();
        }
    }
    @Test
    public void createCustomers() throws ExecutionException, InterruptedException {
        Storage storage = Storage.getStorage();
        FirebaseService firebaseService = new FirebaseService();

        for (int i = 0; i < 1000; i++){
            storage.customer = new Customer();
            int number = 100000000;
            storage.customer.setStreet("Test Street");
            storage.customer.setName("Test");
            storage.customer.setPhoneNumber(String.valueOf(number + i));
            storage.customer.setCity("Test City");

            firebaseService.addCustomer();
        }
    }

    @Test
    public void viewOrders() throws ExecutionException, InterruptedException {
        clickOn("#userName").write("test");
        clickOn("#password").write("test");
        clickOn("#darkButton");
    }
    @Test
    public void viewCustomers() throws ExecutionException, InterruptedException {
        clickOn("#userName").write("admin");
        clickOn("#password").write("lekarna");
        clickOn("#darkButton");
        clickOn("#menuButton");
        sleep(1000);
        clickOn("#customers");
    }


}
