package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 12.02.2019 16:22
 */
public interface PdfTemplateServiceAsync {

    void getSettingsPdfTemplateList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<SettingsPdfTemplateListItem>> callback);

    void getSettingsPdfTemplateFooterAndHeader(Integer objectId, String pdfType, AsyncCallback<SettingsPdfTemplateItem> callback);

    void getSettingsPdfTemplateContentLayout(AsyncCallback<SettingsPdfTemplateItem> callback);

    void getPdfTableColumns(Integer pdfId, String pdfType, AsyncCallback<ArrayList<PdfTemplateTableSettingsItem>> callback);

    void getSettingsPdfTemplateProperties(Integer pdfId, String pdfType, AsyncCallback<SettingsPdfTemplateItem> callback);

    void savePdfPropertiesSettings(SettingsPdfTemplateItem item, AsyncCallback<Integer> asyncCallback);

    void savePdfFooterHeaderSettings(SettingsPdfTemplateItem item, AsyncCallback<Integer> asyncCallback);

    void savePdfContentSettings(SettingsPdfTemplateItem item, AsyncCallback<Integer> callback);

    void generateSettingsPdf(SettingsPdfTemplateItem item, AsyncCallback<SettingsPdfTemplateGenerateItem> asyncCallback);

    void resetPdfSettings(Integer tabStep, String pdfType, Integer pdfId, AsyncCallback<Void> asyncCallback);

    void deleteSettingsPdfTemplate(Integer objectId, AsyncCallback<Void> callback);

    void getClientPdfTemplatesByType(String type, AsyncCallback<ArrayList<SelectItem>> callback);
}
