//package com.edatasite.workforce.gwt.core.server.db.impl.network;
//
//import com.edatasite.workforce.core.domain.EdsRole;
//import com.edatasite.workforce.core.domain.network.EdsNetworkNews;
//import com.edatasite.workforce.gwt.core.server.db.RoleManager;
//import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
//import com.edatasite.workforce.gwt.core.server.db.network.NetworkNewsManager;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.*;
//
///**
// * Created by IntelliJ IDEA.
// * User: Ruslan Muhammadov
// * Date: May 10, 2010
// * Time: 5:00:26 PM
// * To change this template use File | Settings | File Templates.
// */
//
//@SuppressWarnings("unchecked")
//public class NetworkNewsManagerImpl extends BaseManager<EdsNetworkNews> implements NetworkNewsManager {
//
//    private RoleManager roleManager;
//
//    public NetworkNewsManagerImpl() {
//        super(EdsNetworkNews.class);
//    }
//
//    @Autowired
//    public void setRoleManager(RoleManager roleManager) {
//        this.roleManager = roleManager;
//    }
//
//    public List<EdsNetworkNews> getAdminPostedNewsBlogsList(boolean isBlog) {
//        List<EdsNetworkNews> newsList = find("FROM EdsNetworkNews news WHERE news.deleted <> true AND " +
//                "news.isBlog = ? ORDER BY news.createdOrUpdatedDate DESC", isBlog);
//
//        List<EdsNetworkNews> result = new ArrayList<EdsNetworkNews>();
//        for (EdsNetworkNews news : newsList) {
//            result.add(news);
//        }
//
//        for (EdsNetworkNews news : newsList) {
//            if (news.getCreator() != null && !news.getCreator().hasRole(roleManager.get(EdsRole.ADMIN))) {
//                result.remove(news);
//            }
//        }
//        return result;
//    }
//
//    public List<EdsNetworkNews> getCompanyNewsBlogList(boolean isBlog) {
//        return find("FROM EdsNetworkNews news WHERE news.deleted <> true " +
//                "and news.isBlog = ? ORDER BY news.createdOrUpdatedDate DESC", isBlog);
//    }
//
//    public List<EdsNetworkNews> getCompanyNetworkItemList() {
//        return find("FROM EdsNetworkNews news WHERE (news.deleted <> true or news.deleted IS NULL)");
//    }
//
//    public List<EdsNetworkNews> getNetworkNewsIdsIn(String ids) {
//        return (List<EdsNetworkNews>) find("SELECT nn FROM EdsNetworkNews nn WHERE nn.objectID IN(" + ids + ") AND (nn.deleted<>true OR deleted IS NULL)");
//    }
//
//    public List<EdsNetworkNews> getCompanyNetworkNewsList(Integer companyId, Integer start, Integer limit) {
//        return (List<EdsNetworkNews>) findNative(" SELECT * FROM \"" + companyId + "\".networknews nn WHERE nn.id>" + start + " AND (nn.deleted<>true OR deleted IS NULL) AND nn.network_id is not null  ORDER BY nn.id ASC LIMIT " + limit, EdsNetworkNews.class);
//    }
//
//    public List<EdsNetworkNews> getCompanyNewsBlogList(boolean isBlog, String keyword) {
//        String keys = "";
//        for (String key : keyword.split(" ")) {
//            if (key.length() > 2) {
//                key = "%" + key + "%";
//                keys += key;
//            }
//        }
//        return find("FROM EdsNetworkNews news WHERE news.deleted <> true " +
//                "and news.isBlog = ? and (lower(news.subject) like lower('" + keys + "') or lower(news.description) like lower('" + keys + "')) ORDER BY news.createdOrUpdatedDate DESC", isBlog);
//    }
//
//    public List<EdsNetworkNews> getCompanyNewsListByDate(Date date) {
//        GregorianCalendar startDate = new GregorianCalendar();
//        startDate.setTime(date);
//        startDate.set(Calendar.HOUR, 0);
//        startDate.set(Calendar.MINUTE, 0);
//        startDate.set(Calendar.SECOND, 0);
//        startDate.set(Calendar.MILLISECOND, 0);
//
//        GregorianCalendar endDate = new GregorianCalendar();
//        endDate.setTime(date);
//        endDate.set(Calendar.HOUR, 23);
//        endDate.set(Calendar.MINUTE, 59);
//        endDate.set(Calendar.SECOND, 59);
//
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("startDate", startDate.getTime());
//        params.put("endDate", endDate.getTime());
//        return findByNamedParams("FROM EdsNetworkNews news WHERE " +
//                "news.delete <> true AND news.createdOrUpdatedDate >= :startDate AND " +
//                "news.createdOrUpdatedDate <= :endDate ORDER BY news.createdOrUpdatedDate DESC", params);
//    }
//
//    public EdsNetworkNews getNetworkNews(Integer networkNewsID) {
//        return (EdsNetworkNews) findSingle("FROM EdsNetworkNews nn WHERE nn.objectID = ? AND " +
//                "nn.deleted <> true", networkNewsID);
//    }
//
//    public List<EdsNetworkNews> getNetworkNewsList(Integer networkID) {
//        return find("FROM EdsNetworkNews nn WHERE nn.network.objectID = ? " +
//                "AND nn.deleted <> true ORDER BY nn.createdOrUpdatedDate DESC", networkID);
//    }
//
//
//    public Long getNewsNumber(Integer networkID) {
//        return (Long) findSingle("SELECT COUNT(nn.objectID) FROM EdsNetworkNews nn " +
//                "WHERE nn.network.objectID = ? AND nn.deleted <> true", networkID);
//    }
//
//    public List<EdsNetworkNews> getNetworkNewsBlogs(boolean isBlog) {
//        return find("FROM EdsNetworkNews news WHERE news.deleted <> true AND" +
//                " news.isBlog = ? ORDER BY news.createdOrUpdatedDate DESC", isBlog);
//    }
//
//    public Long getUserNetworkNewsBlogsCount(boolean isBlog) {
//        return (Long) findSingle("SELECT COUNT(nn.objectID) FROM EdsNetworkNews nn WHERE (nn.creator = ? OR " +
//                "nn.network IS NULL) AND nn.deleted <> true AND nn.isBlog = ?", getUser(), isBlog);
//    }
//}
