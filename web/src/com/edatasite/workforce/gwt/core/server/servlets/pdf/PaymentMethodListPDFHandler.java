package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Omonullo Abdullaev on 12/10/2016.
 */
public class PaymentMethodListPDFHandler extends AbstractITextPostPdfHandler {
    @Autowired
    AccountingService accountingService;
    EdsUser user;

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("paymentMethodList");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        user = uploadManager.getUser();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        filterParameters.setLimit(filterParameters.getLimit() < (LIMIT_PDF_ROWS) ? filterParameters.getLimit() : LIMIT_PDF_ROWS);
        ListResult<PaymentMethodItem> paymentMethods = accountingService.getAllPaymentMethods(filterParameters);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(PaymentMethodItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        columnHeaderMap.put(PaymentMethodItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        columnHeaderMap.put(PaymentMethodItem.CODE, new CellData(accountingLocalizer.localize(PdfLocalizationName.code), Element.ALIGN_LEFT));
        columnHeaderMap.put(PaymentMethodItem.WEIGTH, new CellData(commonLocalizer.localize(PdfLocalizationName.weight), Element.ALIGN_LEFT));

        List<CellData> header = new ArrayList<>();
        for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
            if (columnHeaderMap.containsKey(panelTools.getColumnCodeName().get(i))) {
                header.add(columnHeaderMap.get(panelTools.getColumnCodeName().get(i)));
            }
        }
        ITextTableList iTextTableList = new ITextTableList(header.size());
        iTextTableList.addPdfTableHeader(header.toArray(new CellData[0]));

        if (paymentMethods != null) {
            for (PaymentMethodItem item : paymentMethods.getList()) {
                Map<String, String> columnMap = new HashMap<>();

                columnMap.put(PaymentMethodItem.NAME, item.getName() != null ? item.getName() : "—");
                columnMap.put(PaymentMethodItem.DESCRIPTION, item.getDescription() != null ? item.getDescription() : "—");
                columnMap.put(PaymentMethodItem.CODE, item.getCode() != null ? item.getCode() : "—");
                columnMap.put(PaymentMethodItem.WEIGTH, item.getWeigth() != null ? String.valueOf(item.getWeigth()) : "—");
                List<String> column = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (columnMap.containsKey(panelTools.getColumnCodeName().get(i))) {
                        column.add(columnMap.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                String[] colArray = new String[column.size()];
                column.toArray(colArray);
                iTextTableList.addPdfTableRows(colArray);
            }
        }
        pdfData.setListTable(iTextTableList);
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Payment_Method_List" + dateFormat(new Date()));
    }
}
