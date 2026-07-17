package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.UnitMeasurementItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 26, 2010
 * Time: 4:04:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementsListPDFHandler extends AbstractITextPostPdfHandler {
    private AccountingService accountingService;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("measurements");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();

        ListResult<UnitMeasurementItem> list = accountingService.getUnitMeasurementsList(filterParametrs);
        List<UnitMeasurementItem> unitMeasurementItems = list.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(UnitMeasurementItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(UnitMeasurementItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(UnitMeasurementItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (UnitMeasurementItem pickList : unitMeasurementItems) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (UnitMeasurementItem.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(pickList.getName());
                    cell.add(header.indexOf(UnitMeasurementItem.NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (UnitMeasurementItem.DESCRIPTION.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(pickList.getDescription());
                    cell.add(header.indexOf(UnitMeasurementItem.DESCRIPTION), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }

        pdfData.setListTable(tableList);
        return pdfData;

    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFullName() + "_" + "Unit_Measurements_List" + dateFormat(new Date()));
    }

    public AccountingService getAccountingService() {
        return accountingService;
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
