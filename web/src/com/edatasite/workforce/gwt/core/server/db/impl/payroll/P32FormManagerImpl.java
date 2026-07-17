package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.P11;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.P32FormManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 28, 2010
 * Time: 4:14:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("p32FormManager")
public class P32FormManagerImpl extends BaseManager<P11> implements P32FormManager {

    public P32FormManagerImpl() {
        super(P11.class);
    }

    public List<Object[]> getP32TotalPaymentRecordObjects(Date fromDate, Date toDate, Integer companyID, Integer employeeID) {
        final Map<String, Object> map = new HashMap<>();
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        String companyId = "\"" + companyID + "\"";
        final StringBuilder sb = new StringBuilder();
        final String month = "date_part('month', p.date)";
        sb.append("SELECT date_part('year', p.date) AS pyear, " +//0
                "   " + month + " AS pmonth, " + //1
                "   SUM(p.tax) AS tax," + //2
                "   COALESCE(SUM(p.studentloandeductions),0) AS studentloandeductions,  " +//3
                "   COALESCE(SUM(p.niTotal),0) AS niTotal,  " +//4
                "   COALESCE(SUM(p.ssp),0) AS ssp,  " +//5 CASE WHEN SUM(ssp)>0 then (SUM(ssp)-SUM(niTotal)*sspRecoveryRate) END AS ssprecovered
                "   COALESCE(SUM(p.smp),0) AS smp, " +//6
                "   COALESCE(SUM(p.spp),0) AS spp,  " +//7
                "   COALESCE(SUM(p.sap),0) AS sap, " +//8
                "   CASE WHEN EXTRACT('day' from p.date) > 5 \n" +
                "       THEN (CASE WHEN " + month + " >= 4 THEN " + month + " - 3 ELSE " + month + " + 9 END) \n" +
                "       ELSE (CASE WHEN " + month + " >= 5 THEN " + month + " - 4 ELSE " + month + " + 8 END)\n" +
                "   END as taxmonth" +
                " FROM " + companyId + ".Payslip p\n" +
                " INNER JOIN " + companyId + ".myuser e ON p.employeeid = e.id \n" +
                " WHERE " + ServerUtils.checkForDeleted("p.deleted") + " AND p.date>=:fromDate AND p.date<:toDate\n" +
                " GROUP BY taxmonth, pyear, pmonth\n" +
                " ORDER BY taxmonth");
        return findNativeByNamedParams(sb.toString(), map);
    }

    public List getP32TotalNIs(Date fromDate, Date toDate, Integer companyID) {
        final Map<String, Object> map = new HashMap<>();
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        String companyId = "\"" + "\"";
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT date_part('year', p.date) AS pyear," +
                "                   date_part('month', p.date) AS pmonth," +
                "                   COALESCE(SUM(p.nitotal),0) AS totalni " +
                "                 FROM " + companyId + ".Payslip p " +
                "                   INNER JOIN "+ companyId + ".myuser e ON p.employeeid = e.id \n" +
                "                 WHERE " + ServerUtils.checkForDeleted("p.deleted") + " AND p.date>=:fromDate AND p.date<:toDate " +
                "                 GROUP BY date_part('year', p.date), date_part('month', p.date) " +
                "                 ORDER BY date_part('year', p.date), date_part('month', p.date)");
        return findNativeByNamedParams(sb.toString(), map);
    }
}
