package cz.naseLekarna.system;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import cz.naseLekarna.gui.mainMenu.MainController;

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

    final String place = "testOrders";

    /**
     * Method adds customer to Firebase
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void addCustomer() throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", storage.customer.getName());
        docData.put("phoneNumber", storage.customer.getPhoneNumber());
        docData.put("street", storage.customer.getStreet());
        docData.put("city", storage.customer.getCity());
        ApiFuture<WriteResult> addedDocRef = db.collection("customers").document(storage.customer.getPhoneNumber()).set(docData);
        addedDocRef.get();
    }
    public boolean addCustomer(Map<String, Object> docData, String Id) throws ExecutionException, InterruptedException {
        //check if id is used
        DocumentReference docRef = db.collection("customers").document(Id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()){
            return false;
        } else {
            ApiFuture<WriteResult> addedDocRef = db.collection("customers").document(Id).set(docData);
            addedDocRef.get();
            return true;
        }
    }

    /**
     * Method loads customer to storage
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void loadCustomers() throws ExecutionException, InterruptedException {
        storage.activeCustomers.clear();
        ApiFuture<QuerySnapshot> future = db.collection("customers").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Customer customer = new Customer();
            customer.setName((String) document.get("name"));
            customer.setPhoneNumber((String) document.get("phoneNumber"));
            customer.setStreet((String) document.get("street"));
            customer.setCity((String) document.get("city"));
            storage.getActiveCustomers().add(customer);
        }
        future.get();
    }

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
        docData.put("state",storage.newOrder.getState());
        docData.put("dateEnd", storage.newOrder.getDateEnd().toString());
        docData.put("datePickUp", storage.newOrder.getDatePickUp().toString());
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
        storage.orderNumbers.clear();
        storage.orderNames.clear();
        CollectionReference orders = db.collection(place);
        Query query = orders.orderBy("dateEnd");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Order order = writeDownInfo(document);
            storage.getActiveOrders().add(order);
            if (order.orderNumber != null){
                storage.orderNumbers.add(order.getOrderNumber());
            }
            storage.orderNames.add(order.getCustomer().getName());
        }
        query.get();
    }

    public void getInfoForEdit(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(place).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Order order = writeDownInfo(document);
        storage.setEditedOrder(order);
        future.get();
    }

    public int getCustomerInfo(String id) throws ExecutionException, InterruptedException {
        int result = 0;
        if (MainController.getMainController().mainStackPane.getChildren().get(0).getId().equals("customersList")){
            storage.editedCustomer = new Customer();
            DocumentReference docRef = db.collection("customers").document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            storage.editedCustomer.setName((String) document.get("name"));
            storage.editedCustomer.setPhoneNumber((String) document.get("phoneNumber"));
            storage.editedCustomer.setCity((String) document.get("city"));
            storage.editedCustomer.setStreet((String) document.get("street"));
            future.get();
            result = 1;
        }
        if (MainController.getMainController().mainStackPane.getChildren().get(0).getId().equals("findCustomers")){
            DocumentReference docRef = db.collection("customers").document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            storage.newOrder.getCustomer().setName((String) document.get("name"));
            storage.newOrder.getCustomer().setPhoneNumber((String) document.get("phoneNumber"));
            storage.newOrder.getCustomer().setCity((String) document.get("city"));
            storage.newOrder.getCustomer().setStreet((String) document.get("street"));
            storage.newOrder.setCustomerFromDb(true);
            future.get();
            result = 2;
        }
        return result;
    }

    public void forgetCustomerInfo(){
        storage.newOrder.getCustomer().setName(null);
        storage.newOrder.getCustomer().setPhoneNumber(null);
        storage.newOrder.getCustomer().setCity(null);
        storage.newOrder.getCustomer().setStreet(null);
        storage.newOrder.setCustomerFromDb(false);
    }

    public void updateOrder(Map<String, Object> docData) throws ExecutionException, InterruptedException {
        DocumentReference docRefTest = db.collection("orders").document(storage.editedOrder.getOrderId());
        ApiFuture<DocumentSnapshot> future = docRefTest.get();
        DocumentSnapshot document = future.get();
        Order order = writeDownInfo(document);
        DocumentReference docRef = db.collection("orders").document(storage.getEditedOrder().getOrderId());
        ApiFuture<WriteResult> writeResult = docRef.update(docData);
        writeResult.get();
    }

    public void updateSettings(Map<String, Object> docData) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(storage.user.id);
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
        customer.setPhoneNumber((String) listCustomer.get(1));
        customer.setStreet((String) listCustomer.get(2));
        customer.setCity((String) listCustomer.get(3));
        order.setCustomer(customer);
        if (document.get("orderNumber") != null){
            order.setOrderNumber(Integer.parseInt(String.valueOf(document.get("orderNumber"))));
        }
        order.setDateBegin(LOCAL_DATE((String) document.get("dateBegin")));
        order.setDateEnd(LOCAL_DATE((String) document.get("dateEnd")));
        order.setDatePickUp(LOCAL_DATE((String) document.get("datePickUp")));
        order.setOrderPickupInfo((String) document.get("orderPickUpInfo"));
        order.setState((String) document.get("state"));
        System.out.println((String) document.get("state"));
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
        DocumentReference docRef = db.collection("users").document(userName);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        String hashedEntryPassword = Logic.getLogic().hashPassword(document.get("salt") + password);
        if (document.exists() && hashedEntryPassword.equals(document.get("password"))){
            storage.user = new User(document.getId(), (String) document.get("username"),(ArrayList) document.get("settings"));
            return true;
        } else return false;
    }

    public boolean userNameExists(String username) throws ExecutionException, InterruptedException {
        boolean result = false;
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            if (username.equals(document.getId())) {
                return true;
            }
        }
        return result;
    }

    public void addUser(String username, String password) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        Map<String, Object> docData = new HashMap<>();

        String salt = Logic.getLogic().generateSalt();

        String saltedPassword = salt + password;

        docData.put("username", username);
        docData.put("password", Logic.getLogic().hashPassword(saltedPassword));
        docData.put("salt", salt);
        docData.put("settings", Arrays.asList(
                "0"
        ));
        ApiFuture<WriteResult> addedDocRef = db.collection("users").document(username).set(docData);
        addedDocRef.get();
    }

    public void deleteCustomer() throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("customers").document(storage.getEditedCustomer().getPhoneNumber()).delete();
        writeResult.get();
    }

    public void updateCustomer(Map<String, Object> docData, String oldId, String newId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("customers").document(oldId);
        docRef.delete();

        ApiFuture<WriteResult> addedDocRef = db.collection("customers").document(newId).set(docData);
        addedDocRef.get();
    }

    public boolean isPasswordCorrect(String passwordToCheck) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        DocumentReference docRef = db.collection("users").document(storage.user.userName);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        String hashedPasswordToCheck = Logic.getLogic().hashPassword(document.get("salt") + passwordToCheck);
        return document.exists() && hashedPasswordToCheck.equals(document.get("password"));
    }

    public void updatePassword(String password) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();

        String salt = Logic.getLogic().generateSalt();

        String saltedPassword = salt + password;

        docData.put("password", Logic.getLogic().hashPassword(saltedPassword));
        docData.put("salt", salt);

        DocumentReference docRef = db.collection("users").document(storage.user.id);
        ApiFuture<WriteResult> writeResult = docRef.update(docData);
        writeResult.get();
    }

    public boolean checkPhoneNumber(String Id) throws ExecutionException, InterruptedException {
        //check if id is used
        DocumentReference docRef = db.collection("customers").document(Id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        return document.exists();
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


