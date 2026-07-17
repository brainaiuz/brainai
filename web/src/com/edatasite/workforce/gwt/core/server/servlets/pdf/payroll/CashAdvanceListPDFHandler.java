package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 21/02/17
 * Time: 12:21 AM
 */
public class CashAdvanceListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private PayrollService payrollService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        if (filterParameters == null) {
            filterParameters = new ListingFilterParameter();
        }
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        filterParameters.setLimit(LIMIT_PDF_ROWS);
        if (companySettings != null && StringUtils.isNotBlank(companySettings.getPdfLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        }

        Integer calculationScale = getCalculationScale();
        ListResult<CashAdvanceItem> cashAdvanceList = payrollService.getCashAdvanceList(filterParameters);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        HashMap<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(CashAdvanceItem.EMPLOYEE_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.employeeCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(CashAdvanceItem.EMPLOYEE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        columnHeaderMap.put(CashAdvanceItem.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        columnHeaderMap.put(CashAdvanceItem.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        columnHeaderMap.put(CashAdvanceItem.AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        columnHeaderMap.put(CashAdvanceItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(CashAdvanceItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(CashAdvanceItem.REMAINING_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.dueAmount), Element.ALIGN_RIGHT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);

        ArrayList<CellData> header = new ArrayList<>();
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (columnHeaderMap.containsKey(columnCode)) {
                header.add(columnHeaderMap.get(columnCode));
            }
        }

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[0]));

        for (CashAdvanceItem item : cashAdvanceList.getList()) {
            HashMap<String, CellData> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.EMPLOYEE_CODE)) {
                columnMap.put(CashAdvanceItem.EMPLOYEE_CODE, new CellData(getResultOrLongDash(item.getEmployeeCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.EMPLOYEE_NAME)) {
                columnMap.put(CashAdvanceItem.EMPLOYEE_NAME, new CellData(getResultOrLongDash(item.getEmployeeName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.NUMBER)) {
                columnMap.put(CashAdvanceItem.NUMBER, new CellData(item.getNumber() != null ? getResultOrLongDash(item.getNumber()) : "—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.DATE)) {
                columnMap.put(CashAdvanceItem.DATE, item.getDate() != null ? new CellData(ServerUtils.shortDateFormat(item.getDate().getNonConvertedDate(), user)) : new CellData("—"));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.APPROVER)) {
                columnMap.put(CashAdvanceItem.APPROVER, new CellData(item.getApprover() != null ? getResultOrLongDash(item.getApprover().getName()) : "—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.AMOUNT)) {
                columnMap.put(CashAdvanceItem.AMOUNT, new CellData(getMoneyFormat(item.getTotalAmount(), calculationScale), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.REMAINING_AMOUNT)) {
                columnMap.put(CashAdvanceItem.REMAINING_AMOUNT, item.getRemainingAmount() != null ? new CellData(getMoneyFormat(item.getRemainingAmount(), calculationScale), Element.ALIGN_RIGHT) : new CellData("—"));
            }
            if (panelTools.getColumnCodeName().contains(CashAdvanceItem.STATUS)) {
                columnMap.put(CashAdvanceItem.STATUS, new CellData(item.getStatus() != null ? getResultOrLongDash(item.getStatus().getName()) : "—", Element.ALIGN_LEFT));
            }

            List<CellData> column = new ArrayList<>();
            for (String columnCode : panelTools.getColumnCodeName()) {
                if (columnMap.containsKey(columnCode)) {
                    column.add(columnMap.get(columnCode));
                }
            }
            tableList.addPdfTableRows(column.toArray(new CellData[0]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + "CashAdvance" + "_" + dateFormat(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize("cashAdvance");
    }
}
