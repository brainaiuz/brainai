package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmEntityMailList;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CrmEntityMailListManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 27.01.14
 * Time: 12:22
 */
@Transactional
public class CrmMailingListCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsMailList> TYPE = new WfmType<>(EventTypes.mailingListCustomEventListener);
    public static final String EVENT_MAIL_LIST_ADD = "MAIL_LIST_ADD";

    @Autowired
    private MailListManager mailListManager;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmEntityMailListManager crmEntityMailListManager;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_MAIL_LIST_ADD.equals(event.getEventType())) {
            createMailList(event);
        }
    }

    private void createMailList(EdsBusinessEvent event) {
        EdsMailList mailList = mailListManager.get(event.getEntityID());
        if (event.getCustomStringField().contains("::")) {
            ListingFilterParameter filterParameter = parseFilterParam(event.getCustomStringField());
            filterParameter.setUserID(event.getSourceID());
            if (filterParameter.getFacetFilter() != null) {
                filterParameter.getFacetFilter().setUserID(event.getSourceID());
            }
            List<Integer> iDs = new ArrayList<>();
                int start = 0;
                int limit = 200;
                int totalLength = 1;
                while (totalLength > start) {
                    ListLoadConfig config1 = new ListLoadConfig();
                    config1.setStart(start);
                    config1.setLimit(limit);
                    filterParameter.setAllByFilter(false);
                    //bu yerda leadga tekwirilgan
                    ListData list = filterParameter.isForCSVonly() ? crmServiceLocal.getLeadList(filterParameter, config1).getListData() : getContactList(filterParameter, config1).getListData();
                    totalLength = list.getTotal();
                    start = start + limit;
                    for (ContactListItem item : (ContactListItem[]) list.getData()) {
                        if (item != null) {
                            iDs.add(item.getObjectId());
                        }
                    }
                    createEntityMailList(mailList, iDs);
                    iDs.clear();
                }
        } else {
            createEntityMailList(mailList, parseIDs(event.getCustomStringField()));
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void createEntityMailList(EdsMailList mailList, List<Integer> iDs) {
        if (iDs.size() > 0) {
            EdsCrmEntityMailList crmEntityMailList;
            for (Integer id : iDs) {
                crmEntityMailList = new EdsCrmEntityMailList();
                crmEntityMailList.setMailList(mailList);
                crmEntityMailList.setEntity(crmContactManager.get(id));
                crmEntityMailListManager.create(crmEntityMailList);
                crmEntityMailListManager.flushAndClear();
            }
        }
    }

    private List<Integer> parseIDs(String customStringField) {
        List<Integer> iDs = new ArrayList<>();
        customStringField.replace("\\(", "");
        customStringField.replace("\\)", "");
        while (customStringField.length() > 0) {
            if (customStringField.contains(",")) {
                iDs.add(Integer.parseInt(customStringField.substring(0, customStringField.indexOf(","))));
                customStringField = customStringField.substring(customStringField.indexOf(",") + 1);
            } else {
                iDs.add(Integer.parseInt(customStringField));
                customStringField = "";
            }
        }
        return iDs;
    }

    private ListingFilterParameter parseFilterParam(String customStringField) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        HashMap<String, String> requestParams = new HashMap<>();
        while (customStringField.length() > 0) {
            if (!"null".equals(customStringField.substring(customStringField.indexOf(":") + 2, customStringField.indexOf("|")))) {
                requestParams.put(customStringField.substring(0, customStringField.indexOf(":")), customStringField.substring(customStringField.indexOf(":") + 2, customStringField.indexOf("|")));
            }
            customStringField = customStringField.indexOf("|") + 2 != customStringField.length() ? customStringField.substring(customStringField.indexOf("|") + 2) : "";
        }
        filterParameter.setRequestParams(requestParams);
        filterParameter.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(filterParameter.getFacetFilterJson()));
        filterParameter.setListPanelTool(WfmJsonUtils.jsonConvertToListPanelToolRpc(filterParameter.getListPanelToolJson()));
        return filterParameter;
    }

    private ContactList getContactList(ListingFilterParameter crmEntityFilterParametrs, ListLoadConfig config) {
        ContactService contactService = (ContactService) ApplicationContextProvider.applicationContext.getBean("contactService");
        if (contactService != null) {
            return contactService.getContactList(crmEntityFilterParametrs, config);
        }
        return null;
    }
}
