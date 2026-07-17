package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Normurod on 6/15/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "consignmentitem")
public class EdsConsignmentItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consignmentid")
    private EdsConsignment consignment;

    private String fromCompany;
    private Integer fromCompanyID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromid")
    private EdsCrmAccount from;

    private String toCompany;
    private Integer toCompanyID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toid")
    private EdsCrmAccount to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private EdsItem product;

    @Column(precision = 25, scale = 5)
    private BigDecimal quantity = BigDecimal.ONE;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsConsignment getConsignment() {
        return consignment;
    }

    public void setConsignment(EdsConsignment consignment) {
        this.consignment = consignment;
    }

    public EdsItem getProduct() {
        return product;
    }

    public void setProduct(EdsItem product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(String fromCompany) {
        this.fromCompany = fromCompany;
    }

    public Integer getFromCompanyID() {
        return fromCompanyID;
    }

    public void setFromCompanyID(Integer fromCompanyID) {
        this.fromCompanyID = fromCompanyID;
    }

    public String getToCompany() {
        return toCompany;
    }

    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    public Integer getToCompanyID() {
        return toCompanyID;
    }

    public void setToCompanyID(Integer toCompanyID) {
        this.toCompanyID = toCompanyID;
    }

    public EdsCrmAccount getFrom() {
        return from;
    }

    public void setFrom(EdsCrmAccount from) {
        this.from = from;
    }

    public EdsCrmAccount getTo() {
        return to;
    }

    public void setTo(EdsCrmAccount to) {
        this.to = to;
    }

    public ConsignmentItem getRPC() {
        ConsignmentItem item = new ConsignmentItem();

        item.setObjectID(getObjectID());

        if (getFrom() != null) {
            item.setFromCompany(getFrom().getAsSelectItem());
            /*if (getFrom().getSubsidiary() != null) {
                item.setFromCompany(new SelectItem(getFrom().getObjectID(), getFrom().getName(), getFrom().getSubsidiary().getCompanyId().toString()));
            } else {
                item.setFromCompany(getFrom().getAsSelectItem());
            }*/
        }
        item.setFromCompanyID(getFromCompanyID());
        if (getTo() != null) {
            item.setToCompany(getTo().getAsSelectItem());
            /*if (getTo().getSubsidiary() != null) {
                item.setFromCompany(new SelectItem(getTo().getObjectID(), getTo().getName(), getTo().getSubsidiary().getCompanyId().toString()));
            } else {
                item.setToCompany(getTo().getAsSelectItem());
            }*/
        }
        item.setToCompanyID(getToCompanyID());
        if (getProduct() != null) {
            item.setProduct(new SelectItem(getProduct().getObjectID(), getProduct().getProductNumber() + " -> " + getProduct().getName(), getProduct().getSubsidiaryProductUniqNum()));
        }
        item.setQuantity(getQuantity());
        return item;
    }
}
