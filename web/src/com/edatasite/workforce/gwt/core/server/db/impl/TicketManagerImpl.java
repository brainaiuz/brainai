package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTicket;
import com.edatasite.workforce.gwt.core.server.db.TicketManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 4/7/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("ticketManager")
public class TicketManagerImpl extends BaseManager<EdsTicket> implements TicketManager {

    public TicketManagerImpl() {
        super(EdsTicket.class);
    }

    @Override
    public void deleteTicket(Integer objectID) {
        update("UPDATE EdsTicket t SET t.deleted = true WHERE t.objectID = ?", objectID);
    }

    @Override
    public List<EdsTicket> getTicketsByEventId(Integer eventId) {
        return (List<EdsTicket>) find("SELECT t FROM EdsTicket t WHERE t.deleted = false AND t.event.id=?", eventId);
    }


}
