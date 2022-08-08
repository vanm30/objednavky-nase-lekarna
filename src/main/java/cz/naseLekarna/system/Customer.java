package cz.naseLekarna.system;

import java.util.Objects;

/**
 * @author Matěj Vaník
 * @created 29.11.2021
 */
public class Customer {
    private String name;
    private Integer phoneNumber;
    private String street;
    private String city;
    private Boolean save = false;

    //GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getName(), customer.getName()) && Objects.equals(getPhoneNumber(), customer.getPhoneNumber()) && Objects.equals(getStreet(), customer.getStreet()) && Objects.equals(getCity(), customer.getCity()) && Objects.equals(getSave(), customer.getSave());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPhoneNumber(), getStreet(), getCity(), getSave());
    }
}
