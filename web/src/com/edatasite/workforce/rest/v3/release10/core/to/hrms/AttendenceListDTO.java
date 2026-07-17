package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class AttendenceListDTO extends ResponseData {

    private ArrayList<AttendenceDTO> attendences;

    public ArrayList<AttendenceDTO> getAttendences() {
        return attendences;
    }

    public void setAttendences(ArrayList<AttendenceDTO> attendences) {
        this.attendences = attendences;
    }
}
