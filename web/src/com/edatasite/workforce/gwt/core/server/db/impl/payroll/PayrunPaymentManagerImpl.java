package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPayment;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrunPaymentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository("payrunPaymentManager")
public class PayrunPaymentManagerImpl extends BaseManager<EdsPayrunPayment> implements PayrunPaymentManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayrunPaymentManagerImpl() {
        super(EdsPayrunPayment.class);
    }

    @Override
    public List<EdsPayrunPayment> getPayrunPayments(Integer payslipTableId) {
        if (payslipTableId == null) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsPayrunPayment p ");
        sql.append(" join p.payslipTable t ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("p.deleted"));
        sql.append(" and t.objectID = :payslipTableId");

        return slaveEntityManager.createQuery(sql.toString(), EdsPayrunPayment.class)
                .setParameter("payslipTableId", payslipTableId)
                .getResultList();
    }

    @Override
    public BigDecimal getTotalPaymentByPayrunId(Integer payslipTableId) {
        if (payslipTableId == null) {
            return BigDecimal.ZERO;
        }
        final String sql = "select coalesce(sum(pi.paymentAmount), 0)" +
                "  from EdsPayrunPaymentItem pi " +
                "  join pi.payslipTableItem pti" +
                "  join pti.payslipTable pt" +
                "  join pti.status r " +
                "  where " + ServerUtils.checkForDeleted("pi.deleted", "pti.deleted") +
                "      and pt.objectID=?";
        return (BigDecimal) findSingle(sql, payslipTableId);
    }
}
