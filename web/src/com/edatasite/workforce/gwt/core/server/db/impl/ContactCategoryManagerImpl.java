package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Oct 30, 2010
 * Time: 6:54:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("contactCategoryManager")
public class ContactCategoryManagerImpl extends BaseManager<EdsContactCategory> implements ContactCategoryManager, Constants {

    public ContactCategoryManagerImpl() {
        super(EdsContactCategory.class);
    }

    @Autowired
    private RolePermissionServiceLocal rolePermissionServiceLocal;

    @Override
    public List<EdsContactCategory> getList() {
        return find("select category from EdsContactCategory category where " + ServerUtils.checkForDeleted("category.deleted") + " and " + ServerUtils.checkForDeleted("category.doNotShow"));
    }

    @Override
    public List<EdsContactCategory> getList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select category from EdsContactCategory category ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("category.deleted"));
        sql.append(" and ").append(ServerUtils.checkForDeleted("category.doNotShow"));
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            sql.append(" and lower(category.name) like '%").append(filterParameter.getSearchKey()).append("%'");
        }
        return find(sql.toString());
    }

    @Override
    public List<EdsContactCategory> getContactCategoryList(ListingFilterParameter filterParameter) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct t.id ,t.*");
        sql.append(getContactCategoryBaseSql(filterParameter));
        sql.append(" order by ");
        if (StringUtils.isNotBlank(filterParameter.getSortField())) {
            if (ContactCategoryListItem.NAME.equals(filterParameter.getSortField())) {
                sql.append("t.name ");
            } else if (ContactCategoryListItem.DESCRIPTION.equals(filterParameter.getSortField())) {
                sql.append("t.description ");
            } else {
                sql.append("t.id ");
            }
        } else {
            sql.append("t.id ");
        }
        if (filterParameter.isAscending()) {
            sql.append("asc ");
        } else {
            sql.append("desc ");
        }

        sql.append("offset ").append(filterParameter.getStart()).append(" limit ").append(filterParameter.getLimit());

        return findNative(sql.toString(), EdsContactCategory.class);
    }

    @Override
    public Integer getContactCategoryCount(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(distinct t.id)");
        sql.append(getContactCategoryBaseSql(filterParameter));
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count.intValue();
    }

    private String getContactCategoryBaseSql(ListingFilterParameter filterParameter) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder("FROM (");
        //Parent categories
        sql.append("select cc.*,0 as clazz_ from ").append(getCompanyId()).append(".contactcategory cc \n");
        sql.append(" where (cc.deleted is null or cc.deleted is not true) \n");
        sql.append(" and (cc.doNotShow is null or cc.doNotShow is not true) \n");
        sql.append(" and (cc.parent_id is null and cc.type !=").append(EdsContactCategory.TEMP).append(")\n");

        //Shared categories shc
        sql.append(" union all \n");
        sql.append(" select shc.*,0 as clazz_ from ").append(getCompanyId()).append(".contactcategoryrbac ccrb \n");
        sql.append(" left join ").append(getCompanyId()).append(".contactcategory shc on ccrb.contactcategory_id = shc.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".myuser u on ccrb.userid = u.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".myuser owner on shc.owner_id = owner.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".trusteegroup gr on ccrb.groupid = gr.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".trustee mem on gr.id = mem.trusteeid \n");
        sql.append(" left join ").append(getPublic()).append(".trusteetype ttype on mem.trusteetype = ttype.id \n");
        sql.append(" where (shc.deleted is null or shc.deleted is not true)\n");
        sql.append(" and (shc.doNotShow is null or shc.doNotShow is not true)\n");
        sql.append(" and shc.foldertype !=").append(EdsContactCategory.PRIVATE_CONTACT_CATEGORY).append("\n");
        sql.append(" and (").append("\n");
        sql.append(" u.id = ").append(user.getObjectID()).append(" or owner.id = ").append(user.getObjectID());
        sql.append(" or (gr.id is not null and (u.id = ").append(user.getObjectID());
        sql.append(" or gr.id in (select distinct gg.id from ").append(getCompanyId()).append(".trusteegroup gg \n");
        sql.append(" left join ").append(getCompanyId()).append(".trustee memb on gr.id = mem.trusteeid \n");
        sql.append(" left join ").append(getPublic()).append(".trusteetype ttt on memb.trusteetype = ttt.id \n");
        sql.append(" where memb.id in (select distinct t.id from ").append(getCompanyId()).append(".trustee t \n");
        sql.append(" where t.trusteeid =").append(user.getObjectID());
        sql.append(" and ttt.id = ").append(EdsTrusteeType.USER);
        sql.append("))))) \n");


        //Shared Private Categories
        sql.append(" union all \n");
        sql.append(" select shc.*,0 as clazz_ from ").append(getCompanyId()).append(".contactcategoryrbac ccrb \n");
        sql.append(" left join ").append(getCompanyId()).append(".contactcategory shc on ccrb.contactcategory_id = shc.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".myuser u on ccrb.userid = u.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".myuser owner on shc.owner_id = owner.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".trusteegroup gr on ccrb.groupid = gr.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".trustee mem on gr.id = mem.trusteeid \n");
        sql.append(" left join ").append(getPublic()).append(".trusteetype ttype on mem.trusteetype = ttype.id \n");
        sql.append(" where (shc.deleted is null or shc.deleted is not true)\n");
        sql.append(" and (shc.doNotShow is null or shc.doNotShow is not true)\n");
        sql.append(" and shc.foldertype =").append(EdsContactCategory.PRIVATE_CONTACT_CATEGORY).append("\n");
        sql.append(" and (").append("\n");
        sql.append(" u.id = ").append(user.getObjectID()).append(" or owner.id = ").append(user.getObjectID());
        sql.append(" or (gr.id is not null and (u.id = ").append(user.getObjectID());
        sql.append(" or gr.id in (select distinct gg.id from ").append(getCompanyId()).append(".trusteegroup gg \n");
        sql.append(" left join ").append(getCompanyId()).append(".trustee memb on gr.id = mem.trusteeid \n");
        sql.append(" left join ").append(getPublic()).append(".trusteetype ttt on memb.trusteetype = ttt.id \n");
        sql.append(" where memb.id in (select distinct t.id from ").append(getCompanyId()).append(".trustee t \n");
        sql.append(" where t.trusteeid =").append(user.getObjectID());
        sql.append(" and ttt.id = ").append(EdsTrusteeType.USER);
        sql.append(")))))");
        sql.append(") t where 1=1");

        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            sql.append(" and (lower(t.name) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" or lower(t.description) like '").append(filterParameter.getSqlSearchKey()).append("')");
        }
        return sql.toString();
    }

    public List<EdsContactCategory> getDefaultCategoriesWithoutPrivateCategories() {
        return find("select category from EdsContactCategory category where " + ServerUtils.checkForDeleted("category.deleted") + " and " + ServerUtils.checkForDeleted("category.doNotShow") + " and category.categoryType <> " + EdsContactCategory.PRIVATE_CONTACT_CATEGORY + " and category.type = " + EdsContactCategory.SYSTEM_BUILTIN);
    }

    public List<EdsContactCategory> getFirstStepCategories() {
        return find("select category from EdsContactCategory category where " + ServerUtils.checkForDeleted("category.deleted") +
                " and " + ServerUtils.checkForDeleted("category.doNotShow") + " and category.parent is null");
    }

    public List<EdsContactCategory> getPrivateCategory(boolean isPrivileged) {
        return find("select category from EdsContactCategory category where " + ServerUtils.checkForDeleted("category.deleted") + " and " + ServerUtils.checkForDeleted("category.doNotShow") + " and category.categoryType = " + EdsContactCategory.PRIVATE_CONTACT_CATEGORY + " and category.type = " + EdsContactCategory.SYSTEM_BUILTIN);
    }

    public List<EdsContactCategory> getOwnCategories(Integer userID) {
        return find("select category from EdsContactCategory category where " + ServerUtils.checkForDeleted("category.deleted") + " and " + ServerUtils.checkForDeleted("category.doNotShow") + " and category.owner.objectID = ?", userID);
    }

    public List<EdsContactCategory> getSharedCategories(Integer userID, boolean isPrivileged) {
        Map params = new HashMap();
        params.put("categoryType", EdsContactCategory.PRIVATE_CONTACT_CATEGORY);
        if (isPrivileged) {
            return findByNamedParams("select distinct ccr.contactCategory from EdsContactCategoryRbac ccr where " + ServerUtils.checkForDeleted("ccr.contactCategory.doNotShow") + " and " + ServerUtils.checkForDeleted("ccr.contactCategory.deleted") + " and ccr.contactCategory.categoryType <> :categoryType ", params);
        }
        params.put("userID", userID);
        params.put("trusteeType", EdsTrusteeType.USER);
        //return findByNamedParams("select distinct ccr.contactCategory from EdsContactCategoryRbac ccr where " + ServerUtils.checkForDeleted("ccr.contactCategory.doNotShow") + " and " + ServerUtils.checkForDeleted("ccr.contactCategory.deleted") + " and ccr.contactCategory.categoryType <> :categoryType " + "and (ccr.user.objectID = :userID or ccr.contactCategory.owner.objectID = :userID or (ccr.group.objectID is not null and (ccr.user.objectID=:userID or ccr.group.objectID in (select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userID and t.type.objectID=:trusteeType)))) ) ", params);
        return new ArrayList<>();//TODO schema update dan keyin qaytamiz
    }

    public List<EdsContactCategory> getAllSharedCategories(Integer userID) {
        Map params = new HashMap();
        params.put("categoryType", EdsContactCategory.PRIVATE_CONTACT_CATEGORY);
        params.put("exceptTempCategoryType", EdsContactCategory.TEMP);
        params.put("userID", userID);
        params.put("trusteeType", EdsTrusteeType.USER);
        return findByNamedParams("select distinct ccr.contactCategory from EdsContactCategoryRbac ccr where " +
                ServerUtils.checkForDeleted("ccr.contactCategory.doNotShow") + " and " +
                ServerUtils.checkForDeleted("ccr.contactCategory.deleted") + " and (ccr.contactCategory.categoryType <> :categoryType or ccr.contactCategory.categoryType = :categoryType) " +
                "and ccr.contactCategory.categoryType <> :exceptTempCategoryType and (ccr.user.objectID = :userID or ccr.contactCategory.owner.objectID = :userID or (ccr.group.objectID is not null " +
                "and (ccr.user.objectID=:userID or ccr.group.objectID in (select distinct gg.objectID from EdsGroup gg join gg.members memb " +
                "where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userID and t.type.objectID=:trusteeType)))) ) ", params);
    }

    public List<EdsContactCategory> getSharedPrivateCategories(Integer userID, boolean isPrivileged) {
        Map params = new HashMap();
        params.put("categoryType", EdsContactCategory.PRIVATE_CONTACT_CATEGORY);
        if (isPrivileged) {
            return findByNamedParams("select distinct ccr.contactCategory from EdsContactCategoryRbac ccr where " + ServerUtils.checkForDeleted("ccr.contactCategory.doNotShow") + " and " + ServerUtils.checkForDeleted("ccr.contactCategory.deleted") + " and ccr.contactCategory.categoryType = :categoryType", params);
        }
        params.put("userID", userID);
        params.put("trusteeType", EdsTrusteeType.USER);
//        return findByNamedParams("select distinct ccr.contactCategory from EdsContactCategoryRbac ccr where " +
//                ServerUtils.checkForDeleted("ccr.contactCategory.doNotShow") + " and " +
//                ServerUtils.checkForDeleted("ccr.contactCategory.deleted") + " and ccr.contactCategory.categoryType = :categoryType " +
//                "and (ccr.user.objectID = :userID or ccr.contactCategory.owner.objectID = :userID or (ccr.group.objectID is not null " +
//                "and (ccr.user.objectID=:userID or ccr.group.objectID in (select distinct gg.objectID from EdsGroup gg join gg.members memb " +
//                "where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userID and t.type.objectID=:trusteeType)))) ) ", params);
        return new ArrayList<>();
    }

    @Override
    public EdsContactCategory getDefaultCategoryByContactType(Integer contactType) {
        return (EdsContactCategory) findSingle(" select distinct cc from EdsContactCategory cc where cc.categoryType =" + contactType + " and cc.type = " + EdsContactCategory.SYSTEM_BUILTIN + " order by cc.objectID");
    }

    @Override
    public List<EdsContactCategory> getDefaultCategoriesByContactType(Integer... contactTypes) {
        return (List<EdsContactCategory>) find(" select distinct cc from EdsContactCategory cc where cc.categoryType in (" + ServerUtils.getAsCommoDelimited(Arrays.asList(contactTypes), "1") + ") and cc.type = " + EdsContactCategory.SYSTEM_BUILTIN + " order by cc.objectID");
    }

    public List<EdsContactCategory> getAllCategories(Integer userID) {
        List<EdsContactCategory> firstStepCategories = getFirstStepCategories();
        List<EdsContactCategory> sharedCategories = getAllSharedCategories(userID);
        for (EdsContactCategory c : sharedCategories) {
            if (!firstStepCategories.contains(c) && c.getParent() == null) {
                firstStepCategories.add(c);
            }
        }
        return firstStepCategories;
    }

    public List<Integer> getAllCategoriesForSolr(Integer userID, boolean isPrivileged) {
        List<EdsContactCategory> defaultCategories = getDefaultCategoriesWithoutPrivateCategories();
        List<EdsContactCategory> sharedCategories = getSharedCategories(userID, false);
        List<Integer> sharedCategoryIDs = new ArrayList<>();
        for (EdsContactCategory c : defaultCategories) {
            if (!sharedCategoryIDs.contains(c.getObjectID())) {
                sharedCategoryIDs.add(c.getObjectID());
            }
        }
        for (EdsContactCategory contactCategory : sharedCategories) {
            if (!sharedCategoryIDs.contains(contactCategory.getObjectID())) {
                sharedCategoryIDs.add(contactCategory.getObjectID());
            }
        }
        return sharedCategoryIDs;
    }

    public List<Integer> getAllPrivateCategoriesForSolr(Integer userID, boolean isPrivileged) {
        List<EdsContactCategory> privateCategories = getPrivateCategory(false);
        List<EdsContactCategory> sharedCategories = getSharedPrivateCategories(userID, isPrivileged);
        List<Integer> sharedCategoryIDs = new ArrayList<>();
        for (EdsContactCategory c : privateCategories) {
            if (!sharedCategoryIDs.contains(c.getObjectID())) {
                sharedCategoryIDs.add(c.getObjectID());
            }
        }
        for (EdsContactCategory contactCategory : sharedCategories) {
            if (!sharedCategoryIDs.contains(contactCategory.getObjectID())) {
                sharedCategoryIDs.add(contactCategory.getObjectID());
            }
        }
        return sharedCategoryIDs;
    }

    /**
     * @param isPrivileged
     * @return Set
     */
    public Set<Integer> getSharedCategoryIDsForUser(boolean isPrivileged) {
        Set<Integer> ids = new HashSet<>();
        EdsUser user = getUser();
        List<Integer> notPrivateIds = getAllCategoriesForSolr(user.getObjectID(), isPrivileged);
        List<Integer> privateIDs = getAllPrivateCategoriesForSolr(user.getObjectID(), isPrivileged);
        ids.addAll(notPrivateIds);
        ids.addAll(privateIDs);
        return ids;
    }

    @Override
    public EdsContactCategory getLeadCategory() {
        return (EdsContactCategory) findSingle("select category from EdsContactCategory category where category.categoryType = " + EdsContactCategory.LEAD_CONTACT_CATEGORY);
    }

    @Override
    public List<Integer> getContactCategoryIDs(Integer contactID) {
        return (List<Integer>) findNative("select cc.categories_id from " + getCompanyId() + ".crmcontact_contactcategory cc where cc.crmcontact_id = " + contactID + " ");
    }

    /**
     * @param prefix       solr operators : or something else which will be the start of the query if ids.size > 0
     * @param user         loggedinUser.
     * @param suffix       solr operators : or something else which will be the start of the query if ids.size > 0
     * @param onlyTheseIDs the ids which are selected In Filter...
     * @return String
     */
    public String getCategoryIDsForUserForSOLR(String prefix, EdsUser user, String suffix, List<Integer> onlyTheseIDs) {
        prefix = prefix == null ? "" : prefix;
        suffix = suffix == null ? "" : suffix;
        boolean isPrivileged = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST, user);

        List<EdsContactCategory> defaultCategories = getDefaultCategoriesWithoutPrivateCategories();
        List<EdsContactCategory> sharedCategories = getSharedCategories(user.getObjectID(), isPrivileged);
        List<Integer> defaultCategoryIDs = new ArrayList<>();
        List<Integer> sharedCategoryIDs = new ArrayList<>();
        for (EdsContactCategory c : defaultCategories) {
            if (!defaultCategoryIDs.contains(c.getObjectID())) {
                defaultCategoryIDs.add(c.getObjectID());
            }
        }
        for (EdsContactCategory contactCategory : sharedCategories) {
            if (!sharedCategoryIDs.contains(contactCategory.getObjectID())) {
                sharedCategoryIDs.add(contactCategory.getObjectID());
            }
        }
        List<Integer> privateIDs = getAllPrivateCategoriesForSolr(user.getObjectID(), isPrivileged);
        if (onlyTheseIDs != null && onlyTheseIDs.size() > 0) {
            defaultCategoryIDs.retainAll(onlyTheseIDs);
            sharedCategoryIDs.retainAll(onlyTheseIDs);
        }
        if (defaultCategoryIDs.size() > 0 || sharedCategoryIDs.size() > 0) {
            String result = prefix;
            if (defaultCategoryIDs.size() > 0) {
                result += " ( " + SolrContactRepresenter.FIELD_CATEGORY_ID + ":(" + ServerUtils.getAsCommoDelimited(defaultCategoryIDs, "0", " ") + ") ";
                if (!isPrivileged) {
                    result += " AND (" + SolrContactRepresenter.FIELD_OWNER_ID + ":" + user.getObjectID() + " OR " + SolrContactRepresenter.FIELD_CRM_ACCOUNT_OWNER_ID + ":" + user.getObjectID() + " )";
                }
                result += ") ";
                if (sharedCategoryIDs.size() > 0) {
                    result += " OR ";
                }
            }
            if (sharedCategoryIDs.size() > 0) {
                result += " ( " + SolrContactRepresenter.FIELD_CATEGORY_ID + ":(" + ServerUtils.getAsCommoDelimited(sharedCategoryIDs, "0", " ") + ")) ";
            }
            if (privateIDs != null && privateIDs.size() > 0) {
                result += " OR (" + SolrContactRepresenter.FIELD_OWNER_ID + ":" + user.getObjectID() + " AND ( " + SolrContactRepresenter.FIELD_CATEGORY_ID + ":(" + ServerUtils.getAsCommoDelimited(privateIDs, "0", " ") + ") ) " + " ) ";
            }
            result += suffix;
            return result;
        }
        return "";
    }


    @Override
    public ArrayList<ContactCategoryListItem> getContactCategories() {
        EdsUser user = getUser();
        boolean isPrivileged = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST);
        List<EdsContactCategory> list = getAllCategories(user.getObjectID());
        List<EdsContactCategory> list2 = getSharedCategories(user.getObjectID(), isPrivileged);
        if (list2 == null) {
            list2 = getSharedPrivateCategories(user.getObjectID(), isPrivileged);
        } else {
            list2.addAll(getSharedPrivateCategories(user.getObjectID(), isPrivileged));
        }
        return EdsContactCategory.getRPCsWithChildren(list, list2);
    }

}
