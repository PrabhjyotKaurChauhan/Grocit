package com.cargc0044.grocit.model;

/**
 * Created by Nguza Yikona on 4/3/2016.
 */
public class ProductModel {
    public String id;
    public String shop_id;
    public String sub_id;
    public String cat_id;
    public String productTitle;
    public String productImage;
    public String description;
    public String highlight;
    public String status;
    public double price;
    public boolean selected;

    public ProductModel(){}

    public ProductModel(double price, String status, String highlight, String description, String productImage, String productTitle, String cat_id, String sub_id, String shop_id, String id) {
        this.price = price;
        this.status = status;
        this.highlight = highlight;
        this.description = description;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.cat_id = cat_id;
        this.sub_id = sub_id;
        this.shop_id = shop_id;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }



}
