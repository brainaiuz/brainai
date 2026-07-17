package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.solr.document.PurchaseInvoiceSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.PurchaseInvoiceSolrDocRepository;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ENDDATE_NC;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_PURCHASE_INVOICE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.STARTDATE_NC;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
@Component
public class PurchaseInvoiceSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(PurchaseInvoiceSolrComponent.class);

    @Autowired
    private PurchaseInvoiceSolrDocRepository purchaseInvoiceSolrDocRepository;
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
    private OpportunityManager opportunityManager;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsPurchaseInvoice invoice) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(invoice));
    }

    @Transactional
    public void indexes(List<EdsPurchaseInvoice> purchaseInvoices) throws IOException, SolrServerException, InterruptedException {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();

        if (!CollectionUtils.isEmpty(purchaseInvoices)) {
            List<PurchaseInvoiceSolrDoc> purchaseInvoiceSolrDocs = new ArrayList<>();


            for (EdsPurchaseInvoice purchaseInvoice : purchaseInvoices) {
                if (purchaseInvoice != null) {
                    try {
                        purchaseInvoiceSolrDocs.add(createPurchaseInvoiceDocument(purchaseInvoice, Integer.valueOf(companyId)));
                        log.info("Indexed PurchaseInvoice Core CID - {}, objId - {}", companyId, purchaseInvoice.getObjectID());

                    } catch (Exception e) {
                        log.error("********************* Error on PurchaseInvoice with id {}, and error message {} **********************", purchaseInvoice.getObjectID(), e.getMessage());
                        throw e;
                    }
                }

                if (!purchaseInvoiceSolrDocs.isEmpty()) {
                    log.info("========= Create PurchaseInvoice solr docs for company {} with size {} =========", companyId, purchaseInvoiceSolrDocs.size());
                    purchaseInvoiceSolrDocRepository.saveAll(purchaseInvoiceSolrDocs);
                }
            }
        }
    }


    @Transactional
    public void indexConcurrently(List<EdsPurchaseInvoice> purchaseInvoices) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(purchaseInvoices)) {
            ConcurrentLinkedQueue<PurchaseInvoiceSolrDoc> purchaseInvoiceSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsPurchaseInvoice purchaseInvoice : purchaseInvoices) {
                if (purchaseInvoice != null) {
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            synchronized (this) {
                                purchaseInvoiceSolrDocs.add(createPurchaseInvoiceDocument(purchaseInvoice, Integer.valueOf(companyId)));
                                log.info("Indexed PurchaseInvoice Core CID - {}, objId - {}", companyId, purchaseInvoice.getObjectID());
                            }
                        } catch (Exception e) {
                            log.error("********************* Error on PurchaseInvoice with id {}, and error message {} **********************", purchaseInvoice.getObjectID(), e.getMessage());
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
                log.error("Error on loading PurchaseInvoice list", e);
            }

            if (!purchaseInvoiceSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create PurchaseInvoice solr docs for company {} with size {} =========", companyId, purchaseInvoiceSolrDocs.size());
                    purchaseInvoiceSolrDocRepository.saveAll(purchaseInvoiceSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving PurchaseInvoice list", e);
                }
            }
        }
    }

    private PurchaseInvoiceSolrDoc createPurchaseInvoiceDocument(EdsPurchaseInvoice edsPurchaseInvoice, Integer
            companyId) {
        PurchaseInvoiceSolrDoc purchaseInvoiceSolrDoc = new PurchaseInvoiceSolrDoc();
        purchaseInvoiceSolrDoc.setOid(SolrUtils.generatedOId(companyId, edsPurchaseInvoice.getObjectID()));
        purchaseInvoiceSolrDoc.setCompanyId(companyId);
        purchaseInvoiceSolrDoc.setPurchaseInvoiceId(edsPurchaseInvoice.getObjectID());
        purchaseInvoiceSolrDoc.setPurchaseInvoiceNumber(edsPurchaseInvoice.getNumber());
        purchaseInvoiceSolrDoc.setInvoiceDate(edsPurchaseInvoice.getInvoiceDate());
        purchaseInvoiceSolrDoc.setDueDate(edsPurchaseInvoice.getDueDate());
        purchaseInvoiceSolrDoc.setCreditNote(edsPurchaseInvoice.isCreditNote());
        purchaseInvoiceSolrDoc.setHasPayment(edsPurchaseInvoice.getPaymentItemsList(edsPurchaseInvoice).size() > 0);
        purchaseInvoiceSolrDoc.setTotalInInvoiceCurrency(edsPurchaseInvoice.getTotalInInvoiceCurrency().doubleValue());
        purchaseInvoiceSolrDoc.setTotalInvoiceBase(edsPurchaseInvoice.getTotal().doubleValue());
        if (edsPurchaseInvoice.getOpportunityID() != null) {
            EdsOpportunity edsOpportunity = opportunityManager.get(edsPurchaseInvoice.getOpportunityID());
            purchaseInvoiceSolrDoc.setOpportunityId(edsPurchaseInvoice.getOpportunityID());
            purchaseInvoiceSolrDoc.setOpportunityNumber(edsOpportunity.getNumber());
        }

        if (edsPurchaseInvoice.getClientOrSupplier() != null) {
            if (!edsPurchaseInvoice.getClientOrSupplier().getOwners().isEmpty()) {
                edsPurchaseInvoice.getClientOrSupplier().getOwners().forEach(o -> purchaseInvoiceSolrDoc.getClientOwnerId().add(o.getObjectID()));
            }
        }

        purchaseInvoiceSolrDoc.setPurchaseInvoiceTotalTaxes(edsPurchaseInvoice.getTotalTaxes() != null ? edsPurchaseInvoice.getTotalTaxes().doubleValue() : BigDecimal.ZERO.doubleValue());
        purchaseInvoiceSolrDoc.setPurchaseInvoiceExchangeRate(edsPurchaseInvoice.getExchangeRate().doubleValue());
        purchaseInvoiceSolrDoc.setPurchaseInvoiceTaxCalculationType(edsPurchaseInvoice.getTaxCalculationType());
        purchaseInvoiceSolrDoc.setInvoiceType(edsPurchaseInvoice.getType());

        if (edsPurchaseInvoice.getRelatedProject() != null) {
            purchaseInvoiceSolrDoc.setRelatedProjectId(edsPurchaseInvoice.getRelatedProject().getObjectID());
            purchaseInvoiceSolrDoc.setRelatedProjectName(edsPurchaseInvoice.getRelatedProject().getName());
            purchaseInvoiceSolrDoc.setRelatedProjectNumber(edsPurchaseInvoice.getRelatedProject().getNumber());
            purchaseInvoiceSolrDoc.setRelatedProjectIdName(SolrUtils.getIdName(edsPurchaseInvoice.getRelatedProject().getObjectID(), edsPurchaseInvoice.getRelatedProject().getName()));
            purchaseInvoiceSolrDoc.setPurchaseInvoiceRelatedProjectStatusCode(edsPurchaseInvoice.getRelatedProject().getStatus().getCode());
        }

        if (edsPurchaseInvoice.getProjects() != null) {
            edsPurchaseInvoice.getProjects().forEach(edsProject -> {
                purchaseInvoiceSolrDoc.getMultiProjectId().add(edsProject.getObjectID());
                purchaseInvoiceSolrDoc.getMultiProjectName().add(edsProject.getName());
                purchaseInvoiceSolrDoc.getMultiProjectNumber().add(edsProject.getNumber());
                purchaseInvoiceSolrDoc.getMultiProjectIdName().add(SolrUtils.getIdName(edsProject.getObjectID(), edsProject.getName()));
                purchaseInvoiceSolrDoc.getMultiProjectNumberName().add(edsProject.getNumber() + SolrPurchaseInvoiceRepresenter.ARROW + edsProject.getName());
            });
        }

        if (edsPurchaseInvoice.getSupplier() != null) {
            purchaseInvoiceSolrDoc.setClientId(edsPurchaseInvoice.getSupplier().getObjectID());
            purchaseInvoiceSolrDoc.setClientName(edsPurchaseInvoice.getSupplier().getName());
            purchaseInvoiceSolrDoc.setClientIdName(SolrUtils.getIdName(edsPurchaseInvoice.getSupplier().getObjectID(), edsPurchaseInvoice.getSupplier().getName()));
            purchaseInvoiceSolrDoc.setPurchaseInvoiceSupplierVatNumber(edsPurchaseInvoice.getSupplier().getVatNumber());
            purchaseInvoiceSolrDoc.setPurchaseInvoiceSupplierTrn(edsPurchaseInvoice.getSupplier().getTrn());
        }

        if (edsPurchaseInvoice.getCreator() != null) {
            purchaseInvoiceSolrDoc.setCreatorId(edsPurchaseInvoice.getCreator().getObjectID());
            purchaseInvoiceSolrDoc.setCreatorName(edsPurchaseInvoice.getCreator().getName());
            purchaseInvoiceSolrDoc.setCreatorIdName(SolrUtils.getIdName(edsPurchaseInvoice.getCreator().getObjectID(), edsPurchaseInvoice.getCreator().getName()));
            purchaseInvoiceSolrDoc.setCreaterFullName(edsPurchaseInvoice.getCreator().getFullName());
        }

        if (edsPurchaseInvoice.getCurrency() != null) {
            purchaseInvoiceSolrDoc.setCurrencyId(edsPurchaseInvoice.getCurrency().getObjectID());
            purchaseInvoiceSolrDoc.setCurrencyName(edsPurchaseInvoice.getCurrency().getName());
            purchaseInvoiceSolrDoc.setCurrencyIdName(SolrUtils.getIdName(edsPurchaseInvoice.getCurrency().getObjectID(), edsPurchaseInvoice.getCurrency().getName()));
        }

        purchaseInvoiceSolrDoc.setDueAmount(edsPurchaseInvoice.getDueAmount().doubleValue());
        purchaseInvoiceSolrDoc.setPaidAmount(edsPurchaseInvoice.getFullPayments().doubleValue());

        if (edsPurchaseInvoice.getStatus() != null) {
            purchaseInvoiceSolrDoc.setStatusId(edsPurchaseInvoice.getStatus().getObjectID());
            purchaseInvoiceSolrDoc.setStatusName(edsPurchaseInvoice.getStatus().getName());
            purchaseInvoiceSolrDoc.setStatusIdName(SolrUtils.getIdName(edsPurchaseInvoice.getStatus().getObjectID(), edsPurchaseInvoice.getStatus().getName()));
            purchaseInvoiceSolrDoc.setStatusCode(edsPurchaseInvoice.getStatus().getCode());
            purchaseInvoiceSolrDoc.setStatusSorder(edsPurchaseInvoice.getStatus().getSorder());
        }

        if (!CollectionUtils.isEmpty(edsPurchaseInvoice.getInvoiceItems())) {
            edsPurchaseInvoice.getInvoiceItems().stream()
                    .filter(edsPurchaseInvoiceItem -> edsPurchaseInvoiceItem.getItem() != null)
                    .forEach(edsPurchaseInvoiceItem -> {
                        purchaseInvoiceSolrDoc.getItemId().add(edsPurchaseInvoiceItem.getItem().getObjectID());
                        if (edsPurchaseInvoiceItem.getWarehouse()!= null){
                            purchaseInvoiceSolrDoc.getWarehouseId().add(edsPurchaseInvoiceItem.getWarehouse().getObjectID());
                        }
                    });
        }

        purchaseInvoiceSolrDoc.setPoNumber(edsPurchaseInvoice.getPoNumber());
        purchaseInvoiceSolrDoc.setReference(edsPurchaseInvoice.getReference());

        if (edsPurchaseInvoice.getCurrentApprover() != null && edsPurchaseInvoice.getCurrentApprover().getExactEmployee() != null) {
            purchaseInvoiceSolrDoc.setCurrentApproverId(edsPurchaseInvoice.getCurrentApprover().getExactEmployee().getObjectID());
            purchaseInvoiceSolrDoc.setCurrentApproverName(edsPurchaseInvoice.getCurrentApprover().getExactEmployee().getFullName());
            purchaseInvoiceSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(edsPurchaseInvoice.getCurrentApprover().getExactEmployee().getObjectID(), edsPurchaseInvoice.getCurrentApprover().getExactEmployee().getFullName()));
        }
        purchaseInvoiceSolrDoc.setCreatedDate(edsPurchaseInvoice.getCreationDate());
        purchaseInvoiceSolrDoc.setUpdatedDate(edsPurchaseInvoice.getUpdatedDate());
        purchaseInvoiceSolrDoc.setZatcaStatus(edsPurchaseInvoice.getZatcaStatus());
        purchaseInvoiceSolrDoc.setConverted(edsPurchaseInvoice.getConvertedQuotes() != null);
        CustomFieldsUtils.setSolrDocDynamicFields(purchaseInvoiceSolrDoc, edsPurchaseInvoice.getCustomFields());
        return purchaseInvoiceSolrDoc;
    }

    public Page<PurchaseInvoiceSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                solrSort = switch (filterParameter.getSortField()) {
                    case AccountingConstants.INVOICE_NUMBER_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.SORTABLE_PURCHASEINVOICE_NUMBER);
                    case AccountingConstants.INVOICE_DATE_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE);
                    case AccountingConstants.DUE_DATE_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE);
                    case InvoiceList.RELATED_PROJECT ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.SORTABLE_RELATED_PROJECT_NAME);
                    case InvoiceList.SUPPLIER ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.SORTABLE_CLIENT_NAME);
                    case AccountingConstants.CURRENCY_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.SORTABLE_CURRENCY_NAME);
                    case AccountingConstants.DUE_AMOUNT_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT);
                    case AccountingConstants.PAID_AMOUNT_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT);
                    case AccountingConstants.STATUS_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_STATUS_NAME);
                    case AccountingConstants.ORIGINAL_AMOUNT_COLUMN ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_IN_INVOICE_CURRENCY);
                    case InvoiceList.BASE_TOTAL ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_INVOICE_BASE);
                    case InvoiceList.QUOTE_NUMBER ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_TOTAL_IN_INVOICE_CURRENCY);
                    case InvoiceList.PO_NUMBER ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_PO_NUMBER);
                    case InvoiceList.REFERENCE ->
                            Sort.by(sortDirection, SolrPurchaseInvoiceRepresenter.FIELD_REFERENCE);
                    default ->
                            CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
                };
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));
        return solrTemplate.query(SOLR_PURCHASE_INVOICE_CORE, query, PurchaseInvoiceSolrDoc.class);
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

    public FacetFilterRpc getPurchaseInvoiceFacetFilterData(FacetFilterRpc purchaseFacetFilter) {
        if (!purchaseFacetFilter.isFilterChanges()) {
            purchaseFacetFilter = commonServiceLocal.getUserFacetFilter(purchaseFacetFilter);
        }

        purchaseFacetFilter = applyNonConvertedFilterPeriod(purchaseFacetFilter);

        StringBuilder solrQuery = new StringBuilder();
        String selectedDate = null;
        if (purchaseFacetFilter.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(purchaseFacetFilter.getSelectedDateSolrCodeName()) ? "DUE_DATE" : "INVOICE_DATE".equals(purchaseFacetFilter.getSelectedDateSolrCodeName()) ? "INVOICE_DATE" : null;
        }
        EdsUser edsUser = employeeManager.getUser();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(purchaseFacetFilter.getSearchKey());
        fp.setStartDate(purchaseFacetFilter.getStartDate());
        fp.setEndDate(purchaseFacetFilter.getEndDate());
        fp.setFacetFilter(purchaseFacetFilter);
        solrQuery.append(invoiceCircularResolver.getPurchaseInvoiceCoreSolrQuery(fp, edsUser, selectedDate));
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(purchaseFacetFilter,
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]
        ));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(purchaseFacetFilter, edsUser.getCompany(),
                SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE,
                SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE,
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5],
                FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_PURCHASE_INVOICE_CORE, solrQuery.toString(), purchaseFacetFilter, PurchaseInvoiceSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, purchaseFacetFilter);
        if (purchaseFacetFilter.getFacetContentMap().containsKey(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4])) {
            getPurchaseInvoiceFacetResultFromSolr(facetPage, purchaseFacetFilter);
        }
        FacetContentRpc facetContentRpc = purchaseFacetFilter.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]);
        if (facetContentRpc != null) {
            SelectItem[] facetItems = facetContentRpc.getFacetItems();
            for (SelectItem facetItem : facetItems) {
                if (facetItem.getDescription() != null) {
                    facetItem.setDescription(facetItem.getDescription().replace("false", "Purchase Invoices"));
                    facetItem.setDescription(facetItem.getDescription().replace("true", "Debit Note"));
                }
            }
            facetContentRpc.setFacetItems(facetItems);
            purchaseFacetFilter.getFacetContentMap().put(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6], facetContentRpc);
        }
        return purchaseFacetFilter;
    }

    private FacetFilterRpc getPurchaseInvoiceFacetResultFromSolr(QueryResponse resp, FacetFilterRpc purchaseFacet) {
        int num = 0;
        FacetField amountFacet = resp.getFacetField(SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT);
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

            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4]).setFacetItems(amount);
        } else {
            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4]).setFacetItems(new SelectItem[0]);
        }

        FacetField paidAmountFacet = resp.getFacetField(SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT);
        if (paidAmountFacet != null && paidAmountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : paidAmountFacet.getValues()) {
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
            SelectItem[] paidAmount = new SelectItem[5];
            paidAmount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            paidAmount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00 ( <b>" + lessThan100 + "</b> )");

            paidAmount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            paidAmount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            paidAmount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            paidAmount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            paidAmount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            paidAmount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            paidAmount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            paidAmount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5]).setFacetItems(paidAmount);
        } else {
            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5]).setFacetItems(new SelectItem[0]);
        }
        if (purchaseFacet.getFacetContentMap().containsKey(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6])) {
            FacetField creditNoteFacet = resp.getFacetField(SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE);
            if (creditNoteFacet != null && creditNoteFacet.getValues() != null) {
                int yes = 0, no = 0;
                for (FacetField.Count count : creditNoteFacet.getValues()) {
                    if ("true".equalsIgnoreCase(count.getName())) {
                        yes += count.getCount();
                    } else {
                        no += count.getCount();
                    }
                }
                SelectItem[] type = new SelectItem[2];
                type[0] = new SelectItem("true".hashCode(), "true");
                type[0].setDescription("Debit Notes  ( <b>" + yes + "</b> )");

                type[1] = new SelectItem("false".hashCode(), "false");
                type[1].setDescription("Purchase Invoices ( <b>" + no + "</b> )");

                purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]).setFacetItems(type);
            } else {
                if (purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]) != null) {
                    purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]).setFacetItems(new SelectItem[0]);
                }
            }
        }
        return purchaseFacet;
    }
}
