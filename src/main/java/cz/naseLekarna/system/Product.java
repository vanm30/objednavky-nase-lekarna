package cz.naseLekarna.system;

import java.util.Objects;

/**
 * @author Matěj Vaník
 * @created 01.02.2022
 */
public class Product {
    private String name;
    private int amount;

    public Product(int amount, String name) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return getAmount() == that.getAmount() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAmount());
    }
}
