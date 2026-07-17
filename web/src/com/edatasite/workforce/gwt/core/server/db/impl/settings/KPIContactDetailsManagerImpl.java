package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.settings.EdsKPIContactDetails;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.KPIContactDetailsManager;
import org.springframework.stereotype.Repository;

@Repository("kpiContactDetailsManager")
public class KPIContactDetailsManagerImpl extends BaseManager<EdsKPIContactDetails> implements KPIContactDetailsManager {
    public KPIContactDetailsManagerImpl() {
        super(EdsKPIContactDetails.class);
    }

    @Override
    public EdsKPIContactDetails getKPIContactDetailsByCountry(EdsCountry country) {
        if (country != null) {
            return (EdsKPIContactDetails) findSingle("select kcd from EdsKPIContactDetails kcd where kcd.countryCode=?", country.getCode());
        }
        return null;
    }
}
