package com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseItem implements IsSerializable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String NUMBER = "number";
    public static final String COURSE = "course";
    public static final String LANGUAGE = "language";
    public static final String LOCATION = "location";
    public static final String START_DATE = "startdate";
    public static final String END_DATE = "enddate";
    public static final String DURATION = "duration";
    public static final String COUNT_OF_SETS = "count_of_sets";
    public static final String AVAILABLE_SET = "available_set";
    public static final String SESSION = "session";
    public static final String VISIBILITY = "visibility";
    public static final String INSTRUCTOR = "instructor";
    public static final String ASSESSOR = "assessor";
    public static final String COURSE_SCHEDULE_STATUS = "status";
    public static final String COURSE_REQUIREMENT = "courserequirement";
    public static final String TEST_OPTION = "testoption";
    public static final String CSV_DOWNLOADABLE = "csvdownloadable";
    public static final String COUNT_OF_STUDENT = "count_of_student";
    public static final String COUNT_OF_CONFIRMED_STUDENT = "count_of_confirmed_student";

    private Integer objectID;

    private String number;

    private Integer customerID;
    private String customer;

    private Integer quoteID;
    private String quote;

    private Integer courseID;
    private String courseName;
    private SelectItem course;

    private Boolean enableOvertime;

    private Date startDate;
    private Date endDate;

    private RecurrenceJobItem recurrenceJobItem;
    private Integer recurrenceId;
    private Date fireTime;

    private Integer instructorID;
    private String instructorName;
    private SelectItem instructor;

    private Integer assessorID;
    private String assessorName;
    private SelectItem assessor;

    private String venue;

    private Integer locationID;
    private String locationName;
    private SelectItem location;

    private Integer sessionID;
    private String sessionName;
    private SelectItem session;

    private Integer numberOfSeats;

    private Integer availableSets;

    private Integer duration;

    private Integer scheduleDuration;//about day(s)

    private Integer visibility;

    private Integer languageID;
    private String languageName;
    private SelectItem language;

    private Integer statusID;
    private String statusCode;
    private String statusName;
    private SelectItem status;

    private SelectItem[] courses;
    private SelectItem[] assessors;
    private SelectItem[] instructors;
    private SelectItem[] sessions;
    private SelectItem[] locations;
    private SelectItem[] languages;
    private SelectItem[] invoices;

    private HashMap<Integer, TimeSlotItem> timeSlotItems;

    private ScheduledCourseReservation[] reservations;
    private CourseRequirementItem[] courseRequirementItems;

    private ArrayList<Date> clonedDateList;

    private BigDecimal price;
    private BigDecimal stopFee;

    private String testOption;
    private String courseRequirementsAsString;

    private boolean hasInvoice = false;
    private boolean isStudentAttended = false;
    private Integer countOfStudent;
    private Integer countOfConfirmedStudent;
    private Integer countOfNotAddressedStudent;
    private Date createdDate;
    private Date modifiedDate;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public ScheduledCourseItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    public Boolean isEnabledOvertime() {
        return enableOvertime != null ? enableOvertime : Boolean.FALSE;
    }

    public void setEnableOvertime(Boolean enableOvertime) {
        this.enableOvertime = enableOvertime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(Integer instructorID) {
        this.instructorID = instructorID;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }


    public Integer getSessionID() {
        return sessionID;
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(Integer quoteID) {
        this.quoteID = quoteID;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getAvailableSets() {
        return availableSets;
    }

    public void setAvailableSets(Integer availableSets) {
        this.availableSets = availableSets;
    }

    public SelectItem[] getCourses() {
        return courses;
    }

    public void setCourses(SelectItem[] courses) {
        this.courses = courses;
    }

    public SelectItem[] getInstructors() {
        return instructors;
    }

    public void setInstructors(SelectItem[] instructors) {
        this.instructors = instructors;
    }

    public SelectItem[] getSessions() {
        return sessions;
    }

    public void setSessions(SelectItem[] sessions) {
        this.sessions = sessions;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public SelectItem[] getLanguages() {
        return languages;
    }

    public void setLanguages(SelectItem[] languages) {
        this.languages = languages;
    }

    public Integer getLanguageID() {
        return languageID;
    }

    public void setLanguageID(Integer languageID) {
        this.languageID = languageID;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public ScheduledCourseReservation[] getReservations() {
        return reservations;
    }

    public String getReservationAsCommoDelimited() {
        StringBuilder builder = new StringBuilder();

        if (reservations != null) {
            boolean isFirst = true;
            for (ScheduledCourseReservation reservation : reservations) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(",");
                }

                builder.append(reservation.getObjectID());
            }

            return builder.toString();
        }

        return null;
    }

    public void setReservations(ScheduledCourseReservation[] reservations) {
        this.reservations = reservations;
    }

    public ScheduledCourseReservation getReservation(Integer categoryID) {
        if (reservations != null && reservations.length != 0) {
            for (ScheduledCourseReservation reservation : reservations) {
                if (categoryID.equals(reservation.getItemCategoryID())) {
                    return reservation;
                }
            }
        }
        return null;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }


    public HashMap<Integer, TimeSlotItem> getTimeSlotItems() {
        return timeSlotItems;
    }

    public void setTimeSlotItems(HashMap<Integer, TimeSlotItem> timeSlotItems) {
        this.timeSlotItems = timeSlotItems;
    }

    public Integer getScheduleDuration() {
        return scheduleDuration;
    }

    public void setScheduleDuration(Integer scheduleDuration) {
        this.scheduleDuration = scheduleDuration;
    }

    public CourseRequirementItem[] getCourseRequirementItems() {
        return courseRequirementItems;
    }

    public void setCourseRequirementItems(CourseRequirementItem[] courseRequirementItems) {
        this.courseRequirementItems = courseRequirementItems;
    }

    public ArrayList<Date> getClonedDateList() {
        return clonedDateList;
    }

    public void setClonedDateList(ArrayList<Date> clonedDateList) {
        this.clonedDateList = clonedDateList;
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

    public String getTestOption() {
        return testOption;
    }

    public void setTestOption(String testOption) {
        this.testOption = testOption;
    }

    public String getCourseRequirementsAsString() {
        return courseRequirementsAsString;
    }

    public boolean isStudentAttended() {
        return isStudentAttended;
    }

    public void setStudentAttended(boolean studentAttended) {
        isStudentAttended = studentAttended;
    }

    public void setCourseRequirementsAsString(String courseRequirementsAsString) {
        this.courseRequirementsAsString = courseRequirementsAsString;
    }

    public Integer getAssessorID() {
        return assessorID;
    }

    public void setAssessorID(Integer assessorID) {
        this.assessorID = assessorID;
    }

    public String getAssessorName() {
        return assessorName;
    }

    public void setAssessorName(String assessorName) {
        this.assessorName = assessorName;
    }

    public SelectItem[] getAssessors() {
        return assessors;
    }

    public void setAssessors(SelectItem[] assessors) {
        this.assessors = assessors;
    }

    public Integer getCountOfStudent() {
        return countOfStudent;
    }

    public void setCountOfStudent(Integer countOfStudent) {
        this.countOfStudent = countOfStudent;
    }

    public Integer getCountOfConfirmedStudent() {
        return countOfConfirmedStudent;
    }

    public void setCountOfConfirmedStudent(Integer countOfConfirmedStudent) {
        this.countOfConfirmedStudent = countOfConfirmedStudent;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean hasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(boolean hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public Integer getCountOfNotAddressedStudent() {
        return countOfNotAddressedStudent;
    }

    public void setCountOfNotAddressedStudent(Integer countOfNotAddressedStudent) {
        this.countOfNotAddressedStudent = countOfNotAddressedStudent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<ScheduledCourseItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ScheduledCourseItem item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    public SelectItem[] getInvoices() {
        return invoices;
    }

    public void setInvoices(SelectItem[] invoices) {
        this.invoices = invoices;
    }


    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }


    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }


    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public SelectItem getCourse() {
        return course;
    }

    public void setCourse(SelectItem course) {
        this.course = course;
    }

    public SelectItem getInstructor() {
        return instructor;
    }

    public void setInstructor(SelectItem instructor) {
        this.instructor = instructor;
    }

    public SelectItem getAssessor() {
        return assessor;
    }

    public void setAssessor(SelectItem assessor) {
        this.assessor = assessor;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getSession() {
        return session;
    }

    public void setSession(SelectItem session) {
        this.session = session;
    }

    public SelectItem getLanguage() {
        return language;
    }

    public void setLanguage(SelectItem language) {
        this.language = language;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }
}
