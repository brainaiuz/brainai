package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsClockHistory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: Feb 11, 2014
 * Time: 7:09:22 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ClockHistoryManager extends Manager<EdsClockHistory> {

    List<EdsClockHistory> getClockItems(Integer busObjectId, Integer type);
}
