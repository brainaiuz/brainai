package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.StockAdjustmentListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public class StockAdjustmentListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;

    @Override
    protected boolean isListingPDF() {
        return true;
    }
    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize("stockAdjustments");
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
        List<EdsStockAdjustment> list = stockAdjustmentManager.getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();

        header.remove(StockAdjustmentListItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(StockAdjustmentListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockAdjustmentListItem.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockAdjustmentListItem.ADJUSTMENT_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.adjustmentAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockAdjustmentListItem.MEMO, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(StockAdjustmentListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));
        for (EdsStockAdjustment item : list) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (StockAdjustmentListItem.NUMBER.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getNumber());
                    cell.add(header.indexOf(StockAdjustmentListItem.NUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockAdjustmentListItem.DATE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(dateFormat(item.getDate()));
                    cell.add(header.indexOf(StockAdjustmentListItem.DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockAdjustmentListItem.ADJUSTMENT_ACCOUNT.equals(header.get(j))) {
                    EdsStockAdjustment edsStockAdjustment = stockAdjustmentManager.get(item.getObjectID());
                    temp[j] = getResultOrLongDash(edsStockAdjustment.getAccount() != null ? edsStockAdjustment.getAccount().getName() : "");
                    cell.add(header.indexOf(StockAdjustmentListItem.ADJUSTMENT_ACCOUNT), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockAdjustmentListItem.MEMO.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getMemo());
                    cell.add(header.indexOf(StockAdjustmentListItem.MEMO), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (StockAdjustmentListItem.STATUS.equals(header.get(j))) {
                    EdsStockAdjustment edsStockAdjustment = stockAdjustmentManager.get(item.getObjectID());
                    temp[j] = edsStockAdjustment.getOverallStatus() != null ? edsStockAdjustment.getOverallStatus().getCode() : "";
                    cell.add(header.indexOf(StockAdjustmentListItem.STATUS), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("StockAdjustmentList_" + user.getFirstName() + dateFormat(new Date()));
    }
}
