package com.edatasite.workforce.gwt.core.server.charts;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * @autor : Dilshod
 */
public class EmployeeReportData {

    private Integer id;
    private String category = "";
    private double timeslot = 0;
    private double dailyload = 0;
    private double timesheet = 0;
    private double actualInHours = 0;
    private double annualAllowance = 20; // set default employee holiday
    private double annualRequestDay = 0;
    private int annualRequestHour = 0;
    private int workHour = 360;
    private List<SelectItem> reasons;

    public EmployeeReportData() {
    }

    public EmployeeReportData(String category) {
        this.category = category;
    }


    public void addAllData(int tslot, int dload, int tsheet) {
        timeslot = timeslot + (double) tslot / 60;
        dailyload = dailyload + (double) dload / 60;
        timesheet = timesheet + (double) tsheet / 60;
    }


    public void addAnnualRequestDay(int day) {
        annualRequestDay += day;
    }

    public void addAnnualRequestHour(int hour) {
        this.annualRequestHour += hour;
    }


    public String getCategory() {
        return category;
    }

    public double getTimeslot() {
        return timeslot;
    }

    public double getDailyload() {
        return dailyload;
    }

    public double getTimesheet() {
        return timesheet;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getActualInHours() {
        return actualInHours;
    }

    public double getAnnualAllowance() {
        return getFormattedVersion(annualAllowance);
    }

    public void setAnnualAllowance(double annualAllowance) {
        this.annualAllowance = annualAllowance;
    }

    public double getAnnualRequestDay() {
        return getFormattedVersion(annualRequestDay);
    }

    public void setAnnualRequestDay(double annualRequestDay) {
        this.annualRequestDay = annualRequestDay;
    }

    public double getTakenStatutorySummaryDay() {
        double sum = annualAllowance - annualRequestDay;
        if (sum >= 0) {
            return getFormattedVersion(annualRequestDay);
        } else {
            return getFormattedVersion(annualAllowance);
        }
    }

    public double getLeftStatutorySummary() {
        double sum = annualAllowance - annualRequestDay;
        if (sum > 0) {
            return getFormattedVersion(sum);
        } else {
            return 0;
        }
    }

    public double getExceededSummary() {
        double sum = annualAllowance - annualRequestDay;
        if (sum < 0) {
            return getFormattedVersion(sum * -1);
        } else {
            return 0;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAnnualRequestHour() {
        return annualRequestHour;
    }

    public int getWorkHour() {
        return workHour;
    }


    public void setWorkHour(int workHour) {
        this.workHour = workHour;
    }

    public List<SelectItem> getReason() {
        return reasons;
    }

    public void setReason(List<SelectItem> reasons) {
        this.reasons = reasons;
    }

    /**
     * gets 12.123123123 returns 12.1
     *
     * @param number
     * @return
     */
    private double getFormattedVersion(double number){
        DecimalFormat df = new DecimalFormat("0.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        try {
            return Double.valueOf(df.format(number));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
