package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 26, 2010
 * Time: 4:04:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrandsListPDFHandler extends AbstractITextPostPdfHandler {
    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(DEFAULT_LIMIT);
        EdsUser user = uploadManager.getUser();
        ListResult<BrandItem> list = accountingService.getBrandsList(filterParametrs);
        List<BrandItem> brandListItems = list.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(BrandItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BrandItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(BrandItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(BrandItem.PARENT, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.parentBrand), Element.ALIGN_LEFT));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));
        for (BrandItem brands : brandListItems) {
            String[] temp = new String[header.size()];
            for (int j = 0; j < header.size(); j++) {
                if (BrandItem.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(brands.getName());
                }
                if (BrandItem.DESCRIPTION.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(brands.getDescription());
                }
                if (BrandItem.PARENT.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(brands.getParentBrandName());
                }

            }
            tableList.addPdfTableRows(temp);
        }


        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize(PdfLocalizationName.brandsList);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFullName() + "_" + "Brands_List" + ServerUtils.getDateAsString(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
