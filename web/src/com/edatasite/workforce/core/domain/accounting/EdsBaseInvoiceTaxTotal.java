package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsVat;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 09.04.2010
 * Time: 19:06:17
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class EdsBaseInvoiceTaxTotal extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatid")
    private EdsVat vat;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        this.vat = vat;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
