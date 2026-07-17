package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
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

/**
 * Created by Omonullo Abdullaev on 10/28/2016.
 */
public class GroupPayrunListPDFHandler extends AbstractITextPostPdfHandler {

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

        ListResult<GroupPayrunData> gPayruns = payrollService.getPayslipTableList(filterParameters);
        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(GroupPayrunData.PERIOD, new CellData(commonLocalizer.localize(PdfLocalizationName.period), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.PREPARER, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.BATCH, new CellData(commonLocalizer.localize(PdfLocalizationName.payrollGroup), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.TOTAL_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.totalAmount), Element.ALIGN_RIGHT));
        columnHeaderMap.put(GroupPayrunData.TOTAL_IN_BASE, new CellData(commonLocalizer.localize(PdfLocalizationName.totalInBase), Element.ALIGN_RIGHT));
        columnHeaderMap.put(GroupPayrunData.CURRENCY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.PAYMENT_METHOD, new CellData(commonLocalizer.localize(PdfLocalizationName.paymentMethod), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.PROCESS_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.processDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.BASIC_SALARY, new CellData(commonLocalizer.localize(PdfLocalizationName.basicSalary), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.ALLOWANCE, new CellData(commonLocalizer.localize(PdfLocalizationName.allowance), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.PENSION, new CellData(commonLocalizer.localize(PdfLocalizationName.pension), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.DEDUCTION, new CellData(commonLocalizer.localize(PdfLocalizationName.deduction), Element.ALIGN_LEFT));
        columnHeaderMap.put(GroupPayrunData.EXPENSE, new CellData(commonLocalizer.localize(PdfLocalizationName.expense), Element.ALIGN_LEFT));


        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (gPayruns != null) {
            for (GroupPayrunData item : gPayruns.getList()) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(GroupPayrunData.PERIOD, new CellData(getResultOrLongDash(ServerUtils.convertToUzbDateFormat(item.getMonth())), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(GroupPayrunData.PERIOD, new CellData(getResultOrLongDash(item.getMonth()), Element.ALIGN_LEFT));
                }
//                columnMap.put(GroupPayrunData.PERIOD, new CellData(getResultOrLongDash(item.getMonth()), Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.APPROVER, item.getApprover() != null ? new CellData(getResultOrLongDash(item.getApprover().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.PREPARER, item.getCreator() != null ? new CellData(getResultOrLongDash(item.getCreator().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.STATUS, new CellData(getResultOrLongDash(item.getStatus()), Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.BATCH, item.getPayrollBatchItem() != null && item.getPayrollBatchItem().getName() != null ? new CellData(item.getPayrollBatchItem().getName(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.TOTAL_AMOUNT, (item.getTotalAmount() != null ? new CellData(item.getTotalAmount().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.TOTAL_IN_BASE, (item.getTotalInBase() != null ? new CellData(item.getTotalInBase().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.CURRENCY_NAME, new CellData(getResultOrLongDash(item.getCurrencyName()), Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.PAYMENT_METHOD, new CellData(getResultOrLongDash(item.getPayMethodName()), Element.ALIGN_LEFT));
                columnMap.put(GroupPayrunData.EXPENSE, (item.getExpense() != null ? new CellData(item.getExpense().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.DEDUCTION, (item.getDeduction() != null ? new CellData(item.getDeduction().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.ALLOWANCE, (item.getAllowance() != null ? new CellData(item.getAllowance().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.BASIC_SALARY, (item.getBasicSalary() != null ? new CellData(item.getBasicSalary().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.PENSION, (item.getPension() != null ? new CellData(item.getPension().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT)));
                columnMap.put(GroupPayrunData.PROCESS_DATE, item.getProcessDate() != null ? new CellData(ServerUtils.shortDateFormat(item.getProcessDate().getNonConvertedDate(), company), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));

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
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("groupPayruns");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Group_Payrun_List_" + dateFormat(new Date()));
    }
}
