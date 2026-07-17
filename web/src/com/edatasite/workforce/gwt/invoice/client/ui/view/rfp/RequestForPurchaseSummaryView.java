package com.edatasite.workforce.gwt.invoice.client.ui.view.rfp;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartMeasurementsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellCheckBox;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteAdditionalFields;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 4/9/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForPurchaseSummaryView extends FooteredCustomForm implements Colapse, PermissionConstants, Constants, FittedContent, HasLinksInterface {
    private static final String CONVERT_TO_PO = "CONVERT_TO_PO";
    private static final String CONVERT_TO_RFQ = "CONVERT_TO_RFQ";
    private static final String CONVERT_TO_SA = "CONVERT_TO_SA";

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    private final boolean warehouseIsEnabled = Utils.isMultiWarehouseEnabled();
    private static final QuoteServiceAsync quoteService = QuoteService.App.get();

    private final Integer objectID;
    private HTML dueDate;
    private HTML numberLabel;
    private EditableTable itemTable;
    private ColumnConfig[] columnConfigs;
    private WfmButton2 sendForApprovalButton;
    private WfmButton2 editButton;
    private WfmButton2 approveButton;
    private WfmButton2 rejectButton;
    private SplitButton convertButton;
    private MaterialPanel buttonList;
    private MaterialLink customerBalanceLink;
    private MaterialLink showMoreLink;
    private QuoteAdditionalFields quoteAdditionalFields;
    private FooterInformer link;
    private RFPData rfpData;

    private DataListBox templates;

    public RequestForPurchaseSummaryView(Integer objectID) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.requestForPurchase()));
        this.objectID = objectID;
    }

    private HasLinks linkingUtil;

    @Override
    public Widget onInitialize() {
        super.onInitialize();
        RFPData filter = new RFPData();
        filter.setObjectID(objectID);
        filter.setView(true);
        quoteService.getRFPData(filter, new AsyncCallback<RFPData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(RFPData result) {
                rfpData = result;
                initialize(result);
                renderButtons(result);
                LoadingPanel.loading(false);
            }
        });
        return null;
    }

    private void initialize(RFPData result) {

        addField(AccountingCustomFormConstants.BUTTON_PANEL, buttonList, "");

        if (result.getTemplates() != null && result.getTemplates().length > 0) {
            templates = new DataListBox();
            templates.setItems(result.getTemplates());
            if (result.getSelectedTemplateId() != null) {
                templates.setSelected(result.getSelectedTemplateId());
            }
        }
        dueDate = new HTML(DateUtils.format(result.getDueDate()));
        numberLabel = new HTML(result.getNumberData().getNumberString());
        //HTML employee = new HTML(result.getCreator().getName());
        StringBuilder managerName = new StringBuilder();
        String br = "";
        if (result.getManagers() != null && result.getManagers().size() > 0) {
            for (SelectItem manager : result.getManagers()) {
                managerName.append(br);
                br = "<br>";
                managerName.append(manager.getName());
            }
        } else {
            managerName = new StringBuilder("N/A");

        }
        HTML manager = new HTML(managerName.toString());
        manager.addStyleName("summaryField");
        HTML customer = new HTML(result.getCustomer() != null ? result.getCustomer().getName() : PA_NOT_AVAILABLE_STRING);


        HTML project = null;
        if (Utils.hasPermission(PM_PROJECT_LIST) && result.getRelatedProject() != null && result.getRelatedProject().getId() != null && result.getRelatedProject().getName() != null) {
            project = new HTML();
            project.addClickHandler(click -> Utils.openURLCurrentTab("ProjectManagement.html#project|summary/" + result.getRelatedProject().getId()));
            project.setHTML("<a href=\"javascript:\">" + result.getRelatedProject().getName() + "</a>");
        } else {
            project = new HTML(result.getRelatedProject() != null ? result.getRelatedProject().getName() : PA_NOT_AVAILABLE_STRING);
        }


        quoteAdditionalFields = new QuoteAdditionalFields(ArrayList::new);
        quoteAdditionalFields.getMailAddressFieldContainer().removeFromParent();
        quoteAdditionalFields.getOptionsContainer().removeFromParent();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat RequestForPurchaseSummaryView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), quoteAdditionalFields));

        AtomicBoolean firstClick = new AtomicBoolean(true);
        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(result.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }

        });

        link.setBadgeCount(result.getRelations().size());

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");
        if (result.getCustomer() != null) {
            InvoiceService.App.get().getClientOrSupplier(result.getCustomer().getId(), RECEIVABLE, new AsyncCallback<TypeItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(TypeItem typeItem) {
                    if (typeItem.getSupplierCustomerBalance() >= 0) {
                        customerBalanceLink.setText(AccountingUtils.get().formatPrice(typeItem.getSupplierCustomerBalance()));
                    } else {
                        customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * typeItem.getSupplierCustomerBalance()) + ")");
                    }
                    customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + result.getCustomer().getId() + "/" + CrmAccountItem.CUSTOMER,
                            wfmStrings.balance() + ": " + typeItem.getName(), typeItem.getName()));
                }
            });
        }

        columnConfigs = getColumns(result);
        itemTable = new EditableTable(columnConfigs, false);
        itemTable.setDraggable(false);
        for (int i = 0; i < result.getItems().size(); i++) {
            itemTable.addRow(getWidgets(result.getItems().get(i)));
        }
        initializeButtons();
        initCustomFields(result.getCustomFieldList());

        FormGroup customerField;
        if (result.getCustomer() != null) {
            customerField = new FormGroup(wrapWidgetToFormControl(customer));
            customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);

            Div clientFieldLabel = customerField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");
            clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));


            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);
        } else {
            customerField = new FormGroup(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), wrapWidgetToFormControl(customer));
        }
        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        addField(AccountingCustomFormConstants.INPUT_SHOW_MORE, showMoreField);

        FormGroup requestNumberField = new FormGroup(property.getShortForNumber(wfmStrings.requestForPurchase()), wrapWidgetToFormControl(numberLabel));
        FormGroup managerField = new FormGroup(wfmStrings.approver(), manager);

        addField(AccountingCustomFormConstants.CUSTOMER, customerField);
        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            addField(AccountingCustomFormConstants.PROJECT, new FormGroup(Property.get(Constants.PROJECT, Property.get(Constants.PROJECT, wfmStrings.project())), wrapWidgetToFormControl(project)));
        }
        addField(AccountingCustomFormConstants.DUE_DATE, new FormGroup(wfmStrings.dueDate(), wrapWidgetToFormControl(dueDate)));
        //addField(AccountingCustomFormConstants.EMPLOYEE, employee, getCustomTitle(wfmStrings.creator()));

        addField(AccountingCustomFormConstants.NUMBER, requestNumberField);

        addField(AccountingCustomFormConstants.MANAGER, managerField);
        addField(AccountingCustomFormConstants.ITEMS_TABLE, itemTable, "");
        show();
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        buttonList = new MaterialPanel("btns-group");
        List<Widget> rightSideWidgets = new ArrayList<>();
        rightSideWidgets.add(buttonList);
        return rightSideWidgets;
    }

    private ColumnConfig[] getColumns(RFPData result) {
        int index = 0;
        ColumnConfig[] columns;
        if (result.getCustomItemColumns() != null && result.getCustomItemColumns().length > 0) {

            columns = new ColumnConfig[APPROVE.equals(result.getStatus()) ? result.getCustomItemColumns().length + 1 : result.getCustomItemColumns().length];
            for (ColumnConfigs column : result.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                ColumnConfig columnConfig;

                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, column.isChanged() ? column.getTitle() : wfmStrings.item(), Utils.getColumnWidth(column.getWidth(), 200), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 100), true, Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, column.isChanged() ? column.getTitle() : wfmStrings.measurement(), Utils.getColumnWidth(column.getWidth(), 75), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), Utils.getColumnWidth(column.getWidth(), 75), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY_ON_HAND:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY_ON_HAND, column.isChanged() ? column.getTitle() : wfmStrings.qtyOnHand(), Utils.getColumnWidth(column.getWidth(), 100), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.WAREHOUSE:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.WAREHOUSE, column.isChanged() ? column.getTitle() : accountingStrings.warehouse(), Utils.getColumnWidth(column.getWidth(), 150), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    default:
                        columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                }
            }
            if (APPROVE.equals(result.getStatus()) ) {
                columns[index] = (new ColumnConfig(CustomCell.class, "tickBox", wfmStrings.select(), 120, true));
            }
        } else {
            int columnCount = 6;
            if (warehouseIsEnabled) {
                columnCount++;
            }
            if (APPROVE.equals(result.getStatus()) ) {
                columnCount++;
            }

            columns = new ColumnConfig[columnCount];
            columns[index++] = (new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 250, true));
            columns[index++] = (new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250, false));
            columns[index++] = (new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, wfmStrings.qty(), 60, true));
            columns[index++] = (new ColumnConfig(CustomCell.class, ItemTableConstants.MEASUREMENT, wfmStrings.measurement(), 60, false));
            columns[index++] = (new ColumnConfig(CustomCell.class, ItemTableConstants.QTY_ON_HAND, wfmStrings.qtyOnHand(), 60, false));
            if (warehouseIsEnabled) {
                columns[index++] = (new ColumnConfig(CustomCell.class, ItemTableConstants.WAREHOUSE, accountingStrings.warehouse(), 100, false));
            }
            if (APPROVE.equals(result.getStatus()) ) {
                columns[index++] = (new ColumnConfig(CustomCell.class, "tickBox", wfmStrings.select(), 120, true));
            }

        }
        return columns;
    }

    private Widget[] getWidgets(final RFPItem rfpItem) {
        Widget[] rowWidgets = new Widget[columnConfigs.length];
        int index = 0;

        for (ColumnConfig config : columnConfigs) {
            switch (config.getName()) {
                case ItemTableConstants.PRODUCT:
                    SmartProductLookUp product = new SmartProductLookUp(Constants.PAYABLE);
                    product.setRfpItemid(rfpItem.getObjectID());
                    product.setEnabled(false);
                    if (rfpItem.getProductItem() != null) {
                        product.addProductItem(rfpItem.getProductItem());
                    }

                    rowWidgets[index++] = product;
                    break;
                case ItemTableConstants.DESCRIPTION:
                    rowWidgets[index++] = new CustomCellLabel(rfpItem.getDescription() != null ? rfpItem.getDescription() : "");
                    break;
                case ItemTableConstants.QTY:
                    rowWidgets[index++] = new CustomCellLabel(AccountingUtils.get().formatQty(rfpItem.getQty()));
                    break;
                case ItemTableConstants.MEASUREMENT:
                    SmartMeasurementsLookUp measurment = new SmartMeasurementsLookUp();
                    if (rfpItem.getMeasurement() != null) {
                        measurment.setSelected(rfpItem.getMeasurement());
                    }
                    measurment.setEnabled(false);

                    rowWidgets[index++] = measurment;
                    break;
                case ItemTableConstants.DEPARTMENT:
                    DepartmentLookUp department = new DepartmentLookUp();
                    if (rfpItem.getDepartmentItem() != null) {
                        department.setSelected(rfpItem.getDepartmentItem());
                    }
                    department.setEnabled(false);

                    rowWidgets[index++] = department;
                    break;
                case ItemTableConstants.QTY_ON_HAND:
                    rowWidgets[index++] = new CustomCellLabel(utils.formatQty(rfpItem.getQtyOnhand() != null ? rfpItem.getQtyOnhand() : AccountingConstants.ZERO));
                    break;
                case ItemTableConstants.WAREHOUSE:
                    WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
                    if (rfpItem.getWareHouse() != null) {
                        warehouseLookUp.setSelected(rfpItem.getWareHouse());
                    }
                    warehouseLookUp.setEnabled(false);

                    rowWidgets[index++] = warehouseLookUp;
                    break;

                case "tickBox":
                    CustomCellCheckBox checkBox2 = new CustomCellCheckBox();
//                    if (rfpItem != null && !rfpItem.isHasProductList()) {
//                        checkBox2.setEnabled(false);
//                    }

                    rowWidgets[index++] = checkBox2;
                    break;

                default:
                    CompanyCustomFieldItem customFieldItem = rfpItem.getCustomFieldByCode(config.getName());
                    Label label = new Label();
                    if (customFieldItem != null) {
                        if (DATA_TYPE_DATE.equals(customFieldItem.getDataType())) {
                            if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
                                label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.dateAndTimeFormatShort2(customFieldItem.getFieldDateNonConvertedValue()) : "");
                            } else {
                                label.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(customFieldItem.getFieldDateNonConvertedValue()) : "");
                            }
                        } else if (UI_TYPE_PERCENTAGE.equals(customFieldItem.getUiType())) {
                            label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() + " %" : "");
                        } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                            String finalValue = "";
                            if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                                for (SelectItem selectItem : customFieldItem.getSelectItems()) {
                                    finalValue += selectItem.getName() + "; ";
                                }
                            }
                            label.setText(finalValue);
                        } else {
                            label.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : "");
                        }
                    } else {
                        label.setText("");
                    }

                    rowWidgets[index++] = label;
                    break;

            }
        }

        return rowWidgets;
    }

    private void initializeButtons() {

        sendForApprovalButton = new WfmButton2(wfmStrings.send(), Constants.BTN_DEFAULT_OUTLINE, event -> {
            changeStatus(SUBMITTED_TO_MANAGER, null);
        });
        editButton = new WfmButton2(wfmStrings.edit(), Constants.BTN_DEFAULT_OUTLINE, new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|edit/" + objectID.toString());
            }
        });
        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, event -> {
            changeStatus(APPROVE, null);
        });
        rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT, event -> {
            showRejectionDialogBox();
        });
        convertButton = new SplitButton(140, BTN_PRIMARY);

        List<SplitButtonItem> splitButtonItems = new ArrayList<>();

        splitButtonItems.add(new SplitButtonItem(CONVERT_TO_PO, Property.get(Constants.PURCHASE_ORDER, wfmStrings.convertToo(), wfmStrings.purchaseorder()), () -> {
            ArrayList<Integer> selectedItems = getSelectedItemsId();
            if (!selectedItems.isEmpty()) {
                quoteService.setSelectedRfpItems(selectedItems, objectID, new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        convertToPO();
                    }
                });
            } else {
                Info.show(wfmStrings.pleaseSelectAtLeastOneItem(), Info.Type.WARNING);
            }
        }, true));

        if (Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_ADD)) {
            splitButtonItems.add(new SplitButtonItem(CONVERT_TO_RFQ, property.getSingular(wfmStrings.convertToo(), wfmStrings.requestForQuote()), new Command() {
                @Override
                public void execute() {
                    List<Integer> selectedItems = getSelectedItemsId();
                    if (!selectedItems.isEmpty()) {
                        quoteService.setSelectedRfpItems(getSelectedItemsId(), objectID, new AbstractAsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                super.onFailure(caught);
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                convertToRFQ();
                            }
                        });
                    } else {
                        Info.show(wfmStrings.pleaseSelectAtLeastOneItem(), Info.Type.WARNING);
                    }
                }
            }));
        }
        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_STOCK_ADJUSTMENT_ADD : ACCOUNTING_STOCK_ADJUSTMENT_ADD)) {
            splitButtonItems.add(new SplitButtonItem(CONVERT_TO_SA, accountingStrings.convertToStockAdjustment(), new Command() {
                @Override
                public void execute() {
                    List<Integer> selectedItems = getSelectedItemsId();
                    if (!selectedItems.isEmpty()) {
                        quoteService.setSelectedRfpItems(getSelectedItemsId(), objectID, new AbstractAsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                super.onFailure(caught);
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                convertToStockAdjustment();
                            }
                        });
                    } else {
                        Info.show(wfmStrings.pleaseSelectAtLeastOneItem(), Info.Type.WARNING);
                    }
                }
            }));
        }
        convertButton.addItemList(splitButtonItems);
//        WfmButton2 pdfButton = new WfmButton2(wfmStrings.pdfVersion(), Constants.BTN_DEFAULT_OUTLINE, (ClickHandler) clickEvent -> {
//            Integer templateID = null;
//            if (templates != null) {
//                templateID = templates.getSelectedId();
//                if (templateID == null) {
//                    Validation.validateListBoxRequired(templates, new HTML(), wfmStrings.pleaseSelectTemplate());
//                    Info.warn(wfmStrings.pleaseSelectTemplate());
//                    return;
//                }
//            }
//            InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
//            HashMap<String, String> parameters = requestObject.getRequestParams();
//            parameters.put("number", numberLabel.getText());
//            parameters.put("date", dueDate.getText());
//            if (templateID != null) {
//                parameters.put("templateID", String.valueOf(templateID));
//            }
//            String pdfURL = CommandConstants.PDF_URL + "/requestForPurchsePDFHandler";
//            Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
//        });

        SplitButton printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (templates != null && templates.getItems() != null) {
            templates.getItems();
            for (SelectItem pdfItem : templates.getItems()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), numberLabel.getText(), dueDate.getText())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, numberLabel.getText(), dueDate.getText()), true);
        pdfVersion.ensureDebugId("rfq_" + "pdfVersionItem");
        pdfTemplatesList.add(pdfVersion);
        if (Utils.hasRole(Constants.ADMIN)) {
            pdfTemplatesList.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.RFP.name())));
        }
        printPdfSplitButton.addItemList(pdfTemplatesList);

        sendForApprovalButton.setVisible(false);
        editButton.setVisible(false);
        approveButton.setVisible(false);
        rejectButton.setVisible(false);
        convertButton.setVisible(false);

        buttonList.add(printPdfSplitButton);
        buttonList.add(sendForApprovalButton);
        buttonList.add(editButton);
        buttonList.add(rejectButton);
        buttonList.add(approveButton);
        buttonList.add(convertButton);
    }

    private ArrayList<Integer> getSelectedItemsId() {
        ArrayList<Integer> itemsId = new ArrayList<>();
        if (itemTable != null) {
            for (int i = 0; i < itemTable.getRowCount(); i++) {
                SmartProductLookUp productLookUp = (SmartProductLookUp) itemTable.getColumnById(i, ItemTableConstants.PRODUCT);
                CustomCellCheckBox tickBox = (CustomCellCheckBox) itemTable.getColumnById(i, "tickBox");
                if (tickBox.getValue() && productLookUp.getRfpItemid() != null) {
                    itemsId.add(productLookUp.getRfpItemid());
                } else {

                }
            }
        }
        return itemsId;
    }

    private void initCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            MaterialPanel customFieldsWrapper = new InvoiceCustomFieldsSummaryView(customFieldItems).getCustomsDataView();
            quoteAdditionalFields.initCustomFieldSummaryWidget(customFieldsWrapper);
        }
    }

    private void convertToStockAdjustment() {
        goTo("stockadjustment|add/add/convertFromRFP/" + objectID);
    }

    private void convertToRFQ() {
        goTo("requestforquote|add/add/convertFromRFP/" + objectID);
    }

    private void convertToPO() {
        Utils.openURL("Accounting.html#purchaseorder|add/add/converFromRFP/" + objectID);
//        goTo("purchaseorder|add/add/converFromRFP/" + objectID);
    }

    private void changeStatus(final String statusCode, String rejectionReason) {
        quoteService.changeRFPstatus(objectID, statusCode, rejectionReason, true, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
                if (Constants.APPROVE.equals(statusCode) || Constants.CONVERTED.equals(statusCode)) {
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_PURCHASE_ADD_EDIT, result, RequestForPurchaseSummaryView.this);
                    if (Constants.CONVERTED.equals(statusCode)) {
                        Info.show(wfmStrings.converted() + property.getSingular(accountingStrings.convertedSuccessfullyToPurchaseOrder(), wfmStrings.requestForPurchase()), Info.Type.INFO);
                    }
                } else {
                    closeTab();
                }

            }
        });
    }

    private void showRejectionDialogBox() {
        KpiModal dialogBox = new KpiModal();
        dialogBox.addStyleName("rfp-rejectPopup file--RequestForPurchaseSummaryView");
        dialogBox.setTitle(wfmStrings.rejectionReason());
        dialogBox.getFooter().removeFromParent(); // this is unusable
        WfmForm form = new WfmForm();
        TextArea2 reason = new TextArea2(250);
        form.addField(null, reason);//https://prnt.sc/sohzvt
        form.addButton(new WfmButton2(wfmStrings.reject(), clickEvent -> {
            changeStatus(REJECT, reason.getText());
            dialogBox.close();
        }));
        dialogBox.add(form);
        dialogBox.open();
    }

    private void renderButtons(RFPData result) {
        String status = result.getStatus();
        if (DRAFT.equals(status) && result.isEmployee()) {
            if (result.isCurrentApprover()) {
                approveButton.setVisible(true);
//                if (result.getCreator().equals(result.getCurrentApprover())) {
//                    rejectButton.setVisible(true);
//                }
            } else {
                sendForApprovalButton.setVisible(true);
            }
        }
        if (result.isEmployee() && (DRAFT.equals(result.getStatus()) || REJECT.equals(result.getStatus()) || APPROVE.equals(result.getStatus()))
                || result.isCurrentApprover() && !(REJECT.equals(result.getStatus()) || APPROVE.equals(result.getStatus()) || DRAFT.equals(result.getStatus()))) {
            editButton.setVisible(true);
        }
        if (!DRAFT.equals(status) && !APPROVE.equals(status) && !REJECT.equals(status) && (result.isCurrentApprover() || Utils.hasPermission(ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE))) {
            approveButton.setVisible(true);
            rejectButton.setVisible(true);
        }
        if (APPROVE.equals(status)) {
            convertButton.setVisible(true);
        }

    }

    private void generatePDF(HTMLPanel panel, Integer templateID, String number, String date) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        parameters.put("number", number);
        parameters.put("date", date);
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        String pdfURL = CommandConstants.PDF_URL + "/requestForPurchsePDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private String getCustomTitle(String text, boolean... required) {
        return getTitle("<b class=label>" + text + "</b>", required);
    }

    @Override
    protected void addButtons() {
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.REQUEST_FOR_PURCHASE_FORM;
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
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> result = new ArrayList<>();
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> quoteService.getRFPHistoryNotes(objectID, callback));
        noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
            LoadingPanel.loading(true);
            quoteService.saveRFPNotes(historyListItem, objectID, new AsyncCallback<Integer>() {
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
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        FooterUploadPanel footerUploadPanel = new FooterUploadPanel(F_RFP, objectID, true, wfmStrings.attachments());

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

        informer.setInitialClasses("informer-item history-notes-container");
        result.add(informer);
        result.add(footerUploadPanel);
        result.add(link);
        return result;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(RequestForPurchaseSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.REQUEST_FOR_PURCHASE;
                }

                @Override
                public String getRelationName() {
                    return rfpData != null ? rfpData.getNumberData().getNumberString() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public String getPropertyCode() {
        return Constants.REQUEST_FOR_PURCHASE;
    }
}
