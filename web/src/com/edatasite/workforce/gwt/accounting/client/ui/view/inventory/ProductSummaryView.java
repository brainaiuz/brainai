package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.PriceLevelLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CurrencyLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPictureForm;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.*;
import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.*;

/**
 * Created by Dilshod on 4/28/2016.
 */
public class ProductSummaryView extends CustomForm2 implements Constants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();


    boolean isAlmadarSerials = Utils.hasGenericAccess(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);

    private NewProduct product;
    private final Integer objectID;
    private DynamicTable priceLevelTable;
    public BarcodeGenerator barcodeGenerator;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public ProductSummaryView(Integer objectID) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.product()));
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ProductServiceView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                ProductSummaryView.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                ProductSummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        productService.getProductEditData(objectID, false, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;
                initialize();
                getPriceLevelsByProduct();
            }
        });
    }

    private void getPriceLevelsByProduct() {
        priceLevelTable.removeItems();
        if (objectID != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setProductId(objectID);
            PriceLevelService.App.get().getPriceLevelPPItems(objectID, new AsyncCallback<HashMap<PriceLevelItem, PriceLevelPPItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(HashMap<PriceLevelItem, PriceLevelPPItem> items) {
                    if (items != null && !items.isEmpty()) {
                        for (Map.Entry<PriceLevelItem, PriceLevelPPItem> entry : items.entrySet()) {
                            priceLevelTable.addRow(getPriceLevelWidgets(entry.getKey(), entry.getValue()));
                        }
                    } else {
                        priceLevelTable.addRow(getPriceLevelWidgets(new PriceLevelItem(), new PriceLevelPPItem()));
                    }
                }
            });
        } else {
            priceLevelTable.addRow(getPriceLevelWidgets(new PriceLevelItem(), new PriceLevelPPItem()));
        }
    }

    private Widget[] getPriceLevelWidgets(final PriceLevelItem priceLevelItem, final PriceLevelPPItem ppItem) {
        int index = 0;

        final TextBox txtCustomPrice = new TextBox();
        txtCustomPrice.setWidth("130px");
        txtCustomPrice.setTextAlignment(TextBox.ALIGN_RIGHT);

        final Widget[] widgets = new Widget[priceLevelTable.getCellCount(0) - 1];

        PriceLevelLookUp pricelevelLookUp = new PriceLevelLookUp();
        widgets[index++] = pricelevelLookUp;

        final HTML currencyHTML = new HTML();
        currencyHTML.setHTML(getCurrencySymbolFromPriceLevel(priceLevelItem));
        pricelevelLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            PriceLevelItem selectedItem = pricelevelLookUp.getCollection().get(pricelevelLookUp.getSelectedItemID());
            currencyHTML.setHTML(getCurrencySymbolFromPriceLevel(selectedItem));
        });
        if (priceLevelItem.getId() != null) {
            pricelevelLookUp.setSelected(new SelectItem(priceLevelItem.getId(), priceLevelItem.getName()));
            pricelevelLookUp.setEnabled(false);
        }

        if (ppItem.getCustomPrice() != null) {
            txtCustomPrice.setValue(AccountingUtils.get().formatUnitPrice(ppItem.getCustomPrice()));
        } else {
            txtCustomPrice.setValue(AccountingUtils.getUnitPriceZero());
        }
        HorizontalPanel pnlWrap = new HorizontalPanel();
        pnlWrap.add(txtCustomPrice);
        pnlWrap.add(currencyHTML);
        pnlWrap.setCellVerticalAlignment(currencyHTML, HasVerticalAlignment.ALIGN_MIDDLE);
        widgets[index++] = pnlWrap;

        return widgets;
    }

    private String getCurrencySymbolFromPriceLevel(PriceLevelItem selectedItem) {
        String newCurrencySymbolOrCode = "";
        if (selectedItem != null) {
            if (selectedItem.getCurrency() != null) {
                if (selectedItem.getCurrency().getFrname() != null) {
                    newCurrencySymbolOrCode = selectedItem.getCurrency().getFrname();
                } else {
                    newCurrencySymbolOrCode = selectedItem.getCurrency().getName();
                }
            }
        }
        if ("".equalsIgnoreCase(newCurrencySymbolOrCode)) {
            newCurrencySymbolOrCode = AccountingUtils.getBaseCurrencyCode();
        }
        return newCurrencySymbolOrCode;
    }

    private DynamicTableColumn[] getPriceLevelTableColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[2];
        columns[0] = new DynamicTableColumn(wfmStrings.priceLevel(), "price_level", 200);
        columns[1] = new DynamicTableColumn(wfmStrings.customPrice(), "custom_price", 150);
        return columns;
    }

    private void initialize() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED)) {
            drawProductSerialTable();
        }

        if (AccountingUtils.get().isEnableBatchTrackingItems() && product.getTrackBatchesEnabled()) {
            drawProductBatchTrackingTable();
        }
        drawMainSection();
        if (INVENTORY_ITEM.equals(product.getType())) {
            drawFinanceSection();
            drawMultiCurrencyPriceSection();
            drawMoreOptionsSection();
            drawInventoryWarehouseTable();
        } else if (ASSEMBLY_ITEM.equals(product.getType())) {
            drawFinanceSection();
            drawMultiCurrencyPriceSection();
            drawMoreOptionsSection();
            drawAssemblyItemTable();
            drawInventoryWarehouseTable();
        } else if (NON_INVENTORY_ITEM.equals(product.getType())) {
            drawFinanceSection();
            drawMoreOptionsSection();
            drawMultiCurrencyPriceSection();
        } else if (SERVICE.equals(product.getType())) {
            drawFinanceSection();
            drawMoreOptionsSection();
            drawMultiCurrencyPriceSection();
        } else if (OTHER_CHARGE.equals(product.getType())) {
            drawFinanceSection();
            drawMultiCurrencyPriceSection();
        } else if (PRODUCT_KIT.equals(product.getType())) {
            drawProductGroupItemTable();
            drawMultiCurrencyPriceSection();
        }
        priceLevelTable = new DynamicTable(getPriceLevelTableColumns());
        priceLevelTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                priceLevelTable.insertRow(rowId + 1, getPriceLevelWidgets(new PriceLevelItem(), new PriceLevelPPItem()));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        priceLevelTable.addRow(getPriceLevelWidgets(new PriceLevelItem(), new PriceLevelPPItem()));

        drawParametersSection();
        drawProductAttachments();
        getCustomFieldUtil().drawCustomFields(this, product.getObjectId(), true);
        getCustomFieldUtil().fillCustomFieldsWithData(product.getProductCustomFieldItems(), true);
        drawProductCategoryCustomFieldsTable();
    }

    private void drawMainSection() {
        HTML productType = new HTML();
        HTML productName = new HTML();
        HTML productNumber = new HTML();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setReadOnly(true);
        HTML activeProductCheckBox = new HTML();
        HTML sentToTextileFindsCheckBox = new HTML();
        HTML statusField = new HTML();
        HTML rentItemField = new HTML();

//        KpiSwitcher activeProductCheckBox = new KpiSwitcher();
//        activeProductCheckBox.setOffLabel(wfmStrings.active());
//        activeProductCheckBox.setEnabled(false);

        productType.setText(PRODUCT_TYPES[product.getType() - 1].getName());
        productName.setText(product.getItemName());
        productName.getElement().setId("product_name");
        productNumber.setText(product.getNumberData().getNumberString());
        productNumber.getElement().setId("product_number");
        descriptionArea.setValue(product.getDescription());
        if (product.getRentStatus() != null) {
            statusField.setText(product.getRentStatus().getName());
        }

        if (product.getRentItem() != null) {
            rentItemField.setText(product.getRentItem().getName());
        } else {
            rentItemField.setText(wfmStrings.notAvailable());
        }
        if (product.isActive()) {
            activeProductCheckBox.setText(wfmStrings.yes());
        } else {
            activeProductCheckBox.setText(wfmStrings.no());
        }
        if (product.isSentToTextileFinds()) {
            sentToTextileFindsCheckBox.setText(wfmStrings.yes());
        } else {
            sentToTextileFindsCheckBox.setText(wfmStrings.no());
        }
//        activeProductCheckBox.setValue(product.isActive());

        addTitleField(CustomFormConstants.TITLE, property.getSingular(accountingStrings.productInformation(), wfmStrings.product()));
        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null) {
            addField(TYPE, productType, getTitle(formPropertyMap.get(TYPE).isChanged() ? formPropertyMap.get(TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(TYPE, productType, wfmStrings.type());
        }

        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, productName, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(NAME, productName, wfmStrings.name());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, productNumber, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, productNumber, property.getShortForNumber(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null) {
            addField(DESCRIPTION, descriptionArea, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(DESCRIPTION, descriptionArea, wfmStrings.description());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACTIVE) != null) {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.ACTIVE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACTIVE).getTitle() : wfmStrings.active()));
        } else {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, wfmStrings.active());
        }

        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            if (formPropertyMap != null && formPropertyMap.get(SENT_TO_TEXTILE_FINDS) != null) {
                addField(SENT_TO_TEXTILE_FINDS, sentToTextileFindsCheckBox, getTitle(formPropertyMap.get(SENT_TO_TEXTILE_FINDS).isChanged() ? formPropertyMap.get(SENT_TO_TEXTILE_FINDS).getTitle() : wfmStrings.active()));
            } else {
                addField(SENT_TO_TEXTILE_FINDS, sentToTextileFindsCheckBox, wfmStrings.sentToTextileFinds());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(STATUS, statusField, getTitle(formPropertyMap.get(STATUS).isChanged() ? formPropertyMap.get(STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(STATUS, statusField, wfmStrings.status());
        }

        if (formPropertyMap != null && formPropertyMap.get(AccountingConstants.RENT_ITEM) != null) {
            addField(AccountingConstants.RENT_ITEM, rentItemField, getTitle(formPropertyMap.get(AccountingConstants.RENT_ITEM).isChanged() ? formPropertyMap.get(AccountingConstants.RENT_ITEM).getTitle() : accountingStrings.rentalItem()));
        } else {
            addField(AccountingConstants.RENT_ITEM, rentItemField, accountingStrings.rentalItem());
        }
    }

    private void drawFinanceSection() {
//        TextBox purchasePriceTextBox = new TextBox();
        HTML purchasePriceTextBox = new HTML();
        HTML salesPriceTextBox = new HTML();
        HTML purchaseAccountTextBox = new HTML();
        HTML salesAccountTextBox = new HTML();
        HTML enableITCheckBox = new HTML();
        HTML purchasedFromSupplierCheckBox = new HTML();
        HTML soldToCustomerCheckBox = new HTML();
        HTML productDiscountType = new HTML();
        HTML productDiscountAmount = new HTML();

        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_COST : PermissionConstants.ACCOUNTING_PRODUCT_COST)) {
            if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null) {
                addField(PURCHASE_PRICE, purchasePriceTextBox, getTitle(formPropertyMap.get(PURCHASE_PRICE).isChanged() ? formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice()));
            } else {
                addField(PURCHASE_PRICE, purchasePriceTextBox, wfmStrings.purchasePrice());
            }

            if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null) {
                addField(PURCHASE_ACCOUNT, purchaseAccountTextBox, getTitle(formPropertyMap.get(PURCHASE_ACCOUNT).isChanged() ? formPropertyMap.get(PURCHASE_ACCOUNT).getTitle() : wfmStrings.purchaseAccount()));
            } else {
                addField(PURCHASE_ACCOUNT, purchaseAccountTextBox, wfmStrings.purchaseAccount());
            }

            if (formPropertyMap != null && formPropertyMap.get(FROM_PURCHASE_INVOICE) != null) {
                addField(FROM_PURCHASE_INVOICE, purchasedFromSupplierCheckBox, getTitle(formPropertyMap.get(FROM_PURCHASE_INVOICE).isChanged() ? formPropertyMap.get(FROM_PURCHASE_INVOICE).getTitle() : wfmStrings.purchasedFromSupplier()));
            } else {
                addField(FROM_PURCHASE_INVOICE, purchasedFromSupplierCheckBox, wfmStrings.purchasedFromSupplier());
            }

            if (formPropertyMap != null && formPropertyMap.get(SOLD_TO_CUSTOMERS) != null) {
                addField(SOLD_TO_CUSTOMERS, soldToCustomerCheckBox, getTitle(formPropertyMap.get(SOLD_TO_CUSTOMERS).isChanged() ? formPropertyMap.get(SOLD_TO_CUSTOMERS).getTitle() : accountingStrings.soldToCustomer()));
            } else {
                addField(SOLD_TO_CUSTOMERS, soldToCustomerCheckBox, accountingStrings.soldToCustomer());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_TYPE) != null) {
            addField(DISCOUNT_TYPE, productDiscountType, getTitle(formPropertyMap.get(DISCOUNT_TYPE).isChanged() ? formPropertyMap.get(DISCOUNT_TYPE).getTitle() : accountingStrings.maximumDiscount()));
        } else {
            addField(DISCOUNT_TYPE, productDiscountType, accountingStrings.maximumDiscount());
        }

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_AMOUNT) != null) {
            addField(DISCOUNT_AMOUNT, productDiscountAmount, getTitle(formPropertyMap.get(DISCOUNT_AMOUNT).isChanged() ? formPropertyMap.get(DISCOUNT_AMOUNT).getTitle() : accountingStrings.discountAmount()));
        } else {
            addField(DISCOUNT_AMOUNT, productDiscountAmount, accountingStrings.discountAmount());
        }

        if (Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_SELLING : PermissionConstants.ACCOUNTING_PRODUCT_SELLING)) {
            if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null) {
                addField(SALES_PRICE, salesPriceTextBox, getTitle(formPropertyMap.get(SALES_PRICE).isChanged() ? formPropertyMap.get(SALES_PRICE).getTitle() : wfmStrings.sellingPrice()));
            } else {
                addField(SALES_PRICE, salesPriceTextBox, wfmStrings.sellingPrice());
            }


            if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null) {
                addField(SALES_ACCOUNT, salesAccountTextBox, getTitle(formPropertyMap.get(SALES_ACCOUNT).isChanged() ? formPropertyMap.get(SALES_ACCOUNT).getTitle() : wfmStrings.salesAccount()));
            } else {
                addField(SALES_ACCOUNT, salesAccountTextBox, wfmStrings.salesAccount());
            }
        }

        if (!product.enableCompanyIT()) {
            if (formPropertyMap != null && formPropertyMap.get(ENABLE_IT) != null) {
                addField(ENABLE_IT, enableITCheckBox, getTitle(formPropertyMap.get(ENABLE_IT).isChanged() ? formPropertyMap.get(ENABLE_IT).getTitle() : accountingStrings.enableInventoryTransaction()));
            } else {
                addField(ENABLE_IT, enableITCheckBox, accountingStrings.enableInventoryTransaction());
            }
        }

//        purchasePriceTextBox.setValue(product.getUnitPrice() != null ? AccountingUtils.get().formatUnitPrice(product.getUnitPrice()) : "");
        purchasePriceTextBox.setText(product.getUnitPrice() != null ? AccountingUtils.get().formatUnitPrice(product.getUnitPrice()) : wfmStrings.notAvailable());
//        salesPriceTextBox.setValue(product.getSellingPrice() != null ? AccountingUtils.get().formatUnitPrice(product.getSellingPrice()) : "");
        salesPriceTextBox.setText(product.getSellingPrice() != null ? AccountingUtils.get().formatUnitPrice(product.getSellingPrice()) : wfmStrings.notAvailable());
        salesPriceTextBox.getElement().setId("product_sales_price");
        //        purchaseAccountTextBox.setValue(product.getCogsAccount() != null ? product.getCogsAccount().getName() : "");
        purchaseAccountTextBox.setText(product.getCogsAccount() != null ? product.getCogsAccount().getName() : wfmStrings.notAvailable());
//        salesAccountTextBox.setValue(product.getAccountItem() != null ? product.getAccountItem().getName() : "");
        salesAccountTextBox.setText(product.getAccountItem() != null ? product.getAccountItem().getName() : wfmStrings.notAvailable());
        enableITCheckBox.setText(product.enableIT() ? wfmStrings.yes() : wfmStrings.no());
        purchasedFromSupplierCheckBox.setText(product.isPurchasedFromSupplier() ? wfmStrings.yes() : wfmStrings.no());
        soldToCustomerCheckBox.setText(product.isSoldToCustomer() ? wfmStrings.yes() : wfmStrings.no());
        String discountType = "";
        if (product.getDiscountType() != null) {
            discountType = PRODUCT_DISCOUNT_TYPES[product.getDiscountType() - 1].getName();
        }
        productDiscountType.setText(discountType);
        productDiscountAmount.setText(String.valueOf(product.getDiscountAmount()));
    }

    private DynamicTableColumn[] getSerailNumberColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[isAlmadarSerials ? 5 : 4];
        columns[0] = new DynamicTableColumn(accountingStrings.serialNumbers(), "serialNumber", 90);
        columns[1] = new DynamicTableColumn(accountingStrings.lotNumbers(), "lotNumber", 90);
        columns[2] = new DynamicTableColumn(accountingStrings.refNumbers(), "refNumber", 90);
        columns[3] = new DynamicTableColumn(wfmStrings.expiryDate(), "expirationDate", 90);
        if (isAlmadarSerials) {
            columns[4] = new DynamicTableColumn(wfmStrings.qty(), "quantity", 40);
        }
        return columns;
    }

    private Widget[] getSerialNumbersWidget(ProductSerialItem item) {
        final Widget[] widgets = new Widget[isAlmadarSerials ? 5 : 4];
        HTML serialNumber = new HTML(item.getSerial() != null ? item.getSerial() : "");
        widgets[0] = serialNumber;
        HTML lotNumber = new HTML(item.getLotNumber() != null ? item.getLotNumber() : "");
        widgets[1] = lotNumber;
        HTML refNumber = new HTML(item.getRefNumber() != null ? item.getRefNumber() : "");
        widgets[2] = refNumber;
//        HTML expirationDate1 = new HTML(item.getExpirationDate() != null ? DateUtils.format(item.getExpirationDate(), DateUtils.dateFormatWithSlash) : "");
        HTML expirationDate = new HTML(item.getExpirationDate() != null ? DateUtils.format(item.getExpirationDate(), DateUtils.format) : "");
        widgets[3] = expirationDate;
        if (isAlmadarSerials) {
            HTML quantity = new HTML(item.getQty() != null ? item.getQty().toString() : "");
            widgets[4] = quantity;
        }
        return widgets;
    }

    private void drawProductSerialTable() {

        DynamicTable serialNumberInTable = new DynamicTable(getSerailNumberColumns(), AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_HEADER, AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_BORDER, false);
        DynamicTable serialNumberOutTable = new DynamicTable(getSerailNumberColumns(), AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_HEADER, AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_BORDER, false);
        serialNumberInTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);
        serialNumberOutTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);
        serialNumberInTable.setBorderWidth(0);
        serialNumberOutTable.setBorderWidth(0);

        //set data
        if (product.getProductSerialItems() != null && !product.getProductSerialItems().isEmpty()) {
            for (ProductSerialItem serialItem : product.getProductSerialItems()) {
                if (serialItem.getInvoiceID() == null) {
                    serialNumberInTable.addRow(getSerialNumbersWidget(serialItem));
                } else {
                    serialNumberOutTable.addRow(getSerialNumbersWidget(serialItem));
                }
            }
        } else {
            serialNumberInTable.addRow(getSerialNumbersWidget(new ProductSerialItem()));
            serialNumberOutTable.addRow(getSerialNumbersWidget(new ProductSerialItem()));
        }
        addTitleField("SERIAL", wfmStrings.serialNumberTracking());
        addField("SERIAL_ITEM_LEFT", new FormGroup(wfmStrings.serialNumberIn(), serialNumberInTable), null);
        addField("SERIAL_ITEM_RIGHT", new FormGroup(wfmStrings.serialNumberOut(), serialNumberOutTable), null);
    }

    private void drawProductBatchTrackingTable() {

        DynamicTable batchTrackTable = new DynamicTable(getBatchNumberColumns(),
                AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_HEADER,
                AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_BORDER, false);
        batchTrackTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);
        batchTrackTable.setBorderWidth(0);

        //set data
        if (product.getTrackBatchItems() != null && !product.getTrackBatchItems().isEmpty()) {
            for (ProductTrackBatchItem batchItem : product.getTrackBatchItems()) {
                batchTrackTable.addRow(getBatchNumbersWidget(batchItem));
            }
        } else {
            batchTrackTable.addRow(getBatchNumbersWidget(new ProductTrackBatchItem()));
        }
        addTitleField("BATCH_TRACK", "Track Batch");
        addField("BATCH_ITEMS", new FormGroup("Batch Details", batchTrackTable), null);
    }

    private DynamicTableColumn[] getBatchNumberColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[4];
        columns[0] = new DynamicTableColumn(accountingStrings.serialNumbers(), "serialNumber", 90);
        columns[1] = new DynamicTableColumn(wfmStrings.expiryDate(), "expirationDate", 90);
        columns[2] = new DynamicTableColumn(accountingStrings.onHand(), "onHand", 40);
        columns[3] = new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", 60);

        return columns;
    }

    private Widget[] getBatchNumbersWidget(ProductTrackBatchItem item) {
        final Widget[] widgets = new Widget[4];
        HTML serialNumber = new HTML(item.getSerial() != null ? item.getSerial() : "");
        widgets[0] = serialNumber;
        HTML expirationDate = new HTML(item.getExpirationDate() != null ? DateUtils.format(item.getExpirationDate(), DateUtils.format) : "N/A");
        widgets[1] = expirationDate;
        HTML quantity = new HTML(item.getBalanceInbatch() != null ? item.getBalanceInbatch().toString() : "");
        widgets[2] = quantity;
        HTML warehouse = new HTML(item.getWarehouseId() != null ? item.getWarehouseName() : "");
        widgets[3] = warehouse;
        return widgets;
    }

    private void drawParametersSection() {
        HTML categoryField = new HTML();
        HTML brandField = new HTML();
        HTML discountField = new HTML();
        HTML taxField = new HTML();
        HTML doubleTaxField = new HTML();

        categoryField.setText(product.getCategoryName());
        categoryField.getElement().setId("product_category");
        brandField.setText(product.getBrandName());
        taxField.setText(product.getTaxItem() != null ? product.getTaxItem().getName() : "");
        doubleTaxField.setText(product.getDoubleTaxItem() != null ? product.getDoubleTaxItem().getName() : "");

        if (product.getDiscountItems() != null && product.getDiscountItems().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (DiscountItem item : product.getDiscountItems()) {
                sb.append(item.getName());
                sb.append("<br>");
            }
            discountField.setText(sb.toString());
        }

        addTitleField(PARAMETRS, wfmStrings.parameters());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null) {
            addField(CATEGORY, categoryField, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category()));
        } else {
            addField(CATEGORY, categoryField, wfmStrings.category());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BRAND) != null) {
            addField(BRAND, brandField, getTitle(formPropertyMap.get(BRAND).isChanged() ? formPropertyMap.get(BRAND).getTitle() : wfmStrings.brand()));
        } else {
            addField(BRAND, brandField, wfmStrings.brand());
        }

        if (formPropertyMap != null && formPropertyMap.get(INPUT_PRICE_LEVEL) != null) {
            addField(INPUT_PRICE_LEVEL, priceLevelTable, getTitle(formPropertyMap.get(INPUT_PRICE_LEVEL).isChanged() ? formPropertyMap.get(INPUT_PRICE_LEVEL).getTitle() : wfmStrings.priceLevel()));
        } else {
            addField(INPUT_PRICE_LEVEL, priceLevelTable, wfmStrings.priceLevel());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DISCOUNT_PANEL) != null) {
            addField(DISCOUNT_PANEL, discountField, getTitle(formPropertyMap.get(DISCOUNT_PANEL).isChanged() ? formPropertyMap.get(DISCOUNT_PANEL).getTitle() : accountingStrings.discounts()));
        } else {
            addField(DISCOUNT_PANEL, discountField, accountingStrings.discounts());
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null) {
            addField(TAX, taxField, getTitle(formPropertyMap.get(TAX).isChanged() ? formPropertyMap.get(TAX).getTitle() : wfmStrings.taxRate()));
        } else {
            addField(TAX, taxField, wfmStrings.taxRate());
        }

        if (product.isDoubleTaxEnabled()) {
            FormGroup doubleTax = new FormGroup(wfmStrings.taxRate() + 2, doubleTaxField);
            addField(TAX + 2, doubleTax);
        }
    }

    private void drawMoreOptionsSection() {

//        TextBox skuNumberTextBox = new TextBox();
//        TextBox upcNumberTextBox = new TextBox();
//        TextBox unitMeasurementTextBox = new TextBox();
//        TextBox vendorTextBox = new TextBox();
//        TextBox manufacturerTextBox = new TextBox();
//        TextBox partNumberTextBox = new TextBox();
//        KpiSwitcher showOnOpportunityCheckBox = new KpiSwitcher();
//        showOnOpportunityCheckBox.setOffLabel(inventoryStrings.showOnOpportunity());
        barcodeGenerator = new BarcodeGenerator(BARCODE);

        HTML skuNumberTextBox = new HTML();
        HTML upcNumberTextBox = new HTML();
        HTML unitMeasurementTextBox = new HTML();
        HTML manufacturerTextBox = new HTML();
        HTML partNumberTextBox = new HTML();
//        HTML showOnOpportunityCheckBox = new HTML();

//        skuNumberTextBox.setEnabled(false);
//        upcNumberTextBox.setEnabled(false);
//        vendorTextBox.setEnabled(false);
//        manufacturerTextBox.setEnabled(false);
//        unitMeasurementTextBox.setEnabled(false);
//        partNumberTextBox.setEnabled(false);
//        showOnOpportunityCheckBox.setEnabled(false);


        barcodeGenerator.setData(product.getBarCodeText(), product.getQRCodeSizeID());

        skuNumberTextBox.setText(product.getInternalSKUNumber() != null ? product.getInternalSKUNumber() : wfmStrings.notAvailable());
        manufacturerTextBox.setText(product.getManufacturer() != null ? product.getManufacturer() : wfmStrings.notAvailable());
        partNumberTextBox.setText(product.getPartNumber() != null ? product.getPartNumber() : wfmStrings.notAvailable());
        upcNumberTextBox.setText(product.getUpcNumber() != null ? product.getUpcNumber() : wfmStrings.notAvailable());
        unitMeasurementTextBox.setText(product.getUnitMeasurement() != null ? product.getUnitMeasurement().getName() : wfmStrings.notAvailable());

        VerticalPanel pnlSuppliers = new VerticalPanel();
        pnlSuppliers.setSpacing(5);
        pnlSuppliers.add(new HTML("N/A"));

        if (product.getSuppliers() != null && product.getSuppliers().length > 0) {
            pnlSuppliers.clear();
            for (final SelectItem supplier : product.getSuppliers()) {
                if (Utils.hasPermission(PermissionConstants.CLIENT_NAME_CLICKABLE)) {
                    MaterialLink clientLink = new MaterialLink(supplier.getName());
                    clientLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + supplier.getId(), supplier.getName()));
                    pnlSuppliers.add(clientLink);
                } else {
                    pnlSuppliers.add(new HTML(supplier.getName()));
                }
            }
        }

        addTitleField(MORE_OPTIONS, wfmStrings.moreOptions());

        if (formPropertyMap != null && formPropertyMap.get(SKU_NUMBER) != null) {
            addField(SKU_NUMBER, skuNumberTextBox, getTitle(formPropertyMap.get(SKU_NUMBER).isChanged() ? formPropertyMap.get(SKU_NUMBER).getTitle() : wfmStrings.skuNumber()));
        } else {
            addField(SKU_NUMBER, skuNumberTextBox, wfmStrings.skuNumber());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.UPC_NUMBER) != null) {
            addField(CustomFormConstants.UPC_NUMBER, upcNumberTextBox, getTitle(formPropertyMap.get(CustomFormConstants.UPC_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.UPC_NUMBER).getTitle() : wfmStrings.upcNumber()));
        } else {
            addField(CustomFormConstants.UPC_NUMBER, upcNumberTextBox, wfmStrings.upcNumber());
        }

        if (formPropertyMap != null && formPropertyMap.get(UNIT_MEASUREMENT) != null) {
            addField(UNIT_MEASUREMENT, unitMeasurementTextBox, getTitle(formPropertyMap.get(UNIT_MEASUREMENT).isChanged() ? formPropertyMap.get(UNIT_MEASUREMENT).getTitle() : wfmStrings.unitMeasurement()));
        } else {
            addField(UNIT_MEASUREMENT, unitMeasurementTextBox, wfmStrings.unitMeasurement());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANUFACTURER) != null) {
            addField(CustomFormConstants.MANUFACTURER, manufacturerTextBox, getTitle(formPropertyMap.get(CustomFormConstants.MANUFACTURER).isChanged() ? formPropertyMap.get(CustomFormConstants.MANUFACTURER).getTitle() : wfmStrings.manufacturer()));
        } else {
            addField(CustomFormConstants.MANUFACTURER, manufacturerTextBox, wfmStrings.manufacturer());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PART_NUMBER) != null) {
            addField(CustomFormConstants.PART_NUMBER, partNumberTextBox, getTitle(formPropertyMap.get(CustomFormConstants.PART_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.PART_NUMBER).getTitle() : wfmStrings.partNumber()));
        } else {
            addField(CustomFormConstants.PART_NUMBER, partNumberTextBox, wfmStrings.partNumber());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIERS) != null) {
            addField(CustomFormConstants.SUPPLIERS, pnlSuppliers, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIERS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIERS).getTitle() : Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers())));
        } else {
            addField(CustomFormConstants.SUPPLIERS, pnlSuppliers, Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null) {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(false), getTitle(formPropertyMap.get(CustomFormConstants.BARCODE).isChanged() ? formPropertyMap.get(CustomFormConstants.BARCODE).getTitle() : wfmStrings.barcode()));
        } else {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(false), wfmStrings.barcode());
        }
//        addField(MORE_DETAILS, moreFieldsPanel, null);
    }

    private void drawMultiCurrencyPriceSection() {
        FlexPanel multiPricePanel = new FlexPanel();

        MultiTableNewUI tblMultiSalesPrice = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsForMultiPrice(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, true);

        MultiTableNewUI tblMultiCostPrice = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsForMultiPrice(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, true);

        tblMultiSalesPrice.removeAllRows();
        tblMultiCostPrice.removeAllRows();
        if (Utils.isMultipleSalesPriceEnable() && product.getMultiPrices() != null && !product.getMultiPrices().isEmpty()) {
            for (MultiPriceItem multiPriceItem : product.getMultiPrices()) {
                if (RECEIVABLE.equals(multiPriceItem.getType())) {
                    tblMultiSalesPrice.addWidgets(getWidgetsForMultiPrice(multiPriceItem));
                } else if (PAYABLE.equals(multiPriceItem.getType())) {
                    tblMultiCostPrice.addWidgets(getWidgetsForMultiPrice(multiPriceItem));
                }
            }
        } else {
            tblMultiSalesPrice.addWidgets(getWidgetsForMultiPrice(null));
            tblMultiCostPrice.addWidgets(getWidgetsForMultiPrice(null));
        }

        GColumn multiSalesPricePanel = new GColumn(GColumnEnum.COL_6);
        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE") != null) {
            multiSalesPricePanel.add(new FormGroup(formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").isChanged() ? formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").getTitle() : accountingStrings.multiCurrencySalesPriceSettings(), tblMultiSalesPrice));
        } else {
            multiSalesPricePanel.add(new FormGroup(accountingStrings.multiCurrencySalesPriceSettings(), tblMultiSalesPrice));
        }

        GColumn multiCostPricePanel = new GColumn(GColumnEnum.COL_6);
        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_COST_PRICE") != null) {
            multiSalesPricePanel.add(new FormGroup(formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").isChanged() ? formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").getTitle() : accountingStrings.multiCurrencyCostPriceSettings(), tblMultiCostPrice));
        } else {
            multiCostPricePanel.add(new FormGroup(accountingStrings.multiCurrencyCostPriceSettings(), tblMultiCostPrice));
        }

        multiPricePanel.add(multiSalesPricePanel);
        if (product.isPurchasedFromSupplier() ||
                INVENTORY_ITEM.equals(product.getType()) ||
                ASSEMBLY_ITEM.equals(product.getType()) ||
                NON_INVENTORY_ITEM.equals(product.getType()) ||
                OTHER_CHARGE.equals(product.getType()) ||
                SERVICE.equals(product.getType()) ||
                PRODUCT_KIT.equals(product.getType())) {
            multiPricePanel.add(multiCostPricePanel);
        }

        if (Utils.isMultipleSalesPriceEnable() && Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_SELLING : PermissionConstants.ACCOUNTING_PRODUCT_SELLING)) {
            addTitleField(MULTI_CURRENCY_PRICE_SETTINGS, wfmStrings.multiCurrencyPriceSettings());
            addField(MULTI_TABLE_PANEL, multiPricePanel, null);
        }
    }

    private WidgetsMap getWidgetsForMultiPrice(MultiPriceItem multiPriceItem) {
        WidgetsMap widgetsMap = new WidgetsMap();
        CurrencyLookUp currencyLookUp = new CurrencyLookUp(true);
        currencyLookUp.setEnabled(false);

        TextBox price = new TextBox();
        price.setText(AccountingUtils.getUnitPriceZero());
        price.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.checkToFocusTextBox(price, AccountingUtils.getUnitPriceZero());
        price.setReadOnly(true);
        new KpiToolTip(price, wfmStrings.price());

        if (multiPriceItem != null) {
            if (multiPriceItem.getCurrency() != null) {
                currencyLookUp.addItem(multiPriceItem.getCurrency());
            }
            if (multiPriceItem.getPrice() != null) {
                price.setText(AccountingUtils.get().formatPrice(multiPriceItem.getPrice()));
            }
        }

        widgetsMap.addToLeft(CURRENCY_COLUMN, currencyLookUp);
        widgetsMap.addToCenter(UNIT_PRICE_COLUMN, price);

        return widgetsMap;
    }

    private void drawInventoryWarehouseTable() {
        KpiRadioButton none = new KpiRadioButton("rb", " None");
        KpiRadioButton trackSerialnumber = new KpiRadioButton("rb", "Track Serial Number");
        KpiRadioButton batchSerialnumber = new KpiRadioButton("rb", "Track Batch Serials");
        KpiRadioButton trackBatches = new KpiRadioButton("rb", wfmStrings.trackBatches());

        if (product.getInventoryTrackingEnabled()) {
            trackSerialnumber.setValue(product.getInventoryTrackingEnabled());
            trackSerialnumber.setEnabled(false);
        } else if (product.getBatchTrackingEnabled()) {
            batchSerialnumber.setValue(product.getBatchTrackingEnabled());
            batchSerialnumber.setEnabled(false);
        } else if (product.getTrackBatchesEnabled()) {
            trackBatches.setEnabled(false);
            trackBatches.setValue(true);
        } else {
            none.setValue(true);
        }
        none.setEnabled(false);

        ProductWarehouseView productWarehouseView = new ProductWarehouseView(objectID, false);
        productWarehouseView.setWarehouseData(product);
        productWarehouseView.enableWarehouseWidgetsForEditForm(false, product);
        addTitleField(INVENTORY_STOCK_INFORMATION, wfmStrings.stockInformation());
        addField(WAREHOUSE_PANEL, productWarehouseView, null);

        if (Utils.isInventoryTrackingEnable() || Utils.isBatchSerialEnable() || AccountingUtils.get().isEnableBatchTrackingItems()) {
            Div div = ProductWarehouseView.generateBatchTrackView(none, trackSerialnumber, batchSerialnumber, trackBatches);
            if (formPropertyMap != null && formPropertyMap.get(SERIAL_NUMBER) != null) {
                addField(SERIAL_NUMBER, div, getTitle(formPropertyMap.get(SERIAL_NUMBER).isChanged() ? formPropertyMap.get(SERIAL_NUMBER).getTitle() : wfmStrings.serialNumber()));
            } else {
                addField(SERIAL_NUMBER, div, wfmStrings.serialNumber());
            }
        }
    }

    private void drawAssemblyItemTable() {
        ProductAssemblyView productAssemblyView = new ProductAssemblyView(objectID);
        productAssemblyView.setAssemblyItems(product);
        addTitleField(CustomFormConstants.BILL_OF_MATERIALS, wfmStrings.billOfMaterials());
        addField(ASSEMBLY_ITEMS, productAssemblyView, null);
    }

    private void drawProductGroupItemTable() {
        ProductKitView productKitView = new ProductKitView(objectID, true);
        productKitView.setProductKitData(product);
        addTitleField(PRODUCT_GROUP_ITEMS_INFORMATION, property.getSingular(wfmStrings.productKitItemInfo(), wfmStrings.product()));
        addField(PRODUCT_GROUP, productKitView, null);
    }

    private void drawProductAttachments() {
        GeneralFileUpload fileUploadPanel = new GeneralFileUpload(F_PRODUCTS_SERVICES, objectID, objectID);
        ProductPictureForm fileUploadForm = new ProductPictureForm(objectID);

        addTitleField(ATTACHMENTS, wfmStrings.attachments());
        addField(ATTACHMENTS, fileUploadPanel, null);
        addField(IMAGE_UPLOAD, fileUploadForm, null);

    }

    private void drawProductCategoryCustomFieldsTable() {
        if (product.getCategoryCustomFieldItems() != null && !product.getCategoryCustomFieldItems().isEmpty()) {
            FlexPanel categoryCustomFieldsPanel = new FlexPanel();

            GColumn column = new GColumn(GColumnEnum.COL_12);
            column.add(new InvoiceCustomFieldsSummaryView(product.getCategoryCustomFieldItems()).getCustomsDataView());
            categoryCustomFieldsPanel.add(column);

            addTitleField(PRODUCT_CATEGORY_CUSTOM_FIELDS, property.getSingular(wfmStrings.productCategoryCustomFields(), wfmStrings.product()));
            addField(INPUT_CUSTOM_FIELDS, categoryCustomFieldsPanel, null);
        }
    }

    @Override
    protected void addButtons() {

        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_EDIT : ACCOUNTING_PRODUCT_EDIT)) {
            WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), BTN_PRIMARY);
            editButton.addClickHandler(clickEvent -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("product|productadd/" + product.getObjectId(), product.getNumberData().getNumberString(), product.getItemName());
            });
            addButton(editButton);
        }

    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PRODUCT;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
    }

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

    @Override
    public String getPropertyCode() {
        return Constants.PRODUCTS_OR_SERVICES;
    }
}
