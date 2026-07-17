package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * User: Abror Abdukadirov
 * Date: 10.02.2020 19:02
 */
public class TimeslotDTO extends ResponseData {
    private Long driver_id;
    private Long companyId;
    private String name;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private int[] monday;
    private int[] tuesday;
    private int[] wednesday;
    private int[] thursday;
    private int[] friday;
    private int[] saturday;
    private int[] sunday;
    private Integer kpi_event_id;
    private Integer timeSlotId;

    public TimeslotDTO(long companyId, String nameUz, String nameRu, String nameEn) {
        this.companyId =companyId;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn =nameEn;


    }

    public TimeslotDTO(long companyId,Integer timeSlotId, String nameUz, String nameRu, String nameEn) {
        this.companyId =companyId;
        this.timeSlotId = timeSlotId;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn =nameEn;


    }

    public boolean hasTimeslot() {
        return (this.getMonday().length > 0
                && this.getTuesday().length > 0
                && this.getWednesday().length > 0
                && this.getThursday().length > 0
                && this.getFriday().length > 0
                && this.getSaturday().length > 0
                && this.getSunday().length > 0);
    }

    public Long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Long driver_id) {
        this.driver_id = driver_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUz() {
        return nameUz;
    }
    public Long getCompanyId() {
        return companyId;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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

    public Integer getKpi_event_id() {
        return kpi_event_id;
    }

    public void setKpi_event_id(Integer kpi_event_id) {
        this.kpi_event_id = kpi_event_id;
    }

    public boolean hasEvent() {
        return this.getKpi_event_id() != null;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeslotId) {
        this.timeSlotId = timeslotId;
    }
}
