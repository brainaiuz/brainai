package com.edatasite.workforce.gwt.payroll.client.ui.view.report.pensionReport;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionContributionData;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PensionContributionReport extends Composite {
    interface PensionContributionReportUiBinder extends UiBinder<HTMLPanel, PensionContributionReport> {
    }
    private static PensionContributionReportUiBinder ourUiBinder = GWT.create(PensionContributionReportUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");
    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");

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

    private DataListBox monthListBox;
    private DataListBox yearListBox;

    public PensionContributionReport() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
    }

    private void onInitialize() {
        GBox gBox = headerPanel.drawNewGroupBox();
        gBox.setStyleUnited(true);
        gBox.setStyleWidthFree(true);

        monthListBox = new DataListBox();
        setMonthItems();
        monthListBox.setSelectedNullLabel();

        headerPanel.addGroupBoxItem(0, wfmStrings.month(), monthListBox);

        yearListBox = new DataListBox();
        setYearItems();
        yearListBox.setWithoutNullLabel(true);
        yearListBox.setSelected(Integer.valueOf(format_year.format(new Date())));

        GBoxItem boxItem = headerPanel.addGroupBoxItem(0, wfmStrings.year(), yearListBox);
        boxItem.setStyleSplitRight(true);

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(c -> update());
        headerPanel.addGroupBoxItem(0, null, updateButton);

        update();
    }

    private void update() {
        LoadingPanel.loading(true);
        Integer month = monthListBox.getSelectedId();
        Integer year = Integer.valueOf(yearListBox.getSelectedItem(true).getName());
        PayrollService.App.get().getPensionContributionByFilter(month, year, new AbstractAsyncCallback<ArrayList<PensionContributionData>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<PensionContributionData> result) {
                LoadingPanel.loading(false);
                tableHead.removeAllChildren();
                tableBody.removeAllChildren();
                noResultMessage.setInnerText(payrollStrings.thereAreNoPensionContributionsSelectedPeriod());
                createHeader(tableHead);
                if (result != null && result.size() > 0) {
                    noMessagePanel.setVisible(false);
                    setReportData(result);
                } else {
                    noMessagePanel.setVisible(true);
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
        employee.getStyle().clearWidth();
        employee.getStyle().setWidth(200, Style.Unit.PX);
        employee.appendChild(divElement);
        tr.appendChild(employee);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.month());
        Element date = DOM.createTH();
        date.setClassName("stickerCell");
        date.getStyle().clearWidth();
        date.getStyle().setWidth(100, Style.Unit.PX);
        date.appendChild(divElement);
        tr.appendChild(date);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(payrollStrings.employeeContribution());
        Element amount = DOM.createTH();
        amount.setClassName("stickerCell");
        amount.addClassName(Constants.RIGHT_ALIGN_CELL);
        amount.getStyle().setWidth(120, Style.Unit.PX);
        amount.appendChild(divElement);
        tr.appendChild(amount);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(payrollStrings.employeeContribution());
        Element paidAmount = DOM.createTH();
        paidAmount.setClassName("stickerCell");
        paidAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
        paidAmount.getStyle().setWidth(120, Style.Unit.PX);
        paidAmount.appendChild(divElement);
        tr.appendChild(paidAmount);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.total());
        Element remainingAmount = DOM.createTH();
        remainingAmount.setClassName("stickerCell");
        remainingAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
        remainingAmount.getStyle().setWidth(120, Style.Unit.PX);
        remainingAmount.appendChild(divElement);
        tr.appendChild(remainingAmount);
        table.appendChild(tr);
    }

    private void setReportData(List<PensionContributionData> data) {
        BigDecimal companyPensionAmount = BigDecimal.ZERO, total = BigDecimal.ZERO;
        for (PensionContributionData item : data) {
            if (item.getCompanyPensionRate() == null || item.getCompanyPensionType() == null) {
                continue;
            }
            companyPensionAmount = BigDecimal.ZERO;
            total = BigDecimal.ZERO;

            total = total.add(item.getEmployeePensionAmount());
            if (item.getCompanyPensionType() == 0) {
                companyPensionAmount = item.getCompanyPensionRate();
            } else {
                companyPensionAmount = item.getBasicSalary().multiply(item.getCompanyPensionRate().divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            total = total.add(companyPensionAmount);

            Element tr = DOM.createTR();

            Element employee = DOM.createTD();
            employee.setInnerText(item.getEmployeeFullName());
            tr.appendChild(employee);

            Element date = DOM.createTD();
            date.setInnerText(item.getMonth());
            tr.appendChild(date);

            Element amount = DOM.createTD();
            amount.addClassName(Constants.RIGHT_ALIGN_CELL);
            amount.setInnerText(PayrollClientUtils.format(item.getEmployeePensionAmount()));
            tr.appendChild(amount);

            Element paidAmount = DOM.createTD();
            paidAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
            paidAmount.setInnerText(PayrollClientUtils.format(companyPensionAmount));
            tr.appendChild(paidAmount);

            Element remainingAmount = DOM.createTD();
            remainingAmount.addClassName(Constants.RIGHT_ALIGN_CELL);
            remainingAmount.setInnerText(PayrollClientUtils.format(total));
            tr.appendChild(remainingAmount);
            tableBody.appendChild(tr);
        }
    }

    private void setMonthItems() {
        SelectItem monthItems[] = new SelectItem[12];
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 0; i < 12; i++) {
            monthItems[i] = new SelectItem(i, format_month.format(date));
            date = DateUtil.addMonths(date, 1);
        }
        monthListBox.setItems(monthItems);
    }

    private void setYearItems() {
        SelectItem yearItem[] = new SelectItem[5];
        Date date = new Date();
        int currentYear = Integer.valueOf(format_year.format(date));

        for (int i = 2, j = 0; j < 2; i--, j++) {
            yearItem[j] = new SelectItem(currentYear - i, String.valueOf(currentYear - i));
        }

        yearItem[2] = new SelectItem(currentYear, String.valueOf(currentYear));

        for (int i = 1, j = 3; i <= 2; i++, j++) {
            yearItem[j] = new SelectItem(currentYear + i, String.valueOf(currentYear + i));
        }
        yearListBox.setItems(yearItem);
    }
}
