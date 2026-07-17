package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDemoRequestDto extends DynamicDto {
    private DynamicDto sys_approval_1;
    private DynamicDto sys_table_for_information;

    public TripDemoRequestDto() {
    }

    public TripDemoRequestDto(DynamicDto sys_approval_1, DynamicDto sys_table_for_information) {
        this.sys_approval_1 = sys_approval_1;
        this.sys_table_for_information = sys_table_for_information;
    }

    public DynamicDto getSys_approval_1() {
        return sys_approval_1;
    }

    public void setSys_approval_1(DynamicDto sys_approval_1) {
        this.sys_approval_1 = sys_approval_1;
    }

    public DynamicDto getSys_table_for_information() {
        return sys_table_for_information;
    }

    public void setSys_table_for_information(DynamicDto sys_table_for_information) {
        this.sys_table_for_information = sys_table_for_information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripDemoRequestDto)) return false;

        TripDemoRequestDto that = (TripDemoRequestDto) o;

        if (sys_approval_1 != null ? !sys_approval_1.equals(that.sys_approval_1) : that.sys_approval_1 != null)
            return false;
        if (sys_table_for_information != null ? !sys_table_for_information.equals(that.sys_table_for_information) : that.sys_table_for_information != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sys_approval_1 != null ? sys_approval_1.hashCode() : 0;
        result = 31 * result + (sys_table_for_information != null ? sys_table_for_information.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TripDemoRequestDto{" +
                "sys_approval_1=" + sys_approval_1 +
                ", sys_table_for_information=" + sys_table_for_information +
                '}';
    }
}
