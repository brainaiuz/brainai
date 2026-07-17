package com.edatasite.workforce.gwt.core.client.ui.crm;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBoxBase;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 11.09.2010
 * Time: 11:55:28
 */
public class CRMLookUp extends LookUp implements CrmConstants {
    protected Integer type;
    private Integer companyID;
    private Integer projectID;
    private Date date;
    private boolean fullSearch;
    private boolean showHeadOffice;
    private boolean excludeNumber;
    private Integer maxSearchKey;
    private String avoidType;
    private String formType;

    public CRMLookUp(Integer type) {
        super(type);
        this.type = type;
        getFilterParametrs().setCRM(true);
    }

    public CRMLookUp(Integer type, String avoidType) {
        super(type);
        this.type = type;
        getFilterParametrs().setCRM(true);
        this.avoidType = avoidType;
    }

    public CRMLookUp(String type, TextBoxBase txtBox) {
        super(0, txtBox);
        initType(type);
    }

    public CRMLookUp(boolean isCustomForm, String formType) {
        this.formType = formType;
    }

    public CRMLookUp(String type) {
        initType(type);
    }

    @UiConstructor
    public CRMLookUp(String type, boolean omitDiv) {
        super(omitDiv, omitDiv);
        initType(type);
    }

    public CRMLookUp(String type, String avoidType) {
        this.avoidType = avoidType;
        initType(type);
    }

    private void initType(String type) {
        switch (type) {
            case LookUpConstants.CRM_LEAD:
                this.type = LookUpConstants.CRM_LEAD_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.TIMESLOT:
                this.type = LookUpConstants.TIMESLOT_ID;
                getFilterParametrs().setCRM(false);
                getFilterParametrs().setHRMS(true);
                break;
            case LookUpConstants.CRM_ACCOUNT:
                this.type = LookUpConstants.CRM_ACCOUNT_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.CLIENT:
                this.type = LookUpConstants.CLIENT_ID;
                getFilterParametrs().setCRM(true);
                break;
            case CrmConstants.SUPPLIER:
                this.type = LookUpConstants.SUPPLIER_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.EMPLOYEE:
                this.type = LookUpConstants.PM_EMPLOYEE_ID;
                getFilterParametrs().setPM(true);
                break;
            case LookUpConstants.HRMS_EMPLOYEE:
                this.type = LookUpConstants.EMPLOYEE_ID;
                getFilterParametrs().setHRMS(true);
                break;
            case LookUpConstants.DEPARTMENT:
                this.type = LookUpConstants.PM_DEPARTMENT_ID;
                getFilterParametrs().setPM(true);
                break;
            case LookUpConstants.CRM_CONTACT:
                this.type = LookUpConstants.CRM_CONTACT_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.CANDIDATE:
                this.type = LookUpConstants.CANDIDATE_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.CRM_OPPORTUNITY:
                this.type = LookUpConstants.CRM_OPPORTUNITY_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.CRM_CASE:
                this.type = LookUpConstants.CRM_CASE_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.CAMPAIGN:
                this.type = LookUpConstants.CRM_CAMPAIGN_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.PROJECT:
                this.type = LookUpConstants.PM_PROJECT_ID;
                getFilterParametrs().setPM(true);
                break;
            case LookUpConstants.TASK:
                this.type = LookUpConstants.PM_TASK_ID;
                getFilterParametrs().setPM(true);
                break;
            case LookUpConstants.TASK_ASSIGNEE:
                this.type = LookUpConstants.PM_TASK_ASSIGNEE_ID;
                getFilterParametrs().setPM(true);
                break;
            case LookUpConstants.ISSUE:
                this.type = LookUpConstants.PM_ISSUE_ID;
                getFilterParametrs().setPM(true);
                break;
            case LookUpConstants.BOOKING:
                this.type = LookUpConstants.RESERVATION_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.CRM_EVENT:
                this.type = LookUpConstants.CRM_EVENT_ID;
                getFilterParametrs().setCRM(true);
                break;
            case LookUpConstants.SALEQUOTE:
                this.type = LookUpConstants.SALE_QUOTE;
                break;
            case LookUpConstants.SALEORDER:
                this.type = LookUpConstants.SALE_ORDER;
                break;
            case LookUpConstants.PRODUCT:
                this.type = LookUpConstants._PRODUCT;
                break;
            case LookUpConstants.COURCE_SCHEDULE:
                this.type = LookUpConstants.COURCE_SCHEDULE_ID;
                getFilterParametrs().setTrainingCenter(true);
                break;
            case LookUpConstants.STUDENT:
                this.type = LookUpConstants.STUDENT_ID;
                getFilterParametrs().setTrainingCenter(true);
                break;
            case LookUpConstants.COURSE_PASSED_STUDENT:
                this.type = LookUpConstants.COURSE_PASSED_STUDENT_ID;
                getFilterParametrs().setTrainingCenter(true);
                break;
            case LookUpConstants.TYPE_PURCHASE_ORDER:
                this.type = LookUpConstants.PURCHASE_ORDER;
                break;
            case LookUpConstants.TYPE_PURCHASE_INVOICE:
                this.type = LookUpConstants.PURCHASE_INVOICE;
                break;
            case LookUpConstants.POSITION:
                this.type = LookUpConstants.POSITION_ID;
                getFilterParametrs().setHRMS(true);
                break;
            case "LOCATION":
                this.type = LookUpConstants.LOCATION_ID;
                getFilterParametrs().setHRMS(true);
                break;
            case LookUpConstants.LEAD_RESOURCE:
                this.type = LookUpConstants.LEAD_SOURCE_ID;
                getFilterParametrs().setCRM(true);
                break;
        }
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        switch (type) {
            case LookUpConstants.CRM_CAMPAIGN_ID:
                addListener(this, WfmUiEventType.ON_CAMPAIGN_ADD_EDIT, WfmUiEventType.ON_CAMPAIGN_DELETED);
                onAddCampaign();
                break;
            case LookUpConstants.CRM_ACCOUNT_ID:
                addListener(this, WfmUiEventType.ON_ACCOUNT_DELETED, WfmUiEventType.ON_ACCOUNTS_ADD_EDIT);
                break;
            case LookUpConstants.CLIENT_ID:
                addListener(this, WfmUiEventType.ON_CLIENT_DELETED, WfmUiEventType.ON_CLIENT_ADD, WfmUiEventType.ON_CLIENT_EDIT);
                break;
            case LookUpConstants.SUPPLIER_ID:
                addListener(this, WfmUiEventType.ON_SUPPLIER_DELETED, WfmUiEventType.ON_SUPPLIER_ADD, WfmUiEventType.ON_SUPPLIER_EDIT);
                break;
            case LookUpConstants.PM_EMPLOYEE_ID:
                addListener(this, WfmUiEventType.ON_EMPLOYEE_DELETE, WfmUiEventType.ON_EMPLOYEE_ADD);
                break;
            case LookUpConstants.PM_DEPARTMENT_ID:
                addListener(this, WfmUiEventType.ON_DEPARTMENT_DELETE, WfmUiEventType.ON_DEPARTMENT_ADD, WfmUiEventType.ON_DEPARTMENT_EDIT);
                break;
            case LookUpConstants.CRM_CASE_ID:
                addListener(this, WfmUiEventType.ON_CASE_ADD, WfmUiEventType.ON_CASE_DELETE);
                break;
            case LookUpConstants.CRM_OPPORTUNITY_ID:
                addListener(this, WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, WfmUiEventType.ON_OPPORTUNITY_DELETED);
                break;
            case LookUpConstants.CRM_CONTACT_ID:
                addListener(this, WfmUiEventType.ON_CONTACT_ADD | WfmUiEventType.ON_CONTACT_DELETE);
                break;
            case LookUpConstants.CANDIDATE_ID:
                addListener(this, WfmUiEventType.ON_CANDIDATE_ADD_EDIT | WfmUiEventType.ON_CANDIDATE_DELETE);
                break;
            case LookUpConstants.CRM_LEAD_ID:
                addListener(this, WfmUiEventType.ON_LEADS_ADD_EDIT | WfmUiEventType.ON_LEADS_DELETE);
                break;
            case LookUpConstants.PM_PROJECT_ID:
                addListener(this, WfmUiEventType.ON_PROJECT_ADD | WfmUiEventType.ON_PROJECT_EDIT | WfmUiEventType.ON_PROJECT_DELETE);
                break;
            case LookUpConstants.PM_TASK_ID:
                addListener(this, WfmUiEventType.ON_TASK_ADD | WfmUiEventType.ON_TASK_EDIT | WfmUiEventType.ON_TASK_DELETE);
                break;
            case LookUpConstants.PM_ISSUE_ID:
                addListener(this, WfmUiEventType.ON_ISSUE_ADD | WfmUiEventType.ON_ISSUE_DELETE);
                break;
        }
    }

    private void onAddCampaign() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CAMPAIGN_ADD_EDIT, this, (sender, args) -> {
            if (args != null && args instanceof CampaignItem) {
                CampaignItem campaignItem = (CampaignItem) args;
                addItem(new SelectItem(((CampaignItem) args).getObjectId(), campaignItem.getName()));
            }
        });
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        if (maxSearchKey != null && filterParametrs.getSearchKey().length() >= maxSearchKey) {
            return;
        }

        filterParametrs.setCompanyID(companyID);
        if (!filterParametrs.isNewType()) {
            filterParametrs.setProjectId(projectID);
            filterParametrs.setStartDate(date);
        }
        filterParametrs.setShowHeadOffice(showHeadOffice);
        filterParametrs.setExcludeNumber(excludeNumber);
        filterParametrs.setAvoidType(avoidType);
        filterParametrs.setFormType(formType);

        AllInOneService.App.get().getLookUpItems(filterParametrs, type,null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                CRMLookUp.super.getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                CRMLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public void setLinkCommand(Command linkCommand) {
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(true);
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFullSearch(boolean fullSearch) {
        this.fullSearch = fullSearch;
    }

    public void setShowHeadOffice(boolean showHeadOffice) {
        this.showHeadOffice = showHeadOffice;
    }

    public void setExcludeNumber(boolean excludeNumber) {
        this.excludeNumber = excludeNumber;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public ListingFilterParameter getFilterParametrs() {
        return super.getFilterParametrs();
    }

    public void setOpenIconVisibility(boolean visible) {
        getOpenIcon().setVisible(visible);
    }

    public void setMaxSearchKey(Integer maxSearchKey) {
        this.maxSearchKey = maxSearchKey;
    }

    public String getAvoidType() {
        return avoidType;
    }

    public void setAvoidType(String avoidType) {
        this.avoidType = avoidType;
    }
}
