package com.edatasite.workforce.gwt.core.client.ui.listpanel.filter;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.lookup.BankLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: dilsh0d
 * Date: 02/09/11
 * Time: 14:48
 */
public class ListingChooseFilter extends KpiModal implements Constants {

    public final static String CLIENT_FIELD = "CLIENT";
    public final static String RECCURING_INVOICE_STATUS_FIELD = "RECCURING_INVOICE_STATUS";
    public final static String RECCURING_INVOICE_RECCURENCE_STATUS_FIELD = "RECCURING_INVOICE_RECCURENCE_STATUS_FIELD";
    public final static String COMPANY_USERS = "COMPANY_USERS";
    public final static String EMPLOYEES = "EMPLOYEES";
    public final static String COMPANY_STATUS = "COMPANY_STATUS";
    public final static String POSITION_DEPARTMENT = "POSITION_DEPARTMENT";
    public final static String POSITION_LOCATION = "POSITION_LOCATION";
    public final static String POSITION_STATUS = "POSITION_STATUS";
    public final static String POSITION_TYPE = "POSITION_TYPE";
    public final static String ROTATION_STATUS = "ROTATION_STATUS";
    public final static String BENEFIT_STATUS = "BENEFIT_STATUS";
    public final static String BENEFIT_APPROVER = "BENEFIT_APPROVER";
    public final static String BENEFIT_REQUESTER = "BENEFIT_REQUESTER";
    public final static String BENEFIT_TYPE = "BENEFIT_TYPE";
    public final static String SHIFT_TYPE = "SHIFT_TYPE";
    public final static String SHIFT_PERIOD = "SHIFT_PERIOD";
    public final static String SHIFT_DEPARTMENT = "SHIFT_DEPARTMENT";
    public final static String SUBSCRIPTION_TYPE = "SUBSCRIPTION_TYPE";
    public final static String FROM_REGISTRATION_DATE = "FROM_REGISTRATION_DATE";
    public final static String TO_REGISTRATION_DATE = "TO_REGISTRATION_DATE";
    public final static String FROM_EXPIRATION_DATE = "FROM_EXPIRATION_DATE";
    public final static String TO_EXPIRATION_DATE = "TO_EXPIRATION_DATE";
    public final static String COUNTRY = "COUNTRY";
    public final static String LOCATION = "LOCATION";
    public final static String CATEGORY_ASSET = "CATEGORY";
    public final static String FROM_DATE = "FROM_DATE";
    public final static String TO_DATE = "TO_DATE";
    public final static String FROM_AMOUNT = "FROM_AMOUNT";
    public final static String TO_AMOUNT = "TO_AMOUNT";
    public final static String RELATED_PROJECT = "RELATED_PROJECT";
    public final static String BANK_NAME = "BANK_NAME";
    public final static String CREATOR = "CREATOR";
    public final static String SHIFT_CREATOR = "SHIFT_CREATOR";
    public final static String ROTATION_CREATOR = "ROTATION_CREATOR";
    public final static String APPROVER = "APPROVER";
    public final static String ROTATION_APPROVER = "ROTATION_APPROVER";
    public final static String DEPARTMENT_ASSET = "DEPARTMENT";
    public final static String DEPARTMENT_STATUS = "DEPARTMENT_STATUS";
    public final static String DEPARTMENT_LOCATION = "DEPARTMENT_LOCATION";
    public final static String DEPARTMENT_PARENT = "DEPARTMENT_PARENT";
    public final static String EMPLOYEE_LOOKUP = "EMPLOYEE_LOOKUP";
    public final static String CUSTOMER = "CUSTOMER";
    public final static String SUPPLIER = "SUPPLIER";
    public final static String ACCOUNT = "ACCOUNT";
    public final static String STATUS = "STATUS";
    public final static String TYPE = "TYPE";
    public final static String TIMESHEET_APPROVERS = "TIMESHEET_APPROVERS";
    public final static String TIMESHEET_APPROVE_STATUS = "TIMESHEET_APPROVE_STATUS";
    public final static String QUANTITY = "QUANTITY";
    public final static String TASK_EMPLOYEES = "TASK_EMPLOYEES";
    public static final String SERIAL_NUMBER = "SERIAL_NUMBER";
    public static final String FROM_EXPIRY_DATE = "FROM_EXPIRY_DATE";
    public static final String TO_EXPIRY_DATE = "TO_EXPIRY_DATE";
    public static final String BATCH_TYPE = "BATCH_TYPE";
    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String CALCULATE_DEPRECIATION = "CALCULATE_DEPRECIATION";

    public static final String BANK_ACCOUNT_CODE = "BANK_ACCOUNT_CODE";
    public static final String BANK_ACCOUNT_NAME = "BANK_ACCOUNT_NAME";
    public static final String BANK_ACCOUNT_NUMBER = "BANK_ACCOUNT_NUMBER";
    public static final String BANK_ACCOUNT_CURRENCY = "BANK_ACCOUNT_CURRENCY";
    public static final String APPRASIAL_EMPLOYEE = "APPRASIAL_EMPLOYEE";
    public static final String ASSESSMENT_INITIATE_BY = "ASSESSMENT_INITIATE_BY";
    public static final String ASSESSMENT_STATUS = "ASSESSMENT_STATUS";
    public static final String VALIDITY_PERIOD = "VALIDITY_PERIOD";

    public final static long CLIENT = 1;
    public final static long PROJECT = 2;
    public final static long DEPARTMENT = 4;
    public final static long EMPLOYEE = 8;
    public final static long REPORTED_BY = 16;
    public final static long ASSESSMENT_TYPE = 32;
    public final static long PROJECT_STATUS = 64;
    public final static long BY_CATEGORY = 128;
    public final static long DATE = 256;
    public final static long E_DEFAULT = 512;
    public final static long RESOLVER = 1024;
    public final static long IN_OUT_TIME_STATUS = 2048;
    public final static long TIME_SLOT_NAME = 4096;
    public final static long BACKEND_USERS = 32768;
    public final static long TIMESHEET_APPROVAL_APPROVERS = 65536;
    public final static long TYPE_LISTS = 131072;
    public final static long EMPLOYEE_POSITION = 262144;
    public final static long TIMESHEET_APPROVAL_STATUS = 1048576;
    public final static long LOCATION_LIST = 2097152;
    public final static long REPORTING_XML_TEMPLATES = 4194304;
    public final static long COMPANY_REPORTING_XML_TEMPLATES = 8388608;
    public final static long ISSUE_STATUS = 536870912;
    public final static long ISSUE_PRIORITY = 1073741824;
    public final static long CLIENT_CONTRACT = 2147483648L;
    public final static long PERIOD_DATE = 4294967296L;
    public final static long DEFAULT = CLIENT + PROJECT + DEPARTMENT + EMPLOYEE;
    public final static long EXPENSE_LIST = EMPLOYEE;
    public final static long ISSUE_LIST = PROJECT + EMPLOYEE + REPORTED_BY + RESOLVER + ISSUE_STATUS + ISSUE_PRIORITY;
    public final static long TIMESHEET_APPROVAL_LIST = 0L;
    public final static long ATTENDANCE_TRACKING_LIST = DEPARTMENT + IN_OUT_TIME_STATUS + TIME_SLOT_NAME + LOCATION_LIST;
//    public final static long ASSESSMENT_ARCHIVE_LIST = EMPLOYEE + ASSESSMENT_INITIATE_BY  + ASSESSMENT_STATUS + VALIDITY_PERIOD;

    public final static long EMPLOYEE_PROMOTIONS_PENALTIES_LIST = 1073741824 + 1;
    public final static long TASK_EMPLOYEE = 5;
    public final static long EMAIL_TEMPLATES = BY_CATEGORY + E_DEFAULT;

    public final static long BY_NEWS_CATEGORIES = BY_CATEGORY + REPORTED_BY + DATE;

    public final static long CONTRACT_LIST = CLIENT_CONTRACT + PERIOD_DATE;

    //    ASSESSMENT_STATUS used as an AgentID
    public final static long EMPLOYEE_LIST = DEPARTMENT + TYPE_LISTS + LOCATION_LIST + EMPLOYEE_POSITION + DATE;

    public final static long SUPERVISOR_FILTER = 8192L;
    public final static long EMPLOYEE_STATUS_FILTER = 16384L;
    public final static long TERMINAL_FILTER = 16777216L;
    public final static long ROLE_FILTER = 33554432L;
    public final static long ATTENDANCE_MARKS_LIST = EMPLOYEE + DATE + LOCATION_LIST + DEPARTMENT + EMPLOYEE_POSITION + TIME_SLOT_NAME + SUPERVISOR_FILTER + TERMINAL_FILTER + EMPLOYEE_STATUS_FILTER + ROLE_FILTER;

    private static final ReportServiceAsync reportService = ReportService.App.get();
    private static final CommonServiceAsync commonService = CommonService.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final String RETURNING_USERS = wfmStrings.returningUsers();
    private static final String PENDING_USERS = wfmStrings.pendingUsers();
    private static final String ACTIVE_USERS = wfmStrings.activeUsers();

    private final WfmButton2 applyButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS);
    private final WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
    private final WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

    private final FlexTable content = new FlexTable();
    private final FlexTable dateTable = new FlexTable();
    private final FlexTable registrationDateTable = new FlexTable();
    private final FlexTable expirationDateTable = new FlexTable();
    private final FlexTable amountTable = new FlexTable();
    private final FlexTable fromToDateTable = new FlexTable();

    private long showWhat;
    private ArrayList<String> fields;
    private ListingFilterParameter fp;
    private boolean reset = false;
    private Integer clientId;
    private Integer projectId;
    private Integer departmentId;
    private Integer employeeId;
    private Integer viewAsId;
    //This is used in accounting lists
    private String accountingListType;
    public static Boolean wCategory = false;
    public static Boolean isDateOneLine = false;
    public static Boolean isAgentID = false;
    private ViewName viewName;
    private ArrayList<DataListBox> customFieldDataListBox;

    public void setStatusname(String statusname) {
        if (statsuAsField != null) {
            statsuAsField.setLabelText(statusname);
        }
    }

    private DataListBox assessmentInitiatedBy;
    private DataListBox assessmentType;
    private DataListBox assessmentStatus;
    private DataListBox assessmentValidityPeriod;

    private WfmForm.Field viewAsField;
    private WfmForm.Field statsuAsField;
    private DatePicker fromDate;
    private DatePicker toDate;
    private DataListBox viewAs;
    private DataListBox client;
    private DataListBox contractClient;
    private DataListBox project;
    private ProjectLookUp projectLookUp;
    private BankLookUp bank;
    private EmployeeLookUpWithCode employeeLookUp;
    private EmployeeLookUpWithCode apprasialEmployeeLookUp;
    private EmployeeLookUpWithCode apprasialInitiator;
    private EmployeeLookUpWithCode shiftEmployeeLookUp;
    private DepartmentLookUp shiftDEpartmentLookUp;
    private EmployeeLookUpWithCode rotationEmployeeLookUp;
    private EmployeeLookUpWithCode rotationApprover;
    private EmployeeLookUpWithCode benefitRequesterLookUp;
    private DataListBox benefitApprover;
    private DataListBox benefitStatus;
    private DataListBox benefitType;
    private DataListBox country;
    private DataListBox companyStatus;
    private DataListBox shiftStatus;
    private DataListBox rotationStatus;
    private DataListBox shiftType;
    private DataListBox departmentStatus;
    private DatePicker shiftPeriod;
    private DataListBox companyUsers;
    private DatePicker fromRegistrationDate;
    private DatePicker toRegistrationDate;
    private DatePicker fromExpirationDate;
    private DatePicker toExpirationDate;
    private DataListBox subscriptionType;
    private DataListBox department;
    private DataListBox employee;
    private DataListBox taskEmployees;
    private DataListBox reportedBy;
    private DataListBox resolver;
    private DataListBox issueStatus;
    private DataListBox issuePriority;
    private DataListBox backendUsers;
    private DataListBox typeList;
    private DataListBox timeSheetApprovalListApprovers;
    private DataListBox timeSheetApprovalStatus;
    private DataListBox projectStatus;
    private DataListBox inOutStatus;
    private DataListBox timeSlotItems;
    private DataListBox locations;
    private DataListBox approvers;
    private DataListBox position;
    private ClickHandler applyButtonHanlder;
    private DataListBox byCategory;
    private DataListBox emaiDefault;
    private DataListBox recurrungInvoiceStatus;
    private DataListBox recurrenceStatus;
    private DataListBox employeePromotionsPenaltiesFilter;
    private EmployeeLookUpWithCode supervisorLookUp;
    private DataListBox employeeStatusFilter;
    private DataListBox terminalFilter;
    private DataListBox roleFilter;
    private CrmAccountLookUp customerLookUp;
    private CrmAccountLookUp supplierLookUp;
    private InvoiceQuoteLookUp quoteLookUp;
    private DataListBox status;
    private DataListBox type;
    private TextBox fromAmount;
    private TextBox toAmount;
    private LinkedHashMap<String, CustomColumnDefinitionConfig> mapColumn = new LinkedHashMap<>();
    private DataListBox columns;
    private DataListBox oper;
    private TextBox numberBox;
    private DataListBox serialNumber;
    private DatePicker fromBatchDate;
    private DatePicker toBatchDate;
    private DataListBox batchType;
    private DataListBox warehouse;
    private DataListBox calculateDepreciation;
    private DataListBox bankAccountCurrency;
    private BankLookUp bankAccountName;
    private BankLookUp bankAccountCode;
    private BankLookUp bankAccountNumber;
    private DepartmentLookUp positionDepartment;
    private DepartmentLookUp departmentLookUp;
    private LocationLookUpWithCode positionLocation;
    private LocationLookUpWithCode departmentLocation;
    private ReferenceLookUp positionType;
    private DataListBox positionStatus;
    private Div locationContainer;
    private Div departmentContainer;
    private Div benefitApproverContainer;

    public ListingChooseFilter(ListingFilterParameter fp, long showWhat) {
        super();
        this.fp = fp;
        this.showWhat = showWhat;
        this.setTitle(wfmStrings.filter());
        this.add(content);
        initialization();
        isDateOneLine = false;
    }

    public ListingChooseFilter(ListingFilterParameter fp, ArrayList<String> fields, ViewName viewName, LinkedHashMap<String, CustomColumnDefinitionConfig> mapColumn) {
        super();
        this.fp = fp;
        this.fields = fields;
        this.setTitle(wfmStrings.filter());
        this.viewName = viewName;
        this.mapColumn = mapColumn;
        this.add(content);
        initialization();
        isDateOneLine = false;
    }

    private void initialization() {
        this.setWidth("500px");

        WfmForm table = new WfmForm();

        content.setWidget(0, 0, table);
        content.getFlexCellFormatter().setAlignment(0, 0, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_TOP);

        addFormBoxes(table);
        addButton(cancelButton);
        addButton(resetButton);
        addButton(applyButton);
    }

    private void addFormBoxes(WfmForm baseForm) {
        viewAs = new DataListBox();
        viewAs.setAllowFirstItem(true);
        viewAs.addValueChangeHandler(arg0 -> {
            reset = false;
            viewAsId = viewAs.getSelectedItem().getId();
            clientList();
            projectsList();
            departmentList();
            employeeList();
        });
        viewAsField = baseForm.addField(wfmStrings.viewAs(), viewAs);

        //Custom Facet Filter Fields
        if (fields != null) {
            baseForm.removeField(viewAsField);
            if (fields.contains(CLIENT_FIELD)) {
                client = new DataListBox();
                baseForm.addField(wfmStrings.customer(), client);
                client.setNullLabel(wfmStrings.all());
                client.setAllowFirstItem(true);
                client.addValueChangeHandler(arg0 -> {
                    reset = false;
                    employeeList();
                });
            }
            if (fields.contains(RECCURING_INVOICE_STATUS_FIELD)) {
                recurrungInvoiceStatus = new DataListBox();
                recurrungInvoiceStatus.addValueChangeHandler(sender -> reset = false);
                baseForm.addField(wfmStrings.status(), recurrungInvoiceStatus);
            }
            if (fields.contains(RECCURING_INVOICE_RECCURENCE_STATUS_FIELD)) {
                recurrenceStatus = new DataListBox();
                recurrenceStatus.addValueChangeHandler(sender -> reset = false);
                baseForm.addField(wfmStrings.recurrenceStatus(), recurrenceStatus);
            }
            if (fields.contains(STATUS)) {
                status = new DataListBox();
                baseForm.addField(wfmStrings.status(), status);
            }
            if (fields.contains(TYPE)) {
                type = new DataListBox();
                if (ViewName.BatchInvoicePaymentView.equals(viewName) || ViewName.BatchPayBillView.equals(viewName)) {
                    baseForm.addField(wfmStrings.paymentType(), type);
                } else {
                    baseForm.addField(wfmStrings.type(), type);
                }
            }
            if (fields.contains(RELATED_PROJECT)) {
                projectLookUp = new ProjectLookUp(null, null);
                projectLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                    reset = false;
                    projectId = projectLookUp.getSelectedItemID();
                    employeeList();
                });
                baseForm.addField(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp);
            }
            if (fields.contains(BANK_NAME)) {
                bank = new BankLookUp();
                baseForm.addField(wfmStrings.bankName(), bank);
            }
            if (fields.contains(CREATOR) || fields.contains(EMPLOYEE_LOOKUP)) {
                employeeLookUp = new EmployeeLookUpWithCode();
                baseForm.addField(ViewName.FixedAssetRegister.equals(viewName) ? wfmStrings.owner() : fields.contains(EMPLOYEE_LOOKUP) ? wfmStrings.employee() : wfmStrings.createdBy(), employeeLookUp);
            }
            if (fields.contains(SHIFT_DEPARTMENT)) {
                shiftDEpartmentLookUp = new DepartmentLookUp();
                baseForm.addField(wfmStrings.department(), shiftDEpartmentLookUp);
            }
            if (fields.contains(SHIFT_CREATOR) || fields.contains(EMPLOYEE_LOOKUP)) {
                shiftEmployeeLookUp = new EmployeeLookUpWithCode();
                baseForm.addField(ViewName.ShiftList.equals(viewName) ? wfmStrings.createdBy() : fields.contains(EMPLOYEE_LOOKUP) ? wfmStrings.employee() : wfmStrings.createdBy(), shiftEmployeeLookUp);
            }
            if (fields.contains(APPRASIAL_EMPLOYEE)) {
                apprasialEmployeeLookUp = new EmployeeLookUpWithCode();
                apprasialEmployeeLookUp.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.employee(), apprasialEmployeeLookUp);
            }
            if (fields.contains(ASSESSMENT_INITIATE_BY)) {
                apprasialInitiator = new EmployeeLookUpWithCode();
                apprasialInitiator.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.initiatedBy(), apprasialInitiator);
            }
            if (fields.contains(ASSESSMENT_STATUS)) {
                assessmentStatus = new DataListBox();
                assessmentStatus.setAllowFirstItem(true);
                assessmentStatus.setNullLabel(wfmStrings.all());
                assessmentStatus.addValueChangeHandler(event -> reset = false);
                baseForm.addField(isAgentID ? wfmStrings.agentID() : wfmStrings.status(), assessmentStatus);
            }
            //Validity period
            if (fields.contains(VALIDITY_PERIOD)) {
                assessmentValidityPeriod = new DataListBox();
                assessmentValidityPeriod.setAllowFirstItem(true);
                assessmentValidityPeriod.setNullLabel(wfmStrings.all());
                assessmentValidityPeriod.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.validityPeriod(), assessmentValidityPeriod);
            }
            if (fields.contains(ROTATION_STATUS)) {
                rotationStatus = new DataListBox();
                rotationStatus.setNullLabel(wfmStrings.all());
                rotationStatus.setAllowFirstItem(true);
                rotationStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), rotationStatus);
            }

            if (fields.contains(ROTATION_APPROVER) || fields.contains(EMPLOYEE_LOOKUP)) {
                rotationApprover = new EmployeeLookUpWithCode();
                baseForm.addField(ViewName.RotationList.equals(viewName) ? wfmStrings.approver() : fields.contains(EMPLOYEE_LOOKUP) ? wfmStrings.employee() : wfmStrings.approver(), rotationApprover);
            }

            if (fields.contains(ROTATION_CREATOR) || fields.contains(EMPLOYEE_LOOKUP)) {
                rotationEmployeeLookUp = new EmployeeLookUpWithCode();
                baseForm.addField(ViewName.RotationList.equals(viewName) ? wfmStrings.createdBy() : fields.contains(EMPLOYEE_LOOKUP) ? wfmStrings.employee() : wfmStrings.createdBy(), rotationEmployeeLookUp);
            }
            if (fields.contains(BENEFIT_REQUESTER) || fields.contains(EMPLOYEE_LOOKUP)) {
                benefitRequesterLookUp = new EmployeeLookUpWithCode();
                baseForm.addField(ViewName.BenefitRequestList.equals(viewName) ? wfmStrings.requester() : fields.contains(EMPLOYEE_LOOKUP) ? wfmStrings.requester() : wfmStrings.requester(), benefitRequesterLookUp);
            }
            if (fields.contains(COMPANY_USERS)) {
                companyUsers = new DataListBox();
                companyUsers.setNullLabel(wfmStrings.all());
                companyUsers.setAllowFirstItem(true);
                companyUsers.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.users(), companyUsers);
            }
            if (fields.contains(COUNTRY)) {
                country = new DataListBox();
                country.setNullLabel(wfmStrings.all());
                country.setAllowFirstItem(true);
                country.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.country(), country);
            }
            if (fields.contains(SHIFT_STATUS)) {
                shiftStatus = new DataListBox();
                shiftStatus.setNullLabel(wfmStrings.all());
                shiftStatus.setAllowFirstItem(true);
                shiftStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), shiftStatus);
            }
            if (fields.contains(SHIFT_PERIOD)) {
                shiftPeriod = new DatePicker();
                shiftPeriod.setOnlyMonthFormat(true);
                shiftPeriod.setDefaultValue();
                shiftPeriod.setDateTimeFormat(DateTimeFormat.getFormat("MMM yyyy"));
                shiftPeriod.addChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.period(), shiftPeriod);
            }

            if (fields.contains(SHIFT_TYPE)) {
                shiftType = new DataListBox();
                shiftType.setNullLabel(wfmStrings.all());
                shiftType.setAllowFirstItem(true);
                shiftType.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.type(), shiftType);
            }
            if (fields.contains(DEPARTMENT_STATUS)) {
                departmentStatus = new DataListBox();
                departmentStatus.setNullLabel(wfmStrings.all());
                departmentStatus.setAllowFirstItem(true);
                departmentStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), departmentStatus);
            }
            if (fields.contains(BENEFIT_STATUS)) {
                benefitStatus = new DataListBox();
                benefitStatus.setNullLabel(wfmStrings.all());
                benefitStatus.setAllowFirstItem(true);
                benefitStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), benefitStatus);
            }
            if (fields.contains(DEPARTMENT_PARENT)) {
                departmentContainer = new Div();
                departmentLookUp = new DepartmentLookUp();
                departmentLookUp.addValueChangeHandler(changeEvent -> reset = false);
                departmentLookUp.getSuggestBox().addSelectionHandler(e -> updateLocation(positionDepartment.getSelectedItemID()));
                departmentContainer.add(departmentLookUp);
                baseForm.addField(wfmStrings.department(), departmentContainer);
            }
            if (fields.contains(BENEFIT_TYPE)) {
                benefitType = new DataListBox();
                benefitType.setNullLabel(wfmStrings.all());
                benefitType.setAllowFirstItem(true);
                benefitType.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.type(), benefitType);
            }
            if (fields.contains(BENEFIT_APPROVER)) {
                benefitApprover = new DataListBox();
                benefitApprover.setNullLabel(wfmStrings.all());
                benefitApprover.setAllowFirstItem(true);
                benefitApprover.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.approver(), benefitApprover);
            }
            if (fields.contains(COMPANY_STATUS)) {
                companyStatus = new DataListBox();
                companyStatus.setNullLabel(wfmStrings.all());
                companyStatus.setAllowFirstItem(true);
                companyStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), companyStatus);
            }
            if (fields.contains(FROM_REGISTRATION_DATE) && fields.contains(TO_REGISTRATION_DATE)) {

                fromRegistrationDate = new DatePicker();
                fromRegistrationDate.setDefaultValue();

                toRegistrationDate = new DatePicker();
                toRegistrationDate.setDefaultValue();

                fromRegistrationDate.setWidth("85px");
                toRegistrationDate.setWidth("85px");

                registrationDateTable.setWidget(0, 1, new HTML(wfmStrings.from()));
                registrationDateTable.setWidget(0, 2, fromRegistrationDate);
                registrationDateTable.setWidget(0, 3, new HTML(wfmStrings.to()));
                registrationDateTable.setWidget(0, 4, toRegistrationDate);
                baseForm.addField(wfmStrings.registrationDate(), registrationDateTable);
            }
            if (fields.contains(FROM_EXPIRATION_DATE) && fields.contains(TO_EXPIRATION_DATE)) {

                fromExpirationDate = new DatePicker();
                fromExpirationDate.setDefaultValue();

                toExpirationDate = new DatePicker();
                toExpirationDate.setDefaultValue();

                fromExpirationDate.setWidth("85px");
                toExpirationDate.setWidth("85px");

                expirationDateTable.setWidget(0, 1, new HTML(wfmStrings.from()));
                expirationDateTable.setWidget(0, 2, fromExpirationDate);
                expirationDateTable.setWidget(0, 3, new HTML(wfmStrings.to()));
                expirationDateTable.setWidget(0, 4, toExpirationDate);
                baseForm.addField(wfmStrings.expiryDate(), expirationDateTable);
            }

            if (fields.contains(SUBSCRIPTION_TYPE)) {
                subscriptionType = new DataListBox();
                subscriptionType.setNullLabel(wfmStrings.all());
                subscriptionType.setAllowFirstItem(true);
                subscriptionType.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.isPaid(), subscriptionType);
            }
            if (fields.contains(CUSTOMER)) {
                customerLookUp = new CrmAccountLookUp(CUSTOMER, false);
                baseForm.addField(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), customerLookUp);
            }
            if (fields.contains(SALE_QUOTE)) {
                quoteLookUp = new InvoiceQuoteLookUp(Constants.SALE_QUOTE);
                baseForm.addField(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), quoteLookUp);
            }

            if (fields.contains(SUPPLIER)) {
                supplierLookUp = new CrmAccountLookUp(SUPPLIER, false);
                baseForm.addField(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), supplierLookUp);
            }
            if (fields.contains(PURCHASE_ORDER)) {
                quoteLookUp = new InvoiceQuoteLookUp(Constants.PURCHASE_ORDER);
                baseForm.addField(wfmStrings.purchaseorder(), quoteLookUp);
            }
            if (fields.contains(FROM_AMOUNT) && fields.contains(TO_AMOUNT)) {
                fromAmount = new TextBox();
                toAmount = new TextBox();

                fromAmount.setWidth("72px");
                toAmount.setWidth("72px");

                fromAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                Validation.addNumericKeyboardListener(fromAmount, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);

                toAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                Validation.addNumericKeyboardListener(toAmount, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);

                amountTable.setWidget(0, 0, new HTML(wfmStrings.from()));
                amountTable.setWidget(0, 1, fromAmount);
                amountTable.setWidget(0, 2, new HTML(wfmStrings.to()));
                amountTable.setWidget(0, 3, toAmount);
                amountTable.addStyleName("fromToRow--cost");
                baseForm.addField(ViewName.FixedAssetRegister.equals(viewName) ? wfmStrings.cost() : wfmStrings.amount(), amountTable);
            }

            if (fields.contains(FROM_DATE) && fields.contains(TO_DATE)) {

                fromDate = new DatePicker();
                fromDate.setDefaultValue();

                toDate = new DatePicker();
                toDate.setDefaultValue();

                fromDate.setWidth("110px");
                toDate.setWidth("110px");

                dateTable.setWidget(0, 1, new HTML(wfmStrings.from()));
                dateTable.setWidget(0, 2, fromDate);
                dateTable.setWidget(0, 3, new HTML(wfmStrings.to()));
                dateTable.setWidget(0, 4, toDate);
                dateTable.addStyleName("fromToRow--creation");
                baseForm.addField(ViewName.EmployeeHistoryList.equals(viewName) ? wfmStrings.modifiedDate() : ViewName.FixedAssetRegister.equals(viewName) ? wfmStrings.purchaseDate() : wfmStrings.datePeriod(), dateTable);
            }
            if (fields.contains(TIMESHEET_APPROVERS)) {
                timeSheetApprovalListApprovers = new DataListBox();
                timeSheetApprovalListApprovers.setNullLabel(wfmStrings.all());
                timeSheetApprovalListApprovers.setAllowFirstItem(true);
                timeSheetApprovalListApprovers.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.approver(), timeSheetApprovalListApprovers);
            }
            if (fields.contains(QUANTITY)) {
                columns = new DataListBox();
                int count = 0;
                ArrayList<SelectItem> items = new ArrayList<>();
                for (CustomColumnDefinitionConfig column : mapColumn.values()) {
                    if (CustomColumnDefinitionConfig.DataType.BigDecimal.equals(column.getDataType())) {
                        items.add(new SelectItem(count++, (String) column.getColumnName(), column.getCodeName()));
                    }
                }
                columns.setItems(items.toArray(new SelectItem[]{}));
                if (!items.isEmpty()) {
                    columns.setSelected(items.get(0));
                }
                oper = new DataListBox();
                oper.setWithoutNullLabel(true);
                SelectItem[] range_symbols = new SelectItem[]{new SelectItem(0, ">")
                        , new SelectItem(1, ">=")
                        , new SelectItem(2, "<")
                        , new SelectItem(3, "<=")};
                oper.setItems(range_symbols);
                numberBox = new TextBox();
                int scale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;
                Validation.addNumericKeyboardListener(numberBox, scale);
                GBox gbox = new GBox(new GBoxRow(
                        new GBoxItem(wfmStrings.column(), columns),
                        new GBoxItem(wfmStrings.operationType(), oper),
                        new GBoxItem(wfmStrings.value(), numberBox)
                ));
                baseForm.addField(wfmStrings.filter(), gbox);
            }
            if (fields.contains(EMPLOYEES)) {
                employee = new DataListBox();
                employee.setNullLabel(wfmStrings.all());
                employee.setAllowFirstItem(true);
                employee.addValueChangeHandler(arg0 -> reset = false);
                statsuAsField = baseForm.addField(wfmStrings.employee(), employee);
            }
            if (fields.contains(TASK_EMPLOYEES)) {
                taskEmployees = new DataListBox();
                taskEmployees.setNullLabel(wfmStrings.all());
                taskEmployees.setAllowFirstItem(true);
                taskEmployees.addValueChangeHandler(arg0 -> reset = false);
                baseForm.addField(wfmStrings.assignees(), taskEmployees);
            }
            if (fields.contains(TIMESHEET_APPROVE_STATUS)) {
                timeSheetApprovalStatus = new DataListBox();
                timeSheetApprovalStatus.setNullLabel(wfmStrings.all());
                timeSheetApprovalStatus.setAllowFirstItem(true);
                timeSheetApprovalStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), timeSheetApprovalStatus);
            }
            if (fields.contains(ACCOUNT)) {
                customerLookUp = new CrmAccountLookUp(CUSTOMER, false);
                baseForm.addField(wfmStrings.account(), customerLookUp);
            }
            if (fields.contains(LOCATION)) {
                locations = new DataListBox();
                locations.setAllowFirstItem(true);
                locations.setNullLabel(wfmStrings.all());
                locations.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(Property.getPluralWithObjectCode(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), locations);
            }
            if (fields.contains(DEPARTMENT_ASSET)) {
                department = new DataListBox();
                department.setAllowFirstItem(true);
                department.addValueChangeHandler(arg0 -> reset = false);
                baseForm.addField(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), department);
            }
            if (fields.contains(SERIAL_NUMBER)) {
                serialNumber = new DataListBox();
                serialNumber.setAllowFirstItem(true);
                serialNumber.setNullLabel(wfmStrings.all());
                serialNumber.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.serialNumber(), serialNumber);
            }
            if (fields.contains(POSITION_DEPARTMENT)) {
                departmentContainer = new Div();
                positionDepartment = new DepartmentLookUp();
                positionDepartment.addValueChangeHandler(changeEvent -> reset = false);
                positionDepartment.getSuggestBox().addSelectionHandler(e -> updateLocation(positionDepartment.getSelectedItemID()));
                departmentContainer.add(positionDepartment);
                baseForm.addField(wfmStrings.department(), departmentContainer);
            }
            if (fields.contains(POSITION_LOCATION)) {
                locationContainer = new Div();
                positionLocation = new LocationLookUpWithCode();
                positionLocation.addValueChangeHandler(changeEvent -> reset = false);
                locationContainer.add(positionLocation);
                locationSelectionHandler(positionLocation);
                baseForm.addField(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), locationContainer);
            }
            if (fields.contains(DEPARTMENT_LOCATION)) {
                locationContainer = new Div();
                departmentLocation = new LocationLookUpWithCode();
                departmentLocation.addValueChangeHandler(changeEvent -> reset = false);
                locationContainer.add(departmentLocation);
                locationSelectionHandler(departmentLocation);
                baseForm.addField(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), locationContainer);
            }
            if (fields.contains(POSITION_TYPE)) {
                positionType = new ReferenceLookUp("POSITION_TYPE");
                positionType.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.type(), positionType);
            }
            if (fields.contains(POSITION_STATUS)) {
                positionStatus = new DataListBox();
                positionStatus.setAllowFirstItem(true);
                positionStatus.addValueChangeHandler(arg0 -> reset = false);
                baseForm.addField(wfmStrings.status(), positionStatus);
            }
            if (fields.contains(FROM_EXPIRY_DATE) && fields.contains(TO_EXPIRY_DATE)) {
                fromBatchDate = new DatePicker();
                fromBatchDate.setDefaultValue();

                toBatchDate = new DatePicker();
                toBatchDate.setDefaultValue();

                fromBatchDate.setWidth("110px");
                toBatchDate.setWidth("110px");
                fromToDateTable.setWidget(0, 1, new HTML(wfmStrings.from()));
                fromToDateTable.setWidget(0, 2, fromBatchDate);
                fromToDateTable.setWidget(0, 3, new HTML(wfmStrings.to()));
                fromToDateTable.setWidget(0, 4, toBatchDate);
                baseForm.addField(wfmStrings.expiryDate(), fromToDateTable);
            }
            if (fields.contains(BATCH_TYPE)) {
                batchType = new DataListBox();
                batchType.setAllowFirstItem(true);
                batchType.setNullLabel(wfmStrings.all());
                batchType.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.type(), batchType);
            }
            if (fields.contains(WAREHOUSE)) {
                warehouse = new DataListBox();
                warehouse.setAllowFirstItem(true);
                warehouse.setNullLabel(wfmStrings.all());
                warehouse.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(accountingStrings.warehouse(), warehouse);
            }

            if (fields.contains(CALCULATE_DEPRECIATION)) {
                calculateDepreciation = new DataListBox();
                calculateDepreciation.setAllowFirstItem(true);
                calculateDepreciation.addValueChangeHandler(changeEvent -> reset = false);
                calculateDepreciation.setItems(new SelectItem[]{
                        new SelectItem(1, "Yes"),
                        new SelectItem(2, "No")
                });
                baseForm.addField(wfmStrings.calculateDepreciation(), calculateDepreciation);
            }

            if (fields.contains(BANK_ACCOUNT_NAME)) {
                bankAccountName = new BankLookUp(Constants.BANK_ACCOUNT_NAME);
                baseForm.addField(wfmStrings.name(), bankAccountName);

            }
            if (fields.contains(BANK_ACCOUNT_CODE)) {
                bankAccountCode = new BankLookUp(Constants.B_ACCOUNT_CODE);
                baseForm.addField(wfmStrings.code(), bankAccountCode);
            }
            if (fields.contains(BANK_ACCOUNT_NUMBER)) {
                bankAccountNumber = new BankLookUp(Constants.B_ACCOUNT_NUMBER);
                baseForm.addField(wfmStrings.accountNumber(), bankAccountNumber);
            }
            if (fields.contains(BANK_ACCOUNT_CURRENCY)) {
                bankAccountCurrency = new DataListBox();
                bankAccountCurrency.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.currency(), bankAccountCurrency);
            }

        } else {
            // Client
            if ((showWhat & CLIENT) != 0) {
                client = new DataListBox();
                baseForm.addField(wfmStrings.customer(), client);
                client.setNullLabel(wfmStrings.all());
                client.setAllowFirstItem(true);
                client.addValueChangeHandler(arg0 -> {
                    reset = false;
                    projectsList();
                    departmentList();
                    employeeList();
                });
            }


            // Project
            if ((showWhat & PROJECT) != 0) {
                project = new DataListBox();
                project.setNullLabel(wfmStrings.all());
                project.setAllowFirstItem(true);
                project.addValueChangeHandler(arg0 -> {
                    reset = false;
                    if (project.getSelectedItem() != null) {
                        projectId = project.getSelectedItem().getId();
                    } else {
                        projectId = null;
                    }
                    clientList();
                    departmentList();
                    employeeList();
                });
                baseForm.addField(Property.get(Constants.PROJECT, wfmStrings.project()), project);
            }

            //Department
            if ((showWhat & DEPARTMENT) != 0) {
                department = new DataListBox();
                department.setNullLabel(wfmStrings.all());
                department.setAllowFirstItem(true);
                department.addValueChangeHandler(arg0 -> {
                    reset = false;
                    clientList();
                    projectsList();
                    employeeList();
                });
                baseForm.addField(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), department);
            }

            // Employee
            if ((showWhat & EMPLOYEE) != 0) {
                if (viewAsField != null) {
                    baseForm.removeField(viewAsField);
                }
                employee = new DataListBox();
                employee.setNullLabel(wfmStrings.all());
                employee.setAllowFirstItem(true);
                employee.addValueChangeHandler(arg0 -> {
                    reset = false;
                    clientList();
                    projectsList();
                    departmentList();
                });
                statsuAsField = baseForm.addField(wfmStrings.employee(), employee);
            }

            // Contract Client;
            if ((showWhat & CLIENT_CONTRACT) != 0) {
                if (viewAsField != null) {
                    baseForm.removeField(viewAsField);
                }
                contractClient = new DataListBox();
                contractClient.setNullLabel(wfmStrings.all());
                contractClient.setAllowFirstItem(true);
                contractClient.addValueChangeHandler(changeEvent -> {
                    reset = false;
                    contractList();
                });
                baseForm.addField(wfmStrings.customer(), contractClient);
            }

            //By Category
            if ((showWhat & BY_CATEGORY) != 0) {
                if (viewAsField != null) {
                    baseForm.removeField(viewAsField);
                }
                byCategory = new DataListBox();
                byCategory.setNullLabel(wfmStrings.all());
                byCategory.setAllowFirstItem(true);
                byCategory.addValueChangeHandler(event -> {
                    reset = false;
                    if (emaiDefault != null)
                        emaiDefault.setEnabled(false);
                });
                baseForm.addField(wCategory ? wfmStrings.category() : wfmStrings.byCategory(), byCategory);
            }

            // Reported by
            if ((showWhat & REPORTED_BY) != 0) {
                reportedBy = new DataListBox();
                reportedBy.setNullLabel(wfmStrings.all());
                reportedBy.setAllowFirstItem(true);
                reportedBy.addValueChangeHandler(arg0 -> reset = false);
                baseForm.addField(wCategory ? wfmStrings.author() : wfmStrings.reportedBy(), reportedBy);
            }
            // Resolver
            if ((showWhat & RESOLVER) != 0) {
                resolver = new DataListBox();
                resolver.setNullLabel(wfmStrings.all());
                resolver.setAllowFirstItem(true);
                resolver.addValueChangeHandler(arg0 -> reset = false);
                baseForm.addField(wfmStrings.resolver(), resolver);
            }
            //Issue Status
            if ((showWhat & ISSUE_STATUS) != 0) {
                issueStatus = new DataListBox();
                issueStatus.addValueChangeHandler(sender -> reset = false);
                baseForm.addField(wfmStrings.status(), issueStatus);
            }

            // Issue Priority
            if ((showWhat & ISSUE_PRIORITY) != 0) {
                issuePriority = new DataListBox();
                issuePriority.addValueChangeHandler(sender -> reset = false);
                baseForm.addField(wfmStrings.priority(), issuePriority);
            }

            //   invoice status

            // Backend users
            if ((showWhat & BACKEND_USERS) != 0) {
                if (viewAsField != null) {
                    baseForm.removeField(viewAsField);
                }
                backendUsers = new DataListBox();
                backendUsers.setNullLabel(wfmStrings.all());
                backendUsers.setAllowFirstItem(true);
                backendUsers.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.users(), backendUsers);
            }

            // Employee status
            if ((showWhat & TYPE_LISTS) != 0) {
                if (viewAsField != null) {
                    baseForm.removeField(viewAsField);
                }
                typeList = new DataListBox();
                typeList.setNullLabel(wfmStrings.all());
                typeList.setAllowFirstItem(true);
                typeList.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.certificateType(), typeList);
            }

            //TimeSheet Approval List Approvers
            if ((showWhat & TIMESHEET_APPROVAL_APPROVERS) != 0) {
                if (viewAsField != null) {
                    baseForm.removeField(viewAsField);
                }
                timeSheetApprovalListApprovers = new DataListBox();
                timeSheetApprovalListApprovers.setNullLabel(wfmStrings.all());
                timeSheetApprovalListApprovers.setAllowFirstItem(true);
                timeSheetApprovalListApprovers.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.approver(), timeSheetApprovalListApprovers);
            }
            //TimeSheet Approval List Status
            if ((showWhat & TIMESHEET_APPROVAL_STATUS) != 0) {
                timeSheetApprovalStatus = new DataListBox();
                timeSheetApprovalStatus.setNullLabel(wfmStrings.all());
                timeSheetApprovalStatus.setAllowFirstItem(true);
                timeSheetApprovalStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), timeSheetApprovalStatus);
            }

            //Project status
            if ((showWhat & PROJECT_STATUS) != 0) {
                projectStatus = new DataListBox();
                projectStatus.setAllowFirstItem(true);
                projectStatus.setNullLabel(wfmStrings.allDueProjects());
                projectStatus.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.status(), projectStatus);
            }
            // In/Out status
            if ((showWhat & IN_OUT_TIME_STATUS) != 0) {
                inOutStatus = new DataListBox();
                inOutStatus.setAllowFirstItem(true);
                inOutStatus.setNullLabel(wfmStrings.all());
                inOutStatus.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.status(), inOutStatus);
            }

            //Assessment initiated by
//            if ((showWhat & ASSESSMENT_INITIATE_BY) != 0) {
//                assessmentInitiatedBy = new DataListBox();
//                assessmentInitiatedBy.setAllowFirstItem(true);
//                assessmentInitiatedBy.setNullLabel(wfmStrings.all());
//                assessmentInitiatedBy.addValueChangeHandler(event -> reset = false);
//                baseForm.addField(wfmStrings.initiatedBy(), assessmentInitiatedBy);
//            }
            //Assessment Type
            if ((showWhat & ASSESSMENT_TYPE) != 0) {
                assessmentType = new DataListBox();
                assessmentType.setAllowFirstItem(true);
                assessmentType.setNullLabel(wfmStrings.all());
                assessmentType.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.type(), assessmentType);
            }
            //Assessment Status

            //TimeSlot name
            if ((showWhat & TIME_SLOT_NAME) != 0) {
                timeSlotItems = new DataListBox();
                timeSlotItems.setAllowFirstItem(true);
                timeSlotItems.setNullLabel(wfmStrings.all());
                timeSlotItems.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.timeslot(), timeSlotItems);
            }

            //Locations
            if ((showWhat & LOCATION_LIST) != 0) {
                if (viewAsField != null & (showWhat & TIME_SLOT_NAME) == 0) {
                    baseForm.removeField(viewAsField);
                }
                locations = new DataListBox();
                locations.setAllowFirstItem(true);
                locations.setNullLabel(wfmStrings.all());
                locations.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(Property.getPluralWithObjectCode(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), locations);
            }

            if ((showWhat & EMPLOYEE_POSITION) != 0) {
                position = new DataListBox();
                position.setAllowFirstItem(true);
                position.setNullLabel(wfmStrings.all());
                position.addValueChangeHandler(changeEvent -> reset = false);
                baseForm.addField(wfmStrings.position(), position);
            }

            if ((showWhat & SUPERVISOR_FILTER) != 0) {
                supervisorLookUp = new EmployeeLookUpWithCode();
                supervisorLookUp.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.supervisor(), supervisorLookUp);
            }

            if ((showWhat & EMPLOYEE_STATUS_FILTER) != 0) {
                employeeStatusFilter = new DataListBox();
                employeeStatusFilter.setAllowFirstItem(true);
                employeeStatusFilter.setNullLabel(wfmStrings.all());
                employeeStatusFilter.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.status(), employeeStatusFilter);
            }

            if ((showWhat & TERMINAL_FILTER) != 0) {
                terminalFilter = new DataListBox();
                terminalFilter.setAllowFirstItem(true);
                terminalFilter.setNullLabel(wfmStrings.all());
                terminalFilter.addValueChangeHandler(event -> reset = false);
                baseForm.addField("Terminal", terminalFilter);
            }

            if ((showWhat & ROLE_FILTER) != 0) {
                roleFilter = new DataListBox();
                roleFilter.setAllowFirstItem(true);
                roleFilter.setNullLabel(wfmStrings.all());
                roleFilter.addValueChangeHandler(event -> reset = false);
                baseForm.addField(wfmStrings.role(), roleFilter);
            }

            //Default
            if ((showWhat & E_DEFAULT) != 0) {
                emaiDefault = new DataListBox();
                emaiDefault.setAllowFirstItem(true);
                emaiDefault.addValueChangeHandler(event -> {
                    reset = false;
                    byCategory.setEnabled(false);
                });
                baseForm.addField(wfmStrings.isDefault(), emaiDefault);
            }

            // Date
            if ((showWhat & DATE) != 0) {
                fromDate = new DatePicker();
                fromDate.setDefaultValue();

                toDate = new DatePicker();
                toDate.setDefaultValue();
                if (!isDateOneLine) {
                    baseForm.addField(wfmStrings.startDate(), fromDate);
                    baseForm.addField(wfmStrings.dueDate(), toDate);
                } else {
                    fromDate.setWidth("85px");
                    toDate.setWidth("85px");
                    dateTable.setWidget(0, 1, fromDate);
                    dateTable.setWidget(0, 2, new HTML(wfmStrings.to()));
                    dateTable.setWidget(0, 3, toDate);
                    baseForm.addField(wfmStrings.hireDate(), dateTable);
                }
            }
            // Period Date
            if ((showWhat & PERIOD_DATE) != 0) {
                fromDate = new DatePicker();
                fromDate.setDefaultValue();

                toDate = new DatePicker();
                toDate.setDefaultValue();

                fromDate.setWidth("85px");
                toDate.setWidth("85px");
                dateTable.setWidget(0, 1, fromDate);
                dateTable.setWidget(0, 2, new HTML("-"));
                dateTable.setWidget(0, 3, toDate);
                baseForm.addField(wfmStrings.period(), dateTable);
            }

        }

        if (!fp.getListPanelTool().getListViewCustomFields().isEmpty() && viewName != null) {
            customFieldDataListBox = new ArrayList<>();
            for (CompanyCustomFieldItem customFieldItem : fp.getListPanelTool().getListViewCustomFields()) {
                if (UI_TYPE_DROPDOWN.equals(customFieldItem.getUiType())) {
                    if (customFieldItem.getPredefinedValues() != null) {
                        DataListBox customFiledListBox = new DataListBox();
//                        customFiledListBox.setWidth(baseWidth);
                        customFiledListBox.setAllowFirstItem(true);
                        customFiledListBox.setNullLabel(wfmStrings.all());
                        String[] values = customFieldItem.getPredefinedValues();
                        for (String value : values) {
                            customFiledListBox.addListItem(new SelectItem(customFieldItem.getObjectId(), value, customFieldItem.getColumnCode()));
                        }
                        customFieldDataListBox.add(customFiledListBox);
                        baseForm.addField(customFieldItem.getFieldName(), customFiledListBox);
                    }
                }
            }
        }
        resetButton.addClickHandler(be -> {
            reset = true;
            if (byCategory != null && emaiDefault != null) {
                byCategory.setEnabled(true);
                emaiDefault.setEnabled(true);
            }
            setNullLabelToBoxes();
            setAllData();
        });

        cancelButton.addClickHandler(be -> {
            setNullLabelToBoxes();
            close();
        });

        applyButton.addClickHandler(baseEvent -> {
            reset = false;
            setAllData();
            applyButtonHanlder.onClick(null);
        });
    }

    private void setAllData() {
        if (viewAs != null) {
            fp.setViewAsId(viewAs.getSelectedId());
        }
        if (client != null) {
            fp.setClientId(client.getSelectedId());
        }
        if (project != null) {
            fp.setProjectId(project.getSelectedId());
        }
        if (projectLookUp != null) {
            fp.setProjectId(projectLookUp.getSelectedItemID());
        }

        if (rotationApprover != null) {
            if (rotationApprover.getSelectedItem() != null) {
                fp.setApproverID(rotationApprover.getSelectedItem().getId());
            }
        }
        if (benefitRequesterLookUp != null) {
            GWT.log("benefitRequesterLookUp.getSelectedItem() " + benefitRequesterLookUp.getSelectedItem());
            if (benefitRequesterLookUp.getSelectedItem() != null) {
                GWT.log("benefitRequesterLookUp.getSelectedItem().getId() " + benefitRequesterLookUp.getSelectedItem().getId());
                fp.setClientId(benefitRequesterLookUp.getSelectedItem().getId());
            } else {
                fp.setClientId(null);
            }
        }
        if (benefitApprover != null) {
            if (benefitApprover.getSelectedItem() != null) {
                fp.setApproverID(!benefitApprover.getSelectedId().equals(0) ? benefitApprover.getSelectedItem().getId() : null);
            } else {
                fp.setApproverID(null);
            }
        }

        if (benefitStatus != null) {
            if (benefitStatus.getSelectedItem() != null) {
                fp.setSearchKey(!benefitStatus.getSelectedId().equals(0) ? benefitStatus.getSelectedItem().getName() : null);
            } else {
                fp.setSearchKey(null);
            }
        }
        if (benefitType != null) {
            if (benefitType.getSelectedItem() != null) {
                fp.setName(!benefitType.getSelectedId().equals(0) ?benefitType.getSelectedItem().getName() : null);
            } else {
                fp.setName(null);
            }
        }
        if (employeeLookUp != null) {
            fp.setEmployeeId(employeeLookUp.getSelectedItemID());
        } else if (employee != null) {
            fp.setEmployeeId(employee.getSelectedId());
        }
        if (country != null) {
            if (country.getSelectedItem() != null && country.getSelectedId() != null) {
                fp.setCountryCode(!country.getSelectedId().equals(0) ? country.getSelectedItem().getName() : null);
            }
        }
        if (companyUsers != null) {
            if (companyUsers.getSelectedId() != null) {
                fp.setBackendUsersId(!companyUsers.getSelectedId().equals(0) ? companyUsers.getSelectedId() : null);
            }
        }
        if (shiftEmployeeLookUp != null) {
            if (shiftEmployeeLookUp.getSelectedItem() != null) {
                fp.setEmployeeId(shiftEmployeeLookUp.getSelectedItem().getId());
            }
        }
        if (apprasialEmployeeLookUp != null) {
            if (apprasialEmployeeLookUp.getSelectedItem() != null ) {
                fp.setEmployeeId(apprasialEmployeeLookUp.getSelectedItem().getId());
            }
        }
        if (apprasialInitiator != null) {
            if (apprasialInitiator.getSelectedItem() != null ) {
                fp.setUserID(apprasialInitiator.getSelectedItem().getId());
            }
        }

        if (shiftDEpartmentLookUp != null) {
            if (shiftDEpartmentLookUp.getSelectedItem() != null) {
                fp.setDepartmentIds(String.valueOf(shiftDEpartmentLookUp.getSelectedItem().getId()));
            }
        }
        if (positionDepartment != null) {
            if (positionDepartment.getSelectedItem() != null) {
                fp.setDepartmentId(positionDepartment.getSelectedItemID());
            } else {
                fp.setDepartmentId(null);
            }
        }

        if (positionType != null) {
            if (positionType.getSelectedItem() != null) {
                fp.setType(positionType.getSelectedItemID());
            } else {
                fp.setType(null);
            }
        }

        if (positionLocation != null) {
            if (positionLocation.getSelectedItem() != null) {
                fp.setLocationId(positionLocation.getSelectedItemID());
            } else {
                fp.setLocationId(null);
            }
        }
        if (departmentLocation != null) {
            if (departmentLocation.getSelectedItem() != null) {
                fp.setLocationId(departmentLocation.getSelectedItemID());
            } else {
                fp.setLocationId(null);
            }
        }
        if (positionStatus != null) {
            if (positionStatus.getSelectedItem() != null) {
                fp.setStatusID(!positionStatus.getSelectedId().equals(0) ? positionStatus.getSelectedId() : null);
            } else {
                fp.setStatusID(null);
            }
        }

        if (shiftStatus != null) {
            if (shiftStatus.getSelectedItem() != null && shiftStatus.getSelectedId() != null) {
                fp.setStatusCode(!shiftStatus.getSelectedId().equals(0) ? shiftStatus.getSelectedItem().getDescription() : null);
            }
        }
        if (rotationStatus != null) {
            if (rotationStatus.getSelectedItem() != null && rotationStatus.getSelectedId() != null) {
                fp.setStatusCode(!rotationStatus.getSelectedId().equals(0) ? rotationStatus.getSelectedItem().getDescription() : null);
            }
        }
        if (rotationEmployeeLookUp != null) {
            if (rotationEmployeeLookUp.getSelectedItem() != null) {
                fp.setEmployeeId(rotationEmployeeLookUp.getSelectedItem().getId());
            }
        }
        if (shiftType != null) {
            if (shiftType.getSelectedItem() != null && shiftType.getSelectedId() != null) {
                fp.setType(shiftType.getSelectedId() != null && !shiftType.getSelectedId().equals(0) ? shiftType.getSelectedItem().getId() : null);
            }
        }
        if (departmentStatus != null) {
            if (departmentStatus.getSelectedItem() != null && departmentStatus.getSelectedId() != null) {
                fp.setStatusCode(!departmentStatus.getSelectedId().equals(0) ? departmentStatus.getSelectedItem().getDescription() : null);
            }
        }
        if (departmentLookUp != null) {
            if (departmentLookUp.getSelectedItem() != null && departmentLookUp.getSelectedItemID() != null) {
                fp.setParentID(!departmentLookUp.getSelectedItemID().equals(0) ? departmentLookUp.getSelectedItemID() : null);
            }
        }
        if (shiftPeriod != null) {
            if (shiftPeriod.getDate() != null) {
                fp.setShiftPeriod(shiftPeriod.getDate());
            }
        }
        if (companyStatus != null) {
            if (companyStatus.getSelectedItem() != null && companyStatus.getSelectedId() != null) {
                fp.setStatusCode(!companyStatus.getSelectedId().equals(0) ? companyStatus.getSelectedItem().getDescription() : null);
            }
        }
        if (fromRegistrationDate != null) {
            fp.setFromRegistrationDate(fromRegistrationDate.getDate());
        }
        if (toRegistrationDate != null) {
            fp.setToRegistrationDate(toRegistrationDate.getDate());
        }
        if (fromExpirationDate != null) {
            fp.setFromExpirationDate(fromExpirationDate.getDate());
        }
        if (toExpirationDate != null) {
            fp.setToExpirationDate(toExpirationDate.getDate());
        }
        if (subscriptionType != null) {
            if (subscriptionType.getSelectedItem() != null && subscriptionType.getSelectedId() != null)
                fp.setSubscriptionTypeName(!subscriptionType.getSelectedId().equals(0) ? subscriptionType.getSelectedItem().getName() : null);
        }
        if (department != null) {
            fp.setDepartmentId(department.getSelectedId());
        }
        if (employee != null) {
            fp.setEmployeeId(employee.getSelectedId());
        }
        if (reportedBy != null && !wCategory) {
            fp.setReportedByID(reportedBy.getSelectedId());
        }
        if (reportedBy != null && wCategory) {
            fp.setEmployeeId(reportedBy.getSelectedId());
        }
        if (resolver != null) {
            fp.setResolverID(resolver.getSelectedId());
        }
        if (projectStatus != null) {
            fp.setProjectStatusId(projectStatus.getSelectedId());
        }
        if (inOutStatus != null) {
            fp.setStatusID(inOutStatus.getSelectedId());
        }
        if (assessmentInitiatedBy != null) {
            fp.setUserID(assessmentInitiatedBy.getSelectedId());
        }
        if (assessmentType != null) {
            fp.setType(assessmentType.getSelectedId());
        }
        if (assessmentStatus != null) {
            if (isAgentID) {
                fp.setAgentID(assessmentStatus.getSelectedId());
            } else {
                fp.setStatusID(assessmentStatus.getSelectedId());
            }
        }
        if (assessmentValidityPeriod != null) {
            fp.setValidityPeriodId(assessmentValidityPeriod.getSelectedId());
        }
        if (timeSlotItems != null) {
            fp.setTimeSlotID(timeSlotItems.getSelectedId());
        }
        if (locations != null) {
            fp.setLocationId(locations.getSelectedId());
        }
        if (issueStatus != null) {
            fp.setIssueStatusId(issueStatus.getSelectedId());
        }
        if (issuePriority != null) {
            fp.setIssuePriorityId(issuePriority.getSelectedId());
        }

        if (employeePromotionsPenaltiesFilter != null) {
            fp.setSearchType(employeePromotionsPenaltiesFilter.getSelectedId());
        }

        if (byCategory != null && byCategory.getSelectedItem() != null && !wCategory) {
            fp.setGroupByName(byCategory.getSelectedItem().getDescription());
            fp.setSearchType(0);
        } else if (emaiDefault != null && emaiDefault.getSelectedItem() != null) {
            fp.setGroupByName(emaiDefault.getSelectedItem().getDescription());
            fp.setSearchType(1);
        } else if (emaiDefault != null && byCategory != null) {
            fp.setGroupByName(null);
            fp.setSearchType(0);
        }
        if (byCategory != null && wCategory) {
            fp.setCategoryID(byCategory.getSelectedId());
        }
        /*if (categoryLookUp != null) {
            fp.setCategoryID(categoryLookUp.getSelectedItemID());
        }*/
        if (fromDate != null) {
            fp.setStartDate(fromDate.getDate());
        }
        if (toDate != null) {
            fp.setEndDate(toDate.getDate());
        }
        if (timeSheetApprovalListApprovers != null) {
            fp.setUserID(timeSheetApprovalListApprovers.getSelectedId());
        }
        if (contractClient != null) {
            fp.setContractClientId(contractClient.getSelectedId());
            //if select all set default 0
            if (contractClient.getSelectedId() != null && contractClient.getSelectedId().equals(0)) {
                fp.setContractClientId(null);
            }
        }
        if (taskEmployees != null) {
            fp.setEmployeeId(taskEmployees.getSelectedId());
        }
        if (timeSheetApprovalStatus != null) {
            fp.setStatusID(timeSheetApprovalStatus.getSelectedId());
        }
        if (typeList != null) {
            fp.setType(typeList.getSelectedId());
        }
        if (position != null) {
            fp.setPositionID(position.getSelectedId());
        }
        if (supervisorLookUp != null) {
            fp.setSupervisorId(supervisorLookUp.getSelectedItem() != null ? supervisorLookUp.getSelectedItem().getId() : null);
        }
        if (employeeStatusFilter != null) {
            fp.setEmployeeStatusID(employeeStatusFilter.getSelectedId());
        }
        if (terminalFilter != null) {
            fp.setDeviceID(terminalFilter.getSelectedItem() != null ? terminalFilter.getSelectedItem().getDescription() : null);
        }
        if (roleFilter != null) {
            fp.setRoleID(roleFilter.getSelectedId());
        }
        if (recurrungInvoiceStatus != null) {
            fp.setInvoiceStatusId(recurrungInvoiceStatus.getSelectedId());
        }
        if (recurrenceStatus != null) {
            fp.setRecurrenceStatus(recurrenceStatus.getSelectedItem() != null ? recurrenceStatus.getSelectedItem().getDescription() : null);
        }
        if (status != null && status.getSelectedItem() != null) {
            fp.setStatusCode(status.getSelectedItem().getDescription());
            fp.setAccountTransactionStatus(status.getSelectedItem().getDescription());
        }
        if (type != null) {
            fp.setRelationID(type.getSelectedId());//for Batch Payment Payment Method
            fp.setViewType(type.getSelectedItem() != null ? type.getSelectedItem().getDescription() : null);//for Manual Transaction
        }
        if (bank != null) {
            fp.setBankID(bank.getSelectedItemID());
        }
        if (customerLookUp != null) {
            fp.setCrmAccountId(customerLookUp.getSelectedItemID());
        }
        if (supplierLookUp != null) {
            fp.setCrmAccountId(supplierLookUp.getSelectedItemID());
        }
        if (quoteLookUp != null) {
            fp.setQuoteId(quoteLookUp.getSelectedItemID());
        }
        if (fromAmount != null) {
            fp.setFromAmount(Utils.isNullOrEmpty(fromAmount.getText()) ? null : Utils.getNumberFormat().parse(fromAmount.getText()));
        }
        if (toAmount != null) {
            fp.setToAmount(Utils.isNullOrEmpty(toAmount.getText()) ? null : Utils.getNumberFormat().parse(toAmount.getText()));
        }
        if (customFieldDataListBox != null) {
            HashMap<String, String> customFieldsMap = new HashMap<>();
            for (DataListBox dataListBox : customFieldDataListBox) {
                if (dataListBox.getSelectedItem() != null) {
                    customFieldsMap.put(dataListBox.getSelectedItem().getDescription(), dataListBox.getSelectedItem().getName());
                }
            }
            fp.setCustomFields(customFieldsMap);
        }
        if (serialNumber != null) {
            fp.setSerialNumber(serialNumber.getSelectedItemText());
        }
        if (batchType != null) {
            fp.setBatchHistoryType(batchType.getSelectedItemText());
        }
        if (warehouse != null) {
            fp.setWarehouseId(warehouse.getSelectedId());
        }
        if (fromBatchDate != null) {
            fp.setFromExpiryDate(fromBatchDate.getDate());
        }
        if (toBatchDate != null) {
            fp.setToExpiryDate(toBatchDate.getDate());
        }

        if (columns != null && columns.getSelectedItem(true) != null && oper != null && numberBox != null && numberBox.getValue() != null && !Utils.isNullOrEmpty(numberBox.getValue())) {
            fp.setColOper(columns.getSelectedItem(true).getDescription() + "__" + oper.getSelectedItem(true).getName() + "__" + numberBox.getValue());
        }
        if (calculateDepreciation != null) {
            fp.setCalculateDepreciation(calculateDepreciation.getSelectedId());
        }
        if (bankAccountNumber != null && bankAccountNumber.getSelectedItem() != null) {
            fp.setBankAccountNumber(bankAccountNumber.getSelectedItem().getName());
        }
        if (bankAccountCode != null && bankAccountCode.getSelectedItem() != null) {
            fp.setBankAccountCode(bankAccountCode.getSelectedItem().getName());
        }
        if (bankAccountName != null && bankAccountName.getSelectedItem() != null) {
            fp.setBankAccountName(bankAccountName.getSelectedItem().getName());
        }
        if (bankAccountCurrency != null) {
            fp.setBankAccountCurrencyId(bankAccountCurrency.getSelectedId());
        }
    }

    private void setNullLabelToBoxes() {
        if (viewAs != null) {
            viewAs.setSelectedNullLabel();
        }
        if (client != null) {
            client.setSelectedNullLabel();
        }
        if (contractClient != null) {
            contractClient.setSelectedNullLabel();
        }
        if (project != null) {
            project.setSelectedNullLabel();
        }
        if (department != null) {
            department.setSelectedNullLabel();
        }
        if (employee != null) {
            employee.setSelectedNullLabel();
        }
        if (reportedBy != null) {
            reportedBy.setSelectedNullLabel();
        }
        if (resolver != null) {
            resolver.setSelectedNullLabel();
        }
        if (projectStatus != null) {
            projectStatus.setSelectedNullLabel();
        }
        if (inOutStatus != null) {
            inOutStatus.setSelectedNullLabel();
        }
        if (assessmentInitiatedBy != null) {
            assessmentInitiatedBy.setSelectedNullLabel();
        }
        if (assessmentType != null) {
            assessmentType.setSelectedNullLabel();
        }
        if (assessmentStatus != null) {
            assessmentStatus.setSelectedNullLabel();
        }
        if (assessmentValidityPeriod != null) {
            assessmentValidityPeriod.setSelectedNullLabel();
        }
        if (timeSlotItems != null) {
            timeSlotItems.setSelectedNullLabel();
        }
        if (locations != null) {
            locations.setSelectedNullLabel();
        }
        if (positionType != null) {
            positionType.clear();
        }
        if (issueStatus != null) {
            issueStatus.setSelectedNullLabel();
        }
        if (issuePriority != null) {
            issuePriority.setSelectedNullLabel();
        }
        if (shiftPeriod != null) {
            shiftPeriod.setDefaultValue();
        }

        if (employeePromotionsPenaltiesFilter != null) {
            employeePromotionsPenaltiesFilter.setSelectedNullLabel();
        }

        if (byCategory != null) {
            byCategory.setSelectedNullLabel();
        }

        if (emaiDefault != null) {
            emaiDefault.setSelectedNullLabel();
        }
        if (fromDate != null) {
            fromDate.setDefaultValue();
        }
        if (toDate != null) {
            toDate.setDefaultValue();
        }
        if (timeSheetApprovalListApprovers != null) {
            timeSheetApprovalListApprovers.setSelectedNullLabel();
        }
        if (taskEmployees != null) {
            taskEmployees.setSelectedNullLabel();
        }
        if (timeSheetApprovalStatus != null) {
            timeSheetApprovalStatus.setSelectedNullLabel();
        }
        if (typeList != null) {
            typeList.setSelectedNullLabel();
        }
        if (position != null) {
            position.setSelectedNullLabel();
        }
        if (supervisorLookUp != null) {
            supervisorLookUp.clear();
        }
        if (employeeStatusFilter != null) {
            employeeStatusFilter.setSelectedNullLabel();
        }
        if (terminalFilter != null) {
            terminalFilter.setSelectedNullLabel();
        }
        if (roleFilter != null) {
            roleFilter.setSelectedNullLabel();
        }
        if (recurrungInvoiceStatus != null) {
            recurrungInvoiceStatus.setSelectedNullLabel();
        }
        if (status != null) {
            status.setSelectedNullLabel();
        }
        if (type != null) {
            type.setSelectedNullLabel();
        }
        if (projectLookUp != null) {
            projectLookUp.clear();
        }
        if (customerLookUp != null) {
            customerLookUp.clear();
        }
        if (supplierLookUp != null) {
            supplierLookUp.clear();
        }
        if (quoteLookUp != null) {
            quoteLookUp.clear();
        }
        if (employeeLookUp != null) {
            employeeLookUp.clear();
        }
        if (shiftEmployeeLookUp != null) {
            shiftEmployeeLookUp.clear();
        }
        if (apprasialEmployeeLookUp != null) {
            apprasialEmployeeLookUp.clear();
        }
        if (apprasialInitiator != null) {
            apprasialInitiator.clear();
        }
        if (shiftDEpartmentLookUp != null) {
            shiftDEpartmentLookUp.clear();
        }
        if (rotationEmployeeLookUp != null) {
            rotationEmployeeLookUp.clear();
        }
        if (rotationApprover != null) {
            rotationApprover.clear();
        }
        if (benefitRequesterLookUp != null) {
            benefitRequesterLookUp.clear();
        }
        if (benefitApprover != null) {
            benefitApprover.setSelectedNullLabel();
        }
        if (benefitType != null) {
            benefitType.setSelectedNullLabel();
        }
        if (benefitStatus != null) {
            benefitStatus.setSelectedNullLabel();
        }
        if (positionStatus != null) {
            positionStatus.clearSelected();
        }
        if (positionLocation != null) {
            positionLocation.clear();
        }
        if (positionDepartment != null) {
            positionDepartment.clear();
        }
        if (shiftStatus != null) {
            shiftStatus.setSelectedNullLabel();
        }
        if (rotationStatus != null) {
            rotationStatus.setSelectedNullLabel();
        }
        if (fromAmount != null) {
            fromAmount.setText("");
        }
        if (toAmount != null) {
            toAmount.setText("");
        }
        if (columns != null) {
            columns.setSelected(new SelectItem(0));
        }
        if (numberBox != null) {
            numberBox.setValue("0");
        }
        if (fromRegistrationDate != null) {
            fromRegistrationDate.setDefaultValue();
        }
        if (toRegistrationDate != null) {
            toRegistrationDate.setDefaultValue();
        }
        if (fromExpirationDate != null) {
            fromExpirationDate.setDefaultValue();
        }
        if (toExpirationDate != null) {
            toExpirationDate.setDefaultValue();
        }
        if (fromBatchDate != null) {
            fromBatchDate.setDefaultValue();
        }
        if (toBatchDate != null) {
            toBatchDate.setDefaultValue();
        }
        if (serialNumber != null) {
            serialNumber.setNullLabel(wfmStrings.all());
        }
        if (batchType != null) {
            batchType.setNullLabel(wfmStrings.all());
        }
        if (warehouse != null) {
            warehouse.setNullLabel(wfmStrings.all());
        }
        if (calculateDepreciation != null) {
            calculateDepreciation.setNullLabel(wfmStrings.pleaseSelect());
        }
        if (bankAccountName != null) {
            bankAccountName.clear();
        }
        if (bankAccountCode != null) {
            bankAccountCode.clear();
        }
        if (bankAccountNumber != null) {
            bankAccountNumber.clear();
        }
        if (bankAccountCurrency != null) {
            bankAccountCurrency.setSelectedNullLabel();
        }
        if (positionDepartment != null) {
            positionDepartment.removeFromParent();
            departmentContainer.remove(positionDepartment);
            positionDepartment = new DepartmentLookUp();
            positionDepartment.getSuggestBox().addSelectionHandler(e -> updateLocation(positionDepartment.getSelectedItemID()));
            departmentContainer.add(positionDepartment);
        }
        if (customFieldDataListBox != null) {
            for (DataListBox dataListBox : customFieldDataListBox) {
                dataListBox.setSelectedNullLabel();

            }
        }
    }

    private void roleList() {
        reportService.getRoleList(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] items) {
                if (items != null) {
                    viewAs.setItems(items);
                }
            }
        });
    }

    private void employeeList() {
        if (employee != null) {
            checkSelected();
            reportService.getEmployeesList(clientId, projectId, departmentId, viewAsId, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    employee.setItems(items);
                    if (reset) {
                        employee.setSelected(0);
                    }
                }
            });
        }
    }

    private void taskEmployeeList() {
        if (taskEmployees != null) {
            checkSelected();
            reportService.getTaskEmployeesList(fp.getTaskID(), new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    taskEmployees.setItems(items);
                    if (reset) {
                        taskEmployees.setSelected(0);
                    }
                }
            });
        }
    }

    private void reportedByList() {
        if (reportedBy != null) {
            if (!wCategory) {
                reportService.getEmployeesList(clientId, projectId, departmentId, viewAsId, new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] items) {
                        reportedBy.setItems(items);
                        if (reset) {
                            reportedBy.setSelected(0);
                        }
                    }
                });
            } else {
                reportService.getUsersByNews(new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] result) {
                        reportedBy.setItems(result);
                        if (reset) {
                            reportedBy.setSelected(0);
                        }
                    }
                });
            }
        }
    }

    private void resolverList() {
        if (resolver != null) {
            reportService.getEmployeesList(clientId, projectId, departmentId, viewAsId, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    resolver.setItems(items);
                    if (reset) {
                        resolver.setSelected(0);
                    }
                }
            });
        }
    }

    public void setEmployeeList(SelectItem[] items) {
        employee.clear();
        employee.setItems(items);
        if (reset) {
            employee.setSelected(0);
        }
    }

    private void departmentList() {
        if (department != null) {
            checkSelected();
            if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
                employeeId = 0;
            }
            reportService.getDepartmentList(clientId, projectId, employeeId, viewAsId, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    department.setItems(items);
                    if (reset) {
                        department.setSelected(0);
                    }
                }
            });
        }
    }

    private void projectsList() {
        if (project != null) {
            checkSelected();
            reportService.getProjectListForReport(clientId, departmentId, employeeId, viewAsId, null, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    project.setItems(items);
                    if (reset) {
                        project.setSelected(0);
                    }
                }
            });
        }
    }

    private void clientList() {
        if (client != null) {
            checkSelected();
            reportService.getClientsList(projectId, departmentId, employeeId, viewAsId, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    client.setItems(items);
                    if (reset) {
                        client.setSelected(0);
                    }
                }
            });
        }
    }

    // Contract Client List
    private void contractList() {
        if (contractClient != null) {
            reportService.getContractList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    if (result != null) {
                        contractClient.setItems(result);
                    }
                }
            });
        }
    }

    private void locationsList() {
        if (locations != null) {
            checkSelected();
            reportService.getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    locations.setItems(result);
                }
            });
        }
    }

    private void projectStatusList() {
        if (projectStatus != null) {
            checkSelected();
            reportService.getStatuses(null, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    projectStatus.clear();
                    projectStatus.setItems(result);
                }
            });
        }
    }

    private void reccuringInvoiceStatusList() {
        if (recurrungInvoiceStatus != null) {
            reportService.getReccuringInvoiceStatuses(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    recurrungInvoiceStatus.clear();
                    recurrungInvoiceStatus.setItems(result);
                }
            });
        }

        if (recurrenceStatus != null) {
            recurrenceStatus.clear();
            recurrenceStatus.setItems(new SelectItem[]{
                    new SelectItem(1, "Active", SchedulerConstant.SUCCESS),
                    new SelectItem(2, "Ended", SchedulerConstant.FAIL),
            });
        }
    }

    private void inOutStatusListBox() {
        if (inOutStatus != null) {
            checkSelected();
            SelectItem[] inOutStatusItems = new SelectItem[]{new SelectItem(11, wfmStrings.checkedIn()),
                    new SelectItem(22, wfmStrings.checkedOut()), new SelectItem(33, hrmsStrings.lunch())};
            inOutStatus.clear();
            inOutStatus.setItems(inOutStatusItems);
        }
    }

    private void assessmentInitiatedBy() {
        if (assessmentInitiatedBy != null) {
            checkSelected();
            reportService.getEmployeesList(clientId, projectId, departmentId, viewAsId, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    assessmentInitiatedBy.setItems(items);
                    if (reset) {
                        assessmentInitiatedBy.setSelected(0);
                    }
                }
            });
        }
    }

    private void assessmentType() {
        if (assessmentType != null) {
            checkSelected();
            reportService.getAssessmentTypeList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    assessmentType.setItems(items);
                    if (reset) {
                        assessmentType.setSelected(0);
                    }
                }
            });
        }
    }

    private void assessmentStatus() {
        if (assessmentStatus != null) {
            checkSelected();
            if (isAgentID) {
                reportService.getAgentIDs(new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] items) {
                        assessmentStatus.setItems(items);
                        if (reset) {
                            assessmentStatus.setSelected(0);
                        }
                    }
                });
            } else {
                reportService.getAssessmentStatusList(new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] items) {
                        assessmentStatus.setItems(items);
                        if (reset) {
                            assessmentStatus.setSelected(0);
                        }
                    }
                });
            }
        }
    }

    private void assessmentValidityPeriod() {
        if (assessmentValidityPeriod != null) {
            checkSelected();
            reportService.getValidityPeriodList(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    assessmentValidityPeriod.setItems(result);
                    if (reset) {
                        assessmentValidityPeriod.setSelectedNullLabel();
                    }
                }
            });
        }
    }

    private void timeSlotItemsListBox() {
        if (timeSlotItems != null) {
            checkSelected();
            reportService.getCompanyTimeSlots(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    timeSlotItems.clear();
                    timeSlotItems.setItems(result);
                }
            });
        }
    }

    private void loadEmployeeStatusFilter() {
        if (employeeStatusFilter != null) {
            reportService.getEmployeeStatusList(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    employeeStatusFilter.clear();
                    employeeStatusFilter.setItems(result);
                }
            });
        }
    }

    private void loadTerminalFilter() {
        if (terminalFilter != null) {
            reportService.getAttendanceTerminalSelectItems(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    terminalFilter.clear();
                    terminalFilter.setItems(result);
                }
            });
        }
    }

    private void loadRoleFilter() {
        if (roleFilter != null) {
            reportService.getRoleList(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    roleFilter.clear();
                    roleFilter.setItems(result);
                }
            });
        }
    }

    private void getTimeSheetApprovalListApprovers() {
        if (timeSheetApprovalListApprovers != null) {
            checkSelected();
            timeSheetApprovalListApprovers.clear();
            reportService.getTimesheetApprovers(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    timeSheetApprovalListApprovers.setItems(items);
                    if (reset) {
                        timeSheetApprovalListApprovers.setSelectedNullLabel();
                    }
                }
            });
        }
    }

    private void getbenefitApproverList() {
        if (benefitApprover != null) {
            checkSelected();
            benefitApprover.clear();
            commonService.getBenefitApprovers(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    benefitApprover.setItems(items);
                    if (reset) {
                        benefitApprover.setSelectedNullLabel();
                    }
                }
            });
        }
    }
    private void getTimeSheetApprovalListStatus() {
        if (timeSheetApprovalStatus != null) {
            checkSelected();
            timeSheetApprovalStatus.clear();
            reportService.getTimesheetApprovalStatusList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    timeSheetApprovalStatus.setItems(items);
                    if (reset) {
                        timeSheetApprovalStatus.setSelectedNullLabel();
                    }
                }
            });
        }
    }

    private void getTypeList() {
        if (typeList != null) {
            checkSelected();
            typeList.clear();
            reportService.getCertificateTypes(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    typeList.setItems(items);
                    if (reset) {
                        typeList.setSelected(0);
                    }
                }
            });
        }
    }

    private void getPositionList() {
        if (position != null) {
            checkSelected();
            position.clear();
            reportService.getEmplyeePositionList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    position.setItems(items);
                    if (reset) {
                        position.setSelected(0);
                    }
                }
            });
        }
    }

    private void backendUsersList() {
        if (backendUsers != null) {
            checkSelected();
            SelectItem[] items = new SelectItem[3];
            items[0] = new SelectItem(0, RETURNING_USERS);
            items[1] = new SelectItem(1, ACTIVE_USERS);
            items[2] = new SelectItem(2, PENDING_USERS);
            backendUsers.setItems(items);
        }

    }

    private void getBenefitType() {
        commonService.getBenefitTypeList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] items) {
                    benefitType.setItems(items);
                    if (reset) {
                        benefitType.setSelectedNullLabel();
                    }
                }
            });
    }

    private void getCountryList() {
        if (country != null) {
            commonService.getCountries(true, new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    country.setItems(result);
                }
            });
        }
    }

    private void getShiftStatusList() {
        if (shiftStatus != null) {
            SelectItem[] items = new SelectItem[4];
            items[0] = new SelectItem(1, wfmStrings.approved(), "SHIFT_APPROVED");
            items[1] = new SelectItem(2, wfmStrings.rejected(), "SHIFT_REJECTED");
            items[2] = new SelectItem(3, wfmStrings.waitingForApproval(), "SHIFT_SUBMITTED");
            items[3] = new SelectItem(4, wfmStrings.draft(), "SHIFT_DRAFT");
            shiftStatus.setItems(items);
        }
    }

    private void getPositionStatusList() {
        if (positionStatus != null) {
            commonService.convertReference2SelectItem(Constants.POS_STATUS, false, null, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    positionStatus.setItems(result);
                }
            });
        }
    }

    private void getRotationStatusList() {
        if (rotationStatus != null) {
            SelectItem[] items = new SelectItem[4];
            items[0] = new SelectItem(1, wfmStrings.approved(), "ROTATION_APPROVED");
            items[1] = new SelectItem(2, wfmStrings.rejected(), "ROTATION_REJECTED");
            items[2] = new SelectItem(3, wfmStrings.waitingForApproval(), "ROTATION_SUBMITTED");
            items[3] = new SelectItem(4, wfmStrings.draft(), "ROTATION_DRAFT");
            rotationStatus.setItems(items);

        }
    }

    private void getShiftTypeList() {
        if (shiftType != null) {
            SelectItem[] items = new SelectItem[4];
            items[0] = new SelectItem(LookUpConstants.BRIGADA_ID, wfmStrings.team(), "");
            items[1] = new SelectItem(LookUpConstants.EMPLOYEE_ID, wfmStrings.duty(), "");
            items[2] = new SelectItem(LookUpConstants.OVERTIME, wfmStrings.overtime(), "");
            shiftType.setItems(items);
        }
    }

    private void getDepartmentStatusList() {
        if (departmentStatus != null) {
            SelectItem[] items = new SelectItem[4];
            items[0] = new SelectItem(1, wfmStrings.active(), "ACTIVE");
            items[1] = new SelectItem(2, wfmStrings.inactive(), "INACTIVE");
            departmentStatus.setItems(items);
        }
    }

    private void getBenefitStatusList() {
        if (benefitStatus != null) {
            SelectItem[] items = new SelectItem[4];
            items[0] = new SelectItem(1, wfmStrings.approved(), "BR_APPROVED");
            items[1] = new SelectItem(2, wfmStrings.rejected(), "BR_REJECTED");
            items[2] = new SelectItem(3, wfmStrings.waitingForApproval(), "RBENEFIT_SUBMITTED");
            items[3] = new SelectItem(4, wfmStrings.draft(), "BENEFIT_DRAFT");
            benefitStatus.setItems(items);
        }
    }

    private void getCompanyStatusList() {
        if (companyStatus != null) {
            SelectItem[] items = new SelectItem[2];
            items[0] = new SelectItem(1, wfmStrings.active(), "ACTIVE");
            items[1] = new SelectItem(2, wfmStrings.inactive(), "INACTIVE");
            companyStatus.setItems(items);
        }
    }

    private void getSubscriptionTypeList() {
        if (subscriptionType != null) {
            SelectItem[] items = new SelectItem[2];
            items[0] = new SelectItem(1, wfmStrings.yes());
            items[1] = new SelectItem(2, wfmStrings.no());
            subscriptionType.setItems(items);
        }
    }

    private void getCompanyUsersList() {
        if (companyUsers != null) {
            checkSelected();
            SelectItem[] items = new SelectItem[3];
            items[0] = new SelectItem(1, RETURNING_USERS);
            items[1] = new SelectItem(2, ACTIVE_USERS);
            items[2] = new SelectItem(3, PENDING_USERS);
            companyUsers.setItems(items);
        }

    }

    private void getBatchSerialNumber() {
        if (serialNumber != null) {

            commonService.getSerialNumbers(fp, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    serialNumber.setItems(result);
                    if (reset) {
                        serialNumber.setSelected(0);
                    }
                }
            });
        }
    }

    private void getBatchType() {
        if (batchType != null) {
            commonService.getBatchType(fp, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    batchType.setItems(result);
                    if (reset) {
                        batchType.setSelected(0);
                    }
                }
            });
        }
    }

    private void getBatchWarehouse() {
        if (warehouse != null) {
            commonService.getBatchWarehouse(fp, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    warehouse.setItems(result);
                    if (reset) {
                        batchType.setSelected(0);
                    }
                }
            });
        }
    }


    private void getCategories() {
        if (byCategory != null) {
            checkSelected();
            byCategory.clear();
            if (!wCategory) {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setSearchType(0);
                fp.setParams("EmailTemplateListView");
                reportService.getEmailTemplateCategoriesByList(fp, new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] result) {
                        byCategory.setItems(result);
                    }
                });
            } else {
                if (ViewName.FixedAssetRegister.equals(viewName)) {

                } else {
                    reportService.getNewsCategories(new AbstractAsyncCallback<SelectItem[]>() {
                        public void success(SelectItem[] result) {
                            byCategory.setItems(result);
                            if (reset) {
                                byCategory.setSelected(0);
                            }
                        }
                    });
                }
            }
            if (reset) {
                byCategory.setSelected(0);
            }
        }
    }

    private void employeePromotionsPenaltiesTypeList() {
        if (employeePromotionsPenaltiesFilter != null) {
            employeePromotionsPenaltiesFilter.clear();
            employeePromotionsPenaltiesFilter.setAllowFirstItem(true);
            employeePromotionsPenaltiesFilter.setNullLabel(wfmStrings.all());
            if ((showWhat & EMPLOYEE_PROMOTIONS_PENALTIES_LIST) != 0) {
                SelectItem[] types = new SelectItem[]{new SelectItem(1, wfmStrings.promotions()), new SelectItem(2, wfmStrings.punishments())};
                employeePromotionsPenaltiesFilter.setItems(types);
            }
        }
    }

    private void locationSelectionHandler(LocationLookUpWithCode locationLookUpWithCode) {
        locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
            positionDepartment.removeFromParent();
            departmentContainer.remove(positionDepartment);
            positionDepartment = new DepartmentLookUp();
            positionDepartment.getFilterParametrs().setLocationId(positionLocation.getSelectedItemID());
            departmentContainer.add(positionDepartment);
        });

    }

    private void updateLocation(Integer departmentId) {
        AllInOneService.App.get().getLocationByDepartmentId(departmentId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem result) {
                positionLocation.removeFromParent();
                locationContainer.remove(positionLocation);
                positionLocation = new LocationLookUpWithCode();
                locationSelectionHandler(positionLocation);
                positionLocation.setSelected(result);
                locationContainer.add(positionLocation);
            }
        });
    }
// private void getDepartments(Integer departmentId) {
//        AllInOneService.App.get().getTeamsList( new AsyncCallback<SelectItem[]>() {
//            @Override
//            public void onFailure(Throwable caught) {
//
//            }
//
//            @Override
//            public void onSuccess(SelectItem[] result) {
//                departmentLookUp.setSelected(result);
//                locationContainer.add(positionLocation);
//            }
//        });
//    }


    private void issuePriorityList() {
        if (issuePriority != null) {
            issuePriority.clear();
            issuePriority.setAllowFirstItem(true);
            issuePriority.setNullLabel(wfmStrings.all());
            if ((showWhat & ISSUE_PRIORITY) != 0) {
                reportService.getIssuePriorities(new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] result) {
                        issuePriority.setItems(result);
                    }
                });
            }
        }
    }

    private void issueStatusList() {
        if (issueStatus != null) {
//            checkSelected();
            issueStatus.clear();
            issueStatus.setAllowFirstItem(true);
            issueStatus.setNullLabel(wfmStrings.all());
            if ((showWhat & ISSUE_STATUS) != 0) {
                reportService.getIssueStatuses(new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(SelectItem[] result) {
                        issueStatus.setItems(result);
                    }
                });
            }
        }
    }

    private void getBankAccountCurrencyList() {
        if (bankAccountCurrency != null) {
            commonService.getBankAccountCurrencyList(fp, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    bankAccountCurrency.setItems(result);
                    if (reset) {
                        bankAccountCurrency.setSelectedNullLabel();
                    }
                }
            });
        }
    }

    private void checkSelected() {
        //client
        if (client != null && client.getSelectedItem() != null) {
            clientId = client.getSelectedItem().getId();
        } else {
            clientId = null;
        }

        // contract
        if (contractClient != null && contractClient.getSelectedItem() != null) {
            clientId = contractClient.getSelectedItem().getId();
        } else {
            clientId = null;
        }

        //department
        if (department != null && department.getSelectedItem() != null) {
            departmentId = department.getSelectedItem().getId();
        } else {
            departmentId = null;
        }

        //employee
        if (employee != null && employee.getSelectedItem() != null) {
            employeeId = employee.getSelectedItem().getId();
        } else {
            employeeId = null;
        }

        //taskEmployees
        if (taskEmployees != null && taskEmployees.getSelectedItem() != null) {
            employeeId = taskEmployees.getSelectedItem().getId();
        } else {
            employeeId = null;
        }

        //views
        if (viewAs.getSelectedItem() != null) {
            viewAsId = viewAs.getSelectedItem().getId();
        }
    }

    public void initListBoxes() {
        if (hasNoItems(viewAs)) {
            roleList();
        }

        if (hasNoItems(client)) {
            clientList();
        }
        if (hasNoItems(byCategory)) {
            getCategories();
        }

        if (hasNoItems(emaiDefault)) {
            getEmailDefault();
        }

        if (hasNoItems(project)) {
            projectsList();
        }
        if (hasNoItems(department)) {
            departmentList();
        }
        if (hasNoItems(employee)) {
            employeeList();
        }
        if (hasNoItems(reportedBy)) {
            reportedByList();
        }
        if (hasNoItems(resolver)) {
            resolverList();
        }
        if (hasNoItems(backendUsers)) {
            backendUsersList();
        }
        if (hasNoItems(typeList)) {
            getTypeList();
        }
        if (hasNoItems(position)) {
            getPositionList();
        }
        if (hasNoItems(timeSheetApprovalListApprovers)) {
            getTimeSheetApprovalListApprovers();
        }
        if (hasNoItems(benefitApprover)) {
            getbenefitApproverList();
        }
        if (hasNoItems(benefitType)) {
            getBenefitType();
        }
        if (hasNoItems(taskEmployees)) {
            taskEmployeeList();
        }
        if (hasNoItems(timeSheetApprovalStatus)) {
            getTimeSheetApprovalListStatus();
        }

        if (hasNoItems(projectStatus)) {
            projectStatusList();
        }
        if (hasNoItems(inOutStatus)) {
            inOutStatusListBox();
        }
        if (hasNoItems(assessmentInitiatedBy)) {
            assessmentInitiatedBy();
        }
        if (hasNoItems(assessmentType)) {
            assessmentType();
        }
        if (hasNoItems(assessmentStatus)) {
            assessmentStatus();
        }

        if (hasNoItems(assessmentValidityPeriod)) {
            assessmentValidityPeriod();
        }
        if (hasNoItems(timeSlotItems)) {
            timeSlotItemsListBox();
        }

        if (hasNoItems(locations)) {
            locationsList();
        }
        if (hasNoItems(employeeStatusFilter)) {
            loadEmployeeStatusFilter();
        }
        if (hasNoItems(terminalFilter)) {
            loadTerminalFilter();
        }
        if (hasNoItems(roleFilter)) {
            loadRoleFilter();
        }
        if (hasNoItems(issueStatus)) {
            issueStatusList();
        }
        if (hasNoItems(issuePriority)) {
            issuePriorityList();
        }

        if (hasNoItems(employeePromotionsPenaltiesFilter)) {
            employeePromotionsPenaltiesTypeList();
        }
        if (hasNoItems(recurrungInvoiceStatus)) {
            reccuringInvoiceStatusList();
        }
        if (hasNoItems(contractClient)) {
            contractList();
        }
        if (hasNoItems(country)) {
            getCountryList();
        }
        if (hasNoItems(companyStatus)) {
            getCompanyStatusList();
        }
        if (hasNoItems(shiftStatus)) {
            getShiftStatusList();
        }
        if (hasNoItems(positionStatus)) {
            getPositionStatusList();
        }
        if (hasNoItems(rotationStatus)) {
            getRotationStatusList();
        }
        if (hasNoItems(shiftType)) {
            getShiftTypeList();
        }
        if (hasNoItems(departmentStatus)) {
            getDepartmentStatusList();
        }
        if (hasNoItems(benefitStatus)) {
            getBenefitStatusList();
        }
        if (hasNoItems(subscriptionType)) {
            getSubscriptionTypeList();
        }
        if (hasNoItems(companyUsers)) {
            getCompanyUsersList();
        }
        if (hasNoItems(serialNumber)) {
            getBatchSerialNumber();
        }
        if (hasNoItems(batchType)) {
            getBatchType();
        }
        if (hasNoItems(warehouse)) {
            getBatchWarehouse();
        }
        if (hasNoItems(bankAccountCurrency)) {
            getBankAccountCurrencyList();
        }


        //Custom Facet Filter Fields Status
        if (hasNoItems(status) && status != null) {
            status.clear();
            status.setAllowFirstItem(true);
            status.setNullLabel(wfmStrings.all());
            SelectItem[] items;
            switch (viewName) {
                case ShiftList:
                    items = new SelectItem[4];
                    items[0] = new SelectItem(0, wfmStrings.approved(), APPROVED);
                    items[1] = new SelectItem(1, wfmStrings.rejected(), REJECTED);
                    items[2] = new SelectItem(2, wfmStrings.submitted(), SUBMITTED);
                    items[3] = new SelectItem(3, wfmStrings.draft(), DRAFT);
                    status.setItems(items);
                    break;
                case AccountTransactions:
                    items = new SelectItem[3];
                    items[0] = new SelectItem(0, accountingStrings.reconciled(), RECONCILED);
                    items[1] = new SelectItem(1, wfmStrings.markAsReconciled(), MARKED_AS_RECONCILED);
                    items[2] = new SelectItem(2, wfmStrings.unReconciled(), UNRECONCILED);
                    status.setItems(items);
                    break;
                case Prepayment:
                case Supplier:
                    items = new SelectItem[3];
                    items[0] = new SelectItem(0, wfmStrings.open(), "PRE_PAYMENT_OPEN_STATUS");
                    items[1] = new SelectItem(1, wfmStrings.partialApplied(), "PRE_PAYMENT_PARTIAL_APPLIED_STATUS");
                    items[2] = new SelectItem(2, wfmStrings.applied(), "PRE_PAYMENT_APPLIED_STATUS");
                    status.setItems(items);
                    break;
                case CheckList:
                    items = new SelectItem[2];
                    items[0] = new SelectItem(0, wfmStrings.postDated(), POST_DATED);
                    items[1] = new SelectItem(1, wfmStrings.posted(), POSTED);
                    status.setItems(items);
                    break;
                case ManualTransaction:
                    items = new SelectItem[2];
                    items[0] = new SelectItem(0, wfmStrings.post(), POST);
                    items[1] = new SelectItem(1, wfmStrings.draft(), DRAFT);
                    items[2] = new SelectItem(2, wfmStrings.submitted(), SUBMITTED);
                    status.setItems(items);
                    break;
                case RequestForQuote:
                    items = new SelectItem[4];
                    items[0] = new SelectItem(0, wfmStrings.open(), OPEN);
                    items[1] = new SelectItem(1, wfmStrings.draft(), DRAFT);
                    items[2] = new SelectItem(2, wfmStrings.converted(), CONVERTED);
                    items[3] = new SelectItem(3, wfmStrings.partiallyConverted(), PARTIAL_CONVERTED);
                    status.setItems(items);
                    break;
            }
        }
        if (type != null && hasNoItems(type)) {
            type.clear();
            type.setAllowFirstItem(true);
            type.setNullLabel(wfmStrings.all());
            SelectItem[] items;
            switch (viewName) {
                case BatchInvoicePaymentView:
                case BatchPayBillView:
                    AllInOneService.App.get().getPaymentMethodList(new AsyncCallback<SelectItem[]>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(SelectItem[] result) {
                            type.setItems(result);
                        }
                    });
                    break;
                case ManualTransaction:
                    items = new SelectItem[2];
                    items[0] = new SelectItem(0, wfmStrings.single(), SINGLE);
                    items[1] = new SelectItem(1, wfmStrings.recurring(), RECURRING);
                    type.setItems(items);
                    break;
            }
        }

    }

    private void getEmailDefault() {
        if (emaiDefault != null) {
            SelectItem yesItem = new SelectItem(0, wfmStrings.yes(), "true");
            SelectItem noItem = new SelectItem(1, wfmStrings.no(), "false");
            emaiDefault.setItems(new SelectItem[]{yesItem, noItem});
        }
    }

    private boolean hasNoItems(DataListBox box) {
        boolean has = box == null || box.getItems() == null || box.getItems().length <= 0;

        return has;
    }

    public void setAccountingListType(String accountingListType) {
        this.accountingListType = accountingListType;
    }

    public void addApplyButtonHandler(ClickHandler applyButtonHanlder) {
        this.applyButtonHanlder = applyButtonHanlder;
    }

    public void setFilterParameters(ListingFilterParameter fp) {
        this.fp = fp;
    }
}