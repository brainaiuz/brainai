package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateSettings;

/**
 * User: Abror Abdukadirov
 * Date: 12.12.2018 18:39
 */
public interface PdfTemplateSettingsManager extends Manager<EdsPdfTemplateSettings> {

    EdsPdfTemplateSettings getPdfSettings();
}
