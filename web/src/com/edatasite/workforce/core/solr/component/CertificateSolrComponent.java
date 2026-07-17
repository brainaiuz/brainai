package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.customfields.EdsCertificateCustomFields;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.solr.document.CertificateSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.CertificateSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCertificateRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateSolrItem;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.response.QueryResponse;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CERTIFICATE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST;

@Component
public class CertificateSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CertificateSolrComponent.class);

    @Autowired
    private CertificateSolrDocRepository certificateSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    private static Map<String, Double> getCertificateSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrCertificateRepresenter.FIELD_NUMBER, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_EMPLOYEE_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_ISSUED_BY_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_TYPE_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_EMPLOYEE_CODE, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_CURRENT_APPROVER_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_STATUS_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    @Transactional
    public void index(EdsCertificateOfEmployment certificate) throws InterruptedException {
        this.indexes(Arrays.asList(certificate));
    }

    @Transactional
    public void indexes(List<EdsCertificateOfEmployment> certificates) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(certificates)) {
            List<CertificateSolrDoc> certificateSolrDocs = new ArrayList<>();

            for (EdsCertificateOfEmployment certificate : certificates) {
                if (certificate != null) {
                    try {
                        certificateSolrDocs.add(createCertificateDocument(certificate.getSolrRPC(), companyId, certificate.getCustomFields()));
                        log.info("Indexed Certificate Core CID - {}, objId - {}", companyId, certificate.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on EdsCertificateOfEmployment with id {}, and error message {} **********************", certificate.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!certificateSolrDocs.isEmpty()) {
                log.info("========= Create EdsCertificateOfEmployment solr docs for company {} with size {} =========", companyId, certificateSolrDocs.size());
                certificateSolrDocRepository.saveAll(certificateSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCertificateOfEmployment> certificates) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(certificates)) {
            ConcurrentLinkedQueue<CertificateSolrDoc> certificateSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCertificateOfEmployment certificate : certificates) {
                if (certificate != null) {
                    CertificateSolrItem solrRPC = certificate.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        certificateSolrDocs.add(createCertificateDocument(solrRPC, companyId, certificate.getCustomFields()));
                                log.info("Indexed Certificate Core CID - {}, objId - {}", companId, certificate.getObjectID());
                            }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on EdsCertificateOfEmployment with id {}, and error: {} **********************", certificate.getObjectID(), e);
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
                log.error("Error on loading Certificate list", e);
            }

            if (!certificateSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create EdsCertificateOfEmployment solr docs for company {} with size {} =========", companId, certificateSolrDocs.size());
                    certificateSolrDocRepository.saveAll(certificateSolrDocs);
                } catch (Exception e) {
                    log.error("********************* Error on saving Certificate list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(CertificateSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectId();
    }

    private CertificateSolrDoc createCertificateDocument(CertificateSolrItem certificate, Integer companyId, EdsCertificateCustomFields customFields) {
        CertificateSolrDoc certificateSolrDoc = new CertificateSolrDoc();
        certificateSolrDoc.setOid(SolrUtils.generatedOId(companyId, certificate.getObjectId()));
        certificateSolrDoc.setCompanyId(companyId);
        certificateSolrDoc.setCertificateId(certificate.getObjectId());
        certificateSolrDoc.setNumber(certificate.getNumber());

        if (certificate.getEmployee() != null) {
            certificateSolrDoc.setEmployeeId(certificate.getEmployee().getId());
            certificateSolrDoc.setEmployeeName(certificate.getEmployee().getName());
            certificateSolrDoc.setEmployeeCode(certificate.getEmployee().getCode() != null ? certificate.getEmployee().getCode() : "");
            certificateSolrDoc.setEmployeeIdName(SolrUtils.getIdName(certificate.getEmployee().getId(), certificate.getEmployee().getName()));
        }

        if (certificate.getType() != null) {
            certificateSolrDoc.setTypeId(certificate.getType().getId());
            certificateSolrDoc.setTypeName(certificate.getType().getName());
            certificateSolrDoc.setTypeIdName(SolrUtils.getIdName(certificate.getType().getId(), certificate.getType().getName()));
        }

        if (certificate.getCurrentApprover() != null) {
            certificateSolrDoc.setCurrentApproverId(certificate.getCurrentApprover().getId());
            certificateSolrDoc.setCurrentApproverName(certificate.getCurrentApprover().getName());
            certificateSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(certificate.getCurrentApprover().getId(), certificate.getCurrentApprover().getName()));
        }

        if (certificate.getStatus() != null) {
            certificateSolrDoc.setStatusId(certificate.getStatus().getId());
            certificateSolrDoc.setStatusName(certificate.getStatus().getName());
            certificateSolrDoc.setStatusIdName(SolrUtils.getIdName(certificate.getStatus().getId(), certificate.getStatus().getName()));
        }

        certificateSolrDoc.setCreatedDate(certificate.getCreatedDate());
        if (certificate.getCreatedBy() != null) {
            certificateSolrDoc.setCreatedById(certificate.getCreatedBy().getId());
            certificateSolrDoc.setCreatedByName(certificate.getCreatedBy().getName());
            certificateSolrDoc.setCreatedByIdName(SolrUtils.getIdName(certificate.getCreatedBy().getId(), certificate.getCreatedBy().getName()));
        }
        certificateSolrDoc.setIssuedDate(certificate.getIssuedDate());
        if (certificate.getIssuedBy() != null) {
            certificateSolrDoc.setIssuedById(certificate.getIssuedBy().getId());
            certificateSolrDoc.setIssuedByName(certificate.getIssuedBy().getName());
            certificateSolrDoc.setIssuedByIdName(SolrUtils.getIdName(certificate.getIssuedBy().getId(), certificate.getIssuedBy().getName()));
        }

        CustomFieldsUtils.setSolrDocDynamicFields(certificateSolrDoc, customFields);
        return certificateSolrDoc;
    }

    public FacetFilterRpc getCertificateFilterData(FacetFilterRpc facetFilterRpc) {

        EdsUser user = userManager.getUser();
        if (!facetFilterRpc.isFilterChanges()) {
            facetFilterRpc = commonServiceLocal.getUserFacetFilter(facetFilterRpc);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(facetFilterRpc.getSearchKey());
        fp.setViewType(facetFilterRpc.getName());

        String solrQuery = getCertificateFacetQuery(fp, user) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilterRpc, company, null, null);
        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CERTIFICATE_CORE, solrQuery.toString(), facetFilterRpc, CertificateSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, facetFilterRpc);

        return facetFilterRpc;
    }

    public Page<CertificateSolrDoc> getList(ListingFilterParameter fp) {
        FacetFilterRpc certificateFacetFilter = fp.getFacetFilter();
        if (certificateFacetFilter != null && !certificateFacetFilter.isFilterChanges()) {
            certificateFacetFilter = commonServiceLocal.getUserFacetFilter(certificateFacetFilter);
        }

        if (certificateFacetFilter != null) {
            if (certificateFacetFilter.getSearchKey() != null && !"".equals(certificateFacetFilter.getSearchKey())) {
                fp.setSearchKey(certificateFacetFilter.getSearchKey());
            }
            fp.setFacetFilter(certificateFacetFilter);
        }

        EdsUser edsUser = employeeManager.getUser();

        String solrQuery = null;
        if (edsUser != null && edsUser.getCompany().getObjectID() != null) {
            solrQuery = getCertificateFacetQuery(fp, edsUser) + SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(certificateFacetFilter, edsUser.getCompany(),
                    SolrSaleInvoiceRepresenter.FIELD_CREATION_DATE, null);
        }

        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrCertificateRepresenter.FIELD_CERTIFICATE_ID);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction sortDirection = fp.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                solrSort = switch (fp.getSortField()) {
                    case CertificateItem.NUMBER -> Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_NUMBER);
                    case CertificateItem.EMPLOYEE_CODE ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_EMPLOYEE_CODE);
                    case CertificateItem.EMPLOYEE ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_EMPLOYEE_NAME);
                    case CertificateItem.CERTIFICATE_TYPE ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_TYPE_NAME);
                    case CertificateItem.ISSUED_DATE ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_ISSUED_DATE);
                    case CertificateItem.ISSUED_BY ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_ISSUED_BY_NAME);
                    case CertificateItem.CREATED_DATE ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_CREATED_DATE);
                    case CertificateItem.CREATED_BY ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_CREATED_BY_NAME);
                    case CertificateItem.APPROVER ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_CURRENT_APPROVER_NAME);
                    case CertificateItem.STATUS ->
                            Sort.by(sortDirection, SolrCertificateRepresenter.SORTABLE_STATUS_NAME);
                    default ->
                            CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), true);
                };
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));
        return solrTemplate.query(SOLR_CERTIFICATE_CORE, query, CertificateSolrDoc.class);
    }

    public String getCertificateFacetQuery(final ListingFilterParameter fp, final EdsUser user) {
        final StringBuffer sql = new StringBuffer();
        sql.append(SolrCertificateRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        if (!ServerUtils.hasPermission(CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST)) {
            sql.append(" AND (").append(SolrCertificateRepresenter.FIELD_EMPLOYEE_ID).append(":").append(user.getObjectID()).append(" OR ")
                    .append(SolrCertificateRepresenter.FIELD_ISSUED_BY_ID).append(":").append(user.getObjectID()).append(")");
        }

        if (fp.getStartDate() != null) {
            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sql.append(" AND ").append(SolrCertificateRepresenter.FIELD_ISSUED_DATE).append(":[").append(format.format(fp.getStartDate())).append(" TO * ] ");
        }

        if (fp.getEmployeeId() != null) {
            sql.append(" AND ").append(SolrCertificateRepresenter.FIELD_EMPLOYEE_ID).append(":").append(fp.getEmployeeId());
        }

        if (fp.getType() != null) {
            sql.append(" AND ").append(SolrCertificateRepresenter.FIELD_TYPE_ID).append(":").append(fp.getType());
        }

        if (fp.getCreatedFrom() != null) {
            sql.append(" AND ").append(SolrCertificateRepresenter.FIELD_ISSUED_BY_ID).append(":").append(fp.getCreatedFrom());
        }

        if (fp.getAccountCode() != null) {
            sql.append(" AND ").append(SolrCertificateRepresenter.FIELD_EMPLOYEE_CODE).append(":").append(fp.getAccountCode());
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ").append(SolrCertificateRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            if (!fp.isLookUp()) {
                final SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getCertificateSearchFields(), fp.getSearchKey());
            }
            sql.append(")");
        }
        return sql.toString();
    }
}
