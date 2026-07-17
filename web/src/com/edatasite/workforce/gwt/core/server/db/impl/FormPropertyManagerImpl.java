package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import org.springframework.stereotype.Repository;


@Repository("FormPropertyManager")
public class FormPropertyManagerImpl extends BaseManager<EdsFormProperty> implements FormPropertyManager {

    public FormPropertyManagerImpl() {
        super(EdsFormProperty.class);
    }


    @Override
    public EdsFormProperty getByFormID(String section) {

        return (EdsFormProperty) findSingle("select its from EdsFormProperty its where formID = ?", section);
    }
}
