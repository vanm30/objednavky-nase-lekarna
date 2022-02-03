package cz.naseLekarna.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Customer customer;
    public Order order;

    public List<ItemPripravek> itemPripravekList = new ArrayList<ItemPripravek>();
    public List<ItemRecept> itemReceptList = new ArrayList<ItemRecept>();
    List<Order> activeOrders = new ArrayList<Order>();


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ItemPripravek> getItemPripravekList() {
        return itemPripravekList;
    }

    public void setItemPripravekList(List<ItemPripravek> itemPripravekList) {
        this.itemPripravekList = itemPripravekList;
    }

    public List<ItemRecept> getItemReceptList() {
        return itemReceptList;
    }

    public void setItemReceptList(List<ItemRecept> itemReceptList) {
        this.itemReceptList = itemReceptList;
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(List<Order> activeOrders) {
        this.activeOrders = activeOrders;
    }
}
