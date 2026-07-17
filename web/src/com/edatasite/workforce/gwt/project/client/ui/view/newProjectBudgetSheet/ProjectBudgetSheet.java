package com.edatasite.workforce.gwt.project.client.ui.view.newProjectBudgetSheet;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ProfitAndLoss.NewProfitAndLoss;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectMessages;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudget;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class ProjectBudgetSheet extends Composite implements Constants {

    interface ProjectBudgetSheetBinder extends UiBinder<HTMLPanel, ProjectBudgetSheet> {
    }

    private static final ProjectBudgetSheetBinder ourUiBinder = GWT.create(ProjectBudgetSheetBinder.class);
    @UiField
    HTMLPanel filterPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    TableSectionElement header;
    @UiField
    DivElement accountText;
    @UiField
    DivElement plannedAmountText;
    @UiField
    DivElement actualAmountText;
    @UiField
    DivElement varianceText;
    @UiField
    DivElement varianceCostText;
    @UiField
    Element revenueGroup;
    @UiField
    Element expensesGroup;
    @UiField
    Element totalsLine;

    private final boolean isAccountingSetup = "true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP));
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectMessages projectMessages = ProjectMessages.App.get();
    private final NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private final NumberFormat percentageFormat = NumberFormat.getFormat(",##0.00");

    private final Integer projectId;
    private boolean hasAccessToChange = true;
    private final KpiCheckBox withTax;
    private final WfmButton2 btnUpdate;
    private boolean isWageRate = true;
    private SimpleLink actionEmployee;
    private final KpiRadioButton clientChargeRate = new KpiRadioButton("rate", wfmStrings.clientChargeRate());
    private KpiRadioButton wageRate = new KpiRadioButton("rate", wfmStrings.wageRate());
    private ProjectBudget projectBudgetWageRate;
    private ProjectBudget projectBudgetClientCharge;

    public ProjectBudgetSheet(Integer projectId, boolean hasAccessToChange) {
        isWageRate = !Utils.hasGenericAccess(GenericSettingsEnum.IS_DISABLED_WAGE_RATE);

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        header.getStyle().setDisplay(Style.Display.NONE);

        withTax = new KpiCheckBox(projectStrings.withTax());
        withTax.ensureDebugId("projectBudget_" + "withTax");
        withTax.addValueChangeHandler(valueChangeEvent -> {
            if (projectBudgetWageRate != null) {
                clearElementChild(revenueGroup);
                clearElementChild(expensesGroup);
                clearTotals(totalsLine);
                updateBudget();
            }
        });
        btnUpdate = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        btnUpdate.addClickHandler(clickEvent -> {
            clearElementChild(revenueGroup);
            clearElementChild(expensesGroup);
            clearTotals(totalsLine);
            updateBudget();
        });

        wageRate = new KpiRadioButton("rate", wfmStrings.wageRate());
        wageRate.setValue(isWageRate);
        wageRate.addClickHandler(clickEvent -> {
            isWageRate = true;
            clearElementChild(revenueGroup);
            clearElementChild(expensesGroup);
            clearTotals(totalsLine);
            updateBudget();
        });

        clientChargeRate.setValue(!isWageRate);
        clientChargeRate.addClickHandler(clickEvent -> {
            isWageRate = false;
            clearElementChild(revenueGroup);
            clearElementChild(expensesGroup);
            clearTotals(totalsLine);
            updateBudget();
        });

        HorizontalPanel headerPanel1 = new HorizontalPanel();
        headerPanel1.setWidth("100%");
        MaterialPanel fixedContentPanel = new MaterialPanel("fixed-content operPanel operPanel--header");
        MaterialPanel sectionBoxPanel = new MaterialPanel("operPanel__actions");
        sectionBoxPanel.add(withTax);
        sectionBoxPanel.add(wageRate);
        sectionBoxPanel.add(clientChargeRate);
        sectionBoxPanel.add(getExportOption());
        sectionBoxPanel.add(btnUpdate);
        fixedContentPanel.getElement().getStyle().setZIndex(1);
        fixedContentPanel.add(sectionBoxPanel);
        headerPanel1.add(fixedContentPanel);

        filterPanel.add(headerPanel1);

        this.projectId = projectId;
        this.hasAccessToChange = hasAccessToChange;

        accountText.setInnerHTML("");
        plannedAmountText.setInnerHTML(projectStrings.plannedAmount());
        actualAmountText.setInnerHTML(projectStrings.actualAmount());
        varianceText.setInnerHTML(projectStrings.variancePercent());
        varianceCostText.setInnerHTML(projectStrings.variancecost());
        updateBudget();
    }

    private Div getExportOption() {
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();
        ieIcon.setClass("ficon--download-cloud");
        showLink.add(ieIcon);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2 dropdown-content--export");
        showMenuContainer.setBelowOrigin(true);
        showLink.add(showMenuContainer);

        MaterialLink exportPDF = new MaterialLink();
        exportPDF.addStyleName("hasicon--left");
        Icon pdfIcon = new Icon();
        pdfIcon.setClass("ficon--file-pdf");
        exportPDF.add(pdfIcon);
        exportPDF.setText(wfmStrings.pdf());
        exportPDF.addClickHandler(ch -> {
            generatePdfOrExcel(exportPanel, true);
        });
        showMenuContainer.add(exportPDF);

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(ch -> {
            generatePdfOrExcel(exportPanel, false);
        });
        showMenuContainer.add(exportExl);

        showMenuBar.add(showLink);
        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        return div;
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
                header.getStyle().setDisplay(Style.Display.BLOCK);
                built();
                Utils.table__frame_affix_init();
                LoadingPanel.loading(false);
            }
        });

    }

    private void built() {
        //REVENUE
        collectRevenueTreeData(revenueGroup, projectBudgetWageRate, wfmStrings.revenue());

        //EXPENSE
        collectExpenseTreeData(expensesGroup, wfmStrings.expenses());

        //PROJECT TOTALS
        createGroupTotalRow(totalsLine, projectStrings.PROJECTREVENUE(), projectBudgetWageRate.getSubTotalRevenue());
        if (!isWageRate && projectBudgetClientCharge != null) {
            createGroupTotalRow(totalsLine, projectStrings.TOTALPROJECTCOST(), projectBudgetClientCharge.getTotalProjectCost());
            createGroupTotalRow(totalsLine, projectStrings.TOTALPROFIT(), projectBudgetClientCharge.getTotalProfit());
        } else {
            createGroupTotalRow(totalsLine, projectStrings.TOTALPROJECTCOST(), projectBudgetWageRate.getTotalProjectCost());
            createGroupTotalRow(totalsLine, projectStrings.TOTALPROFIT(), projectBudgetWageRate.getTotalProfit());
        }
    }

    private void collectRevenueTreeData(Element element, ProjectBudget projectBudget, String groupName) {
        createGroupHeader(element, groupName, null);

        String link = getProjectBudgetWageRate().getClientID() != null && getProjectBudgetWageRate().getClientID() != 0 ? "/relatedProject" + "/" + projectId + "/" + getProjectBudgetWageRate().getClientID() : "";

        //SALES QUOTES
        Element saleQuotesTree = drawChilds(element, Property.getPluralWithObjectCode(Constants.SALE_QUOTE, wfmStrings.salesQuotes()), GWT.getHostPageBaseURL() + "Accounting.html#salequote|add/add" + link, false);
        createGroup(saleQuotesTree, projectBudgetWageRate.getSalesQuotes());

        //SALES ORDERS
        Element saleOrdersTree = drawChilds(element, Property.getPluralWithObjectCode(Constants.SALE_ORDER_CODE, wfmStrings.salesOrders()), GWT.getHostPageBaseURL() + "Accounting.html#saleorder|add/add" + link, false);
        createGroup(saleOrdersTree, projectBudgetWageRate.getSalesOrders());

        //SALES INVOICES
        Element saleInvoicesTree = drawChilds(element, Property.getPluralWithObjectCode(Constants.SALE_INVOICE, wfmStrings.saleInvoices()), GWT.getHostPageBaseURL() + "Accounting.html#saleinvoice|add/add" + link, false);
        createGroup(saleInvoicesTree, projectBudgetWageRate.getSalesInvoices());

        //BANK RECEIPT
        Element bankResiptTree = drawChilds(element, projectStrings.bankReceipt(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/RECEIVE_MONEY/relatedProject/" + projectId, false);
        createGroup(bankResiptTree, projectBudgetWageRate.getBankReceipts());

        //CASH RECEIPT
        Element cashResiptTree = drawChilds(element, wfmStrings.cashReceipt(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/CASH_RECEIPT/relatedProject/" + projectId, false);
        createGroup(cashResiptTree, projectBudgetWageRate.getCashReceipts());

        //Manual Entry Revenue
        Element manualEntryRevenueTree = drawChilds(element, wfmStrings.manualEntry(), GWT.getHostPageBaseURL() + "Accounting.html#manual|add/add/relatedProject/" + projectId, false);
        createGroup(manualEntryRevenueTree, projectBudgetWageRate.getManualEntryRevenue());

        createGroupTotalRow(element, wfmStrings.total() + " " + groupName, projectBudget.getSubTotalRevenue());
    }

    private void collectExpenseTreeData(Element element, String groupName) {
        createGroupHeader(element, groupName, null);

        //EMPLOYEE COST
        actionEmployee = new SimpleLink(wfmStrings.details(), "projectbudget|summary/" + projectId, "Details", null, "btn btn--default");
        if (!isWageRate && projectBudgetClientCharge != null) {
            Element element1 = drawChilds(element, projectStrings.employeecost(), null, true, actionEmployee);
            createGroup(element1, projectBudgetClientCharge.getEmployeeCosts());
            createGroupTotalRow(element1, wfmStrings.total() + " " + projectStrings.employeecost(), projectBudgetClientCharge.getSubTotalEmployees());
        } else {
            Element element1 = drawChilds(element, projectStrings.employeecost(), null, true, actionEmployee);
            createGroup(element1, projectBudgetWageRate.getEmployeeCosts());
            createGroupTotalRow(element1, wfmStrings.total() + " " + projectStrings.employeecost(), projectBudgetWageRate.getSubTotalEmployees());
        }

        //EXPENSE CLAIMS
        Element expenseClaimTree = drawChilds(element, Property.getPluralWithObjectCode(Constants.EXPENSES_CLAIM, wfmStrings.expenseClaims()), GWT.getHostPageBaseURL() + "Accounting.html#expenseReports|add/add" + "/relatedProject/" + projectId, true);
        createGroup(expenseClaimTree, projectBudgetWageRate.getExpenseClaims());
        createGroupTotalRow(expenseClaimTree, wfmStrings.total() + " " + Property.getPluralWithObjectCode(Constants.EXPENSES_CLAIM, wfmStrings.expenseClaims()), projectBudgetWageRate.getSubTotalExpences());

        //BANK PAYMENTS
        Element bankPaymentTree = drawChilds(element, wfmStrings.bankPayment(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/SPEND_MONEY/relatedProject/" + projectId, true);
        createGroup(bankPaymentTree, projectBudgetWageRate.getBankPayments());
        createGroupTotalRow(bankPaymentTree, wfmStrings.total()  + " " + wfmStrings.bankPayment(), projectBudgetWageRate.getSubTotalBankPayments());

        //CASH PAYMENTS
        Element cashPaymentTree = drawChilds(element, wfmStrings.cashPayment(), GWT.getHostPageBaseURL() + "Accounting.html#spendreceivemoney|add/add/CASH_PAYMENT/relatedProject/" + projectId, true);
        createGroup(cashPaymentTree, projectBudgetWageRate.getCashPayments());
        createGroupTotalRow(cashPaymentTree, wfmStrings.total() + " " + wfmStrings.cashPayment(), projectBudgetWageRate.getSubTotalCashPayments());

        //PRODUCT & SERVICES COST
        Element productServicesCostTree = drawChilds(element, Property.getPluralWithObjectCodeWithReplace(Constants.PRODUCTS_OR_SERVICES, projectStrings.ProductServicescost(), wfmStrings.productsOrServices()), null, true);

        //PURCHASE ORDERS
        Element purchaseOrdersTree = drawChilds(productServicesCostTree, Property.getPluralWithObjectCode(Constants.PURCHASE_ORDER, wfmStrings.purchaseOrders()), GWT.getHostPageBaseURL() + "Accounting.html#purchaseorder|add/add" + "/relatedProject/" + projectId, false);
        createGroup(purchaseOrdersTree, projectBudgetWageRate.getPurchaseOrders());

        //PURCHASE INVOICES
        Element purchaseInvoicesTree = drawChilds(productServicesCostTree, Property.getPluralWithObjectCode(Constants.PURCHASE_INVOICE, wfmStrings.purchaseInvoices()), GWT.getHostPageBaseURL() + "Accounting.html#purchaseinvoice|add/add" + "/relatedProject/" + projectId, false);
        createGroup(purchaseInvoicesTree, projectBudgetWageRate.getPurchaseInvoices());

        createGroupTotalRow(productServicesCostTree, wfmStrings.total() + " " + projectStrings.ProductServicescost(), projectBudgetWageRate.getSubTotalPurchases());

        //STOCK ADJUSTMENTS
        Element stockAdjustmentsTree = drawChilds(element, wfmStrings.stockAdjustments(), GWT.getHostPageBaseURL() + "Accounting.html#stockadjustment|add/add" + "/relatedProject/" + projectId, false);
        createGroup(stockAdjustmentsTree, projectBudgetWageRate.getStockAdjustments());

        //Manual Entry Expense
        Element manualEntryExpenseTree = drawChilds(element, wfmStrings.manualEntry(), GWT.getHostPageBaseURL() + "Accounting.html#manual|add/add" + "/relatedProject/" + projectId, false);
        createGroup(manualEntryExpenseTree, projectBudgetWageRate.getManualEntryExpense());

        if (!isWageRate && projectBudgetClientCharge != null) {
            createGroupTotalRow(element, wfmStrings.total() + " " + groupName, projectBudgetClientCharge.getTotalProjectCost());
        } else {
            createGroupTotalRow(element, wfmStrings.total() + " " + groupName, projectBudgetWageRate.getTotalProjectCost());
        }
    }

    private void createGroup(Element element, ProjectBudgetItem[] items) {
        if (items != null && items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                create(element, items[i]);
            }
        }
    }

    private Element create(Element element, ProjectBudgetItem accItem1) {
        Element mainTr = DOM.createTR();
        Element mainTd = DOM.createTD();
        mainTr.appendChild(mainTd);
        if (accItem1.getName() != null) {
            Element tr = DOM.createTR();
            Element td1 = DOM.createTD();
            td1.addClassName(LEFT_ALIGN_CELL);
            String vendorName = accItem1.getVendor() != null ? accItem1.getVendor().getName() != null
                                                               ? (" - [" + accItem1.getVendor().getName() + "]")
                                                               : "" : "";
            if (accItem1.getAction() != null) {
                td1.appendChild(linkDetails(accItem1.getName(), accItem1.getAction()).getElement());
            } else {
                td1.setInnerHTML(accItem1.getName() + vendorName);
            }
            tr.appendChild(td1);

            Element td2 = DOM.createTD();
            td2.addClassName(RIGHT_ALIGN_CELL);
            if (accItem1.getPlannedWageAmount() != null) {
                td2.appendChild(getSheetItem(accItem1.getPlannedWageAmount()).getElement());
            } else {
                td2.setInnerHTML("0.0000");
            }
            tr.appendChild(td2);

            Element td3 = DOM.createTD();
            td3.addClassName(RIGHT_ALIGN_CELL);
            if (accItem1.getActualWageAmount() != null) {
                td3.appendChild(getSheetItem(accItem1.getActualWageAmount()).getElement());
            } else {
                td3.setInnerHTML("0.0000");
            }
            tr.appendChild(td3);

            Element td4 = DOM.createTD();
            td4.addClassName(RIGHT_ALIGN_CELL); // Joylashuv klassini har doim qo'shamiz
            // Null tekshiruvini qo'shish muhim, chunki null ob'ektda compareTo() chaqirib bo'lmaydi
            if (accItem1.getVariancePercentItem() != null && accItem1.getVariancePercentItem().compareTo(BigDecimal.ZERO) != 0) {
                td4.appendChild(getSheetItemPercentage(accItem1.getVariancePercentItem()).getElement());
            } else {
                td4.setInnerHTML("0.00%"); // Agar ma'lumot bo'lmasa, standart qiymat beramiz
            }
            tr.appendChild(td4);

            Element td5 = DOM.createTD();
            td5.addClassName(RIGHT_ALIGN_CELL);
            // Null tekshiruvini qo'shish muhim
            if (accItem1.getVarianceAmount() != null && accItem1.getVarianceAmount().compareTo(BigDecimal.ZERO) != 0) {
                td5.appendChild(getSheetItem(accItem1.getVarianceAmount()).getElement());
            } else {
                td5.setInnerHTML("0.0000");
            }
            tr.appendChild(td5);

            element.appendChild(tr);
        }
        return element;
    }

    private Element drawChilds(Element element, String groupName, String actionUrl, boolean collapsable, Widget... action) {
        Element mainTr = DOM.createTR();
        Element mainTd = DOM.createTD();
        mainTr.appendChild(mainTd);
        mainTd.addClassName("second_level");
        mainTd.setAttribute("colspan", "5");

        Element childTable = DOM.createTable();
        childTable.setClassName("table table_report");
        mainTd.appendChild(childTable);

        Element header = createTH();
        header.setAttribute("style", "display: none;");

        Element childTBody = DOM.createTBody();
        childTBody.setClassName("category_set");

        childTable.appendChild(header);
        childTable.appendChild(childTBody);

        Element childTr = DOM.createTR();
        childTr.addClassName("heading_row");
        Element childTd = DOM.createTD();
        childTr.appendChild(childTd);

        if (collapsable) {
            childTBody.addClassName("collapsed");
            Element icon = DOM.createElement("i");
            icon.addClassName("btn--circle plusMinus");
            DOM.sinkEvents(icon.cast(), Event.ONCLICK);
            DOM.setEventListener(icon.cast(), event -> {
                if (childTBody.getClassName().contains("collapsed")) {
                    childTBody.removeClassName("collapsed");
                    childTBody.addClassName("expanded");
                } else {
                    childTBody.removeClassName("expanded");
                    childTBody.addClassName("collapsed");
                }
            });
            childTd.appendChild(icon);
        } else {
            childTBody.addClassName("expanded");
        }

        childTd.addClassName(LEFT_ALIGN_CELL);
        Element nameElement = DOM.createElement("span");
        nameElement.setInnerHTML(groupName);
        childTd.appendChild(nameElement);
        if (actionUrl != null) {
            childTd.appendChild(getAddNewIcon(actionUrl));
        }

        Element td2 = DOM.createTD();
        childTr.appendChild(td2);
        Element td3 = DOM.createTD();
        childTr.appendChild(td3);
        Element td4 = DOM.createTD();
        childTr.appendChild(td4);
        Element td5 = DOM.createTD();
        childTr.appendChild(td5);
        childTBody.appendChild(childTr);
        element.appendChild(mainTr);
        return childTBody;
    }

    private Element getAddNewIcon(final String actionUrl) {
        Element icon = DOM.createElement("a");
        icon.addClassName("btn--circle btn-small btn--success");
        icon.setInnerHTML("<svg class=\" icon--plus\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#plus\"></use></svg>");
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.setEventListener(icon.cast(), event -> {
            if (isAccountingSetup) {
                Window.open(actionUrl, "_blank", "");
            } else {
                showIsAccountingSetUpMessage();
            }
        });
        return icon;
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private void clearTotals(Element element) {
        element.removeAllChildren();
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

    private Element createTH() {
        Element header = DOM.createTHead();
        header.setClassName("point_affix_top text-nowrap");
        Element tr = DOM.createTR();

        Element th = DOM.createTH();
        th.addClassName("stickerCell");
        th.addClassName(TEXT_LEFT);
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.getStyle().clearWidth();
        divElement.getStyle().setProperty("minWidth", "260px");
        divElement.setInnerHTML(NewProfitAndLoss.wfmStrings.accountName());
        th.appendChild(divElement);
        tr.appendChild(th);

        Element th1 = DOM.createTH();
        th1.addClassName("stickerCell text-right");

        Element divElement1 = DOM.createDiv();
        divElement1.setClassName("frame_affix_top");

        divElement1.getStyle().clearWidth();
        divElement1.getStyle().setProperty("minWidth", "90px");
        th1.appendChild(divElement1);
        for (int i = 2; i >= 0; i--) {
            Element comparedTh = DOM.createTH();
            comparedTh.addClassName("stickerCell text-right");
            Element divElement2 = DOM.createDiv();
            divElement2.getStyle().setProperty("minWidth", "90px");
            divElement2.setClassName("frame_affix_top");
            comparedTh.appendChild(divElement2);
            tr.appendChild(comparedTh);
        }

        tr.appendChild(th1);
        header.appendChild(tr);
        return header;
    }

    private HTMLPanel getSheetItem(BigDecimal amount) {
        HTMLPanel itemValue = new HTMLPanel("span", (amount.compareTo(BigDecimal.ZERO) < 0
                                                     ? "(" + numberFormat.format(amount.abs()) + ")"
                                                     : numberFormat.format(amount)));
        itemValue.setStyleName("header-cell-settings");
        return itemValue;
    }

    private HTMLPanel getSheetItemPercentage(BigDecimal amount) {
        HTMLPanel itemValue = new HTMLPanel("span", (amount.compareTo(BigDecimal.ZERO) < 0
                                                     ? "(" + percentageFormat.format(amount.abs()) + "%)"
                                                     : (percentageFormat.format(amount) + "%")));
        itemValue.setStyleName("header-cell-settings");
        return itemValue;
    }

    private com.google.gwt.user.client.ui.Anchor linkDetails(String name, String action) {
        return new Anchor(name, true, GWT.getHostPageBaseURL() + "Accounting.html#" + action, "_blank");
    }

    private void refreshEmployeeCost() {
        ProjectService.App.get().getEmployeeCostClientCharge(projectId, new AbstractAsyncCallback<ProjectBudget>() {

            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ProjectBudget projectBudget) {
                //PROJECT COST
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
            }
        });
    }

    private void createNewLine(Element element, String name) {
        Element tr = DOM.createTR();
        tr.addClassName("heading_row");
        Element td = DOM.createTD();

        Element nameElement = DOM.createElement("span");
        nameElement.setInnerHTML("<b>" + name + "</b>");
        td.appendChild(nameElement);

        td.setAttribute("colspan", "5");
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createGroupHeader(Element element, String groupName, String actionUrl) {
        element.addClassName("expanded");
        Element tr = DOM.createTR();
        tr.addClassName("heading_row");
        Element td = DOM.createTD();

        Element icon = DOM.createElement("i");
        icon.addClassName("btn--circle plusMinus");
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.setEventListener(icon.cast(), event -> {
            if (element.getClassName().contains("collapsed")) {
                element.removeClassName("collapsed");
                element.addClassName("expanded");
            } else {
                element.removeClassName("expanded");
                element.addClassName("collapsed");
            }
        });

        Element nameElement = DOM.createElement("span");
        nameElement.setInnerHTML(groupName);
        td.appendChild(icon);
        td.appendChild(nameElement);

        td.setAttribute("colspan", "5");
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createGroupTotalRow(Element element, String groupName, ProjectBudgetItem itemsTotal) {
        Element td = DOM.createTD();

        Element tr = DOM.createTR();
        tr.addClassName("total_row");
        td.setInnerHTML(groupName);
        td.setAttribute("style", "border-right: none;");
        tr.appendChild(td);
        element.appendChild(tr);
        Element td2 = DOM.createTD();
        tr.appendChild(td2);
        Element td3 = DOM.createTD();
        tr.appendChild(td3);
        Element td4 = DOM.createTD();
        tr.appendChild(td4);
        Element td5 = DOM.createTD();
        tr.appendChild(td5);

        if (itemsTotal != null) {
            if (itemsTotal.getPlannedWageAmount() != null) {
                td2.setInnerHTML(getValueAsString(itemsTotal.getPlannedWageAmount()));
                td2.addClassName(RIGHT_ALIGN_CELL);
            }
            if (itemsTotal.getActualWageAmount() != null) {
                td3.setInnerHTML(getValueAsString(itemsTotal.getActualWageAmount()));
                td3.addClassName(RIGHT_ALIGN_CELL);
            }
            if (itemsTotal.getVariancePercentItem() != null) {
                td4.appendChild(getSheetItemPercentage(itemsTotal.getVariancePercentItem()).getElement());
                td4.addClassName(RIGHT_ALIGN_CELL);
            }
            if (itemsTotal.getVarianceAmount() != null) {
                td5.setInnerHTML(getValueAsString(itemsTotal.getVarianceAmount()));
                td5.addClassName(RIGHT_ALIGN_CELL);
            }
        }
        element.appendChild(tr);
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return " " + AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
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

    private void generatePdfOrExcel(HTMLPanel hp, boolean isPdf) {
        String url = null;
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(projectId);
        filterParameter.setWageRate(wageRate.getValue());
        filterParameter.setWithTax(withTax.getValue());
        if (isPdf) {
            url = CommandConstants.PDF_URL + "/projectBudgetSheetPDFHandler";
        } else {
            url = CommandConstants.COMMON_URL + "/projectBudgetSheetExcelHandler";
        }
        HashMap<String, String> parametrs = filterParameter.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, url, parametrs, "_blank");
    }
}
