package cz.naseLekarna.system;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRecept)) return false;
        ItemRecept that = (ItemRecept) o;
        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
