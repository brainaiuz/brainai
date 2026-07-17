package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class BackendChartHandler implements HttpRequestHandler {

    public static int PERIOD_TODAY = 0;
    public static int PERIOD_YESTERDAY = 1;
    public static int PERIOD_LAST_10_DAYS = 2;
    public static int PERIOD_THIS_MONTH = 3;
    public static int PERIOD_LAST_MONTH = 4;
    public static int PERIOD_ALL_PERIOD = 5;

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private UserSessionManager userSessionManager;

    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JFreeChart chart = null;
        String chartType = request.getParameter("chartType");
        String periodId = request.getParameter("periodId");
        int periodType = PERIOD_LAST_10_DAYS;
        if (periodId != null) {
            periodType = Integer.parseInt(periodId);
        }
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();

        if ("dailyActivity".equals(chartType)) {
            chart = dailyActivityChart(periodType);
            int height = 300;
            int width = 700;
            writeChartAsPNG(out, chart, width, height);
        }
        out.close();

    }

    public static void writeChartAsPNG(OutputStream out, JFreeChart chart,
                                       int width, int height) throws IOException {

        ChartUtilities.writeChartAsPNG(out, chart, width, height, null);

    }

    public JFreeChart dailyActivityChart(int periodType) {

        List<Object[]> dataList = updateList(periodType);
        TimeSeriesCollection timeSheetSummaryDataSet = new TimeSeriesCollection();
        TimeSeries s1 = new TimeSeries("Signups", Day.class);
        TimeSeries s2 = new TimeSeries("Employees Registered", Day.class);
        TimeSeries s3 = new TimeSeries("Employees Activated", Day.class);
        TimeSeries s4 = new TimeSeries("Access Counts", Day.class);

        if (PERIOD_LAST_MONTH == periodType || PERIOD_THIS_MONTH == periodType || PERIOD_ALL_PERIOD == periodType) {

            s1 = new TimeSeries("Signups", Month.class);
            s2 = new TimeSeries("Employees Registered", Month.class);
            s3 = new TimeSeries("Employees Activated", Month.class);
            s4 = new TimeSeries("Access Counts", Month.class);

        }

        if (dataList != null) {
            for (Object[] elem : dataList) {
                long value1 = Long.parseLong("" + elem[1]);
                long value2 = Long.parseLong("" + elem[2]);
                long value3 = Long.parseLong("" + elem[3]);
                long value4 = Long.parseLong("" + elem[4]);
                String name = (String) elem[0];
                if (name.length() == 7) { // i.e. 2007-01
                    int mm = Integer.parseInt(name.split("-")[1]);
                    int yy = Integer.parseInt(name.split("-")[0]);
                    Month month = new Month(mm, yy);
                    s1.add(month, value1);
                    s2.add(month, value2);
                    s3.add(month, value3);
                    s4.add(month, value4);
                } else { // 2007-01-02
                    int mm = Integer.parseInt(name.split("-")[1]);
                    int yy = Integer.parseInt(name.split("-")[0]);
                    int dd = Integer.parseInt(name.split("-")[2]);

                    Calendar calendar = new GregorianCalendar();
                    calendar.set(Calendar.DATE, dd);
                    calendar.set(Calendar.MONTH, mm - 1);
                    calendar.set(Calendar.YEAR, yy);
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Day day = new Day(calendar.getTime());
                    s1.add(day, value1);
                    s2.add(day, value2);
                    s3.add(day, value3);
                    s4.add(day, value4);
                }
            }
            timeSheetSummaryDataSet.addSeries(s4);
            timeSheetSummaryDataSet.addSeries(s1);
            timeSheetSummaryDataSet.addSeries(s2);
            timeSheetSummaryDataSet.addSeries(s3);

        }
        return ChartFactory.createTimeSeriesChart("", "", "",
                timeSheetSummaryDataSet, true, true, true);

    }

    public List<Object[]> updateList(int periodType) {

        List<Object[]> dataList = new ArrayList<>();

        if (periodType == PERIOD_TODAY) {
            Date[] dates = getBackwardDates(1);
            List<Date> employeeRegisteredDates = employeeManager.getEmployeesRegisteredByDate(dates[0], dates[0]);
            List<Date> signUpDates = companyManager.getSignupCompaniesByDate(dates[0], dates[dates.length - 1]);
            List<Date> employeesActivationDates = employeeManager.getEmployeesActivationDates(dates[0], dates[0]);
            List<Date> accessCountDates = userSessionManager.getAccessDates(dates[0], dates[0]);
            for (Date d : dates) {
                int signupcount = getDateCount(signUpDates, d);
                int employeescount = getDateCount(employeeRegisteredDates, d);
                int activationcount = getDateCount(employeesActivationDates, d);
                int accesscount = getDateCount(accessCountDates, d);
                String str = ServerUtils.getDateAsString(d/*,employeeManager.getUser()*/);
                dataList.add(new Object[]{str, signupcount, employeescount, activationcount, accesscount});
            }
        } else if (periodType == PERIOD_YESTERDAY) {

            Date[] dates = getBackwardDates(2);
            List<Date> employeeRegisteredDates = employeeManager.getEmployeesRegisteredByDate(dates[0], dates[dates.length - 1]);
            List<Date> employeesActivationDates = employeeManager.getEmployeesActivationDates(dates[0], dates[dates.length - 1]);
            List<Date> signUpDates = companyManager.getSignupCompaniesByDate(dates[0], dates[dates.length - 1]);
            List<Date> accessCountDates = userSessionManager.getAccessDates(dates[0], dates[dates.length - 1]);
            for (Date d : dates) {
                int signupcount = getDateCount(signUpDates, d);
                int employeescount = getDateCount(employeeRegisteredDates, d);
                int activationcount = getDateCount(employeesActivationDates, d);
                int accesscount = getDateCount(accessCountDates, d);
                String str = ServerUtils.getDateAsString(d/*,employeeManager.getUser()*/);
                dataList.add(new Object[]{str, signupcount, employeescount, activationcount, accesscount});
            }

        } else if (periodType == PERIOD_LAST_10_DAYS) {

            Date[] dates = getBackwardDates(10);
            List<Date> employeeRegisteredDates = employeeManager.getEmployeesRegisteredByDate(dates[0], dates[dates.length - 1]);
            List<Date> employeesActivationDates = employeeManager.getEmployeesActivationDates(dates[0], dates[dates.length - 1]);
            List<Date> signUpDates = companyManager.getSignupCompaniesByDate(dates[0], dates[dates.length - 1]);
            List<Date> accessCountDates = userSessionManager.getAccessDates(dates[0], dates[dates.length - 1]);
            for (Date d : dates) {
                int signupcount = getDateCount(signUpDates, d);
                int employeescount = getDateCount(employeeRegisteredDates, d);
                int activationcount = getDateCount(employeesActivationDates, d);
                int accesscount = getDateCount(accessCountDates, d);
                String str = ServerUtils.getDateAsString(d/*,employeeManager.getUser()*/);
                dataList.add(new Object[]{str, signupcount, employeescount, activationcount, accesscount});
            }

        } else if (periodType == PERIOD_LAST_MONTH) {

            Date[] dates = getBackwardMonths(2);
            Date startDate = dates[0];
            Date endDate = new Date();//dates[dates.length - 1];
            List<Date> employeeRegisteredDates = employeeManager.getEmployeesRegisteredByDate(startDate, endDate);
            List<Date> employeesActivationDates = employeeManager.getEmployeesActivationDates(startDate, endDate);
            List<Date> signUpDates = companyManager.getSignupCompaniesByDate(startDate, endDate);
            List<Date> accessCountDates = userSessionManager.getAccessDates(startDate, endDate);
            for (Date d : dates) {
                int signupcount = getMonthCount(signUpDates, d);
                int employeescount = getMonthCount(employeeRegisteredDates, d);
                int activationcount = getMonthCount(employeesActivationDates, d);
                int accesscount = getDateCount(accessCountDates, d);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String str = format.format(d);
                dataList.add(new Object[]{str, signupcount, employeescount, activationcount, accesscount});
            }

        } else if (periodType == PERIOD_THIS_MONTH) {


            Date[] dates = getBackwardMonths(1);
            Date startDate = dates[0];
            Date endDate = new Date();//dates[dates.length - 1];
            List<Date> employeeRegisteredDates = employeeManager.getEmployeesRegisteredByDate(startDate, endDate);
            List<Date> employeesActivationDates = employeeManager.getEmployeesActivationDates(startDate, endDate);
            List<Date> signUpDates = companyManager.getSignupCompaniesByDate(startDate, endDate);
            List<Date> accessCountDates = userSessionManager.getAccessDates(startDate, endDate);
            for (Date d : dates) {
                int signupcount = getMonthCount(signUpDates, d);
                int employeescount = getMonthCount(employeeRegisteredDates, d);
                int activationcount = getMonthCount(employeesActivationDates, d);
                int accesscount = getDateCount(accessCountDates, d);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String str = format.format(d);
                dataList.add(new Object[]{str, signupcount, employeescount, activationcount, accesscount});
            }

        } else if (periodType == PERIOD_ALL_PERIOD) {

            Calendar calendar = Calendar.getInstance();
            Date[] dates = getBackwardMonths(calendar.get(Calendar.MONTH) + 1);
            Date startDate = dates[0];
            Date endDate = new Date();//dates[dates.length - 1];
            List<Date> employeeRegisteredDates = employeeManager.getEmployeesRegisteredByDate(startDate, endDate);
            List<Date> employeesActivationDates = employeeManager.getEmployeesActivationDates(startDate, endDate);
            List<Date> signUpDates = companyManager.getSignupCompaniesByDate(startDate, endDate);
            List<Date> accessCountDates = userSessionManager.getAccessDates(startDate, endDate);

            for (Date d : dates) {
                int signupcount = getMonthCount(signUpDates, d);
                int employeescount = getMonthCount(employeeRegisteredDates, d);
                int activationcount = getMonthCount(employeesActivationDates, d);
                int accesscount = getDateCount(accessCountDates, d);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String str = format.format(d);
                dataList.add(new Object[]{str, signupcount, employeescount, employeescount, accesscount});
            }
        }
        return dataList;
    }

    private Date[] getBackwardDates(int days) {
        Calendar calendar = Calendar.getInstance();
        Date[] dates = new Date[days];
        calendar.add(Calendar.DATE, -days);
        for (int i = 0; i < days; i++) {
            calendar.add(Calendar.DATE, 1);
            dates[i] = calendar.getTime();
        }
        return dates;
    }

    private Date[] getBackwardMonths(int months) {
        Date[] dates = new Date[months];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -months);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < months; i++) {
            calendar.add(Calendar.MONTH, 1);
            dates[i] = calendar.getTime();
        }
        return dates;
    }

    private int getDateCount(List<Date> list, Date date) {
        int count = 0;
        try {
            for (Date d : list) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str1 = format.format(d);
                String str2 = format.format(date);
                if (str1.equals(str2)) {
                    count++;
                }
            }
        } catch (RuntimeException e) {

            e.printStackTrace();
        }
        return count;
    }

    private int getMonthCount(List<Date> list, Date date) {
        int count = 0;
        try {
            for (Date d : list) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String str1 = format.format(d);
                String str2 = format.format(date);
                if (str1.equals(str2)) {
                    count++;
                }
            }
        } catch (RuntimeException e) {

            e.printStackTrace();
        }
        return count;
    }

}
