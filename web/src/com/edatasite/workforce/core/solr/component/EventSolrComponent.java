package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.solr.document.EventSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.EventSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.EventSolrItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
@Component
public class EventSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(EventSolrComponent.class);

    @Autowired
    private EventSolrDocRepository eventSolrDocRepository;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsEvent edsEvent) throws InterruptedException {
        this.indexes(Arrays.asList(edsEvent));
    }

    @Transactional
    public void indexes(List<EdsEvent> edsEventList) throws InterruptedException {

        Integer companyID = SecurityContext.getCompanyID();
        List<EventSolrDoc> eventSolrDocs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(edsEventList)) {

            Map<Integer, Set<EdsUser>> sharedUsers = eventManager.getEventSharedUsers(EdsEvent.getObjectIDs(edsEventList));
            for (EdsEvent edsEvent : edsEventList) {
                if (Objects.nonNull(edsEvent) && Boolean.TRUE.equals(!edsEvent.getDeleted())) {
                    try {
                        List<EdsRelation> relations = relationManager.getAllRelations(EdsRelation.TYPE_EVENT, edsEvent.getObjectID());
                        eventSolrDocs.add(createCaseDocument(edsEvent.getSolrRPC(), companyID, sharedUsers, relations, edsEvent.getEventCustomFields()));
                        log.info("Indexed Event Core CID - {}, objId - {}", companyID, edsEvent.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on Event with id = {} **********************", edsEvent.getObjectID());
                        throw e;
                    }
                }
                ;
            }
        }
        if (!eventSolrDocs.isEmpty()) {
            log.info("========= Create Event solr docs for company {} with size {} =========", companyID, eventSolrDocs.size());
            eventSolrDocRepository.saveAll(eventSolrDocs);
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsEvent> edsEventList, Map<Integer, List<EdsRelation>> relationsMap) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsEventList)) {
            ConcurrentLinkedQueue<EventSolrDoc> eventSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            Map<Integer, Set<EdsUser>> sharedUsers = eventManager.getEventSharedUsers(EdsEvent.getObjectIDs(edsEventList));
            for (EdsEvent edsEvent : edsEventList) {
                if (Objects.nonNull(edsEvent) && Boolean.TRUE.equals(!edsEvent.getDeleted())) {
                    EventSolrItem solrRPC = edsEvent.getSolrRPC();
                    EdsCrmCustomFields eventCustomFields = edsEvent.getEventCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        List<EdsRelation> edsRelations = relationsMap.get(edsEvent.getObjectID());
                                        eventSolrDocs.add(createCaseDocument(solrRPC, companyID, sharedUsers, edsRelations, eventCustomFields));
                                        log.info("Indexed Event Core CID - {}, objId - {}", companyId, edsEvent.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Event with id = {} **********************", edsEvent.getObjectID());
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading Cash Advance list", e);
            }

            if (!eventSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Event solr docs for company {} with size {} =========", companyID, eventSolrDocs.size());
                    eventSolrDocRepository.saveAll(eventSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Event list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EventSolrItem event) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + event.getObjectId();
    }

    private EventSolrDoc createCaseDocument(EventSolrItem event, Integer companyID, Map<Integer, Set<EdsUser>> sharedUsers, List<EdsRelation> edsRelationList, EdsCustomFields customFields) {
        EventSolrDoc eventSolrDoc = new EventSolrDoc();

        eventSolrDoc.setOid(SolrUtils.generatedOId(companyID, event.getObjectId()));
        eventSolrDoc.setCompanyId(companyID);
        eventSolrDoc.setEventId(event.getObjectId());
        eventSolrDoc.setSubject(event.getSubject());
        if (event.getCallType() != null) {
            eventSolrDoc.setCallType(event.getCallType());
        }
        eventSolrDoc.setInbound(event.getInbound());
        eventSolrDoc.setMissed(event.getMissed());
        if (event.getOwner() != null) {
            eventSolrDoc.setOwnerId(event.getOwner().getId());
            eventSolrDoc.setOwnerName(event.getOwner().getName());
            eventSolrDoc.setOwnerIdName(SolrUtils.getIdName(event.getOwner().getId(), event.getOwner().getName()));
        }
        if (event.getUpdater() != null) {
            eventSolrDoc.setUpdaterId(event.getUpdater().getId());
            eventSolrDoc.setUpdaterName(event.getUpdater().getName());
            eventSolrDoc.setUpdaterIdName(SolrUtils.getIdName(event.getUpdater().getId(), event.getUpdater().getName()));
        }
        eventSolrDoc.setGoogleId(event.getGoogleId());

        Set<EdsUser> users = sharedUsers.get(event.getObjectId());
        if (users != null && !users.isEmpty()) {
            users.forEach(sharedUser -> {
                eventSolrDoc.getSharedUserId().add(sharedUser.getObjectID());
                eventSolrDoc.getSharedUserName().add(sharedUser.getFullName());
                eventSolrDoc.getSharedUserIdName().add(SolrUtils.getIdName(sharedUser));
            });
        }
        if (edsRelationList != null && !edsRelationList.isEmpty()) {
            edsRelationList.forEach(relation -> {
                if ("contact".equals(relation.getToType())) {
                    eventSolrDoc.getContactId().add(relation.getToID());
                }
            });
        }
        eventSolrDoc.setCreationDate(event.getCreationDate());
        eventSolrDoc.setStartDate(event.getStartDate());
        eventSolrDoc.setEndDate(event.getEndDate());
        eventSolrDoc.setLastUpdateDate(event.getLastUpdateDate());
        eventSolrDoc.setRecurrenceId(event.getRecurrenceId());
        eventSolrDoc.setAllDay(event.getAllDay());
        eventSolrDoc.setMultiDay(event.getMultiDay());
        eventSolrDoc.setFromRecorder(event.getFromRecorder());
        eventSolrDoc.setDuration(event.getDuration());
        eventSolrDoc.setActivityTypeId(event.getActivityType().getId());
        String idName = SolrUtils.getIdName(Appointment.EVENT, Appointment.TYPE_EVENT);
        if (Appointment.CALL_LOG == event.getActivityType().getId()) {
            idName = SolrUtils.getIdName(Appointment.CALL_LOG, Appointment.TYPE_CALL_LOG);
        } else if (Appointment.INTERVIEW == event.getActivityType().getId()) {
            idName = SolrUtils.getIdName(Appointment.INTERVIEW, Appointment.TYPE_INTERVIEW);
        } else if (Appointment.SMS == event.getActivityType().getId()) {
            idName = SolrUtils.getIdName(Appointment.SMS, Appointment.TYPE_SMS);
        }
        eventSolrDoc.setActivityTypeIdName(idName);
        eventSolrDoc.setBooking(event.getBooking());
        eventSolrDoc.setDescription(event.getDescription());
        eventSolrDoc.setEdsLocationId(event.getLocationId());
        eventSolrDoc.setCreatedFromId(event.getCreatedFromId());

        if (edsRelationList != null && !edsRelationList.isEmpty()) {

            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_CONTACT,
                    eventSolrDoc::setContactRelatedId, eventSolrDoc::setContactRelatedName,
                    (doc, id, name) -> {
                        doc.setContactRelatedIdName(SolrUtils.getIdName(id, name));
                    });


            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_LEAD,
                    eventSolrDoc::setLeadRelatedId, eventSolrDoc::setLeadRelatedName,
                    (doc, id, name) -> {
                        doc.setLeadRelatedIdName(SolrUtils.getIdName(id, name));
                    });


            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_CRM_ACCOUNT,
                    eventSolrDoc::setCrmAccountRelatedId, eventSolrDoc::setCrmAccountRelatedName,
                    (doc, id, name) -> {
                        doc.setCrmAccountRelatedIdName(SolrUtils.getIdName(id, name));
                    });


            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_CANDIDATE,
                    eventSolrDoc::setCandidateRelatedId, eventSolrDoc::setCandidateRelatedName,
                    (doc, id, name) -> {
                        doc.setCandidateRelatedIdName(SolrUtils.getIdName(id, name));
                    });

            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_EMPLOYEE,
                    eventSolrDoc::setEmployeeRelatedId, eventSolrDoc::setEmployeeRelatedName,
                    (doc, id, name) -> {
                        doc.setEmployeeRelatedIdName(SolrUtils.getIdName(id, name));
                    });

            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_TASK,
                    eventSolrDoc::setTaskRelatedId, eventSolrDoc::setTaskRelatedName,
                    (doc, id, name) -> {
                        doc.setTaskRelatedIdName(SolrUtils.getIdName(id, name));
                    });

            setRelationFields(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT, EdsRelation.TYPE_PROJECT,
                    eventSolrDoc::setProjectRelatedId, eventSolrDoc::setProjectRelatedName,
                    (doc, id, name) -> {
                        doc.setProjectRelatedIdName(SolrUtils.getIdName(id, name));
                    });

            SolrRelationUtils.addToRelationBaseSolrDoc(eventSolrDoc, edsRelationList, EdsRelation.TYPE_EVENT);
            CustomFieldsUtils.setSolrDocDynamicFields(eventSolrDoc, customFields);
        }
        return eventSolrDoc;
    }

    private void setRelationFields(EventSolrDoc solrDoc, List<EdsRelation> edsRelationList, String fromType, String toType,
                                   Consumer<Integer> setId, Consumer<String> setName,
                                   TriConsumer<EventSolrDoc, Integer, String> setIdName) {
        for (EdsRelation relation : edsRelationList) {
            if (EdsRelation.TYPE_EVENT.equals(relation.getFromType()) && toType.equalsIgnoreCase(relation.getToType())) {
                setId.accept(relation.getToID());
                setName.accept(relation.getToName());
                setIdName.accept(solrDoc, relation.getToID(), relation.getToName());
            } else if (EdsRelation.TYPE_EVENT.equals(relation.getToType()) && fromType.equalsIgnoreCase(relation.getFromType())) {
                setId.accept(relation.getFromID());
                setName.accept(relation.getFromName());
                setIdName.accept(solrDoc, relation.getFromID(), relation.getFromName());
            }
        }
    }

    public Page<EventSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery eventSolrQuery = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrEventRepresenter.FIELD_LAST_UPDATE_DATE);
        if (!filterParameter.isSearchButton()) {
            if (StringUtils.isNotBlank(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                switch (filterParameter.getSortField()) {
                    case EventItem.SUBJECT -> solrSort = Sort.by(sortDirection, SolrEventRepresenter.SORTABLE_SUBJECT);
                    case EventItem.START_DATE ->
                            solrSort = Sort.by(new Sort.Order(sortDirection, SolrEventRepresenter.FIELD_START_DATE));
                    case EventItem.END_DATE ->
                            solrSort = Sort.by(new Sort.Order(sortDirection, SolrEventRepresenter.FIELD_END_DATE));
                    case EventItem.ASSIGNEE ->
                            solrSort = Sort.by(sortDirection, SolrEventRepresenter.FIELD_SHARED_USER_NAME);
                    case EventItem.CREATER -> solrSort = Sort.by(sortDirection, SolrEventRepresenter.FIELD_OWNER_NAME);
                }
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        eventSolrQuery.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_EVENT_CORE, eventSolrQuery, EventSolrDoc.class);
    }

    public List<EventSolrDoc> getDocumentsExistingInBase2(List<EventSolrDoc> eventSolrDocs) {
        List<EventSolrDoc> documents = new ArrayList<>();
        Map<Integer, EventSolrDoc> mapDocuments = new HashMap<>();
        if (eventSolrDocs != null && !eventSolrDocs.isEmpty()) {
            for (EventSolrDoc doc : eventSolrDocs) {
                documents.add(doc);
                mapDocuments.put(doc.getEventId(), doc);
            }
        }

        List<Integer> objectIDsFromDatabase = eventManager.getEventIDsBySolrIDs(new ArrayList(mapDocuments.keySet()));
        if (objectIDsFromDatabase != null && !objectIDsFromDatabase.isEmpty()) {
            for (Integer objectID : objectIDsFromDatabase) {
                mapDocuments.remove(objectID);
            }
            if (!mapDocuments.isEmpty()) {
                documents.removeAll(mapDocuments.values());
            }
        }
        return documents;
    }

    public FacetFilterRpc getEventFacetFilterData(FacetFilterRpc eventFacetFilter, Integer eventType, Integer createdFrom) {
        if (!eventFacetFilter.isFilterChanges()) {
            eventFacetFilter = commonServiceLocal.getUserFacetFilter(eventFacetFilter);
        }
        if (eventFacetFilter != null && !eventFacetFilter.getCustomData().isEmpty()) {
            if (eventFacetFilter.getCustomData().containsKey(Appointment.TODAY)
                    && Boolean.valueOf(eventFacetFilter.getCustomData().get(Appointment.TODAY))) {
                eventFacetFilter.setStartDate(null);
                eventFacetFilter.setEndDate(null);
            }
        }
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEventType(eventType);
        fp.setSearchKey(eventFacetFilter.getSearchKey());
        fp.setRelationType(eventFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE));
        fp.setCreatedFrom(createdFrom);
        try {
            fp.setRelationID(eventFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID) != null ? Integer.valueOf(eventFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID)) : null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(crmServiceLocal.getEventCoreSolrQuery(edsUser, eventFacetFilter, fp));

        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQuery(
                eventFacetFilter,
                edsCompany,
                SolrEventRepresenter.FIELD_START_DATE,
                SolrEventRepresenter.FIELD_END_DATE
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_EVENT_CORE, solrQuery.toString(), eventFacetFilter, EventSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, eventFacetFilter);

        if (eventFacetFilter != null && eventFacetFilter.getFacetContentMap().containsKey(FacetContentType.EventFacetFilter.getContentCode()[1])) {
            SelectItem[] items = eventFacetFilter.getFacetContentMap().get(FacetContentType.EventFacetFilter.getContentCode()[1]).getFacetItems();
            if (items != null && items.length > 0) {
                for (SelectItem item : items) {
                    if (item != null && item.getDescription() != null && !"".equals(item.getDescription())) {
                        item.setDescription(item.getDescription().replace("false", "Event"));
                        item.setDescription(item.getDescription().replace("true", "Call"));
                    }
                }
            }
        }
        return eventFacetFilter;
    }

    @FunctionalInterface
    private interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
