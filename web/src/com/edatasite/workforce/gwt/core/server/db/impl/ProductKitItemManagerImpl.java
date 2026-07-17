package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProductKitItems;
import com.edatasite.workforce.gwt.core.server.db.ProductKitItemManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 2, 2010
 * Time: 12:36:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("productKitItemManager")
public class ProductKitItemManagerImpl extends BaseManager<EdsProductKitItems> implements ProductKitItemManager {

    public ProductKitItemManagerImpl() {
        super(EdsProductKitItems.class);
    }

    @Override
    public void deleteProductKitItems(Integer pkID) {
        update("DELETE FROM EdsProductKitItems pki WHERE pki.productKit.objectID = ?", pkID);
    }

    @Override
    public boolean isUsedInProductKit(Integer productId) {
        return find("select pk from EdsProductKitItems  pk where pk.item.objectID=?", productId).size() > 0;
    }
}
