package com.edatasite.workforce.gwt.payroll.client.ui.view.report.salaryReport;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.enums.ReportDatesEnum;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollReportUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/4/16
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalaryReport extends Composite implements Constants {
    interface SalaryReportUiBinder extends UiBinder<HTMLPanel, SalaryReport> {
    }

    private static final SalaryReportUiBinder ourUiBinder = GWT.create(SalaryReportUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private ListingFilterParameter lfp;
    private List<Date> financialQuartiesList;
    private Date financialYearStart;
    private Date currentDate;

    private DataListBox datesValue;
    private DatePicker fromValue;
    private DatePicker toValue;
    private EmployeeLookUpWithCode employee;
    private DataListBox payrollGroup;
    private WfmButton2 updateButton;
    private PagingWidget pagingWidget;
    private SplitButton exportButton;
    private GBoxRow paginationPanel;
    private final HTMLPanel rootElement;

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    Element tableHead;
    @UiField
    Element tableBody;
    @UiField
    HTMLPanel noMessagePanel;
    @UiField
    HeadingElement noResultMessage;

    SalaryReport() {
        rootElement = ourUiBinder.createAndBindUi(this);
        rootElement.setStyleName("content-box content-box--white");
        onInitialize();
    }

    private void onInitialize() {
        GBox filterBox = headerPanel.drawNewGroupBox();
        filterBox.setStyleUnited(true);
//        filterBox.setStyleWidthFree(true);

        lfp = new ListingFilterParameter();
        lfp.setLimit(50);
        datesValue = new DataListBox();
        datesValue.setWithoutNullLabel(true);
        datesValue.setItems(PayrollReportUtils.getDatesListItems());
        GBoxItem datesItem = headerPanel.addGroupBoxItem(0, wfmStrings.dates(), datesValue);
        datesItem.setStyleWidthFree(true);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        fromValue.ensureDebugId("profitAndLoss-startDate");
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);

        toValue = new DatePicker();
        toValue.ensureDebugId("profitsAndLoss-endDate  ");
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        headerPanel.addGroupBoxItem(0, datePeriodItem);

        employee = new EmployeeLookUpWithCode();
        employee.showClearButton();
        headerPanel.addGroupBoxItem(0, wfmStrings.employee(), employee);

        payrollGroup = new DataListBox();
        GBoxItem payrollItem = headerPanel.addGroupBoxItem(0, payrollStrings.payrollGroup(), payrollGroup);
        //payrollItem.setStyleSplitRight(true);

        initExportPanel();

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);

        pagingWidget = new PagingWidget(true);
        pagingWidget.setLimit(50);
        pagingWidget.setPaging(new PagingWidget.Paging() {

            @Override
            public void loadData(int start, int limit) {
                lfp.setStart(start);
                lfp.setLimit(limit);
                update();
            }
        });
        GBoxItem pagingField = new GBoxItem(null, pagingWidget);
        paginationPanel = new GBoxRow(pagingField);

        PayrollService.App.get().getFinancialYearDate(new AbstractAsyncCallback<Date>() {
            @Override
            public void failure(Throwable throwable) {
                initInternal();
            }

            @Override
            public void success(Date result) {
                financialYearStart = DateUtil.addDays(result, 1);
                currentDate = new Date();
                financialYearStart.setYear(currentDate.getYear());

                while (financialYearStart.after(currentDate)) {
                    financialYearStart.setYear(financialYearStart.getYear() - 1);
                }

                financialQuartiesList = Utils.setupFinancialQuarties(financialYearStart);
                fromValue.setDate(DateUtil.getMonthFirstDay(currentDate));
                toValue.setDate(DateUtil.getMonthLastDate(currentDate));
                initInternal();
            }
        });

        PayrollService.App.get().getPayrollBatchesForLookUp(new ListingFilterParameter(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                payrollGroup.setItems(result.toArray(new SelectItem[0]));
            }
        });

    }


    private void initInternal() {
        employee.setClearCommand(() -> update());

        payrollGroup.addValueChangeHandler(e -> update());

        datesValue.addValueChangeHandler(changeEvent -> PayrollReportUtils.setFromAndToDates(fromValue, toValue, datesValue.getSelectedId(), financialQuartiesList, financialYearStart));


        fromValue.addChangeHandler(changeEvent -> datesValue.setSelected(ReportDatesEnum.Custom.getId()));
        toValue.addChangeHandler(changeEvent -> datesValue.setSelected(ReportDatesEnum.Custom.getId()));

        updateButton.addClickHandler(event -> {
            lfp.setStart(0);
            update();
        });
        update();
    }

    private void initExportPanel() {
        exportButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        List<SplitButtonItem> buttonItems = new ArrayList<>();

        SplitButtonItem pdfItem = new SplitButtonItem("PDF_VERSION", wfmStrings.pdf(), () -> {
            String pdfURL = CommandConstants.PDF_URL + "/downloadSalaryReportPdf";
            Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, lfp.getRequestParams(), "_blank");
        }, true);
        pdfItem.ensureDebugId("pdfItem");
        buttonItems.add(pdfItem);

        SplitButtonItem excelItem = new SplitButtonItem("EXCEL_VERSION", wfmStrings.excel(), () -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadSalaryReportExcel";
            Utils.sendPDFOrExcelRequest(exportPanel, excelURL, lfp.getRequestParams(), "_blank");
        }, false);
        excelItem.ensureDebugId("pdfVersionItem");
        buttonItems.add(excelItem);

        exportButton.addItemList(buttonItems);
    }

    private void update() {
        lfp.setStartDateWithoutOffset(fromValue.getDate());
        lfp.setEndDateWithoutOffset(DateUtil.getDayLastTime(toValue.getDate()));
        lfp.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        lfp.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        if (employee.getSelectedItem() != null) {
            lfp.setPayrollBatchID(null);
            lfp.setEmployeeId(employee.getSelectedItem().getId());
        } else if (payrollGroup.getSelectedItem() != null) {
            lfp.setEmployeeId(null);
            lfp.setPayrollBatchID(payrollGroup.getSelectedItem().getId());
        } else {
            lfp.setEmployeeId(null);
            lfp.setPayrollBatchID(null);
        }
        lfp.setStart(lfp.getStart() < 0 ? 0 : lfp.getStart());
        lfp.setLimit(lfp.getLimit() < 1 ? 20 : lfp.getLimit());
        LoadingPanel.loading(true);
        PayrollService.App.get().getSalarReportData(lfp, new AbstractAsyncCallback<SalaryReportData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SalaryReportData data) {
                LoadingPanel.loading(false);
                tableHead.removeAllChildren();
                tableBody.removeAllChildren();
                noResultMessage.setInnerText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                createHeader(tableHead);
                if (data != null && data.getSalaryReportItems().size() > 0) {
                    noMessagePanel.setVisible(false);
                    pagingWidget.setTotalCount(data.getTotalCount());
                    setReportData(data.getSalaryReportItems());
                } else {
                    noMessagePanel.setVisible(true);
                }
                Utils.frame_affix();
            }
        });

    }

    private void setReportData(List<SalaryReportItem> salaryReportItems) {
        BigDecimal basicSalaryTotal = BigDecimal.ZERO, allowanceTotal = BigDecimal.ZERO, expPaymentTotal = BigDecimal.ZERO, expDeductionTotal = BigDecimal.ZERO,
                pensionTotal = BigDecimal.ZERO, deductionTotal = BigDecimal.ZERO, overallTotal = BigDecimal.ZERO;
        for (SalaryReportItem item : salaryReportItems) {
            Element tr = DOM.createTR();

            createTableCell(tr, (item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() + " - " : "") + item.getEmployeeName(), "");
            createTableCell(tr, item.getMonth() != null ? item.getMonth() : wfmStrings.notAvailable(), "");
            createTableCell(tr, item.getCurrency() != null ? item.getCurrency() : wfmStrings.notAvailable(), "");
            createTableCell(tr, PayrollClientUtils.format(item.getBasicSalary()), Constants.RIGHT_ALIGN_CELL);
            createTableCell(tr, PayrollClientUtils.format(item.getAllowance()), Constants.RIGHT_ALIGN_CELL);
            createTableCell(tr, PayrollClientUtils.format(item.getExpensePayment()), Constants.RIGHT_ALIGN_CELL);
            createTableCell(tr, PayrollClientUtils.format(item.getExpenseDeduction()), Constants.RIGHT_ALIGN_CELL);
            createTableCell(tr, PayrollClientUtils.format(item.getPensionAmount()), Constants.RIGHT_ALIGN_CELL);
            createTableCell(tr, PayrollClientUtils.format(item.getDeduction()), Constants.RIGHT_ALIGN_CELL);
            createTableCell(tr, PayrollClientUtils.format(item.getTotal()), Constants.RIGHT_ALIGN_CELL);
            tableBody.appendChild(tr);

            basicSalaryTotal = basicSalaryTotal.add(item.getBasicSalary());
            allowanceTotal = allowanceTotal.add(item.getAllowance());
            expPaymentTotal = expPaymentTotal.add(item.getExpensePayment());
            expDeductionTotal = expDeductionTotal.add(item.getExpenseDeduction());
            pensionTotal = pensionTotal.add(item.getPensionAmount());
            deductionTotal = deductionTotal.add(item.getDeduction());
            overallTotal = overallTotal.add(item.getTotal());
        }

        Element totalTR = DOM.createTR();
        totalTR.addClassName("total_row");
        Element td = DOM.createTD();
        td.setInnerHTML(wfmStrings.total());
        td.setAttribute("colspan", "3");
        totalTR.appendChild(td);
        createTableCell(totalTR, PayrollClientUtils.format(basicSalaryTotal), Constants.RIGHT_ALIGN_CELL);
        createTableCell(totalTR, PayrollClientUtils.format(allowanceTotal), Constants.RIGHT_ALIGN_CELL);
        createTableCell(totalTR, PayrollClientUtils.format(expPaymentTotal), Constants.RIGHT_ALIGN_CELL);
        createTableCell(totalTR, PayrollClientUtils.format(expDeductionTotal), Constants.RIGHT_ALIGN_CELL);
        createTableCell(totalTR, PayrollClientUtils.format(pensionTotal), Constants.RIGHT_ALIGN_CELL);
        createTableCell(totalTR, PayrollClientUtils.format(deductionTotal), Constants.RIGHT_ALIGN_CELL);
        createTableCell(totalTR, PayrollClientUtils.format(overallTotal), Constants.RIGHT_ALIGN_CELL);
        tableBody.appendChild(totalTR);
    }

    private void createTableCell(Element tr, String text, String style) {
        Element td = DOM.createTD();
        if (!style.isEmpty()) {
            td.addClassName(style);
        }
        td.setInnerText(text);
        tr.appendChild(td);
    }

    private void createHeader(Element table) {
        Element tr = DOM.createTR();
        createCell(tr, wfmStrings.employee(), 130, "");
        createCell(tr, wfmStrings.month(), 80, "");
        createCell(tr, wfmStrings.currency(), 80, "");
        createCell(tr, wfmStrings.basicSalary(), 80, Constants.RIGHT_ALIGN_CELL);
        createCell(tr, wfmStrings.allowances(), 80, Constants.RIGHT_ALIGN_CELL);
        createCell(tr, payrollStrings.expensePayment(), 85, Constants.RIGHT_ALIGN_CELL);
        createCell(tr, wfmStrings.expenseDeduction(), 85, Constants.RIGHT_ALIGN_CELL);
        createCell(tr, payrollStrings.pension(), 80, Constants.RIGHT_ALIGN_CELL);
        createCell(tr, wfmStrings.deductions(), 80, Constants.RIGHT_ALIGN_CELL);
        createCell(tr, payrollStrings.totalPaid(), 80, Constants.RIGHT_ALIGN_CELL);
        table.appendChild(tr);
    }

    private void createCell(Element tr, String text, double width, String style) {
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(text);
        Element th = DOM.createTH();
        th.setClassName("stickerCell");
        if (!style.isEmpty()) {
            th.addClassName(style);
        }
        th.getStyle().setWidth(width, Style.Unit.PX);
        th.appendChild(divElement);
        tr.appendChild(th);
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }

    public WfmButton2 getUpdateButton() {
        return updateButton;
    }

    public SplitButton getExportButton() {
        return exportButton;
    }

    public GBoxRow getPaginationPanel() {
        return paginationPanel;
    }
}
