package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 17:14:50
 * To change this template use File | Settings | File Templates.
 */
public class SavedReceivableCreditNoteViewPDFHandler extends SavedCreditNoteViewPDFHandler {

    @Override
    protected boolean isClient() {
        return true;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.RECEIVABLE_CREDIT_NOTE;
    }

    @Override
    protected EdsCrmAccount getClientOrSupplier(Integer clientSupplierID) {
        return clientManager.get(clientSupplierID);
    }
}
