package cz.naseLekarna.system;

/**
 * @author Matěj Vaník
 * @created 01.02.2022
 */
public class ItemRecept {
    private String code;

    public ItemRecept(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
