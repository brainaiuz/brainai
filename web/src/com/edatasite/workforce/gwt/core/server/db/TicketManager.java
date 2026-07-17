package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTicket;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 4/7/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TicketManager extends Manager<EdsTicket> {
    void deleteTicket(Integer objectID);

    List<EdsTicket> getTicketsByEventId(Integer eventId);
}
