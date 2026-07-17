package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsSickRequestCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.SickRequestCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Abror Abdukadirov
 * Date: 01.05.2017 20:01
 */
@Repository("sickRequestCFManager")
public class SickRequestCFManagerImpl extends BaseManager<EdsSickRequestCustomFields> implements SickRequestCFManager {

    public SickRequestCFManagerImpl() {
        super(EdsSickRequestCustomFields.class);
    }
}
