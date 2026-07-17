package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.P11;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 28, 2010
 * Time: 3:04:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface P32FormManager extends Manager<P11> {

    List<Object[]> getP32TotalPaymentRecordObjects(Date fromDate, Date toDate, Integer companyID, Integer employeeID);

    List getP32TotalNIs(Date fromDate, Date toDate, Integer companyID);
}
