package com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowActionItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by shohruh on 25-Mar-17.
 */
public class WorkflowActionItemWidget implements Constants.WorkflowActionConstants.Type {
    private static final NumberFormat numberFormat = Utils.getNumberFormat();

    private int dataType;
    private Label label;
    private DataListBox fieldsListBox;
    private Widget defaultValue;
    private final WorkflowActionItem item;

    public WorkflowActionItemWidget(FlexTable table, String name, SelectItem[] fields, Integer mappedId, WorkflowActionItem actionItem, int dataType, boolean required, String... params) {
        super();
        this.dataType = dataType;
        this.item = actionItem != null ? actionItem : new WorkflowActionItem();
        item.setMappedId(mappedId);

        label = new Label(name);
        fieldsListBox = new DataListBox();

        switch (dataType) {
            case TEXT:
                defaultValue = new TextBox();
                if (item.getDefaultText() != null) ((TextBox) defaultValue).setText(item.getDefaultText());
                break;
            case TEXT_2:
                defaultValue = new TextArea();
                if (item.getDefaultText() != null) {
                    ((TextArea) defaultValue).setText(item.getDefaultText());
                }
                break;
            case NUMERIC:
                defaultValue = new TextBox();
                Validation.addNumericKeyboardListener((TextBox) defaultValue);
                if (item.getDefaultNumeric() != null)
                    ((TextBox) defaultValue).setText(formatNumber(item.getDefaultNumeric()));
                break;
            case DATE:
                defaultValue = new DatePicker();
                if (item.getDefaultDate() != null) ((DatePicker) defaultValue).setDate(item.getDefaultDate());
                break;
            case CASH_ADVANCE_CATEGORY:
                defaultValue = new CategoryLookUp("Deduction", true);
                if (item.getDefaultId() != null)
                    ((CategoryLookUp) defaultValue).setSelected(new SelectItem(item.getDefaultId(), item.getDefaultText()));
                break;
            case EMP_LOOKUP:
            case APPROVER_LOOKUP:
                defaultValue = new EmployeeLookUpWithCode();
                if (item.getDefaultId() != null)
                    ((EmployeeLookUpWithCode) defaultValue).setSelected(new SelectItem(item.getDefaultId(), item.getDefaultText()));
                break;
            case PAYROLL_CATEGORY_PAYMENT:
                defaultValue = new CategoryLookUp("Payment");
                if (item.getDefaultId() != null)
                    ((CategoryLookUp) defaultValue).setSelected(new SelectItem(item.getDefaultId(), item.getDefaultText()));
                break;
            case PAYROLL_CATEGORY_DEDUCTION:
                defaultValue = new CategoryLookUp("Deduction");
                if (item.getDefaultId() != null)
                    ((CategoryLookUp) defaultValue).setSelected(new SelectItem(item.getDefaultId(), item.getDefaultText()));
                break;
            case ACCOUNT_LOOKUP:
                defaultValue = new AccountsLookUp();
                if (item.getDefaultId() != null)
                    ((AccountsLookUp) defaultValue).setSelected(new SelectItem(item.getDefaultId(), item.getDefaultText()));
                break;
            case CERTIFICATE_LIST_BOX:
                defaultValue = new DataListBox();
                HrmsService.App.get().getCertificateTypes(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void onSuccess(SelectItem[] result) {
                        ((DataListBox) defaultValue).setItems(result);
                        if (item.getDefaultId() != null) {
                            ((DataListBox) defaultValue).setSelected(item.getDefaultId());
                        }
                    }
                });
                break;
        }

        fieldsListBox.setItems(fields);
        if (item.getFieldId() != null) fieldsListBox.setSelected(item.getFieldId());

        label.setWidth("180px");
        fieldsListBox.setWidth("180px");
        defaultValue.setWidth("180px");

        int rowCount = table.getRowCount();
        table.setWidget(rowCount, 0, label);
        table.setWidget(rowCount, 1, fieldsListBox);
        table.setWidget(rowCount, 2, defaultValue);
    }

    public WorkflowActionItem getRPC() {
        item.setFieldId(fieldsListBox.getSelectedId());
        switch (dataType) {
            case TEXT:
                item.setDefaultText(((TextBox) defaultValue).getText());
                break;
            case TEXT_2:
                item.setDefaultText(((TextArea) defaultValue).getText());
                break;
            case NUMERIC:
                item.setDefaultNumeric(parseToBigDecimal(((TextBox) defaultValue).getText()));
                break;
            case DATE:
                item.setDefaultDate(((DatePicker) defaultValue).getDate());
                break;
            case CASH_ADVANCE_CATEGORY:
            case PAYROLL_CATEGORY_PAYMENT:
            case PAYROLL_CATEGORY_DEDUCTION:
            case ACCOUNT_LOOKUP:
            case EMP_LOOKUP:
            case APPROVER_LOOKUP:
                if (((LookUp) defaultValue).getSelectedItem() != null) {
                    item.setDefaultId(((LookUp) defaultValue).getSelectedItem().getId());
                    item.setDefaultText(((LookUp) defaultValue).getSelectedItem().getName());
                }
                break;
            case CERTIFICATE_LIST_BOX:
                if (((DataListBox) defaultValue).getSelectedItem() != null) {
                    item.setDefaultId(((DataListBox) defaultValue).getSelectedItem().getId());
                    item.setDefaultText(((DataListBox) defaultValue).getSelectedItem().getName());
                }
                break;
        }
        return item;
    }

    public static BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            return new BigDecimal(numberFormat.parse(text));
        }
        return null;
    }

    public static String formatNumber(BigDecimal amount) {
        return numberFormat.format(amount);
    }
}
