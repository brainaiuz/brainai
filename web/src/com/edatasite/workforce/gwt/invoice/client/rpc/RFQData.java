package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class RFQData extends HasApprovers implements IsSerializable, ListingCustomFields {

    public static final String REQUEST_FROM = "requestfrom";
    public static final String REQUEST_NUMBER = "requestnumber";
    public static final String DATE = "date";
    public static final String VALID_UNTIL = "vlidUntil";
    public static final String STATUS = "status";
    public static final String APPROVER = "approver";
    public static final String OPPORTUNITY_NUMBER = "opportunityNumber";
    public static final String OPPORTUNITY_NAME = "opportunityName";
    public static final String PROJECT = "project";
    public static final String CUSTOMER_COUNTRY = "customerCountry";
    public static String CUSTOMER = "customer";
    private Integer objectID;
    private Integer requestFrom;
    private SelectItem project;
    private SelectItem customer;
    private SelectItem[] instructions;
    private DateNonConvertable date;
    private DateNonConvertable validUntil;
    private NumberData numberData;
    private String number;
    private String sqNumber;
    private Address addressData;
    private HistoryListItem[] historyList;
    private String introduction;
    private ArrayList<RFQItem> items;
    private String statusCode;
    private boolean isEditable;
    private boolean isNotConvertedSupplierBidExists;
    private boolean sendNotificationToSuppliers = true;
    private Integer opportunityID;
    private String opportunityName;
    private String oppportunityNumber;
    private boolean isSupplier;
    private Integer mailAddressId;
    private SelectItem[] templates;
    private Integer selectedTemplateId;
    private SelectItem approver;
    private boolean isApprover;
    private boolean isApproverSaved;
    private Integer currentUserId;
    private ArrayList<Integer> rfpIds;
    private FileItem[] attachments;
    private ArrayList<CompanyCustomFieldItem> customFieldList;
    private HashMap<String, Object> customFields;
    private ColumnConfigs[] customItemColumns;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private ArrayList<RelationItem> relations;
    private String clientAddress;
    private ListResult<Email> linkedEmails;
    private Integer dueDateType;
    private InvoiceTermsItem invoiceTermsItem;

    public RFQData() {
    }

    public static String getCUSTOMER() {
        return CUSTOMER;
    }

    public static void setCUSTOMER(String CUSTOMER) {
        RFQData.CUSTOMER = CUSTOMER;
    }

    public SelectItem getCustomer() {
        return customer;
    }

    public void setCustomer(SelectItem customer) {
        this.customer = customer;
    }

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

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(DateNonConvertable validUntil) {
        this.validUntil = validUntil;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSqNumber() {
        return sqNumber;
    }

    public void setSqNumber(String sqNumber) {
        this.sqNumber = sqNumber;
    }

    public Address getAddressData() {
        return addressData;
    }

    public void setAddressData(Address addressData) {
        this.addressData = addressData;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ArrayList<RFQItem> getItems() {
        if (items == null)
            items = new ArrayList<>();
        return items;
    }

    public void setItems(ArrayList<RFQItem> items) {
        this.items = items;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    public String getAddressAsString(boolean isHtml) {

        String txt = isHtml ? "<br/>" : "\n";
        if (addressData != null) {
            StringBuilder sql = new StringBuilder();
            if (addressData.getAddress() != null && !"".equals(addressData.getAddress().trim())) {
                sql.append(addressData.getAddress() + txt);
            }
            if (addressData.getCity() != null && !"".equals(addressData.getCity().trim())) {
                sql.append(addressData.getCity() + txt);
            }
            if (addressData.getCountry() != null && !"".equals(addressData.getCountry().trim())) {
                sql.append(addressData.getCountry() + txt);
            }
            if (addressData.getState() != null && !"".equals(addressData.getState().trim())) {
                sql.append(addressData.getState() + txt);
            }
            if (addressData.getZipCode() != null && !"".equals(addressData.getZipCode().trim())) {
                sql.append(addressData.getZipCode() + txt);
            }
            return sql.toString();
        }
        return "";
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public boolean isNotConvertedSupplierBidExists() {
        return isNotConvertedSupplierBidExists;
    }

    public void setNotConvertedSupplierBidExists(boolean notConvertedSupplierBidExists) {
        isNotConvertedSupplierBidExists = notConvertedSupplierBidExists;
    }

    public HistoryListItem[] getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListItem[] historyList) {
        this.historyList = historyList;
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

    public void setMailAddressId(Integer mailAddressId) {
        this.mailAddressId = mailAddressId;
    }

    public Integer getMailAddressId() {
        return mailAddressId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getOppportunityNumber() {
        return oppportunityNumber;
    }

    public void setOppportunityNumber(String oppportunityNumber) {
        this.oppportunityNumber = oppportunityNumber;
    }

    public ArrayList<Integer> getRfpIds() {
        if (rfpIds == null) rfpIds = new ArrayList<>();
        return rfpIds;
    }

    public void setRfpIds(ArrayList<Integer> rfpIds) {
        this.rfpIds = rfpIds;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public Integer getSelectedTemplateId() {
        return selectedTemplateId;
    }

    public void setSelectedTemplateId(Integer selectedTemplateId) {
        this.selectedTemplateId = selectedTemplateId;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean approver) {
        isApprover = approver;
    }

    public boolean isApproverSaved() {
        return isApproverSaved;
    }

    public void setApproverSaved(boolean approverSaved) {
        isApproverSaved = approverSaved;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldList() {
        return customFieldList;
    }

    public void setCustomFieldList(ArrayList<CompanyCustomFieldItem> customFieldList) {
        this.customFieldList = customFieldList;
    }

    public HashMap<String, Object> getCustomFields() {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        return customFields;
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFields() != null ? getCustomFields().get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFields().put(columnCodeKey, cellValue);
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return customItemColumns;
    }

    public void setCustomItemColumns(ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public ArrayList<RelationItem> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<RelationItem> relations) {
        this.relations = relations;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public ListResult<Email> getLinkedEmails() {
        return linkedEmails;
    }

    public void setLinkedEmails(ListResult<Email> linkedEmails) {
        this.linkedEmails = linkedEmails;
    }

    public SelectItem[] getInstructions() {
        return this.instructions;
    }

    public void setInstructions(final SelectItem[] instructions) {
        this.instructions = instructions;
    }

    public Integer getDueDateType() {
        return this.dueDateType;
    }

    public void setDueDateType(final Integer dueDateType) {
        this.dueDateType = dueDateType;
    }

    public InvoiceTermsItem getInvoiceTermsItem() {
        return this.invoiceTermsItem;
    }

    public void setInvoiceTermsItem(final InvoiceTermsItem invoiceTermsItem) {
        this.invoiceTermsItem = invoiceTermsItem;
    }
}
