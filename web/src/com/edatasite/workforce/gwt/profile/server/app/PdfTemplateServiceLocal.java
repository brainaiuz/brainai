package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableSettingsItem;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 12.02.2019 16:33
 */
public interface PdfTemplateServiceLocal {

    List<PdfTemplateTableSettingsItem> getPdfTableActiveColumns(Integer pdfId, String pdfType);

    void getCustomFields(CustomFieldSection section, LinkedHashMap<String, PdfTemplateTableSettingsItem> columnMap);
}
