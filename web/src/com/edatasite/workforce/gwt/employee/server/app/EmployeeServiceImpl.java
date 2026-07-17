package com.edatasite.workforce.gwt.employee.server.app;

import com.edatasite.shared.components.PasswordGenerator;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeItemTableCF;
import com.edatasite.workforce.core.domain.customform.EdsEmployeeCustomItemTable;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTable;
import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTableCF;
import com.edatasite.workforce.core.domain.payrolluk.*;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettingsDefault;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.component.PositionSolrComponent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.document.EmployeeSolrDoc;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.EmployeePayslipItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.SupervisorStructureUtils;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.controllers.EmailAddressValidator;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeExperienceItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.*;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsDefaultManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.*;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.FileCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.DeparmentEmployees;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.rpc.TeamEmployee;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeManagedDepartment;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.DepartmentDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User:
 * Date: 07.01.2008
 * Time: 16:47:41
 */
@Transactional
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService, Constants, Errors, EmployeeServiceLocal {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private UserContactManager userContactManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private EmployeePayrollSettingsTemplateManager employeePayrollSettingsTemplateManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private BrigadaManager brigadaManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ContactService contactService;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private ProjectService projectService;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private AnnualLeaveAllowanceManager annualLeaveAllowanceManager;
    @Autowired
    private GradeManager gradeManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private UserBankAccountManager userBankAccountManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("countryLocalizer")
    private WfmMessageSource countryLocalizer;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    @Qualifier("projectService")
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private EmployeeCFManager employeeCFManager;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskService;
    @Autowired
    private PayslipPaymentsManager payslipPaymentsManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private PaymentDeductionManager paymentDeductionManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PayrollBatchManager payrollBatchManager;
    @Autowired
    private ApproverEmployeeManager approverEmployeeManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private SignupMessageManager signupMessageManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private PositionSolrComponent positionSolrComponent;
    @Autowired
    private EmployeeItemTableManager employeeItemTableManager;
    @Autowired
    private EmployeeExperienceItemTableManager employeeExperienceItemTableManager;
    @Autowired
    private EmployeeExperienceItemTableCFManager employeeExperienceItemTableCFManager;
    @Autowired
    private EmployeeItemTableCFManager employeeItemTableCFManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private LabourPeriodManager labourPeriodManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private ListPanelSettingsDefaultManager listPanelSettingsDefaultManager;
    @Autowired
    private ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Autowired
    private CrmCustomFieldsManager crmCustomFieldsManager;

    @Autowired
    private CrmContactItemParamsManager crmContactItemParamsManager;
    @Autowired
    private SpokenLanguagesManager spokenLanguagesManager;
    @Autowired
    private EmployeeLocationManager employeeLocationManager;
    @Autowired
    private SalaryHistoryLocal salaryHistoryLocal;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private SalaryHistoryManager salaryHistoryManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    private ListResult<EmployeeListItem> createEmployeeList(ListingFilterParameter fp, List<EdsEmployee> employees) {
        int totalCount = employees.size();
        if (fp.getLimit() > 0) {
            employees = ListUtils.getSublist(employees, fp.getStart(), fp.getLimit());
        }
        ArrayList<EmployeeListItem> resultList = new ArrayList<>();
        EdsUser user = userManager.getUser();
        EdsUser creator = user.getCompany().getCreator();
        for (EdsEmployee employee : employees) {
            EmployeeListItem result = createEmployeeListItem(employee);
            EdsLocation location = employee.getLocation();
            result.setLocation(location != null ? location.getName() : "");

            result.setCreator(creator.getObjectID().equals(employee.getObjectID()));

            if (fp.isCustomFieldsShown()) {
                result.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(employee.getCustomFields(), fp.getColumnsOfListing()));
            }
            resultList.add(result);
        }
        return new ListResult<>(resultList, totalCount);
    }

    private String getSortedRolesAsString(Set<EdsRole> roles) {
        StringBuilder rolesString = new StringBuilder();
        List rolesList = getRolesSortedByPattern(roles);
        for (Object aRolesList : rolesList) {
            if (rolesString.length() > 0) {
                rolesString.append(", ");
            }
            EdsRole role = roleManager.get((Integer) aRolesList);
            rolesString.append(commonLocalizer.localize(role.getCode(), role.getName()));
        }
        return rolesString.toString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployeeViewItem getCurrentEmployee() {

        return getEmployee(employeeManager.getUser().getEmployee().getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployeeViewItem getEmployee(Integer objectID) {
        EdsEmployee employee = employeeManager.get(objectID);
        EmployeeViewItem result = new EmployeeViewItem();
        if (employee != null) {
            ListingFilterParameter fpPr = new ListingFilterParameter();
            fpPr.setEmployeeId(objectID);
            fpPr.setViewAsId(EdsRole.MEM);
            List<EdsProject> projects = projectManager.list(fpPr);
            if (projects.size() > 0) {
                EmployeeProjectsListItem[] employeeprojects = new EmployeeProjectsListItem[projects
                        .size()];
                int i = 0;
                for (EdsProject project : projects) {
                    employeeprojects[i] = new EmployeeProjectsListItem();
                    employeeprojects[i].setName(project.getName());
                    employeeprojects[i].setDescription(project.getDescription());
                    if (project.getStartDate() != null) {
                        employeeprojects[i].setStartDate(project.getStartDate());
                    }
                    if (project.getEndDate() != null) {
                        employeeprojects[i].setEndDate(project.getEndDate());
                    }
                    i++;
                }
                result.setProjects(employeeprojects);
            }
            if (employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null) {
                EmployeeManagedDepartment department = new EmployeeManagedDepartment();
                department.setName(employee.getEmployeeTeam().getTeam().getName());
                department.setDescription(employee.getEmployeeTeam().getTeam().getDescription());
                Set<EdsEmployeeDepartment> employeeDepartments = employee.getEmployeeTeam().getTeam().getMembers();
                int i = 0;
                for (EdsEmployeeDepartment emplDep : employeeDepartments) {
                    if (!emplDep.getDeleted()) {
                        i++;
                    }
                }
                department.setEmployees(i);
                result.setDepartments(department);
            }
            result.setObjectID(employee.getObjectID());
            if (employee.getPosition() != null) {
                result.setPosition(employee.getPosition().getName());
            }
            if (employee.getLocation() != null) {
                result.setLocationName(employee.getLocation().getCity());
            } else {
                result.setLocationName("N/A");
            }
            result.setStartDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
            result.setUserName(employee.getEmail());
            result.setFirstName(employee.getFirstName() != null ? employee
                    .getFirstName() : "N/A");
            result.setLastName(employee.getLastName() != null ? employee
                    .getLastName() : "N/A");
            result.setMiddleName(employee.getMiddleName() != null ? employee
                    .getMiddleName() : "N/A");
            result.setEmail(employee.getEmail() != null ? employee.getEmail() : "N/A");

            if (employee.getProfile() != null) {
                if (employee.getProfile().getContact() != null) {
                    EdsAddress address = EdsAddress.getFirstAddress(employee.getProfile().getContact().getAddresses(), true, null, EdsAddress.HOME);
                    if (address != null) {
                        result.setHomeAddress(address.getAddress() != null ? address.getAddress() : "N/A");
                        result.setCityTown(address.getCity() != null ? address.getCity() : "N/A");
                        result.setCountry(address.getCountry() != null ? address.getCountry().getName() : "N/A");
                        result.setRegion(address.getState() != null ? address.getState().getName() : "N/A");
                        result.setPostCode(address.getZipCode() != null ? address.getZipCode() : "N/A");
                    }
                }
                result.setGender(employee.getProfile().getGender() != null ? employee.getProfile().getGender() : "N/A");
            } else {
                result.setHomeAddress("N/A");
                result.setCityTown("N/A");
                result.setCountry("N/A");
                result.setRegion("N/A");
                result.setPostCode("N/A");
            }
            result.setHomePhone(employee.getHomePhoneFirst() != null ? employee
                    .getHomePhoneFirst() : "N/A");
            result.setMobilePhone(employee.getMobilePhoneFirst() != null ? employee
                    .getMobilePhoneFirst() : "N/A");
            result.setWorkPhone(employee.getWorkPhoneFirst() != null ? employee
                    .getWorkPhoneFirst() : "N/A");
            result.setEndDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);
            Calendar c = GregorianCalendar.getInstance();
            double annualAllowanceDays = 0;
            boolean isCustomLeaveCalculation = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_LEAVE_CALCULATION);
            if (isCustomLeaveCalculation) {
                annualAllowanceDays = annualLeaveAllowanceManager.getLeaveAllowanceCustom(employee.getObjectID());
            } else {
                EdsAnnualLeaveAllowance edsAnnualLeaveAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(c.get(Calendar.YEAR), employee.getObjectID(), EdsSickRequest.LR_TYPE_ANNUAL_LEAVE, null);
                if (edsAnnualLeaveAllowance != null) { //TODO Hurshid Juraev, it wasn't checked before. Give solution. Otherwise it may throw NullPointerException
                    annualAllowanceDays = edsAnnualLeaveAllowance.getAllowanceDays();
                }
            }
            Integer totalAllowance = (int) Math.round(annualAllowanceDays);
            result.setAnnualAllowance(totalAllowance);
            result.setTrainingNeeds(employee.getTrainingNeeds());
            result.setGrade(employee.getGrade() != null ? referenceWfmMessageSource.localizeRef(employee.getGrade()) : "N/A");
            result.setTimeSlot(employee.getTimeSlot() != null ? employee.getTimeSlot().getName() : "N/A");
            result.setStatus(referenceWfmMessageSource.localize(employee.getAccountStatus().getCode(), employee.getAccountStatus().getName()));
            result.setCompany(employee.getCompany() != null ? employee.getCompany().getName() : "N/A");
            result.setCompanyID(employee.getCompany() != null ? employee.getCompany().getObjectID() : -1);
            result.setRole(getSortedRolesAsString(employee.getRoles())/*employee.getRolesAsIntegersString()*/);
        }
        return result;
    }

    public Integer createEmployee(NewEmployee employee) {
        return createEmployee(employee, false);
    }

    private EmployeeListItem createEmployeeListItem(EdsEmployee employee) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        EmployeeListItem result = new EmployeeListItem();
        result.setObjectID(employee.getObjectID());
        if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
            result.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
        }
        result.setFirstName(employee.getFirstName());
        result.setMiddleName(employee.getMiddleName());
        result.setLastName(employee.getLastName());
        result.setPhoneNumber(employee.getPrimaryPhone());
        if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
            result.setEmployeeNumber(employee.getProfile().getEmployeeCode());
            result.setPassportNumberField(employee.getProfile().getPassportNumber());
            result.setPassportIssueDateField(employee.getProfile().getPassportIssueDate() != null ? new DateNonConvertable(employee.getProfile().getPassportIssueDate()) : null);
            EdsEmployeeProfile passportIssue = employee.getProfile();
            if (passportIssue.getCountry() != null) {
                result.setPassportIssueIDField(passportIssue.getCountry().getObjectID());
                result.setPassportIssueNameField(passportIssue.getCountry().getName());
            }
            result.setPassportExpiryDateField(employee.getProfile().getPassportExpiryDate() != null ? new DateNonConvertable(employee.getProfile().getPassportExpiryDate()) : null);
            result.setMedicalExpiryDateField(employee.getProfile().getMedicalInsuranceExDate() != null ? new DateNonConvertable(employee.getProfile().getMedicalInsuranceExDate()) : null);
            result.setInsuranceNumberField(employee.getProfile().getInsuranceNumber());
            result.setVisaNumberField(employee.getProfile().getVisaNumber());
            result.setVisaIssueDateField(employee.getProfile().getVisaIssueDate() != null ? new DateNonConvertable(employee.getProfile().getVisaIssueDate()) : null);
            result.setVisaExpiryDateField(employee.getProfile().getVisaExpirationDate() != null ? new DateNonConvertable(employee.getProfile().getVisaExpirationDate()) : null);
        }
        EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
        if (userBankAccount != null) {
            result.setAgentName(userBankAccount.getAgentID());
        }
        result.setEmail(employee.getEmail());
        result.setStatus(referenceWfmMessageSource.localize(employee.getAccountStatus().getCode(), employee.getAccountStatus().getName()));

        result.setStatusCode(employee.getAccountStatus().getCode());
        result.setPosition(employee.getPosition() != null ? employee.getPosition().getName() : "");
        result.setStartDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
        result.setEnddate(employee.getEndDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
        if (employee.getDriverNumber() != null) {
            result.setDriverNumber(employee.getDriverNumber().toString());
        }
        result.setRole(getSortedRolesAsString(employee.getRoles()));
        result.setActive(EMPLOYEE_STATUS_ACTIVE.equals(employee.getAccountStatus().getCode()));
        result.setLastUpdate(ServerUtils.shortDateFormat(employee.getLastUpdateTime(), employee.getCompany()));
        if (employee.getObjectID() != null) {
            EdsEmployeePayrollSettings employeePayrollSettings = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.SALARY);
            if (employeePayrollSettings != null && !"".equals(employeePayrollSettings.getValue())) {
                result.setSalaryAmount(BigDecimal.valueOf(Double.parseDouble(employeePayrollSettings.getValue())));
            }
        }
        return result;
    }

    private Integer createOneOffUser(NewEmployee client, EdsUser user) {
        EdsUserContact cont = new EdsUserContact();
        cont.setFirstName(client.getFname());
        cont.setLastName(client.getLname());
        cont.setUserName(client.getEmail());
        cont.setEmail(client.getEmail());
        cont.setRandom(ServerUtils.randomstring());
//        cont.setActive(true);
        cont.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
        cont.setCreator(user);
        roleManager.addRole(cont, client.getRole());
        userContactManager.create(cont);
        userManager.saveUserAuthenticationData(cont, cont.getCompany().getObjectID());
        return cont.getObjectID();
    }

    public Integer createEmployee(NewEmployee employee, boolean fromSignUp) {
        employee.setCreatedFrom(fromSignUp ? FROM_SIGNUP_CREATED : "");
        if (fromSignUp) {
            return createFirstEmployee(employee);
        } else {
            return createEmployeeInternal(employee, null);
        }
    }

    @Transactional
    public Integer createEmployeeInternal(NewEmployee employee, Integer parentEmployeeId) {
        EdsUser loggedUser;
        if (parentEmployeeId != null) {
            loggedUser = employeeManager.get(parentEmployeeId);
        } else {
            loggedUser = employeeManager.getUser();
        }
        boolean userNameExist = false;
        if (employee.hasAccess() && !(employee.getEmail() == null || "".equals(employee.getEmail()))) {
            System.out.println(" -----------------  " + employee.getEmail() + "------------------");
            if (checkUserName(employee.getEmail(), loggedUser.getCompany().getObjectID()) == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
            }
            if (!EmailAddressValidator.checkHost(employee.getEmail())) {
                return EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST;
            }
        }
        //For ALfursan code integer companies
        boolean isCodeIntegerEnabled = (employee.getEmpCode() == null || "".equals(employee.getEmpCode())) && employeeManager.isIntegerEmployeeCodeEnabled();
        if (employee.getEmployeeTemplateID() != null || isCodeIntegerEnabled) {
            employee.setNumberData(generateEmployeeNumber(null));
            employee.setEmpCode(employee.getNumberData().getNumberString());
        }

        if ((employee.getEmpCode() != null && !"".equals(employee.getEmpCode())) && profileManager.isEmployeeCodeExists(employee.getEmpCode(), null)) {
            return EMPLOYEE_WITH_THIS_CODE_ALREADY_EXISTS;
        }

        if (employee.getPassportNumber() != null && !"".equals(employee.getPassportNumber())) {
            if (profileManager.isPassportNumberExists(employee.getPassportNumber(), null)) {
                return EMPLOYEE_WITH_THIS_PASSPORT_NUMBER_ALREADY_EXISTS;
            }
        }

        //User limit Validation
        Integer limit = checkUserLimit(employee.isEssUser(), employee.hasAccess(), loggedUser.getCompany() != null ? loggedUser.getCompany().getObjectID() : null);
        if (limit < 0) {
            return limit;
        }


        if (employee.isOneOff()) {
            return createOneOffUser(employee, loggedUser);
        }

        EdsEmployee user = employeeManager.get(loggedUser.getObjectID());
        EdsEmployee empl = new EdsEmployee();
        empl.clear();
        if (employee.getTimeslot() != null) {
            empl.setTimeSlot(timeSlotManager.get(employee.getTimeslot().getId()));
        } else {
            empl.setTimeSlot(user.getCompany().getDefaultTimeSlot());
        }
        if (employee.getEmail() != null && !"".equals(employee.getEmail())) {
            empl.setEmail(employee.getEmail());
        } else {
            String email = employee.getFname().replaceAll("\\s+", "") + "." + employee.getLname().replaceAll("\\s+", "") + "_test@workforcetrack.com";
            empl.setEmail(email);
        }
        empl.setFirstName(employee.getFname());
        empl.setNewUser(true);
        empl.setMiddleName(employee.getMname());
        empl.setLastName(employee.getLname());
        empl.setDriverNumber(employee.getDriverNumber());
        empl.setPaymentMethod(employee.getPaymentMethod());
        empl.setSalaryMode(employee.getSalaryMode());
        ArrayList<CompanyDomain> fingerprintSetup = globalAuthJdbcSpringManager.getFingerprintSetup(SecurityContext.getCompanyID());
        if (employee.getFingerprintDeviceId() != null && fingerprintSetup != null) {
            List<String> unique = fingerprintSetup.stream()
                    .filter(fs -> List.of(employee.getFingerprintDeviceId()).contains(fs.getObjectID()))
                    .map(CompanyDomain::getCompanyUniqueID)
                    .toList();
            employee.setFingerprintDeviceUuids(unique);
        }
        empl.setFingerprintDeviceUuids(employee.getFingerprintDeviceUuids());
        empl.setImportFileID(employee.getImportFileID());
        if (employee.getPhotoID() != null) {
            EdsUpload photo = attachmentManager.get(employee.getPhotoID());
            empl.setPhoto(photo);
        }
        empl.setOpeningBalanceDays(employee.getOpeningBalanceDays());
        empl.setProbationDays(employee.getProbationDays());
        if (employee.getPlacementId() != null) {
            EdsPlacement edsPlacement = placementManager.get(employee.getPlacementId());
            edsPlacement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_HIRED));
            empl.setPlacement(placementManager.get(employee.getPlacementId()));
        }

        //If user with this name already exists password should be set as existing active user's password
        if (employee.hasAccess()) {
            String password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), employee.getEmail());
            if (password == null) {
                empl.setRandom(ServerUtils.randomstring());
                PasswordGenerator pg = new PasswordGenerator(6);
                password = pg.generateAsString();
                employee.setPassword(password);
                empl.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING));
            } else {
                userNameExist = true;
                //if isn't sent activation link to employee it must be active
                empl.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
            }
            empl.setPassword(password);
        } else {
            empl.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_NO_ACCCESS));
        }
        EdsEmployeeDepartment empDept;
        if (employee.getDepartment() != null) {
            empDept = new EdsEmployeeDepartment(departmentManager.get(employee.getDepartment()), null);
        } else if ((user != null && user.getTeam() != null && !user.getTeam().getDeleted())) {
            empDept = new EdsEmployeeDepartment(user.getTeam(), null);
        } else {
            empDept = new EdsEmployeeDepartment(user.getCompany().getDefaultDepartment(), null);
        }

        // This employee importing from quick book
        if (employee.getQuickbookEmployeeID() != null && !employee.getQuickbookEmployeeID().isEmpty()) {
            empDept = new EdsEmployeeDepartment(user.getCompany().getDefaultDepartment(), null);
        }
        empDept.setStartDate(user.getCompany().getCompanyDate());
        employeeDepartmentManager.create(empDept);
        empl.setEmployeeDepartmentId(empDept.getObjectID());

        if (employee.isEssUser()) {
            empl.getRoles().clear();
            empl.getRoles().add(roleManager.getByCode(ESS_USER_CODE));
        } else {
            if (employee.getRole() != null) {
                empl.getRoles().add(roleManager.get(employee.getRole()));
            }
            if (employee.getRoleId() != null && employee.getRoleId().length > 0) {
                empl.getRoles().clear();
                for (Integer roleID : employee.getRoleId()) {
                    if (!Objects.equals(roleID, EdsRole.MEM)) {
                        empl.getRoles().add(roleManager.get(roleID));
                    }
                }
            }
            empl.getRoles().add(roleManager.get(EdsRole.MEM));
        }

        if (employee.getPositionId() != null) {
            empl.setPosition(positionManager.get(employee.getPositionId()));
        }
        if (employee.getLocationId() != null) {
            empl.setLocation(locationManager.get(employee.getLocationId()));
        }

        empl.setStartDate(employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : null);
        empl.setEndDate(employee.getEndDate() != null ? employee.getEndDate().getNonConvertedDate() : null);/*it refers to Date left previous employment*/
        String username;
        if (RegistrationTypeEnum.PHONE == employee.getRegistrationType()
                && employee.getContactListItem() != null
                && employee.getContactListItem().getPrimaryPhone() != null) {
            username = employee.getContactListItem().getPrimaryPhone();
            empl.setPassword(username);
        } else if (employee.getEmail() != null) {
            username = employee.getEmail();
        } else {
            username = employee.getFname();
        }

        //set Employee external GUID (the part for the Export employee data to QB)
        UUID externalGUID = UUID.randomUUID();
        empl.setExternalGUID(externalGUID.toString());

        empl.setUserName(username);
        employeeManager.create(empl);
        empDept.setEmployee(empl);
        employeeDepartmentManager.update(empDept);
        if (employee.hasAccess()) {
            userManager.saveUserAuthenticationData(empl, empl.getCompany().getObjectID(), !userNameExist, false);
        }
        if (employee.isFromCandidate()) {
            empl.addHistoryChange("Status", "Candidate", "Employee", true);
            employeeManager.update(empl);
        }

        if (employee.getLocationId() != null) {
            EdsEmployeeLocation employeeLocation = new EdsEmployeeLocation();
            employeeLocation.setLocation(empl.getLocation());
            employeeLocation.setUser(empl);
            employeeLocation.setLocation(empl.getLocation());
            employeeLocationManager.create(employeeLocation);
        }

        int employeeId = empl.getObjectID();
        availabilityServiceLocal.createOrUpdateLeaveAllowance(employeeId);

        hrmsServiceLocal.createLabourPeriodToEmployee(empl, employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : null);

        for (Map.Entry<String, String> payrollSetting : employee.getPayrollSettings().entrySet()) {
            if (payrollSetting.getValue() == null) {
                continue;
            }
            EdsEmployeePayrollSettings eps = employeePayrollSettingsManager.getEmployeeSettingValue(employeeId, payrollSetting.getKey());
            if (payrollSetting.getValue().equals(PayrollConstants.EMPTY_VALUE)) { //Delete if empty
                if (eps != null) {
                    employeePayrollSettingsManager.delete(eps);
                }
                continue;
            }
            if (eps == null) {
                eps = new EdsEmployeePayrollSettings();
            }
            eps.setEmployeeId(empl.getObjectID());
            eps.setKey(payrollSetting.getKey());
            eps.setValue(payrollSetting.getValue());
            employeePayrollSettingsManager.createOrUpdate(eps);
        }
        EdsPaymentDeduction newPaymentDeduction;
        if (employee.getPayments() != null && employee.getPayments().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getPayments()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(empl.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }

        if (employee.getDeductions() != null && employee.getDeductions().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getDeductions()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                    paymentDeductionManager.flush();
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(empl.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                newPaymentDeduction.getLinkedCategories().clear();
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                    EdsPayrollCategory category = null;
                    for (PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                        category = categoryManager.get(linkedCategory.getCategoryItem().getId());
                        if (category != null) {
                            category.addPaymentDeduction(newPaymentDeduction);
                        }
                    }
                }
            }
        }

        if (employee.getLoans() != null && employee.getLoans().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getLoans()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(empl.getObjectID());
                if (paymentOrDeductionItem.getPercentage() != null && paymentOrDeductionItem.getPercentage().compareTo(BigDecimal.ZERO) != 0) {
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                }
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setStartDate(paymentOrDeductionItem.getStarttDate().getNonConvertedDate());
                newPaymentDeduction.setTotalAmount(paymentOrDeductionItem.getTotalAmount());
                newPaymentDeduction.setRecurring(true);
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }

        if (employee.getEmployerContributions() != null && employee.getEmployerContributions().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getEmployerContributions()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(empl.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }


        if (employee.getDeletedCategories() != null && employee.getDeletedCategories().size() > 0) {
            for (Integer id : employee.getDeletedCategories()) {
                paymentDeductionManager.deletePaymentOrDeduction(id);
            }
        }

        if (employee.getInactiveCategories() != null && employee.getInactiveCategories().size() > 0) {
            EdsPaymentDeduction paymentDeduction;
            for (Integer id : employee.getInactiveCategories()) {
                paymentDeduction = paymentDeductionManager.get(id);
                if (paymentDeduction != null) {
                    paymentDeduction.setRecurring(false);
                }
            }
        }

        String startPage = "";
        for (EdsRole item : empl.getRoles()) {
            if (EdsRole.HR_CODE.equals(item.getCode())) {
                startPage = "Hrms.html";
                break;
            } else if (EdsRole.ACCOUNTANT_CODE.equals(item.getCode())) {
                startPage = "Accounting.html";
                break;
            } else if (EdsRole.SALESPERSON_CODE.equals(item.getCode()) || EdsRole.SALESMAN_CODE.equals(item.getCode())) {
                startPage = "Crm.html";
                break;
            }
        }
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(empl);
        userSettings.setStartPage(!"".equals(startPage) ? startPage : null);
        long start = System.currentTimeMillis();
        if (!employee.isFromEmployeeImport()) {
            if (employee.isFromMultiEmployee()) {
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(NewEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, empl, user);
                event.setCustomStringField("createAttendaceRawDataRecords");
            } else {
                availabilityService.createAttendaceRawDataRecords(empl.getObjectID(), 0);
            }
        }
        log.info("createAttendaceRawDataRecords took:" + (System.currentTimeMillis() - start));

        //Add employee to groups
        start = System.currentTimeMillis();
        createAssignUsersToGroups(empl);
        log.info("createAssignUsersToGroups took:" + (System.currentTimeMillis() - start));

        Address homeAddress = new Address();
        homeAddress.setAddress(employee.getHomeAddress());
        homeAddress.setCity(employee.getCityTown());
        if (employee.getCountry() != null) {
            homeAddress.setCountryId(employee.getCountry());
        }
        if (employee.getRegion() != null) {
            homeAddress.setStateId(employee.getRegion());
        }
        homeAddress.setZipCode(employee.getPostCode());
        ContactListItem contact = new ContactListItem();
        if (employee.getHphone() != null && !"".equals(employee.getHphone())) {
            contact.getHomePhone().add(employee.getHphone());
        }
        if (employee.getWphone() != null && !"".equals(employee.getWphone())) {
            contact.getWorkPhone().add(employee.getWphone());
        }
        if (employee.getMphone() != null && !"".equals(employee.getMphone())) {
            contact.getMobile().add(employee.getMphone());
        }
        contact.getAddresses().add(homeAddress);
        if (employee.getBirthDate() != null) {
            contact.setBirthDate(employee.getBirthDate());
        }
        if (FROM_HIRED_PLACEMENT_CANDIDATE.equals(employee.getCreatedFrom())) {
            //create employee contact logic (from candidate to employee contact)
            if (employee.getExistingContactID() != null) {
                EdsCrmContact crmContact = crmContactManager.get(employee.getExistingContactID());
                if (crmContact != null) {
                    EdsReference hiredCandidateStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_HIRED);
                    crmContact.setLeadStatus(hiredCandidateStatus);

                    if (empl.getProfile() != null) {
                        empl.getProfile().setContact(crmContact);
                        profileManager.update(empl.getProfile());
                    } else {
                        EdsEmployeeProfile profile = new EdsEmployeeProfile();
                        profile.setEmployee(empl);
                        profileManager.create(profile);
                        empl.setProfile(profile);
                        profile.setContact(crmContact);
                        profileManager.update(profile);
                    }
                    //contact never gets null... after these ifs... :)
                    if (empl.getProfile() != null) {
                        crmContact.setEntityContactID(empl.getProfile().getObjectID());
                    }

                    crmContact.setContactType(EdsCrmContact.EMPLOYEE_CONTACT);

                    try {
                        contactSolrComponent.index(crmContact);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            start = System.currentTimeMillis();
            contactServiceLocal.saveContactDetailsOfEmployee(empl, contact, false);
            log.info("saveContactDetailsOfEmployee took:" + (System.currentTimeMillis() - start));
        }
        start = System.currentTimeMillis();
        empl.setCustomFields(hrmsServiceLocal.saveEmployeeCustomFields(empl.getCustomFields(), employee.getCustomFields()));
        for (Map.Entry<String, ArrayList<CustomTableRpc>> map : employee.getCustomTableItems().entrySet()) {
            List<CustomTableRpc> values = map.getValue();
            if (empl != null && empl.getObjectID() != null) {
                for (CustomTableRpc customTableRpc : values) {
                    List<EdsEmployeeCustomItemTable> oldValuesEmployee = employeeItemTableManager.findByUuid(empl.getObjectID(), customTableRpc.getUuid());

                    if (oldValuesEmployee != null && oldValuesEmployee.size() > 0) {
                        for (EdsEmployeeCustomItemTable itemTable : oldValuesEmployee) {
                            employeeItemTableManager.delete(itemTable);
                        }
                    }
                }
            }

            for (CustomTableRpc rpc : values) {
                EdsEmployeeCustomItemTable customItemTable = new EdsEmployeeCustomItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setEmployee(empl);
                if (saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()) != null) {
                    employeeItemTableManager.createOrUpdate(customItemTable);
                }
            }
        }

        Set<EdsEmployeeExperienceItemTable> items = new HashSet<>();
        if (employee.getExperienceTableItems() != null) {
            for (ExperienceTableItems experienceTableItem : employee.getExperienceTableItems()) {
                if (experienceTableItem.getHireDate() != null) {
                    EdsEmployeeExperienceItemTable edsEmployeeExperienceItemTable = new EdsEmployeeExperienceItemTable();
                    if (experienceTableItem.getHireDate() != null) {
                        edsEmployeeExperienceItemTable.setHireDate(experienceTableItem.getHireDate());
                    }
                    if (experienceTableItem.getResignDate() != null) {
                        edsEmployeeExperienceItemTable.setResignDate(experienceTableItem.getResignDate());
                    }
                    if (experienceTableItem.getPosition() != null) {
                        edsEmployeeExperienceItemTable.setPosition(experienceTableItem.getPosition());
                    }
                    if (experienceTableItem.getDepartment() != null) {
                        edsEmployeeExperienceItemTable.setDepartment(experienceTableItem.getDepartment());
                    }
                    if (experienceTableItem.getOrganization() != null) {
                        edsEmployeeExperienceItemTable.setOrganization(experienceTableItem.getOrganization());
                    }
                    if (experienceTableItem.getIndustry() != null) {
                        edsEmployeeExperienceItemTable.setIndustryId(experienceTableItem.getIndustry().getId());
                    }
                    edsEmployeeExperienceItemTable.setCustomFields(saveExperienceItemCustomFields(edsEmployeeExperienceItemTable.getCustomFields(), experienceTableItem.getItemCustomFields()));
                    edsEmployeeExperienceItemTable.setEdsEmployee(empl);
                    employeeExperienceItemTableManager.createOrUpdate(edsEmployeeExperienceItemTable);
                    items.add(edsEmployeeExperienceItemTable);
                }
            }
        }
        empl.setExperienceItemTables(items);

        log.info("saveEmployeeCustomFields took:" + (System.currentTimeMillis() - start));
        if (employee.isAddSingleEmployee()) {
            start = System.currentTimeMillis();
            if (employee.getContactListItem() == null) {
                employee.setContactListItem(new ContactListItem());
            }
            employee.getContactListItem().setObjectId(empl.getContact().getObjectID());
            employee.getContactListItem().setCustomFields(null);
            contactService.saveContact(employee.getContactListItem(), null, true);
            contactServiceLocal.copyEmployeeCustomFields(empl.getCustomFields(), empl.getContact());

            if (employee.getGender() != null) {
                empl.getProfile().setGender(employee.getGender());
            }
            empl.getProfile().setNationality(employee.getNationality());

            if (employee.getMartialStatusId() != null) {
                empl.getProfile().setMartialStatus(referenceManager.get(employee.getMartialStatusId()));
            }

            ArrayList<Integer> newList = new ArrayList<>();
            if (employee.getSpokingLanguages() != null) {
                spokenLanguagesManager.firstRemoveEmployeeLanguages(empl.getObjectID(), EdsSpokenLanguages.TYPE_EMPLOYEE);
                for (SpokenLanguageItem languageItem : employee.getSpokingLanguages()) {
                    if (languageItem.getLanguage() != null && languageItem.getLanguage().getId() != null && languageItem.getLevel() != null && languageItem.getLevel().getId() != null) {
                        EdsSpokenLanguages language = spokenLanguagesManager.getByRelation(empl.getObjectID(), EdsSpokenLanguages.TYPE_EMPLOYEE, languageItem.getLanguage().getId());
                        if (language == null) {
                            language = new EdsSpokenLanguages();
                            language.setEntityType(EdsSpokenLanguages.TYPE_EMPLOYEE);
                            language.setEntityId(empl.getObjectID());
                            language.setLanguage(referenceManager.get(languageItem.getLanguage().getId()));
                        }
                        language.setLevel(referenceManager.get(languageItem.getLevel().getId()));
                        spokenLanguagesManager.createOrUpdate(language);
                        newList.add(languageItem.getLanguage().getId());
                    }
                }
            }
            if (!newList.isEmpty()) {
                spokenLanguagesManager.removedLanguages(employeeId, EdsSpokenLanguages.TYPE_EMPLOYEE, newList);
            }
            empl.getProfile().setEmployeeCode(employee.getEmpCode());
            if (employee.getNumberData() != null) {
                empl.getProfile().setIntNumber(employee.getNumberData().getIntNumber());
                empl.getProfile().setSavedNumberFormula(employee.getNumberData().getSavedNumberFormula());
            }
            if (employee.getDepartment() != null) {
                if ((empl.getEmployeeTeam() != null) && (empl.getEmployeeTeam().getTeam() != null)) {
                    if (!employee.getDepartment().equals(empl.getEmployeeTeam().getTeam().getObjectID())) {
                        departmentService.saveEmployeeDepartment(new HashSet<>(empl.getObjectID()), employee.getDepartment(), true, false, false);
                    }
                }
            }

            if (!empl.getWageRate().equals(employee.getWageRate()) || !empl.getClientChargeRate().equals(employee.getClientChargeRate())) {
                if (employee.getWageRate() != null) {
                    empl.setWageRate(employee.getWageRate());
                }
                if (employee.getClientChargeRate() != null) {
                    empl.setClientChargeRate(employee.getClientChargeRate());
                }
                EmployeeWageClientRateHistory hist = new EmployeeWageClientRateHistory();
                hist.setChangeDate(empl.getCompany().getCompanyDate());
                hist.setWageRate(empl.getWageRate());
                hist.setClientChargeRate(empl.getClientChargeRate());
                hist.setEmployee(empl);
                empl.getWageClientRatesHistory().add(hist);
            }
            if (employee.getQualificationID() != null) {
                empl.setQualification(referenceManager.get(employee.getQualificationID()));
            }
            if (employee.getStatusId() != null && !empl.getAccountStatus().getObjectID().equals(employee.getStatusId())) {
                EdsReference status = referenceManager.get(employee.getStatusId());
                if (EMPLOYEE_STATUS_ACTIVE.equals(status.getCode())) {
                    if (EMPLOYEE_STATUS_NO_ACCCESS.equals(empl.getAccountStatus().getCode())) {
                        //GRANT ACCESS
                        Boolean successfull = grantAccessToEmployee(empl.getObjectID(), true);
                        if (!successfull) {
                            return EMPLOYEE_STATUS_NO_ACCESS;
                        }
                    } else {
                        //ACTIVATE
                        activateOrDisactivateEmployee(empl.getObjectID(), true);
                    }
                } else if (EMPLOYEE_STATUS_INACTIVE.equals(status.getCode())) {
                    //DEACTIVATE
                    activateOrDisactivateEmployee(empl.getObjectID(), false);
                } else if (EMPLOYEE_STATUS_NO_ACCCESS.equals(status.getCode())) {
                    //REVOKE ACCESS
                    grantAccessToEmployee(empl.getObjectID(), false);
                }
            }

            if (employee.getReportsToId() != null) {
                empl.getProfile().setReportsTo(employeeManager.get(employee.getReportsToId()));
                //for supervisor structure
                Integer employeeGraphChartMapSize = RedisClient.getKey("EmployeeGraphChartMapSize_" + ServerSecurityContext.getInstance().getCompanyId(), Integer.class);
                RedisClient.removeKey("EmployeeGraphChartMapSize_" + ServerSecurityContext.getInstance().getCompanyId());
                if (employeeGraphChartMapSize != null && employeeGraphChartMapSize != 0) {
                    RedisClient.setKey("employeeGraphChartMapIsChanged_" + ServerSecurityContext.getInstance().getCompanyId(), true, Boolean.class);
                    for (int i = 2; i <= employeeGraphChartMapSize; i++) {
                        RedisClient.removeKey("EmployeeGraphChart_" + i + "_level_" + ServerSecurityContext.getInstance().getCompanyId());
                    }
                    EdsBusinessEvent event = baseEventPostProcessor.registerEvent(NewEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, null, null);
                    event.setUpdateOrgChartCash(true);
                }
            }
            empl.getProfile().setTermsOfContract(employee.getTermsOfContract());
            if (employee.getTermsOfCMonthORYear() != null) {
                empl.getProfile().setTermsOfCMonthOrYear(employee.getTermsOfCMonthORYear());//
            }
            if (employee.getEmpModeId() != null) {
                empl.getProfile().setEmploymentMode(referenceManager.get(employee.getEmpModeId()));
            }
            if (employee.getSalaryGradeId() != null) {
                empl.getProfile().setSalaryGrade(gradeManager.get(employee.getSalaryGradeId()));
            }
            if (employee.getSalaryAmount() != null) {
                empl.getProfile().setSalaryAmount(employee.getSalaryAmount());
                employeePayrollSettingsManager.update(empl, SALARY, String.valueOf(employee.getSalaryAmount()));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : new Date());
                ServerUtils.setBeginningOfTheDay(calendar);

                SalaryHistory salaryHistory = new SalaryHistory();
                salaryHistory.setEmployeeId(empl.getObjectID());
                salaryHistory.setSalary(BigDecimal.valueOf(employee.getSalaryAmount()));
                salaryHistory.setEffectiveDate(new DateNonConvertable(calendar.getTime()));
                salaryHistory.setRelationId(empl.getProfile().getObjectID());
                salaryHistory.setRelationType(EdsSalaryHistory.TYPE_PROFILE);
                salaryHistoryLocal.save(salaryHistory);
            }
            if (employee.getJobTitleId() != null) {
                employeePayrollSettingsManager.update(empl, CustomFormConstants.JOB_TITLE, String.valueOf(employee.getJobTitleId()));
                employeePayrollSettingsManager.update(empl, JOB_TITLE_TEXT, employee.getJobTitle());
            }
            boolean hasVisaExpirationDate = false;
            if (employee.getVisaExpirationDate() != null) {
                empl.getProfile().setVisaExpirationDate(employee.getVisaExpirationDate().getNonConvertedDate());
                hasVisaExpirationDate = true;
            }
            Integer employeeProfileObjectID = empl.getProfile().getObjectID();
            ArrayList<CalendarEventReminder> visaExpirationDateReminder = employee.getVisaExpirationDateReminder();
            if (hasVisaExpirationDate && employeeProfileObjectID != null) {
                //clear employee visa expiration reminders
                profileManager.deleteEmployeeVisaExpirationReminder(employeeProfileObjectID);
                //
                List<EdsRecurrence> recurrenceList = recurrenceManager.getRecurrenceJobList(SchedulerConstant.EMPLOYEE_VISA_EXPIRATION_REMINDER, employeeProfileObjectID, empl.getCompany().getObjectID());
                if (recurrenceList != null && recurrenceList.size() > 0) {
                    for (EdsRecurrence rec : recurrenceList) {
                        recurrenceService.updateRecurrence(rec, true, true);
                    }
                }
                if (visaExpirationDateReminder != null && visaExpirationDateReminder.size() > 0) {
                    RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
                    recurrenceJobItem.setEnabled(true);
                    recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
                    recurrenceJobItem.setJobType(SchedulerConstant.EMPLOYEE_VISA_EXPIRATION_REMINDER);
                    recurrenceJobItem.setBusObjectId(employeeProfileObjectID);
                    recurrenceJobItem.setInterval(1);
                    recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    recurrenceJobItem.setEndType(SchedulerConstant.END_BY_DATE);
                    //
                    for (CalendarEventReminder reminder : visaExpirationDateReminder) {
                        if (reminder.getReminderTimes() != null) {
                            Date recStartDate = DateUtil.addMinutes(empl.getProfile().getVisaExpirationDate(), (-1) * reminder.getReminderTimes());
                            if (recStartDate.after(new Date())) {
                                recurrenceJobItem.setEndDate(DateUtil.addMinutes(recStartDate, 5));
                                recurrenceJobItem.setStartDate(recStartDate);
                                recurrenceJobItem.setBusObjectParams(reminder.getReminderTimes().toString());
                                recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                                recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());

                                recurrenceJobItem.setStartDate(recStartDate);
                                recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                                recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                                recurrenceService.saveRecurrenceJob(recurrenceJobItem);

                                //create/update employee visa expiration date reminder
                                EdsEmployeeProfileVisaExpirationReminder visaExpirationReminder = new EdsEmployeeProfileVisaExpirationReminder();
                                visaExpirationReminder.setMinutes(reminder.getReminderTimes());
                                visaExpirationReminder.setEmployeeProfile(empl.getProfile());
                                empl.getProfile().getVisaExpirationReminders().add(visaExpirationReminder);
                            }
                        }
                    }
                }
            }

            if (employee.getPositionId() != null) {
                EdsPosition position = positionManager.get(employee.getPositionId());

                if (position != null) {
//                    empl.getProfile().setPositionId(position.getObjectID());
                    empl.setPosition(position);
                }
            }
            empl.getProfile().setPassportNumber(employee.getPassportNumber());
            empl.getProfile().setPassportIssueDate(employee.getPassportIssueDate() != null ? employee.getPassportIssueDate().getNonConvertedDate() : null);
            if (employee.getPassportIssueID() != null) {
                empl.getProfile().setCountry(countryManager.get(employee.getPassportIssueID()));
            }
            empl.getProfile().setPassportExpiryDate(employee.getPassportExpiryDate() != null ? employee.getPassportExpiryDate().getNonConvertedDate() : null);
            empl.getProfile().setMedicalInsuranceExDate(employee.getMedicalInsuranceExpireDate() != null ? employee.getMedicalInsuranceExpireDate().getNonConvertedDate() : null);
            empl.getProfile().setVisaNumber(employee.getVisaNumber());
            empl.getProfile().setVisaIssueDate(employee.getVisaIssueDate() != null ? employee.getVisaIssueDate().getNonConvertedDate() : null);
            empl.getProfile().setInsuranceNumber(employee.getInsuranceNumber());
            empl.getProfile().setEmployeeDegree(referenceManager.get(employee.getEmployeeDegree()));

            if (employee.getAttachments() != null && employee.getAttachments().length > 0) {
                attachmentUtilsManager.saveAttachments(F_EMPLOYEE_PROFILE, empl.getObjectID(), empl.getObjectID(), employee.getAttachments());
            }

            EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(empl);
            UserBankAccountData bankAccountData = employee.getBankAccountData();
            if (bankAccountData != null) {
                if (userBankAccount == null) {
                    userBankAccount = new EdsUserBankAccount();
                    userBankAccount.setUser(empl);
                }
                userBankAccount.setBankName(bankAccountData.getBankName());
                userBankAccount.setBankAddress(bankAccountData.getBankAddress());
                userBankAccount.setAccountNumber(bankAccountData.getAccountNumber());
                userBankAccount.setAccountName(bankAccountData.getAccountName());
                userBankAccount.setSwiftCode(bankAccountData.getSwiftCode());
                userBankAccount.setSortCode(bankAccountData.getSortCode());
                userBankAccount.setIbanCode(bankAccountData.getIbanCode());
                userBankAccount.setAgentID(bankAccountData.getAgentID());

                if (userBankAccount.getObjectID() != null) {
                    userBankAccountManager.update(userBankAccount);
                } else {
                    userBankAccountManager.create(userBankAccount);
                }
            } else {
                if (userBankAccount != null) {
                    userBankAccountManager.delete(userBankAccount);
                }
            }
            empl.setLastUpdateTime(new Date());
            employeeManager.update(empl);
            log.info("isAddSingleEmployee took:" + (System.currentTimeMillis() - start));
        }
        start = System.currentTimeMillis();
        Integer[] defaultProjectMember = {empl.getObjectID()};
        if (FROM_HIRED_PLACEMENT_CANDIDATE.equals(employee.getCreatedFrom())) {
            if (employee.getProjectID() != null) {
                commonService.addMembers(employee.getProjectID(), defaultProjectMember);
            }
        } else {
            if (loggedUser.getCompany().getDefaultProject() != null) {
                commonService.addMembers(loggedUser.getCompany().getDefaultProject().getObjectID(), defaultProjectMember);
            }
        }
        log.info("commonService.addMembers:" + (System.currentTimeMillis() - start));
        if (empl.getEmployeeTeam() != null) {
            if (employee.getProjects() != null) {
                employee.getProjects();
                for (Integer id : employee.getProjects()) {
                    EdsProject project = projectManager.get(id);
                    EdsEmployeeDepartment employeeDepartment = empl
                            .getEmployeeTeam();
                    if (employeeDepartment != null) {
                        EdsProjectEmployee pe = new EdsProjectEmployee(
                                employeeDepartment, project);
                        projectEmployeeManager.create(pe);
                        baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);
                        baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
                    }
                }
            }
        }

        if (empl.getObjectID() == null) {
            return CAN_NOT_CREATE_EMPLOYEE;
        } else {
            if (employee.hasAccess()) {
                if (userNameExist) {
                    baseEventPostProcessor.registerEvent(ExistingEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
                } else {
                    if (employee.getCreatedFrom().equals(EMPLOYEE_CREATED_GOOGLE_MARKET_PLACE)) {
                        baseEventPostProcessor.registerEvent(NewEmployeeFromGoogleMarketEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
                    } else {
                        if ((!employee.getCreatedFrom().equals(EMPLOYEE_CREATED_FROM_ASSESSMENT)) /*&& (!employee.getCreatedFrom().equals(EMPLOYEE_CREATED_FROM_PM_GETTING_STARTED))*/) {
                            baseEventPostProcessor.registerEvent(NewEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
                        }
                    }
                }
            }
            //chatService into business event
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
        workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(empl.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, "Add employee");
        EdsEmployeeProfile profile = profileManager.getProfile(empl.getObjectID());
        if (profile != null) {
            if (!employee.isAddSingleEmployee() || (employee.isFromEmployeeImport() && (profile.getEmployeeCode() == null || "".equals(profile.getEmployeeCode())))) {
                NumberData numberData = generateEmployeeNumber(null);
                if (numberData != null) {
                    profile.setIntNumber(numberData.getIntNumber());
                    String fullNumber = numberData.getNumberString() != null ? numberData.getNumberString() : "";
                    if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                        profile.setSavedNumberFormula(numberData.getSavedNumberFormula());
                    } else {
                        profile.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + ("".equals(numberData.getIntNumber()) ? "null" : decimalFormat.format(numberData.getIntNumber())) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
                    }
                    profile.setEmployeeCode(fullNumber);
                }
            }

            profileManager.update(profile);
        }

        List<EdsListPanelSettingsDefault> defaultSettings = listPanelSettingsDefaultManager.getPanelListDefaultSettings();
        if (defaultSettings != null && defaultSettings.size() > 0) {
            for (EdsListPanelSettingsDefault setting : defaultSettings) {
                EdsListPanelSettings listPanelSettings = new EdsListPanelSettings();
                listPanelSettings.setUser(empl);
                listPanelSettings.setSortBy(setting.getSortBy());
                listPanelSettings.setPanelType(setting.getPanelType());
                listPanelSettings.setSettingsJSONData(setting.getSettingsJSONData());
                listPanelSettingsManager.createOrUpdate(listPanelSettings);
            }
        }
        EdsBusinessEvent edsBusinessEvent = baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
        edsBusinessEvent.setSolrIndexed(true);
        start = System.currentTimeMillis();
        try {
            employeeSolrComponent.index(empl);
        } catch (SolrServerException | InterruptedException e) {
            edsBusinessEvent.setSolrIndexed(false);
            log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
        } catch (IOException e) {
            edsBusinessEvent.setSolrIndexed(false);
            log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
        }
        log.info("addEmployeeToIndex took:" + (System.currentTimeMillis() - start));
        return empl.getObjectID();
    }

    public Integer checkUserLimit(boolean essUser, boolean hasAccess, Integer companyID) {
        Integer[] count = getAllEmployeesMaxCount(companyID, null);
        int active = count[Constants.ACTIVE];
        int noAccess = count[Constants.NO_ACCESS];
        int ess = count[Constants.ESS];
        if (essUser) {
            if (ess <= 0) {
                return ESS_LIMIT_EXCEEDED;
            }
        } else {
            if (hasAccess && active <= 0) {
                return ACTIVE_LIMIT_EXCEEDED;
            }
            if (!hasAccess && noAccess <= 0) {
                return NO_ACCESS_LIMIT_EXCEEDED;
            }
        }
        return 0;
    }

    public NumberData generateEmployeeNumber(Integer objectID) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = profileManager.getEmployeeLastIntNumber();
        String savedNumberFormat;
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER)) {
            savedNumberFormat = profileManager.getSavedNumberformat(objectID);
            NumberData numberData = new NumberData();
            numberData.setNumberString(savedNumberFormat);
            return numberData;
        } else if (settings != null && settings.getEmployeeNumberingFormat() != null) {
            if (objectID != null) {
                savedNumberFormat = profileManager.getSavedNumberformat(objectID);
                NumberData numberData = settings.parsNumberDataForEdit(intNumber, savedNumberFormat, settings.getEmployeeNumberingFormat());
                numberData.setDelimiter(settings.getDelimetrEmployeeNumbering());
                return numberData;
            }
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getEmployeeNumberingFormat(), settings.getDelimetrEmployeeNumbering(), null, null, null, "employee");
            numberData.setDelimiter(settings.getDelimetrEmployeeNumbering());
            return numberData;
        } else if (objectID != null) {
            savedNumberFormat = profileManager.getSavedNumberformat(objectID);
            EdsNumberingSettings numberingSettings = new EdsNumberingSettings();
            return numberingSettings.parsNumberDataForEdit(intNumber, savedNumberFormat, "EMP");
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_EMPLOYEE_PREFIX);
        }
    }

    private Integer createFirstEmployee(NewEmployee employee) {
        if (employee.getRegistrationType() == null || RegistrationTypeEnum.EMAIL.equals(employee.getRegistrationType())) {
            if (checkUserName(employee.getEmail(), Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())) == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
            }
        } else {
            if (checkUserName(employee.getSocialUserName(), Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())) == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
            }
        }

        if (!EmailAddressValidator.checkHost(employee.getEmail())) {
            if (employee.getRegistrationType() == null || RegistrationTypeEnum.EMAIL.equals(employee.getRegistrationType())) {
                return EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST;

            }
        }
        ContactListItem contact = new ContactListItem();
        EdsEmployee edsEmployee = new EdsEmployee();
        edsEmployee.setNewUser(true);
        edsEmployee.setEmail(employee.getEmail());
        edsEmployee.setFirstName(employee.getFname());
        edsEmployee.setMiddleName(employee.getMname());
        edsEmployee.setLastName(employee.getLname());
        edsEmployee.setSocialImageUrl(employee.getPhotoURL());
        edsEmployee.setRegistrationType(employee.getRegistrationType());
        if (employee.getHphone() != null) {
            contact.getHomePhone().add(employee.getHphone());
        }
        if (employee.getMphone() != null) {
            contact.getMobile().add(employee.getMphone());
        }
        if (employee.getWphone() != null) {
            contact.getWorkPhone().add(employee.getWphone());
        }
        if (employee.getBirthDate() != null) {
            contact.setBirthDate(employee.getBirthDate());
        }
        edsEmployee.setStartDate(employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : null);
        edsEmployee.setEndDate(employee.getEndDate() != null ? employee.getEndDate().getNonConvertedDate() : null);/*it refers to Date left previous employment*/
        if (employee.isActive() || employee.getRegistrationType() != null && !RegistrationTypeEnum.EMAIL.equals(employee.getRegistrationType())) {
            edsEmployee.setActive(true);//social sign up , set employee as active
            edsEmployee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
        } else {
            //If Signup came from SocialNetworks
            edsEmployee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING));
        }
        employeeManager.create(edsEmployee);
        EdsEmployeeProfile profile = new EdsEmployeeProfile();
        profile.setEmployee(edsEmployee);
        profileManager.create(profile);
        edsEmployee.setProfile(profile);
        if (employee.getGender() != null) {
            profile.setGender(employee.getGender());
        }

        Address homeAddress = new Address();
        homeAddress.setAddress(employee.getHomeAddress());
        homeAddress.setCity(employee.getCityTown());
        if (employee.getCountry() != null) {
            homeAddress.setCountryId(employee.getCountry());
        }
        if (employee.getRegion() != null) {
            homeAddress.setStateId(employee.getRegion());
        }
        homeAddress.setZipCode(employee.getPostCode());
        contact.getAddresses().add(homeAddress);
        boolean isFromSigUp = employee.getCreatedFrom() != null && FROM_SIGNUP_CREATED.equals(employee.getCreatedFrom());
        Integer contactID = contactServiceLocal.saveContactDetailsOfEmployee(edsEmployee, contact, isFromSigUp);
        if (contactID != null) {
            profile.setContact(crmContactManager.get(contactID));
        }
        EdsEmployeeProfile edsProfile = profileManager.getProfile(edsEmployee.getObjectID());
        if (edsProfile != null) {
            NumberData numberData = generateEmployeeNumber(null);
            if (numberData != null) {
                edsProfile.setIntNumber(numberData.getIntNumber());
                String fullNumber = numberData.getNumberString() != null ? numberData.getNumberString() : "";
                if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                    edsProfile.setSavedNumberFormula(numberData.getSavedNumberFormula());
                } else {
                    edsProfile.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + ("".equals(numberData.getIntNumber()) ? "null" : decimalFormat.format(numberData.getIntNumber())) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
                }
                edsProfile.setEmployeeCode(fullNumber);
            }
            profileManager.update(edsProfile);
        }
        availabilityServiceLocal.createOrUpdateLeaveAllowance(edsEmployee.getObjectID());

        hrmsServiceLocal.createLabourPeriodToEmployee(edsEmployee, employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : null);


        return edsEmployee.getObjectID();
    }

    public Integer[] createEmployees(NewEmployee[] employees) {
        Integer[] result = new Integer[employees.length];
        int i = 0;
        for (NewEmployee employee : employees) {
            result[i] = createEmployeeInternal(employee, null);
            i++;
        }

        return result;
    }

    public Integer[] createEmployeesForQB(NewEmployee[] employees) {
        Integer[] result = new Integer[employees.length];
        int i = 0;
        for (NewEmployee employee : employees) {
            result[i] = createEmployeeInternal(employee, null);
            employeeManager.flushAndClear();
            i++;
        }

        return result;
    }

    @Override
    public ListResult<EmployeePayslipItem> getEmployeePayslips(ListingFilterParameter listingFilterParameter) {
        Integer totalCount = employeeManager.getEmployeePayslipCount(listingFilterParameter.getEmployeeId());
        List<EmployeePayslipItem> payslips = employeeManager.getEmployeePayslips(listingFilterParameter);
        if (payslips != null && !payslips.isEmpty()) {
            payslips.forEach(employeePayslipItem -> {
                if (employeePayslipItem.getStatusId() != null) {
                    EdsReference status = referenceManager.get(employeePayslipItem.getStatusId());
                    if (status != null) {
                        employeePayslipItem.setStatus(status.getName());
                    }
                }
            });
        }
        return new ListResult<EmployeePayslipItem>(new ArrayList<>(payslips), totalCount);
    }

    @Transactional
    public void createAssignUsersToGroups(EdsUser user) {
        EdsTrustee userTrustee = trusteeManager.getTrustee(user);
        user.getMembershipGroups().clear();
        if (userTrustee == null) {
            userTrustee = trusteeManager.getTrustee(user);
        }
        for (EdsRole role : user.getRoles()) {
            if (EdsRole.ADMIN.equals(role.getObjectID())) {
                EdsGroup admins = groupManager.getCompanyBuiltInGroup(EdsGroup.ADMINISTRATORS/*, company*/);
                user.getMembershipGroups().add(admins);
                admins.getMembers().add(userTrustee);
            } else {
                if (EdsRole.DR.equals(role.getObjectID())) {
                    EdsGroup directors = groupManager.getCompanyBuiltInGroup(EdsGroup.DIRECTORS/*, company*/);
                    user.getMembershipGroups().add(directors);
                    directors.getMembers().add(userTrustee);

                } else {
                    if (EdsRole.TL.equals(role.getObjectID())) {
                        EdsGroup departmentLeaders = groupManager.getCompanyBuiltInGroup(EdsGroup.DEPARTMENT_LEADERS/*, company*/);
                        user.getMembershipGroups().add(departmentLeaders);
                        departmentLeaders.getMembers().add(userTrustee);

                    } else {
                        if (EdsRole.ACCOUNTANT.equals(role.getObjectID())) {
                            EdsGroup accountants = groupManager.getCompanyBuiltInGroup(EdsGroup.ACCOUNTANTS/*, company*/);
                            user.getMembershipGroups().add(accountants);
                            accountants.getMembers().add(userTrustee);

                        } else {
                            if (EdsRole.ADMIN_LOCATION.equals(role.getObjectID())) {
                                EdsGroup adminLocations = groupManager.getCompanyBuiltInGroup(EdsGroup.ADMIN_LOCATIONS/*, company*/);
                                user.getMembershipGroups().add(adminLocations);
                                adminLocations.getMembers().add(userTrustee);

                            } else {
                                if (EdsRole.CALENDAR_EDITOR.equals(role.getObjectID())) {
                                    EdsGroup calendarEditors = groupManager.getCompanyBuiltInGroup(EdsGroup.CALENDAR_EDITORS/*, company*/);
                                    user.getMembershipGroups().add(calendarEditors);
                                    calendarEditors.getMembers().add(userTrustee);

                                } else {
                                    if (EdsRole.CALENDAR_VIEWER.equals(role.getObjectID())) {
                                        EdsGroup calendarViewers = groupManager.getCompanyBuiltInGroup(EdsGroup.CALENDAR_VIEWERS/*, company*/);
                                        user.getMembershipGroups().add(calendarViewers);
                                        calendarViewers.getMembers().add(userTrustee);

                                    } else {
                                        if (EdsRole.TIMESHEET_EDITOR.equals(role.getObjectID())) {
                                            EdsGroup timesheetEditors = groupManager.getCompanyBuiltInGroup(EdsGroup.TIMESHEET_EDITORS);
                                            user.getMembershipGroups().add(timesheetEditors);
                                            timesheetEditors.getMembers().add(userTrustee);

                                        } else {
                                            if (EdsRole.GUEST.equals(role.getObjectID())) {
                                                EdsGroup guest = groupManager.getCompanyBuiltInGroup(EdsGroup.GUEST);
                                                user.getMembershipGroups().add(guest);
                                                guest.getMembers().add(userTrustee);
                                            } else {
                                                if (EdsRole.SALESMAN.equals(role.getObjectID())) {
                                                    EdsGroup salesMen = groupManager.getCompanyBuiltInGroup(EdsGroup.SALESMEN);
                                                    if (salesMen != null) {
                                                        user.getMembershipGroups().add(salesMen);
                                                        salesMen.getMembers().add(userTrustee);
                                                    }

                                                } else {
                                                    if (EdsRole.SALESPERSON.equals(role.getObjectID())) {
                                                        EdsGroup salesPersons = groupManager.getCompanyBuiltInGroup(EdsGroup.SALESPERSONS);
                                                        if (salesPersons != null) {
                                                            user.getMembershipGroups().add(salesPersons);
                                                            salesPersons.getMembers().add(userTrustee);
                                                        }

                                                    } else {
                                                        if (EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE.equals(role.getObjectID())) {
                                                            EdsGroup customerServiceRepresentatives = groupManager.getCompanyBuiltInGroup(EdsGroup.CUSTOMER_SERVICE_REPRESENTATIVES/*, company*/);
                                                            user.getMembershipGroups().add(customerServiceRepresentatives);
                                                            customerServiceRepresentatives.getMembers().add(userTrustee);

                                                        } else {
                                                            if (EdsRole.HR.equals(role.getObjectID())) {
                                                                EdsGroup hrs = groupManager.getCompanyBuiltInGroup(EdsGroup.HRS/*, company*/);
                                                                user.getMembershipGroups().add(hrs);
                                                                hrs.getMembers().add(userTrustee);

                                                            } else {
                                                                if (EdsRole.MEM.equals(role.getObjectID())) {
                                                                    EdsGroup members = groupManager.getCompanyBuiltInGroup(EdsGroup.MEMBERS/*, company*/);
                                                                    user.getMembershipGroups().add(members);
                                                                    members.getMembers().add(userTrustee);
                                                                } else {
                                                                    if (EdsRole.PM.equals(role.getObjectID())) {
                                                                        EdsGroup projectManagers = groupManager.getCompanyBuiltInGroup(EdsGroup.PROJECT_MANAGERS/*, company*/);
                                                                        user.getMembershipGroups().add(projectManagers);
                                                                        projectManagers.getMembers().add(userTrustee);
                                                                    } else {
                                                                        if (EdsRole.CHAT_EXPERT.equals(role.getObjectID())) {
                                                                            EdsGroup projectManagers = groupManager.getCompanyBuiltInGroup(EdsGroup.CHAT_EXPERT/*, company*/);
                                                                            user.getMembershipGroups().add(projectManagers);
                                                                            projectManagers.getMembers().add(userTrustee);
                                                                        } else {
                                                                            if (EdsRole.CUSTOM_MEMBER.equals(role.getObjectID())) {
                                                                                EdsGroup projectManagers = groupManager.getCompanyBuiltInGroup(EdsGroup.CUSTOM_MEMBER/*, company*/);
                                                                                user.getMembershipGroups().add(projectManagers);
                                                                                projectManagers.getMembers().add(userTrustee);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PayrollSettings getEmployeeDetailsAndPayrollSettings(Integer employeeId, Date date) {
        PayrollSettings payrollSettings = new PayrollSettings();
        payrollSettings.setCurrencies(currencyService.getCurrencies(true));
        EdsEmployee employee = null;
        if (employeeId != null) {
            employee = employeeManager.get(employeeId);
        }
        payrollSettings.setRoleList(getRoles());
        boolean canAddNoAccessUsers = false;
        Integer[] count = getAllEmployeesMaxCount(null, null);
        if (count != null && count.length > 0) {
            canAddNoAccessUsers = count[1] > 0; //no-access employees count
        }

        List<EdsReference> employeeStatusList = referenceManager.listReferences(EMPLOYEE_STATUS);
        List<EdsReference> necessaryEmployeeStatusList = new ArrayList<>();
        String employeeStatusCode = employee != null ? employee.getAccountStatus().getCode() : referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING).getCode();//for training center -> instructor
        for (EdsReference status : employeeStatusList) {
            //we shouldn't add 'RESIGNED' as it's not possible to resign from edit view unless current status is 'RESIGNED' and
            //also shouldn't add 'NO ACCESS' status, in case such option is not enabled in generic settings
            if ((!EMPLOYEE_STATUS_RESIGNED.equals(employeeStatusCode) && EMPLOYEE_STATUS_RESIGNED.equals(status.getCode())) || (EMPLOYEE_STATUS_NO_ACCCESS.equals(status.getCode()) && !canAddNoAccessUsers)) {
                continue;
            }
            if (EMPLOYEE_STATUS_ACTIVE.equals(employeeStatusCode) && !EMPLOYEE_STATUS_PENDING.equals(status.getCode())) {
                //add ACTIVE, INACTIVE, RESIGNED, NO ACCESS
                necessaryEmployeeStatusList.add(status);
            } else if (EMPLOYEE_STATUS_INACTIVE.equals(employeeStatusCode) && !EMPLOYEE_STATUS_PENDING.equals(status.getCode())) {
                //add ACTIVE, INACTIVE, RESIGNED, NO ACCESS
                necessaryEmployeeStatusList.add(status);
            } else if (EMPLOYEE_STATUS_PENDING.equals(employeeStatusCode) && EMPLOYEE_STATUS_PENDING.equals(status.getCode())) {
                //add PENDING
                necessaryEmployeeStatusList.add(status);
            } else if (EMPLOYEE_STATUS_RESIGNED.equals(employeeStatusCode) && (EMPLOYEE_STATUS_RESIGNED.equals(status.getCode()) || EMPLOYEE_STATUS_ACTIVE.equals(status.getCode()))) {
                //add ACTIVE, RESIGNED
                necessaryEmployeeStatusList.add(status);
            } else if (EMPLOYEE_STATUS_NO_ACCCESS.equals(employeeStatusCode) && (!EMPLOYEE_STATUS_INACTIVE.equals(status.getCode()) && !EMPLOYEE_STATUS_PENDING.equals(status.getCode()))) {
                //add ACTIVE, RESIGNED, NO ACCESS
                necessaryEmployeeStatusList.add(status);
            }
        }

        payrollSettings.setUserLimit(count);
        payrollSettings.setStatusList(commonServiceLocal.reference2SelectItem(necessaryEmployeeStatusList, null));

        if (employeeId != null) {
            Integer[] roleIDs = new Integer[employee.getRoles().size()];
            int j = 0;
            for (EdsRole employeeRole : employee.getRoles()) {
                roleIDs[j] = employeeRole.getObjectID();
                if (ESS_USER_CODE.equals(employeeRole.getCode())) {
                    payrollSettings.setEss(true);
                }
                j++;
            }
            payrollSettings.setRoleId(roleIDs);
            payrollSettings.setStatusId(employee != null ? employee.getAccountStatus().getObjectID() : referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING).getObjectID());
            payrollSettings.setStatus(employee != null && employee.getAccountStatus() != null ? employee.getAccountStatus().getName() : "");
            payrollSettings.setStatusCode(employee != null && employee.getAccountStatus() != null ? employee.getAccountStatus().getCode() : "");

            payrollSettings.setEmployeeId(employeeId);
            payrollSettings.setEmployeeName(employee.getFullName());
            payrollSettings.setEmployeeFirstName(employee.getFirstName());
            payrollSettings.setEmployeeLastName(employee.getLastName());
            payrollSettings.setEmployeeEmail(employee.getEmail());
            payrollSettings.setPaymentMethod(employee.getPaymentMethod());
            if (employee.getProfile() != null && !"".equals(employee.getProfile().getEmployeeCode()) && employee.getProfile().getSavedNumberFormula() != null) {
                payrollSettings.setNumberData(generateEmployeeNumber(employee.getProfile().getObjectID()));
                payrollSettings.getNumberData().setNumberString(employee.getProfile().getEmployeeCode());
                payrollSettings.getNumberData().setIntNumber(employee.getProfile().getIntNumber());
            } else if (employee.getProfile().getEmployeeCode() != null && !"".equals(employee.getProfile().getEmployeeCode()) && employee.getProfile().getSavedNumberFormula() != null) {
                payrollSettings.setNumberData(generateEmployeeNumber(employee.getProfile().getObjectID()));
                payrollSettings.getNumberData().setNumberString(employee.getProfile().getEmployeeCode());
                payrollSettings.getNumberData().setIntNumber(employee.getProfile().getIntNumber());
            } else {
                payrollSettings.setNumberData(generateOldEmployeeNumber(employee.getProfile().getEmployeeCode(), employee.getProfile().getIntNumber()));
                payrollSettings.getNumberData().setNumberString(employee.getProfile().getEmployeeCode());
                payrollSettings.getNumberData().setIntNumber(employee.getProfile().getIntNumber());
            }

            payrollSettings.setStartDateForOnlyPayroll(employee.getStartDateForOnlyPayroll() != null ? new DateNonConvertable(employee.getStartDateForOnlyPayroll()) : null);
            payrollSettings.setResignationDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);
            payrollSettings.setPrevEndDate(employee.getPrevEndDate() != null ? new DateNonConvertable(employee.getPrevEndDate()) : null);
            payrollSettings.setStartDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
            payrollSettings.setDob(employee.getBirthDay() != null ? new DateNonConvertable(employee.getBirthDay()) : null);
            //LOAD EMPLOYEE'S PAYROLL SETTINGS
            payrollSettings.setPayrollSettings(getEmployeePayrollSettings(employeeId));

            if (!payrollSettings.getPayrollSettings().containsKey(SEX)) {
                payrollSettings.getPayrollSettings().put(SEX, employee.getProfile().getGender());
            }
            if (!payrollSettings.getPayrollSettings().containsKey(FAMILY_STATUS)) {
                if (employee.getProfile().getMartialStatus() != null) {
                    payrollSettings.getPayrollSettings().put(FAMILY_STATUS, employee.getProfile().getMartialStatus().getName());
                } else {
                    payrollSettings.getPayrollSettings().put(FAMILY_STATUS, "N/A");
                }
            }
            if (payrollSettings.getPayrollSettings().containsKey(SALARY_CATEGORY) && !"".equals(payrollSettings.getPayrollSettings().get(SALARY_CATEGORY))) {
                EdsPayrollCategory salaryCategory = categoryManager.get(Integer.parseInt(payrollSettings.getPayrollSettings().get(SALARY_CATEGORY)));
                if (salaryCategory != null) {
                    payrollSettings.setSalaryCategory(salaryCategory.createPaymentDeductionSelectItem());
                }
            } else {
                EdsPayrollCategory salaryCategory = categoryManager.getCategoryByCode(BASIC_SALARY);
                if (salaryCategory != null) {
                    payrollSettings.setSalaryCategory(salaryCategory.createPaymentDeductionSelectItem());
                }
            }
            List<EdsTimeSlotItem> timeSlotItems = timeSlotItemManager.getTimeSlotItems(employee.getTimeSlot());
            Integer sumMinutes = 0;
            for (EdsTimeSlotItem timeSlotItem : timeSlotItems) {
                sumMinutes += timeSlotItem.getEndTime() - timeSlotItem.getStartTime();
            }
            if (employee.getSalaryCurrency() != null) {
                payrollSettings.setSalaryCurrency(employee.getSalaryCurrency().createCurrencyItem());
            }
            EdsCountry citizenship = employee.getCitizenship();
            if (citizenship != null) {
                payrollSettings.setCitizenship(citizenship.getAsSelectItem());
            }
            if (employee.getPayMethod() != null) {
                payrollSettings.setPayMethod(employee.getPayMethod().getAsSelectItem());
            }
            payrollSettings.setEmployeePeriodMinuts(sumMinutes);
            payrollSettings.setSalaryMode(employee.getSalaryMode());

            List<EdsPaymentDeduction> categories = employee.getCategories();/*paymentDeductionManager.getEmployeeCategories(employeeId, Integer.parseInt(monthFormat.format(date)), Integer.parseInt(yearFormat.format(date)));*/
            if (categories != null && categories.size() > 0) {
                PaymentDeductionObject object;
                for (EdsPaymentDeduction category : categories) {
                    object = category.getRPC();
                    object.setUsed(payslipPaymentsManager.checkPaymentDeductionForUsed(category.getObjectID()));
                    if (category.getLinkedCategories() != null && category.getLinkedCategories().size() > 0) {
                        PaymentDeductionObject linkedObject;
                        for (EdsPayrollCategory linkedCategory : category.getLinkedCategories()) {
                            linkedObject = new PaymentDeductionObject();
                            linkedObject.setCategoryItem(linkedCategory.createPaymentDeductionSelectItem());
                            object.getLinkedCategories().add(linkedObject);
                        }
                    }
                    if (object.isPaymentCategory()) {
                        payrollSettings.getPaymentCategories().add(object);
                    } else if (object.isLoan()) {
                        if (!category.isFullPayed()) {
                            object.setRemainingAmount(category.getRemainingAmount());
                            payrollSettings.getLoanCategories().add(object);
                        }
                    } else if (object.isDeductionCategory()) {
                        payrollSettings.getDeductionCategories().add(object);
                    } else if (object.isTaxCategory()) {
                        payrollSettings.getTaxCategories().add(object);
                    } else if (object.isEmployerContributionCategory()) {
                        ArrayList<PaymentDeductionObject> employerContributions = new ArrayList<>();
                        employerContributions.add(object);
                        payrollSettings.setEmployerContributions(employerContributions);
                    }
                }
            }
        } else {
            EdsPayrollCategory salaryCategory = categoryManager.getCategoryByCode(BASIC_SALARY);
            if (salaryCategory != null) {
                payrollSettings.setSalaryCategory(salaryCategory.createPaymentDeductionSelectItem());
            }
            payrollSettings.setNumberData(generateEmployeeNumber(null));
        }
        return payrollSettings;
    }

    public void updateEmployeePaySettings(Integer employeeID, KeyValueStruct[] payrollSettings) {
        final EdsEmployee empl = employeeManager.get(employeeID);
        /* UPDATE PAYROLL SETTINGS */
        for (KeyValueStruct payrollSetting1 : payrollSettings) {
            if (payrollSetting1 == null || payrollSetting1.getValue() == null) {
                continue;
            }
            EdsEmployeePayrollSettings eps = employeePayrollSettingsManager.getEmployeeSettingValue(empl.getObjectID(), payrollSetting1.getKey());
            final boolean create = eps == null;
            if (eps == null) {
                eps = new EdsEmployeePayrollSettings();
            }
            eps.setEmployeeId(empl.getObjectID());
            eps.setKey(payrollSetting1.getKey());
            eps.setValue(payrollSetting1.getValue());
            if (create) {
                employeePayrollSettingsManager.create(eps);
            } else {
                employeePayrollSettingsManager.update(eps);
            }
        }
    }

    public Integer createPosition(NewPosition position) {

        if (positionManager.getByName(position.getName()) == null) {

            EdsReference referencePositionTitles = referenceManager.getByCode("POSITION_TITLES");
            ReferenceItem parentReferenceItem = referencePositionTitles != null ? referencePositionTitles.getRPC() : null;
            ReferenceItem childReferenceItem = new ReferenceItem();
            childReferenceItem.setName(position.getName());
            childReferenceItem.setParentID(parentReferenceItem.getObjectID());
            childReferenceItem.setParent(parentReferenceItem.getName());
            childReferenceItem.setParentCode(parentReferenceItem.getCode());

            Integer newPositionId = allInOneService.saveReference(childReferenceItem, null, true);
            EdsReference edsReference = referenceManager.get(newPositionId);

            EdsPosition pos = new EdsPosition();
            pos.setName(position.getName());
            pos.setDescription(position.getDescription());
            pos.setPositionName(edsReference);

            if (edsReference.getLocale() != null) {
                pos.setLocale(edsReference.getLocale());
            }

            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            Integer intNumber = positionManager.getPositionLastIntNumber();
            if (intNumber == null) {
                intNumber = 0;
            }
            NumberData numberData;
            if (settings != null && settings.getPositionNumberingFormat() != null) {
                numberData = settings.parseNumberDataForALL(intNumber, settings.getPositionNumberingFormat(), settings.getDelimetrPositionNumbering(), null, null, null, "position");
                numberData.setDelimiter(settings.getDelimetrPositionNumbering());
            } else {
                numberData = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_POSITION_PREFIX);
            }

            //position code
            if (numberData != null) {
                pos.setIntNumber(numberData.getIntNumber());
                pos.setNumberData(numberData.getNumberString());
            }

            EdsReference positionStatus = referenceManager.getByCode(Constants.POS_STATUS_ACTIVE);
            if (positionStatus == null) {
                pos.setStatus(positionStatus.getObjectID());
            }
            pos.setCreator(userManager.getUser());
            pos.setCreationTime(new Date());

            positionManager.create(pos);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pos, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_POSITION);

            try {
                positionSolrComponent.index(pos);
            } catch (Exception e) {
                log.error("Failed to index position id={}", pos.getObjectID(), e);
            }

            return pos.getObjectID();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRoles() {
        List<EdsRole> roles = getUserRolesByPattern(roleManager.list());
        SelectItem[] r = new SelectItem[roles.size()];
        int i = 0;
        for (EdsRole rol : roles) {
            if (!rol.getObjectID().equals(EdsRole.CLIENT)) {
                r[i] = new SelectItem();
                r[i].setId(rol.getObjectID());
                r[i].setName(commonLocalizer.localize(rol.getCode(), rol.getName()));
                i++;
            }
        }
        SelectItem[] ss = new SelectItem[i];
        for (int j = 0; j < i; j++) {
            if (r[j] != null) {
                ss[j] = r[j];
            }
        }
        return ss;
    }

    private List<EdsRole> getUserRolesByPattern(List<EdsRole> roles) {
        Integer[] sortRoleByPattern = null;
        EdsCompany company = userManager.getUser().getCompany();
        if (company.getObjectID().equals(8934) || company.getObjectID().equals(5377)) {
            sortRoleByPattern = new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.ADMIN_LOCATION,
                    EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.SALESPERSON, EdsRole.TL, EdsRole.PM,
                    EdsRole.MEM, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.CHAT_EXPERT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST};
        } else {
            sortRoleByPattern = new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.ADMIN_LOCATION,
                    EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.SALESPERSON, EdsRole.TL, EdsRole.PM,
                    EdsRole.MEM, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST};
        }
        List<EdsRole> userRoles = new ArrayList<>();
        for (Integer aSortRoleByPattern : sortRoleByPattern) {
            EdsRole rol = roleManager.get(aSortRoleByPattern);
            if (roles.contains(rol)) {
                userRoles.add(rol);
            }
        }
        return userRoles;
    }

    private List getRolesSortedByPattern(Set<EdsRole> roles) {
        return getRolesSortedByPattern(roles, new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT,
                EdsRole.ADMIN_LOCATION, EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.SALESPERSON,
                EdsRole.TL, EdsRole.PM, EdsRole.MEM, EdsRole.CLIENT, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST});
    }

    private List getRolesSortedByPattern(Set<EdsRole> roles, Integer[] sortPattern) {
        List<Integer> rolesList = new ArrayList<>();
        for (Integer aSortPattern : sortPattern) {
            if (roles.contains(roleManager.get(aSortPattern))) {
                rolesList.add(aSortPattern);
            }
        }
        for (EdsRole role : roles) {
            if (!rolesList.contains(role.getObjectID()) && (role.getDeleted() == null || !role.getDeleted()) && (role.getSystem() == null || !role.getSystem())) {
                rolesList.add(role.getObjectID());
            }
        }
        return rolesList;
    }

    @Override
    public LinkedHashMap<String, Double> getEmployeeByStatusChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();
        fp.setLimit(10000);
        ListResult<EmployeeListItem> employeelist = getEmployeeList(fp);
        Double i;
        if (employeelist != null) {
            for (EmployeeListItem employee : employeelist.getList()) {
                i = 1.0;
                result.merge(employee.getStatus(), i, Double::sum);
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<EmployeeListItem> getEmployees(ListingFilterParameter filterParametrs) {
        long begin = System.currentTimeMillis();

        //list panel tool rpc
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools == null) {
            List<String> columnCodeName = Arrays.asList(EmployeeListItem.FIRST_NAME, EmployeeListItem.LAST_NAME,
                    EmployeeListItem.PHONE_NUMBER, EmployeeListItem.EMAIL,
                    EmployeeListItem.POSITION, EmployeeListItem.LAST_UPDATE,
                    EmployeeListItem.STATUS, EmployeeListItem.LOCATION);
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(new ArrayList<String>(columnCodeName));
        }
        filterParametrs.setColumnsOfListing(panelTools.getColumnCodeName());
        if (panelTools.isCustomFieldsShown()) {
            filterParametrs.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Employee));
        }
        List<EdsEmployee> employees = employeeManager.list(filterParametrs);
        return createEmployeeList(filterParametrs, employees);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<EmployeeListItem> getEmployeeList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Employee list (from solr)");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        boolean isIntegerEmployeeCodeEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        fp.setCheckNumber(isIntegerEmployeeCodeEnabled);
        ServerUtils.kpiLog(log, kpiLog, isIntegerEmployeeCodeEnabled ? "Employee code INTEGER" : "Employee code STRING");
        FacetFilterRpc employeeFacetFilter = fp.getFacetFilter();
        if (employeeFacetFilter != null && !employeeFacetFilter.isFilterChanges()) {
            employeeFacetFilter = commonServiceLocal.getUserFacetFilter(employeeFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        if (Constants.FROM_TRAINING_CENTER.equals(fp.getViewType())) {
            EdsRole instructorRole = roleManager.getByCode(INSTRUCTOR_CODE);
            fp.setRoleID(instructorRole.getObjectID());
        }
        if (fp.getProjectId() != null) {
            ArrayList<Integer> employeeIDs = projectEmployeeManager.getEmployeeIDsByProject(fp.getProjectId());
            fp.setObjectIDs(employeeIDs);
        }

        if (fp.getLocationId() != null) {
            ArrayList<Integer> employeesIdByLocation = employeeManager.getLocationEmployees(fp.getLocationId());
            fp.setObjectIDs(employeesIdByLocation);
        }

        boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
        boolean showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_PROJECT_EMPLOYEE_LIST);
        boolean showDepartmentEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST);
        boolean showUnderEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS);
        if (!showAllEmployees && showUnderEmployees) {
            Set<Integer> childEmployeeIds = new HashSet<>();
            getDepartmentAndChildDepartmentEmployeeIds(edsUser, childEmployeeIds);
            if (childEmployeeIds.size() > 0) {
                String empIds = childEmployeeIds.stream().map(Objects::toString).collect(Collectors.joining(","));
                fp.setEmployeeIDs(empIds);
            }
        }
        if (fp.getModule() == null || "".equals(fp.getModule())) {
            fp.setModule(PermissionConstants.HRMS_CONTEXT);
        } else if (fp.getModule().equals(PermissionConstants.PM_CONTEXT)) {
            showAllEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_EMPLOYEE_LIST);
            showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_PROJECT_EMPLOYEE_LIST);
            showDepartmentEmployees = ServerUtils.hasPermission(PermissionConstants.PM_SHOW_DEPARTMENT_EMPLOYEE_LIST);
        } else if (fp.getModule().equals(PermissionConstants.PAYROLL_CONTEXT)) {
            showAllEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_ALL_EMPLOYEE_LIST);
            showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST);
            showDepartmentEmployees = ServerUtils.hasPermission(PermissionConstants.PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST);
        }
        if (!showAllEmployees && showProjectEmployees) {
            List<Integer> employeeIDs = projectManager.getPMManagedProjectsEmployeeIDs(edsUser.getObjectID());
            fp.setEmployeeIDs(ServerUtils.getAsCommoDelimited(employeeIDs, "0", " "));
        }
        List<Integer> departmentList = Lists.newArrayList();
        if (!showAllEmployees && showDepartmentEmployees) {
            List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(edsUser.getObjectID());
            departmentList.addAll(edsDepartments.stream().map(EdsDepartment::getObjectID).toList());
        }
        solrQuery.append(QueryBuilderForSolr.getEmployeeSolrQuery(fp, edsUser, departmentList));

        if (FROM_PAYROLL.equals(fp.getViewType())) {
            solrQuery.append(generatePermissionQueryForPayroll());
        }
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(employeeFacetFilter, edsUser.getCompany(), null, null));
        return getEmployeesListResponse(fp, edsUser, solrQuery.toString());
    }

    private void getDepartmentAndChildDepartmentEmployeeIds(EdsUser user, Set<Integer> allEmployeeIds) {
        EdsDepartment departmentByLeader = departmentManager.getDepartmentByLeader(user);
        if (departmentByLeader != null) {
            ArrayList<Integer> employeeIDsByTeamLeader = departmentManager.getEmployeeIDsByTeamLeader(user.getObjectID());
            allEmployeeIds.addAll(employeeIDsByTeamLeader);
            List<Integer> childList = departmentTreeManager.getAllChildList(departmentByLeader.getObjectID());
            if (childList != null && childList.size() > 0) {
                childList.forEach(ch -> {
                    List<EdsEmployee> teamEmployees = employeeDepartmentManager.getTeamEmployees2(ch);
                    if (teamEmployees != null && teamEmployees.size() > 0) {
                        teamEmployees.forEach(e -> allEmployeeIds.add(e.getObjectID()));
                    }
                });
            }
        }
    }

    private String generatePermissionQueryForPayroll() {
        EdsUser user = payrollBatchManager.getUser();

        if (roleManager.hasRole(user, EdsRole.DR) || roleManager.hasRole(user, EdsRole.ADMIN) || roleManager.hasRole(user, EdsRole.HR)
                || ServerUtils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_FULL_ACCESS)) {
            return "";
        }

        List<EdsPayrollBatch> groups = payrollBatchManager.getManagerPayrollGroups(user.getObjectID());

        StringBuilder query = new StringBuilder();

        if (groups != null && !groups.isEmpty()) {
            StringBuilder whereClause = new StringBuilder();

            for (EdsPayrollBatch g : groups) {
                if (!whereClause.isEmpty()) {
                    whereClause.append(" OR ");
                }
                whereClause.append(g.getObjectID());
            }
            query.append(" AND ").append(SolrEmployeeRepresenter.FIELD_PAYROLL_BATCH_ID).append(":(").append(whereClause).append(") ");
        }
        return query.toString();
    }

    public ListResult<EmployeeListItem> getEmployeesListResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
        Page<EmployeeSolrDoc> employeeSolrDocPage = employeeSolrComponent.getList(filterParameter, solrQuery);
        return getEmployeeFromSolrResult(employeeSolrDocPage, edsUser, filterParameter);
    }

    private EdsEmployeeExperienceItemTableCF saveExperienceItemCustomFields(EdsEmployeeExperienceItemTableCF edsItemCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsItemCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsItemCustomFields = new EdsEmployeeExperienceItemTableCF();
                employeeExperienceItemTableCFManager.createOrUpdate(edsItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsItemCustomFields, customFieldItems);
            return edsItemCustomFields;
        }
        return null;
    }

    private ListResult<EmployeeListItem> getEmployeeFromSolrResult(Page<EmployeeSolrDoc> employeeSolrDocPage, EdsUser user, ListingFilterParameter filterParameter) {
        ArrayList<EmployeeListItem> itemList = new ArrayList<>();
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        if (panelSettings == null) {
            panelSettings = ListPanelToolRpc.createIntance();
        }
        boolean addressEnabled = false;
        Map<Integer, EdsCountry> countries = null;
        if (panelSettings.getColumnCodeName() != null && (panelSettings.getColumnCodeName().contains(EmployeeListItem.COUNTRY) || panelSettings.getColumnCodeName().contains(EmployeeListItem.STATE)
                || panelSettings.getColumnCodeName().contains(EmployeeListItem.STREET) || panelSettings.getColumnCodeName().contains(EmployeeListItem.STREET2)
                || panelSettings.getColumnCodeName().contains(EmployeeListItem.POST_CODE))) {
            addressEnabled = true;
            countries = ServerUtils.getListAsMapIntegerAndValue(countryManager.list());
        }
        EdsCompany company = user.getCompany();
        int totalNumber = 0;
        boolean fromPayroll = filterParameter.getViewType() != null && filterParameter.getViewType().equals(FROM_PAYROLL);
        List<Integer> employeeIds = new ArrayList<>();
        HashMap<Integer, EmployeeSolrDoc> solrEmployeeMap = new LinkedHashMap<>();
        HashMap<Integer, BigDecimal> salaryMap = null;
        HashMap<Integer, String> employeeTemplateMap = null;
        HashMap<Integer, BigDecimal> paymentsTotalMap = null;
        HashMap<Integer, BigDecimal> deductionsTotalMap = null;
        HashMap<Integer, BigDecimal> loansTotalMap = null;
        Integer id;
        List<EmployeeSolrDoc> resultList = employeeSolrDocPage != null ? employeeSolrDocPage.getContent() : null;
        if (resultList != null) {
            totalNumber = (int) employeeSolrDocPage.getTotalElements();
            if (!filterParameter.isLookUp() && !filterParameter.isAllByFilter()) {
                for (EmployeeSolrDoc doc : resultList) {
                    if (doc != null) {
                        id = doc.getEmployeeId();
                        employeeIds.add(id);
                        solrEmployeeMap.put(id, doc);
                    }
                }
                if (!employeeIds.isEmpty()) {
                    salaryMap = employeePayrollSettingsManager.getEmployeeSalaryMap(ServerUtils.getAsCommoDelimited(employeeIds, "0", ","));
                    paymentsTotalMap = paymentDeductionManager.getEmployeeCategoriesTotal(ServerUtils.getAsCommoDelimited(employeeIds, "0", ","), PAYMENT);
                    deductionsTotalMap = paymentDeductionManager.getEmployeeCategoriesTotal(ServerUtils.getAsCommoDelimited(employeeIds, "0", ","), DEDUCTION);
                }
            }
            if (fromPayroll && !employeeIds.isEmpty()) {
                employeeTemplateMap = employeePayrollSettingsTemplateManager.getEmployeeAssignedTemplateMap(ServerUtils.getAsCommoDelimited(employeeIds, "0", ","));
                loansTotalMap = paymentDeductionManager.getEmployeeCategoriesTotal(ServerUtils.getAsCommoDelimited(employeeIds, "0", ","), LOAN);
            }
            List<String> customFieldsCodeForLocale = companyCustomFieldsManager.getCustomFieldsCodeForLocale();
            for (EmployeeSolrDoc relevantDoc : fromPayroll ? solrEmployeeMap.values() : resultList) {
                EmployeeListItem item = new EmployeeListItem();
                Integer employeeID = relevantDoc.getEmployeeId();
                item.setObjectID(employeeID);
                item.setFullName(relevantDoc.getEmployeeName());
                item.setMiddleName(relevantDoc.getMiddleName());
                item.setEmployeeNumber(filterParameter.isCheckNumber() ? String.valueOf(relevantDoc.getEmployeeIntegerNumber()) : relevantDoc.getEmployeeNumber());
                item.setEmail(relevantDoc.getEmail());
                item.setPhoneNumber(relevantDoc.getPhoneNumber());

                item.setDepartmentId(relevantDoc.getDepartmentId());
                item.setLocationId(relevantDoc.getLocationId());
                item.setPositionId(relevantDoc.getPositionId());
                String userLang = ServerUtils.getUserLocale().getLanguage();
                String department = null;
                String location = null;
                String position = null;
                String positionType = relevantDoc.getPositionTypeName();
                if (userLang != null && !userLang.isEmpty()) {
                    switch (userLang) {
                        case "uz" -> {
                            department = relevantDoc.getDepartmentNameUz() != null ? relevantDoc.getDepartmentNameUz() : relevantDoc.getDepartmentName();
                            location = relevantDoc.getLocationNameUz() != null ? relevantDoc.getLocationNameUz() : relevantDoc.getLocationName();
                            position = relevantDoc.getPositionNameUz() != null ? relevantDoc.getPositionNameUz() : relevantDoc.getPositionName();
                            positionType = relevantDoc.getPositionTypeNameUz() != null ? relevantDoc.getPositionTypeNameUz() : relevantDoc.getPositionTypeName();
                        }
                        case "ru" -> {
                            department = relevantDoc.getDepartmentNameRu() != null ? relevantDoc.getDepartmentNameRu() : relevantDoc.getDepartmentName();
                            location = relevantDoc.getLocationNameRu() != null ? relevantDoc.getLocationNameRu() : relevantDoc.getLocationName();
                            position = relevantDoc.getPositionNameRu() != null ? relevantDoc.getPositionNameRu() : relevantDoc.getPositionName();
                            positionType = relevantDoc.getPositionTypeNameRu() != null ? relevantDoc.getPositionTypeNameRu() : relevantDoc.getPositionTypeName();
                        }
                        case "en" -> {
                            department = relevantDoc.getDepartmentNameEn() != null ? relevantDoc.getDepartmentNameEn() : relevantDoc.getDepartmentName();
                            location = relevantDoc.getLocationNameEn() != null ? relevantDoc.getLocationNameEn() : relevantDoc.getLocationName();
                            position = relevantDoc.getPositionNameEn() != null ? relevantDoc.getPositionNameEn() : relevantDoc.getPositionName();
                            positionType = relevantDoc.getPositionTypeNameEn() != null ? relevantDoc.getPositionTypeNameEn() : relevantDoc.getPositionTypeName();
                        }
                        case "ar" -> {
                            department = relevantDoc.getDepartmentNameAr() != null ? relevantDoc.getDepartmentNameAr() : relevantDoc.getDepartmentName();
                            location = relevantDoc.getLocationNameAr() != null ? relevantDoc.getLocationNameAr() : relevantDoc.getLocationName();
                            position = relevantDoc.getPositionNameAr() != null ? relevantDoc.getPositionNameAr() : relevantDoc.getPositionName();
                            positionType = relevantDoc.getPositionTypeNameAr() != null ? relevantDoc.getPositionTypeNameAr() : relevantDoc.getPositionTypeName();
                        }
                    }
                    item.setDepartment(department);
                    item.setPosition(position);
                    item.setLocation(location);
                } else {
                    item.setDepartment(relevantDoc.getDepartmentName());
                    item.setPosition(relevantDoc.getPositionName());
                    item.setLocation(relevantDoc.getLocationName());
                }
                item.setStatusCode(relevantDoc.getStatusCode());
                if (filterParameter.isLookUp()) {
                    itemList.add(item);
                    continue;
                }
                item.setPayrolBatchIDs((ArrayList<Integer>) relevantDoc.getPayrollBatchId());
                item.setCurrency(new CurrencyItem(relevantDoc.getCurrencyId(), relevantDoc.getCurrencyName(), null, null));
                if (filterParameter.isAllByFilter()) {
                    itemList.add(item);
                    continue;
                }
                item.setFirstName(relevantDoc.getFirstName());
                item.setLastName(relevantDoc.getLastName());
                item.setMiddleName(relevantDoc.getMiddleName());
                item.setRole(ServerUtils.asListToString(relevantDoc.getRoleName()));
                item.setRoleCode(ServerUtils.asListToString(relevantDoc.getRoleCode()));
                item.setStatus(referenceWfmMessageSource.localize(relevantDoc.getStatusCode(), relevantDoc.getStatusName()));
                item.setStatusCode(relevantDoc.getStatusCode());
                item.setDriverNumber(relevantDoc.getDriverId());
                item.setPassportNumberField(relevantDoc.getPassportNumber());
                item.setPassportIssueIDField(relevantDoc.getPassportIssuedId());
                item.setPassportIssueNameField(relevantDoc.getPassportIssuedBy());
                Date passportIssueDate = relevantDoc.getPassportIssueDate();
                item.setPassportIssueDateField(passportIssueDate != null ? new DateNonConvertable(passportIssueDate) : null);
                Date passportExpiryDate = relevantDoc.getPassportExpireDate();
                item.setPassportExpiryDateField(passportExpiryDate != null ? new DateNonConvertable(passportExpiryDate) : null);
                item.setInsuranceNumberField(relevantDoc.getInsuranceNumber());
                Date insuranceExpiryDate = relevantDoc.getInsuranceExpiryDate();
                item.setInsuranceExpiryDate(insuranceExpiryDate != null ? new DateNonConvertable(insuranceExpiryDate) : null);
                item.setVisaNumberField(relevantDoc.getVisaNumber());
                Date visaIssueDate = relevantDoc.getVisaIssueDate();
                item.setVisaIssueDateField(visaIssueDate != null ? new DateNonConvertable(visaIssueDate) : null);
                Date visaExpiryDate = relevantDoc.getVisaExpireDate();
                item.setVisaExpiryDateField(visaExpiryDate != null ? new DateNonConvertable(visaExpiryDate) : null);
                item.setAgentName(relevantDoc.getAgentName());
                item.setBankNameString(relevantDoc.getBankName());
                item.setAccountNumberString(relevantDoc.getAccountNumber());
                item.setAccountNameString(relevantDoc.getAccountName());
                item.setBankAddressString(relevantDoc.getBankAddress());
                item.setSwiftBICCodeString(relevantDoc.getSwiftCode());
                item.setSortCodeString(relevantDoc.getSortCode());
                item.setiBANNumberString(relevantDoc.getIbanCode());
                item.setGenderName(relevantDoc.getGenderName());
                item.setOpeningBalanceDay(relevantDoc.getOpeningBalanceDays());
                item.setProbationDay(relevantDoc.getProbationDays());
                item.setSupervisorItem(new SelectItem(relevantDoc.getSupervisorId(), relevantDoc.getSupervisorName()));
                item.setQualificationId(relevantDoc.getQualificationId());
                item.setQualificationName(relevantDoc.getQualificationName());

                if (addressEnabled && (relevantDoc.getCountryName() != null || relevantDoc.getStateName() != null || relevantDoc.getStreet() != null || relevantDoc.getCity() != null || relevantDoc.getPostCode() != null)) {
                    Address address = new Address();
                    address.setCountryId(relevantDoc.getCountryId());
                    address.setStateId(relevantDoc.getStateId());
                    if (countries != null && address.getCountryId() != null) {
                        EdsCountry country = countries.get(address.getCountryId());
                        boolean timeZoneFound = false;
                        if (address.getStateId() != null && country != null) {
                            EdsRegion state = regionManager.get(address.getStateId());
                            if (state != null && state.getCountry() != null && state.getCountry().getObjectID().equals(country.getObjectID()) && state.getTimeZone() != null) {
                                address.setCountry(countryLocalizer.localize(country.getCode(), country.getName()) + regionManager.getStateTimeZoneAndPhoneCode(country, state));
                                timeZoneFound = true;
                            }
                        }
                        if (!timeZoneFound && country != null) {
                            address.setCountry(countryLocalizer.localize(country.getCode(), country.getName()) + countryManager.getCountryTimeZoneAndPhoneCode(country));
                        }
                    } else {
                        address.setCountry(countryLocalizer.localize(relevantDoc.getCountryCode(), relevantDoc.getCountryName()));
                    }
                    address.setState(relevantDoc.getStateName());
                    address.setStateId(relevantDoc.getStateId());
                    address.setAddress(relevantDoc.getStreet());
                    address.setAddressb(relevantDoc.getStreet2());
                    address.setCity(relevantDoc.getCity());
                    address.setZipCode(relevantDoc.getPostCode());
                    item.setPrimaryAddress(address);
                } else {
                    item.setPrimaryAddress(null);
                }

                if (filterParameter.isBriefly()) {
                    item.setSkills(ServerUtils.asListToString(relevantDoc.getSkillName()));
                    item.setWageRate(relevantDoc.getWageRate());
                    item.setClientChargeRate(relevantDoc.getClientChargeRate());
                }
                if (salaryMap != null) {
                    item.setSalaryAmount(salaryMap.get(employeeID));
                }
                if (paymentsTotalMap != null) {
                    item.setPaymentsTotal(paymentsTotalMap.get(employeeID));
                }
                if (deductionsTotalMap != null) {
                    item.setDeductionsTotal(deductionsTotalMap.get(employeeID));
                }
                if (fromPayroll) {
                    item.setLoansTotal(loansTotalMap.get(employeeID));
                    if (employeeTemplateMap.get(employeeID) != null) {
                        String data = employeeTemplateMap.get(employeeID);
                        item.setEmployeeTemplateID(Integer.valueOf(data.substring(0, data.indexOf("_"))));
                        item.setStatus(data.substring(data.indexOf("_") + 1));
                    }
                }
                BigDecimal paymentTotal = item.getPaymentsTotal() != null ? item.getPaymentsTotal() : BigDecimal.ZERO;
                BigDecimal deductionTotal = item.getDeductionsTotal() != null ? item.getDeductionsTotal() : BigDecimal.ZERO;
                BigDecimal totalSalary = item.getSalaryAmount() != null ? item.getSalaryAmount().add(paymentTotal).subtract(deductionTotal) : BigDecimal.ZERO;
                item.setTotalSalary(totalSalary);
                item.setLastUpdate(ServerUtils.shortDateFormat(relevantDoc.getLastUpdate(), company));
                Date birthDate = relevantDoc.getBirthDate();
                item.setBirthDate(birthDate != null ? new DateNonConvertable(birthDate) : null);
                Date startDate = relevantDoc.getHireDate();
                item.setStartDate(startDate != null ? new DateNonConvertable(startDate) : null);
                Date endDate = relevantDoc.getEndDate();
                item.setEnddate(endDate != null ? new DateNonConvertable(endDate) : null);
                HashMap<String, Object> map = CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName());
                if (map != null && !map.isEmpty() && customFieldsCodeForLocale != null) {
                    item.setCustomFieldsMap(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
                } else {
                    item.setCustomFieldsMap(map);
                }
                item.setContactID(relevantDoc.getContactId());
                item.setContactName(relevantDoc.getContactName());
                item.setTimeslot(new SelectItem(relevantDoc.getTimeslotId(), relevantDoc.getTimeslotName()));
                item.setPositionType(new SelectItem(relevantDoc.getPositionTypeId(), positionType));
                item.setMaritalStatusId(relevantDoc.getMartialStatusId());
                itemList.add(item);
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    @Override
    public LinkedHashMap<Double[], List<SelectItem>> getNewEmployeeJoiningRatio(ListingFilterParameter fp) {
        LinkedHashMap<Double[], List<SelectItem>> resultMap = new LinkedHashMap<>();
        List<SelectItem> result = new ArrayList<>();

        // current year employees
        fp.setStartDate(ServerUtils.getYearStartDate(fp.getSelectedYear()));
        if ((new Date()).getMonth() != fp.getSelectedMonth()) {
            fp.setEndDate(ServerUtils.getMonthEndDate(new Date(fp.getSelectedYear() - 1900, fp.getSelectedMonth(), 1)));
        } else {
            fp.setEndDate(DateUtil.getDayLastTime(new Date(fp.getSelectedYear() - 1900, fp.getSelectedMonth(), fp.getSelectedDay())));
        }
        ListResult<EmployeeListItem> newEmployeeList = getEmployeeList(fp);
        Integer newEmployeesCount = newEmployeeList.getTotal();
        // employee of previous years
        fp.setStartDate(null);
        Integer newTotalEmployeesCount = employeeManager.getEmployeesCountByHireDate(fp).intValue();

        //getting last year employees
        fp.setStartDate(ServerUtils.getYearStartDate(fp.getSelectedYear() - 1));
        fp.setEndDate(ServerUtils.getMonthEndDate(new Date(fp.getSelectedYear() - 1901, fp.getSelectedMonth(), 1)));
        Integer oldEmployeesCount = employeeManager.getEmployeesCountByHireDate(fp).intValue();
        // employees before last year
        fp.setStartDate(null);
        Integer oldTotalEmployeesCount = employeeManager.getEmployeesCountByHireDate(fp).intValue();

        // newTotalEmployeesCount and oldTotalEmployeesCount not zero;
        newTotalEmployeesCount = newTotalEmployeesCount != 0 ? newTotalEmployeesCount : 1;
        oldTotalEmployeesCount = oldTotalEmployeesCount != 0 ? oldTotalEmployeesCount : 1;

        double newEmployeeDiff = (newEmployeesCount * 100.0) / newTotalEmployeesCount;
        double oldEmployeeDiff = (oldEmployeesCount * 100.0) / oldTotalEmployeesCount;

        if (oldEmployeesCount == oldTotalEmployeesCount) {
            oldEmployeeDiff = 0;
        }
        if (newEmployeesCount == newTotalEmployeesCount) {
            newEmployeeDiff = 0;
        }
        if (newEmployeeList.getList().size() > 0) {
            for (EmployeeListItem item : newEmployeeList.getList()) {
                SelectItem employeeItem = new SelectItem();
                employeeItem.setId(item.getObjectID());
                employeeItem.setName(item.getFullName());
                if (item.getStartDate() != null) {
                    employeeItem.setDescription(ServerUtils.shortDateFormat(item.getStartDate().getNonConvertedDate(), userManager.getUser().getCompany()));
                }
                result.add(employeeItem);
            }
        }
        Double[] difference = new Double[2];
        difference[0] = newEmployeeDiff;
        difference[1] = newEmployeeDiff - oldEmployeeDiff;
        if (result.size() > 0) {
            resultMap.put(difference, result);
        }
        return resultMap;
    }

    @Override
    public void convertEmployeeToCandidate(HashSet<EmployeeListItem> selectedItems) {
        ArrayList<CompanyCustomFieldItem> employeeCfs = this.commonService.getCompanyCustomFields(ViewName.Employee);
        ArrayList<CompanyCustomFieldItem> candidateCfs = this.commonService.getCompanyCustomFields(ViewName.Candidate);
        EdsCrmCustomFields edsCrmCustomFields = new EdsCrmCustomFields();
        EdsUser user = userManager.getUser();
        EdsReference status = referenceManager.findReferenceByCode(ContactListItem.C_S_NEW);

        selectedItems.forEach(selectedItem -> {
            EdsEmployee employee = employeeManager.get(selectedItem.getObjectID());
            EdsCrmContact crmContact = new EdsCrmContact();
            crmContact.setFirstName(selectedItem.getFirstName());
            crmContact.setLastName(selectedItem.getLastName());
            crmContact.setMiddleName(selectedItem.getMiddleName());
            crmContact.setContactType(EdsCrmContact.CANDIDATE);
            crmContact.setCandidateStatus(status);
            crmContact.setDateOfBirth(selectedItem.getBirthDate() != null ? selectedItem.getBirthDate().getDate() : null);
            crmContact.setGender(selectedItem.getGenderName());
            crmContact.setPrimaryPhone(selectedItem.getPhoneNumber());
            crmContact.setCreator(user);
            crmContact.setPrimaryEmail(selectedItem.getEmail());

            if (selectedItem.getMaritalStatusId() != null) {
                crmContact.setMartialStatus(referenceManager.get(selectedItem.getMaritalStatusId()));
            }
            if (selectedItem.getLocation() != null) {
                crmContact.setPrefferedLocation(locationManager.get(selectedItem.getLocationId()));
            }

            HashMap<String, CompanyCustomFieldItem> candidateCfMap = CustomFieldsUtils.setRPCCustomFieldItems(new EdsCrmCustomFields(),
                            candidateCfs).stream()
                    .collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, Function.identity(), (k1, k2) -> k1, HashMap::new));
            List<CompanyCustomFieldItem> employeeCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), employeeCfs);

            employeeCustomFields.forEach(employeeCustomField -> {
                CompanyCustomFieldItem candidateCustomField = candidateCfMap.get(employeeCustomField.getAliasName());
                if (candidateCustomField != null && !ServerUtils.isNullOrEmpty(employeeCustomField.getFieldStringValue())) {
                    candidateCustomField.setFieldStringValue(employeeCustomField.getFieldStringValue());
                    candidateCustomField.setSelectedId(employeeCustomField.getSelectedId());
                    candidateCustomField.setFieldDateNonConvertedValue(employeeCustomField.getFieldDateNonConvertedValue());
                }
            });

            crmContact.setCustomFields(saveCustomFields(edsCrmCustomFields, new ArrayList<>(candidateCfMap.values())));
            NumberData numberData = allInOneServiceLocal.generateCandidateNumber(crmContact.getObjectID());
            crmContact.setNumber(numberData.getNumberString());
            crmContact.setNumberInteger(numberData.getIntNumber());
            crmContactManager.create(crmContact);


            if (selectedItem.getPhoneNumber() != null) {
                createContactItemParams(crmContact, EdsCrmContactItemParams.PHONE, selectedItem.getPhoneNumber());
            }
            if (selectedItem.getEmail() != null) {
                createContactItemParams(crmContact, EdsCrmContactItemParams.EMAIL, selectedItem.getEmail());
            }

            try {
                solrManager.addContactToIndex(crmContact);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private int levelSize = 0;

    @Override
    public Integer getLevelOfEmployees() {
        return levelSize;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getEmployeeGraphChart(boolean isShowView, Integer levelOptionList, boolean levelActive) {
        levelOptionList = levelOptionList != null ? levelOptionList : 2;
//        Boolean employeeGraphChartMapIsChanged = RedisClient.getKey("employeeGraphChartMapIsChanged_" + ServerSecurityContext.getInstance().getCompanyId(), Boolean.class);
//        if (RedisClient.getKey("EmployeeGraphChart_" + levelOptionList + "_level_" + ServerSecurityContext.getInstance().getCompanyId()) != null && employeeGraphChartMapIsChanged != null && !employeeGraphChartMapIsChanged) {
//            levelSize = RedisClient.getKey("EmployeeGraphChartMapSize_" + ServerSecurityContext.getInstance().getCompanyId(), Integer.class);
//            return RedisClient.getKey("EmployeeGraphChart_" + levelOptionList + "_level_" + ServerSecurityContext.getInstance().getCompanyId());
//        }

        List<EdsEmployee> parentEmployees = new ArrayList<>();
        Map<EdsEmployee, List<EdsEmployee>> employeeRelation = new HashMap<>();
        Map<Integer, List<EdsEmployee>> map = new LinkedHashMap<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setResignedEmployeesIncluded(false);
        fp.setShowAll(true);
        List<EdsEmployee> employees = employeeManager.list(fp);
        for (EdsEmployee employee : employees) {
            EdsEmployeeProfile profile = employee.getProfile();
            if (profile != null) {
                if (profile.getReportsTo() == null) {
                    parentEmployees.add(employee);
                } else {
                    if (employeeRelation.get(profile.getReportsTo()) != null) {
                        employeeRelation.get(profile.getReportsTo()).add(employee);
                    } else {
                        List<EdsEmployee> members = new ArrayList<>();
                        members.add(employee);
                        employeeRelation.put(profile.getReportsTo(), members);
                    }

                }
            }
        }
        for (EdsEmployee parent : parentEmployees) {
            int i = 1;
            if (employeeRelation.get(parent) != null) {
                orderLevel(i, map, parent, employeeRelation);
            }
        }

//        RedisClient.setKey("employeeGraphChartMapIsChanged_" + ServerSecurityContext.getInstance().getCompanyId(), false, Boolean.class);

        levelSize = map.size();

//        RedisClient.setKey("EmployeeGraphChartMapSize_" + ServerSecurityContext.getInstance().getCompanyId(), levelSize, Integer.class);

        List<EdsEmployee> levelOfEmployees = new ArrayList<>();

        for (int i = 1; i <= levelOptionList; i++) {
            levelOfEmployees.addAll(map.get(i));
        }

        if (levelActive) {
            parentEmployees.clear();
            employeeRelation.clear();
            for (EdsEmployee employee : levelOfEmployees) {
                EdsEmployeeProfile profile = employee.getProfile();
                if (profile != null) {
                    if (profile.getReportsTo() == null) {
                        parentEmployees.add(employee);
                    } else {
                        if (employeeRelation.get(profile.getReportsTo()) != null) {
                            employeeRelation.get(profile.getReportsTo()).add(employee);
                        } else {
                            List<EdsEmployee> members = new ArrayList<>();
                            members.add(employee);
                            employeeRelation.put(profile.getReportsTo(), members);
                        }

                    }
                }
            }
            levelActive = false;
        }

        StringBuilder htmlTeamOrgChart = new StringBuilder();
//        ListingFilterParameter filterParameter = new ListingFilterParameter();
//        filterParameter.setLimit(1000);
//        filterParameter.setBriefly(true);
//        List<Integer> memberId = (employeeManager.getEmployeeIds(new ListingFilterParameter()));
        htmlTeamOrgChart.append("<div class=\"spvrStructure\">");
        for (EdsEmployee parent : parentEmployees) {
            List<EdsEmployee> topMembers;
            if (!employeeRelation.isEmpty()) {
                topMembers = employeeRelation.get(parent);
            } else {
                topMembers = Collections.singletonList(parent);
            }
            if (topMembers != null) {
                int colspan = topMembers.size() * 2;
                // Tree Team Root
                String imageUrl = (parent.getPhoto() != null ? commonService.getImageUrl(parent.getPhoto().getObjectID()) : "");
                String gender = parent.getProfile().getGender();
                htmlTeamOrgChart.append("<table class=\"spvrStructureTable employee-gender-").append(gender).append("\" cellpadding=\"0\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tbody>");
                if (!isShowView) {
                    htmlTeamOrgChart.append(SupervisorStructureUtils.getEmployeeNodesRowForSuperVsr(colspan, parent.getObjectID(), parent.getName(),
                            (parent.getPosition() != null ? parent.getPosition().getName() : "&nbsp;"),
                            (parent.getTeam() != null ? parent.getTeam().getName() : "&nbsp;"),
                            imageUrl));
                } else {
                    htmlTeamOrgChart.append(SupervisorStructureUtils.getEmployeeNodesRowForSuperVsrVerticalView(colspan, parent.getObjectID(), parent.getName(),
                            (parent.getPosition() != null ? parent.getPosition().getName() : "&nbsp;"),
                            (parent.getTeam() != null ? parent.getTeam().getName() : "&nbsp;"),
                            imageUrl));
                }
                if (!topMembers.isEmpty() && !employeeRelation.isEmpty()) {
                    htmlTeamOrgChart.append(!isShowView ? SupervisorStructureUtils.getTeamTLinesRowForSuperVsr(colspan) : SupervisorStructureUtils.getTeamTLinesRowForSuperVsrVerticalView(colspan));
                    htmlTeamOrgChart.append(!isShowView ? SupervisorStructureUtils.getTeamVerticalLineRowForSuperVsr(colspan) : SupervisorStructureUtils.getTeamVerticalLineRowForSuperVsrVerticalView(colspan));
                    htmlTeamOrgChart.append("<tr>");
                    for (EdsEmployee child : topMembers) {
                        htmlTeamOrgChart.append("<td class=\"spvrStructure\" colspan=\"2\">").append(drawEmployeeChart(isShowView, child, employeeRelation)).append("</td>");
                    }
                }
                htmlTeamOrgChart.append("</tr>");
                htmlTeamOrgChart.append("</tbody></table>");
            }
        }
        htmlTeamOrgChart.append("</div>");

//        RedisClient.setKey("EmployeeGraphChart_" + levelOptionList + "_level_" + ServerSecurityContext.getInstance().getCompanyId(), htmlTeamOrgChart.toString());

        return htmlTeamOrgChart.toString();
    }

    private void orderLevel(int order, Map<Integer, List<EdsEmployee>> map, EdsEmployee employee, Map<EdsEmployee, List<EdsEmployee>> listMap) {
        if (map.get(order) == null) {
            List<EdsEmployee> emps = new ArrayList<>();
            emps.add(employee);
            map.put(order, emps);
        } else {
            map.get(order).add(employee);
        }
        order++;
        if (listMap.get(employee) != null) {
            for (EdsEmployee edsEmployee : listMap.get(employee)) {
                orderLevel(order, map, edsEmployee, listMap);
            }
        }
    }

    public int orgChartViewSize() {
        List<EdsEmployee> parentEmployees = new ArrayList<>();
        Map<EdsEmployee, List<EdsEmployee>> employeeRelation = new HashMap<>();
        Map<Integer, List<EdsEmployee>> map = new LinkedHashMap<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setResignedEmployeesIncluded(false);
        fp.setShowAll(true);
        List<EdsEmployee> employees = employeeManager.list(fp);

        for (EdsEmployee employee : employees) {
            EdsEmployeeProfile profile = employee.getProfile();
            if (profile != null) {
                if (profile.getReportsTo() == null) {
                    parentEmployees.add(employee);
                } else {
                    if (employeeRelation.get(profile.getReportsTo()) != null) {
                        employeeRelation.get(profile.getReportsTo()).add(employee);
                    } else {
                        List<EdsEmployee> members = new ArrayList<>();
                        members.add(employee);
                        employeeRelation.put(profile.getReportsTo(), members);
                    }

                }
            }
        }
        for (EdsEmployee parent : parentEmployees) {
            int i = 1;
            if (employeeRelation.get(parent) != null) {
                orderLevel(i, map, parent, employeeRelation);
            }
        }
        levelSize = map.size();
        return map.size();
    }

    private String drawEmployeeChart(boolean isShowView, EdsEmployee employee, Map<EdsEmployee, List<EdsEmployee>> employeeRelation) {
        StringBuilder htmlTeamOrgChart = new StringBuilder();
        EdsEmployeeProfile profile = employee.getProfile();
        if (employeeRelation == null || profile == null) {
            return htmlTeamOrgChart.toString();
        }
        List<EdsEmployee> members = employeeRelation.get(employee);
        int colspan = members != null ? members.size() * 2 : 0;

        String imageUrl = getImageUrl(employee.getPhoto());
        String gender = employee.getProfile().getGender();

        htmlTeamOrgChart.append("<table class=\"spvrNodes employee-gender-").append(gender).append("\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tbody>");

        int employeeId = ServerUtils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE) ? employee.getObjectID() : 0;
        if (!isShowView) {
            htmlTeamOrgChart.append(SupervisorStructureUtils.getEmployeeNodesRowForSuperVsr(colspan, (employee.getObjectID()), employee.getName(),
                    (employee.getPosition() != null ? employee.getPosition().getName() : "&nbsp;"),
                    (employee.getTeam() != null ? employee.getTeam().getName() : "&nbsp;"),
                    imageUrl));
        } else {
            htmlTeamOrgChart.append(SupervisorStructureUtils.getEmployeeNodesRowForSuperVsrVerticalView(colspan, employeeId, employee.getName(),
                    (employee.getPosition() != null ? employee.getPosition().getName() : "&nbsp;"),
                    (employee.getTeam() != null ? employee.getTeam().getName() : "&nbsp;"),
                    imageUrl));
        }

        if (profile.getReportsTo() == null || members == null) {
            return (htmlTeamOrgChart.append("</tbody></table>").toString());
        } else {
            htmlTeamOrgChart.append(!isShowView ? SupervisorStructureUtils.getTeamTLinesRowForSuperVsr(colspan) : SupervisorStructureUtils.getTeamTLinesRowForSuperVsrVerticalView(colspan));
            htmlTeamOrgChart.append(!isShowView ? SupervisorStructureUtils.getTeamVerticalLineRowForSuperVsr(colspan) : SupervisorStructureUtils.getTeamVerticalLineRowForSuperVsrVerticalView(colspan));

            htmlTeamOrgChart.append("<tr>");
            for (EdsEmployee childNode : members) {
                htmlTeamOrgChart.append("<td colspan=\"2\">").append(drawEmployeeChart(isShowView, childNode, employeeRelation)).append("</td>");
            }
            htmlTeamOrgChart.append("</tr>");
            return (htmlTeamOrgChart.append("</tbody></table>").toString());
        }
    }

    private String getImageUrl(EdsUpload image) {
        return uploadManager.getFileURL(image);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int checkEmployeeForApprovers(Integer employeeID) {
        int i = 0;
        List<EdsApproverEmployees> employeeses = approverEmployeeManager.listByEmployee(employeeID);
        if (employeeses.size() > 0) {
            i++;
        }
        EdsUser user = userManager.get(employeeID);
        if (user.hasRole(roleManager.get(EdsUser.ADMIN))) {
            List<EdsEmployee> otherAdmins = employeeManager.getCompanyOtherAdmins(employeeID);
            if (otherAdmins.size() == 0) {
                i = i + 2;
            }
        }
        return i;
    }

    @Override
    public SelectItem[] getEmployeesForShiftAsSelectItem(ListingFilterParameter filterParameter) {
        ArrayList<SelectItem> employeeList = new ArrayList<>();
        EdsEmployee currentEmployee = null;
        if (employeeManager.getUser() != null && employeeManager.getUser() instanceof EdsEmployee) {
            currentEmployee = (EdsEmployee) employeeManager.getUser();
        } else {
            return employeeList.toArray(new SelectItem[]{});
        }
        List<EdsEmployee> teamEmployees = null;
        if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_ALL)) {
            teamEmployees = employeeManager.getActiveEmployees(currentEmployee.getCompany());
        } else if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_BY_DEPARTMENT)) {
            teamEmployees = employeeManager.getActiveTeamEmployees(currentEmployee.getEmployeeDepartment().getTeam().getObjectID());
        } else if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_OWN)) {
            teamEmployees = new ArrayList<>();
            getChildEmployeeIds(currentEmployee.getObjectID(), teamEmployees);
            teamEmployees.add(currentEmployee);
        } else {
            teamEmployees = new ArrayList<>();
            teamEmployees.add(currentEmployee);

        }
        employeeList = (ArrayList<SelectItem>) teamEmployees.stream().map(e -> new SelectItem(e.getObjectID(), e.getFormmattedName())).collect(Collectors.toList());
        return employeeList.toArray(new SelectItem[]{});
    }

    @Override
    public List<EmployeeListItem> checkMultipleEmployeesForApprovers(List<EmployeeListItem> employees) {
        List<EmployeeListItem> list = new ArrayList<>();
        for (EmployeeListItem employee : employees) {
            int result = checkEmployeeForApprovers(employee.getObjectID());
            if (result > 0) {
                employee.setEmployeeCode(result);
                list.add(employee);
            }
        }
        return list;
    }

    @Override
    public Boolean checkPositionAvailability(Integer positionId) {
        EdsPosition position = positionManager.get(positionId);
        if (!ServerUtils.isNullOrEmpty(position.getCount())) {
            Integer positionCount = Integer.valueOf(position.getCount());
            return positionCount > employeeManager.getEmployeesCountByPosition(position);
        }
        return true;
    }

    @Override
    public Boolean checkPositionAvailability(ArrayList<Integer> positionIds) {
        return positionIds.stream()
                .map(this::checkPositionAvailability)
                .reduce(Boolean.TRUE, (a, b) -> a && b);
    }

    @Override
    public void updateEmployeeSocialData(Integer employeeID, String socialImageUrl) {
        EdsEmployee edsEmployee = employeeManager.get(employeeID);
        edsEmployee.setSocialImageUrl(socialImageUrl);
        employeeManager.update(edsEmployee);
    }

    @Override
    public List<ProfileItem> getEmployeeForVerification(String text) {
        List<EdsEmployee> employeesForVerification = employeeManager.getEmployeesForVerification(text);
        List<ProfileItem> profileItems = new ArrayList<>();
        for (EdsEmployee edsEmployee : employeesForVerification) {
            ProfileItem item = new ProfileItem();
            item.setObjectId(edsEmployee.getObjectID());
            item.setFirstName(edsEmployee.getFirstName());
            item.setLastName(edsEmployee.getLastName());
            item.setMiddleName(edsEmployee.getMiddleName());
            item.setPassportNumber(edsEmployee.getProfile().getPassportNumber());
            item.setEmpCode(edsEmployee.getProfile().getEmployeeCode());
            item.setStatus(this.referenceWfmMessageSource.localize(edsEmployee.getAccountStatus().getCode(), edsEmployee.getAccountStatus().getName()));
            if (edsEmployee.getPhoto() != null) {
                item.setEmployeeImageUrl(commonService.getImageUrl(edsEmployee.getPhoto().getObjectID()));
            }
            if (edsEmployee.getRejectionReason() != null) {
                item.setRejectionReason(referenceManager.get(edsEmployee.getRejectionReason()).getName());
            }
            profileItems.add(item);
        }
        return profileItems;
    }

    @Override
    public List<ProfileItem> getEmployeesByData(ProfileItem profileItem) {
        List<EdsEmployee> employeesForVerification = employeeManager.getEmployeesByData(profileItem);
        List<ProfileItem> profileItems = new ArrayList<>();
        for (EdsEmployee edsEmployee : employeesForVerification) {
            ProfileItem item = new ProfileItem();
            item.setObjectId(edsEmployee.getObjectID());
            item.setFirstName(edsEmployee.getFirstName());
            item.setLastName(edsEmployee.getLastName());
            item.setMiddleName(edsEmployee.getMiddleName());
            item.setPassportNumber(edsEmployee.getProfile().getPassportNumber());
            item.setEmpCode(edsEmployee.getProfile().getEmployeeCode());
            item.setStatus(this.referenceWfmMessageSource.localize(edsEmployee.getAccountStatus().getCode(), edsEmployee.getAccountStatus().getName()));
            if (edsEmployee.getPhoto() != null) {
                item.setEmployeeImageUrl(commonService.getImageUrl(edsEmployee.getPhoto().getObjectID()));
            }
            if (edsEmployee.getRejectionReason() != null) {
                item.setRejectionReason(referenceManager.get(edsEmployee.getRejectionReason()).getName());
            }
            profileItems.add(item);
        }
        return profileItems;
    }

    /**
     * Register employee editable row values
     *
     * @param rowValue       - row value
     * @param columnCodeName - column code name
     */
    public Integer saveEmployeeEditCellValue(EmployeeListItem rowValue, String columnCodeName) {
        try {
            EdsEmployee employee = employeeManager.get(rowValue.getObjectID());
            employee.clear();
            if (employee.getProfile() != null) {
                if (EmployeeListItem.PASSPORT_NUMBER.equals(columnCodeName)) {
                    employee.getProfile().setPassportNumber(rowValue.getPassportNumberField());
                } else if (EmployeeListItem.INSURANCE_NUMBER.equals(columnCodeName)) {
                    employee.getProfile().setInsuranceNumber(rowValue.getInsuranceNumberField());
                } else if (EmployeeListItem.VISA_NUMBER.equals(columnCodeName)) {
                    employee.getProfile().setVisaNumber(rowValue.getVisaNumberField());
                } else if (EmployeeListItem.PASSPORT_ISSUE_BY.equals(columnCodeName)) {
                    employee.getProfile().setCountry(countryManager.get(rowValue.getPassportIssueIDField()));
                } else if (EmployeeListItem.PASSPORT_ISSUE_DATE.equals(columnCodeName)) {
                    employee.getProfile().setPassportIssueDate(rowValue.getPassportIssueDateField() != null ? rowValue.getPassportIssueDateField().getNonConvertedDate() : null);
                } else if (EmployeeListItem.PASSPORT_EXPIRE_DATE.equals(columnCodeName)) {
                    employee.getProfile().setPassportExpiryDate(rowValue.getPassportExpiryDateField() != null ? rowValue.getPassportExpiryDateField().getNonConvertedDate() : null);
                } else if (EmployeeListItem.VISA_ISSUE_DATE.equals(columnCodeName)) {
                    employee.getProfile().setVisaIssueDate(rowValue.getVisaIssueDateField() != null ? rowValue.getVisaIssueDateField().getNonConvertedDate() : null);
                } else if (EmployeeListItem.VISA_EXPIRATION_DATE.equals(columnCodeName)) {
                    employee.getProfile().setVisaExpirationDate(rowValue.getVisaExpiryDateField() != null ? rowValue.getVisaExpiryDateField().getNonConvertedDate() : null);
                } else if (EmployeeListItem.START_DATE.equals(columnCodeName)) {
                    if ((rowValue.getStartDate() != null && employee.getStartDate() == null)
                            || (rowValue.getStartDate() == null && employee.getStartDate() != null)
                            || (rowValue.getStartDate() != null && employee.getStartDate() != null
                            && !rowValue.getStartDate().getNonConvertedDate().equals(employee.getStartDate()))) {
                        if (labourPeriodManager.isUsedEmployeeLabourPeriod(employee.getObjectID())) {
                            return Errors.EMPLOYEE_LABOUR_PERIOD_USED;
                        } else {
                            labourPeriodManager.clearEmployeeLabourPeriod(employee.getObjectID());
                            hrmsServiceLocal.createLabourPeriodToEmployee(employee, rowValue.getStartDate() != null ? rowValue.getStartDate().getNonConvertedDate() : null);
                        }
                    } else {
                        List<EdsLabourPeriod> periodsByEmployeeId = labourPeriodManager.periodListByEmployeeId(rowValue.getObjectID());
                        if (rowValue.getStartDate() != null && (periodsByEmployeeId == null || periodsByEmployeeId.size() == 0)) {
                            hrmsServiceLocal.createLabourPeriodToEmployee(employee, rowValue.getStartDate() != null ? rowValue.getStartDate().getNonConvertedDate() : null);
                        }
                    }
                    employee.setStartDate(rowValue.getStartDate() != null ? rowValue.getStartDate().getNonConvertedDate() : null);
                } else if (EmployeeListItem.END_DATE.equals(columnCodeName)) {
                    employee.setEndDate(rowValue.getEnddate() != null ? rowValue.getEnddate().getNonConvertedDate() : null);
                } else if (EmployeeListItem.INSURANCE_EXPIRY_DATE.equals(columnCodeName)) {
                    employee.getProfile().setMedicalInsuranceExDate(rowValue.getInsuranceExpiryDate() != null ? rowValue.getInsuranceExpiryDate().getNonConvertedDate() : null);
                } else if (EmployeeListItem.OPENING_BALANCE_DAYS.equals(columnCodeName)) {
                    employee.setOpeningBalanceDays(rowValue.getOpeningBalanceDay());
                } else if (EmployeeListItem.PROBATION_DAYS.equals(columnCodeName)) {
                    employee.setProbationDays(rowValue.getProbationDay());
                } else if (EmployeeListItem.BIRH_DATE.equals(columnCodeName)) {
                    if (rowValue.getBirthDate() != null) {
                        employee.getProfile().getContact().setDateOfBirth(rowValue.getBirthDate().getNonConvertedDate());
                        crmContactManager.update(employee.getProfile().getContact(), true);
                    }
                } else if (EmployeeListItem.GENDER_NAME.equals(columnCodeName)) {
                    employee.getProfile().setGender(rowValue.getGenderName());
                } else if (EmployeeListItem.PHONE_NUMBER.equals(columnCodeName)) {
                    if (employee.getContact() != null) {
                        employee.getContact().setPrimaryPhone(rowValue.getPhoneNumber());
                        if (employee.getContact().getItemParams(EdsCrmContactItemParams.PHONE) != null && employee.getContact().getItemParams(EdsCrmContactItemParams.PHONE).size() > 0 && employee.getContact().getItemParams(EdsCrmContactItemParams.PHONE).size() == 1) {
                            employee.getContact().getItemParams(EdsCrmContactItemParams.PHONE).get(0).setValue(rowValue.getPhoneNumber());
                        }
                    }
                } else if (EmployeeListItem.COUNTRY.equals(columnCodeName) || EmployeeListItem.STATE.equals(columnCodeName) || EmployeeListItem.STREET.equals(columnCodeName) ||
                        EmployeeListItem.STREET2.equals(columnCodeName) || EmployeeListItem.CITY.equals(columnCodeName) || EmployeeListItem.POST_CODE.equals(columnCodeName)) {
                    if (employee.getContact() != null) {
                        Address address = employee.getContact().getPrimaryAddressFromAll();
                        Integer addressID = address != null ? address.getObjectID() : null;
                        EdsAddress primaryAddress = addressID != null ? addressManager.get(addressID) : null;
                        if (primaryAddress == null) {
                            primaryAddress = new EdsAddress();
                            primaryAddress.setRelationType(EdsCrmContactItemParams.WORK);
                            primaryAddress.setContact(employee.getContact());
                            employee.getContact().getAddresses().add(primaryAddress);
                        }
                        if (EmployeeListItem.COUNTRY.equals(columnCodeName)) {
                            primaryAddress.setCountry(countryManager.get(rowValue.getPrimaryAddress().getCountryId()));
                            primaryAddress.setState(null);
                        } else {
                            if (EmployeeListItem.STATE.equals(columnCodeName) && primaryAddress.getCountry() != null) {
                                EdsRegion state = regionManager.getRegionByName(rowValue.getPrimaryAddress().getState());
                                if (state.getCountry().equals(primaryAddress.getCountry())) {
                                    primaryAddress.setState(state);
                                }
                            } else {
                                if (EmployeeListItem.STREET.equals(columnCodeName)) {
                                    primaryAddress.setAddress(rowValue.getPrimaryAddress().getAddress());
                                } else if (EmployeeListItem.CITY.equals(columnCodeName)) {
                                    primaryAddress.setCity(rowValue.getPrimaryAddress().getCity());
                                } else if (EmployeeListItem.STREET2.equals(columnCodeName)) {
                                    primaryAddress.setAddressb(rowValue.getPrimaryAddress().getAddressb());
                                } else if (EmployeeListItem.POST_CODE.equals(columnCodeName)) {
                                    primaryAddress.setZipCode(rowValue.getPrimaryAddress().getZipCode());
                                }
                            }
                        }
                    }
                } else if (EmployeeListItem.EMAIL.equals(columnCodeName)) {
                    if (EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS == checkUserName(rowValue.getEmail(), employee.getCompany().getObjectID())) {
                        return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
                    }
                    employee.setEmail(rowValue.getEmail());
                    if (employee.getContact() != null) {
                        String primaryEmailName = employee.getContact().getPrimaryEmail() == null ? employee.getContact().getPrimaryEmailFromAll() : employee.getContact().getPrimaryEmail();
                        if (primaryEmailName == null) {
                            EdsCrmContactItemParams param = new EdsCrmContactItemParams();
                            param.setValue(rowValue.getEmail());
                            param.setParam(EdsCrmContactItemParams.EMAIL);
                            param.setRelation(EdsCrmContactItemParams.WORK);
                            employee.getContact().getItemParams().add(param);
                        } else {
                            for (EdsCrmContactItemParams item : employee.getContact().getItemParams(EdsCrmContactItemParams.EMAIL)) {
                                if (primaryEmailName.equalsIgnoreCase(item.getValue())) {
                                    item.setValue(rowValue.getEmail());
                                    break;
                                }
                            }
                        }
                        employee.getContact().setPrimaryEmail(rowValue.getEmail());
                    }
                } else if (EmployeeListItem.SUPERVISOR.equals(columnCodeName)) {
                    EdsEmployee supervisor = null;
                    if (rowValue.getSupervisorItem() != null && rowValue.getSupervisorItem().getId() != null) {
                        supervisor = employeeManager.get(rowValue.getSupervisorItem().getId());
                        if (employee.getObjectID().equals(rowValue.getSupervisorItem().getId())) {
                            return 0;
                        }
                    }
                    employee.getProfile().setReportsTo(supervisor);
                    if (employee.getProfile().getContact() != null) {
                        employee.getProfile().getContact().setReportsToId(supervisor != null ? supervisor.getObjectID() : null);
                    }
                } else if (EmployeeListItem.POSITION.equals(columnCodeName)) {
                    EdsPosition position = null;
                    if (rowValue.getPositionId() != null) {
                        position = positionManager.get(rowValue.getPositionId());
                    }
                    employee.setPosition(position);
                } else if (EmployeeListItem.BANK_NAME.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                        userBankAccount.setUser(employee);
                    }
                    userBankAccount.setBankName(rowValue.getBankNameString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.ACCOUNT_NAME.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                        userBankAccount.setUser(employee);
                    }
                    userBankAccount.setAccountName(rowValue.getAccountNameString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.ACCOUNT_NUMBER.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                        userBankAccount.setUser(employee);
                    }
                    userBankAccount.setAccountNumber(rowValue.getAccountNumberString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.SWIFT_CODE.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                        userBankAccount.setUser(employee);
                    }
                    userBankAccount.setSwiftCode(rowValue.getSwiftBICCodeString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.SORT_CODE.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                        userBankAccount.setUser(employee);
                    }
                    userBankAccount.setSortCode(rowValue.getSortCodeString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.IBAN_CODE.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                        userBankAccount.setUser(employee);
                    }
                    userBankAccount.setIbanCode(rowValue.getiBANNumberString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.BANK_ADDRESS.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                    }
                    userBankAccount.setUser(employee);
                    userBankAccount.setBankAddress(rowValue.getBankAddressString());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.CURRENCY.equals(columnCodeName)) {
                    if (rowValue.getCurrency() != null) {
                        employee.setSalaryCurrency(currencyManager.get(rowValue.getCurrency().getId()));
                        if (employee.getPayrollBatches() != null && employee.getPayrollBatches().size() > 0) {
                            employee.getPayrollBatches().removeIf(batch -> !Objects.equals(batch.getCurrency(), employee.getSalaryCurrency()));
                        }
                    }
                } else if (EmployeeListItem.AGENT_ID.equals(columnCodeName)) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    if (userBankAccount == null) {
                        userBankAccount = new EdsUserBankAccount();
                    }
                    userBankAccount.setUser(employee);
                    userBankAccount.setAgentID(rowValue.getAgentName());
                    userBankAccountManager.createOrUpdate(userBankAccount);
                } else if (EmployeeListItem.LOCATION.equals(columnCodeName)) {
                    EdsLocation location = rowValue.getLocationId() != null ? locationManager.get(rowValue.getLocationId()) : null;
                    if (!Objects.equals(employee.getLocation(), location)) {
                        baseEventPostProcessor.registerEvent(LocationEventListenerImpl.TYPE, LocationEventListenerImpl.EMPLOYEE_LOCATION_CHANGE, location, employee);
                    }
                    if (location == null) {
                        employeeLocationManager.removeLocationHistory(employee);
                    } else {
                        employeeLocationManager.removeLocationHistory(employee, location);
                        EdsEmployeeLocation employeeLocation = new EdsEmployeeLocation();
                        employeeLocation.setUser(employee);
                        employeeLocation.setLocation(location);
                        employeeLocationManager.create(employeeLocation);
                    }
                    employee.setLocation(location);
                } else if (EmployeeListItem.WPS_NUMBER.equals(columnCodeName)) {
                    employeePayrollSettingsManager.update(employee, CustomFormConstants.WPS_NUMBER, rowValue.getWpsNumberString());
                } else if (EmployeeListItem.SALARY_AMOUNT.equals(columnCodeName)) {
                    employeePayrollSettingsManager.update(employee, SALARY, rowValue.getSalaryAmount().toString());
                } else if (EmployeeListItem.DEPARTMENT.equals(columnCodeName)) {
                    if (rowValue.getDepartmentId() != null) {
                        departmentService.saveEmployeeDepartment(new HashSet<>(Collections.singletonList(employee.getObjectID())), rowValue.getDepartmentId(), true, true, rowValue.getDeptStartDate(), false);
                    }
                } else if (EmployeeListItem.TIMESLOT.equals(columnCodeName)) {
                    EdsTimeSlot timeSlot = rowValue.getTimeslot() != null ? timeSlotManager.get(rowValue.getTimeslot().getId()) : null;
                    if (!Objects.equals(employee.getTimeSlot(), timeSlot)) {
                        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, null, userManager.getUser());
                        event.setSourceID(timeSlot.getObjectID());
                        event.setCustomStringField(employee.getObjectID().toString());
                    }
                    employee.setTimeSlot(timeSlot);
                } else {
                    //employee custom field
                    EdsEmployeeCustomFields edsEmployeeCustomFields = employee.getCustomFields();
                    if (edsEmployeeCustomFields == null) {
                        edsEmployeeCustomFields = new EdsEmployeeCustomFields();
                        employeeCFManager.create(edsEmployeeCustomFields);
                        employee.setCustomFields(edsEmployeeCustomFields);
                    }
                    Object ob = CustomFieldsUtils.getObjectValue(edsEmployeeCustomFields, columnCodeName);
                    if (ob != null) {
                        if (ob instanceof String text) {
                            if (!text.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                                employee.addChange(columnCodeName);
                            }
                        } else if (ob instanceof Number) {
                            String text = String.valueOf(((Double) ob).intValue());
                            if (!text.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                                employee.addChange(columnCodeName);
                            }
                        } else if (ob instanceof Date date) {
                            if (!date.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                                employee.addChange(columnCodeName);
                            }
                        }
                    } else {
                        employee.addChange(columnCodeName);
                    }
                    CustomFieldsUtils.setDomenObjectFieldChange(edsEmployeeCustomFields, rowValue.getCustomFieldsMap(), columnCodeName);
                }
            }
            employee.setLastUpdateTime(new Date());
            employee.setUpdater(userManager.getUser());
            try {
                employeeSolrComponent.index(employee);
            } catch (SolrServerException e) {
                log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
            } catch (IOException e) {
                log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
            }
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employee, employeeManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
        } catch (Exception e) {
            System.out.println("Employee List Edit Cell Column Code :" + columnCodeName);
        }
        return 1;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<DeparmentEmployees> getEmployeesWithTeams() {
        EdsUser user = employeeManager.getUser();
        ArrayList<DeparmentEmployees> result = new ArrayList<>();
        List<EdsDepartment> departments = departmentManager.getCompanyDepartments(user.getCompany());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        for (EdsDepartment department : departments) {
            DeparmentEmployees team = new DeparmentEmployees(department.getObjectID(), department.getName());
            fp.setDepartmentId(department.getObjectID());
            ArrayList<EmployeeListItem> members = new ArrayList<EmployeeListItem>(employeeManager.getEmployeeList(fp));
            team.setMembers(members);
            result.add(team);
        }
        return result;
    }

    private int checkUserName(String userName, Integer companyID) {
        try {
            // usernames by default all lowercased
            if (userManager.searchUserByUserName(userName.toLowerCase(), companyID) != null) {
                return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
            }
        } catch (Exception ignored) {
        }
        return EMPLOYEE_WITH_THIS_EMAIL_DOES_NOT_EXIST;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployeesAsSelectItems() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setResignedEmployeesIncluded(false);
        fp.setViewAsId(EdsRole.DR);
        List<EdsEmployee> employees = employeeManager.list(fp);

        ArrayList<SelectItem> res = new ArrayList<>();
        for (EdsEmployee employee : employees) {
            if (!employee.getDeleted()) {
                res.add(new SelectItem(employee.getObjectID(), employee
                        .getFullName()));
            }
        }
        return res.toArray(new SelectItem[]{});
    }

    @Override
    public SelectItem[] getEmployeesAaSelectItemsByDepartmentId(Integer departmentID) {
        List<EdsEmployee> employees = employeeManager.getEmployeesByDepartment(departmentID,true);
        ArrayList<SelectItem> res = new ArrayList<>();
        for (EdsEmployee employee : employees) {
            if (!employee.getDeleted()) {
                res.add(new SelectItem(employee.getObjectID(), employee
                        .getFullName()));
            }
        }
        return res.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployeesAsSelectItems(ListingFilterParameter fp) {
        fp.setViewAsId(EdsRole.DR);
        List<EdsEmployee> employees = employeeManager.list(fp);

        ArrayList<SelectItem> res = new ArrayList<>();
        for (EdsEmployee employee : employees) {
            if (!employee.getDeleted()) {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FULLNAME_FOR_CUSTOM_EMPLOYEE_LOOKUP) && employee.getMiddleName() != null) {
                    res.add(new SelectItem(employee.getObjectID(), employee
                            .getFullName() + " " + employee.getMiddleName()));
                } else {
                    res.add(new SelectItem(employee.getObjectID(), employee
                            .getFullName()));
                }

            }
        }
        return res.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployeesForPayroll() {
        List<EdsEmployee> employees = employeeManager.getEmployeesForPayroll();
        List<SelectItem> result = new ArrayList<>();

        for (EdsEmployee employee : employees) {
            if (!employee.getDeleted()) {
                result.add(new SelectItem(employee.getObjectID(), employee
                        .getFullName()));
            }
        }
        SelectItem[] selectItems = result.toArray(new SelectItem[]{});
        Arrays.sort(selectItems, Comparator.comparing(SelectItem::getName));
        return selectItems;
    }

    public Boolean changeToESSEmployee(Integer employeeID) {
        try {
            EdsEmployee employee = userManager.get(employeeID).getEmployee();
            if (employee != null) {
                employee.getRoles().clear();
                employee.getRoles().add(roleManager.getByCode(ESS_USER_CODE));
                employeeManager.update(employee);

                try {
                    employeeSolrComponent.index(employee);
                } catch (Exception e) {
                    log.error("", e);
                }

            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    public void activateOrDisactivateEmployee(Integer employeeID, Boolean activate) {
        activateOrDisactivateEmployee(employeeID, null, activate, false, true);
    }

    @Override
    public void activateOrDisactivateEmployee(Integer employeeID, Boolean activate, boolean indexSolr) {
        activateOrDisactivateEmployee(employeeID, null, activate, false, indexSolr);
    }

    private void activateOrDisactivateEmployee(Integer employeeID, Date date, Boolean activate, boolean deceased, boolean indexSolr) {
        try {
            EdsEmployee employee = userManager.get(employeeID).getEmployee();
            if (activate) {
                employee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
                employeeManager.update(employee);
//                messageManager.sendEmployeeActivationMessage(employee);
                signupMessageManager.sendEmployeeActivationMessage(employee);

                if (indexSolr) {
                    try {
                        employeeSolrComponent.index(employee);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                employee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE));
                employee.setEndDate(date);
                employee.setDeceased(deceased);
                employeeManager.update(employee);
                if (indexSolr) {
                    try {
                        employeeSolrComponent.index(employee);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean grantAccessToEmployee(Integer employeeID, Boolean grantAccess, boolean indexSolr) {
        return grantAccessToEmployee(employeeID, grantAccess, indexSolr, false);
    }

    public Boolean grantAccessToEmployeeWithEss(Integer employeeID, Boolean grantAccess, boolean isEss) {
        return grantAccessToEmployee(employeeID, grantAccess, true, isEss);
    }

    public Boolean grantAccessToEmployee(Integer employeeID, Boolean grantAccess, boolean indexSolr, Boolean isEss) {
        EdsUser user = employeeManager.getUser();
        boolean userNameExist = false;
        EdsEmployee empl = employeeManager.get(employeeID);
        Integer limit = checkUserLimit(isEss, grantAccess, null);
        if (limit < 0) {
            return false;
        }
        if (grantAccess) {
            String password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), empl.getEmail());
            if (password == null) {
                empl.setRandom(ServerUtils.randomstring());
                PasswordGenerator pg = new PasswordGenerator(6);
                password = pg.generateAsString();
                empl.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING));
            } else {
                userNameExist = true;
                //if isn't sent activation link to employee it must be active
                empl.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
            }
            empl.setPassword(password);
            employeeManager.update(empl);
            userManager.saveUserAuthenticationData(empl, empl.getCompany().getObjectID(), !userNameExist, indexSolr, false);
            if (userNameExist) {
                baseEventPostProcessor.registerEvent(ExistingEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
            } else {
                baseEventPostProcessor.registerEvent(NewEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, empl, user);
            }
            empl.getEmployeeDepartment().setDeleted(false);
            empl.setDeleted(false);
            empl.setRejectionReason(null);
            if (indexSolr) {
                try {
                    employeeSolrComponent.index(empl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else {
            empl.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_NO_ACCCESS));
            empl.getEmployeeDepartment().setDeleted(false);
            empl.setDeleted(false);
            employeeManager.update(empl);
            if (indexSolr) {
                try {
                    employeeSolrComponent.index(empl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }

    public Boolean grantAccessToEmployee(Integer employeeID, Boolean grantAccess) {
        return grantAccessToEmployee(employeeID, grantAccess, true);
    }

    public void resendActivationLink(Integer employeeID) {
        try {
            EdsUser admin = userManager.getUser();
            EdsEmployee employee = userManager.get(employeeID).getEmployee();
            messageManager.sendEmployeeAddActivationLink(employee, admin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getProjectEmployees(Integer projectID) {

        EdsUser user = employeeManager.getUser();
        EdsProject project = projectManager.get(projectID);
        byte permission = 0;
        if (project.getManager().getObjectID().equals(user.getObjectID()) || (project.isUserBackupManager(user.getObjectID()))
                || user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsUser.ADMIN))) {
            permission = EDIT;
            ProjectMember[] members = getProjectEmployeesWithTeams(projectID);
            if (members.length > 0) {
                members[0].setPermission(permission);
            }
            return members;
        } else {
            permission = READ;
            ProjectMember[] members = getProjectEmployeesWithTeams(projectID);
            if (members.length > 0) {
                members[0].setPermission(permission);
            }
            return members;
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getProjectEmployeeEditablePermmission(Integer projectId) {
        EdsUser user = employeeManager.getUser();
        EdsProject project = projectManager.get(projectId);
        if (user == null || project == null || project.getManager() == null) {
            return READ;
        }
        if (project.getManager().getObjectID().equals(user.getObjectID()) || (project.isUserBackupManager(user.getObjectID()))) {
            return EDIT;
        } else {
            return READ;
        }
    }

    public List<DepartmentDto> getDepartmentEmployees(String searchText, Integer start, Integer limit) {
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setSearchKey(searchText);
        listingFilterParameter.setStart(start);
        listingFilterParameter.setLimit(limit);
        List<DepartmentDto> departmentList = new ArrayList<>();
        List<EdsEmployeeDepartment> departments = departmentManager.getCompanyDepartments(listingFilterParameter);
        if (start >= departments.size()) {
            return new ArrayList<>();
        }
        departments = departments.subList(start, Math.min(start + limit, departments.size()));
        Map<EdsDepartment, List<EdsEmployee>> departmentListHashMap =
                departments.stream()
                        .collect(Collectors.groupingBy(
                                EdsEmployeeDepartment::getTeam,
                                Collectors.mapping(EdsEmployeeDepartment::getEmployee, Collectors.toList())
                        ));
        for (EdsDepartment department : departmentListHashMap.keySet()) {
            departmentList.add(new DepartmentDto(department.getObjectID(),
                    department.getName(),
                    EdsObject.getAsSelectItems(departmentListHashMap.getOrDefault(department, new ArrayList<>()))));
        }
        return departmentList;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getProjectEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> projectEmployees = new LinkedHashMap<>();

        EdsEmployee currentEmployee = null;
        if (employeeManager.getUser() != null && employeeManager.getUser() instanceof EdsEmployee) {
            currentEmployee = (EdsEmployee) employeeManager.getUser();
        } else {
            return projectEmployees;
        }

        List<EdsProjectEmployee> pemployees = null;

        if (projectID != null) { // this condition is needs to editing project view
            pemployees = projectManager.getEmployeesByProject(projectID);
        }

        boolean team;
        KpiTreeInfo employeeInfo;
        EdsDepartment department;

        if (projectID == null && !hasEmployeeAssignRole) {
            //user = employee
            department = currentEmployee.getTeam();
            employeeInfo = new KpiTreeInfo();
            if (currentEmployee.getProfile() != null && currentEmployee.getProfile().getEmployeeCode() != null) {
                employeeInfo.setName(currentEmployee.getProfile().getEmployeeCode() + " - " + currentEmployee.getName());
            } else {
                employeeInfo.setName(currentEmployee.getName());
            }
            employeeInfo.setId(currentEmployee.getObjectID());
            employeeInfo.setEmployeeId(currentEmployee.getObjectID());
            employeeInfo.setWageRate(currentEmployee.getWageRate());
            employeeInfo.setClientChargeRate(currentEmployee.getClientChargeRate());
            employeeInfo.setDepartmentId(department.getObjectID());
            employeeInfo.setDepartmentName(department.getName());
            employeeInfo.setSelected(true);

            KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
            ArrayList list = new ArrayList<>();
            list.add(employeeInfo);
            projectEmployees.put(departmentInfo, list);
            return projectEmployees;
        }

        List<Object[]> employeesWithDepartments;

        if (currentEmployee != null && currentEmployee.getCompany() != null && (currentEmployee.getCompany().getObjectID().equals(3465) || currentEmployee.getCompany().getObjectID().equals(25608) || currentEmployee.getCompany().getObjectID().equals(8687))) {
            if (currentEmployee.hasRole(roleManager.get(EdsRole.ADMIN)) || currentEmployee.hasRole(roleManager.get(EdsRole.DR))) {
                employeesWithDepartments = employeeManager.getEmployeesWithDepartment();
            } else {
                EdsLocation userLocation = null;
                if (currentEmployee.hasRole(roleManager.get(EdsRole.ADMIN_LOCATION))) {
                    userLocation = currentEmployee.getLocation();
                }
                employeesWithDepartments = employeeManager.getEmployeesWithDepartmentByDepartmentAndLocation(currentEmployee.getEmployeeTeam().getTeam(), userLocation);
            }
        } else {
            employeesWithDepartments = employeeManager.getEmployeesWithDepartment();
        }

        List<ProjectMember> members = projectEmployeeManager.getProjectEmployeesInfo(projectID);
        for (Object[] employeesWithDepartment : employeesWithDepartments) {
            team = false;
            employeeInfo = new KpiTreeInfo();

            EdsEmployee employee = (EdsEmployee) employeesWithDepartment[0];
            department = (EdsDepartment) employeesWithDepartment[1];

            if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                employeeInfo.setName(employee.getProfile().getEmployeeCode() + " - " + employee.getName());
            } else {
                employeeInfo.setName(employee.getName());
            }
            employeeInfo.setId(employee.getObjectID());
            employeeInfo.setEmployeeId(employee.getObjectID());
            employeeInfo.setWageRate(employee.getWageRate());
            employeeInfo.setClientChargeRate(employee.getClientChargeRate());

            employeeInfo.setDepartmentId(department.getObjectID());
            employeeInfo.setDepartmentName(department.getName());
            for (EdsRole role : employee.getRoles()) {
                employeeInfo.setEssRole(role.getCode().equals("ESS_USER"));
            }

            if (members != null && members.size() > 0) {
                for (ProjectMember mem : members) {
                    if (mem.getId().equals(employee.getObjectID())) {
                        employeeInfo.setTime(mem.getEstimatedTime());
                        employeeInfo.setTimeSpent(mem.getTimeSpent());
                        break;
                    }
                }
            }
            if (pemployees != null) {
                for (EdsProjectEmployee pe : pemployees) {
                    if (employee.getObjectID().equals(pe.getEmployeeDepartment().getEmployee().getObjectID())) {
                        employeeInfo.setClientChargeRate(pe.getClientChargeRate());
                        employeeInfo.setWageRate(pe.getWageRate());
                        employeeInfo.setWorkloadPercentage(pe.getWorkloadPercentage());
                        employeeInfo.setSelected(true);
                        employeeInfo.setProjectEmployeeId(pe.getObjectID());
                        break;
                    }
                }
            } else {
                if (employee.getObjectID().equals(currentEmployee.getObjectID())) {
                    employeeInfo.setSelected(true);
                }
            }

            for (KpiTreeInfo s : projectEmployees.keySet()) {
                if (s.getId().equals(department.getObjectID())) {
                    team = true;
                    projectEmployees.get(s).add(employeeInfo);
                    break;
                }
            }

            if (!team) {
                KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
                ArrayList list = new ArrayList<KpiTreeInfo>();
                list.add(employeeInfo);
                projectEmployees.put(departmentInfo, list);
            }
        }
        return projectEmployees;
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getBrigadaEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> projectEmployees = new LinkedHashMap<>();
        EdsEmployee currentEmployee = null;
        if (employeeManager.getUser() != null && employeeManager.getUser() instanceof EdsEmployee) {
            currentEmployee = (EdsEmployee) employeeManager.getUser();
        } else {
            return projectEmployees;
        }

        List<EdsBrigadaEmployee> pemployees = null;

        if (projectID != null) { // this condition is needs to editing project view
            pemployees = brigadaManager.getEmployeesByBrigada(projectID);
        }

        boolean team;
        KpiTreeInfo employeeInfo;
        EdsDepartment department;

        List<EdsEmployee> teamEmployees = null;
        if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_ALL)) {
            teamEmployees = employeeManager.getActiveEmployees(currentEmployee.getCompany());
        } else if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_BY_DEPARTMENT)) {
            teamEmployees = employeeManager.getActiveTeamEmployees(currentEmployee.getEmployeeDepartment().getTeam().getObjectID());
        } else if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_OWN)) {
            teamEmployees = new ArrayList<>();
            getChildEmployeeIds(currentEmployee.getObjectID(), teamEmployees);
            teamEmployees.add(currentEmployee);
        } else {
            teamEmployees = new ArrayList<>();
            teamEmployees.add(currentEmployee);
        }

        List<ProjectMember> members = projectEmployeeManager.getProjectEmployeesInfo(projectID);
        for (EdsEmployee employeesWithDepartment : teamEmployees) {
            team = false;
            employeeInfo = new KpiTreeInfo();

            EdsEmployee employee = employeesWithDepartment;
            department = employeesWithDepartment.getTeam();

            if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                employeeInfo.setName(employee.getProfile().getEmployeeCode() + " - " + employee.getName());
            } else {
                employeeInfo.setName(employee.getName());
            }
            employeeInfo.setId(employee.getObjectID());
            employeeInfo.setEmployeeId(employee.getObjectID());
            employeeInfo.setWageRate(employee.getWageRate());
            employeeInfo.setClientChargeRate(employee.getClientChargeRate());

            if (department != null) {
                employeeInfo.setDepartmentId(department.getObjectID());
                employeeInfo.setDepartmentName(department.getName());
            }
            for (EdsRole role : employee.getRoles()) {
                employeeInfo.setEssRole(role.getCode().equals("ESS_USER"));
            }

            if (members != null && members.size() > 0) {
                for (ProjectMember mem : members) {
                    if (mem.getId().equals(employee.getObjectID())) {
                        employeeInfo.setTime(mem.getEstimatedTime());
                        employeeInfo.setTimeSpent(mem.getTimeSpent());
                        break;
                    }
                }
            }
            if (pemployees != null) {
                for (EdsBrigadaEmployee pe : pemployees) {
                    if (employee.getObjectID().equals(pe.getEmployeeDepartment().getEmployee().getObjectID())) {
                        employeeInfo.setSelected(true);
                        employeeInfo.setProjectEmployeeId(pe.getObjectID());
                        employeeInfo.setUnit(pe.getUnit());
                        break;
                    }
                }
            }

            for (KpiTreeInfo s : projectEmployees.keySet()) {
                if (department != null && s.getId().equals(department.getObjectID())) {
                    team = true;
                    projectEmployees.get(s).add(employeeInfo);
                    break;
                }
            }

            if (!team && department != null) {
                KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
                ArrayList list = new ArrayList<KpiTreeInfo>();
                list.add(employeeInfo);
                projectEmployees.put(departmentInfo, list);
            }
        }
        return projectEmployees;
    }

    private void getChildEmployeeIds(Integer supervisorId, List<EdsEmployee> allEmployeeIds) {
        List<EdsEmployee> childEmployeeIds = employeeManager.getChildEmployeesObject(supervisorId);
        if (childEmployeeIds != null && !childEmployeeIds.isEmpty()) {
            for (EdsEmployee childId : childEmployeeIds) {
                allEmployeeIds.add(childId);
                getChildEmployeeIds(childId.getObjectID(), allEmployeeIds);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<KpiTreeInfo> getPositionEmployees(ListingFilterParameter fp, Map<Integer, HashMap<String, KpiTreeInfo>> map) {
        ArrayList list = new ArrayList<KpiTreeInfo>();
        Integer userID = employeeManager.getUser().getObjectID();
        EdsEmployee user = employeeManager.get(userID);

        //not get assigned employee to other position
        ArrayList<Integer> objectIDs = new ArrayList<>();
        /*if (!map.isEmpty()) {
            for (Integer positionID : map.keySet()) {
                if (!positionID.equals(fp.getPositionID())) {
                    objectIDs.addAll(map.get(positionID).keySet());
                }
            }
        }*/
        fp.setObjectIDs(objectIDs);
        fp.setBriefly(true);
        fp.setModule(PermissionConstants.PM_CONTEXT);
        ListResult<EmployeeListItem> employeeList = getEmployeeList(fp);

        if (employeeList == null) {
            return list;
        }
        KpiTreeInfo employeeInfo;
        for (EmployeeListItem employee : employeeList.getList()) {
            if (employee.getStatusCode() != null && EMPLOYEE_STATUS_RESIGNED.equals(employee.getStatusCode())) {
                continue;
            }

            employeeInfo = new KpiTreeInfo();

            employeeInfo.setId(employee.getObjectID());
            employeeInfo.setName(employee.getFullName());
            employeeInfo.setEmployeeNumber(employee.getEmployeeNumber());
            employeeInfo.setEmployeeId(employee.getObjectID());
            employeeInfo.setKey(UUID.randomUUID().toString());

            if (employee.getPosition() != null) {
                employeeInfo.setPositionId(employee.getPositionId());
                employeeInfo.setPositionName(employee.getPosition());
            }
            employeeInfo.setSkills(employee.getSkills());

            EdsProjectEmployee pe = projectEmployeeManager.getEmployeeLastAssignedProject(fp.getProjectId(), employee.getObjectID());
            if (pe != null) {
                employeeInfo.setLastContractDate(new DateNonConvertable(pe.getContractStartDate()));
                employeeInfo.setLastContractedProject(pe.getProject().getAsSelectItem());
                employeeInfo.setCreatedDate(pe.getCreationdate());
                if (pe.getPosition() != null) {
                    employeeInfo.setAssignedPositionName(pe.getPosition().getName());
                }
            }

            EdsProjectEmployee lastAssignedProject = projectEmployeeManager.getEmployeeLastAssignedProject(null, employee.getObjectID());
            if (lastAssignedProject != null) {
                employeeInfo.setCurrenctProjecs(lastAssignedProject.getProject().getName());
            }

            employeeInfo.setWageRate(employee.getWageRate());
            employeeInfo.setClientChargeRate(employee.getClientChargeRate());

            employeeInfo.setDepartmentId(employee.getDepartmentId());
            employeeInfo.setDepartmentName(employee.getDepartment());

            if (fp.getStartDate() != null) {
                if (employeeInfo.getAvailableFrom() == null) {
                    employeeInfo.setAvailableFrom(fp.getStartDate());
                    list.add(employeeInfo);
                } else if (fp.getStartDate().compareTo(employeeInfo.getAvailableFrom()) >= 0) {
                    list.add(employeeInfo);
                }
            } else {
                list.add(employeeInfo);
            }
        }
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getProjectEmployeesWithTeams(Integer projectID) {
        Integer userID = employeeManager.getUser().getObjectID();
        EdsEmployee user = employeeManager.get(userID);
        List<EdsEmployee> employees = null;

        if (user.getCompany().getObjectID().equals(25608) || user.getCompany().getObjectID().equals(8687)) {
            if (user.hasRole(roleManager.get(EdsRole.ADMIN)) || user.hasRole(roleManager.get(EdsRole.DR))) {
                employees = employeeManager.getEmployees(user.getCompany());
            } else {
                EdsLocation userLocation = null;
                if (user.hasRole(roleManager.get(EdsRole.ADMIN_LOCATION))) {
                    userLocation = user.getLocation();
                }
                employees = employeeManager.getTeamLocationEmployees(user.getEmployeeTeam().getTeam(), userLocation);
            }
        } else {
            employees = employeeManager.getEmployees(user.getCompany());
        }
        Integer defaultDepartmentid = user.getCompany().getDefaultDepartment() != null ? user.getCompany().getDefaultDepartment().getObjectID() : null;

        ProjectMember[] result = new ProjectMember[employees.size()];
        Map<Integer, ProjectMember> pMembers = new HashMap<>();
        if (projectID != null) {
            ProjectMember[] projectMembers = getProjectMembers(projectID, null);
            for (ProjectMember pM : projectMembers) {
                pMembers.put(pM.getId(), pM);
            }
        }

        int t = 0;

        for (EdsEmployee employee : employees) {
            if (pMembers.containsKey(employee.getObjectID())) {
                result[t] = pMembers.get(employee.getObjectID());
                if (employee.getEmployeeDepartment().getTeam().getName() != null) {
                    result[t].setDepartmentId(employee.getEmployeeDepartment().getTeam().getObjectID());
                    result[t].setTeamName(employee.getEmployeeDepartment().getTeam().getName());
                }

            } else {
                String teamName = "";
                Integer teamId = null;
                if (employee.getEmployeeDepartment().getTeam().getName() != null) {
                    teamName = employee.getEmployeeDepartment().getTeam().getName();
                    teamId = employee.getEmployeeDepartment().getTeam().getObjectID();
                }

                result[t] = new ProjectMember();
                result[t].setId(employee.getObjectID());
                if (defaultDepartmentid != null) {
                    result[t].setDefaulDepartmentId(defaultDepartmentid);
                }
                result[t].setDepartmentId(teamId);
                result[t].setName(employee.getName());
                result[t].setWageRate(employee.getWageRate());
                result[t].setClientChargeRate(employee.getClientChargeRate());
                result[t].setTeamName(teamName);
                result[t].setCheck(false);
            }
            t++;
        }
        return result;
    }

    @Override
    public ProjectMember getProjectMemberByEmployee(Integer employeeID) {
        EdsEmployee employee = employeeManager.get(employeeID);
        EdsUser user = employeeManager.getUser();
        Integer defaultDepartmentid = user.getCompany().getDefaultDepartment() != null ? user.getCompany().getDefaultDepartment().getObjectID() : null;
        ProjectMember result = null;
        if (employee != null) {
            String teamName = "";
            Integer teamId = null;
            if (employee.getEmployeeDepartment().getTeam().getName() != null) {
                teamName = employee.getEmployeeDepartment().getTeam().getName();
                teamId = employee.getEmployeeDepartment().getTeam().getObjectID();
            }

            result = new ProjectMember();
            result.setId(employee.getObjectID());
            if (defaultDepartmentid != null) {
                result.setDefaulDepartmentId(defaultDepartmentid);
            }
            result.setDepartmentId(teamId);
            result.setName(employee.getName());
            result.setWageRate(employee.getWageRate());
            result.setClientChargeRate(employee.getClientChargeRate());
            result.setTeamName(teamName);
            result.setCheck(false);
        }
        return result;
    }

    @Override
    public HashSet<String> getEmployeeSpecificPermission(Integer employeeID, String sectionContext) {
        EdsUser user = employeeManager.getUser();
        EdsEmployee employee = employeeManager.get(employeeID);
        if (employee.getTeam().getLeader() != null && user.getObjectID().equals(employee.getTeam().getLeader().getObjectID())) {
            user.addArtificialRole(roleManager.getByCode(Constants.DLOFPR));
        }
        return rolePermissionServiceLocal.getPermissionList(sectionContext, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getProjectMembers(Integer projectId, Integer employeeId) {
        Integer defaultDepartmentId = projectManager.getUser().getCompany().getDefaultDepartment().getObjectID();
        List<EdsProjectEmployee> pemployees = projectManager.getEmployeesByProject(projectId, employeeId);
        List<ProjectMember> members = new ArrayList<>();

        for (EdsProjectEmployee member : pemployees) {
            ProjectMember mem = new ProjectMember();
            EdsEmployee employee = member.getEmployeeDepartment().getEmployee();
            if (employee != null) {
                mem.setName(employee.getName());
                mem.setId(employee.getObjectID());
                if (employee.getProfile() != null) {
                    mem.setEmployeeNumber(employee.getProfile().getEmployeeCode());
                }
            }
            mem.setProjectEmployeeId(member.getObjectID());
            mem.setWageRate(member.getWageRate());
            mem.setClientChargeRate(member.getClientChargeRate());
            mem.setWorkloadPercentage(member.getWorkloadPercentage());
            mem.setDefaulDepartmentId(defaultDepartmentId);
            mem.setContractStart(member.getContractStartDate() != null ? new DateNonConvertable(member.getContractStartDate()) : null);
            mem.setContractEnd(member.getContractEndDate() != null ? new DateNonConvertable(member.getContractEndDate()) : null);

            EdsEmployeeDepartment department = member.getEmployeeDepartment();
            if (department != null) {
                if (department.getTeam() != null && department.getTeam().getDeleted() != null && !department.getTeam().getDeleted()) {
                    mem.setDepartmentId(department.getTeam().getObjectID());
                    mem.setTeamName(department.getTeam().getName());
                } else {
                    EdsDepartment edsDepartment = departmentManager.get(defaultDepartmentId);
                    mem.setDepartmentId(defaultDepartmentId);
                    mem.setTeamName(edsDepartment.getName());
                }
            }
            if (member.getPosition() != null) {
                mem.setPositionId(member.getPosition().getObjectID());
            }
            mem.setCheck(true);
            members.add(mem);
        }
        return members.toArray(new ProjectMember[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getProjectMembersAll(Integer projectId) {
        Integer defaultDepartmentId = projectManager.getUser().getCompany().getDefaultDepartment().getObjectID();
        List<EdsProjectEmployee> pemployees = projectManager.getEmployeesByProjectAll(projectId);
        List<ProjectMember> members = new ArrayList<>();
        for (EdsProjectEmployee member : pemployees) {
            ProjectMember mem = new ProjectMember();
            EdsEmployee employee = member.getEmployeeDepartment().getEmployee();
            if (employee != null) {
                mem.setName(employee.getName());
                mem.setId(employee.getObjectID());
                if (employee.getProfile() != null) {
                    mem.setEmployeeNumber(employee.getProfile().getEmployeeCode());
                }
            }
            mem.setProjectEmployeeId(member.getObjectID());
            mem.setWageRate(member.getWageRate());
            mem.setClientChargeRate(member.getClientChargeRate());
            mem.setWorkloadPercentage(member.getWorkloadPercentage());
            mem.setDefaulDepartmentId(defaultDepartmentId);
            mem.setDeleted(member.getDeleted());

            EdsEmployeeDepartment department = member.getEmployeeDepartment();
            if (department != null) {
                if (department.getTeam() != null && department.getTeam().getDeleted() != null && !department.getTeam().getDeleted()) {
                    mem.setDepartmentId(department.getTeam().getObjectID());
                    mem.setTeamName(department.getTeam().getName());
                } else {
                    EdsDepartment edsDepartment = departmentManager.get(defaultDepartmentId);
                    mem.setDepartmentId(defaultDepartmentId);
                    mem.setTeamName(edsDepartment.getName());
                }
            }
            mem.setCheck(true);
            members.add(mem);
        }
        return members.toArray(new ProjectMember[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DepartmentItem[] getDepartmentsSelectItem() {
        return departmentService.getDepartmentsSelectItem();
    }

    @Override
    @Transactional
    public boolean setEmployeeLocation(Integer employeeID, Double latitude, Double longitude) {
        EdsEmployee employee = (employeeID != null) ? employeeManager.get(employeeID) : employeeManager.getUser().getEmployee();
        employee.setLatitude(latitude);
        employee.setLongitude(longitude);
        return true;
    }

    /* Employee where team leader get objects*/

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TeamEmployee getTeamByEmployeeId(Integer employeeId) {
        List<EdsDepartment> teams = departmentManager.getTeamsByEmployeeId(employeeId);
        Integer[] objectId = new Integer[teams.size()];
        StringBuilder teamNames = new StringBuilder();
        int k = 0;
        for (EdsDepartment team : teams) {
            if (!teamNames.toString().equals("") && k < 5) {
                teamNames.append(", ");
            }
            if (k < 5) {
                teamNames.append(team.getName());
            }
            objectId[k++] = team.getObjectID();
        }
        TeamEmployee teamEmployee = new TeamEmployee();
        teamEmployee.setTeamNames(teamNames.toString());
        teamEmployee.setObjectId(objectId);
        return teamEmployee;
    }

    /* Employee where manager get objects */

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TeamEmployee getManagerByEmployeeId(Integer employeeId) {
        List<EdsProject> managers = projectManager.getProjectManagersByEmployeeId(employeeId, true);
        Integer[] objectId = new Integer[managers.size()];
        StringBuilder managerNames = new StringBuilder();
        int k = 0;
        for (EdsProject project : managers) {
            if (!managerNames.toString().equals("") && k < 5) {
                managerNames.append(", ");
            }
            if (k < 5) {
                managerNames.append(project.getName());
            }
            objectId[k++] = project.getObjectID();
        }
        TeamEmployee managerEmployee = new TeamEmployee();
        managerEmployee.setTeamNames(managerNames.toString());
        managerEmployee.setObjectId(objectId);
        return managerEmployee;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getEmployeesMaxCount() {
        EdsCompany company = employeeManager.getUser().getCompany();
        Long employeeCount = employeeManager.getEmployeesCountByCompany(null, true, true);
        return company.getEmployeeMaxCount(employeeCount != null ? employeeCount.intValue() : 0, company);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer[] getAllEmployeesMaxCount(Integer companyID, Integer exceptEmployee) {
        EdsCompany company;
        if (companyID != null) {
            company = companyManager.get(companyID);
        } else {
            company = employeeManager.getUser().getCompany();
        }
        Long employeeCount = employeeManager.getEmployeesCountByCompany(null, true, true);
        Long noAccessCount = employeeManager.getNoAccessEmployeesCountByCompany();
        Long essUserCount = employeeManager.getEmployeeCountByRoleCodeExceptByUserId(EdsRole.ESS_USER_CODE, exceptEmployee);
        return company.getAllEmployeeMaxCount(employeeCount != null ? employeeCount.intValue() : 0, noAccessCount != null ? noAccessCount.intValue() : 0, essUserCount.intValue(), company);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getEmployeesMaxCountByCompanyId(Integer companyId) {
        EdsCompany company = companyManager.get(companyId);
        Long employeeCount = employeeManager.getEmployeesCountByCompany(null, true, true);
        return company.getEmployeeMaxCount(employeeCount != null ? employeeCount.intValue() : 0, company);
    }

    public Integer[] getUserLimit() {
        EdsCompany company = employeeManager.getUser().getCompany();
        Integer employeeCount = getEmployeesMaxCount();
        Integer maxUsers = company.getUsegePlanMaxUsers(company);

        return new Integer[]{employeeCount != null ? employeeCount.intValue() : 0, maxUsers};
    }

    @Override
    public EmployeeViewItem getEmployeeByDriverNumber(Long driverNumber) {
        EmployeeViewItem result = null;
        EdsEmployee employee = employeeManager.getEmployeeByDriverNumber(driverNumber);
        if (employee != null) {
            result = getEmployee(employee.getObjectID());
        }
        return result;
    }

    @Override
    public Integer getEmployeeIdByDriverNumber(Long driverNumber) {
        Integer employeeId = null;

        EdsEmployee employee = employeeManager.getEmployeeByDriverNumber(driverNumber);
        if (employee != null) {
            employeeId = employee.getObjectID();
        }
        return employeeId;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, String> getEmployeePayrollSettings(Integer employeeID) {
        List<EdsEmployeePayrollSettings> items = employeePayrollSettingsManager.getEmployeeSettings(employeeID);
        HashMap<String, String> res = new HashMap<>();
        for (EdsEmployeePayrollSettings item : items) {
            res.put(item.getKey(), item.getValue());
        }
        BigDecimal salary = salaryHistoryManager.getEmployeeLastSalaryHistory(employeeID, new Date());
        String value = salary != null ? salary.toString() : "0";
        res.put(SALARY, value);
        return res;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRegions(Integer countryId) {
        return profileService.getRegions(countryId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return profileService.getCountries();
    }

    public String getEmployeeName(Integer employeeId) {
        EdsEmployee employee = employeeManager.get(employeeId);
        return employee.getName();
    }

    public void deleteEmployee(Integer employeeId, boolean removeContact, boolean isRemove, DateNonConvertable resignationDate, ReferenceItem rejectionReason) {
        long begin = System.currentTimeMillis();
        EdsEmployee employee = employeeManager.get(employeeId);
        EdsUser user = employeeManager.getUser();
        // Who did delete this employee
        employee.clear();
        employee.setUpdater(user);
        employee.setLastUpdateTime(user.getCompany().getCompanyDate());

        employeeTaskManager.deleteEmployeeTasksByEmployee(employeeId);

        // get Project Employee list
        List<EdsProjectEmployee> projectEmployeeList = projectManager.getEmployeeNotStartedOnGoingProjects(employee);

        for (EdsProjectEmployee projectEmployee : projectEmployeeList) {
            // Employee task list
            List<EdsEmployeeTask> employeeList = employeeTaskManager.getEstimatedEmployeeTasks(projectEmployee);
            for (EdsEmployeeTask employeeTask : employeeList) {
                //clear task from the employee items
                //this history for the calculation PROJECT COST
                EdsTask task = employeeTask.getTask();
                EdsTaskEstimateTimeSpentHistory estimateTimeSpentHistory = new EdsTaskEstimateTimeSpentHistory();
                estimateTimeSpentHistory.setTask(task);
                estimateTimeSpentHistory.setOldEstimatedTime(employeeTask.getEstimatedTime());
                estimateTimeSpentHistory.setEstimatedTime(0);
                task.getEstimateTimeSpentHistoryList().add(estimateTimeSpentHistory);
                task.setEstimatedTime(task.getEstimatedTime() - employeeTask.getEstimatedTime());
                task.setChangedCalculationFields(true);
                task.setLastUpdateTime(new Date());
            }
        }
        //Delete Project Employee
        projectEmployeeManager.updateProjectEmployee(employee.getEmployeeTeam());
        //Delete Department Employee
        employeeDepartmentManager.deleteEmployeeInTeam(employee);
        baseEventPostProcessor.registerEvent(EmployeeDepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, employee.getEmployeeTeam(), user);

        //remove employee from payroll groups
        payrollBatchManager.removeEmployeeFromGroups(employee.getObjectID());

        EdsPlacement edsPlacement = employee.getPlacement();
        if (edsPlacement != null) {
            employee.setPlacement(null);
            edsPlacement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_APPROVED));
            placementManager.update(edsPlacement);
        }
        // Delete user
        if (isRemove) {
            userManager.deleteUser(employee.getObjectID(), referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE));
        } else {
            employee.setDeleted(true);
            employee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_RESIGNED));
            employee.setEndDate(resignationDate != null ? resignationDate.getNonConvertedDate() : new Date());
        }
        if (rejectionReason != null && rejectionReason.getCategory() != null) {
            EdsNoteHistory edsNote = new EdsNoteHistory();
            edsNote.setEmployee(userManager.getUser());
            edsNote.setComment(rejectionReason.getCategory());
            edsNote.setEventDate(new Date());
            edsNote.setRelatedId(employee.getObjectID());
            edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(RelationItem.TYPE_EMPLOYEE));
            edsNote.setSuperUser(ServerUtils.isSuperUser());
            noteHistoryManager.createOrUpdate(edsNote);
        }
        if (rejectionReason != null && rejectionReason.getId() != null) {
            employee.setRejectionReason(rejectionReason.getId());
        }
        userManager.removeEmployeeFromShareReport(employee.getObjectID());
        if (employee.getProfile() != null && employee.getProfile().getContact() != null) {
            EdsCrmContact contact = employee.getProfile().getContact();
            if (removeContact) {
                contact.setContactType(EdsCrmContact.CRM_CONTACT);
                contact.setDeleted(true);
                crmContactManager.update(contact, true);
            } else {
                contact.setEntityContactID(null);
                contact.setContactType(EdsCrmContact.CRM_CONTACT);
                crmContactManager.update(contact, true);
            }
        }
        try {
            if (isRemove) {
                solrManager.removeEmployeesByIds(employee.getObjectID());
            } else {
                employeeSolrComponent.index(employee);
            }
        } catch (SolrServerException | InterruptedException e) {
            log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
        } catch (IOException e) {
            log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
        }
        //Delete document rbac Entries
        if (isRemove) {
            commonServiceLocal.removeDocumentEntries(employee.getObjectID());
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_EMPLOYEE_DELETED, new EdsFileHeader(), employee);
        }
        //Delete employee from groups
        EdsTrustee trustee = trusteeManager.getTrustee(employee);
        for (EdsGroup group : employee.getMembershipGroups()) {
            group.getMembers().remove(trustee);
        }
        //delete employee assigned task rbac entries for assignee facet filter
        taskService.removeDeletedEmployeeRbacks(employeeId);
        System.out.println(">>>>DELETE EMPLOYEE TOOK - " + (System.currentTimeMillis() - begin) / 1000 + " seconds");
        stepEmployeeManager.archiveOthers(employeeId, null, null, EdsStepEmployee.EMPLOYEE_TYPE);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(employeeId);
        ServerUtils.kpiLog(log, kpiLog, "Delete employee");
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, employee, user);
        workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
    }


    public void deleteEmployees(ArrayList<Integer> employeeIds, boolean removeContact) {
        if (employeeIds != null && employeeIds.size() > 0) {
            for (Integer employeeId : employeeIds) {
                deleteEmployee(employeeId, removeContact, true, null, null);
            }
        }
    }

    /* if delete employee he has department that workes this method */

    public void removeDepartmentLeader(Integer[] teamsId, Integer employeeId, Integer moveEmployeeId) {
        StringBuilder ids = new StringBuilder();
        for (Integer id : teamsId) {
            if (!ids.toString().equals("")) {
                ids.append(",");
            }
            ids.append(id);
        }
        EdsEmployee employee = employeeManager.get(moveEmployeeId);
        // move department leader to employee
        departmentManager.removeTeamLeaderAndMoveNewEmployee(ids.toString(), employee);
        // delete old employee role
        EdsUser user = userManager.get(employeeId);
        Set<EdsRole> roleList = user.getRoles();

        for (EdsRole role : roleList) {
            if (role.getObjectID().equals(EdsRole.TL)) {
                user.getRoles().remove(role);
                break;
            }
        }
        // if emmployee no team add team rolee
        String role = employee.getRolesAsIntegersString();
        if (role.indexOf(EdsRole.TL) == -1) {
            Set<EdsRole> roles = employee.getRoles();
            EdsRole tl_role = roleManager.get(EdsRole.TL);
            roles.add(tl_role);
            employee.setRoles(roles);
        }
    }

    /* delete employee in project managers */

    public void removeProjectManagers(Integer[] projectId, Integer employeeId, Integer moveEmployeeId) {
        StringBuilder ids = new StringBuilder();
        for (Integer id : projectId) {
            if (!ids.toString().equals("")) {
                ids.append(",");
            }
            ids.append(id);
        }
        EdsEmployee employee = employeeManager.get(moveEmployeeId);
        EdsUser user = userManager.getUser();

        // get Project List
        List<EdsProject> list = projectManager.getProjects(ids.toString());
        // project manager clear and set new project manager
        for (EdsProject project : list) {
            if (project.getManager().getObjectID().equals(employeeId)) {
                project.setManager(employee);
                baseEventPostProcessor.registerEvent(ProjectManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
            } else {
                if (!project.getManager().getObjectID().equals(employee.getObjectID()) && !project.getBackupManagerIDs().contains(employee.getObjectID())) {
                    project.replaceBackupManager(employeeManager.get(employeeId), employee);
                    baseEventPostProcessor.registerEvent(ProjectBackupManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user, employee);
                } else {
                    project.replaceBackupManager(employeeManager.get(employeeId), null);
                }
            }

            ProjectMember[] members = new ProjectMember[1];
            members[0] = new ProjectMember();
            members[0].setId(employee.getObjectID());
            members[0].setWageRate(0d);
            members[0].setClientChargeRate(0d);
            members[0].setWorkloadPercentage(0f);
            projectServiceLocal.addMembers(project.getObjectID(), members);
            projectManager.update(project);
            try {
                EdsCompany edsCompany = employee.getCompany();
                projectSolrComponent.index(project);
            } catch (SolrServerException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // if emmployee no team add team rolee
        String role = employee.getRolesAsIntegersString();
        if (role.indexOf(EdsRole.PM) == -1) {
            Set<EdsRole> roles = employee.getRoles();
            EdsRole pm_role = roleManager.get(EdsRole.PM);
            roles.add(pm_role);
            employee.setRoles(roles);
        }
    }

    public HistoryListItem[] getEmployeeNotes(Integer employeeID) {
        EdsEmployee employee = employeeManager.get(employeeID);
        HistoryListItem[] employeeNotes;
        if (employee != null) {
            EdsNoteHistory[] employeeNote = noteHistoryManager.getNoteList(new ListingFilterParameter()).toArray(new EdsNoteHistory[]{});
            List<EdsNoteHistory> histrNotes = new LinkedList<>();
            for (EdsNoteHistory noteHistr : employeeNote) {
                if ((EdsNoteHistory.EMPLOYEE == noteHistr.getRelatedTo() && noteHistr.getRelatedId() != null) &&
                        (noteHistr.getRelatedId().intValue() == employee.getObjectID().intValue())) {
                    histrNotes.add(noteHistr);
                }
            }
            employeeNotes = new HistoryListItem[histrNotes.size()];
            EdsUser user = employeeManager.getUser();
            for (int i = 0; i < histrNotes.size(); i++) {
                EdsNoteHistory notes = histrNotes.get(i);
                HistoryListItem items = new HistoryListItem();
                items.setObjectID(notes.getObjectID());
                items.setEmployee(notes.getEmployee().getName());
                items.setSubject(notes.getSubject());
                items.setComment(notes.getComment());
                items.setVisibility(notes.isVisibility());
                items.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
                items.setEditable(user.equals(notes.getEmployee()));
                NewsComment[] noteComments = getEmployeeNoteComments(notes.getObjectID());
                if (noteComments.length > 0) {
                    items.setNotesComments(noteComments);
                } else {
                    items.setNotesComments(new NewsComment[0]);
                }
                employeeNotes[i] = items;
            }
            return employeeNotes;

        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompamyLocaleList() {
        List<EdsLocale> edsLocaleList = localeManager.list();
        SelectItem[] items = new SelectItem[edsLocaleList.size()];
        int i = 0;
        for (EdsLocale edsLocale : edsLocaleList) {
            items[i++] = new SelectItem(edsLocale.getId(), edsLocale.getCountry());
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsComment[] getEmployeeNoteComments(Integer noteID) {
        return commonService.getNotecomments(noteID);
    }

    public NewsComment saveEmployeeNoteComments(NewsComment data) {
        return commonService.saveNoteComment(data);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getOrgChart() {
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProject(ListingFilterParameter filterParametrs) {
        List<EdsProject> projects = projectManager.list(filterParametrs);
        if (projects != null && projects.size() > 0) {
            SelectItem[] projectItem = new SelectItem[projects.size()];
            int i = 0;
            for (EdsProject project : projects) {
                SelectItem item = new SelectItem(project.getObjectID(), project.getName());
                projectItem[i++] = item;
            }
            return projectItem;
        }
        return new SelectItem[0];
    }

    @Override
    public SelectItem[] getProjectsAsSelectItem(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        ArrayList<String> columns = new ArrayList<>();
        Collections.addAll(columns, FacetContentType.ProjectFacetFilter.getContentCode());

        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setStart(filterParametrs.getStart() != null ? filterParametrs.getStart() : 0);
        listingFilterParameter.setLimit(filterParametrs.getLimit() != null ? filterParametrs.getLimit() : 20);
        listingFilterParameter.setSearchKey(filterParametrs.getSearchKey());
        listingFilterParameter.setLookUp(true);

        HashMap<String, FacetSolrField> projectSolrField = new HashMap<>();

        FacetFilterRpc facetFilter = new FacetFilterRpc(columns, projectSolrField);
        facetFilter.setType(ListPanelType.ProjectListPanel);
        listingFilterParameter.setFacetFilter(facetFilter);

        ListResult<ProjectListItem> projectList = projectService.getProjectList(listingFilterParameter);
        if (projectList != null && projectList.getList() != null && projectList.getList().size() > 0) {
            SelectItem[] projectItems = new SelectItem[projectList.getList().size()];
            int i = 0;
            for (ProjectListItem project : projectList.getList()) {
                SelectItem item = new SelectItem(project.getObjectId(), project.getName());
                projectItems[i++] = item;
            }
            return projectItems;
        }
        return new SelectItem[0];
    }

    public String getEmployeeRoles(Integer employeeID) {
        EdsEmployee employee = employeeManager.get(employeeID);
        return employee.getRolesAsIntegersString();
    }

    private void createContactItemParams(EdsCrmContact crmContact, Integer param, String value) {
        EdsCrmContactItemParams edsCrmContactItemParams = new EdsCrmContactItemParams();
        edsCrmContactItemParams.setRelation(EdsCrmContactItemParams.WORK);
        edsCrmContactItemParams.setContact(crmContact);
        edsCrmContactItemParams.setParam(param);
        edsCrmContactItemParams.setValue(value);
        crmContactItemParamsManager.create(edsCrmContactItemParams);
    }

    private NumberData generateOldEmployeeNumber(String employeeCode, Integer intNumber) {
        NumberData numberData = new NumberData();
        numberData.setIntNumber(intNumber);
        numberData.setFirstNumberString(employeeCode);
        numberData.setNumberFormat(employeeCode + "_" + intNumber);
        numberData.setNumberString(employeeCode + intNumber);
        return numberData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectEmployeesAsSelectItem(ListingFilterParameter fp) {
        EdsProject edsProject = projectManager.get(fp.getProjectId());
        if (edsProject == null) {
            return new SelectItem[0];
        }
        fp = fp == null ? new ListingFilterParameter() : fp;
        List<EdsEmployee> projectEmployeeList = projectEmployeeManager.getEmployeesByProject(fp);
        if (projectEmployeeList == null) {
            return new SelectItem[0];
        }
        int i = 0;
        SelectItem[] result = new SelectItem[projectEmployeeList.size()];
        if (projectEmployeeList.size() > 0) {
            for (EdsEmployee employee : projectEmployeeList) {
                if (employee == null) {
                    continue;
                }
                EdsProjectEmployee edsProjectEmployee = projectEmployeeManager.getProjectEmployee(employee, edsProject);
                SelectItem item = new SelectItem();
                item.setId(employee.getObjectID());
                if (employee.getProfile() != null && !StringUtils.isEmpty(employee.getProfile().getEmployeeCode())) {
                    item.setName(employee.getProfile().getEmployeeCode() + " - " + employee.getName());
                } else {
                    item.setName(employee.getName());
                }
                if (edsProjectEmployee != null) {
                    item.setDescription(String.valueOf(edsProjectEmployee.getObjectID()));
                }
                result[i++] = item;
            }
        }
        return result;
    }

    public LinkedHashMap<String, Integer> getHeadcountChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, Integer> resultMap = null;
        if (fp.getCategoryID() == 1) {
            ListResult<TeamListItem> teamList = departmentService.getTeams(fp);
            resultMap = new LinkedHashMap<>();
            for (TeamListItem item : teamList.getList()) {
                resultMap.put(item.getName(), Integer.valueOf(item.getHeadCount()));
            }
        } else if (fp.getCategoryID() == 2) {
            resultMap = new LinkedHashMap<>();
            ListResult<CompLocationRpc> locationList = locationService.getLocations(fp);
            for (CompLocationRpc item : locationList.getList()) {
                resultMap.put(item.getName(), item.getMemberCount());
            }
        } else {
            resultMap = new LinkedHashMap<>();
            ListResult<PositionItem> positionList = hrmsServiceLocal.getPositionList(fp);
            for (PositionItem item : positionList.getList()) {
                resultMap.put(item.getName(), item.getEmployeeCount());
            }
        }
        return resultMap;
    }

    public Long[] getEmployeesGenderRatio() {
        return employeeManager.getEmployeesGenderRatio();
    }

    private EdsCrmCustomFields saveCustomFields(EdsCrmCustomFields edsCrmCustomField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsCrmCustomField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCrmCustomField = new EdsCrmCustomFields();
                crmCustomFieldsManager.create(edsCrmCustomField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCrmCustomField, customFieldItems);
            return edsCrmCustomField;
        }
        return null;
    }

    public EdsEmployeeItemTableCF saveCustomTableFields(EdsEmployeeItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customfField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && fieldItem.getFieldStringValue().length() > 0)
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || fieldItem.getProfielImageId() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customfField = new EdsEmployeeItemTableCF();
                employeeItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }
}
