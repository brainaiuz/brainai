package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.ManualTransactionImportItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dilsh0d Madrahimov  on 10-Feb-17.
 */
public class ImportBankTransferView extends CustomForm2 implements Constants, Colapse {

    private final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private KpiSwitcher csvHeaderBox;
    private DataListBox bankAccountListBox;
    private DataListBox cashAccountListBox;
    private DataListBox narrationListBox;
    private DataListBox referenceListBox;
    private DataListBox dateListBox;
    private DataListBox accountListBox;
    private DataListBox descriptionListBox;
    private DataListBox amountListBox;
    private DataListBox nameListBox;
    private DataListBox departmentListBox;
    private DataListBox taxCalculationTypeListBox;
    private DataListBox taxNameListBox;
    private DataListBox projectListBox;

    private final Integer objectID;
    private final String viewType;

    public ImportBankTransferView(Integer objectID, String viewType) {
        super("add", "Import Transaction");
        this.objectID = objectID;
        this.viewType = viewType;
    }

    private SelectItem[] items;
    private char defaultSeparator = ',';

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return this;
    }

    @Override
    protected void registerFields() {

        csvHeaderBox = new KpiSwitcher();
        csvHeaderBox.setValue(Boolean.TRUE);

        bankAccountListBox = new DataListBox();
        bankAccountListBox.addStyleName(DEFAULT_WIDTH);

        cashAccountListBox = new DataListBox();
        cashAccountListBox.addStyleName(DEFAULT_WIDTH);

        narrationListBox = new DataListBox();
        narrationListBox.addStyleName(DEFAULT_WIDTH);

        referenceListBox = new DataListBox();
        referenceListBox.addStyleName(DEFAULT_WIDTH);

        dateListBox = new DataListBox();
        dateListBox.addStyleName(DEFAULT_WIDTH);

        accountListBox = new DataListBox();
        accountListBox.addStyleName(DEFAULT_WIDTH);

        descriptionListBox = new DataListBox();
        descriptionListBox.addStyleName(DEFAULT_WIDTH);

        amountListBox = new DataListBox();
        amountListBox.addStyleName(DEFAULT_WIDTH);

        nameListBox = new DataListBox();
        nameListBox.addStyleName(DEFAULT_WIDTH);

        departmentListBox = new DataListBox();
        departmentListBox.addStyleName(DEFAULT_WIDTH);

        taxCalculationTypeListBox = new DataListBox();
        taxCalculationTypeListBox.addStyleName(DEFAULT_WIDTH);

        taxNameListBox = new DataListBox();
        taxNameListBox.addStyleName(DEFAULT_WIDTH);

        projectListBox = new DataListBox();
        projectListBox.addStyleName(DEFAULT_WIDTH);

        String requiredInformation = "Required Information";
        addTitleField(ManualTransactionImport.REQUIRED_INFORMATIONS, getTitle(requiredInformation));
        String optionalInformation = "Optional Information";
        addTitleField(ManualTransactionImport.OPTIONAL_INFORMATIONS, getTitle(optionalInformation));
        addField(ManualTransactionImport.HAS_CSV_HEADER, csvHeaderBox, wfmStrings.myCSVFileHasHeaders());
        if (AccountingConstants.CASH_RECEIPT_STR.equals(viewType) || AccountingConstants.CASH_PAYMENT_STR.equals(viewType)) {
            addField(CASH_ACCOUNT_NAME, cashAccountListBox, getTitle(accountingStrings.cashAccountCode(), true));
        } else {
            addField(BANK_ACCOUNT_NAME, bankAccountListBox, getTitle(accountingStrings.bankAccountCode(), true));
        }
        addField(ManualTransactionImport.NARRATION, narrationListBox, getTitle(wfmStrings.narration()));
        addField(ManualTransactionImport.REFERENCE, referenceListBox, getTitle(wfmStrings.reference()));
        addField(ManualTransactionImport.DATE, dateListBox, getTitle(wfmStrings.date(), true));
        addField(ManualTransactionImport.ACCOUNT_CODE, accountListBox, getTitle(wfmStrings.accountCode(), true));
        addField(ManualTransactionImport.DESCRIPTION, descriptionListBox, getTitle(wfmStrings.description()));
        addField(ManualTransactionImport.AMOUNT, amountListBox, getTitle(wfmStrings.amount(), true));
        addField(ManualTransactionImport.NAME, nameListBox, getTitle(wfmStrings.name()));
        addField(ManualTransactionImport.DEPARTMENT, departmentListBox, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        addField(ManualTransactionImport.TAX_CALC_TYPE, taxCalculationTypeListBox, getTitle(accountingStrings.taxCalculationType()));
        addField(ManualTransactionImport.TAX_VALUE, taxNameListBox, getTitle(wfmStrings.taxRate()));
        addField(ManualTransactionImport.PROJECT_CODE, projectListBox, getTitle(wfmStrings.projectCode()));

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null && !fieldID.isEmpty()) {
            if (ManualTransactionImport.REQUIRED_INFORMATIONS.equals(fieldID)) {
                return "Required Information";
            } else if (ManualTransactionImport.OPTIONAL_INFORMATIONS.equals(fieldID)) {
                return "Optional Information";
            } else if (BANK_ACCOUNT_NAME.equals(fieldID)) {
                return accountingStrings.bankAccountCode();
            } else if (CASH_ACCOUNT_NAME.equals(fieldID)) {
                return accountingStrings.cashAccountCode();
            } else if (ManualTransactionImport.NARRATION.equals(fieldID)) {
                return wfmStrings.narration();
            } else if (ManualTransactionImport.REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (ManualTransactionImport.DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (ManualTransactionImport.ACCOUNT_CODE.equals(fieldID)) {
                return wfmStrings.accountCode();
            } else if (ManualTransactionImport.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (ManualTransactionImport.AMOUNT.equals(fieldID)) {
                return wfmStrings.amount();
            } else if (ManualTransactionImport.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (ManualTransactionImport.DEPARTMENT.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            } else if (ManualTransactionImport.PROJECT_CODE.equals(fieldID)) {
                return wfmStrings.projectCode();
            }
        }

        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), clickEvent -> save());
//        addButton(accountingStrings.cancel(), (ClickHandler) clickEvent -> closeTab());
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getCSVColumns(objectID, new AsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(HashMap<String, SelectItem[]> map) {
                LoadingPanel.loading(false);

                for (Map.Entry<String, SelectItem[]> entry : map.entrySet()) {
                    String key = entry.getKey();
                    items = entry.getValue();

                    if (!key.equals(String.valueOf(defaultSeparator))) {
                        defaultSeparator = key.charAt(0);
                    }

                    setItems(items, bankAccountListBox, cashAccountListBox, dateListBox, narrationListBox, referenceListBox, accountListBox, descriptionListBox, amountListBox, nameListBox, departmentListBox, taxCalculationTypeListBox, taxNameListBox, projectListBox);
                }
            }
        });
    }

    private void setItems(SelectItem[] items, final DataListBox... dataListBoxes) {
        for (DataListBox dataListBox : dataListBoxes) {
            if (dataListBox != null) {
                dataListBox.setItems(items);
            }
        }

        bankAccountListBox.setSelectedByValue(accountingStrings.bankAccountCode());
        cashAccountListBox.setSelectedByValue(accountingStrings.cashAccountCode());
        dateListBox.setSelectedByValue(wfmStrings.date());
        narrationListBox.setSelectedByValue(wfmStrings.narration());
        referenceListBox.setSelectedByValue(wfmStrings.reference());
        accountListBox.setSelectedByValue(wfmStrings.accountCode());
        descriptionListBox.setSelectedByValue(wfmStrings.description());
        amountListBox.setSelectedByValue(wfmStrings.amount());
        nameListBox.setSelectedByValue(wfmStrings.name());
        departmentListBox.setSelectedByValue(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        taxCalculationTypeListBox.setSelectedByValue(accountingStrings.taxCalculationType());
        taxNameListBox.setSelectedByValue(wfmStrings.taxRate());
        projectListBox.setSelectedByValue(wfmStrings.projectCode());

    }

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateListBoxRequired(dateListBox, new HTML(), "")) {
            errors++;
        }
        if ((AccountingConstants.CASH_RECEIPT_STR.equals(viewType) || AccountingConstants.CASH_PAYMENT_STR.equals(viewType)) && !Validation.validateListBoxRequired(cashAccountListBox, new HTML(), "")) {
            errors++;
        }
        if ((AccountingConstants.SPEND_MONEY_STR.equals(viewType) || AccountingConstants.RECEIVE_MONEY_STR.equals(viewType)) && !Validation.validateListBoxRequired(bankAccountListBox, new HTML(), "")) {
            errors++;
        }
        if ((AccountingConstants.SPEND_MONEY_STR.equals(viewType) || AccountingConstants.RECEIVE_MONEY_STR.equals(viewType)) && !Validation.validateListBoxRequired(amountListBox, new HTML(), "")) {

            errors++;
        }
        if ((AccountingConstants.SPEND_MONEY_STR.equals(viewType) || AccountingConstants.RECEIVE_MONEY_STR.equals(viewType)) && !Validation.validateListBoxRequired(accountListBox, new HTML(), "")) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }
        return errors == 0;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        ManualTransactionImportItem item = new ManualTransactionImportItem();
        item.setBankAccountId(bankAccountListBox.getSelectedId());
        item.setCashAccountId(cashAccountListBox.getSelectedId());
        item.setDate(dateListBox.getSelectedId());
        item.setNarration(narrationListBox.getSelectedId());
        item.setReference(referenceListBox.getSelectedId());
        item.setAccountCode(accountListBox.getSelectedId());
        item.setDescription(descriptionListBox.getSelectedId());
        item.setAmount(amountListBox.getSelectedId());
        item.setName(nameListBox.getSelectedId());
        item.setDepartment(departmentListBox.getSelectedId());
        item.setTaxCalculationType(taxCalculationTypeListBox.getSelectedId());
        item.setTaxRate(taxNameListBox.getSelectedId());
        item.setProjectCode(projectListBox.getSelectedId());

        Integer selectedId = projectListBox.getSelectedId();

        ImportFile importFile = item.getImportFile();
        importFile.setFileID(objectID);
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(csvHeaderBox.getValue());
        importFile.setViewType(viewType);
        importFile.setType(ImportTypeEnum.BANK_TRANSFER_TRANSACTION);

        LoadingPanel.loading(true);
        ImportFileService.App.get().addImportToQueue(importFile, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                showFailureMessage();
            }

            @Override
            public void success(String result) {
                LoadingPanel.loading(false);
                if (result != null && !"".equals(result)) {
                    String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
                    showFailureMessage(errorMessage);
                } else {
                    showSuccessMessage();
                }
            }
        });

    }

    private void showSuccessMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(WfmMessages.App.get().itemsSuccessfullyImported(
                AccountingConstants.RECEIVE_MONEY_STR.equals(viewType) ? accountingStrings.receiveMoney() :
                AccountingConstants.SPEND_MONEY_STR.equals(viewType) ? wfmStrings.bankPayment() :
                        AccountingConstants.CASH_RECEIPT_STR.equals(viewType) ? wfmStrings.cashReceipt() :
                                AccountingConstants.CASH_PAYMENT_STR.equals(viewType) ? wfmStrings.cashPayment() : accountingStrings.transactions()));
        messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
        messageBox.open();
    }

    private void showFailureMessage(final String... message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(message != null && message.length > 0 ? message[0] : wfmStrings.sureEnteredAllData());
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> {
            if (message == null || message.length == 0) {
                closeTab();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_BANK_TRANSACTION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
