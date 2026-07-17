package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsVatEFiling;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.08.2010
 * Time: 18:07:30
 * To change this template use File | Settings | File Templates.
 */
public interface VatEFilingManager extends Manager<EdsVatEFiling> {
    List<EdsVatEFiling> getUnsubmittedVatReturnReports();

    List<EdsVatEFiling> getCompanyVatReturnReportList();

    BigDecimal getVatReturnPaymentTotal(Integer vatReturnID);
}
