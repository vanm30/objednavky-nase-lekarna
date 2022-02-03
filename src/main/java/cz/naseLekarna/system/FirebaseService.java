package cz.naseLekarna.system;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.*;
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
        db.collection("customers").document(String.valueOf(storage.customer.getPhoneNumber())).set(docData);

    }

    public void addOrder() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("customer", Arrays.asList(
                storage.order.getCustomer().getName(),
                storage.order.getCustomer().getPhoneNumber(),
                storage.order.getCustomer().getStreet(),
                storage.order.getCustomer().getCity()
        ));
        if (!storage.itemPripravekList.isEmpty()){
            docData.put("itemPripravekList", storage.itemPripravekList);
        }
        if (!storage.itemReceptList.isEmpty()){
            docData.put("itemReceptList", storage.itemReceptList);
        }
        docData.put("dateBegin", storage.order.getDateBegin());
        docData.put("orderPickUpInfo", storage.order.getOrderPickupInfo());
        docData.put("dateEnd", storage.order.getDateEnd());
        docData.put("notes", storage.order.getNotes());
        db.collection("orders").add(docData);


    }
    public void loadOrders() throws ExecutionException, InterruptedException {
        storage.activeOrders.clear();
        ApiFuture<QuerySnapshot> future = db.collection("orders").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()){
            for (QueryDocumentSnapshot document : documents) {
                List list = (List) document.get("customer");
                Customer customer = new Customer(list.get(0).toString(),Integer.parseInt(String.valueOf(list.get(1))),list.get(2).toString(),list.get(3).toString());
                Order order = new Order(customer,document.get("dateBegin"),document.get("orderPickupInfo"),document.get("dateEnd"),document.get("notes"));
                storage.activeOrders.add(order);
            }
        }
    }

}
