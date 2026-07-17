package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 8/19/15 7:18 PM
 */
public class MonthlyTimesheetViewPDFHandler extends AbstractITextPostPdfHandler {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final Logger log = LoggerFactory.getLogger(MonthlyTimesheetViewPDFHandler.class);

    @Autowired
    private TimesheetService timesheetService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Date startDate = null;
        try {
            startDate = dateFormat.parse(fp.getStartDateNC());
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("Date parse exception while genenating Monthly Timesheet List PDF");
        }
        List<MonthlyTimesheetItem> timesheetItems = timesheetService.getMonthlyTimesheetData(new DateNonConvertable(startDate), fp.getProjectId(), fp.getEmployeeId(), fp.isShowYTD());

        String[] headers = new String[7];
        headers[0] = commonLocalizer.localize(PdfLocalizationName.employeeCode);
        headers[1] = commonLocalizer.localize(PdfLocalizationName.employee);
        headers[2] = commonLocalizer.localize(PdfLocalizationName.workedHours);
        headers[3] = commonLocalizer.localize(PdfLocalizationName.totalWorkedDays);
        headers[4] = commonLocalizer.localize(PdfLocalizationName.overtimeHours);
        headers[5] = commonLocalizer.localize(PdfLocalizationName.weekendOvertimeHours);
        headers[6] = commonLocalizer.localize(PdfLocalizationName.holidayOvertimeHours);

        ITextTableList tableList = new ITextTableList(headers.length);
        tableList.addPdfTableHeader(headers);

        for (MonthlyTimesheetItem item : timesheetItems) {
            String[] columns = new String[7];
            Integer indexOffString = item.getEmployeeName().indexOf(" - ");
            String employeeCode = "";
            String employeeName = item.getEmployeeName();
            if (indexOffString > 0 && item.getEmployeeName().length() > 0) {
                employeeCode = item.getEmployeeName().substring(0, indexOffString);
                employeeName = "";
                if (indexOffString + 3 < item.getEmployeeName().length() - 1) {
                    employeeName = item.getEmployeeName().substring(indexOffString + 3, item.getEmployeeName().length() - 1);
                }
            }
            columns[0] = employeeCode;
            columns[1] = employeeName;
            columns[2] = item.getWorkedHours() != null ? item.getWorkedHours() + "" : "0";
            columns[3] = item.getTotalWorkedDays() != null ? item.getTotalWorkedDays() + "" : "0";
            columns[4] = item.getOvertimeHours() != null ? item.getOvertimeHours() + "" : "0";
            columns[5] = item.getWeekendOvertimeHours() != null ? item.getWeekendOvertimeHours() + "" : "0";
            columns[6] = item.getHolidayOvertimeHours() != null ? item.getHolidayOvertimeHours() + "" : "0";
            tableList.addPdfTableRows(columns);
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
        EdsProperty property = propertManager.findByCode(Constants.TIMESHEET);
        return property != null ? pdfWfmMessageSource.localize("monthly") + " " + property.getPlural() : pdfWfmMessageSource.localize("monthlyTimesheet");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getName() + "_" + user.getLastName() + dateFormat(new Date()));
    }

}
