package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal.JournalTable;
import com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal.JournalTableColumn;
import com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal.JournalTableItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudget;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 8/3/11
 * Time: 11:49 AM
 */
public class ProjectBudgetSheetSpecificView extends View implements Constants {

    private final NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private final NumberFormat percentageFormat = NumberFormat.getFormat(",##0.00");

    private static final String KEY = "BUDGETSHEETSPECIFICVIEW";

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final Integer projectId;
    private JournalTable table;
    private boolean isWageRate = true;
    private final boolean isFirst = true;
    private ProjectBudget projectBudgetWageRate;
    private ProjectBudget projectBudgetClientCharge;
    private WfmButton2 btnUpdate;
    private final boolean isAccountingSetup = "true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP));
    private final KpiRadioButton clientChargeRate = new KpiRadioButton("rate", wfmStrings.clientChargeRate());
    private KpiRadioButton wageRate = new KpiRadioButton("rate", wfmStrings.wageRate());
    private JournalTableColumn[] columns;
    private SimpleLink actionEmployee;
    private String link;
    private WfmButton2 pdfVersionButton;
    private WfmButton2 excelVersionButton;
    private boolean hasAccessToChange = true;
    private KpiCheckBox withTax;

    public ProjectBudgetSheetSpecificView(Integer projectId, boolean hasAccessToChange) {
        super("projectBudgetSpecific", projectStrings.budgetSheet());
        this.projectId = projectId;
        this.hasAccessToChange = hasAccessToChange;
    }

    @Override
    protected Widget onInitialize() {
        isWageRate = !Utils.hasGenericAccess(GenericSettingsEnum.IS_DISABLED_WAGE_RATE);
        HorizontalPanel headerPanel = new HorizontalPanel();
        headerPanel.setWidth("100%");

        GBox headerBoxPanel = new GBox();
        headerBoxPanel.addStyleName("group-box--width-free");
        GBoxRow groupBoxRow = new GBoxRow();

        btnUpdate = new WfmButton2(wfmStrings.update());
        btnUpdate.addClickHandler(clickEvent -> updateBudget());
        pdfVersionButton = new WfmButton2(wfmStrings.pdfVersion());
        pdfVersionButton.addClickHandler(clickEvent -> generatePDF(headerPanel));
        excelVersionButton = new WfmButton2(wfmStrings.excelVersion());
        excelVersionButton.addClickHandler(clickEvent -> generateExcel(headerPanel));

        withTax = new KpiCheckBox(projectStrings.withTax());
        withTax.ensureDebugId("projectBudget_" + "withTax");
        withTax.addValueChangeHandler(valueChangeEvent -> {
            if (projectBudgetWageRate != null) {
                built();
            }
        });

        clientChargeRate.addClickHandler(clickEvent -> {
            isWageRate = false;
            refreshEmployeeCost();
            built();
        });

        GBoxItem withTaxItem = new GBoxItem(withTax);
        withTaxItem.setStyleNoBorder(true);
        withTaxItem.getgBoxItemLabel().removeFromParent();
        groupBoxRow.add(withTaxItem);

        GBoxItem updateButtonItem = new GBoxItem(btnUpdate);
        updateButtonItem.setStyleNoBorder(true);
        updateButtonItem.getgBoxItemLabel().removeFromParent();
        groupBoxRow.add(updateButtonItem);

        GBoxItem pdfButtonItem = new GBoxItem(pdfVersionButton);
        pdfButtonItem.setStyleNoBorder(true);
        pdfButtonItem.getgBoxItemLabel().removeFromParent();
        groupBoxRow.add(pdfButtonItem);

        GBoxItem excelButtonItem = new GBoxItem(excelVersionButton);
        excelButtonItem.setStyleNoBorder(true);
        excelButtonItem.getgBoxItemLabel().removeFromParent();
        groupBoxRow.add(excelButtonItem);

        headerBoxPanel.add(groupBoxRow);

        columns = new JournalTableColumn[5];
        columns[0] = new JournalTableColumn("title", "");
        columns[0].setWidthPercentage(34);

        columns[1] = new JournalTableColumn("planned", projectStrings.plannedAmount());
        columns[1].setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        columns[1].setWidthPercentage(16.5);

        columns[2] = new JournalTableColumn("actual", projectStrings.actualAmount());
        columns[2].setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        columns[2].setWidthPercentage(16.5);

        columns[3] = new JournalTableColumn("perCentVariance", projectStrings.variancePercent());
        columns[3].setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        columns[3].setWidthPercentage(16.5);

        columns[4] = new JournalTableColumn("varianceCost", projectStrings.variancecost());
        columns[4].setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        columns[4].setWidthPercentage(16.5);

        MaterialPanel fixedContentPanel = new MaterialPanel("fixed-content operPanel operPanel--header");
        MaterialPanel sectionBoxPanel = new MaterialPanel("operPanel__actions");
        sectionBoxPanel.add(withTax);
        sectionBoxPanel.add(btnUpdate);
        sectionBoxPanel.add(pdfVersionButton);
        sectionBoxPanel.add(excelVersionButton);
        RootPanel.get().addStyleName("fitted-content");

        fixedContentPanel.getElement().getStyle().setZIndex(1);
        fixedContentPanel.add(sectionBoxPanel);

        headerPanel.add(fixedContentPanel);
        add(headerPanel);
        updateBudget();

        return null;
    }

    private void generatePDF(HorizontalPanel hp) {
        String pdfURL = null;
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(projectId);
        filterParameter.setWageRate(wageRate.getValue());
        filterParameter.setWithTax(withTax.getValue());
        pdfURL = CommandConstants.PDF_URL + "/projectBudgetSheetPDFHandler";
        HashMap<String, String> parametrs = filterParameter.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private void generateExcel(HorizontalPanel hp) {
        String excelURL = null;
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(projectId);
        filterParameter.setWageRate(wageRate.getValue());
        filterParameter.setWithTax(withTax.getValue());
        excelURL = CommandConstants.COMMON_URL + "/projectBudgetSheetExcelHandler";
        HashMap<String, String> parametrs = filterParameter.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, excelURL, parametrs, "_blank");
    }

    private void updateBudget() {
        LoadingPanel.loading(true);

        ProjectService.App.get().getProjectBudgetItems(projectId, withTax.getValue(), new AbstractAsyncCallback<ProjectBudget>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ProjectBudget projectBudget) {
                setProjectBudgetWageRate(projectBudget);
                if (!isWageRate) {
                    refreshEmployeeCost();
                }
                LoadingPanel.loading(false);
                built();
            }
        });

    }

    private void built() {
        if (table != null) {
            remove(table);
        }

        table = new JournalTable(columns);
        table.setWidth("100%");
        table.drawGroupBookMark(createGroupHeader(wfmStrings.revenue()));

        link = getProjectBudgetWageRate().getClientID() != null && getProjectBudgetWageRate().getClientID() != 0 ? "/relatedProject" + "/" + projectId + "/" + getProjectBudgetWageRate().getClientID() : "";

        createGroupPlanned(projectBudgetWageRate.getSalesQuotes(), Property.getPluralWithObjectCode(Constants.SALE_QUOTE, wfmStrings.salesQuotes()), GWT.getHostPageBaseURL() + "Accounting.html#salequote|add/add" + link);
        createGroupPlanned(projectBudgetWageRate.getSalesOrders(), Property.getPluralWithObjectCode(Constants.SALE_ORDER_CODE, wfmStrings.salesOrders()), GWT.getHostPageBaseURL() + "Accounting.html#saleorder|add/add" + link);
        createGroup(projectBudgetWageRate.getSalesInvoices(), Property.getPluralWithObjectCode(Constants.SALE_INVOICE, wfmStrings.saleInvoices()), GWT.getHostPageBaseURL() + "Accounting.html#saleinvoice|add/add" + link);
        createGroup(projectBudgetWageRate.getBankReceipts(), projectStrings.bankReceipt(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/RECEIVE_MONEY/relatedProject/" + projectId);
        createGroup(projectBudgetWageRate.getCashReceipts(), wfmStrings.cashReceipt(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/CASH_RECEIPT/relatedProject/" + projectId);
        createGroup(projectBudgetWageRate.getManualEntryRevenue(), wfmStrings.manualEntry(), GWT.getHostPageBaseURL() + "Accounting.html#manual|add/add/relatedProject/" + projectId);
        table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetWageRate.getSubTotalRevenue(), null, 200d));


        table.drawGroupBookMark(createGroupHeader(wfmStrings.expenses()));

        wageRate = new KpiRadioButton("rate", wfmStrings.wageRate());
        wageRate.setValue(isWageRate);


        clientChargeRate.setValue(!isWageRate);

        wageRate.addClickHandler(clickEvent -> {
            isWageRate = true;
            refresh();
        });

        actionEmployee = new SimpleLink(wfmStrings.details(), "projectbudget|summary/" + projectId, "Details", null, "btn btn--default");
        if (!isWageRate && projectBudgetClientCharge != null) {
            createGroup(projectBudgetClientCharge.getEmployeeCosts(), projectStrings.employeecost(), actionEmployee, wageRate, clientChargeRate);
            table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetClientCharge.getSubTotalEmployees(), null, 200d));
        } else {
            createGroup(projectBudgetWageRate.getEmployeeCosts(), projectStrings.employeecost(), actionEmployee, wageRate, clientChargeRate);
            table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetWageRate.getSubTotalEmployees(), null, 200d));
        }


        createGroup(projectBudgetWageRate.getExpenseClaims(), Property.getPluralWithObjectCode(Constants.EXPENSES_CLAIM, wfmStrings.expenseClaims()), GWT.getHostPageBaseURL() + "Accounting.html#expenseReports|add/add" + "/relatedProject/" + projectId);
        table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetWageRate.getSubTotalExpences(), null, 200d));

        createGroup(projectBudgetWageRate.getBankPayments(), wfmStrings.bankPayment(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/SPEND_MONEY/relatedProject/" + projectId);
        table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetWageRate.getSubTotalBankPayments(), null, 200d));

        createGroup(projectBudgetWageRate.getCashPayments(), wfmStrings.cashPayment(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/CASH_PAYMENT/relatedProject/" + projectId);
        table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetWageRate.getSubTotalCashPayments(), null, 200d));

        table.setGroup(projectStrings.ProductServicescost(), null);

        createGroupPlanned(projectBudgetWageRate.getPurchaseOrders(), "&nbsp;&nbsp;&nbsp;&nbsp;" + Property.getPluralWithObjectCode(Constants.PURCHASE_ORDER, wfmStrings.purchaseOrders()), GWT.getHostPageBaseURL() + "Accounting.html#purchaseorder|add/add" + "/relatedProject/" + projectId);
        createGroup(projectBudgetWageRate.getPurchaseInvoices(), "&nbsp;&nbsp;&nbsp;&nbsp;" + Property.getPluralWithObjectCode(Constants.PURCHASE_INVOICE, wfmStrings.purchaseInvoices()), GWT.getHostPageBaseURL() + "Accounting.html#purchaseinvoice|add/add" + "/relatedProject/" + projectId);
        table.drawGroupBookMark(createTotalItem(wfmStrings.subtotal(), projectBudgetWageRate.getSubTotalPurchases(), null, 200d));
        createGroup(projectBudgetWageRate.getStockAdjustments(), "&nbsp;&nbsp;&nbsp;&nbsp;" + wfmStrings.stockAdjustments(), GWT.getHostPageBaseURL() + "Accounting.html#stockadjustment|add/add" + "/relatedProject/" + projectId);
        createGroup(projectBudgetWageRate.getManualEntryExpense(), wfmStrings.manualEntry(), GWT.getHostPageBaseURL() + "Accounting.html#manual|add/add/relatedProject/" + projectId);
        table.drawGroupBookMark(createTotalItem(projectStrings.PROJECTREVENUE(), projectBudgetWageRate.getSubTotalRevenue(), null, 10d));
        if (!isWageRate && projectBudgetClientCharge != null) {
            table.drawGroupBookMark(createTotalItem(projectStrings.TOTALPROJECTCOST(), projectBudgetClientCharge.getTotalProjectCost(), null, 10d));
            table.drawGroupBookMark(createTotalItem(projectStrings.TOTALPROFIT(), projectBudgetClientCharge.getTotalProfit(), null, 10d));
        } else {
            table.drawGroupBookMark(createTotalItem(projectStrings.TOTALPROJECTCOST(), projectBudgetWageRate.getTotalProjectCost(), null, 10d));
            table.drawGroupBookMark(createTotalItem(projectStrings.TOTALPROFIT(), projectBudgetWageRate.getTotalProfit(), null, 10d));
        }

        table.getTable().setWidth("100%");
        add(table);
    }

    @Override
    public String getIconStyle() {
        return "bgMark project-budget-sheet";
    }

    private void createGroup(ProjectBudgetItem[] items, String groupName, String actionUrl) {
        JournalTableItem[] tabItems = null;
        if (items != null && items.length > 0) {
            tabItems = new JournalTableItem[items.length];
            for (int i = 0; i < items.length; i++) {
                Object[] objects = new Object[5];
                if (items[i].getName() != null) {
                    String vendorName = items[i].getVendor() != null ? items[i].getVendor().getName() != null ? (" - [" + items[i].getVendor().getName() + "]") : "" : "";
                    objects[0] = (items[i].getAction() != null ? linkDetails(items[i].getName(), items[i].getAction()) : items[i].getName()) + vendorName;
                }
                if (items[i].getPlannedWageAmount() != null && items[i].getPlannedWageAmount().doubleValue() != 0d) {
                    objects[1] = getSheetItem(items[i].getPlannedWageAmount());
                }
                if (items[i].getActualWageAmount() != null) {
                    objects[2] = getSheetItem(items[i].getActualWageAmount());
                }
                if (items[i].getVariancePercentItem().compareTo(BigDecimal.ZERO) != 0) {
                    objects[3] = getSheetItemPercentage(items[i].getVariancePercentItem());
                } else {
                    objects[3] = " ";
                }
                if (items[i].getVarianceAmount() != null && items[i].getVarianceAmount().compareTo(BigDecimal.ZERO) != 0) {
                    objects[4] = getSheetItem(items[i].getVarianceAmount());
                } else {
                    objects[4] = " ";
                }
                tabItems[i] = new JournalTableItem(objects);
            }
        }

        FlexTable groupTitle = new FlexTable();
        if (hasAccessToChange) {
            groupTitle.setWidget(0, 0, new HTMLPanel("b", groupName));
            groupTitle.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

            groupTitle.setWidget(0, 1, addNew(actionUrl));
            groupTitle.getFlexCellFormatter().setStyleName(0, 1, "text-right");
        } else {
            groupTitle.setWidget(0, 0, new HTMLPanel("b", groupName));
            groupTitle.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        }
        table.setGroup(groupTitle, tabItems);

    }

    private void createGroupPlanned(ProjectBudgetItem[] items, String groupName, final String actionUrl) {
        JournalTableItem[] tabItems = null;
        if (items != null && items.length > 0) {
            tabItems = new JournalTableItem[items.length];
            for (int i = 0; i < items.length; i++) {
                Object[] objects = new Object[5];
                if (items[i].getName() != null) {
                    String vendorName = items[i].getVendor() != null ? items[i].getVendor().getName() != null ? (" - [" + items[i].getVendor().getName() + "]") : "" : "";
                    objects[0] = (items[i].getAction() != null ? linkDetails(items[i].getName(), items[i].getAction()) : items[i].getName()) + vendorName;
                }
                if (items[i].getPlannedWageAmount() != null) {
                    objects[1] = getSheetItem(items[i].getPlannedWageAmount());
                }
                if (items[i].getActualWageAmount() != null && items[i].getActualWageAmount().doubleValue() != 0d) {
                    objects[2] = getSheetItem(items[i].getActualWageAmount());
                }
                if (items[i].getVariancePercentItem().compareTo(BigDecimal.ZERO) != 0) {
                    objects[3] = getSheetItemPercentage(items[i].getVariancePercentItem());
                } else {
                    objects[3] = " ";
                }
                if (items[i].getVarianceAmount() != null && items[i].getVarianceAmount().compareTo(BigDecimal.ZERO) != 0) {
                    objects[4] = getSheetItem(items[i].getVarianceAmount());
                } else {
                    objects[4] = " ";
                }
                tabItems[i] = new JournalTableItem(objects);
            }
        }

        FlexTable groupTitle = new FlexTable();

        if (hasAccessToChange) {
            groupTitle.setWidget(0, 0, new HTMLPanel("b", groupName));
            groupTitle.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

            groupTitle.setWidget(0, 1, addNew(actionUrl));
            groupTitle.getFlexCellFormatter().setStyleName(0, 1, "text-right");
        } else {
            groupTitle.setWidget(0, 0, new HTMLPanel("b", groupName));
            groupTitle.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        }


        table.setGroup(groupTitle, tabItems);

    }

    private Anchor addNew(final String actionUrl) {
        Anchor action = new Anchor();
        action.setStyleName("btn--circle plusMinus");
        action.addClickHandler(clickEvent -> {
            if (isAccountingSetup) {
                Window.open(actionUrl, "_blank", "");
            } else {
                showIsAccountingSetUpMessage();
            }
        });
        return action;
    }

    private void createGroup(ProjectBudgetItem[] items, String groupName, Widget... action) {
        JournalTableItem[] tabItems = null;
        if (items != null && items.length > 0) {
            tabItems = new JournalTableItem[items.length];
            for (int i = 0; i < items.length; i++) {
                Object[] objects = new Object[5];
                if (items[i].getName() != null) {
                    String vendorName = items[i].getVendor() != null ? items[i].getVendor().getName() != null ? (" - [" + items[i].getVendor().getName() + "]") : "" : "";
                    objects[0] = (items[i].getAction() != null ? linkDetails(items[i].getName(), items[i].getAction()) : items[i].getName()) + vendorName;
                }
                if (items[i].getPlannedWageAmount() != null) {
                    objects[1] = getSheetItem(items[i].getPlannedWageAmount());
                }
                if (items[i].getActualWageAmount() != null) {
                    objects[2] = getSheetItem(items[i].getActualWageAmount());
                }
                if (items[i].getVariancePercentItem().compareTo(BigDecimal.ZERO) != 0) {
                    objects[3] = getSheetItemPercentage(items[i].getVariancePercentItem());
                } else {
                    objects[3] = " ";
                }
                if (items[i].getVarianceAmount() != null && items[i].getVarianceAmount().compareTo(BigDecimal.ZERO) != 0) {
                    objects[4] = getSheetItem(items[i].getVarianceAmount());
                } else {
                    objects[4] = " ";
                }
                tabItems[i] = new JournalTableItem(objects);
            }
        }

        FlexTable groupTitle = new FlexTable();
        groupTitle.setWidget(0, 0, new HTMLPanel("b", groupName));
        if (action.length > 2) {
            Div div = new Div();
            div.add(action[1]);
            Widget widget = action[2];
            widget.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
            div.add(widget);
            groupTitle.setWidget(0, 1, div);
        }
        groupTitle.setWidget(0, 2, action[0]);
        groupTitle.getFlexCellFormatter().setStyleName(0, 2, "text-right");

        table.setGroup(groupTitle, tabItems);
    }

    private JournalTableItem createGroupHeader(String header) {
        Object[] values = new Object[1];
        values[0] = new HTML("<span>" + header + "</span>");
        JournalTableItem item = new JournalTableItem(values);
        item.setColspans(new Integer[]{5});
        item.setRowStyleName("budget-sheet-table__subheader-1");
        return item;
    }

    private JournalTableItem createTotalItem(String title, ProjectBudgetItem totalItem, String color, double padding) {
        Object[] values = new Object[5];
        HTMLPanel titleHtml = new HTMLPanel(title);
        titleHtml.getElement().getStyle().setPaddingLeft(padding, Style.Unit.PX);
        titleHtml.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        values[0] = titleHtml;
        values[1] = getSheetItemBold(totalItem.getPlannedWageAmount());
        values[2] = getSheetItemBold(totalItem.getActualWageAmount());
        values[3] = getSheetItemPercentageBold(totalItem.getVariancePercentItem());
        values[4] = getSheetItemBold(totalItem.getVarianceAmount());
        JournalTableItem journalTableItem = new JournalTableItem(values);
        journalTableItem.setBackgroundColor(color);
        return journalTableItem;
    }

    private HTMLPanel getSheetItemPercentage(BigDecimal amount) {
        HTMLPanel itemValue = new HTMLPanel("span", (amount.compareTo(BigDecimal.ZERO) < 0 ? "(" + percentageFormat.format(amount.abs()) + "%)" : (percentageFormat.format(amount) + "%")));
        itemValue.setStyleName("header-cell-settings");
        return itemValue;
    }

    private HTMLPanel getSheetItemPercentageBold(BigDecimal amount) {
        HTMLPanel itemValue = new HTMLPanel("span", (amount.compareTo(BigDecimal.ZERO) < 0 ? "(" + percentageFormat.format(amount.abs()) + "%)" : (percentageFormat.format(amount) + "%")));
        itemValue.setStyleName("header-cell-settings");
        itemValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        return itemValue;
    }

    private HTMLPanel getSheetItem(BigDecimal amount) {
        HTMLPanel itemValue = new HTMLPanel("span", (amount.compareTo(BigDecimal.ZERO) < 0 ? "(" + numberFormat.format(amount.abs()) + ")" : numberFormat.format(amount)));
        itemValue.setStyleName("header-cell-settings");
        return itemValue;
    }

    private HTMLPanel getSheetItemBold(BigDecimal amount) {
        HTMLPanel itemValue = new HTMLPanel("span", (amount.compareTo(BigDecimal.ZERO) < 0 ? "(" + numberFormat.format(amount.abs()) + ")" : numberFormat.format(amount)));
        itemValue.setStyleName("header-cell-settings");
        itemValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        return itemValue;
    }


    private Anchor linkDetails(String name, String action) {
        return new Anchor(name, true, GWT.getHostPageBaseURL() + "Accounting.html#" + action, "_blank");
    }

    private void refreshEmployeeCost() {
        ProjectService.App.get().getEmployeeCostClientCharge(projectId, new AbstractAsyncCallback<ProjectBudget>() {

            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ProjectBudget projectBudget) {
//                Window.alert("loop");
                /////TOTALS/////
                //Project Cost//
                projectBudget.getTotalProjectCost().setPlannedWageAmount(getProjectBudgetWageRate().getSubTotalExpences().getPlannedWageAmount().add(projectBudget.getSubTotalEmployees().getPlannedWageAmount()).add(getProjectBudgetWageRate().getSubTotalPurchases().getPlannedWageAmount()));
                projectBudget.getTotalProjectCost().setActualWageAmount(getProjectBudgetWageRate().getSubTotalExpences().getActualWageAmount().add(projectBudget.getSubTotalEmployees().getActualWageAmount()).add(getProjectBudgetWageRate().getSubTotalPurchases().getActualWageAmount()));
                projectBudget.getTotalProjectCost().setVarianceAmount(projectBudget.getTotalProjectCost().getPlannedWageAmount().subtract(projectBudget.getTotalProjectCost().getActualWageAmount()));
                if (projectBudget.getTotalProjectCost().getActualWageAmount() != null && projectBudget.getTotalProjectCost().getActualWageAmount().doubleValue() != 0d) {
                    projectBudget.getTotalProjectCost().setVariancePerCent(projectBudget.getTotalProjectCost().getVarianceAmount().divide(projectBudget.getTotalProjectCost().getActualWageAmount(), 4, RoundingMode.HALF_UP));
                }

                projectBudget.getTotalProfit().setPlannedWageAmount(getProjectBudgetWageRate().getSubTotalRevenue().getPlannedWageAmount().subtract(projectBudget.getTotalProjectCost().getPlannedWageAmount()));
                projectBudget.getTotalProfit().setActualWageAmount(getProjectBudgetWageRate().getSubTotalRevenue().getActualWageAmount().subtract(projectBudget.getTotalProjectCost().getActualWageAmount()));
                projectBudget.getTotalProfit().setVarianceAmount(projectBudget.getTotalProfit().getActualWageAmount().subtract(projectBudget.getTotalProfit().getPlannedWageAmount()));
                if (projectBudget.getTotalProfit().getPlannedWageAmount() != null && projectBudget.getTotalProfit().getPlannedWageAmount().doubleValue() != 0d) {
                    projectBudget.getTotalProfit().setVariancePerCent(projectBudget.getTotalProfit().getVarianceAmount().divide(projectBudget.getTotalProfit().getPlannedWageAmount(), 4, RoundingMode.HALF_UP));
                }
                setProjectBudgetClientCharge(projectBudget);
                built();
            }
        });
    }

    private void refresh() {
        built();
    }

    public ProjectBudget getProjectBudgetWageRate() {
        return projectBudgetWageRate;
    }

    public void setProjectBudgetWageRate(ProjectBudget projectBudgetWageRate) {
        this.projectBudgetWageRate = projectBudgetWageRate;
    }

    public void setProjectBudgetClientCharge(ProjectBudget projectBudgetClientCharge) {
        this.projectBudgetClientCharge = projectBudgetClientCharge;
    }

    private void showIsAccountingSetUpMessage() {
        String message = null;
        if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.ACCOUNTANT)) {
            message = projectStrings.yourAccounting() + " " + Utils.getSupportEmail() + " " + wfmStrings.forMoreDetails();
        } else {
            message = wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1();
        }
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK,
                message, new CloseHandler() {
            @Override
            public void onSubmit() {
                //here if should be link to Accounting PAGE;
            }
        });
        wfmMessageBox.setTitle(wfmStrings.information());
        wfmMessageBox.open();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().considerBodyHasOperPanel(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        MainLayout.get().considerBodyHasOperPanel(false);
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
