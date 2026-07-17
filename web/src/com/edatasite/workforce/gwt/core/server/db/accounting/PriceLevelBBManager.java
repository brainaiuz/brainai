package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelBB;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/16/16
 * Time: 3:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PriceLevelBBManager extends Manager<EdsPriceLevelBB> {

    void deleteByPriceLevel(Integer objectID);

}
