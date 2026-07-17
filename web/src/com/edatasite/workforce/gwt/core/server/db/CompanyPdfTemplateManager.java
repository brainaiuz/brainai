package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 17:40:19
 * To change this template use File | Settings | File Templates.
 */
public interface CompanyPdfTemplateManager extends Manager<EdsCompanyPdfTemplate> {

    EdsCompanyPdfTemplate getCompanyPdfTemplateByIDOrCode(Integer companyID, String pdfReferenceCode, Integer selectedTemplateID);

    EdsCompanyPdfTemplate getCompanyPdfTemplateByIDOrCode(Integer companyID, String pdfReferenceCode, Integer selectedTemplateID, boolean isBrowserVersion);

    List<EdsCompanyPdfTemplate> getCompanyPDFTemplatesByType(String type, boolean isBrowserVersion);

    List<EdsCompanyPdfTemplate> getClientPDFTemplatesByType(String type);

    List<EdsCompanyPdfTemplate> getCompanyPDFTemplates(ListingFilterParameter fp);

    Integer getCompanyPDFTemplatesCount(ListingFilterParameter fp);

    List<EdsCompanyPdfTemplate> getCompanyPDFTemplatesByType(String type);

    List<EdsCompanyPdfTemplate> getCompanyPDFTemplatesByTypeWithFormId(String type, String formId);

    EdsCompanyPdfTemplate getPdfTemplateByEntityID(Integer websiteId, String entityId, String templateId);

    ArrayList<EdsCompanyPdfTemplate> getPdfTemplateListByEntityGUID(Integer websiteId, String entityGUId);

    EdsCompanyPdfTemplate getDefaultCompanyPdfTemplateByType(String type);

    void removeCustomEntityPdfTemplate(String templateID);

    Integer getSettingsPdfTemplatesCount(ListingFilterParameter fp);

    List<EdsCompanyPdfTemplate> getSettingsPdfTemplates(ListingFilterParameter fp);

    void updateDefaultTemplates(Integer objectID, Integer typeId);

    Integer getCompanyBarcodePDFTemplateId();
}
