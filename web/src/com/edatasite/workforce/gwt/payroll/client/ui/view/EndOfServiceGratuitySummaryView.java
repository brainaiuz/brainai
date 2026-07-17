package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.ui.EndOfServiceCalculationView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 27.08.14
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceGratuitySummaryView extends EndOfServiceCalculationView {
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private Integer objectID;
    private HTML employee, date, reason, paymentNumber;

    public EndOfServiceGratuitySummaryView(Integer objectID, boolean fromSaudi) {
        super(fromSaudi);
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    public void init() {
        employee = new HTML();
        date = new HTML();
        reason = new HTML();
        paymentNumber = new HTML();
        calculationTable = new EditableTable(getColumns(), false);

        addTitleField(PAYROLL.EOS_GRATUITY, payrollStrings.endOfServiceGratuityCalculation());
        addField(PAYROLL_STARTER.EMPLOYEE, employee, getTitle(wfmStrings.employee()));
        addField(CREATED_DATE, date, getTitle(wfmStrings.date()));
        addField(PAYROLL_STARTER.PAYMENT_NUMBER, paymentNumber, getTitle(wfmStrings.number()));
        addField(REASON, reason, getTitle(wfmStrings.reason()));
        addField(PAYROLL_STARTER.CALCULATION_TABLE, calculationTable);
    }

    @Override
    public void setValues(EoSCalculationData data) {
        employee.setText(data.getEmployee().getName());
        date.setText(DateUtils.format(data.getDate().getNonConvertedDate()));
        reason.setText(Constants.EMPLOYEE_RESIGNATION.equals(data.getReasonCode()) ? wfmStrings.employeeResignation() : wfmStrings.contractTermination());
        paymentNumber.setText(data.getPaymentNumber() != null ? data.getPaymentNumber() : "");
        calculationTable.addRow(getWidgets(data));
    }

    @Override
    public ColumnConfig[] getColumns() {
        return super.getColumns();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.close(), (ClickHandler) clickEvent -> closeTab());
    }

    @Override
    protected void getDataToFillFields() {
        PayrollService.App.get().getEndOfServiceGratuity(objectID, new AbstractAsyncCallback<EoSCalculationData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(EoSCalculationData result) {
                setValues(result);
            }
        });
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return super.getWikiCode();
    }

    @Override
    public String getIconStyle() {
        return super.getIconStyle();
    }

    @Override
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
}
