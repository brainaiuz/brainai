package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.LookUpCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.ui.view.quickadd.EmployeeQuickAdd;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Br;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CONTACT;

/**
 * User: Admin
 * Date: 16.01.2008
 * Time: 15:58:44
 */

public class EmployeeListView extends BaseListView implements Constants, PermissionConstants {

    private static final NumberFormat salaryFormat = NumberFormat.getFormat(",##0.00");

    private String fromView;
    private ListingPanel<EmployeeListItem> employeeListPanel;
    private SimpleLink viewState;
    private HashMap<String, String[]> countryKey = null;
    private String selectedCountryName = null;
    HashMap<Integer, SelectItem[]> map = null;
    private Integer maxEmp;
    private Integer maxNoAccessEmp;
    private Integer maxEssUser;
    private EmployeeLookUpWithCode employeeLookUp;
    private CountryLookUp countryLookUp;
    private boolean editPermission = false;
    private boolean editOwnPermission;
    protected boolean birthDayPermission;
    protected boolean personalInfPermission;
    protected boolean employmentInfPermission;
    protected boolean editLocationPermission;
    private boolean employeeAddress;
    protected ContextMenu actions;
    protected HashSet<EmployeeListItem> selectedItems = new HashSet<>();
    private Integer departmentId;
    private Integer positionId;
    private Integer locationId;
    private int totalCount = 0;

    public EmployeeQuickAdd quickAddBox;

    public EmployeeListView(String fromView) {
        super((FROM_HRMS.equals(fromView) ? HRMS_EMPLOYEES_LIST : EMLOYEE_LIST));
        setDescription(property.getPlural(FROM_TRAINING_CENTER.equals(fromView) ? wfmStrings.instructors() : wfmStrings.employeesList()));
        this.fromView = fromView;
        if (((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE) && Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_NEW_EMPLOYEE) || Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) || Utils.isSettings())) {
            setAddNew(this::addDetailedView);
        } else if ((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_NEW_EMPLOYEE)) || Utils.isSettings()) {
            setAddNew(this::addQuickEmployee);
        } else if ((isFromPM() && Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) || Utils.isSettings() ||
                (isFromPAYROLL() && Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_ADD))) {
            setAddNew(this::addQuickEmployee);
        }
    }

    public EmployeeListView(Integer departmentId) {
        super(HRMS_EMPLOYEES_LIST);
        this.departmentId = departmentId;
        setDescription(property.getPlural(wfmStrings.employees()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
            setAddNew(this::addQuickEmployee);
        }
    }

    public EmployeeListView(Integer positionId, String fromPosition) {
        super(HRMS_EMPLOYEES_LIST);
        this.positionId = positionId;
        setDescription(property.getPlural(wfmStrings.employees()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
            setAddNew(this::addQuickEmployee);
        }
    }

    public EmployeeListView(Integer locationId, boolean fromLocation) {
        super(HRMS_EMPLOYEES_LIST);
        this.locationId = locationId;
        setDescription(property.getPlural(wfmStrings.employees()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
            setAddNew(this::addQuickEmployee);
        }
    }


    protected boolean isFromHRMS() {
        return FROM_HRMS.equals(fromView);
    }

    protected boolean isFromTC() {
        return FROM_TRAINING_CENTER.equals(fromView);
    }

    protected boolean isFromPAYROLL() {
        return FROM_PAYROLL.equals(fromView);
    }

    protected boolean isFromPRICING() {
        return FROM_PRICING.equals(fromView);
    }

    protected boolean isFromPM() {
        return FROM_PM.equals(fromView);
    }

    protected void getEmployeesMaxCount() {
        ReportService.App.get().getEmployeesMaxCount(null, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void success(Integer[] result) {
                maxEmp = result[ACTIVE];
                maxNoAccessEmp = result[NO_ACCESS];
                maxEssUser = result[ESS];
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "employee employee-list";
    }

    @Override
    public FlowPanel getHelpContainer() {
        if (helpPanel == null) {
            if (isFromHRMS()) {
                helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.HRMS_CONTEXT, PermissionConstants.HRMS_EMPLOYEE_LIST);
            } else if (isFromPM()) {
                helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_EMPLOYEE_LIST);
            }
        }
        return helpPanel;
    }

    /**
     * <i>... Employee Listing row action column widget ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create date {20:49 15/07/2011} ...</i>
     *
     * @param item - employeeListItem RPC
     * @return - action links
     */
    protected Anchor getEmployeeActions(final EmployeeListItem item) {
        if (isFromHRMS()) {
            return getActionsForHRMS(item);
        } else {
            return getActions(item);
        }
    }

    @Override
    protected Widget onInitialize() {

        editPermission = isFromPM() ? Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_EDIT) : Utils.hasPermission(PermissionConstants.HRMS_EDIT_PROFILE);
        editOwnPermission = isFromPM() ? Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_EDIT_OWN_PROFILE) : Utils.hasPermission(PermissionConstants.HRMS_EDIT_OWN_PROFILE);
        birthDayPermission = Utils.hasPermission(isFromPM() ? PermissionConstants.PM_SHOW_EMPLOYEE_BIRTH_DAY : PermissionConstants.HRMS_SHOW_EMPLOYEE_BIRTH_DAY);
        personalInfPermission = Utils.hasPermission(PermissionConstants.SHOW_EMPLOYEE_PERSONAL_INFORMATION);
        employmentInfPermission = Utils.hasPermission(isFromPM() ? PermissionConstants.PM_SHOW_EMPLOYMENT_INFORMATION :
                isFromPAYROLL() ? PermissionConstants.PAYROLL_EMPLOYEE_BASIC_SALARY : PermissionConstants.HRMS_SHOW_EMPLOYMENT_INFORMATION);
        editLocationPermission = Utils.hasPermission(PermissionConstants.HRMS_EDIT_LOCATION);
        employeeAddress = Utils.hasPermission(isFromPM() ? PermissionConstants.PM_SHOW_EMPLOYEE_ADDRESS : PermissionConstants.HRMS_SHOW_EMPLOYEE_ADDRESS);

        if (isFromPAYROLL()) {
            employeeListPanel = new GuideListingPanel(ListPanelType.PayrollEmployeeListPanel, getColumnConfig(), getRequestDriver(), getPanelDesign());
        } else if (isFromPRICING()) {
            employeeListPanel = new GuideListingPanel(ListPanelType.PricingEmployeeListPanel, getColumnConfig(), getRequestDriver(), getPanelDesign());
        } else if (isFromHRMS()) {
            employeeListPanel = new GuideListingPanel(ListPanelType.HrmsEmployeeListPanel, getColumnConfig(), getRequestDriver(), getPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
            employeeListPanel.addSelectionRowHandler(selectedRows -> {
                selectedItems = selectedRows;
                for (Object selectedRow : selectedRows) {
                    EmployeeListItem selectedRow1 = (EmployeeListItem) selectedRow;

                }
            });
        } else {
            employeeListPanel = new GuideListingPanel((isFromHRMS() ? ListPanelType.HrmsEmployeeListPanel : isFromTC() ? ListPanelType.InstructorListPanel : ListPanelType.EmployeeListPanel), getColumnConfig(), getRequestDriver(), getPanelDesign());
        }

        employeeListPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveEmployeeEditCellValue((EmployeeListItem) rowValue, columnCodeName));

        if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_TO_PDF)) {
            employeeListPanel.setExcelListener(event -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadEmployeeListExcel";
                ListingFilterParameter filterParametrs = employeeListPanel.getFilterParametrs();
                filterParametrs.setPropertyCode(getPropertyCode());
                employeeListPanel.callListExcel(excelURL, filterParametrs);
            });

            employeeListPanel.setPDFListener(event -> {
                String pdfURL = CommandConstants.PDF_URL + "/employeeListPDFHandler";
                ListingFilterParameter filterParametrs = employeeListPanel.getFilterParametrs();
                filterParametrs.setPropertyCode(getPropertyCode());
                employeeListPanel.callListPDF(pdfURL, filterParametrs);
            });
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, EmployeeListView.this, (sender, args) -> employeeListPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_DELETE, EmployeeListView.this, (sender, args) -> employeeListPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_LIST_EDIT_CELL, EmployeeListView.this, (sender, args) -> employeeListPanel.reloadPage());

        if (isFromHRMS()) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_PROFILE_UPDATE, EmployeeListView.this, (sender, args) -> employeeListPanel.reloadPage());
        }
        add(employeeListPanel);

        return null;
    }

    private Anchor getActions(final EmployeeListItem item) {
        if (isFromTC()) {
            return getActionsForTC(item);
        } else {
            int actionItemCount = 0;
            MenuBar menuBar = new MenuBar(true);
            menuBar.setAutoOpen(true);
            String employeeName = item.getFirstName() + " " + item.getLastName();
            //employee summary
            if (!isFromPRICING() && Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_SUMMARY)) {
                MenuPopItem employeeSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-small");
                employeeSummary.ensureDebugId("summary");
                employeeSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName()));
                actionItemCount++;
                menuBar.addItem(employeeSummary);
            }
            //edit employee

            if (!isFromPRICING() && (editPermission || (Utils.getUserID().equals(item.getObjectID()) && editOwnPermission))) {
                final MenuPopItem employeeEdit = new MenuPopItem(wfmStrings.edit(), "icon-client-edit-small");
                employeeEdit.ensureDebugId("edit");
                employeeEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|edit/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName()));
                actionItemCount++;
                menuBar.addItem(employeeEdit);
            }

            //employee note
        /*if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_NOTE)) {
            MenuPopItem employeeNotes = new MenuPopItem(wfmStrings.notes(), "icon-note");
            employeeNotes.ensureDebugId("notes");
            employeeNotes.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|employeeNotes/" + item.getObjectID()));
            actionItemCount++;
            menuBar.addItem(employeeNotes);
        }*/

            //employee activate/deactivate

            if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ACTIVATE_DEACTIVATE) || Utils.hasPermission(PermissionConstants.HRMS_ACTIVATE_DEACTIVATE)) {
                if (EMPLOYEE_STATUS_INACTIVE.equals(item.getStatusCode()) || EMPLOYEE_STATUS_PENDING.equals(item.getStatusCode()) ) {
                    MenuPopItem resendAcLinkItem = new MenuPopItem(wfmStrings.resendActivationLink(), "icon-employee-resend-profile");
                    resendAcLinkItem.ensureDebugId("resendActivationLink");
                    resendAcLinkItem.setCommand(() -> EmployeeService.App.get().resendActivationLink(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.couldntSendActivationLink(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            Info.show(wfmStrings.activationLinkHasBeenSent(), Info.Type.INFO);
                            employeeListPanel.reloadPage();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(resendAcLinkItem);

                    //employee activate
                    MenuPopItem activateItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile");
                    activateItem.setEnabled(!Utils.getUserID().equals(item.getObjectID()));
                    activateItem.setCommand(() -> EmployeeService.App.get().activateOrDisactivateEmployee(item.getObjectID(), true, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            Info.show(wfmStrings.activated(), Info.Type.INFO);
                            employeeListPanel.reloadPage();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(activateItem);
                } else if (EMPLOYEE_STATUS_ACTIVE.equals(item.getStatusCode())) {

                    //disActivate
                    MenuPopItem disActivateItem = new MenuPopItem(wfmStrings.deactivate(), "icon-employee-disactivate-profile");
                    disActivateItem.ensureDebugId("deactivate");
                    disActivateItem.setEnabled(!Utils.getUserID().equals(item.getObjectID()));
                    disActivateItem.setCommand(() -> EmployeeService.App.get().activateOrDisactivateEmployee(item.getObjectID(), false, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            Info.show(wfmStrings.deactivated(), Info.Type.INFO);
                            employeeListPanel.reloadPage();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(disActivateItem);
                } else if (EMPLOYEE_STATUS_NO_ACCCESS.equals(item.getStatusCode())) {
                    //activate
                    MenuPopItem activateItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile");
                    activateItem.setEnabled(!Utils.getUserID().toString().equals(String.valueOf(item.getObjectID())));
                    activateItem.setCommand(() -> EmployeeService.App.get().grantAccessToEmployee(item.getObjectID(), true, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean activated) {
                            if (activated) {
                                Info.show(wfmStrings.activated(), Info.Type.INFO);
                                employeeListPanel.reloadPage();
                            } else {
                                Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                            }
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(activateItem);
                } else if (EMPLOYEE_STATUS_RESIGNED.equals(item.getStatusCode())) {
                    //activate
                    MenuPopItem activateItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile");
                    activateItem.setEnabled(!Utils.getUserID().toString().equals(String.valueOf(item.getObjectID())));
                    activateItem.setCommand(() -> EmployeeService.App.get().grantAccessToEmployee(item.getObjectID(), true, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean activated) {
                            if (activated) {
                                Info.show(wfmStrings.activated(), Info.Type.INFO);
                                employeeListPanel.reloadPage();
                            } else {
                                Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                            }
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(activateItem);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_TERMINATE_EMPLOYMENT) || Utils.hasPermission(PermissionConstants.PM_TERMINATE_EMPLOYMENT)) {
                    if (!EMPLOYEE_STATUS_RESIGNED.equals(item.getStatusCode())) {
                        //TERMINATE EMPLOYMENT
                        MenuPopItem terminateEmploymentItem = new MenuPopItem(wfmStrings.resign(), "icon-employee-disactivate-profile");
                        terminateEmploymentItem.ensureDebugId("resign");
                        final EmployeeRemovePopup terminateEmployement = new EmployeeRemovePopup(fromView, item.getObjectID().toString(), employeeName, employeeListPanel, false);
                        terminateEmploymentItem.setEnabled(!Utils.getUserID().toString().equals(String.valueOf(item.getObjectID())));
                        terminateEmploymentItem.setCommand(() -> EmployeeService.App.get().checkEmployeeForApprovers(item.getObjectID(), new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                terminateEmployement.selectionListener();
                            }

                            @Override
                            public void onSuccess(Integer result) {
                                if (result > 0) {
                                    switch (result) {
                                        case 1:
                                            Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                            break;
                                        case 2:
                                            Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                            break;
                                        case 3:
                                            Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                            Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                            break;
                                    }
                                } else {
                                    terminateEmployement.selectionListener();
                                }
                            }
                        }));
                        actionItemCount++;
                        menuBar.addItem(terminateEmploymentItem);
                    }
                }
            }
            if (isFromPRICING() && (item.getRoleCode() == null || !item.getRoleCode().contains(ESS_USER_CODE)) && !Utils.getUserID().toString().equals(String.valueOf(item.getObjectID()))) {
                MenuPopItem statusMenuPopItem = new MenuPopItem(wfmStrings.essUser(), "icon-leads");
                statusMenuPopItem.ensureDebugId(wfmStrings.essUser());
                statusMenuPopItem.setCommand(() -> EmployeeService.App.get().changeToESSEmployee(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Boolean result) {
                        if (result) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.employee()), Info.Type.INFO);
                            employeeListPanel.reloadPage();
                        } else {
                            Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                        }
                    }
                }));
                actionItemCount++;
                menuBar.addItem(statusMenuPopItem);
            }
            //remove
            if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_REMOVE) && !Utils.getUserID().toString().equals(String.valueOf(item.getObjectID()))) {
                final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.ensureDebugId("delete");
                final EmployeeRemovePopup employeeDelete = new EmployeeRemovePopup(fromView, item.getObjectID().toString(), employeeName, employeeListPanel, true);
                removeItem.setCommand(() -> EmployeeService.App.get().checkEmployeeForApprovers(item.getObjectID(), new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        employeeDelete.selectionListener();
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result > 0) {
                            switch (result) {
                                case 1:
                                    Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                    break;
                                case 2:
                                    Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                    break;
                                case 3:
                                    Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                    Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                    break;
                            }
                        } else {
                            employeeDelete.selectionListener();
                        }
                    }
                }));
                actionItemCount++;
                menuBar.addItem(removeItem);
            }

            if (!isFromPRICING()) {
                boolean hasPermission = isFromPM() ? (Utils.hasPermission(PM_SEND_EMAIL) || Utils.hasPermission(PM_SEND_SMS))
                        : (Utils.hasPermission(PAYROLL_SEND_EMAIL) || Utils.hasPermission(PAYROLL_SEND_SMS));
                if (hasPermission) {
                    MenuBar bar = new MenuBar(true);
                    bar.setAutoOpen(true);

                    if (Utils.hasPermission(isFromPM() ? PM_SEND_EMAIL : PAYROLL_SEND_EMAIL) && !Utils.isNullOrEmpty(item.getEmail())) {
                        final MenuPopItem emailItem = new MenuPopItem(wfmStrings.email(), "icon-send-message");
                        emailItem.ensureDebugId(wfmStrings.sendEmail());
                        emailItem.setCommand(() -> {
                            emailItem.closeAll(menuBar);
                            goTo("emailcompose|add/add/" + item.getEmail() + "/" + RelationItem.TYPE_EMPLOYEE + "/" + item.getObjectID() + "/" + item.getFullName());
                        });
                        bar.addItem(emailItem);
                    }

                    if (Utils.hasPermission(isFromPM() ? PM_SEND_SMS : PAYROLL_SEND_SMS)) {
                        final MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
                        addSms.ensureDebugId(wfmStrings.sendSms());
                        addSms.setCommand(() -> {
                            addSms.closeAll(menuBar);
                            new ActivityQuickAddForm(Appointment.SMS, item, RelationItem.newEventRelation(RelationItem.TYPE_EMPLOYEE, item.getObjectID(), item.getFullName()));
                        });
                        bar.addItem(addSms);
                    }

                    actionItemCount++;
                    menuBar.addItem(new MenuPopItem(wfmStrings.send(), "icon-send", bar));
                }
            }

            final ToolItem toolItem = new ToolItem(actionItemCount);
            toolItem.setWidget(menuBar);
            return toolItem.getAction();
        }
    }

    private Anchor getActionsForHRMS(final EmployeeListItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        menuBar.setAutoOpen(true);
        String employeeName = item.getFirstName() + " " + item.getLastName();
        if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE) || Utils.isSettings()) {
            //employee profile summary
            MenuPopItem employeeSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-fulldetails-profile");
            employeeSummary.ensureDebugId("summary");
            employeeSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName()));
            actionItemCount++;
            menuBar.addItem(employeeSummary);
        }
        //edit employee profile
        if (editPermission || Utils.isSettings() || (Utils.getUserID().equals(item.getObjectID()) && editOwnPermission)) {
            final MenuPopItem employeeEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
            employeeEdit.ensureDebugId("edit");
            employeeEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("editemployeeProfile|editprofile/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName()));
            actionItemCount++;
            menuBar.addItem(employeeEdit);
        }
        //pdf
        if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_TO_PDF) || Utils.getUserID().equals(item.getObjectID())) {
            MenuPopItem employeePDF = new MenuPopItem(wfmStrings.pdf(), "icon-document-pdf");
            employeePDF.setCommand(() -> new PDFTemplateSelector(AccountingConstants.EMPLOYEE_PROFILE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    generatePDF(id, item.getObjectID());
                }
            }));
            actionItemCount++;
            menuBar.addItem(employeePDF);
        }
        //employee activate/disactivate

        if (Utils.hasPermission(PermissionConstants.HRMS_ACTIVATE_DEACTIVATE) || Utils.isSettings()) {
            //INACTIVE - RESEND/ACTIVATE/REVOKE ACCESS/FIRE,      PENDING - RESEND/FIRE,       ACTIVE - DEACTIVATE/REVOKE ACCESS/FIRE,      NO ACCESS - GRAND ACCESS/FIRE
            if (EMPLOYEE_STATUS_INACTIVE.equals(item.getStatusCode()) || EMPLOYEE_STATUS_PENDING.equals(item.getStatusCode())) {
                //resend activation link
                MenuPopItem resendAcLinkItem = new MenuPopItem(wfmStrings.resendActivationLink(), "icon-employee-resend-profile");
                resendAcLinkItem.ensureDebugId("resendActivationLink");
                resendAcLinkItem.setCommand(() -> EmployeeService.App.get().resendActivationLink(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.couldntSendActivationLink(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(wfmStrings.activationLinkHasBeenSent(), Info.Type.INFO);
                        employeeListPanel.reloadPage();
                    }
                }));
                actionItemCount++;
                menuBar.addItem(resendAcLinkItem);


                //activate
                MenuPopItem activateItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile");
                activateItem.ensureDebugId("activate");
                activateItem.setEnabled(!Utils.getUserID().equals(item.getObjectID()));
                activateItem.setCommand(() -> EmployeeService.App.get().activateOrDisactivateEmployee(item.getObjectID(), true, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.errorOccuredwhileActivating(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(wfmStrings.accountHasBeenSuccessfullyActivated(), Info.Type.INFO);
                        employeeListPanel.reloadPage();
                    }
                }));
                actionItemCount++;
                menuBar.addItem(activateItem);


                //revoke access
            } else if (EMPLOYEE_STATUS_ACTIVE.equals(item.getStatusCode()) && !Utils.getUserID().equals(item.getObjectID())) {

                //disActivate
                MenuPopItem disActivateItem = new MenuPopItem(wfmStrings.deactivate(), "icon-employee-disactivate-profile");
                disActivateItem.ensureDebugId("deactivate");
                disActivateItem.setCommand(() -> EmployeeService.App.get().activateOrDisactivateEmployee(item.getObjectID(), false, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(wfmStrings.deactivated(), Info.Type.INFO);
                        employeeListPanel.reloadPage();
                    }
                }));
                actionItemCount++;
                menuBar.addItem(disActivateItem);
            } else if (EMPLOYEE_STATUS_NO_ACCCESS.equals(item.getStatusCode())) {

                //activate
                MenuPopItem activateItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile");
                activateItem.ensureDebugId("activate");
                activateItem.setEnabled(!Utils.getUserID().toString().equals(String.valueOf(item.getObjectID())));
                activateItem.setCommand(() -> EmployeeService.App.get().grantAccessToEmployee(item.getObjectID(), true, new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Boolean activated) {
                        if (activated) {
                            Info.show(wfmStrings.activated(), Info.Type.INFO);
                            employeeListPanel.reloadPage();
                        } else {
                            Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                        }
                    }
                }));
                actionItemCount++;
                menuBar.addItem(activateItem);

            } else if (EMPLOYEE_STATUS_RESIGNED.equals(item.getStatusCode())) {

                //activate
                MenuPopItem activateItem = new MenuPopItem(wfmStrings.activate(), "icon-employee-activate-profile");
                activateItem.ensureDebugId("activate");
                activateItem.setEnabled(!Utils.getUserID().toString().equals(String.valueOf(item.getObjectID())));
                activateItem.setCommand(() -> EmployeeService.App.get().grantAccessToEmployeeWithEss(item.getObjectID(), true, ESS_USER_CODE.equals(item.getRoleCode()), new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.couldNotActivate(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Boolean activated) {
                        if (activated) {
                            Info.show(wfmStrings.activated(), Info.Type.INFO);
                            employeeListPanel.reloadPage();
                        } else {
                            Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                        }
                    }
                }));
                actionItemCount++;
                menuBar.addItem(activateItem);
            }
        }

        //TERMINATE EMPLOYMENT
        if ((!EMPLOYEE_STATUS_RESIGNED.equals(item.getStatusCode())) && (Utils.hasPermission(PermissionConstants.PM_TERMINATE_EMPLOYMENT) || Utils.hasPermission(PermissionConstants.HRMS_TERMINATE_EMPLOYMENT) || Utils.isSettings())) {

            if (!Utils.getUserID().toString().equals(String.valueOf(item.getObjectID()))) {
                MenuPopItem terminateEmploymentItem = new MenuPopItem(wfmStrings.resign(), "icon-employee-disactivate-profile");
                terminateEmploymentItem.ensureDebugId("resign");
                final EmployeeRemovePopup terminateEmployement = new EmployeeRemovePopup(fromView, item.getObjectID().toString(), employeeName, employeeListPanel, false);
                terminateEmploymentItem.setCommand(() -> EmployeeService.App.get().checkEmployeeForApprovers(item.getObjectID(), new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        terminateEmployement.selectionListener();
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result > 0) {
                            switch (result) {
                                case 1:
                                    Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                    break;
                                case 2:
                                    Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                    break;
                                case 3:
                                    Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                    Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                    break;
                            }
                        } else {
                            terminateEmployement.selectionListener();
                        }
                    }
                }));
                actionItemCount++;
                menuBar.addItem(terminateEmploymentItem);
            }

        }
        //remove
        if ((Utils.hasPermission(HRMS_EMPLOYEE_REMOVE) || Utils.isSettings()) && !Utils.getUserID().toString().equals(String.valueOf(item.getObjectID()))) {
            final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
            removeItem.ensureDebugId("delete");
            final EmployeeRemovePopup employeeDelete = new EmployeeRemovePopup(fromView, item.getObjectID().toString(), employeeName, employeeListPanel, true);
            removeItem.setCommand(() -> EmployeeService.App.get().checkEmployeeForApprovers(item.getObjectID(), new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    employeeDelete.selectionListener();
                }

                @Override
                public void onSuccess(Integer result) {
                    if (result > 0) {
                        switch (result) {
                            case 1:
                                Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                break;
                            case 2:
                                Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                break;
                            case 3:
                                Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                                Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                break;
                        }
                    } else {
                        employeeDelete.selectionListener();
                    }
                }
            }));
            actionItemCount++;
            menuBar.addItem(removeItem);
        }

        if (Utils.hasPermission(HRMS_SEND_EMAIL) || Utils.hasPermission(HRMS_SEND_SMS)) {
            MenuBar bar = new MenuBar(true);
            bar.setAutoOpen(true);

            if (Utils.hasPermission(HRMS_SEND_EMAIL) && !Utils.isNullOrEmpty(item.getEmail())) {
                MenuPopItem emailItem = new MenuPopItem(wfmStrings.email(), "icon-send-message");
                emailItem.ensureDebugId(wfmStrings.sendEmail());
                emailItem.setCommand(() -> {
                    emailItem.closeAll(menuBar);
                    goTo("emailcompose|add/add/" + item.getEmail() + "/" + RelationItem.TYPE_EMPLOYEE + "/" + item.getObjectID() + "/" + item.getFullName());
                });
                bar.addItem(emailItem);
            }

            if (Utils.hasPermission(HRMS_SEND_SMS)) {
                MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
                addSms.ensureDebugId(wfmStrings.sendSms());
                addSms.setCommand(() -> {
                    addSms.closeAll(menuBar);
                    new ActivityQuickAddForm(Appointment.SMS, item, RelationItem.newEventRelation(RelationItem.TYPE_EMPLOYEE, item.getObjectID(), item.getFullName()));
                });
                bar.addItem(addSms);
            }

            if (Utils.hasPermission(HRMS_FULL_ADD_NEW_ACTIVITY_EVENT) || Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT)) {
                MenuPopItem addEvent = new MenuPopItem(wfmStrings.event(), "icon-event-interview");
                addEvent.ensureDebugId(wfmStrings.event());
                addEvent.setCommand(() -> {
                    addEvent.closeAll(menuBar);
                    if (Utils.hasPermission(HRMS_FULL_ADD_NEW_ACTIVITY_EVENT)) {
                        goTo("event|add/add/" + Appointment.EVENT + "/" + item.getObjectID() + "/" + RelationItem.TYPE_EMPLOYEE);
                    } else if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT)) {
                        new ActivityQuickAddForm(Appointment.EVENT, item, RelationItem.newEventRelation(RelationItem.TYPE_EMPLOYEE, item.getObjectID(), item.getFullName()));
                    }
                });
                bar.addItem(addEvent);
            }

            menuBar.addItem(new MenuPopItem(wfmStrings.send(), "icon-send", bar));
        }

        final ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }


    private Anchor getActionsForTC(final EmployeeListItem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        String employeeName = item.getFirstName() + " " + item.getLastName();
        //Instructor summary
        if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_SUMMARY)) {
            MenuPopItem employeeSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-small");
            employeeSummary.ensureDebugId("summary");
            employeeSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("tcInstructor|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName()));
            actionItemCount++;
            menuBar.addItem(employeeSummary);
        }
        //edit Instructor

        if (editPermission || (Utils.getUserID().equals(item.getObjectID()))) {
            final MenuPopItem employeeEdit = new MenuPopItem(wfmStrings.edit(), "icon-client-edit-small");
            employeeEdit.ensureDebugId("edit");
            employeeEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("tcInstructor|editInstructor/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName()));
            actionItemCount++;
            menuBar.addItem(employeeEdit);
        }

        //remove Instructor
        final MenuPopItem removeInstructor = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
        removeInstructor.ensureDebugId("delete");
        final EmployeeRemovePopup instructorDelete = new EmployeeRemovePopup(fromView, item.getObjectID().toString(), employeeName, employeeListPanel, true);
        removeInstructor.setCommand(() -> EmployeeService.App.get().checkEmployeeForApprovers(item.getObjectID(), new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                instructorDelete.selectionListener();
            }

            @Override
            public void onSuccess(Integer result) {
                if (result > 0) {
                    switch (result) {
                        case 1:
                            Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                            break;
                        case 2:
                            Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                            break;
                        case 3:
                            Info.show(wfmMessages.employeeParticipatedInApprovalProcess(item.getFullName(), item.getFullName()), Info.Type.WARNING);
                            Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                            break;
                    }
                } else {
                    instructorDelete.selectionListener();
                }
            }
        }));
        actionItemCount++;
        menuBar.addItem(removeInstructor);
//        }

        final ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }


    protected ColumnDefinitionConfig[] getColumnConfig() {

        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig columnConfig;

        if (isFromHRMS() || isFromPRICING() || isFromTC() || isFromPM()) {
            //action
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
                @Override
                public Anchor getCellValue(EmployeeListItem item) {
                    return getEmployeeActions(item);
                }
            };
            columnConfig.setColumnSortable(false);
            columnConfig.setMinimumColumnWidth(100);
            columnConfig.setMaximumColumnWidth(100);
            columns.add(columnConfig);
        }

        if (employmentInfPermission) {
            if (isFromPM() || isFromHRMS() || isFromPAYROLL() || isFromPRICING()) {
                //employee code
                columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SimpleLink>(wfmStrings.employeeCode(), EmployeeListItem.EMPLOYEE_NUMBER, 110) {
                    @Override
                    public SimpleLink getCellValue(EmployeeListItem item) {
                        if (isFromPAYROLL()) {
                            if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector() && !Utils.hasPermission(PAYROLL_EMPLOYEE_EDIT)) {
                                return getLink(item.getEmployeeNumber() != null ? item.getEmployeeNumber() : "N/A", "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                            } else {
                                return getLink(item.getEmployeeNumber() != null ? item.getEmployeeNumber() : "N/A", "starter|summary/" + item.getObjectID() + "/fromEmployeeList/view", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                            }
                        } else if (isFromPM()) {
                            return getLink(item.getEmployeeNumber() != null ? item.getEmployeeNumber() : "N/A", "employee|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                        } else if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
                            return getLink(item.getEmployeeNumber() != null ? item.getEmployeeNumber() : "N/A", "employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                        } else {
                            return getLink(item.getEmployeeNumber() != null ? item.getEmployeeNumber() : "N/A", null);
                        }
                    }
                };
                columnConfig.setMinimumColumnWidth(85);
                columnConfig.setColumnSortable(true);
                columns.add(columnConfig);
            }
        }
        //first name
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SimpleLink>(wfmStrings.firstName(), EmployeeListItem.FIRST_NAME, 110) {
            @Override
            public SimpleLink getCellValue(EmployeeListItem item) {
                if (isFromHRMS()) {
                    if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
                        return getLink(item.getFirstName(), "employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    }
                    return getLink(item.getFirstName(), null);
                } else if (isFromPAYROLL()) {
                    if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector() && !Utils.hasPermission(PAYROLL_EMPLOYEE_EDIT)) {
                        return getLink(item.getFirstName(), "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    } else {
                        return getLink(item.getFirstName(), "starter|summary/" + item.getObjectID() + "/fromEmployeeList/view", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    }
                } else if (isFromPRICING()) {
                    return getLink(item.getFirstName(), "", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else if (isFromTC()) {
                    return getLink(item.getFirstName(), "tcInstructor|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else {
                    return getLink(item.getFirstName(), "employee|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                }
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //last name
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SimpleLink>(wfmStrings.lastName(), EmployeeListItem.LAST_NAME, 110) {
            @Override
            public SimpleLink getCellValue(EmployeeListItem item) {
                if (isFromHRMS()) {
                    if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
                        return getLink(item.getLastName(), "employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    }
                    return getLink(item.getLastName(), null);
                } else if (isFromPAYROLL()) {
                    if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector() && !Utils.hasPermission(PAYROLL_EMPLOYEE_EDIT)) {
                        return getLink(item.getLastName(), "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    } else {
                        return getLink(item.getLastName(), "starter|summary/" + item.getObjectID() + "/fromEmployeeList/view", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    }
                } else if (isFromPRICING()) {
                    return getLink(item.getLastName(), "", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else if (isFromTC()) {
                    return getLink(item.getLastName(), "tcInstructor|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else {
                    return getLink(item.getLastName(), "employee|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                }
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //phone number
//        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TWILIO)) {
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, Div>(wfmStrings.phone(), EmployeeListItem.PHONE_NUMBER, 140) {
            @Override
            public Div getCellValue(final EmployeeListItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPhoneNumber(), RelationItem.TYPE_CONTACT, rowValue.getObjectID(), rowValue.getContactName(), false, true, null, TYPE_CONTACT, rowValue, rowValue.getContactID());
                return phonePopup.getPhoneWidget();
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //email
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, HTML>(wfmStrings.email(), EmployeeListItem.EMAIL, 200) {
            @Override
            public HTML getCellValue(EmployeeListItem item) {
                return getEmailLink(item);
            }

            @Override
            public void setCellValue(EmployeeListItem rowValue, HTML cellValue) {
                rowValue.setEmail(cellValue.getText());
                saveCellValue(rowValue);
            }
        };
        columnConfig.setMinimumColumnWidth(130);
        columns.add(columnConfig);

        //employee role
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.role(), EmployeeListItem.ROLE, 80) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getRole();
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setColumnSortable(true);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //employee status
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.status(), EmployeeListItem.STATUS, 110) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getStatus();
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //employee position
        if (employmentInfPermission) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.position(), EmployeeListItem.POSITION, 90) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getPositionId() != null ? new SelectItem(item.getPositionId(), item.getPosition()) : null;
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setPositionId(cellValue.getId());
                        rowValue.setPosition(cellValue.getName());
                    } else {
                        rowValue.setPositionId(null);
                        rowValue.setPosition(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setShow(false);
            columnConfig.setMinimumColumnWidth(70);
            columns.add(columnConfig);
        }

        //last update time
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.modifiedDate(), EmployeeListItem.LAST_UPDATE, 80) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                if (LocaleInfo.getCurrentLocale().getLocaleName().equals("uz")) {
                    return DateUtils.convertToUzbDateFormat(item.getLastUpdate());
                } else {
                    return item.getLastUpdate();
                }
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //employee location
        if (employmentInfPermission) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), EmployeeListItem.LOCATION, 80) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getLocation() != null && item.getLocationId() != null ? new SelectItem(item.getLocationId(), item.getLocation()) : null;
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setLocationId(cellValue.getId());
                        rowValue.setLocation(cellValue.getName());
                    } else {
                        rowValue.setLocationId(null);
                        rowValue.setLocation(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        //gender
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.gender(), EmployeeListItem.GENDER_NAME, 150) {
            @Override
            public SelectItem getCellValue(EmployeeListItem item) {
                Integer genderId = item.getGenderName() != null ? (item.getGenderName().equals("Male") ? 0 : 1) : null;
                return genderId != null ? new SelectItem(genderId, genderId == 0 ? wfmStrings.male() : wfmStrings.female()) : null;
            }

            @Override
            public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
//                rowValue.setGenderName(cellValue.getName());
                saveCellValue(rowValue);
            }
        };
        columnConfig.setMinimumColumnWidth(130);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //middle name
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.middleName(), EmployeeListItem.MIDDLE_NAME, 80) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getMiddleName();
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //STREET
        if (employeeAddress) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.streetAddress1(), EmployeeListItem.STREET, 100) {
                @Override
                public String getCellValue(EmployeeListItem rowValue) {
                    Address addressItems = rowValue.getPrimaryAddress();
                    return addressItems != null && addressItems.getAddress() != null ? addressItems.getAddress() : "N/A";
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    if (rowValue.getPrimaryAddress() == null) {
                        rowValue.setPrimaryAddress(new Address());
                    }
                    rowValue.getPrimaryAddress().setAddress(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(100);
            columnConfig.setShow(false);
            columns.add(columnConfig);
            //STREET2
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.streetAddress2(), EmployeeListItem.STREET2, 100) {
                @Override
                public String getCellValue(EmployeeListItem rowValue) {
                    Address addressItems = rowValue.getPrimaryAddress();
                    return addressItems != null && addressItems.getAddressb() != null ? addressItems.getAddressb() : "N/A";
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    if (rowValue.getPrimaryAddress() == null) {
                        rowValue.setPrimaryAddress(new Address());
                    }
                    rowValue.getPrimaryAddress().setAddressb(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(100);
            columnConfig.setShow(false);
            columns.add(columnConfig);
            //CITY
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.city(), EmployeeListItem.CITY, 100) {
                @Override
                public String getCellValue(EmployeeListItem rowValue) {
                    Address addressItems = rowValue.getPrimaryAddress();
                    return addressItems != null && addressItems.getCity() != null ? addressItems.getCity() : "N/A";
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    if (rowValue.getPrimaryAddress() == null) {
                        rowValue.setPrimaryAddress(new Address());
                    }
                    rowValue.getPrimaryAddress().setCity(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(100);
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //Country
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.country(), EmployeeListItem.COUNTRY, 80) {
                @Override
                public SelectItem getCellValue(EmployeeListItem rowValue) {
                    rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                    Address addressItems = rowValue.getPrimaryAddress();
                    return addressItems.getCountryId() != null ? new SelectItem(addressItems.getCountryId(), addressItems.getCountry()) : null;
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem value) {
                    rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                    if (SelectItem.isDifferent(value, new SelectItem(rowValue.getPrimaryAddress().getCountryId(), rowValue.getPrimaryAddress().getCountry()))) {
                        rowValue.getPrimaryAddress().setCountry(value != null ? value.getName() : null);
                        rowValue.getPrimaryAddress().setCountryId(value != null ? value.getId() : null);
                        rowValue.getPrimaryAddress().setState(null);
                        saveCellValue(rowValue);
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(40);
//            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //State
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.state(), EmployeeListItem.STATE, 60) {
                @Override
                public SelectItem getCellValue(EmployeeListItem rowValue) {
                    rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                    setSelectedCountryName(rowValue.getPrimaryAddress().getCountry());
                    return new SelectItem(rowValue.getPrimaryAddress().getStateId(), rowValue.getPrimaryAddress().getState());
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem value) {
                    rowValue.setPrimaryAddress(rowValue.getPrimaryAddress(true));
                    if (SelectItem.isDifferent(value, new SelectItem(rowValue.getPrimaryAddress().getStateId(), rowValue.getPrimaryAddress().getState()))) {
                        rowValue.getPrimaryAddress().setState(value != null ? value.getName() : null);
                        rowValue.getPrimaryAddress().setStateId(value != null ? value.getId() : null);
                        saveCellValue(rowValue);
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(30);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //Country
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.postCode(), EmployeeListItem.POST_CODE, 100) {
                @Override
                public String getCellValue(EmployeeListItem rowValue) {
                    Address addressItems = rowValue.getPrimaryAddress();
                    return addressItems != null && addressItems.getZipCode() != null ? addressItems.getZipCode() : "N/A";
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    if (rowValue.getPrimaryAddress() == null) {
                        rowValue.setPrimaryAddress(new Address());
                    }
                    rowValue.getPrimaryAddress().setZipCode(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
        }


        //currency
        if (employmentInfPermission) {
            if (isFromPAYROLL() && !isFromPM()) {
                columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.currency(), EmployeeListItem.CURRENCY, 50) {
                    @Override
                    public SelectItem getCellValue(EmployeeListItem item) {
                        return item.getCurrency() != null && item.getCurrency().getName() != null ? new SelectItem(item.getCurrency().getId(), item.getCurrency().getName()) : null;
                    }

                    @Override
                    public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                        if (cellValue != null) {
                            rowValue.setCurrency(new CurrencyItem(cellValue.getId(), cellValue.getName()));
                        } else {
                            rowValue.setCurrency(null);
                        }
                        saveCellValue(rowValue);
                    }
                };
                columnConfig.setMinimumColumnWidth(40);
                columnConfig.setShow(false);
                columns.add(columnConfig);
            }
        }

        //Birth Date
        if (birthDayPermission) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.dateOfBirth(), EmployeeListItem.BIRH_DATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return DateUtils.format(item.getBirthDate());
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        rowValue.setBirthDate(new DateNonConvertable(DateUtils.parse(cellValue)));
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        if (employmentInfPermission) {
            //employee supervisor
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.supervisor(), EmployeeListItem.SUPERVISOR, 100) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getSupervisorItem();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.getSupervisorItem().setId(cellValue.getId());
                        rowValue.getSupervisorItem().setName(cellValue.getName());
                    } else {
                        rowValue.getSupervisorItem().setId(null);
                        rowValue.getSupervisorItem().setName(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //department
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), EmployeeListItem.DEPARTMENT, 80) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getDepartment() != null && item.getDepartmentId() != null ? new SelectItem(item.getDepartmentId(), item.getDepartment()) : null;
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if ((cellValue == null && rowValue.getDepartmentId() != null) ||
                            (rowValue.getDepartmentId() == null && cellValue != null) ||
                            !rowValue.getDepartmentId().equals(cellValue.getId())) {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.OK, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.specifyResignationDate());
                        DatePicker newDeptEffectiveDate = new DatePicker(new Date());
                        message.addWidget(newDeptEffectiveDate, wfmStrings.specifyResignationDate());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                if (cellValue != null) {
                                    rowValue.setDepartmentId(cellValue.getId());
                                    rowValue.setDepartment(cellValue.getName());
                                } else {
                                    rowValue.setDepartmentId(null);
                                    rowValue.setDepartment(null);
                                }
                                rowValue.setDeptStartDate(newDeptEffectiveDate.getDate());
                                saveCellValue(rowValue);
                            }
                        });
                        message.open();
                    }
                }
            };

            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //Hire date
            if (!isFromPRICING()) {
                columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.hireDate(), EmployeeListItem.START_DATE, 100) {
                    @Override
                    public String getCellValue(EmployeeListItem item) {
                        return item.getStartDate() != null ? DateUtils.format1(item.getStartDate().getNonConvertedDate()) : wfmStrings.notAvailable();
                    }

                    @Override
                    public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                        try {
                            Date cellDate = DateUtils.parse(cellValue);
                            if (cellValue != null && rowValue.getEnddate() != null && cellDate.getDate() > rowValue.getEnddate().getDateLong()) {
                                Info.show(wfmStrings.resignationDateCannotBeEarlierThanHireDate(), Info.Type.WARNING);
                            } else {
                                rowValue.setStartDate(cellDate != null ? new DateNonConvertable(cellDate) : null);
                                saveCellValue(rowValue);
                            }
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };
                columnConfig.setMinimumColumnWidth(70);
                columns.add(columnConfig);
            }

            //Fire date
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.resignationDate(), EmployeeListItem.END_DATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getEnddate() != null ? DateUtils.format1(item.getEnddate().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        if (cellValue != null && rowValue.getStartDate() != null && cellDate.getTime() < rowValue.getStartDate().getDateLong()) {
                            Info.show(wfmStrings.resignationDateCannotBeEarlierThanHireDate(), Info.Type.WARNING);
                        } else {
                            rowValue.setEnddate(cellDate != null ? new DateNonConvertable(cellDate) : null);
                            saveCellValue(rowValue);
                        }
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

//        if (CompanyConstants.C47229.equals(Utils.getEncryptedCompanyID())) {
//            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>("Driver ID", EmployeeListItem.DRIVER_ID, 60) {
//                @Override
//                public String getCellValue(EmployeeListItem item) {
//                    return item.getDriverNumber() != null ? item.getDriverNumber() : "";
//                }
//            };
//            columnConfig.setMinimumColumnWidth(50);
//            columnConfig.setShow(false);
//            columns.add(columnConfig);
//        }

        //basic salary
        if (employmentInfPermission && !isFromPM() &&
                Utils.hasPermission(isFromHRMS() ? PermissionConstants.EMP_PROFILE_BASIC_SALARY : PermissionConstants.PAYROLL_EMPLOYEE_BASIC_SALARY)) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.basicSalary(), EmployeeListItem.SALARY_AMOUNT, 110) {

                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getSalaryAmount() != null ? salaryFormat.format(item.getSalaryAmount()) : new BigDecimal("0.00").toString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    BigDecimal amount = BigDecimal.valueOf(salaryFormat.parse(cellValue));
                    rowValue.setSalaryAmount(amount);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setColumnSortable(true);
            columnConfig.setMinimumColumnWidth(70);
            columns.add(columnConfig);

            //total salary
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.totalSalary(), EmployeeListItem.TOTAL_SALARY, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getTotalSalary() != null ? salaryFormat.format(item.getTotalSalary()) : BigDecimal.ZERO.toString();
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);
        }

        if ((isFromPAYROLL() && !isFromPRICING()) || Utils.hasPermission(HRMS_PAYROLL_DEDUCTION_CATEGORIES)) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.paymentsTotal(), EmployeeListItem.PAYMENTS_TOTAL, 80) {

                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getPaymentsTotal() != null ? salaryFormat.format(item.getPaymentsTotal()) : new BigDecimal("0.00").toString();
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.deductionsTotal(), EmployeeListItem.DEDUCTIONS_TOTAL, 80) {

                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getDeductionsTotal() != null ? salaryFormat.format(item.getDeductionsTotal()) : new BigDecimal("0.00").toString();
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.loansTotal(), EmployeeListItem.LOANS_TOTAL, 80) {

                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getLoansTotal() != null ? salaryFormat.format(item.getLoansTotal()) : new BigDecimal("0.00").toString();
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        //Wage rate
        if (employmentInfPermission && !isFromPRICING()) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.wageRate(), EmployeeListItem.WAGE_RATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    if (Utils.hasAccessToDefaultEmployeeRate(item.getObjectID())) {
                        return item.getWageRate() != null ? salaryFormat.format(item.getWageRate()) : new BigDecimal("0.00").toString();
                    } else {
                        return wfmStrings.notAvailable();
                    }
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        //Client charge rate
        if (!isFromPRICING()) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.customerChargeRate(), EmployeeListItem.CLIENT_CHARGE_RATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    if (Utils.hasAccessToDefaultEmployeeRate(item.getObjectID())) {
                        return item.getClientChargeRate() != null ? salaryFormat.format(item.getClientChargeRate()) : new BigDecimal("0.00").toString();
                    } else {
                        return wfmStrings.notAvailable();
                    }
                }
            };

            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }


        if (!isFromPRICING()) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.openingBalanceForAnnualLeave(), EmployeeListItem.OPENING_BALANCE_DAYS, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getOpeningBalanceDay() != null ? String.valueOf(item.getOpeningBalanceDay()) : "";
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    if (cellValue != null) {
                        rowValue.setOpeningBalanceDay(Double.valueOf(cellValue));
                        saveCellValue(rowValue);
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columns.add(columnConfig);


            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.probationPeriodDays(), EmployeeListItem.PROBATION_DAYS, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getProbationDay() != null ? String.valueOf(item.getProbationDay()) : "";
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    if (cellValue != null) {
                        rowValue.setProbationDay(Double.valueOf(cellValue));
                        saveCellValue(rowValue);
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
//            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        //passport number field
        if (personalInfPermission && !isFromPRICING()) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.passportNumber(), EmployeeListItem.PASSPORT_NUMBER, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getPassportNumberField();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setPassportNumberField(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //passport Issue Date field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.passportIssueDate(), EmployeeListItem.PASSPORT_ISSUE_DATE, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getPassportIssueDateField() != null ? DateUtils.format1(item.getPassportIssueDateField().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        rowValue.setPassportIssueDateField(cellDate != null ? new DateNonConvertable(cellDate) : null);
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            // passport Issue Name Field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.passportIssueBy(), EmployeeListItem.PASSPORT_ISSUE_BY, 80) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return new SelectItem(item.getPassportIssueIDField(), item.getPassportIssueNameField());
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setPassportIssueNameField(cellValue != null && cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                        rowValue.setPassportIssueIDField(cellValue.getId());
                    } else {
                        rowValue.setPassportIssueNameField(null);
                        rowValue.setPassportIssueIDField(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            // Passport Expiry Date field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.passportExpireDate(), EmployeeListItem.PASSPORT_EXPIRE_DATE, 120) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getPassportExpiryDateField() != null ? DateUtils.format1(item.getPassportExpiryDateField().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        rowValue.setPassportExpiryDateField(cellDate != null ? new DateNonConvertable(cellDate) : null);
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //insurance Number Field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.insuranseNumber(), EmployeeListItem.INSURANCE_NUMBER, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getInsuranceNumberField();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setInsuranceNumberField(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.insuranceExpiryDate(), EmployeeListItem.INSURANCE_EXPIRY_DATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getInsuranceExpiryDate() != null ? DateUtils.format1(item.getInsuranceExpiryDate().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        rowValue.setInsuranceExpiryDate(cellDate != null ? new DateNonConvertable(cellDate) : null);
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //Visa Number field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.visaNumber(), EmployeeListItem.VISA_NUMBER, 70) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getVisaNumberField();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setVisaNumberField(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            // Visa Issue Date field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.visaIssueDate(), EmployeeListItem.VISA_ISSUE_DATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getVisaIssueDateField() != null ? DateUtils.format1(item.getVisaIssueDateField().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        rowValue.setVisaIssueDateField(cellDate != null ? new DateNonConvertable(cellDate) : null);
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);

            // Visa Expiration Date field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.visaExpirationDate(), EmployeeListItem.VISA_EXPIRATION_DATE, 110) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getVisaExpiryDateField() != null ? DateUtils.format1(item.getVisaExpiryDateField().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        rowValue.setVisaExpiryDateField(cellDate != null ? new DateNonConvertable(cellDate) : null);
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);
        }

        if (!isFromPRICING()) {
            // Agent ID field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.agentID(), EmployeeListItem.AGENT_ID, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getAgentName();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setAgentName(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);
        }

        if (!isFromPRICING()) {
            // Bank Name field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.bankName(), EmployeeListItem.BANK_NAME, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getBankNameString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setBankNameString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);

            // Account Number field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.accountNumber(), EmployeeListItem.ACCOUNT_NUMBER, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getAccountNumberString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setAccountNumberString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);

            // Account Name field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.accountName(), EmployeeListItem.ACCOUNT_NAME, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getAccountNameString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setAccountNameString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
            columnConfig.setColumnSortable(true);
            columns.add(columnConfig);

            // Bank Address field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.bankAddress(), EmployeeListItem.BANK_ADDRESS, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getBankAddressString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setBankAddressString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
            columnConfig.setColumnSortable(true);
            columns.add(columnConfig);

            // Swift/BIC code field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.swiftCode(), EmployeeListItem.SWIFT_CODE, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getSwiftBICCodeString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setSwiftBICCodeString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);

            // Sort code field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.sortCode(), EmployeeListItem.SORT_CODE, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getSortCodeString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setSortCodeString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
//            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);

            // IBAN code field
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.ibanCode(), EmployeeListItem.IBAN_CODE, 100) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getiBANNumberString();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    rowValue.setiBANNumberString(cellValue);
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        // Timeslot field
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.timeslot(), EmployeeListItem.TIMESLOT, 100) {
            @Override
            public SelectItem getCellValue(EmployeeListItem item) {
                return item.getTimeslot();
            }

            @Override
            public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                if (cellValue != null) {
                    rowValue.setTimeslot(new SelectItem(cellValue.getId(), cellValue.getName()));
                } else {
                    rowValue.setTimeslot(null);
                }
                saveCellValue(rowValue);
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        if (!isFromPRICING()) {
            initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        }
        return columns.toArray(new ColumnDefinitionConfig[0]);
    }

    private LookUpCellEditor<SelectItem> getLookUpWidget() {
        employeeLookUp = new EmployeeLookUpWithCode();
        return new LookUpCellEditor<SelectItem>(employeeLookUp) {
            @Override
            protected SelectItem getValue() {
                return getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                employeeLookUp.clearAndClearItems();
                employeeLookUp.refreshOracle(true);
                setSelectItem(cellValue);
            }
        };
    }

    private LookUpCellEditor<SelectItem> getCountryLookUpWidget() {
        countryLookUp = new CountryLookUp();
        return new LookUpCellEditor<SelectItem>(countryLookUp) {
            @Override
            protected SelectItem getValue() {
                return getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                countryLookUp.clearAndClearItems();
                countryLookUp.refreshOracle(true);
                setSelectItem(cellValue);
            }
        };
    }


    protected void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (EmployeeListItem.PASSPORT_NUMBER.equals(entry.getKey()) || EmployeeListItem.INSURANCE_NUMBER.equals(entry.getKey())
                    || EmployeeListItem.VISA_NUMBER.equals(entry.getKey())
                    || EmployeeListItem.BANK_NAME.equals(entry.getKey()) || EmployeeListItem.ACCOUNT_NAME.equals(entry.getKey())
                    || EmployeeListItem.SWIFT_CODE.equals(entry.getKey()) || EmployeeListItem.SORT_CODE.equals(entry.getKey())
                    || EmployeeListItem.IBAN_CODE.equals(entry.getKey()) || EmployeeListItem.BANK_ADDRESS.equals(entry.getKey())
                    || EmployeeListItem.AGENT_ID.equals(entry.getKey()) || EmployeeListItem.WPS_NUMBER.equals(entry.getKey())
                    || EmployeeListItem.SALARY_AMOUNT.equals(entry.getKey())) {
                widget = new TextBoxCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        return getText();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        setText(cellValue);
                    }
                };
            } else if (EmployeeListItem.ACCOUNT_NUMBER.equals(entry.getKey()) || EmployeeListItem.OPENING_BALANCE_DAYS.equals(entry.getKey()) || EmployeeListItem.PROBATION_DAYS.equals(entry.getKey())) {
                widget = getAccountNumberCell();
            } else if (EmployeeListItem.PASSPORT_ISSUE_BY.equals(entry.getKey())) {
                widget = getCountryLookUpWidget();
            } else if (EmployeeListItem.PASSPORT_ISSUE_DATE.equals(entry.getKey())
                    || EmployeeListItem.START_DATE.equals(entry.getKey())
                    || EmployeeListItem.END_DATE.equals(entry.getKey())
                    || EmployeeListItem.INSURANCE_EXPIRY_DATE.equals(entry.getKey())
                    || EmployeeListItem.PASSPORT_EXPIRE_DATE.equals(entry.getKey())
                    || EmployeeListItem.VISA_ISSUE_DATE.equals(entry.getKey())
                    || EmployeeListItem.VISA_EXPIRATION_DATE.equals(entry.getKey())
                    || EmployeeListItem.BIRH_DATE.equals(entry.getKey())) {

                widget = new DateTimePickerCellEditor<String>(true) {
                    @Override
                    protected String getValue() {
                        return DateUtils.format1(getDate());
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        try {
                            Date date;
                            if (cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equals(cellValue)) {
                                date = new Date();
                                setDefaultValue(true);
                            } else {
                                date = DateUtils.parse(cellValue);
                                setDefaultValue(false);
                            }
                            setDate(date, false);
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };

            } else if (EmployeeListItem.GENDER_NAME.equals(entry.getKey())) {
                widget = getGenderCell();
                getQuickGenderSaveData((DropDownCellEditor) widget);
            } else if (EmployeeListItem.EMAIL.equals(entry.getKey())) {
                widget = getEmailCell();
            } else if (EmployeeListItem.COUNTRY.equals(entry.getKey())) {
                widget = getCountryCell();
                getQuickSaveData((DropDownCellEditor) widget);
            } else if (EmployeeListItem.STATE.equals(entry.getKey())) {
                widget = getStateCell();
            } else if (EmployeeListItem.CITY.equals(entry.getKey())
                    || EmployeeListItem.STREET.equals(entry.getKey())
                    || EmployeeListItem.STREET2.equals(entry.getKey())
                    || EmployeeListItem.POST_CODE.equals(entry.getKey())
                    || EmployeeListItem.JOB_TITLE.equals(entry.getKey())) {

                widget = new TextBoxCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        return getText();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        setText(cellValue);
                    }
                };
            } else if (EmployeeListItem.SUPERVISOR.equals(entry.getKey())) {
                widget = getLookUpWidget();
            } else if (EmployeeListItem.POSITION.equals(entry.getKey())) {
                widget = getPositionCell();
                getPositionQuickSaveData((DropDownCellEditor) widget);
            } else if (EmployeeListItem.CURRENCY.equals(entry.getKey())) {
                widget = getCurrencyCell();
                getCurrencyList((DropDownCellEditor) widget);
            } else if (EmployeeListItem.LOCATION.equals(entry.getKey()) && editLocationPermission) {
                widget = getLocationCell();
                getLocationList((DropDownCellEditor) widget);
            } else if (EmployeeListItem.OPENING_BALANCE_DAYS.equals(entry.getKey()) || EmployeeListItem.PROBATION_DAYS.equals(entry.getKey())) {
                widget = new TextBoxCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        if (getTextBox().getText() != null && !"".equals(getTextBox().getText().trim())) {
                            return getText();
                        } else {
                            return null;
                        }
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        setText(cellValue);
                    }
                };
                ((TextBoxCellEditor) widget).addNumberValidation(false);

            } else if (EmployeeListItem.DEPARTMENT.equals(entry.getKey())) {
                widget = getDepartmentCell();
                getDepartmentList((DropDownCellEditor) widget);
            } else if (EmployeeListItem.TIMESLOT.equals(entry.getKey())) {
                widget = getTimeslotCell();
                getTimeslotList((DropDownCellEditor) widget);
            }

            if (widget != null) {
                column.setCellEditor(widget);
                column.setCellChangesSave(new CellChange<EmployeeListItem>() {
                    @Override
                    public void saveCell(EmployeeListItem rowValue, String columnCodeName) {
                        saveEmployeeEditCellValue(rowValue, columnCodeName);
                    }
                });
            }
        }
    }

    private void getQuickGenderSaveData(final DropDownCellEditor<String> country) {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, wfmStrings.male());
        items[1] = new SelectItem(1, wfmStrings.female());
        country.setItems(items);
    }

    private DropDownCellEditor<SelectItem> getPositionCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    private DropDownCellEditor<SelectItem> getCurrencyCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    private DropDownCellEditor<SelectItem> getLocationCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    private DropDownCellEditor<SelectItem> getDepartmentCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    private DropDownCellEditor<SelectItem> getTimeslotCell() {
        return new DropDownCellEditor<SelectItem>(true) {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    private void getPositionQuickSaveData(final DropDownCellEditor<String> position) {
        CommonService.App.get().getPositions(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                position.setItems(result);
            }
        });
    }

    public void getCurrencyList(final DropDownCellEditor<String> currency) {
        CurrencyService.App.get().getCurrencies(true, new AsyncCallback<CurrencyItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(CurrencyItem[] result) {
                currency.setItems(result);
            }
        });
    }

    public void getLocationList(final DropDownCellEditor<String> location) {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {
            }

            public void success(SelectItem[] selectItems) {
                if (selectItems != null) {
                    location.setItems(selectItems);
                }
            }
        });
    }

    public void getDepartmentList(final DropDownCellEditor<String> department) {
        ReportService.App.get().getDepartmentList(null, null, null, null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(SelectItem[] selectItems) {
                if (selectItems != null) {
                    department.setItems(selectItems);
                }

            }
        });
    }

    public void getTimeslotList(DropDownCellEditor<String> timeslot) {
        ReportService.App.get().getCompanyTimeSlots(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(SelectItem[] selectItems) {
                if (selectItems != null) {
                    timeslot.setItems(selectItems);
                }

            }
        });
    }

    private DropDownCellEditor<SelectItem> getGenderCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    private DropDownCellEditor<SelectItem> getStateCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                String cntry = (selectedCountryName + "(").split("\\(")[0].trim();
                String[] temp = countryKey.get(cntry);
                if (temp == null || temp.length < 2) {
                    getListBox().setItems(new SelectItem[0]);
                    getListBox().setEnabled(false);
                    return;
                }
                String selectedCountryID = countryKey.get(cntry)[0];
                if (selectedCountryID == null) {
                    getListBox().setItems(new SelectItem[0]);
                    getListBox().setEnabled(false);
                    return;
                }
                SelectItem[] items = map.get(Integer.parseInt(selectedCountryID));
                getListBox().setItems(items == null ? new SelectItem[0] : items);
                getListBox().setEnabled(items != null && items.length >= 1);
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue);
                }
            }
        };
    }

    private void getQuickSaveData(final DropDownCellEditor<String> country) {
        CRMService.App.get().getCountriesKey(new AsyncCallback<HashMap<String, String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(HashMap<String, String[]> result) {
                countryKey = result;
            }
        });
        CommonService.App.get().getCountries(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                country.setItems(result);
            }
        });
        CRMService.App.get().getStatesByCountryName(new AsyncCallback<HashMap<Integer, SelectItem[]>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(HashMap<Integer, SelectItem[]> result) {
                map = result;
            }
        });
    }

    private DropDownCellEditor<SelectItem> getCountryCell() {
        return new DropDownCellEditor<SelectItem>() {
            @Override
            protected SelectItem getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(SelectItem cellValue) {
                if (cellValue == null || cellValue.getId() == null) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelected(cellValue.getId());
                }
            }
        };
    }

    public TextBoxCellEditor<Object> getEmailCell() {
        return new TextBoxCellEditor<Object>(200) {
            @Override
            protected Object getValue() {
                if (getTextBox().getText() != null && !"".equals(getTextBox().getText().trim()) && !Utils.validateEmail(getTextBox().getText(), false)) {
                    getTextBox().setStyleName("x-form-invalid");
                    return viewState.getText();
                }
                viewState.setText(getTextBox().getText());
                return viewState;
            }

            @Override
            protected void setValue(Object cellValue) {
                getTextBox().setText(((HTML) cellValue).getText());
            }
        };
    }

    private TextBoxCellEditor<String> getAccountNumberCell() {
        final TextBoxCellEditor<String> cellEditor = new TextBoxCellEditor<String>() {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        cellEditor.addNumberValidation(false);

        return cellEditor;
    }

    /**
     * <i>... This is method listing panel create employee panel design ...</i>
     * <br/>
     * <i>... Wirte by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create date {20:34} ...</i>
     *
     * @return - list panel design
     */

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        if (employmentInfPermission) {
            contentConfigure.addContentConfigure("department", Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID_NAME;
                }
            });
            contentConfigure.addContentConfigure("position", wfmStrings.position(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_POSITION_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_POSITION_ID_NAME;
                }
            });
            contentConfigure.addContentConfigure("location", Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_LOCATION_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_LOCATION_ID_NAME;
                }
            });
            contentConfigure.addContentConfigure("timeslot", wfmStrings.timeslot(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_TIMESLOT_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_TIMESLOT_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        contentConfigure.addContentConfigure("role", wfmStrings.role(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.FIELD_ROLE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.FIELD_ROLE_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure("status", wfmStrings.accountStatus(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.FIELD_STATUS_ID_NAME;
            }
        });
        if (employmentInfPermission) {
            contentConfigure.addContentConfigure("currency", wfmStrings.currency(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_CURRENCY_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_CURRENCY_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure("supervisor", wfmStrings.supervisor(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_SUPERVISOR_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_SUPERVISOR_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure("qualification", wfmStrings.qualification(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEmployeeRepresenter.FIELD_QUALIFICATION_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEmployeeRepresenter.FIELD_QUALIFICATION_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        if (birthDayPermission) {
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_BIRTH_DATE, wfmStrings.birthDay());
        }
        if (employmentInfPermission) {
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_HIRE_DATE, wfmStrings.hireDate());
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_END_DATE, wfmStrings.resignationDate());
        }
        if (personalInfPermission) {
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_PASSPORT_ISSUE_DATE, wfmStrings.passportIssueDate());
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_PASSPORT_EXPIRE_DATE, wfmStrings.passportExpireDate());
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_VISA_ISSUE_DATE, wfmStrings.visaIssueDate());
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_VISA_EXPIRE_DATE, wfmStrings.visaExpirationDate());
            contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_INSURANCE_EXPIRY_DATE, wfmStrings.insuranceExpiryDate());
        }
        contentConfigure.addContentConfigureDateListBox(SolrEmployeeRepresenter.FIELD_LAST_UPDATE_DATE, wfmStrings.modifiedDate());
        contentConfigure.addContentConfigure("positionType", wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEmployeeRepresenter.FIELD_POSITION_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEmployeeRepresenter.FIELD_POSITION_TYPE_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        return contentConfigure;
    }

    private GuideListingPanelDesign getPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {

                return ((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) || Utils.isSettings()) ? EmployeeListView.this::addDetailedView : ((isFromPM() && Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) || Utils.isSettings() ||
                        (isFromPAYROLL() && Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_ADD))) ? EmployeeListView.this::addDetailedView : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (isFromPM()) {
                                data.setName(PermissionConstants.PM_CONTEXT);
                            }
                            if (isFromPAYROLL()) {
                                data.setName(FROM_PAYROLL);
                            }
                            if (Utils.isSettings() || isFromPRICING()) {
                                data.setAllEmployees(true);
                            }
                            RbacService.App.get().getEmployeeFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc facetFilterRpc) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if ((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) ||
                        (isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_NEW_EMPLOYEE)) ||
                        (isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_MULTI_EMPLOYEES))) {

                    MenuBar menu = new MenuBar(true);
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);

                    if (((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) || Utils.isSettings())) {
                        MenuPopItem addNewEmployee = new MenuPopItem(wfmStrings.employee());
                        addNewEmployee.setCommand(() -> addDetailedView());
                        menu.addItem(addNewEmployee);
                    }

                    if (((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_QUICK_ADD_NEW_EMPLOYEE)) || Utils.isSettings())) {
                        MenuPopItem quickAddEmployee = new MenuPopItem(wfmStrings.quickAdd());
                        quickAddEmployee.setCommand(() -> addQuickEmployee());
                        menu.addItem(quickAddEmployee);
                    }
                    if (((isFromHRMS() && Utils.hasPermission(PermissionConstants.HRMS_ADD_MULTI_EMPLOYEES)) || Utils.isSettings())) {
                        MenuPopItem addNewMultiEmployee = new MenuPopItem(wfmStrings.multiEmployee());
                        addNewMultiEmployee.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("hrmsemployee|add/add"));
                        menu.addItem(addNewMultiEmployee);
                    }
                    newItem.setMenu(menu);

                    return newItem;

                } else if ((isFromPM() && Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) || Utils.isSettings() ||
                        (isFromPAYROLL() && Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_ADD))) {
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                    MenuBar menu = new MenuBar(true);

                    MenuPopItem addNew = new MenuPopItem(wfmStrings.employee());
                    addNew.setCommand(() -> addDetailedView());
                    menu.addItem(addNew);

                    MenuPopItem addQuickEmployee = new MenuPopItem(wfmStrings.quickAdd());
                    addQuickEmployee.setCommand(() -> addQuickEmployee());
                    menu.addItem(addQuickEmployee);

                    MenuPopItem addNewMultiEmployee = new MenuPopItem(wfmStrings.multiEmployee());
                    addNewMultiEmployee.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|add/add"));
                    menu.addItem(addNewMultiEmployee);

                    newItem.setMenu(menu);
                    return newItem;
                } else if (isFromTC()) {
                    ActionButton addInstructor = getAddNewButton(ActionButton.Type.BUTTON);
                    addInstructor.addClickHandler(clickEvent -> addDetailedView());
                    return addInstructor;
                }

                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {

                if (Utils.hasPermission(PermissionConstants.SHOW_IMPORT_EMPLOYEE) || Utils.hasPermission(PermissionConstants.SHOW_IMPORT_EMPLOYEE_HRMS)) {
                    final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.EMPLOYEE, null);
                    imp.setSubmitCompleted(() -> {
                        if (imp.getObjectId() != null) {
                            goTo("importemployee|add/add/" + imp.getObjectId());
                        }
                    });

                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                    exportOption.initExport(null);
                } else {
                    exportOption.initExport(null);
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noEmployeeMessage());
                if (isFromHRMS()) {
                    if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
                        if (maxEmp > 0 || maxNoAccessEmp > 0 || maxEssUser > 0) {
                            message.setHref("singleemployee|add/add/" + FROM_HRMS);
                        } else {
                            message.setHref("employee|add/add");
                        }
                        message.setTextBeforeLink(wfmStrings.pleaseRegisterYourEmployeeByClicking());
                    }
                } else if (isFromPAYROLL()) {
                    if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
                        if (maxEmp > 0 || maxNoAccessEmp > 0 || maxEssUser > 0) {
                            message.setHref("starter|add/add/" + FROM_PAYROLL);
                        } else {
                            message.setHref("employee|add/add");
                        }
                        message.setTextBeforeLink(wfmStrings.pleaseRegisterYourEmployeeByClicking());
                    }
                } else {
                    if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) {
                        if (maxEmp > 0 || maxNoAccessEmp > 0 || maxEssUser > 0) {
                            message.setHref("singleemployee|add/add/" + FROM_PM);
                        } else {
                            message.setHref("employee|add/add");
                        }
                        message.setTextBeforeLink(wfmStrings.pleaseRegisterYourEmployeeByClicking());
                    }
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return (Utils.adminOrDirector() || Utils.hasRole(HR) || editPermission);//enable/disable editable option manage to employee custom fields
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("hrmsEmployeesMore");
                more.addClickHandler(event -> {
                    MenuBar menuBar = getActionsForSelections();
                    menuBar.setAutoOpen(true);
                    more.setMenu(menuBar);
                });
                return more;
            }
        };
    }

    private void addDetailedView() {
        if (maxEmp > 0 || maxNoAccessEmp > 0 || maxEssUser > 0) {
            if (isFromHRMS()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("singleemployee|add/add/" + FROM_HRMS);
            } else if (isFromPAYROLL()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("starter|add/add/" + FROM_PAYROLL);
            } else if (isFromTC()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("tcInstructor|add/add");
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("singleemployee|add/add/" + FROM_PM);
            }
        } else {
            Info.show(wfmStrings.userLimitEssExceeded(), Info.Type.WARNING);
        }
    }

    private void addQuickEmployee() {
        if (maxEmp > 0 || maxNoAccessEmp > 0 || maxEssUser > 0) {
            quickAddBox = new EmployeeQuickAdd();
            quickAddBox.setCommand(() -> {
                quickAddBox.remove();
                employeeListPanel.reloadPage();
            });
        } else {
            Info.show(wfmStrings.userLimitEssExceeded(), Info.Type.WARNING);
        }

    }

    /**
     * <i>... This is method employee listing panel request provider ...</i>
     * <br/>
     * <i>... Wirte by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create date {20:34} ...</i>
     *
     * @return - request provider
     */
    private ListingRequestProvider<EmployeeListItem> getRequestDriver() {
        return (fp, callback) -> loadList(fp, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadList(fp, null, container);
    }

    private void loadList(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (isFromPAYROLL()) {
            fp.setViewType(FROM_PAYROLL);
            fp.setModule(PermissionConstants.PAYROLL_CONTEXT);
        }
        if (isFromPM()) {
            fp.setModule(PermissionConstants.PM_CONTEXT);
        }
        if (Utils.isSettings() || isFromPRICING()) {
            fp.setAllEmployees(true);
        }
        if (isFromTC()) {
            fp.setViewType(FROM_TRAINING_CENTER);
        }
        fp.setBriefly(true);
        fp.setDepartmentId(departmentId);
        fp.setPositionID(positionId);
        fp.setLocationId(locationId);

        getEmployeesMaxCount();
        EmployeeService.App.get().getEmployeeList(fp, new AbstractAsyncCallback<ListResult<EmployeeListItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<EmployeeListItem> result) {
                totalCount = result.getTotal();
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    /**
     * Register editable option for employee custom field columns
     *
     * @param rowValue       - row value
     * @param columnCodeName - row column code name
     */
    private void saveEmployeeEditCellValue(EmployeeListItem rowValue, String columnCodeName) {
        EmployeeService.App.get().saveEmployeeEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Integer result) {
                if (result.equals(0)) {
                    Info.show(wfmStrings.employeeCantBeSupervisorForHimself(), Info.Type.WARNING);
                } else if (Errors.EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS == result) {
                    Info.warn(wfmStrings.employeeWithEmailAlreadyExist());
                } else if (Errors.EMPLOYEE_LABOUR_PERIOD_USED == result) {
                    Info.warn("The employee's labor period is used");
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYE_LIST_EDIT_CELL, result, EmployeeListView.this);
            }
        });
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

    private void setSelectedCountryName(String selectedCountryName) {
        this.selectedCountryName = selectedCountryName;
    }

    public String getPropertyCode() {
        return EMLOYEE_LIST;
    }

    private MenuBar getActionsForSelections() {
        if (actions == null) {
            actions = new ContextMenu();
            actions.getMenuBar().setAutoOpen(true);
            if (Utils.hasPermission(HRMS_EMPLOYEE_REMOVE)) {
                actions.addMenuItem(wfmStrings.delete(), true, () -> {
                    actions.hide();
                    deleteSelection();
                });
            }
            if (editPermission) {
                actions.addMenuItem(wfmStrings.changeSupervisor(), true, () -> {
                    actions.hide();
                    supervisorSelection();
                });
            }
            if (Utils.hasPermission(HRMS_CONVERT_CANDIDATE) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CONVERT_TO_CANDIDATE)) {
                actions.addMenuItem(wfmStrings.convertTo() + " " + wfmStrings.candidate(), true, () -> {
                    actions.hide();
                    convertToCandidateSelection();
                });
            }
        }
        actions.getMenuBar().setAutoOpen(true);
        return actions.getMenuBar();
    }

    private void convertToCandidateSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.employee())), Info.Type.WARNING);
        } else {
            convertEmployeeToCandidate();
        }

    }

    private void convertEmployeeToCandidate() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, wfmStrings.areYouSureThatYouWantToUpdate(), new CloseHandler() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSubmit() {
                EmployeeService.App.get().convertEmployeeToCandidate(selectedItems, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Info.show(wfmStrings.messSuccessfullySaved());
                        employeeListPanel.reloadPage();
                    }
                });
            }
        });
        messageBox.open();
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.employee())), Info.Type.WARNING);
        } else {
            deleteEmployeeItem(new ArrayList<>(selectedItems));
        }
    }

    private void supervisorSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.employee())), Info.Type.WARNING);
        } else {
            changeEmployeesSupervisor(new ArrayList<>(selectedItems));
        }
    }

    private void deleteEmployeeItem(final ArrayList<EmployeeListItem> items) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        CheckBox removeContactCheckBox = new KpiCheckBox("&nbsp;", true);
        removeContactCheckBox.setValue(false);
        removeContactCheckBox.setText(wfmStrings.removeFromContactsToo());
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                EmployeeService.App.get().checkMultipleEmployeesForApprovers(items, new AbstractAsyncCallback<List<EmployeeListItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(List<EmployeeListItem> result) {
                        if (result.size() > 0) {
                            for (EmployeeListItem employee : result) {
                                switch (employee.getEmployeeCode()) {
                                    case 1:
                                        Info.show(wfmMessages.employeeParticipatedInApprovalProcess(employee.getFullName(), employee.getFullName()), Info.Type.WARNING);
                                        break;
                                    case 2:
                                        Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                        break;
                                    case 3:
                                        Info.show(wfmMessages.employeeParticipatedInApprovalProcess(employee.getFullName(), employee.getFullName()), Info.Type.WARNING);
                                        Info.show(wfmMessages.lastOneCompanyAdminDelete(), Info.Type.WARNING);
                                        break;
                                }
                            }
                        }
                        deleteEmployee(items, removeContactCheckBox.getValue());
                    }
                });
            }
        });
        messageBox.add(removeContactCheckBox);
        messageBox.open();

    }


    private void deleteEmployee(final ArrayList<EmployeeListItem> items, boolean removeContact) {
        LoadingPanel.loading(true);
        EmployeeService.App.get().deleteEmployees(getIDs(items), removeContact, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_DELETE, getIDs(items), EmployeeListView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.employee()), Info.Type.INFO);
            }
        });
    }

    protected ArrayList<Integer> getIDs(List<EmployeeListItem> selectedEmployees) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (selectedEmployees.size() > 0) {
            for (EmployeeListItem item : selectedEmployees) {
                if (item != null && !ids.contains(item.getObjectID())) {
                    ids.add(item.getObjectID());
                }
            }
        }
        return ids;
    }


    private void changeEmployeesSupervisor(final List<EmployeeListItem> items) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureThatYouWantToUpdateSelectedEmployeesSupervisor();
        messageBox.setMessage(message);

        EmployeeLookUp employeeLookUp = new EmployeeLookUp(true, false, false, false);
        messageBox.add(new Br());
        messageBox.addWidget(employeeLookUp, wfmStrings.selectSupervisor());

        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (employeeLookUp.getSelectedItemID() == null || getIDs(items).contains(employeeLookUp.getSelectedItemID())) {
                    Info.show(wfmStrings.pleaseSelectAtLeastOneEmployee(), Info.Type.WARNING);
                } else {
                    for (EmployeeListItem employee : items) {
                        employee.setSupervisorItem(new SelectItem(employeeLookUp.getSelectedItemID()));
                        saveEmployeeEditCellValue(employee, EmployeeListItem.SUPERVISOR);
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.employee()), Info.Type.INFO);
                }
            }
        });

        messageBox.open();
    }

    private HTML getEmailLink(final EmployeeListItem rowValue) {
        SimpleLink sendEmailLink = new SimpleLink("");
        if (!Utils.isNullOrEmpty(rowValue.getEmail())) {
            sendEmailLink = new SimpleLink(rowValue.getEmail());
            sendEmailLink.addClickHandler(clickEvent -> {
                goTo("emailcompose|add/add/" + rowValue.getEmail() + "/" + RelationItem.TYPE_EMPLOYEE + "/" + rowValue.getObjectID() + "/" + rowValue.getFullName());
            });
        }
        viewState = sendEmailLink;
        return sendEmailLink;
    }

    private void generatePDF(Integer pdfTemplateID, Integer objectID) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectID, null, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/employeeProfileSummaryViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(this, pdfURL, parametrs, "_blank");
    }
}
