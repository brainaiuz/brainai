package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddUnitMeasurementView;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BillOfMaterialItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.grayForm.GrayForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartMeasurementsLookUp;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hurshid on 11/14/2017.
 */
public class BillOfMaterialView extends FooteredView implements Constants, ItemTableConstants, PermissionConstants, FittedContent {

    private static final ProjectServiceAsync projectService = ProjectService.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final BigDecimal ZERO = new BigDecimal("0.00");

    public BillOfMaterialView(Integer projectID) {
        super("billofmaterial", wfmStrings.billOfMaterials());
        this.projectID = projectID;
    }

    private final Integer projectID;
    private DynamicTable productTable;
    private TotalTable totalTable;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private final List<Integer> selected = new ArrayList<>();
    private WfmButton2 btnDraft, submitToManager, approve, reject, requestForPurchase, editButton;
    private HorizontalPanel productPanel;
    private FlexPanel totalPanel;
    private boolean editMode = true;
    private int count = 0;
    private String statusCode;
    private final boolean isRequester = Utils.hasPermission(BILL_OF_MATERIAL_REQUEST_MATERIALS);
    private final MaterialPanel panelContainer = new MaterialPanel("box-radius wfmForm-container--custom");

    @Override
    protected Widget onInitialize() {

        btnDraft = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        btnDraft.addClickHandler(click -> save(DRAFT, null));
        btnDraft.setVisible(false);

        submitToManager = new WfmButton2(wfmStrings.submitToManager(), WfmButton2.BTN_PRIMARY);
        submitToManager.addClickHandler(click -> save(SUBMITTED_TO_MANAGER, null));
        submitToManager.setVisible(false);

        approve = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
        approve.addClickHandler(click -> save(APPROVED, null));
        approve.setVisible(false);

        reject = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        reject.setVisible(false);
        reject.addClickHandler(click -> {
            final GrayForm grayForm = new GrayForm();
            grayForm.noteShell(wfmStrings.rejectionReason(), true, false, null);
            grayForm.addHistoryPanel(false);
            Command noteListener = () -> save(REJECTED, grayForm.getHistory().getComment());
            grayForm.setNoteListener(noteListener);
        });

        requestForPurchase = new WfmButton2(projectStrings.convertToRFP(), WfmButton2.BTN_PRIMARY);
        requestForPurchase.setVisible(false);
        requestForPurchase.addClickHandler(click -> {
            if (Utils.isAccountingSetup()) {
                saveRequested();
            } else {
                Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
            }
        });
        requestForPurchase.setEnabled(false);

        editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE);
        editButton.setVisible(false);
        editButton.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            projectService.unfreezeBOM(projectID, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(String status) {
                    LoadingPanel.loading(false);
                    statusCode = status;
                    editMode = true;
                    showHideButtons();
                    reloadProductTable();
                }
            });
        });


        productPanel = new HorizontalPanel();
        productPanel.setWidth("100%");

        totalPanel = new FlexPanel();
        totalPanel.setWidth("100%");
        projectService.getBillOfItemsStatus(projectID, new AbstractAsyncCallback<String[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(String[] status) {
                LoadingPanel.loading(false);
                statusCode = status[0];
                editMode = (DRAFT.equals(statusCode) || REJECTED.equals(statusCode)) && Utils.hasPermission(BILL_OF_MATERIAL_CREATE_PROJECT_PLAN);
                showHideButtons();
                drawProductTable();

                totalTable = new TotalTable();
                GColumn cTotalTable = new GColumn(GColumnEnum.COL_3, totalTable);
                cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);
                cTotalTable.getElement().setAttribute("style", "margin-top: 50px;");
                totalPanel.add(cTotalTable);
                calculate(null);
            }
        });
        panelContainer.add(productPanel);
        panelContainer.add(totalPanel);
        panelContainer.add(createFooter());
        add(panelContainer);
        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return BillOfMaterialView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();

        Div btnDraftWrapper = new Div();
        btnDraftWrapper.add(btnDraft);
        rightWidgets.add(btnDraftWrapper);

        Div editButtonWrapper = new Div();
        editButtonWrapper.add(editButton);
        rightWidgets.add(editButtonWrapper);

        Div submitToManagerWrapper = new Div();
        submitToManagerWrapper.add(submitToManager);
        rightWidgets.add(submitToManagerWrapper);

        Div requestForPurchaseWrapper = new Div();
        requestForPurchaseWrapper.add(requestForPurchase);
        rightWidgets.add(requestForPurchaseWrapper);

        Div approveWrapper = new Div();
        approveWrapper.add(approve);
        rightWidgets.add(approveWrapper);

        Div rejectWrapper = new Div();
        rejectWrapper.add(reject);
        rightWidgets.add(rejectWrapper);

        return rightWidgets;
    }

    private void saveRequested() {
        if (!validateRequestQty()) {
            return;
        }
        ArrayList<BillOfMaterialItem> items = getRequestedData();
        if (items.isEmpty()) {
            Info.show(wfmMessages.pleaseMakeSureIsNotZero("Request qty"), Info.Type.WARNING);
            return;
        }
        enableButtons(false);
        LoadingPanel.loading(true);
        projectService.saveRequestedBillOfMaterial(projectID, items, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                enableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            public void success(final Integer _objectId) {
                enableButtons(true);
                deselectAllCheckBoxes();
                LoadingPanel.loading(false);
                if (Constants.SUCCESS == _objectId) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|add/add/" + +projectID + "/billOfMaterials");
                }
                closeTab();
            }
        });
    }

    private void deselectAllCheckBoxes() {
        for (int i = 0; i < productTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = productTable.getItem(i);
            KpiCheckBox checkBox = (KpiCheckBox) tableItem.getColumnById(ItemTableConstants.CHECKBOX);
            if (checkBox.getValue()) {
                TextBox requestQty = (TextBox) tableItem.getColumnById(ItemTableConstants.REQUEST_QTY);
                requestQty.setEnabled(false);
                checkBox.setValue(false);
            }
        }
        requestForPurchase.setEnabled(false);
        count = 0;
    }

    private ArrayList<BillOfMaterialItem> getRequestedData() {
        ArrayList<BillOfMaterialItem> list = new ArrayList<>();
        for (int i = 0; i < productTable.getRowNumber(); i++) {
            BillOfMaterialItem item = new BillOfMaterialItem();
            DynamicTableItem tableItem = productTable.getItem(i);
            KpiCheckBox checkBox = (KpiCheckBox) tableItem.getColumnById(CHECKBOX);
            if (checkBox.getValue()) {
                TextBox requestQty = (TextBox) tableItem.getColumnById(REQUEST_QTY);
                item.setRequestedQqty(requestQty.getValue() != null ? new BigDecimal(requestQty.getValue().replace(",", "").replace(" ", "")) : BigDecimal.ZERO);
                item.setObjectID(tableItem.getEntityID());
                if (BigDecimal.ZERO.compareTo(item.getRequestedQqty()) != 0) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    private void showHideButtons() {

        btnDraft.setVisible(!REJECTED.equals(statusCode) && editMode && Utils.hasPermission(BILL_OF_MATERIAL_CREATE_PROJECT_PLAN));
        submitToManager.setVisible(editMode && Utils.hasPermission(BILL_OF_MATERIAL_SUBMIT_TO_MANAGER));
        requestForPurchase.setVisible(APPROVED.equals(statusCode) && isRequester);
        approve.setVisible(SUBMITTED_TO_MANAGER.equals(statusCode) && Utils.hasPermission(BILL_OF_MATERIAL_APPROVE_REJECT));
        reject.setVisible(SUBMITTED_TO_MANAGER.equals(statusCode) && Utils.hasPermission(BILL_OF_MATERIAL_APPROVE_REJECT));
        editButton.setVisible(APPROVED.equals(statusCode) && Utils.hasPermission(BILL_OF_MATERIAL_EDIT));
    }

    private void drawTotalTable(BigDecimal subTotal, String status) {

        if (totalTable != null) {
            totalTable.clear();
            totalTable.addWidgetsInARow(new HTML(wfmStrings.subtotal()), new HTML(AccountingUtils.get().formatPrice(subTotal)));
            totalTable.addWidgetsInARow(new HTML(wfmStrings.status()), new HTML(status != null && status.length() > 0 ? status : wfmStrings.pending()));
        }

    }

    private void drawProductTable() {
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.BILL_OF_MATERIALS_ITEM, new AbstractAsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {
                if (result != null) {
                    for (ColumnConfigs cc : result) {
                        if (!isRequester && (REQUESTED_BEFORE.equals(cc.getCode()) || REQUEST_QTY.equals(cc.getCode()) || CHECKBOX.equals(cc.getCode()))) {
                            continue;
                        }
                        columnsMap.put(cc.getCode(), cc);
                    }
                }

                productTable = new DynamicTable(getColumns(), null, "", editMode);
                productTable.getElement().getStyle().setMarginTop(25, Style.Unit.PX);
                productTable.addStyleName("tbl-billOfMaterials");
                productTable.setBorderWidth(0);
                productTable.addListener(new AddListener() {
                    public void plusClicked(int rowId) {
                        productTable.insertRow(rowId + 1, getWidgets(new BillOfMaterialItem(), rowId + 1));
                    }

                    @Override
                    public void minusClicked(int rowId, Integer objectId) {
                        selected.remove(objectId);
                        calculate(null);
                    }
                });
                productTable.addRow(getWidgets(new BillOfMaterialItem(), 0));
                fillBillOfMaterialItemsWithInventory();
                productPanel.add(productTable);
            }
        });
    }


    public void calculate(String status) {
        if (productTable != null) {
            BigDecimal subTotalAmount = BigDecimal.ZERO;
            for (int rowID = 0; rowID < productTable.getRowNumber(); rowID++) {
                DynamicTableItem dynamicTableItem = productTable.getItem(rowID);
                BigDecimal subtotal = BigDecimal.ZERO;
                TextBox txtPrice = null;
                if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
                    txtPrice = (TextBox) dynamicTableItem.getColumnById(ItemTableConstants.UNITPRICE);
                }
                TextBox txtQty = null;
                if (columnsMap.containsKey(ItemTableConstants.QTY)) {
                    txtQty = (TextBox) dynamicTableItem.getColumnById(ItemTableConstants.QTY);
                    if (txtQty.getValue() != null && !txtQty.getValue().isEmpty() && txtPrice != null && txtPrice.getValue() != null && !txtPrice.getValue().isEmpty()) {
                        subtotal = subtotal.add(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue().replace(" ", ""))
                                .multiply(AccountingUtils.get().parseToBigDecimal(txtQty.getValue().replace(" ", ""))));
                    }
                }

                if (columnsMap.containsKey(ItemTableConstants.NET_AMT)) {
                    TextBox netAmount = (TextBox) dynamicTableItem.getColumnById(ItemTableConstants.NET_AMT);
                    if (txtQty.getValue() != null && !txtQty.getValue().isEmpty() && txtPrice != null && txtPrice.getValue() != null && !txtPrice.getValue().isEmpty()) {
                        BigDecimal netAmountBigDecimal = AccountingUtils.get().parseToBigDecimal(txtPrice.getValue().replace(" ", "")).multiply(AccountingUtils.get().parseToBigDecimal(txtQty.getValue().replace(" ", "")));
                        netAmount.setValue(AccountingUtils.get().formatPrice(netAmountBigDecimal));
                    }
                }

                subTotalAmount = subTotalAmount.add(subtotal);
            }
            drawTotalTable(subTotalAmount, status);
        }
    }

    private void fillBillOfMaterialItemsWithInventory() {
        LoadingPanel.loading(true);
        projectService.fillBillOfMaterialItemsWithInventory(projectID, new LoadingPanelCallback<BillOfMaterialItem[]>(productTable, wfmStrings.pleaseWait()) {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(BillOfMaterialItem[] billOfMaterialItems) {
                setValues(billOfMaterialItems);
                requestForPurchase.setEnabled(false);
                LoadingPanel.loading(false);
            }
        });
    }

    private void setValues(BillOfMaterialItem[] items) {
        if (items != null && items.length > 0) {
            productTable.clear();
            int i = 0;
            for (BillOfMaterialItem item : items) {
                selected.add(item.getItemID());
                productTable.addRow(getWidgets(item, i));
                productTable.getItem(i).setEntityID(item.getObjectID());
                productTable.getItem(i).setObjectId(item.getItemID());
                i++;
            }
            calculate(items[0].getStatus());
        }
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[columnsMap.keySet().size()];
        int i = 0;
        int size = columnsMap.size() > 3 ? 1000 / columnsMap.size() : 1300 / columnsMap.size();
        for (String cc : columnsMap.keySet()) {
            if (ItemTableConstants.PRODUCT.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 400 : size);
            }else if (ItemTableConstants.DESCRIPTION.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 300 : size);
            } else if (REQUESTED_BEFORE.equals(cc) || REQUEST_QTY.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 50 : size);
            } else if (ItemTableConstants.QTY.equals(cc) || ItemTableConstants.MEASUREMENT.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 50 : size);
            } else if (ItemTableConstants.UNITPRICE.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 70 : size, Constants.RIGHT_ALIGN_CELL);
            } else if (ItemTableConstants.CLIENT.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 140 : size);
            } else if (ItemTableConstants.QTY_ON_HAND.equals(cc)) {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, columnsMap.size() > 3 ? 70 : size);
            } else {
                columns[i++] = new DynamicTableColumn(columnsMap.get(cc).getTitle(), cc, size);
            }
        }
        columns[1].setStyle(Constants.CENTER_ALIGN_CELL);
        return columns;
    }

    private Widget[] getWidgets(final BillOfMaterialItem item, Integer row) {
        int index = 0;
        final Widget[] widgets = new Widget[productTable.getCellCount(0) - 1];
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                final ProductLookUp product = new ProductLookUp(RECEIVED);
                product.setEnabled(editMode);
                product.setValueNotEmptyMeansSelected(true);
                product.getSuggestBox().getElement().getStyle().setPaddingTop(3, Style.Unit.PX);
                product.getSuggestBox().getElement().getStyle().setPaddingBottom(3, Style.Unit.PX);
                if (editMode) {
                    product.setWidth("100%");
                    product.getSuggestBox().setWidth("100%");
                    product.addStyleName("lookUp-moveRight");
                } else {
                    product.addStyleName(DEFAULT_WIDTH);
                }

                if (item.getItemID() != null) {
                    if (item.getItemNumber() != null && !"".equals(item.getItemNumber())) {
                        product.addItem(new SelectItem(item.getItemID(), item.getItemNumber() + "->" + item.getItemName()));
                    } else {
                        product.addItem(new SelectItem(item.getItemID(), item.getItemName()));
                    }
                    product.setSelected(item.getItemID());
                } else if (item.getItemName() != null) {
                    product.getTextBox().setText(item.getItemName());
                }
                product.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                    if (selected.contains(product.getSelectedItemID())) {
                        Info.show("This product was selected", Info.Type.WARNING);
                        product.clearAndClearItems();
                        product.refreshOracle(true);
                    } else {
                        productTable.getItem(row).setObjectId(product.getSelectedItemID());
                        selected.add(product.getSelectedItemID());
                        setItemValues(product.getSelectedItem(), widgets);
                    }
                });
                product.setTitle(columnCode);
                widgets[index++] = product;
            } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                TextArea txtDescription = new TextArea();
                txtDescription.setEnabled(editMode);
                txtDescription.setSize("100%", "40px");
                txtDescription.setValue(item.getDescription());
                txtDescription.setTitle(columnCode);
                Div divPanel = new Div("input-field");
                divPanel.add(txtDescription);
                widgets[index++] = divPanel;
            } else if (ItemTableConstants.QTY.equals(columnCode)) {
                TextBox quantity = new TextBox();
                quantity.setEnabled(editMode);
                quantity.setWidth("100%");
                addHandler(quantity);
                quantity.addKeyboardListener(new KeyboardListenerAdapter() {
                    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                        calculate(null);
                    }
                });
                Validation.addNumericKeyboardListener(quantity, 2);
                if (item.getQty() != null) {
                    quantity.setValue(item.getQty().toString());
                }
                quantity.setTitle(columnCode);
                widgets[index++] = quantity;
            } else if (ItemTableConstants.MEASUREMENT.equals(columnCode)) {
                final SmartMeasurementsLookUp measurementsLookUp = new SmartMeasurementsLookUp();
                measurementsLookUp.setEnabled(editMode);
                measurementsLookUp.addStyleName("lookUp-moveRight");
                measurementsLookUp.getSuggestBox().setWidth("100%");
                measurementsLookUp.setLinkCommand(() -> {
                    ObjectCommand command = item1 -> measurementsLookUp.addMeasurementUnit((SelectItem) item1);
                    new AddUnitMeasurementView(null, command);
                });
                measurementsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> measurementsLookUp.islink());
                if (item.getUnitMeasurement() != null) {
                    measurementsLookUp.setSelected(item.getUnitMeasurement());
                    measurementsLookUp.initMeasurementUnits(new SelectItem[]{item.getUnitMeasurement()});
                }
                measurementsLookUp.setTitle(columnCode);
                widgets[index++] = measurementsLookUp;
            } else if (ItemTableConstants.UNITPRICE.equals(columnCode)) {
                final TextBox txtPrice = new TextBox();
                txtPrice.setEnabled(editMode);
                txtPrice.setWidth("100%");
                addHandler(txtPrice);
                txtPrice.addKeyboardListener(new KeyboardListenerAdapter() {
                    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                        calculate(null);
                    }
                });

                Validation.addNumericKeyboardListener(txtPrice, 2);
                if (item.getPrice() != null) {
                    txtPrice.setValue(item.getPrice().toString());
                    txtPrice.setLayoutData(item.getPrice());
                }
                txtPrice.setTitle(columnCode);
                widgets[index++] = txtPrice;
            } else if (REQUESTED_BEFORE.equals(columnCode) && isRequester) {
                Label requestedAmount = new Label();

                if (item.getRequestedQqty() != null) {
                    requestedAmount.setLayoutData(item.getRequestedQqty());
                    requestedAmount.setText(item.getRequestedQqty().toString());
                }
                requestedAmount.setTitle(columnCode);
                widgets[index++] = requestedAmount;
            } else if (REQUEST_QTY.equals(columnCode) && isRequester) {
                TextBox requestAmount = new TextBox();
                requestAmount.setLayoutData(item.getQty().subtract(item.getRequestedQqty())); //remaining qty (qty-requested)
                requestAmount.setEnabled(false);
                requestAmount.setWidth("100%");
                requestAmount.addBlurHandler(handler -> {
                    BigDecimal remaining = (BigDecimal) requestAmount.getLayoutData();
                    BigDecimal req = requestAmount.getValue() != null ? new BigDecimal(requestAmount.getValue().replace(",", "").replace(" ", "")) : ZERO;
                    if (remaining.compareTo(req) < 0) {
                        Info.show(wfmStrings.requestQtyShouldBeLessThanPlannedQty(), Info.Type.WARNING);
                    }
                });
                Validation.addNumericKeyboardListener(requestAmount, 2);
                if (item.getRequestedQqty() != null) {
                    requestAmount.setValue(item.getQty().subtract(item.getRequestedQqty()).toString());
                }
                requestAmount.setTitle(columnCode);
                widgets[index++] = requestAmount;
            } else if (CHECKBOX.equals(columnCode) && isRequester) {
                KpiCheckBox checkBox = new KpiCheckBox();
                checkBox.setEnabled(APPROVED.equals(statusCode) && isRequester);
                checkBox.setTitle(columnCode);
                checkBox.setLayoutData(row);
//                checkBox.setWidth("auto"); not working fixed from css only
                widgets[index++] = checkBox;
                checkBox.addValueChangeHandler(valueChangeEvent -> {
                    count = valueChangeEvent.getValue() ? count + 1 : count - 1;
                    requestForPurchase.setEnabled(count > 0);

                    DynamicTableItem tableItem = productTable.getItem((Integer) checkBox.getLayoutData());
                    TextBox requst = (TextBox) tableItem.getColumnById(REQUEST_QTY);
                    requst.setEnabled(valueChangeEvent.getValue());
                });
            } else if (ItemTableConstants.NET_AMT.equals(columnCode)) {
                final TextBox netAmount = new TextBox();
                netAmount.setEnabled(false);
                netAmount.setWidth("100%");
                if (item.getQty() != null && item.getPrice() != null) {
                    BigDecimal netAmountBigDecimal = item.getPrice().multiply(item.getQty());
                    netAmount.setValue(AccountingUtils.get().formatPrice(netAmountBigDecimal));
                }
                widgets[index++] = netAmount;
            } else if (QTY_ON_HAND.equals(columnCode) && isRequester) {
                TextBox onHand = new TextBox();
                onHand.setEnabled(false);
                onHand.setWidth("100%");
                if (item.getOnHand() != null) {
                    onHand.setValue(item.getOnHand().toString());
                }
                widgets[index++] = onHand;
            }
        }
        return widgets;
    }

    private void addHandler(TextBox textBox) {
        textBox.addFocusHandler(event -> {
            if (("0.00".equals(textBox.getValue()) || "0".equals(textBox.getValue()))) {
                textBox.setValue("");
            }
        });
        textBox.addBlurHandler(event -> {
            if ("".equals(textBox.getValue().trim())) {
                textBox.setValue("0.00");
            }
        });
    }

    private void save(String status, String message) {
        if (!validate(status)) {
            return;
        }
        enableButtons(false);
        BillOfMaterialItem[] items = getObjectData();
        projectService.saveBillOfMaterialItems(projectID, status, items, message, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            public void success(final Integer _objectId) {
                statusCode = status;
                editMode = (DRAFT.equals(statusCode) || REJECTED.equals(statusCode)) && Utils.hasPermission(BILL_OF_MATERIAL_CREATE_PROJECT_PLAN);
                showHideButtons();
                enableButtons(true);
                reloadProductTable();
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.billOfMaterials()), Info.Type.INFO);
            }
        });
    }

    private void reloadProductTable() {
        productPanel.clear();
        drawProductTable();
        calculate(null);
    }

    private void enableButtons(boolean enable) {
        btnDraft.setEnabled(enable);
        submitToManager.setEnabled(enable);
        approve.setEnabled(enable);
        reject.setEnabled(enable);
        requestForPurchase.setEnabled(enable);
    }

    public BillOfMaterialItem[] getObjectData() {
        BillOfMaterialItem[] billOfMaterialItem = new BillOfMaterialItem[productTable.getRowNumber()];
        ArrayList<CompanyCustomFieldItem> resultItemList;
        productTable.resetValidation();
        for (int i = 0; i < productTable.getRowNumber(); i++) {
            billOfMaterialItem[i] = new BillOfMaterialItem();
            resultItemList = new ArrayList<>();
            DynamicTableItem dynamicTableItem = productTable.getItem(i);
            for (String columnCode : columnsMap.keySet()) {
                billOfMaterialItem[i].setObjectID(dynamicTableItem.getEntityID());
                if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                    ProductLookUp product = (ProductLookUp) dynamicTableItem.getColumnById(ItemTableConstants.PRODUCT);
                    if (product.getSelectedItemID() != null && product.getSelectedItem().getName().equals(product.getSuggestBox().getText())) {
                        billOfMaterialItem[i].setItemID(product.getSelectedItemID());
                        if (product.getSelectedItem().getName().contains("->")) {
                            billOfMaterialItem[i].setItemNumber(product.getSelectedItem().getName().split("->")[0].trim());
                            billOfMaterialItem[i].setItemName(product.getSelectedItem().getName().split("->")[1]);
                        } else {
                            billOfMaterialItem[i].setItemName(product.getSelectedItem().getName());
                        }
                    } else if (product.getText() != null) {
                        billOfMaterialItem[i].setItemName(product.getText());
                    }
                } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                    Div divPanel = (Div) dynamicTableItem.getColumnById(ItemTableConstants.DESCRIPTION);
                    TextArea description = (TextArea) divPanel.getWidget(0);
                    billOfMaterialItem[i].setDescription(description.getValue());
                } else if (ItemTableConstants.QTY.equals(columnCode)) {
                    TextBox quantity = (TextBox) dynamicTableItem.getColumnById(ItemTableConstants.QTY);
                    billOfMaterialItem[i].setQty(quantity.getValue() != null ? new BigDecimal(quantity.getValue().replace(",", "").replace(" ", "")) : ZERO);
                } else if (ItemTableConstants.MEASUREMENT.equals(columnCode)) {
                    MeasurementsLookUp measurement = (MeasurementsLookUp) dynamicTableItem.getColumnById(ItemTableConstants.MEASUREMENT);
                    billOfMaterialItem[i].setUnitMeasurement((SelectItem) measurement.getSelectData());
                } else if (ItemTableConstants.UNITPRICE.equals(columnCode)) {
                    TextBox price = (TextBox) dynamicTableItem.getColumnById(ItemTableConstants.UNITPRICE);
                    billOfMaterialItem[i].setPrice(price.getValue() != null ? new BigDecimal(price.getValue().replace(",", "").replace(" ", "")) : ZERO);
                } else if (ItemTableConstants.QTY_ON_HAND.equals(columnCode)) {
                    TextBox onHand = (TextBox) dynamicTableItem.getColumnById(ItemTableConstants.QTY_ON_HAND);
                    String rawValue = onHand.getValue();
                    billOfMaterialItem[i].setOnHand(
                            rawValue != null && !rawValue.trim().isEmpty()
                                    ? new BigDecimal(rawValue.replace(",", "").replace(" ", ""))
                                    : ZERO
                    );
                }
            }
            billOfMaterialItem[i].setItemCustomFields(resultItemList);
        }
        return billOfMaterialItem;
    }

    private void setItemValues(SelectItem item, final Widget[] widgets) {
        if (item != null && item.getId() != null) {
            ProductService.App.get().getProductBaseData(item.getId(), new LoadingPanelCallback<NewProduct>(productTable, wfmStrings.pleaseWait()) {
                public void success(NewProduct product) {
                    for (Widget widget : widgets) {
                        if (ItemTableConstants.PRODUCT.equals(widget.getTitle())) {
                            ProductLookUp productLookUp = ((ProductLookUp) widget);
                            productLookUp.getSuggestBox().setText(productLookUp.getSuggestBox().getText());
                        } else if (ItemTableConstants.DESCRIPTION.equals(widget.getTitle())) {
                            Div divPanel = (Div) widget;
                            ((TextArea) divPanel.getWidget(0)).setText(product.getDescription());
                        } else if (ItemTableConstants.QTY.equals(widget.getTitle())) {
                            ((TextBox) widget).setText(product.getQuantity() != null ? product.getQuantity().toString() : new BigDecimal("0").toString());
                        } else if (ItemTableConstants.MEASUREMENT.equals(widget.getTitle()) && product.getUnitMeasurement() != null) {
                            SmartMeasurementsLookUp umLookUp = ((SmartMeasurementsLookUp) widget);
                            umLookUp.setSelected(product.getUnitMeasurement());
                        } else if (ItemTableConstants.UNITPRICE.equals(widget.getTitle())) {
                            ((TextBox) widget).setText(product.getUnitPrice() != null ? product.getUnitPrice().toString() : new BigDecimal("0").toString());
                            widget.setLayoutData(product.getUnitPrice() != null ? product.getUnitPrice() : new BigDecimal("0"));
                        } else if (ItemTableConstants.QTY_ON_HAND.equals(widget.getTitle())) {
                            ((TextBox) widget).setText(product.getTotalQtyOnHand().toString() != null ? product.getTotalQtyOnHand().toString() : "0");
                        }
                    }
                }
            });
        }
    }

    private boolean validate(String status) {
        int errors = 0;
        productTable.resetValidation();
        for (int rowId = 0; rowId < productTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = productTable.getItem(rowId);
            ProductLookUp product = (ProductLookUp) tableItem.getColumnById(ItemTableConstants.PRODUCT);
            if (product.getSelectedItemID() == null) {
                productTable.notValid(rowId, ItemTableConstants.PRODUCT);
                errors++;
            }

            Label requestedBeforeLabel = (Label) tableItem.getColumnById(ItemTableConstants.REQUESTED_BEFORE);
            BigDecimal requestedBefore = requestedBeforeLabel.getLayoutData() != null ? (BigDecimal) requestedBeforeLabel.getLayoutData() : BigDecimal.ZERO;

            TextBox plannedQtyText = (TextBox) tableItem.getColumnById(ItemTableConstants.QTY);
            BigDecimal planned = plannedQtyText.getValue() != null && !"".equals(plannedQtyText.getValue().trim()) ? new BigDecimal(plannedQtyText.getValue().replaceAll("[\\s,]", "")) : BigDecimal.ZERO;

            if (planned.compareTo(requestedBefore) < 0) {
                productTable.notValid(rowId, ItemTableConstants.QTY);
                Info.show(projectStrings.plannedQtyCanNotBeLessThanUsedQty(), Info.Type.WARNING);
                return false;
            }

            if (SUBMITTED_TO_MANAGER.equals(status)) {

                TextBox plannedQty = (TextBox) tableItem.getColumnById(ItemTableConstants.QTY);
                BigDecimal r = plannedQty.getValue() != null && !"".equals(plannedQty.getValue().trim()) ? new BigDecimal(plannedQty.getValue().replaceAll("[\\s,]", "").trim()) : BigDecimal.ZERO;
                if (r == null || r.compareTo(BigDecimal.ONE) < 0) {
                    productTable.notValid(rowId, ItemTableConstants.QTY);
                    Info.show(wfmMessages.pleaseMakeSureIsNotZero("Planned Qty"), Info.Type.WARNING);
                    return false;
                }

                TextBox price = (TextBox) tableItem.getColumnById(ItemTableConstants.UNITPRICE);
                BigDecimal p = price.getValue() != null && !"".equals(price.getValue().trim()) ? new BigDecimal(price.getValue().replaceAll("[\\s,]", "").trim())
                        : BigDecimal.ZERO;
                if (p == null || p.compareTo(BigDecimal.ZERO) <= 0) {
                    productTable.notValid(rowId, ItemTableConstants.UNITPRICE);
                    Info.show(wfmMessages.pleaseMakeSureIsNotZero("item price"), Info.Type.WARNING);
                    return false;
                }
            }

        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateRequestQty() {
        productTable.resetValidation();
        for (int rowId = 0; rowId < productTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = productTable.getItem(rowId);
            KpiCheckBox checkBox = (KpiCheckBox) tableItem.getColumnById(CHECKBOX);
            if (checkBox.getValue()) {

                TextBox requestQty = (TextBox) tableItem.getColumnById(ItemTableConstants.REQUEST_QTY);
                BigDecimal remaining = requestQty.getLayoutData() != null ? (BigDecimal) requestQty.getLayoutData() : BigDecimal.ZERO;
                BigDecimal r = requestQty.getValue() != null && !"".equals(requestQty.getValue().trim()) ?
                        new BigDecimal(requestQty.getValue().replace(",", "").replace(" ", "")) : BigDecimal.ZERO;

                if (r == null || r.compareTo(BigDecimal.ONE) < 0) {
                    productTable.notValid(rowId, ItemTableConstants.REQUEST_QTY);
                    Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                    return false;
                } else if (remaining.compareTo(r) < 0) {
                    productTable.notValid(rowId, ItemTableConstants.REQUEST_QTY);
                    Info.show(wfmStrings.requestQtyShouldBeLessThanPlannedQty(), Info.Type.WARNING);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getIconStyle() {
        return "bgMark project-budget-sheet";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
