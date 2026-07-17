package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsPassportCourse;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.PassportCourseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 23/06/14
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
@Repository("passportCourseManager")
public class PassportCourseManagerImpl extends BaseManager<EdsPassportCourse> implements PassportCourseManager {
    public PassportCourseManagerImpl() {
        super(EdsPassportCourse.class);
    }

    @Override
    public List<EdsPassportCourse> getPassportCourses(Integer passportID) {
        return find("SELECT pc FROM EdsPassportCourse pc WHERE pc.deleted<>true AND pc.passport.objectID=?", passportID);
    }

    @Override
    public void deletePassportCourses(Integer passportID) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(getCompanyId()).append(".passportCourse set deleted = true where passport_id = " + passportID);
        updateNative(sql.toString());
    }
}
