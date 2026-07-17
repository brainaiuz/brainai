package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectBudget;
import com.edatasite.workforce.core.domain.EdsProjectBudgetItem;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransferItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournalItem;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectBudgetManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Normurod on 9/12/14.
 */
@Transactional
public class ProjectBudgetCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsObject> TYPE = new WfmType<>(EventTypes.projectBudgetCustomEventListener);

    public static final String EXPENSE_REPORT_SUBMIT = "EXPENSE_REPORT_SUBMIT";
    public static final String EXPENSE_REPORT_DECLINE = "EXPENSE_REPORT_DECLINE";
    public static final String SALE_QUOTE_APPROVE = "SALE_QUOTE_APPROVE";
    public static final String SALE_QUOTE_AVOID = "SALE_QUOTE_AVOID";
    public static final String PURCHASE_ORDER_APPROVE = "PURCHASE_ORDER_APPROVE";
    public static final String PURCHASE_ORDER_AVOID = "PURCHASE_ORDER_AVOID";
    public static final String SALE_INVOICE_APPROVE = "SALE_INVOICE_APPROVE";
    public static final String PURCHASE_INVOICE_APPROVE = "PURCHASE_INVOICE_APPROVE";
    public static final String BANK_TRANSFER_ADD = "BANK_TRANSFER_ADD";
    public static final String MANUAL_TRANSACTION_ADD = "MANUAL_TRANSACTION_ADD";

    private Map<String, EdsProjectBudgetItem> map = null;

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private ExpenseReportManager expenseReportManager;

    @Autowired
    private ProjectBudgetManager projectBudgetManager;

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Autowired
    private QuoteManager quoteManager;

    @Autowired
    private InvoiceManager invoiceManager;

    @Autowired
    private AccountingManager accountingManager;

    @Autowired
    private ManualJournalManager manualJournalManager;
    @Autowired
    private SpendReceiveMoneyManager spendReceiveMoneyManager;

    private boolean isProjectInLineItem;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        isProjectInLineItem = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        EdsProjectBudget projectBudget = event.getCustomStringField() != null && !event.getCustomStringField().isEmpty() ? getProjectBudget(Integer.parseInt(event.getCustomStringField())) : null;

        if (EXPENSE_REPORT_SUBMIT.equals(event.getEventType()) || EXPENSE_REPORT_DECLINE.equals(event.getEventType())) {
            EdsExpenseReport expenseReport = expenseReportManager.get(event.getEntityID());

            Calendar reportDate = ServerUtils.getDateAsCalendar(expenseReport.getStartDate());
            Integer month = reportDate.get(Calendar.MONTH) + 1;
            Integer year = reportDate.get(Calendar.YEAR);
            map = new HashMap<>();

            if (projectBudget == null && expenseReport.getProject() != null) {
                projectBudget = new EdsProjectBudget();
                projectBudget.setProject(expenseReport.getProject());
                projectBudgetManager.create(projectBudget);
            }

            for (EdsExpense expense : expenseReport.getExpenses()) {
                if (EXPENSE_REPORT_SUBMIT.equals(event.getEventType())) {
                    updateProjectBudgetByExpense(projectBudget, expense, month, year, true);
                } else {
                    updateProjectBudgetByExpense(projectBudget, expense, month, year, false);
                }
            }

            if (!map.isEmpty()) {
                saveBudgetItems("EXPENSE", map);
            }

        } else if (SALE_QUOTE_APPROVE.equals(event.getEventType()) || SALE_QUOTE_AVOID.equals(event.getEventType())) {
            EdsSaleQuote quote = quoteManager.getSaleQuote(event.getEntityID());

            Calendar reportDate = ServerUtils.getDateAsCalendar(quote.getInvoiceDate());
            Integer month = reportDate.get(Calendar.MONTH) + 1;
            Integer year = reportDate.get(Calendar.YEAR);
            map = new HashMap<>();

            if (projectBudget == null && quote.getRelatedProject() != null) {
                projectBudget = new EdsProjectBudget();
                projectBudget.setProject(quote.getRelatedProject());
                projectBudgetManager.create(projectBudget);
            }

            for (EdsQuoteItem item : quote.getQuoteItems()) {
                if (SALE_QUOTE_APPROVE.equals(event.getEventType())) {
                    updateProjectBudgetBySQ(projectBudget, item, month, year, true);
                } else {
                    updateProjectBudgetBySQ(projectBudget, item, month, year, false);
                }
            }
            if (quote.getTotalTaxes() != null && quote.getTotalTaxes().compareTo(BigDecimal.ZERO) > 0) {
                EdsQuoteItem item = new EdsQuoteItem();
                item.setAccount(accountingManager.getVatAccount(Constants.RECEIVABLE));
                item.setNet(quote.getTotalTaxes());
                updateProjectBudgetBySQ(projectBudget, item, month, year, SALE_QUOTE_APPROVE.equals(event.getEventType()));
            }
            saveBudgetItems("REVENUE", map);

        } else if (PURCHASE_ORDER_APPROVE.equals(event.getEventType()) || PURCHASE_ORDER_AVOID.equals(event.getEventType())) {
            EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(event.getEntityID());

            Calendar reportDate = ServerUtils.getDateAsCalendar(purchaseOrder.getInvoiceDate());
            Integer month = reportDate.get(Calendar.MONTH) + 1;
            Integer year = reportDate.get(Calendar.YEAR);
            map = new HashMap<>();

            if (projectBudget == null && purchaseOrder.getRelatedProject() != null) {
                projectBudget = new EdsProjectBudget();
                projectBudget.setProject(purchaseOrder.getRelatedProject());
                projectBudgetManager.create(projectBudget);
            }

            for (EdsQuoteItem item : purchaseOrder.getQuoteItems()) {
                if (PURCHASE_ORDER_APPROVE.equals(event.getEventType())) {
                    updateProjectBudgetByPO(projectBudget, item, month, year, true);
                } else {
                    updateProjectBudgetByPO(projectBudget, item, month, year, false);
                }
            }

            if (purchaseOrder.getTotalTaxes() != null && purchaseOrder.getTotalTaxes().compareTo(BigDecimal.ZERO) > 0) {
                EdsQuoteItem item = new EdsQuoteItem();
                item.setAccount(accountingManager.getVatAccount(Constants.PAYABLE));
                item.setNet(purchaseOrder.getTotalTaxes());
                updateProjectBudgetByPO(projectBudget, item, month, year, PURCHASE_ORDER_APPROVE.equals(event.getEventType()));
            }

            saveBudgetItems("PURCHASE", map);

        } else if (PURCHASE_INVOICE_APPROVE.equals(event.getEventType())) {
            EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(event.getEntityID());

            Calendar reportDate = ServerUtils.getDateAsCalendar(purchaseInvoice.getInvoiceDate());
            Integer month = reportDate.get(Calendar.MONTH) + 1;
            Integer year = reportDate.get(Calendar.YEAR);
            map = new HashMap<>();

            if (projectBudget == null && purchaseInvoice.getRelatedProject() != null) {
                projectBudget = new EdsProjectBudget();
                projectBudget.setProject(purchaseInvoice.getRelatedProject());
                projectBudgetManager.create(projectBudget);
            }
            updateProjectBudgetByPI(projectBudget, purchaseInvoice.getInvoiceItems(), month, year);
        } else if (SALE_INVOICE_APPROVE.equals(event.getEventType())) {
            EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(event.getEntityID());

            Calendar reportDate = ServerUtils.getDateAsCalendar(saleInvoice.getInvoiceDate());
            Integer month = reportDate.get(Calendar.MONTH) + 1;
            Integer year = reportDate.get(Calendar.YEAR);
            map = new HashMap<>();


            if (projectBudget == null && saleInvoice.getRelatedProject() != null) {
                projectBudget = new EdsProjectBudget();
                projectBudget.setProject(saleInvoice.getRelatedProject());
                projectBudgetManager.create(projectBudget);
            }

            updateProjectBudgetBySI(projectBudget, saleInvoice.getInvoiceItems(), month, year);
        } else if (BANK_TRANSFER_ADD.equals(event.getEventType())) {
            EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(event.getEntityID());
            Calendar reportDate = ServerUtils.getDateAsCalendar(bankTransfer.getDate());
            Integer month = reportDate.get(Calendar.MONTH) + 1;
            Integer year = reportDate.get(Calendar.YEAR);
            map = new HashMap<>();

            List<EdsBankTransferItem> transferItems = new ArrayList<>();
            if (bankTransfer.getItems() != null) {
                if (isProjectInLineItem) {
                    for (EdsBankTransferItem item : bankTransfer.getItems()) {
                        if (item.getProject() != null) {
                            transferItems.add(item);
                        }
                    }
                } else {
                    transferItems.addAll(bankTransfer.getItems());
                }
            }

            updateProjectBudgetByBankTransfer(bankTransfer, transferItems, month, year);
        } else if (MANUAL_TRANSACTION_ADD.equals(event.getEventType())) {
            EdsManualJournal manualJournal = manualJournalManager.get(event.getEntityID());
            List<EdsManualJournalItem> journalItems = new ArrayList<>();
            if (manualJournal.getItems() != null) {
                for (EdsManualJournalItem item : manualJournal.getItems()) {
                    if (item.getProject() != null) {
                        journalItems.add(item);
                    }
                }
            }

            if (!journalItems.isEmpty()) {
                Calendar reportDate = ServerUtils.getDateAsCalendar(manualJournal.getDate());
                Integer month = reportDate.get(Calendar.MONTH) + 1;
                Integer year = reportDate.get(Calendar.YEAR);
                map = new HashMap<>();

                updateProjectBudgetByManualTransaction(journalItems, month, year);
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void updateProjectBudgetByExpense(EdsProjectBudget projectBudget, EdsExpense expense, Integer month, Integer year, boolean isSubmitted) {
        if (isProjectInLineItem && expense.getProject() != null) {
            projectBudget = getProjectBudget(expense.getProject().getObjectID());
        }

        //if project wasn't set to the Expense Report or its items
        if (projectBudget == null) {
            return;
        }
        String mapKey = projectBudget.getObjectID() + "_EXPENSE_" + expense.getAccount().getObjectID() + "_" + month + "_" + year;
        EdsProjectBudgetItem budgetItem;

        if (map.get(mapKey) != null) {
            budgetItem = map.get(mapKey);
        } else {
            budgetItem = projectBudgetManager.getBudgetItem(projectBudget.getObjectID(), "EXPENSE", expense.getAccount().getObjectID(), month, year);
        }

        if (budgetItem == null && !isSubmitted) {
            return;
        }
        budgetItem = ServerUtils.getEdsProjectBudgetItem(projectBudget, budgetItem, expense.getBaseSubtotal(), "EXPENSE");
        if (budgetItem != null) {
            BigDecimal amount = budgetItem.getAmount();
            if (isSubmitted) { //if expense report was submitted
                if (Constants.EXPENSE_SUBMITTED.equals(expense.getReport().getStatus().getCode())) {
                    amount = amount.add(expense.getBaseSubtotal());
                }
            } else if (budgetItem.getAmount().doubleValue() >= expense.getBaseSubtotal().doubleValue()) {//if expense report was declined or reversed
                amount = amount.subtract(expense.getBaseSubtotal());
            }
            budgetItem.setAmount(amount);
        }

        budgetItem.setAccount(expense.getAccount());
        budgetItem.setMonth(month);
        budgetItem.setYear(year);
        budgetItem.setTotal(Boolean.FALSE);

        map.put(mapKey, budgetItem);
    }

    private void updateProjectBudgetBySQ(EdsProjectBudget projectBudget, EdsQuoteItem item, Integer month, Integer year, boolean isApproved) {

        if (isProjectInLineItem && item.getProject() != null) {
            projectBudget = getProjectBudget(item.getProject().getObjectID());
        }


        //if project wasn't set to the Quote or its items
        if (projectBudget == null) {
            return;
        }

        String mapKey = projectBudget.getObjectID() + "_REVENUE_" + item.getAccount().getObjectID() + "_" + month + "_" + year;
        EdsProjectBudgetItem budgetItem;

        if (map.get(mapKey) != null) {
            budgetItem = map.get(mapKey);
        } else {
            budgetItem = projectBudgetManager.getBudgetItem(projectBudget.getObjectID(), "REVENUE", item.getAccount().getObjectID(), month, year);
        }

        if (budgetItem == null && !isApproved) {
            return;
        }

        if (budgetItem == null) {
            budgetItem = new EdsProjectBudgetItem();
            budgetItem.setProjectBudget(projectBudget);
            budgetItem.setAmount(item.getNet());
            budgetItem.setTotal(Boolean.FALSE);
            budgetItem.setType("REVENUE");
        } else {
            BigDecimal amount = BigDecimal.ZERO;
            if (isApproved) { //if sale quote was approved
                amount = budgetItem.getAmount().add(item.getNet());
            } else if (budgetItem.getAmount().doubleValue() >= item.getNet().doubleValue()) {//if salequote was declined or reversed
                amount = budgetItem.getAmount().subtract(item.getNet());
            }
            budgetItem.setAmount(amount);
        }

        budgetItem.setAccount(item.getAccount());
        budgetItem.setMonth(month);
        budgetItem.setYear(year);
        budgetItem.setTotal(Boolean.FALSE);

        map.put(mapKey, budgetItem);
    }

    private void updateProjectBudgetByPO(EdsProjectBudget projectBudget, EdsQuoteItem item, Integer month, Integer year, boolean isApproved) {
        boolean purchaseEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_BUDGET_DETAILED_PURCHASE_ENABLED);

        if (isProjectInLineItem && item.getProject() != null) {
            projectBudget = getProjectBudget(item.getProject().getObjectID());
        }

        //if project wasn't set to the Purchase Order or its items
        if (projectBudget == null) {
            return;
        }

        String mapKey = projectBudget.getObjectID() + "_PURCHASE_" + (purchaseEnabled ? item.getAccount().getObjectID() : "") + "_" + month + "_" + year;
        EdsProjectBudgetItem budgetItem;

        if (map.get(mapKey) != null) {
            budgetItem = map.get(mapKey);
        } else {
            budgetItem = projectBudgetManager.getBudgetItem(projectBudget.getObjectID(), "PURCHASE", purchaseEnabled ? item.getAccount().getObjectID() : null, month, year);
        }

        if (budgetItem == null && !isApproved) {
            return;
        }

        budgetItem = ServerUtils.getEdsProjectBudgetItem(projectBudget, budgetItem, item.getNet(), "PURCHASE");
        if (budgetItem != null) {
            BigDecimal amount = BigDecimal.ZERO;

            if (isApproved) { //if sale quote was approved
                amount = budgetItem.getAmount().add(item.getNet());
            } else if (budgetItem.getAmount().doubleValue() >= item.getNet().doubleValue()) {//if salequote was declined or reversed
                amount = budgetItem.getAmount().subtract(item.getNet());
            }
            budgetItem.setAmount(amount);
        }

        budgetItem.setAccount(purchaseEnabled ? item.getAccount() : null);
        budgetItem.setMonth(month);
        budgetItem.setYear(year);
        budgetItem.setTotal(Boolean.FALSE);

        map.put(mapKey, budgetItem);
    }

    private void updateProjectBudgetByPI(EdsProjectBudget projectBudget, List<EdsInvoiceItem> items, Integer month, Integer year) {
        boolean purchaseEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_BUDGET_DETAILED_PURCHASE_ENABLED);

        if (!purchaseEnabled) {
            return;
        }

        for (EdsInvoiceItem item : items) {
            if (isProjectInLineItem && item.getProject() != null) {
                projectBudget = getProjectBudget(item.getProject().getObjectID());
            }

            //if project wasn't set to the Purchase Invoice or its items
            if (projectBudget == null) {
                return;
            }
            String mapKey = projectBudget.getObjectID() + "_PURCHASE_" + (purchaseEnabled ? item.getAccount().getObjectID() : "") + "_" + month + "_" + year;
            EdsProjectBudgetItem budgetItem = null;

            if (map.get(mapKey) != null) {
                budgetItem = map.get(mapKey);
            } else {
                budgetItem = projectBudgetManager.getBudgetItem(projectBudget.getObjectID(), "PURCHASE", purchaseEnabled ? item.getAccount().getObjectID() : null, month, year);
            }
            budgetItem = ServerUtils.getEdsProjectBudgetItem(projectBudget, budgetItem, item.getNet(), "PURCHASE");
            budgetItem.setAccount(purchaseEnabled ? item.getAccount() : null);
            budgetItem.setMonth(month);
            budgetItem.setYear(year);
            budgetItem.setTotal(Boolean.FALSE);

            map.put(mapKey, budgetItem);
        }

        saveBudgetItems("PURCHASE", map);
    }

    private void updateProjectBudgetBySI(EdsProjectBudget projectBudget, List<EdsInvoiceItem> items, Integer month, Integer year) {
        for (EdsInvoiceItem item : items) {

            if (isProjectInLineItem && item.getProject() != null) {
                projectBudget = getProjectBudget(item.getProject().getObjectID());
            }

            //if project wasn't set to the Invoice or its items
            if (projectBudget == null) {
                return;
            }
            String mapKey = projectBudget.getObjectID() + "_REVENUE_" + item.getAccount().getObjectID() + "_" + month + "_" + year;
            EdsProjectBudgetItem budgetItem;

            if (map.get(mapKey) != null) {
                budgetItem = map.get(mapKey);
            } else {
                budgetItem = projectBudgetManager.getBudgetItem(projectBudget.getObjectID(), "REVENUE", item.getAccount().getObjectID(), month, year);
            }

            budgetItem = ServerUtils.getEdsProjectBudgetItem(projectBudget, budgetItem, item.getNet(), "REVENUE");
            budgetItem.setAccount(item.getAccount());
            budgetItem.setMonth(month);
            budgetItem.setYear(year);
            budgetItem.setTotal(Boolean.FALSE);

            map.put(mapKey, budgetItem);
        }

        saveBudgetItems("REVENUE", map);

    }

    private void updateProjectBudgetByBankTransfer(EdsBankTransfer bankTransfer, List<EdsBankTransferItem> items, Integer month, Integer year) {
        String category = "";
        EdsProjectBudget projectBudget = null;
        if (!isProjectInLineItem && bankTransfer.getProject() != null) {
            projectBudget = getProjectBudget(bankTransfer.getProject().getObjectID());
        }
        for (EdsBankTransferItem transferItem : items) {
            if (isProjectInLineItem && transferItem.getProject() != null) {
                projectBudget = getProjectBudget(transferItem.getProject().getObjectID());
            }
            if (projectBudget == null && transferItem.getProject() != null) {
                projectBudget = new EdsProjectBudget();
                projectBudget.setProject(transferItem.getProject());
                projectBudgetManager.create(projectBudget);
            }
            String mapKey = bankTransfer.getObjectID() + "_BANK_TRANSFER_" + transferItem.getAccount().getObjectID() + "_" + month + "_" + year;
            EdsProjectBudgetItem budgetItem;

            if ("EXPENSES".equals(transferItem.getAccount().getAccountType().getCategory())) {
                category = EdsProjectBudgetItem.EXPENSE;
            } else if ("ASSETS".equals(transferItem.getAccount().getAccountType().getCategory())) {
                category = EdsProjectBudgetItem.ASSET;
            } else category = transferItem.getAccount().getAccountType().getCategory();

            if (map.get(mapKey) != null) {
                budgetItem = map.get(mapKey);
            } else {
                budgetItem = projectBudgetManager.getBudgetItem(bankTransfer.getObjectID(), category, transferItem.getAccount().getObjectID(), month, year);
            }
            budgetItem = ServerUtils.getEdsProjectBudgetItem(projectBudget, budgetItem, transferItem.getAmount(), category);
            budgetItem.setAccount(transferItem.getAccount());
            budgetItem.setMonth(month);
            budgetItem.setYear(year);
            projectBudgetManager.persist(budgetItem);
//            map.put(mapKey, budgetItem);
        }
//        saveBudgetItems(category, map);
    }

    private void updateProjectBudgetByManualTransaction(List<EdsManualJournalItem> items, Integer month, Integer year) {
        String category = "";
        for (EdsManualJournalItem journalItem : items) {
            if (journalItem.getProject() != null) {
                EdsProject project = projectManager.get(journalItem.getProject().getObjectID());
                if (project != null) {
                    EdsProjectBudget projectBudget = projectBudgetManager.getBudgetByProject(project.getObjectID());
                    if (projectBudget == null) {
                        projectBudget = new EdsProjectBudget();
                        projectBudget.setProject(project);
                        projectBudgetManager.create(projectBudget);
                    }
                    String mapKey = projectBudget.getObjectID() + "_MANUAL_TRANSACTION_" + journalItem.getAccount().getObjectID() + "_" + month + "_" + year;
                    EdsProjectBudgetItem budgetItem = null;

                    if ("EXPENSES".equals(journalItem.getAccount().getAccountType().getCategory())) {
                        category = EdsProjectBudgetItem.EXPENSE;
                    } else if ("ASSETS".equals(journalItem.getAccount().getAccountType().getCategory())) {
                        category = EdsProjectBudgetItem.ASSET;
                    } else category = journalItem.getAccount().getAccountType().getCategory();

                    if (map.get(mapKey) != null) {
                        budgetItem = map.get(mapKey);
                    } else {
                        budgetItem = projectBudgetManager.getBudgetItem(projectBudget.getObjectID(), category, journalItem.getAccount().getObjectID(), month, year);
                    }
                    budgetItem = ServerUtils.getEdsProjectBudgetItem(projectBudget, budgetItem, journalItem.getDebit(), category);
                    budgetItem.setAccount(journalItem.getAccount());
                    budgetItem.setMonth(month);
                    budgetItem.setYear(year);
                    budgetItem.setTotal(Boolean.FALSE);
                    projectBudgetManager.persist(budgetItem);
//                    map.put(mapKey, budgetItem);
                }
            }
        }
//        saveBudgetItems(category, map);
    }

    private void saveBudgetItems(String type, Map<String, EdsProjectBudgetItem> map) {
        if (map != null && !map.values().isEmpty()) {
            for (EdsProjectBudgetItem budgetItem : map.values()) {

                if (budgetItem.getObjectID() == null) {
                    projectBudgetManager.persist(budgetItem);
                } else {
                    projectBudgetManager.merge(budgetItem);
                }

                BigDecimal totalBudget = BigDecimal.ZERO;
                List<EdsProjectBudgetItem> list = projectBudgetManager.getBudgetItems(budgetItem.getProjectBudget().getObjectID(), type, budgetItem.getAccount() != null ? budgetItem.getAccount().getObjectID() : null);

                if (list != null && !list.isEmpty()) {
                    for (EdsProjectBudgetItem bi : list) {
                        totalBudget = totalBudget.add(bi.getAmount());
                    }
                }

                EdsProjectBudgetItem totalProjectBudgetItem = projectBudgetManager.getTotalBudgetItem(budgetItem.getProjectBudget().getObjectID(), type, budgetItem.getAccount() != null ? budgetItem.getAccount().getObjectID() : null);

                if (totalProjectBudgetItem == null) {
                    totalProjectBudgetItem = new EdsProjectBudgetItem();
                    totalProjectBudgetItem.setProjectBudget(budgetItem.getProjectBudget());
                    totalProjectBudgetItem.setType(type);
                    totalProjectBudgetItem.setTotal(Boolean.TRUE);
                    totalProjectBudgetItem.setAccount(budgetItem.getAccount());
                }

                totalProjectBudgetItem.setAmount(totalBudget != null ? totalBudget : budgetItem.getAmount());

                if (totalProjectBudgetItem.getObjectID() == null) {
                    projectBudgetManager.persist(totalProjectBudgetItem);
                } else {
                    projectBudgetManager.merge(totalProjectBudgetItem);
                }
            }
        }
    }

    private EdsProjectBudget getProjectBudget(Integer projectID) {
//        Integer projectID = Integer.parseInt(event.getCustomStringField());

        EdsProjectBudget projectBudget = projectBudgetManager.getBudgetByProject(projectID);

        if (projectBudget == null) {
            projectBudget = new EdsProjectBudget();
            projectBudget.setProject(projectManager.get(projectID));
            projectBudgetManager.create(projectBudget);
        }

        return projectBudget;
    }
}
