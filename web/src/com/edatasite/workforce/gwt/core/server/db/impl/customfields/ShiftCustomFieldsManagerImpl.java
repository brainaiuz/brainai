package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsShiftCustomFields;
import com.edatasite.workforce.gwt.core.server.db.ShiftCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("shiftCustomFieldsManagerImpl")
public class ShiftCustomFieldsManagerImpl extends BaseManager<EdsShiftCustomFields> implements ShiftCustomFieldsManager {
    public ShiftCustomFieldsManagerImpl() {
        super(EdsShiftCustomFields.class);
    }
}
