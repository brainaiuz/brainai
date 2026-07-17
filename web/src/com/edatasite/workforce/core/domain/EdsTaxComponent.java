package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 09.06.2010
 * Time: 11:33:52
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taxcomponent")
public class EdsTaxComponent extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private Boolean compound;

    @Column(precision = 25, scale = 5)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    private EdsVat tax;

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

    public Boolean getCompound() {
        return compound != null ? compound : false;
    }

    public void setCompound(Boolean compound) {
        this.compound = compound;
    }

    public BigDecimal getRate() {
        return rate != null ? rate : BigDecimal.ZERO;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }
}
