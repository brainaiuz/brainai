package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.TaxQuickAddForm;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.ProductNumbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.RENTAL_ITEM;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_PRICE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_PRICE;

/**
 * Created by IntelliJ IDEA.
 * User: Iftixor
 * Date: 09.08.2021
 * Time: 12:10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class RentalProductlAddEditView extends CustomForm2 implements Colapse, Constants {

    public static final SelectItem[] RENTAL_UNITS = new SelectItem[]{
//            new SelectItem(1, wfmStrings.minutes(), Constants.TIME_GRANULARITY.MINUTES),
            new SelectItem(2, wfmStrings.hours(), Constants.TIME_GRANULARITY.HOURS),
            new SelectItem(3, wfmStrings.days(), Constants.TIME_GRANULARITY.DAYS),
            new SelectItem(4, wfmStrings.weeks(), TIME_GRANULARITY.WEEKS),
            new SelectItem(5, wfmStrings.months(), TIME_GRANULARITY.MONTHS)
    };


    private static final ProductServiceAsync productService = ProductService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    /**
     * Financial fields
     */
    public TextBox txtPurchasePrice;
    private ProductNameWidget productNameWidget;
    private ProductNumbering productNumberWidget;
    private KpiSwitcher activeProductCheckBox;
    private DataListBox dwCategory;
    private DataListBox dwBrand;
    private MultiTableNewUI multiSupplierTable;
    private BarcodeGenerator barcodeGenerator;
    private WfmButton2 btnSave;
    private TextBox txtSalesPrice;
    private AccountsLookUp salesAccountLookUp;
    private AccountsLookUp purchaseAccountLookUp;
    private SmartTaxRateLookUp taxLookUp;
    /**
     * Attachment fields
     */
    private ProfileImage fileUploadForm;
    private GeneralFileUpload fileUploadPanel;
    private final Integer objectID;
    //Number Data
    private NumberData numberData;
    private NewProduct product;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private EditableTable itemsTable;
    private ColumnConfig[] columnConfigs;
    private LinkedList<String> itemColumns;
    private TextBox extraHour;
    private TextBox extraDay;
    private TextBox securityTime;
    private DataListBox rentalType;
    private FormHasCustomField customFieldUtil;


    public RentalProductlAddEditView(Integer objectID) {
        super("productrentaladd");
        setDescription(objectID == null ? property.getSingular(accountingStrings.addProduct(), accountingStrings.rentalItem()) : property.getSingular(accountingStrings.editProduct(), accountingStrings.rentalItem()));
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.RentalProductsView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void onSuccess(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                RentalProductlAddEditView.super.onInitialize();
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
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void getDataToFillFields() {
        drawForm();
        LoadingPanel.loading(true);
        productService.getRentalProductEditData(objectID, false, new AbstractAsyncCallback<NewProduct>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;

                if (product != null) {

                    if (product.getIntNumber() != null) {
                        productNumberWidget.setNumberData(product.getNumberData());
                    } else if (product.getObjectId() != null) {
                        productNumberWidget.setNumberData(product.getNumberData());
//                        productNumberWidget.getTxtPrefix().setWidth("100px");
                        productNumberWidget.getTxtNumber().setVisible(false);
                    } else if (product.getObjectId() == null) {
                        numberData = product.getNumberData();
                        productNumberWidget.setNumberData(product.getNumberData());
                    }

                    if (product.getItemName() != null && !"".equals(product.getItemName().trim())) {
                        productNameWidget.setValue(product.getItemNameID(), product.getItemName());
                    }

                    barcodeGenerator.setData(product.getBarCodeText(), product.getQRCodeSizeID());
                    txtPurchasePrice.setValue(product.getUnitPrice() != null ? utils.formatUnitPrice(product.getUnitPrice()) : "");
                    txtSalesPrice.setValue(product.getSellingPrice() != null ? utils.formatUnitPrice(product.getSellingPrice()) : "");
                    activeProductCheckBox.setValue(product.isActive());
                    dwCategory.setItems(product.getProductCategories());
                    if (product.getCategoryID() != null) {
                        dwCategory.setSelected(product.getCategoryID());
                    }
                    dwBrand.setItems(product.getBrands());
                    if (product.getBrandID() != null) {
                        dwBrand.setSelected(product.getBrandID());
                    }
                    if (product.getTaxItem() != null) {
                        taxLookUp.setSelected(new SelectItem(product.getTaxItem().getId(), product.getTaxItem().getName()));
                    }
                    //accounting fill
                    if (product.getAccountId() != null) {
                        salesAccountLookUp.addAccountItem(product.getAccountItem());
                    } else if (product.getDefaultReceivableAccount() != null) {
                        salesAccountLookUp.addAccountItem(product.getDefaultReceivableAccount());
                    }

                    if (product.getSuppliers() != null && product.getSuppliers().length > 0) {
                        multiSupplierTable.removeAllRows();
                        for (SelectItem supplier : product.getSuppliers()) {
                            multiSupplierTable.addWidgets(getSupplierTableWidget(supplier));
                        }
                    }

                    if (product.getCogsAccountID() != null) {
                        purchaseAccountLookUp.addAccountItem(product.getCogsAccount());
                    } else if (product.getDefaultPayableAccount() != null) {
                        purchaseAccountLookUp.setSelected(product.getDefaultPayableAccount());
                    }
                    extraHour.setText(product.getExtraHour() != null ? utils.formatUnitPrice(product.getExtraHour()) : "");
                    extraDay.setText(product.getExtraDay() != null ? utils.formatUnitPrice(product.getExtraDay()) : "");
                    securityTime.setText(product.getSecurityTime() != null ? utils.formatUnitPrice(product.getSecurityTime()) : "");
                    getCustomFieldUtil().fillCustomFieldsWithData(product.getProductCustomFieldItems());

                    List<RentalProductItem> rentalProductItems = new ArrayList<>();
                    RentalProductItem hourItem = new RentalProductItem(Constants.TIME_GRANULARITY.HOURS);
                    RentalProductItem dayItem = new RentalProductItem(Constants.TIME_GRANULARITY.DAYS);
                    RentalProductItem weekItem = new RentalProductItem(Constants.TIME_GRANULARITY.WEEKS);
                    RentalProductItem monthItem = new RentalProductItem(Constants.TIME_GRANULARITY.MONTHS);
                    if (product.getRentalProductItems() != null && product.getRentalProductItems().size() > 0) {
                        for (RentalProductItem rentalProductItem : product.getRentalProductItems()) {
                            if (rentalProductItem != null) {
                                switch (rentalProductItem.getUnitCode()) {
                                    case Constants.TIME_GRANULARITY.HOURS:
                                        hourItem = rentalProductItem;
                                        break;
                                    case TIME_GRANULARITY.DAYS:
                                        dayItem = rentalProductItem;
                                        break;
                                    case TIME_GRANULARITY.WEEKS:
                                        weekItem = rentalProductItem;
                                        break;
                                    case TIME_GRANULARITY.MONTHS:
                                        monthItem = rentalProductItem;
                                        break;
                                }
                            }
                        }
                    }

                    rentalProductItems = new ArrayList<>();
                    rentalProductItems.add(hourItem);
                    rentalProductItems.add(dayItem);
                    rentalProductItems.add(weekItem);
                    rentalProductItems.add(monthItem);
                    for (int i = 0; i < rentalProductItems.size(); i++) {
                        itemsTable.addRow(getWidgets(rentalProductItems.get(i)));
                    }
                }

                CommonService.App.get().getRentalProductImageURL(objectID, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        fileUploadForm.initialize(s, result != null ? result.getItemName() : null, null, true);
                    }
                });

                if (objectID == null) {
                    setDefaultValues();
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    private void drawForm() {
        drawMainSection();
        drawRentalPricingSection();
        drawFinanceSection();
        drawAttachmentsSection();

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, false);
    }

    private void drawMainSection() {

        productNumberWidget = new ProductNumbering();
        productNameWidget = new ProductNameWidget();
        productNameWidget.ensureDebugId("productName");

        activeProductCheckBox = new KpiSwitcher();

        dwCategory = new DataListBox();
        dwCategory.ensureDebugId(PRODUCT + "dwCategory");
        boolean[] isFirstSelect = {true};



        dwBrand = new DataListBox();
        dwBrand.ensureDebugId("brand-listBox");

        MaterialLink lnkNewBrand = new MaterialLink();
        lnkNewBrand.setClass("ficon--plus");
        lnkNewBrand.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                new BrandViewPopup(new ExtendedCommand() {
                    public void execute(final Integer id) {
                        AccountingService.App.get().getBrandsAsSelectItem(new AbstractAsyncCallback<SelectItem[]>() {
                            public void success(SelectItem[] items) {
                                dwBrand.setItems(items);
                                dwBrand.setSelected(id);
                            }
                        });
                    }
                });
            }
        });
        AdvancedInputGroup brandPanel = new AdvancedInputGroup(dwBrand, lnkNewBrand);

        MaterialLink lnkNewCategory = new MaterialLink();
        lnkNewCategory.setClass("ficon--plus");
        lnkNewCategory.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                new ProductCategoryViewPopup(new ExtendedCommand() {
                    public void execute(final Integer id) {
                        AccountingService.App.get().getCategoriesAsTreeSelectItem(new AbstractAsyncCallback<TreeSelectItem[]>() {
                            public void failure(Throwable caught) {
                                GWT.log(caught.getMessage());
                            }

                            public void success(TreeSelectItem[] items) {
                                dwCategory.setItems(items);
                                dwCategory.setSelected(id);
                            }
                        });
                    }
                }, null, false);
            }
        });
        AdvancedInputGroup categoryPanel = new AdvancedInputGroup(dwCategory, lnkNewCategory);


        multiSupplierTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getSupplierTableWidget(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });
        multiSupplierTable.setOnLinesRemoved(() -> regenerateProductNumber());
        barcodeGenerator = new BarcodeGenerator(BARCODE);


        addTitleField(CustomFormConstants.PRODUCT_INFORMATION, property.getSingular(accountingStrings.productInformation(), accountingStrings.rentalItem()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(NAME, productNameWidget, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()));
            productNameWidget.setEnabled(!formPropertyMap.get(NAME).isDisabled());
        } else {
            addField(NAME, productNameWidget, wfmStrings.name());
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : property.getShortForNumber(wfmStrings.number()), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()));
            productNumberWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, productNumberWidget, property.getShortForNumber(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACTIVE) != null) {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.ACTIVE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACTIVE).getTitle() : wfmStrings.active(), formPropertyMap.get(CustomFormConstants.ACTIVE).isRequired()));
            activeProductCheckBox.setEnabled(!formPropertyMap.get(CustomFormConstants.ACTIVE).isDisabled());
        } else {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, wfmStrings.active());
        }
        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryPanel, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY).isRequired()));
            dwCategory.setEnabled(!formPropertyMap.get(CATEGORY).isDisabled());
        } else {
            addField(CATEGORY, categoryPanel, wfmStrings.category());
        }

        if (formPropertyMap != null && formPropertyMap.get(BRAND) != null) {
            addField(BRAND, brandPanel, getTitle(formPropertyMap.get(BRAND).isChanged() ? formPropertyMap.get(BRAND).getTitle() : wfmStrings.brand(), formPropertyMap.get(BRAND).isRequired()));
            dwBrand.setEnabled(!formPropertyMap.get(BRAND).isDisabled());
        } else {
            addField(BRAND, brandPanel, wfmStrings.brand());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIERS) != null) {
            addField(CustomFormConstants.SUPPLIERS, multiSupplierTable, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIERS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIERS).getTitle() : Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers()), formPropertyMap.get(CustomFormConstants.SUPPLIERS).isRequired()));
        } else {
            addField(CustomFormConstants.SUPPLIERS, multiSupplierTable, Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null) {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(), getTitle(formPropertyMap.get(CustomFormConstants.BARCODE).isChanged() ? formPropertyMap.get(CustomFormConstants.BARCODE).getTitle() : wfmStrings.barcode(), formPropertyMap.get(BARCODE).isRequired()));
            barcodeGenerator.setEnabled(!formPropertyMap.get(BARCODE).isDisabled());
        } else {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(), wfmStrings.barcode());
        }
    }

    private void drawFinanceSection() {
        txtPurchasePrice = new TextBox(true);
        txtPurchasePrice.ensureDebugId(PRODUCT + "txtPurchasePrice");
        Validation.checkToFocusTextBox(txtPurchasePrice, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(txtPurchasePrice, AccountingUtils.customUnitPriceScale);
        txtPurchasePrice.addChangeHandler(c -> priceChange(txtPurchasePrice));

        txtSalesPrice = new TextBox(true);
        txtSalesPrice.ensureDebugId(PRODUCT + "txtSalesPrice");
//        Validation.checkToFocusTextBox(txtSalesPrice, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(txtSalesPrice, AccountingUtils.customUnitPriceScale);
        txtSalesPrice.setMaxLength(10);
        txtSalesPrice.addChangeHandler(c -> {
            priceChange(txtSalesPrice);
        });

        txtPurchasePrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        txtSalesPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        salesAccountLookUp = new AccountsLookUp(REVENUE, LIABILITIES);
        salesAccountLookUp.setAutocompleteOff();
        salesAccountLookUp.ensureDebugId(PRODUCT + "salesAccountLookUp");

        purchaseAccountLookUp = new AccountsLookUp(EXPENSES);
        purchaseAccountLookUp.setAutocompleteOff();
        purchaseAccountLookUp.ensureDebugId(PRODUCT + "purchaseAccountLookUp");

        taxLookUp = new SmartTaxRateLookUp(RECEIVABLE, () -> new TaxQuickAddForm(o -> taxLookUp.addTaxItem((TaxItem) o)));
        taxLookUp.getSuggestBox().addSelectionHandler(s -> taxLookUp.islink());
        taxLookUp.setAutocompleteOff();
        taxLookUp.ensureDebugId(PRODUCT + "taxLookUp");

        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null) {
            addField(PURCHASE_PRICE, txtPurchasePrice, getTitle(formPropertyMap.get(PURCHASE_PRICE).isChanged() ? formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice(), formPropertyMap.get(PURCHASE_PRICE).isRequired()));
            txtPurchasePrice.setEnabled(!formPropertyMap.get(PURCHASE_PRICE).isDisabled());
        } else {
            addField(PURCHASE_PRICE, txtPurchasePrice, wfmStrings.purchasePrice());
        }
        if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null) {
            addField(SALES_PRICE, txtSalesPrice, getTitle(formPropertyMap.get(SALES_PRICE).isChanged() ? formPropertyMap.get(SALES_PRICE).getTitle() : wfmStrings.sellingPrice(), formPropertyMap.get(SALES_PRICE).isRequired()));
            txtSalesPrice.setEnabled(!formPropertyMap.get(SALES_PRICE).isDisabled());
        } else {
            addField(SALES_PRICE, txtSalesPrice, wfmStrings.sellingPrice());
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null) {
            addField(PURCHASE_ACCOUNT, purchaseAccountLookUp, getTitle(formPropertyMap.get(PURCHASE_ACCOUNT).isChanged() ? formPropertyMap.get(PURCHASE_ACCOUNT).getTitle() : wfmStrings.purchaseAccount(), formPropertyMap.get(PURCHASE_ACCOUNT).isRequired()));
            purchaseAccountLookUp.setEnabled(!formPropertyMap.get(PURCHASE_ACCOUNT).isDisabled());
        } else {
            addField(PURCHASE_ACCOUNT, purchaseAccountLookUp, wfmStrings.purchaseAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null) {
            addField(SALES_ACCOUNT, salesAccountLookUp, getTitle(formPropertyMap.get(SALES_ACCOUNT).isChanged() ? formPropertyMap.get(SALES_ACCOUNT).getTitle() : wfmStrings.salesAccount(), formPropertyMap.get(SALES_ACCOUNT).isRequired()));
            salesAccountLookUp.setEnabled(!formPropertyMap.get(SALES_ACCOUNT).isDisabled());
        } else {
            addField(SALES_ACCOUNT, salesAccountLookUp, wfmStrings.salesAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null) {
            addField(TAX, taxLookUp, getTitle(formPropertyMap.get(TAX).isChanged() ? formPropertyMap.get(TAX).getTitle() : wfmStrings.taxRate(), formPropertyMap.get(TAX).isRequired()));
            taxLookUp.setEnabled(!formPropertyMap.get(TAX).isDisabled());
        } else {
            addField(TAX, taxLookUp, wfmStrings.taxRate());
        }
    }

    private void drawRentalPricingSection() {
        columnConfigs = getColumns();
        itemsTable = new EditableTable(columnConfigs, false, false);
        itemsTable.setDraggable(false);
//        itemsTable.setListener(new EditableTableListener() {
//            @Override
//            public void addRow() {
//                itemsTable.addRow(getWidgets(new RentalProductItem()));
//            }
//
//            @Override
//            public void removeRow() {
//            }
//        });
        addField(CustomFormConstants.ITEMS, itemsTable, null, true);


        extraHour = new TextBox(true);
        extraHour.ensureDebugId(PRODUCT + "extraHour");
        extraHour.addChangeHandler(c -> {
            priceChange(extraHour);
        });
        Validation.checkToFocusTextBox(extraHour, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(extraHour, AccountingUtils.customUnitPriceScale);

        extraDay = new TextBox(true);
        extraDay.ensureDebugId(PRODUCT + "extraDay");
        extraDay.addChangeHandler(c -> {
            priceChange(extraDay);
        });
        Validation.checkToFocusTextBox(extraDay, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(extraDay, AccountingUtils.customUnitPriceScale);

        securityTime = new TextBox(true);
        securityTime.ensureDebugId(PRODUCT + "securityTime");
        securityTime.addChangeHandler(c -> {
            priceChange(securityTime);
        });
        Validation.checkToFocusTextBox(securityTime, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(securityTime, AccountingUtils.customUnitPriceScale);

        rentalType = new DataListBox();
        rentalType.setEnabled(false);
        SelectItem[] items = new SelectItem[1];
        items[0] = new SelectItem(0, "Consumable");
        rentalType.setItems(items);
        rentalType.setSelected(0);

        if (formPropertyMap != null && formPropertyMap.get(OVERTIME_HOURS) != null) {
            addField(OVERTIME_HOURS, extraHour, getTitle(formPropertyMap.get(OVERTIME_HOURS).isChanged() ? formPropertyMap.get(OVERTIME_HOURS).getTitle() : wfmStrings.extraHour(), formPropertyMap.get(OVERTIME_HOURS).isRequired()));
            extraHour.setEnabled(!formPropertyMap.get(OVERTIME_HOURS).isDisabled());
        } else {
            addField(OVERTIME_HOURS, extraHour, wfmStrings.extraHour());
        }
        if (formPropertyMap != null && formPropertyMap.get(OVERTIME_END_DATE) != null) {
            addField(OVERTIME_END_DATE, extraDay, getTitle(formPropertyMap.get(OVERTIME_END_DATE).isChanged() ? formPropertyMap.get(OVERTIME_END_DATE).getTitle() : wfmStrings.extraDay(), formPropertyMap.get(OVERTIME_END_DATE).isRequired()));
            extraDay.setEnabled(!formPropertyMap.get(OVERTIME_END_DATE).isDisabled());
        } else {
            addField(OVERTIME_END_DATE, extraDay, wfmStrings.extraDay());
        }

        if (formPropertyMap != null && formPropertyMap.get(SECURITY_TIME) != null) {
            addField(SECURITY_TIME, securityTime, getTitle(formPropertyMap.get(SECURITY_TIME).isChanged() ? formPropertyMap.get(SECURITY_TIME).getTitle() : wfmStrings.securityTime(), formPropertyMap.get(SECURITY_TIME).isRequired()));
            securityTime.setEnabled(!formPropertyMap.get(SECURITY_TIME).isDisabled());
        } else {
            addField(SECURITY_TIME, securityTime, wfmStrings.securityTime());
        }

        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null) {
            addField(TYPE, rentalType, getTitle(formPropertyMap.get(TYPE).isChanged() ? formPropertyMap.get(TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(TYPE).isRequired()));
//            rentalType.setEnabled(!formPropertyMap.get(TYPE).isDisabled());
        } else {
            addField(TYPE, rentalType, wfmStrings.type());
        }
    }

    private void regenerateProductNumber() {
        NumberData productCurrentNumber = productNumberWidget.getNumberData(false);
        productService.regenerateProductNumber(getSupplierIds(), dwCategory.getSelectedId(), productCurrentNumber, new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void success(NumberData result) {
                numberData = result;
                productNumberWidget.setNumberData(result);
            }
        });
    }

    private WidgetsMap getSupplierTableWidget(SelectItem client) {
        WidgetsMap widgetsMap = new WidgetsMap();

        CRMLookUp clientLookUp = new CRMLookUp(LookUpConstants.SUPPLIER_ID);
        if (client != null && client.getId() != null) {
            clientLookUp.addItem(client);
            clientLookUp.setSelected(client);
        }
        clientLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            regenerateProductNumber();
        });
        widgetsMap.addToCenter(SUPPLIER, clientLookUp);
        return widgetsMap;
    }

    private void drawAttachmentsSection() {
        fileUploadPanel = new GeneralFileUpload(F_PRODUCTS_SERVICES, objectID, true, objectID, null);
        fileUploadForm = new ProfileImage(objectID, getFormID());

        addField(IMAGE_UPLOAD, fileUploadForm, null);
        addField(ATTACHMENTS, fileUploadPanel, null);
    }

    @Override
    protected void addButtons() {

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(click -> {
            btnSave.setEnabled(false);
            if (validate()) {
                LoadingPanel.loading(true);
                product = getProductObject();
                validateProduct();
            } else {
                btnSave.setEnabled(true);
            }
        });
        addButton(btnSave);
    }

    private void validateProduct() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCaseID(product.getObjectId());
        fp.setName(productNameWidget.getItemName());
        fp.setCheckNumber(true);
        fp.setNumber(productNumberWidget.getNumberData(false).getNumberString());


        productService.validateProduct(fp, new AbstractAsyncCallback<boolean[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void success(boolean[] result) {
                if (!result[0] && !result[1] && !result[2] && !result[3]) {
                    save(product);
                } else {
                    LoadingPanel.loading(false);
                    if (result[0]) {
                        Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.rentalItem()), Info.Type.WARNING);
                    } else if (result[2]) {
                        Info.show(property.getSingular(accountingStrings.upcErrorMessage(), accountingStrings.rentalItem()), Info.Type.WARNING);
                    } else if (result[3]) {
                        warnAboutProductExistance();
                    }
                }
            }
        });
    }

    private void warnAboutProductExistance() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setMessage(wfmStrings.confirmation());
        messageBox.setMessage(property.getSingular(accountingStrings.alreadyProduct(), accountingStrings.rentalItem()));
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                save(product);
            }
        });
        messageBox.open();
        btnSave.setEnabled(true);
    }

    private void priceChange(TextBox textBox) {
        String uprice = (textBox.getValue() != null && !textBox.getValue().isEmpty()) ? utils.formatUnitPrice(new BigDecimal(textBox.getValue().replaceAll("[,]", ""))) : AccountingUtils.getUnitPriceZero();
        textBox.setText(uprice);
    }

    private NewProduct getProductObject() {
        product.setObjectId(objectID);
        product.setType(RENTAL_ITEM);
        NumberData numberData = productNumberWidget.getNumberData(false);

        product.setNumberData(numberData);
        product.setItemNameID(productNameWidget.getItemNameID());
        product.setItemName(productNameWidget.getItemName());
        if (dwCategory.isSomethingSelected()) {
            product.setCategoryID(dwCategory.getSelectedId());
        } else {
            product.setCategoryID(0);
        }

        product.setVatId(taxLookUp.getSelectedItemID());

        product.setBarCodeText(barcodeGenerator.getBarcodeText());
        product.setQRCodeSizeID(barcodeGenerator.getQRCodeSizeID());

        product.setAccountId(salesAccountLookUp.getSelectedItemID());
        product.setCogsAccountID(purchaseAccountLookUp.getSelectedItemID());

        product.setSellingPrice(AccountingUtils.get().parseToBigDecimal(txtSalesPrice.getText()));
        product.setUnitPrice(AccountingUtils.get().parseToBigDecimal(txtPurchasePrice.getText()));

        product.setBrandID(dwBrand.getSelectedItem() != null ? dwBrand.getSelectedItem().getId() : Integer.valueOf(0));
        product.setActive(activeProductCheckBox.getValue());
        product.setSuppliers(getSupplierIds());

        product.setImageId(fileUploadForm.getImageID());
        product.setAttachments(fileUploadPanel.getAttachedFiles());
        if (extraHour.getText() != null) {
            product.setExtraHour(AccountingUtils.get().parseToBigDecimal(extraHour.getText()));
        }
        if (extraDay.getText() != null) {
            product.setExtraDay(AccountingUtils.get().parseToBigDecimal(extraDay.getText()));
        }
        if (securityTime.getText() != null) {
            product.setSecurityTime(AccountingUtils.get().parseToBigDecimal(securityTime.getText()));
        }

        ArrayList<RentalProductItem> rentalProductItems = new ArrayList<>();
        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            TextArea2 description = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
            DataListBox units = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.UNITS);
            CustomCellTextBox price = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.UNITPRICE);

            if (units.getSelectedId() != null && price.getText() != null && AccountingUtils.get().parseToBigDecimal(price.getValue()).compareTo(BigDecimal.ZERO) > 0) {
                RentalProductItem rentalProductItem = new RentalProductItem();
                rentalProductItem.setDescription(description.getText());
                rentalProductItem.setPrice(AccountingUtils.get().parseToBigDecimal(price.getText()));
                rentalProductItem.setUnitCode(units.getSelectedItem().getDescription());

                rentalProductItems.add(rentalProductItem);
            }
        }
        product.setRentalProductItems(rentalProductItems);
        product.setProductCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        return product;
    }

    private SelectItem[] getSupplierIds() {
        ArrayList<SelectItem> suppliers = new ArrayList<>();
        for (WidgetsMap map : multiSupplierTable.getWidgetsMaps()) {
            CRMLookUp clientLookup = (CRMLookUp) map.getWidget(SUPPLIER);
            if (clientLookup != null && clientLookup.getSelectedItemID() != null) {
                suppliers.add(clientLookup.getSelectedItem());
            }
        }
        if (!suppliers.isEmpty()) {
            return suppliers.toArray(new SelectItem[]{});
        } else {
            return new SelectItem[0];
        }
    }

    private boolean validate() {
        int errors = 0;

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
                errors += markAsError(productNameWidget, !productNameWidget.validate());
                productNameWidget.removeStyleName(Constants.ERROR_FORM_STYLE);
            }
        } else {
            errors += markAsError(productNameWidget, !productNameWidget.validate());
            productNameWidget.removeStyleName(Constants.ERROR_FORM_STYLE);
        }

        errors += markAsError(productNumberWidget, !productNumberWidget.validate());

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null) {
            if (formPropertyMap.get(PURCHASE_PRICE).isRequired()) {
                errors += markAsError(txtPurchasePrice, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PURCHASE_PRICE).isChanged() ?
                        formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice(), txtPurchasePrice, formPropertyMap.get(PURCHASE_PRICE).getMinChar()));
            }
        } else {
            errors += markAsError(txtPurchasePrice, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PURCHASE_PRICE).isChanged() ?
                    formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice(), txtPurchasePrice, formPropertyMap.get(PURCHASE_PRICE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null) {
            if (formPropertyMap.get(PURCHASE_ACCOUNT).isRequired()) {
                errors += markAsError(purchaseAccountLookUp, purchaseAccountLookUp.getSelectedItemID() == null);
            }
        } else {
            errors += markAsError(purchaseAccountLookUp, purchaseAccountLookUp.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null) {
            if (formPropertyMap.get(SALES_PRICE).isRequired()) {
                errors += markAsError(txtSalesPrice, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(SALES_PRICE).isChanged() ?
                        formPropertyMap.get(SALES_PRICE).getTitle() : accountingStrings.salePrice(), txtSalesPrice, formPropertyMap.get(SALES_PRICE).getMinChar()));
            }
        } else {
            errors += markAsError(txtSalesPrice, Utils.isNullOrEmpty(txtSalesPrice.getText()));
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null) {
            if (formPropertyMap.get(SALES_ACCOUNT).isRequired()) {
                errors += markAsError(salesAccountLookUp, salesAccountLookUp.getSelectedItemID() == null);
            }
        } else {
            errors += markAsError(salesAccountLookUp, salesAccountLookUp.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            if (formPropertyMap.get(CATEGORY).isRequired()) {
                errors += markAsError(dwCategory, !Validation.validateListBoxRequired(dwCategory, new HTML(), accountingStrings.categoryIsRequired()));
            }
        } else {
            errors += markAsError(dwCategory, !Validation.validateListBoxRequired(dwCategory, new HTML(), accountingStrings.categoryIsRequired()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BRAND) != null && formPropertyMap.get(CustomFormConstants.BRAND).isRequired() && dwBrand != null) {
            errors += markAsError(dwBrand, !Validation.validateListBoxRequired(dwBrand));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null && formPropertyMap.get(TAX).isRequired() && taxLookUp != null) {
            errors += markAsError(taxLookUp, !Validation.validateLookUpRequired(taxLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(SUPPLIERS) != null && formPropertyMap.get(SUPPLIERS).isRequired() && multiSupplierTable != null && multiSupplierTable.getWidgets() != null) {
            if (multiSupplierTable.getWidgets() != null && multiSupplierTable.getWidgets().get(0).size() > 0) {
                CRMLookUp clientLookup = (CRMLookUp) multiSupplierTable.getWidgetsMaps().get(0).getWidget(SUPPLIER);
                if (clientLookup != null) {
                    errors += markAsError(multiSupplierTable, !Validation.validateLookUpRequired(clientLookup));
                }
            }
        }

        errors += fileUploadPanel.validated() ? 0 : 1;

        boolean foundPrice = false;
        for (int rowID = 0; rowID < itemsTable.getGrid().getRowCount(); rowID++) {
            itemsTable.resetValidation(rowID);
            CustomCellTextBox price = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
            DataListBox units = (DataListBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITS);

            if (units.getSelectedId() != null && price.getText() != null && AccountingUtils.get().parseToBigDecimal(price.getValue()).compareTo(BigDecimal.ZERO) > 0) {
                foundPrice = true;
                break;
            }
        }
        if (!foundPrice) {
            itemsTable.notValid(0, ItemTableConstants.UNITS);
            itemsTable.notValid(0, ItemTableConstants.UNITPRICE);
            errors++;
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
        }

        return errors == 0;
    }

    private void save(NewProduct product) {
        productService.saveProduct(product, new AbstractAsyncCallback<ProductSelectItem>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ProductSelectItem result) {
                LoadingPanel.loading(false);
                if (result != null && result.getId() == -1) {
                    productNumberWidget.addStyleName(Constants.ERROR_FORM_STYLE);
                    Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.rentalItem()), Info.Type.WARNING);
                    return;
                }
                closeTab(null);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RENTAL_PRODUCT_ADDED, result, RentalProductlAddEditView.this);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.RENTAL_PRODUCT_FORM;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
        return Constants.RENTAL_PRODUCTS;
    }

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue().length() > 0 && productNameWidget != null) {
            productNameWidget.setValue(null, formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null && formPropertyMap.get(PURCHASE_PRICE).getDefaultValue() != null && txtPurchasePrice != null && isNumeric(formPropertyMap.get(PURCHASE_PRICE).getDefaultValue())) {
            BigDecimal text = new BigDecimal(formPropertyMap.get(PURCHASE_PRICE).getDefaultValue());
            txtPurchasePrice.setValue(text != null ? utils.formatUnitPrice(text) : "");
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null && formPropertyMap.get(SALES_PRICE).getDefaultValue() != null && txtSalesPrice != null && isNumeric(formPropertyMap.get(SALES_PRICE).getDefaultValue())) {
            BigDecimal text = new BigDecimal(formPropertyMap.get(SALES_PRICE).getDefaultValue());
            txtSalesPrice.setValue(text != null ? utils.formatUnitPrice(text) : "");
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null && formPropertyMap.get(PURCHASE_ACCOUNT).getSelectedId() != null && purchaseAccountLookUp != null) {
            purchaseAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(PURCHASE_ACCOUNT).getSelectedId(), formPropertyMap.get(PURCHASE_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null && formPropertyMap.get(SALES_ACCOUNT).getSelectedId() != null && salesAccountLookUp != null) {
            salesAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(SALES_ACCOUNT).getSelectedId(), formPropertyMap.get(SALES_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).getSelectedId() != null && dwCategory != null) {
            dwCategory.setSelected(new SelectItem(formPropertyMap.get(CATEGORY).getSelectedId(), formPropertyMap.get(CATEGORY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(BRAND) != null && formPropertyMap.get(BRAND).getSelectedId() != null && dwBrand != null) {
            dwBrand.setSelected(new SelectItem(formPropertyMap.get(BRAND).getSelectedId(), formPropertyMap.get(BRAND).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null && formPropertyMap.get(CustomFormConstants.BARCODE).getDefaultValue() != null && barcodeGenerator != null) {
            barcodeGenerator.setData(formPropertyMap.get(CustomFormConstants.BARCODE).getDefaultValue(), 1);
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null && formPropertyMap.get(TAX).getSelectedId() != null && taxLookUp != null) {
            taxLookUp.setSelected(new SelectItem(formPropertyMap.get(TAX).getSelectedId(), formPropertyMap.get(TAX).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get("SUPPLIERS") != null && formPropertyMap.get("SUPPLIERS").getSelectedId() != null && multiSupplierTable != null && multiSupplierTable.getWidgets() != null && multiSupplierTable.getWidgets().get(0) != null) {
            CRMLookUp clientLookup = (CRMLookUp) multiSupplierTable.getWidgetsMaps().get(0).getWidget(SUPPLIER);
            if (clientLookup != null) {
                clientLookup.setSelected(formPropertyMap.get("SUPPLIERS").getSelectedId(), formPropertyMap.get("SUPPLIERS").getDefaultValue());
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(OVERTIME_END_DATE) != null && formPropertyMap.get(CustomFormConstants.OVERTIME_END_DATE).getDefaultValue() != null) {
            extraDay.setText(formPropertyMap.get(CustomFormConstants.OVERTIME_END_DATE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(OVERTIME_HOURS) != null && formPropertyMap.get(CustomFormConstants.OVERTIME_HOURS).getDefaultValue() != null) {
            extraHour.setText(formPropertyMap.get(CustomFormConstants.OVERTIME_HOURS).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(SECURITY_TIME) != null && formPropertyMap.get(CustomFormConstants.SECURITY_TIME).getDefaultValue() != null) {
            securityTime.setText(formPropertyMap.get(CustomFormConstants.SECURITY_TIME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null && formPropertyMap.get(TYPE).getSelectedId() != null && dwBrand != null) {
            rentalType.setSelected(new SelectItem(formPropertyMap.get(TYPE).getSelectedId(), formPropertyMap.get(TYPE).getDefaultValue()));
        }



    }

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }

    private ColumnConfig[] getColumns() {
        itemColumns = new LinkedList<>();
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        if (product != null && product.getCustomItemColumns() != null && product.getCustomItemColumns().length > 0) {
            ColumnConfig columnConfig;
            for (ColumnConfigs column : product.getCustomItemColumns()) {
                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ItemTableConstants.UNITS:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITS, column.isChanged() ? column.getTitle() : wfmStrings.units(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.UNITS);
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, column.isChanged() ? column.getTitle() : wfmStrings.price(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.UNITPRICE);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.DESCRIPTION);
                        break;
                }
            }
        } else {
            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.UNITS, wfmStrings.units(), 150, true));
            itemColumns.add(ItemTableConstants.UNITS);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, wfmStrings.price(), 150, true));
            itemColumns.add(ItemTableConstants.UNITPRICE);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250, false));
            itemColumns.add(ItemTableConstants.DESCRIPTION);
        }
        return columnsList.toArray(new ColumnConfig[]{});
    }

    private Widget[] getWidgets(RentalProductItem item) {
        int index = 0;
        final Widget[] widgets = new Widget[itemColumns.size()];
        for (String columnCode : itemColumns) {
            if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                TextArea2 txtDescription = new TextArea2();
                if (item != null && item.getDescription() != null) {
                    txtDescription.setText(item.getDescription());
                }
                txtDescription.setTitle(columnCode);
                txtDescription.hideCharacterLimitPanel();
                widgets[index++] = txtDescription;
            } else if (ItemTableConstants.UNITS.equals(columnCode)) {
                DataListBox units = new DataListBox();
                units.setItems(RENTAL_UNITS);
                units.setEnabled(false);
                if (item != null && item.getUnitCode() != null) {
                    units.setSelectedByDescription(item.getUnitCode());
                }
                widgets[index++] = units;
            } else if (ItemTableConstants.UNITPRICE.equals(columnCode)) {
                final CustomCellTextBox txtPrice = new CustomCellTextBox();
                Validation.checkToFocusTextBox(txtPrice, AccountingUtils.getUnitPriceZero());
                txtPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                txtPrice.setWidth("100%");
                Validation.addNumericKeyboardListener(txtPrice, 2);
                if (item != null && item.getPrice() != null) {
                    txtPrice.setText(AccountingUtils.get().formatPrice(item.getPrice()));
                }
                txtPrice.addChangeHandler(c -> {
                    priceChange(txtPrice);
                });
                txtPrice.setTitle(columnCode);
                widgets[index++] = txtPrice;
            }
        }
        return widgets;
    }
}
