package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

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
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/19/12
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "comission_allocate_item")
public class EdsComissionAllocateItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private EdsSaleQuote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_man_id")
    private EdsUser salesMan;

    private Double comissionPercent;

    @Column(precision = 25, scale = 5)
    private BigDecimal allocateAmount;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsSaleQuote getQuote() {
        return quote;
    }

    public void setQuote(EdsSaleQuote quote) {
        this.quote = quote;
    }

    public EdsUser getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(EdsUser salesMan) {
        this.salesMan = salesMan;
    }

    public Double getComissionPercent() {
        return comissionPercent;
    }

    public void setComissionPercent(Double comissionPercent) {
        this.comissionPercent = comissionPercent;
    }

    public BigDecimal getAllocateAmount() {
        return allocateAmount;
    }

    public void setAllocateAmount(BigDecimal allocateAmount) {
        this.allocateAmount = allocateAmount;
    }
}
