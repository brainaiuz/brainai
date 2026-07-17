package com.edatasite.workforce.gwt.payroll.client.ui.view.report.wpsReport;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportItem;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shohruh on 27-Dec-16.
 */
public class WpsReport extends Composite {
    interface WpsReportUiBinder extends UiBinder<HTMLPanel, WpsReport> {
    }

    private static WpsReportUiBinder ourUiBinder = GWT.create(WpsReportUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private ListingFilterParameter lfp;
    private HashMap<SelectItem, SelectItem[]> yearMonthData;

    private DataListBox year;
    private DataListBox month;
    private DataListBox payMethod;
    private DataListBox payrollGroup;
    private WfmButton2 updateButton;

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

    public WpsReport() {
        initWidget(ourUiBinder.createAndBindUi(this));
        RootPanel.get().addStyleName("fitted-content");
        onInitialize();
    }

    private void createHeader(Element table) {
        Element tr = DOM.createTR();
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.employee());
        Element employee = DOM.createTH();
        employee.setClassName("stickerCell");
        employee.getStyle().setWidth(20, Style.Unit.PCT);
        employee.appendChild(divElement);
        tr.appendChild(employee);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.wpsNumber());
        Element wpsNumber = DOM.createTH();
        wpsNumber.setClassName("stickerCell");
        wpsNumber.getStyle().setWidth(10, Style.Unit.PCT);
        wpsNumber.appendChild(divElement);
        tr.appendChild(wpsNumber);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.createdBy());
        Element creator = DOM.createTH();
        creator.setClassName("stickerCell");
        creator.getStyle().setWidth(20, Style.Unit.PCT);
        creator.appendChild(divElement);
        tr.appendChild(creator);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.approver());
        Element approver = DOM.createTH();
        approver.setClassName("stickerCell");
        approver.getStyle().setWidth(20, Style.Unit.PCT);
        approver.appendChild(divElement);
        tr.appendChild(approver);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.month());
        Element month = DOM.createTH();
        month.setClassName("stickerCell");
        month.getStyle().setWidth(10, Style.Unit.PCT);
        month.appendChild(divElement);
        tr.appendChild(month);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.year());
        Element year = DOM.createTH();
        year.setClassName("stickerCell");
        year.getStyle().setWidth(10, Style.Unit.PCT);
        year.appendChild(divElement);
        tr.appendChild(year);

        divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.setInnerText(wfmStrings.total());
        Element total = DOM.createTH();
        total.setClassName("stickerCell");
        total.addClassName(Constants.RIGHT_ALIGN_CELL);
        total.getStyle().setWidth(10, Style.Unit.PCT);
        total.appendChild(divElement);
        tr.appendChild(total);

        table.appendChild(tr);
    }

    private BigDecimal setReportData(List<WpsReportItem> items) {
        BigDecimal t = BigDecimal.ZERO;
        for (WpsReportItem item : items) {
            Element tr = DOM.createTR();

            Element employee = DOM.createTD();
            employee.setInnerText(item.getEmployeeName());
            tr.appendChild(employee);

            Element wpsNumber = DOM.createTD();
            wpsNumber.setInnerText(item.getWpsNumber());
            tr.appendChild(wpsNumber);

            Element creator = DOM.createTD();
            Element span = DOM.createSpan();
            span.addClassName("cell-decorator");
            span.setInnerText(item.getCreator());
            creator.appendChild(span);
            tr.appendChild(creator);

            Element approver = DOM.createTD();
            approver.setInnerText(item.getApprover().isEmpty() ? "N/A" : item.getApprover());
            tr.appendChild(approver);

            Element month = DOM.createTD();
            month.setInnerText(item.getMonth());
            tr.appendChild(month);

            Element year = DOM.createTD();
            year.setInnerText(String.valueOf(item.getYear()));
            tr.appendChild(year);

            Element total = DOM.createTD();
            total.addClassName(Constants.RIGHT_ALIGN_CELL);
            total.setInnerText(PayrollClientUtils.format(item.getTotal()));
            tr.appendChild(total);
            t = t.add(item.getTotal());

            tableBody.appendChild(tr);
        }
        return t;
    }

    private void setTotalRow(BigDecimal t) {
        Element totalTR = DOM.createTR();
        totalTR.addClassName("total_row");

        Element td = DOM.createTD();
        td.setInnerHTML(wfmStrings.total());
        td.setAttribute("colspan", "6");
        totalTR.appendChild(td);


        Element total = DOM.createTD();
        total.addClassName(Constants.TEXT_RIGHT);
        total.setInnerText(PayrollClientUtils.format(t));
        totalTR.appendChild(total);

        tableBody.appendChild(totalTR);
    }

    private void initInternal() {
        payrollGroup.addValueChangeHandler(event -> update());

        if (!yearMonthData.isEmpty()) {
            year.setItems(yearMonthData.keySet().toArray(new SelectItem[]{}));
            for (SelectItem it : yearMonthData.keySet()) {
                month.setItems(yearMonthData.get(it));
                break;
            }
        }
        year.addValueChangeHandler(changeEvent -> month.setItems(yearMonthData.get(year.getSelectedItem(true))));

        updateButton.addClickHandler(event -> update());

        update();
    }

    private void update() {
        lfp = new ListingFilterParameter();
        lfp.setYear(year.getSelectedId(true));
        lfp.setSelectedMonth(month.getSelectedId(true));
        lfp.setMonthName(month.getSelectedItem(true) != null ? month.getSelectedItem(true).getName() : "");
        lfp.setPayrollBatchID(payrollGroup.getSelectedItem() != null ? payrollGroup.getSelectedItem().getId() : null);
        lfp.setPaymentMethodId(payMethod.getSelectedItem() != null ? payMethod.getSelectedItem().getId() : null);

        LoadingPanel.loading(true);

        PayrollService.App.get().getWpsReportData(lfp, new AsyncCallback<WpsReportData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WpsReportData wpsReportData) {
                LoadingPanel.loading(false);
                tableHead.removeAllChildren();
                tableBody.removeAllChildren();
                noResultMessage.setInnerText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());

                createHeader(tableHead);

                if (wpsReportData.getWpsReportItems().size() > 0) {
                    noMessagePanel.setVisible(false);
                    BigDecimal total = setReportData(wpsReportData.getWpsReportItems());
                    setTotalRow(total);
                } else {
                    noMessagePanel.setVisible(true);
                }
                Utils.frame_affix();
            }
        });
    }

    private void onInitialize() {
        GBox gBox = headerPanel.drawNewGroupBox();
        gBox.setStyleUnited(true);
//        gBox.setStyleWidthFree(true);

        lfp = new ListingFilterParameter();

        year = new DataListBox();
        year.setWithoutNullLabel(true);
        headerPanel.addGroupBoxItem(wfmStrings.year(), year);

        month = new DataListBox();
        month.setWithoutNullLabel(true);
        headerPanel.addGroupBoxItem(wfmStrings.month(), month);

        payMethod = new DataListBox();
        headerPanel.addGroupBoxItem(wfmStrings.paymentMethod(), payMethod);

        payrollGroup = new DataListBox();
        GBoxItem payrollItem = headerPanel.addGroupBoxItem(payrollStrings.payrollGroup(), payrollGroup);
        payrollItem.setStyleSplitRight(true);

        /*MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadPayslipExcel";
            Utils.sendPDFOrExcelRequest(exportPanel, excelURL, lfp.getRequestParams(), "_blank");
        });

        MaterialLink exportSif = new MaterialLink();
        exportSif.addStyleName("hasicon--left");
        Icon sifIcon = new Icon();
        sifIcon.setClass("ficon--file-text");
        exportSif.add(sifIcon);
        exportSif.setText("SIF");
        exportSif.addClickHandler(clickEvent -> {
            String sifURL = CommandConstants.COMMON_URL + "/generateSifFile";
            Utils.sendPDFOrExcelRequest(exportPanel, sifURL, lfp.getRequestParams(), "_blank");
        });

        MaterialSplitButton exportButtons = new MaterialSplitButton(exportExl, Constants.BTN_DEFAULT, true);
        exportButtons.addItem(exportSif);

        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, exportButtons);
        exportItem.setStyleWidthFree(true);
        exportItem.setStyleSplitRight(true);*/

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);
        updateItem.getComponent().getElement().addClassName("group-box__item-content--no-border");

        exportSection();

        PayrollService.App.get().getYearMonthsForWps(new AsyncCallback<HashMap<SelectItem, SelectItem[]>>() {
            @Override
            public void onFailure(Throwable throwable) {
                initInternal();
            }

            @Override
            public void onSuccess(HashMap<SelectItem, SelectItem[]> map) {
                yearMonthData = map;
                initInternal();
            }
        });

        AllInOneService.App.get().getPaymentMethodList(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                payMethod.setItems(result);
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
        exportExl.addClickHandler(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadPayslipExcel";
            Utils.sendPDFOrExcelRequest(exportPanel, excelURL, lfp.getRequestParams(), "_blank");
        });

        MaterialLink exportSif = new MaterialLink();
        exportSif.addStyleName("hasicon--left");
        Icon sifIcon = new Icon();
        sifIcon.setClass("ficon--file-plus");
        exportSif.add(sifIcon);
        exportSif.setText("SIF");
        exportSif.addClickHandler(event -> {
            String sifURL = CommandConstants.COMMON_URL + "/generateSifFile";
            Utils.sendPDFOrExcelRequest(exportPanel, sifURL, lfp.getRequestParams(), "_blank");
        });

        showMenuContainer.add(exportExl);
        showMenuContainer.add(exportSif);

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        exportItem.setStyleWidthFree(true);
        exportItem.setStyleSplitRight(true);
        exportItem.setStyleName("group-box__item group-box__item--width-free");
    }

}