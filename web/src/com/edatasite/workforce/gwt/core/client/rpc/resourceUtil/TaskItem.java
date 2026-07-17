package com.edatasite.workforce.gwt.core.client.rpc.resourceUtil;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/21/12
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskItem implements IsSerializable {

    private DateNonConvertable[] dailyDate;     //Daily date
    private boolean editable;                   //Editable option for time spent
    private boolean isIssue;                    //is issue
    private String task_description;            //Task description
    private Integer task_id;                    //Task id
    private String task_name;                   //Task name
    private Date task_start_date;               //Task start date
    private Date task_due_date;                 //Task due date

    private int[] totalEstimatedTime;           //Daily task total time spent
    private int[] totalTimeSheetHours;          //Daily total timeSheet hour
    private int[] totalTimeSlotHours;           //Daily total timeSlot hour

    private int[] withHoliday_INT;              //Daily holiday time
    private int[] with_LR_INT;                  //With LR time
    private boolean[] workingDay;               //Working day

    public TaskItem() {
    }

    public TaskItem(Integer task_id, String task_name, int maxMonthDay) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.dailyDate = new DateNonConvertable[maxMonthDay];
        this.totalEstimatedTime = new int[maxMonthDay];
        this.totalTimeSheetHours = new int[maxMonthDay];
        this.totalTimeSlotHours = new int[maxMonthDay];
        this.withHoliday_INT = new int[maxMonthDay];
        this.with_LR_INT = new int[maxMonthDay];
        this.workingDay = new boolean[maxMonthDay];
    }

    public DateNonConvertable[] getDailyDate() {
        return dailyDate;
    }

    public void setDailyDate(DateNonConvertable[] dailyDate) {
        this.dailyDate = dailyDate;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isIssue() {
        return isIssue;
    }

    public void setIssue(boolean issue) {
        isIssue = issue;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public Date getTask_start_date() {
        return task_start_date;
    }

    public void setTask_start_date(Date task_start_date) {
        this.task_start_date = task_start_date;
    }

    public Date getTask_due_date() {
        return task_due_date;
    }

    public void setTask_due_date(Date task_due_date) {
        this.task_due_date = task_due_date;
    }

    public int[] getTotalEstimatedTime() {
        return totalEstimatedTime;
    }

    public void setTotalEstimatedTime(int[] totalEstimatedTime) {
        this.totalEstimatedTime = totalEstimatedTime;
    }

    public int[] getTotalTimeSheetHours() {
        return totalTimeSheetHours;
    }

    public void setTotalTimeSheetHours(int[] totalTimeSheetHours) {
        this.totalTimeSheetHours = totalTimeSheetHours;
    }

    public int[] getTotalTimeSlotHours() {
        return totalTimeSlotHours;
    }

    public void setTotalTimeSlotHours(int[] totalTimeSlotHours) {
        this.totalTimeSlotHours = totalTimeSlotHours;
    }

    public int[] getWithHoliday_INT() {
        return withHoliday_INT;
    }

    public void setWithHoliday_INT(int[] withHoliday_INT) {
        this.withHoliday_INT = withHoliday_INT;
    }

    public int[] getWith_LR_INT() {
        return with_LR_INT;
    }

    public void setWith_LR_INT(int[] with_LR_INT) {
        this.with_LR_INT = with_LR_INT;
    }

    public boolean[] getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(boolean[] workingDay) {
        this.workingDay = workingDay;
    }
}