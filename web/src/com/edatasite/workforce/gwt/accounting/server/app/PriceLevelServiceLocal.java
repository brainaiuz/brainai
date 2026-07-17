package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created with IntelliJ IDEA.
 * User: Fathulla
 * Date: 09.12.14
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public interface PriceLevelServiceLocal {
    void setPriceLevelToClient(SelectItem[] priceLevelList, EdsCrmAccount client, Integer currencyId);
}
