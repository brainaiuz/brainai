package com.edatasite.workforce.core.solr.component;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.solr.document.SaleQuoteSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.SaleQuoteSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQSolrItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaleQuoteSolrItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:31.
 */
@Component
public class SaleQuoteSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(SaleQuoteSolrComponent.class);

    @Autowired
    private SaleQuoteSolrDocRepository saleQuoteSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private PickListManager pickListManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsSaleQuote edsSaleQuote) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsSaleQuote), null);
    }

    @Transactional
    public void indexes(List<EdsSaleQuote> edsSaleQuoteList, List<EdsPickList> pickList) throws IOException, SolrServerException, InterruptedException {

        if (!CollectionUtils.isEmpty(edsSaleQuoteList)) {
            List<SaleQuoteSolrDoc> saleQuoteSolrDocs = new ArrayList<>();

            Integer companyID = SecurityContext.getCompanyID();
            Map<Integer, Integer> quoteByPickListMap = new ConcurrentHashMap();
            if (pickList != null && !pickList.isEmpty()) {
                for (EdsPickList pl : pickList) {
                    quoteByPickListMap.put(pl.getSaleQuote().getObjectID(), pl.getObjectID());
                }
            }
            for (EdsSaleQuote edsSaleQuote : edsSaleQuoteList) {
                if (edsSaleQuote != null && !edsSaleQuote.isDeleted()) {
                    try {
                        saleQuoteSolrDocs.add(createSaleQuoteDocument(edsSaleQuote.getSolrRPC(), Integer.valueOf(companyID), quoteByPickListMap.get(edsSaleQuote.getObjectID()), edsSaleQuote.getCustomFields()));
                        log.info("Indexed SaleQuote Core CID - {}, objId - {}", companyID, edsSaleQuote.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on SaleQuote with id {}, and error message {} **********************", edsSaleQuote.getObjectID(), e.getMessage());
                    }
                }
            }

            if (!saleQuoteSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create SaleQuote solr docs for company {} with size {} =========", companyID, saleQuoteSolrDocs.size());
                    saleQuoteSolrDocRepository.saveAll(saleQuoteSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving SaleQuote list", e);
                }
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsSaleQuote> edsSaleQuoteList, List<EdsPickList> pickList) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsSaleQuoteList)) {
            ConcurrentLinkedQueue<SaleQuoteSolrDoc> saleQuoteSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();

            Map<Integer, Integer> quoteByPickListMap = new ConcurrentHashMap();
            if (pickList != null && !pickList.isEmpty()) {
                for (EdsPickList pl : pickList) {
                    quoteByPickListMap.put(pl.getSaleQuote().getObjectID(), pl.getObjectID());
                }
            }
            for (EdsSaleQuote edsSaleQuote : edsSaleQuoteList) {
                SaleQuoteSolrItem solrRPC = edsSaleQuote.getSolrRPC();
                EdsInvoiceCustomFields customFields = edsSaleQuote.getCustomFields();
                Callable<Void> task = () -> {
                    try {
                        ServerSecurityContext.getInstance().setDatabase(dataBase);
                        ServerSecurityContext.getInstance().setCompanyId(companyId);
                        sync.execute(getSynchronizedKey(solrRPC), () -> {
                                    saleQuoteSolrDocs.add(createSaleQuoteDocument(solrRPC, Integer.valueOf(companyId), quoteByPickListMap.get(edsSaleQuote.getObjectID()), customFields));
                                    log.info("Indexed SaleQuote Core CID - {}, objId - {}", companyId, edsSaleQuote.getObjectID());
                                }
                        );
                    } catch (Exception e) {
                        log.error("********************* Error on SaleQuote with id {}, and error message {} **********************", edsSaleQuote.getObjectID(), e.getMessage());
                    }
                    return null;
                };
                tasks.add(task);
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
                log.error("Error on loading SaleQuote list", e);
            }

            if (!saleQuoteSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create SaleQuote solr docs for company {} with size {} =========", companyId, saleQuoteSolrDocs.size());
                    saleQuoteSolrDocRepository.saveAll(saleQuoteSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving SaleQuote list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(SaleQuoteSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectID();
    }

    private SaleQuoteSolrDoc createSaleQuoteDocument(SaleQuoteSolrItem saleQuote, Integer companyID, Integer pickListId, EdsCustomFields customFields) {
        SaleQuoteSolrDoc saleQuoteSolrDoc = new SaleQuoteSolrDoc();

        saleQuoteSolrDoc.setCompanyId(companyID);
        saleQuoteSolrDoc.setOid(SolrUtils.generatedOId(companyID, saleQuote.getObjectID()));
        saleQuoteSolrDoc.setSaleInvoiceId(saleQuote.getObjectID());
        saleQuoteSolrDoc.setOpportunityId(saleQuote.getOpportunity().getId());

        if (saleQuote.getClient() != null) {
            saleQuoteSolrDoc.setClientId(saleQuote.getClient().getId());
            saleQuoteSolrDoc.setClientName(saleQuote.getClient().getName());
            saleQuoteSolrDoc.setClientIdName(SolrUtils.getIdName(saleQuote.getClient().getId(), saleQuote.getClient().getName()));

            if (!saleQuote.getCustomerOwnerIds().isEmpty()) {
                saleQuoteSolrDoc.getCustomerOwnerId().addAll(saleQuote.getCustomerOwnerIds());
            }
        }

        if (saleQuote.getClientContact() != null) {
            SelectItem contact = saleQuote.getClientContact();
            saleQuoteSolrDoc.setClientContactId(contact.getId());
            saleQuoteSolrDoc.setClientContactEmail(contact.getName());
            saleQuoteSolrDoc.setClientContactIdEmail(SolrUtils.getIdName(contact.getId(), contact.getName()));
        }

        if (saleQuote.getCurrency() != null) {
            SelectItem currency = saleQuote.getCurrency();
            saleQuoteSolrDoc.setCurrencyId(currency.getId());
            saleQuoteSolrDoc.setCurrencyName(currency.getName());
            saleQuoteSolrDoc.setCurrencyIdName(SolrUtils.getIdName(currency.getId(), currency.getName()));
        }

        if (saleQuote.getRelatedProject() != null) {
            SelectItem relatedProject = saleQuote.getRelatedProject();
            saleQuoteSolrDoc.setRelatedProjectId(relatedProject.getId());
            saleQuoteSolrDoc.setRelatedProjectName(relatedProject.getName());
            saleQuoteSolrDoc.setRelatedProjectNumber(relatedProject.getNumber());
            if (relatedProject.getCode() != null) {
                saleQuoteSolrDoc.setRelatedProjectCode(relatedProject.getCode());
            }
            saleQuoteSolrDoc.setRelatedProjectIdName(SolrUtils.getIdName(relatedProject.getId(), relatedProject.getName()));
        }

        if (saleQuote.getPdfTemplateId() != null) {
            saleQuoteSolrDoc.setPdfTemplateId(saleQuote.getPdfTemplateId());
        }

        if (saleQuote.getMultiProject() != null && !saleQuote.getMultiProject().isEmpty()) {
            saleQuote.getMultiProject().forEach(project -> {
                saleQuoteSolrDoc.getMultiProjectId().add(project.getId());
                saleQuoteSolrDoc.getMultiProjectName().add(project.getName());
                saleQuoteSolrDoc.getMultiProjectNumber().add(project.getNumber());
                saleQuoteSolrDoc.getMultiProjectIdName().add(SolrUtils.getIdName(project.getId(), project.getName()));
                saleQuoteSolrDoc.getMultiProjectNumberName().add(project.getNumber() + SolrSaleInvoiceRepresenter.ARROW + project.getName());

            });
        }

        if (saleQuote.getStatus() != null) {
            ReferenceItem status = saleQuote.getStatus();
            saleQuoteSolrDoc.setStatusId(status.getObjectID());
            saleQuoteSolrDoc.setStatusName(status.getName());
            saleQuoteSolrDoc.setStatusIdName(SolrUtils.getIdName(status.getObjectID(), status.getName()));
            saleQuoteSolrDoc.setStatusCode(status.getCode());
            saleQuoteSolrDoc.setStatusSorder(status.getOrder());
        }

        if (saleQuote.getShippingMethod() != null) {
            SelectItem shippingMethod = saleQuote.getShippingMethod();
            saleQuoteSolrDoc.setShppingmethodId(shippingMethod.getId());
            saleQuoteSolrDoc.setShppingmethodName(shippingMethod.getName());
            saleQuoteSolrDoc.setShppingmethodIdName(SolrUtils.getIdName(shippingMethod.getId(), shippingMethod.getName()));
        }

        if (saleQuote.getCreator() != null) {
            SelectItem creator = saleQuote.getCreator();
            saleQuoteSolrDoc.setCreatorId(creator.getId());
            saleQuoteSolrDoc.setCreatorName(creator.getName());
            saleQuoteSolrDoc.setCreatorIdName(SolrUtils.getIdName(creator.getId(), creator.getName()));
        }

        saleQuoteSolrDoc.setPicklistId(pickListId);


        if (saleQuote.getProjectidsFromEmployeeId() != null && !saleQuote.getProjectidsFromEmployeeId().isEmpty()) {
            saleQuoteSolrDoc.getProjectidsFromEmployeeId().addAll(saleQuote.getProjectidsFromEmployeeId());
        }

        if (saleQuote.getCurrentApprover() != null) {
            SelectItem approver = saleQuote.getCurrentApprover();
            saleQuoteSolrDoc.setCurrentApproverId(approver.getId());
            saleQuoteSolrDoc.setCurrentApproverName(approver.getName());
            saleQuoteSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(approver.getId(), approver.getName()));
        }
        if (!CollectionUtils.isEmpty(saleQuote.getItemIds())) {
            saleQuoteSolrDoc.getItemId().addAll(saleQuote.getItemIds());
        }
        saleQuoteSolrDoc.setInvoiceNumber(saleQuote.getInvoiceNumber());
        saleQuoteSolrDoc.setInvoiceDate(saleQuote.getInvoiceDate());
        saleQuoteSolrDoc.setDueDate(saleQuote.getDueDate());

        saleQuoteSolrDoc.setTotalTaxes(saleQuote.getTotalTaxes().doubleValue());
        saleQuoteSolrDoc.setExchargeRate(saleQuote.getExchangeRate().doubleValue());
        saleQuoteSolrDoc.setTotalInvoiceCurrency(saleQuote.getTotalInvoiceCurrency().doubleValue());
        saleQuoteSolrDoc.setTotalInvoiceBase(saleQuote.getTotalInvoiceBase().doubleValue());
        saleQuoteSolrDoc.setDueAmount(saleQuote.getDueAmount().doubleValue());
        saleQuoteSolrDoc.setSubTotal(saleQuote.getSubTotal().doubleValue());
        saleQuoteSolrDoc.setNetAmountTotal(saleQuote.getNetAmountTotal().doubleValue());
        saleQuoteSolrDoc.setSalesOrder(saleQuote.isSalesOrder());
        saleQuoteSolrDoc.setPoNumber(saleQuote.getPoNumber());
        saleQuoteSolrDoc.setProgressInvoicing(saleQuote.isProgressInvoicing());
        saleQuoteSolrDoc.setIntroduction(saleQuote.getIntroduction());
        saleQuoteSolrDoc.setReference(saleQuote.getReference());
        saleQuoteSolrDoc.setCreatedDate(saleQuote.getCreatedDate());
        saleQuoteSolrDoc.setUpdatedDate(saleQuote.getUpdatedDate());
        saleQuoteSolrDoc.setTaxCalculationType(saleQuote.getTaxCalculationType());

        if (saleQuote.getOpportunity() != null) {
            EdsOpportunity opportunity = opportunityManager.get(saleQuote.getOpportunity().getId());
            if (opportunity != null) {
                saleQuoteSolrDoc.setOpportunityNumber(opportunity.getNumber());
            }
        }
        CustomFieldsUtils.setSolrDocDynamicFields(saleQuoteSolrDoc, customFields);
        return saleQuoteSolrDoc;
    }

    public Page<SaleQuoteSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);
        if (!filterParameter.isSearchButton() && (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField()))) {
            Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
            solrSort = switch (filterParameter.getSortField()) {
                case InvoiceList.INVOICE_NUMBER ->
                        Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_INVOICE_NUMBER);
                case InvoiceList.INVOICE_DATE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE);
                case InvoiceList.DUE_DATE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE);
                case InvoiceList.CLIENT -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_CLIENT_NAME);
                case InvoiceList.CURRENCY -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_CURRENCY_NAME);
                case InvoiceList.PAID_AMOUNT ->
                        Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_SORTABLE_PAID_AMOUNT);
                case InvoiceList.DUE_AMOUNT -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
                case InvoiceList.STATUS -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME);
                case InvoiceList.RELATED_PROJECT ->
                        Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_RELATED_PROJECT_NAME);
                case InvoiceList.ORIGINAL_AMOUNT ->
                        Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY);
                case InvoiceList.BASE_TOTAL ->
                        Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE);
                case InvoiceList.PO_NUMBER -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_PO_NUMBER);
                case InvoiceList.SUB_TOTAL -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_SUB_TOTAL);
                case InvoiceList.TAX_TOTAL -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_TOTAL_TAXES);
                case InvoiceList.QUOTE_NUMBER -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER);
                case InvoiceList.REFERENCE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.REFERENCE);
                case InvoiceList.CREATED_DATE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE);
                case InvoiceList.UPDATED_DATE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE);
                default ->
                        CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
            };
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_SALEQUOTE_CORE, query, SaleQuoteSolrDoc.class);
    }

    public FacetFilterRpc getSaleQuoteFacetFilterData(FacetFilterRpc quoteFacet) {
        if (!quoteFacet.isFilterChanges()) {
            quoteFacet = commonServiceLocal.getUserFacetFilter(quoteFacet);
        }

        quoteFacet = applyNonConvertedFilterPeriod(quoteFacet);

        StringBuilder solrQuery = new StringBuilder();
        String selectedDate = null;
        if (quoteFacet.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(quoteFacet.getSelectedDateSolrCodeName()) ? "DUE_DATE" : "INVOICE_DATE".equals(quoteFacet.getSelectedDateSolrCodeName()) ? "INVOICE_DATE" : null;
        }
        EdsUser edsUser = employeeManager.getUser();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(quoteFacet.getSearchKey());
        fp.setStartDate(quoteFacet.getStartDate());
        fp.setEndDate(quoteFacet.getEndDate());
        solrQuery.append(invoiceCircularResolver.getSaleQuoteSolrQuery(fp, edsUser, false, selectedDate));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(quoteFacet, FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(quoteFacet, edsUser.getCompany(), SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_SALEQUOTE_CORE, solrQuery.toString(), quoteFacet, SaleQuoteSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, quoteFacet);
        if (quoteFacet.getFacetContentMap().containsKey(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2])) {
            getSaleQuoteFacetResultFromSolr(facetPage, quoteFacet);
        }
        return quoteFacet;
    }

    public FacetFilterRpc getSaleOrderFacetFilterData(FacetFilterRpc quoteFacet) {
        if (!quoteFacet.isFilterChanges()) {
            quoteFacet = commonServiceLocal.getUserFacetFilter(quoteFacet);
        }

        quoteFacet = applyNonConvertedFilterPeriod(quoteFacet);

        StringBuilder solrQuery = new StringBuilder();
        EdsUser edsUser = employeeManager.getUser();
        String selectedDate = null;
        if (quoteFacet.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(quoteFacet.getSelectedDateSolrCodeName()) ? "DUE_DATE" : "INVOICE_DATE".equals(quoteFacet.getSelectedDateSolrCodeName()) ? "INVOICE_DATE" : null;
        }
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(quoteFacet.getSearchKey());
        fp.setStartDate(quoteFacet.getStartDate());
        fp.setEndDate(quoteFacet.getEndDate());
        solrQuery.append(invoiceCircularResolver.getSaleOrderSolrQuery(fp, edsUser, false, selectedDate));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(quoteFacet, FacetContentType.SaleOrderFacetFilter.getContentCode()[2]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(quoteFacet, edsUser.getCompany(), SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                FacetContentType.SaleOrderFacetFilter.getContentCode()[2]
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_SALEQUOTE_CORE, solrQuery.toString(), quoteFacet, SaleQuoteSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, quoteFacet);
        if (quoteFacet.getFacetContentMap().containsKey(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2])) {
            getSaleQuoteFacetResultFromSolr(facetPage, quoteFacet);
        }
        return quoteFacet;
    }

    private FacetFilterRpc applyNonConvertedFilterPeriod(FacetFilterRpc facet) {
        HashMap<String, String> customData = facet.getCustomData();
        if (customData.get(STARTDATE_NC) != null) {
            facet.setStartDate(ServerUtils.parseFilterParameterDate(customData.get(STARTDATE_NC)));
        }
        if (customData.get(ENDDATE_NC) != null) {
            facet.setEndDate(ServerUtils.parseFilterParameterDate(customData.get(ENDDATE_NC)));
        }
        return facet;
    }

    private FacetFilterRpc getSaleQuoteFacetResultFromSolr(QueryResponse resp, FacetFilterRpc quoteFacet) {
        int num = 0;
        FacetField amountFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
        if (amountFacet != null && amountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
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
            SelectItem[] amount = new SelectItem[5];
            amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            amount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            amount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            amount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            amount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            amount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            amount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            amount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            amount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            quoteFacet.getFacetContentMap().get(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]).setFacetItems(amount);
        } else {
            quoteFacet.getFacetContentMap().get(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]).setFacetItems(new SelectItem[0]);
        }
        return quoteFacet;
    }

}
