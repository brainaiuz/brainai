package com.edatasite.workforce.gwt.core.server.app.settings.module.impl;

import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsModuleLocalize;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.mobile.EdsUserModule;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.settings.module.UserModuleService;
import com.edatasite.workforce.gwt.core.server.db.ModuleLocalizeManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.settings.mobile.UserModuleManager;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.ToggleUserModuleDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.UserModuleDTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.DOCUMENTS_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PAYROLL_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.REPORTING_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.TC_MAIN_MENU;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.WORKSPACE_MAIN_MENU;

@Service("userModuleService")
public class UserModuleServiceImpl implements UserModuleService {
    @Autowired
    private UserModuleManager userModuleManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private ModuleLocalizeManager moduleLocalizeManager;


    @Override
    public List<EdsUserModule> findAllByUserIdAndSelected(Integer userId, Boolean selected) {
        return userModuleManager.findAllByUserIdAndSelected(userId, selected);
    }

    @Transactional
    @Override
    public void toggleModules(List<ToggleUserModuleDTO> modulesToSave) {
        List<EdsUserModule> entity = modulesToSave.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
        deleteModulesByUser(roleManager.getUser().getObjectID());
        createUserModules(entity);
    }

    private void createUserModules(List<EdsUserModule> entity) {
        for (EdsUserModule edsUserModule : entity) {
            userModuleManager.create(edsUserModule);
        }
    }

    private void deleteModulesByUser(Integer userId) {
        for (EdsUserModule edsUserModule : findAllByUserIdAndSelected(userId, true)) {
            userModuleManager.delete(edsUserModule);
        }
    }

    private UserModuleDTO toDto(EdsUserModule entity) {
        if (entity == null) return null;
        UserModuleDTO dto = new UserModuleDTO();
        dto.setSelected(entity.isSelected());
        dto.setModuleCode(entity.getModule().getCode());
        dto.setOrder(entity.getOrder());
        dto.setTitle(resolveTitle(entity.getModule().getCode()));
        return dto;
    }

    private EdsUserModule fromDto(ToggleUserModuleDTO dto) {
        EdsUserModule entity = new EdsUserModule();
        entity.setModule(moduleManager.getModuleByCode(dto.getModuleCode()));
        entity.setUser(roleManager.getUser());
        entity.setSelected(true);
        entity.setOrder(dto.getOrder());
        return entity;
    }

    @Override
    public List<UserModuleDTO> mapUserModule(HashSet<String> companyModules, List<EdsUserModule> userModules, Boolean selected) {
        List<UserModuleDTO> response = userModules.stream()
                .filter(m -> companyModules.contains(m.getModule().getCode()))
                .map(this::toDto)
                .collect(Collectors.toList());
        List<String> userModuleNames = userModules.stream().map(EdsUserModule::getModule).map(EdsModule::getCode).toList();
        if (selected != null && selected) {
            return response;
        }
        for (String companyModule : companyModules) {
            if (userModuleNames.contains(companyModule)) continue;
            String title = this.resolveTitle(companyModule);
            if (title.equals(companyModule)) continue;
            UserModuleDTO dto = new UserModuleDTO();
            dto.setModuleCode(companyModule);
            dto.setTitle(title);
            response.add(dto);
        }
        return response;
    }

    private String resolveTitle(String code) {
        return switch (code) {
            case PermissionConstants.TASK_MANAGEMENT -> wfmLocalizer.localize("taskManagement");
            case PermissionConstants.ISSUE_TRACKING -> wfmLocalizer.localize("issueTracking");
            case PermissionConstants.TIMESHEET -> wfmLocalizer.localize("timesheet");
            case PermissionConstants.MONTHLY_TIMESHEET -> wfmLocalizer.localize("monthlyTimesheet");
            case PermissionConstants.RESOURCE_PLANNING -> wfmLocalizer.localize("resourcePlanning");
            case PermissionConstants.BOOKING_ITEMS -> wfmLocalizer.localize("bookingItems");
            case PermissionConstants.GANTT_CHART -> wfmLocalizer.localize("ganttChart");
            case PermissionConstants.TIMER -> wfmLocalizer.localize("timer");
            case PermissionConstants.IMPORT_FROM_MS_PROJECT -> wfmLocalizer.localize("importFromMSProject");
            case PermissionConstants.CUSTOMER_CENTER -> wfmLocalizer.localize("customerCenter");
            case PermissionConstants.LEAD_MANAGEMENT -> wfmLocalizer.localize("leadManagement");
            case PermissionConstants.CONTACT_MANAGEMENT -> wfmLocalizer.localize("contactManagement");
            case PermissionConstants.OPPORTUNITY_TRACKING -> wfmLocalizer.localize("opportunityTracking");
            case PermissionConstants.ACTIVITIES -> wfmLocalizer.localize("activities");
            case PermissionConstants.CRM_CALENDAR -> wfmLocalizer.localize("calenderCrm");
            case PermissionConstants.IS_ACTIVE_WORKFLOW_STATUS -> wfmLocalizer.localize("workflowRecurrence");
            case PermissionConstants.CASE_MANAGEMENT -> wfmLocalizer.localize("caseManagement");
            case PermissionConstants.MESSAGE_CENTER -> wfmLocalizer.localize("messageCenter");
            case PermissionConstants.SMS_INTEGRATION -> wfmLocalizer.localize("smsIntegration");
            case PermissionConstants.SOLUTION_MANAGEMENT -> wfmLocalizer.localize("solutionsManagement");
            case PermissionConstants.CRM_REQUEST_FOR_QUOTES -> wfmLocalizer.localize("crmRequestForQuotes");
            case PermissionConstants.PRODUCTS_SERVICES_CRM -> wfmLocalizer.localize("productsServicesCrm");
            case PermissionConstants.SALES_QUOTES -> wfmLocalizer.localize("salesQuotes");
            case PermissionConstants.SALES_ORDERS -> wfmLocalizer.localize("salesOrders");
            case PermissionConstants.REQUEST_FOR_QUOTES -> wfmLocalizer.localize("requestForQuotes");
            case PermissionConstants.RECURRING_BILLS -> wfmLocalizer.localize("recurringBills");
            case PermissionConstants.SUPPLIER_CENTER -> wfmLocalizer.localize("supplierCenter");
            case PermissionConstants.BANK_ACCOUNTS -> wfmLocalizer.localize("bankAccounts");
            case PermissionConstants.ACCOUNTING_CHART_OF_ACCOUNTS -> wfmLocalizer.localize("chartOfAccounts");
            case PermissionConstants.RESERVATIONS -> wfmLocalizer.localize("reservations");
            case PermissionConstants.CONSIGNMENTS -> wfmLocalizer.localize("consignments");
            case PermissionConstants.REQUEST_FOR_PURCHASES -> wfmLocalizer.localize("requestForPurchase");
            case PermissionConstants.TIMESHEET_INVOICES -> wfmLocalizer.localize("timesheetInvoice");
            case PermissionConstants.SALES_INVOICING -> wfmLocalizer.localize("salesInvoice");
            case PermissionConstants.PURCHASE_ORDERS -> wfmLocalizer.localize("purchaseorder");
            case PermissionConstants.PURCHASE_INVOICING -> wfmLocalizer.localize("purchaseInvoicing");
            case PermissionConstants.RECCURING_INVOICES -> wfmLocalizer.localize("recurringInvoices");
            case PermissionConstants.INVENTORY_MANAGEMENT -> wfmLocalizer.localize("inventoryManagement");
            case PermissionConstants.EXPENSE_REPORTING -> wfmLocalizer.localize("expenseReporting");
            case PermissionConstants.FIXED_ASSESTS -> wfmLocalizer.localize("fixedAsset");
            case PermissionConstants.CHECKS -> wfmLocalizer.localize("checks");
            case PermissionConstants.MANUAL_TRANSACTIONS -> wfmLocalizer.localize("manualTransactions");
            case PermissionConstants.ACCOUNTING_CUSTOMER_CENTER -> wfmLocalizer.localize("customerCenter");
            case PermissionConstants.PRODUCTS_SERVICES -> wfmLocalizer.localize("productsOrServices");
            case PermissionConstants.PRODUCT_INVENTORY_ITEMS -> wfmLocalizer.localize("inventoryItems");
            case PermissionConstants.PRODUCT_RENTAL_ITEMS -> wfmLocalizer.localize("rentalItems");
            case PermissionConstants.PRODUCT_ASSEMBLY_ITEMS -> wfmLocalizer.localize("assemblyItems");
            case PermissionConstants.RENTAL_ORDER_MODULE -> wfmLocalizer.localize("rentalOrder");
            case PermissionConstants.LEAVE_MANAGEMENT -> wfmLocalizer.localize("leaveManagement");
            case PermissionConstants.ATTENDING_TRACKING -> wfmLocalizer.localize("attendanceTracking");
            case PermissionConstants.PERFORMANCE_APPRAISAL -> wfmLocalizer.localize("performanceAppraisal");
            case PermissionConstants.GOAL_MANAGEMENT -> wfmLocalizer.localize("goalManagement");
            case PermissionConstants.EMPLOYEE_EXPENSES -> wfmLocalizer.localize("employeeExpenses");
            case PermissionConstants.EMPLOYEE_INCIDENTS -> wfmLocalizer.localize("employeeIncidents");
            case PermissionConstants.ONBOARDING -> wfmLocalizer.localize("onboarding");
            case PermissionConstants.TELEGRAM_CHATS -> wfmLocalizer.localize("telegramChats");
            case PermissionConstants.ADDITIONAL_MODULE -> wfmLocalizer.localize("{");
            case PermissionConstants.PAYROLL -> wfmLocalizer.localize("payroll");
            case PermissionConstants.REPORTING_SYSTEM -> wfmLocalizer.localize("reportingSystem");
            case PermissionConstants.DOCUMENT_MANAGEMENT -> wfmLocalizer.localize("documentManagement");
            case PermissionConstants.TRAINING_CENTER -> wfmLocalizer.localize("trainingCenter");
            case PermissionConstants.WEBSITES -> wfmLocalizer.localize("websites");
            case PermissionConstants.PERMISSION_MANAGEMENT -> wfmLocalizer.localize("permissionManagement");
            case PermissionConstants.CUSTOM_FIELDS -> wfmLocalizer.localize("customFields");
            case PermissionConstants.CUSTOM_REFERENCES -> wfmLocalizer.localize("customReferences");
            case PermissionConstants.SYNCRONIZE_WITH_GOOGLE -> wfmLocalizer.localize("syncronizeWithGoogle");
            case PermissionConstants.REMINDERS -> wfmLocalizer.localize("reminders");
            case PermissionConstants.ADD_ONS_MODULE -> wfmLocalizer.localize("{");
            case PermissionConstants.IPHONE_APPS -> wfmLocalizer.localize("iPhoneApps");
            case PermissionConstants.ANDROID_APPS -> wfmLocalizer.localize("androidApps");
            case PermissionConstants.TIMESHEET_PLUGIN -> wfmLocalizer.localize("timesheetPlugin");
            case PermissionConstants.OUTLOOK_PLUGIN -> wfmLocalizer.localize("outlookPlugin");
            case PermissionConstants.STOREFRONT -> wfmLocalizer.localize("storefront");
            case PermissionConstants.INVOICE_TEMPLATES -> wfmLocalizer.localize("invoiceTemplates");
            default -> code;
        };
    }
    public List<UserModuleDTO> getUserModules(){
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        HashMap<String, String> moduleLocalizeMap = new HashMap<>();
        List<EdsModuleLocalize> moduleLocalizers = moduleLocalizeManager.listModuleLocalize();
        if (moduleLocalizers != null && !moduleLocalizers.isEmpty()) {
            for (EdsModuleLocalize moduleLocalize : moduleLocalizers) {
                moduleLocalizeMap.put(moduleLocalize.getModuleCode(), moduleLocalize.getName());
            }
        }
        List<String> permissions = new ArrayList<>(Arrays.asList(
                WORKSPACE_MAIN_MENU,
                ACCOUNTING_MAIN_MENU,
                CRM_MAIN_MENU,
                HRMS_MAIN_MENU,
                PM_MAIN_MENU,
                PAYROLL_MAIN_MENU,
                REPORTING_MAIN_MENU,
                DOCUMENTS_MAIN_MENU,
                TC_MAIN_MENU
        ));
        List<String> userHasPermissions = permissionManager.getPermissions(permissions, user);

       return userHasPermissions.stream()
                .map(up -> {
                    UserModuleDTO userModuleDTO = null;
                    switch (up){
                        case  WORKSPACE_MAIN_MENU:
                            userModuleDTO =  new UserModuleDTO(WORKSPACE_MAIN_MENU,true,null,null);
                            break;
                            case  ACCOUNTING_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(ACCOUNTING_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.ACCOUNTING.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.ACCOUNTING.getCode())
                                    : wfmLocalizer.localize("accounts"));
                            break;
                            case  CRM_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(CRM_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.CRM.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.CRM.getCode())
                                    :wfmLocalizer.localize("crm"));
                            break;
                            case  HRMS_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(HRMS_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.HRMS.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.HRMS.getCode())
                                    : wfmLocalizer.localize("hrms"));
                            break;
                            case  PM_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(PM_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.PM.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.PM.getCode())
                                    : wfmLocalizer.localize("projects"));
                            break;
                            case  PAYROLL_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(PAYROLL_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.PAYROLL.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.PAYROLL.getCode())
                                    : wfmLocalizer.localize("payroll"));
                            break;
                            case  REPORTING_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(REPORTING_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.REPORTING.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.REPORTING.getCode())
                                    : wfmLocalizer.localize("reports"));
                            break;
                            case  DOCUMENTS_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(DOCUMENTS_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.DOCUMENTS.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.DOCUMENTS.getCode())
                                    : wfmLocalizer.localize("docs"));
                            break;
                            case  TC_MAIN_MENU:
                                userModuleDTO = new UserModuleDTO(TC_MAIN_MENU,true,null,
                                    moduleLocalizeMap.get(ModuleEnum.TC.getCode()) != null ? moduleLocalizeMap.get(ModuleEnum.TC.getCode())
                                    : wfmLocalizer.localize("trainingCenter"));
                            break;
                    }
                    return userModuleDTO;
                }).collect(Collectors.toList());
    }


}
