package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;

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
 * User: administrator
 * Date: 24.02.2009
 * Time: 16:46:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "taxtemplate")
public class EdsTaxTemplate extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCountry country;

    private String name;

    @Column(precision = 11, scale = 2)
    private BigDecimal amount;

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
