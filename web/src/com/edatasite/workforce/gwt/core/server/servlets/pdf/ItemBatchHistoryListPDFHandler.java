package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsItemBatch;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBatchHistoryListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private ItemBatchManager itemBatchManager;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("itemBatchHistoryList");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(1000);
        List<EdsItemBatch> results = itemBatchManager.getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        header.remove(ProductTrackBatchItem.RELATED_TO);
        List<CellData> header2 = new ArrayList<>();
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductTrackBatchItem.NUMBER, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductTrackBatchItem.EXPIRY_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.expirationDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductTrackBatchItem.QTY, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.qty), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductTrackBatchItem.TYPE, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductTrackBatchItem.RELATED, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.related), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductTrackBatchItem.WAREHOUSE, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.warehouse), Element.ALIGN_LEFT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (EdsItemBatch item : results) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (ProductTrackBatchItem.NUMBER.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getSerial() != null ? item.getSerial() : "");
                    cell.add(header.indexOf(ProductTrackBatchItem.NUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductTrackBatchItem.EXPIRY_DATE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(dateFormat(item.getExpiryDate() != null ? item.getExpiryDate() : new Date()));
                    cell.add(header.indexOf(ProductTrackBatchItem.EXPIRY_DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductTrackBatchItem.QTY.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getQty() != null ? item.getQty().toString() : "");
                    cell.add(header.indexOf(ProductTrackBatchItem.QTY), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductTrackBatchItem.TYPE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getBatchType() != null ? item.getBatchType() : "");
                    cell.add(header.indexOf(ProductTrackBatchItem.TYPE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductTrackBatchItem.RELATED.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getEntityType() != null ? item.getEntityType() : "");
                    cell.add(header.indexOf(ProductTrackBatchItem.RELATED), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductTrackBatchItem.WAREHOUSE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getWarehouse() != null ? item.getWarehouse().getName() : "");
                    cell.add(header.indexOf(ProductTrackBatchItem.WAREHOUSE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("ItemBatchHistory_" + user.getFirstName() + "_" + dateFormat(new Date()));
    }
}
