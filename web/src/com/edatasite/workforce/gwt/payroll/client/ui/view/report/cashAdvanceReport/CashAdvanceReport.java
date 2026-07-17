package com.edatasite.workforce.gwt.payroll.client.ui.view.report.cashAdvanceReport;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
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
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.enums.ReportDatesEnum;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollReportUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/3/16
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceReport  extends Composite implements Constants {
    interface CashAdvanceReportUiBinder extends UiBinder<HTMLPanel, CashAdvanceReport> {
    }

    private static CashAdvanceReportUiBinder ourUiBinder = GWT.create(CashAdvanceReportUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();


    private ListingFilterParameter lfp;
    private List<Date> financialQuartiesList;
    private Date financialYearStart;
    private DateTimeFormat format;
    private Date currentDate;
    private int nowPosition;
    private int allCount;
    private int step;

    private DataListBox datesValue;
    private DatePicker fromValue;
    private DatePicker toValue;
    private EmployeeLookUpWithCode employee;
    private DataListBox payrollGroup;
    private CategoryLookUp categoryLookUp;
    private WfmButton2 updateButton;
    private MaterialLink pagingResultText;
    private MaterialLink previousLink;
    private MaterialLink nextLink;
    private TextBox current;
    private GBoxRow paginationPanel;
    private SplitButton exportButton;
    HTMLPanel rootElement;

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    Element tableHead;
    @UiField
    Element tableBody;
    @UiField
    HTMLPanel noMessagePanel;
    @UiField
    HeadingElement noResultMessage;
    @UiField
    HTMLPanel exportPanel;

    public CashAdvanceReport() {
        rootElement = ourUiBinder.createAndBindUi(this);
        rootElement.setStyleName("content-box content-box--white");
        onInitialize();
    }

    private void onInitialize() {
        GBox filterBox = headerPanel.drawNewGroupBox();
        filterBox.setStyleUnited(true);

        lfp = new ListingFilterParameter();
        format = DateTimeFormat.getFormat("MMMM d, yyyy");
        datesValue = new DataListBox();
        headerPanel.addGroupBoxItem(0, wfmStrings.dates(), datesValue);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);

        toValue = new DatePicker();
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);

        headerPanel.addGroupBoxItem(0, datePeriodItem);

        employee = new EmployeeLookUpWithCode();
        employee.showClearButton();
        employee.getElement().addClassName("report-lookUp report-lookUp-width");
        headerPanel.addGroupBoxItem(0, wfmStrings.employee(), employee);

        payrollGroup = new DataListBox();
        payrollGroup.getElement().addClassName("report-lookUp report-lookUp-width");
        headerPanel.addGroupBoxItem(0, payrollStrings.payrollGroup(), payrollGroup);

        categoryLookUp = new CategoryLookUp();
        categoryLookUp.showClearButton();
        categoryLookUp.setCategoryType(PayrollConstants.CATEGORY_DEDUCTION);
        categoryLookUp.setCashAdvance(true);
        categoryLookUp.getElement().addClassName("report-lookUp report-lookUp-width");
        GBoxItem categoryItem = headerPanel.addGroupBoxItem(0, wfmStrings.category(), categoryLookUp);

        initExportPanel();

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);

        initPaginationPanel();

        PayrollService.App.get().getPayrollBatchesForLookUp(new ListingFilterParameter(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                payrollGroup.setItems(result.toArray(new SelectItem[0]));
            }
        });

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
    }

    private void initExportPanel() {
        exportButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        List<SplitButtonItem> buttonItems = new ArrayList<>();

        SplitButtonItem pdfItem = new SplitButtonItem("PDF_VERSION", wfmStrings.pdf(), () -> {
            String pdfURL = CommandConstants.PDF_URL + "/downloadCashAdvanceReportPdf";
            Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, lfp.getRequestParams(), "_blank");
        }, true);
        pdfItem.ensureDebugId("pdfItem");
        buttonItems.add(pdfItem);

        SplitButtonItem excelItem = new SplitButtonItem("EXCEL_VERSION", wfmStrings.excel(), () -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadCashAdvanceReportExcel";
            Utils.sendPDFOrExcelRequest(exportPanel, excelURL, lfp.getRequestParams(), "_blank");
        }, false);
        excelItem.ensureDebugId("pdfVersionItem");
        buttonItems.add(excelItem);

        exportButton.addItemList(buttonItems);
    }

    private void initPaginationPanel() {
        pagingResultText = new MaterialLink();
        pagingResultText.setHref("javascript:void(0)");
        pagingResultText.setClass("btn btn--white");
        pagingResultText.setText("0 - 0 of 0");

        previousLink = new MaterialLink();
        previousLink.setStyleName("btn btn--white btn--icon");
        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        previousLink.add(prevIcon);

        current = new TextBox();
        current.setStyleName("currLoc form-control");
        current.setValue("1");
        GBoxItem currentItem = new GBoxItem(current);
        currentItem.getComponent().addStyleName("paging__currentpage");

        nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        nextLink.add(nextIcon);

        paginationPanel = new GBoxRow();
        paginationPanel.add(new GBoxItem(pagingResultText));
        paginationPanel.add(new GBoxItem(previousLink));
        paginationPanel.add(currentItem);
        paginationPanel.add(new GBoxItem(nextLink));
    }

    private void initInternal() {
        datesValue.setWithoutNullLabel(true);
        datesValue.setItems(PayrollReportUtils.getDatesListItems());
        datesValue.addValueChangeHandler(changeEvent -> PayrollReportUtils.setFromAndToDates(fromValue, toValue, datesValue.getSelectedId(), financialQuartiesList, financialYearStart));

        fromValue.addChangeHandler(changeEvent -> datesValue.setSelected(ReportDatesEnum.Custom.getId()));
        toValue.addChangeHandler(changeEvent -> datesValue.setSelected(ReportDatesEnum.Custom.getId()));

        employee.setClearCommand(() -> update());
        payrollGroup.addValueChangeHandler(event -> update());
        categoryLookUp.setClearCommand(() -> update());

        updateButton.addClickHandler(clickEvent -> {
            lfp.setLimit(20);
            lfp.setStart(0);
            update();
        });

        previousLink.addClickHandler(event -> {
            if (nowPosition > 0) {
                pageEvent(nowPosition - step);
            }
        });
        nextLink.addClickHandler(event -> {
            if (nowPosition + step < allCount) {
                pageEvent(nowPosition + step);
            }
        });
        current.addKeyUpHandler(keyUpEvent -> {
            int key = keyUpEvent.getNativeKeyCode();
            if (key == KeyCodes.KEY_ENTER) {
                try {
                    int begin = Integer.parseInt(current.getValue().trim());
                    if (begin > 0 && begin <= (int) Math.ceil(((double) allCount / (double) (step)))) {
                        pageEvent(begin * step - step);
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        update();
    }

    private void update(){
        lfp.setStartDate(fromValue.getDate());
        lfp.setEndDate(toValue.getDate());
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
        lfp.setCategoryID(categoryLookUp.getSelectedItem() != null ? categoryLookUp.getSelectedItem().getId() : null);
        if (lfp.getStart() == 0 && lfp.getLimit() == 0) {
            lfp.setStart(0);
            lfp.setLimit(20);
        }
        LoadingPanel.loading(true);
        PayrollService.App.get().getCashAdvanceReportData(lfp, new AbstractAsyncCallback<CashAdvanceReportData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CashAdvanceReportData data) {
                LoadingPanel.loading(false);
                tableHead.removeAllChildren();
                tableBody.removeAllChildren();
                noResultMessage.setInnerText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                createHeader(tableHead);
                if (data != null && data.getCashAdvanceReportItems().size() > 0) {
                    noMessagePanel.setVisible(false);
                    step= lfp.getLimit();
                    nowPosition = lfp.getStart();
                    allCount = data.getTotalCount();

                    current.setValue("" + (nowPosition / step + 1));
                    pagingResultText.setText((nowPosition + 1) + " - " + ((nowPosition + step) < allCount ? (nowPosition + step) : allCount) + " " + wfmStrings.of() + " " + allCount);

                    setReportData(data.getCashAdvanceReportItems());

                } else {
                    noMessagePanel.setVisible(true);
                    current.setValue("0");
                    pagingResultText.setText(0 + " - " + 0 + " " + wfmStrings.of() + " " + 0);
                }
                Utils.frame_affix();
            }
        });
    }

    private void createHeader(Element table) {
        Element tr = DOM.createTR();
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.employee());
        Element employee = DOM.createTH();
        employee.setClassName("stickerCell");
        employee.getStyle().setWidth(150, Style.Unit.PX);
        employee.appendChild(divElement);
        tr.appendChild(employee);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.cashAdvanceDate(), wfmStrings.cashAdvance()));
        Element date = DOM.createTH();
        date.setClassName("stickerCell");
        date.getStyle().setWidth(100, Style.Unit.PX);
        date.appendChild(divElement);
        tr.appendChild(date);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.amount());
        Element amount = DOM.createTH();
        amount.setClassName("stickerCell");
        amount.addClassName(Constants.RIGHT_ALIGN_CELL);
        amount.getStyle().setWidth(120, Style.Unit.PX);
        amount.appendChild(divElement);
        tr.appendChild(amount);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.paidAmount());
        Element paidAmount = DOM.createTH();
        paidAmount.setClassName("stickerCell");
        paidAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
        paidAmount.getStyle().setWidth(120, Style.Unit.PX);
        paidAmount.appendChild(divElement);
        tr.appendChild(paidAmount);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.remainingAmount());
        Element remainingAmount = DOM.createTH();
        remainingAmount.setClassName("stickerCell");
        remainingAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
        remainingAmount.getStyle().setWidth(120, Style.Unit.PX);
        remainingAmount.appendChild(divElement);
        tr.appendChild(remainingAmount);
        table.appendChild(tr);
    }

    private void setReportData(List<CashAdvanceReportItem> data) {
        BigDecimal amountTotal = BigDecimal.ZERO, paidAmountTotal = BigDecimal.ZERO, remainingTotal = BigDecimal.ZERO;
        for (CashAdvanceReportItem item : data) {
            Element tr = DOM.createTR();

            Element employee = DOM.createTD();
            String employeeNumber = item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() + " - " : "";
            employee.setInnerText(employeeNumber + item.getEmployeeName());
            tr.appendChild(employee);

            Element date = DOM.createTD();
            date.setInnerText(format.format(item.getDate()));
            tr.appendChild(date);

            Element amount = DOM.createTD();
            amount.addClassName(Constants.RIGHT_ALIGN_CELL);
            amount.setInnerText(PayrollClientUtils.format(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO));
            tr.appendChild(amount);

            Element paidAmount = DOM.createTD();
            paidAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
            paidAmount.setInnerText(PayrollClientUtils.format(item.getPaidAmount() != null ? item.getPaidAmount() : BigDecimal.ZERO));
            tr.appendChild(paidAmount);

            Element remainingAmount = DOM.createTD();
            remainingAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
            remainingAmount.setInnerText(PayrollClientUtils.format(item.getRemainingAmount() != null ? item.getRemainingAmount() : BigDecimal.ZERO));
            tr.appendChild(remainingAmount);
            tableBody.appendChild(tr);
            if (item.getAmount() != null) {
                amountTotal = amountTotal.add(item.getAmount());
            }
            if (item.getPaidAmount() != null) {
                paidAmountTotal = paidAmountTotal.add(item.getPaidAmount());
            }
            if (item.getRemainingAmount() != null) {
                remainingTotal = remainingTotal.add(item.getRemainingAmount());
            }
        }

        Element totalTR = DOM.createTR();
        totalTR.addClassName("total_row");
        Element td = DOM.createTD();
        td.setInnerHTML(wfmStrings.total());
        td.setAttribute("colspan", "2");
        totalTR.appendChild(td);
        Element amount = DOM.createTD();
        amount.addClassName(Constants.RIGHT_ALIGN_CELL);
        amount.setInnerText(PayrollClientUtils.format(amountTotal));
        totalTR.appendChild(amount);
        Element paidAmount = DOM.createTD();
        paidAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
        paidAmount.setInnerText(PayrollClientUtils.format(paidAmountTotal));
        totalTR.appendChild(paidAmount);
        Element remainingAmount = DOM.createTD();
        remainingAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
        remainingAmount.setInnerText(PayrollClientUtils.format(remainingTotal));
        totalTR.appendChild(remainingAmount);
        tableBody.appendChild(totalTR);

    }

    private void pageEvent(int beganPositon) {
        lfp.setStart(beganPositon);
        update();
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
