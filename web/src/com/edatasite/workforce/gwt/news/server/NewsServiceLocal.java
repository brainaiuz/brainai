package com.edatasite.workforce.gwt.news.server;

import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import org.apache.solr.client.solrj.SolrQuery;

public interface NewsServiceLocal {

    NewsData getNews(Integer objectId);

    NewsComment[] getNewsComments(Integer objectId);

    SelectItem[] getUsersByNews();

    SelectItem[] getNewsCategories();

    SolrQuery getSolrQueryForNews(ListingFilterParameter fp);

}
