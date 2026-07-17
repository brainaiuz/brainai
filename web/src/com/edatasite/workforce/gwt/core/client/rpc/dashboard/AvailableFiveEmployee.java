package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 02.07.2009
 * Time: 14:26:20
 * To change this template use File | Settings | File Templates.
 */

public class AvailableFiveEmployee implements IsSerializable {

    private Integer id;
    private String employeeName;
    private String departmentName;
    private Date date;
    private Integer lunchStart;
    private Integer lunchEnd;
    private Integer coffeeStart;
    private Integer coffeeEnd;
    private long total = 0; //minute
    private long interval = 0;
    private long hour = 0;
    private long day = (1000 * 60 * 60 * 24);

    public AvailableFiveEmployee() {
    }

    public AvailableFiveEmployee(Integer id, String firstName, String lastName, String departmentName, Date endDate, Date startDate) {
        this.id = id;
        this.employeeName = firstName + " " + lastName;
        this.departmentName = departmentName;
        this.date = startDate;
        interval = (endDate.getTime() - startDate.getTime()) / day;
        hour = (endDate.getTime() - startDate.getTime()) % day;
        total += ((interval != 0 ? day : 0) + hour) / 60000;// 60 sec * 1000 milli sec
    }

    public AvailableFiveEmployee(Integer id, String firstName, String lastName, String departmentName, Date endDate, Date startDate, Integer lunchEnd, Integer lunchStart, Integer coffeeEnd, Integer coffeeStart) {
        this.id = id;
        this.employeeName = firstName + " " + lastName;
        this.departmentName = departmentName;
        this.lunchEnd = lunchEnd;
        this.lunchStart = lunchStart;
        this.coffeeEnd = coffeeEnd;
        this.coffeeStart = coffeeStart;
        this.date = startDate;
        interval = (endDate.getTime() - startDate.getTime()) / day;
        hour = (endDate.getTime() - startDate.getTime()) % day;
        total += ((interval != 0 ? day : 0) + hour) / 60000 - (this.lunchEnd - this.lunchStart) - (this.coffeeEnd - this.coffeeStart);// 60 sec * 1000 milli sec
    }

    public AvailableFiveEmployee(Integer id, String employeeName, String departmentName, long total) {
        this.id = id;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.total = total;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addTotal(long num) {
        this.total += num;
    }

    public void setTimeAvarage(long avarageCount) {
        total = total / avarageCount;
    }
}
