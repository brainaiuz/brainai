package com.edatasite.workforce.gwt.news.server;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.solr.component.NewsSolrComponent;
import com.edatasite.workforce.core.solr.document.NewsSolrDoc;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.NewsCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import com.edatasite.workforce.gwt.news.client.rpc.NewsListItem;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.CommonParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * Created by Virus on 4/15/14.
 */
@Service("newsService")
public class NewServiceImpl extends RemoteServiceServlet implements NewsService, NewsServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(NewServiceImpl.class);
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private NewsCategoryManager newsCategoryManager;
    @Autowired
    private NewsCommentManager newsCommentManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private CommonService commonService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private ReportService reportService;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private NewsSolrComponent newsSolrComponent;

    @Override
    @Transactional
    public Integer saveNews(NewsData data) {
        EdsNews news;
        if (data.getObjectId() != null) {
            news = newsManager.get(data.getObjectId());
        } else {
            news = new EdsNews();
        }
        news.setSubject(data.getSubject());
        news.setShortDescription(data.getShortDescription());
        news.setFullText(data.getFullDescription());
        news.setShowHomePage(data.getShowHomePage());
        news.setNews(data.isNews());
        news.setTopNews(data.isTopNews());
        news.setFeatures(data.isFeatures());
        news.setIsPressRelease(data.isPressRelease());
        news.setIsOpinion(data.isOpinion());
        news.setIsWhitePaper(data.isWhitePaper());
        news.setOwner(data.getOwner());
        news.setSponsoredArticle(data.isSponsoredArticle());
        news.setEventArchive(data.isEventArchive());
        if (data.getCreatorId() != null && data.getAuthor() != null) {
            news.setAuthor(data.getAuthor());
            news.setUser(userManager.get(data.getCreatorId()));
        } else {
            news.setUser(newsCategoryManager.getUser());
        }
        if (data.getLocationID() != null) {
            news.setLocation(locationManager.get(data.getLocationID()));
        } else {
            news.setLocation(null);
        }
        news.setDate(data.getPublishedDate());

        EdsAttachment file = null;
        EdsAttachment image = null;
        if (data.getFileId() != null && data.getFileId() > 0) {
            file = attachmentManager.get(data.getFileId());
            if (isImage(file))
                image = attachmentManager.get(data.getFileId());
        }
        news.setImage(image);
        news.setFile(file);
        List<EdsNewsCategory> categories = new ArrayList<>();
        if (data.getCategories() != null && data.getCategories().size() > 0) {
            for (NewsCategory category : data.getCategories()) {
                if (category != null && category.getId() != null) {
                    EdsNewsCategory newsCategory = newsCategoryManager.get(category.getId());
                    if (newsCategory != null) {
                        categories.add(newsCategory);
                    }
                }
            }
        }
        news.setCategories(categories);

        Set<EdsCrmAccount> suppliers = new HashSet<>();
        if (data.getSuppliers() != null && data.getSuppliers().size() > 0 && data.getShowOptions()) {
            for (CrmAccountItem supplier : data.getSuppliers()) {
                if (supplier != null) {
                    EdsCrmAccount s = crmAccountManager.get(supplier.getObjectId());
                    if (s != null) {
                        suppliers.add(s);
                    }
                }
            }
        }

        if (data.getShowOptions() != null && data.getShowOptions()) {
            news.setSuppliers(suppliers);
        }

        news.setVisibility(data.isVisibility());
        EdsUser user = news.getUser();
        boolean newCreated = newsManager.createOrUpdate(news);

        if (data.getFileItems() != null && data.getFileItems().length > 0) {
            attachmentUtilsManager.saveAttachments(F_NEWS, news.getObjectID(), news.getObjectID(), data.getFileItems());
        }
        if (newCreated && news.getLocation() != null) {
            baseEventPostProcessor.registerEvent(NewsCustomEventListenerImpl.TYPE, NewsCustomEventListenerImpl.EVENT_NEWS_NOTIFICATION, news, user);
        } else if (!newCreated && news.getLocation() != null) {
            baseEventPostProcessor.registerEvent(NewsCustomEventListenerImpl.TYPE, NewsCustomEventListenerImpl.EVENT_NEWS_UPDATE_NOTIFICATION, news, user);

        }
        try {
            newsSolrComponent.index(news);
            if (newCreated) {
                //If main transaction will fail we have to remove news from solr index to avoid DB&Solr inconsistency
                solrTransactionManager.registerEvent(SolrEvent.NEWS_ADD, news, user.getCompany());
            } else {
                solrTransactionManager.registerEvent(SolrEvent.NEWS_REMOVE, news, user.getCompany());
            }
        } catch (InterruptedException e) {
            log.error("SAVE_NEWS ERROR:" + e.getMessage(), e);
            baseEventPostProcessor.registerEvent(NewsCustomEventListenerImpl.TYPE, NewsCustomEventListenerImpl.EVENT_NEWS_ADD_TO_SOLR, news, user);
        }
        return news.getObjectID();
    }

    private boolean isImage(EdsAttachment file) {
        if (file.getOriginalName() != null && !file.getOriginalName().equals("")) {
            if (file.getOriginalName().toLowerCase().lastIndexOf(".jpg") != -1 ||
                    file.getOriginalName().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                    file.getOriginalName().toLowerCase().lastIndexOf(".gif") != -1 ||
                    file.getOriginalName().toLowerCase().lastIndexOf(".png") != -1 ||
                    file.getOriginalName().toLowerCase().lastIndexOf(".ico") != -1 ||
                    file.getOriginalName().toLowerCase().lastIndexOf(".bmp") != -1 || file.getContentType().contains("image")) {
                return true;
            }
        }
        return false;
    }

    public SelectItem[] getEmployeeSelectItem() {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        ArrayList<SelectItem> res = new ArrayList<>();
        List<EdsEmployee> employees = employeeManager.getEmployees(company);
        for (EdsEmployee employee : employees) {
            if (!employee.getDeleted()) {
                res.add(new SelectItem(employee.getObjectID(), employee.getFullName()));
            }
        }
        return res.toArray(new SelectItem[]{});
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsData getNews(Integer newsId) {
        NewsData data = new NewsData();
        data.setLocationItems(reportService.getLocationList());
        if (newsId != null) {
            EdsNews news = newsManager.get(newsId);
            data.setObjectId(news.getObjectID());
            data.setSubject(news.getSubject());
            data.setPublishedDate(news.getDate());
            data.setShortDescription(news.getShortDescription());
            data.setFullDescription(news.getFullText());
            data.setShowHomePage(news.getShowHomePage() != null ? news.getShowHomePage() : false);
            data.setNews(news.isNews() != null ? news.isNews() : false);
            data.setTopNews(news.isTopNews() != null ? news.isNews() : false);
            data.setFeatures(news.isFeatures() != null ? news.isFeatures() : false);
            data.setIsPressRelease(news.isPressRelease() != null ? news.isPressRelease() : false);
            data.setIsOpinion(news.isOpinion() != null ? news.isOpinion() : false);
            data.setIsWhitePaper(news.isWhitePaper() != null ? news.isWhitePaper() : false);
            data.setSponsoredArticle(news.getSponsoredArticle() != null ? news.getSponsoredArticle() : false);
            data.setEventArchive(news.getEventArchive() != null ? news.getEventArchive() : false);
            data.setAuthor(news.getAuthor() != null ? news.getAuthor() : news.getUser().getFullName());
            data.setCreatorId(news.getUser().getObjectID());
            if (news.getImage() != null) {
                data.setImageId(news.getImage().getObjectID());
                data.setImageName(news.getImage().getOriginalName());
                data.setImageUrl(commonService.getImageUrl(news.getImage().getObjectID()));
            }
            if (news.getFile() != null) {
                data.setFileId(news.getFile().getObjectID());
                data.setFileName(news.getFile().getOriginalName());
                data.setFileLink(commonService.getImageUrl(news.getFile().getObjectID()));
                data.setFileContentType(news.getFile().getContentType());
            }
            if (news.getLocation() != null) {
                data.setLocationID(news.getLocation().getObjectID());
                data.setLocation(news.getLocation().getName());
            }
            ArrayList<NewsCategory> categories = new ArrayList<>();

            for (EdsNewsCategory category : news.getCategories()) {
                categories.add(new NewsCategory(category.getObjectID(), category.getName()));
            }

            data.setCategories(categories);

            ArrayList<CrmAccountItem> suppliers = new ArrayList<>();
            for (EdsCrmAccount supplier : news.getSuppliers()) {
                CrmAccountItem crmAccountItem = new CrmAccountItem();
                crmAccountItem.setObjectId(supplier.getObjectID());
                suppliers.add(crmAccountItem);
            }

            data.setSuppliers(suppliers);
            data.setCreatorName(news.getUser().getName());
            NewsComment[] comments = getNewsComments(newsId);
            ArrayList<NewsComment> commentList = new ArrayList<>();

            Collections.addAll(commentList, comments);
            data.setComments(commentList);
            data.setVisibility(news.getVisibility() != null ? news.getVisibility() : false);

            List<EdsNewsComment> cList = newsCommentManager.getComments(newsId);
            HistoryListItem[] commListItem = new HistoryListItem[cList.size()];
            int i = 0;
            for (EdsNewsComment newsComment : cList) {
                HistoryListItem item = new HistoryListItem(newsComment.getComment());
                item.setObjectID(newsComment.getObjectID());
                item.setRelatedId(newsId);
                item.setEventDate(newsComment.getDate());
                if (newsComment.isSuperUser()) {
                    item.setEmployee(defaultSupportName);
                } else {
                    item.setEmployee(newsComment.getUser() != null ? newsComment.getUser().getFullName() : "");
                }
                commListItem[i] = item;
                i++;
            }

            data.setCommentList(commListItem);
        }
        return data;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getNewsCategories() {
        List<EdsNewsCategory> categories = newsCategoryManager.getCategories();
        SelectItem[] items = new SelectItem[categories.size()];
        int i = 0;
        for (EdsNewsCategory c : categories) {
            items[i] = new SelectItem(c.getObjectID(), c.getName());
            i++;
        }
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<NewsCategory> getNewsCategoriesByFilter(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        List<EdsNewsCategory> categories = newsCategoryManager.list(fp);
        Integer totalCount = newsCategoryManager.getListCount(fp);

        NewsCategory[] items = new NewsCategory[categories.size()];
        int i = 0;

        for (EdsNewsCategory category : categories) {
            items[i] = new NewsCategory();
            items[i].setId(category.getObjectID());
            items[i].setName(category.getName());
            i++;
        }

        ListResult<NewsCategory> list = new ListResult<>();

        list.setList(new ArrayList<>(Arrays.asList(items)));
        list.setTotal(totalCount);

        return list;
    }

    @Transactional
    @Override
    public Boolean deleteNewsCategory(Integer id) {
        EdsNewsCategory category = newsCategoryManager.get(id);
        if (category != null) {
            category.setDeleted(true);
            newsCategoryManager.update(category);
            return true;
        }

        return false;
    }

    private String getUserImageURL(Integer userId) {
        String url = "";
        EdsUser edsUser = userManager.get(userId);
        if (edsUser.getPhoto() != null) {
            url = commonService.getImageUrl(edsUser.getPhoto().getObjectID());
        } else {
            url = null;
        }
        return url;
    }

    @Transactional
    @Override
    public void saveNewsComment(HistoryListItem data) {
        EdsNewsComment comment;
        if (data.getObjectID() != null) {
            comment = newsCommentManager.get(data.getObjectID());
            if (comment == null) {
                comment = new EdsNewsComment();
            }
        } else {
            comment = new EdsNewsComment();
        }
        EdsNews edsNews = newsManager.get(data.getRelatedId());
        comment.setComment(data.getComment());
        EdsUser user = newsManager.getUser();
        comment.setDate(new Date());
        comment.setUser(user);
        comment.setNews(edsNews);
        comment.setSuperUser(ServerUtils.isSuperUser());
        newsCommentManager.createOrUpdate(comment);

        try {
            newsSolrComponent.index(edsNews);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public NewsComment[] getNewsComments(Integer newsID) {
        List<EdsNewsComment> cList = newsCommentManager.getComments(newsID);
        NewsComment[] comments = new NewsComment[cList.size()];
        int i = 0;
        for (EdsNewsComment c : cList) {
            comments[i] = new NewsComment();
            comments[i].setUsername(c.isSuperUser() ? defaultSupportName : c.getUser().getName());
            comments[i].setDate(new Date(c.getDate().getTime()));
            comments[i].setEmployeeImageUrl(getUserImageURL(c.getUser().getObjectID()));
            comments[i].setComment(c.getComment());
            comments[i].setNewsId(newsID);
            comments[i].setCommentId(c.getObjectID());
            i++;
        }
        Arrays.sort(comments, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return comments;
    }

    @Override
    @Transactional
    public void deleteNewsComment(Integer newsCommentId) {
        EdsNewsComment noteComment = newsCommentManager.get(newsCommentId);
        newsCommentManager.delete(noteComment);
    }

    @Override
    @Transactional
    public void deleteNews(Integer newsId) {
        EdsNews news = newsManager.get(newsId);
        news.setDeleted(true);
        newsManager.update(news);
        try {
            solrManager.removeCompanyNewsByIds(newsId);
        } catch (SolrServerException | IOException e) {
            baseEventPostProcessor.registerEvent(NewsCustomEventListenerImpl.TYPE, NewsCustomEventListenerImpl.EVENT_NEWS_DELETE_FROM_SOLR, news, newsManager.getUser());
        }
    }

    @Override
    @Transactional
    public ListResult<NewsListItem> getNewsList(ListingFilterParameter fp) {
        /*SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_NEWS_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSolrQueryForNews(fp), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }*/
        Page<NewsSolrDoc> newsSolrDocPage = newsSolrComponent.getList(fp);
        return getNewsFromSolrResult(newsSolrDocPage);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getUsersByNews() {
        List<EdsUser> userList = newsManager.getCompanyNews2();
        SelectItem[] items = new SelectItem[userList.size()];
        int i = 0;
        for (EdsUser user : userList) {
            items[i] = user.getAsSelectItem();
            i++;
        }
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsCategory getNewsCategory(Integer id) {
        EdsNewsCategory category = newsCategoryManager.get(id);
        NewsCategory result = new NewsCategory();

        if (category != null) {
            result.setId(category.getObjectID());
            result.setName(category.getName());
            if (category.getParent() != null) {
                result.setParentId(category.getParent().getObjectID());
            }

            return result;
        }

        return null;
    }

    @Override
    @Transactional
    public Integer saveNewsCategory(NewsCategory newsCategory) {
        EdsNewsCategory category = new EdsNewsCategory();
        if (newsCategory.getId() != null) {
            category = newsCategoryManager.get(newsCategory.getId());
        }

        category.setName(newsCategory.getName());
        EdsNewsCategory parent = null;
        if (newsCategory.getParentId() != null && newsCategory.getParentId() > 0) {
            parent = newsCategoryManager.get(newsCategory.getParentId());
        }
        category.setParent(parent);

        if (newsCategory.getId() != null && newsCategory.getId() > 0) {
            newsCategoryManager.update(category);
        } else {
            newsCategoryManager.create(category);
        }

        return category.getObjectID();
    }

    @Transactional
    public boolean deleteNewsImage(Integer newsId, Integer imageID) {
        if (newsId != null) {
            EdsNews edsNews = newsManager.get(newsId);
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsNewsCategory.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(edsNews.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Delete news image");
            edsNews.setImage(null);
            edsNews.setImageUrl(null);
            edsNews.setFile(null);
            newsManager.update(edsNews);
        }
        EdsUpload upload = (EdsUpload) uploadManager.get(imageID);

        try {
            uploadManager.deleteFile(upload);
            uploadManager.delete(upload);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private ListResult<NewsListItem> getNewsFromSolrResult(Page<NewsSolrDoc> newsSolrDocPage) {
        ArrayList<NewsListItem> newsListItems = new ArrayList<>();
        int totalCount = 0;
        if (newsSolrDocPage != null && newsSolrDocPage.getContent() != null && newsSolrDocPage.getContent().size() > 0) {
            totalCount = (int) newsSolrDocPage.getTotalElements();
            for (NewsSolrDoc relevantDoc : newsSolrDocPage.getContent()) {
                NewsListItem item = new NewsListItem();
                EdsNews edsNews = newsManager.get(relevantDoc.getNewsId());
                if (edsNews != null) {
                    item.setObjectId(relevantDoc.getNewsId());
                    item.setLogoURL(edsNews.getImage() != null ? commonService.getImageUrl(edsNews.getImage().getObjectID()) : null);
                    item.setSubject(relevantDoc.getSubject());
                    item.setDate(relevantDoc.getDate());
                    item.setPostedBy(relevantDoc.getUser());
                    for (String category : relevantDoc.getFieldCategoryName()) {
                        item.setCategoryName(category);
                    }
                    item.setLocationName(relevantDoc.getFieldLocation());
                    item.setComments(relevantDoc.getFieldComments());
                    item.setFullText(relevantDoc.getFullText());
                    item.setVisibility(relevantDoc.isFieldNewsVisibility());
                    item.setShortDescription(edsNews.getShortDescription());
                    newsListItems.add(item);
                }
            }
        }
        return new ListResult<>(newsListItems, totalCount);
    }

    @Override
    @Transactional
    public SolrQuery getSolrQueryForNews(ListingFilterParameter fp) {
        EdsUser user = newsManager.getUser();
        EdsCompany company = user.getCompany();

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getWorkspaceNewsListCore(fp, user, company));
        query.setStart(fp.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));

        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = false;
                if (Constants.DESC == fp.asConfig().getSortDir()) {
                    desc = true;
                }
                if (NewsListItem.SUBJECT.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.SORTABLE_SUBJECT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (NewsListItem.DATE.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.FIELD_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (NewsListItem.POSTED_BY.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.SORTABLE_USER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (NewsListItem.VISIBILITY.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.FIELD_NEWS_VISIBILITY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (NewsListItem.CATEGORY.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.SORTABLE_CATEGORY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (NewsListItem.LOCATION.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.FIELD_LOCATION_ID_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (NewsListItem.COMMENT.equals(fp.getSortField())) {
                    query.setSort(SolrNewsRepresenter.FIELD_COMMENTS, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrNewsRepresenter.FIELD_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

}
