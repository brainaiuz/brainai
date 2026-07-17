package com.edatasite.workforce.gwt.invoice.client.ui.view.rfp;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddUnitMeasurementView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.EditProductViewPopup;
import com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd.ProductQuickAddForm;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
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
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartMeasurementsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteAdditionalFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.APPROVE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.FALSE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_RFP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MANAGER_REJECT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.NUMBER_EXIST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.REJECT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.REQUEST_FOR_PURCHASE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RIGHT_ALIGN_CELL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SUBMITTED_TO_MANAGER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DROPDOWN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_MULTI_LOOKUP;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_PERCENTAGE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTAREA;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX_EMAIL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_URL;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 4/9/13
 * Time: 2:38 PM
 */
public class RequestForPurchaseView extends FooteredCustomForm implements Colapse, FittedContent, HasLinksInterface {
    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    private static final Integer DEFAULT_ITEM_ROWS = 3;
    private RFPData rfpData;
    private Integer objectID;
    private Integer copyFromID;
    private DatePicker dueDate;
    private Numbering numberWidget;
    private NumberData numberData;

    //    private AccountingEmployeeLookUp managerLookUp;
    private ChosenApproversWidget approver;
    private Label creator;
    private EditableTable itemTable;
    private EditableGrid grid;
    private ColumnConfig[] columnConfigs;
    private ProjectLookUp projectLookup;
    private LookUp crmAccountLookUp;

    private WfmButton2 saveAsDraftButton;
    private WfmButton2 submitToManager;
    private SplitButton saveAndApprove;

    //    private WfmButton2 approveButton;
    //    private WfmButton2 rejectButton;
    private SplitButton printPdfSplitButton;

    private boolean copy;
    private boolean copyFromQuote;
    private boolean billOfMaterials;
    private boolean fromProject;
    private final boolean warehouseIsEnabled = Utils.isMultiWarehouseEnabled();
    private FormGroup approverField;
    private EditProductViewPopup productviewpopup;
    private FooterUploadPanel footerUploadPanel;
    private NoteHistoryWidget noteHistoryWidget;
    private MaterialLink customerBalanceLink;
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;
    private MaterialLink showMoreLink;
    private QuoteAdditionalFields quoteAdditionalFields;
    private DataListBox templates;
    private SelectItem defaultDepartment;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private FooterInformer link;
    private Integer relationID;
    private String relationType;
    private String relationName;


    public RequestForPurchaseView(Integer objectID) {
        super("edit");
        setDescription(wfmStrings.edit() + "&nbsp;" + property.getSingular(wfmStrings.requestForPurchase()));
        this.objectID = objectID;
    }

    public RequestForPurchaseView(String[] params) {
        super("requestforpurchaseadd");
        setDescription(wfmStrings.add() + "&nbsp;" + property.getSingular(wfmStrings.requestForPurchase()));
        initFormParameters(params);
    }

    private HasLinks linkingUtil;

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.REQUEST_FOR_PURCHASE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void initFormParameters(String[] params) {
        if (params != null) {
            if (params.length >= 3) { // isCopy
                if (params[1] != null && params[1].matches("^\\d+$")) {
                    copyFromID = Integer.parseInt(params[1]);
                }
                if (params[2] != null && params[2].equals("copy")) {
                    copy = true;
                } else if (params[2] != null && params[2].equals("copyFromQuote")) {
                    copyFromQuote = true;
                } else if (params[2] != null && params[2].equals("billOfMaterials")) {
                    billOfMaterials = true;
                    relationID = Integer.parseInt(params[1]);
                    relationType = RelationItem.TYPE_PROJECT;
                } else if (params[2] != null && params[2].equals("project")) {
                    fromProject = true;
                    relationID = Integer.parseInt(params[1]);
                    relationType = RelationItem.TYPE_PROJECT;
                }
            }
        }
    }


    private void getButtons() {

        saveAsDraftButton = new WfmButton2(wfmStrings.draft(), Constants.BTN_DEFAULT_OUTLINE);
        saveAsDraftButton.setVisible(false);
        saveAsDraftButton.addClickHandler(event -> save(DRAFT, null));

        submitToManager = new WfmButton2(wfmStrings.submitForApproval(), BTN_PRIMARY);
        submitToManager.setVisible(false);
        submitToManager.addClickHandler(event -> save(SUBMITTED_TO_MANAGER, null));

        saveAndApprove = new SplitButton(120, BTN_PRIMARY);
        saveAndApprove.ensureDebugId("RFP_saveAndApprove");

//        approveButton = new WfmButton2(accountingStrings.approve(), WfmButton2.BTN_PRIMARY);
//        approveButton.setVisible(false);
//        approveButton.addClickHandler(clickEvent -> save(APPROVE, null));

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.setVisible(false);
    }

    private ColumnConfig[] getColumns(RFPData result) {
        ColumnConfig[] columns;
        int index = 0;
        if (result.getCustomItemColumns() != null && result.getCustomItemColumns().length > 0) {
            columns = new ColumnConfig[result.getCustomItemColumns().length];
            for (ColumnConfigs column : result.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                ColumnConfig columnConfig;

                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, column.isChanged() ? column.getTitle() : wfmStrings.item(), Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, column.isChanged() ? column.getTitle() : wfmStrings.measurement(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.QTY_ON_HAND:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY_ON_HAND, column.isChanged() ? column.getTitle() : wfmStrings.qtyOnHand(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    case ItemTableConstants.WAREHOUSE:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.WAREHOUSE, column.isChanged() ? column.getTitle() : accountingStrings.warehouse(), Utils.getColumnWidth(column.getWidth(), 150), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columns[index++] = columnConfig;
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165), column.isRequired(), true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columns[index++] = columnConfig;
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), true);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columns[index++] = columnConfig;
                        }
                        break;
                }
            }
        } else {
            columns = new ColumnConfig[warehouseIsEnabled ? 6 : 5];
            columns[index++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 200, true);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, wfmStrings.qty(), 75, true, RIGHT_ALIGN_CELL);
            columns[index++] = new ColumnConfig(LookUpCell.class, DEPARTMENT, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), 75, false);
            columns[index++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, wfmStrings.measurement(), 75, false);
            columns[index++] = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY_ON_HAND, wfmStrings.qtyOnHand(), 75, false, RIGHT_ALIGN_CELL);
            if (warehouseIsEnabled) {
                columns[index] = new ColumnConfig(LookUpCell.class, ItemTableConstants.WAREHOUSE, accountingStrings.warehouse(), 100, true, RIGHT_ALIGN_CELL);
            }
        }
        return columns;
    }

    private Widget[] getWidgets(RFPItem rfpItem, RFPData rfpData) {
        Widget[] rowWidgets = new Widget[columnConfigs.length];
        boolean validRfpItem = rfpItem != null;
        int index = 0;

        final SmartProductLookUp productLookUp = new SmartProductLookUp(Constants.PAYABLE);
        TextArea2 description = new TextArea2(10000);
//        ItemUploadTable uploadPanel = new ItemUploadTable(F_RFP);
        CustomCellLabel requestedQty = new CustomCellLabel();//bill of material qty
        CustomCellTextBox qtyTxtBox = new CustomCellTextBox(true);
        CustomCellTextBox qtyOnHandTxtBox = new CustomCellTextBox();
        final SmartMeasurementsLookUp measurementsLookUp = new SmartMeasurementsLookUp();
        final DepartmentLookUp departmentLookUp = new DepartmentLookUp();
        WarehouseLookUp warehouseLookUp = new WarehouseLookUp();
        for (ColumnConfig config : columnConfigs) {
            switch (config.getName()) {
                case ItemTableConstants.PRODUCT:
//                    productLookUp.setLinkCommand(() -> {
//                        if (productviewpopup == null) {
//                            productviewpopup = new EditProductViewPopup(item -> {
//                                productviewpopup.close();
//                                productLookUp.addProductItem(item);
//                                if (productLookUp.getOnSelectListener() != null) {
//                                    productLookUp.getOnSelectListener().execute();
//                                }
//                            }, REQUEST_FOR_PURCHASE);
//                        }
//                        productviewpopup.center();
//                    });


                    productLookUp.setLinkCommand(() -> new ProductQuickAddForm(true, AccountingConstants.INVENTORY_ITEM, item -> {
                        productLookUp.addProductItem(item);
                        description.setText(((ProductSelectItem) productLookUp.getSelectedData()).getDescription());
                        if (productLookUp.getOnSelectListener() != null) {
                            productLookUp.getOnSelectListener().execute();
                        }
                    }));
                    productLookUp.setEnabled(!config.isDisabled());
                    productLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                        if (productLookUp.getSelectedItem().getDescription() != null) {
                            description.setText(productLookUp.getSelectedItem().getDescription());
                            CustomCell descriptionBoxCustomCell = (CustomCell) itemTable.getColumnCellWidgetById(itemTable.getGrid().getCurrentRow(), ItemTableConstants.DESCRIPTION);
                            descriptionBoxCustomCell.isActive();

                        }
                        if (productLookUp.getSelectedItem().getQtyOnHand() != null) {
                            qtyOnHandTxtBox.setText(utils.formatPrice(productLookUp.getSelectedItem().getQtyOnHand()));
                            CustomCell qtyOnHandTxtBoxCustomCell = (CustomCell) itemTable.getColumnCellWidgetById(itemTable.getGrid().getCurrentRow(), ItemTableConstants.QTY_ON_HAND);
                            qtyOnHandTxtBoxCustomCell.InActive();
                        }
                    });
                    if (rfpItem != null && rfpItem.getEntityID() != null) {
                        productLookUp.setLayoutData(rfpItem.getEntityID());
                    }
                    productLookUp.getSuggestBox().setWidth("200px");
                    productLookUp.setAutocompleteOff();
                    if ((!copy && rfpData != null && SUBMITTED_TO_MANAGER.equals(rfpData.getStatus()) && rfpData.isCurrentApprover())
                            || (APPROVE.equals(rfpData.getStatus()) && !rfpData.isCurrentApprover()) || billOfMaterials) {
                        productLookUp.setEnabled(false);
                    }

                    rowWidgets[index++] = productLookUp;

                    if (validRfpItem) {
                        if (rfpItem.getProductItem() != null) {
                            productLookUp.addProductItem(rfpItem.getProductItem());
                        }
                    }
                    break;
                case ItemTableConstants.DESCRIPTION:
                    description.hideCharacterLimitPanel();
                    description.setEnabled(!config.isDisabled());
                    rowWidgets[index++] = description;

                    if (validRfpItem) {
                        description.setText(rfpItem.getDescription());
                    }
                    break;
                case ItemTableConstants.QTY:
                    qtyTxtBox.setWidth("110px");
                    qtyTxtBox.setEnabled(!config.isDisabled());
                    qtyTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    rowWidgets[index++] = qtyTxtBox;
                    Validation.addNumericKeyboardListener(qtyTxtBox, 2);

                    if (validRfpItem) {
                        if (rfpItem.getQty() != null) {
                            qtyTxtBox.setText(utils.formatQty(rfpItem.getQty()));
                            requestedQty.setText(utils.formatQty(rfpItem.getQty()));
                        }
                    }
                    if ((!copy && rfpData != null && SUBMITTED_TO_MANAGER.equals(rfpData.getStatus()) && rfpData.isCurrentApprover()
                            || (APPROVE.equals(rfpData.getStatus()) && !rfpData.isCurrentApprover()) || billOfMaterials)) {
                        qtyTxtBox.setEnabled(false);
                    }
                    break;
                case DEPARTMENT:
                    rowWidgets[index++] = departmentLookUp;
                    departmentLookUp.setEnabled(!config.isDisabled());

                    if (validRfpItem) {
                        if (rfpItem.getDepartmentItem() != null) {
                            departmentLookUp.addItem(rfpItem.getDepartmentItem());
                        } else if (defaultDepartment != null) {
                            departmentLookUp.setSelected(defaultDepartment);
                        }
                    }

                    break;
                case ItemTableConstants.MEASUREMENT:

                    measurementsLookUp.addStyleName("lookUp-moveRight");
                    measurementsLookUp.setEnabled(!config.isDisabled());
                    measurementsLookUp.getSuggestBox().setWidth("100%");
                    measurementsLookUp.setLinkCommand(() -> {
                        ObjectCommand command = item1 -> measurementsLookUp.addMeasurementUnit((SelectItem) item1);
                        new AddUnitMeasurementView(null, command);
                    });
                    measurementsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> measurementsLookUp.islink());
                    if (validRfpItem) {
                        if (rfpItem.getMeasurement() != null) {
                            measurementsLookUp.addItem(rfpItem.getMeasurement());
                        }
                    }
                    rowWidgets[index++] = measurementsLookUp;

                    break;
                case ItemTableConstants.QTY_ON_HAND:
                    qtyOnHandTxtBox.setWidth("110px");
                    qtyOnHandTxtBox.setEnabled(!config.isDisabled());
                    qtyOnHandTxtBox.setEnabled(false);
                    Validation.addNumericKeyboardListener(qtyOnHandTxtBox, 2);
                    qtyOnHandTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    rowWidgets[index++] = qtyOnHandTxtBox;

                    if (validRfpItem) {
                        if (rfpItem.getQtyOnhand() != null) {
                            qtyOnHandTxtBox.setText(utils.formatQty(rfpItem.getQtyOnhand()));
                        }
                    }
                    break;

                case ItemTableConstants.WAREHOUSE:
                    warehouseLookUp.getSuggestBox().setWidth("165px");
                    warehouseLookUp.setEnabled(!config.isDisabled());
                    rowWidgets[index++] = warehouseLookUp;
                    if (validRfpItem) {
                        if (rfpItem.getWareHouse() != null) {
                            warehouseLookUp.addItem(rfpItem.getWareHouse());
                        }
                    }
                    if ((!copy && rfpData != null && SUBMITTED_TO_MANAGER.equals(rfpData.getStatus()) && rfpData.isCurrentApprover())
                            || (APPROVE.equals(rfpData.getStatus()) && !rfpData.isCurrentApprover())) {
                        warehouseLookUp.setEnabled(false);
                    }
                    break;
                default:
                    CompanyCustomFieldItem fieldItem = customFieldsMap.get(config.getName()).cloneObject();
                    if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType()) || UI_TYPE_URL.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomTextBoxField(fieldItem);
                    } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomPercentageField(fieldItem);
                    } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDropDownField(fieldItem);
                    } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDatePicker(fieldItem);
                    } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomDateTime(fieldItem);
                    } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomTextAreaField(fieldItem);
                    } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomFieldLookUpField(fieldItem);
                    } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                        rowWidgets[index] = new CustomFieldMultiLookUpField(fieldItem);
                    }
                    if (validRfpItem && rfpItem.getItemCustomFields() != null && !rfpItem.getItemCustomFields().isEmpty()) {
                        CompanyCustomFieldItem fitem = rfpItem.getCustomFieldByCode(fieldItem.getColumnCode());
                        if (fitem != null) {
                            ((CustomFieldInterface) rowWidgets[index]).setFieldItem(fitem);
                        }
                    }
                    index++;
                    break;
            }
        }
        return rowWidgets;
    }

    private void initialize() {
        setLabelEndParameter("");
        dueDate = new DatePicker(true);
        dueDate.setDate(new Date());
        dueDate.ensureDebugId("rfp_view_dueDate");
        numberWidget = new Numbering();

        creator = new Label();

        numberWidget.getTxtPrefix().setWidth("90px");
        numberWidget.getTxtNumber().setWidth("100px");
        numberWidget.getTxtNumber().setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        boolean hasPermissonCustomerQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.CUSTOMER, true, () -> {
            if (hasPermissonCustomerQuick) {
                new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonCustomerAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
            }
        }, false, hasPermissonCustomerQuick || hasPermissonCustomerAdd);

        crmAccountLookUp.ensureDebugId("rfp_view_crmAccountLookUp");

        crmAccountLookUp.getSuggestBox().addSelectionHandler(sh -> {
            if (crmAccountLookUp.getSelectedItemID() != null) {
                InvoiceService.App.get().getClientOrSupplier(crmAccountLookUp.getSelectedItemID(), RECEIVABLE, new AsyncCallback<TypeItem>() {
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
                        customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + crmAccountLookUp.getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER,
                                wfmStrings.balance() + ": " + typeItem.getName(), typeItem.getName()));

                        if (typeItem.getDefaultDepartment() != null) {
                            defaultDepartment = typeItem.getDefaultDepartment();
                            for (int i = 0; i < grid.getRowCount(); i++) {
                                ProductLookUp productLookUp = (ProductLookUp) itemTable.getColumnById(i, ItemTableConstants.PRODUCT);
                                if (productLookUp != null && productLookUp.getSelectedItem() == null) {
                                    return;
                                }
                                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemTable.getColumnById(i, DEPARTMENT);
                                if (departmentLookUp != null) {
                                    if (defaultDepartment != null) {
                                        departmentLookUp.setSelected(defaultDepartment);
                                    } else {
                                        departmentLookUp.clear();
                                    }
                                    LookUpCell dCell = (LookUpCell) itemTable.getColumnCellWidgetById(i, DEPARTMENT);
                                    dCell.InActive();
                                }
                            }
                        }
                    }
                });
            }
        });
        projectLookup = new ProjectLookUp(Constants.PAYABLE);
        projectLookup.setAutocompleteOff();
        projectLookup.setVisible(true);
        projectLookup.ensureDebugId("rfp_view_project");
        projectLookup.setEnabled(!billOfMaterials && !fromProject);


        approver = new ChosenApproversWidget(CrmConstants.REQUEST_FOR_PURCHASE, objectID);
        approver.ensureDebugId("rfp_view_approver");

        quoteAdditionalFields = new QuoteAdditionalFields(ArrayList::new);
        quoteAdditionalFields.getOptionsContainer().removeFromParent();
        quoteAdditionalFields.getMailAddressFieldContainer().removeFromParent();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat RequestForPurchaseView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), quoteAdditionalFields));


        //GBoxItem creatorField = new GBoxItem(creator, getCustomTitle(wfmStrings.creator()));
        FormGroup customerField = new FormGroup(crmAccountLookUp);
        customerField.setAutocompleteOff();
        customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);

        Div clientFieldLabel = customerField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");

        clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        clientFieldLabel.add(balance);
        FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, Property.get(Constants.PROJECT, wfmStrings.project())), projectLookup, false);
        FormGroup dueDateField = new FormGroup(wfmStrings.dueDate(), dueDate, true);

        FormGroup requestNumberField = new FormGroup(property.getShortForNumber(wfmStrings.requestForPurchase()), numberWidget);

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");

        approverField = new FormGroup(wfmStrings.approver(), approver, true);
        approverField.setAutocompleteOff();

        //addField(AccountingCustomFormConstants.EMPLOYEE,creatorField);
        addField(AccountingCustomFormConstants.CUSTOMER, customerField);
        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            addField(AccountingCustomFormConstants.PROJECT, projectField);
        }
        addField(AccountingCustomFormConstants.DUE_DATE, dueDateField);

        addField(AccountingCustomFormConstants.NUMBER, requestNumberField);

        addField(AccountingCustomFormConstants.MANAGER, approverField);

        addField(AccountingCustomFormConstants.ITEMS_TABLE, itemTable);
        addField(AccountingCustomFormConstants.INPUT_SHOW_MORE, showMoreField);

        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }

        show();
    }

    private void generateRfpNumber() {
        QuoteService.App.get().generateRfpNumber(new AsyncCallback<NumberData>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(NumberData result) {
                numberData = result;
                numberWidget.setNumberData(numberData);
            }
        });
    }

//    private void showRejectionReason(String value) {
//        DOM.getElementById("rejectionReason").getStyle().setVisibility(Style.Visibility.VISIBLE);
//        DOM.getElementById("rejectionReasonLabel").getStyle().setVisibility(Style.Visibility.VISIBLE);
//        DOM.getElementById("rejectionReasonLabel").setInnerHTML(value);
//    }

    private void reloadButtons() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, RequestForPurchaseView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApprove.setVisible(true);
                        submitToManager.setVisible(false);
                    } else {
                        saveAndApprove.setVisible(false);
                        submitToManager.setVisible(true);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApprove.setVisible(true);
                        submitToManager.setVisible(false);
                    } else {
                        saveAndApprove.setVisible(false);
                        submitToManager.setVisible(true);
                    }
                }
            }
        });
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        SplitButtonItem saveButtonItem;
        if (objectID != null) {
            if (DRAFT.equals(rfpData.getStatus()) || REJECT.equals(rfpData.getStatus()) || (SUBMITTED_TO_MANAGER.equals(rfpData.getStatus()) && !rfpData.getCurrentApprover().getId().equals(Utils.getUserID()))) {
                saveAsDraftButton.setVisible(true);
            }
            if (rfpData.getCurrentApprover() != null && rfpData.getCurrentApprover().getId() != null) {
                saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), () -> save(APPROVE, null), true);
                saveButtonItem.ensureDebugId("saveButtonItem");
                splitButtonItems.add(saveButtonItem);
                if ((SUBMITTED_TO_MANAGER.equals(rfpData.getStatus()) && !rfpData.getCurrentApprover().getId().equals(Utils.getUserID()))
                        || (MANAGER_REJECT.equals(rfpData.getStatus()) && !rfpData.getCurrentApprover().getId().equals(Utils.getUserID()))) {
                    saveAndApprove.setVisible(false);
                }
                if (approver != null && approver.getFirstApproverLookUp() != null
                        && approver.getFirstApproverLookUp().getSelectedItem() != null
                        && !approver.getFirstApproverLookUp().getSelectedItem().getId().equals(Utils.getUserID())) {
                    submitToManager.setVisible(true);
//                        viewInterface.getApproveSplitButton().setVisible(false);
                } else {
                    saveAndApprove.setVisible(true);
                }
            } else {
                if (DRAFT.equals(rfpData.getStatus()) || REJECT.equals(rfpData.getStatus())) {
                    saveAsDraftButton.setVisible(true);
                }
                saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), () -> save(APPROVE, null), true);
                saveButtonItem.ensureDebugId("saveButtonItem");
                splitButtonItems.add(saveButtonItem);
            }
        } else {
            if (rfpData.getCurrentApprover() != null && rfpData.getCurrentApprover().getId() != null) {
                submitToManager.setVisible(!rfpData.getCurrentApprover().getId().equals(Utils.getUserID()));
            }
            saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), () -> save(APPROVE, null), true);
            saveButtonItem.ensureDebugId("saveButtonItem");
            splitButtonItems.add(saveButtonItem);
        }
        saveAndApprove.addItemList(splitButtonItems);
        saveAsDraftButton.setVisible(true);
    }

    private void pdfOption(RFPData result) {
        if (result.getTemplates() != null && result.getTemplates().length > 0) {
            templates = new DataListBox();
            templates.setItems(result.getTemplates());
            if (result.getSelectedTemplateId() != null) {
                templates.setSelected(result.getSelectedTemplateId());
            }
        }


        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (templates != null && templates.getItems() != null) {
            templates.getItems();
            for (SelectItem pdfItem : templates.getItems()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), result.getNumberData().getNumberString(), dueDate.getText())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, result.getNumberData().getNumberString(), dueDate.getText()), true);
        pdfVersion.ensureDebugId("rfq_" + "pdfVersionItem");
        pdfTemplatesList.add(pdfVersion);
        if (Utils.hasRole(ADMIN)) {
            pdfTemplatesList.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.RFP.name())));
        }
        printPdfSplitButton.addItemList(pdfTemplatesList);
        printPdfSplitButton.setVisible(true);

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

    private void initTable(RFPData result) {

        columnConfigs = getColumns(result);
        itemTable = new EditableTable(columnConfigs, true, true);
        itemTable.setDraggable(true);
        itemTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemTable.addRow(getWidgets(null, result));
            }

            @Override
            public void removeRow() {
            }
        });

        grid = itemTable.getGrid();
        if (objectID == null && !(copy || billOfMaterials)) {
            for (int i = 0; i < DEFAULT_ITEM_ROWS; i++) {
                itemTable.addRow(getWidgets(null, result));
            }
        }
        addField(AccountingCustomFormConstants.ITEMS_TABLE, itemTable, "");
    }

    private void save(String statusCode, String rejectionReason) {
        if (!validate(statusCode)) {
            return;
        }
        LoadingPanel.loading(true);
        rfpData = setRfpData(statusCode, rejectionReason);
        QuoteService.App.get().saveRFPData(rfpData, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(String result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REQUEST_FOR_PURCHASE_ADD_EDIT, result, RequestForPurchaseView.this);
                if (NUMBER_EXIST.equals(result)) {
                    Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.requestForPurchase()), Info.Type.WARNING);
                    return;
                } else if (FALSE.equals(result)) {
                    Info.show(wfmStrings.requestQtyShouldBeLessThanPlannedQty(), Info.Type.WARNING);
                    return;
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.requestForPurchase()));
                    closeTab("accounting|requestforpurchase");
                }
                submitToManager.setEnabled(false);
                saveAsDraftButton.setEnabled(false);
                saveAndApprove.setEnabled(false);
//                approveButton.setEnabled(false);
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        RFPData filter = new RFPData();
        filter.setCopy(copy);
        filter.setCopyFromQuote(copyFromQuote);
        filter.setObjectID(copy || copyFromQuote || billOfMaterials ? copyFromID : objectID);
        filter.setProjectID(copyFromID);
        filter.setFromBillOfMaterials(billOfMaterials);
        filter.setFromProject(fromProject);
        QuoteService.App.get().getRFPData(filter, new AsyncCallback<RFPData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(RFPData result) {
                rfpData = result;
                if (result.getItemCustomFields() != null && !result.getItemCustomFields().isEmpty()) {
                    customFieldsMap = new HashMap<>();
                    for (CompanyCustomFieldItem field : result.getItemCustomFields()) {
                        customFieldsMap.put(field.getColumnCode(), field);
                    }
                }
                initTable(result);
                numberWidget.setNumberData(result.getNumberData());
                creator.setText(result.getCreator().getName());
                if (result.getObjectID() != null || copy || billOfMaterials || fromProject) {
                    if (result.getDueDate() != null) {
                        dueDate.setDate(result.getDueDate());
                    }
                    if (result.getRelatedProject() != null) {
                        projectLookup.addItem(result.getRelatedProject());
                    }
                }
                if (result.getCustomer() != null) {
                    crmAccountLookUp.setSelected(result.getCustomer());
                }
//                if (REJECT.equals(result.getStatus())) {
//                    showRejectionReason(result.getRejectionReason());
//                }
                if (result.getCustomFieldList() != null && result.getCustomFieldList().size() > 0) {
                    quoteAdditionalFields.createAndAppendQuoteCustomFieldsView(ViewAddFiledsCodeName.RequestForPurchaseAdd, result.getCustomFieldList());
                }
                if (objectID != null && !rfpData.isApproverSaved()) {
                    approver.reloadApproverWidgets(RelationItem.REQUEST_FOR_PURCHASE, null);
                } else {
                    approver.reloadApproverWidgets(RelationItem.REQUEST_FOR_PURCHASE, objectID);
                }
                reloadButtons();
                if (objectID != null || (objectID == null && copy) || (objectID == null && billOfMaterials)) {
                    for (int i = 0; i < result.getItems().size(); i++) {
                        itemTable.addRow(getWidgets(result.getItems().get(i), result));
                    }

                    Integer additionalRows = DEFAULT_ITEM_ROWS - result.getItems().size();
                    for (int i = 0; i < additionalRows; i++) {
                        itemTable.addRow(getWidgets(null, result));
                    }

                    if (DRAFT.equals(result.getStatus())) {
                        pdfOption(result);
                    }
                }
                if (objectID != null) {
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
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private boolean validate(String statusCode) {
        int errors = 0;
        if (!Validation.validateDate(dueDate)) {
            errors++;
        }
        if (!validateCustomFields()) {
            errors++;
        }
//        if (!approver.isValid()) {
//            errors++;
//        }
        if (!numberWidget.validate()) {
            errors++;
        }
        if (!validateItemTable(statusCode)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateCustomFields() {
        boolean validate = quoteAdditionalFields.validateCustomFieldRequiredFields();

        //if there are not validate fields in the custom fields
        //then we should open the advanced pop-up

        if (!validate) {
            quoteAdditionalFields.getCustomFieldContainer().setActive(0);

            showAdvancedOptions(wfmStrings.additionalFields(), quoteAdditionalFields);
        }
        return validate;
    }

    private RFPData setRfpData(String statusCode, String rejectionReason) {
        RFPData rfpData = new RFPData();
        rfpData.setObjectID(objectID);
        rfpData.setDueDate(dueDate.getDate());

        rfpData.setApprovers(approver.getChosenApprovers());

        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
            rfpData.setSelfApprover(true);
        }
        if (numberWidget != null) {
            numberData = numberWidget.getNumberData(false);
            rfpData.setNumberData(numberData);
            rfpData.setNumber(numberWidget.getNumberData(false).getNumberString());
        }
        rfpData.setStatus(statusCode);
        if (REJECT.equals(statusCode)) {
            rfpData.setRejectionReason(rejectionReason);
        }
        rfpData.setRelatedProject(projectLookup.getSelectedItem());
        rfpData.setCustomer(crmAccountLookUp.getSelectedItem());
        rfpData.setIsEmployee(true);
        rfpData.setIsCurrentApprover(false);
        rfpData.setCreator(this.rfpData.getCreator());
        rfpData.setFromBillOfMaterials(billOfMaterials);
        if (objectID == null) {
            if (relationID != null && relationType != null) {
                ArrayList<RelationItem> relations = new ArrayList<>();
                relations.add(new RelationItem(null, relationID, relationType, relationName, null, REQUEST_FOR_PURCHASE, null));
                rfpData.setRelations(relations);
            }
        } else {
            if (firstClick.get()) {
                rfpData.setRelations(item != null ? this.rfpData.getRelations() : null);
            } else {
                rfpData.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
            }
        }
        if (quoteAdditionalFields.getCustomFieldsData() != null) {
            rfpData.setCustomFieldList(quoteAdditionalFields.getCustomFieldsData());
        }

        EditableGrid grid = itemTable.getGrid();
        ArrayList<RFPItem> itemList = new ArrayList<>();
        for (int i = 0; i < grid.getRowCount(); i++) {
            if (itemTable.isItemValid(i)) {
                SmartProductLookUp productLookUp = (SmartProductLookUp) itemTable.getColumnById(i, ItemTableConstants.PRODUCT);
                TextArea2 descriptionTxtArea = (TextArea2) itemTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                SmartMeasurementsLookUp measurementLookUp = (SmartMeasurementsLookUp) itemTable.getColumnById(i, ItemTableConstants.MEASUREMENT);
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                CustomCellTextBox quantityOnHandTxtBox = (CustomCellTextBox) itemTable.getColumnById(i, ItemTableConstants.QTY_ON_HAND);


                RFPItem rfpItem = new RFPItem();

                if (productLookUp != null) {
                    if (productLookUp.getSelectedData() != null) {
                        rfpItem.setProductItem((ProductSelectItem) productLookUp.getSelectedData());
                    } else {
                        if (!wfmStrings.searchTypeMessage().equals(productLookUp.getText())) {
                            rfpItem.setProductItem(new ProductSelectItem(null, productLookUp.getText()));
                        }
                    }
                    rfpItem.setEntityID(productLookUp.getLayoutData() != null ? (Integer) productLookUp.getLayoutData() : null);
                }
                if (descriptionTxtArea != null) {
                    rfpItem.setDescription(descriptionTxtArea.getText());
                }
                if (measurementLookUp != null && measurementLookUp.getSelectedItem() != null) {
                    rfpItem.setMeasurement(measurementLookUp.getSelectedItem());
                }
                if (departmentLookUp != null && departmentLookUp.getSelectedItem() != null) {
                    rfpItem.setDepartmentItem(departmentLookUp.getSelectedItem());
                }

                CustomCellTextBox quantityTxtBox = (CustomCellTextBox) itemTable.getColumnById(i, ItemTableConstants.QTY);
                rfpItem.setQty(AccountingUtils.get().parseToBigDecimal(quantityTxtBox.getText()));

                if (quantityOnHandTxtBox != null) {
                    rfpItem.setQtyOnhand(AccountingUtils.get().parseToBigDecimal(quantityOnHandTxtBox.getText()));
                }

                if (warehouseIsEnabled) {
                    WarehouseLookUp warehouseLookUp = (WarehouseLookUp) itemTable.getColumnById(i, ItemTableConstants.WAREHOUSE);
                    if (warehouseLookUp != null && warehouseLookUp.getSelectedItem() != null) {
                        rfpItem.setWareHouse(warehouseLookUp.getSelectedItem());
                    }
                }
                rfpItem.setEntityID(productLookUp.getLayoutData() != null ? (Integer) productLookUp.getLayoutData() : null);

                if (customFieldsMap != null && !customFieldsMap.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();
                    for (String key : customFieldsMap.keySet()) {
                        CustomFieldInterface customField = (CustomFieldInterface) itemTable.getColumnById(i, key);
                        if (customField != null) {
                            fieldItems.add(customField.getFieldItem());
                        }
                    }

                    if (!fieldItems.isEmpty()) {
                        rfpItem.setItemCustomFields(fieldItems);
                    }
                }
                if (rfpItem.getProductItem() != null && rfpItem.getProductItem().getName() != null) {
                    itemList.add(rfpItem);
                }
            }
        }
        rfpData.setItems(itemList);
        rfpData.setAttachments(footerUploadPanel.getAttachedFiles());
        rfpData.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        return rfpData;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();
        getButtons();

        Div pdfWrapper = new Div();
        pdfWrapper.add(printPdfSplitButton);

        Div draftWrapper = new Div();
        draftWrapper.add(saveAsDraftButton);

        Div submitWrapper = new Div();
        submitWrapper.add(submitToManager);

        Div approveWrapper = new Div();
        approveWrapper.add(saveAndApprove);

        result.add(pdfWrapper);
        result.add(draftWrapper);
        result.add(submitWrapper);
        result.add(approveWrapper);
        return result;
    }

    private boolean validateItemTable(String status) {
        int errors = 0;
        itemTable.setValidRows(0);
        EditableGrid grid = itemTable.getGrid();
        List<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();

        for (ColumnConfig config : columnConfigs) {
            if (customFieldsMap != null && customFieldsMap.containsKey(config.getName()) && (customFieldsMap.get(config.getName()).isRequired() ||
                    (UI_TYPE_TEXTBOX_EMAIL.equals(customFieldsMap.get(config.getName()).getUiType())) ||
                    (UI_TYPE_URL.equals(customFieldsMap.get(config.getName()).getUiType())) ||
                    (UI_TYPE_PERCENTAGE.equals(customFieldsMap.get(config.getName()).getUiType())))) {
                requiredAndEmailCFs.add(customFieldsMap.get(config.getName()));
            }
        }
        boolean errorFound = false;


        ArrayList<String> requiredColumnCodes = new ArrayList<>();
        int requiredRow = 0;
        if (rfpData.getCustomItemColumns() != null && rfpData.getCustomItemColumns().length > 0) {
            for (ColumnConfigs columnConfigs : rfpData.getCustomItemColumns()) {
                if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                    requiredRow++;
                    requiredColumnCodes.add(columnConfigs.getCode());
                }
            }
        } else {
            requiredRow = warehouseIsEnabled ? 3 : 2;
        }
        for (int rowID = 0; rowID < grid.getRowCount(); rowID++) {
            int rowError = 0;
            itemTable.resetValidation(rowID);
            rowError = validateRequiredItems(rowID, status, requiredAndEmailCFs, requiredColumnCodes)[0];

            if (rowError == 0) {
                itemTable.setItemValid(rowID, true);
                itemTable.incValidRow();
            } else if (rowError == requiredRow + requiredAndEmailCFs.size() - validateRequiredItems(rowID, status, requiredAndEmailCFs, requiredColumnCodes)[1]) {
                if (!areOtherRowsAffected(rowID)) {
                    itemTable.setItemValid(rowID, false); // exclude
                } else {
                    colorizeErrorField(rowID, status, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            } else {
                colorizeErrorField(rowID, status, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }
        }
        if (itemTable.getValidRows() == 0) {
            colorizeErrorField(0, status, requiredAndEmailCFs, requiredColumnCodes);
            errorFound = true;
        }
        if (customFieldsMap != null && customFieldsMap.values().size() > 0) {
            return Validation.itemTableNumericCFMinValueValidate(itemTable, customFieldsMap.values());
        } else {
            return !errorFound;
        }
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
    public String getPropertyCode() {
        return REQUEST_FOR_PURCHASE;
    }

    private int[] validateRequiredItems(int rowID, String status, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[2];
        SmartProductLookUp productLookUp = (SmartProductLookUp) itemTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        WarehouseLookUp warehouseLookUp = (WarehouseLookUp) itemTable.getColumnById(rowID, ItemTableConstants.WAREHOUSE);

        if (!DRAFT.equals(status)) {

            if (requiredColumnCodes.isEmpty()) {
                if (productLookUp.getText() == null || productLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(productLookUp.getText())) {
                    itemTable.setColumnValid(ItemTableConstants.PRODUCT);
                    errors++;
                }

                CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemTable.getColumnById(rowID, ItemTableConstants.QTY);
                if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                    itemTable.setColumnValid(ItemTableConstants.QTY);
                    errors++;
                } else {
                    if (utils.parseToBigDecimal(qtyTxtBox.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                        itemTable.setColumnValid(ItemTableConstants.QTY);
                        errors++;
                    }

                }

                if (Utils.isMultiWarehouseEnabled()) {
                    if (!Validation.validateLookUpRequired(warehouseLookUp)) {
                        itemTable.setColumnValid(ItemTableConstants.WAREHOUSE);
                        errors++;
                    }
                }
            } else {
                if (productLookUp.getText() == null || productLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(productLookUp.getText()) && requiredColumnCodes.contains(ItemTableConstants.PRODUCT)) {
                    itemTable.setColumnValid(ItemTableConstants.PRODUCT);
                    errors++;
                }
                if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                    TextArea2 descriptionTxtArea = (TextArea2) itemTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
                    if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                        itemTable.setColumnValid(ItemTableConstants.DESCRIPTION);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.MEASUREMENT)) {
                    SmartMeasurementsLookUp measurementLookUp = (SmartMeasurementsLookUp) itemTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
                    if (!Validation.validateLookUpRequired(measurementLookUp)) {
                        itemTable.setColumnValid(ItemTableConstants.MEASUREMENT);
                        errors++;
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.DEPARTMENT)) {
                    DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemTable.getColumnById(rowID, ItemTableConstants.DEPARTMENT);
                    if (!Validation.validateLookUpRequired(departmentLookUp)) {
                        itemTable.setColumnValid(ItemTableConstants.DEPARTMENT);
                        errors++;
                    }
                }

                if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
                    CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemTable.getColumnById(rowID, ItemTableConstants.QTY);
                    if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                        itemTable.setColumnValid(ItemTableConstants.QTY);
                        errors++;
                    } else {
                        if (utils.parseToBigDecimal(qtyTxtBox.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                            itemTable.setColumnValid(ItemTableConstants.QTY);
                            errors++;
                        }

                    }
                }

                if (Utils.isMultiWarehouseEnabled() && requiredColumnCodes.contains(ItemTableConstants.WAREHOUSE)) {
                    if (!Validation.validateLookUpRequired(warehouseLookUp)) {
                        itemTable.setColumnValid(ItemTableConstants.WAREHOUSE);
                        errors++;
                    }
                }

            }


            for (CompanyCustomFieldItem fieldItem : requiredCFs) {
                if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateTextBoxRequired(t)) {
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                    CustomPercentageField t = (CustomPercentageField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired()) {
                        if (!Validation.validateIntegerTextBoxRequired(t)) {
                            itemTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }/* else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                itemTable.setColumnValid(fieldItem.getColumnCode());
                                errors++;
                            }
                        }*/
                    } else {
                        if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                            itemTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        } else {
                            nonRequired++;
                        }
                    }
                } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired()) {
                        if (!Validation.validateEmailRequired(t)) {
                            itemTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        if (!fieldItem.isRequired() && t.getText().length() > 0) {
                            if (!Validation.validateEmailRequired(t)) {
                                itemTable.setColumnValid(fieldItem.getColumnCode());
                                errors++;
                            }
                        } else {
                            nonRequired++;
                        }
                    }
                } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired()) {
                        if (!Validation.validateUrl(t, null)) {
                            itemTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        if (!fieldItem.isRequired() && t.getText().length() > 0) {
                            if (!Validation.validateUrl(t, null)) {
                                itemTable.setColumnValid(fieldItem.getColumnCode());
                                errors++;
                            }
                        } else {
                            nonRequired++;
                        }
                    }
                } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                    DataListBox t = (DataListBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItem() == null) {
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                    TextArea t = (TextArea) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getText() == null) {
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                    DatePicker t = (DatePicker) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDate(t)) {
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                    DateTimeWidget t = (DateTimeWidget) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDateTime(t)) {
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldLookUpField t = (CustomFieldLookUpField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateLookUpRequired(t)) {
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                        t.addStyleName(ERROR_FORM_STYLE);
                        Utils.scrollIntoView(t.getElement());
                        itemTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                }
            }
        }

        error[0] = errors;
        error[1] = nonRequired;
        return error;
    }

    private boolean areOtherRowsAffected(int rowID) {
        boolean result = false;

        SmartProductLookUp productLookUp = (SmartProductLookUp) itemTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemTable.getColumnById(rowID, ItemTableConstants.QTY);
        WarehouseLookUp warehouseLookUp = (WarehouseLookUp) itemTable.getColumnById(rowID, ItemTableConstants.WAREHOUSE);

        TextArea2 descriptionTxtArea = (TextArea2) itemTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
        CustomCellTextBox qtyOnHand = (CustomCellTextBox) itemTable.getColumnById(rowID, ItemTableConstants.COMISSION);
        SmartMeasurementsLookUp measurementLookUp = (SmartMeasurementsLookUp) itemTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
        DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemTable.getColumnById(rowID, ItemTableConstants.DEPARTMENT);
        result |= descriptionTxtArea != null && (descriptionTxtArea.getText() != null && !"".equals(descriptionTxtArea.getText().trim()));
        result |= measurementLookUp != null && (measurementLookUp.getSelectedItem() != null && measurementLookUp.getSelectedItem().getId() != null);
        result |= departmentLookUp != null && (departmentLookUp.getSelectedItem() != null && departmentLookUp.getSelectedItem().getId() != null);
        result |= productLookUp != null && ((productLookUp.getSelectedItem() != null && productLookUp.getSelectedItem().getId() != null)
                || (productLookUp.getText() != null && !wfmStrings.searchTypeMessage().equals(productLookUp.getText())));
        result |= warehouseLookUp != null && (warehouseLookUp.getSelectedItem() != null && warehouseLookUp.getSelectedItem().getId() != null);
        result |= qtyTxtBox != null && (qtyTxtBox.getText() != null && !"".equals(qtyTxtBox.getText().trim()));
        result |= qtyOnHand != null && (qtyOnHand.getText() != null && !"".equals(qtyOnHand.getText().trim()));
        return result;
    }

    private void colorizeErrorField(int rowID, String status, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        SmartProductLookUp productLookUp = (SmartProductLookUp) itemTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemTable.getColumnById(rowID, ItemTableConstants.QTY);
        WarehouseLookUp warehouseLookUp = (WarehouseLookUp) itemTable.getColumnById(rowID, ItemTableConstants.WAREHOUSE);

        if (!DRAFT.equals(status)) {
            if (requiredColumnCodes.isEmpty()) {
                if (productLookUp.getSelectedItem() == null || (productLookUp.getText() == null || productLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(productLookUp.getText()))) {
                    itemTable.notValid(rowID, ItemTableConstants.PRODUCT);
                }
                if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                    itemTable.notValid(rowID, ItemTableConstants.QTY);
                } else {
                    if (utils.parseToBigDecimal(qtyTxtBox.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                        itemTable.notValid(rowID, ItemTableConstants.QTY);
                    }
                }

                if (warehouseIsEnabled) {
                    if (!Validation.validateLookUpRequired(warehouseLookUp)) {
                        itemTable.notValid(rowID, ItemTableConstants.WAREHOUSE);
                    }
                }
            } else {
                if (requiredColumnCodes.contains(ItemTableConstants.PRODUCT) && productLookUp.getSelectedItem() == null || (productLookUp.getText() == null || productLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(productLookUp.getText()))) {
                    itemTable.notValid(rowID, ItemTableConstants.PRODUCT);
                }
                if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
                    if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                        itemTable.notValid(rowID, ItemTableConstants.QTY);
                    } else {
                        if (utils.parseToBigDecimal(qtyTxtBox.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                            itemTable.notValid(rowID, ItemTableConstants.QTY);
                        }
                    }
                }

                if (warehouseIsEnabled && requiredColumnCodes.contains(ItemTableConstants.WAREHOUSE)) {
                    if (!Validation.validateLookUpRequired(warehouseLookUp)) {
                        itemTable.notValid(rowID, ItemTableConstants.WAREHOUSE);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                    TextArea2 descriptionTxtArea = (TextArea2) itemTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
                    if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                        itemTable.notValid(rowID, ItemTableConstants.DESCRIPTION);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.MEASUREMENT)) {
                    SmartMeasurementsLookUp measurementLookUp = (SmartMeasurementsLookUp) itemTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
                    if (!Validation.validateLookUpRequired(measurementLookUp)) {
                        itemTable.notValid(rowID, ItemTableConstants.MEASUREMENT);
                    }
                }
                if (requiredColumnCodes.contains(ItemTableConstants.DEPARTMENT)) {
                    DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemTable.getColumnById(rowID, ItemTableConstants.DEPARTMENT);
                    if (!Validation.validateLookUpRequired(departmentLookUp)) {
                        itemTable.notValid(rowID, ItemTableConstants.DEPARTMENT);
                    }
                }
            }
            for (CompanyCustomFieldItem fieldItem : requiredCFs) {
                if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateTextBoxRequired(t)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                }
                if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                    TextArea t = (TextArea) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateTextAreaRequired(t)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateEmailRequired(t)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                    TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateUrl(t, null)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                    CustomPercentageField t = (CustomPercentageField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (fieldItem.isRequired()) {
                        if (!Validation.validateIntegerTextBoxRequired(t)) {
                            itemTable.notValid(rowID, fieldItem.getColumnCode());
                        }/* else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                itemTable.notValid(rowID, fieldItem.getColumnCode());
                            }
                        }*/
                    } else {
                        if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                            itemTable.notValid(rowID, fieldItem.getColumnCode());

                        }
                    }
                } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                    DataListBox t = (DataListBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItem() == null) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                    DatePicker t = (DatePicker) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDate(t)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                    DateTimeWidget t = (DateTimeWidget) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateDateTime(t)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldLookUpField t = (CustomFieldLookUpField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (!Validation.validateLookUpRequired(t)) {
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                    CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
                    if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                        t.addStyleName(ERROR_FORM_STYLE);
                        Utils.scrollIntoView(t.getElement());
                        itemTable.notValid(rowID, fieldItem.getColumnCode());
                    }
                }
            }

        }

    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> result = new ArrayList<>();
        noteHistoryWidget = new NoteHistoryWidget(callback -> QuoteService.App.get().getRFPHistoryNotes(objectID, callback));
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        footerUploadPanel = new FooterUploadPanel(F_RFP, objectID, true, wfmStrings.attachments());
        informer.setInitialClasses("informer-item history-notes-container");

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

        result.add(informer);
        result.add(footerUploadPanel);
        if (objectID != null) {
            result.add(link);
        }
        return result;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(RequestForPurchaseView.this) {
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

    private void getRelationName(final Integer relationID, final String relType) {
        AllInOneService.App.get().getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    relationName = result;
                }
            }
        });
    }

}
