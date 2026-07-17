package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class ProductPicturesTo extends ResponseData {

    private String name;
    private String pictureType;
    private Boolean defaultPicture;
    private String url;

    public ProductPicturesTo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
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
}
