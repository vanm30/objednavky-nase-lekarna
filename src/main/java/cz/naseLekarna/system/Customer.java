package cz.naseLekarna.system;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */
public class Customer {
    private String name;
    private int phoneNumber;
    private String street;
    private String city;
    private Boolean save = false;


    /**
     * This is a contructor for class Customer.
     *
     * @param name        Customers name
     * @param phoneNumber Customers phone number
     * @param street      Customers address - street
     * @param city        Customers address - city
     */
    public Customer(String name, int phoneNumber, String street, String city) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
    }

    public Customer(Object name, Object phoneNumber, Object street, Object city) {
        this.name = (String) name;
        this.phoneNumber = (int) phoneNumber;
        this.street = (String) street;
        this.city = (String) city;
    }

    //GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }
}