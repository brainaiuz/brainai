package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateTableSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 14.01.2019 19:36
 */
public interface PdfTemplateTableSettingsManager extends Manager<EdsPdfTemplateTableSettings> {

    List<EdsPdfTemplateTableSettings> getListByTypeAndTableType(Integer pdfId, PdfTemplateTypeEnum typeEnum, PdfTemplateTableTypeEnum tableEnum);

    EdsPdfTemplateTableSettings getItemByTypeAndTableTypeAndColumnCode(Integer pdfId, PdfTemplateTypeEnum typeEnum, PdfTemplateTableTypeEnum tableEnum, String columnCode);

    void deleteByTypeAndPdfId(PdfTemplateTypeEnum typeEnum, Integer pdfId);

    void deleteByPdfId(Integer pdfId);

    void deleteByTypeAndColumnCode(PdfTemplateTypeEnum typeEnum, String columnCode);

    void deleteNotExistByIds(Integer pdfId, List<Integer> columnIds, PdfTemplateTableTypeEnum tableEnum);
}
