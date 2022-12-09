package cz.naseLekarna.system;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import cz.naseLekarna.controllers.mainMenu.MainController;

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

    final String place = "orders";

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
        CollectionReference orders = db.collection("customers");
        Query query = orders.orderBy("name");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Customer customer = new Customer();
            customer.setName((String) document.get("name"));
            customer.setPhoneNumber((String) document.get("phoneNumber"));
            customer.setStreet((String) document.get("street"));
            customer.setCity((String) document.get("city"));
            storage.getActiveCustomers().add(customer);
        }
        query.get();
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
        docData.put("customer", new HashMap<String, Object>() {
                    {
                        put("name",storage.newOrder.getCustomer().getName());
                        put("phoneNumber",storage.newOrder.getCustomer().getPhoneNumber());
                        put("street",storage.newOrder.getCustomer().getStreet());
                        put("city",storage.newOrder.getCustomer().getCity());
                    }
                }
        );
        if (!storage.newOrder.orderedProductList.isEmpty()) {
            docData.put("productList", storage.newOrder.getOrderedProductList());
        }
        if (!storage.newOrder.orderedPrescriptionList.isEmpty()) {
            docData.put("prescriptionList", storage.newOrder.getOrderedPrescriptionList());
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
            if (order.orderNumber != null) storage.orderNumbers.add(order.getOrderNumber());
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
        HashMap<String, Object> customerMap = (HashMap<String, Object>) document.get("customer");
        Order order = new Order();
        Customer customer = new Customer();
        if (!customerMap.get("name").equals("")){
            customer.setName((String) customerMap.get("name"));
        } else customer.setName("");
        if (!customerMap.get("phoneNumber").equals("")){
            customer.setPhoneNumber((String) customerMap.get("phoneNumber"));
        } else customer.setPhoneNumber("");
        if (!customerMap.get("street").equals("")){
            customer.setStreet((String) customerMap.get("street"));
        } else customer.setStreet("");
        if (!customerMap.get("city").equals("")){
            customer.setCity((String) customerMap.get("city"));
        } else customer.setCity("");
        order.setCustomer(customer);
        if (document.get("orderNumber") != null){
            order.setOrderNumber(Integer.parseInt(String.valueOf(document.get("orderNumber"))));
        }
        order.setDateBegin(LOCAL_DATE((String) document.get("dateBegin")));
        order.setDateEnd(LOCAL_DATE((String) document.get("dateEnd")));
        order.setDatePickUp(LOCAL_DATE((String) document.get("datePickUp")));
        order.setOrderPickupInfo((String) document.get("orderPickUpInfo"));
        order.setState((String) document.get("state"));
        order.setNotes((String) document.get("notes"));
        order.setOrderId(document.getId());
        ArrayList itemReceptList = (ArrayList) document.get("prescriptionList");
        ArrayList itemPripravekList = (ArrayList) document.get("productList");
        if (itemReceptList != null) {
            for (int i = 0; i < itemReceptList.size(); i++) {
                String code1 = itemReceptList.get(i).toString();
                String code = code1.substring(code1.lastIndexOf("=") + 1);
                order.orderedPrescriptionList.add(new Prescription(code.substring(0, code.length() - 1)));
            }
        }
        if (itemPripravekList != null) {
            for (int i = 0; i < itemPripravekList.size(); i++) {
                String string = itemPripravekList.get(i).toString();
                String x = string.substring(string.lastIndexOf("=") + 1);
                String name = x.substring(0, x.length() - 1);
                String amount = string.substring(string.indexOf("=") + 1, string.indexOf(","));
                order.orderedProductList.add(new Product(Integer.parseInt(amount), name));
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
            storage.user = new User(document.getId(), (String) document.get("username"),(Map<String, Object>) document.get("settings"), (String) document.get("role"));
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
        docData.put("settings", new HashMap<String,Object>(){
            {
                put("autoSave",0);
            }
        });
        docData.put("role","employee");
        ApiFuture<WriteResult> addedDocRef = db.collection("users").document(username).set(docData);
        addedDocRef.get();
    }

    public void deleteCustomer() throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("customers").document(storage.getEditedCustomer().getPhoneNumber()).delete();
        writeResult.get();
    }

    public void deleteCustomer(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("customers").document(id).delete();
        writeResult.get();
    }

    public boolean isPasswordCorrect(String passwordToCheck) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        DocumentReference docRef = db.collection("users").document(storage.user.userName);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        String hashedPasswordToCheck = Logic.getLogic().hashPassword(document.get("salt") + passwordToCheck);
        return document.exists() && hashedPasswordToCheck.equals(document.get("password"));
    }

    public void updatePassword(String id,String password) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();

        String salt = Logic.getLogic().generateSalt();

        String saltedPassword = salt + password;

        docData.put("password", Logic.getLogic().hashPassword(saltedPassword));
        docData.put("salt", salt);

        DocumentReference docRef = db.collection("users").document(id);
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

    public ArrayList<User> getUsers() throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection("users");
        Query query = ref.orderBy("username");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        ArrayList<User> users = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            users.add(new User(document.getId(), (String) document.get("username"), (Map<String, Object>) document.get("settings"), (String) document.get("role")));
        }
        query.get();
        return users;
    }

    public void deleteUser(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = db.collection("users").document(id).delete();
        writeResult.get();
    }

    public void updateUser(String oldId,String newId, Map<String, Object> docData) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(oldId);
        User user = getUser(oldId);
        docData.put("settings",user.settings);
        docData.put("salt",docRef.get().get().get("salt"));
        docData.put("password",docRef.get().get().get("password"));
        deleteUser(oldId);

        ApiFuture<WriteResult> addedDocRef = db.collection("users").document(newId).set(docData);
        addedDocRef.get();
    }
    public User getUser(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        User user = new User(document.getId(), (String) document.get("username"), (Map<String, Object>) document.get("settings"), (String) document.get("role"));
        future.get();
        return user;
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


