/*
package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.grayForm.GrayForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.localization.InventoryStrings;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;
import java.util.Date;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/18/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class BuildAssemblyItemView extends View implements Colapse, Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private DatePicker datePicker;
    private ProductLookUp productLookUp;
    private WarehouseLookUp warehouseLookUp;
    private DynamicTable assemblyItemTable;

    private GrayForm grayForm;
    private WfmForm table;
    private WfmForm.Field datePickerField;
    private WfmForm.Field productLookUpField;
    private WfmForm.Field qtyField;
    private WfmForm.Field warehouseField;

    private VerticalPanel pnlFirstPartWrap;
    private VerticalPanel pnlSecondPartWrap;

    private TextBox qtyToBuild;

    private WfmButton2 btnBuild;
    private WfmButton2 btnCancel;
    private Widget[] widgets;

    private NewProduct product;

    private String buildAsseblyItemView = "build_assebly_item_view_";

    public BuildAssemblyItemView() {
        super("buildAssemblyadd", accountingStrings.buildAssembly());
    }

    @Override
    protected Widget onInitialize() {
        grayForm = new GrayForm();
        grayForm.setWidth("50%");
        grayForm.addBookmark(accountingStrings.buildAssembly());

        datePicker = new DatePicker(true);
        datePicker.setDate(new Date());
        datePicker.ensureDebugId(buildAsseblyItemView + "datePicker");

        productLookUp = new ProductLookUp("ASSEMBLY");
        productLookUp.ensureDebugId(buildAsseblyItemView + "productLookUp");

        productLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> loadData(productLookUp.getSelectedItem()));

        qtyToBuild = new TextBox();
        qtyToBuild.ensureDebugId(buildAsseblyItemView + "qtyToBuild");
        qtyToBuild.setWidth("100px");
        qtyToBuild.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(qtyToBuild, AccountingUtils.customQtyScale);
        qtyToBuild.addKeyUpHandler(keyUpEvent -> calculate());

        drawForm();

        add(grayForm);

        return null;
    }

    private void drawForm() {
        table = new WfmForm(new String[]{"50%", "50%"});
        datePickerField = table.addField(wfmStrings.date(), datePicker,true);
        productLookUpField = table.addField(inventoryStrings.selectProduct(), productLookUp, true);
        qtyField = table.addField(accountingStrings.qtyToBuild(), qtyToBuild, true);
        if (Utils.isMultiWarehouseEnabled()) {
            warehouseLookUp = new WarehouseLookUp();
            warehouseLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
            warehouseField = table.addField(inventoryStrings.warehouse(), warehouseLookUp, true);
        }

        pnlFirstPartWrap = grayForm.addInnerPanel();
        pnlFirstPartWrap.add(table);
        pnlFirstPartWrap.setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_LEFT);

        assemblyItemTable = new DynamicTable(getColumns(), false);
        assemblyItemTable.ensureDebugId(buildAsseblyItemView + "assemblyItemTable");
        assemblyItemTable.addRow(getWidgets());

        pnlSecondPartWrap = grayForm.addInnerPanel();
        pnlSecondPartWrap.add(assemblyItemTable);
        pnlSecondPartWrap.setCellHorizontalAlignment(assemblyItemTable, HasHorizontalAlignment.ALIGN_LEFT);

        btnBuild = new WfmButton2(inventoryStrings.build());
        btnBuild.ensureDebugId(buildAsseblyItemView + "btnBuild");
        btnBuild.addClickHandler(event -> {
            btnBuild.setEnabled(false);
            if (validate()) {
                build();
            } else {
                btnBuild.setEnabled(true);
            }
        });

        btnCancel = new WfmButton2(inventoryStrings.cancel(), WfmButton2.BTN_DEFAULT);
        btnCancel.ensureDebugId(buildAsseblyItemView + "btnCancel");
        btnCancel.addClickHandler(event -> closeTab());

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(btnBuild);
        buttonPanel.add(btnCancel);
        buttonPanel.setSpacing(10);

        VerticalPanel pnlButtonsWrap = grayForm.addInnerPanel();
        pnlButtonsWrap.add(buttonPanel);
        pnlButtonsWrap.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_LEFT);

    }

    private boolean validate() {
        boolean isActive = true;
        int errors = 0;
        if (!Validation.validateDate(datePicker, datePickerField, true)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(qtyToBuild, qtyField)) {
            errors++;
        }

        if (!Validation.validateSuggestBoxExist(productLookUp, productLookUpField)) {
            errors++;
        }
        if (Utils.isMultiWarehouseEnabled() && !Validation.validateSuggestBoxExist(warehouseLookUp, warehouseField)) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
        }

        if (product.getAssemblyItems() != null && product.getAssemblyItems().size() > 0) {
            for (AssemblyItem assemblyItem : product.getAssemblyItems()) {
                if (!assemblyItem.isActive()) {
                    isActive = false;
                    break;
                }
            }
            if (!isActive) {
                Info.show(inventoryStrings.errorBuildAssemblyItem(), Info.Type.WARNING);
                return false;
            }
        }

        return errors == 0;
    }

//    private boolean checkForQuantity() {
//        boolean result = true;
//        String qty;
//        for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
//            DynamicTableItem item = assemblyItemTable.getProductItem(i);
//            BigDecimal qtyOnHand = AccountingUtils.get().parseToBigDecimal(((Label) item.getColumnById("qtyOnHand")).getText());
//            BigDecimal qtyNeeded = AccountingUtils.get().parseToBigDecimal(((Label) item.getColumnById("qtyNeeded")).getText());
//            String type = ((Label) item.getColumnById("type")).getText();
//            if (!type.equals(NON_INVENTORY_ITEM_STR) && qtyOnHand.compareTo(qtyNeeded) < 0) {
//                if ("".equals(qtyToBuild.getText())) {
//                    qty = "1";
//                } else {
//                    qty = qtyToBuild.getText();
//                }
//                Info.show("", "You do not have enough stock to build " + qty + " quantity of " + product.getItemName(), Info.Type.WARNING);
//                result = false;
//                break;
//            }
//        }
//
//        return result;
//    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[5];
        columns[0] = new DynamicTableColumn(accountingStrings.item(), "item", 140);
        columns[1] = new DynamicTableColumn(accountingStrings.description(), "description", 160);
        columns[2] = new DynamicTableColumn(accountingStrings.type(), "type", 100);
        columns[3] = new DynamicTableColumn(accountingStrings.qtyOnHand(), "qtyOnHand", 100);
        columns[4] = new DynamicTableColumn(accountingStrings.qtyNeeded(), "qtyNeeded", 100);
        return columns;
    }

    private Widget[] getWidgets() {
        Integer index = 0;
        widgets = new Widget[5];

        Label assemblyItem = new Label();
        widgets[index++] = assemblyItem;

        Label description = new Label();
        widgets[index++] = description;
        Label type = new Label();
        widgets[index++] = type;

        Label qtyOnHand = new Label();
        widgets[index++] = qtyOnHand;

        ExtendedLabel qtyNeeded = new ExtendedLabel();
        widgets[index++] = qtyNeeded;

        return widgets;
    }


    private void calculate() {
        String qty = qtyToBuild.getText();
        if (!"".equals(qty)) {
            BigDecimal qt = AccountingUtils.get().parseToBigDecimal(qty);
            for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
                DynamicTableItem item = assemblyItemTable.getProductItem(i);
                ExtendedLabel qtyLabel = (ExtendedLabel) item.getColumnById("qtyNeeded");
                BigDecimal qtt = qt.multiply(qtyLabel.getOldValue());
                qtyLabel.setText(AccountingUtils.get().formatQty(qtt));
            }
        } else {
            for (int i = 0; i < assemblyItemTable.getRowNumber(); i++) {
                DynamicTableItem item = assemblyItemTable.getProductItem(i);
                ExtendedLabel qtyLabel = (ExtendedLabel) item.getColumnById("qtyNeeded");
                qtyLabel.setText(AccountingUtils.get().formatQty(qtyLabel.getOldValue()));
            }
        }
    }

    private void loadData(SelectItem item) {
        if (item != null && item.getId() != null) {
            final Integer productId = item.getId();
            LoadingPanel.loading(true);
            ProductService.App.get().getProductForBuildAssembly(productId, new AsyncCallback<NewProduct>() {
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
                }
            });
        }
    }

    private void drawAssemblyItems(NewProduct product) {
        assemblyItemTable.clear();
        if (product.getAssemblyItems() != null && product.getAssemblyItems().size() > 0) {
            Integer length = product.getAssemblyItems().size();
            for (int i = 0; i < length; i++) {
                assemblyItemTable.addRow(getWidgets());
                AssemblyItem item = product.getAssemblyItems().get(i);
                DynamicTableItem tableItem = assemblyItemTable.getProductItem(i);
                if (item.getAssemblyItemId() != null) {
                    tableItem.setObjectId(item.getAssemblyItemId());
                }
                Label asItem = (Label) tableItem.getColumnById("item");
                if (item.getProduct() != null) {
                    asItem.setText(item.getProduct().getName());
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

            }
        }
        calculate();
    }

    private void build() {
        AssemblyItem assemblyItem = new AssemblyItem();
        assemblyItem.setWarehouseId(warehouseLookUp != null ? warehouseLookUp.getSelectedItemID() : null);
        assemblyItem.setQuantity(AccountingUtils.get().parseToBigDecimal(qtyToBuild.getText()));
        assemblyItem.setProductId(product.getObjectId());
        assemblyItem.setDate(new DateNonConvertable(datePicker.getDate()));

        AccountingService.App.get().buildAssemblyItem(assemblyItem, new AbstractAsyncCallback<BuildAssemblySaveResult>() {
            @Override
            public void onSuccess(BuildAssemblySaveResult result) {
                LoadingPanel.loading(false);
                if (result.getNotValidatedProducts() != null && result.getNotValidatedProducts().size() > 0) {
                    btnBuild.setEnabled(true);
                    Info.show(accountingMessages.youDoNotHaveEnoughStockToBuild("" + result.getNotValidatedProducts().size(), qtyToBuild.getText(), product.getItemName()), Info.Type.WARNING);
                    return;
                }

                Info.show(accountingStrings.assemblyItemBuildSuccessfully(), Info.Type.INFO);
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, BuildAssemblyItemView.this);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    public class ExtendedLabel extends Label {
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

*/
