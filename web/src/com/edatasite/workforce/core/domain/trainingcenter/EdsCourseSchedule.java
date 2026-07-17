package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsScheduledCourseCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseReservation;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "scheduledcourse",
        indexes = {
                @Index(columnList = "course_id", name = "scheduledcourse_course_id_index"),
                @Index(columnList = "instructor_id", name = "scheduledcourse_instructor_id_index")
        })
public class EdsCourseSchedule extends EdsTraceable implements Constants {

    public static final String STUDENT_COURSE_STATUS = "_STUDENT_COURSE_STATUS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private EdsCrmAccount customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private EdsSaleQuote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private EdsCourse course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private EdsReference session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private EdsReference language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private EdsEmployee instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessor_id")
    private EdsEmployee assessor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "courseSchedule")
    private List<EdsInstructorScheduledCourse> instructorScheduledCourses = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "courseScheduleBooking")
//    private List<EdsCourseScheduleStudent> courseScheduleStudents = new ArrayList<EdsCourseScheduleStudent>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "courseschedule_reservation",
            joinColumns = {@JoinColumn(name = "courseschedule_id")},
            inverseJoinColumns = {@JoinColumn(name = "reservation_id")})
    private List<EdsBookingItemReservation> reservations = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "courseschedule_invoice",
            joinColumns = {@JoinColumn(name = "courseschedule_id")},
            inverseJoinColumns = {@JoinColumn(name = "invoice_id")})
    private List<EdsSaleInvoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsReference status;

    private String venue;

    private Boolean enableOvertime;

    private Date startDate;
    private Date endDate;

    private Integer scheduleDuration;//duration day(s)

    private Integer numberOfSeats;

    private Integer visibility;

    private Date expireDate;

    private String testOption;

    @Column(name = "number")
    private String number;

    @Column(name = "price", precision = 14, scale = 4)
    private BigDecimal price;

    @Column(name = "stopFee", precision = 14, scale = 4)
    private BigDecimal stopFee;

    @Column(name = "intNumber")
    private Integer intNumber;

    private Integer eventID;

    private Boolean deleted = false;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsScheduledCourseCustomFields customFields;

    @Column(name = "recurrenceId")
    private Integer recurrenceID;

    @Column(name = "fireTime")
    private Date fireTime;

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.NUMBER);
        }
        this.number = number;
    }

    public EdsCrmAccount getCustomer() {
        return customer;
    }

    public void setCustomer(EdsCrmAccount customer) {
        this.customer = customer;
    }

    public EdsSaleQuote getQuote() {
        return quote;
    }

    public void setQuote(EdsSaleQuote quote) {
        this.quote = quote;
    }

    public EdsCourse getCourse() {
        return course;
    }

    public void setCourse(EdsCourse course) {
        if (!ServerUtils.equalsEdsObject(this.course, course)) {
            addChange(CustomFormConstants.TRAINING_CENTER.COURSE);
        }
        this.course = course;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }
    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        if (!ServerUtils.equalsEdsObject(this.location, location)) {
            addChange(CustomFormConstants.TRAINING_CENTER.LOCATION);
        }
        this.location = location;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Boolean getEnableOvertime() {
        return enableOvertime != null ? enableOvertime : Boolean.FALSE;
    }

    public void setEnableOvertime(Boolean enableOvertime) {
        this.enableOvertime = enableOvertime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(startDate);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
        if ((this.startDate == null && startDate != null) || (this.startDate != null && startDate == null) || !this.startDate.equals(startDate)) {
            addChange(CustomFormConstants.START_DATE);
        }
        this.startDate = startDate;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer countOfSets) {
        if (!ServerUtils.equalsInteger(this.numberOfSeats, countOfSets)) {
            addChange(CustomFormConstants.TRAINING_CENTER.NUMBER_OF_SEATS);
        }
        this.numberOfSeats = countOfSets;
    }

    public Integer getScheduleDuration() {
        return scheduleDuration;
    }

    public void setScheduleDuration(Integer scheduleDuration) {
        this.scheduleDuration = scheduleDuration;
    }

    public EdsReference getSession() {
        return session;
    }

    public void setSession(EdsReference session) {
        this.session = session;
    }

    public EdsEmployee getInstructor() {
        return instructor;
    }

    public void setInstructor(EdsEmployee instructor) {
        if (!ServerUtils.equalsEdsObject(this.instructor, instructor)) {
            addChange(CustomFormConstants.TRAINING_CENTER.INSTRUCTOR);
        }
        this.instructor = instructor;
    }

    public List<EdsInstructorScheduledCourse> getInstructorScheduledCourses() {
        return instructorScheduledCourses;
    }

//    public List<EdsCourseScheduleStudent> getCourseScheduleStudents() {
//        return courseScheduleStudents;
//    }
//
//    public void setCourseScheduleStudents(List<EdsCourseScheduleStudent> courseScheduleStudents) {
//        this.courseScheduleStudents = courseScheduleStudents;
//    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public EdsReference getLanguage() {
        return language;
    }

    public void setLanguage(EdsReference language) {
        this.language = language;
    }

    public List<EdsBookingItemReservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<EdsBookingItemReservation> reservations) {
        this.reservations = reservations;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getTestOption() {
        return testOption;
    }

    public void setTestOption(String testOption) {
        this.testOption = testOption;
    }

    public EdsScheduledCourseCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsScheduledCourseCustomFields customFields) {
        this.customFields = customFields;
    }

    @Override
    public Integer getObjectID() {
        return objectID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();

        if (getNumber() != null) {
            builder.append(getNumber()).append(" ");
        }
        if (course != null) {
            builder.append(course.getName());
        }

        return builder.toString();
    }

    public EdsEmployee getAssessor() {
        return assessor;
    }

    public void setAssessor(EdsEmployee assessor) {
        this.assessor = assessor;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getClassRoom() {
        if (getReservations() != null && getReservations().size() > 0) {
            for (EdsBookingItemReservation rv : getReservations()) {
                EdsBookingItem bitem = rv.getBookingItem();
                if (BOOKING_ROOM_CATEGORY.equals(bitem.getCategory().getCode())) {
                    return bitem.getName();
                }
            }
        }

        return null;
    }

    public String getLocationAsString() {
        if (location != null) {
            String _location = location.getCountry().getName();

            if (location.getState() != null) {
                _location += " " + location.getState().getName();
            }

            if (location.getCity() != null && !location.getCity().isEmpty()) {
                _location += " " + location.getCity();
            }

            return _location;
        }

        return null;
    }

    public ScheduledCourseItem getRPC() {
        ScheduledCourseItem item = new ScheduledCourseItem();
        item.setObjectID(getObjectID());
        item.setNumber(getNumber());
        if (getCourse() != null) {
            item.setCourseID(getCourse().getObjectID());
            item.setCourseName(getCourse().getName());
            item.setDuration(getCourse().getDuration());
            SelectItem course = new SelectItem(getCourse().getObjectID(), getCourse().getName());
            course.setDescription(getCourse().getNumber());
            item.setCourse(course);
        }
        item.setNumberOfSeats(getNumberOfSeats());
        item.setEnableOvertime(getEnableOvertime());
        item.setStartDate(getStartDate());
        item.setEndDate(getEndDate());
        item.setScheduleDuration(getScheduleDuration());
        if (getInstructor() != null) {
            item.setInstructorID(getInstructor().getObjectID());
            item.setInstructorName(getInstructor().getFullName());
            item.setInstructor(new SelectItem(getInstructor().getObjectID(), getInstructor().getFullName()));
        }
        if (getAssessor() != null) {
            item.setAssessorID(getAssessor().getObjectID());
            item.setAssessorName(getAssessor().getFullName());
            item.setAssessor(new SelectItem(getAssessor().getObjectID(), getAssessor().getFullName()));
        }
        item.setPrice(getPrice());
        item.setStopFee(getStopFee());
        item.setTestOption(getTestOption());

        if (getLocation() != null) {
            item.setLocationID(getLocation().getObjectID());
            item.setLocationName(getLocationAsString());
            item.setLocation(new SelectItem(getLocation().getObjectID(), getLocation().getName()));
        }
        if (getLanguage() != null) {
            item.setLanguageID(getLanguage().getObjectID());
            item.setLanguageName(getLanguage().getName());
            item.setLanguage(new SelectItem(getLanguage().getObjectID(), getLanguage().getName()));
        }
        if (getStatus() != null) {
            item.setStatusID(getStatus().getObjectID());
            item.setStatusName(getStatus().getName());
            item.setStatusCode(getStatus().getCode());
            SelectItem status = new SelectItem(getStatus().getObjectID(), getStatus().getName());
            status.setCode(getStatus().getCode());
            item.setStatus(status);
        }
        if (getReservations() != null && getReservations().size() > 0) {
            List<ScheduledCourseReservation> courseReservations = new ArrayList<>();
            for (EdsBookingItemReservation reservation : getReservations()) {
                ScheduledCourseReservation courseReservation = new ScheduledCourseReservation();
                courseReservation.setObjectID(reservation.getObjectID());
                courseReservation.setItemCategoryID(reservation.getBookingItem().getCategory().getObjectID());
                courseReservation.setItemCategory(reservation.getBookingItem().getCategory().getName());
                courseReservation.setItemID(reservation.getBookingItem().getObjectID());
                courseReservation.setItem(reservation.getBookingItem().getName());
                courseReservations.add(courseReservation);
            }
            item.setReservations(courseReservations.toArray(new ScheduledCourseReservation[]{}));
        }
        if (getCourse().getCourseRequirements() != null && getCourse().getCourseRequirements().size() > 0) {
            StringBuilder builder = new StringBuilder();
            boolean isFirst = true;

            for (EdsReference cr : getCourse().getCourseRequirements()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }

                builder.append(cr.getName());
            }

            item.setCourseRequirementsAsString(builder.toString());
        }
        if (getInvoices() != null && getInvoices().size() > 0) {
            List<SelectItem> invoiceList = new ArrayList<>();
            for (EdsInvoice invoice : getInvoices()) {
                SelectItem inv = new SelectItem();
                inv.setId(invoice.getObjectID());
                inv.setName(invoice.getNumber());
                invoiceList.add(inv);
            }

            item.setInvoices(invoiceList.toArray(new SelectItem[]{}));
        }

        return item;
    }

    public SolrInputDocument wrapCourseScheduleToSolrDocument() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COMPANY_ID, SecurityContext.getCompanyID());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COMPOSITE_ID, SecurityContext.getCompanyID() + "_" + getObjectID());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID, getObjectID());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_NUMBER, getNumber());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_ID, getCourse().getObjectID());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_CODE, getCourse().getNumber());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_NAME, getCourse().getName());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_COURSE_ID_NAME, getCourse().getObjectID() + SolrCourseScheduleRepresenter.SPLIT + getCourse().getName());

        if (getLanguage() != null) {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID, getLanguage().getObjectID());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_LANGUAGE_NAME, getLanguage().getName());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID_NAME, getLanguage().getObjectID() + SolrCourseScheduleRepresenter.SPLIT + getLanguage().getName());
        }
        if (getLocation() != null) {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_LOCATION_NAME, getLocation().getName());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_LOCATION_ID_NAME, getLocation().getObjectID() + SolrCourseScheduleRepresenter.SPLIT + getLocation().getName());
        }
        if (getAssessor() != null) {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_ASSESSOR_ID, getAssessor().getObjectID());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_ASSESSOR_NAME, getAssessor().getName());
        }
        if (getInstructor() != null) {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID, getInstructor().getObjectID());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_NAME, getInstructor().getName());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID_NAME, getInstructor().getObjectID() + SolrCourseScheduleRepresenter.SPLIT + getInstructor().getName());
        }
        if (getStatus() != null) {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrCourseScheduleRepresenter.SPLIT + getStatus().getName());
            doc.addField(SolrCourseScheduleRepresenter.FIELD_STATUS_CODE, getStatus().getCode());
        }
        if (getCourse().getDuration() != null) {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_DURATION, getCourse().getDuration());
        } else {
            doc.addField(SolrCourseScheduleRepresenter.FIELD_DURATION, 0);
        }
        doc.addField(SolrCourseScheduleRepresenter.FIELD_ENABLE_OVERTIME, getEnableOvertime());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_START_DATE, getStartDate());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_NUMBER_OF_SEATS, getNumberOfSeats());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_CREATED_DATE, getCreationTime());
        doc.addField(SolrCourseScheduleRepresenter.FIELD_MODIFIED_DATE, getModificationDate());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    public static ScheduledCourseItem wrapCourseScheduleSolrToRPC(SolrDocument doc) {
        ScheduledCourseItem item = new ScheduledCourseItem();
        item.setObjectID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID));
        item.setNumber(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_NUMBER));
        item.setCourseID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_COURSE_ID));
        item.setCourseName(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_COURSE_NAME));
        item.setLanguageID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_LANGUAGE_ID));
        item.setLanguageName(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_LANGUAGE_NAME));
        item.setEnableOvertime(SolrUtils.asBoolean(doc, SolrCourseScheduleRepresenter.FIELD_ENABLE_OVERTIME));
        item.setStartDate(SolrUtils.asDate(doc, SolrCourseScheduleRepresenter.FIELD_START_DATE));
        item.setLocationID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_LOCATION_ID));
        item.setLocationName(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_LOCATION_NAME));
        item.setInstructorID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_ID));
        item.setInstructorName(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_INSTRUCTOR_NAME));
        item.setDuration(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_DURATION));
        item.setNumberOfSeats(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_NUMBER_OF_SEATS));
        item.setAssessorID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_ASSESSOR_ID));
        item.setAssessorName(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_ASSESSOR_NAME));
        item.setCountOfStudent(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_COUNT_OF_STUDENT));
        item.setCountOfConfirmedStudent(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_COUNT_OF_CONFIRMED_STUDENT));
        item.setStatusID(SolrUtils.asInteger(doc, SolrCourseScheduleRepresenter.FIELD_STATUS_ID));
        item.setStatusName(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_STATUS_NAME));
        item.setStatusCode(SolrUtils.asString(doc, SolrCourseScheduleRepresenter.FIELD_STATUS_CODE));
        item.setCreatedDate(SolrUtils.asDate(doc, SolrCourseScheduleRepresenter.FIELD_CREATED_DATE));
        item.setModifiedDate(SolrUtils.asDate(doc, SolrCourseScheduleRepresenter.FIELD_MODIFIED_DATE));
        return item;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public List<EdsSaleInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<EdsSaleInvoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            switch (fieldID) {
                case CustomFormConstants.TRAINING_CENTER.LOCATION -> setLocation((EdsLocation) value);
                case CustomFormConstants.TRAINING_CENTER.COURSE -> setCourse((EdsCourse) value);
                case CustomFormConstants.TRAINING_CENTER.LANGUAGE -> setLanguage((EdsReference) value);
                case CustomFormConstants.START_DATE -> setStartDate((Date) value);
                case CustomFormConstants.TRAINING_CENTER.INSTRUCTOR -> setInstructor((EdsEmployee) value);
                case CustomFormConstants.TRAINING_CENTER.ASSESSOR -> setAssessor((EdsEmployee) value);
                case CustomFormConstants.TRAINING_CENTER.NUMBER_OF_SEATS ->
                        setNumberOfSeats(((Double) value).intValue());
                case CustomFormConstants.NUMBER -> setNumber((String) value);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.TRAINING_CENTER.LOCATION)) {
            return getLocation();
        } else if (fieldID.equals(CustomFormConstants.TRAINING_CENTER.COURSE)) {
            return getCourse();
        } else if (fieldID.equals(CustomFormConstants.TRAINING_CENTER.LANGUAGE)) {
            return getLanguage();
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            return getStartDate();
        } else if (fieldID.equals(CustomFormConstants.TRAINING_CENTER.INSTRUCTOR)) {
            return getInstructor();
        } else if (fieldID.equals(CustomFormConstants.TRAINING_CENTER.ASSESSOR)) {
            return getAssessor();
        } else if (fieldID.equals(CustomFormConstants.TRAINING_CENTER.NUMBER_OF_SEATS)) {
            return getNumberOfSeats();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getModificationDate();
        }
        return super.getRealValue(fieldID);
    }
}
