package com.edatasite.workforce.gwt.payroll.client.ui.view.report.salaryDetailedReport;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.enums.ReportDatesEnum;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollReportUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryDetailedReport  extends Composite implements Constants {
    interface SalaryDetailedReportUiBinder extends UiBinder<HTMLPanel, SalaryDetailedReport> {
    }
    private static final SalaryDetailedReportUiBinder ourUiBinder = GWT.create(SalaryDetailedReportUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private ListingFilterParameter lfp;
    private List<Date> financialQuartiesList;
    private Date financialYearStart;
    private Date currentDate;

    private DataListBox datesValue;
    private DatePicker fromValue;
    private DatePicker toValue;
    private EmployeeLookUpWithCode employee;

    private final HashMap<String, String> types = new HashMap<>();

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    HTMLPanel table;
//    @UiField
//    Element paymentsHeader;
    @UiField
    Element paymentsBody;
//    @UiField
//    Element paymentsFooter;
//    @UiField
//    Element deductionsHeader;
    @UiField
    Element deductionsBody;
    @UiField
    Element employerContributionBody;
//    @UiField
//    Element deductionsFooter;
    @UiField
    Element footer;
    @UiField
    HTMLPanel noMessagePanel;
    @UiField
    HeadingElement noResultMessage;

    SalaryDetailedReport() {
        initWidget(ourUiBinder.createAndBindUi(this));
        onInitialize();
    }

    private void onInitialize() {
        GBox filterBox = headerPanel.drawNewGroupBox();
        filterBox.setStyleUnited(true);
        filterBox.setStyleWidthFree(true);

        lfp = new ListingFilterParameter();
        lfp.setLimit(50);
        datesValue = new DataListBox();
        datesValue.setWithoutNullLabel(true);
        datesValue.setItems(PayrollReportUtils.getDatesListItems());
        datesValue.addValueChangeHandler(changeEvent -> PayrollReportUtils.setFromAndToDates(fromValue, toValue, datesValue.getSelectedId(), financialQuartiesList, financialYearStart));
        GBoxItem datesItem = headerPanel.addGroupBoxItem(0, wfmStrings.dates(), datesValue);
        datesItem.setStyleWidthFree(true);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        fromValue.ensureDebugId("profitAndLoss-startDate");
        fromValue.addChangeHandler(changeEvent -> datesValue.setSelected(ReportDatesEnum.Custom.getId()));
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);

        toValue = new DatePicker();
        toValue.ensureDebugId("profitsAndLoss-endDate  ");
        toValue.addChangeHandler(changeEvent -> datesValue.setSelected(ReportDatesEnum.Custom.getId()));
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        headerPanel.addGroupBoxItem(0, datePeriodItem);


        employee = new EmployeeLookUpWithCode();
        employee.showClearButton();
        employee.setClearCommand(this::update);
        headerPanel.addGroupBoxItem(0, wfmStrings.employee(), employee);

        noResultMessage.setInnerText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());

//        initExportPanel();

        initTypes();

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> update());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);

        LoadingPanel.loading(true);
        PayrollService.App.get().getFinancialYearDate(new AbstractAsyncCallback<Date>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
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
                LoadingPanel.loading(false);
            }
        });
    }

    private void update() {
        clearContent();
        hasContent(false);
        if (!Validation.validateLookUpRequired(employee)) {
            return;
        }
        lfp.setStartDateWithoutOffset(fromValue.getDate());
        lfp.setEndDateWithoutOffset(DateUtil.getDayLastTime(toValue.getDate()));
        lfp.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        lfp.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        lfp.setEmployeeId(employee.getSelectedItem().getId());

        LoadingPanel.loading(true);
        PayrollService.App.get().getSalaryDetailedReportData(lfp, new AbstractAsyncCallback<SalaryDetailedReportData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SalaryDetailedReportData data) {
                clearContent();
                if (data != null && (data.getPayments().size() > 0 || data.getDeductions().size() > 0 || data.getEmployerContribution().size() > 0)) {
                    hasContent(true);
                    BigDecimal paymentsTotal = BigDecimal.ZERO, deductionsTotal = BigDecimal.ZERO;
                    if (data.getPayments().size() > 0) {
//                        createGroupHeader(paymentsHeader, payrollStrings.payments());
                        for (Map.Entry<String, ArrayList<SalaryDetailedReportItem>> listEntry : data.getPayments().entrySet()) {
                            BigDecimal itemTotal = create(paymentsBody, listEntry.getKey(), listEntry.getValue());
                            paymentsTotal = paymentsTotal.add(itemTotal);
                        }
//                        createTotal(paymentsFooter, wfmMessages.total(payrollStrings.payments()), paymentsTotal);
                    }
                    if (data.getDeductions().size() > 0) {
//                        createGroupHeader(deductionsHeader, payrollStrings.deductions());
                        for (Map.Entry<String, ArrayList<SalaryDetailedReportItem>> listEntry : data.getDeductions().entrySet()) {
                            BigDecimal itemTotal = create(deductionsBody, listEntry.getKey(), listEntry.getValue());
                            if (!PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(listEntry.getKey())) {
                                deductionsTotal = deductionsTotal.add(itemTotal);
                            }
                        }
//                        createTotal(deductionsFooter, wfmMessages.total(payrollStrings.deductions()), deductionsTotal);
                    }
                    createTotal(footer, wfmStrings.total(), paymentsTotal.subtract(deductionsTotal));

                    if (data.getEmployerContribution().size() > 0) {
                        for (Map.Entry<String, ArrayList<SalaryDetailedReportItem>> listEntry : data.getEmployerContribution().entrySet()) {
                            create(employerContributionBody, listEntry.getKey(), listEntry.getValue());
                        }
                    }

                } else {
                    hasContent(false);
                }
                LoadingPanel.loading(false);
                Utils.table__frame_affix_init();
            }
        });

    }

    private void initTypes() {
        types.put(PayrollConstants.CATEGORY_PAYMENT, wfmStrings.payments());
        types.put(PayrollConstants.CATEGORY_MATERIAL_AID, payrollStrings.materialAid());
        types.put(PayrollConstants.CATEGORY_DEDUCTION, wfmStrings.deductions());
        types.put(PayrollConstants.CATEGORY_TAX, wfmStrings.taxes());
        types.put(PayrollConstants.CATEGORY_LOAN, wfmStrings.loans());
        types.put(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION, wfmStrings.employerContribution());
    }

    private void createGroupHeader(Element element, String name) {
        Element tr = DOM.createTR();
        Element th = DOM.createTH();
        th.setInnerHTML(name);
        tr.appendChild(th);
        Element td1 = DOM.createTD();
        td1.setInnerHTML("&nbsp;");
        tr.appendChild(td1);
        element.appendChild(tr);
    }

    private BigDecimal create(Element element, String code, ArrayList<SalaryDetailedReportItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items != null && items.size() > 0) {
            Element table = DOM.createTable();

            String headerText = types.get(code);

            Element tHead = DOM.createTHead();
            createGroupHeader(tHead, headerText);
            table.appendChild(tHead);

            Element tBody = DOM.createTBody();
            for (SalaryDetailedReportItem item : items) {
                Element tr = DOM.createTR();
                Element td = DOM.createTD();
                td.setInnerHTML(item.getCategoryName());
                tr.appendChild(td);
                Element td1 = DOM.createTD();
                td1.addClassName(Constants.RIGHT_ALIGN_CELL);
                td1.appendChild(getDOMLink(item.getTotal(), item.getRelationId(), item.getRelationType()));
                tr.appendChild(td1);
                tBody.appendChild(tr);

                total = total.add(item.getTotal());
            }

            table.appendChild(tBody);
            element.appendChild(table);

            Element tFoot = DOM.createTFoot();
            table.appendChild(tFoot);
            createTotal(tFoot, wfmMessages.total(headerText), total);
        }
        return total;
    }

    private void createTotal(Element element, String text, BigDecimal total) {
        Element tr = DOM.createTR();
        Element th = DOM.createTH();
        th.setInnerHTML(text);
        tr.appendChild(th);
        Element td1 = DOM.createTD();
        td1.addClassName(Constants.RIGHT_ALIGN_CELL);
        td1.setInnerHTML(getValueAsString(total));
        tr.appendChild(td1);
        element.appendChild(tr);
    }

    private Element getDOMLink(BigDecimal value, final Integer relationId, final String relationType) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(getValueAsString(value));
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        String url;
        if ("PAYSLIP".equals(relationType)) {
            url = "singlePayrun|viewPayslip/" + relationId;
        } else if ("ADDITIONAL".equals(relationType)) {
            url = "additionalPayment|view/" + relationId;
        } else {
            url = null;
        }
        if (url != null) {
            DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged(url));
        }

        return link;
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return " " + AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private void clearContent() {
//        paymentsHeader.removeAllChildren();
        paymentsBody.removeAllChildren();
//        paymentsFooter.removeAllChildren();

//        deductionsHeader.removeAllChildren();
        deductionsBody.removeAllChildren();
        employerContributionBody.removeAllChildren();
//        deductionsFooter.removeAllChildren();
        footer.removeAllChildren();
    }

    private void hasContent(boolean has) {
        table.setVisible(has);
        noMessagePanel.setVisible(!has);
    }
}
