package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfqsupplierbid")
public class EdsRFQSupplierBid extends EdsObject implements ObjectHistory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfqitemid")
    private EdsRFQItem rfqItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierid")
    private EdsCrmAccount supplier;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater")
    private EdsUser updater;

    private Date creationTime;
    private Date lastUpdateTime;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsRFQItem getRfqItem() {
        return rfqItem;
    }

    public void setRfqItem(EdsRFQItem rfqItem) {
        this.rfqItem = rfqItem;
    }

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmAccount supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public void setLastUpdateTime(Date value) {
        this.lastUpdateTime = value;
    }

    @Override
    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    @Override
    public void setCreationTime(Date value) {
        this.creationTime = value;
    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }
}
