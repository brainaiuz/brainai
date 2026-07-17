package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.solr.document.CourseBookingSolrDoc;
import com.edatasite.workforce.core.solr.repository.CourseBookingSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseBookingRepresenter;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BOOKING_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BOOKING_REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_COURSE_BOOKING_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:25.
 */
@Component
public class CourseBookingSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CourseBookingSolrComponent.class);

    @Autowired
    private CourseBookingSolrDocRepository courseBookingSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCourseBooking courseBooking) throws InterruptedException {
        this.indexes(Arrays.asList(courseBooking));
    }

    @Transactional(readOnly = true)
    public void indexes(List<EdsCourseBooking> edsCourseBookings) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCourseBookings)) {
            List<CourseBookingSolrDoc> courseBookingSolrDocs = new ArrayList<>();

            for (EdsCourseBooking courseBooking : edsCourseBookings) {
                if (Objects.nonNull(courseBooking)) {
                    try {
                        courseBookingSolrDocs.add(createCourceBookingDocument(courseBooking.getRPC(), companyId));
                        log.info("Indexed CourseBooking Core CID - {}, objId - {}", companyId, courseBooking.getObjectID());
                    } catch (Exception e) {

                        log.error("********************* Error on EdsCourseBooking with id = {} **********************", courseBooking.getObjectID());
                        throw e;
                    }
                }
            }
            if (!courseBookingSolrDocs.isEmpty()) {
                log.info("========= Create Course Booking solr docs for company {} with size {} =========", companyId, courseBookingSolrDocs.size());
                courseBookingSolrDocRepository.saveAll(courseBookingSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCourseBooking> edsCourseBookings) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCourseBookings)) {
            ConcurrentLinkedQueue<CourseBookingSolrDoc> courseBookingSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCourseBooking courseBooking : edsCourseBookings) {
                if (Objects.nonNull(courseBooking)) {
                    CourseBookingItem rpc = courseBooking.getRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(rpc), () -> {
                                courseBookingSolrDocs.add(createCourceBookingDocument(rpc, companyId));
                                        log.info("Indexed CourseBooking Core CID - {}, objId - {}", companId, courseBooking.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on EdsCourseBooking with id = {} **********************", courseBooking.getObjectID());
//                            throw e;
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
                log.error("Error on loading Course Booking list", e);
            }

            if (!courseBookingSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Course Booking solr docs for company {} with size {} =========", companyId, courseBookingSolrDocs.size());
                    courseBookingSolrDocRepository.saveAll(courseBookingSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Course Booking list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(CourseBookingItem rpc) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + rpc.getObjectID();
    }

    private CourseBookingSolrDoc createCourceBookingDocument(CourseBookingItem courseBooking, Integer companyId) {
        CourseBookingSolrDoc bookingSolrDoc = new CourseBookingSolrDoc();

        bookingSolrDoc.setOid(SolrUtils.generatedOId(companyId, courseBooking.getObjectID()));
        bookingSolrDoc.setCompanyId(companyId);
        bookingSolrDoc.setCourseBookingId(courseBooking.getObjectID());
        bookingSolrDoc.setCourseBookingNumber(courseBooking.getNumber());

        if (courseBooking.getCustomer() != null) {
            bookingSolrDoc.setCustomerId(courseBooking.getCustomer().getId());
            bookingSolrDoc.setCustomerName(courseBooking.getCustomer().getName());
            bookingSolrDoc.setCustomerIdName(SolrUtils.getIdName(courseBooking.getCustomer().getId(), courseBooking.getCustomer().getName()));
        }

        if (courseBooking.getContact() != null) {
            bookingSolrDoc.setMangerId(courseBooking.getContact().getId());
            bookingSolrDoc.setMangerName(courseBooking.getContact().getName());
            bookingSolrDoc.setMangerIdName(SolrUtils.getIdName(courseBooking.getContact().getId(), courseBooking.getContact().getName()));
        }

        if (courseBooking.getLocation() != null) {
            bookingSolrDoc.setLocationId(courseBooking.getLocation().getId());
            bookingSolrDoc.setLocationName(courseBooking.getLocation().getName());
            bookingSolrDoc.setLocationIdName(SolrUtils.getIdName(courseBooking.getLocation().getId(), courseBooking.getLocation().getName()));
        }

        if (courseBooking.getStatus() != null) {
            bookingSolrDoc.setStatusId(courseBooking.getStatus().getId());
            bookingSolrDoc.setStatusName(courseBooking.getStatus().getName());
            bookingSolrDoc.setStatusIdName(SolrUtils.getIdName(courseBooking.getStatus().getId(), courseBooking.getStatus().getName()));
            bookingSolrDoc.setStatusCode(courseBooking.getStatus().getCode());

            if (BOOKING_APPROVED.equals(courseBooking.getStatus().getCode()) || BOOKING_REJECTED.equals(courseBooking.getStatus().getCode())) {
                if (courseBooking.getUpdater() == null) {
                    bookingSolrDoc.setUpdaterName("Client");
                } else {
                    bookingSolrDoc.setUpdaterId(courseBooking.getUpdater().getId());
                    bookingSolrDoc.setUpdaterName(courseBooking.getUpdater().getName());
                }
            }

        }

        if (courseBooking.getType() != null) {
            bookingSolrDoc.setTypeId(courseBooking.getType().getId());
            bookingSolrDoc.setTypeName(courseBooking.getType().getName());
            bookingSolrDoc.setTypeIdName(SolrUtils.getIdName(courseBooking.getType().getId(), courseBooking.getType().getName()));
            bookingSolrDoc.setTypeCode(courseBooking.getType().getCode());
        }

        if (courseBooking.getCreator() != null) {
            bookingSolrDoc.setCreatorId(courseBooking.getCreator().getId());
            bookingSolrDoc.setCreatorName(courseBooking.getCreator().getName());
            bookingSolrDoc.setCreatorIdName(SolrUtils.getIdName(courseBooking.getCreator().getId(), courseBooking.getCreator().getName()));
        }

        bookingSolrDoc.setCreatedDate(courseBooking.getCreationDate());

        return bookingSolrDoc;
    }

    public Page<CourseBookingSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.ASC, SolrCourseBookingRepresenter.FIELD_CREATED_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case CourseBookingItem.NUMBER ->
                            solrSort = Sort.by(order, SolrCourseBookingRepresenter.SORTABLE_COURSE_BOOKING_NUMBER);
                    case CourseBookingItem.CUSTOMER ->
                            solrSort = Sort.by(order, SolrCourseBookingRepresenter.SORTABLE_CUSTOMER_NAME);
                    case CourseBookingItem.LOCATION ->
                            solrSort = Sort.by(order, SolrCourseBookingRepresenter.SORTABLE_LOCATION_NAME);
                    case CourseBookingItem.STATUS ->
                            solrSort = Sort.by(order, SolrCourseBookingRepresenter.FIELD_STATUS_NAME);
                    case CourseBookingItem.TYPE ->
                            solrSort = Sort.by(order, SolrCourseBookingRepresenter.SORTABLE_TYPE_NAME);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_COURSE_BOOKING_CORE, query, CourseBookingSolrDoc.class);
    }

}
