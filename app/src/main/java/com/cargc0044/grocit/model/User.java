package com.cargc0044.grocit.model;

/**
 * Created by Nguza on 30/01/16.
 */
public class User {
    public String phoneNumber;
    public String fname;
    public String lname;
    public String email;
    public String password;
    private String id;
    private String other_contact;
    private String genderId;


    public User(){}

    /**
     * @param fname
     * @param lname
     * @param email
     * @param phoneNumber
     * @param password
     * @brief default constructor
     */

    public User(String fname, String lname, String email, String phoneNumber, String password) {

    }

    /**
     * @return id in String
     * @brief get id of user
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOther_contact() {
        return other_contact;
    }

    public void setOther_contact(String other_contact) {
        this.other_contact = other_contact;
    }

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }


}
