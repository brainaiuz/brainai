package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsWorldPayHistory;
import com.edatasite.workforce.gwt.core.server.db.WorldPayHistoryManager;
import org.springframework.stereotype.Repository;

/**
 * User: Murad Satimov
 * Date: 5/30/15 1:01 AM
 */
@Repository("worldPayHistoryManager")
public class WorldPayHistoryManagerImpl extends BaseManager<EdsWorldPayHistory> implements WorldPayHistoryManager {
    public WorldPayHistoryManagerImpl() {
        super(EdsWorldPayHistory.class);
    }
}
