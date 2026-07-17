package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.trainingcenter.EdsStudentAttended;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 31/07/12
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public interface StudentAttendedManager extends Manager<EdsStudentAttended> {
    Map<Integer,Object[]> getStudentsAttended(Integer scheduleCourseID, String studentIDs);

    void updateStudentAttended(List<StudentItem> studentItemList, Integer insSchCourseId);
}
