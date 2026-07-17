package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ASSEMBLY_ITEM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT;

public class BuildAssemblySummaryView extends CustomForm2 implements Colapse, Constants {
    private static final BuildAssemblyServiceAsync buildAssemblyService = BuildAssemblyService.App.get();

    private final Integer objectId;
    private AssemblyItem item;

    private HTML numberWidget;
    private HTML product;
    private HTML qtyToBuild;
    private HTML datePicker;
    private HTML warehouse;
    private HTML approvers;
    private DynamicTable itemsTable;
    private SplitButton printPdfSplitButton;

    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private final String buildAsseblyItemView = "build_assebly_item_view_";

    public BuildAssemblySummaryView(Integer objectId) {
        super("buildAssemblyView", accountingStrings.buildAssembly());
        this.objectId = objectId;
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
                BuildAssemblySummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());
        numberWidget = initHTML();
        product = initHTML();
        datePicker = initHTML();
        qtyToBuild = initHTML();
        warehouse = initHTML();
        approvers = initHTML();
        itemsTable = new DynamicTable(getColumns(), false);

        numberWidget.ensureDebugId(buildAsseblyItemView + "numberWidget");
        product.ensureDebugId(buildAsseblyItemView + "productLookUp");
        datePicker.ensureDebugId(buildAsseblyItemView + "datePicker");
        qtyToBuild.ensureDebugId(buildAsseblyItemView + "qtyToBuild");
        warehouse.ensureDebugId(buildAsseblyItemView + "warehouseLookUp");
        itemsTable.ensureDebugId(buildAsseblyItemView + "itemsTable");

        addFields();
    }

    private void addFields() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, numberWidget, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()));
        } else {
            addField(CustomFormConstants.NUMBER, numberWidget, wfmStrings.number());
        }
        if (formPropertyMap != null && formPropertyMap.get(AccountingCustomFormConstants.DATE) != null) {
            addField(AccountingCustomFormConstants.DATE, datePicker, getTitle(formPropertyMap.get(AccountingCustomFormConstants.DATE).isChanged() ? formPropertyMap.get(AccountingCustomFormConstants.DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(AccountingCustomFormConstants.DATE).isRequired()));
        } else {
            addField(AccountingCustomFormConstants.DATE, datePicker, wfmStrings.date());
        }
        if (formPropertyMap != null && formPropertyMap.get(PRODUCT) != null) {
            addField(PRODUCT, product, getTitle(formPropertyMap.get(PRODUCT).isChanged() ? formPropertyMap.get(PRODUCT).getTitle() : wfmStrings.product(), formPropertyMap.get(PRODUCT).isRequired()));
        } else {
            addField(PRODUCT, product, wfmStrings.product());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.QUANTITY) != null) {
            addField(CustomFormConstants.QUANTITY, qtyToBuild, getTitle(formPropertyMap.get(CustomFormConstants.QUANTITY).isChanged() ? formPropertyMap.get(CustomFormConstants.QUANTITY).getTitle() : accountingStrings.qtyToBuild(), true));
        } else {
            addField(CustomFormConstants.QUANTITY, qtyToBuild, accountingStrings.qtyToBuild());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE) != null) {
            addField(CustomFormConstants.WAREHOUSE, warehouse, getTitle(formPropertyMap.get(CustomFormConstants.WAREHOUSE).isChanged() ? formPropertyMap.get(CustomFormConstants.WAREHOUSE).getTitle() : accountingStrings.warehouse(), formPropertyMap.get(CustomFormConstants.WAREHOUSE).isRequired()));
        } else {
            addField(CustomFormConstants.WAREHOUSE, warehouse, accountingStrings.warehouse());
        }
        addField(CustomFormConstants.ASSEMBLY_ITEMS, itemsTable, null);
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    private void drawAssemblyItems() {
        itemsTable.clear();
        NewProduct product = item.getNewProduct();
        if (product != null && product.getAssemblyItems() != null && !product.getAssemblyItems().isEmpty()) {
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
                asItemLookUp.setEnabled(false);
                if (item.getProduct() != null) {
                    asItemLookUp.setSelected(item.getProduct());
                }
                Label description = (Label) tableItem.getColumnById("description");
                description.setText(item.getDescription());

                Label qtyOnHand = (Label) tableItem.getColumnById("qtyOnHand");
                if (item.getItemsInStock() != null) {
                    qtyOnHand.setText(AccountingUtils.get().formatQty(item.getItemsInStock()));
                }

                ExtendedLabel qtyNeeded = (ExtendedLabel) tableItem.getColumnById("qtyNeeded");
                qtyNeeded.setText(AccountingUtils.get().formatQty(item.getQuantity()));
                qtyNeeded.setOldValue(item.getQuantity());
                qtyNeeded.setEnabled(false);

                if (isMultiWarehouseEnabled()) {
                    WarehouseLookUp warehouseLookUp = (WarehouseLookUp) tableItem.getColumnById("warehouse");
                    warehouseLookUp.setEnabled(false);
                    if (item.getProductDefaultWarehouse() != null) {
                        warehouseLookUp.setSelected(item.getProductDefaultWarehouse());
                    }
                }
            }
        }
    }

    public void pdfTool() {
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (item != null && item.getTemplates() != null && item.getTemplates().length > 0) {
            for (SelectItem pdfItem : item.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/buildAssemblyPdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
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
        widgets[index++] = category;

        final ProductLookUp itemLookUp = new ProductLookUp(ASSEMBLY_ITEMS);
        itemLookUp.ensureDebugId(PRODUCT + "productLookUp");
        itemLookUp.setWithoutType(ASSEMBLY_ITEM);

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
        }

        return widgets;
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
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {
        buildAssemblyService.getBuildAssemblyItem(objectId, new AbstractAsyncCallback<AssemblyItem>() {
            @Override
            public void onSuccess(AssemblyItem result) {
                super.onSuccess(result);
                item = result;
                setValues();
                initButtonsPanel();
                drawAssemblyItems();
                pdfTool();
                drawFooter();
            }
        });
    }

    private void setValues() {
        setInnerHTML(numberWidget, item.getNumberData().getNumberString());
        if (item.getAssemblyItem() != null) {
            product.setHTML(item.getAssemblyItem().getName());
        }
        if (item.getDate() != null && item.getDate().getDate() != null) {
            datePicker.setHTML(DateUtils.getDateFormatShort(item.getDate().getDate()));
        }
        if (item.getQuantity() != null) {
            qtyToBuild.setHTML(item.getQuantity().setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP).toString());
        }
        if (isMultiWarehouseEnabled()) {
            warehouse.setHTML(item.getWareHouseItem().getName());
        }
        if (item.getApprover() != null) {
            approvers.setHTML(item.getApprover().getName());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
    }

    private void initButtonsPanel() {
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF)) {
            addRightButton(printPdfSplitButton);
        }

        if (item.isApproveProcessEnabled()) {
            Integer currentUserId = Utils.getUserID();
            Integer currentApproverId = item.getApprover() != null ? item.getApprover().getId() : null;

            WfmButton2 submitButton = addButton(wfmStrings.submit(), BTN_SUCCESS, clickEvent -> updateStatusBuildAssembly(Constants.BUILD_ASSEMBLY_STATUS_SUBMITTED));
            WfmButton2 approveButton = addButton(wfmStrings.approve(), BTN_SUCCESS, clickEvent -> updateStatusBuildAssembly(Constants.BUILD_ASSEMBLY_STATUS_APPROVED));
            WfmButton2 rejectButton = addButton(wfmStrings.reject(), BTN_REJECT, clickEvent -> updateStatusBuildAssembly(Constants.BUILD_ASSEMBLY_STATUS_REJECTED));
            submitButton.setVisible(false);
            approveButton.setVisible(false);
            rejectButton.setVisible(false);

            addField(CustomFormConstants.APPROVERS, approvers, getTitle(wfmStrings.approvers()));
            if (currentUserId.equals(currentApproverId)) {
                if (BUILD_ASSEMBLY_STATUS_SUBMITTED.equals(item.getStatusCode())) {
                    approveButton.setVisible(true);
                    rejectButton.setVisible(true);
                } else if (BUILD_ASSEMBLY_STATUS_REJECTED.equals(item.getStatusCode())) {
                    approveButton.setVisible(true);
                }
            } else {
                if (BUILD_ASSEMBLY_STATUS_REJECTED.equals(item.getStatusCode()) && item.getCreator() != null && currentUserId.equals(item.getCreator().getId())) {
                    submitButton.setVisible(true);
                }
            }
        }
    }

    void updateStatusBuildAssembly(String statusCode) {
        LoadingPanel.loading(true);
        buildAssemblyService.updateStatusBuildAssembly(objectId, statusCode, new AbstractAsyncCallback<Void>() {
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
                super.success(result);
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, BuildAssemblySummaryView.this);
                closeTab();
            }
        });
    }

    private void drawFooter() {
        if (objectId == null) return;
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> buildAssemblyService.loadBuildAssembyNotes(objectId, callback));
        noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
            if (historyItem != null) {
                LoadingPanel.loading(true);
                buildAssemblyService.saveBuildAssemblyNote(objectId, historyItem, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Integer hisItemId) {
                        historyItem.setObjectID(hisItemId);
                        LoadingPanel.loading(false);
                    }
                });
            }
        });


        FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
//            showJournal.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + item.getId() + "/---", accountingStrings.reportView() + ": " + --.getMonth(), accountingStrings.reportView() + ": " + --.getMonth()));
//            showJournal.setBadgeCount(groupPayrunData.getTableItems().length);
//            footer.addToLeftSide(showJournal);
        }

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
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
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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
