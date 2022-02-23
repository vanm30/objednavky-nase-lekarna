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

    /**
     * Method adds user to Firebase
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addUser() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", storage.customer.getName());
        docData.put("phoneNumber", storage.customer.getPhoneNumber());
        docData.put("street", storage.customer.getStreet());
        docData.put("city", storage.customer.getCity());
        db.collection("customers").document(String.valueOf(storage.customer.getPhoneNumber())).set(docData);

    }

    /**
     * Method loads Users to storage
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void loadUsers() throws ExecutionException, InterruptedException {
        storage.activeCustomers.clear();

        ApiFuture<QuerySnapshot> future = db.collection("customers").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Customer customer = new Customer(document.get("name"), Integer.parseInt(document.get("phoneNumber").toString()), document.get("street"), document.get("city"));
            storage.getActiveCustomers().add(customer);
        }

    }

    /**
     * Method adds order to Firebase
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addOrder() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("customer", Arrays.asList(
                storage.order.getCustomer().getName(),
                storage.order.getCustomer().getPhoneNumber(),
                storage.order.getCustomer().getStreet(),
                storage.order.getCustomer().getCity()
        ));
        if (!storage.order.orderedPripravekList.isEmpty()) {
            docData.put("itemPripravekList", storage.order.getOrderedPripravekList());
        }
        if (!storage.order.orderedReceptList.isEmpty()) {
            docData.put("itemReceptList", storage.order.getOrderedReceptList());
        }
        docData.put("dateBegin", storage.order.getDateBegin());
        docData.put("orderPickUpInfo", storage.order.getOrderPickupInfo());
        docData.put("dateEnd", storage.order.getDateEnd());
        docData.put("notes", storage.order.getNotes());
        db.collection("orders").add(docData);


    }

    /**
     * Method loads orders to storage
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void loadOrders() throws ExecutionException, InterruptedException {
        storage.activeOrders.clear();
        CollectionReference orders = db.collection("orders");
        Query query = orders.orderBy("dateEnd");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();


        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            List listCustomer = (List) document.get("customer");
            Customer customer = new Customer(listCustomer.get(0).toString(), Integer.parseInt(String.valueOf(listCustomer.get(1))), listCustomer.get(2).toString(), listCustomer.get(3).toString());
            Order order = new Order();
            order.setCustomer(customer);
            order.setDateBegin((String) document.get("dateBegin"));
            order.setDateEnd((String) document.get("dateEnd"));
            order.setOrderPickupInfo((String) document.get("orderPickUpInfo"));
            order.setNotes((String) document.get("notes"));
            ArrayList itemReceptList = (ArrayList) document.get("itemReceptList");
            ArrayList itemPripravekList = (ArrayList) document.get("itemPripravekList");
            if (itemReceptList != null) {
                for (int i = 0; i < itemReceptList.size(); i++) {
                    String code1 = itemReceptList.get(i).toString();
                    String code = code1.substring(code1.lastIndexOf("=") + 1);
                    order.orderedReceptList.add(new ItemRecept(code.substring(0, code.length() - 1)));
                }
            }
            if (itemPripravekList != null) {
                for (int i = 0; i < itemPripravekList.size(); i++) {
                    String string = itemPripravekList.get(i).toString();
                    String x = string.substring(string.lastIndexOf("=") + 1);
                    String name = x.substring(0, x.length() - 1);
                    String amount = string.substring(string.indexOf("=") + 1, string.indexOf(","));
                    order.orderedPripravekList.add(new ItemPripravek(Integer.parseInt(amount), name));
                }
            }
            storage.getActiveOrders().add(order);
        }
    }
    public void getInfoForEdit(String name) throws ExecutionException, InterruptedException {
        //Find ID of clicled document
        ApiFuture<QuerySnapshot> future = db.collection("orders").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        String id = null;
        for (QueryDocumentSnapshot document : documents) {
            List list = (List) document.get("customer");
            if (name.equals(list.get(0))){
                id = document.getId();
            }
        }

        //Save information to storage > editedOrder
        DocumentReference docRef = db.collection("orders").document(id);
        ApiFuture<DocumentSnapshot> future2 = docRef.get();
        DocumentSnapshot document = future2.get();

        List list = (List) document.get("customer");

        Order order = new Order();
        Customer customer = new Customer(list.get(0),Integer.parseInt(list.get(1).toString()),list.get(2).toString(),list.get(3).toString());

        order.setCustomer(customer);
        order.setDateBegin((String) document.get("dateBegin"));
        order.setDateEnd((String) document.get("dateEnd"));
        order.setOrderPickupInfo((String) document.get("orderPickUpInfo"));
        order.setNotes((String) document.get("notes"));
        order.setOrderID(id);

        ArrayList itemReceptList = (ArrayList) document.get("itemReceptList");
        ArrayList itemPripravekList = (ArrayList) document.get("itemPripravekList");
        if (itemReceptList != null) {
            for (int i = 0; i < itemReceptList.size(); i++) {
                String code1 = itemReceptList.get(i).toString();
                String code = code1.substring(code1.lastIndexOf("=") + 1);
                order.orderedReceptList.add(new ItemRecept(code.substring(0, code.length() - 1)));
            }
        }
        if (itemPripravekList != null) {
            for (int i = 0; i < itemPripravekList.size(); i++) {
                String string = itemPripravekList.get(i).toString();
                String x = string.substring(string.lastIndexOf("=") + 1);
                String name2 = x.substring(0, x.length() - 1);
                String amount = string.substring(string.indexOf("=") + 1, string.indexOf(","));
                order.orderedPripravekList.add(new ItemPripravek(Integer.parseInt(amount), name2));
            }
        }
        storage.setEditedOrder(order);
    }

    public void updateOrder(Map<String, Object> docData){

        DocumentReference docRef = db.collection("orders").document(storage.getEditedOrder().getOrderID());
        docRef.update(docData);

    }

    public void deleteOrder() throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("orders").document(storage.getEditedOrder().getOrderID()).delete();
        writeResult.get();
    }

}


