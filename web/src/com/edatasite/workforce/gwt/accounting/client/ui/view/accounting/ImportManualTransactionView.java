package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
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
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
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

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by dilshod on 23-Mar-16.
 */
public class ImportManualTransactionView extends CustomForm2 {

    private final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private KpiSwitcher csvHeaderBox;
    private DataListBox numberListBox;
    private DataListBox dateListBox;
    private DataListBox narrationListBox;
    private DataListBox referenceListBox;
    private DataListBox accountListBox;
    private DataListBox debitListBox;
    private DataListBox creditListBox;
    private DataListBox descriptionListBox;
    private DataListBox departmentListBox;
    private DataListBox nameListBox;
    private DataListBox projectListBox;
    private DataListBox exchangeRateListBox;
    private DataListBox currencyListBox;

    private final String manualTransaction = "manualTransaction";
    private final Integer objectID;

    public ImportManualTransactionView(Integer objectID) {
        super("add", wfmStrings.importManualEntry());
        this.objectID = objectID;
    }

    private SelectItem[] items;
    private char defaultSeparator = ',';
    private final String requiredInformation = "Required Information";
    private final String optionalInformation = "Optional Information";

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }


    @Override
    protected void registerFields() {

        csvHeaderBox = new KpiSwitcher();
        csvHeaderBox.setValue(Boolean.TRUE);
        csvHeaderBox.ensureDebugId(manualTransaction + "csvHeaderBox");

        numberListBox = new DataListBox();
        numberListBox.addStyleName(DEFAULT_WIDTH);
        numberListBox.ensureDebugId(manualTransaction + "number");

        dateListBox = new DataListBox();
        dateListBox.addStyleName(DEFAULT_WIDTH);
        dateListBox.ensureDebugId(manualTransaction + "date");

        narrationListBox = new DataListBox();
        narrationListBox.addStyleName(DEFAULT_WIDTH);
        narrationListBox.ensureDebugId(manualTransaction + "narration");

        referenceListBox = new DataListBox();
        referenceListBox.addStyleName(DEFAULT_WIDTH);
        referenceListBox.ensureDebugId(manualTransaction + "reference");

        accountListBox = new DataListBox();
        accountListBox.addStyleName(DEFAULT_WIDTH);
        accountListBox.ensureDebugId(manualTransaction + "account");

        debitListBox = new DataListBox();
        debitListBox.addStyleName(DEFAULT_WIDTH);
        debitListBox.ensureDebugId(manualTransaction + "debit");

        creditListBox = new DataListBox();
        creditListBox.addStyleName(DEFAULT_WIDTH);
        creditListBox.ensureDebugId(manualTransaction + "credit");

        descriptionListBox = new DataListBox();
        descriptionListBox.addStyleName(DEFAULT_WIDTH);
        descriptionListBox.ensureDebugId(manualTransaction + "description");

        departmentListBox = new DataListBox();
        departmentListBox.addStyleName(DEFAULT_WIDTH);
        departmentListBox.ensureDebugId(manualTransaction + "department");

        nameListBox = new DataListBox();
        nameListBox.addStyleName(DEFAULT_WIDTH);
        nameListBox.ensureDebugId(manualTransaction + "name");

        projectListBox = new DataListBox();
        projectListBox.addStyleName(DEFAULT_WIDTH);
        projectListBox.ensureDebugId(manualTransaction + "project");

        exchangeRateListBox = new DataListBox();
        exchangeRateListBox.addStyleName(DEFAULT_WIDTH);
        exchangeRateListBox.ensureDebugId(manualTransaction + "exchangeRate");

        currencyListBox = new DataListBox();
        currencyListBox.addStyleName(DEFAULT_WIDTH);
        currencyListBox.ensureDebugId(manualTransaction + "currency");

        addTitleField(ManualTransactionImport.REQUIRED_INFORMATIONS, getTitle(requiredInformation));
        addTitleField(ManualTransactionImport.OPTIONAL_INFORMATIONS, getTitle(optionalInformation));
        addField(ManualTransactionImport.HAS_CSV_HEADER, csvHeaderBox, wfmStrings.myCSVFileHasHeaders());
        addField(ManualTransactionImport.NUMBER, numberListBox, getTitle(wfmStrings.number()));
        addField(ManualTransactionImport.DATE, dateListBox, getTitle(wfmStrings.date(), true));
        addField(ManualTransactionImport.NARRATION, narrationListBox, getTitle(wfmStrings.narration()));
        addField(ManualTransactionImport.REFERENCE, referenceListBox, getTitle(wfmStrings.reference()));
        addField(ManualTransactionImport.ACCOUNT_CODE, accountListBox, getTitle(wfmStrings.accountCode(), true));
        addField(ManualTransactionImport.DEBIT, debitListBox, getTitle(wfmStrings.debit(), true));
        addField(ManualTransactionImport.CREDIT, creditListBox, getTitle(wfmStrings.credit(), true));
        addField(ManualTransactionImport.DESCRIPTION, descriptionListBox, getTitle(wfmStrings.description()));
        addField(ManualTransactionImport.DEPARTMENT, departmentListBox, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        addField(ManualTransactionImport.NAME, nameListBox, getTitle(wfmStrings.name()));
        addField(ManualTransactionImport.PROJECT_CODE, projectListBox, getTitle(wfmStrings.project()));
        addField(ManualTransactionImport.EXCHANGE_RATE, exchangeRateListBox, getTitle(wfmStrings.exchangeRate()));
        addField(ManualTransactionImport.CURRENCY, currencyListBox, getTitle(wfmStrings.currency()));

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null && !fieldID.isEmpty()) {
            if (ManualTransactionImport.NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            } else if (ManualTransactionImport.DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (ManualTransactionImport.NARRATION.equals(fieldID)) {
                return wfmStrings.narration();
            }else if (ManualTransactionImport.REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (ManualTransactionImport.ACCOUNT_CODE.equals(fieldID)) {
                return wfmStrings.accountCode();
            } else if (ManualTransactionImport.DEBIT.equals(fieldID)) {
                return wfmStrings.debit();
            } else if (ManualTransactionImport.CREDIT.equals(fieldID)) {
                return wfmStrings.credit();
            } else if (ManualTransactionImport.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            }  else if (ManualTransactionImport.DEPARTMENT.equals(fieldID)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            } else if (ManualTransactionImport.HAS_CSV_HEADER.equals(fieldID)) {
                return wfmStrings.myCSVFileHasHeaders();
            } else if (ManualTransactionImport.REQUIRED_INFORMATIONS.equals(fieldID)) {
                return requiredInformation;
            } else if (ManualTransactionImport.OPTIONAL_INFORMATIONS.equals(fieldID)) {
                return optionalInformation;
            } else if (ManualTransactionImport.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (ManualTransactionImport.PROJECT_CODE.equals(fieldID)) {
                return wfmStrings.product();
            } else if (ManualTransactionImport.CURRENCY.equals(fieldID)) {
                return wfmStrings.currency();
            }else if (ManualTransactionImport.EXCHANGE_RATE.equals(fieldID)) {
                return wfmStrings.exchangeRate();
            }
        }

        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.saveAndClose(), "import_save_and_close", "import_save_and_close", clickEvent -> save());
//        addButton(accountingStrings.cancel(), "import_cancel_button", "import_cancel_button", (ClickHandler) clickEvent -> closeTab());
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

                    setItems(items, numberListBox, dateListBox, narrationListBox, referenceListBox, accountListBox, debitListBox, creditListBox,
                            descriptionListBox, departmentListBox, nameListBox, projectListBox, exchangeRateListBox, currencyListBox);
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

        numberListBox.setSelectedByTheBestValue(wfmStrings.number());
        dateListBox.setSelectedByTheBestValue(wfmStrings.date());
        narrationListBox.setSelectedByTheBestValue(wfmStrings.narration());
        referenceListBox.setSelectedByTheBestValue(wfmStrings.reference());
        accountListBox.setSelectedByTheBestValue(wfmStrings.accountCode());
        debitListBox.setSelectedByTheBestValue(wfmStrings.debit());
        creditListBox.setSelectedByTheBestValue(wfmStrings.credit());
        descriptionListBox.setSelectedByTheBestValue(wfmStrings.description());
        departmentListBox.setSelectedByTheBestValue(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        nameListBox.setSelectedByTheBestValue(wfmStrings.name());
        projectListBox.setSelectedByTheBestValue(wfmStrings.projectCode());
        exchangeRateListBox.setSelectedByTheBestValue(wfmStrings.exchangeRate());
        currencyListBox.setSelectedByTheBestValue(accountingStrings.foreignCurrency());
    }

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateListBoxRequired(dateListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(narrationListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(accountListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(debitListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(creditListBox, new HTML(), "")) {
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
        item.setNumber(numberListBox.getSelectedId());
        item.setDate(dateListBox.getSelectedId());
        item.setNarration(narrationListBox.getSelectedId());
        item.setReference(referenceListBox.getSelectedId());
        item.setAccountCode(accountListBox.getSelectedId());
        item.setDebit(debitListBox.getSelectedId());
        item.setCredit(creditListBox.getSelectedId());
        item.setDescription(descriptionListBox.getSelectedId());
        item.setDepartment(departmentListBox.getSelectedId());
        item.setName(nameListBox.getSelectedId());
        item.setProjectCode(projectListBox.getSelectedId());
        item.setExchangeRate(exchangeRateListBox.getSelectedId());
        item.setCurrency(currencyListBox.getSelectedId());

        boolean hasHeader_ = csvHeaderBox.getValue();
        ImportFile importFile = item.getImportFile();
        importFile.setFileID(objectID);
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader_);
        importFile.setType(ImportTypeEnum.MANUAL_TRANSACTION);

        LoadingPanel.loading(true);
        ImportFileService.App.get().addImportToQueue(importFile, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);

            }


            @Override
            public void success(String result) {
                LoadingPanel.loading(false);
                if (result != null && !"".equals(result)) {
                    String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
                    Info.show(errorMessage, Info.Type.WARNING);
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.setMessage(WfmMessages.App.get().itemsSuccessfullyImported(wfmStrings.manualTransaction()));
                    messageBox.open();
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, result, ImportManualTransactionView.this);
                }
            }
        });

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_MANUAL_TRANSACTION_FORM;
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
