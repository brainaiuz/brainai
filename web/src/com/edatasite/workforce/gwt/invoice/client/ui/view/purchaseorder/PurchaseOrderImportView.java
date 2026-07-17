package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.CustomPurchaseOrderImportItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_PURCHASE_ORDERS_FORM;

public class PurchaseOrderImportView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    private DataListBox poNumber;
    private DataListBox poDate;
    private DataListBox poValidDate;
    private DataListBox supplierNumber;
    private DataListBox currency;
    private DataListBox exchangeRate;
    private DataListBox itemNumber;
    private DataListBox quantity;
    private DataListBox price;
    private DataListBox taxRate;
    private DataListBox account;


    public PurchaseOrderImportView(Integer objectId) {
        super("importpurchaseordersadd", "Import Purchase Orders");
        this.objectId = objectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        String importPCView = "import_purchase_orders_view_";
        poNumber = new DataListBox();
        poNumber.ensureDebugId(importPCView + "poNumber");
        poNumber.addStyleName(DEFAULT_WIDTH);

        poDate = new DataListBox();
        poDate.ensureDebugId(importPCView + "poDate");
        poDate.addStyleName(DEFAULT_WIDTH);

        poValidDate = new DataListBox();
        poValidDate.ensureDebugId(importPCView + "poValidDate");
        poValidDate.addStyleName(DEFAULT_WIDTH);

        supplierNumber = new DataListBox();
        supplierNumber.ensureDebugId(importPCView + "supplierNumber");
        supplierNumber.addStyleName(DEFAULT_WIDTH);

        currency = new DataListBox();
        currency.ensureDebugId(importPCView + "currency");
        currency.addStyleName(DEFAULT_WIDTH);

        exchangeRate = new DataListBox();
        exchangeRate.ensureDebugId(importPCView + "exchangeRate");
        exchangeRate.addStyleName(DEFAULT_WIDTH);

        itemNumber = new DataListBox();
        itemNumber.ensureDebugId(importPCView + "itemNumber");
        itemNumber.addStyleName(DEFAULT_WIDTH);

        quantity = new DataListBox();
        quantity.ensureDebugId(importPCView + "quantity");
        quantity.addStyleName(DEFAULT_WIDTH);

        price = new DataListBox();
        price.ensureDebugId(importPCView + "price");
        price.addStyleName(DEFAULT_WIDTH);

        taxRate = new DataListBox();
        taxRate.ensureDebugId(importPCView + "taxRate");
        taxRate.addStyleName(DEFAULT_WIDTH);

        account = new DataListBox();
        account.ensureDebugId(importPCView + "account");
        account.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.PO_NUMBERS, poNumber, getTitle(wfmStrings.poNumber(), true));
        addField(CustomFormConstants.DATE, poDate, getTitle(wfmStrings.date(), true));
        addField(CustomFormConstants.DUE_DATE, poValidDate, getTitle(wfmStrings.dueDate(), true));
        addField(CustomFormConstants.SUPPLIER_NUMBER, supplierNumber, getTitle(wfmStrings.supplier(), true));
        addField(CustomFormConstants.CURRENCY, currency, getTitle(wfmStrings.currency(), true));
        addField(CustomFormConstants.EXCHANGE_RATE, exchangeRate, getTitle(wfmStrings.exchangeRate(), true));
        addField(CustomFormConstants.ITEM_NUMBER, itemNumber, getTitle(wfmStrings.itemNumber(), false));
        addField(CustomFormConstants.QUANTITY, quantity, getTitle(wfmStrings.qty(), false));
        addField(CustomFormConstants.PRICE, price, getTitle(wfmStrings.price(), false));
        addField(CustomFormConstants.ITEM_TABLE_TAX_RATE, taxRate, getTitle(wfmStrings.taxRate(), false));
        addField(CustomFormConstants.ACCOUNT, account, getTitle(wfmStrings.account(), false));
        reInitialize();
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.PurchaseOrder;
    }


    @Override
    public void setItems(SelectItem[] items) {
        poNumber.setItems(items, wfmStrings.poNumber());
        poDate.setItems(items, wfmStrings.date());
        poValidDate.setItems(items, wfmStrings.dueDate());
        supplierNumber.setItems(items, wfmStrings.supplier());
        currency.setItems(items, wfmStrings.currency());
        exchangeRate.setItems(items, wfmStrings.exchangeRate());
        itemNumber.setItems(items, wfmStrings.itemNumber());
        quantity.setItems(items, wfmStrings.qty());
        price.setItems(items, wfmStrings.price());
        taxRate.setItems(items, wfmStrings.taxRate());
        account.setItems(items, wfmStrings.account());
        LoadingPanel.loading(false);
    }

    private ImportFile createColumns(CustomPurchaseOrderImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_PO_NUMBER, item.getPoNumber() != null ? item.getPoNumber(): -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_PO_DATE, item.getPoDate() != null ? item.getPoDate() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_PO_VALID_DATE, item.getPoValidDate() !=  null ? item.getPoValidDate() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_SUPPLIER_NUMBER, item.getSupplierNumber() != null ? item.getSupplierNumber() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_CURRENCY, item.getCurrency() != null ? item.getCurrency() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_EXCHANGE_RATE, item.getExchangeRate() != null ? item.getExchangeRate() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_ITEM_NUMBER, item.getItemNumber() != null ? item.getItemNumber() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_QTY, item.getQuantity() != null ? item.getQuantity() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_PRICE, item.getPrice() != null ? item.getPrice() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_TAX_RATE, item.getTaxRate() != null ? item.getTaxRate() : -1);
        importFile.addColumn(ImportField.PurchaseOrderImportFields.FIELD_ACCOUNT_CODE, item.getAccount() != null ? item.getAccount() : -1);

        return importFile;
    }

    private CustomPurchaseOrderImportItem getRPC() {
        CustomPurchaseOrderImportItem item = new CustomPurchaseOrderImportItem();
        item.setId(objectId);
        item.setPoNumber(getSelectedItem(poNumber));
        item.setPoDate(getSelectedItem(poDate));
        item.setPoValidDate(getSelectedItem(poValidDate));
        item.setSupplierNumber(getSelectedItem(supplierNumber));
        item.setCurrency(getSelectedItem(currency));
        item.setExchangeRate(getSelectedItem(exchangeRate));
        item.setItemNumber(getSelectedItem(itemNumber));
        item.setQuantity(getSelectedItem(quantity));
        item.setPrice(getSelectedItem(price));
        item.setTaxRate(getSelectedItem(taxRate));
        item.setAccount(getSelectedItem(account));

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
        return ImportTypeEnum.PURCHASE_ORDER;
    }

    @Override
    protected String getFormID() {
        return IMPORT_PURCHASE_ORDERS_FORM;
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

