package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 26.09.11
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MProductPicture {

    private String name;
    private Integer pictureID;
    private String pictureType;
    private Integer productID;
    private Boolean defaultPicture;
    private String url;
    private String urlOriginal;
    private String urlMedium;
    private String urlSmall;

    public MProductPicture() {

    }

    public MProductPicture(ProductPicture productPicture) {
        this.name = productPicture.getName();
        this.pictureID = productPicture.getPictureID();
        this.pictureType = productPicture.getPictureType();
        this.productID = productPicture.getProductID();
        this.defaultPicture = productPicture.isDefaultPicture();
        this.url = productPicture.getUrl();
        this.urlOriginal = productPicture.getUrlOriginal();
        this.urlMedium = productPicture.getUrlMedium();
        this.urlSmall = productPicture.getUrlSmall();
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

    public Boolean getDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(Boolean defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
