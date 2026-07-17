package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateTableSettings;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateTableSettingsManager;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 14.01.2019 19:37
 */
@Repository("pdfTemplateTableSettingsManager")
public class PdfTemplateTableSettingsManagerImpl extends BaseManager<EdsPdfTemplateTableSettings> implements PdfTemplateTableSettingsManager {

    public PdfTemplateTableSettingsManagerImpl() {
        super(EdsPdfTemplateTableSettings.class);
    }

    @Override
    public List<EdsPdfTemplateTableSettings> getListByTypeAndTableType(Integer pdfId, PdfTemplateTypeEnum typeEnum,
                                                                       PdfTemplateTableTypeEnum tableEnum) {
        boolean hasPdfId = pdfId != null;
        String sqlQuery = "select p from EdsPdfTemplateTableSettings p " +
                          " where p.pdfType = :pdfType " +
                          "     and p.pdfTableType = :tableType ";
        if (hasPdfId) {
            sqlQuery += " and p.companyPdfTemplate.objectID =:pdfId ";
        } else {
            sqlQuery += " and p.companyPdfTemplate is null ";
        }
        sqlQuery += " order by p.sorder";
        TypedQuery<EdsPdfTemplateTableSettings> query = slaveEntityManager.createQuery(sqlQuery, EdsPdfTemplateTableSettings.class)
                                                                     .setParameter("pdfType", typeEnum)
                                                                     .setParameter("tableType", tableEnum);
        if (hasPdfId) {
            query.setParameter("pdfId", pdfId);
        }
        return query.getResultList();
    }

    @Override
    public EdsPdfTemplateTableSettings getItemByTypeAndTableTypeAndColumnCode(Integer pdfId, PdfTemplateTypeEnum typeEnum,
                                                                              PdfTemplateTableTypeEnum tableEnum, String columnCode) {
        boolean hasPdfId = pdfId != null;
        String sqlQuery = "select p from EdsPdfTemplateTableSettings p " +
                          " where p.pdfType = :pdfType " +
                          "     and p.pdfTableType = :tableType " +
                          "     and p.columnCode = :columnCode ";
        if (hasPdfId) {
            sqlQuery += " and p.companyPdfTemplate.objectID =:pdfId ";
        } else {
            sqlQuery += " and p.companyPdfTemplate is null ";
        }
        sqlQuery += " order by p.sorder";
        TypedQuery<EdsPdfTemplateTableSettings> query = slaveEntityManager.createQuery(sqlQuery, EdsPdfTemplateTableSettings.class)
                                                                     .setParameter("pdfType", typeEnum)
                                                                     .setParameter("tableType", tableEnum)
                                                                     .setParameter("columnCode", columnCode)
                                                                     .setMaxResults(1);
        if (hasPdfId) {
            query.setParameter("pdfId", pdfId);
        }
        List<EdsPdfTemplateTableSettings> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void deleteByTypeAndPdfId(PdfTemplateTypeEnum typeEnum, Integer pdfId) {
        if (typeEnum == null) {
            return;
        }
        boolean hasPdfId = pdfId != null;
        String sql = "delete from EdsPdfTemplateTableSettings " +
                     "  where pdfType = :pdfType";
        if (hasPdfId) {
            sql += "    and companyPdfTemplate.objectID = :pdfId";
        } else {
            sql += "    and companyPdfTemplate is null";
        }
        Query query = masterEntityManager.createQuery(sql)
                                   .setParameter("pdfType", typeEnum);
        if (hasPdfId) {
            query.setParameter("pdfId", pdfId);
        }
        query.executeUpdate();
    }

    @Override
    public void deleteByPdfId(Integer pdfId) {
        if (pdfId == null) {
            return;
        }
        masterEntityManager.createQuery("delete from EdsPdfTemplateTableSettings " +
                                  "     where companyPdfTemplate.objectID = :pdfId")
                     .setParameter("pdfId", pdfId)
                     .executeUpdate();
    }

    @Override
    public void deleteByTypeAndColumnCode(PdfTemplateTypeEnum typeEnum, String columnCode) {
        if (typeEnum == null || StringUtils.isEmpty(columnCode)) {
            return;
        }
        masterEntityManager.createQuery("delete from EdsPdfTemplateTableSettings " +
                                  "     where isCustomField = true" +
                                  "         and pdfType = :typeEnum" +
                                  "         and columnCode = :columnCode")
                     .setParameter("typeEnum", typeEnum)
                     .setParameter("columnCode", columnCode)
                     .executeUpdate();
    }

    @Override
    public void deleteNotExistByIds(Integer pdfId, List<Integer> columnIds, PdfTemplateTableTypeEnum tableEnum) {
        masterEntityManager.createQuery("delete from EdsPdfTemplateTableSettings ts " +
                                  "     where ts.companyPdfTemplate.objectID =:pdfId " +
                                  "         and ts.objectID not in (:columnIds)" +
                                  "         and ts.pdfTableType = :pdfType")
                     .setParameter("pdfId", pdfId)
                     .setParameter("columnIds", columnIds)
                     .setParameter("pdfType", tableEnum)
                     .executeUpdate();
    }
}
