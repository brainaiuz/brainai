package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBrigadaCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BrigadaCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Bekhruz on 03/11/2022
 */
@Repository("brigadaCFManager")
public class BrigadaCFManagerIml extends BaseManager<EdsBrigadaCustomFields> implements BrigadaCFManager {
    public BrigadaCFManagerIml() {
        super(EdsBrigadaCustomFields.class);
    }
}
