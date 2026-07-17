package com.edatasite.workforce.gwt.news.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Virus on 4/15/14.
 */
public interface NewsServiceAsync {

    void saveNews(NewsData data, AsyncCallback<Integer> callback);

    void getNews(Integer objectId, AsyncCallback<NewsData> abstractAsyncCallback);

    void getEmployeeSelectItem(AsyncCallback<SelectItem[]> async);

    void getNewsCategories(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getNewsCategoriesByFilter(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<NewsCategory>> abstractAsyncCallback);

    void deleteNewsCategory(Integer id, AsyncCallback<Boolean> abstractAsyncCallback);

    void saveNewsComment(HistoryListItem comments, AsyncCallback<Void> abstractAsyncCallback);

    void deleteNewsComment(Integer commentId, AsyncCallback<Void> abstractAsyncCallback);

    void deleteNews(Integer objectId, AsyncCallback<Void> abstractAsyncCallback);

    void getNewsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<NewsListItem>> asyncCallback);

    void getNewsCategory(Integer objectID, AsyncCallback<NewsCategory> abstractAsyncCallback);

    void saveNewsCategory(NewsCategory newsCategory, AsyncCallback<Integer> abstractAsyncCallback);

    void deleteNewsImage(Integer newsId, Integer imageID, AsyncCallback<Boolean> async);

}
