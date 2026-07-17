package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.edatasite.workforce.gwt.importfile.client.rpc.NimbleImportItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/13/12
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportNimbleCommerceView extends View {

    
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final String importNimbleView = "import_nimble_view_";

    private char defaultSeparator = ',';

    private final Integer objectID;

    private DataListBox offerID;
    private DataListBox offerName;
    private DataListBox offerPrice;
    private DataListBox customerFirstName;
    private DataListBox customerLastName;
    private DataListBox customerEmail;
    private DataListBox customerPhone;
    private DataListBox orderNumber;
    private DataListBox transactionDate;
    private DataListBox transactionTime;
    private DataListBox quantity;
    private DataListBox merchantID;
    private DataListBox tax;

    private WfmForm.Field offerIDField;
    private WfmForm.Field offerNameField;
    private WfmForm.Field offerPriceField;
    private WfmForm.Field customerFirstNameField;
    private WfmForm.Field customerLastNameField;
    private WfmForm.Field customerEmailField;
    private WfmForm.Field customerPhoneField;
    private WfmForm.Field orderNumberField;
    private WfmForm.Field transactionDateField;
    private WfmForm.Field transactionTimeField;
    private WfmForm.Field quantityField;
    private WfmForm.Field merchantIDField;
//    private WfmForm.Field taxField;

    private KpiCheckBox hasHeader;

    private WfmButton2 saveButton;

    public ImportNimbleCommerceView(Integer objectID) {
        super("add", accountingStrings.importNimbleData());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        initializeFields();

        WfmForm table = new WfmForm(new String[]{"7%", "100%", "25%"});
        table.setLabelSize("150px");

        offerIDField = table.addField(accountingStrings.offerId(), offerID, true);
        offerNameField = table.addField(accountingStrings.offerName(), offerName, true);
        offerPriceField = table.addField(accountingStrings.offerPrice(), offerPrice, true);
        customerFirstNameField = table.addField(wfmStrings.firstName(), customerFirstName, true);
        customerLastNameField = table.addField(wfmStrings.lastName(), customerLastName, true);
        customerEmailField = table.addField(wfmStrings.email(), customerEmail, true);
        customerPhoneField = table.addField(wfmStrings.phone(), customerPhone, true);
        orderNumberField = table.addField(wfmStrings.orderNumber(), orderNumber, true);
        transactionDateField = table.addField(accountingStrings.transactionDate(), transactionDate, true);
        transactionTimeField = table.addField(wfmStrings.time(), transactionTime, true);
        quantityField = table.addField(wfmStrings.qty(), quantity, true);
        merchantIDField = table.addField(wfmStrings.merchantId(), merchantID, true);
        /*taxField = */
        table.addField(wfmStrings.tax(), tax, false);

        table.addField(wfmStrings.myCSVFileHasHeaders(), hasHeader, true);
        table.addButton(saveButton);

        AccountingService.App.get().getCSVColumns(objectID, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            public void failure(Throwable d) {
                LoadingPanel.loading(false);
            }

            public void success(final Map<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    SelectItem[] listItems = null;
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        listItems = entry.getValue();
                        if (!key.equals(String.valueOf(defaultSeparator))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(listItems, offerID, offerName, offerPrice,
                            customerFirstName, customerLastName, customerEmail, customerPhone,
                            orderNumber, transactionDate, transactionTime, quantity, merchantID, tax);
                    LoadingPanel.loading(false);
                });
            }
        });


        add(table);

        return null;
    }

    private void setItems(SelectItem[] items, final DataListBox... dataListBoxes) {
        for (DataListBox dataListBox : dataListBoxes) {
            if (dataListBox != null) {
                dataListBox.setItems(items);
            }
        }
    }

    private void initializeFields() {
        offerID = new DataListBox();
        offerID.ensureDebugId(importNimbleView + "offerid");

        offerName = new DataListBox();
        offerName.ensureDebugId(importNimbleView + "offername");

        offerPrice = new DataListBox();
        offerPrice.ensureDebugId(importNimbleView + "offerprice");

        customerFirstName = new DataListBox();
        customerFirstName.ensureDebugId(importNimbleView + "firstname");

        customerLastName = new DataListBox();
        customerLastName.ensureDebugId(importNimbleView + "lastname");

        customerEmail = new DataListBox();
        customerEmail.ensureDebugId(importNimbleView + "email");

        customerPhone = new DataListBox();
        customerPhone.ensureDebugId(importNimbleView + "phone");

        orderNumber = new DataListBox();
        orderNumber.ensureDebugId(importNimbleView + "number");

        transactionDate = new DataListBox();
        transactionDate.ensureDebugId(importNimbleView + "transactiondate");

        transactionTime = new DataListBox();
        transactionTime.ensureDebugId(importNimbleView + "transactiontime");

        quantity = new DataListBox();
        quantity.ensureDebugId(importNimbleView + "quantity");

        merchantID = new DataListBox();
        merchantID.ensureDebugId(importNimbleView + "merchantid");

        tax = new DataListBox();
        tax.ensureDebugId(importNimbleView + "merchantid");

        hasHeader = new KpiCheckBox("");
        hasHeader.setValue(true);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(event -> save());

        offerID.addStyleName(DEFAULT_WIDTH);
        offerName.addStyleName(DEFAULT_WIDTH);
        offerPrice.addStyleName(DEFAULT_WIDTH);
        customerFirstName.addStyleName(DEFAULT_WIDTH);
        customerLastName.addStyleName(DEFAULT_WIDTH);
        customerEmail.addStyleName(DEFAULT_WIDTH);
        customerPhone.addStyleName(DEFAULT_WIDTH);
        orderNumber.addStyleName(DEFAULT_WIDTH);
        transactionDate.addStyleName(DEFAULT_WIDTH);
        transactionTime.addStyleName(DEFAULT_WIDTH);
        quantity.addStyleName(DEFAULT_WIDTH);
        merchantID.addStyleName(DEFAULT_WIDTH);
        tax.addStyleName(DEFAULT_WIDTH);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        NimbleImportItem importItem = new NimbleImportItem();
        importItem.setObjectID(objectID);
        importItem.setOfferID(offerID.getSelectedId());
        importItem.setOfferNameID(offerName.getSelectedId());
        importItem.setOfferPriceID(offerPrice.getSelectedId());
        importItem.setFirstNameID(customerFirstName.getSelectedId());
        importItem.setLastNameID(customerLastName.getSelectedId());
        importItem.setEmailID(customerEmail.getSelectedId());
        importItem.setPhoneID(customerPhone.getSelectedId());
        importItem.setOrderNumberID(orderNumber.getSelectedId());
        importItem.setTransDateID(transactionDate.getSelectedId());
        importItem.setTransTimeID(transactionTime.getSelectedId());
        importItem.setQuantityID(quantity.getSelectedId());
        importItem.setMerchantID(merchantID.getSelectedId());
        importItem.setTaxID(tax.getSelectedId());

        ImportFile importFile = importItem.getImportFile();
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader.getValue());
        importFile.setType(ImportTypeEnum.NIMBLE_COMMERCE);

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

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateListBoxRequired(offerID, offerIDField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(offerName, offerNameField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(offerPrice, offerPriceField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(customerFirstName, customerFirstNameField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(customerLastName, customerLastNameField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(customerEmail, customerEmailField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(customerPhone, customerPhoneField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(orderNumber, orderNumberField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(transactionDate, transactionDateField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(transactionTime, transactionTimeField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(quantity, quantityField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(merchantID, merchantIDField, wfmStrings.pleaseSelect())) {
            errors++;
        }

        if (errors > 0) {
            WfmWindow.alert(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void showSuccessMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(accountingStrings.importNimbleData());
        messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
        messageBox.open();
    }

    private void showFailureMessage(final String... message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(message != null && message.length > 0 ? message[0] : wfmStrings.error());
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> {
            if (message == null || message.length == 0) {
                closeTab();
            }
        });
    }

    @Override
    public String getIconStyle() {
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
