package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:07:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NewsManager extends Manager<EdsNews> {
    List<EdsNews> getCompanyNews(ListingFilterParameter fp);

    List<EdsNews> getWebsiteNews(Integer websiteID, ListingFilterParameter fp);

    List<EdsNews> getWebsiteNews(Integer websiteID, ListingFilterParameter fp, Integer start, Integer limit);
    List<EdsNews> getNewsBySupplier(Integer supplierId, Integer companyId);


    List<EdsNews> getCompanyNewsForSolr(SolrReindexRpc solrReindex, Integer startAt, Integer limit);

    List<EdsNews> getCompanyNews(Integer companyID);

    List<EdsNews> getCompanyNewsWithoutCategory();

    List<EdsNews> getUndeletedNewsIn(String ids);

    Long getUserNewsCount(EdsUser user);

    List<EdsNews> getNetworkNewsList(Integer networkID);

    List<EdsNews> getCompanyNewsBlogList(boolean isBlog, ListLoadConfig config);

    List<EdsNews> getAdminPostedNewsBlogsList(boolean isBlog);

    EdsNews getNetworkNews(Integer networkNewsID);

    Long getUserNetworkNewsBlogsCount(boolean isBlog);

    List<Integer> getUndeletedNewsIdList(String ids);

    List<Integer> getNewsIdListWithLimit(Integer companyID, int startat, int limit);

    List<EdsUser> getCompanyNews2();

    List<Integer> getCompanyDeletedNewsFolrSolr(SolrReindexRpc solrReindex);
}
