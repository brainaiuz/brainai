package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.contact.EdsContactHistory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 08.12.2010
 * Time: 16:30:03
 * To change this template use File | Settings | File Templates.
 */
public interface ContactHistoryManager extends Manager<EdsContactHistory> {
    List<EdsContactHistory> getContactHistoryList(Integer contactID);
}
