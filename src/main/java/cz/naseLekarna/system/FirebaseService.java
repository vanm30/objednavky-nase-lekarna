package cz.naseLekarna.system;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
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
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addUser() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", storage.customer.getName());
        docData.put("phoneNumber", storage.customer.getPhoneNumber());
        docData.put("street", storage.customer.getStreet());
        docData.put("city", storage.customer.getCity());
        ApiFuture<DocumentReference> addedDocRef = db.collection("customer").add(docData);
        addedDocRef.get();
    }

    /* *//**
     * Method loads Users to storage
     * @throws ExecutionException
     * @throws InterruptedException
     *//*
    public void loadUsers() throws ExecutionException, InterruptedException {
        storage.activeCustomers.clear();

        ApiFuture<QuerySnapshot> future = db.collection("customers").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Customer customer = new Customer(document.get("name"), Integer.parseInt(document.get("phoneNumber").toString()), document.get("street"), document.get("city"));
            storage.getActiveCustomers().add(customer);
        }
        future.get();
    }*/

    /**
     * Method adds order to Firebase
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addOrder() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("orderNumber", storage.newOrder.getOrderNumber());
        docData.put("customer", Arrays.asList(
                storage.newOrder.getCustomer().getName(),
                storage.newOrder.getCustomer().getPhoneNumber(),
                storage.newOrder.getCustomer().getStreet(),
                storage.newOrder.getCustomer().getCity()
        ));
        if (!storage.newOrder.orderedPripravekList.isEmpty()) {
            docData.put("itemPripravekList", storage.newOrder.getOrderedPripravekList());
        }
        if (!storage.newOrder.orderedReceptList.isEmpty()) {
            docData.put("itemReceptList", storage.newOrder.getOrderedReceptList());
        }
        docData.put("dateBegin", storage.newOrder.getDateBegin().toString());
        docData.put("orderPickUpInfo", storage.newOrder.getOrderPickupInfo());
        docData.put("dateEnd", storage.newOrder.getDateEnd().toString());
        docData.put("notes", storage.newOrder.getNotes());
        ApiFuture<DocumentReference> addedDocRef = db.collection("orders").add(docData);
        addedDocRef.get();
    }

    /*
     * Method loads orders to local storage
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void loadOrders() throws ExecutionException, InterruptedException {
        storage.activeOrders.clear();
        CollectionReference orders = db.collection("orders");
        Query query = orders.orderBy("dateEnd");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Order order = writeDownInfo(document);
            storage.getActiveOrders().add(order);
        }
        query.get();
    }


    public void getInfoForEdit(String id) throws ExecutionException, InterruptedException {
        //Save information to storage > editedOrder
        DocumentReference docRef = db.collection("orders").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Order order = writeDownInfo(document);
        storage.setEditedOrder(order);
        future.get();
    }

    public void updateOrder(Map<String, Object> docData) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("orders").document(storage.getEditedOrder().getOrderId());
        ApiFuture<WriteResult> writeResult = docRef.update(docData);
        writeResult.get();
    }

    public void deleteOrder() throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("orders").document(storage.getEditedOrder().getOrderId()).delete();
        writeResult.get();
    }

    public Order writeDownInfo(DocumentSnapshot document) {
        List listCustomer = (List) document.get("customer");
        Order order = new Order();
        Customer customer = new Customer();
        customer.setName((String) listCustomer.get(0));
        if (listCustomer.get(1) != null) {
            customer.setPhoneNumber(Integer.parseInt(String.valueOf(listCustomer.get(1))));
        }
        customer.setStreet((String) listCustomer.get(2));
        customer.setCity((String) listCustomer.get(3));
        order.setCustomer(customer);

        order.setOrderNumber(Integer.parseInt(String.valueOf(document.get("orderNumber"))));
        order.setDateBegin(LOCAL_DATE((String) document.get("dateBegin")));
        order.setDateEnd(LOCAL_DATE((String) document.get("dateEnd")));
        order.setOrderPickupInfo((String) document.get("orderPickUpInfo"));
        order.setNotes((String) document.get("notes"));
        order.setOrderId(document.getId());
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
        return order;
    }

    public boolean validateLogin(String userName, String password) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        ApiFuture<QuerySnapshot> future = db.collection("employees").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        String hashedEntryPassword = Logic.getLogic().hashPassword(password);

        for (QueryDocumentSnapshot document : documents) {
            if (userName.equals(document.get("username")) && hashedEntryPassword.equals(document.get("password"))) {
                return true;
            }
        }
        return false;
    }

    //TODO zda je pouzivanej

    public void addEmployee(String username, String password) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("username", username);
        docData.put("password", Logic.getLogic().hashPassword(password));
        ApiFuture<DocumentReference> addedDocRef = db.collection("employees").add(docData);
        addedDocRef.get();


    }

    /**
     * Method converts String to LocalDate
     *
     * @param dateString
     * @return date in LocalDate type
     */
    public static LocalDate LOCAL_DATE(String dateString) {
        LocalDate local_date = LocalDate.parse(dateString);
        return local_date;
    }

}


