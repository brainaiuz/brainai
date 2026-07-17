package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExchangeRateManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ALL_ACCOUNTS;

/**
 * User: dilsh0d
 * Date: 11/12/12
 * Time: 16:14
 */
@Service("accountReportService")
public class AccountReportServiceImpl implements AccountReportService {

    private static final Logger log = LoggerFactory.getLogger(AccountReportServiceImpl.class);

    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ExchangeRateManager exchangeRateManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private AccountBudgetManager accountBudgetManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("accountingLocalizer")
    private WfmMessageSource accountingLocalizer;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getTrialBalanceAllSubsidiariesReport(Date fromDate, Date toDate, LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList, Map<String, TrialBalanceItem> mergeAccountTypeMap, Map<String, BigDecimal> trailBalanceTotalMap, Integer projectID, Integer currencyId, Integer showAccounts, Boolean summary) {
        EdsCompany edsCompany = companyManager.get(SecurityContext.getCompanyID());
        if (!edsCompany.isDeleted() && edsCompany.getAccountingSetup()) {
            log.info("CONSOLIDATION_COMPANY_ID: " + edsCompany.getObjectID());

            List<EdsAccount> accounts = accountingManager.getAccountsAttendedInTransactions(toDate, null, projectID, showAccounts, false);
            LinkedHashSet<EdsAccount> accountsHash = new LinkedHashSet<>(accounts);

            EdsAccount gainAndLossAccount = accountingManager.getAccountByKey(EdsAccount.EXCHANGE_VARIANCE);
            accountsHash.add(gainAndLossAccount);

            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            Integer companyBaseCurrencyID = financialSettings.getCurrency().getObjectID();

            Map<Integer, TotalDebitCredit> beginnetBalanceMap;
            Map<Integer, TotalDebitCredit> debitCreditMap;
            if (showAccounts != null && showAccounts.equals(ALL_ACCOUNTS)) { // number 3 means all accounts
                beginnetBalanceMap = accountingManager.getSubsidiariesAllAccountsDebitCredit(fromDate, null, companyBaseCurrencyID);
                debitCreditMap = accountingManager.getSubsidiariesAllAccountsDebitCredit(fromDate, toDate, companyBaseCurrencyID);
            } else {
                beginnetBalanceMap = accountingManager.getSubsidiariesDebitCreditTotal(fromDate, null, companyBaseCurrencyID);
                debitCreditMap = accountingManager.getSubsidiariesDebitCreditTotal(fromDate, toDate, companyBaseCurrencyID);
            }
            TotalDebitCredit reTotalDebitCredit = new TotalDebitCredit();//debit credit of Retained Earnings

            for (EdsAccount account : accounts) {
                EdsAccount parent = summary ? getAccountParent(account, account.getParent()) : account;
                log.info("CONSOLIDATION_ACCOUNT: " + parent.getObjectID() + " | " + parent.getName());
                TrialBalanceItem tbi = new TrialBalanceItem(parent.getObjectID(), parent.getName());

                boolean isMultiAccount = false;
                String key = parent.getName().trim().toLowerCase() + "{@}" + parent.getAccountType().getObjectID();
                if (parent.getAccountType() != null && mergeAccountTypeMap.get(key) != null) {
                    isMultiAccount = true;
                    tbi = mergeAccountTypeMap.get(key);
                    log.info("CONSOLIDATION_GETTING_ITEM_TO_EDIT");
                } else {
                    mergeAccountTypeMap.put(key, tbi);
                    log.info("CONSOLIDATION_PUTTING_ITEM_TO_EDIT");
                }
                tbi.setCode(parent.getAccountCode());

                TrialBalanceItem tbiReceivablePayable = null;
                if (account.getKey() != null && (account.getKey().equals(EdsAccount.ACCOUNTS_RECEIVABLE) || account.getKey().equals(EdsAccount.ACCOUNTS_PAYABLE))) {
                    String interCompanyAccountKey = (account.getKey().equals(EdsAccount.ACCOUNTS_RECEIVABLE) ?
                            AccountingConstants.ACCOUNTS_RECEIVABLE_INTERCOMPANY : AccountingConstants.ACCOUNTS_PAYABLE_INTERCOMPANY);

                    if (mergeAccountTypeMap.get(interCompanyAccountKey) != null) {
                        tbiReceivablePayable = mergeAccountTypeMap.get(interCompanyAccountKey);
                    } else {
                        tbiReceivablePayable = new TrialBalanceItem(parent.getObjectID(), parent.getKey().equals(EdsAccount.ACCOUNTS_RECEIVABLE) ? "Accounts Receivable Intercompany" : "Accounts Payable Intercompany");
                        mergeAccountTypeMap.put(interCompanyAccountKey, tbiReceivablePayable);
                        mapAccountTypeByList.get(parent.getAccountType().getCategory()).add(tbiReceivablePayable);
                    }
                    tbiReceivablePayable.setCode(parent.getAccountCode());
                }

                ////////Debit/Credit for selected period
                TotalDebitCredit dcTotalPeriod = debitCreditMap.get(account.getObjectID()) != null ? debitCreditMap.get(account.getObjectID()) : new TotalDebitCredit();
                ///////Beginning Balance as of start date
                TotalDebitCredit dcTotalBeginningBalance = beginnetBalanceMap.get(account.getObjectID()) != null ? beginnetBalanceMap.get(account.getObjectID()) : new TotalDebitCredit();

                if (account instanceof EdsFloatingAccount) {
                    TotalDebitCredit pnlTotalPeriod = calculateBankAccountsGainLoss(fromDate, toDate, null, projectID, currencyId, financialSettings.getExchangeRateScale());
                    dcTotalPeriod = dcTotalPeriod.add(pnlTotalPeriod);

                    byte sidePeriod = dcTotalPeriod.dcCompare();
                    calculateFloatingAccount(mapAccountTypeByList, account, tbi, sidePeriod);
                }
                if (Constants.REVENUE.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory())) {
                    calculateRetainedEarningsAccount(reTotalDebitCredit, dcTotalBeginningBalance);
                }

                BigDecimal dcPeriod = dcTotalPeriod.getRealDebitCreditDiff();
                log.info("CONSOLIDATION_DCPERIOD:" + dcPeriod.toString());

                BigDecimal dcBeginningBalance = dcTotalBeginningBalance.getRealDebitCreditDiff();
                log.info("CONSOLIDATION_DCBEGINNING:" + dcBeginningBalance.toString());

                if (!(Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory()))) {
                    dcBeginningBalance = dcBeginningBalance.multiply(new BigDecimal(-1));
                }

                if (isMultiAccount && tbi.getBeginningBalance() != null) {
                    tbi.setBeginningBalance(tbi.getBeginningBalance().add(dcBeginningBalance));
                } else {
                    tbi.setBeginningBalance(dcBeginningBalance);
                }

                if (Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory())) {
                    trailBalanceTotalMap.put("totalBeginningBalance", trailBalanceTotalMap.get("totalBeginningBalance").subtract(dcBeginningBalance));
                } else {
                    trailBalanceTotalMap.put("totalBeginningBalance", trailBalanceTotalMap.get("totalBeginningBalance").add(dcBeginningBalance));
                }

                if (isMultiAccount && tbi.getDebit() != null) {
                    tbi.setDebit(tbi.getDebit().add(dcTotalPeriod.debit));
                } else {
                    tbi.setDebit(dcTotalPeriod.debit);
                }
                trailBalanceTotalMap.put("totalDebit", trailBalanceTotalMap.get("totalDebit").add(dcTotalPeriod.debit));
                if (isMultiAccount && tbi.getCredit() != null) {
                    tbi.setCredit(tbi.getCredit().add(dcTotalPeriod.credit));
                } else {
                    tbi.setCredit(dcTotalPeriod.credit);
                }
                trailBalanceTotalMap.put("totalCredit", trailBalanceTotalMap.get("totalCredit").add(dcTotalPeriod.credit));

                BigDecimal debCred = (tbi.getDebit() != null ? tbi.getDebit() : BigDecimal.ZERO).subtract(tbi.getCredit() != null ? tbi.getCredit() : BigDecimal.ZERO);
                if (!(Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory()))) {
                    debCred = debCred.multiply(new BigDecimal(-1));
                    dcPeriod = dcPeriod.multiply(new BigDecimal(-1));
                }

                tbi.setEndingBalance(tbi.getBeginningBalance().add(debCred));
                if (Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory())) {
                    trailBalanceTotalMap.put("totalEndingBalance", trailBalanceTotalMap.get("totalEndingBalance").subtract(dcBeginningBalance.add(dcPeriod)));
                } else {
                    trailBalanceTotalMap.put("totalEndingBalance", trailBalanceTotalMap.get("totalEndingBalance").add(dcBeginningBalance.add(dcPeriod)));
                }

                if (tbiReceivablePayable != null) {
                    ///////InterCompany Beginning Balance as of start date
                    BigDecimal dcBeginningBalanceInterCompany = dcTotalBeginningBalance.getInterCompanyRealDebitCreditDiff();
                    tbiReceivablePayable.setBeginningBalance((tbiReceivablePayable.getBeginningBalance() != null ? tbiReceivablePayable.getBeginningBalance() : BigDecimal.ZERO).add(dcBeginningBalanceInterCompany));

                    if (!(Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory()))) {
                        dcBeginningBalanceInterCompany = dcBeginningBalanceInterCompany.multiply(new BigDecimal(-1));
                    }

                    if (Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory())) {
                        trailBalanceTotalMap.put("totalBeginningBalance", trailBalanceTotalMap.get("totalBeginningBalance").subtract(dcBeginningBalanceInterCompany));
                    } else {
                        trailBalanceTotalMap.put("totalBeginningBalance", trailBalanceTotalMap.get("totalBeginningBalance").add(dcBeginningBalanceInterCompany));
                    }

                    ////////InterCompany Debit/Credit for selected period
                    BigDecimal dcPeriodInterCompany = dcTotalPeriod.getInterCompanyDebitCreditDiff();

                    tbiReceivablePayable.setDebit((tbiReceivablePayable.getDebit() != null ? tbiReceivablePayable.getDebit() : BigDecimal.ZERO).add(dcTotalPeriod.interCompanyDebit));
                    trailBalanceTotalMap.put("totalDebit", trailBalanceTotalMap.get("totalDebit").add(dcTotalPeriod.interCompanyDebit));

                    tbiReceivablePayable.setCredit((tbiReceivablePayable.getCredit() != null ? tbiReceivablePayable.getCredit() : BigDecimal.ZERO).add(dcTotalPeriod.interCompanyCredit));
                    trailBalanceTotalMap.put("totalCredit", trailBalanceTotalMap.get("totalCredit").add(dcTotalPeriod.interCompanyCredit));

                    BigDecimal debCredInterCompany = (tbiReceivablePayable.getDebit() != null ? tbiReceivablePayable.getDebit() : BigDecimal.ZERO).subtract(tbiReceivablePayable.getCredit() != null ? tbiReceivablePayable.getCredit() : BigDecimal.ZERO);
                    if (!(Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory()))) {
                        debCredInterCompany = debCredInterCompany.multiply(new BigDecimal(-1));
                    }
                    tbiReceivablePayable.setEndingBalance(tbiReceivablePayable.getBeginningBalance().add(debCredInterCompany));
                    if (Constants.ASSETS.equals(account.getAccountType().getCategory()) || Constants.EXPENSES.equals(account.getAccountType().getCategory())) {
                        trailBalanceTotalMap.put("totalEndingBalance", trailBalanceTotalMap.get("totalEndingBalance").subtract(dcBeginningBalanceInterCompany.add(dcPeriodInterCompany)));
                    } else {
                        trailBalanceTotalMap.put("totalEndingBalance", trailBalanceTotalMap.get("totalEndingBalance").add(dcBeginningBalanceInterCompany.add(dcPeriodInterCompany)));
                    }
                }

                if (!isMultiAccount && !(account instanceof EdsFloatingAccount)) {
                    mapAccountTypeByList.get(parent.getAccountType().getCategory()).add(tbi);
                }
            }
            calculateRetainedEarningsForTrialBalance(reTotalDebitCredit, mapAccountTypeByList, trailBalanceTotalMap);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getTrialBalanceReport(Date fromDate, Date toDate, LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList, HashMap<String, BigDecimal> trailBalanceTotalMap, Integer showAccounts, String departmentAndTreeChildIDs, Integer projectID, Integer currencyId, Boolean summary) {
        HashMap<Integer, TotalDebitCredit> beginningBalanceMap;
        HashMap<Integer, TotalDebitCredit> foreignBeginningBalanceMap;
        HashMap<Integer, TotalDebitCredit> debitCreditMap;
        HashMap<Integer, TotalDebitCredit> foreignDebitCreditMap;

        HashMap<Integer, TrialBalanceItem> tempAccountByTypeMap = new HashMap<>();

        LinkedHashMap<String, TrialBalanceItem> tempAccountByTypeMapSummary = new LinkedHashMap<>();
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.BANK, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.CURRENT_ASSET, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.NON_CURRENT_ASSET, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.PREPAYMENT, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.FIXED_ASSET, null);

        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.LIABILITY, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.CURRENT_LIABILITY, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.LONG_TERM_LIABILITY, null);

        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.EQUITY, null);

        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.SALES, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.REVENUE, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.OTHER_INCOME, null);

        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.COST_OF_SALES, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.DIRECT_EXPENSES, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.OVERHEAD, null);
        tempAccountByTypeMapSummary.putIfAbsent(EdsAccountType.DEPRECIATION, null);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer baseCurrencyId = financialSettings.getCurrency().getObjectID();
        Integer exchangeRateScale = financialSettings.getExchangeRateScale();

        foreignBeginningBalanceMap = accountingManager.getForeignAccountsDebitCreditTotal(departmentAndTreeChildIDs, projectID, null, fromDate, null);
        foreignDebitCreditMap = accountingManager.getForeignAccountsDebitCreditTotal(departmentAndTreeChildIDs, projectID, fromDate, toDate, null);
        beginningBalanceMap = accountingManager.getAllAccountsDebitCredit(departmentAndTreeChildIDs, null, fromDate);
        debitCreditMap = accountingManager.getAllAccountsDebitCredit(departmentAndTreeChildIDs, fromDate, toDate);

        ArrayList<EdsAccount> accounts = (ArrayList<EdsAccount>) accountingManager.getAccountsAttendedInTransactions(toDate, departmentAndTreeChildIDs, projectID, showAccounts, false);
        LinkedHashSet<EdsAccount> accountsHash = new LinkedHashSet<>(accounts);

        EdsAccount gainAndLossAccount = accountingManager.getAccountByKey(EdsAccount.EXCHANGE_VARIANCE);
        if (gainAndLossAccount != null) {
            accountsHash.add(gainAndLossAccount);
        }

        BigDecimal exchangeRate = BigDecimal.ONE;
        BigDecimal prevExchangeRate = BigDecimal.ONE;

        if (currencyId != null) {
            CurrencyListItem item = currencyService.getCurrencyRateByDate(currencyId, new DateNonConvertable(toDate));
            exchangeRate = BigDecimal.valueOf(item.getExchangeRate()).setScale(exchangeRateScale, RoundingMode.HALF_UP);

            item = currencyService.getCurrencyRateByDate(currencyId, new DateNonConvertable(ServerUtils.getStartDate(new Date())));
            prevExchangeRate = BigDecimal.valueOf(item.getExchangeRate()).setScale(exchangeRateScale, RoundingMode.HALF_UP);
        }

        TotalDebitCredit beginningGainLoss = new TotalDebitCredit(), gainLoss = new TotalDebitCredit();

        List<EdsAccount> foreignAccounts = accountingManager.getAccountsAttendedInTransactions(toDate, departmentAndTreeChildIDs, projectID, showAccounts, true);
        for (EdsAccount account : foreignAccounts) {

            if (account.getCurrency() == null || baseCurrencyId.equals(account.getCurrency().getObjectID())) {
                continue;
            }

            TotalDebitCredit dcTotalPeriod = new TotalDebitCredit();
            TotalDebitCredit dcTotalBeginningBalance = new TotalDebitCredit();
            BigDecimal balanceDiff;

            if (foreignBeginningBalanceMap.get(account.getObjectID()) != null) {
                balanceDiff = calculateGainLossBalance(dcTotalBeginningBalance, foreignBeginningBalanceMap.get(account.getObjectID()), currencyId, baseCurrencyId, prevExchangeRate, exchangeRateScale);
                if (balanceDiff.compareTo(BigDecimal.ZERO) > 0) {
                    beginningGainLoss.credit = beginningGainLoss.credit.add(balanceDiff);
                } else {
                    beginningGainLoss.debit = beginningGainLoss.debit.add(balanceDiff.abs());
                }

                if (!account.getCurrency().getObjectID().equals(currencyId)) {
                    TotalDebitCredit dcTotalBeginningBalanceInCurrent = new TotalDebitCredit();
                    calculateGainLossBalance(dcTotalBeginningBalanceInCurrent, foreignBeginningBalanceMap.get(account.getObjectID()), currencyId, baseCurrencyId, exchangeRate, exchangeRateScale);

                    balanceDiff = dcTotalBeginningBalanceInCurrent.getRealDebitCreditDiffInBase().subtract(dcTotalBeginningBalance.getRealDebitCreditDiffInBase());
                    if (balanceDiff.compareTo(BigDecimal.ZERO) > 0) {
                        gainLoss.credit = gainLoss.credit.add(balanceDiff);
                    } else {
                        gainLoss.debit = gainLoss.debit.add(balanceDiff.abs());
                    }
                }
            }

            if (foreignDebitCreditMap.get(account.getObjectID()) != null) {
                balanceDiff = calculateGainLossBalance(dcTotalPeriod, foreignDebitCreditMap.get(account.getObjectID()), currencyId, baseCurrencyId, exchangeRate, exchangeRateScale);

                if (balanceDiff.compareTo(BigDecimal.ZERO) > 0) {
                    gainLoss.credit = gainLoss.credit.add(balanceDiff);
                } else {
                    gainLoss.debit = gainLoss.debit.add(balanceDiff.abs());
                }
            }
        }

        TotalDebitCredit reTotalDebitCredit = new TotalDebitCredit();//debit credit of Retained Earnings
        TrialBalanceAccountsItem accountsItem = new TrialBalanceAccountsItem();
        accountsItem.setSummary(summary);
        accountsItem.setAccounts(accounts);
        accountsItem.setDebitCreditMap(debitCreditMap);
        accountsItem.setBeginningBalanceMap(beginningBalanceMap);
        accountsItem.setForeignBeginningBalanceMap(foreignBeginningBalanceMap);
        accountsItem.setCurrencyId(currencyId);
        accountsItem.setBaseCurrencyId(baseCurrencyId);
        accountsItem.setExchangeRateScale(exchangeRateScale);
        accountsItem.setPrevExchangeRate(prevExchangeRate);
        accountsItem.setExchangeRate(exchangeRate);
        accountsItem.setPrevBalanceDate(new Date());
        accountsItem.setToDate(toDate);
        accountsItem.setForeignDebitCreditMap(foreignDebitCreditMap);
        accountsItem.setGainLoss(gainLoss);
        accountsItem.setShowAccounts(showAccounts);
        accountsItem.setMapAccountTypeByList(mapAccountTypeByList);
        accountsItem.setReTotalDebitCredit(reTotalDebitCredit);
        accountsItem.setTrailBalanceTotalMap(trailBalanceTotalMap);
        accountsItem.setTempAccountByTypeMapSummary(tempAccountByTypeMapSummary);
        accountsItem.setTempAccountByTypeMap(tempAccountByTypeMap);
        for (EdsAccount account : accountsHash) {
            accountsItem.setAccount(account);
            accountsItem.setParent(false);
            accountToBalanceItem(accountsItem);
        }
        if (summary) {
            for (TrialBalanceItem tbi : accountsItem.getTempAccountByTypeMapSummary().values()) {
                if (!(tbi == null || tbi.isFloatingAccount())) {
                    accountsItem.getMapAccountTypeByList().get(tbi.getCategoryType()).add(tbi);
                }
            }
        } else {
            for (Map.Entry<Integer, TrialBalanceItem> item : accountsItem.getTempAccountByTypeMap().entrySet()) {
                TrialBalanceItem tbi = item.getValue();
                if (!tbi.isFloatingAccount()) {
                    mapAccountTypeByList.get(tbi.getCategoryType()).add(tbi);
                }
            }
        }
        beginningGainLoss.multiply(accountsItem.getPrevExchangeRate());
        calculateRetainedEarningsAccount(accountsItem.getReTotalDebitCredit(), beginningGainLoss);
        calculateRetainedEarningsForTrialBalance(accountsItem.getReTotalDebitCredit(), accountsItem.getMapAccountTypeByList(), accountsItem.getTrailBalanceTotalMap());
    }

    private void accountToBalanceItem(TrialBalanceAccountsItem item) {
        if (item != null) {
            TrialBalanceItem tbi = new TrialBalanceItem(item.getAccount().getObjectID(),
                    item.isSummary() ? accountingLocalizer.localize(item.getAccount().getAccountType().getCode(), item.getAccount().getAccountType().getName()) : item.getAccount().getName());
            tbi.setCode(item.getAccount().getAccountCode());
            tbi.setCategoryCode(item.getAccount().getAccountType().getCode());
            tbi.setCategoryType(item.getAccount().getAccountType().getCategory());
            tbi.setBaseAccountId(item.getAccount().getBaseAccount() != null ? item.getAccount().getBaseAccount().getObjectID() : null);
            tbi.setKey(item.getAccount().getKey());
            if (item.getAccount().getParent() != null) {
                EdsAccount parentAccount = item.getAccount().getParent();
                tbi.setParentId(parentAccount.getObjectID());
                tbi.setParentName(parentAccount.getName());
                tbi.setParentCode(parentAccount.getAccountCode());
            }

            TotalDebitCredit dcTotalPeriod = item.getDebitCreditMap().computeIfAbsent(item.getAccount().getObjectID(), k -> new TotalDebitCredit());
            TotalDebitCredit dcTotalBeginningBalance = item.getBeginningBalanceMap().computeIfAbsent(item.getAccount().getObjectID(), k -> new TotalDebitCredit());

            if (item.getAccount().isForeignAccount()) {
                BigDecimal balanceDiff = BigDecimal.ZERO;
                BigDecimal balanceDiffBegin = BigDecimal.ZERO;

                TotalDebitCredit dcTotalBeginningBalanceInCurrent = new TotalDebitCredit();
                if (item.getForeignBeginningBalanceMap().get(item.getAccount().getObjectID()) != null || item.getForeignDebitCreditMap().get(item.getAccount().getObjectID()) != null) {
                    if (item.getForeignBeginningBalanceMap().get(item.getAccount().getObjectID()) != null) {
                        calculateGainLossBalance(dcTotalBeginningBalance, item.getForeignBeginningBalanceMap().get(item.getAccount().getObjectID()), item.getCurrencyId(), item.getBaseCurrencyId(), item.getPrevExchangeRate(), item.getExchangeRateScale());
                        calculateGainLossBalance(dcTotalBeginningBalanceInCurrent, item.getForeignBeginningBalanceMap().get(item.getAccount().getObjectID()), item.getCurrencyId(), item.getBaseCurrencyId(), item.getExchangeRate(), item.getExchangeRateScale());
                    }

                    if (!item.getCurrencyId().equals(item.getAccount().getCurrency().getObjectID())) {
                        balanceDiffBegin = dcTotalBeginningBalanceInCurrent.getRealDebitCreditDiffInBase().subtract(dcTotalBeginningBalance.getRealDebitCreditDiffInBase());
                        balanceDiffBegin = balanceDiffBegin.multiply(item.getExchangeRate());

                        if (item.getForeignDebitCreditMap().get(item.getAccount().getObjectID()) != null) {
                            balanceDiff = calculateGainLossBalance(dcTotalPeriod, item.getForeignDebitCreditMap().get(item.getAccount().getObjectID()), item.getCurrencyId(), item.getBaseCurrencyId(), item.getExchangeRate(), item.getExchangeRateScale());
                            dcTotalPeriod.debit = item.getForeignDebitCreditMap().get(item.getAccount().getObjectID()).debitInBase;
                            dcTotalPeriod.credit = item.getForeignDebitCreditMap().get(item.getAccount().getObjectID()).creditInBase;
                        }

                        balanceDiff = balanceDiff.multiply(item.getExchangeRate());


                        dcTotalPeriod.debit = dcTotalPeriod.debit.multiply(item.getExchangeRate());

                        dcTotalPeriod.credit = dcTotalPeriod.credit.multiply(item.getExchangeRate());

                    }

                    if (balanceDiffBegin.compareTo(BigDecimal.ZERO) > 0) {
                        dcTotalPeriod.debit = dcTotalPeriod.debit.add(balanceDiffBegin);
                    } else {
                        dcTotalPeriod.credit = dcTotalPeriod.credit.add(balanceDiffBegin.abs());
                    }

                    if (balanceDiff.compareTo(BigDecimal.ZERO) > 0) {
                        dcTotalPeriod.debit = dcTotalPeriod.debit.add(balanceDiff);
                    } else {
                        dcTotalPeriod.credit = dcTotalPeriod.credit.add(balanceDiff.abs());
                    }

                }
            } else {
                dcTotalPeriod.multiply(item.getExchangeRate());
                dcTotalBeginningBalance.multiply(item.getPrevExchangeRate());
            }

            if (item.getAccount() instanceof EdsFloatingAccount) {
                item.getGainLoss().multiply(item.getExchangeRate());
                dcTotalPeriod = dcTotalPeriod.add(item.getGainLoss());
            }

            BigDecimal dcPeriod = dcTotalPeriod.getDebitCreditDiff();
            BigDecimal dcBeginningBalance = dcTotalBeginningBalance.getRealDebitCreditDiff();

            tbi.setBeginningDebit(dcBeginningBalance.compareTo(BigDecimal.ZERO) >= 0 ? dcBeginningBalance : BigDecimal.ZERO);
            tbi.setBeginningCredit(dcBeginningBalance.compareTo(BigDecimal.ZERO) < 0 ? dcBeginningBalance.multiply(new BigDecimal(-1)) : BigDecimal.ZERO);

            if (!(Constants.ASSETS.equals(item.getAccount().getAccountType().getCategory()) || Constants.EXPENSES.equals(item.getAccount().getAccountType().getCategory()))) {
                dcBeginningBalance = dcBeginningBalance.multiply(new BigDecimal(-1));
            }

            if (item.isParent() || (item.getShowAccounts() == 1 && dcPeriod.compareTo(BigDecimal.ZERO) > 0
                    || item.getShowAccounts() == 2 && (dcBeginningBalance.abs().compareTo(BigDecimal.ZERO) > 0
                    || dcPeriod.compareTo(BigDecimal.ZERO) > 0) || item.getShowAccounts() == 3)) {

                if (item.getAccount() instanceof EdsFloatingAccount) {
                    ////////Debit/Credit for selected period
                    byte sidePeriod = dcTotalPeriod.dcCompare();
                    tbi.setFloatingAccount(true);
                    calculateFloatingAccount(item.getMapAccountTypeByList(), item.getAccount(), tbi, sidePeriod);
                }
                if (Constants.REVENUE.equals(item.getAccount().getAccountType().getCategory()) || Constants.EXPENSES.equals(item.getAccount().getAccountType().getCategory())) {
                    calculateRetainedEarningsAccount(item.getReTotalDebitCredit(), dcTotalBeginningBalance);
//                    if ("74078".equals(ServerSecurityContext.getInstance().getCompanyId()) || "71409".equals(ServerSecurityContext.getInstance().getCompanyId())) {
//                        calculateRetainedEarningsCurrentPeriod(item.getReTotalDebitCredit(), dcTotalBalance);
//                    }
                    dcBeginningBalance = dcTotalBeginningBalance.getRealDebitCreditDiff();
                }
                tbi.setBeginningBalance(dcBeginningBalance);
                if (Constants.ASSETS.equals(item.getAccount().getAccountType().getCategory()) || Constants.EXPENSES.equals(item.getAccount().getAccountType().getCategory())) {
                    item.getTrailBalanceTotalMap().put("totalBeginningBalance", item.getTrailBalanceTotalMap().get("totalBeginningBalance").subtract(dcBeginningBalance));
                } else {
                    item.getTrailBalanceTotalMap().put("totalBeginningBalance", item.getTrailBalanceTotalMap().get("totalBeginningBalance").add(dcBeginningBalance));
                }
                item.getTrailBalanceTotalMap().put("totalBeginningDebit", item.getTrailBalanceTotalMap().get("totalBeginningDebit").add(tbi.getBeginningDebit()));
                item.getTrailBalanceTotalMap().put("totalBeginningCredit", item.getTrailBalanceTotalMap().get("totalBeginningCredit").add(tbi.getBeginningCredit()));

                tbi.setDebit(dcTotalPeriod.debit.setScale(5,RoundingMode.HALF_UP));
                item.getTrailBalanceTotalMap().put("totalDebit", item.getTrailBalanceTotalMap().get("totalDebit").add(dcTotalPeriod.debit));

                tbi.setCredit(dcTotalPeriod.credit.setScale(5,RoundingMode.HALF_UP));
                item.getTrailBalanceTotalMap().put("totalCredit", item.getTrailBalanceTotalMap().get("totalCredit").add(dcTotalPeriod.credit));

                BigDecimal debCred = (tbi.getDebit() != null ? tbi.getDebit() : BigDecimal.ZERO).subtract(tbi.getCredit() != null ? tbi.getCredit() : BigDecimal.ZERO);

                BigDecimal endingBalance;
                if (tbi.getBeginningDebit().compareTo(BigDecimal.ZERO) > 0) {
                    endingBalance = tbi.getBeginningDebit().add(debCred);
                } else if (tbi.getBeginningCredit().compareTo(BigDecimal.ZERO) > 0) {
                    endingBalance = (tbi.getBeginningCredit().subtract(debCred)).multiply(new BigDecimal(-1));
                } else {
                    endingBalance = debCred.multiply(BigDecimal.ONE);
                }

                tbi.setEndingDebit(endingBalance.compareTo(BigDecimal.ZERO) >= 0 ? endingBalance : BigDecimal.ZERO);
                tbi.setEndingCredit(endingBalance.compareTo(BigDecimal.ZERO) < 0 ? endingBalance.multiply(new BigDecimal(-1)) : BigDecimal.ZERO);

                if (!(Constants.ASSETS.equals(item.getAccount().getAccountType().getCategory()) || Constants.EXPENSES.equals(item.getAccount().getAccountType().getCategory()))) {
                    debCred = debCred.multiply(new BigDecimal(-1));
                }

                tbi.setEndingBalance(tbi.getBeginningBalance().add(debCred));

                if (item.isSummary()) {
                    if (item.getTempAccountByTypeMapSummary().get(tbi.getCategoryCode()) != null) {
                        TrialBalanceItem parentTbi = item.getTempAccountByTypeMapSummary().get(tbi.getCategoryCode());
                        parentTbi.setBeginningBalance(parentTbi.getBeginningBalance().add(tbi.getBeginningBalance()));
                        parentTbi.setBeginningDebit(parentTbi.getBeginningDebit().add(tbi.getBeginningDebit()));
                        parentTbi.setBeginningCredit(parentTbi.getBeginningCredit().add(tbi.getBeginningCredit()));
                        parentTbi.setDebit(parentTbi.getDebit().add(tbi.getDebit()));
                        parentTbi.setCredit(parentTbi.getCredit().add(tbi.getCredit()));
                        parentTbi.setEndingDebit(parentTbi.getEndingDebit().add(tbi.getEndingDebit()));
                        parentTbi.setEndingCredit(parentTbi.getEndingCredit().add(tbi.getEndingCredit()));
                        parentTbi.setEndingBalance(parentTbi.getEndingBalance().add(tbi.getEndingBalance()));
                        item.getTempAccountByTypeMapSummary().put(tbi.getCategoryCode(), parentTbi);
                    } else {
                        item.getTempAccountByTypeMapSummary().put(tbi.getCategoryCode(), tbi);
                    }
                } else {
                    if (item.getShowAccounts() != null && (item.getShowAccounts().equals(ALL_ACCOUNTS) ||
                            (item.getShowAccounts().equals(2)) && ((tbi.getCredit() != null && tbi.getCredit().compareTo(BigDecimal.ZERO) != 0) || (tbi.getDebit() != null && tbi.getDebit().compareTo(BigDecimal.ZERO) != 0) || (tbi.getEndingBalance() != null && tbi.getEndingBalance().compareTo(BigDecimal.ZERO) != 0)))) {
                        item.getTempAccountByTypeMap().put(item.getAccount().getObjectID(), tbi);
                    }
                }

                if (Constants.ASSETS.equals(item.getAccount().getAccountType().getCategory()) || Constants.EXPENSES.equals(item.getAccount().getAccountType().getCategory())) {
                    item.getTrailBalanceTotalMap().put("totalEndingBalance", item.getTrailBalanceTotalMap().get("totalEndingBalance").subtract(tbi.getEndingBalance()));
                } else {
                    item.getTrailBalanceTotalMap().put("totalEndingBalance", item.getTrailBalanceTotalMap().get("totalEndingBalance").add(tbi.getEndingBalance()));
                }
                item.getTrailBalanceTotalMap().put("totalEndingDebit", item.getTrailBalanceTotalMap().get("totalEndingDebit").add(tbi.getEndingDebit()));
                item.getTrailBalanceTotalMap().put("totalEndingCredit", item.getTrailBalanceTotalMap().get("totalEndingCredit").add(tbi.getEndingCredit()));
            }
            if (item.getAccount().getParent() != null) {
                EdsAccount parentAccount = item.getAccount().getParent();
                if (!item.getAccounts().contains(parentAccount)) {
                    item.getAccounts().add(parentAccount);
                    item.setAccount(parentAccount);
                    item.setParent(true);
                    accountToBalanceItem(item);
                }
            }
        }
    }

    public BigDecimal calculateGainLossBalance(TotalDebitCredit glTotalPeriod, TotalDebitCredit debitCredit, Integer currencyId, Integer baseCurrencyId, BigDecimal exchangeRate, Integer exchangeRateScale) {
        BigDecimal balanceDiff = BigDecimal.ZERO;

        if (baseCurrencyId.equals(debitCredit.getCurrencyID()) || debitCredit.getCurrencyID().equals(0)) {
            glTotalPeriod.debitInBase = glTotalPeriod.debitInBase.add(glTotalPeriod.debit);
            glTotalPeriod.debit = glTotalPeriod.debit.add(debitCredit.debit.multiply(exchangeRate).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));

            glTotalPeriod.creditInBase = glTotalPeriod.creditInBase.add(glTotalPeriod.credit);
            glTotalPeriod.credit = glTotalPeriod.credit.add(debitCredit.credit.multiply(exchangeRate).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        } else {
            BigDecimal balanceXR;

            CurrencyListItem item = currencyService.getCurrencyRateByDate(debitCredit.getCurrencyID(), new DateNonConvertable(ServerUtils.getStartDate(new Date())));
            balanceXR = BigDecimal.valueOf(item.getExchangeRate()).setScale(exchangeRateScale, RoundingMode.HALF_UP);

            BigDecimal debit = debitCredit.debit.divide(balanceXR, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            BigDecimal credit = debitCredit.credit.divide(balanceXR, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            balanceDiff = balanceDiff.add(debit.subtract(credit).subtract(debitCredit.debitInBase.subtract(debitCredit.creditInBase)));

            glTotalPeriod.debitInBase = glTotalPeriod.debitInBase.add(debit);
            glTotalPeriod.creditInBase = glTotalPeriod.creditInBase.add(credit);

            if (!currencyId.equals(debitCredit.getCurrencyID())) {
                glTotalPeriod.debit = glTotalPeriod.debit.add(debit.multiply(exchangeRate).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                glTotalPeriod.credit = glTotalPeriod.credit.add(credit.multiply(exchangeRate).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            } else {
                glTotalPeriod.debit = glTotalPeriod.debit.add(debitCredit.debit);
                glTotalPeriod.credit = glTotalPeriod.credit.add(debitCredit.credit);
            }
        }
        return balanceDiff;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getBalanceSheetAllSubsidiariesReport(Date fromDate, Date toDate, Map<String, List<BalanceSheetInnerItem>> balanceSheetMap, Map<String, BigDecimal> balanceSheetTotalMap,
            Integer baseCurrencyID, Map<String, BalanceSheetInnerItem> mergeBalanceSheetMap,
            String departmentAndTreeChildIDs, Integer projectID) {
        EdsCompany edsCompany = companyManager.get(SecurityContext.getCompanyID());
        if (!edsCompany.isDeleted() && edsCompany.getAccountingSetup()) {
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            Date financialYearEnd = financialSettings.getFinancialYearEnd();
            Date financialStartDate = getBeginningOfAccountingPeriod(financialYearEnd, toDate);
            fromDate.setTime(0);//1970

            Date lastFinancialYearEnd = getEndingOfAccountingPeriod(financialYearEnd, toDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastFinancialYearEnd);
            calendar.add(Calendar.YEAR, -1);
            lastFinancialYearEnd = calendar.getTime();

            List<EdsAccount> assetTransAccounts = transactionManager.getBalanceSubsidiariesDataByDateAndAccountCategory(EdsAccountType.ASSETS, fromDate, toDate, departmentAndTreeChildIDs, projectID);
            List<EdsAccount> liabilityTransAccounts = transactionManager.getBalanceSubsidiariesDataByDateAndAccountCategory(EdsAccountType.LIABILITIES, fromDate, toDate, departmentAndTreeChildIDs, projectID);
            List<EdsAccount> equityTransAccounts = transactionManager.getBalanceSubsidiariesDataByDateAndAccountCategory(EdsAccountType.EQUITY, fromDate, toDate, departmentAndTreeChildIDs, projectID);

            List<Object[]> revenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);
            List<Object[]> expenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);

            List<Object[]> currentRevenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);
            List<Object[]> currentExpenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);

            Integer companyBaseCurrencyID = financialSettingsManager.getFinancialSettings().getCurrency().getObjectID();
            Map<String, BigDecimal> exchangeRateMap = new HashMap<>();
            boolean compCurParentCompCurEquals = true;
            if (!companyBaseCurrencyID.equals(baseCurrencyID)) {
                compCurParentCompCurEquals = false;
                exchangeRateMap = exchangeRateManager.getExchangeRatesByCurrency(companyBaseCurrencyID, fromDate, toDate);
            }

            for (EdsAccount account : assetTransAccounts) {
                TotalDebitCredit totalDebitCredit = account.getDebitCreditTotal(fromDate, toDate, exchangeRateMap, compCurParentCompCurEquals);
                BigDecimal difference = totalDebitCredit.getRealDebitCreditDiff();
                BalanceSheetInnerItem balanceSheetInnerItem;
                boolean isMultiAccount = false;
                if (account.getAccountType() != null && mergeBalanceSheetMap.containsKey(account.getName() + "{@}" + account.getAccountType().getObjectID())) {
                    isMultiAccount = true;
                    balanceSheetInnerItem = mergeBalanceSheetMap.get(account.getName() + "{@}" + account.getAccountType().getObjectID());
                } else {
                    balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                    mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                }

                if (EdsAccountType.FIXED_ASSET.equals(account.getAccountType().getCode())) {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.FIXED_ASSET).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.FIXED_ASSET, balanceSheetTotalMap.get(AccountingConstants.FIXED_ASSET).add(difference));
                } else if (EdsAccountType.BANK.equals(account.getAccountType().getCode())) {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.BANK).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.BANK, balanceSheetTotalMap.get(AccountingConstants.BANK).add(difference));
                } else if (EdsAccountType.PREPAYMENT.equals(account.getAccountType().getCode())) {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.PREPAYMENT).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.PREPAYMENT, balanceSheetTotalMap.get(AccountingConstants.PREPAYMENT).add(difference));
                } else {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.CURRENT_ASSET).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.CURRENT_ASSET, balanceSheetTotalMap.get(AccountingConstants.CURRENT_ASSET).add(difference));

                    if (account.getKey() != null && account.getKey().equals(EdsAccount.ACCOUNTS_RECEIVABLE)) {
                        BalanceSheetInnerItem bsiReceivable;
                        BigDecimal interCompanyDifference = totalDebitCredit.getInterCompanyRealDebitCreditDiff();
                        String interCompanyAccountKey = AccountingConstants.ACCOUNTS_RECEIVABLE_INTERCOMPANY/* + (account.getCurrency() != null ? account.getCurrency().getName() : "")*/;
                        if (mergeBalanceSheetMap.containsKey(interCompanyAccountKey)) {
                            bsiReceivable = mergeBalanceSheetMap.get(interCompanyAccountKey);
                            bsiReceivable.setValue((bsiReceivable.getValue() != null ? bsiReceivable.getValue() : BigDecimal.ZERO).add(interCompanyDifference));
                        } else {
                            bsiReceivable = new BalanceSheetInnerItem("Accounts Receivable Intercompany", interCompanyDifference);
                            mergeBalanceSheetMap.put(interCompanyAccountKey, bsiReceivable);
                            balanceSheetMap.get(AccountingConstants.CURRENT_ASSET).add(bsiReceivable);
                        }
                        balanceSheetTotalMap.put(AccountingConstants.CURRENT_ASSET, balanceSheetTotalMap.get(AccountingConstants.CURRENT_ASSET).add(interCompanyDifference));
                    }
                }
            }
            balanceSheetTotalMap.put(AccountingConstants.TOTAL_ASSET,
                    balanceSheetTotalMap.get(AccountingConstants.FIXED_ASSET)
                            .add(balanceSheetTotalMap.get(AccountingConstants.BANK))
                            .add(balanceSheetTotalMap.get(AccountingConstants.PREPAYMENT))
                            .add(balanceSheetTotalMap.get(AccountingConstants.CURRENT_ASSET)));

            for (EdsAccount account : liabilityTransAccounts) {
                TotalDebitCredit totalDebitCredit = account.getDebitCreditTotal(fromDate, toDate, exchangeRateMap, compCurParentCompCurEquals);
                BigDecimal difference = BigDecimal.ZERO.subtract(totalDebitCredit.getRealDebitCreditDiff());
                BalanceSheetInnerItem balanceSheetInnerItem;
                boolean isMultiAccount = false;
                if (account.getAccountType() != null && mergeBalanceSheetMap.containsKey(account.getName() + "{@}" + account.getAccountType().getObjectID())) {
                    isMultiAccount = true;
                    balanceSheetInnerItem = mergeBalanceSheetMap.get(account.getName() + "{@}" + account.getAccountType().getObjectID());
                } else {
                    balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                    mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                }
                if (EdsAccountType.CURRENT_LIABILITY.equals(account.getAccountType().getCode())) {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.CURRENT_LIABILITY).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.CURRENT_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.CURRENT_LIABILITY).add(difference));

                    if (account.getKey() != null && account.getKey().equals(EdsAccount.ACCOUNTS_PAYABLE)) {
                        BalanceSheetInnerItem bsiPayable;
                        BigDecimal interCompanyDifference = BigDecimal.ZERO.subtract(totalDebitCredit.getInterCompanyRealDebitCreditDiff());
                        String interCompanyAccountKey = AccountingConstants.ACCOUNTS_PAYABLE_INTERCOMPANY/* + (account.getCurrency() != null ? account.getCurrency().getName() : "")*/;
                        if (mergeBalanceSheetMap.containsKey(interCompanyAccountKey)) {
                            bsiPayable = mergeBalanceSheetMap.get(interCompanyAccountKey);
                            bsiPayable.setValue((bsiPayable.getValue() != null ? bsiPayable.getValue() : BigDecimal.ZERO).add(interCompanyDifference));
                        } else {
                            bsiPayable = new BalanceSheetInnerItem("Accounts Payable Intercompany", interCompanyDifference);
                            mergeBalanceSheetMap.put(interCompanyAccountKey, bsiPayable);
                            balanceSheetMap.get(AccountingConstants.CURRENT_LIABILITY).add(bsiPayable);
                        }
                        balanceSheetTotalMap.put(AccountingConstants.CURRENT_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.CURRENT_LIABILITY).add(interCompanyDifference));
                    }

                } else if (EdsAccountType.LONG_TERM_LIABILITY.equals(account.getAccountType().getCode())) {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.LONG_TERM_LIABILITY).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.LONG_TERM_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.LONG_TERM_LIABILITY).add(difference));
                } else {
                    if (isMultiAccount) {
                        balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                    } else {
                        balanceSheetMap.get(AccountingConstants.LIABILITY).add(balanceSheetInnerItem);
                    }
                    balanceSheetTotalMap.put(AccountingConstants.LIABILITY, balanceSheetTotalMap.get(AccountingConstants.LIABILITY).add(difference));
                }
            }
            balanceSheetTotalMap.put(AccountingConstants.TOTAL_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.CURRENT_LIABILITY)
                    .add(balanceSheetTotalMap.get(AccountingConstants.LONG_TERM_LIABILITY))
                    .add(balanceSheetTotalMap.get(AccountingConstants.LIABILITY)));

            BigDecimal retainedEarnings = BigDecimal.ZERO;
            BigDecimal currentYearEarnings = BigDecimal.ZERO;

            for (Object[] obj : revenueTransItems) {
                retainedEarnings = retainedEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
            }
            for (Object[] obj : expenseTransItems) {
                retainedEarnings = retainedEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
            }
            for (Object[] obj : currentRevenueTransItems) {
                currentYearEarnings = currentYearEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
            }
            for (Object[] obj : currentExpenseTransItems) {
                currentYearEarnings = currentYearEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
            }

            boolean retainedEarningsFound = false;
            for (EdsAccount account : equityTransAccounts) {
                TotalDebitCredit totalDebitCredit = account.getDebitCreditTotal(fromDate, toDate, exchangeRateMap, compCurParentCompCurEquals);
                BigDecimal difference = BigDecimal.ZERO.subtract(totalDebitCredit.getRealDebitCreditDiff());
                if (account.getKey() != null && account.getKey().equals(EdsAccount.RETAINED_EARNINGS)) {
                    difference = difference.add(retainedEarnings);
                    retainedEarningsFound = true;
                }
                BalanceSheetInnerItem balanceSheetInnerItem;
                boolean isMultiAccount = false;
                if (account.getAccountType() != null && mergeBalanceSheetMap.containsKey(account.getName() + "{@}" + account.getAccountType().getObjectID())) {
                    isMultiAccount = true;
                    balanceSheetInnerItem = mergeBalanceSheetMap.get(account.getName() + "{@}" + account.getAccountType().getObjectID());
                } else {
                    balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                    mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                }
                if (isMultiAccount) {
                    balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                } else {
                    balanceSheetMap.get(AccountingConstants.EQUITY).add(balanceSheetInnerItem);
                }
                balanceSheetTotalMap.put(AccountingConstants.EQUITY, balanceSheetTotalMap.get(AccountingConstants.EQUITY).add(difference));
            }
            //Retained Earnings
            if (!retainedEarningsFound) {
                EdsAccount account = accountingManager.getAccountByKey(EdsAccount.RETAINED_EARNINGS);
                BigDecimal difference = retainedEarnings;
                BalanceSheetInnerItem balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                balanceSheetMap.get(AccountingConstants.EQUITY).add(balanceSheetInnerItem);
                balanceSheetTotalMap.put(AccountingConstants.EQUITY, balanceSheetTotalMap.get(AccountingConstants.EQUITY).add(difference));
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getBalanceSheetAllSubsidiariesReportSummary(Date fromDate, Date toDate, Map<String, BigDecimal> balanceSheetAssetTotalMap, Map<String, BigDecimal> balanceSheetLiabilityTotalMap, Integer baseCurrencyID, Map<String, BalanceSheetInnerItem> mergeBalanceSheetMap, String departmentAndTreeChildIDs, Integer projectID) {
        EdsCompany edsCompany = companyManager.get(SecurityContext.getCompanyID());
        if (!edsCompany.isDeleted() && edsCompany.getAccountingSetup()) {
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            Date financialYearEnd = financialSettings.getFinancialYearEnd();
            Date financialStartDate = getBeginningOfAccountingPeriod(financialYearEnd, toDate);
            fromDate.setTime(0);//1970

            Date lastFinancialYearEnd = getEndingOfAccountingPeriod(financialYearEnd, toDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastFinancialYearEnd);
            calendar.add(Calendar.YEAR, -1);
            lastFinancialYearEnd = calendar.getTime();

            List<EdsAccount> assetTransAccounts = transactionManager.getBalanceSubsidiariesDataByDateAndAccountCategory(EdsAccountType.ASSETS, fromDate, toDate, departmentAndTreeChildIDs, projectID);
            List<EdsAccount> liabilityTransAccounts = transactionManager.getBalanceSubsidiariesDataByDateAndAccountCategory(EdsAccountType.LIABILITIES, fromDate, toDate, departmentAndTreeChildIDs, projectID);
            List<EdsAccount> equityTransAccounts = transactionManager.getBalanceSubsidiariesDataByDateAndAccountCategory(EdsAccountType.EQUITY, fromDate, toDate, departmentAndTreeChildIDs, projectID);

            List<Object[]> revenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);
            List<Object[]> expenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);

            List<Object[]> currentRevenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);
            List<Object[]> currentExpenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);

            Integer companyBaseCurrencyID = financialSettingsManager.getFinancialSettings().getCurrency().getObjectID();
            Map<String, BigDecimal> exchangeRateMap = new HashMap<>();
            boolean compCurParentCompCurEquals = true;
            if (!companyBaseCurrencyID.equals(baseCurrencyID)) {
                compCurParentCompCurEquals = false;
                exchangeRateMap = exchangeRateManager.getExchangeRatesByCurrency(companyBaseCurrencyID, fromDate, toDate);
            }

            for (EdsAccount account : assetTransAccounts) {
                TotalDebitCredit totalDebitCredit = account.getDebitCreditTotal(fromDate, toDate, exchangeRateMap, compCurParentCompCurEquals);
                BigDecimal difference = totalDebitCredit.getRealDebitCreditDiff();
                BalanceSheetInnerItem balanceSheetInnerItem;
                boolean isMultiAccount = false;
                account = getAccountParent(account, account.getParent());
                if (account.getAccountType() != null && mergeBalanceSheetMap.containsKey(account.getName() + "{@}" + account.getAccountType().getObjectID())) {
                    isMultiAccount = true;
                    balanceSheetInnerItem = mergeBalanceSheetMap.get(account.getName() + "{@}" + account.getAccountType().getObjectID());
                } else {
                    balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                    mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                }

                if (isMultiAccount) {
                    balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                } else {
//                    balanceSheetAssetTotalMap.get(AccountingConstants.ASSETS).add(balanceSheetInnerItem);
                }
                balanceSheetLiabilityTotalMap.put(AccountingConstants.ASSETS, balanceSheetLiabilityTotalMap.get(AccountingConstants.ASSETS).add(difference));
                if (!EdsAccountType.FIXED_ASSET.equals(account.getAccountType().getCode()) && account.getKey() != null && account.getKey().equals(EdsAccount.ACCOUNTS_RECEIVABLE)) {
                    BalanceSheetInnerItem bsiReceivable;
                    BigDecimal interCompanyDifference = totalDebitCredit.getInterCompanyRealDebitCreditDiff();
                    String interCompanyAccountKey = AccountingConstants.ACCOUNTS_RECEIVABLE_INTERCOMPANY;
                    if (mergeBalanceSheetMap.containsKey(interCompanyAccountKey)) {
                        bsiReceivable = mergeBalanceSheetMap.get(interCompanyAccountKey);
                        bsiReceivable.setValue((bsiReceivable.getValue() != null ? bsiReceivable.getValue() : BigDecimal.ZERO).add(interCompanyDifference));
                    } else {
                        bsiReceivable = new BalanceSheetInnerItem("Accounts Receivable Intercompany", interCompanyDifference);
                        mergeBalanceSheetMap.put(interCompanyAccountKey, bsiReceivable);
//                        balanceSheetAssetTotalMap.get(AccountingConstants.ASSETS).add(bsiReceivable);
                    }
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.ASSETS, balanceSheetLiabilityTotalMap.get(AccountingConstants.ASSETS).add(interCompanyDifference));
                }
            }

            for (EdsAccount account : liabilityTransAccounts) {
                TotalDebitCredit totalDebitCredit = account.getDebitCreditTotal(fromDate, toDate, exchangeRateMap, compCurParentCompCurEquals);
                BigDecimal difference = BigDecimal.ZERO.subtract(totalDebitCredit.getRealDebitCreditDiff());
                BalanceSheetInnerItem balanceSheetInnerItem;
                boolean isMultiAccount = false;
                account = getAccountParent(account, account.getParent());
                if (account.getAccountType() != null && mergeBalanceSheetMap.containsKey(account.getName() + "{@}" + account.getAccountType().getObjectID())) {
                    isMultiAccount = true;
                    balanceSheetInnerItem = mergeBalanceSheetMap.get(account.getName() + "{@}" + account.getAccountType().getObjectID());
                } else {
                    balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                    mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                }

                if (isMultiAccount) {
                    balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                } else {
//                    balanceSheetAssetTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(balanceSheetInnerItem);
                }
                balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(difference));

                if (EdsAccountType.CURRENT_LIABILITY.equals(account.getAccountType().getCode()) && account.getKey() != null && account.getKey().equals(EdsAccount.ACCOUNTS_PAYABLE)) {
                    BalanceSheetInnerItem bsiPayable;
                    BigDecimal interCompanyDifference = BigDecimal.ZERO.subtract(totalDebitCredit.getInterCompanyRealDebitCreditDiff());
                    String interCompanyAccountKey = AccountingConstants.ACCOUNTS_PAYABLE_INTERCOMPANY;
                    if (mergeBalanceSheetMap.containsKey(interCompanyAccountKey)) {
                        bsiPayable = mergeBalanceSheetMap.get(interCompanyAccountKey);
                        bsiPayable.setValue((bsiPayable.getValue() != null ? bsiPayable.getValue() : BigDecimal.ZERO).add(interCompanyDifference));
                    } else {
                        bsiPayable = new BalanceSheetInnerItem("Accounts Payable Intercompany", interCompanyDifference);
                        mergeBalanceSheetMap.put(interCompanyAccountKey, bsiPayable);
//                        balanceSheetAssetTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(bsiPayable);
                    }
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(interCompanyDifference));
                }
            }

            BigDecimal retainedEarnings = BigDecimal.ZERO;
            BigDecimal currentYearEarnings = BigDecimal.ZERO;

            for (Object[] obj : revenueTransItems) {
                retainedEarnings = retainedEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
            }
            for (Object[] obj : expenseTransItems) {
                retainedEarnings = retainedEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
            }
            for (Object[] obj : currentRevenueTransItems) {
                currentYearEarnings = currentYearEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
            }
            for (Object[] obj : currentExpenseTransItems) {
                currentYearEarnings = currentYearEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
            }

            boolean retainedEarningsFound = false;
            for (EdsAccount account : equityTransAccounts) {
                TotalDebitCredit totalDebitCredit = account.getDebitCreditTotal(fromDate, toDate, exchangeRateMap, compCurParentCompCurEquals);
                BigDecimal difference = BigDecimal.ZERO.subtract(totalDebitCredit.getRealDebitCreditDiff());
                BalanceSheetInnerItem balanceSheetInnerItem;
                boolean isMultiAccount = false;
                account = getAccountParent(account, account.getParent());
                if (account.getKey() != null && account.getKey().equals(EdsAccount.RETAINED_EARNINGS)) {
                    difference = difference.add(retainedEarnings);
                    retainedEarningsFound = true;
                }
                if (account.getAccountType() != null && mergeBalanceSheetMap.containsKey(account.getName() + "{@}" + account.getAccountType().getObjectID())) {
                    isMultiAccount = true;
                    balanceSheetInnerItem = mergeBalanceSheetMap.get(account.getName() + "{@}" + account.getAccountType().getObjectID());
                } else {
                    balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                    mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
                }
                if (isMultiAccount) {
                    balanceSheetInnerItem.setValue((balanceSheetInnerItem.getValue() != null ? balanceSheetInnerItem.getValue() : BigDecimal.ZERO).add(difference));
                } else {
//                    balanceSheetAssetTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(balanceSheetInnerItem);
                }
                balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(difference));
            }
            //Retained Earnings
            if (!retainedEarningsFound) {
                EdsAccount account = accountingManager.getAccountByKey(EdsAccount.RETAINED_EARNINGS);
                BigDecimal difference = retainedEarnings;
                BalanceSheetInnerItem balanceSheetInnerItem = new BalanceSheetInnerItem(account.getName(), difference, account.getObjectID());
                mergeBalanceSheetMap.put(account.getName() + "{@}" + account.getAccountType().getObjectID(), balanceSheetInnerItem);
//                balanceSheetAssetTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(balanceSheetInnerItem);
                balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(difference));
            }
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getBalanceSheetReport(Date fromDate, Date toDate, Map<String, List<BalanceSheetInnerItem>> balanceSheetMap, Map<String, BigDecimal> balanceSheetTotalMap,
            BigDecimal exchangeRate, String departmentAndTreeChildIDs, Integer currencyId, Integer projectID) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Date financialYearEnd = financialSettings.getFinancialYearEnd();
        Date financialStartDate = getBeginningOfAccountingPeriod(financialYearEnd, toDate);
        fromDate.setTime(0);//1970

        Date lastFinancialYearEnd = getEndingOfAccountingPeriod(financialYearEnd, toDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastFinancialYearEnd);
        calendar.add(Calendar.YEAR, -1);
        lastFinancialYearEnd = calendar.getTime();

        List<Object[]> assetTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.ASSETS, fromDate, toDate, departmentAndTreeChildIDs, projectID);
        List<Object[]> liabilityTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.LIABILITIES, fromDate, toDate, departmentAndTreeChildIDs, projectID);
        List<Object[]> equityTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EQUITY, fromDate, toDate, departmentAndTreeChildIDs, projectID);

        List<Object[]> revenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);
        List<Object[]> expenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);

        List<Object[]> currentRevenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);
        List<Object[]> currentExpenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);

        exchangeRate = exchangeRate == null ? BigDecimal.ONE : exchangeRate;
        Integer baseCurrencyId = financialSettings.getCurrency().getObjectID();

        HashMap<Integer, BigDecimal> rateMap = new HashMap<>();
        for (Object[] obj : assetTransItems) {
            EdsAccount account = accountingManager.get((Integer) obj[2]);
            BigDecimal difference = getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true);

            if (account.isForeignAccount()) {
                Integer accountCurrencyID = account.getCurrency() != null ? account.getCurrency().getObjectID() : baseCurrencyId;
                if (!accountCurrencyID.equals(currencyId)) {
                    //exchange rate between bank account currency and base currency
                    BigDecimal bankCurrencyExchangeRate;

                    if (rateMap.get(accountCurrencyID) != null) {
                        bankCurrencyExchangeRate = rateMap.get(accountCurrencyID);
                    } else {
                        CurrencyListItem currencyItem = currencyService.getCurrencyRateByDate(accountCurrencyID, new DateNonConvertable(ServerUtils.getStartDate(new Date())));
                        bankCurrencyExchangeRate = currencyItem != null ? BigDecimal.valueOf(currencyItem.getExchangeRate()).setScale(financialSettings.getExchangeRateScale(), RoundingMode.HALF_UP) : BigDecimal.ONE;
                        rateMap.put(accountCurrencyID, bankCurrencyExchangeRate);
                    }
                    //this line does bank account amount convert to base currency amount
                    difference = difference.divide(bankCurrencyExchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    difference = difference.multiply(exchangeRate);
                }
            } else {
                difference = difference.multiply(exchangeRate);
            }
            if (EdsAccountType.NON_CURRENT_ASSET.equals(account.getAccountType().getCode())) {
                balanceSheetMap.get(AccountingConstants.NON_CURRENT_ASSET).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.NON_CURRENT_ASSET, balanceSheetTotalMap.get(AccountingConstants.NON_CURRENT_ASSET).add(difference));
            } else if (EdsAccountType.FIXED_ASSET.equals(account.getAccountType().getCode())) {
                balanceSheetMap.get(AccountingConstants.FIXED_ASSET).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.FIXED_ASSET, balanceSheetTotalMap.get(AccountingConstants.FIXED_ASSET).add(difference));
            } else if (EdsAccountType.BANK.equals(account.getAccountType().getCode())) {
                balanceSheetMap.get(AccountingConstants.BANK).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.BANK, balanceSheetTotalMap.get(AccountingConstants.BANK).add(difference));
            } else if (EdsAccountType.PREPAYMENT.equals(account.getAccountType().getCode())) {
                balanceSheetMap.get(AccountingConstants.PREPAYMENT).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.PREPAYMENT, balanceSheetTotalMap.get(AccountingConstants.PREPAYMENT).add(difference));
            } else if (EdsAccountType.CURRENT_ASSET.equals(account.getAccountType().getCode()) && account.getEnablePayments()) {
                balanceSheetMap.get(AccountingConstants.CASH).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.CASH, balanceSheetTotalMap.get(AccountingConstants.CASH).add(difference));
            } else {
                balanceSheetMap.get(AccountingConstants.CURRENT_ASSET).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.CURRENT_ASSET, balanceSheetTotalMap.get(AccountingConstants.CURRENT_ASSET).add(difference));
            }
            balanceSheetTotalMap.put(AccountingConstants.TOTAL_ASSET, balanceSheetTotalMap.get(AccountingConstants.TOTAL_ASSET).add(difference));
        }

        for (Object[] obj : liabilityTransItems) {
            EdsAccount account = accountingManager.get((Integer) obj[2]);
            BigDecimal difference = getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false);

            if (account.isForeignAccount()) {
                Integer accountCurrencyID = account.getCurrency() != null ? account.getCurrency().getObjectID() : baseCurrencyId;
                if (!accountCurrencyID.equals(currencyId)) {
                    //exchange rate between bank account currency and base currency
                    BigDecimal bankCurrencyExchangeRate;

                    if (rateMap.get(accountCurrencyID) != null) {
                        bankCurrencyExchangeRate = rateMap.get(accountCurrencyID);
                    } else {
                        CurrencyListItem currencyItem = currencyService.getCurrencyRateByDate(accountCurrencyID, new DateNonConvertable(ServerUtils.getStartDate(new Date())));
                        bankCurrencyExchangeRate = currencyItem != null ? BigDecimal.valueOf(currencyItem.getExchangeRate()).setScale(financialSettings.getExchangeRateScale(), RoundingMode.HALF_UP) : BigDecimal.ONE;
                        rateMap.put(accountCurrencyID, bankCurrencyExchangeRate);
                    }
                    //this line does bank account amount convert to base currency amount
                    difference = difference.divide(bankCurrencyExchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    difference = difference.multiply(exchangeRate);
                }
            } else {
                difference = difference.multiply(exchangeRate);
            }
            if (EdsAccountType.CURRENT_LIABILITY.equals(account.getAccountType().getCode())) {
                balanceSheetMap.get(AccountingConstants.CURRENT_LIABILITY).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.CURRENT_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.CURRENT_LIABILITY).add(difference));
            } else if (EdsAccountType.LONG_TERM_LIABILITY.equals(account.getAccountType().getCode())) {
                balanceSheetMap.get(AccountingConstants.LONG_TERM_LIABILITY).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.LONG_TERM_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.LONG_TERM_LIABILITY).add(difference));
            } else {
                balanceSheetMap.get(AccountingConstants.LIABILITY).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.LIABILITY, balanceSheetTotalMap.get(AccountingConstants.LIABILITY).add(difference));
            }
            balanceSheetTotalMap.put(AccountingConstants.TOTAL_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.TOTAL_LIABILITY).add(difference));
        }

        BigDecimal retainedEarnings = BigDecimal.ZERO;
        BigDecimal currentYearEarnings = BigDecimal.ZERO;

        for (Object[] obj : revenueTransItems) {
            retainedEarnings = retainedEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
        }
        for (Object[] obj : expenseTransItems) {
            retainedEarnings = retainedEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
        }
        for (Object[] obj : currentRevenueTransItems) {
            currentYearEarnings = currentYearEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
        }
        for (Object[] obj : currentExpenseTransItems) {
            currentYearEarnings = currentYearEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
        }

        TotalDebitCredit foreignRetainedGainAndLoss = calculateBankAccountsGainLoss(fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID, currencyId, financialSettings.getExchangeRateScale());
        retainedEarnings = retainedEarnings.subtract(foreignRetainedGainAndLoss.getRealDebitCreditDiff()).multiply(exchangeRate);

        //Bank account gain and loss
        TotalDebitCredit foreignGainAndLoss = calculateBankAccountsGainLoss(financialStartDate, toDate, departmentAndTreeChildIDs, projectID, currencyId, financialSettings.getExchangeRateScale());
        currentYearEarnings = currentYearEarnings.subtract(foreignGainAndLoss.getRealDebitCreditDiff()).multiply(exchangeRate);

        balanceSheetMap.get(AccountingConstants.EQUITY).add(new BalanceSheetInnerItem(commonLocalizer.localize(PdfLocalizationName.currentYearEarnings, "Current Year Earnings"), currentYearEarnings, -1));

        balanceSheetTotalMap.put(AccountingConstants.EQUITY, balanceSheetTotalMap.get(AccountingConstants.EQUITY).add(currentYearEarnings));

        boolean found = false;
        for (Object[] obj : equityTransItems) {
            EdsAccount account = accountingManager.get((Integer) obj[2]);
            BigDecimal difference = getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false);
            difference = difference.multiply(exchangeRate);
            if (account.getKey() != null && account.getKey().equals(EdsAccount.RETAINED_EARNINGS)) {
                difference = difference.add(retainedEarnings);

                if (financialSettings.isIncludeCurrentYearEariningInRetained()) {
                    difference = difference.add(currentYearEarnings);
                }
                found = true;
            }
            balanceSheetMap.get(AccountingConstants.EQUITY).add(new BalanceSheetInnerItem(account.getName(), account.getAccountCode(), difference, account.getObjectID()));
            balanceSheetTotalMap.put(AccountingConstants.EQUITY, balanceSheetTotalMap.get(AccountingConstants.EQUITY).add(difference));
        }
        //Retained Earnings
        if (!found) {
            EdsAccount account = accountingManager.getAccountByKey(EdsAccount.RETAINED_EARNINGS);
            if (account != null) {
                BigDecimal difference = retainedEarnings;

                if (financialSettings.isIncludeCurrentYearEariningInRetained()) {
                    difference = difference.add(currentYearEarnings);
                }
                balanceSheetMap.get(AccountingConstants.EQUITY).add(new BalanceSheetInnerItem(commonLocalizer.localize("retainedEarnings", "Retained Earnings"), account.getAccountCode(), difference, account.getObjectID()));
                balanceSheetTotalMap.put(AccountingConstants.EQUITY, balanceSheetTotalMap.get(AccountingConstants.EQUITY).add(difference));
            }
        }
        balanceSheetTotalMap.put(AccountingConstants.TOTAL_LIABILITY, balanceSheetTotalMap.get(AccountingConstants.TOTAL_LIABILITY).add(balanceSheetTotalMap.get(AccountingConstants.EQUITY)));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getBalanceSheetReportSummary(Date fromDate, Date toDate, Map<String, BigDecimal> balanceSheetAssetTotalMap, Map<String, BigDecimal> balanceSheetLiabilityTotalMap,
            BigDecimal exchangeRate, String departmentAndTreeChildIDs, Integer currencyId, Integer projectID) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Date financialYearEnd = financialSettings.getFinancialYearEnd();
        Date financialStartDate = getBeginningOfAccountingPeriod(financialYearEnd, toDate);
        fromDate.setTime(0);//1970

        Date lastFinancialYearEnd = getEndingOfAccountingPeriod(financialYearEnd, toDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastFinancialYearEnd);
        calendar.add(Calendar.YEAR, -1);
        lastFinancialYearEnd = calendar.getTime();

        List<Object[]> assetTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.ASSETS, fromDate, toDate, departmentAndTreeChildIDs, projectID);
        List<Object[]> liabilityTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.LIABILITIES, fromDate, toDate, departmentAndTreeChildIDs, projectID);
        List<Object[]> equityTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EQUITY, fromDate, toDate, departmentAndTreeChildIDs, projectID);

        List<Object[]> revenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);
        List<Object[]> expenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID);

        List<Object[]> currentRevenueTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.REVENUE, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);
        List<Object[]> currentExpenseTransItems = transactionManager.getBalanceDataByDateAndAccountCategory(EdsAccountType.EXPENSES, financialStartDate, toDate, departmentAndTreeChildIDs, projectID);

        exchangeRate = exchangeRate == null ? new BigDecimal(1) : exchangeRate;
        Integer baseCurrencyId = financialSettings.getCurrency().getObjectID();

        HashMap<Integer, BigDecimal> rateMap = new HashMap<>();
        for (Object[] obj : assetTransItems) {
            EdsAccount account = accountingManager.get((Integer) obj[2]);
            BigDecimal difference = getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true);
            if (account.isForeignAccount()) {
                Integer accountCurrencyID = account.getCurrency() != null ? account.getCurrency().getObjectID() : baseCurrencyId;
                if (!accountCurrencyID.equals(currencyId)) {
                    //exchange rate between bank account currency and base currency
                    BigDecimal bankCurrencyExchangeRate;

                    if (rateMap.get(accountCurrencyID) != null) {
                        bankCurrencyExchangeRate = rateMap.get(accountCurrencyID);
                    } else {
                        CurrencyListItem currencyItem = currencyService.getCurrencyRateByDate(accountCurrencyID, new DateNonConvertable(toDate));
                        bankCurrencyExchangeRate = currencyItem != null ? BigDecimal.valueOf(currencyItem.getExchangeRate()) : BigDecimal.ONE;
                        rateMap.put(accountCurrencyID, bankCurrencyExchangeRate);
                    }
                    //this line does bank account amount convert to base currency amount
                    difference = difference.divide(bankCurrencyExchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    difference = difference.multiply(exchangeRate);
                }
            } else {
                difference = difference.multiply(exchangeRate);
            }

            if (EdsAccountType.NON_CURRENT_ASSET.equals(account.getAccountType().getCode())) {
                if (balanceSheetAssetTotalMap.containsKey(AccountingConstants.NON_CURRENT_ASSET)) {
                    balanceSheetAssetTotalMap.put(AccountingConstants.NON_CURRENT_ASSET, balanceSheetAssetTotalMap.get(AccountingConstants.NON_CURRENT_ASSET).add(difference));
                } else {
                    balanceSheetAssetTotalMap.put(AccountingConstants.NON_CURRENT_ASSET, difference);
                }
            } else if (EdsAccountType.FIXED_ASSET.equals(account.getAccountType().getCode())) {
                if (balanceSheetAssetTotalMap.containsKey(AccountingConstants.FIXED_ASSET)) {
                    balanceSheetAssetTotalMap.put(AccountingConstants.FIXED_ASSET, balanceSheetAssetTotalMap.get(AccountingConstants.FIXED_ASSET).add(difference));
                } else {
                    balanceSheetAssetTotalMap.put(AccountingConstants.FIXED_ASSET, difference);
                }
            } else if (EdsAccountType.BANK.equals(account.getAccountType().getCode())) {
                if (balanceSheetAssetTotalMap.containsKey(AccountingConstants.BANK)) {
                    balanceSheetAssetTotalMap.put(AccountingConstants.BANK, balanceSheetAssetTotalMap.get(AccountingConstants.BANK).add(difference));
                } else {
                    balanceSheetAssetTotalMap.put(AccountingConstants.BANK, difference);
                }
            } else if (EdsAccountType.PREPAYMENT.equals(account.getAccountType().getCode())) {
                if (balanceSheetAssetTotalMap.containsKey(AccountingConstants.PREPAYMENT)) {
                    balanceSheetAssetTotalMap.put(AccountingConstants.PREPAYMENT, balanceSheetAssetTotalMap.get(AccountingConstants.PREPAYMENT).add(difference));
                } else {
                    balanceSheetAssetTotalMap.put(AccountingConstants.PREPAYMENT, difference);
                }
            } else {
                if (balanceSheetAssetTotalMap.containsKey(AccountingConstants.CURRENT_ASSET)) {
                    balanceSheetAssetTotalMap.put(AccountingConstants.CURRENT_ASSET, balanceSheetAssetTotalMap.get(AccountingConstants.CURRENT_ASSET).add(difference));
                } else {
                    balanceSheetAssetTotalMap.put(AccountingConstants.CURRENT_ASSET, difference);
                }
            }
            balanceSheetAssetTotalMap.put(AccountingConstants.ASSETS, balanceSheetAssetTotalMap.get(AccountingConstants.ASSETS).add(difference));
        }

        for (Object[] obj : liabilityTransItems) {
            EdsAccount account = accountingManager.get((Integer) obj[2]);
            BigDecimal difference = getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false);
            difference = difference.multiply(exchangeRate);

            if (EdsAccountType.CURRENT_LIABILITY.equals(account.getAccountType().getCode())) {
                if (balanceSheetLiabilityTotalMap.containsKey(AccountingConstants.CURRENT_LIABILITY)) {
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.CURRENT_LIABILITY, balanceSheetLiabilityTotalMap.get(AccountingConstants.CURRENT_LIABILITY).add(difference));
                } else {
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.CURRENT_LIABILITY, difference);
                }
            } else if (EdsAccountType.LONG_TERM_LIABILITY.equals(account.getAccountType().getCode())) {
                if (balanceSheetLiabilityTotalMap.containsKey(AccountingConstants.LONG_TERM_LIABILITY)) {
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.LONG_TERM_LIABILITY, balanceSheetLiabilityTotalMap.get(AccountingConstants.LONG_TERM_LIABILITY).add(difference));
                } else {
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.LONG_TERM_LIABILITY, difference);
                }
            } else {
                if (balanceSheetLiabilityTotalMap.containsKey(AccountingConstants.LIABILITY)) {
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.LIABILITY, balanceSheetLiabilityTotalMap.get(AccountingConstants.LIABILITY).add(difference));
                } else {
                    balanceSheetLiabilityTotalMap.put(AccountingConstants.LIABILITY, difference);
                }
            }
            balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(difference));
        }

        BigDecimal retainedEarnings = BigDecimal.ZERO;
        BigDecimal currentYearEarnings = BigDecimal.ZERO;

        for (Object[] obj : revenueTransItems) {
            retainedEarnings = retainedEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
        }
        for (Object[] obj : expenseTransItems) {
            retainedEarnings = retainedEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
        }
        for (Object[] obj : currentRevenueTransItems) {
            currentYearEarnings = currentYearEarnings.add(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false));
        }
        for (Object[] obj : currentExpenseTransItems) {
            currentYearEarnings = currentYearEarnings.subtract(getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], true));
        }

        TotalDebitCredit foreignRetainedGainAndLoss = calculateBankAccountsGainLoss(fromDate, lastFinancialYearEnd, departmentAndTreeChildIDs, projectID, currencyId, financialSettings.getExchangeRateScale());
        retainedEarnings = retainedEarnings.subtract(foreignRetainedGainAndLoss.getRealDebitCreditDiff()).multiply(exchangeRate);

        //Bank account gain and loss
        TotalDebitCredit foreignGainAndLoss = calculateBankAccountsGainLoss(financialStartDate, toDate, departmentAndTreeChildIDs, projectID, currencyId, financialSettings.getExchangeRateScale());
        currentYearEarnings = currentYearEarnings.subtract(foreignGainAndLoss.getRealDebitCreditDiff()).multiply(exchangeRate);

        balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY, currentYearEarnings);
        balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(currentYearEarnings));

        boolean found = false;
        for (Object[] obj : equityTransItems) {
            EdsAccount account = accountingManager.get((Integer) obj[2]);
            BigDecimal difference = getDifference((BigDecimal) obj[0], (BigDecimal) obj[1], false);
            difference = difference.multiply(exchangeRate);
            account = getAccountParent(account, account.getParent());

            if (account.getKey() != null && account.getKey().equals(EdsAccount.RETAINED_EARNINGS)) {
                difference = difference.add(retainedEarnings);

                if (financialSettings.isIncludeCurrentYearEariningInRetained()) {
                    difference = difference.add(currentYearEarnings);
                }
                found = true;
            }
            balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY).add(difference));
            balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(difference));
        }
        //Retained Earnings
        if (!found) {
            EdsAccount account = accountingManager.getAccountByKey(EdsAccount.RETAINED_EARNINGS);
            if (account != null) {
                BigDecimal difference = retainedEarnings;

                if (financialSettings.isIncludeCurrentYearEariningInRetained()) {
                    difference = difference.add(currentYearEarnings);
                }
                balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY).add(difference));
                balanceSheetLiabilityTotalMap.put(AccountingConstants.EQUITY_LIABILITIES, balanceSheetLiabilityTotalMap.get(AccountingConstants.EQUITY_LIABILITIES).add(difference));
            }
        }
    }

    public Date getBeginningOfAccountingPeriod(Date financialEndDate, Date date) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        //Beginning of Accounting Period
        calendar1.setTime(financialEndDate);
        calendar1.add(Calendar.DAY_OF_MONTH, +1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar1.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));

        if (calendar1.getTime().after(date)) {
            calendar1.add(Calendar.YEAR, -1);
        }
        return calendar1.getTime();
    }

    public Date getEndingOfAccountingPeriod(Date financialEndDate, Date date) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        //Ending of Accounting Period
        calendar1.setTime(financialEndDate);
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        calendar1.set(Calendar.MILLISECOND, 999);
        calendar1.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));

        if (calendar1.getTime().before(date)) {
            calendar1.add(Calendar.YEAR, +1);
        }
        return calendar1.getTime();
    }

    private EdsAccount getAccountParent(EdsAccount account, EdsAccount parent) {
        if (parent == null || account.equals(parent)) {
            return account;
        }
        if (parent.getParent() != null) {
            return getAccountParent(account, parent.getParent());
        }
        return parent;
    }

    @Override
    public BudgetManagerItems getProfitAndLossReport(PnLFilter filter) {
        //there can be account transaction data to be fetched, as an optimization measure
        List<EdsAccount> expRevAccounts = accountingManager.getRevenueExpensesAttendedInTransactions(filter.getMain(), filter.getCompareTo(), filter.getDepartmentAndTreeChildIDs(), filter.getProjectID(), filter.getSortField(), filter.getSortDirection());//obtaining accounts
        LinkedHashSet<EdsAccount> accountsHash = new LinkedHashSet<>(expRevAccounts);

        //configure template of the result
        BudgetManagerItems budgetManagerItems = new BudgetManagerItems();
        ArrayList<BudgetInDate> grossProfit = AccountItemWithBudgetDate.createAndSetRowCellsData(filter.getMain(), filter.getCompareTo());
        ArrayList<BudgetInDate> netProfitBeforeIncomeTax = AccountItemWithBudgetDate.createAndSetRowCellsData(filter.getMain(), filter.getCompareTo());
        ArrayList<BudgetInDate> netProfit = AccountItemWithBudgetDate.createAndSetRowCellsData(filter.getMain(), filter.getCompareTo());

        //mapping PnL categories
        Map<String, PNLCalculation> mapAccountTypeByPNLCalculation = getMapAccountTypeByPNLCalculation(filter.getMain(), filter.getCompareTo(), budgetManagerItems, grossProfit, netProfitBeforeIncomeTax, netProfit);

        Map<EdsAccount, List<BudgetInDate>> showBudgetDataAsMap = null;

        //budget list that could be compared with PnL
        if (filter.isShowBudget()) {
            List<EdsAccountBudget> budgetedAccount = accountBudgetManager.findBudgetedAccountInTheRange(filter.getMain().getFrom().getNonConvertedDate(), filter.getMain().getTo().getNonConvertedDate(), filter.getDepartmentAndTreeChildIDs(), true);
            accountsHash.addAll(budgetedAccount.stream().map(EdsAccountBudget::getAccount).toList());
            showBudgetDataAsMap = putBudgetByAccount(budgetedAccount);
        }

        //income tax account logic if it was enabled
        Integer incomeTaxAccount = financialSettingsManager.getFinancialSettings().getIncomeTaxAccount();

        Map<String, HashMap<Integer, TotalDebitCredit>> totalCreditDebitMap = new HashMap<>();
        Map<String, BigDecimal> exchangeRateMap = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //main period debit/credit total of the chart of accounts
        HashMap<Integer, TotalDebitCredit> mainDebitCreditTotal = accountingManager.getDebitCreditTotalForPNL(filter.getDepartmentAndTreeChildIDs(), filter.getProjectID(), filter.getMain().getFrom().getNonConvertedDate(), filter.getMain().getTo().getNonConvertedDate());
        totalCreditDebitMap.put(dateFormat.format(filter.getMain().getTo().getNonConvertedDate()), mainDebitCreditTotal);

        //gain/loss account added to list of account
        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            EdsAccount gainAndLossAccount = accountingManager.getAccountByKey(EdsAccount.EXCHANGE_VARIANCE);
            TotalDebitCredit debitCredit = calculateBankAccountsGainLoss(filter.getMain().getFrom().getNonConvertedDate(), filter.getMain().getTo().getNonConvertedDate(), filter.getDepartmentAndTreeChildIDs(), filter.getProjectID(), filter.getCurrencyId(), filter.getExchangeRateScale());
            if ((debitCredit != null && (debitCredit.debit.compareTo(BigDecimal.ZERO) > 0 || debitCredit.credit.compareTo(BigDecimal.ZERO) > 0)) ||
                    totalCreditDebitMap.get(dateFormat.format(filter.getMain().getTo().getNonConvertedDate())).get(gainAndLossAccount.getObjectID()) != null) {
                accountsHash.add(gainAndLossAccount);
            }
        }

        for (FromToDate compareDate : filter.getCompareTo()) {
            CurrencyListItem item = currencyService.getCurrencyRateByDate(filter.getCurrencyId(), compareDate.getTo());
            exchangeRateMap.put(dateFormat.format(compareDate.getTo().getNonConvertedDate()), BigDecimal.valueOf(item.getExchangeRate()));

            //compared period debit/credit total of the chart of accounts
            HashMap<Integer, TotalDebitCredit> compareDebitCreditTotal = accountingManager.getDebitCreditTotalForPNL(filter.getDepartmentAndTreeChildIDs(), filter.getProjectID(), compareDate.getFrom().getNonConvertedDate(), compareDate.getTo().getNonConvertedDate());
            totalCreditDebitMap.put(dateFormat.format(compareDate.getTo().getNonConvertedDate()), compareDebitCreditTotal);
        }

        for (EdsAccount account : accountsHash) {
            if (account != null) {
                String accountType = account.getAccountType().getCode();

                if (incomeTaxAccount != null && incomeTaxAccount.equals(account.getObjectID())) {
                    mapAccountTypeByPNLCalculation.get(EdsAccountType.INCOME_TAX).calculate(filter, account, totalCreditDebitMap, showBudgetDataAsMap, exchangeRateMap);
                } else {
                    mapAccountTypeByPNLCalculation.get(accountType).calculate(filter, account, totalCreditDebitMap, showBudgetDataAsMap, exchangeRateMap);
                }
            }
        }

        budgetManagerItems.setGrossProfit(grossProfit);
        budgetManagerItems.setNetProfitBeforeIncomeTax(netProfitBeforeIncomeTax);
        budgetManagerItems.setNetProfit(netProfit);
        if (!expRevAccounts.isEmpty()) {
            ActBudVar grossVariance = new ActBudVar();
            grossVariance.setActual(grossProfit.isEmpty() ? BigDecimal.ZERO : grossProfit.get(0).getValue());
            ActBudVar netVarianceBeforeIncomeTax = new ActBudVar();
            ActBudVar netVariance = new ActBudVar();
            netVarianceBeforeIncomeTax.setActual(netProfitBeforeIncomeTax.isEmpty() ? BigDecimal.ZERO : netProfitBeforeIncomeTax.get(0).getValue());
            netVariance.setActual(netProfit.isEmpty() ? BigDecimal.ZERO : netProfit.get(0).getValue());
            calulateBudgetProfitInRange(budgetManagerItems, grossVariance, netVarianceBeforeIncomeTax, netVariance);
            budgetManagerItems.setGrossVariance(grossVariance);
            budgetManagerItems.setNetVarianceBeforeIncomeTax(netVarianceBeforeIncomeTax);
            budgetManagerItems.setNetVariance(netVariance);
        }
        return budgetManagerItems;
    }

    @Override
    public BudgetManagerItems getProfitAndLossSubsidiariesReport(PnLFilter filter) {

        return new BudgetManagerItems();

    }

    public abstract class PNLCalculation {
        AccountItemsByAccountType accountItemsByType;
        List<AccountItemWithBudgetDate> accountWithDate;
        ActBudVar actualBudgetedVarianceTotal;
        List<BudgetInDate> actualTotal;
        protected List<BudgetInDate> calculated;

        PNLCalculation(String name, FromToDate main, FromToDate[] compareTo) {
            accountItemsByType = new AccountItemsByAccountType(name);
            accountWithDate = new ArrayList<>();
            actualBudgetedVarianceTotal = new ActBudVar();
            actualTotal = AccountItemWithBudgetDate.createAndSetRowCellsData(main, compareTo);
            accountItemsByType.setAccountItems(accountWithDate);
            accountItemsByType.setTotalWithVariance(actualBudgetedVarianceTotal);
            accountItemsByType.setActualTotal(actualTotal);
            setAccountItems();
        }

        public void calculate(PnLFilter filter, EdsAccount account, Map<String, HashMap<Integer, TotalDebitCredit>> totalCreditDebitMap, Map<EdsAccount, List<BudgetInDate>> showBudgetDataAsMap, Map<String, BigDecimal> exchangeRateMap) {
            AccountItemWithBudgetDate aItem = calculatePNLRow(filter, account, totalCreditDebitMap, showBudgetDataAsMap, accountWithDate, exchangeRateMap);
            if (account.getParent() != null && !account.getParent().isDeleted()) {
                aItem.setParentId(account.getParent().getObjectID());
                aItem.setParentCode(account.getParent().getCodeString());
                aItem.setParentName(account.getParent().getName());
            }
            calculated = aItem.getRowCells();

            if (!filter.isConsolidation()) {

                if (aItem.getVariance() != null) {
                    verticalMergeBudgetOfPNL(actualBudgetedVarianceTotal, aItem.getVariance());
                }
                verticalMerge(actualTotal, calculated);
                setGrossAndNet();
            }
        }

        public abstract void setGrossAndNet();

        public abstract void setAccountItems();
    }

    private void verticalMergeBudgetOfPNL(ActBudVar total, ActBudVar addable) {
        total.setBudget(total.getBudget() == null ? addable.getBudget() : total.getBudget().add(addable.getBudget()));
        total.setActual(total.getActual() == null ? addable.getActual() : total.getActual().add(addable.getActual()));
    }

    private void calulateBudgetProfitInRange(BudgetManagerItems budgetManagerItems, ActBudVar grossVariance, ActBudVar netVarianceBeforeIncomeTax, ActBudVar netVariance) {
        BigDecimal revenue = calculateAccountGroupBudget(budgetManagerItems.getRevenue()),
                sale = calculateAccountGroupBudget(budgetManagerItems.getSale()),
                directCosts = calculateAccountGroupBudget(budgetManagerItems.getDirectCosts()),
                otherIncome = calculateAccountGroupBudget(budgetManagerItems.getOtherIncome()),
                expense = calculateAccountGroupBudget(budgetManagerItems.getExpense()),
                depreciation = calculateAccountGroupBudget(budgetManagerItems.getDepreciation()),
                overhead = calculateAccountGroupBudget(budgetManagerItems.getOverhead()),
                incomeTax = calculateAccountGroupBudget(budgetManagerItems.getIncomeTax());

        BigDecimal grossBudget = revenue.add(sale).subtract(directCosts).subtract(expense);
        BigDecimal netBudgetBeforeIncomeTax = grossBudget.add(otherIncome).subtract(overhead);
        BigDecimal netBudget = netBudgetBeforeIncomeTax.subtract(incomeTax);
        grossVariance.setBudget(grossBudget);
        netVarianceBeforeIncomeTax.setBudget(netBudgetBeforeIncomeTax);
        netVariance.setBudget(netBudget);
    }

    private BigDecimal calculateAccountGroupBudget(AccountItemsByAccountType group) {
        BigDecimal budget = BigDecimal.ZERO;
        if (group != null) {
            AccountItemWithBudgetDate[] accountItems = group.getAccountItems();
            if (accountItems != null) {
                for (AccountItemWithBudgetDate accData : accountItems) {
                        if (accData.getVariance() != null && accData.getVariance().getBudget() != null) {
                            budget = budget.add(accData.getVariance().getBudget());
                        }
                }
            }
        }
        return budget;
    }

    private void verticalMerge(List<BudgetInDate> result, List<BudgetInDate> budgetInOneAccount) {
        if (budgetInOneAccount == null) {
            return;
        }
        for (int i = 0; i < budgetInOneAccount.size(); i++) {
            if (budgetInOneAccount.get(i).getValue() != null) {
                result.get(i).setValue((result.get(i).getValue() != null ? result.get(i).getValue() : BigDecimal.ZERO).add(budgetInOneAccount.get(i).getValue()));
            }
        }
    }

    private void verticalMergeMinus(List<BudgetInDate> result, List<BudgetInDate> budgetInOneAccount) {
        if (budgetInOneAccount == null) {
            return;
        }
        for (int i = 0; i < budgetInOneAccount.size(); i++) {
            if (budgetInOneAccount.get(i).getValue() != null) {
                result.get(i).setValue((result.get(i).getValue() != null ? result.get(i).getValue() : BigDecimal.ZERO).subtract(budgetInOneAccount.get(i).getValue()));
            }
        }
    }

    //    @SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument"})
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccountItemWithBudgetDate calculatePNLRow(PnLFilter filter, EdsAccount account, Map<String, HashMap<Integer, TotalDebitCredit>> totalCreditDebitMap, Map<EdsAccount, List<BudgetInDate>> showBudgetDataAsMap, List<AccountItemWithBudgetDate> accountGroupTableData, Map<String, BigDecimal> exchangeRateMap) {
        AccountItemWithBudgetDate rowData = null, interCompanyRowData = null;

        if (filter.isConsolidation()) {
            for (AccountItemWithBudgetDate accItem : accountGroupTableData) {
                if (accItem.getName().equals(account.getName()) && accItem.getAccountTypeID().equals(account.getAccountType().getObjectID())) {
                    rowData = accItem;
                }
                if (accItem.getName().equals(account.getName() + " Intercompany") && accItem.getAccountTypeID().equals(account.getAccountType().getObjectID())) {
                    interCompanyRowData = accItem;
                }
            }
            if (rowData == null) {
                rowData = account.createPNLAccountItem(filter.getMain(), filter.getCompareTo());
                accountGroupTableData.add(rowData);
            }
        } else {
            rowData = account.createPNLAccountItem(filter.getMain(), filter.getCompareTo());
            accountGroupTableData.add(rowData);
        }

        int colIndex = 0;
        TotalDebitCredit debCred;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.isConsolidation()) {
            debCred = account.getPNLConsolidationDebitCreditTotal(filter.getMain().getFrom().getNonConvertedDate(), filter.getMain().getTo().getNonConvertedDate(), exchangeRateMap, filter.isCompCurParentCompCurEquals());
            applyCellData(colIndex, debCred, rowData.getRowCells());

            if (debCred.isContainsInterCompany() && showBudgetDataAsMap == null) {

                if (interCompanyRowData == null) {
                    interCompanyRowData = account.createPNLAccountItem(filter.getMain(), filter.getCompareTo());
                    interCompanyRowData.setName(interCompanyRowData.getName() + " Intercompany");
                    interCompanyRowData.setRowCells(interCompanyRowData.getRowCells());
                    accountGroupTableData.add(interCompanyRowData);
                }
                applyInterCompanyCellData(colIndex, debCred, interCompanyRowData.getRowCells());
            }
        } else {
//            debCred = accountingManager.getDebitCreditTotal(account, departmentAndTreeChildIDs, projectID, main.getFrom().getNonConvertedDate(), main.getTo().getNonConvertedDate());
            debCred = totalCreditDebitMap.get(dateFormat.format(filter.getMain().getTo().getNonConvertedDate())).get(account.getObjectID());

            if (debCred == null) {
                debCred = new TotalDebitCredit();
                debCred.setAccountTypeCode(account.getAccountType().getCode());
                debCred.debit = BigDecimal.ZERO;
                debCred.credit = BigDecimal.ZERO;
            }
            //Floating account is Exchange gain/loss account
            if (account instanceof EdsFloatingAccount) {
                debCred.add(calculateBankAccountsGainLoss(filter.getMain().getFrom().getNonConvertedDate(), filter.getMain().getTo().getNonConvertedDate(), filter.getDepartmentAndTreeChildIDs(), filter.getProjectID(), filter.getCurrencyId(), filter.getExchangeRateScale()));
            }
            debCred.multiply(filter.getExchangeRate());

            if (account instanceof EdsFloatingAccount && debCred.getRealDebitCreditDiff().compareTo(BigDecimal.ZERO) != 0) {
                debCred.setContainsTransaction(true);
            }
            applyCellData(colIndex, debCred, rowData.getRowCells());
        }

        //works when selected compared periods
        if (showBudgetDataAsMap == null) {   //for simple version
            for (FromToDate compareDate : filter.getCompareTo()) {
                colIndex++;

                if (filter.isConsolidation()) {
                    debCred = account.getPNLConsolidationDebitCreditTotal(compareDate.getFrom().getNonConvertedDate(), compareDate.getTo().getNonConvertedDate(), exchangeRateMap, filter.isCompCurParentCompCurEquals());
                    applyCellData(colIndex, debCred, rowData.getRowCells());

                    if (debCred.isContainsInterCompany()) {

                        if (interCompanyRowData == null) {
                            interCompanyRowData = account.createPNLAccountItem(filter.getMain(), filter.getCompareTo());
                            interCompanyRowData.setName(interCompanyRowData.getName() + " Intercompany");
                            interCompanyRowData.setRowCells(interCompanyRowData.getRowCells());
                            accountGroupTableData.add(interCompanyRowData);
                        }
                        applyInterCompanyCellData(colIndex, debCred, interCompanyRowData.getRowCells());
                    }
                } else {
//                    debCred = account.getDebitCreditTotal(compareDate.getFrom().getNonConvertedDate(), compareDate.getTo().getNonConvertedDate());
                    debCred = totalCreditDebitMap.get(dateFormat.format(compareDate.getTo().getNonConvertedDate())).get(account.getObjectID());

                    if (debCred == null) {
                        debCred = new TotalDebitCredit();
                        debCred.setAccountTypeCode(account.getAccountType().getCode());
                        debCred.debit = BigDecimal.ZERO;
                        debCred.credit = BigDecimal.ZERO;
                    }
                    //Floating account is Exchange gain/loss account
                    if (account instanceof EdsFloatingAccount) {
                        debCred.add(calculateBankAccountsGainLoss(compareDate.getFrom().getNonConvertedDate(), compareDate.getTo().getNonConvertedDate(), filter.getDepartmentAndTreeChildIDs(), filter.getProjectID(), filter.getCurrencyId(), filter.getExchangeRateScale()));

                        if (debCred.getRealDebitCreditDiff().compareTo(BigDecimal.ZERO) != 0) {
                            debCred.setContainsTransaction(true);
                        }
                    }
                    BigDecimal toDateExchangeRate;
                    String getToField = dateFormat.format(compareDate.getTo().getDate());

                    if (exchangeRateMap != null && exchangeRateMap.get(getToField) != null) {
                        toDateExchangeRate = exchangeRateMap.get(getToField);
                    } else {
                        toDateExchangeRate = BigDecimal.valueOf(currencyService.getCurrencyRateByDate(filter.getCurrencyId(), compareDate.getTo()).getExchangeRate());
                    }
                    filter.setExchangeRate(toDateExchangeRate.setScale(filter.getExchangeRateScale(), RoundingMode.HALF_UP));

                    debCred.multiply(filter.getExchangeRate());

                    applyCellData(colIndex, debCred, rowData.getRowCells());
                }
            }
        } else {   //for with budget variance option
            ActBudVar actBudVar = new ActBudVar();
            BigDecimal totalValue = BigDecimal.ZERO;

            if (showBudgetDataAsMap.get(account) != null) {
                for (BudgetInDate bd : showBudgetDataAsMap.get(account)) {
                    totalValue = totalValue.add(bd.getValue().multiply(filter.getExchangeRate()));
                }
            }
            actBudVar.setBudget(totalValue);
            actBudVar.setActual(debCred.getDebitCreditDiff());
            rowData.setVariance(actBudVar);
        }
        return rowData;
    }

    private void applyCellData(int index, TotalDebitCredit debCred, ArrayList<BudgetInDate> rowCells) {
        if (debCred.isContainsTransaction()) {
            rowCells.get(index).setValue((rowCells.get(index).getValue() != null ? rowCells.get(index).getValue() : BigDecimal.ZERO).add(debCred.getPNLRealDifference()));
        }
    }

    private void applyInterCompanyCellData(int index, TotalDebitCredit debCred, ArrayList<BudgetInDate> rowCells) {
        rowCells.get(index).setValue((rowCells.get(index).getValue() != null ? rowCells.get(index).getValue() : BigDecimal.ZERO).add(debCred.getPNLInterCompanyRealDifference()));
    }

    private Map<String, PNLCalculation> getMapAccountTypeByPNLCalculation(FromToDate main, FromToDate[] compareTo, final BudgetManagerItems budgetManagerItems,
            final List<BudgetInDate> grossProfit, final List<BudgetInDate> netProfitBeforeIncomeTax,
            final List<BudgetInDate> netProfit) {
        Map<String, PNLCalculation> typeByCalculation = new HashMap<>();
        typeByCalculation.put(EdsAccountType.REVENUE, new PNLCalculation(commonLocalizer.localize("revenue", "Revenue"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setRevenue(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMerge(grossProfit, calculated);// +++++++
                verticalMerge(netProfitBeforeIncomeTax, calculated);  //++++++
                verticalMerge(netProfit, calculated);  //++++++
            }
        });
        typeByCalculation.put(EdsAccountType.SALES, new PNLCalculation(commonLocalizer.localize("sales", "Sales"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setSale(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMerge(grossProfit, calculated);// +++++++
                verticalMerge(netProfitBeforeIncomeTax, calculated);  // +++++++
                verticalMerge(netProfit, calculated);  // +++++++
            }
        });
        typeByCalculation.put(EdsAccountType.COST_OF_SALES, new PNLCalculation(commonLocalizer.localize("costOfSales", "Cost of Sales"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setExpense(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMergeMinus(grossProfit, calculated);//-----------
                verticalMergeMinus(netProfitBeforeIncomeTax, calculated);//-----------
                verticalMergeMinus(netProfit, calculated);//-----------
            }
        });

        typeByCalculation.put(EdsAccountType.DIRECT_EXPENSES, new PNLCalculation(commonLocalizer.localize("directExpenses", "Direct Expenses"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setDirectCosts(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMergeMinus(grossProfit, calculated);// -------
                verticalMergeMinus(netProfitBeforeIncomeTax, calculated);// -------
                verticalMergeMinus(netProfit, calculated);// -------
            }
        });
        typeByCalculation.put(EdsAccountType.DEPRECIATION, new PNLCalculation(commonLocalizer.localize("depreciation", "Depreciation"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setDepreciation(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMergeMinus(netProfitBeforeIncomeTax, calculated);//------------
                verticalMergeMinus(netProfit, calculated);//------------
            }
        });

        typeByCalculation.put(EdsAccountType.OTHER_INCOME, new PNLCalculation(commonLocalizer.localize("otherIncomes", "Other Income"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setOtherIncome(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMerge(netProfitBeforeIncomeTax, calculated);//+++++++++
                verticalMerge(netProfit, calculated);//+++++++++
            }
        });

        typeByCalculation.put(EdsAccountType.OVERHEAD, new PNLCalculation(commonLocalizer.localize("overhead", "Overhead"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setOverhead(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMergeMinus(netProfitBeforeIncomeTax, calculated);//-----------
                verticalMergeMinus(netProfit, calculated);//-----------
            }
        });
        typeByCalculation.put(EdsAccountType.INCOME_TAX, new PNLCalculation(commonLocalizer.localize("incomeTax", "Income Tax"), main, compareTo) {
            public void setAccountItems() {
                budgetManagerItems.setIncomeTax(accountItemsByType);
            }

            public void setGrossAndNet() {
                verticalMergeMinus(netProfit, calculated);//+++++++++
            }
        });
        return typeByCalculation;
    }

    /**
     * Calculate bank accounts gain and loss
     * in base currency
     */
    public TotalDebitCredit calculateBankAccountsGainLoss(Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID, Integer currencyId, Integer exchangeRateScale) {
        TotalDebitCredit foreignCurrencyGainLoss = new TotalDebitCredit();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer baseCurrencyId = financialSettings.getCurrency().getObjectID();
        currencyId = currencyId == null ? baseCurrencyId : currencyId;

        Map<Integer, TotalDebitCredit> beginningBalanceMap = accountingManager.getForeignAccountsDebitCreditTotal(departmentAndTreeChildIDs, projectID, null, fromDate, null);
        Map<Integer, TotalDebitCredit> balanceMap = accountingManager.getForeignAccountsDebitCreditTotal(departmentAndTreeChildIDs, projectID, fromDate, toDate, null);
        List<EdsAccount> foreignAccounts = accountingManager.getAccountsAttendedInTransactions(toDate, departmentAndTreeChildIDs, projectID, ALL_ACCOUNTS, true);

        //exchange rate for the current period
        CurrencyListItem item = currencyService.getCurrencyRateByDate(currencyId, new DateNonConvertable(ServerUtils.getStartDate(new Date())));
        BigDecimal exchangeRate = BigDecimal.valueOf(item.getExchangeRate()).setScale(exchangeRateScale, RoundingMode.HALF_UP);

        //exchange rate for the previous period

        for (EdsAccount account : foreignAccounts) {

            if (account.getCurrency() == null || baseCurrencyId.equals(account.getCurrency().getObjectID())) {
                continue;
            }

            TotalDebitCredit dcTotalPeriod = new TotalDebitCredit();
            BigDecimal balanceDiff;

            if (balanceMap.get(account.getObjectID()) != null) {
                balanceDiff = calculateGainLossBalance(dcTotalPeriod, balanceMap.get(account.getObjectID()), currencyId, baseCurrencyId, exchangeRate, exchangeRateScale);

                if (balanceDiff.compareTo(BigDecimal.ZERO) > 0) {
                    foreignCurrencyGainLoss.credit = foreignCurrencyGainLoss.credit.add(balanceDiff);
                } else {
                    foreignCurrencyGainLoss.debit = foreignCurrencyGainLoss.debit.add(balanceDiff.abs());
                }
            }
        }

        return foreignCurrencyGainLoss;
    }

    /**
     * Maps the budget list(of dates) by account
     * List should by sorted by account id  column
     *
     * @param budgets list should be sorted by "budget.account and budgt.date"
     * @return the map
     */
    private Map<EdsAccount, List<BudgetInDate>> putBudgetByAccount(List<EdsAccountBudget> budgets) {
        Map<EdsAccount, List<BudgetInDate>> map = new HashMap<>();
        List<BudgetInDate> accountBudgetList = null;
        EdsAccount account = null;
        for (int i = 0; i < budgets.size(); i++) {
            EdsAccountBudget ab = budgets.get(i);
            Integer budgetProfitID = ab.getBudgetedProfit() != null ? ab.getBudgetedProfit().getObjectID() : null;
            Date budgetDate = new Date(ab.getDate().getTime());
            BudgetInDate bIndate = new BudgetInDate(ab.getObjectID(), budgetProfitID, budgetDate, ab.getBudget());
            if ((!ab.getAccount().equals(account))) {
                accountBudgetList = new ArrayList<>();
                map.put(ab.getAccount(), accountBudgetList);
            }
            account = ab.getAccount();
            accountBudgetList.add(bIndate);
            if (i == budgets.size() - 1) {
                map.put(account, accountBudgetList);
            }
        }
        return map;
    }

    private void calculateFloatingAccount(LinkedHashMap<String, LinkedList<TrialBalanceItem>> map, EdsAccount account, TrialBalanceItem tbi, byte sidePeriod) {
        EdsFloatingAccount fAccount = (EdsFloatingAccount) account;
        if (sidePeriod == EdsAccount.DEBIT) {
            account.setAccountType(fAccount.getDebitType());
            map.get(fAccount.getDebitType().getCategory()).add(tbi);
        } else if (sidePeriod == EdsAccount.CREDIT) {
            account.setAccountType(fAccount.getCreditType());
            map.get(fAccount.getCreditType().getCategory()).add(tbi);
        }
    }


    private void calculateRetainedEarningsAccount(TotalDebitCredit reTotalDebitCredit, TotalDebitCredit dcTotalBeginningBalance) {
        reTotalDebitCredit.credit = reTotalDebitCredit.credit.add(dcTotalBeginningBalance.getRealDebitCreditDiff());
        dcTotalBeginningBalance.debit = BigDecimal.ZERO;
        dcTotalBeginningBalance.credit = BigDecimal.ZERO;
    }

    private void calculateRetainedEarningsForTrialBalance(TotalDebitCredit reTotalDebitCredit, LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList, Map<String, BigDecimal> trailBalanceTotalMap) {
        EdsAccount account = accountingManager.getAccountByKey(EdsAccount.RETAINED_EARNINGS);
        if (account != null) {
            TrialBalanceItem tbi = new TrialBalanceItem(account.getObjectID(), account.getName());
            tbi.setCode(account.getAccountCode());
            tbi.setCategoryCode(account.getAccountType().getCode());
            tbi.setBeginningCredit(reTotalDebitCredit.credit);
            tbi.setBeginningDebit(reTotalDebitCredit.debit);
            tbi.setBeginningBalance(tbi.getBeginningDebit().subtract(tbi.getBeginningCredit()));
            tbi.setCredit(reTotalDebitCredit.creditCurrentPeriod);
            tbi.setDebit(reTotalDebitCredit.debitCurrentPeriod);
            tbi.setEndingCredit(tbi.getCredit().add(tbi.getBeginningCredit()));
            tbi.setEndingDebit(tbi.getDebit().add(tbi.getBeginningDebit()));
            tbi.setEndingBalance(tbi.getEndingDebit().subtract(tbi.getEndingCredit()));
            List<TrialBalanceItem> list = mapAccountTypeByList.computeIfAbsent(account.getAccountType().getCategory(), k -> new LinkedList<>());

            if (list.contains(tbi)) {
                TrialBalanceItem item = list.get(list.indexOf(tbi));
                item.setBeginningCredit(item.getBeginningCredit().add(tbi.getBeginningCredit()));
                item.setBeginningDebit(item.getBeginningDebit().add(tbi.getBeginningDebit()));
                item.setBeginningBalance(item.getBeginningBalance().add(tbi.getBeginningBalance()));
                item.setCredit(item.getCredit().add(tbi.getCredit()));
                item.setDebit(item.getDebit().add(tbi.getDebit()));
                item.setEndingCredit(item.getEndingCredit().add(tbi.getCredit()));
                item.setEndingDebit(item.getEndingDebit().add(tbi.getDebit()));
                item.setEndingBalance(item.getEndingBalance().add(tbi.getEndingBalance()));
            } else {
                list.add(tbi);
            }
            trailBalanceTotalMap.put("totalBeginningDebit", trailBalanceTotalMap.get("totalBeginningDebit").add(tbi.getBeginningDebit()));
            trailBalanceTotalMap.put("totalBeginningCredit", trailBalanceTotalMap.get("totalBeginningCredit").add(tbi.getBeginningCredit()));
            trailBalanceTotalMap.put("totalBeginningBalance", trailBalanceTotalMap.get("totalBeginningBalance").add(tbi.getBeginningBalance()));

            trailBalanceTotalMap.put("totalDebit", trailBalanceTotalMap.get("totalDebit").add(tbi.getDebit().subtract(reTotalDebitCredit.debitCurrentPeriod)));
            trailBalanceTotalMap.put("totalCredit", trailBalanceTotalMap.get("totalCredit").add(tbi.getCredit().subtract(reTotalDebitCredit.creditCurrentPeriod)));

            trailBalanceTotalMap.put("totalEndingDebit", trailBalanceTotalMap.get("totalEndingDebit").add(tbi.getEndingDebit().subtract(reTotalDebitCredit.debitCurrentPeriod)));
            trailBalanceTotalMap.put("totalEndingCredit", trailBalanceTotalMap.get("totalEndingCredit").add(tbi.getEndingCredit().subtract(reTotalDebitCredit.creditCurrentPeriod)));
            trailBalanceTotalMap.put("totalEndingBalance", trailBalanceTotalMap.get("totalEndingBalance").add(tbi.getEndingBalance().subtract(reTotalDebitCredit.debitCurrentPeriod.subtract(reTotalDebitCredit.creditCurrentPeriod))));
        }
    }

    private BigDecimal getDifference(BigDecimal debit, BigDecimal credit, boolean isDebit) {
        if (debit == null) {
            debit = AccountingConstants.ZERO;
        }
        if (credit == null) {
            credit = AccountingConstants.ZERO;
        }
        return isDebit ? debit.subtract(credit) : credit.subtract(debit);
    }
}
