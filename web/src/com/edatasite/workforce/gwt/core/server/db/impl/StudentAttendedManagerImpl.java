package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.trainingcenter.EdsStudentAttended;
import com.edatasite.workforce.gwt.core.server.db.StudentAttendedManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 31/07/12
 * Time: 15:08
 * To change this template use File | Settings | File Templates.
 */
@Repository("studentAttendedManager")
public class StudentAttendedManagerImpl extends BaseManager<EdsStudentAttended> implements StudentAttendedManager {
    public StudentAttendedManagerImpl() {
        super(EdsStudentAttended.class);
    }

    @Override
    public Map<Integer, Object[]> getStudentsAttended(Integer scheduleCourseID, String studentIDs) {
        List<Object[]> studentAttendedList = findNative("SELECT id,student_id,attended FROM " + getCompanyId() + ".studentattended WHERE instructorScheduledCourse_id=? AND student_id IN " + studentIDs, scheduleCourseID);
        Map<Integer, Object[]> studentAttendedMap = new HashMap<>();// key studentId
        for (Object[] objects : studentAttendedList) {
            studentAttendedMap.put((Integer) objects[1], objects);
        }
        return studentAttendedMap;
    }

    @Override
    public void updateStudentAttended(List<StudentItem> studentItemList, Integer insSchCourseId) {
        StringBuffer updateSqlQuery = new StringBuffer();
        int i = 0;
        for (StudentItem studentItem : studentItemList) {
            if (studentItem.getStudentAttendedId() != null) {
                updateSqlQuery.append("UPDATE " + getCompanyId() + ".studentattended SET attended=" + studentItem.isAttended() + " WHERE id=" + studentItem.getStudentAttendedId() + ";\n");
                i++;
                if (i >= 50) {
                    updateNative(updateSqlQuery.toString());
                    i = 0;
                    updateSqlQuery = new StringBuffer();
                }
            }
        }
        if (i != 0) {
            updateNative(updateSqlQuery.toString());
        }
    }
}
