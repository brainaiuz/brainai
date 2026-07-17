package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddUnitMeasurementView;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.ui.ContactCategoryPropertiesDialog;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.reference.WebAddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
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
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TelegramChatSingleLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunityReceiptTable;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUpload;
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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;
import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ADD_PREFIX_TO_ADD_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVED;

/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class AddContactView extends CustomForm2 implements Colapse, FormHasCustomFieldInterface {

    public static final String PARAM_TEXT_BOX = "PARAM_TEXT_BOX";
    private static final String RELATION_LIST_BOX = "RELATION_LIST_BOX";
    private static final String PRIMARY_RADIO_BUTTON = "PRIMARY_RADIO_BUTTON";
    private static final String RELATED_CHAT = "RELATED_CHAT";
    public static final String FOR_SEND_INVOICE_QUOTE = "forSendInvoiceQuote";
    public static final String FROM_OUTLOOK = "fromOutlook";
    public static final String FROM_INCOMING_CALL = "phone";
    public static final String FROM_OPPORTUNITY = "fromOpportunity";
    public static final String FROM_COMPANY = "fromCompany";

    public LinkedHashMap imAddressMap = new LinkedHashMap();
    public final DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
    public final DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMMM");
    public final ContactServiceAsync contactService = ContactService.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    protected String successMessage;
    protected String errorMessage;

    private ContactCategoryTree tree = null;
    private ModelForm modelForm;

    int errors = 0;
    protected boolean forceToSave = false;
    private Integer accountID = null;
    private Integer categoryID = null;
    protected CheckboxMailingListDataGrid mailListTable;
    public String actionString = "";

    public String viewName = Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact());
    public Integer objectId;
    public ContactListItem item;
    public DataListBox imAddressLocation;
    public CrmAccountItem emptyCrmAccount;
    private CrmAccountItem parentItem;

    protected Integer campaignID;
    protected String campaignName;

    public GeneralFileUpload uploadForm;
    //faqatgina webformada ishlatish mumkin esdan chiqmasin
    public GWTFileUpload uploadFormMini;

    public TextBox firstName;
    public TextBox middleName;
    public TextBox lastName;
    public TextBox otherName;
    public TextBox title;
    public DOBWidget birthDOBWidget;
    protected NoteWidget noteWidget;
    protected CrmActivityGrid activityWidget;
    public DataListBox titl;
    public CRMLookUp companyName;
    public DataListBox industries;
    public MatrixTable accountType;
    public TextBox jobTitle;
    public TextBox jobFunction;
    public TextBox refIndNumber;
    public TextBox department;
    public TextBox assets;
    public KpiCheckBox copyAccountAddress;
    public HTML accountAddressLabel;
    public TextBox connection;
    public CRMLookUp campaignSource;
    public KpiCheckBox emailOpt;
    public DataListBox contactOwner;
    public DataListBox reportsTo;
    public String DEFAULT_WIDTH = Constants.DEFAULT_WIDTH;

    public boolean saveAndClose = false;
    public boolean saveAndNew = false;
    public boolean saveAndAddOpp = false;
    public MultiTableNewUI emailInf;
    public MultiTableNewUI phoneNumInf;
    public MultiTableNewUI imsAddressInf;
    public MultiTableNewUI webSiteInf;
    public MultiTableNewUI addressInf;
    public MultiTableNewUI parentAddressInf;
    public MultiTableNewUI telegramInf;
    public MultiTable categories;
    public MultiTableNewUI relations;
    public TextArea2 backgrounInformation;
    public TextArea2 disclaimer;
    private SelectItem[] relationsDropDownItems = new SelectItem[0];
    private SelectItem[] imAddressDropDownItems = new SelectItem[0];
    private TreeSelectItem[] categoriesDropDownsItems = new TreeSelectItem[0];

    public Div categoryPanel;
    private FlexTable verticalPanel;
    protected boolean fromOpportunity;
    protected boolean fromCompany;

    protected int contactType = ContactListItem.CRM_CONTACT;
    protected boolean showRequired = false;
    private String[] params;
    private Address address;
    private final Map<Integer, Address> parentAddressesMap = new HashMap<>();
    private final String addContactView = "add_contact_view_";
    protected boolean isCopying;
    public Command popupCommand;
    public boolean isPopup;
    //From Outlook plugin params
    private LinkedHashMap<String, String> pluginParams;
    protected ProfileImage profilePicture;
    public String defaultPhone;
    public LinkedHashMap<String, FormProperty> formPropertyMap;
    public boolean isAddView;
    public ListingFilterParameter filterParametrs = new ListingFilterParameter();
    private static final Integer DEFAULT_ITEM_ROWS = 3;
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    public BigDecimal generalExchangeRate = BigDecimal.ONE;
    protected EditableTable productTable;
    protected OpportunityReceiptTable receiptTable;
    protected boolean hasProduct;
    protected ExtendedHTML subTotal,
            totalPrice,
            taxTotal,
            quantityTotal;
    protected HTML subTotalLabel,
            totalLabel,
            totalTaxLabel;
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private final Map<String, CompanyCustomFieldItem> itemCFs = new LinkedHashMap<>();
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();
    private HashMap<Integer, Widget> taxWidgetMap;
    protected final Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();
    private SelectItem[] telegramBots;

    public AddContactView(String name) {
        super(name);
    }

    public AddContactView(String name, String description) {
        super(name);
        setDescription(description);
    }

    public AddContactView(String name, String description, String params) {
        super(name);
        setDescription(description);
        parsePluginParams(params);
    }

    public AddContactView(Integer objectId, String name, boolean isCopying) {
        super(name == null ? "addcontact" : name);
        if (objectId != null) {
            this.objectId = objectId;
            viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
        }
        this.isCopying = isCopying;
    }

//    public AddContactView(Integer objectId, String name, String description, boolean isCopying) {
//        super(name == null ? "addcontact" : name);
//        setDescription(description == null ? property.getSingular(wfmStrings.addContact(), wfmStrings.contact()) : description);
//        if (objectId != null) {
//            this.objectId = objectId;
//            viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
//        }
//        this.isCopying = isCopying;
//    }

//    public AddContactView(Integer objectId, boolean isCoping) {
//        super("addcontact");
//        setDescription(property.getSingular(wfmStrings.addContact(), wfmStrings.contact()));
//        if (objectId != null) {
//            setDescription(property.getSingular(wfmStrings.editContact(), wfmStrings.contact()));
//            this.objectId = objectId;
//            viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
//        }
//        this.isCopying = isCoping;
//    }

//    public AddContactView(Integer objectId, Integer accountID) {
//        this(objectId, null, null, false);
//        if (accountID != null) {
//            this.accountID = accountID;
//        }
//    }

//    public AddContactView(Integer objectId, Integer accountID, String actionString) {
//        this(objectId, false);
//        if (accountID != null) {
//            this.accountID = accountID;
//        }
//        if (actionString != null) {
//            this.actionString = actionString;
//        }
//    }

//    public AddContactView(Integer objectId, Integer accountID, Integer categoryID) {
//        this(objectId, null, null, false);
//        if (accountID != null) {
//            this.accountID = accountID;
//        }
//        if (categoryID != null) {
//            this.categoryID = categoryID;
//        }
//    }

//    public AddContactView(Integer campaignID, String campaignName) {
//        super("addcontact");
//        setDescription(property.getSingular(wfmStrings.addContact(), wfmStrings.contact()));
//        this.campaignID = campaignID;
//        this.campaignName = campaignName;
//    }

    public AddContactView(String[] params) {
        super("addcontact");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.contact()));
        this.params = params;
        this.isAddView = "add".equals(params[0]);
        initFormParameters(params);
    }

    protected void parsePluginParams(String params) {
        if (params != null && !"".equals(params)) {
            String[] paramsArray = params.split("&\\$&");
            if (paramsArray != null && paramsArray.length > 0) {
                pluginParams = new LinkedHashMap<>();
                for (String param : paramsArray) {
                    String[] pair = param.split("=");
                    if (pair != null && pair.length > 1) {
                        pluginParams.put(pair[0], pair[1]);
                    }
                }
            }
        }
    }


    private void initFormParameters(String[] params) {
        String numberExp = "^\\d+$";

        boolean hasPermissiontoAdd = Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CONTACT);
        if (params != null && params.length > 1) {
            if (params.length > 3) {
                if (AddContactView.FROM_OPPORTUNITY.equals(params[2])) {
                    setDescription(params[2] == null ? property.getSingular(wfmStrings.addMess(), wfmStrings.contact()) : params[2]);
                    fromOpportunity = true;
                    if (params[1] != null && !params[1].isEmpty()) {
                        this.accountID = Integer.valueOf(params[1]);
                    }
                    if (params[3] != null && !params[3].isEmpty()) {
                        this.objectId = Integer.valueOf(params[3]);
                    }
                } else if (AddContactView.FROM_COMPANY.equals(params[2])) {
                    fromCompany = true;
                    if (params[1] != null && !params[1].isEmpty()) {
                        this.accountID = Integer.valueOf(params[1]);
                    }
                    if (params[3] != null && !params[3].isEmpty()) {
                        this.objectId = Integer.valueOf(params[3]);
                    }
                } else {
                    if (params[2] != null && params[2].matches(numberExp)) {
                        this.campaignID = Integer.valueOf(params[2]);
                        this.campaignName = params[3];
                    } else {
                        if (hasPermissiontoAdd) {
                            Integer objectId = null;
                            this.campaignName = params[3];
                        }
                    }
                }
            } else if (params.length > 2) {
                if (AddContactView.FOR_SEND_INVOICE_QUOTE.equals(params[2]) && hasPermissiontoAdd) {
                    if (params[1] != null) {
                        this.accountID = Integer.valueOf(params[1]);
                    }
                    if (params[2] != null) {
                        this.actionString = params[2];
                    }
                } else if (AddContactView.FROM_OUTLOOK.equals(params[2])) {
                    parsePluginParams(params[1]);
                } else if (AddContactView.FROM_INCOMING_CALL.equals(params[2])) {
                    setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.contact()));
                    if (params[1] != null) {
                        defaultPhone = params[1];
//                        this.objectId = Integer.valueOf(params[1]);
//                        viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
                    }
                } else if (AddContactView.FROM_OPPORTUNITY.equals(params[2])) {
                    setDescription(params[2] == null ? property.getSingular(wfmStrings.addMess(), wfmStrings.contact()) : params[2]);
                    fromOpportunity = true;
                    if (params[1] != null && !params[1].isEmpty()) {
                        this.accountID = Integer.valueOf(params[1]);
                    }
                    if (params[3] != null && !params[3].isEmpty()) {
                        this.objectId = Integer.valueOf(params[3]);
                    }
                } else if (params[2] != null && "COPY".equals(params[2])) {
                    if (params[1] != null && params[1].matches(numberExp)) {
                        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.contact()));
                        if (params[1] != null) {
                            setDescription(property.getSingular(wfmStrings.editContact(), wfmStrings.contact()));
                            this.objectId = Integer.valueOf(params[1]);
                            viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
                        }
                        this.isCopying = true;
                    } else {
                        if (hasPermissiontoAdd) {
                            setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.contact()));
                            if (params[1] != null) {
                                setDescription(property.getSingular(wfmStrings.editContact(), wfmStrings.contact()));
                                this.objectId = null;
                                viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
                            }
                            this.isCopying = true;
                        }
                    }
                } else if (AddContactView.FROM_COMPANY.equals(params[2])) {
                    fromCompany = true;
                    if (params[1] != null && !params[1].isEmpty()) {
                        this.accountID = Integer.valueOf(params[1]);
                    }
                    if (params[3] != null && !params[3].isEmpty()) {
                        this.objectId = Integer.valueOf(params[3]);
                    }
                } else {
                    if (hasPermissiontoAdd) {
                        if (params[1] != null && !params[1].isEmpty()) {
                            this.accountID = Integer.valueOf(params[1]);
                        }
                        if (params[2] != null) {
                            this.categoryID = Integer.valueOf(params[2]);
                        }
                    }
                }
            } else {
                if (hasPermissiontoAdd) {
                    if (params[1] != null && !params[1].isEmpty()) {
                        this.accountID = Integer.valueOf(params[1]);
                    }
                }
            }
        } else {
            if (hasPermissiontoAdd) {
                setDescription(params[2] == null ? property.getSingular(wfmStrings.addMess(), wfmStrings.contact()) : params[2]);
                if (params[1] != null) {
                    this.objectId = Integer.valueOf(params[1]);
                    viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
                }
                this.isCopying = Boolean.valueOf(params[3]);
            }
        }
    }

    protected void initPredefinedValues() {
        if (item != null) {
            addPredefinedValues(OWNER, item.getLeadAssignees());
            addPredefinedValues(INDUSTRY, item.getCrmAccount().getIndustries());
            addPredefinedValues(CRM_ACCOUNT_INDUSTRY, item.getCrmAccount().getIndustries());
            addPredefinedValues(REPORTS_TO, item.getSupervisors());
            addPredefinedValues(MARTIAL_STATUS, item.getMartialStatusList());
            if (LayoutRPC.LEAD_FORM.equals(getFormID())) {
                addPredefinedValues(LEAD_OWNER, item.getLeadAssignees());
                addPredefinedValues(ASSIGNEE, item.getLeadAssignees());
                addPredefinedValues(BACKUP_ASSIGNEE, item.getLeadAssignees());
                addPredefinedValues(RATING, item.getLeadRatings());
                addPredefinedValues(STATUS, item.getLeadStatuses());
                addPredefinedValues(LEAD_SOURCE, item.getLeadSources());
            }
            if (LayoutRPC.CANDIDATE_FORM.equals(getFormID())) {
                addPredefinedValues(STATUS, item.getCandidateStatuses());
                addPredefinedValues(LEAD_SOURCE, item.getCandidateSources());
                addPredefinedValues(CustomFormConstants.CANDIDATE.LOCATION, item.getLocations());
            }
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CONTACT_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId != null && !isCopying ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        //save and close button
        MaterialLink save = new MaterialLink(Utils.isWebForm() ? Utils.getButtonText() : (!fromOpportunity || !fromCompany ? wfmStrings.save() : wfmStrings.saveAndClose()));
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });

        if (Utils.isWebForm()) {
            addButton(wfmStrings.submit(), CustomFormConstants.SUBMIT, (addContactView + "submit_button"), event -> {
                saveAndClose = true;
                save();
            });
            addButton(wfmStrings.reset(), CustomFormConstants.RESET, (addContactView + "reset_button"), event -> Utils.reloadPage());
        }
        //save and new button
        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> {
            saveAndClose = false;
            saveAndNew = true;
            save();
        });


        if (LayoutRPC.CONTACT_FORM.equals(getFormID()) && (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST) || Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES))) {

            String saveAndAdd_Opp = property.getSingularWithLocalizedName(Constants.Opportunities, "");
            if (saveAndAdd_Opp.isEmpty()) {
                saveAndAdd_Opp = wfmStrings.saveAndAddOpportunity();
            } else {
                saveAndAdd_Opp = wfmStrings.saveAndAdd() + saveAndAdd_Opp;
            }
            MaterialLink saveAddOpp = new MaterialLink(saveAndAdd_Opp);
            saveAddOpp.addClickHandler(event -> {
                saveAndAddOpp = true;
                save();
            });
            splitButton.addItem(saveAddOpp);
        }
        splitButton.addItem(saveAdd);
        addButton(splitButton);
    }

    protected Widget onInitialize() {
        String leadSuccessAdd = Property.get(Constants.LEADS, wfmStrings.messSuccessfullyAdded(), wfmStrings.lead());
        String contactSuccessAdd = property.getSingular(wfmStrings.messSuccessfullyAdded(), wfmStrings.contact());
        String leadErrorAdd = Property.get(Constants.LEADS, wfmStrings.messAddLeadError(), wfmStrings.lead());
        String contactErrorAdd = property.getSingular(wfmStrings.messAddContactError(), wfmStrings.contact());
        String leadUpdateSuc = Property.get(Constants.LEADS, wfmStrings.messSuccessfullyUpdated(), wfmStrings.lead());
        String contactUpdateSuc = property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.contact());
        String leadUpdateError = Property.get(Constants.LEADS, wfmStrings.messLeadUpdateError(), wfmStrings.lead());
        String contactUpdateError = property.getSingular(wfmStrings.messContactUpdateError(), wfmStrings.contact());

        successMessage = isLead() ? leadSuccessAdd : isCandidate() ? Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.candidate()) : contactSuccessAdd;
        errorMessage = isLead() ? leadErrorAdd : isCandidate() ? wfmStrings.errorOccurredSavingChanges() : contactErrorAdd;
        if (objectId != null && !isCopying) {
            successMessage = (isLead() ? leadUpdateSuc : isCandidate() ? Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.candidate()) : contactUpdateSuc);
            errorMessage = isLead() ? leadUpdateError : isCandidate() ? wfmStrings.errorOccurredUpdate() : contactUpdateError;
        }

        CommonService.App.get().getCompanyAllCustomFields(ViewName.Lead, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
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

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(isLead() ? ViewName.Lead : (isCandidate() ? ViewName.Candidate : ViewName.Contact), isLead() ? LayoutRPC.LEAD_FORM : (isCandidate() ? LayoutRPC.CANDIDATE_FORM : LayoutRPC.CONTACT_FORM), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                AddContactView.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                    result.getCompanyCustomFieldItems().forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }
                AddContactView.super.onInitialize();
            }
        });

        if (isCandidate()) {

            CommonService.App.get().getCompanyCustomFields(ViewName.CandidateCustomItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    AddContactView.super.onInitialize();
                }

                @Override
                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                    }
                    drawItemTable();
                }
            });
        }
        CommonService.App.get().getCompanyCustomFields(ViewName.Lead, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
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
        service.getModelForm(getFormID(), new AbstractAsyncCallback<ModelForm>() {
            @Override
            public void failure(Throwable caught) {

            }

            @Override
            public void success(ModelForm result) {
                modelForm = result;
            }
        });
        return this;
    }

    protected void drawItemTable() {
        LoadingPanel.loading(true);
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.LEAD_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                LoadingPanel.loading(false);
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        AddContactView.this.configMap = result;

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

    @Override
    protected void registerFields() {
        initialize();
        drawForm();
        show();
    }

    public void initialize() {

        profilePicture = new ProfileImage(objectId, LayoutRPC.CONTACT_FORM);

        /* Personal Infomation  began */
        firstName = new TextBox();
        firstName.getElement().setId(addContactView + "firstName");

        middleName = new TextBox();
        middleName.getElement().setId(addContactView + "middleName");
        middleName.addStyleName(DEFAULT_WIDTH);

        lastName = new TextBox();
        lastName.getElement().setId(addContactView + "lastName");
        lastName.addStyleName(DEFAULT_WIDTH);

        otherName = new TextBox();
        otherName.getElement().setId(addContactView + "otherName");
        otherName.addStyleName("width250 file--AddContactView");

        birthDOBWidget = new DOBWidget();
        birthDOBWidget.getElement().setId(addContactView + "birthDOBWidget");
        birthDOBWidget.addStyleName(DEFAULT_WIDTH);

        reportsTo = new DataListBox();
        reportsTo.getElement().setId(addContactView + " reportsTo");
        reportsTo.addStyleName("width250");

        backgrounInformation = new TextArea2(1500);
        backgrounInformation.getTextArea().getElement().setId(addContactView + "backgrounInformation");
        backgrounInformation.addStyleName(DEFAULT_WIDTH);
        /* Personal Information end*/

        /*Company Information began*/
        if (isLead()) {
            companyName = new CRMLookUp(CRMLookUp.CRM_ACCOUNT, Constants.SUPPLIER);
        } else {
            companyName = new CRMLookUp(CRMLookUp.CRM_ACCOUNT);
        }
        companyName.setValueNotEmptyMeansSelected(true);
        companyName.getElement().setId(addContactView + "companyName");
        companyName.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                reinitCrmAccount(companyName.getOracle().getItemID(suggestionSelectionEvent.getSelectedItem().getDisplayString()));
            }
        });

        companyName.getSuggestBox().addBlurHandler(blurEvent -> {
            if (companyName.getOracle().getItemID(companyName.getText()) == null) {
                reinitCrmAccount(null);
            } else {
                reinitCrmAccount(companyName.getOracle().getItemID(companyName.getText()));
            }
        });
        companyName.addStyleName("width250");

        industries = new DataListBox();
        industries.addStyleName(DEFAULT_WIDTH);
        industries.getElement().setId(addContactView + "industries");
        industries.addStyleName("width250");

        accountType = new MatrixTable(3);
        accountType.setStyleName("crm-account-type");
        accountType.addStyleName("width250");

        jobTitle = new TextBox();
        jobTitle.addStyleName(DEFAULT_WIDTH);
        jobTitle.getElement().setId(addContactView + "jobTitle");
        jobTitle.addStyleName("width250");

        jobFunction = new TextBox();
        jobFunction.getElement().setId(addContactView + "jobFunction");
        jobFunction.addStyleName("width250");

        refIndNumber = new TextBox();
        refIndNumber.getElement().setId(addContactView + "refIndNumber");
        refIndNumber.addStyleName("width250");

        department = new TextBox();
        department.addStyleName(DEFAULT_WIDTH);
        department.getElement().setId(addContactView + "department");
        department.addStyleName("width250");

        assets = new TextBox();
        assets.getElement().setId(addContactView + "assets");

        titl = new DataListBox();
        titl.getElement().setId(addContactView + "titl");

        title = new TextBox();
        title.getElement().setId(addContactView + "title");
        title.setWidth("97px");
        title.setVisible(false);

        titl.setAllowFirstItem(true);
        titl.setWidth("122px");
        titl.addValueChangeHandler(event -> {
            title.setVisible(titl.getSelectedItem() != null && isOtherSelected(titl));
        });
        /*Company Information end*/

        /*Contact Information began*/
        emailInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getEmailWidgets(null, null, null);
            }

            public boolean isFilled() {
                for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
                    TextBox value = (TextBox) emailRow.get(PARAM_TEXT_BOX);
                    if (!isEmpty(value.getText()) && !wfmStrings.email().equals(value.getText())) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
//        emailInf.setWidth("275px");
//        emailInf.setAddLabel(wfmStrings.addEmail());

        phoneNumInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getPhoneWidgets(defaultPhone != null ? defaultPhone : null, null, defaultPhone != null ? defaultPhone : null);
            }

            @Override
            public boolean isFilled() {
                boolean isFilled = false;
                for (Map<String, Widget> widgetsMap : phoneNumInf.getWidgets()) {
                    PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTableNewUI.PHONE_NUMBER);
                    if (!"".equals(phoneNumber.toString())) {
                        isFilled = true;
                    }
                }
                return isFilled;
            }
        }, false);

        imsAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getIMAddressWidgets(null, null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> imAddressRow : imsAddressInf.getWidgets()) {
                    TextBox imAddress = (TextBox) imAddressRow.get(MultiTableNewUI.LIST_BOX);
                    if (!isEmpty(imAddress.getText())) {
                        return true;
                    }
                }
                return false;
            }
        }, false);

        relations = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getRelationWidgets(null, null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> relation : relations.getWidgets()) {
                    TextBox relationBox = (TextBox) relation.get(MultiTableNewUI.LIST_BOX);
                    if (!isEmpty(relationBox.getText())) {
                        return true;
                    }
                }
                return false;
            }
        }, false);

        webSiteInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getWebSiteWidgets(null, null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> imAddressRow : webSiteInf.getWidgets()) {
                    TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
                    if (!isEmpty(imAddress.getText())) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
        webSiteInf.getElement().setId(addContactView + "webAddress");

        copyAccountAddress = new KpiCheckBox();
        copyAccountAddress.getElement().setId(addContactView + "copyAccountAddress");
        copyAccountAddress.setEnabled(false);
        copyAccountAddress.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent != null) {
                if (booleanValueChangeEvent.getValue()) {
                    accountAddressLabel.setVisible(true);
                    accountAddressLabel.addStyleName("hidden-field--active");
                    getParentAccoundAddress();
                } else {
                    accountAddressLabel.setVisible(false);
                    accountAddressLabel.removeStyleName("hidden-field--active");
                    parentAddressInf.removeAllRows();
                }
            }
        });

        addressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null);
            }

            @Override
            public boolean isFilled() {
                if (isAddView) {
                    for (Map<String, Widget> addressRow : addressInf.getColumnsWidget()) {
                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                        if (addressWidget != null && addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    for (Map<String, Widget> addressRow : addressInf.getWidgets()) {
                        AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                        if (addressWidget != null && addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }, false);
        addressInf.getElement().setId("addressInformation");

        parentAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(address);
            }

            @Override
            public boolean isFilled() {
                if (isAddView) {
                    for (Map<String, Widget> addressRow : addressInf.getColumnsWidget()) {
                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                        if (addressWidget != null && addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    for (Map<String, Widget> addressRow : addressInf.getWidgets()) {
                        AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                        if (addressWidget != null && addressWidget.isNotEmpty()) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }, false);

        telegramInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getTelegramWidgets(null);
            }

            public boolean isFilled() {
                for (Map<String, Widget> telegramRow : telegramInf.getWidgets()) {
                    DataListBox value = (DataListBox) telegramRow.get(RELATION_LIST_BOX);
                    if (value.getSelectedId() != null) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
        getTelegramBots();

        connection = new TextBox();
        connection.getElement().setId(addContactView + "connection");
        connection.addStyleName("width250");
        /*Contact Information end*/

        /*Additional Information begin */
        tree = new ContactCategoryTree();
        tree.getElement().setId(addContactView);

        verticalPanel = new FlexTable();
        verticalPanel.addStyleName("box-radius--top");
        ScrollPanel catecoryPanel = new ScrollPanel();
        catecoryPanel.add(tree);
        com.google.gwt.user.client.Element oldElement = verticalPanel.getElement();
        com.google.gwt.dom.client.Element element = oldElement;
        TableElement tableElement = (TableElement) element;
        TableSectionElement tHead = tableElement.createTHead();
        TableRowElement row = tHead.insertRow(0);
        Element th = DOM.createTH();
        DOM.appendChild(row, th);
        th.setInnerText(wfmStrings.category());
        Div div = new Div();
        div.add(catecoryPanel);

        if (Utils.hasPermission(CRM_CONTACT_CATEGORY_ADD)) {
            Anchor addNewCategory = new Anchor(wfmStrings.addCategory());
            addNewCategory.addClickHandler(event -> new ContactCategoryPropertiesDialog(null, null));
            div.add(addNewCategory);
        }

        categoryPanel = new Div("custom-gwt-panel");
        categoryPanel.add(verticalPanel);
        categoryPanel.add(div);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CATEGORY_ADD, this, (sender, args) -> {
            if (args instanceof ContactCategoryListItem) {
                initCategoryWidgets((ContactCategoryListItem) args);
            }
        });
//        reportsTo.getElement().getStyle().setMarginLeft(5, Unit.PX);
        contactOwner = new DataListBox();
        contactOwner.getElement().setId(addContactView + " contactOwner");
        contactOwner.addStyleName(DEFAULT_WIDTH);
        /*Additional Information end*/

        /*Crm Details began*/
        campaignSource = new CRMLookUp(CrmConstants.CRM_CAMPAIGN_ID);
        campaignSource.addStyleName(DEFAULT_WIDTH);
        campaignSource.getElement().setId(addContactView + " campaignSource");
        campaignSource.addStyleName("width250");

        emailOpt = new KpiCheckBox();
        emailOpt.getElement().setId(addContactView + " emailOpt");
        emailOpt.getElement().getStyle().setMarginLeft(5, Unit.PX);
        noteWidget = new NoteWidget(isCopying ? null : objectId, getEntityType());
        if (objectId != null && !isCopying) {
            activityWidget = new CrmActivityGrid(objectId, getEntityType());
        }
        noteWidget.getTextBox().getTextArea().getElement().setId(addContactView + " noteWidget");

        taxWidgetMap = new HashMap<>();

        if (!Utils.isWebForm()) {
            uploadForm = new GeneralFileUpload((isLead() ? Constants.F_LEAD : isCandidate() ? Constants.F_CANDIDATE : Constants.F_CRM_CONTACT), isCopying ? null : objectId, true,  isCopying ? null : objectId, null);
            uploadForm.getElement().setId(addContactView + "uploadForm");

            uploadFormMini = new GWTFileUpload(true);
            uploadFormMini.setStyleName("GWTUpld-abs");
            uploadFormMini.setEnabled(true);
        }
        /*Crm Details end*/
    }

    protected void drawForm() {
        addField(TITLE, null, getTitle(wfmStrings.title()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(FIRST_NAME, new InputGroup(titl, firstName), getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation()) {
                new KpiToolTip(firstName, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getInformationText());
            }

            firstName.setEnabled(!formPropertyMap.get(CustomFormConstants.FIRST_NAME).isDisabled());
        } else {
            addField(FIRST_NAME, new InputGroup(titl, firstName), getTitle(wfmStrings.firstName(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            addField(LAST_NAME, lastName, getTitle(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.LAST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.LAST_NAME).isInformation()) {
                new KpiToolTip(lastName, formPropertyMap.get(CustomFormConstants.LAST_NAME).getInformationText());
            }
            lastName.setEnabled(!formPropertyMap.get(CustomFormConstants.LAST_NAME).isDisabled());
        } else {
            addField(LAST_NAME, lastName, getTitle(wfmStrings.lastName(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MIDDLE_NAME) != null) {
            addField(MIDDLE_NAME, middleName, getTitle(formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName(), formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).isRequired()));
            middleName.setEnabled(!formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).isDisabled());
        } else {
            addField(MIDDLE_NAME, middleName, getTitle(wfmStrings.middleName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OTHER_NAME) != null) {
            addField(OTHER_NAME, otherName, getTitle(formPropertyMap.get(CustomFormConstants.OTHER_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.OTHER_NAME).getTitle() : wfmStrings.otherName(), formPropertyMap.get(CustomFormConstants.OTHER_NAME).isRequired()));
            otherName.setEnabled(!formPropertyMap.get(CustomFormConstants.OTHER_NAME).isDisabled());
        } else {
            addField(OTHER_NAME, otherName, getTitle(wfmStrings.otherName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY) != null) {
            addField(BIRTH_DAY, birthDOBWidget, getTitle(formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isChanged() ? formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getTitle() : wfmStrings.dateOfBirth(), formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isInformation()) {
                new KpiToolTip(birthDOBWidget, formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getInformationText());
            }

//            birthDOBWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isDisabled());
        } else {
            addField(BIRTH_DAY, birthDOBWidget, getTitle(wfmStrings.dateOfBirth()));
        }

        addField(PROFILE_PICTURE, profilePicture, null, true);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null) {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.company(), showRequired || formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isInformation()) {
                new KpiToolTip(companyName, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getInformationText());
            }
            companyName.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isDisabled());
        } else {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(wfmStrings.company()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null) {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, accountType, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getTitle() : wfmStrings.accountType(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isInformation()) {
                new KpiToolTip(accountType, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getInformationText());
            }

            accountType.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, accountType, getTitle(wfmStrings.accountType()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null) {
            addField(JOB_TITLE, jobTitle, getTitle(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.JOB_TITLE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.JOB_TITLE).isInformation()) {
                new KpiToolTip(jobTitle, formPropertyMap.get(CustomFormConstants.JOB_TITLE).getInformationText());
            }

            jobTitle.setEnabled(!formPropertyMap.get(CustomFormConstants.JOB_TITLE).isDisabled());
        } else {
            addField(JOB_TITLE, jobTitle, getTitle(wfmStrings.jobTitle()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DEPARTMENT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DEPARTMENT).isInformation()) {
                new KpiToolTip(department, formPropertyMap.get(CustomFormConstants.DEPARTMENT).getInformationText());
            }

            department.setEnabled(!formPropertyMap.get(CustomFormConstants.DEPARTMENT).isDisabled());
        } else {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER) != null) {
            addField(REF_IND_NUMBER, refIndNumber, getTitle(formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).getTitle() : "Ref Ind Number", formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).isRequired()));
            refIndNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).isDisabled());
        } else {
            addField(REF_IND_NUMBER, refIndNumber, getTitle("Ref Ind Number"));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSETS) != null) {
            addField(CustomFormConstants.ASSETS, assets, getTitle(formPropertyMap.get(CustomFormConstants.ASSETS).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSETS).getTitle() : wfmStrings.assetsUnderManagement(), formPropertyMap.get(CustomFormConstants.ASSETS).isRequired()));
            assets.setEnabled(!formPropertyMap.get(CustomFormConstants.ASSETS).isDisabled());
        } else {
            addField(CustomFormConstants.ASSETS, assets, getTitle(wfmStrings.assetsUnderManagement()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null) {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getTitle() : wfmStrings.industry(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isInformation()) {
                new KpiToolTip(industries, formPropertyMap.get(CustomFormConstants.JOB_TITLE).getInformationText());
            }

            industries.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isDisabled());
        } else {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(wfmStrings.industry()));
        }

        addTitleField(CONTACT_INFORMATION, isCandidate() ? wfmStrings.candidateInformation() : property.getSingular(wfmStrings.contactInformation(), wfmStrings.contact()));

        emailInf.getElement().setId("Crm_contact_email");
        emailInf.addStyleName("addFieldSet file--AddContactView");
        boolean isRequiredEmail = FOR_SEND_INVOICE_QUOTE.equals(actionString) || (item != null && !item.isCrmContact() && !item.isLeadContact()) || (item != null && item.isLeadContact() && showRequired);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(CustomFormConstants.EMAIL, emailInf, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.EMAIL).isRequired() || isRequiredEmail), false,
                    formPropertyMap.get(CustomFormConstants.EMAIL).isInformation());
            if (formPropertyMap.get(CustomFormConstants.EMAIL).isInformation()) {
                new KpiToolTip(emailInf, formPropertyMap.get(CustomFormConstants.EMAIL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.EMAIL, emailInf, getTitle(wfmStrings.email(), isRequiredEmail));
        }

        telegramInf.getElement().setId("Crm_contact_telegram");
        telegramInf.addStyleName("addFieldSet");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TELEGRAM) != null) {
            addField(CustomFormConstants.TELEGRAM, telegramInf, getTitle(formPropertyMap.get(CustomFormConstants.TELEGRAM).isChanged() ? formPropertyMap.get(CustomFormConstants.TELEGRAM).getTitle() : wfmStrings.telegram(), formPropertyMap.get(CustomFormConstants.TELEGRAM).isRequired()));
        } else {
            addField(CustomFormConstants.TELEGRAM, telegramInf, wfmStrings.telegram());
        }

        phoneNumInf.getElement().setId("Crm_contact_phoneNumber");
        phoneNumInf.addStyleName("addFieldSet");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null) {
            addField(CustomFormConstants.PHONE, phoneNumInf, getTitle(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(), formPropertyMap.get(CustomFormConstants.PHONE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PHONE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PHONE).isInformation()) {
                new KpiToolTip(phoneNumInf, formPropertyMap.get(CustomFormConstants.PHONE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PHONE, phoneNumInf, getTitle(wfmStrings.phone()));
        }

        imsAddressInf.addStyleName("addFieldSet");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null) {
            addField(IM_ADDRESS, imsAddressInf, getTitle(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress(), formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isInformation()) {
                new KpiToolTip(imsAddressInf, formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getInformationText());
            }
        } else {
            addField(IM_ADDRESS, imsAddressInf, getTitle(wfmStrings.imAddress()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null) {
            addField(WEB_ADDRESS, webSiteInf, getTitle(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress(), formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isInformation()) {
                new KpiToolTip(webSiteInf, formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getInformationText());
            }
        } else {
            addField(WEB_ADDRESS, webSiteInf, getTitle(wfmStrings.webAddress()));
        }

        relations.addStyleName("addFieldSet");
        relations.getElement().setId("relationship-texBox");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATIONSHIP) != null) {
            addField(RELATIONSHIP, relations, getTitle(formPropertyMap.get(CustomFormConstants.RELATIONSHIP).isChanged() ? formPropertyMap.get(CustomFormConstants.RELATIONSHIP).getTitle() : wfmStrings.relationship(), formPropertyMap.get(CustomFormConstants.RELATIONSHIP).isRequired()));
        } else {
            addField(RELATIONSHIP, relations, getTitle(wfmStrings.relationship()));
        }

        copyAccountAddress.setName("sameAsGetParentAddress");
        copyAccountAddress.setHTML(wfmStrings.accoundAddress());
        if (objectId == null) {
            copyAccountAddress.setValue(false);
        }
        accountAddressLabel = new HTML("<b>" + wfmStrings.parentAccountAddress() + "</b>");
        accountAddressLabel.addStyleName("hidden-label");
//        accountAddressLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
//        accountAddressLabel.getElement().getStyle().setMarginTop(5, Unit.PX);
        accountAddressLabel.setVisible(false);

        VerticalPanel accountAddressPanel = new VerticalPanel();
//        accountAddressPanel.setSpacing(3);
        accountAddressPanel.addStyleName("accountAddressPanel");
        accountAddressPanel.add(copyAccountAddress);
        accountAddressPanel.add(accountAddressLabel);
        accountAddressPanel.add(parentAddressInf);

        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(ADDRESS, addressInf, property.getSingular(wfmStrings.primaryContactAddress(), wfmStrings.contact()), true);
        addField(PARENT_ADDRESSES, accountAddressPanel, wfmStrings.parentAccountAddress(), true);

        if (contactType != ContactListItem.LEAD_CONTACT) {
            addField(CATEGORY, categoryPanel, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTS_TO) != null) {
            addField(REPORTS_TO, reportsTo, getTitle(formPropertyMap.get(CustomFormConstants.REPORTS_TO).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTS_TO).getTitle() : wfmStrings.supervisor(), formPropertyMap.get(CustomFormConstants.REPORTS_TO).isRequired()));
            reportsTo.setEnabled(!formPropertyMap.get(CustomFormConstants.REPORTS_TO).isDisabled());
        } else {
            addField(REPORTS_TO, reportsTo, getTitle(wfmStrings.supervisor()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null) {
            addField(OWNER, contactOwner, getTitle(formPropertyMap.get(CustomFormConstants.OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.OWNER).getTitle() : wfmStrings.owner(), formPropertyMap.get(CustomFormConstants.OWNER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.OWNER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.OWNER).isInformation()) {
                new KpiToolTip(contactOwner, formPropertyMap.get(CustomFormConstants.OWNER).getInformationText());
            }

            contactOwner.setEnabled(!formPropertyMap.get(CustomFormConstants.OWNER).isDisabled());
        } else {
            addField(OWNER, contactOwner, getTitle(wfmStrings.owner()));
        }

        // Crm Details
        addTitleField(CRM_DETAILS, wfmStrings.crmDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null) {
            addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getTitle() : wfmStrings.campaign(), formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isInformation()) {
                new KpiToolTip(campaignSource, formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getInformationText());
            }

            campaignSource.setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isDisabled());
        } else {
            addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(wfmStrings.campaign()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT) != null) {
            addField(EMAIL_OPT_OUT, emailOpt, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).getTitle() : wfmStrings.emailOptOut(), formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isRequired()));
            emailOpt.setEnabled(!formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isDisabled());
        } else {
            addField(EMAIL_OPT_OUT, emailOpt, getTitle(wfmStrings.emailOptOut()));
        }

        if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            mailListTable = new CheckboxMailingListDataGrid(objectId, false, null);
            addField(SUBSCRIPTION_LIST, mailListTable, null);
        }

        addField(CRM_ACTIVITIES, activityWidget, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()), true);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null) {
            addField(CRM_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.meetingNotesAttachments(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CRM_NOTE).isInformation()) {
                new KpiToolTip(noteWidget, formPropertyMap.get(CustomFormConstants.CRM_NOTE).getInformationText());
            }

            if (noteWidget.getTextBox() != null) {
                noteWidget.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.CRM_NOTE).isDisabled());
            }
        } else {
            addField(CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        addField(ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
        addField(ATTACHMENTS_MINI, uploadFormMini, wfmStrings.attachments(), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.CAPTCHA_ADD_TO_FORM, null, AddContactView.this);
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (CONTACT_INFORMATION.equals(fieldID)) {
                return isCandidate() ? wfmStrings.candidateInformation() : wfmStrings.basicDetails();
            } else {
                return getLocalizer().localizeByFieldID(getFormID(), fieldID);
            }
        }
        return null;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ContactService.App.get().editContact(contactType, objectId, accountID, Utils.isWebForm()
                ? Utils.getWebFormID()
                : null, false, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;

                    profilePicture.initialize(o.getContactImageUrl(), o.getFirstName(), o.getLastName(), true);

                    if (isCopying && objectId != null) {
                        item.setObjectId(null);
                        item.setEntityID(null);
                        objectId = null;
                        item.getRelations().clear();
                    }
                    setPluginParams(item);
                    setContactItem();
                    if (o.getObjectId() == null) {
                        setDefaultValues();
                        setDefaultValuesByFormProperty();
                    }
                });
            }
        });
    }

    @Override
    protected String getWikiCode() {
        if (Utils.isCRM()) {
            return PermissionConstants.CRM_CONTACT_ADD;
        } else {
            return null;
        }
    }

    public String getIconStyle() {
        return "icon-edit";
    }

    protected String getEntityType() {
        return RelationItem.TYPE_CONTACT;
    }

    private void recursionCategoryAdd(ContactCategoryListItem[] categories, ContactCategoryTreeItem parent) {
        if (categories != null) {
            for (ContactCategoryListItem category : categories) {
                if (category != null) {
                    if (category.isShared()) {
                        ContactCategoryTreeItem item = new ContactCategoryTreeItem(category, tree, parent);
                        if (parent != null) {
                            parent.addItem(item);
                        } else {
                            tree.addItem(item);
                        }
                        if (category.getChildren() != null && category.getChildren().length > 0) {
                            recursionCategoryAdd(category.getChildren(), item);
                        }
                    } else {
                        if (category.getChildren() != null && category.getChildren().length > 0) {
                            recursionCategoryAdd(category.getChildren(), parent);
                        }
                    }
                }
            }
        }
    }

    private void initCategoryWidgets(ContactCategoryListItem... newDropDownItems) {
        Set<Integer> newSelectedCategories = new HashSet<>();
        if (!categories.isEmpty()) {
            for (Map<String, Widget> widgets : categories.getWidgets()) {
                if (widgets != null && widgets.containsKey(MultiTable.LIST_BOX)) {
                    DataListBox listBox = (DataListBox) widgets.get(MultiTable.LIST_BOX);
                    if (listBox.getSelectedItem() != null) {
                        newSelectedCategories.add(listBox.getSelectedItem().getId());
                    }
                }
            }
        }
        TreeSelectItem[] categoriesDropDownsItems_ = new TreeSelectItem[categoriesDropDownsItems.length + newDropDownItems.length];
        int i = 0;
        for (TreeSelectItem selectItem : categoriesDropDownsItems) {
            categoriesDropDownsItems_[i++] = selectItem;
        }
        for (ContactCategoryListItem newDropDownItem : newDropDownItems) {
            if (newDropDownItem != null) {
                categoriesDropDownsItems_[i++] = newDropDownItem.asTreeSelectItem();
            }
        }
        categoriesDropDownsItems = categoriesDropDownsItems_;
        categories.removeAllRows();
        for (Integer selectedID : newSelectedCategories.toArray(new Integer[]{})) {
            categories.addWidgets(getCategoryWidgets(selectedID));
        }
    }

    private WidgetsMap getCategoryWidgets(Integer selectedID) {
        WidgetsMap widgetsMap = new WidgetsMap();
        DataListBox listBox = new DataListBox();
        listBox.setWithoutNullLabel(true);
//        listBox.addStyleName("width250");
        if (getCategoriesDropDownsItems() != null) {
            listBox.setItems(getCategoriesDropDownsItems());
        }
        if (selectedID != null) {
            listBox.setSelected(selectedID);
        } else {
            if (categoryID != null) {
                listBox.setSelected(categoryID);
            } else {
                listBox.setSelectedDefault();
            }
        }
        widgetsMap.addWidgetToMap(MultiTable.LIST_BOX, listBox);
        widgetsMap.addWidgets(listBox);
        return widgetsMap;
    }

    private WidgetsMap getRelationWidgets(SelectItem item) {
        return item != null ? getRelationWidgets(item.getDescription(), item.getId()) : getRelationWidgets(null, null);
    }

    private WidgetsMap getIMAddressWidgets(SelectItem item) {
        return item != null ? getIMAddressWidgets(item.getDescription(), item.getId()) : getIMAddressWidgets(null, null);
    }

    private WidgetsMap getRelationWidgets(String name, Integer id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        TextBox relationTextBox = new TextBox();
        if (name != null) {
            relationTextBox.setText(name);
        }
        DataListBox relationListBox = new DataListBox();
        relationListBox.setWidth("130px");
        relationListBox.setItems(getRelationsDropDownItems());
        if (id != null) {
            relationListBox.setSelected(id);
        }
        widgetsMap.addToCenter(MultiTableNewUI.TEXT_BOX, relationTextBox);
        widgetsMap.addToLeft(MultiTableNewUI.LIST_BOX, relationListBox);
        return widgetsMap;
    }

    /**
     * @return WidgetsMap
     */
    private WidgetsMap getWebSiteWidgets(String name, Integer id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        TextBox webAddress = new TextBox();
        webAddress.getElement().setId("webAddress-textBox");
        if (name != null) {
            webAddress.setText(name);
        }
        DataListBox webAddressLocation = new DataListBox();
        webAddressLocation.setWithoutNullLabel(true);
        webAddressLocation.setItems(getWebAddress());
        webAddressLocation.getElement().setId("leads-webAddressLocation");
        if (id != null) {
            webAddressLocation.setSelected(id);
        } else {
            webAddressLocation.setSelected(WebAddressReference.WORK.getId());
        }
        widgetsMap.addToCenter(PARAM_TEXT_BOX, webAddress);
        widgetsMap.addToLeft(RELATION_LIST_BOX, webAddressLocation);
        return widgetsMap;
    }

    private WidgetsMap getIMAddressWidgets(String name, Integer id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        TextBox imAddress = new TextBox();
        imAddress.getElement().setId("imAddress");
        if (name != null) {
            imAddress.setText(name);
        }
        imAddressLocation = new DataListBox();
        imAddressLocation.setItems(getImAddressDropDownItems());
        imAddressLocation.getElement().setId("imAddressLocation");
        if (id != null) {
            imAddressLocation.setSelected(id);
        }
        widgetsMap.addToCenter(MultiTableNewUI.TEXT_BOX, imAddress);
        widgetsMap.addToLeft(MultiTableNewUI.LIST_BOX, imAddressLocation);
        return widgetsMap;
    }

    private WidgetsMap getPhoneWidgets(String name, Integer id, String primaryPhone) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final PhoneNumber phone = new PhoneNumber("");
        if (name != null) {
            phone.setData(name);
        }
        final DataListBox phoneLocation = new DataListBox();
        phoneLocation.addStyleName("multiwidget-sub");
        phoneLocation.setWidth("105px");
        phoneLocation.getElement().setId("phoneLocation");
        phoneLocation.setWithoutNullLabel(true);
        phoneLocation.setItems(getPhoneNumber());
        if (id != null) {
            phoneLocation.setSelected(id);
        } else {
            phoneLocation.setSelected(PhoneReference.WORK.getId());
        }

        phoneLocation.addValueChangeHandler(event -> {
            if (phoneLocation.getSelectedId() != null && phoneLocation.getSelectedId().equals(PhoneReference.EXTENSION.getId())) {
                phone.onlyExternal(true);
                phone.setData("");
            } else {
                phone.onlyExternal(false);
//                phone.getField();
            }
        });
        KpiRadioButton primary = new KpiRadioButton("primaryPhone");
        new KpiToolTip(primary, wfmStrings.primary());
        primary.getElement().setId("phone_radiobutton_primary");
        primary.addStyleName("multiwidget-sub");
        if (primaryPhone != null && name != null && !"".equals(name.trim()) && !"".equals(primaryPhone.trim()) && primaryPhone.trim().equalsIgnoreCase(name.trim())) {
            primary.setValue(true);
        } else {
            if (name == null && id == null && phoneNumInf == null) {
                primary.setValue(true);
            }
        }
        widgetsMap.addToLeft(PRIMARY_RADIO_BUTTON, primary);
        widgetsMap.addToCenter(RELATION_LIST_BOX, phoneLocation);
        widgetsMap.addWidgets(phone.getPhoneFeild());
        widgetsMap.addWidgetToMap(MultiTableNewUI.PHONE_NUMBER, phone);
        if (id != null && id.equals(PhoneReference.EXTENSION.getId())) {
            phone.onlyExternal(true);
        }
        return widgetsMap;
    }

    private WidgetsMap getEmailWidgets(String name, Integer id, String primaryEmail) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox email = new TextBox();
        email.setPlaceHolder(wfmStrings.email());
        email.addStyleName("email");
        email.getElement().setId("email");
        if (name != null) {
            email.setText(name);
        } else {
            email.addStyleName("search-textbox");
        }
        DataListBox emailLocation = new DataListBox();
        emailLocation.getElement().setId("emailLocation");
        emailLocation.addStyleName("multiwidget-sub");
        emailLocation.setWidth("85px");
        emailLocation.setWithoutNullLabel(true);
        emailLocation.setItems(getAddress());
        emailLocation.setVisible(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMAIL_TYPES));
        if (id != null) {
            emailLocation.setSelected(id);
        } else {
            emailLocation.setSelected(AddressReference.WORK.getId());
        }

        KpiRadioButton primary = new KpiRadioButton("primaryEmail");
        new KpiToolTip(primary, wfmStrings.primary());
        primary.addStyleName("multiwidget-sub");
        primary.getElement().setId("email_primary");
        primary.addStyleName("email_primary");
        if (primaryEmail != null && name != null && !"".equals(name.trim()) && !"".equals(primaryEmail.trim()) && primaryEmail.trim().equalsIgnoreCase(name.trim())) {
            primary.setValue(true);
        } else {
            if (name == null && id == null && emailInf == null) {
                primary.setValue(true);
            }
        }
        widgetsMap.addToLeft(PRIMARY_RADIO_BUTTON, primary);
        widgetsMap.add(RELATION_LIST_BOX, emailLocation);
        widgetsMap.add(PARAM_TEXT_BOX, email);
        return widgetsMap;
    }

    private WidgetsMap getTelegramWidgets(SelectItem telegramItem) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final DataListBox telegram = new DataListBox();
        telegram.setItems(telegramBots);
        if (telegramItem != null) {
            telegram.setSelected(telegramItem);
        } else {
            telegram.setPlaceholder(wfmStrings.pleaseSelect());
        }
        TelegramChatSingleLookUp chatLookUp = new TelegramChatSingleLookUp();
        if (telegramItem != null) {
            chatLookUp.clear();
            chatLookUp.refreshOracle(true);
            chatLookUp.clearOracleItems();
            chatLookUp.setAccessToken(telegramItem.getDescription());
            chatLookUp.setSelected(new SelectItem(telegramItem.getEntityId(), telegramItem.getName()));
        } else {
            chatLookUp.setEnabled(false);
        }
        telegram.addValueChangeHandler(change -> {
            if (change.getValue() != null) {
                chatLookUp.setEnabled(true);
                chatLookUp.clear();
                chatLookUp.refreshOracle(true);
                chatLookUp.clearOracleItems();
                chatLookUp.setAccessToken(change.getValue().getDescription());
            }
        });
        widgetsMap.addToLeft(RELATION_LIST_BOX, telegram);
        widgetsMap.add(RELATED_CHAT, chatLookUp);
        return widgetsMap;
    }

    private WidgetsMap getAddressWidgets(Address addressData) {
        if (isAddView) {
            if (isCopying && addressData != null && addressData.getObjectID() != null) {
                addressData.setObjectID(null);
            }
            AddressWidgetAddView addressWidget = new AddressWidgetAddView(addressData, false, (objectId != null && !isCopying ? objectId.toString() : "add"), false, false);
            final WidgetsMap widgetsMap = new WidgetsMap();
            widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
            widgetsMap.add(wfmStrings.addressName(), addressWidget.name);
            widgetsMap.add(wfmStrings.addressLine1(), addressWidget.street);
            widgetsMap.add(wfmStrings.addressLine2(), addressWidget.streetB);
            widgetsMap.add(wfmStrings.city(), addressWidget.city);
            widgetsMap.add(wfmStrings.country(), addressWidget.country);
            widgetsMap.add(wfmStrings.state(), addressWidget.state);
            widgetsMap.add(wfmStrings.postCode(), addressWidget.postCode);
            widgetsMap.add("", addressWidget.primaryButton);
            return widgetsMap;
        } else {
            if (isCopying && addressData != null && addressData.getObjectID() != null) {
                addressData.setObjectID(null);
            }
            if (objectId == null && item != null && item.getCrmAccount() != null && item.getCrmAccount().getDefaultAddress(true) != null && item.getCrmAccount().getDefaultAddress(true).getCountryId() != null) {
                addressData.setCountryId(item.getCrmAccount().getDefaultAddress(true).getCountryId());
            }
            AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, true, (objectId != null && !isCopying ? objectId.toString() : "add"), false, getRequiredCodes().contains(ADDRESS), filterParametrs);
            final WidgetsMap widgetsMap = new WidgetsMap();
            widgetsMap.addWidgetToMap(MultiTableNewUI.ADDRESS, addressWidget);
            widgetsMap.addToLeft(null, addressWidget.primaryField);
            widgetsMap.addToCenter(null, addressWidget.nameField);
            widgetsMap.addToCenter(null, addressWidget.addressViewField);
            widgetsMap.addToRight(null, addressWidget.editButton);
            return widgetsMap;
        }
    }

    private void getTelegramBots() {
        TelegramChatService.App.get().getTelegramSettingsAsSelectItems(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                telegramBots = selectItems;
                for (Map<String, Widget> emailRow : telegramInf.getWidgets()) {
                    DataListBox value = (DataListBox) emailRow.get(RELATION_LIST_BOX);
                    value.setItems(selectItems);
                    break;
                }
            }
        });
    }

    private void reinitCrmAccount(final Integer crmAccountID) {
        disableAccountFields(true);
        if (crmAccountID != null || emptyCrmAccount == null) {
            LoadingPanel.loading(true);
            ClientService.App.get().editAccount(crmAccountID, null, new AbstractAsyncCallback<CrmAccountItem>() {
                @Override
                public void failure(Throwable throwable) {
                    assets = null;
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(CrmAccountItem crmAccountItem) {
                    LoadingPanel.loading(false);
                    if (item != null) {
                        if (item.getCrmAccount() == null || (crmAccountItem != null && crmAccountItem.getObjectId() != null && !crmAccountItem.getObjectId().equals(item.getCrmAccount().getObjectId()))) {
                            setReportsToList(crmAccountItem.getObjectId());
                        }
                        item.setCrmAccount(crmAccountItem);
                    }
                    fillAccountFields();
                    parentItem = crmAccountItem;
                    parentAddressInf.removeAllRows();
                    copyAccountAddress.setValue(false);
                    copyAccountAddress.setEnabled(true);
                    if (crmAccountID == null && emptyCrmAccount == null) {
                        emptyCrmAccount = crmAccountItem.clone();
                        copyAccountAddress.setEnabled(false);
                    }
                }
            });
        } else {
            if (item != null) {
                item.setCrmAccount(emptyCrmAccount.clone());
                fillAccountFields();
            }
            reportsTo.clear();
            reportsTo.setItems(new SelectItem[]{});
        }
    }

    private void disableAccountFields(boolean disable) {
        industries.setEnabled(!disable);
        accountType.setEnabled(!disable);
    }

    private void fillAccountFields() {
        industries.clearSelected();
        accountType.clearSelected();
        industries.setSelected(item.getCrmAccount().getIndustryID());
        accountType.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, item.getCrmAccount().getAccountTypes()), true);
        disableAccountFields(false);
        if (objectId == null && item.getCrmAccount().getDefaultAddress(true) != null && item.getCrmAccount().getDefaultAddress(true).getCountryId() != null) {
            changeContactCountry();
        }
    }

    private void changeContactCountry() {
        if (isAddView) {
            if (addressInf.getColumnsWidget().size() == 1 && (addressInf.getColumnsWidget().get(0).values()) != null) {
                for (Map<String, Widget> addressRow : addressInf.getColumnsWidget()) {

                    if (addressRow.get(ADDRESS) instanceof AddressWidgetAddView) {
                        AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                        if (addressWidget != null) {
                            addressWidget.getCountry().setSelected(item.getCrmAccount().getDefaultAddress(true).getCountryId());
                        }
                    }
                }
            }
        } else {
            if (addressInf.getWidgets().size() == 1 && (addressInf.getWidgets().get(0).values()) != null) {
                for (Map<String, Widget> addressRow : addressInf.getWidgets()) {

                    if (addressRow.get(ADDRESS) instanceof AddressWidget) {
                        AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                        if (addressWidget != null) {
                            addressWidget.getCountry().setSelected(item.getCrmAccount().getDefaultAddress(true).getCountryId());
                        }
                    }
                }
            }
        }

    }

    protected void setPluginParams(ContactListItem contactItem) {
        if (pluginParams != null && !pluginParams.isEmpty()) {
            contactItem.setLastName(pluginParams.get("lastName"));
            contactItem.setFirstName(pluginParams.get("firstName"));
            contactItem.setPrimaryEmail(pluginParams.get("email"));
            contactItem.getWorkEmail().add(pluginParams.get("email"));
        }
    }

    public static TreeSelectItem[] removeSystemsCategories(final TreeSelectItem[] categories) {
        return categories;
    }

    public void setContactItem() {
        initPredefinedValues();
        if (item != null && item.getObjectId() != null) {
            Utils.registrRelation(item);
        }
        // Personal Infomation
        relationsDropDownItems = item.getRelationships();
        categoriesDropDownsItems = item.getCategories();
        imAddressDropDownItems = item.getContactImAddress();
        firstName.setText(item.getFirstName());
        middleName.setText(item.getMiddleName());
        lastName.setText(item.getLastName());
        otherName.setText(item.getOtherName());
        backgrounInformation.setText(item.getBackgroundInformation());
        titl.setItems(item.getCrmAccount().getTitle());
        if (item.getTitleId() != null) {
            titl.setSelected(item.getTitleId());
            if (isOtherSelected(titl)) {
                title.setVisible(true);
                title.setText(item.getTitle());
            }
        } else if (item.getTitle() != null) {
            titl.setSelectedByValue(item.getTitle());
        }
        if (item.getBirthDate() != null) {
            birthDOBWidget.setSelected(item.getBirthDate().getNonConvertedDate().getDate(), item.getBirthDate().getNonConvertedDate().getMonth(), item.getBirthDate().getNonConvertedDate().getYear());
        } else {
            birthDOBWidget.setSelected(0);
        }
        // Company information
        industries.setItems(item.getCrmAccount().getIndustries());
        contactOwner.setItems(item.getLeadAssignees());
        if (item.getCrmAccount().getObjectId() != null) {
            companyName.addItem(new SelectItem(item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
            companyName.setSelected(item.getCrmAccount().asSelectItem());
            copyAccountAddress.setEnabled(true);
            if (item.getShowAccountAddress()) {
                getParentAccoundAddress();
                copyAccountAddress.setValue(true);
            }
        }
        fillAccountFields();
        jobTitle.setText(item.getJobTitle());
        jobFunction.setText(item.getJobFunction());
        refIndNumber.setText(item.getRefIndNumber());
        department.setText(item.getDepartment());
        //Contact Information
        if (item.isEmployeeContact()) {
            //email.title... red qilish kerak...
        }
        setEmailDetailsWidgets();
        setPhoneNumbersWidgets();
        if (objectId != null && defaultPhone != null) {
            phoneNumInf.addWidgets(getPhoneWidgets(defaultPhone, null, defaultPhone));
        }
        setImAddressWidgets();
        setWebSiteWidgets();
        setAddressWidgets();
        //Additional Details
        setCategoryWidgets();
        setRelationWidgets();
        setTelegramWidgets();
        if (item.getSupervisors() != null && item.getSupervisors().length > 0) {
            reportsTo.setItems(item.getSupervisors());
        } else {
            reportsTo.setItems(new SelectItem[]{});
        }
        if (item.getReportsToId() != null) {
            reportsTo.setSelected(item.getReportsToId());
        } else {
            reportsTo.setSelectedNullLabel();
        }
        //Crm Details
        if (item.getOwnerId() != null) {
            contactOwner.setSelected(item.getOwnerId());
        }

        if (item.getCampaignId() != null) {
            campaignSource.setSelected(item.getCampaignId(), item.getCampaign());
        } else if (campaignID != null && campaignName != null) {
            campaignSource.setSelected(campaignID, campaignName);
        }
        emailOpt.setValue(item.isEmailOptOut());
        setItemTableValues(item.getCustomTableItems());
        if (productTable != null) {
            setValues(item.getItems());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());
    }

    protected void setValues(OpportunityItem[] items) {
        if (items != null && items.length > 0) {
            hasProduct = true;
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

    private void setReportsToList(Integer companyID) {
        LoadingPanel.loading(true);
        CRMService.App.get().getContactsByAccount(companyID, item != null ? item.getObjectId() : null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onSuccess(SelectItem[] result) {
                LoadingPanel.loading(false);
                if (result != null && result.length > 0) {
                    reportsTo.clear();
                    reportsTo.setItems(result);
                } else {
                    reportsTo.clear();
                    reportsTo.setItems(new SelectItem[]{});
                }
                reportsTo.setSelectedNullLabel();
            }
        });
    }

    private void setCategoryWidgets() {
        if (item.getSelectedCategories() != null && item.getSelectedCategories().size() > 0) {
            HashMap<Integer, ContactCategoryListItem> map = ContactCategoryListItem.asMap(item.getContactCategoryListItem());
            for (SelectItem treeSelectItem : item.getSelectedCategories()) {
                if (map.containsKey(treeSelectItem.getId())) {
                    map.get(treeSelectItem.getId()).setSelected(true);
                }
            }
        }
        recursionCategoryAdd(item.getContactCategoryListItem(), null);
    }

    private void setRelationWidgets() {
        if (item.getSelectedRelationships() != null && item.getSelectedRelationships().size() > 0) {
            relations.clear();
            for (SelectItem selectedRelation : item.getSelectedRelationships()) {
                relations.addWidgets(getRelationWidgets(selectedRelation));
            }
        } else {
            relations.initWidgets(getRelationWidgets(null));
        }
    }

    private void setTelegramWidgets() {
        if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
            telegramInf.clear();
            for (SelectItem item : item.getTelegramChats()) {
                telegramInf.addWidgets(getTelegramWidgets(item));
            }
        }
    }

    private void setAddressWidgets() {
        addressInf.clear();
        parentAddressInf.clear();
        if (!isAddView && item.getAddresses() != null && item.getAddresses().size() > 0) {
            item.getAddresses().sort((o1, o2) -> Boolean.compare(o2.isPrimary(), o1.isPrimary()));
            for (int i = 0; i < item.getAddresses().size(); i++) {
                if (!item.getAddresses().get(i).isLinkedAddress()) {
                    addressInf.addWidgets(getAddressWidgets(item.getAddresses().get(i)));
                } else {
                    parentAddressesMap.put(item.getAddresses().get(i).getLinkedAddressID(), item.getAddresses().get(i));
                }
            }
            if (addressInf.getWidgets().size() == 0 && copyAccountAddress != null && !copyAccountAddress.getValue()) {
                if (address != null) {
                    address.setPrimary(true);
                }
                addressInf.addWidgets(getAddressWidgets(address));
            }
            address = item.getAddresses().get(0);
            if (address != null) {
                address.setObjectID(null);
            }
        } else {
            if (address == null) {
                Address address1 = new Address();
                address1.setPrimary(true);
                if (isAddView) {
                    addressInf.addColumnsWidget(getAddressWidgets(address1));
                } else {
                    addressInf.addWidgets(getAddressWidgets(address1));
                }
            } else {
                if (isAddView) {
                    addressInf.addColumnsWidget(getAddressWidgets(address));
                } else {
                    addressInf.addWidgets(getAddressWidgets(address));
                }
            }
        }
    }

    private void setWebSiteWidgets() {
        if (!item.isItemParamsEmpty(Constants.CONTACT_WEBSITES)) {
            webSiteInf.clear();
        }
        setMultiTableItems(Constants.CONTACT_WEBSITES);
    }

    private void setImAddressWidgets() {
        if (item.getSelectedContactImAddress() != null && item.getSelectedContactImAddress().size() > 0) {
            imsAddressInf.clear();
            for (SelectItem selectedImAddress : item.getSelectedContactImAddress()) {
                imsAddressInf.addWidgets(getIMAddressWidgets(selectedImAddress));
            }
        } else {
            imsAddressInf.initWidgets(getIMAddressWidgets(null));
        }
    }

    private void setPhoneNumbersWidgets() {
        if (!item.isItemParamsEmpty(Constants.CONTACT_PHONES)) {
            phoneNumInf.clear();
        }
        setMultiTableItems(Constants.CONTACT_PHONES);
    }

    private void setEmailDetailsWidgets() {
        if (!item.isItemParamsEmpty(Constants.CONTACT_EMAILS)) {
            emailInf.clear();
        }
        setMultiTableItems(Constants.CONTACT_EMAILS);
    }

    private void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(item, param);
        if (itemParamsAsMap == null || itemParamsAsMap.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
            int relation = entry.getKey();
            for (String value : entry.getValue()) {
                if (value == null || "".equals(value.trim())) {
                    continue;
                }
                switch (param) {
                    case Constants.CONTACT_EMAILS:
                        emailInf.addWidgets(getEmailWidgets(value, relation, item.getPrimaryEmail()));
                        break;
                    case Constants.CONTACT_PHONES:
                        phoneNumInf.addWidgets(getPhoneWidgets(value, relation, item.getPrimaryPhone()));
                        break;
                    case Constants.CONTACT_WEBSITES:
                        webSiteInf.addWidgets(getWebSiteWidgets(value, relation));
                        break;
                }
            }
        }
    }

    protected void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        saveContact();
    }

    public void setValues() {
        if (item == null) {
            item = new ContactListItem();
        }
        if (Utils.isWebForm()) {
            item.setWebFormID(Utils.getWebFormID());
            item.setAntibot(Utils.getAntibot());
        }
        if (objectId != null) {
            item.setObjectId(objectId);
        } else {
            item.setContactType(contactType);
        }
        if (isCopying) {
            item.setObjectId(null);
        }
        item.setNotes(noteWidget.getNewNotesToSave());

        // Personall Information
        item.setFirstName(firstName.getText());
        item.setMiddleName(middleName.getText());
        item.setLastName(lastName.getText());
        item.setOtherName(otherName.getText());
        if (titl.isSomethingSelected()) {
            item.setTitleId(titl.getSelectedItem().getId());
            if (wfmStrings.other().equalsIgnoreCase(titl.getSelectedItem().getName())) {
                if (title.getText() != null) {
                    item.setTitle(title.getText());
                }
            }
        } else {
            item.setTitleId(null);
        }
        if (birthDOBWidget.getConvertableDOBDate() != null) {
            item.setBirthDate(birthDOBWidget.getConvertableDOBDate());
        }

        // Comany Information
        item.getCrmAccount().setObjectId(null);
        if (companyName.getSelectedItem() != null) {
            CrmAccountItem account = new CrmAccountItem();
            account.setObjectId(companyName.getSelectedItem().getId());
            account.setName(companyName.getSelectedItem().getName());
            item.setCrmAccount(account);
        } else {
            if (companyName.getText() != null && !"".equals(companyName.getText().trim()) && !LookUp.wfmStrings.searchTypeMessage().equals(companyName.getText().trim())) {
                item.setCrmAccount(new CrmAccountItem());

                item.getCrmAccount().setName(companyName.getText().trim());
                if (isLead()) {
                    SelectItem[] owners = new SelectItem[1];
                    SelectItem selectItem = new SelectItem();
                    selectItem.setId(contactOwner.getSelectedId());
                    selectItem.setName(contactOwner.getSelectedItem().getName());
                    selectItem.setSelected(true);
                    owners[0] = selectItem;
                    item.getCrmAccount().setOwnerItems(owners);
                }
            } else {
                item.setCrmAccount(null);
            }
        }
        item.setRefIndNumber(refIndNumber.getText());
        item.setDepartment(department.getText());
        item.setJobTitle(jobTitle.getText());
        item.setJobFunction(jobFunction.getText());
        if (assets != null && !"".equals(assets.getText())) {
            item.setAssets(assets.getText());
        } else {
            item.setAssets("");
        }
        if (industries.isSomethingSelected() && !isOtherSelected(industries)) {
            item.getCrmAccount().setIndustryID(industries.getSelectedItem().getId());
            item.getCrmAccount().setIndustry(industries.getSelectedItem().getName());
        } else {
            if (isOtherSelected(industries)) {
                item.getCrmAccount().setIndustryID(industries.getSelectedItem().getId());
            } else {
                item.getCrmAccount().setIndustryID(null);
                item.getCrmAccount().setIndustry("");
            }
        }


        item.getCrmAccount().setAccountTypes(accountType.getValuesMap().keySet().toArray(new SelectItem[]{}));
        // Cotanct Information
        setEmail();
        setPhoneNumber();
        setImAddress();
        setWebAdrres();
        setAddressData();
        setTelegramBots();
        if (contactType != ContactListItem.LEAD_CONTACT && contactType != ContactListItem.CANDIDATE) {
            setContactCategories();
        }
        setRelationships();
        //Additional Details
        if (reportsTo.getSelectedId() != null) {
            item.setReportsTo(reportsTo.getSelectedItem().getName());
            item.setReportsToId(reportsTo.getSelectedItem().getId());
        } else {
            item.setReportsTo(null);
            item.setReportsToId(null);
        }

        // Crm Details
        if (contactOwner.getSelectedId() != null) {
            item.setOwner(contactOwner.getSelectedItem(true).getName());
            item.setOwnerId(contactOwner.getSelectedId(true));
        }

        item.setCampaignId(null);
        if (campaignSource.getSelectedItem() != null) {
            item.setCampaignId(campaignSource.getSelectedItemID());
            item.setCampaign(campaignSource.getSelectedItem().getName());
        }
        item.setEmailOptOut(emailOpt.getValue());

        if (uploadForm != null) {
            item.setAttachments(uploadForm.getAttachedFiles());
        }
        if (uploadFormMini != null && Utils.isWebForm()) {
            item.setAttachments(uploadFormMini.getAttachedFiles());
        }
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        item.setShowAccountAddress(copyAccountAddress.getValue() != null && copyAccountAddress.getValue());
    }

    public OpportunityItem[] getLeadItemsData() {
        ArrayList<OpportunityItem> opportunityItems = new ArrayList<>();
        for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
            if (productTable.isItemValid(i)) {
                SmartProductLookUp productLookUp = null;
                OpportunityItem result = new OpportunityItem();
                Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();

                productLookUp = (SmartProductLookUp) productTable.getColumnById(i, ItemTableConstants.PRODUCT);
                if (productLookUp.getSelectedItemID() != null && productLookUp.getSelectedItem().getName().equals(productLookUp.getSuggestBox().getText())) {
                    result.setItemID(productLookUp.getSelectedItemID());
                    if (productLookUp.getSelectedItem().getName().contains("->")) {
                        result.setItemNumber(productLookUp.getSelectedItem().getName().split("->")[0].trim());
                        result.setItemName(productLookUp.getSelectedItem().getName().split("->")[1]);
                    } else {
                        result.setItemName(productLookUp.getSelectedItem().getName());
                    }
                } else if (productLookUp.getSuggestBox() != null && productLookUp.getSuggestBox().getText() != null) {
                    result.setItemName(productLookUp.getSuggestBox().getText());
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

                CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) productTable.getColumnById(i, ItemTableConstants.SUPPLIER);
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

    protected void saveContact() {
        setValues();
        ArrayList<Integer> mailLists = new ArrayList<>();
        if (mailListTable != null) {
            mailLists = mailListTable.getSelectedIdsList();
        }
        item.setCheckForDuplicates(!forceToSave);
        LoadingPanel.loading(true);
        ContactService.App.get().saveContact(item, mailLists, new AbstractAsyncCallback<SelectItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                enableButton(true);
                com.edatasite.workforce.gwt.core.client.ui.LoadingPanel.loading(false);
            }

            public void success(SelectItem selectItem) {
                LoadingPanel.loading(false);

                enableButton(true);
                if (selectItem != null) {
                    if (Utils.isWebForm()) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.WEB_FORM_SAVED, selectItem.getId(), AddContactView.this);
                    }
                    if (selectItem.getId() != null && selectItem.getId() > 0) {
                        if (selectItem.getEntityId() != null) {
                            CrmAccountItem crmAccountItem = new CrmAccountItem();
                            crmAccountItem.setObjectId(selectItem.getEntityId());
                            crmAccountItem.setName(selectItem.getDescription());
                            item.setCrmAccount(crmAccountItem);
                        }
                        if (item.getWebFormID() == null || item.getObjectId() != null) {
                            Info.show(successMessage, Info.Type.INFO);
                        }
                        if (item.getObjectId() == null) {
                            item.setObjectId(selectItem.getId());
                        }
                        if (item.isLeadContact()) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_ADD_EDIT, item, AddContactView.this);
                        }
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_ADD, item, AddContactView.this);
                        if (item.getCrmAccount().getName() != null && !"".equals(item.getCrmAccount().getName())) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, item, AddContactView.this);
                        }
                        if (saveAndAddOpp) {
                            saveAndAddOpp = false;
                            Integer crmAccountId = null;
                            String crmAccountName = null;
                            if (item.getCrmAccount() != null) {
                                crmAccountId = item.getCrmAccount().getObjectId();
                                crmAccountName = item.getCrmAccount().getName();
                            }

                            if (Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES)) {
                                closeTab();
                                new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getName()),
                                        RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null, item.getCrmAccount() != null ? item.getCrmAccount().getName() : null));

                            } else if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
                                closeTab();
                                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + crmAccountId + "/" + crmAccountName + "/" + RelationItem.TYPE_CONTACT + "/" + selectItem.getId() + "/" + item.getName(), item.getName(), item.getName());
                            }
                        } else {
                            onShellOk();
                        }
                    } else if (selectItem.getId() != null && !Constants.ANTIBOT_ERROR.equals(selectItem.getId())) {
                        showDuplicatePopup(selectItem.getId() == -1);
                    }
                    if (!isPopup && selectItem.getId() != null && selectItem.getId() > 0) {
                        if (fromOpportunity || fromCompany) {
                            closeTab();
                        } else if (Utils.isAccounting() && saveAndClose) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + selectItem.getId(), item.getName(), item.getName());
                        } else if (accountID != null && saveAndClose) {
                            if (item.getCrmAccount() != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + accountID, item.getCrmAccount().getNumber(), item.getCrmAccount().getName());
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + accountID);
                            }
                        }
                    }
                }
            }
        });
    }

    protected void showDuplicatePopup(final boolean nameIsDuplicated) {
        String fullName = firstName.getText() + " " + lastName.getText();
        IconEnum iconEnum = nameIsDuplicated ? IconEnum.QUESTION : IconEnum.WARN;
        Action action = nameIsDuplicated ? Action.YesNo : Action.OK;
        String message = wfmMessages.duplicateDetectedMessage(isLead() ? Property.get(Constants.LEADS, wfmStrings.lead()) : isCandidate() ? wfmStrings.candidate() : property.getSingular(wfmStrings.contact()), nameIsDuplicated ? fullName : item.getPrimaryEmail());
        String buttonText = nameIsDuplicated ? wfmStrings.continueAnyway() : wfmStrings.ok();
        WfmMessageBox messageBox = new WfmMessageBox(iconEnum, action, message, buttonText, wfmStrings.cancel(), new CloseHandler() {
            @Override
            public void onCancel() {
                if (!nameIsDuplicated) {
                    markAsError(emailInf, true);
                }
                enableButton(true);
            }

            @Override
            public void onSubmit() {
                if (!nameIsDuplicated) {
                    markAsError(emailInf, true);
                    enableButton(true);
                } else {
                    forceToSave = true;
                    saveContact();
                }
            }
        });
        messageBox.open();
    }

    public void setContactCategories() {
        item.setSelectedCategories(null);
        if (tree.getSelectedIDs(false) != null) {
            tree.getSelectedIDs(false);
            for (SelectItem selecteds : tree.getSelectedIDs(false)) {
                item.addSelectedCategories(selecteds);
            }
        }
    }

    public void setRelationships() {
        item.setSelectedRelationships((ArrayList<SelectItem>) null);
        if (!relations.isEmpty()) {
            for (Map<String, Widget> widgets : relations.getWidgets()) {
                TextBox relationTextBox = (TextBox) widgets.get(MultiTableNewUI.TEXT_BOX);
                DataListBox relationListBox = (DataListBox) widgets.get(MultiTableNewUI.LIST_BOX);
                if (relationListBox.getSelectedItem() != null && relationTextBox.getText() != null && !"".equals(relationTextBox.getText().trim())) {
                    SelectItem selectItem = new SelectItem(relationListBox.getSelectedId(), relationListBox.getSelectedItem().getName(), relationTextBox.getText());
                    item.addSelectedRelationships(selectItem);
                }
            }
        }
    }

    public void setAddressData() {
        item.setAddresses(new ArrayList<>());
        if (isAddView) {
            for (Map<String, Widget> addressRow : addressInf.getColumnsWidget()) {
                AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                if (addressWidget.isNotEmpty()) {
                    item.getAddresses().add(addressWidget.getAddress());
                }
            }
            for (Map<String, Widget> addressRow : parentAddressInf.getColumnsWidget()) {
                AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                if (addressWidget != null && addressWidget.isNotEmpty()) {
                    item.getAddresses().add(addressWidget.getAddress());
                }
            }
        } else {
            for (Map<String, Widget> addressRow : addressInf.getWidgets()) {
                AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                if (addressWidget.isNotEmpty()) {
                    item.getAddresses().add(addressWidget.getAddress());
                }
            }
            for (Map<String, Widget> addressRow : parentAddressInf.getWidgets()) {
                AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                if (addressWidget != null && addressWidget.isNotEmpty()) {
                    item.getAddresses().add(addressWidget.getAddress());
                }
            }
        }

    }

    public void setWebAdrres() {
        item.setWebAddresses();
        for (Map<String, Widget> webSiteRow : webSiteInf.getWidgets()) {
            TextBox webAddress = (TextBox) webSiteRow.get(PARAM_TEXT_BOX);
            if (isEmpty(webAddress.getText())) {
                continue;
            }
            DataListBox webAddressType = (DataListBox) webSiteRow.get(RELATION_LIST_BOX);
            item.addParam(Constants.CONTACT_WEBSITES, webAddressType.getSelectedId(), webAddress.getText());
        }
    }

    public void setImAddress() {
        item.setSelectedContactImAddress((ArrayList<SelectItem>) null);
        if (!imsAddressInf.isEmpty()) {
            for (Map<String, Widget> widgets : imsAddressInf.getWidgets()) {
                TextBox imAddressTextBox = (TextBox) widgets.get(MultiTableNewUI.TEXT_BOX);
                DataListBox imAddressListBox = (DataListBox) widgets.get(MultiTableNewUI.LIST_BOX);
                if (imAddressListBox.getSelectedItem() != null && imAddressTextBox.getText() != null && !"".equals(imAddressTextBox.getText().trim())) {
                    SelectItem selectItem = new SelectItem(imAddressListBox.getSelectedId(), imAddressListBox.getSelectedItem().getName(), imAddressTextBox.getText());
                    item.addSelectedImAddresses(selectItem);
                }
            }
        }
    }

    public void setPhoneNumber() {
        item.setPhones();
        for (Map<String, Widget> widgetsMap : phoneNumInf.getWidgets()) {
            PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTableNewUI.PHONE_NUMBER);
            DataListBox phoneType = (DataListBox) widgetsMap.get(RELATION_LIST_BOX);
            phoneType.addStyleName("multiwidget-sub");
            KpiRadioButton primary = (KpiRadioButton) widgetsMap.get(PRIMARY_RADIO_BUTTON);
            primary.addStyleName("multiwidget-sub");
            boolean isPrimary = primary.getValue();
            if ("".equals(phoneNumber.toString()) || item.getAllPhones().contains(phoneNumber.toString())) {
                continue;
            }
            item.addParam(Constants.CONTACT_PHONES, phoneType.getSelectedId(), phoneNumber.toString());
            if (isPrimary) {
                item.setPrimaryPhone(phoneNumber.toString());
            }
            if (item.getCrmAccount().isNew()) {
                if (Constants.G_WORK == phoneType.getSelectedId(true)) {
                    item.getCrmAccount().setPhone(phoneNumber.toString());
                }
                if (Constants.G_FAX == phoneType.getSelectedId(true)) {
                    item.getCrmAccount().setFax(phoneNumber.toString());
                }
            }
        }
    }

    private void setTelegramBots() {
        ArrayList<SelectItem> telegramChats = new ArrayList<>();
        for (Map<String, Widget> widgetsMap : telegramInf.getWidgets()) {
            TelegramChatSingleLookUp chatId = (TelegramChatSingleLookUp) widgetsMap.get(RELATED_CHAT);
            DataListBox telegramBot = (DataListBox) widgetsMap.get(RELATION_LIST_BOX);
            if (telegramBot.getSelectedItem() != null && chatId.getSelectedItem() != null) {
                telegramChats.add(new SelectItem(telegramBot.getSelectedId(), chatId.getSelectedItemID(), null, null));
            }
        }
        item.setTelegramChats(telegramChats);
    }

    public void setDefaultPhoneNumber(String phone) {
        this.defaultPhone = phone;
    }

    public void setEmail() {
        item.setEmails();
        for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
            TextBox value = (TextBox) emailRow.get(PARAM_TEXT_BOX);
            if (!isEmpty(value.getText()) && !wfmStrings.email().equals(value.getText()) && Utils.validateEmail(value.getText(), false)) {
                DataListBox emailType = (DataListBox) emailRow.get(RELATION_LIST_BOX);
                KpiRadioButton primary = (KpiRadioButton) emailRow.get(PRIMARY_RADIO_BUTTON);
                primary.addStyleName("multiwidget-sub");
                item.addParam(Constants.CONTACT_EMAILS, emailType.getSelectedId(), value.getText());
                if (primary.getValue() != null && primary.getValue()) {
                    item.setPrimaryEmail(value.getText());
                }
                if (Constants.G_WORK == emailType.getSelectedId(true) && item.getCrmAccount().isNew()) {
                    item.getCrmAccount().setEmail(value.getText());
                }
            }
        }
    }

    public void fillAddressWithParentAddress(Address[] addresses, boolean isAccountAddress) {
        if (!isAccountAddress) {
            addressInf.removeAllRows();
        }
        parentAddressInf.removeAllRows();
        Arrays.sort(addresses, (o1, o2) -> Boolean.compare(o2.isPrimary(), o1.isPrimary()));
        if (addresses != null) {
            for (Address address : addresses) {
                address.setRelationType(Constants.G_WORK);
                if (isAccountAddress) {
                    address.setLinkedAddressID(address.getLinkedAddressID() != null ? address.getLinkedAddressID() : address.getObjectID());
                    address.setLinkedAddress(true);
                    address.setObjectID(null);
                    if (isAddView) {
                        parentAddressInf.addColumnsWidget(getAddressWidgets(address));
                    } else {
                        parentAddressInf.addWidgets(getAddressWidgets(address));
                    }
                }
            }
        }
        if (isAddView) {
            if (addressInf.getColumnsWidget().size() == 0 && parentAddressInf.getColumnsWidget().size() == 0) {
                if (address != null) {
                    address.setPrimary(true);
                }
                addressInf.addColumnsWidget(getAddressWidgets(address));
            }
            if (!isAccountAddress) {
                addressInf.addColumnsWidget(getAddressWidgets(new Address()));
            }
        } else {
            if (addressInf.getWidgets().size() == 0 && parentAddressInf.getWidgets().size() == 0) {
                if (address != null) {
                    address.setPrimary(true);
                }
                addressInf.addWidgets(getAddressWidgets(address));
            }
            if (!isAccountAddress) {
                addressInf.addWidgets(getAddressWidgets(new Address()));
            }
        }
    }

    private Address[] getAccountAllAdresses(Address[] billingAddresses, Address[] shippingAddresses) {
        int length = (billingAddresses != null ? billingAddresses.length : 0) + (shippingAddresses != null ? shippingAddresses.length : 0);
        int i = 0;
        Address[] accountAddresses = new Address[length];
        for (Address address : billingAddresses) {
            if (address.isPrimary()) {
                if (parentAddressesMap.get(address.getObjectID()) != null && !parentAddressesMap.get(address.getObjectID()).isPrimary()) {
                    address.setPrimary(false);
                }
            }
            accountAddresses[i] = address;
            i++;
        }
        for (Address address : shippingAddresses) {
            if (address.isPrimary()) {
                if (parentAddressesMap.get(address.getObjectID()) != null && !parentAddressesMap.get(address.getObjectID()).isPrimary()) {
                    address.setPrimary(false);
                }
            }
            accountAddresses[i] = address;
            i++;
        }
        return accountAddresses;
    }

    private void getParentAccoundAddress() {
        if (parentItem != null) {
            fillAddressWithParentAddress(getAccountAllAdresses(parentItem.getBillAddresses(), parentItem.getMailAddresses()), true);
        } else if (parentItem == null || companyName.getSelectedItemID() != null) {
            LoadingPanel.loading(true);
            ClientService.App.get().editAccount(companyName.getSelectedItemID(), null, new AbstractAsyncCallback<CrmAccountItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(final CrmAccountItem crmAccound) {
                    LoadingPanel.loading(false);
                    Scheduler.get().scheduleDeferred(() -> {
                        parentItem = crmAccound;
                        fillAddressWithParentAddress(getAccountAllAdresses(parentItem.getBillAddresses(), parentItem.getMailAddresses()), true);
                    });
                }
            });
        }
    }

    protected boolean validate() {
        clearErrorStyle();
        errors = customValidate();
        if (!validateEmails()) {
            errors++;
        }
        errors += Utils.validateCaptcha();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()) {
                errors += markAsError(LAST_NAME, lastName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), lastName, formPropertyMap.get(CustomFormConstants.LAST_NAME).getMinChar()));
            }
        } else {
            errors += markAsError(LAST_NAME, lastName, lastName.getText() == null || "".equals(lastName.getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            if (formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()) {
                errors += markAsError(FIRST_NAME, firstName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), firstName, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getMinChar()));
            }
        } else {
            errors += markAsError(FIRST_NAME, firstName, firstName.getText() == null || "".equals(firstName.getText()));
        }

        if (contactType != ContactListItem.LEAD_CONTACT && contactType != ContactListItem.CANDIDATE) {
            if (categories != null) {
                DataListBox categoryListBox;
                boolean ident = false;
                for (Map<String, Widget> categoryRow : categories.getWidgets()) {
                    categoryListBox = (DataListBox) categoryRow.get(MultiTable.LIST_BOX);
                    if (categoryListBox.getSelectedId() != null) {
                        ident = true;
                        break;
                    } else {
                        markAsError(categoryListBox, true);
                    }
                }
                if (!ident) {
                    errors++;
                }
            }
        }
        if (relations != null) {
            TextBox relationTextBox;
            DataListBox relationListBox;
            int j = 0;
            for (Map<String, Widget> relationRow : relations.getWidgets()) {
                relationTextBox = (TextBox) relationRow.get(MultiTableNewUI.TEXT_BOX);
                relationListBox = (DataListBox) relationRow.get(MultiTableNewUI.LIST_BOX);
                if (!"".equals(relationTextBox.getText())) {
                    if (!Validation.validateListBoxRequired(relationListBox, new HTML(), "")) {
                        j++;
                    }
                }
            }
            if (j > 0) {
                errors++;
            }
        }
        if (showRequired && item.isLeadContact()) {
            errors += markAsError(companyName, !Validation.validateTextBoxRequired(companyName.getTextBox()) || LookUp.wfmStrings.searchTypeMessage().equals(companyName.getTextBox().getText()));
        }

        if (FOR_SEND_INVOICE_QUOTE.equals(actionString) || (item != null && item.isLeadContact() && showRequired && isCandidate())) {
            boolean ident = false;
            TextBox email;
            for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
                email = (TextBox) emailRow.get(PARAM_TEXT_BOX);
                if (Validation.validateTextBoxRequired(email) && Validation.validEmailFormat(email.getText(), false)) {
                    ident = true;
                    break;
                } else {
                    errors += markAsError(email, true);
                }
            }
            if (!ident) {
                errors++;
            }
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MIDDLE_NAME) != null && formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.MIDDLE_NAME, middleName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName(), middleName, formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OTHER_NAME) != null && formPropertyMap.get(CustomFormConstants.OTHER_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.OTHER_NAME, otherName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.OTHER_NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.OTHER_NAME).getTitle() : wfmStrings.otherName(), otherName, formPropertyMap.get(CustomFormConstants.OTHER_NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY) != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isRequired()) {
            errors += markAsError(BIRTH_DAY, birthDOBWidget, birthDOBWidget.box_validate(true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()) {
            errors += markAsError(companyName, !Validation.validateTextBoxRequired(companyName.getTextBox()) || LookUp.wfmStrings.searchTypeMessage().equals(companyName.getTextBox().getText()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_ACCOUNT_TYPE, accountType, accountType.getValuesMap().keySet().toArray(new SelectItem[]{}) == null || accountType.getValuesMap().keySet().toArray(new SelectItem[]{}) != null && accountType.getValuesMap().keySet().toArray(new SelectItem[]{}).length == 0);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.JOB_TITLE, jobTitle, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), jobTitle, formPropertyMap.get(CustomFormConstants.JOB_TITLE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()) {
            errors += markAsError(CustomFormConstants.DEPARTMENT, department, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department(), department, formPropertyMap.get(CustomFormConstants.DEPARTMENT).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER) != null && formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.REF_IND_NUMBER, refIndNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).getTitle() : wfmStrings.refIndNumeber(), refIndNumber, formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSETS) != null && formPropertyMap.get(CustomFormConstants.ASSETS).isRequired()) {
            errors += markAsError(CustomFormConstants.ASSETS, assets, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.ASSETS).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.ASSETS).getTitle() : wfmStrings.assets(), assets, formPropertyMap.get(CustomFormConstants.ASSETS).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_ACCOUNT_INDUSTRY, industries, industries.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null && formPropertyMap.get(CustomFormConstants.EMAIL).isRequired()) {

            if (emailInf.getWidgets() != null && emailInf.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) emailInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.EMAIL, emailInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ?
                            formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), value, formPropertyMap.get(CustomFormConstants.EMAIL).getMinChar()));
                }
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null && formPropertyMap.get(CustomFormConstants.PHONE).isRequired()) {

            if (phoneNumInf.getWidgets() != null && phoneNumInf.getWidgets().get(0).size() > 0) {
                PhoneNumber phoneNumber = (PhoneNumber) phoneNumInf.getWidgets().get(0).get(MultiTableNewUI.PHONE_NUMBER);
                if (phoneNumber != null) {
                    errors += markAsError(CustomFormConstants.PHONE, phoneNumInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(), phoneNumber.getPhoneFeild(), formPropertyMap.get(CustomFormConstants.PHONE).getMinChar()));
                }
            }
        }

        if (telegramInf.getWidgets() != null && telegramInf.getWidgets().size() > 0) {
            List<Integer> botIds = new ArrayList<>();
            for (int i = 0; i < telegramInf.getWidgets().size(); i++) {
                HashMap<String, Widget> widgets = telegramInf.getWidgets().get(i);
                DataListBox telegram = (DataListBox) widgets.get(RELATION_LIST_BOX);
                TelegramChatSingleLookUp chatLookUp = (TelegramChatSingleLookUp) widgets.get(RELATED_CHAT);
                botIds.add(telegram.getSelectedId());
                if (telegram != null && telegram.getSelectedId() != null && telegram.getSelectedId() > 0 && chatLookUp.getSelectedItemID() == null) {
                    errors += markAsError(CustomFormConstants.TELEGRAM, telegramInf, true);
                }
            }
            if (botIds.size() > 1) {
                boolean sameFound = false;
                for (int i = 0; i < botIds.size(); i++) {
                    for (int j = 0; j < botIds.size(); j++) {
                        if (i != j && botIds.get(i).equals(botIds.get(j))) {
                            sameFound = true;
                            break;
                        }
                    }
                }
                errors += markAsError(CustomFormConstants.TELEGRAM, telegramInf, sameFound);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isRequired()) {

            if (imsAddressInf.getWidgets() != null && imsAddressInf.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) imsAddressInf.getWidgets().get(0).get(MultiTableNewUI.TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.IM_ADDRESS, imsAddressInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isChanged() ?
                            formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress(), value, formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getMinChar()));
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isRequired()) {

            if (webSiteInf.getWidgets() != null && webSiteInf.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) webSiteInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.WEB_ADDRESS, webSiteInf, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isChanged() ?
                            formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress(), value, formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getMinChar()));
                }
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATIONSHIP) != null && formPropertyMap.get(CustomFormConstants.RELATIONSHIP).isRequired()) {

            if (relations.getWidgets() != null && relations.getWidgets().get(0).size() > 0) {
                TextBox value = (TextBox) relations.getWidgets().get(0).get(PARAM_TEXT_BOX);
                if (value != null) {
                    errors += markAsError(CustomFormConstants.RELATIONSHIP, relations, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.RELATIONSHIP).isChanged() ?
                            formPropertyMap.get(CustomFormConstants.RELATIONSHIP).getTitle() : wfmStrings.relationship(), value, formPropertyMap.get(CustomFormConstants.RELATIONSHIP).getMinChar()));
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTS_TO) != null && formPropertyMap.get(CustomFormConstants.REPORTS_TO).isRequired()) {
            errors += markAsError(CustomFormConstants.REPORTS_TO, reportsTo, reportsTo.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null && formPropertyMap.get(CustomFormConstants.OWNER).isRequired()) {
            errors += markAsError(CustomFormConstants.OWNER, contactOwner, contactOwner.getSelectedItem() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.CRM_CAMPAIGN_NAME, campaignSource, campaignSource.getSelectedItemID() == null);
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

    protected boolean validateItemTable() {
        int errors = 0;
        productTable.setValidRows(0);
//        SelectItem firstSupplierItem = null;
        ArrayList<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();
        for (String columnCode : columnsMap.keySet()) {
            if (itemCFs.containsKey(columnCode) && (itemCFs.get(columnCode).isRequired() || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(itemCFs.containsKey(columnCode)) || Constants.UI_TYPE_URL.equals(itemCFs.containsKey(columnCode)) || Constants.UI_TYPE_PERCENTAGE.equals(itemCFs.containsKey(columnCode)))) {
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
        return errorFound;
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

        if (requiredColumnCodes.contains(ItemTableConstants.SUPPLIER)) {
            CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) productTable.getColumnById(rowID, ItemTableConstants.SUPPLIER);
            if (supplierLookUp.getSelectedItemID() == null) {
                supplierLookUp.getSuggestBox().addStyleName(Constants.ERROR_FORM_STYLE);
                productTable.notValid(rowID, ItemTableConstants.SUPPLIER);
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            }
            if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateEmailRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateUrl(t, null)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
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
            } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    productTable.notValid(rowID, fieldItem.getColumnCode());
                }
            }
        }

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

    private int[] validateRequiredItems(int rowID, List<CompanyCustomFieldItem> requiredCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[2];
        SmartProductLookUp product = (SmartProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);

        if (requiredColumnCodes.contains(ItemTableConstants.PRODUCT) && product.getSelectedItemID() == null) {
            productTable.setColumnValid(ItemTableConstants.PRODUCT);
            errors++;
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

        if (requiredColumnCodes.contains(ItemTableConstants.DISCOUNT_LIST)) {
            WfmDropdown discountList = (WfmDropdown) productTable.getColumnById(rowID, ItemTableConstants.DISCOUNT_LIST);
            if (discountList.getSelectedItem() == null) {
                productTable.setColumnValid(ItemTableConstants.DISCOUNT_LIST);
                errors++;
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.SUPPLIER)) {
            CrmAccountLookUp supplierLookUp = (CrmAccountLookUp) productTable.getColumnById(rowID, ItemTableConstants.SUPPLIER);
            if (supplierLookUp.getSelectedItemID() == null) {
                productTable.setColumnValid(ItemTableConstants.SUPPLIER);
                errors++;
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredCFs) {
            if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
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
            } else if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
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
            } else if (Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
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
            } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedId() == null) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea2 t = (TextArea2) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateTextAreaRequired(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    productTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(rowID, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(Constants.ERROR_FORM_STYLE);
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

    protected boolean validateLookUPCustomItemTables() {
        AtomicInteger errors = new AtomicInteger();
        Set<CompanyCustomFieldItem> lookUpCFs = new HashSet<>();


        for (Map.Entry<String, EditableTable> mapTable : editableTableMap.entrySet()) {

            String uuid = mapTable.getKey();

            List<CompanyCustomFieldItem> itemCustom = itemCustomCFs.get(uuid);

            Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(uuid))
                    .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            for (String columnCode : columnsMap.keySet()) {
                if (Constants.UI_TYPE_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                    lookUpCFs.add(getCustomFieldItem(itemCustom, columnCode));
                }
            }

            EditableTable productTable = mapTable.getValue();
            ArrayList<CustomTableRpc> tableItem = new ArrayList<>();

            for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                for (CompanyCustomFieldItem lookUpCfItem : lookUpCFs) {
                    if (Constants.UI_TYPE_LOOKUP.equals(lookUpCfItem.getUiType())) {
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

    protected void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME).getDefaultValue() != null) {
            firstName.setText(formPropertyMap.get(CustomFormConstants.FIRST_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null && formPropertyMap.get(CustomFormConstants.LAST_NAME).getDefaultValue() != null) {
            lastName.setText(formPropertyMap.get(CustomFormConstants.LAST_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MIDDLE_NAME) != null && formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).getDefaultValue() != null) {
            middleName.setText(formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OTHER_NAME) != null && formPropertyMap.get(CustomFormConstants.OTHER_NAME).getDefaultValue() != null) {
            otherName.setText(formPropertyMap.get(CustomFormConstants.OTHER_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getDefaultValue() != null) {
            companyName.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE).getDefaultValue() != null) {
            jobTitle.setText(formPropertyMap.get(CustomFormConstants.JOB_TITLE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue() != null) {
            department.setText(formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER) != null && formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).getDefaultValue() != null) {
            refIndNumber.setText(formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSETS) != null && formPropertyMap.get(CustomFormConstants.ASSETS).getDefaultValue() != null) {
            assets.setText(formPropertyMap.get(CustomFormConstants.ASSETS).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getDefaultValue() != null) {
            industries.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null && formPropertyMap.get(CustomFormConstants.EMAIL).getDefaultValue() != null && emailInf.getWidgets() != null && emailInf.getWidgets().get(0) != null) {
            TextBox value = (TextBox) emailInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.EMAIL).getDefaultValue());
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null && formPropertyMap.get(CustomFormConstants.PHONE).getDefaultValue() != null && phoneNumInf.getWidgets() != null && phoneNumInf.getWidgets().get(0) != null) {
            PhoneNumber phoneNumber = (PhoneNumber) phoneNumInf.getWidgets().get(0).get(MultiTableNewUI.PHONE_NUMBER);
            if (phoneNumber != null) {
                if (Utils.hasGenericAccess(ENABLE_ADD_PREFIX_TO_ADD_CONTACT) && params.length > 2 && AddContactView.FROM_INCOMING_CALL.equals(params[2])) {
                    phoneNumber.setData(formPropertyMap.get(CustomFormConstants.PHONE).getDefaultValue() + defaultPhone);
                } else {
                    phoneNumber.setData(formPropertyMap.get(CustomFormConstants.PHONE).getDefaultValue());
                }
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getDefaultValue() != null && imsAddressInf.getWidgets() != null && imsAddressInf.getWidgets().get(0) != null) {
            TextBox value = (TextBox) imsAddressInf.getWidgets().get(0).get(MultiTableNewUI.TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getDefaultValue());
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getDefaultValue() != null && webSiteInf.getWidgets() != null && webSiteInf.getWidgets().get(0) != null) {
            TextBox value = (TextBox) webSiteInf.getWidgets().get(0).get(PARAM_TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getDefaultValue());
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATIONSHIP) != null && formPropertyMap.get(CustomFormConstants.RELATIONSHIP).getDefaultValue() != null && relations.getWidgets() != null && relations.getWidgets().get(0) != null) {
            TextBox value = (TextBox) relations.getWidgets().get(0).get(PARAM_TEXT_BOX);
            if (value != null) {
                value.setText(formPropertyMap.get(CustomFormConstants.RELATIONSHIP).getDefaultValue());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTS_TO) != null && formPropertyMap.get(CustomFormConstants.REPORTS_TO).getDefaultValue() != null) {
            reportsTo.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.REPORTS_TO).getSelectedId(), formPropertyMap.get(CustomFormConstants.REPORTS_TO).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null && formPropertyMap.get(CustomFormConstants.OWNER).getDefaultValue() != null) {
            contactOwner.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.OWNER).getSelectedId(), formPropertyMap.get(CustomFormConstants.OWNER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getDefaultValue() != null) {
            campaignSource.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && noteWidget != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue() != null) {
            noteWidget.getTextBox().setText(formPropertyMap.get(CustomFormConstants.CRM_NOTE).getDefaultValue());
        }
    }

    public boolean validateEmails() {
        boolean validEmails = true;
        for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
            final TextBox emailBox = (TextBox) emailRow.get(PARAM_TEXT_BOX);
            if (!isEmpty(emailBox.getText()) && !wfmStrings.email().equals(emailBox.getText()) && !Utils.validateEmail(emailBox.getText(), false)) {
                markAsError(emailBox, true);
                validEmails = false;
                Utils.scrollIntoView(emailBox.getElement());
            }
        }
        return validEmails;
    }

    protected void onShellOk() {
        if (!Utils.isWebForm()) {
            if (isPopup) {
                popupCommand.execute();
            } else {
                if (saveAndClose) {
                    closeTab();
                } else if (saveAndNew) {
                    if (isCandidate()) {
                        closeTab("candidate|add/add");
                    } else if (isLead()) {
                        closeTab("lead|add/add");
                    } else {
                        closeTab("contact|add/add");
                    }
                } else {
                    objectId = null;
                    initForm();
                    registerFields();
                }
            }
        }
    }

    public static SelectItem[] getAddress() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(AddressReference.HOME.getId(), wfmStrings.contacthome());
        items[1] = new SelectItem(AddressReference.WORK.getId(), wfmStrings.contactwork());
        items[2] = new SelectItem(AddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }


    public static SelectItem[] getWebAddress() {
        SelectItem[] items = new SelectItem[11];
        items[1] = new SelectItem(WebAddressReference.WORK.getId(), wfmStrings.contactwork());
        items[0] = new SelectItem(WebAddressReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(WebAddressReference.HOMEPAGE.getId(), wfmStrings.contacthomePage());
        items[3] = new SelectItem(WebAddressReference.FTP.getId(), wfmStrings.ftp());
        items[4] = new SelectItem(WebAddressReference.BLOG.getId(), wfmStrings.blog());
        items[5] = new SelectItem(WebAddressReference.PROFILE.getId(), wfmStrings.contactprofile());
        items[6] = new SelectItem(WebAddressReference.OTHER.getId(), wfmStrings.other());
        items[7] = new SelectItem(WebAddressReference.LINKEDIN.getId(), wfmStrings.linkedIn());
        items[8] = new SelectItem(WebAddressReference.FACEBOOK.getId(), wfmStrings.facebook());
        items[9] = new SelectItem(WebAddressReference.TWITTER.getId(), wfmStrings.twitter());
        items[10] = new SelectItem(WebAddressReference.INSTAGRAM.getId(), wfmStrings.instagram());
        return items;
    }

    public static SelectItem[] getPhoneNumber() {
        SelectItem[] items = new SelectItem[6];
        items[0] = new SelectItem(PhoneReference.WORK.getId(), wfmStrings.contactwork());
        items[1] = new SelectItem(PhoneReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(PhoneReference.MOBILE.getId(), wfmStrings.mobile());
//        items[3] = new SelectItem(PhoneReference.FAX.getId(), wfmStrings.fax());
        items[3] = new SelectItem(PhoneReference.WHATS_APP.getId(), wfmStrings.whatsApp());
        items[4] = new SelectItem(PhoneReference.TELEGRAM.getId(), wfmStrings.telegram());
        items[5] = new SelectItem(PhoneReference.VIBER.getId(), wfmStrings.viber());
        return items;
    }

    public SelectItem[] getRelationsDropDownItems() {
        return relationsDropDownItems;
    }

    public SelectItem[] getImAddressDropDownItems() {
        return imAddressDropDownItems;
    }

    public TreeSelectItem[] getCategoriesDropDownsItems() {
        return categoriesDropDownsItems;
    }

    protected boolean isOtherSelected(DataListBox listBox) {
        return listBox.getSelectedItem() != null && wfmStrings.other().equalsIgnoreCase(listBox.getSelectedItem().getName());
    }

    protected boolean isOtherSelected(CRMLookUp list) {
        return list.getSelectedItem() != null && wfmStrings.other().equalsIgnoreCase(list.getSelectedItem().getName());
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public boolean isLead() {
        return ContactListItem.LEAD_CONTACT == contactType || (item != null && item.isLeadContact());
    }

    public boolean isCandidate() {
        return ContactListItem.CANDIDATE == contactType || (item != null && item.isCandidate());
    }

    private Widget[] getCustomWidgets(CustomTableRpc item, String fieldID) {
        int index = 0;

        Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(fieldID))
                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (itemCustomCFs.containsKey(fieldID)) {

                CompanyCustomFieldItem cfItem = getCustomFieldItem(itemCustomCFs.get(fieldID), columnCode);

                if (Constants.UI_TYPE_TEXTBOX.equals(cfItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(cfItem.getUiType()) || Constants.UI_TYPE_URL.equals(cfItem.getUiType())) {
                    CustomTextBoxField t = new CustomTextBoxField(cfItem);
                    t.setWidth("100%");
                    if (Constants.DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        Validation.addNumericKeyboardListener(t, 5, true);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                    CustomPercentageField t = new CustomPercentageField(cfItem);
                    t.setWidth("100%");
                    t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (Constants.UI_TYPE_DROPDOWN.equals(cfItem.getUiType())) {
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
                } else if (Constants.UI_TYPE_DATEPICKER.equals(cfItem.getUiType())) {
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

    protected HashMap<String, ArrayList<CustomTableRpc>> getCustomObjectData() {
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
                    if (itemCFs.containsKey(uuid)) {
                        Object customFieldValue = null;
                        Integer customFieldValueId = null;
                        SelectItem itemValue = null;
                        if (Constants.UI_TYPE_TEXTBOX.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
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

                        } else if (Constants.UI_TYPE_DROPDOWN.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDropDownField t = (CustomDropDownField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                            }
                        } else if (Constants.UI_TYPE_DATEPICKER.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDatePicker t = (CustomDatePicker) productTable.getColumnById(i, columnCode);
                            if (t.getDate() != null) {
                                customFieldValue = t.getDate();
                            }
                        } else if (Constants.UI_TYPE_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                                customFieldValueId = t.getSelectedItem().getId();
                            }
                        } else if (Constants.UI_TYPE_CURRENCY.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldCurrencyWidget t = (CustomFieldCurrencyWidget) productTable.getColumnById(i, columnCode);
                            if (t.getCurrencyID() != null) {
                                customFieldValue = t.getCurrencyName();
                                customFieldValueId = t.getCurrencyID();
                            }
                        } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItems() != null && t.getSelectedItems().size() > 0) {
                                customFieldValue = t.getSelectedItems();
                            }
                        } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
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
                            if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
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

    protected void drawProductTable() {
        LoadingPanel.loading(true);
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.LEAD_ITEM, new AbstractAsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    for (ColumnConfigs cc : result) {
                        if (cc.isSelected()) {
                            columnsMap.put(cc.getCode(), cc);
                        }
                    }
                }

                productTable = new EditableTable(getColumns(), true, true);
                productTable.setDraggable(true);
                productTable.ensureDebugId("Lead_product_table");
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

                receiptTable = new OpportunityReceiptTable();
                receiptTable.addStyleName("totalsTable java-AddContactView");
                fp.add(receiptTable);

                initTotalTableWidgets();
                addField(CustomFormConstants.CRM_LEAD_ITEMS, fp, "", true);
            }
        });
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
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.item(), Utils.getColumnWidth(columnConfigs.getWidth(), 200), columnConfigs.isRequired());
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
                case ItemTableConstants.SUPPLIER:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.SUPPLIER, columnConfigs.isChanged() ? columnConfigs.getTitle() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
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
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnConfigs.getCode(), columnConfigs.getTitle(), Utils.getColumnWidth(columnConfigs.getWidth(), 100), columnConfigs.isRequired(), isPixel);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns[i++] = columnConfig;

                    if (Constants.UI_TYPE_TEXTAREA.equalsIgnoreCase(columnConfigs.getUiType())) {
                        columnConfig.setCustomStyleName("product-description-cell");
                    }
                    break;
            }
        }
        return columns;
    }


    public void drawTotalsTable() {
        receiptTable.clear();
        if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
            receiptTable.setSubtotalItem(subTotalLabel, subTotal);
        }
        if (columnsMap.containsKey(ItemTableConstants.DISCOUNT_AMT)) {
        }

        if (columnsMap.containsKey(ItemTableConstants.TAX_LIST)) {
            for (Integer key : taxWidgetMap.keySet()) {
                TaxView tax = (TaxView) taxWidgetMap.get(key);
                HTML taxLabel = new HTML(tax.getItem().getName());
                taxLabel.setStyleName("totallabel");
                tax.setStyleName("totalvalue");

                receiptTable.addItem(taxLabel, tax);
            }
        }

        totalLabel.setHTML(accountingMessages.dynamicTotal(item.getBaseCurrencyName()));
        if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
            receiptTable.addGrossItem(totalLabel, totalPrice);
        }

        if (columnsMap.containsKey(ItemTableConstants.QTY)) {
            receiptTable.addGrossItem(new HTML(wfmStrings.qty()), quantityTotal);
        }
    }

    protected void initTotalTableWidgets() {
        subTotalLabel = new HTML(wfmStrings.subtotal());
        totalLabel = new HTML(wfmStrings.total());
        totalTaxLabel = new HTML(wfmStrings.taxTotal());

        subTotalLabel.ensureDebugId("subTotalLabel");
        totalLabel.ensureDebugId("totalLabel");
        totalTaxLabel.ensureDebugId("totalTaxLabel");

        subTotal = getZeroAsHTML();
        totalPrice = getZeroAsHTML();
        taxTotal = getZeroAsHTML();
        quantityTotal = getZeroAsHTML();

        subTotal.ensureDebugId("subTotal");
        totalPrice.ensureDebugId("totalPrice");
        taxTotal.ensureDebugId("taxTotal");

        subTotalLabel.setStyleName("totallabel");
        totalLabel.setStyleName("totallabel");
        totalTaxLabel.setStyleName("totallabel");

        subTotal.setStyleName("totalvalue");
        totalPrice.setStyleName("totalvalue");
        taxTotal.setStyleName("totalvalue");
    }

    private Widget[] getWidgets(OpportunityItem item, boolean updateExchangeRate) {
        int index = 0;
        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                final SmartProductLookUp product = new SmartProductLookUp(RECEIVED);
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
                }
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
                    txtPrice.setBaseCurrencyValue(item.getPrice());
                }
                txtPrice.addChangeHandler(changeEvent -> {
                    txtPrice.setLayoutData(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue()))/*.divide(AccountingUtils.get().parseToBigDecimal(exchangeRate.getText()), 5, BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_HALF_UP))*/;
                    txtPrice.setBaseCurrencyValue(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue()));
                    calculate();
                });
                txtPrice.setTitle(columnCode);
                widgets[index++] = txtPrice;
            } else if (ItemTableConstants.SUPPLIER.equals(columnCode)) {
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
            } else if (ItemTableConstants.TAX_LIST.equals(columnCode)) {
                SmartTaxRateLookUp taxLookUp = new SmartTaxRateLookUp(Constants.PAYABLE);
                taxLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getTaxItem() != null) {
                    taxLookUp.addTaxItem(item.getTaxItem());
                }
                taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> reDrawTaxesDropdown());
                taxLookUp.getSuggestBox().addKeyUpHandler(keyUpEvent -> reDrawTaxesDropdown());
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

    public void calculate() {
        if (hasProduct) {
            BigDecimal subTotalAmount = BigDecimal.ZERO, quantity = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;
            final Map<Integer, BigDecimal> taxTotal = new HashMap<>();
            for (int rowID = 0; rowID < productTable.getGrid().getRowCount(); rowID++) {
                BigDecimal subtotal = BigDecimal.ZERO, itemTaxAmount = BigDecimal.ZERO;
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
                if (columnsMap.containsKey(ItemTableConstants.TAX_LIST)) {
                    SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) productTable.getColumnById(rowID, ItemTableConstants.TAX_LIST);
                    if (taxLookUp != null && taxLookUp.getSelectedItemID() != null) {
                        itemTaxAmount = calculateTaxAmount(taxLookUp, taxTotal, subtotal);
                    } else if (taxLookUp != null) {
                        taxLookUp.setItemTaxAmount(BigDecimal.ZERO);
                    }

                }

                ExtendedHTML netAmount = (ExtendedHTML) productTable.getColumnById(rowID, ItemTableConstants.NET_AMT);
                if (netAmount != null) {
                    netAmount.setText(numberFormat.format(subtotal));
                    netAmount.setValue(subtotal);
                    CustomCell netAmountCell = (CustomCell) productTable.getColumnCellWidgetById(rowID, ItemTableConstants.NET_AMT);
                    netAmountCell.InActive();
                }

                ExtendedHTML totalWidget = (ExtendedHTML) productTable.getColumnById(rowID, ItemTableConstants.TOTAL_AMT);
                if (totalWidget != null) {
                    BigDecimal lineTotal = subtotal;
                    lineTotal = lineTotal.add(itemTaxAmount);
                    totalWidget.setText(numberFormat.format(lineTotal));
                    totalWidget.setValue(lineTotal);
                    CustomCell totalCell = (CustomCell) productTable.getColumnCellWidgetById(rowID, ItemTableConstants.TOTAL_AMT);
                    totalCell.InActive();
                }

                subTotalAmount = subTotalAmount.add(subtotal);
                totalAmount = totalAmount.add(subtotal);
            }
            subTotal.setValue(subTotalAmount);
            totalPrice.setValue(totalAmount);
            quantityTotal.setValue(quantity);
        }

        drawTotalsTable();
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

    private BigDecimal calculateTaxAmount(TaxLookUp taxLookUp, Map<Integer, BigDecimal> taxTotal, BigDecimal discountedNet) {
        TaxView taxHTML = (TaxView) taxWidgetMap.get(taxLookUp.getSelectedItemID());
        BigDecimal itemTaxAmount = BigDecimal.ZERO;

        if (taxHTML == null) {
            taxHTML = new TaxView();
            taxHTML.setItem(taxLookUp.getSelectedData());
            taxWidgetMap.put(taxLookUp.getSelectedItemID(), taxHTML);
        }
        BigDecimal taxPercent = taxHTML.getItem().getEffectiveTaxPercent();
        itemTaxAmount = discountedNet.multiply(taxHTML.getItem().getEffectiveTaxPercent().divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
        itemTaxAmount = itemTaxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        BigDecimal totalTax = taxTotal.get(taxLookUp.getSelectedItemID());
        BigDecimal currentTaxTotal = (totalTax != null ? totalTax : BigDecimal.ZERO).add(itemTaxAmount);

        taxHTML.setHTML(numberFormat.format(currentTaxTotal));
        taxTotal.put(taxLookUp.getSelectedItemID(), currentTaxTotal);
        taxLookUp.setItemTaxAmount(itemTaxAmount);

        return itemTaxAmount;
    }

    private void checkForProductExist() {
        hasProduct = false;
        for (int rowID = 0; rowID < productTable.getGrid().getRowCount(); rowID++) {
            if (columnsMap.containsKey(ItemTableConstants.PRODUCT)) {
                ProductLookUp product = (ProductLookUp) productTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
                if (product.getSelectedItemID() != null || !(Utils.isNullOrEmpty(product.getText()) || LookUp.wfmStrings.searchTypeMessage().equals(product.getText().trim()))) {
                    hasProduct = true;
                    break;
                }
            }
        }
    }

    private void setItemValues(SmartProductLookUp productLookUp) {
        ProductSelectItem item = productLookUp.getSelectedItem();
        if (item != null && item.getId() != null) {
            hasProduct = true;
            ProductService.App.get().getProductBaseData(item.getId(), true, new LoadingPanelCallback<NewProduct>(productTable, wfmStrings.pleaseWait()) {
                public void success(NewProduct product) {
                    if (product != null) {
                        OpportunityItem opportunityItem = new OpportunityItem();
                        opportunityItem.setItemName(product.getItemName());
                        opportunityItem.setItemNumber(product.getNumberData() != null ? product.getNumberData().getNumberString() : "");
                        opportunityItem.setItemID(product.getObjectId());
                        opportunityItem.setDescription(product.getDescription());
                        opportunityItem.setQty(product.getQuantity() != null ? product.getQuantity() : new BigDecimal("0"));
                        opportunityItem.setUnitMeasurement(product.getUnitMeasurement());
                        opportunityItem.setPrice(product.getSellingPrice());
                        opportunityItem.setProductCategory(new SelectItem(product.getCategoryID(), product.getCategoryName()));
                        opportunityItem.setProductBrand(new SelectItem(product.getBrandID(), product.getBrandName()));
                        opportunityItem.setItemCustomFields(product.getProductCustomFieldItems());
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

    private void setValueStaticFieldFromCFByAliasName(OpportunityItem opportunityItem, ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        for (CompanyCustomFieldItem productCFItem : productCustomFieldItems) {
            if (productCFItem != null && productCFItem.getAliasName() != null) {
                switch (productCFItem.getAliasName()) {
                    case ItemTableConstants.DESCRIPTION:
                        if ((Utils.isNullOrEmpty(opportunityItem.getDescription())) &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (Constants.UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(productCFItem.getUiType()))) {
                            opportunityItem.setDescription(productCFItem.getFieldStringValue());
                        }
                        break;

                    case ItemTableConstants.QTY:
                        if (opportunityItem.getQty() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (Constants.UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && Constants.DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setQty(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.MEASUREMENT:
                        if (opportunityItem.getUnitMeasurement() == null &&
                                productCFItem.getSelectedId() != null && Constants.UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setUnitMeasurement(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.UNITPRICE:
                        if (opportunityItem.getPrice() == null &&
                                !Utils.isNullOrEmpty(productCFItem.getFieldStringValue()) && (Constants.UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || Constants.UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && Constants.DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            opportunityItem.setPrice(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.SUPPLIER:
                        if (opportunityItem.getSupplierID() == null &&
                                productCFItem.getSelectedId() != null && Constants.UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setSupplierID(productCFItem.getSelectedId());
                            opportunityItem.setSupplierName(productCFItem.getFieldStringValue());
                        }
                        break;

                    case ItemTableConstants.CATEGORY:
                        if (opportunityItem.getProductCategory() == null &&
                                productCFItem.getSelectedId() != null && Constants.UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT_CATEGORY.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProductCategory(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                        break;

                    case ItemTableConstants.PROJECT:
                        if (Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE) && opportunityItem.getProject() == null &&
                                productCFItem.getSelectedId() != null && Constants.UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(productCFItem.getLookUpTypeEnum())) {
                            opportunityItem.setProject(new SelectItem(productCFItem.getSelectedId(), productCFItem.getFieldStringValue()));
                        }
                        break;
                }
            }
        }
    }

    public ExtendedHTML getZeroAsHTML() {
        ExtendedHTML zeroValue = new ExtendedHTML(AccountingUtils.getZero());
        zeroValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        return zeroValue;
    }

    private ColumnConfig[] getCustomColumns(Map<String, ColumnConfigs> columnsMap) {
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            switch (cc) {
                case ItemTableConstants.PRODUCT:
                    columns[i++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 100, columnsMap.get(cc).isRequired());
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
                    if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
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

    protected void setItemTableValues(HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
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

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
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
        return Constants.Contacts;
    }
}
