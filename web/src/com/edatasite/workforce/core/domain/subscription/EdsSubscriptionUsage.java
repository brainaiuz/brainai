package com.edatasite.workforce.core.domain.subscription;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User : Akhror
 * Date : 03.11.2023
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "subscription_usage")
public class EdsSubscriptionUsage extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private EdsSubscription subscription;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private EdsCrmContact supplier;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(EdsSubscription subscription) {
        this.subscription = subscription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsCrmContact getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmContact supplier) {
        this.supplier = supplier;
    }
}
