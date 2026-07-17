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
import com.edatasite.workforce.gwt.importfile.client.rpc.CustomInvoiceImportItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
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
 * Date: 1/3/14
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomInvoiceImportView extends View {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final String importNimbleView = "import_nimble_view_";

    private char defaultSeparator = ',';

    private final Integer objectID;

    private DataListBox invoiceNumber;
    private DataListBox invoiceType;
    private DataListBox invoiceDate;
    private DataListBox dueDate;
    private DataListBox customerName;
    private DataListBox projectName;
    private DataListBox parentProjectName;
    private DataListBox reference;
    private DataListBox customerStrAddress;
    private DataListBox customerCity;
    private DataListBox customerCountry;
    private DataListBox customerPostCode;
    private DataListBox customerVAT;
    private DataListBox productName;
    private DataListBox description;
    private DataListBox productQty;
    private DataListBox productPrice;
    private DataListBox productDiscount;
    private DataListBox productTax;
    private DataListBox account;

    private WfmForm.Field invoiceNumberField;
    private WfmForm.Field invoiceTypeField;
    private WfmForm.Field invoiceDateField;
    private WfmForm.Field dueDateField;
    private WfmForm.Field customerNameField;
    private WfmForm.Field projectNameField;
    private WfmForm.Field parentProjectNameField;
    private WfmForm.Field referenceField;
    private WfmForm.Field customerStrAddressField;
    private WfmForm.Field customerCityField;
    private WfmForm.Field customerCountryField;
    private WfmForm.Field customerPostCodeField;
    private WfmForm.Field customerVATField;
    private WfmForm.Field productNameField;
    private WfmForm.Field descriptionField;
    private WfmForm.Field productQtyField;
    private WfmForm.Field productPriceField;
    private WfmForm.Field productDiscountField;
    private WfmForm.Field productTaxField;
    private WfmForm.Field beneficiaryAccountField;

    private KpiCheckBox hasHeader;

    private WfmButton2 saveButton;

    public CustomInvoiceImportView(Integer objectID) {
        super("add", "Import Invoices");
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        initializeFields();

        WfmForm table = new WfmForm(new String[]{"7%", "100%", "25%"});
        table.setLabelSize("150px");

        invoiceNumberField = table.addField("Invoice Number", invoiceNumber, false);
        invoiceTypeField = table.addField("Invoice Type", invoiceType, false);
        invoiceDateField = table.addField("Invoice Date", invoiceDate, true);
        dueDateField = table.addField("Due Date", dueDate, true);
        customerNameField = table.addField("Customer Name", customerName, true);
        projectNameField = table.addField("Project Name", projectName, false);
        referenceField = table.addField("Reference", reference, false);
//        customerStrAddressField = table.addField("Street Address", customerStrAddress, true);
//        customerCityField = table.addField("Customer City", customerCity, true);
//        customerCountryField = table.addField("Customer Country", customerCountry, true);
//        customerPostCodeField = table.addField("Customer post code", customerPostCode, true);
//        customerVATField = table.addField("Customer VAT", customerVAT, true);
        productNameField = table.addField("Product Name", productName, true);
        descriptionField = table.addField("Description", description, true);
        productQtyField = table.addField("Quantity", productQty, true);
        productPriceField = table.addField("Price", productPrice, true);
//        productDiscountField = table.addField("Discount %", productDiscount, false);
//        productTaxField = table.addField("VAT rate %", productTax, false);
        beneficiaryAccountField = table.addField("Account", account, true);

        table.addField(wfmStrings.myCSVFileHasHeaders(), hasHeader, true);
        table.addButton(saveButton);

        AccountingService.App.get().getCSVColumns(objectID, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final HashMap<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    SelectItem[] listItems = null;
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        listItems = entry.getValue();
                        if (!key.equals(new String(new char[]{defaultSeparator}))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(listItems);
                    LoadingPanel.loading(false);
                });
            }
        });


        add(table);

        return null;
    }

    private void setItems(SelectItem[] items) {
        for (SelectItem item : items) {
            item.setName(item.getName().trim());
        }
        invoiceNumber.setItems(items, invoiceNumberField);
        invoiceType.setItems(items, invoiceTypeField);
        invoiceDate.setItems(items, invoiceDateField);
        dueDate.setItems(items, dueDateField);
        customerName.setItems(items, customerNameField);
        parentProjectName.setItems(items, parentProjectNameField);
        projectName.setItems(items, projectNameField);
        reference.setItems(items, referenceField);
//        customerStrAddress.setItems(items, customerStrAddressField);
//        customerCity.setItems(items, customerCityField);
//        customerCountry.setItems(items, customerCountryField);
//        customerPostCode.setItems(items, customerPostCodeField);
//        customerVAT.setItems(items, customerVATField);
        productName.setItems(items, productNameField);
        description.setItems(items, descriptionField);
        productQty.setItems(items, productQtyField);
        productPrice.setItems(items, productPriceField);
//        productDiscount.setItems(items, productDiscountField);
//        productTax.setItems(items, productTaxField);
        account.setItems(items, beneficiaryAccountField);
    }

    private void initializeFields() {
        invoiceNumber = new DataListBox();
        invoiceNumber.ensureDebugId(importNimbleView + "number");

        invoiceType = new DataListBox();
        invoiceType.ensureDebugId(importNimbleView + "type");

        invoiceDate = new DataListBox();
        invoiceDate.ensureDebugId(importNimbleView + "date");

        dueDate = new DataListBox();
        dueDate.ensureDebugId(importNimbleView + "duedate");

        customerName = new DataListBox();
        customerName.ensureDebugId(importNimbleView + "customername");

        projectName = new DataListBox();
        projectName.ensureDebugId(importNimbleView + "projectname");

        parentProjectName = new DataListBox();
        parentProjectName.ensureDebugId(importNimbleView + "parentprojectname");

        reference = new DataListBox();
        reference.ensureDebugId(importNimbleView + "reference");

        customerStrAddress = new DataListBox();
        customerStrAddress.ensureDebugId(importNimbleView + "customeraddress");

        customerCity = new DataListBox();
        customerCity.ensureDebugId(importNimbleView + "customercity");

        customerCountry = new DataListBox();
        customerCountry.ensureDebugId(importNimbleView + "customercountry");

        customerPostCode = new DataListBox();
        customerPostCode.ensureDebugId(importNimbleView + "customerpostcode");

        customerVAT = new DataListBox();
        customerVAT.ensureDebugId(importNimbleView + "customertax");

        productName = new DataListBox();
        productName.ensureDebugId(importNimbleView + "productname");

        description = new DataListBox();
        description.ensureDebugId(importNimbleView + "description");

        productQty = new DataListBox();
        productQty.ensureDebugId(importNimbleView + "productqty");

        productPrice = new DataListBox();
        productPrice.ensureDebugId(importNimbleView + "productprice");

        productDiscount = new DataListBox();
        productDiscount.ensureDebugId(importNimbleView + "productdiscount");

        productTax = new DataListBox();
        productTax.ensureDebugId(importNimbleView + "producttax");

        account = new DataListBox();
        account.ensureDebugId(importNimbleView + "productaccount");

        hasHeader = new KpiCheckBox("");
        hasHeader.setValue(true);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(event -> save());

        invoiceNumber.addStyleName(DEFAULT_WIDTH);
        invoiceType.addStyleName(DEFAULT_WIDTH);
        invoiceDate.addStyleName(DEFAULT_WIDTH);
        dueDate.addStyleName(DEFAULT_WIDTH);

        customerName.addStyleName(DEFAULT_WIDTH);
        projectName.addStyleName(DEFAULT_WIDTH);
        parentProjectName.addStyleName(DEFAULT_WIDTH);
        reference.addStyleName(DEFAULT_WIDTH);
        customerStrAddress.addStyleName(DEFAULT_WIDTH);
        customerCity.addStyleName(DEFAULT_WIDTH);
        customerCountry.addStyleName(DEFAULT_WIDTH);
        customerPostCode.addStyleName(DEFAULT_WIDTH);
        customerVAT.addStyleName(DEFAULT_WIDTH);

        productName.addStyleName(DEFAULT_WIDTH);
        description.addStyleName(DEFAULT_WIDTH);
        productQty.addStyleName(DEFAULT_WIDTH);
        productPrice.addStyleName(DEFAULT_WIDTH);
        productDiscount.addStyleName(DEFAULT_WIDTH);
        productTax.addStyleName(DEFAULT_WIDTH);
        account.addStyleName(DEFAULT_WIDTH);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        CustomInvoiceImportItem importItem = new CustomInvoiceImportItem();
        importItem.setObjectID(objectID);

        importItem.setInvoiceNumber(invoiceNumber.getSelectedId());
        importItem.setInvoiceType(invoiceType.getSelectedId());
        importItem.setInvoiceDate(invoiceDate.getSelectedId());
        importItem.setDueDate(dueDate.getSelectedId());

        importItem.setCustomerName(customerName.getSelectedId());
        importItem.setProjectName(projectName.getSelectedId());
        importItem.setParentProjectName(parentProjectName.getSelectedId());
        importItem.setReference(reference.getSelectedId());

        importItem.setProductName(productName.getSelectedId());
        importItem.setDescription(description.getSelectedId());
        importItem.setProductQty(productQty.getSelectedId());
        importItem.setProductPrice(productPrice.getSelectedId());
        importItem.setBeneficiaryAccount(account.getSelectedId());

        ImportFile importFile = importItem.getImportFile();
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader.getValue());
        importFile.setType(ImportTypeEnum.CUSTOM_INVOICE);

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
        /*if (!Validation.validateListBoxRequired(invoiceNumber, invoiceNumberField, wfmStrings.pleaseSelect())) {
            errors++;
        }*/
        if (!Validation.validateListBoxRequired(invoiceDate, invoiceDateField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(customerName, customerNameField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(productName, productNameField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(productQty, productQtyField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(productPrice, productPriceField, wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(account, beneficiaryAccountField, wfmStrings.pleaseSelect())) {
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
