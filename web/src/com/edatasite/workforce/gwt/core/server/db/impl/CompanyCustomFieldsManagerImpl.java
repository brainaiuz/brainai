package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CUSTOM_VIEW;

/**
 * User: Ilxom Lutfullaev
 * Date: Mar 24, 2010
 * Time: 5:49:00 PM
 */

@Repository("companyCFSettingsManager")
public class CompanyCustomFieldsManagerImpl extends BaseManager<EdsCompanyCustomFieldsSettings> implements CompanyCustomFieldsManager {
    public CompanyCustomFieldsManagerImpl() {
        super(EdsCompanyCustomFieldsSettings.class);
    }

    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFields(final String entityName, final String columncode) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ?  and ccfs.entityCategoryName is null " +
                ((columncode != null) ? "and ccfs.columnCode = '" + columncode + "'" : " ") + " order by ccfs.objectID asc", entityName);
    }

    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByRelationship(final String entityName, final Integer relationship, final Integer limitCustomfields) {
        if (limitCustomfields == null && "RequestForQuote".equals(entityName)) {
            final List<EdsCompanyCustomFieldsSettings> customFieldsRFQWithoutFirst = this.getCompanyCustomFields(entityName, null, null, relationship, null, null);
            if (customFieldsRFQWithoutFirst.size() > 0) {
                customFieldsRFQWithoutFirst.remove(0);
            }
            return customFieldsRFQWithoutFirst;
        } else {
            return this.getCompanyCustomFields(entityName, null, null, relationship, null, limitCustomfields);
        }
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFields(final String entityName, final String dataType, final String category, final Integer relationship, final Integer objectID, final Integer limitCustomFields) {
        String s = "";
        if (entityName != null) {
            s = entityName;
        }
        if (limitCustomFields != null) {
            return this.findLimited("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? " +
                    ((dataType != null) ? "and ccfs.dataType = '" + dataType + "'" : " ") + " " +
                    ((category != null) ? "and ccfs.entityCategoryName = '" + category + "'" : " ") + " " +
                    ((relationship != null) ? "and ccfs.relationship = '" + relationship + "'" : " ") +
                    ((objectID != null) ? " and ccfs.objectID != '" + objectID + "'" : " ") +
                    " order by ccfs.objectID asc ", limitCustomFields, s);
        } else {
            return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? " +
                    ((dataType != null) ? "and ccfs.dataType = '" + dataType + "'" : " ") + " " +
                    ((category != null) ? "and ccfs.entityCategoryName = '" + category + "'" : " ") + " " +
                    ((relationship != null) ? "and ccfs.relationship = '" + relationship + "'" : " ") +
                    ((objectID != null) ? " and ccfs.objectID != '" + objectID + "'" : " ") + " order by ccfs.objectID asc", s);
        }
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFields(final String entityName, final String dataType, final String category, final Integer relationship, final Integer objectID, final Integer limitCustomFields, Boolean withDaletedValues) {
        String s = "";
        if (entityName != null) {
            s = entityName;
        }
        if (withDaletedValues) {
            String sql = "select ccfs.* from " + getCompanyId() + ".companyCustomFieldsSettings ccfs where ccfs.entityName = '" + s + "' " +
                    ((dataType != null) ? "and ccfs.dataType = '" + dataType + "'" : " ") + " " +
                    ((category != null) ? "and ccfs.entityCategoryName = '" + category + "'" : " ") + " " +
                    ((relationship != null) ? "and ccfs.relationship = '" + relationship + "'" : " ") +
                    ((objectID != null) ? " and ccfs.id != '" + objectID + "'" : " ") + " order by ccfs.id asc";
            if (limitCustomFields != null) {
                sql += " limit " + limitCustomFields;
            }
            return (List<EdsCompanyCustomFieldsSettings>) findNative(sql, EdsCompanyCustomFieldsSettings.class);
        }
        return getCompanyCustomFields(entityName, dataType, category, relationship, objectID, limitCustomFields);
    }


    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByEntityName(final String entityName) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? order by ccfs.objectID asc", entityName);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCustomFieldsForQuickAdd(final String entityName) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.isRequired is not null and ccfs.isRequired is true order by ccfs.objectID asc", entityName);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByLookUpType(final String lookUpType, final boolean isCrmAccount, final boolean isCustomForm) {
        final StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(BaseManager.getCompanyId()).append(".companyCustomFieldsSettings where ");
        if (isCrmAccount) {
            sql.append("lookUpType in ('SUPPLIER', 'CUSTOMER')");
        } else {
            sql.append("lookUpType = '").append(lookUpType).append("' ");
        }
        if (isCustomForm) {
            sql.append(" and entityname = 'CustomFormItems'");
            sql.append(" and addTab = true");
        }
        return (List<EdsCompanyCustomFieldsSettings>) this.findNative(sql.toString(), EdsCompanyCustomFieldsSettings.class);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsForListView(final String entityName) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.showInListing = true order by ccfs.fieldName asc", entityName);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFormFiledsByFormId(String entityName, String entitycategoryname, String uiType) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.entityCategoryName = ? and ccfs.uiType = ? ", entityName, entitycategoryname, uiType);
    }


    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByCategoryForListView(final String entityName, final String category) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.entityCategoryName = ? and ccfs.showInListing = true order by ccfs.fieldName asc", entityName, category);
    }

    @Override
    public ArrayList<String> getCompanyCustomFieldsByEntityCategory(String entityCategory, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ccfs.fieldName from ").append("\"").append(companyId).append("\".").append("companyCustomFieldsSettings ccfs ");
        sql.append(" where ccfs.entityCategoryName ='").append(entityCategory).append("'");
        return (ArrayList<String>) findNative(sql.toString());
    }

    @Override
    public ArrayList<String> getCompanyCustomFieldsByEntityNative(String entityName, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ccfs.fieldName from ").append("\"").append(companyId).append("\".").append("companyCustomFieldsSettings ccfs ");
        sql.append(" where ccfs.entityname ='").append(entityName).append("'");
        return (ArrayList<String>) findNative(sql.toString());
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsForFiltering(final String entityName) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.showInFilterGrouping = true order by ccfs.fieldName asc", entityName);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsForBaseInvoices(final String entityName) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.isFacetable = true and ccfs.uiType != 'MultiLookup' order by ccfs.fieldName asc", entityName);
    }

    @Override
    public EdsCompanyCustomFieldsSettings getCompanyCustomField(final String entityName, final String columnCode) {
        final List<EdsCompanyCustomFieldsSettings> list = this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.columnCode = ?", entityName, columnCode);

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public EdsCompanyCustomFieldsSettings getCompanyCustomFieldColumnCode(final String entityName, final String categoryName, final String uitype) {
        final List<EdsCompanyCustomFieldsSettings> list = this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.uiType = ? and ccfs.entityCategoryName = ? ", entityName, uitype, CUSTOM_VIEW + categoryName.substring(0, categoryName.indexOf("_FORM")));

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public EdsCompanyCustomFieldsSettings getCompanyCustomField(final String entityName, final String categoryName, final String columnCode) {
        final List<EdsCompanyCustomFieldsSettings> list = this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.entityCategoryName = ? and ccfs.columnCode = ?", entityName, categoryName, columnCode);

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public EdsCompanyCustomFieldsSettings getByAliasName(final String entityName, final String aliasName) {
        return (EdsCompanyCustomFieldsSettings) this.findSingle("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and lower(ccfs.aliasName) = ?", entityName, aliasName.toLowerCase());
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCustomFields(final ListingFilterParameter fp) {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT cf FROM EdsCompanyCustomFieldsSettings cf ");
        sql.append("WHERE 1=1 ");

        if (fp.getObjectId() != null) {
            sql.append("AND cf.objectID != '").append(fp.getObjectId()).append("' ");
        }
        if (fp.getEntityName() != null) {
            sql.append("AND cf.entityName IN (").append(fp.getEntityName()).append(") ");
        }
        if (StringUtils.isNotBlank(fp.getCategory())) {
            sql.append("AND cf.entityCategoryName='").append(fp.getCategory()).append("' ");
        }

        if (fp.getDataType() != null) {
            sql.append("AND cf.dataType = '").append(fp.getDataType()).append("' ");
        }
        if (fp.getColumnCode() != null) {
            sql.append("AND cf.columnCode = '").append(fp.getColumnCode()).append("' ");
        }

        if (fp.getShowInListing() != null) {
            sql.append("AND cf.showInListing =").append(fp.getShowInListing()).append(" ");
        }

        if (fp.getShowInFilterGrouping() != null) {
            sql.append("AND cf.showInFilterGrouping = ").append(fp.getShowInFilterGrouping()).append(" ");
        }

        if (fp.getRelationID() != null) {
            sql.append("AND cf.relationship = '").append(fp.getRelationID()).append("' ");
        }

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (fp.isLookUp()) {
                sql.append("AND cf.fieldName LIKE '").append(fp.getSearchKey()).append("%' ");
            } else {
                sql.append("AND ( LOWER(cf.entityName) LIKE '").append(fp.getSqlSearchKey()).append("' OR ");
                sql.append("LOWER(cf.fieldName) LIKE '").append(fp.getSqlSearchKey()).append("' OR ");
                sql.append("LOWER(cf.dataType) LIKE '").append(fp.getSqlSearchKey()).append("' OR ");
                sql.append("LOWER(cf.uiType) LIKE '").append(fp.getSqlSearchKey()).append("' ) ");
            }
        }

        if (StringUtils.isNotBlank(fp.getSortField())) {
            sql.append("ORDER BY ");

            switch (fp.getSortField()) {
                case "entityname" -> sql.append("cf.entityName ");
                case "fieldname" -> sql.append("cf.fieldName ");
                case "uitype" -> sql.append("cf.uiType ");
                case "datatype" -> sql.append("cf.dataType ");
                case "aliasname" -> sql.append("cf.aliasName ");
                case "creationDate" -> sql.append("cf.objectID "); //to do
                case "createdBy" -> sql.append("cf.objectID "); //to do
                case "lastUpdatedDate" -> sql.append("cf.objectID "); //to do
                case "lastUpdatedBy" -> sql.append("cf.objectID "); //to do
                default -> sql.append("cf.objectID ");
            }

            if (!fp.isAscending()) {
                sql.append("DESC");
            }
        } else {
            sql.append("ORDER BY cf.entityName DESC ");
        }

        return this.findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyLeadContactAndOpportunityCustomFields(final String lead, final String contact, final String opportunity, final String account) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? or ccfs.entityName = ? or ccfs.entityName = ? or ccfs.entityName = ? order by ccfs.entityName asc", lead, contact, opportunity, account);
    }

    @Override
    public Integer getCustomFieldsCount(final ListingFilterParameter fp) {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(cf.objectID) FROM EdsCompanyCustomFieldsSettings cf ");
        sql.append("WHERE 1=1 ");

        if (fp.getEntityName() != null) {
            sql.append("AND cf.entityName IN (").append(fp.getEntityName()).append(") ");
        }

        if (fp.getColumnCode() != null) {
            sql.append("AND cf.columnCode = '").append(fp.getColumnCode()).append("' ");
        }

        if (fp.getShowInListing() != null) {
            sql.append("AND cf.showInListing =").append(fp.getShowInListing()).append(" ");
        }

        if (fp.getShowInFilterGrouping() != null) {
            sql.append("AND cf.showInFilterGrouping = ").append(fp.getShowInFilterGrouping()).append(" ");
        }

        if (fp.getRelationID() != null) {
            sql.append("AND cf.relationship = '").append(fp.getRelationID()).append("' ");
        }

        return ((Long) this.findSingle(sql.toString())).intValue();
    }

    @Override
    public EdsCompanyCustomFieldsSettings getCompanyCustomFieldByEntityNameAndFieldName(final String entityName, final String fieldName) {
        final List<EdsCompanyCustomFieldsSettings> list = this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.fieldName = ?", entityName, fieldName);

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void deleteCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null ");
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getAllDeletedCustomFieldsByViewName(String viewname, String categoryName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * from ").append(getCompanyId())
                .append(".companyCustomFieldsSettings ").append(" t where t.entityname = '")
                .append(viewname);
        if (categoryName != null) {
            sql.append("' and t.entityCategoryName = '").append(categoryName).append("'");
        } else {
            sql.append("' and t.deleted is true");
        }
        return findNative(sql.toString(), EdsCompanyCustomFieldsSettings.class);
    }

    @Override
    public void deleteProductCustomFieldValues(final String tableName, final String fieldName, final Integer relationship) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        if (relationship != null) {
            this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT itemcustomfieldsid FROM \"" + schema + "\".item WHERE categoryid = '" + relationship + "' )");
        } else {
            this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfieldsid FROM \"" + schema + "\".item where customfieldsid is not null )");

        }
    }

    @Override
    public void deleteLeadCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".crmContact WHERE contactType = '" + EdsCrmContact.LEAD_CONTACT + "' )");
    }

    @Override
    public void deleteCandidateCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".crmContact WHERE contactType = '" + EdsCrmContact.CANDIDATE + "' )");
    }

    @Override
    public void deleteOpportunityCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".opportunity )");
    }

    @Override
    public void deleteCrmAccountCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".crmAccount )");
    }

    @Override
    public void deleteContactCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".crmContact WHERE contactType = '" + EdsCrmContact.CRM_CONTACT + "' )");
    }

    @Override
    public void deleteCrmCaseCustomFieldValues(final String tableName, final String fieldName) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".crmCase)");
    }

    @Override
    public void deleteInvoiceCustomFieldValues(final String tableName, final String fieldName, final String type) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfields_id FROM \"" + schema + "\".invoice WHERE type = '" + type + "' )");
    }

    @Override
    public void deletePrepaymentCustomFieldValues(final String tableName, final String fieldName, final String type) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT prepaymentCustomFields_id FROM \"" + schema + "\".invoicePayments WHERE type = '" + type + "' )");
    }

    @Override
    public void deleteQuoteCustomFieldValues(final String tableName, final String fieldName, final String type, final Boolean isSaleOrder) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        final StringBuilder sql = new StringBuilder();
        sql.append("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT q.customfields_id FROM \"" + schema + "\".quote q ");
        if ("PAYABLE".equals(type)) {
            sql.append(" WHERE q.type = '" + type + "' )");
        } else {
            sql.append(" left join \"" + schema + "\".salequote sq on sq.id = q.id ");
            sql.append(" WHERE q.type = '" + type + "' ");
            if (isSaleOrder) {
                sql.append(" and sq.issalesorder = true ");
            } else {
                sql.append(" and sq.issalesorder is not true ");
            }
            sql.append(")");
        }

        this.updateNative(sql.toString());
    }

    @Override
    public void deleteCustomFormCustomFieldValues(final String tableName, final String fieldName, final String form_id) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT form_customfieldsid FROM \"" + schema + "\".custom_form_item WHERE form_id = '" + form_id + "' )");
    }

    @Override
    public void deleteCustomFormItemTableCustomFieldValues(final String tableName, final String fieldName, final String uuid) {
        final String schema = ServerSecurityContext.getInstance().getCompanyId();
        this.updateNative("UPDATE \"" + schema + "\"." + tableName + " SET " + fieldName + "= null WHERE id IN (SELECT customfieldsid FROM \"" + schema + "\".custom_item_table WHERE uuid = '" + uuid + "' )");
    }

    @Override
    public boolean isCustomAliasNameExists(final String type, final String category, final String aliasName, final Integer fieldID) {
        if (fieldID != null) {
            return this.find("select cf from EdsCompanyCustomFieldsSettings cf where cf.entityName = ? " +
                    ((category != null) ? "and cf.entityCategoryName = '" + category + "'" : " ") + " " +
                    " and cf.aliasName = ? and cf.objectID != ? and cf.relationship is null", type.trim(), aliasName.trim(), fieldID).size() > 0;
        } else {
            return this.find("select cf from EdsCompanyCustomFieldsSettings cf where cf.entityName = ? " +
                    ((category != null) ? "and cf.entityCategoryName = '" + category + "'" : " ") + " " +
                    " and cf.aliasName = ?", type.trim(), aliasName.trim()).size() > 0;
        }
    }

    @Override
    public void deleteCustomFieldValidations(final Integer objectID) {
        this.update("UPDATE  EdsCustomFieldValidation cfv  SET cfv.joinedField.objectID = null  where cfv.joinedField.objectID = ?", objectID);
        this.update("DELETE FROM EdsCustomFieldValidation cfv WHERE cfv.customfield.objectID = ?", objectID);
    }

    @Override
    public EdsCompanyCustomFieldsSettings getByColumnCode(final String viewName, final String columnCode) {
        return (EdsCompanyCustomFieldsSettings) this.findSingle("select setting from EdsCompanyCustomFieldsSettings setting where setting.entityName = '" + viewName + "' and setting.columnCode ='" + columnCode + "'");
    }

    @Override
    public SelectItem[] getCustomFieldDataByQuery(final Integer companyID, String query, String ... searchKey ) {
        String searchkey = Arrays.toString(searchKey);
        String key = searchkey.substring(1,searchkey.length()-1);
        if (query == null || query.isEmpty()) {
            return new SelectItem[0];
        }
        query = query.replace("anv", companyID != null ? String.valueOf(companyID) : ServerSecurityContext.getInstance().getCompanyId());
        query = query.replace("#\\?user_id\\?#", ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID().toString());
        String s;
        if (!"".equals(key)) {
            s = "select id,name from (" + query.replace(';',' ') + ") sd where name ilike ('%" + key + "%')  limit 20 ;";
        } else {
            s = query;
        }
        final List<Object[]> items = this.findNative(s);
        final List<SelectItem> selectItems = new ArrayList<>();
        for (final Object[] objects : items) {
            final SelectItem selectItem;
            if (objects.length < 2) {
                selectItem = new SelectItem(Integer.valueOf(String.valueOf(objects[0])), "");
            } else if (objects.length == 3) {
                selectItem = new SelectItem(Integer.valueOf(String.valueOf(objects[0])), String.valueOf(objects[1]), String.valueOf(objects[2]));
            } else {
                selectItem = new SelectItem(Integer.valueOf(String.valueOf(objects[0])), String.valueOf(objects[1]));
            }
            selectItem.setSelectedId(Integer.valueOf(String.valueOf(objects[0])));
            selectItems.add(selectItem);
        }
        return selectItems.toArray(new SelectItem[0]);
    }

    @Override
    public List<String> getCompanyCustomFieldsColumnCodesList(final String entityName) {
        return this.find("SELECT ccfs.columnCode FROM EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ? and ccfs.showInListing = true order by ccfs.objectID asc", entityName);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyFileUploadCustomFields() {
        final String dataTypeString = "File Upload";
        return this.find("select cfucf from EdsCompanyCustomFieldsSettings cfucf where cfucf.dataType = ?", dataTypeString);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsWithCategory(final String entityName, final String entityCategory) {
        return this.find("select ccfs from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ?  and ccfs.entityCategoryName = ? " +
                " order by ccfs.objectID asc", entityName, entityCategory);
    }

    @Override
    public List<String> getSeeRelatedCompanyCustomFields(final String entityName, final String entityCategory) {
        return this.find("select ccfs.columnCode from EdsCompanyCustomFieldsSettings ccfs where ccfs.entityName = ?  and ccfs.entityCategoryName = ? and ccfs.seeOwnPermission is true" +
                " order by ccfs.objectID asc", entityName, entityCategory);
    }

    @Override
    public void copyCustomFields(final Integer fromCompanyID, final Integer toCompanyID, final String entityName, final String categoryName) {
        if (fromCompanyID != null && entityName != null && categoryName != null) {
            final List<EdsCompanyCustomFieldsSettings> cfS = this.getCompanyCustomFieldsByCategoryFromCompany(fromCompanyID, entityName, categoryName);
            if (cfS != null && cfS.size() > 0) {
                int i = 0;
                final StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO \"").append(toCompanyID).append("\".companyCustomFieldsSettings (entityname, aliasname, fieldname, datatype, uitype, query, " +
                        "predefinedValues, showinlisting, clickable, showinfiltergrouping, columncode, relationship, isFacetable, isRequired, entityCategoryName, entityCategoryAlias) VALUES ");
                for (final EdsCompanyCustomFieldsSettings cf : cfS) {
                    sql.append("(").append(cf.getEntityName() != null ? "'" + cf.getEntityName() + "'" : null).append(",");
                    sql.append(cf.getAliasName() != null ? "'" + cf.getAliasName() + "'" : null).append(",");
                    sql.append(cf.getFieldName() != null ? "'" + cf.getFieldName() + "'" : null).append(",");
                    sql.append(cf.getDataType() != null ? "'" + cf.getDataType() + "'" : null).append(",");
                    sql.append(cf.getUiType() != null ? "'" + cf.getUiType() + "'" : null).append(",");
                    sql.append(cf.getQuery() != null ? "'" + cf.getQuery() + "'" : null).append(",");
                    sql.append(cf.getPredefinedValues() != null ? "'" + cf.getPredefinedValues() + "'" : null).append(",");
                    sql.append(cf.isShowInListing()).append(",");
                    sql.append(cf.isClickable()).append(",");
                    sql.append(cf.isShowInFilterGrouping()).append(",");
                    sql.append(cf.getColumnCode() != null ? "'" + cf.getColumnCode() + "'" : null).append(",");
                    sql.append(cf.getRelationship()).append(",");
                    sql.append(cf.getFacetable() != null ? cf.getFacetable() : "false").append(",");
                    sql.append(cf.getRequired() != null ? cf.getRequired() : "false").append(",");
                    sql.append(cf.getEntityCategoryName() != null ? "'" + cf.getEntityCategoryName() + "'" : null).append(",");
                    sql.append(cf.getEntityCategoryAlias() != null ? "'" + cf.getEntityCategoryAlias() + "'" : null).append(")");
                    i++;
                    if (i < cfS.size()) {
                        sql.append(", ");
                    }
                }
                this.updateNative(sql.toString());
            }
        }
    }

    @Override
    public void deleteStepCustomFieldPermissions(final String viewName) {
        this.updateNative("delete from " + BaseManager.getCompanyId() + ".customfield_permissions where customfield_id in (select id from " + BaseManager.getCompanyId() + ".companyCustomFieldsSettings where entityName = 'OnboardingStep' and entityCategoryName = '" + viewName + "')");
    }

    @Override
    public void deleteStepCustomFields(final String viewName) {
        this.updateNative("delete from " + BaseManager.getCompanyId() + ".companyCustomFieldsSettings where entityName = 'OnboardingStep' and entityCategoryName = '" + viewName + "'");
    }

    @Override
    public boolean isCustomNameExists(final String viewName, final String name) {
        return this.find("select cf from EdsCompanyCustomFieldsSettings cf where cf.entityName = ? and (cf.aliasName = ? or cf.fieldName = ?)", viewName, name, name).size() > 0;
    }

    private List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByCategoryFromCompany(final Integer companyID, final String entityName, final String categoryName) {
        return (List<EdsCompanyCustomFieldsSettings>) this.findNative("select * from \"" + companyID + "\".companyCustomFieldsSettings where entityname = '" + entityName + "' and entityCategoryName = '" + categoryName + "'", EdsCompanyCustomFieldsSettings.class);
    }

    @Override
    public List<String> getValuesOfAutoNumberingByTableName(final CompanyCustomFieldItem customFieldItem, boolean checkForDate) {
        if (customFieldItem == null || customFieldItem.getEntityCategoryName() == null) {
            log.error("customFieldItem or getEntityCategoryName() is null");
            return new ArrayList<>();
        }
        final String formId = customFieldItem.getEntityCategoryName().replaceFirst(CUSTOM_VIEW, "") + "_FORM";
        final String columnCode = customFieldItem.getColumnCode();
        final String sql = "select " + columnCode + " from " +
                BaseManager.getCompanyId() + ".customform_customfields cf" +
                " join " + BaseManager.getCompanyId() + ".custom_form_item cfi" +
                " on cfi.form_customfieldsid = cf.id" +
                " where cfi.form_id = '" + formId + "'" +
                (checkForDate ? " and cfi.creationdate >= '" + ServerUtils.getYearStartDate(ServerUtils.getYear(new Date())) + "'" : "") +
                " order by " + columnCode;
        final List<String> result = this.findNative(sql);
        if (result != null && result.size() == 0) {
            return null;
        }
        return result;
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getEntityCustomFields(String entityName, String entityCategoryName) {
        return (List<EdsCompanyCustomFieldsSettings>) findNative("select * from " + BaseManager.getCompanyId() + ".companyCustomFieldsSettings where entityname = '" + entityName + "'" + (entityCategoryName != null ? " and entitycategoryname = '" + entityCategoryName + "'" : "") + " and (uitype = 'EntityLookUp' or uitype = 'EntityDropDown')", EdsCompanyCustomFieldsSettings.class);
    }

    @Override
    public List<String> getCustomFieldsCodeForLocale() {
        return (List<String>) findNative("select * from " + getCompanyId() + ".companyCustomFieldsSettings where entityname = 'Employee' and (uitype = 'MultiLookup' or uitype = 'LOOKUP') and (lookuptype = 'POSITION' or lookuptype = 'DEPARTMENT')");
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCFByUiTypes(String viewName, List<String> uiTypes, String entityCategoryName) {
        Map<String, Object> params = new HashMap<>();
        params.put("viewName", viewName);
        params.put("uiTypes", uiTypes);
        if (entityCategoryName != null) {
            params.put("entityCategoryName", entityCategoryName);
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cf FROM EdsCompanyCustomFieldsSettings cf where cf.entityName =:viewName and cf.uiType in(:uiTypes)"
                + (entityCategoryName != null ? " and cf.entityCategoryName=:entityCategoryName " : ""));
        return findByNamedParams(sql.toString(), params);
    }

    @Override
    public List<EdsCompanyCustomFieldsSettings> getCFByUiTypesForHrBot() {
        StringBuilder query = new StringBuilder()
                .append("SELECT cf FROM EdsCompanyCustomFieldsSettings cf ")
                .append("WHERE cf.entityName = 'Candidate' AND cf.deleted <> true  AND ( ")
                .append("cf.uiType IN ('TextBox','TextArea') ")
                .append("OR ")
                .append("(cf.uiType IN ('LOOKUP') AND cf.lookUpType = 'REFERENCE')) ");
        return find(query.toString());
    }

    @Override
    public Map<Integer, EdsCompanyCustomFieldsSettings> getCFByIdsForHrBot(List<Integer> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) return Collections.emptyMap();
        StringBuilder query = new StringBuilder()
                .append("SELECT cf FROM EdsCompanyCustomFieldsSettings cf ")
                .append("WHERE cf.entityName = 'Candidate' AND cf.deleted <> true ")
                .append("AND cf.objectID IN :fieldIds ")
                .append("AND (cf.uiType IN ('TextBox','TextArea') ")
                .append("OR (cf.uiType IN ('LOOKUP') AND cf.lookUpType = 'REFERENCE')) ");

        Map<String, Object> params = new HashMap<>();
        params.put("fieldIds", fieldIds);

        List<EdsCompanyCustomFieldsSettings> result = findByNamedParams(query.toString(), params);
        return result.stream()
                .collect(Collectors.toMap(EdsCompanyCustomFieldsSettings::getObjectID, Function.identity()));
    }



    @Override
    public ArrayList<EdsCompanyCustomFieldsSettings> getDeletedCustomFieldsByType(ArrayList<String> fieldTypes) {
        /// agarda deleted data chiqmasa bu entity ni ustidagi where anotatsiya tufayli bulishi mn
//        Session session = masterEntityManager.unwrap(Session.class);
//        session.disableFilter("");
        String sql = "select ccs.* from " + getCompanyId() + ".companyCustomFieldsSettings ccs where deleted is true and datatype in (" + ServerUtils.getAsCommoDelimited(fieldTypes, "0", ",") + ")";
        return (ArrayList<EdsCompanyCustomFieldsSettings>) findNative(sql, EdsCompanyCustomFieldsSettings.class);
    }

    @Override
    public void clearAllDeletedFields(List<Integer> ids) {
        updateNative("delete from " + getCompanyId() + ".customfield_permissions where customfield_id in (select id from " + getCompanyId() + ".companyCustomFieldsSettings where id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ") and deleted is true) ");
        updateNative("delete from " + getCompanyId() + ".companyCustomFieldsSettings where id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ") and deleted is true");
    }
}
