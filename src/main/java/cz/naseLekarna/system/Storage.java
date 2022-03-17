package cz.naseLekarna.system;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matěj Vaník
 * @created 01.02.2022
 */
public class Storage {

    private static Storage storage = new Storage();

    public Storage() {
        storage = this;
    }

    public static Storage getStorage() {
        return storage;
    }

    public User user;
    public Customer customer;
    public Order newOrder;
    public Order editedOrder;

    List<Order> activeOrders = new ArrayList<Order>();
    List<Customer> activeCustomers = new ArrayList<>();


    public List<Customer> getActiveCustomers() {
        return activeCustomers;
    }

    public void setActiveCustomers(List<Customer> activeCustomers) {
        this.activeCustomers = activeCustomers;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Order newOrder) {
        this.newOrder = newOrder;
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(List<Order> activeOrders) {
        this.activeOrders = activeOrders;
    }

    public Order getEditedOrder() {
        return editedOrder;
    }

    public void setEditedOrder(Order editedOrder) {
        this.editedOrder = editedOrder;
    }
}
