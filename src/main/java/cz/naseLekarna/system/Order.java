package cz.naseLekarna.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */
public class Order {
    private String orderId;
    private Integer orderNumber;
    private Customer customer;
    private LocalDate dateBegin;
    private String orderPickupInfo;
    private LocalDate dateEnd;
    private String notes;
    public List<ItemPripravek> orderedPripravekList = new ArrayList<ItemPripravek>();
    public List<ItemRecept> orderedReceptList = new ArrayList<ItemRecept>();


    //GETTERS AND SETTERS
    public String getOrderPickupInfo() {
        return orderPickupInfo;
    }

    public void setOrderPickupInfo(String orderPickupInfo) {
        this.orderPickupInfo = orderPickupInfo;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDate dateBegin) {
        this.dateBegin = dateBegin;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}


