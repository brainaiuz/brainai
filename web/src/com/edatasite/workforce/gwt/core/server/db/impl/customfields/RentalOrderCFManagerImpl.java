package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsRentalOrderCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.RentalOrderCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Asadbek on 25/03/2022
 */

@Repository("rentalOrderCFManager")
public class RentalOrderCFManagerImpl extends BaseManager<EdsRentalOrderCustomFields> implements RentalOrderCFManager {
    public RentalOrderCFManagerImpl() {
        super(EdsRentalOrderCustomFields.class);
    }

}
