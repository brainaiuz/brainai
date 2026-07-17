package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsRFQCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQSolrItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfq")
public class EdsRFQ extends EdsApprovable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer requestFrom;

    private Date date;
    private Date validUntil;

    @Type(type = "text")
    private String number;
    private Integer intNumber;

    @Type(type = "text")
    private String sqNumber;
    @Type(type = "text")
    private String introduction;

    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private EdsCrmAccount client;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rfqid")
    @OrderBy("objectID")
    private List<EdsRFQItem> items = new ArrayList<>();

    @Column(columnDefinition = "boolean default true")
    private boolean sendNotificationToSuppliers = true;

    private Integer opportunityID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceterms")
    private EdsInvoiceTerms invoiceTerms;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mailingaddressid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsAddress mailingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsRFQCustomFields customFields;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'requestforquote' AND (deleted = 'false' or deleted is null) ")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @Transient
    List<CompanyCustomFieldItem> itemCustomFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(Integer requestFrom) {
        this.requestFrom = requestFrom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (!ServerUtils.equalsDate(this.date, date)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.DATE);
        }
        this.date = date;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        if (!ServerUtils.equalsDate(this.validUntil, validUntil)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.DUE_DATE);
        }
        this.validUntil = validUntil;
    }

    public EdsInvoiceTerms getInvoiceTerms() {
        return this.invoiceTerms;
    }

    public void setInvoiceTerms(final EdsInvoiceTerms invoiceTerms) {
        this.invoiceTerms = invoiceTerms;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.NUMBER);
        }
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getSqNumber() {
        return sqNumber;
    }

    public void setSqNumber(String sqNumber) {
        if (!ServerUtils.equalsString(this.sqNumber, sqNumber)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.SQ_NUMBER);
        }
        this.sqNumber = sqNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        if (!ServerUtils.equalsEdsObject(this.creator, creator)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.CREATOR);
        }
        this.creator = creator;
    }

    public List<EdsRFQItem> getItems() {
        if (items == null)
            items = new LinkedList<>();
        return items;
    }

    public void setItems(List<EdsRFQItem> items) {
        this.items = items;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isSendNotificationToSuppliers() {
        return sendNotificationToSuppliers;
    }

    public void setSendNotificationToSuppliers(boolean sendNotificationToSuppliers) {
        this.sendNotificationToSuppliers = sendNotificationToSuppliers;
    }

    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public EdsAddress getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(EdsAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        if (!ServerUtils.equalsEdsObject(this.project, project)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.PROJECT);
        }
        this.project = project;
    }

    public EdsRFQCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsRFQCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        if (!ServerUtils.equalsEdsObject(this.client, client)) {
            addChange(CustomFormConstants.ACCOUNTING.RFQ.CUSTOMER);
        }
        this.client = client;
    }

    public List<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(List<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
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
    public void setEntityStatus(EdsReference status) {
        setOverallStatus(status);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.RFQ_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.RFQ_DECLINED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_DECLINED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_DECLINED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_DECLINED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && Constants.RFQ_DECLINED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.RFQ_STATUS, Constants.RFQ_SUBMITTED));
        }
    }

    public RFQData createRFQData(boolean fullData) {
        RFQData rfqData = new RFQData();
        rfqData.setObjectID(getObjectID());
        rfqData.setRequestFrom(getRequestFrom());
        rfqData.setProject(getProject() != null ? getProject().getAsSelectItem() : null);
        rfqData.setDate(new DateNonConvertable(getDate()));
        rfqData.setValidUntil(new DateNonConvertable(getValidUntil()));
        rfqData.setNumberData(new NumberData(getNumber(), getIntNumber()));
        rfqData.getNumberData().setNumberFormat("RFQ_0001");
        rfqData.setSqNumber(getSqNumber());
        rfqData.setIntroduction(getIntroduction());
        if (getOverallStatus() != null) {
            rfqData.setStatusCode(getOverallStatus().getCode());
        }
        rfqData.setSendNotificationToSuppliers(isSendNotificationToSuppliers());
        rfqData.setOpportunityID(getOpportunityID());
        if (getClient() != null) {
            rfqData.setCustomer(getClient().getAsSelectItem());
            if (getClient().getBillingAddress() != null && getClient().getBillingAddress().getCountry() != null) {
                rfqData.setClientAddress(getClient().getBillingAddress().getCountry().getName());
            } else if (getClient().getMailingAddress() != null && getClient().getMailingAddress().getCountry() != null) {
                rfqData.setClientAddress(getClient().getMailingAddress().getCountry().getName());
            }
        }
        if (getMailingAddress() != null) {
            rfqData.setMailAddressId(getMailingAddress().getObjectID());
        }

        if (fullData) {
            ArrayList<RFQItem> itemsList = new ArrayList<>();
            for (EdsRFQItem edsRFQItem : items) {
                itemsList.add(edsRFQItem.createItemData(getItemCustomFields()));
            }
            rfqData.setItems(itemsList);
        }
        initApproverData(rfqData);
        return rfqData;
    }

    public RFQSolrItem getSolrRPC() {
        RFQSolrItem rfqSolrItem = new RFQSolrItem();

        rfqSolrItem.setObjectID(getObjectID());
        rfqSolrItem.setRfqNumber(getNumber());
        if (getClient() != null) {
            rfqSolrItem.setClient(getClient().getAsSelectItem());
            if (getClient().getBillingAddress() != null && getClient().getBillingAddress().getCountry() != null) {
                rfqSolrItem.setCountry(getClient().getBillingAddress().getCountry().getAsSelectItem());
            } else if (getClient().getMailingAddress() != null && getClient().getMailingAddress().getCountry() != null) {
                rfqSolrItem.setCountry(getClient().getMailingAddress().getCountry().getAsSelectItem());
            }
        }
        if (getProject() != null) {
            SelectItem project = new SelectItem(getProject().getObjectID(), getProject().getName());
            project.setNumber(getProject().getNumber());
            rfqSolrItem.setRelatedProject(project);
        }
        if (getOverallStatus() != null) {
            rfqSolrItem.setStatus(getOverallStatus().getRPC());
        }
        if (getCreator() != null) {
            rfqSolrItem.setCreator(getCreator().getAsSelectItem());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            SelectItem approver = new SelectItem(getCurrentApprover().getExactEmployee().getObjectID(), getCurrentApprover().getExactEmployee().getFullName());
            rfqSolrItem.setCurrentApprover(approver);
        }

        if (!getItems().isEmpty()) {
            getItems().stream()
                    .filter(edsRFQItem -> edsRFQItem.getObjectID() != null)
                    .forEach(edsRFQItem
                            -> rfqSolrItem.getItemIds().add(edsRFQItem.getObjectID()));

        }

        rfqSolrItem.setDueDate(getValidUntil());
        rfqSolrItem.setRfqDate(getDate());
        if (getCreationDate() != null) {
            rfqSolrItem.setCreationDate(getCreationDate());
        }

        return rfqSolrItem;
    }

    @Override
    public void jumpToPreviousApprover() {
        super.jumpToPreviousApprover();
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    public SolrInputDocument wrapToSolrDocument(EdsRFQ rfq, Integer companyID) {
        String compositID = companyID + "_" + rfq.getObjectID();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID, rfq.getObjectID());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_RFQ_NUMBER, rfq.getNumber());
        if (rfq.getClient() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, rfq.getClient().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, rfq.getClient().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME, rfq.getClient().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + rfq.getClient().getName());
            if (rfq.getClient().getBillingAddress() != null && rfq.getClient().getBillingAddress().getCountry() != null) {
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_COUNTRY_ID, rfq.getClient().getBillingAddress().getCountry().getObjectID());
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_COUNTRY_NAME, rfq.getClient().getBillingAddress().getCountry().getName());
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_COUNTRY_ID_NAME, rfq.getClient().getBillingAddress().getCountry().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + rfq.getClient().getBillingAddress().getCountry().getName());
            } else if (getClient().getMailingAddress() != null && getClient().getMailingAddress().getCountry() != null) {
                rfq.getClient().getMailingAddress().getCountry().getName();
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_COUNTRY_ID, rfq.getClient().getMailingAddress().getCountry().getObjectID());
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_COUNTRY_NAME, rfq.getClient().getMailingAddress().getCountry().getName());
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_COUNTRY_ID_NAME, rfq.getClient().getMailingAddress().getCountry().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + rfq.getClient().getMailingAddress().getCountry().getName());
            }
        }
        if (rfq.getProject() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, rfq.getProject().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, rfq.getProject().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NUMBER, rfq.getProject().getNumber());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME, rfq.getProject().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + rfq.getProject().getName());
        }
        if (rfq.getOverallStatus() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, rfq.getOverallStatus().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, rfq.getOverallStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME, rfq.getOverallStatus().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + rfq.getOverallStatus().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_SORDER, rfq.getOverallStatus().getSorder());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_CODE, rfq.getOverallStatus().getCode());
        }
        if (rfq.getCreator() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, rfq.getCreator().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME, rfq.getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME, rfq.getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + rfq.getCreator().getName());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }

        if (CollectionUtils.isNotEmpty(getItems())) {
            getItems().stream()
                           .filter(edsRFQItem -> edsRFQItem.getObjectID() != null)
                           .forEach(edsRFQItem
                                            -> doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_ITEM_ID, edsRFQItem.getObjectID()));

        }

        doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, rfq.getValidUntil());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE, rfq.getDate());
        if (rfq.getCreationDate() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATION_DATE, rfq.getCreationDate());
        }
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());

        return doc;
    }
    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        String[] values = fieldID.split(",");
        fieldID = values.length >= 2 ? values[0] : fieldID;
        switch (fieldID) {
            case CustomFormConstants.ACCOUNTING.RFQ.SQ_NUMBER -> {
                return getSqNumber();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.NUMBER -> {
                return getNumber();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.DATE -> {
                return getDate();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.DUE_DATE -> {
                return getValidUntil();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.PROJECT -> {
                return getProject();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.CUSTOMER -> {
                return getClient();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.CREATOR -> {
                return getCreator();
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_PRODUCT -> {
                List<EdsItem> edsItems = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    edsItems.add(item.getProduct());
                }
                return edsItems;
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_DESCRIPTION -> {
                List<String> descriptions = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    descriptions.add(item.getDescription());
                }
                return descriptions;
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_QUANTITY -> {
                List<BigDecimal> quantities = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    quantities.add(item.getQty());
                }
                return quantities;
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_REMARK -> {
                List<String> remarks = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    remarks.add(item.getRemarks());
                }
                return remarks;
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_MEASUREMENT -> {
                List<EdsUnitMeasurement> measurements = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    measurements.add(item.getMeasurement());
                }
                return measurements;
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_COST -> {
                List<BigDecimal> costs = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    costs.add(item.getUnitCost());
                }
                return costs;
            }
            case CustomFormConstants.ACCOUNTING.RFQ.ITEM_SUPPLIER -> {
                List<EdsCrmAccount> suppliers = new ArrayList<>();
                for (EdsRFQItem item : getItems()) {
                    suppliers.add(item.getSupplier());
                }
                return suppliers;
            }
            default -> {
                return super.getRealValue(fieldID);
            }
        }
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field == null || field.getField_ID() == null) {
            super.setValueForField(field, value);
            return;
        }
        String fieldId = field.getField_ID();
        switch (fieldId) {
            case CustomFormConstants.ACCOUNTING.RFQ.SQ_NUMBER -> setSqNumber((String) value);
            case CustomFormConstants.ACCOUNTING.RFQ.DATE -> setDate((Date) value);
            case CustomFormConstants.ACCOUNTING.RFQ.DUE_DATE -> setValidUntil((Date) value);
            case CustomFormConstants.ACCOUNTING.RFQ.PROJECT -> setProject((EdsProject) value);
        }
        super.setValueForField(field, value);
    }
}
