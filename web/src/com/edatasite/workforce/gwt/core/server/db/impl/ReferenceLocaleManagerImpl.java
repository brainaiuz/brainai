package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.gwt.core.server.db.ReferenceLocaleManager;
import org.springframework.stereotype.Repository;

@Repository("referenceLocaleManager")
public class ReferenceLocaleManagerImpl extends BaseManager<EdsReferenceLocale> implements ReferenceLocaleManager {
    public ReferenceLocaleManagerImpl() {
        super(EdsReferenceLocale.class);
    }
}
