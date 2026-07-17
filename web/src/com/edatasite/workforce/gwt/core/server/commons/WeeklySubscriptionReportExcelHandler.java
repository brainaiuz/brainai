package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.server.db.CompanyStatisticManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Dec 9, 2009
 * Time: 8:04:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeeklySubscriptionReportExcelHandler extends BaseExcelHandler implements ExcelBAOSHandler {

    private static final Logger log = LoggerFactory.getLogger(BackendViewExcelHandler.class);
    private Format formatter = new SimpleDateFormat("MM-dd-yyyy");

    @Autowired
    private CompanyStatisticManager statisticManager;

    protected Object getDataClass(HttpServletRequest request) {
        return null;
    }

    @Override
    protected void setFileName() {
        Calendar endTime = new GregorianCalendar();
        endTime.setTime(getStartTime(endTime.getTime()));
        filename = "Weekly_Subscription_report_" + formatter.format(endTime.getTime());
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        // Get date a week ago
        Calendar startTime = new GregorianCalendar();
        startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) - 7);
        startTime.setTime(getStartTime(startTime.getTime()));

        Calendar endTime = new GregorianCalendar();
        endTime.setTime(getStartTime(endTime.getTime()));


        List<CompanyListItem> companyListItems = statisticManager.getWeeklySubscriptions(startTime.getTime(), endTime.getTime());
        ExcelData[] cellDatas;
        try {
            List<ExcelData[]> list = new LinkedList<>();
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            cellDatas = new ExcelData[]{
                    new ExcelData(" Total subscriptions : " + companyListItems.size(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3),
                    new ExcelData(" Report period : " + formatter.format(startTime.getTime()) + " - " + formatter.format(endTime.getTime()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3),
            };
            list.add(cellDatas);

            cellDatas = new ExcelData[]{
                    new ExcelData("Company Name", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Email", ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Contact Person", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),

                    new ExcelData("Phone", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Last Access", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Registration Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Expire Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Active", ExcelData.STRING, 8, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),

                    new ExcelData("Subscription Type", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Payment Status", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),

                    new ExcelData("Country", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("SignUped Ip", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Access Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Employees", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Projects", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Tasks", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Departments", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Overall users", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Active users", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Clients", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("InProgress Tasks", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Completed Tasks", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),

            };
            list.add(cellDatas);

            if (companyListItems != null && companyListItems.size() > 0) {
                for (CompanyListItem item : companyListItems) {
                    cellDatas = new ExcelData[]{
                            new ExcelData(item.getCompanyName(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getEmail(), ExcelData.STRING, 20, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getContactPerson(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),

                            new ExcelData(item.getPhone(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(getDate(item.getLastAccessDate()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(getDate(item.getRegistrationDate()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(getDate(item.getUsagPlanEndDate()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getActivated(), ExcelData.STRING, 8, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),

                            new ExcelData(item.getUsagePlanPaymentType(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getUsagePlanPaymentStatus(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),

                            new ExcelData(item.getCountry(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getCompanySigupCompIP(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getAccessCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getEmployeeCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getProjectCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getTaskCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getDepartmentCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getOverallUsersCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getActiveUsersCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getClientsCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getTasksinProgressCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                            new ExcelData(item.getTasksCompletedCount(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),


                    };
                    list.add(cellDatas);
                }
            }

            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot ganarate backend view excel report, exception: " + e);
        }
        return null;
    }

    private String getDate(Date date) {
        formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String value = "";
        if (date != null) {
            value = formatter.format(date);
        }
        return value;
    }

    private Date getStartTime(Date sTime) {
        sTime.setHours(0);
        sTime.setMinutes(0);
        sTime.setSeconds(0);
        return sTime;
    }
}