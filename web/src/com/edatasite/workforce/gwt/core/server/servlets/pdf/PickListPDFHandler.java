package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 26, 2010
 * Time: 4:04:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PickListPDFHandler extends AbstractITextPostPdfHandler {
    private QuoteService quoteService;

    @Override
    protected String getTableName(Object dataClass) {
        return userManager.getUser().getFirstName() + "'s Bank Accounting List";
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();

        ListResult<PickList> positionList = quoteService.getPickListData(filterParametrs);
        List<PickList> positionItems = positionList.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(PickList.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PickList.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(PickList.SHIP_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.shipDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(PickList.EXPECTED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.expectedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(PickList.TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.total), Element.ALIGN_RIGHT));
        mapColumnHeader.put(PickList.DISCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.discount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(PickList.DUE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.dueDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(PickList.CLIENT, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        for (PickList pickList : positionItems) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (PickList.STATUS.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(pickList.getStatus());
                    cell.add(header.indexOf(PickList.STATUS), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (PickList.SHIP_DATE.equals(header.get(j))) {
                    temp[j] = pickList.getShipDate() != null ? ServerUtils.shortDateFormat(pickList.getShipDate().getNonConvertedDate(), company) : "—";
                    cell.add(header.indexOf(PickList.SHIP_DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (PickList.EXPECTED_DATE.equals(header.get(j))) {
                    temp[j] = pickList.getExpectedDate() != null ? ServerUtils.shortDateFormat(pickList.getExpectedDate().getNonConvertedDate(), company) : "—";
                    cell.add(header.indexOf(PickList.EXPECTED_DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (PickList.TOTAL.equals(header.get(j))) {
                    temp[j] = pickList.getTotal() != null ? priceScaleNumberFormat.format(pickList.getTotal()) : "0.00";
                    cell.add(header.indexOf(PickList.TOTAL), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (PickList.DISCOUNT.equals(header.get(j))) {
                    temp[j] = pickList.getDiscount() != null ? priceScaleNumberFormat.format(pickList.getDiscount()) : "0.00";
                    cell.add(header.indexOf(PickList.DISCOUNT), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (PickList.DUE_DATE.equals(header.get(j))) {
                    temp[j] = pickList.getDueDate() != null ? ServerUtils.shortDateFormat(pickList.getDueDate().getNonConvertedDate(), company) : "—";
                    cell.add(header.indexOf(PickList.DUE_DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (PickList.CLIENT.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(pickList.getClientName());
                    cell.add(header.indexOf(PickList.CLIENT), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;

    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFullName() + "_" + "Pick_Lists_List" + dateFormat(new Date()));
    }

    public QuoteService getQuoteService() {
        return quoteService;
    }

    public void setQuoteService(QuoteService quoteService) {
        this.quoteService = quoteService;
    }
}
