package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPickListItem;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 24, 2010
 * Time: 6:38:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("pickListItemManager")
public class PickListItemManagerImpl extends BaseManager<EdsPickListItem> implements PickListItemManager {
    public PickListItemManagerImpl() {
        super(EdsPickListItem.class);
    }
}
