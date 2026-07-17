package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsVacancyCustomFields;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.solr.document.VacancySolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.VacancySolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancySolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.hrms.server.db.JobFamilyManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:33.
 */
@Component
public class VacancySolrComponent {

    private static final Logger log = LoggerFactory.getLogger(VacancySolrComponent.class);

    @Autowired
    private VacancySolrDocRepository vacancySolrDocRepository;
    @Autowired
    private JobFamilyManager jobFamilyManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsVacancy edsVacancy) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsVacancy));
    }

    @Transactional
    public void indexes(List<EdsVacancy> edsVacancyList) throws IOException, SolrServerException, InterruptedException {

        if (!CollectionUtils.isEmpty(edsVacancyList)) {
            List<VacancySolrDoc> vacancySolrDocs = new ArrayList<>();

            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsVacancy edsVacancy : edsVacancyList) {
                if (edsVacancy != null) {
                    try {
                        vacancySolrDocs.add(createVacancyDocument(edsVacancy.getSolrRPC(), Integer.valueOf(companyId), edsVacancy.getVacancyCustomFields()));
                        log.info("Indexed Vacancy Core CID - {}, objId - {}", companyId, edsVacancy.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on Vacancy with id {}, and error message {} **********************", edsVacancy.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!vacancySolrDocs.isEmpty()) {
                log.info("========= Create Vacancy solr docs for company {} with size {} =========", companyId, vacancySolrDocs.size());
                vacancySolrDocRepository.saveAll(vacancySolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsVacancy> edsVacancyList) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsVacancyList)) {
            ConcurrentLinkedQueue<VacancySolrDoc> vacancySolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsVacancy edsVacancy : edsVacancyList) {
                if (edsVacancy != null) {
                    VacancySolrItem solrRPC = edsVacancy.getSolrRPC();
                    EdsVacancyCustomFields vacancyCustomFields = edsVacancy.getVacancyCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        vacancySolrDocs.add(createVacancyDocument(solrRPC, Integer.valueOf(companyId), vacancyCustomFields));
                                        log.info("Indexed Vacancy Core CID - {}, objId - {}", companyId, edsVacancy.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Vacancy with id {}, and error message {} **********************", edsVacancy.getObjectID(), e.getMessage());
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
                log.error("Error on loading Vacancy list", e);
            }

            if (!vacancySolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Vacancy solr docs for company {} with size {} =========", companyId, vacancySolrDocs.size());
                    vacancySolrDocRepository.saveAll(vacancySolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Vacancy list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(VacancySolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectID();
    }

    private VacancySolrDoc createVacancyDocument(VacancySolrItem vacancy, Integer companyId, EdsCustomFields customFields) {
        VacancySolrDoc vacancySolrDoc = new VacancySolrDoc();

        vacancySolrDoc.setOid(SolrUtils.generatedOId(companyId, vacancy.getObjectID()));
        vacancySolrDoc.setCompanyId(companyId);
        vacancySolrDoc.setVacancyId(vacancy.getObjectID());
        vacancySolrDoc.setVacancyNumber(vacancy.getVacancyNumber());

        EdsJobFamily jobFamily = jobFamilyManager.get(vacancy.getJobFamily());
        if (jobFamily != null) {
            vacancySolrDoc.setJobFamilyId(jobFamily.getObjectID());
            vacancySolrDoc.setJobFamilyName(jobFamily.getName());
            vacancySolrDoc.setJobFamilyNameId(SolrUtils.getIdName(jobFamily.getObjectID(), jobFamily.getName()));
        }

        EdsReference jobType = referenceManager.get(vacancy.getFullPartTime());
        if (jobType != null) {
            vacancySolrDoc.setJobTypeId(jobType.getObjectID());
            vacancySolrDoc.setJobTypeName(jobType.getName());
            vacancySolrDoc.setJobTypeNameId(SolrUtils.getIdName(jobType.getObjectID(), jobType.getName()));
        }
        vacancySolrDoc.setJobTitle(vacancy.getJobTitle());

        vacancySolrDoc.setNameEn(vacancy.getNameEn());
        vacancySolrDoc.setNameRu(vacancy.getNameRu());
        vacancySolrDoc.setNameAr(vacancy.getNameAr());
        vacancySolrDoc.setNameUz(vacancy.getNameUz());

        if (vacancy.getApprover() != null) {
            vacancySolrDoc.setApproverId(vacancy.getApprover().getId());
            vacancySolrDoc.setApproverName(vacancy.getApprover().getName());
            vacancySolrDoc.setApproverIdName(SolrUtils.getIdName(vacancy.getApprover().getId(), vacancy.getApprover().getName()));
        }

        if (vacancy.getVacancyStatus() != null) {
            ReferenceItem status = vacancy.getVacancyStatus();
            vacancySolrDoc.setVacancyStatusId(status.getObjectID());
            vacancySolrDoc.setVacancyStatus(status.getName());
            vacancySolrDoc.setVacancyStatusIdCode(SolrUtils.getIdName(status.getObjectID(), status.getCode()));
            vacancySolrDoc.setRdegreeStatusIdCodeName(SolrUtils.getIdCodeName(status.getObjectID(), status.getCode(), status.getName()));
            vacancySolrDoc.setVacancyStatusCode(status.getCode());
            vacancySolrDoc.setVacancyStatusSorder(status.getOrder());
        }

        if (vacancy.getRdegreeStatus() != null) {
            ReferenceItem rdegree = vacancy.getRdegreeStatus();
            vacancySolrDoc.setRdegreeStatusId(rdegree.getObjectID());
            vacancySolrDoc.setRdegreeStatus(rdegree.getName());
            vacancySolrDoc.setRdegreeStatusIdCode(SolrUtils.getIdName(rdegree.getObjectID(), rdegree.getCode()));
            vacancySolrDoc.setRdegreeStatusIdCodeName(SolrUtils.getIdCodeName(rdegree.getObjectID(), rdegree.getCode(), rdegree.getName()));
            vacancySolrDoc.setRdegreeStatusCode(rdegree.getCode());
            vacancySolrDoc.setRdegreeStatusSorder(rdegree.getOrder());
        }

        if (vacancy.getProject() != null) {
            SelectItem project = vacancy.getProject();
            vacancySolrDoc.setProjectId(project.getId());
            vacancySolrDoc.setProjectName(project.getName());
            vacancySolrDoc.setProjectIdName(SolrUtils.getIdName(project.getId(), project.getName()));
        }

        vacancySolrDoc.setGender(vacancy.getGender());
        vacancySolrDoc.setProposedSalary(vacancy.getProposedSalary());
        vacancySolrDoc.setJobRequirements(vacancy.getJobRequirements());
        vacancySolrDoc.setContractFrom(vacancy.getContractFrom());
        vacancySolrDoc.setContractTo(vacancy.getContractTo());
        vacancySolrDoc.setVacancyType(vacancy.getVacancyType().getId());
        vacancySolrDoc.setVacancyTypeName(vacancy.getVacancyType().getName());

        if (vacancy.getManager() != null) {
            SelectItem manager = vacancy.getManager();
            vacancySolrDoc.setManagerId(manager.getId());
            vacancySolrDoc.setManagerName(manager.getName());
            vacancySolrDoc.setManagerIdName(SolrUtils.getIdName(manager.getId(), manager.getName()));
        }

        if (vacancy.getPosition() != null) {
            SelectItem position = vacancy.getPosition();
            vacancySolrDoc.setPositionId(position.getId());
            vacancySolrDoc.setPositionName(position.getName());
            vacancySolrDoc.setPositionIdName(SolrUtils.getIdName(position.getId(), position.getName()));
        }

        if (vacancy.getLocation() != null) {
            SelectItem location = vacancy.getLocation();
            vacancySolrDoc.setLocationId(location.getId());
            vacancySolrDoc.setLocationName(location.getName());
            vacancySolrDoc.setLocationIdName(SolrUtils.getIdName(location.getId(), location.getName()));
        }

        if (vacancy.getCurrency() != null) {
            SelectItem currency = vacancy.getCurrency();
            vacancySolrDoc.setCurrencyId(currency.getId());
            vacancySolrDoc.setCurrencyName(currency.getName());
            vacancySolrDoc.setCurrencyIdName(SolrUtils.getIdName(currency.getId(), currency.getName()));
        }

        vacancySolrDoc.setStartDate(vacancy.getStartDate());
        vacancySolrDoc.setEndDate(vacancy.getEndDate());
        vacancySolrDoc.setCreatedDate(vacancy.getCreatedDate());
        vacancySolrDoc.setLastUpdateDate(vacancy.getLastUpdateDate());
        vacancySolrDoc.setCreatedBy(vacancy.getCreatedBy());
        vacancySolrDoc.setModifiedBy(vacancy.getModifiedBy());

        if (customFields != null) {
            CustomFieldsUtils.setSolrDocDynamicFields(vacancySolrDoc, customFields);
        }
        return vacancySolrDoc;
    }

    public Page<VacancySolrDoc> getList(ListingFilterParameter fp) {
        EdsUser user = vacancyManager.getUser();
        EdsCompany company = user.getCompany();

        FacetFilterRpc vacancyFacetFilter = fp.getFacetFilter();
        if (vacancyFacetFilter != null && !vacancyFacetFilter.isFilterChanges()) {
            vacancyFacetFilter = commonServiceLocal.getUserFacetFilter(vacancyFacetFilter);
        }

        SimpleQuery solrQuery = new SimpleQuery();
        String facetFilterQuery = QueryBuilderForSolr.getVacancySolrQuery(fp, user, company);
        facetFilterQuery += SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(vacancyFacetFilter, company,
                SolrVacancyRepresenter.FIELD_START_DATE, SolrVacancyRepresenter.FIELD_END_DATE);
        if (!StringUtils.isEmpty(facetFilterQuery)) {
            solrQuery.addCriteria(new SimpleStringCriteria(facetFilterQuery));
        }

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrVacancyRepresenter.FIELD_LAST_UPDATE_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = !fp.isAscending();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                solrSort = switch (fp.getSortField()) {
                    case VacancyItem.VACANCY_ID ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_VACANCY_NUMBER);
                    case VacancyItem.VACANCY_JOB_TITLE ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_JOB_TITLE);
                    case VacancyItem.VACANCY_STATUS ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_VACANCY_STATUS);
                    case VacancyItem.VACANCY_PROPOSED_SALARY ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_PROPOSED_SALARY);
                    case VacancyItem.VACANCY_GENDER -> Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_GENDER);
                    case VacancyItem.VACANCY_JOB_REQUIREMENT ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_JOB_REQUIREMENTS);
                    case VacancyItem.VACANCY_CONTRACT_FROM ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_CONTRACT_FROM);
                    case VacancyItem.VACANCY_CONTRACT_TO ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_CONTRACT_TO);
                    case VacancyItem.VACANCY_TYPE -> Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_VACANCY_TYPE);
                    case VacancyItem.VACANCY_START_DATE ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_START_DATE);
                    case VacancyItem.VACANCY_END_DATE -> Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_END_DATE);
                    case VacancyItem.VACANCY_MANAGER ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_MANAGER_NAME);
                    case VacancyItem.VACANCY_POSITION ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_POSITION_NAME);
                    case VacancyItem.VACANCY_JOB_TYPE ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_JOB_TYPE_NAME);
                    case VacancyItem.VACANCY_JOB_FAMILY ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_JOB_FAMILY_NAME);
                    case VacancyItem.VACANCY_REQUIRED_DEGREE ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_RDEGREE_STATUS);
                    case VacancyItem.VACANCY_DEPARTMENT ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_DEPARTMENT_NAME);
                    case VacancyItem.VACANCY_CURRENCY ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_CURRENCY);
                    case VacancyItem.VACANCY_MODIFIED_BY ->
                            Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_MODIFIED_BY);
                    default -> Sort.by(sortDirection, SolrVacancyRepresenter.FIELD_SORTABLE_LAST_UPDATE_DATE);
                };
            }
        }

        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        solrQuery.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(Constants.SOLR_VACANCY_CORE, solrQuery, VacancySolrDoc.class);
    }

    public FacetFilterRpc getVacancyFacetFilterData(FacetFilterRpc vacancyFacetData) {
        if (!vacancyFacetData.isFilterChanges()) {
            vacancyFacetData = commonServiceLocal.getUserFacetFilter(vacancyFacetData);
        }
        EdsUser edsUser = companyManager.getUser();
        EdsCompany company = edsUser.getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(vacancyFacetData.getSearchKey());
        fp.setStartDate(vacancyFacetData.getStartDate());
        fp.setEndDate(vacancyFacetData.getEndDate());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getVacancySolrQuery(fp, edsUser, company));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(vacancyFacetData, company,
                SolrVacancyRepresenter.FIELD_START_DATE,
                SolrVacancyRepresenter.FIELD_END_DATE));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_VACANCY_CORE, solrQuery.toString(), vacancyFacetData, VacancySolrDoc.class);

        return SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, vacancyFacetData);
    }
}
