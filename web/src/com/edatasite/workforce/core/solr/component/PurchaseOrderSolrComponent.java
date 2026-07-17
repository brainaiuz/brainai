package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.solr.document.PurchaseOrderSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.PurchaseOrderSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PurchaseOrderSolrItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:31.
 */
@Component
public class PurchaseOrderSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderSolrComponent.class);

    @Autowired
    private PurchaseOrderSolrDocRepository purchaseOrderSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsPurchaseOrder purchaseOrder) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(purchaseOrder));
    }

    @Transactional
    public void indexes(List<EdsPurchaseOrder> purchaseOrders) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(purchaseOrders)) {
            List<PurchaseOrderSolrDoc> purchaseOrderSolrDocs = new ArrayList<>();

            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsPurchaseOrder purchaseOrder : purchaseOrders) {
                if (purchaseOrder != null) {
                    try {
                        purchaseOrderSolrDocs.add(createPurchaseOrderDocument(purchaseOrder.getSolrRPC(), Integer.valueOf(companyId), purchaseOrder.getCustomFields()));
                        log.info("Indexed PurchaseOrder Core CID - {}, objId - {}", companyId, purchaseOrder.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on PurchaseOrder with id {}, and error message {} **********************", purchaseOrder.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!purchaseOrderSolrDocs.isEmpty()) {
                log.info("========= Create PurchaseOrder solr docs for company {} with size {} =========", companyId, purchaseOrderSolrDocs.size());
                purchaseOrderSolrDocRepository.saveAll(purchaseOrderSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsPurchaseOrder> purchaseOrders) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(purchaseOrders)) {
            ConcurrentLinkedQueue<PurchaseOrderSolrDoc> purchaseOrderSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsPurchaseOrder purchaseOrder : purchaseOrders) {
                if (purchaseOrder != null) {
                    PurchaseOrderSolrItem solrRPC = purchaseOrder.getSolrRPC();
                    EdsInvoiceCustomFields customFields = purchaseOrder.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        purchaseOrderSolrDocs.add(createPurchaseOrderDocument(solrRPC, Integer.valueOf(companyId), customFields));
                                        log.info("Indexed PurchaseOrder Core CID - {}, objId - {}", companyId, purchaseOrder.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on PurchaseOrder with id {}, and error message {} **********************", purchaseOrder.getObjectID(), e.getMessage());
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
                log.error("Error on loading PurchaseOrder list", e);
            }

            if (!purchaseOrderSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create PurchaseOrder solr docs for company {} with size {} =========", companyId, purchaseOrderSolrDocs.size());
                    purchaseOrderSolrDocRepository.saveAll(purchaseOrderSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving PurchaseOrder list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(PurchaseOrderSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getOrderId();
    }

    private PurchaseOrderSolrDoc createPurchaseOrderDocument(PurchaseOrderSolrItem purchaseOrder, Integer companyId, EdsCustomFields customFields) {
        PurchaseOrderSolrDoc purchaseOrderSolrDoc = new PurchaseOrderSolrDoc();
        purchaseOrderSolrDoc.setOid(SolrUtils.generatedOId(companyId, purchaseOrder.getOrderId()));
        purchaseOrderSolrDoc.setCompanyId(companyId);
        purchaseOrderSolrDoc.setSaleInvoiceId(purchaseOrder.getOrderId());
        if (purchaseOrder.getOpportunity() != null) {
            purchaseOrderSolrDoc.setOpportunityId(purchaseOrder.getOpportunity().getId());
            purchaseOrderSolrDoc.setOpportunityNumber(purchaseOrder.getOpportunity().getNumber());
        }
        purchaseOrderSolrDoc.setCustomerId(purchaseOrder.getClient().getId());

        if (purchaseOrder.getClient() != null) {
            purchaseOrderSolrDoc.setClientId(purchaseOrder.getClient().getId());
            purchaseOrderSolrDoc.setClientName(purchaseOrder.getClient().getName());
            purchaseOrderSolrDoc.setClientIdName(SolrUtils.getIdName(purchaseOrder.getClient().getId(), purchaseOrder.getClient().getName()));

            if (!purchaseOrder.getClientOwnerIds().isEmpty()) {
                purchaseOrderSolrDoc.getClientOwnerId().addAll(purchaseOrder.getClientOwnerIds());
            }
        }

        if (purchaseOrder.getCurrency() != null) {
            purchaseOrderSolrDoc.setCurrencyId(purchaseOrder.getCurrency().getId());
            purchaseOrderSolrDoc.setCurrencyName(purchaseOrder.getCurrency().getName());
            purchaseOrderSolrDoc.setCurrencyIdName(SolrUtils.getIdName(purchaseOrder.getCurrency().getId(), purchaseOrder.getCurrency().getName()));
        }

        if (purchaseOrder.getRelatedProject() != null) {
            purchaseOrderSolrDoc.setRelatedProjectId(purchaseOrder.getRelatedProject().getId());
            purchaseOrderSolrDoc.setRelatedProjectName(purchaseOrder.getRelatedProject().getName());
            purchaseOrderSolrDoc.setRelatedProjectNumber(purchaseOrder.getRelatedProject().getNumber());
            purchaseOrderSolrDoc.setRelatedProjectIdName(SolrUtils.getIdName(purchaseOrder.getRelatedProject().getId(), purchaseOrder.getRelatedProject().getName()));
            if (purchaseOrder.getRelatedProject().getCode() != null) {
                purchaseOrderSolrDoc.setRelatedProjectCode(purchaseOrder.getRelatedProject().getCode());
            }
        }

        if (purchaseOrder.getMultiProject() != null && !purchaseOrder.getMultiProject().isEmpty()) {
            purchaseOrder.getMultiProject().forEach(edsProject -> {
                purchaseOrderSolrDoc.getMultiProjectId().add(edsProject.getId());
                purchaseOrderSolrDoc.getMultiProjectName().add(edsProject.getName());
                purchaseOrderSolrDoc.getMultiProjectNumber().add(edsProject.getNumber());
                purchaseOrderSolrDoc.getMultiProjectIdName().add(SolrUtils.getIdName(edsProject.getId(), edsProject.getName()));
                purchaseOrderSolrDoc.getMultiProjectNumberName().add(edsProject.getNumber() + SolrSaleInvoiceRepresenter.ARROW + edsProject.getName());
            });
        }

        if (purchaseOrder.getStatus() != null) {
            purchaseOrderSolrDoc.setStatusId(purchaseOrder.getStatus().getObjectID());
            purchaseOrderSolrDoc.setStatusName(purchaseOrder.getStatus().getName());
            purchaseOrderSolrDoc.setStatusIdName(SolrUtils.getIdName(purchaseOrder.getStatus().getObjectID(), purchaseOrder.getStatus().getName()));
            purchaseOrderSolrDoc.setStatusSorder(purchaseOrder.getStatus().getOrder());
            purchaseOrderSolrDoc.setStatusCode(purchaseOrder.getStatus().getCode());
        }

        if (purchaseOrder.getCreator() != null) {
            purchaseOrderSolrDoc.setCreatorId(purchaseOrder.getCreator().getId());
            purchaseOrderSolrDoc.setCreatorName(purchaseOrder.getCreator().getName());
            purchaseOrderSolrDoc.setCreatorIdName(SolrUtils.getIdName(purchaseOrder.getCreator().getId(), purchaseOrder.getCreator().getName()));
        }

        if (purchaseOrder.getManager() != null) {
            purchaseOrderSolrDoc.setManagerId(purchaseOrder.getManager().getId());
            purchaseOrderSolrDoc.setManagerName(purchaseOrder.getManager().getName());
            purchaseOrderSolrDoc.setManagerIdName(SolrUtils.getIdName(purchaseOrder.getManager().getId(), purchaseOrder.getManager().getName()));
        }

        if (purchaseOrder.getCurrentApprover() != null) {
            purchaseOrderSolrDoc.setCurrentApproverId(purchaseOrder.getCurrentApprover().getId());
            purchaseOrderSolrDoc.setCurrentApproverName(purchaseOrder.getCurrentApprover().getName());
            purchaseOrderSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(purchaseOrder.getCurrentApprover().getId(), purchaseOrder.getCurrentApprover().getName()));
        }

        if (!CollectionUtils.isEmpty(purchaseOrder.getItemIds())) {
            purchaseOrderSolrDoc.getItemId().addAll(purchaseOrder.getItemIds());
        }

        purchaseOrderSolrDoc.setInvoiceNumber(purchaseOrder.getInvoiceNumber());
        purchaseOrderSolrDoc.setInvoiceDate(purchaseOrder.getInvoiceDate());
        purchaseOrderSolrDoc.setDueDate(purchaseOrder.getDueDate());

        purchaseOrderSolrDoc.setReference(purchaseOrder.getReference());
        purchaseOrderSolrDoc.setTotalInvoiceBase(purchaseOrder.getTotalInvoiceBase().doubleValue());
        purchaseOrderSolrDoc.setTotalInvoiceCurrency(purchaseOrder.getTotalInvoiceCurrency().doubleValue());
        purchaseOrderSolrDoc.setDueAmount(purchaseOrder.getTotalInvoiceCurrency().doubleValue());
        purchaseOrderSolrDoc.setQuoteNumber(purchaseOrder.getQuoteNumber());
        purchaseOrderSolrDoc.setTotalTaxes(purchaseOrder.getTotalTaxes().doubleValue());
        purchaseOrderSolrDoc.setCreatedDate(purchaseOrder.getCreatedDate());
        purchaseOrderSolrDoc.setUpdatedDate(purchaseOrder.getUpdatedDate());
        purchaseOrderSolrDoc.setSubTotal(purchaseOrder.getSubTotal().doubleValue());
        purchaseOrderSolrDoc.setExchargeRate(purchaseOrder.getExchangeRate() != null ? purchaseOrder.getExchangeRate().doubleValue() : 0d);
        purchaseOrderSolrDoc.setTaxCalculationType(purchaseOrder.getTaxCalculationType());
        CustomFieldsUtils.setSolrDocDynamicFields(purchaseOrderSolrDoc, customFields);
        return purchaseOrderSolrDoc;
    }

    public Page<PurchaseOrderSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                solrSort = switch (filterParameter.getSortField()) {
                    case InvoiceList.INVOICE_NUMBER ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_INVOICE_NUMBER);
                    case InvoiceList.INVOICE_DATE ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE);
                    case InvoiceList.DUE_DATE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE);
                    case InvoiceList.CLIENT -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_CLIENT_NAME);
                    case InvoiceList.CURRENCY ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.SORTABLE_CURRENCY_NAME);
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
                    case InvoiceList.QUOTE_NUMBER ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER);
                    case InvoiceList.REFERENCE -> Sort.by(sortDirection, SolrSaleInvoiceRepresenter.REFERENCE);
                    default ->
                            CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
                };
            }
        }

        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_PURCHASE_ORDER_CORE, query, PurchaseOrderSolrDoc.class);
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

    public FacetFilterRpc getPurchaseOrderFacetFilterData(FacetFilterRpc orderFacet) {
        if (!orderFacet.isFilterChanges()) {
            orderFacet = commonServiceLocal.getUserFacetFilter(orderFacet);
        }

        orderFacet = applyNonConvertedFilterPeriod(orderFacet);

        StringBuilder solrQuery = new StringBuilder();
        EdsUser edsUser = employeeManager.getUser();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(orderFacet.getSearchKey());
        fp.setStartDate(orderFacet.getStartDate());
        fp.setEndDate(orderFacet.getEndDate());
        fp.setModule(orderFacet.getName());
        solrQuery.append(invoiceCircularResolver.getPurchaseOrderSolrQuery(fp, edsUser, false));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(orderFacet, FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(orderFacet, edsUser.getCompany(), SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                FacetContentType.PurchaseOrderFacetFilter.getContentCode()[2]
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_PURCHASE_ORDER_CORE, solrQuery.toString(), orderFacet, PurchaseOrderSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, orderFacet);
        if (orderFacet.getFacetContentMap().containsKey(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2])) {
            getSaleQuoteFacetResultFromSolr(facetPage, orderFacet);
        }
        return orderFacet;
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
