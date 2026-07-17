package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.solr.document.ChartOfAccountSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.ChartOfAccountSolrDocRepository;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CHART_OF_ACCOUNT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:24.
 */
@Component
public class ChartOfAccountSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(ChartOfAccountSolrComponent.class);

    @Resource
    private ChartOfAccountSolrComponent chartOfAccountComponent;

    @Autowired
    private ChartOfAccountSolrDocRepository repository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsAccount account) throws InterruptedException {
        chartOfAccountComponent.indexes(Collections.singletonList(account));
    }

    @Transactional
    public void indexes(List<EdsAccount> accounts) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(accounts)) {
            List<ChartOfAccountSolrDoc> chartOfAccountSolrDocs = new ArrayList<>();

            for (EdsAccount account : accounts) {
                if (account != null) {
                    try {
                        chartOfAccountSolrDocs.add(createChartOfAccountDocument(account.getSolrRPC(), companyId));
                        log.info("Indexed ChartOfAccount Core CID - {}, objId - {}", companyId, account.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on ChartOfAccount with id {}, and error message {} **********************", account.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!chartOfAccountSolrDocs.isEmpty()) {
                log.info("========= Create ChartOfAccount solr docs for company {} with size {} =========", companyId, chartOfAccountSolrDocs.size());
                repository.saveAll(chartOfAccountSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsAccount> accounts) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(accounts)) {
            ConcurrentLinkedQueue<ChartOfAccountSolrDoc> chartOfAccountSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsAccount account : accounts) {
                if (account != null) {
                    AccountItem solrRPC = account.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {

                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(account), () -> {
                                        chartOfAccountSolrDocs.add(createChartOfAccountDocument(solrRPC, companyId));
                                        log.info("Indexed ChartOfAccount Core CID - {}, objId - {}", companyId, account.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on ChartOfAccount with id {}, and error message {} **********************", account.getObjectID(), e);
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
                log.error("Error on loading ChartOfAccount list", e);
            }

            if (!chartOfAccountSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create ChartOfAccount solr docs for company {} with size {} =========", companId, chartOfAccountSolrDocs.size());
                    repository.saveAll(chartOfAccountSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving ChartOfAccount solr docs", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EdsAccount account) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + account.getObjectID();
    }

    private ChartOfAccountSolrDoc createChartOfAccountDocument(AccountItem account, Integer companyId) {
        ChartOfAccountSolrDoc accountSolrDoc = new ChartOfAccountSolrDoc();

        accountSolrDoc.setOid(SolrUtils.generatedOId(companyId, account.getId()));
        accountSolrDoc.setCompanyId(companyId);
        accountSolrDoc.setAccountId(account.getId());
        accountSolrDoc.setCode(account.getCode());
        accountSolrDoc.setChartOfAccountKey(Integer.valueOf(account.getKey()));
        accountSolrDoc.setName(account.getName());
        accountSolrDoc.setActive(account.isActive());
        accountSolrDoc.setLastUpdatedDate(account.getLastUpdatedDate());
        if (account.getParent() != null) {
            accountSolrDoc.setParentId(account.getParent().getId());
            accountSolrDoc.setParentName(account.getParent().getName());
            accountSolrDoc.setParentIdName(SolrUtils.getIdName(account.getParent().getId(), account.getParent().getName()));
        }
        if (account.getCurrency() != null) {
            accountSolrDoc.setCurrencyId(account.getCurrency().getId());
            accountSolrDoc.setCurrencyName(account.getCurrency().getName());
            accountSolrDoc.setCurrencyIdName(SolrUtils.getIdName(account.getCurrency().getId(), account.getCurrency().getName()));
        }
        if (account.getAccountType() != null) {
            accountSolrDoc.setTypeId(account.getAccountType().getId());
            accountSolrDoc.setTypeName(account.getAccountType().getName());
            accountSolrDoc.setTypeCode(account.getAccountType().getCode());
            accountSolrDoc.setTypeCategory(account.getAccountType().getCategory());
            accountSolrDoc.setTypeIdName(SolrUtils.getIdName(account.getAccountType().getId(), account.getAccountType().getName()));
        }
        accountSolrDoc.setBankAccountActive(account.isBankAccountActive());

        return accountSolrDoc;
    }

    public Page<ChartOfAccountSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrChartOfAccountRepresenter.FIELD_LAST_UPDATED_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                solrSort = switch (fp.getSortField()) {
                    case AccountListItem.NAME -> Sort.by(order, SolrChartOfAccountRepresenter.SORTABLE_NAME);
                    case AccountListItem.CODE -> Sort.by(order, SolrChartOfAccountRepresenter.SORTABLE_CODE);
                    case AccountListItem.PARENT -> Sort.by(order, SolrChartOfAccountRepresenter.SORTABLE_PARENT_NAME);
                    case AccountListItem.TYPE -> Sort.by(order, SolrChartOfAccountRepresenter.SORTABLE_TYPE_NAME);
                    case AccountListItem.CURRENCY ->
                            Sort.by(order, SolrChartOfAccountRepresenter.SORTABLE_CURRENCY_NAME);
                    default -> Sort.by(order, SolrChartOfAccountRepresenter.FIELD_LAST_UPDATED_DATE);
                };
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_CHART_OF_ACCOUNT_CORE, query, ChartOfAccountSolrDoc.class);
    }

    public FacetFilterRpc getChartOfAccountFacetFilterData(FacetFilterRpc chartOfAccountFacetFilter) {
        if (!chartOfAccountFacetFilter.isFilterChanges()) {
            chartOfAccountFacetFilter = commonServiceLocal.getUserFacetFilter(chartOfAccountFacetFilter);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(chartOfAccountFacetFilter.getSearchKey());
        fp.setStartDate(chartOfAccountFacetFilter.getStartDate());
        fp.setEndDate(chartOfAccountFacetFilter.getEndDate());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getChartOfAccountSolrQuery(fp));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(chartOfAccountFacetFilter, company,
                SolrChartOfAccountRepresenter.FIELD_LAST_UPDATED_DATE,
                SolrChartOfAccountRepresenter.FIELD_LAST_UPDATED_DATE));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CHART_OF_ACCOUNT_CORE, solrQuery.toString(), chartOfAccountFacetFilter, ChartOfAccountSolrDoc.class);
        return SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, chartOfAccountFacetFilter);
    }
}
