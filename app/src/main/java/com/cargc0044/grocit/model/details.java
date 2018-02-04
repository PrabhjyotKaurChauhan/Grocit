package com.cargc0044.grocit.model;

/**
 * Created by Nguza on 30/01/16.
 */
public class details {

    public String name;
    public String password;


    public details(String name, String password) {
        this.setName(name);
        this.setPassword(password);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
