package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: Mar 15, 2011
 * Time: 8:09:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class SalesQuoteEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsSaleQuote> TYPE = new WfmType<>(EventTypes.salesQuoteEventListener);

    public static String EVENT_SALES_QUOTE_CONVERT_TO_SALE_ORDER = "EVENT_SALES_QUOTE_CONVERT_TO_SALE_ORDER";
    public static String EVENT_SALES_QUOTE_CLIENT_APPROVE = "EVENT_SALES_QUOTE_CLIENT_APPROVE";
    public static String EVENT_SALES_QUOTE_REJECT = "EVENT_SALES_QUOTE_REJECT";
    public static String EVENT_SALES_QUOTE_SEND_TO_CLIENT = "EVENT_SALES_QUOTE_SEND_TO_CLIENT";
    public static String EVENT_SALES_QUOTE_MANAGER_APPROVE = "EVENT_SALES_QUOTE_MANAGER_APPROVE";
    public static String EVENT_SALES_QUOTE_MANAGER_REJECT = "EVENT_SALES_QUOTE_MANAGER_REJECT";
    public static String EVENT_SALES_QUOTE_SUBMITTED_TO_MANAGER = "EVENT_SALES_QUOTE_SUBMITTED_TO_MANAGER";
    public static String EVENT_STATUS_CLOSED_SALE_QUOTE = "EVENT_STATUS_CLOSED_SALE_QUOTE";



    @Autowired
    GenericSettingsManager genericSettingsManager;
    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    QuoteManager quoteManager;
    @Autowired
    NotificationMsgManager notificationMsgManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_SALES_QUOTE_CLIENT_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onClientApproveEvent(event);
        } else if (EVENT_SALES_QUOTE_REJECT.equalsIgnoreCase(event.getEventType())) {
            onRejectEvent(event);
        } else if (EVENT_SALES_QUOTE_CONVERT_TO_SALE_ORDER.equalsIgnoreCase(event.getEventType())) {
            onConvertToSaleOrder(event);
        } else if (EVENT_SALES_QUOTE_SEND_TO_CLIENT.equalsIgnoreCase(event.getEventType())) {
            onSendToClient(event);
        } else if (EVENT_SALES_QUOTE_MANAGER_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onManagerApproveEvent(event);
        } else if (EVENT_SALES_QUOTE_MANAGER_REJECT.equalsIgnoreCase(event.getEventType())) {
            onManagerRejectEvent(event);
        } else if (EVENT_SALES_QUOTE_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        } else if (EVENT_STATUS_CLOSED_SALE_QUOTE.equalsIgnoreCase(event.getEventType())) {
            onClosedEvent(event);
        }
    }


    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(quote.getClient().getObjectID());
        }
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteAddUpdate(quote, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

        if (quote.getOverallStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(quote.getOverallStatus().getCode())) {
            notificationMsgManager.updateClickedNotificationEvent(quote.getObjectID(), NotificationTypeEnum.SalesQuote, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createSalesQuoteNotification(quote, creator);
        }

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(quote.getClient().getObjectID());
        }
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteEditUpdate(quote, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        if (quote.getOverallStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(quote.getOverallStatus().getCode())) {
            notificationMsgManager.updateClickedNotificationEvent(quote.getObjectID(), NotificationTypeEnum.SalesQuote, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createSalesQuoteNotification(quote, creator);
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
        }
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteDeleteUpdate(saleQuote, event.getCustomStringField(), creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onClientApproveEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
        }

        boolean isEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.QUOTES_APPROVEDBYCLIENT_SET_PROJECT_CUSTOMER);
        if (isEnabled) {
            List<EdsProject> projects = new ArrayList<>();
            if (saleQuote.getQuoteItems() != null) {
                saleQuote.getQuoteItems().forEach(q -> {
                    EdsProject project = q.getProject();
                    if (project != null) {
                        project.setClient(saleQuote.getClient());
                        projectManager.update(project);
                        projects.add(project);
                    }
                });
            }

            if (!projects.isEmpty()) {
                try {
                    projectSolrComponent.indexes(projects);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteClientApproveUpdate(saleQuote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onRejectEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
        }
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteRejectUpdate(saleQuote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }


    public void onConvertToSaleOrder(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
        }
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteConvertToSaleOrderUpdate(saleQuote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onSendToClient(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
        }
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteSendToClient(saleQuote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onSubmittedToManager(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            quoteManager.calculateCustomerQuoteBalance(saleQuote.getClient().getObjectID());
        }
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteSubmittedToManager(saleQuote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManagerApproveEvent(EdsBusinessEvent event) {

        EdsUser manager = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = quoteManager.getSaleQuote(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteManagerApproveUpdate(saleQuote, manager, saleQuote.getCreator(), event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }


    private void onManagerRejectEvent(EdsBusinessEvent event) {

        EdsUser manager = userManager.get(event.getSourceID());
        EdsSaleQuote saleQuote = quoteManager.getSaleQuote(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteManagerRejectUpdate(saleQuote, manager, saleQuote.getCreator(), event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onClosedEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleQuoteClosed(quote, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }


}
