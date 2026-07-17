package com.edatasite.workforce.gwt.accounting.server.app.costofgoods;

import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class COGSServices {
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    private final PluginRegistry<COGSService, String> registry;

    @Autowired
    public COGSServices(List<COGSService> services) {
        this.registry = OrderAwarePluginRegistry.create(services);
    }

    public COGSService getService(String type) {
        return registry.getPluginFor(type).orElse(getDefault());
    }

    public COGSService getService() {
        String type = financialSettingsManager.getFinancialSettings().getCostOfGoodsCalculationType();
        return type == null ? getDefault() : registry.getPluginFor(type).get();
    }

    private COGSService getDefault() {
        return registry.getPluginFor(COGSType.FIFO).get();
    }
}
