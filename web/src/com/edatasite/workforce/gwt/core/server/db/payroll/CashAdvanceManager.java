package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.08.14
 * Time: 0:33
 * To change this template use File | Settings | File Templates.
 */
public interface CashAdvanceManager extends Manager<EdsCashAdvance> {


    List<EdsCashAdvance> getCashAdvancedList(ListingFilterParameter lfp);

    Integer getCashAdvanceCount();

    List<Integer> getCashAdvanceIdsByIds(String IDs);

    List<EdsCashAdvance> getCashAdvanceByIds(List<Integer> IDs);

    List<Integer> getCashAdvanceIdsWithLimit(Integer startat, Integer limit);

    List<Integer> getCompanyDeletedCashAdvanceListForSolr(SolrReindexRpc solrReindex);

    List<EdsCashAdvance> getCashAdvanceListForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<CashAdvanceReportItem> getCashAdvanceReportItems(ListingFilterParameter lfp);

    BigDecimal getCashAdvanceAppliedAmount(ListingFilterParameter lfp);

    BigDecimal getCashAdvanceRemainingAmount(Integer objectId);

    List<EdsCashAdvance> getCashAdvanceListByEmployeeId(Integer employeeId);

    Integer getCashAdvanceIntNumber();

    boolean numberExists(String numberString, Integer objectId);

    boolean isCashAdvanceUsedInPayslip(Integer objectId);

    Integer getCashAdvanceReportItemsCount(ListingFilterParameter lfp);

    List<EdsCashAdvance> getListByMultiCashAdvance(Integer objectID);
}
