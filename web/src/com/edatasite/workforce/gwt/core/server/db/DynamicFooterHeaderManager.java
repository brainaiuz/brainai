package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.pdf.EdsPdfDynamicFooterHeader;

import java.util.List;

public interface DynamicFooterHeaderManager extends Manager<EdsPdfDynamicFooterHeader> {
    EdsPdfDynamicFooterHeader getByKeyAndTemplateSettingId(String key, Integer templateId);

    void updateDynamicSettingsBytemplateId(Integer pdfId);

    List<EdsPdfDynamicFooterHeader> getDefaultFooterHeaderValues();

}
