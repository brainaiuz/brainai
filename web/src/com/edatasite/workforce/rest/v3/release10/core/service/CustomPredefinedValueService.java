package com.edatasite.workforce.rest.v3.release10.core.service;

import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.NO_TAX_CALCULATION;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TAX_CALCULATION_EXCLUSIVE;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TAX_CALCULATION_INCLUSIVE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants._COMPANY_WORKAREA;

@Service
public class CustomPredefinedValueService {

    @Autowired
    @Qualifier("referenceWfmMessageSource")
    protected WfmMessageSource referenceWfmMessageSource;
    private final ProjectService projectService;
    private final ReportService reportService;
    private final TaskService taskService;
    private final CommonService commonService;
    private final AllInOneService allInOneService;
    private final WfmMessageSource commonLocalizer;
    private final ReferenceManager referenceManager;
    private final CurrencyService currencyService;
    private final CRMService crmService;
    private final AccountingService accountingService;

    public CustomPredefinedValueService(ProjectService projectService, ReportService reportService, TaskService taskService, CommonService commonService, AllInOneService allInOneService, WfmMessageSource commonLocalizer, ReferenceManager referenceManager, CurrencyService currencyService, CRMService crmService, AccountingService accountingService, WfmMessageSource referenceWfmMessageSource) {
        this.projectService = projectService;
        this.reportService = reportService;
        this.taskService = taskService;
        this.commonService = commonService;
        this.allInOneService = allInOneService;
        this.commonLocalizer = commonLocalizer;
        this.referenceManager = referenceManager;
        this.currencyService = currencyService;
        this.crmService = crmService;
        this.accountingService = accountingService;
        this.referenceWfmMessageSource = referenceWfmMessageSource;
    }

    public SelectItem[] predefinedValue(String fieldId, String formId) {
        if (LayoutRPC.PROJECT_FORM.equals(formId)) {
            return projectPredefinedValues(fieldId);
        }
        if (LayoutRPC.TASK_MAX_FORM.equals(formId)) {
            return taskPredefinedValues(fieldId);
        }
        if (LayoutRPC.LEAVE_REQUEST_FORM.equals(formId)) {
            return leavePredefinedValues(fieldId);
        }
        if (LayoutRPC.OPPORTUNITY_FORM.equals(formId)) {
            return opportunityPredefinedValues(fieldId);
        }
        if (LayoutRPC.LEAD_FORM.equals(formId)) {
            return leadPredefinedValues(fieldId);
        }
        if (LayoutRPC.CONTACT_FORM.equals(formId)) {
            return contactPredefinedValues(fieldId);
        }
        if (LayoutRPC.PRODUCT.equals(formId)) {
            return productPredefinedValues(fieldId);
        }
        return new SelectItem[0];
    }

    private SelectItem[] projectPredefinedValues(String fieldId) {
        return switch (fieldId) {
            case CustomFormConstants.STATUS -> projectService.getProjectStatuses();
            case CustomFormConstants.PROJECT.LOCATION -> reportService.getLocationList();
            case CustomFormConstants.PROJECT.BACKUP_MANAGER, CustomFormConstants.PROJECT.MANAGER ->
                    projectService.getManagers().toArray(new SelectItem[0]);
            default -> new SelectItem[0];
        };
    }

    private SelectItem[] taskPredefinedValues(String fieldId) {
        if (CustomFormConstants.PRIORITY.equals(fieldId)) {
            return taskService.getPriorities();
        }
        if (CustomFormConstants.STATUS.equals(fieldId)) {
            return commonService.getAddTaskStatusDrop();
        }
        return new SelectItem[0];
    }

    private SelectItem[] leavePredefinedValues(String fieldId) {
        if (CustomFormConstants.REASON.equals(fieldId)) {
            return allInOneService.getReasons(referenceManager.getUser().getObjectID());
        }
        if (CustomFormConstants.TAKE_LIVE_TYPE.equals(fieldId)) {
            return Stream.of(new SelectItem(1, "MONEY", commonLocalizer.localize("money")), new SelectItem(2, "DAY", commonLocalizer.localize("day")))
                    .toArray(SelectItem[]::new);
        }
        if (CustomFormConstants.TYPE.equals(fieldId)) {
            return Stream.of(new SelectItem(1, "ST_PAID", commonLocalizer.localize("paid")), new SelectItem(2, "NON_PAID", referenceWfmMessageSource.localize("NON_PAID")))
                    .toArray(SelectItem[]::new);
        }
        return new SelectItem[0];
    }

    private SelectItem[] opportunityPredefinedValues(String fieldId) {
        if (CustomFormConstants.TAX_CALC_TYPE.equals(fieldId)) {
            return getTaxCalcTypes();
        }
        if (CustomFormConstants.CRM_OPPORTUNITY_STAGE.equals(fieldId)) {
            return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE), ServerUtils.REFERENCE);
        }
        if (CustomFormConstants.CURRENCY.equals(fieldId)) {
            return currencyService.getCurrencies();
        }
        return new SelectItem[0];
    }

    private SelectItem[] getTaxCalcTypes() {
        return new SelectItem[]{
                new SelectItem(NO_TAX_CALCULATION, commonLocalizer.localize("noTax")),
                new SelectItem(TAX_CALCULATION_INCLUSIVE, commonLocalizer.localize("taxInclusive")),
                new SelectItem(TAX_CALCULATION_EXCLUSIVE, commonLocalizer.localize("taxExclusive"))
        };
    }

    private SelectItem[] contactPredefinedValues(String fieldId) {
        if (CustomFormConstants.FIRST_NAME.equals(fieldId)) {
            return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsEmployeeProfile.TITLE), ServerUtils.REFERENCE);
        }
        if (CustomFormConstants.CRM_ACCOUNT_TYPE.equals(fieldId)) {
            return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE), ServerUtils.REFERENCE);
        }
        return new SelectItem[0];
    }

    private SelectItem[] productPredefinedValues(String fieldId) {
        if (CustomFormConstants.CATEGORY.equals(fieldId)) {
            return accountingService.getCategoriesAsSelectItem();
        }
        return new SelectItem[0];
    }

    private SelectItem[] leadPredefinedValues(String fieldId) {
        if (CustomFormConstants.ASSIGNEE.equals(fieldId)) {
            return crmService.getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER);
        }
        if (CustomFormConstants.BACKUP_ASSIGNEE.equals(fieldId)) {
            return crmService.getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER);
        }
        if (CustomFormConstants.LEAD_OWNER.equals(fieldId)) {
            return crmService.getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER);
        }
        if (CustomFormConstants.STATUS.equals(fieldId)) {
            return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_STATUS), ServerUtils.REFERENCE);
        }
        if (CustomFormConstants.RATING.equals(fieldId)) {
            return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_RATING), ServerUtils.REFERENCE);
        }
        if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(fieldId)) {
            return ServerUtils.getAsSelectItem(referenceManager.listReferences(_COMPANY_WORKAREA), ServerUtils.REFERENCE);
        }

        return new SelectItem[0];
    }
}
