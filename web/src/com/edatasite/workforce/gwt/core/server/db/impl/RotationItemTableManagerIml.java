package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsRotationItemTable;
import com.edatasite.workforce.gwt.core.server.db.RotationItemTableManager;
import org.springframework.stereotype.Repository;

@Repository("rotationItemTableManager")
public class RotationItemTableManagerIml extends BaseManager<EdsRotationItemTable> implements RotationItemTableManager {
    public RotationItemTableManagerIml() {
        super(EdsRotationItemTable.class);
    }
}
