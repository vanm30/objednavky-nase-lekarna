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
    CustomerInfoController customerInfoController = CustomerInfoController.getCustomerInfoController();

    public void addUser() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", customerInfoController.customer.getName());
        docData.put("phoneNumber",customerInfoController.customer.getPhoneNumber());
        docData.put("street", customerInfoController.customer.getStreet());
        docData.put("city", customerInfoController.customer.getCity());
        docData.put("zip", customerInfoController.customer.getZip());
        db.collection("customers").document(customerInfoController.phoneNumber.getText()).set(docData);

    }

    public void addOrder() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("customer", Arrays.asList(
                customerInfoController.order.getCustomer().getPhoneNumber(),
                customerInfoController.order.getCustomer().getName(),
                customerInfoController.order.getCustomer().getStreet(),
                customerInfoController.order.getCustomer().getCity(),
                customerInfoController.order.getCustomer().getZip()
                ));
        docData.put("dateBegin", customerInfoController.order.getDateBegin());
        docData.put("orderPickUpInfo", customerInfoController.order.getOrderPickupInfo());
        docData.put("dateEnd", customerInfoController.order.getDateEnd());
        docData.put("notes", customerInfoController.order.getNotes());
        db.collection("orders").add(docData);


    }
}
