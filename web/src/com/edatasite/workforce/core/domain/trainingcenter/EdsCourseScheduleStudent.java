package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "courseschedulestudent")
public class EdsCourseScheduleStudent extends EdsTraceable {

    public static String STUDENT_COURSE_SCHEDULE_PARENT_STATUS = "_STUDENT_COURSE_SCHEDULE_STATUS";
    public static String STUDENT_COURSE_SCHEDULE_PENDING = "_STUDENT_COURSE_SCHEDULE_PENDING";
    public static String STUDENT_COURSE_SCHEDULE_REJECTED = "_STUDENT_COURSE_SCHEDULE_REJECTED";
    public static String STUDENT_COURSE_SCHEDULE_ATTENDED = "_STUDENT_COURSE_SCHEDULE_ATTENDED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coursebooking_id")
    private EdsCourseBooking courseBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseschedule_id")
    private EdsCourseSchedule courseScheduleBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private EdsStudent student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stutus_id")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_stutus_id")
    private EdsReference examStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attended_status_id")
    private EdsReference attendedStatus;

    //this parameters for the temporary lock of a seats
    private String itemUUID;//{booking item uuid}

    private Boolean isPreRequisite = Boolean.FALSE;

    @Column(name = "price", precision = 14, scale = 4)
    private BigDecimal price;

    @Column(name = "stopFee", precision = 14, scale = 4)
    private BigDecimal stopFee;

    @Column(name = "droppable")
    private Boolean droppable = Boolean.FALSE;

    @Column(name = "grade")
    private String grade;

    @Column(name = "points")
    private String points;

    private Integer invoiceID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCourseBooking getCourseBooking() {
        return courseBooking;
    }

    public void setCourseBooking(EdsCourseBooking courseBooking) {
        this.courseBooking = courseBooking;
    }

    public EdsCourseSchedule getCourseScheduleBooking() {
        return courseScheduleBooking;
    }

    public void setCourseScheduleBooking(EdsCourseSchedule courseScheduleBooking) {
        this.courseScheduleBooking = courseScheduleBooking;
    }

    public EdsStudent getStudent() {
        return student;
    }

    public void setStudent(EdsStudent student) {
        this.student = student;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsReference getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(EdsReference examStatus) {
        this.examStatus = examStatus;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public Boolean getPreRequisite() {
        return isPreRequisite;
    }

    public void setPreRequisite(Boolean preRequisite) {
        isPreRequisite = preRequisite;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStopFee() {
        return stopFee;
    }

    public void setStopFee(BigDecimal stopFee) {
        this.stopFee = stopFee;
    }

    public Boolean getDroppable() {
        return droppable;
    }

    public void setDroppable(Boolean droppable) {
        this.droppable = droppable;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public EdsReference getAttendedStatus() {
        return attendedStatus;
    }

    public void setAttendedStatus(EdsReference attendedStatus) {
        this.attendedStatus = attendedStatus;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public StudentAsInvoiceItem createStudentAsInvoiceItem(EdsCourseScheduleStudent css) {
        StudentAsInvoiceItem item = new StudentAsInvoiceItem();
        item.setObjectID(css.getObjectID());
        item.setStudentID(css.getStudent() != null ? css.getStudent().getObjectID() : null);
        if (css.getStudent() != null && css.getStudent().getContact() != null) {
            item.setFirstName(css.getStudent().getContact().getFirstName());
            item.setLastName(css.getStudent().getContact().getLastName());
        }
        if (css.getCourseScheduleBooking() != null) {
            item.setCourseScheduleID(css.getCourseScheduleBooking().getObjectID());
            item.setCourseScheduleNumber(css.getCourseScheduleBooking().getNumber());

            item.setCourseID(css.getCourseScheduleBooking().getCourse().getObjectID());
            item.setCourseCode(css.getCourseScheduleBooking().getCourse().getNumber());
        }

        if (css.getCourseBooking() != null) {
            item.setCourseBookingID(css.getCourseBooking().getObjectID());
            item.setCourseBookingNumber(css.getCourseBooking().getNumber());
        }
        item.setPrice(css.getPrice());
        item.setStopFee(css.getStopFee());
        return item;
    }

    @Override
    public Map<String, Object> getPropertyValueAsMap() {
        String name = "";
        String email = "";
        String phone = "";
        String newCS = "";
        if (getStudent() != null && getStudent().getContact() != null && getStudent().getContact().getFullName() != null) {
            name = getStudent().getContact().getFullName();
        }
        if (getStudent() != null && getStudent().getContact() != null && getStudent().getContact().getPrimaryEmail() != null) {
            email = getStudent().getContact().getPrimaryEmail();
        }
        if (getStudent() != null && getStudent().getContact() != null && getStudent().getContact().getPrimaryPhone() != null) {
            phone = getStudent().getContact().getPrimaryPhone().replaceAll("\\||w|\\+", "");
        }
        if (getCourseScheduleBooking() != null && getCourseScheduleBooking().getNumber() != null) {
            newCS = getCourseScheduleBooking().getNumber();
        }
        addFieldIDValue(EmailTemplateConstants.TRAINING_CENTER.SCHEDULED_COURSE_STUDENT.STUDENT_EMAIL, email);
        addFieldIDValue(EmailTemplateConstants.TRAINING_CENTER.SCHEDULED_COURSE_STUDENT.STUDENT_PHONE, phone);
        addFieldIDValue(EmailTemplateConstants.TRAINING_CENTER.SCHEDULED_COURSE_STUDENT.STUDENT_NEW_CS, newCS);
        addFieldIDValue(EmailTemplateConstants.TRAINING_CENTER.SCHEDULED_COURSE_STUDENT.STUDENT_NAME, name);
        return fieldIDValue;
    }
}
