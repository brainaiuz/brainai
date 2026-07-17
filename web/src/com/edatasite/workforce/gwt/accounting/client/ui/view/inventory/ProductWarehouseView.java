package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseLocationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAddTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialPopup;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION;


/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 3, 2011
 * Time: 1:55:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductWarehouseView extends Composite implements AccountingConstants, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final boolean isEnabledDetailedProductLocation = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_DETAILED_PRODUCT_LOCATION);
    private final Integer inventoryObjectID;
    private MultiTableNewUI tblInventoryWarehouse;
    private AccountsLookUp assetAccountsLookUp;
    private DatePicker asOfDatePicker;
    private KpiRadioButton none;
    private KpiRadioButton trackSerialnumber;
    private KpiRadioButton batchSerialnumber;
    private KpiRadioButton trackBatches;
    private Div widgetBatch;
    private HTML total;
    private BigDecimal unitPrice = ZERO;
    private BigDecimal totalQty = ZERO;
    private BigDecimal totalQtyOnHand = ZERO;
    private boolean editable = true;
    private Date asOfDate = null;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private Integer productLocationId;

    public ProductWarehouseView(Integer inventoryObjectID, boolean editable, LinkedHashMap<String, FormProperty> formPropertyMap) {
        this.inventoryObjectID = inventoryObjectID;
        this.editable = editable;
        this.formPropertyMap = formPropertyMap;
        initialize();
    }

    public ProductWarehouseView(Integer inventoryObjectID, boolean editable) {
        this.inventoryObjectID = inventoryObjectID;
        this.editable = editable;
        initialize();
    }

    public NewProduct getWarehouseData(NewProduct product) {
        product.setProductLocations(getProductLocations(product));
        product.setTotalQtyOnHand(totalQtyOnHand);
        calculateAndSetTotalValueToProduct(product);
        product.setQuantity(totalQty);
        return product;
    }

    public void setWarehouseData(NewProduct product) {
        //fill selected product locations
        if (product.getProductLocations() != null && product.getProductLocations().length > 0) {
            tblInventoryWarehouse.removeAllRows();
            for (int n = 0; n < product.getProductLocations().length; n++) {
                tblInventoryWarehouse.addWidgets(getWarehouseWidgets(product.getProductLocations()[n], product));
            }
        }

        if (product.getAssetAccount() != null && product.getAssetAccount().getId() != null) {
            assetAccountsLookUp.addItem(product.getAssetAccount());
        } else if (product.getDefaultAssetAccount() != null) {
            assetAccountsLookUp.setSelected(product.getDefaultAssetAccount());
        }
        if (product.getAsOf() != null) {
            asOfDatePicker.setDate(product.getAsOf().getNonConvertedDate());
            asOfDate = product.getAsOf().getNonConvertedDate();
        }
        if (product.getTotalValue() != null) {
            total.setText(AccountingUtils.get().formatPrice(product.getTotalValue()));
        }
        if (product.getInventoryTrackingEnabled()) {
            trackSerialnumber.setValue(true);
        } else if (product.getBatchTrackingEnabled()) {
            batchSerialnumber.setValue(true);
        } else if (product.getTrackBatchesEnabled()) {
            none.setEnabled(false);
            trackBatches.setEnabled(false);
            trackBatches.setValue(true);
        } else {
            none.setValue(true);
        }
    }

    public boolean validate() {
        int errors = 0;
        totalQty = ZERO;

        //warehouse table validation       DO NOT DELETE!!!
        for (WidgetsMap map : tblInventoryWarehouse.getWidgetsMaps()) {
            if (Utils.isMultiWarehouseEnabled()) {
                WarehouseLookUp warehouseLookUp = (WarehouseLookUp) map.getWidget("warehouse");
                if (!Validation.validateLookUpRequired(warehouseLookUp)) {
                    errors++;
                }
            }
            TextBox qtyOnHand = (TextBox) map.getWidget("qtyOnHand");
            ItemSerialPopup.Link serialLlink = (ItemSerialPopup.Link) map.getWidget("serialNumber");
            ItemAddTrackBatchPopup.Link batchLlink = (ItemAddTrackBatchPopup.Link) map.getWidget("trackBatch");
            if ((!Validation.validateTextBoxRequired(qtyOnHand) /*|| "0.00".equals(qtyOnHand.getValue())*/) && inventoryObjectID == null) {
                errors++;
            } else if (trackSerialnumber.getValue() && AccountingUtils.get().parseToBigDecimal(qtyOnHand.getValue()).intValue() != serialLlink.getSerials().size()) {
                errors++;
            } else if (inventoryObjectID == null && trackBatches.getValue()
                    && AccountingUtils.get().parseToBigDecimal(qtyOnHand.getValue()).compareTo(batchLlink.getTotalQty()) != 0) {
                batchLlink.addStyleName("x-form-invalid");
                errors++;
            } else {
                totalQty = totalQty.add(AccountingUtils.get().parseToBigDecimal(qtyOnHand.getValue()));
            }

            TextBox minReorderPoint = (TextBox) map.getWidget("minReorderPoint");
            if (!Validation.validateTextBoxRequired(minReorderPoint) && inventoryObjectID == null) {
                errors++;
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT) != null) {
            if (formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).isRequired() && !Validation.validateLookUpRequired(assetAccountsLookUp)) {
                errors++;
            }
        } else {
            if (!Validation.validateLookUpRequired(assetAccountsLookUp)) {
                errors++;
            }
        }

        if (formPropertyMap != null && formPropertyMap.get("AS_OF") != null && formPropertyMap.get("AS_OF").isRequired() && !Validation.validateDate(asOfDatePicker)) {
            errors++;
        }

        if (totalQty.intValue() == 0 && ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(total.getText())) < 0 && inventoryObjectID == null) {
            errors++;
            Info.show(accountingStrings.theOnHandFieldMess(), Info.Type.WARNING);
        }

        return errors == 0;
    }

    public void calculate() {
        BigDecimal countOfProduct = new BigDecimal(0);
        BigDecimal countOfReorderPoint = new BigDecimal(0);

        //calculate count of product and reorder point  DO NOT DELETE PLEASE !!!
        for (WidgetsMap map : tblInventoryWarehouse.getWidgetsMaps()) {
            TextBox txtQtyOnHand = (TextBox) map.getWidget("qtyOnHand");
            if (txtQtyOnHand.getValue() != null && !txtQtyOnHand.getValue().isEmpty()) {
                countOfProduct = countOfProduct.add(AccountingUtils.get().parseToBigDecimal(txtQtyOnHand.getValue()));
            }

            TextBox txtMinReorderPoint = (TextBox) map.getWidget("minReorderPoint");
            if (txtMinReorderPoint.getValue() != null && !txtMinReorderPoint.getValue().isEmpty()) {
                countOfReorderPoint = countOfReorderPoint.add(AccountingUtils.get().parseToBigDecimal(txtMinReorderPoint.getValue()));
            }
        }


        if (unitPrice != null) {
            total.setText(AccountingUtils.get().formatPrice(unitPrice.multiply(countOfProduct)));
        }
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void enableWarehouseWidgetsForEditForm(boolean enable, NewProduct product) {
        //warehouse table validation
        for (WidgetsMap map : tblInventoryWarehouse.getWidgetsMaps()) {
            WarehouseLookUp warehouseLookUp = (WarehouseLookUp) map.getWidget("warehouse");
            if (warehouseLookUp != null) {
                warehouseLookUp.setEnabled(enable);
            }
            ((TextBox) map.getWidget("qtyOnHand")).setEnabled(enable);
        }

        assetAccountsLookUp.setEnabled(editable);
        asOfDatePicker.setEnabled(enable);

        setUnitPrice(product.getUnitPrice());
    }

    private void initialize() {
        FlexPanel pnlContainer = new FlexPanel();

        assetAccountsLookUp = new AccountsLookUp(true, Constants.ASSETS);

        none = new KpiRadioButton("rb", " None");
        if (inventoryObjectID == null) {
            none.setValue(true);
            none.addValueChangeHandler(e -> onSerialNumberTrackingChange(false));
        }
        none.setEnabled(editable);
        trackSerialnumber = new KpiRadioButton("rb", "Track Serial Number");
        if (inventoryObjectID == null) {
            trackSerialnumber.addValueChangeHandler(e -> onSerialNumberTrackingChange(e.getValue()));
        }
        trackSerialnumber.setEnabled(inventoryObjectID == null);

        batchSerialnumber = new KpiRadioButton("rb", "Track Batch Serials");
        if (inventoryObjectID == null) {
            batchSerialnumber.addValueChangeHandler(e -> onSerialNumberTrackingChange(false));
        }
        batchSerialnumber.setEnabled(editable);

        trackBatches = new KpiRadioButton("rb", wfmStrings.trackBatches());
        if (inventoryObjectID == null) {
            trackBatches.addValueChangeHandler(e -> onTrackBatchChange(e.getValue()));
        }
        trackBatches.setEnabled(editable);

        total = new HTML();
        total.setText(AccountingUtils.get().formatPrice(AccountingConstants.ZERO));

        asOfDatePicker = new DatePicker(new Date());

        tblInventoryWarehouse = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWarehouseWidgets(new ProductLocationItem(), new NewProduct());
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, inventoryObjectID != null);
        tblInventoryWarehouse.setOnLinesRemoved(this::calculate);

        TotalTable totalTable = new TotalTable();
        totalTable.addItem(accountingStrings.totalValue(), total);

        GColumn col1 = new GColumn(GColumnEnum.COL_6);
        GColumn col2 = new GColumn(GColumnEnum.COL_6);
        GColumn col3 = new GColumn(GColumnEnum.COL_12);
        GColumn col4 = new GColumn(GColumnEnum.COL_6);
        GColumn col5 = new GColumn(GColumnEnum.COL_6);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT) != null) {
            FormGroup assetFormGroup = new FormGroup(formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).getTitle() : wfmStrings.assetAccount(), assetAccountsLookUp);
            assetFormGroup.setVisible(!Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION));
            col1.add(assetFormGroup);
            assetAccountsLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).isDisabled());
            if (inventoryObjectID == null && formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).getSelectedId() != null) {
                assetAccountsLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).getSelectedId(), formPropertyMap.get(CustomFormConstants.ASSET_ACCOUNT).getDefaultValue()));
            }
        } else {
            col1.add(new FormGroup(wfmStrings.assetAccount(), assetAccountsLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get("AS_OF") != null) {
            col2.add(new FormGroup(formPropertyMap.get("AS_OF").isChanged() ? formPropertyMap.get("AS_OF").getTitle() : wfmStrings.asOF(), asOfDatePicker));
            asOfDatePicker.setEnabled(!formPropertyMap.get("AS_OF").isDisabled());
            if (inventoryObjectID == null && formPropertyMap.get("AS_OF").getDefaultValue() != null) {
                try {
                    asOfDatePicker.setDate(DateUtils.parse(formPropertyMap.get("AS_OF").getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        } else {
            col2.add(new FormGroup(wfmStrings.asOF(), asOfDatePicker));
        }

        col3.add(tblInventoryWarehouse);

        if (Utils.isInventoryTrackingEnable() || Utils.isBatchSerialEnable() || AccountingUtils.get().isEnableBatchTrackingItems()) {
            Div div =generateBatchTrackView(none, trackSerialnumber, batchSerialnumber, trackBatches);
            widgetBatch = div;
            col4.add(div);
        }
        col5.add(new FormGroup("&nbsp;", totalTable));

        pnlContainer.add(new GRow(col1, col2));
        pnlContainer.add(new GRow(col3));
        pnlContainer.add(new GRow(col4, col5));

        initWidget(pnlContainer);
    }

    static Div generateBatchTrackView(KpiRadioButton none, KpiRadioButton trackSerialnumber, KpiRadioButton batchSerialnumber, KpiRadioButton trackBatches) {
        Div div = new Div("stack-x");
        div.add(none);
        if (Utils.isInventoryTrackingEnable()) {
            div.add(trackSerialnumber);
        }
        if (Utils.isBatchSerialEnable()) {
            div.add(batchSerialnumber);
        }
        if (AccountingUtils.get().isEnableBatchTrackingItems()) {
            div.add(trackBatches);
        }
        return div;
    }

    private WidgetsMap getWarehouseWidgets(final ProductLocationItem item, NewProduct product) {
        WidgetsMap widgetsMap = new WidgetsMap();

        if (Utils.isMultiWarehouseEnabled()) {
            final WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
            warehouseLookUp.setEnabled(editable);

            RadioButton itemDefaultWarehouseRadio = new KpiRadioButton("itemDefaultWarehouse");
            new KpiToolTip(itemDefaultWarehouseRadio, wfmStrings.defaultWarehouse());

            final WfmDropdown productLocationsDropdown = new WfmDropdown();
            productLocationsDropdown.setEnabled(true);
            warehouseLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setProductLocations(null, warehouseLookUp.getSelectedItemID(), productLocationsDropdown));
            WfmDropdown aisleDropdown = new WfmDropdown();
            WfmDropdown rackDropdown = new WfmDropdown();
            WfmDropdown shelfDropdown = new WfmDropdown();
            WfmButton2 saveLocationButton = new WfmButton2(wfmStrings.save());

            if (isEnabledDetailedProductLocation) {
                aisleDropdown.setWidth(NORMAL_WIDTH);
                rackDropdown.setWidth(NORMAL_WIDTH);
                shelfDropdown.setWidth(NORMAL_WIDTH);
                warehouseLookUp.setWidth(MIN_DEFAULT_WIDTH);

                AccountingService.App.get().getProductLocationReference("WAREHOUSE_LOCATOINS", new AsyncCallback<List<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(List<SelectItem> result) {
                        aisleDropdown.setItems(result);
                    }
                });


                aisleDropdown.addValueChangeHandler(event -> {
                    saveLocationButton.setEnabled(true);
                    shelfDropdown.clear();
                    getProductLocationRefences(aisleDropdown, rackDropdown);
                });

                rackDropdown.addValueChangeHandler(event -> {
                    saveLocationButton.setEnabled(true);
                    getProductLocationRefences(rackDropdown, shelfDropdown);
                });

                shelfDropdown.addValueChangeHandler(event -> saveLocationButton.setEnabled(true));

                saveLocationButton.addClickHandler(event -> {
                    WarehouseLocationItem productLocation = new WarehouseLocationItem();
//                productLocation.setObjectID(objectId);
                    productLocation.setAisleItem(aisleDropdown.getSelectedItem());
                    productLocation.setRackItem(rackDropdown.getSelectedItem());
                    productLocation.setShelfItem(shelfDropdown.getSelectedItem());
                    productLocation.setWarehouseID(warehouseLookUp.getSelectedItemID());

                    AccountingService.App.get().saveWarehouseLocation(productLocation, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            WfmWindow.error(wfmStrings.errorOccurredSavingChanges());
                        }

                        @Override
                        public void onSuccess(Integer result) {
                            productLocationId = result;
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customForms()), Info.Type.INFO);
                            saveLocationButton.setEnabled(false);
                        }
                    });
                });
            }
            Div div = new Div("input-group-append");
            div.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            Div div1 = new Div("input-group-text");
            Style style = div1.getElement().getStyle();
            style.setBackgroundColor("#fff");
            style.setProperty("borderTopLeftRadius", 0, Style.Unit.PX);
            style.setProperty("borderBottomLeftRadius", 0, Style.Unit.PX);
            style.setProperty("borderTopRightRadius", 0, Style.Unit.PX);
            style.setProperty("borderBottomRightRadius", 0, Style.Unit.PX);
            Anchor anchor = new Anchor();
            anchor.addStyleName("ficon--plus");
            anchor.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            div1.add(anchor);
            div.add(div1);
            div.addClickHandler((event) -> {
                if (warehouseLookUp.getSelectedItemID() == null) {
                    WfmWindow.alert(accountingStrings.pleaseSelectWarehouse());
                    return;
                }
                final ProductLocationAddEditDialogBox locationView = new ProductLocationAddEditDialogBox(warehouseLookUp.getSelectedItemID());
                locationView.setUpdateLocationsProvider(() -> setProductLocations(locationView.getObjectID(), warehouseLookUp.getSelectedItemID(), productLocationsDropdown));
            });
            if (item.getWarehouseID() != null) {
                warehouseLookUp.addItem(new SelectItem(item.getWarehouseID(), item.getWarehouseName()));
                setProductLocations(item.getProductLocationID(), item.getWarehouseID(), productLocationsDropdown);
                if (isEnabledDetailedProductLocation && item.getProductLocationID() != null) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setWarehouseID(item.getWarehouseID());
                    AccountingService.App.get().getDetailedProductLocation(filterParameter, new AsyncCallback<WarehouseLocationItem>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                        }

                        @Override
                        public void onSuccess(WarehouseLocationItem warehouseLocationItem) {
                            if (warehouseLocationItem.getAisleItem() != null) {

                                AccountingService.App.get().getProductLocationReference(warehouseLocationItem.getAisleItem().getDescription(), new AsyncCallback<List<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void onSuccess(List<SelectItem> result) {
                                        aisleDropdown.setSelected(warehouseLocationItem.getAisleItem().getId());
                                        rackDropdown.setItems(result);
                                        rackDropdown.setSelected(warehouseLocationItem.getRackItem().getId());
                                    }
                                });
                            }

                            if (warehouseLocationItem.getRackItem() != null) {
                                AccountingService.App.get().getProductLocationReference(warehouseLocationItem.getRackItem().getDescription(), new AsyncCallback<List<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void onSuccess(List<SelectItem> result) {
                                        shelfDropdown.setItems(result);
                                        shelfDropdown.setSelected(warehouseLocationItem.getShelfItem() != null ? warehouseLocationItem.getShelfItem().getId() : null);
                                    }
                                });
                            }



                            productLocationId = warehouseLocationItem.getObjectID();
                        }
                    });
                }
            } else {
                if (inventoryObjectID != null && product.getDefaultItemWarehouse() != null) {
                    warehouseLookUp.addItem(product.getDefaultItemWarehouse());
                    itemDefaultWarehouseRadio.setValue(true);
                } else if (product.getWarehouse() != null) {
                    warehouseLookUp.addItem(product.getWarehouse());
                }
                if (product.getWarehouse() != null) {
                    setProductLocations(item.getProductLocationID(), product.getWarehouse().getId(), productLocationsDropdown);
                }
                if (isEnabledDetailedProductLocation && item.getProductLocationID() != null) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    if (product.getWarehouse() != null) {
                        filterParameter.setWarehouseID(product.getWarehouse().getId());
                    }
                    AccountingService.App.get().getDetailedProductLocation(filterParameter, new AsyncCallback<WarehouseLocationItem>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                        }

                        @Override
                        public void onSuccess(WarehouseLocationItem warehouseLocationItem) {
                            if (warehouseLocationItem.getAisleItem() != null) {
                                aisleDropdown.setSelected(warehouseLocationItem.getAisleItem().getId());
                                AccountingService.App.get().getProductLocationReference(warehouseLocationItem.getAisleItem().getDescription(), new AsyncCallback<List<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void onSuccess(List<SelectItem> result) {
                                        rackDropdown.setItems(result);
                                    }
                                });
                            }

                            if (warehouseLocationItem.getRackItem() != null) {
                                rackDropdown.setSelected(warehouseLocationItem.getRackItem().getId());
                                AccountingService.App.get().getProductLocationReference(warehouseLocationItem.getRackItem().getDescription(), new AsyncCallback<List<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
                                    }

                                    @Override
                                    public void onSuccess(List<SelectItem> result) {
                                        shelfDropdown.setItems(result);
                                    }
                                });
                            }


                            shelfDropdown.setSelected(warehouseLocationItem.getShelfItem() != null ? warehouseLocationItem.getShelfItem().getId() : null);
                            productLocationId = warehouseLocationItem.getObjectID();
                        }
                    });
                }
            }

            if (product.getDefaultItemWarehouse() != null && warehouseLookUp.getSelectedItem() != null) {
                itemDefaultWarehouseRadio.setValue(product.getDefaultItemWarehouse().getName().equals(warehouseLookUp.getSelectedItem().getName()));
            }
            itemDefaultWarehouseRadio.addDoubleClickHandler(clickEvent -> {
                itemDefaultWarehouseRadio.setValue(false);
                product.setDefaultItemWarehouse(null);
            });
            new KpiToolTip(warehouseLookUp, accountingStrings.warehouse());
            new KpiToolTip(productLocationsDropdown, Property.getPluralWithObjectCode(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.locations()));
            widgetsMap.addToLeft("itemDefaultWarehouse", itemDefaultWarehouseRadio);
            widgetsMap.addToLeft("warehouse", warehouseLookUp);
            if (isEnabledDetailedProductLocation) {
                widgetsMap.addToLeft("aisle", aisleDropdown);
                widgetsMap.addToLeft("rack", rackDropdown);
                widgetsMap.addToLeft("shelf", shelfDropdown);
                widgetsMap.addToLeft("saveLocationButton", saveLocationButton);
            } else if (!Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                widgetsMap.addToLeft("productlocation", productLocationsDropdown);
                widgetsMap.addToLeft("addProductLocation", div);
            }

        }

        TextBox quantity = new TextBox();
        quantity.setEnabled(editable);
        quantity.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(quantity, AccountingUtils.customQtyScale);
        quantity.setText(item.getQty() != null ? AccountingUtils.get().formatQty(item.getQty()) : "");
        totalQty = totalQty.add(item.getQty() != null ? item.getQty() : BigDecimal.ZERO);
        quantity.getElement().setAttribute("objectID", item.getObjectID() != null ? item.getObjectID().toString() : "");
        quantity.addKeyUpHandler(event -> calculate());
        new KpiToolTip(quantity, wfmStrings.qtyOnHand());
        widgetsMap.addToCenter("qtyOnHand", quantity);

        if (!item.getQty().equals(BigDecimal.ZERO)) {
            trackBatches.removeFromParent();
            none.removeFromParent();
        }

        TextBox minOrderPoint = new TextBox();
        minOrderPoint.setEnabled(editable);
        minOrderPoint.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(minOrderPoint, AccountingUtils.customQtyScale);

        if (item.getMinReorderPoint() != null) {
            minOrderPoint.setText(AccountingUtils.get().formatQty(item.getMinReorderPoint() != null ? item.getMinReorderPoint() : ONE));
        } else {
            minOrderPoint.setText(null);
        }
        minOrderPoint.addKeyUpHandler(event -> calculate());
        new KpiToolTip(minOrderPoint, accountingStrings.minReorderPoint());
        widgetsMap.addToCenter("minReorderPoint", minOrderPoint);

        ItemSerialPopup itemSerialsPopup = new ItemSerialPopup(inventoryObjectID, quantity);
        itemSerialsPopup.getLink().addStyleName("btn btn--white");
        itemSerialsPopup.getLink().setVisible(inventoryObjectID == null && trackSerialnumber.getValue());
        widgetsMap.addToRight("serialNumber", itemSerialsPopup.getLink());

        ItemAddTrackBatchPopup itemTrackBatchPopup = new ItemAddTrackBatchPopup(inventoryObjectID, quantity);
        itemTrackBatchPopup.setProductName(wfmStrings.create());
        itemTrackBatchPopup.getLink().addStyleName("btn btn--white");
        itemTrackBatchPopup.getLink().setVisible(inventoryObjectID == null && trackBatches.getValue());
        widgetsMap.addToRight("trackBatch", itemTrackBatchPopup.getLink());

        return widgetsMap;
    }

    private void getProductLocationRefences(WfmDropdown aisleDropdown, WfmDropdown rackDropdown) {
        AccountingService.App.get().getProductLocationReference(aisleDropdown.getSelectedItem().getCode(), new AsyncCallback<List<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                WfmWindow.error(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(List<SelectItem> result) {
                rackDropdown.setItems(result);
            }
        });
    }

    private void setProductLocations(final Integer selectedID, Integer warehouseID, final WfmDropdown wfmDropdown) {
        wfmDropdown.clear();
        if (warehouseID != null) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setWarehouseID(warehouseID);
            AccountingService.App.get().getProductLocations(filterParameter, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    wfmDropdown.addItems(result);
                    if (selectedID != null)
                        wfmDropdown.setSelected(selectedID);
                }
            });
        }
    }

    private ProductLocationItem[] getProductLocations(NewProduct product) {
        totalQtyOnHand = ZERO;
        product.setInventoryTrackingEnabled(trackSerialnumber.getValue());
        product.setBatchTrackingEnabled(batchSerialnumber.getValue());
        product.setTrackBatchesEnabled(trackBatches.getValue());
        List<ProductLocationItem> productLocations = new ArrayList<>();
        for (WidgetsMap map : tblInventoryWarehouse.getWidgetsMaps()) {
            TextBox qty = (TextBox) map.getWidget("qtyOnHand");
            TextBox minReorderPoint = (TextBox) map.getWidget("minReorderPoint");

            Integer objectID = null;
            String strObjectID = qty.getElement().getAttribute("objectID");
            if (strObjectID != null && !strObjectID.isEmpty()) {
                objectID = Integer.valueOf(strObjectID);
            }

            ProductLocationItem productLocation = new ProductLocationItem();
            productLocation.setObjectID(objectID);

            if (Utils.isMultiWarehouseEnabled()) {
                WarehouseLookUp warehouseLookUp = (WarehouseLookUp) map.getWidget("warehouse");
                WfmDropdown productLocationDropdown = (WfmDropdown) map.getWidget("productlocation");
                RadioButton itemDefaultWarehouseRadio = (RadioButton) map.getWidget("itemDefaultWarehouse");
                productLocation.setWarehouseID(warehouseLookUp.getSelectedItemID());
                if (isEnabledDetailedProductLocation) {
                    productLocation.setProductLocationID(productLocationId);
                } else if (productLocationDropdown != null) {
                    productLocation.setProductLocationID(productLocationDropdown.getSelectedId());
                }
                if (itemDefaultWarehouseRadio.getValue()) {
                    product.setDefaultItemWarehouse(warehouseLookUp.getSelectedItem());
                }
            }

            if (trackSerialnumber.getValue()) {
                ItemSerialPopup.Link link = (ItemSerialPopup.Link) map.getWidget("serialNumber");
                productLocation.setSerials(link.getSerials());
            }
            if (trackBatches.getValue()) {
                ItemAddTrackBatchPopup.Link link = (ItemAddTrackBatchPopup.Link) map.getWidget("trackBatch");
                productLocation.setTrackBatchItems(link.getTtrackBatches());
            }

            productLocation.setQty(AccountingUtils.get().parseToBigDecimal(qty.getText()));

            if (!minReorderPoint.getText().isEmpty()) {
                productLocation.setMinReorderPoint(AccountingUtils.get().parseToBigDecimal(minReorderPoint.getText()));
            }else {
                productLocation.setMinReorderPoint(null);
            }

            totalQtyOnHand = totalQtyOnHand.add(AccountingUtils.get().parseToBigDecimal(qty.getValue()));
            productLocations.add(productLocation);
        }
        return productLocations.toArray(new ProductLocationItem[0]);
    }

    private void onSerialNumberTrackingChange(boolean enable) {
        for (WidgetsMap map : tblInventoryWarehouse.getWidgetsMaps()) {
            ItemSerialPopup.Link addSerialsLink = (ItemSerialPopup.Link) map.getWidget("serialNumber");
            addSerialsLink.setVisible(enable);
            ItemAddTrackBatchPopup.Link addTrackBtatchesLink = (ItemAddTrackBatchPopup.Link) map.getWidget("trackBatch");
            addTrackBtatchesLink.setVisible(enable);
        }
    }

    private void onTrackBatchChange(boolean enable) {
        for (WidgetsMap map : tblInventoryWarehouse.getWidgetsMaps()) {
            ItemAddTrackBatchPopup.Link addTrackBtatchesLink = (ItemAddTrackBatchPopup.Link) map.getWidget("trackBatch");
            addTrackBtatchesLink.setEntityType(ItemSerialEntityType.OPENING_BALANCE.name());
            addTrackBtatchesLink.setVisible(enable);
        }
    }

    private void calculateAndSetTotalValueToProduct(NewProduct product) {
        product.setAssetAccountID(assetAccountsLookUp.getSelectedItemID());
        product.setTotalValue(AccountingUtils.get().parseToBigDecimal(total.getText()));
        product.setAsOf(asOfDatePicker.getDate() != null ? new DateNonConvertable(asOfDatePicker.getDate()) : null);
    }

    private BigDecimal getTotalValue() {
        BigDecimal total = BigDecimal.ZERO;
        if (this.total.getText() != null && !this.total.getText().isEmpty()) {
            total = AccountingUtils.get().parseToBigDecimal(this.total.getText());
        }

        return total;
    }

    public boolean validateClosePostingPeriod(BigDecimal oldTotal) {
        if (asOfDatePicker.getDate() != null
                && Utils.isInventoryLocked()
                && (DateUtils.getTransactionLockDate().after(asOfDatePicker.getDate())
                || asOfDate != null && DateUtils.getTransactionLockDate().after(asOfDate) && DateUtils.getTransactionLockDate().before(asOfDatePicker.getDate()))) {

            return asOfDate != null && DateUtil.resetTime(asOfDate).equals(DateUtil.resetTime(asOfDatePicker.getDate())) && (oldTotal == null || getTotalValue().compareTo(oldTotal) == 0);
        }
        return true;
    }

    public Div getBatchWidget() {
        return widgetBatch;
    }

    public void setNoneValue(boolean val) {
        none.setValue(val);
        trackSerialnumber.setValue(val);
        batchSerialnumber.setValue(val);
    }

    public void setNoneEnable(boolean val) {
        none.setEnabled(val);
    }

    public void setTrackBatchesValue(boolean val) {
        trackBatches.setValue(val);
        onTrackBatchChange(val);
    }
}
