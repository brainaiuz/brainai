package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPaymentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrunPaymentItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("payrunPaymentItemManager")
public class PayrunPaymentItemManagerImpl extends BaseManager<EdsPayrunPaymentItem> implements PayrunPaymentItemManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayrunPaymentItemManagerImpl() {
        super(EdsPayrunPaymentItem.class);
    }

    @Override
    public List<EdsPayrunPaymentItem> getListByFilter(ListingFilterParameter fp) {
        if (fp == null || fp.getObjectId() == null) {
            return Collections.emptyList();
        }
        String sql = "select pi from EdsPayrunPaymentItem pi " +
                "  join pi.payrunPayment p" +
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
        TypedQuery<EdsPayrunPaymentItem> query = this.slaveEntityManager.createQuery(sql, EdsPayrunPaymentItem.class)
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
    public List<EdsPayrunPaymentItem> getPayrunPaymentItems(Integer singlePayrunId) {
        if (singlePayrunId == null) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select pi from EdsPayrunPaymentItem pi ");
        sql.append(" join pi.payslipTableItem ti ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("pi.deleted"));
        sql.append(" and ti.objectID = :singlePayrunId");

        return slaveEntityManager.createQuery(sql.toString(), EdsPayrunPaymentItem.class)
                .setParameter("singlePayrunId", singlePayrunId)
                .getResultList();
    }

    @Override
    public BigDecimal getTotalPaymentBySinglePayrunId(Integer singlePayrunId) {
        if (singlePayrunId == null) {
            return BigDecimal.ZERO;
        }
        final String sql = "select coalesce(sum(pi.paymentAmount), 0)" +
                "  from EdsPayrunPaymentItem pi " +
                "  join pi.payslipTableItem ti" +
                "  join ti.status r " +
                "  where " + ServerUtils.checkForDeleted("pi.deleted", "ti.deleted") +
                "      and ti.objectID=?";
        return (BigDecimal) findSingle(sql, singlePayrunId);
    }

    @Override
    public BigDecimal getTotalSinglePaymentsByGroupPayrunId(Integer groupPayrunId) {
        if (groupPayrunId == null) {
            return BigDecimal.ZERO;
        }
        final String sql = "select coalesce(sum(pi.paymentAmount), 0)" +
                "  from EdsPayrunPaymentItem pi " +
                "  join pi.payslipTableItem ti" +
                "  join ti.payslipTable pt " +
                "  where " + ServerUtils.checkForDeleted("pi.deleted", "ti.deleted") +
                "      and pi.payrunPayment.objectID is null" +
                "      and pt.objectID=?";
        return (BigDecimal) findSingle(sql, groupPayrunId);
    }

    @Override
    public boolean isLastItemInPayrunPayment(Integer paymentID) {
        return find("select ppi from EdsPayrunPaymentItem ppi where " + ServerUtils.checkForDeleted("ppi.deleted") + " and ppi.payrunPayment.objectID=?", paymentID).size() == 1;
    }
}
