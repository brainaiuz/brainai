package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 5:06:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EventManager extends Manager<EdsEvent> {

    List<EdsEvent> getList(ListingFilterParameter fp);

    Integer getListCount(ListingFilterParameter fp);

    EdsEvent getFirstOrLastEventInRecurringSeries(Integer recurrenceID, boolean isFirst);

    EdsEvent getEventInstance(Integer recurrenceID, Date fireTime);

    List<EdsEvent> getAllEventInstances(Integer recurrenceID);

    List<Integer> getAllEventInstancesIDs(Integer recurrenceID, Date afterFireTime);

    List<EdsEvent> getAllEventInstancesAfter(Integer recurrenceID, Date afterFireTime);

    void deleteEvents(Integer recurrenceID, Integer userID, Date afterFireTime);

    List<Integer> deleteEvents(List<Integer> objectIDs, EdsUser user);

    EdsEvent getNextEventByRecurrence(EdsRecurrence recurrence, Integer eventId);

    void removeRecurrenceFromEvent(Integer recurrenceID, Integer companyID);

    List<Object[]> getEventsByDate(boolean isDueDate);

    BigInteger getEventsSize();

    Date getRecurringEventFirstOrLastDate(Integer recurrenceID, Date currentEventFireTime, boolean isFirst);

    Long getAllEventInstancesSize(Integer recurrenceID);

    List<Integer> getCompanyDeletedEventListForSolr(SolrReindexRpc solrReindex);

    List<EdsEvent> getCompanyEventListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getEventIdsByIDs(String ids);

    List<Integer> getEventIdsWithLimit(int startat, int limit);

    EdsEvent getEventByID(Integer eventID);

    List<EdsEvent> getEventsByIDs(String eventIDs);

    void create(EdsEvent event, boolean addToSolr);

    void update(EdsEvent event, boolean addToSolr);

    void addToSolr(Integer... objectIDs);

    void addToSolr(EdsEvent... events);

    Map<Integer, Set<EdsUser>> getEventSharedUsers(ArrayList<Integer> eventIDs);

    EdsEvent getEventByScheduledCourse(String subject, Date startDate, Date endDate);

    List<Integer> getEventIDsBySolrIDs(List<Integer> objectIDs);

    List<EdsEvent> getWorkflowEvents(ListingFilterParameter filterParameter);

    void deleteUserTemporaryKey(Integer objectID, String temporaryKey);

    LinkedHashMap<Integer, ArrayList<Date>> getEventDates(Date startDate, boolean isPositive, ListingFilterParameter filterParameter);

    EdsEvent getByAsteriskid(String asteriskid);
}
