package cz.naseLekarna.system;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */
public class Order {
    private Customer customer;
    private String dateBegin;
    private String orderPickupInfo;
    private String dateEnd;
    private String notes;

    /**
     * This is a contructor for class Order
     * @param customer Customer info
     * @param dateBegin Date of order confirmation
     * @param orderPickupInfo How customer picks up his order
     * @param dateEnd Date of shipping or pick-up
     * @param notes Addition notes for employees
     */
    public Order(Customer customer, String dateBegin, String orderPickupInfo, String dateEnd, String notes) {
        this.customer = customer;
        this.dateBegin = dateBegin;
        this.orderPickupInfo = orderPickupInfo;
        this.dateEnd = dateEnd;
        this.notes = notes;
    }

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
}
