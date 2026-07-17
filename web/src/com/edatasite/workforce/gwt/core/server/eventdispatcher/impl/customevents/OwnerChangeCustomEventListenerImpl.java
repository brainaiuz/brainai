package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.client.rpc.UpdateModeEnum;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 27.01.14
 * Time: 12:22
 */
@Transactional
public class OwnerChangeCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsUser> TYPE = new WfmType<>(EventTypes.ownerChangeCustomEventListener);
    public static final String EVENT_OWNER_ADD = "EVENT_OWNER_ADD";

    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrManager solrManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (event.getCustomStringField() != null && event.getCustomStringField().contains("::")) {
            ListingFilterParameter filterParameter = parseFilterParam(event.getCustomStringField());
            filterParameter.setUserID(event.getSourceID());
            filterParameter.setIDsOnly(true);
            if (filterParameter.getFacetFilter() != null) {
                filterParameter.getFacetFilter().setUserID(event.getSourceID());
            }
            List<Integer> accountIDs = new ArrayList<>();
            int start = 0;
            int limit = 200;
            int totalLength = 1;
            while (totalLength > start) {
                filterParameter.setStart(start);
                filterParameter.setLimit(limit);
                filterParameter.setCRM(true);
                CrmAccountList crmAccountList = crmServiceLocal.getCrmAccounts(filterParameter);
                totalLength = crmAccountList.getTotal();
                start = start + limit;
                crmAccountList.getList().forEach(item -> accountIDs.add(item.getObjectId()));
                changeOwners(accountIDs, ServerUtils.getStringAsList(event.getRelationIDs(), ","), event.getRelationType());
                accountIDs.clear();
            }
        } else {
            changeOwners(ServerUtils.getStringAsList(event.getEntityIDs(), ","), ServerUtils.getStringAsList(event.getRelationIDs(), ","), event.getRelationType());
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    /**
     * Change accounts owners
     *
     * @param accountIDs
     * @param ownerIDs
     * @param updateMode ADD,OVER_WRITE
     */
    private void changeOwners(List<Integer> accountIDs, List<Integer> ownerIDs, String updateMode) {
        List<EdsCrmAccount> crmAccounts = crmAccountManager.getCrmAccountsByIDs(accountIDs);
        List<EdsUser> owners = userManager.getByIDs(ownerIDs);
        if (UpdateModeEnum.ADD.name().equals(updateMode)) {
            crmAccounts.forEach(crmAccount -> {
                owners.stream().filter(owner -> !crmAccount.getOwners().contains(owner)).forEach(owner -> crmAccount.getOwners().add(owner));
                crmAccountManager.update(crmAccount);
            });
        } else if (UpdateModeEnum.OVER_WRITE.name().equals(updateMode)) {
            crmAccounts.forEach(crmAccount -> {
                crmAccount.getOwners().clear();
                crmAccount.getOwners().addAll(owners);
                crmAccountManager.update(crmAccount);
            });
        }

        try {
            solrManager.addCrmAccountWithContactToIndex(crmAccounts.toArray(new EdsCrmAccount[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
