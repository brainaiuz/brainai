package com.edatasite.workforce.gwt.core.server.zatca;

import com.edatasite.workforce.gwt.core.server.zatca.service.errors.ZatcaException;

public interface ZatcaService {

    void initSettings() throws ZatcaException;

    void renewSettings() throws ZatcaException;

    String complianceInvoice(Integer invoiceID, String type) throws ZatcaException;

    String clearanceInvoice(Integer invoiceID, String type) throws ZatcaException;

    String getZatcaSettingsStatus();
}
