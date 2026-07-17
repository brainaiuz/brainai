package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsEmployeeSkills;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.solr.document.EmployeeSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.EmployeeSolrDocRepository;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.UserBankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_NO_ACCCESS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.INSTRUCTOR_CODE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_EMPLOYEE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
@Component
public class EmployeeSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(EmployeeSolrComponent.class);

    @Autowired
    UserBankAccountManager userBankAccountManager;
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private EmployeeSolrDocRepository employeeSolrDocRepository;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    @Qualifier("payrollService")
    private PayrollServiceLocal payrollService;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsEmployee edsEmployee) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsEmployee));
    }

    @Transactional
    public void indexes(List<EdsEmployee> edsEmployeeList) throws IOException, SolrServerException, InterruptedException {

        if (!CollectionUtils.isEmpty(edsEmployeeList)) {
            List<EmployeeSolrDoc> employeeSolrDocs = new ArrayList<>();

            String companyID = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsEmployee edsEmployee : edsEmployeeList) {
                if (edsEmployee != null) {
                    try {
                        employeeSolrDocs.add(createEmployeeDocument(edsEmployee.getSolrRPC(), Integer.valueOf(companyID), edsEmployee.getCustomFields()));
                        log.info("Indexed Employee Core CID - {}, objId - {}", companyID, edsEmployee.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on Employee with id {}, and error message {} **********************", edsEmployee.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!employeeSolrDocs.isEmpty()) {
                log.info("========= Create Employee solr docs for company {} with size {} =========", companyID, employeeSolrDocs.size());
                employeeSolrDocRepository.saveAll(employeeSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsEmployee> edsEmployeeList) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsEmployeeList)) {
            ConcurrentLinkedQueue<EmployeeSolrDoc> employeeSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyID = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsEmployee edsEmployee : edsEmployeeList) {
                if (edsEmployee != null) {
                    Hibernate.initialize(edsEmployee.getCustomFields());
                    EmployeeSolrItem solrRPC = edsEmployee.getSolrRPC();
                    EdsEmployeeCustomFields customFields = edsEmployee.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyID);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        employeeSolrDocs.add(createEmployeeDocument(solrRPC, Integer.valueOf(companyID), customFields));
                                        log.info("Indexed Employee Core CID - {}, objId - {}", companyID, edsEmployee.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Employee with id: {} **********************", edsEmployee.getObjectID());
                            log.error("ERROR: ", e);
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading Employee list", e);
            }

            if (!employeeSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Employee solr docs for company {} with size {} =========", companyID, employeeSolrDocs.size());
                    employeeSolrDocRepository.saveAll(employeeSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Employee list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EmployeeSolrItem employee) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + employee.getObjectId();
    }

    private EmployeeSolrDoc createEmployeeDocument(EmployeeSolrItem employee, Integer companyID, EdsCustomFields customFields) {
        EmployeeSolrDoc employeeSolrDoc = new EmployeeSolrDoc();

        employeeSolrDoc.setOid(SolrUtils.generatedOId(companyID, employee.getObjectId()));
        employeeSolrDoc.setCompanyId(companyID);
        employeeSolrDoc.setEmployeeId(employee.getObjectId());
        String code = employee.getEmployeeNumber();
        employeeSolrDoc.setEmployeeNumber(code);
        Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        if (StringUtils.isNotBlank(code) && isIntegerNumberEnabled) {
            employeeSolrDoc.setEmployeeIntegerNumber(Long.parseLong(code.replaceAll("[\\D]", "")));
        }
        if (employee.getSupervisor() != null) {
            employeeSolrDoc.setSupervisorId(employee.getSupervisor().getId());
            employeeSolrDoc.setSupervisorName(employee.getSupervisor().getName());
            employeeSolrDoc.setSupervisorIdName(SolrUtils.getIdName(employee.getSupervisor().getId(), employee.getSupervisor().getName()));
        }
        employeeSolrDoc.setPassportNumber(employee.getPassportNumber());
        if (employee.getCountry() != null) {
            employeeSolrDoc.setPassportIssuedBy(employee.getCountry().getName());
            employeeSolrDoc.setPassportIssuedId(employee.getCountry().getId());
        }
        employeeSolrDoc.setPassportIssueDate(employee.getPassportIssueDate());
        employeeSolrDoc.setPassportExpireDate(employee.getPassportExpireDate());

        employeeSolrDoc.setInsuranceNumber(employee.getInsuranceNumber());
        employeeSolrDoc.setVisaNumber(employee.getVisaNumber());
        employeeSolrDoc.setVisaIssueDate(employee.getVisaIssueDate());
        employeeSolrDoc.setVisaExpireDate(employee.getVisaExpireDate());
        employeeSolrDoc.setInsuranceExpiryDate(employee.getInsuranceExpiryDate());
        employeeSolrDoc.setGenderName(employee.getGenderName());
        employeeSolrDoc.setEmployeeName(employee.getEmployeeName());
        employeeSolrDoc.setFirstName(employee.getFirstName());
        employeeSolrDoc.setLastName(employee.getLastName());
        employeeSolrDoc.setMiddleName(employee.getMiddleName());
        employeeSolrDoc.setPhoneNumber(employee.getPhoneNumber());
        if (employee.getEmail() != null && !"".equals(employee.getEmail())) {
            employeeSolrDoc.setEmail(employee.getEmail());
        }
        if (employee.getPosition() != null) {
            employeeSolrDoc.setPositionId(employee.getPosition().getId());
            employeeSolrDoc.setPositionName(employee.getPosition().getName());
            employeeSolrDoc.setPositionIdName(SolrUtils.getIdName(employee.getPosition().getId(), employee.getPosition().getName()));
            ReferenceLocale locale = employee.getPositionName();
            if (locale != null) {
                employeeSolrDoc.setPositionNameUz(locale.getUzbek());
                employeeSolrDoc.setPositionNameRu(locale.getRussian());
                employeeSolrDoc.setPositionNameEn(locale.getEnglish());
                employeeSolrDoc.setPositionNameAr(locale.getArabic());
            }
        }
        if (employee.getPositionType() != null && employee.getPositionTypeLocale() != null) {
            SelectItem positionType = employee.getPositionType();
            ReferenceLocale positionTypeLocale = employee.getPositionTypeLocale();
            employeeSolrDoc.setPositionTypeId(positionType.getId());
            employeeSolrDoc.setPositionTypeName(positionType.getName());
            employeeSolrDoc.setPositionTypeNameUz(positionTypeLocale != null ? positionTypeLocale.getUzbek() : employee.getPosition().getName());
            employeeSolrDoc.setPositionTypeNameEn(positionTypeLocale != null ? positionTypeLocale.getEnglish() : employee.getPosition().getName());
            employeeSolrDoc.setPositionTypeNameRu(positionTypeLocale != null ? positionTypeLocale.getRussian() : employee.getPosition().getName());
            employeeSolrDoc.setPositionTypeNameAr(positionTypeLocale != null ? positionTypeLocale.getArabic() : employee.getPosition().getName());
            employeeSolrDoc.setPositionTypeIdName(SolrUtils.getIdName(positionType.getId(), positionType.getName()));
        }
        if (employee.getRole() != null) {
            employee.getRole().forEach(role -> {
                employeeSolrDoc.getRoleId().add(role.getId());
                employeeSolrDoc.getRoleName().add(role.getName());
                employeeSolrDoc.getRoleCode().add(role.getCode());
                employeeSolrDoc.getRoleIdName().add(SolrUtils.getIdName(role.getId(), role.getName()));
            });
        }
        if (employee.getSkill() != null && employee.getSkill().size() > 0) {
            for (SelectItem skill : employee.getSkill()) {
                employeeSolrDoc.getSkillId().add(skill.getId());
                employeeSolrDoc.getSkillName().add(skill.getName());
                employeeSolrDoc.getSkillIdName().add(SolrUtils.getIdName(skill.getId(), skill.getName()));
            }
        }
        if (employee.getStatus() != null) {
            employeeSolrDoc.setStatusId(employee.getStatus().getId());
            employeeSolrDoc.setStatusName(employee.getStatus().getName());
            employeeSolrDoc.setStatusCode(employee.getStatus().getCode());
            employeeSolrDoc.setStatusIdName(SolrUtils.getIdName(employee.getStatus().getId(), employee.getStatus().getName()));
        }
        if (employee.getLocation() != null) {
            SelectItem location = employee.getLocation();
            employeeSolrDoc.setLocationId(location.getId());
            employeeSolrDoc.setLocationName(location.getCode() != null ? location.getCode() + "->" + location.getName() : location.getName());
            employeeSolrDoc.setLocationIdName(SolrUtils.getIdName(location.getId(), location.getCode()));
            if (employee.getLocationState() != null) {
                employeeSolrDoc.setLocationState(employee.getLocationState());
            }
            employeeSolrDoc.setLocationCity(employee.getLocationCity());
            ReferenceLocale locale = employee.getLocationName();
            if (locale != null) {
                employeeSolrDoc.setLocationNameUz(locale.getUzbek());
                employeeSolrDoc.setLocationNameRu(locale.getRussian());
                employeeSolrDoc.setLocationNameEn(locale.getEnglish());
                employeeSolrDoc.setLocationNameAr(locale.getArabic());
            }
        }
        if (employee.getDepartment() != null) {
            SelectItem department = employee.getDepartment();
            employeeSolrDoc.setDepartmentId(department.getId());
            employeeSolrDoc.setDepartmentName(department.getName());
            employeeSolrDoc.setDepartmentIdName(SolrUtils.getIdName(department.getId(), department.getName()));
            ReferenceLocale locale = employee.getDepartmentName();
            if (locale != null) {
                employeeSolrDoc.setDepartmentNameUz(locale.getUzbek());
                employeeSolrDoc.setDepartmentNameRu(locale.getRussian());
                employeeSolrDoc.setDepartmentNameEn(locale.getEnglish());
                employeeSolrDoc.setDepartmentNameAr(locale.getArabic());
            }

        }
        if (employee.getQualification() != null) {
            employeeSolrDoc.setQualificationId(employee.getQualification().getId());
            employeeSolrDoc.setQualificationName(employee.getQualification().getName());
            employeeSolrDoc.setQualificationIdName(SolrUtils.getIdName(employee.getQualification().getId(), employee.getQualification().getName()));
        }
        if (employee.getContact() != null) {
            employeeSolrDoc.setContactId(employee.getContact().getId());
            employeeSolrDoc.setContactName(employee.getContact().getName());
            employeeSolrDoc.setContactIdName(SolrUtils.getIdName(employee.getContact().getId(), employee.getContact().getName()));
        }
        if (employee.getTimeslot() != null) {
            employeeSolrDoc.setTimeslotId(employee.getTimeslot().getId());
            employeeSolrDoc.setTimeslotName(employee.getTimeslot().getName());
            employeeSolrDoc.setTimeslotIdName(SolrUtils.getIdName(employee.getTimeslot().getId(), employee.getTimeslot().getName()));
        }
        if (employee.getQualification() != null) {
            employeeSolrDoc.setQualificationId(employee.getQualification().getId());
            employeeSolrDoc.setQualificationName(employee.getQualification().getName());
            employeeSolrDoc.setQualificationIdName(SolrUtils.getIdName(employee.getQualification().getId(), employee.getQualification().getName()));
        }

        if (employee.getDriverId() != null) {
            employeeSolrDoc.setDriverId(employee.getDriverId());
        }
        EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUserId(employee.getObjectId());
        if (userBankAccount != null) {
            employeeSolrDoc.setAgentName(userBankAccount.getAgentID());
            employeeSolrDoc.setBankName(userBankAccount.getBankName());
            employeeSolrDoc.setAccountNumber(userBankAccount.getAccountNumber());
            employeeSolrDoc.setAccountName(userBankAccount.getAccountName());
            employeeSolrDoc.setBankAddress(userBankAccount.getBankAddress());
            employeeSolrDoc.setSwiftCode(userBankAccount.getSwiftCode());
            employeeSolrDoc.setSortCode(userBankAccount.getSortCode());
            employeeSolrDoc.setIbanCode(userBankAccount.getIbanCode());
        }
        employeeSolrDoc.setWageRate(employee.getWageRate());
        employeeSolrDoc.setClientChargeRate(employee.getClientChargeRate());
        if (employee.getCountry() != null) {
            SelectItem primaryAddress = employee.getCountry();
            if (primaryAddress.getId() != null) {
                employeeSolrDoc.setCountryId(primaryAddress.getId());
                employeeSolrDoc.setCountryName(primaryAddress.getName());
                employeeSolrDoc.setCountryCode(primaryAddress.getCode());
                employeeSolrDoc.setCountryIdCode(SolrUtils.getIdName(primaryAddress.getId(), primaryAddress.getCode()));
                employeeSolrDoc.setCountryIdCodeName(SolrUtils.getIdCodeName(primaryAddress.getId(), primaryAddress.getCode(), primaryAddress.getName()));
            }
            if (employee.getState() != null) {
                employeeSolrDoc.setStateId(employee.getState().getId());
                employeeSolrDoc.setStateName(employee.getState().getName());
                employeeSolrDoc.setStateIdName(SolrUtils.getIdName(employee.getState().getId(), employee.getState().getName()));
            }
            employeeSolrDoc.setCity(employee.getCity());
            employeeSolrDoc.setStreet(employee.getStreet());
            employeeSolrDoc.setStreet2(employee.getStreet2());
            employeeSolrDoc.setPostCode(employee.getPostCode());
        }
        employeeSolrDoc.setCreatedDate(employee.getCreatedDate());
        employeeSolrDoc.setLastUpdate(employee.getLastUpdate());
        employeeSolrDoc.setBirthDate(employee.getBirthDate());
        employeeSolrDoc.setHireDate(employee.getHireDate());
        employeeSolrDoc.setEndDate(employee.getEndDate());
        if (employee.getCurrency() != null) {
            SelectItem currency = employee.getCurrency();
            employeeSolrDoc.setCurrencyId(currency.getId());
            employeeSolrDoc.setCurrencyName(currency.getName());
            employeeSolrDoc.setCurrencyIdName(SolrUtils.getIdName(currency.getId(), currency.getName()));
        }
        if (employee.getPayrollBatchId() != null && employee.getPayrollBatchId().size() > 0) {
            employeeSolrDoc.getPayrollBatchId().addAll(employee.getPayrollBatchId());
        }
        employeeSolrDoc.setOpeningBalanceDays(employee.getOpeningBalanceDays());
        employeeSolrDoc.setProbationDays(employee.getProbationDays());

        if (customFields != null) {
            CustomFieldsUtils.setSolrDocDynamicFields(employeeSolrDoc, customFields);
        }

        return employeeSolrDoc;
    }

    public FacetFilterRpc getEmployeeFacetFilterData(FacetFilterRpc employeeFacet) {
        EdsUser user = userManager.getUser();
        if (!employeeFacet.isFilterChanges()) {
            employeeFacet = commonServiceLocal.getUserFacetFilter(employeeFacet);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setAllEmployees(employeeFacet.isAllEmployees());
        fp.setSearchKey(employeeFacet.getSearchKey());
        fp.setViewType(employeeFacet.getName());
        if (Constants.FROM_TRAINING_CENTER.equals(fp.getViewType())) {
            EdsRole instructorRole = roleManager.getByCode(INSTRUCTOR_CODE);
            fp.setRoleID(instructorRole.getObjectID());
        }

        boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
        boolean showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_PROJECT_EMPLOYEE_LIST);
        boolean showDepartmentEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST);
        fp.setModule(employeeFacet.getName());
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
            List<Integer> employeeIDs = projectManager.getPMManagedProjectsEmployeeIDs(user.getObjectID());
            fp.setEmployeeIDs(ServerUtils.getAsCommoDelimited(employeeIDs, "0", " "));
        }
        List<Integer> departmentList = Lists.newArrayList();
        if (!showAllEmployees && ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST)) {
            List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(user.getObjectID());
            departmentList.addAll(edsDepartments.stream().map(EdsDepartment::getObjectID).collect(Collectors.toList()));
        }

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getEmployeeSolrQuery(fp, user, departmentList));

        if (Constants.FROM_PAYROLL.equals(fp.getViewType())) {
            solrQuery.append(payrollService.generatePermissionQuery(PermissionConstants.PAYROLL_EMPLOYEES_LIST));
        }
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(employeeFacet, company,
                SolrEmployeeRepresenter.FIELD_CREATED_DATE, SolrEmployeeRepresenter.FIELD_CREATED_DATE));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_EMPLOYEE_CORE, solrQuery.toString(),
                employeeFacet, EmployeeSolrDoc.class);
        FacetFilterRpc facetFilterRpc = SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, employeeFacet);
        if (employeeFacet.getType() != null) {
            commonServiceLocal.getFacetFilterWithLocale(facetFilterRpc != null ? facetFilterRpc.getFacetContentMap() : null, commonServiceLocal.getCompanyCustomFields(employeeFacet.getType().getViewName()));
        }
        return facetFilterRpc;
    }

    public Page<EmployeeSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        String userLang = ServerUtils.getUserLocale().getLanguage();
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrEmployeeRepresenter.FIELD_LAST_UPDATE_DATE);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (filterParameter.getSortField()) {
                    case EmployeeListItem.EMPLOYEE_NUMBER -> {
                        if (filterParameter.isCheckNumber()) {
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER);
                        } else {
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_EMPLOYEE_NUMBER);
                        }
                    }
                    case EmployeeListItem.FIRST_NAME ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_FIRST_NAME);
                    case EmployeeListItem.LAST_NAME ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_LAST_NAME);
                    case EmployeeListItem.EMPLOYEE_NAME ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_EMPLOYEE_NAME);
                    case EmployeeListItem.PHONE_NUMBER ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_PHONE_NUMBER);
                    case EmployeeListItem.EMAIL ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_EMAIL);
                    case EmployeeListItem.POSITION -> {
                        if (userLang != null && !userLang.isEmpty()) {
                            solrSort = switch (userLang.toLowerCase()) {
                                case "uz" -> Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_POSITION_NAME_UZ);
                                case "ru" -> Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_POSITION_NAME_RU);
                                case "en" -> Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_POSITION_NAME_EN);
                                case "ar" -> Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_POSITION_NAME_AR);
                                default -> solrSort;
                            };
                        } else {
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_POSITION_NAME);
                        }
                    }
                    case EmployeeListItem.ROLE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_ROLE_NAME);
                    case EmployeeListItem.STATUS ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_STATUS_NAME);
                    case EmployeeListItem.DEPARTMENT -> {
                        if (userLang != null && !userLang.isEmpty()) {
                            solrSort = switch (userLang.toLowerCase()) {
                                case "uz" ->
                                        Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME_UZ);
                                case "ru" ->
                                        Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME_RU);
                                case "en" ->
                                        Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME_EN);
                                case "ar" ->
                                        Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME_AR);
                                default -> solrSort;
                            };
                        } else {
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_DEPARTMENT_NAME);
                        }
                    }
                    case EmployeeListItem.LAST_UPDATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_LAST_UPDATE_DATE);
                    case EmployeeListItem.START_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_HIRE_DATE);
                    case EmployeeListItem.END_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_END_DATE);
                    case EmployeeListItem.HIRE_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_HIRE_DATE);
                    case EmployeeListItem.PASSPORT_NUMBER ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_PASSPORT_NUMBER);
                    case EmployeeListItem.PASSPORT_ISSUE_BY ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_PASSPORT_ISSUED_BY);
                    case EmployeeListItem.PASSPORT_ISSUE_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_PASSPORT_ISSUE_DATE);
                    case EmployeeListItem.PASSPORT_EXPIRE_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_PASSPORT_EXPIRE_DATE);
                    case EmployeeListItem.INSURANCE_NUMBER ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_INSURANCE_NUMBER);
                    case EmployeeListItem.VISA_NUMBER ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_VISA_NUMBER);
                    case EmployeeListItem.VISA_ISSUE_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_VISA_ISSUE_DATE);
                    case EmployeeListItem.VISA_EXPIRATION_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_VISA_EXPIRE_DATE);
                    case EmployeeListItem.BIRH_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_BIRTH_DATE);
                    case EmployeeListItem.INSURANCE_EXPIRY_DATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_INSURANCE_EXPIRY_DATE);
                    case EmployeeListItem.SUPERVISOR ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_SUPERVISOR_NAME);
                    case EmployeeListItem.GENDER_NAME ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_GENDER_NAME);
                    case EmployeeListItem.COUNTRY ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_COUNRTY_NAME);
                    case EmployeeListItem.LOCATION ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_LOCATION_NAME);
                    case EmployeeListItem.STREET ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_STREET);
                    case EmployeeListItem.STREET2 ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_STREET2);
                    case EmployeeListItem.CITY ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_CITY);
                    case EmployeeListItem.STATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_STATE_NAME);
                    case EmployeeListItem.CURRENCY ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_CURRENCY_NAME);
                    case EmployeeListItem.AGENT_ID ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_AGENT_NAME);
                    case EmployeeListItem.BANK_NAME ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_BANK_NAME);
                    case EmployeeListItem.ACCOUNT_NUMBER ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_ACCOUNT_NUMBER);
                    case EmployeeListItem.ACCOUNT_NAME ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_ACCOUNT_NAME);
                    case EmployeeListItem.BANK_ADDRESS ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_BANK_ADDRESS);
                    case EmployeeListItem.SORT_CODE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_SORT_CODE);
                    case EmployeeListItem.IBAN_CODE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_IBAN_CODE);
                    case EmployeeListItem.SALARY_AMOUNT ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_SALARY_AMOUNT);
                    case EmployeeListItem.QUALIFICATION ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.SORTABLE_QUALIFICATION_NAME);
                    case EmployeeListItem.WAGE_RATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_WAGE_RATE);
                    case EmployeeListItem.CLIENT_CHARGE_RATE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_CLIENT_CHARGE_RATE);
                    case EmployeeListItem.OPENING_BALANCE_DAYS ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_OPENING_BALANCE_DAYS);
                    case EmployeeListItem.PROBATION_DAYS ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_PROBATION_DAYS);
                    case EmployeeListItem.SWIFT_CODE ->
                            solrSort = Sort.by(sortDirection, SolrEmployeeRepresenter.FIELD_SWIFT_CODE);
                }
            }
        } else if (filterParameter.isCheckNumber()) {
            solrSort = Sort.by(Sort.Direction.ASC, SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER);
        }

        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_EMPLOYEE_CORE, query, EmployeeSolrDoc.class);
    }
}
