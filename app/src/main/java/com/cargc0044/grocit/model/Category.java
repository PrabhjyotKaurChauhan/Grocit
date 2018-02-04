package com.cargc0044.grocit.model;

/**
 * Created by cyborg on 18/3/16.
 */
public class Category {
    private String id;
    private String title;
    private String iconUrl;
    private String imageUrl;

    public Category() {

    }

    /**
     * @return id in String
     * @brief methods for getting category id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id in String
     * @brief methods for setting category id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return title in String
     * @brief methods for getting title of category
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return title in String
     * @brief methods for setting category title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return imageUrl in String
     * @brief methods for getting imageUrl of category
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return imageUrl in String
     * @brief methods for setting category imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return iconUrl in String
     * @brief methods for getting iconUrl of category
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @return iconUrl in String
     * @brief methods for setting category iconUrl
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}
