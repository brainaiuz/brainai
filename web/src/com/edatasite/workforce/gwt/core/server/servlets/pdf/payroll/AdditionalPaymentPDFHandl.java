package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdditionalPaymentPDFHandl extends AbstractITextPostPdfHandler {

    @Autowired
    PayrollService payrollService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        Integer calculationScale = getCalculationScale();
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParameters.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<AdditionalPayment> aPayments = payrollService.getAdditionalPaymentList(filterParameters);
        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(AdditionalPayment.PERIOD, new CellData(commonLocalizer.localize(PdfLocalizationName.period), Element.ALIGN_LEFT));
        columnHeaderMap.put(AdditionalPayment.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        columnHeaderMap.put(AdditionalPayment.CATEGORY, new CellData(commonLocalizer.localize(PdfLocalizationName.category), Element.ALIGN_LEFT));
        columnHeaderMap.put(AdditionalPayment.REFERENCE, new CellData(commonLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        columnHeaderMap.put(AdditionalPayment.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(AdditionalPayment.TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.total), Element.ALIGN_LEFT));
        columnHeaderMap.put(AdditionalPayment.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));


        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (aPayments != null) {
            for (AdditionalPayment item : aPayments.getList()) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(AdditionalPayment.PERIOD, new CellData(getResultOrLongDash(ServerUtils.convertToUzbDateFormat(item.getPeriod())), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(AdditionalPayment.PERIOD, new CellData(getResultOrLongDash(item.getPeriod()), Element.ALIGN_LEFT));
                }
                columnMap.put(AdditionalPayment.APPROVER, item.getApprover() != null ? new CellData(getResultOrLongDash(item.getApprover().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                columnMap.put(AdditionalPayment.REFERENCE, item.getReference() != null ? new CellData(getResultOrLongDash(item.getReference()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                columnMap.put(AdditionalPayment.TOTAL, (item != null ? new CellData(item.getTotal().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_LEFT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(AdditionalPayment.STATUS, new CellData(getResultOrLongDash(item.getStatus()), Element.ALIGN_LEFT));
                columnMap.put(AdditionalPayment.CREATOR, new CellData(getResultOrLongDash(item.getCreator().getName()), Element.ALIGN_LEFT));
                columnMap.put(AdditionalPayment.CATEGORY, item.getCategoryType() != null ? new CellData(getResultOrLongDash(item.getCategoryType()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));

                List<CellData> columns = panelTools.getColumnCodeName().stream()
                        .filter(columnCode -> columnMap.containsKey(columnCode))
                        .map(columnCode -> columnMap.get(columnCode))
                        .collect(Collectors.toList());
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
            }

        }
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }


    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("additionals");
    }


    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Additional_Payment_List_" + dateFormat(new Date()));
    }
}
