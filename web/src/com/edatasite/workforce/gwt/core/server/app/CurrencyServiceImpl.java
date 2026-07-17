package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.currency.EdsExchangeCurrency;
import com.edatasite.workforce.core.domain.currency.EdsExchangeCurrencyRate;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.MultiCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyRateManager;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.CurrencyLayer;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Shohruh on 26 Mar 2016.
 */
@Transactional
@Service("currencyService")
public class CurrencyServiceImpl implements CurrencyService, CurrencyServiceLocal {

    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private MultiCurrencyManager multiCurrencyManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private ExchangeCurrencyRateManager exchangeCurrencyRateManager;
    @Autowired
    private ExchangeCurrencyManager exchangeCurrencyManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AccountingManager accountingManager;

    @Deprecated
    @Override
    public CurrencyItem[] getCurrencies() {
        EdsUser user = currencyManager.getUser();
        List<EdsCurrency> currencies;
        if (user != null && user.getCompany() != null && user.getCompany().getParentCompanyId() != null) {
            currencies = multiCurrencyManager.getSubsidiaryCurrencies();
        } else {
            return getCurrencies(true);
        }
        CurrencyItem[] items = new CurrencyItem[currencies.size()];
        int i = 0;
        for (EdsCurrency currency : currencies) {
            items[i] = currency.createCurrencyItem();
            i++;
        }
        return items;
    }

    public CurrencyItem getBaseCurrency() {
        return invoiceCircularResolver.getBaseCurrency();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem[] getCurrencies(boolean showUsed) {
        return getCurrencies(showUsed, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem[] getCurrencies(boolean showUsed, boolean withoutBaseCurrency) {
        List<EdsCurrency> currencies;
        List<CurrencyItem> items = new ArrayList<>();

        Integer baseCurrencyId = invoiceCircularResolver.getBaseCurrency().getId();
        EdsCurrency baseCurrency = currencyManager.getCurrency(baseCurrencyId);
        if (showUsed) {
            currencies = exchangeCurrencyManager.getCurrencyList();
            if (!withoutBaseCurrency) {
                items.add(createCurrencyItem(baseCurrency, baseCurrency));
            }
            for (EdsCurrency currency : currencies) {
                items.add(createCurrencyItem(currency, baseCurrency));
            }
        } else {
            currencies = exchangeCurrencyManager.getAvailableCurrencies();
            for (EdsCurrency currency : currencies) {
                if (currency.getObjectID() != baseCurrency.getObjectID()) {
                    items.add(createCurrencyItem(currency));
                }
            }
            items.add(createCurrencyItem(baseCurrency, baseCurrency));
        }
        return items.toArray(new CurrencyItem[0]);
    }

    @Override
    public CurrencyItem[] getEmployeeCurrencies(Integer employeeId, boolean ifBaseAll) {
        List<EdsCurrency> currencies;
        List<CurrencyItem> items = new ArrayList<>();

        Integer baseCurrencyId = invoiceCircularResolver.getBaseCurrency().getId();
        EdsCurrency baseCurrency = currencyManager.getCurrency(baseCurrencyId);
        EdsCurrency salaryCurrency = employeeManager.get(employeeId).getSalaryCurrency();
        if (ifBaseAll && salaryCurrency != null) {//expense claim
            items.add(createCurrencyItem(salaryCurrency, baseCurrency));
        }
        if (!ifBaseAll || !Objects.equals(baseCurrency, salaryCurrency)) {
            items.add(createCurrencyItem(baseCurrency, baseCurrency));
        }
        if (ifBaseAll && (salaryCurrency == null || salaryCurrency.equals(baseCurrency))) {//expense claim
            currencies = exchangeCurrencyManager.getCurrencyList();
            for (EdsCurrency currency : currencies) {
                items.add(createCurrencyItem(currency, baseCurrency));
            }
        }
        if (!ifBaseAll && salaryCurrency != null && !salaryCurrency.equals(baseCurrency)) {//cash advance
            items.add(createCurrencyItem(salaryCurrency, baseCurrency));
        }
        return items.toArray(new CurrencyItem[0]);
    }

    private CurrencyItem createCurrencyItem(EdsCurrency currency, EdsCurrency countryCurrency) {
        CurrencyItem item = currency.createCurrencyItem();
        item.setCompanyCurrency(currency.equals(countryCurrency));
        return item;
    }

    private CurrencyItem createCurrencyItem(EdsCurrency currency) {
        CurrencyItem item = currency.createCurrencyItem();
        item.setName(currency.getName() + " - " + currency.getFullName());
        return item;
    }

    @Override
    public void createOrUpdateCurrency(CurrencyListItem item) {
        EdsCurrency currency = currencyManager.getCurrency(item.getCurrency().getId());
        Integer baseCurrencyID = invoiceCircularResolver.getBaseCurrency().getId();
        EdsCurrency baseCurrency = currencyManager.getCurrency(baseCurrencyID);

        DateNonConvertable currentDateNC = new DateNonConvertable(new Date());
        currentDateNC.setInTimeZoneOffSetMs(item.getDate().getInTimeZoneOffSetMs());

        Date date = item.getDate().getNonConvertedDate();
        Date now = currentDateNC.getNonConvertedDate();

        String exchangeRateKey = getExchangeRateKey(baseCurrencyID, item.getCurrency().getId(), date.before(now) ? date : now);
        RedisClient.removeKey(exchangeRateKey);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        ServerUtils.setBeginningOfTheDay(calendar);
        date = calendar.getTime();

        createCurrency(item.getCurrency().getId(), item.isFixedRate() ? BigDecimal.valueOf(item.getExchangeRate()) : null);

        if (!item.isFromService() && !item.isFixedRate()) {
            exchangeCurrencyRateManager.registerExchangeRateHistory(baseCurrency, currency, BigDecimal.valueOf(item.getExchangeRate()), date, now);
        } else {
            exchangeCurrencyRateManager.deleteExchangeRateHistory(baseCurrency.getObjectID(), currency.getObjectID(), date);
        }
        accountingManager.getMultiCurrencyAccount(AccountingConstants.ACCOUNTS_PAYABLE_KEY, currency);
        accountingManager.getMultiCurrencyAccount(AccountingConstants.ACCOUNTS_RECEIVABLE_KEY, currency);
        accountingManager.getMultiCurrencyAccount(AccountingConstants.PREPAID_EXPANSES_KEY, currency);
        accountingManager.getMultiCurrencyAccount(AccountingConstants.UNEARNED_REVENUE_KEY, currency);
    }

    @Override
    public void createCurrency(Integer currencyID) {
        createCurrency(currencyID, null);
    }

    private EdsExchangeCurrency createCurrency(Integer currencyID, BigDecimal fixedRate) {
        EdsCurrency currency = currencyManager.get(currencyID);
        EdsExchangeCurrency exchangeCurrency = exchangeCurrencyManager.getCurrency(currency);
        if (exchangeCurrency == null) {
            exchangeCurrency = new EdsExchangeCurrency();
        }
        exchangeCurrency.setCurrency(currency);
        exchangeCurrency.setFixedRate(fixedRate);
        exchangeCurrency.setDeleted(false);
        exchangeCurrencyManager.createOrUpdate(exchangeCurrency);
        return exchangeCurrency;
    }

    @Override
    public void deleteCurrency(Integer currencyID) {
        EdsCurrency currency = currencyManager.getCurrency(currencyID);
        EdsExchangeCurrency exchangeCurrency = exchangeCurrencyManager.getCurrency(currency);
        if (exchangeCurrency != null) {
            exchangeCurrency.setDeleted(true);
            exchangeCurrencyManager.update(exchangeCurrency);
        }
    }

    @Override
    public CurrencyItem getCurrency(Integer currencyID) {
        if (currencyID == null) {
            return null;
        }
        EdsCurrency edsCurrency = currencyManager.getCurrency(currencyID);
        if (edsCurrency == null) {
            return null;
        }
        CurrencyItem currencyItem = new CurrencyItem();
        currencyItem.setId(edsCurrency.getObjectID());
        currencyItem.setName(edsCurrency.getName());
        currencyItem.setFullName(edsCurrency.getFullName());
        currencyItem.setSymbol(edsCurrency.getSymbol());
        return currencyItem;
    }

    @Override
    public ListResult<CurrencyListItem> getCurrencyRateList(DateNonConvertable date) {
        List<EdsCurrency> currencyList = exchangeCurrencyManager.getCurrencyList();
        int total = currencyList.size();
        ArrayList<CurrencyListItem> items = new ArrayList<>();
        for (EdsCurrency currency : currencyList) {
            CurrencyListItem item = getCurrencyRateByDate(currency.getObjectID(), date);
            items.add(item);
        }
        return new ListResult<>(items, total);
    }

    @Override
    public CurrencyListItem getCurrencyRateByDate(Integer currencyId, DateNonConvertable dateNC) {
        CurrencyListItem item = new CurrencyListItem();
        Integer baseCurrencyId = invoiceCircularResolver.getBaseCurrency().getId();
        currencyId = currencyId != null ? currencyId : baseCurrencyId;
        EdsCurrency baseCurrency = currencyManager.getCurrency(baseCurrencyId);
        EdsCurrency currency = currencyManager.getCurrency(currencyId);

        DateNonConvertable currentDateNC = new DateNonConvertable(new Date());
        currentDateNC.setInTimeZoneOffSetMs(dateNC.getInTimeZoneOffSetMs());

        Date date = dateNC.getNonConvertedDate();
        Date now = currentDateNC.getNonConvertedDate();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        ServerUtils.setBeginningOfTheDay(calendar);
        date = calendar.getTime();

        item.setBaseCurrency(baseCurrency.createCurrencyItem());
        item.setCurrency(currency.createCurrencyItem());
        item.setExRateInSum(getExchangeRateInSumm(currencyId));
        if (baseCurrencyId.equals(currencyId)) {
            item.setXeRate(1d);
            item.setExRate(1d);
            item.setXeUpdateTime(new DateNonConvertable(new Date()));
            item.setFixedRate(true);
            item.setFromService(false);
            return item;
        }

        String exchangeRateKey = getExchangeRateKey(baseCurrencyId, currencyId, date.before(now) ? date : now);

        if (RedisClient.getKey(exchangeRateKey) != null) {
            return RedisClient.getKey(exchangeRateKey, item.getClass());
        }

        EdsExchangeCurrency exchangeCurrency = exchangeCurrencyManager.getCurrency(currency);
        boolean fixedRate = false;
        boolean fromService = true;

        if (exchangeCurrency != null && exchangeCurrency.getFixedRate() != null) {
            item.setExRate(exchangeCurrency.getFixedRate().doubleValue());
            fixedRate = true;
            fromService = false;
        } else {
            EdsExchangeCurrencyRate exchangeCurrencyRate = exchangeCurrencyRateManager.getExchangeRateByDate(baseCurrencyId, currencyId, date);

            if (exchangeCurrencyRate != null) {
                item.setExRate(exchangeCurrencyRate.getExchangeRate().doubleValue());
                item.setDate(new DateNonConvertable(exchangeCurrencyRate.getDate()));
                item.setUpdateTime(new DateNonConvertable(exchangeCurrencyRate.getUpdateTime()));

                if (exchangeCurrencyRate.getDate().after(now) || ServerUtils.getDayCount(exchangeCurrencyRate.getDate(), now) == 0 || ServerUtils.getDayCount(exchangeCurrencyRate.getDate(), date) == 0) {
                    fromService = false;
                }
            }
        }

        CurrencyLayerItem currencyItem = getExchangeRateDouble(baseCurrency.getName(), currency.getName(), date.before(now) ? date : now, 0);
        if (currencyItem == null) {
            currencyItem = new CurrencyLayerItem(1d, new Date());
        }

        item.setXeRate(currencyItem.getRate());
        item.setXeUpdateTime(new DateNonConvertable(currencyItem.getLastUpdateTime()));
        item.setFixedRate(fixedRate);
        item.setFromService(fromService);

        RedisClient.setKey(exchangeRateKey, item, item.getClass());
        return item;
    }

    public CurrencyLayerItem getExchangeRateDouble(String from, String to, Date date, int attempt) {
        if (attempt >= 3 || from.equals(to)) {
            return new CurrencyLayerItem(1d, new Date());
        }
        CurrencyLayer currencyLayer = CurrencyLayer.getInstance();
        try {
            return currencyLayer.getExchangeRateDouble(from, to, date);
        } catch (Exception ex) {
            try {
                int millis = 300 * (++attempt);
                Thread.sleep(millis);
                return getExchangeRateDouble(from, to, date, attempt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new CurrencyLayerItem(1d, new Date());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem getCompanyBaseCurrency() {
        CurrencyItem currencyItem = null;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getCurrency() != null) {
            currencyItem = financialSettings.getCurrency().createCurrencyItem();
        }
        return currencyItem;
    }

    @Override
    public CurrencyItem[] getAccountCurrencyWithBase(Integer accountCurrencyId) {
        List<CurrencyItem> items = new ArrayList<>();

        CurrencyItem baseCurrency = invoiceCircularResolver.getBaseCurrency();
        baseCurrency.setCompanyCurrency(true);
        items.add(baseCurrency);
        EdsCurrency edsAccountCurrency = currencyManager.getCurrency(accountCurrencyId);
        if (!baseCurrency.getId().equals(accountCurrencyId)) {
            CurrencyItem accountCurrency = edsAccountCurrency.createCurrencyItem();
            accountCurrency.setCompanyCurrency(false);
            items.add(accountCurrency);
        }
        return items.toArray(new CurrencyItem[0]);
    }

    private String getExchangeRateKey(Integer baseCurrencyId, Integer currencyId, Date date) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(CacheConstants.EXCHANGE_RATE).append("_");

        if (ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
            keyBuilder.append(ServerSecurityContext.getInstance().getCompanyId()).append("_");
        }
        if (baseCurrencyId != null) {
            keyBuilder.append(baseCurrencyId).append("_");
        }
        if (currencyId != null) {
            keyBuilder.append(currencyId).append("_");
        }
        if (date != null) {
            keyBuilder.append(ServerUtils.getDateShortFormat(date));
        }

        return keyBuilder.toString();
    }

    public Double getExchangeRateInSumm(int currencyId) {
        EdsCurrency selectedCurrency = currencyManager.getCurrency(currencyId);
        Integer baseCurrencyId = invoiceCircularResolver.getBaseCurrency().getId();
        EdsExchangeCurrency base = exchangeCurrencyManager.getCurrencyById(baseCurrencyId);
        EdsExchangeCurrency selected = exchangeCurrencyManager.getCurrency(selectedCurrency);
        EdsExchangeCurrency sum = exchangeCurrencyManager.getCurrencyById(225);
        BigDecimal result = BigDecimal.ZERO;
        if (base != null && base.getFixedRate() != null &&
                selected != null && selected.getFixedRate() != null &&
                sum != null && sum.getFixedRate() != null) {
            result = (base.getFixedRate()).divide(selected.getFixedRate(), 9, RoundingMode.HALF_UP).multiply(sum.getFixedRate());
        }
        return result.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).doubleValue();
    }
}
