package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.settings.EdsKPIContactDetails;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface KPIContactDetailsManager extends Manager<EdsKPIContactDetails> {
    EdsKPIContactDetails getKPIContactDetailsByCountry(EdsCountry country);
}
