package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.solr.document.CourseScheduleSolrDoc;
import com.edatasite.workforce.core.solr.repository.CourseScheduleSolrDocRepository;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
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

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_COURSE_SCHEDULE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Sardorbek Juraboev on 05.09.2023 12:39.
 */
@Component
public class CourseScheduleSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CourseScheduleSolrComponent.class);

    @Autowired
    private CourseScheduleSolrDocRepository courseScheduleSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCourseSchedule courseSchedule) throws InterruptedException {
        this.indexes(Arrays.asList(courseSchedule));
    }

    @Transactional(readOnly = true)
    public void indexes(List<EdsCourseSchedule> courseSchedules) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(courseSchedules)) {
            List<CourseScheduleSolrDoc> courseScheduleSolrDocs = new ArrayList<>();

            for (EdsCourseSchedule courseSchedule : courseSchedules) {
                if (Objects.nonNull(courseSchedule)) {
                    try {
                        courseScheduleSolrDocs.add(createCourceScheduleDocument(courseSchedule.getRPC(), companyId));
                        log.info("Indexed CourseSchedule Core CID - {}, objId - {}", companyId, courseSchedule.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on EdsCourseSchedule with id = {} **********************", courseSchedule.getObjectID());
                        throw e;
                    }
                }
            }
            if (!courseScheduleSolrDocs.isEmpty()) {
                log.info("========= Create Course Schedusle solr docs for company {} with size {} =========", companyId, courseScheduleSolrDocs.size());
                courseScheduleSolrDocRepository.saveAll(courseScheduleSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCourseSchedule> courseSchedules) throws InterruptedException {
        if (!CollectionUtils.isEmpty(courseSchedules)) {
            ConcurrentLinkedQueue<CourseScheduleSolrDoc> courseScheduleSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCourseSchedule courseSchedule : courseSchedules) {
                if (Objects.nonNull(courseSchedule)) {
                    ScheduledCourseItem rpc = courseSchedule.getRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(rpc), () -> {
                                        courseScheduleSolrDocs.add(createCourceScheduleDocument(rpc, Integer.valueOf(companyId)));
                                        log.info("Indexed CourseSchedule Core CID - {}, objId - {}", companyId, courseSchedule.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on EdsCourseSchedule with id = {} **********************", courseSchedule.getObjectID());
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
                log.error("Error on loading Course Schedule list", e);
            }

            if (!courseScheduleSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Course Schedule solr docs for company {} with size {} =========", companyId, courseScheduleSolrDocs.size());
                    courseScheduleSolrDocRepository.saveAll(courseScheduleSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Course Schedule list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(ScheduledCourseItem rpc) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + rpc.getObjectID();
    }

    private CourseScheduleSolrDoc createCourceScheduleDocument(ScheduledCourseItem courseSchedule, Integer companyId) {
        CourseScheduleSolrDoc scheduleSolrDoc = new CourseScheduleSolrDoc();

        scheduleSolrDoc.setOid(SolrUtils.generatedOId(companyId, courseSchedule.getObjectID()));
        scheduleSolrDoc.setCompanyId(companyId);
        scheduleSolrDoc.setCourseScheduleId(courseSchedule.getObjectID());
        scheduleSolrDoc.setCourseScheduleNumber(courseSchedule.getNumber());

        if (courseSchedule.getCourse() != null) {
            scheduleSolrDoc.setCourseId(courseSchedule.getCourse().getId());
            scheduleSolrDoc.setCourseName(courseSchedule.getCourse().getName());
            scheduleSolrDoc.setCourseIdName(SolrUtils.getIdName(courseSchedule.getCourse().getId(), courseSchedule.getCourse().getName()));
            scheduleSolrDoc.setCourseCode(courseSchedule.getCourse().getNumber());
        }

        if (courseSchedule.getLanguage() != null) {
            scheduleSolrDoc.setLanguageId(courseSchedule.getLanguage().getId());
            scheduleSolrDoc.setLanguageName(courseSchedule.getLanguage().getName());
            scheduleSolrDoc.setLanguageIdName(SolrUtils.getIdName(courseSchedule.getLanguage().getId(), courseSchedule.getLanguage().getName()));
        }

        scheduleSolrDoc.setEnableOvertime(courseSchedule.isEnabledOvertime());
        scheduleSolrDoc.setStartDate(courseSchedule.getStartDate());

        if (courseSchedule.getLocation() != null) {
            scheduleSolrDoc.setLocationId(courseSchedule.getLocation().getId());
            scheduleSolrDoc.setLocationName(courseSchedule.getLocation().getName());
            scheduleSolrDoc.setLocationIdName(SolrUtils.getIdName(courseSchedule.getLocation().getId(), courseSchedule.getLocation().getName()));
        }

        if (courseSchedule.getAssessor() != null) {
            scheduleSolrDoc.setAssessorId(courseSchedule.getAssessor().getId());
            scheduleSolrDoc.setAssessorName(courseSchedule.getAssessor().getName());
        }

        if (courseSchedule.getInstructor() != null) {
            scheduleSolrDoc.setInstructorId(courseSchedule.getInstructor().getId());
            scheduleSolrDoc.setInstructorName(courseSchedule.getInstructor().getName());
            scheduleSolrDoc.setInstructorIdName(SolrUtils.getIdName(courseSchedule.getInstructor().getId(), courseSchedule.getInstructor().getName()));
        }

        if (courseSchedule.getStatus() != null) {
            scheduleSolrDoc.setStatusId(courseSchedule.getStatus().getId());
            scheduleSolrDoc.setStatusName(courseSchedule.getStatus().getName());
            scheduleSolrDoc.setStatusIdName(SolrUtils.getIdName(courseSchedule.getStatus().getId(), courseSchedule.getStatus().getName()));
            scheduleSolrDoc.setCourseCode(courseSchedule.getStatus().getCode());
        }

        if (courseSchedule.getDuration() != null) {
            scheduleSolrDoc.setDuration(courseSchedule.getDuration());
        } else {
            scheduleSolrDoc.setDuration(0);
        }

        scheduleSolrDoc.setNumberOfSeats(courseSchedule.getNumberOfSeats());
        scheduleSolrDoc.setCreatedAt(courseSchedule.getCreatedDate());
        scheduleSolrDoc.setModifiedAt(courseSchedule.getModifiedDate());

        return scheduleSolrDoc;
    }

    public Page<CourseScheduleSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrCourseScheduleRepresenter.FIELD_CREATED_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case ScheduledCourseItem.NUMBER ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_COURSE_SCHEDULE_NUMBER);
                    case ScheduledCourseItem.COURSE ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_COURSE_NAME);
                    case ScheduledCourseItem.DURATION ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_DURATION);
                    case ScheduledCourseItem.ASSESSOR ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_ASSESSOR_NAME);
                    case ScheduledCourseItem.INSTRUCTOR ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_INSTRUCTOR_NAME);
                    case ScheduledCourseItem.LANGUAGE ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_LANGUAGE_NAME);
                    case ScheduledCourseItem.START_DATE ->
                            solrSort = Sort.by(order, SolrCourseScheduleRepresenter.SORTABLE_START_DATE);
                    default ->
                            solrSort = Sort.by(Sort.Direction.DESC, SolrCourseScheduleRepresenter.FIELD_CREATED_DATE);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_COURSE_SCHEDULE_CORE, query, CourseScheduleSolrDoc.class);
    }
}
