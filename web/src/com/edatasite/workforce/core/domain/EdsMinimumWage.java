package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User : Akhror
 * Date : 12.03.2024
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "minimum_wage")
public class EdsMinimumWage extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public SelectItem getAsSelectItem() {
        SelectItem item = new SelectItem(getObjectID());
        item.setDate(getEffectiveDate() != null ? new DateNonConvertable(getEffectiveDate()) : null);
        item.setQtyAmount(getAmount());
        item.setCode(getType());
        return item;
    }
}
