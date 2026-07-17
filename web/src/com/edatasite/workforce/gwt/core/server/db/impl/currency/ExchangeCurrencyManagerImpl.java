package com.edatasite.workforce.gwt.core.server.db.impl.currency;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.currency.EdsExchangeCurrency;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 08.01.16
 * Time: 15:08:00
 * To change this template use File | Settings | File Templates.
 */
@Repository("exchangeCurrencyManager")
public class ExchangeCurrencyManagerImpl extends BaseManager<EdsExchangeCurrency> implements ExchangeCurrencyManager {
    public ExchangeCurrencyManagerImpl() {
        super(EdsExchangeCurrency.class);
    }

    public List<EdsCurrency> getCurrencyList() {
        return (List<EdsCurrency>) find("select ec.currency from EdsExchangeCurrency ec where ec.deleted is not true  and ec.currency.name is not null and ec.currency.objectID not in (select fs.currency.objectID from EdsFinancialSettings fs ) order by ec.currency.name");
    }

    public EdsExchangeCurrency getCurrency(EdsCurrency currency) {
        return (EdsExchangeCurrency) findSingle("select ec from EdsExchangeCurrency ec where ec.deleted is not true and ec.currency = ?", currency);
    }

    public EdsExchangeCurrency getCurrencyById(Integer currencyId) {
        return (EdsExchangeCurrency) findSingle("select ec from EdsExchangeCurrency ec where ec.deleted is not true and ec.currency.objectID = ?", currencyId);
    }

    public List<EdsCurrency> getAvailableCurrencies() {
        return find("select c from EdsCurrency c where c.name is not null and c.objectID not in (select ec.currency.objectID from EdsExchangeCurrency ec where ec.deleted is not true)" +
                " order by c.name");
    }
}
