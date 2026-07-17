package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsTaxTemplate;
import com.edatasite.workforce.gwt.core.server.db.TaxTemplateManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 24.02.2009
 * Time: 17:54:00
 * To change this template use File | Settings | File Templates.
 */
@Repository("taxTemplateManager")
public class TaxTemplateManagerImpl extends BaseManager<EdsTaxTemplate> implements TaxTemplateManager {

    public TaxTemplateManagerImpl() {
        super(EdsTaxTemplate.class);
    }
}
