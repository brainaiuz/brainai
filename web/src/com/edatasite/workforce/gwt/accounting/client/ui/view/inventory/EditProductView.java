package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.PriceLevelLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CurrencyLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.DiscountViewPopup;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPictureForm;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.TaxQuickAddForm;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.ProductCategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldBuilder;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.ProductNumbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationMultiLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartMeasurementsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductSerialsImportPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ProductSerialsPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ASSEMBLY_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.CURRENCY_COLUMN;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.INVENTORY_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.NON_INVENTORY_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.OTHER_CHARGE;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT_DISCOUNT_TYPES;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT_KIT;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.RENTAL_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.RENT_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.SENT_TO_TEXTILE_FINDS;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.SERVICE;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.UNIT_PRICE_COLUMN;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;
import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.FROM_PURCHASE_INVOICE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.INPUT_PRICE_LEVEL;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_PRICE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_PRICE;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 12, 2010
 * Time: 10:02:57 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"ALL"})
public class EditProductView extends CustomForm2 implements Colapse, Constants, FeatureConstants {

    private static final ProductServiceAsync productService = ProductService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    /**
     * Financial fields
     */
    public TextBox txtPurchasePrice;
    SelectItem[] PRODUCT_TYPES = new SelectItem[]{
            new SelectItem(INVENTORY_ITEM, Property.get(Constants.INVENTORY_ITEMS, wfmStrings.inventoryItem())),
            new SelectItem(NON_INVENTORY_ITEM, Property.get(Constants.INVENTORY_ITEMS, wfmStrings.nonInventoryItem(), wfmStrings.inventoryItem())),
            new SelectItem(SERVICE, wfmStrings.service()),
            new SelectItem(ASSEMBLY_ITEM, Property.get(Constants.ASSEMBLY_PRODUCTS, wfmStrings.assemblyItem())),
            new SelectItem(OTHER_CHARGE, wfmStrings.otherCharge()),
            new SelectItem(PRODUCT_KIT, property.getSingular(wfmStrings.product()) + " " + wfmStrings.group())
            //new SelectItem(RENTAL_ITEM, RENTAL_ITEM_STR)
    };
    private boolean isMultiWarehouseEnabled = Utils.isMultiWarehouseEnabled();
    /**
     * Main fields
     */
    private ProductNameWidget productNameWidget;
    private ProductNumbering productNumberWidget;
    private DataListBox dwProductType;
    private TextArea txtProductDescription;
    private ReferenceLookUp statusListBox;
    private SmartProductLookUp rentItemLookUp;

    private KpiSwitcher activeProductCheckBox;
    private TextBox txtSalesPrice;
    private AccountsLookUp salesAccountLookUp;
    private AccountsLookUp purchaseAccountLookUp;
    private KpiSwitcher purchasedFromSupplierCheckBox;
    private KpiSwitcher soldToCustomerCheckBox;
    private KpiSwitcher enableITCheckBox;
    private FormGroup enableITField;
    private KpiSwitcher sentToTextileFindsCheckBox;
    /**
     * Main parameters
     */
    private DataListBox dwCategory;                  // eski clientlar
    private ProductCategoryLookUp dwCategoryLookUp;  // TextileFinds
    private DataListBox dwBrand;
    private SmartTaxRateLookUp taxLookUp;
    private SmartTaxRateLookUp doubleTaxLookUp;
    private MaterialComboBox<DiscountItem> discountListBox;
    /**
     * Inventory parameters
     */
    private ProductWarehouseView productWarehouseView;
    private DynamicTable priceLevelTable;
    /**
     * Multi Price
     */
    private MultiTableNewUI tblMultiSalesPrice;
    private GColumn multiSalesPricePanel;
    private MultiTableNewUI tblMultiCostPrice;
    private GColumn multiCostPricePanel;
    private FlexPanel multiPricePanel;
    /**
     * Assembly Item fields
     */
    private ProductAssemblyView productAssemblyView;
    /**
     * Product Kit fields
     */
    private ProductKitView productKitView;
    /**
     * More information
     */
    private SmartMeasurementsLookUp dwUnitMeasurement;
    private TextBox txtSkuNumber;
    private TextBox txtUpcNumber;
    private TextBox txtManufacturer;
    private TextBox txtPartNumber;
    private TextBox discountAmount;
    private DataListBox discountType;
    private LocationMultiLookUp location;

    private MultiTableNewUI multiSupplierTable;
    //    private KpiSwitcher showOnOpportunityCheckBox;
    private BarcodeGenerator barcodeGenerator;
    /**
     * Attachment fields
     */
    private ProductPictureForm fileUploadForm;
    private GeneralFileUpload fileUploadPanel;
    /**
     * Custom fields
     */
    private CustomFieldBuilder categoryCustomFieldBuilder;
    /**
     * Buttons
     */
    private MaterialSplitButton splitButton;
    private HashMap<Integer, BigDecimal> assemblyItems = new HashMap<Integer, BigDecimal>();
    private HashMap<Integer, List<MultiPriceItem>> multiPriceItems = new HashMap<Integer, List<MultiPriceItem>>();
    private Integer objectID;
    private Integer categoryID;
    private Integer storefrontID;
    private Integer externalId;
    private SplitButton assignSerialsButton;
    private ProductSerialsPopup productSerialsPopup;
    private ProductSerialsImportPopup productSerialsImportPopup;
    //Number Data
    private NumberData numberData;
    private Integer productType = SERVICE;
    private boolean newItem;
    private boolean checkNumber;
    private NewProduct product;
    private Integer existingProductType;
    private boolean fromInventory;
    private boolean fromAssembly;
    private boolean isDoubleTaxEnabled;
    private boolean copied;
    private String type;

    private double unFormattedSalesPrice;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public EditProductView() {
        super("productadd");
        setDescription(property.getSingular(accountingStrings.addProduct(), wfmStrings.product()));
    }

    public EditProductView(Integer objectID) {
        super("productadd");
        setDescription(property.getSingular(accountingStrings.editProduct(), wfmStrings.product()));
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ProductServiceView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
            }
        });
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void registerFields() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ProductServiceView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                if (newItem) {
                    objectID = null;
                }
                if (fromInventory) {
                    productType = INVENTORY_ITEM;
                }
                if (fromAssembly) {
                    productType = ASSEMBLY_ITEM;
                }
                drawForm();

                newItem = false;
                existingProductType = null;

                show();
                ArrayList<BigDecimal> assemblyItemValues = new ArrayList();
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSAMBLY_ITEM_TOTAL_CHANGE, EditProductView.this, (sender, args) -> {
                    if (objectID != null) {
                        BigDecimal oldValue = ((BigDecimal[]) args)[0];
                        assemblyItemValues.add(oldValue);
                        if ((assemblyItemValues.get(0).compareTo(((BigDecimal[]) args)[0])) != 0) {
                            txtPurchasePrice.setValue(utils.formatPrice(((BigDecimal[]) args)[0]));
                            txtSalesPrice.setValue(utils.formatPrice(((BigDecimal[]) args)[1]));
                        }
                    } else {
                        txtPurchasePrice.setValue(utils.formatPrice(((BigDecimal[]) args)[0]));
                        txtSalesPrice.setValue(utils.formatPrice(((BigDecimal[]) args)[1]));
                    }
                });
            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        productService.getProductEditData(externalId != null ? externalId : objectID, externalId != null, new AbstractAsyncCallback<NewProduct>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;

                if (product.getCategoryID() != null) {
                    drawCategoryCustomFieldForm(product.getCategoryCustomFieldItems());
                }
                isDoubleTaxEnabled = result.isDoubleTaxEnabled();
                getPriceLevelsByProduct();
                if (product != null) {
                    if (product.getType() != null) {
                        productType = product.getType();
                        dwProductType.setSelected(existingProductType = product.getType());
                    }
                    if (product.getDiscountType() != null) {
                        discountType.setSelected(product.getDiscountType());
                    }
                    statusListBox.setSelected(product.getRentStatus());
                    rentItemLookUp.setSelected(product.getRentItem());
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

                    txtProductDescription.setText(product.getDescription());
                    txtSkuNumber.setValue(product.getInternalSKUNumber());
                    discountAmount.setValue(product.getDiscountAmount()!=null? utils.formatUnitPrice(product.getDiscountAmount()): "");
                    txtManufacturer.setValue(product.getManufacturer());
                    txtPartNumber.setValue(product.getPartNumber());
                    barcodeGenerator.setData(product.getBarCodeText(), product.getQRCodeSizeID());
                    txtUpcNumber.setValue(product.getUpcNumber());
                    txtPurchasePrice.setValue(product.getUnitPrice() != null ? utils.formatUnitPrice(product.getUnitPrice()) : "");
                    txtSalesPrice.setValue(product.getSellingPrice() != null ? utils.formatUnitPrice(product.getSellingPrice()) : "");
                    activeProductCheckBox.setValue(product.isActive());
                    sentToTextileFindsCheckBox.setValue(product.isSentToTextileFinds());
                    updateSalesPrice(product.getSellingPrice());

                    //init/fill selected product kit items
                    if (PRODUCT_KIT.equals(productType)) {
                        productKitView.setVisible(true);
                        if (product.getProductKitItems() != null && product.getProductKitItems().length > 0) {
                            productKitView.setProductKitData(product);
                        }
                    }
                    if (ASSEMBLY_ITEM.equals(productType)) {
                        productAssemblyView.setAssemblyItems(product);
                    }
//                    if (product.isProductSerialsEnabled()) {
                        assignSerialsButton.setVisible(true);
//                    }
                    tblMultiSalesPrice.removeAllRows();
                    tblMultiCostPrice.removeAllRows();
                    boolean receivable = false;
                    boolean payable = false;
                    if (Utils.isMultipleSalesPriceEnable() && product.getMultiPrices() != null && product.getMultiPrices().size() > 0) {
                        for (MultiPriceItem multiPriceItem : product.getMultiPrices()) {
                            if (RECEIVABLE.equals(multiPriceItem.getType())) {
                                receivable = true;
                                tblMultiSalesPrice.addWidgets(getWidgetsForMultiPrice(multiPriceItem));
                            } else if (PAYABLE.equals(multiPriceItem.getType())) {
                                payable = true;
                                tblMultiCostPrice.addWidgets(getWidgetsForMultiPrice(multiPriceItem));
                            }
                        }
                    } else {
                        tblMultiSalesPrice.addWidgets(getWidgetsForMultiPrice(null));
                        tblMultiCostPrice.addWidgets(getWidgetsForMultiPrice(null));
                    }

                    if (objectID != null && !receivable && Utils.isMultipleSalesPriceEnable()) {
                        tblMultiSalesPrice.addWidgets(getWidgetsForMultiPrice(null));
                    }
                    if (objectID != null && !payable && (Utils.isMultipleSalesPriceEnable() && isMultiCostPriceAllowed())) {
                        tblMultiCostPrice.addWidgets(getWidgetsForMultiPrice(null));
                    }

                    enableITField.setVisible(!product.enableCompanyIT());

                    productWarehouseView.setWarehouseData(product);


                    if (Constants.PURCHASE_ORDER.equalsIgnoreCase(type) || Constants.PURCHASE_INVOICE.equalsIgnoreCase(type)) {
                        purchasedFromSupplierCheckBox.setValue(true);
                    } else {
                        purchasedFromSupplierCheckBox.setValue(product.isPurchasedFromSupplier() != null ? product.isPurchasedFromSupplier() : Boolean.TRUE);
                    }
                    soldToCustomerCheckBox.setValue(product.isSoldToCustomer());

                    enableITCheckBox.setValue(product.enableIT());
//                    showOnOpportunityCheckBox.setValue(product.isShowOnOpportunity());

                    if (product.getUnitMeasurement() != null) {
                        dwUnitMeasurement.setSelected(product.getUnitMeasurement());
                    }
                    /*dwCategory.setItems(product.getProductCategories());

                    if (product.getCategoryID() != null) {
                        dwCategory.setSelected(product.getCategoryID());
                    }*/

                    if (product.getUnitMeasurementID() != null) {
                        dwUnitMeasurement.setSelected(product.getUnitMeasurementID());
                    }
                    /*if (product.getCategoryID() != null) {
                        dwCategory.setSelected(product.getCategoryID());
                    }*/
                    if (product.getTaxItem() != null) {
                        taxLookUp.setSelected(new SelectItem(product.getTaxItem().getId(), product.getTaxItem().getName()));
                    }
                    if (product.getDoubleTaxItem() != null) {
                        doubleTaxLookUp.setSelected(new SelectItem(product.getDoubleTaxItem().getId(), product.getDoubleTaxItem().getName()));
                    }
                    //accounting fill
                    if (product.getAccountId() != null) {
                        salesAccountLookUp.addAccountItem(product.getAccountItem());
                    } else if (product.getDefaultReceivableAccount() != null) {
                        salesAccountLookUp.addAccountItem(product.getDefaultReceivableAccount());
                    }

                    //set enable false warehouse widget for editing form
                    if (objectID != null) {
                        productWarehouseView.enableWarehouseWidgetsForEditForm(!product.hasUsed(), product);
                    }
                    if (product.getSuppliers() != null && product.getSuppliers().length > 0) {
                        multiSupplierTable.removeAllRows();
                        for (SelectItem supplier : product.getSuppliers()) {
                            multiSupplierTable.addWidgets(getSupplierTableWidget(supplier));
                        }
                    }
                    location.setSelectedItems(product.getLocations());
                    productAssemblyView.setLocationIds(location.getSelectedItemIds());
                    getCustomFieldUtil().fillCustomFieldsWithData(product.getProductCustomFieldItems());
                    //set selected category of product
                    if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                        if (product.getCategoryID() != null) {
                            AccountingService.App.get().getProductCategory(product.getCategoryID(), new AbstractAsyncCallback<ProductCategoryItem>() {
                                public void failure(Throwable caught) {}
                                public void success(ProductCategoryItem result) {
                                    addCategoryItem(new SelectItem(result.getId(), result.getName()));
                                    // result.getName() endi "A > B > C" formatda (buildHierarchyName qo'shilgandan keyin)
                                }
                            });
                        } else if (categoryID != null) {
                            AccountingService.App.get().getProductCategory(categoryID, new AbstractAsyncCallback<ProductCategoryItem>() {
                                public void failure(Throwable caught) {}
                                public void success(ProductCategoryItem result) {
                                    addCategoryItem(new SelectItem(result.getId(), result.getName()));
                                }
                            });
                        }
                    } else {
                    AccountingService.App.get().getCategoriesAsTreeSelectItem(new AbstractAsyncCallback<TreeSelectItem[]>() {
                        public void failure(Throwable caught) {
                            GWT.log(caught.getMessage());
                        }

                        public void success(TreeSelectItem[] result) {
                            dwCategory.setItems(result);
                            if (categoryID != null) {
                                dwCategory.setSelected(categoryID);
                            }
                            if (product.getCategoryID() != null) {
                                dwCategory.setSelected(product.getCategoryID());
                            }
                        }
                    });
                    }

                    DiscountService.App.get().getDiscountListAsSelectItem(new AbstractAsyncCallback<DiscountItem[]>() {
                        public void failure(Throwable caught) {
                            GWT.log(caught.getMessage());
                        }

                        public void success(DiscountItem[] allDiscounts) {
                            initDiscountListWidget(allDiscounts);
                            if (objectID == null && formPropertyMap != null && formPropertyMap.get(DISCOUNT_PANEL) != null && formPropertyMap.get(DISCOUNT_PANEL).getDefaultValue() != null && discountListBox != null) {
                                List<DiscountItem> items = new ArrayList<>();
                                items.add(new DiscountItem(formPropertyMap.get(DISCOUNT_PANEL).getSelectedId(), formPropertyMap.get(DISCOUNT_PANEL).getDefaultValue()));
                                discountListBox.setValues(items);
                            }
                        }
                    });

                    AccountingService.App.get().getBrandsAsSelectItem(new AbstractAsyncCallback<SelectItem[]>() {
                        public void failure(Throwable caught) {
                            GWT.log(caught.getMessage());
                        }

                        public void success(SelectItem[] result) {
                            dwBrand.setItems(result);
                            if (product.getBrandID() != null) {
                                dwBrand.setSelected(product.getBrandID());
                            }
                        }
                    });

                    onChangeProductType();
                    //fill cogs account
                    if (product.getCogsAccountID() != null) {
                        purchaseAccountLookUp.addAccountItem(product.getCogsAccount());
                    } else if (product.getDefaultPayableAccount() != null) {
                        purchaseAccountLookUp.setSelected(product.getDefaultPayableAccount());
                    }
                }

                if (objectID == null) {
                    setDefaultValues();
                    setDefaultValuesByFormProperty();
                }
            }

        });
    }

    private void getPriceLevelsByProduct() {
        Integer productId = externalId != null ? externalId : objectID;
        priceLevelTable.removeItems();
        if (productId != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setProductId(productId);
            PriceLevelService.App.get().getPriceLevelPPItems(productId, new AsyncCallback<HashMap<PriceLevelItem, PriceLevelPPItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(HashMap<PriceLevelItem, PriceLevelPPItem> items) {
                    if (items != null && items.size() > 0) {
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

        final TextBox txtCustomPrice = new TextBox(true);
        txtCustomPrice.setWidth("130px");
        txtCustomPrice.setTextAlignment(TextBox.ALIGN_RIGHT);

        final Widget[] widgets = new Widget[priceLevelTable.getCellCount(0) - 1];

        PriceLevelLookUp pricelevelLookUp = new PriceLevelLookUp();
        pricelevelLookUp.setAutocompleteOff();
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

    private PriceLevelPPItem[] getPriceLevelPPItems() {
        PriceLevelPPItem[] priceLevelPPItems = new PriceLevelPPItem[priceLevelTable.getRowNumber()];

        for (int i = 0; i < priceLevelTable.getRowNumber(); i++) {
            DynamicTableItem item = priceLevelTable.getItem(i);
            PriceLevelLookUp priceLevelLookUp = (PriceLevelLookUp) item.getColumnById("price_level");
            HorizontalPanel pnlCustomPrice = (HorizontalPanel) item.getColumnById("custom_price");
            TextBox txtCustomPrice = (TextBox) pnlCustomPrice.getWidget(0);

            priceLevelPPItems[i] = new PriceLevelPPItem();
            priceLevelPPItems[i].priceLevelID = priceLevelLookUp.getSelectedItemID();
            if (txtCustomPrice.getValue() != null && !txtCustomPrice.getValue().isEmpty()) {
                priceLevelPPItems[i].setCustomPrice(Utils.universalParse(Utils.getNumberFormat(), txtCustomPrice.getValue()));
            }
        }
        return priceLevelPPItems;
    }

    private void drawForm() {
        drawMainSection();
        drawFinanceSection();
        drawParametersSection();
        drawMoreOptionsSection();
        drawProductKitSection();
        drawMultiCurrencyPriceSection();
        drawAssemblyItemRelatedForm();
        drawAttachmentsSection();
        drawInventoryWarehouseSection();
        initSerialsButton();

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);
    }

    private void initSerialsButton() {
        assignSerialsButton = new SplitButton(accountingStrings.assignSerials(), 80, BTN_DEFAULT_OUTLINE, true);
        assignSerialsButton.setVisible(false);
        List<SplitButtonItem> serialButtonItems = new ArrayList<SplitButtonItem>(2);
        serialButtonItems.add(new SplitButtonItem("ASSIGN_SERIALS", accountingStrings.assignSerials(), new Command() {
            @Override
            public void execute() {
                NewProduct newProduct = productWarehouseView.getWarehouseData(product);
                if (product.getObjectId() == null) {
                    product.setTotalQtyOnHand(newProduct.getTotalQtyOnHand());
                    productSerialsPopup = new ProductSerialsPopup(product, PRODUCT_ADD);
                } else {
                    ArrayList<ProductSerialItem> productSerialItems = new ArrayList<ProductSerialItem>();
                    if (product.getProductSerialItems() != null && !product.getProductSerialItems().isEmpty()) {
                        productSerialItems.addAll(product.getProductSerialItems());
                    }
                    for (int i = 0; i < newProduct.getTotalQtyOnHand().intValue() - productSerialItems.size(); i++) {
                        productSerialItems.add(new ProductSerialItem());
                    }
                    product.setProductSerialItems(productSerialItems);
                    productSerialsPopup = new ProductSerialsPopup(product, PRODUCT_EDIT);
                }
            }
        }, true));
        serialButtonItems.add(new SplitButtonItem("IMPORT_EXCEL", "Import", new Command() {
            @Override
            public void execute() {
                productSerialsImportPopup = new ProductSerialsImportPopup();
            }
        }, false));
        assignSerialsButton.addItemList(serialButtonItems);
    }

    private void drawMainSection() {
        dwProductType = new DataListBox();
        dwProductType.ensureDebugId(PRODUCT + "dwProductType");
        dwProductType.addValueChangeHandler(event -> {
            onChangeProductType();
        });
        fillProductTypeList();

        productNumberWidget = new ProductNumbering();

        productNameWidget = new ProductNameWidget();
        productNameWidget.ensureDebugId("productName");

        txtProductDescription = new TextArea();

        activeProductCheckBox = new KpiSwitcher();
        sentToTextileFindsCheckBox = new KpiSwitcher();

        statusListBox = new ReferenceLookUp(RENT_ITEM_STATUS);
        statusListBox.ensureDebugId(PRODUCT + "statusListBox");
        statusListBox.setEnabled(true);

        rentItemLookUp = new SmartProductLookUp(Constants.RENTAL_PRODUCTS);


        if (!fromInventory && !fromAssembly) {
            if (formPropertyMap != null && formPropertyMap.get(TYPE) != null) {
                addField(TYPE, dwProductType, getTitle(formPropertyMap.get(TYPE).isChanged() ? formPropertyMap.get(TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(TYPE).isRequired()), false,
                        formPropertyMap.get(TYPE).isInformation());
                if (formPropertyMap.get(TYPE).isInformation()) {
                    new KpiToolTip(dwProductType, formPropertyMap.get(TYPE).getInformationText());
                }

                dwProductType.setEnabled(!formPropertyMap.get(TYPE).isDisabled());
            } else {
                addField(TYPE, dwProductType, wfmStrings.type());
            }
        }

        addTitleField(CustomFormConstants.PRODUCT_INFORMATION, property.getSingular(accountingStrings.productInformation(), wfmStrings.product()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(NAME, productNameWidget, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()) {
                new KpiToolTip(productNameWidget, formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
            }

            productNameWidget.setEnabled(!formPropertyMap.get(NAME).isDisabled());
        } else {
            addField(NAME, productNameWidget, wfmStrings.name());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : property.getShortForNumber(wfmStrings.number()), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(productNumberWidget, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }

            productNumberWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, productNumberWidget, property.getShortForNumber(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null) {
            addField(DESCRIPTION, txtProductDescription, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(DESCRIPTION).isRequired()), false,
                    formPropertyMap.get(DESCRIPTION).isInformation());
            if (formPropertyMap.get(DESCRIPTION).isInformation()) {
                new KpiToolTip(txtProductDescription, formPropertyMap.get(DESCRIPTION).getInformationText());
            }

            txtProductDescription.setEnabled(!formPropertyMap.get(DESCRIPTION).isDisabled());
        } else {
            addField(DESCRIPTION, txtProductDescription, wfmStrings.description());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACTIVE) != null) {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.ACTIVE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACTIVE).getTitle() : wfmStrings.active(), formPropertyMap.get(CustomFormConstants.ACTIVE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.ACTIVE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.ACTIVE).isInformation()) {
                new KpiToolTip(activeProductCheckBox, formPropertyMap.get(CustomFormConstants.ACTIVE).getInformationText());
            }

            activeProductCheckBox.setEnabled(!formPropertyMap.get(CustomFormConstants.ACTIVE).isDisabled());
        } else {
            addField(CustomFormConstants.ACTIVE, activeProductCheckBox, wfmStrings.active());
        }

        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            if (formPropertyMap != null && formPropertyMap.get(SENT_TO_TEXTILE_FINDS) != null) {
                addField(SENT_TO_TEXTILE_FINDS, sentToTextileFindsCheckBox, getTitle(formPropertyMap.get(SENT_TO_TEXTILE_FINDS).isChanged() ? formPropertyMap.get(SENT_TO_TEXTILE_FINDS).getTitle() : wfmStrings.sentToTextileFinds(), formPropertyMap.get(SENT_TO_TEXTILE_FINDS).isRequired()), false,
                        formPropertyMap.get(SENT_TO_TEXTILE_FINDS).isInformation());
                if (formPropertyMap.get(SENT_TO_TEXTILE_FINDS).isInformation()) {
                    new KpiToolTip(sentToTextileFindsCheckBox, formPropertyMap.get(SENT_TO_TEXTILE_FINDS).getInformationText());
                }
                sentToTextileFindsCheckBox.setEnabled(!formPropertyMap.get(SENT_TO_TEXTILE_FINDS).isDisabled());
            } else {
                addField(SENT_TO_TEXTILE_FINDS, sentToTextileFindsCheckBox, wfmStrings.sentToTextileFinds());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(STATUS, statusListBox, getTitle(formPropertyMap.get(STATUS).isChanged() ? formPropertyMap.get(STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(STATUS).isRequired()));
            statusListBox.setEnabled(!formPropertyMap.get(STATUS).isDisabled());
        } else {
            addField(STATUS, statusListBox, wfmStrings.status());
        }


        if (formPropertyMap != null && formPropertyMap.get(RENT_ITEM) != null) {
            addField(RENT_ITEM, rentItemLookUp, getTitle(formPropertyMap.get(RENT_ITEM).isChanged() ? formPropertyMap.get(RENT_ITEM).getTitle() : accountingStrings.rentalItem(), formPropertyMap.get(RENT_ITEM).isRequired()));
            rentItemLookUp.setEnabled(!formPropertyMap.get(RENT_ITEM).isDisabled());
        } else {
            addField(RENT_ITEM, rentItemLookUp, accountingStrings.rentalItem());
        }
    }

    private void drawFinanceSection() {
        txtPurchasePrice = new TextBox(true);
        txtPurchasePrice.ensureDebugId(PRODUCT + "txtPurchasePrice");
        Validation.checkToFocusTextBox(txtPurchasePrice, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(txtPurchasePrice, AccountingUtils.customUnitPriceScale);
        txtPurchasePrice.setMaxLength(20);
        txtPurchasePrice.addChangeHandler(c -> calculateTotal());

        txtSalesPrice = new TextBox(true);
        txtSalesPrice.ensureDebugId(PRODUCT + "txtSalesPrice");
//        Validation.checkToFocusTextBox(txtSalesPrice, AccountingUtils.getUnitPriceZero());
        Validation.addNumericKeyboardListener(txtSalesPrice, AccountingUtils.customUnitPriceScale);
        txtSalesPrice.setMaxLength(20);
        txtSalesPrice.addChangeHandler(c -> {
            salesPriceChange();
        });

        discountAmount = new TextBox(true);
        discountAmount.ensureDebugId(PRODUCT + "discountAmount");
        discountAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        discountType = new DataListBox();
        discountType.ensureDebugId(PRODUCT + "discountType");
        discountType.addValueChangeHandler(event -> {
            if (discountType.isSomethingSelected()) {
                discountAmount.setEnabled(true);
            } else {
                discountAmount.setText("");
                discountAmount.setEnabled(false);
            }
        });
        fillDiscountTypeList();

        discountAmount.addKeyUpHandler(event -> {
            if (discountType.isSomethingSelected()) {
                String val = discountAmount.getText();
                discountAmount.setText(val.replaceAll("[^0-9.]", ""));
                int oneMorePoint = val.indexOf('.', val.indexOf('.') + 1);
                double amount = Double.parseDouble(discountAmount.getValue());
                if (discountType.getSelectedId().equals(FIXED_AMOUNT)) {
                    if (amount < unFormattedSalesPrice) {
                        if (oneMorePoint != -1) {
                            discountAmount.setText(val.substring(0, oneMorePoint));
                        }
                    } else {
                        discountAmount.setText("");
                    }
                } else {
                    if (Double.parseDouble(discountAmount.getValue()) <= 100) {
                        if (oneMorePoint != -1) {
                            discountAmount.setText(val.substring(0, oneMorePoint));
                        }
                    } else {
                        discountAmount.setText("");
                    }
                }
            } else {
                Info.warn(wfmStrings.pleaseSelect() + " " + accountingStrings.maximumDiscount());
            }
        });

        location = new LocationMultiLookUp();
        location.ensureDebugId(PRODUCT + "location");

        if (Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_MATERIALS_BY_LOCATION_IN_ADD_VIEW)) {
            if (objectID == null) {
                CommonService.App.get().getUserLocation(new AbstractAsyncCallback<SelectItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                    }

                    @Override
                    public void success(SelectItem item) {
                        if (item != null) {
                            location.setSelectedItems(item);
                            productAssemblyView.setLocationIds(location.getSelectedItemIds());
                        }
                    }
                });
            }

            location.setRemoveActionCommand(() -> {
                if (location.getSelectedItemIds().isEmpty()) {
                    productAssemblyView.clearData();
                    productAssemblyView.setLocationIds(new ArrayList<>());
                    location.clearOracleItems();
                }
                productAssemblyView.reRender(location.getSelectedItemIds());
            });
            location.getSuggestBox().addSelectionHandler(selectionEvent -> {
                productAssemblyView.setLocationIds(location.getSelectedItemIds());
                if (location.getSelectedItemIds().size() == 1) {
                    productAssemblyView.clearData();
                }
            });
        }

        txtPurchasePrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        txtSalesPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        salesAccountLookUp = new AccountsLookUp(REVENUE, LIABILITIES);
        salesAccountLookUp.setAutocompleteOff();
        salesAccountLookUp.ensureDebugId(PRODUCT + "salesAccountLookUp");

        purchaseAccountLookUp = new AccountsLookUp(EXPENSES);
        purchaseAccountLookUp.setAutocompleteOff();
        purchaseAccountLookUp.ensureDebugId(PRODUCT + "purchaseAccountLookUp");

        purchasedFromSupplierCheckBox = new KpiSwitcher();
        purchasedFromSupplierCheckBox.ensureDebugId("isPurchasedFrom-supplier-checkBox");
        purchasedFromSupplierCheckBox.setValue(true);
        purchasedFromSupplierCheckBox.addValueChangeHandler((e) -> {
            if (productType.equals(SERVICE) || productType.equals(NON_INVENTORY_ITEM) || productType.equals(OTHER_CHARGE) || productType.equals(PRODUCT_KIT)) {
                if (Utils.isMultipleSalesPriceEnable()) {
                    multiCostPricePanel.setVisible(purchasedFromSupplierCheckBox.getValue());
                }
            }
        });

        soldToCustomerCheckBox = new KpiSwitcher();
        soldToCustomerCheckBox.setValue(true);

        enableITCheckBox = new KpiSwitcher();
        enableITField = new FormGroup("&nbsp;", enableITCheckBox);

        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null) {
            addField(PURCHASE_PRICE, txtPurchasePrice, getTitle(formPropertyMap.get(PURCHASE_PRICE).isChanged() ? formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice(), formPropertyMap.get(PURCHASE_PRICE).isRequired()), false,
                    formPropertyMap.get(PURCHASE_PRICE).isInformation());
            if (formPropertyMap.get(PURCHASE_PRICE).isInformation()) {
                new KpiToolTip(txtPurchasePrice, formPropertyMap.get(PURCHASE_PRICE).getInformationText());
            }

            txtPurchasePrice.setEnabled(!formPropertyMap.get(PURCHASE_PRICE).isDisabled());
        } else {
            addField(PURCHASE_PRICE, txtPurchasePrice, wfmStrings.purchasePrice());
        }
        if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null) {
            addField(SALES_PRICE, txtSalesPrice, getTitle(formPropertyMap.get(SALES_PRICE).isChanged() ? formPropertyMap.get(SALES_PRICE).getTitle() : wfmStrings.sellingPrice(), formPropertyMap.get(SALES_PRICE).isRequired()), false,
                    formPropertyMap.get(SALES_PRICE).isInformation());
            if (formPropertyMap.get(SALES_PRICE).isInformation()) {
                new KpiToolTip(txtSalesPrice, formPropertyMap.get(SALES_PRICE).getInformationText());
            }

            txtSalesPrice.setEnabled(!formPropertyMap.get(SALES_PRICE).isDisabled());
        } else {
            addField(SALES_PRICE, txtSalesPrice, wfmStrings.sellingPrice());
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null) {
            addField(PURCHASE_ACCOUNT, purchaseAccountLookUp, getTitle(formPropertyMap.get(PURCHASE_ACCOUNT).isChanged() ? formPropertyMap.get(PURCHASE_ACCOUNT).getTitle() : wfmStrings.purchaseAccount(), formPropertyMap.get(PURCHASE_ACCOUNT).isRequired()), false,
                    formPropertyMap.get(PURCHASE_ACCOUNT).isInformation());
            if (formPropertyMap.get(PURCHASE_ACCOUNT).isInformation()) {
                new KpiToolTip(purchaseAccountLookUp, formPropertyMap.get(PURCHASE_ACCOUNT).getInformationText());
            }

            purchaseAccountLookUp.setEnabled(!formPropertyMap.get(PURCHASE_ACCOUNT).isDisabled());
        } else {
            addField(PURCHASE_ACCOUNT, purchaseAccountLookUp, wfmStrings.purchaseAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null) {
            addField(SALES_ACCOUNT, salesAccountLookUp, getTitle(formPropertyMap.get(SALES_ACCOUNT).isChanged() ? formPropertyMap.get(SALES_ACCOUNT).getTitle() : wfmStrings.salesAccount(), formPropertyMap.get(SALES_ACCOUNT).isRequired()), false,
                    formPropertyMap.get(SALES_ACCOUNT).isInformation());
            if (formPropertyMap.get(SALES_ACCOUNT).isInformation()) {
                new KpiToolTip(salesAccountLookUp, formPropertyMap.get(SALES_ACCOUNT).getInformationText());
            }

            salesAccountLookUp.setEnabled(!formPropertyMap.get(SALES_ACCOUNT).isDisabled());
        } else {
            addField(SALES_ACCOUNT, salesAccountLookUp, wfmStrings.salesAccount());
        }

        if (formPropertyMap != null && formPropertyMap.get(FROM_PURCHASE_INVOICE) != null) {
            purchasedFromSupplierCheckBox.setOffLabel(formPropertyMap.get(FROM_PURCHASE_INVOICE).isChanged() ? formPropertyMap.get(FROM_PURCHASE_INVOICE).getTitle() : wfmStrings.purchasedFromSupplier());
            addField(FROM_PURCHASE_INVOICE, purchasedFromSupplierCheckBox, null, false, formPropertyMap.get(FROM_PURCHASE_INVOICE).isInformation());
            if (formPropertyMap.get(FROM_PURCHASE_INVOICE).isInformation()) {
                new KpiToolTip(purchasedFromSupplierCheckBox, formPropertyMap.get(FROM_PURCHASE_INVOICE).getInformationText());
            }

            purchasedFromSupplierCheckBox.setEnabled(!formPropertyMap.get(SALES_ACCOUNT).isDisabled());
        } else {
            purchasedFromSupplierCheckBox.setOffLabel(wfmStrings.purchasedFromSupplier());
            addField(FROM_PURCHASE_INVOICE, purchasedFromSupplierCheckBox, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(SOLD_TO_CUSTOMERS) != null) {
            soldToCustomerCheckBox.setOffLabel(formPropertyMap.get(SOLD_TO_CUSTOMERS).isChanged() ? formPropertyMap.get(SOLD_TO_CUSTOMERS).getTitle() : accountingStrings.soldToCustomer());
            addField(SOLD_TO_CUSTOMERS, soldToCustomerCheckBox, null);
            soldToCustomerCheckBox.setEnabled(!formPropertyMap.get(SOLD_TO_CUSTOMERS).isDisabled());
        } else {
            soldToCustomerCheckBox.setOffLabel(accountingStrings.soldToCustomer());
            addField(SOLD_TO_CUSTOMERS, soldToCustomerCheckBox, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(ENABLE_IT) != null) {
            enableITCheckBox.setOffLabel(formPropertyMap.get(ENABLE_IT).isChanged() ? formPropertyMap.get(ENABLE_IT).getTitle() : accountingStrings.enableInventoryTransaction());
            addField(ENABLE_IT, enableITCheckBox, null);
            enableITCheckBox.setEnabled(!formPropertyMap.get(ENABLE_IT).isDisabled());
        } else {
            enableITCheckBox.setOffLabel(accountingStrings.enableInventoryTransaction());
            addField(ENABLE_IT, enableITField);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DISCOUNT_AMOUNT) != null) {
            addField(CustomFormConstants.DISCOUNT_AMOUNT, discountAmount, getTitle(formPropertyMap.get(CustomFormConstants.DISCOUNT_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.DISCOUNT_AMOUNT).getTitle() : accountingStrings.discountAmount(), formPropertyMap.get(DISCOUNT_AMOUNT).isRequired()));
        } else {
            addField(CustomFormConstants.DISCOUNT_AMOUNT, discountAmount, accountingStrings.discountAmount());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DISCOUNT_TYPE) != null) {
            addField(CustomFormConstants.DISCOUNT_TYPE, discountType, getTitle(formPropertyMap.get(CustomFormConstants.DISCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.DISCOUNT_TYPE).getTitle() : accountingStrings.maximumDiscount(), formPropertyMap.get(DISCOUNT_TYPE).isRequired()));
        } else {
            addField(CustomFormConstants.DISCOUNT_TYPE, discountType, accountingStrings.maximumDiscount());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CANDIDATE.LOCATION) != null) {
            boolean required = false;
            if (Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_MATERIALS_BY_LOCATION_IN_ADD_VIEW)) {
                required = true;
            } else {
                formPropertyMap.get(CustomFormConstants.CANDIDATE.LOCATION).isRequired();
            }
            addField(CustomFormConstants.CANDIDATE.LOCATION, location, getTitle(formPropertyMap.get(CustomFormConstants.CANDIDATE.LOCATION).isChanged() ? formPropertyMap.get(CustomFormConstants.CANDIDATE.LOCATION).getTitle() : wfmStrings.location(), required));
        } else {
            addField(CustomFormConstants.CANDIDATE.LOCATION, location, getTitle(wfmStrings.location(), Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_MATERIALS_BY_LOCATION_IN_ADD_VIEW)));
        }
    }

    private void salesPriceChange() {
        if (product.getObjectId() == null || (product.getObjectId() != null && !productType.equals(existingProductType)) || !product.hasUsed()) {
            String uprice = (txtSalesPrice.getValue() != null && !txtSalesPrice.getValue().isEmpty()) ? utils.formatUnitPrice(new BigDecimal(txtSalesPrice.getValue())) : AccountingUtils.getUnitPriceZero();
            unFormattedSalesPrice = Double.parseDouble(txtSalesPrice.getValue());
            if (discountAmount != null && discountType != null && discountType.getSelectedId() != null) {
                if (discountType.getSelectedId().equals(FIXED_AMOUNT)) {
                    if (Double.parseDouble(discountAmount.getValue()) > unFormattedSalesPrice) {
                        discountAmount.setText("");
                    }
                }
            }
            txtSalesPrice.setText(uprice);
        }
    }

    private void drawParametersSection() {
        boolean[] isFirstSelect = {true};
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            dwCategoryLookUp = new ProductCategoryLookUp();
            dwCategoryLookUp.ensureDebugId(PRODUCT + "dwCategory");
            dwCategoryLookUp.getSuggestBox().addSelectionHandler(event -> {
                onChangeCategory(isFirstSelect);
            });
        } else {
            dwCategory = new DataListBox();
            dwCategory.ensureDebugId(PRODUCT + "dwCategory");
        }

        discountListBox = new MaterialComboBox<>();
        discountListBox.setMultiple(true);
        discountListBox.ensureDebugId(PRODUCT + "discountList");
        discountListBox.setCloseOnSelect(false);

        taxLookUp = new SmartTaxRateLookUp(RECEIVABLE, () -> new TaxQuickAddForm(o -> taxLookUp.addTaxItem((TaxItem) o)));
        taxLookUp.getSuggestBox().addSelectionHandler(s -> taxLookUp.islink());
        taxLookUp.setAutocompleteOff();
        taxLookUp.ensureDebugId(PRODUCT + "taxLookUp");

        priceLevelTable = new DynamicTable(getPriceLevelTableColumns());
        priceLevelTable.getElement().setAttribute("autocomplete", "off");
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

        doubleTaxLookUp = new SmartTaxRateLookUp(RECEIVABLE);

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
        if (!Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            lnkNewCategory.setClass("ficon--plus");
        }
        lnkNewCategory.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                new ProductCategoryViewPopup(new ExtendedCommand() {
                    public void execute(final Integer id) {
                        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                            AccountingService.App.get().getProductCategory(id, new AbstractAsyncCallback<ProductCategoryItem>() {
                                @Override
                                public void failure(Throwable caught) {
                                    GWT.log(caught.getMessage());
                                }

                                @Override
                                public void success(ProductCategoryItem result) {
                                    // hierarchy name serverdan qurilgan bo'ladi
                                    String hierarchyName = result.getParentCategoryName() != null && !result.getParentCategoryName().isEmpty()
                                            ? result.getParentCategoryName() + " > " + result.getName()
                                            : result.getName();

                                    clearCategory();
                                    addCategoryItem(new SelectItem(result.getId(), result.getName()));
                                }
                            });
                        } else {
                            AccountingService.App.get().getCategoriesAsTreeSelectItem(new AbstractAsyncCallback<TreeSelectItem[]>() {
                                public void failure(Throwable caught) {
                                    GWT.log(caught.getMessage());
                                }

                                public void success(TreeSelectItem[] items) {
                                    dwCategory.setItems(items);
                                     dwCategory.setSelected(id); // causes visual bug after creation, disabled the autoselect function until I find a solution
                                }
                            });
                        }

                    }
                }, storefrontID, false);
            }
        });
        AdvancedInputGroup categoryPanel = new AdvancedInputGroup(getCategoryWidget(), lnkNewCategory);

        MaterialLink lnkNewDiscount = new MaterialLink();
        lnkNewDiscount.setClass("ficon--plus");
        lnkNewDiscount.addClickHandler(c -> {
            new DiscountViewPopup(new ExtendedCommand() {
                public void execute(final Integer id) {
                    DiscountService.App.get().getDiscountData(id, new AbstractAsyncCallback<DiscountItem>() {
                        public void success(DiscountItem item) {
                            discountListBox.addItem(item.getName(), item);
                        }
                    });
                }
            });
        });
        AdvancedInputGroup discountPanel = new AdvancedInputGroup(discountListBox, lnkNewDiscount);

        addTitleField(PARAMETRS, wfmStrings.parameters());

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryPanel, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY).isRequired()), false,
                    formPropertyMap.get(CATEGORY).isInformation());
            if (formPropertyMap.get(CATEGORY).isInformation()) {
                new KpiToolTip(categoryPanel, formPropertyMap.get(CATEGORY).getInformationText());
            }

            setCategoryEnabled(!formPropertyMap.get(CATEGORY).isDisabled());
        } else {
            addField(CATEGORY, categoryPanel, wfmStrings.category());
        }

        if (formPropertyMap != null && formPropertyMap.get(BRAND) != null) {
            addField(BRAND, brandPanel, getTitle(formPropertyMap.get(BRAND).isChanged() ? formPropertyMap.get(BRAND).getTitle() : wfmStrings.brand(), formPropertyMap.get(BRAND).isRequired()), false,
                    formPropertyMap.get(BRAND).isInformation());
            if (formPropertyMap.get(BRAND).isInformation()) {
                new KpiToolTip(brandPanel, formPropertyMap.get(BRAND).getInformationText());
            }

            dwBrand.setEnabled(!formPropertyMap.get(BRAND).isDisabled());
        } else {
            addField(BRAND, brandPanel, wfmStrings.brand());
        }

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_PANEL) != null) {
            addField(DISCOUNT_PANEL, discountPanel, getTitle(formPropertyMap.get(DISCOUNT_PANEL).isChanged() ? formPropertyMap.get(DISCOUNT_PANEL).getTitle() : accountingStrings.discounts(), formPropertyMap.get(DISCOUNT_PANEL).isRequired()), false,
                    formPropertyMap.get(DISCOUNT_PANEL).isInformation());
            if (formPropertyMap.get(DISCOUNT_PANEL).isInformation()) {
                new KpiToolTip(discountPanel, formPropertyMap.get(DISCOUNT_PANEL).getInformationText());
            }

            discountListBox.setEnabled(!formPropertyMap.get(DISCOUNT_PANEL).isDisabled());
        } else {
            addField(DISCOUNT_PANEL, discountPanel, accountingStrings.discounts());
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null) {
            addField(TAX, taxLookUp, getTitle(formPropertyMap.get(TAX).isChanged() ? formPropertyMap.get(TAX).getTitle() : wfmStrings.taxRate(), formPropertyMap.get(TAX).isRequired()), false,
                    formPropertyMap.get(TAX).isInformation());
            if (formPropertyMap.get(TAX).isInformation()) {
                new KpiToolTip(taxLookUp, formPropertyMap.get(TAX).getInformationText());
            }

            taxLookUp.setEnabled(!formPropertyMap.get(TAX).isDisabled());
        } else {
            addField(TAX, taxLookUp, wfmStrings.taxRate());
        }
        addField(INPUT_PRICE_LEVEL, priceLevelTable, null);

        if (isDoubleTaxEnabled) {
            if (formPropertyMap != null && formPropertyMap.get(TAX + 2) != null) {
                addField(TAX + 2, doubleTaxLookUp, getTitle(formPropertyMap.get(TAX + 2).isChanged() ? formPropertyMap.get(TAX + 2).getTitle() : wfmStrings.taxRate() + 2, formPropertyMap.get(TAX + 2).isRequired()));
                doubleTaxLookUp.setEnabled(!formPropertyMap.get(TAX + 2).isDisabled());
            } else {
                FormGroup doubleTaxField = new FormGroup(wfmStrings.taxRate() + 2, doubleTaxLookUp);
                addField(TAX + 2, doubleTaxField);
            }
        }
    }

    private void onChangeCategory(boolean[] isFirstSelect) {
        AccountingService.App.get().getProductCategory(getCategorySelectedId(), new AbstractAsyncCallback<ProductCategoryItem>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void success(ProductCategoryItem productCategoryItem) {

                if (isFirstSelect[0] && product.getCategoryID() == null) {
                    drawCategoryCustomFieldForm(productCategoryItem.getCategoryCustomFields());
                    isFirstSelect[0] = false;
                } else {
                    if (productCategoryItem.getCategoryCustomFields() != null && productCategoryItem.getCategoryCustomFields().size() > 0) {
                        categoryCustomFieldBuilder.setReload(productCategoryItem.getCategoryCustomFields());
                        showSection(PRODUCT_CATEGORY_CUSTOM_FIELDS);
                    } else {
                        hideSection(PRODUCT_CATEGORY_CUSTOM_FIELDS);
                    }
                }
            }
        });
        regenerateProductNumber();
    }

    private void regenerateProductNumber() {
        NumberData productCurrentNumber = productNumberWidget.getNumberData(false);
        productService.regenerateProductNumber(getSupplierIds(), getCategorySelectedId(), productCurrentNumber, new AbstractAsyncCallback<NumberData>() {
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

    private DynamicTableColumn[] getPriceLevelTableColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[2];
        columns[0] = new DynamicTableColumn(wfmStrings.priceLevel(), "price_level", 200);
        columns[1] = new DynamicTableColumn(wfmStrings.customPrice(), "custom_price", 150);
        return columns;
    }

    private void drawMultiCurrencyPriceSection() {
        multiPricePanel = new FlexPanel();

        tblMultiSalesPrice = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsForMultiPrice(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        tblMultiCostPrice = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsForMultiPrice(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        multiSalesPricePanel = new GColumn(GColumnEnum.COL_6);
        multiSalesPricePanel.setVisible(Utils.isMultipleSalesPriceEnable());

        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE") != null) {
            multiSalesPricePanel.add(new FormGroup(formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").isChanged() ? formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").getTitle() : accountingStrings.multiCurrencySalesPriceSettings(), tblMultiSalesPrice));
        } else {
            multiSalesPricePanel.add(new FormGroup(accountingStrings.multiCurrencySalesPriceSettings(), tblMultiSalesPrice));
        }

        multiCostPricePanel = new GColumn(GColumnEnum.COL_6);
        multiCostPricePanel.setVisible(Utils.isMultipleSalesPriceEnable() && isMultiCostPriceAllowed());
        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_COST_PRICE") != null) {
            multiCostPricePanel.add(new FormGroup(formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").isChanged() ? formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").getTitle() : accountingStrings.multiCurrencyCostPriceSettings(), tblMultiCostPrice));
        } else {
            multiCostPricePanel.add(new FormGroup(accountingStrings.multiCurrencyCostPriceSettings(), tblMultiCostPrice));
        }

        multiPricePanel.add(new GRow(multiSalesPricePanel, multiCostPricePanel));

        addTitleField(MULTI_CURRENCY_PRICE_SETTINGS, wfmStrings.multiCurrencyPriceSettings());
        addField(MULTI_TABLE_PANEL, multiPricePanel, null);
    }

    private void drawMoreOptionsSection() {

        txtSkuNumber = new TextBox(true);
        txtSkuNumber.ensureDebugId(PRODUCT + "txtSkuNumber");
        txtUpcNumber = new TextBox(true);
        txtUpcNumber.ensureDebugId(PRODUCT + "txtUpcNumber");
        txtManufacturer = new TextBox(true);
        txtManufacturer.ensureDebugId(PRODUCT + "txtManufacturer");
        txtPartNumber = new TextBox(true);
        txtPartNumber.ensureDebugId(PRODUCT + "txtPartNumber");

        dwUnitMeasurement = new SmartMeasurementsLookUp(() -> new AddUnitMeasurementView(null, item -> dwUnitMeasurement.addMeasurementUnit((SelectItem) item)));
        dwUnitMeasurement.getSuggestBox().addSelectionHandler(c -> dwUnitMeasurement.islink());

//        showOnOpportunityCheckBox = new KpiSwitcher();
//        showOnOpportunityCheckBox.setOffLabel(inventoryStrings.showOnOpportunity());

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

        addTitleField(MORE_OPTIONS, wfmStrings.moreOptions());
        if (formPropertyMap != null && formPropertyMap.get(SKU_NUMBER) != null) {
            addField(SKU_NUMBER, txtSkuNumber, getTitle(formPropertyMap.get(SKU_NUMBER).isChanged() ? formPropertyMap.get(SKU_NUMBER).getTitle() : wfmStrings.skuNumber(), formPropertyMap.get(SKU_NUMBER).isRequired()), false,
                    formPropertyMap.get(SKU_NUMBER).isInformation());
            if (formPropertyMap.get(SKU_NUMBER).isInformation()) {
                new KpiToolTip(txtSkuNumber, formPropertyMap.get(SKU_NUMBER).getInformationText());
            }

            txtSkuNumber.setEnabled(!formPropertyMap.get(SKU_NUMBER).isDisabled());
        } else {
            addField(SKU_NUMBER, txtSkuNumber, wfmStrings.skuNumber());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.UPC_NUMBER) != null) {
            addField(CustomFormConstants.UPC_NUMBER, txtUpcNumber, getTitle(formPropertyMap.get(CustomFormConstants.UPC_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.UPC_NUMBER).getTitle() : wfmStrings.upcNumber(), formPropertyMap.get(CustomFormConstants.UPC_NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.UPC_NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.UPC_NUMBER).isInformation()) {
                new KpiToolTip(txtUpcNumber, formPropertyMap.get(CustomFormConstants.UPC_NUMBER).getInformationText());
            }

            txtUpcNumber.setEnabled(!formPropertyMap.get(UPC_NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.UPC_NUMBER, txtUpcNumber, wfmStrings.upcNumber());
        }

        if (formPropertyMap != null && formPropertyMap.get(UNIT_MEASUREMENT) != null) {
            addField(UNIT_MEASUREMENT, dwUnitMeasurement, getTitle(formPropertyMap.get(UNIT_MEASUREMENT).isChanged() ? formPropertyMap.get(UNIT_MEASUREMENT).getTitle() : wfmStrings.unitMeasurement(), formPropertyMap.get(UNIT_MEASUREMENT).isRequired()), false,
                    formPropertyMap.get(UNIT_MEASUREMENT).isInformation());
            if (formPropertyMap.get(UNIT_MEASUREMENT).isInformation()) {
                new KpiToolTip(dwUnitMeasurement, formPropertyMap.get(UNIT_MEASUREMENT).getInformationText());
            }

            dwUnitMeasurement.setEnabled(!formPropertyMap.get(UNIT_MEASUREMENT).isDisabled());
        } else {
            addField(UNIT_MEASUREMENT, dwUnitMeasurement, wfmStrings.unitMeasurement());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANUFACTURER) != null) {
            addField(CustomFormConstants.MANUFACTURER, txtManufacturer, getTitle(formPropertyMap.get(CustomFormConstants.MANUFACTURER).isChanged() ? formPropertyMap.get(CustomFormConstants.MANUFACTURER).getTitle() : wfmStrings.manufacturer(), formPropertyMap.get(CustomFormConstants.MANUFACTURER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.MANUFACTURER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.MANUFACTURER).isInformation()) {
                new KpiToolTip(txtManufacturer, formPropertyMap.get(CustomFormConstants.MANUFACTURER).getInformationText());
            }

            txtManufacturer.setEnabled(!formPropertyMap.get(MANUFACTURER).isDisabled());
        } else {
            addField(CustomFormConstants.MANUFACTURER, txtManufacturer, wfmStrings.manufacturer());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PART_NUMBER) != null) {
            addField(CustomFormConstants.PART_NUMBER, txtPartNumber, getTitle(formPropertyMap.get(CustomFormConstants.PART_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.PART_NUMBER).getTitle() : wfmStrings.partNumber(), formPropertyMap.get(CustomFormConstants.PART_NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PART_NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PART_NUMBER).isInformation()) {
                new KpiToolTip(txtPartNumber, formPropertyMap.get(CustomFormConstants.PART_NUMBER).getDefaultValue());
            }

            txtPartNumber.setEnabled(!formPropertyMap.get(PART_NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.PART_NUMBER, txtPartNumber, wfmStrings.partNumber());
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIERS) != null) {
            addField(CustomFormConstants.SUPPLIERS, multiSupplierTable, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIERS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIERS).getTitle() : Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers()), formPropertyMap.get(CustomFormConstants.SUPPLIERS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIERS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.SUPPLIERS).isInformation()) {
                new KpiToolTip(multiSupplierTable, formPropertyMap.get(CustomFormConstants.SUPPLIERS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIERS, multiSupplierTable, Property.getPluralWithObjectCode(Constants.SUPPLIER_LIST, wfmStrings.suppliers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null) {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(), getTitle(formPropertyMap.get(CustomFormConstants.BARCODE).isChanged() ? formPropertyMap.get(CustomFormConstants.BARCODE).getTitle() : wfmStrings.barcode(), formPropertyMap.get(BARCODE).isRequired()), false, formPropertyMap.get(CustomFormConstants.BARCODE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.BARCODE).isInformation()) {
                new KpiToolTip(barcodeGenerator.createWidget(), formPropertyMap.get(CustomFormConstants.BARCODE).getInformationText());
            }

            barcodeGenerator.setEnabled(!formPropertyMap.get(BARCODE).isDisabled());
        } else {
            addField(CustomFormConstants.BARCODE, barcodeGenerator.createWidget(), wfmStrings.barcode());
        }
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

    private void drawInventoryWarehouseSection() {
        addTitleField(INVENTORY_STOCK_INFORMATION, wfmStrings.stockInformation());
        productWarehouseView = new ProductWarehouseView(objectID, true, formPropertyMap);
        Div batchWidget = productWarehouseView.getBatchWidget();

        if ((Utils.isInventoryTrackingEnable() || Utils.isBatchSerialEnable() || AccountingUtils.get().isEnableBatchTrackingItems()) && objectID == null) {
            if (formPropertyMap != null && formPropertyMap.get(SERIAL_NUMBER) != null) {
                addField(SERIAL_NUMBER, batchWidget, getTitle(formPropertyMap.get(SERIAL_NUMBER).isChanged() ? formPropertyMap.get(SERIAL_NUMBER).getTitle() : wfmStrings.serialNumber(), formPropertyMap.get(SERIAL_NUMBER).isRequired()));
                productWarehouseView.setNoneEnable(!formPropertyMap.get(SERIAL_NUMBER).isDisabled());
                batchWidget.setEnabled(!formPropertyMap.get(SERIAL_NUMBER).isDisabled());
            } else {
                addField(SERIAL_NUMBER, batchWidget, wfmStrings.serialNumber());
            }
        }

        addField(WAREHOUSE_PANEL, productWarehouseView, null);
    }

    private void drawProductKitSection() {
        if (!fromInventory && !fromAssembly) {
            addTitleField(PRODUCT_GROUP_ITEMS_INFORMATION, property.getSingular(wfmStrings.productKitItemInfo(), wfmStrings.product()));
        }
        productKitView = new ProductKitView(objectID);
        addField(PRODUCT_GROUP, productKitView, null);
    }

    private void drawAssemblyItemRelatedForm() {
        addTitleField(CustomFormConstants.BILL_OF_MATERIALS, wfmStrings.billOfMaterials());
        productAssemblyView = new ProductAssemblyView(objectID);
        addField(ASSEMBLY_ITEMS, productAssemblyView, null);
    }

//    private void drawProductCustomFieldsSection() {
//        productCustomFieldsPanel = new FlexPanel();
//        productCustomFieldsPanel.setVisible(false);
//        addField(INPUT_PRODUCT_CUSTOM_FIELDS, productCustomFieldsPanel, null);
//    }

    //    private void drawProductCustomFieldsSection(List<CompanyCustomFieldItem> customFields) {
//        wfmProductCustomTable = new WfmCustomFieldsForm();
//
//        productCustomFieldBuilder = new ProductCustomFieldBuilder(ViewAddFiledsCodeName.ProductServiceAdd, wfmProductCustomTable, new CustomFiledsOnLoad() {
//            @Override
//            public void onLoad(List<String> showFieldCodeName) {
//
//            }
//
//            @Override
//            public void setCustomFieldValues() {
//                if (customFields != null) {
//                    productCustomFieldBuilder.setValues(customFields);
//                }
//            }
//        }, (String) null);
//
//        GColumn column = new GColumn(GColumnEnum.COL_12);
//        column.add(wfmProductCustomTable);
//
//        productCustomFieldsPanel.add(column);
//        productCustomFieldsPanel.setVisible(true);
//    }
//    FlexPanel categoryCustomFieldsPanel = new FlexPanel();

    private void drawAttachmentsSection() {
        fileUploadPanel = new GeneralFileUpload(F_PRODUCTS_SERVICES, objectID, true, objectID, null);
        fileUploadForm = new ProductPictureForm(isCopied() ? externalId : objectID);

        addTitleField(ATTACHMENTS, wfmStrings.attachments());
        addField(ATTACHMENTS, fileUploadPanel, null);
        addField(IMAGE_UPLOAD, fileUploadForm, null);
    }

    private void drawCategoryCustomFieldForm(ArrayList<CompanyCustomFieldItem> customFields) {

        WfmCustomFieldsForm wfmCategoryCustomTable = new WfmCustomFieldsForm();
        categoryCustomFieldBuilder = new CustomFieldBuilder(wfmCategoryCustomTable, customFields);
        addTitleField(PRODUCT_CATEGORY_CUSTOM_FIELDS, property.getSingular(wfmStrings.productCategoryCustomFields(), wfmStrings.product()));
        addField(INPUT_CUSTOM_FIELDS, wfmCategoryCustomTable, null);
        if (customFields != null && customFields.size() > 0) {
            showSection(PRODUCT_CATEGORY_CUSTOM_FIELDS);
        } else {
            hideSection(PRODUCT_CATEGORY_CUSTOM_FIELDS);
        }
    }

    @Override
    protected void addButtons() {
        addButton(assignSerialsButton);

        MaterialLink btnSave = new MaterialLink(wfmStrings.save());
        splitButton = new MaterialSplitButton(btnSave);

        MaterialLink btnSaveAndNew = new MaterialLink(wfmStrings.saveAndNew());
        btnSave.ensureDebugId(PRODUCT + "btnSaveAndClose");
        btnSave.addClickHandler(ch -> {
            splitButton.setEnabled(false);

            if (validate()) {
                LoadingPanel.loading(true);
                product = getProductObject();
                validateProduct(existingProductType != null && !existingProductType.equals(product.getType()));
            } else {
                splitButton.setEnabled(true);
            }
        });


        btnSaveAndNew.ensureDebugId(PRODUCT + "btnNewItem");
        btnSaveAndNew.addClickHandler(ch -> {
            splitButton.setEnabled(false);

            if (validate()) {
                LoadingPanel.loading(true);
                newItem = true;
                product = getProductObject();
                validateProduct(existingProductType != null && !existingProductType.equals(product.getType()));
            } else {
                splitButton.setEnabled(true);
            }
        });
        splitButton.addItem(btnSaveAndNew);
        addButton(splitButton);
    }

    private void initDiscountListWidget(DiscountItem[] allDiscounts) {
        if (discountListBox.getValues().size() > 0) {
            discountListBox.clear();
        }
        if (allDiscounts != null && allDiscounts.length > 0) {
            for (DiscountItem discountItem : allDiscounts) {
                discountListBox.addItem(discountItem.getName(), discountItem);
            }
            if (product.getDiscountItems() != null) {
                discountListBox.setValues(new ArrayList<>(Arrays.asList(product.getDiscountItems())));
            }
        }
    }

    private void validateProduct(final boolean isNewType) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCaseID(product.getObjectId());
        if (checkNumber) {
            fp.setCheckNumber(checkNumber);
            fp.setNumber(productNumberWidget.getNumberData(false).getNumberString());
        }
        fp.setName(productNameWidget.getItemName());
        if (isNewType) {
            fp.setNewType(isNewType);
        }
        if (txtUpcNumber != null && !"".equals(txtUpcNumber.getText())) {
            fp.setColumn(txtUpcNumber.getText());
        }
        productService.validateProduct(fp, new AbstractAsyncCallback<boolean[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void success(boolean[] result) {
                if (!checkNumber) {
                    result[0] = false;
                }
                if (isNewType && !copied) {
                    if (!result[1]) {
                        LoadingPanel.loading(false);
                        Info.show(property.getSingular(accountingStrings.errorUpdatingProductType(), wfmStrings.product()), Info.Type.WARNING);
                    } else {
                        product.setObjectId(null);
                        if (!result[0] && !result[2] && !result[3]) {
                            save(product);
                        } else {
                            LoadingPanel.loading(false);
                            if (result[0]) {
                                Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.product()), Info.Type.WARNING);
                            } else if (result[2]) {
                                Info.show(property.getSingular(accountingStrings.upcErrorMessage(), wfmStrings.product()), Info.Type.WARNING);
                            } else if (result[3]) {
                                warnAboutProductExistance();
                            }
                        }
                    }
                } else {
                    if (!result[0] && !result[2] && !result[3]) {
                        save(product);
                    } else {
                        LoadingPanel.loading(false);
                        if (result[0]) {
                            Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.product()), Info.Type.WARNING);
                        } else if (result[2]) {
                            Info.show(property.getSingular(accountingStrings.upcErrorMessage(), wfmStrings.product()), Info.Type.WARNING);
                        } else if (result[3]) {
                            warnAboutProductExistance();
                        }
                    }
                }
            }
        });
    }

    private void warnAboutProductExistance() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setMessage(wfmStrings.confirmation());
        messageBox.setMessage(property.getSingular(accountingStrings.alreadyProduct(), wfmStrings.product()));
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                save(product);
            }
        });
        messageBox.open();
        splitButton.setEnabled(true);
    }

    private void calculateTotal() {
        if (product.getObjectId() == null || (product.getObjectId() != null && !productType.equals(existingProductType)) || !product.hasUsed()) {
            String uprice = (txtPurchasePrice.getValue() != null && !txtPurchasePrice.getValue().isEmpty()) ? utils.formatUnitPrice(new BigDecimal(txtPurchasePrice.getValue().replaceAll("[,]", ""))) : AccountingUtils.getUnitPriceZero();
            txtPurchasePrice.setText(uprice);
            if (productType.equals(INVENTORY_ITEM) || ASSEMBLY_ITEM.equals(productType)) {
                productWarehouseView.setUnitPrice(AccountingUtils.get().parseToBigDecimal(uprice));
                productWarehouseView.calculate();
            }
        }
    }

    private WidgetsMap getWidgetsForMultiPrice(MultiPriceItem multiPriceItem) {
        WidgetsMap widgetsMap = new WidgetsMap();
        CurrencyLookUp currencyLookUp = new CurrencyLookUp(true);
        currencyLookUp.setAutocompleteOff();

        TextBox price = new TextBox(true);
        price.setText(AccountingUtils.getUnitPriceZero());
        price.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.checkToFocusTextBox(price, AccountingUtils.getUnitPriceZero());
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

    private BigDecimal getProductMultiPrice(ProductLookUp productLookUp) {
        if (productLookUp != null && productLookUp.getSelectedItemID() != null && assemblyItems.get(productLookUp.getSelectedItemID()) != null) {
            Integer productId = productLookUp.getSelectedItemID();
            List<MultiPriceItem> multiPriceItemsList = multiPriceItems.get(productId);
            if (multiPriceItemsList != null) {
                for (MultiPriceItem mpi : multiPriceItemsList) {
                    if (mpi != null && mpi.getCurrency() != null) {
                        return mpi.getPrice();
                    }
                }
            }
        }
        return null;
    }

    private boolean isMultiCostPriceAllowed() {
        return (purchasedFromSupplierCheckBox != null && purchasedFromSupplierCheckBox.getValue()) ||
                (productType.equals(INVENTORY_ITEM) || productType.equals(ASSEMBLY_ITEM) ||
                        productType.equals(NON_INVENTORY_ITEM) || productType.equals(OTHER_CHARGE) ||
                        productType.equals(SERVICE) || productType.equals(PRODUCT_KIT));
    }

    private void setMultiPriceItems() {
        ArrayList<MultiPriceItem> multiPriceItems = new ArrayList<>();

        for (HashMap<String, Widget> map : tblMultiSalesPrice.getWidgets()) {
            CurrencyLookUp currencyLookUp = (CurrencyLookUp) map.get(CURRENCY_COLUMN);
            TextBox priceBox = (TextBox) map.get(UNIT_PRICE_COLUMN);
            if (currencyLookUp.getSelectedItemID() != null && !priceBox.getText().isEmpty() && AccountingUtils.get().parseToBigDecimal(priceBox.getText()).compareTo(ZERO) != 0) {
                multiPriceItems.add(new MultiPriceItem(currencyLookUp.getSelectedItem(), AccountingUtils.get().parseToBigDecimal(priceBox.getText()), RECEIVABLE));
            }
        }
        if (isMultiCostPriceAllowed()) {
            for (HashMap<String, Widget> map : tblMultiCostPrice.getWidgets()) {
                CurrencyLookUp currencyLookUp = (CurrencyLookUp) map.get(CURRENCY_COLUMN);
                TextBox priceBox = (TextBox) map.get(UNIT_PRICE_COLUMN);
                if (currencyLookUp.getSelectedItemID() != null && !priceBox.getText().isEmpty() && AccountingUtils.get().parseToBigDecimal(priceBox.getText()).compareTo(ZERO) != 0) {
                    multiPriceItems.add(new MultiPriceItem(currencyLookUp.getSelectedItem(), AccountingUtils.get().parseToBigDecimal(priceBox.getText()), PAYABLE));
                }
            }
        }
        product.setMultiPrices(multiPriceItems);
    }

    private void fillProductTypeList() {
        if (dwProductType == null)
            dwProductType = new DataListBox();

        dwProductType.clear();
        dwProductType.setItems(PRODUCT_TYPES);
        dwProductType.setSelected(productType);
    }

    private void fillDiscountTypeList() {
        if (discountType == null)
            discountType = new DataListBox();

        discountType.clear();
        discountType.setItems(PRODUCT_DISCOUNT_TYPES);
        discountType.setSelected(0);
    }


    private void onChangeProductType() {
        if (dwProductType.getSelectedId() != null) {
            productType = fromAssembly ? ASSEMBLY_ITEM : dwProductType.getSelectedId();
        }
        multiSalesPricePanel.setVisible(Utils.isMultipleSalesPriceEnable());
        multiCostPricePanel.setVisible(Utils.isMultipleSalesPriceEnable() && isMultiCostPriceAllowed());
        multiPricePanel.setVisible(Utils.isMultipleSalesPriceEnable());
        if (Utils.isMultipleSalesPriceEnable()) {
            showSection(MULTI_CURRENCY_PRICE_SETTINGS);
        } else {
            hideSection(MULTI_CURRENCY_PRICE_SETTINGS);
        }

        productWarehouseView.setVisible(productType.equals(INVENTORY_ITEM) || productType.equals(ASSEMBLY_ITEM));
        productWarehouseView.enableWarehouseWidgetsForEditForm((existingProductType != null && !existingProductType.equals(productType)) || objectID == null || !product.hasUsed(), product);
        productAssemblyView.setVisible(productType.equals(ASSEMBLY_ITEM));
        productKitView.setVisible(productType.equals(PRODUCT_KIT));

        if (ASSEMBLY_ITEM.equals(productType)) {
            showSection(CustomFormConstants.BILL_OF_MATERIALS);
            showSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            showSection(CustomFormConstants.MORE_OPTIONS);
            openSection(CustomFormConstants.BILL_OF_MATERIALS);
            hideSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
        } else if (INVENTORY_ITEM.equals(productType)) {
            showSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            openSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            showSection(CustomFormConstants.MORE_OPTIONS);
            hideSection(CustomFormConstants.BILL_OF_MATERIALS);
            hideSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
        } else if (PRODUCT_KIT.equals(productType)) {
            showSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
            openSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
            hideSection(CustomFormConstants.BILL_OF_MATERIALS);
            hideSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            hideSection(CustomFormConstants.MORE_OPTIONS);
        } else if (NON_INVENTORY_ITEM.equals(productType)) {
            showSection(CustomFormConstants.MORE_OPTIONS);
            hideSection(CustomFormConstants.BILL_OF_MATERIALS);
            hideSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            hideSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
        } else if (SERVICE.equals(productType)) {
            showSection(CustomFormConstants.MORE_OPTIONS);
            hideSection(CustomFormConstants.BILL_OF_MATERIALS);
            hideSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            hideSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
        } else {
            hideSection(CustomFormConstants.BILL_OF_MATERIALS);
            hideSection(CustomFormConstants.INVENTORY_STOCK_INFORMATION);
            hideSection(CustomFormConstants.PRODUCT_GROUP_ITEMS_INFORMATION);
            hideSection(CustomFormConstants.MORE_OPTIONS);
        }

        // Preserve the currently selected Purchase Account across product type changes
        SelectItem currentSelectedPurchaseAccount = purchaseAccountLookUp.getSelectedItem();
        Integer currentPurchaseAccountId = currentSelectedPurchaseAccount != null ? currentSelectedPurchaseAccount.getId() : null;

        if ((INVENTORY_ITEM.equals(productType) || ASSEMBLY_ITEM.equals(productType)) && product.getCostOfSalesAccountItemList() != null && product.getCostOfSalesAccountItemList().size() > 0) {
            purchaseAccountLookUp.clear();
            purchaseAccountLookUp.setAccountCode(COST_OF_SALES);
            for (SelectItem item : product.getCostOfSalesAccountItemList()) {
                purchaseAccountLookUp.addAccountItem((AccountItem) item);
            }
        } else if (product.getAccountItemList() != null && product.getAccountItemList().size() > 0) {
            purchaseAccountLookUp.clear();
            for (SelectItem item : product.getAccountItemList()) {
                purchaseAccountLookUp.addAccountItem((AccountItem) item);
            }
        }

        // Restore the previously selected account if it's still valid, otherwise use default
        if (currentPurchaseAccountId != null && purchaseAccountLookUp.getSelectedItem(currentPurchaseAccountId) != null &&
                purchaseAccountLookUp.getSelectedItem(currentPurchaseAccountId).getName() != null) {
            purchaseAccountLookUp.setSelected(currentSelectedPurchaseAccount);
        } else {
            purchaseAccountLookUp.setSelected(product.getDefaultPayableAccount());
        }
    }

    private NewProduct getProductObject() {
        product.setObjectId(objectID);
        product.setType(dwProductType.getSelectedId());
        product.setDiscountType(discountType.getSelectedId());
        NumberData numberData = productNumberWidget.getNumberData(false);

        product.setNumberData(numberData);
        product.setItemNameID(productNameWidget.getItemNameID());
        product.setItemName(productNameWidget.getItemName());
        product.setDescription(txtProductDescription.getText());
        if (isCategorySelected()) {
            product.setCategoryID(getCategorySelectedId());
        } else {
            product.setCategoryID(0);
        }

        product.setDiscountItems(discountListBox.getSelectedValues().toArray(new DiscountItem[discountListBox.getSelectedValues().size()]));
        product.setVatId(taxLookUp.getSelectedItemID());
        product.setDoubleVatId(doubleTaxLookUp.getSelectedItemID());
        product.setRentStatus(statusListBox.getSelectedItem());
        product.setRentItem(rentItemLookUp.getSelectedItem());

        product.setInternalSKUNumber(txtSkuNumber.getText());
        product.setManufacturer(txtManufacturer.getText());
        product.setPartNumber(txtPartNumber.getText());
        product.setUpcNumber(txtUpcNumber.getText());
        product.setDiscountAmount(AccountingUtils.get().parseToBigDecimal(discountAmount.getText()));
        product.setUnitMeasurementID(dwUnitMeasurement.getSelectedItemID());
        product.setBarCodeText(barcodeGenerator.getBarcodeText());
        product.setQRCodeSizeID(barcodeGenerator.getQRCodeSizeID());

        product.setAccountId(salesAccountLookUp.getSelectedItemID());
        product.setCogsAccountID(purchaseAccountLookUp.getSelectedItemID());

        product.setSellingPrice(AccountingUtils.get().parseToBigDecimal(txtSalesPrice.getText()));
        product.setUnitPrice(AccountingUtils.get().parseToBigDecimal(txtPurchasePrice.getText()));

        if (Utils.isMultipleSalesPriceEnable() && !productType.equals(RENTAL_ITEM)) {
            setMultiPriceItems();
        }
        product.setBrandID(dwBrand.getSelectedItem() != null ? dwBrand.getSelectedItem().getId() : Integer.valueOf(0));
        product.setPurchasedFromSupplier(purchasedFromSupplierCheckBox.getValue());
        product.setSoldToCustomer(soldToCustomerCheckBox.getValue());
        product.setEnableIT(enableITCheckBox.getValue());
//        product.setShowOnOpportunity(showOnOpportunityCheckBox.getValue());
        product.setActive(activeProductCheckBox.getValue());
        product.setSentToTextileFinds(sentToTextileFindsCheckBox.getValue());

        if (productType.equals(ASSEMBLY_ITEM)) {
            product.setAssemblyItems(productAssemblyView.getAssemblyItems());
            product = productWarehouseView.getWarehouseData(product);
        } else if (productType.equals(INVENTORY_ITEM)) {
            product = productWarehouseView.getWarehouseData(product);
        } else if (productType.equals(PRODUCT_KIT)) {
            product = productKitView.getProductKitData(product);
        }
        if (productSerialsPopup != null && productSerialsPopup.getProductSerialItems() != null) {
            product.setProductSerialItems(productSerialsPopup.getProductSerialItems());
        }

        if (productSerialsImportPopup != null && productSerialsImportPopup.getProductSerialItems() != null) {
            product.setProductSerialItems(productSerialsImportPopup.getProductSerialItems());
        }

        if (categoryCustomFieldBuilder != null) {
            product.setCategoryCustomFieldItems(categoryCustomFieldBuilder.getValues());
        }

        product.setProductCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());


        product.setSuppliers(getSupplierIds());

        product.setImageGallery(fileUploadForm.getUploadedFiles());
        product.setAttachments(fileUploadPanel.getAttachedFiles());
        product.setLocations(location.getSelectedItems());

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

        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null) {
            if (formPropertyMap.get(TYPE).isRequired()) {
                errors += markAsError(dwProductType, dwProductType.getSelectedId() == null);
            }
        } else {
            errors += markAsError(dwProductType, dwProductType.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_TYPE) != null) {
            if (formPropertyMap.get(DISCOUNT_TYPE).isRequired()) {
                errors += markAsError(discountType, discountType.getSelectedId() == null);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
//                errors += markAsError(productNameWidget, !productNameWidget.validate());
                TextBox productName = new TextBox();
                productName.setText(productNameWidget.getItemName());
                errors += markAsError(productNameWidget, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged()
                        ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), productName, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
            }
        } else {
            errors += markAsError(productNameWidget, !productNameWidget.validate());
        }

        errors += markAsError(productNumberWidget, !productNumberWidget.validate());

        productNameWidget.removeStyleName(Constants.ERROR_FORM_STYLE); // Ikkita qizil chiziqni birini olib tashlash uchun yozilgan

        /*if (Utils.isProductTableCustomizationEnable()) {
            if (!Validation.validateTextAreaRequired(txtProductDescription)) {
                errors++;
            }

            if (!productType.equals(NON_INVENTORY_ITEM) && txtSkuNumber != null && !Validation.validateTextBoxRequired(txtSkuNumber)) {
                errors++;
            }
            if (txtManufacturer != null && !Validation.validateTextBoxRequired(txtManufacturer)) {
                errors++;
            }
        }*/

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null) {
            if (formPropertyMap.get(PURCHASE_PRICE).isRequired()) {
                errors += markAsError(txtPurchasePrice, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PURCHASE_PRICE).isChanged() ?
                        formPropertyMap.get(PURCHASE_PRICE).getTitle() : wfmStrings.purchasePrice(), txtPurchasePrice, formPropertyMap.get(PURCHASE_PRICE).getMinChar()));
            }/* else {
                errors += markAsError(txtPurchasePrice, Utils.isNullOrEmpty(txtPurchasePrice.getText()));
            }*/
        } else {
            errors += markAsError(txtPurchasePrice, Utils.isNullOrEmpty(txtPurchasePrice.getText()));
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

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_AMOUNT) != null) {
            if (formPropertyMap.get(DISCOUNT_AMOUNT).isRequired()) {
                errors += markAsError(discountAmount, !Validation.validateTextBoxRequired(discountAmount));
            }
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
                errors += markAsError(getCategoryWidget(), !isCategoryValid());
            }
        } else {
            errors += markAsError(getCategoryWidget(), !isCategoryValid());
        }

        errors += markAsError(productAssemblyView, productType.equals(ASSEMBLY_ITEM) && !productAssemblyView.validate());

        if ((productType.equals(INVENTORY_ITEM) || productType.equals(ASSEMBLY_ITEM))) {

            errors += markAsError(productWarehouseView, !productWarehouseView.validate());

            if (!productWarehouseView.validateClosePostingPeriod(product.getTotalValue())) {
                Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.asOfDate(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }
        }

        errors += markAsError(productKitView, productType.equals(PRODUCT_KIT) && !productKitView.validateKitItems());

        errors += getCustomFieldUtil().validateCustomFields();  //temporary solution

        //this is a temporary solution.
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PRODUCT_CATEGORY_VALIDATION)) {
            errors += markAsError(getCategoryWidget(), !isCategoryValid());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SUPPLIER_VALIDATION)) {
            for (WidgetsMap map : multiSupplierTable.getWidgetsMaps()) {
                CRMLookUp clientLookup = (CRMLookUp) map.getWidget(SUPPLIER);
                if (clientLookup != null) {
                    errors += markAsError(clientLookup, clientLookup.getSelectedItem() == null);
                    Validation.validateLookUpRequired(clientLookup);
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired() && txtProductDescription != null) {
            errors += markAsError(txtProductDescription, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), txtProductDescription, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BRAND) != null && formPropertyMap.get(CustomFormConstants.BRAND).isRequired() && dwBrand != null) {
            errors += markAsError(dwBrand, !Validation.validateListBoxRequired(dwBrand));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DISCOUNT_PANEL) != null && formPropertyMap.get(CustomFormConstants.DISCOUNT_PANEL).isRequired() && discountListBox != null) {
            errors += markAsError(discountListBox, discountListBox.getSelectedValues() == null || discountListBox.getSelectedValues() != null && discountListBox.getSelectedValues().size() == 0);
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null && formPropertyMap.get(TAX).isRequired() && taxLookUp != null) {
            errors += markAsError(taxLookUp, !Validation.validateLookUpRequired(taxLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX + 2) != null && formPropertyMap.get(TAX + 2).isRequired() && doubleTaxLookUp != null) {
            errors += markAsError(doubleTaxLookUp, !Validation.validateLookUpRequired(doubleTaxLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(SKU_NUMBER) != null && formPropertyMap.get(SKU_NUMBER).isRequired() && txtSkuNumber != null) {
            errors += markAsError(txtSkuNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(SKU_NUMBER).isChanged() ?
                    formPropertyMap.get(SKU_NUMBER).getTitle() : wfmStrings.skuNumber(), txtSkuNumber, formPropertyMap.get(SKU_NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(UPC_NUMBER) != null && formPropertyMap.get(UPC_NUMBER).isRequired() && txtUpcNumber != null) {
            errors += markAsError(txtUpcNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(UPC_NUMBER).isChanged() ?
                    formPropertyMap.get(UPC_NUMBER).getTitle() : wfmStrings.upcNumber(), txtUpcNumber, formPropertyMap.get(UPC_NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(SUPPLIERS) != null && formPropertyMap.get(SUPPLIERS).isRequired() && multiSupplierTable != null && multiSupplierTable.getWidgets() != null) {
            if (multiSupplierTable.getWidgets() != null && multiSupplierTable.getWidgets().get(0).size() > 0) {
                CRMLookUp clientLookup = (CRMLookUp) multiSupplierTable.getWidgetsMaps().get(0).getWidget(SUPPLIER);
                if (clientLookup != null) {
                    errors += markAsError(multiSupplierTable, !Validation.validateLookUpRequired(clientLookup));
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(MANUFACTURER) != null && formPropertyMap.get(MANUFACTURER).isRequired() && txtManufacturer != null) {
            errors += markAsError(txtManufacturer, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(MANUFACTURER).isChanged() ?
                    formPropertyMap.get(MANUFACTURER).getTitle() : wfmStrings.manufacturer(), txtManufacturer, formPropertyMap.get(MANUFACTURER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(UNIT_MEASUREMENT) != null && formPropertyMap.get(UNIT_MEASUREMENT).isRequired() && dwUnitMeasurement != null) {
            errors += markAsError(dwUnitMeasurement, !Validation.validateLookUpRequired(dwUnitMeasurement));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null && formPropertyMap.get(CustomFormConstants.BARCODE).isRequired() && barcodeGenerator != null) {
            errors += markAsError(barcodeGenerator.createImageWidget(), !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.BARCODE).isChanged() ? formPropertyMap.get(CustomFormConstants.BARCODE).getTitle() : wfmStrings.barcode(), barcodeGenerator.getBarcodeTxtBox(), formPropertyMap.get(CustomFormConstants.BARCODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(PART_NUMBER) != null && formPropertyMap.get(PART_NUMBER).isRequired() && txtPartNumber != null) {
            errors += markAsError(txtPartNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PART_NUMBER).isChanged() ?
                    formPropertyMap.get(PART_NUMBER).getTitle() : wfmStrings.partNumber(), txtPartNumber, formPropertyMap.get(PART_NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE") != null && formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").isRequired() && tblMultiSalesPrice != null && tblMultiSalesPrice.getWidgets() != null && tblMultiSalesPrice.getWidgets().get(0) != null) {
            CurrencyLookUp currencyLookUp = (CurrencyLookUp) tblMultiSalesPrice.getWidgetsMaps().get(0).getWidget(CURRENCY_COLUMN);
            if (currencyLookUp != null) {
                errors += markAsError(tblMultiSalesPrice, !Validation.validateLookUpRequired(currencyLookUp));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_COST_PRICE") != null && formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").isRequired() && tblMultiCostPrice != null && tblMultiCostPrice.getWidgets() != null && tblMultiCostPrice.getWidgets().get(0) != null) {
            CurrencyLookUp currencyLookUp = (CurrencyLookUp) tblMultiCostPrice.getWidgetsMaps().get(0).getWidget(CURRENCY_COLUMN);
            if (currencyLookUp != null) {
                errors += markAsError(tblMultiCostPrice, !Validation.validateLookUpRequired(currencyLookUp));
            }
        }

//        errors += fileUploadPanel.validated() ? 0 : 1;
        if (errors > 0) {
            Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
        }

        return errors == 0;
    }

    private void save(NewProduct product) {
        product.setCopied(isCopied());
        productService.saveProduct(product, new AbstractAsyncCallback<ProductSelectItem>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ProductSelectItem result) {
                LoadingPanel.loading(false);
                if (result != null && result.getId() == -1) {
                    splitButton.setEnabled(true);
                    productNumberWidget.addStyleName(Constants.ERROR_FORM_STYLE);
                    Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.product()), Info.Type.WARNING);
                    return;
                }
                PriceLevelService.App.get().setCustomPriceFromPriceLevels(result.getId(), getPriceLevelPPItems(), new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        //EditProductView.this.saved(result);
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        //EditProductView.this.saved(result);
                    }
                });
                if (newItem) {
                    productType = dwProductType.getSelectedIndex();
                    objectID = null;
                    closeTab("product|add/add");
                    onInitialize();
                } else {
                    closeTab(null);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, null);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
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

    public boolean isCopied() {
        return copied;
    }

    public void setCopied(boolean copied) {
        this.copied = copied;
    }

    public void setStorefrontID(Integer storefrontID) {
        this.storefrontID = storefrontID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public void setFromInventory(boolean fromInventory) {
        this.fromInventory = fromInventory;
    }

    public void setFromAssembly(boolean fromAssembly) {
        this.fromAssembly = fromAssembly;
    }

    public TextBox getTxtPurchasePrice() {
        return txtPurchasePrice;
    }

    public void setTxtPurchasePrice(TextBox txtPurchasePrice) {
        this.txtPurchasePrice = txtPurchasePrice;
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

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null && formPropertyMap.get(TYPE).getSelectedId() != null && dwProductType != null) {
            dwProductType.setSelected(new SelectItem(formPropertyMap.get(TYPE).getSelectedId(), formPropertyMap.get(TYPE).getDefaultValue()));
            onChangeProductType();
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue().length() > 0 && productNameWidget != null) {
            productNameWidget.setValue(null, formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null && txtProductDescription != null) {
            txtProductDescription.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_PRICE) != null && formPropertyMap.get(PURCHASE_PRICE).getDefaultValue() != null && txtPurchasePrice != null && isNumeric(formPropertyMap.get(PURCHASE_PRICE).getDefaultValue())) {
            BigDecimal text = new BigDecimal(formPropertyMap.get(PURCHASE_PRICE).getDefaultValue());
            txtPurchasePrice.setValue(text != null ? utils.formatUnitPrice(text) : "");
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_PRICE) != null && formPropertyMap.get(SALES_PRICE).getDefaultValue() != null && txtSalesPrice != null && isNumeric(formPropertyMap.get(SALES_PRICE).getDefaultValue())) {
            BigDecimal text = new BigDecimal(formPropertyMap.get(SALES_PRICE).getDefaultValue());
            txtSalesPrice.setValue(text != null ? utils.formatUnitPrice(text) : "");
            updateSalesPrice(text);
        }


        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_AMOUNT) != null && formPropertyMap.get(DISCOUNT_AMOUNT).getDefaultValue() != null && discountAmount != null && isNumeric(formPropertyMap.get(DISCOUNT_AMOUNT).getDefaultValue())) {
            BigDecimal text = new BigDecimal(formPropertyMap.get(DISCOUNT_AMOUNT).getDefaultValue());
            discountAmount.setValue(text != null ? utils.formatUnitPrice(text) : "");
        }

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_TYPE) != null && formPropertyMap.get(DISCOUNT_TYPE).getSelectedId() != null && discountType != null) {
            discountType.setSelected(new SelectItem(formPropertyMap.get(DISCOUNT_TYPE).getSelectedId(), formPropertyMap.get(DISCOUNT_TYPE).getDefaultValue()));
        }


        if (formPropertyMap != null && formPropertyMap.get(PURCHASE_ACCOUNT) != null && formPropertyMap.get(PURCHASE_ACCOUNT).getSelectedId() != null && purchaseAccountLookUp != null) {
            purchaseAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(PURCHASE_ACCOUNT).getSelectedId(), formPropertyMap.get(PURCHASE_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(SALES_ACCOUNT) != null && formPropertyMap.get(SALES_ACCOUNT).getSelectedId() != null && salesAccountLookUp != null) {
            salesAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(SALES_ACCOUNT).getSelectedId(), formPropertyMap.get(SALES_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null
                && formPropertyMap.get(CATEGORY).getSelectedId() != null) {
            setCategorySelected(
                    formPropertyMap.get(CATEGORY).getSelectedId(),
                    formPropertyMap.get(CATEGORY).getDefaultValue()
            );
            boolean[] isFirstSelect = {true};
            onChangeCategory(isFirstSelect);
        }

        if (formPropertyMap != null && formPropertyMap.get(BRAND) != null && formPropertyMap.get(BRAND).getSelectedId() != null && dwBrand != null) {
            dwBrand.setSelected(new SelectItem(formPropertyMap.get(BRAND).getSelectedId(), formPropertyMap.get(BRAND).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BARCODE) != null && formPropertyMap.get(CustomFormConstants.BARCODE).getDefaultValue() != null && barcodeGenerator != null) {
            barcodeGenerator.setData(formPropertyMap.get(CustomFormConstants.BARCODE).getDefaultValue(), 1);
        }

        if (formPropertyMap != null && formPropertyMap.get(DISCOUNT_PANEL) != null && formPropertyMap.get(DISCOUNT_PANEL).getDefaultValue() != null && discountListBox != null) {
            List<DiscountItem> items = new ArrayList<>();
            items.add(new DiscountItem(formPropertyMap.get(DISCOUNT_PANEL).getSelectedId(), formPropertyMap.get(DISCOUNT_PANEL).getDefaultValue()));
            discountListBox.setValues(items);
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX) != null && formPropertyMap.get(TAX).getSelectedId() != null && taxLookUp != null) {
            taxLookUp.setSelected(new SelectItem(formPropertyMap.get(TAX).getSelectedId(), formPropertyMap.get(TAX).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX + 2) != null && formPropertyMap.get(TAX + 2).getSelectedId() != null && doubleTaxLookUp != null) {
            doubleTaxLookUp.setSelected(new SelectItem(formPropertyMap.get(TAX + 2).getSelectedId(), formPropertyMap.get(TAX + 2).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get("SKU_NUMBER") != null && formPropertyMap.get("SKU_NUMBER").getDefaultValue() != null && txtSkuNumber != null) {
            txtSkuNumber.setText(formPropertyMap.get("SKU_NUMBER").getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get("UPC_NUMBER") != null && formPropertyMap.get("UPC_NUMBER").getDefaultValue() != null && txtUpcNumber != null) {
            txtUpcNumber.setText(formPropertyMap.get("UPC_NUMBER").getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get("SUPPLIERS") != null && formPropertyMap.get("SUPPLIERS").getSelectedId() != null && multiSupplierTable != null && multiSupplierTable.getWidgets() != null && multiSupplierTable.getWidgets().get(0) != null) {
            CRMLookUp clientLookup = (CRMLookUp) multiSupplierTable.getWidgetsMaps().get(0).getWidget(SUPPLIER);
            if (clientLookup != null) {
                clientLookup.setSelected(formPropertyMap.get("SUPPLIERS").getSelectedId(), formPropertyMap.get("SUPPLIERS").getDefaultValue());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANUFACTURER) != null && formPropertyMap.get(CustomFormConstants.MANUFACTURER).getDefaultValue() != null && txtPartNumber != null) {
            txtManufacturer.setText(formPropertyMap.get(CustomFormConstants.MANUFACTURER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get("UNIT_MEASUREMENT") != null && formPropertyMap.get("UNIT_MEASUREMENT").getSelectedId() != null && dwUnitMeasurement != null) {
            dwUnitMeasurement.setSelected(new SelectItem(formPropertyMap.get("UNIT_MEASUREMENT").getSelectedId(), formPropertyMap.get("UNIT_MEASUREMENT").getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get("PART_NUMBER") != null && formPropertyMap.get("PART_NUMBER").getDefaultValue() != null && txtPartNumber != null) {
            txtPartNumber.setText(formPropertyMap.get("PART_NUMBER").getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE") != null && formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").getSelectedId() != null && tblMultiSalesPrice != null && tblMultiSalesPrice.getWidgets() != null && tblMultiSalesPrice.getWidgets().get(0) != null) {
            CurrencyLookUp currencyLookUp = (CurrencyLookUp) tblMultiSalesPrice.getWidgetsMaps().get(0).getWidget(CURRENCY_COLUMN);
            if (currencyLookUp != null) {
                currencyLookUp.setSelected(formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").getSelectedId(), formPropertyMap.get("MULTI_CURRENCY_SALES_PRICE").getDefaultValue());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get("MULTI_CURRENCY_COST_PRICE") != null && formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").getSelectedId() != null && tblMultiCostPrice != null && tblMultiCostPrice.getWidgets() != null && tblMultiCostPrice.getWidgets().get(0) != null) {
            CurrencyLookUp currencyLookUp = (CurrencyLookUp) tblMultiCostPrice.getWidgetsMaps().get(0).getWidget(CURRENCY_COLUMN);
            if (currencyLookUp != null) {
                currencyLookUp.setSelected(formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").getSelectedId(), formPropertyMap.get("MULTI_CURRENCY_COST_PRICE").getDefaultValue());
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(SERIAL_NUMBER) != null && formPropertyMap.get(SERIAL_NUMBER).getDefaultValue() != null && soldToCustomerCheckBox != null) {
            if ("None".equals(formPropertyMap.get(SERIAL_NUMBER).getDefaultValue())) {
                productWarehouseView.setNoneValue(true);
            } else if ("Track batches".equals(formPropertyMap.get(SERIAL_NUMBER).getDefaultValue())) {
                productWarehouseView.setTrackBatchesValue(true);
            }
        }
    }

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }

    private void updateSalesPrice(BigDecimal amount) {
        if (amount != null) {
            unFormattedSalesPrice = amount.doubleValue();
        } else {
            unFormattedSalesPrice = 0.0;
        }
    }

    private Integer getCategorySelectedId() {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            return dwCategoryLookUp.getSelectedItemID();
        }
        return dwCategory.getSelectedId();
    }

    private void setCategorySelected(Integer id, String name) {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            dwCategoryLookUp.setSelected(new SelectItem(id, name));
        } else {
            dwCategory.setSelected(id);
        }
    }

    private void setCategoryItems(SelectItem[] items) {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            dwCategoryLookUp.setItems(items);
        } else {
            dwCategory.setItems(items);
        }
    }

    private void addCategoryItem(SelectItem item) {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            if (dwCategoryLookUp != null) dwCategoryLookUp.addItem(item);
        } else {
            if (dwCategory != null) dwCategory.addItem(item);
        }
    }

    private void clearCategory() {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            dwCategoryLookUp.clear();
        } else {
            dwCategory.clear();
        }
    }

    private void setCategoryEnabled(boolean enabled) {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            dwCategoryLookUp.setEnabled(enabled);
        } else {
            dwCategory.setEnabled(enabled);
        }
    }

    private boolean isCategorySelected() {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            return dwCategoryLookUp.getSelectedItemID() != null;
        }
        return dwCategory.isSomethingSelected();
    }

    private Widget getCategoryWidget() {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            return dwCategoryLookUp;
        }
        return dwCategory;
    }

    private boolean isCategoryValid() {
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            return Validation.validateLookUpRequired(dwCategoryLookUp);
        }
        return Validation.validateListBoxRequired(dwCategory, new HTML(), accountingStrings.categoryIsRequired());
    }
}
