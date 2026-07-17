package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import org.hibernate.annotations.Type;

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
 * Created by Hurshid on 11/14/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "billofmaterial")
public class EdsBillOfMaterial extends EdsObject {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Integer objectID;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "project_id")
    private EdsProject project;

    @Type (type = "text")
    private String description;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "item_id")
    private EdsItem item;

    private String itemName;

    @Column (precision = 14, scale = 4)
    private BigDecimal price;

    @Column (precision = 11, scale = 4)
    private BigDecimal qty;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "unitmeasurementid")
    private EdsUnitMeasurement unitMeasurement;

    private Integer supplierID;
    private String supplierName;
    @Column (name = "sorder", columnDefinition = "int default 0")
    private Integer sorder;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public EdsUnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(EdsUnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getSorder() {
        if (sorder == null) {
            sorder = 0;
        }
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }
}
