package cz.naseLekarna.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */
public class Order {
    private String orderId = null;
    public Integer orderNumber;
    private Customer customer;
    private LocalDate dateBegin;
    private String orderPickupInfo;
    private String state;
    private LocalDate dateEnd;
    private LocalDate datePickUp;
    private String notes;
    private boolean customerFromDb;
    public List<Product> orderedProductList = new ArrayList<Product>();
    public List<Prescription> orderedPrescriptionList = new ArrayList<Prescription>();


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

    public List<Product> getOrderedProductList() {
        return orderedProductList;
    }

    public void setOrderedProductList(List<Product> orderedPripravekList) {
        this.orderedProductList = orderedPripravekList;
    }

    public List<Prescription> getOrderedPrescriptionList() {
        return orderedPrescriptionList;
    }

    public void PrescriptionList(List<Prescription> orderedReceptList) {
        this.orderedPrescriptionList = orderedReceptList;
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

    public LocalDate getDatePickUp() {
        return datePickUp;
    }

    public void setDatePickUp(LocalDate datePickUp) {
        this.datePickUp = datePickUp;
    }

    public boolean isCustomerFromDb() {
        return customerFromDb;
    }

    public void setCustomerFromDb(boolean customerFromDb) {
        this.customerFromDb = customerFromDb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}


