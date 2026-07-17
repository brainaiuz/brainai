//package com.edatasite.workforce.gwt.core.server.db.network;
//
//import com.edatasite.workforce.core.domain.network.EdsNetworkNews;
//import com.edatasite.workforce.gwt.core.server.db.Manager;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by IntelliJ IDEA.
// * User: Ruslan Muhammadov
// * Date: May 10, 2010
// * Time: 4:58:38 PM
// * To change this template use File | Settings | File Templates.
// */
//public interface NetworkNewsManager extends Manager<EdsNetworkNews> {
//
//    List<EdsNetworkNews> getAdminPostedNewsBlogsList(boolean isBlog);
//
//    List<EdsNetworkNews> getCompanyNewsBlogList(boolean isBlog);
//
//    List<EdsNetworkNews> getCompanyNetworkItemList();
//
//    List<EdsNetworkNews> getNetworkNewsIdsIn(String ids);
//
//    List<EdsNetworkNews> getCompanyNetworkNewsList(Integer companyId, Integer start, Integer limit);
//
//    List<EdsNetworkNews> getCompanyNewsBlogList(boolean isBlog, String keyword);
//
//    List<EdsNetworkNews> getCompanyNewsListByDate(Date date);
//
//    EdsNetworkNews getNetworkNews(Integer networkNewsID);
//
//    List<EdsNetworkNews> getNetworkNewsList(Integer networkID);
//
//    Long getNewsNumber(Integer networkID);
//
//    List<EdsNetworkNews> getNetworkNewsBlogs(boolean isBlog);
//
//    Long getUserNetworkNewsBlogsCount(boolean isBlog);
//}
