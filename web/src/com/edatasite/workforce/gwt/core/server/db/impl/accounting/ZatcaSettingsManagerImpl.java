package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsZatcaSettings;
import com.edatasite.workforce.core.domain.enums.ZatcaState;
import com.edatasite.workforce.gwt.core.server.db.accounting.ZatcaSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("zatcaSettingsManager")
public class ZatcaSettingsManagerImpl extends BaseManager<EdsZatcaSettings> implements ZatcaSettingsManager {
    public ZatcaSettingsManagerImpl() {
        super(EdsZatcaSettings.class);
    }

    @Override
    public EdsZatcaSettings getZatcaSettings() {
        return (EdsZatcaSettings) findSingle("from EdsZatcaSettings zs where zs.state = '" + ZatcaState.ACTIVE + "'");
    }
}
