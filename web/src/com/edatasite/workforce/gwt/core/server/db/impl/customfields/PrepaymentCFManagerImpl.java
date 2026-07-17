package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsPrepaymentCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.PrepaymentCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("prepaymentCFManager")
public class PrepaymentCFManagerImpl extends BaseManager<EdsPrepaymentCustomFields> implements PrepaymentCFManager {
    public PrepaymentCFManagerImpl() {
        super(EdsPrepaymentCustomFields.class);
    }
}