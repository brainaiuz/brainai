package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

@Repository("invoicingSettingsManager")
public class InvoicingSettingsManagerImpl extends BaseManager<EdsInvoicingSettings> implements InvoicingSettingsManager {

    public InvoicingSettingsManagerImpl() {
        super(EdsInvoicingSettings.class);
    }

    public EdsInvoicingSettings getInvoiceSettings(EdsCompany company) {
        if (company != null)
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());

        return (EdsInvoicingSettings) findSingle("select eis from EdsInvoicingSettings eis ");
    }

    public EdsInvoicingSettings getInvoiceSettings() {
        return (EdsInvoicingSettings) findSingle("select eis from EdsInvoicingSettings eis ");
    }

    @Override
    public Integer getMasterCardPaymentAccountID(EdsCompany company) {
        if (company != null)
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
        return (Integer)findSingle("select eis.masterCardPaymentAccount.objectID from EdsInvoicingSettings eis ");
    }
}
