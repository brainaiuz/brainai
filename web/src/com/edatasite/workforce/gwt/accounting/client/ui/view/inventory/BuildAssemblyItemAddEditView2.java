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
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
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
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ASSEMBLY_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT;

public class BuildAssemblyItemAddEditView2 extends CustomForm2 implements Colapse, Constants {
    private static final BuildAssemblyServiceAsync buildAssemblyService = BuildAssemblyService.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private Integer objectId;
    private NewProduct product;
    private AssemblyItem item;

    private Numbering numbering;
    private DatePicker datePicker;
    private ProductLookUp productLookUp;
    private TextBox qtyToBuild;
    private WarehouseLookUp warehouseLookUp;
    private DynamicTable itemsTable;
    private ChosenApproversWidget approver;
    private WfmButton2 submitButton, approveButton, rejectButton;

    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private final String buildAsseblyItemView = "build_assebly_item_view_";

    public BuildAssemblyItemAddEditView2() {
        super("buildAssemblyadd", accountingStrings.buildAssembly());
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BuildAssembly, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                BuildAssemblyItemAddEditView2.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        numbering = new Numbering();
        numbering.setEnabled(false);

        datePicker = new DatePicker(true);
        datePicker.setDate(new Date());
        datePicker.ensureDebugId(buildAsseblyItemView + "datePicker");

        productLookUp = new ProductLookUp("ASSEMBLY");
        productLookUp.ensureDebugId(buildAsseblyItemView + "productLookUp");
        productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> loadData(productLookUp.getSelectedItemID()));

        qtyToBuild = new TextBox();
        qtyToBuild.ensureDebugId(buildAsseblyItemView + "qtyToBuild");
        qtyToBuild.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        qtyToBuild.addKeyUpHandler(keyUpEvent -> calculate());
        Validation.addNumericKeyboardListener(qtyToBuild, AccountingUtils.systemCalculationScale);

        warehouseLookUp = new WarehouseLookUp();
        warehouseLookUp.ensureDebugId(buildAsseblyItemView + "warehouseLookUp");

        itemsTable = new DynamicTable(getColumns(), true);
        itemsTable.ensureDebugId("build_assembly_table");
        itemsTable.addRow(getWidgets());
        itemsTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                itemsTable.addRow(getWidgets());
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {}
        });

        addFields();
        getCustomFieldUtil().drawCustomFields(this, objectId, false);
        show();
    }

    @Override
    protected void addButtons() {
        submitButton = new WfmButton2(accountingStrings.buildAssembly(), BTN_PRIMARY);
        submitButton.addClickHandler(event -> save(BUILD_ASSEMBLY_STATUS_SUBMITTED));
        submitButton.getElement().setId(buildAsseblyItemView + "submit_button");
        addButton(submitButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
        approveButton.setVisible(false);
        approveButton.getElement().setId(buildAsseblyItemView + "approve_button");
        approveButton.addClickHandler(click -> save(Constants.BUILD_ASSEMBLY_STATUS_APPROVED));

        rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        rejectButton.setVisible(false);
        rejectButton.getElement().setId(buildAsseblyItemView + "reject_button");
        rejectButton.addClickHandler(click -> save(Constants.BUILD_ASSEMBLY_STATUS_REJECTED));
    }

    private void addFields() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, numbering, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()));
            numbering.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, numbering, wfmStrings.number());
        }
        if (formPropertyMap != null && formPropertyMap.get(AccountingCustomFormConstants.DATE) != null) {
            addField(AccountingCustomFormConstants.DATE, datePicker, getTitle(formPropertyMap.get(AccountingCustomFormConstants.DATE).isChanged() ?
                    formPropertyMap.get(AccountingCustomFormConstants.DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(AccountingCustomFormConstants.DATE).isRequired()));
            // datePicker.setEnabled(!formPropertyMap.get(AccountingCustomFormConstants.DATE).isDisabled());
        } else {
            addField(AccountingCustomFormConstants.DATE, datePicker, wfmStrings.date());
        }
        if (formPropertyMap != null && formPropertyMap.get(PRODUCT) != null) {
            addField(PRODUCT, productLookUp, getTitle(formPropertyMap.get(PRODUCT).isChanged() ?
                    formPropertyMap.get(PRODUCT).getTitle() : wfmStrings.product(), formPropertyMap.get(PRODUCT).isRequired()));
            // productLookUp.setEnabled(!formPropertyMap.get(PRODUCT).isDisabled());
        } else {
            addField(PRODUCT, productLookUp, wfmStrings.product());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.QUANTITY) != null) {
            addField(CustomFormConstants.QUANTITY, qtyToBuild, getTitle(formPropertyMap.get(CustomFormConstants.QUANTITY).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.QUANTITY).getTitle() : accountingStrings.qtyToBuild(), true));
            // qtyToBuild.setEnabled(!formPropertyMap.get(CustomFormConstants.QUANTITY).isDisabled());
        } else {
            addField(CustomFormConstants.QUANTITY, qtyToBuild, accountingStrings.qtyToBuild());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE) != null) {
            addField(CustomFormConstants.WAREHOUSE, warehouseLookUp, getTitle(formPropertyMap.get(CustomFormConstants.WAREHOUSE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.WAREHOUSE).getTitle() : accountingStrings.warehouse(), formPropertyMap.get(CustomFormConstants.WAREHOUSE).isRequired()));
            warehouseLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.WAREHOUSE).isDisabled());
        } else {
            addField(CustomFormConstants.WAREHOUSE, warehouseLookUp, accountingStrings.warehouse());
        }
        addField(CustomFormConstants.ASSEMBLY_ITEMS, itemsTable, null);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        buildAssemblyService.getBuildAssemblyItem(objectId, new AbstractAsyncCallback<AssemblyItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AssemblyItem result) {
                super.onSuccess(result);
                item = result;
                initButtonsPanel();
                fillFormWithData();
                drawAssemblyItems(item.getNewProduct());
                LoadingPanel.loading(false);
            }
        });
    }

    private void fillFormWithData() {
        numbering.setNumberData(item.getNumberData());
        if (item.getDate() != null) {
            datePicker.setDate(item.getDate().getDate());
        }
        productLookUp.setSelected(item.getAssemblyItem());
        qtyToBuild.setText(item.getQuantity() != null ? item.getQuantity().setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).toString() : null);
        if (isMultiWarehouseEnabled()) {
            warehouseLookUp.setSelected(item.getWareHouseItem());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems());
    }

    private void loadData(Integer productID) {
        LoadingPanel.loading(true);
        buildAssemblyService.getProductForBuildAssembly(productID, new AsyncCallback<NewProduct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(NewProduct result) {
                LoadingPanel.loading(false);
                product = result;
                drawAssemblyItems(product);
                if (product.getDefaultItemWarehouse() != null) {
                    warehouseLookUp.setSelected(product.getDefaultItemWarehouse());
                }
            }
        });
    }

    private void drawAssemblyItems(NewProduct product) {
        if (product != null && product.getAssemblyItems() != null && !product.getAssemblyItems().isEmpty()) {
            itemsTable.clear();
            int length = product.getAssemblyItems().size();

            for (int i = 0; i < length; i++) {
                itemsTable.addRow(getWidgets());

                AssemblyItem item = product.getAssemblyItems().get(i);
                DynamicTableItem tableItem = itemsTable.getItem(i);

                if (item.getAssemblyItemId() != null) {
                    tableItem.setObjectId(item.getAssemblyItemId());
                }

                Label category = (Label) tableItem.getColumnById("category");
                category.setText(item.getCategory());

                ProductLookUp asItemLookUp = (ProductLookUp) tableItem.getColumnById("item");

                if (item.getProduct() != null) {
                    asItemLookUp.setSelected(item.getProduct());
                }
                Label description = (Label) tableItem.getColumnById("description");
                description.setText(item.getDescription());

                Label qtyOnHand = (Label) tableItem.getColumnById("qtyOnHand");
                qtyOnHand.setText(AccountingUtils.get().formatQty(item.getItemsInStock()));

                ExtendedLabel qtyNeeded = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");
                qtyNeeded.setText(AccountingUtils.get().formatQty(item.getQuantity()));
                qtyNeeded.setOldValue(item.getQuantity());

                if (isMultiWarehouseEnabled()) {
                    WarehouseLookUp warehouseLookUp = (WarehouseLookUp) tableItem.getColumnById("warehouse");

                    if (item.getProductDefaultWarehouse() != null) {
                        warehouseLookUp.setSelected(item.getProductDefaultWarehouse());
                    }
                }
            }
            sortItemTable();
            calculate();
        }
    }

    private DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> columns = new ArrayList<>();
        columns.add(new DynamicTableColumn(wfmStrings.category(), "category", 300));
        columns.add(new DynamicTableColumn(wfmStrings.item(), "item", 300));
        columns.add(new DynamicTableColumn(wfmStrings.description(), "description", 300));
        columns.add(new DynamicTableColumn(wfmStrings.qtyOnHand(), "qtyOnHand", 100));
        columns.add(new DynamicTableColumn(accountingStrings.qtyNeeded(), "qtyNeeded", 150));
        if (isMultiWarehouseEnabled()) columns.add(new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", 300));
        return columns.toArray(new DynamicTableColumn[0]);
    }

    private Widget[] getWidgets() {
        int index = 0;
        Widget[] widgets = new Widget[isMultiWarehouseEnabled() ? 6 : 5];

        Label category = new Label();
        category.ensureDebugId(PRODUCT + "category");
        widgets[index++] = category;

        final ProductLookUp itemLookUp = new ProductLookUp(ASSEMBLY_ITEMS);
        itemLookUp.ensureDebugId(PRODUCT + "productLookUp");
        itemLookUp.setWithoutType(ASSEMBLY_ITEM);
        itemLookUp.getSuggestBox().addSelectionHandler(event -> setItemValues(itemLookUp.getSelectedItem(), widgets));
        widgets[index++] = itemLookUp;

        Label description = new Label();
        widgets[index++] = description;

        Label qtyOnHand = new Label();
        widgets[index++] = qtyOnHand;

        ExtendedLabel qtyNeeded = new ExtendedLabel();
        widgets[index++] = qtyNeeded;

        if (isMultiWarehouseEnabled()) {
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
        return widgets;
    }

    private void setItemValues(SelectItem item, final Widget[] widgets) {
        if (item != null && item.getId() != null) {
            final Integer productId = item.getId();
            ProductService.App.get().getProductBaseData(productId, new LoadingPanelCallback<NewProduct>(itemsTable) {
                @Override
                public void success(NewProduct product) {
                    generateWidgets(product, widgets);
                }
            });
        }
    }

    private void sortItemTable() {
        for (int i = 0; i < itemsTable.getRowNumber(); i++) {
            for (int j = 0; j < i; j++) {
                itemsTable.getItem(j).getColumnById("category");
                itemsTable.getItem(j).getColumnById("category");

            }
        }
    }

    private void generateWidgets(NewProduct product, Widget[] widgets) {
        ((Label) widgets[0]).setText(product.getCategoryName());
        ((Label) widgets[2]).setText(product.getDescription());
        ((Label) widgets[3]).setText(AccountingUtils.get().formatQty(product.getQuantity()));
        ((WarehouseLookUp) widgets[5]).setSelected(product.getDefaultItemWarehouse());
        calculate();
    }

    private void calculate() {
        String qty = qtyToBuild.getText();
        if (!"".equals(qty)) {
            BigDecimal qt = AccountingUtils.get().parseToBigDecimal(qty);
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem item = itemsTable.getItem(i);
                ExtendedLabel qtyLabel = (ExtendedLabel) item.getColumnById("qtyNeeded");
                BigDecimal qtt = qtyLabel.getOldValue() != null ? qt.multiply(qtyLabel.getOldValue()) : qt;
                qtyLabel.setText(AccountingUtils.get().formatQty(qtt));
            }
        } else {
            for (int i = 0; i < itemsTable.getRowNumber(); i++) {
                DynamicTableItem item = itemsTable.getItem(i);
                ExtendedLabel qtyLabel = (ExtendedLabel) item.getColumnById("qtyNeeded");
                if (qtyLabel.getOldValue() != null) {
                    qtyLabel.setText(AccountingUtils.get().formatQty(qtyLabel.getOldValue()));
                }
            }
        }
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initButtonsPanel() {
        if (item.isApproveProcessEnabled()) {
            approver = new ChosenApproversWidget(RelationItem.TYPE_BUILD_ASSEMBLY, item.getApprover() != null ? objectId : null);
            addField(CustomFormConstants.APPROVERS, approver, wfmStrings.approvers());
            addRightButton(approveButton);
            addRightButton(rejectButton);

            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, BuildAssemblyItemAddEditView2.this, (sender, args) -> {
                if (approver.getFirstApproverLookUp() != null) {
                    approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        Integer itemId = item != null ? item.getId() : null;
                        Integer currentUserId = Utils.getUserID();
                        if (currentUserId.equals(itemId)) {
                            approveButton.setVisible(true);
                            rejectButton.setVisible(true);
                            submitButton.setVisible(false);
                        } else {
                            submitButton.setVisible(true);
                            approveButton.setVisible(false);
                            rejectButton.setVisible(false);
                        }
                    });
                    if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                            approveButton.setVisible(true);
                            rejectButton.setVisible(true);
                            submitButton.setVisible(false);
                        } else {
                            approveButton.setVisible(false);
                            rejectButton.setVisible(false);
                            submitButton.setVisible(true);
                        }
                    }
                    if (approver != null && item != null && item.getAssemblyItemId() != null) {
                        approver.setEnabled(BUILD_ASSEMBLY_STATUS_APPROVED.equals(item.getStatusCode()) || BUILD_ASSEMBLY_STATUS_REJECTED.equals(item.getStatusCode()));
                    }
                }
            });
        }
    }

    private boolean validate() {
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
        if (isMultiWarehouseEnabled() && !Validation.validateLookUpRequired(warehouseLookUp)) {
            errors++;
        }
        if (formPropertyMap != null && formPropertyMap.get(APPROVERS) != null && formPropertyMap.get(APPROVERS).isRequired() && item != null && item.isApproveProcessEnabled() && !approver.isValid()) {
            errors++;
        }

        for (int i = 0; i < itemsTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = itemsTable.getItem(i);
            ProductLookUp asItemLookUp = (ProductLookUp) tableItem.getColumnById("item");
            ExtendedLabel qtyLabel = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");

            if (asItemLookUp.getSelectedItemID() != null && asItemLookUp.getSelectedItemID().intValue() > 0) {
//                String qtyText = qtyLabel.getText();
//                double qty = 0.0;
//                if (!Utils.isNullOrEmpty(qtyText)) {
//                    try {
//                        qty = Double.parseDouble(qtyText);
//                    } catch (NumberFormatException e) {
//                        qty = 0.0;
//                    }
//                }
//
//                if (qty <= 0) {
//                    itemsTable.notValid(i, "qtyNeeded");
//                    errors++;
//                }
                if (Utils.isNullOrEmpty(qtyLabel.getText())) {
                    itemsTable.notValid(i, "qtyNeeded");
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

    private void enableButtons(boolean enable) {
        submitButton.setEnabled(enable);
        approveButton.setEnabled(enable);
        rejectButton.setEnabled(enable);
    }

    private void save(String statusCode) {
        enableButtons(false);
        if (!validate()) {
            enableButtons(true);
            return;
        }
        setValues(statusCode);
        validateItemsInStock(item);
    }

    private void setValues(String statusCode) {
        item.setId(this.objectId);
        item.setWarehouseId(warehouseLookUp != null ? warehouseLookUp.getSelectedItemID() : null);
        item.setQuantity(AccountingUtils.get().parseToBigDecimal(qtyToBuild.getText()));
        item.setProductId(productLookUp.getSelectedItemID());
        item.setProduct(productLookUp.getSelectedItem());
        item.setDate(new DateNonConvertable(datePicker.getDate()));
        item.setNumberData(numbering.getNumberData(false));
        item.setItems(getItems());
        item.setStatusCode(statusCode);
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        if (item.isApproveProcessEnabled()) {
            item.setApprovers(approver.getChosenApprovers());
        }
    }

    private QuantityItem[] getItems() {
        ArrayList<QuantityItem> items = new ArrayList<>();
        for (int i = 0; i < itemsTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = itemsTable.getItem(i);
            Label category = (Label) tableItem.getColumnById("category");
            ProductLookUp asItemLookUp = (ProductLookUp) tableItem.getColumnById("item");
            ExtendedLabel qtyLabel = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");
            Label description = (Label) tableItem.getColumnById("description");
            WarehouseLookUp warehouse = (WarehouseLookUp) tableItem.getColumnById("warehouse");
            Integer warehouseId = isMultiWarehouseEnabled() ? warehouse.getSelectedItemID() : null;
            if (warehouseId == null && warehouseLookUp != null) warehouseId = warehouseLookUp.getSelectedItemID();
            if (asItemLookUp.getSelectedItemID() != null && asItemLookUp.getSelectedItemID().intValue() > 0) {
                QuantityItem item = new QuantityItem();
                item.setId(asItemLookUp.getSelectedItemID());
                item.setCategory(category.getText());
                item.setQuantity(AccountingUtils.get().parseToBigDecimal(qtyLabel.getText()));
                item.setDescription(description.getText());
                item.setWarehouseID(warehouseId);
                items.add(item);
            }
        }
        return items.toArray(new QuantityItem[]{});
    }

    private void validateItemsInStock(AssemblyItem assemblyItem) {
        InvoiceService.App.get().validateStockAvailability(assemblyItem.getItems(), null, StockOutFlow.FROM_BUILD_ASSEMBLY, null, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButtons(true);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(SelectItem[] errors) {
                enableButtons(false);
                if (errors == null || errors.length == 0) {
                    isThereServicesIncluded(assemblyItem);
                } else {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                    alertStockItemsMessage(errors);
                }
            }
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
            if (items[i].getDescription() != null && !items[i].getDescription().isEmpty()) {
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

    private void isThereServicesIncluded(AssemblyItem assemblyItem) {
        productService.hasServicesIncluded(assemblyItem.getItems(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButtons(true);
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

    private void applyLiabilityAccount(AssemblyItem assemblyItem) {
        KpiModal accountModal = new KpiModal();
        accountModal.setWidth("400px");
        accountModal.setTitle(accountingStrings.buildAssembly());
        accountModal.add(new HTML("<p>Please note that you need to select an account for service, non-inventory.... type products when building an assembly item</p>"));

        WfmButton2 btnBuild = new WfmButton2(accountingStrings.buildAssembly(), BTN_PRIMARY);
        WfmButton2 btnCancel = new WfmButton2(wfmStrings.cancel());

        AccountsLookUp accountsList = new AccountsLookUp();
        accountModal.addWidget(accountsList, wfmStrings.account());
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

    private void build(AssemblyItem assemblyItem) {
        LoadingPanel.loading(true);
        buildAssemblyService.buildAssemblyItem(assemblyItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                super.onSuccess(result);
                LoadingPanel.loading(false);
                Info.show(accountingStrings.assemblyItemBuildSuccessfully(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, BuildAssemblyItemAddEditView2.this);
                closeTab();
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                enableButtons(true);
            }
        });
    }

    private boolean isMultiWarehouseEnabled() {
        return Utils.isMultiWarehouseEnabled();
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
        return LayoutRPC.BUILD_ASSEMBLY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public static class ExtendedLabel extends TextBox {
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
