package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsProductCategoryCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.ProductCategoryCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("productCategoryCFManager")
public class ProductCategoryCFManagerImpl extends BaseManager<EdsProductCategoryCustomFields> implements ProductCategoryCFManager {
    public ProductCategoryCFManagerImpl() {
        super(EdsProductCategoryCustomFields.class);
    }
}