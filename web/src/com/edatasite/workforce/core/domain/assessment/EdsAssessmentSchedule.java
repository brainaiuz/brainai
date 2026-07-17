package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsMonth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 21.01.2008
 * Time: 15:45:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assessmentSchedule")
public class EdsAssessmentSchedule extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId")
    private EdsAssessmentTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewerId")
    private EdsEmployee reviewer;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "employeeSchedule", joinColumns =
    @JoinColumn(name = "scheduleId", referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "employeeId", referencedColumnName = "id"))
    private Set<EdsEmployee> employees = new HashSet<>();

    private Integer lastMonth = -1;
    private Integer week = 0; //1st , 2nd, 3rd, 4th, last(5th)
    private Integer weekDay = 0;// Sunday-1, Monday-2 ...

    @Transient
    private Date scheduleDate;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "scheduleMonth", joinColumns =
    @JoinColumn(name = "assessmentScheduleId", referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "monthid", referencedColumnName = "id"))
    @OrderBy("objectID ASC")
    private List<EdsMonth> months = new ArrayList<>();  //should be something ordered

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastassessmentId")
    private EdsAssessment lastAssessment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public EdsEmployee getReviewer() {
        return reviewer;
    }

    public void setReviewer(EdsEmployee reviewer) {
        this.reviewer = reviewer;
    }

    public String getWeekDayToString() {
        if (getWeekDay() == null) {
            return "";
        }
        return switch (getWeekDay()) {
            case 1 -> ("Sunday");
            case 2 -> ("Monday");
            case 3 -> ("Tuesday");
            case 4 -> ("Wednsday");
            case 5 -> ("Thursday");
            case 6 -> ("Friday");
            case 7 -> ("Saturday");
            default -> "";
        };
    }


    public String getWeekToString() {
        if (getWeek() == null) {
            return "";
        }
        return switch (getWeek()) {
            case 1 -> ("First");
            case 2 -> ("Second");
            case 3 -> ("Third");
            case 4 -> ("Forth");
            case 5 -> ("Last");
            default -> "";
        };

    }

    public Integer[] getMonthIds() {
        Integer[] ids = new Integer[getMonths().size()];
        int i = 0;
        for (EdsMonth month : getMonths()) {
            ids[i] = month.getObjectID();
            i++;
        }
        return ids;
    }

    public Integer[] getEmployeesIds() {
        Integer[] ids = new Integer[getEmployees().size()];
        int i = 0;
        for (EdsEmployee employee : getEmployees()) {
            ids[i] = employee.getObjectID();
            i++;
        }
        return ids;
    }


    public Integer getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(Integer lastMonth) {
        this.lastMonth = lastMonth;
    }

    public EdsAssessmentTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EdsAssessmentTemplate template) {
        this.template = template;
    }

    public Set<EdsEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EdsEmployee> employees) {
        this.employees = employees;
    }

    public List<EdsMonth> getMonths() {
        return months;
    }

    public String getMonthsToString() {
        if (getMonths() == null || getMonths().size() == 0) {
            return "";
        }
        StringBuilder ms = new StringBuilder();
        Iterator<EdsMonth> iterator = getMonths().iterator();
        while (iterator.hasNext()) {
            EdsMonth elem = iterator.next();
            switch (elem.getObjectID()) {
                case 0 -> ms.append("Jan");
                case 1 -> ms.append("Feb");
                case 2 -> ms.append("Mar");
                case 3 -> ms.append("Apr");
                case 4 -> ms.append("May");
                case 5 -> ms.append("June");
                case 6 -> ms.append("July");
                case 7 -> ms.append("Aug");
                case 8 -> ms.append("Sep");
                case 9 -> ms.append("Oct");
                case 10 -> ms.append("Nov");
                case 11 -> ms.append("Dec");
            }
            if (iterator.hasNext()) {
                ms.append(", ");
            }
        }
        return ms.toString();
    }

    public String getCurrentMonthToString() {
        StringBuilder ms = new StringBuilder();
        Date date = new Date();
        switch (date.getMonth()) {
            case 0 -> ms.append("Jan");
            case 1 -> ms.append("Feb");
            case 2 -> ms.append("Mar");
            case 3 -> ms.append("Apr");
            case 4 -> ms.append("May");
            case 5 -> ms.append("June");
            case 6 -> ms.append("July");
            case 7 -> ms.append("Aug");
            case 8 -> ms.append("Sep");
            case 9 -> ms.append("Oct");
            case 10 -> ms.append("Nov");
            case 11 -> ms.append("Dec");
        }
        return ms.toString();
    }

    public String getNextMonthToString() {
        StringBuilder ms = new StringBuilder();
        Date date = new Date();
        date.setMonth(date.getMonth() + 1);
        switch (date.getMonth()) {
            case 0 -> ms.append("Jan");
            case 1 -> ms.append("Feb");
            case 2 -> ms.append("Mar");
            case 3 -> ms.append("Apr");
            case 4 -> ms.append("May");
            case 5 -> ms.append("June");
            case 6 -> ms.append("July");
            case 7 -> ms.append("Aug");
            case 8 -> ms.append("Sep");
            case 9 -> ms.append("Oct");
            case 10 -> ms.append("Nov");
            case 11 -> ms.append("Dec");
        }
        return ms.toString();
    }

    public String getEmployeesList() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = getEmployees().iterator();
        while (iterator.hasNext()) {
            EdsEmployee employee = (EdsEmployee) iterator.next();
            sb.append(employee.getName());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }


    public void setMonths(List<EdsMonth> months) {
        this.months = months;
    }

    public EdsAssessment getLastAssessment() {
        return lastAssessment;
    }

    public void setLastAssessment(EdsAssessment lastAssessment) {
        this.lastAssessment = lastAssessment;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }


}
