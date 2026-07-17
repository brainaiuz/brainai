package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.DiscountViewPopup;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddUnitMeasurementView;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
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
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.view.OpportunityPercentageStageModal;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTipListener;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.OpportunityDiscountWidget;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.StageHistoryGrid;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartMeasurementsLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldCurrencyWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomHTMLTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.TaxView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;

/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class AddOpportunityView extends CustomForm2 implements Constants, FormHasCustomFieldInterface, HasLinksInterface, Colapse {
    protected static final CRMServiceAsync crmService = CRMService.App.get();
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private static final Integer DEFAULT_ITEM_ROWS = 3;
    private Integer taxCalculationType = AccountingConstants.TAX_CALCULATION_EXCLUSIVE;

    private Integer fromContactID = null;
    private Integer campaignID = null;
    private Integer crmAccountID = null;
    private Integer crmContactID = null;
    private String crmAccountName = null;
    private String crmContactName = null;
    private String campaignName = null;
    protected Integer objectId;
    protected OpportunityListItem item;
    public LinkedHashMap<String, FormProperty> formPropertyMap;
    private EmployeeLookUp assignee;
    private EmployeeLookUp backupAssignee;
    public DataListBox taxCalcTypeListBox;
    private TextBox opportunityNumber;
    private Numbering numbering;
    private HTML labelCurrent;
    private CRMLookUp opportunityName;
    private TextBox amount;
    private CurrencyWidget currencyWidgets;
    private ProjectLookUp projectLookUp;
    public BigDecimal generalExchangeRate = BigDecimal.ONE;
    private Integer currencyID;
    protected DateTimePicker closingDate;
    private CRMLookUp accountName;
    private CRMLookUp contactName;
    private MaterialLink addNewAccountLink;
    private Div menubar;
    private Div menubarAccount;
    private CRMLookUp campaign;
    protected DataListBox stage;
    private TextBox probability;
    private DataListBox type;
    private TextBox expectedRevenue;
    private TextBox nextStep;
    protected CRMLookUp leadSource;
    protected HTMLPanel companyName;
    protected HTMLPanel email;
    protected HTMLPanel phone;
    protected HTMLPanel fax;
    private GeneralFileUpload fileUpload;
    private NoteWidget noteWidget;
    private HasLinks linkingUtil;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private final Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();
    private HashMap<Integer, Widget> taxWidgetMap;

    protected boolean saveAndClose = false;
    private final Integer opportunityConvertingTo = null;

    private NumberData numberData;
    CrmActivityGrid crmActivityGrid;
    StageHistoryGrid stageHistoryGrid;

    private boolean isCopying;
    private String formType = null;
    private Integer convertedFormId = null;
    private boolean fromCampaign;
    private EditableTable productTable;
    OpportunityReceiptTable opportunityReceiptTable;
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private boolean hasProduct;
    private Integer stageId;
    private FlowPanel contactPhone;
    protected MaterialLink customerBalanceLink;
    private WfmButton2 approveButton;
    private WfmButton2 submitButton;
    private WfmButton2 draftButton;
    private ChosenApproversWidget approvers;

    ExtendedHTML subTotal,
            discount,
            totalPrice,
            taxTotal,
            quantityTotal,
            baseTotal;

    HTML subTotalLabel,
            discountLabel,
            totalLabel,
            totalTaxLabel,
            baseTotalLabel;

    public AddOpportunityView(Integer objectId, boolean isCopying, Integer fromContactID) {
        super("add");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.opportunity()));
        this.objectId = objectId;
        this.isCopying = isCopying;
        if (!(objectId == null || isCopying)) {
            setDescription(property.getSingular(wfmStrings.edit(), wfmStrings.opportunity()));
        }
        this.fromContactID = fromContactID;
    }

    public AddOpportunityView(Integer objectId, Integer stageId) {
        this(objectId, false);
        this.stageId = stageId;
    }

    public AddOpportunityView(Integer objectId, boolean isCopying) {
        super("add");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.opportunity()));
        this.objectId = objectId;
        this.isCopying = isCopying;
        if (!(objectId == null || isCopying)) {
            setDescription(property.getSingular(wfmStrings.edit(), wfmStrings.opportunity()));
        }
    }

    public AddOpportunityView(boolean fromCampaign, Integer campaignID, String campaignName) {
        super("add");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.opportunity()));
        this.fromCampaign = fromCampaign;
        this.campaignID = campaignID;
        this.campaignName = campaignName;
    }

    public AddOpportunityView(Integer objectId, Integer crmAccountID, String crmAccountName, Integer crmContactID, String contactName) {
        super("add");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.opportunity()));
        this.objectId = objectId;
        this.crmAccountID = crmAccountID;
        this.crmAccountName = crmAccountName;
        this.crmContactID = crmContactID;
        this.crmContactName = contactName;
        if (objectId != null) {
            setDescription(property.getSingular(wfmStrings.edit(), wfmStrings.opportunity()));
        }
    }

    public AddOpportunityView(String viewopportunity) {
        super(viewopportunity);
    }

    public AddOpportunityView(String formType, Integer convertedFormId) {
        super("add");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.opportunity()));
        this.convertedFormId = convertedFormId;
        this.formType = formType;
    }

    @Override
    protected Widget onInitialize() {
        if (!(this instanceof ViewOpportunityForm)) {
            onAccountAdded();
            onContactAdded();
        }
        CommonService.App.get().getCompanyAllCustomFields(ViewName.OpportunitySubItem, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    for (CompanyCustomFieldItem item : result) {
                        itemCFs.put(item.getColumnCode(), item);
                    }
                }
            }
        });
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Opportunity, LayoutRPC.OPPORTUNITY_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(CompanyCfAndPropertyItems result) {
                super.onSuccess(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddOpportunityView.super.onInitialize();
            }
        });

        CommonService.App.get().getCompanyCustomFields(ViewName.OpportunityItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }
                drawItemTable();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        drawProductTable();
        drawForm();
    }

    private void drawForm() {

        contactPhone = new FlowPanel();
        assignee = new EmployeeLookUp(true, PermissionConstants.CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE);
        String nickDebugId = "add_opportunity_view";
        assignee.ensureDebugId(nickDebugId + "assignee");

        backupAssignee = new EmployeeLookUp(true, PermissionConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE);
        backupAssignee.ensureDebugId(nickDebugId + "backupAssignee");

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(nickDebugId + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.addValueChangeHandler(event -> onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), true));

        taxWidgetMap = new HashMap<>();

        opportunityNumber = new TextBox();
        opportunityNumber.ensureDebugId(nickDebugId + "opportunityNumber");

        numbering = new Numbering();
        numbering.ensureDebugId(nickDebugId + "numbering");

        approvers = new ChosenApproversWidget(RelationItem.TYPE_OPPORTUNITY, null);
        approvers.setWidth("200px");


        opportunityName = new CRMLookUp(LookUpConstants.CRM_OPPORTUNITY_ID);
        opportunityName.ensureDebugId(nickDebugId + "opportunityName");
        opportunityName.getFilterParametrs().setWithCode(false);

        labelCurrent = new HTML();
        labelCurrent.addStyleName("fieldKey");

        amount = new TextBox();
        amount.ensureDebugId(nickDebugId + "amount");
        amount.addChangeHandler(c -> {
            amount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(amount.getText())));
        });

        amount.setText("0");
        Validation.addNumericKeyboardListener(amount, AccountingUtils.customQtyScale);
        amount.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                calculate();
            }
        });

        currencyWidgets = new CurrencyWidget(objectId == null);
        currencyWidgets.addListener(() -> {
            if (hasProduct) {
                BigDecimal newAmount = AccountingUtils.get().parseToBigDecimal(amount.getValue());
                newAmount = newAmount != null && generalExchangeRate != null && BigDecimal.ZERO.compareTo(newAmount) != 0 ? newAmount.divide(generalExchangeRate, 2, RoundingMode.HALF_UP) : newAmount;
                amount.setText(numberFormat.format(newAmount.multiply(currencyWidgets.getExchangeRate())));
                generalExchangeRate = BigDecimal.ZERO.compareTo(currencyWidgets.getExchangeRate()) != 0 ? currencyWidgets.getExchangeRate() : BigDecimal.ONE;
            }
            labelCurrent.setHTML("(" + currencyWidgets.getCurrencyName() + ")");
            if (productTable != null) {
                for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                    Widget widget = productTable.getColumnById(i, "discount");
                    if (widget != null) {
                        OpportunityDiscountWidget discountWidget = (OpportunityDiscountWidget) widget;
                        if (!discountWidget.getDiscountUnit().equals(OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                            discountWidget.setDiscountUnit(currencyWidgets.getCurrencyName());
                        }
                    }
                    if (!currencyWidgets.getCurrencyID().equals(currencyID)) {
                        CustomCellTextBox price = (CustomCellTextBox) productTable.getColumnById(i, ItemTableConstants.UNITPRICE);
                        if (price != null && price.getText() != null && !price.getText().isEmpty()) {
                            price.setText(AccountingUtils.get().formatPrice(price.getBaseCurrencyValue().multiply(generalExchangeRate)));
                            productTable.refreshCustomCellDisplayValue(i, ItemTableConstants.UNITPRICE);
                        }
                    }
                }
                currencyID = currencyWidgets.getCurrencyID();
            }
            calculate();

        });
        currencyWidgets.addStyleName(DEFAULT_WIDTH);
        currencyWidgets.ensureDebugId("currency");

        closingDate = new DateTimePicker(false, true);
        closingDate.dueDate.ensureDebugId(nickDebugId + "closingDate");
        DatePicker date = new DatePicker();
        date.addStyleName("file--AddOpportunityView");
        date.setDate(closingDate.dueDate != null && closingDate.dueDate.getDate() != null ? closingDate.dueDate.getDate() : new Date());
        currencyWidgets.setDatePicker(date);

        projectLookUp = new ProjectLookUp(null);
        projectLookUp.ensureDebugId("opportunity_relatedProject");

        customerBalanceLink = new MaterialLink("");
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");

        accountName = new CRMLookUp(CrmConstants.CRM_ACCOUNT_ID);
        accountName.setEnsureDebugId("Click_Button_icon_1");
        accountName.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            contactName.clear();
            contactName.refreshOracle(true);
            contactName.clearOracleItems();
            setCrmAccountBalanceAdd(accountName, customerBalanceLink);
        });
        accountName.ensureDebugId(nickDebugId + "accountName");
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_QUICK_ADD) || Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_ADD)) {
            accountName.setLinkCommand(() -> {
                if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_QUICK_ADD)) {
                    new CrmQuickAdd(LayoutRPC.ACCOUNT_FORM);
                } else if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_ADD)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add" + "/fromOpportunity");
                }
            });
        }

        contactName = new CRMLookUp(CrmConstants.CRM_CONTACT_ID, Constants.SUPPLIER);
        contactName.setEnsureDebugId("Click_Button_icon_2");
        contactName.setBeforeSearch(() -> {
            contactName.getFilterParametrs().setAccountID(accountName.getSelectedItemID());
        });
        contactName.getSuggestBox().addSelectionHandler(selectionEvent -> {
            onContactSelected(contactName.getSelectedItemID());
        });
        contactName.ensureDebugId(nickDebugId + "contactName");

        stage = new DataListBox();
        stage.addValueChangeHandler(sender -> {
            if (stage.getSelectedItem() != null && stage.getSelectedItem().isDraggable()) {
                if (Utils.isDoubleMessageEnable() && !(objectId == null || isCopying)) {
                    WfmMessageBox changeStageMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    changeStageMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(stage.getSelectedItem().getName()));
                    changeStageMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            if (stage.getSelectedItem() != null) {
                                if ("0".equals(stage.getSelectedItem().getDescription()) || stage.getSelectedItem().isSelected()) {
                                    Integer previousStageId = item.getStageId();
                                    item.setStageId(stage.getSelectedId());
                                    new OpportunityPercentageStageModal(item, stage.getSelectedItem().isSelected(), "0".equals(stage.getSelectedItem().getDescription()), true);
                                    WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, AddOpportunityView.this, (sender, args) -> {
                                        if ((boolean) args) {
                                            stageHistoryGrid.refresher();
                                        } else {
                                            stage.setSelected(previousStageId);
                                            item.setStageId(previousStageId);
                                        }
                                    });
                                }
                                probability.setText(stage.getSelectedItem().getDescription());
                                calculateExpectedRevenue();
                            } else {
                                probability.setText("");
                            }
                        }

                        @Override
                        public void onCancel() {
                            if (item.getStageId() != null) {
                                stage.setSelected(item.getStageId());
                            }
                        }
                    });

                    changeStageMessageBox.setTitle(wfmStrings.warning());
                    changeStageMessageBox.open();
                } else {
                    if (stage.getSelectedItem() != null) {
                        if (!(objectId == null || isCopying) && ("0".equals(stage.getSelectedItem().getDescription()) || stage.getSelectedItem().isSelected())) {
                            Integer previousStageId = item.getStageId();
                            item.setStageId(stage.getSelectedId());
                            new OpportunityPercentageStageModal(item, stage.getSelectedItem().isSelected(), "0".equals(stage.getSelectedItem().getDescription()), true);
                            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, AddOpportunityView.this, (sender2, args) -> {
                                if ((boolean) args) {
                                    stageHistoryGrid.refresher();
                                } else {
                                    stage.setSelected(previousStageId);
                                    item.setStageId(previousStageId);
                                }
                            });
                        }
                        probability.setText(stage.getSelectedItem().getDescription());
                        calculateExpectedRevenue();
                    } else {
                        probability.setText("");
                    }
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                stage.setSelected(new SelectItem());
            }
        });
        stage.ensureDebugId(nickDebugId + "stage");

        probability = new TextBox();
        probability.setEnabled(false);
        probability.ensureDebugId(nickDebugId + "probability");

        type = new DataListBox();
        type.ensureDebugId("opportunity-type");

        nextStep = new TextBox();
        nextStep.ensureDebugId(nickDebugId + "nextStep");

        expectedRevenue = new TextBox();

        expectedRevenue.addChangeHandler(c -> {
            expectedRevenue.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(expectedRevenue.getText())));
        });

        expectedRevenue.setEnabled(false);
        expectedRevenue.ensureDebugId(nickDebugId + "expectedRevenue");

        leadSource = new CRMLookUp(CRMLookUp.LEAD_RESOURCE);
        leadSource.ensureDebugId(nickDebugId + "leadsource");
        leadSource.getElement().setId("lead-source");
        leadSource.addStyleName("width250");

        campaign = new CRMLookUp(CrmConstants.CRM_CAMPAIGN_ID);
        campaign.setEnsureDebugId("Click_Button_icon_3");
        campaign.ensureDebugId(nickDebugId + "campaign");

        menubar = new Div("btn-group dropdown-split");
        MaterialLink addNewContactLink = new MaterialLink();
        addNewContactLink.setStyleName("dropdown-button");

        MaterialDropDown menuContainer = new MaterialDropDown(addNewContactLink);
        menuContainer.setClass("dropdown-content");
        menuContainer.setBelowOrigin(true);
        addNewContactLink.addBlurHandler(bh -> {
            menubar.removeStyleName("dropdown-split--open");
            menubar.addStyleName("dropdown-split");
        });
        addNewContactLink.addClickHandler(ch -> {
            if (menubar.getStyleName().contains("dropdown-split--open")) {
                menubar.removeStyleName("dropdown-split--open");
                menubar.addStyleName("dropdown-split");
            } else {
                menubar.removeStyleName("dropdown-split");
                menubar.addStyleName("dropdown-split--open");
            }
        });
        Icon moreIcon = new Icon();
        moreIcon.setClass("ficon--more-horiz");
        addNewContactLink.add(moreIcon);
        Div div = new Div("btn-group dropdown-split__toggle");
        div.add(addNewContactLink);
        div.add(menuContainer);
        menubar.add(div);

        MaterialLink addContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
        addContact.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CONTACT)) {
                if (accountName.getSelectedItemID() != null) {
                    goTo("contact|add/add/" + accountName.getSelectedItem().getId() + "/fromOpportunity/");
                } else {
                    goTo("contact|add/add//fromOpportunity");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainer.add(addContact);

        MaterialLink editContact = new MaterialLink(Property.get(Constants.Contacts, wfmStrings.editContact(), wfmStrings.contact()));
        editContact.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CONTACT)) {
                if (contactName.getSelectedItemID() != null) {
                    goTo("contact|add/add//fromOpportunity/" + contactName.getSelectedItem().getId(), contactName.getSelectedItem().getName());
                } else if (accountName.getSelectedItemID() != null) {
                    goTo("contact|add/add/" + accountName.getSelectedItem().getId() + "/fromOpportunity");
                } else {
                    goTo("contact|add/add//fromOpportunity");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainer.add(editContact);

        menubarAccount = new Div("btn-group dropdown-split");
        addNewAccountLink = new MaterialLink();
        addNewAccountLink.setStyleName("dropdown-button");
        MaterialDropDown menuContainerAccount = new MaterialDropDown(addNewAccountLink);
        menuContainerAccount.setClass("dropdown-content");
        menuContainerAccount.setBelowOrigin(true);
        addNewAccountLink.addBlurHandler(bh -> {
            menubarAccount.removeStyleName("dropdown-split--open");
            menubarAccount.addStyleName("dropdown-split");
        });
        addNewAccountLink.addClickHandler(ch -> {
            if (menubarAccount.getStyleName().contains("dropdown-split--open")) {
                menubarAccount.removeStyleName("dropdown-split--open");
                menubarAccount.addStyleName("dropdown-split");
            } else {
                menubarAccount.removeStyleName("dropdown-split");
                menubarAccount.addStyleName("dropdown-split--open");
            }
        });
        Icon moreIconAccount = new Icon();
        moreIconAccount.setClass("ficon--more-horiz");
        addNewAccountLink.add(moreIconAccount);
        Div divAccount = new Div("btn-group dropdown-split__toggle");
        divAccount.add(addNewAccountLink);
        divAccount.add(menuContainerAccount);
        menubarAccount.add(divAccount);

        MaterialLink addAccount = new MaterialLink(Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()));
        addAccount.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_ADD)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add" + "/fromOpportunity");

            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainerAccount.add(addAccount);

        MaterialLink editAccount = new MaterialLink(Property.get(Constants.CLIENT_LIST, wfmStrings.editClient(), wfmStrings.customer()));
        editAccount.addClickHandler(event -> {
            if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_EDIT)) {
                if (accountName.getSelectedItemID() != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add" + "/fromOpportunity/" + accountName.getSelectedItem().getId(), accountName.getSelectedItem().getName());
                } else {
                    goTo("account|add/add" + "/fromOpportunity");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        menuContainerAccount.add(editAccount);


        noteWidget = new NoteWidget(isCopying ? null : objectId, CrmConstants.CRM_OPPORTUNITY);
        noteWidget.ensureDebugId("opportunity-additional_information");

        fileUpload = new GeneralFileUpload(F_OPPORTUNITY, isCopying ? null : objectId, isCopying ? null : objectId);
        fileUpload.ensureDebugId(nickDebugId + "fileUpload");


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE) != null) {
            addField(CRM_OPPORTUNITY_ASSIGNEE, assignee, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).getTitle() : wfmStrings.assignee(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isInformation()) {
                new KpiToolTip(assignee, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).getInformationText());
            }

            assignee.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE, assignee, getTitle(wfmStrings.assignee()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE) != null) {
            addField(CRM_OPPORTUNITY_BACKUP_ASSIGNEE, backupAssignee, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).getTitle() : wfmStrings.backupAssignee(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isInformation()) {
                new KpiToolTip(backupAssignee, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).getInformationText());
            }

            backupAssignee.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE, backupAssignee, getTitle(wfmStrings.backupAssignee()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE) != null) {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getTitle() : wfmStrings.amount(), formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isRequired()));
            taxCalcTypeListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(wfmStrings.amount()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER) != null) {
            addField(CRM_OPPORTUNITY_NUMBER, numbering, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isInformation()) {
                new KpiToolTip(numbering, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).getInformationText());
            }

            numbering.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_NUMBER, numbering, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME) != null) {
            addField(CRM_OPPORTUNITY_NAME, opportunityName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isInformation()) {
                new KpiToolTip(opportunityName, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getInformationText());
            }

            opportunityName.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_NAME, opportunityName, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME) != null) {
            FormGroup clientField = new FormGroup(new AdvancedInputGroup(null, accountName, menubarAccount, true, true));
            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");

            Span spanElement = new Span(getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).getTitle() : wfmStrings.customer()));
            Span requiredSpan = new Span(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isRequired() ? "*" : "");
            requiredSpan.addStyleName("redTitle");
            spanElement.add(requiredSpan);
            spanElement.setId("customer-label");
            clientFieldLabel.add(spanElement);

            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);

            addField(CRM_OPPORTUNITY_ACCOUNT_NAME, clientField, getFieldLabel(null), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isRequired(),
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isInformation()) {
                new KpiToolTip(clientField, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).getInformationText());
            }

            accountName.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isDisabled());
        } else {
            FormGroup clientField = new FormGroup(new AdvancedInputGroup(null, accountName, menubarAccount, true, true));
            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");

            Span spanElement = new Span(getTitle(wfmStrings.customer()));
            Span requiredSpan = new Span("*");
            requiredSpan.addStyleName("redTitle");
            spanElement.add(requiredSpan);
            spanElement.setId("customer-label");
            clientFieldLabel.add(spanElement);

            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            clientFieldLabel.add(balance);

            addField(CRM_OPPORTUNITY_ACCOUNT_NAME, clientField, getFieldLabel(null));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME) != null) {
            addField(CRM_OPPORTUNITY_CONTACT_NAME, new AdvancedInputGroup(null, contactName, menubar, true, true), getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).getTitle() : Property.get(Constants.Contacts, wfmStrings.contact()), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isInformation()) {
                new KpiToolTip(contactName, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).getInformationText());
            }

            contactName.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME, new AdvancedInputGroup(null, contactName, menubar, true, true), getTitle(Property.get(Constants.Contacts, wfmStrings.contact())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE) != null) {
            addField(CRM_OPPORTUNITY_LEAD_SOURCE, leadSource, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).getTitle() : wfmStrings.source(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isInformation()) {
                new KpiToolTip(leadSource, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).getInformationText());
            }

            leadSource.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE, leadSource, getTitle(wfmStrings.source()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE) != null) {
            addField(CRM_OPPORTUNITY_CAMPAIGN_SOURCE, campaign, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).getTitle() : wfmStrings.campaign(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isInformation()) {
                new KpiToolTip(campaign, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).getInformationText());
            }

            campaign.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE, campaign, getTitle(wfmStrings.campaign()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE) != null) {
            addField(CRM_OPPORTUNITY_TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isInformation()) {
                new KpiToolTip(type, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).getInformationText());
            }

            type.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_TYPE, type, getTitle(wfmStrings.type()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP) != null) {
            addField(CRM_OPPORTUNITY_NEXT_STEP, nextStep, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getTitle() : wfmStrings.nextStep(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isInformation()) {
                new KpiToolTip(nextStep, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getInformationText());
            }

            nextStep.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP, nextStep, getTitle(wfmStrings.nextStep()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT) != null) {
            addField(CRM_OPPORTUNITY_AMOUNT, new AdvancedInputGroup(null, amount, labelCurrent, true, true), getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getTitle() : wfmStrings.amount(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isInformation()) {
                new KpiToolTip(amount, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getInformationText());
            }

            amount.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT, new AdvancedInputGroup(null, amount, labelCurrent, true, true), getTitle(wfmStrings.amount()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null) {
            addField(CURRENCY, currencyWidgets, getTitle(formPropertyMap.get(CustomFormConstants.CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.CURRENCY).getTitle() : wfmStrings.currency(), formPropertyMap.get(CustomFormConstants.CURRENCY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CURRENCY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CURRENCY).isInformation()) {
                new KpiToolTip(currencyWidgets, formPropertyMap.get(CustomFormConstants.CURRENCY).getInformationText());
            }
            currencyWidgets.setEnabled(!formPropertyMap.get(CustomFormConstants.CURRENCY).isDisabled());
        } else {
            addField(CustomFormConstants.CURRENCY, currencyWidgets, getTitle(wfmStrings.currency()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE) != null) {
            addField(CRM_OPPORTUNITY_CLOSING_DATE, closingDate.dueDate, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getTitle() : wfmStrings.closeDate(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isInformation()) {
                new KpiToolTip(closingDate.dueDate, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getInformationText());
            }
            closingDate.dueDate.setTextBoxEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE, closingDate.dueDate, getTitle(wfmStrings.closeDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE) != null) {
            addField(CRM_OPPORTUNITY_STAGE, stage, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getTitle() : wfmStrings.stage(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isInformation());
            new KpiToolTip(stage, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getInformationText());

            stage.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_STAGE, stage, getTitle(wfmStrings.stage(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY) != null) {
            addField(CRM_OPPORTUNITY_PROBABILITY, probability, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getTitle() : wfmStrings.probability(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isInformation()) {
                new KpiToolTip(probability, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getInformationText());
            }

            probability.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY, probability, getTitle(wfmStrings.probability()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE) != null) {
            addField(CRM_OPPORTUNITY_EXPECTED_REVENUE, expectedRevenue, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getTitle() : wfmStrings.expectedRevenue(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isInformation()) {
                new KpiToolTip(expectedRevenue, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getInformationText());
            }

            expectedRevenue.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE, expectedRevenue, getTitle(wfmStrings.expectedRevenue()));
        }

        if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null) {
                addField(PROJECT_FIELD, projectLookUp, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isRequired()));
                projectLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isDisabled());
            } else {
                addField(CustomFormConstants.PROJECT_FIELD, projectLookUp, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CRM_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired(), false,
                    formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()), true);
            if (formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()) {
                new KpiToolTip(noteWidget, formPropertyMap.get(CustomFormConstants.CRM_NOTE).getInformationText());
            }

            if (noteWidget.getTextBox() != null) {
                noteWidget.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_NOTE).isDisabled());
            }
        } else {
            addField(CustomFormConstants.CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        if (objectId != null || crmContactID != null) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE) != null) {
                addField(CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE, contactPhone, getTitle(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE).getTitle() : wfmStrings.phone()));
            } else {
                addField(CRM_OPPORTUNITY_CONTACT_PRIMARY_PHONE, contactPhone, getTitle(wfmStrings.phone()));
            }
        }

        addField(CustomFormConstants.CRM_OPPORTUNITY_ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);
        addTitleField(CustomFormConstants.CRM_OPPORTUNITY_LINKS, wfmStrings.links());
//        addField(CRM_OPPORTUNITY_LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), wfmStrings.links(), true);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        addTitleField(CustomFormConstants.OPPORTUNITY_INFORMATION, property.getSingular(wfmStrings.basicDetails(), wfmStrings.opportunity()));
//        addField(CustomFormConstants.GET_PRODUCT, includeItems, getTitle(wfmStrings.getProduct()));
        if (!isCopying) {
            setEditValues();
        }
        addField(APPROVERS, approvers, getTitle(wfmStrings.approvers()));
        initButtonsPanel();
        getCustomFieldUtil().drawCustomFields(this, objectId);
        show();
    }

    @Override
    protected void addButtons() {
        FooterInformer link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITY_LINKS)) {
            footer.addToLeftSide(link);
        }

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                if (objectId == null) {
                    ArrayList<RelationItem> relationItems = new ArrayList<>();
                    getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);
                } else {
                    getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                }
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

//        MaterialLink save = new MaterialLink(wfmStrings.save());
//        MaterialSplitButton splitButton = new MaterialSplitButton(save);
//        save.addClickHandler(event -> {
//            saveAndClose = true;
//            save(null);
//        });
//        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
//        saveAdd.addClickHandler(event -> {
//            saveAndClose = false;
//            save(null);
//        });
//        splitButton.addItem(saveAdd);
//        addButton(splitButton);

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        if (fromContactID != null) {
            crmContactID = fromContactID;
        }
        crmService.editOpportunity(objectId, formType, convertedFormId, crmContactID, new AbstractAsyncCallback<OpportunityListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final OpportunityListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;

                    if (isCopying) {
                        item.setObjectId(null);
                        objectId = null;
//                        item.getRelations().clear();
                        generateNewNumber();

                    }
                    fillFormWithData();
                });
            }
        });
    }

    protected void fillFormWithData() {
        initPredefinedValues();
        if (!isCopying) {
            Utils.registrRelation(item);
        }
        numberData = item.getNumberData();
        assignee.setSelected(item.getAssigneeId() != null ? new SelectItem(item.getAssigneeId(), item.getAssignee()) : new SelectItem(Utils.getUserID(), Utils.getUserFullName()));

        if (item.getBackupAssigneeID() != null) {
            backupAssignee.setSelected(new SelectItem(item.getBackupAssigneeID(), item.getBackupAssignee()));
        }

        numbering.setNumberData(numberData);
        opportunityNumber.setText(numberData.getNumberString());
        opportunityName.getSuggestBox().setText(item.getOpportunityName());
        taxCalcTypeListBox.setSelected(item.getTaxCalculationType() != null ? AccountingUtils.getTaxCalcType(item.getTaxCalculationType()) : AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
        onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), false);

        if (item.getAmount() != null) {
            amount.setText(numberFormat.format(item.getAmount().doubleValue()));
        }
//        currencyBox.setItems(item.getCurrencies());
//        if (objectId == null || item.getCurrency() == null) {
//            currencyBox.setSelected(item.getBaseCurrencyID());
//            labelCurrent.setHTML("(" + item.getBaseCurrencyName() + ")");
//        } else {
//            currencyBox.setSelected(item.getCurrencyId());
//            labelCurrent.setHTML(item.getCurrency() != null ? "(" + item.getCurrency() + ")" : "");
//        }
//        exchangeRate.setText(AccountingUtils.get().formatExRate(item.getExchangeRate() != null ? item.getExchangeRate().doubleValue() : BigDecimal.ONE.doubleValue()));
//        baseCurrencyName.setText(accountingMessages.dynamicCurrencyView(item.getBaseCurrencyName()));

        currencyWidgets.setCurrency(item.getCurrencyId(), item.getExchangeRate());
        currencyID = item.getCurrencyId();
        generalExchangeRate = item.getExchangeRate();
        if (item.getClosingDate() != null) {
            closingDate.setDueDate(item.getClosingDate());
        }
        if (item.getAccountId() != null) {
            accountName.setSelected(item.getAccountId(), item.getAccount());
        }
        if (crmAccountID != null && item.getAccountId() == null) {
            accountName.setSelected(crmAccountID, crmAccountName);
        }
        setCrmAccountBalanceEdit(item, customerBalanceLink);
        type.setItems(Utils.sortSelectItemByName(item.getTypes()));
        if (item.getTypeId() != null) {
            type.setSelected(item.getTypeId());
        }

        if (crmContactID != null || item.getContactId() != null) {
            contactName.setSelected(crmContactID != null ? crmContactID : item.getContactId(), item.getContact());

            if (item.getContactItem() != null) {
                setMultiTableItems(Constants.CONTACT_PHONES);
            }
        } else if (item.getContactItem() != null && TYPE_ACCOUNT == item.getContactItem().getContactType()) {
            setMultiTableItems(Constants.CONTACT_PHONES);
        }
        stage.setItems(item.getStages());
        if (item.getStageId() != null) {
            stage.setSelected(item.getStageId());
        }
        if (item.getProbability() != null) {
            probability.setText(item.getProbability().toString());
        }
        nextStep.setText(item.getNextStep());
        if (item.getExpectedRevenue() != null) {
            expectedRevenue.setText(numberFormat.format(item.getExpectedRevenue().doubleValue()));
        }
        if (item.getLeadSourceId() != null) {
            leadSource.setSelected(item.getLeadSourceId(), item.getLeadSource());
        }
        if (item.getCampaignId() != null) {
            campaign.setSelected(item.getCampaignId(), item.getCampaign());
        } else if (fromCampaign) {
            campaign.setSelected(campaignID, campaignName);
        }
        if (projectLookUp != null && item.getProject() != null) {
            projectLookUp.setSelected(item.getProject());
        }

        // Reload approvers widget when editing an existing opportunity
        if (objectId != null && !isCopying) {
            approvers.reloadApproverWidgets(RelationItem.TYPE_OPPORTUNITY, objectId);
        }

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());

        setValues(item.getItems());
//        getLinkingUtil().getTaggingView().setFromName(item.getRelationName());
//        getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
//        getLinkingUtil().drawLinks();

        setItemTableValues(item.getCustomTableItems());

        if (objectId == null) {
            setDefaultValues();
            setDefaultValuesByFormProperty();
            if (stageId != null) {
                stage.setSelected(stageId);
            }
        }
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(CRM_OPPORTUNITY_ASSIGNEE, item.getAssignees());
        addPredefinedValues(CRM_OPPORTUNITY_BACKUP_ASSIGNEE, item.getAssignees());
        addPredefinedValues(CRM_OPPORTUNITY_TYPE, item.getTypes());
        addPredefinedValues(CRM_OPPORTUNITY_STAGE, item.getStages());
        addPredefinedValues(CRM_OPPORTUNITY_LEAD_SOURCE, item.getLeadSources());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private void generateNewNumber() {
        crmService.generateOpportunityNumber(new AbstractAsyncCallback<NumberData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(NumberData result) {
                if (result != null) {
                    numberData = result;
                    item.setNumberData(result);
                    numbering.setNumberData(numberData);
                    opportunityNumber.setText(numberData.getNumberString());
                }
            }
        });
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.CRM_OPPORTUNITIES_ADD;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.OPPORTUNITY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    public String getIconStyle() {
        return "oport opportunities-list";
    }


    private void drawProductTable() {
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.OPPORTUNITY_SUB_ITEM, new AbstractAsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {
                if (result != null) {
                    for (ColumnConfigs cc : result) {
                        if (cc.isSelected()) {
                            columnsMap.put(cc.getCode(), cc);
                        }
                    }
                }

                productTable = new EditableTable(getColumns(), true, true);
                productTable.setDraggable(true);
                productTable.ensureDebugId("Opportunity_product_table");
                productTable.setWidth("100%");
                productTable.setListener(new EditableTableListener() {
                    @Override
                    public void addRow() {
                        productTable.addRow(getWidgets(new OpportunityItem(), false));
                    }

                    @Override
                    public void removeRow() {
                        calculate();
                    }
                });

                FlowPanel fp = new FlowPanel();
                fp.add(productTable);

                opportunityReceiptTable = new OpportunityReceiptTable();
                opportunityReceiptTable.addStyleName("totalsTable java-AddOpportunityView");
                fp.add(opportunityReceiptTable);

                initTotalTableWidgets();

                addField(CustomFormConstants.CRM_OPPORTUNITY_INCLUDE_ITEMS, fp, Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()), true);
            }
        });
    }

    protected void initTotalTableWidgets() {
        subTotalLabel = new HTML(wfmStrings.subtotal());
        discountLabel = new HTML(wfmStrings.discount());
        totalLabel = new HTML(wfmStrings.total());
        totalTaxLabel = new HTML(wfmStrings.taxTotal());
        baseTotalLabel = new HTML(wfmStrings.total());

        subTotalLabel.ensureDebugId(formType + "subTotalLabel");
        discountLabel.ensureDebugId(formType + "discountLabel");
        totalLabel.ensureDebugId(formType + "totalLabel");
        baseTotalLabel.ensureDebugId(formType + "baseTotalLabel");
        totalTaxLabel.ensureDebugId(formType + "totalTaxLabel");

        subTotal = getZeroAsHTML();
        discount = getZeroAsHTML();
        totalPrice = getZeroAsHTML();
        taxTotal = getZeroAsHTML();
        baseTotal = getZeroAsHTML();
        quantityTotal = getZeroAsHTML();

        subTotal.ensureDebugId(formType + "subTotal");
        discount.ensureDebugId(formType + "discountAmount");
        totalPrice.ensureDebugId(formType + "totalPrice");
        taxTotal.ensureDebugId(formType + "taxTotal");
        baseTotal.ensureDebugId(formType + "baseTotal");

        subTotalLabel.setStyleName("totallabel");
        discountLabel.setStyleName("totallabel");
        totalLabel.setStyleName("totallabel");
        totalTaxLabel.setStyleName("totallabel");
        baseTotalLabel.setStyleName("totallabel");

        subTotal.setStyleName("totalvalue");
        discount.setStyleName("totalvalue");
        totalPrice.setStyleName("totalvalue");
        taxTotal.setStyleName("totalvalue");
        baseTotal.setStyleName("totalvalue");
    }

    private void checkForProductExist() {
        hasProduct = false;
        for (int rowID = 0; rowID < productTable.getGrid().getRowCount(); rowID++) {
            if (columnsMap.containsKey(ItemTableConstants.PRODUCT)) {
                ProductLookUp product = (ProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
                if (product.getSelectedItemID() != null || !(Utils.isNullOrEmpty(product.getText()) || LookUp.wfmStrings.searchTypeMessage().equals(product.getText().trim()))) {
                    hasProduct = true;
                    amount.setEnabled(false);
                    break;
                }
            }
        }
        if (!hasProduct) {
            amount.setEnabled(true);
            amount.setValue(numberFormat.format(BigDecimal.ZERO));
        }
    }

    private ColumnConfig[] getColumns() {
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (String cc : columnsMap.keySet()) {

            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;

            switch (cc) {
                case ItemTableConstants.PRODUCT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.product(), Utils.getColumnWidth(columnConfigs.getWidth(), 200), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.description(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), "product-description-cell");
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.QTY:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.MEASUREMENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.MEASUREMENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.measurement(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.DISCOUNT_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DISCOUNT_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.discount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.UNITPRICE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.price(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.TAX_LIST:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TAX_LIST, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.taxRate(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.NET_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.NET_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.netAmount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.TOTAL_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TOTAL_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.totalAmount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.CLIENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.CLIENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.CATEGORY:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.CATEGORY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.category(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.BRAND:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.BRAND, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.brand(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;
                    break;
                case ItemTableConstants.PROJECT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PROJECT, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columns[i++] = columnConfig;
                    break;
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnConfigs.getCode(), columnConfigs.getTitle(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), isPixel);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;

                    if (UI_TYPE_TEXTAREA.equalsIgnoreCase(columnConfigs.getUiType())) {
                        columnConfig.setCustomStyleName("product-description-cell");
                    }
                    break;
            }
        }
        return columns;
    }

    private Widget[] getWidgets(OpportunityItem item, boolean updateExchangeRate) {
        int index = 0;
        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                final SmartProductLookUp product = new SmartProductLookUp(RECEIVED/*, true*/);
                product.setValueNotEmptyMeansSelected(true);
                product.setWidth("100%");
                product.addStyleName("lookUp-moveRight");
                product.getSuggestBox().setWidth("100%");
                product.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getItemID() != null) {
                    if (!(Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_LOOKUP_DESCRIPTION_INCLUDED) || Utils.isNullOrEmpty(item.getItemNumber()))) {
                        product.setSelected(new ProductSelectItem(item.getItemID(), item.getItemNumber() + " -> " + item.getItemName()));
                    } else {
                        product.setSelected(new ProductSelectItem(item.getItemID(), item.getItemName()));
                    }
                } else {
                    if (item.getItemName() != null && !"".equalsIgnoreCase(item.getItemName())) {
                        product.getSuggestBox().setText(item.getItemName());
                        product.getSuggestBox().getElement().setAttribute("style", "color:#536677 !important");
                    }
                }
                if (item.getProductCategory() != null) {
                    product.setCategoryId(item.getProductCategory().getId());
                }
                if (item.getProductBrand() != null) {
                    product.setBrandId(item.getProductBrand().getId());
                }
                if (item.getId() != null) {
                    product.setItemID(item.getId());
                }
                product.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> setItemValues(product));
                product.getSuggestBox().getTextBox().addKeyUpHandler(keyUpEvent -> checkForProductExist());
                product.getSuggestBox().getTextBox().addKeyDownHandler(keyDownEvent -> checkForProductExist());
                product.setTitle(columnCode);
                widgets[index++] = product;
            } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                TextArea2 txtDescription = new TextArea2(TextArea2.AREA_LENGTH_3);
                txtDescription.setSize("100%", "40px");
                txtDescription.setText(item.getDescription());
                txtDescription.setTitle(columnCode);
                txtDescription.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                Validation.addAutoResizeListenerToTextArea(txtDescription.getTextArea());
                txtDescription.hideCharacterLimitPanel();
                widgets[index++] = txtDescription;
            } else if (ItemTableConstants.QTY.equals(columnCode)) {
                CustomCellTextBox quantity = new CustomCellTextBox();
                quantity.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                quantity.setWidth("100%");
                Validation.addNumericKeyboardListener(quantity, 2);
                if (item.getQty() != null) {
                    quantity.setText(AccountingUtils.get().formatQty(item.getQty()));
                } else {
                    quantity.setText(AccountingUtils.get().formatQty(BigDecimal.ZERO));
                }

                String qtyText = item.getQtyOnHand() != null ? AccountingUtils.get().formatQty(item.getQtyOnHand()) : AccountingUtils.get().formatQty(new BigDecimal(0));
                WfmToolTipListener toolTipListener = new WfmToolTipListener(qtyText, 300000, "easyTooltip2");
                quantity.addMouseUpHandler(toolTipListener);
                quantity.addMouseDownHandler(toolTipListener);
                quantity.addFocusHandler(toolTipListener);

                quantity.addChangeHandler(changeEvent -> calculate());
                quantity.setTitle(columnCode);
                widgets[index++] = quantity;
            } else if (ItemTableConstants.MEASUREMENT.equals(columnCode)) {
                final SmartMeasurementsLookUp measurementsLookUp = new SmartMeasurementsLookUp();
                measurementsLookUp.addStyleName("lookUp-moveRight");
                measurementsLookUp.getSuggestBox().setWidth("100%");
                measurementsLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                measurementsLookUp.setLinkCommand(() -> {
                    ObjectCommand command = item1 -> measurementsLookUp.addMeasurementUnit((SelectItem) item1);
                    new AddUnitMeasurementView(null, command);
                });
                measurementsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> measurementsLookUp.islink());
                if (item.getUnitMeasurement() != null) {
                    measurementsLookUp.setSelected(item.getUnitMeasurement());
                }
                measurementsLookUp.setTitle(columnCode);
                widgets[index++] = measurementsLookUp;
            } else if (ItemTableConstants.UNITPRICE.equals(columnCode)) {
                final CustomCellTextBox txtPrice = new CustomCellTextBox();
                txtPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                txtPrice.setWidth("100%");
                txtPrice.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                Validation.addNumericKeyboardListener(txtPrice, 2);
                if (item.getPrice() != null) {
                    txtPrice.setText(AccountingUtils.get().formatPrice(item.getPrice()));
                    txtPrice.setBaseCurrencyValue(item.getPrice().divide(updateExchangeRate ? currencyWidgets.getExchangeRate() : this.item.getExchangeRate() != null ? this.item.getExchangeRate() : currencyWidgets.getExchangeRate(), 8, RoundingMode.HALF_UP));
                }
                txtPrice.addChangeHandler(changeEvent -> {
                    txtPrice.setLayoutData(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue()))/*.divide(AccountingUtils.get().parseToBigDecimal(exchangeRate.getText()), 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_HALF_UP))*/;
                    txtPrice.setBaseCurrencyValue(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue()).divide(currencyWidgets.getExchangeRate(), 8, RoundingMode.HALF_UP));
                    calculate();
                });
                txtPrice.setTitle(columnCode);
                widgets[index++] = txtPrice;
            } else if (ItemTableConstants.DISCOUNT_LIST.equals(columnCode)) {
                final WfmDropdown dwDiscountList = new WfmDropdown(true, accountingStrings.addNewDiscount());
                dwDiscountList.setWidth("100%");
                dwDiscountList.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getDiscountItems() != null && item.getDiscountItems().length > 0) {
                    dwDiscountList.addItems(item.getDiscountItems());
                } else {
                    dwDiscountList.addItem(new SelectItem(Constants.ONE_OFF_DISCOUNT, wfmStrings.percentage()));
                    dwDiscountList.addItem(new SelectItem(Constants.ONE_OFF_FIXED_AMOUNT, wfmStrings.fixedAmount()));
                    dwDiscountList.setData(new DiscountItem(Constants.ONE_OFF_DISCOUNT, wfmStrings.percentage()));
                    dwDiscountList.setData(new DiscountItem(Constants.ONE_OFF_FIXED_AMOUNT, wfmStrings.fixedAmount()));
                }
                if (item.getDiscountItemID() != null) {
                    dwDiscountList.setSelected(item.getDiscountItemID());
                } else if (ONE_OFF_DISCOUNT.equals(item.getDiscountItemFixedType())) {
                    dwDiscountList.setSelected(Constants.ONE_OFF_DISCOUNT);
                } else if (ONE_OFF_FIXED_AMOUNT.equals(item.getDiscountItemFixedType())) {
                    dwDiscountList.setSelected(Constants.ONE_OFF_FIXED_AMOUNT);
                } else {
                    dwDiscountList.setSelected(Constants.ONE_OFF_DISCOUNT);
                }
                dwDiscountList.addEventHandler(new DropdownListener() {
                    @Override
                    public void itemSelected() {
                        onChangeDiscount(widgets);
                        calculate();
                    }

                    @Override
                    public void saveNewItem() {
                        ExtendedCommand provider = new ExtendedCommand() {
                            public void execute(final Integer id) {
                                DiscountService.App.get().getDiscountData(id, new AbstractAsyncCallback<DiscountItem>() {
                                    @Override
                                    public void success(DiscountItem discountItem) {
                                        dwDiscountList.addItem(discountItem);
                                        dwDiscountList.setData(discountItem);
                                        dwDiscountList.setSelected(id);
                                        onChangeDiscount(widgets);
                                        calculate();
                                    }
                                });
                            }
                        };
                        if (columnsMap.containsKey(ItemTableConstants.PRODUCT)) {
                            for (Widget w : widgets) {
                                if (ItemTableConstants.PRODUCT.equals(w.getTitle()) && w instanceof ProductLookUp) {
                                    Integer productID = ((ProductLookUp) w).getSelectedItemID();
                                    new DiscountViewPopup(provider, productID);
                                    break;
                                }
                            }
                        }
                    }
                });
                dwDiscountList.setTitle(columnCode);
                widgets[index++] = dwDiscountList;
            } else if (ItemTableConstants.DISCOUNT_AMT.equals(columnCode)) {
                OpportunityDiscountWidget discount = new OpportunityDiscountWidget();
                discount.setWidth("100%");
                discount.getTxtDiscount().setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                discount.getTxtDiscount().setWidth("60%");
                if (item.getDiscountPercent() != null) {
                    discount.setValueText(AccountingUtils.get().formatDiscount(item.getDiscountPercent()), item.getDiscountPercent());
                    discount.setDiscountUnit(OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT);
                } else if (item.getDiscountAmount().compareTo(BigDecimal.ZERO) != 0) {
                    discount.setValueText(AccountingUtils.get().formatDiscount(item.getDiscountAmount()), item.getDiscountAmount());
                    if (currencyWidgets != null && currencyWidgets.getCurrencyName() != null) {
                        discount.setDiscountUnit(currencyWidgets.getCurrencyName());
                    } else {
                        discount.setDiscountUnit("");
                    }
                }
                discount.addChangeHandler(changeEvent -> calculate());
                discount.setTitle(columnCode);
                widgets[index++] = discount;
            } else if (ItemTableConstants.CLIENT.equals(columnCode)) {
                CrmAccountLookUp supplierLookUp = new CrmAccountLookUp(CrmAccountItem.SUPPLIER, true);
                supplierLookUp.addStyleName("lookUp-moveRight");
                supplierLookUp.getSuggestBox().setWidth("100%");
                supplierLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getSupplierID() != null) {
                    supplierLookUp.addItem(new SelectItem(item.getSupplierID(), item.getSupplierName()));
                    supplierLookUp.setSelected(item.getSupplierID());
                } else if (item.getSupplierName() != null) {
                    supplierLookUp.getTextBox().setText(item.getSupplierName());
                    supplierLookUp.getTextBox().getElement().getStyle().setColor("#536677");
                }
                supplierLookUp.setTitle(columnCode);
                widgets[index++] = supplierLookUp;
            } else if (ItemTableConstants.CATEGORY.equals(columnCode)) {
                DataListBox categoryBox = new DataListBox();
                categoryBox.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                categoryBox.addValueChangeHandler(valueChangeEvent -> {
                    if (valueChangeEvent.getValue().getId() != null) {
                        onChangeProductCategory(valueChangeEvent, item);
                    }
                    LookUpCell productCell = (LookUpCell) productTable.getColumnCellWidgetById(productTable.getGrid().getCurrentRow(), ItemTableConstants.PRODUCT);
                    ProductLookUp productLookUp = (ProductLookUp) productCell.getLookUp();
                    productCell.clear();
                    productLookUp.clearAndClearItems();
                    productLookUp.setCategoryId(valueChangeEvent.getValue().getId());

                });
                if (this.item != null) {
                    categoryBox.setItems(this.item.getProductCategories());
                }

                if (item.getProductCategory() != null) {
                    categoryBox.setSelected(item.getProductCategory());
                }
                widgets[index++] = categoryBox;
            } else if (ItemTableConstants.BRAND.equals(columnCode)) {
                DataListBox brandBox = new DataListBox();
                brandBox.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                brandBox.addValueChangeHandler(valueChangeEvent -> {
                    LookUpCell productCell = (LookUpCell) productTable.getColumnCellWidgetById(productTable.getGrid().getCurrentRow(), ItemTableConstants.PRODUCT);
                    ProductLookUp productLookUp = (ProductLookUp) productCell.getLookUp();
                    productCell.clear();
                    productLookUp.clearAndClearItems();
                    productLookUp.setBrandId(valueChangeEvent.getValue().getId());
                });
                if (this.item != null) {
                    brandBox.setItems(this.item.getProductBrands());
                }

                if (item.getProductBrand() != null) {
                    brandBox.setSelected(item.getProductBrand());
                }
                widgets[index++] = brandBox;
            } else if (ItemTableConstants.PROJECT.equals(columnCode)) {

                ProjectLookUp projectLookUp = new ProjectLookUp(null);
                projectLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());

                if (item != null && item.getProject() != null) {
                    projectLookUp.addItem(item.getProject());
                }
                widgets[index++] = projectLookUp;
            } else if (ItemTableConstants.TAX_LIST.equals(columnCode)) {
                SmartTaxRateLookUp taxLookUp = new SmartTaxRateLookUp(PAYABLE);
                if (taxLookUp != null) {
                    taxLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                    if (taxCalcTypeListBox.getSelectedId() != null && AccountingConstants.NO_TAX_CALCULATION.equals(taxCalcTypeListBox.getSelectedId())) {
                        taxLookUp.clear();
                        taxLookUp.setEnabled(false);
                    } else if (item.getTaxItem() != null) {
                        taxLookUp.addTaxItem(item.getTaxItem());
                        reDrawTaxesDropdown();
                    }
                    taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> reDrawTaxesDropdown());
                    taxLookUp.getSuggestBox().addKeyUpHandler(keyUpEvent -> reDrawTaxesDropdown());
                }
                widgets[index++] = taxLookUp;
            } else if (ItemTableConstants.NET_AMT.equals(columnCode)) {
                ExtendedHTML netAmount = getZeroAsHTML();
                widgets[index++] = netAmount;
            } else if (ItemTableConstants.TOTAL_AMT.equals(columnCode)) {
                ExtendedHTML totalAmount = getZeroAsHTML();
                widgets[index++] = totalAmount;
            } else if (itemCFs.containsKey(columnCode)) {

                CompanyCustomFieldItem cfItem = itemCFs.get(columnCode);
                CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(item.getItemCustomFields(), cfItem);

                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomTextBoxField(companyCustomFieldItem);
                } else if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextAreaField customTextAreaField = new CustomTextAreaField(companyCustomFieldItem);
                    customTextAreaField.hideCharacterLimitPanel();
                    Validation.addAutoResizeListenerToTextArea(customTextAreaField.getTextArea());
                    widgets[index] = customTextAreaField;
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomPercentageField(companyCustomFieldItem);
                } else if (Constants.UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomDropDownField(companyCustomFieldItem);
                } else if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomDatePicker(companyCustomFieldItem);
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomDateTime(companyCustomFieldItem);
                } else if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomFieldLookUpField(companyCustomFieldItem);
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets[index] = new CustomFieldMultiLookUpField(companyCustomFieldItem);
                }

                if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                    CompanyCustomFieldItem fitem = companyCustomFieldItem;
                    if (fitem != null) {
                        ((CustomFieldInterface) widgets[index]).setFieldItem(fitem);
                    }
                }
                index++;
            }
        }
        return widgets;
    }

    private CompanyCustomFieldItem setCustomFieldValue(ArrayList<CompanyCustomFieldItem> itemCustomFields, CompanyCustomFieldItem cfItem) {
        if (itemCustomFields != null && itemCustomFields.size() > 0) {
            for (CompanyCustomFieldItem customFieldItem : itemCustomFields) {
                if (customFieldItem.getDataType().equals(cfItem.getDataType()) && customFieldItem.getUiType().equals(cfItem.getUiType()) && customFieldItem.getAliasName().equals(cfItem.getAliasName())) {
                    return customFieldItem;
                }
            }
        }
        return cfItem;
    }

    private void onChangeProductCategory(ValueChangeEvent<SelectItem> categoryItem, OpportunityItem item) {
        item.setProductCategory(categoryItem.getValue());
        ProductService.App.get().getProductCategoryCF(categoryItem.getValue().getId(), new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {

                item.getItemCustomFields().clear();
                if (result != null && result.size() > 0) {
                    item.setItemCustomFields(result);
                    if (productTable.getGrid().getCurrentRow() < productTable.getGrid().getRowCount()) {
                        productTable.addRow(productTable.getGrid().getCurrentRow(), getWidgets(item, false));
                    } else {
                        productTable.addRow(getWidgets(item, false));
                    }
                }
            }
        });
    }

    private void reDrawTaxesDropdown() {
        taxWidgetMap.clear();
        for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
            if (productTable.getColumnById(i, ItemTableConstants.TAX_LIST) != null) {
                SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) productTable.getColumnById(i, ItemTableConstants.TAX_LIST);
                if (taxLookUp.getSelectedItemID() != null && !taxWidgetMap.containsKey(taxLookUp.getSelectedItemID())) {
                    TaxView tax = new TaxView(AccountingUtils.getZero());
                    tax.setItem(taxLookUp.getData(taxLookUp.getSelectedItemID()));
                    taxWidgetMap.put(taxLookUp.getSelectedItemID(), tax);
                }
            }
        }
        calculate();
    }

    public ExtendedHTML getZeroAsHTML() {
        ExtendedHTML zeroValue = new ExtendedHTML(AccountingUtils.getZero());
        zeroValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        return zeroValue;
    }

    private void setItemValues(SmartProductLookUp productLookUp) {
        ProductSelectItem item = productLookUp.getSelectedItem();
        if (item != null && item.getId() != null) {
            hasProduct = true;
            amount.setEnabled(false);
            ProductService.App.get().getProductBaseData(item.getId(), true, new LoadingPanelCallback<NewProduct>(productTable, wfmStrings.pleaseWait()) {
                public void success(NewProduct product) {
                    if (product != null) {
                        OpportunityItem opportunityItem = new OpportunityItem();
                        opportunityItem.setItemName(product.getItemName());
                        opportunityItem.setItemNumber(product.getNumberData() != null ? product.getNumberData().getNumberString() : "");
                        opportunityItem.setItemID(product.getObjectId());
                        opportunityItem.setDescription(product.getDescription());
                        opportunityItem.setQtyOnHand(product.getQuantity() != null ? product.getQuantity() : new BigDecimal("0"));
                        opportunityItem.setUnitMeasurement(product.getUnitMeasurement());
                        opportunityItem.setPrice(product.getSellingPrice().multiply(currencyWidgets.getExchangeRate()));
                        opportunityItem.setDiscountItems(product.getDiscountItems());
                        opportunityItem.setProductCategory(new SelectItem(product.getCategoryID(), product.getCategoryName()));
                        opportunityItem.setProductBrand(new SelectItem(product.getBrandID(), product.getBrandName()));
                        opportunityItem.setItemCustomFields(product.getProductCustomFieldItems());
                        opportunityItem.setTaxItem(product.getTaxItem());
                        opportunityItem.setQty(BigDecimal.ONE);
                        if (product.getSuppliers() != null && product.getSuppliers().length > 0) {
                            opportunityItem.setSupplierID(product.getSuppliers()[0].getId());
                            opportunityItem.setSupplierName(product.getSuppliers()[0].getName());
                        }
                        if (product.getProductCustomFieldItems() != null && !product.getProductCustomFieldItems().isEmpty()) {
                            setValueStaticFieldFromCFByAliasName(opportunityItem, product.getProductCustomFieldItems());
                        }

                        if (productTable.getGrid().getCurrentRow() < productTable.getGrid().getRowCount()) {
                            productTable.addRow(productTable.getGrid().getCurrentRow(), getWidgets(opportunityItem, true));
                        } else {
                            productTable.addRow(getWidgets(opportunityItem, true));
                        }
                    }

                    calculate();
                }
            });
        } else {
            checkForProductExist();
        }
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddOpportunityView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                });
                if (approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            } else {
                approveButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });
    }

    private void initButtonsPanel() {
        if (item == null) {
            item = new OpportunityListItem();
        }
        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(event -> {
            save(Constants.OPPORTUNITY_DRAFT);
            saveAndClose=true;
        });
        addRightButton(draftButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> {save(Constants.OPPORTUNITY_APPROVED);saveAndClose=true;});
        approveButton.setVisible(false);
        addRightButton(approveButton);


        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent ->{ save(Constants.OPPORTUNITY_SUBMITTED);saveAndClose=true;});
        submitButton.setVisible(false);
        addRightButton(submitButton);

        approveButton.setVisible(true);
        if (Constants.OPPORTUNITY_SUBMITTED.equals(item.getStatusCode()) ||
                Constants.OPPORTUNITY_APPROVED.equals(item.getStatusCode())) {
            draftButton.setVisible(false);
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddOpportunityView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    if (itemId != null && Utils.getUserID().equals(itemId)) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        approveButton.setVisible(false);
                    }
                });
                if (approveButton != null && submitButton != null && approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });
    }

    private void setValueStaticFieldFromCFByAliasName(OpportunityItem opportunityItem, ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        for (CompanyCustomFieldItem productCFItem : productCustomFieldItems) {
            if (productCFItem != null && productCFItem.getAliasName() != null) {
                switch (productCFItem.getAliasName()) {
                    case ItemTableConstants.DESCRIPTION:
                        if ((Utils.isNullOrEmpty(opportunityItem.getDescription())) &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType()))) {
                            opportunityItem.setDescription(productCFItem.getFieldStringValue());
                        }
                        break;

                    case ItemTableConstants.QTY:
                        if (opportunityItem.getQty() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setQty(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.MEASUREMENT:
                        if (opportunityItem.getUnitMeasurement() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setUnitMeasurement(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.UNITPRICE:
                        if (opportunityItem.getPrice() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setPrice(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.DISCOUNT_AMT:
                        if (opportunityItem.getDiscountAmount() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setDiscountAmount(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.SUPPLIER:
                        if (opportunityItem.getSupplierID() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setSupplierID(productCFItem.getSelectedId());
                            opportunityItem.setSupplierName(productCFItem.getFieldStringValue());
                        }
                        break;

                    case ItemTableConstants.CATEGORY:
                        if (opportunityItem.getProductCategory() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT_CATEGORY.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProductCategory(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.PROJECT:
                        if (Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE) && opportunityItem.getProject() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProject(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                        break;
                }
            }
        }
    }

    private void onChangeDiscount(Widget[] widgets) {
        if (columnsMap.containsKey(ItemTableConstants.DISCOUNT_AMT)) {
            WfmDropdown dwDiscountList = null;
            OpportunityDiscountWidget discount = null;
            for (Widget widget : widgets) {
                if (ItemTableConstants.DISCOUNT_LIST.equals(widget.getTitle())) {
                    dwDiscountList = (WfmDropdown) widget;
                } else if (ItemTableConstants.DISCOUNT_AMT.equals(widget.getTitle())) {
                    discount = (OpportunityDiscountWidget) widget;
                }
            }
            DiscountItem discountItem = dwDiscountList != null ? (DiscountItem) dwDiscountList.getSelectedData() : null;
            if (discountItem == null) {
                discount.setValueText(ZERO.toString(), ZERO);
                discount.setDiscountUnit(OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT);
                return;
            }
            if ((discountItem.getPercentage() != null) || (discountItem.getId() != null && discountItem.getId().equals(Constants.ONE_OFF_DISCOUNT))) {
                discount.setDiscountUnit(OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT);
                if (discountItem.getPercentage() != null) {
                    discount.setValueText(AccountingUtils.get().formatDiscount(discountItem.getPercentage()), discountItem.getPercentage());
                }
                return;
            }
            if ((discountItem.getFixedAmount() != null) || (discountItem.getId() != null && discountItem.getId().equals(Constants.SIMPLE_DISCOUNT))) {
                if (discountItem.getPercentage() != null) {
                    discount.setValueText(AccountingUtils.get().formatDiscount(discountItem.getPercentage()), discountItem.getPercentage());
                    discount.setDiscountUnit(OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT);
                } else {
                    if (currencyWidgets != null && currencyWidgets.getCurrencyName() != null) {
                        discount.setDiscountUnit(currencyWidgets.getCurrencyName());
                    } else {
                        discount.setDiscountUnit("");
                    }
                    if (discountItem.getFixedAmount() != null) {
                        discount.setValueText(AccountingUtils.get().formatPrice(discountItem.getFixedAmount()), discountItem.getFixedAmount());
                        if (currencyWidgets.getCurrencyName() != null) {
                            discount.setDiscountUnit(currencyWidgets.getCurrencyName());
                        } else {
                            discount.setDiscountUnit("");
                        }
                    }
                }
            }
        }
    }

    private void setValues(OpportunityItem[] items) {
        if (items != null && items.length > 0) {
            hasProduct = true;
            amount.setEnabled(false);
            productTable.removeAllRows();
            for (OpportunityItem item : items) {
                productTable.addRow(getWidgets(item, false));
            }
            if (items != null && items.length < 3) {
                for (int i = 0; i < DEFAULT_ITEM_ROWS - items.length; i++) {
                    productTable.addRow(getWidgets(new OpportunityItem(), false));
                }
            }
        } else {
            for (int i = 0; i < DEFAULT_ITEM_ROWS; i++) {
                productTable.addRow(getWidgets(new OpportunityItem(), false));
                productTable.getGrid().setCurrentRow(0);
            }
        }
    }

    public OpportunityItem[] getOpportunityItemsData() {
        ArrayList<OpportunityItem> opportunityItems = new ArrayList<>();
        for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
            if (productTable.isItemValid(i)) {
                SmartProductLookUp productLookUp = null;
                OpportunityItem result = new OpportunityItem();
                Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();

                productLookUp = (SmartProductLookUp) productTable.getColumnById(i, ItemTableConstants.PRODUCT);
                if (productLookUp.getSelectedItemID() != null && Utils.normalize(productLookUp.getSelectedItem().getName()).equals(Utils.normalize(productLookUp.getSuggestBox().getText()))) {
                    result.setItemID(productLookUp.getSelectedItemID());
                    if (productLookUp.getSelectedItem().getName().contains("->")) {
                        result.setItemNumber(productLookUp.getSelectedItem().getName().split("->")[0].trim());
                        result.setItemName(productLookUp.getSelectedItem().getName().split("->")[1]);
                    } else {
                        result.setItemName(productLookUp.getSelectedItem().getName());
                    }
                } else if (productLookUp.getSuggestBox() != null && productLookUp.getSuggestBox().getText() != null && !productLookUp.getSuggestBox().getText().isEmpty()) {
                    result.setItemName(productLookUp.getSuggestBox().getText());
                } else {
                    continue;
                }
                if (productLookUp.getItemID() != null) {
                    itemCFsValues.putAll(setHideValues(result, productLookUp.getItemID(), i));
                }

                TextArea2 description = (TextArea2) productTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                if (description != null) {
                    result.setDescription(description.getText());
                }

                CustomCellTextBox quantity = (CustomCellTextBox) productTable.getColumnById(i, ItemTableConstants.QTY);
                if (quantity != null) {
                    result.setQty(quantity.getValue() != null ? AccountingUtils.get().parseToBigDecimal(quantity.getValue()) : ZERO);
                }

                MeasurementsLookUp measurement = (MeasurementsLookUp) productTable.getColumnById(i, ItemTableConstants.MEASUREMENT);
                if (measurement != null) {
                    result.setUnitMeasurement(measurement.getSelectedItem());
                }

                CustomCellTextBox price = (CustomCellTextBox) productTable.getColumnById(i, ItemTableConstants.UNITPRICE);
                if (price != null) {
                    result.setPrice(price.getValue() != null ? AccountingUtils.parsePriceToBigDecimal(price.getValue()) : ZERO);
                }

                SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) productTable.getColumnById(i, ItemTableConstants.TAX_LIST);
                if (taxLookUp != null) {
                    result.setTaxItem(taxLookUp.getSelectedData());
                    result.setTaxAmount(taxLookUp.getItemTaxAmount());
                }
                ExtendedHTML netamount = (ExtendedHTML) productTable.getColumnById(i, ItemTableConstants.NET_AMT);
                if (netamount != null) {
                    result.setNet(netamount.getValue());
                }

                ExtendedHTML linetotal = (ExtendedHTML) productTable.getColumnById(i, ItemTableConstants.TOTAL_AMT);
                if (linetotal != null) {
                    result.setSubTotal(linetotal.getValue());
                }

                WfmDropdown discountList = (WfmDropdown) productTable.getColumnById(i, ItemTableConstants.DISCOUNT_LIST);
                if (discountList != null && discountList.getSelectedItem() != null) {
                    if (ONE_OFF_FIXED_AMOUNT.equals(discountList.getSelectedItem().getId()) && ONE_OFF_FIXED_AMOUNT_STR.equals(discountList.getSelectedItem().getName())) {
                        result.setDiscountItemFixedType(ONE_OFF_FIXED_AMOUNT);
                    } else if (ONE_OFF_DISCOUNT.equals(discountList.getSelectedItem().getId()) && ONE_OFF_DISCOUNT_STR.equals(discountList.getSelectedItem().getName())) {
                        result.setDiscountItemFixedType(ONE_OFF_DISCOUNT);
                    } else {
                        result.setDiscountItemID(discountList.getSelectedId());
                    }
                }

                OpportunityDiscountWidget discountWidget = (OpportunityDiscountWidget) productTable.getColumnById(i, ItemTableConstants.DISCOUNT_AMT);
                if (discountWidget != null) {
                    BigDecimal discount = discountWidget.getValue();
                    if (discountWidget.getDiscountUnit().equals(OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT)) {
                        result.setDiscountPercent(discount);
                        result.setDiscountAmount(null);
                    } else {
                        result.setDiscountAmount(discount);
                        result.setDiscountPercent(null);
                    }
                }

                CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) productTable.getColumnById(i, ItemTableConstants.CLIENT);
                if (supplierLookUp != null && supplierLookUp.getSelectedItem() != null && supplierLookUp.getSuggestBox().getText().equals(supplierLookUp.getSelectedItem().getName())) {
                    result.setSupplierID(supplierLookUp.getSelectedItem().getId());
                    result.setSupplierName(supplierLookUp.getSelectedItem().getName());
                } else {
                    result.setSupplierName(null);
                }

                DataListBox categoryBox = (DataListBox) productTable.getColumnById(i, ItemTableConstants.CATEGORY);
                if (categoryBox != null) {
                    result.setProductCategory(categoryBox.getSelectedItem());
                }

                DataListBox brandBox = (DataListBox) productTable.getColumnById(i, ItemTableConstants.BRAND);
                if (brandBox != null) {
                    result.setProductBrand(brandBox.getSelectedItem());
                }

                ProjectLookUp projectLookUp = (ProjectLookUp) productTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (projectLookUp != null) {
                    result.setProject(projectLookUp.getSelectedItem());
                }

                //initialize custom fields data
                if (itemCFs != null && !itemCFs.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                    for (String key : itemCFs.keySet()) {
                        CustomFieldInterface customField = (CustomFieldInterface) productTable.getColumnById(i, key);

                        if (customField != null) {
                            final CompanyCustomFieldItem companyCustomFieldItem = customField.getFieldItem();
                            final CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                            resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                            resultItem.setDataType(companyCustomFieldItem.getDataType());
                            resultItem.setUiType(companyCustomFieldItem.getUiType());
                            resultItem.setColumnCode(key);
                            resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                            resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                            resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                            resultItem.setUiType(companyCustomFieldItem.getUiType());
                            resultItem.setLookUpTypeEnum(companyCustomFieldItem.getLookUpTypeEnum());
                            resultItem.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            resultItem.setSelectedId(companyCustomFieldItem.getSelectedId());
                            resultItem.setReferenceItem(customField.getFieldItem().getReferenceItem());
                            resultItem.setFieldDateNonConvertedValue(customField.getFieldItem().getFieldDateNonConvertedValue());

                            fieldItems.add(resultItem);
                        } else if (itemCFsValues.size() > 0 && itemCFsValues.get(key) != null && itemCFsValues.get(key).getUiType() != null) {
                            fieldItems.add(itemCFsValues.get(key));
                        }
                    }
                    if (!fieldItems.isEmpty()) {
                        result.setItemCustomFields(fieldItems);
                    }
                }
                if (result.getItemID() != null || !result.getItemName().equals(wfmStrings.searchTypeMessage())) {
                    opportunityItems.add(result);
                }
            }
        }
        return opportunityItems.toArray(new OpportunityItem[]{});
    }

    private Map<String, CompanyCustomFieldItem> setHideValues(OpportunityItem result, Integer lineItemId, Integer rowId) {
        Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();
        if (item.getItems() != null) {
            item.getItems();
            for (OpportunityItem opportunityItem : item.getItems()) {
                if (lineItemId.equals(opportunityItem.getId())) {
                    if (opportunityItem.getDescription() != null) {
                        result.setDescription(opportunityItem.getDescription());
                    }
                    if (opportunityItem.getQty() != null) {
                        result.setQty(opportunityItem.getQty());
                    }
                    if (opportunityItem.getUnitMeasurement() != null) {
                        result.setUnitMeasurement(opportunityItem.getUnitMeasurement());
                    }
                    if (opportunityItem.getPrice() != null) {
                        result.setPrice(opportunityItem.getPrice());
                    }
                    if (opportunityItem.getDiscountItems() != null) {
                        result.setDiscountItems(opportunityItem.getDiscountItems());
                    }
                    if (opportunityItem.getDiscountItemID() != null) {
                        result.setDiscountItemID(opportunityItem.getDiscountItemID());
                    }

                    if (opportunityItem.getDiscountPercent() != null) {
                        result.setDiscountPercent(opportunityItem.getDiscountPercent());
                    }
                    if (opportunityItem.getDiscountAmount() != null) {
                        result.setDiscountAmount(opportunityItem.getDiscountAmount());
                    }

                    if (opportunityItem.getDiscountItemFixedType() != null) {
                        result.setDiscountItemFixedType(opportunityItem.getDiscountItemFixedType());
                    }

                    if (opportunityItem.getSupplierID() != null) {
                        result.setSupplierID(opportunityItem.getSupplierID());
                    }
                    if (opportunityItem.getSupplierName() != null) {
                        result.setSupplierName(opportunityItem.getSupplierName());
                    }
                    if (opportunityItem.getProductCategory() != null) {
                        result.setProductCategory(opportunityItem.getProductCategory());
                    }
                    if (opportunityItem.getProductBrand() != null) {
                        result.setProductBrand(opportunityItem.getProductBrand());
                    }
                    if (opportunityItem.getTaxItem() != null) {
                        result.setTaxItem(opportunityItem.getTaxItem());
                    }
                    if (opportunityItem.getNet() != null) {
                        result.setNet(opportunityItem.getNet());
                    }
                    if (opportunityItem.getSubTotal() != null) {
                        result.setSubTotal(opportunityItem.getSubTotal());
                    }
                    if (opportunityItem.getProject() != null) {
                        result.setProject(opportunityItem.getProject());
                    }

                    if (itemCFs != null && opportunityItem.getCustomFieldValuesAsMap() != null) {
                        for (String columnCode : itemCFs.keySet()) {
                            if (opportunityItem.getCustomFieldValuesAsMap().get(columnCode) != null && opportunityItem.getCustomFieldValuesAsMap().get(columnCode).getUiType() != null) {
                                itemCFsValues.put(columnCode, opportunityItem.getCustomFieldValuesAsMap().get(columnCode));
                            }
                        }
                    }
                    break;
                }
            }
        }
        return itemCFsValues;
    }

    private String successMessage = property.getSingular(wfmStrings.messSuccessfullyAdded(), wfmStrings.opportunity());

    private void setEditValues() {
        successMessage = property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.opportunity());
    }

    private void enableButtons(boolean enable) {
        if (widgets.size() > 0) {
            enableButton(enable);
        }
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = customValidate();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER) != null) {
            TextBox opportunityNumber = new TextBox();
            opportunityNumber.setText(numbering.getTxtNumber().getText() + numbering.getTxtPrefix().getText());
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isRequired()) {
                errors += markAsError(numbering.getTxtNumber(), !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).getTitle() : wfmStrings.number(), opportunityNumber, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NUMBER).getMinChar()));
            }
        } else {
            errors += markAsError(numbering.getTxtNumber(), !numbering.validate());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE) != null) {
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).isRequired()) {
                if (!Validation.validateDate(closingDate.dueDate, new HTML(""), true)) {
                    errors++;
                }
            }
        } else {
            if (!Validation.validateDate(closingDate.dueDate, new HTML(""), true)) {
                errors++;
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isRequired()) {
                errors += markAsError(opportunityName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getTitle() : wfmStrings.name(), opportunityName.getTextBox(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getMinChar()));
            }
        } else {
            errors += markAsError(opportunityName, Utils.isNullOrEmpty(opportunityName.getText()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).isRequired()) {
                errors += markAsError(contactName, !Validation.validateLookUpRequired(contactName));
            }
        } else {
            errors += markAsError(contactName, !Validation.validateLookUpRequired(contactName));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).isRequired()) {
                errors += markAsError(accountName, !Validation.validateLookUpRequired(accountName));
            }
        } else {
            errors += markAsError(accountName, !Validation.validateLookUpRequired(accountName));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE) != null) {
            if (formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).isRequired()) {
                errors += markAsError(stage, !Validation.validateDataListBoxRequired(stage));
            }
        } else {
            errors += markAsError(stage, stage.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AMOUNT) != null) {
            if (!Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.AMOUNT).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.AMOUNT).getTitle() : wfmStrings.amount(), amount, formPropertyMap.get(CustomFormConstants.AMOUNT).getMinChar())) {
                try {
                    numberFormat.parse(amount.getText());
                } catch (NumberFormatException ex) {
                    errors++;
                    markAsError(amount, true);
                }
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (!validateItemTable()) {
            errors++;
            Utils.openParentSection(productTable);
        }
        if (!validateLookUPCustomItemTables()) {
            errors++;
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY, probability, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getTitle() : wfmStrings.probability(), probability, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE, assignee, assignee.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE, backupAssignee, backupAssignee.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT_FIELD, projectLookUp, projectLookUp.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE, leadSource, leadSource.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE, campaign, campaign.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_TYPE, type, type.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null && formPropertyMap.get(CustomFormConstants.CURRENCY).isRequired()) {
            errors += markAsError(CustomFormConstants.CURRENCY, currencyWidgets, currencyWidgets.getCurrencyID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP, nextStep, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getTitle() : wfmStrings.nextStep(), nextStep, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT, amount, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getTitle() : wfmStrings.amount(), amount, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE, expectedRevenue, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getTitle() : wfmStrings.expectedRevenue(), expectedRevenue, formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired()) {
            if (noteWidget != null && !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), noteWidget.getTextBox().getTextArea(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).getMinChar())) {
                errors++;
            }
        }


        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean areOtherRowsAffected(int rowID) {
        boolean result = false;

        SmartProductLookUp productLookUp = (SmartProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.QTY);
        TextArea2 descriptionTxtArea = (TextArea2) productTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
        MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) productTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
        CustomCellTextBox price = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);

        result |= descriptionTxtArea != null && (descriptionTxtArea.getText() != null && !"".equals(descriptionTxtArea.getText().trim()));
        result |= measurementLookUp != null && (measurementLookUp.getSelectedItem() != null && measurementLookUp.getSelectedItem().getId() != null);
        result |= price != null && (price.getText() != null && !"".equals(price.getText().trim()));
        result |= productLookUp != null && (productLookUp.getSelectedItem() != null && productLookUp.getSelectedItem().getId() != null);
        result |= qtyTxtBox != null && (qtyTxtBox.getText() != null && !"".equals(qtyTxtBox.getText().trim()));
        return result;
    }

    private boolean validateItemTable() {
        int errors = 0;
        productTable.setValidRows(0);
//        SelectItem firstSupplierItem = null;
        ArrayList<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            if (itemCFs.containsKey(columnCode) && (itemCFs.get(columnCode).isRequired() || UI_TYPE_TEXTBOX_EMAIL.equals(itemCFs.containsKey(columnCode)) || UI_TYPE_URL.equals(itemCFs.containsKey(columnCode)) || UI_TYPE_PERCENTAGE.equals(itemCFs.containsKey(columnCode)))) {
                requiredAndEmailCFs.add(itemCFs.get(columnCode));
            }
        }
        boolean errorFound = false;

        ArrayList<String> requiredColumnCodes = new ArrayList<>();
        int requiredRow = 0;

        if (columnsMap != null && columnsMap.values().size() > 0) {
            for (ColumnConfigs columnConfigs : columnsMap.values()) {
                if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                    requiredRow++;
                    requiredColumnCodes.add(columnConfigs.getCode());
                }
            }
        }


        for (int rowID = 0; rowID < productTable.getGrid().getRowCount(); rowID++) {
            int rowError;

            productTable.resetValidation(rowID);
            rowError = validateRequiredItems(rowID, requiredAndEmailCFs, requiredColumnCodes)[0];
            if (rowError == 0) {
                productTable.setItemValid(rowID, true);
                productTable.incValidRow();
            } else if (rowError == requiredRow + requiredAndEmailCFs.size() - validateRequiredItems(rowID, requiredAndEmailCFs, requiredColumnCodes)[1]) {

                if (!areOtherRowsAffected(rowID)) {
                    productTable.setItemValid(rowID, false); // exclude
                } else {
                    colorizeErrorField(rowID, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            } else {
                colorizeErrorField(rowID, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }
        }
        if (productTable.getValidRows() == 0) {
            colorizeErrorField(0, requiredAndEmailCFs, requiredColumnCodes);
            errorFound = true;
        }

        if (itemCFs != null && itemCFs.values().size() > 0 && !Validation.itemTableNumericCFMinValueValidate(productTable, itemCFs.values())) {
            errorFound = true;
        }
        return !errorFound;
    }


    private boolean validateLookUPCustomItemTables() {
        AtomicInteger errors = new AtomicInteger();
        Set<CompanyCustomFieldItem> lookUpCFs = new HashSet<>();


        for (Map.Entry<String, EditableTable> mapTable : editableTableMap.entrySet()) {

            String uuid = mapTable.getKey();

            List<CompanyCustomFieldItem> itemCustom = itemCustomCFs.get(uuid);

            Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(uuid))
                    .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            for (String columnCode : columnsMap.keySet()) {
                if (UI_TYPE_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                    lookUpCFs.add(getCustomFieldItem(itemCustom, columnCode));
                }
            }

            EditableTable productTable = mapTable.getValue();
            ArrayList<CustomTableRpc> tableItem = new ArrayList<>();

            for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                for (CompanyCustomFieldItem lookUpCfItem : lookUpCFs) {
                    if (UI_TYPE_LOOKUP.equals(lookUpCfItem.getUiType())) {
                        CustomFieldLookUp t = (CustomFieldLookUp) productTable.getColumnById(i, lookUpCfItem.getColumnCode());
                        if (t.getSelectedItemID() == null && t.getText() != null && t.getText().trim().length() > 0 && !LookUp.wfmStrings.searchTypeMessage().equals(t.getText())) {
                            productTable.notValid(i, lookUpCfItem.getColumnCode());
                            t.getTextBox().addStyleName(Constants.ERROR_FORM_STYLE);
                            Utils.openParentSection(t);
                            errors.getAndIncrement();
                        }
                    }
                }
            }
        }
        return errors.get() == 0;
    }

    protected void save(String status) {
        if (!validate()) {
            return;
        }
        item.setOpportunityName(opportunityName.getText());
        item.setNumberData(numbering.getNumberData(false));
        item.setCurrencyId(currencyWidgets.getCurrencyID());
        item.setExchangeRate(currencyWidgets.getExchangeRate());
        item.setItems(hasProduct ? getOpportunityItemsData() : null);
        item.setCustomTableItems(getCustomObjectData());
        item.setNotes(noteWidget.getNewNotesToSave());
        item.setAmount(Utils.isNullOrEmpty(amount.getText()) ? 0 : parseByNumberFormat(amount.getText()));
        item.setClosingDate(closingDate.getDueDate());
        item.setAccountId(accountName.getSelectedItemID());
        item.setContactId(contactName.getSelectedItemID());
        item.setStageId(stage.getSelectedId());
        item.setProbability(Utils.isNullOrEmpty(probability.getText()) ? 0 : Double.valueOf(parseByNumberFormat(probability.getText())).floatValue());
        item.setTypeId(type.getSelectedId());
        item.setNextStep(nextStep.getText());
        item.setExpectedRevenue(Utils.isNullOrEmpty(expectedRevenue.getText()) ? 0 : parseByNumberFormat(expectedRevenue.getText()));
        item.setLeadSourceId(leadSource.getSelectedItemID());
        item.setCampaignId(null);
        item.setCampaignId(campaign.getSelectedItemID());
        item.setAssigneeId(assignee.getSelectedItemID());
        item.setBackupAssigneeID(backupAssignee.getSelectedItemID());
        item.setFromContactID(fromContactID);
        item.setAttachments(fileUpload.getAttachedFiles());
        item.setFormType(formType);
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        item.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        item.setSubTotal(subTotal.getValue());
        if (projectLookUp != null) {
            item.setProject(projectLookUp.getSelectedItem());
        }
        item.setDiscountTotal(discount.getValue());
        item.setTotal(totalPrice.getValue());
        item.setQuantityTotal(quantityTotal.getValue());
        if (!currencyWidgets.getCurrencyID().equals(currencyWidgets.getBaseCurrency().getId())) {
            item.setTotalInBase(baseTotal.getValue());
        } else {
            item.setTotalInBase(totalPrice.getValue());
        }
        BigDecimal taxTotal = BigDecimal.ZERO;
        if (item.getItems() != null) {
            item.getItems();
            for (OpportunityItem item : item.getItems()) {
                taxTotal = taxTotal.add(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO);
            }
        }
        item.setStatusCode(status);
        item.setApprovers(approvers.getChosenApprovers());
        item.setTaxTotal(taxTotal);

        if (firstClick.get()) {
            item.setRelations(item != null ? item.getRelations() : null);
        } else {
            item.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        enableButtons(false);
        LoadingPanel.loading(true);
        try {
            CRMService.App.get().saveOpportunity(item, opportunityConvertingTo, new AbstractAsyncCallback<SelectItem>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(final SelectItem selectItem) {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                    Info.show(successMessage, Info.Type.INFO);
                    onShellOk(selectItem);
                    if (selectItem != null && CT_PROJECT.equals(opportunityConvertingTo)) {
                        String editProject = GWT.getHostPageBaseURL() + "ProjectManagement.html#" + Constants.PROJECT + "|edit/" + selectItem.getId();
                        Window.open(editProject, "_blank", "");
                    }
                    if (formType != null && convertedFormId != null) {
                        saveConvertedRelations(selectItem.getId());
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, selectItem.getId(), AddOpportunityView.this);
                }
            });
        } catch (NumberExistingException e) {
            e.printStackTrace();
            enableButtons(true);
        }
    }

    private void saveConvertedRelations(Integer _objectId) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, RelationItem.TYPE_OPPORTUNITY, item.getOpportunityName(), convertedFormId, formType, item.getFromName()));
        AllInOneService.App.get().getAdditionalRelations(_objectId, RelationItem.TYPE_OPPORTUNITY, item.getOpportunityName(), convertedFormId, formType, item.getFromName(), new AbstractAsyncCallback<ArrayList<RelationItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<RelationItem> result) {
                if (result != null && result.size() > 1) {
                    result.remove(0);
                    for (RelationItem item_ : result) {
                        boolean haveRelationItem = false;

                        if (item.getConvertedRelations() != null) {
                            for (RelationItem item : item.getConvertedRelations()) {

                                if (item.getToType().equals(item_.getToType()) && item.getToID().equals(item_.getToID())) {
                                    haveRelationItem = true;
                                    break;
                                }
                            }
                        }
                        if (!haveRelationItem) {
                            relationItems.add(item_);
                        }
                    }
                }
                if (relationItems != null && relationItems.size() > 0) {
                    AllInOneService.App.get().saveRelations(RelationItem.TYPE_OPPORTUNITY, _objectId, item.getOpportunityName(), relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<RelationItem> selectItems) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            }
        });
    }

    protected void onShellOk(SelectItem selectItem) {
        if (saveAndClose) {
            closeTab("opportunity|summary/" + selectItem.getId() + "/" + item.isConvertedLead() + "/" + item.getContactId() + "/" + item.getAccountId(), selectItem != null && selectItem.getName() != null ? selectItem.getName() : item.getOpportunityName(), item.getOpportunityName());
        } else {
            reinit();
        }
    }

    public void reinit() {
        objectId = null;
        registerFields();
        initForm();
    }

//    private void getCurrency() {
//        LoadingPanel.loading(true);
//        CurrencyService.App.get().getCurrencyRateByDate(currencyBox.getSelectedId(), new DateNonConvertable(closingDate.dueDate.getDate()), new AbstractAsyncCallback<CurrencyListItem>() {
//            @Override
//            public void success(CurrencyListItem result) {
//                LoadingPanel.loading(false);
//                onExchangeRateChange(new BigDecimal(result.getExchangeRate()));
//            }
//
//            @Override
//            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
//            }
//        });
//    }

//    private void onExchangeRateChange(BigDecimal rate) {
//        exchangeRate.setText(AccountingUtils.get().formatExRate(rate.setScale(AccountingUtils.customExRateScale, BigDecimal.ROUND_HALF_UP)));
//        if (!hasProduct) {
//            BigDecimal newAmount = AccountingUtils.get().parseToBigDecimal(amount.getValue());
//            newAmount = newAmount != null && BigDecimal.ZERO.compareTo(newAmount) != 0 ? newAmount.divide(generalExchangeRate, 2, BigDecimal.ROUND_HALF_UP) : newAmount;
//            amount.setText(numberFormat.format(newAmount.multiply(rate)));
//            generalExchangeRate = BigDecimal.ZERO.compareTo(rate) != 0 ? rate : BigDecimal.ONE;
//        }
//        calculate();
//    }

    public void calculate() {
        if (hasProduct) {
            BigDecimal subTotalAmount = BigDecimal.ZERO, discountAmount = BigDecimal.ZERO, quantity = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;
            final Map<Integer, BigDecimal> taxTotal = new HashMap<>();
            for (int rowID = 0; rowID < productTable.getGrid().getRowCount(); rowID++) {
                BigDecimal subtotal = BigDecimal.ZERO, discount = BigDecimal.ZERO, effectiveDiscount = BigDecimal.ZERO, itemTaxAmount = BigDecimal.ZERO, effectiveTaxAmount = BigDecimal.ZERO;
                CustomCellTextBox txtPrice = null;
                if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
                    txtPrice = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
                    if (txtPrice.getLayoutData() != null) {
                        txtPrice.setValue(AccountingUtils.get().formatPrice(AccountingUtils.get().parseToBigDecimal(txtPrice.getText())));
                    }
                }
                if (columnsMap.containsKey(ItemTableConstants.QTY)) {
                    CustomCellTextBox txtQty = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.QTY);
                    if (txtQty.getValue() != null && !txtQty.getValue().isEmpty()) {
                        if (txtPrice != null && txtPrice.getValue() != null && !txtPrice.getValue().isEmpty()) {
                            subtotal = subtotal.add(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue().replace(" ", ""))
                                    .multiply(AccountingUtils.get().parseToBigDecimal(txtQty.getValue().replace(" ", ""))));
                        }
                        quantity = quantity.add(AccountingUtils.get().parseToBigDecimal(txtQty.getValue().replace(" ", "")));
                    }
                }
                if (columnsMap.containsKey(ItemTableConstants.DISCOUNT_AMT)) {
                    OpportunityDiscountWidget discountWidget = (OpportunityDiscountWidget) productTable.getColumnById(rowID, ItemTableConstants.DISCOUNT_AMT);
                    if (OpportunityDiscountWidget.DEFAULT_DISCOUNT_TYPE_UNIT.equals(discountWidget.getDiscountUnit()) && BigDecimal.ZERO.compareTo(discountWidget.getValue()) != 0) {
                        discount = subtotal.multiply(discountWidget.getValue()).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                    } else {
                        discount = discountWidget.getValue();
                    }
                }
                effectiveDiscount = Utils.hasGenericAccess(GenericSettingsEnum.DISCOUNT_NOT_EFFECTED) ? effectiveDiscount : discount;
                if (columnsMap.containsKey(ItemTableConstants.TAX_LIST)) {
                    SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) productTable.getColumnById(rowID, ItemTableConstants.TAX_LIST);
                    if (taxLookUp != null && taxLookUp.getSelectedItemID() != null) {
                        itemTaxAmount = calculateTaxAmount(taxLookUp, taxTotal, subtotal.subtract(effectiveDiscount));
                    } else if (taxLookUp != null) {
                        taxLookUp.setItemTaxAmount(BigDecimal.ZERO);
                    }

                }
                effectiveTaxAmount = Utils.hasGenericAccess(GenericSettingsEnum.DISCOUNT_NOT_EFFECTED) ? effectiveTaxAmount : itemTaxAmount;
                ExtendedHTML netAmount = (ExtendedHTML) productTable.getColumnById(rowID, ItemTableConstants.NET_AMT);
                if (netAmount != null) {
                    netAmount.setText(numberFormat.format(subtotal.subtract(effectiveDiscount)));
                    netAmount.setValue(subtotal.subtract(effectiveDiscount));
                    CustomCell netAmountCell = (CustomCell) productTable.getColumnCellWidgetById(rowID, ItemTableConstants.NET_AMT);
                    netAmountCell.InActive();
                }

                ExtendedHTML totalWidget = (ExtendedHTML) productTable.getColumnById(rowID, ItemTableConstants.TOTAL_AMT);
                if (totalWidget != null) {
                    BigDecimal lineTotal = subtotal.subtract(effectiveDiscount);
                    if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalcTypeListBox.getSelectedId())) {
                        lineTotal = lineTotal.add(effectiveTaxAmount);
                    }
                    totalWidget.setText(numberFormat.format(lineTotal));
                    totalWidget.setValue(lineTotal);
                    CustomCell totalCell = (CustomCell) productTable.getColumnCellWidgetById(rowID, ItemTableConstants.TOTAL_AMT);
                    totalCell.InActive();
                }

                subTotalAmount = subTotalAmount.add(subtotal);
                discountAmount = discountAmount.add(discount);
                totalAmount = totalAmount.add(subtotal.subtract(effectiveDiscount));
                if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalcTypeListBox.getSelectedId())) {
                    totalAmount = totalAmount.add(effectiveTaxAmount);
                }
            }
            amount.setValue(numberFormat.format(totalAmount));
            subTotal.setValue(subTotalAmount);
            discount.setValue(discountAmount);
            totalPrice.setValue(totalAmount);
            if (generalExchangeRate != null) {
                baseTotal.setValue(totalAmount.divide(generalExchangeRate, 8, RoundingMode.HALF_UP));
            }
            quantityTotal.setValue(quantity);
        }

        drawTotalsTable();

        calculateExpectedRevenue();
    }

    private BigDecimal calculateTaxAmount(TaxLookUp taxLookUp, Map<Integer, BigDecimal> taxTotal, BigDecimal discountedNet) {
        TaxView taxHTML = (TaxView) taxWidgetMap.get(taxLookUp.getSelectedItemID());
        BigDecimal itemTaxAmount = BigDecimal.ZERO;

        if (taxHTML == null) {
            taxHTML = new TaxView();
            taxHTML.setItem(taxLookUp.getSelectedData());
            taxWidgetMap.put(taxLookUp.getSelectedItemID(), taxHTML);
        }

        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
            BigDecimal taxPercent = taxHTML.getItem().getEffectiveTaxPercent();
            itemTaxAmount = discountedNet.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
            itemTaxAmount = discountedNet.multiply(taxHTML.getItem().getEffectiveTaxPercent().divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
        }

        itemTaxAmount = itemTaxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        BigDecimal totalTax = taxTotal.get(taxLookUp.getSelectedItemID());
        BigDecimal currentTaxTotal = (totalTax != null ? totalTax : BigDecimal.ZERO).add(itemTaxAmount);

        taxHTML.setHTML(numberFormat.format(currentTaxTotal));
        taxTotal.put(taxLookUp.getSelectedItemID(), currentTaxTotal);
        taxLookUp.setItemTaxAmount(itemTaxAmount);

        return itemTaxAmount;
    }

    public void onTaxCalculationTypeChange(Integer type, boolean calculate) {
        setTaxCalculationType(type == null ? AccountingConstants.TAX_CALCULATION_EXCLUSIVE : type, calculate);
    }

    private void setTaxCalculationType(Integer taxCalculationType, boolean calculate) {
        this.taxCalculationType = taxCalculationType;
        for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
            SmartTaxRateLookUp lookUp = (SmartTaxRateLookUp) productTable.getColumnById(i, ItemTableConstants.TAX_LIST);
            if (lookUp != null) {
                if (AccountingConstants.NO_TAX_CALCULATION.equals(taxCalculationType)) {
                    lookUp.clear();
                    lookUp.setEnabled(false);

                    CustomCell taxCell = (CustomCell) this.productTable.getColumnCellWidgetById(i, ItemTableConstants.TAX_LIST);
                    if (taxCell != null) {
                        taxCell.InActive();
                    }
                } else {
                    lookUp.setEnabled(true);
                }
            }
        }
        if (calculate) {
            calculate();
        }
    }

    public void drawTotalsTable() {
        opportunityReceiptTable.clear();
        if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
            opportunityReceiptTable.setSubtotalItem(subTotalLabel, subTotal);
        }
        if (columnsMap.containsKey(ItemTableConstants.DISCOUNT_AMT)) {
            opportunityReceiptTable.setDiscountItem(discountLabel, discount);
        }

        if (columnsMap.containsKey(ItemTableConstants.TAX_LIST)) {
            for (Integer key : taxWidgetMap.keySet()) {
                TaxView tax = (TaxView) taxWidgetMap.get(key);
                HTML taxLabel = new HTML(tax.getItem().getName());
                taxLabel.setStyleName("totallabel");
                tax.setStyleName("totalvalue");

                opportunityReceiptTable.addItem(taxLabel, tax);
            }
        }

        totalLabel.setHTML(accountingMessages.dynamicTotal(currencyWidgets.getCurrencyName()));
        if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
            opportunityReceiptTable.addGrossItem(totalLabel, totalPrice);
        }

        if (columnsMap.containsKey(ItemTableConstants.UNITPRICE) && !currencyWidgets.getCurrencyID().equals(currencyWidgets.getBaseCurrency().getId())) {
            baseTotalLabel.setHTML(accountingMessages.dynamicTotal(currencyWidgets.getBaseCurrencyName()));
            opportunityReceiptTable.addGrossItem(baseTotalLabel, baseTotal);
        }
        if (columnsMap.containsKey(ItemTableConstants.QTY)) {
            opportunityReceiptTable.addGrossItem(new HTML(wfmStrings.qty()), quantityTotal);
        }
    }

    private void calculateExpectedRevenue() {
        BigDecimal totalAmount = AccountingUtils.get().parseToBigDecimal(amount.getValue());
        double expRevenue = 0;
        double pro;
        try {
            pro = parseByNumberFormat(probability.getText());
        } catch (NumberFormatException e) {
            pro = 0;
        }
        if (pro == 100) {
            if (contactName.getSelectedItem() == null) {
                //contactName.getSuggestBox().setFocus(true);
                contactName.getSuggestBox().addBlurHandler(event -> {
                    if (contactName.getSelectedItem() != null) {
                        contactName.getSuggestBox().getTextBox().removeStyleName(Constants.ERROR_FORM_STYLE);
                    } else {
                        markAsError(contactName.getSuggestBox().getTextBox(), true);
                    }
                });
            }
        }
        if (stage.getSelectedItem() != null) {
            expRevenue = totalAmount.doubleValue() * pro / 100;
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_OPPORTUNITY_EXPECTED_REVENUE)) {
            expectedRevenue.setValue(numberFormat.format(totalAmount) + "");
        } else {
            expectedRevenue.setValue(numberFormat.format(expRevenue) + "");
        }
    }

    private double parseByNumberFormat(String d) {
        try {
            return numberFormat.parse(d);
        } catch (NumberFormatException e1) {
            return 0d;
        }
    }

    private void onAccountAdded() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ACCOUNTS_ADD_EDIT, AddOpportunityView.this, (sender, args) -> {
            if (args != null && args instanceof CrmAccountItem) {
                CrmAccountItem crmAccountItem = (CrmAccountItem) args;
                accountName.setSelected(crmAccountItem.getObjectId(), crmAccountItem.getName());
                contactName.clear();
                contactName.refreshOracle(true);
            }
        });
    }

    private void onContactAdded() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, AddOpportunityView.this, (sender, args) -> {
            if (args instanceof ContactListItem) {
                onContactSelected(((ContactListItem) args).getObjectId());
            }
        });
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddOpportunityView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectId;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_OPPORTUNITY;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getOpportunityName() : null;
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
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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

    @Override
    public String getPropertyCode() {
        return Constants.Opportunities;
    }

    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.OPPORTUNITY_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        AddOpportunityView.this.configMap = result;

                        String fieldID = configMap.getKey();
                        ColumnConfigs[] configs = configMap.getValue();
                        if (configs != null && configs.length == 0) {
                            continue;
                        }

                        Map<String, ColumnConfigs> columnsMap = Stream.of(configs)
                                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

                        EditableTable editableTable = new EditableTable(getCustomColumns(columnsMap), true, true);

                        editableTableMap.put(fieldID, editableTable);

                        editableTable.setLayoutData(fieldID);
                        editableTable.setDraggable(true);
                        editableTable.setWidth("100%");
                        editableTable.setListener(new EditableTableListener() {
                            @Override
                            public void addRow() {
                                editableTable.addRow(getCustomWidgets(new CustomTableRpc(), fieldID));
                            }

                            @Override
                            public void removeRow() {

                            }
                        });
                        for (int i = 0; i < 3; i++) {
                            editableTable.addRow(getCustomWidgets(new CustomTableRpc(), fieldID));
                        }
                        addField(fieldID, editableTable, null, true);
                    }
                }
            }
        });
    }

    private Widget[] getCustomWidgets(CustomTableRpc item, String fieldID) {
        int index = 0;

        Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(fieldID))
                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (itemCustomCFs.containsKey(fieldID)) {

                CompanyCustomFieldItem cfItem = getCustomFieldItem(itemCustomCFs.get(fieldID), columnCode);

                if (UI_TYPE_TEXTBOX.equals(cfItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(cfItem.getUiType()) || UI_TYPE_URL.equals(cfItem.getUiType())) {
                    CustomTextBoxField t = new CustomTextBoxField(cfItem);
                    t.setWidth("100%");
                    if (DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        Validation.addNumericKeyboardListener(t, 5, true);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                    CustomPercentageField t = new CustomPercentageField(cfItem);
                    t.setWidth("100%");
                    t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (UI_TYPE_DROPDOWN.equals(cfItem.getUiType())) {
                    CustomDropDownField d = new CustomDropDownField(cfItem);
                    d.setWidth("100%");
                    if (cfItem.getPredefinedValues() != null) {
                        SelectItem[] sItems = new SelectItem[cfItem.getPredefinedValues().length];
                        int x = 0;
                        for (String s : cfItem.getPredefinedValues()) {
                            sItems[x] = new SelectItem(x, s);
                            x++;
                        }
                        d.setItems(sItems);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        d.setSelectedByValue(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (UI_TYPE_DATEPICKER.equals(cfItem.getUiType())) {
                    CustomDatePicker d = new CustomDatePicker(cfItem);
                    d.setWidth("100%");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        d.setDate(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfItem.getUiType())) {
                    CustomDateTime customDateTime = new CustomDateTime(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        customDateTime.setDateTime(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    customDateTime.setTitle(columnCode);
                    widgets[index++] = customDateTime;

                } else if (Constants.UI_TYPE_TEXTAREA.equals(cfItem.getUiType())) {
                    CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        textAreaField.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    textAreaField.setTitle(columnCode);
                    widgets[index++] = textAreaField;
                } else if (Constants.UI_TYPE_HTML_TEXTAREA.equals(cfItem.getUiType())) {
                    CustomHTMLTextAreaField htmlTextAreaField = new CustomHTMLTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        htmlTextAreaField.setData(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    htmlTextAreaField.setTitle(columnCode);
                    widgets[index++] = htmlTextAreaField;
                } else if (Constants.UI_TYPE_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }
                    lookup.setTitle(columnCode);
                    widgets[index++] = lookup;
                } else if (Constants.UI_TYPE_CURRENCY.equals(cfItem.getUiType())) {
                    CustomFieldCurrencyWidget currencyWidget = new CustomFieldCurrencyWidget(cfItem, "CustomForm");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            currencyWidget.setCurrency(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }

                    currencyWidget.setTitle(columnCode);
                    widgets[index++] = currencyWidget;
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldMultiLookUpField multiLookUp = new CustomFieldMultiLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        ArrayList<SelectItem> list = new ArrayList<>();
                        if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                            multiLookUp.setSelectedItems(list);
                        }
                    }

                    multiLookUp.setTitle(columnCode);
                    widgets[index++] = multiLookUp;
                } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(cfItem.getUiType())) {

                    CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getItem() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getItem().getId(), customFieldItem.getItem().getName()));
                            textAreaField.setText(customFieldItem.getItem().getDescription());
                        }
                    }
                    lookup.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

                        if (lookup.getSelectedItem() != null && lookup.getSelectedItem().getId() != null) {
                            AllInOneService.App.get().getProductDescription(lookup.getSelectedItem().getId(), new AbstractAsyncCallback<String>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    super.failure(throwable);
                                }

                                @Override
                                public void success(String result) {
                                    if (result != null) {
                                        textAreaField.setText(result);
                                        lookup.getSelectedItem().setDescription(result);
                                        int currentRowId = editableTableMap.get(fieldID).getGrid().getCurrentRow();
                                        CustomCell cel = (CustomCell) editableTableMap.get(fieldID).getColumnCellWidgetById(currentRowId, columnCode + "_DESCRIPTION");
                                        cel.InActive();
                                    }
                                }
                            });
                        }
                    });

                    lookup.setTitle(columnCode);

                    textAreaField.setTitle(wfmStrings.description());
                    widgets[index++] = lookup;
                    widgets[index++] = textAreaField;

                }
            }
        }
        return widgets;
    }

    private ColumnConfig[] getCustomColumns(Map<String, ColumnConfigs> columnsMap) {
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            switch (cc) {
                case ItemTableConstants.PRODUCT:
                    columns[i++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.productOrService(), 100, columnsMap.get(cc).isRequired());
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columns[i++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 100, columnsMap.get(cc).isRequired());
                    break;
                default:
                    ColumnConfig columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), 100, columnsMap.get(cc).isRequired(), true);
                    if (columnsMap.get(cc).getWidth() != null && columnsMap.get(cc).getWidth() > 0) {
                        columnConfig.setWidth(columnsMap.get(cc).getWidth());
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                    }
                    if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
                        ColumnConfig columnConfigItem = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigItem.setPixel(false);
                        columnConfigItem.setForceWidthInPercent(true);
                        columns[i++] = columnConfigItem;

                        ColumnConfig columnConfigDescription = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode() + "_DESCRIPTION", wfmStrings.description(), columnsMap.get(cc).getWidth() * 40 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigDescription.setPixel(false);
                        columnConfigDescription.setForceWidthInPercent(true);
                        columns[i++] = columnConfigDescription;
                    } else {
                        columns[i++] = columnConfig;
                    }
                    break;
            }
        }
        return columns;
    }

    private CompanyCustomFieldItem getCustomFieldItem(List<CompanyCustomFieldItem> companyCustomFieldItems, String columnCode) {
        return companyCustomFieldItems.stream()
                .filter(item -> columnCode.equals(item.getColumnCode()))
                .findFirst()
                .orElse(new CompanyCustomFieldItem());
    }

    private HashMap<String, ArrayList<CustomTableRpc>> getCustomObjectData() {
        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();
        for (Map.Entry<String, EditableTable> mapTable : editableTableMap.entrySet()) {

            String uuid = mapTable.getKey();

            List<CompanyCustomFieldItem> itemCustom = itemCustomCFs.get(uuid);

            Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(uuid))
                    .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            EditableTable productTable = mapTable.getValue();
            ArrayList<CustomTableRpc> tableItem = new ArrayList<>();
            ArrayList<CompanyCustomFieldItem> resultItemList;
            for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                CustomTableRpc result = new CustomTableRpc();
                resultItemList = new ArrayList<>();
                for (String columnCode : columnsMap.keySet()) {
                    if (itemCustomCFs.containsKey(uuid)) {
                        Object customFieldValue = null;
                        Integer customFieldValueId = null;
                        SelectItem itemValue = null;
                        if (UI_TYPE_TEXTBOX.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomTextBoxField t = (CustomTextBoxField) productTable.getColumnById(i, columnCode);
                            if (t.getText() != null && !t.getText().isEmpty()) {
                                customFieldValue = t.getText();
                            }
                        }
                        if (Constants.UI_TYPE_PERCENTAGE.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomPercentageField percentageField = (CustomPercentageField) productTable.getColumnById(i, columnCode);
                            if (percentageField != null && !percentageField.getText().isEmpty()) {
                                customFieldValue = percentageField.getText();
                            }

                        } else if (UI_TYPE_DROPDOWN.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDropDownField t = (CustomDropDownField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                            }
                        } else if (UI_TYPE_DATEPICKER.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDatePicker t = (CustomDatePicker) productTable.getColumnById(i, columnCode);
                            if (t.getDate() != null) {
                                customFieldValue = t.getDate();
                            }
                        } else if (UI_TYPE_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                                customFieldValueId = t.getSelectedItem().getId();
                            }
                        } else if (UI_TYPE_CURRENCY.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldCurrencyWidget t = (CustomFieldCurrencyWidget) productTable.getColumnById(i, columnCode);
                            if (t.getCurrencyID() != null) {
                                customFieldValue = t.getCurrencyName();
                                customFieldValueId = t.getCurrencyID();
                            }
                        } else if (UI_TYPE_MULTI_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItems() != null && t.getSelectedItems().size() > 0) {
                                customFieldValue = t.getSelectedItems();
                            }
                        } else if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUp item = (CustomFieldLookUp) productTable.getColumnById(i, columnCode);
                            CustomTextAreaField desc = (CustomTextAreaField) productTable.getColumnById(i, columnCode + "_DESCRIPTION");
                            if (item.getSelectedItem() != null) {
                                itemValue = new SelectItem(item.getSelectedItemID(), item.getSelectedItem().getName(), desc.getText());
                            }
                        }
                        CompanyCustomFieldItem companyCustomFieldItem = getCustomFieldItem(itemCustom, columnCode);
                        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
                        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                        resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());

                        if (customFieldValue != null) {
                            if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                                resultItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFieldValue));
                            } else {
                                resultItem.setFieldStringValue((String) customFieldValue);
                            }
                            if (customFieldValueId != null) {
                                resultItem.setSelectedId(customFieldValueId);
                            }
                        }
                        if (itemValue != null) {
                            resultItem.setItem(itemValue);
                        }
                        resultItemList.add(resultItem);
                    }
                }
                result.setUuid(uuid);
                result.setItemCustomFields(resultItemList);
                tableItem.add(result);
            }
            map.put(uuid, tableItem);
        }
        return map;
    }

    private void setItemTableValues(HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
        if (tableItems != null && tableItems.size() > 0) {
            for (Map.Entry map : tableItems.entrySet()) {
                String uuid = (String) map.getKey();
                if (editableTableMap.get(uuid) != null) {
                    editableTableMap.get(uuid).removeAllRows();
                }
                for (CustomTableRpc item : (List<CustomTableRpc>) map.getValue()) {
                    if (editableTableMap.get(uuid) != null) {
                        editableTableMap.get(uuid).addRow(getCustomWidgets(item, uuid));
                    }
                }
            }
        }
    }

    private int[] validateRequiredItems(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[2];
        SmartProductLookUp product = (SmartProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);

        if (requiredColumnCodes.contains(ItemTableConstants.PRODUCT)) {
            if (Utils.hasGenericAccess(GenericSettingsEnum.DISABLE_ONE_OF_ITEM_OPPORTUNITY) && product.getSelectedItemID() == null) {
                productTable.setColumnValid(ItemTableConstants.PRODUCT);
                errors++;
            } else if (product.getSuggestBox() == null || product.getSuggestBox().getText() == null || product.getSuggestBox().getText().isEmpty() || wfmStrings.searchTypeMessage().equals(product.getSuggestBox().getText())) {
                productTable.setColumnValid(ItemTableConstants.PRODUCT);
                errors++;
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
            TextArea2 descriptionTxtArea = (TextArea2) productTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
            if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                productTable.setColumnValid(ItemTableConstants.DESCRIPTION);
                errors++;
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
            CustomCellTextBox qtyTxtBox = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.QTY);
            if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                productTable.setColumnValid(ItemTableConstants.QTY);
                errors++;
            } else if (columnsMap.get(ItemTableConstants.QTY) != null && AccountingUtils.get().parseToBigDecimal(qtyTxtBox.getValue()).compareTo(columnsMap.get(ItemTableConstants.QTY).getMinValue()) < 0) {
                productTable.setColumnValid(ItemTableConstants.QTY);
                errors++;
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.MEASUREMENT)) {
            MeasurementsLookUp measurementLookUp = (MeasurementsLookUp) productTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
            if (!Validation.validateLookUpRequired(measurementLookUp)) {
                productTable.setColumnValid(ItemTableConstants.MEASUREMENT);
                errors++;
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.UNITPRICE)) {
            CustomCellTextBox txtPrice = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
            if (!Validation.validateTextBoxRequired(txtPrice)) {
                productTable.setColumnValid(ItemTableConstants.UNITPRICE);
                errors++;
            } else if (columnsMap.get(ItemTableConstants.UNITPRICE) != null && AccountingUtils.get().parseToBigDecimal(txtPrice.getValue()).compareTo(columnsMap.get(ItemTableConstants.UNITPRICE).getMinValue()) < 0) {
                productTable.setColumnValid(ItemTableConstants.UNITPRICE);
                errors++;
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.PROJECT)) {
            ProjectLookUp projectLookUp = (ProjectLookUp) productTable.getColumnById(rowID, ItemTableConstants.PROJECT);
            if (!Validation.validateLookUpRequired(projectLookUp)) {
                productTable.setColumnValid(ItemTableConstants.PROJECT);
                errors++;
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.DISCOUNT_LIST)) {
            WfmDropdown discountList = (WfmDropdown) productTable.getColumnById(rowID, ItemTableConstants.DISCOUNT_LIST);
            if (discountList.getSelectedItem() == null) {
                productTable.setColumnValid(ItemTableConstants.DISCOUNT_LIST);
                errors++;
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.DISCOUNT_AMT)) {
            OpportunityDiscountWidget discountWidget = (OpportunityDiscountWidget) productTable.getColumnById(rowID, ItemTableConstants.DISCOUNT_AMT);
            if (discountWidget.getValue() == null) {
                productTable.setColumnValid(ItemTableConstants.DISCOUNT_AMT);
                errors++;
            } else if (discountWidget.getValue().compareTo(BigDecimal.ZERO) == 0) {
                productTable.setColumnValid(ItemTableConstants.DISCOUNT_AMT);
                errors++;
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.CLIENT)) {
            CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) productTable.getColumnById(rowID, ItemTableConstants.CLIENT);
            if (supplierLookUp.getSelectedItemID() == null) {
                productTable.setColumnValid(ItemTableConstants.CLIENT);
                errors++;
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        productTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }/* else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                productTable.setColumnValid(fieldItem.getColumnCode());
                                errors++;
                            }
                        }*/
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        productTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        productTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText() != null && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            productTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        productTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText() != null && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            productTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedId() == null) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            }
        }

        error[0] = errors;
        error[1] = nonRequired;
        return error;
    }

    private void colorizeErrorField(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        SmartProductLookUp productLookUp = (SmartProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.QTY);

        if (requiredColumnCodes.contains(ItemTableConstants.PRODUCT)) {
            if (productLookUp.getSelectedItem() == null || (productLookUp.getText() == null || productLookUp.getText().isEmpty() || wfmStrings.searchTypeMessage().equals(productLookUp.getText()))) {
                productTable.notValid(rowID, ItemTableConstants.PRODUCT);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
            if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                productTable.notValid(rowID, ItemTableConstants.QTY);
            } else if (columnsMap.get(ItemTableConstants.QTY) != null && AccountingUtils.get().parseToBigDecimal(qtyTxtBox.getValue()).compareTo(columnsMap.get(ItemTableConstants.QTY).getMinValue()) < 0) {
                productTable.notValid(rowID, ItemTableConstants.QTY);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
            TextArea2 descriptionTxtArea = (TextArea2) productTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
            if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                productTable.notValid(rowID, ItemTableConstants.DESCRIPTION);
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.MEASUREMENT)) {
            SmartMeasurementsLookUp measurementLookUp = (SmartMeasurementsLookUp) productTable.getColumnById(rowID, ItemTableConstants.MEASUREMENT);
            if (!Validation.validateLookUpRequired(measurementLookUp)) {
                productTable.notValid(rowID, ItemTableConstants.MEASUREMENT);
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.UNITPRICE)) {
            CustomCellTextBox txtPrice = (CustomCellTextBox) productTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
            if (!Validation.validateTextBoxRequired(txtPrice)) {
                productTable.notValid(rowID, ItemTableConstants.UNITPRICE);
            } else if (columnsMap.get(ItemTableConstants.UNITPRICE) != null && AccountingUtils.get().parseToBigDecimal(qtyTxtBox.getValue()).compareTo(columnsMap.get(ItemTableConstants.UNITPRICE).getMinValue()) < 0) {
                productTable.notValid(rowID, ItemTableConstants.UNITPRICE);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.DISCOUNT_LIST)) {
            WfmDropdown discountList = (WfmDropdown) productTable.getColumnById(rowID, ItemTableConstants.DISCOUNT_LIST);
            if (discountList.getSelectedItem() == null) {
                productTable.notValid(rowID, ItemTableConstants.DISCOUNT_LIST);
            }
        }
        if (requiredColumnCodes.contains(ItemTableConstants.DISCOUNT_AMT)) {
            OpportunityDiscountWidget discountWidget = (OpportunityDiscountWidget) productTable.getColumnById(rowID, ItemTableConstants.DISCOUNT_AMT);
            if (discountWidget.getValue() == null) {
                discountWidget.getTxtDiscount().addStyleName(Constants.ERROR_FORM_STYLE);
                productTable.notValid(rowID, ItemTableConstants.DISCOUNT_AMT);
            } else if (discountWidget.getValue().compareTo(BigDecimal.ZERO) == 0) {
                discountWidget.getTxtDiscount().addStyleName(Constants.ERROR_FORM_STYLE);
                productTable.notValid(rowID, ItemTableConstants.DISCOUNT_AMT);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.CLIENT)) {
            CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) productTable.getColumnById(rowID, ItemTableConstants.CLIENT);
            if (supplierLookUp.getSelectedItemID() == null) {
                supplierLookUp.getSuggestBox().addStyleName(Constants.ERROR_FORM_STYLE);
                productTable.notValid(rowID, ItemTableConstants.CLIENT);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.PROJECT)) {
            ProjectLookUp projectLookUp = (ProjectLookUp) productTable.getColumnById(rowID, ItemTableConstants.PROJECT);
            if (!Validation.validateLookUpRequired(projectLookUp)) {
                projectLookUp.getSuggestBox().addStyleName(Constants.ERROR_FORM_STYLE);
                productTable.notValid(rowID, ItemTableConstants.PROJECT);
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            }
            if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateEmailRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateUrl(t, null)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        productTable.notValid(rowID, fieldItem.getColumnCode());
                    }/* else {
                            if (t.getText() != null && t.getText().trim().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                                productTable.notValid(rowID, fieldItem.getColumnCode());
                            }
                        }*/
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        productTable.notValid(rowID, fieldItem.getColumnCode());

                    }
                }
            } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            }
        }

    }

    private void onContactSelected(Integer contactId) {
        ContactService.App.get().getContact(contactId, false, new AsyncCallback<ContactListItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ContactListItem contactListItem) {
                contactName.clear();
                contactName.refreshOracle(true);
                contactName.setSelected(contactListItem.getObjectId(), contactListItem.getName());
                if (contactListItem.getCrmAccount() != null) {
                    accountName.setSelected(contactListItem.getCrmAccount().asSelectItem());
                }

            }
        });
    }

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getDefaultValue() != null) {
            probability.setText(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).getSelectedId() != null) {
            assignee.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).getSelectedId() != null) {
            backupAssignee.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getSelectedId() != null) {
            projectLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getDefaultValue() != null) {
            opportunityName.getSuggestBox().setText(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).getSelectedId() != null) {
            accountName.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).getSelectedId() != null) {
            contactName.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).getSelectedId() != null) {
            leadSource.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).getSelectedId() != null) {
            campaign.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).getSelectedId() != null) {
            type.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_TYPE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null && formPropertyMap.get(CustomFormConstants.CURRENCY).getSelectedId() != null) {
//            currencyWidgets.onCurrencyLoaded(() -> generalExchangeRate = currencyWidgets.getExchangeRate());
            if (!isCopying) {
                currencyWidgets.setCurrency(new SelectItem(formPropertyMap.get(CustomFormConstants.CURRENCY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CURRENCY).getDefaultValue()));
                currencyID = formPropertyMap.get(CustomFormConstants.CURRENCY).getSelectedId();
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getSelectedId() != null) {
            stage.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_STAGE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE) != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getSelectedId() != null) {
            taxCalcTypeListBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getDefaultValue() != null) {
            nextStep.setText(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getDefaultValue() != null) {
            amount.setText(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getDefaultValue() != null) {
            expectedRevenue.setText(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue() != null && noteWidget != null && noteWidget.getTextBox() != null) {
            noteWidget.getTextBox().setText(formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE) != null && formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                closingDate.dueDate.setDate(currentDate);
            } else {
                try {
                    closingDate.dueDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setCrmAccountBalanceEdit(OpportunityListItem item, MaterialLink customerBalanceLink) {
        CRMService.App.get().getCrmAccountBalance(item.getAccountId(), new AbstractAsyncCallback<Double>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Double customerBalance) {
                String balance = numberFormat.format(customerBalance);
                customerBalanceLink.setText(balance);
                customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + item.getCrmAccountItem().getObjectId() + "/" + CrmAccountItem.CUSTOMER,
                        wfmStrings.balance() + ": " + item.getCrmAccountItem().getName()));
            }
        });
    }

    private static void setCrmAccountBalanceAdd(CRMLookUp accountName, MaterialLink customerBalanceLink) {
        Integer accountId = accountName != null && accountName.getSelectedItem() != null ? accountName.getSelectedItem().getId() : null;
        String customerName = accountName != null && accountName.getSelectedItem() != null ? accountName.getSelectedItem().getName() : null;
        CRMService.App.get().getCrmAccountBalance(accountId, new AbstractAsyncCallback<Double>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Double customerBalance) {
                String balance = numberFormat.format(customerBalance);
                customerBalanceLink.setText(balance);
                customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + accountId + "/" + CrmAccountItem.CUSTOMER,
                        wfmStrings.balance() + ": " + customerName));
            }
        });
    }

    private void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(item.getContactItem(), param);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                int relation = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value.trim())) {
                        value = value.replace("|", "-");
                        getKeyValuElement(value, relation, param);
                    }
                }
            }
        }
    }

    private void getKeyValuElement(final String value, int relation, int param) {
        FlowPanel f = new FlowPanel();
        f.getElement().getStyle().setDisplay(Style.Display.FLEX);
        f.addStyleName("firstChildHasPadding mb-2");
        PhonePopup popup;
        String phoneNumber = null;
        boolean mobile = false;
        HTML relationS = null;
        if (param == Constants.CONTACT_PHONES) {
            switch (relation) {
                case Constants.G_HOME:
                    relationS = new HTML(getTitle(wfmStrings.home()));
                    phoneNumber = value;
                    break;
                case Constants.G_WORK:
                    relationS = new HTML(getTitle(wfmStrings.contactwork()));
                    phoneNumber = value;
                    break;
                case Constants.G_OTHER:
                    relationS = new HTML(getTitle(wfmStrings.other()));
                    phoneNumber = value;
                    break;
                case Constants.G_MOBILE:
                    relationS = new HTML(getTitle(wfmStrings.mobile()));
                    phoneNumber = value;
                    mobile = true;
                    break;
                case Constants.G_HOME_FAX:
                    relationS = new HTML(getTitle(wfmStrings.homeFax()));
                    phoneNumber = value;
                    break;
                case Constants.G_WORK_FAX:
                    relationS = new HTML(getTitle(wfmStrings.workFax()));
                    phoneNumber = value;
                    break;
                case Constants.G_PAGER:
                    relationS = new HTML(getTitle(wfmStrings.pager()));
                    phoneNumber = value;
                    break;
                case Constants.G_EXTENSION:
                    relationS = new HTML(getTitle(wfmStrings.extension()));
                    phoneNumber = value;
                    break;
                case Constants.G_FAX:
                    relationS = new HTML(getTitle(wfmStrings.fax()));
                    phoneNumber = value;
                    break;
                case Constants.G_WHATS_APP:
                    relationS = new HTML(getTitle(wfmStrings.whatsApp()));
                    phoneNumber = value;
                    break;
                case Constants.G_TELEGRAM:
                    relationS = new HTML(getTitle(wfmStrings.telegram()));
                    phoneNumber = value;
                    break;
                case Constants.G_VIBER:
                    relationS = new HTML(getTitle(wfmStrings.viber()));
                    phoneNumber = value;
                    break;
            }
            f.getElement().setAttribute("style", "display: flex; align-items: center");
            popup = new PhonePopup(phoneNumber, item.getContactItem(), mobile);
            Div phoneWidget = popup.getPhoneWidget();
            f.add(relationS);
            f.add(phoneWidget);
            contactPhone.add(f);
        }
    }
}
