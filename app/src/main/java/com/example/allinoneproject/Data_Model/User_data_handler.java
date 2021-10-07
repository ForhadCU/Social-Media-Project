package com.example.allinoneproject;

import com.google.firebase.firestore.Exclude;

public class User_data_handler {
    private String userName ;
    private String email;
    private String password;
    private String status;

    User_data_handler()
    {

    }

    public User_data_handler(String userName, String email, String password, String status) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.status = status;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getStatus() {
        return status;
    }

}
