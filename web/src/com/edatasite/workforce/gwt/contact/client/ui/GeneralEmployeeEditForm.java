package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.reference.WebAddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.CheckboxSelector;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.SpokenLanguagesWidget;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkedLinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TelegramChatSingleLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.LinkedTypeWidget;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldCurrencyWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomHTMLTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER;
import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS;
import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.LOCALE_PAYROLL;

/**
 * User: unni
 * Date: Oct 23, 2009
 * Time: 4:20:48 PM
 */

public class GeneralEmployeeEditForm extends CustomForm2 implements Constants, FormHasCustomFieldInterface, CommandConstants, Colapse, Errors {

    private final boolean isCustomise = SinksContainerFactory.entryPoint.moduleSetting.isCustomise();

    protected WfmButton2 saveButton;
    protected WfmButton2 saveAndCloseButton;
    protected WfmButton2 cancelButton;

    public static NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");
    private static final NumberFormat singlePointPrecisionNumberFormat = NumberFormat.getFormat(",##0.0");
    private static final String PARAM_TEXT_BOX = "PARAM_TEXT_BOX";
    private static final String PRIMARY_RADIO_BUTTON = "PRIMARY_RADIO_BUTTON";
    private static final String RELATION_LIST_BOX = "RELATION_LIST_BOX";
    private static final String RELATED_CHAT = "RELATED_CHAT";
    public static final int DEFAULT_ROWS = 5;
    private boolean emptyRows;
    protected ListingFilterParameter filterParameter = new ListingFilterParameter();

    private Address address;
    public boolean isAddView;
    public Integer placementId;
    public boolean isPlacement;
    private int contactType = ContactListItem.EMPLOYEE_CONTACT;
    protected ContactListItem contactListItem;

    protected Integer employeeID;
    private Integer employeeContactID;// objectId
    private boolean isFromPlacement = false;

    private KpiRadioButton male;
    private DataListBox martialStatus;
    //private TextBox languages;
    private CustomList languages;
    private KpiRadioButton female;
    private KpiRadioButton minSalaryMethod;
    private KpiRadioButton midSalaryMethod;
    private KpiRadioButton maxSalaryMethod;
    private DataListBox salaryMode;

    protected DatePicker fireDatePicker;
    protected DatePicker hireDatePicker;

    public LinkedHashMap<Integer, String> imAddressMap = new LinkedHashMap<>();
    DataListBox imAddressLocation;

    private FormHasCustomField customFieldUtil;

    //
    private TextBox firstName;
    private TextBox middleName;
    private TextBox lastName;
    private TextBox otherName;
    private TextBox driverID;

    private DataListBox titl;
    private TextBox title;
    private DatePicker birthDatePicker;

    private MultiTableNewUI emailInf;
    private MultiTableNewUI phoneNumInf;
    private MultiTableNewUI imsAddressInf;
    private MultiTableNewUI webSiteInf;
    private MultiTableNewUI addressInf;
    private MultiTableNewUI telegramInf;
    //

    private Numbering empCode;
    private TextBox wageRate;
    private TextBox clientChargeRate;
    private DataListBox employeeQualification;
    private DataListBox status;
    private EmployeeLookUpWithCode reportsTo;
    private DepartmentLookUp pmDepartment;
    private LocationLookUpWithCode locations;

    private DataListBox timeslot;

    private TextBox termsOfContractBox;
    private DataListBox monthYearBox;

    private DataListBox empMode;
    private TextBox salaryAmount;
    //    private TextBox salaryTotalAmount;
    private DataListBox jobTitle;

    private CheckboxSelector mainRole;
    private CheckboxSelector fingerprintDevices;
    protected KpiSwitcher noAccess, essUser;
    private PositionLookUp positionPanel;
    private AdvancedInputGroup locationPanel1;
    private FlexTable genderTable;
    private FlexTable paymentMethodTable;
    private TextBox nationality;
    private KpiCellTree courses;


    //UserBankAccount fields
    private TextBox bankName;
    private TextArea2 bankAddress;
    private TextBox accountNumber;
    private TextBox accountName;
    private TextBox swiftCode;
    private TextBox sortCode;
    private TextBox iBanCode;
    private TextBox agentID;

    //UserBankAccount fields

    protected ProfileItem profileItem;

    private ArrayList<CompanyCustomFieldItem> companyCustomFieldItems;
    private boolean showCustomFields = false;
    private GeneralFileUpload uploadForm;

    protected boolean fromSectionPMEmployeeEdit;
    protected boolean employeeAddFromPayroll = false;
    protected boolean fromSectionProfileSettingsEdit;
    protected boolean fromSectionTCInstructor;
    protected boolean fromSectionSingleEmployeeAdd;
    private String test_code_ID_name = "employee_";

    public String successMessage = wfmStrings.employeeprofilehasbeenupdated();
    public String errorMessage = wfmStrings.errorOccurredUpdate();
    //Personal Identity Information
    private TextBox passportNumber;
    private CountryLookUp passportIssue;
    private DatePicker passportIssueDate;
    private DatePicker passportExpiryDate;
    private TextBox wpsNumber;
    private TextBox visaNumber;
    private DatePicker visaIssueDate;
    private TextBox insuranceNumber;
    private DatePicker visaExpirationDate;
    private MultiTableNewUI visaExpirationDateReminderTable;
    private DatePicker medicalInsuranceExDate;
    private KpiCheckBox applyLeaveAllowanceForEmployee;
    private VerticalPanel wageRateWithCheckBox;
    private KpiCheckBox applyWageRate;
    private DateBox applyWageRateFrom;
    private VerticalPanel clientChargeRateWithCheckBox;
    private KpiCheckBox applyClientChargeRate;
    private DateBox applyClientChargeRateFrom;

    private TextBox openingBalanceDays;
    private TextBox probationDays;
    public EditableTable paymentsTable;
    public EditableTable deductionsTable;
    public EditableTable taxTable;
    private EditableTable loansTable;
    private EditableTable employerContributionTable;
    private ArrayList<Integer> deletedCategories;
    private ArrayList<Integer> inactiveCategories;
    private ProfileImage profilePicture;
    private LinkedHashMap<String, FormProperty> formProperty;
    private InputGroup firstNameWidget;
    private SelectItem[] telegramBots;
    private DataListBox employeeDegree;
    private SpokenLanguagesWidget languagesWidget;
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private final Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();

    protected String formType = null;
    protected Integer convertedFormId = null;
    private Integer departmentId = null;
    private DatePicker newDeptEffectiveDate = null;
    private EditableTable experienceTable;
    private Div positionContainer;
    private Div departmentContainer;

    private final Map<String, ColumnConfigs> experienceColumnsMap = new LinkedHashMap<>();

    private final Map<String, CompanyCustomFieldItem> experienceItemCFs = new LinkedHashMap<>();

    private final SelectItem salaryMode0 = new SelectItem(0, wfmStrings.byTariffGrid(), TARIFF_GRID);
    private final SelectItem salaryMode1 = new SelectItem(1, wfmStrings.byFixedAmount(), FIXED_AMOUNT);
    private final SelectItem salaryMode2 = new SelectItem(2, wfmStrings.basedOnAttendanceReport(), BY_ATTENDANCE_REPORT);


    public GeneralEmployeeEditForm(String name, String description) {
        super(name, description);
    }

    public GeneralEmployeeEditForm(Integer employeeID) {
        this(employeeID, Constants.HRMS_EDIT_PROFILE, wfmStrings.editEmployeeProfile(), "employee_profile_edit_view_", FROM_HRMS);
    }

    public GeneralEmployeeEditForm(Integer employeeID, String name, String description, String test_code_ID_name, String... fromSection) {
        super(name, description);
        this.test_code_ID_name = (test_code_ID_name != null && !"".equals(test_code_ID_name) ? test_code_ID_name : "employee_profile_edit_view_");

        if (fromSection != null && fromSection.length > 0 && fromSection[0] != null) {
            if (FROM_PM.equals(fromSection[0])) {
                fromSectionPMEmployeeEdit = true;
            } else if (FROM_SETTINGS_PROFILE.equals(fromSection[0])) {
                fromSectionProfileSettingsEdit = true;
            } else if (FROM_TC_INSTRUCTOR.equals(fromSection[0])) {
                fromSectionTCInstructor = true;
            }
        }
        setContactType(CrmConstants.TYPE_EMPLOYEE_CONTACT);
        if (employeeID != null && employeeID != 0) {
            this.employeeID = employeeID;
        }
        if (fromSectionPMEmployeeEdit || FROM_HRMS.equals(fromSection[0])) {
            drawCustomFields();
        }
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        saveButton = addButton(wfmStrings.save(), null, (test_code_ID_name + "save_button"), event -> {
            //save logic
            save(false);
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        String from = fromSectionTCInstructor ? TC_INSTRUCTOR_ADD_FORM : fromSectionSingleEmployeeAdd ? FROM_SINGLE_EMPLOYEE_ADD : null;
        boolean canChange = Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_UPLOAD_PHOTO : PermissionConstants.HRMS_EMPLOYEE_UPLOAD_PHOTO);
        if (Utils.isSettings()) {
            canChange = true;
        }
        boolean finalCanChange = canChange;
        ContactService.App.get().editProfile(employeeID, from, false, placementId, formType, convertedFormId, new AbstractAsyncCallback<ProfileItem>() {
            public void success(ProfileItem o) {
                profileItem = o;
                employeeContactID = o.getContactID();
                departmentId = o.getPmDepartmentID();
//                currentWorkType = o.getWorkType();
                profilePicture.initialize(profileItem.getEmployeeImageUrl(), profileItem.getFirstName(), profileItem.getLastName(), finalCanChange, profileItem.getGender());

                setImmData();
                fillFormWithData();
                if (employeeID == null) {
                    setDefaultValues();
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void setImmData() {
        imAddressLocation.setItems(profileItem.getContactImAddress());
        for (SelectItem immAddress : profileItem.getContactImAddress()) {
            imAddressMap.put(immAddress.getId(), immAddress.getDescription());
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMPLOYEE_PROFILE_FORM;
    }

    @Override
    protected String getFormType() {
        return employeeID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Employee, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formProperty = result.getFormPropertyMap();
                GeneralEmployeeEditForm.super.onInitialize();
            }
        });
        return null;
    }


    @Override
    protected void initPredefinedValues() {
        if (profileItem != null) {
            addPredefinedValues(CustomFormConstants.FIRST_NAME, profileItem.getTitleList());
            addPredefinedValues(QUALIFICATION, profileItem.getQualifications());
            addPredefinedValues(MARTIAL_STATUS, profileItem.getMartialStatusList());
            addPredefinedValues(CustomFormConstants.DEPARTMENT, profileItem.getPmDepartmentItems());
            addPredefinedValues(EMPLOYMENT_MODE, profileItem.getEmpModeList());
            addPredefinedValues(ACCOUNT_STATUS, profileItem.getStatusList());
        }
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }


    protected void drawAddressInformation() {
        //address information
        if (!fromSectionTCInstructor) {
            String permission = fromSectionPMEmployeeEdit ? PermissionConstants.PM_SHOW_EMPLOYEE_ADDRESS : PermissionConstants.HRMS_SHOW_EMPLOYEE_ADDRESS;
            if ((employeeID != null && employeeID.equals(Utils.getUserID())) || Utils.hasPermission(permission)) {
                addTitleField(CustomFormConstants.ADDRESS_INFORMATION, wfmStrings.addressInformation());
                addField(CustomFormConstants.ADDRESS, addressInf, wfmStrings.address());
            }
        }
    }

    protected void drawAccountInformation() {
        addTitleField(CustomFormConstants.ACCOUNT_INFORMATION, wfmStrings.basicInfo());
        //account status
        addField(CustomFormConstants.ACCOUNT_STATUS, status, getTitle(wfmStrings.accountStatus()));
        //account roles
        addField(CustomFormConstants.NO_ACCESS, noAccess, getTitle(wfmStrings.noAccess()));
    }

    protected void drawAttachments() {
        //attachments
        addField(CustomFormConstants.ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
    }

    protected void drawBankInformation() {
        addTitleField(CustomFormConstants.BANK_ACCOUNT_INFORMATION, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountInformation(), wfmStrings.bankAccount()));
        //bank name

        if (formProperty != null && formProperty.get(CustomFormConstants.BANK_NAME) != null) {
            addField(CustomFormConstants.BANK_NAME, bankName, getTitle(formProperty.get(CustomFormConstants.BANK_NAME).isChanged() ?
                            formProperty.get(CustomFormConstants.BANK_NAME).getTitle() : wfmStrings.bankName(), formProperty.get(CustomFormConstants.BANK_NAME).isRequired()), false,
                    formProperty.get(CustomFormConstants.BANK_NAME).isInformation());
            if (formProperty.get(CustomFormConstants.BANK_NAME).isInformation()) {
                new KpiToolTip(bankAddress, formProperty.get(CustomFormConstants.BANK_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        }

        bankName.addStyleName(test_code_ID_name + "bank_name");
        //account number

        if (formProperty != null && formProperty.get(CustomFormConstants.ACCOUNT_NUMBER) != null) {
            addField(CustomFormConstants.ACCOUNT_NUMBER, accountNumber, getTitle(formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).isChanged() ?
                            formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNumber(), formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).isRequired()), false,
                    formProperty.get(CustomFormConstants.ACCOUNT_NAME).isInformation());
            if (formProperty.get(CustomFormConstants.ACCOUNT_NAME).isInformation()) {
                new KpiToolTip(accountNumber, formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.ACCOUNT_NUMBER, accountNumber, getTitle(wfmStrings.accountNumber()));
        }

        accountNumber.addStyleName(test_code_ID_name + "account_number");
        //account name

        if (formProperty != null && formProperty.get(CustomFormConstants.ACCOUNT_NAME) != null) {
            addField(CustomFormConstants.ACCOUNT_NAME, accountName, getTitle(formProperty.get(CustomFormConstants.ACCOUNT_NAME).isChanged() ?
                            formProperty.get(CustomFormConstants.ACCOUNT_NAME).getTitle() : wfmStrings.accountName(), formProperty.get(CustomFormConstants.ACCOUNT_NAME).isRequired()), false,
                    formProperty.get(CustomFormConstants.ACCOUNT_NAME).isInformation());
            if (formProperty.get(CustomFormConstants.ACCOUNT_NAME).isInformation()) {
                new KpiToolTip(accountName, formProperty.get(CustomFormConstants.ACCOUNT_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        }

        accountName.addStyleName(test_code_ID_name + "account_name");
        //bank address

        if (formProperty != null && formProperty.get(CustomFormConstants.BANK_ADDRESS) != null) {
            addField(CustomFormConstants.BANK_ADDRESS, bankAddress, getTitle(formProperty.get(CustomFormConstants.BANK_ADDRESS).isChanged() ?
                            formProperty.get(CustomFormConstants.BANK_ADDRESS).getTitle() : wfmStrings.bankAddress(), formProperty.get(CustomFormConstants.BANK_ADDRESS).isRequired()), false,
                    formProperty.get(CustomFormConstants.BANK_ADDRESS).isInformation());
            if (formProperty.get(CustomFormConstants.BANK_ADDRESS).isInformation()) {
                new KpiToolTip(bankAddress, formProperty.get(CustomFormConstants.BANK_ADDRESS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.BANK_ADDRESS, bankAddress, wfmStrings.bankAddress());
        }

        bankAddress.addStyleName(test_code_ID_name + "bank_address");
        //swift code

        if (formProperty != null && formProperty.get(CustomFormConstants.SWIFT_CODE) != null) {
            addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(formProperty.get(CustomFormConstants.SWIFT_CODE).isChanged() ?
                            formProperty.get(CustomFormConstants.SWIFT_CODE).getTitle() : wfmStrings.swiftCode(), formProperty.get(CustomFormConstants.SWIFT_CODE).isRequired()), false,
                    formProperty.get(CustomFormConstants.SWIFT_CODE).isInformation());
            if (formProperty.get(CustomFormConstants.SWIFT_CODE).isInformation()) {
                new KpiToolTip(swiftCode, formProperty.get(CustomFormConstants.SWIFT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        }
        swiftCode.addStyleName(test_code_ID_name + "swift_code");
        //sort code

        if (formProperty != null && formProperty.get(CustomFormConstants.SORT_CODE) != null) {
            addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(formProperty.get(CustomFormConstants.SORT_CODE).isChanged() ?
                            formProperty.get(CustomFormConstants.SORT_CODE).getTitle() : wfmStrings.sortCode(), formProperty.get(CustomFormConstants.SORT_CODE).isRequired()), false,
                    formProperty.get(CustomFormConstants.SORT_CODE).isInformation());
            if (formProperty.get(CustomFormConstants.SORT_CODE).isInformation()) {
                new KpiToolTip(sortCode, formProperty.get(CustomFormConstants.SORT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        }
        sortCode.addStyleName(test_code_ID_name + "sort_code");
        //iBan code

        if (formProperty != null && formProperty.get(CustomFormConstants.IBAN_CODE) != null) {
            addField(CustomFormConstants.IBAN_CODE, iBanCode, getTitle(formProperty.get(CustomFormConstants.IBAN_CODE).isChanged() ?
                            formProperty.get(CustomFormConstants.IBAN_CODE).getTitle() : wfmStrings.ibanCode(), formProperty.get(CustomFormConstants.IBAN_CODE).isRequired()), false,
                    formProperty.get(CustomFormConstants.IBAN_CODE).isInformation());
            if (formProperty.get(CustomFormConstants.IBAN_CODE).isInformation()) {
                new KpiToolTip(iBanCode, formProperty.get(CustomFormConstants.IBAN_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.IBAN_CODE, iBanCode, getTitle(wfmStrings.ibanCode()));
        }
        iBanCode.addStyleName(test_code_ID_name + "iBan_code");
//        AgentID

        if (formProperty != null && formProperty.get(CustomFormConstants.AGENT_ID) != null) {
            addField(CustomFormConstants.AGENT_ID, agentID, getTitle(formProperty.get(CustomFormConstants.AGENT_ID).isChanged() ?
                            formProperty.get(CustomFormConstants.AGENT_ID).getTitle() : wfmStrings.agentID(), formProperty.get(CustomFormConstants.AGENT_ID).isRequired()), false,
                    formProperty.get(CustomFormConstants.AGENT_ID).isInformation());
            if (formProperty.get(CustomFormConstants.AGENT_ID).isInformation()) {
                new KpiToolTip(agentID, formProperty.get(CustomFormConstants.AGENT_ID).getInformationText());
            }
        } else {
            addField(CustomFormConstants.AGENT_ID, agentID, getTitle(wfmStrings.agentID()));
        }

        agentID.addStyleName(test_code_ID_name + "agentID");
    }

    protected void drawContactDetails() {
        addTitleField(CustomFormConstants.CONTACT_INFORMATION, Property.get(Constants.Contacts, wfmStrings.contactDetails(), wfmStrings.contact()));
        //email
        if (formProperty != null && formProperty.get(CustomFormConstants.EMAIL) != null) {
            addField(CustomFormConstants.EMAIL, emailInf, getTitle(formProperty.get(CustomFormConstants.EMAIL).isChanged() ? formProperty.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(),
                            formProperty.get(CustomFormConstants.EMAIL).isRequired()), false,
                    formProperty.get(CustomFormConstants.EMAIL).isInformation());
            if (formProperty.get(CustomFormConstants.EMAIL).isInformation()) {
                new KpiToolTip(emailInf, formProperty.get(CustomFormConstants.EMAIL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.EMAIL, emailInf, getTitle(wfmStrings.email(), true));
        }
        //phone numbers
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null) {
            addField(CustomFormConstants.PHONE, phoneNumInf, getTitle(formProperty.get(CustomFormConstants.PHONE).isChanged() ? formProperty.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(),
                            formProperty.get(CustomFormConstants.PHONE).isRequired()), false,
                    formProperty.get(CustomFormConstants.PHONE).isInformation());
            if (formProperty.get(CustomFormConstants.PHONE).isInformation()) {
                new KpiToolTip(phoneNumInf, formProperty.get(CustomFormConstants.PHONE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PHONE, phoneNumInf, getTitle(wfmStrings.phone()));
        }
        if (!Utils.isSettings()) {
            //IM address
            if (formProperty != null && formProperty.get(CustomFormConstants.IM_ADDRESS) != null) {
                addField(CustomFormConstants.IM_ADDRESS, imsAddressInf, getTitle(formProperty.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formProperty.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress(),
                                formProperty.get(CustomFormConstants.IM_ADDRESS).isRequired()), false,
                        formProperty.get(CustomFormConstants.IM_ADDRESS).isInformation());
                if (formProperty.get(CustomFormConstants.IM_ADDRESS).isInformation()) {
                    new KpiToolTip(imsAddressInf, formProperty.get(CustomFormConstants.IM_ADDRESS).getInformationText());
                }
            } else {
                addField(CustomFormConstants.IM_ADDRESS, imsAddressInf, getTitle(wfmStrings.imAddress()));
            }
            //web address
            if (formProperty != null && formProperty.get(CustomFormConstants.WEB_ADDRESS) != null) {
                addField(CustomFormConstants.WEB_ADDRESS, webSiteInf, getTitle(formProperty.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formProperty.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress(),
                                formProperty.get(CustomFormConstants.WEB_ADDRESS).isRequired()), false,
                        formProperty.get(CustomFormConstants.WEB_ADDRESS).isInformation());
                if (formProperty.get(CustomFormConstants.WEB_ADDRESS).isInformation()) {
                    new KpiToolTip(webSiteInf, formProperty.get(CustomFormConstants.WEB_ADDRESS).getInformationText());
                }
            } else {
                addField(CustomFormConstants.WEB_ADDRESS, webSiteInf, getTitle(wfmStrings.webAddress()));
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.TELEGRAM) != null) {
            addField(CustomFormConstants.TELEGRAM, telegramInf, getTitle(formProperty.get(CustomFormConstants.TELEGRAM).isChanged() ? formProperty.get(CustomFormConstants.TELEGRAM).getTitle() : wfmStrings.telegram(),
                            formProperty.get(CustomFormConstants.TELEGRAM).isRequired()), false,
                    formProperty.get(CustomFormConstants.TELEGRAM).isInformation());
            if (formProperty.get(CustomFormConstants.TELEGRAM).isInformation()) {
                new KpiToolTip(telegramInf, formProperty.get(CustomFormConstants.TELEGRAM).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TELEGRAM, telegramInf, wfmStrings.telegram());
        }
    }

    protected void drawPersonalIdentityInformation() {
        addTitleField(CustomFormConstants.PERSONAL_IDENTITY_INFORMATION, wfmStrings.personalIdentityInformation());

        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_NUMBER) != null) {
            addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, getTitle(formProperty.get(CustomFormConstants.PASSPORT_NUMBER).isChanged() ?
                            formProperty.get(CustomFormConstants.PASSPORT_NUMBER).getTitle() : wfmStrings.passportNumber(), formProperty.get(CustomFormConstants.PASSPORT_NUMBER).isRequired()), false,
                    formProperty.get(CustomFormConstants.PASSPORT_NUMBER).isInformation());
            if (formProperty.get(CustomFormConstants.PASSPORT_NUMBER).isInformation()) {
                new KpiToolTip(passportNumber, formProperty.get(CustomFormConstants.PASSPORT_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, getTitle(wfmStrings.passportNumber()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE) != null) {
            addField(CustomFormConstants.PASSPORT_ISSUE, passportIssue, getTitle(formProperty.get(CustomFormConstants.PASSPORT_ISSUE).isChanged() ?
                            formProperty.get(CustomFormConstants.PASSPORT_ISSUE).getTitle() : wfmStrings.passportIssueBy(), formProperty.get(CustomFormConstants.PASSPORT_ISSUE).isRequired()), false,
                    formProperty.get(CustomFormConstants.PASSPORT_ISSUE).isInformation());
            if (formProperty.get(CustomFormConstants.PASSPORT_ISSUE).isInformation()) {
                new KpiToolTip(passportIssue, formProperty.get(CustomFormConstants.PASSPORT_ISSUE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PASSPORT_ISSUE, passportIssue, getTitle(wfmStrings.passportIssueBy()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE) != null) {
            addField(CustomFormConstants.PASSPORT_ISSUE_DATE, passportIssueDate, getTitle(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).isChanged() ?
                            formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getTitle() : wfmStrings.passportIssueDate(), formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).isRequired()), false,
                    formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).isInformation());
            if (formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).isInformation()) {
                new KpiToolTip(passportIssueDate, formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PASSPORT_ISSUE_DATE, passportIssueDate, getTitle(wfmStrings.passportIssueDate()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE) != null) {
            addField(CustomFormConstants.PASSPORT_EXPIRY_DATE, passportExpiryDate, getTitle(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).isChanged() ?
                            formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getTitle() : wfmStrings.passportExpireDate(), formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).isRequired()), false,
                    formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).isInformation());
            if (formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).isInformation()) {
                new KpiToolTip(passportExpiryDate, formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PASSPORT_EXPIRY_DATE, passportExpiryDate, getTitle(wfmStrings.passportExpireDate()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.INSURANCE_NUMBER) != null) {
            addField(CustomFormConstants.INSURANCE_NUMBER, insuranceNumber, getTitle(formProperty.get(CustomFormConstants.INSURANCE_NUMBER).isChanged() ?
                            formProperty.get(CustomFormConstants.INSURANCE_NUMBER).getTitle() : wfmStrings.insuranseNumber(), formProperty.get(CustomFormConstants.INSURANCE_NUMBER).isRequired()), false,
                    formProperty.get(CustomFormConstants.INSURANCE_NUMBER).isInformation());
            if (formProperty.get(CustomFormConstants.INSURANCE_NUMBER).isInformation()) {
                new KpiToolTip(insuranceNumber, formProperty.get(CustomFormConstants.INSURANCE_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.INSURANCE_NUMBER, insuranceNumber, getTitle(wfmStrings.insuranseNumber()));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_NUMBER) != null) {
            addField(CustomFormConstants.VISA_NUMBER, visaNumber, getTitle(formProperty.get(CustomFormConstants.VISA_NUMBER).isChanged() ?
                            formProperty.get(CustomFormConstants.VISA_NUMBER).getTitle() : wfmStrings.visaNumber(), formProperty.get(CustomFormConstants.VISA_NUMBER).isRequired()), false,
                    formProperty.get(CustomFormConstants.VISA_NUMBER).isInformation());
            if (formProperty.get(CustomFormConstants.VISA_NUMBER).isInformation()) {
                new KpiToolTip(visaNumber, formProperty.get(CustomFormConstants.VISA_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.VISA_NUMBER, visaNumber, getTitle(wfmStrings.visaNumber()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.WPS_NUMBER) != null) {
            addField(CustomFormConstants.WPS_NUMBER, wpsNumber, getTitle(formProperty.get(CustomFormConstants.WPS_NUMBER).isChanged() ?
                            formProperty.get(CustomFormConstants.WPS_NUMBER).getTitle() : wfmStrings.wpsNumber(), formProperty.get(CustomFormConstants.WPS_NUMBER).isRequired()), false,
                    formProperty.get(CustomFormConstants.WPS_NUMBER).isInformation());
            if (formProperty.get(CustomFormConstants.WPS_NUMBER).isInformation()) {
                new KpiToolTip(wpsNumber, formProperty.get(CustomFormConstants.WPS_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.WPS_NUMBER, wpsNumber, getTitle(!"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_ISSUE_DATE) != null) {
            addField(CustomFormConstants.VISA_ISSUE_DATE, visaIssueDate, getTitle(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).isChanged() ?
                            formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getTitle() : wfmStrings.visaIssueDate(), formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).isRequired()), false,
                    formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).isInformation());
            if (formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).isInformation()) {
                new KpiToolTip(visaIssueDate, formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.VISA_ISSUE_DATE, visaIssueDate, getTitle(wfmStrings.visaIssueDate()));
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_VISA_EXPIRATION_DATE) || (employeeID != null && Utils.getUserID().equals(employeeID))) {

            if (formProperty != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE) != null) {
                addField(CustomFormConstants.VISA_EXPIRATION_DATE, visaExpirationDate, getTitle(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).isChanged() ?
                                formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getTitle() : wfmStrings.visaExpirationDate(), formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).isRequired()), false,
                        formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).isInformation());
                if (formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).isInformation()) {
                    new KpiToolTip(visaExpirationDate, formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.VISA_EXPIRATION_DATE, visaExpirationDate, getTitle(wfmStrings.visaExpirationDate()));
            }
            visaExpirationDate.addStyleName(test_code_ID_name + "visa_expiration_date");
        }
        //visa expiration date reminder
        if (Utils.hasPermission(PermissionConstants.HRMS_VISA_EXPIRATION_DATE_REMINDER)) {

            if (formProperty != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER) != null) {
                addField(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER, visaExpirationDateReminderTable, getTitle(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).isChanged() ?
                                formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).getTitle() : wfmStrings.setExpirationReminder(), formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).isRequired()), false,
                        formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).isInformation());
                if (formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).isInformation()) {
                    new KpiToolTip(visaExpirationDateReminderTable, formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).getInformationText());
                }
            } else {
                addField(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER, visaExpirationDateReminderTable, getTitle(wfmStrings.setExpirationReminder()));
            }
            visaExpirationDateReminderTable.addStyleName(test_code_ID_name + "visa_expiration_date_reminder");
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE) != null) {
            addField(CustomFormConstants.INSURANCE_EXPIRY_DATE, medicalInsuranceExDate, getTitle(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).isChanged() ?
                            formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getTitle() : wfmStrings.insuranceExpiryDate(), formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).isRequired()), false,
                    formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).isInformation());
            if (formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).isInformation()) {
                new KpiToolTip(medicalInsuranceExDate, formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.INSURANCE_EXPIRY_DATE, medicalInsuranceExDate, getTitle(wfmStrings.insuranceExpiryDate()));
        }
    }

    protected void drawPaymentDeductionCategoryTable() {
        if (Utils.hasPermission(HRMS_PAYROLL_DEDUCTION_CATEGORIES)) {
            addTitleField(CustomFormConstants.PAYMENT_DEDUCTION_INFORMATION, wfmStrings.paymentDeductionCategoryTable());
            addField(CustomFormConstants.PAYMENT_TABLE, paymentsTable, null);
            addField(CustomFormConstants.DEDUCTION_TABLE, deductionsTable, null);
            addField(CustomFormConstants.TAX_TABLE, taxTable, null);
            addField(CustomFormConstants.LOAN_TABLE, loansTable, null);
            addField(CustomForm.EMPLOYER_CONTRIBUTION, employerContributionTable, null);
        }
    }

    protected void drawCourses() {
        //courses
        if (fromSectionTCInstructor) {
            addTitleField(CustomFormConstants.INSTRUCTOR_COURSES, wfmStrings.courses());
            addField(CustomFormConstants.COURSES, courses, getTitle(wfmStrings.courses()));
        }
    }

    protected void drawCustomFields() {
        String customPermission = fromSectionPMEmployeeEdit ? PermissionConstants.PM_SHOW_ADDITIONAL_INFORMATION : PermissionConstants.HRMS_SHOW_ADDITIONAL_INFORMATION;
        if (Utils.hasPermission(customPermission)) {
            showCustomFields = true;
            CommonService.App.get().getCompanyCustomFields(fromSectionTCInstructor ? ViewName.Instructor : ViewName.Employee, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                public void failure(Throwable throwable) {

                }

                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        companyCustomFieldItems = result;
                        getCustomFieldUtil().setCompanyCustomFieldItems(companyCustomFieldItems);
                        drawEmployeeCustomFields();
                    }
                }
            });

            CommonService.App.get().getCompanyCustomFields(ViewName.EmployeeItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
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

            CommonService.App.get().getCompanyAllCustomFields(ViewName.ExperienceItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                @Override
                public void failure(Throwable throwable) {

                }

                @Override
                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        for (CompanyCustomFieldItem item : result) {
                            experienceItemCFs.put(item.getColumnCode(), item);
                        }
                    }
                    drawExperienceItemTable();
                }
            });
        }
    }

    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.HRMS_EMPLOYEE_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        GeneralEmployeeEditForm.this.configMap = result;

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
                        getItemTableWebhooks(editableTable, ItemTableEnum.EMPLOYEE_CUSTOM_ITEM, fieldID);
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
                    com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker d = new com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker(cfItem);
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

    private CompanyCustomFieldItem getCustomFieldItem(List<CompanyCustomFieldItem> companyCustomFieldItems, String columnCode) {
        return companyCustomFieldItems.stream()
                .filter(item -> columnCode.equals(item.getColumnCode()))
                .findFirst()
                .orElse(new CompanyCustomFieldItem());
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


    protected void drawExperienceItemTable() {
        addTitleField(CustomFormConstants.EXPERIENCE, "");
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.EXPERIENCE_ITEM_TABLE, new AbstractAsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {
                if (result != null) {
                    for (ColumnConfigs cc : result) {
                        if (cc.isSelected()) {
                            experienceColumnsMap.put(cc.getCode(), cc);
                        }
                    }
                }

                experienceTable = new EditableTable(getExperienceColumns(experienceColumnsMap), true, true);
                experienceTable.setDraggable(true);
                experienceTable.ensureDebugId("experience_item_table");
                experienceTable.setWidth("100%");
                experienceTable.setListener(new EditableTableListener() {
                    @Override
                    public void addRow() {
                        experienceTable.addRow(widgets(new ExperienceTableItems()));
                    }

                    @Override
                    public void removeRow() {

                    }
                });


                for (int i = 0; i < 1; i++) {
                    experienceTable.addRow(widgets(new ExperienceTableItems()));
                }

                addField(EXPERIENCE, experienceTable, "");
                getItemTableWebhooks(experienceTable, ItemTableEnum.EXPERIENCE_ITEM_TABLE, null);
            }
        });
    }


    private Widget[] widgets(ExperienceTableItems item) {
        int index1 = 0;
        ArrayList<Widget> widgets = new ArrayList<>();
        for (String columnCode : experienceColumnsMap.keySet()) {
            if (ItemTableConstants.HIRE_DATE.equals(columnCode)) {
                CustomDatePicker hireDate = new CustomDatePicker();
                hireDate.setWidth("100%");
                hireDate.setDate(item.getHireDate());
                hireDate.setTitle(columnCode);
                widgets.add(hireDate);
            } else if (ItemTableConstants.RESIGN_DATE.equals(columnCode)) {
                CustomDatePicker resignDate = new CustomDatePicker();
                resignDate.setWidth("100%");
                resignDate.setDate(item.getResignDate());
                resignDate.setTitle(columnCode);
                resignDate.addChangeHandler(event -> {
                    CustomDatePicker hireDate = (CustomDatePicker) experienceTable.getColumnById(experienceTable.getGrid().getCurrentRow(), ItemTableConstants.HIRE_DATE);
                    if (resignDate.getDate().compareTo(hireDate.getDate()) < 0) {
                        Info.warn(wfmStrings.resignationDateCannotBeEarlierThanHireDate());
                        resignDate.setDate(null);
                    }
                });
                widgets.add(resignDate);
            } else if (ItemTableConstants.INDUSTRY.equals(columnCode)) {
                ReferenceLookUp industry = new ReferenceLookUp(_COMPANY_WORKAREA);
                industry.setWidth("100%");
                industry.setSelected(item.getIndustry());
                industry.setTitle(columnCode);
                widgets.add(industry);
            } else if (ItemTableConstants.POSITION.equals(columnCode)) {
                CustomCellTextBox position = new CustomCellTextBox();
                position.setWidth("100%");
                position.setText(item.getPosition());
                position.setTitle(columnCode);
                widgets.add(position);
            } else if (ItemTableConstants.DEPARTMENT.equals(columnCode)) {
                CustomCellTextBox department = new CustomCellTextBox();
                department.setWidth("100%");
                department.setText(item.getDepartment());
                department.setTitle(columnCode);
                widgets.add(department);
            } else if (ItemTableConstants.ORGANIZATION.equals(columnCode)) {
                CustomCellTextBox organization = new CustomCellTextBox();
                organization.setWidth("100%");
                organization.setText(item.getOrganization());
                organization.setTitle(columnCode);
                widgets.add(organization);
            } else if (experienceItemCFs.containsKey(columnCode)) {

                CompanyCustomFieldItem cfItem = experienceItemCFs.get(columnCode);
                CompanyCustomFieldItem companyCustomFieldItem = setCustomFieldValue(item.getItemCustomFields(), cfItem);

                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomTextBoxField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                    CustomTextAreaField customTextAreaField = new CustomTextAreaField(companyCustomFieldItem);
                    customTextAreaField.hideCharacterLimitPanel();
                    Validation.addAutoResizeListenerToTextArea(customTextAreaField.getTextArea());
                    widgets.add(customTextAreaField);
                } else if (Constants.UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomPercentageField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomDropDownField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomDateTime(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomFieldLookUpField(companyCustomFieldItem));
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    widgets.add(new CustomFieldMultiLookUpField(companyCustomFieldItem));
                }

                if (item.getItemCustomFields() != null && !item.getItemCustomFields().isEmpty()) {
                    CompanyCustomFieldItem fitem = companyCustomFieldItem;
                    if (fitem != null) {
                        ((CustomFieldInterface) widgets.get(index1)).setFieldItem(fitem);
                    }
                }
            }
            index1++;


        }
        return widgets.toArray(new Widget[]{});
    }

    private ColumnConfig[] getExperienceColumns(Map<String, ColumnConfigs> columnsMap) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        for (String cc : columnsMap.keySet()) {
            ColumnConfigs columnConfigs = columnsMap.get(cc);
            boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            ColumnConfig columnConfig;
            switch (cc) {
                case ItemTableConstants.HIRE_DATE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.HIRE_DATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.hireDate(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.RESIGN_DATE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.RESIGN_DATE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.resignationDate(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.INDUSTRY:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.INDUSTRY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.industry(), Utils.getColumnWidth(columnConfigs.getWidth(), 90), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.POSITION:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.POSITION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.position(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.DEPARTMENT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DEPARTMENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.department(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                case ItemTableConstants.ORGANIZATION:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.ORGANIZATION, columnConfigs.isChanged() ? columnConfigs.getTitle() : crmStrings.organization(), Utils.getColumnWidth(columnConfigs.getWidth(), 80), columnConfigs.isRequired(), Constants.LEFT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columns.add(columnConfig);
                    break;
                default:
                    columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), 100, columnsMap.get(cc).isRequired(), true);
                    if (columnsMap.get(cc).getWidth() != null && columnsMap.get(cc).getWidth() > 0) {
                        columnConfig.setWidth(columnsMap.get(cc).getWidth());
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                    }
                    if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
                        columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                        columns.add(columnConfig);

                        columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode() + "_DESCRIPTION", wfmStrings.description(), columnsMap.get(cc).getWidth() * 40 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                        columns.add(columnConfig);
                    } else if (UI_TYPE_LOOKUP.equals(columnsMap.get(cc).getUiType())) {
                        columnConfig = new ColumnConfig(LookUpCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                        columns.add(columnConfig);
                    } else {
                        columns.add(columnConfig);
                    }
                    break;
            }
        }
        return columns.toArray(new ColumnConfig[]{});
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


    public ExperienceTableItems[] getExperienceTableItems() {
        ArrayList<ExperienceTableItems> experienceTableItems = new ArrayList<>();
        for (int i = 0; i < experienceTable.getGrid().getRowCount(); i++) {
            ExperienceTableItems result = new ExperienceTableItems();
            Map<String, CompanyCustomFieldItem> itemCFsValues = new HashMap<>();

            if (profileItem.getExperienceTableItems() != null && profileItem.getExperienceTableItems()[i] != null) {
                result.setId(profileItem.getExperienceTableItems()[i].getId());
            }
            CustomDatePicker hireDate = (CustomDatePicker) experienceTable.getColumnById(i, ItemTableConstants.HIRE_DATE);
            if (hireDate != null) {
                result.setHireDate(hireDate.getDate());
            }

            CustomDatePicker resignDate = (CustomDatePicker) experienceTable.getColumnById(i, ItemTableConstants.RESIGN_DATE);
            if (resignDate != null) {
                result.setResignDate(resignDate.getDate());
            }

            ReferenceLookUp industry = (ReferenceLookUp) experienceTable.getColumnById(i, ItemTableConstants.INDUSTRY);
            if (industry != null) {
                result.setIndustry(industry.getSelectedItem());
            }

            CustomCellTextBox position = (CustomCellTextBox) experienceTable.getColumnById(i, ItemTableConstants.POSITION);
            if (position != null) {
                result.setPosition(position.getText());
            }

            CustomCellTextBox department = (CustomCellTextBox) experienceTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
            if (department != null) {
                result.setDepartment(department.getText());
            }

            CustomCellTextBox organization = (CustomCellTextBox) experienceTable.getColumnById(i, ItemTableConstants.ORGANIZATION);
            if (organization != null) {
                result.setOrganization(organization.getText());
            }


            if (experienceItemCFs != null && !experienceItemCFs.isEmpty()) {
                ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

                for (String key : experienceItemCFs.keySet()) {
                    CustomFieldInterface customField = (CustomFieldInterface) experienceTable.getColumnById(i, key);

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
            experienceTableItems.add(result);

        }
        return experienceTableItems.toArray(new ExperienceTableItems[]{});
    }

    protected void drawEmployeeInformation() {

        String employeeInformationTITLE = isCustomise ? wfmStrings.employeeInformation() : fromSectionTCInstructor ? wfmStrings.instructorDetails() : wfmStrings.personalInformation();
        addTitleField(CustomFormConstants.EMPLOYEE_INFORMATION, employeeInformationTITLE);

        //personal information
        addField(CustomFormConstants.PROFILE_PICTURE, profilePicture, null, true);
        firstNameWidget = new InputGroup(titl, firstName);
        if (formProperty != null && formProperty.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(CustomFormConstants.FIRST_NAME, firstNameWidget, getTitle(formProperty.get(CustomFormConstants.FIRST_NAME).isChanged() ? formProperty.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), formProperty.get(CustomFormConstants.FIRST_NAME).isRequired()), false,
                    formProperty.get(CustomFormConstants.FIRST_NAME).isInformation());
            firstName.setEnabled(!formProperty.get(CustomFormConstants.FIRST_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.FIRST_NAME).isInformation()) {
                new KpiToolTip(firstName, formProperty.get(CustomFormConstants.FIRST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.FIRST_NAME, firstNameWidget, getTitle(wfmStrings.firstName(), true));
        }

        addField(CustomFormConstants.OTHER_NAME, otherName, getTitle(wfmStrings.otherName()));
        if (formProperty != null && formProperty.get(CustomFormConstants.LAST_NAME) != null) {
            addField(CustomFormConstants.LAST_NAME, lastName, getTitle(formProperty.get(CustomFormConstants.LAST_NAME).isChanged() ? formProperty.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), formProperty.get(CustomFormConstants.LAST_NAME).isRequired()), false,
                    formProperty.get(CustomFormConstants.LAST_NAME).isInformation());
            lastName.setEnabled(!formProperty.get(CustomFormConstants.LAST_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.LAST_NAME).isInformation()) {
                new KpiToolTip(lastName, formProperty.get(CustomFormConstants.LAST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.LAST_NAME, lastName, getTitle(wfmStrings.lastName()));
        }
        addField(CustomFormConstants.DRIVER_ID, driverID, getTitle("Driver ID", true));
        if (formProperty != null && formProperty.get(CustomFormConstants.MIDDLE_NAME) != null) {
            addField(CustomFormConstants.MIDDLE_NAME, middleName, getTitle(formProperty.get(CustomFormConstants.MIDDLE_NAME).isChanged() ? formProperty.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName(), formProperty.get(CustomFormConstants.MIDDLE_NAME).isRequired()), false,
                    formProperty.get(CustomFormConstants.MIDDLE_NAME).isInformation());
            middleName.setEnabled(!formProperty.get(CustomFormConstants.MIDDLE_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.MIDDLE_NAME).isInformation()) {
                new KpiToolTip(middleName, formProperty.get(CustomFormConstants.MIDDLE_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.MIDDLE_NAME, middleName, getTitle(wfmStrings.middleName()));
        }
        String birthDayPermission = fromSectionPMEmployeeEdit ? PermissionConstants.PM_SHOW_EMPLOYEE_BIRTH_DAY : PermissionConstants.HRMS_SHOW_EMPLOYEE_BIRTH_DAY;
        if (Utils.hasPermission(birthDayPermission) || (employeeID != null && Utils.getUserID().equals(employeeID))) {
            if (formProperty != null && formProperty.get(CustomFormConstants.BIRTH_DAY) != null) {
                addField(CustomFormConstants.BIRTH_DAY, birthDatePicker, getTitle(formProperty.get(CustomFormConstants.BIRTH_DAY).isChanged() ? formProperty.get(CustomFormConstants.BIRTH_DAY).getTitle() : wfmStrings.dateOfBirth(), formProperty.get(CustomFormConstants.BIRTH_DAY).isRequired()), false,
                        formProperty.get(CustomFormConstants.MIDDLE_NAME).isInformation());
                birthDatePicker.setEnabled(!formProperty.get(CustomFormConstants.BIRTH_DAY).isDisabled());
                if (formProperty.get(CustomFormConstants.BIRTH_DAY).isInformation()) {
                    new KpiToolTip(birthDatePicker, formProperty.get(CustomFormConstants.BIRTH_DAY).getInformationText());
                }
            } else {
                addField(CustomFormConstants.BIRTH_DAY, birthDatePicker, getTitle(wfmStrings.dateOfBirth()));
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GENDER) != null) {
            addField(CustomFormConstants.GENDER, genderTable, getTitle(formProperty.get(CustomFormConstants.GENDER).isChanged() ? formProperty.get(CustomFormConstants.GENDER).getTitle() : wfmStrings.gender(), formProperty.get(CustomFormConstants.GENDER).isRequired()), false,
                    formProperty.get(CustomFormConstants.GENDER).isInformation());
            if (formProperty.get(CustomFormConstants.GENDER).isInformation()) {
                new KpiToolTip(genderTable, formProperty.get(CustomFormConstants.GENDER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GENDER, genderTable, getTitle(wfmStrings.gender()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.NATIONALITY) != null) {
            addField(CustomFormConstants.NATIONALITY, nationality, getTitle(formProperty.get(CustomFormConstants.NATIONALITY).isChanged() ?
                            formProperty.get(CustomFormConstants.NATIONALITY).getTitle() :
                            wfmStrings.nationality(), formProperty.get(CustomFormConstants.NATIONALITY).isRequired()), false,
                    formProperty.get(CustomFormConstants.NATIONALITY).isInformation());
            nationality.setEnabled(!formProperty.get(CustomFormConstants.NATIONALITY).isDisabled());
            if (formProperty.get(CustomFormConstants.NATIONALITY).isInformation()) {
                new KpiToolTip(nationality, formProperty.get(CustomFormConstants.NATIONALITY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NATIONALITY, nationality, getTitle(wfmStrings.nationality()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MARTIAL_STATUS) != null) {
            addField(CustomFormConstants.MARTIAL_STATUS, martialStatus, getTitle(formProperty.get(CustomFormConstants.MARTIAL_STATUS).isChanged() ?
                            formProperty.get(CustomFormConstants.MARTIAL_STATUS).getTitle() :
                            wfmStrings.maritalStatus(), formProperty.get(CustomFormConstants.MARTIAL_STATUS).isRequired()), false,
                    formProperty.get(CustomFormConstants.MARTIAL_STATUS).isInformation());
            martialStatus.setEnabled(!formProperty.get(CustomFormConstants.MARTIAL_STATUS).isDisabled());
            if (formProperty.get(CustomFormConstants.MARTIAL_STATUS).isInformation()) {
                new KpiToolTip(martialStatus, formProperty.get(CustomFormConstants.MARTIAL_STATUS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.MARTIAL_STATUS, martialStatus, getTitle(wfmStrings.maritalStatus()));
        }
        /*addField(CustomFormConstants.LANGUAGE, languages, getTitle(wfmStrings.spokenLanguages()));*/
        if (Utils.isMonthlyTimeSheetEnable()) {


            if (formProperty != null && formProperty.get(CustomFormConstants.PAYMENT_METHOD) != null) {
                addField(CustomFormConstants.PAYMENT_METHOD, paymentMethodTable, getTitle(formProperty.get(CustomFormConstants.PAYMENT_METHOD).isChanged() ?
                                formProperty.get(CustomFormConstants.PAYMENT_METHOD).getTitle() :
                                wfmStrings.salaryLevel(), formProperty.get(CustomFormConstants.PAYMENT_METHOD).isRequired()), false,
                        formProperty.get(CustomFormConstants.PAYMENT_METHOD).isInformation());
                if (formProperty.get(CustomFormConstants.PAYMENT_METHOD).isInformation()) {
                    new KpiToolTip(paymentMethodTable, formProperty.get(CustomFormConstants.PAYMENT_METHOD).getInformationText());
                }

            } else {
                addField(CustomFormConstants.PAYMENT_METHOD, paymentMethodTable, getTitle(wfmStrings.salaryLevel()));
            }
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.LANGUAGE) != null) {
            addField(CustomFormConstants.LANGUAGE, languagesWidget, getTitle(formProperty.get(CustomFormConstants.LANGUAGE).isChanged() ?
                            formProperty.get(CustomFormConstants.LANGUAGE).getTitle() :
                            wfmStrings.spokenLanguages(), formProperty.get(CustomFormConstants.LANGUAGE).isRequired()), false,
                    formProperty.get(CustomFormConstants.LANGUAGE).isInformation());
            if (formProperty.get(CustomFormConstants.LANGUAGE).isInformation()) {
                new KpiToolTip(languagesWidget, formProperty.get(CustomFormConstants.LANGUAGE).getInformationText());
            }
        } else {
            addField(LANGUAGE, languagesWidget, getTitle(wfmStrings.spokenLanguages()));
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE) != null) {
            addField(CustomFormConstants.EMPLOYEE_DEGREE, employeeDegree, getTitle(formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).isChanged() ?
                            formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).getTitle() :
                            wfmStrings.degree(), formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).isRequired()), false,
                    formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).isInformation());
            if (formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).isInformation()) {
                new KpiToolTip(employeeDegree, formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).getInformationText());
            }
        } else {
            addField(EMPLOYEE_DEGREE, employeeDegree, getTitle(wfmStrings.degree()));
        }
    }

    protected void drawEmploymentInformation() {
        addTitleField(CustomFormConstants.EMPLOYMENT_INFORMATION, getTitle(wfmStrings.employmentInformation()));
        if (!fromSectionPMEmployeeEdit) {
            addField(CustomFormConstants.EMPLOYEE_CODE, empCode, getTitle(formProperty.get(CustomFormConstants.EMPLOYEE_CODE).isChanged() ? formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getTitle() : wfmStrings.employeeCode(),
                            formProperty.get(CustomFormConstants.EMPLOYEE_CODE).isRequired()), false,
                    formProperty.get(CustomFormConstants.EMPLOYEE_CODE).isInformation());
            empCode.setEnabled(!formProperty.get(CustomFormConstants.EMPLOYEE_CODE).isDisabled());
            if (formProperty.get(CustomFormConstants.EMPLOYEE_CODE).isInformation()) {
                new KpiToolTip(empCode, formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.EMPLOYEE_CODE, empCode, getTitle(wfmStrings.employeeCode()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(CustomFormConstants.DEPARTMENT, departmentContainer, getTitle(formProperty.get(CustomFormConstants.DEPARTMENT).isChanged() ? formProperty.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department(),
                            formProperty.get(CustomFormConstants.DEPARTMENT).isRequired()), false,
                    formProperty.get(CustomFormConstants.DEPARTMENT).isInformation());
            departmentContainer.setEnabled(!formProperty.get(CustomFormConstants.DEPARTMENT).isDisabled());
            if (formProperty.get(CustomFormConstants.DEPARTMENT).isInformation()) {
                new KpiToolTip(departmentContainer, formProperty.get(CustomFormConstants.DEPARTMENT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DEPARTMENT, departmentContainer, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.POSITION) != null) {
            addField(CustomFormConstants.POSITION, positionContainer, getTitle(formProperty.get(CustomFormConstants.POSITION).isChanged() ? formProperty.get(CustomFormConstants.POSITION).getTitle() : wfmStrings.position(),
                            formProperty.get(CustomFormConstants.POSITION).isRequired()), false,
                    formProperty.get(CustomFormConstants.POSITION).isInformation());
            positionPanel.setEnabled(!formProperty.get(CustomFormConstants.POSITION).isDisabled());
            if (formProperty.get(CustomFormConstants.POSITION).isInformation()) {
                new KpiToolTip(positionContainer, formProperty.get(CustomFormConstants.POSITION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.POSITION, positionContainer, getTitle(wfmStrings.position()));
        }
        positionPanel.addStyleName(test_code_ID_name + "position");
        if (!fromSectionTCInstructor) {
            if (formProperty != null && formProperty.get(CustomFormConstants.LOCATION_FIELD) != null) {
                addField(CustomFormConstants.LOCATION_FIELD, locationPanel1, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()),
                                formProperty.get(CustomFormConstants.LOCATION_FIELD).isRequired()), false,
                        formProperty.get(CustomFormConstants.LOCATION_FIELD).isInformation());
                locations.setEnabled(!formProperty.get(CustomFormConstants.LOCATION_FIELD).isDisabled());
                locationPanel1.setEnabled(!formProperty.get(CustomFormConstants.LOCATION_FIELD).isDisabled());
                if (formProperty.get(LOCATION_FIELD).isInformation()) {
                    new KpiToolTip(locations, formProperty.get(CustomFormConstants.LOCATION_FIELD).getInformationText());
                }

            } else {
                boolean isRequired = formProperty.get(LOCATION_FIELD) != null ? formProperty.get(LOCATION_FIELD).isRequired() : false;
                addField(CustomFormConstants.LOCATION_FIELD, locationPanel1, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), isRequired));
            }
            locationPanel1.addStyleName(test_code_ID_name + "locations");
        }
        if (!fromSectionPMEmployeeEdit) {
            if (formProperty != null && formProperty.get(CustomFormConstants.SUPERVISOR) != null) {
                addField(CustomFormConstants.SUPERVISOR, reportsTo, getTitle(formProperty.get(CustomFormConstants.SUPERVISOR).isChanged() ? formProperty.get(CustomFormConstants.SUPERVISOR).getTitle() : wfmStrings.supervisor(),
                                formProperty.get(CustomFormConstants.SUPERVISOR).isRequired()), false,
                        formProperty.get(CustomFormConstants.SUPERVISOR).isInformation());
                reportsTo.setEnabled(!formProperty.get(CustomFormConstants.SUPERVISOR).isDisabled());
                if (formProperty.get(CustomFormConstants.SUPERVISOR).isInformation()) {
                    new KpiToolTip(reportsTo, formProperty.get(CustomFormConstants.SUPERVISOR).getInformationText());
                }
            } else {
                addField(CustomFormConstants.SUPERVISOR, reportsTo, getTitle(wfmStrings.supervisor()));
            }

            reportsTo.addStyleName(test_code_ID_name + "supervisor");
        }
        if (Utils.hasAccessToDefaultEmployeeRate(employeeID)) {
            if (formProperty != null && formProperty.get(CustomFormConstants.WAGE_RATE) != null) {
                addField(CustomFormConstants.WAGE_RATE, wageRateWithCheckBox, getTitle(formProperty.get(CustomFormConstants.WAGE_RATE).isChanged() ? formProperty.get(CustomFormConstants.WAGE_RATE).getTitle() : wfmStrings.wageRate(),
                                formProperty.get(CustomFormConstants.WAGE_RATE).isRequired()), false,
                        formProperty.get(CustomFormConstants.WAGE_RATE).isInformation());
                wageRate.setEnabled(!formProperty.get(CustomFormConstants.WAGE_RATE).isDisabled());
                applyWageRate.setEnabled(!formProperty.get(CustomFormConstants.WAGE_RATE).isDisabled());
                if (formProperty.get(CustomFormConstants.WAGE_RATE).isInformation()) {
                    new KpiToolTip(wageRate, formProperty.get(CustomFormConstants.WAGE_RATE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.WAGE_RATE, wageRateWithCheckBox, getTitle(wfmStrings.wageRate()));
            }
            if (formProperty != null && formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE) != null) {
                addField(CustomFormConstants.CLIENT_CHARGE_RATE, clientChargeRateWithCheckBox, getTitle(formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isChanged() ? formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).getTitle() : wfmStrings.clientChargeRate(),
                                formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isRequired()), false,
                        formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isInformation());
                clientChargeRate.setEnabled(!formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isDisabled());
                applyClientChargeRate.setEnabled(!formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isDisabled());
                if (formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isInformation()) {
                    new KpiToolTip(clientChargeRate, formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.CLIENT_CHARGE_RATE, clientChargeRateWithCheckBox, getTitle(wfmStrings.clientChargeRate()));
            }
        }
        if (!fromSectionPMEmployeeEdit) {

            if (formProperty != null && formProperty.get(CustomFormConstants.HIRE_DATE) != null) {
                addField(CustomFormConstants.HIRE_DATE, hireDatePicker, getTitle(formProperty.get(CustomFormConstants.HIRE_DATE).isChanged() ? formProperty.get(CustomFormConstants.HIRE_DATE).getTitle() : wfmStrings.hireDate(),
                                formProperty.get(CustomFormConstants.HIRE_DATE).isRequired()), false,
                        formProperty.get(CustomFormConstants.HIRE_DATE).isInformation());
                hireDatePicker.setEnabled(!formProperty.get(CustomFormConstants.HIRE_DATE).isDisabled());
                if (formProperty.get(CustomFormConstants.HIRE_DATE).isInformation()) {
                    new KpiToolTip(hireDatePicker, formProperty.get(CustomFormConstants.HIRE_DATE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.HIRE_DATE, hireDatePicker, getTitle(wfmStrings.hireDate()));
            }
            hireDatePicker.addStyleName(test_code_ID_name + "hire_date");


            if (formProperty != null && formProperty.get(CustomFormConstants.RESIGNATION_DATE) != null) {
                addField(CustomFormConstants.RESIGNATION_DATE, fireDatePicker, getTitle(formProperty.get(CustomFormConstants.RESIGNATION_DATE).isChanged() ? formProperty.get(CustomFormConstants.RESIGNATION_DATE).getTitle() : wfmStrings.resignationDate(),
                                formProperty.get(CustomFormConstants.RESIGNATION_DATE).isRequired()), false,
                        formProperty.get(CustomFormConstants.RESIGNATION_DATE).isInformation());
                fireDatePicker.setEnabled(!formProperty.get(CustomFormConstants.RESIGNATION_DATE).isDisabled());
                if (formProperty.get(CustomFormConstants.RESIGNATION_DATE).isInformation()) {
                    new KpiToolTip(fireDatePicker, formProperty.get(CustomFormConstants.RESIGNATION_DATE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.RESIGNATION_DATE, fireDatePicker, getTitle(wfmStrings.resignationDate()));
            }

            fireDatePicker.addStyleName(test_code_ID_name + "resignation_date");
            InputGroup widgets = new InputGroup(termsOfContractBox, monthYearBox);
            if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS) != null) {
                addField(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS, widgets, getTitle(formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).isChanged() ? formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).getTitle() : wfmStrings.employmentContractTerms(),
                                formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).isRequired()), false,
                        formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).isInformation());
                widgets.setEnabled(!formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).isDisabled());
                if (formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).isInformation()) {
                    new KpiToolTip(widgets, formProperty.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).getInformationText());
                }
            } else {
                addField(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS, widgets, getTitle(wfmStrings.employmentContractTerms()));
            }


            if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYMENT_MODE) != null) {
                addField(CustomFormConstants.EMPLOYMENT_MODE, empMode, getTitle(formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).isChanged() ? formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).getTitle() : wfmStrings.employmentMode(),
                                formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).isRequired()), false,
                        formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).isInformation());
                empMode.setEnabled(!formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).isDisabled());
                if (formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).isInformation()) {
                    new KpiToolTip(empMode, formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.EMPLOYMENT_MODE, empMode, getTitle(wfmStrings.employmentMode()));
            }

            empMode.addStyleName(test_code_ID_name + "employment_mode");


            if (formProperty != null && formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE) != null) {
                addField(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDays, getTitle(formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isChanged() ? formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).getTitle() : wfmStrings.openingBalanceForAnnualLeave(),
                                formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isRequired()), false,
                        formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isInformation());
                openingBalanceDays.setEnabled(!formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isDisabled());
                if (formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isInformation()) {
                    new KpiToolTip(openingBalanceDays, formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).getInformationText());
                }
            } else {
                addField(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDays, getTitle(wfmStrings.openingBalanceForAnnualLeave()));
            }

            if (formProperty != null && formProperty.get(CustomFormConstants.PROBATION_DAYS) != null) {
                addField(CustomFormConstants.PROBATION_DAYS, probationDays, getTitle(formProperty.get(CustomFormConstants.PROBATION_DAYS).isChanged() ? formProperty.get(CustomFormConstants.PROBATION_DAYS).getTitle() : wfmStrings.probationPeriodDays(),
                                formProperty.get(CustomFormConstants.PROBATION_DAYS).isRequired()), false,
                        formProperty.get(CustomFormConstants.PROBATION_DAYS).isInformation());
                probationDays.setEnabled(!formProperty.get(CustomFormConstants.PROBATION_DAYS).isDisabled());
                if (formProperty.get(CustomFormConstants.PROBATION_DAYS).isInformation()) {
                    new KpiToolTip(probationDays, formProperty.get(CustomFormConstants.PROBATION_DAYS).getInformationText());
                }
            } else {
                addField(CustomFormConstants.PROBATION_DAYS, probationDays, getTitle(wfmStrings.probationPeriodDays()));
            }


            if (formProperty != null && formProperty.get(CustomFormConstants.QUALIFICATION) != null) {
                addField(CustomFormConstants.QUALIFICATION, employeeQualification, getTitle(formProperty.get(CustomFormConstants.QUALIFICATION).isChanged() ? formProperty.get(CustomFormConstants.QUALIFICATION).getTitle() : wfmStrings.qualification(),
                                formProperty.get(CustomFormConstants.QUALIFICATION).isRequired()), false,
                        formProperty.get(CustomFormConstants.QUALIFICATION).isInformation());
                employeeQualification.setEnabled(!formProperty.get(CustomFormConstants.QUALIFICATION).isDisabled());
                if (formProperty.get(CustomFormConstants.QUALIFICATION).isInformation()) {
                    new KpiToolTip(employeeQualification, formProperty.get(CustomFormConstants.QUALIFICATION).getInformationText());
                }
            } else {
                addField(CustomFormConstants.QUALIFICATION, employeeQualification, getTitle(wfmStrings.qualification()));
            }

            if (formProperty != null && formProperty.get(CustomFormConstants.TIMESLOT) != null) {
                addField(CustomFormConstants.TIMESLOT, timeslot, getTitle(formProperty.get(CustomFormConstants.TIMESLOT).isChanged() ? formProperty.get(CustomFormConstants.TIMESLOT).getTitle() : wfmStrings.timeslot(),
                                formProperty.get(CustomFormConstants.TIMESLOT).isRequired()), false,
                        formProperty.get(CustomFormConstants.TIMESLOT).isInformation());
                timeslot.setEnabled(!formProperty.get(CustomFormConstants.TIMESLOT).isDisabled());
                if (formProperty.get(CustomFormConstants.TIMESLOT).isInformation()) {
                    new KpiToolTip(timeslot, formProperty.get(CustomFormConstants.TIMESLOT).getInformationText());
                }
            } else {
                addField(CustomFormConstants.TIMESLOT, timeslot, getTitle(wfmStrings.timeslot()));
            }

            if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
                if (formProperty != null && formProperty.get(CustomFormConstants.SALARY_MODE) != null) {
                    addField(CustomFormConstants.SALARY_MODE, salaryMode, getTitle(formProperty.get(CustomFormConstants.SALARY_MODE).isChanged() ?
                            formProperty.get(CustomFormConstants.SALARY_MODE).getTitle() :
                            wfmStrings.salaryMode(), formProperty.get(CustomFormConstants.SALARY_MODE).isRequired()));
                    salaryMode.setEnabled(!formProperty.get(CustomFormConstants.SALARY_MODE).isDisabled());
                } else {
                    addField(CustomFormConstants.SALARY_MODE, salaryMode, getTitle(wfmStrings.salaryMode()));
                }
            }
        }
    }

    /**
     * Filled custom fields
     */
    protected void fillCustomFields() {
        if (showCustomFields) {
            if (employeeID != null || isFromPlacement) {
                getCustomFieldUtil().fillCustomFieldsWithData(profileItem.getCustomFields());
            }
        }
    }

    /**
     * Filled form data
     */
    protected void fillFormWithData() {

        if (fromSectionPMEmployeeEdit) {
            profileItem.setCreatedFrom(ContactListItem.REQUEST_FROM_PM_EMPLOYEE_EDIT);
            if (profileItem.getEditablePermission() != EDIT && !Utils.hasRole(ADMIN_LOCATION) && !employeeAddFromPayroll) {
                enableButton(false);
            }
        }
        //title list items
        titl.setItems(profileItem.getTitleList());
        fillFormWithDataContact(profileItem);
        isFromPlacement = profileItem.isFromPlacement();
        //gender
        if (profileItem.getGender() != null) {
            if (profileItem.getGender().equals(Constants.MALE)) {
                male.setValue(true);
            } else {
                female.setValue(true);
            }
        }
        if (profileItem.getPaymentMethod() != null && !"".equals(profileItem.getPaymentMethod())) {
            if (profileItem.getPaymentMethod().equals(wfmStrings.minSalary())) {
                minSalaryMethod.setValue(true);
            } else if (profileItem.getPaymentMethod().equals(wfmStrings.midSalary())) {
                midSalaryMethod.setValue(true);
            } else if (profileItem.getPaymentMethod().equals(wfmStrings.maxSalary())) {
                maxSalaryMethod.setValue(true);
            }
        }
        if (profileItem.getDriverID() != null) {
            driverID.setText(profileItem.getDriverID());
        }
        //martial status items
        if (profileItem.getMartialStatusList() != null) {
            martialStatus.setItems(profileItem.getMartialStatusList());
        }
        //martial status
        if (profileItem.getMartialStatusId() != null) {
            martialStatus.setSelected(profileItem.getMartialStatusId());
        }
        if (profileItem.getNationality() != null) {
            nationality.setText(profileItem.getNationality());
        }
        /*initLanguagesWidget(profileItem.getLanguages(), profileItem.getSpokenLanguages());*/

        //employment code
        if (Utils.hasGenericAccess(ENABLE_EMPLOYEE_CODE_INTEGER)) {
            NumberData numberData = new NumberData();
            numberData.setNumberString(profileItem.getNumberData().getNumberString());
            numberData.setNumberFormat("");
            if (profileItem.getObjectId() != null) {
                numberData.setIntNumber(0);
            }
            empCode.setNumberData(numberData);
        } else {
            empCode.setNumberData(profileItem.getNumberData());
        }
        /*if (!Utils.isNullOrEmpty(profileItem.getNumberData().getDelimiter())) {
            empCode.getTxtPrefix().setMaxLength(5 + profileItem.getNumberData().getDelimiter().toCharArray().length);
        } else {
            empCode.getTxtPrefix().setMaxLength(5);
        }*/
        if (profileItem.getSalaryMode() != null) {
            if (profileItem.getSalaryMode().equals(TARIFF_GRID) && !Utils.hasGenericAccess(LOCALE_PAYROLL)) {
                salaryMode.setSelected(salaryMode1);
            } else {
                salaryMode.setSelectedByDescription(profileItem.getSalaryMode());
            }
            salaryAmount.setEnabled(!profileItem.getSalaryMode().equals(TARIFF_GRID));
        } else {
            if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
                salaryMode.setSelected(salaryMode0);
                salaryAmount.setEnabled(false);
            } else {
                salaryMode.setSelected(salaryMode1);
                salaryAmount.setEnabled(true);
            }
        }
        //department items
//        pmDepartment.setItems(null, profileItem.getPmDepartmentItems());
        //department
        if (profileItem.getPmDepartmentID() != null) {
            pmDepartment.setSelected(new SelectItem(profileItem.getPmDepartmentID(), profileItem.getDepartment()));
        }
        //custom fields
        if (showCustomFields) {
            fillCustomFields();
        }
        //wage rate
        wageRate.setText(profileItem.getWageRate() != null ? profileItem.getWageRate().toString() : "0");
        //client charge rate
        clientChargeRate.setText(profileItem.getClientChargeRate() != null ? profileItem.getClientChargeRate().toString() : "0");
        //employee qualification
        employeeQualification.setItems(profileItem.getQualifications());
        if (profileItem.getQualificationID() != null) {
            employeeQualification.setSelected(profileItem.getQualificationID());
        }
        //employee timeslot
        timeslot.setItems(profileItem.getTimeslots());
        if (profileItem.getTimeslot() != null) {
            timeslot.setSelected(profileItem.getTimeslot());
        } else {
            timeslot.setSelected(profileItem.getDefaultTimeslot());
        }
        //employee status items
        if (profileItem.getStatusList() != null) {
            status.setItems(profileItem.getStatusList());
        }
        status.setEnabled(false);
        //employee status
        if (profileItem.getStatusId() != null) {
            status.setSelected(profileItem.getStatusId());
        }
        //hire date
        if (profileItem.getHireDate() != null) {
            hireDatePicker.setDate(profileItem.getHireDate().getNonConvertedDate());
        } else {
            hireDatePicker.clearSelected();
        }
        //resignation date
        if (profileItem.getFireDate() != null) {
            fireDatePicker.setDate(profileItem.getFireDate().getNonConvertedDate());
        } else {
            fireDatePicker.clearSelected();
        }
        //supervisor
        if (profileItem.getReportsToId() != null) {
            reportsTo.setSelected(profileItem.getReportsToId(), profileItem.getReportsTo());
        }
        //terms of contract (year)
        if (profileItem.getTermsOfContract() != null) {
            termsOfContractBox.setText(profileItem.getTermsOfContract().toString());
        }
        //terms of contract
        if (profileItem.getTermsOfCMonthORYear() != null) {
            monthYearBox.setSelected(profileItem.getTermsOfCMonthORYear());
        }
        //employment mode items
        if (profileItem.getEmpModeList() != null) {
            empMode.setItems(profileItem.getEmpModeList());
        }
        //employment mode
        if (profileItem.getEmpMode() != null && profileItem.getEmpModeId() != null) {
            empMode.setSelected(new SelectItem(profileItem.getEmpModeId(), profileItem.getEmpMode()));
        } else if (profileItem.getEmpModeId() != null) {
            empMode.setSelected(profileItem.getEmpModeId());
        }

        //salary amount
        if (profileItem.getSalaryAmount() != null) {
            salaryAmount.setText(extendedNumberFormat.format(profileItem.getSalaryAmount()));
        }
        if (profileItem.getJobTitleId() != null && jobTitle != null) {
            jobTitle.setSelected(profileItem.getJobTitleId());
        }
        //bank account information
        UserBankAccountData bankData = profileItem.getBankAccountData();
        if (bankData != null) {
            //bank name
            bankName.setText(bankData.getBankName());
            //bank address
            if (bankData.getBankAddress() != null) {
                bankAddress.setText(bankData.getBankAddress());
            }
            //account number
            accountNumber.setText(bankData.getAccountNumber());
            //account name
            if (bankData.getAccountName() != null) {
                accountName.setText(bankData.getAccountName());
            }
            //swift code
            if (bankData.getSwiftCode() != null) {
                swiftCode.setText(bankData.getSwiftCode());
            }
            //sort code
            sortCode.setText(bankData.getSortCode());
            //iBan code
            if (bankData.getIbanCode() != null) {
                iBanCode.setText(bankData.getIbanCode());
            }
            //Agentid
            if (bankData.getAgentID() != null) {
                agentID.setText(bankData.getAgentID());
            }
        }

        //position
        if (profileItem.getPositionId() != null) {
            positionPanel.setSelected(profileItem.getPositionId());
            if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                positionPanel.getFilterParametrs().setDepartmentId(departmentId);
            }
        }
        //location
        if (profileItem.getLocationId() != null) {
            locations.setSelected(profileItem.getLocation());
        }
    
        if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
            if (profileItem.getPositionId() != null) {
                positionPanel.setSelected(new SelectItem(profileItem.getPositionId(), profileItem.getPosition()));
                positionPanel.getFilterParametrs().setDepartmentId(departmentId);
            }
            if (profileItem.getPmDepartmentID() != null) {
                pmDepartment.getFilterParametrs().setLocationId(locations.getSelectedItemID());
                pmDepartment.setSelected(new SelectItem(profileItem.getPmDepartmentID(), profileItem.getDepartment()));
            }
        }
        //employee roles
        if (!fromSectionTCInstructor) {
            Arrays.sort(profileItem.getRoleList());
            mainRole.addItems(profileItem.getRoleList(), profileItem.getRoleId());
        }
        fingerprintDevices.addItems(profileItem.getFingerprintDeviceList(), profileItem.getFingerprintDeviceId());
        if (employeeID != null && EMPLOYEE_STATUS_NO_ACCCESS.equals(profileItem.getStatusCode())) {
            noAccess.setValue(true);
            mainRole.setVisible(false);
        } else {
            noAccess.setValue(profileItem.getNoAccess());
        }
        int remainingEssUserLimit = profileItem.getUserLimit() != null ? profileItem.getUserLimit()[Constants.ESS] : 0;
        if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET) && (remainingEssUserLimit > 0 || profileItem.getEss())) {
            addField(CustomFormConstants.ESS_USER, essUser, getTitle(wfmStrings.essUser()));
        }
        if (profileItem.getEss()) {
            essUser.setValue(true);
            status.setEnabled(false);
            mainRole.setVisible(false);
        }

        //courses
        if (profileItem.getCoursesMap() != null && profileItem.getCoursesMap().size() > 0 && courses != null) {
            courses.setItems(profileItem.getCoursesMap());
        }
        //Personal Identity Information
        if (profileItem.getPassportNumber() != null) {
            passportNumber.setText(profileItem.getPassportNumber());
        }
        if (profileItem.getPassportIssueItem() != null) {
            passportIssue.setSelected(profileItem.getPassportIssueItem());
        }
        if (profileItem.getPassportIssueDate() != null) {
            passportIssueDate.setDate(profileItem.getPassportIssueDate().getNonConvertedDate());
        }
        if (profileItem.getPassportExpiryDate() != null) {
            passportExpiryDate.setDate(profileItem.getPassportExpiryDate().getNonConvertedDate());
        }
        if (profileItem.getMedicalInsuranceExpireDate() != null) {
            medicalInsuranceExDate.setDate(profileItem.getMedicalInsuranceExpireDate().getNonConvertedDate());
        }
        visaNumber.setText(profileItem.getVisaNumber());
        if (profileItem.getVisaIssueDate() != null) {
            visaIssueDate.setDate(profileItem.getVisaIssueDate().getNonConvertedDate());
        }
        if (profileItem.getPayrollSettings().containsKey(WPS_NUMBER)) {
            wpsNumber.setText(profileItem.getPayrollSettings().get(WPS_NUMBER) != null ? profileItem.getPayrollSettings().get(WPS_NUMBER) : "");
        }

        if (profileItem.getOpeningBalanceDays() != null) {
            openingBalanceDays.setText(String.valueOf(profileItem.getOpeningBalanceDays()));
        } else {
            openingBalanceDays.setText(singlePointPrecisionNumberFormat.format(0.0));
        }
        if (profileItem.getProbationDays() != null) {
            probationDays.setText(String.valueOf(profileItem.getProbationDays()));
        } else {
            probationDays.setText(singlePointPrecisionNumberFormat.format(0.0));
        }

        if (employeeID != null) {
            fillTable(profileItem.getPaymentCategories(), PayrollConstants.CATEGORY_PAYMENT);
            fillTable(profileItem.getDeductionCategories(), PayrollConstants.CATEGORY_DEDUCTION);
            fillTable(profileItem.getTaxes(), PayrollConstants.CATEGORY_TAX);
            fillTable(profileItem.getLoanCategories(), PayrollConstants.CATEGORY_LOAN);
            fillTable(profileItem.getEmployerContributions(), PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);

            calculateBasicTotalSalary();
        }
        insuranceNumber.setText(profileItem.getInsuranceNumber());
        //visa expiration date
        if (profileItem.getVisaExpirationDate() != null && profileItem.getVisaExpirationDate().getNonConvertedDate() != null) {
            visaExpirationDate.setDate(profileItem.getVisaExpirationDate().getNonConvertedDate());
        }
        //visa expiration date reminder
        ArrayList<CalendarEventReminder> visaExpirationDateReminder = profileItem.getVisaExpirationDateReminder();
        if (visaExpirationDateReminder != null && visaExpirationDateReminder.size() > 0) {
            visaExpirationDateReminderTable.removeAllRows();
            for (CalendarEventReminder reminder : visaExpirationDateReminder) {
                visaExpirationDateReminderTable.addWidgets(getVisaExpirationDateReminderWidgets(reminder.getReminderTimes()));
            }
        }
        languagesWidget.setLanguages(profileItem.getSpokingLanguages());
        employeeDegree.setItems(profileItem.getemployeeDegrees());
        employeeDegree.setSelected(profileItem.getemployeeDegree() != null ? profileItem.getemployeeDegree().getObjectID() : null);

        if (employeeID == null) {
            setDefaultValuesByFormProperty();
        }

        if (profileItem.getPrimaryAddress() != null && isAddView) {
            Address primaryAddress = profileItem.getPrimaryAddress();
            TextBox addressNameWithDot = (TextBox) addressInf.getColumnsWidget().get(0).get(wfmStrings.addressName());
            TextBox addressLine1 = (TextBox) addressInf.getColumnsWidget().get(0).get(wfmStrings.addressLine1());
            TextBox addressLine2 = (TextBox) addressInf.getColumnsWidget().get(0).get(wfmStrings.addressLine2());
            TextBox cityWidthDot = (TextBox) addressInf.getColumnsWidget().get(0).get(wfmStrings.city());
            WfmDropdown countryWidthDot = (WfmDropdown) addressInf.getColumnsWidget().get(0).get(wfmStrings.country());
            WfmDropdown stateWidthDot = (WfmDropdown) addressInf.getColumnsWidget().get(0).get(wfmStrings.state());
            addressNameWithDot.setValue(primaryAddress.getName() != null ? primaryAddress.getName() : "");
            addressLine1.setValue(primaryAddress.getAddress() != null ? primaryAddress.getAddress() : "");
            addressLine2.setValue(primaryAddress.getAddressb() != null ? primaryAddress.getAddressb() : "");
            cityWidthDot.setValue(primaryAddress.getCity() != null ? primaryAddress.getCity() : "");
            int countryId = primaryAddress.getCountryId() != null ? primaryAddress.getCountryId() : 0;
            int stateId = primaryAddress.getStateId() != null ? primaryAddress.getStateId() : 0;
            countryWidthDot.addItem(new SelectItem(countryId, primaryAddress.getCountry() != null ? primaryAddress.getCountry() : ""));
            countryWidthDot.setSelected(primaryAddress.getCountryId() != null ? primaryAddress.getCountryId() : null);
            stateWidthDot.addItem(new SelectItem(stateId, primaryAddress.getState() != null ? primaryAddress.getState() : ""));
            stateWidthDot.setSelected(primaryAddress.getStateId() != null ? primaryAddress.getStateId() : null);
        }

        if (profileItem.getExperienceTableItems() != null && profileItem.getExperienceTableItems().length > 0) {
            getExperienceTableInfo(profileItem.getExperienceTableItems());
        }


        setItemTableValues(profileItem.getCustomTableItems());
    }


    private void getExperienceTableInfo(ExperienceTableItems[] experienceTableItems) {
        experienceTable.removeAllRows();
        for (ExperienceTableItems items : experienceTableItems) {
            experienceTable.addRow(widgets(items));
        }
    }


    private void fillTable(List<PaymentDeductionObject> items, final String from) {
        if (items != null && items.size() > 0) {
            for (PaymentDeductionObject item : items) {
                addItem(item, from);
            }
        } else {
            addPaymentDeductionEmptyRows(0, from);
        }
    }

    private void initLanguagesWidget(SelectItem[] _languages, SelectItem[] _spokenLanguages) {
        if (languages.getItems() != null) {
            languages.removeItems();
        }

        if (_languages != null) {
            for (SelectItem _language : _languages) {
                CustomListItem item = new CustomListItem(_language);
                languages.add(item);

                if (_spokenLanguages != null) {
                    for (SelectItem _spokenLanguage : _spokenLanguages) {
                        if (_spokenLanguage.getId().equals(_language.getId())) {
                            item.setCheck(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Filled form contact data
     *
     * @param contact - contact item
     */
    protected void fillFormWithDataContact(ContactListItem... contact) {
        if (contact != null && contact.length > 0) {
            contactListItem = contact[0];
        }
        //Personal Information
        //first name
        firstName.setText(contactListItem.getFirstName());
        //middle name
        middleName.setText(contactListItem.getMiddleName());
        //last name
        lastName.setText(contactListItem.getLastName());
        //other name
        otherName.setText(contactListItem.getOtherName());
        //title
        if (contactListItem.getTitleId() != null) {
            titl.setSelected(contactListItem.getTitleId());
            if (isOtherSelected(titl)) {
                title.setVisible(true);
                title.setText(contactListItem.getTitle());
            }
        }
        //date of birth
        if (contactListItem.getBirthDate() != null) {
            birthDatePicker.setDate(contactListItem.getBirthDate().getNonConvertedDate());
        } else {
            birthDatePicker.clearSelected();
        }
        //Company Information
        titl.setItems(contactListItem.getCrmAccount().getTitle());
        titl.clearSelected();
        titl.setSelected(contactListItem.getTitleId());

        //Contact Information
        setEmailDetailsWidgets();
        setPhoneNumbersWidgets();
        setImAddressWidgets();
        setWebSiteWidgets();
        setAddressWidgets();
        setTelegramWidgets();
    }
    ////////////////////////////////////////////////--filled form fields - end--////////////////////////////////////////

    protected void registerFields() {
        deletedCategories = new ArrayList<>();
        inactiveCategories = new ArrayList<>();
        paymentsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.payments(), false, false));
        deductionsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.deductions(), false, true));
        taxTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.taxes(), false, true));
        loansTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.loans(), true, false));
        employerContributionTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.employerContribution(), false, true));
        ////////////////////////////////////////////////////////////////////
        LoadingPanel.loading(true);
        //title list box
        titl = new DataListBox();
        titl.setAllowFirstItem(true);
        titl.setWidth("122px");

        title = new TextBox();
        title.setWidth("97px");
        title.setVisible(false);
        titl.addValueChangeHandler(event -> {
            title.setVisible(titl.getSelectedItem() != null && isOtherSelected(titl));
        });
        profilePicture = new ProfileImage(employeeID, LayoutRPC.HRMS_EMPLOYEE_FORM);
        //first name
        firstName = new TextBox();
        firstName.ensureDebugId("employee_firstName");
        firstName.addStyleName(test_code_ID_name + "first_name");
        //middle name
        middleName = new TextBox();
        middleName.ensureDebugId("employee_middleName");
        middleName.addStyleName(DEFAULT_WIDTH);
        middleName.addStyleName(test_code_ID_name + "middle_name");
        //last name
        lastName = new TextBox();
        lastName.ensureDebugId("employee_lastName");
        lastName.addStyleName(DEFAULT_WIDTH);
        lastName.addStyleName(test_code_ID_name + "last_name");
        //other name
        otherName = new TextBox();
        otherName.addStyleName(DEFAULT_WIDTH);
        otherName.addStyleName(test_code_ID_name + "other_name");
        driverID = new TextBox();
        driverID.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(driverID);
        driverID.addStyleName(test_code_ID_name + "driver_id");
        //birth day
        birthDatePicker = new DatePicker(true);
        birthDatePicker.ensureDebugId("employee_dateOfBirth");
        birthDatePicker.addStyleName(Constants.DEFAULT_WIDTH);
        birthDatePicker.addStyleName(test_code_ID_name + "date_of_birth");

        //email information table
        emailInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getEmailWidgets(null, null, null);
            }

            @Override
            public boolean isFilled() {
                if (!noAccess.getValue()) {
                    for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
                        TextBox value = (TextBox) emailRow.get(PARAM_TEXT_BOX);
                        if (!isEmpty(value.getText()) && !wfmStrings.email().equals(value.getText())) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }
        }, false);
        emailInf.ensureDebugId("employee_email");
        emailInf.addStyleName(test_code_ID_name + "email");
        //phone number information table
        phoneNumInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getPhoneWidgets(null, null, null);

            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> widgetsMap : phoneNumInf.getWidgets()) {
                    PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTable.PHONE_NUMBER);
                    if (!"".equals(phoneNumber.toString())) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
        phoneNumInf.addStyleName(test_code_ID_name + "phone_number");
        //im address information table
        imsAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getIMAddressWidgets(null, null);

            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> imAddressRow : imsAddressInf.getWidgets()) {
                    TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
                    if (!isEmpty(imAddress.getText())) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
        imsAddressInf.addStyleName(test_code_ID_name + "im_address");
        //web site information
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
        webSiteInf.addStyleName(test_code_ID_name + "web_site_address");
        //address information
        addressInf = new MultiTableNewUI(new MultiTableWidgets() {
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
                        AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                        if (addressWidget.isNotEmpty()) {
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
        /////////////////////////////////////////////////////////////////////////////////////
        //hire date
        hireDatePicker = new DatePicker(true);
        hireDatePicker.ensureDebugId("employee_hireDate");

        //fire(resignation) date
        fireDatePicker = new DatePicker(true);
        fireDatePicker.ensureDebugId("employee_resignationDate");
        //gender: male
        male = new KpiRadioButton("gender", wfmStrings.male());
        male.addStyleName(test_code_ID_name + "gender_male");
        //gender: female
        female = new KpiRadioButton("gender", wfmStrings.female());
        female.addStyleName(test_code_ID_name + "gender_female");
        // payment methods
        minSalaryMethod = new KpiRadioButton("paymentMethod", wfmStrings.minSalary());
        minSalaryMethod.addStyleName(test_code_ID_name + "minSalary");
        midSalaryMethod = new KpiRadioButton("paymentMethod", wfmStrings.midSalary());
        midSalaryMethod.addStyleName(test_code_ID_name + "midSalary");
        maxSalaryMethod = new KpiRadioButton("paymentMethod", wfmStrings.maxSalary());
        maxSalaryMethod.addStyleName(test_code_ID_name + "maxSalary");

        paymentMethodTable = new FlexTable();
        paymentMethodTable.setCellPadding(3);
        paymentMethodTable.setWidget(0, 0, minSalaryMethod);
        paymentMethodTable.setWidget(0, 1, midSalaryMethod);
        paymentMethodTable.setWidget(0, 2, maxSalaryMethod);
        //gender table
        genderTable = new FlexTable();
        genderTable.addStyleName(DEFAULT_WIDTH + " " + "options-row");
        genderTable.setWidget(0, 0, male);
        genderTable.setWidget(0, 1, female);
        if (formProperty != null && formProperty.get(CustomFormConstants.GENDER) != null) {
            male.setEnabled(!formProperty.get(CustomFormConstants.GENDER).isDisabled());
            female.setEnabled(!formProperty.get(CustomFormConstants.GENDER).isDisabled());
        }
        nationality = new TextBox();
        nationality.ensureDebugId("employee_nationality");
        nationality.addStyleName(DEFAULT_WIDTH);
        //martial status
        martialStatus = new DataListBox();
        martialStatus.ensureDebugId("employee_maritalStatus");
        martialStatus.addStyleName(DEFAULT_WIDTH);
        martialStatus.setAllowFirstItem(true);
        martialStatus.addStyleName(test_code_ID_name + "martial_status");
        //languages
        //languages = new TextBox();
        languages = new CustomList(Design.CHECK, true);
        languages.addStyleName(test_code_ID_name + "languages");

        languages.setSearchText(wfmStrings.searchLanguage()
        );
        languages.setHeight(100);

        //employee code
        empCode = new Numbering(false);
        //empCode.getTxtNumber().setMaxLength(7);
        //empCode.getTxtPrefix().setMaxLength(5);
        empCode.addStyleName(Constants.DEFAULT_WIDTH);
        empCode.addStyleName(test_code_ID_name + "employee_code");
        //wage rate (hourly)
        wageRate = new TextBox();
        wageRate.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        wageRate.addStyleName(test_code_ID_name + "wage_rate");
        Integer sysCalScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;
        Validation.addNumericKeyboardListener(wageRate, sysCalScale);
        wageRate.addKeyPressHandler(keyPressEvent -> wageRate.removeStyleName(Constants.ERROR_FORM_STYLE));
        //client charge rate (hourly)
        clientChargeRate = new TextBox();
        clientChargeRate.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        clientChargeRate.addStyleName(test_code_ID_name + "client_charge_rate");
        Validation.addNumericKeyboardListener(clientChargeRate, sysCalScale);
        clientChargeRate.addKeyPressHandler(keyPressEvent -> clientChargeRate.removeStyleName(Constants.ERROR_FORM_STYLE));

        //employee qualification
        employeeQualification = new DataListBox();
        employeeQualification.addStyleName(DEFAULT_WIDTH);
        employeeQualification.setAllowFirstItem(true);
        employeeQualification.addStyleName(test_code_ID_name + "employee_qualification");

        //employee timeslot
        timeslot = new DataListBox();
        timeslot.setWithoutNullLabel(true);
        timeslot.addStyleName(DEFAULT_WIDTH);
        timeslot.addStyleName(test_code_ID_name + "timeslot");

        //department
        pmDepartment = new DepartmentLookUp();
        pmDepartment.ensureDebugId("employee_department");
        pmDepartment.addStyleName(DEFAULT_WIDTH);
        pmDepartment.addStyleName(test_code_ID_name + "pm_department");
        pmDepartment.getSuggestBox().addSelectionHandler(event -> {
            if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                updateLocation(pmDepartment.getSelectedItemID());
            }
            if (departmentId != null && pmDepartment.getSelectedItem() != null && !pmDepartment.getSelectedItem().getId().equals(departmentId)) {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.OK, true);
                message.setTitle(wfmStrings.warning());
                message.setMessage(wfmStrings.effectiveStartDate());
                newDeptEffectiveDate = new DatePicker(new Date());
                message.addWidget(newDeptEffectiveDate, null);
                message.open();
            } else if (newDeptEffectiveDate != null) {
                newDeptEffectiveDate.setDate(null);
            }
        });

        salaryMode = new DataListBox();
        salaryMode.ensureDebugId(test_code_ID_name + "salaryMode");
        salaryMode.addStyleName(DEFAULT_WIDTH);
        if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
            salaryMode.addListItem(salaryMode0);
        }
        salaryMode.addListItem(salaryMode1);
        salaryMode.addListItem(salaryMode2);
        salaryMode.addValueChangeHandler(event -> salaryAmount.setEnabled(!event.getValue().getName().equals(wfmStrings.byTariffGrid())));
        //employment history
        //empHistory = new TextArea2();
        /*if (Utils.isMediaCom()) {
            empHistory.setMAX_LENGTH(1400);
            empHistory.setHeight("200px");
            empHistory.addStyleName(DEFAULT_WIDTH);
        }*/
        //account status
        status = new DataListBox();
        status.ensureDebugId("accountStatus");
        status.addStyleName(DEFAULT_WIDTH);
        status.setAllowFirstItem(true);
        status.addStyleName(test_code_ID_name + "account_status");

        reportsTo = new EmployeeLookUpWithCode();
        reportsTo.ensureDebugId("employee_supervisor");
        reportsTo.addStyleName(DEFAULT_WIDTH);
        reportsTo.setBeforeSearch(() -> reportsTo.getFilterParametrs().setIgnoreID(employeeID));

        //terms of contract
        termsOfContractBox = new TextBox();
        termsOfContractBox.setWidth("100px");
        termsOfContractBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        termsOfContractBox.setMaxLength(2);
        termsOfContractBox.addKeyUpHandler(event -> {
            Validation.numberValidation(termsOfContractBox);
            Validation.addNumericKeyboardListener(termsOfContractBox);
        });
        //terms of contract -> month/year box
        monthYearBox = new DataListBox();
        monthYearBox.setAllowFirstItem(true);
        monthYearBox.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.month()), new SelectItem(2, wfmStrings.year())});
        //terms of contract -> table

        //employment mode
        empMode = new DataListBox();
        empMode.ensureDebugId("employmentMode");
        empMode.addStyleName(DEFAULT_WIDTH);
        empMode.setAllowFirstItem(true);
        //salary grade
        /*salaryGrade = new DataListBox();
        salaryGrade.addStyleName(DEFAULT_WIDTH);
        salaryGrade.setAllowFirstItem(true);
        //add new salary grade link
        SimpleLink addNewSalaryGrade = new SimpleLink(wfmStrings.addNewSalaryGrade());
        addNewSalaryGrade.addStyleName("addField");
        addNewSalaryGrade.setWordWrap(false);
        addNewSalaryGrade.addClickHandler(widget -> goTo("grade|add/add"));
        //salary grade panel
        salaryDock = new VerticalPanel();
        salaryDock.addStyleName(Constants.DEFAULT_WIDTH);
        salaryDock.add(salaryGrade);
        salaryDock.add(addNewSalaryGrade);
        salaryDock.setCellHorizontalAlignment(addNewSalaryGrade, HasHorizontalAlignment.ALIGN_RIGHT);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALARY_GRADE_ADD, GeneralEmployeeEditForm.this, (sender, args) -> {
            if (args instanceof Integer) {
                initSalaryGradeValues((Integer) args);
            }
        });*/

        //salary amount
        salaryAmount = new TextBox();
        salaryAmount.ensureDebugId("employee_basicSalary");
        salaryAmount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(salaryAmount, 2);
        salaryAmount.addKeyDownHandler(c -> onCalculateBasicTotalSalary());
        salaryAmount.addKeyUpHandler(c -> onCalculateBasicTotalSalary());
        salaryAmount.addValueChangeHandler(c -> onCalculateBasicTotalSalary());

//        salaryTotalAmount = new TextBox();
//        salaryTotalAmount.ensureDebugId("employee_totalSalary");
//        salaryTotalAmount.addStyleName(DEFAULT_WIDTH);
//        salaryTotalAmount.setEnabled(false);
//        Validation.addNumericKeyboardListener(salaryTotalAmount, 2);

        if (!fromSectionPMEmployeeEdit) {
            /*if (Utils.isArabicCompany()) {
                jobTitle = new DataListBox();
                jobTitle.addStyleName(DEFAULT_WIDTH);
                AllInOneService.App.get().getJobTitles(new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.show(caught.toString(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(SelectItem[] result) {
                        addField(CustomFormConstants.JOB_TITLE, jobTitle, getTitle(wfmStrings.jobTitle()));
                        jobTitle.setItems(result);
                        jobTitle.addValueChangeHandler(event -> {
                            if (jobTitle.getSelectedItem() != null) {
                                salaryAmount.setText(jobTitle.getSelectedItem().getDescription());
                            }
                        });

                    }
                });
            }*/
            if (Utils.hasPermission(EMP_PROFILE_BASIC_SALARY) && !Utils.isSettings()) {
                if (formProperty != null && formProperty.get(CustomFormConstants.SALARY_AMOUNT) != null) {
                    addField(CustomFormConstants.SALARY_AMOUNT, salaryAmount, getTitle(formProperty.get(CustomFormConstants.SALARY_AMOUNT).isChanged() ?
                            formProperty.get(CustomFormConstants.SALARY_AMOUNT).getTitle() : getTitle(wfmStrings.basicSalary()), formProperty.get(CustomFormConstants.SALARY_AMOUNT).isRequired()));
                    salaryAmount.setEnabled(!formProperty.get(CustomFormConstants.SALARY_AMOUNT).isDisabled());
                } else {
                    addField(CustomFormConstants.SALARY_AMOUNT, salaryAmount, getTitle(wfmStrings.basicSalary()));
                }
//                addField(CustomFormConstants.SALARY_TOTAL_AMOUNT, salaryTotalAmount, getTitle(wfmStrings.totalSalary()));
            }
            salaryAmount.addStyleName(test_code_ID_name + "salary_amount");
        }

        departmentContainer = new Div();
        departmentContainer.add(pmDepartment);

        applyLeaveAllowanceForEmployee = new KpiCheckBox();
        applyLeaveAllowanceForEmployee.addStyleName(DEFAULT_WIDTH);
        new KpiToolTip(applyLeaveAllowanceForEmployee, wfmStrings.applyNewPositionsLeaveAllowances());

        //employee position
        positionPanel = new PositionLookUp();
        positionPanel.ensureDebugId("employee_position");

        applyWageRate = new KpiCheckBox(wfmStrings.applyFrom() + " : ");
        applyWageRateFrom = new DateBox();
        applyWageRateFrom.addStyleName("file--GeneralEmployeeEditForm form-control-sm");
        applyWageRateFrom.setVisible(false);
        applyWageRateFrom.getDatePicker().addStyleName("gwt-DatePicker-mod");
        applyWageRate.addClickHandler(clickEvent -> applyWageRateFrom.setVisible(applyWageRate.getValue()));
        wageRateWithCheckBox = new VerticalPanel();
        wageRateWithCheckBox.add(wageRate);
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(applyWageRate);
        hp.add(applyWageRateFrom);
        hp.setCellVerticalAlignment(applyWageRate, HasVerticalAlignment.ALIGN_MIDDLE);
        wageRateWithCheckBox.add(hp);

        applyClientChargeRate = new KpiCheckBox(wfmStrings.applyFrom() + " : ");
        applyClientChargeRateFrom = new DateBox();
        applyClientChargeRateFrom.addStyleName("form-control-sm");
        applyClientChargeRateFrom.getDatePicker().addStyleName("gwt-DatePicker-mod");
        applyClientChargeRateFrom.setVisible(false);
        applyClientChargeRate.addClickHandler(clickEvent -> applyClientChargeRateFrom.setVisible(applyClientChargeRate.getValue()));

        clientChargeRateWithCheckBox = new VerticalPanel();
        clientChargeRateWithCheckBox.add(clientChargeRate);
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.add(applyClientChargeRate);
        hp2.add(applyClientChargeRateFrom);
        hp2.setCellVerticalAlignment(applyClientChargeRate, HasVerticalAlignment.ALIGN_MIDDLE);
        clientChargeRateWithCheckBox.add(hp2);
        positionContainer = new Div();
        positionContainer.add(positionPanel);
        positionPanel.getSuggestBox().addSelectionHandler(e -> {
            setPositionItems();
        });



        //location
        locations = new LocationLookUpWithCode();
        locations.addStyleName(DEFAULT_WIDTH);
//        locations.setAllowFirstItem(true);

        locationPanel1 = new AdvancedInputGroup(locations);
        locationPanel1.ensureDebugId("employee_location");
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_LOCATION)) {
            locationPanel1.setAppender("ficon--plus");
            locationPanel1.appenderClickHandler(() -> goTo("location|add/add"));
        }

        if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
            locationSelectionHandler(locations);
        }


//        refreshLocationDropDown(null);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_ADD, GeneralEmployeeEditForm.this, (sender, args) -> {
//            if (args instanceof Integer) {
//                refreshLocationDropDown((Integer) args);
//            }
        });

        //initialize bank account information fields;
        //bank name
        bankName = new TextBox();
        bankName.ensureDebugId("bankName");
        bankName.addStyleName(DEFAULT_WIDTH);
        //bank address
        bankAddress = new TextArea2();
        bankAddress.ensureDebugId("bankAddress");
//        bankAddress.addStyleName(DEFAULT_WIDTH);
        //account number
        accountNumber = new TextBox();
        accountNumber.ensureDebugId("accountNumber");
        accountNumber.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(accountNumber);
        //account name
        accountName = new TextBox();
        accountName.ensureDebugId("accountName");
        accountName.addStyleName(DEFAULT_WIDTH);
        //swift code
        swiftCode = new TextBox();
        swiftCode.ensureDebugId("swiftCode");
        swiftCode.addStyleName(DEFAULT_WIDTH);
        //sort code
        sortCode = new TextBox();
        sortCode.ensureDebugId("sortCode");
        sortCode.addStyleName(DEFAULT_WIDTH);
        //iBan code
        iBanCode = new TextBox();
        iBanCode.ensureDebugId("iBanCode");
        iBanCode.addStyleName(DEFAULT_WIDTH);
        //agentID
        agentID = new TextBox();
        agentID.addStyleName(DEFAULT_WIDTH);
        //bank account information

        // Personal Identity Information
        passportNumber = new TextBox();
        passportNumber.ensureDebugId("employee_passportNumber");
        passportNumber.addStyleName(DEFAULT_WIDTH);

        passportIssue = new CountryLookUp();
        passportIssue.ensureDebugId("employee_passportIssue");
        passportIssue.addStyleName(DEFAULT_WIDTH);

        passportIssueDate = new DatePicker(true);
        passportIssueDate.ensureDebugId("employee_passportIssueDate");
        passportIssueDate.addStyleName(DEFAULT_WIDTH);
        passportExpiryDate = new DatePicker(true);
        passportExpiryDate.ensureDebugId("employyee_passportExpiryDate");
        passportExpiryDate.addStyleName(DEFAULT_WIDTH);
        medicalInsuranceExDate = new DatePicker(true);
        medicalInsuranceExDate.ensureDebugId("employee_insuranceExpiryDate");
        medicalInsuranceExDate.addStyleName(DEFAULT_WIDTH);
        insuranceNumber = new TextBox();
        insuranceNumber.ensureDebugId("employee_insuranceNumber");
        insuranceNumber.addStyleName(DEFAULT_WIDTH);
        visaNumber = new TextBox();
        visaNumber.ensureDebugId("employee_visaNumber");
        visaNumber.addStyleName(DEFAULT_WIDTH);
        wpsNumber = new TextBox();
        wpsNumber.ensureDebugId("wpsNumber");
        wpsNumber.addStyleName(DEFAULT_WIDTH);

        openingBalanceDays = new TextBox();
        openingBalanceDays.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(openingBalanceDays, 10, true);
        probationDays = new TextBox();
        if (!Utils.hasRole(ADMIN)) {
            probationDays.setEnabled(false);
            openingBalanceDays.setEnabled(false);
        }
        probationDays.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(probationDays, 10, true);

        if (employeeID == null && !emptyRows) {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_PAYMENT);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_DEDUCTION);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_TAX);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_LOAN);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            emptyRows = true;
        }

        visaIssueDate = new DatePicker(true);
        visaIssueDate.ensureDebugId("employee_visaIssueDate");
        visaIssueDate.addStyleName(DEFAULT_WIDTH);
        visaExpirationDate = new DatePicker(true);
        visaExpirationDate.ensureDebugId("employeeExpirationDate");
        visaExpirationDate.addStyleName(DEFAULT_WIDTH);
        visaExpirationDateReminderTable = new MultiTableNewUI(true, 5, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getVisaExpirationDateReminderWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> row : visaExpirationDateReminderTable.getWidgets()) {
                    DataListBox value = (DataListBox) row.get(MultiTable.LIST_BOX);
                    if (value.getSelectedItem() != null && value.getSelectedItem().getId() != null) {
                        return true;
                    }
                }
                return false;
            }
        });
        visaExpirationDateReminderTable.ensureDebugId("employee_setExpiryReminder");
        //main role
        mainRole = new CheckboxSelector();
        mainRole.ensureDebugId("account_information-role");
        mainRole.addStyleName("cellBadgetWidget-prevent-absolute");
        if (!fromSectionTCInstructor && (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)
                || Utils.isSettings() || Utils.hasPermission(PermissionConstants.PM_SHOW_EMPLOYEE_ROLE_WIDGET))) {
            addField(CustomFormConstants.ACCOUNT_ROLES, mainRole, getTitle(wfmStrings.roles()));
            mainRole.addStyleName(test_code_ID_name + "account_roles");
        }

        fingerprintDevices = new CheckboxSelector();
        fingerprintDevices.ensureDebugId("account_information-fingerprint_devices");
        fingerprintDevices.addStyleName("cellBadgetWidget-prevent-absolute");
        addField(CustomFormConstants.FINGERPRINT_DEVICE, fingerprintDevices, getTitle(wfmStrings.fingerprintSetup()));

        noAccess = new KpiSwitcher();
        essUser = new KpiSwitcher();
        noAccess.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                essUser.setValue(false);
                profileItem.setStatusCode(EMPLOYEE_STATUS_NO_ACCCESS);
                mainRole.setVisible(false);
            } else {
                profileItem.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
                mainRole.setVisible(true);
            }
        });
        essUser.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                noAccess.setValue(false);
                mainRole.setVisible(false);
            } else {
                mainRole.setVisible(true);
            }
            profileItem.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
        });
        //courses
        if (fromSectionTCInstructor) {
            courses = new KpiCellTree();
            courses.addStyleName(test_code_ID_name + "courses");
            courses.setSearchDefaultText(wfmStrings.searchCourses());
            courses.drawSelectedSide(new SelectionContainer() {
                @Override
                public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                    //Employee Name Blow
                    Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return object.getName();
                        }
                    };
                    employee.setSortable(true);
                    sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                    selectedDataGrid.addColumn(employee, wfmStrings.courses());
                    selectedDataGrid.setColumnWidth(employee, 40, Style.Unit.PCT);

                    //Remove Action
                    final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return wfmStrings.delete();
                        }
                    };
                    action.setFieldUpdater((index, object, value) -> {
                        object.setSelected(false);
                        selectionModel.setSelected(object, false);
                        List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                        contacts.remove(object);
                    });
                    selectedDataGrid.addColumn(action, wfmStrings.action());
                    selectedDataGrid.setColumnWidth(action, 20, Style.Unit.PCT);
                }

                @Override
                public void additionalActions(HTMLPanel actionsPanel) {
                }
            });
            courses.addHandler(event -> courses.removeStyleName(ERROR_FORM_STYLE), ClickEvent.getType());
        }

        initTables(PayrollConstants.CATEGORY_PAYMENT);
        initTables(PayrollConstants.CATEGORY_DEDUCTION);
        initTables(PayrollConstants.CATEGORY_TAX);
        initTables(PayrollConstants.CATEGORY_LOAN);
        initTables(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);

        //languages widget
        languagesWidget = new SpokenLanguagesWidget(null);
        languagesWidget.addStyleName(DEFAULT_WIDTH);
        languagesWidget.getElement().setId("employee_language_tab");
        languagesWidget.addStyleName("inCol-input-group");

        //employee degree
        employeeDegree = new DataListBox();
        employeeDegree.addStyleName(DEFAULT_WIDTH);
        employeeDegree.getElement().setId("employee_degree_tab");

        //attachments
        uploadForm = new GeneralFileUpload(F_EMPLOYEE_PROFILE, employeeID, employeeID);

        LoadingPanel.loading(false);
    }

    private void addPaymentDeductionEmptyRows(int length, String type) {
        while (length < DEFAULT_ROWS) {
            addItem(null, type);
            length++;
        }
    }

    private void initTables(final String from) {
        EditableTable table = getTable(from);
        boolean editable = Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL);

        table.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, from);
            }

            @Override
            public void removeRow() {

            }
        });

        table.setRemoveRowListener(() -> {
            if (!editable)
                return;
            if (table.getRowCount() > 1) {
                final PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                if (amountWidget.getItemID() != null & amountWidget.isUsed()) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage("Please note that you cannot delete the Category which has been used in Payslips.<br/><br/>If you will not use this Recurring Category, you can make it inactive so that it does not appear in Recurring Payment/Deduction categories list. Do you want to make it Inactive?");
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            inactiveCategories.add(amountWidget.getItemID());
                            table.getGrid().getModel().removeRow(table.getGrid().getCurrentRow());
                            onCalculateBasicTotalSalary();
                        }
                    });
                    messageBox.open();
                } else if (amountWidget.getItemID() != null) {
                    deletedCategories.add(amountWidget.getItemID());
                    table.getGrid().getModel().removeRow(table.getGrid().getCurrentRow());
                    onCalculateBasicTotalSalary();
                } else {
                    table.getGrid().getModel().removeRow(table.getGrid().getCurrentRow());
                    onCalculateBasicTotalSalary();
                }
            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });
    }

    private void updateLocation(Integer departmentId) {
        AllInOneService.App.get().getLocationByDepartmentId(departmentId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem result) {
                //

                if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                    locations.removeFromParent();
                    locationPanel1.remove(locations);
                    locations = new LocationLookUpWithCode();
                    locationSelectionHandler(locations);
                    locations.setSelected(result);
                    locationPanel1.add(locations);
                    positionPanel.removeFromParent();
                }
                positionContainer.remove(positionPanel);
                positionPanel = new PositionLookUp();
                positionPanel.getFilterParametrs().setDepartmentId(departmentId);
                positionPanel.getSuggestBox().addSelectionHandler(e -> {
                    setPositionItems();
                });
                positionContainer.add(positionPanel);
            }
        });


    }

    private void locationSelectionHandler(LocationLookUpWithCode locationLookUpWithCode) {
        locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
            pmDepartment.removeFromParent();
            departmentContainer.remove(pmDepartment);
            pmDepartment = new DepartmentLookUp();
            if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                pmDepartment.getSuggestBox().addSelectionHandler(event -> {
                    updateLocation(pmDepartment.getSelectedItemID());
                });
            }
            pmDepartment.getFilterParametrs().setLocationId(locations.getSelectedItemID());
            departmentContainer.add(pmDepartment);
            positionPanel.clear();
        });
        locationLookUpWithCode.getTextBox().addKeyDownHandler(e -> {
            pmDepartment.removeFromParent();
            departmentContainer.remove(pmDepartment);
            pmDepartment = new DepartmentLookUp();
            departmentContainer.add(pmDepartment);
            positionPanel.removeFromParent();
            positionContainer.remove(positionPanel);
            positionPanel = new PositionLookUp();
            positionPanel.getSuggestBox().addSelectionHandler(event -> {
                setPositionItems();
            });
            positionContainer.add(positionPanel);
        });

        if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
            pmDepartment.getTextBox().addKeyDownHandler(e -> {
                positionPanel.removeFromParent();
                positionContainer.remove(positionPanel);
                positionPanel = new PositionLookUp();
                positionPanel.getSuggestBox().addSelectionHandler(event -> {
                    setPositionItems();
                });
                positionPanel.getFilterParametrs().setDepartmentId(departmentId);
                positionContainer.add(positionPanel);
            });
        }


    }

    private void setPositionItems() {
        AllInOneService.App.get().getPositionItems(positionPanel.getSelectedItemID(), new AsyncCallback<PositionsSelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(PositionsSelectItem result) {
                if (Utils.hasGenericAccess(ENABLE_LOCATION_DEPARTMENT_POSITION_RELATIONS)) {
                    locations.setSelected(result.getLocation());
                    pmDepartment.setSelected(result.getDepartment());
                }
            }
        });
    }


    private EditableTable getTable(final String from) {
        switch (from) {
            case PayrollConstants.CATEGORY_PAYMENT:
                return paymentsTable;
            case PayrollConstants.CATEGORY_DEDUCTION:
                return deductionsTable;
            case PayrollConstants.CATEGORY_TAX:
                return taxTable;
            case PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION:
                return employerContributionTable;
            default:
                return loansTable;
        }
    }

    private ColumnConfig[] getPaymentDeductionTableColumns(String title, boolean isLoan, boolean isLinkedType) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        if (isLoan) {
            columns.add(new ColumnConfig(LookUpCell.class, "category", title, 200, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / %", 100, true, "right-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "totalAmount", wfmStrings.total() + " " + wfmStrings.amount(), 100, true, "right-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "startDate", wfmStrings.startDate(), 100, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "remainAmount", wfmStrings.remainingAmount(), 140, true, "right-align-Cell"));
        } else if (isLinkedType) {
            columns.add(new ColumnConfig(LookUpCell.class, "category", title, 220, true, "left-align-Cell"));
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 80, true, "left-align-Cell"));
            }
            columns.add(new ColumnConfig(LinkedLinkableCell.class, "type", wfmStrings.type(), 210, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / %", 120, true, "right-align-Cell"));
        } else {
            columns.add(new ColumnConfig(LookUpCell.class, "category", title, 220, true, "left-align-Cell"));
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 80, true, "left-align-Cell"));
            }
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / %", 120, true, "right-align-Cell"));
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    public void addItem(PaymentDeductionObject paymentDeduction, final String from) {
        EditableTable table = getTable(from);
        boolean editable = Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL);

        DataListBox epPaymentType = new DataListBox();
        epPaymentType.setWithoutNullLabel(true);
        epPaymentType.setItems(new SelectItem[]{
                new SelectItem(EPPaymentType.RECURRING.getId(), EPPaymentType.RECURRING.getTitle()),
                new SelectItem(EPPaymentType.ADDITIONAL.getId(), EPPaymentType.ADDITIONAL.getTitle())
        });
        epPaymentType.setSelected(paymentDeduction != null && paymentDeduction.getPaymentType() != null ? paymentDeduction.getPaymentType().getAsSelectItem() : epPaymentType.getItems()[1]);
        Command cmdEPPaymentType = () -> {
            EPPaymentType epType = EPPaymentType.findById(epPaymentType.getSelectedItem().getId());
            Widget typeWidget = table.getColumnById(table.getGrid().getCurrentRow(), "type");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");

            if (typeWidget instanceof LinkedTypeWidget) {
                if (EPPaymentType.ADDITIONAL.equals(epType)) {
                    ((LinkedTypeWidget) typeWidget).setSelected(0);
                    amountWidget.showPercentage(false);
                    Command cmd = ((LinkedTypeWidget) typeWidget).getChangeHandler();
                    if (cmd != null) {
                        cmd.execute();
                    }
                }
                ((LinkedTypeWidget) typeWidget).setEnabled(!EPPaymentType.ADDITIONAL.equals(epType));
                ((LinkedTypeWidget) typeWidget).showOrRemoveLink();
                LinkedLinkableCell cell = (LinkedLinkableCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                cell.InActive();
            } else {
                if (EPPaymentType.ADDITIONAL.equals(epType)) {
                    ((DataListBox) typeWidget).setSelected(0);
                    amountWidget.showPercentage(false);
                }
                ((DataListBox) typeWidget).setEnabled(!EPPaymentType.ADDITIONAL.equals(epType));

                CustomCell cell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                cell.InActive();
            }
            CustomCell amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
            amountWidgetCell.InActive();
        };
        epPaymentType.addValueChangeHandler(ch -> {
            cmdEPPaymentType.execute();
        });

        final DataListBox type = new DataListBox();
        final LinkedTypeWidget linkedType = new LinkedTypeWidget();
        final CategoryLookUp categoryLookUp = new CategoryLookUp(from, () -> true);
        categoryLookUp.setEnabled(editable);
//        categoryLookUp.getSuggestBox().getElement().setAttribute("style", "width:200px !important");
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
        }

        categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCategorySelected(categoryLookUp, from));

        categoryLookUp.getSuggestBox().addKeyUpHandler(event -> onCategorySelected(categoryLookUp, from));

        if (PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from)) {
            linkedType.setEnabled(editable);
            linkedType.setChangeHandler(() -> {
                PayslipItemAmountWidget amountWidget;
                CustomCell amountWidgetCell;
                LinkedLinkableCell typeWidgetCell;
                amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
                typeWidgetCell = (LinkedLinkableCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                linkedType.showOrRemoveLink();
                amountWidget.showPercentage(linkedType.getSelectedId() != 0);

                amountWidgetCell.InActive();
                typeWidgetCell.InActive();
                onCalculateBasicTotalSalary();
            });
            linkedType.setCopyFromBoxHandler(() -> {
                linkedType.clearCategoriesTable();
                for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                    CategoryLookUp categoryLookUp1 = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                    if (categoryLookUp1.getSelectedData() != null) {
                        PaymentDeductionObject object = new PaymentDeductionObject();
                        object.setCategoryItem(categoryLookUp1.getSelectedData());
                        linkedType.addItem(object, true);
                    }
                }
            });
            if (paymentDeduction != null && paymentDeduction.getType() != null) {
                linkedType.setSelected(paymentDeduction.getType());
                if (paymentDeduction.getLinkedCategories() != null && paymentDeduction.getLinkedCategories().size() > 0) {
                    linkedType.setLinkedItems(paymentDeduction.getLinkedCategories());
                } else if (paymentDeduction.isFromAllAllowances()) {
                    linkedType.clearCategoriesTable();
                    linkedType.setValue(paymentDeduction.isFromAllAllowances());
                    for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                        CategoryLookUp category = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                        if (category.getSelectedData() != null) {
                            PaymentDeductionObject object = new PaymentDeductionObject();
                            object.setCategoryItem(category.getSelectedData());
                            linkedType.addItem(object, true);
                        }
                    }
                }
            } else {
                linkedType.setSelected(0);
            }
        } else {
            type.setEnabled(editable);
            type.setWithoutNullLabel(true);
            type.setItems(new SelectItem[]{
                    new SelectItem(0, wfmStrings.fixed() + " "),
                    new SelectItem(1, wfmStrings.basicOfPersentage())
            });
            if (paymentDeduction != null && paymentDeduction.getType() != null) {
                type.setSelected(paymentDeduction.getType());
            } else {
                type.setSelected(0);
            }
            type.addValueChangeHandler(changeEvent -> {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                CustomCell amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
                CustomCell typeWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                amountWidget.showPercentage(type.getSelectedId() != 0);
                amountWidgetCell.InActive();
                typeWidgetCell.InActive();
            });
        }

        Command calculate = () -> {
            if (PayrollConstants.CATEGORY_PAYMENT.equals(from) || PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from)) {
                onCalculateBasicTotalSalary();
            }
        };

        final PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.setEditable(editable);
        amountWidget.setWidth("118px");
        amountWidget.getAmountTextBox().addKeyPressHandler(keyPressEvent -> calculate.execute());
        amountWidget.getAmountTextBox().addKeyUpHandler(keyPressEvent -> calculate.execute());
        amountWidget.getAmountTextBox().addChangeHandler(keyPressEvent -> calculate.execute());
        if (paymentDeduction != null) {
            amountWidget.setAmount(paymentDeduction.getType() == null || paymentDeduction.getType() == 0 ? paymentDeduction.getPaymentAmount() : paymentDeduction.getPercentage());

            amountWidget.showPercentage(paymentDeduction.getType() != null && paymentDeduction.getType() != 0);
            amountWidget.setItemID(paymentDeduction.getId());
            amountWidget.setPsdId(paymentDeduction.getPsdId());
            amountWidget.setUsed(paymentDeduction.isUsed());
        }

        CustomDatePicker startDate = new CustomDatePicker();
        startDate.setEnabled(editable);
        if (paymentDeduction != null && paymentDeduction.getStarttDate() != null && paymentDeduction.getStarttDate().getDate() != null) {
            startDate.setDate(paymentDeduction.getStarttDate().getNonConvertedDate());
        }
        EditableTextBox totalAmount = new EditableTextBox();
        totalAmount.setEnabled(editable);
        if (paymentDeduction != null && paymentDeduction.getTotalAmount() != null) {
            totalAmount.setText(Utils.getCalculationNumberFormat().format(paymentDeduction.getTotalAmount()));
        }
        EditableTextBox remainingAmount = new EditableTextBox();
        remainingAmount.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getRemainingAmount() != null) {
            remainingAmount.setText(Utils.getCalculationNumberFormat().format(paymentDeduction.getRemainingAmount()));
        }
        Widget typeWidget = PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from) ? linkedType : type;
        if (from.equals(PayrollConstants.CATEGORY_LOAN)) {
            table.addRow(new Widget[]{categoryLookUp, typeWidget, amountWidget, totalAmount, startDate, remainingAmount});
        } else {
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                table.addRow(new Widget[]{categoryLookUp, epPaymentType, typeWidget, amountWidget});
                cmdEPPaymentType.execute();
            } else {
                table.addRow(new Widget[]{categoryLookUp, typeWidget, amountWidget});
            }
        }
    }

    private void onCategorySelected(CategoryLookUp categoryLookUp, final String from) {
        EditableTable table = getTable(from);
        int selectedCategoryCount = 0;
        PaymentDeductionSelectItem selectedCategory = categoryLookUp.getSelectedData();
        if (selectedCategory != null) {
            for (int i = 0; i < table.getGrid().getRowCount(); i++) {
                PaymentDeductionSelectItem selectedItem = ((CategoryLookUp) table.getColumnById(i, "category")).getSelectedData();
                if (selectedItem != null && selectedItem.getCode() != null && selectedCategory.getCode() != null && selectedItem.getCode().equals(selectedCategory.getCode())) {
                    selectedCategoryCount++;
                }
            }
            if (selectedCategoryCount >= 2) {
                categoryLookUp.clear();
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, selectedCategory.getName() + wfmStrings.isAlreadySelected());
                messageBox.open();
            }
        }
    }

    private WidgetsMap getVisaExpirationDateReminderWidgets(Integer minutes) {
        DataListBox visaExpirationDateReminderBox = new DataListBox();
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24, " " + wfmStrings.oneDay()));        //one day    //1
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 2, " " + wfmStrings.twoDays()));       //two days   //2
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 3, " " + wfmStrings.threeDays()));     //three days //3
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 5, " " + wfmStrings.fiveDays()));      //five    days  //4
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 7, " " + wfmStrings.oneWeek()));       //one week   //5
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 7 * 2, " " + wfmStrings.twoWeeks()));  //two weeks  //6
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 30, " " + wfmStrings.oneMonth()));    //one month  //7
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 45, " " + wfmStrings.fortyFiveDays()));    //one month  //8
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 60, " " + wfmStrings.twoMonths()));    //one month  //9
        visaExpirationDateReminderBox.addListItem(new SelectItem(60 * 24 * 90, " " + wfmStrings.threeMonths()));    //one month  //10
        if (minutes != null) {
            visaExpirationDateReminderBox.setSelected(minutes);
        }

        WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addToCenter(MultiTable.LIST_BOX, visaExpirationDateReminderBox);
        return widgetsMap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected ContactListItem setValues() {
        if (profileItem == null) {
            profileItem = new ProfileItem();
        }
        if (employeeID == null) {
            profileItem.setPhotoId(profilePicture.getImageID());
        }
        profileItem.setEmployeeId(employeeID);
        //date of birth
        profileItem.setDob(new DateNonConvertable(birthDatePicker.getDate()));
        //SET CONTACT ITEM VALUES
        profileItem = (ProfileItem) setValuesCCC(profileItem);
        profileItem.setDriverID(driverID.getText());
        //gender: male
        if (male.getValue()) {
            profileItem.setGender(Constants.MALE);
        }
        if (minSalaryMethod.getValue()) {
            profileItem.setPaymentMethod(wfmStrings.minSalary());
        } else if (midSalaryMethod.getValue()) {
            profileItem.setPaymentMethod(wfmStrings.midSalary());
        } else if (maxSalaryMethod.getValue()) {
            profileItem.setPaymentMethod(wfmStrings.maxSalary());
        }
        SelectItem selectedItem = salaryMode.getSelectedItem();
        profileItem.setSalaryMode(salaryMode.getSelectedItem().getDescription());
        //gender: female
        if (female.getValue()) {
            profileItem.setGender(Constants.FEMALE);
        }
        //Nationality
        profileItem.setNationality(nationality.getText());
        //martial status
        if (martialStatus.getSelectedItem() != null) {
            profileItem.setMartialStatusId(martialStatus.getSelectedItem().getId());
        }
        //spoken languages
        /*if (languages.getItems() != null && languages.getItems().size() > 0) {
            List<SelectItem> appliedClients = new ArrayList<>();
            for (CustomListItem client : languages.getItems()) {
                if (client.getValue()) {
                    appliedClients.add(client.getItem());
                }
            }
            profileItem.setSpokenLanguages(appliedClients.toArray(new SelectItem[]{}));
        }*/
        if (languagesWidget != null) {
            profileItem.setSpokingLanguages(languagesWidget.getLanguages());
        }
        if (employeeDegree.getSelectedItem() != null) {
            profileItem.setemployeeDegree(new ReferenceItem(employeeDegree.getSelectedItem().getId(), employeeDegree.getSelectedItem().getName()));
        }
        /*if (workType.getSelectedItem() != null) {
            profileItem.setWorkType(workType.getSelectedItem().getDescription());
            if (!workType.getSelectedItem().getDescription().equals(currentWorkType)) {
                profileItem.setWorkTypeStart(newWorkTypeEffectiveDate.getDate());
            }
        }*/

        //
        profileItem.setFrom(WORKFORCETRACK);
        if (fromSectionTCInstructor) {
            profileItem.setFrom(TC_INSTRUCTOR_ADD_FORM);
        }
        //employment code
        profileItem.setNumberData(empCode.getNumberData(true));
        profileItem.setEmpCode(empCode.getNumberData(true).getNumberString());
        if (Utils.hasGenericAccess(ENABLE_EMPLOYEE_CODE_INTEGER)) {
            profileItem.setEmpCode(empCode.getTxtNumber().getValue());
        }

        //department
        if (pmDepartment.getSelectedItem() != null) {
            profileItem.setPmDepartmentID(pmDepartment.getSelectedItem().getId());
            if (departmentId != null && !departmentId.equals(pmDepartment.getSelectedItem().getId())) {
                profileItem.setDeptStartDate(newDeptEffectiveDate != null ? newDeptEffectiveDate.getDate() : null);
            }
        }
        //wage rate
        Double wr = 0.0, ccr = 0.0;
        if (wageRate.getText() != null && !"".equals(wageRate.getText())) {
            wr = Double.valueOf(wageRate.getText());
        }
        profileItem.setWageRate(wr);

        if (applyWageRate != null && applyWageRate.getValue()) {
            profileItem.setApplyWageRateFrom(applyWageRateFrom.getValue());
        }
        if (applyClientChargeRate != null && applyClientChargeRate.getValue()) {
            profileItem.setApplyClientChargeRateFrom(applyClientChargeRateFrom.getValue());
        }

        //client charge rate
        if (clientChargeRate.getText() != null && !"".equals(clientChargeRate.getText())) {
            ccr = Double.valueOf(clientChargeRate.getText());
        }
        profileItem.setClientChargeRate(ccr);
        //employee qualification
        profileItem.setQualificationID(null);
        if (employeeQualification.getSelectedItem() != null) {
            profileItem.setQualificationID(employeeQualification.getSelectedItem().getId());
        }

        //employee timeslot
        profileItem.setTimeslot(null);
        if (timeslot.getSelectedItem() != null) {
            profileItem.setTimeslot(timeslot.getSelectedItem());
        }

        //account status
        profileItem.setStatusId(null);
        if (status.getSelectedItem() != null) {
            profileItem.setStatusId(status.getSelectedItem().getId());
        }
        //resignation date
        profileItem.setFireDate(fireDatePicker.getDate() != null ? new DateNonConvertable(fireDatePicker.getDate()) : null);
        //hire date
        profileItem.setHireDate(hireDatePicker.getDate() != null ? new DateNonConvertable(hireDatePicker.getDate()) : null);
        //supervisor
        profileItem.setReportsToId(null);
        if (reportsTo.getSelectedItem() != null) {
            profileItem.setReportsToId(reportsTo.getSelectedItem().getId());
        }
        //location
        if (locations.getSelectedItem() != null) {
            profileItem.setLocationId(locations.getSelectedItem().getId());
        }
        //terms of contract
        if (termsOfContractBox.getText() != null && !"".equals(termsOfContractBox.getText())) {
            profileItem.setTermsOfContract(Integer.valueOf(termsOfContractBox.getText()));
        }
        //terms of contract list box
        if (monthYearBox.isSomethingSelected()) {
            profileItem.setTermsOfCMonthORYear(monthYearBox.getSelectedItem().getId());
        }
        //employment mode
        profileItem.setEmpModeId(null);
        if (empMode.getSelectedItem() != null) {
            profileItem.setEmpModeId(empMode.getSelectedItem().getId());
        }
        profileItem.setSalaryGradeId(null);
        //salary amount
        if (salaryAmount.getText() != null && !"".equals(salaryAmount.getText())) {
            profileItem.setSalaryAmount(extendedNumberFormat.parse(salaryAmount.getValue()));
        }
        if (jobTitle != null && jobTitle.getSelectedId() != null) {
            profileItem.setJobTitleId(jobTitle.getSelectedId());
            profileItem.setJobTitle(jobTitle.getSelectedItem().getName());
        }
        //visa expiration date
        if (visaExpirationDate.getDate() != null) {
            DateUtil.resetTime(visaExpirationDate.getDate());
            profileItem.setVisaExpirationDate(new DateNonConvertable(visaExpirationDate.getDate()));
        } else {
            profileItem.setVisaExpirationDate(null);
        }
        //visa expiration date
        LinkedList<HashMap<String, Widget>> widgetsMapList = visaExpirationDateReminderTable.getWidgets();
        ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
        if (widgetsMapList != null && widgetsMapList.size() > 0) {
            ArrayList<Integer> reminderMinutes = new ArrayList<>();
            for (HashMap<String, Widget> widgetHashMap : widgetsMapList) {
                DataListBox visaExpirationDateBoxT = (DataListBox) widgetHashMap.get(MultiTable.LIST_BOX);
                if (visaExpirationDateBoxT != null && visaExpirationDateBoxT.isSomethingSelected()) {
                    CalendarEventReminder reminder = new CalendarEventReminder();
                    Integer reminderTimes = visaExpirationDateBoxT.getSelectedItem().getId();
                    reminder.setReminderTimes(reminderTimes);
                    if (!reminderMinutes.contains(reminderTimes)) {
                        reminderMinutes.add(reminderTimes);
                        reminders.add(reminder);
                    }
                }
            }
        }
        profileItem.setVisaExpirationDateReminder(reminders);

        //position
        profileItem.setPositionId(null);
        if (positionPanel.getSelectedItemID() != null && positionPanel.getSelectedItemID() != -1) {
            profileItem.setPositionId(positionPanel.getSelectedItemID());
            profileItem.setPosition(positionPanel.getSelectedItem().getName());
            profileItem.setApplyPositionLeaveForEmployee(applyLeaveAllowanceForEmployee.getValue());
        }
        //bank account information
        if (isBankInformationEntered()) {
            UserBankAccountData bankAccountData = new UserBankAccountData();
            bankAccountData.setBankName(bankName.getText());
            bankAccountData.setBankAddress(bankAddress.getText());
            bankAccountData.setAccountNumber(accountNumber.getText());
            bankAccountData.setAccountName(accountName.getText());
            bankAccountData.setSwiftCode(swiftCode.getText());
            bankAccountData.setSortCode(sortCode.getText());
            bankAccountData.setIbanCode(iBanCode.getText());
            bankAccountData.setAgentID(agentID.getText());
            profileItem.setBankAccountData(bankAccountData);
        } else {
            profileItem.setBankAccountData(null);
        }
        //employee roles
        if (!mainRole.getSelectItemIDs().isEmpty() && (essUser.getValue() && !noAccess.getValue()) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ESS_AS_PROJECT_MANAGER)) {
            profileItem.setRoleId(mainRole.getSelectItemIDs().toArray(new Integer[]{}));
        } else if (!mainRole.getSelectItemIDs().isEmpty() && (!essUser.getValue() && !noAccess.getValue())) {
            profileItem.setRoleId(mainRole.getSelectItemIDs().toArray(new Integer[]{}));
        }
        profileItem.setFingerprintDeviceId(fingerprintDevices.getSelectItemIDs().toArray(new Integer[]{}));
        profileItem.setNoAccess(noAccess.getValue());
        profileItem.setEss(essUser.getValue());
        //courses
        if (courses != null && !courses.getSelectedData().isEmpty()) {
            ArrayList<SelectItem> selectedItems = courses.getSelectedData().stream()
                    .map(it -> new SelectItem(it.getId(), it.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            profileItem.setCoursesItems(selectedItems);
        }

        //employee custom fields
        if (showCustomFields) {
            contactListItem.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        }
        //attachments
        if (uploadForm != null) {
            profileItem.setAttachments(uploadForm.getAttachedFiles());
        }
        //Personal Identity Information
        profileItem.setPassportNumber(passportNumber.getText());
        profileItem.setPassportIssueDate(passportIssueDate.getDate() != null ? new DateNonConvertable(passportIssueDate.getDate()) : null);
        profileItem.setPassportExpiryDate(passportExpiryDate.getDate() != null ? new DateNonConvertable(passportExpiryDate.getDate()) : null);
        profileItem.setMedicalInsuranceExpireDate(medicalInsuranceExDate.getDate() != null ? new DateNonConvertable(medicalInsuranceExDate.getDate()) : null);
        profileItem.setVisaNumber(visaNumber.getText());
        profileItem.setVisaIssueDate(visaIssueDate.getDate() != null ? new DateNonConvertable(visaIssueDate.getDate()) : null);
        profileItem.setInsuranceNumber(insuranceNumber.getText());
        if (passportIssue.getSelectedItem() != null) {
            profileItem.setPassportIssueItem(passportIssue.getSelectedItem());
        }
        profileItem.setPayments(getTableItems(PayrollConstants.CATEGORY_PAYMENT));
        profileItem.setDeductions(getTableItems(PayrollConstants.CATEGORY_DEDUCTION));
        profileItem.setTaxes(getTableItems(PayrollConstants.CATEGORY_TAX));
        profileItem.setEmployerContributions(getTableItems(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION));
        profileItem.setLoans(getLoanTableItems());
        if (experienceTable != null && experienceTable.getRowCount() > 0) {
            profileItem.setExperienceTableItems(getExperienceTableItems());
        }

        HashMap<String, String> payrollSettings = new HashMap<>();
        payrollSettings.put(WPS_NUMBER, wpsNumber.getText());
        profileItem.setPayrollSettings(payrollSettings);
        if (probationDays.getText() != null && probationDays.getText().matches("^-?\\d+(\\.\\d{1,10})?")) {
            profileItem.setProbationDays(Double.parseDouble(probationDays.getText()));
        }
        if (openingBalanceDays.getText() != null && openingBalanceDays.getText().matches("^-?\\d+(\\.\\d{1,10})?")) {
            profileItem.setOpeningBalanceDays(Double.parseDouble(openingBalanceDays.getText()));
        }
        profileItem.setDeletedCategories(deletedCategories);
        profileItem.setInactiveCategories(inactiveCategories);
        profileItem.setCustomTableItems(getCustomObjectData());

        return profileItem;
    }


    protected ContactListItem setValuesCCC(ContactListItem... newItem) {
        if (newItem != null && newItem.length > 0) {
            contactListItem = newItem[0];
        }
        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }
        //contact () ID
        if (employeeContactID != null) {
            contactListItem.setObjectId(employeeContactID);
        } else {
            //contact type (default EMPLOYEE_CONTACT)
            contactListItem.setContactType(contactType);
        }
        // Personal Information
        //first name
        contactListItem.setFirstName(firstName.getText());
        //middle name
        contactListItem.setMiddleName(middleName.getText());
        //last name
        contactListItem.setLastName(lastName.getText());
        //other name
        contactListItem.setOtherName(otherName.getText());
        //title
        if (titl.isSomethingSelected()) {
            contactListItem.setTitleId(titl.getSelectedItem().getId());
            if (wfmStrings.other().equalsIgnoreCase(titl.getSelectedItem().getName())) {
                if (title.getText() != null) {
                    contactListItem.setTitle(title.getText());
                }
            }
        } else {
            contactListItem.setTitleId(null);
        }
        //date of birth
        if (birthDatePicker.getDate() != null) {
            contactListItem.setBirthDate(new DateNonConvertable(birthDatePicker.getDate()));
        }
        // Contact Information
        setEmail();
        setPhoneNumber();
        setImAddress();
        setWebAddress();
        setAddressData();
        setTelegramBots();
        return contactListItem;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void save(boolean saveAndClose) {
        enableButton(false);
        if (!validation()) {
            enableButton(true);
            Info.show(wfmStrings.pleasefillintherequiredfields(), Info.Type.WARNING);
            return;
        } else if (hireDatePicker != null && fireDatePicker != null && hireDatePicker.getDate() != null && fireDatePicker.getDate() != null
                && hireDatePicker.getDate().after(fireDatePicker.getDate())) {
                    enableButton(true);
                    Info.show(wfmStrings.hireDateCannotBeAfterResignDate(), Info.Type.WARNING);
                    markAsError(hireDatePicker, true);
                    markAsError(fireDatePicker, true);
                    return;
        }
        setValues();
        if (fromSectionTCInstructor) {
            successMessage = wfmStrings.yourInstructorHasBeenSaved();
            errorMessage = wfmStrings.errorOccurredSavingChanges();
        }

        String from = fromSectionTCInstructor ? TC_INSTRUCTOR_ADD_FORM : null;
        ReportService.App.get().isEmployeeNumberExists(profileItem.getEmpCode(), profileItem.getEmployeeId(), from, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                try {
                    throw throwable;
                } catch (NumberExistingException ex) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, ex.getDetailedMessage());
                    messageBox.setTitle(wfmStrings.error());
                    messageBox.open();
                } catch (Throwable ex) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }

            @Override
            public void success(Boolean exist) {
                if (exist) {
                    Info.show(wfmStrings.empWithThisNumberExists(), Info.Type.WARNING);
                    enableButton(true);
                } else {
                    LoadingPanel.loading(true);
                    ContactService.App.get().updateProfile(profileItem, new AbstractAsyncCallback<Integer>() {

                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            enableButton(true);
                            Info.show((fromSectionPMEmployeeEdit ? wfmStrings.errorOccurredUpdate() : errorMessage), Info.Type.WARNING);
                        }

                        public void success(Integer o) {
                            LoadingPanel.loading(false);
                            enableButton(true);

                            if (o != null) {
                                if (Errors.EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS == o) {
                                    Info.show((fromSectionTCInstructor ? wfmStrings.instructorWithEmailAlreadyExist() : wfmStrings.employeeWithEmailAlreadyExist()), Info.Type.WARNING);
                                } else if (Errors.EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST == o) {
                                    Info.show(wfmStrings.invalidEmail(), Info.Type.WARNING);
                                } else if (Errors.CAN_NOT_CREATE_EMPLOYEE == o) {
                                    Info.show((fromSectionTCInstructor ? wfmStrings.canNotCreateInstructor() : wfmStrings.canNotCreateAnEmployee()), Info.Type.WARNING);
                                } else if (Errors.ACTIVE_LIMIT_EXCEEDED == o) {
                                    Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                                } else if (Errors.NO_ACCESS_LIMIT_EXCEEDED == o) {
                                    Info.show(wfmStrings.userLimitNoAccessExceeded(), Info.Type.WARNING);
                                } else if (Errors.ESS_LIMIT_EXCEEDED == o) {
                                    Info.show(wfmStrings.userLimitEssExceeded(), Info.Type.WARNING);
                                } else if (Errors.SUPERVISOR_CIRCULAR_REFERENCE == o) {
                                    Info.show(wfmStrings.supervisorCircularReferenceError(), Info.Type.WARNING);
                                } else if (LEAST_ONE_ADMIN_ROLE == o) {
                                    Info.show(wfmStrings.leastOneAdminRole(), Info.Type.WARNING);
                                } else if (EMPLOYEE_LABOUR_PERIOD_USED == o) {
                                    Info.show("The employee's labor period is used", Info.Type.WARNING);
                                } else {

                                    if (employeeID != null && employeeID.equals(Utils.getUserID())) {
                                        Iterator<Integer> iterator = mainRole.getSelectItemIDs().iterator();
                                        StringBuilder sb = new StringBuilder();
                                        while (iterator.hasNext()) {
                                            Integer role = iterator.next();
                                            sb.append(role);
                                            if (iterator.hasNext()) {
                                                sb.append(",");
                                            }
                                        }
                                        Utils.userSettings.put(ROLES, sb.toString());
                                    }
                                    if (fromSectionPMEmployeeEdit) {
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, o, GeneralEmployeeEditForm.this);
                                    }
                                    Info.show((fromSectionPMEmployeeEdit ? Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employee()) : successMessage), Info.Type.INFO);
                                    onShellOk(saveAndClose, o);
                                }
                            }
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, null, GeneralEmployeeEditForm.this);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_BANK_UPDATE, null, GeneralEmployeeEditForm.this);
                        }
                    });
                }
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void drawEmployeeCustomFields() {
        if (showCustomFields) {
            getCustomFieldUtil().drawCustomFields(this, employeeID);
            addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        }
    }

    private boolean isBankInformationEntered() {
        return bankName.getText().trim().length() > 0 || (bankAddress.getText() != null && bankAddress.getText().trim().length() > 0) ||
                accountNumber.getText().trim().length() > 0 || accountName.getText().trim().length() > 0
                || swiftCode.getText().trim().length() > 0 || sortCode.getText().trim().length() > 0 || iBanCode.getText().trim().length() > 0 || agentID.getText().trim().length() > 0;
    }

    private boolean isOtherSelected(DataListBox listBox) {
        return listBox.getSelectedItem() != null && wfmStrings.other().equalsIgnoreCase(listBox.getSelectedItem().getName());
    }

    protected void onShellOk(boolean saveAndClose, Integer objectId) {
        closeTab();
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_PROFILE_UPDATE, objectId, GeneralEmployeeEditForm.this);
        if (!saveAndClose) {
            if (Utils.isSettings()) {
                if (fromSectionProfileSettingsEdit) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("profileSettingsHome|profile/" + objectId, profileItem.getEmpCode() != null && !profileItem.getEmpCode().isEmpty() ? profileItem.getEmpCode() : profileItem.getFirstName(), profileItem.getFirstName());
                }
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + objectId, profileItem.getEmpCode() != null && !profileItem.getEmpCode().isEmpty() ? profileItem.getEmpCode() : profileItem.getFirstName(), profileItem.getFirstName());
            }
        }
    }

//    private void refreshLocationDropDown(final Integer locationID) {
//        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
//            public void failure(Throwable throwable) {
//            }
//
//            public void success(SelectItem[] selectItems) {
//                if (selectItems != null) {
//                    locations.clear();
//                    locations.setItems(selectItems);
//                }
//                if (locationID != null) {
//                    locations.setSelected(locationID);
//                }
//            }
//        });
//    }

    /**
     * @return WidgetsMap
     */
    private WidgetsMap getAddressWidgets(Address address) {
        if (isAddView) {
            AddressWidgetAddView addressWidget = new AddressWidgetAddView(address, false, (employeeContactID != null ? employeeContactID.toString() : "add"), false, false);
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
            AddressNewUIWidget addressWidget = new AddressNewUIWidget(address, true, (employeeContactID != null ? employeeContactID.toString() : "add"), false, false, filterParameter);
            final WidgetsMap widgetsMap = new WidgetsMap();
            widgetsMap.addWidgetToMap(MultiTable.ADDRESS, addressWidget);
            widgetsMap.addToLeft(null, addressWidget.primaryField);
            widgetsMap.addToCenter(null, addressWidget.nameField);
            widgetsMap.addToCenter(null, addressWidget.addressViewField);
            widgetsMap.addToRight(null, addressWidget.editButton);
            widgetsMap.addWidgets(addressWidget);
            return widgetsMap;
        }
    }

    private WidgetsMap getEmailWidgets(String name, Integer id, String primaryEmail) {
        WidgetsMap widgetsMap = new WidgetsMap();
        TextBox email = new TextBox();
        if (name != null) {
            email.setText(name);
        } else {
            email.setText(wfmStrings.email());
            email.addStyleName("search-textbox");
        }
        /*email.addClickHandler(event -> {
            if (wfmStrings.email().equals(email.getText())) {
                email.setText("");
                email.removeStyleName("search-textbox");
            }
        });*/
        email.addFocusHandler(focusEvent -> {
            if (wfmStrings.email().equals(email.getText())) {
                email.setText("");
                email.removeStyleName("search-textbox");
            }
        });
        email.addBlurHandler(blurEvent -> {
            if (email.getText() != null && !"".equals(email.getText()) && !wfmStrings.email().equals(email.getText())) {
                email.setText(email.getText().toLowerCase().trim());
            }
        });
        email.addStyleName(DEFAULT_WIDTH);

        DataListBox emailLocation = new DataListBox();
        emailLocation.setVisible(false);
        emailLocation.getElement().setClassName("");
        //emailLocation.addStyleName("multiwidget-sub");
        //emailLocation.setWidth("85px");
        emailLocation.setWithoutNullLabel(true);
        emailLocation.setItems(getAddress());
        if (id != null) {
            emailLocation.setSelected(id);
        } else {
            emailLocation.setSelected(AddressReference.WORK.getId());
        }
        KpiRadioButton primary = new KpiRadioButton("primaryEmail");
        new KpiToolTip(primary, wfmStrings.primary());
        if (primaryEmail != null && name != null && !"".equals(name.trim()) && !"".equals(primaryEmail.trim()) && primaryEmail.trim().equalsIgnoreCase(name.trim())) {
            primary.setValue(true);
        } else if (name == null && id == null && emailInf == null) {
            primary.setValue(true);
        }
        widgetsMap.addToLeft(PRIMARY_RADIO_BUTTON, primary);
        widgetsMap.add(RELATION_LIST_BOX, emailLocation);
        widgetsMap.add(PARAM_TEXT_BOX, email);
        if (formProperty != null && formProperty.get(CustomFormConstants.EMAIL) != null) {
            primary.setEnabled(!formProperty.get(CustomFormConstants.EMAIL).isDisabled());
            emailLocation.setEnabled(!formProperty.get(CustomFormConstants.EMAIL).isDisabled());
            email.setEnabled(!formProperty.get(CustomFormConstants.EMAIL).isDisabled());
        }
        return widgetsMap;
    }

    private WidgetsMap getIMAddressWidgets(String name, Integer id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        TextBox imAddress = new TextBox();
        if (name != null) {
            imAddress.setText(name);
        }
        imAddressLocation = new DataListBox();
        imAddressLocation.setWithoutNullLabel(true);
        if (id != null) {
            imAddressLocation.setSelected(id);
        }
        widgetsMap.addToCenter(PARAM_TEXT_BOX, imAddress);
        widgetsMap.addToLeft(RELATION_LIST_BOX, imAddressLocation);
        if (formProperty != null && formProperty.get(CustomFormConstants.IM_ADDRESS) != null) {
            imAddress.setEnabled(!formProperty.get(CustomFormConstants.IM_ADDRESS).isDisabled());
            imAddressLocation.setEnabled(!formProperty.get(CustomFormConstants.IM_ADDRESS).isDisabled());
        }
        return widgetsMap;
    }

    private WidgetsMap getPhoneWidgets(String name, Integer id, String primaryPhone) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final PhoneNumber phone = new PhoneNumber("");
        if (name != null) {
            phone.setData(name);
        }
        final DataListBox phoneLocation = new DataListBox();
        phoneLocation.addStyleName(test_code_ID_name + "phone_type");
        phoneLocation.addStyleName("multiwidget-sub");
        phoneLocation.setWidth("85px");
        phoneLocation.setWithoutNullLabel(true);
        phoneLocation.setItems(getPhoneNumber());
        if (id != null) {
            phoneLocation.setSelected(id);
        } else {
            phoneLocation.setSelected(PhoneReference.WORK.getId());
        }

//        phoneLocation.addValueChangeHandler(event -> {
//            if (phoneLocation.getSelectedId() != null && phoneLocation.getSelectedId().equals(PhoneReference.EXTENSION.getId())) {
//                phone.onlyExternal(true);
//            } else {
//                phone.onlyExternal(false);
//            }
//        });
        KpiRadioButton primary = new KpiRadioButton("primaryPhone");
        new KpiToolTip(primary, wfmStrings.primary());
        primary.addStyleName(test_code_ID_name + "primary_checkbox");
        if (primaryPhone != null && name != null && !"".equals(name.trim()) && !"".equals(primaryPhone.trim()) && primaryPhone.trim().equalsIgnoreCase(name.trim())) {
            primary.setValue(true);
        } else if (name == null && id == null && phoneNumInf == null) {
            primary.setValue(true);
        }
        widgetsMap.addToLeft(PRIMARY_RADIO_BUTTON, primary);
        widgetsMap.addToCenter(RELATION_LIST_BOX, phoneLocation);
        widgetsMap.addWidgets(phone.getPhoneFeild());
        widgetsMap.add(MultiTable.PHONE_NUMBER, phone);
        if (id != null && id.equals(PhoneReference.EXTENSION.getId())) {
            phone.onlyExternal(true);
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null) {
            primary.setEnabled(!formProperty.get(CustomFormConstants.PHONE).isDisabled());
            phoneLocation.setEnabled(!formProperty.get(CustomFormConstants.PHONE).isDisabled());
            phone.setEnabled(!formProperty.get(CustomFormConstants.PHONE).isDisabled());
        }
        return widgetsMap;
    }

    private WidgetsMap getWebSiteWidgets(String name, Integer id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        TextBox webAddress = new TextBox();
        webAddress.addStyleName(test_code_ID_name + "address");
        if (name != null) {
            webAddress.setText(name);
        }
        DataListBox webAddressLocation = new DataListBox();
        webAddressLocation.setWithoutNullLabel(true);
        webAddressLocation.setItems(getWebAddress());
        if (id != null) {
            webAddressLocation.setSelected(id);
        } else {
            webAddressLocation.setSelected(WebAddressReference.WORK.getId());
        }
        widgetsMap.addToCenter(PARAM_TEXT_BOX, webAddress);
        widgetsMap.addToLeft(RELATION_LIST_BOX, webAddressLocation);
        if (formProperty != null && formProperty.get(CustomFormConstants.WEB_ADDRESS) != null) {
            webAddress.setEnabled(!formProperty.get(CustomFormConstants.WEB_ADDRESS).isDisabled());
            webAddressLocation.setEnabled(!formProperty.get(CustomFormConstants.WEB_ADDRESS).isDisabled());
        }
        return widgetsMap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static SelectItem[] getAddress() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(AddressReference.HOME.getId(), wfmStrings.contacthome());
        items[1] = new SelectItem(AddressReference.WORK.getId(), wfmStrings.contactwork());
        items[2] = new SelectItem(AddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }

    private static SelectItem[] getPhoneNumber() {
        SelectItem[] items = new SelectItem[5];
        items[0] = new SelectItem(PhoneReference.HOME.getId(), wfmStrings.contacthome());
        items[1] = new SelectItem(PhoneReference.WORK.getId(), wfmStrings.contactwork());
        items[2] = new SelectItem(PhoneReference.MOBILE.getId(), wfmStrings.mobile());
//        items[3] = new SelectItem(PhoneReference.WORKFAX.getId(), wfmStrings.workFax());
//        items[4] = new SelectItem(PhoneReference.HOMEFAX.getId(), wfmStrings.homeFax());
//        items[3] = new SelectItem(PhoneReference.PAGER.getId(), wfmStrings.pager());
        items[3] = new SelectItem(PhoneReference.OTHER.getId(), wfmStrings.other());
        items[4] = new SelectItem(PhoneReference.EXTENSION.getId(), wfmStrings.extension());
        return items;
    }

    private static SelectItem[] getWebAddress() {
        SelectItem[] items = new SelectItem[7];
        items[1] = new SelectItem(WebAddressReference.WORK.getId(), wfmStrings.contactwork());
        items[0] = new SelectItem(WebAddressReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(WebAddressReference.HOMEPAGE.getId(), wfmStrings.contacthomePage());
        items[3] = new SelectItem(WebAddressReference.FTP.getId(), wfmStrings.ftp());
        items[4] = new SelectItem(WebAddressReference.BLOG.getId(), wfmStrings.blog());
        items[5] = new SelectItem(WebAddressReference.PROFILE.getId(), wfmStrings.contactprofile());
        items[6] = new SelectItem(WebAddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setAddressWidgets() {
        addressInf.remove(0);
        if (contactListItem.getAddresses() != null && contactListItem.getAddresses().size() > 0) {
            address = contactListItem.getAddresses().get(0);
            if (address != null) {
                address.setObjectID(null);
            }
            for (int i = 0; i < contactListItem.getAddresses().size(); i++) {
                addressInf.addWidgets(getAddressWidgets(contactListItem.getAddresses().get(i)));
            }
        } else {
            if (isAddView) {
                if (address == null) {
                    Address address1 = new Address();
                    address1.setPrimary(true);
                    addressInf.addColumnsWidget(getAddressWidgets(address1));
                }
            } else {
                addressInf.addWidgets(getAddressWidgets(address));
            }
        }
    }

    private void setWebSiteWidgets() {
        if (!contactListItem.isItemParamsEmpty(Constants.CONTACT_WEBSITES)) {
            webSiteInf.remove(0);
        }
        setMultiTableItems(Constants.CONTACT_WEBSITES);
    }

    private void setImAddressWidgets() {
        if (!contactListItem.isItemParamsEmpty(Constants.CONTACT_IMADDRESSES)) {
            imsAddressInf.remove(0);
        }
        setMultiTableItems(Constants.CONTACT_IMADDRESSES);
    }

    private void setPhoneNumbersWidgets() {
        if (!contactListItem.isItemParamsEmpty(Constants.CONTACT_PHONES)) {
            phoneNumInf.remove(0);
        }
        setMultiTableItems(Constants.CONTACT_PHONES);
    }

    private void setEmailDetailsWidgets() {
        if (!contactListItem.isItemParamsEmpty(Constants.CONTACT_EMAILS)) {
            emailInf.remove(0);
        }
        setMultiTableItems(Constants.CONTACT_EMAILS);
    }

    private void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(contactListItem, param);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                int relation = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value.trim())) {
                        switch (param) {
                            case CONTACT_EMAILS:
                                emailInf.addWidgets(getEmailWidgets(value, relation, contactListItem.getPrimaryEmail()));
                                break;
                            case CONTACT_PHONES:
                                phoneNumInf.addWidgets(getPhoneWidgets(value, relation, contactListItem.getPrimaryPhone()));
                                break;
                            case CONTACT_IMADDRESSES:
                                imsAddressInf.addWidgets(getIMAddressWidgets(value, relation));
                                break;
                            case CONTACT_WEBSITES:
                                webSiteInf.addWidgets(getWebSiteWidgets(value, relation));
                                break;
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setAddressData() {
        contactListItem.setAddresses(new ArrayList<>());
        if (isAddView) {
            for (Map<String, Widget> addressRow : addressInf.getColumnsWidget()) {
                AddressWidgetAddView addressWidget = (AddressWidgetAddView) addressRow.get(ADDRESS);
                if (addressWidget.isNotEmpty()) {
                    contactListItem.getAddresses().add(addressWidget.getAddress());
                }
            }
        } else {
            for (Map<String, Widget> addressRow : addressInf.getWidgets()) {
                AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                if (addressWidget.isNotEmpty()) {
                    contactListItem.getAddresses().add(addressWidget.getAddress());
                }
            }
        }
    }

    private void setContactType(int contactType) {
        this.contactType = contactType;
    }

    private void setEmail() {
        contactListItem.setEmails();
        for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
            TextBox value = (TextBox) emailRow.get(PARAM_TEXT_BOX);
            if (!isEmpty(value.getText()) && !wfmStrings.email().equals(value.getText()) && Utils.validateEmail(value.getText(), false)) {
                DataListBox emailType = (DataListBox) emailRow.get(RELATION_LIST_BOX);
                KpiRadioButton primary = (KpiRadioButton) emailRow.get(PRIMARY_RADIO_BUTTON);
                contactListItem.addParam(Constants.CONTACT_EMAILS, emailType.getSelectedId(), value.getText());
                if (primary.getValue() != null && primary.getValue()) {
                    contactListItem.setPrimaryEmail(value.getText());
                }
                if (Constants.G_WORK == emailType.getSelectedId(true) && contactListItem.getCrmAccount().isNew()) {
                    contactListItem.getCrmAccount().setEmail(value.getText());
                }
            }
        }
    }

    private void setImAddress() {
        contactListItem.setImAddresses();
        contactListItem.setImmCodes(imAddressMap);
        for (Map<String, Widget> imAddressRow : imsAddressInf.getWidgets()) {
            TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
            if (isEmpty(imAddress.getText())) {
                continue;
            }
            DataListBox imAddressType = (DataListBox) imAddressRow.get(RELATION_LIST_BOX);
            contactListItem.addParam(Constants.CONTACT_IMADDRESSES, imAddressType.getSelectedId(), imAddress.getText());
        }
    }

    private void setPhoneNumber() {
        contactListItem.removeAllPhones();
        for (Map<String, Widget> widgetsMap : phoneNumInf.getWidgets()) {
            PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTable.PHONE_NUMBER);
            DataListBox phoneType = (DataListBox) widgetsMap.get(RELATION_LIST_BOX);
            KpiRadioButton primary = (KpiRadioButton) widgetsMap.get(PRIMARY_RADIO_BUTTON);
            boolean isPrimary = primary.getValue();
            if ("".equals(phoneNumber.toString()) || contactListItem.getAllPhones().contains(phoneNumber.toString())) {
                continue;
            }
            if (contactListItem.getCrmAccount().getObjectId() != null && contactListItem.getCrmAccount().isNew()) {
                if (Constants.G_WORK == phoneType.getSelectedId(true)) {
                    contactListItem.getCrmAccount().setPhone(phoneNumber.toString());
                }
                if (Constants.G_HOME == phoneType.getSelectedId(true)) {
                    contactListItem.setHomePhone(phoneNumber.toString());
                }
                if (Constants.G_MOBILE == phoneType.getSelectedId(true)) {
                    contactListItem.setMobile(phoneNumber.toString());
                }
                if (Constants.G_EXTENSION == phoneType.getSelectedId(true)) {
                    contactListItem.setExtension(phoneNumber.toString());
                }
                if (Constants.G_WORK_FAX == phoneType.getSelectedId(true)) {
                    contactListItem.getCrmAccount().setFax(phoneNumber.toString());
                }
            } else {
                contactListItem.addParam(Constants.CONTACT_PHONES, phoneType.getSelectedId(), phoneNumber.toString());
            }
            if (isPrimary) {
                contactListItem.setPrimaryPhone(phoneNumber.toString());
            }
        }
    }

    private void setWebAddress() {
        contactListItem.setWebAddresses();
        for (Map<String, Widget> webSiteRow : webSiteInf.getWidgets()) {
            TextBox webAddress = (TextBox) webSiteRow.get(PARAM_TEXT_BOX);
            if (isEmpty(webAddress.getText())) {
                continue;
            }
            DataListBox webAddressType = (DataListBox) webSiteRow.get(RELATION_LIST_BOX);
            contactListItem.addParam(Constants.CONTACT_WEBSITES, webAddressType.getSelectedId(), webAddress.getText());
        }
    }

    private ArrayList<PaymentDeductionObject> getTableItems(final String from) {
        EditableTable table = getTable(from);
        ArrayList<PaymentDeductionObject> list = new ArrayList<>();
        if (table != null) {
            for (int i = 0; i < table.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(i, "amount");
                Widget typeWidget = table.getColumnById(i, "type");
                DataListBox paymentType = (DataListBox) table.getColumnById(i, "paymentType");
                if (categoryLookUp.getSelectedItem() != null && amountWidget.getAmount() != null) {
                    PaymentDeductionObject object = new PaymentDeductionObject();
                    object.setCategoryItem(categoryLookUp.getSelectedData());

                    if (typeWidget instanceof LinkedTypeWidget) {
                        LinkedTypeWidget type = (LinkedTypeWidget) table.getColumnById(i, "type");
                        object.setType(type.getSelectedId());
                        object.setFromAllAllowances(type.isFromAllAllowances());
                        if (!object.isFromAllAllowances()) {
                            object.setLinkedCategories(type.getLinkedCategories());
                        }
                    } else {
                        DataListBox type = (DataListBox) table.getColumnById(i, "type");
                        object.setType(type.getSelectedId());
                    }
                    if (object.getType() == 0) {
                        object.setPaymentAmount(amountWidget.getAmount());
                    } else {
                        object.setPercentage(amountWidget.getAmount());
                    }

                    if (paymentType != null && paymentType.getSelectedId() != null) {
                        object.setPaymentType(EPPaymentType.findById(paymentType.getSelectedId()));
                    }
                    object.setPsdId(amountWidget.getPsdId());
                    object.setId(amountWidget.getItemID());
                    list.add(object);
                }
            }
        }
        return list;
    }

    private ArrayList<PaymentDeductionObject> getLoanTableItems() {
        ArrayList<PaymentDeductionObject> list = new ArrayList<>();
        if (loansTable != null) {
            for (int i = 0; i < loansTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) loansTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) loansTable.getColumnById(i, "amount");
                CustomDatePicker startDate = (CustomDatePicker) loansTable.getColumnById(i, "startDate");
                DataListBox type = (DataListBox) loansTable.getColumnById(i, "type");
                if (categoryLookUp.getSelectedData() != null && amountWidget.getAmount() != null && startDate.getDate() != null) {
                    EditableTextBox totalAmount = (EditableTextBox) loansTable.getColumnById(i, "totalAmount");
                    BigDecimal total = parseToBigDecimal(totalAmount.getText());
                    PaymentDeductionObject object = new PaymentDeductionObject();
                    object.setCategoryItem(categoryLookUp.getSelectedData());
                    object.setType(type.getSelectedId());
                    if (object.getType() == 0) {
                        object.setPaymentAmount(amountWidget.getAmount());
                    } else {
                        object.setPercentage(amountWidget.getAmount());
                        object.setPaymentAmount(total.multiply(object.getPercentage().divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                    }
                    object.setStarttDate(new DateNonConvertable(startDate.getDate()));
                    object.setTotalAmount(total);
                    object.setId(amountWidget.getItemID());
                    list.add(object);
                }
            }
        }
        return list;
    }

    private BigDecimal parseToBigDecimal(String text) { //Couldn't find an appropriate alternative, thus created onei
        if (text != null && text.length() > 0) {
            String currencyCode = Utils.getParam(Utils.BASE_CURRENCY);
            if (currencyCode != null && text.startsWith(currencyCode)) {
                return BigDecimal.valueOf(Utils.getNumberFormat().parse(text.replace(currencyCode, "")));
            }
            return BigDecimal.valueOf(Utils.getNumberFormat().parse(text));
        }
        return BigDecimal.ZERO;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////--validations begin--/////////////////////////////////////////////////
    protected boolean validation() {
        clearErrorStyle();
        int errors = customValidate();
//        errors += markAsError(firstName, firstName.getText() == null || "".equals(firstName.getText()));

        if (formProperty != null && formProperty.get(CustomFormConstants.FIRST_NAME) != null && formProperty.get(CustomFormConstants.FIRST_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.FIRST_NAME, firstName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.FIRST_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), firstName, formProperty.get(CustomFormConstants.FIRST_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LAST_NAME) != null && formProperty.get(CustomFormConstants.LAST_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.LAST_NAME, lastName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.LAST_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), lastName, formProperty.get(CustomFormConstants.LAST_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MIDDLE_NAME) != null && formProperty.get(CustomFormConstants.MIDDLE_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.MIDDLE_NAME, middleName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.MIDDLE_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName(), middleName, formProperty.get(CustomFormConstants.MIDDLE_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.BIRTH_DAY) != null && formProperty.get(CustomFormConstants.BIRTH_DAY).isRequired()) {
            errors += markAsError(CustomFormConstants.BIRTH_DAY, birthDatePicker, birthDatePicker.getDate() == null);
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MARTIAL_STATUS) != null && formProperty.get(CustomFormConstants.MARTIAL_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.MARTIAL_STATUS, martialStatus, !Validation.validateListBoxRequired(martialStatus));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GENDER) != null && formProperty.get(CustomFormConstants.GENDER).isRequired()) {
            errors += markAsError(CustomFormConstants.GENDER, genderTable, !male.getValue() && !female.getValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.DEPARTMENT) != null && formProperty.get(CustomFormConstants.DEPARTMENT).isRequired()) {
            errors += markAsError(CustomFormConstants.DEPARTMENT, pmDepartment, !Validation.validateLookUpRequired(pmDepartment));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE) != null && formProperty.get(CustomFormConstants.EMPLOYEE_DEGREE).isRequired()) {
            errors += markAsError(CustomFormConstants.EMPLOYEE_DEGREE, employeeDegree, !Validation.validateListBoxRequired(employeeDegree));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PAYMENT_METHOD) != null && formProperty.get(CustomFormConstants.PAYMENT_METHOD).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYMENT_METHOD, paymentMethodTable, !minSalaryMethod.getValue() && !maxSalaryMethod.getValue() && midSalaryMethod.getValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.SUPERVISOR) != null && formProperty.get(CustomFormConstants.SUPERVISOR).isRequired()) {
            errors += markAsError(CustomFormConstants.SUPERVISOR, reportsTo, !Validation.validateLookUpRequired(reportsTo));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYMENT_MODE) != null && formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).isRequired()) {
            errors += markAsError(CustomFormConstants.EMPLOYMENT_MODE, empMode, !Validation.validateListBoxRequired(empMode));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LANGUAGE) != null && formProperty.get(CustomFormConstants.LANGUAGE).isRequired()) {
            errors += markAsError(CustomFormConstants.LANGUAGE, languagesWidget, !(languagesWidget.getLanguages() != null && languagesWidget.getLanguages().size() > 0));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LOCATION_FIELD) != null && formProperty.get(CustomFormConstants.LOCATION_FIELD).isRequired()) {
            errors += markAsError(CustomFormConstants.LOCATION_FIELD, locations, !Validation.validateLookUpRequired(locations));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.QUALIFICATION) != null && formProperty.get(CustomFormConstants.QUALIFICATION).isRequired()) {
            errors += markAsError(CustomFormConstants.QUALIFICATION, employeeQualification, !Validation.validateListBoxRequired(employeeQualification));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.TIMESLOT) != null && formProperty.get(CustomFormConstants.TIMESLOT).isRequired()) {
            errors += markAsError(CustomFormConstants.TIMESLOT, timeslot, !Validation.validateListBoxRequired(timeslot));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.POSITION) != null && formProperty.get(CustomFormConstants.POSITION).isRequired()) {
            errors += markAsError(CustomFormConstants.POSITION, positionPanel, !Validation.validateLookUpRequired(positionPanel));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.NATIONALITY) != null && formProperty.get(CustomFormConstants.NATIONALITY).isRequired()) {
            errors += markAsError(CustomFormConstants.NATIONALITY, nationality, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.NATIONALITY).isChanged() ?
                    formProperty.get(CustomFormConstants.NATIONALITY).getTitle() : wfmStrings.nationality(), nationality, formProperty.get(CustomFormConstants.NATIONALITY).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_NUMBER) != null && formProperty.get(CustomFormConstants.PASSPORT_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.PASSPORT_NUMBER, passportNumber, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.PASSPORT_NUMBER).isChanged() ?
                    formProperty.get(CustomFormConstants.PASSPORT_NUMBER).getTitle() : wfmStrings.passportNumber(), passportNumber, formProperty.get(CustomFormConstants.PASSPORT_NUMBER).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_NUMBER) != null && formProperty.get(CustomFormConstants.VISA_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.VISA_NUMBER, visaNumber, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.VISA_NUMBER).isChanged() ?
                    formProperty.get(CustomFormConstants.VISA_NUMBER).getTitle() : wfmStrings.visaNumber(), visaNumber, formProperty.get(CustomFormConstants.VISA_NUMBER).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.SALARY_AMOUNT) != null && formProperty.get(CustomFormConstants.SALARY_AMOUNT).isRequired()) {
            errors += markAsError(CustomFormConstants.SALARY_AMOUNT, salaryAmount, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.SALARY_AMOUNT).isChanged() ?
                    formProperty.get(CustomFormConstants.SALARY_AMOUNT).getTitle() : wfmStrings.basicSalary(), salaryAmount, formProperty.get(CustomFormConstants.SALARY_AMOUNT).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.INSURANCE_NUMBER) != null && formProperty.get(CustomFormConstants.INSURANCE_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.INSURANCE_NUMBER, insuranceNumber, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.INSURANCE_NUMBER).isChanged() ?
                    formProperty.get(CustomFormConstants.INSURANCE_NUMBER).getTitle() : wfmStrings.insuranseNumber(), insuranceNumber, formProperty.get(CustomFormConstants.INSURANCE_NUMBER).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE) != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE).isRequired()) {
            errors += markAsError(CustomFormConstants.PASSPORT_ISSUE, passportIssue, !Validation.validateLookUpRequired(passportIssue));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE) != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.PASSPORT_ISSUE_DATE, passportIssueDate, !Validation.validateDate(passportIssueDate));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE) != null && formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.PASSPORT_EXPIRY_DATE, passportExpiryDate, !Validation.validateDate(passportExpiryDate));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_ISSUE_DATE) != null && formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.VISA_ISSUE_DATE, visaIssueDate, !Validation.validateDate(visaIssueDate));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE) != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.VISA_EXPIRATION_DATE, visaExpirationDate, !Validation.validateDate(visaExpirationDate));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE) != null && formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.INSURANCE_EXPIRY_DATE, medicalInsuranceExDate, !Validation.validateDate(medicalInsuranceExDate));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.HIRE_DATE) != null && formProperty.get(CustomFormConstants.HIRE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.HIRE_DATE, hireDatePicker, !Validation.validateDate(hireDatePicker));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.RESIGNATION_DATE) != null && formProperty.get(CustomFormConstants.RESIGNATION_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.RESIGNATION_DATE, fireDatePicker, !Validation.validateDate(fireDatePicker));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.WAGE_RATE) != null && formProperty.get(CustomFormConstants.WAGE_RATE).isRequired()) {
            errors += markAsError(CustomFormConstants.WAGE_RATE, wageRate, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.WAGE_RATE).isChanged() ?
                    formProperty.get(CustomFormConstants.WAGE_RATE).getTitle() : wfmStrings.wageRate(), wageRate, formProperty.get(CustomFormConstants.WAGE_RATE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE) != null && formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isRequired()) {
            errors += markAsError(CustomFormConstants.CLIENT_CHARGE_RATE, clientChargeRate, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isChanged() ?
                    formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).getTitle() : wfmStrings.clientChargeRate(), clientChargeRate, formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PROBATION_DAYS) != null && formProperty.get(CustomFormConstants.PROBATION_DAYS).isRequired()) {
            errors += markAsError(CustomFormConstants.PROBATION_DAYS, probationDays, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.PROBATION_DAYS).isChanged() ?
                    formProperty.get(CustomFormConstants.PROBATION_DAYS).getTitle() : wfmStrings.probationPeriodDays(), probationDays, formProperty.get(CustomFormConstants.PROBATION_DAYS).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE) != null && formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDays, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).isChanged() ?
                    formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).getTitle() : wfmStrings.openingBalanceDate(), openingBalanceDays, formProperty.get(CustomFormConstants.OPENING_BALANCE_DATE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.BANK_NAME) != null && formProperty.get(CustomFormConstants.BANK_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.BANK_NAME, bankName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.BANK_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.BANK_NAME).getTitle() : wfmStrings.bankName(), bankName, formProperty.get(CustomFormConstants.BANK_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.SWIFT_CODE) != null && formProperty.get(CustomFormConstants.SWIFT_CODE).isRequired()) {
            errors += markAsError(CustomFormConstants.SWIFT_CODE, swiftCode, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.SWIFT_CODE).isChanged() ?
                    formProperty.get(CustomFormConstants.SWIFT_CODE).getTitle() : wfmStrings.swiftCode(), swiftCode, formProperty.get(CustomFormConstants.SWIFT_CODE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.WPS_NUMBER) != null && formProperty.get(CustomFormConstants.WPS_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.WPS_NUMBER, wpsNumber, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.WPS_NUMBER).isChanged() ?
                    formProperty.get(CustomFormConstants.WPS_NUMBER).getTitle() : wfmStrings.wpsNumber(), wpsNumber, formProperty.get(CustomFormConstants.WPS_NUMBER).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ACCOUNT_NAME) != null && formProperty.get(CustomFormConstants.ACCOUNT_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.ACCOUNT_NAME, accountName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.ACCOUNT_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.ACCOUNT_NAME).getTitle() : wfmStrings.accountNumber(), accountName, formProperty.get(CustomFormConstants.ACCOUNT_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.IBAN_CODE) != null && formProperty.get(CustomFormConstants.IBAN_CODE).isRequired()) {
            errors += markAsError(CustomFormConstants.IBAN_CODE, iBanCode, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.IBAN_CODE).isChanged() ?
                    formProperty.get(CustomFormConstants.IBAN_CODE).getTitle() : wfmStrings.ibanCode(), iBanCode, formProperty.get(CustomFormConstants.IBAN_CODE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ACCOUNT_NUMBER) != null && formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.ACCOUNT_NUMBER, accountNumber, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).isChanged() ?
                    formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNumber(), accountNumber, formProperty.get(CustomFormConstants.ACCOUNT_NUMBER).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.SORT_CODE) != null && formProperty.get(CustomFormConstants.SORT_CODE).isRequired()) {
            errors += markAsError(CustomFormConstants.SORT_CODE, sortCode, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.SORT_CODE).isChanged() ?
                    formProperty.get(CustomFormConstants.SORT_CODE).getTitle() : wfmStrings.sortCode(), sortCode, formProperty.get(CustomFormConstants.SORT_CODE).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.AGENT_ID) != null && formProperty.get(CustomFormConstants.AGENT_ID).isRequired()) {
            errors += markAsError(CustomFormConstants.AGENT_ID, agentID, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.AGENT_ID).isChanged() ?
                    formProperty.get(CustomFormConstants.AGENT_ID).getTitle() : wfmStrings.agentID(), agentID, formProperty.get(CustomFormConstants.AGENT_ID).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYEE_CODE) != null && formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getMinChar() != null && !formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getMinChar().equals("")) {
            int error = markAsError(CustomFormConstants.EMPLOYEE_CODE, empCode, empCode.getNumberData(true).getNumberString().trim().length() != Integer.parseInt(formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getMinChar()));
            errors += error;
            if (error > 0) {
                Info.warn(wfmMessages.allowedCharLimit(formProperty.get(CustomFormConstants.EMPLOYEE_CODE).isChanged() ?
                        formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getTitle() : wfmStrings.employeeCode(), formProperty.get(CustomFormConstants.EMPLOYEE_CODE).getMinChar()));
            }
        }

        if (!Utils.isSettings()) {
            errors += markAsError(empCode, !empCode.validate(false));
            if (termsOfContractBox.getText() != null && !"".equals(termsOfContractBox.getText())) {
                errors += markAsError(monthYearBox, monthYearBox.getSelectedId() == null);
            }
            if (employeeID != null && employeeID.equals(reportsTo.getSelectedItemID())) {
                errors++;
            }
        }

        if (!fromSectionTCInstructor) {
            if (mainRole != null && mainRole.getSelectItemIDs().size() > 0) {
                for (Integer selectedID : mainRole.getSelectItemIDs()) {
                    if (ADMIN_LOCATION.equals(selectedID)) {
                        errors += markAsError(locations, !Validation.validateLookUpRequired(locations));
                        break;
                    }
                }
            }
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.WAGE_RATE) != null && formProperty.get(CustomFormConstants.WAGE_RATE).isRequired()) {
            errors += markAsError(wageRate, !Validation.validateTextBoxRequired(wageRate));
        }
        try {
            if (wageRate.getText() != null && !"".equals(wageRate.getText())) {
                Double.valueOf(wageRate.getText());
            }
        } catch (NumberFormatException ex) {
            errors += markAsError(wageRate, true);
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE) != null &&
                formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).isRequired()) {
            errors += markAsError(clientChargeRate, !Validation.validateTextBoxRequired(clientChargeRate));
        }
        try {
            if (clientChargeRate.getText() != null && !"".equals(clientChargeRate.getText())) {
                Double.valueOf(clientChargeRate.getText());
            }
        } catch (NumberFormatException ex) {
            errors += markAsError(clientChargeRate, true);
        }


        if (formProperty != null && formProperty.get(CustomFormConstants.EMAIL) != null && formProperty.get(CustomFormConstants.EMAIL).isRequired() && !noAccess.getValue()) {
            boolean ident = false;
            TextBox email;
            for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
                email = (TextBox) emailRow.get(PARAM_TEXT_BOX);
                if (!wfmStrings.email().equals(email.getText()) && Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.EMAIL).isChanged() ?
                        formProperty.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), email, formProperty.get(CustomFormConstants.EMAIL).getMinChar())
                        && Validation.validEmailFormat(email.getText(), false)) {
                    ident = true;
                } else {
                    errors += markAsError(CustomFormConstants.EMAIL, email, true);
                    email.addKeyUpHandler(event -> {
                        TextBox textbox = (TextBox) event.getSource();
                        if (textbox.getText().length() < 1) {
                            textbox.addStyleName(ERROR_FORM_STYLE);
                        } else {
                            if (!"".equals(textbox.getStyleName())) {
                                textbox.removeStyleName(ERROR_FORM_STYLE);
                            }
                        }
                    });
                }
            }
            if (!ident) {
                errors++;
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null && formProperty.get(CustomFormConstants.PHONE).isRequired()) {
            boolean ident = false;
            for (Map<String, Widget> widgetsMap : phoneNumInf.getWidgets()) {
                PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTable.PHONE_NUMBER);
                if (Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.PHONE).isChanged() ?
                        formProperty.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(), phoneNumber.getPhoneFeild(), formProperty.get(CustomFormConstants.PHONE).getMinChar())) {
                    ident = true;
                } else {
                    errors += markAsError(CustomFormConstants.PHONE, phoneNumber, Validation.validateTextBoxRequired(phoneNumber.getPhoneFeild()));
                }
            }
            if (!ident) {
                errors++;
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.IM_ADDRESS) != null && formProperty.get(CustomFormConstants.IM_ADDRESS).isRequired()) {
            boolean ident = false;
            for (Map<String, Widget> imAddressRow : imsAddressInf.getWidgets()) {
                TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
                if (Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formProperty.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress(), imAddress, formProperty.get(CustomFormConstants.IM_ADDRESS).getMinChar())) {
                    ident = true;
                } else {
                    errors += markAsError(CustomFormConstants.IM_ADDRESS, imsAddressInf, Validation.validateTextBoxRequired(imAddress));
                }
            }
            if (!ident) {
                errors++;
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER) != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER).isRequired()) {
            boolean ident = false;
            for (Map<String, Widget> visaExpirationDateReminderTable : visaExpirationDateReminderTable.getWidgets()) {
                TextBox reminderTable = (TextBox) visaExpirationDateReminderTable.get(PARAM_TEXT_BOX);
                if (Validation.validateTextBoxRequired(reminderTable)) {
                    ident = true;
                } else {
                    errors += markAsError(CustomFormConstants.VISA_EXPIRATION_DATE_REMINDER, reminderTable, Validation.validateTextBoxRequired(reminderTable));
                }
            }
            if (!ident) {
                errors++;
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.WEB_ADDRESS) != null && formProperty.get(CustomFormConstants.WEB_ADDRESS).isRequired()) {
            boolean ident = false;
            for (Map<String, Widget> imAddressRow : webSiteInf.getWidgets()) {
                TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
                if (Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formProperty.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress(), imAddress, formProperty.get(CustomFormConstants.WEB_ADDRESS).getMinChar())) {
                    ident = true;
                } else {
                    errors += markAsError(CustomFormConstants.WEB_ADDRESS, webSiteInf, Validation.validateTextBoxRequired(imAddress));
                }
            }
            if (!ident) {
                errors++;
            }
        }

        if (telegramInf.getWidgets() != null && telegramInf.getWidgets().size() > 0) {
            List<Integer> botIds = new ArrayList<>();
            for (int i = 0; i < telegramInf.getWidgets().size(); i++) {
                HashMap<String, Widget> widgets = telegramInf.getWidgets().get(i);
                DataListBox telegram = (DataListBox) widgets.get(RELATION_LIST_BOX);
                TelegramChatSingleLookUp chatLookUp = (TelegramChatSingleLookUp) widgets.get(RELATED_CHAT);
                botIds.add(telegram.getSelectedId());
                if (telegram.getSelectedId() != null && telegram.getSelectedId() > 0 && chatLookUp.getSelectedItemID() == null) {
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

        if (showCustomFields) {
            errors += getCustomFieldUtil().validateCustomFields();
        }

        if (errors > 0) {
            return false;
        }

        if (Utils.isEmployeesLocked() && hireDatePicker.getDate() != null && DateUtils.getTransactionLockDate().after(hireDatePicker.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.hireOnly(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (passportIssueDate != null && passportIssueDate.getDate() != null && passportExpiryDate != null && passportExpiryDate.getDate() != null && passportExpiryDate.getDate().before(passportIssueDate.getDate())) {
            Info.show(wfmMessages.expireDateCannotBeEarliesThanIssueDate(), Info.Type.WARNING);
            return false;
        }
        if (visaIssueDate != null && visaIssueDate.getDate() != null && visaExpirationDate != null && visaExpirationDate.getDate() != null && visaExpirationDate.getDate().before(visaIssueDate.getDate())) {
            Info.show(wfmMessages.expireDateCannotBeEarliesThanIssueDate(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private boolean validateEmails() {
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

    private boolean validateBankInformationTextBox(final TextBox textBox) {
        if ("".equals(textBox.getText())) {
            textBox.addKeyDownHandler(event -> markAsError(textBox, true));
            return false;
        }
        return true;
    }

    protected void setDefaultValuesByFormProperty() {
        if (formProperty != null && formProperty.get(CustomFormConstants.FIRST_NAME) != null && formProperty.get(CustomFormConstants.FIRST_NAME).getDefaultValue() != null) {
            firstName.setText(formProperty.get(CustomFormConstants.FIRST_NAME).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.WAGE_RATE) != null && formProperty.get(CustomFormConstants.WAGE_RATE).getDefaultValue() != null) {
            wageRate.setText(formProperty.get(CustomFormConstants.WAGE_RATE).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE) != null && formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).getDefaultValue() != null) {
            clientChargeRate.setText(formProperty.get(CustomFormConstants.CLIENT_CHARGE_RATE).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LAST_NAME) != null && formProperty.get(CustomFormConstants.LAST_NAME).getDefaultValue() != null) {
            lastName.setText(formProperty.get(CustomFormConstants.LAST_NAME).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.SALARY_AMOUNT) != null && formProperty.get(CustomFormConstants.SALARY_AMOUNT).getDefaultValue() != null) {
            salaryAmount.setText(formProperty.get(CustomFormConstants.SALARY_AMOUNT).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MIDDLE_NAME) != null && formProperty.get(CustomFormConstants.MIDDLE_NAME).getDefaultValue() != null) {
            middleName.setText(formProperty.get(CustomFormConstants.MIDDLE_NAME).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MARTIAL_STATUS) != null && formProperty.get(CustomFormConstants.MARTIAL_STATUS).getDefaultValue() != null) {
            martialStatus.setSelected(new SelectItem(formProperty.get(CustomFormConstants.MARTIAL_STATUS).getSelectedId(), formProperty.get(CustomFormConstants.MARTIAL_STATUS).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.POSITION) != null && formProperty.get(CustomFormConstants.POSITION).getDefaultValue() != null) {
            positionPanel.setSelected(new SelectItem(formProperty.get(CustomFormConstants.POSITION).getSelectedId(), formProperty.get(CustomFormConstants.POSITION).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.EMPLOYMENT_MODE) != null && formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).getDefaultValue() != null) {
            empMode.setSelected(new SelectItem(formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).getSelectedId(), formProperty.get(CustomFormConstants.EMPLOYMENT_MODE).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.EMAIL) != null && formProperty.get(CustomFormConstants.EMAIL).getDefaultValue() != null) {
            for (Map<String, Widget> emailRow : emailInf.getWidgets()) {
                TextBox value = (TextBox) emailRow.get(PARAM_TEXT_BOX);
                value.setText(formProperty.get(CustomFormConstants.EMAIL).getDefaultValue());
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null && formProperty.get(CustomFormConstants.PHONE).getDefaultValue() != null) {
            for (Map<String, Widget> widgetsMap : phoneNumInf.getWidgets()) {
                PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTable.PHONE_NUMBER);
                phoneNumber.getPhoneFeild().setText(formProperty.get(CustomFormConstants.PHONE).getDefaultValue());
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.IM_ADDRESS) != null && formProperty.get(CustomFormConstants.IM_ADDRESS).getDefaultValue() != null) {
            for (Map<String, Widget> imAddressRow : imsAddressInf.getWidgets()) {
                TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
                imAddress.setText(formProperty.get(CustomFormConstants.IM_ADDRESS).getDefaultValue());
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.WEB_ADDRESS) != null && formProperty.get(CustomFormConstants.WEB_ADDRESS).getDefaultValue() != null) {
            for (Map<String, Widget> imAddressRow : webSiteInf.getWidgets()) {
                TextBox imAddress = (TextBox) imAddressRow.get(PARAM_TEXT_BOX);
                imAddress.setText(formProperty.get(CustomFormConstants.WEB_ADDRESS).getDefaultValue());
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.DEPARTMENT) != null && formProperty.get(CustomFormConstants.DEPARTMENT).getDefaultValue() != null) {
            pmDepartment.setSelected(new SelectItem(formProperty.get(CustomFormConstants.DEPARTMENT).getSelectedId(), formProperty.get(CustomFormConstants.DEPARTMENT).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.NATIONALITY) != null && formProperty.get(CustomFormConstants.NATIONALITY).getDefaultValue() != null) {
            nationality.setValue(formProperty.get(CustomFormConstants.NATIONALITY).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.SUPERVISOR) != null && formProperty.get(CustomFormConstants.SUPERVISOR).getDefaultValue() != null) {
            reportsTo.setSelected(new SelectItem(formProperty.get(CustomFormConstants.SUPERVISOR).getSelectedId(), formProperty.get(CustomFormConstants.SUPERVISOR).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LOCATION_FIELD) != null && formProperty.get(CustomFormConstants.LOCATION_FIELD).getDefaultValue() != null) {
            locations.setSelected(new SelectItem(formProperty.get(CustomFormConstants.LOCATION_FIELD).getSelectedId(), formProperty.get(CustomFormConstants.LOCATION_FIELD).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE) != null && formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue() != null) {
            if (!"".equals(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                passportIssueDate.setDate(currentDate);
            } else {
                try {
                    passportIssueDate.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.HIRE_DATE) != null && formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue() != null) {
            if (!"".equals(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                hireDatePicker.setDate(currentDate);
            } else {
                try {
                    hireDatePicker.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.HIRE_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formProperty != null && formProperty.get(CustomFormConstants.RESIGNATION_DATE) != null && formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue() != null) {
//           datePicker.setDate(DateUtils.fullDateFormat.parse());
            if (!"".equals(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                fireDatePicker.setDate(currentDate);
            } else {
                try {
                    fireDatePicker.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE) != null && formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue() != null) {
//           datePicker.setDate(DateUtils.fullDateFormat.parse());
            if (!"".equals(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                passportExpiryDate.setDate(currentDate);
            } else {
                try {
                    passportExpiryDate.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_ISSUE_DATE) != null && formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue() != null) {
//           datePicker.setDate(DateUtils.fullDateFormat.parse());
            if (!"".equals(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                visaIssueDate.setDate(currentDate);
            } else {
                try {
                    visaIssueDate.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.VISA_ISSUE_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formProperty != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE) != null && formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue() != null) {
//           datePicker.setDate(DateUtils.fullDateFormat.parse());
            if (!"".equals(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                visaExpirationDate.setDate(currentDate);
            } else {
                try {
                    visaExpirationDate.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.VISA_EXPIRATION_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formProperty != null && formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE) != null && formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue() != null) {
//           datePicker.setDate(DateUtils.fullDateFormat.parse());
            if (!"".equals(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue()) && ("TODAY".equals(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue()) || "TOMORROW".equals(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                medicalInsuranceExDate.setDate(currentDate);
            } else {
                try {
                    medicalInsuranceExDate.setDate(DateUtils.parse(formProperty.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PROBATION_DAYS) != null && formProperty.get(CustomFormConstants.PROBATION_DAYS).getDefaultValue() != null) {
            probationDays.setText(formProperty.get(CustomFormConstants.PROBATION_DAYS).getDefaultValue());
        }


    }

    //////////////////////////////////////////////--validations end--///////////////////////////////////////////////////
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

    public class CustomDatePicker extends DatePicker implements CustomCellInterface {
        private DateTimeFormat dateFormatter = DateUtils.getFormat();
        private Date date;

        @Override
        public String getDisplayValue() {
            return getDate() != null ? DateUtils.format(getDate()) : wfmStrings.pleaseSelect();
        }

        @Override
        public void setItemValue(Object value) {
            setDate((Date) value);
        }

        @Override
        public Date getDate() {
            if (getText() != null && !getText().isEmpty() && !wfmStrings.pleaseSelect().equals(getText()) && !dateFormatter.getPattern().equals(getText())) {
                try {
                    if (dateFormatter == null) {
                        dateFormatter = DateUtils.getFormat();
                    }
                    date = dateFormatter.parse(getText());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                return date;
            }
            return null;
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

    }

    private void onCalculateBasicTotalSalary() {
        BigDecimal basicSalary = salaryAmount.getValue() != null && !salaryAmount.getValue().trim().equals("") ? BigDecimal.valueOf(extendedNumberFormat.parse(salaryAmount.getValue())) : BigDecimal.ZERO;
        BigDecimal grandTotalPayment = BigDecimal.ZERO;
        BigDecimal grandTotalDeduction = BigDecimal.ZERO;
        BigDecimal grandTotalTax = BigDecimal.ZERO;

        //Calc Payments
        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            DataListBox type = (DataListBox) paymentsTable.getColumnById(i, "type");
            if (type.getSelectedItem() == null || type.getSelectedItem().getId() == 0) {
                grandTotalPayment = grandTotalPayment.add(amountWidget.getAmount());
            } else {
                grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            }
        }

        //Calc Deductions
        for (int i = 0; i < deductionsTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
            LinkedTypeWidget type = (LinkedTypeWidget) deductionsTable.getColumnById(i, "type");
            if (type.getSelectedId() == null || type.getSelectedId() == 0) {
                grandTotalDeduction = grandTotalDeduction.add(amountWidget.getAmount());
            } else if (type.getSelectedId() == 1) {
                grandTotalDeduction = grandTotalDeduction.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            } else if (type.getSelectedId() == 2) {
                if (type.getLinkedCategories() != null && !type.getLinkedCategories().isEmpty()) {
                    List<PaymentDeductionObject> linkedAllowance = new ArrayList<>();
                    List<Integer> linkedIds = new ArrayList<>();
                    for (PaymentDeductionObject linkedCategory : type.getLinkedCategories()) {
                        linkedIds.add(linkedCategory.getCategoryItem().getId());
                    }
                    for (PaymentDeductionObject payment : profileItem.getPaymentCategories()) {
                        if (linkedIds.contains(payment.getCategoryItem().getId())) {
                            linkedAllowance.add(payment);
                        }
                    }
                    BigDecimal linkedTotal = BigDecimal.ZERO;
                    for (PaymentDeductionObject allowance : linkedAllowance) {
                        if (allowance.getType() == 1) {
                            linkedTotal = linkedTotal.add((basicSalary.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                        } else {
                            linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                        }
                    }
                    grandTotalDeduction = grandTotalDeduction.add(((basicSalary.add(linkedTotal)).multiply(amountWidget.getAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                }
            } else {//todo add allowances
                BigDecimal amount = basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                grandTotalDeduction = grandTotalDeduction.add(amount);
            }
        }

        //Calc Taxes
        for (int i = 0; i < taxTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
            LinkedTypeWidget type = (LinkedTypeWidget) taxTable.getColumnById(i, "type");
            if (type.getSelectedId() == null || type.getSelectedId() == 0) {
                grandTotalTax = grandTotalTax.add(amountWidget.getAmount());
            } else if (type.getSelectedId() == 1) {
                grandTotalTax = grandTotalTax.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            } else if (type.getSelectedId() == 2) {
                if (type.getLinkedCategories() != null && !type.getLinkedCategories().isEmpty()) {
                    List<PaymentDeductionObject> linkedAllowance = new ArrayList<>();
                    List<Integer> linkedIds = new ArrayList<>();
                    for (PaymentDeductionObject linkedCategory : type.getLinkedCategories()) {
                        linkedIds.add(linkedCategory.getCategoryItem().getId());
                    }
                    for (PaymentDeductionObject payment : profileItem.getPaymentCategories()) {
                        if (linkedIds.contains(payment.getCategoryItem().getId())) {
                            linkedAllowance.add(payment);
                        }
                    }
                    BigDecimal linkedTotal = BigDecimal.ZERO;
                    for (PaymentDeductionObject allowance : linkedAllowance) {
                        if (allowance.getType() == 1) {
                            linkedTotal = linkedTotal.add((basicSalary.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                        } else {
                            linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                        }
                    }
                    grandTotalTax = grandTotalTax.add(((basicSalary.add(linkedTotal)).multiply(amountWidget.getAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                }
            } else {//todo add allowances
                BigDecimal amount = basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                grandTotalTax = grandTotalTax.add(amount);
            }
        }

        for (int i = 0; i < employerContributionTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) employerContributionTable.getColumnById(i, "amount");
            DataListBox type = (DataListBox) employerContributionTable.getColumnById(i, "type");
            if (type.getSelectedItem() == null || type.getSelectedItem().getId() == 0) {
                grandTotalPayment = grandTotalPayment.add(amountWidget.getAmount());
            } else {
                grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            }
        }

//        salaryTotalAmount.setValue(extendedNumberFormat.format(basicSalary.add(grandTotalPayment).subtract(grandTotalDeduction).subtract(grandTotalTax)));
    }

    private void calculateBasicTotalSalary() {
        BigDecimal basicSalary = BigDecimal.ZERO;
        if (salaryAmount.getValue() != null && !salaryAmount.getValue().trim().isEmpty()) {
            basicSalary = BigDecimal.valueOf(extendedNumberFormat.parse(salaryAmount.getValue()));
        }
        BigDecimal grandTotalPayment = BigDecimal.ZERO;
        BigDecimal grandTotalDeduction = BigDecimal.ZERO;

        if (profileItem.getPaymentCategories() != null && profileItem.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeduction : profileItem.getPaymentCategories()) {
                if (paymentDeduction.getPaymentAmount() != null) {
                    if (paymentDeduction.getType() == null || paymentDeduction.getType() == 0) {
                        grandTotalPayment = grandTotalPayment.add(paymentDeduction.getPaymentAmount());
                    } else {
                        grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                }
            }
        }

        if (profileItem.getDeductionCategories() != null && profileItem.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeduction : profileItem.getDeductionCategories()) {
                if (paymentDeduction.getPaymentAmount() != null) {
                    if (paymentDeduction.getType() == null || paymentDeduction.getType() == 0) {
                        grandTotalDeduction = grandTotalDeduction.add(paymentDeduction.getPaymentAmount());
                    } else if (paymentDeduction.getType() == 2) {
                        if (paymentDeduction.getLinkedCategories() != null && !paymentDeduction.getLinkedCategories().isEmpty()) {
                            List<PaymentDeductionObject> linkedAllowance = new ArrayList<>();
                            List<Integer> linkedIds = new ArrayList<>();
                            for (PaymentDeductionObject linkedCategory : paymentDeduction.getLinkedCategories()) {
                                linkedIds.add(linkedCategory.getCategoryItem().getId());
                            }
                            for (PaymentDeductionObject payment : profileItem.getPaymentCategories()) {
                                if (linkedIds.contains(payment.getCategoryItem().getId())) {
                                    linkedAllowance.add(payment);
                                }
                            }
                            BigDecimal linkedTotal = BigDecimal.ZERO;
                            for (PaymentDeductionObject allowance : linkedAllowance) {
                                if (allowance.getType() == 1) {
                                    linkedTotal = linkedTotal.add((basicSalary.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                                } else {
                                    linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                                }
                            }
                            grandTotalDeduction = grandTotalDeduction.add(((basicSalary.add(linkedTotal)).multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                        }
                    } else {
                        grandTotalDeduction = grandTotalDeduction.add((basicSalary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                }
            }
        }

//        salaryTotalAmount.setValue(extendedNumberFormat.format(basicSalary.add(grandTotalPayment).subtract(grandTotalDeduction)));
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
            chatLookUp.setSelected(new SelectItem(telegramItem.getEntityId(), telegramItem.getCode()));
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

    private void setTelegramWidgets() {
        if (contactListItem.getTelegramChats() != null && !contactListItem.getTelegramChats().isEmpty()) {
            telegramInf.clear();
            for (SelectItem item : contactListItem.getTelegramChats()) {
                telegramInf.addWidgets(getTelegramWidgets(item));
            }
        }
    }

    private void setTelegramBots() {
        ArrayList<SelectItem> telegramChats = new ArrayList<>();
        for (Map<String, Widget> widgetsMap : telegramInf.getWidgets()) {
            TelegramChatSingleLookUp chatId = (TelegramChatSingleLookUp) widgetsMap.get(RELATED_CHAT);
            DataListBox telegramBot = (DataListBox) widgetsMap.get(RELATION_LIST_BOX);
            telegramChats.add(new SelectItem(telegramBot.getSelectedId(), chatId.getSelectedItemID(), null, null));
        }
        contactListItem.setTelegramChats(telegramChats);
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
                            com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker t = (com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker) productTable.getColumnById(i, columnCode);
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

    private void setItemTableValues(Map<String, ArrayList<CustomTableRpc>> tableItems) {
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
}
