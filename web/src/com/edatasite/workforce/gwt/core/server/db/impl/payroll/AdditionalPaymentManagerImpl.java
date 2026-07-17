package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPaymentNote;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.LR_TYPE_ANNUAL_LEAVE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.LR_TYPE_SICK_LEAVE;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
@Repository("additionalPaymentManager")
public class AdditionalPaymentManagerImpl extends BaseManager<EdsAdditionalPayment> implements AdditionalPaymentManager{

    public AdditionalPaymentManagerImpl() {
        super(EdsAdditionalPayment.class);
    }

    @Override
    public List<Integer> getAdditionalPaymentIdsByIds(String IDs) {
        return find("select ap.objectID from EdsAdditionalPayment ap where ap.objectID in (" + IDs + ") and " + ServerUtils.checkForDeleted("ap.deleted"));
    }

    @Override
    public List<Integer> getAdditionalPaymentIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select ap.objectID from EdsAdditionalPayment ap where ap.objectID > ? and " + ServerUtils.checkForDeleted("ap.deleted") + " order by ap.objectID", limit, startat);
    }

    @Override
    public List<Integer> getCompanyDeletedAdditionalPaymentListForSolr(SolrReindexRpc solrReindex) {
        return find("select ap.objectID from EdsAdditionalPayment ap where ap.deleted=true and ap.lastUpdateTime>='" + solrReindex.getLastUpdateTime() + "'"
                + (solrReindex.getLastUpdateEndTime() != null ? " and ap.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""));
    }

    @Override
    public List<EdsAdditionalPayment> getAdditionalPaymentListForSolr(SolrReindexRpc solrReindex, int startat, int limit) {

        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select ap from EdsAdditionalPayment ap where (ap.deleted is null or ap.deleted is false) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and ap.lastUpdateTime >= :updatedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and ap.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by ap.objectID ASC ");
        return findIntervalByNamedParams(sqlQuery.toString(), startat, limit, params);
    }

    public List<EdsPaymentDeduction> getAdditionalPaymentItemList(ListingFilterParameter lp) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select pd from EdsPaymentDeduction pd ");
        sqlQuery.append(" where " + ServerUtils.checkForDeleted("pd.deleted") + " and pd.additionalPayment is not null ");
        sqlQuery.append(" and " + ServerUtils.checkForDeleted("pd.additionalPayment.deleted"));
        if (lp.getObjectId() != null) {
            sqlQuery.append(" and pd.additionalPayment.objectID = " + lp.getObjectId());
        }
        if (lp.getEmployeeId() != null && !getUser().hasRole(Constants.ADMIN_CODE)) {
            sqlQuery.append(" and pd.employee.objectID = " + lp.getEmployeeId());
        }
        if (lp.getDepartmentId() != null) {
            sqlQuery.append(" and pd.employee.employeeDepartment.department.objectID = " + lp.getDepartmentId());
        }
        if (lp.getSearchKey() != null && !"".equals(lp.getSearchKey())) {
            sqlQuery.append(" and (lower(pd.additionalPayment.reference) like '%" + lp.getSearchKey().toLowerCase() + "%' or lower(concat( pd.employee.firstName,' ', pd.employee.lastName)) like '%" + lp.getSearchKey().toLowerCase() + "%')");
        }
        sqlQuery.append(" order by pd.objectID desc");
        return find(sqlQuery.toString());
    }

    public List<EdsAdditionalPayment> isExistAdditionalPaymentByCategory(ListingFilterParameter fp) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select ap from EdsAdditionalPayment ap ");
        if (fp.getValueMap().get("EMPLOYEE_IDS") != null && !fp.getValueMap().get("EMPLOYEE_IDS").isEmpty())
            sqlQuery.append(" where ap.employeeIds = '" + fp.getValueMap().get("EMPLOYEE_IDS") + "' ");
        else if (fp.getValueMap().get("DEPARTMENT_ID") != null)
            sqlQuery.append(" where ap.department = " + fp.getValueMap().get("DEPARTMENT_ID") + " ");
        else if (fp.getValueMap().get("LOCATION") != null)
            sqlQuery.append(" where ap.location = " + fp.getValueMap().get("LOCATION") + " ");
        else if (fp.getValueMap().get("PAYROLL_BATCH_ID") != null)
            sqlQuery.append(" where ap.payrollBatch = " + fp.getValueMap().get("PAYROLL_BATCH_ID") + " ");
        else if (fp.getValueMap().get("SUPERVISOR") != null)
            sqlQuery.append(" where ap.supervisor = " + fp.getValueMap().get("SUPERVISOR") + " ");
        sqlQuery.append(" and ap.monthID = " + fp.getMonthId() + " and ap.year = " + fp.getYear() + " and (ap.deleted = 'false')");
        return find(sqlQuery.toString());
    }

    @Override
    public List<EdsAdditionalPaymentNote> getAdditionalPaymentNote(Integer id) {
        return find("select adp from EdsAdditionalPaymentNote adp where adp.id=? order by adp.date desc", id);
    }

    public BigDecimal getAddPaymentMaterialAidTotalPayments(Integer year, Integer employeeId, String systemCode) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select coalesce(sum(pd.paymentamount),0)  from ");
        sqlQuery.append(getCompanyId()).append(".PaymentDeduction pd ");
        sqlQuery.append(" left join ").append(getCompanyId()).append(".additionalPayment psi on psi.id = pd.addpayment_id");
        sqlQuery.append(" left join ").append(getCompanyId()).append(".Category c on pd.categoryID=c.id ");
        sqlQuery.append(" left join ").append(getCompanyId()).append(".reference ref on psi.overallstatus = ref.id");
        sqlQuery.append(" where (ref.code='").append(Constants.PAYMENT_STATUS_APPROVED).append("' or ref.code='").append(Constants.PAYMENT_STATUS_PARTIAL_PAID).append("' or ref.code='").append(Constants.PAYMENT_STATUS_PAID).append("') ");
        sqlQuery.append(" and " + ServerUtils.checkForDeleted("pd.deleted") + " and pd.addpayment_id is not null ");
        sqlQuery.append(" and " + ServerUtils.checkForDeleted("psi.deleted"));
        sqlQuery.append(" and psi.showInPaySlip is false ");
        sqlQuery.append(" and pd.employeeId = :employeeId ");
        sqlQuery.append(" and psi.year = :year ");
        sqlQuery.append(" and c.type = :type ");
        if (systemCode != null) {
            sqlQuery.append("AND c.system_code = :systemCode ");
        }

        final HashMap<String, Object> params = new HashMap<>();
        params.put("type", EdsPayrollCategory.MATERIAL_AID);
        params.put("employeeId", employeeId);
        params.put("year", year);
        if (systemCode != null) {
            params.put("systemCode", systemCode);
        }
        return (BigDecimal) findNativeSingleByNamedParams(sqlQuery.toString(), params);
    }

    @Override
    public List<EdsAdditionalPayment> getAdditionalPaymentByLeaveRequestId(Integer leaveRequestId) {
        return find("select adp from EdsAdditionalPayment adp where adp.deleted is not true and adp.leaveRequestId=? ", leaveRequestId);
    }

    @Override
    public BigDecimal getEmployeeAddPaymentByPeriod(Integer employeeId, Integer month, Integer year, String type) {
        StringBuilder sql = new StringBuilder(" select coalesce(sum(pd.paymentamount),0) - coalesce(sum(pd.basicSalaryPartAmount),0) from ");
        sql.append(getCompanyId()).append(".PaymentDeduction pd ");
        sql.append(" left join ").append(getCompanyId()).append(".additionalPayment psi on psi.id = pd.addpayment_id");
        sql.append(" left join ").append(getCompanyId()).append(".category c on c.id = pd.categoryID");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".reference ref on psi.overallstatus = ref.id");
        sql.append(" where (ref.code='").append(Constants.PAYMENT_STATUS_APPROVED).append("' or ref.code='").append(Constants.PAYMENT_STATUS_PARTIAL_PAID).append("' or ref.code='").append(Constants.PAYMENT_STATUS_PAID).append("') ");
        sql.append(" and (psi.deleted is null or psi.deleted != true) ");
        sql.append(" and psi.showInPaySlip is false");
        sql.append(" and pd.employeeid =").append(employeeId);
        sql.append(" and ( psi.year >=").append(year - 1).append(" and psi.year <").append(year).append(" and psi.monthID >= ").append(month);
        sql.append("      or psi.year <=").append(year).append(" and psi.year > ").append(year - 1).append(" and psi.monthID < ").append(month).append(")");
        sql.append(" and pd.addpayment_id is not null and pd.deleted is not true ");
        if (type != null) {
            if (LR_TYPE_SICK_LEAVE.equals(type)) {
                sql.append(" and c.excludeSickLeave is not true");
            } else if (LR_TYPE_ANNUAL_LEAVE.equals(type)) {
                sql.append(" and c.excludeAnnualLeave is not true");
            }
        }

        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsAdditionalPayment> getAdditionalPaymentByBackupsEmployeeId(Integer backupsEmployeeID) {
        return find("select adp from EdsAdditionalPayment adp where adp.deleted is not true and adp.backupsEmployeeId=? ", backupsEmployeeID);
    }

    @Override
    public List<EdsPaymentDeduction> getAdditionalPaymentByBackupsEmployeeId(Integer backupsEmployeeID, Integer monthId, Integer year) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.*");
        sql.append(" from ").append(getCompanyId()).append(".PaymentDeduction p ");
        sql.append(" left join ").append(getCompanyId()).append(".additionalPayment ad on p.addpayment_id = ad.id ");
        sql.append(" left join ").append(getCompanyId()).append(".reference ref on ad.overallstatus = ref.id");
        sql.append(" where ").append(ServerUtils.checkForDeleted("ad.deleted"));
        sql.append(" and ad.showInPaySlip is false");
        sql.append(" and (ref.code='").append(Constants.PAYMENT_STATUS_APPROVED).append("' or ref.code='").append(Constants.PAYMENT_STATUS_PARTIAL_PAID).append("' or ref.code='").append(Constants.PAYMENT_STATUS_PAID).append("') ");
        sql.append(" and ad.backupsEmployeeId = ").append(backupsEmployeeID);
        if (monthId != null && year != null) {
            sql.append(" and ad.monthid = " + monthId);
            sql.append(" and ad.year = " + year);
        }
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

}
