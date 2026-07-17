package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTaxComponent;
import com.edatasite.workforce.gwt.core.server.db.TaxComponentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 09.06.2010
 * Time: 11:39:29
 * To change this template use File | Settings | File Templates.
 */
@Repository("taxComponentManager")
public class TaxComponentManagerImpl extends BaseManager<EdsTaxComponent> implements TaxComponentManager {
    public TaxComponentManagerImpl() {
        super(EdsTaxComponent.class);
    }

    public List<EdsTaxComponent> getTaxComponents(Integer taxID) {
        return find("select tc from EdsTaxComponent tc where tc.tax.objectID = ?", taxID);
    }

    public void deleteTaxComponents(Integer taxID) {
        update("delete from EdsTaxComponent where tax.objectID = ?", taxID);
    }
}
