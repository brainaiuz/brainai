package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;

/**
 * Created by Sherzod on 7/8/2015.
 */
public class BatchPayBillViewPDFHandler extends BatchReceivePaymentViewPDFHandler{

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.BATCH_PAY_BILL;
    }
}
