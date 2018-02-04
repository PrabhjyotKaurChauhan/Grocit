package com.cargc0044.grocit.model;

/**
 * Created by Harpal Singh on 19-03-2016.
 */
public class items {
    private String name,password;

    public items(String naamee,String pass)
    {
        this.setName(naamee);
        this.setPassword(pass);

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
