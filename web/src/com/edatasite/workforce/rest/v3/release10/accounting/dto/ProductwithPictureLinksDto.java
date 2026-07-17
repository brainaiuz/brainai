package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import java.util.List;

public class ProductwithPictureLinksDto {
    private Integer productId;
    private String productNumber;
    private String productName;

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    private String defultPictureLink;
    private List<String> pictureList;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDefultPictureLink() {
        return defultPictureLink;
    }

    public void setDefultPictureLink(String defultPictureLink) {
        this.defultPictureLink = defultPictureLink;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }
}
