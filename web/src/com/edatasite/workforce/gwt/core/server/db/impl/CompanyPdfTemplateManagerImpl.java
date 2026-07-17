package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.core.client.enums.PDFTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateListItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 17:41:17
 * To change this template use File | Settings | File Templates.
 */
@Repository("companyPdfTemplateManager")
public class CompanyPdfTemplateManagerImpl extends BaseManager<EdsCompanyPdfTemplate> implements CompanyPdfTemplateManager {
    public CompanyPdfTemplateManagerImpl() {
        super(EdsCompanyPdfTemplate.class);
    }

    @Override
    public EdsCompanyPdfTemplate getCompanyPdfTemplateByIDOrCode(Integer companyID, String pdfReferenceCode, Integer selectedTemplateID) {
        return getCompanyPdfTemplateByIDOrCode(companyID, pdfReferenceCode, selectedTemplateID, false);
    }

    public EdsCompanyPdfTemplate getCompanyPdfTemplateByIDOrCode(Integer companyID, String pdfReferenceCode, Integer selectedTemplateID, boolean isBrowserVersion) {
        StringBuilder query = new StringBuilder();
        query.append("select t from EdsCompanyPdfTemplate t join t.template.type ttt ");
        query.append("where (t.deleted=false or t.deleted is null) ");
        query.append("and t.browserVersion = " + isBrowserVersion);
        if (selectedTemplateID != null) {
            query.append(" and t.objectID = '" + selectedTemplateID + "' ");
            return (EdsCompanyPdfTemplate) findSingle(query.toString());
        } else if (pdfReferenceCode != null) {
            query.append(" and ttt.code = '" + pdfReferenceCode + "' ");
            query.append("and t.defaultTemplate=true ");
            return (EdsCompanyPdfTemplate) findSingle(query.toString());
        }
        return null;
    }

    @Override
    public List<EdsCompanyPdfTemplate> getCompanyPDFTemplatesByType(String type) {
        return getCompanyPDFTemplatesByType(type, false);
    }

    @Override
    public List<EdsCompanyPdfTemplate> getCompanyPDFTemplatesByType(String type, boolean isBrowserVersion) {
        StringBuilder query = new StringBuilder();
        query.append("select t from EdsCompanyPdfTemplate t join t.template.type ttt ");
        query.append("where t.deleted = false ");
        query.append("and ttt.code = ? ");
        query.append("and t.browserVersion = ? order by t.name ");
        return find(query.toString(), type, isBrowserVersion);
    }

    @Override
    public List<EdsCompanyPdfTemplate> getCompanyPDFTemplatesByTypeWithFormId(String type, String formId) {
        StringBuilder query = new StringBuilder();
        query.append("select t from EdsCompanyPdfTemplate t join t.template.type ttt ");
        query.append("where t.deleted = false ");
        query.append("and ttt.code = ? ");
        query.append("and t.customFormItemFormId = ? order by t.name ");
        return find(query.toString(), type, formId);
    }

    @Override
    public List<EdsCompanyPdfTemplate> getClientPDFTemplatesByType(String type) {
        StringBuilder query = new StringBuilder();
        query.append("select t from EdsCompanyPdfTemplate t join t.template.type ttt ");
        query.append("where t.deleted = false ");
        query.append("and ttt.code = ? ");
        query.append("and t.isClientPdf = true order by t.name ");
        return find(query.toString(), type);
    }

    @Override
    public List<EdsCompanyPdfTemplate> getCompanyPDFTemplates(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT ct FROM EdsCompanyPdfTemplate ct join ct.template.type ttt ");
        sql.append(" LEFT JOIN ct.template t ");
        sql.append(" WHERE " + ServerUtils.checkForDeleted("ct.deleted"));
        sql.append(" AND " + ServerUtils.checkForDeleted("t.deleted"));
        if (fp.getRelationType() != null) {
            sql.append(" and ttt.code = '").append(fp.getRelationType()).append("' ");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND lower(ct.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        sql.append(" ORDER BY ct.objectID DESC");

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getCompanyPDFTemplatesCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(ct.objectID) FROM EdsCompanyPdfTemplate ct join ct.template.type ttt ");
        sql.append(" LEFT JOIN ct.template t ");
        sql.append(" WHERE " + ServerUtils.checkForDeleted("ct.deleted"));
        sql.append(" AND " + ServerUtils.checkForDeleted("t.deleted"));
        if (fp.getRelationType() != null) {
            sql.append(" and ttt.type.code = '").append(fp.getRelationType()).append("' ");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND lower(ct.name) like '").append(fp.getSqlSearchKey()).append("'");
        }
        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public EdsCompanyPdfTemplate getPdfTemplateByEntityID(Integer websiteId, String entityId, String templateId) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT pdf FROM EdsCompanyPdfTemplate pdf ");
        buffer.append(" inner join pdf.customEntity cus");
        buffer.append(" left join pdf.template tem ");
        buffer.append(" where cus.objectID = ");
        buffer.append(Integer.parseInt(entityId));
        buffer.append(" where cus.websiteID = ");
        buffer.append(websiteId);
        if (templateId != null) {
            buffer.append(" and pdf.objectID = ");
            buffer.append(Integer.parseInt(templateId));
        }
        return (EdsCompanyPdfTemplate) findSingle(buffer.toString());
    }

    @Override
    public ArrayList<EdsCompanyPdfTemplate> getPdfTemplateListByEntityGUID(Integer websiteId, String entityGUID) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT pdf FROM EdsCompanyPdfTemplate pdf ");
        buffer.append(" inner join pdf.customEntity cus");
        buffer.append(" left join pdf.template tem ");
        buffer.append(" where cus.externalGUID = ");
        buffer.append("'" + entityGUID + "'");
        buffer.append(" and cus.websiteID = " + websiteId);

        return (ArrayList<EdsCompanyPdfTemplate>) find(buffer.toString());
    }

    public EdsCompanyPdfTemplate getDefaultCompanyPdfTemplateByType(String type) {
        return (EdsCompanyPdfTemplate) findSingle("select t from EdsCompanyPdfTemplate t join t.template.type ttt where t.deleted = false and ttt.code = ? and defaultTemplate = true order by t.name", type);
    }

    @Override
    public void removeCustomEntityPdfTemplate(String templateID) {
        update("delete EdsCompanyPdfTemplate pdf where pdf.objectID =?", Integer.parseInt(templateID));
    }

    @Override
    public Integer getSettingsPdfTemplatesCount(ListingFilterParameter fp) {
        boolean hasSearch = StringUtils.isNotEmpty(fp.getSearchKey());
        String sql = "select count(ct.objectID) from EdsCompanyPdfTemplate ct, EdsPdfTemplate t " +
                "  where ct.template = t.objectID" +
                "      and t.deleted = false" +
                "      and ct.deleted = false" +
                "      and ct.isClientPdf = true";
        if (hasSearch) {
            sql += "    and lower(ct.name) like :searchKey";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sql, Long.class)
                .setMaxResults(1);
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        List<Long> list = query.getResultList();

        return list.isEmpty() ? 0 : list.get(0).intValue();
    }

    @Override
    public List<EdsCompanyPdfTemplate> getSettingsPdfTemplates(ListingFilterParameter fp) {
        boolean hasSearch = StringUtils.isNotEmpty(fp.getSearchKey());
        boolean hasSort = StringUtils.isNotEmpty(fp.getSortField());
        String sql = "select ct from EdsCompanyPdfTemplate ct, EdsPdfTemplate t " +
                " left join ct.updator u " +
                "  where ct.template = t.objectID" +
                "      and t.deleted = false" +
                "      and ct.deleted = false" +
                "      and ct.isClientPdf = true";
        if (hasSearch) {
            sql += "    and lower(ct.name) like :searchKey";
        }
        if (hasSort) {
            switch (fp.getSortField()) {
                case SettingsPdfTemplateListItem.NAME -> sql += " order by ct.name";
                case SettingsPdfTemplateListItem.CATEGORY -> sql += " order by ct.template.type.name";
                case SettingsPdfTemplateListItem.MODIFIED_BY -> sql += " order by u.firstName||' '||u.lastName ";
                case SettingsPdfTemplateListItem.MODIFIED_DATE -> sql += " order by ct.updatedDate";
                case SettingsPdfTemplateListItem.CREATION_DATE -> sql += " order by ct.createdDate";
                default -> sql += " order by ct.objectID";
            }
            sql += fp.isAscending() ? " asc" : " desc";
        } else {
            sql += " order by ct.objectID desc";
        }
        TypedQuery<EdsCompanyPdfTemplate> query = slaveEntityManager.createQuery(sql, EdsCompanyPdfTemplate.class)
                .setFirstResult(fp.getStart())
                .setMaxResults(fp.getLimit());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        return query.getResultList();
    }

    @Override
    public void updateDefaultTemplates(Integer objectID, Integer typeId) {
        masterEntityManager.createNativeQuery("update " + getCompanyId() + ".companypdftemplate set defaultTemplate = false" +
                "     where (deleted = false or deleted is null)" +
                "         and id != :objectId" +
                "         and templateid in (select id from " + getCompanyId() + ".pdftemplate " +
                "                                   where typeid = :typeId)")
                .setParameter("typeId", typeId)
                .setParameter("objectId", objectID)
                .executeUpdate();
    }

    @Override
    public Integer getCompanyBarcodePDFTemplateId() {
        EdsCompanyPdfTemplate companyPdfTemplate = (EdsCompanyPdfTemplate) findSingle("select t from EdsCompanyPdfTemplate t where t.deleted = false and t.templateType = ? and defaultTemplate = true order by t.name", PDFTemplateTypeEnum.BARCODE);
        return companyPdfTemplate != null ? companyPdfTemplate.getObjectID() : null;
    }
}
