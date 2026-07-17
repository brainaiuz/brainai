package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.customfields.EdsSickRequestCustomFields;
import com.edatasite.workforce.core.solr.document.LeaveRequestSolrDoc;
import com.edatasite.workforce.core.solr.repository.LeaveRequestSolrDocRepository;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.EmployeeSolr;
import com.edatasite.workforce.gwt.payroll.client.rpc.SickRequestSolr;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LEAVE_REQUEST_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
@Component
public class LeaveRequestSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(LeaveRequestSolrComponent.class);

    @Autowired
    private LeaveRequestSolrDocRepository leaveRequestSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsSickRequest edsSickRequest) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsSickRequest));
    }

    @Transactional
    public void indexes(List<EdsSickRequest> edsSickRequestList) throws InterruptedException {

        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsSickRequestList)) {
            List<LeaveRequestSolrDoc> leaveRequestSolrDocs = new ArrayList<>();

            for (EdsSickRequest edsSickRequest : edsSickRequestList) {
                if (edsSickRequest != null) {
                    try {
                        leaveRequestSolrDocs.add(createLeaveRequestDocument(edsSickRequest.getRPC(), edsSickRequest.getCustomFields(), companyID));
                        log.info("Indexed LeaveRequest Core CID - {}, objId - {}", companyID, edsSickRequest.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Leave Request = {} **********************", edsSickRequest.getNumberData());
                        throw e;
                    }
                }
            }

            if (!leaveRequestSolrDocs.isEmpty()) {
                log.info("========= Create leave request solr docs for company {} with size {} =========", companyID, leaveRequestSolrDocs.size());
                leaveRequestSolrDocRepository.saveAll(leaveRequestSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsSickRequest> edsSickRequestList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsSickRequestList)) {
            ConcurrentLinkedQueue<LeaveRequestSolrDoc> leaveRequestSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsSickRequest edsSickRequest : edsSickRequestList) {
                if (edsSickRequest != null) {
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(edsSickRequest),
                                    () -> {
                                        leaveRequestSolrDocs.add(createLeaveRequestDocument(edsSickRequest.getRPC(), edsSickRequest.getCustomFields(), companyID));
                                        log.info("Indexed LeaveRequest Core CID - {}, objId - {}", companId, edsSickRequest.getObjectID());
                                    });
                        } catch (Exception e) {
                            log.error("********************* Leave Request = {} **********************", edsSickRequest.getNumberData());
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> invokedTasks = executor.invokeAll(tasks);
                for (Future<Void> invokedTask : invokedTasks) {
                    try {
                        invokedTask.get();
                    } catch (ExecutionException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading Leave Request list", e);
            }
            if (!leaveRequestSolrDocs.isEmpty()) {
                log.info("========= Create leave request solr docs for company {} with size {} =========", companyID, leaveRequestSolrDocs.size());
                try {
                    leaveRequestSolrDocRepository.saveAll(leaveRequestSolrDocs);
                } catch (Exception e) {
                    log.error("Error indexing leave request solr docs", e);
                }
            }
        }
    }

    private String getSynchronizedKey(EdsSickRequest edsSickRequest) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + edsSickRequest.getObjectID();
    }

    private LeaveRequestSolrDoc createLeaveRequestDocument(SickRequestSolr sickRequest, EdsSickRequestCustomFields customFields, Integer companyId) {
        LeaveRequestSolrDoc leaveRequestSolrDoc = new LeaveRequestSolrDoc();

        leaveRequestSolrDoc.setOid(SolrUtils.generatedOId(companyId, sickRequest.getId()));
        leaveRequestSolrDoc.setCompanyId(companyId);
        leaveRequestSolrDoc.setObjectId(sickRequest.getId());
        leaveRequestSolrDoc.setNumberData(sickRequest.getNumberData() != null && !sickRequest.getNumberData().isEmpty() ? sickRequest.getNumberData() : "N/A");
        if (sickRequest.getEmployee() != null) {
            EmployeeSolr employee = sickRequest.getEmployee();
            leaveRequestSolrDoc.setEmployeeId(employee.getId());
            leaveRequestSolrDoc.setEmployeeName(employee.getName());
            leaveRequestSolrDoc.setEmployeeIdName(SolrUtils.getIdName(employee.getId(), employee.getName()));
            if (sickRequest.getEmployee().getTeam() != null) {
                SelectItem department = sickRequest.getEmployee().getTeam();
                leaveRequestSolrDoc.setDepartmentId(department.getId());
                leaveRequestSolrDoc.setDepartmentName(department.getName());
                leaveRequestSolrDoc.setDepartmentIdName(SolrUtils.getIdName(department.getId(), department.getName()));
            }
            if (sickRequest.getEmployee().getPosition() != null) {
                SelectItem position = sickRequest.getEmployee().getPosition();
                leaveRequestSolrDoc.setPositionId(position.getId());
                leaveRequestSolrDoc.setPositionName(position.getName());
                leaveRequestSolrDoc.setPositionIdName(SolrUtils.getIdName(position.getId(), position.getName()));
            }
            if (sickRequest.getEmployee().getLocation() != null) {
                SelectItem location = sickRequest.getEmployee().getLocation();
                leaveRequestSolrDoc.setLocationId(location.getId());
                leaveRequestSolrDoc.setLocationName(location.getName());
                leaveRequestSolrDoc.setLocationIdName(SolrUtils.getIdName(location.getId(), location.getName()));
            }
            if (employee.getSupervisor() != null) {
                SelectItem reportsTo = employee.getSupervisor();
                leaveRequestSolrDoc.setSupervisorId(reportsTo.getId());
                leaveRequestSolrDoc.setSupervisorName(reportsTo.getName());
                leaveRequestSolrDoc.setSupervisorIdName(SolrUtils.getIdName(reportsTo.getId(), reportsTo.getName()));
            }
        }
        leaveRequestSolrDoc.setDescription(sickRequest.getDescription());
        if (sickRequest.getLeaveReason() != null) {
            SelectItem leaveReason = sickRequest.getLeaveReason();
            leaveRequestSolrDoc.setReasonId(leaveReason.getId());
            leaveRequestSolrDoc.setReasonName(leaveReason.getName());
            leaveRequestSolrDoc.setReasonCode(leaveReason.getCode());
            leaveRequestSolrDoc.setReasonIdName(SolrUtils.getIdName(leaveReason.getId(), leaveReason.getName()));
        }
        if (sickRequest.getOverallStatus() != null) {
            SelectItem status = sickRequest.getOverallStatus();
            leaveRequestSolrDoc.setStatusId(status.getId());
            leaveRequestSolrDoc.setStatusName(status.getName());
            leaveRequestSolrDoc.setStatusCode(status.getCode());
            leaveRequestSolrDoc.setStatusIdName(SolrUtils.getIdName(status.getId(), status.getName()));
            leaveRequestSolrDoc.setStatusIdCode(SolrUtils.getIdName(status.getId(), status.getCode()));
        }
        leaveRequestSolrDoc.setStartDate(sickRequest.getStartDate());
        leaveRequestSolrDoc.setEndDate(sickRequest.getEndDate());
        leaveRequestSolrDoc.setCreatedDate(sickRequest.getCreatedDate());
        if (sickRequest.getType() != null) {
            leaveRequestSolrDoc.setTypeId(sickRequest.getType().getId());
            leaveRequestSolrDoc.setTypeName(sickRequest.getType().getName());
        }
        if (sickRequest.getCurrentApprover() != null) {
            SelectItem approver = sickRequest.getCurrentApprover();
            leaveRequestSolrDoc.setApproverId(approver.getId());
            leaveRequestSolrDoc.setApproverName(approver.getName());
            leaveRequestSolrDoc.setApproverIdName(SolrUtils.getIdName(approver.getId(), approver.getName()));
        }
        if (sickRequest.getRegisteredBy() != null) {
            SelectItem creator = sickRequest.getRegisteredBy();
            leaveRequestSolrDoc.setCreatorId(creator.getId());
            leaveRequestSolrDoc.setCreatorName(creator.getName());
            leaveRequestSolrDoc.setCreatorIdName(SolrUtils.getIdName(creator.getId(), creator.getName()));
        }
        CustomFieldsUtils.setSolrDocDynamicFields(leaveRequestSolrDoc, customFields);
        return leaveRequestSolrDoc;
    }

    public Page<LeaveRequestSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = solrSort = Sort.by(Sort.Direction.DESC, SolrLeaveRequestConst.FIELD_CREATED_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = !fp.isAscending();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case LeaveRequestLisItem.EMPLOYEE_NAME ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_EMPLOYEE_NAME);
                    case LeaveRequestLisItem.REASON ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_REASON_NAME);
                    case LeaveRequestLisItem.CREATED_DATE ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_CREATED_DATE);
                    case LeaveRequestLisItem.FROM_DATE ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_START_DATE);
                    case LeaveRequestLisItem.TO_DATE ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_END_DATE);
                    case LeaveRequestLisItem.STATUS ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_STATUS_NAME);
                    case LeaveRequestLisItem.APPROVER ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_APPROVER_NAME);
                    case LeaveRequestLisItem.CODE ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_NUMBER_DATA);
                    case LeaveRequestLisItem.TYPE ->
                            solrSort = Sort.by(sortDirection, SolrLeaveRequestConst.FIELD_TYPE_NAME);
                    default ->
                            solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), true);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_LEAVE_REQUEST_CORE, query, LeaveRequestSolrDoc.class);
    }
}
