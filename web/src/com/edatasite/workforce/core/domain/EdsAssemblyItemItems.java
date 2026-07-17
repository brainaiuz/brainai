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


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assemblyItemItems")
public class EdsAssemblyItemItems extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer itemId;

    private String description;

    private BigDecimal quantity;

    private Integer warehouseID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assembly_id")
    private EdsSavedAssemblyItem edsSavedAssemblyItem;

    @Override
    public Integer getObjectID() {
        return objectID;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public EdsSavedAssemblyItem getEdsSavedAssemblyItem() {
        return edsSavedAssemblyItem;
    }

    public void setEdsSavedAssemblyItem(EdsSavedAssemblyItem edsSavedAssemblyItem) {
        this.edsSavedAssemblyItem = edsSavedAssemblyItem;
    }
}
