package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPaymentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollPaymentItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("payrollPaymentItemManager")
public class PayrollPaymentItemManagerImpl extends BaseManager<EdsPayrollPaymentItem> implements PayrollPaymentItemManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayrollPaymentItemManagerImpl() {
        super(EdsPayrollPaymentItem.class);
    }

    @Override
    public List<EdsPayrollPaymentItem> getListByFilter(ListingFilterParameter fp) {
        if (fp == null || fp.getObjectId() == null) {
            return Collections.emptyList();
        }
        String sql = "select pi from EdsPayrollPaymentItem pi " +
                "  join pi.payrollPayment p" +
                "  join pi.employee e" +
                "  left join e.profile ep " +
                "  where (pi.deleted is null or pi.deleted = false)" +
                "      and p.objectID=:paymentId";

        if (fp.getSqlSearchKey() != null) {
            sql += "      and (lower(e.firstName) like(:searchKey)" +
                    "          or lower(e.lastName) like(:searchKey)" +
                    "          or lower(e.email) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))";
        }
        sql += "  order by e.firstName, e.lastName asc ";
        TypedQuery<EdsPayrollPaymentItem> query = this.slaveEntityManager.createQuery(sql, EdsPayrollPaymentItem.class)
                .setParameter("paymentId", fp.getObjectId())
                .setFirstResult(fp.getStart());

        if (fp.getLimit() > 0 && fp.getLimit() <= 500) {
            query = query.setMaxResults(fp.getLimit());
        }
        if (fp.getSqlSearchKey() != null) {
            query = query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        return query.getResultList();
    }

    @Override
    public List<EdsPayrollPaymentItem> getPayrollPaymentItems(Integer singleAddPaymentId) {
        if (singleAddPaymentId == null) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select pi from EdsPayrollPaymentItem pi ");
        sql.append(" join pi.paymentDeduction pd ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("pi.deleted"));
        sql.append(" and pd.objectID = :singleAddPaymentId");

        return slaveEntityManager.createQuery(sql.toString(), EdsPayrollPaymentItem.class)
                .setParameter("singleAddPaymentId", singleAddPaymentId)
                .getResultList();
    }

    @Override
    public BigDecimal getTotalPaymentBySingleAddPaymentId(Integer singleAddPaymentId) {
        if (singleAddPaymentId == null) {
            return BigDecimal.ZERO;
        }
        final String sql = "select coalesce(sum(pi.paymentAmount), 0)" +
                "  from EdsPayrollPaymentItem pi " +
                "  join pi.paymentDeduction pd" +
                "  join pd.status r " +
                "  where " + ServerUtils.checkForDeleted("pi.deleted", "pd.deleted") +
                "      and pd.objectID=?";
        return (BigDecimal) findSingle(sql, singleAddPaymentId);
    }

    @Override
    public BigDecimal getTotalSinglePaymentsByAdditionalPaymentId(Integer additionalPaymentId) {
        if (additionalPaymentId == null) {
            return BigDecimal.ZERO;
        }
        final String sql = "select coalesce(sum(pi.paymentAmount), 0)" +
                "  from EdsPayrollPaymentItem pi " +
                "  join pi.paymentDeduction pd" +
                "  join pd.additionalPayment ad " +
                "  where " + ServerUtils.checkForDeleted("pi.deleted", "pd.deleted") +
                "      and pi.payrollPayment.objectID is null" +
                "      and ad.objectID=?";
        return (BigDecimal) findSingle(sql, additionalPaymentId);
    }

    @Override
    public boolean isLastItemInPayrollPayment(Integer paymentID) {
        return find("select ppi from EdsPayrollPaymentItem ppi where " + ServerUtils.checkForDeleted("ppi.deleted") + " and ppi.payrollPayment.objectID=?", paymentID).size() == 1;
    }
}
