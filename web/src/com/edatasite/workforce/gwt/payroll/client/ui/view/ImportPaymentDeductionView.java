package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_PAYMENT_DEDUCTION_FORM;

public class ImportPaymentDeductionView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final String PAYMENTS = "PAYMENTS";
    private final String type;
    private final String importPDView = "import_payment_deduction_view_";
    private DataListBox code;
    private DataListBox name;
    private DataListBox useIn;
    private DataListBox debitAccount;
    private DataListBox creditAccount;

    public ImportPaymentDeductionView(Integer objectId, String type) {
        super("importpaymentdeductionadd", "Import " + (type.equals(PAYMENTS) ? "Payment" : "Deduction"));
        this.objectId = objectId;
        this.type = type;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        code = new DataListBox();
        code.ensureDebugId(importPDView + "code");
        code.addStyleName(DEFAULT_WIDTH);

        name = new DataListBox();
        name.ensureDebugId(importPDView + "name");
        name.addStyleName(DEFAULT_WIDTH);

        useIn = new DataListBox();
        useIn.ensureDebugId(importPDView + "useIn");
        useIn.addStyleName(DEFAULT_WIDTH);

        debitAccount = new DataListBox();
        debitAccount.ensureDebugId(importPDView + "debitAccount");
        debitAccount.addStyleName(DEFAULT_WIDTH);

        creditAccount = new DataListBox();
        creditAccount.ensureDebugId(importPDView + "creditAccount");
        creditAccount.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.CODE, code, getTitle(wfmStrings.code(), true));
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.USE_IN, useIn, getTitle(type.equals(PAYMENTS) ? payrollStrings.isDefaultCategory() : Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.isCashAdvance(), wfmStrings.cashAdvance()), false));
        addField(CustomFormConstants.DEBIT_TO_ACCOUNT, debitAccount, getTitle(wfmStrings.debitToAccount(), false));
        addField(CustomFormConstants.CREDIT_TO_ACCOUNT, creditAccount, getTitle(wfmStrings.creditToAccount(), false));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Project;
    }

    @Override
    public void setItems(SelectItem[] items) {
        code.setItems(items, wfmStrings.code());
        name.setItems(items, wfmStrings.name());
        useIn.setItems(items, type.equals(PAYMENTS) ? payrollStrings.isDefaultCategory() : Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.isCashAdvance(), wfmStrings.cashAdvance()));
        debitAccount.setItems(items, wfmStrings.debitToAccount());
        creditAccount.setItems(items, wfmStrings.creditToAccount());
    }

    private ImportFile createColumns(CategoryObject item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.PaymentDeductionFields.FIELD_CATEGORY_NAME, item.getNameId() != null ? item.getNameId() : -1);
        importFile.addColumn(ImportField.PaymentDeductionFields.FIELD_CATEGORY_CODE, item.getCodeId() != null ? item.getCodeId() : -1);
        importFile.addColumn(ImportField.PaymentDeductionFields.FIELD_USE_IN, item.getUseInId() != null ? item.getUseInId() : -1);
        importFile.addColumn(ImportField.PaymentDeductionFields.FIELD_DEBIT_TO_ACCOUNT, item.getDebitToAccountID() != null ? item.getDebitToAccountID() : -1);
        importFile.addColumn(ImportField.PaymentDeductionFields.FIELD_CREDIT_TO_ACCOUNT, item.getCreditToAccountID() != null ? item.getCreditToAccountID() : -1);

        return importFile;
    }

    private CategoryObject getRPC() {
        CategoryObject item = new CategoryObject();
        item.setId(objectId);
        item.setCodeId(getSelectedItem(code));
        item.setNameId(getSelectedItem(name));
        item.setUseInId(getSelectedItem(useIn));
        item.setCreditToAccountID(getSelectedItem(creditAccount));
        item.setDebitToAccountID(getSelectedItem(debitAccount));
        return item;
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    @Override
    protected ImportTypeEnum getType() {
        return type.equals(PAYMENTS) ? ImportTypeEnum.PAYMENT : ImportTypeEnum.DEDUCTION;
    }

    @Override
    protected String getFormID() {
        return IMPORT_PAYMENT_DEDUCTION_FORM;
    }

    @Override
    protected String getFormType() {
        return IMPORT;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
