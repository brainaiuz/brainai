package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
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

public class StockTransferListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private StockTransferManager stockTransferManager;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {

        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize("stockTransfer");
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
        ArrayList<EdsStockTransfer> list = stockTransferManager.getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();

        header.remove(StockTransferItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(StockTransferItem.TRANSFER_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockTransferItem.TRANSFER_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockTransferItem.TRANSFER_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockTransferItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        for (EdsStockTransfer item : list) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (StockTransferItem.TRANSFER_NUMBER.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getNumber() != null ? item.getNumber() : "N/A");
                    cell.add(header.indexOf(StockTransferItem.TRANSFER_NUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockTransferItem.TRANSFER_NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getTransferName());
                    cell.add(header.indexOf(StockTransferItem.TRANSFER_NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockTransferItem.TRANSFER_DATE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(dateFormat(item.getDate() != null ? item.getDate() : new Date()));
                    cell.add(header.indexOf(StockTransferItem.TRANSFER_DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockTransferItem.STATUS.equals(header.get(j))) {
                    EdsStockTransfer stockTransfer = stockTransferManager.get(item.getObjectID());
                    String statusCode = stockTransfer != null && stockTransfer.getOverallStatus() != null ? stockTransfer.getOverallStatus().getCode() : null;
                    temp[j] = getResultOrLongDash(statusCode);
                    cell.add(header.indexOf(StockTransferItem.STATUS), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("StockTransferList_" + user.getFirstName() + "_" + dateFormat(new Date()));
    }
}
