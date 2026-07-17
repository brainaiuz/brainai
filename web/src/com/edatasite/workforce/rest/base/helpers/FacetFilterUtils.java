package com.edatasite.workforce.rest.base.helpers;

import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;

import java.util.HashMap;

/**
 * Created by Normurod Buriev.
 * Date: 11/13/2020 9:11 PM
 */

public class FacetFilterUtils {
    /**
     * Task panel
     */

    static HashMap<String, FacetSolrField> getTaskSolrFields() {
        final String[] contentCodes = FacetContentType.TaskFacetFilter.getContentCode();

        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID, SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID_NAME));
            this.put(contentCodes[18], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID, SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[3], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID, SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[19], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[4], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID, SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[5], new FacetSolrField(SolrTaskRepresenter.FIELD_USER_ID, SolrTaskRepresenter.FIELD_USER_ID_NAME));
            this.put(contentCodes[6], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT));
            this.put(contentCodes[7], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT));
            this.put(contentCodes[8], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD));
            this.put(contentCodes[9], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE));
            this.put(contentCodes[10], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY));
            this.put(contentCodes[11], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_PROJECT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_PROJECT));
            this.put(contentCodes[12], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT));
            this.put(contentCodes[13], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE));
            this.put(contentCodes[14], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE));
            this.put(contentCodes[15], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT));
            this.put(contentCodes[16], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT));
            this.put(contentCodes[17], new FacetSolrField(SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER, SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER));
            this.put(contentCodes[4], new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_TYPE_ID, SolrTaskRepresenter.FIELD_TASK_TYPE_ID_CODE_NAME, LocalizationType.REFERENCE));
        }};
    }

    /**
     * Project panel
     */

    static HashMap<String, FacetSolrField> getProjectSolrFields() {
        final String[] contentCodes = FacetContentType.ProjectFacetFilter.getContentCode();

        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID, SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID, SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID, SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[3], new FacetSolrField(SolrProjectListRepresenter.FIELD_USER_ID, SolrProjectListRepresenter.FIELD_USER_ID_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_ID, SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_ID_NAME));
            this.put(contentCodes[17], new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_ID, SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_ID_NAME));
            this.put(contentCodes[5], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT));
            this.put(contentCodes[6], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT));
            this.put(contentCodes[7], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD));
            this.put(contentCodes[8], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE));
            this.put(contentCodes[9], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY));
            this.put(contentCodes[10], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT));
            this.put(contentCodes[11], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_TASK, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_TASK));
            this.put(contentCodes[12], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE));
            this.put(contentCodes[13], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE));
            this.put(contentCodes[14], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT));
            this.put(contentCodes[15], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT));
            this.put(contentCodes[16], new FacetSolrField(SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER));
        }};
    }

    /**
     * Cases panel
     */
    static HashMap<String, FacetSolrField> getCaseSolrFields() {
        final String[] contentCodes = FacetContentType.CaseFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrCaseRepresenter.REPORTED_BY, SolrCaseRepresenter.REPORTED_BY));
            this.put(contentCodes[1], new FacetSolrField(SolrCaseRepresenter.CASE_TYPE_ID, SolrCaseRepresenter.CASE_TYPE_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[2], new FacetSolrField(SolrCaseRepresenter.PRIORITY_ID, SolrCaseRepresenter.PRIORITY_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[3], new FacetSolrField(SolrCaseRepresenter.STATUS_ID, SolrCaseRepresenter.STATUS_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[4], new FacetSolrField(SolrCaseRepresenter.CASE_ORIGIN_ID, SolrCaseRepresenter.CASE_ORIGIN_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[5], new FacetSolrField(SolrCaseRepresenter.CASE_ASSIGNEE_ID, SolrCaseRepresenter.CASE_ASSIGNEE_ID_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[6], new FacetSolrField(SolrCaseRepresenter.CASE_DEPARTMENT_ID, SolrCaseRepresenter.CASE_DEPARTMENT_ID_NAME));
            this.put(contentCodes[7], new FacetSolrField(SolrCaseRepresenter.RESOLVER_ID, SolrCaseRepresenter.RESOLVER_ID_NAME));
            this.put(contentCodes[8], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT));
            this.put(contentCodes[9], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT));
            this.put(contentCodes[10], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD));
            this.put(contentCodes[11], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY));
            this.put(contentCodes[12], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_TASK, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_TASK));
            this.put(contentCodes[13], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_PROJECT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_PROJECT));
            this.put(contentCodes[14], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT));
            this.put(contentCodes[15], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE));
            this.put(contentCodes[16], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT));
            this.put(contentCodes[17], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT));
            this.put(contentCodes[18], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER));
            this.put(contentCodes[19], new FacetSolrField(SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SALEQUOTE, SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SALEQUOTE));

        }};
    }

    static HashMap<String, FacetSolrField> getContactSolrFields() {
        final String[] contentCodes = FacetContentType.ContactFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrContactRepresenter.FIELD_COUNTRY_ID, SolrContactRepresenter.FIELD_COUNTRY_ID_CODE_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID, SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrContactRepresenter.FIELD_DEPARTMENT, SolrContactRepresenter.FIELD_DEPARTMENT, LocalizationType.COUNTRY, false));
            this.put(contentCodes[3], new FacetSolrField(SolrContactRepresenter.FIELD_JOB_TITLE, SolrContactRepresenter.FIELD_JOB_TITLE, LocalizationType.COUNTRY, false));
            this.put(contentCodes[4], new FacetSolrField(SolrContactRepresenter.FIELD_OWNER_ID, SolrContactRepresenter.FIELD_OWNER_ID_NAME));
            this.put(contentCodes[5], new FacetSolrField(SolrContactRepresenter.FIELD_CATEGORY_ID, SolrContactRepresenter.FIELD_CATEGORY_ID_NAME));
            this.put(contentCodes[6], new FacetSolrField(SolrContactRepresenter.FIELD_CAMPAIGN_ID, SolrContactRepresenter.FIELD_CAMPAIGN_ID_NAME));
            this.put(contentCodes[7], new FacetSolrField(SolrContactRepresenter.FIELD_STATE_ID, SolrContactRepresenter.FIELD_STATE_ID_NAME));
            this.put(contentCodes[8], new FacetSolrField(SolrContactRepresenter.FIELD_MAIL_LIST_ID, SolrContactRepresenter.FIELD_MAIL_LIST_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getLeadSolrFields() {
        final String[] contentCodes = FacetContentType.LeadFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrContactRepresenter.FIELD_CAMPAIGN_ID, SolrContactRepresenter.FIELD_CAMPAIGN_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID, SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID, SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrContactRepresenter.FIELD_COUNTRY_ID, SolrContactRepresenter.FIELD_COUNTRY_ID_CODE_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrContactRepresenter.FIELD_JOB_TITLE, SolrContactRepresenter.FIELD_JOB_TITLE, false));
            this.put(contentCodes[5], new FacetSolrField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME));
            this.put(contentCodes[6], new FacetSolrField(SolrContactRepresenter.FIELD_OWNER_ID, SolrContactRepresenter.FIELD_OWNER_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getCrmAccountSolrFields() {
        final String[] contentCodes = FacetContentType.CrmAccountFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_TYPE_ID, SolrCrmAccountRepresenter.FIELD_TYPE_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[2], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_OWNER_ID, SolrCrmAccountRepresenter.FIELD_OWNER_ID_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_OWNERSHIP_ID, SolrCrmAccountRepresenter.FIELD_OWNERSHIP_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[4], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID, SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[5], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_ID, SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[6], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_ID, SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_ID_CODE_NAME, LocalizationType.REFERENCE));
            this.put(contentCodes[7], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID, SolrCrmAccountRepresenter.FIELD_COUNTRY_ID_CODE_NAME, LocalizationType.COUNTRY));
            this.put(contentCodes[8], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_BLOCKED, SolrCrmAccountRepresenter.FIELD_BLOCKED, LocalizationType.COUNTRY, false));
            this.put(contentCodes[9], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_TAX_ID, SolrCrmAccountRepresenter.FIELD_TAX_ID));
        }};
    }


    static HashMap<String, FacetSolrField> getAdditionalPaymentSolrFields() {
        final String[] contentCodes = FacetContentType.AdditionalPaymentFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrAdditionalPaymentPresenter.FIELD_MONTH_ID, SolrAdditionalPaymentPresenter.FIELD_MONTH_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrAdditionalPaymentPresenter.FIELD_YEAR, SolrAdditionalPaymentPresenter.FIELD_YEAR_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID, SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID, SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT, SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT));
            this.put(contentCodes[5], new FacetSolrField(SolrAdditionalPaymentPresenter.FIELD_STATUS_ID, SolrAdditionalPaymentPresenter.FIELD_STATUS_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getSaleQuoteSolrFields() {
        final String[] contentCodes = FacetContentType.SaleQuoteFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT));
            this.put(contentCodes[3], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_PROJECT_ID, SolrSaleInvoiceRepresenter.FIELD_PROJECT_ID));
            this.put(contentCodes[4], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME));
            this.put(contentCodes[5], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID, SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID_NAME));
        }};
    }
    static HashMap<String, FacetSolrField> getScheduleSolrFields() {
        final String[] contentCodes = FacetContentType.CourseScheduleFaceFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrCourseScheduleRepresenter.FIELD_COURSE_ID, SolrCourseScheduleRepresenter.FIELD_COURSE_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID, SolrCourseScheduleRepresenter.FIELD_LOCATION_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID, SolrCourseScheduleRepresenter.FIELD_LOCATION_ID_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrCourseScheduleRepresenter.FIELD_STATUS_ID, SolrCourseScheduleRepresenter.FIELD_STATUS_ID_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID, SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getExpenseReportSolrFields() {
        final String[] contentCodes = FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID, SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_REPORTER_ID, SolrExpenseReportRepresenter.FIELD_REPORTER_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_APPROVER_ID, SolrExpenseReportRepresenter.FIELD_APPROVER_ID_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_APPROVER2_ID, SolrExpenseReportRepresenter.FIELD_APPROVER2_ID_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_STATUS_ID, SolrExpenseReportRepresenter.FIELD_STATUS_ID_NAME));
            this.put(contentCodes[5], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_STATUS2_ID, SolrExpenseReportRepresenter.FIELD_STATUS2_ID_NAME));
            this.put(contentCodes[6], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT, SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT));
            this.put(contentCodes[7], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_SUPPLIER_ID, SolrExpenseReportRepresenter.FIELD_SUPPLIER_ID_NAME));
            this.put(contentCodes[8], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_CURRENCY_ID, SolrExpenseReportRepresenter.FIELD_CURRENCY_ID_NAME));
            this.put(contentCodes[9], new FacetSolrField(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE, SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE));
        }};
    }

    static HashMap<String, FacetSolrField> getOpportunitySolrFields() {
        //OpportunityFacetFilter("opportunitystage", "assignee", "account", "country", "amount", "campaign", "relatedContact", "relatedCrmAccount", "relatedLead", "relatedCase", "relatedTask", "relatedProject", "relatedEvent", "relatedIssue", "relatedEmployee", "relatedDepartment", "relatedClient", "relatedSupplier", "creator", "backupAssignee", "leadSource"),
        final String[] contentCodes = FacetContentType.OpportunityFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID_CODE));
            this.put(contentCodes[1], new FacetSolrField(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID, SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_ID, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_ID_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrOpportunityRepresenter.FIELD_AMOUNT, SolrOpportunityRepresenter.FIELD_AMOUNT, LocalizationType.COUNTRY, false));
            this.put(contentCodes[5], new FacetSolrField(SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID, SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID_NAME));
            this.put(contentCodes[6], new FacetSolrField(SolrOpportunityRepresenter.FIELD_CREATOR_ID, SolrOpportunityRepresenter.FIELD_CREATOR_ID_NAME));
            this.put(contentCodes[7], new FacetSolrField(SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID, SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID_NAME));
            this.put(contentCodes[8], new FacetSolrField(SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_ID, SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_ID_NAME));
            this.put(contentCodes[9], new FacetSolrField(SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_ID, SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_ID_NAME));
            //this.put(contentCodes[21], new FacetSolrField(SolrOpportunityRepresenter.FIELD_TYPE_ID, SolrOpportunityRepresenter.FIELD_TYPE_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getClientSolrFields() {
        final String[] contentCodes = FacetContentType.ClientFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            this.put(contentCodes[0], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID_NAME));
            this.put(contentCodes[1], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_OWNER_ID, SolrCrmAccountRepresenter.FIELD_OWNER_ID_NAME));
            this.put(contentCodes[2], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID, SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID_CODE_NAME));
            this.put(contentCodes[3], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID, SolrCrmAccountRepresenter.FIELD_COUNTRY_ID_CODE_NAME));
            this.put(contentCodes[4], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_STATE_ID, SolrCrmAccountRepresenter.FIELD_STATE_ID_NAME));
            this.put(contentCodes[5], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_CITY, SolrCrmAccountRepresenter.FIELD_CITY));
            this.put(contentCodes[6], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_IN_TARGET, SolrCrmAccountRepresenter.FIELD_IN_TARGET));
            this.put(contentCodes[10], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_TAX_ID, SolrCrmAccountRepresenter.FIELD_TAX_ID_NAME));
            this.put(contentCodes[11], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_BLOCKED, SolrCrmAccountRepresenter.FIELD_BLOCKED));
            this.put(contentCodes[12], new FacetSolrField(SolrCrmAccountRepresenter.FIELD_SALES_TYPE, SolrCrmAccountRepresenter.FIELD_SALES_TYPE));
        }};
    }

    static HashMap<String, FacetSolrField> getSaleInvoiceFields() {
        final String[] contentCodes = FacetContentType.SaleInvoiceFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            put(contentCodes[0], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME));
            put(contentCodes[1], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME));
            put(contentCodes[2], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT));
            put(contentCodes[4], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME));
            put(contentCodes[5], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID, SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getPurchaseInvoiceFields() {
        final String[] contentCodes = FacetContentType.PurchaseInvoiceFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            put(contentCodes[0], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME));
            put(contentCodes[1], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_ID, SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_ID_NAME));
            put(contentCodes[2], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_ID, SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_ID_NAME));
            put(contentCodes[3], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_STATUS_ID, SolrPurchaseInvoiceRepresenter.FIELD_STATUS_ID_NAME));
            put(contentCodes[4], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT, SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT));
            put(contentCodes[5], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT, SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT));
            put(contentCodes[6], new FacetSolrField(SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE, SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE));
            put(FacetContentType.ClientFacetFilter.getContentCode()[1], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getPurchaseOrderFields() {
        final String[] contentCodes = FacetContentType.PurchaseOrderFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            put(contentCodes[0], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME));
            put(contentCodes[1], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME));
            put(contentCodes[2], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT));
            put(contentCodes[3], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME));
            put(contentCodes[4], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME));
            put(contentCodes[6], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME));
            put(contentCodes[7], new FacetSolrField(SolrSaleInvoiceRepresenter.FIELD_MANAGER_ID, SolrSaleInvoiceRepresenter.FIELD_MANAGER_ID_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getProductServiceFields() {
        final String[] contentCodes = FacetContentType.ProductsServicesFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            put(contentCodes[0], new FacetSolrField(SolrProductServiceRepresenter.FIELD_ACCOUNT_ID, SolrProductServiceRepresenter.FIELD_ACCOUNT_ID_NAME));
            put(contentCodes[1], new FacetSolrField(SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID, SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID_NAME));
            put(contentCodes[3], new FacetSolrField(SolrProductServiceRepresenter.FIELD_CATEGORY_ID, SolrProductServiceRepresenter.FIELD_CATEGORY));
            put(contentCodes[4], new FacetSolrField(SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID, SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID_NAME));
            put(contentCodes[5], new FacetSolrField(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID, SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID_NAME));
            put(contentCodes[6], new FacetSolrField(SolrProductServiceRepresenter.FIELD_BRAND_ID, SolrProductServiceRepresenter.FIELD_BRAND_ID_NAME));
            put(contentCodes[8], new FacetSolrField(SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE, SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE));
            put(contentCodes[9], new FacetSolrField(SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_ID, SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_ID_NAME));
            put(contentCodes[10], new FacetSolrField(SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_ID, SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_ID_NAME));
            put(contentCodes[11], new FacetSolrField(SolrProductServiceRepresenter.FIELD_WAREHOUSE_ID, SolrProductServiceRepresenter.FIELD_WAREHOUSE_NAME));
        }};
    }

    static HashMap<String, FacetSolrField> getCustomFormFields() {
        final String[] contentCodes = FacetContentType.CustomFormItemFacetFilter.getContentCode();
        return new HashMap<String, FacetSolrField>() {{
            put(contentCodes[0], new FacetSolrField(SolrCustomFormConst.FIELD_CREATOR_ID, SolrCustomFormConst.FIELD_CREATOR_ID_NAME));
            put(contentCodes[1], new FacetSolrField(SolrCustomFormConst.FIELD_UPDATER_ID, SolrCustomFormConst.FIELD_UPDATER_ID_NAME));
            put(contentCodes[2], new FacetSolrField(SolrCustomFormConst.FIELD_STATUS_ID, SolrCustomFormConst.FIELD_STATUS_ID_NAME));
        }};
    }

    public static HashMap<String, FacetSolrField> getLeaveRequestFields() {
        final String[] contentCodes = FacetContentType.LeaveFacetFilter.getContentCode();

        return new HashMap<String, FacetSolrField>() {{
            put(contentCodes[0], new FacetSolrField(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID, SolrLeaveRequestConst.FIELD_EMPLOYEE_ID));
            put(contentCodes[1], new FacetSolrField(SolrLeaveRequestConst.FIELD_REASON_ID, SolrLeaveRequestConst.FIELD_REASON_NAME));
            put(contentCodes[2], new FacetSolrField(SolrLeaveRequestConst.FIELD_STATUS_ID, SolrLeaveRequestConst.FIELD_STATUS_NAME));
            put(contentCodes[3], new FacetSolrField(SolrLeaveRequestConst.FIELD_APPROVER_ID, SolrLeaveRequestConst.FIELD_APPROVER_NAME));
            put(contentCodes[4], new FacetSolrField(SolrLeaveRequestConst.FIELD_DEPARTMENT_ID, SolrLeaveRequestConst.FIELD_DEPARTMENT_NAME));
            put(contentCodes[5], new FacetSolrField(SolrLeaveRequestConst.FIELD_POSITION_ID, SolrLeaveRequestConst.FIELD_POSITION_NAME));
        }};
    }
}
