package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsCustomFormCustomFields;
import com.edatasite.workforce.gwt.core.server.db.CustomFormCFManager;
import org.springframework.stereotype.Repository;

@Repository
public class CustomFormCFManagerImpl extends BaseManager<EdsCustomFormCustomFields> implements CustomFormCFManager {

    public CustomFormCFManagerImpl() {
        super(EdsCustomFormCustomFields.class);
    }
}
