package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseLanguageListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseScheduleListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.widgets.CourseBookingCalculationTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 16/08/12
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class AddStudentCourseBookingView extends CustomForm2 implements Colapse, Constants {

    public static final String DEP_CODE = "DEP_CODE";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String COMPANY_EMPLOYEE_NUMBER = "COMPANY_EMPLOYEE_NUMBER";
    public static final String REFERENCE_INDECATED_NUMBER = "REFERENCE_INDECATED_NUMBER";
    public static final String STUDENT_RESIDENCE_NUMBER = "RESIDENCE_NUMBER";
    public static final String EMAIL = "EMAIL";
    public static final String MOBILE = "MOBILE";
    public static final String PRE_REQUISITE = "PRE_REQUISITE";
    public static final String COURSE_NAME = "COURSE_NAME";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String COURSE_START_DATE = "COURSE_START_DATE";
    public static final String COURSE_END_DATE = "COURSE_END_DATE";
    public static final String RESIDENCE = "residence";
    public static final String COMPANY_EMPL_NUM = "comemplnum";
    public static final String ITEM_UUID = "itemuuid";

    protected static final TCStrings tcStrings = TCStrings.App.get();
    protected static TCServiceAsync tcService = TCService.App.get();

    private static final int DEFAULT_SHEET_SIZE = 10;

    private final Integer courseBookingID;
    private final Integer locationID;
    private CourseListItem[] courseListItems;
    private final List<String> addStudentList = new ArrayList<>();
    private final Map<String, Map<Integer, CourseScheduleListItem>> validationStudentMap = new HashMap<>();// key student residence id second key CourseScheduleId
    private final Map<String, StudentItem> studentItemMap = new HashMap<>();// key student residence id
    private final Map<String, String> compEmplNumMap = new HashMap<>(); // key company employee number value student residenceNumber

    private CRMLookUp contactLookUp;
    private TextBox positionBox;
    private TextBox contactEmailBox;
    private TextBox contactRefInd;
    private PhoneNumber contactPhoneNumber;

    private WfmButton2 btnSubmitForApproval;
    private WfmButton2 btnMasterCardPayment;
    private WfmButton2 btnPayUponArrival;
    private WfmButton2 btnPayBankTransfer;

    private EditableTable studentAttendedCourseBooking;
    private boolean isFromWebsite = false;
    private boolean saving = false;
    private CourseBookingItem courseBookingItem;

    private CourseBookingCalculationTable calculationTable;

    private String bookingStatus = null;
    private String bookingType = null;

    public AddStudentCourseBookingView(Integer courseBookingID, Integer locationID) {
        this(courseBookingID, locationID, false);
    }

    public AddStudentCourseBookingView(Integer courseBookingID, Integer locationID, boolean isFromWebsite) {
        super(TCConstants.TC_ADD_STUDENT_COURSE_BOOKING, tcStrings.addStudentBooking());
        this.courseBookingID = courseBookingID;
        this.locationID = locationID;
        this.isFromWebsite = isFromWebsite;
    }

    @Override
    public Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        initializationWidget();
    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        tcService.getCourseListByCourseBooking(courseBookingID, new AsyncCallback<CourseBookingItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(CourseBookingItem result) {
                LoadingPanel.loading(false);
                courseBookingItem = result;
                bookingStatus = courseBookingItem.getStatusCode();
                studentAttendedCourseBooking.setVisible(true);
                if (BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode())) {
                    Element elem = DOM.getElementById("clientContact");
                    if (elem != null) {
                        elem.getStyle().setDisplay(Style.Display.NONE);
                    }
                } else {
                    drawKeyClientWidget();
                }
                fillCourseListItems(courseBookingItem.getCourseListItemList());
            }
        });
    }

    private void drawKeyClientWidget() {
        // Client Contact data
        addTitleField(COURSE_BOOKING.CLIENT_AUTH, getTitle(tcStrings.clientAuthorisation()));
        addField(COURSE_BOOKING.CONTACT_NAME, contactLookUp, getTitle(wfmStrings.manager(), true));
        addField(COURSE_BOOKING.POSITION, positionBox, getTitle(wfmStrings.position()));
        addField(COURSE_BOOKING.REF_IND, contactRefInd, getTitle(tcStrings.refInd()));
        addField(COURSE_BOOKING.CONTACT_PHONE, contactPhoneNumber.getField(), getTitle(wfmStrings.phone()));
        addField(COURSE_BOOKING.CONTACT_EMAIL, contactEmailBox, getTitle(wfmStrings.email()));
//        contactPhoneNumber.getPhoneFeild().setWidth("125px");
        if (courseBookingItem.getCustomer() != null) {
            contactLookUp.setBeforeSearch(() -> contactLookUp.getFilterParametrs().setAccountID(courseBookingItem.getCustomer().getId()));
            contactLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (suggestionSelectionEvent.getSelectedItem() != null) {
                    contactChangeOrSelectionHandler(contactLookUp.getOracle().getItemID(suggestionSelectionEvent.getSelectedItem().getDisplayString()));
                }
            });
            contactLookUp.getSuggestBox().addBlurHandler(blurEvent -> contactChangeOrSelectionHandler(contactLookUp.getOracle().getItemID(contactLookUp.getText())));
        }
//        show();
    }

    private void clearContactOldSelectData() {
        positionBox.setText("");
        contactRefInd.setText("");
        contactPhoneNumber.clearPhoneData();
        contactEmailBox.setText("");
    }

    private void fillContactData(ClientContact result) {
        positionBox.setText(result.getPosition());
        contactPhoneNumber.setData(result.getPhone());
        contactEmailBox.setText(result.getEmail());
        contactRefInd.setText(result.getRefIndNumber());
    }

    private void contactChangeOrSelectionHandler(Integer contactID) {
        if (contactID != null) {
            LoadingPanel.loading(true);
            tcService.getContactData(contactID, new AsyncCallback<ClientContact>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ClientContact result) {
                    LoadingPanel.loading(false);
                    clearContactOldSelectData();
                    fillContactData(result);
                }
            });
        } else {
            clearContactOldSelectData();
        }
    }


    private void fillCourseListItems(CourseListItem[] courseListItems) {
        this.courseListItems = courseListItems;

        btnSubmitForApproval.setVisible(!BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode()));
        btnMasterCardPayment.setVisible(BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode()) || (isFromWebsite && !courseBookingItem.isKeyClient()));

        for (int i = 0; i < DEFAULT_SHEET_SIZE; i++) {
            studentAttendedCourseBooking.addRow(addNewStudentAttendedRow(null));
        }
    }

    @Override
    protected void addButtons() {
        if (!isFromWebsite) {
            addButton(wfmStrings.save(), BTN_DEFAULT_OUTLINE, event -> save());
        }

        btnSubmitForApproval = addButton(wfmStrings.submitForApproval(), BTN_PRIMARY, event -> {
            bookingStatus = BOOKING_SUBMITTED_TO_MANAGER;
            save();
        });

        btnMasterCardPayment = addButton(tcStrings.payOnline(), BTN_PRIMARY, event -> {
            bookingType = BOOKING_PAY_ONLINE;
            save();
        });

        btnSubmitForApproval.setVisible(false);
        btnMasterCardPayment.setVisible(false);
//        btnPayUponArrival.setVisible(false);
//        btnPayBankTransfer.setVisible(false);
    }


    private void initializationWidget() {
        // CLIENT AUTHORISATION
        contactLookUp = new CRMLookUp(CRMLookUp.CRM_CONTACT_ID);
        contactLookUp.addStyleName(DEFAULT_WIDTH);

        positionBox = new TextBox();
        positionBox.setEnabled(false);
        positionBox.addStyleName(DEFAULT_WIDTH);

        contactRefInd = new TextBox();
        contactRefInd.setEnabled(false);
        contactRefInd.addStyleName(DEFAULT_WIDTH);

        contactPhoneNumber = new PhoneNumber("");
        contactPhoneNumber.setEnabled(false);
        contactPhoneNumber.addStyleName(DEFAULT_WIDTH);

        contactEmailBox = new TextBox();
        contactEmailBox.setEnabled(false);
        contactEmailBox.addStyleName(DEFAULT_WIDTH);

        studentAttendedCourseBooking = new EditableTable(getColumnConf(), true);
        studentAttendedCourseBooking.setVisible(false);

        calculationTable = new CourseBookingCalculationTable();
        calculationTable.addStyleName(DEFAULT_WIDTH);
        calculationTable.setVisible(false);

        addWidgetsToForm();
        addWidgetsEventListener();
        show();
    }

    private Object[] addNewStudentAttendedRow(CourseScheduleListItem courseBoookingItem) {
        EditableTextBox residenceNum = new EditableTextBox();// Residence Num
        EditableTextBox comEmpNum = new EditableTextBox(); // Company Employee Num
        EditableTextBox refIndNum = new EditableTextBox(); // PDO ref. Ind. Number
        EditableTextBox depCode = new EditableTextBox(); // Department Code
        EditableTextBox firstName = new EditableTextBox(); // First Name
        EditableTextBox lastName = new EditableTextBox(); // Last Name
        EditableTextBox email = new EditableTextBox(); // Email
        PhoneNumber mobile = new PhoneNumber(""); // Mobile

        final DataListBox courseNameBox = new DataListBox(); // Course Name
        final EditableTextBox preRequisite = new EditableTextBox(); // PreRequisite
        final DataListBox languageBox = new DataListBox(); // Language
        final DataListBox courseStartDateListBox = new DataListBox(); // StartDate
        final EditableTextBox courseEndDateBox = new EditableTextBox();// EndDate
        courseEndDateBox.setEnabled(false);
        preRequisite.setEnabled(false);
//        preRequisite.setText(tcStrings.notPreRequisite());
        email.setWidth("208px");
        residenceNum.getElement().setAttribute(ITEM_UUID, DOM.createUniqueId());

        if (courseListItems != null) {
            courseNameBox.setItems(courseListItems);
        }

        int index = 0;
        Object[] objects = new Object[13];
        objects[index++] = residenceNum;
        objects[index++] = comEmpNum;
        objects[index++] = refIndNum;
        objects[index++] = depCode;
        objects[index++] = firstName;
        objects[index++] = lastName;
        objects[index++] = email;
        objects[index++] = mobile.getField();
        mobile.getPhoneFeild().setWidth("100px");

        objects[index++] = courseNameBox;
        objects[index++] = preRequisite;
        objects[index++] = languageBox;
        objects[index++] = courseStartDateListBox;
        objects[index++] = courseEndDateBox;

        addSheetTableWidgetListeners(depCode, firstName, lastName, comEmpNum, residenceNum, email, mobile, courseNameBox, preRequisite, languageBox, courseStartDateListBox, courseEndDateBox);

        return objects;
    }

    private void addSheetTableWidgetListeners(final EditableTextBox depCode, final EditableTextBox firstName, final EditableTextBox lastName, final EditableTextBox comEmpNum, final EditableTextBox residenceNum, final EditableTextBox email, final PhoneNumber mobile, final DataListBox courseNameBox, final EditableTextBox preRequisite, final DataListBox languageBox, final DataListBox courseStartDateListBox, final EditableTextBox courseEndDateBox) {
        // Residence Number Change handler
        residenceNum.addChangeHandler(event -> residendeceNumberChange(residenceNum, courseStartDateListBox, depCode, firstName, lastName, comEmpNum, email, mobile));
        // Company Employee Number Chanage Handler
//        comEmpNum.addValueChangeHandler(new ChangeHandler() {
//            @Override
//            public void onChange(ChangeEvent changeEvent) {
//                comEmployeeNumberChange(comEmpNum, residenceNum, firstName, lastName, email, mobile, courseStartDateListBox);
//            }
//        });

        // Course List
        courseNameBox.addValueChangeHandler(event -> {
            clearCourseAllWidgetData(languageBox, courseStartDateListBox, courseEndDateBox);
            if (residenceNum.getText() != null && !"".equals(residenceNum.getText())) {
                if (courseNameBox.getSelectedId() != null) {
                    final CourseListItem courseListItem = (CourseListItem) courseNameBox.getSelectedItem();
                    inActivePreRequisite();
                    if (courseListItem.isHavePreRequisiteCourse()) {
                        preRequisite.setText(tcStrings.notConfirm());
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.setMessage("I confirm the prerequisites:<br/>Courses: " + courseListItem.getPreRequisiteCourseNames()
                                + (courseListItem.getOtherPreRequisite() != null && !"".equals(courseListItem.getOtherPreRequisite()) ? "<br/>Other prerequisites: " + courseListItem.getOtherPreRequisite() : ""));
                        wfmMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                setDefaultValueCourseDateListBox(courseNameBox);
                            }

                            @Override
                            public void onSubmit() {
                                preRequisite.setText(wfmStrings.confirm());
                                courseListItem.setPreRequisite(true);
                                validationCourse(courseNameBox, languageBox, courseListItem, preRequisite);
                                inActivePreRequisite();
                            }
                        });
                        wfmMessageBox.open();
                    } else {
                        preRequisite.setText(tcStrings.notPreRequisite());
                        validationCourse(courseNameBox, languageBox, courseListItem, preRequisite);
                        inActivePreRequisite();
                    }
                } else {
                    removeAddStudentCourseSchedule(residenceNum, courseStartDateListBox);
                    preRequisite.setText(tcStrings.notPreRequisite());
                    courseEndDateBox.setText("");
                    setDefaultValueCourseDateListBox(languageBox);
                    setDefaultValueCourseDateListBox(courseStartDateListBox);
                    inActiveCourseEndDate();
                    inActiveCourseStartDate();
                    inActiveCourseLanguage();
                    inActivePreRequisite();
                }
            } else {
                preRequisite.setText("");
                inActivePreRequisite();
                setDefaultValueCourseDateListBox(courseNameBox);
                Info.warn(tcStrings.pleaseEnterStudentResidenceNumber());
            }
        });


        // Language List
        languageBox.addValueChangeHandler(event -> {
            setDefaultValueCourseDateListBox(courseStartDateListBox);
            if (residenceNum.getText() != null && !"".equals(residenceNum.getText())) {
                if (courseNameBox.getSelectedId() != null) {
                    if (languageBox.getSelectedId() != null) {
                        CourseLanguageListItem courseLanguageListItem = (CourseLanguageListItem) languageBox.getSelectedItem();
                        buildCourseStartDateList(courseStartDateListBox, courseLanguageListItem);
                    } else {
                        removeAddStudentCourseSchedule(residenceNum, courseStartDateListBox);
                        courseEndDateBox.setText("");
                        setDefaultValueCourseDateListBox(courseStartDateListBox);
                        inActiveCourseEndDate();
                        inActiveCourseStartDate();
                    }
                } else {
                    courseEndDateBox.setText("");
                    setDefaultValueCourseDateListBox(languageBox);
                    setDefaultValueCourseDateListBox(courseStartDateListBox);
                    inActiveCourseEndDate();
                    inActiveCourseStartDate();
                    inActiveCourseLanguage();
                    Info.warn(tcStrings.pleaseSelectCourse());
                }
            } else {
                courseEndDateBox.setText("");
                setDefaultValueCourseDateListBox(courseNameBox);
                setDefaultValueCourseDateListBox(languageBox);
                setDefaultValueCourseDateListBox(courseStartDateListBox);
                inActiveCourseEndDate();
                inActiveCourseStartDate();
                inActiveCourseLanguage();
                inActiveCourseName();
                Info.warn(tcStrings.pleaseEnterStudentResidenceNumber());
            }
        });

        // Course startDate
        courseStartDateListBox.addValueChangeHandler(event -> {
            if (residenceNum.getText() != null && !"".equals(residenceNum.getText())) {
                if (courseNameBox.getSelectedId() != null) {
                    if (languageBox.getSelectedId() != null) {
                        if (courseStartDateListBox.getSelectedId() != null) {
                            CourseScheduleListItem courseScheduleListItem = (CourseScheduleListItem) courseStartDateListBox.getSelectedItem();
                            courseScheduleListItem.setItemUUID(residenceNum.getElement().getAttribute(ITEM_UUID));

                            if (validationStudentAttended(courseScheduleListItem, residenceNum.getText().trim().toLowerCase())) {
                                setTemporaryLockAndCheckedCourseCount(courseScheduleListItem, residenceNum, languageBox, courseStartDateListBox, courseEndDateBox);
                            } else if (!validateScheduleNumberOfSeats(courseScheduleListItem)) {
                                clearCourseAllWidgetData(languageBox, courseStartDateListBox, courseEndDateBox);
                                Info.warn("Sorry, the scheduled course on " + DateUtils.formatInternal(courseScheduleListItem.getStartDate()) + "-" + DateUtils.formatInternal(courseScheduleListItem.getEndDate()) + " date is completely full. Please choose a different date.");
                            } else {
                                clearCourseAllWidgetData(languageBox, courseStartDateListBox, courseEndDateBox);
                                Info.warn("Sorry, this student is already attending " + conflictedCourse.getCourseCode() + " " + conflictedCourse.getCourseName() + " course on " + DateUtils.formatInternal(conflictedCourse.getStartDate()) + "-" + DateUtils.formatInternal(conflictedCourse.getEndDate()) + ". Please choose a different date.");
                            }
                        } else {
                            removeAddStudentCourseSchedule(residenceNum, courseStartDateListBox);
                            courseEndDateBox.setText("");
                            inActiveCourseEndDate();
                        }
                    } else {
                        courseEndDateBox.setText("");
                        setDefaultValueCourseDateListBox(courseStartDateListBox);
                        inActiveCourseEndDate();
                        inActiveCourseStartDate();
                        Info.warn(tcStrings.pleaseSelectCourseLanguage());
                    }
                } else {
                    courseEndDateBox.setText("");
                    setDefaultValueCourseDateListBox(languageBox);
                    setDefaultValueCourseDateListBox(courseStartDateListBox);
                    inActiveCourseEndDate();
                    inActiveCourseStartDate();
                    inActiveCourseLanguage();
                    Info.warn(tcStrings.pleaseSelectCourse());
                }
            } else {
                courseEndDateBox.setText("");
                setDefaultValueCourseDateListBox(courseNameBox);
                setDefaultValueCourseDateListBox(languageBox);
                setDefaultValueCourseDateListBox(courseStartDateListBox);
                inActiveCourseEndDate();
                inActiveCourseStartDate();
                inActiveCourseLanguage();
                inActiveCourseName();
                Info.warn(tcStrings.pleaseEnterStudentResidenceNumber());
            }
        });

    }

    private void validationCourse(DataListBox courseNameBox, DataListBox languageBox, CourseListItem courseListItem, EditableTextBox preRequisite) {
        if (courseListItem.isValidationKeyClient()) {
            languageBox.setItems(courseListItem.getLanguageItem());
            inActiveCourseLanguage();
        } else {
            preRequisite.setText("");
            inActivePreRequisite();
            setDefaultValueCourseDateListBox(courseNameBox);
            Info.warn("Sorry, this course is not included in your company contract.");
        }
    }

    private void setTemporaryLockAndCheckedCourseCount(final CourseScheduleListItem courseScheduleListItem, final EditableTextBox residenceNum, final DataListBox languageBox, final DataListBox courseStartDateListBox, final EditableTextBox courseEndDateBox) {
        LoadingPanel.loading(true);
        tcService.setTemporaryLock(courseBookingID, courseScheduleListItem, courseBookingItem.isPrePaid(), new AsyncCallback<CourseScheduleListItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CourseScheduleListItem result) {
                LoadingPanel.loading(false);
                if (result.getNumOfSeatsCount() > result.getAttendStudentCount()) {
                    courseEndDateBox.setText(DateUtils.formatInternalShort1(courseScheduleListItem.getEndDate()));
                    inActiveCourseEndDate();

                    if (!courseBookingItem.isKeyClient()) {
                        calculationTable.setVisible(true);
                        calculationTable.calculation(getStudentItems());
                    }
                } else {
                    if (result.getDroppableStudentCount() == 0) {
                        clearCourseAllWidgetData(languageBox, courseStartDateListBox, courseEndDateBox);
                        Info.warn("Sorry, the course is full.");
                    } else {
                        clearCourseAllWidgetData(languageBox, courseStartDateListBox, courseEndDateBox);
                        Info.warn(" Sorry, course is full, but there are " + result.getDroppableStudentCount() + " seats available by drop-off only. If you need " + result.getDroppableStudentCount() + " seats made available, please contact training center administration.");
                    }
                }
            }
        });
    }

    private void removeAddStudentCourseSchedule(EditableTextBox residenceNum, DataListBox courseStartDateListBox) {
        if (residenceNum.getText() != null && courseStartDateListBox.getSelectedItem() != null && validationStudentMap.containsKey(residenceNum.getText().toLowerCase().trim())) {
            CourseScheduleListItem courseScheduleListItem = (CourseScheduleListItem) courseStartDateListBox.getSelectedItem();
            validationStudentMap.get(residenceNum.getText().toLowerCase().trim()).remove(courseScheduleListItem.getCourseScheduleId());
        }
    }

    private void comEmployeeNumberChange(final EditableTextBox depCode, final EditableTextBox comEmpNum, final EditableTextBox residenceNum, final EditableTextBox firstName, final EditableTextBox lastName, final EditableTextBox email, final PhoneNumber mobile, final DataListBox courseStartDateListBox) {
        final String oldComEmplNum = comEmpNum.getElement().getAttribute(COMPANY_EMPL_NUM);
        if (comEmpNum.getText() != null && compEmplNumMap.containsKey(comEmpNum.getText().trim().toLowerCase()) && residenceNum.getText() != null && studentItemMap.containsKey(residenceNum.getText().trim().toLowerCase())) {
            if (studentItemMap.get(residenceNum.getText().trim().toLowerCase()).getObjectId() == null && !studentItemMap.get(residenceNum.getText().trim().toLowerCase()).isFillNewStudentItem()) {
                LoadingPanel.loading(true);
                getStudentItems();
                LoadingPanel.loading(false);
                studentItemMap.get(residenceNum.getText().trim().toLowerCase()).setFillNewStudentItem(true);
            }
            fillStudentData(studentItemMap.get(compEmplNumMap.get(comEmpNum.getText().trim().toLowerCase())), depCode, firstName, lastName, comEmpNum, residenceNum, email, mobile);
            comEmpNum.getElement().setAttribute(COMPANY_EMPL_NUM, comEmpNum.getText().trim().toLowerCase());
        } else if (comEmpNum.getText() != null && !"".equals(comEmpNum.getText())) {
            LoadingPanel.loading(true);
            tcService.findStudentByCompanyEmployeeNumber(comEmpNum.getText().trim().toLowerCase(), courseBookingID, locationID, new AsyncCallback<StudentItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(StudentItem studentItem) {
                    LoadingPanel.loading(false);
                    if (studentItem != null) {
                        fillStudentData(studentItem, depCode, firstName, lastName, comEmpNum, residenceNum, email, mobile);
                        if (!compEmplNumMap.containsKey(comEmpNum.getText().trim().toLowerCase())) {
                            compEmplNumMap.put(comEmpNum.getText().trim().toLowerCase(), studentItem.getSafetyPPNumber());
                        }
                        if (!studentItemMap.containsKey(studentItem.getSafetyPPNumber().trim().toLowerCase())) {
                            studentItemMap.put(studentItem.getSafetyPPNumber().trim().toLowerCase(), studentItem);
                        }
                        if (!validationStudentMap.containsKey(studentItem.getSafetyPPNumber().trim().toLowerCase())) {
                            validationStudentMap.put(studentItem.getSafetyPPNumber().trim().toLowerCase(), new HashMap<>());
                        }
                        Map<Integer, CourseScheduleListItem> courseScheduleItemMap = validationStudentMap.get(studentItem.getSafetyPPNumber().trim().toLowerCase());
                        for (CourseScheduleListItem courseSchedule : studentItem.getStudentCourseBookingItems()) {
                            courseScheduleItemMap.put(courseSchedule.getCourseScheduleId(), courseSchedule);
                        }
                    }

                    if (oldComEmplNum != null && !"".equals(oldComEmplNum) && !comEmpNum.getText().equals(oldComEmplNum) && compEmplNumMap.containsKey(oldComEmplNum)
                            && validationStudentMap.containsKey(compEmplNumMap.get(oldComEmplNum).trim().toLowerCase())
                            && validationStudentMap.get(compEmplNumMap.get(oldComEmplNum).trim().toLowerCase()).size() > 1) {

                        changeOnlyThisResidence(compEmplNumMap.get(oldComEmplNum), courseStartDateListBox);
                    }
                    comEmpNum.getElement().setAttribute(COMPANY_EMPL_NUM, comEmpNum.getText().trim().toLowerCase());
                }
            });
        } else {
            if (oldComEmplNum != null && !"".equals(oldComEmplNum) && compEmplNumMap.containsKey(oldComEmplNum)
                    && validationStudentMap.containsKey(compEmplNumMap.get(oldComEmplNum).trim().toLowerCase())
                    && validationStudentMap.get(compEmplNumMap.get(oldComEmplNum).trim().toLowerCase()).size() > 1) {

                changeOnlyThisResidence(compEmplNumMap.get(oldComEmplNum), courseStartDateListBox);
            }
            comEmpNum.getElement().setAttribute(COMPANY_EMPL_NUM, "");
        }
    }

    private void residendeceNumberChange(final EditableTextBox residenceNum, final DataListBox courseStartDateListBox, final EditableTextBox depCode, final EditableTextBox firstName, final EditableTextBox lastName, final EditableTextBox comEmpNum, final EditableTextBox email, final PhoneNumber mobile) {
        final String oldResindenceNum = residenceNum.getElement().getAttribute(RESIDENCE);
        if (residenceNum.getText() != null && studentItemMap.containsKey(residenceNum.getText().trim().toLowerCase())) {
            if (studentItemMap.get(residenceNum.getText().trim().toLowerCase()).getObjectId() == null && !studentItemMap.get(residenceNum.getText().trim().toLowerCase()).isFillNewStudentItem()) {
                LoadingPanel.loading(true);
                getStudentItems();
                LoadingPanel.loading(false);
                studentItemMap.get(residenceNum.getText().trim().toLowerCase()).setFillNewStudentItem(true);
            }
            fillStudentData(studentItemMap.get(residenceNum.getText().trim()), depCode, firstName, lastName, comEmpNum, residenceNum, email, mobile);
            residenceNum.getElement().setAttribute(RESIDENCE, residenceNum.getText().trim().toLowerCase());
        } else if (residenceNum.getText() != null && !"".equals(residenceNum.getText())) {
            LoadingPanel.loading(true);
            tcService.findStudentByResidenneNum(residenceNum.getText().trim().toLowerCase(), courseBookingID, locationID, new AsyncCallback<StudentItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(StudentItem result) {
                    LoadingPanel.loading(false);
                    if (result != null) {
                        fillStudentData(result, depCode, firstName, lastName, comEmpNum, residenceNum, email, mobile);
                        if (!studentItemMap.containsKey(residenceNum.getText().trim().toLowerCase())) {
                            studentItemMap.put(residenceNum.getText().trim().toLowerCase(), result);
                        }
                        if (result.getCompEmpNum() != null && !compEmplNumMap.containsKey(result.getCompEmpNum())) {
                            compEmplNumMap.put(result.getCompEmpNum().trim().toLowerCase(), residenceNum.getText().trim().toLowerCase());
                        }
                        if (!validationStudentMap.containsKey(residenceNum.getText().trim().toLowerCase())) {
                            validationStudentMap.put(residenceNum.getText().trim().toLowerCase(), new HashMap<>());
                        }
                        Map<Integer, CourseScheduleListItem> courseScheduleItemMap = validationStudentMap.get(residenceNum.getText().trim().toLowerCase());
                        for (CourseScheduleListItem courseSchedule : result.getStudentCourseBookingItems()) {
                            courseScheduleItemMap.put(courseSchedule.getCourseScheduleId(), courseSchedule);
                        }
                    } else {
                        StudentItem studentItem = new StudentItem();
                        studentItem.setSafetyPPNumber(residenceNum.getText());
                        studentItemMap.put(residenceNum.getText().trim().toLowerCase(), studentItem);
                    }

                    if (oldResindenceNum != null && !"".equals(oldResindenceNum) && !residenceNum.getText().equals(oldResindenceNum) && validationStudentMap.containsKey(oldResindenceNum) && validationStudentMap.get(oldResindenceNum).size() > 1) {
                        changeOnlyThisResidence(oldResindenceNum, courseStartDateListBox);
                    }
                    residenceNum.getElement().setAttribute(RESIDENCE, residenceNum.getText().trim().toLowerCase());
                }
            });

        } else {
            if (oldResindenceNum != null && !"".equals(oldResindenceNum) && validationStudentMap.containsKey(oldResindenceNum) && validationStudentMap.get(oldResindenceNum).size() > 1) {
                changeOnlyThisResidence(oldResindenceNum, courseStartDateListBox);
            }
            residenceNum.getElement().setAttribute(RESIDENCE, "");
        }
    }

    private void changeOnlyThisResidence(String oldResindenceNum, DataListBox courseStartDateListBox) {
        if (courseStartDateListBox.getSelectedId() != null) {
            CourseScheduleListItem currentCourseScheduleItem = (CourseScheduleListItem) courseStartDateListBox.getSelectedItem();
            validationStudentMap.get(oldResindenceNum).remove(currentCourseScheduleItem);
            courseStartDateListBox.clearSelected();
            courseStartDateListBox.setSelectedItem(null);
        }
    }


    private void fillStudentData(StudentItem result, EditableTextBox depCode, EditableTextBox firstName, EditableTextBox lastName, EditableTextBox comEmpNum, EditableTextBox residenceNum, EditableTextBox email, PhoneNumber mobile) {
        if (result.getDepartmentCode() != null) {
            depCode.setText(result.getDepartmentCode());
        }
        if (result.getFirstName() != null) {
            firstName.setText(result.getFirstName());
        }
        if (result.getLastName() != null) {
            lastName.setText(result.getLastName());
        }
        if (result.getCompEmpNum() != null) {
            comEmpNum.setText(result.getCompEmpNum());
        }
        if (result.getSafetyPPNumber() != null) {
            residenceNum.setText(result.getSafetyPPNumber());
        }
        if (result.getPrimaryEmail() != null) {
            email.setText(result.getPrimaryEmail());
        }
        if (result.getPrimaryPhone() != null) {
            mobile.setData(result.getPrimaryPhone());
        }
        int currentRow = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
        CustomCell depCodeCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, DEP_CODE);
        CustomCell firstNameCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, FIRST_NAME);
        CustomCell lastNameCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, LAST_NAME);
        CustomCell residenceNumCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, STUDENT_RESIDENCE_NUMBER);
        CustomCell comEmpNumNameCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, COMPANY_EMPLOYEE_NUMBER);
        CustomCell emailCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, EMAIL);
        CustomCell mobileCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, MOBILE);

        EditableTextBox refIndNum = (EditableTextBox) studentAttendedCourseBooking.getColumnById(currentRow, REFERENCE_INDECATED_NUMBER);
        if (result.getRefIndNumber() != null) {
            refIndNum.setText(result.getRefIndNumber());
        }
        CustomCell refIndNumCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, REFERENCE_INDECATED_NUMBER);

        depCodeCell.InActive();
        firstNameCell.InActive();
        lastNameCell.InActive();
        comEmpNumNameCell.InActive();
        emailCell.InActive();
        mobileCell.InActive();
        residenceNumCell.InActive();
        refIndNumCell.InActive();

    }

    private void buildCourseStartDateList(DataListBox courseDateListBox, CourseLanguageListItem courseLanguageListItem) {
        List<CourseScheduleListItem> cShListItems = new ArrayList<>();
        if (!courseLanguageListItem.isSetupSelectItem()) {
            for (Date datekey : courseLanguageListItem.getCourseScheduleListItemMap().keySet()) {
                //if (validateDate(datekey)) {
                    Map<Integer, CourseScheduleListItem> courseScheduleListItemMap = courseLanguageListItem.getCourseScheduleListItemMap().get(datekey);
                    String startDate = DateUtils.formatInternalShort1(datekey);
                    for (CourseScheduleListItem courseScheduleItem : courseScheduleListItemMap.values()) {
                        courseScheduleItem.setId(courseScheduleItem.getCourseScheduleId());
                        if (courseScheduleListItemMap.size() > 1) {
                            courseScheduleItem.setName((startDate + " " + courseScheduleItem.getInstructorName()));
                        } else {
                            courseScheduleItem.setName(startDate);
                        }
                        courseScheduleItem.setDescription(String.valueOf(courseScheduleItem.getInstructoId()));
                        cShListItems.add(courseScheduleItem);
                    }
                //}
            }
            courseLanguageListItem.setSetupSelectItem(true);
        } else {
            for (Date datekey : courseLanguageListItem.getCourseScheduleListItemMap().keySet()) {
//                if (validateDate(datekey)) {
                    Map<Integer, CourseScheduleListItem> courseScheduleListItems = courseLanguageListItem.getCourseScheduleListItemMap().get(datekey);
                    cShListItems.addAll(courseScheduleListItems.values());
//                }
            }
        }
        cShListItems.sort((o1, o2) -> {
            if (o1.getStartDate().after(o2.getStartDate())) {
                return 1;
            }
            if (o1.getStartDate().before(o2.getStartDate())) {
                return -1;
            }
            return 0;
        });
        courseDateListBox.setItems(cShListItems.toArray(new CourseScheduleListItem[]{}));
    }

    private CourseScheduleListItem conflictedCourse = null;

    private boolean validationStudentAttended(CourseScheduleListItem currentCourseScheduleItem, String studentResidenceId) {
        if (!validateScheduleNumberOfSeats(currentCourseScheduleItem)) {
            return false;
        } else if (!validationStudentMap.containsKey(studentResidenceId)) {
            validationStudentMap.put(studentResidenceId, new HashMap<>());
            validationStudentMap.get(studentResidenceId).put(currentCourseScheduleItem.getCourseScheduleId(), currentCourseScheduleItem);
            return true;
        } else {
            boolean validate = true;
            for (CourseScheduleListItem courseScheduleItem : validationStudentMap.get(studentResidenceId).values()) {
                if (!courseScheduleItem.validateDatePeriod(currentCourseScheduleItem)) {
                    conflictedCourse = courseScheduleItem;
                    validate = false;
                    break;
                }
            }
            if (validate) {
                validationStudentMap.get(studentResidenceId).put(currentCourseScheduleItem.getCourseScheduleId(), currentCourseScheduleItem);
            }
            return validate;
        }
    }

    private boolean validateScheduleNumberOfSeats(CourseScheduleListItem currentCourseScheduleItem) {

        if (!validationStudentMap.values().isEmpty()) {
            int counter = 0;
            for (Map<Integer, CourseScheduleListItem> map : validationStudentMap.values()) {
                if (map.get(currentCourseScheduleItem.getCourseScheduleId()) != null) {
                    counter++;
                }
            }

            return counter < currentCourseScheduleItem.getAvailableNumbOfSeatsCount();
        }

        return true;
    }

    private void inActiveCourseName() {
        int currentRow = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
        CustomCell courseNameCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, COURSE_NAME);
        courseNameCell.InActive();
    }

    private void inActivePreRequisite() {
        int currentRow = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
        CustomCell preRequisiteCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, PRE_REQUISITE);
        preRequisiteCell.InActive();
    }

    private void inActiveCourseLanguage() {
        int currentRow = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
        CustomCell courseLanguageCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, LANGUAGE);
        courseLanguageCell.InActive();
    }

    private void inActiveCourseStartDate() {
        int currentRow = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
        CustomCell courseStartDateCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, COURSE_START_DATE);
        courseStartDateCell.InActive();
    }

    private void inActiveCourseEndDate() {
        int currentRow = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
        CustomCell courseEndDateCell = (CustomCell) studentAttendedCourseBooking.getColumnCellWidgetById(currentRow, COURSE_END_DATE);
        courseEndDateCell.InActive();
    }

    private void setDefaultValueCourseDateListBox(DataListBox courseDateListBox) {
        courseDateListBox.setSelectedItem(null);
        courseDateListBox.clearSelected();
    }

    private void clearCourseAllWidgetData(DataListBox languageBox, DataListBox courseDateListBox, EditableTextBox courseEndDateBox) {
        courseEndDateBox.setText("");
        setDefaultValueCourseDateListBox(languageBox);
        setDefaultValueCourseDateListBox(courseDateListBox);
        inActiveCourseLanguage();
        inActiveCourseEndDate();
        inActiveCourseStartDate();
    }

    private void addWidgetsToForm() {
        addTitleField(COURSE_BOOKING.STUDENT_COURSE_SCHEDULE_DETAILS, tcStrings.studentCourseScheduleDetails());
        addField(COURSE_BOOKING.STUDENT_COURSE_BOOKING, studentAttendedCourseBooking, null);
        addField(COURSE_BOOKING.CALCULATION_TABLE, calculationTable, null);

    }

    private void addWidgetsEventListener() {
        studentAttendedCourseBooking.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                studentAttendedCourseBooking.addRow(addNewStudentAttendedRow(null));
            }

            @Override
            public void removeRow() {
                int rowID = studentAttendedCourseBooking.getGridPanel().getCurrentRow();
                EditableTextBox residenceNum = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, STUDENT_RESIDENCE_NUMBER);
                DataListBox courseStartDateListBox = (DataListBox) studentAttendedCourseBooking.getColumnById(rowID, COURSE_START_DATE);
                if (courseStartDateListBox.getSelectedId() != null) {
                    CourseScheduleListItem courseScheduleListItem = (CourseScheduleListItem) courseStartDateListBox.getSelectedItem();
                    if (residenceNum.getText() != null && !"".equals(residenceNum.getText())) {
                        if (validationStudentMap.containsKey(residenceNum.getText().trim().toLowerCase())) {
                            validationStudentMap.get(residenceNum.getText().trim().toLowerCase()).remove(courseScheduleListItem.getCourseScheduleId());
                        }
                    }
                }
                LoadingPanel.loading(true);
                tcService.expireTemporaryLock(courseBookingID, residenceNum.getElement().getAttribute(ITEM_UUID), new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                    }
                });

                calculationTable.calculation(getStudentItems());
            }
        });
    }

    private ColumnConfig[] getColumnConf() {
        ColumnConfig[] columnConfigs = new ColumnConfig[13];

        int index = 0;

        columnConfigs[index++] = new ColumnConfig(CustomCell.class, STUDENT_RESIDENCE_NUMBER, tcStrings.residenceNumber(), 70, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, COMPANY_EMPLOYEE_NUMBER, wfmStrings.companyEmployeeNumber(), 60, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, REFERENCE_INDECATED_NUMBER, tcStrings.refIndNumber(), 60, false);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, DEP_CODE, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.number(), wfmStrings.department()), 60, false);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, FIRST_NAME, wfmStrings.firstName(), 70, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, LAST_NAME, wfmStrings.lastName(), 70, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, EMAIL, wfmStrings.email(), 120, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, MOBILE, wfmStrings.mobile(), 120, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, COURSE_NAME, tcStrings.courseName(), 80, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, PRE_REQUISITE, wfmStrings.preRequisite(), 70, false);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, LANGUAGE, wfmStrings.language(), 70, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, COURSE_START_DATE, wfmStrings.startDate(), 80, true);
        columnConfigs[index++] = new ColumnConfig(CustomCell.class, COURSE_END_DATE, wfmStrings.endDate(), 80, false);
        return columnConfigs;
    }

    public boolean validationRequiresFields() {
        int courseValid = 0;
        int expireDate = 0;
        for (int rowID = 0; rowID < studentAttendedCourseBooking.getRowCount(); rowID++) {
            int courseValidRow = 0;
            EditableTextBox residenceNum = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, STUDENT_RESIDENCE_NUMBER);
            if (residenceNum.getText() != null && !"".equalsIgnoreCase(residenceNum.getText().trim())) {
                EditableTextBox firstName = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, FIRST_NAME);
                EditableTextBox employeeNember = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, COMPANY_EMPLOYEE_NUMBER);
                EditableTextBox lasttName = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, LAST_NAME);
                EditableTextBox email = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, EMAIL);
                PhoneNumber mobile = (PhoneNumber) studentAttendedCourseBooking.getColumnById(rowID, MOBILE);

                DataListBox courListBox = (DataListBox) studentAttendedCourseBooking.getColumnById(rowID, COURSE_NAME);
                DataListBox languageListBox = (DataListBox) studentAttendedCourseBooking.getColumnById(rowID, LANGUAGE);
                DataListBox startDateListBox = (DataListBox) studentAttendedCourseBooking.getColumnById(rowID, COURSE_START_DATE);

                if (firstName.getText() == null || "".equals(firstName.getText())) {
                    courseValid++;
                    studentAttendedCourseBooking.setColumnValid(FIRST_NAME);
                    studentAttendedCourseBooking.notValid(rowID, FIRST_NAME);
                }

                if (employeeNember.getText() == null || "".equals(employeeNember.getText())) {
                    courseValid++;
                    studentAttendedCourseBooking.setColumnValid(COMPANY_EMPLOYEE_NUMBER);
                    studentAttendedCourseBooking.notValid(rowID, COMPANY_EMPLOYEE_NUMBER);
                }

                if (lasttName.getText() == null || "".equals(lasttName.getText())) {
                    courseValid++;
                    studentAttendedCourseBooking.setColumnValid(LAST_NAME);
                    studentAttendedCourseBooking.notValid(rowID, LAST_NAME);
                }

                if (email.getText() == null || "".equals(email.getText()) || !Validation.validEmailFormat(email.getText(), true)) {
                    courseValid++;
                    studentAttendedCourseBooking.setColumnValid(EMAIL);
                    studentAttendedCourseBooking.notValid(rowID, EMAIL);
                }

                if ("".equals(mobile.getDisplayValue())) {
                    courseValid++;
                    studentAttendedCourseBooking.setColumnValid(MOBILE);
                    studentAttendedCourseBooking.notValid(rowID, MOBILE);
                }

                if (languageListBox.getSelectedId() == null) {
                    courseValidRow++;
                    studentAttendedCourseBooking.setColumnValid(LANGUAGE);
                    studentAttendedCourseBooking.notValid(rowID, LANGUAGE);
                }

                if (startDateListBox.getSelectedId() == null) {
                    courseValidRow++;
                    studentAttendedCourseBooking.setColumnValid(COURSE_START_DATE);
                    studentAttendedCourseBooking.notValid(rowID, COURSE_START_DATE);
                }
                if (isFromWebsite) {
                    CourseScheduleListItem courseScheduleListItem = (CourseScheduleListItem) startDateListBox.getSelectedItem();
                    Date today = new Date();
                    if (courseScheduleListItem.getStartDate().before(today)) {
                        expireDate++;
                        studentAttendedCourseBooking.setColumnValid(COURSE_START_DATE);
                        studentAttendedCourseBooking.notValid(rowID, COURSE_START_DATE);
                    }
                }
                studentAttendedCourseBooking.setItemValid(rowID, courseValidRow <= 0);

                courseValid += courseValidRow;
                courseValidRow = 0;
            }
        }
        if (!BOOKING_PAY_ONLINE.equals(courseBookingItem.getTypeCode())) {
            courseValid += markAsError(COURSE_BOOKING.CONTACT_NAME, contactLookUp, contactLookUp.getSelectedItem() == null);
        }
        if (courseValid > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        if (expireDate > 0) {
            Info.show(tcStrings.dateHasAlreadyPassed(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public ArrayList<StudentItem> getStudentItems() {
        addStudentList.clear();
        clearStudentItemMapCourseSchedules();

        ArrayList<StudentItem> studentItemList = new ArrayList<>();
        for (int rowID = 0; rowID < studentAttendedCourseBooking.getRowCount(); rowID++) {
            EditableTextBox residenceNum = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, STUDENT_RESIDENCE_NUMBER);
            if (residenceNum.getText() != null && !"".equalsIgnoreCase(residenceNum.getText().trim())) {
                StudentItem studentItem;
                if (studentItemMap.containsKey(residenceNum.getText().trim().toLowerCase())) {
                    studentItem = studentItemMap.get(residenceNum.getText().trim().toLowerCase());
                } else {
                    studentItem = new StudentItem();
                }
                DataListBox courseStartDateListBox = (DataListBox) studentAttendedCourseBooking.getColumnById(rowID, COURSE_START_DATE);
                if (!addStudentList.contains(residenceNum.getText().trim().toLowerCase())) {
                    EditableTextBox depCode = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, DEP_CODE);
                    EditableTextBox firstName = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, FIRST_NAME);
                    EditableTextBox lastName = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, LAST_NAME);
                    EditableTextBox compEmpName = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, COMPANY_EMPLOYEE_NUMBER);
                    EditableTextBox refIndNumber = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, REFERENCE_INDECATED_NUMBER);
                    EditableTextBox email = (EditableTextBox) studentAttendedCourseBooking.getColumnById(rowID, EMAIL);
                    PhoneNumber mobile = (PhoneNumber) studentAttendedCourseBooking.getColumnById(rowID, MOBILE);

                    studentItem.setDepartmentCode(depCode.getText());
                    studentItem.setFirstName(firstName.getText());
                    studentItem.setLastName(lastName.getText());
                    studentItem.setSafetyPPNumber(residenceNum.getText().trim());
                    studentItem.setCompEmpNum(compEmpName.getText());
                    studentItem.setRefIndNumber(refIndNumber.getText());
                    studentItem.setPrimaryEmail(email.getText());
                    studentItem.setPrimaryPhone(mobile.toString());
                    addStudentList.add(residenceNum.getText().trim().toLowerCase());
                    if (saving && studentItem.getObjectId() == null) {
                        ArrayList<String> emails = new ArrayList<>();
                        emails.add(email.getText());
                        studentItem.setWorkEmail(emails);
//                        studentItem.getWorkPhone().clear();
//                        if ("".equals(mobile.toString(true)) || studentItem.getAllPhones().contains(mobile.toString(true))) {
//                            continue;
//                        }
                        studentItem.addPhone(Constants.G_WORK, mobile.toString());
                        if (studentItem.getCrmAccount().isNew()) {
                            studentItem.getCrmAccount().setPhone(mobile.toString());
                        }
                    } else if (saving && studentItem.getAllPhones().size() == 0) {
                        studentItem.addPhone(Constants.G_WORK, mobile.toString());
                    }
                }
                CourseScheduleListItem courseScheduleListItem = (CourseScheduleListItem) courseStartDateListBox.getSelectedItem();
                studentItem.getStudentCourseBookingItems().add(courseScheduleListItem);
                studentItemMap.put(residenceNum.getText().trim().toLowerCase(), studentItem);
            }
        }

        studentItemList.addAll(studentItemMap.values());
        return studentItemList;
    }

    private void clearStudentItemMapCourseSchedules() {
        for (StudentItem studentItem : studentItemMap.values()) {
            studentItem.getStudentCourseBookingItems().clear();
        }
    }

    private void save() {
        if (!validationRequiresFields()) {
            return;
        }
        saving = true;
        ArrayList<StudentItem> studentItemList = getStudentItems();
        ContactListItem contactListItem = null;
        if (contactLookUp != null && contactLookUp.getSelectedItemID() != null) {
            contactListItem = new ContactListItem();
            contactListItem.setObjectId(contactLookUp.getSelectedItemID());
            contactListItem.setCheckForDuplicates(true);
        }
        courseBookingItem.setContactItems(contactListItem);
        CourseBookingItem bookingItem = new CourseBookingItem();
        bookingItem.setObjectID(courseBookingID);
        bookingItem.setContactItems(contactListItem);
        bookingItem.setKeyClient(courseBookingItem.isKeyClient());
        bookingItem.setUserDefinedUrl(isFromWebsite ? getUserDefinedUrl() : null);
        bookingItem.setStudentItems(studentItemList);
        bookingItem.setStatusCode(bookingStatus);
        bookingItem.setTypeCode(bookingType != null ? bookingType : courseBookingItem.getTypeID() != null ? courseBookingItem.getTypeCode() : !courseBookingItem.isKeyClient() ? BOOKING_PAY_UPON_ARRIVAL : BOOKING_BY_APPROVAL);

        LoadingPanel.loading(true);
        tcService.saveCourseBookingAttendedStudents(bookingItem, new AsyncCallback<CourseBookingItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
                saving = false;
            }

            @Override
            public void onSuccess(CourseBookingItem result) {
                LoadingPanel.loading(false);
                courseBookingItem = result;
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySubmitted(), tcStrings.courseBooking()));
                if (!isFromWebsite && !courseBookingItem.isKeyClient() && BOOKING_PAY_ONLINE.equals(bookingType) && courseBookingItem.getMasterCardPaymentURL() != null) {
                    Window.open(courseBookingItem.getMasterCardPaymentURL(), "_blank", "");
                    closeTab();
                } else if (isFromWebsite && !courseBookingItem.isKeyClient() && BOOKING_PAY_ONLINE.equals(bookingType) && courseBookingItem.getMasterCardPaymentURL() != null) {
                    Window.Location.assign(courseBookingItem.getMasterCardPaymentURL());
                } else {
                    if (isFromWebsite) {
                        Window.Location.assign(getApprovalConfirmationUrl());
                    } else {
                        closeTab();
                    }
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COURSE_BOOKING_ADD_EDIT, null, null);
            }
        });
    }

    private String userDefinedUrl;
    private String approvalConfirmationUrl;

    private String getUserDefinedUrl() {
        return userDefinedUrl;
    }

    private String getApprovalConfirmationUrl() {
        return approvalConfirmationUrl;
    }

    public void setUserDefinedUrl(String url) {
        this.userDefinedUrl = "http://" + getDomain() + url;
    }

    public void setApprovalConfirmationUrl(String url) {
        this.approvalConfirmationUrl = "http://" + getDomain() + url;
    }

    private static native String getDomain()/*-{
        return $wnd.location.host;
    }-*/;

    @Override
    protected String getFormID() {
        return LayoutRPC.STUDENT_ATTENDED_COURSE_BOOKING;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
