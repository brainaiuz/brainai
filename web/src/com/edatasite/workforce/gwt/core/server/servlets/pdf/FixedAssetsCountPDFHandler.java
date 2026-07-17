package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 27.03.12
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetsCountPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler {

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        FixedAssetItem items[] = ((FixedAssetItem[]) dataClass);
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);

        ITextTableList table = new ITextTableList(4);
        table.addPdfTableHeader(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.assetCode),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.assetName),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.addCategory),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.assetStatus));

        pdfData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.fixedAssetsCount));
        pdfData.setListTable(table);

        for (FixedAssetItem fixedAssetItem : items) {
            CellData code = new CellData(fixedAssetItem.getCode());
            CellData name = new CellData(fixedAssetItem.getName());
            CellData status = new CellData(fixedAssetItem.getExistingCodeStatus());
            CellData category = new CellData(fixedAssetItem.getAccount().getName());
            table.addPdfTableRows(code, name, category, status);
        }

        return pdfData;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new FixedAssetList();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
