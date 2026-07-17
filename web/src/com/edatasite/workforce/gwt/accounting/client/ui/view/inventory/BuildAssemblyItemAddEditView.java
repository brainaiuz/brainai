package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Madrahimov Dilshod
 * Date: 7/05/16
 * Time: 21:26 PM
 */
public class BuildAssemblyItemAddEditView extends FooteredView implements Colapse, Constants, AccountingConstants, AccountingCustomFormConstants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final BuildAssemblyServiceAsync buildAssemblyService = BuildAssemblyService.App.get();

    private DatePicker datePicker;
    private ProductLookUp productLookUp;
    private TextBox qtyToBuild;
    private WarehouseLookUp warehouseLookUp;
    private DynamicTable assemblyItemTable;
    private HashMap<String, Widget> widgetsMap;

    private WfmButton2 btnBuild;

    private NewProduct product;

    private final String buildAsseblyItemView = "build_assebly_item_view_";
    private final String requiredHTML = "<font color='red'>*</font>";


    public BuildAssemblyItemAddEditView() {
        super("buildAssemblyadd", accountingStrings.buildAssembly());
    }

    @Override
    protected Widget onInitialize() {
        initForm();
        initWidgetMap();
        loadData(null, true);
        return null;
    }

    private void loadData(Integer productID, final boolean reloadForm) {
        LoadingPanel.loading(true);
        buildAssemblyService.getProductForBuildAssembly(productID, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;
                drawAssemblyItems(product);
                // Setting product default warehouse
                if (product.getDefaultItemWarehouse() != null) {
                    warehouseLookUp.setSelected(product.getDefaultItemWarehouse());
                }

                if (reloadForm) {
                    HTMLPanel htmlPanel = new WftHTMLPanel(result.getLayoutHTML(), widgetsMap).getContainer();
                    htmlPanel.setStyleName("add-form");
                    htmlPanel.add(createFooter());
                    add(htmlPanel);
                }
            }
        });
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {

            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return BuildAssemblyItemAddEditView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {

        List<Widget> list = new ArrayList<>();

        Div btnBuildWrapper = new Div();
        btnBuildWrapper.add(btnBuild);
        list.add(btnBuildWrapper);

        return list;
    }

    private void initForm() {
        datePicker = new DatePicker(true);
        datePicker.setDate(new Date());
        datePicker.ensureDebugId(buildAsseblyItemView + "datePicker");

        productLookUp = new ProductLookUp("ASSEMBLY");
        productLookUp.ensureDebugId(buildAsseblyItemView + "productLookUp");
        productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> loadData(productLookUp.getSelectedItemID(), false));

        qtyToBuild = new TextBox();
        qtyToBuild.ensureDebugId(buildAsseblyItemView + "qtyToBuild");
        qtyToBuild.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(qtyToBuild, AccountingUtils.systemCalculationScale);
        qtyToBuild.addKeyUpHandler(keyUpEvent -> calculate());

        if (Utils.isMultiWarehouseEnabled()) {
            warehouseLookUp = new WarehouseLookUp();
            warehouseLookUp.ensureDebugId(buildAsseblyItemView + "warehouseLookUp");
        }

        assemblyItemTable = new DynamicTable(getColumns(), true);
        assemblyItemTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                assemblyItemTable.addRow(getWidgets());
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });

        btnBuild = new WfmButton2(accountingStrings.build(), WfmButton2.BTN_PRIMARY);
        btnBuild.ensureDebugId(buildAsseblyItemView + "btnBuild");
        btnBuild.addClickHandler(event -> {
            btnBuild.setEnabled(false);

            if (validate()) {
                validateItemsInStock();
            } else {
                btnBuild.setEnabled(true);
            }
        });

    }

    private void initWidgetMap() {
        widgetsMap = new HashMap<>();


        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), datePicker));

        widgetsMap.put(INPUT_PRODUCT_NAME, new FormGroup(wfmStrings.product(), productLookUp));

        widgetsMap.put(INPUT_ITEM_QTY, new FormGroup(accountingStrings.qtyToBuild(), qtyToBuild));

        if (Utils.isMultiWarehouseEnabled()) {
            widgetsMap.put(INPUT_WAREHOUSE, new FormGroup(accountingStrings.warehouse() + requiredHTML, warehouseLookUp));
        }

//        assemblyItemTable.setStyleName(STYLE_PRODUCT_TABLE);
        widgetsMap.put(INPUT_ITEM_TABLE, assemblyItemTable);

    }


    private boolean validate() {
        boolean isActive = true;
        int errors = 0;
        if (!Validation.validateDate(datePicker, new HTML(""), true)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(qtyToBuild)) {
            errors++;
        }

        if (!Validation.validateLookUpRequired(productLookUp)) {
            errors++;
        }
        if (Utils.isMultiWarehouseEnabled() && !Validation.validateLookUpRequired(warehouseLookUp)) {
            errors++;
        }

        for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = assemblyItemTable.getItem(i);
            ProductLookUp asItemLookUp = (ProductLookUp) tableItem.getColumnById("item");
            ExtendedLabel qtyLabel = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");

            if (asItemLookUp.getSelectedItemID() != null && asItemLookUp.getSelectedItemID().intValue() > 0) {

                if (Utils.isNullOrEmpty(qtyLabel.getText())) {
                    assemblyItemTable.notValid(i, "qtyNeeded");
                    errors++;
                }
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
        }
        if (datePicker.getDate() != null
                && Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(datePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Build", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return errors == 0;
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[Utils.isMultiWarehouseEnabled() ? 6 : 5];
        columns[0] = new DynamicTableColumn(wfmStrings.item(), "item", 150);
        columns[1] = new DynamicTableColumn(wfmStrings.description(), "description", 150);
        columns[2] = new DynamicTableColumn(wfmStrings.type(), "type", 150);
        columns[3] = new DynamicTableColumn(wfmStrings.qtyOnHand(), "qtyOnHand", 100);
        columns[4] = new DynamicTableColumn(accountingStrings.qtyNeeded(), "qtyNeeded", 100);
        if (Utils.isMultiWarehouseEnabled()) {
            columns[5] = new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", 150);
        }
        return columns;
    }

    private Widget[] getWidgets() {
        Integer index = 0;
        Widget[] widgets = new Widget[Utils.isMultiWarehouseEnabled() ? 6 : 5];

        final ProductLookUp itemLookUp = new ProductLookUp(ASSEMBLY_ITEMS);
        itemLookUp.ensureDebugId(PRODUCT + "productLookUp");
        itemLookUp.setWithoutType(ASSEMBLY_ITEM);

        widgets[index++] = itemLookUp;

        Label description = new Label();
        widgets[index++] = description;

        Label type = new Label();
        widgets[index++] = type;

        Label qtyOnHand = new Label();
        widgets[index++] = qtyOnHand;

        ExtendedLabel qtyNeeded = new ExtendedLabel();
        widgets[index++] = qtyNeeded;
        if (Utils.isMultiWarehouseEnabled()) {
            WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
            widgets[index++] = warehouseLookUp;
            warehouseLookUp.getSuggestBox().addSelectionHandler(e -> {
                if (warehouseLookUp.getSelectedItemID() != null) {
                    AccountingService.App.get().getItemQtyByWarehouse(itemLookUp.getSelectedItemID(), warehouseLookUp.getSelectedItemID(), new AsyncCallback<BigDecimal>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(BigDecimal qty) {
                            qtyOnHand.setText(AccountingUtils.get().formatQty(qty));
                        }
                    });
                }
            });
        }

        itemLookUp.getSuggestBox().addSelectionHandler(event -> setItemValues(itemLookUp.getSelectedItem(), widgets));
        return widgets;
    }

    private void setItemValues(SelectItem item, final Widget[] widgets) {
        if (item != null && item.getId() != null) {
            final Integer productId = item.getId();
            ProductService.App.get().getProductBaseData(productId, new LoadingPanelCallback<NewProduct>(assemblyItemTable) {
                @Override
                public void success(NewProduct product) {
                    generateWidgets(product, widgets);
                }
            });
        }
    }

    private void generateWidgets(NewProduct product, Widget[] widgets) {
        ((Label) widgets[1]).setText(product.getDescription());
        ((Label) widgets[2]).setText(product.getTypeName());
        ((Label) widgets[3]).setText(AccountingUtils.get().formatQty(product.getQuantity()));
        ((WarehouseLookUp) widgets[5]).setSelected(product.getDefaultItemWarehouse());


        calculate();
    }


    private void calculate() {
        String qty = qtyToBuild.getText();
        if (!"".equals(qty)) {
            BigDecimal qt = AccountingUtils.get().parseToBigDecimal(qty);
            for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
                DynamicTableItem item = assemblyItemTable.getItem(i);
                ExtendedLabel qtyLabel = (ExtendedLabel) item.getColumnById("qtyNeeded");
                BigDecimal qtt = qtyLabel.getOldValue() != null ? qt.multiply(qtyLabel.getOldValue()) : qt;
                qtyLabel.setText(AccountingUtils.get().formatQty(qtt));
            }
        } else {
            for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
                DynamicTableItem item = assemblyItemTable.getItem(i);
                ExtendedLabel qtyLabel = (ExtendedLabel) item.getColumnById("qtyNeeded");

                if (qtyLabel.getOldValue() != null) {
                    qtyLabel.setText(AccountingUtils.get().formatQty(qtyLabel.getOldValue()));
                }
            }
        }
    }

    private QuantityItem[] getItems() {
        ArrayList<QuantityItem> items = new ArrayList<>();

        for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = assemblyItemTable.getItem(i);
            ProductLookUp asItemLookUp = (ProductLookUp) tableItem.getColumnById("item");
            ExtendedLabel qtyLabel = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");
            WarehouseLookUp warehouse = (WarehouseLookUp) tableItem.getColumnById("warehouse");
            Integer warehouseId = Utils.isMultiWarehouseEnabled() ? warehouse.getSelectedItemID() : null;
            if (warehouseId == null && warehouseLookUp != null) {
                warehouseId = warehouseLookUp.getSelectedItemID();
            }

            if (asItemLookUp.getSelectedItemID() != null && asItemLookUp.getSelectedItemID().intValue() > 0) {
                QuantityItem item = new QuantityItem();
                item.setId(asItemLookUp.getSelectedItemID());
                item.setQuantity(AccountingUtils.get().parseToBigDecimal(qtyLabel.getText()));
                item.setWarehouseID(warehouseId);
                items.add(item);
            }
        }

        return items.toArray(new QuantityItem[]{});
    }

    private void drawAssemblyItems(NewProduct product) {
        assemblyItemTable.clear();

        if (product.getAssemblyItems() != null && product.getAssemblyItems().size() > 0) {
            Integer length = product.getAssemblyItems().size();

            for (int i = 0; i < length; i++) {
                assemblyItemTable.addRow(getWidgets());
                assemblyItemTable.addStyleName("testPest--table-row-bordered");

                AssemblyItem item = product.getAssemblyItems().get(i);
                DynamicTableItem tableItem = assemblyItemTable.getItem(i);

                if (item.getAssemblyItemId() != null) {
                    tableItem.setObjectId(item.getAssemblyItemId());
                }
                ProductLookUp asItemLookUp = (ProductLookUp) tableItem.getColumnById("item");

                if (item.getProduct() != null) {
                    asItemLookUp.setSelected(item.getProduct());
                }
                Label description = (Label) tableItem.getColumnById("description");
                description.setText(item.getDescription());

                Label type = (Label) tableItem.getColumnById("type");

                if (INVENTORY_ITEM.equals(item.getProductType())) {
                    type.setText(INVENTORY_ITEM_STR);
                } else if (NON_INVENTORY_ITEM.equals(item.getProductType())) {
                    type.setText(NON_INVENTORY_ITEM_STR);
                } else if (PRODUCT_KIT.equals(item.getProductType())) {
                    type.setText(PRODUCT_KIT_STR);
                } else if (ASSEMBLY_ITEM.equals(item.getProductType())) {
                    type.setText(ASSEMBLY_ITEM_STR);
                }

                Label qtyOnHand = (Label) tableItem.getColumnById("qtyOnHand");
                qtyOnHand.setText(AccountingUtils.get().formatQty(item.getItemsInStock()));

                ExtendedLabel qtyNeeded = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");
                qtyNeeded.setText(AccountingUtils.get().formatQty(item.getQuantity()));
                qtyNeeded.setOldValue(item.getQuantity());

                if (Utils.isMultiWarehouseEnabled()) {
                    WarehouseLookUp warehouseLookUp = (WarehouseLookUp) tableItem.getColumnById("warehouse");

                    if (item.getProductDefaultWarehouse() != null) {
                        warehouseLookUp.setSelected(item.getProductDefaultWarehouse());
                    }
                }

            }
        }
        calculate();
    }

    private void validateItemsInStock() {
        AssemblyItem assemblyItem = new AssemblyItem();
        assemblyItem.setWarehouseId(warehouseLookUp != null ? warehouseLookUp.getSelectedItemID() : null);
        assemblyItem.setQuantity(AccountingUtils.get().parseToBigDecimal(qtyToBuild.getText()));
        assemblyItem.setProductId(product.getObjectId());
        assemblyItem.setDate(new DateNonConvertable(datePicker.getDate()));
        assemblyItem.setItems(getItems());

        InvoiceService.App.get().validateStockAvailability(assemblyItem.getItems(), null, StockOutFlow.FROM_BUILD_ASSEMBLY, null, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                btnBuild.setEnabled(true);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(SelectItem[] errors) {
                btnBuild.setEnabled(false);

                if (errors == null || errors.length == 0) {
                    isThereServicesIncluded(assemblyItem);
                } else {
                    LoadingPanel.loading(false);
                    btnBuild.setEnabled(true);
                    alertStockItemsMessage(errors);
                }
            }
        });

    }

    private void isThereServicesIncluded(AssemblyItem assemblyItem) {
        ProductService.App.get().hasServicesIncluded(assemblyItem.getItems(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                btnBuild.setEnabled(true);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Boolean hasServicesIncluded) {

                if (!hasServicesIncluded.booleanValue()) {
                    build(assemblyItem);
                } else {
                    applyLiabilityAccount(assemblyItem);
                }
            }
        });
    }

    private void build(AssemblyItem assemblyItem) {
        LoadingPanel.loading(true);
        buildAssemblyService.buildAssemblyItem(assemblyItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                super.onSuccess(result);
                LoadingPanel.loading(false);
                Info.show(accountingStrings.assemblyItemBuildSuccessfully(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, BuildAssemblyItemAddEditView.this);
                closeTab();
            }

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }
        });
    }

    private void applyLiabilityAccount(AssemblyItem assemblyItem) {
        KpiModal accountModal = new KpiModal();
        accountModal.setWidth("400px");
        accountModal.setTitle(accountingStrings.buildAssembly());
        accountModal.add(new HTML("<p>Please note that you need to select an account for service, non-inventory.... type products when building an assembly item</p>"));

        AccountsLookUp accountsList = new AccountsLookUp();
        accountModal.addWidget(accountsList, wfmStrings.account());

        WfmButton2 btnBuild = new WfmButton2(accountingStrings.buildAssembly(), BTN_PRIMARY);
        WfmButton2 btnCancel = new WfmButton2(wfmStrings.cancel());
        accountModal.addButton(btnCancel);
        accountModal.addButton(btnBuild);

        accountModal.open();

        btnCancel.addClickHandler(ch -> accountModal.close());
        btnBuild.addClickHandler(ch -> {
            assemblyItem.setAccount(accountsList.getSelectedItem());
            build(assemblyItem);
            accountModal.close();
        });
    }

    public void alertStockItemsMessage(SelectItem[] items) {

        StringBuilder itemNames = new StringBuilder();
        StringBuilder bookingReservation = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i].getName()).append("\"");
            if (items[i].getDescription() != null && items[i].getDescription().length() > 0) {
                if (i != 0) {
                    bookingReservation.append(", ");
                }
                bookingReservation.append("\"(").append(items[i].getDescription()).append(")\"");
            }
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        messageBox.setWidth(400);
        if (bookingReservation != null && bookingReservation.length() > 0) {
            messageBox.setTitle(WfmStrings.App.get().warning());
            messageBox.setMessage(AccountingMessages.App.get().bookingReservation(itemNames.toString(), bookingReservation.toString()));
        } else {
            messageBox.setTitle(AccountingStrings.App.get().notEnoughQuantity());
            messageBox.setMessage(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(itemNames.toString()));
        }
        messageBox.open();
    }

    public class ExtendedLabel extends TextBox {
        BigDecimal oldValue;

        public ExtendedLabel() {
            super();
        }

        public BigDecimal getOldValue() {
            return oldValue;
        }

        public void setOldValue(BigDecimal oldValue) {
            this.oldValue = oldValue;
        }
    }


    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
