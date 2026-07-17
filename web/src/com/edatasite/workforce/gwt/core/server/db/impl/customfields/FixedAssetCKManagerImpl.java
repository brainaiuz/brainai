package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsFixedAssetCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.FixedAssetCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilshod Madrahimov
 * Date: 8/23/14
 * Time: 8:36 PM
 */
@Repository("fixedAssetCFManager")
public class FixedAssetCKManagerImpl extends BaseManager<EdsFixedAssetCustomFields> implements FixedAssetCFManager {

    public FixedAssetCKManagerImpl() {
        super(EdsFixedAssetCustomFields.class);
    }
}
