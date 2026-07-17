package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsItemMultiPrice;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 27.06.12
 * Time: 17:09:22 PM 0
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class PurchaseOrderEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsPurchaseOrder> TYPE = new WfmType<>(EventTypes.purchaseOrderEventListener);

    public static String EVENT_PURCHASE_ORDER_CLIENT_APPROVE = "EVENT_PURCHASE_ORDER_CLIENT_APPROVE";
    public static String EVENT_PURCHASE_ORDER_SEND_TO_CLIENT = "EVENT_PURCHASE_ORDER_SEND_TO_CLIENT";
    public static String EVENT_PURCHASE_ORDER_RECEIVED = "EVENT_PURCHASE_ORDER_RECEIVED";
    public static String EVENT_PURCHASE_ORDER_PARTIAL_RECEIVED = "EVENT_PURCHASE_ORDER_PARTIAL_RECEIVED";
    public static String EVENT_PURCHASE_ORDER_SUBMITTED_TO_MANAGER = "EVENT_PURCHASE_ORDER_SUBMITTED_TO_MANAGER";
    public static String EVENT_STATUS_CLOSED_PURCHASE_ORDER = "EVENT_STATUS_CLOSED_PURCHASE_ORDER";


    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    QuoteManager quoteManager;
    @Autowired
    FinancialSettingsManager financialSettingsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_PURCHASE_ORDER_SEND_TO_CLIENT.equalsIgnoreCase(event.getEventType())) {
            onSendToClient(event);
        } else if (EVENT_PURCHASE_ORDER_CLIENT_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onClientApproveEvent(event);
        } else if (EVENT_PURCHASE_ORDER_RECEIVED.equalsIgnoreCase(event.getEventType())) {
            onReceivedEvent(event);
        } else if (EVENT_PURCHASE_ORDER_PARTIAL_RECEIVED.equalsIgnoreCase(event.getEventType())) {
            onPartialReceivedEvent(event);
        } else if (EVENT_PURCHASE_ORDER_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        } else if (EVENT_STATUS_CLOSED_PURCHASE_ORDER.equalsIgnoreCase(event.getEventType())) {
            onClosedEvent(event);
        }

    }


    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsPurchaseOrder order = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderAddUpdate(order, creator, event.getTime());
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

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsPurchaseOrder order = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderEditUpdate(order, creator, event.getTime());
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

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsPurchaseOrder order = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderDeleteUpdate(order, event.getCustomStringField(), creator, event.getTime());
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

    public void onSendToClient(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderSendToClient(purchaseOrder, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onClientApproveEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderClientApproveUpdate(purchaseOrder, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onReceivedEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getUpdateCostPriceOnPurhcase() && purchaseOrder != null) {
            List<EdsQuoteItem> quoteItems = purchaseOrder.getQuoteItems();
            if (quoteItems != null) {
                for (EdsQuoteItem quoteItem : quoteItems) {
                    if (quoteItem.getItem().getMultiPrices() != null && !quoteItem.getItem().getMultiPrices().isEmpty()) {
                        for (EdsItemMultiPrice mp : quoteItem.getItem().getMultiPrices()) {
                            if (mp.getCurrency() != null && mp.getCurrency().equals(purchaseOrder.getCurrency())) {
                                if (EdsItemMultiPrice.PAYABLE.equals(mp.getType())) {
                                    mp.setSellingPrice(quoteItem.getUnitPrice());
                                }
                            }
                        }
                    } else {
                        quoteItem.getItem().setUnitPrice(quoteItem.getUnitPrice().divide(purchaseOrder.getExchangeRate(), ServerUtils.getSystemPriceScale(), RoundingMode.HALF_EVEN));
                    }
                    try {
                        productsServicesSolrComponent.index(quoteItem.getItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderReceivedUpdate(purchaseOrder, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onPartialReceivedEvent(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getUpdateCostPriceOnPurhcase()) {
            if (purchaseOrder != null) {
                List<EdsQuoteItem> quoteItems = purchaseOrder.getQuoteItems();
                if (quoteItems != null) {
                    for (EdsQuoteItem quoteItem : quoteItems) {
                        if (quoteItem.getReceive() != null && BigDecimal.ZERO.compareTo(quoteItem.getReceive()) > 0) {
                            if (quoteItem.getItem().getMultiPrices() != null && !quoteItem.getItem().getMultiPrices().isEmpty()) {
                                for (EdsItemMultiPrice mp : quoteItem.getItem().getMultiPrices()) {
                                    if (mp.getCurrency() != null && mp.getCurrency().equals(purchaseOrder.getCurrency())) {
                                        if (EdsItemMultiPrice.PAYABLE.equals(mp.getType())) {
                                            mp.setSellingPrice(quoteItem.getUnitPrice());
                                        }
                                    }
                                }
                            } else {
                                quoteItem.getItem().setUnitPrice(quoteItem.getUnitPrice().divide(purchaseOrder.getExchangeRate(), AccountingUtils.getUnitPriceScale(), BigDecimal.ROUND_HALF_UP));
                            }
                            try {
                                productsServicesSolrComponent.index(quoteItem.getItem());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderPartialReceivedUpdate(purchaseOrder, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    public void onSubmittedToManager(EdsBusinessEvent event) {

        EdsUser user = userManager.get(event.getSourceID());
        EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderSubmittedToManager(purchaseOrder, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onClosedEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsPurchaseOrder quote = (EdsPurchaseOrder) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerPurchaseOrderClosed(quote, creator, event.getTime());
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
