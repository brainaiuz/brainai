package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 31/07/12
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
@Entity
    @Table(schema = EdsScope.PRIVATE_SCHEMA, name = "studentattended")
public class EdsStudentAttended extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private EdsStudent student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructorScheduledCourse_id")
    private EdsInstructorScheduledCourse instructorScheduledCourse;

    @Column(name="attended")
    private Boolean attended = Boolean.FALSE;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsStudent getStudent() {
        return student;
    }

    public void setStudent(EdsStudent student) {
        this.student = student;
    }

    public EdsInstructorScheduledCourse getInstructorScheduledCourse() {
        return instructorScheduledCourse;
    }

    public void setInstructorScheduledCourse(EdsInstructorScheduledCourse instructorScheduledCourse) {
        this.instructorScheduledCourse = instructorScheduledCourse;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
