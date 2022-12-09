package cz.naseLekarna.system;

import java.util.Map;

/**
 * @author Matěj Vaník
 * @created 17.03.2022
 */
public class User {

    public String id;
    public String userName;
    private String role;
    public Map<String, Object> settings;

    public User(String id, String userName, Map<String, Object> settings, String role) {
        this.id = id;
        this.userName = userName;
        this.settings = settings;
        this.role = role;
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

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
