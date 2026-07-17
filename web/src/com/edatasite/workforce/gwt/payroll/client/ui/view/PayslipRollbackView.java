package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeePayrollSettingsObject;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeServiceAsync;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollServiceAsync;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Apr 15, 2010
 * Time: 6:05:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayslipRollbackView extends DialogBox {

    static final PayrollStrings strings = PayrollStrings.App.get();
    static final PayrollServiceAsync payrollService = PayrollService.App.get();
    static final EmployeeServiceAsync employeeService = EmployeeService.App.get();
    static final WfmStrings wfmStrings = WfmStrings.App.get();
    private FlexTable table;

    private DataListBox employees;
    private DataListBox taxYears;
    private DataListBox months;
    private DataListBox weeks;

    private DialogBox dialogBox;

    private boolean refresh = false;

    public PayslipRollbackView() {
        super(false, true);
        load();
    }

    public void load() {
        table = new FlexTable();
        table.getElement().setAttribute("style", "padding-left: 5px;");
        table.setWidth("100%");

        employees = new DataListBox();
        employeeService.getCompanyEmployeesForPayroll(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] o) {
                employees.setNullLabel(wfmStrings.allEmployees());
                employees.setItems(o);
                /*employees.setSelectedNullLabel();*/

                table.setWidget(0, 0, new HTMLPanel(wfmStrings.chooseEmployee()));
                table.getFlexCellFormatter().setWidth(0, 0, "150px");
                table.getFlexCellFormatter().setWordWrap(0, 0, false);
                table.setWidget(0, 1, employees);
            }
        });
        employees.addValueChangeHandler(event -> {
            if (employees.getSelectedIndex() != 0 && employees.getSelectedItem().getId() != null) {
                payrollService.getEmployeePayrollSettings(employees.getSelectedItem().getId(), Constants.PAY_FREQUENCY, new AbstractAsyncCallback<EmployeePayrollSettingsObject>() {
                    @Override
                    public void success(EmployeePayrollSettingsObject result) {
                        if ("Monthly".equals(result.getValue())) {
                            months.setEnabled(true);
                            weeks.setEnabled(false);
                        } else {
                            weeks.setEnabled(true);
                            months.setEnabled(false);
                        }
                    }
                });
            } else {
                weeks.setEnabled(true);
                months.setEnabled(true);
            }
        });

        taxYears = new DataListBox();
        taxYears.setWidth("100px");
        taxYears.setWithoutNullLabel(true);
        payrollService.getCompanyTaxYears(null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                taxYears.setItems(result);
                taxYears.setSelected(result[0]);
                /*if(result.length==1){
                    taxYears.setEnabled(false);
                }*/
                table.setWidget(1, 0, new HTMLPanel((taxYears.getItems().length > 1 ? wfmStrings.choose() : "") + wfmStrings.taxYear()));
                table.setWidget(1, 1, taxYears);
            }
        });

        months = new DataListBox();
        months.setWidth("100px");
        months.setItems(PayrollClientUtils.getMonths());
        months.setSelected(months.getItems()[0]);

        weeks = new DataListBox();
        weeks.setWidth("100px");
        weeks.setItems(PayrollClientUtils.getWeeks());
        weeks.setSelected(weeks.getItems()[0]);

        final WfmButton2 rollback = new WfmButton2(Utils.isUKCompany() ? strings.rollback() : wfmStrings.delete(), event -> {
            initPopUp();
            dialogBox.show();
        });

        final WfmButton2 close = new WfmButton2(wfmStrings.close(), event -> hide());

        table.getFlexCellFormatter().setColSpan(2, 0, 2);
        table.setWidget(2, 0, new HTMLPanel(Utils.isUKCompany() ? strings.chooseThePeriodToRollbackTheP11EntriesTo() : strings.choosePeriodToDeletePayslip()));

        table.setWidget(3, 0, new HTMLPanel(wfmStrings.month()));
        table.setWidget(3, 1, months);

        table.setWidget(4, 0, new HTMLPanel(wfmStrings.week()));
        table.setWidget(4, 1, weeks);

        table.getFlexCellFormatter().setColSpan(5, 0, 2);
        table.setWidget(5, 0, new HTMLPanel(""));
        table.getFlexCellFormatter().setHeight(5, 0, "20px");

        /*table.getFlexCellFormatter().setColSpan(5, 0, 2);*/
        table.setWidget(6, 0, rollback);
        /*table.getFlexCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);*/
        table.getFlexCellFormatter().getElement(6, 0).setAttribute("style", "float:right");
        table.setWidget(6, 1, close);

        /*add(table);*/
        setText(Utils.isUKCompany() ? strings.rollbackP11() : wfmStrings.delete());
        setWidget(table);
        setSize("350px", "200px");
        center();
    }

    public void initPopUp() {
        final String append = getDetails() + " ?";
        if (dialogBox != null && dialogBox.getWidget() != null) {
            ((FlexTable) dialogBox.getWidget()).setWidget(0, 0, new HTMLPanel(Utils.isArabicCompany() ? strings.areYouSureToDeleteP11() + append : strings.areYouSureToRollbackP11() + append));
            return;
        }
        dialogBox = new DialogBox(false, true);
        dialogBox.setWidth("260px");
        dialogBox.setText(strings.areYouSure());
        /*dialogBox.setStyleName("payroll-Gwt-DialogBox");*/

        final WfmButton2 yes = new WfmButton2(wfmStrings.yes(), event -> {
            if (weeks.isEnabled()) {
                rollback(0, !months.isEnabled());/*weekly*/
            }
            if (months.isEnabled()) {
                rollback(1, true);/*monthly*/
            }
            dialogBox.hide();
        });
        final WfmButton2 no = new WfmButton2(wfmStrings.no(), event -> dialogBox.hide());
        final FlexTable dialogBoxContent = new FlexTable();
        dialogBoxContent.getFlexCellFormatter().setColSpan(0, 0, 2);
        dialogBoxContent.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        dialogBoxContent.setWidget(0, 0, new HTMLPanel(Utils.isArabicCompany() ? strings.areYouSureToDeleteP11() + append : strings.areYouSureToRollbackP11() + append));
        dialogBoxContent.getFlexCellFormatter().getElement(0, 0).setAttribute("style", "text-align: center;");

        dialogBoxContent.setWidget(2, 0, yes);
        dialogBoxContent.getFlexCellFormatter().getElement(2, 0).setAttribute("style", "float:right");
        dialogBoxContent.setWidget(2, 1, no);
        dialogBox.setWidget(dialogBoxContent);
        dialogBox.center();
    }

    private void rollback(final int frequency, final boolean showMessage) {
        payrollService.rollback(frequency, months.getSelectedItem().getId(), taxYears.getSelectedItem().getId(), employees.getSelectedItem() != null ? employees.getSelectedItem().getId() : null, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                if (showMessage) {
                    table.setWidget(5, 0, new HTMLPanel(strings.rollbackfailed() + getDetails() + ". " + wfmStrings.reason() + ": " + throwable.getMessage()));
                    table.getFlexCellFormatter().setHeight(5, 0, "40px");
                    table.getWidget(5, 0).getElement().setAttribute("style", "text-align: center; color: red; font-weight: bold;");
                }
            }

            @Override
            public void success(Integer result) {
                if (showMessage) {
                    table.setWidget(5, 0, new HTMLPanel(strings.youHaveSuccessfullyRolledBack() + result + "P11" + strings.entries() + getDetails()));
                    table.getFlexCellFormatter().setHeight(5, 0, "40px");
                    table.getWidget(5, 0).getElement().setAttribute("style", "text-align: center; color: green; font-weight: bold;");
                }
                refresh = true;
            }
        });
    }

    private String getDetails() {
        boolean isMonthEnabled = months.isEnabled() && months.isSomethingSelected();
        boolean isWeekEnabled = weeks.isEnabled() && weeks.isSomethingSelected();
        return " " + wfmStrings.forLocalize() + "  " + (employees.getSelectedItem() != null ? employees.getSelectedItem().getName() : employees.getNullLabel()) + " " + wfmStrings.to() + " " + (isMonthEnabled ? months.getSelectedItem().getName() + (isWeekEnabled ? "/" : "") : "") + (isWeekEnabled ? weeks.getSelectedItem().getName() : "") + " " + strings.inclusively();
    }

    public boolean isRefresh() {
        return refresh;
    }
}
