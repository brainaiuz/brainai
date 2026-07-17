package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.customfields.EdsInvoiceItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.accounting.InvoiceItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Normurod on 3/25/2017.
 */
@Repository("invoiceItemCFManager")
public class InvoiceItemCFManagerImpl extends BaseManager<EdsInvoiceItemCustomFields> implements InvoiceItemCFManager {

    public InvoiceItemCFManagerImpl() {
        super(EdsInvoiceItemCustomFields.class);
    }
}
