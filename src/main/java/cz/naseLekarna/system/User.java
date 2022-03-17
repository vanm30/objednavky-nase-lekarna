package cz.naseLekarna.system;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Matěj Vaník
 * @created 17.03.2022
 */
public class User {

    public String id;
    public String userName;
    public ArrayList<String> settings;

    public User(String id, String userName, ArrayList<String> settings) {
        this.id = id;
        this.userName = userName;
        this.settings = settings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<String> settings) {
        this.settings = settings;
    }
}
