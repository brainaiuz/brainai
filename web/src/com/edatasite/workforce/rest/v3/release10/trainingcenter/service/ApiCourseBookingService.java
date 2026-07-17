package com.edatasite.workforce.rest.v3.release10.trainingcenter.service;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.server.db.CourseManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseSubjectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseScheduleListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseBookingDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseSubjectsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiCourseBookingService {
    @Autowired
    private TCService tcService;
    @Autowired
    private CourseManager courseManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;
    @Autowired
    private StudentManager studentManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;

    private static final Logger log = LoggerFactory.getLogger(ApiCourseBookingService.class);

    @Transactional
    public Integer create(CourseBookingDto courseBookingDto) {
        if (courseBookingDto.getCrmAccountId() == null) {
            throw new IllegalArgumentException("Crm Account Id is required");
        }
        if (courseBookingDto.getScheduledCourseId() == null) {
            throw new IllegalArgumentException("Scheduled Course Id is required");
        }
        EdsCourseSchedule edsCourseSchedule = scheduledCourseManager.get(courseBookingDto.getScheduledCourseId());
        if (edsCourseSchedule == null) {
            throw new IllegalArgumentException("Scheduled Course Id " + courseBookingDto.getScheduledCourseId() + " not found");
        }
        EdsStudent edsstudent = studentManager.getStudentByCrmAccountId(courseBookingDto.getCrmAccountId());
        if (edsstudent == null) {
            throw new IllegalArgumentException("Student not found");
        }
        Integer bookingId = courseScheduleStudentManager.getBookingIdByScheduledCourseId(courseBookingDto.getScheduledCourseId());
        CourseBookingItem cbItem = new CourseBookingItem();

        if (bookingId != null) {
            cbItem.setObjectID(bookingId);
            saveCourseBookingAttendedStudents(edsCourseSchedule, edsstudent, cbItem);
            return bookingId;
        } else {
            cbItem.setStatusCode("BOOKING_DRAFT");
            cbItem.setTypeID(courseBookingDto.getTypeId());
            if (courseBookingDto.getLocaitionId() != null) {
                cbItem.setLocation(new SelectItem(courseBookingDto.getLocaitionId()));
            }
            if (edsCourseSchedule.getInstructor() != null
                    && edsCourseSchedule.getInstructor().getContact() != null && edsCourseSchedule.getInstructor().getContact().getCrmAccount() != null) {
                cbItem.setCustomer(new SelectItem(edsCourseSchedule.getInstructor().getContact().getCrmAccount().getObjectID()));
            }

            Integer[] integers = tcService.saveCourseBooking(cbItem);
            cbItem.setObjectID(integers[0]);

            saveCourseBookingAttendedStudents(edsCourseSchedule, edsstudent, cbItem);
            return integers[0];
        }
    }

    public boolean cancelStudentCourseBooking(Integer crmAccountId, Integer scheduledCourseId) {
        try {
            EdsStudent edsstudent = studentManager.getStudentByCrmAccountId(crmAccountId);
            tcService.cancelGymStudentCourseBooking(edsstudent.getObjectID(), scheduledCourseId);
            return true;
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Student not found", e);
        } catch (Exception e) {
            log.error("Error while canceling course booking for crmAccountId: {}", crmAccountId, e);
            return false;
        }
    }

    private void saveCourseBookingAttendedStudents(EdsCourseSchedule edsCourseSchedule, EdsStudent edsstudent, CourseBookingItem cbItem) {
        StudentItem studentItem = getStudentItems(edsstudent);
        CourseScheduleListItem courseScheduleItem = getCourseScheduleListItem(edsCourseSchedule);
        studentItem.getStudentCourseBookingItems().add(courseScheduleItem);
        cbItem.setStudentItems(new ArrayList<>(List.of(studentItem)));

        if (edsCourseSchedule.getInstructor() != null && edsCourseSchedule.getInstructor().getContact() != null) {
            ContactListItem contactListItem = new ContactListItem();
            contactListItem.setObjectId(edsCourseSchedule.getInstructor().getContact().getObjectID());
            contactListItem.setCheckForDuplicates(true);
            cbItem.setContactItems(contactListItem);
        }

        tcService.saveGymCourseBookingAttendedStudents(cbItem);
    }

    private static CourseScheduleListItem getCourseScheduleListItem(EdsCourseSchedule edsCourseSchedule) {
        CourseScheduleListItem courseScheduleItem = new CourseScheduleListItem();
        courseScheduleItem.setStartDate(edsCourseSchedule.getStartDate());
        courseScheduleItem.setEndDate(edsCourseSchedule.getEndDate());
        courseScheduleItem.setCourseScheduleId(edsCourseSchedule.getObjectID());
        courseScheduleItem.setCourseName(edsCourseSchedule.getCourse().getName());
        courseScheduleItem.setCourseCode(edsCourseSchedule.getCourse().getNumber());
        return courseScheduleItem;
    }

    private StudentItem getStudentItems(EdsStudent edsStudent) {
        StudentItem studentItem = new StudentItem();
        studentItem.setObjectId(edsStudent.getObjectID());
        studentItem.setDepartmentCode(edsStudent.getDepartmentCode());
        studentItem.setFirstName(edsStudent.getFullName());
        studentItem.setSafetyPPNumber(edsStudent.getSafetyPPNumber());
        studentItem.setCompEmpNum(edsStudent.getCompEmplNumber());
        studentItem.setPrimaryEmail(edsStudent.getEmail());
        studentItem.setPrimaryPhone(edsStudent.getPhone());
        studentItem.setWorkEmail(edsStudent.getEmail());
        return studentItem;
    }

    public ListResultTO<CourseSubjectsDto> getCourseSubjects() {
        ListResult<CourseSubjectItem> courseSubjectList = tcService.getCourseSubjectList(null);

        ArrayList<CourseSubjectsDto> courseScheduleDtoList = courseSubjectList.getList().stream()
                .map(it -> new CourseSubjectsDto(it.getObjectId(), it.getName(), it.getDescription()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new ListResultTO<>(courseSubjectList.getTotal(), courseScheduleDtoList);
    }

    @Transactional
    public Integer createCourseSubject(CourseSubjectsDto request) {
        CourseSubjectItem courseSubjectItem = new CourseSubjectItem();
        courseSubjectItem.setObjectId(request.getId());
        courseSubjectItem.setName(request.getName());
        courseSubjectItem.setDescription(request.getDescription());
        if (request.getParent() != null){
            courseSubjectItem.setParent(new SelectItem(request.getParent().getId(),request.getParent().getName()));
        }
        return tcService.saveCourseSubject(courseSubjectItem);
    }

    public Integer deleteCourseSubject(Integer subjectId) {
        return tcService.deleteCourseSubject(subjectId);
    }
}
