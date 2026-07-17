package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.config.ExecutorConfig;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.enums.EntityTypeEnum;
import com.edatasite.workforce.core.solr.document.SaleInvoiceSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.SaleInvoiceSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.CustomCrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaleInvoiceSolrItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.hibernate.Hibernate;
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
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:31.
 */

@Component
public class SaleInvoiceSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(SaleInvoiceSolrComponent.class);

    @Autowired
    private SaleInvoiceSolrDocRepository saleInvoiceSolrDocRepository;
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
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsSaleInvoice invoice) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(invoice));
    }

    @Transactional
    public void indexes(List<EdsSaleInvoice> invoices) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(invoices)) {
            List<SaleInvoiceSolrDoc> saleInvoicesDocs = new ArrayList<>();

            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsSaleInvoice invoice : invoices) {
                Set<Integer> quoteCreatorIds;
                if (!invoice.getConvertedQuotes().isEmpty()) {
                    quoteCreatorIds = invoice.getConvertedQuotes().stream()
                            .map(EdsQuote::getCreator)
                            .filter(Objects::nonNull)
                            .map(EdsUser::getObjectID)
                            .collect(Collectors.toSet());
                } else {
                    quoteCreatorIds = null;
                }
                if (invoice != null) {
                    try {
                        saleInvoicesDocs.add(createSaleInvoiceDocument(invoice, Integer.valueOf(companyId), quoteCreatorIds));
                        log.info("Indexed SaleInvoice Core CID - {}, objId - {}", companyId, invoice.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on SaleInvoice with id {}, and error message {} **********************", invoice.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!saleInvoicesDocs.isEmpty()) {
                log.info("========= Create SaleInvoice solr docs for company {} with size {} =========", companyId, saleInvoicesDocs.size());
                saleInvoiceSolrDocRepository.saveAll(saleInvoicesDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsSaleInvoice> invoices) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(invoices)) {
            ConcurrentLinkedQueue<SaleInvoiceSolrDoc> saleInvoicesDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsSaleInvoice invoice : invoices) {
                if (invoice != null) {
                    Set<Integer> quoteCreatorIds;
                    if (!invoice.getConvertedQuotes().isEmpty()) {
                        quoteCreatorIds = invoice.getConvertedQuotes().stream()
                                .map(EdsQuote::getCreator)
                                .filter(Objects::nonNull)
                                .map(EdsUser::getObjectID)
                                .collect(Collectors.toSet());
                    } else {
                        quoteCreatorIds = null;
                    }
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            synchronized (this) {
                                saleInvoicesDocs.add(createSaleInvoiceDocument(invoice, Integer.valueOf(companyId), quoteCreatorIds));
                                log.info("Indexed SaleInvoice Core CID - {}, objId - {}", companyId, invoice.getObjectID());
                            }
                        } catch (Exception e) {
                            log.error("********************* Error on SaleInvoice with id {}, and error message {} **********************", invoice.getObjectID(), e.getMessage());
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
                Thread.currentThread().interrupt();
                log.error("Error on loading SaleInvoice list", e);
            }

            if (!saleInvoicesDocs.isEmpty()) {
                try {
                    log.info("========= Create SaleInvoice solr docs for company {} with size {} =========", companyId, saleInvoicesDocs.size());
                    saleInvoiceSolrDocRepository.saveAll(saleInvoicesDocs);
                } catch (Exception e) {
                    log.error("Error on saving SaleInvoice list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EdsSaleInvoice invoice) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + invoice.getObjectID();
    }

    private SaleInvoiceSolrDoc createSaleInvoiceDocument(EdsSaleInvoice invoice, Integer companyId, Set<Integer> quoteCreatorIds) {
        SaleInvoiceSolrDoc saleInvoiceSolrDoc = new SaleInvoiceSolrDoc();
        saleInvoiceSolrDoc.setOid(SolrUtils.generatedOId(companyId, invoice.getObjectID()));
        saleInvoiceSolrDoc.setCompanyId(companyId);
        saleInvoiceSolrDoc.setSaleInvoiceId(invoice.getObjectID());
        if (invoice.getOpportunityID() != null) {
            EdsOpportunity edsOpportunity = opportunityManager.get(invoice.getOpportunityID());
            saleInvoiceSolrDoc.setOpportunityId(edsOpportunity != null ? invoice.getOpportunityID() : null);
            saleInvoiceSolrDoc.setOpportunityNumber(edsOpportunity != null ? edsOpportunity.getNumber() : null);
        }

        if (quoteCreatorIds != null && !quoteCreatorIds.isEmpty()) {
            saleInvoiceSolrDoc.getInvoiceFromQuoteCreatorId().addAll(quoteCreatorIds);
        }

        if (invoice.getClient() != null) {
            saleInvoiceSolrDoc.setClientId(invoice.getClientOrSupplier().getObjectID());
            saleInvoiceSolrDoc.setClientName(invoice.getClientOrSupplier().getName());
            saleInvoiceSolrDoc.setClientIdName(SolrUtils.getIdName(invoice.getClientOrSupplier().getObjectID(), invoice.getClientOrSupplier().getName()));
            saleInvoiceSolrDoc.setClientVat(invoice.getClientOrSupplier().getVatNumber());
            saleInvoiceSolrDoc.setClientTrn(invoice.getClientOrSupplier().getTrn());

            if (!invoice.getClientOrSupplier().getOwners().isEmpty()) {
                invoice.getClientOrSupplier().getOwners().forEach(o -> saleInvoiceSolrDoc.getCustomerOwnerId().add(o.getObjectID()));
            }
        }
        GenericSettingsManager genericSettingsManager = StaticContextAccessor.getBean(GenericSettingsManager.class);
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT)) {
            CustomCrmAccountManager customCrmAccountManager = StaticContextAccessor.getBean(CustomCrmAccountManager.class);
            EdsCustomCrmAccount edsCustomCrmAccount = customCrmAccountManager.getCustomCrmAccountByEntityTypeAndEntityId(invoice.getObjectID(), EntityTypeEnum.SALE_INVOICE.name());
            if (edsCustomCrmAccount != null) {
                saleInvoiceSolrDoc.setCustomClientId(edsCustomCrmAccount.getObjectID());
                saleInvoiceSolrDoc.setCustomClientName(edsCustomCrmAccount.getClientName());
                saleInvoiceSolrDoc.setCustomClientIdName(SolrUtils.getIdName(edsCustomCrmAccount.getObjectID(), edsCustomCrmAccount.getClientName()));
            }
        }
        if (invoice.getClientContact() != null) {
            saleInvoiceSolrDoc.setClientContactId(invoice.getClientContact().getObjectID());
            saleInvoiceSolrDoc.setClientContactEmail(invoice.getClientContact().getPrimaryEmail());
            saleInvoiceSolrDoc.setClientContactIdEmail(SolrUtils.getIdName(invoice.getClientContact().getObjectID(), invoice.getClientContact().getPrimaryEmail()));
        }
        if (invoice.getCurrency() != null) {
            saleInvoiceSolrDoc.setCurrencyId(invoice.getCurrency().getObjectID());
            saleInvoiceSolrDoc.setCurrencyName(invoice.getCurrency().getName());
            saleInvoiceSolrDoc.setCurrencyIdName(SolrUtils.getIdName(invoice.getCurrency().getObjectID(), invoice.getCurrency().getName()));
        }
        if (invoice.getRelatedProject() != null) {
            saleInvoiceSolrDoc.setRelatedProjectId(invoice.getRelatedProject().getObjectID());
            saleInvoiceSolrDoc.setRelatedProjectName(invoice.getRelatedProject().getName());
            saleInvoiceSolrDoc.setRelatedProjectNumber(invoice.getRelatedProject().getNumber());
            saleInvoiceSolrDoc.setRelatedProjectIdName(SolrUtils.getIdName(invoice.getRelatedProject().getObjectID(), invoice.getRelatedProject().getName()));
            if (invoice.getRelatedProject().getStatus() != null) {
                saleInvoiceSolrDoc.setRelatedProjectCode(invoice.getRelatedProject().getStatus().getCode());
            }
        }
        if (invoice.getProjects() != null) {
            invoice.getProjects().forEach(edsProject -> {
                saleInvoiceSolrDoc.getMultiProjectId().add(edsProject.getObjectID());
                saleInvoiceSolrDoc.getMultiProjectName().add(edsProject.getName());
                saleInvoiceSolrDoc.getMultiProjectNumber().add(edsProject.getNumber());
                saleInvoiceSolrDoc.getMultiProjectIdName().add(SolrUtils.getIdName(edsProject.getObjectID(), edsProject.getName()));
                saleInvoiceSolrDoc.getMultiProjectNumberName().add(edsProject.getNumber() + SolrSaleInvoiceRepresenter.ARROW + edsProject.getName());
            });
        }
        if (invoice.getStatus() != null) {
            saleInvoiceSolrDoc.setStatusId(invoice.getStatus().getObjectID());
            saleInvoiceSolrDoc.setStatusName(invoice.getStatus().getName());
            saleInvoiceSolrDoc.setStatusIdName(SolrUtils.getIdName(invoice.getStatus().getObjectID(), invoice.getStatus().getName()));
            saleInvoiceSolrDoc.setStatusSorder(invoice.getStatus().getSorder());
            saleInvoiceSolrDoc.setStatusCode(invoice.getStatus().getCode());
        }
        if (invoice.getShippingMethod() != null) {
            saleInvoiceSolrDoc.setShppingMethodId(invoice.getShippingMethod().getObjectID());
            saleInvoiceSolrDoc.setShppingMethodName(invoice.getShippingMethod().getName());
            saleInvoiceSolrDoc.setShppingMethodIdName(SolrUtils.getIdName(invoice.getShippingMethod().getObjectID(), invoice.getShippingMethod().getName()));
        }
        if (invoice.getCreator() != null) {
            saleInvoiceSolrDoc.setCreatorId(invoice.getCreator().getObjectID());
            saleInvoiceSolrDoc.setCreatorName(invoice.getCreator().getName());
            saleInvoiceSolrDoc.setCreatorIdName(SolrUtils.getIdName(invoice.getCreator().getObjectID(), invoice.getCreator().getName()));
        }
        saleInvoiceSolrDoc.setInvoiceNumber(invoice.getNumber());
        saleInvoiceSolrDoc.setInvoiceDate(invoice.getInvoiceDate());
        saleInvoiceSolrDoc.setDueDate(invoice.getDueDate());
        saleInvoiceSolrDoc.setPoNumber(invoice.getPoNumber());
        saleInvoiceSolrDoc.setReference(invoice.getReference());
        saleInvoiceSolrDoc.setQuoteNumber(invoice.getQuoteNumber());
        saleInvoiceSolrDoc.setIntroduction(invoice.getIntroduction());
        if (invoice.getQuotePercent() != null) {
            saleInvoiceSolrDoc.setQuotePercent(invoice.getQuotePercent().doubleValue());
        }
        saleInvoiceSolrDoc.setProjectBased(invoice.isProjectBasedInvoice());
        saleInvoiceSolrDoc.setTaxCalculationType(invoice.getTaxCalculationType());
        if (invoice.getPdfTemplate() != null) {
            saleInvoiceSolrDoc.setPdfTemplateId(invoice.getPdfTemplate().getObjectID());
        }

        if (!CollectionUtils.isEmpty(invoice.getInvoiceItems())) {
            invoice.getInvoiceItems().stream()
                    .filter(edsInvoiceItem -> edsInvoiceItem.getItem() != null)
                    .forEach(edsInvoiceItem -> {
                        saleInvoiceSolrDoc.getItemId().add(edsInvoiceItem.getItem().getObjectID());
                        if (edsInvoiceItem.getWarehouse() != null) {
                            saleInvoiceSolrDoc.getWarehouseId().add(edsInvoiceItem.getWarehouse().getObjectID());
                        }
                    });
        }
        BigDecimal fullPayment = invoice.getFullPayments();
        BigDecimal totalCurrency = invoice.getTotalInInvoiceCurrency() != null ? invoice.getTotalInInvoiceCurrency() :
                invoice.getTotal().multiply(invoice.getExchangeRate());

        saleInvoiceSolrDoc.setCreditNode(invoice.isCreditNote());
        saleInvoiceSolrDoc.setTotalTaxes(invoice.getTotalTaxes() != null ? invoice.getTotalTaxes().doubleValue() : 0d);
        saleInvoiceSolrDoc.setExchargeRate(invoice.getExchangeRate().doubleValue());
        saleInvoiceSolrDoc.setTotalInvoiceCurrency(totalCurrency.doubleValue());
        saleInvoiceSolrDoc.setTotalInvoiceBase(invoice.getTotal().doubleValue());
        saleInvoiceSolrDoc.setPaidAmount(fullPayment.doubleValue());
        saleInvoiceSolrDoc.setSubTotal(invoice.getSubtotal().doubleValue());

        saleInvoiceSolrDoc.setDueAmount(totalCurrency.doubleValue() - fullPayment.doubleValue());
        saleInvoiceSolrDoc.setInTarget(invoice.isInTarget());
        saleInvoiceSolrDoc.setHasPayment(fullPayment.compareTo(BigDecimal.ZERO) > 0);
        if (invoice.getCurrentApprover() != null && invoice.getCurrentApprover().getExactEmployee() != null) {
            saleInvoiceSolrDoc.setCurrentApproverId(invoice.getCurrentApprover().getExactEmployee().getObjectID());
            saleInvoiceSolrDoc.setCurrentApproverName(invoice.getCurrentApprover().getExactEmployee().getFullName());
            saleInvoiceSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(invoice.getCurrentApprover().getExactEmployee().getObjectID(), invoice.getCurrentApprover().getExactEmployee().getFullName()));
        }
        if (!ServerUtils.isNullOrEmpty(invoice.getZatcaStatus())) {
            saleInvoiceSolrDoc.setZatcaStatus(invoice.getZatcaStatus());
        }
        if (invoice.getType().equals(Constants.RECEIVABLE)) {
            Set<Integer> st = new HashSet<>();
            List<EdsInvoiceItem> invoiceItems = invoice.getInvoiceItems();
            for (int i = 0; i < invoiceItems.size(); i++) {
                if (invoiceItems.get(i).getItem() != null) {
                    st.add(invoiceItems.get(i).getItem().getObjectID());
                }
            }
            if (!st.isEmpty()) {
                for (Integer i : st
                ) {
                    saleInvoiceSolrDoc.getProductIdsFromInvoice().add(i);
                }
            }
        }
        if (!CollectionUtils.isEmpty(invoice.getInvoiceItems())) {
            invoice.getInvoiceItems().stream()
                    .filter(edsInvoiceItem -> edsInvoiceItem.getItem() != null)
                    .forEach(edsInvoiceItem -> {
                        saleInvoiceSolrDoc.getProductName().add(edsInvoiceItem.getItem().getName());
                    });
        }
        saleInvoiceSolrDoc.setCreatedDate(invoice.getCreationDate());
        saleInvoiceSolrDoc.setUpdatedDate(invoice.getUpdatedDate());
        CustomFieldsUtils.setSolrDocDynamicFields(saleInvoiceSolrDoc, invoice.getCustomFields());
        return saleInvoiceSolrDoc;
    }

    public Page<SaleInvoiceSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
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
                    case InvoiceList.CREATED_DATE ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_CREATED_DATE);
                    case InvoiceList.UPDATED_DATE ->
                            Sort.by(sortDirection, SolrSaleInvoiceRepresenter.FIELD_UPDATED_DATE);
                    default ->
                            CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
                };
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_SALEINVOICE_CORE, query, SaleInvoiceSolrDoc.class);
    }

    public FacetFilterRpc getSaleInvoiceFacetFilterData(FacetFilterRpc invoiceFacet) {
        if (!invoiceFacet.isFilterChanges()) {
            invoiceFacet = commonServiceLocal.getUserFacetFilter(invoiceFacet);
        }

        invoiceFacet = applyNonConvertedFilterPeriod(invoiceFacet);

        StringBuilder solrQuery = new StringBuilder();
        String selectedDate = null;
        if (invoiceFacet.getSelectedDateSolrCodeName() != null) {
            selectedDate = "DUE_DATE".equals(invoiceFacet.getSelectedDateSolrCodeName()) ? "DUE_DATE" : "INVOICE_DATE".equals(invoiceFacet.getSelectedDateSolrCodeName()) ? "INVOICE_DATE" : null;
        }
        EdsUser edsUser = employeeManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(invoiceFacet.getSearchKey());
        fp.setStartDate(invoiceFacet.getStartDate());
        fp.setEndDate(invoiceFacet.getEndDate());
        fp.setFacetFilter(invoiceFacet);
        solrQuery.append(invoiceCircularResolver.getSaleInvoiceSolrQuery(fp, edsUser, false, selectedDate));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                invoiceFacet,
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(invoiceFacet, edsUser.getCompany(), SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_DATE,
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9],
                FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_SALEINVOICE_CORE, solrQuery.toString(), invoiceFacet, SaleInvoiceSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, invoiceFacet);
        return getSaleInvoiceFacetResultFromSolr(facetPage, invoiceFacet);
    }

    private FacetFilterRpc getSaleInvoiceFacetResultFromSolr(QueryResponse resp, FacetFilterRpc invoiceFacet) {
        int num = 0;
        if (invoiceFacet.getFacetContentMap().containsKey(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2])) {
            FacetField amountFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
            if (amountFacet != null && amountFacet.getValues() != null) {
                num = 0;
                int lessThan100 = 0, from100To500 = 0, from500To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
                for (FacetField.Count count : amountFacet.getValues()) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 500) {
                        from100To500 += count.getCount();
                    } else if (501 <= total && total <= 1000) {
                        from500To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
                SelectItem[] amount = new SelectItem[6];
                amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
                amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

                amount[1] = new SelectItem("[100 TO 500]".hashCode(), "[100 TO 500]");
                amount[1].setDescription("100.00 - 500.00 ( <b>" + from100To500 + "</b> )");

                amount[2] = new SelectItem("[500 TO 1000]".hashCode(), "[500 TO 1000]");
                amount[2].setDescription("500.00 - 1,000.00 ( <b>" + from500To1000 + "</b> )");

                amount[3] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
                amount[3].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

                amount[4] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
                amount[4].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

                amount[5] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
                amount[5].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2]).setFacetItems(amount);
            } else {
                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[2]).setFacetItems(new SelectItem[0]);
            }
        }
        if (invoiceFacet.getFacetContentMap().containsKey(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3])) {
            FacetField paidAmountFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_PAID_AMOUNT);
            if (paidAmountFacet != null && paidAmountFacet.getValues() != null) {
                num = 0;
                int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
                for (FacetField.Count count : paidAmountFacet.getValues()) {
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

                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3]).setFacetItems(paidAmount);
            } else {
                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[3]).setFacetItems(new SelectItem[0]);
            }
        }

        if (invoiceFacet.getFacetContentMap().containsKey(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7])) {
            FacetField totalInvoiceFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY);
            if (totalInvoiceFacet != null && totalInvoiceFacet.getValues() != null) {
                num = 0;
                int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
                for (FacetField.Count count : totalInvoiceFacet.getValues()) {
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
                SelectItem[] totalInvoice = new SelectItem[5];
                totalInvoice[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
                totalInvoice[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00 ( <b>" + lessThan100 + "</b> )");

                totalInvoice[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
                totalInvoice[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

                totalInvoice[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
                totalInvoice[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

                totalInvoice[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
                totalInvoice[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

                totalInvoice[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
                totalInvoice[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7]).setFacetItems(totalInvoice);
            } else {
                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7]).setFacetItems(new SelectItem[0]);
            }
        }
        if (invoiceFacet.getFacetContentMap().containsKey(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9])) {
            FacetField amountFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_IN_TARGET);
            if (amountFacet != null && amountFacet.getValues() != null) {
                int yes = 0, no = 0;
                for (FacetField.Count count : amountFacet.getValues()) {
                    if ("true".equalsIgnoreCase(count.getName())) {
                        yes += count.getCount();
                    } else {
                        no += count.getCount();
                    }
                }
                SelectItem[] amount = new SelectItem[2];
                amount[0] = new SelectItem("true".hashCode(), "true");
                amount[0].setDescription("Yes  ( <b>" + yes + "</b> )");

                amount[1] = new SelectItem("false".hashCode(), "false");
                amount[1].setDescription("No ( <b>" + no + "</b> )");

                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9]).setFacetItems(amount);
            } else {
                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9]).setFacetItems(new SelectItem[0]);
            }
        }
        if (invoiceFacet.getFacetContentMap().containsKey(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10])) {
            FacetField creditNoteFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_IS_CREDITNODE);
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
                type[0].setDescription("Credit Notes  ( <b>" + yes + "</b> )");

                type[1] = new SelectItem("false".hashCode(), "false");
                type[1].setDescription("Sales Invoices ( <b>" + no + "</b> )");

                invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]).setFacetItems(type);
            } else {
                if (invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]) != null) {
                    invoiceFacet.getFacetContentMap().get(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10]).setFacetItems(new SelectItem[0]);
                }
            }
        }
        return invoiceFacet;
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
}
