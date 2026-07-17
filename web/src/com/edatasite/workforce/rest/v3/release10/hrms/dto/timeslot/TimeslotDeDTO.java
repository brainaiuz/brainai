package com.edatasite.workforce.rest.v3.release10.hrms.dto.timeslot;

import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TimeslotDeDTO {

    private Integer id;
    private String name;
    private String description;

    private Integer weekStart;

    private int[] monday;
    private int[] tuesday;
    private int[] wednesday;
    private int[] thursday;
    private int[] friday;
    private int[] saturday;
    private int[] sunday;

    private int[] lunchMo;
    private int[] lunchTu;
    private int[] lunchWe;
    private int[] lunchTh;
    private int[] lunchFr;
    private int[] lunchSa;
    private int[] lunchSu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    public TimeslotDeDTO() {
    }

    public TimeslotDeDTO(TimeslotItem timeslot) {
        this.id = timeslot.getObjectID();
        this.name = timeslot.getName();
        this.description = timeslot.getDescription();
        this.weekStart = timeslot.getWeekStart();
        this.monday = timeslot.getMonday();
        this.tuesday = timeslot.getTuesday();
        this.wednesday = timeslot.getWednesday();
        this.thursday = timeslot.getThursday();
        this.friday = timeslot.getFriday();
        this.saturday = timeslot.getSaturday();
        this.sunday = timeslot.getSunday();
        this.lunchMo = timeslot.getLunchMo();
        this.lunchTu = timeslot.getLunchTu();
        this.lunchWe = timeslot.getLunchWe();
        this.lunchTh = timeslot.getLunchTh();
        this.lunchFr = timeslot.getLunchFr();
        this.lunchSa = timeslot.getLunchSa();
        this.lunchSu = timeslot.getLunchSu();
        this.effectiveDate = timeslot.getEffectiveDate();
        this.endDate = timeslot.getEndDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(Integer weekStart) {
        this.weekStart = weekStart;
    }

    public int[] getMonday() {
        return monday;
    }

    public void setMonday(int[] monday) {
        this.monday = monday;
    }

    public int[] getTuesday() {
        return tuesday;
    }

    public void setTuesday(int[] tuesday) {
        this.tuesday = tuesday;
    }

    public int[] getWednesday() {
        return wednesday;
    }

    public void setWednesday(int[] wednesday) {
        this.wednesday = wednesday;
    }

    public int[] getThursday() {
        return thursday;
    }

    public void setThursday(int[] thursday) {
        this.thursday = thursday;
    }

    public int[] getFriday() {
        return friday;
    }

    public void setFriday(int[] friday) {
        this.friday = friday;
    }

    public int[] getSaturday() {
        return saturday;
    }

    public void setSaturday(int[] saturday) {
        this.saturday = saturday;
    }

    public int[] getSunday() {
        return sunday;
    }

    public void setSunday(int[] sunday) {
        this.sunday = sunday;
    }

    public int[] getLunchMo() {
        return lunchMo;
    }

    public void setLunchMo(int[] lunchMo) {
        this.lunchMo = lunchMo;
    }

    public int[] getLunchTu() {
        return lunchTu;
    }

    public void setLunchTu(int[] lunchTu) {
        this.lunchTu = lunchTu;
    }

    public int[] getLunchWe() {
        return lunchWe;
    }

    public void setLunchWe(int[] lunchWe) {
        this.lunchWe = lunchWe;
    }

    public int[] getLunchTh() {
        return lunchTh;
    }

    public void setLunchTh(int[] lunchTh) {
        this.lunchTh = lunchTh;
    }

    public int[] getLunchFr() {
        return lunchFr;
    }

    public void setLunchFr(int[] lunchFr) {
        this.lunchFr = lunchFr;
    }

    public int[] getLunchSa() {
        return lunchSa;
    }

    public void setLunchSa(int[] lunchSa) {
        this.lunchSa = lunchSa;
    }

    public int[] getLunchSu() {
        return lunchSu;
    }

    public void setLunchSu(int[] lunchSu) {
        this.lunchSu = lunchSu;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
