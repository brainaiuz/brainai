package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.server.db.SupplierManager;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 11, 2009
 * Time: 3:20:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierManagerImpl extends BaseManager<EdsCrmAccount> implements SupplierManager {
    public SupplierManagerImpl() {
        super(EdsCrmAccount.class);
    }
}
