package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/12/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TermsAndDueProvider {

    void setDueDateAndTermsLabel(String text);

    Date getInvoiceDate();

    void applyPaymentInstructionData();

    boolean isEditForm();
}
