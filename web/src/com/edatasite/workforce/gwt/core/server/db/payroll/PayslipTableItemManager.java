package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollAmountsTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionContributionData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 07.03.14
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public interface PayslipTableItemManager extends Manager<EdsPayslipTableItem> {

    List<EdsPayslipTableItem> getPayslipTableItemsByTableID(Integer objectID);

    List<EdsPayslipTableItem> getPayslipTableItemsByTableID(Integer objectID, boolean onlyApprovedItems);

    List<EdsPaymentDeduction> getItemCategories(Integer payslipItemID);

    List<EdsPaymentDeduction> getItemCategories(Integer payslipItemId, Boolean forwarded);

    List<EdsPaymentDeduction> getItemCategoriesByType(Integer payslipItemID, String type);

    BigDecimal getPayslipItemPaymentsTotal(Integer payslipItemID, String type);

    ArrayList<PensionContributionData> getPensionContributions(Integer month, Integer year);

    List<EdsPayslipTableItem> getPayslipsByFilter(ListingFilterParameter filterParameter);

    Integer getPayslipsCount();

    ArrayList<String> getPayedMonthList(Integer objectID, Integer employeeID);

    List<Integer> getPayslipTableItemIdsByIds(String IDs);

    List<Integer> getPayslipTableItemIdsWithLimit(Integer startat, Integer limit);

    boolean isLastItemInGroupPayrun(Integer objectID);

    BigDecimal getTotalPayToDate(Integer employeeID, Date date, Integer year);

    List<Integer> getCompanyDeletedPayslipTableItemListForSolr(SolrReindexRpc solrReindex);

    List<EdsPayslipTableItem> getPayslipTableItemListForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<EdsPayslipTableItem> getPayslipTableItemList(ListingFilterParameter filterParameter);

    Integer getPayslipTableItemListTotal(ListingFilterParameter filterParameter);

    List<SalaryReportItem> getSalaryReportItems(ListingFilterParameter lfp);

    Map<Integer, BigDecimal> getRecurringCategoriesTotalByItems(Integer payslipTableId);

    HashMap<SelectItem, SelectItem[]> getYearMonthsForWps();

    List<Object[]> getPayslipApiList(ListingFilterParameter filterParameter);

    PayrollTotalTO getTotalAmountGroupId(Integer groupPayrunId);

    PayrollAmountsTO getTotalsByGroupId(Integer groupPayrunId);

    Integer getCountByFilter(ListingFilterParameter fp);

    List<EdsPayslipTableItem> getListByFilter(ListingFilterParameter fp);

    EdsPayslipTableItem getEmployeePayslipTable(Integer employeeID, int month, int year);

    ArrayList<Integer> getPendingItems(Integer payslipTableId);

    SalaryDetailedReportData getSalaryDetailedReportItems(ListingFilterParameter lfp);

    List<EdsPaymentDeduction> getItemCategoriesByCategoryID(Integer payslipItemID, Integer categoryId);

    BigDecimal getEmployeeAllowanceByPeriod(Integer employeeID, int month, int year, String type);

    SinglePayrunItem getSinglePayrunTO(Integer id);
}
