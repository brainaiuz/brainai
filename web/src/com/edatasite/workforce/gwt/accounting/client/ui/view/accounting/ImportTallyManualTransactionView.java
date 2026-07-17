package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.ManualTransactionImportItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
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
public class ImportTallyManualTransactionView extends CustomForm2 {

    private final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private KpiCheckBox csvHeaderBox;
    private DataListBox dateListBox;
    private DataListBox particularsListBox;
    private DataListBox exchangeRateListBox;
    private DataListBox voucherNumberListBox;
    private DataListBox debitListBox;
    private DataListBox creditListBox;

    private final String manualTransaction = "manualTransaction";
    private final Integer objectID;

    public ImportTallyManualTransactionView(Integer objectID) {
        super("add", "Import Manual Entry");
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

        csvHeaderBox = new KpiCheckBox();
        csvHeaderBox.setValue(Boolean.TRUE);
        csvHeaderBox.setText(wfmStrings.myCSVFileHasHeaders());
        csvHeaderBox.ensureDebugId(manualTransaction + "csvHeaderBox");

        dateListBox = new DataListBox();
        dateListBox.addStyleName(DEFAULT_WIDTH);
        dateListBox.ensureDebugId(manualTransaction + "dateListBox");

        particularsListBox = new DataListBox();
        particularsListBox.addStyleName(DEFAULT_WIDTH);
        particularsListBox.ensureDebugId(manualTransaction + "particularsListBox");

        exchangeRateListBox = new DataListBox();
        exchangeRateListBox.addStyleName(DEFAULT_WIDTH);
        exchangeRateListBox.ensureDebugId(manualTransaction + "exchangeRateListBox");

        voucherNumberListBox = new DataListBox();
        voucherNumberListBox.addStyleName(DEFAULT_WIDTH);
        voucherNumberListBox.ensureDebugId(manualTransaction + "voucherNumberListBox");

        debitListBox = new DataListBox();
        debitListBox.addStyleName(DEFAULT_WIDTH);
        debitListBox.ensureDebugId(manualTransaction + "debit");

        creditListBox = new DataListBox();
        creditListBox.addStyleName(DEFAULT_WIDTH);
        creditListBox.ensureDebugId(manualTransaction + "credit");


        addTitleField(ManualTransactionImport.REQUIRED_INFORMATIONS, getTitle(requiredInformation));
        addTitleField(ManualTransactionImport.OPTIONAL_INFORMATIONS, getTitle(optionalInformation));
        addField(ManualTransactionImport.HAS_CSV_HEADER, csvHeaderBox, "Has Header");
        addField(ManualTransactionImport.DATE, dateListBox, getTitle(wfmStrings.date(), true));
        addField(ManualTransactionImport.PARTICULARS, particularsListBox, getTitle("Particulars", true));
        addField(ManualTransactionImport.EXCHANGE_RATE, exchangeRateListBox, getTitle(wfmStrings.exchangeRate()));
        addField(ManualTransactionImport.VOUCHER_NUMBER, voucherNumberListBox, getTitle("Vch No."), true);
        addField(ManualTransactionImport.DEBIT, debitListBox, getTitle(wfmStrings.debit(), true));
        addField(ManualTransactionImport.CREDIT, creditListBox, getTitle(wfmStrings.credit(), true));

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null && !fieldID.isEmpty()) {
            if (ManualTransactionImport.DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (ManualTransactionImport.PARTICULARS.equals(fieldID)) {
                return "Particulars";
            } else if (ManualTransactionImport.EXCHANGE_RATE.equals(fieldID)) {
                return wfmStrings.exchangeRate();
            } else if (ManualTransactionImport.VOUCHER_NUMBER.equals(fieldID)) {
                return "Vch No.";
            } else if (ManualTransactionImport.DEBIT.equals(fieldID)) {
                return wfmStrings.debit();
            } else if (ManualTransactionImport.CREDIT.equals(fieldID)) {
                return wfmStrings.credit();
            } else if (ManualTransactionImport.HAS_CSV_HEADER.equals(fieldID)) {
                return wfmStrings.myCSVFileHasHeaders();
            } else if (ManualTransactionImport.REQUIRED_INFORMATIONS.equals(fieldID)) {
                return requiredInformation;
            } else if (ManualTransactionImport.OPTIONAL_INFORMATIONS.equals(fieldID)) {
                return optionalInformation;
            }
        }

        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.saveAndClose(), clickEvent -> save());
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

                    setItems(items, dateListBox, particularsListBox, exchangeRateListBox, voucherNumberListBox, debitListBox, creditListBox);
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

        dateListBox.setSelectedByValue(wfmStrings.date());
        particularsListBox.setSelectedByValue("Particulars");
        exchangeRateListBox.setSelectedByValue(wfmStrings.exchangeRate());
        voucherNumberListBox.setSelectedByValue("Vch No.");
        debitListBox.setSelectedByValue(wfmStrings.debit());
        creditListBox.setSelectedByValue(wfmStrings.credit());
    }

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateListBoxRequired(dateListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(particularsListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(exchangeRateListBox, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(voucherNumberListBox, new HTML(), "")) {
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
        item.setDate(dateListBox.getSelectedId());
        item.setParticulars(particularsListBox.getSelectedId());
        item.setExchangeRate(exchangeRateListBox.getSelectedId());
        item.setVoucherNumber(voucherNumberListBox.getSelectedId());
        item.setDebit(debitListBox.getSelectedId());
        item.setCredit(creditListBox.getSelectedId());

        boolean hasHeader_ = csvHeaderBox.getValue();
        ImportFile importFile = item.getImportFile();
        importFile.setFileID(objectID);
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader_);
        importFile.setType(ImportTypeEnum.MANUAL_TRANSACTION_TALLY);

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
                    Info.show("Manual Entries are importing...", Info.Type.INFO);
                    closeTab();
                }
            }
        });

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_MANUAL_TRANSACTION_FORM_TALLY;
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
