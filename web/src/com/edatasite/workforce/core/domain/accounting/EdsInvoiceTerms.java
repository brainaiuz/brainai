package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/11/12
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoiceterms")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsInvoiceTerms extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    private Integer days;

    private Boolean deleted = false;

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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public InvoiceTermsItem getAsRPC() {
        return new InvoiceTermsItem(getObjectID(), getName(), getDays());
    }
}
