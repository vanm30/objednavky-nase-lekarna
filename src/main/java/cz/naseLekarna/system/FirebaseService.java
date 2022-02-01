package cz.naseLekarna.system;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import cz.naseLekarna.gui.CustomerInfoController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */

public class FirebaseService {

    Firestore db = FirestoreClient.getFirestore();
    Storage storage = Storage.getStorage();

    public void addUser() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", storage.customer.getName());
        docData.put("phoneNumber", storage.customer.getPhoneNumber());
        docData.put("street", storage.customer.getStreet());
        docData.put("city", storage.customer.getCity());
        docData.put("zip", storage.customer.getZip());
        db.collection("customers").document(String.valueOf(storage.customer.getPhoneNumber())).set(docData);

    }

    public void addOrder() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("customer", Arrays.asList(
                storage.order.getCustomer().getPhoneNumber(),
                storage.order.getCustomer().getName(),
                storage.order.getCustomer().getStreet(),
                storage.order.getCustomer().getCity(),
                storage.order.getCustomer().getZip()
        ));
        docData.put("itemReceptList", storage.itemReceptList);
        docData.put("itemPripravekList", storage.itemPripravekList);
        docData.put("dateBegin", storage.order.getDateBegin());
        docData.put("orderPickUpInfo", storage.order.getOrderPickupInfo());
        docData.put("dateEnd", storage.order.getDateEnd());
        docData.put("notes", storage.order.getNotes());
        db.collection("orders").add(docData);


    }
}
