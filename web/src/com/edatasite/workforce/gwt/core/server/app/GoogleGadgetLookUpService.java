package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Extreme
 * Date: 10/23/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("gadgetLookUpService")
public class GoogleGadgetLookUpService implements CrmConstants {
    @Autowired
    AllInOneService allInOneService;
    @Autowired
    TaskService taskService;
    @Autowired
    GoogleGadgetService googleGadgetService;

    private static final String TASK = "TASK";
    private static final String CASE = "case";
    private static final String LEAD = "lead";
    private static final String CONTACT = "contact";
    private static final String ACCOUNT = "account";
    private static final String COMPAIGN = "compaign";
    private static final String OPPORTUNITY = "opportunity";

    private static final Integer LIMIT = 30;

    public ArrayList<EmailLinkItem> getSelectExistingItems(ArrayList<RelationItem> relations, Integer companyId) {
        ArrayList<EmailLinkItem> items = new ArrayList<>();
        String type = "";
        String name = "";
        Integer id = null;
        for (RelationItem relation : relations) {

            if (relation.getFromType().equals(RelationItem.TYPE_EMAIL_TRACKER)) {
                type = relation.getToType();
                id = relation.getToID();
                name = relation.getToName();
            } else {
                type = relation.getFromType();
                id = relation.getFromID();
                name = relation.getFromName();
            }

            EmailLinkItem item = new EmailLinkItem();
            item.setLinkTypes(getType(type));
            item.setExist(true);

            SelectItem selectItem = new SelectItem(id, name, type, true);
            item.setLinkItems(selectItem);

            if (type.equals(RelationItem.TYPE_TASK)) {
                SelectItem project = taskService.getProjectByTask(id);
                item.setLinkProjects(project);
            }


            items.add(item);
        }
        return items;
    }

    public SelectItem[] getItems(String type, Integer companyId, Integer projectId, String searchKey, Integer accountId) {
        switch (type) {
            case TASK -> {
                if (projectId != null) {
                    return getTasks(companyId, projectId, searchKey);
                } else {
                    return getProjectList(companyId, searchKey);
                }
            }
            case CASE -> {
                return getCases(companyId, searchKey);
            }
            case LEAD -> {
                return getLead(companyId, searchKey);
            }
            case CONTACT -> {
                if (accountId != null) {
                    return getContactByAccount(companyId, searchKey, accountId);
                } else {
                    return getContact(companyId, searchKey);
                }
            }
            case ACCOUNT -> {
                return getAccount(companyId, searchKey);
            }
            case COMPAIGN -> {
                return getCompaigns(companyId, searchKey);
            }
            case OPPORTUNITY -> {
                return getOpprotunities(companyId, searchKey);
            }
            default -> {
                return null;
            }
        }
    }


    public SelectItem[] getType(String type) {
        HashMap<String, Boolean> permissions = googleGadgetService.getPermissionsViewForCurrentUser();
        ArrayList<SelectItem> typesList = new ArrayList<>();

        if (permissions.get(PermissionConstants.CRM_TASKS_LIST)) {
            typesList.add(new SelectItem(0, RelationItem.TYPE_TASK, "Task"));
        }
        if (permissions.get(PermissionConstants.CRM_CASES_LIST)) {
            typesList.add(new SelectItem(1, RelationItem.TYPE_CASE, "Case"));
        }
        if (permissions.get(PermissionConstants.CRM_LEADS_LIST)) {
            typesList.add(new SelectItem(2, RelationItem.TYPE_LEAD, "Lead"));
        }
        if (permissions.get(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            typesList.add(new SelectItem(3, RelationItem.TYPE_OPPORTUNITY, "Opportunity"));
        }
        if (permissions.get(PermissionConstants.CRM_CONTACTS_LIST)) {
            typesList.add(new SelectItem(4, RelationItem.TYPE_CONTACT, "Contact"));
        }
        if (permissions.get(PermissionConstants.CRM_ACCOUNTS_LIST)) {
            typesList.add(new SelectItem(5, RelationItem.TYPE_CRM_ACCOUNT, "Account"));
        }


        for (SelectItem typeItem : typesList) {
            if (typeItem.getName().equals(type)) {
                typeItem.setSelected(true);
            }
        }

        return typesList.toArray(new SelectItem[0]);
    }


    private SelectItem[] getCases(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_CASE_ID, null);
    }


    private SelectItem[] getTasks(Integer companyId, Integer projectId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setProjectId(projectId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setPM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, PM_TASK_ID, null);
    }

    private SelectItem[] getCompaigns(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_CAMPAIGN_ID, null);
    }

    private SelectItem[] getOpprotunities(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_OPPORTUNITY_ID, null);
    }

    private SelectItem[] getProjectList(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setPM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, PM_PROJECT_ID, null);
    }

    private SelectItem[] getLead(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_LEAD_ID, null);
    }

    private SelectItem[] getAccount(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_ACCOUNT_ID, null);
    }

    private SelectItem[] getContact(Integer companyId, String searchKey) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_CONTACT_ID, null);
    }

    private SelectItem[] getContactByAccount(Integer companyId, String searchKey, Integer accountId) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);
        filterParametrs.setDoNotSearch(false);
        filterParametrs.setAccountID(accountId);
        filterParametrs.setSearchKey(searchKey);

        return allInOneService.getLookUpItems(filterParametrs, CRM_CONTACT_ID, null);
    }

    private SelectItem[] selectDefault(SelectItem[] items, Integer id) {
        for (SelectItem item : items) {
            if (item.getId().equals(id)) {
                item.setSelected(true);
            }
        }
        return items;
    }

    public static class EmailLinkItem {

        public SelectItem[] linkTypes;
        public SelectItem linkProjects;
        public SelectItem linkItems;
        public Boolean isExist = false;

        public Boolean getExist() {
            return isExist;
        }

        public void setExist(Boolean exist) {
            isExist = exist;
        }

        public SelectItem[] getLinkTypes() {
            return linkTypes;
        }

        public void setLinkTypes(SelectItem[] linkTypes) {
            this.linkTypes = linkTypes;
        }

        public SelectItem getLinkProjects() {
            return linkProjects;
        }

        public void setLinkProjects(SelectItem linkProjects) {
            this.linkProjects = linkProjects;
        }

        public SelectItem getLinkItems() {
            return linkItems;
        }

        public void setLinkItems(SelectItem linkItems) {
            this.linkItems = linkItems;
        }
    }

}
