package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsExchangeRate;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExchangeRateManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 25/10/12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
@Repository("exchangeRateManager")
public class ExchangeRateManagerImpl extends BaseManager<EdsExchangeRate> implements ExchangeRateManager {
    public ExchangeRateManagerImpl() {
        super(EdsExchangeRate.class);
    }

    @Override
    public List<Object[]> getCurrencyExchangeRateByPeriod(Date date, String currencyIds) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        return findNative("SELECT cur.id,to_char(er.date,'dd') as month_of_day,er.exchangerate FROM " + getCompanyId() + ".exchangerate er \n " +
                " INNER JOIN " + getPublic() + ".currency cur ON cur.id=er.currencyid \n " +
                " WHERE to_char(er.date,'yyyy-mm')='" + dateFormat.format(date) + "' AND er.currencyid IN (" + currencyIds + ")");
    }

    @Override
    public Map<String, BigDecimal> getExchangeRatesByCurrency(Integer currencyId, Date startDate, Date endDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<EdsExchangeRate> edsExchangeRateList;
        if (startDate != null && endDate != null) {
            edsExchangeRateList = find("SELECT er FROM EdsExchangeRate er WHERE (er.date BETWEEN ? AND ?) AND er.currrency.objectID=? ORDER BY er.date", startDate, endDate, currencyId);
        } else if (endDate != null) {
            edsExchangeRateList = find("SELECT er FROM EdsExchangeRate er WHERE er.date <= ? AND er.currrency.objectID=? ORDER BY er.date", endDate, currencyId);
        } else {
            edsExchangeRateList = find("SELECT er FROM EdsExchangeRate er WHERE er.currrency.objectID=? ORDER BY er.date", currencyId);
        }
        Map<String, BigDecimal> exchangeRateMap = new HashMap<>();
        for (EdsExchangeRate edsExchangeRate : edsExchangeRateList) {
            exchangeRateMap.put(dateFormat.format(edsExchangeRate.getDate()), edsExchangeRate.getExchangeRate());
        }
        return exchangeRateMap;
    }
}

