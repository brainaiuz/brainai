package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsRentalOrderCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

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
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rental_order")
public class EdsRentalOrder extends EdsApprovable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private EdsCrmAccount customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "termsId")
    private EdsInvoiceTerms terms;

    @Column(name = "invoice_id")
    private Integer invoiceID;

    @Column(name = "invoice_name")
    private String invoiceName;

    private Date startDate;
    private Date expirationDate;

    private Boolean deleted = false;

    @Type(type = "text")
    private String number;
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsRentalOrderCustomFields customFields;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Column(name = "taxAmount", precision = 25, scale = 5)
    private BigDecimal taxAmount;

    @Column(name = "subTotal", precision = 25, scale = 5)
    private BigDecimal subTotal;

    @Column(name = "total", precision = 25, scale = 5)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_order_id")
    private List<EdsRentalOrderItem> items = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'RENTAL_ORDER'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCrmAccount getCustomer() {
        return this.customer;
    }

    public void setCustomer(final EdsCrmAccount customer) {
        this.customer = customer;
    }

    public EdsInvoiceTerms getTerms() {
        return this.terms;
    }

    public void setTerms(final EdsInvoiceTerms terms) {
        this.terms = terms;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return this.intNumber;
    }

    public void setIntNumber(final Integer intNumber) {
        this.intNumber = intNumber;
    }

    public List<EdsRentalOrderItem> getItems() {
        return this.items;
    }

    public void setItems(final List<EdsRentalOrderItem> items) {
        this.items = items;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public EdsUser getCreator() {
        return this.creator;
    }

    public void setCreator(final EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return this.updater;
    }

    public void setUpdater(final EdsUser updater) {
        this.updater = updater;
    }

    public EdsRentalOrderCustomFields getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final EdsRentalOrderCustomFields customFields) {
        this.customFields = customFields;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(final BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    public EdsReference getStatus() {
        return this.status;
    }

    public void setStatus(final EdsReference status) {
        this.status = status;
    }

    public RentalOrderData createRentalOrderData() {
        RentalOrderData rentalOrderData = new RentalOrderData();
        rentalOrderData.setObjectID(getObjectID());
        rentalOrderData.setNumberData(new NumberData(getNumber(), getIntNumber()));
        rentalOrderData.getNumberData().setNumberFormat("BOOKING_0001");
        rentalOrderData.setStartDate(getStartDate());
        rentalOrderData.setExpirationDate(getExpirationDate());
        rentalOrderData.setSubTotal(getSubTotal());
        rentalOrderData.setTotal(getTotal());
        rentalOrderData.setTaxAmount(getTaxAmount());
        rentalOrderData.setInvoiceItem(new SelectItem(getInvoiceID(), getInvoiceName()));
        rentalOrderData.setCreator(getCreator() != null ? new SelectItem(getCreator().getObjectID(), getCreator().getName()) : null);
        rentalOrderData.setCreatedDate(getCreatedDate());
        rentalOrderData.setUpdatedDate(getUpdatedDate());
        rentalOrderData.setTaxAmount(getTaxAmount());
//        initApproverData(rentalOrderData);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            rentalOrderData.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (getStatus() != null) {
            rentalOrderData.setStatus(getStatus().getAsSelectItem());
            rentalOrderData.setStatusCode(getStatus().getCode());
        }
        if (getCustomer() != null) {
            rentalOrderData.setCustomer(getCustomer().getAsSelectItem());
        }
        if (getTerms() != null) {
            rentalOrderData.setPaymentTerms(getTerms().getAsSelectItem());
        }
        return rentalOrderData;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setStatus(overallStatus);
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.RENTAL_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.RENTAL_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_REJECTED);
        }
        return null;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID.equals(AccountingCustomFormConstants.CUSTOMER)) {
            return getCustomer();
        } else if (fieldID.equals(CustomFormConstants.CLIENT_INVOICE_TERM)) {
            return getTerms();
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            return getStartDate();
        } else if (fieldID.equals(CustomFormConstants.DATE)) {
            return getExpirationDate();
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getNumber();
        }
        return super.getRealValue(fieldID);
    }
}
