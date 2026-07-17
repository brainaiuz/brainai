package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ImportProductInitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductImportFillingData;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ReferenceInsertionTable;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.importfile.client.rpc.CoreProductItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Sep 21, 2010
 * Time: 1:43:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportProductsView extends CustomForm implements Constants, AccountingConstants, FormHasCustomFieldInterface {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final String importProductsView = "import_products_view_";

    private final String type; //type is a import type. For example type contains {FROM_CSV, FROM_PARENT(subsidiry)}
    private String fromView;

    public ImportProductsView(Integer objectId, String type, String viewName) {
        super("add", accountingStrings.importProducts());
        this.objectId = objectId;
        this.type = type;
        this.fromView = fromView;
    }

    private static final String viewName = accountingStrings.importProducts();
    private final Integer objectId;
    private SelectItem[] items;
    private char defaultSeparator = ',';

    //Personal Information
    private WfmForm.Field numberField;
    private WfmForm.Field nameField;

    private DataListBox number;
    private DataListBox itemsForObjectID;
    private DataListBox name;
    private DataListBox description;
    private DataListBox sp_description;
    private DataListBox pr_description;
    private DataListBox fr_description;
    private DataListBox sp_name;
    private DataListBox pr_name;
    private DataListBox fr_name;
    private DataListBox sysCategoryItems;
    private DataListBox brand;
    private ReferenceInsertionTable categoryInsertionType;
    private DataListBox sysProductTypeItems;
    private ReferenceInsertionTable productInsertionType;

    private DataListBox skuNumber;
    private DataListBox barCode;
    private DataListBox upcNumber;
    private DataListBox manufacturer;
    private DataListBox partNumber;

    private DataListBox sysUnitMeasurementItems;
    private ReferenceInsertionTable unitMeasurementInsertionType;
    private DataListBox sysVendorItems;
    private ReferenceInsertionTable vendorInsertionType;

    private DataListBox costPrice;
    private DataListBox sellingPrice;
    private AccountsLookUp sysAccountItems;
    private ReferenceInsertionTable accountInsertionType;
    private AccountsLookUp sysCogsAccountItems;
    private ReferenceInsertionTable cogsAccountInsertionType;

    private WfmDropdown sysTaxRateItems;
    private ReferenceInsertionTable taxRateInsertionType;

    private DataListBox sysWarehouseItems;
    private DataListBox sysPriceLevelItems;
    private DataListBox quantity;
    private DynamicTable warehouseTable;
    private DynamicTable priceLevelTable;

    //Inventory Item, Product Kit, Rental Item Related
    private AccountsLookUp sysAssetAccountItems;
    private ReferenceInsertionTable assetAccountInsertionType;
    private DataListBox globalReorderPoint;
    private DataListBox openingBalanceDate;

    private HTML titleView;
    private KpiCheckBox hasHeader;
    private KpiRadioButton skipDuplicates;
    private KpiRadioButton mergeDuplicates;
    private KpiRadioButton cloneDuplicates;
    private FlexTable duplicateActionTable;

    private WfmForm table;
    //private CustomFieldPopup customFieldPopup;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<String> currencies;
    private SelectItem[] warehouses;
    private SelectItem[] priceLevels;
    private HashMap<String, SelectItem> warehouseMap;
    private HashMap<String, SelectItem> priceLevelMap;
    private LinkedHashMap<String, DataListBox> multiSellingPrices;
    private LinkedHashMap<String, DataListBox> multiCostPrices;
    private DataListBox[] tbValues;
    private DataListBox location;

    int errors = 0;

    protected Widget onInitialize() {
        super.onInitialize();
        init();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_PRODUCT_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }


    private void init() {
        AccountingService.App.get().getProductInitData(new AbstractAsyncCallback<ImportProductInitItem>() {
            @Override
            public void failure(Throwable throwable) {
                initialize();
            }

            @Override
            public void success(ImportProductInitItem result) {
                customFieldItems = result.getCustomFieldItems();
                currencies = result.getCurrencyList();
                warehouses = result.getWarehouses();
                priceLevels = result.getPriceLevels();
                initialize();
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getProductImportFillingData(new AbstractAsyncCallback<ProductImportFillingData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ProductImportFillingData data) {

                sysCategoryItems.setItems(data.getCategories());
                sysUnitMeasurementItems.setItems(data.getUnitMeasurements());
                sysVendorItems.setItems(data.getVendors());
                sysTaxRateItems.addItems(data.getTaxes());
                sysPriceLevelItems.setItems(data.getPriceLevels());

                if (data.getDefaultWarehouseId() != null) {
                    sysWarehouseItems.setSelected(data.getDefaultWarehouseId());
                }

                if (data.getDefaultAccount() != null) {
                    sysAccountItems.setSelected(data.getDefaultAccount());
                }
                if (data.getDefaultCogsAccount() != null) {
                    sysCogsAccountItems.setSelected(data.getDefaultCogsAccount());
                }
                if (data.getDefaultAssetAccount() != null) {
                    sysAssetAccountItems.setSelected(data.getDefaultAssetAccount());
                }
                if (data.getTaxes() != null && data.getTaxes().length > 0 && data.getTaxes()[0] != null) {
                    sysTaxRateItems.setSelected(data.getTaxes()[0].getId());
                }
                LoadingPanel.loading(false);
            }
        });
    }
    private final String errorMessage = wfmStrings.error();

    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save());

    }

    private void setSelected(SelectItem[] items) {
        if (items != null && items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null && items[i].getName() != null && !"".equals(items[i].getName())) {
                    if ("Part Number".equalsIgnoreCase(items[i].getName())) {
                        partNumber.setSelected(i);
                        break;
                    }
                }
            }
        }
    }

    public void initialize() {
        LoadingPanel.loading(true);
        createAndSetWidth();

        table = new WfmForm(new String[]{"7%", "100%", "25%"});
        table.setLabelAlignment(WfmForm.ALIGN_RIGHT);

        titleView = new HTML("<div style='margin:1px 0 0 1px;'><b class=customTitle><font size=+1>" + viewName + "</font></b></div>");

        if (INVENTORY_ITEMS.equals(fromView)) {
            sysProductTypeItems.setItems(new SelectItem[]{new SelectItem(INVENTORY_ITEM, INVENTORY_ITEM_STR)});
        } else if (ASSEMBLY_PRODUCTS.equals(fromView)) {
            sysProductTypeItems.setItems(new SelectItem[]{new SelectItem(ASSEMBLY_ITEM, ASSEMBLY_ITEM_STR)});
        } else {
            sysProductTypeItems.setItems(PRODUCT_TYPES);
        }
        sysProductTypeItems.setSelected(INVENTORY_ITEM);
        productInsertionType = new ReferenceInsertionTable(wfmStrings.type(), sysProductTypeItems, true, table);
        categoryInsertionType = new ReferenceInsertionTable(wfmStrings.category(), sysCategoryItems, table);
        taxRateInsertionType = new ReferenceInsertionTable(wfmStrings.taxRate(), sysTaxRateItems, false, table);

        unitMeasurementInsertionType = new ReferenceInsertionTable(wfmStrings.unitMeasurement(), sysUnitMeasurementItems, table);
        vendorInsertionType = new ReferenceInsertionTable(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), sysVendorItems, table);
        table.addHorizontalLine();

        accountInsertionType = new ReferenceInsertionTable(wfmStrings.account() + "/" + accountingStrings.incomeAccount(), sysAccountItems, true, table);
        cogsAccountInsertionType = new ReferenceInsertionTable(wfmStrings.cogsAccount(), sysCogsAccountItems, table);
        assetAccountInsertionType = new ReferenceInsertionTable(wfmStrings.assetAccount(), sysAssetAccountItems, table);

        table.addHorizontalLine();
        if (customFieldItems != null && customFieldItems.size() > 0) {
            addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

            tbValues = new DataListBox[customFieldItems.size()];
            for (int i = 0; i < customFieldItems.size(); i++) {
                tbValues[i] = new DataListBox();
                tbValues[i].addStyleName(DEFAULT_WIDTH);

                SelectItem cf = new SelectItem(customFieldItems.get(i).getObjectId(), customFieldItems.get(i).getFieldName());
                tbValues[i].addListItem(cf);
                tbValues[i].setSelectedByValue(cf.getName());

                addField("string_value" + (i + 1), tbValues[i], customFieldItems.get(i).getFieldName());
            }
        }

        hasHeader = new KpiCheckBox("");
        hasHeader.setValue(Boolean.TRUE);
        hasHeader.setText(wfmStrings.myCSVFileHasHeaders());

        skipDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.skip());
        skipDuplicates.setFormValue(ImportFile.SKIP);
        skipDuplicates.setValue(Boolean.TRUE);

        mergeDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.overwrite());
        mergeDuplicates.setFormValue(ImportFile.MERGE);

        cloneDuplicates = new KpiRadioButton("duplicateAction", wfmStrings.clonE());
        cloneDuplicates.setFormValue(ImportFile.CLONE);

        duplicateActionTable = new FlexTable();
        duplicateActionTable.addStyleName(DEFAULT_WIDTH);
        duplicateActionTable.setWidget(0, 0, skipDuplicates);
        duplicateActionTable.setWidget(0, 1, mergeDuplicates);
        duplicateActionTable.setWidget(0, 2, cloneDuplicates);

        if (FROM_PARENT.equals(type)) {
            ProductService.App.get().getProductColumns(new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(SelectItem[] selectItems) {
                    LoadingPanel.loading(false);
                    items = selectItems;
                    initItemsWithColumns(selectItems);

                    int index = 0;
                    number.setSelected(index++);
                    name.setSelected(index++);
                    description.setSelected(index++);
                    location.setSelected(index++);

                    if (Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE)) {
                        index++;
                        productInsertionType.getCsvDataListBox().resetSelectedItem();
                        productInsertionType.getCsvDataListBox().setEnabled(false);
                    } else {
                        productInsertionType.getCsvDataListBox().setSelected(index++);
                    }
                    categoryInsertionType.getCsvDataListBox().setSelected(index++);
                    taxRateInsertionType.getCsvDataListBox().setSelected(index++);
                    skuNumber.setSelected(index++);
                    upcNumber.setSelected(index++);
                    partNumber.setSelected(index++);
                    unitMeasurementInsertionType.getCsvDataListBox().setSelected(index++);
                    vendorInsertionType.getCsvDataListBox().setSelected(index++);
                    manufacturer.setSelected(index++);
                    costPrice.setSelected(index++);
                    sellingPrice.setSelected(index++);
                }
            });
        } else {
            AccountingService.App.get().getCSVColumns(objectId, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
                public void onFailure(Throwable d) {
                    LoadingPanel.loading(false);
                    //closeTab();
                }

                public void onSuccess(final HashMap<String, SelectItem[]> o) {
                    LoadingPanel.loading(false);

                    for (HashMap.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        items = entry.getValue();
                        if (!key.equals(String.valueOf(defaultSeparator))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    initItemsWithColumns(items);
                    setSelected(items);
                }
            });
        }
        this.drawForm();
    }

    private void initItemsWithColumns(SelectItem[] items) {
        setItems(items,
                itemsForObjectID, number, name, sp_name, pr_name, fr_name, description, sp_description, pr_description, fr_description,
                categoryInsertionType.getCsvDataListBox(), productInsertionType.getCsvDataListBox(),
                skuNumber, barCode, upcNumber, manufacturer, partNumber, openingBalanceDate, unitMeasurementInsertionType.getCsvDataListBox(),
                vendorInsertionType.getCsvDataListBox(), costPrice, sellingPrice,
                accountInsertionType.getCsvDataListBox(), cogsAccountInsertionType.getCsvDataListBox(),
                taxRateInsertionType.getCsvDataListBox(), quantity, assetAccountInsertionType.getCsvDataListBox(),
                globalReorderPoint, brand,location);

        productInsertionType.getCsvDataListBox().setSelectedByTheBestValue(wfmStrings.type());
        number.setSelectedByTheBestValue(wfmStrings.number());
        name.setSelectedByTheBestValue(wfmStrings.name());
        description.setSelectedByTheBestValue(wfmStrings.description());
        brand.setSelectedByTheBestValue(wfmStrings.brand());
        categoryInsertionType.getCsvDataListBox().setSelectedByTheBestValue(wfmStrings.category());
        taxRateInsertionType.getCsvDataListBox().setSelectedByTheBestValue(wfmStrings.taxRate());
        skuNumber.setSelectedByTheBestValue(wfmStrings.skuNumber());
        upcNumber.setSelectedByTheBestValue(wfmStrings.upcNumber());
        manufacturer.setSelectedByTheBestValue(wfmStrings.manufacturer());
        partNumber.setSelectedByTheBestValue(wfmStrings.partNumber());
        vendorInsertionType.getCsvDataListBox().setSelectedByTheBestValue(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
        unitMeasurementInsertionType.getCsvDataListBox().setSelectedByTheBestValue(wfmStrings.unitMeasurement());
        cogsAccountInsertionType.getCsvDataListBox().setSelectedByTheBestValue(wfmStrings.cogsAccount());
        assetAccountInsertionType.getCsvDataListBox().setSelectedByTheBestValue(wfmStrings.assetAccount());
        accountInsertionType.getCsvDataListBox().setSelectedByTheBestValue(accountingStrings.incomeAccount());
        sysUnitMeasurementItems.setSelectedByTheBestValue(wfmStrings.unitMeasurement());
        costPrice.setSelectedByTheBestValue(wfmStrings.costPrice());
        sellingPrice.setSelectedByTheBestValue(wfmStrings.sellingPrice());
        quantity.setSelectedByTheBestValue(wfmStrings.qtyOnHand());
        globalReorderPoint.setSelectedByTheBestValue(accountingStrings.globalReorderPoint());
        openingBalanceDate.setSelectedByTheBestValue(wfmStrings.openingBalanceDate());
        barCode.setSelectedByTheBestValue(wfmStrings.barcode());
        location.setSelectedByTheBestValue(wfmStrings.location());

        if (Utils.isMultipleSalesPriceEnable()) {
            for (DataListBox selPrice : multiSellingPrices.values()) {
                setItems(items, selPrice);
            }
            for (DataListBox costPrice : multiCostPrices.values()) {
                setItems(items, costPrice);
            }
        }
        if (Utils.isMultiWarehouseEnabled()) {
            for (int rowID = 0; rowID < warehouseTable.getRowNumber(); rowID++) {
                DynamicTableItem item = warehouseTable.getItem(rowID);
                DataListBox warehouseListBox = (DataListBox) item.getColumnById(WAREHOUSE);
                SelectItem warehouse = warehouseListBox.getSelectedItem();

                setItems(items, warehouseListBox);

                if (warehouse != null) {
                    warehouseListBox.setSelectedByTheBestValue(warehouse.getName());
                }
            }
        }
        for (int rowID = 0; rowID < priceLevelTable.getRowNumber(); rowID++) {
            DynamicTableItem item = priceLevelTable.getItem(rowID);
            DataListBox listBox = (DataListBox) item.getColumnById(CustomFormConstants.PRICE_LEVEL);
            SelectItem selectItem = listBox.getSelectedItem();

            setItems(items, listBox);

            if (selectItem != null) {
                listBox.setSelectedByTheBestValue(selectItem.getName());
            }
        }
        /*if (customFieldPopup != null) {
            customFieldPopup.fillCustomFieldBoxes();
            customFieldPopup.drawCustomFieldForm();
        } else*/
        if (tbValues != null) {
            for (DataListBox dataListBox : tbValues) {
                if (dataListBox != null) {
                    SelectItem cf = dataListBox.getSelectedItem();

                    dataListBox.setItems(items);

                    if (cf != null) {
                        dataListBox.setSelectedByTheBestValue(cf.getName());
                    }
                }
            }
        }
    }

    private void setItems(SelectItem[] items, final DataListBox... dataListBoxes) {
        for (DataListBox dataListBox : dataListBoxes) {
            if (dataListBox != null) {
                dataListBox.setItems(items);
            }
        }
    }

    private void createAndSetWidth() {
        itemsForObjectID = new DataListBox();
        itemsForObjectID.ensureDebugId(importProductsView + "objectId");

        number = new DataListBox();
        number.ensureDebugId(importProductsView + "number");

        name = new DataListBox();
        sp_name = new DataListBox();
        pr_name = new DataListBox();
        fr_name = new DataListBox();
        name.ensureDebugId(importProductsView + "name");

        description = new DataListBox();
        sp_description = new DataListBox();
        pr_description = new DataListBox();
        fr_description = new DataListBox();
        description.ensureDebugId(importProductsView + "description");

        sysCategoryItems = new DataListBox();
        sysCategoryItems.ensureDebugId(importProductsView + "sysCategoryItems");

        brand = new DataListBox();
        brand.ensureDebugId(importProductsView + "brands");

        sysProductTypeItems = new DataListBox();
        sysProductTypeItems.ensureDebugId(importProductsView + "sysProductTypeItems");

        skuNumber = new DataListBox();
        skuNumber.ensureDebugId(importProductsView + "skuNumber");

        barCode = new DataListBox();
        barCode.ensureDebugId(importProductsView + "barcode");

        upcNumber = new DataListBox();
        upcNumber.ensureDebugId(importProductsView + "upcNumber");

        manufacturer = new DataListBox();
        manufacturer.ensureDebugId(importProductsView + "manufacturer");

        partNumber = new DataListBox();
        partNumber.ensureDebugId(importProductsView + "partNumber");

        costPrice = new DataListBox();
        costPrice.ensureDebugId(importProductsView + "costPrice");

        sellingPrice = new DataListBox();
        sellingPrice.ensureDebugId(importProductsView + "sellingPrice");

        location = new DataListBox();
        location.ensureDebugId((importProductsView+ "location"));


        if (Utils.isMultipleSalesPriceEnable()) {
            multiCostPrices = new LinkedHashMap<>();
            multiSellingPrices = new LinkedHashMap<>();

            for (String currency : currencies) {
                DataListBox costPrice = new DataListBox();
                DataListBox salesPrice = new DataListBox();
                costPrice.addStyleName(DEFAULT_WIDTH);
                salesPrice.addStyleName(DEFAULT_WIDTH);
                multiCostPrices.put(currency, costPrice);
                multiSellingPrices.put(currency, salesPrice);
            }
        }

        quantity = new DataListBox();
        quantity.ensureDebugId(importProductsView + "quantity");

        if (Utils.isMultiWarehouseEnabled()) {
            warehouseTable = new DynamicTable(getColumns(), true);
            warehouseMap = new HashMap<>();
            for (SelectItem warehouse : warehouses) {
                warehouseTable.addRow(getWidgets(warehouse));
                warehouseMap.put(warehouse.getName().trim(), warehouse);
            }

            quantity.setEnabled(false);
        }
        priceLevelTable = new DynamicTable(getPriceLevelColumns(), AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_HEADER, AccountingCustomFormConstants.STYLE_PRODUCT_TABLE_BORDER, true);
        priceLevelTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);
        priceLevelMap = new HashMap<>();
        for (SelectItem priceLevel : priceLevels) {
            priceLevelTable.addRow(getWidgets(priceLevel));
            priceLevelMap.put(priceLevel.getName().trim(), priceLevel);
        }

        sysUnitMeasurementItems = new DataListBox();
        sysUnitMeasurementItems.ensureDebugId(importProductsView + "sysUnitMeasurementItems");

        sysVendorItems = new DataListBox();
        sysVendorItems.ensureDebugId(importProductsView + "sysVendorItems");

        sysAccountItems = new AccountsLookUp(RECEIVABLE);
        sysAccountItems.ensureDebugId(importProductsView + "sysAccountItems");

        sysCogsAccountItems = new AccountsLookUp(EXPENSES);
        sysCogsAccountItems.ensureDebugId(importProductsView + "sysCogsAccountItems");
        sysCogsAccountItems.setAccountCode(COST_OF_SALES);

        sysTaxRateItems = new WfmDropdown(false, true);
        sysTaxRateItems.ensureDebugId(importProductsView + "sysTaxRateItems");

        sysWarehouseItems = new DataListBox();
        sysWarehouseItems.ensureDebugId(importProductsView + "sysWarehouseItems");

        sysPriceLevelItems = new DataListBox();
        sysPriceLevelItems.ensureDebugId(importProductsView + "sysPriceLevelItems");

        sysAssetAccountItems = new AccountsLookUp(Constants.ASSETS);
        sysAssetAccountItems.ensureDebugId(importProductsView + "sysAssetAccountItems");

        globalReorderPoint = new DataListBox();
        globalReorderPoint.ensureDebugId(importProductsView + "globalReorderPoint");

        openingBalanceDate = new DataListBox();
        openingBalanceDate.ensureDebugId(importProductsView + "openingBalanceDate");

        sysWarehouseItems.setWithoutNullLabel(true);

        itemsForObjectID.addStyleName(DEFAULT_WIDTH);
        number.addStyleName(DEFAULT_WIDTH);
        name.addStyleName(DEFAULT_WIDTH);
        description.addStyleName(DEFAULT_WIDTH);
        brand.addStyleName(DEFAULT_WIDTH);
        skuNumber.addStyleName(DEFAULT_WIDTH);
        barCode.addStyleName(DEFAULT_WIDTH);
        sysProductTypeItems.setWithoutNullLabel(true);

        upcNumber.addStyleName(DEFAULT_WIDTH);
        manufacturer.addStyleName(DEFAULT_WIDTH);
        partNumber.addStyleName(DEFAULT_WIDTH);
        costPrice.addStyleName(DEFAULT_WIDTH);
        sellingPrice.addStyleName(DEFAULT_WIDTH);
        sysWarehouseItems.addStyleName(DEFAULT_WIDTH);
        quantity.addStyleName(DEFAULT_WIDTH);
        sysAssetAccountItems.addStyleName(DEFAULT_WIDTH);
        globalReorderPoint.addStyleName(DEFAULT_WIDTH);
        openingBalanceDate.addStyleName(DEFAULT_WIDTH);
        location.addStyleName(DEFAULT_WIDTH);
    }

    protected void drawForm() {
        if (Utils.isMultipleSalesPriceEnable()) {
            addField(PRODUCT_ID, itemsForObjectID, getTitle(accountingStrings.productId()));
        }
        addField(MY_CSV_FILE_HAS_HEADERS, hasHeader, getTitle(wfmStrings.myCSVFileHasHeaders()));
        addField(DUPLICATE, duplicateActionTable, getTitle(wfmStrings.duplicateAction()));
        addField(TITLE, titleView);
        addTitleField(BASIC_DETAILS, wfmStrings.basicDetails());
        addField(CustomFormConstants.PRODUCT_TYPE, productInsertionType.getTable(), getTitle(wfmStrings.type()));
        addField(CATEGORY, categoryInsertionType.getTable(), getTitle(wfmStrings.category()));
        addField(BRAND, brand, getTitle(wfmStrings.brand()));
        addField(TAX_CODE, taxRateInsertionType.getTable(), getTitle(wfmStrings.taxRate()));
        addField(Constants.NUMBER, number, getTitle(wfmStrings.number()));
        addField(NAME, name, getTitle(wfmStrings.name(), true));

        addField(DESCRIPTION, description, getTitle(wfmStrings.description()));

        //for MORE DETAILS
        addTitleField(MORE_DETAILS, wfmStrings.moreDetails());
        addField(SKY_NUMBER, skuNumber, wfmStrings.skuNumber());
        addField(CustomFormConstants.BARCODE, barCode, wfmStrings.barcode());
        addField(UPC_NUMBER, upcNumber, wfmStrings.upcNumber());
        addField(MANUFACTURER, manufacturer, wfmStrings.manufacturer());
        addField(PART_NUMBER, partNumber, wfmStrings.partNumber());
        addField(UNIT_MEASUREMENT, unitMeasurementInsertionType.getTable(), getTitle(wfmStrings.unitMeasurement()));
        addField(VENDOR_INSERTTION_TYPE, vendorInsertionType.getTable(), getTitle(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier())));
        addField(LOCATIONS, location, getTitle(wfmStrings.locations()));

        //for financial data
        addTitleField(FINANCIAL_DATA, accountingStrings.financialData());
        addField(COST_PRICE, costPrice, wfmStrings.costPrice());
        if (Utils.isMultipleSalesPriceEnable()) {
            int i = 1;
            for (String currency : multiCostPrices.keySet()) {
                addField(COST_PRICE + i, multiCostPrices.get(currency), wfmStrings.costPrice() + " " + currency);
                i++;
            }
        }
        addField(COGS_ACCOUNT, cogsAccountInsertionType.getTable(), getTitle(wfmStrings.cogsAccount()));
        addField(SELLING_PRICE, sellingPrice, wfmStrings.sellingPrice());
        if (Utils.isMultipleSalesPriceEnable()) {
            int i = 1;
            for (String currency : multiSellingPrices.keySet()) {
                addField(SELLING_PRICE + i, multiSellingPrices.get(currency), wfmStrings.sellingPrice() + " " + currency);
                i++;
            }
        }
        addField(ACCOUNT_TYPE, accountInsertionType.getTable(), getTitle(accountingStrings.incomeAccount()));
        addField(ASSET_ACCOUNT, assetAccountInsertionType.getTable(), getTitle(wfmStrings.assetAccount()));

        addField(QUANTITY_STR, quantity, getTitle(wfmStrings.qtyOnHand()));
        addField(GLOBAL_REORDER_POINT, globalReorderPoint, getTitle(accountingStrings.globalReorderPoint()));
        addField(OPENING_BALANCE_DATE, openingBalanceDate, getTitle(wfmStrings.openingBalanceDate()));

        if (Utils.isMultiWarehouseEnabled()) {
            addTitleField(WAREHOUSE, accountingStrings.warehouse());
            addField(WAREHOUSE_PANEL, warehouseTable, getTitle(accountingStrings.warehouse()));
//            DOM.getElementById("warehouse_section").getStyle().setDisplay(Style.Display.BLOCK);
        } /*else {
            DOM.getElementById("warehouse_section").getStyle().setDisplay(Style.Display.NONE);
        }*/

        addTitleField(CustomFormConstants.PRICE_LEVEL, wfmStrings.priceLevel());
        addField(PRICE_LEVEL_PANEL, priceLevelTable, getTitle(wfmStrings.priceLevel()));
        show();
    }

    protected void save() {
        LoadingPanel.loading(true);
        if (!validate()) {
            LoadingPanel.loading(false);
            return;
        }
        CoreProductItem item = new CoreProductItem();
        item.setObjectId(objectId);
        item.setMultiWarehouseEnabled(Utils.isMultiWarehouseEnabled());
        item.setItemId(getSelectedItem(itemsForObjectID));
        item.setNumberId(getSelectedItem(number) != null ? getSelectedItem(number) : 0);//This should be discussed
        item.setNameId(getSelectedItem(name));
        item.setOpeningBalanceDate(getSelectedItem(openingBalanceDate));
        item.setSpNameId(getSelectedItem(sp_name));
        item.setPrNameId(getSelectedItem(pr_name));
        item.setFrNameId(getSelectedItem(fr_name));
        item.setDescriptionId(getSelectedItem(description));
        item.setSpDescriptionId(getSelectedItem(sp_description));
        item.setPrDescriptionId(getSelectedItem(pr_description));
        item.setFrDescriptionId(getSelectedItem(fr_description));
        item.setCategory(categoryInsertionType.getData());
        item.setProductType(productInsertionType.getData());
        item.setSkuNumberId(getSelectedItem(skuNumber));
        item.setBarcodeId(getSelectedItem(barCode));
        item.setManufacturerId(getSelectedItem(manufacturer));
        item.setPartNumberId(getSelectedItem(partNumber));
        item.setBrandId(getSelectedItem(brand));

        item.setUpcNumberId(getSelectedItem(upcNumber));
        item.setUnitMeasurement(unitMeasurementInsertionType.getData());
        item.setVendor(vendorInsertionType.getData());

        item.setCostPriceId(getSelectedItem(costPrice));
        item.setSellingPriceId(getSelectedItem(sellingPrice));
        item.setLocationNameId(getSelectedItem(location));

        if (Utils.isMultipleSalesPriceEnable()) {
            for (String currency : multiSellingPrices.keySet()) {
                switch (currency.toUpperCase()) {
                    case "USD":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_USD, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "GBP":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_GBP, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "AED":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_AED, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "EUR":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_EUR, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "RUB":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_RUB, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "SAR":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_SAR, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "KWD":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_KWD, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                    case "PKR":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_SELLING_PRICE_PKR, getSelectedItem(multiSellingPrices.get(currency)));
                        break;
                }
            }
            for (String currency : multiCostPrices.keySet()) {
                switch (currency.toUpperCase()) {
                    case "USD":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_USD, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "GBP":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_GBP, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "AED":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_AED, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "EUR":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_EUR, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "RUB":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_RUB, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "SAR":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_SAR, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "KWD":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_KWD, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                    case "PKR":
                        item.getMultiPrices().put(ImportField.ProductFields.FIELD_COST_PRICE_PKR, getSelectedItem(multiCostPrices.get(currency)));
                        break;
                }
            }
        }
        item.setAccount(accountInsertionType.getData());
        item.setCogsAccount(cogsAccountInsertionType.getData());
        item.setTaxRate(taxRateInsertionType.getData());
        /*if (warehouseInsertionType != null) {
            DataListBox warehouse = warehouseInsertionType.getCsvDataListBox();
            DataListBox sysWarehouse = (DataListBox) warehouseInsertionType.getSysValuesWidget();
            item.setWarehouseNameId(getSelectedItem(warehouse));
            item.setSysWarehouseId(getSelectedItem(sysWarehouse));
        }*/
        item.setQuantityId(getSelectedItem(quantity));

        item.setAssetAccount(assetAccountInsertionType.getData());
        item.setGlobalReorderPointId(getSelectedItem(globalReorderPoint));
        item.setOpeningBalanceDate(getSelectedItem(openingBalanceDate));

        if (Utils.isMultiWarehouseEnabled()) {
            for (int rowID = 0; rowID < warehouseTable.getRowNumber(); rowID++) {
                DynamicTableItem dynamicTableItem = warehouseTable.getItem(rowID);
                DataListBox warehouseListBox = (DataListBox) dynamicTableItem.getColumnById(WAREHOUSE);

                if (warehouseListBox.getSelectedItem() != null && warehouseListBox.getSelectedItem().getId() != null) {
                    if (warehouseMap.get(warehouseListBox.getSelectedItem().getName().trim()) != null) {
                        SelectItem warehouseItem = warehouseMap.get(warehouseListBox.getSelectedItem().getName().trim());
                        item.getMultiWarehouses().put(warehouseItem.getId() + WAREHOUSE_ID_PART, getSelectedItem(warehouseListBox));
                    }
                }
            }
        }
        for (int rowID = 0; rowID < priceLevelTable.getRowNumber(); rowID++) {
            DynamicTableItem dynamicTableItem = priceLevelTable.getItem(rowID);
            DataListBox priceLevelListBox = (DataListBox) dynamicTableItem.getColumnById(CustomFormConstants.PRICE_LEVEL);

            if (priceLevelListBox.getSelectedItem() != null && priceLevelListBox.getSelectedItem().getId() != null) {
                if (priceLevelMap.get(priceLevelListBox.getSelectedItem().getName().trim()) != null) {
                    SelectItem priceLevelItem = priceLevelMap.get(priceLevelListBox.getSelectedItem().getName().trim());
                    item.getMultiPriceLevels().put(priceLevelItem.getId() + PRICELEVEL_ID_PART, getSelectedItem(priceLevelListBox));
                }
            }
        }

        /*if (enabledProductLocalization) {
            item.setCustomFields(customFieldPopup.getCustomFieldItemsForSave());
        } else*/
        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (int i = 0; i < tbValues.length; i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(customFieldItems.get(i).getObjectId());
                resultItem.setDataType(customFieldItems.get(i).getDataType());
                resultItem.setUiType(customFieldItems.get(i).getUiType());
                resultItem.setColumnCode(customFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(customFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(customFieldItems.get(i).getPredefinedValues());
                resultItem.setFieldName(customFieldItems.get(i).getFieldName());

                if (tbValues[i].getSelectedItem() != null && tbValues[i].getSelectedItem().getId() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            item.setCustomFields(resultItemList);
        }
        final ImportFile importFile = item.getImportFile();

        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader.getValue());
        if (skipDuplicates.getValue()) {
            importFile.setDuplicateAction(skipDuplicates.getFormValue());
        }
        if (mergeDuplicates.getValue()) {
            importFile.setDuplicateAction(mergeDuplicates.getFormValue());
        }
        if (cloneDuplicates.getValue()) {
            importFile.setDuplicateAction(cloneDuplicates.getFormValue());
        }
        importFile.setType(FROM_PARENT.equals(type) ? ImportTypeEnum.PRODUCT_FROM_PARENT : ImportTypeEnum.PRODUCT);
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

    private Integer getSelectedItem(DataListBox dataListBox) {
        if (dataListBox != null) {
            if (dataListBox.getSelectedItem() != null) {
                return dataListBox.getSelectedItem().getId();
            }
        }
        return null;
    }

    private boolean validate() {
        errors = 0;
        table.cleanupErrors();
        if (!Validation.validateListBoxRequired(name, nameField, accountingStrings.pleaseSelectName())) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void showSuccessMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
        messageBox.setTitle(wfmStrings.information());
        messageBox.setMessage(WfmMessages.App.get().itemsSuccessfullyImported(Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()))); //crmMessages.importingMessage("Contact")
        messageBox.addCloseHandler(popupPanelCloseEvent -> {
            closeTab();
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCT_IMPORT_RELOAD_PAGE, null, null);
        });
        messageBox.open();
    }

    private void showFailureMessage(final String... message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(message != null && message.length > 0 ? message[0] : errorMessage);
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> {
            if (message == null || message.length == 0) {
                closeTab();
            }
        });
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

    /*private class CustomFieldPopup extends WfmMessageBox {
        FlexTable customTable;
        WfmMessageBox messageBox;
        DataListBox[] customFieldBoxesEN;
        DataListBox[] customFieldBoxesSP;
        DataListBox[] customFieldBoxesFR;
        DataListBox[] customFieldBoxesPR;
        List<CompanyCustomFieldItem> customFieldItems;
        List<CompanyCustomFieldItem> customFieldItemsForSave;
        WfmButton2 save;
        WfmButton2 cancel;

        private CustomFieldPopup(List<CompanyCustomFieldItem> customFieldItems) {
            super(IconEnum.INFO, true);
            this.customFieldItems = customFieldItems;
        }

        private void fillCustomFieldBoxes() {
            customFieldBoxesEN = new DataListBox[customFieldItems.size()];
            for (int i = 0; i < customFieldItems.size(); i++) {
                customFieldBoxesEN[i] = new DataListBox();
                customFieldBoxesEN[i].setWidth("105px");
                customFieldBoxesEN[i].setItems(items);
            }
            customFieldBoxesSP = new DataListBox[customFieldItems.size()];
            for (int i = 0; i < customFieldItems.size(); i++) {
                customFieldBoxesSP[i] = new DataListBox();
                customFieldBoxesSP[i].setWidth("105px");
                customFieldBoxesSP[i].setItems(items);
            }
            customFieldBoxesFR = new DataListBox[customFieldItems.size()];
            for (int i = 0; i < customFieldItems.size(); i++) {
                customFieldBoxesFR[i] = new DataListBox();
                customFieldBoxesFR[i].setWidth("105px");
                customFieldBoxesFR[i].setItems(items);
            }
            customFieldBoxesPR = new DataListBox[customFieldItems.size()];
            for (int i = 0; i < customFieldItems.size(); i++) {
                customFieldBoxesPR[i] = new DataListBox();
                customFieldBoxesPR[i].setWidth("105px");
                customFieldBoxesPR[i].setItems(items);
            }
        }

        private void drawCustomFieldForm() {
            AbsolutePanel ap = new AbsolutePanel();
            HorizontalPanel exportPanel = new HorizontalPanel();
            exportPanel.setSpacing(20);
            ScrollPanel scrollPanel = new ScrollPanel();
            scrollPanel.setHeight("250px");
            scrollPanel.setWidth("650px");
            messageBox = new WfmMessageBox(IconEnum.NONE, true);
            messageBox.setWidth(600);
            messageBox.setText(accountingStrings.information());
            messageBox.setMessage("");
            save = new WfmButton2(accountingStrings.saveAndClose());
            save.addClickHandler(event -> messageBox.close());
            exportPanel.add(save);
            cancel = new WfmButton2(accountingStrings.cancel());
            cancel.addClickHandler(event -> messageBox.close());
            exportPanel.add(cancel);
            customTable = new FlexTable();
            customTable.setStyleName("flexTable");
            customTable.setCellPadding(0);
            customTable.setCellSpacing(0);
            customTable.setWidget(0, 0, new HTML("<span  style='width:120px'>" + "Custom Fields" + "</span>"));
            customTable.setWidget(0, 1, new HTML("<span  style='width:15%'>" + "English" + "</span>"));
            customTable.setWidget(0, 2, new HTML("<span  style='width:15%'>" + "Spanish" + "</span>"));
            customTable.setWidget(0, 3, new HTML("<span  style='width:15%'>" + "French" + "</span>"));
            customTable.setWidget(0, 4, new HTML("<span  style='width:15%'>" + "Portuguese" + "</span>"));
            customTable.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
            customTable.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
            customTable.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
            customTable.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
            customTable.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label");
            int i = 1;
            for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                customTable.setWidget(i, 0, new HTML(customFieldItem.getFieldName()));
                customTable.setWidget(i, 1, customFieldBoxesEN[i - 1]);
                customTable.setWidget(i, 2, customFieldBoxesSP[i - 1]);
                customTable.setWidget(i, 3, customFieldBoxesFR[i - 1]);
                customTable.setWidget(i, 4, customFieldBoxesPR[i - 1]);
                customTable.getFlexCellFormatter().setStyleName(i, 0, "flexTable-td");
                customTable.getFlexCellFormatter().setStyleName(i, 1, "flexTable-td");
                customTable.getFlexCellFormatter().setStyleName(i, 2, "flexTable-td");
                customTable.getFlexCellFormatter().setStyleName(i, 3, "flexTable-td");
                customTable.getFlexCellFormatter().setStyleName(i, 4, "flexTable-td");
                i++;
            }
            ap.add(customTable);
            ap.add(exportPanel);
            messageBox.setContent(ap);
        }

        private void showCustomPopup() {
            messageBox.open();
        }

        private List<CompanyCustomFieldItem> getCustomFieldItemsForSave() {
            customFieldItemsForSave = new ArrayList<>();
            for (int i = 0; i < customFieldItems.size(); i++) {
                if (customFieldBoxesEN[i].getSelectedId() != null) {
                    CompanyCustomFieldItem customFieldItem = new CompanyCustomFieldItem();
                    customFieldItem.setObjectId(customFieldItems.get(i).getObjectId());
                    customFieldItem.setDataType(customFieldItems.get(i).getDataType());
                    customFieldItem.setColumnCode(customFieldItems.get(i).getColumnCode());
                    customFieldItem.setCustomFieldSettingID(customFieldItems.get(i).getCustomFieldSettingID());
                    customFieldItem.setPredefinedValues(customFieldItems.get(i).getPredefinedValues());
                    customFieldItem.setFieldStringValue(customFieldBoxesEN[i].getSelectedItem().getId().toString());
                    customFieldItemsForSave.add(customFieldItem);
                }
                if (customFieldBoxesSP[i].getSelectedId() != null) {
                    CompanyCustomFieldItem spCustomFieldItem = new CompanyCustomFieldItem();
                    spCustomFieldItem.setObjectId(customFieldItems.get(i).getObjectId());
                    spCustomFieldItem.setDataType(customFieldItems.get(i).getDataType());
                    spCustomFieldItem.setColumnCode(customFieldItems.get(i).getColumnCode());
                    spCustomFieldItem.setCustomFieldSettingID(customFieldItems.get(i).getCustomFieldSettingID());
                    spCustomFieldItem.setPredefinedValues(customFieldItems.get(i).getPredefinedValues());
                    spCustomFieldItem.setFieldStringValue(customFieldBoxesSP[i].getSelectedItem().getId().toString());
                    spCustomFieldItem.setLocaleCode("es");
                    customFieldItemsForSave.add(spCustomFieldItem);
                }
                if (customFieldBoxesPR[i].getSelectedId() != null) {
                    CompanyCustomFieldItem prCustomFieldItem = new CompanyCustomFieldItem();
                    prCustomFieldItem.setObjectId(customFieldItems.get(i).getObjectId());
                    prCustomFieldItem.setDataType(customFieldItems.get(i).getDataType());
                    prCustomFieldItem.setColumnCode(customFieldItems.get(i).getColumnCode());
                    prCustomFieldItem.setCustomFieldSettingID(customFieldItems.get(i).getCustomFieldSettingID());
                    prCustomFieldItem.setPredefinedValues(customFieldItems.get(i).getPredefinedValues());
                    prCustomFieldItem.setFieldStringValue(customFieldBoxesPR[i].getSelectedItem().getId().toString());
                    prCustomFieldItem.setLocaleCode("pt");
                    customFieldItemsForSave.add(prCustomFieldItem);
                }
                if (customFieldBoxesFR[i].getSelectedId() != null) {
                    CompanyCustomFieldItem frCustomFieldItem = new CompanyCustomFieldItem();
                    frCustomFieldItem.setObjectId(customFieldItems.get(i).getObjectId());
                    frCustomFieldItem.setDataType(customFieldItems.get(i).getDataType());
                    frCustomFieldItem.setColumnCode(customFieldItems.get(i).getColumnCode());
                    frCustomFieldItem.setCustomFieldSettingID(customFieldItems.get(i).getCustomFieldSettingID());
                    frCustomFieldItem.setPredefinedValues(customFieldItems.get(i).getPredefinedValues());
                    frCustomFieldItem.setFieldStringValue(customFieldBoxesFR[i].getSelectedItem().getId().toString());
                    frCustomFieldItem.setLocaleCode("fr");
                    customFieldItemsForSave.add(frCustomFieldItem);
                }
            }
            return customFieldItemsForSave;
        }

    }*/

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[1];
        columns[0] = new DynamicTableColumn(accountingStrings.warehouse(), WAREHOUSE, 160);
        return columns;
    }

    private DynamicTableColumn[] getPriceLevelColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[1];
        columns[0] = new DynamicTableColumn(wfmStrings.priceLevel(), CustomFormConstants.PRICE_LEVEL, 160);
        return columns;
    }

    private Widget[] getWidgets(SelectItem warehouse) {
        DataListBox warehouseListBox = new DataListBox();
        warehouseListBox.addListItem(warehouse);
        warehouseListBox.setSelected(warehouse);
        warehouseListBox.addStyleName(DEFAULT_WIDTH);
        return new Widget[]{warehouseListBox};
    }
}
