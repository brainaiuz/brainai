package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateSettings;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 12.12.2018 18:41
 */
@Repository("pdfTemplateSettingsManager")
public class PdfTemplateSettingsManagerImpl extends BaseManager<EdsPdfTemplateSettings> implements PdfTemplateSettingsManager {

    public PdfTemplateSettingsManagerImpl() {
        super(EdsPdfTemplateSettings.class);
    }

    @Override
    public EdsPdfTemplateSettings getPdfSettings() {
        String sql = "select ts from EdsPdfTemplateSettings ts";

        List<EdsPdfTemplateSettings> list = slaveEntityManager.createQuery(sql, EdsPdfTemplateSettings.class)
                                                         .setMaxResults(1)
                                                         .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
