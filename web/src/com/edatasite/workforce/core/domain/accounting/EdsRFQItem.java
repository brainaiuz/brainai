package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsRFQItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import org.hibernate.annotations.Type;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfqitem")
public class EdsRFQItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfqid")
    private EdsRFQ rfq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private EdsItem product;

    @Type(type = "text")
    private String name;

    @Type(type = "text")
    private String description;

    @Type(type = "text")
    private String remarks;

    private BigDecimal qty;
    private BigDecimal commission;

    private BigDecimal unitCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierid")
    private EdsCrmAccount supplier;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rfqitemid")
    @OrderBy("objectID")
    private List<EdsRFQSupplierBid> supplierBids = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurementid")
    private EdsUnitMeasurement measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseorderid")
    private EdsPurchaseOrder purchaseOrder;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsRFQItemCustomFields customFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsRFQ getRfq() {
        return rfq;
    }

    public void setRfq(EdsRFQ rfq) {
        this.rfq = rfq;
    }

    public EdsItem getProduct() {
        return product;
    }

    public void setProduct(EdsItem product) {
        this.product = product;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmAccount supplier) {
        this.supplier = supplier;
    }

    public List<EdsRFQSupplierBid> getSupplierBids() {
        return supplierBids;
    }

    public void setSupplierBids(List<EdsRFQSupplierBid> supplierBids) {
        this.supplierBids = supplierBids;
    }

    public EdsUnitMeasurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(EdsUnitMeasurement measurement) {
        this.measurement = measurement;
    }

    public EdsPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public EdsRFQItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsRFQItemCustomFields customFields) {
        this.customFields = customFields;
    }

    public RFQItem createItemData(List<CompanyCustomFieldItem> itemCustomFields) {
        RFQItem rfqItem = new RFQItem();
        rfqItem.setObjectID(getObjectID());
        if (getProduct() != null) {
            rfqItem.setProduct(getProduct().getAsProductSelectItem());
        } else if (getName() != null) {
            rfqItem.setProduct(new ProductSelectItem(null, getName()));
        }
        rfqItem.setDescription(getDescription());
        rfqItem.setQty(getQty());
        rfqItem.setCommission(getCommission());
        rfqItem.setReMarks(getRemarks());

        if (getMeasurement() != null) {
            rfqItem.setMeasurement(getMeasurement().getAsSelectItem());
        }
        if (getSupplierBids() != null && getSupplierBids().size() > 0) {
            rfqItem.setUnitCost(getSupplierBids().get(0).getAmount());
            rfqItem.setSupplier(getSupplierBids().get(0).getSupplier().getAsSelectItem());
        } else {
            rfqItem.setUnitCost(getUnitCost());
            if (getSupplier() != null) {
                rfqItem.setSupplier(getSupplier().getAsSelectItem());
            }
        }
        rfqItem.setConverted(isConverted());
        if (itemCustomFields != null && getCustomFields() != null) {
            ArrayList<CompanyCustomFieldItem> items = new ArrayList<>();

            for (CompanyCustomFieldItem item : itemCustomFields) {
                items.add(item.cloneObject());
            }
            rfqItem.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(getCustomFields(), items));
        }

        return rfqItem;
    }

    public boolean isConverted() {
        return getPurchaseOrder() != null;
    }
}
