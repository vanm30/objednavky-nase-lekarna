package cz.naseLekarna.system;

/**
 * @author Matěj Vaník
 * @created 01.02.2022
 */
public class ItemPripravek {
    private String name;
    private int amount;

    public ItemPripravek(int amount, String name) {
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
}
