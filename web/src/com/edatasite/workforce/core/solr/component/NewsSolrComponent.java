package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.solr.document.NewsSolrDoc;
import com.edatasite.workforce.core.solr.repository.NewsSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.news.client.rpc.NewsListItem;
import com.edatasite.workforce.gwt.news.client.rpc.NewsSolr;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_NEWS_CORE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
@Component
public class NewsSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(NewsSolrComponent.class);

    @Autowired
    private NewsSolrDocRepository newsSolrDocRepository;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsNews edsNews) throws InterruptedException {
        this.indexes(Arrays.asList(edsNews));
    }

    @Transactional
    public void indexes(List<EdsNews> edsNewsList) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsNewsList)) {
            List<NewsSolrDoc> newsSolrDocs = new ArrayList<>();

            for (EdsNews edsNews : edsNewsList) {
                if (Objects.nonNull(edsNews) && Boolean.TRUE.equals(!edsNews.getDeleted())) {
                    try {
                        newsSolrDocs.add(createNewsDocument(edsNews.getRPC(), companyId));
                        log.info("Indexed News Core CID - {}, objId - {}", companyId, edsNews.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on News with id = {} **********************", edsNews.getObjectID());
                        throw e;
                    }
                }
            }
            if (!newsSolrDocs.isEmpty()) {
                log.info("========= Create News solr docs for company {} with size {} =========", companyId, newsSolrDocs.size());
                newsSolrDocRepository.saveAll(newsSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsNews> edsNewsList) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsNewsList)) {
            ConcurrentLinkedQueue<NewsSolrDoc> newsSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsNews edsNews : edsNewsList) {
                if (Objects.nonNull(edsNews) && Boolean.TRUE.equals(!edsNews.getDeleted())) {
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(edsNews),
                                    () -> {
                                        newsSolrDocs.add(createNewsDocument(edsNews.getRPC(), companyId));
                                        log.info("Indexed News Core CID - {}, objId - {}", companId, edsNews.getObjectID());
                                    });
                        } catch (Exception e) {
                            log.error("********************* Error on News with id = {} **********************", edsNews.getObjectID());
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
                log.error("Error on loading News list", e);
            }

            if (!newsSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create News solr docs for company {} with size {} =========", companyId, newsSolrDocs.size());
                    newsSolrDocRepository.saveAll(newsSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving News list", e);
                }
            }
        }
    }

    private String getSynchronizedKey(EdsNews edsNews) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + edsNews.getObjectID();
    }

    private NewsSolrDoc createNewsDocument(NewsSolr edsNews, Integer companyId) {
        NewsSolrDoc newsSolrDoc = new NewsSolrDoc();

        newsSolrDoc.setOid(SolrUtils.generatedOId(companyId, edsNews.getId()));
        newsSolrDoc.setCompanyId(companyId);
        newsSolrDoc.setNewsId(edsNews.getId());
        newsSolrDoc.setSubject(edsNews.getSubject());
        newsSolrDoc.setSubjectComposite(edsNews.getSubject());
        newsSolrDoc.setComposite((edsNews.getSubject() != null ? edsNews.getSubject() : "") + (edsNews.getFullText() != null ? edsNews.getFullText() : ""));
        newsSolrDoc.setDate(edsNews.getDate());
        newsSolrDoc.setCreationDate(edsNews.getCreationTime());
        newsSolrDoc.setFullText(edsNews.getFullText());
        if (edsNews.getUser() != null) {
            Integer userId = edsNews.isAnonym() ? 0 : edsNews.getUser().getId();
            String userName = edsNews.isAnonym() ? "Anonymous" : edsNews.getUser().getFullname();
            newsSolrDoc.setUser(userName);
            newsSolrDoc.setUserId(userId);
            newsSolrDoc.setUserIdName(SolrUtils.getIdName(userId, userName));
        }
        newsSolrDoc.setFieldNewsVisibility(edsNews.isVisible());
        newsSolrDoc.setFieldNewsIsGeneral(edsNews.isGeneralNews());
        newsSolrDoc.setFieldIsBlog(edsNews.isBlog());
        int categoriesCount = 1;
        if (edsNews.getCategories() != null && !edsNews.getCategories().isEmpty()) {
            categoriesCount = edsNews.getCategories().size() - 1;

            for (int b = 0; b <= categoriesCount; b++) {
                if (edsNews.isDeleted()) {
                    b = categoriesCount + 1;
                    continue;
                }
                if (edsNews.getCategories().get(b) != null) {
                    newsSolrDoc.getFieldCategoryId().add(edsNews.getCategories().get(b).getId());
                    newsSolrDoc.getFieldCategoryName().add(edsNews.getCategories().get(b).getName());
                }
            }
        }
        String text = "";
        if (edsNews.isGeneralNews()) {
            if (edsNews.getUser().getCompanyId() == 8934) {
                if (edsNews.isBlog()) {
                    text = "Thought Leadership";
                } else {
                    text = "News";
                }
            } else if (edsNews.getUser().getCompanyId() == 5377) {
                if (edsNews.isBlog()) {
                    text = "Opinion";
                } else {
                    text = "News";
                }
            }
        } else {
            if (edsNews.getUser().getCompanyId() == 5377) {
                if (edsNews.isBlog()) {
                    text = "Network Opinion";
                } else {
                    text = "Network News";
                }
            } else if (edsNews.getUser().getCompanyId() == 8934) {
                if (edsNews.isBlog()) {
                    text = "Network Discussion";
                } else {
                    text = "Network News";
                }
            }
        }
        newsSolrDoc.setFieldNewsType(text);
        if (edsNews.isGeneralNews()) {
            if (edsNews.getOwnerName() != null && !"".equals(edsNews.getOwnerName())) {
                String userName = edsNews.getOwnerName();
                if (edsNews.isAnonym()) {
                    userName = "Anonymous";
                }
                newsSolrDoc.setFieldNewsOwner(userName);
            } else {
                if (edsNews.isAnonym()) {
                    String userName = "Anonymous";
                    newsSolrDoc.setFieldNewsOwner(userName);
                }
            }
        } else {
            String userName = "";
            if (edsNews.getUser() != null) {
                userName = edsNews.getUser().getFullname();
            }
            if (edsNews.isAnonym()) {
                userName = "Anonymous";
            }
            newsSolrDoc.setFieldNewsOwner(userName);
        }
        if (edsNews.getLocation() != null) {
            SelectItem location = edsNews.getLocation();
            newsSolrDoc.setFieldLocationId(location.getId());
            newsSolrDoc.setFieldLocation(location.getName());
            newsSolrDoc.setFieldLocationIdName(SolrUtils.getIdName(location.getId(), location.getName()));
        }
        if (edsNews.getViewCount() != null) {
            newsSolrDoc.setFieldComments(edsNews.getViewCount());
        }
        return newsSolrDoc;
    }

    public Page<NewsSolrDoc> getList(ListingFilterParameter fp) {
        EdsUser user = newsManager.getUser();
        EdsCompany company = user.getCompany();

        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(QueryBuilderForSolr.getWorkspaceNewsListCore(fp, user, company)));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrNewsRepresenter.FIELD_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = !fp.isAscending();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                if (NewsListItem.SUBJECT.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.SORTABLE_SUBJECT);
                } else if (NewsListItem.DATE.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.FIELD_DATE);
                } else if (NewsListItem.POSTED_BY.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.SORTABLE_USER);
                } else if (NewsListItem.VISIBILITY.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.FIELD_NEWS_VISIBILITY);
                } else if (NewsListItem.CATEGORY.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.SORTABLE_CATEGORY);
                } else if (NewsListItem.LOCATION.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.FIELD_LOCATION_ID_NAME);
                } else if (NewsListItem.COMMENT.equals(fp.getSortField())) {
                    solrSort = Sort.by(sortDirection, SolrNewsRepresenter.FIELD_COMMENTS);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_NEWS_CORE, query, NewsSolrDoc.class);
    }
}
