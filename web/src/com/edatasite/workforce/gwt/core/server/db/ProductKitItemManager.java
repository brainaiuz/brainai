package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProductKitItems;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 2, 2010
 * Time: 12:34:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductKitItemManager extends Manager<EdsProductKitItems> {

    void deleteProductKitItems(Integer pkID);

    boolean isUsedInProductKit(Integer productId);
}
