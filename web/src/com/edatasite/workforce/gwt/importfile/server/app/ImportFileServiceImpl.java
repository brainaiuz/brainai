package com.edatasite.workforce.gwt.importfile.server.app;

import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sher on 1/12/2016.
 */

@Transactional
@Service("importFileService")
public class ImportFileServiceImpl implements ImportFileService, ImportFileServiceLocal {
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private CommonServiceLocal commonServiceLocal;

    @Override
    @Transactional
    public String addImportToQueue(ImportFile importFile) {
        EdsUser employee = employeeManager.getUser();
        ImportTypeEnum type = importFile.getType();
        String event = "";
        if (ImportTypeEnum.CONTACT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_CONTACT;
        } else if (ImportTypeEnum.CRM_ACCOUNT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_CRMACCOUNT;
        } else if (ImportTypeEnum.LEAD.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_LEAD;
        } else if (ImportTypeEnum.CANDIDATE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_CANDIDATE;
        } else if (ImportTypeEnum.CUSTOMER.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_CLIENT;
        } else if (ImportTypeEnum.SUPPLIER.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_SUPPLIER;
        } else if (ImportTypeEnum.PRODUCT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_PRODUCT;
        } else if (ImportTypeEnum.PRODUCT_FROM_PARENT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_PRODUCT_FROM_PARENT;
        } else if (ImportTypeEnum.NIMBLE_COMMERCE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_NIMBLE_COMMERCE;
        } else if (ImportTypeEnum.CUSTOM_INVOICE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_CUSTOM_INVOICE;
        } else if (ImportTypeEnum.CHART_OF_ACCOUNTS.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_CHART_OF_ACCOUNTS;
        } else if (ImportTypeEnum.OPPORTUNITY.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_OPPORTUNITY;
        } else if (ImportTypeEnum.EMPLOYEE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_EMPLOYEE;
        } else if (ImportTypeEnum.EXPENSE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_EXPENSE;
        } else if (ImportTypeEnum.COMPANY_EXPENSE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_COMPANY_EXPENSE;
        } else if (ImportTypeEnum.PROJECT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_PROJECT;
        } else if (ImportTypeEnum.VCARD_CONTACT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_VCARD_CONTACT;
        } else if (ImportTypeEnum.MANUAL_TRANSACTION.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_MANUAL_TRANSACTION;
        } else if (ImportTypeEnum.MANUAL_TRANSACTION_TALLY.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_MANUAL_TRANSACTION_TALLY;
        } else if (ImportTypeEnum.ADDITIONAL_PAYMENT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_ADDITIONAL_PAYMENT;
        } else if (ImportTypeEnum.BANK_TRANSFER_TRANSACTION.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_BANK_TRANSFER_TRANSACTION;
        } else if (ImportTypeEnum.BUDGET_MANAGER.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_BUDGET_MANAGER;
        } else if (ImportTypeEnum.REPORT_DATA.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_REPORT_DATA;
        } else if (ImportTypeEnum.GROUP_PAYRUN.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_GROUP_PAYRUN;
        } else if (ImportTypeEnum.PAYMENT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_PAYMENT;
        } else if (ImportTypeEnum.DEDUCTION.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_DEDUCTION;
        } else if (ImportTypeEnum.PRODUCT_CATEGORIES.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_PRODUCT_CATEGORIES;
        } else if (ImportTypeEnum.BRAND.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_BRAND;
        } else if (ImportTypeEnum.PURCHASE_ORDER.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_PURCHASE_ORDER;
        } else if (ImportTypeEnum.ANNUAL_ALLOWANCE.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_EMPLOYEE_LEAVE_ALLOWANCE;
        } else if (ImportTypeEnum.POSITION.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_POSITION;
        } else if (ImportTypeEnum.DEPARTMENT.equals(type)) {
            event = ImportCustomEventListenerImpl.EVENT_IMPORT_DEPARTMENT;
        }

        EdsImportFile lastImportFile = importFileManager.getQueueByUser(employee, event, type);
        if (lastImportFile != null) {
            return type.getCode();
        }
        if (importFile.getConversionDate() != null) {
            importFile.setConversionDate(ServerUtils.convertServerDateToUserDate(importFile.getConversionDate(), employee.getUserTimezone()));
        }

        EdsImportFile edsImportFile = commonServiceLocal.saveImportFile(importFile, employee);

        if (importFile.getMailingListId() != null) {
            EdsMailList edsMailList = mailListManager.get(importFile.getMailingListId());
            if (edsMailList != null && edsMailList.getObjectID() != null) {
                baseEventsPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, event, edsImportFile, employee, edsMailList);
            }
        } else {
            baseEventsPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, event, edsImportFile, employee);
        }
        return null;
    }

    @Override
    public String getImportPreference() {
        EdsCompanySettings companySettings = employeeManager.getUser().getCompany().getCompanySettings();
        return companySettings.getImportPreference();
    }
}
