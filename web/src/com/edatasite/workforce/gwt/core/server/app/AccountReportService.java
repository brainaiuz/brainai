package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.accounting.TotalDebitCredit;
import com.edatasite.workforce.gwt.accounting.client.rpc.BalanceSheetInnerItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetManagerItems;
import com.edatasite.workforce.gwt.accounting.client.rpc.PnLFilter;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceItem;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: dilsh0d  Date: 11/12/12
 */
public interface AccountReportService {

    void getTrialBalanceAllSubsidiariesReport(Date fromDate, Date toDate, LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList, Map<String, TrialBalanceItem> mergeAccountTypeMap, Map<String, BigDecimal> trailBalanceTotalMap, Integer projectID, Integer currencyId, Integer showAccounts, Boolean summary);

    void getTrialBalanceReport(Date fromDate, Date toDate, LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList, HashMap<String, BigDecimal> trailBalanceTotalMap, Integer showAccounts, String departmentAndTreeChildIDs, Integer projectID, Integer currencyId, Boolean summary);

    void getBalanceSheetAllSubsidiariesReport(Date fromDate, Date toDate, Map<String, List<BalanceSheetInnerItem>> balanceSheetMap, Map<String, BigDecimal> balanceSheetTotalMap, Integer baseCurrencyID, Map<String, BalanceSheetInnerItem> mergeBalanceSheetMap, String departmentAndTreeChildIDs, Integer projectID);

    void getBalanceSheetAllSubsidiariesReportSummary(Date fromDate, Date toDate, Map<String, BigDecimal> balanceSheetAssetTotalMap, Map<String, BigDecimal> balanceSheetLiabilityTotalMap, Integer baseCurrencyID, Map<String, BalanceSheetInnerItem> mergeBalanceSheetMap, String departmentAndTreeChildIDs, Integer projectID);

    void getBalanceSheetReport(Date fromDate, Date toDate, Map<String, List<BalanceSheetInnerItem>> balanceSheetMap, Map<String, BigDecimal> balanceSheetTotalMap, BigDecimal exchangeRate, String departmentAndTreeChildIDs, Integer currencyId, Integer projectID);

    void getBalanceSheetReportSummary(Date fromDate, Date toDate, Map<String, BigDecimal> balanceSheetAssetTotalMap, Map<String, BigDecimal> balanceSheetLiabilityTotalMap, BigDecimal exchangeRate, String departmentAndTreeChildIDs, Integer currencyId, Integer projectID);

    BudgetManagerItems getProfitAndLossReport(PnLFilter filter);

    BudgetManagerItems getProfitAndLossSubsidiariesReport(PnLFilter filter);

    Date getBeginningOfAccountingPeriod(Date financialEndDate, Date date);

    Date getEndingOfAccountingPeriod(Date financialEndDate, Date date);

    BigDecimal calculateGainLossBalance(TotalDebitCredit glTotalPeriod, TotalDebitCredit debitCredit, Integer currencyId, Integer baseCurrencyId, BigDecimal exchangeRate, Integer exchangeRateScale);

    TotalDebitCredit calculateBankAccountsGainLoss(Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID, Integer currencyId, Integer exchangeRateScale);
}
