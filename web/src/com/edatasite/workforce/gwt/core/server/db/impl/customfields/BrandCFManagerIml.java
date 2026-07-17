package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBrandCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BrandCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("brandCFManager")
public class BrandCFManagerIml extends BaseManager<EdsBrandCustomFields> implements BrandCFManager {
    public BrandCFManagerIml() {
        super(EdsBrandCustomFields.class);
    }

}

