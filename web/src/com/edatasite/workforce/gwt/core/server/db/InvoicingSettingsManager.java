package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;

public interface InvoicingSettingsManager extends Manager<EdsInvoicingSettings> {

    EdsInvoicingSettings getInvoiceSettings(EdsCompany company);

    EdsInvoicingSettings getInvoiceSettings();

    Integer getMasterCardPaymentAccountID(EdsCompany edsCompany);
}
