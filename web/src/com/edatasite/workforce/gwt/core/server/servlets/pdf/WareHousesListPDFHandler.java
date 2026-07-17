package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
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
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 8, 2010
 * Time: 8:44:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class WareHousesListPDFHandler extends AbstractITextPostPdfHandler {

    private AccountingService accountingService;
    protected String filename;
    @Override
    protected boolean isListingPDF() {
        return true;
    }
    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("warehousesList");
    }
    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }
    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(1000);
        ListResult<WarehouseItem> list = accountingService.getWarehousesList(filterParametrs);
        List<WarehouseItem> warehouseList = list.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();

        header.remove(WarehouseItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(WarehouseItem.WAREHOUSE_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(WarehouseItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(WarehouseItem.NOTES, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(WarehouseItem.ASSIGNEE, new CellData(commonLocalizer.localize(PdfLocalizationName.assignee), Element.ALIGN_LEFT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (WarehouseItem wh : warehouseList) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {

                if (WarehouseItem.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(wh.getName());
                    cell.add(header.indexOf(WarehouseItem.NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                }

                if (WarehouseItem.WAREHOUSE_CODE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(wh.getObjectID() != null ? wh.getObjectID().toString() : "");
                    cell.add(header.indexOf(WarehouseItem.WAREHOUSE_CODE), new CellData(temp[j], Element.ALIGN_LEFT));
                }

                if (WarehouseItem.NOTES.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(wh.getNotes());
                    cell.add(header.indexOf(WarehouseItem.NOTES), new CellData(temp[j], Element.ALIGN_LEFT));
                }


                if (WarehouseItem.ASSIGNEE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(wh.getSelectedOwners().stream().map(SelectItem::getName).collect(Collectors.joining(", ")));
                    cell.add(header.indexOf(WarehouseItem.ASSIGNEE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }
    @Autowired
    private PropertManager propertManager;
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.warehouse);
    }
    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
    }
    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
