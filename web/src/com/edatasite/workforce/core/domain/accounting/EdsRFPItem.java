
package com.edatasite.workforce.core.domain.accounting;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.customfields.EdsRFPItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfpitem")
public class EdsRFPItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfpid")
    private EdsRFP rfp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private EdsItem product;

    @Column(name = "itemName")
    @Type(type = "text")
    private String itemName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    EdsWarehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurementid")
    private EdsUnitMeasurement measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @Type(type = "text")
    private String description;


    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsRFPItemCustomFields customFields;

    private Boolean selected;

    private BigDecimal qty;

    private Integer entityID;

    private Boolean hasProductList;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsRFP getRfp() {
        return rfp;
    }

    public void setRfp(EdsRFP rfp) {
        this.rfp = rfp;
    }

    public EdsItem getProduct() {
        return product;
    }

    public void setProduct(EdsItem product) {
        this.product = product;
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

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public EdsUnitMeasurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(EdsUnitMeasurement measurement) {
        this.measurement = measurement;
    }


    public EdsDepartment getDepartment() {
        return this.department;
    }

    public void setDepartment(final EdsDepartment department) {
        this.department = department;
    }

    public Boolean getSelected() {
        return selected != null && selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public EdsRFPItemCustomFields getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final EdsRFPItemCustomFields customFields) {
        this.customFields = customFields;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public Boolean getHasProductList() {
        return this.hasProductList;
    }

    public void setHasProductList(final Boolean hasProductList) {
        this.hasProductList = hasProductList;
    }

    public RFPItem createRFPItem(boolean copy, List<CompanyCustomFieldItem> itemCustomFields) {
        RFPItem item = new RFPItem();
        if (!copy) {
            item.setObjectID(getObjectID());
        }

        if (getProduct() != null && getProduct().getAsProductSelectItem() != null && !"Type here to search...".equals(getProduct().getAsProductSelectItem().getName())) {
            item.setProductItem(getProduct().getAsProductSelectItem());
            item.setQtyOnhand(getProduct().getItemsInStock());
        } else if (getProduct() == null && getItemName() != null && !"Type here to search...".equals(getItemName())) {
            item.setProductItem(new ProductSelectItem(getObjectID(), getItemName()));
        }
        item.setHasProductList(/*getHasProductList()*/ true);
        item.setDescription(getDescription());
        if (getMeasurement() != null) {
            item.setMeasurement(getMeasurement().getAsSelectItem());
        }
        if (getDepartment() != null) {
            item.setDepartmentItem(getDepartment().getAsSelectItem());
        }
        item.setQty(getQty());
        item.setEntityID(getEntityID());
        item.setWareHouse(getWarehouse() != null ? getWarehouse().getAsSelectItem() : null);

        item.setSelected(getSelected());
        if (itemCustomFields != null && getCustomFields() != null) {
            ArrayList<CompanyCustomFieldItem> items = new ArrayList<>();

            for (CompanyCustomFieldItem cf : itemCustomFields) {
                items.add(cf.cloneObject());
            }
            item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(getCustomFields(), items));
        }

        return item;
    }
}
