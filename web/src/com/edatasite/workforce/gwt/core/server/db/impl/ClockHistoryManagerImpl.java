package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsClock;
import com.edatasite.workforce.core.domain.EdsClockHistory;
import com.edatasite.workforce.gwt.core.server.db.ClockHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 7:10:06 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("clockHistoryManager")
public class ClockHistoryManagerImpl extends BaseManager<EdsClockHistory> implements ClockHistoryManager{

    public ClockHistoryManagerImpl() {
        super(EdsClock.class);
    }

    @Override
    public List<EdsClockHistory> getClockItems(Integer busObjectId, Integer type) {
        return find("select clock from EdsClockHistory clock where clock.busObjectId = ? and clock.relation = ? order by clock.date desc", busObjectId, type);
    }
}
