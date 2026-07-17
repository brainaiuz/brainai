package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.approving.EdsDynamicApprover;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.availability.client.rpc.TeammatesAvailability;
import com.edatasite.workforce.gwt.client.client.rpc.EmployeePayslipItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectCustomEventListenerImpl;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;


/**
 * User: Admin
 * Date: 07.01.2008
 * Time: 17:19:06
 */
@Repository("employeeManager")
public class EmployeeManagerImpl extends BaseManager<EdsEmployee> implements EmployeeManager, Constants {

    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private ApproverManager approverManager;

    public EmployeeManagerImpl() {
        super(EdsEmployee.class);
    }

    public List<EdsEmployee> list(ListingFilterParameter fp) {
        return list(fp, false);
    }

    public List<EdsEmployee> list(ListingFilterParameter fp, boolean fromRU) {
        return findNative(employeeListQuery(fp, fromRU, false), EdsEmployee.class);
    }

    public String employeeListQuery(ListingFilterParameter fp, boolean fromRU, boolean onlyCount) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        EdsUser user = getUser();

        int activeEmployeeStsId = 0;
        if (fp.isShowActive()) {
            activeEmployeeStsId = getActiveEmployeeStatusId(activeEmployeeStsId);
        }
        List<String> cfList = null;
        boolean hasCustomFieldColumnName = false;
        if (fp.isCustomFieldsShown()) {
            cfList = find("SELECT ccfs.columnCode FROM EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.showInListing = true order by ccfs.objectID asc ", ViewName.Employee.name());
            hasCustomFieldColumnName = cfList != null && cfList.contains(fp.getSortField());
        }
        String orderCode = "";
        if (isIntegerEmployeeCodeEnabled() && fp.getSearchKey() != null) {
            orderCode = (String) findNativeSingle("select ep.employeeCode from " + getCompanyId() + ".employee e \n" +
                    "left join " + getCompanyId() + ".myuser mu on mu.id=e.id \n" +
                    "left join " + getCompanyId() + ".employeeprofile ep on e.profileid=ep.id \n" +
                    "where mu.deleted = false and regexp_replace(ep.employeeCode, '[a-z]', '', 'gi')='" + fp.getSearchKey() + "'");
        }
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        if (onlyCount) {
            sql.append(" select count(distinct mu.id) ");
        } else {
            sql.append(" select distinct mu.*, e.*, pr.employeeCode, po.name, re.sorder ").append(hasCustomFieldColumnName
                    ? (", ecf." + fp.getSortField() + " ")
                    : "");
            //by sorting shown columns - begin
            sql.append("phoneNumber".equals(fp.getSortField()) ? ", c.primaryPhone as phoneNumber " : "");
            sql.append("department".equals(fp.getSortField()) ? ", tem.name as department " : "");
            sql.append("lastUpdateTime".equals(fp.getSortField()) ? ", e.lastUpdateTime " : "");
            sql.append("startDate".equals(fp.getSortField()) ? ", e.startDate " : "");
        }
        //by sorting shown columns - end
        sql.append(" from ").append(companyId).append(".employee e ");
        sql.append(" left outer join ").append(companyId).append(".myuser mu on (e.id=mu.id) ");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on e.id = te.employeeid ");
        sql.append(" left outer join ").append(companyId).append(".projectemployee pe on te.id=pe.employeedepartmentid ");
        sql.append(" left outer join ").append(companyId).append(".project p on (p.id=pe.projectid) ");
        sql.append(" left outer join ").append(companyId).append(".team tem on (tem.id=te.teamid) ");
        sql.append(" left outer join ").append(companyId).append(".position po on (po.id = e.positionid) ");
        sql.append(" left outer join ").append(companyId).append(".userBankAccount uba on (uba.userid = e.id) ");
        sql.append(" left join ").append(companyId).append(".reference re on re.id = mu.accountstatusid ");
        sql.append(" left join ").append(companyId).append(".emp_batch eb on e.id = eb.emp_id ");

        if (isIntegerEmployeeCodeEnabled()) {
            sql.append(" left outer join (select id, contact_id, cast(NULLIF(regexp_replace(employeeCode, '[[:alpha:]]', '', 'gi'), '') as int) employeeCode from ").append(companyId).append(".employeeprofile) pr on (pr.id = e.profileid) ");
        } else {
            sql.append(" left outer join ").append(companyId).append(".employeeprofile pr on (pr.id = e.profileid) ");
        }
        sql.append(" left outer join ").append(companyId).append(".crmContact c on (c.id = pr.contact_id) ");

        //for custom field
        if (fp.isCustomFieldsShown()) {
            sql.append(" left outer join ").append(companyId).append(".employeecustomfields ecf on (ecf.id = e.customfields_id) ");
        }

        //filter for instructor
        if (FROM_TRAINING_CENTER.equals(fp.getViewType())) {
            sql.append(" left outer join ").append(companyId).append(".myuser_role roles on (roles.users_id = mu.id) \n");
        }

        sql.append(" where (mu.deleted<>true " + (fp.isShowActive() ? "and re.id =" + activeEmployeeStsId : ""));

        if (!fp.isEmployeeListForVacant()){
            sql.append(" or (mu.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "') ");
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null && fp.isThisMonthEmployees()) {
            sql.append(" AND (e.endDate > '" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate()) + "'" + " or e.endDate <> null) " + "  and (e.startDate < '" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate()) + "' or e.endDate <> null)) \n");
        } else {
            sql.append(") ");
        }
        sql.append("department".equals(fp.getSortField()) ? " and te.isdeleted is not true " : "");

        // Filter by client, department, employee
        if (fp.isCRM()) {
            sql.append(" and re.code = '" + EMPLOYEE_STATUS_ACTIVE + "' ");
        } else if (!fp.isResignedEmployeesIncluded()) {
            sql.append(" and re.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ");
        }
        if (fp.getCurrencyID() != null) {
            sql.append(" and (e.currency_id = " + fp.getCurrencyID() + (fp.getCurrencyID().equals(fp.getBaseCurrencyID()) ? " or e.currency_id is null) " : ") "));
        }
        //filter for instructor
        if (FROM_TRAINING_CENTER.equals(fp.getViewType())) {
            EdsRole instructorRole = roleManager.getByCode(INSTRUCTOR_CODE);
            if (instructorRole != null) {
                sql.append(" and roles.roles_id =").append(instructorRole.getObjectID()).append(" \n");
            }
        }
        if (fp.getViewAsId() != null && EdsRole.PM.equals(fp.getViewAsId())) {
            sql.append(" and p.managerid =").append(user.getObjectID());
            sql.append(" or p.backup_managerid=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid2=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid3=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid4=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid5=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid6=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid7=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid8=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid9=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid10=").append(user.getObjectID());
        }
        if (fp.getLocationId() != null) {
            sql.append(" and mu.locationId=").append(fp.getLocationId());
        }
        if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and p.clientid=").append(fp.getClientId()).append(" ");
        }
        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" and pe.isdeleted<>true and pe.projectid=").append(fp.getProjectId()).append(" ");
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and te.teamid=").append(fp.getDepartmentId()).append(" ");
            sql.append(" and te.isdeleted<>true ");
        }
        if (fp.getEmployeeStatusID() != null && fp.getEmployeeStatusID() > 0) {
            sql.append(" and mu.accountstatusid=").append(fp.getEmployeeStatusID()).append(" ");
        }
        if (fp.getPositionID() != null && fp.getPositionID() > 0) {
            sql.append(" and po.id=").append(fp.getPositionID()).append(" ");
        }
        if (fp.getPayrollBatchID() != null && fp.getPayrollBatchID() > 0) {
            sql.append(" and eb.batch_id=").append(fp.getPayrollBatchID()).append(" ");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null && !fp.isThisMonthEmployees()) {
            sql.append("AND (e.startDate between  '" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate()) + "' and '" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate()) + "') \n");
        }
        if (fp.getTimeSlotID() != null && fp.getTimeSlotID() > 0) {
            sql.append(" and e.timeslotid=").append(fp.getTimeSlotID()).append(" ");
        }
        if (fp.getLanguageIDs() != null && !fp.getLanguageIDs().isEmpty()) {
            sql.append(" and e.id in (").append("select sl.entityid from ").append(getCompanyId()).append(".spokenlanguages sl where sl.entitytype = 'EMPLOYEE' AND sl.languageId in ( ").append(fp.getLanguageIDs()).append(")) ");
        }
        if (fp.getPositionIDs() != null && !fp.getPositionIDs().isEmpty()) {
            if (fp.getNoPosition()) {
                sql.append(" and (po.id in (").append(fp.getPositionIDs()).append(") ");
                sql.append(" or e.positionid is null) ");
            } else {
                sql.append(" and po.id in (").append(fp.getPositionIDs()).append(") ");
            }
        } else if (fp.getNoPosition()) {
            sql.append(" and e.positionid is null");
        }
        if (fp.getObjectIDs() != null && !fp.getObjectIDs().isEmpty()) {
            if (fp.isIDsOnly()) {
                sql.append(" and e.id in ('" + ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", "','")).append("') ");
            } else {
                sql.append(" and e.id not in ('" + ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", "','")).append("') ");
            }
        }
        if (fp.getAgentId() != null && fp.getAgentId() > 0) {
            sql.append(" and uba.id=").append(fp.getAgentId()).append(" ");
        }

        //if the request is from Resource Utilization, the result is retrieved based on the Role Permission Settings
        if (fromRU) {
            sql.append(getRolePermission());
            sql.append(" and mu.deleted = false");
        } else {
            boolean seeAllEmployees = false;
            EdsCompany company = user.getCompany();
            boolean mindshare = (Integer.valueOf(25608).equals(company.getObjectID()) ||
                    Integer.valueOf(8687).equals(company.getObjectID()));
            if (mindshare) {
                seeAllEmployees = user.hasEitherRoles(EdsRole.ADMIN);
            } else {
                seeAllEmployees = (user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.HR) || ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST));
            }

            if (!seeAllEmployees && user.hasEitherRoles(EdsRole.DR, EdsRole.TL, EdsRole.HR, EdsRole.ADMIN_LOCATION) && mindshare) {
                EdsLocation location = user.getLocation();
                sql.append(" and (mu.locationId is not null)  and mu.locationId=").append(location != null
                        ? location.getObjectID()
                        : null);
            }

            // Admin, Director, HR or user who has HRMS_SEE_ALL_EMPLOYEES role permission should be able see ALl employees
            if (seeAllEmployees || (fp.isShowAll() != null && fp.isShowAll())) {
                // he should be able to see all employees
            } else if (user.hasEitherRoles(EdsRole.CLIENT) || user.isClientContact()) {
                //Client should see only those employees who are working on his projects
                sql.append(" and p.clientid=").append(user.getClientContact().getClientID()).append(" ");
            } else {
                //Other roles should only see employees who matters to them
                //check if the user is TL or PM or Backup PM of this employee
                sql.append(" and (tem.leaderid=").append(user.getObjectID()).append(" or p.managerid=").append(user.getObjectID()).
                        append(" or p.backup_managerid=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid2=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid3=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid4=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid5=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid6=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid7=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid8=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid9=").append(user.getObjectID()).append(" or ");
                sql.append(" p.backup_managerid10=").append(user.getObjectID()).append(" or ");
                //check if the user is in the same Department with this employee
                sql.append(" (tem.id in (select teamid from ").append(companyId).append(".teamemployee where employeeid=").append(user.getObjectID()).append(")) or ");
                //check if the user is in the same Project with this employee
                sql.append(" (p.id in (select pe2.projectid from ").append(companyId).append(".projectemployee pe2 left join ").
                        append(companyId).append(".teamemployee te2 on te2.id=pe2.employeedepartmentid where te2.employeeid=").append(user.getObjectID()).append(")) ");

                sql.append(")");
            }
        }
        // Get employee resource null
        if (fp.isResourceIdNull()) {
            sql.append(" and (e.resourceId is null) ");
        }

        if (fp.isFeatured()) {
            sql.append(" and e.endDate is not null ");
            sql.append(" and to_char(e.endDate, 'MM.dd') between to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate()) + "', 'yyyy.MM.dd'), 'MM.dd') ");
            sql.append(" and to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate()) + "', 'yyyy.MM.dd'), 'MM.dd') ");
        }

        if (fp.isDoNotExportToQB()) {
            sql.append(" and (e.quickbook_employee_id is null) ");
        }

        if (fp.getSqlSearchKey() != null) {
            if (isIntegerEmployeeCodeEnabled() && orderCode != null && !"".equals(orderCode)) {
                sql.append(" and ( to_char(pr.employeeCode, '999999') like '%").append(fp.getSqlSearchKey()).append("' ");
            } else {
                sql.append(" and (");
                sql.append("   lower(mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(mu.firstName || ' ' || mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(mu.lastName || ' ' || mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(po.name) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(re.name) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(tem.name) like '").append(fp.getSqlSearchKey()).append("' ");
                if (isIntegerEmployeeCodeEnabled()) {
                    sql.append("or to_char(pr.employeeCode, '999999') like '%").append(fp.getSqlSearchKey()).append("' ");
                } else {
                    sql.append("or lower(pr.employeeCode) like '%").append(fp.getSqlSearchKey()).append("' ");
                }
                sql.append("or lower(c.primaryPhone) like '").append(fp.getSqlSearchKey()).append("' ");
                if (fp.isEmailIncluded()) {
                    sql.append("or lower(mu.email) like '").append(fp.getSqlSearchKey()).append("' ");
                }
            }

            //searching by employee custom fields
            if (fp.isCustomFieldsShown() && cfList != null && cfList.size() > 0) {
                for (String ccfS : cfList) {
                    if (ccfS.contains("string_value")) {
                        sql.append(" or lower(ecf.").append(ccfS).append(") like '").append(fp.getSqlSearchKey()).append("' ");
                    }
                }
            }
            sql.append(") ");
        }

        // Sort by Employee Name
        if (!onlyCount) {
            String sortOrder = fp.isAscending() ? "" : " Desc";
            if (fp.getSortField() != null) {
                sql.append("ORDER BY ");
                if ("firstName".equals(fp.getSortField())) {
                    sql.append(" mu.firstname ");
                } else if ("lastName".equals(fp.getSortField())) {
                    sql.append(" mu.lastname ");
                } else if ("position".equals(fp.getSortField())) {
                    sql.append(" po.name ");
                } else if ("status".equals(fp.getSortField())) {
                    sql.append(" re.sorder ");
                } else if ("email".equals(fp.getSortField())) {
                    sql.append(" mu.email ");
                } else if ("startDate".equals(fp.getSortField())) {
                    sql.append(" e.startDate ");
                } else if ("role".equals(fp.getSortField())) {
                    sql.append(" mu.firstname ");
                } else if ("phoneNumber".equals(fp.getSortField())) {
                    sql.append(" c.primaryPhone ");
                } else if ("lastUpdate".equals(fp.getSortField())) {
                    sql.append(" e.lastUpdateTime ");
                } else if ("location".equals(fp.getSortField())) {
                    sql.append(" mu.firstname ");
                } else if ("mobile".equals(fp.getSortField())) {
                    sql.append(" mu.firstname ");
                } else if ("department".equals(fp.getSortField())) {
                    sql.append(" tem.name ");
                } else if ("EMPLOYEE_NAME".equals(fp.getSortField())) {
                    if (!fp.isAscending()) {
                        sql.append(" mu.firstname DESC, mu.lastname ");
                    } else {
                        sql.append(" mu.firstname, mu.lastname ");
                    }
                } else if ("employeeNumber".equals(fp.getSortField())) {
                    sql.append(" pr.employeeCode ");
                } else if (hasCustomFieldColumnName) {//sorting by employee custom fields
                    sql.append("ecf.").append(fp.getSortField()).append(" ");
                } else {
                    sql.append(" mu.firstname ");
                }
                sql.append(sortOrder);
            } else if (fp.isFeatured()) {
                sql.append(" ORDER BY e.endDate desc ");
            } else {
                if (!"".equals(orderCode)) {
                    sql.append(" ORDER BY pr.employeeCode asc ");
                } else {
                    sql.append(" ORDER BY mu.firstname asc ");
                }
            }

            if (fp.isDoNotExportToQB() && fp.getLimit() != null) {
                if (fp.getStart() == null) {
                    sql.append(" OFFSET 0 LIMIT ").append(fp.getLimit());
                } else {
                    sql.append(" OFFSET ").append(fp.getStart());
                    sql.append(" LIMIT ").append(fp.getLimit());
                }
            }
        }
        return sql.toString();
    }

    @Override
    public Boolean isIntegerEmployeeCodeEnabled() {
        return genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);
    }

    public List<EdsEmployee> empList(Integer companyId) {
        String sql = " SELECT  mu.*, e.* from \"" + companyId + "\".employee e " +
                " left join \"" + companyId + "\".myuser mu on (e.id=mu.id) WHERE mu.deleted is not true ";
        return findNative(sql, EdsEmployee.class);
    }

    @Override
    public List<EdsEmployee> empListForEOS(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct mu.*, e.* from " + getCompanyId() + ".employee e ");
        sql.append("left join " + getCompanyId() + ".myuser mu on e.id = mu.id ");
        sql.append("left join " + getCompanyId() + ".reference rf on rf.id = mu.accountstatusid ");
        sql.append("left join " + getCompanyId() + ".emp_batch eb ON e.id = eb.emp_id ");
        sql.append("JOIN " + getCompanyId() + ".employeepayrollsettings eps ON e.id = eps.employeeid AND eps.key = 'SALARY' AND eps.value IS NOT NULL AND eps.value != '' ");
        sql.append("WHERE (mu.deleted <> TRUE OR mu.deleted = TRUE AND rf.code = 'RESIGNED_EMPLOYEE') AND e.startdate IS NOT NULL");
        if (fp.getEmployeeId() != null) {
            sql.append(" AND e.id = ").append(fp.getEmployeeId());
        }
        if (fp.getPayrollBatchID() != null && fp.getPayrollBatchID() > 0) {
            sql.append(" and eb.batch_id = ").append(fp.getPayrollBatchID());
        }
        sql.append(" ORDER BY mu.firstname");
        return findNative(sql.toString(), EdsEmployee.class);
    }

    @Override
    public List<EmployeeListItem> getEmployeeList() {
        return getEmployeeList(null);
    }

    @Override
    public List<EmployeeListItem> getEmployeeList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        EdsUser user = getUser();
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct e.id as objectID, mu.firstname, mu.lastname, mu.middlename, po.name, c.primaryphone as phoneNumber, t.name as department,");
        sql.append(" mu.email, mu.active, po.name as position, 'N/A' as landing, e.startdate, e.lastupdatetime as lastUpdate, e.latitude, e.longitude ");
        sql.append(" from ").append(companyId).append(".employee e ");
        sql.append(" left outer join ").append(companyId).append(".myuser mu on (e.id=mu.id) ");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on (e.id = te.employeeid) ");
        sql.append(" left outer join ").append(companyId).append(".projectemployee pe on(te.id=pe.employeedepartmentid ) ");
        sql.append(" left outer join ").append(companyId).append(".project p on (p.id=pe.projectid) ");
        sql.append(" left outer join ").append(companyId).append(".team t on (t.id=te.teamid) ");
        sql.append(" left outer join ").append(companyId).append(".position po on (po.id = e.positionid) ");
        sql.append(" left outer join ").append(companyId).append(".employeeprofile pr on (pr.id = e.profileid) ");
        sql.append(" left outer join ").append(companyId).append(".crmcontact c on (c.id = pr.contact_id) ");
        sql.append(" left outer join ").append(companyId).append(".landing l on (l.id = mu.landingid) ");
        sql.append("where mu.deleted<>true ");

        if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and p.clientid=").append(fp.getClientId()).append(" ");
        }
//        }
        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" and pe.projectid=").append(fp.getProjectId()).append(" ");
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and te.teamid=").append(fp.getDepartmentId()).append(" ");
            sql.append(" and te.isdeleted<>true ");
        }
        if (fp.getTimeSlotID() != null && fp.getTimeSlotID() > 0) {
            sql.append(" and e.timeslotid=").append(fp.getTimeSlotID()).append(" ");
        }
        boolean seeAllEmployees = false;
        if ((Integer.valueOf(25608).equals(user.getCompany().getObjectID()) ||
                Integer.valueOf(8687).equals(user.getCompany().getObjectID())) && user.hasEitherRoles(EdsRole.ADMIN)) {
            seeAllEmployees = true;
        } else if ((!(Integer.valueOf(25608).equals(user.getCompany().getObjectID()) ||
                Integer.valueOf(8687).equals(user.getCompany().getObjectID()))) && (user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.TL))) {
            seeAllEmployees = true;
        }

        if (!seeAllEmployees && user.hasEitherRoles(EdsRole.DR, EdsRole.TL, EdsRole.HR, EdsRole.ADMIN_LOCATION) && (
                Integer.valueOf(25608).equals(user.getCompany().getObjectID()) || Integer.valueOf(8687).equals(user.getCompany().getObjectID()))) {
            EdsLocation location = user.getLocation();
            sql.append(" and (mu.locationId is not null)  and mu.locationId=").append(location != null
                    ? location.getObjectID()
                    : null);
        }

        // Admin, Director and HR should be able see ALl employees
        if (seeAllEmployees) {
            // he should be able to see all employees
        } else if (user.hasEitherRoles(EdsRole.CLIENT) || user.isClientContact()) {
            //Client should see only those employees who are working on his projects
            sql.append(" and p.clientid=").append(user.getClientContact().getClientID()).append(" ");
        } else {
            //Other roles should only see employees who matters to them
            //check if the user is TL or PM or Backup PM of this employee
            sql.append(" and (t.leaderid=").append(user.getObjectID()).append(" or p.managerid=").append(user.getObjectID()).
                    append(" or p.backup_managerid=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid2=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid3=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid4=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid5=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid6=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid7=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid8=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid9=").append(user.getObjectID()).append(" or ");
            sql.append(" p.backup_managerid10=").append(user.getObjectID()).append(" or ");
            //check if the user is in the same Department with this employee
            sql.append(" (t.id in (select teamid from ").append(companyId).append(".teamemployee where employeeid=").append(user.getObjectID()).append(")) or ");
            //check if the user is in the same Project with this employee
            sql.append(" (p.id in (select pe2.projectid from ").append(companyId).append(".projectemployee pe2 left join ").
                    append(companyId).append(".teamemployee te2 on te2.id=pe2.employeedepartmentid where te2.employeeid=").append(user.getObjectID()).append(")) ");

            sql.append(")");
        }
        // Get employee resource null
        if (fp.isResourceIdNull()) {
            sql.append(" and (e.resourceId is null) ");
        }

        if (fp.isDoNotExportToQB()) {
            sql.append(" and (e.quickbook_employee_id is null) ");
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append("   lower(mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.email) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }

        if (fp.getSortField() != null) {
            sql.append("ORDER BY ");
            if ("firstName".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("lastName".equals(fp.getSortField())) {
                sql.append(" mu.lastname ");
            } else if ("position".equals(fp.getSortField())) {
                sql.append(" po.name ");
            } else if ("status".equals(fp.getSortField())) {
                sql.append(" mu.active ");
            } else if ("email".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("startDate".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("role".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("phoneNumber".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("lastUpdate".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("location".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("mobile".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else if ("department".equals(fp.getSortField())) {
                sql.append(" mu.firstname ");
            } else {//default ordering -> firstName
                sql.append(" mu.firstname ");
            }
            if (!fp.isAscending()) {
                sql.append("DESC");
            }
        } else {
            sql.append(" ORDER BY mu.firstname asc ");
        }

        if (fp.isDoNotExportToQB() && fp.getLimit() != null) {
            sql.append(" OFFSET 0 LIMIT ").append(fp.getLimit());
        }

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(EmployeeListItem.class));
    }

    public ListingObjectItem<Object[]> getTeamOrAllEmployees(ListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        String lang = ServerUtils.getUserLocale().getLanguage();
        String companyID = getCompanyId();
        EdsUser user = getUser();
        final String avaialableStatus = "In"; //not change!!!
        final String lunchStatus = "Lunch";   //not change!!!
        final String outStatus = "Out";       //not change!!!
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ( ");
        sql.append(" select distinct mu.id as uid, (mu.firstName ||' ' || mu.lastName) as fullName, t.id as departmentId, ");
        switch (lang) {
            case "en" -> sql.append("COALESCE (rl.english, t.name) as departmentName,");
            case "ru" -> sql.append("COALESCE (rl.russian, t.name) as departmentName,");
            case "uz" -> sql.append("COALESCE (rl.uzbek, t.name) as departmentName,");
            case "ar" -> sql.append("COALESCE (rl.arabic, t.name) as departmentName,");
            default -> sql.append("t.name as departmentName, ");
        }
        sql.append(" ts.name as timeSlotName, ");
        // in/out status == IN or OUT or LUNCH
        sql.append("(");
        sql.append("CASE WHEN ");
        sql.append("(select tt.statusid from ").append(companyID).append(".timetrack tt where tt.employeeid = e.id and (tt.enddate is null) order by tt.startdate desc limit 1) is null ");
        sql.append(" THEN ");
        sql.append(" '").append(outStatus).append("' ");
        sql.append(" else ");
        sql.append("(select (CASE WHEN to_char(tt.startdate, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd') THEN ");
        sql.append("(CASE WHEN tt.statusid = ").append(20).append(" THEN ");
        sql.append(" '").append(avaialableStatus).append("' ");
        sql.append(" WHEN tt.statusid = ").append(21).append(" THEN ");
        sql.append(" '").append(outStatus).append("' ");
        sql.append(" ELSE ");
        sql.append(" '").append(lunchStatus).append("' ");
        sql.append(" END) ELSE ");
        sql.append("(CASE WHEN (tt.statusid = ").append(20).append(" or tt.statusid = ").append(22).append(") THEN null ELSE ").append(" '").append(outStatus).append("' ");
        sql.append(" END) END) ");
        sql.append("from ").append(companyID).append(".timetrack tt where tt.employeeid = e.id and (tt.enddate is null) order by tt.startdate desc limit 1) END ");
        sql.append(") as inOutStatus, ");

        sql.append(" mu.deleted as uDeleted, ");
        sql.append(" re.code as uActive, ");
        sql.append(" te.isdeleted as tedeleted, ");
        sql.append(" t.isdeleted as tdeleted, ");

        sql.append(" t.leaderid as leaderid, ");
        sql.append(" mu.locationId as locationId, ");
        sql.append(" e.timeslotid as timeslotid ");

        sql.append(" from ").append(companyID).append(".employee e ");
        sql.append(" left outer join ").append(companyID).append(".myuser mu on (e.id = mu.id) ");
        sql.append(" left join ").append(companyID).append(".reference re on re.id = mu.accountstatusid ");
        sql.append(" left outer join ").append(companyID).append(".teamemployee te on (e.employeedepartmentid = te.id) ");
        sql.append(" left outer join ").append(companyID).append(".team t on (t.id = te.teamid) ");
        sql.append(" left outer join ").append(companyID).append(".timeslot ts on (ts.id = e.timeslotid) ");
        sql.append(" left outer join ").append(companyID).append(".reference_locale rl ON rl.id = t.localeid");
        sql.append(" ) ttt ");

        sql.append(" where ttt.uDeleted is not true ");
        sql.append(" and ttt.tedeleted is not true and ttt.tdeleted is not true ");
        if (filterParameter.getDepartmentId() != null && filterParameter.getDepartmentId() > 0) {
            sql.append(" and ttt.departmentId=").append(filterParameter.getDepartmentId()).append(" ");
        }
        if (filterParameter.getTimeSlotID() != null && filterParameter.getTimeSlotID() > 0) {
            sql.append(" and ttt.timeslotid=").append(filterParameter.getTimeSlotID()).append(" ");
        }
        if (filterParameter.getLocationId() != null && filterParameter.getLocationId() > 0) {
            sql.append(" and ttt.locationId=").append(filterParameter.getLocationId()).append(" ");
        }
        Integer inOutStatusID = filterParameter.getStatusID();//not change statusIds (11, 22, 33)
        if (inOutStatusID != null) {
            if (inOutStatusID == 11) {
                //In
                sql.append(" and ttt.inOutStatus=").append(" '").append(avaialableStatus).append("' ");
            } else if (inOutStatusID == 22) {
                //Out
                sql.append(" and ttt.inOutStatus=").append(" '").append(outStatus).append("' ");
            } else if (inOutStatusID == 33) {
                //Lunch
                sql.append(" and ttt.inOutStatus=").append(" '").append(lunchStatus).append("' ");
            }
        }
        boolean seeAllEmployees = false;
        if ((Integer.valueOf(25608).equals(user.getCompany().getObjectID()) ||
                Integer.valueOf(8687).equals(user.getCompany().getObjectID())) && user.hasEitherRoles(EdsRole.ADMIN)) {
            seeAllEmployees = true;
        } else if ((!(Integer.valueOf(25608).equals(user.getCompany().getObjectID()) ||
                Integer.valueOf(8687).equals(user.getCompany().getObjectID()))) && (user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.TL))) {
            seeAllEmployees = true;
        }

        if (!seeAllEmployees && user.hasEitherRoles(EdsRole.DR, EdsRole.TL, EdsRole.HR, EdsRole.ADMIN_LOCATION) && (Integer.valueOf(3465).equals(user.getCompany().getObjectID()) ||
                Integer.valueOf(25608).equals(user.getCompany().getObjectID()) || Integer.valueOf(8687).equals(user.getCompany().getObjectID()))) {
            EdsLocation location = user.getLocation();
            sql.append(" and (ttt.locationId is not null)  and ttt.locationId=").append(location != null
                    ? location.getObjectID()
                    : null);
        }
        // Admin, Director and HR should be able see ALl employees
        if (seeAllEmployees) {
            // he should be able to see all employees
        } else {
            //Other roles should only see employees who matters to them
            //check if the user is TL of this employee
            sql.append(" and (ttt.leaderid=").append(user.getObjectID()).append(" or ");
            //check if the user is in the same Department with this employee
            sql.append(" (ttt.departmentId in (select teamid from ").append(companyID).append(".teamemployee where employeeid=").append(user.getObjectID()).append(" and (isdeleted <> true or isdeleted is null) )) ");
            sql.append(")");
        }

        if (filterParameter.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(ttt.fullName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(" or lower(ttt.departmentName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(" or lower(ttt.timeSlotName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }

        // Sorting
        if (filterParameter.getSortField() != null) {
            sql.append("ORDER BY ");
            if (TeammatesAvailability.AT_EMPLOYEE_NAME.equals(filterParameter.getSortField())) {
                sql.append("ttt.fullName ");
            } else if (TeammatesAvailability.AT_DEPARTMENT_NAME.equals(filterParameter.getSortField())) {
                sql.append("ttt.departmentName ");
            } else if (TeammatesAvailability.AT_TIME_SLOT_NAME.equals(filterParameter.getSortField())) {
                sql.append("ttt.timeSlotName ");
            } else if (TeammatesAvailability.AT_STATUS_NAME.equals(filterParameter.getSortField())) {
                sql.append("ttt.inOutStatus ");
            } else {
                sql.append("ttt.fullName ");//
            }
            if (!filterParameter.isAscending()) {
                sql.append("DESC ");
            }
        } else {
            sql.append(" ORDER BY ttt.fullName asc");
        }

        List<Object[]> listObject = findNative(sql.toString());
        int totalCount = listObject.size();
        if (filterParameter.getLimit() > 0) {
            listObject = ListUtils.getSublist(listObject, filterParameter.getStart(), filterParameter.getLimit());
        }

        return new ListingObjectItem<>(listObject, totalCount);
    }

    public List<EdsEmployee> getEmployees(EdsCompany company) {

        return find("select e from EdsEmployee e left join fetch e.employeeDepartment ed  " +
                "left join fetch ed.department d where e.deleted = false and ed.deleted = false order by d.name, e.firstName, e.lastName, e.middleName");

    }

    public List<EdsEmployee> getEmployeesForPayroll(EdsCompany company) {

        return find("select e from EdsEmployee e left join fetch e.employeeDepartment ed  " +
                "left join fetch ed.department d where e.deleted = false or e.accountStatus.code = 'RESIGNED_EMPLOYEE' order by d.name, e.firstName, e.lastName, e.middleName");

    }

    public List<EdsEmployee> getActiveEmployees(EdsCompany company) {

        return find("select e from EdsEmployee e left join fetch e.employeeDepartment ed  " +
                "left join fetch ed.department d where e.deleted = false and e.accountStatus.code != 'RESIGNED_EMPLOYEE' order by d.name, e.firstName, e.lastName, e.middleName");

    }

    public EdsEmployee getEmployeeByPlacementIds(Integer id) {
        if (id == null || "".equals(String.valueOf(id).trim())) {
            return null;
        }
        return (EdsEmployee) findSingle("SELECT emp FROM EdsEmployee emp where emp.placement=" + id);
    }

    public List<EdsEmployee> getEmployeeSortName(EdsCompany company) {

        return find("select e from EdsEmployee e left join fetch e.employeeDepartment ed  " +
                "left join fetch ed.department d where e.deleted = false and ed.deleted = false order by e.firstName, e.lastName, e.middleName, d.name");

    }

    public List<Object[]> getEmployeesWithDepartment() {

        return find("select distinct e,d, ep.employeeCode from EdsEmployee e left join e.employeeDepartment ed  " +
                "left join e.profile ep " +
                "left join ed.department d where e.deleted = false and ed.deleted = false order by ep.employeeCode");

    }

    public List<Integer> getEmployeeIds() {

        return find("select e.objectID from EdsEmployee e left join e.employeeDepartment ed  " +
                "left join ed.department d where e.deleted = false and ed.deleted = false order by e.objectID");

    }

    public List<EdsEmployee> getEmployeesByIds(String Ids) {
        if (Ids == null || "".equals(Ids.trim())) {
            return new ArrayList<>();
        }

        return (List<EdsEmployee>) find("SELECT e FROM EdsEmployee e WHERE e.objectID IN (" + Ids + ")");
    }

    public List<EdsEmployee> getEmployeesByPermissionCode(String permissionCode) {
        if (permissionCode != null) {
            String sql = "select distinct e from EdsEmployee e join e.roles er join er.rolePermissions rp where e.deleted<>true and rp.priviledgeCode = 'ALLOW' and rp.permissioncode = ?";
            return (List<EdsEmployee>) find(sql, permissionCode);

        }
        return null;
    }

    public List<EdsEmployee> getOwnersByPermission(String permissionCode) {
        String sql = "select distinct e from EdsEmployee e join e.roles er join er.rolePermissions rp join e.accountStatus st where e.deleted<>true and rp.priviledgeCode = 'ALLOW' and st.code in('" + Constants.EMPLOYEE_STATUS_ACTIVE + "','" + Constants.EMPLOYEE_STATUS_NO_ACCCESS + "') and rp.permissioncode = ? order by e.firstName";
        return (List<EdsEmployee>) find(sql, permissionCode);
    }

    public List<EdsEmployee> getAssignees(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append("select distinct (case when lower(firstname) like lower('" + fp.getSearchKey() + "') or lower(lastname) like lower('" + fp.getSearchKey() + "') then 1");
            sql.append(" when lower(firstname) like lower('" + fp.getSearchKey() + "%') or lower(lastname) like lower('" + fp.getSearchKey() + "%') then 2");
            sql.append(" else 3 end) rank,");
            sql.append(" u.*, e.* ");
            sql.append(getAssigneesBaseQuery(fp));
            sql.append(" order by rank ");
        } else {
            sql.append("select distinct e.*,0 as clazz_");
            sql.append(getAssigneesBaseQuery(fp));
        }
        if (fp.getLimit() > 0) {
            sql.append(" offset ").append(fp.getStart()).append(" limit ").append(fp.getLimit());
        }
        return findNative(sql.toString(), EdsEmployee.class);
    }

    public Integer getAssigneesTotalCount(ListingFilterParameter filterParameter) {

        return Integer.parseInt(findNativeSingle("SELECT count(distinct e.id)" + getAssigneesBaseQuery(filterParameter)).toString());
    }

    private StringBuilder getAssigneesBaseQuery(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(getCompanyId()).append(".employee e");
        sql.append(" inner join ").append(getCompanyId()).append(".myuser u on u.id = e.id");
        sql.append(" left join ").append(getCompanyId()).append(".reference st on st.id = u.accountstatusid");
        sql.append(" left join ").append(getCompanyId()).append(".myuser_role ur on ur.users_id = u.id");
        sql.append(" left join ").append(getCompanyId()).append(".role r on r.id = ur.roles_id");
        sql.append(" left join ").append(getCompanyId()).append(".rolepermission rp on rp.rolecode = r.code");
        sql.append(" where (u.deleted is null or u.deleted is not true)");
        if (StringUtils.isNotBlank(fp.getPermissionCode())) {
            sql.append(" and rp.access = 'ALLOW' and rp.permissioncode = '").append(fp.getPermissionCode()).append("'");
        }

        sql.append(" and st.code in ('").append(EdsEmployee.EMPLOYEE_STATUS_ACTIVE).append("','").append(EdsEmployee.EMPLOYEE_STATUS_NO_ACCCESS).append("')");
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and (");
            sql.append("    lower(firstname) like lower('" + fp.getSearchKey() + "')");
            sql.append(" or lower(lastname) like lower('" + fp.getSearchKey() + "')");
            sql.append(" or lower(firstname) like lower('" + fp.getSearchKey() + "%')");
            sql.append(" or lower(lastname) like lower('" + fp.getSearchKey() + "%')");
            sql.append(" or lower(firstname) like lower('%" + fp.getSearchKey() + "%')");
            sql.append(" or lower(lastname) like lower('%" + fp.getSearchKey() + "%')");
            sql.append(")");
        }
        return sql;
    }

    @Override
    public Map<String, Integer> getEmployeesByPermissionCodeAsMap(String permissionCode) {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT coalesce(m.firstName||' '||m.lastName),e.id  FROM " + getCompanyId() + ".employee e " +
                "LEFT JOIN " + getCompanyId() + ".myUser m ON (e.id=m.id) " +
                "LEFT JOIN " + getCompanyId() + ".reference st ON (st.id=m.accountstatusid) " +
                "LEFT JOIN " + getCompanyId() + ".myuser_role ur ON (ur.users_id=m.id) " +
                "LEFT JOIN " + getCompanyId() + ".role r ON (ur.roles_id=r.id) " +
                "LEFT JOIN " + getCompanyId() + ".rolepermission rp ON (rp.rolecode=r.code) " +
                "WHERE m.deleted is not true " +
                "AND st.code in ('" + EdsEmployee.EMPLOYEE_STATUS_ACTIVE + "','" + EdsEmployee.EMPLOYEE_STATUS_NO_ACCCESS + "') " +
                "AND rp.access = 'ALLOW' and rp.permissioncode = '" + permissionCode + "' ";
        List<Object[]> s = findNative(sql);
        if (s != null && s.size() > 0) {
            for (Object[] s_ : s) {
                if (s_ != null && s_.length > 1 && s_[0] != null && s_[1] != null) {
                    result.put(s_[0].toString(), Integer.valueOf(s_[1].toString()));
                    result.put(s_[0].toString().toLowerCase(), Integer.valueOf(s_[1].toString()));
                }
            }
        }
        return result;
    }

    public Long getEmployeesCountByCompany(Boolean active, Boolean withoutNoAccessAndFired, boolean withoutEssUser) {
        Map<String, Object> map = new HashMap<>();
        String sql;
        sql = "select count(distinct e.objectID) from EdsEmployee e join e.roles r where "
                + " (e.deleted=false or e.deleted is null)";
        if (withoutNoAccessAndFired) {
            sql = sql + " and not e.accountStatus.code=:no_access and not e.accountStatus.code=:fired ";
            map.put("no_access", EMPLOYEE_STATUS_NO_ACCCESS);
            map.put("fired", EMPLOYEE_STATUS_RESIGNED);
        }
        Integer essUserCount = 0;
        if (withoutEssUser) {
            String essUserSql = "select t.users_id, t.roles from (select ur.users_id, array_to_string(array_agg(r.code),',') roles " +
                    "                    from " + getCompanyId() + ".myuser_role ur\n" +
                    "                    join" + getCompanyId() + ".employee em on ur.users_id = em.id\n" +
                    "                    join" + getCompanyId() + ".myuser mu on mu.id = em.id\n" +
                    "                    join " + getCompanyId() + ".role r on ur.roles_id=r.id\n" +
                    " where (mu.deleted is null or mu.deleted = false)   group by ur.users_id) t where t.roles like '%ESS_USER%'";
            essUserCount = findNative(essUserSql).size();
            //TODO Shetini qarash esdan chiqmasin

        }
        if (active != null) {
            sql = sql + " and (e.accountStatus.code=:active";
            if (active) {
                map.put("active", EMPLOYEE_STATUS_ACTIVE);
            } else {
                map.put("active", EMPLOYEE_STATUS_INACTIVE);
            }
            sql += " OR e.accountStatus.code=:pending)";
            map.put("pending", EMPLOYEE_STATUS_PENDING);
        }
        return (Long) findSingleByNamedParams(sql, map) - essUserCount;
    }

    public Long getEssUsersCount(Boolean active) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select count(distinct e.objectID) from EdsEmployee e join e.roles r where ");
        sql.append(" (e.deleted=false or e.deleted is null)");
        sql.append(" and r.code=:ess_user and ( e.accountStatus.code=:pending ");
        map.put("ess_user", ESS_USER_CODE);
        map.put("pending", EMPLOYEE_STATUS_PENDING);

        if (active != null) {
            sql.append(" OR e.accountStatus.code=:active");
            if (active) {
                map.put("active", EMPLOYEE_STATUS_ACTIVE);
            } else {
                map.put("active", EMPLOYEE_STATUS_INACTIVE);
            }
        }
        sql.append(" ) ");
        return (Long) findSingleByNamedParams(sql.toString(), map);
    }

    public Long getNoAccessEmployeesCountByCompany() {
        Map<String, Object> map = new HashMap<>();
        String sql;
        sql = "select count(e.objectID) from EdsEmployee e where "
                + " (e.deleted=false or e.deleted is null)";
        sql = sql + " and (e.accountStatus.code=:no_access or e.accountStatus.code=:inactive) ";
        map.put("no_access", EMPLOYEE_STATUS_NO_ACCCESS);
        map.put("inactive", EMPLOYEE_STATUS_INACTIVE);
        return (Long) findSingleByNamedParams(sql, map);
    }

    public Long getEmployeesCountByDepartment(EdsDepartment department) {
        return (Long) findSingle("select count(*) from EdsEmployee e where e.employeeDepartment.department=?" +
                "  and (e.deleted=false)", department);
    }

    public List<EdsEmployee> getDirectors() {
        return getEmployeeByRole(EdsRole.DR);
    }


    public List<EdsEmployee> getEmployeeByRole(Integer roleID) {//
        return (List<EdsEmployee>) find("select e from EdsEmployee e join e.roles r where e.deleted<>true " +
                "and r.objectID = ?", roleID);
    }

    public List<EdsEmployee> getDirectors2() {
        return (List<EdsEmployee>) find("select e from EdsEmployee e join e.roles r where r.objectID = ?", EdsRole.DR);
    }

    public List<EdsEmployee> getCompanyOtherAdmins(Integer employeeId) {
        String sql = "SELECT muser.id FROM " + getCompanyId() + ".myuser muser \n" +
                "INNER JOIN " + getCompanyId() + ".myuser_role urole on muser.id= urole.users_id \n" +
                "INNER JOIN " + getCompanyId() + ".role mrole on urole.roles_id= mrole.id \n" +
                " WHERE mrole.code='" + EdsRole.ADMIN_CODE + "' AND muser.deleted is not true and muser.id != " + employeeId;

        return (List<EdsEmployee>) findNative(sql);
    }

    public List<EdsUser> getUserByRole(Integer roleID) {//
        String sql = "select e from EdsUser e join e.roles r " +
                "join e.accountStatus st " +
                "where e.deleted<>true and st.code<>'RESIGNED_EMPLOYEE' " +
                "and r.objectID = ? ";
        return (List<EdsUser>) find(sql, roleID);
    }


    public List<Integer> getEmployeesByApprover(EdsApprover approver, ListingFilterParameter filterParametrs) {
        List<Integer> roleIDs = new ArrayList<>();
        List<String> roleCodes = new ArrayList<>();
        List<Integer> employeeIDs = new ArrayList<>();
        roleIDs.add(0);
        employeeIDs.add(0);
        EdsUser user = filterParametrs.getEmployeeId() == null ? getUser() : get(filterParametrs.getEmployeeId());
        boolean isHierarChicalWorkflow = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.HIERARCHICAL_APPROVAL_WORKFLOW);
        if (isOk(approver.getApproverRoles())) {
            roleIDs.remove(0);
            for (EdsApproverRoles role : approver.getApproverRoles()) {
                roleIDs.add(role.getRole().getObjectID());
                roleCodes.add(role.getRole().getCode());
            }
        }
        if (isOk(approver.getApproverEmployees())) {
            employeeIDs.remove(0);
            for (EdsApproverEmployees employee : approver.getApproverEmployees()) {
                employeeIDs.add(employee.getEmployee().getObjectID());
            }
        }
        if (user instanceof EdsEmployee) {
            EdsEmployee employee = (EdsEmployee) user;
            if (isHierarChicalWorkflow && employee.getTeam() != null && roleCodes.contains(Constants.DLOFPR)) {
                List<Integer> leadersList = departmentTreeManager.getTreeTeamLeadersList(employee.getTeam().getObjectID());
                int lengthDepth = 0, currentStep = 0;
                for (EdsApprover edsApprover : approverManager.list(approver.getEntityType(), null)) {
                    if (edsApprover.getApproverRoles() != null && !edsApprover.getApproverRoles().isEmpty()) {
                        for (EdsApproverRoles edsApproverRoles : edsApprover.getApproverRoles()) {
                            if (Constants.DLOFPR.equals(edsApproverRoles.getRole().getCode())) {
                                if (lengthDepth < leadersList.size()) {
                                    lengthDepth++;
                                    if (edsApprover.equals(approver)) {
                                        currentStep = lengthDepth;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (currentStep != 0 && (leadersList.size() - currentStep) >= 0) {
                    employeeIDs.add(leadersList.get(leadersList.size() - currentStep));
                }
                roleCodes.remove(Constants.DLOFPR);// do not add team leader if HIERARCHICAL_APPROVAL_WORKFLOW check
            }

            for (EdsEmployee e : getApprovers(employee, roleCodes)) {
                if (isOk(e) && !employeeIDs.contains(e.getObjectID())) {
                    employeeIDs.add(e.getObjectID());
                }
            }
        }
        EdsLocation location = user.getLocation();
        boolean isLocation = approver.isLocation() != null ? approver.isLocation() : false;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT e.id ")
                .append("FROM ").append(getCompanyId()).append(".myuser e ")
                .append("LEFT JOIN ").append(getCompanyId()).append(".myuser_role r ON e.id = r.users_id ")
                .append("LEFT JOIN ").append(getCompanyId()).append(".reference ref ON e.accountstatusid = ref.id ")
                .append("WHERE e.deleted <> true and ref.code <> ").append("'") .append(EMPLOYEE_STATUS_NO_ACCCESS).append("' ");

        if (location != null && isLocation) {
            sql.append("AND e.locationId = ").append(location.getObjectID()).append(" ");
        }

        sql.append("AND (")
                .append("r.roles_id IN (").append(ServerUtils.integerListToString(roleIDs)).append(") ")
                .append("OR e.id IN (").append(ServerUtils.integerListToString(employeeIDs)).append(") ");

        if (isOk(approver.getDynamicQueries())) {
            for (EdsDynamicApprover query : approver.getDynamicQueries()) {
                sql.append(" or e.id in (").append(ServerUtils.evaluateQuery(query.getQuery(), filterParametrs)).append(") ");
            }
        }
        sql.append(")");
        if (filterParametrs.getSearchKey() != null) {
            sql.append(" and  (lower(e.lastName) like '%").append(filterParametrs.getSearchKey().toLowerCase()).append("%' or ").append("       lower(e.firstName) like '%").append(filterParametrs.getSearchKey().toLowerCase()).append("%' or ").append("       lower(e.middleName) like '%").append(filterParametrs.getSearchKey().toLowerCase()).append("%' ").append("      ) ");
        }
        if (filterParametrs.getLimit() != null && filterParametrs.getLimit() > 0) {
            return findNativeLimited(sql.toString(), filterParametrs.getStart(), filterParametrs.getLimit());
        }
        return (List<Integer>) findNative(sql.toString());
    }


    public List<EdsEmployee> getHRManagers() {
        return getEmployeeByRole(EdsRole.HR);
    }

    public List<EdsEmployee> getAdministrators() {
        return getEmployeeByRole(EdsRole.ADMIN);
    }

    public List<EdsEmployee> getLastEmployees() {
        return findLimited(
                "from EdsEmployee e where "
                        + "e.deleted<>true order by e.objectID DESC", 12, (Object[]) null
        );
    }

    public List<EdsDepartment> getDepartmentByUser(EdsUser user) {
        return find(
                "select distinct ed.department from EdsEmployeeDepartment ed "
                        + "where ed.employee =? and ( ed.deleted is null or ed.deleted <> true) ", user
        );
    }


    public List<EdsEmployee> getProjectManagers(EdsUser user) {
        List<EdsEmployee> projectManagers = new ArrayList<>();
        List<EdsProject> userProjects = projectManager.getUserProjects(user);
        if (userProjects != null && userProjects.size() > 0) {
            for (EdsProject project : userProjects) {
                //project manager
                EdsEmployee manager = project.getManager();
                if (manager != null && !projectManagers.contains(manager)) {
                    projectManagers.add(manager);
                }
                //project backup managers
                List<EdsEmployee> backupManagers = project.getBackupManagers();
                if (backupManagers != null && backupManagers.size() > 0) {
                    for (EdsEmployee backupManager : backupManagers) {
                        if (!projectManagers.contains(backupManager)) {
                            projectManagers.add(backupManager);
                        }
                    }
                }
            }
        }
        return projectManagers;
    }

    public EdsEmployee getSupervisor(EdsEmployee employee) {
        return getSupervisor(employee.getObjectID());
    }

    public EdsEmployee getSupervisor(Integer employeeID) {
        return (EdsEmployee) findSingle("SELECT e.profile.reportsTo FROM EdsEmployee e " +
                "WHERE (e.profile.reportsTo.deleted is null OR e.profile.reportsTo.deleted <> true) " +
                "AND e.objectID=" + employeeID);
    }

    public List<EdsEmployee> getApprovers(EdsEmployee empl, List<String> roleCodes) {
        //has department leader role
        List<String> roleCodeList = new ArrayList<>(roleCodes);
        boolean hasDepartmentLeaderRole = false;
        if (roleCodes.contains(EdsRole.TL_CODE) || roleCodes.contains(Constants.DLOFPR) || roleCodes.contains(Constants.DLOFPR2)
                || roleCodes.contains(Constants.DLOFPR3) || roleCodes.contains(Constants.DLOFPR4) || roleCodes.contains(Constants.DLOFPR5)) {
            hasDepartmentLeaderRole = true;
            roleCodes.remove(EdsRole.TL_CODE);
            roleCodes.remove(Constants.DLOFPR);
            roleCodes.remove(Constants.DLOFPR2);
            roleCodes.remove(Constants.DLOFPR3);
            roleCodes.remove(Constants.DLOFPR4);
            roleCodes.remove(Constants.DLOFPR5);
        }
        //has project manager role
        boolean hasProjectManagerRole = false;
        if (roleCodes.contains(EdsRole.PM_CODE) || roleCodes.contains(Constants.PMOFPR) || roleCodes.contains(Constants.BMOFPR)) {
            hasProjectManagerRole = true;
            roleCodes.remove(EdsRole.PM_CODE);
            roleCodes.remove(Constants.PMOFPR);
            roleCodes.remove(Constants.BMOFPR);
        }
        //has supervisor role
        boolean hasSupervisorRole = false;
        if (roleCodes.contains(EdsRole.SUPERVISOR_CODE)) {
            hasSupervisorRole = true;
            roleCodes.remove(EdsRole.SUPERVISOR_CODE);
        }
        ArrayList<EdsEmployee> approversList = new ArrayList<>();
        //Department leaders
        if (hasDepartmentLeaderRole) {
            List<EdsDepartment> teams = getDepartmentByUser(empl);
            if (teams != null && !teams.isEmpty()) {
                for (EdsDepartment team : teams) {
                    if (team != null && team.getLeader() != null) {
                        if (!approversList.contains(team.getLeader()) && (roleCodeList.contains(EdsRole.TL_CODE) || roleCodeList.contains(Constants.DLOFPR))) {
                            approversList.add(team.getLeader());
                        }
                        if (!approversList.contains(team.getLeader2()) && roleCodeList.contains(Constants.DLOFPR2)) {
                            approversList.add(team.getLeader2());
                        }
                        if (!approversList.contains(team.getLeader3()) && roleCodeList.contains(Constants.DLOFPR3)) {
                            approversList.add(team.getLeader3());
                        }
                        if (!approversList.contains(team.getLeader4()) && roleCodeList.contains(Constants.DLOFPR4)) {
                            approversList.add(team.getLeader4());
                        }
                        if (!approversList.contains(team.getLeader5()) && roleCodeList.contains(Constants.DLOFPR5)) {
                            approversList.add(team.getLeader5());
                        }
                    }
                }
            }
        }
        //Project Managers
        if (hasProjectManagerRole) {
            List<EdsEmployee> projectManagers = getProjectManagers(empl);
            if (projectManagers != null && !projectManagers.isEmpty()) {
                for (EdsEmployee projectManager : projectManagers) {
                    if (!approversList.contains(projectManager)) {
                        approversList.add(projectManager);
                    }
                }
            }
        }
        //Supervisor role
        if (hasSupervisorRole) {
            final EdsEmployee supervisor = empl != null ? getSupervisor(empl) : null;
            if (supervisor != null && !supervisor.getObjectID().equals(empl.getObjectID())) {
                if (!approversList.contains(supervisor)) {
                    approversList.add(supervisor);
                }
            }
        }
        //Admins, Directors, HRs, and other role users
        List<EdsEmployee> otherRoleUsers = getEmployeesForAccounting(null, roleCodes.toArray(new String[]{}));
        if (otherRoleUsers != null && !otherRoleUsers.isEmpty()) {
            for (EdsEmployee otherRoleUser : otherRoleUsers) {
                if (!approversList.contains(otherRoleUser)) {
                    approversList.add(otherRoleUser);
                }
            }
        }
        return approversList;
    }

    public List<EdsEmployee> getUnAssignedEmployees() {
        return find("from EdsEmployee e where e.deleted<>true "
                + "and e.employeeDepartment is null");
    }

    @Deprecated
    public List<EdsEmployee> getEmployeesByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime) {
        List list;
        if (company == null) {
            list = find("FROM EdsEmployee e where (e.creationTime between '" + sTime + "' and '" + eTime + "') ");
        } else if (includeUpdateTime) {
            list = find("FROM EdsEmployee e where ((e.creationTime between '" + sTime + "' and '" + eTime + "') or (e.lastUpdateTime between '" + sTime + "' and '" + eTime + "'))");
        } else {
            list = find("FROM EdsEmployee e where (e.creationTime between '" + sTime + "' and '" + eTime + "')");
        }
        return list;
    }

    public List<Date> getEmployeesRegisteredByDate(Date startDate, Date endDate) {
        Map<String, Date> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        StringBuilder sql = new StringBuilder();

        List<EdsCompany> companyList = jdbcSpringManager.getSchemaNameList();
        for (EdsCompany company : companyList) {
            Integer companyId = company.getObjectID();
            if (!company.getTestCompany()) {
                if (!"".contentEquals(sql)) {
                    sql.append("UNION ALL");
                }
                sql.append(" (select e.creationTime from \"" + companyId + "\".employee e ");
                sql.append(" left outer join " + getPublic() + ".usercompany uc on uc.userid = e.id and uc.companyid=" + companyId);
                sql.append(" left outer join " + getPublic() + ".company c on c.id = uc.companyid");
                sql.append(" where c.id=" + companyId);
                sql.append(" and e.creationTime >=:startDate and e.creationTime <=:endDate )");
            }
        }
        return (List<Date>) findNativeByNamedParams(sql.toString(), map);
    }

    public List<Date> getEmployeesActivationDates(Date startDate, Date endDate) {
        Map<String, Date> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        StringBuilder sql = new StringBuilder();
        List<EdsCompany> companyList = jdbcSpringManager.getSchemaNameList();
        for (EdsCompany company : companyList) {
            Integer companyId = company.getObjectID();
            if (!company.getTestCompany()) {
                if (!"".contentEquals(sql)) {
                    sql.append("UNION ALL");
                }
                sql.append(" (select e.activationDate from \"" + companyId + "\".employee e ");
                sql.append(" left outer join " + getPublic() + ".usercompany uc on uc.userid = e.id and uc.companyid=" + companyId);
                sql.append(" left outer join " + getPublic() + ".company c on c.id = uc.companyid");
                sql.append(" where c.id=" + companyId);
                sql.append(" and e.activationDate >=:startDate and e.activationDate <=:endDate )");
            }
        }
        return (List<Date>) findNativeByNamedParams(sql.toString(), map);
    }

    public List<EdsEmployee> getCompanyEmployees() {
        return (List<EdsEmployee>) find("from EdsEmployee e where e.deleted<>true");
    }

    public List<EdsEmployee> getCompanyEmployees(List<Integer> availableItems) {
        return (List<EdsEmployee>) find(
                "from EdsEmployee e where e.deleted<>true and e.objectID in (:ids)",
                Collections.singletonMap("ids", availableItems)
        );
    }

    public List<EdsEmployee> getInactiveEmployees() {
        Map<String, Object> map = new HashMap<>();
        map.put("inactive", EMPLOYEE_STATUS_INACTIVE);
        return (List<EdsEmployee>) findByNamedParams("from EdsEmployee e where e.accountStatus.code=:inactive", map);
    }

    public List<EdsEmployee> getEmployees(Integer timeSlotId) {
        return (List<EdsEmployee>) find("from EdsEmployee e where e.deleted <> true and e.timeSlot.objectID=? ", timeSlotId);
    }

    public List<EdsEmployee> getEmployeesByEmail(String s) {
        return find("from EdsUser e where e.email=?", s);
    }

    public List<EdsEmployee> getPositionEmployees(EdsPosition position) {
        return find("from EdsEmployee e where e.position = ? and e.deleted<>true", position);
    }

    public Long getPositionEmployeeCount(Integer id) {
        return (Long) findSingle("SELECT COUNT(e.objectID) FROM EdsEmployee e where e.position.objectID = ? and e.deleted<>true", id);
    }

    @Override
    public List<EdsEmployee> getPositionEmployees(Integer positionId) {
        return find("from EdsEmployee e where e.position.objectID = ? and e.deleted<>true order by e.firstName, e.lastName", positionId);

    }

    public List<EdsEmployee> getEmployeesForPayroll() {
        return find("select distinct eps.employee from EdsEmployeePayrollSettings eps");
    }

    public List<EdsEmployee> getCompanyEmployees(Integer companyid, Integer start, Integer limit) {

        return findLimited("SELECT e FROM EdsEmployee e WHERE e.objectID >=? AND " +
                " (e.deleted=false OR e.deleted is null) " +
                "ORDER BY e.objectID ASC", limit, start);

    }

    public List getEmployeesForDailyTimeSheetData(EdsUser employee, Date startDate, Date endDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeSheetDataRolePermission = "";
        Set<Integer> roles = employee.getRoleIds();
        if (roles.contains(EdsRole.ADMIN)) {

        } else if (roles.contains(EdsRole.DR)) {
            if (roles.contains(EdsRole.TL)) {
                timeSheetDataRolePermission += " AND (tem.leaderid=" + employee.getObjectID() + ") ";
            } else {
                timeSheetDataRolePermission += " AND (e.id=" + employee.getObjectID() + ") ";
            }
        } else if (roles.contains(EdsRole.TL)) {
            timeSheetDataRolePermission += " AND (tem.leaderid=" + employee.getObjectID() + ") ";
        } else {
            timeSheetDataRolePermission += " AND (e.id=" + employee.getObjectID() + ") ";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mu.id as eid,(mu.firstName ||' '|| mu.lastName) as employeeName,dj.from_date,tem.name as teamName, \n");
        sql.append("(SELECT (CASE WHEN atraw.dayoff is true OR atraw.holiday is true THEN 0 ELSE (CASE WHEN atraw.leave > 0 THEN atraw.timeslot - atraw.leave ELSE atraw.timeslot END) END)) as timeSlotT, \n");

        sql.append("(SELECT SUM(tsh.timespent) as timeSheet FROM").append(getCompanyId()).append(".timesheet tsh \n");
        sql.append("WHERE (dj.from_date=tsh.date) AND tsh.employeeid= mu.id)as timeSpentT, \n");

        sql.append("atraw.holiday,atraw.dayoff, \n");
        sql.append("(SELECT (CASE WHEN atraw.leave = atraw.timeslot AND atraw.leave !=0 AND atraw.employeeid=mu.id THEN true ELSE false END)) as leaveT \n");
        sql.append("FROM ").append(getPublic()).append(".datejoin dj \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".employee e ON (1=1) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myuser mu  ON (e.id=mu.id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".useremailsettings us ON (mu.id=us.userid) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=e.id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".teamEmployee te ON (te.id=e.employeeDepartmentId) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".team tem ON (tem.id=te.teamid) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".projectEmployee pe ON (pe.employeeDepartmentId = te.id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".project p ON (p.id=pe.projectid) \n");
        sql.append("WHERE (dj.from_date between '").append(dateFormat.format(startDate)).append("' AND '").append(dateFormat.format(endDate)).append("') \n");
        sql.append("AND us.timesheetrequired is true \n");
        sql.append("AND pe.isdeleted is not true AND p.isdeleted is not true \n");
        sql.append("AND te.isdeleted is not true \n");
        sql.append("AND mu.deleted is not true \n");
        sql.append(timeSheetDataRolePermission).append(" \n");
        sql.append("GROUP BY mu.id,atraw.employeeid,mu.firstName,mu.lastName,dj.from_date,tem.name,atraw.dayoff,atraw.holiday,atraw.leave,atraw.timeSlot \n");
        sql.append("ORDER BY employeeName,dj.from_date ");

        return findNative(sql.toString());
    }

    public List<EdsEmployee> getTeamEmployees(Integer departmentId) {
        EdsUser user = getUser();
        return getTeamEmployees(departmentId, user);
    }


    public List<EdsEmployee> getTeamEmployees(Integer departmentId, EdsUser user) {
        return find("select e from EdsEmployee e " +
                "left join e.employeeDepartment de " +
                "where de.deleted<>true and e.deleted<>true " +
                (departmentId != null ? " and de.department.objectID=" + departmentId : " ") +
                " order by e.firstName, e.lastName asc");
    }

    public List<EdsEmployee> getActiveTeamEmployees(Integer departmentId) {
        return find("select e from EdsEmployee e " +
                "left join e.employeeDepartment de " +
                "where de.deleted<>true and e.deleted<>true " +
                (departmentId != null ? " and de.department.objectID=" + departmentId : " ") +
                " order by e.firstName, e.lastName asc");
    }

    public Map<Integer, SelectItem> getTeamEmployeesForOrgChart(Integer departmentId, boolean showExternalEmployee, Integer typeExternalId) {
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en" -> nameLocale += "COALESCE (rl.english, po.name) ";
            case "ru" -> nameLocale += "COALESCE (rl.russian, po.name) ";
            case "uz" -> nameLocale += "COALESCE (rl.uzbek, po.name) ";
            case "ar" -> nameLocale += "COALESCE (rl.arabic, po.name) ";
            default -> nameLocale += "po.name";
        }

        List<Object[]> result = find(getOrgChatQuery(departmentId, showExternalEmployee, nameLocale));
        Map<Integer, SelectItem> itemMap = new LinkedHashMap<>();
        for (Object[] objects : result) {
            Integer id = (Integer) objects[0];
            String firstName = (String) objects[1];
            String lastName = (String) objects[2];
            String position = (String) objects[3];
            Integer photoID = (Integer) objects[4];
            String gender = (String) objects[5];

            SelectItem item = new SelectItem(id, firstName + " " + lastName, position, gender);
            item.setNumber(photoID != null ? String.valueOf(photoID) : null);
            itemMap.put(id, item);
        }
        return itemMap;
    }
    private static String getOrgChatQuery(Integer departmentId, boolean showExternalEmployee, String nameLocale) {
        String employeeTypeCondition = showExternalEmployee ? "po.type.code = 'TYPE_EXTERNAL'" : "po.type.code = 'TYPE_INTERNAL'";

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT e.objectID, e.firstName, e.lastName, ")
                .append(nameLocale).append(", ph.objectID, ep.gender ")
                .append("FROM EdsEmployee e ")
                .append("LEFT JOIN e.employeeDepartment de ")
                .append("LEFT JOIN e.profile ep ")
                .append("LEFT JOIN e.position po ")
                .append("LEFT JOIN e.position.locale rl ")
                .append("LEFT JOIN e.photo ph ")
                .append("WHERE ").append(employeeTypeCondition).append(" AND ")
                .append("(de.deleted IS NULL OR de.deleted = FALSE) ")
                .append("AND (e.deleted IS NULL OR e.deleted = FALSE) ")
                .append("AND e.accountStatus.code != 'RESIGNED_EMPLOYEE' ");

        if (departmentId != null) {
            sqlBuilder.append("AND de.department.objectID = ").append(departmentId).append(" ");
        }

        sqlBuilder.append("ORDER BY e.firstName ASC, e.lastName ASC");

        return sqlBuilder.toString();
    }

    public Map<Integer, Integer> getTeamEmployeesCount() {
        List<Object[]> result = find("select de.department.objectID, coalesce(count(e.objectID),0)  " +
                " from EdsEmployee e " +
                " join e.employeeDepartment de " +
                "where (de.deleted is null OR de.deleted=false) and (e.deleted is null OR e.deleted=false) " +
                " group by de.department.objectID ");
        Map<Integer, Integer> itemMap = new LinkedHashMap<>();
        for (Object[] objects : result) {
            itemMap.put((Integer) objects[0], Math.toIntExact((Long) objects[1]));
        }
        return itemMap;
    }

    public List<EdsEmployee> getTeamLocationEmployees(EdsDepartment department, EdsLocation location) {
        if (location != null) {
            return find("select e from EdsEmployee e left join fetch e.employeeDepartment left join e.employeeDepartment ed " +
                    "left join ed.department d " +
                    "where (d=? or e.location=?) and ( e.deleted = false and ed.deleted = false) order by d.name, e.firstName, e.lastName, e.middleName", department, location);
        } else {
            return find("select e from EdsEmployee e left join fetch e.employeeDepartment left join e.employeeDepartment ed " +
                    "left join ed.department d where d=? and ( e.deleted = false and ed.deleted = false) order by d.name, e.firstName, e.lastName, e.middleName", department);
        }
    }

    public List<Object[]> getEmployeesWithDepartmentByDepartmentAndLocation(EdsDepartment department, EdsLocation location) {
        if (location != null) {
            return find("select distinct e,d,ep.employeeCode from EdsEmployee e left join fetch e.employeeDepartment left join e.employeeDepartment ed " +
                    "left join e.profile ep " +
                    "left join ed.department d " +
                    "where (d=? or e.location=?) and ( e.deleted = false and ed.deleted = false) order by ep.employeeCode", department, location);
        } else {
            return find("select distinct e,d,ep.employeeCode from EdsEmployee e left join fetch e.employeeDepartment left join e.employeeDepartment ed " +
                    "left join e.profile ep " +
                    "left join ed.department d where d=? and ( e.deleted = false and ed.deleted = false) order by ep.employeeCode", department);
        }
    }

    public void deletePositionEmployee(EdsPosition position) {
        update("update EdsEmployee e set e.position = null where e.position =? and (e.deleted<>true or e.deleted is null)", position);
    }

    public EdsEmployee getEmployeeByProfileID(Integer profileID) {
        return (EdsEmployee) findSingle("select e from EdsEmployee e where e.profile.objectID = " + profileID);
    }

    public Integer getEmployeeByEmail(String email) {
        return (Integer) findSingle("select u.objectID from EdsUser u where u.email = '" + email + "'");
    }

    public EdsTimeSlot getEmployeeTimeSlot(Integer employeeID) {
        return (EdsTimeSlot) findSingle("select ep.timeSlot from EdsEmployee ep where ep.objectID = " + employeeID);
    }

    @Override
    public List<EdsEmployee> getEmployeesForAccounting(ListingFilterParameter filterParametrs, String[] roles) {
        StringBuilder query = new StringBuilder("select distinct e from EdsEmployee e join e.roles r left join fetch e.profile p where e.deleted<>true");

        EdsUser user = getUser();

        if (roles != null && roles.length > 0) {
            query.append(" and ((r.code in (");
            int i = 0;
            boolean isContainPMRole = false, isContainTLRole = false;
            for (String role : roles) {

                if (user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT)) {
                    i = roleAppend(query, i, role);
                }

                if (EdsRole.PM_CODE.equals(role)) {
                    isContainPMRole = true;
                    if (roles.length == 1) {
                        i = roleAppend(query, i, role);
                    }
                } else if (EdsRole.TL_CODE.equals(role)) {
                    isContainTLRole = true;
                    if (roles.length == 1) {
                        i = roleAppend(query, i, role);
                    }
                } else {
                    i = roleAppend(query, i, role);
                }
            }
            query.append(") ");
            if (isContainPMRole && filterParametrs != null && filterParametrs.getProjectId() != null) {
                query.append(" OR (r.code='" + EdsRole.PM_CODE + "' and e.objectID in (").append(ServerUtils.getAsCommoDelimited(projectManager.get(filterParametrs.getProjectId()).getManagerIDs(), "0")).append("))");
            }
            if (isContainTLRole && user instanceof EdsEmployee && ((EdsEmployee) user).getTeam() != null && ((EdsEmployee) user).getTeam().getLeader() != null) {
                query.append(" OR (r.code='" + EdsRole.TL_CODE + "' and e.employeeDepartment.department.leader.objectID=").append(((EdsEmployee) user).getTeam().getLeader().getObjectID()).append(")");
            }
            query.append(")) ");
        } else {
            return new LinkedList<>();
        }
        if (filterParametrs != null && !filterParametrs.isResignedEmployeesIncluded()) {
            query.append(" and e.accountStatus.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ");
        }
        if (filterParametrs != null && filterParametrs.getSqlSearchKey() != null) {
            query.append(" and (");
            query.append("lower(e.firstName) like '").append(filterParametrs.getSqlSearchKey()).append("' or ");
            query.append("lower(e.lastName) like '").append(filterParametrs.getSqlSearchKey()).append("' or ");
            query.append("lower(p.employeeCode) like '").append(filterParametrs.getSqlSearchKey()).append("'");
            query.append(")");
        }
        return find(query.toString());
    }

    private int roleAppend(StringBuilder query, int i, String role) {
        if (i != 0) {
            query.append(", ");
        }
        query.append("'");
        query.append(role);
        query.append("'");
        i++;
        return i;
    }

    @Override
    public int getEmpoyeeCountByTimeSlot(EdsTimeSlot ts) {
        final Object single = findSingle("select count(e.objectID) from EdsEmployee e where e.timeSlot=? ", ts);
        if (single == null) {
            return 0;
        }
        return Integer.valueOf(single.toString());
    }

    @Override
    public void onRolesChanged(EdsEmployee employee, ArrayList<Integer> removedRoleIDs, ArrayList<Integer> addedRoleIDs, ArrayList<Integer> intersect) {
        Set<Integer> allRoles = new HashSet<>();
        allRoles.addAll(removedRoleIDs);
        allRoles.addAll(addedRoleIDs);
        changeCrmActivityProjectEmployee(employee, allRoles, removedRoleIDs, addedRoleIDs);
    }

    private void changeCrmActivityProjectEmployee(EdsEmployee employee, Set<Integer> intersect, ArrayList<Integer> removedRoleIDs, ArrayList<Integer> addedRoleIDs) {
        ArrayList<Integer> crmActivityProjectRoles = new ArrayList<>();
        crmActivityProjectRoles.add(EdsRole.ADMIN);
        crmActivityProjectRoles.add(EdsRole.DR);
        crmActivityProjectRoles.add(EdsRole.SALESMAN);
        crmActivityProjectRoles.add(EdsRole.SALESPERSON);
        crmActivityProjectRoles.add(EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE);
        boolean add = false;
        boolean remove = false;
        for (Integer roleID : crmActivityProjectRoles) {
            if (!intersect.contains(roleID)) {
                if (addedRoleIDs.contains(roleID)) {
                    add = true;
                } else if (removedRoleIDs.contains(roleID)) {
                    remove = true;
                }
            } else {
                add = false;
                remove = false;
                break;
            }
        }
        if (employee != null && employee.getObjectID() != null && (add || remove)) {
            EdsProject crmProject = projectManager.getCrmProject();
            if (crmProject != null) {
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(ProjectCustomEventListenerImpl.TYPE, add
                        ? ProjectCustomEventListenerImpl.EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_ADD
                        : ProjectCustomEventListenerImpl.EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_REMOVE, crmProject, getUser());
                event.setCustomStringField(employee.getObjectID().toString());
            }
        }
    }

    public String getRolePermission() {

        EdsUser user = getUser();
        String rolePermission = "";

        if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_EMPLOYEES)) {
            //
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_DEPARTMENT_EMPLOYEES)) {
            Integer myTeamId = user.getEmployee().getEmployeeDepartment().getTeam().getObjectID();
            rolePermission += " AND (te.teamId=" + myTeamId + ") ";
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_PROJECT_EMPLOYEES)) {
            rolePermission += "AND e.id in \n" +
                    "(SELECT distinct e.id from " + getCompanyId() + ".task t\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".project p ON (p.id = t.projectid) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskid = t.id) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe ON (et.projectemployeeId=pe.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (pe.employeeDepartmentId=te.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employee e ON (te.employeeid=e.id)\n" +
                    "WHERE p.id in \n " +
                    "(SELECT distinct p.id from " + getCompanyId() + ".task t\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".project p ON (p.id = t.projectid) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskid = t.id) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe ON (et.projectemployeeId=pe.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (pe.employeeDepartmentId=te.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employee e ON (te.employeeid=e.id)\n" +
                    "WHERE e.id = " + user.getObjectID() + ")) ";
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_SUPERVISED_EMPLOYEES)) {
            rolePermission += " AND (e.id IN \n" +
                    "(SELECT e.id from " + getCompanyId() + ".employee e \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeprofile ep on (ep.employeeid=e.id) where ep.reportsto=" + user.getObjectID() + ")\n" +
                    " or te.employeeid = " + user.getObjectID() + ")";
        } else {
            rolePermission += " AND (e.id = " + user.getObjectID() + ") ";
        }
        return rolePermission;
    }

    public String getRolePermissionForPoject() {
        String rolePermissionForPoject = "";
        EdsUser user = getUser();

        if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_EMPLOYEES)) {
            //
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_DEPARTMENT_EMPLOYEES)) {
            Integer myTeamId = user.getEmployee().getEmployeeDepartment().getTeam().getObjectID();
            rolePermissionForPoject += " AND (te.teamid=" + myTeamId + ") ";
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_PROJECT_EMPLOYEES)) {
            rolePermissionForPoject += "AND p.id in \n" +
                    "(SELECT distinct p.id from " + getCompanyId() + ".task t\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".project p ON (p.id = t.projectid) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskid = t.id) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe ON (et.projectemployeeId=pe.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (pe.employeeDepartmentId=te.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employee e ON (te.employeeid=e.id)\n" +
                    "WHERE e.id = " + user.getObjectID() + ") ";
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_SUPERVISED_EMPLOYEES)) {
            rolePermissionForPoject += " AND (te.employeeid IN \n" +
                    "(SELECT e.id from " + getCompanyId() + ".employee e \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeprofile ep on (ep.employeeid=e.id) where ep.reportsto=" + user.getObjectID() + ")\n" +
                    " or te.employeeid = " + user.getObjectID() + ")";
        } else {
            rolePermissionForPoject += " AND (te.employeeid = " + user.getObjectID() + ") ";
        }
        return rolePermissionForPoject;
    }

    public List getResourceUtilReport(ListingFilterParameter fp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        EdsUser user = genericSettingsManager.getUser();
        String timeZoneCurrentUser = user.getUserTimezone().getID();
        //boolean withLunchTime = genericSettingsManager.isSettingsEnabled(EdsGenericSettings.ATTENDANCE_LUNCH_AND_COFFEE_TIME);
        boolean isFingerprintEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);

        String inOutHours = ", max(fpd.actualtime) as inOut \n";
        String joinTables = " ";

        //if (!withLunchTime) {
        joinTables = "left join (select tt.employeeid, tt.startdate , \n" +
                "(CASE WHEN SUM((EXTRACT(EPOCH FROM tt.endDate at time zone '" + timeZoneCurrentUser + "')-EXTRACT(EPOCH FROM tt.startDate at time zone '" + timeZoneCurrentUser + "'))/60)<0 THEN 0 \n" +
                "ELSE SUM((EXTRACT(EPOCH FROM tt.endDate at time zone '" + timeZoneCurrentUser + "')-EXTRACT(EPOCH FROM tt.startDate at time zone '" + timeZoneCurrentUser + "'))/60) END) as actualTime \n";
        /*} else {
            joinTables = "left join (select tt.employeeid, tt.startdate , \n" +
                    "(CASE WHEN SUM((EXTRACT(EPOCH FROM tt.endDate at time zone '" + timeZoneCurrentUser + "')-EXTRACT(EPOCH FROM tt.startDate at time zone '" + timeZoneCurrentUser + "'))/60- (tsi.lunchend - tsi.lunchstart) - (tsi.coffeeend - tsi.coffeestart))<0 THEN 0 \n" +
                    "ELSE SUM((EXTRACT(EPOCH FROM tt.endDate at time zone '" + timeZoneCurrentUser + "')-EXTRACT(EPOCH FROM tt.startDate at time zone '" + timeZoneCurrentUser + "'))/60 - (tsi.lunchend - tsi.lunchstart) - (tsi.coffeeend - tsi.coffeestart)) END) as actualTime \n";
        }*/

        if (!isFingerprintEnabled) {
            joinTables += " from " + getCompanyId() + ".timetrack tt " +
                    "left outer join " + getCompanyId() + ".employee e on e.id=tt.employeeid \n" +
                    //"left join " + getCompanyId() + ".timeslotitem tsi on tsi.timeslotid = e.timeslotid \n" +
                    "where tt.startDate is not null and tt.endDate is not null and tt.statusid=20 \n" +
                    "group by tt.employeeid, tt.startdate) fpd on fpd.employeeid=e.id \n" +
                    " and date(fpd.startdate)=date(dj.from_date) ";
        } else {
            joinTables += " from " + getCompanyId() + ".fingerprint tt " +
                    "left join " + getCompanyId() + ".userfingerprintdevice fd on fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id \n" +
                    "left outer join " + getCompanyId() + ".employee e on e.id=fd.userid \n" +
                    //"left join " + getCompanyId() + ".timeslotitem tsi on tsi.timeslotid = e.timeslotid \n" +
                    "where tt.startDate is not null and tt.endDate is not null and tt.statusid=20 \n" +
                    "group by tt.employeeid, tt.startdate) fpd on fpd.employeeid=e.id \n" +
                    " and date(fpd.startdate)=date(dj.from_date) ";
        }

        String positionSql = getPositionSql(fp);
        int activeEmployeeStsId = 0;
        if (fp.isShowActive()) {
            activeEmployeeStsId = getActiveEmployeeStatusId(activeEmployeeStsId);
        }
        String sv = ("SELECT DISTINCT ON (e.id, dj.from_date) dj.from_date,e.id as eid,(mu.firstName ||' '|| mu.lastName) as employeeName, \n" +                "atraw.dayoff, \n" +
                "(CASE WHEN atraw.dayoff is true OR atraw.holiday is true THEN 0 ELSE (CASE WHEN atraw.leave > 0 THEN atraw.timeslot - atraw.leave ELSE atraw.timeslot END) END) as timeSlotT, \n" +
                "atraw.holiday,atraw.leave,sr.reason, lr.shortname as leave_reason_shortname \n" +
                inOutHours +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "CROSS JOIN " + getCompanyId() + ".employee e \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON (mu.id=e.id) \n" +
                (fp.getEmployeeId() != null ? ("AND e.id=" + fp.getEmployeeId() + " \n") : "") +
                (fp.isShowActive() ? ("LEFT OUTER JOIN " + getCompanyId() + ".reference re ON (re.id=mu.accountstatusid)\n") : "") +
                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".sickrequestduration srd ON (dj.from_date = srd.date and srd.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "') \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".sickrequest sr ON (sr.id = srd.sickrequestid and atraw.employeeid = sr.employeeid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".leave_reason lr ON (sr.reason_code = lr.code) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".timesheet tsh ON (tsh.employeeid=e.id AND dj.from_date=tsh.date) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (e.employeeDepartmentId=te.teamId) \n" +
                joinTables +
                " WHERE (dj.from_date between '" + dateFormat.format(fp.getStartDate()) + "' AND '" + dateFormat.format(fp.getEndDate()) + "') \n" +
                positionSql +
                "AND mu.deleted is not true \n" +
                (fp.isShowActive() ? ("AND mu.accountstatusid =" + activeEmployeeStsId + " \n") : "") +
                getRolePermission() + " \n" +
                (fp.getEmployeeId() != null ? ("AND e.id=" + fp.getEmployeeId() + " \n") : "") +
                "GROUP BY dj.from_date,e.id,mu.firstName,mu.lastName,atraw.dayoff,atraw.timeSlot,atraw.timeSheet,atraw.holiday,atraw.leave,sr.reason, lr.shortname \n" +
                "ORDER BY e.id,dj.from_date");
        return findNative(sv);
    }

    private int getActiveEmployeeStatusId(int activeEmployeeStsId) {
        EdsReference reference = referenceManager.getByCode(Constants.EMPLOYEE_STATUS_ACTIVE);
        if (reference != null && reference.getObjectID() != null) {
            activeEmployeeStsId = reference.getObjectID();
        }
        return activeEmployeeStsId;
    }

    public List getOtherDetailsResourceUtilReport(ListingFilterParameter fp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoing = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);

        String dailyLoad = "SUM(CASE WHEN tsh.dailyestimatedtime is not null and tsh.dailyestimatedtime > 0 AND et.deleted is not true AND te.isdeleted is not true and t.startdate <= '" + dateFormat.format(fp.getEndDate()) + "' and pe.isdeleted is not true and te.isdeleted is not true THEN tsh.dailyestimatedtime ELSE 0 END) as dailyLoadT, \n";
        if (fp.isShowFilledCells()) {
            dailyLoad = "SUM(CASE WHEN tsh.dailyestimatedtime is not null and tsh.dailyestimatedtime > 0 AND et.deleted is not true AND te.isdeleted is not true and t.startdate <= '" + dateFormat.format(fp.getEndDate()) + "' and pe.isdeleted is not true and te.isdeleted is not true THEN tsh.dailyestimatedtime ELSE 0 END) as dailyLoadT, \n";
        }
        String positionSql = getPositionSql(fp);
        int activeEmployeeStsId = 0;
        if (fp.isShowActive()) {
            activeEmployeeStsId = getActiveEmployeeStatusId(activeEmployeeStsId);
        }
        String sv = ("SELECT dj.from_date,e.id as eid,(mu.firstName ||' '|| mu.lastName) as employeeName, \n" +
                dailyLoad +
                "SUM(CASE WHEN tsh.timeSpent is not null THEN tsh.timeSpent ELSE 0 END) as timeSpentT \n" +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "CROSS JOIN " + getCompanyId() + ".employee e \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON (mu.id=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".timesheet tsh ON (tsh.employeeid=e.id AND dj.from_date=tsh.date) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".project p ON (p.id=tsh.projectid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.id=tsh.employeetaskId) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe ON et.projectemployeeId=pe.id \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (e.employeeDepartmentId=te.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".task t ON (et.taskId=t.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re ON (re.id=p.statusid and re.isSystemReference is not true) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re2 ON (re2.id=et.statusid) \n" +
                " WHERE t.isissue is not true \n" +
                "AND (dj.from_date between '" + dateFormat.format(fp.getStartDate()) + "' AND '" + dateFormat.format(fp.getEndDate()) + "') \n" +
                positionSql +
                "AND t.deleted is not true \n" +
                "AND p.isdeleted is not true \n" +
                "AND mu.deleted is not true \n" +
                (fp.isShowActive() ? ("AND mu.accountstatusid =" + activeEmployeeStsId + " \n") : "") +

                getRolePermissionForPoject() + " \n" +

                (fp.getDepartmentId() != null ? ("AND te.teamid=" + fp.getDepartmentId() + " \n") : "") +
                (fp.getEmployeeId() != null ? ("AND e.id=" + fp.getEmployeeId() + " \n") : "") +
                (fp.getProjectId() != null ? ("AND p.id=" + fp.getProjectId() + " \n") : "") +
                ("AND (p.statusid=" + notStarted.getObjectID()) +
                (" OR p.statusid=" + ongoing.getObjectID()) +
                (" OR (re.deleted is not true)) \n") +
                "AND ((et.completedDate>='" + dateFormat.format(fp.getStartDate()) + "' AND re2.code='" + EdsTask.COMPLETED + "' )" +
                "OR (et.closeddate is not null AND et.closeddate>='" + dateFormat.format(fp.getStartDate()) + "' AND re2.code='" + EdsTask.CLOSED + "' )" +
                "OR re2.code='" + EdsTask.IN_PROGRESS + "' OR re2.code='" + EdsTask.NOT_STARTED + "' " +
                "OR re2.code='" + EdsTask.WAITING_FOR_SOMEONE_ELSE + "' " +
                "OR (re2.isSystemReference is not true and re2.deleted is not true) ) \n" +
                "GROUP BY dj.from_date, e.id, mu.firstName, mu.lastName \n" +
                "ORDER BY employeeName, dj.from_date");
        return findNative(sv);
    }

    private String getPositionSql(ListingFilterParameter fp) {
        String positionSql = " ";
        if (fp.getPositionIDs() != null && !fp.getPositionIDs().equals("") && fp.getNoPosition()) {
            positionSql = " AND (e.positionid in (" + fp.getPositionIDs() + " ) and  e.positionid is null )\n";
        } else if (fp.getPositionIDs() != null && !fp.getPositionIDs().equals("")) {
            positionSql = " AND e.positionid in (" + fp.getPositionIDs() + " )\n";
        } else if (fp.getNoPosition()) {
            positionSql = " AND e.positionid is null \n";
        }
        return positionSql;
    }

    @Override
    public ArrayList<Integer> getTimesheetRequiredEmployeesID() {
        String sql = "SELECT distinct userid from " + getCompanyId() + ".useremailsettings \n" +
                "WHERE timesheetrequired is true \n" +
                "AND userid in (select mu.id from " + getCompanyId() + ".myuser mu " +
                "left join " + getCompanyId() + ".reference r ON mu.accountstatusid=r.id " +
                "where mu.deleted is not true and r.code = '" + EMPLOYEE_STATUS_ACTIVE + "') \n";
        ArrayList<Integer> aNative = (ArrayList<Integer>) findNative(sql);
        ArrayList<Integer> roleIDs = new ArrayList<>();
        roleIDs.add(EdsRole.ADMIN);
        roleIDs.add(EdsRole.DR);
        roleIDs.add(EdsRole.TL);
        roleIDs.add(EdsRole.PM);
        roleIDs.add(EdsRole.ADMIN_LOCATION);
        List<Integer> admins = getActiveEmployeeIdsByRoleIds(roleIDs);
        aNative.removeAll(admins);
        aNative.addAll(admins);
        return aNative;
    }

    private List<Integer> getActiveEmployeeIdsByRoleIds(ArrayList<Integer> roleIDs) {
        String sql = "SELECT distinct e.id from " + getCompanyId() + ".employee e \n" +
                "LEFT JOIN " + getCompanyId() + ".myuser mu ON e.id=mu.id \n" +
                "LEFT JOIN " + getCompanyId() + ".reference r ON mu.accountstatusid=r.id \n" +
                "LEFT JOIN " + getCompanyId() + ".myuser_role mur ON mur.users_id=mu.id \n" +
                "WHERE mu.deleted is not true \n" +
                "AND r.code = '" + EMPLOYEE_STATUS_ACTIVE + "' \n" +
                "AND mur.roles_id in (" + ServerUtils.getAsCommoDelimited(roleIDs, "0", ",") + ") \n";
        return findNative(sql);
    }

    @Override
    public void updateRequired(String employeeIDs) {
        update("update EdsUserEmailSettings set timesheetrequired = true");
        update("update EdsUserEmailSettings set timesheetrequired = false where userid not in (" + employeeIDs + ")");
    }

    @Override
    public void updateAllRequired() {
        update("update EdsUserEmailSettings set timesheetrequired = false");
    }


    public List<EdsEmployee> getEmployeeByRoleCode(String roleCode) {//
        return (List<EdsEmployee>) find("select e from EdsEmployee e join e.roles r where e.deleted<>true " +
                "and r.code = ?", roleCode);
    }

    public Long getEmployeeCountByRoleCodeExceptByUserId(String roleCode, Integer userId) {
        return (Long) findSingle("select count(e.objectID) from EdsEmployee e join e.roles r where " + (userId != null ? " e.objectID = " + userId + " and " : "") + "e.deleted<>true " +
                "and r.code = ?", roleCode);
    }

    public List<EdsEmployee> getProjectManagers(EdsUser user, String managerType) {
        List<EdsEmployee> projectManagers = new ArrayList<>();
        List<EdsProject> userProjects = projectManager.getUserProjects(user);
        if (userProjects != null && userProjects.size() > 0) {
            for (EdsProject project : userProjects) {
                if (Constants.PMOFPR.equals(managerType)) {
                    //project manager
                    EdsEmployee manager = project.getManager();
                    if (manager != null && !projectManagers.contains(manager)) {
                        projectManagers.add(manager);
                    }
                } else {
                    //project backup managers
                    List<EdsEmployee> backupManagers = project.getBackupManagers();
                    if (backupManagers != null && backupManagers.size() > 0) {
                        for (EdsEmployee backupManager : backupManagers) {
                            if (!projectManagers.contains(backupManager)) {
                                projectManagers.add(backupManager);
                            }
                        }
                    }
                }
            }
        }
        return projectManagers;
    }

    public List<Object[]> getTimesheetBaseData(Integer employeeID, Date startDate, Date endDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("employeeID", employeeID);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("status", EdsTimeSheet._APPROVE);
        String sb = "select tsh.objectID,p.objectID,tsh.timeSpent, pe.objectID,tsh.date from EdsTimeSheet tsh" +
                "  left join tsh.employeeTask et" +
                "  left join et.task t  " +
                "  left join et.projectEmployee pe " +
                "  left join pe.employeeDepartment ed " +
                "  left join ed.employee e " +
                "  left join pe.project p where" +
                "  tsh.status.code =:status and tsh.employeeID=:employeeID  and tsh.date between :startDate and :endDate and tsh.payslipID is null and tsh.timeSpent > 0";
        return findByNamedParams(sb, map);
    }

    public EdsEmployee getEmployeeByFirstNameViaLastName(String firstNameViaLastName) {
        return (EdsEmployee) findSingle("SELECT e FROM EdsEmployee e WHERE (e.deleted is null or e.deleted is false) and trim(lower(trim(e.firstName) || ' ' || trim(e.lastName))) like ?", firstNameViaLastName.toLowerCase());
    }

    @Override
    public Integer getEmployeePayslipCount(Integer employeeId) {
        String sql = "select pti.id from " + getCompanyId() + ".payslipTableItem pti " +
                "left join " + getCompanyId() + ".reference ref on ref.id=pti.status_id" +
                " where " + ServerUtils.checkForDeleted("pti.deleted") + " and ref.code='PY_APPROVED'" + " and pti.employee_id=" + employeeId +
                " group by pti.id";
        return findNative(sql).size();
    }

    @Override
    public List<EmployeePayslipItem> getEmployeePayslips(ListingFilterParameter fp) {
        List<EmployeePayslipItem> result = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select pti.id as objectID, ref.name as status, ref.id as statusId, (mu.firstname || ' ' || mu.lastname) as creator, (ma.firstname || ' ' || ma.lastname) as approver,");
        sql.append(" pti.month, pti.monthID, (mu.firstname || ' ' || mu.lastname) as employee, pti.fromDate, pti.toDate, pti.daysworked,");
        sql.append(" pti.description, pti.basicSalary, pti.dailyRate, pti.actualMonthPay, pti.allowance, pti.additionalPay,");
        sql.append(" pti.deduction, pti.expense, pti.total, pti.year from ").append(getCompanyId()).append(".payslipTableItem pti ");
        sql.append(" left join ").append(getCompanyId()).append(".reference ref on ref.id=pti.status_id");
        sql.append(" left join ").append(getCompanyId()).append(".employee emp on emp.id=pti.preparer_id");
        sql.append(" left join ").append(getCompanyId()).append(".myuser mu on mu.id=emp.id");
        sql.append(" left join ").append(getCompanyId()).append(".employee ea on ea.id=pti.approver_id");
        sql.append(" left join ").append(getCompanyId()).append(".myuser ma on ma.id=ea.id");
        sql.append(" left join ").append(getCompanyId()).append(".employee e on e.id=pti.employee_id");
        sql.append(" left join ").append(getCompanyId()).append(".crmcontact me on me.id=e.id");
        sql.append(" where ").append(ServerUtils.checkForDeleted("pti.deleted")).append(" and  pti.monthID is not null and ref.code='PY_APPROVED'").append(" and pti.employee_id=").append(fp.getEmployeeId());
        sql.append(" group by pti.id, ref.name,ref.id, mu.firstname, mu.lastname, me.firstname, me.lastname, ma.firstname, ma.lastname, pti.fromDate, pti.toDate, " +
                "pti.daysworked, pti.description, pti.basicSalary, pti.dailyRate, pti.actualMonthPay, pti.allowance, pti.additionalPay, pti.deduction, " +
                "pti.expense, pti.total, pti.year ");

        if (fp.getSortField() != null) {
            if ("month".equals(fp.getSortField())) {
                sql.append(" order by pti.month ");
            } else if ("approver".equals(fp.getSortField())) {
                sql.append(" order by (ma.firstname || ' ' || ma.lastname) ");
            } else if ("preparer".equals(fp.getSortField())) {
                sql.append(" order by (mu.firstname || ' ' || mu.lastname) ");
            } else if ("status".equals(fp.getSortField())) {
                sql.append(" order by ref.name ");
            } else if ("total".equals(fp.getSortField())) {
                sql.append(" order by pti.total ");
            }
            if (fp.getSortDir() != null && fp.getSortDir() == 1) {
                sql.append(" asc ");
            } else {
                sql.append(" desc ");
            }
        } else {
            sql.append(" order by pti.fromDate desc");
        }

        if (fp.getLimit() > 0) {
            sql.append(" LIMIT " + fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET " + fp.getStart());
        }

        result = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(EmployeePayslipItem.class));
        return result;
    }

    @Override
    public List<Object[]> getTeamEmployees() {
        return find("select d, e from EdsEmployee e " +
                "join e.employeeDepartment de " +
                "join de.department d " +
                "where de.deleted<>true and e.deleted<>true and d.deleted<>true " +
                " order by d.name, e.firstName, e.lastName asc");
    }

    @Override
    public EdsEmployee getEmployeeByDriverNumber(Long driverNumber) {
        return (EdsEmployee) findSingle("SELECT e FROM EdsEmployee e WHERE (e.deleted<>true OR e.deleted is null) and e.driverNumber = " + driverNumber.toString());
    }

    @Override
    public List<SelectItem> getEmployeeListForPayroll(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        List<SelectItem> result = new ArrayList<>();
        boolean isEnableWithMiddle = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_FULLNAME_WITH_MIDDLENAME);
        filterParameter.setLookUp(false);
        sql.append("select distinct e.id as id,\n");
        sql.append(" case when ep.employeecode is not null then \n");
        sql.append("    ep.employeecode || ' -> ' || (coalesce(mu.firstName, '') || ' ' || coalesce(mu.lastName, '')");
        sql.append(isEnableWithMiddle ? " || ' ' || coalesce(mu.middleName, ''))\n" : ")\n");
        sql.append(" else (coalesce(mu.firstName, '') || ' ' || coalesce(mu.lastName, '')");
        sql.append(isEnableWithMiddle ? " || ' ' || coalesce(mu.middleName, '')) end as name \n" : ") end as name \n");
        sql.append(" from ").append(getCompanyId()).append(".employee e \n");
        sql.append("left join ").append(getCompanyId()).append(".myuser mu on e.id=mu.id \n");
        sql.append("left join ").append(getCompanyId()).append(".reference re on re.id = mu.accountstatusid \n");
        sql.append("left join ").append(getCompanyId()).append(".employeeprofile ep on ep.id = e.profileid \n");
        sql.append("left join ").append(getCompanyId()).append(".emp_batch eb on e.id = eb.emp_id \n");
        sql.append("left join ").append(getCompanyId()).append(".group_managers gm on gm.groupid = eb.batch_id \n");
        sql.append("where ").append(" e.id not in (select ec.employee_id from \n");
        sql.append(getCompanyId()).append(".eos_calculation ec where ").append(ServerUtils.checkForDeleted("ec.deleted")).append(")\n");

        if (filterParameter.isResignedEmployeesIncluded()) {
            sql.append(" and (re.code = '" + EMPLOYEE_STATUS_RESIGNED + "' ");
            if (filterParameter.isShowEmployeesWithResignationDate()) {
                sql.append(" or e.endDate is not null)");
            }
        } else if (!filterParameter.isAllEmployees()) {
            sql.append(" and re.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ");
        }
        if (!(roleManager.hasRole(getUser(), EdsRole.DR) || roleManager.hasRole(getUser(), EdsRole.ADMIN) || roleManager.hasRole(getUser(), EdsRole.HR)
                || ServerUtils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_FULL_ACCESS))) {
            sql.append(" and gm.managerid = " + getUser().getObjectID() + " ");
        }

        if (filterParameter.getSearchKey() != null) {
            sql.append(" and (");
            sql.append("   lower(mu.firstName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(ep.employeecode) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append("or lower(mu.lastName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.middleName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.firstName || ' ' || mu.lastName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.lastName || ' ' || mu.firstName || ' ' || mu.middleName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.lastName || ' ' || mu.firstName) like '").append(filterParameter.getSqlSearchKey()).append("')");
        }
        if (!filterParameter.isResignedEmployeesIncluded()) {
            sql.append(" and (mu.deleted is not true ");
            if (filterParameter.isAllEmployees()) {
                sql.append("or (mu.deleted is true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "')");
            }
            sql.append(") ");
        }
        if (filterParameter.isShowEvent()) {
            result.add(0, new SelectItem(0, "Select All"));
        }
        result.addAll(jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class)));

        return result;
    }

    @Override
    public List<SelectItem> getDriverListAsSelectItems(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct e.id as id,\n");
        sql.append("e.driver_number || ' -> ' || (coalesce(mu.firstName, '') || ' ' || coalesce(mu.lastName, '')) as name ,\n");
        sql.append(" (coalesce(mu.firstName, '') || ' ' || coalesce(mu.lastName, '')) as description \n");
        sql.append(" from ").append(getCompanyId()).append(".employee e \n");
        sql.append("left join ").append(getCompanyId()).append(".myuser mu on e.id=mu.id \n");
        sql.append("left join ").append(getCompanyId()).append(".reference re on re.id = mu.accountstatusid \n");
        sql.append("left join ").append(getCompanyId()).append(".employeeprofile ep on ep.id = e.profileid \n");
        sql.append("left join ").append(getCompanyId()).append(".emp_batch eb on e.id = eb.emp_id \n");
        sql.append("left join ").append(getCompanyId()).append(".group_managers gm on gm.groupid = eb.batch_id \n");
        sql.append("where e.driver_number is not null\n");
        sql.append("and e.id not in (select ec.employee_id from \n");
        sql.append(getCompanyId()).append(".eos_calculation ec where ").append(ServerUtils.checkForDeleted("ec.deleted")).append(")\n");

        if (filterParameter.isResignedEmployeesIncluded()) {
            sql.append(" and ((re.code = '" + EMPLOYEE_STATUS_RESIGNED + "' and mu.deleted is true) or re.code = '" + EMPLOYEE_STATUS_RESIGNED + "') ");
        } else {
            sql.append(" and re.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ");
        }
        if (!(roleManager.hasRole(getUser(), EdsRole.DR) || roleManager.hasRole(getUser(), EdsRole.ADMIN) || roleManager.hasRole(getUser(), EdsRole.HR))) {
            sql.append(" and gm.managerid = " + getUser().getObjectID() + " ");
        }

        if (filterParameter.getSearchKey() != null) {
            sql.append(" and (");
            sql.append("lower(CAST(e.driver_number AS text)) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.firstName) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append("or lower(mu.lastName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.firstName || ' ' || mu.lastName) like '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append("or lower(mu.lastName || ' ' || mu.firstName) like '").append(filterParameter.getSqlSearchKey()).append("')");
        }
        if (!filterParameter.isResignedEmployeesIncluded()) {
            sql.append(" and mu.deleted is not true ");
        }

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class));
    }

    @Override
    public EdsEmployee getEmployeeByNumber(String employeeCode) {
        return (EdsEmployee) findSingle("SELECT e FROM EdsEmployee e WHERE (e.deleted<>true OR e.deleted is  null) AND e.profile.employeeCode = '" + employeeCode + "'");
    }

    @Override
    public EdsEmployee getEmployeeByInn(String inn) {
        return (EdsEmployee) findSingle("SELECT e FROM EdsEmployee e WHERE (e.deleted<>true OR e.deleted is  null) AND e.customFields.stringValue5 = '" + inn + "'");
    }

    @Override
    public Map<String, EdsEmployee> getEmployeeMapByCode() {
        List<EdsEmployee> employees = find("SELECT e FROM EdsEmployee e WHERE (e.deleted<>true OR e.deleted is  null) and e.profile is not null");
        return employees.stream().collect(Collectors.toMap(employee -> employee.getProfile().getEmployeeCode(), employee -> employee, (p1, p2) -> p1));
    }

    @Override
    public Map<Integer, UserBankAccountData> getEmployeeBankAccountMap() {
        Map<Integer, UserBankAccountData> bankAccountDataMap = new HashMap<>();
        List<EdsUserBankAccount> edsBankAccounts = find("SELECT usb FROM EdsUserBankAccount usb where usb.user.deleted<>true or usb.user.accountStatus.code=? ", EMPLOYEE_STATUS_RESIGNED);
        for (EdsUserBankAccount userBankAccount : edsBankAccounts) {
            UserBankAccountData bankAccountData = new UserBankAccountData();
            bankAccountData.setBankName(userBankAccount.getBankName());
            bankAccountData.setBankAddress(userBankAccount.getBankAddress());
            bankAccountData.setAccountNumber(userBankAccount.getAccountNumber());
            bankAccountData.setAccountName(userBankAccount.getAccountName());
            bankAccountData.setSwiftCode(userBankAccount.getSwiftCode());
            bankAccountData.setSortCode(userBankAccount.getSortCode());
            bankAccountData.setIbanCode(userBankAccount.getIbanCode());
            bankAccountData.setAgentID(userBankAccount.getAgentID());

            bankAccountDataMap.put(userBankAccount.getUser().getObjectID(), bankAccountData);
        }
        return bankAccountDataMap;
    }

    public HashMap<Integer, HashMap<String, String>> getEmployeesPayrollSettingsMap() {
        HashMap<Integer, HashMap<String, String>> integerHashMapHashMap = new HashMap<>();
        String sql = " select distinct es.* " +
                " from " + getCompanyId() + ".EmployeePayrollSettings es ";
        List<EdsEmployeePayrollSettings> payrollSettings = findNative(sql, EdsEmployeePayrollSettings.class);
        for (EdsEmployeePayrollSettings settings : payrollSettings) {
            if (integerHashMapHashMap.get(settings.getEmployee().getObjectID()) != null) {
                integerHashMapHashMap.get(settings.getEmployee().getObjectID()).put(settings.getKey(), settings.getValue());
            } else {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put(settings.getKey(), settings.getValue());
                integerHashMapHashMap.put(settings.getEmployee().getObjectID(), stringStringHashMap);
            }

        }
        return integerHashMapHashMap;
    }

    @Override
    public List<EdsUser> getEmployeesForReminder(String columnCode, Integer companyId, String reminderTimeActions, String endReminderTimeAction) {
        String stB = ("select mu.*, 0 as clazz_ from " + getCompanyId() + ".employee e ") +
                "inner join \"" + companyId + "\".myuser mu on mu.id=e.id " +
                "inner join \"" + companyId + "\".employeeprofile emp on emp.id=e.profileId " +
                "inner join \"" + companyId + "\".reference st on mu.accountstatusid=st.id " +
                "left join \"" + companyId + "\".employeecustomfields itemcf on itemcf.id=e.customfields_id " +
                "where mu.deleted is not true and st.code !='RESIGNED_EMPLOYEE' and " +
                " (" + columnCode + "='" + reminderTimeActions + "'" +
                " OR " +
                columnCode + " between '" + reminderTimeActions + "' and '" + endReminderTimeAction + "')";
        return findNative(stB, EdsUser.class);
    }

    @Override
    public SelectItem getEmployeesById(Integer objectID) {
        Object[] obj = getEmployeeObjects(objectID, false);
        SelectItem item = new SelectItem();
        item.setId((Integer) obj[0]);
        item.setName((String) obj[1]);
        item.setDescription((String) obj[2]);
        if (obj[3] != null) {
            item.setCategory((String) obj[3]);
        }
        return item;
    }

    @Override
    public SelectItem getEmployeesWithTeamLeader(Integer objectID) {
        Object[] obj = getEmployeeObjects(objectID, true);
        SelectItem item = new SelectItem();
        item.setId((Integer) obj[0]);
        item.setName(String.valueOf(obj[1]));
        item.setDescription((String) obj[2]);
        if (obj[3] != null) {
            item.setCategory(String.valueOf(obj[3]));
        }
        return item;
    }

    private Object[] getEmployeeObjects(Integer objectID, boolean forHrReminder) {
        StringBuilder sql = new StringBuilder();
        String companyId = getCompanyId();
        if (forHrReminder) {
            sql.append("select e.id employeeid,tem.id teamid, tem.name teamname, tem.leaderId temleaderId ");
        } else {
            sql.append("select e.id employeeid, tem.name teamname, po.name positionname, ep.employeeCode ");
        }
        sql.append(" from ").append(companyId).append(".employee e ");
        sql.append(" join ").append(companyId).append(".myuser mu on e.id=mu.id ");
        sql.append(" left join ").append(companyId).append(".teamemployee te on e.id = te.employeeid and te.isdeleted is not true ");
        sql.append(" left join ").append(companyId).append(".team tem on tem.id=te.teamid and tem.isdeleted is not true ");
        sql.append(" left join ").append(companyId).append(".position po on po.id = e.positionid ");
        sql.append(" left join ").append(companyId).append(".employeeprofile ep on e.profileId = ep.id ");
        sql.append(" left join ").append(companyId).append(".reference re on re.id = mu.accountstatusid ");
        if (forHrReminder) {
            sql.append(" where (mu.deleted is not true and re.code !='" + EMPLOYEE_STATUS_RESIGNED + "') ");
        } else {
            sql.append(" where (mu.deleted is not true or (mu.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "')) ");
        }
        sql.append(" and e.id =" + objectID);
        return (Object[]) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsOnboardingStep> getOnboardingstepForReminder(String columnCode, Integer companyId, String formatDate, String endformatDate) {
        String stB = "select onbs.*, 0 as clazz_ from \"" + companyId + "\".onboardingstep onbs " +
                "left join \"" + companyId + "\".onboardingstepcustomfields itemcf on itemcf.id=onbs.onboardingstepcustomfieldsid " +
                "where onbs.deleted is not true and (" +
                columnCode + "='" + formatDate + "'" +
                " OR " +
                columnCode + " between '" + formatDate + "' and '" + endformatDate + "'" +
                ")";
        return findNative(stB, EdsOnboardingStep.class);
    }

    public List getProjectIds(ListingFilterParameter fp) {
        String sv = ("SELECT p.id as projectid\n" +
                "FROM " + getCompanyId() + ".timesheet ts \n" +
                "left join " + getCompanyId() + ".employeetask et on(et.id=ts.employeetaskid)\n" +
                "left join " + getCompanyId() + ".task ta on (et.taskid=ta.id )\n" +
                "left join " + getCompanyId() + ".projectemployee pe on(et.projectemployeeid=pe.id )\n" +
                "left join " + getCompanyId() + ".project p on (pe.projectid=p.id )\n" +
                "left join " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id )\n" +
                "left join " + getCompanyId() + ".myuser mu on (te.employeeid=mu.id)\n" +
                "left join " + getCompanyId() + ".team t on (t.id=te.teamid)\n" +
                "\n" +
                "WHERE ts.employeetaskid=et.id\n" +
                "and et.deleted<>true\n" +
                "and ta.deleted<>true \n" +
                "and pe.isdeleted<>true\n" +
                "and p.isdeleted<>true\n" +
                "and te.isdeleted<>true\n" +
                "and mu.deleted<>true\n" +
                "and t.isdeleted<>true\n" +

                (fp.getDepartmentId() != null ? ("AND t.id=" + fp.getDepartmentId() + " \n") : "") +
                (fp.getEmployeeId() != null ? ("AND mu.id=" + fp.getEmployeeId() + " \n") : "") +
                (fp.getProjectId() != null ? ("AND p.id=" + fp.getProjectId() + " \n") : "") +
                "group by p.id\n" +
                "order by p.id asc");
        return findNative(sv);
    }

    @Override
    public EdsEmployee getEmployeeByContact(EdsCrmContact contact) {
        return (EdsEmployee) findSingle("SELECT em FROM EdsEmployee em WHERE (em.deleted is null or em.deleted = false) AND em.profile.contact=?", contact);
    }

    public List getEmployeeIds(ListingFilterParameter fp) {
        String sv = ("SELECT mu.id as employeeid \n" +
                "FROM " + getCompanyId() + ".projectemployee pe\n" +
                "left join " + getCompanyId() + ".project p on (pe.projectid=p.id )\n" +
                "left join " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id )\n" +
                "left join " + getCompanyId() + ".myuser mu on (te.employeeid=mu.id)\n" +
                "left join " + getCompanyId() + ".team t on (t.id=te.teamid)\n" +
                "\n" +
                "WHERE pe.isdeleted<>true\n" +
                "and p.isdeleted<>true\n" +
                "and te.isdeleted<>true\n" +
                "and mu.deleted<>true\n" +
                "and t.isdeleted<>true\n" +

                (fp.getDepartmentId() != null ? ("AND t.id=" + fp.getDepartmentId() + " \n") : "") +
                (fp.getEmployeeId() != null ? ("AND mu.id=" + fp.getEmployeeId() + " \n") : "") +
                (fp.getProjectId() != null ? ("AND p.id=" + fp.getProjectId() + " \n") : "") +
                "group by mu.id\n" +
                "order by mu.id asc");
        return findNative(sv);
    }

    public List<Integer> getEmployeeIDs(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyId = getCompanyId();
        sql.append("select e.id ");
        sql.append(" from ").append(companyId).append(".employee e ");
        sql.append(" left outer join ").append(companyId).append(".myuser mu on (e.id=mu.id) ");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on (e.id = te.employeeid) ");
        sql.append(" left outer join ").append(companyId).append(".team tem on (tem.id=te.teamid) ");
        sql.append(" where ");
        sql.append(" mu.deleted<>true and te.isdeleted<>true and tem.isdeleted<>true ");
        if (fp.getObjectIDs() != null && fp.getObjectIDs().size() > 0) {
            sql.append(" and e.id in ('" + ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", "','")).append("') ");
        }
        if (fp.getStartDate() != null) {
            sql.append(" and e.startDate >= '" + fp.getStartDate()).append("'");
        }
        if (fp.getEndDate() != null) {
            sql.append(" and e.startDate <= '" + fp.getEndDate()).append("'");
        }
        sql.append(" order by e.id desc ");

        return findNative(sql.toString());
    }


    @Override
    public ArrayList<Integer> getEmployeePosition(Integer positionID) {
        Query query = masterEntityManager.createNativeQuery("SELECT e.id FROM " + getCompanyId() + ".employee e " +
                        " LEFT JOIN  " + getCompanyId() + ".position pt  ON e.positionId=pt.id " +
                        " LEFT JOIN " + getCompanyId() + ".myuser u ON e.id = u.id " +
                        " WHERE pt.id = ? AND pt.isDeleted <> TRUE AND u.deleted <> TRUE")
                .setParameter(1, positionID)
                .setMaxResults(1);

        List<Integer> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }

        return new ArrayList<>(resultList);

//        return (ArrayList<Integer>) find("select e.objectID from EdsEmployee e " +
//                "left join e.position pt " +
//                "where pt.objectID=?  and pt.deleted<>true and e.deleted<>true", positionID);
    }

    @Override
    public List<EdsEmployee> getEmployeeByPayrollBatch(Integer payrollBatchID) {
        return find("select e from EdsEmployee e join e.payrollBatches pb where pb.objectID = ?", payrollBatchID);
    }

    @Override
    public List<Integer> getEmployeeIDsByIDs(String IDs) {
        return find("select e.objectID from EdsEmployee e left join e.accountStatus re " +
                "where ((e.deleted is null OR e.deleted<>true) or (e.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "')) and e.objectID IN (" + IDs + ")");
    }

    @Override
    public List<Integer> getEmployeeIDsWithLimit(Integer startat, Integer limit) {
        return findInterval("select e.objectID from EdsEmployee e " +
                "left join e.accountStatus re " +
                "where e.objectID > ? AND (e.deleted<>true or (e.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "')) order by e.objectID ASC", startat, limit,  limit);
    }

    @Override
    public List<Integer> getCompanyDeletedEmployeeListForSolr(SolrReindexRpc solrReindex) {
        return (List<Integer>) find("select e.objectID from EdsEmployee e left join e.accountStatus re " + "where (e.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "') and e.lastUpdateTime>=" + "'" + solrReindex.getLastUpdateTime() + "'"
                + (solrReindex.getLastUpdateEndTime() != null ? " and e.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""));
    }

    @Override
    public List<EdsEmployee> getEmployeeItemListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select e from EdsEmployee e left join e.accountStatus re where ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append("e.lastUpdateTime >= :modifiedDate and ");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append("e.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("' and ");
            }
        }
        sqlQuery.append("(e.deleted<>true or (e.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "')) order by e.objectID asc ");
        return findIntervalByNamedParams(sqlQuery.toString(), start, limit, params);
    }

    public ArrayList<Integer> getLocationEmployees(Integer locationId) {
        return (ArrayList<Integer>) find("select e.objectID from EdsEmployee e where e.location.objectID=? and e.deleted<>true and (e.accountStatus.code = '" + EMPLOYEE_STATUS_ACTIVE + "' or e.accountStatus.code = '" + EMPLOYEE_STATUS_NO_ACCCESS + "')", locationId);
    }

    @Override
    public ArrayList<EdsEmployee> getEmployeesByLocation(Integer locationId) {
        return (ArrayList<EdsEmployee>) find("select e from EdsEmployee e where e.location.objectID=? and e.deleted<>true and (e.accountStatus.code = '" + EMPLOYEE_STATUS_ACTIVE + "' or e.accountStatus.code = '" + EMPLOYEE_STATUS_NO_ACCCESS + "')", locationId);
    }

    @Override
    public Long getEmployeesCountByHireDate(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(e.id) from EdsEmployee e left join e.accountStatus re ");
        sql.append("where (e.deleted <> true or (e.deleted=true and re.code = '" + EMPLOYEE_STATUS_RESIGNED + "')) ");
        if (fp.getStartDate() != null) {
            sql.append("and e.startDate is not null and e.startDate >= '" + fp.getStartDate() + "' ");
        }
        if (fp.getEndDate() != null) {
            sql.append(" and (e.startDate is null or e.startDate <= '" + fp.getEndDate() + "') ");
        }
        return (Long) findSingle(sql.toString());
    }

    @Override
    public ArrayList<Integer> getSupervisorIDsByEmployee(Integer employeeID) {
        String sql = "select e.id from " + getCompanyId() + ".employee e " +
                " left outer join " + getCompanyId() + ".myuser mu on (e.id=mu.id) " +
                " left outer join " + getCompanyId() + ".teamemployee te on (e.id = te.employeeid and te.isdeleted=false) " +
                " left outer join " + getCompanyId() + ".team tem on (tem.id=te.teamid) " +
                " left outer join " + getCompanyId() + ".position po on (po.id = e.positionid) " +
                " left outer join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid " +
                " left outer join " + getCompanyId() + ".employeeprofile pr on (pr.id = e.profileid) " +
                " where mu.deleted<>true and pr.reportsTo =" + employeeID;
        return (ArrayList<Integer>) findNative(sql);
    }

    @Override
    public List<EdsEmployee> getEmployeesBySupervisorId(Integer supervisorId) {
        String sql = "select e.*,mu.* from " + getCompanyId() + ".employee e " +
                " left outer join " + getCompanyId() + ".myuser mu on (e.id=mu.id) " +
                " left outer join " + getCompanyId() + ".teamemployee te on (e.id = te.employeeid and te.isdeleted=false) " +
                " left outer join " + getCompanyId() + ".team tem on (tem.id=te.teamid) " +
                " left outer join " + getCompanyId() + ".position po on (po.id = e.positionid) " +
                " left outer join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid " +
                " left outer join " + getCompanyId() + ".employeeprofile pr on (pr.id = e.profileid) " +
                " where mu.deleted<>true and pr.reportsTo =" + supervisorId;
        return findNative(sql, EdsEmployee.class);
    }

    @Override
    public void updateEmployeesPosition(ArrayList<Integer> oldEmployeeIDs, Integer positionID) {
        updateNative("update " + getCompanyId() + ".employee set positionID = " + positionID + " where id in (" + ServerUtils.getAsCommoDelimited(oldEmployeeIDs, "0", ",") + ")");
    }

    @Override
    public void setProbationDaystoAllEmployees(Double probationDays) {
        updateNativeByParams("update " + getCompanyId() + ".employee set probationperiods = ?", probationDays);
    }

    @Override
    public List<Integer> getByImportFileID(Integer entityID) {
        return (List<Integer>) findByNamedParams("select e.objectID from EdsEmployee e where e.importFileID = :entityID order by e.objectID asc ",
                preparing(new Entry("entityID", entityID)));
    }

    @Override
    public boolean userIsAssignToTask(Integer userID, Integer taskID) {
        String sql = " select mu.id from " + getCompanyId() + ".task t " +
                " join " + getCompanyId() + ".employeetask et on t.id=et.taskid and et.deleted is not true " +
                " join " + getCompanyId() + ".projectemployee pe on et.projectEmployeeId=pe.id and pe.isdeleted is not true " +
                " join " + getCompanyId() + ".teamemployee te on pe.employeeDepartmentId=te.id and te.isdeleted is not true " +
                " join " + getCompanyId() + ".myuser mu on te.employeeid=mu.id and mu.deleted is not true " +
                " where t.id=" + taskID + " and mu.id =" + userID;
        Integer userId = (Integer) findNativeSingle(sql);
        return userId != null && userId > 0;
    }

    @Override
    public Long[] getEmployeesGenderRatio() {
        if (StringUtils.isEmpty(ServerSecurityContext.getInstance().getCompanyId())) {
            return new Long[0];
        }
        String sql = "select (select count(*) " +
                " from " + getCompanyId() + ".employee e " +
                "  left join " + getCompanyId() + ".myuser mu on mu.id = e.id " +
                "  left join " + getCompanyId() + ".employeeprofile ep on ep.employeeid = e.id " +
                " where (mu.deleted is null or mu.deleted <> true or (mu.deleted is true and mu.accountstatusid = (select id from " + getCompanyId() + ".reference where code = 'EMPLOYEE_STATUS_RESIGNED')))" +
                "      and (mu.deleted is null or mu.deleted <> true) " +
                "      and ep.gender = 'Male') as male, " +
                " (select count(*) " +
                " from " + getCompanyId() + ".employee e " +
                "  left join " + getCompanyId() + ".myuser mu on mu.id = e.id " +
                "  left join " + getCompanyId() + ".employeeprofile ep on ep.employeeid = e.id " +
                " where (mu.deleted is null or mu.deleted <> true or (mu.deleted is true and mu.accountstatusid = (select id from " + getCompanyId() + ".reference where code = 'EMPLOYEE_STATUS_RESIGNED'))) " +
                "      and (mu.deleted is null or mu.deleted <> true) " +
                "      and ep.gender = 'Female') as female, " +
                " (select count(*) " +
                " from " + getCompanyId() + ".employee e " +
                "  left join " + getCompanyId() + ".myuser mu on mu.id = e.id " +
                "  left join " + getCompanyId() + ".employeeprofile ep on ep.employeeid = e.id " +
                " where (mu.deleted is null or mu.deleted <> true or (mu.deleted is true and mu.accountstatusid = (select id from " + getCompanyId() + ".reference where code = 'EMPLOYEE_STATUS_RESIGNED'))) " +
                "      and (mu.deleted is null or mu.deleted <> true) " +
                "      and (ep.gender is null or ep.gender not in ('Male', 'Female'))) as hezalak, " +
                "  count(*) as total from " + getCompanyId() + ".employee e " +
                " left join " + getCompanyId() + ".myuser mu on mu.id = e.id " +
                " left join " + getCompanyId() + ".employeeprofile ep on ep.employeeid = e.id " +
                "  where (mu.deleted is null or mu.deleted <> true or (mu.deleted is true and mu.accountstatusid = (select id from " + getCompanyId() + ".reference where code = 'EMPLOYEE_STATUS_RESIGNED'))) " +
                " and (mu.deleted is null or mu.deleted <> true);";
        List<Object[]> objects = (List<Object[]>) findNative(sql);
        Long[] result = new Long[4];
        int count = 0;
        Object[] values = objects.get(0);
        for (Object value : values) {
            result[count++] = ((BigInteger) value).longValue();
        }
        return result;
    }

    @Override
    public List<Integer> getChildEmployees(Integer leadID) {
        return find("select objectID from EdsEmployee e where profile.reportsTo.objectID = " + leadID + "and e.deleted = false");
    }

    @Override
    public List<Integer> getDepartmentAndChildDepartmentEmployeeIds(Integer leadID) {
        String sql = "select e.id from " + getCompanyId() + ".teamEmployee te \n" +
                "join " + getCompanyId() + ".team t on te.teamId=t.id \n" +
                "join " + getCompanyId() + ".employee e on te.employeeId = e.id \n" +
                "where t.isdeleted <> true and t.leaderId = " + leadID;
        return (List<Integer>) findNative(sql);
    }

    @Override
    public List<EdsEmployee> getChildEmployeesObject(Integer supervisorId) {
        return find("select e from EdsEmployee e where profile.reportsTo.objectID = " + supervisorId + "and e.deleted = false and e.accountStatus.code = 'ACTIVE_EMPLOYEE'");
    }

    @Override
    public void updateEmployeeLocation(Set<Integer> employeesId, Integer locationId) {
        ArrayList<Integer> empIds = new ArrayList<>(employeesId);
        updateNative("update " + getCompanyId() + ".myUser set locationId = " + locationId + " where id in (" + ServerUtils.getAsCommoDelimited(empIds, "0") + ")");
    }

    public void updateEmployeeLocationByTeam(Set<Integer> teamIds, EdsLocation location) {
        ArrayList<Integer> teamId = new ArrayList<>(teamIds);
        update("update EdsUser u set u.location = ? where u.id in (select e.id from EdsEmployee e where e.employeeDepartment.department.objectID in (" + ServerUtils.getAsCommoDelimited(teamId, "0") + "))", location);
    }

    @Override
    public EdsEmployee getEmployeeByUniqueId(String approverUniqueId) {
        return (EdsEmployee) findSingle("select e from EdsEmployee e where e.externalGUID = '" + approverUniqueId + "'");
    }

    @Override
    public Long getEmployeesCountByPosition(EdsPosition position) {
        return (Long) findSingle("select count(*) from EdsEmployee e where e.position = ?  and ( e.deleted is null or e.deleted <> true) and e.accountStatus.code = 'ACTIVE_EMPLOYEE' ", position);
    }

    @Override
    public ArrayList<SelectItem> getAvailablePositionsForOrgChart(Integer departmentId) {
        ArrayList<SelectItem> items = new ArrayList<>();
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en" -> nameLocale += "COALESCE (rl.english, p.name) ";
            case "ru" -> nameLocale += "COALESCE (rl.russian, p.name) ";
            case "uz" -> nameLocale += "COALESCE (rl.uzbek, p.name) ";
            case "ar" -> nameLocale += "COALESCE (rl.arabic, p.name) ";
            default -> nameLocale += "p.name";
        }
        String sql = "SELECT CAST(p.counter AS INTEGER)-COUNT(e.*), p.id, " + nameLocale + " " +
                "FROM " + getCompanyId() + ".position p " +
                "LEFT JOIN " + getCompanyId() + ".employee e ON e.positionid = p.id " +
                "LEFT JOIN " + getCompanyId() + ".teamEmployee te ON e.employeeDepartmentId = te.id " +
                "LEFT JOIN " + getCompanyId() + ".reference_locale  rl  ON p.localeId = rl.id " +
                "LEFT JOIN " + getCompanyId() + ".myuser m ON e.id = m.id " +
                "WHERE p.isdeleted IS NOT TRUE AND m.deleted IS NOT TRUE AND p.status = (SELECT id FROM " + getCompanyId() + ".reference WHERE code = 'POS_STATUS_ACTIVE') AND p.department =  " + departmentId +
                " AND te.teamId = " + departmentId +
                " GROUP BY p.id, p.counter, " + nameLocale + " " +
                " HAVING CAST(p.counter AS INTEGER)-COUNT(e.*) > 0 " +
                " ORDER BY CAST(p.counter AS INTEGER)-COUNT(e.*) DESC;";
        List<Object[]> objects = findNative(sql);
        for (Object[] object : objects) {
            BigInteger availablePositionCount = (BigInteger) object[0];
            Integer depId = (Integer) object[1];
            String positionName = (String) object[2];
            items.add(new SelectItem(-depId, String.valueOf(availablePositionCount.intValue()), positionName));
        }
        return items;
    }


    @Override
    public List<EdsEmployee> getEmployeesForForteensSalary(LocalDate date) {

        String sql = "select e from EdsEmployee e WHERE EXTRACT(MONTH FROM e.startDate) = " + date.getMonth().getValue() +
                " and EXTRACT(YEAR FROM startDate) < " + date.getYear() +
                " and 'FIXED_ATTENDANCE_REPORT_OVERTIME_RATE' = (select s.value from EdsEmployeePayrollSettings s where s.employeeId = e.objectID and s.key = 'RATE_TYPE') " +
                " and ( e.deleted is null or e.deleted <> true) and e.accountStatus.code = 'ACTIVE_EMPLOYEE'";

        return find(sql);

    }

    @Override
    public List<EdsEmployee> getEmployeesByDepartment(Integer departmentId,boolean isActiveEmployee) {
        StringBuilder query = new StringBuilder();
        query.append(" select e from EdsEmployee e ");
        query.append(" where  e.deleted is null or e.deleted <> true");
        if (isActiveEmployee) {
            query.append(" and e.accountStatus.code = 'ACTIVE_EMPLOYEE'");
        }
        query.append(" and  e.employeeDepartment.department.objectID = ").append(departmentId);
        query.append(" order by e.firstName");

        return find(query.toString());
    }

    @Override
    public List<EdsEmployee> getEmployeesByFilter(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select DISTINCT e.*,mu.* ")
                .append(getEmployeesBaseQuery(filterParameter));

        if (CustomFormConstants.EMPLOYEE_CODE.equals(filterParameter.getSortField())) {
            sql.append(" ORDER BY e.code ASC ");
        } else {
            sql.append(" ORDER BY mu.firstname ASC ");
        }

        Query query = this.slaveEntityManager.createNativeQuery(sql.toString(), EdsEmployee.class);
        if (!StringUtil.isEmpty(filterParameter.getSearchKey())) {
            query = query.setParameter("searchKey", filterParameter.getSqlSearchKey());
        }
        if (filterParameter.getLimit() != null && filterParameter.getLimit() > 0) {
            query = query.setMaxResults(filterParameter.getLimit());
        }
        if (filterParameter.getStart() != null) {
            query = query.setFirstResult(filterParameter.getStart());
        }
        return query.getResultList();
    }

    @Override
    public Integer getEmployeesCountByFilter(ListingFilterParameter filterParameter) {
        String sql = "SELECT count(distinct e.id) " +
                getEmployeesBaseQuery(filterParameter);

        Query query = this.masterEntityManager.createNativeQuery(sql);
        if (!StringUtil.isEmpty(filterParameter.getSearchKey())) {
            query = query.setParameter("searchKey", filterParameter.getSqlSearchKey());
        }

        final List<BigInteger> list = query.getResultList();
        return list.isEmpty() ? 0 : list.get(0).intValue();
    }

    @Override
    public EdsEmployee getEmployeeByCode(String employeeCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e from EdsEmployee e where e.profile.employeeCode = '").append(employeeCode).append("'");
        return (EdsEmployee) findSingle(sql.toString());
    }

    @Override
    public EdsEmployee getEmployeeByPassportNumber(String passportNumber) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e from EdsEmployee e where trim(e.profile.passportNumber) = '").append(passportNumber.trim()).append("'");
        return (EdsEmployee) findSingle(sql.toString());
    }

    @Override
    public EdsEmployee getEmployeeByFirstAndLastName(String firstName, String lastName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e from EdsEmployee e where e.firstName= '").append(firstName).append("' and e.lastName= '").append(lastName).append("'");
        return (EdsEmployee) findSingle(sql.toString());
    }

    @Override
    public List<EdsEmployee> getEmployeesForVerification(String text) {
        StringBuilder query = new StringBuilder();
        query.append("select e from EdsEmployee e where LOWER(e.firstName) LIKE LOWER('" + text + "') OR LOWER(e.lastName) LIKE LOWER('" + text + "')  OR LOWER(e.middleName) LIKE LOWER('" + text + "') ");
        query.append(" OR LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER('" + text + "') ");
        query.append(" OR LOWER(CONCAT(e.firstName, ' ', e.lastName, ' ', e.middleName)) LIKE LOWER('" + text + "')");
        query.append(" OR LOWER(e.profile.employeeCode) LIKE LOWER('" + text + "') ");
        query.append(" OR LOWER(e.profile.passportNumber) LIKE LOWER('" + text + "') ");
        return find(query.toString());
    }

    @Override
    public List<EdsEmployee> getEmployeesByData(ProfileItem profileItem) {
        StringBuilder query = new StringBuilder();
        query.append("select e from EdsEmployee e where 1 = 1");
        if (profileItem.getEmpCode() != null && !profileItem.getEmpCode().isEmpty()) {
            query.append(" AND LOWER(e.profile.employeeCode) LIKE LOWER('" + profileItem.getEmpCode() + "') ");
        }
        if (profileItem.getPassportNumber() != null && !profileItem.getPassportNumber().isEmpty()) {
            query.append(" AND LOWER(e.profile.passportNumber) LIKE LOWER('" + profileItem.getPassportNumber() + "') ");
        }
        if (profileItem.getFirstName() != null &&  !profileItem.getFirstName().isEmpty()) {
            query.append(" AND LOWER(e.firstName) LIKE LOWER('" + profileItem.getFirstName() + "') ");
        }
        if (profileItem.getLastName() != null &&  !profileItem.getLastName().isEmpty()) {
            query.append(" AND LOWER(e.lastName) LIKE LOWER('" + profileItem.getLastName() + "') ");
        }
        return find(query.toString());
    }

    private String getEmployeesBaseQuery(ListingFilterParameter filterParameter) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        if (filterParameter.getMonthId() != null) {
            calendar.set(2, filterParameter.getMonthId());
        }

        if (filterParameter.getYear() != null) {
            calendar.set(1, filterParameter.getYear());
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(getCompanyId()).append(".employee e ");
        sql.append(" left join ").append(getCompanyId()).append(".myuser mu on e.id=mu.id ");
        sql.append(" left join ").append(getCompanyId()).append(".employeeprofile ep ON ep.id = e.profileid ");
        sql.append(" left join ").append(getCompanyId()).append(".teamemployee te on e.id = te.employeeid");
        sql.append(" left join ").append(getCompanyId()).append(".emp_batch eb ON e.id = eb.emp_id ");
        sql.append(" left join ").append(getCompanyId()).append(".employeelocation el on e.id = el.userid ");
        sql.append(" left join ").append(getCompanyId()).append(".reference re on re.id = mu.accountstatusid ");

        sql.append(" where 1=1 ");
        if (filterParameter.getEmployeeId() != null) {
            sql.append(" and e.id =").append(filterParameter.getEmployeeId());
        } else if (filterParameter.getEmployeeIDs() != null) {
            sql.append(" and e.id in (").append(filterParameter.getEmployeeIDs()).append(")");
        } else {
            sql.append("and ((").append(ServerUtils.checkForDeleted("mu.deleted")).append(") or (e.enddate is null or e.enddate >= '").append(calendar.getTime()).append("')) ");
        }
        if (filterParameter.getDepartmentId() != null) {
            sql.append(" and ").append(ServerUtils.checkForDeleted("te.isdeleted"));
            sql.append(" and  te.teamId =").append(filterParameter.getDepartmentId());
        }
        if (filterParameter.getLocationId() != null) {
            sql.append(" and el.locationId = ").append(filterParameter.getLocationId());
            sql.append(" and ").append(ServerUtils.checkForDeleted("el.deleted"));
            sql.append(" and (el.startDate is null or el.startDate >= '").append(calendar.getTime()).append("') ");
            sql.append(" and (el.endDate is null or el.endDate < '").append(calendar.getTime()).append("') ");
        }

        if (filterParameter.getSupervisorId() != null) {
            sql.append(" and ep.reportsTo =").append(filterParameter.getSupervisorId());
        }
        if (filterParameter.getObjectId() != null && filterParameter.getObjectId() > 0) {
            sql.append(" and eb.batch_id =").append(filterParameter.getObjectId());
        }

        if (!StringUtil.isEmpty(filterParameter.getSearchKey())) {
            sql.append("      and (lower(mu.firstName) like(:searchKey)" +
                    "          or lower(mu.lastName) like(:searchKey)" +
                    "          or lower(mu.middleName) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))");
        }

        return sql.toString();
    }

}
