package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:08:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("newsManager")
public class NewsManagerImpl extends BaseManager<EdsNews> implements NewsManager {

    private RoleManager roleManager;

    public NewsManagerImpl() {
        super(EdsNews.class);
    }

    DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    public List<EdsNews> getCompanyNews(ListingFilterParameter fp) {
        String s = "";
        Date selectDate = null;
        String sDate = "";

        if (fp.isValidSearchKey()) {
            s += " and (";
            s += "lower(n.subject) like '" + fp.getSqlSearchKey() + "'";
            s += " or lower(n.user.firstName) like '" + fp.getSqlSearchKey() + "'";
            s += " or lower(n.user.lastName) like '" + fp.getSqlSearchKey() + "'";
            s += ") ";
        }
        //search by date
        if (!"".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 0) {
            try {
                selectDate = format.parse(fp.getGroupByName());
                sDate = format2.format(selectDate);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            s += " and to_char(n.date, 'yyyy-mm-dd')='" + sDate + "' ";
        }
        //search by category
        if (!"".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 1) {
        }
        //search by user
        if (!"".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 2) {
            s += " and '" + fp.getGroupByName() + "'=CONCAT(n.user.firstName, CONCAT(' ', n.user.lastName)) ";
        }

        if (getUser() != null && getUser().isClientContact()) {
            s += "and n.visibility = true";
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            s += " and (n.date between '" + fp.getEndDate() + "' and '" + fp.getStartDate() + "')";
        }

        return find("select n from EdsNews n where (n.deleted = null or n.deleted<>true)" + s + " order by n.date desc");
    }

    public List<EdsNews> getNewsBySupplier(Integer supplierId, Integer companyId) {
        String query = "select * from \"" + companyId + "\".news n\n" +
                "inner join \"" + companyId + "\".newssupplierrelation nr on n.id=nr.newsid\n" +
                "where nr.supplierid=" + supplierId + " order by n.id";
        return findNative(query, EdsNews.class);
    }

    public List<EdsNews> getWebsiteNews(Integer websiteID, ListingFilterParameter fp) {
        return getWebsiteNews(websiteID, fp, null, null);
    }

    public List<EdsNews> getWebsiteNews(Integer websiteID, ListingFilterParameter fp, Integer start, Integer limit) {
        String s = "";

        if (fp.isValidSearchKey()) {
            s += " AND (";
            s += "lower(n.subject) LIKE '" + fp.getSqlSearchKey() + "'";
            s += " OR lower(n.user.firstName) LIKE '" + fp.getSqlSearchKey() + "'";
            s += " OR lower(n.user.lastName) LIKE '" + fp.getSqlSearchKey() + "'";
            s += ") ";
        }

        //Sort by date
        if (fp.getSortDir() != null && fp.getSortDir() > 0) {
            s += " ORDER BY n.date desc";
            if (fp.getSortDir().equals(2)) { //default asc
                s += " desc";
            }
        }


        if (start != null && limit != null) {
            return findInterval("SELECT n FROM EdsWebsite w JOIN w.newsCategory c JOIN c.news n WHERE n.deleted<>true and w.objectID = ? " + s, start, limit, websiteID);
        }

        return find("SELECT n FROM EdsWebsite w JOIN w.newsCategory c JOIN c.news n WHERE n.deleted<>true and w.objectID = ? " + s, websiteID);
    }

    @Override
    public List<EdsNews> getUndeletedNewsIn(String ids) {
        return (List<EdsNews>) find("SELECT ns FROM EdsNews ns WHERE ns.id IN (" + ids + ") AND (ns.deleted IS NULL or ns.deleted<>true)");
    }

    public List<Integer> getCompanyDeletedNewsFolrSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsNews ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    public List<EdsNews> getCompanyNewsForSolr(SolrReindexRpc solrReindex, Integer startAt, Integer limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder newsSqlQuery = new StringBuilder();
        newsSqlQuery.append("select n from EdsNews n where (n.deleted is null or n.deleted<>true) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            newsSqlQuery.append(" AND n.lastUpdateTime >= :updatedDate ");
            if (solrReindex.getLastUpdateEndTime() != null) {
                newsSqlQuery.append(" and n.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        newsSqlQuery.append("order by n.objectID ASC ");
        return findIntervalByNamedParams(newsSqlQuery.toString(), startAt, limit, params);
    }

    @Override
    public List<Integer> getNewsIdListWithLimit(Integer companyID, int startAt, int limit) {
        String query = "SELECT ns.id FROM \"" + companyID + "\".news ns WHERE (ns.deleted is null or ns.deleted<>true) AND ns.id >" + startAt + " order by ns.id asc limit " + limit;
        return findNative(query);
    }

    @Override
    public List<EdsUser> getCompanyNews2() {
     return find("select distinct n.user from EdsNews n where n.deleted is null or n.deleted = false");
    }

    public List<EdsNews> getCompanyNews(Integer companyID) {
        return find("SELECT ns FROM EdsNews ns WHERE ns.deleted is NULL or ns.deleted<>true");
    }

    @Override
    public List<EdsNews> getCompanyNewsWithoutCategory() {
        return find("SELECT ns FROM EdsNews ns left join ns.categories c WHERE ns.deleted is not true and c.objectID is NULL ORDER BY ns.date desc");
    }

    public Long getUserNewsCount(EdsUser user) {
        return (Long) findSingle("select count(news.objectID) from EdsNews news where news.user.objectID = ? and (news.deleted is null or news.deleted <> true)", user.getObjectID());
    }

    public List<EdsNews> getNetworkNewsList(Integer networkID) {
        String s = "";
        if (getUser().isClientContact()) {
            s = " AND nn.visibility = true";
        }
        return (List<EdsNews>) findNative("select * FROM " + getCompanyId() + ".news nn WHERE nn.id in (select newsid from " + getCompanyId() + ".newsnetworkrelation nr where networkid=" + networkID + ") " +
                " AND (nn.deleted <> true or nn.deleted is null) " + s + " ORDER BY nn.date DESC", EdsNews.class);
    }

    public List<EdsNews> getCompanyNewsBlogList(boolean isBlog, ListLoadConfig config) {
        String s = "";
        String sortBy = "";
        if (getUser().isClientContact()) {
            s = " AND news.visibility = true";
        }
        sortBy = "ORDER BY news.date DESC";
        if (config != null && config.getSortField() != null) {
            if (config.getSortField().equals("subject") || config.getSortField().equals("date")) {
                sortBy = "ORDER BY news." + config.getSortField();
            }
            if (config.getSortField().equals("postedby")) {
                sortBy = "ORDER BY news.user.firstName";
            }
            if (config.getSortDir() == Constants.DESC) {
                sortBy += " DESC";
            }
        } else {
            sortBy = "ORDER BY news.date DESC";
        }

        return find("FROM EdsNews news WHERE (news.deleted <> true OR news.deleted IS NULL) " + s +
                " and news.isBlog = ? " + sortBy, isBlog);
    }

    public List<EdsNews> getAdminPostedNewsBlogsList(boolean isBlog) {
        String s = "";
        if (getUser().isClientContact()) {
            s = " AND news.visibility = true";
        }
        List<EdsNews> newsList = find("FROM EdsNews news WHERE (news.deleted <> true OR news.deleted IS NULL) " + s +
                " news.isBlog = ? ORDER BY news.date DESC", isBlog);

        List<EdsNews> result = new ArrayList<>(newsList);

        for (EdsNews news : newsList) {
            if (news.getUser() != null && !news.getUser().hasRole(roleManager.get(EdsRole.ADMIN))) {
                result.remove(news);
            }
        }
        return result;
    }

    public EdsNews getNetworkNews(Integer networkNewsID) {
        return (EdsNews) findSingle("FROM EdsNews nn WHERE nn.objectID = ? AND " +
                "nn.deleted <> true", networkNewsID);
    }

    public Long getUserNetworkNewsBlogsCount(boolean isBlog) {
        return (Long) findSingle("SELECT COUNT(nn.objectID) FROM EdsNews nn WHERE nn.user=? AND " +
                "(nn.deleted <> true OR nn.deleted IS NULL) AND nn.isBlog=?", getUser(), isBlog);
    }

    @Override
    public List<Integer> getUndeletedNewsIdList(String ids) {
        return (List<Integer>) find("SELECT ns.id FROM EdsNews ns WHERE ns.id IN (" + ids + ") AND (ns.deleted IS NULL or ns.deleted<>true)");
    }
}
