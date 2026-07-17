package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by Azam on 09/26/2019.
 * Created date: 22:00
 */
public class CustomFormItemListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private CustomFormManager customFormManager;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return super.getDataClass(request);
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        EdsCustomForm customForm = customFormManager.get(filterParameters.getFacetFilter().getTypeId());
        String tableName = customForm != null ? customForm.getName() : "List";
        return tableName;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;

        filterParameters.setAllByFilter(true);
        filterParameters.setForExportOnly(true);
        filterParameters.setStart(0);
        EdsUser user = uploadManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParameters.setLimit(LIMIT_PDF_ROWS);
        }
        filterParameters.setParentID(filterParameters.getFacetFilter().getTypeId());

        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ListResult<FormItems> customFormItems = commonService.getCustomFormItems(filterParameters);
        List<FormItems> itemsList = customFormItems.getList();

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(FormItems.CREATER, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(FormItems.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(FormItems.UPDATER, new CellData(commonLocalizer.localize("modifiedBy"), Element.ALIGN_LEFT));
        columnHeaderMap.put(FormItems.UPDATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(FormItems.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(FormItems.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);

        List<CellData> header = new ArrayList<>();
        header.add(new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number), Element.ALIGN_LEFT));
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (columnHeaderMap.containsKey(columnCode)) {
                header.add(columnHeaderMap.get(columnCode));
            }
        }

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        int count = 1;
        for (FormItems item : itemsList) {
            Map<String, CellData> columnMap = new HashMap<>();
            String counter = String.valueOf(count);
            if (panelTools.getColumnCodeName().contains(FormItems.CREATER)) {
                if (item.isAnonymous()) {
                    columnMap.put(FormItems.CREATER, new CellData(commonLocalizer.localize(PdfLocalizationName.anonymous), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(FormItems.CREATER, new CellData(getResultOrLongDash(item.getCreator()), Element.ALIGN_LEFT));
                }
            }
            if (panelTools.getColumnCodeName().contains(FormItems.CREATED_DATE)) {
                columnMap.put(FormItems.CREATED_DATE, new CellData(item.getCreatedDate() != null ? ServerUtils.shortDateFormat(item.getCreatedDate(), edsCompany) : "—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FormItems.UPDATER)) {
                if (item.isAnonymous()) {
                    columnMap.put(FormItems.UPDATER, new CellData(getResultOrLongDash(commonLocalizer.localize(PdfLocalizationName.anonymous)), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(FormItems.UPDATER, new CellData(getResultOrLongDash(item.getUpdater()), Element.ALIGN_LEFT));
                }
            }
            if (panelTools.getColumnCodeName().contains(FormItems.UPDATED_DATE)) {
                columnMap.put(FormItems.UPDATED_DATE, new CellData(item.getModifiedData() != null ? ServerUtils.longDateFormat(item.getModifiedData(), edsCompany) : "—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FormItems.STATUS)) {
                columnMap.put(FormItems.STATUS, new CellData(getResultOrLongDash(item.getStatus()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(FormItems.APPROVER)) {
                columnMap.put(FormItems.APPROVER, new CellData(getResultOrLongDash(item.getCurrentApproverName()), Element.ALIGN_LEFT));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), item, company);

            List<CellData> column = new ArrayList<>();
            column.add(new CellData(counter, Element.ALIGN_LEFT));
            for (String columnValue : panelTools.getColumnCodeName()) {
                if (columnMap.containsKey(columnValue)) {
                    column.add(columnMap.get(columnValue));
                }
            }
            tableList.addPdfTableRows(column.toArray(new CellData[0]));
            count++;
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);

        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_CustomFormItem_" + dateFormat(new Date()));
    }
}
