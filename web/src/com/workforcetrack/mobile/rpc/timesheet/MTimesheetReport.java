package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * User: Abdulaziz
 * Date: 8/18/11
 * Time: 5:12 PM
 */
@XmlRootElement
public class MTimesheetReport implements Serializable{
    private int sum;
    private String projectName;
    public MTimesheetReport(){

    }
    public MTimesheetReport(TimesheetReport report){
        this.sum = report.getSum();
        this.projectName = report.getProjectName();
    }

    public static TimesheetReport convertFromMobile(MTimesheetReport mReport){
       TimesheetReport report = new TimesheetReport();
       report.setSum(mReport.getSum());
       report.setProjectName(mReport.getProjectName());
       return  report;
    }
    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
