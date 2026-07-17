package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCashAdvanceCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CashAdvanceCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Jamshid on 6/10/2022
 */

@Repository("cashAdvanceCFManager")
public class CashAdvanceCFManagerImpl extends BaseManager<EdsCashAdvanceCustomFields> implements CashAdvanceCFManager {
    public CashAdvanceCFManagerImpl() {
        super(EdsCashAdvanceCustomFields.class);
    }
}