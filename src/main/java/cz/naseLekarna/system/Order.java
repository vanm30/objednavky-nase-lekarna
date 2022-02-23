package cz.naseLekarna.system;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */
public class Order {
    private String orderID;
    private Customer customer;
    private String dateBegin;
    private String orderPickupInfo;
    private String dateEnd;
    private String notes;
    public List<ItemPripravek> orderedPripravekList = new ArrayList<ItemPripravek>();
    public List<ItemRecept> orderedReceptList = new ArrayList<ItemRecept>();


    //GETTERS AND SETTERS
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getOrderPickupInfo() {
        return orderPickupInfo;
    }

    public void setOrderPickupInfo(String orderPickupInfo) {
        this.orderPickupInfo = orderPickupInfo;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ItemPripravek> getOrderedPripravekList() {
        return orderedPripravekList;
    }

    public void setOrderedPripravekList(List<ItemPripravek> orderedPripravekList) {
        this.orderedPripravekList = orderedPripravekList;
    }

    public List<ItemRecept> getOrderedReceptList() {
        return orderedReceptList;
    }

    public void setOrderedReceptList(List<ItemRecept> orderedReceptList) {
        this.orderedReceptList = orderedReceptList;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}


