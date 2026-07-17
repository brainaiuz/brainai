package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 21/02/17
 * Time: 12:21 AM
 */
public class SinglePayrunListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private PayrollService payrollService;
    @Autowired
    protected PropertManager propertManager;

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
        if (companySettings != null && companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParameters.setLimit(LIMIT_PDF_ROWS);
        }

        Integer calculationScale = getCalculationScale();
        ListResult<SinglePayrunItem> singlePayrunList = payrollService.getSinglePayrunList(filterParameters);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        HashMap<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(SinglePayrunItem.EMPLOYEE_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.employeeCode), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.PROJECTS, new CellData(commonLocalizer.localize(PdfLocalizationName.projects), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.total), Element.ALIGN_RIGHT));
        columnHeaderMap.put(SinglePayrunItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.PREPARER, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.PERIOD, new CellData(commonLocalizer.localize(PdfLocalizationName.period), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.DRIVER_ID, new CellData(commonLocalizer.localize("driverID"), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.PROCESS_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.processDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(SinglePayrunItem.PAYMENT_METHOD, new CellData(commonLocalizer.localize(PdfLocalizationName.paymentMethod), Element.ALIGN_LEFT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);

        ArrayList<CellData> header = new ArrayList<>();
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (columnHeaderMap.containsKey(columnCode)) {
                header.add(columnHeaderMap.get(columnCode));
            }
        }

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[0]));

        for (SinglePayrunItem item : singlePayrunList.getList()) {
            HashMap<String, CellData> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.EMPLOYEE_CODE)) {
                columnMap.put(SinglePayrunItem.EMPLOYEE_CODE, new CellData(escapeHtml(item.getEmployeeCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.EMPLOYEE)) {
                columnMap.put(SinglePayrunItem.EMPLOYEE, new CellData(escapeHtml(item.getEmployee()), Element.ALIGN_LEFT));
            }

            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PROJECTS)) {
                StringBuilder projectNames = new StringBuilder("");
                if (!item.getProjects().isEmpty()) {
                    for (SelectItem project : item.getProjects()) {
                        if (projectNames.toString().isEmpty()) {
                            projectNames.append(project.getName());
                        } else {
                            projectNames.append(",").append(" ").append(project.getName());
                        }
                    }
                }
                columnMap.put(SinglePayrunItem.PROJECTS, new CellData(projectNames.toString(), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PERIOD)) {
                StringBuilder period = new StringBuilder("");
                period.append(item.getMonth() != null ? item.getMonth() : "")
                        .append(period.length() > 0 && item.getYear() != null ? "," : "")
                        .append(item.getYear() != null ? item.getYear() : "");
//                columnMap.put(SinglePayrunItem.PERIOD, new CellData(period.toString(), Element.ALIGN_LEFT));
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(SinglePayrunItem.PERIOD, new CellData(ServerUtils.convertToUzbDateFormat(period.toString()), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(SinglePayrunItem.PERIOD, new CellData(period.toString(), Element.ALIGN_LEFT));
                }
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.APPROVER)) {
                columnMap.put(SinglePayrunItem.APPROVER, item.getApprover() != null ? new CellData(getResultOrLongDash(item.getApprover().getName())) : new CellData("—"));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PREPARER)) {
                columnMap.put(SinglePayrunItem.PREPARER, item.getCreator() != null ? new CellData(getResultOrLongDash(item.getCreator().getName())) : new CellData("—"));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.TOTAL)) {
                columnMap.put(SinglePayrunItem.TOTAL, new CellData(item.getTotal().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString(), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.STATUS)) {
                columnMap.put(SinglePayrunItem.STATUS, new CellData(getResultOrLongDash(item.getStatus()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.CURRENCY)) {
                columnMap.put(SinglePayrunItem.CURRENCY, item.getCurrency() != null ? new CellData(getResultOrLongDash(item.getCurrency().getName()), Element.ALIGN_LEFT) : new CellData("—"));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.DRIVER_ID)) {
                columnMap.put(SinglePayrunItem.DRIVER_ID, item.getDriverID() != null ? new CellData(item.getDriverID().toString(), Element.ALIGN_LEFT) : new CellData("—"));
            }

            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PROCESS_DATE)) {
                columnMap.put(SinglePayrunItem.PROCESS_DATE, item.getProcessDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getProcessDate().getNonConvertedDate(), user)), Element.ALIGN_LEFT) : new CellData("—"));
            }
            if (panelTools.getColumnCodeName().contains(SinglePayrunItem.PAYMENT_METHOD)) {
                columnMap.put(SinglePayrunItem.PAYMENT_METHOD, new CellData(getResultOrLongDash(item.getPayMethodName()), Element.ALIGN_LEFT));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), item, user.getCompany());

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
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + "SinglePayruns" + "_" + dateFormat(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("payslips");
    }
}
