package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Mar 19, 2010
 * Time: 4:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("emailTemplateManager")
public class EmailTemplateManagerImpl extends BaseManager<EdsEmailTemplate> implements
        EmailTemplateManager, Constants, EmailTemplateConstants {

    public EmailTemplateManagerImpl() {
        super(EdsEmailTemplate.class);
    }

    public List<EdsEmailTemplate> getCompanyEmailTemplates(ListingFilterParameter fp) {
        StringBuilder s = new StringBuilder();
        if (getUser() != null) {
            s.append(" and (");
            s.append("userID is null or userID = " + getUser().getObjectID());
            s.append(") ");
        }
        if (fp.isValidSearchKey()) {
            s.append(" and (");
            s.append("lower(name) like '" + fp.getSqlSearchKey() + "'");
            s.append(") ");
        }
        //search by category
        if (fp.getGroupByName() != null && !"".equals(fp.getGroupByName()) && fp.getSearchType() == 0) {
            s.append(" and '" + fp.getGroupByName() + "'=category.code ");
        }
        //search by is default
        if (fp.getGroupByName() != null && !"".equals(fp.getGroupByName()) && fp.getSearchType() == 1) {
            s.append(" and '" + fp.getGroupByName() + "'=isDefault");
        }
        //get by module
        if (fp.getModule() != null) {
            s.append(" and module.code = '" + fp.getModule() + "' ");
        }

        s.append(" ORDER BY ");
        if (fp.getSortField() == null && "".equals(fp.getSortField())) {
            s.append("name");
        } else if (EmailTemplateItem.TEMPLATE_NAME.equals(fp.getSortField())) {
            s.append("name");
        } else if (EmailTemplateItem.TEMPLATE_SUBJECT.equals(fp.getSortField())) {
            s.append("subject");
        } else if (EmailTemplateItem.TEMPLATE_CATEGORY.equals(fp.getSortField())) {
            s.append("category.name");
        } else if (EmailTemplateItem.TEMPLATE_IS_DEFAULT.equals(fp.getSortField())) {
            s.append("isDefault");
        } else if (EmailTemplateItem.TEMPLATE_ONLY_MINE.equals(fp.getSortField())) {
            s.append("userID");
        } else {
            s.append("name");
        }

        if (fp.isAscending()) {
            s.append(" DESC ");
        }

        if (fp != null && fp.getParams() != null && "EmailTemplateListView".equals(fp.getParams())) {
            Locale locale = null;
            if (ServerUtils.getUserLocale() != null) {
                String localeCode = ServerUtils.getUserLocale().getLanguage();
                String localeCountry = ServerUtils.getUserLocale().getCountry();

                if (localeCode != null && !StringUtils.isEmpty(localeCountry)) {
                    localeCode = localeCode + "_" + localeCountry;

                    if ("en_gb".equalsIgnoreCase(localeCode)) {
                        locale = new Locale(localeCode.toLowerCase());
                    }
                }
            }
            if (locale != null) {
                return find("from EdsEmailTemplate where (deleted is null or deleted<>true) AND (locale is null or locale = ? or locale = ?)" + s, ServerUtils.getUserLocale(), locale);
            } else {
                return find("from EdsEmailTemplate where (deleted is null or deleted<>true) AND (locale is null or locale = ?)" + s, ServerUtils.getUserLocale());
            }
        } else {
            return find("SELECT t FROM EdsEmailTemplate t WHERE (t.deleted is null or t.deleted<>true) AND (t.isCompanyEmailTemplate = '" + COMPANY_EMAIL_TEMPLATE + "') " + s);
        }
    }

    public List<EdsEmailTemplate> getCompanyAutoResponseTemplates() {
        return find("select temp from EdsEmailTemplate temp where " +
                "(temp.deleted is null or temp.deleted<>true) and (temp.isCompanyEmailTemplate = '" + COMPANY_EMAIL_TEMPLATE + "') and temp.category.code = '" + CASE_AUTO_RESPONSE_CATEGORY + "' order by temp.name");
    }

    @Override
    public List<EdsEmailTemplate> getEmailTemplatesByCategory(String messageCenterCategory, com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter params) {
        return findInterval("SELECT t FROM EdsEmailTemplate t WHERE (t.deleted is null or t.deleted<>true) AND (t.userID is null or t.userID" + (getUser() != null ? "=" + getUser().getObjectID() + ")" : " is null)") + " AND (t.isCompanyEmailTemplate = '" + COMPANY_EMAIL_TEMPLATE + "') AND t.category.code=? ORDER BY t.name", params.getStart(), params.getLimit(), messageCenterCategory);
    }

    public void updateDefaultTemplate(Integer objectId, Integer categoryId) {
        if (objectId != null) {
            updateNative("update " + getCompanyId() + ".emailtemplate set isDefault=false where categoryId=" + categoryId + " and id!=" + objectId);
        } else {
            updateNative("update " + getCompanyId() + ".emailtemplate set isDefault=false where categoryId=" + categoryId);
        }
    }

    public Long getCountNonDeletedEmailTemplates(Integer categoryID) {
        return (Long) findSingle("SELECT count(et.objectID) FROM EdsEmailTemplate et WHERE (et.deleted is null or et.deleted<>true) AND et.category.objectID=?", categoryID);
    }

    public List<EdsEmailTemplate> getEmailTemplatesByCategory(String categoryCode) {
        return find("SELECT t FROM EdsEmailTemplate t WHERE (deleted is null or deleted<>true) AND (t.userID is null or t.userID" + (getUser() != null ? "=" + getUser().getObjectID() + ")" : " is null)") + " AND t.category.code=? AND (locale is null or locale = ?) ORDER BY t.name", categoryCode, ServerUtils.getUserLocale());
    }

    public List<EdsEmailTemplate> getEmailTemplatesForMessageCenter(ArrayList<String> templateModule) {
        String templateModules = "'" + ServerUtils.getAsCommoDelimited(templateModule, "", "','") + "'";
        return find("SELECT t FROM EdsEmailTemplate t WHERE (deleted is null or deleted<>true) AND (t.userID is null or t.userID" + (getUser() != null ? "=" + getUser().getObjectID() + ")" : " is null)") + " AND (t.module.code in (" + templateModules + ") and showInMessageCenter = true) AND (locale is null or locale = ?) ORDER BY t.name", ServerUtils.getUserLocale());
    }

    public List<EdsEmailTemplate> getEmailTemplates(ArrayList<String> templateModule) {
        String templateModules = "'" + ServerUtils.getAsCommoDelimited(templateModule, "", "','") + "'";
        return find("SELECT t FROM EdsEmailTemplate t WHERE (t.deleted is null or t.deleted is false) AND (t.userID is null or t.userID" + (getUser() != null ? "=" + getUser().getObjectID() + ")" : " is null)") + " AND (t.module.code in ( " + templateModules + ") ) AND (t.locale is null or t.locale = ?) ORDER BY t.name", ServerUtils.getUserLocale());
    }

    public EdsEmailTemplate getCompanyDefaultEmailTemplatesByCategory(String categoryCode) {
        if (SMS_TEMPLATE_CATEGORY.equals(categoryCode)) {
            return (EdsEmailTemplate) findSingle("from EdsEmailTemplate where (deleted is null or deleted is false) and (isCompanyEmailTemplate = '" + DEFAULT_EMAIL_TEMPLATE + "') and category.code=? and isDefault=true and locale = ?", categoryCode, ServerUtils.getUserLocale());
        } else {
            return (EdsEmailTemplate) findSingle("from EdsEmailTemplate where (deleted is null or deleted is false) and (isCompanyEmailTemplate = '" + COMPANY_EMAIL_TEMPLATE + "') and category.code=? and isDefault=true", categoryCode);
        }
    }

    public EdsEmailTemplate getDefaultEmailTemplateByCategory(String categoryCode) {
        return (EdsEmailTemplate) findSingle("from EdsEmailTemplate where (deleted is null or deleted<>true) and category.code=? and locale = ? and isDefault=true", categoryCode, ServerUtils.getUserLocale());
    }

    public EdsEmailTemplate getEmailTemplateByCategory(String categoryCode) {
        return (EdsEmailTemplate) findSingle("from EdsEmailTemplate where (deleted is null or deleted<>true) and (isCompanyEmailTemplate = '" + DEFAULT_EMAIL_TEMPLATE + "') and category.code=? and locale = ? ", categoryCode, ServerUtils.getUserLocale());
    }

    /*public List<EdsEmailTemplate> getTemplatesByCustomEntityAndAction(String customEntityID, EntityAction action) {
        return (List<EdsEmailTemplate>) find("from EdsEmailTemplate where (deleted is null or deleted<>true) and customEntity.externalGUID=? and action = ?", customEntityID, action);
    }*/

    public List<EdsEmailTemplate> getTemplatesByCustomEntity(Integer customEntityID) {
        return (List<EdsEmailTemplate>) find("from EdsEmailTemplate where (deleted is null or deleted<>true) and customEntity.objectID=?", customEntityID);
    }

/*
    public List<EdsEmailTemplate> getTemplatesBySystemEntity(EntityType entityType) {
        return (List<EdsEmailTemplate>) find(" from EdsEmailTemplate where (deleted is null or deleted<>true) and entityType = ?", entityType);
    }

    public List<EdsEmailTemplate> getTemplatesByAction(EntityAction action) {
        return (List<EdsEmailTemplate>) find(" from EdsEmailTemplate where (deleted is null or deleted<>true) and action = ?", action);
    }

    public EdsEmailTemplate getTemplatesByEntityTypeAndAction(EntityType entityType, EntityAction action) {
        return (EdsEmailTemplate) findSingle("select et from EdsEmailTemplate et where (et.deleted is null or et.deleted<>true) and et.entityType=? and et.action = ?", entityType, action);
    }

    @Override
    public EdsEmailTemplate getWfpEmailTemplateByName(String name, EntityType entityType, EntityAction action) {
        return (EdsEmailTemplate) findSingle("SELECT et FROM EdsEmailTemplate et WHERE et.deleted is not true AND et.name = ? AND et.entityType=? AND et.action = ?", name, entityType, action);
    }
*/

    @Override
    public List<EdsEmailTemplate> getWfpEmailTemplates() {
        return find("SELECT et FROM EdsEmailTemplate et WHERE et.deleted is not true AND et.entityType is not null AND et.action is not null ");
    }

    @Override
    public void updateFromEmail(String email, String activeEmails) {
        update("update EdsEmailTemplate et set et.fromEmail = '" + email + "' where et.fromEmail not in (" + activeEmails + ")");
    }
}
