package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 21, 2011
 * Time: 1:55:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "googleCheckoutOrder")
public class EdsGoogleCheckoutOrder extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "orderNumber")
    private String orderNumber;

    @Column(name = "orderSummary")
    @Type(type = "text")
    private String orderSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usageplanid")
    private EdsUsagePlan usagePlan;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderSummary() {
        return orderSummary;
    }

    public void setOrderSummary(String orderSummary) {
        this.orderSummary = orderSummary;
    }

    public EdsUsagePlan getUsagePlan() {
        return usagePlan;
    }

    public void setUsagePlan(EdsUsagePlan usagePlan) {
        this.usagePlan = usagePlan;
    }
}
