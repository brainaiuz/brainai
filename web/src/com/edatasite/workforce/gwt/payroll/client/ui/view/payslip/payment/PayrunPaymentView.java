package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollServiceAsync;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayrunPaymentView extends PayrunPaymentAddView {
    protected static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    protected static final PayrollServiceAsync payrollService = PayrollService.App.get();

    private Integer objectID;

    public PayrunPaymentView(Integer id) {
        this("view");
        objectID = id;
        setDescription(wfmStrings.payment());
    }

    public PayrunPaymentView(String name) {
        super(name);
    }

    @Override
    protected void initialize() {
        super.initialize();
        disableFields();
    }

    @Override
    protected void loadData() {
        ListingFilterParameter fp = getFilterParameter();
        PayrollService.App.get().getPayrunPayment(fp, new AsyncCallback<PayrunPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PayrunPayment result) {
                LoadingPanel.loading(false);
                paymentObject = result;
                setMainData(result);
                setTableData(result);
                setTotalData(result.getAmount());
                show();
            }
        });
    }

    private void setMainData(PayrunPayment result) {
        paymentDate.setDate(result.getPaymentDate().getDate());

        paidFromAccountLookUp.addItem(result.getPaidFromAccount());
        paidFromAccountLookUp.setSelected(result.getPaidFromAccountID());

        paidToAccountLookUp.addItem(result.getPaidToAccount());
        paidToAccountLookUp.setSelected(result.getPaidToAccountID());

        detailsBox.setValue(result.getDetails());
        currencyWidget.setCurrency(result.getCurrency());
    }

    @Override
    protected ListingFilterParameter getFilterParameter() {
        ListingFilterParameter fp = super.getFilterParameter();
        fp.setObjectId(objectID);
        return fp;
    }

    private void disableFields() {
        paymentDate.setEnabled(false);
        paidFromAccountLookUp.setEnabled(false);
        paidToAccountLookUp.setEnabled(false);

        detailsBox.setEnabled(false);
        currencyWidget.setEnabled(false);
    }

    @Override
    protected void setColumns() {
        columnsMap = new HashMap<>();
        columnsMap.put(PayrunPaymentConstants.EMPLOYEE, new ColumnConfig(LinkableCell.class, PayrunPaymentConstants.EMPLOYEE, wfmStrings.employee(), 180, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.REFERENCE, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.REFERENCE, wfmStrings.reference(), 80, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.PAID_FROM, new ColumnConfig(LookUpCell.class, PayrunPaymentConstants.PAID_FROM, wfmStrings.paidFrom(), 120, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.PAID_TO, new ColumnConfig(LookUpCell.class, PayrunPaymentConstants.PAID_TO, wfmStrings.paidTo(), 120, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.DETAILS, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.DETAILS, wfmStrings.details(), 120, false, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.PAYMENT_AMOUNT, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.PAYMENT_AMOUNT, wfmStrings.paymentAmount(), 120, false, Constants.RIGHT_ALIGN_CELL));
    }

    @Override
    protected Object[] getWidgets(PayrunPaymentItem item) {
        List<Widget> widgets = new ArrayList<>();
        itemMap.put(item.getEmployeeID(), item);

        LinkCellWidget employeeCell = new LinkCellWidget(item.getEmployee(), null);
        employeeCell.setItem(new SelectItem(item.getEmployeeID(), item.getEmployee()));
        widgets.add(employeeCell);

        if (columnsMap.containsKey(PayrunPaymentConstants.REFERENCE)) {
            CustomCellLabel reference = new CustomCellLabel();

            if (item.getReference() != null) {
                reference.setText(item.getReference());
            }
            widgets.add(reference);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.PAID_FROM)) {
            PaymentAccountsLookUp paidFromAccountLookUp = new PaymentAccountsLookUp(true);
            paidFromAccountLookUp.setEnabled(false);

            if (item.getPaidFromAccountID() != null) {
                paidFromAccountLookUp.addItem(item.getPaidFromAccount());
                paidFromAccountLookUp.setSelected(item.getPaidFromAccountID());
            } else if (this.paidFromAccountLookUp.getSelectedItemID() != null) {
                SelectItem paidFromAccount = this.paidFromAccountLookUp.getSelectedItem();

                paidFromAccountLookUp.addItem(paidFromAccount);
                paidFromAccountLookUp.setSelected(this.paidFromAccountLookUp.getSelectedItemID());
            }
            widgets.add(paidFromAccountLookUp);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.PAID_TO)) {
            AccountsLookUp paidToAccountLookUp = new AccountsLookUp();
            paidToAccountLookUp.setEnabled(false);

            if (item.getPaidToAccountID() != null) {
                paidToAccountLookUp.addItem(item.getPaidToAccount());
                paidToAccountLookUp.setSelected(item.getPaidToAccountID());
            } else if (this.paidToAccountLookUp.getSelectedItemID() != null) {
                SelectItem paidToAccount = this.paidToAccountLookUp.getSelectedItem();

                paidToAccountLookUp.addItem(paidToAccount);
                paidToAccountLookUp.setSelected(this.paidToAccountLookUp.getSelectedItemID());
            }
            widgets.add(paidToAccountLookUp);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.DETAILS)) {
            CustomCellLabel details = new CustomCellLabel();

            if (item.getDetails() != null) {
                details.setText(item.getDetails());
            }
            widgets.add(details);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.PAYMENT_AMOUNT)) {
            CustomCellLabel paymentAmount = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));

            if (item.getPaymentAmount() != null) {
                paymentAmount.setText(PayrollClientUtils.format(item.getPaymentAmount()));
            }
            widgets.add(paymentAmount);
        }

        return widgets.toArray(new Object[]{});
    }

    @Override
    protected void initButtons() {

    }

    @Override
    public String getFormType() {
        return LayoutRPC.VIEW;
    }
}
