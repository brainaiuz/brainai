package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsWarehouseCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.WarehouseCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("WarehouseCFManager")
public class WarehouseCFManagerImpl extends BaseManager<EdsWarehouseCustomFields> implements WarehouseCFManager {
    public WarehouseCFManagerImpl() {
        super(EdsWarehouseCustomFields.class);
    }
}
