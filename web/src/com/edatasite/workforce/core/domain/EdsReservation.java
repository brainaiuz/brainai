package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 3:42:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "reservation")
public class EdsReservation extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    private Date fromDate;
    private Date toDate;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid")
    private EdsWarehouse location;

    private Integer qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientid")
    private EdsCrmAccount client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private EdsProductCategory group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemid")
    private EdsItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid")
    private EdsEvent event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingmethod")
    private EdsShippingMethod shippingMethod;

    private Integer status;

    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsWarehouse getLocation() {
        return location;
    }

    public void setLocation(EdsWarehouse location) {
        this.location = location;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public EdsProductCategory getGroup() {
        return group;
    }

    public void setGroup(EdsProductCategory group) {
        this.group = group;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public EdsEvent getEvent() {
        return event;
    }

    public void setEvent(EdsEvent event) {
        this.event = event;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(EdsShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
}
