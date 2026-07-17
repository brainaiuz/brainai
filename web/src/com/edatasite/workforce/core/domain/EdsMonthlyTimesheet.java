package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: Lochin
 * Date: 27.03.2001
 * Time: 22:02:08
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "monthly_timesheet",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"project_employee_id", "month_year"})}
)
public class EdsMonthlyTimesheet extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "worked_hours")
    private Double workedHours;
    @Column(name = "total_days_worked")
    private Double totalDaysWorked;
    @Column(name = "overtime")
    private Double overtime;
    @Column(name = "holiday_overtime")
    private Double holidayOvertime;
    @Column(name = "weekend_overtime")
    private Double weekendOvertime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_employee_id")
    private EdsProjectEmployee projectEmployee;

    @Column(name = "month_year")
    private String monthYear;
    private Boolean isPaid;


    public EdsMonthlyTimesheet() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProjectEmployee getProjectEmployee() {
        return projectEmployee;
    }

    public void setProjectEmployee(EdsProjectEmployee projectEmployee) {
        this.projectEmployee = projectEmployee;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Double getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(Double workedHours) {
        this.workedHours = workedHours;
    }

    public Double getTotalDaysWorked() {
        return totalDaysWorked;
    }

    public void setTotalDaysWorked(Double totalDaysWorked) {
        this.totalDaysWorked = totalDaysWorked;
    }

    public Double getOvertime() {
        return overtime;
    }

    public void setOvertime(Double overtime) {
        this.overtime = overtime;
    }

    public Double getHolidayOvertime() {
        return holidayOvertime;
    }

    public void setHolidayOvertime(Double holidayOvertime) {
        this.holidayOvertime = holidayOvertime;
    }

    public Double getWeekendOvertime() {
        return weekendOvertime;
    }

    public void setWeekendOvertime(Double weekendOvertime) {
        this.weekendOvertime = weekendOvertime;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }
}
