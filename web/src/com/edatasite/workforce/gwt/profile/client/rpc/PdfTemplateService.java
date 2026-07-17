package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 12.02.2019 16:22
 */
public interface PdfTemplateService extends RemoteService {

    ListResult<SettingsPdfTemplateListItem> getSettingsPdfTemplateList(ListingFilterParameter fp);

    SettingsPdfTemplateItem getSettingsPdfTemplateFooterAndHeader(Integer objectId, String pdfType);

    SettingsPdfTemplateItem getSettingsPdfTemplateContentLayout();

    ArrayList<PdfTemplateTableSettingsItem> getPdfTableColumns(Integer pdfId, String pdfType);

    SettingsPdfTemplateItem getSettingsPdfTemplateProperties(Integer pdfId, String pdfType);

    Integer savePdfPropertiesSettings(SettingsPdfTemplateItem item);

    Integer savePdfFooterHeaderSettings(SettingsPdfTemplateItem item);

    Integer savePdfContentSettings(SettingsPdfTemplateItem item);

    SettingsPdfTemplateGenerateItem generateSettingsPdf(SettingsPdfTemplateItem item);

    void resetPdfSettings(Integer tabStep, String pdfType, Integer pdfId);

    void deleteSettingsPdfTemplate(Integer objectId);

    ArrayList<SelectItem> getClientPdfTemplatesByType(String type);


    class App {
        public static PdfTemplateServiceAsync get() {
            ServiceDefTarget target = GWT.create(PdfTemplateService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/pdfTemplate");
            return (PdfTemplateServiceAsync) target;
        }
    }
}
