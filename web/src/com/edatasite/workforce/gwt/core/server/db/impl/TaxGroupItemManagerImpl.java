package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTaxGroupItem;
import com.edatasite.workforce.gwt.core.server.db.TaxGroupItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sherzod on 10/28/2015.
 */

@Repository("taxGroupItemManager")
public class TaxGroupItemManagerImpl extends BaseManager<EdsTaxGroupItem> implements TaxGroupItemManager {
    public TaxGroupItemManagerImpl() {
        super(EdsTaxGroupItem.class);
    }

    @Override
    public void deleteGroupItems(Integer taxID) {
        update("delete from EdsTaxGroupItem where tax.objectID = ?", taxID);
    }

    @Override
    public List<EdsTaxGroupItem> getGroupItems(Integer taxID) {
        return find("select tgi from EdsTaxGroupItem tgi where tgi.tax.objectID=? order by tgi.objectID", taxID);
    }
}
