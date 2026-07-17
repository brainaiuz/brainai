package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/14/11
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MProductCategoryListItem {
    private Integer objectID;
    private String name;
    private String description;
    private Integer parentCategoryID;
    private List<Integer> storeFrontIDs;
    private Integer storeFrontID;

    //private List<MStoreFrontItem> storeFrontItems;
    private BigDecimal price;

    public MProductCategoryListItem() {
    }


    public MProductCategoryListItem(ProductCategoryItem productCategoryItem) {
        if (productCategoryItem != null) {
            this.objectID = productCategoryItem.getId();
            this.name = productCategoryItem.getName();
            this.description = productCategoryItem.getDescription();
            this.parentCategoryID = productCategoryItem.getParentCategoryID();
            this.storeFrontID = productCategoryItem.getStoreFrontID();
            this.price = productCategoryItem.getPrice();
            if (productCategoryItem.getStoreFrontIDs() != null && productCategoryItem.getStoreFrontIDs().length > 0) {
                storeFrontIDs = new ArrayList<>();
                storeFrontIDs.addAll(Arrays.asList(productCategoryItem.getStoreFrontIDs()));
            }
        }
    }

    public ProductCategoryItem convertToProductCategoryItem(ProductCategoryItem productCategoryItem) {
        productCategoryItem.setId(this.objectID);
        productCategoryItem.setName(this.name);
        productCategoryItem.setDescription(this.description);
        productCategoryItem.setParentCategoryID(this.parentCategoryID);
        if (this.storeFrontIDs != null) {
            productCategoryItem.setStoreFrontIDs(this.storeFrontIDs.toArray(new Integer[]{}));
        } else {
            productCategoryItem.setStoreFrontIDs(new Integer[0]);
        }
        productCategoryItem.setPrice(this.price);
        return productCategoryItem;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public List<Integer> getStoreFrontIDs() {
        return storeFrontIDs;
    }

    public void setStoreFrontIDs(List<Integer> storeFrontIDs) {
        this.storeFrontIDs = storeFrontIDs;
    }

    public Integer getStoreFrontID() {
        return storeFrontID;
    }

    public void setStoreFrontID(Integer storeFrontID) {
        this.storeFrontID = storeFrontID;
    }
}
