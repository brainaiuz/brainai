package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: 7/16/11
 * Time: 6:52 PM
 */
public class MTimesheetData implements Serializable{
    public MTimesheetData(){

    }
    public MTimesheetData(TimesheetData data){
        if(data != null){
            this.year =  data.getYear();
            this.week = data.getWeek();
            this.lastWeek = data.isLastWeek();
            this.employeeId = data.getEmployeeId();

            this.clientsToday = (data.getClientsToday() != null && data.getClientsToday().getDateLong()>0)?data.getClientsToday().getNonConvertedDate():null;
            this.today = (data.getToday()!=null && data.getToday().getDateLong()>0)?data.getToday().getNonConvertedDate():null;
            this.yesterday = (data.getYesterday() != null && data.getYesterday().getDateLong()>0)?data.getYesterday().getNonConvertedDate():null;
            if(data.getDates() != null){
               for(DateNonConvertable dat:data.getDates()){
                   this.dates.add(dat.getNonConvertedDate());
               }
            }
            if(data.getDailyStatistics()!= null){
                for(int st:data.getDailyStatistics()){
                    this.dailyStatistics.add(st);
                }
            }
            if(data.getTransferTasks()!=null){
                for(TaskTransfer tr:data.getTransferTasks()){
                    MTaskTransfer trans = new MTaskTransfer(tr);
                    this.transferTasks.add(trans);
                }
            }
            if(data.getItems()!=null){
                for(TimesheetDataItem item:data.getItems()){
                    MTimesheetDataItem mItem = new MTimesheetDataItem(item);
                    this.items.add(mItem);
                }
            }
            this.timeslotItem = new MTimeslotItem(data.getTimeslotItem());
//            this.transferTasks = data.getTransferTasks();
//            this.items = data.getItems();
//            this.weeklyStatistics =  data.getWeeklyStatistics();
//            this.monthlyStatistices = data.getMonthlyStatistices();
        }
    }
    public static TimesheetData convertFromMobile(MTimesheetData mData){
        TimesheetData data = new TimesheetData();
        data.setYear(mData.getYear());
        data.setWeek(mData.getWeek());
        data.setLastWeek(mData.isLastWeek());
        if(mData.getDailyStatistics()!=null && mData.getDailyStatistics().size()>0){
            int[] ds = new int[mData.getDailyStatistics().size()];
            int i = 0;
            for(Integer it:mData.getDailyStatistics()){
                ds[i] = it;
                i++;
            }
            data.setDailyStatistics(ds);
        }

        if(mData.getClientsToday()!=null){
            data.setClientsToday(new DateNonConvertable(mData.getClientsToday()));
        }
        if(mData.getToday()!=null){
            data.setToday(new DateNonConvertable(mData.getToday()));
        }
        if(mData.getYesterday()!=null){
            data.setYesterday(new DateNonConvertable(mData.getYesterday()));
        }
        if(mData.getDates()!=null && mData.getDates().size()>0){
            DateNonConvertable[] ds = new DateNonConvertable[mData.getDates().size()];
            int i = 0;
            for(Date d:mData.getDates()){
                ds[i] = new DateNonConvertable(d);
                i++;
           }
            data.setDates(ds);
        }

        if(mData.getTransferTasks()!=null&&mData.getTransferTasks().size()>0){
            TaskTransfer[] ts = new TaskTransfer[mData.getTransferTasks().size()];
            int i = 0;
            for(MTaskTransfer tt:mData.getTransferTasks()){
                ts[i] = MTaskTransfer.convertFromMobile(tt);
                i++;
            }
            data.setTransferTasks(ts);
        }

        if(mData.getItems()!=null && mData.getItems().size()>0){
            TimesheetDataItem[] tit = new TimesheetDataItem[mData.getItems().size()];
            int i = 0;
            for(MTimesheetDataItem mItem:mData.getItems()){
                tit[i] = MTimesheetDataItem.convertFromMobile(mItem);
                i++;
            }
            data.setItems(tit);
        }
        if(mData.getWeeklyStatistics()!=null&&mData.getWeeklyStatistics().size()>0){
            TimesheetReport[] tr = new TimesheetReport[mData.getWeeklyStatistics().size()];
            int i = 0;
            for(MTimesheetReport mr:mData.getWeeklyStatistics()){
                tr[i] = MTimesheetReport.convertFromMobile(mr);
                i++;
            }
            data.setWeeklyStatistics(tr);
        }
        if(mData.getMonthlyStatistices()!=null&&mData.getMonthlyStatistices().size()>0){
            TimesheetReport[] tr = new TimesheetReport[mData.getMonthlyStatistices().size()];
            int i = 0;
            for(MTimesheetReport mr:mData.getMonthlyStatistices()){
                tr[i] = MTimesheetReport.convertFromMobile(mr);
                i++;
            }
            data.setMonthlyStatistices(tr);
        }
        data.setTimeslotItem(MTimeslotItem.convertFromMobile(mData.getTimeslotItem()));

        return data;
    }
    private int year;
    private int week;
    private boolean lastWeek;
    private int employeeId;
    private List<Integer> dailyStatistics = new ArrayList<>();

    private Date clientsToday;
    private Date today;
    private Date yesterday;
    private List<Date> dates = new ArrayList<>();

    private List<MTaskTransfer> transferTasks = new ArrayList<>();
    private List<MTimesheetDataItem> items = new ArrayList<>();
    private List<MTimesheetReport> weeklyStatistics = new ArrayList<>();
    private List<MTimesheetReport> monthlyStatistices = new ArrayList<>();
    private MTimeslotItem timeslotItem;
//    private List<MSelectItem> projects = new ArrayList<MSelectItem>();
//    private List<MSelectItem> workstream = new ArrayList<MSelectItem>();
//    private List<MSelectItem> clients = new ArrayList<MSelectItem>();
//    private List<MSelectItem> employees = new ArrayList<MSelectItem>();

    public MTimeslotItem getTimeslotItem() {
        return timeslotItem;
    }

    public void setTimeslotItem(MTimeslotItem timeslotItem) {
        this.timeslotItem = timeslotItem;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public boolean isLastWeek() {
        return lastWeek;
    }

    public void setLastWeek(boolean lastWeek) {
        this.lastWeek = lastWeek;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public List<Integer> getDailyStatistics() {
        return dailyStatistics;
    }

    public void setDailyStatistics(List<Integer> dailyStatistics) {
        this.dailyStatistics = dailyStatistics;
    }

    public Date getClientsToday() {
        return clientsToday;
    }

    public void setClientsToday(Date clientsToday) {
        this.clientsToday = clientsToday;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getYesterday() {
        return yesterday;
    }

    public void setYesterday(Date yesterday) {
        this.yesterday = yesterday;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public List<MTaskTransfer> getTransferTasks() {
        return transferTasks;
    }

    public void setTransferTasks(List<MTaskTransfer> transferTasks) {
        this.transferTasks = transferTasks;
    }

    public List<MTimesheetDataItem> getItems() {
        return items;
    }

    public void setItems(List<MTimesheetDataItem> items) {
        this.items = items;
    }

    public List<MTimesheetReport> getWeeklyStatistics() {
        return weeklyStatistics;
    }

    public void setWeeklyStatistics(List<MTimesheetReport> weeklyStatistics) {
        this.weeklyStatistics = weeklyStatistics;
    }

    public List<MTimesheetReport> getMonthlyStatistices() {
        return monthlyStatistices;
    }

    public void setMonthlyStatistices(List<MTimesheetReport> monthlyStatistices) {
        this.monthlyStatistices = monthlyStatistices;
    }

    public  <T> void convertArrayToList(T a[], List<T> list){
        if(a != null && a.length > 0){
            list.addAll(Arrays.asList(a));
        }
    }

}
