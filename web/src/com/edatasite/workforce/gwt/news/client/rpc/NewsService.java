package com.edatasite.workforce.gwt.news.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by Virus on 4/15/14.
 */
public interface NewsService extends RemoteService {

    Integer saveNews(NewsData data);

    NewsData getNews(Integer objectId);

    SelectItem[] getEmployeeSelectItem();

    SelectItem[] getNewsCategories();

    ListResult<NewsCategory> getNewsCategoriesByFilter(ListingFilterParameter filterParametrs);

    Boolean deleteNewsCategory(Integer id);

    void saveNewsComment(HistoryListItem comments);

    void deleteNewsComment(Integer commentId);

    void deleteNews(Integer objectId);

    ListResult<NewsListItem> getNewsList(ListingFilterParameter filterParametrs);

    NewsCategory getNewsCategory(Integer objectID);

    Integer saveNewsCategory(NewsCategory newsCategory);

    boolean deleteNewsImage(Integer categoryID, Integer imageID);

    class App {
        public static NewsServiceAsync get() {
            ServiceDefTarget target = GWT.create(NewsService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/newsService");
            return (NewsServiceAsync) target;
        }
    }
}
