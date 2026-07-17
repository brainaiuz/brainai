package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeRemovePopup;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.*;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.LOCALE_PAYROLL;

/**
 * Created by Djuraev on 11/9/15.
 */
public abstract class NewGlobalEmployeeSummaryView extends CustomForm2 implements Constants, HasLinksInterface {


    public static final int DEFAULT_ROWS = 5;

    private HTML firstname, lastname, middleName, othername, dob, gender, maritalStatus, /*languages,*/
            roles,
            passportNumber, passportIssueDate, passportIssBy, passportExpireDate, insuranceNumber, medicalInsuranceExDate,
            visaNumber, visaaIssueDate, visaExpireDate, employeeCode, wageRate, clientChageRate, qualification, employementMode,
            hireDate, resignationDate, supervisor, termOfContract, salaryGrade, basicSalary, basicTotalSalary, department, position, location, driverID,
            nationality, paymentMethod, status, essUser, noAccess, jobTitle, empHistory, bankName, bankAddress, accountNumber, accountName, swiftCode, salaryMode,
            sortCode, ibanCode, agentID, wpsNumber, openingBalanceDays, probationDays, employeeDegree, timeslot;
    private FlowPanel phoneNumInf, imsAddressInf, webSiteInf, emailInf, telegramInf;
    private EmployeeRemovePopup employeeDelete;

    protected boolean isEmployeeList;
    private MultiTableNewUI addressInf, parentAddressInf;
    protected ContactListItem contactListItem;
    private GeneralFileUpload uploadForm;

    private boolean pmEmployeeView;
    private boolean settingsEmployeeView;
    private boolean tcInstructor;
    public static String FROM_TC_INSTRUCTOR_VIEW = "`FROM_TC_INSTRUCTOR_VIEW`";

    private ProfileImage profilePicture;
    private static final NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");
    private ProfileItem profileItem;
    private final Integer employeeID;
    private final String employeeView = "employee_view ";
    public ListingFilterParameter filterParameter = new ListingFilterParameter();

    protected static String FROM_HRMS_EMPLOYEE_VIEW = "FROM_HRMS_EMPLOYEE_VIEW";
    protected static String FROM_PM_EMPLOYEE_VIEW = "FROM_PM_EMPLOYEE_VIEW";
    protected static String FROM_SETTINGS_EMPLOYEE_VIEW = "FROM_SETTINGS_EMPLOYEE_VIEW";
    private boolean hrmsEmployeeView;
    private final String from;

    public EditableTable paymentsTable;
    public EditableTable deductionsTable;
    public EditableTable taxTable;
    private EditableTable loansTable;
    private EditableTable employerContributionTable;

    private BigDecimal grandTotalPayment;
    private BigDecimal grandTotalDeduction;
    private BigDecimal basicSalaryAmount;

    private FormHasCustomField customFieldUtil;
    private FlexTable languagesWidget;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FooterInformer link;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);
    private HasLinks linkingUtil;

    private EditableTable experienceTable;

    private final Map<String, CompanyCustomFieldItem> experienceItemCFs = new LinkedHashMap<>();

    private final Map<String, ColumnConfigs> experienceColumnsMap = new LinkedHashMap<>();


    public NewGlobalEmployeeSummaryView(Integer employeeId, String name, String description, String... fromSection) {
        super((name != null && !"".equals(name) ? name : EMPLOYEE_PROFILE_VIEW));
        setDescription(property.getPlural(description));
        from = fromSection[0];
        this.employeeID = employeeId;
        if (fromSection.length > 0 && fromSection[0] != null) {
            if (FROM_PM_EMPLOYEE_VIEW.equals(fromSection[0])) {
                pmEmployeeView = true;
            } else if (FROM_SETTINGS_EMPLOYEE_VIEW.equals(fromSection[0])) {
                settingsEmployeeView = true;
            } else if (FROM_TC_INSTRUCTOR_VIEW.equals(fromSection[0])) {
                tcInstructor = true;
            } else if (FROM_HRMS_EMPLOYEE_VIEW.equals(fromSection[0])) {
                hrmsEmployeeView = true;
            }
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_PROFILE_UPDATE, NewGlobalEmployeeSummaryView.this, (sender, args) -> {
            getDataToFillFields();
        });
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public String getIconStyle() {
        return "hrms employee-profile-summary";
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);
        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(profileItem.getRelationItems(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        if (Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_REMOVE : PermissionConstants.HRMS_EMPLOYEE_REMOVE)) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

//        delete button

            String deletePermissionCode = Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_REMOVE : PermissionConstants.HRMS_EMPLOYEE_REMOVE;
            if ((Utils.hasPermission(deletePermissionCode) || Utils.isSettings()) && !Utils.getUserID().toString().equals(String.valueOf(employeeID))) {
                MaterialLink addDeleteButton = new MaterialLink(wfmStrings.delete());
                addDeleteButton.addClickHandler(click -> EmployeeService.App.get().checkEmployeeForApprovers(employeeID, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        employeeDelete.selectionListener();
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result > 0) {
                            switch (result) {
                                case 1:
                                    Info.show(wfmMessages.employeeParticipatedInApprovalProcess(profileItem.getFirstName(), profileItem.getFirstName()), Info.Type.WARNING);
                                    break;
                                case 2:
                                    Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                    break;
                                case 3:
                                    Info.show(wfmMessages.employeeParticipatedInApprovalProcess(profileItem.getFirstName(), profileItem.getFirstName()), Info.Type.WARNING);
                                    Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                    break;
                            }
                        } else {
                            employeeDelete.selectionListener();
                        }
                    }
                }));
                options.add(addDeleteButton);
            }
        }

        if (!settingsEmployeeView && !tcInstructor &&
                (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_TO_PDF) || Utils.getUserID().equals(employeeID))) {
            List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
            SplitButton printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
            printPdfSplitButton.setVisible(false);

            ContactService.App.get().getEployeePdfTemplateList(employeeID, new AbstractAsyncCallback<ProfileItem>() {
                public void success(ProfileItem result) {
                    LoadingPanel.loading(false);
                    Integer defaultTemplateId = null;
                    if (result != null && result.getTemplates() != null) {
                        result.getTemplates();
                        for (SelectItem pdfItem : result.getTemplates()) {
                            if (pdfItem.isDefaultSelected()) {
                                defaultTemplateId = pdfItem.getId();
                            }
                            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(pdfItem.getId())));
                        }
                    }
                    Integer finalDefaultTemplateId = defaultTemplateId;

                    SplitButtonItem pdfVersion = new SplitButtonItem("PDF_VERSION", wfmStrings.pdfVersion(), () -> generatePDF(finalDefaultTemplateId), true);
                    pdfVersion.ensureDebugId("check_" + "pdfVersionItem");
                    pdfTemplatesList.add(pdfVersion);
                    printPdfSplitButton.addItemList(pdfTemplatesList);
                    printPdfSplitButton.setVisible(true);
                }
            });

            addRightButton(printPdfSplitButton);
        }

        boolean own = Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT_OWN_PROFILE : PermissionConstants.HRMS_EDIT_OWN_PROFILE);
        if (Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT : PermissionConstants.HRMS_EDIT_PROFILE) ||
                (employeeID != null && Utils.getUserID().equals(employeeID) && own)) {
            WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
            editButton.addClickHandler(event -> {
                String action = null;
                String empName = profileItem.getFirstName();
                String empCode = profileItem.getEmpCode() != null && !profileItem.getEmpCode().isEmpty() ? profileItem.getEmpCode() : empName;
                if (FROM_HRMS_EMPLOYEE_VIEW.equals(from)) {
                    action = "editemployeeProfile|editprofile/" + employeeID;
                } else if (FROM_SETTINGS_EMPLOYEE_VIEW.equals(from)) {
                    action = "profileSettingsEdit|editprofile/" + employeeID;
                } else if (FROM_PM_EMPLOYEE_VIEW.equals(from)) {
                    action = "employee|edit/" + employeeID;
                } else if (tcInstructor) {
                    action = "tcInstructor|editInstructor/" + employeeID;
                }
                if (isEmployeeList)
                    closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged(action, empCode, empName);
            });
            addButton(editButton);
        }
        boolean hasSendButton = false;
        if (pmEmployeeView || hrmsEmployeeView) {
            boolean hasPermissionForSMSAndEmail = pmEmployeeView ? (Utils.hasPermission(PM_SEND_EMAIL) || Utils.hasPermission(PM_SEND_SMS))
                    : (Utils.hasPermission(HRMS_SEND_EMAIL) || Utils.hasPermission(HRMS_SEND_SMS));
            MaterialLink send = new MaterialLink(wfmStrings.send());
            MaterialSplitButton sendButton = new MaterialSplitButton(send);
            if (hasPermissionForSMSAndEmail) {
                hasSendButton = true;
                if (pmEmployeeView ? Utils.hasPermission(PM_SEND_EMAIL) : Utils.hasPermission(HRMS_SEND_EMAIL)) {
                    MaterialLink sendEmail = new MaterialLink(wfmStrings.email());
                    sendEmail.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + profileItem.getPrimaryEmail() + "/" + RelationItem.TYPE_EMPLOYEE + "/" + profileItem.getObjectId() + "/" + profileItem.getFirstName() + " " + profileItem.getLastName()));
                    sendButton.addItem(sendEmail);
                }
                if (pmEmployeeView ? Utils.hasPermission(PM_SEND_SMS) : Utils.hasPermission(HRMS_SEND_SMS)) {
                    MaterialLink sendSms = new MaterialLink(wfmStrings.sms());
                    sendSms.addClickHandler(event -> new ActivityQuickAddForm(Appointment.SMS, profileItem.getPrimaryPhone(), profileItem, RelationItem.newEventRelation(RelationItem.TYPE_EMPLOYEE, profileItem.getContactID(), profileItem.getName())));
                    sendButton.addItem(sendSms);
                }
            }
            if (Utils.hasPermission(HRMS_FULL_ADD_NEW_ACTIVITY_EVENT) || Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT)) {
                hasSendButton = true;
                MaterialLink eventButton = new MaterialLink(wfmStrings.event());
                if (Utils.hasPermission(HRMS_FULL_ADD_NEW_ACTIVITY_EVENT)) {
                    eventButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.EVENT + "/" + profileItem.getObjectId() + "/" + RelationItem.TYPE_EMPLOYEE));
                } else if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT)) {
                    eventButton.addClickHandler(event -> new ActivityQuickAddForm(Appointment.EVENT, profileItem.getPrimaryPhone(), profileItem, RelationItem.newEventRelation(RelationItem.TYPE_EMPLOYEE, profileItem.getObjectId(), profileItem.getName())));
                }
                sendButton.addItem(eventButton);
            }
            if (hasSendButton) {
                addRightButton(sendButton);
            }

        }

    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(NewGlobalEmployeeSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return employeeID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_EMPLOYEE;
                }

                @Override
                public String getRelationName() {
                    return String.valueOf(employeeID);
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private void generatePDF(Integer pdfTemplateID) {
        LeaveRequestObject requestObject = new LeaveRequestObject(employeeID, null, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/employeeProfileSummaryViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();

        Utils.sendPDFOrExcelRequest(this, pdfURL, parametrs, "_blank");
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        String from = tcInstructor ? TC_INSTRUCTOR_ADD_FORM : this.from;
        boolean canChange = Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_UPLOAD_PHOTO : PermissionConstants.HRMS_EMPLOYEE_UPLOAD_PHOTO);

        ContactService.App.get().editProfile(employeeID, from, true, new AbstractAsyncCallback<ProfileItem>() {
            public void success(ProfileItem o) {
                LoadingPanel.loading(false);
                profileItem = o;
                employeeDelete = new EmployeeRemovePopup(from, employeeID.toString(), profileItem.getFirstName(), null, true);
                employeeDelete.setDeleteCommand(() -> closeTab());
                if (profilePicture != null)
                    profilePicture.initialize(profileItem.getEmployeeImageUrl(), profileItem.getFirstName(), profileItem.getLastName(), canChange, profileItem.getGender());

                fillFormWithData();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMPLOYEE_PROFILE_FORM;
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
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Employee, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                NewGlobalEmployeeSummaryView.super.onInitialize();
            }
        });
//        initialize();
        return null;
    }

    protected void initialize() {
        super.onInitialize();
    }

    protected void registerFields() {

//        profilePanel();

        profilePicture = new ProfileImage(employeeID, LayoutRPC.HRMS_EMPLOYEE_FORM);

        firstname = new HTML();
        firstname.addStyleName(DEFAULT_WIDTH);
        firstname.ensureDebugId(employeeView + "firstname");

        lastname = new HTML();
        lastname.addStyleName(DEFAULT_WIDTH);
        lastname.ensureDebugId(employeeView + "lastname");

        middleName = new HTML();
        middleName.addStyleName(DEFAULT_WIDTH);
        middleName.ensureDebugId(employeeView + "middleName");

        othername = new HTML();
        othername.addStyleName(DEFAULT_WIDTH);
        othername.ensureDebugId(employeeView + "othername");

        driverID = initHTML();
        driverID.addStyleName(DEFAULT_WIDTH);
        driverID.ensureDebugId(employeeView + "driver_id");

        dob = new HTML();
        dob.addStyleName(DEFAULT_WIDTH);
        dob.ensureDebugId(employeeView + "dob");

        gender = new HTML();
        gender.addStyleName("gender-type" + maritalStatus);
        gender.ensureDebugId(employeeView + "gender");

        maritalStatus = new HTML();
        maritalStatus.addStyleName(DEFAULT_WIDTH);
        maritalStatus.ensureDebugId(employeeView + "maritalStatus");

        jobTitle = new HTML();
        jobTitle.addStyleName(DEFAULT_WIDTH);
        jobTitle.ensureDebugId(employeeView + "maritalStatus");

        /*languages = new HTML();
        languages.addStyleName(DEFAULT_WIDTH);
        languages.ensureDebugId(employeeView + "languages");*/

        //Languages
        languagesWidget = new FlexTable();
        languagesWidget.addStyleName("languagesWidget-table");
        languagesWidget.getElement().setId(employeeView + "languages");
        languagesWidget.getRowFormatter().addStyleName(0, "languagesWidget-table__thead");
        languagesWidget.setHTML(0, 0, wfmStrings.language());
        languagesWidget.setHTML(0, 1, wfmStrings.level());

        employeeDegree = new HTML();
        employeeDegree.addStyleName(DEFAULT_WIDTH);
        employeeDegree.ensureDebugId(employeeView + "degree");

        timeslot = new HTML();
        timeslot.addStyleName(DEFAULT_WIDTH);
        timeslot.ensureDebugId(employeeView + "timeslot");

        position = new HTML();
        position.addStyleName(DEFAULT_WIDTH);
        position.ensureDebugId(employeeView + "position");

        paymentMethod = new HTML();
        paymentMethod.addStyleName(DEFAULT_WIDTH);
        paymentMethod.ensureDebugId(employeeView + "paymentMethod");

        empHistory = new HTML();
        empHistory.addStyleName(DEFAULT_WIDTH);
        empHistory.ensureDebugId(employeeView + "empHistory");

        roles = new HTML();
        roles.addStyleName(DEFAULT_WIDTH);
        roles.ensureDebugId(employeeView + "roles");

        nationality = new HTML();
        nationality.addStyleName(DEFAULT_WIDTH);
        nationality.ensureDebugId(employeeView + "nationality");

        location = new HTML();
        location.addStyleName(DEFAULT_WIDTH);
        location.ensureDebugId(employeeView + "location");

        emailInf = new FlowPanel();
        phoneNumInf = new FlowPanel();
        telegramInf = new FlowPanel();

        imsAddressInf = new FlowPanel();
        webSiteInf = new FlowPanel();
        addressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : addressInf.getWidgets()) {
                    AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }, true);
        parentAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : parentAddressInf.getWidgets()) {
                    AddressNewUIWidget addressWidget = (AddressNewUIWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }, true);

        passportNumber = new HTML();
        passportNumber.addStyleName(DEFAULT_WIDTH);
        passportNumber.ensureDebugId(employeeView + "passportNumber");

        passportIssueDate = new HTML();
        passportIssueDate.addStyleName(DEFAULT_WIDTH);
        passportIssueDate.ensureDebugId(employeeView + "passportIssueDate");

        passportIssBy = new HTML();
        passportIssBy.addStyleName(DEFAULT_WIDTH);
        passportIssBy.ensureDebugId(employeeView + "passportIssueDate");

        passportExpireDate = new HTML();
        passportExpireDate.addStyleName(DEFAULT_WIDTH);
        passportExpireDate.ensureDebugId(employeeView + "passportExpireDate");

        insuranceNumber = new HTML();
        insuranceNumber.addStyleName(DEFAULT_WIDTH);
        insuranceNumber.ensureDebugId(employeeView + "passportExpireDate");

        medicalInsuranceExDate = new HTML();
        medicalInsuranceExDate.addStyleName(DEFAULT_WIDTH);
        medicalInsuranceExDate.ensureDebugId(employeeView + "medicalInsuranceExDate");

        visaNumber = new HTML();
        visaNumber.addStyleName(DEFAULT_WIDTH);
        visaNumber.ensureDebugId(employeeView + "visaNumber");

        wpsNumber = new HTML();
        wpsNumber.addStyleName(DEFAULT_WIDTH);
        wpsNumber.ensureDebugId(employeeView + "wpsNumber");

        openingBalanceDays = new HTML();
        openingBalanceDays.addStyleName(DEFAULT_WIDTH);
        openingBalanceDays.ensureDebugId(employeeView + "openingBalanceDate");

        probationDays = new HTML();
        probationDays.addStyleName(DEFAULT_WIDTH);
        probationDays.ensureDebugId(employeeView + "probationDays");

        visaaIssueDate = new HTML();
        visaaIssueDate.addStyleName(DEFAULT_WIDTH);
        visaaIssueDate.ensureDebugId(employeeView + "visaaIssueDate");

        visaExpireDate = new HTML();
        visaExpireDate.addStyleName(DEFAULT_WIDTH);
        visaExpireDate.ensureDebugId(employeeView + "visaExpireDate");

        employeeCode = new HTML();
        employeeCode.addStyleName(DEFAULT_WIDTH);
        employeeCode.ensureDebugId(employeeView + "employeeCode");

        status = new HTML();
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(employeeView + "status");

        essUser = new HTML();
        essUser.addStyleName(DEFAULT_WIDTH);
        essUser.ensureDebugId(employeeView + "essUser");

        noAccess = new HTML();
        noAccess.addStyleName(DEFAULT_WIDTH);
        noAccess.ensureDebugId(employeeView + "noAccess");

        wageRate = new HTML();
        wageRate.addStyleName(DEFAULT_WIDTH);
        wageRate.ensureDebugId(employeeView + "wageRate");

        clientChageRate = new HTML();
        clientChageRate.addStyleName(DEFAULT_WIDTH);
        clientChageRate.ensureDebugId(employeeView + "clientChageRate");

        qualification = new HTML();
        qualification.addStyleName(DEFAULT_WIDTH);
        qualification.ensureDebugId(employeeView + "clientChageRate");

        employementMode = new HTML();
        employementMode.addStyleName(DEFAULT_WIDTH);
        employementMode.ensureDebugId(employeeView + "employementMode");

        hireDate = new HTML();
        hireDate.addStyleName(DEFAULT_WIDTH);
        hireDate.ensureDebugId(employeeView + "hireDate");

        resignationDate = new HTML();
        resignationDate.addStyleName(DEFAULT_WIDTH);
        resignationDate.ensureDebugId(employeeView + "resignationDate");

        supervisor = new HTML();
        supervisor.addStyleName(DEFAULT_WIDTH);
        supervisor.ensureDebugId(employeeView + "supervisor");

        termOfContract = new HTML();
        termOfContract.addStyleName(DEFAULT_WIDTH);
        termOfContract.ensureDebugId(employeeView + "termOfContract");

        salaryGrade = new HTML();
        salaryGrade.addStyleName(DEFAULT_WIDTH);
        salaryGrade.ensureDebugId(employeeView + "salaryGrade");

        salaryMode = new HTML();
        salaryMode.addStyleName(DEFAULT_WIDTH);
        salaryMode.ensureDebugId(employeeView + "salaryMode");

        basicSalary = new HTML();
        basicSalary.addStyleName(DEFAULT_WIDTH);
        basicSalary.ensureDebugId(employeeView + "basicSalary");

        basicTotalSalary = new HTML();
        basicTotalSalary.addStyleName(DEFAULT_WIDTH);
        basicTotalSalary.ensureDebugId(employeeView + "basicTotalSalary");

        department = new HTML();
        department.addStyleName(DEFAULT_WIDTH);
        department.ensureDebugId(employeeView + "department");

        bankName = new HTML();
        bankName.addStyleName(DEFAULT_WIDTH);
        bankName.ensureDebugId(employeeView + "bankName");

        bankAddress = new HTML();
        bankAddress.addStyleName(DEFAULT_WIDTH);
        bankAddress.ensureDebugId(employeeView + "bankAddress");

        accountNumber = new HTML();
        accountNumber.addStyleName(DEFAULT_WIDTH);
        accountNumber.ensureDebugId(employeeView + "accountNumber");

        accountName = new HTML();
        accountName.addStyleName(DEFAULT_WIDTH);
        accountName.ensureDebugId(employeeView + "accountName");

        swiftCode = new HTML();
        swiftCode.addStyleName(DEFAULT_WIDTH);
        swiftCode.ensureDebugId(employeeView + "swiftCode");

        sortCode = new HTML();
        sortCode.addStyleName(DEFAULT_WIDTH);
        sortCode.ensureDebugId(employeeView + "sortCode");

        ibanCode = new HTML();
        ibanCode.addStyleName(DEFAULT_WIDTH);
        ibanCode.ensureDebugId(employeeView + "ibanCode");

        agentID = new HTML();
        agentID.addStyleName(DEFAULT_WIDTH);
        agentID.ensureDebugId(employeeView + "agentID");

        paymentsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.payments(), false, false), false);
        deductionsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.deductions(), false, true), false);
        taxTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.taxes(), false, true), false);
        loansTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.loans(), true, false), false);
        employerContributionTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.employerContribution(), false, true), false);

        uploadForm = new GeneralFileUpload(F_EMPLOYEE_PROFILE, employeeID, employeeID);
        LoadingPanel.loading(false);
    }

    private ColumnConfig[] getPaymentDeductionTableColumns(String title, boolean isLoan, boolean isLinkedType) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        if (isLoan) {
            columns.add(new ColumnConfig(CustomCell.class, "category", title, 220, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 110, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + "%", 110, true, "right-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "totalAmount", wfmStrings.total() + " " + wfmStrings.amount(), 100, true, "right-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "startDate", wfmStrings.startDate(), 100, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "remainAmount", wfmStrings.remainingAmount(), 100, true, "right-align-Cell"));
        } else if (isLinkedType) {
            columns.add(new ColumnConfig(CustomCell.class, "category", title, 220, true, "left-align-Cell"));
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 110, true, "left-align-Cell"));
            }
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 210, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + "%", 120, true, "right-align-Cell"));
        } else {
            columns.add(new ColumnConfig(CustomCell.class, "category", title, 220, true, "left-align-Cell"));
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 110, true, "left-align-Cell"));
            }
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 110, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + "%", 110, true, "right-align-Cell"));
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    protected void drawEmployeeInformation() {
        String employeeInformationTITLE = tcInstructor ? wfmStrings.instructorDetails() : wfmStrings.personalInformation();
        addTitleField(CustomFormConstants.EMPLOYEE_INFORMATION, employeeInformationTITLE);

        addField(CustomFormConstants.PROFILE_PICTURE, profilePicture, null, true);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(CustomFormConstants.FIRST_NAME, firstname, getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName()));
        } else {
            addField(CustomFormConstants.FIRST_NAME, firstname, getTitle(wfmStrings.firstName(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            addField(CustomFormConstants.LAST_NAME, lastname, getTitle(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName()));
        } else {
            addField(CustomFormConstants.LAST_NAME, lastname, getTitle(wfmStrings.lastName()));
        }

        addField(CustomFormConstants.OTHER_NAME, othername, getTitle(wfmStrings.otherName()));
        addField(CustomFormConstants.DRIVER_ID, driverID, getTitle("Driver ID", true));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MIDDLE_NAME) != null) {
            addField(CustomFormConstants.MIDDLE_NAME, middleName, getTitle(formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName()));
        } else {
            addField(CustomFormConstants.MIDDLE_NAME, middleName, getTitle(wfmStrings.middleName()));
        }
        if (Utils.hasPermission(EMP_PROFILE_BASIC_SALARY) && !Utils.isSettings()) {
            addField(CustomFormConstants.SALARY_AMOUNT, basicSalary, getTitle(wfmStrings.basicSalary()));
            addField(CustomFormConstants.SALARY_TOTAL_AMOUNT, basicTotalSalary, getTitle(wfmStrings.totalSalary()));
        }
        String birthDayPermission = pmEmployeeView ? PermissionConstants.PM_SHOW_EMPLOYEE_BIRTH_DAY : PermissionConstants.HRMS_SHOW_EMPLOYEE_BIRTH_DAY;
        if (Utils.hasPermission(birthDayPermission) || Utils.getUserID().equals(employeeID)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY) != null) {
                addField(CustomFormConstants.BIRTH_DAY, dob, getTitle(formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isChanged() ? formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getTitle() : wfmStrings.dateOfBirth()));
            } else {
                addField(CustomFormConstants.BIRTH_DAY, dob, getTitle(wfmStrings.dateOfBirth()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null) {
            addField(CustomFormConstants.GENDER, gender, getTitle(formPropertyMap.get(CustomFormConstants.GENDER).isChanged() ? formPropertyMap.get(CustomFormConstants.GENDER).getTitle() : wfmStrings.gender()));
        } else {
            addField(CustomFormConstants.GENDER, gender, getTitle(wfmStrings.gender()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NATIONALITY) != null) {
            addField(CustomFormConstants.NATIONALITY, nationality, getTitle(formPropertyMap.get(CustomFormConstants.NATIONALITY).isChanged() ? formPropertyMap.get(CustomFormConstants.NATIONALITY).getTitle() : wfmStrings.nationality()));
        } else {
            addField(CustomFormConstants.NATIONALITY, nationality, getTitle(wfmStrings.nationality()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS) != null) {
            addField(CustomFormConstants.MARTIAL_STATUS, maritalStatus, getTitle(formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.MARTIAL_STATUS).getTitle() : wfmStrings.maritalStatus()));
        } else {
            addField(CustomFormConstants.MARTIAL_STATUS, maritalStatus, getTitle(wfmStrings.maritalStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LANGUAGE) != null) {
            addField(CustomFormConstants.LANGUAGE, languagesWidget, getTitle(formPropertyMap.get(CustomFormConstants.LANGUAGE).isChanged() ? formPropertyMap.get(CustomFormConstants.LANGUAGE).getTitle() : wfmStrings.spokenLanguages()));
        } else {
            addField(CustomFormConstants.LANGUAGE, languagesWidget, getTitle(wfmStrings.spokenLanguages()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE_DEGREE) != null) {
            addField(CustomFormConstants.EMPLOYEE_DEGREE, employeeDegree, getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYEE_DEGREE).isChanged() ? formPropertyMap.get(CustomFormConstants.EMPLOYEE_DEGREE).getTitle() : wfmStrings.degree()));
        } else {
            addField(CustomFormConstants.EMPLOYEE_DEGREE, employeeDegree, getTitle(wfmStrings.degree()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TIMESLOT) != null) {
            addField(CustomFormConstants.TIMESLOT, timeslot, getTitle(formPropertyMap.get(CustomFormConstants.TIMESLOT).isChanged() ? formPropertyMap.get(CustomFormConstants.TIMESLOT).getTitle() : wfmStrings.timeslot()));
        } else {
            addField(CustomFormConstants.TIMESLOT, timeslot, getTitle(wfmStrings.timeslot()));
        }
    }

    protected void drawContactDetails() {
        addTitleField(CustomFormConstants.CONTACT_INFORMATION, Property.get(Constants.Contacts, wfmStrings.contactDetails(), wfmStrings.contact()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(CustomFormConstants.EMAIL, emailInf, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(CustomFormConstants.EMAIL, emailInf);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null) {
            addField(CustomFormConstants.PHONE, phoneNumInf, getTitle(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone()));
        } else {
            addField(CustomFormConstants.PHONE, phoneNumInf);
        }

        if (!Utils.isSettings()) {
            addField(CustomFormConstants.IM_ADDRESS, imsAddressInf);
            addField(CustomFormConstants.WEB_ADDRESS, webSiteInf);
        }
    }

    protected void drawAddressInformation() {
        if (!tcInstructor) {
            String permission = pmEmployeeView ? PermissionConstants.PM_SHOW_EMPLOYEE_ADDRESS : PermissionConstants.HRMS_SHOW_EMPLOYEE_ADDRESS;
            if ((employeeID != null && employeeID.equals(Utils.getUserID())) || Utils.hasPermission(permission)) {
                addTitleField(CustomFormConstants.ADDRESS_INFORMATION, wfmStrings.addressInformation());
                addField(CustomFormConstants.ADDRESS, addressInf, wfmStrings.address());
            }
        }
    }

    protected void drawAccountInformation() {
        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
            addTitleField(CustomFormConstants.ACCOUNT_INFORMATION, wfmStrings.basicInfo());
            addField(CustomFormConstants.ACCOUNT_STATUS, status, getTitle(wfmStrings.accountStatus()));
            int maxEssUser = profileItem != null && profileItem.getUserLimit() != null ? profileItem.getUserLimit()[Constants.ESS] : 0;
            if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET) && (maxEssUser > 0 || (profileItem != null && profileItem.getEss()))) {
                addField(CustomFormConstants.ESS_USER, essUser, getTitle(wfmStrings.essUser()));
            }
            addField(CustomFormConstants.NO_ACCESS, noAccess, getTitle(wfmStrings.noAccess()));
        }
    }

    protected void drawPaymentDeductionCategoryTable() {
        if (Utils.hasPermission(HRMS_PAYROLL_DEDUCTION_CATEGORIES)) {
            addTitleField(CustomFormConstants.PAYMENT_DEDUCTION_INFORMATION, wfmStrings.paymentDeductionCategoryTable());
            addField(CustomFormConstants.PAYMENT_TABLE, paymentsTable, null);
            addField(CustomFormConstants.DEDUCTION_TABLE, deductionsTable, null);
            addField(CustomFormConstants.TAX_TABLE, taxTable, null);
            addField(CustomFormConstants.LOAN_TABLE, loansTable, null);
            addField(CustomFormConstants.EMPLOYER_CONTRIBUTION, employerContributionTable, null);
        }
    }

    protected void drawEmploymentInformation() {
        addTitleField(CustomFormConstants.EMPLOYMENT_INFORMATION, getTitle(wfmStrings.employmentInformation()));
        if (from.equals(FROM_HRMS_EMPLOYEE_VIEW)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE) != null) {
                addField(CustomFormConstants.EMPLOYEE_CODE, employeeCode, getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).getTitle() : wfmStrings.employeeCode()));
            } else {
                addField(CustomFormConstants.EMPLOYEE_CODE, employeeCode, getTitle(wfmStrings.employeeCode()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department()));
        } else {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.POSITION) != null) {
            addField(CustomFormConstants.POSITION, position, getTitle(formPropertyMap.get(CustomFormConstants.POSITION).isChanged() ? formPropertyMap.get(CustomFormConstants.POSITION).getTitle() : wfmStrings.position()));
        } else {
            addField(CustomFormConstants.POSITION, position, getTitle(wfmStrings.position()));
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CANDIDATE.LOCATION) != null) {
            addField(CustomFormConstants.LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.CANDIDATE.LOCATION).isRequired()));
        } else {
            addField(CustomFormConstants.LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), false));
        }

        if (!pmEmployeeView) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPERVISOR) != null) {
                addField(CustomFormConstants.SUPERVISOR, supervisor, getTitle(formPropertyMap.get(CustomFormConstants.SUPERVISOR).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPERVISOR).getTitle() : wfmStrings.supervisor()));
            } else {
                addField(CustomFormConstants.SUPERVISOR, supervisor, getTitle(wfmStrings.supervisor()));
            }
        }
        if (Utils.hasAccessToDefaultEmployeeRate(employeeID)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAGE_RATE) != null) {
                addField(CustomFormConstants.WAGE_RATE, wageRate, getTitle(formPropertyMap.get(CustomFormConstants.WAGE_RATE).isChanged() ? formPropertyMap.get(CustomFormConstants.WAGE_RATE).getTitle() : wfmStrings.wageRate()));
            } else {
                addField(CustomFormConstants.WAGE_RATE, wageRate, getTitle(wfmStrings.wageRate()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_CHARGE_RATE) != null) {
                addField(CustomFormConstants.CLIENT_CHARGE_RATE, clientChageRate, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_CHARGE_RATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_CHARGE_RATE).getTitle() : wfmStrings.clientChargeRate()));
            } else {
                addField(CustomFormConstants.CLIENT_CHARGE_RATE, clientChageRate, getTitle(wfmStrings.clientChargeRate()));
            }

        }
        if (!pmEmployeeView) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE) != null) {
                addField(CustomFormConstants.HIRE_DATE, hireDate, getTitle(formPropertyMap.get(CustomFormConstants.HIRE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.HIRE_DATE).getTitle() : wfmStrings.hireDate()));
            } else {
                addField(CustomFormConstants.HIRE_DATE, hireDate, getTitle(wfmStrings.hireDate()));
            }

            addField(CustomFormConstants.SALARY_GRADE, salaryGrade, getTitle(wfmStrings.salaryGrade()));
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE) != null) {
                addField(CustomFormConstants.RESIGNATION_DATE, resignationDate, getTitle(formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).getTitle() : wfmStrings.resignationDate()));
            } else {
                addField(CustomFormConstants.RESIGNATION_DATE, resignationDate, getTitle(wfmStrings.resignationDate()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS) != null) {
                addField(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS, termOfContract, getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).isChanged() ? formPropertyMap.get(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS).getTitle() : wfmStrings.employmentContractTerms()));
            } else {
                addField(CustomFormConstants.EMPLOYMENT_CONTACT_TERMS, termOfContract, getTitle(wfmStrings.employmentContractTerms()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYMENT_MODE) != null) {
                addField(CustomFormConstants.EMPLOYMENT_MODE, employementMode, getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYMENT_MODE).isChanged() ? formPropertyMap.get(CustomFormConstants.EMPLOYMENT_MODE).getTitle() : wfmStrings.employmentMode()));
            } else {
                addField(CustomFormConstants.EMPLOYMENT_MODE, employementMode, getTitle(wfmStrings.employmentMode()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.QUALIFICATION) != null) {
                addField(CustomFormConstants.QUALIFICATION, qualification, getTitle(formPropertyMap.get(CustomFormConstants.QUALIFICATION).isChanged() ? formPropertyMap.get(CustomFormConstants.QUALIFICATION).getTitle() : wfmStrings.qualification()));
            } else {
                addField(CustomFormConstants.QUALIFICATION, qualification, getTitle(wfmStrings.qualification()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE) != null) {
                addField(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDays, getTitle(formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).getTitle() : wfmStrings.openingBalanceForAnnualLeave()));
            } else {
                addField(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDays, getTitle(wfmStrings.openingBalanceForAnnualLeave()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROBATION_DAYS) != null) {
                addField(CustomFormConstants.PROBATION_DAYS, probationDays, getTitle(formPropertyMap.get(CustomFormConstants.PROBATION_DAYS).isChanged() ? formPropertyMap.get(CustomFormConstants.PROBATION_DAYS).getTitle() : wfmStrings.probationPeriodDays()));
            } else {
                addField(CustomFormConstants.PROBATION_DAYS, probationDays, getTitle(wfmStrings.probationPeriodDays()));
            }
            if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_MODE) != null) {
                    addField(CustomFormConstants.SALARY_MODE, salaryMode, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_MODE).isChanged() ?
                            formPropertyMap.get(CustomFormConstants.SALARY_MODE).getTitle() : wfmStrings.salaryMode()));
                } else {
                    addField(CustomFormConstants.SALARY_MODE, salaryMode, getTitle(wfmStrings.salaryMode()));
                }
            }
        }
    }

    protected void drawBankInformation() {
        addTitleField(CustomFormConstants.BANK_ACCOUNT_INFORMATION, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountInformation(), wfmStrings.bankAccount()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BANK_NAME) != null) {
            addField(CustomFormConstants.BANK_NAME, bankName, getTitle(formPropertyMap.get(CustomFormConstants.BANK_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.BANK_NAME).getTitle() : wfmStrings.bankName()));
        } else {
            addField(CustomFormConstants.BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_NUMBER) != null) {
            addField(CustomFormConstants.ACCOUNT_NUMBER, accountNumber, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNumber()));
        } else {
            addField(CustomFormConstants.ACCOUNT_NUMBER, accountNumber, getTitle(wfmStrings.accountNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_NAME) != null) {
            addField(CustomFormConstants.ACCOUNT_NAME, accountName, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNT_NAME).getTitle() : wfmStrings.accountName()));
        } else {
            addField(CustomFormConstants.ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BANK_ADDRESS) != null) {
            addField(CustomFormConstants.BANK_ADDRESS, bankAddress, getTitle(formPropertyMap.get(CustomFormConstants.BANK_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.BANK_ADDRESS).getTitle() : wfmStrings.bankAddress()));
        } else {
            addField(CustomFormConstants.BANK_ADDRESS, bankAddress, getTitle(wfmStrings.bankAddress()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SWIFT_CODE) != null) {
            addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(formPropertyMap.get(CustomFormConstants.SWIFT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SWIFT_CODE).getTitle() : wfmStrings.swiftCode()));
        } else {
            addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SORT_CODE) != null) {
            addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(formPropertyMap.get(CustomFormConstants.SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SORT_CODE).getTitle() : wfmStrings.sortCode()));
        } else {
            addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IBAN_CODE) != null) {
            addField(CustomFormConstants.IBAN_CODE, ibanCode, getTitle(formPropertyMap.get(CustomFormConstants.IBAN_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.IBAN_CODE).getTitle() : wfmStrings.ibanCode()));
        } else {
            addField(CustomFormConstants.IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AGENT_ID) != null) {
            addField(CustomFormConstants.AGENT_ID, agentID, getTitle(formPropertyMap.get(CustomFormConstants.AGENT_ID).isChanged() ? formPropertyMap.get(CustomFormConstants.AGENT_ID).getTitle() : wfmStrings.agentID()));
        } else {
            addField(CustomFormConstants.AGENT_ID, agentID, getTitle(wfmStrings.agentID()));
        }
    }

    protected void drawPersonalIdentityInformation() {
        addTitleField(CustomFormConstants.PERSONAL_IDENTITY_INFORMATION, wfmStrings.personalIdentityInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER) != null) {
            addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).getTitle() : wfmStrings.passportNumber()));
        } else {
            addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, getTitle(wfmStrings.passportNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_ISSUE) != null) {
            addField(CustomFormConstants.PASSPORT_ISSUE, passportIssBy, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_ISSUE).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_ISSUE).getTitle() : wfmStrings.passportIssueBy()));
        } else {
            addField(CustomFormConstants.PASSPORT_ISSUE, passportIssBy, getTitle(wfmStrings.passportIssueBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_ISSUE_DATE) != null) {
            addField(CustomFormConstants.PASSPORT_ISSUE_DATE, passportIssueDate, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_ISSUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_ISSUE_DATE).getTitle() : wfmStrings.passportIssueDate()));
        } else {
            addField(CustomFormConstants.PASSPORT_ISSUE_DATE, passportIssueDate, getTitle(wfmStrings.passportIssueDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_EXPIRY_DATE) != null) {
            addField(CustomFormConstants.PASSPORT_EXPIRY_DATE, passportExpireDate, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_EXPIRY_DATE).getTitle() : wfmStrings.passportExpireDate()));
        } else {
            addField(CustomFormConstants.PASSPORT_EXPIRY_DATE, passportExpireDate, getTitle(wfmStrings.passportExpireDate()));
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INSURANCE_NUMBER) != null) {
            addField(CustomFormConstants.INSURANCE_NUMBER, insuranceNumber, getTitle(formPropertyMap.get(CustomFormConstants.INSURANCE_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.INSURANCE_NUMBER).getTitle() : wfmStrings.insuranseNumber()));
        } else {
            addField(CustomFormConstants.INSURANCE_NUMBER, insuranceNumber, getTitle(wfmStrings.insuranseNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISA_NUMBER) != null) {
            addField(CustomFormConstants.VISA_NUMBER, visaNumber, getTitle(formPropertyMap.get(CustomFormConstants.VISA_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.VISA_NUMBER).getTitle() : wfmStrings.visaNumber()));
        } else {
            addField(CustomFormConstants.VISA_NUMBER, visaNumber, getTitle(wfmStrings.visaNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER) != null) {
            addField(CustomFormConstants.WPS_NUMBER, wpsNumber, getTitle(formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getTitle() : wfmStrings.wpsNumber()));
        } else {
            addField(CustomFormConstants.WPS_NUMBER, wpsNumber, getTitle(!"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISA_ISSUE_DATE) != null) {
            addField(CustomFormConstants.VISA_ISSUE_DATE, visaaIssueDate, getTitle(formPropertyMap.get(CustomFormConstants.VISA_ISSUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.VISA_ISSUE_DATE).getTitle() : wfmStrings.visaIssueDate()));
        } else {
            addField(CustomFormConstants.VISA_ISSUE_DATE, visaaIssueDate, getTitle(wfmStrings.visaIssueDate()));
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_VISA_EXPIRATION_DATE) || (employeeID != null && Utils.getUserID().equals(employeeID))) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISA_EXPIRATION_DATE) != null) {
                addField(CustomFormConstants.VISA_EXPIRATION_DATE, visaExpireDate, getTitle(formPropertyMap.get(CustomFormConstants.VISA_EXPIRATION_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.VISA_EXPIRATION_DATE).getTitle() : wfmStrings.visaExpirationDate()));
            } else {
                addField(CustomFormConstants.VISA_EXPIRATION_DATE, visaExpireDate, getTitle(wfmStrings.visaExpirationDate()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.INSURANCE_EXPIRY_DATE) != null) {
            addField(CustomFormConstants.INSURANCE_EXPIRY_DATE, medicalInsuranceExDate, getTitle(formPropertyMap.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.INSURANCE_EXPIRY_DATE).getTitle() : wfmStrings.insuranceExpiryDate()));
        } else {
            addField(CustomFormConstants.INSURANCE_EXPIRY_DATE, medicalInsuranceExDate, getTitle(wfmStrings.insuranceExpiryDate()));
        }
    }

    protected void drawAttachments() {
        addField(CustomFormConstants.ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
    }

    protected void drawCustomFields() {
        String customPermission = pmEmployeeView ? PermissionConstants.PM_SHOW_ADDITIONAL_INFORMATION : PermissionConstants.HRMS_SHOW_ADDITIONAL_INFORMATION;
        if (Utils.hasPermission(customPermission)) {
            CommonService.App.get().getCompanyCustomFields( tcInstructor ? ViewName.Instructor : ViewName.Employee, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                public void failure(Throwable throwable) {
                }

                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        getCustomFieldUtil().setCompanyCustomFieldItems(result);
                        drawEmployeeCustomFields();
                    }
                }
            });
        }
    }

    private void drawEmployeeCustomFields() {
        getCustomFieldUtil().drawCustomFields(NewGlobalEmployeeSummaryView.this, employeeID, true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
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
                        CustomFormItemGrid itemView = new CustomFormItemGrid(employeeID, configMap.getKey(), LayoutRPC.HRMS_EMPLOYEE_FORM, configMap.getValue(), 1000);
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }


    protected void drawExperienceTable() {
        addTitleField(CustomFormConstants.EXPERIENCE, "");
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
            }
        });

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
                experienceTable.ensureDebugId("Rotation_item_table");
                experienceTable.setWidth("100%");
                addField(EXPERIENCE, experienceTable, "");
            }
        });
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

    private Widget[] widgets(ExperienceTableItems item) {
        int index1 = 0;
        ArrayList<Widget> widgets = new ArrayList<>();
        for (String columnCode : experienceColumnsMap.keySet()) {
            if (ItemTableConstants.HIRE_DATE.equals(columnCode)) {
               CustomDatePicker hireDate = new CustomDatePicker();
                hireDate.setWidth("100%");
                hireDate.setDate(item.getHireDate());
                hireDate.setTitle(columnCode);
                hireDate.setEnabled(false);
                widgets.add(hireDate);
            } else if (ItemTableConstants.RESIGN_DATE.equals(columnCode)) {
                CustomDatePicker resignDate = new CustomDatePicker();
                resignDate.setWidth("100%");
                resignDate.setDate(item.getResignDate());
                resignDate.setTitle(columnCode);
                resignDate.setEnabled(false);
                widgets.add(resignDate);
            } else if (ItemTableConstants.INDUSTRY.equals(columnCode)) {
                ReferenceLookUp industry = new ReferenceLookUp(_COMPANY_WORKAREA);
                industry.setWidth("100%");
                industry.setSelected(item.getIndustry());
                industry.setTitle(columnCode);
                industry.setEnabled(false);
                widgets.add(industry);
            } else if (ItemTableConstants.POSITION.equals(columnCode)) {
                CustomCellTextBox position = new CustomCellTextBox();
                position.setWidth("100%");
                position.setText(item.getPosition());
                position.setTitle(columnCode);
                position.setEnabled(false);
                widgets.add(position);
            } else if (ItemTableConstants.DEPARTMENT.equals(columnCode)) {
                CustomCellTextBox department = new CustomCellTextBox();
                department.setWidth("100%");
                department.setText(item.getDepartment());
                department.setTitle(columnCode);
                department.setEnabled(false);
                widgets.add(department);
            }else if (ItemTableConstants.ORGANIZATION.equals(columnCode)) {
                CustomCellTextBox organization = new CustomCellTextBox();
                organization.setWidth("100%");
                organization.setText(item.getOrganization());
                organization.setTitle(columnCode);
                organization.setEnabled(false);
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
                    customTextAreaField.setEnabled(false);
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





    protected void fillFormWithData() {
        grandTotalPayment = BigDecimal.ZERO;
        grandTotalDeduction = BigDecimal.ZERO;
        basicSalaryAmount = profileItem.getSalaryAmount() != null ? BigDecimal.valueOf(profileItem.getSalaryAmount()) : BigDecimal.ZERO;

        if (pmEmployeeView) {
            profileItem.setCreatedFrom(ContactListItem.REQUEST_FROM_PM_EMPLOYEE_EDIT);
            if (profileItem.getEditablePermission() != EDIT && !Utils.hasRole(ADMIN_LOCATION)) {
//                enableButton(false);
            }
        }
        fillFormWithDataContact(profileItem);
        setMultiTableItems(Constants.CONTACT_EMAILS);
        setMultiTableItems(Constants.CONTACT_PHONES);
        setMultiTableItems(Constants.CONTACT_IMADDRESSES);
        setMultiTableItems(Constants.CONTACT_WEBSITES);
        setTelegramWidgets();

//        fillProfile();

        String profileItemGender = "";
        if (Constants.MALE.equals(profileItem.getGender())) {
            profileItemGender = wfmStrings.male();
        } else if (Constants.FEMALE.equals(profileItem.getGender())) {
            profileItemGender = wfmStrings.female();
        } else if (Constants.OTHER.equals(profileItem.getGender())) {
            profileItemGender = wfmStrings.other();
        } else if (Constants.VACANT.equals(profileItem.getGender())) {
            profileItemGender = wfmStrings.vacancy();
        }
        gender.setHTML(profileItemGender);
        paymentMethod.setHTML(profileItem.getPaymentMethod());
        driverID.setHTML(profileItem.getDriverID());
        maritalStatus.setHTML(profileItem.getMartialStatus());
        nationality.setHTML(profileItem.getNationality());
        while (languagesWidget.getRowCount() > 1) {
            languagesWidget.removeRow(1);
        }
        if (profileItem.getSpokingLanguages() != null) {
            profileItem.getSpokingLanguages().forEach(sl -> {
                int index = languagesWidget.getRowCount();
                languagesWidget.setHTML(index, 0, sl.getLanguage() != null ? sl.getLanguage().getName() : "");
                languagesWidget.setHTML(index, 1, sl.getLevel() != null ? sl.getLevel().getName() : "");
            });
        }
        /*languages.setHTML(profileItem.getSpokenLanguagesAsString());*/
        employeeDegree.setHTML(profileItem.getemployeeDegree() != null ? profileItem.getemployeeDegree().getName() : "");
        timeslot.setHTML(profileItem.getTimeslot() != null ? profileItem.getTimeslot().getName() : "");
        employeeCode.setHTML(profileItem.getEmpCode());
        department.setHTML(profileItem.getDepartment());
        wageRate.setHTML(profileItem.getWageRate() != null ? profileItem.getWageRate().toString() : "0");
        clientChageRate.setHTML(profileItem.getClientChargeRate() != null ? profileItem.getClientChargeRate().toString() : "0");
        status.setHTML(profileItem.getStatus());
        essUser.setHTML(profileItem.getEss() ? wfmStrings.yes() : wfmStrings.no());
        noAccess.setHTML(EMPLOYEE_STATUS_NO_ACCCESS.equals(profileItem.getStatusCode()) ? wfmStrings.yes() : wfmStrings.no());
        hireDate.setHTML(DateUtils.format(profileItem.getHireDate()));
        openingBalanceDays.setHTML(profileItem.getOpeningBalanceDays() != null ? String.valueOf(profileItem.getOpeningBalanceDays()) : "");
        probationDays.setHTML(profileItem.getProbationDays() != null ? String.valueOf(profileItem.getProbationDays()) : "");
        resignationDate.setHTML(DateUtils.format(profileItem.getFireDate()));
        qualification.setHTML(profileItem.getQualificationName());
        supervisor.setHTML(profileItem.getReportsTo());
        if (profileItem.getTermsOfContract() != null) {
            if (profileItem.getTermsOfContract() != 0 && profileItem.getTermsOfContract() > 0) {
                if (profileItem.getTermsOfCMonthORYear() != null && profileItem.getTermsOfCMonthORYear() == 1) {
                    termOfContract.setHTML(profileItem.getTermsOfContract().toString() + wfmStrings.month());
                } else if (profileItem.getTermsOfCMonthORYear() != null && profileItem.getTermsOfCMonthORYear() == 2) {
                    if (profileItem.getTermsOfContract() == 1) {
                        termOfContract.setHTML(profileItem.getTermsOfContract().toString() + wfmStrings.year());
                    } else {
                        termOfContract.setHTML(profileItem.getTermsOfContract().toString() + wfmStrings.years());
                    }
                }
            }
        }
        employementMode.setHTML(profileItem.getEmpMode());
//        if (CompanyConstants.C25507.equals(Utils.getEncryptedCompanyID()) ) {  //Ian's company (Solidnet-25507, Kloud - 26338)
//            if (Utils.hasRole(ADMIN)) {
//                salaryGrade.setHTML(profileItem.getSalaryGrade());
//                basicSalary.setHTML(profileItem.getSalaryAmount() != null ? extendedNumberFormat.format(profileItem.getSalaryAmount()) : "");
//            }
//        }
//        else {
        salaryMode.setHTML(profileItem.getSalaryMode() != null && !profileItem.getSalaryMode().equals(TARIFF_GRID) ? profileItem.getSalaryMode().equals(FIXED_AMOUNT) ? wfmStrings.byFixedAmount() : wfmStrings.basedOnAttendanceReport() : wfmStrings.byTariffGrid());
        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(HR)) {
            salaryGrade.setHTML(profileItem.getSalaryGrade());
        }
        if (Utils.hasPermission(EMP_PROFILE_BASIC_SALARY)) {
            basicSalary.setHTML(profileItem.getSalaryAmount() != null ? extendedNumberFormat.format(profileItem.getSalaryAmount()) : "");
        }
//        }
        jobTitle.setHTML(profileItem.getJobTitle());
        UserBankAccountData bankData = profileItem.getBankAccountData();
        if (bankData != null) {
            bankName.setHTML(bankData.getBankName());
            bankAddress.setHTML(bankData.getBankAddress());
            accountNumber.setHTML(bankData.getAccountNumber());
            accountName.setHTML(bankData.getAccountName());
            swiftCode.setHTML(bankData.getSwiftCode());
            sortCode.setHTML(bankData.getSortCode());
            ibanCode.setHTML(bankData.getIbanCode());
            agentID.setHTML(bankData.getAgentID());
        }

        position.setHTML(profileItem.getPosition());
        location.setHTML(profileItem.getLocationName());
        if (!profileItem.getEss() && (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)
                || Utils.hasPermission(PermissionConstants.PM_SHOW_EMPLOYEE_ROLE_WIDGET))) {
            addField(CustomFormConstants.ACCOUNT_ROLES, roles, getTitle(wfmStrings.roles()));
        }
        roles.setHTML(roles(profileItem.getRoleList(), profileItem.getRoleId()));

        //Personal Identity Information
        passportNumber.setHTML(profileItem.getPassportNumber());
        passportIssBy.setHTML(profileItem.getPassportIssueItem() != null ? profileItem.getPassportIssueItem().getName() : "");
        passportIssueDate.setHTML(DateUtils.format(profileItem.getPassportIssueDate()));
        passportExpireDate.setHTML(DateUtils.format(profileItem.getPassportExpiryDate()));
        medicalInsuranceExDate.setHTML(DateUtils.format(profileItem.getMedicalInsuranceExpireDate()));
        visaNumber.setHTML(profileItem.getVisaNumber());
        if (profileItem.getPayrollSettings().containsKey(WPS_NUMBER)) {
            wpsNumber.setHTML(profileItem.getPayrollSettings().get(WPS_NUMBER) != null ? profileItem.getPayrollSettings().get(WPS_NUMBER) : "");
        }
        if (profileItem.getPaymentCategories() != null && profileItem.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject object : profileItem.getPaymentCategories()) {
                addItem(object, PayrollConstants.CATEGORY_PAYMENT);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_PAYMENT);
            }
        }

        if (profileItem.getDeductionCategories() != null && profileItem.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject object : profileItem.getDeductionCategories()) {
                addItem(object, PayrollConstants.CATEGORY_DEDUCTION);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_DEDUCTION);
            }
        }

        if (profileItem.getTaxes() != null && profileItem.getTaxes().size() > 0) {
            for (PaymentDeductionObject object : profileItem.getTaxes()) {
                addItem(object, PayrollConstants.CATEGORY_TAX);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_TAX);
            }
        }

        if (profileItem.getLoanCategories() != null && profileItem.getLoanCategories().size() > 0) {
            for (PaymentDeductionObject object : profileItem.getLoanCategories()) {
                addItem(object, PayrollConstants.CATEGORY_LOAN);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_LOAN);
            }
        }

        if (profileItem.getEmployerContributions() != null && profileItem.getEmployerContributions().size() > 0) {
            for (PaymentDeductionObject object : profileItem.getEmployerContributions()) {
                addItem(object, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            }
        }
        visaaIssueDate.setHTML(DateUtils.format(profileItem.getVisaIssueDate()));
        insuranceNumber.setHTML(profileItem.getInsuranceNumber());
        visaExpireDate.setHTML(DateUtils.format(profileItem.getVisaExpirationDate()));

        if (employeeID != null) {
            getCustomFieldUtil().fillCustomFieldsWithData(profileItem.getCustomFields(), true);
        }

        if (profileItem.getExperienceTableItems() != null && profileItem.getExperienceTableItems().length > 0) {
            getExperienceTableInfo(profileItem.getExperienceTableItems());
        }

        basicTotalSalary.setHTML(extendedNumberFormat.format(basicSalaryAmount.add(grandTotalPayment).subtract(grandTotalDeduction)));
        link.setBadgeCount(profileItem.getRelationItems() != null ? profileItem.getRelationItems().size() : 0);
    }

    private void getExperienceTableInfo(ExperienceTableItems[] experienceTableItems) {
        experienceTable.removeAllRows();
        for (ExperienceTableItems items : experienceTableItems) {
            experienceTable.addRow(widgets(items));
        }
    }

    private String roles(SelectItem[] items, Integer[] ids) {
        String role = "";

        if (items != null && items.length > 0) {
            if (ids != null) {
                for (Integer idS : ids) {
                    for (SelectItem selectItem : items) {
                        if (selectItem.getId().equals(idS)) {
                            if ("".equals(role)) {
                                role = role + selectItem.getName();
                            } else {
                                role = role + ", " + selectItem.getName();
                            }
                        }
                    }
                }
            }
        }
        return role;
    }

    private void fillFormWithDataContact(ContactListItem... contact) {
        if (contact != null && contact.length > 0) {
            contactListItem = contact[0];
        }
        if (firstname != null)
            firstname.setHTML(contactListItem.getFirstName());
        if (middleName != null) {
            middleName.setHTML(contactListItem.getMiddleName());
        }
        lastname.setHTML(contactListItem.getLastName());
        othername.setHTML(contactListItem.getOtherName());
        dob.setHTML(DateUtils.format(contactListItem.getBirthDate()));
        setAddressWidgets();
    }

    private void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(contactListItem, param);
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
        HTML valueF = new HTML(value);
        String phoneNumber = null;
        boolean mobile = false;
//        valueF.setStyleName("field");
        HTML relationS = null;
        switch (param) {
            case Constants.CONTACT_EMAILS:
                emailInf.clear();
                valueF.setHTML("<a href=\"javascript:\">" + value + "</a>");
                valueF.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + value + "/" + RelationItem.TYPE_EMPLOYEE + "/" + profileItem.getObjectId() + "/" + profileItem.getFirstName() + " " + profileItem.getLastName()));
                emailInf.add(valueF);
                break;
            case Constants.CONTACT_PHONES:
                phoneNumInf.clear();
                switch (relation) {
                    case Constants.G_HOME:
                        relationS = new HTML(getTitle(wfmStrings.home()));
                        phoneNumber = value;
                        break;
                    case Constants.G_WORK:
                        relationS = new HTML(getTitle(wfmStrings.contactwork()));
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
                    case Constants.G_FAX:
                        relationS = new HTML(getTitle(wfmStrings.fax()));
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
                    case Constants.G_WHATS_APP:
                        relationS = new HTML(getTitle(wfmStrings.whatsApp()));
                        phoneNumber = value;
                        break;
                    case Constants.G_EXTENSION:
                        relationS = new HTML(getTitle(wfmStrings.extension()));
                        phoneNumber = value;
                        break;
                    case Constants.G_OTHER:
                        relationS = new HTML(getTitle(wfmStrings.other()));
                        phoneNumber = value;
                        break;
                }
                f.getElement().setAttribute("style", "display: flex; align-items: center");
                PhonePopup popup = new PhonePopup(phoneNumber, profileItem, pmEmployeeView ? Utils.hasPermission(PM_SEND_SMS) : Utils.hasPermission(HRMS_SEND_SMS));
                Div phoneLink = popup.getPhoneWidget();
//                phoneLink.setStyleName("field");
                f.add(relationS);
                f.add(phoneLink);
                phoneNumInf.add(f);
                break;
            case Constants.CONTACT_IMADDRESSES:
                switch (relation) {
                    case Constants.G_GOOGLE_TALK:
                        relationS = new HTML(getTitle(Constants.G_GOOGLE_TALK_STR));
                        break;
                    case Constants.G_AIM:
                        relationS = new HTML(getTitle(Constants.G_AIM_STR));
                        break;
                    case Constants.G_YAHOO:
                        relationS = new HTML(getTitle(Constants.G_YAHOO_STR));
                        break;
                    case Constants.G_SKYPE:
                        relationS = new HTML(getTitle(Constants.G_SKYPE_STR));
                        break;
                    case Constants.G_QQ:
                        relationS = new HTML(getTitle(Constants.G_QQ_STR));
                        break;
                    case Constants.G_MSN:
                        relationS = new HTML(getTitle(Constants.G_MSN_STR));
                        break;
                    case Constants.G_ICQ:
                        relationS = new HTML(getTitle(Constants.G_ICQ_STR));
                        break;
                    case Constants.G_JABBER:
                        relationS = new HTML(getTitle(Constants.G_JABBER_STR));
                        break;
                }
                f.add(relationS);
                f.add(valueF);
                imsAddressInf.add(f);
                break;
            case Constants.CONTACT_WEBSITES:
                switch (relation) {
                    case Constants.G_HOME:
                        relationS = new HTML(getTitle(wfmStrings.home()));
                        break;
                    case Constants.G_WORK:
                        relationS = new HTML(getTitle(wfmStrings.contactwork()));
                        break;
                    case Constants.G_OTHER:
                        relationS = new HTML(getTitle(wfmStrings.other()));
                        break;
                    case Constants.G_HOME_PAGE:
                        relationS = new HTML(getTitle(Constants.G_HOME_PAGE_STR));
                        break;
                    case Constants.G_FTP:
                        relationS = new HTML(getTitle(Constants.G_FTP_STR));
                        break;
                    case Constants.G_BLOG:
                        relationS = new HTML(getTitle(Constants.G_BLOG_STR));
                        break;
                    case Constants.G_PROFILE:
                        relationS = new HTML(getTitle(Constants.G_PROFILE_STR));
                        break;
                }
                String href = value.contains("http://") ? value : "http://" + value;
                valueF.setHTML("<a target=\"_blank\" href=\"" + href + "\">" + value + "</a>");
                f.add(relationS);
                f.add(valueF);
                webSiteInf.add(f);
                break;
        }
    }

    private void setAddressWidgets() {
        parentAddressInf.removeAllRows();
        if (profileItem.getAddresses() != null && profileItem.getAddresses().size() > 0) {
            addressInf.clear();
            for (int i = 0; i < profileItem.getAddresses().size(); i++) {
                (profileItem.getAddresses().get(i).isLinkedAddress() ? parentAddressInf : addressInf).addWidgets(getAddressWidgets(profileItem.getAddresses().get(i)));
            }
        }
    }

    private WidgetsMap getAddressWidgets(Address addressData) {
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, true, (employeeID.toString()), true, false, filterParameter);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTable.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
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

    public void addItem(PaymentDeductionObject paymentDeduction, String type) {
        EditableTextBox category = new EditableTextBox();
        category.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            category.setText(paymentDeduction.getCategoryItem().getName());
        }
        EditableTextBox paymentType = new EditableTextBox();
        paymentType.setEnabled(false);
        if (paymentDeduction != null) {
            paymentType.setText(paymentDeduction.getPaymentType() != null ? paymentDeduction.getPaymentType().getTitle() : EPPaymentType.RECURRING.getTitle());
        }

        EditableTextBox payType = new EditableTextBox();
        payType.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getType() != null) {
            if (paymentDeduction.getType() == 0) {
                payType.setText(wfmStrings.fixed());
            } else if (paymentDeduction.getType() == 1) {
                payType.setText(wfmStrings.basicOfPersentage());
            } else {
                payType.setText(wfmStrings.ofBasicAllowances());
            }
        } else {
            payType.setText(wfmStrings.fixed());
        }

        EditableTextBox amount = new EditableTextBox();
        amount.setEnabled(false);
        if (paymentDeduction != null && (paymentDeduction.getType() == null || paymentDeduction.getType() == 0)) {
            amount.setText(Utils.getCalculationNumberFormat().format(paymentDeduction.getPaymentAmount()));
            if (PayrollConstants.CATEGORY_PAYMENT.equals(type)) {
                grandTotalPayment = grandTotalPayment.add(paymentDeduction.getPaymentAmount());
            } else if (PayrollConstants.CATEGORY_DEDUCTION.equals(type) || PayrollConstants.CATEGORY_TAX.equals(type)) {
                grandTotalDeduction = grandTotalDeduction.add(paymentDeduction.getPaymentAmount());
            }
        } else if (paymentDeduction != null && paymentDeduction.getType() == 1 && paymentDeduction.getPercentage() != null) {
            amount.setText(Utils.getNumberFormat().format(paymentDeduction.getPercentage()) + "%");
            if (PayrollConstants.CATEGORY_PAYMENT.equals(type)) {
                grandTotalPayment = grandTotalPayment.add((basicSalaryAmount.multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
            } else if (PayrollConstants.CATEGORY_DEDUCTION.equals(type) || PayrollConstants.CATEGORY_TAX.equals(type)) {
                grandTotalDeduction = grandTotalDeduction.add((basicSalaryAmount.multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
            }
        } else if (paymentDeduction != null && paymentDeduction.getType() == 2) {
            amount.setText(Utils.getNumberFormat().format(paymentDeduction.getPercentage()) + "%");
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
                        linkedTotal = linkedTotal.add((basicSalaryAmount.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                    } else {
                        linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                    }
                }
                grandTotalDeduction = grandTotalDeduction.add(((basicSalaryAmount.add(linkedTotal)).multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
            } else {
                grandTotalDeduction = grandTotalDeduction.add((basicSalaryAmount.multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
            }
        } else {
            amount.setText(Utils.getCalculationNumberFormat().format(BigDecimal.ZERO));
        }

        EditableTextBox startDate = new EditableTextBox();
        startDate.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getStarttDate() != null && paymentDeduction.getStarttDate().getDate() != null) {
            startDate.setText(DateUtils.format(paymentDeduction.getStarttDate()));
        }

        EditableTextBox totalAmount = new EditableTextBox();
        totalAmount.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getTotalAmount() != null) {
            totalAmount.setText(Utils.getCalculationNumberFormat().format(paymentDeduction.getTotalAmount()));
        }

        EditableTextBox remainingAmount = new EditableTextBox();
        remainingAmount.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getRemainingAmount() != null) {
            remainingAmount.setText(Utils.getCalculationNumberFormat().format(paymentDeduction.getRemainingAmount()));
        }

        EditableTable table = getTable(type);
        if (PayrollConstants.CATEGORY_LOAN.equals(type)) {
            table.addRow(new Widget[]{category, payType, amount, totalAmount, startDate, remainingAmount});
        } else {
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                table.addRow(new Widget[]{category, paymentType, payType, amount});
            } else {
                table.addRow(new Widget[]{category, payType, amount});
            }
        }
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

    private void setTelegramWidgets() {
        telegramInf.clear();
        if (contactListItem.getTelegramChats() != null && !contactListItem.getTelegramChats().isEmpty()) {
            for (SelectItem telegramChat : contactListItem.getTelegramChats()) {
                FlowPanel f = new FlowPanel();
                f.getElement().getStyle().setDisplay(Style.Display.FLEX);
                f.addStyleName("firstChildHasPadding mb-2");
                HTML relationS = new HTML(getTitle(telegramChat.getName()));
                HTML valueF = new HTML(telegramChat.getCode());
                f.add(relationS);
                f.add(valueF);
                telegramInf.add(f);
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TELEGRAM) != null) {
            addField(CustomFormConstants.TELEGRAM, telegramInf, getTitle(formPropertyMap.get(CustomFormConstants.TELEGRAM).isChanged() ? formPropertyMap.get(CustomFormConstants.TELEGRAM).getTitle() : wfmStrings.telegram()));
        } else {
            addField(TELEGRAM, telegramInf, wfmStrings.telegram());
        }
    }

    public String getPropertyCode() {
        return EMPLOYEE_PROFILE_VIEW;
    }
}
