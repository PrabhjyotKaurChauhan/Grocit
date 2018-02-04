package com.cargc0044.grocit.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nguza Yikona on 4/10/2016.
 */
public class CartModel {
    private String UsrID;
    private String ProdID;
    private String shopID;
    private String prodName;
    private double price;
    private int quantity;
    private String image;

    public String getUsrID() {
        return UsrID;
    }

    public void setUsrID(String usrID) {
        UsrID = usrID;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
