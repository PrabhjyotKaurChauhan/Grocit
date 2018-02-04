package com.cargc0044.grocit.activity;

/**
 * Created by Hahn on 28/01/16.
 */
public class ParseUser {
    public Object signUpInBackground;
    private String rUname;
    private String rEmail;
    private String rPhone;
    private String rPassword;
    private String rPasswordConfirm;
    private String rAddress;

    public void setrUname(String rUname) {
        this.rUname = rUname;
    }

    public void setrEmail(String rEmail) {
        this.rEmail = rEmail;
    }

    public void setrPhone(String rPhone) {
        this.rPhone = rPhone;
    }

    public void setrPassword(String rPassword) {
        this.rPassword = rPassword;
    }

    public void setrPasswordConfirm(String rPasswordConfirm) {
        this.rPasswordConfirm = rPasswordConfirm;
    }

    public void setrAddress(String rAddress) {
        this.rAddress = rAddress;
    }

    public void signUpInBackground(SignUpCallback sucessfull) {
    }
}
