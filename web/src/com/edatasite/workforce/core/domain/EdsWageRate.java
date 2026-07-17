package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.settings.EdsPayrollZone;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User : Akhror
 * Date : 13.03.2024
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "wage_rate")
public class EdsWageRate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private EdsPayrollZone zone;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "wage_rate_id")
    private List<EdsWageRateItem> items = new ArrayList<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public EdsPayrollZone getZone() {
        return zone;
    }

    public void setZone(EdsPayrollZone zone) {
        this.zone = zone;
    }

    public List<EdsWageRateItem> getItems() {
        return items;
    }

    public void setItems(List<EdsWageRateItem> items) {
        this.items = items;
    }

    public SelectItem toRpc(boolean isSummary) {
        SelectItem item = new SelectItem(getObjectID());
        item.setDate(getEffectiveDate() != null ? new DateNonConvertable(getEffectiveDate()) : null);
        item.setEntityId(getZone() != null ? getZone().getObjectID() : null);
        item.setCategory(getZone() != null ? getZone().getName() : null);

        if (isSummary && getItems() != null) {
            item.setRelatedItems(getItems().stream().map(i -> {
                SelectItem selectItem = new SelectItem();
                selectItem.setEntityId(i.getPosition() != null ? i.getPosition().getObjectID() : null);
                selectItem.setCategory(i.getPosition() != null ? i.getPosition().getName() : null);
                selectItem.setQtyAmount(i.getRate());
                return selectItem;
            }).toList().toArray(new SelectItem[]{}));
        }
        return item;
    }
}
