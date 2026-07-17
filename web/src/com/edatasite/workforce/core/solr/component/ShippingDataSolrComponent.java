package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.solr.document.ShippingDataSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.ShippingDataSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQSolrItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataSolrItem;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
@Component
public class ShippingDataSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(ShippingDataSolrComponent.class);

    @Autowired
    private ShippingDataSolrDocRepository shippingDataSolrDocRepository;

    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsShippingData shippingData) throws InterruptedException {
        this.indexes(Arrays.asList(shippingData));
    }

    @Transactional
    public void indexes(List<EdsShippingData> shippingData) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(shippingData)) {
            List<ShippingDataSolrDoc> shippingDataSolrDocs = new ArrayList<>();

            for (EdsShippingData edsShippingData : shippingData) {
                if (Objects.nonNull(edsShippingData)) {
                    try {
                        shippingDataSolrDocs.add(createShippingDataDocument(edsShippingData.getSolrRPC(), companyId));
                        log.info("Indexed ShippingData Core CID - {}, objId - {}", companyId, edsShippingData.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on ShippingDataDocument with id = {} **********************", edsShippingData.getObjectID());
                        throw e;
                    }
                }
            }

            if (!shippingDataSolrDocs.isEmpty()) {
                log.info("========= Create ShippingDataDocument solr docs for company {} with size {} =========", companyId, shippingDataSolrDocs.size());
                shippingDataSolrDocRepository.saveAll(shippingDataSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsShippingData> shippingData) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(shippingData)) {
            ConcurrentLinkedQueue<ShippingDataSolrDoc> shippingDataSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsShippingData edsShippingData : shippingData) {
                if (Objects.nonNull(edsShippingData)) {
                    ShippingDataSolrItem solrRPC = edsShippingData.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        shippingDataSolrDocs.add(createShippingDataDocument(solrRPC, companyId));
                                        log.info("Indexed ShippingData Core CID - {}, objId - {}", companyId, edsShippingData.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on ShippingDataDocument with id = {} **********************", edsShippingData.getObjectID());
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
                log.error("Error on loading ShippingDataDocument list", e);
            }

            if (!shippingDataSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create ShippingDataDocument solr docs for company {} with size {} =========", companyId, shippingDataSolrDocs.size());
                    shippingDataSolrDocRepository.saveAll(shippingDataSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving ShippingDataDocument list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(ShippingDataSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectId();
    }

    private ShippingDataSolrDoc createShippingDataDocument(ShippingDataSolrItem shippingData, Integer companyId) {
        ShippingDataSolrDoc shippingDataSolrDoc = new ShippingDataSolrDoc();
        Integer invoiceId = shippingDataManager.getGrnGdnRelatedInvoiceNumber(shippingData.getObjectId());
        EdsInvoice invoice = invoiceManager.get(invoiceId);
        shippingDataSolrDoc.setOid(SolrUtils.generatedOId(companyId, shippingData.getObjectId()));
        shippingDataSolrDoc.setCompanyId(companyId);
        shippingDataSolrDoc.setShippingDataId(shippingData.getObjectId());
        shippingDataSolrDoc.setShippingDataNumber(shippingData.getShippingDataNumber());
        shippingDataSolrDoc.setShippingDate(shippingData.getShippingDate());
        if (shippingData.getClient() != null) {
            shippingDataSolrDoc.setClientId(shippingData.getClient().getId());
            shippingDataSolrDoc.setClientName(shippingData.getClient().getName());
            shippingDataSolrDoc.setClientIdName(SolrUtils.getIdName(shippingData.getClient().getId(), shippingData.getClient().getName()));
        }
        if (shippingData.getCurrency() != null) {
            shippingDataSolrDoc.setCurrencyId(shippingData.getCurrency().getId());
            shippingDataSolrDoc.setCurrencyName(shippingData.getCurrency().getName());
            shippingDataSolrDoc.setCurrencyIdName(SolrUtils.getIdName(shippingData.getCurrency().getId(), shippingData.getCurrency().getName()));
        }
        if (shippingData.getQuoteNumber() != null) {
            shippingDataSolrDoc.setQuoteNumber(shippingData.getQuoteNumber());
        }
        if (shippingData.getShippingDataStatusName() != null) {
            shippingDataSolrDoc.setShippingDataStatusName(shippingData.getShippingDataStatusName());
        }
        if (shippingData.getCreator() != null) {
            shippingDataSolrDoc.setCreatorId(shippingData.getCreator().getId());
            shippingDataSolrDoc.setCreatorName(shippingData.getCreator().getName());
            shippingDataSolrDoc.setCreatorIdName(SolrUtils.getIdName(shippingData.getCreator().getId(), shippingData.getCreator().getName()));
            if (shippingData.getCreatorLocationId() != null) {
                shippingDataSolrDoc.setCreatorLocationId(shippingData.getCreatorLocationId());
            }
        }
        if (shippingData.getCreationDate() != null) {
            shippingDataSolrDoc.setCreationDate(shippingData.getCreationDate());
        }
        shippingDataSolrDoc.setGdn(shippingData.getGdn());
        if (invoice != null) {
            shippingDataSolrDoc.setInvoiceNumber(invoice.getNumber());
            shippingDataSolrDoc.setSaleInvoiceId(invoice.getObjectID());
            shippingDataSolrDoc.setInvoiceDate(invoice.getInvoiceDate());
            shippingDataSolrDoc.setDueDate(invoice.getDueDate());

            if (invoice.getStatus() != null) {
                shippingDataSolrDoc.setStatusId(invoice.getStatus().getObjectID());
                shippingDataSolrDoc.setStatusName(invoice.getStatus().getName());
                shippingDataSolrDoc.setStatusIdName(SolrUtils.getIdName(invoice.getStatus().getObjectID(), invoice.getStatus().getName()));
                shippingDataSolrDoc.setStatusSorder(invoice.getStatus().getSorder());
                shippingDataSolrDoc.setStatusCode(invoice.getStatus().getCode());
            }
        }
        if (shippingData.getQuoteId() != null) {
            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(shippingData.getQuoteId());
            if (saleQuote != null) {
                shippingDataSolrDoc.setGdnIsSalesOrder(saleQuote.isSalesOrder());
            }
        }
        if (!shippingData.getWarehouseIds().isEmpty()) {
            shippingDataSolrDoc.getWarehouseId().addAll(shippingData.getWarehouseIds());
        }
        return shippingDataSolrDoc;
    }

    public Page<ShippingDataSolrDoc> getList(ListingFilterParameter fp, String toString) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(toString));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case ShippingData.NUMBER ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.SORTABLE_SHIPPING_DATA_NUMBER);
                    case ShippingData.DATE -> solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATE);
                    case ShippingData.SUPPLIER ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.SORTABLE_CLIENT_NAME);
                    case ShippingData.ORDER_NUMBER ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER);
                    case ShippingData.CREATOR ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME);
                    case ShippingData.INVOICE_NUMBER ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.SORTABLE_INVOICE_NUMBER);
                    case ShippingData.STATUS ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_STATUS_NAME);
                    case ShippingData.INVOICE_STATUS ->
                            solrSort = Sort.by(order, SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_SHIPPING_DATA_CORE, query, ShippingDataSolrDoc.class);
    }

    public FacetFilterRpc getGdnGrnFacetFilterData(FacetFilterRpc facetFilterRpc, boolean isGdn) {
        EdsUser user = userManager.getUser();
        if (!facetFilterRpc.isFilterChanges()) {
            facetFilterRpc = commonServiceLocal.getUserFacetFilter(facetFilterRpc);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(facetFilterRpc.getSearchKey());
        fp.setViewType(facetFilterRpc.getName());
        fp.setIsGdn(isGdn);

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(quoteServiceLocal.getShippingDataSolrQuery(fp, user));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilterRpc, company, null, null, null));


        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_SHIPPING_DATA_CORE, solrQuery.toString(), facetFilterRpc, ShippingDataSolrDoc.class);
        return SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, facetFilterRpc);
    }
}
