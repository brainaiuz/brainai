package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
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
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anvar Akramov on 5/3/2019.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bill_of_entry")
public class EdsBillOfEntry extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    /*@Column(name = "purchase_invoice_id")
    private Integer purchaseInvoiceId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_invoice_id", updatable = false, insertable = false)
    private EdsPurchaseInvoice purchaseInvoice;*/

    @Column(name = "boe_number")
    private String boeNumber;

    @Column(name = "port_id")
    private Integer portId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id", updatable = false, insertable = false)
    private EdsReference port;

    @Column(name = "boe_date")
    private Date boeDate;
    @Column(name = "reference")
    private String reference;
    @Column(name = "description")
    @Type(type = "text")
    private String description;

    //Paid Through
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_through_id")
    private EdsAccount paidThrough;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "billofentry_id")
//    @OrderBy("objectID")
    private List<EdsBillOfEntryItem> items = new ArrayList<>();

    @Column(name = "total_custom_duty", precision = 25, scale = 5)
    private BigDecimal totalCustomDuty;//Total Custom Duty + Additional Charges
    @Column(name = "total_tax_amount", precision = 25, scale = 5)
    private BigDecimal totalTaxAmount;//Total Tax Amount
    @Column(name = "total_amount", precision = 25, scale = 5)
    private BigDecimal totalAmount;//Total Amount (AED) :

    @Column(name = "vat_return_id")
    private Integer vatReturnId;

    public EdsBillOfEntry() {
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    /*public EdsPurchaseInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(EdsPurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }*/

    public String getBoeNumber() {
        return boeNumber;
    }

    public void setBoeNumber(String boeNumber) {
        this.boeNumber = boeNumber;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public EdsReference getPort() {
        return port;
    }

    public void setPort(EdsReference port) {
        this.port = port;
    }

    public Date getBoeDate() {
        return boeDate;
    }

    public void setBoeDate(Date boeDate) {
        this.boeDate = boeDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsAccount getPaidThrough() {
        return paidThrough;
    }

    public void setPaidThrough(EdsAccount paidThrough) {
        this.paidThrough = paidThrough;
    }

    public BigDecimal getTotalCustomDuty() {
        return totalCustomDuty;
    }

    public void setTotalCustomDuty(BigDecimal totalCustomDuty) {
        this.totalCustomDuty = totalCustomDuty;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<EdsBillOfEntryItem> getItems() {
        return items;
    }

    public void setItems(List<EdsBillOfEntryItem> items) {
        this.items = items;
    }
}
