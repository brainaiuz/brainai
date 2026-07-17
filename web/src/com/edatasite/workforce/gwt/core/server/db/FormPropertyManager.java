package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsFormProperty;

public interface FormPropertyManager extends Manager<EdsFormProperty> {

    EdsFormProperty getByFormID(String section);
}
