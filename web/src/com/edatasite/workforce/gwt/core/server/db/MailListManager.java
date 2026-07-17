package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:38:49
 * To change this template use File | Settings | File Templates.
 */
public interface MailListManager extends Manager<EdsMailList> {

    List<EdsMailList> getList(ListingFilterParameter fp);

    List<Object[]> getListOfMailLists(ListingFilterParameter fp);

    Long getTotalCountOfMailLists(ListingFilterParameter fp);

    List<EdsMailList> getContactsEdsMailingLists(Integer contactID);

    List<SelectItem> getContactsMailingLists(Integer contactID);

    Long getLeadCountByMessageID(Integer messageID);
}
