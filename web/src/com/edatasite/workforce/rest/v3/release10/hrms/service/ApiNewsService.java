package com.edatasite.workforce.rest.v3.release10.hrms.service;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.CategoryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.edatasite.workforce.gwt.news.server.NewsServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.NewsDTO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Service
public class ApiNewsService implements Constants {

    protected static final Integer GAP_BTW_STATIC_AND_CUSTOM_FIELDS = 100000;
    protected static String customFieldFileNameRegex = "custom_field_(\\d*)_attachment_(\\d+)";
    private final NewsServiceLocal newsServiceLocal;
    private final NewsService newsService;
    private final NewsManager newsManager;
    private final LocationManager locationManager;
    private final EmployeeManager employeeManager;
    private final CategoryManager categoryManager;
    private final DocumentsServiceLocal documentsServiceLocal;
    private final AttachmentUtilsManager attachmentUtilsManager;

    @Autowired
    public ApiNewsService(NewsService newsService, NewsServiceLocal newsServiceLocal, NewsManager newsManager, LocationManager locationManager, EmployeeManager employeeManager, CategoryManager categoryManager, WfmCommandServiceLocal wfmCommandServiceLocal, DocumentsServiceLocal documentsServiceLocal, AttachmentUtilsManager attachmentUtilsManager) {
        this.newsServiceLocal = newsServiceLocal;
        this.newsService = newsService;
        this.newsManager = newsManager;
        this.locationManager = locationManager;
        this.employeeManager = employeeManager;
        this.categoryManager = categoryManager;
        this.documentsServiceLocal = documentsServiceLocal;
        this.attachmentUtilsManager = attachmentUtilsManager;
    }

    public ListResultTO<NewsDTO> getNewsList(ListingFilterParameter fp) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_NEWS_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(newsServiceLocal.getSolrQueryForNews(fp), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        ListResultTO<NewsDTO> news = new ListResultTO<>();
        if (resp != null) {
            List<Integer> ids = resp.getResults().stream().map(doc -> Objects.requireNonNull(SolrUtils.asInteger(doc, SolrNewsRepresenter.FIELD_NEWS_ID))).toList();
            news.setTotalNumber(ids.size());
            ArrayList<NewsDTO> items = new ArrayList<>();
            ids.forEach(id -> {
                NewsData item = newsService.getNews(id);
                List<FileResource> files = attachmentUtilsManager.getAttachments(F_NEWS, id, id);
                items.add(ConvertUtils.toDto(item, files));
            });
            news.setItems(items);
        }

        return news;
    }

    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Integer id) throws RestException {
        Optional.ofNullable(newsManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "News with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));
        NewsData item = newsService.getNews(id);
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_NEWS, id, id);
        return ConvertUtils.toDto(item, files);
    }

    @Transactional
    public Integer save(final NewsDTO news, boolean isNew) throws RestException {
        NewsData item;
        if (!isNew && news.getId() != null) {
            item = Optional.ofNullable(newsService.getNews(news.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "News with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
        } else {
            item = new NewsData();
        }

        item.setSubject(news.getSubject());
        item.setShortDescription(news.getShortText());
        item.setFullDescription(news.getFullText());
        item.setShowHomePage(false);
        item.setNews(true);
        item.setTopNews(false);
        item.setSponsoredArticle(false);
        item.setEventArchive(false);
        if (news.getLocation() != null) {
            Optional.ofNullable(locationManager.get(news.getLocation().getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Location with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
            item.setLocationID(news.getLocation().getId());
        }
        if (news.getAuthor() != null) {
            Optional.ofNullable(employeeManager.get(news.getAuthor().getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Author with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
            item.setAuthor(news.getAuthor().getName());
            item.setCreatorId(news.getAuthor().getId());
        }
        item.setPublishedDate(news.getDate());
        item.setVisibility(news.isInternal());
        item.setFeatures(false);
        item.setIsPressRelease(false);
        item.setIsOpinion(false);
        item.setIsWhitePaper(false);

        ArrayList<NewsCategory> categories = new ArrayList<>();
        if (news.getCategories() != null && !news.getCategories().isEmpty()) {
            Optional.ofNullable(categoryManager.get(news.getCategories().get(0).getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Category with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
            NewsCategory newsCategory = new NewsCategory(news.getCategories().get(0).getId(), news.getCategories().get(0).getCode());
            categories.add(newsCategory);
            item.setCategories(categories);
        }

        Integer objectId = newsService.saveNews(item);

        return objectId;
    }

    @Transactional
    public Integer savePatch(final NewsDTO newsDTO) throws RestException {

        Optional.ofNullable(newsManager.get(newsDTO.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "News with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));

        NewsData item = newsService.getNews(newsDTO.getId());

        Optional.ofNullable(newsDTO.getSubject()).ifPresent(item::setSubject);
        Optional.ofNullable(newsDTO.getShortText()).ifPresent(item::setShortDescription);
        Optional.ofNullable(newsDTO.getFullText()).ifPresent(item::setFullDescription);

        Optional.ofNullable(newsDTO.getLocation()).ifPresent(loc -> {
            try {
                Optional.ofNullable(locationManager.get(newsDTO.getLocation().getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Location with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
            } catch (RestException e) {
                e.printStackTrace();
            }
            item.setLocationID(newsDTO.getLocation().getId());
        });
        Optional.ofNullable(newsDTO.getAuthor()).ifPresent(auth -> {
            try {
                Optional.ofNullable(employeeManager.get(newsDTO.getAuthor().getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Author with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
            } catch (RestException e) {
                e.printStackTrace();
            }
            item.setAuthor(newsDTO.getAuthor().getName());
            item.setCreatorId(newsDTO.getAuthor().getId());
        });

        Optional.ofNullable(newsDTO.getDate()).ifPresent(item::setPublishedDate);
        Optional.of(newsDTO.isInternal()).ifPresent(item::setVisibility);

        Optional.ofNullable(newsDTO.getCategories()).ifPresent(cat -> {
            Optional.ofNullable(newsDTO.getCategories().get(0).getId()).ifPresent(c -> {
                try {
                    Optional.ofNullable(categoryManager.get(newsDTO.getCategories().get(0).getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Category with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
                } catch (RestException e) {
                    e.printStackTrace();
                }
                item.getCategories().get(0).setId(newsDTO.getCategories().get(0).getId());
            });
            Optional.ofNullable(newsDTO.getCategories().get(0).getCode()).ifPresent(item.getCategories().get(0)::setName);
        });

        return newsService.saveNews(item);
    }
}
