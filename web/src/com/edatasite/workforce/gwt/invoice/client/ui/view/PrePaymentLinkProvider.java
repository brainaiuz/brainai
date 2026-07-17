package com.edatasite.workforce.gwt.invoice.client.ui.view;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/24/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PrePaymentLinkProvider {
    BigDecimal getPaymentExRate();

    BigDecimal getDueAmount();

    void fireInvoicePaymentChange(String type);
}
