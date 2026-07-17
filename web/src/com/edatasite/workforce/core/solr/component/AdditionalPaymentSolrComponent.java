package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.solr.document.AdditionalPaymentSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.AdditionalPaymentSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
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
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_ADDITIONAL_PAYMENT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:23.
 */
@Component
public class AdditionalPaymentSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(AdditionalPaymentSolrComponent.class);

    @Autowired
    private AdditionalPaymentSolrDocRepository repository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsAdditionalPayment additionalPayment) throws InterruptedException {
        this.indexes(Arrays.asList(additionalPayment));
    }

    @Transactional
    public void indexes(List<EdsAdditionalPayment> additionalPayments) {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(additionalPayments)) {
            List<AdditionalPaymentSolrDoc> additionalPaymentSolrDocs = new ArrayList<>();

            for (EdsAdditionalPayment edsAdditionalPayment : additionalPayments) {
                if (edsAdditionalPayment != null) {
                    try {
                        additionalPaymentSolrDocs.add(createAdditionalPaymentDocument(edsAdditionalPayment.getRPC(), companyId));
                        log.info("Indexed AdditionalPayment Core CID - {}, objId - {}", companyId, edsAdditionalPayment.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* ERROR occurred while creating solr doc EdsAdditionalPayment = {} **********************", edsAdditionalPayment.getName());
                        throw e;
                    }
                }
            }

            if (!additionalPaymentSolrDocs.isEmpty()) {
                log.info("========= Create additional payment solr docs for company {} with size {} =========", companyId, additionalPaymentSolrDocs.size());
                repository.saveAll(additionalPaymentSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsAdditionalPayment> additionalPayments) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(additionalPayments)) {
            ConcurrentLinkedQueue<AdditionalPaymentSolrDoc> additionalPaymentSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();

            for (EdsAdditionalPayment edsAdditionalPayment : additionalPayments) {
                if (edsAdditionalPayment != null) {
                    AdditionalPayment rpc = edsAdditionalPayment.getRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(
                                    getSynchronizedKey(rpc), () -> {
                                        additionalPaymentSolrDocs.add(createAdditionalPaymentDocument(rpc, companyId));
                                        log.info("Indexed AdditionalPayment Core CID - {}, objId - {}", companId, edsAdditionalPayment.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* ERROR occurred while creating solr doc EdsAdditionalPayment = {} **********************\n", edsAdditionalPayment.getName());
                            log.error("ERROR: {}", e);
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
                log.error("Error on loading Additional Payment list", e);
            }

            if (!additionalPaymentSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create additional payment solr docs for company {} with size {} =========", companyId, additionalPaymentSolrDocs.size());
                    repository.saveAll(additionalPaymentSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving additional payment list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(AdditionalPayment rpc) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + rpc.getObjectID();
    }

    private AdditionalPaymentSolrDoc createAdditionalPaymentDocument(AdditionalPayment additionalPayment, Integer companyId) {
        AdditionalPaymentSolrDoc paymentSolrDoc = new AdditionalPaymentSolrDoc();

        paymentSolrDoc.setOid(SolrUtils.generatedOId(companyId, additionalPayment.getObjectID()));
        paymentSolrDoc.setCompanyId(companyId);
        paymentSolrDoc.setAdditionalPaymentId(additionalPayment.getObjectID());
        paymentSolrDoc.setReference(additionalPayment.getReference() != null ? additionalPayment.getReference() : null);

        if (additionalPayment.getCurrentApprover() != null) {
            paymentSolrDoc.setApproverId(additionalPayment.getCurrentApprover().getExactEmployee().getId());
            paymentSolrDoc.setApproverName(additionalPayment.getCurrentApprover().getExactEmployee().getName());
            paymentSolrDoc.setApproverIdName(SolrUtils.getIdName(additionalPayment.getCurrentApprover().getExactEmployee().getId(), additionalPayment.getCurrentApprover().getExactEmployee().getName()));
        }
        if (additionalPayment.getCreator() != null) {
            paymentSolrDoc.setCreatorId(additionalPayment.getCreator().getId());
            paymentSolrDoc.setCreatorName(additionalPayment.getCreator().getName());
            paymentSolrDoc.setCreatorIdName(SolrUtils.getIdName(additionalPayment.getCreator().getId(), additionalPayment.getCreator().getName()));
        }
        if (additionalPayment.getOverallStatus() != null) {
            paymentSolrDoc.setStatusId(additionalPayment.getOverallStatus().getObjectID());
            paymentSolrDoc.setStatusName(additionalPayment.getOverallStatus().getName());
            paymentSolrDoc.setStatusCode(additionalPayment.getOverallStatus().getCode());
            paymentSolrDoc.setStatusIdName(SolrUtils.getIdName(additionalPayment.getOverallStatus().getObjectID(), additionalPayment.getOverallStatus().getName()));
        }
        paymentSolrDoc.setType(additionalPayment.getType());
        paymentSolrDoc.setEntityType(additionalPayment.getEntityType());
        paymentSolrDoc.setTotalAmount(additionalPayment.getTotal() != null ? additionalPayment.getTotal().doubleValue() : 0);
        if (additionalPayment.getPayrollBatch() != null) {
            paymentSolrDoc.setPayrollGroupId(additionalPayment.getPayrollBatch().getId());
            paymentSolrDoc.setPayrollGroupName(additionalPayment.getPayrollBatch().getName());
        }
        String categoryLookUpName = "";
        if (additionalPayment.getPayrollBatch() != null) {
            categoryLookUpName = additionalPayment.getPayrollBatch().getName();
        } else if (additionalPayment.getDepartment() != null) {
            categoryLookUpName = additionalPayment.getDepartment().getName();
        } else if (additionalPayment.getLocation() != null) {
            categoryLookUpName = additionalPayment.getLocation().getName();
        } else if (additionalPayment.getSupervisor() != null) {
            categoryLookUpName = additionalPayment.getSupervisor().getName();
        }
        paymentSolrDoc.setCategoryLookupName(categoryLookUpName);

        if (additionalPayment.getDepartment() != null) {
            paymentSolrDoc.setPayrollDepartmentId(additionalPayment.getDepartment().getId());
            paymentSolrDoc.setPayrollDepartmentName(additionalPayment.getDepartment().getName());
        }
        paymentSolrDoc.setCreationDate(additionalPayment.getDate().getDate());
        paymentSolrDoc.setApprovedDate(additionalPayment.getApprovedDate() != null ? additionalPayment.getApprovedDate().getDate() : null);
        paymentSolrDoc.setMonthId(additionalPayment.getMonthID());
        paymentSolrDoc.setMonthName(additionalPayment.getMonth());
        paymentSolrDoc.setMonthIdName(SolrUtils.getIdName(additionalPayment.getMonthID(), additionalPayment.getMonth()));
        paymentSolrDoc.setYear(additionalPayment.getYear());
        paymentSolrDoc.setPdfTemplateId(additionalPayment.getPdfTemplateID());
        paymentSolrDoc.setLastUpdate(additionalPayment.getUpdatedTime() != null ? additionalPayment.getUpdatedTime().getDate() : null);
        paymentSolrDoc.setPaymentCategory(additionalPayment.getCategoryType());
        paymentSolrDoc.setDeleted(additionalPayment.isDeleted());
        return paymentSolrDoc;
    }

    public Page<AdditionalPaymentSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                solrSort = switch (fp.getSortField()) {
                    case "reference" -> Sort.by(order, SolrAdditionalPaymentPresenter.SORTABLE_REFERENCE);
                    case "period" ->
                            Sort.by(Arrays.asList(new Sort.Order(order, SolrAdditionalPaymentPresenter.FIELD_YEAR),
                                    new Sort.Order(order, SolrAdditionalPaymentPresenter.FIELD_MONTH_ID)));
                    case "approver" -> Sort.by(order, SolrAdditionalPaymentPresenter.SORTABLE_APPROVER_NAME);
                    case "total" -> Sort.by(order, SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT);
                    case "status" -> Sort.by(order, SolrAdditionalPaymentPresenter.FIELD_STATUS_NAME);
                    default -> Sort.by(Sort.Direction.DESC, SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID);
                };
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_ADDITIONAL_PAYMENT_CORE, query, AdditionalPaymentSolrDoc.class);
    }

    public FacetFilterRpc getAdditionalPaymentFacetFilterData(FacetFilterRpc additionalPaymentFacet) {
        if (!additionalPaymentFacet.isFilterChanges()) {
            additionalPaymentFacet = commonServiceLocal.getUserFacetFilter(additionalPaymentFacet);
        }
        EdsUser edsUser = userManager.getUser();
        EdsCompany company = edsUser.getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(additionalPaymentFacet.getSearchKey());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getAdditionalPaymentSolrQuery(fp, edsUser));
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(additionalPaymentFacet, FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(additionalPaymentFacet, company, null, null, FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4]));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_ADDITIONAL_PAYMENT_CORE, solrQuery.toString(), additionalPaymentFacet, AdditionalPaymentSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, additionalPaymentFacet);
        if (additionalPaymentFacet.getFacetContentMap().containsKey(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4])) {
            getAdditionalPaymentFacetResultFromSolr(facetPage, additionalPaymentFacet);
        }
        return additionalPaymentFacet;
    }

    private FacetFilterRpc getAdditionalPaymentFacetResultFromSolr(QueryResponse facetPage, FacetFilterRpc additionalPaymentFacet) {
        FacetField totalFacet = facetPage.getFacetField(SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT);
        if (totalFacet != null && totalFacet.getValues() != null) {
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : totalFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] total = new SelectItem[5];
            total[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            total[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            total[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            total[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            total[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            total[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            total[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            total[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            total[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            total[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            additionalPaymentFacet.getFacetContentMap().get(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4]).setFacetItems(total);
        } else {
            additionalPaymentFacet.getFacetContentMap().get(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4]).setFacetItems(new SelectItem[0]);
        }
        return additionalPaymentFacet;
    }
}
