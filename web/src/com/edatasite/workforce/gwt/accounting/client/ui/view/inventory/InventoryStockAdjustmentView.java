package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
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
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAddTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemTrackBatches.ItemAssignTrackBatchPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialAssignPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials.ItemSerialPopup;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.FlexAlignContent;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/18/11
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryStockAdjustmentView extends FooteredCustomForm implements Colapse, Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private DatePicker adjustmentDate;
    private TextBox txtAdjustmentNumber;
    private AccountsLookUp accountLookUp;
    private DynamicTable tblAdjustmentStock;
    private TextArea memoTxtArea;

    private Integer objectID;
    private Integer productID;
    private boolean isViewMode;
    private boolean isEditMode;
    private GeneralFileUpload uploadForm;
    private String[] params;
    private SelectItem defaultProject;
    private ArrayList<Integer> rfpIds;
    private AdjustmentItem adjustmentItem;
    private FooterInformer showJournal;
    private WfmButton2 saveAsDraft;
    private WfmButton2 submitButton;
    private WfmButton2 saveAndApproveButton;
    private WfmButton2 rejectBtn;
    private WfmButton2 editBtn;
    private ChosenApproversWidget approver;
    private NoteHistoryWidget noteHistoryWidget;
    private Map<String, DynamicTableColumn> columns = new HashMap<>();

    private BankTransferNumberData transferNumberData;
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");

    boolean hasAccessAutoSelect = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_WAREHOUSE_AUTO_SELECT);
    private int rowCounter;
    private SelectItem selectedWarehouse;


    public InventoryStockAdjustmentView(Integer productID) {
        super("stockadjustmentadd", accountingStrings.adjustStockQuantity());
        this.productID = productID;
    }

    public InventoryStockAdjustmentView(Integer objectID, boolean isViewMode) {
        super((isViewMode ? "summary" : "edit"), accountingStrings.adjustStockQuantity());
        this.objectID = objectID;
        this.isViewMode = isViewMode;
        this.isEditMode = !this.isViewMode;
    }

    public InventoryStockAdjustmentView(String[] params) {
        super("stockadjustmentadd", wfmStrings.add() + "&nbsp;" + accountingStrings.adjustStockQuantity());
        this.params = params;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.STOCKADJUSTMENTS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    protected boolean showNewQty() { return true; }

    protected boolean showDescription() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    protected boolean showCostPerItem() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    protected boolean showUsedQty() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    protected boolean showAccountLookUp() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    protected boolean showProject() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    protected String getAdjustmentType() {
        return AccountingConstants.STOCK_ADJUSTMENT_TYPE;
    }

    protected boolean showUnitOfMeasure() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID == null) {
            initTable();
        }
        if (objectID != null) {
            LoadingPanel.loading(true);
            ProductService.App.get().getStockAdjustmentData(objectID, new AsyncCallback<AdjustmentItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(AdjustmentItem result) {
                    adjustmentItem = result;
                    initTable();
                    renderButtons();
                    setFormData(result);
                    if (result.getJournalID() != null && showJournal != null) {
                        showJournal.setVisible(true);
                    }
                    if (isViewMode) {
                        disableAllFields();
                    }
                    LoadingPanel.loading(false);
                }
            });
        } else if (productID != null) {
            ProductService.App.get().getProductByID(productID, new AsyncCallback<ProductItem>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(ProductItem product) {
                    if (SERVICE == product.getType() || PRODUCT_KIT == product.getType() || NON_INVENTORY_ITEM == product.getType() || OTHER_CHARGE == product.getType()) {
                        tblAdjustmentStock.addRow(getWidgets(null, 2));
                    } else {
                        ProductItem[] productItems = new ProductItem[1];
                        productItems[0] = product;
                        AdjustmentItem item = new AdjustmentItem();
                        item.setProductItems(productItems);
                        setFormData(item);
                    }
                }
            });
        } else if (params.length > 2 && "relatedProject".equals(params[1])) {
            ProjectService.App.get().getProjectAsLookupItem(Integer.parseInt(params[2]), new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(SelectItem item) {
                    defaultProject = item;
                    tblAdjustmentStock.addRow(getWidgets(null, 2));
                }
            });
        } else if (params.length > 2 && "convertFromRFP".equals(params[1])) {
            String[] idsArray = params[2].split(",");
            rfpIds = new ArrayList<>();
            for (String id : idsArray) {
                rfpIds.add(Integer.parseInt(id));
            }
            QuoteService.App.get().getRFPItemsForStockAdjustment(rfpIds, new AsyncCallback<ProductItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ProductItem[] productItems) {
                    AdjustmentItem item = new AdjustmentItem();
                    item.setProductItems(productItems);
                    setFormData(item);
                }
            });
        } else {
            tblAdjustmentStock.addRow(getWidgets(null, 1));
        }
        if (objectID == null) {
            LoadingPanel.loading(true);
            ProductService.App.get().getStockAdjustmentData(null, new AsyncCallback<AdjustmentItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(AdjustmentItem result) {
                    adjustmentItem = result;
                    renderButtons();
                    transferNumberData = result.getBankTransferNumberData();
                    txtAdjustmentNumber.setText(result.getNumber());
                    if (objectID != null && transferNumberData != null) {
                        String dateString = result.getDate() != null && result.getDate().getDate() != null ? dateFormat.format(result.getDate().getDate()) : null;
                        transferNumberData.setWithDate(result.getNumber() != null && result.getNumber().contains(dateString));
                        transferNumberData.setDate(transferNumberData.isWithDate() ? dateString : "");
                    }
                    LoadingPanel.loading(false);
                }
            });
        }

    }

    private void renderButtons() {
        if (adjustmentItem.isApprover()) {
            addField(APPROVERS, approver, getTitle(wfmStrings.approver()));

            if (objectID != null) {
                if (Constants.STOCK_ADJUSTMENT_DRAFT.equals(adjustmentItem.getStatusCode())) {
                    saveAsDraft.setVisible(true);
                }
                if (Constants.STOCK_ADJUSTMENT_SUBMITTED.equals(adjustmentItem.getStatusCode()) ||
                        Constants.STOCK_ADJUSTMENT_APPROVED.equals(adjustmentItem.getStatusCode()) ||
                        Constants.STOCK_ADJUSTMENT_DECLINED.equals(adjustmentItem.getStatusCode())) {
                    saveAsDraft.setVisible(false);
                }
            } else {
                saveAsDraft.setVisible(true);
            }
            if (!adjustmentItem.isApproverSaved()) {
                approver.reloadApproverWidgets(RelationItem.TYPE_STOCK_ADJUSTMENT, null);
            }

            if (isViewMode) {
                Integer currentApproverId = adjustmentItem.getApprover() != null ? adjustmentItem.getApprover().getId() : null;
                Integer currentUserId = adjustmentItem.getCurrentUserId() != null ? adjustmentItem.getCurrentUserId() : Utils.getUserID();
                if (STOCK_ADJUSTMENT_SUBMITTED.equals(adjustmentItem.getStatusCode()) && currentUserId.equals(currentApproverId)) {
                    saveAndApproveButton.setVisible(true);
                    rejectBtn.setVisible(true);
                }

                if (editBtn != null && !currentUserId.equals(currentApproverId)) {
                    editBtn.setVisible(false);
                }
                if (STOCK_ADJUSTMENT_DRAFT.equals(adjustmentItem.getStatusCode()) || STOCK_ADJUSTMENT_DECLINED.equals(adjustmentItem.getStatusCode())) {
                    submitButton.setVisible(true);
                }
            }
        } else if (!isViewMode) {
            saveAndApproveButton.setVisible(true);
            if (adjustmentItem != null && (Constants.STOCK_ADJUSTMENT_SUBMITTED.equals(adjustmentItem.getStatusCode()) ||
                    Constants.STOCK_ADJUSTMENT_APPROVED.equals(adjustmentItem.getStatusCode()) ||
                    Constants.STOCK_ADJUSTMENT_DECLINED.equals(adjustmentItem.getStatusCode()))) {
                saveAsDraft.setVisible(false);
            }
        }
    }

    @Override
    protected void addButtons() {
    }

    protected Widget onInitialize() {
        super.onInitialize();

        accountLookUp = new AccountsLookUp();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStockAdjustment(true);
        accountLookUp.setFilterParametrs(fp);
        accountLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        accountLookUp.setAutocompleteOff();
        accountLookUp.ensureDebugId("stock_adjustments-adjustment_account");

        adjustmentDate = new DatePicker();
        adjustmentDate.addStyleName(DEFAULT_WIDTH);
        adjustmentDate.setDate(new Date());
        adjustmentDate.ensureDebugId("stock_adjustments-date");
        adjustmentDate.addChangeHandler(valueChangeEvent -> {
            if (transferNumberData != null && transferNumberData.isWithDate()) {
                transferNumberData.setDate(dateFormat.format(adjustmentDate.getDate()));
                String[] numberParts = txtAdjustmentNumber.getText().split("-"); //MT0001 or MT0001-05/2015
                txtAdjustmentNumber.setText(numberParts[0] + "-" + transferNumberData.getDate());
            }
        });
        txtAdjustmentNumber = new TextBox(true);
        txtAdjustmentNumber.addStyleName(DEFAULT_WIDTH);
        txtAdjustmentNumber.ensureDebugId("stock_adjustments-number");

        memoTxtArea = new TextArea();
        memoTxtArea.addStyleName(DEFAULT_WIDTH);
        memoTxtArea.ensureDebugId("stock_adjustments-memo");

        uploadForm = new GeneralFileUpload(F_STOCK_ADJUSTMENT, objectID, objectID);

        approver = new ChosenApproversWidget(RelationItem.TYPE_STOCK_ADJUSTMENT, objectID);

        drawForm();
        return null;
    }

    private void initTable() {
        tblAdjustmentStock = new DynamicTable(getColumns(), true);
        tblAdjustmentStock.ensureDebugId("stock_adjustments-productne");
        tblAdjustmentStock.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                tblAdjustmentStock.addRow(getWidgets(null, tblAdjustmentStock.getRowCount()));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
                if (rowId == 0) {
                    selectDefaultWarehouse();
                }
            }
        });

        addField(STOCKADJUSTMENT_PRODUCT_TABLE, tblAdjustmentStock, "", true);
    }

    private void selectDefaultWarehouse() {
        if (tblAdjustmentStock != null && tblAdjustmentStock.getRowCount() > 1) {
            DynamicTableItem item = tblAdjustmentStock.getItem(0);
            WarehouseLookUp warehouseLookUp = (WarehouseLookUp) item.getColumnById("warehouse");
            if (warehouseLookUp != null && warehouseLookUp.getSelectedItem() != null) {
                selectedWarehouse = warehouseLookUp.getSelectedItem();
            }
        }
    }

    private void setFormData(AdjustmentItem result) {
        adjustmentDate.setDate(result.getDate() != null ? result.getDate().getNonConvertedDate() : new Date());
        txtAdjustmentNumber.setText(result.getNumber());
        if (result.getAccount() != null) {
            accountLookUp.addItem(result.getAccount());
        }
        memoTxtArea.setText(result.getMemo());

        if (result.getProductItems() != null) {
            result.getProductItems();
            for (ProductItem productItem : result.getProductItems()) {
                tblAdjustmentStock.addRow(getWidgets(productItem, 2));
            }
        }
    }

    private void disableAllFields() {
        adjustmentDate.setEnabled(false);
        txtAdjustmentNumber.setEnabled(false);
        if (showAccountLookUp()) {
            accountLookUp.setEnabled(false);
        }
        memoTxtArea.setEnabled(false);
        approver.setEnabled(false);
        if (approver.getFirstApproverLookUp() != null) {
            approver.getFirstApproverLookUp().setEnabled(false);
        }

        for (int i = 0; i < tblAdjustmentStock.getRowNumber(); i++) {
            DynamicTableItem item = tblAdjustmentStock.getItem(i);

            CustomProductLookUp productLookUp = (CustomProductLookUp) item.getColumnById("product");
            WarehouseLookUp warehouseLookUp = (WarehouseLookUp) item.getColumnById("warehouse");
            MeasurementsLookUp measurementsLookUp = (MeasurementsLookUp) item.getColumnById("uom");
            Div usedQtyDiv = (Div) item.getColumnById("used_qty");
            if (usedQtyDiv != null) {
                TextBox txtUsedQty = (TextBox) usedQtyDiv.getWidget(0);
                if (txtUsedQty != null) txtUsedQty.setEnabled(false);
            }

            Div newQtyDiv = (Div) item.getColumnById("new_qty");
            if (newQtyDiv != null) {
                TextBox txtNewQty = (TextBox) newQtyDiv.getWidget(0);
                ItemSerialPopup.Link serialLink = (ItemSerialPopup.Link) ((Div) newQtyDiv.getWidget(1)).getWidget(0);
                if (txtNewQty != null) {
                    txtNewQty.setEnabled(false);
                    serialLink.setEnabled(false);
                }
            }
            DepartmentLookUp departmentLookUp = (DepartmentLookUp) item.getColumnById("department");

            productLookUp.setEnabled(false);
            if (warehouseLookUp != null) {
                warehouseLookUp.setEnabled(false);
            }
            if (measurementsLookUp != null) {
                measurementsLookUp.setEnabled(false);
            }
            if (departmentLookUp != null) {
                departmentLookUp.setEnabled(false);
            }
        }
    }

    private void drawForm() {
        addTitleField(TITLE_STOCKADJUSTMENT, accountingStrings.adjustStockQuantity());
        addField(DATE_FIELD, adjustmentDate, getTitle(wfmStrings.date(), true));
        addField(CustomFormConstants.NUMBER, txtAdjustmentNumber, getTitle(accountingStrings.adjustStockQuantityNo(), true));
        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            injectHideStyles();
        } else {
            addField(ACCOUNT_NAME, accountLookUp, getTitle(accountingStrings.adjustmentAccount(), true));
            addField(NOTES, memoTxtArea, getTitle(wfmStrings.description()));
            addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, getTitle(wfmStrings.attachments()));
            addField(CustomFormConstants.ATTACHMENTS, uploadForm);
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, InventoryStockAdjustmentView.this, (sender, args) -> {
            if (!isViewMode && approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    Integer currentUserId = adjustmentItem != null && adjustmentItem.getCurrentUserId() != null ? adjustmentItem.getCurrentUserId() : Utils.getUserID();
                    if (currentUserId.equals(itemId)) {
                        saveAndApproveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        saveAndApproveButton.setVisible(false);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApproveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        saveAndApproveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });
        show();
    }

    private native void injectHideStyles() /*-{
        var style = $doc.createElement('style');
        style.innerHTML =
            '#attachments-section,' +
            '#approvers-section,' +
            '#account-name__and__notes-secion {' +
            'display: none !important;' +
            '}';
        $doc.head.appendChild(style);
    }-*/;

    protected DynamicTableColumn[] getColumns() {
        columns = new HashMap<>();
        columns.put("product", new DynamicTableColumn(wfmStrings.name(), "product", 160));
        if (showDescription()) {
            columns.put("description", new DynamicTableColumn(wfmStrings.description(), "description", 200));
        }
        if (Utils.isMultiWarehouseEnabled()) {
            columns.put("warehouse", new DynamicTableColumn(accountingStrings.warehouse(), "warehouse", 150));
        }

        if (showUnitOfMeasure()) {
            columns.put("uom", new DynamicTableColumn(wfmStrings.unitMeasurement(), "uom", 100));
        }
        columns.put("current_qty", new DynamicTableColumn(accountingStrings.currentQty(), "current_qty", 100, RIGHT_ALIGN_CELL));
        if (showUsedQty()) {
            String title = Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION) ? wfmStrings.quantity() : accountingStrings.usedQty();
            columns.put("used_qty", new DynamicTableColumn(title, "used_qty", 100, RIGHT_ALIGN_CELL));
        }

        if (showNewQty()) {
            String title = Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION) ? wfmStrings.quantity() : accountingStrings.newQty();
            columns.put("new_qty", new DynamicTableColumn(title, "new_qty", 100, RIGHT_ALIGN_CELL));
        }
        columns.put("total_qty", new DynamicTableColumn(wfmStrings.total(), "total_qty", 100, RIGHT_ALIGN_CELL));
        boolean isVisibleCostPerItem = true;
        if (isViewMode && adjustmentItem != null && adjustmentItem.getProductItems() != null) {
            isVisibleCostPerItem = false;
            for (ProductItem productItem : adjustmentItem.getProductItems()) {
                if (productItem.getNewQty() != null && productItem.getNewQty().compareTo(BigDecimal.ZERO) > 0) {
                    isVisibleCostPerItem = true;
                    break;
                }
            }
        }
        if (showCostPerItem() && isVisibleCostPerItem) {
            columns.put("PNL_COST_PER_ITEM", new DynamicTableColumn(accountingStrings.costPerItem(), "PNL_COST_PER_ITEM", 150, RIGHT_ALIGN_CELL));
        }

        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
            columns.put("department", new DynamicTableColumn(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), "department", 150));
        }
        if (showProject() && "true".equals(Utils.userSettings.get(PM_IS_SETUP))) {
            columns.put("project", new DynamicTableColumn(Property.get(Constants.PROJECT, wfmStrings.project()), "project", 150));
        }
        return columns.values().toArray(new DynamicTableColumn[0]);
    }

    protected Widget[] getWidgets(ProductItem productItem, int rowId) {
        final CustomProductLookUp productLookUp = new CustomProductLookUp(STOCK_ADJUSTMENT);

        final WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
        warehouseLookUp.getSuggestBox().setWidth("140px");

        MeasurementsLookUp measurementsLookUp = new MeasurementsLookUp();
        measurementsLookUp.getSuggestBox().setWidth("100px");

        DepartmentLookUp departmentLookUp = new DepartmentLookUp();
        departmentLookUp.getSuggestBox().setWidth("140px");

        final Label lblDescription = new Label();
        final Label lblCurrentQty = new Label(Utils.getNumberFormat().format(BigDecimal.ZERO));
        final TextBox txtUsedQty = new TextBox();
        final TextBox txtNewQty = new TextBox();
        final Label txtTotalQty = new Label(Utils.getNumberFormat().format(BigDecimal.ZERO));

        ItemSerialAssignPopup serialAssignPopup = new ItemSerialAssignPopup(txtUsedQty);
        ItemSerialAssignPopup.Link serialAssignLink = serialAssignPopup.getLink();
        serialAssignLink.setVisible(false);

        Integer objectId = productItem != null ? productItem.getObjectId() : null;
        ItemAssignTrackBatchPopup assignTrackBatchPopup = new ItemAssignTrackBatchPopup(objectId, txtUsedQty, isViewMode);
        ItemAssignTrackBatchPopup.Link assignTrackBatchLink = assignTrackBatchPopup.getLink();
        assignTrackBatchLink.setVisible(false);

        Div usedQtyPanel = new Div();
        usedQtyPanel.addStyleName("input-group input-group--plus-off");
        usedQtyPanel.add(txtUsedQty);
        Div divAssign = new Div("input-group-append");
        divAssign.add(serialAssignLink);
        divAssign.add(assignTrackBatchLink);
        usedQtyPanel.add(divAssign);

        ItemSerialPopup serialPopup = new ItemSerialPopup(txtNewQty);
        ItemSerialPopup.Link serialLink = serialPopup.getLink();
        serialLink.setVisible(false);

        ItemAddTrackBatchPopup trackBatchPopup = new ItemAddTrackBatchPopup(objectId, txtNewQty, isViewMode);
        ItemAddTrackBatchPopup.Link addingTrackBatchLink = trackBatchPopup.getLink();
        addingTrackBatchLink.setVisible(false);

        Div newQtyPanel = new Div();
        newQtyPanel.addStyleName("input-group input-group--plus-off");
        newQtyPanel.add(txtNewQty);
        Div divAdd = new Div("input-group-append");
        divAdd.add(serialLink);
        divAdd.add(addingTrackBatchLink);
        newQtyPanel.add(divAdd);

        txtTotalQty.setWidth("90px");

        txtUsedQty.setStyleName("usedQtyValue form-control");
        txtNewQty.setStyleName("newQtyValue form-control");

        Validation.addNumericKeyboardListener(txtUsedQty, 2, false);
        Validation.addNumericKeyboardListener(txtNewQty, 2, false);

        CRMLookUp project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.ensureDebugId("stock_project");
        project.setWidth(NORMAL_WIDTH);
        project.setEnabled(false);

        if (defaultProject != null) project.setSelected(defaultProject);
        VerticalPanel pnlCostPerItem = getCostPerItemWidget(productItem);

        final LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();
        widgetsMap.put("PRODUCT", productLookUp);
        widgetsMap.put("DESCRIPTION", lblDescription);
        widgetsMap.put("WAREHOUSE", warehouseLookUp);
        widgetsMap.put("UOM", measurementsLookUp);
        widgetsMap.put("CURRENT_QTY", lblCurrentQty);
        widgetsMap.put("USED_QTY", txtUsedQty);
        widgetsMap.put("NEW_QTY", txtNewQty);
        widgetsMap.put("TOTAL_QTY", txtTotalQty);
        widgetsMap.put("PNL_COST_PER_ITEM", pnlCostPerItem);
        widgetsMap.put("PNL_PROJECT", project);
        widgetsMap.put("SERIAL_NUMBER", serialLink);
        widgetsMap.put("TRACK_BATCH", assignTrackBatchLink);
        widgetsMap.put("ASSIGN_SERIAL_NUMBER", serialAssignLink);
        widgetsMap.put("ASSIGN_TRACK_BATCH", addingTrackBatchLink);
        widgetsMap.put("DEPARTMENT", departmentLookUp);

        productLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setInventoryData(widgetsMap, false, rowId));
        warehouseLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setInventoryData(widgetsMap, true, rowId));
        measurementsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setInventoryData(widgetsMap, false, rowId));
        departmentLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setInventoryData(widgetsMap, false, rowId));

        if (!isViewMode) {
            txtUsedQty.addChangeHandler(e -> {
                onQtyChange(widgetsMap);

                project.setEnabled(AccountingUtils.get().parseToBigDecimal(txtUsedQty.getValue()).compareTo(BigDecimal.ZERO) > 0);
            });
            txtNewQty.addChangeHandler(e -> onQtyChange(widgetsMap));
        }

        if (productItem != null) {
            if (productItem.getLineItemID() != null) {
                productLookUp.setLineItemID(productItem.getLineItemID());
            }
            if (productItem.getObjectId() != null && productItem.getName() != null) {
                productLookUp.addProductItem(new SelectItem(productItem.getObjectId(), productItem.getProductNumber() + "-" + productItem.getName()));
            }
            if (productItem.getDescription() != null) {
                lblDescription.setText(productItem.getDescription());
            }
            if (productItem.getWarehouseId() != null && productItem.getWarehouseName() != null) {
                warehouseLookUp.addItem(new SelectItem(productItem.getWarehouseId(), productItem.getWarehouseName()));
            }
            if (productItem.getUnitMeasurementId() != null && productItem.getUnitMeasurementName() != null) {
                measurementsLookUp.addItem(new SelectItem(productItem.getUnitMeasurementId(), productItem.getUnitMeasurementName()));
            }
            if (productItem.getDepartmentId() != null && productItem.getDepartmentName() != null) {
                departmentLookUp.addItem(new SelectItem(productItem.getDepartmentId(), productItem.getDepartmentName()));
            }
            if (isEditMode) {
                productLookUp.setEnabled(false);
                warehouseLookUp.setEnabled(false);
                if (productItem.getCurrentQty() != null) {
                    lblCurrentQty.setText(Utils.getNumberFormat().format(productItem.getCurrentQty()));
                }
            } else {
                if (isViewMode) {
                    if (productItem.getCurrentQty() != null) {
                        lblCurrentQty.setText(Utils.getNumberFormat().format(productItem.getCurrentQty()));
                    }
                } else {
                    if (productItem.getItemsInStock() != null) {
                        lblCurrentQty.setText(Utils.getNumberFormat().format(productItem.getItemsInStock()));
                    }
                }
            }
            if (productItem.getUsedQty() != null) {
                txtUsedQty.setText(Utils.getNumberFormat().format(productItem.getUsedQty()));

                if (productItem.getUsedQty().compareTo(BigDecimal.ZERO) > 0) {
                    project.setEnabled(true);
                }
            }
            if (productItem.getNewQty() != null) {
                txtNewQty.setText(Utils.getNumberFormat().format(productItem.getNewQty()));
            }
            if (productItem.getTotalQty() != null) {
                txtTotalQty.setText(Utils.getNumberFormat().format(productItem.getTotalQty()));
            }
            if (productItem.getProjectID() != null) {
                project.setSelected(new SelectItem(productItem.getProjectID(), productItem.getProjectName()));
            }
            if (productItem.getSerials() != null && productItem.getSerials().size() > 0) {
                serialPopup.setItems(productItem.getSerials());
                serialLink.setProductId(productItem.getObjectId());
                serialLink.setVisible(true);

            }
            if (productItem.getAssignedSerials() != null && productItem.getAssignedSerials().size() > 0) {
                serialAssignPopup.setItems(productItem.getAssignedSerials());
                serialAssignLink.setProductId(productItem.getObjectId());
                serialAssignLink.setVisible(true);
            }

            String productLabel = productItem.getName() != null ?
                    (productItem.getProductNumber() != null ? productItem.getProductNumber() + " -> " + productItem.getName() : productItem.getName()) :
                    wfmStrings.notAvailable();
            if (productItem.getBatchItems() != null && productItem.getBatchItems().size() > 0) {
                trackBatchPopup.setTrackBatchItems(productItem.getBatchItems());
                addingTrackBatchLink.setProductId(productItem.getObjectId());
                addingTrackBatchLink.setProductName(productLabel);
                addingTrackBatchLink.setVisible(true);
                newQtyPanel.removeStyleName("input-group--plus-off");
                newQtyPanel.addStyleName("input-group--plus-on");
            }
            if (productItem.getAssignedBatchItems() != null && productItem.getAssignedBatchItems().size() > 0) {
                assignTrackBatchPopup.setTrackBatchItems(productItem.getAssignedBatchItems());
                assignTrackBatchLink.setProductName(productLabel);
                assignTrackBatchLink.setProductId(productItem.getObjectId());
                assignTrackBatchLink.setVisible(true);
                usedQtyPanel.removeStyleName("input-group--plus-off");
                usedQtyPanel.addStyleName("input-group--plus-on");
            }
            if (isViewMode && productItem.getUnitpPrice() != null) {
                pnlCostPerItem.setVisible(true);
                ((TextBox) pnlCostPerItem.getWidget(1)).setValue(AccountingUtils.get().format(productItem.getUnitpPrice()));
                pnlCostPerItem.getWidget(0).setVisible(false);
                pnlCostPerItem.getWidget(1).setVisible(true);
                ((TextBox) pnlCostPerItem.getWidget(1)).setEnabled(false);
            }
        }

        List<Widget> widgets = new ArrayList<>();
        widgets.add(productLookUp);
        if (showDescription()) {
            widgets.add(lblDescription);
        }
        if (Utils.isMultiWarehouseEnabled()) {
            widgets.add(warehouseLookUp);
        }
        if (showUnitOfMeasure()) {
            widgets.add(measurementsLookUp);
        }
        widgets.add(lblCurrentQty);
        if (showUsedQty()) {
            widgets.add(usedQtyPanel);
        }
        if (showNewQty()) {
            widgets.add(newQtyPanel);
        }
        widgets.add(txtTotalQty);
        if (showCostPerItem() && columns.get("PNL_COST_PER_ITEM") != null) {
            widgets.add(pnlCostPerItem);
        }
        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
            widgets.add(departmentLookUp);
        }
        if (showProject() && "true".equals(Utils.userSettings.get(PM_IS_SETUP))) {
            widgets.add(project);
        }
        return widgets.toArray(new Widget[0]);
    }

    private void onQtyChange(LinkedHashMap<String, Widget> widgetsMap) {
        Label lblCurrentQty = (Label) widgetsMap.get("CURRENT_QTY");
        TextBox txtUsedQty = (TextBox) widgetsMap.get("USED_QTY");
        TextBox txtNewQty = (TextBox) widgetsMap.get("NEW_QTY");
        Label txtTotalQty = (Label) widgetsMap.get("TOTAL_QTY");
        VerticalPanel pnlCostPerItem = (VerticalPanel) widgetsMap.get("PNL_COST_PER_ITEM");
        BigDecimal cQty = !"".equals(lblCurrentQty.getText().trim()) ? BigDecimal.valueOf(Utils.getNumberFormat().parse(lblCurrentQty.getText())).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal usedQty = !"".equals(txtUsedQty.getText().trim()) ? BigDecimal.valueOf(Utils.getNumberFormat().parse(txtUsedQty.getText())).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal newQty = !"".equals(txtNewQty.getText().trim()) ? BigDecimal.valueOf(Utils.getNumberFormat().parse(txtNewQty.getText())).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal totalQty = cQty.subtract(usedQty).add(newQty);

        txtTotalQty.setText(Utils.getNumberFormat().format(totalQty));
        pnlCostPerItem.setVisible(newQty.compareTo(ZERO) > 0);
    }

    private void setInventoryData(final LinkedHashMap<String, Widget> widgetsMap, boolean isWarehouseLookup, int rowId) {
        CustomProductLookUp productLookUp = (CustomProductLookUp) widgetsMap.get("PRODUCT");
        WarehouseLookUp warehouseLookUp = (WarehouseLookUp) widgetsMap.get("WAREHOUSE");
        MeasurementsLookUp measurementsLookUp = (MeasurementsLookUp) widgetsMap.get("UOM");
        final Label lblDescription = (Label) widgetsMap.get("DESCRIPTION");
        final Label lblCurrentQty = (Label) widgetsMap.get("CURRENT_QTY");
        if (productLookUp.getSelectedItemID() != null) {
            if (rowId == 1 && warehouseLookUp != null) {
                selectedWarehouse = warehouseLookUp.getSelectedItem();
            }
            LoadingPanel.loading(true);
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setObjectId(productLookUp.getSelectedItemID());
            fp.setWarehouseID(warehouseLookUp != null ? warehouseLookUp.getSelectedItemID() : null);
            fp.setUnitMeasurementId(measurementsLookUp != null ? measurementsLookUp.getSelectedItemID() : null);
            ProductService.App.get().getInventoryStock(fp, new AsyncCallback<ProductItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ProductItem result) {
                    LoadingPanel.loading(false);

                    lblDescription.setText(result.getDescription() != null ? result.getDescription() : "");
                    lblCurrentQty.setText(Utils.getNumberFormat().format(result.getItemsInStock() != null ? result.getItemsInStock() : BigDecimal.ZERO));
                    if (result.getUnitMeasurementId() != null && result.getUnitMeasurementName() != null) {
                        measurementsLookUp.setSelected(result.getUnitMeasurementId(), result.getUnitMeasurementName());
                    }

                    if (hasAccessAutoSelect && (rowId > 1 && selectedWarehouse != null && !isWarehouseLookup)) {
                        warehouseLookUp.setSelected(selectedWarehouse);
                    }
                    if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                        warehouseLookUp.setSelected(result.getWarehouseId(), result.getWarehouseName());
                    }

                    if (result.getInventoryTrackingEnabled()) {
                        ItemSerialAssignPopup.Link serialAssignLink = (ItemSerialAssignPopup.Link) widgetsMap.get("ASSIGN_SERIAL_NUMBER");
                        serialAssignLink.setProductId(result.getObjectId());
                        serialAssignLink.setWarehouseId(warehouseLookUp != null
                                ? warehouseLookUp.getSelectedItemID()
                                : null);
                        serialAssignLink.setVisible(true);

                        ItemSerialPopup.Link serialLink = (ItemSerialPopup.Link) widgetsMap.get("SERIAL_NUMBER");
                        serialLink.setProductId(result.getObjectId());
                        serialLink.setVisible(true);
                    }

                    if (result.getTrackBatchesEnabled()) {
                        ItemAssignTrackBatchPopup.Link trackBatchAssignLink = (ItemAssignTrackBatchPopup.Link) widgetsMap.get("TRACK_BATCH");
                        trackBatchAssignLink.setProductId(result.getObjectId());
                        trackBatchAssignLink.setWarehouseId(warehouseLookUp != null
                                ? warehouseLookUp.getSelectedItemID()
                                : null);
                        String productLabel = productLookUp.getSelectedItem() != null ? productLookUp.getSelectedItem().getName() : wfmStrings.notAvailable();
                        trackBatchAssignLink.setProductName(productLabel);
                        trackBatchAssignLink.setVisible(true);
                        TextBox txtUsedQty = (TextBox) widgetsMap.get("USED_QTY");
                        txtUsedQty.getElement().getParentElement().removeClassName("input-group--plus-off");
                        txtUsedQty.getElement().getParentElement().addClassName("input-group--plus-on");

                        ItemAddTrackBatchPopup.Link addTrackBatchLink = (ItemAddTrackBatchPopup.Link) widgetsMap.get("ASSIGN_TRACK_BATCH");
                        addTrackBatchLink.setProductId(result.getObjectId());
                        addTrackBatchLink.setProductName(productLabel);
                        addTrackBatchLink.setVisible(true);
                        TextBox txtNewQty = (TextBox) widgetsMap.get("NEW_QTY");
                        txtNewQty.getElement().getParentElement().removeClassName("input-group--plus-off");
                        txtNewQty.getElement().getParentElement().addClassName("input-group--plus-on");
                    } else {
                        ItemAssignTrackBatchPopup.Link trackBatchAssignLink = (ItemAssignTrackBatchPopup.Link) widgetsMap.get("TRACK_BATCH");
                        trackBatchAssignLink.setVisible(false);
                        TextBox txtUsedQty = (TextBox) widgetsMap.get("USED_QTY");
                        txtUsedQty.getElement().getParentElement().removeClassName("input-group--plus-on");
                        txtUsedQty.getElement().getParentElement().addClassName("input-group--plus-off");

                        ItemAddTrackBatchPopup.Link addTrackBatchLink = (ItemAddTrackBatchPopup.Link) widgetsMap.get("ASSIGN_TRACK_BATCH");
                        addTrackBatchLink.setVisible(false);
                        TextBox txtNewQty = (TextBox) widgetsMap.get("NEW_QTY");
                        txtNewQty.getElement().getParentElement().removeClassName("input-group--plus-on");
                        txtNewQty.getElement().getParentElement().addClassName("input-group--plus-off");
                    }

                    onQtyChange(widgetsMap);
                }
            });
        }
    }

    private void save(String status) {
        if (isViewMode) {
            updateStatus(STOCK_ADJUSTMENT_APPROVED, null);
        } else {
            if (!validate()) {
                reEnableButtons(true);
                return;
            }
            AdjustmentItem adjustmentItem = getObjectData();
            adjustmentItem.setStatusCode(status);
            adjustmentItem.setType(getAdjustmentType());
            QuantityItem[] itemsForValidateStockAvailability = getQunaittyItemsForStockValidation();
            if (itemsForValidateStockAvailability != null && itemsForValidateStockAvailability.length > 0) {
                InvoiceService.App.get().validateStockAvailability(itemsForValidateStockAvailability, objectID, StockOutFlow.FROM_STOCK_ADJUSTMENT, null, new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                        reEnableButtons(true);
                    }

                    @Override
                    public void success(SelectItem[] result) {
                        if (result == null || result.length == 0) {
                            save(adjustmentItem, status);
                        } else {
                            reEnableButtons(true);
                            Info.warn(AccountingMessages.App.get().youDoNotHaveSufficientStock(result[0].getName()), 5000);
                        }
                    }
                });
            } else {
                save(adjustmentItem, status);
            }
        }
    }

    private void save(AdjustmentItem adjustmentItem, String status) {
        QuantityItem[] qitems = getQuantityItemsForValidateStockInconsistency();
        if (objectID != null && qitems.length > 0) {
            validateForInconsistency(adjustmentItem, qitems, status);
        } else {
            saveAdjustment(adjustmentItem, status);
        }
    }

    private void validateForInconsistency(AdjustmentItem adjustmentItem, QuantityItem[] quantityItems, String status) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().validateStockInconsistencyInAdjustProcess(StockTransactionType.ADJUSTMENT, adjustmentItem.getObjectID(), quantityItems, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    saveAdjustment(adjustmentItem, status);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveSufficientStock(result.getName()), 5000);
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    private void saveAdjustment(AdjustmentItem adjustmentItem, String status) {
        LoadingPanel.loading(true);
        ProductService.App.get().saveStockAdjustment(adjustmentItem, new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(TestRPC result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    if (result.getMessageCommand() != null && MessageCommand.isNumberExists.equals(result.getMessageCommand())) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(accountingStrings.systemGenerateNextFreeNumber());
                        messageBox.open();
                        messageBox.addCloseHandler(event -> {
                            if (result.getMessage() != null) {
                                txtAdjustmentNumber.setText(result.getMessage());
                                save(status);
                            }
                        });
                    } else if (result.getMessageCommand() != null && MessageCommand.hasOutTransactions.equals(result.getMessageCommand())) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(result.getMessage());
                        messageBox.open();
                    } else {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.stockAdjustment()), Info.Type.INFO);
                        closeTab();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STOCK_ADJUSTMENT_SAVED, null, InventoryStockAdjustmentView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, InventoryStockAdjustmentView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, null, InventoryStockAdjustmentView.this);
                    }
                }
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        Date date = DateUtil.resetTime(adjustmentDate.getDate());
        if (adjustmentDate.getDate() != null && Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(date)) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.stockAdjustment(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (!Validation.validateDate(adjustmentDate, new HTML(), true)) {
            errors++;
        }

        if (!Validation.validateTextBoxRequired(txtAdjustmentNumber)) {
            errors++;
        }

        if (showAccountLookUp() && !Validation.validateLookUpRequired(accountLookUp)) {
            errors++;
        }

        errors += validateItems();

        if (adjustmentItem != null && adjustmentItem.isApprover()) {
            if (!approver.isValid()) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
            return false;
        }

        return errors == 0;
    }

    private int validateItems() {
        int errors = tblAdjustmentStock.getRowNumber() > 0 ? 0 : 1;
        if (tblAdjustmentStock != null && tblAdjustmentStock.getRowNumber() > 0) {
            for (int i = 0; i < tblAdjustmentStock.getRowNumber(); i++) {
                DynamicTableItem item = tblAdjustmentStock.getItem(i);
                BigDecimal usedQty = BigDecimal.ZERO, newQty = BigDecimal.ZERO;
                CustomProductLookUp productLookUp = (CustomProductLookUp) item.getColumnById("product");
                WarehouseLookUp warehouseLookUp = (WarehouseLookUp) item.getColumnById("warehouse");

                Div usedQtyDiv = (Div) item.getColumnById("used_qty");
                TextBox txtUsedQty = usedQtyDiv != null ? (TextBox) usedQtyDiv.getWidget(0) : null;
                ItemSerialAssignPopup.Link serialAssignLink = usedQtyDiv != null ? (ItemSerialAssignPopup.Link) ((Div) usedQtyDiv.getWidget(1)).getWidget(0) : null;
                ItemAssignTrackBatchPopup.Link batchTrackAssignLink = usedQtyDiv != null ? (ItemAssignTrackBatchPopup.Link) ((Div) usedQtyDiv.getWidget(1)).getWidget(1) : null;
                String productLabel = productLookUp.getSelectedItem() != null ? productLookUp.getSelectedItem().getName() : wfmStrings.notAvailable();
                if (batchTrackAssignLink != null) batchTrackAssignLink.setProductName(productLabel);

                Div newQtyDiv = (Div) item.getColumnById("new_qty");
                TextBox txtNewQty = newQtyDiv != null ? (TextBox) newQtyDiv.getWidget(0) : null;
                ItemSerialPopup.Link serialLink = newQtyDiv != null ? (ItemSerialPopup.Link) ((Div) newQtyDiv.getWidget(1)).getWidget(0) : null;
                ItemAddTrackBatchPopup.Link addTrackBatchLink = newQtyDiv != null ? (ItemAddTrackBatchPopup.Link) ((Div) newQtyDiv.getWidget(1)).getWidget(1) : null;
                if (addTrackBatchLink != null) addTrackBatchLink.setProductName(productLabel);

                Label lblTotalQty = (Label) item.getColumnById("total_qty");

                if (!Validation.validateLookUpRequired(productLookUp)) {
                    errors++;
                }
                if (Utils.isMultiWarehouseEnabled() && warehouseLookUp != null
                        && !Validation.validateLookUpRequired(warehouseLookUp)) {
                    errors++;
                }

                if (txtUsedQty != null && txtUsedQty.getValue() != null && !txtUsedQty.getValue().isEmpty()) {
                    usedQty = BigDecimal.valueOf(Utils.getNumberFormat().parse(txtUsedQty.getValue())).setScale(2, RoundingMode.HALF_UP);
                }
                if (txtNewQty != null && txtNewQty.getValue() != null && !txtNewQty.getValue().isEmpty()) {
                    newQty = BigDecimal.valueOf(Utils.getNumberFormat().parse(txtNewQty.getValue())).setScale(2, RoundingMode.HALF_UP);
                }

                if (usedQty.compareTo(BigDecimal.ZERO) == 0 && newQty.compareTo(BigDecimal.ZERO) == 0) {
                    errors += markAsError(txtUsedQty, true);
                    errors += markAsError(txtNewQty, true);
                } else if (serialAssignLink != null && serialAssignLink.isVisible() && usedQty.intValue() != serialAssignLink.getSerials().size()) {
                    errors += markAsError(txtUsedQty, true);
                } else if (serialLink != null && serialLink.isVisible() && newQty.intValue() != serialLink.getSerials().size()) {
                    errors += markAsError(txtNewQty, true);
                } else if (batchTrackAssignLink != null && batchTrackAssignLink.isVisible() && usedQty.compareTo(batchTrackAssignLink.getTotalQty()) != 0) {
                    errors += markAsError(txtUsedQty, true);
                } else if (addTrackBatchLink != null && addTrackBatchLink.isVisible() && newQty.compareTo(addTrackBatchLink.getTotalQty()) != 0) {
                    errors += markAsError(txtNewQty, true);
                }

                BigDecimal totalQty = BigDecimal.valueOf(Utils.getNumberFormat().parse(lblTotalQty.getText())).setScale(2, RoundingMode.HALF_UP);
                errors += markAsError(lblTotalQty, totalQty.compareTo(BigDecimal.ZERO) < 0);
            }
        }
        return errors;
    }

    private AdjustmentItem getObjectData() {
        AdjustmentItem adjustmentItem = new AdjustmentItem();
        adjustmentItem.setObjectID(objectID);
        adjustmentItem.setNumber(txtAdjustmentNumber.getValue());
        adjustmentItem.setDate(adjustmentDate.getDate() != null ? new DateNonConvertable(adjustmentDate.getDate()) : new DateNonConvertable());
        adjustmentItem.setAccount(accountLookUp.getSelectedItem());
        adjustmentItem.setMemo(memoTxtArea.getText());
        adjustmentItem.setProductItems(getAdjustmentItems());
        adjustmentItem.setAttachments(uploadForm.getAttachedFiles());
        adjustmentItem.setRfpIds(rfpIds);
        if (this.adjustmentItem != null && this.adjustmentItem.isApprover()) {
            adjustmentItem.setApprovers(approver.getChosenApprovers());
        }
        adjustmentItem.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        if (objectID == null) {
            if (!"".equals(txtAdjustmentNumber.getText()) && txtAdjustmentNumber.getText() != null && transferNumberData != null) {
                String prefix = transferNumberData.getPrefix();
                String[] numberParts = txtAdjustmentNumber.getText().split("-"); //MT0001 or MT0001-05/2015
                String fourDigitNumber = numberParts[0].substring(prefix.length());
                if (fourDigitNumber.matches("[0-9]+")) {
                    adjustmentItem.setIntNumber(Integer.valueOf(fourDigitNumber));
                }
            }
            adjustmentItem.setNumber(txtAdjustmentNumber.getText());
        }
        return adjustmentItem;
    }

    private ProductItem[] getAdjustmentItems() {
        List<ProductItem> productItems = new ArrayList<>();
        if (tblAdjustmentStock != null && tblAdjustmentStock.getRowNumber() > 0) {
            for (int i = 0; i < tblAdjustmentStock.getRowNumber(); i++) {
                DynamicTableItem item = tblAdjustmentStock.getItem(i);

                CustomProductLookUp productLookUp = (CustomProductLookUp) item.getColumnById("product");
                WarehouseLookUp warehouseLookUp = (WarehouseLookUp) item.getColumnById("warehouse");
                MeasurementsLookUp measurementsLookUp = (MeasurementsLookUp) item.getColumnById("uom");
                Label lblCurrentQty = (Label) item.getColumnById("current_qty");
                Label lblDescription = (Label) item.getColumnById("description");

                Div usedQtyDiv = (Div) item.getColumnById("used_qty");
                TextBox txtUsedQty = usedQtyDiv != null ? (TextBox) usedQtyDiv.getWidget(0) : null;
                ItemSerialAssignPopup.Link serialAssignLink = usedQtyDiv != null ? (ItemSerialAssignPopup.Link) ((Div) usedQtyDiv.getWidget(1)).getWidget(0) : null;
                ItemAssignTrackBatchPopup.Link trackBatchAssignLink = usedQtyDiv != null ? (ItemAssignTrackBatchPopup.Link) ((Div) usedQtyDiv.getWidget(1)).getWidget(1) : null;
                String productLabel = productLookUp.getSelectedItem() != null ? productLookUp.getSelectedItem().getName() : wfmStrings.notAvailable();
                if (trackBatchAssignLink != null) trackBatchAssignLink.setProductName(productLabel);

                Div newQtyDiv = (Div) item.getColumnById("new_qty");
                TextBox txtNewQty = newQtyDiv != null ? (TextBox) newQtyDiv.getWidget(0) : null;
                ItemSerialPopup.Link serialLink = newQtyDiv != null ? (ItemSerialPopup.Link) ((Div) newQtyDiv.getWidget(1)).getWidget(0) : null;
                ItemAddTrackBatchPopup.Link addTrackBatchLink = newQtyDiv != null ? (ItemAddTrackBatchPopup.Link) ((Div) newQtyDiv.getWidget(1)).getWidget(1) : null;
                if (addTrackBatchLink != null) addTrackBatchLink.setProductName(productLabel);

                VerticalPanel pnlCostPerItem = (VerticalPanel) item.getColumnById("PNL_COST_PER_ITEM");
                CRMLookUp project = (CRMLookUp) item.getColumnById("project");
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) item.getColumnById("department");


                BigDecimal currentQty = BigDecimal.ZERO, usedQty = BigDecimal.ZERO, newQty = BigDecimal.ZERO, totalQty = BigDecimal.ZERO;
                Integer projectID = null;
                String projectName = "";
                if (lblCurrentQty.getText() != null && !lblCurrentQty.getText().isEmpty()) {
                    currentQty = BigDecimal.valueOf(Utils.getNumberFormat().parse(lblCurrentQty.getText())).setScale(2, RoundingMode.HALF_UP);
                }
                if (txtUsedQty != null && txtUsedQty.getValue() != null && !txtUsedQty.getValue().isEmpty()) {
                    usedQty = BigDecimal.valueOf(Utils.getNumberFormat().parse(txtUsedQty.getValue())).setScale(2, RoundingMode.HALF_UP);
                }
                if (txtNewQty != null && txtNewQty.getValue() != null && !txtNewQty.getValue().isEmpty()) {
                    newQty = BigDecimal.valueOf(Utils.getNumberFormat().parse(txtNewQty.getValue())).setScale(2, RoundingMode.HALF_UP);
                }
                totalQty = currentQty.subtract(usedQty).add(newQty);
                if (project != null && project.getSelectedItem() != null) {
                    projectID = project.getSelectedItemID();
                    projectName = project.getSelectedItem().getName();
                }

                ProductItem productItem = new ProductItem();
                productItem.setLineItemID(productLookUp.getLineItemID());
                productItem.setObjectId(productLookUp.getSelectedItemID());
                productItem.setName(productLookUp.getSelectedItem().getName());
                if (lblDescription != null && lblDescription.getText() != null && !lblDescription.getText().isEmpty()) {
                    productItem.setDescription(lblDescription.getText());
                }
                if (warehouseLookUp != null) {
                    productItem.setWarehouseId(warehouseLookUp.getSelectedItemID());
                    productItem.setWarehouseName(warehouseLookUp.getSelectedItem().getName());
                }
                if (measurementsLookUp != null && measurementsLookUp.getSelectedItem() != null) {
                    productItem.setUnitMeasurementId(measurementsLookUp.getSelectedItemID());
                    productItem.setUnitMeasurementName(measurementsLookUp.getSelectedItem().getName());
                }

                if (departmentLookUp != null && departmentLookUp.getSelectedItem() != null) {
                    productItem.setDepartmentId(departmentLookUp.getSelectedItemID());
                    productItem.setDepartmentName(departmentLookUp.getSelectedItem().getName());
                }
                productItem.setCurrentQty(currentQty);
                productItem.setUsedQty(usedQty);
                productItem.setNewQty(newQty);
                productItem.setTotalQty(totalQty);
                productItem.setProjectID(projectID);
                productItem.setProjectName(projectName);
                if (serialLink != null && serialLink.isVisible() && serialLink.getSerials().size() > 0) {
                    productItem.setSerials(serialLink.getSerials());
                }
                if (serialAssignLink != null && serialAssignLink.isVisible() && serialAssignLink.getSerials().size() > 0) {
                    productItem.setAssignedSerials(serialAssignLink.getSerials());
                }
                if (trackBatchAssignLink != null && trackBatchAssignLink.isVisible() && trackBatchAssignLink.getTtrackBatches().size() > 0) {
                    productItem.setAssignedBatchItems(trackBatchAssignLink.getTtrackBatches());
                }
                if (addTrackBatchLink != null && addTrackBatchLink.isVisible() && addTrackBatchLink.getTtrackBatches().size() > 0) {
                    productItem.setBatchItems(addTrackBatchLink.getTtrackBatches());
                }
                TextBox txtNewPrice = pnlCostPerItem != null ? (TextBox) pnlCostPerItem.getWidget(1) : null;
                if (txtNewPrice != null && txtNewPrice.getValue() != null && !txtNewPrice.getValue().isEmpty()) {
                    productItem.setUnitpPrice(AccountingUtils.get().parseToBigDecimal(txtNewPrice.getValue()));
                }
                productItems.add(productItem);
            }
        }

        return productItems.toArray(new ProductItem[]{});
    }

    private VerticalPanel getCostPerItemWidget(ProductItem productItem) {
        VerticalPanel pnlContainer = new VerticalPanel();
        final VerticalPanel pnlTextContainer = new VerticalPanel();

        final TextBox txtNewPrice = new TextBox();
        if (productItem != null && productItem.getUnitpPrice() != null) {
            txtNewPrice.setValue(AccountingUtils.get().format(productItem.getUnitpPrice()));
        }
        txtNewPrice.getElement().setAttribute("style", "text-align: right !important");
        Validation.addNumericKeyboardListener(txtNewPrice, AccountingUtils.getPriceScale(), true);
        txtNewPrice.setVisible(false);

        Anchor lnkChangePrice = new Anchor(accountingStrings.orChangeManualy());
        lnkChangePrice.addClickHandler(e -> {
            txtNewPrice.setVisible(true);
            pnlTextContainer.setVisible(false);
        });

        pnlTextContainer.add(new HTML(accountingStrings.byDefaultYourProduct()));
        pnlTextContainer.add(lnkChangePrice);

        pnlContainer.add(pnlTextContainer);
        pnlContainer.add(txtNewPrice);

        return pnlContainer;
    }

    private class CustomProductLookUp extends ProductLookUp {
        private Integer lineItemID;

        private CustomProductLookUp(String type) {
            super(type);
        }

        public Integer getLineItemID() {
            return lineItemID;
        }

        public void setLineItemID(Integer lineItemID) {
            this.lineItemID = lineItemID;
        }
    }

    @Override
    public String getIconStyle() {
        return "accountMark inventory-stock";  //To change body of implemented methods use File | Settings | File Templates.
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
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> result = new ArrayList<>();
        if (Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + adjustmentItem.getJournalID(), accountingStrings.reportView() + ": " + adjustmentItem.getNumber(), accountingStrings.reportView() + ": " + adjustmentItem.getNumber());
            });
            showJournal.setBadgeCount(1);
            showJournal.setVisible(false);

            result.add(showJournal);
        }
        noteHistoryWidget = new NoteHistoryWidget(callback -> QuoteService.App.get().getStockAdjustmentHistoryNotes(objectID, callback));
        if (isViewMode) {
            noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
                LoadingPanel.loading(true);
                QuoteService.App.get().saveStockAdjustmentNotes(historyListItem, objectID, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Integer savedObjectId) {
                        historyListItem.setObjectID(savedObjectId);
                        LoadingPanel.loading(false);
                    }
                });
            });
        }
        FooterInformer notes = new FooterInformer(SvgEnum.docHistory, View.wfmStrings.historyAndNotes(), noteHistoryWidget);
        notes.setInitialClasses("informer-item history-notes-container");
        result.add(notes);
        return result;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightResult = new ArrayList<>();
        WfmButton2 pdfAndExcelVersions = new WfmButton2(wfmStrings.pdfVersion(), BTN_DEFAULT_OUTLINE);

        // PDF version
        pdfAndExcelVersions.addClickHandler(e -> {
            String pdfURL = CommandConstants.PDF_URL + "/stockAdjustmentPDFHandler";
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(objectID);
            if (objectID != null) {
                Utils.sendPDFOrExcelRequest(getPanel(), pdfURL, filterParameter.getRequestParams(), "_blank");
            }
        });

        Div pdfAndExcelVersionsWrapper = new Div();
        pdfAndExcelVersionsWrapper.add(pdfAndExcelVersions);

        saveAsDraft = new WfmButton2(wfmStrings.draft(), Constants.BTN_DEFAULT_OUTLINE);
        saveAsDraft.addClickHandler(e -> {
            saveAsDraft.setEnabled(false);
            save(Constants.STOCK_ADJUSTMENT_DRAFT);
        });
        saveAsDraft.ensureDebugId("saveAsDraft-button");
        Div draftButtonWrapper = new Div();
        draftButtonWrapper.add(saveAsDraft);


        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.setVisible(false);
        submitButton.ensureDebugId("submit-button");
        submitButton.addClickHandler(e -> {
            submitButton.setEnabled(false);
            save(Constants.STOCK_ADJUSTMENT_SUBMITTED);
        });
        Div submitButtonWrapper = new Div();
        submitButtonWrapper.add(submitButton);

        saveAndApproveButton = new WfmButton2(wfmStrings.saveAndApprove(), WfmButton2.BTN_SUCCESS);
        saveAndApproveButton.setVisible(false);
        saveAndApproveButton.ensureDebugId("approve-button");
        saveAndApproveButton.addClickHandler(clickEvent -> {
            reEnableButtons(false);
            save(Constants.STOCK_ADJUSTMENT_APPROVED);
        });

        Div saveButtonWrapper = new Div();
        saveButtonWrapper.add(saveAndApproveButton);


        if (isViewMode) {
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_ADJUSTMENT_EDIT : ACCOUNTING_STOCK_ADJUSTMENT_EDIT)) {
                editBtn = new WfmButton2(wfmStrings.edit(), BTN_DEFAULT_OUTLINE);

                editBtn.addClickHandler(event -> {
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("stockadjustment|edit/" + objectID);
                });
                Div editWrapper = new Div();
                editWrapper.add(editBtn);
                rightResult.add(editWrapper);
            }

            rejectBtn = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
            rejectBtn.setVisible(false);
            rejectBtn.ensureDebugId("reject-button");
            rejectBtn.addClickHandler(clickEvent -> {
                reEnableButtons(false);
                showRejectionDialogBox();
            });

            Div rejectButtonWrapper = new Div();
            rejectButtonWrapper.add(rejectBtn);

            rightResult.add(rejectButtonWrapper);

            rightResult.add(submitButtonWrapper);

            rightResult.add(saveButtonWrapper);

            rightResult.add(pdfAndExcelVersionsWrapper);
        } else {
            rightResult.add(draftButtonWrapper);

            rightResult.add(submitButtonWrapper);

            rightResult.add(saveButtonWrapper);

            if (objectID != null) {
                rightResult.add(pdfAndExcelVersionsWrapper);
            }
        }

        return rightResult;
    }

    public QuantityItem[] getQunaittyItemsForStockValidation() {
        Map<String, QuantityItem> map = new HashMap<>();
        Stream.of(getAdjustmentItems()).forEach(item -> {
            String key = item.getObjectId() + (item.getWarehouseId() != null ? "_" + item.getWarehouseId() : "");
            if (map.get(key) != null) {
                map.get(key).setQuantity(map.get(key).getQuantity().add(item.getUsedQty()).subtract(item.getNewQty()));
            } else {
                QuantityItem qitem = new QuantityItem();
                qitem.setId(item.getObjectId());
                qitem.setWarehouseID(item.getWarehouseId() != null ? item.getWarehouseId() : null);
                qitem.setQuantity(item.getUsedQty().subtract(item.getNewQty()));
                map.put(key, qitem);
            }
        });
        return map.values().stream()
                .filter(item -> item.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList())
                .toArray(new QuantityItem[]{});
    }

    public QuantityItem[] getQuantityItemsForValidateStockInconsistency() {
        Map<String, QuantityItem> oldItemsMap = mapAdjustmentItems(adjustmentItem != null ? adjustmentItem.getProductItems() : null);
        Map<String, QuantityItem> adjustedItemsMap = mapAdjustmentItems(getAdjustmentItems());

        List<QuantityItem> list = new ArrayList<>();
        if (oldItemsMap != null && oldItemsMap.size() > 0) {
            oldItemsMap.keySet().forEach(key -> {
                if (adjustedItemsMap.get(key) == null) {
                    QuantityItem qitem = oldItemsMap.get(key);
                    qitem.setQuantity(BigDecimal.ZERO.subtract(qitem.getQuantity()));
                    list.add(qitem);
                } else if (oldItemsMap.get(key).getQuantity().compareTo(adjustedItemsMap.get(key).getQuantity()) > 0) {
                    list.add(adjustedItemsMap.get(key));
                }
            });
        }
        return list.toArray(new QuantityItem[]{});
    }

    Map<String, QuantityItem> mapAdjustmentItems(ProductItem[] items) {
        if (items != null) {
            List<QuantityItem> itemList = Stream.of(items)
                    .filter(item -> item.getNewQty().compareTo(BigDecimal.ZERO) > 0)
                    .map(item -> {
                        QuantityItem qitem = new QuantityItem();
                        qitem.setId(item.getObjectId());
                        qitem.setWarehouseID(item.getWarehouseId() != null ? item.getWarehouseId() : null);
                        qitem.setQuantity(item.getNewQty());
                        return qitem;
                    }).collect(Collectors.toList());
            Map<String, QuantityItem> map = new HashMap<>();
            itemList.forEach(item -> {
                String key = item.getId() + (item.getWarehouseID() != null ? "_" + item.getWarehouseID() : "");
                if (map.get(key) != null) {
                    map.get(key).setQuantity(map.get(key).getQuantity().add(item.getQuantity()));
                } else {
                    map.put(key, item);
                }
            });

            return map;
        }
        return null;
    }

    private void reEnableButtons(boolean enable) {
        if (saveAndApproveButton != null) {
            saveAndApproveButton.setEnabled(enable);
        }
        if (submitButton != null) {
            submitButton.setEnabled(enable);
        }
        if (saveAsDraft != null) {
            saveAsDraft.setEnabled(enable);
        }
        if (rejectBtn != null) {
            rejectBtn.setEnabled(enable);
        }
    }

    private void showRejectionDialogBox() {
        KpiModal reasonBox = new KpiModal();
        reasonBox.setTitle(wfmStrings.reject());
        reasonBox.setFlexAlignContent(FlexAlignContent.CENTER);

        final TextArea txtReason = new TextArea();
//        txtReason.setWidth("342px");
        txtReason.setHeight("120px");
        txtReason.setStyleName("form-control file--inventoryStockTransferSummaryView"); //https://prnt.sc/rmkekr
        reasonBox.add(txtReason);
        reasonBox.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickevent -> {
            reasonBox.close();
            reEnableButtons(true);
        }));
        reasonBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            String comment = txtReason.getText();
            if (comment == null || "".equals(comment)) {
                txtReason.addStyleName(ERROR_FORM_STYLE);
                Info.warn(accountingMessages.pleaseSpecifyRejectionReason());
                reEnableButtons(true);
                return;
            }
            reasonBox.close();
            updateStatus(STOCK_ADJUSTMENT_DECLINED, comment);
        }));
        reasonBox.setWidth("400px");

        reasonBox.center();
    }

    private void updateStatus(String statusCode, String rejectionReason) {
        LoadingPanel.loading(true);
        ProductService.App.get().updateStockAdjustmentStatus(objectID, statusCode, rejectionReason, new AbstractAsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                reEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Void result) {
                reEnableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.success(), Info.Type.INFO);

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STOCK_ADJUSTMENT_SAVED, result, InventoryStockAdjustmentView.this);
                closeTab();
            }
        });
    }

}
