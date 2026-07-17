package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.io.IOException;
import java.util.*;

public class BenefitRequestListPDFHandler extends AbstractITextPostPdfHandler{


    @Autowired
    protected WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    private BenefitRequestManager benefitRequestManager;

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : "benefitRequestList";
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("benefit_request_list_"+dateFormat(new Date()));
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(1000);
        ListResult<BenefitRequestItem> benefitRequestList = benefitRequestManager.getBenefitRequestList(filterParametrs);
        ArrayList<BenefitRequestItem> items = benefitRequestList.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        header.remove("action");
        List<CellData> header2 = new ArrayList<>();
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BenefitRequestItem.REQUESTER, new CellData(commonLocalizer.localize(PdfLocalizationName.requester), Element.ALIGN_LEFT));
        mapColumnHeader.put(BenefitRequestItem.BENEFIT_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(BenefitRequestItem.REQUESTED_QUANTITY, new CellData(commonLocalizer.localize(PdfLocalizationName.qty), Element.ALIGN_LEFT));
        mapColumnHeader.put(BenefitRequestItem.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(BenefitRequestItem.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        mapColumnHeader.put(BenefitRequestItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (BenefitRequestItem item: items) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int i = 0; i < header.size(); i++) {
                if (BenefitRequestItem.REQUESTER.equals(header.get(i))) {
                    temp[i] = getResultOrLongDash(item.getRequester() != null ? item.getRequester() : "");
                    cell.add(header.indexOf(BenefitRequestItem.REQUESTER), new CellData(temp[i], Element.ALIGN_LEFT));
                }else if (BenefitRequestItem.BENEFIT_TYPE.equals(header.get(i))) {
                    temp[i] = getResultOrLongDash(item.getBenefitName() != null ? item.getBenefitName() : "");
                    cell.add(header.indexOf(BenefitRequestItem.BENEFIT_TYPE), new CellData(temp[i], Element.ALIGN_LEFT));
                }else if (BenefitRequestItem.REQUESTED_QUANTITY.equals(header.get(i))) {
                    temp[i] = getResultOrLongDash(String.valueOf(item.getRequestedQuantity()));
                    cell.add(header.indexOf(BenefitRequestItem.REQUESTED_QUANTITY), new CellData(temp[i], Element.ALIGN_LEFT));
                }else if (BenefitRequestItem.DATE.equals(header.get(i))) {
                    String date = dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : new Date());
                    temp[i] = getResultOrLongDash(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(date) : date);
                    cell.add(header.indexOf(BenefitRequestItem.DATE), new CellData(temp[i], Element.ALIGN_LEFT));
                }else if (BenefitRequestItem.APPROVER.equals(header.get(i))) {
                    temp[i] = getResultOrLongDash(item.getApprover() != null ? item.getApprover() : "");
                    cell.add(header.indexOf(BenefitRequestItem.APPROVER), new CellData(temp[i], Element.ALIGN_LEFT));
                }else if (BenefitRequestItem.STATUS.equals(header.get(i))) {
                    temp[i] = getResultOrLongDash(item.getStatus() != null ? item.getStatus().getName() : "");
                    cell.add(header.indexOf(BenefitRequestItem.STATUS), new CellData(temp[i], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }
}
