package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.NewsSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Hayot
 * Date: Aug 11, 2010
 * Time: 2:28:55 PM
 */
@Transactional
public class NewsCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsNews> TYPE = new WfmType<>(EventTypes.newsCustomEventListener);
    public static String EVENT_NEWS_ADD_TO_SOLR = "NEWS_ADD_TO_SOLR";
    public static String EVENT_NEWS_NOTIFICATION = "NEWS_NOTIFICATION";
    public static String EVENT_NEWS_DELETE_FROM_SOLR = "NEWS_DELETE_FROM_SOLR";
    public static String EVENT_NEWS_DELETE_ALL_FROM_SOLR_BY_IDS = "NEWS_DELETE_ALL_FROM_SOLR_BY_IDS";
    public static String EVENT_NEWS_UPDATE_NOTIFICATION = "EVENT_NEWS_UPDATE_NOTIFICATION";

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private NewsSolrComponent newsSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_NEWS_ADD_TO_SOLR.equals(event.getEventType())) {
            onAdd(event);
        } else if (EVENT_NEWS_DELETE_FROM_SOLR.equals(event.getEventType())) {
            onDelete(event);
        } else if (EVENT_NEWS_DELETE_ALL_FROM_SOLR_BY_IDS.equals(event.getEventType())) {
            deleteByIds(event);
        } else if (EVENT_NEWS_NOTIFICATION.equals(event.getEventType())) {
            sendNotification(event);
        } else if (EVENT_NEWS_UPDATE_NOTIFICATION.equals(event.getEventType())) {
            sendUpdateNotification(event);
        }
    }

    private void sendNotification(EdsBusinessEvent event) {
        EdsNews news = newsManager.get(event.getEntityID());
        EdsUser edsUser = userManager.get(event.getSourceID());
        List<Integer> userList = employeeManager.getLocationEmployees(news.getLocation().getObjectID());
        if (userList != null && !userList.isEmpty()) {
            for (Integer employeeID : userList) {
                if (!employeeID.equals(edsUser.getObjectID())) {
                    messageManager.sendNewsNotificationByLocation(news, edsUser.getObjectID(), employeeID ,"News create notification");

                }
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void sendUpdateNotification(EdsBusinessEvent event) {
        EdsNews news = newsManager.get(event.getEntityID());
        EdsUser edsUser = userManager.get(event.getSourceID());
        List<Integer> userList = employeeManager.getLocationEmployees(news.getLocation().getObjectID());
        if (userList != null && !userList.isEmpty()) {
            for (Integer employeeID : userList) {
                if (!employeeID.equals(edsUser.getObjectID())) {
                    messageManager.sendNewsNotificationByLocation(news, edsUser.getObjectID(), employeeID ,"News update notification");

                }
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void deleteByIds(EdsBusinessEvent event) {
        try {
            List<Integer> ids = new ArrayList<>();
            if (event.getCustomStringField() != null && !"".equals(event.getCustomStringField())) {
                for (String id_ : event.getCustomStringField().split(",")) {
                    try {
                        ids.add(Integer.parseInt(id_));
                    } catch (NumberFormatException e) {

                    }
                }
            }
            if (ids.size() > 0) {
                solrManager.removeCompanyNewsByIds(ids.toArray(new Integer[]{}));
            }
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsNews news = newsManager.get(event.getEntityID());
        try {
            newsSolrComponent.index(news);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onDelete(EdsBusinessEvent event) {
        try {
            solrManager.removeCompanyNewsByIds(event.getEntityID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

}
