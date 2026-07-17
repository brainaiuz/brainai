package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class RFPData implements Serializable, ListingCustomFields {

    public static final String NUMBER = "number";
    public static final String DUE_DATE = "dueDate";
    public static final String CREATOR = "creator";
    public static final String MANAGER = "manager";
    public static final String RELATED_PROJECT = "relatedProject";
    public static final String LOCATION = "location";
    public static final String STATUS = "status";
    public static final String CREATED_DATE = "created_date";
    public static final String CUSTOMER = "customer";

    private Integer objectID;
    private NumberData numberData;
    private String number;
    private Date dueDate;
    private String status;
    private SelectItem creator;
    //    private SelectItem manager;
    ArrayList<ApproverItemMini> approvers = new ArrayList<>();
    private boolean selfApprover;
    private boolean isApproverSaved;
    private Integer projectID;
    private SelectItem relatedProject;
    private SelectItem customer;
    private Boolean isCurrentApprover;
    private Boolean isEmployee;
    private SelectItem[] templates;
    private Integer selectedTemplateId;
    private ArrayList<RFPItem> items = new ArrayList<>();
    private FileItem[] attachments;
    private HistoryListItem[] historyList;
    private boolean copy;
    private boolean copyFromQuote;
    private boolean fromProject;
    private boolean fromBillOfMaterials;
    private boolean view;
    private SelectItem currentApprover;
    private ArrayList<SelectItem> managers;
    private String rejectionReason;
    private Date createdDate;
    private ArrayList<CompanyCustomFieldItem> customFieldList;
    private HashMap<String, Object> customFields;
    private ColumnConfigs[] customItemColumns;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private ArrayList<RelationItem> relations;

    public RFPData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public ArrayList<ApproverItemMini> getApprovers() {
        if (approvers == null) {
            approvers = new ArrayList<>();
        }
        return approvers;
    }

    public void setApprovers(ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public boolean isSelfApprover() {
        return selfApprover;
    }

    public void setSelfApprover(boolean selfApprover) {
        this.selfApprover = selfApprover;
    }

    public ArrayList<RFPItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<RFPItem> items) {
        this.items = items;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public HistoryListItem[] getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListItem[] historyList) {
        this.historyList = historyList;
    }

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public SelectItem getCustomer() {
        return customer;
    }

    public void setCustomer(SelectItem customer) {
        this.customer = customer;
    }

    public boolean isCurrentApprover() {
        return isCurrentApprover != null ? isCurrentApprover : false;
    }

    public void setIsCurrentApprover(Boolean manager) {
        this.isCurrentApprover = manager;
    }

    public boolean isEmployee() {
        return isEmployee != null ? isEmployee : false;
    }

    public void setIsEmployee(Boolean isEmployee) {
        this.isEmployee = isEmployee;
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

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isCopyFromQuote() {
        return copyFromQuote;
    }

    public void setCopyFromQuote(boolean copyFromQuote) {
        this.copyFromQuote = copyFromQuote;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public boolean isFromBillOfMaterials() {
        return fromBillOfMaterials;
    }

    public void setFromBillOfMaterials(boolean fromBillOfMaterials) {
        this.fromBillOfMaterials = fromBillOfMaterials;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setManagers(ArrayList<SelectItem> managers) {
        this.managers = managers;
    }

    public ArrayList<SelectItem> getManagers() {
        return managers;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldList() {
        return customFieldList;
    }

    public void setCustomFieldList(ArrayList<CompanyCustomFieldItem> customFieldList) {
        this.customFieldList = customFieldList;
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

    public HashMap<String, Object> getCustomFields() {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        return customFields;
    }

    public void setCustomFields(final HashMap<String, Object> customFields) {
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

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public boolean isApproverSaved() {
        return this.isApproverSaved;
    }

    public void setApproverSaved(final boolean approverSaved) {
        this.isApproverSaved = approverSaved;
    }

    public boolean isFromProject() {
        return this.fromProject;
    }

    public void setFromProject(final boolean fromProject) {
        this.fromProject = fromProject;
    }

    public ArrayList<RelationItem> getRelations() {
        return this.relations;
    }

    public void setRelations(final ArrayList<RelationItem> relations) {
        this.relations = relations;
    }

    public boolean isView() {
        return this.view;
    }

    public void setView(final boolean view) {
        this.view = view;
    }
}
