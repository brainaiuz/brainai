package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentmethod")
public class EdsPaymentMethod extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    @Column(name="description")
    @Type(type="string")
    private String description;
    private String code;
    private Integer sortOrder;

    @Column(name="deleted")
    @Type(type="boolean")
    private Boolean deleted=false;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "paymentCategory")
//    private EdsPaymentCategory paymentCategory;

    private Integer weigth;

    private Date lastused;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    public EdsPaymentCategory getPaymentCategory() {
//        return paymentCategory;
//    }
//
//    public void setPaymentCategory(EdsPaymentCategory paymentCategory) {
//        this.paymentCategory = paymentCategory;
//    }

    public Integer getWeigth() {
        return weigth;
    }

    public void setWeigth(Integer weigth) {
        this.weigth = weigth;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getLastused() {
        return lastused;
    }

    public void setLastused(Date lastused) {
        this.lastused = lastused;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PaymentMethodItem getRPC(){
        PaymentMethodItem pmi = new PaymentMethodItem();
        pmi.setObjectID(getObjectID());
        pmi.setCode(getCode());
        pmi.setName(getName());
        pmi.setDescription(getDescription());
        pmi.setSortOrder(getSortOrder());
        pmi.setLastused(getLastused());
        pmi.setWeigth(getWeigth());
        return pmi;
    }
}
