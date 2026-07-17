package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.InvoiceCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/28/11
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("invoiceCFManager")
public class InvoiceCFManagerImpl extends BaseManager<EdsInvoiceCustomFields> implements InvoiceCFManager {

    public InvoiceCFManagerImpl() {
        super(EdsInvoiceCustomFields.class);
    }
}
