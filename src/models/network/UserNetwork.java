package models.network;

import com.google.cloud.firestore.annotation.Exclude;

public class UserNetwork {

    private String username;
    private String name;

    public UserNetwork(){}

    public UserNetwork(String username, String name) {
        this.username = username;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
