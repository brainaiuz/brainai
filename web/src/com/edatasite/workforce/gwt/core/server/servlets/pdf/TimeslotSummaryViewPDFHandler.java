package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.ExceptionalTimeSlotItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfHeaderPosition;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 2/14/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeslotSummaryViewPDFHandler extends AbstractITextPostPdfHandler {

    private AvailabilityService availabilityService;

    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        Rectangle a4 = PageSize.A4;
        if (((RequestObject) dataClass).getIS_LANDSCAPE()) {
            a4 = a4.rotate();
        }
        return new Document(a4, 20, 20, 120, 50);
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        return requestObject.getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        ITextSummaryView pdfData = new ITextSummaryView();
        pdf.setSummaryView(pdfData);
        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);

        ITextTableList tableList = new ITextTableList(2);
        tableList.setHeaderPosition(ITextPdfHeaderPosition.FIRST_VERTICAL_COLUMN);
        tableList.setTableAlignment(Element.ALIGN_LEFT);


        EdsUser user = uploadManager.getUser();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer timeslotId = requestObject.getObjectID();
        TimeslotItem item = availabilityService.getTimeslot(timeslotId);

        tableList.setName(availabilityLocalizer.localizeWithParam(PdfLocalizationName.timeslotSummary, user.getFullName()));
        tableList.addPdfTableHeader(
                availabilityLocalizer.localize(PdfLocalizationName.name),
                availabilityLocalizer.localize(PdfLocalizationName.description),
                "Effective date");

        String timeslotName = item.getName();
        String description = item.getDescription();
        String date = dateFormat(item.getEffectiveDate());

        tableList.addPdfTableRows(timeslotName);
        tableList.addPdfTableRows(description);
        tableList.addPdfTableRows(date);
        tableList.addTableWidthPercentage(25f, 75f);
        tableList.setTotalWidth(250);

        //time entry table
        ITextTableList timeList = new ITextTableList(7);
        timeList.setTotalWidth(450);
        if (item.getWeekStart() != null) {
            if (item.getWeekStart().equals(1)) {
                timeList.setName(availabilityLocalizer.localize(PdfLocalizationName.timeEntryForEachSunday));
            } else if (item.getWeekStart().equals(2)) {
                timeList.setName(availabilityLocalizer.localize(PdfLocalizationName.timeEntryForEachMonday));
            } else if (item.getWeekStart().equals(7)) {
                timeList.setName(availabilityLocalizer.localize(PdfLocalizationName.timeEntryForEachSaturday));
            } else {
                timeList.setName("Time entry for each of the week");
            }
        }


        CellData cellData0 = new CellData("");
        CellData cellData = new CellData(availabilityLocalizer.localizeWithParam(PdfLocalizationName.workTime, user.getFullName()));
        CellData cellData1 = new CellData(availabilityLocalizer.localizeWithParam(PdfLocalizationName.lunchTime, user.getFullName()));
        CellData cellData2 = new CellData(availabilityLocalizer.localizeWithParam(PdfLocalizationName.coffeeBreak, user.getFullName()));
        cellData.setColspan(2);
        cellData1.setColspan(2);
        cellData2.setColspan(2);
        timeList.addPdfTableHeader(cellData0, cellData, cellData1, cellData2);

        CellData[] cellDatas = {
                new CellData(getTimeSlot(item.getMonday()[0] / 60) + ":" + getTimeSlot(item.getMonday()[0] % 60)),
                new CellData(getTimeSlot(item.getMonday()[1] / 60) + ":" + getTimeSlot(item.getMonday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchMo()[0] / 60) + ":" + getTimeSlot(item.getLunchMo()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchMo()[1] / 60) + ":" + getTimeSlot(item.getLunchMo()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[1] % 60)),
                new CellData(getTimeSlot(item.getTuesday()[0] / 60) + ":" + getTimeSlot(item.getTuesday()[0] % 60)),
                new CellData(getTimeSlot(item.getTuesday()[1] / 60) + ":" + getTimeSlot(item.getTuesday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchTu()[0] / 60) + ":" + getTimeSlot(item.getLunchTu()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchTu()[1] / 60) + ":" + getTimeSlot(item.getLunchTu()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[1] % 60)),
                new CellData(getTimeSlot(item.getWednesday()[0] / 60) + ":" + getTimeSlot(item.getWednesday()[0] % 60)),
                new CellData(getTimeSlot(item.getWednesday()[1] / 60) + ":" + getTimeSlot(item.getWednesday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchWe()[0] / 60) + ":" + getTimeSlot(item.getLunchWe()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchWe()[1] / 60) + ":" + getTimeSlot(item.getLunchWe()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[1] % 60)),
                new CellData(getTimeSlot(item.getThursday()[0] / 60) + ":" + getTimeSlot(item.getThursday()[0] % 60)),
                new CellData(getTimeSlot(item.getThursday()[1] / 60) + ":" + getTimeSlot(item.getThursday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchTh()[0] / 60) + ":" + getTimeSlot(item.getLunchTh()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchTh()[1] / 60) + ":" + getTimeSlot(item.getLunchTh()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[1] % 60)),
                new CellData(getTimeSlot(item.getFriday()[0] / 60) + ":" + getTimeSlot(item.getFriday()[0] % 60)),
                new CellData(getTimeSlot(item.getFriday()[1] / 60) + ":" + getTimeSlot(item.getFriday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchFr()[0] / 60) + ":" + getTimeSlot(item.getLunchFr()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchFr()[1] / 60) + ":" + getTimeSlot(item.getLunchFr()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[1] % 60)),
                new CellData(getTimeSlot(item.getSaturday()[0] / 60) + ":" + getTimeSlot(item.getSaturday()[0] % 60)),
                new CellData(getTimeSlot(item.getSaturday()[1] / 60) + ":" + getTimeSlot(item.getSaturday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchSa()[0] / 60) + ":" + getTimeSlot(item.getLunchSa()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchSa()[1] / 60) + ":" + getTimeSlot(item.getLunchSa()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[1] % 60)),
                new CellData(getTimeSlot(item.getSunday()[0] / 60) + ":" + getTimeSlot(item.getSunday()[0] % 60)),
                new CellData(getTimeSlot(item.getSunday()[1] / 60) + ":" + getTimeSlot(item.getSunday()[1] % 60)),
                new CellData(getTimeSlot(item.getLunchSu()[0] / 60) + ":" + getTimeSlot(item.getLunchSu()[0] % 60)),
                new CellData(getTimeSlot(item.getLunchSu()[1] / 60) + ":" + getTimeSlot(item.getLunchSu()[1] % 60)),
                new CellData(getTimeSlot(item.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[0] % 60)),
                new CellData(getTimeSlot(item.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[1] % 60))
        };
        for (CellData cell : cellDatas) {
            cell.setAlignment(Element.ALIGN_CENTER);
        }

        if (item.getWeekStart().equals(2)) {
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.monday)), cellDatas[0], cellDatas[1], cellDatas[2], cellDatas[3], cellDatas[4], cellDatas[5]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.tuesday)), cellDatas[6], cellDatas[7], cellDatas[8], cellDatas[9], cellDatas[10], cellDatas[11]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.wednesday)), cellDatas[12], cellDatas[13], cellDatas[14], cellDatas[15], cellDatas[16], cellDatas[17]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.thursday)), cellDatas[18], cellDatas[19], cellDatas[20], cellDatas[21], cellDatas[22], cellDatas[23]);
            timeList.addPdfTableRows(new CellData(commonLocalizer.localize(PdfLocalizationName.friday)), cellDatas[24], cellDatas[25], cellDatas[26], cellDatas[27], cellDatas[28], cellDatas[29]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.saturday)), cellDatas[30], cellDatas[31], cellDatas[32], cellDatas[33], cellDatas[34], cellDatas[35]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.sunday)), cellDatas[36], cellDatas[37], cellDatas[38], cellDatas[39], cellDatas[40], cellDatas[41]);
        } else if (item.getWeekStart().equals(1)) {
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.sunday)), cellDatas[36], cellDatas[37], cellDatas[38], cellDatas[39], cellDatas[40], cellDatas[41]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.monday)), cellDatas[0], cellDatas[1], cellDatas[2], cellDatas[3], cellDatas[4], cellDatas[5]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.tuesday)), cellDatas[6], cellDatas[7], cellDatas[8], cellDatas[9], cellDatas[10], cellDatas[11]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.wednesday)), cellDatas[12], cellDatas[13], cellDatas[14], cellDatas[15], cellDatas[16], cellDatas[17]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.thursday)), cellDatas[18], cellDatas[19], cellDatas[20], cellDatas[21], cellDatas[22], cellDatas[23]);
            timeList.addPdfTableRows(new CellData(commonLocalizer.localize(PdfLocalizationName.friday)), cellDatas[24], cellDatas[25], cellDatas[26], cellDatas[27], cellDatas[28], cellDatas[29]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.saturday)), cellDatas[30], cellDatas[31], cellDatas[32], cellDatas[33], cellDatas[34], cellDatas[35]);
        } else if (item.getWeekStart().equals(7)) {
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.saturday)), cellDatas[30], cellDatas[31], cellDatas[32], cellDatas[33], cellDatas[34], cellDatas[35]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.sunday)), cellDatas[36], cellDatas[37], cellDatas[38], cellDatas[39], cellDatas[40], cellDatas[41]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.monday)), cellDatas[0], cellDatas[1], cellDatas[2], cellDatas[3], cellDatas[4], cellDatas[5]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.tuesday)), cellDatas[6], cellDatas[7], cellDatas[8], cellDatas[9], cellDatas[10], cellDatas[11]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.wednesday)), cellDatas[12], cellDatas[13], cellDatas[14], cellDatas[15], cellDatas[16], cellDatas[17]);
            timeList.addPdfTableRows(new CellData(availabilityLocalizer.localize(PdfLocalizationName.thursday)), cellDatas[18], cellDatas[19], cellDatas[20], cellDatas[21], cellDatas[22], cellDatas[23]);
            timeList.addPdfTableRows(new CellData(commonLocalizer.localize(PdfLocalizationName.friday)), cellDatas[24], cellDatas[25], cellDatas[26], cellDatas[27], cellDatas[28], cellDatas[29]);
        }

        pdfData.addTable(tableList);
        pdfData.addTable(timeList);

        //exceptional cases table
        ArrayList<ExceptionalTimeSlotItem> timeSlotItemExceptionalCases = item.getExceptionalCases();
        if (timeSlotItemExceptionalCases != null && timeSlotItemExceptionalCases.size() > 0) {
            ITextTableList exceptCases = new ITextTableList(7);
            exceptCases.setTotalWidth(450);
            exceptCases.setName(availabilityLocalizer.localize(PdfLocalizationName.exceptionalCases));
            exceptCases.addPdfTableHeader(cellData0, cellData, cellData1, cellData2);

            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy", commonLocalizer.initializeUserLocale());

            for (ExceptionalTimeSlotItem eTsi : timeSlotItemExceptionalCases) {
                CellData[] datas = {new CellData(formatDate.format(eTsi.getExceptionalDate().getNonConvertedDate())),
                        new CellData(getTimeSlot(eTsi.getWeekDay()[0] / 60) + ":" + getTimeSlot(eTsi.getWeekDay()[0] % 60)),
                        new CellData(getTimeSlot(eTsi.getWeekDay()[1] / 60) + ":" + getTimeSlot(eTsi.getWeekDay()[1] % 60)),
                        new CellData(getTimeSlot(eTsi.getLunch()[0] / 60) + ":" + getTimeSlot(eTsi.getLunch()[0] % 60)),
                        new CellData(getTimeSlot(eTsi.getLunch()[1] / 60) + ":" + getTimeSlot(eTsi.getLunch()[1] % 60)),
                        new CellData(getTimeSlot(eTsi.getCoffee()[0] / 60) + ":" + getTimeSlot(eTsi.getCoffee()[0] % 60)),
                        new CellData(getTimeSlot(eTsi.getCoffee()[1] / 60) + ":" + getTimeSlot(eTsi.getCoffee()[1] % 60))
                };
                for (int i = 1; i < datas.length; i++) {
                    datas[i].setAlignment(Element.ALIGN_CENTER);
                }
                exceptCases.addPdfTableRows(datas[0], datas[1], datas[2], datas[3], datas[4], datas[5], datas[6]);
            }
            pdfData.addTable(exceptCases);
        }
        //assigned department and employees table
        ITextTableList assigned = new ITextTableList(4);
        assigned.setTotalWidth(450);
        assigned.setName(availabilityLocalizer.localize(PdfLocalizationName.assignedEmployees));
        assigned.addPdfTableHeader(availabilityLocalizer.localize(PdfLocalizationName.employee),
                commonLocalizer.localize(PdfLocalizationName.department),
                availabilityLocalizer.localize(PdfLocalizationName.position),
                availabilityLocalizer.localize(PdfLocalizationName.status)
        );
        ArrayList<TimeslotEmployeeItem> timeslotEmployeeItems = item.getTimeslotEmployeeItems();
        if (timeslotEmployeeItems != null && timeslotEmployeeItems.size() > 0) {
            for (TimeslotEmployeeItem employeeItem : timeslotEmployeeItems) {
                assigned.addPdfTableRows(employeeItem.getEmployeeFullName(), employeeItem.getEmployeeDepartment(), employeeItem.getEmployeePosition(), employeeItem.getEmployeeStatus());
            }
        }
        pdfData.addTable(assigned);
        return pdf;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getName() + "_" + user.getLastName() + "_" + availabilityLocalizer.localize(PdfLocalizationName.timeslotFileName) + "_" + dateFormat(new Date()));
    }

    public void setAvailabilityService(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    private String getTimeSlot(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        if (String.valueOf(time).length() == 0) {
            return "00";
        }
        return String.valueOf(time);
    }
}
