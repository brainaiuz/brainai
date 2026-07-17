package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.recruitment.EdsRotationItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.customfields.RotationItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("rotationItemCFManager")
public class RotationItemCFManagerIml extends BaseManager<EdsRotationItemTableCF> implements RotationItemCFManager {
    public RotationItemCFManagerIml() {
        super(EdsRotationItemTableCF.class);
    }
}
