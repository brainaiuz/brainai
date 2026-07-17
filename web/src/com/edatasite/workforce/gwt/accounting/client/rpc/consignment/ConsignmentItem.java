package com.edatasite.workforce.gwt.accounting.client.rpc.consignment;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Normurod on 6/15/15.
 */
public class ConsignmentItem implements IsSerializable, Serializable {

    private Integer objectID;
    private SelectItem fromCompany;
    private SelectItem toCompany;
    private SelectItem product;

    private BigDecimal quantity;

    private Integer fromCompanyID;
    private Integer toCompanyID;

    public SelectItem getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(SelectItem fromCompany) {
        this.fromCompany = fromCompany;
    }

    public SelectItem getToCompany() {
        return toCompany;
    }

    public void setToCompany(SelectItem toCompany) {
        this.toCompany = toCompany;
    }

    public SelectItem getProduct() {
        return product;
    }

    public void setProduct(SelectItem product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getFromCompanyID() {
        return fromCompanyID;
    }

    public void setFromCompanyID(Integer fromCompanyID) {
        this.fromCompanyID = fromCompanyID;
    }

    public Integer getToCompanyID() {
        return toCompanyID;
    }

    public void setToCompanyID(Integer toCompanyID) {
        this.toCompanyID = toCompanyID;
    }
}
