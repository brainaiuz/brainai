package com.workforcetrack.mobile.rpc.accounting;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 26.09.11
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MProductCategoryItem {

    private Integer objectID;
    private String name;
    private String description;
    private Integer parentCategoryID;
    private String parentCategoryName;
    private Integer companyID;
    private String companyName;
    private List<Integer> storeFrontIDs;
    private List<Integer> websiteIDs;
    private Integer storeFrontID;
    private BigDecimal price;
    private String priceAsString;
    private List<MProductPicture> pictures;

    private Boolean includeCustomField = false;

    public MProductCategoryItem() {

    }

    public MProductCategoryItem(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(Integer parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Integer> getStoreFrontIDs() {
        return storeFrontIDs;
    }

    public void setStoreFrontIDs(List<Integer> storeFrontIDs) {
        this.storeFrontIDs = storeFrontIDs;
    }

    public List<Integer> getWebsiteIDs() {
        return websiteIDs;
    }

    public void setWebsiteIDs(List<Integer> websiteIDs) {
        this.websiteIDs = websiteIDs;
    }

    public Integer getStoreFrontID() {
        return storeFrontID;
    }

    public void setStoreFrontID(Integer storeFrontID) {
        this.storeFrontID = storeFrontID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceAsString() {
        return priceAsString;
    }

    public void setPriceAsString(String priceAsString) {
        this.priceAsString = priceAsString;
    }

    public List<MProductPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<MProductPicture> pictures) {
        this.pictures = pictures;
    }

    public Boolean getIncludeCustomField() {
        return includeCustomField;
    }

    public void setIncludeCustomField(Boolean includeCustomField) {
        this.includeCustomField = includeCustomField;
    }
}
