package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsVatEFiling;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.server.db.accounting.VatEFilingManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.08.2010
 * Time: 15:05:55
 * To change this template use File | Settings | File Templates.
 */
@Repository("vatEFilingManager")
public class VatEFilingManagerImpl extends BaseManager<EdsVatEFiling> implements VatEFilingManager {

    public VatEFilingManagerImpl() {
        super(EdsVatEFiling.class);
    }

    public List<EdsVatEFiling> getUnsubmittedVatReturnReports() {
        return find("select ve from EdsVatEFiling ve where ve.status = ?", AccountingConstants.SUBMISSION_PENDING);
    }

    public List<EdsVatEFiling> getCompanyVatReturnReportList() {
        return find("select distinct v from EdsVatEFiling v where v.companyID = ? ", getUser().getCompany().getObjectID());
    }

    public BigDecimal getVatReturnPaymentTotal(Integer vatReturnID) {
        return (BigDecimal)findSingle("select sum(p.amount) from EdsInvoicePayment p where p.vatEFile.objectID = ?", vatReturnID);
    }
}
