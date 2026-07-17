package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AdditionalPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.AdditionalPaymentLeaveRequestWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment.AdditionalPaymentItemModal;
import com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment.PaymentCalculationSideNavBox;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Small;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdditionalPaymentSummaryView extends CustomForm2 implements Colapse, Constants {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private final String GROUP_TYPE = "group";
    private final String EMPLOYEE_TYPE = "employee";
    private final String DEPARTMENT_TYPE = "department";
    private final String LOCATION_TYPE = "location";
    private final String SUPERVISOR_TYPE = "supervisor";
    private final Integer objectID;
    private final Integer employeeId;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private HTML lookUp, showInPayslip, categoryLookUp, referenceTextBox, defaultDateWidget, period, paymentType, approver, fixedAmount;
    private EditableTable paymentsTable;
    private AdditionalPaymentItemModal additionalPaymentItemModal;
    private String statusCode;
    private AdditionalPayment data;
    private HTML totalLabel, totalAmount;
    private ReceiptTable totalsTable;
    private WfmButton2 submitButton, approveButton, declineButton, editButton, paymentButton;
    private SplitButton pdfButton;
    private MaterialLink exportExl;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private NoteHistoryWidget noteHistoryWidget;
    private PaymentCalculationSideNavBox calculationSideNavBox;
    private FormHasCustomField customFieldUtil;

    private DataListBox tableLimitListBox;
    Integer tableStart = 0;
    private TextBox tableCurrentBox;
    private Integer totalTableItems = 0;
    private Integer tableCurrent = 0;
    private MaterialLink tablePagingResult;
    private TextBox tableSearchBox;
    private ListingFilterParameter fp;
    private FooterUploadPanel footerUploadPanel;


    public AdditionalPaymentSummaryView(Integer objectID, String statusCode, Integer employeeId) {
        super(ADDITIONAL_PAYMENT_LIST);
        setDescription(property.getSingular(wfmStrings.additionalPayment()));
        this.objectID = objectID;
        this.statusCode = statusCode;
        this.employeeId = employeeId;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.AdditionalPayment, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
            }
        });
        addFormListeners();
        return null;
    }

    @Override
    protected void registerFields() {
        initPaginationWidgets();
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYROLL_PAYMENT_ADD, AdditionalPaymentSummaryView.this, (sender, args) -> {
            clear();
            onInitialize();
        });
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ListingFilterParameter fp = getListingFilterParam();
        LoadingPanel.loading(true);
        PayrollService.App.get().getAdditionalPaymentData(fp, new AsyncCallback<AdditionalPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(AdditionalPayment additionalPayment) {
                LoadingPanel.loading(false);
                data = additionalPayment;
                if (data.getOverallStatus() != null) {
                    statusCode = data.getOverallStatus().getCode();
                }
                boolean isBeforeLockDate = (Utils.isAdditionalPaymentsLocked() && additionalPayment.getDefaultDate() != null && DateUtils.getTransactionLockDate().after(additionalPayment.getDefaultDate().getNonConvertedDate()));
                drawForm();
                getCustomFieldUtil().fillCustomFieldsWithData(data.getCustomFields(), true);
                if (data.isApprover()) {
                    Integer currentApproverId = data.getApprover() != null ? data.getApprover().getId() : null;
                    Integer currentUserId = Utils.getUserID();
                    if (!isBeforeLockDate && PAYMENT_STATUS_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId)) {
                        approveButton.setVisible(true);
                        declineButton.setVisible(true);
                        submitButton.setVisible(false);
                    }

                    editButton.setVisible(!isBeforeLockDate &&
                            Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT)
                            && !(PAYMENT_STATUS_APPROVED.equals(statusCode) || PAYMENT_STATUS_PARTIAL_PAID.equals(statusCode) || PAYMENT_STATUS_PAID.equals(statusCode))
                            && (data.getCreator() != null && currentUserId.equals(data.getCreator().getId())));


                    if (!isBeforeLockDate && PAYMENT_STATUS_REJECTED.equals(statusCode) && data.getCreator() != null && currentUserId.equals(data.getCreator().getId())) {
                        submitButton.setVisible(true);
                    }
                } else {
                    editButton.setVisible(!isBeforeLockDate && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT)
                            && !(PAYMENT_STATUS_APPROVED.equals(statusCode) || PAYMENT_STATUS_PARTIAL_PAID.equals(statusCode) || PAYMENT_STATUS_PAID.equals(statusCode)));
                }

                if (!isBeforeLockDate && !data.isShowInPayslip() && data.isMakePayment() && Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_EDIT)
                        && (PAYMENT_STATUS_APPROVED.equals(statusCode) || PAYMENT_STATUS_PARTIAL_PAID.equals(statusCode))) {
                    paymentButton.setVisible(true);
                }

                if (data.getCategoryType().equals(PayrollConstants.CATEGORY_DEDUCTION) && PAYMENT_STATUS_APPROVED.equals(statusCode)) {
                    paymentButton.setVisible(false);
                }

                List<SplitButtonItem> pdfItems = new ArrayList<>();
                Integer defaultTemplateId = null;
                if (data.getPdfTemplateList() != null && data.getPdfTemplateList().getItems() != null && data.getPdfTemplateList().getItems().length > 0) {
                    for (SelectItem pdfItem : data.getPdfTemplateList().getItems()) {

                        if (pdfItem.isDefaultSelected()) {
                            defaultTemplateId = pdfItem.getId();
                        }
                        pdfItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(pdfItem.getId())));
                    }
                } else {
                    pdfItems.add(new SplitButtonItem("PDF_TEMPLATE_LANDSCAPE", wfmStrings.pdf(), () -> generatePDF(true), true));
                }
                Integer finalDefaultTemplateId = defaultTemplateId;
                SplitButtonItem pdfVersion = new SplitButtonItem("PDF_VERSION", wfmStrings.print(), () -> generatePDF(finalDefaultTemplateId), true);
                exportExl = new MaterialLink();
                exportExl.addStyleName("hasicon--left");
                Icon exlIcon = new Icon();
                exlIcon.setClass("ficon--file-excel");
                exportExl.add(exlIcon);
                exportExl.setText(wfmStrings.xlsCaption());
                exportExl.addClickHandler((event) -> {
                    excelVersion(exportPanel, true);
                });
                SplitButtonItem splitButtonItem = new SplitButtonItem("XLS_VERSION", wfmStrings.excel(), () -> excelVersion(exportPanel, false), true);
                pdfItems.add(splitButtonItem);
                pdfItems.add(pdfVersion);
                pdfButton.addItemList(pdfItems);

                if (data.getCalculationDetails() != null && data.getCalculationDetails().size() > 0) {
                    calculationSideNavBox = new PaymentCalculationSideNavBox(data.getCalculationDetails(), data.getEmployeeDataDetail());
                    FooterInformer calculationHistory = new FooterInformer(SvgEnum.messageSquare, wfmStrings.calculationDetails());
                    calculationHistory.setInitialClasses("informer-item history-notes-container");
                    calculationHistory.addClickHandler(click -> calculationSideNavBox.show());

                    footer.addToLeftSide(calculationHistory);
                }

                if (data.getLeaveRequestId() != null || (data.getFromId() != null && data.getFromType() != null)) {
                    FooterInformer link = new FooterInformer(SvgEnum.link, wfmStrings.links(),null);
                    link.setBadgeCount(1);

                    link.addClickHandler(event -> new AdditionalPaymentLeaveRequestWidget(data.getFromObject(), data.getFromType(), data.getLeaveRequestId() != null).show());
                    footer.addToLeftSide(link);
                }
                setPaginationData(data.getTotalItems());
                if (data.getItems() != null && data.getItems().size() > 0) {
                    for (PaymentDeductionObject item : data.getItems()) {
                        paymentsTable.addRow(getWidgets(item));
                    }
                }
            }
        });
    }

    private ListingFilterParameter getListingFilterParam() {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setObjectId(objectID);
        fp.setEmployeeId(employeeId);
        fp.setStart(Optional.ofNullable(tableStart).orElse(0));
        fp.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20));
        if (tableSearchBox != null) {
            fp.setSearchKey(tableSearchBox.getText());
        }
        return fp;
    }

    private void excelVersion(MaterialPanel hp, boolean fromSummary) {
        ListingFilterParameter filter = new ListingFilterParameter();

        filter.setObjectId(objectID);
        filter.setViewType(fromSummary ? Constants.VIEW : Constants.ADD);
        HashMap<String, String> parametrs = filter.getRequestParams();
        String excelURL = CommandConstants.COMMON_URL + "/additionalPaymentViewExcelHandler";

        Utils.sendPDFOrExcelRequest(hp, excelURL, parametrs, "_blank");
    }

    private void drawForm() {
        drawMainSection();
        drawTableSection();

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
    }

    private void drawMainSection() {

        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        String name = "";
        lookUp = initHTML();
        if (EMPLOYEE_TYPE.equals(data.getEntityType())) {
            name = formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.employee();
            if (data.getEmployee() != null) {
                lookUp.setHTML(data.getEmployee().getName());
            }
        } else if (DEPARTMENT_TYPE.equals(data.getEntityType())) {
            name = formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.department();
            if (data.getDepartment() != null) {
                lookUp.setHTML(data.getDepartment().getName());
            }
        } else if (LOCATION_TYPE.equals(data.getEntityType())) {
            name = formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            if (data.getLocation() != null) {
                lookUp.setHTML(data.getLocation().getName());
            }
        } else if (GROUP_TYPE.equals(data.getEntityType())) {
            name = formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.group();
            if (data.getPayrollBatch() != null) {
                lookUp.setHTML(data.getPayrollBatch().getName());
            }
        } else if (SUPERVISOR_TYPE.equals(data.getEntityType())) {
            name = formPropertyMap.get(EMPLOYEE).isChanged() ? formPropertyMap.get(EMPLOYEE).getTitle() : wfmStrings.supervisor();
            if (data.getSupervisor() != null) {
                lookUp.setHTML(data.getSupervisor().getName());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(EMPLOYEE) != null) {
            addField(EMPLOYEE, lookUp, getTitle(name));
        } else {
            addField(EMPLOYEE, lookUp, getTitle(name));
        }

        defaultDateWidget = initHTML();
        if (data.getDefaultDate() != null) {
            defaultDateWidget.setHTML(DateUtils.getDateFormatShort(data.getDefaultDate().getNonConvertedDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE) != null) {
            addField(AdditionalPaymentImport.PAYMENT_DATE, defaultDateWidget, getTitle(formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).isChanged() ? formPropertyMap.get(AdditionalPaymentImport.PAYMENT_DATE).getTitle() : wfmStrings.paymentDate()));
        } else {
            addField(AdditionalPaymentImport.PAYMENT_DATE, defaultDateWidget, getTitle(wfmStrings.paymentDate()));
        }


        categoryLookUp = initHTML();
        if (data.getDefaultCategory() != null) {
            categoryLookUp.setHTML(data.getDefaultCategory().getName());
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryLookUp, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category()));
        } else {
            addField(CATEGORY, categoryLookUp, getTitle(wfmStrings.category()));
        }

        period = initHTML();
        String periodTxt = "";
        if (data.getMonthID() != null) {
            periodTxt += getMonthItems(data.getMonthID());
        }
        if (data.getYear() != null) {
            periodTxt += ", " + data.getYear().toString();
        }
        period.setHTML(periodTxt);

        if (formPropertyMap != null && formPropertyMap.get(PERIOD) != null) {
            addField(PERIOD, period, formPropertyMap.get(PERIOD).isChanged() ? formPropertyMap.get(PERIOD).getTitle() : wfmStrings.period());
        } else {
            addField(PERIOD, period, wfmStrings.period());
        }

        referenceTextBox = initHTML();
        referenceTextBox.setHTML(data.getReference());
        if (formPropertyMap != null && formPropertyMap.get(REFERENCE) != null) {
            addField(REFERENCE, referenceTextBox, formPropertyMap.get(REFERENCE).isChanged() ? formPropertyMap.get(REFERENCE).getTitle() : wfmStrings.reference());
        } else {
            addField(REFERENCE, referenceTextBox, getTitle(wfmStrings.reference()));
        }

        paymentType = initHTML();
        if (data.getPaymentType() != null) {
            paymentType.setHTML("FIXED_AMOUNT".equals(data.getPaymentType()) ? wfmStrings.fixedAmount() : BASIC_SALARY.equals(data.getPaymentType()) ? wfmStrings.basicSalary() : "BASIC_SALARY_ALLOWANCE".equals(data.getPaymentType()) ? wfmStrings.basicAllowancePay() : wfmStrings.notAvailable());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE) != null) {
            addField(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE, paymentType, formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE).getTitle() : wfmStrings.paymentType());
        } else {
            addField(CustomFormConstants.ACCOUNTING.PAYMENT_TYPE, paymentType, wfmStrings.paymentType());
        }


        approver = initHTML();
        if (data.getApprover() != null) {
            approver.setHTML(data.getApprover().getName());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVERS) != null) {
            addField(CustomFormConstants.APPROVERS, approver, getTitle(formPropertyMap.get(CustomFormConstants.APPROVERS).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVERS).getTitle() : wfmStrings.approver()));
        } else {
            addField(APPROVERS, approver, getTitle(wfmStrings.approver(), true));
        }
        fixedAmount = initHTML();
        fixedAmount.setHTML("FIXED_AMOUNT".equals(data.getPaymentType()) && data.getFixedAmount() != null ? PayrollClientUtils.format(data.getFixedAmount()) : data.getPercentage() != null ? PayrollClientUtils.format(data.getPercentage()) : "");

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AMOUNT) != null) {
            addField(CustomFormConstants.AMOUNT, fixedAmount, formPropertyMap.get(CustomFormConstants.AMOUNT) != null && formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : "FIXED_AMOUNT".equals(data.getPaymentType()) ? wfmStrings.fixedAmount() : wfmStrings.percentage());
        } else {
            addField(CustomFormConstants.AMOUNT, fixedAmount, "FIXED_AMOUNT".equals(data.getPaymentType()) ? wfmStrings.fixedAmount() : wfmStrings.percentage());
        }

        showInPayslip = initHTML();
        showInPayslip.setText(data.isShowInPayslip() ? wfmStrings.yes() : wfmStrings.no());
        addField("SHOW_PAYSLIP", showInPayslip, formPropertyMap != null && formPropertyMap.get("SHOW_PAYSLIP") != null && formPropertyMap.get("SHOW_PAYSLIP").isChanged() ? formPropertyMap.get("SHOW_PAYSLIP").getTitle() : wfmStrings.showInPayslip());
        initSearchPanel();
    }

    private String getMonthItems(Integer monthId) {
        switch (monthId) {
            case 0:
                return wfmStrings.january();
            case 1:
                return wfmStrings.february();
            case 2:
                return wfmStrings.march();
            case 3:
                return wfmStrings.april();
            case 4:
                return wfmStrings.may();
            case 5:
                return wfmStrings.june();
            case 6:
                return wfmStrings.july();
            case 7:
                return wfmStrings.august();
            case 8:
                return wfmStrings.september();
            case 9:
                return wfmStrings.october();
            case 10:
                return wfmStrings.november();
            case 11:
                return wfmStrings.december();
        }
        return "";
    }

    private void drawTableSection() {
        if (data.getColumnConfigs() != null) {
            columnsMap.clear();
            for (ColumnConfigs cc : data.getColumnConfigs()) {
                if (cc.isSelected()) {
                    columnsMap.put(cc.getCode(), cc);
                }
            }
        }
        initTables();
        initTotals();
        updateTotal();

        GColumn cTotalTable = new GColumn(GColumnEnum.COL_2, totalsTable);
        cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_10);

        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, paymentsTable)));
        itemsTableContainer.add(new GRow(cTotalTable));
        addField(CustomFormConstants.ITEMS, itemsTableContainer, null, true);
    }

    public void initTables() {
        paymentsTable = new EditableTable(getColumns(), false, false);
    }


    private void initTotals() {
        totalLabel = new HTML("<b>" + wfmStrings.total() + "</b>");
        totalAmount = new HTML(data.getTotal() != null ? "<b>" + PayrollClientUtils.format(data.getTotal()) + "</b>" : "0");

        totalsTable = new ReceiptTable();
        totalsTable.clear();
        totalsTable.removeShippingBody();
        totalsTable.addGrossItem(totalLabel, totalAmount);
    }

    private void updateTotal() {
        if (!data.isShowInPayslip()) {
            BigDecimal dueAmount = data.getTotal() != null ? data.getTotal() : BigDecimal.ZERO;
            BigDecimal paymentAmount = BigDecimal.ZERO;
            if (data.getPayments() != null && data.getPayments().size() > 0) {
                for (PayrollPayment payment : data.getPayments()) {
                    setPaymentInfoToTable(payment);
                    paymentAmount = paymentAmount.add(payment.getAmount());
                }
                dueAmount = dueAmount.subtract(paymentAmount);
            }
            HTML dueAmountHTML = new HTML(PayrollClientUtils.format(dueAmount));
            totalsTable.setDueAmount(new HTML(wfmStrings.dueAmount()), dueAmountHTML);
        }
    }

    protected void setPaymentInfoToTable(PayrollPayment payment) {
        String title = payment.getObjectID() != null ? wfmStrings.payment() : payrollStrings.singlePayments();
        String action = payment.getObjectID() != null ? "payrollPayment|view/" + payment.getObjectID() : null;
        PaymentInformation paymentInformation = new PaymentInformation(payment, title, action);
        totalsTable.addPaidItem(paymentInformation, (paymentInformation.getAction() != null) ? new MaterialLink(PayrollClientUtils.format(payment.getAmount()), paymentInformation.getAction()) : new HTML(PayrollClientUtils.format(payment.getAmount())));
    }

    @Override
    protected void addButtons() {

        noteHistoryWidget = new NoteHistoryWidget(callback -> PayrollService.App.get().loadPaymentNotes(objectID, callback));
        noteHistoryWidget.setSaveIntoDatabase(noteHistory -> {
            LoadingPanel.loading(true);
            PayrollService.App.get().createPaymentHistoryNote(objectID, noteHistory, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer hisItemId) {
                    noteHistory.setObjectID(hisItemId);
                    LoadingPanel.loading(false);
                }
            });
        });
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");

        footer.addToLeftSide(informer);

        footerUploadPanel = new FooterUploadPanel(Constants.F_ADDITIONAL_PAYMENT, objectID, true);
        if (Utils.hasPermission(PAYROLL_ADDITIONAL_PAYMENT_ATTACHMENT)) {
            footer.addToLeftSide(footerUploadPanel);
        }

        Div filterDiv = new Div("frame__info-paging");
        filterDiv.add(drawPaginationPanel());
        footer.addToLeftSide(filterDiv);

        pdfButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        pdfButton.setVisible(Utils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_PDF));
        addButton(pdfButton);

        editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged((PayrollConstants.CATEGORY_PAYMENT.equals(data.getCategoryType()) ? "additionalPayment|add/add/" : "additionalDeduction|add/add/") + data.getEntityType() + "/" + objectID, data.getReference());
        });
        editButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), BTN_PRIMARY, clickEvent -> save(PAYMENT_STATUS_APPROVED));
        approveButton.setVisible(false);

        declineButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> save(Constants.PAYMENT_STATUS_REJECTED));
        declineButton.setVisible(false);

        submitButton = addButton(Constants.PAYMENT_STATUS_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.PAYMENT_STATUS_SUBMITTED);
        });
        submitButton.setVisible(false);

        paymentButton = addButton(wfmStrings.makePayment(), BTN_PRIMARY, clickEvent -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("payrollPayment|add/add/" + objectID, data.getReference());
        });
        paymentButton.setVisible(false);
    }

    private void save(String status) {
        data.setStatusCode(status);
        data.setFromView(true);

        LoadingPanel.loading(true);
        enableButtons(false);
        PayrollService.App.get().saveAdditionalPayment(data, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButtons(true);
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void onSuccess(Void aVoid) {
                closeTab();
                LoadingPanel.loading(false);
                Info.show(Property.get(ADDITIONAL_PAYMENT_LIST, wfmStrings.messSuccessfullySaved(), PayrollConstants.CATEGORY_PAYMENT.equals(data.getType()) ? wfmStrings.additionalPayment() : payrollStrings.additionalDeduction()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADDITIONAL_PAYMENT_ADD, null, null);

            }
        });

    }

    private void enableButtons(boolean isEnable) {
        declineButton.setEnabled(isEnable);
        submitButton.setEnabled(isEnable);
        approveButton.setEnabled(isEnable);
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ADDITIONAL_PAYMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
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

    @Override
    public String getPropertyCode() {
        return Constants.ADDITIONAL_PAYMENT_LIST;
    }

    private ColumnConfig[] getColumns() {
        List<ColumnConfig> columns = new ArrayList<>();
        for (String cc : columnsMap.keySet()) {
            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;
            switch (cc) {
                case ItemTableConstants.EMPLOYEE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.EMPLOYEE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.employee(), Utils.getColumnWidth(columnConfigs.getWidth() != null && "FIXED_AMOUNT".equals(data.getPaymentType()) ? columnConfigs.getWidth() + 8 : columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.BASIC_SALARY:
                    if (!"FIXED_AMOUNT".equals(data.getPaymentType())) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.BASIC_SALARY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.basicSalary(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns.add(columnConfig);
                    }
                    break;
                case ItemTableConstants.PERCENTAGE:
                    if (!"FIXED_AMOUNT".equals(data.getPaymentType())) {
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PERCENTAGE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.percentage() + "(%)", Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns.add(columnConfig);
                    }
                    break;
                case ItemTableConstants.AMOUNT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.amount(), Utils.getColumnWidth(columnConfigs.getWidth() != null && "FIXED_AMOUNT".equals(data.getPaymentType()) ? columnConfigs.getWidth() + 8 : columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.CATEGORY:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.CATEGORY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.categories(), Utils.getColumnWidth(columnConfigs.getWidth() != null && "FIXED_AMOUNT".equals(data.getPaymentType()) ? columnConfigs.getWidth() + 7 : columnConfigs.getWidth(), 130), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.PAYMENT_DATE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PAYMENT_DATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.paymentDate(), Utils.getColumnWidth(columnConfigs.getWidth() != null && "FIXED_AMOUNT".equals(data.getPaymentType()) ? columnConfigs.getWidth() + 5 : columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.EMPLOYER_CONTRIBUTION:
                    columnConfig = new ColumnConfig(data.isShowInPayslip() ? CustomCell.class : LinkableCell.class, PayrollContants.EMPLOYER_CONTRIBUTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.employerContribution(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.TAX:
                    columnConfig = new ColumnConfig(data.isShowInPayslip() ? CustomCell.class : LinkableCell.class, PayrollContants.TAX, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.tax(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.DEDUCTION:
                    columnConfig = new ColumnConfig(data.isShowInPayslip() ? CustomCell.class : LinkableCell.class, PayrollContants.DEDUCTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.deduction(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case PayrollContants.TOTAL_SALARY:
                    columnConfig = new ColumnConfig(CustomCell.class, PayrollContants.TOTAL_SALARY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.total(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnConfigs.getCode(), columnConfigs.getTitle(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), isPixel);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);

                    if (Constants.UI_TYPE_TEXTAREA.equalsIgnoreCase(columnConfigs.getUiType())) {
                        columnConfig.setCustomStyleName("product-description-cell");
                    }
                    break;
            }
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    private Widget[] getWidgets(PaymentDeductionObject item) {
        List<Widget> widgets = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.EMPLOYEE.equals(columnCode)) {
                EmployeeBox employeeBox = new EmployeeBox(item.getId(), item.getEmployee(), item.getCountIncident(), item.getCategoryItem() != null && item.getCategoryItem().isNonMoneyType());
                employeeBox.setEnabled(true);
                employeeBox.setReadOnly(true);
                employeeBox.addStyleName(DEFAULT_WIDTH);
                employeeBox.setStyleName("file--AdditionalPaymentUIBinder");
                widgets.add(employeeBox);
            } else if (ItemTableConstants.AMOUNT.equals(columnCode)) {
                ExtendedHTML amountTextBox = new ExtendedHTML();
                amountTextBox.setHTML(PayrollClientUtils.format(item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO));
                widgets.add(amountTextBox);
            } else if (ItemTableConstants.CATEGORY.equals(columnCode)) {
                ExtendedHTML categoryLookUp = new ExtendedHTML();
                categoryLookUp.setHTML(item.getCategoryItem() != null ? item.getCategoryItem().getName() : wfmStrings.na());
                widgets.add(categoryLookUp);
            } else if (ItemTableConstants.BASIC_SALARY.equals(columnCode)) {
                if (!"FIXED_AMOUNT".equals(data.getPaymentType())) {
                    BigDecimal amount = "BASIC_SALARY_ALLOWANCE".equals(data.getPaymentType()) ? item.getBasicPlusAllowance() : item.getEmployeeBasicSalary();
                    amount = amount != null ? amount : BigDecimal.ZERO;

                    ExtendedHTML basicSalaryTextBox = new ExtendedHTML();
                    basicSalaryTextBox.setHTML(PayrollClientUtils.format(amount));
                    widgets.add(basicSalaryTextBox);
                }

            } else if (ItemTableConstants.PERCENTAGE.equals(columnCode)) {
                if (!"FIXED_AMOUNT".equals(data.getPaymentType())) {
                    BigDecimal percentage = item.getPercentage() != null ? item.getPercentage() : BigDecimal.ZERO;

                    ExtendedHTML percentageTextBox = new ExtendedHTML();
                    percentageTextBox.setHTML(PayrollClientUtils.format(percentage));
                    widgets.add(percentageTextBox);
                }
            } else if (ItemTableConstants.PAYMENT_DATE.equals(columnCode)) {

                ExtendedHTML paymentDate = new ExtendedHTML();
                if (item.getAdditionalPaymentDate() != null) {
                    paymentDate.setHTML(DateUtils.getDateFormatShort(item.getAdditionalPaymentDate().getNonConvertedDate()));
                }
                widgets.add(paymentDate);
            } else if (PayrollContants.EMPLOYER_CONTRIBUTION.equals(columnCode)) {
                if (!data.isShowInPayslip()) {
                    LinkCellWidget employerContribution = new LinkCellWidget(PayrollClientUtils.format(BigDecimal.ZERO), () -> {

                        if (data.isShowInPayslip() || item.getCategoryItem() == null) {
                            if (data.isShowInPayslip()) {
                                Info.warn("The Show in payslip field should be unselectable");
                            } else {
                                Info.warn("Category field should be selectable");
                            }
                        } else {
                            showTaxModal(item);
                        }
                    });
                    if (item != null && item.getEmployerContribution() != null) {
                        employerContribution.setText(PayrollClientUtils.format(item.getEmployerContribution()));
                    }
                    widgets.add(employerContribution);
                } else {
                    ExtendedHTML employerContribution = new ExtendedHTML();
                    employerContribution.setHTML(PayrollClientUtils.format(item.getEmployerContribution() != null ? item.getEmployerContribution() : BigDecimal.ZERO));
                    widgets.add(employerContribution);
                }
            } else if (PayrollContants.TAX.equals(columnCode)) {

                if (!data.isShowInPayslip()) {
                    LinkCellWidget taxCell = new LinkCellWidget(PayrollClientUtils.format(BigDecimal.ZERO), () -> {

                        if (data.isShowInPayslip() || item.getCategoryItem() == null) {
                            if (data.isShowInPayslip()) {
                                Info.warn("The Show in payslip field should be unselectable");
                            } else {
                                Info.warn("Category field should be selectable");
                            }
                        } else {
                            showTaxModal(item);
                        }
                    });
                    if (item != null && item.getTax() != null) {
                        taxCell.setText(PayrollClientUtils.format(item.getTax()));
                    }
                    widgets.add(taxCell);
                } else {
                    ExtendedHTML taxCell = new ExtendedHTML();
                    taxCell.setHTML(PayrollClientUtils.format(item.getTax() != null ? item.getTax() : BigDecimal.ZERO));
                    widgets.add(taxCell);
                }
            } else if (PayrollContants.DEDUCTION.equals(columnCode)) {

                if (!data.isShowInPayslip()) {
                    LinkCellWidget deductionCell = new LinkCellWidget(PayrollClientUtils.format(BigDecimal.ZERO), () -> {

                        if (data.isShowInPayslip() || item.getCategoryItem() == null) {
                            if (data.isShowInPayslip()) {
                                Info.warn("The Show in payslip field should be unselectable");
                            } else {
                                Info.warn("Category field should be selectable");
                            }
                        } else {
                            showTaxModal(item);
                        }
                    });
                    if (item != null && item.getDeduction() != null) {
                        deductionCell.setText(PayrollClientUtils.format(item.getDeduction()));
                    }
                    widgets.add(deductionCell);
                } else {
                    ExtendedHTML deductionCell = new ExtendedHTML();
                    deductionCell.setHTML(PayrollClientUtils.format(item.getDeduction() != null ? item.getDeduction() : BigDecimal.ZERO));
                    widgets.add(deductionCell);
                }
            } else if (PayrollContants.TOTAL_SALARY.equals(columnCode)) {

                CustomCellLabel totalSalary = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
                totalSalary.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                if (item != null && item.getTotalAmount() != null) {
                    totalSalary.setText(PayrollClientUtils.format(item.getTotalAmount()));
                }
                widgets.add(totalSalary);
            } else {
                ExtendedHTML cf = new ExtendedHTML();
                if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                    CompanyCustomFieldItem cfItem = item.getCustomFieldValuesAsMap().get(columnCode);
                    String value = "";
                    if (Constants.DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                        value = cfItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(cfItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                    } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                        value = cfItem.getFieldStringValue() != null ? cfItem.getFieldStringValue() + " % " : "";
                    } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                        String finalValue = "";
                        if (cfItem.getSelectItems() != null && cfItem.getSelectItems().size() > 0) {
                            for (SelectItem selectItem : cfItem.getSelectItems()) {
                                finalValue += selectItem.getName() + "; ";
                            }
                        }
                        value = finalValue;
                    } else {
                        value = cfItem.getFieldStringValue();
                    }
                    cf.setHTML(value);
                    widgets.add(cf);
                }
            }
        }
        return widgets.toArray(new Widget[]{});
    }

    private void showTaxModal(PaymentDeductionObject item) {

        if (additionalPaymentItemModal != null) {
            additionalPaymentItemModal.close();
        }
        additionalPaymentItemModal = new AdditionalPaymentItemModal(item, columnsMap,
                item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO, item.getCategoryItem(), true,
                item.getTaxCategories(), item.getEmployerContributionCategories(), item.getDeductionCategories());
    }

    private Widget drawPaginationPanel() {
        this.initPaginationWidgets();
        GBoxItem limitField = new GBoxItem(tableLimitListBox);
        limitField.setWidth("100px");

        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        MaterialLink prevLink = new MaterialLink();
        prevLink.setStyleName("btn btn--white btn--icon");
        prevLink.add(prevIcon);

        GBoxItem currentItem = new GBoxItem(tableCurrentBox);
        currentItem.addStyleToComponent("paging__currentpage");

        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        MaterialLink nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        nextLink.add(nextIcon);

        GBoxRow row = new GBoxRow();
        row.add(new GBoxItem(tablePagingResult));
        row.add(limitField);
        row.add(new GBoxItem(prevLink));
        row.add(currentItem);
        row.add(new GBoxItem(nextLink));

        prevLink.addClickHandler((event) -> {
            Integer totalPagesSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            tableStart -= totalPagesSize;
            if (tableStart <= 0) {
                tableStart = 0;
            }
            loadTableData();
        });

        nextLink.addClickHandler((event) -> {
            Integer selectedLimit = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            int totalPages = totalTableItems / selectedLimit + (totalTableItems % selectedLimit > 0 ? 1 : 0);
            int currentPage = tableStart / selectedLimit + 1;

            if (currentPage >= totalPages) {
                return;
            }
            tableStart += selectedLimit;
            loadTableData();
        });

        tableLimitListBox.addValueChangeHandler(event -> {
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            loadTableData();
        });
        return row;
    }

    private void initPaginationWidgets() {
        if (tableLimitListBox == null) {
            tableLimitListBox = new DataListBox();
            tableLimitListBox.setWithoutNullLabel(true);
            tableLimitListBox.setItems(new SelectItem[]{
                    new SelectItem(10, "10"),
                    new SelectItem(20, "20"),
                    new SelectItem(30, "30"),
                    new SelectItem(50, "50"),
            });
            tableLimitListBox.setSelected(new SelectItem(20, "20"));
        }
        if (tableCurrentBox == null) {
            tableCurrentBox = new TextBox();
            tableCurrentBox.setStyleName("currLoc form-control");
            tableCurrentBox.setValue(tableCurrent.toString());
        }
        if (tablePagingResult == null) {
            tablePagingResult = new MaterialLink();
            tablePagingResult.setHref("javascript:void(0)");
            tablePagingResult.setClass("btn btn--white");
            tablePagingResult.setText("0 - 0 of 0");
        }
    }

    void setPaginationData(Integer total) {
        if (total == null) {
            total = 0;
        }
        totalTableItems = total;

        int pageSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
        int position = Optional.ofNullable(tableStart).orElse(0);

        tableCurrent = position / pageSize + 1;

        tableCurrentBox.setValue(tableCurrent.toString());
        tablePagingResult.setText((position + 1) + " - " + ((position + pageSize) < totalTableItems ? (position + pageSize) : totalTableItems) + " " + wfmStrings.of() + " " + totalTableItems);
    }

    private void loadTableData() {
        LoadingPanel.loading(true);
        ListingFilterParameter fp = getListingFilterParam();
        fp.setShowSummaryView(true);
        PayrollService.App.get().getAdditionalPaymentItemsData(fp, new AsyncCallback<AdditionalPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AdditionalPayment result) {
                LoadingPanel.loading(false);
                setPaginationData(data.getTotalItems());
                paymentsTable.removeAllRows();
                if (result.getItems() != null && result.getItems().size() > 0) {
                    for (PaymentDeductionObject item : result.getItems()) {
                        paymentsTable.addRow(getWidgets(item));
                    }
                }
            }
        });
    }

    private static class EmployeeBox extends Div implements CustomCellInterface {
        Integer deductionId;
        SelectItem employee;
        Integer incidentCount;
        boolean nonMoneyType;
        Span span = new Span();
        CustomCellTextBox textBox = new CustomCellTextBox();

        public EmployeeBox(Integer deductionId, SelectItem employee, Integer countIncident, boolean nonMoneyType) {
            super();
            this.deductionId = deductionId;
            this.employee = employee;
            this.incidentCount = countIncident;
            this.nonMoneyType = nonMoneyType;

            if (employee != null) {
                if (employee.getDescription() != null && !"".equals(employee.getDescription())) {
                    textBox.setText(employee.getDescription() + " -> " + employee.getName());
                } else {
                    textBox.setText(employee.getName());
                }
            }

            if (incidentCount != null && incidentCount > 0) {
                span.setStyleName("tab-label");
                span.setText(String.valueOf(incidentCount));
                add(span);

                textBox.addFocusHandler(focusEvent -> {
                    if (Utils.hasPermission("HRMS_INCIDENT_LIST")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("starter|incidentList/" + employee.getId() + "/fromEmployeeList/view/");
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT);
                    }
                });
            } else {
                textBox.addFocusHandler(focusEvent -> {
                    if (Utils.hasPermission("HRMS_EMPLOYEE_PROFILE_SUMMARY")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + employee.getId() + "/fromEmployeeList/view/");
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT);
                    }
                });
            }
            add(textBox);
        }

        public void setReadOnly(boolean readOnly) {
            textBox.setReadOnly(readOnly);
        }


        @Override
        public String getDisplayValue() {
            if (Utils.hasGenericAccess(GenericSettingsEnum.SHOW_INCIDENT_COUNT_ADDITIONAL_PAYMENT) && incidentCount != null && incidentCount > 0) {
                return textBox.getText() + " <span class='tab-label'>" + incidentCount + "</span>";
            }
            return textBox.getDisplayValue();
        }

        @Override
        public void setItemValue(Object value) {
            textBox.setItemValue(value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            textBox.setItemFocus(focused);
        }

        public Integer getDeductionId() {
            return deductionId;
        }

        public boolean isNonMoneyType() {
            return this.nonMoneyType;
        }

        public SelectItem getEmployee() {
            return employee;
        }
    }

    private void generatePDF(Integer pdfId) {
        AdditionalPaymentRequestObject requestObject = new AdditionalPaymentRequestObject(data.getObjectID(), null);
        requestObject.setPdfTemplateID(pdfId);
        String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentPdfHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    private void generatePDF(boolean isLandscape) {
        String URL = CommandConstants.PDF_URL + "/additionalPaymentPdfHandler";
        RequestObject requestObject = new RequestObject(data.getObjectID());
        requestObject.setIS_LANDSCAPE(isLandscape);
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, URL, parametrs, "_blank");
    }

    public static int getPriceScale() {
        if (Utils.getAccountingCalculationScale() != null) {
            return Utils.getAccountingCalculationScale();
        }
        return 2;
    }

    protected class PaymentInformation extends FigureWidget {

        private final String action;

        public PaymentInformation(PayrollPayment payment, String title, String action) {
            this.action = action;
            addStyleName("right-label");

            FigCaption figCaption = new FigCaption();
            add(figCaption);

            Div container = new Div();
            figCaption.add(container);

            HorizontalPanelDiv pnlCont = new HorizontalPanelDiv();

            if (payment.getObjectID() != null) {
                SvgIcon trashIcon = new SvgIcon((SvgEnum.trash2));
                MaterialLink removePaymentLink = new MaterialLink();
                removePaymentLink.setClass("btn--icon");
                removePaymentLink.add(trashIcon);
                removePaymentLink.addClickHandler(ch -> deletePayment(payment));
                pnlCont.add(removePaymentLink);
            }

            if (action != null && !action.isEmpty()) {
                MaterialLink detailsLink = new MaterialLink(title, action);
                pnlCont.add(detailsLink);
            } else {
                pnlCont.add(new Span(title));
            }
            container.add(pnlCont);

            figCaption.add(new Small(DateUtils.format(payment.getPaymentDate())));

            SvgIcon svgIcon = new SvgIcon(SvgEnum.check);
            Div iconWrapper = new Div();
            iconWrapper.setClass("icon-wrapp--circle");
            iconWrapper.add(svgIcon);
            add(iconWrapper);
        }

        private void deletePayment(PayrollPayment payment) {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(wfmMessages.sureYouWantToDelete(wfmStrings.payment(), "?"));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    PayrollService.App.get().deletePayrollPayment(payment.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        public void success(Boolean result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_PAYMENT_DELETE, null, AdditionalPaymentSummaryView.this);
                            closeTab();
                            SinksContainerFactory.entryPoint.onHistoryChanged((PayrollConstants.CATEGORY_PAYMENT.equals(data.getCategoryType()) ? "additionalPayment|view/" : "additionalDeduction|view/") + data.getObjectID() + "/" + data.getStatusCode(), data.getReference());
                        }
                    });
                }

                @Override
                public void onCancel() {

                }
            });
            messageBox.open();
        }

        public String getAction() {
            return action;
        }
    }

    private void initSearchPanel() {
        tableSearchBox = new TextBox();
        tableSearchBox.addStyleName("gwt-SuggestBox");
        tableSearchBox.setPlaceHolder(wfmStrings.searchEmployee());
        tableSearchBox.addKeyDownHandler((event) -> {
            if (event.getNativeKeyCode() == 13) {
                String searchtext = tableSearchBox.getText();

                if ((searchtext == null || searchtext.trim().isEmpty()) && getListingFilterParam() != null && getListingFilterParam().getSqlSearchKey() == null) {
                    return;
                }
                loadTableData();
            }
        });
        Span btnSearch = new Span();
        btnSearch.setStyleName("caret");
        btnSearch.ensureDebugId("searchForm__btn");
        btnSearch.addClickHandler((event) -> {
            String searchtext = tableSearchBox.getText();

            if (searchtext == null || searchtext.trim().isEmpty()) {
                return;
            }
            loadTableData();
        });
        Div searchDiv = new Div("simpleGwt-ComboBox");
        searchDiv.add(tableSearchBox);
        searchDiv.add(btnSearch);
        addField(SEARCH, searchDiv, null);
    }
}
