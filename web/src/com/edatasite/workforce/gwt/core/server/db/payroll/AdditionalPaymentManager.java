package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPaymentNote;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
public interface AdditionalPaymentManager extends Manager<EdsAdditionalPayment> {

    List<Integer> getAdditionalPaymentIdsByIds(String IDs);

    List<Integer> getAdditionalPaymentIdsWithLimit(Integer startat, Integer limit);

    List<Integer> getCompanyDeletedAdditionalPaymentListForSolr(SolrReindexRpc solrReindex);

    List<EdsAdditionalPayment> getAdditionalPaymentListForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<EdsPaymentDeduction> getAdditionalPaymentItemList(ListingFilterParameter lp);

    List<EdsAdditionalPayment> isExistAdditionalPaymentByCategory(ListingFilterParameter fp);

    List<EdsAdditionalPaymentNote> getAdditionalPaymentNote(Integer ObjectID);

    BigDecimal getAddPaymentMaterialAidTotalPayments(Integer year, Integer employeeId, String systemCode);

    List<EdsAdditionalPayment> getAdditionalPaymentByLeaveRequestId(Integer leaveRequestId);

    BigDecimal getEmployeeAddPaymentByPeriod(Integer employeeId, Integer month, Integer year, String type);

    List<EdsAdditionalPayment> getAdditionalPaymentByBackupsEmployeeId(Integer backupsEmployeeID);

    List<EdsPaymentDeduction> getAdditionalPaymentByBackupsEmployeeId(Integer backupsEmployeeID, Integer month, Integer year);

}
