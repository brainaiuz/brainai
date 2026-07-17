package com.edatasite.workforce.rest.v3.release10.trainingcenter.service;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CourseManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseScheduleDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.END_AFTER_OCCURRENCES;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.END_BY_DATE;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.NO_END_DATE;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_DAILY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_MONTHLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_WEEKLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_YEARLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRING_COURSE_SCHEDULE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.FORMAT_WITH_DATETIME_AND_TIMEZONE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INVALID;

@Service
public class ApiCourseScheduleService {
    @Autowired
    private TCServiceLocal tcService;

    @Autowired
    private TCService tcService2;

    @Autowired
    private CourseManager courseManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;
    @Autowired
    private StudentManager studentManager;

    private final SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);


    public ListResultTO<CourseScheduleDto> getCourseScheduleFromSolr(ListingFilterParameter fp) {
        ListResult<ScheduledCourseItem> item = tcService.getCourseScheduleFromSolr(fp);

        ArrayList<CourseScheduleDto> courseScheduleDtoList = item.getList().stream()
                .map(it -> {
                    EdsCourse course = courseManager.getCourseById(it.getCourseID()); // Fetch course once
                    Integer courseScheduleStudentCount = courseScheduleStudentManager.getCourseScheduleStudentCount(it.getObjectID());
                    return new CourseScheduleDto(
                            it.getObjectID(),
                            new ItemDto(it.getLocationID(), it.getLocationName()),
                            new ItemDto(it.getLanguageID(), it.getLanguageName()),
                            new CourseDto(course.getObjectID(), course.getName(), course.getDuration().toString(), course.getValidity().toString()), // Fixed issue with CourseDto
                            new ItemDto(it.getInstructorID(), it.getInstructorName()),
                            it.getStartDate(),
                            new ItemDto(it.getAssessorID(), it.getAssessorName()),
                            it.getDuration().toString(),
                            it.getNumberOfSeats(),
                            it.isEnabledOvertime(),
                            courseScheduleStudentCount,
                            it.getCountOfConfirmedStudent()
                    );
                }).collect(Collectors.toCollection(ArrayList::new));

        return new ListResultTO<>(item.getTotal(), courseScheduleDtoList);
    }

    public CourseScheduleDto getById(Integer scheduleId) {
        EdsCourseSchedule courseSchedule = null;
        if (scheduleId != null) {
            courseSchedule = scheduledCourseManager.get(scheduleId);
        }
        EdsCourse course = courseManager.getCourseById(courseSchedule.getCourse().getObjectID()); // Fetch course once

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setScheduledCourseID(scheduleId);
        fp.setSearchButton(false);
        fp.setLimit(1000); //shoshilinchda qilindi ! managerda limit 0 bo'lib yedirib qoyadi
        Optional<ListResult<StudentItem>> scheduledCourseStudents = Optional.ofNullable(tcService2.getScheduledCourseStudents(fp));
        List<ItemDto> studentList = scheduledCourseStudents.map(listResult -> listResult.getList().stream()
                        .filter(Objects::nonNull)
                        .map(o -> {
                            ItemDto item = new ItemDto();
                            item.setId(o.getObjectId());
                            item.setName(o.getName());
                            item.getProperties().put("crmAccountId",
                                    o.getCrmAccount() != null ? o.getCrmAccount().getObjectId() : null);
                            return item;
                        })
                        .collect(Collectors.toList())
                )
                .orElseGet(ArrayList::new);

        return new CourseScheduleDto(
                courseSchedule.getObjectID(),
                new ItemDto(courseSchedule.getLocation().getObjectID(), courseSchedule.getLocation().getName()),
                new ItemDto(courseSchedule.getLanguage().getObjectID(), courseSchedule.getLanguage().getName()),
                new CourseDto(course.getObjectID(), course.getName(), course.getDuration().toString(), course.getValidity().toString()), // Fixed issue with CourseDto
                new ItemDto(courseSchedule.getInstructor().getObjectID(), courseSchedule.getInstructor().getName()),
                courseSchedule.getStartDate(),
                new ItemDto(courseSchedule.getAssessor() != null ? courseSchedule.getAssessor().getObjectID() : null, courseSchedule.getAssessor() != null ? courseSchedule.getAssessor().getName() : null),
                courseSchedule.getScheduleDuration().toString(),
                courseSchedule.getNumberOfSeats(),
                courseSchedule.getEnableOvertime(),
                studentList,
                studentList.size()
        );
    }

    public void save(CourseScheduleDto dto) throws RestException {

        Date endDate = null;
        ScheduledCourseItem scheduledCourseItem = new ScheduledCourseItem();
        scheduledCourseItem.setStartDate(dto.getStartDate());
        EdsCourse course = null;
        if (dto.getCourse() != null && dto.getCourse().getId() != null) {
            scheduledCourseItem.setCourseID(dto.getCourse().getId());
            course = courseManager.get(dto.getCourse().getId());
        }
        if (dto.getLocation() != null && dto.getLocation().getId() != null) {
            scheduledCourseItem.setLocationID(dto.getLocation().getId());
        }
        if (dto.getLanguage() != null && dto.getLanguage().getId() != null) {
            scheduledCourseItem.setLanguageID(dto.getLanguage().getId());
        }
        if (dto.getInstructor() != null && dto.getInstructor().getId() != null) {
            scheduledCourseItem.setInstructorID(dto.getInstructor().getId());
        }
        if (dto.getAssessor() != null && dto.getAssessor().getId() != null) {
            scheduledCourseItem.setAssessorID(dto.getAssessor().getId());
        }
        scheduledCourseItem.setNumberOfSeats(dto.getNumberOfSeats());
        if (dto.getStartDate() != null && course != null) {
            endDate = new Date(dto.getStartDate().getTime() + TimeUnit.HOURS.toMillis(course.getDuration()));
            scheduledCourseItem.setEndDate(endDate);
            scheduledCourseItem.setScheduleDuration(course.getDuration());
        }
        scheduledCourseItem.setEnableOvertime(false);

        if (dto.getRecurrence() != null) {

            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setJobType(RECURRING_COURSE_SCHEDULE);
            recurrenceJobItem.setStartDate(dto.getStartDate());
//            item.setBusObjectId(recurrenceItem.getBusObjectId());
            recurrenceJobItem.setEnabled(Boolean.TRUE);
            if (dto.getRecurrence().getRepeats() != null) {
                if ("DAILY".equalsIgnoreCase(dto.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_DAILY);
                    recurrenceJobItem.setInterval(dto.getRecurrence().getRepeats().getCount());
                    recurrenceJobItem.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);

                } else if ("WEEKLY".equalsIgnoreCase(dto.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_WEEKLY);
                    if (dto.getRecurrence().getRepeats().getSelected_days() != null) {

                        Map<String, String> selectedDays = dto.getRecurrence().getRepeats().getSelected_days()
                                .stream().collect(Collectors.toMap(dayOfWeek -> dayOfWeek, dayOfWeek -> dayOfWeek));

                        recurrenceJobItem.setInterval(1);
                        recurrenceJobItem.setSunday(selectedDays.get("SUNDAY") != null);
                        recurrenceJobItem.setMonday(selectedDays.get("MONDAY") != null);
                        recurrenceJobItem.setTuesday(selectedDays.get("TUESDAY") != null);
                        recurrenceJobItem.setWednesday(selectedDays.get("WEDNESDAY") != null);
                        recurrenceJobItem.setThursday(selectedDays.get("THURSDAY") != null);
                        recurrenceJobItem.setFriday(selectedDays.get("FRIDAY") != null);
                        recurrenceJobItem.setSaturday(selectedDays.get("SATURDAY") != null);
                    }
                } else if ("MONTHLY".equalsIgnoreCase(dto.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_MONTHLY);
                    recurrenceJobItem.setInterval(dto.getRecurrence().getRepeats().getCount());
                    recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);

                } else if ("YEARLY".equalsIgnoreCase(dto.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_YEARLY);
                    recurrenceJobItem.setInterval(dto.getRecurrence().getRepeats().getCount());
                    if (StringUtils.isNotBlank(dto.getRecurrence().getRepeats().getYearly_date())) {
                        try {
                            Date yearlyDate = dto.getStartDate();
                            Calendar yYear = new GregorianCalendar();
                            yYear.setTime(yearlyDate);
                            recurrenceJobItem.setMonthlyOrYearlyDay(yYear.get(Calendar.DAY_OF_MONTH)); // 15 of 31 (or 30 or 28-29) day of month
                            recurrenceJobItem.setYearlyMonth(yYear.get(Calendar.MONTH) + 1);
                        } catch (Exception e) {
                            throw new RestException("Invalid date format", "Invalid date format for yearly_date. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                        }

                    }
                    recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                }

            }
//            item.setUserTimeZone(getTimeZone(recurrenceJobItem.getStartDate()));

            //Until (End Date)
            if (dto.getRecurrence().getUntil() != null) {
                if ("DATE".equalsIgnoreCase(dto.getRecurrence().getUntil().getType())) {
                    recurrenceJobItem.setEndType(END_BY_DATE);
                    try {
                        recurrenceJobItem.setEndDate(longDateTimezoneFormat.parse(dto.getRecurrence().getUntil().getDate()));
                    } catch (Exception e) {
                        throw new RestException("Invalid date format", "Invalid date format for until. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                } else if ("NUMBER_EVENTS".equalsIgnoreCase(dto.getRecurrence().getUntil().getType())) {
                    recurrenceJobItem.setEndType(END_AFTER_OCCURRENCES);
                    if (dto.getRecurrence().getUntil().getOccurences() != null && dto.getRecurrence().getUntil().getOccurences() > 0) {
                        recurrenceJobItem.setOccurrence(dto.getRecurrence().getUntil().getOccurences());
                    } else {
                        recurrenceJobItem.setOccurrence(1);
                    }
                } else {
                    recurrenceJobItem.setEndType(NO_END_DATE);
                }
            }
            //End of Until (End Date)

            if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_MONTHLY)) {
                recurrenceJobItem.setMonthlyOrYearlyDay(scheduledCourseItem.getStartDate().getDate());
                recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                //recurrenceJobItem.setInterval(1);
            }
            Date normalizedEndDate = recurrenceJobItem.getEndDate();
            if (normalizedEndDate != null) {
                normalizedEndDate.setHours(23);
                normalizedEndDate.setMinutes(59);
                recurrenceJobItem.setEndDate(normalizedEndDate);

            }
            scheduledCourseItem.setRecurrenceJobItem(recurrenceJobItem);

        } else {
            scheduledCourseItem.setRecurrenceJobItem(null);
        }
        tcService2.saveCourseSchedule(scheduledCourseItem);
    }

    public ListResultTO<CourseScheduleDto> getCourseScheduleByStudentId(Integer studentId, String sortAs) throws RestException {
        EdsStudent edsstudent = studentManager.getStudentByCrmAccountId(studentId);
        if (edsstudent == null) {
            throw new RestException("Student Not Found by crmAccountId " + studentId, "Student Not Found by crmAccountId ", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        List<ScheduledCourseItem> items = courseScheduleStudentManager.getScheduleListByStudentId(edsstudent.getObjectID(), sortAs).stream().map(EdsCourseSchedule::getRPC).toList();
        ArrayList<CourseScheduleDto> courseScheduleDtoList = items.stream()
                .map(it -> {
                    EdsCourse course = courseManager.getCourseById(it.getCourseID()); // Fetch course once

                    return new CourseScheduleDto(
                            it.getObjectID(),
                            new ItemDto(it.getLocationID(), it.getLocationName()),
                            new ItemDto(it.getLanguageID(), it.getLanguageName()),
                            new CourseDto(course.getObjectID(), course.getName(), course.getDuration().toString(), course.getValidity().toString()), // Fixed issue with CourseDto
                            new ItemDto(it.getInstructorID(), it.getInstructorName()),
                            it.getStartDate(),
                            new ItemDto(it.getAssessorID(), it.getAssessorName()),
                            it.getDuration().toString(),
                            it.getNumberOfSeats(),
                            it.isEnabledOvertime(),
                            it.getCountOfStudent(),
                            it.getCountOfConfirmedStudent()
                    );
                }).collect(Collectors.toCollection(ArrayList::new));

        return new ListResultTO<>(items.size(), courseScheduleDtoList);
    }
}
