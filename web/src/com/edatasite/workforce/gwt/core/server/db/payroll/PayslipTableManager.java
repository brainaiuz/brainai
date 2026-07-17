package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.core.server.rpc.GroupPayrunItems;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 07.03.14
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public interface PayslipTableManager extends Manager<EdsPayslipTable> {
    List<EdsPayslipTable> getTables(ListingFilterParameter filterParameter);

    List<EdsEmployee> getEmployeeDetails(ListingFilterParameter filterParameter);

    List<EdsPaymentDeduction> getCategoriesForTransaction(ListingFilterParameter lfp, String type);

    List<EdsPaymentDeduction> getCategoriesByEmployee(ListingFilterParameter lfp);

    void deleteTableItems(Integer payslipTableID);

    List<String> getPayedMonthList(Integer objectID);

    ArrayList<WpsReportItem> getWpsReportItems(ListingFilterParameter lfp);

    List<GroupPayrunItems> getEmployeeDataForGroupPayrun(ListingFilterParameter lfp);

    List<Integer> getPayslipTableIdsByIds(String IDs);

    List<Integer> getPayslipTableIdsWithLimit(Integer startat, Integer limit);

    List<Integer> getCompanyDeletedPayslipTableListForSolr(SolrReindexRpc solrReindex);

    List<EdsPayslipTable> getPayslipTableListForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    Integer getEmployeeDataForGroupPayrunCount(PayslipFilter filter);

    List<Integer> getEmployeeListDataForGroupPayrun(PayslipFilter filter);

    void updatePayslipTableTotalItems(Integer totalItems, Integer payslipTableID);
}
