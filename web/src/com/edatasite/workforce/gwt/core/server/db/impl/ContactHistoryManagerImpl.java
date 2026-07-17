package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.contact.EdsContactHistory;
import com.edatasite.workforce.gwt.core.server.db.ContactHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 08.12.2010
 * Time: 16:30:25
 * To change this template use File | Settings | File Templates.
 */
@Repository("contactHistoryManager")
public class ContactHistoryManagerImpl extends BaseManager<EdsContactHistory> implements ContactHistoryManager {

    public ContactHistoryManagerImpl() {
        super(EdsContactHistory.class);
    }

    public List<EdsContactHistory> getContactHistoryList(Integer contactID) {
        return find("select ch from EdsContactHistory ch where ch.contact.objectID = ? order by ch.creationTime desc ", contactID);
    }
}
