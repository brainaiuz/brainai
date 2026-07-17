/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCrmEntityMailList;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:38:49
 * To change this template use File | Settings | File Templates.
 */
public interface CrmEntityMailListManager extends Manager<EdsCrmEntityMailList> {

    List<EdsCrmEntityMailList> getList(ListingFilterParameter fp);

    Long getCrmEntityCount(ArrayList<Integer> mailListIDs);

    List<EdsCrmContact> getMailListMembers(ListingFilterParameter fp);

    Long getMailListMembersCount(ListingFilterParameter fp);

    EdsCrmEntityMailList getMailListDeletedEntity(Integer mailListID, Integer entityID);

    Map<Integer, List<Integer>> getByCrmEntityIDs(ArrayList<Integer> crmEntityIDs);

    List<Integer> getCrmEntitiesSubscribedLists(Integer entityID);

    List<Integer> getCrmEntitiesUnsubscribedLists(Integer crmEntityID);

    void subscribeOrUnsubscribeUsers(Integer mailListID, ArrayList<Integer> entityIDs, boolean unsubscribe);

    List<Integer> getMailListEntityIDs(Integer mailListID, List<Integer> subscriberID);

    List<EdsCrmEntityMailList> getSubscribedListsByCrmEntityId(Integer crmEntityID);

    Long getCountByDay(Calendar date);
}