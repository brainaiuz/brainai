package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 17:19:45
 * To change this template use File | Settings | File Templates.
 */
public class PayableCreditNoteViewPDFHandler extends CreditNoteViewPDFHandler {
    protected boolean isClient() {
        return false;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE;
    }

    @Override
    protected EdsCrmAccount getClientOrSupplier(Integer clientSupplierID) {
        return crmAccountManager.get(clientSupplierID);
    }
}
