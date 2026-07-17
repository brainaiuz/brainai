package com.edatasite.workforce.gwt.trainingcenter.client.rpc.student;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseScheduleListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Normurod
 * Date: 7/18/12
 * Time: 1:56 PM
 */
public class StudentItem extends ContactListItem {
    public static final String STUDENT_ACTION = "STUDENT_ACTION";
    public static final String STUDENT_NAME = "STUDENT_NAME";
    public static final String STUDENT_FIRST_NAME = "STUDENT_FIRST_NAME";
    public static final String STUDENT_NUMBER = "STUDENT_NUMBER";
    public static final String STUDENT_LAST_NAME = "STUDENT_LAST_NAME";
    public static final String STUDENT_CUSTOMER = "STUDENT_CUSTOMER";
    public static final String STUDENT_PHONE_NUMBER = "STUDENT_PHONE_NUMBER";
    public static final String STUDENT_E_MAIL = "STUDENT_E_MAIL";
    public static final String STUDENT_POSITION = "STUDENT_POSITION";
    public static final String STUDENT_LAST_UPDATE_DATE = "STUDENT_LAST_UPDATE_DATE";
    public static final String STUDENT_COMPANY_EMPLOYEE_NUMBER = "STUDENT_COMPANY_EMPLOYEE_NUMBER";
    public static final String STUDENT_DEPARTMENT_CODE = "STUDENT_DEPARTMENT_CODE";
    public static final String STUDENT_RESIDENCE_NUMBER = "STUDENT_RESIDENCE_NUMBER";
    public static final String STUDENT_REFERENCE_IND_NUMBER = "STUDENT_REFERENCE_IND_NUMBER";
    public static final String STUDENT_STATUS = "STUDENT_STATUS";
    public static final String STUDENT_ATTENDED_STATUS = "STUDENT_ATTENDED_STATUS";
    public static final String STUDENT_EXAM_STATUS = "STUDENT_EXAM_STATUS";
    public static final String STUDENT_GRADE_COLUMN = "STUDENT_GRADE_COLUMN";
    public static final String STUDENT_POINTS = "STUDENT_POINTS";
    public static final String STUDENT_LOCATION = "STUDENT_LOCATION";
    public static final String COURSE_BOOKING = "COURSE_BOOKING";
    public static final String INVOICE_NUMBER = "INVOICE_NUMBER";
    public static final String STUDENT_GENDER = "STUDENT_GENDER";
    public static final String STUDENT_NATIONALITY = "STUDENT_NATIONALITY";
    public static final String STUDENT_ADDRESS = "STUDENT_ADDRESS";

    //student card types
    //parent
    public static final String _STUDENT_CARD_TYPES = "_STUDENT_CARD_TYPES";                          //Student card types
    //children
    public static final String S_CARD_TYPE_DRIVING_LICENSE = "STUDENT_CARD_TYPE_DRIVING_LICENSE";   //Driving license
    public static final String S_CARD_TYPE_EMIRATES_ID = "STUDENT_CARD_TYPE_EMIRATES_ID";           //Emirates ID
    public static final String S_CARD_TYPE_LABOUR_CARD = "STUDENT_CARD_TYPE_LABOUR_CARD";           //Labour card
    public static final String S_CARD_TYPE_PASSPORT = "STUDENT_CARD_TYPE_PASSPORT";                 //Passport


    private Integer numberOrder;
    private String cardNumber;                //card number

    private Integer cardTypeID;               //card type ID
    private String cardTypeName;              //card type name
    private SelectItem[] cardTypes;           //card type items
    private String gender;

    private Integer contactID;                //contact ID
    private String contactName;               //contact name
    private Integer customerID;               //customer ID
    private String customerName;              //customerName
    private String compEmpNum;                //Company Employee Number
    private String departmentCode;            //Department Code independent field
    //    private String refIndNum;                //Reference Indecated Number
    private String safetyPPNumber;            //safety PP number
    private String number;            //company Employee number
    private boolean attended = false;
    private boolean isExamStatusChange = false;
    private boolean isFillNewStudentItem = false;
    private Integer studentAttendedId;
    private ArrayList<CourseScheduleListItem> studentCourseBookingItems;

    private String instructor;
    private Date courseSchedulerStartDate;
    private Date courseSchedulerEndDate;
    private String courseSchedulerNumber;
    private Integer courseScheduleID;
    private String company;
    private String course;
    private Date courseStartDate;

    private String statusCode;

    private String grade;

    private String points;
    private String language;

    private String courseBookingNumber;
    private Integer courseBookingId;

    private Integer attendedStatusID;
    private String attendedStatus;
    private String attendedStatusCode;

    private String invoiceNumber;
    private Integer invoiceID;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCourseSchedulerNumber() {
        return courseSchedulerNumber;
    }

    public void setCourseSchedulerNumber(String courseSchedulerNumber) {
        this.courseSchedulerNumber = courseSchedulerNumber;
    }

    public Date getCourseSchedulerStartDate() {
        return courseSchedulerStartDate;
    }

    public void setCourseSchedulerStartDate(Date courseSchedulerStartDate) {
        this.courseSchedulerStartDate = courseSchedulerStartDate;
    }

    public Date getCourseSchedulerEndDate() {
        return courseSchedulerEndDate;
    }

    public void setCourseSchedulerEndDate(Date courseSchedulerEndDate) {
        this.courseSchedulerEndDate = courseSchedulerEndDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public StudentItem() {
        super();
        setContactType(ContactListItem.STUDENT_CONTACT);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(Integer numberOrder) {
        this.numberOrder = numberOrder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getCardTypeID() {
        return cardTypeID;
    }

    public void setCardTypeID(Integer cardTypeID) {
        this.cardTypeID = cardTypeID;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public SelectItem[] getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(SelectItem[] cardTypes) {
        this.cardTypes = cardTypes;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompEmpNum() {
        return compEmpNum;
    }

    public void setCompEmpNum(String compEmpNum) {
        this.compEmpNum = compEmpNum;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getSafetyPPNumber() {
        return safetyPPNumber;
    }

    public void setSafetyPPNumber(String safetyPPNumber) {
        this.safetyPPNumber = safetyPPNumber;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public boolean isExamStatusChange() {
        return isExamStatusChange;
    }

    public void setExamStatusChange(boolean examStatusChange) {
        isExamStatusChange = examStatusChange;
    }

    public boolean isFillNewStudentItem() {
        return isFillNewStudentItem;
    }

    public void setFillNewStudentItem(boolean fillNewStudentItem) {
        isFillNewStudentItem = fillNewStudentItem;
    }

    public Integer getStudentAttendedId() {
        return studentAttendedId;
    }

    public void setStudentAttendedId(Integer studentAttendedId) {
        this.studentAttendedId = studentAttendedId;
    }


    public ArrayList<CourseScheduleListItem> getStudentCourseBookingItems() {
        if (studentCourseBookingItems == null) {
            studentCourseBookingItems = new ArrayList<>();
        }
        return studentCourseBookingItems;
    }

    public void setStudentCourseBookingItems(ArrayList<CourseScheduleListItem> studentCourseBookingItems) {
        this.studentCourseBookingItems = studentCourseBookingItems;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public Integer getCourseScheduleID() {
        return courseScheduleID;
    }

    public void setCourseScheduleID(Integer courseScheduleID) {
        this.courseScheduleID = courseScheduleID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCourseBookingNumber() {
        return courseBookingNumber;
    }

    public void setCourseBookingNumber(String courseBookingNumber) {
        this.courseBookingNumber = courseBookingNumber;
    }

    public Integer getCourseBookingId() {
        return courseBookingId;
    }

    public void setCourseBookingId(Integer courseBookingId) {
        this.courseBookingId = courseBookingId;
    }

    public Integer getAttendedStatusID() {
        return attendedStatusID;
    }

    public void setAttendedStatusID(Integer attendedStatusID) {
        this.attendedStatusID = attendedStatusID;
    }

    public String getAttendedStatus() {
        return attendedStatus;
    }

    public void setAttendedStatus(String attendedStatus) {
        this.attendedStatus = attendedStatus;
    }

    public String getAttendedStatusCode() {
        return attendedStatusCode;
    }

    public void setAttendedStatusCode(String attendedStatusCode) {
        this.attendedStatusCode = attendedStatusCode;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public static ArrayList<MergeItem> getAsMergeItems(String field, HashMap<Integer, StudentItem> studentItems) {
        ArrayList<MergeItem> items = new ArrayList<>();
        if (studentItems != null && studentItems.size() > 0) {
            for (Map.Entry<Integer, StudentItem> item : studentItems.entrySet()) {
                if (item != null) {
                    items.add(item.getValue().getAsMergeItem(field));
                }
            }
        }
        return items;
    }

    private MergeItem getAsMergeItem(String field) {
        MergeItem item = new MergeItem(getObjectId());
        if (field != null) {
            if (StudentItem.STUDENT_CUSTOMER.equals(field)) {
                item.setId(getCustomerID());
                item.setName(getCustomerName());
            } else if (StudentItem.STUDENT_NAME.equals(field)) {
                item.setName(getName());
            } else if (StudentItem.STUDENT_FIRST_NAME.equals(field)) {
                item.setName(getFirstName());
            } else if (StudentItem.STUDENT_LAST_NAME.equals(field)) {
                item.setName(getLastName());
            } else if (StudentItem.STUDENT_E_MAIL.equals(field)) {
                item.setName(getPrimaryEmail());
            } else if (StudentItem.STUDENT_PHONE_NUMBER.equals(field)) {
                item.setName(getPrimaryPhone());
            } else if (StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER.equals(field)) {
                item.setName(getCompEmpNum());
            } else if (StudentItem.STUDENT_RESIDENCE_NUMBER.equals(field)) {
                item.setName(getSafetyPPNumber());
            } else if (StudentItem.STUDENT_REFERENCE_IND_NUMBER.equals(field)) {
                item.setName(getRefIndNumber());
            } else if (StudentItem.STUDENT_GENDER.equals(field)) {
                item.setName(getGender());
            } else if (StudentItem.STUDENT_NATIONALITY.equals(field)) {
                item.setName(getNationality());
            } else if (StudentItem.STUDENT_ADDRESS.equals(field)) {
                item.setManyResults(true);
                if (getAddresses() != null && getAddresses().size() > 0) {
                    for (Address address : getAddresses()) {
                        if (address != null) {
                            String address_ = address.toString();
                            if (!"".equals(address_) && !"N/A".equals(address_)) {
                                item.addChild(new MergeItem(getObjectId(), address.getObjectID(), address.toString()));
                            }
                        }
                    }
                }
            }
        } else if (StudentItem.DATE_OF_BIRTH.equals(field)) {
            item.setName(getBirthDate() != null ? DateUtils.format(getBirthDate().getDate()) : null);
        }
        return item;
    }

    public void changeByMergeItem(String field, MergeItem item, boolean value) {
        if (item.getValue() == null || "N/A".equals(item.getValue())) {
            item.setValue(null);
        }

        if (field != null) {
            if ("MAINITEM".equals(field)) {
                setObjectId(item.getItemObjectID());
            } else if (StudentItem.STUDENT_CUSTOMER.equals(field)) {
                setCustomerID(item.getId());
                setCustomerName(item.getName());
            }else if (StudentItem.STUDENT_FIRST_NAME.equals(field)) {
                setFirstName(item.getName());
            } else if (StudentItem.STUDENT_LAST_NAME.equals(field)) {
                setLastName(item.getName());
            } else if (StudentItem.STUDENT_E_MAIL.equals(field)) {
                getWorkEmail().clear();
                addParam(Constants.CONTACT_EMAILS, 2, item.getName());
                setPrimaryEmail(item.getName());
            } else if (StudentItem.STUDENT_PHONE_NUMBER.equals(field)) {
                getWorkPhone().clear();
                addParam(Constants.CONTACT_PHONES, 2, item.getName());
                setPrimaryPhone(item.getName());
            } else if (StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER.equals(field)) {
                setCompEmpNum(item.getName());
            } else if (StudentItem.STUDENT_RESIDENCE_NUMBER.equals(field)) {
                setSafetyPPNumber(item.getName());
            } else if (StudentItem.STUDENT_REFERENCE_IND_NUMBER.equals(field)) {
                setRefIndNumber(item.getName());
            } else if (StudentItem.STUDENT_GENDER.equals(field)) {
                setGender(item.getName());
            } else if (StudentItem.STUDENT_NATIONALITY.equals(field)) {
                setNationality(item.getName());
            } else if (StudentItem.STUDENT_ADDRESS.equals(field)) {
                HashMap<Integer, Address> addresses = Address.asMap(getAddresses().toArray(new Address[]{}));
                if (value) {
                    addresses.put(item.getValueID(), new Address(item.getValueID()));
                } else addresses.remove(item.getValueID());
                setAddresses(new ArrayList<Address>(Arrays.asList(addresses.values().toArray(new Address[]{}))));
            }
        }
    }
}