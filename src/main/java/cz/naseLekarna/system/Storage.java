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

}
