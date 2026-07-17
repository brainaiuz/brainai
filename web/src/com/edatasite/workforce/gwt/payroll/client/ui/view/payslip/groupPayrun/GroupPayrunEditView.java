package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunRequestObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrolTableItemListResult;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets.ExtendedHTMLCell;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GroupPayrunEditView extends GroupPayrunAddView {
    protected static final AccountingStrings accountingStrings = GWT.create(AccountingStrings.class);
    protected FooterInformer showJournal;

    protected ListingFilterParameter filterParameter;

    public GroupPayrunEditView(Integer id) {
        super("edit");
        setDescription(property.getPlural(payrollStrings.groupPayruns()));
        this.id = id;
    }

    public GroupPayrunEditView(String name) {
        super(name);
    }

    @Override
    protected void loadMainData() {
        PayslipFilter filter = new PayslipFilter();
        filter.setObjectID(id);
        filter.setFromExisting(false);
        LoadingPanel.loading(true);

        payrollService.getPayslipTableSimple(filter, new AbstractAsyncCallback<GroupPayrunData>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(GroupPayrunData result) {
                groupPayrunData = result;
                initializeForm();
                show();
                setValues();
                loadTableData();
            }
        });
    }

    @Override
    protected void initializeForm() {
        super.initializeForm();
        initTotalTable();
    }

    private void initTotalTable() {
        totalLabel = new HTML(wfmStrings.total());
        totalInBaseLabel = new HTML(wfmStrings.total());

        totalAmount = new ExtendedHTMLCell(PayrollClientUtils.format(BigDecimal.ZERO));
        totalInBaseAmount = new ExtendedHTMLCell(PayrollClientUtils.format(BigDecimal.ZERO));

        totalTable = new ReceiptTable();
        totalTable.clear();
        totalTable.removeShippingBody();
        totalTable.addGrossItem(totalLabel, totalAmount);

        addTotalTable();
    }

    protected void addTotalTable() {
        addField(PAYROLL_STARTER.TOTAL_PANEL, totalTable);
    }

    protected Object[] getWidgets(SinglePayrunItem item) {
        Object[] widgets = super.getWidgets(item);
        CustomCellLabel status = new CustomCellLabel(item.getStatus());
        widgets[widgets.length] = status;

        return widgets;
    }

    protected void setColumns() {
        super.setColumns();
        columnsMap.put(PayrollContants.STATUS, new ColumnConfig(CustomCell.class, PayrollContants.STATUS, wfmStrings.status(), 75));
    }

    protected void setValues() {
        if (groupPayrunData.getMonthID() != null) {
            month.setSelected(groupPayrunData.getMonthID());
        }
        if (groupPayrunData.getYear() != null) {
            year.setSelected(groupPayrunData.getYear());
        } else {
            year.setSelected(Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(new Date())));
        }
        if (groupPayrunData.getFrequency() != null) {
            frequency.setSelected(groupPayrunData.getFrequency());
        } else {
            frequency.setSelected(1);
        }
        if (groupPayrunData.getApprover() != null) {
            approver.addItem(groupPayrunData.getApprover());
            approver.setSelected(groupPayrunData.getApprover());
            onApproverLookupSelected();
        }
        if (groupPayrunData.getPayrollBatchItem() != null) {
            payrollBatchLookUp.addItem(groupPayrunData.getPayrollBatchItem());
            payrollBatchLookUp.setSelected(groupPayrunData.getPayrollBatchItem());
            typeListBox.setSelected(TYPE_GROUP);
            enableGroupOrProject(true, false);
        } else if (groupPayrunData.getProjectItem() != null) {
            projectLookUp.addItem(groupPayrunData.getProjectItem());
            projectLookUp.setSelected(groupPayrunData.getProjectItem());
            typeListBox.setSelected(TYPE_PROJECT);
            enableGroupOrProject(false, false);
        } else if (groupPayrunData.getLocationItem() != null) {
            locationLookUp.addItem(groupPayrunData.getLocationItem());
            locationLookUp.setSelected(groupPayrunData.getLocationItem());
            typeListBox.setSelected(TYPE_LOCATION);
            enableGroupOrProject(false, true);
        }
        if (groupPayrunData.getProcessDate() != null) {
            processDate.setDate(groupPayrunData.getProcessDate().getNonConvertedDate());
        }
        if (groupPayrunData.getTotalAmount() != null) {
            totalAmount.setText(PayrollClientUtils.format(groupPayrunData.getTotalAmount()));
            totalAmount.setAmount(groupPayrunData.getTotalAmount());
        }
        if (groupPayrunData.getCurrency() != null && groupPayrunData.isEnabledMultiCurrency()) {
            totalLabel.setHTML(wfmMessages.total(groupPayrunData.getCurrency().getName()));
            totalInBaseLabel.setHTML(wfmMessages.total(groupPayrunData.getCurrencyName()));
            if (groupPayrunData.getTotalInBase() != null) {
                totalInBaseAmount.setText(PayrollClientUtils.format(groupPayrunData.getTotalInBase()));
                totalInBaseAmount.setAmount(groupPayrunData.getTotalInBase());
            }
            totalTable.clearTotalItems();
            totalTable.addItem(totalLabel, totalAmount);
            totalTable.addGrossItem(totalInBaseLabel, totalInBaseAmount);
        } else {
            totalLabel.setHTML(wfmMessages.total(groupPayrunData.getCurrencyName()));
        }
        currencyWidget.setCurrency(groupPayrunData.getCurrency() != null
                ? groupPayrunData.getCurrency().getId()
                : null, groupPayrunData.getExchangeRate());
        if (groupPayrunData.getPaymentMethods() != null) {
            paymentMethodListBox.setItems(groupPayrunData.getPaymentMethods().toArray(new SelectItem[]{}));
            if (groupPayrunData.getPayMethod() != null) {
                paymentMethodListBox.setSelected(groupPayrunData.getPayMethod());
            }
        }
    }

    protected ListingFilterParameter getFilterParameter() {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        filterParameter.setStart(Optional.ofNullable(tableStart).orElse(0));
        filterParameter.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(10));
        filterParameter.setSearchKey(tableSearchBox.getText());
        filterParameter.setGroupPayrunID(id);
        return filterParameter;
    }

    @Override
    protected void loadTableData() {
        ListingFilterParameter filterParameter = getFilterParameter();

        payrollService.getPayslipTableItemsList(filterParameter, new AbstractAsyncCallback<PayrolTableItemListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PayrolTableItemListResult result) {
                setPaginationData(result);
                setTableData(result);
                setShowJournalData();
            }
        });
    }

    protected void setTableData(PayrolTableItemListResult result) {
        itemMap.clear();
        employeeTable.removeAllRows();
        for (SinglePayrunItem item : result.getList()) {
            employeeTable.addRow(getWidgets(item));
        }
        if (result.getTotal() > 0) {
            employeeTable.addRow(getTotalWidgets(result, null));
        }
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = super.getFooterRightSideWidgets();
        rightWidgets.add(getExportOptions());

        return rightWidgets;
    }

    @Override
    protected void initButtons() {
        if (Constants.PAYRUN_STATUS_DRAFT.equals(groupPayrunData.getStatusCode())) {
            WfmButton2 saveAsDraftButton = new WfmButton2(wfmStrings.saveAsDraft(), BTN_DEFAULT_OUTLINE);
            saveAsDraftButton.addClickHandler(event -> save(Constants.PAYRUN_STATUS_DRAFT));
            buttonsPanel.add(saveAsDraftButton);
        } else if (Constants.PAYRUN_STATUS_PENDING.equals(groupPayrunData.getStatusCode()) && groupPayrunData.getPendingItemIds() != null && groupPayrunData.getPendingItemIds().size() > 0) {
            WfmButton2 pendingButton = new WfmButton2("View pending payslips", BTN_DEFAULT_OUTLINE);
            pendingButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("payslipTable|pending/" + id, "Pending for " + groupPayrunData.getMonth()));
            buttonsPanel.add(pendingButton);
        }
    }

//    protected MaterialMenuBar getExportOptions() {
//        MaterialMenuBar showMenuBar = new MaterialMenuBar();
//        showMenuBar.setClass("dropdown-kit--arrow--top dropdown-flow--left");
//
//        MaterialLink showLink = new MaterialLink(wfmStrings.print());
//        showLink.addStyleName(BTN_DEFAULT_OUTLINE);
//
//        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
//        showMenuContainer.setClass("dropdown-content--2");
//        showMenuContainer.setBelowOrigin(true);
//
//        //pdf button
//        MaterialLink pdfVersion = getPdfVersion();
//        pdfVersion.ensureDebugId("pdf_button");
//
//        Div wrapper = new Div("java-wrap");
//
//        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
//        mdp.setHover(true);
//        mdp.setHoverable(true);
//
//        mdp.add(GroupPayrunEditView.this::getPortraitLink);
//        mdp.add(GroupPayrunEditView.this::getLandscapeLink);
//
//        wrapper.add(mdp);
//        setPDFListener();
//        pdfVersion.add(wrapper);
//        showMenuContainer.add(pdfVersion);
//
//        //excel button
//        MaterialLink exportExl = new MaterialLink();
//        exportExl.addStyleName("hasicon--left");
//        Icon exlIcon = new Icon();
//        exlIcon.setClass("ficon--file-excel");
//        exportExl.add(exlIcon);
//        exportExl.setText(wfmStrings.excel());
//        exportExl.addClickHandler((event) -> {
//            excelVersion(exportPanel, false);
//        });
//        showMenuContainer.add(exportExl);
//
//        //sif button
//        MaterialLink exportSif = new MaterialLink();
//        exportExl.addStyleName("hasicon--left");
//        Icon sifIcon = new Icon();
//        sifIcon.setClass("ficon--file-plus");
//        exportSif.add(sifIcon);
//        exportSif.setText("SIF");
//        exportSif.addClickHandler((event) -> {
//            generateReport(exportPanel, true);
//        });
//        showMenuContainer.add(exportSif);
//
//        showLink.add(showMenuContainer);
//        showMenuBar.add(showLink);
//
//        /*List<SplitButtonItem> items = new ArrayList<>();
//        SplitButtonItem pdfItem = new SplitButtonItem("PDF", wfmStrings.print(), () -> pdfVersion(exportPanel, false, null), true);
//        items.add(pdfItem);
//
//        SplitButtonItem excelItem = new SplitButtonItem("EXCEL", wfmStrings.excel(), () -> excelVersion(exportPanel, false));
//        items.add(excelItem);
//
//        SplitButtonItem siftem = new SplitButtonItem("SIF", "SIF", () -> generateReport(exportPanel, true));
//        items.add(siftem);
//
//        SplitButton printButtons = new SplitButton(wfmStrings.export(), 97, Constants.BTN_DEFAULT_OUTLINE, true);
//        printButtons.addItemList(items);
//        Div printWrapper = new Div();
//        printWrapper.add(printButtons);*/
//
//        return showMenuBar;
//    }


    protected MaterialMenuBar getExportOptions() {
        List<SplitButtonItem> export = new ArrayList<>();

        SplitButtonItem pdfPortrait = new SplitButtonItem("PDF_PORTRAIT", wfmStrings.pdfPortrait(),
                () -> pdfVersion(exportPanel, false, null, false),true);
        export.add(pdfPortrait);

        export.add(new SplitButtonItem("SIF", "SIF",
                () -> generateReport(exportPanel, true)));

        export.add(new SplitButtonItem("EXCEL", wfmStrings.excel(),
                () -> excelVersion(exportPanel, false)));

        export.add(new SplitButtonItem("PDF_LANDSCAPE", wfmStrings.pdfLandscape(),
                () -> pdfVersion(exportPanel, false, null, true)));

        SplitButton exportButton = new SplitButton(wfmStrings.export(), 97, Constants.BTN_DEFAULT_OUTLINE, true);
        exportButton.addItemList(export);
        exportButton.ensureDebugId("export_button");

        MaterialMenuBar menuBar = new MaterialMenuBar();
        menuBar.add(exportButton);
        return menuBar;
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.
                    portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            pdfVersion(exportPanel, false, null, false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            pdfVersion(exportPanel, false, null, true);
        });
    }

    protected void saveTableItem(Integer rowId, Boolean dateChange, SinglePayrunItem singlePayrunItem) {
        LoadingPanel.loading(true);
        payrollService.updateSinglePayrollItem(singlePayrunItem, dateChange, new AbstractAsyncCallback<SinglePayrunItem>() {
            @Override
            public void onFailure(final Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final SinglePayrunItem result) {
                super.onSuccess(result);
                LoadingPanel.loading(false);
                if (result != null) {
                    setTotalData(result.getTotalTO());
                    employeeTable.addRow(rowId, getWidgets(result));
                    employeeTable.addRow(employeeTable.getRowCount() - 1, getTotalWidgets(null, result.getTotalTO()));
                }
            }
        });
    }

    protected void deleteTableItem(Integer rowId) {
        LinkCellWidget employee = (LinkCellWidget) employeeTable.getColumnById(rowId, GroupPayrunContants.EMPLOYEE);
        SinglePayrunItem singlePayrunItem = itemMap.get(employee.getItem().getId());

        LoadingPanel.loading(true);
        payrollService.deleteSinglePayrun(singlePayrunItem.getObjectID(), new AbstractAsyncCallback<PayrollTotalTO>() {
            @Override
            public void onFailure(final Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final PayrollTotalTO result) {
                super.onSuccess(result);
                LoadingPanel.loading(false);
                setTotalData(result);
                employeeTable.addRow(employeeTable.getRowCount() - 1, getTotalWidgets(null, result));
            }
        });
    }

    protected void setTotalData(PayrollTotalTO totalTO) {
        if (totalTO == null) {
            totalAmount.setHTML(PayrollClientUtils.format(BigDecimal.ZERO));
            return;
        }
        totalAmount.setHTML(PayrollClientUtils.format(Optional.ofNullable(totalTO.getTotalAmount()).orElse(BigDecimal.ZERO)));
        if (groupPayrunData.isEnabledMultiCurrency()) {
            totalInBaseAmount.setHTML(PayrollClientUtils.format(Optional.ofNullable(totalTO.getTotalAmount())
                    .orElse(BigDecimal.ZERO).multiply(Optional.ofNullable(currencyWidget.getExchangeRate()).orElse(BigDecimal.ONE))));
        }
    }

    private void pdfVersion(MaterialPanel hp, boolean fromSummary, Integer templateId, boolean landscape) {
        GroupPayrunRequestObject rq = new GroupPayrunRequestObject(id, (fromSummary ? Constants.VIEW : Constants.ADD), templateId);
        rq.setIS_LANDSCAPE(landscape);
        HashMap<String, String> parametrs = rq.getRequestParams();
        String pdfURL = CommandConstants.PDF_URL + "/groupPayrunViewPDFHandler";

        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private void excelVersion(MaterialPanel hp, boolean fromSummary) {
        ListingFilterParameter filter = new ListingFilterParameter();

        filter.setObjectId(id);
        filter.setViewType(fromSummary ? Constants.VIEW : Constants.ADD);
        HashMap<String, String> parametrs = filter.getRequestParams();
        String excelURL = CommandConstants.COMMON_URL + "/groupPayrunViewExcelHandler";

        Utils.sendPDFOrExcelRequest(hp, excelURL, parametrs, "_blank");
    }

    private void generateReport(Panel hp, boolean isSifFile) {
        StringBuilder actionUlr = new StringBuilder(CommandConstants.COMMON_URL);

        if (isSifFile) {
            actionUlr.append("/generateSifFile?objectId=");
        } else {
            actionUlr.append("/downloadPayslipExcel?objectId=");
        }
        PostFormPanel post = new PostFormPanel(actionUlr.append(id).toString(), "_blank");

        hp.add(post);
        post.submit();
    }

    Object[] getTotalWidgets(PayrolTableItemListResult item, PayrollTotalTO totals) {
        List<Widget> widgets = new ArrayList<>();

        widgets.add(new LinkCellWidget(wfmStrings.total(), null));

        CustomCellLabel basicSalary = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
        basicSalary.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        basicSalary.setText(PayrollClientUtils.format(item != null ? item.getBasicSalary() : totals.getBasicSalary()));
        widgets.add(basicSalary);

        if (columnsMap.containsKey(PayrollContants.ALLOWANCE)) {
            CustomCellLabel allowanceCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            allowanceCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            allowanceCell.setText(PayrollClientUtils.format(item != null ? item.getAllowance() : totals.getAllowance()));
            widgets.add(allowanceCell);
        }

        if (columnsMap.containsKey(PayrollContants.PENSION)) {
            CustomCellLabel pension = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            pension.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            pension.setText(PayrollClientUtils.format(item != null ? item.getPensionAmount() : totals.getPension()));
            widgets.add(pension);
        }

        if (columnsMap.containsKey(PayrollContants.EMPLOYER_CONTRIBUTION)) {
            CustomCellLabel employerContribution = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            employerContribution.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            employerContribution.setText(PayrollClientUtils.format(item != null ? item.getEmployeeContribution() : BigDecimal.ZERO));
            widgets.add(employerContribution);
        }

        if (columnsMap.containsKey(PayrollContants.DEDUCTION)) {
            CustomCellLabel deductionCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            deductionCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            deductionCell.setText(PayrollClientUtils.format(item != null ? item.getDeduction() : totals.getDeduction()));
            widgets.add(deductionCell);
        }

        if (columnsMap.containsKey(PayrollContants.TAX)) {
            CustomCellLabel taxCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            taxCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            taxCell.setText(PayrollClientUtils.format(item != null ? item.getTax() : totals.getDeduction()));
            widgets.add(taxCell);
        }

        if (Utils.isEnableAccountingModule() && columnsMap.containsKey(PayrollContants.EXPENSE)) {
            CustomCellLabel expenseCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            expenseCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            expenseCell.setText(PayrollClientUtils.format(item != null ? item.getEmployeeExpenses() : totals.getExpense()));
            widgets.add(expenseCell);
        }

        CustomCellLabel totalSalary = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
        totalSalary.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        totalSalary.setText(PayrollClientUtils.format(item != null && item.getTotalTO() != null ? item.getTotalTO().getTotalAmount() : totals.getTotalAmount()));

        widgets.add(totalSalary);

        CustomCellLabel status = new CustomCellLabel("");
        widgets.add(status);

        return widgets.toArray(new Object[]{});
    }

    @Override
    protected void disableFields() {
        payrollBatchLookUp.setEnabled(false);
        projectLookUp.setEnabled(false);
        currencyWidget.setEnabled(false);
        typeListBox.setEnabled(false);
        month.setEnabled(false);
        year.setEnabled(false);
    }

    @Override
    protected void loadPaymentMethods() {
    }

    @Override
    public String getFormType() {
        return LayoutRPC.EDIT;
    }

    void setShowJournalData() {
        showJournal.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + groupPayrunData.getObjectID() + "/GROUP_PAYRUN", accountingStrings.reportView() + ": " + groupPayrunData.getMonth(), accountingStrings.reportView() + ": " + groupPayrunData.getMonth()));
        showJournal.setBadgeCount(groupPayrunData.getTableItems().length);
    }
}
