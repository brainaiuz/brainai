package com.edatasite.workforce.gwt.core.client.rpc.task;

import com.edatasite.workforce.core.domain.EdsTimeSheet;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Eminem
 * Date: 05/10/12
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetSummary {
    
    private EdsTimeSheet timeSheet;
    private Integer minutes;
    private ArrayList<Integer> oldEmployeeTaskIDList;

    public TimesheetSummary() {
        this.minutes = 0;
        this.oldEmployeeTaskIDList = new ArrayList<>();
    }

    public EdsTimeSheet getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(EdsTimeSheet timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = this.minutes + minutes;
    }

    public ArrayList<Integer> getOldEmployeeTaskIDList() {
        return oldEmployeeTaskIDList;
    }

    public void setOldEmployeeTaskIDList(ArrayList<Integer> oldEmployeeTaskIDList) {
        this.oldEmployeeTaskIDList = oldEmployeeTaskIDList;
    }
}
