package com.edatasite.workforce.gwt.payroll.client.ui.view.report.endOfServiceReport;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.EndOfServiceData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EosReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/16/15
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceReport extends Composite implements Constants, AccountingConstants {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    interface EndOfServiceReportViewUiBinder extends UiBinder<HTMLPanel, EndOfServiceReport> {
    }

    private static final EndOfServiceReportViewUiBinder ourUiBinder = GWT.create(EndOfServiceReportViewUiBinder.class);
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DataListBox reason;
    private DatePicker date;
    private EmployeeLookUpWithCode employee;
    private DataListBox payrollGroup;
    private MaterialLink pagingResultText;
    private MaterialLink prevLink;
    private MaterialLink nextLink;
    private TextBox current;
    private DataListBox limitField;

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

    private DateTimeFormat format;
    private EosReportData data;
    private EndOfServiceData eosSettings;
    private ListingFilterParameter lfp;
    private int nowPosition;
    private int allCount;
    private int step;

    public EndOfServiceReport(final View view) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
    }

    private void onInitialize() {
        GBox gBox = headerPanel.drawNewGroupBox();
        gBox.setStyleUnited(true);
        gBox.setStyleWidthFree(true);

        lfp = new ListingFilterParameter();
        format = DateTimeFormat.getFormat("MMMM d, yyyy");

        reason = new DataListBox();
        reason.addValueChangeHandler(changeEvent -> {
            calculate();
            Utils.frame_affix();
        });
        reason.setWithoutNullLabel(true);
        reason.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.employeeResignation(), Constants.EMPLOYEE_RESIGNATION),
                new SelectItem(1, wfmStrings.contractTermination(), Constants.CONTRACT_TERMINATION)}
        );
        reason.setSelected(0);
        headerPanel.addGroupBoxItem(wfmStrings.reason(), reason);

        date = new DatePicker();
        date.addChangeHandler(changeEvent -> getDataAndCalculate());
        date.setDate(new Date());
        headerPanel.addGroupBoxItem(wfmStrings.date(), date);

        employee = new EmployeeLookUpWithCode();
        employee.showClearButton();
        employee.setClearCommand(() -> getDataAndCalculate());
        headerPanel.addGroupBoxItem(wfmStrings.employee(), employee);

        payrollGroup = new DataListBox();
        payrollGroup.addValueChangeHandler(event -> getDataAndCalculate());
        headerPanel.addGroupBoxItem(payrollStrings.payrollGroup(), payrollGroup);
        //payrollItem.setStyleSplitRight(true);

        /*MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadEndOfServiceReportExcel";
            Utils.sendPDFOrExcelRequest(exportPanel, excelURL, lfp.getRequestParams(), "_blank");
        });

        MaterialLink exportPdf = new MaterialLink();
        exportPdf.addStyleName("hasicon--left");
        Icon pdfIcon = new Icon();
        pdfIcon.setClass("ficon--file-pdf");
        exportPdf.add(pdfIcon);
        exportPdf.setText(wfmStrings.pdf());
        exportPdf.addClickHandler(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/downloadEndOfServiceReportPdf";
            Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, lfp.getRequestParams(), "_blank");
        });

        MaterialSplitButton exportButtons = new MaterialSplitButton(exportExl, Constants.BTN_DEFAULT, true);
        exportButtons.addItem(exportPdf);

        GBoxItem exportItem = headerPanel.addGroupBoxItem(null, exportButtons);
        exportItem.setStyleSplitRight(true);*/
        exportSection();

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> {
            lfp.setStart(0);
            getDataAndCalculate();
        });
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);

        pagingResultText = new MaterialLink();
        pagingResultText.setHref("javascript:void(0)");
        pagingResultText.setClass("btn btn--white");
        pagingResultText.setText("0 - 0 of 0");
        GBoxItem pagingResultItem = headerPanel.addGroupBoxItem(null, pagingResultText);
        pagingResultItem.getElement().setAttribute("style", "margin-left: auto;");

        limitField = new DataListBox();
        limitField.setWithoutNullLabel(true);
        GBoxItem limitFieldItem = headerPanel.addGroupBoxItem(null, limitField);
        limitFieldItem.getElement().setAttribute("style", "width: 70px;");

        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        prevLink = new MaterialLink();
        prevLink.setStyleName("btn btn--white btn--icon");
        prevLink.add(prevIcon);
        headerPanel.addGroupBoxItem(null, prevLink);

        current = new TextBox();
        current.setStyleName("currLoc");
        current.setValue("1");
        GBoxItem currentItem = headerPanel.addGroupBoxItem(null, current);
        currentItem.addStyleToComponent("paging__currentpage");

        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        nextLink.add(nextIcon);
        headerPanel.addGroupBoxItem(null, nextLink);

        limitField.setItems(Utils.getLimit(), "20");
        limitField.addValueChangeHandler(changeEvent -> {
            lfp.setLimit(limitField.getSelectedId(true));
            getDataAndCalculate();
        });

        prevLink.addClickHandler(event -> {
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
        PayrollService.App.get().getPayrollBatchesForLookUp(new ListingFilterParameter(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                payrollGroup.setItems(result.toArray(new SelectItem[0]));
            }
        });

        getEosSettings();
    }

    private void pageEvent(int beganPositon) {
        lfp.setStart(beganPositon);
        getDataAndCalculate();
    }

    private void getDataAndCalculate() {
        lfp.setStartDateNC(Utils.getStartDateNCForFilter(date.getDate()));
        lfp.setStartDate(date.getDate());

        lfp.setReasonID(reason.getSelectedId());

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
        lfp.setCorporate(Utils.isSaudiCompany());

        lfp.setStart(lfp.getStart() < 0 ? 0 : lfp.getStart());
        lfp.setLimit(lfp.getLimit() < 1 ? 20 : lfp.getLimit());

        lfp.setCountryCode(Utils.getCompanyrCountryCode());
        LoadingPanel.loading(true);
        PayrollService.App.get().getEmployeeEosDataList(lfp, new AsyncCallback<EosReportData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(EosReportData result) {
                data = result;
                calculate();
                Utils.frame_affix();
                LoadingPanel.loading(false);
            }
        });

    }

    private void getEosSettings() {
        PayrollService.App.get().getEndOfServiceSettings(Utils.getCompanyrCountryCode(), new AsyncCallback<EndOfServiceData>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(EndOfServiceData result) {
                eosSettings = result;
                getDataAndCalculate();
            }
        });
    }

    private void calculate() {
        lfp.setReasonID(reason.getSelectedId());

        BigDecimal total = null;
        double year;
        Integer lessYear = null;
        tableHead.removeAllChildren();
        tableBody.removeAllChildren();
        createHeader(tableHead);
        noResultMessage.setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
        if (data == null || data.getEoSCalculationData().isEmpty()) {
            noMessagePanel.setVisible(true);
            return;
        } else {
            noMessagePanel.setVisible(false);
        }

        step = lfp.getLimit();
        nowPosition = lfp.getStart();
        allCount = data.getTotalCount();

        current.setValue("" + (nowPosition / step + 1));

        pagingResultText.setText((nowPosition + 1) + " - " + ((nowPosition + step) < allCount ? (nowPosition + step) : allCount) + " " + wfmStrings.of() + " " + allCount);

        for (EoSCalculationData eosData : data.getEoSCalculationData()) {
            if (eosData.getTotalWorkedDays() == null) {
                continue;
            }
            total = BigDecimal.ZERO;
            year = Double.valueOf(eosData.getTotalWorkedDays()) / 365;
            if (Utils.isSaudiCompany()) {
                BigDecimal amount = eosData.getBasicSalary();
                amount = amount.add(eosData.getLastPaymentsTotal());
                if (reason.getSelectedId() == 1) {
                    if (year <= 5) {
                        total = total.add(amount.multiply(BigDecimal.valueOf(year)).divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        total = total.add(amount.multiply(new BigDecimal(2.5))).add(amount.multiply(new BigDecimal(year - 5)));
                    }
                } else {
                    if (year > 2 && year <= 5) {
                        total = total.add(amount.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(year)));
                    } else if (year > 5 && year <= 10) {
                        total = total.add(amount.multiply(new BigDecimal(2.5))).add(amount.multiply(new BigDecimal(year - 5))).multiply(new BigDecimal(2)).divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
                    } else if (year > 10) {
                        total = total.add(amount.multiply(new BigDecimal(2.5))).add(amount.multiply(new BigDecimal(year - 5)));
                    }
                }
            } else {
                final double d = eosData.getNumberOfWorkDay() != null ? 1 / eosData.getNumberOfWorkDay() : 12.00 / 365;
                Integer days = 0;
                Integer months = null;
                boolean moreThan5Years = false;

                for (EndOfServiceRules rule : eosSettings.getRules()) {
                    if (rule.getMonths() != null && rule.isUseMonthPayment()) {
                        months = rule.getMonths();
                    }
                    if (reason.getSelectedId() == 0) {  // Employee Resignation
                        if (rule.getRuleCode().equals("0<x<1") &&
                                rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) &&
                                rule.getRuleType().equals(eosData.getEmployeeContractType())) {

                            if (year > 0 && year < 1) {
                                days = rule.getDays();
                                lessYear = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("1<=x<3")) {
                            if (year >= 1 && year < 3) {
                                days = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("3<=x<5")) {
                            if (year >= 3 && year < 5) {
                                days = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("x>5") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                            if (year > 5) {
                                moreThan5Years = true;
                                days = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("1<=x<5") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                            if (year >= 1 && year < 5) {
                                days = rule.getDays();
                            }
                        }
                    } else {
                        if (rule.getRuleCode().equals("x<=5")) {
                            if (year <= 5) {
                                days = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("x>5") && rule.getReasonCode().equals(Constants.CONTRACT_TERMINATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                            if (year > 5) {
                                moreThan5Years = true;
                                days = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("0<x<1") && rule.getReasonCode().equals(Constants.CONTRACT_TERMINATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                            if (year > 0 && year < 1) {
                                days = rule.getDays();
                                lessYear = rule.getDays();
                            }
                        } else if (rule.getRuleCode().equals("x>1") && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                            if (year > 1) {
                                days = rule.getDays();
                            }
                        }
                    }
                }
                BigDecimal calculatePeriod;
                if (months != null) {
                    calculatePeriod = new BigDecimal(months);
                } else {
                    calculatePeriod = BigDecimal.valueOf(d).multiply(new BigDecimal(days));
                }
                if (moreThan5Years) {
                    final BigDecimal years5Total = eosData.getBasicSalary().multiply(BigDecimal.valueOf(d)).multiply(BigDecimal.valueOf(21d)).multiply(BigDecimal.valueOf(5d));

                    BigDecimal after5YearsTotal = eosData.getBasicSalary().multiply(calculatePeriod).multiply(BigDecimal.valueOf((year - 5)));
                    total = total.add(years5Total).add(after5YearsTotal);
                } else {
                    total = total.add(eosData.getBasicSalary().multiply(calculatePeriod).multiply(BigDecimal.valueOf(year)));
                }
            }
            final Element tr = DOM.createTR();
            final String employeeNumber = eosData.getEmployee().getDescription() != null && !"".equals(eosData.getEmployee().getDescription()) ? eosData.getEmployee().getDescription() + " - " : "";

            tr.appendChild(createTdElement(employeeNumber + Optional.ofNullable(eosData.getEmployee().getName()).orElse(""), ""));
            tr.appendChild(createTdElement(format.format(eosData.getHireDate().getNonConvertedDate()), ""));
            tr.appendChild(createTdElement((eosData.getResignationDate() != null && eosData.getResignationDate().getDate() != null) ? format.format(eosData.getResignationDate().getNonConvertedDate()) : format.format(date.getDate()), ""));
            tr.appendChild(createTdElement(eosData.getTotalWorkedDays().toString(), ""));
            if (eosSettings.isIncludeLeaveAllowances()) {
                tr.appendChild(createTdElement(PayrollClientUtils.format(eosData.getLeftLeaveDays()), ""));
                if (lessYear != null && lessYear == 0 && eosData.getTotalWorkedDays() != null && eosData.getTotalWorkedDays() < 365) {
                    tr.appendChild(createTdElement(PayrollClientUtils.format(BigDecimal.ZERO), Constants.RIGHT_ALIGN_CELL));
                } else {
                    tr.appendChild(createTdElement(PayrollClientUtils.format(eosData.getLeaveAllowanceTotal()), Constants.RIGHT_ALIGN_CELL));
                    total = total.add(eosData.getLeaveAllowanceTotal());
                }
            }
            if (eosSettings.isIncludeBenefitPayments()) {
                tr.appendChild(createTdElement(PayrollClientUtils.format(eosData.getBenefitPaymentTotal()), Constants.RIGHT_ALIGN_CELL));
                total = total.add(eosData.getBenefitPaymentTotal());
            }
            tr.appendChild(createTdElement(PayrollClientUtils.format(total), Constants.RIGHT_ALIGN_CELL));
            tableBody.appendChild(tr);
        }
    }

    private Element createTdElement(String innerText, String style) {
        final Element tdElement = DOM.createTD();
        if (!style.isEmpty()) {
            tdElement.addClassName(style);
        }
        tdElement.setInnerText(innerText);
        return tdElement;
    }

    private void createHeader(Element table) {
        Element tr = DOM.createTR();
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.employee());
        divElement.getStyle().clearWidth();
        Element employee = DOM.createTH();
        employee.setClassName("stickerCell");
        employee.getStyle().setWidth(200, Style.Unit.PX);
        employee.appendChild(divElement);
        tr.appendChild(employee);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.hireDate());
        Element hireDate = DOM.createTH();
        hireDate.setClassName("stickerCell");
        hireDate.getStyle().setWidth(100, Style.Unit.PX);
        hireDate.appendChild(divElement);
        tr.appendChild(hireDate);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.resignationDate());
        Element resignationDate = DOM.createTH();
        resignationDate.setClassName("stickerCell");
        resignationDate.getStyle().setWidth(100, Style.Unit.PX);
        resignationDate.appendChild(divElement);
        tr.appendChild(resignationDate);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(payrollStrings.daysWorked());
        Element workedDays = DOM.createTH();
        workedDays.setClassName("stickerCell");
        workedDays.getStyle().setWidth(70, Style.Unit.PX);
        workedDays.appendChild(divElement);
        tr.appendChild(workedDays);

        if (eosSettings.isIncludeLeaveAllowances()) {
            divElement = DOM.createDiv();
            divElement.setClassName("frame_affix_top");
            divElement.setInnerText(wfmStrings.leftLeaveDays());
            Element leaveDays = DOM.createTH();
            leaveDays.setClassName("stickerCell");
            leaveDays.getStyle().setWidth(70, Style.Unit.PX);
            leaveDays.appendChild(divElement);
            tr.appendChild(leaveDays);

            divElement = DOM.createDiv();
            divElement.setClassName("frame_affix_top");
            divElement.setInnerText(wfmStrings.leaveAllowance());
            Element leavePayment = DOM.createTH();
            leavePayment.setClassName("stickerCell");
            leavePayment.addClassName(Constants.RIGHT_ALIGN_CELL);
            leavePayment.getStyle().setWidth(70, Style.Unit.PX);
            leavePayment.appendChild(divElement);
            tr.appendChild(leavePayment);
        }

        if (eosSettings.isIncludeBenefitPayments()) {
            divElement = DOM.createDiv();
            divElement.setClassName("frame_affix_top");
            divElement.setInnerText(payrollStrings.benefitPayment());
            Element benefitPayment = DOM.createTH();
            benefitPayment.setClassName("stickerCell");
            benefitPayment.addClassName(Constants.RIGHT_ALIGN_CELL);
            benefitPayment.getStyle().setWidth(70, Style.Unit.PX);
            benefitPayment.appendChild(divElement);
            tr.appendChild(benefitPayment);
        }

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.gratuityTotal());
        Element total = DOM.createTH();
        total.setClassName("stickerCell");
        total.addClassName(Constants.RIGHT_ALIGN_CELL);
        total.getStyle().setWidth(80, Style.Unit.PX);
        total.appendChild(divElement);
        tr.appendChild(total);
        table.appendChild(tr);
    }

    private void exportSection() {

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

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadEndOfServiceReportExcel";
            Utils.sendPDFOrExcelRequest(exportPanel, excelURL, lfp.getRequestParams(), "_blank");
        });

        MaterialLink exportPdf = new MaterialLink();
        exportPdf.addStyleName("hasicon--left");
        Icon pdfIcon = new Icon();
        pdfIcon.setClass("ficon--file-pdf");
        exportPdf.add(pdfIcon);
        exportPdf.setText(wfmStrings.pdf());
        exportPdf.addClickHandler(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/downloadEndOfServiceReportPdf";
            Utils.sendPDFOrExcelRequest(exportPanel, pdfURL, lfp.getRequestParams(), "_blank");
        });

        showMenuContainer.add(exportExl);
        showMenuContainer.add(exportPdf);

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        exportItem.setStyleSplitRight(true);
    }
}