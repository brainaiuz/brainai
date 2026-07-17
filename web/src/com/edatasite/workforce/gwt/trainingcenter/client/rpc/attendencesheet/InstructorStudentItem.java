package com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 27/07/12
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class InstructorStudentItem implements IsSerializable {
    private InstructorItem instructorItems;
    private ArrayList<StudentItem> studentItems;
    private SelectItem[] attendedStatusList;

    public InstructorItem getInstructorItems() {
        return instructorItems;
    }

    public void setInstructorItems(InstructorItem instructorItems) {
        this.instructorItems = instructorItems;
    }

    public ArrayList<StudentItem> getStudentItems() {
        if(studentItems == null){
            studentItems = new ArrayList<>();
        }
        return studentItems;
    }

    public void setStudentItems(ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
    }

    public SelectItem[] getAttendedStatusList() {
        return attendedStatusList;
    }

    public void setAttendedStatusList(SelectItem[] attendedStatusList) {
        this.attendedStatusList = attendedStatusList;
    }
}
