package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeStepCustomFields;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.solr.document.EmployeeStepSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.EmployeeStepSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
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
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
@Component
public class EmployeeStepSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(EmployeeStepSolrComponent.class);

    @Autowired
    private EmployeeStepSolrDocRepository employeeStepSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsStepEmployee edsStepEmployee) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsStepEmployee));
    }

    @Transactional
    public void indexes(List<EdsStepEmployee> edsStepEmployeeList) throws IOException, SolrServerException, InterruptedException {

        if (!CollectionUtils.isEmpty(edsStepEmployeeList)) {
            List<EmployeeStepSolrDoc> employeeStepSolrDocs = new ArrayList<>();

            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsStepEmployee edsStepEmployee : edsStepEmployeeList) {
                if (edsStepEmployee != null && !edsStepEmployee.isDeleted()) {
                    try {
                        employeeStepSolrDocs.add(createEmployeeStepDocument(edsStepEmployee.getSolrRPC(), Integer.valueOf(companyId), edsStepEmployee.getEmployeeStepCustomFields()));
                        log.info("Indexed EmployeeStep Core CID - {}, objId - {}", companyId, edsStepEmployee.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on EmployeeStep Core with id {}, and error message {} **********************", edsStepEmployee.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!employeeStepSolrDocs.isEmpty()) {
                log.info("========= Create EmployeeStep solr docs for company {} with size {} =========", companyId, employeeStepSolrDocs.size());
                employeeStepSolrDocRepository.saveAll(employeeStepSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsStepEmployee> edsStepEmployeeList) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsStepEmployeeList)) {
            ConcurrentLinkedQueue<EmployeeStepSolrDoc> employeeStepSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsStepEmployee edsStepEmployee : edsStepEmployeeList) {
                if (edsStepEmployee != null && !edsStepEmployee.isDeleted()) {
                    EmployeeStepSolrItem solrRPC = edsStepEmployee.getSolrRPC();
                    EdsEmployeeStepCustomFields employeeStepCustomFields = edsStepEmployee.getEmployeeStepCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        employeeStepSolrDocs.add(createEmployeeStepDocument(solrRPC, Integer.valueOf(companyId), employeeStepCustomFields));
                                        log.info("Indexed EmployeeStep Core CID - {}, objId - {}", companyId, edsStepEmployee.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on EmployeeStep Core with id {}, and error message {} **********************", edsStepEmployee.getObjectID(), e.getMessage());
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
                log.error("Error on loading EmployeeStep list", e);
            }

            if (!employeeStepSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create EmployeeStep solr docs for company {} with size {} =========", companyId, employeeStepSolrDocs.size());
                    employeeStepSolrDocRepository.saveAll(employeeStepSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving EmployeeStep list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EmployeeStepSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectId();
    }

    private EmployeeStepSolrDoc createEmployeeStepDocument(EmployeeStepSolrItem stepEmployee, Integer companyId, EdsCustomFields customFields) {
        EmployeeStepSolrDoc employeeStepSolrDoc = new EmployeeStepSolrDoc();

        employeeStepSolrDoc.setOid(SolrUtils.generatedOId(companyId, stepEmployee.getObjectId()));
        employeeStepSolrDoc.setCompanyId(companyId);
        employeeStepSolrDoc.setStepId(stepEmployee.getObjectId());
        employeeStepSolrDoc.setWorkflowId(stepEmployee.getWorkflowId());

        if (stepEmployee.getOnboardingStep() != null) {
            SelectItem onboardingStep = stepEmployee.getOnboardingStep();
            employeeStepSolrDoc.setOnboardingStepId(onboardingStep.getId());
            employeeStepSolrDoc.setOnboardingStepFormId(stepEmployee.getOnboardingStepFormId());
            employeeStepSolrDoc.setOnboardingStepName(onboardingStep.getName());
            employeeStepSolrDoc.setOnboardingStepIdName(SolrUtils.getIdName(onboardingStep.getId(), onboardingStep.getName()));
        }

        if (stepEmployee.getEmployee() != null) {
            SelectItem employee = stepEmployee.getEmployee();
            employeeStepSolrDoc.setEmployeeId(employee.getId());
            employeeStepSolrDoc.setEmployeeName(employee.getName());
            if (employee.getCode() != null) {
                employeeStepSolrDoc.setEmployeeCode(employee.getCode());
            }
            employeeStepSolrDoc.setEmployeeIdName(SolrUtils.getIdName(employee.getId(), employee.getName()));
            if (stepEmployee.getEmployeeLocation() != null) {
                SelectItem location = stepEmployee.getEmployeeLocation();
                employeeStepSolrDoc.setEmployeeLocationId(location.getId());
                employeeStepSolrDoc.setEmployeeLocationName(location.getName());
                employeeStepSolrDoc.setEmployeeLocationIdName(SolrUtils.getIdName(location.getId(), location.getName()));
                if (stepEmployee.getEmployeeLocationState() != null) {
                    employeeStepSolrDoc.setEmployeeLocationState(stepEmployee.getEmployeeLocationState());
                }
                employeeStepSolrDoc.setEmployeeLocationCity(stepEmployee.getEmployeeLocationCity());
            }
        }

        employeeStepSolrDoc.setCreationDate(stepEmployee.getCreationDate());
        employeeStepSolrDoc.setModificationDate(stepEmployee.getModificationDate());

        if (stepEmployee.getCreator() != null) {
            employeeStepSolrDoc.setCreatorId(stepEmployee.getCreator().getId());
            employeeStepSolrDoc.setCreatorName(stepEmployee.getCreator().getName());
            employeeStepSolrDoc.setCreatorIdName(SolrUtils.getIdName(stepEmployee.getCreator().getId(), stepEmployee.getCreator().getName()));
        }

        if (stepEmployee.getStatus() != null) {
            SelectItem status = stepEmployee.getStatus();
            employeeStepSolrDoc.setStatusId(status.getId());
            employeeStepSolrDoc.setStatusName(status.getName());
            employeeStepSolrDoc.setStatusIdName(SolrUtils.getIdName(status.getId(), status.getName()));
        }
        if (stepEmployee.getType() != null) {
            SelectItem type = stepEmployee.getType();
            employeeStepSolrDoc.setTypeId(type.getId());
            employeeStepSolrDoc.setTypeCode(type.getCode());
            employeeStepSolrDoc.setTypeName(type.getName());
            employeeStepSolrDoc.setTypeIdName(SolrUtils.getIdName(type.getId(), type.getName()));
        }

        if (stepEmployee.getCurrentApprover() != null) {
            employeeStepSolrDoc.setCurrentApproverId(stepEmployee.getCurrentApprover().getId());
            employeeStepSolrDoc.setApproverApproveStatusId(stepEmployee.getApproverApproveStatusId());
            employeeStepSolrDoc.setApproverRejectStatusId(stepEmployee.getApproverRejectStatusId());
        }
        employeeStepSolrDoc.setArchived(stepEmployee.getArchived());

        if (customFields != null) {
            CustomFieldsUtils.setSolrDocDynamicFields(employeeStepSolrDoc, customFields);
        }
        return employeeStepSolrDoc;
    }

    public Page<EmployeeStepSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrEmployeeStepRepresenter.FIELD_MODIFICATION_DATE);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                String sortField = SolrEmployeeStepRepresenter.getSortField(filterParameter.getSortField());
                if (sortField != null) {
                    solrSort = Sort.by(sortDirection, sortField);
                } else {
                    solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), desc, true);
                }
            }
        }

        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_EMPLOYEE_STEP_CORE, query, EmployeeStepSolrDoc.class);
    }

    public FacetFilterRpc getEmployeeStepFacetFilterData(ListingFilterParameter fp, FacetFilterRpc employeeStepFacet) {
        if (!employeeStepFacet.isFilterChanges()) {
            employeeStepFacet = commonServiceLocal.getUserFacetFilter(employeeStepFacet);
        }

        EdsUser user = companyManager.getUser();

        fp = fp == null ? new ListingFilterParameter() : fp;
        fp.setSearchKey(employeeStepFacet.getSearchKey());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getEmployeeStepCoreSolrQuery(user, fp));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(employeeStepFacet, user.getCompany(),
                SolrEmployeeStepRepresenter.FIELD_CREATION_DATE,
                SolrEmployeeStepRepresenter.FIELD_MODIFICATION_DATE));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_EMPLOYEE_STEP_CORE, solrQuery.toString(), employeeStepFacet, EmployeeStepSolrDoc.class);

        return SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, employeeStepFacet);
    }
}
