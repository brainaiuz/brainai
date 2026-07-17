package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product;


import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 19, 2010
 * Time: 3:16:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductPicture implements Serializable {

    private String name;
    private Integer pictureID;
    private String pictureType;
    private Integer productID;
    private Boolean defaultPicture;
    private String url;
    private String urlOriginal;
    private String urlMedium;
    private String urlSmall;

    public ProductPicture() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPictureID() {
        return pictureID;
    }

    public void setPictureID(Integer pictureID) {
        this.pictureID = pictureID;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Boolean isDefaultPicture() {
        return defaultPicture != null ? defaultPicture : false;
    }

    public void setDefaultPicture(Boolean defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getUrlMedium() {
        return urlMedium;
    }

    public void setUrlMedium(String urlMedium) {
        this.urlMedium = urlMedium;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}