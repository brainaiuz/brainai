package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRFQCustomFields;
import com.edatasite.workforce.core.solr.document.RequestForQuoteSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.RequestForQuoteSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.PurchaseOrderSolrItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQSolrItem;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

@Component
public class RequestForQuoteSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(RequestForQuoteSolrComponent.class);

    @Autowired
    private RequestForQuoteSolrDocRepository requestForQuoteSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsRFQ edsRFQ) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsRFQ));
    }

    @Transactional
    public void indexes(List<EdsRFQ> edsRFQList) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsRFQList)) {
            List<RequestForQuoteSolrDoc> requestForQuoteSolrDocs = new ArrayList<>();

            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsRFQ edsRFQ : edsRFQList) {
                if (edsRFQ != null) {
                    try {
                        requestForQuoteSolrDocs.add(createRFQDocument(edsRFQ.getSolrRPC(), Integer.valueOf(companyId), edsRFQ.getCustomFields()));
                        log.info("Indexed RequestForQuote Core CID - {}, objId - {}", companyId, edsRFQ.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on RequestForQuote with id {}, and error message {} **********************", edsRFQ.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!requestForQuoteSolrDocs.isEmpty()) {
                log.info("========= Create RequestForQuote solr docs for company {} with size {} =========", companyId, requestForQuoteSolrDocs.size());
                requestForQuoteSolrDocRepository.saveAll(requestForQuoteSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsRFQ> edsRFQList) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsRFQList)) {
            ConcurrentLinkedQueue<RequestForQuoteSolrDoc> requestForQuoteSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsRFQ edsRFQ : edsRFQList) {
                if (edsRFQ != null) {
                    RFQSolrItem solrRPC = edsRFQ.getSolrRPC();
                    EdsRFQCustomFields customFields = edsRFQ.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        requestForQuoteSolrDocs.add(createRFQDocument(solrRPC, Integer.valueOf(companyId), customFields));
                                        log.info("Indexed RequestForQuote Core CID - {}, objId - {}", companyId, edsRFQ.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on RequestForQuote with id {}, and error message {} **********************", edsRFQ.getObjectID(), e.getMessage());
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
                log.error("Error on loading RequestForQuote list", e);
            }

            if (!requestForQuoteSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create RequestForQuote solr docs for company {} with size {} =========", companyId, requestForQuoteSolrDocs.size());
                    requestForQuoteSolrDocRepository.saveAll(requestForQuoteSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving RequestForQuote list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(RFQSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectID();
    }

    private RequestForQuoteSolrDoc createRFQDocument(RFQSolrItem rfq, Integer companyId, EdsCustomFields customFields) {
        RequestForQuoteSolrDoc requestForQuoteSolrDoc = new RequestForQuoteSolrDoc();
        requestForQuoteSolrDoc.setOid(SolrUtils.generatedOId(companyId, rfq.getObjectID()));
        requestForQuoteSolrDoc.setCompanyId(companyId);
        requestForQuoteSolrDoc.setRfqId(rfq.getObjectID());
        requestForQuoteSolrDoc.setRfqNumber(rfq.getRfqNumber());
        if (rfq.getClient() != null) {
            requestForQuoteSolrDoc.setClientId(rfq.getClient().getId());
            requestForQuoteSolrDoc.setClientName(rfq.getClient().getName());
            requestForQuoteSolrDoc.setClientIdName(SolrUtils.getIdName(rfq.getClient().getId(), rfq.getClient().getName()));
            if (rfq.getCountry() != null) {
                requestForQuoteSolrDoc.setCountryId(rfq.getCountry().getId());
                requestForQuoteSolrDoc.setCountryName(rfq.getCountry().getName());
                requestForQuoteSolrDoc.setCountryIdName(SolrUtils.getIdName(rfq.getCountry().getId(), rfq.getCountry().getName()));
            }
        }
        if (rfq.getRelatedProject() != null) {
            requestForQuoteSolrDoc.setRelatedProjectId(rfq.getRelatedProject().getId());
            requestForQuoteSolrDoc.setRelatedProjectName(rfq.getRelatedProject().getName());
            requestForQuoteSolrDoc.setRelatedProjectNumber(rfq.getRelatedProject().getNumber());
            requestForQuoteSolrDoc.setRelatedProjectIdName(SolrUtils.getIdName(rfq.getRelatedProject().getId(), rfq.getRelatedProject().getName()));
        }
        if (rfq.getStatus() != null) {
            requestForQuoteSolrDoc.setStatusId(rfq.getStatus().getObjectID());
            requestForQuoteSolrDoc.setStatusName(rfq.getStatus().getName());
            requestForQuoteSolrDoc.setStatusIdName(SolrUtils.getIdName(rfq.getStatus().getObjectID(), rfq.getStatus().getName()));
            requestForQuoteSolrDoc.setStatusSorder(rfq.getStatus().getOrder());
            requestForQuoteSolrDoc.setStatusCode(rfq.getStatus().getCode());
        }
        if (rfq.getCreator() != null) {
            requestForQuoteSolrDoc.setCreatorId(rfq.getCreator().getId());
            requestForQuoteSolrDoc.setCreatorName(rfq.getCreator().getName());
            requestForQuoteSolrDoc.setCreatorIdName(SolrUtils.getIdName(rfq.getCreator().getId(), rfq.getCreator().getName()));
        }

        if (rfq.getCurrentApprover() != null) {
            requestForQuoteSolrDoc.setCurrentApproverId(rfq.getCurrentApprover().getId());
            requestForQuoteSolrDoc.setCurrentApproverName(rfq.getCurrentApprover().getName());
            requestForQuoteSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(rfq.getCurrentApprover().getId(), rfq.getCurrentApprover().getName()));
        }

        if (!rfq.getItemIds().isEmpty()) {
            requestForQuoteSolrDoc.getItemId().addAll(rfq.getItemIds());
        }

        requestForQuoteSolrDoc.setDueDate(rfq.getDueDate());
        requestForQuoteSolrDoc.setRfqDate(rfq.getRfqDate());
        if (rfq.getCreationDate() != null) {
            requestForQuoteSolrDoc.setCreationDate(rfq.getCreationDate());
        }
        CustomFieldsUtils.setSolrDocDynamicFields(requestForQuoteSolrDoc, customFields);

        return requestForQuoteSolrDoc;
    }

    public FacetFilterRpc getRFQFacetFilterData(FacetFilterRpc rfqFacet) {

        if (!rfqFacet.isFilterChanges()) {
            rfqFacet = commonServiceLocal.getUserFacetFilter(rfqFacet);
        }

        rfqFacet = applyNonConvertedFilterPeriod(rfqFacet);

        StringBuilder solrQuery = new StringBuilder();
        String selectedDate = null;
        if (rfqFacet.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(rfqFacet.getSelectedDateSolrCodeName()) ? "DUE_DATE" :
                    "RFQ_DATE".equals(rfqFacet.getSelectedDateSolrCodeName()) ? "RFQ_DATE" : null;
        }
        EdsUser edsUser = employeeManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(rfqFacet.getSearchKey());
        fp.setStartDate(rfqFacet.getStartDate());
        fp.setEndDate(rfqFacet.getEndDate());
        fp.setFacetFilter(rfqFacet);
        solrQuery.append(quoteServiceLocal.getRFQSolrQuery(fp, edsUser, selectedDate));
        solrQuery.append(
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                        rfqFacet, edsUser.getCompany(), SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE,
                        SolrSaleInvoiceRepresenter.FIELD_DUE_DATE));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_REQUEST_FOR_QUOTE_CORE, solrQuery.toString(), rfqFacet, RequestForQuoteSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, rfqFacet);
        return rfqFacet;
    }

    private FacetFilterRpc applyNonConvertedFilterPeriod(FacetFilterRpc facet) {
        HashMap<String, String> customData = facet.getCustomData();
        if (customData.get(Constants.STARTDATE_NC) != null) {
            facet.setStartDate(ServerUtils.parseFilterParameterDate(customData.get(Constants.STARTDATE_NC)));
        }
        if (customData.get(Constants.ENDDATE_NC) != null) {
            facet.setEndDate(ServerUtils.parseFilterParameterDate(customData.get(Constants.ENDDATE_NC)));
        }
        return facet;
    }

    public Page<RequestForQuoteSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrSaleInvoiceRepresenter.FIELD_RFQ_ID);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                solrSort = switch (filterParameter.getSortField()) {
                    case RFQData.REQUEST_NUMBER -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_RFQ_NUMBER);
                    case RFQData.DATE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_RFQ_DATE);
                    case RFQData.STATUS -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME);
                    case RFQData.OPPORTUNITY_NAME ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME);
                    case RFQData.PROJECT ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME);
                    case RFQData.APPROVER ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_CURRENT_APPROVER_NAME);
                    default ->
                            CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
                };
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));
        return solrTemplate.query(SOLR_REQUEST_FOR_QUOTE_CORE, query, RequestForQuoteSolrDoc.class);
    }
}
