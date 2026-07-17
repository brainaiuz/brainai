package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPayment;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollPaymentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository("payrollPaymentManager")
public class PayrollPaymentManagerImpl extends BaseManager<EdsPayrollPayment> implements PayrollPaymentManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayrollPaymentManagerImpl() {
        super(EdsPayrollPayment.class);
    }

    @Override
    public List<EdsPayrollPayment> getPayrollPayments(Integer additionalPaymentId) {
        if (additionalPaymentId == null) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsPayrollPayment p ");
        sql.append(" join p.additionalPayment ap ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("p.deleted"));
        sql.append(" and ap.objectID = :additionalPaymentId");

        return masterEntityManager.createQuery(sql.toString(), EdsPayrollPayment.class)
                .setParameter("additionalPaymentId", additionalPaymentId)
                .getResultList();
    }

    @Override
    public BigDecimal getTotalPaymentByAdditionalPaymentId(Integer additionalPaymentId) {
        if (additionalPaymentId == null) {
            return BigDecimal.ZERO;
        }
        final String sql = "select coalesce(sum(pi.paymentAmount), 0)" +
                "  from EdsPayrollPaymentItem pi " +
                "  join pi.paymentDeduction pd" +
                "  join pd.additionalPayment ad" +
                "  join pd.status r " +
                "  where " + ServerUtils.checkForDeleted("pi.deleted", "pd.deleted") +
                "      and ad.objectID=?";
        return (BigDecimal) findSingle(sql, additionalPaymentId);
    }
}
