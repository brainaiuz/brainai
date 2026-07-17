package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.hrms.client.rpc.GradeItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 10:26:26 AM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "grade")
public class EdsGrade extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EdsEmployee user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private EdsPosition position;

    @Column(name = "gradeCode")
    private String gradeCode;

    @Column(name = "gradeLevel")
    private String gradeLevel;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "hourlyMin")
    private Double hourlyMin;

    @Column(name = "weeklyMin")
    private Double weeklyMin;

    @Column(name = "monthlyMin")
    private Double monthlyMin;

    @Column(name = "annualMin")
    private Double annualMin;

    @Column(name = "hourlyMid")
    private Double hourlyMid;

    @Column(name = "monthlyMid")
    private Double monthlyMid;

    @Column(name = "annualMid")
    private Double annualMid;

    @Column(name = "hourlyMax")
    private Double hourlyMax;

    @Column(name = "monthlyMax")
    private Double monthlyMax;

    @Column(name = "annualMax")
    private Double annualMax;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getUser() {
        return user;
    }

    public void setUser(EdsEmployee user) {
        this.user = user;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getHourlyMin() {
        return hourlyMin;
    }

    public void setHourlyMin(Double hourlyMin) {
        this.hourlyMin = hourlyMin;
    }

    public Double getWeeklyMin() {
        return weeklyMin;
    }

    public void setWeeklyMin(Double weeklyMin) {
        this.weeklyMin = weeklyMin;
    }

    public Double getMonthlyMin() {
        return monthlyMin;
    }

    public void setMonthlyMin(Double monthlyMin) {
        this.monthlyMin = monthlyMin;
    }

    public Double getAnnualMin() {
        return annualMin;
    }

    public void setAnnualMin(Double annualMin) {
        this.annualMin = annualMin;
    }

    public Double getHourlyMid() {
        return hourlyMid;
    }

    public void setHourlyMid(Double hourlyMid) {
        this.hourlyMid = hourlyMid;
    }

    public Double getMonthlyMid() {
        return monthlyMid;
    }

    public void setMonthlyMid(Double monthlyMid) {
        this.monthlyMid = monthlyMid;
    }

    public Double getAnnualMid() {
        return annualMid;
    }

    public void setAnnualMid(Double annualMid) {
        this.annualMid = annualMid;
    }

    public Double getHourlyMax() {
        return hourlyMax;
    }

    public void setHourlyMax(Double hourlyMax) {
        this.hourlyMax = hourlyMax;
    }

    public Double getMonthlyMax() {
        return monthlyMax;
    }

    public void setMonthlyMax(Double monthlyMax) {
        this.monthlyMax = monthlyMax;
    }

    public Double getAnnualMax() {
        return annualMax;
    }

    public void setAnnualMax(Double annualMax) {
        this.annualMax = annualMax;
    }

    public GradeItem getRPC() {
        GradeItem item = new GradeItem();
        //grade ID
        item.setObjectId(getObjectID());
        //grade code
        item.setGradeCode(getGradeCode());
        //grade level
        item.setGradeLevel(getGradeLevel());
        //grade description
        item.setDescription(getDescription());
        //grade position
        if (getPosition() != null) {
            item.setPositionId(getPosition().getObjectID());
        }
        //salary range
        //hourly min
        item.setHourlyMin(getHourlyMin());
        //weekly min
        item.setWeeklyMin(getWeeklyMin());
        //monthly min
        item.setMonthlyMin(getMonthlyMin());
        //annual min
        item.setAnnualMin(getAnnualMin());
        //hourly mid
        item.setHourlyMid(getHourlyMid());
        //monthly mid
        item.setMonthlyMid(getMonthlyMid());
        //annual mid
        item.setAnnualMid(getAnnualMid());
        //hourly max
        item.setHourlyMax(getHourlyMax());
        //monthly max
        item.setMonthlyMax(getMonthlyMax());
        //annual max
        item.setAnnualMax(getAnnualMax());

        return item;
    }

    @Override
    public String getName() {
        String desc = "";
        if (getDescription() != null && !"".equals(getDescription())) {
            desc = " - " + (getDescription().length() > 25 ? getDescription().substring(0, 25) : getDescription());
        }
        return getGradeCode() + " " + getGradeLevel() + desc;
    }
}
