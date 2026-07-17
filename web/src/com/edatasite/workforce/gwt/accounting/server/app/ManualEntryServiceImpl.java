package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.EdsManualJournalItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.PathFinder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExchangeRateHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.ManualJournalEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectBudgetCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.FindEncodeInputStream;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Dilsh0d Madrahimov on 4/11/2017.
 */
@Service("manualEntryService")
@Transactional
public class ManualEntryServiceImpl implements ManualEntryService, ManualEntryServiceLocal, Constants, AccountingConstants {

    private static final Logger log = LoggerFactory.getLogger(ManualEntryServiceImpl.class);

    @Autowired
    private ManualJournalManager manualJournalManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private UserManager userManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private LayoutManager layoutManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ExchangeRateHistoryManager exchangeRateHistoryManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ManualJournalNoteManager manualJournalNoteManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private ManualJournalItemCFManager manualJournalItemCFManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private RoleManager roleManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ManualJournalListItem> getManualTransactions(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get manual transactions list");

        ArrayList<ManualJournalListItem> items = new ArrayList<>();
        List<Object[]> manualJournalList = manualJournalManager.getManualJournalList(fp);
        int totalCount = manualJournalManager.getManualJournalsCount(fp);

        List<Integer> recurringManualJournalIDList = new LinkedList<>();
        for (Object[] mjData : manualJournalList) {
            recurringManualJournalIDList.add((Integer) mjData[0]);
        }
        Map<Integer, Date> nextInvoiceDates = null;
        if (!fp.isFromExcelPDF()) {
            try {
                nextInvoiceDates = recurrenceService.getNextFireTimesAsMap(recurringManualJournalIDList.toArray(new Integer[]{}), SchedulerConstant.RECURRING_MANUAL_JOURNAL_REMINDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = new GregorianCalendar();

        EdsCompany company = manualJournalManager.getUser().getCompany();
        Integer currentUserObjectId = manualJournalManager.getUser().getObjectID();
        for (Object[] manualJournalObj : manualJournalList) {
            ManualJournalListItem item = new ManualJournalListItem();

            item.setObjectId((Integer) manualJournalObj[0]);
            if (!fp.isFromExcelPDF()) {
                item.setUsed(manualJournalManager.isUsedForPayments((Integer) manualJournalObj[0]));
            }
            item.setNarration((String) manualJournalObj[1]);
            item.setDate(new DateNonConvertable((Date) manualJournalObj[2]));
            item.setCredit((BigDecimal) manualJournalObj[3]);
            item.setDebit((BigDecimal) manualJournalObj[4]);
            item.setStatus((String) manualJournalObj[5]);
            item.setReferenceNumber((String) manualJournalObj[6]);

            item.setRecurringTemplate(manualJournalObj[7] != null ? ((Boolean) manualJournalObj[7]) : false);
            item.setNumber((String) manualJournalObj[8]);
            item.setCreator((String) manualJournalObj[10]);
            if (manualJournalObj[14] != null) {
                item.setCreatorId((Integer) manualJournalObj[14]);
            }
            item.setCurrency((String) manualJournalObj[11]);
            Integer approverId = (Integer) manualJournalObj[12];
            item.setApprover(currentUserObjectId.equals(approverId));
            item.setSetupAP(approverId != null);
            item.setCurrentApprover((String) manualJournalObj[13]);
            if (item.isRecurringTemplate()) {
                item.setRepeats(recurrenceService.getRecurrenceTemplateString(item.getObjectId(), SchedulerConstant.RECURRING_MANUAL_JOURNAL_REMINDER));

                if (nextInvoiceDates != null) {
                    item.setNextCreationDate(nextInvoiceDates.get(item.getObjectId()));
                }
                EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_MANUAL_JOURNAL_REMINDER, item.getObjectId(), company.getObjectID());
                if (recurrence != null) {
                    Date recEndDate = recurrenceManager.getTriggerEndDate(recurrence, true);
                    if (recurrence.getEndType() == SchedulerConstant.END_AFTER_OCCURRENCES && recurrence.getType() == SchedulerConstant.RECURRENCE_TYPE_DAILY &&
                            recurrence.getDailyPatternOptions() == SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL) {
                    }
                    item.setEndDate(recEndDate);
                    item.setRecurrenceStatus(recEndDate.before(calendar.getTime()) ? "Ended" : "Active");
                }
            }
            item.setProject((String) manualJournalObj[9]);
            items.add(item);
        }

        return new ListResult<>(items, totalCount);
    }

    @Override
    @Transactional
    public Boolean voidManualJournal(Integer manualJournalID, DateNonConvertable voidDate) {
        try {
            EdsManualJournal manualJournal = manualJournalManager.get(manualJournalID);
            EdsManualTransaction manualTrans = transactionManager.getTransactionByManualJournal(manualJournal)/*(EdsManualTransaction) transactionManager.get(transactionId)*/;
            if (manualTrans != null) {
                EdsManualTransaction transaction = new EdsManualTransaction();
                transaction.setJournalDate((voidDate != null && voidDate.getNonConvertedDate() != null)
                        ? voidDate.getNonConvertedDate()
                        : manualTrans.getJournalDate());
                transaction.setName(manualTrans.getName());
                transaction.setPostedDate(manualTrans.getPostedDate());
                transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
                transaction.setPostedBy(transactionManager.getUser());
                transaction.setManualJournal(manualJournal);
                transaction.setDescription(manualTrans.getDescription());
                transaction.setReversalTransaction(manualTrans);

                if (manualTrans.getTransactionItems() != null && !manualTrans.getTransactionItems().isEmpty()) {
                    EdsTransactionItem item;
                    for (EdsTransactionItem ti : manualTrans.getTransactionItems()) {
                        item = new EdsTransactionItem();
                        item.setDescription(ti.getDescription());
                        item.setAccount(ti.getAccount());
                        if (ti.getCrmAccount() != null) {
                            item.setCrmAccount(ti.getCrmAccount());
                        }
                        item.setTax(ti.getTax());
                        item.setDebit(ti.getCredit());
                        item.setCredit(ti.getDebit());
                        item.setForeignDebit(ti.getForeignCredit());
                        item.setForeignCredit(ti.getForeignDebit());
                        transaction.addTransactionItem(item);
                    }
                }
                transactionManager.create(transaction);
            }
            manualJournal.setOverallStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.REVERSED));
            baseEventPostProcessor.registerEvent(ManualJournalEventListenerImpl.TYPE, ManualJournalEventListenerImpl.MANUAL_JOURNAL_VOID, manualJournal, userManager.getUser());
            manualJournalManager.update(manualJournal);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteManualJournal(Integer manualJournalID) {
        EdsManualJournal manualJournal = manualJournalManager.get(manualJournalID);
        if (manualJournal == null) {
            return false;
        }
        try {
            List<EdsManualTransaction> transaction = transactionManager.getTransactionsByManualJournal(manualJournal);

            if (transaction != null && !transaction.isEmpty()) {
                for (EdsManualTransaction manualTransaction : transaction) {
                    if (manualTransaction != null) {
                        manualTransaction.setDeleted(true);
                        transactionManager.setChangedAccountsForRecalculate(manualTransaction.getObjectID());
                        transactionManager.update(manualTransaction);

                        List<EdsManualJournalItem> journalItems = manualJournal.getItems();
                        if (journalItems != null) {
                            for (EdsManualJournalItem mji : journalItems) {
                                mji.setTransactionItem(null);
                            }
                        }
                    }
                }
            }
            manualJournal.setDeleted(true);
            manualJournalManager.update(manualJournal);

            baseEventPostProcessor.registerEvent(ManualJournalEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, manualJournal, userManager.getUser());
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(manualJournalID);
            ServerUtils.kpiLog(log, kpiLog, "Delete manual transaction");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Integer deleteSelectedManualEntryServices(ArrayList<Integer> ids) {
        int countDeleted = 0;
        for (Integer objectID : ids) {
            boolean deleted = deleteManualJournal(objectID);
            if (deleted) {
                countDeleted++;
            }
        }
        return countDeleted;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ManualTransactionData getManualJournalsData(Integer objectId) {
        CurrencyItem companyBaseCurrency = currencyService.getCompanyBaseCurrency();

        ManualTransactionData result = new ManualTransactionData();
        result.setCurrencyItems(currencyService.getCurrencies());
        result.setBaseCurrency(companyBaseCurrency);
        result.setConversionDate(accountingServiceLocal.getConversionDate(0));
        result.setLayoutHtml(PathFinder.getLayoutHTML(MANUAL_TRANSACTIONS));
        result.setCurrentUserId(userManager.getUser().getObjectID());
        NewManualTransaction transaction = null;
        if (objectId != null) {
            transaction = getManualJournal(objectId);
            result.setManualTransactionItem(transaction);
            result.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_MANUAL_JOURNAL, objectId));
        } else {
            NewManualTransaction manualTransaction = new NewManualTransaction();
            manualTransaction.setCurrency(companyBaseCurrency);
            manualTransaction.setExchangeRate(BigDecimal.ONE);
            manualTransaction.setTransferNumberData(generateManualTransactionMoneyNumber());
            StringBuilder sb = new StringBuilder();
            sb.append(manualTransaction.getTransferNumberData().getPrefix());
            sb.append(manualTransaction.getTransferNumberData().getFourDigitNumber());
            if (manualTransaction.getTransferNumberData().isWithDate()) {
                sb.append("-");
                sb.append(ServerUtils.getBankTransferDateNumber(new Date()));
            }
            manualTransaction.setNumber(sb.toString());

            manualTransaction.setPdfTemplateList(invoiceServiceLocal.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.MANUAL_ENTRY.name()));
            manualTransaction.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ManualJournalItem));
            manualTransaction.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.MANUAL_JOURNAL_ITEM));
            result.setManualTransactionItem(manualTransaction);
        }
        if (transaction != null && userManager.getUser() != null && transaction.getApprover() != null) {
            result.setApprover(userManager.getUser().getObjectID().equals(transaction.getApprover().getId()));
        }
        if (transaction != null && transaction.getApprover() == null) {
            result.setSetUpAP(false);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ManualTransactionData getManualJournalsData(Integer objectId, boolean isMemorized) {
        CurrencyItem companyBaseCurrency = currencyService.getCompanyBaseCurrency();

        ManualTransactionData result = new ManualTransactionData();
        result.setCurrencyItems(currencyService.getCurrencies());
        result.setBaseCurrency(companyBaseCurrency);
        result.setConversionDate(accountingServiceLocal.getConversionDate(0));
        result.setLayoutHtml(PathFinder.getLayoutHTML(MANUAL_TRANSACTIONS));
        result.setCurrentUserId(userManager.getUser().getObjectID());
        if (objectId != null) {
            result.setManualTransactionItem(getManualJournal(objectId, isMemorized));
            result.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_MANUAL_JOURNAL, objectId));
        } else {
            NewManualTransaction manualTransaction = new NewManualTransaction();
            manualTransaction.setCurrency(companyBaseCurrency);
            manualTransaction.setExchangeRate(BigDecimal.ONE);
            manualTransaction.setTransferNumberData(generateManualTransactionMoneyNumber());
            StringBuilder sb = new StringBuilder();
            sb.append(manualTransaction.getTransferNumberData().getPrefix());
            sb.append(manualTransaction.getTransferNumberData().getFourDigitNumber());
            if (manualTransaction.getTransferNumberData().isWithDate()) {
                sb.append("-");
                sb.append(ServerUtils.getBankTransferDateNumber(new Date()));
            }
            manualTransaction.setNumber(sb.toString());

            manualTransaction.setPdfTemplateList(invoiceServiceLocal.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.MANUAL_ENTRY.name()));
            manualTransaction.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ManualJournalItem));
            manualTransaction.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.MANUAL_JOURNAL_ITEM));
            result.setManualTransactionItem(manualTransaction);
        }
        result.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_MANUAL_JOURNAL));
        return result;
    }

    @Override
    @Transactional
    public List<HistoryNote> getManualJournalHistoryNote(Integer objectId) {
        List<HistoryNote> result = new ArrayList<>();
        if (objectId != null) {
            List<HistoryListItem> notes = getAsHistoryItems(manualJournalNoteManager.getManualJournalNoteByManualJournalId(objectId));
            result.addAll(notes);
            List<MyUpdateItem> updates = invoiceServiceLocal.getAllHistory(objectId, MANUAL_JOURNAL);
            result.addAll(updates);
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewManualTransaction getManualJournal(Integer objectId) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(log, kpiLog, "View manual transaction");
        NewManualTransaction transForEdit = new NewManualTransaction();
        EdsManualJournal manualJournal = manualJournalManager.get(objectId);
        if (manualJournal == null) {
            return transForEdit;
        }
        manualJournal.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ManualJournalItem));
        transForEdit.setObjectId(manualJournal.getObjectID());
        transForEdit.setDate(new DateNonConvertable(manualJournal.getDate()));
        transForEdit.setReference(manualJournal.getReference());
        transForEdit.setNarration(manualJournal.getNarration());
        transForEdit.setShowOnCashReports(manualJournal.isShowOnCashReports());
        if (manualJournal.getProject() != null) {
            transForEdit.setProjectId(manualJournal.getProject().getObjectID());
            transForEdit.setProject(new SelectItem(manualJournal.getProject().getObjectID(),
                    (manualJournal.getProject().getNumber() != null && !"".equals(manualJournal.getProject().getNumber().trim()) ? manualJournal.getProject().getNumber() + " -> " : "") + manualJournal.getProject().getName(),
                    manualJournal.getProject().getNumber()));
        }
        if (manualJournal.getRole() != null) {
            transForEdit.setRoleId(manualJournal.getRole().getObjectID());
            transForEdit.setRole(new SelectItem(manualJournal.getRole().getObjectID(), manualJournal.getRole().getName()));
        }
        if (manualJournal.getOverallStatus() != null) {
            transForEdit.setStatus(manualJournal.getOverallStatus().getCode());
        }
        if (manualJournal.getCreator() != null) {
            transForEdit.setCreatorItem(manualJournal.getCreator().getAsSelectItem());
        }
        transForEdit.setMemorizedTransaction(manualJournal.isMemorizedTransaction());
        if (manualJournal.getNumber() != null && manualJournal.getIntNumber() != null) {
            transForEdit.setNumber(manualJournal.getNumber());
            transForEdit.setIntNumber(manualJournal.getIntNumber());
            BankTransferNumberData transferNumberData = generateManualTransactionMoneyNumber();
            transferNumberData.setFourDigitNumber(manualJournal.getIntNumber() != null
                    ? new DecimalFormat("0000").format(manualJournal.getIntNumber())
                    : "");
            transForEdit.setTransferNumberData(transferNumberData);
        } else {
            BankTransferNumberData transferNumberData = generateManualTransactionMoneyNumber();
            transForEdit.setTransferNumberData(transferNumberData);
        }

        CurrencyItem companyBaseCurrency = currencyService.getCompanyBaseCurrency();

        transForEdit.setCurrency(manualJournal.getCurrency().createCurrencyItem());
        transForEdit.setBaseCurrency(companyBaseCurrency);
        transForEdit.setExchangeRate(manualJournal.getExchangeRate());
        transForEdit.setPdfTemplateID(manualJournal.getPdfTemplate() != null
                ? manualJournal.getPdfTemplate().getObjectID()
                : null);
        transForEdit.setPdfTemplateList(invoiceServiceLocal.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.MANUAL_ENTRY.name()));
        transForEdit.setUsed(manualJournalManager.isUsedForPayments(objectId));
        if (manualJournal.getCurrentApprover() != null && manualJournal.getCurrentApprover().getExactEmployee() != null) {
            if (manualJournal.getCurrentApprover().getExactEmployee().isEmployee()) {
                EdsEmployee edsEmployee = manualJournal.getCurrentApprover().getExactEmployee().getEmployee();
                if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                    transForEdit.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                } else {
                    transForEdit.setApprover(manualJournal.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            } else {
                transForEdit.setApprover(manualJournal.getCurrentApprover().getExactEmployee().getAsSelectItem());
            }
        }
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = financialSettings.getAccountingCalculationScale();

        BigDecimal debitTaxTotal = ZERO, creditTaxTotal = ZERO, debitTotal = ZERO, creditTotal = ZERO;
        if (manualJournal.getItems() != null) {
            NewManualTransactionItem[] items = new NewManualTransactionItem[manualJournal.getItems().size()];
            int i = 0;
            for (EdsManualJournalItem item : manualJournal.getItems()) {
                items[i] = new NewManualTransactionItem();
                items[i].setObjectId(item.getObjectID());
                items[i].setDescription(item.getDescription());
                items[i].setAccountItem(item.getAccount().createAccountItem());
                if (item.getTax() != null) {
                    items[i].setTaxItem(item.getTax().createTaxItem());
                }
                if (item.getClientOrSupplier() != null) {
                    SelectItem csItem = new SelectItem();
                    csItem.setId(item.getClientOrSupplier().getObjectID());
                    csItem.setName(item.getClientOrSupplier().getName());
                    items[i].setCustomerOrSupplier(csItem);
                }
                if (item.getEmployee() != null) {
                    items[i].setEmployee(item.getEmployee().getAsSelectItem());
                }
                if (item.getProject() != null) {
                    SelectItem pItem = new SelectItem();
                    pItem.setId(item.getProject().getObjectID());
                    pItem.setName(item.getProject().getName());
                    items[i].setProject(pItem);
                    if (item.getProject().getParent() != null) {
                        SelectItem parentProj = new SelectItem();
                        parentProj.setId(item.getProject().getParent().getObjectID());
                        parentProj.setName(item.getProject().getParent().getName());
                        items[i].setParentProject(parentProj);
                    }
                }
                items[i].setDepartment(item.getDepartment() != null ? item.getDepartment().getAsSelectItem() : null);
                items[i].setDebit(item.getDebit());
                items[i].setCredit(item.getCredit());
                items[i].setClient(item.getClient() != null ? item.getClient().getAsSelectItem() : null);
                if (manualJournal.getItemCustomFields() != null && item.getManualJournalItemCustomFields() != null) {
                    ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

                    for (CompanyCustomFieldItem cf : manualJournal.getItemCustomFields()) {
                        customFieldItems.add(cf.cloneObject());
                    }
                    items[i].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(item.getManualJournalItemCustomFields(), customFieldItems));
                }

                if (item.getDebit() != null) {
                    BigDecimal taxAmount = item.getTax() != null
                            ? item.getDebit().multiply(item.getTax().getTaxRateAsBigDecimal()).divide(HUNDRED, calculationScale, RoundingMode.HALF_UP)
                            : ZERO;
                    debitTaxTotal = debitTaxTotal.add(taxAmount);
                    debitTotal = debitTotal.add(item.getDebit().add(taxAmount));
                }
                if (item.getCredit() != null) {
                    BigDecimal taxAmount = item.getTax() != null
                            ? item.getCredit().multiply(item.getTax().getTaxRateAsBigDecimal()).divide(HUNDRED, calculationScale, RoundingMode.HALF_UP)
                            : ZERO;
                    creditTaxTotal = creditTaxTotal.add(taxAmount);
                    creditTotal = creditTotal.add(item.getCredit().add(taxAmount));
                }
                i++;
            }
            Arrays.sort(items, Comparator.comparing(NewManualTransactionItem::getObjectId));
            transForEdit.setItems(items);

            transForEdit.setDebitTaxTotal(debitTaxTotal);
            transForEdit.setDebitTotal(debitTotal);
            transForEdit.setCreditTaxTotal(creditTaxTotal);
            transForEdit.setCreditTotal(creditTotal);
        }

        transForEdit.setAttachments(getManualTransactionAttachments(objectId));
        transForEdit.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ManualJournalItem));
        transForEdit.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.MANUAL_JOURNAL_ITEM));

        if (manualJournal.isRecurringTemplate()) {
            transForEdit.setRecurringTemplate(manualJournal.isRecurringTemplate());
            transForEdit.setRecurrenceJobItem(recurrenceService.getRecurringManualJournalRecurrenceItem(manualJournal));
        }

        transForEdit.setEnabledDepartmentRelation(financialSettings.getEnableAccountingDepartmentRelation());
        EdsManualTransaction manualTransaction = transactionManager.getTransactionByManualJournal(manualJournal);
        if (manualTransaction != null) {
            transForEdit.setJournalID(manualTransaction.getJournalId());
        }
        return transForEdit;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewManualTransaction getManualJournal(Integer objectId, boolean isMemorized) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(log, kpiLog, "View manual transaction");
        NewManualTransaction transForEdit = new NewManualTransaction();
        EdsManualJournal manualJournal = manualJournalManager.get(objectId);
        if (manualJournal == null) {
            return transForEdit;
        }
        manualJournal.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ManualJournalItem));
        transForEdit.setObjectId(manualJournal.getObjectID());
        transForEdit.setDate(new DateNonConvertable(manualJournal.getDate()));
        transForEdit.setReference(manualJournal.getReference());
        if (manualJournal.getCreator() != null) {
            transForEdit.setCreatorItem(manualJournal.getCreator().getAsSelectItem());
        }
        transForEdit.setNarration(manualJournal.getNarration());
        transForEdit.setShowOnCashReports(manualJournal.isShowOnCashReports());
        if (manualJournal.getProject() != null) {
            transForEdit.setProjectId(manualJournal.getProject().getObjectID());
            transForEdit.setProject(new SelectItem(manualJournal.getProject().getObjectID(),
                    (manualJournal.getProject().getNumber() != null && !"".equals(manualJournal.getProject().getNumber().trim()) ? manualJournal.getProject().getNumber() + " -> " : "") + manualJournal.getProject().getName(),
                    manualJournal.getProject().getNumber()));
        }
        if (manualJournal.getRole() != null) {
            transForEdit.setRoleId(manualJournal.getRole().getObjectID());
            transForEdit.setRole(new SelectItem(manualJournal.getRole().getObjectID(), manualJournal.getRole().getName()));
        }
        if (manualJournal.getOverallStatus() != null) {
            transForEdit.setStatus(manualJournal.getOverallStatus().getCode());
        }
        transForEdit.setMemorizedTransaction(manualJournal.isMemorizedTransaction());
        if (manualJournal.getNumber() != null && manualJournal.getIntNumber() != null && !isMemorized) {
            transForEdit.setNumber(manualJournal.getNumber());
            transForEdit.setIntNumber(manualJournal.getIntNumber());
            BankTransferNumberData transferNumberData = generateManualTransactionMoneyNumber();
            transferNumberData.setFourDigitNumber(manualJournal.getIntNumber() != null
                    ? new DecimalFormat("0000").format(manualJournal.getIntNumber())
                    : "");
            transForEdit.setTransferNumberData(transferNumberData);
        } else {
            BankTransferNumberData transferNumberData = generateManualTransactionMoneyNumber();
            transForEdit.setTransferNumberData(transferNumberData);
        }

        CurrencyItem companyBaseCurrency = currencyService.getCompanyBaseCurrency();

        transForEdit.setCurrency(manualJournal.getCurrency().createCurrencyItem());
        transForEdit.setBaseCurrency(companyBaseCurrency);
        transForEdit.setExchangeRate(manualJournal.getExchangeRate());
        transForEdit.setPdfTemplateID(manualJournal.getPdfTemplate() != null
                ? manualJournal.getPdfTemplate().getObjectID()
                : null);
        transForEdit.setPdfTemplateList(invoiceServiceLocal.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.MANUAL_ENTRY.name()));
        transForEdit.setUsed(manualJournalManager.isUsedForPayments(objectId));
        if (manualJournal.getCurrentApprover() != null && manualJournal.getCurrentApprover().getExactEmployee() != null) {
            if (manualJournal.getCurrentApprover().getExactEmployee().isEmployee()) {
                EdsEmployee edsEmployee = manualJournal.getCurrentApprover().getExactEmployee().getEmployee();
                if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                    transForEdit.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                } else {
                    transForEdit.setApprover(manualJournal.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            } else {
                transForEdit.setApprover(manualJournal.getCurrentApprover().getExactEmployee().getAsSelectItem());
            }
        }
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = financialSettings.getAccountingCalculationScale();

        BigDecimal debitTaxTotal = ZERO, creditTaxTotal = ZERO, debitTotal = ZERO, creditTotal = ZERO;
        if (manualJournal.getItems() != null) {
            NewManualTransactionItem[] items = new NewManualTransactionItem[manualJournal.getItems().size()];
            int i = 0;
            for (EdsManualJournalItem item : manualJournal.getItems()) {
                items[i] = new NewManualTransactionItem();
                items[i].setObjectId(item.getObjectID());
                items[i].setDescription(item.getDescription());
                items[i].setAccountItem(item.getAccount().createAccountItem());
                if (item.getTax() != null) {
                    items[i].setTaxItem(item.getTax().createTaxItem());
                }
                if (item.getClientOrSupplier() != null) {
                    SelectItem csItem = new SelectItem();
                    csItem.setId(item.getClientOrSupplier().getObjectID());
                    csItem.setName(item.getClientOrSupplier().getName());
                    items[i].setCustomerOrSupplier(csItem);
                }
                if (item.getEmployee() != null) {
                    items[i].setEmployee(item.getEmployee().getAsSelectItem());
                }
                if (item.getProject() != null) {
                    SelectItem pItem = new SelectItem();
                    pItem.setId(item.getProject().getObjectID());
                    pItem.setName(item.getProject().getName());
                    items[i].setProject(pItem);
                    if (item.getProject().getParent() != null) {
                        SelectItem parentProj = new SelectItem();
                        parentProj.setId(item.getProject().getParent().getObjectID());
                        parentProj.setName(item.getProject().getParent().getName());
                        items[i].setParentProject(parentProj);
                    }
                }
                items[i].setDepartment(item.getDepartment() != null ? item.getDepartment().getAsSelectItem() : null);
                items[i].setDebit(item.getDebit());
                items[i].setCredit(item.getCredit());
                items[i].setClient(item.getClient() != null ? item.getClient().getAsSelectItem() : null);
                if (manualJournal.getItemCustomFields() != null && item.getManualJournalItemCustomFields() != null) {
                    ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

                    for (CompanyCustomFieldItem cf : manualJournal.getItemCustomFields()) {
                        customFieldItems.add(cf.cloneObject());
                    }
                    items[i].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(item.getManualJournalItemCustomFields(), customFieldItems));
                }

                if (item.getDebit() != null) {
                    BigDecimal taxAmount = item.getTax() != null
                            ? item.getDebit().multiply(item.getTax().getTaxRateAsBigDecimal()).divide(HUNDRED, calculationScale, RoundingMode.HALF_UP)
                            : ZERO;
                    debitTaxTotal = debitTaxTotal.add(taxAmount);
                    debitTotal = debitTotal.add(item.getDebit().add(taxAmount));
                }
                if (item.getCredit() != null) {
                    BigDecimal taxAmount = item.getTax() != null
                            ? item.getCredit().multiply(item.getTax().getTaxRateAsBigDecimal()).divide(HUNDRED, calculationScale, RoundingMode.HALF_UP)
                            : ZERO;
                    creditTaxTotal = creditTaxTotal.add(taxAmount);
                    creditTotal = creditTotal.add(item.getCredit().add(taxAmount));
                }
                i++;
            }
            Arrays.sort(items, Comparator.comparing(NewManualTransactionItem::getObjectId));
            transForEdit.setItems(items);

            transForEdit.setDebitTaxTotal(debitTaxTotal);
            transForEdit.setDebitTotal(debitTotal);
            transForEdit.setCreditTaxTotal(creditTaxTotal);
            transForEdit.setCreditTotal(creditTotal);
        }

        transForEdit.setAttachments(getManualTransactionAttachments(objectId));
        transForEdit.setItemCustomFields(commonService.getCompanyCustomFields(ViewName.ManualJournalItem));
        transForEdit.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.MANUAL_JOURNAL_ITEM));

        if (manualJournal.isRecurringTemplate()) {
            transForEdit.setRecurringTemplate(manualJournal.isRecurringTemplate());
            transForEdit.setRecurrenceJobItem(recurrenceService.getRecurringManualJournalRecurrenceItem(manualJournal));
        }

        transForEdit.setEnabledDepartmentRelation(financialSettings.getEnableAccountingDepartmentRelation());
        EdsManualTransaction manualTransaction = transactionManager.getTransactionByManualJournal(manualJournal);
        if (manualTransaction != null) {
            transForEdit.setJournalID(manualTransaction.getJournalId());
        }
        return transForEdit;
    }

    public BankTransferNumberData generateManualTransactionMoneyNumber() {
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = manualJournalManager.getMTLastIntNumber();
        String format = null;
        if (settings != null) {
            format = settings.getMtNumberingFormat();
        }
        if (format != null) {
            parseBankTransferNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_MANUAL_TRANSACTION_PREFIX;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
            transferNumberData.setNumberString(transferNumberData.getPrefix() + transferNumberData.getFourDigitNumber());
        }
        return transferNumberData;
    }

    private FileItem[] getManualTransactionAttachments(Integer id) {
        List<FileResource> manualTransactionAttachments = attachmentUtilsManager.getAttachments(F_MANUAL_TRANSACTION, id, id);
        FileItem[] fileItems = new FileItem[manualTransactionAttachments.size()];
        for (int i = 0; i < manualTransactionAttachments.size(); i++) {
            FileResource fileResource = manualTransactionAttachments.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setAttachmentId(fileResource.getBodyId());
            fileItem.setFileName(fileResource.getEncodedName());
            fileItem.setDescription(fileResource.getDescription());
            fileItem.setSize(fileResource.getContentLength());
            fileItem.setUploadType(fileResource.getUploadType());
            fileItem.setDate(fileResource.getCreationDate());
            if (GOOGLE.equals(fileResource.getUploadType())) {
                fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
            } else if (OFFICE_365.equals(fileResource.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
                fileItem.setGoogleDocumentLink(fileResource.getOfficeDownloadLink());
            } else {
                fileItem.setAmazonLink(fileResource.getAmazonLink());
            }
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

    private void parseBankTransferNumber(String numberFormat, BankTransferNumberData numberData, Integer fourDigitNumber) {
        String[] mainPartNumbers = numberFormat.split("_");  // e.g CP_0001-05/2015 or CR_0001-05/2015
        String[] datePartNumbers = mainPartNumbers[1].split("-");  // e.g 0001-05/2015 or 0001-05/2015

        numberData.setPrefix(mainPartNumbers[0]);
        numberData.setWithDate(datePartNumbers.length == 2);

        String lastFourNumber = datePartNumbers[0];

        DecimalFormat format = new DecimalFormat("0000");
        numberData.setFourDigitNumber(fourDigitNumber != null ? format.format(fourDigitNumber + 1) : lastFourNumber);
        if (!numberData.getPrefix().isEmpty()){
            numberData.setNumberString(numberData.getPrefix() + numberData.getFourDigitNumber());
        }
        if (numberData.isWithDate()) {
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
        }
    }


    @Override
    @Transactional
    public Boolean updateManualTransaction(Integer manualJournalID, String status) {
        try {
            EdsManualJournal edsManualJournal = manualJournalManager.get(manualJournalID);
            if (status == null) {
                edsManualJournal.setOverallStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.POST)); // for api v1
                manualJournalManager.update(edsManualJournal);
                saveManualJournalTransaction(edsManualJournal);
            } else {
                EdsReference edsReference = referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, status);

                if (!EdsManualJournal.APPROVED.equals(edsReference.getCode())) {
                    edsManualJournal.setEntityStatus(edsReference);

                } else if (EdsManualJournal.APPROVED.equals(edsReference.getCode()) && edsManualJournal.getOverallStatus() != null
                        && EdsManualJournal.DRAFT.equals(edsManualJournal.getOverallStatus().getCode())) {
                    edsManualJournal.setOverallStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.SUBMITTED));
                }
                edsManualJournal.updateStatus(edsReference);
                manualJournalManager.update(edsManualJournal);

                if (EdsManualJournal.POST.equals(edsReference.getCode())) {
                    saveManualJournalTransaction(edsManualJournal);
                }
            }
            allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_MANUAL_JOURNAL, edsManualJournal.getObjectID(), null);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsManualJournal, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_MANUAL_JOURNAL);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private EdsManualTransaction saveManualJournalTransaction(EdsManualJournal manualJournal) {      //save manual transaction and post
        StringBuilder ids = new StringBuilder();
        EdsManualTransaction transaction = transactionManager.getTransactionByManualJournal(manualJournal);

        if (manualJournal.getOverallStatus() != null && EdsManualJournal.POST.equals(manualJournal.getOverallStatus().getCode())) {
            if (transaction != null) {
                if (transaction.getTransactionItems() != null && !transaction.getTransactionItems().isEmpty()) {
                    transaction.getTransactionItems().forEach(item -> ids.append(item.getObjectID()).append(","));
                    transaction.getTransactionItems().clear();
                }
                /*transactionManager.deleteTransactionItems(transaction.getObjectID());*/
            } else {
                transaction = new EdsManualTransaction();    //shu erga keldi new manual transaction
                transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
            }

            transaction.setManualJournal(manualJournal);
            transaction.setJournalDate(manualJournal.getDate());
            transaction.setName("Manual Entry: " + manualJournal.getNarration() + " | " + manualJournal.getNumber());
            transaction.setPostedDate(transactionManager.getUser().getUserDate());
            transaction.setPostedBy(transactionManager.getUser());
            transaction.setDescription(manualJournal.getNarration());
            transaction.setReference(manualJournal.getReference());

            BigDecimal exRate = manualJournal.getExchangeRate();

            Integer calcScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

            HashMap<EdsManualJournalItem, EdsTransactionItem> journalAndTransactionItemsMap = new HashMap<>();
            List<EdsManualJournalItem> items = manualJournal.getItems();

            BigDecimal journalTotalInCurrency = BigDecimal.ZERO;
            BigDecimal debitTotalInBase = BigDecimal.ZERO, creditTotalInBase = BigDecimal.ZERO;
            EdsTransactionItem lastDebitItem = null, lastCreditItem = null;

            boolean isHomeCurrencyAdjustment = manualJournal.isCurrencyAdjustment();

            for (EdsManualJournalItem item : items) {
                EdsTransactionItem transactionItem = new EdsTransactionItem();
                transactionItem.setDescription(item.getDescription());
                transactionItem.setAccount(item.getAccount());
                transactionItem.setCrmAccount(item.getClientOrSupplier());
                transactionItem.setDepartment(item.getDepartment());
                transactionItem.setProject(item.getProject());
                transactionItem.setItemId(item.getObjectID());

                if (item.getDebit() != null) {
                    journalTotalInCurrency = journalTotalInCurrency.add(item.getDebit());

                    BigDecimal debitInBase = item.getDebit().divide(exRate, calcScale, RoundingMode.HALF_UP);
                    debitTotalInBase = debitTotalInBase.add(debitInBase);
                    transactionItem.setDebit(debitInBase);
                    if (transactionItem.getAccount().isForeignAccount()) {
                        transactionItem.setForeignDebit(isHomeCurrencyAdjustment ? BigDecimal.ZERO : item.getDebit());
                    }
                    lastDebitItem = transactionItem;
                }
                if (item.getCredit() != null) {
                    BigDecimal creditInBase = item.getCredit().divide(exRate, calcScale, RoundingMode.HALF_UP);
                    creditTotalInBase = creditTotalInBase.add(creditInBase);
                    transactionItem.setCredit(creditInBase);
                    if (transactionItem.getAccount().isForeignAccount()) {
                        transactionItem.setForeignCredit(isHomeCurrencyAdjustment ? BigDecimal.ZERO : item.getCredit());
                    }
                    lastCreditItem = transactionItem;
                }
                transaction.addTransactionItem(transactionItem);
                journalAndTransactionItemsMap.put(item, transactionItem);
            }

            BigDecimal journalTotalInBase = journalTotalInCurrency.divide(exRate, calcScale, RoundingMode.HALF_UP);

            if (lastDebitItem != null) {
                lastDebitItem.setDebit(lastDebitItem.getDebit().add(journalTotalInBase.subtract(debitTotalInBase)));//Add debit difference to last debit item
            }
            if (lastCreditItem != null) {
                lastCreditItem.setCredit(lastCreditItem.getCredit().add(journalTotalInBase.subtract(creditTotalInBase)));//Add credit difference to last credit item
            }

            transactionManager.createOrUpdate(transaction);

            for (EdsManualJournalItem mji : journalAndTransactionItemsMap.keySet()) {
                mji.setTransactionItem(journalAndTransactionItemsMap.get(mji));
            }

            if (!ids.isEmpty()) {
                transactionManager.deleteMJTransactionItemsByIds(ids.substring(0, ids.length() - 1));
            }
            return transaction;
        } else if (manualJournal.getOverallStatus() != null && EdsManualJournal.DRAFT.equals(manualJournal.getOverallStatus().getCode())) {
            if (transaction != null) {
                transaction.setDeleted(true);
                transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
                transactionManager.update(transaction);
            }
        }
        return null;
    }


    @Override
    @Transactional
    public Integer saveManualJournal(NewManualTransaction manualTransaction) {       //save as a draft
        EdsManualJournal manualJournal;
        Integer transactionID = null;
        EdsUser user = userManager.getUser();
        if (manualTransaction.getObjectId() == null) {
            manualJournal = new EdsManualJournal();
            manualJournal.setCreator(user);

            if (manualTransaction.getCurrency() != null && manualTransaction.getCurrency().getId() != null && manualTransaction.getExchangeRate() != null) {
                EdsCurrency currency = currencyManager.get(manualTransaction.getCurrency().getId());
                exchangeRateHistoryManager.registerExchangeRateHistory(manualTransaction.getExchangeRate(), currency);
            }
        } else {
            manualJournal = manualJournalManager.get(manualTransaction.getObjectId());
            EdsTransaction transaction = transactionManager.getTransactionByManualJournal(manualJournal);
            if (transaction != null) {
                transactionID = transaction.getObjectID();
            }
        }

        if (manualTransaction.getNumber() != null && manualJournalManager.isDuplicateMTNumber(manualTransaction.getNumber(),
                manualJournal.getObjectID(),
                (manualTransaction.getDate() != null ? manualTransaction.getDate().getNonConvertedDate() : null))) {

            BankTransferNumberData numberData = generateManualTransactionMoneyNumber();
            manualTransaction.setNumber(numberData.getNumberString());
            manualTransaction.setNumberData(new NumberData(numberData.getNumberString(), null));
            manualTransaction.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
            //return NewManualTransaction.REFERENCE_EXIST;
        }
        if (!isOk(manualTransaction.getApprovers())) {
            manualJournal.setEntityStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, manualTransaction.getStatus()));
        }
        manualJournalManager.createOrUpdate(manualJournal);

        if (isOk(manualTransaction.getApprovers())) {
            manualTransaction.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : manualTransaction.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (manualJournal.getCurrentApprover() != null && manualTransaction.getStatus() != null && isFirstApprover) {
                        manualJournal.getCurrentApprover().setStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, manualTransaction.getStatus()));
                        manualJournal.setEntityStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.SUBMITTED));
                        isFirstApprover = false;
                    } else if (manualJournal.getCurrentApprover() != null && manualTransaction.getStatus() != null) {
                        manualJournal.getCurrentApprover().setStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.SUBMITTED));
                    }
                    if (manualTransaction.getStatus() != null && !APPROVE.equals(manualTransaction.getStatus())) {
                        manualJournal.setEntityStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, manualTransaction.getStatus()));
                    }
                    if (manualJournal.isCurrentApproverRejected()) {
                        manualJournal.setEntityStatus(manualJournal.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(manualJournal.getObjectID());
                edsApprover.setIs_default(false);
                if (manualTransaction.getStatus() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, manualTransaction.getStatus()));
                    if (Constants.DRAFT.equals(manualTransaction.getStatus())) {
                        manualJournal.setEntityStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, manualTransaction.getStatus()));
                    } else {
                        manualJournal.setEntityStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (manualTransaction.getStatus() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.MANUAL_JOURNAL_STATUS, EdsManualJournal.SUBMITTED));
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                approverManager.createOrUpdate(edsApprover);

                for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (manualJournal.getCurrentApprover() == null) {
                    manualJournal.setCurrentApprover(edsApprover);
                }
                manualJournal.getApprovers().add(edsApprover);
            }
        }
        manualJournal.setReference(manualTransaction.getReference());
        manualJournal.setDate(manualTransaction.getDate().getNonConvertedDate());
        manualJournal.setNarration(manualTransaction.getNarration());
        if (manualTransaction.getProjectId() != null) {
            manualJournal.setProject(projectManager.get(manualTransaction.getProjectId()));
        } else {
            manualJournal.setProject(null);
        }
        if (manualTransaction.getRoleId() != null) {
            manualJournal.setRole(roleManager.get(manualTransaction.getRoleId()));
        } else {
            manualJournal.setRole(null);
        }
        if (manualTransaction.isRecurringTemplate()) {
            manualJournal.setIntNumber(null);
        } else {
            if (manualTransaction.getIntNumber() != null) {
                manualJournal.setIntNumber(manualTransaction.getIntNumber());
            }
            if (manualTransaction.getNumber() != null) {
                manualJournal.setNumber(manualTransaction.getNumber());
            }
        }
        manualJournal.setShowOnCashReports(manualTransaction.isShowOnCashReports());
        manualJournal.setMemorizedTransaction(manualTransaction.isMemorizedTransaction());
        manualJournal.setCurrencyAdjustment(manualTransaction.isCurrencyAdjustment());

        manualJournal.setCurrency(currencyManager.get(manualTransaction.getCurrency().getId()));
        manualJournal.setExchangeRate(manualTransaction.getExchangeRate());
        if (manualTransaction.getPdfTemplateID() != null) {
            manualJournal.setPdfTemplate(companyPdfTemplateManager.get(manualTransaction.getPdfTemplateID()));
        }

        manualJournal.setRecurringTemplate(manualTransaction.isRecurringTemplate());

        StringBuilder ids = new StringBuilder();
        if (manualJournal.getObjectID() != null && !manualJournal.getItems().isEmpty()) {
            manualJournal.getItems().forEach(item -> ids.append(item.getObjectID()).append(","));
            manualJournal.getItems().clear();
            /*manualJournalManager.deleteJournalItems(manualJournal.getObjectID());*/
        }

        NewManualTransactionItem[] items = manualTransaction.getItems();
        for (NewManualTransactionItem item : items) {
            EdsManualJournalItem journalItem = new EdsManualJournalItem();
            journalItem.setDescription(item.getDescription());
            journalItem.setAccount(accountingManager.get(item.getAccountItem().getId()));
            if (item.getTaxItem() != null && item.getTaxItem().getId() != null) {
                journalItem.setTax(vatManager.get(item.getTaxItem().getId()));
            }
            if (item.getCustomerOrSupplier() != null) {
                journalItem.setClientOrSupplier(crmAccountManager.get(item.getCustomerOrSupplier().getId()));
            }
            journalItem.setEmployee(item.getEmployee() != null && item.getEmployee().getId() != null
                    ? employeeManager.get(item.getEmployee().getId())
                    : null);
            if (item.getProject() != null) {
                journalItem.setProject(projectManager.get(item.getProject().getId()));
            }
            journalItem.setDepartment((item.getDepartment() != null && item.getDepartment().getId() != null)
                    ? departmentManager.get(item.getDepartment().getId())
                    : null);
            journalItem.setDebit(item.getDebit());
            journalItem.setCredit(item.getCredit());
            journalItem.setClient((item.getClient() != null && item.getClient().getId() != null)
                    ? crmAccountManager.get(item.getClient().getId())
                    : null);
            journalItem.setManualTransfer(manualJournal);
            if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                journalItem.setManualJournalItemCustomFields(this.createManualJournalItemCustomFields(item.getItemCustomFields()));
            }
            manualJournal.addItem(journalItem);
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsManualJournal.class.getSimpleName());
        if (manualTransaction.getObjectId() == null) {
            manualJournalManager.create(manualJournal);
            if (manualJournal.getOverallStatus() != null && DRAFT.equalsIgnoreCase(manualJournal.getOverallStatus().getCode())) {
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(manualJournal.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Manual transaction saved as draft");
            } else {
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(manualJournal.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Save and post Manual transaction");
            }
            baseEventPostProcessor.registerEvent(ManualJournalEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, manualJournal, user);
            baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.MANUAL_TRANSACTION_ADD, manualJournal, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, manualJournal, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_MANUAL_JOURNAL);
        } else {
            manualJournalManager.update(manualJournal);
            if (!ids.isEmpty()) {
                manualJournalManager.deleteManualJournalItemsByIds(ids.substring(0, ids.length() - 1));
            }
            if (manualJournal.getOverallStatus() != null && DRAFT.equalsIgnoreCase(manualJournal.getOverallStatus().getCode())) {
                kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                kpiLog.setEntityId(manualJournal.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Existing Manual transaction  saved as draft");
            } else {
                kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                kpiLog.setEntityId(manualJournal.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Updated and posted Manual transaction");
            }
            baseEventPostProcessor.registerEvent(ManualJournalEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, manualJournal, user);
            baseEventPostProcessor.registerEvent(ProjectBudgetCustomEventListenerImpl.TYPE, ProjectBudgetCustomEventListenerImpl.MANUAL_TRANSACTION_ADD, manualJournal, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, manualJournal, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_MANUAL_JOURNAL);
        }

        if (manualTransaction.getAttachments() != null && manualTransaction.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_MANUAL_TRANSACTION, manualJournal.getObjectID(), manualJournal.getObjectID(), manualTransaction.getAttachments());
        }

        if (manualJournal.isRecurringTemplate()) {
            saveBillRecurringItem(manualTransaction, manualJournal);

            manualJournal.setSender(manualJournalManager.getUser());

            EdsManualTransaction transaction = transactionManager.getTransactionByManualJournal(manualJournal);
            if (transaction != null) {
                transactionManager.deleteTransaction(transaction.getObjectID());
            }
        } else {
            if (manualTransaction.getObjectId() != null) {
                recurrenceService.deleteRecurrence(manualJournal.getObjectID(), SchedulerConstant.RECURRING_MANUAL_JOURNAL_REMINDER);
            }
            saveManualJournalTransaction(manualJournal);
        }
        createTransferHistoryItems(manualJournal, manualTransaction.getHistoryListItems());

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(),
                manualJournal, user);
        workflowEvent.setEntityType(RelationItem.TYPE_MANUAL_JOURNAL);

        return manualJournal.getObjectID();
    }

    private EdsManualJournalItemCustomFields createManualJournalItemCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            EdsManualJournalItemCustomFields manualJournalItemCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                manualJournalItemCustomFields = manualJournalItemCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                manualJournalItemCustomFields = new EdsManualJournalItemCustomFields();
                manualJournalItemCFManager.create(manualJournalItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(manualJournalItemCustomFields, customFieldItems);
            return manualJournalItemCustomFields;
        }
        return null;
    }

    private void createTransferHistoryItems(EdsManualJournal manualJournal, HistoryListItem[] hisItems) {
        if (manualJournal != null && hisItems != null && hisItems.length > 0) {
            Integer hlen = hisItems.length;
            List<HistoryListItem> notesInDB = getAsHistoryItems(manualJournalNoteManager.getManualJournalNoteByManualJournalId(manualJournal.getObjectID()));
            if (notesInDB != null && !notesInDB.isEmpty()) {
                for (HistoryListItem aNotesInDB : notesInDB) {
                    boolean keepInDB = false;
                    for (HistoryListItem hisItem : hisItems) {
                        if (aNotesInDB.getObjectID() == hisItem.getObjectID()) {
                            keepInDB = true;
                            break;
                        }
                    }
                    if (!keepInDB) {
                        deleteManualJournalNote(aNotesInDB.getObjectID());
                    }
                }
            }
            for (HistoryListItem hisItem : hisItems) {
                if (hisItem.getObjectID() == null) {
                    EdsManualJournalNote edsManualJournalNote = new EdsManualJournalNote();
                    edsManualJournalNote.setComment(hisItem.getComment());
                    edsManualJournalNote.setCommentator(userManager.getUser());
                    edsManualJournalNote.setManualJournal(manualJournal);
                    edsManualJournalNote.setDate(new Date());
                    edsManualJournalNote.setSuperUser(ServerUtils.isSuperUser());
                    manualJournalNoteManager.create(edsManualJournalNote);
                }
            }
        }
    }

    private List<HistoryListItem> getAsHistoryItems(List<EdsManualJournalNote> btNotes) {
        List<HistoryListItem> hisItems = new ArrayList<>();
        if (btNotes == null || btNotes.isEmpty()) {
            return hisItems;
        }
        for (EdsManualJournalNote note : btNotes) {
            HistoryListItem hisItem = new HistoryListItem();
            hisItem.setObjectID(note.getObjectID());
            hisItem.setComment(note.getComment());
            if (note.isSuperUser()) {
                hisItem.setEmployee(Constants.defaultSupportName);
            } else {
                hisItem.setEmployee(note.getCommentator().getFullName());
            }
            hisItem.setEmployeeID(note.getCommentator().getObjectID());
            hisItem.setEventDate(note.getDate());
            hisItems.add(hisItem);
        }
        return hisItems;
    }

    public Integer createManualJournalNote(Integer transferID, HistoryListItem hisItem) {
        if (transferID != null && hisItem != null) {
            EdsManualJournal edsManualJournal = manualJournalManager.get(transferID);
            EdsManualJournalNote manualJournalNote = new EdsManualJournalNote();
            manualJournalNote.setManualJournal(edsManualJournal);
            manualJournalNote.setComment(hisItem.getComment());
            manualJournalNote.setDate(new Date());
            manualJournalNote.setCommentator(userManager.getUser());
            manualJournalNote.setSuperUser(ServerUtils.isSuperUser());
            manualJournalNoteManager.create(manualJournalNote);
            return manualJournalNote.getObjectID();
        }
        return null;
    }

    public Boolean deleteManualJournalNote(Integer noteID) {
        if (noteID != null) {
            EdsManualJournalNote manualJournalNote = manualJournalNoteManager.get(noteID);
            manualJournalNoteManager.delete(manualJournalNote);
            return true;
        }
        return false;
    }

    private Integer saveBillRecurringItem(NewManualTransaction manualTransaction, EdsManualJournal manualJournal) {
        EdsCompany company = manualJournalManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_MANUAL_JOURNAL_REMINDER, manualJournal.getObjectID(), company.getObjectID());
        manualTransaction.getRecurrenceJobItem().setObjectId(recurrence != null ? recurrence.getObjectID() : null);
        manualTransaction.getRecurrenceJobItem().setBusObjectId(manualJournal.getObjectID());
        manualTransaction.getRecurrenceJobItem().setJobType(SchedulerConstant.RECURRING_MANUAL_JOURNAL_REMINDER);
        return recurrenceService.saveRecurrenceJob(manualTransaction.getRecurrenceJobItem());
    }

    @Override
    @Transactional
    public synchronized Integer createManualJournalFromRecurringJob(Integer recurrencyID, Integer recurringManualJournalID) {

        EdsManualJournal edsManualJournal = manualJournalManager.get(recurringManualJournalID);
        if (edsManualJournal == null || edsManualJournal.isDeleted()) {
            EdsRecurrence recurrence = recurrenceManager.get(recurrencyID);
            recurrence.setChanged(true);
            recurrence.setDeleted(true);
            recurrenceManager.update(recurrence);
            return edsManualJournal != null ? edsManualJournal.getObjectID() : -1;
        }
        EdsUser edsSender = edsManualJournal.getSender();
        ServerSecurityContext.getInstance().setStaticUserID(edsSender.getObjectID());

        NewManualTransaction newManualTransaction = getManualJournal(recurringManualJournalID);
        newManualTransaction.setObjectId(null);
        newManualTransaction.setRecurringTemplate(false);
        newManualTransaction.setRecurrenceJobItem(null);
        newManualTransaction.setRecurringTemplateId(recurringManualJournalID);
        newManualTransaction.setTransferNumberData(generateManualTransactionMoneyNumber());
        if (newManualTransaction.getTransferNumberData().isWithDate()) {
            String dateString = ServerUtils.getBankTransferDateNumber(edsManualJournal.getDate());
            newManualTransaction.getTransferNumberData().setDate(dateString);
        }
        newManualTransaction.setNumber(newManualTransaction.getTransferNumberData().getTransferNumber());
        newManualTransaction.setIntNumber(Integer.valueOf(newManualTransaction.getTransferNumberData().getFourDigitNumber()));

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(edsSender.getUserDate());
        ServerUtils.setBeginningOfTheDay(calendar);
        newManualTransaction.setDate(new DateNonConvertable(calendar.getTime()));

        return saveManualJournal(newManualTransaction);
    }

    @Override
    public void insertcustomDataToManualJournal(FindEncodeInputStream inputStream) {
        try {
            HashMap<String, EdsAccount> accountMap = accountingManager.getAccountAsMap(new ListingFilterParameter());
            CurrencyItem companyBaseCurrency = currencyService.getCompanyBaseCurrency();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            ArrayList<NewManualTransactionItem> items = new ArrayList<>();
            NewManualTransaction manualTransaction = null;
            int i = 0;
            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) break;
                Cell dateCell = null;
                try {
                    dateCell = row.getCell(0);
                } catch (Exception e) {
                    break;
                }
                Cell accountCell = row.getCell(1), itemDecriptionCell = row.getCell(2), narationCell = row.getCell(3),
                        referenceCell = row.getCell(4), debitCell = row.getCell(5), creditCell = row.getCell(6);
                Date date;
                double debit, credit;
                try {
                    date = dateCell.getDateCellValue();
                } catch (Exception e) {
                    date = null;
                }
                try {
                    debit = debitCell.getNumericCellValue();
                } catch (Exception e) {
                    debit = 0d;
                }
                try {
                    credit = creditCell.getNumericCellValue();
                } catch (Exception e) {
                    credit = 0d;
                }

                if (debit == 0d && credit == 0d) {
                    continue;
                }
                if (date != null) {
                    if (manualTransaction != null && !items.isEmpty()) {
                        manualTransaction.setItems(items.toArray(new NewManualTransactionItem[0]));
                        saveManualJournal(manualTransaction);
                    }
                    manualTransaction = new NewManualTransaction();
                    items = new ArrayList<>();
                    manualTransaction.setDate(new DateNonConvertable(date));
                    try {
                        manualTransaction.setNarration(narationCell.getStringCellValue());
                    } catch (Exception ignored) {

                    }
                    try {
                        manualTransaction.setReference(referenceCell.getStringCellValue());
                    } catch (Exception ignored) {

                    }
                    manualTransaction.setCurrency(companyBaseCurrency);
                    manualTransaction.setExchangeRate(BigDecimal.ONE);
                    manualTransaction.setStatus(POST);
                    manualTransaction.setMemorizedTransaction(false);

                }
                String accountName = null;
                try {
                    accountName = accountCell.getStringCellValue();
                } catch (Exception e) {
                    continue;
                }
                EdsAccount account = accountMap.get(accountName);
                if (account == null) {
                    continue;
                }
                NewManualTransactionItem item = new NewManualTransactionItem();
                item.setAccountItem(account.createAccountItem());
                try {
                    item.setDescription(itemDecriptionCell.getStringCellValue());
                } catch (Exception ignored) {

                }
                item.setDebit(new BigDecimal(debit));
                item.setCredit(new BigDecimal(credit));
                items.add(item);
                System.out.println("qator - " + i++);
            }
            if (manualTransaction != null && !items.isEmpty()) {
                manualTransaction.setItems(items.toArray(new NewManualTransactionItem[0]));
                saveManualJournal(manualTransaction);
            }
        } catch (IOException | InvalidFormatException ignored) {

        }
    }
}
