package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 17:23:19
 */
@Repository("itemCFManager")
public class ItemCFManagerImpl extends BaseManager<EdsItemCustomFields> implements ItemCFManager {
    public ItemCFManagerImpl() {
        super(EdsItemCustomFields.class);
    }

    @Override
    public Object getCustomFieldValue(Integer objectID, String columnCode) {
       return  findNativeSingle("select cf." + columnCode + " from " + getCompanyId() + ".itemcustomfields cf  where cf.id=" + objectID);
    }
}