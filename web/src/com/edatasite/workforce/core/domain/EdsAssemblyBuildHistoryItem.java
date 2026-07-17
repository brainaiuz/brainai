package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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
 * Created by Normurod Buriev.
 * Date: 9/29/2020 5:25 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assemblyBuildHistoryItem")
public class EdsAssemblyBuildHistoryItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private EdsAssemblyItemBuildHistory history;

    @Column(name = "item_id")
    private Integer itemId;

    private String description;

    @Column(name = "qty", precision = 11, scale = 2)
    private BigDecimal qty = new BigDecimal(1);

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "liability_account_id")
    private Integer liabilityAccountId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getLiabilityAccountId() {
        return liabilityAccountId;
    }

    public void setLiabilityAccountId(Integer liabilityAccountId) {
        this.liabilityAccountId = liabilityAccountId;
    }

    public EdsAssemblyItemBuildHistory getHistory() {
        return history;
    }

    public void setHistory(EdsAssemblyItemBuildHistory history) {
        this.history = history;
    }
}
