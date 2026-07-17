package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/25/12
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "instructorScheduledCourse",
        indexes = {
                @Index(columnList = "scheduled_course_id", name = "instructorScheduledCourse_course_id_index"),
                @Index(columnList = "instructor_id", name = "instructorScheduledCourse_instructor_id_index")
        })
public class EdsInstructorScheduledCourse extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private EdsEmployee instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_course_id")
    private EdsCourseSchedule courseSchedule;

    private Date date;

    private Date endTime;

    private boolean attended;
    private boolean approved;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getInstructor() {
        return instructor;
    }

    public void setInstructor(EdsEmployee instructor) {
        this.instructor = instructor;
    }

    public EdsCourseSchedule getCourseSchedule() {
        return courseSchedule;
    }

    public void setCourseSchedule(EdsCourseSchedule courseSchedule) {
        this.courseSchedule = courseSchedule;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
