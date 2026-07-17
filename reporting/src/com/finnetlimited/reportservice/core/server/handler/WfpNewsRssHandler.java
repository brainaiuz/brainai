package com.finnetlimited.reportservice.core.server.handler;

import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsNewsCategory;
import com.edatasite.workforce.gwt.core.server.db.NewsCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 17.10.11
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class WfpNewsRssHandler implements HttpRequestHandler {

    private SyndFeed feed;
    private SyndEntry entry;
    private List<SyndEntry> entries;
    private SyndFeedOutput output;
    private PrintWriter writer;
    private SyndContent description;
    private List<EdsNews> newsList;

    @Autowired
    private NewsManager newsManager;

    @Autowired
    private NewsCategoryManager newsCategoryManager;

    @Override
    @Transactional
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String page = httpServletRequest.getParameter("pageSize");
        String index = httpServletRequest.getParameter("index");
        String forHome = httpServletRequest.getParameter("forhome");
        String organization = httpServletRequest.getParameter("organization");
        String category = httpServletRequest.getParameter("category");
        String url = httpServletRequest.getParameter("url");
        if (StrUtils.isEmpty(url)) {
            url = "http://test.com";
        }
        if (!url.contains("#") && !StrUtils.isEmpty(url)) {
            url = url + "#" + "news";
        }
        Integer pageSize = null;
        Integer organizationId = null;
        Integer categoryId = null;
        Integer indexNum = null;
        if (!StrUtils.isEmpty(page)) {
            pageSize = Integer.parseInt(page);
        }
        if (!StrUtils.isEmpty(index)) {
            indexNum = Integer.parseInt(index);
        }
        if (!StrUtils.isEmpty(organization)) {
            organizationId = Integer.parseInt(organization);
        }
        if (!StrUtils.isEmpty(category)) {
            categoryId = Integer.parseInt(category);
        }
        Integer companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());

        if (categoryId != null) {
            EdsNewsCategory newsCategory = newsCategoryManager.get(categoryId);
            if (newsCategory != null) {
                newsList = newsCategory.getNews();
            }
        } else if (organizationId != null) {
            newsList = newsManager.getNewsBySupplier(organizationId, companyId);
        }
        feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("RSS NEWS FEED");
        feed.setLink(url);
        feed.setDescription("RSS NEWS FEED");
        entries = new ArrayList<>();
        for (EdsNews news : newsList) {
            entry = new SyndEntryImpl();
            entry.setTitle(news.getSubject());
            entry.setLink(url + ";id=" + news.getObjectID());
            description = new SyndContentImpl();
            description.setValue(news.getShortDescription());
            entry.setDescription(description);
            entries.add(entry);
        }
        feed.setEntries(entries);
        writer = httpServletResponse.getWriter();
        output = new SyndFeedOutput();
        try {
            output.output(feed, writer);
        } catch (FeedException e) {
            e.printStackTrace();
        }
        writer.close();
    }
}
