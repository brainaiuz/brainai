package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GroupPlacementItem extends HasApprovers implements IsSerializable, ListingCustomFields {
    public static final String APPROVER = "approver";
    public static String NUMBER = "placement_code";
    public static String STATUS = "statusCode";
    public static String DATE = "date";
    public static String CREATOR = "creator";
    public static String UPDATER = "updater";
    public static String CREATED_DATE = "createdDate";
    public static String UPDATED_DATE = "updatedDate";

    private Integer id;
    private Integer statusId;
    private String placementCode;
    private Date date;


    private NumberData numberData;
    private String statusCode;
    private Boolean isApprover;
    private DateNonConvertable approvedDate;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem approverEmployee;
    private ColumnConfigs[] columnConfigs;
    private GroupPlacementTableItem[] groupPlacementTableItems;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private SelectItem[] templates;
    private SelectItem department;
    private SelectItem position;

    public static String getNUMBER() {
        return NUMBER;
    }

    public static void setNUMBER(String NUMBER) {
        GroupPlacementItem.NUMBER = NUMBER;
    }

    public static String getSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(String STATUS) {
        GroupPlacementItem.STATUS = STATUS;
    }


    public static String getDATE() {
        return DATE;
    }

    public static void setDATE(String DATE) {
        GroupPlacementItem.DATE = DATE;
    }

    public static String getCREATOR() {
        return CREATOR;
    }

    public static void setCREATOR(String CREATOR) {
        GroupPlacementItem.CREATOR = CREATOR;
    }

    public static String getUPDATER() {
        return UPDATER;
    }

    public static void setUPDATER(String UPDATER) {
        GroupPlacementItem.UPDATER = UPDATER;
    }

//    public static String getCreatedDate() {
//        return CREATED_DATE;
//    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public DateNonConvertable getCreatedDate() {
        return this.createdDate;
    }

    public static void setCreatedDate(String createdDate) {
        CREATED_DATE = createdDate;
    }

    public DateNonConvertable getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(DateNonConvertable updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public SelectItem getApproverEmployee() {
        return approverEmployee;
    }

    public void setApproverEmployee(SelectItem approverEmployee) {
        this.approverEmployee = approverEmployee;
    }

    public ColumnConfigs[] getColumnConfigs() {
        return columnConfigs;
    }

    public void setColumnConfigs(ColumnConfigs[] columnConfigs) {
        this.columnConfigs = columnConfigs;
    }

    public GroupPlacementTableItem[] getPlacementTableItems() {
        return groupPlacementTableItems;
    }

    public void setPlacementTableItems(GroupPlacementTableItem[] rotationTableItems) {
        this.groupPlacementTableItems = rotationTableItems;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public static void setUpdatedDate(String updatedDate) {
        UPDATED_DATE = updatedDate;
    }


    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean isApprover() {
        return isApprover;
    }

    public Boolean getApprover() {
        return isApprover;
    }

    public void setApprover(Boolean approver) {
        isApprover = approver;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getPlacementCode() {
        return placementCode;
    }

    public void setPlacementCode(String placementCode) {
        this.placementCode = placementCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GroupPlacementTableItem[] getGroupPlacementTableItems() {
        return groupPlacementTableItems;
    }

    public void setGroupPlacementTableItems(GroupPlacementTableItem[] groupPlacementTableItems) {
        this.groupPlacementTableItems = groupPlacementTableItems;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {

    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }
}
