package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsRotationCutomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.RotationCfManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("rotationCfManager")
public class RotationCFManagerImpl extends BaseManager<EdsRotationCutomFields> implements RotationCfManager {
    public RotationCFManagerImpl() {
        super(EdsRotationCutomFields.class);
    }
}
