package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet.InstructorItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet.InstructorStudentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 26/07/12
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class AttendenceSheetView extends CustomForm2 implements Constants {

    public static TCServiceAsync tcService = TCService.App.get();
    public static WfmStrings wfmStrings = WfmStrings.App.get();
    public static TCStrings tcStrings = TCStrings.App.get();

    private DatePicker datePicker;
    private DataListBox locationListBox;
    private DataListBox scheduledCourseListBox;
    private DataListBox instructorListBox;
    private KpiDataGrid<InstructorItem> instructorAttendenceGrid;
    private KpiDataGrid<StudentItem> studentsAttendenceGrid;
    private ListDataProvider<InstructorItem> instructorDataProvider;
    private ListDataProvider<StudentItem> studentDataProvider;
    private boolean isFirstLoadContent = true;

    private List<SelectItem> sASList = new ArrayList<>(); //Student Attended Status List

    private InstructorStudentItem instructorStudentItem;

    public static final ProvidesKey<InstructorItem> KEY_PROVIDER_INSTRUCTOR = item -> item == null ? null : item.getInsSchCourseId();

    public static final ProvidesKey<StudentItem> KEY_PROVIDER_STUDENT = item -> item == null ? null : item.getObjectId();

    public AttendenceSheetView() {
        super(TCConstants.TC_ATTENDENCE_SHEET);
        setDescription(property.getPlural(tcStrings.attendenceSheet()));
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initAttendenceView();
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initAttendenceView() {
        locationListBox = new DataListBox();
        locationListBox.addStyleName(DEFAULT_WIDTH);

        datePicker = new DatePicker(true);
        datePicker.setWidth("230px");
        datePicker.setDefaultValue();
        datePicker.setEnabled(false);

        if (Utils.hasRole(Constants.INSTRUCTOR) && !Utils.hasRole(Constants.ADMIN) && !Utils.hasRole(Constants.DR)) {
            datePicker.setEnabled(true);
        }

        scheduledCourseListBox = new DataListBox();
        scheduledCourseListBox.addStyleName(DEFAULT_WIDTH);
        scheduledCourseListBox.setEnabled(false);

        instructorListBox = new DataListBox();
        instructorListBox.addStyleName(DEFAULT_WIDTH);
        instructorListBox.setEnabled(false);

        instructorAttendenceGrid = new KpiDataGrid<>(KEY_PROVIDER_INSTRUCTOR);
        instructorAttendenceGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
//        instructorAttendenceGrid.setSize("850px","70px");
        initInstructorColumns();

        instructorDataProvider = new ListDataProvider<>();
        instructorDataProvider.addDataDisplay(instructorAttendenceGrid);

        registrationWidgetEvents();
        addWidgetsToForm();
        show();
    }

    private void registrationWidgetEvents() {
        locationListBox.addValueChangeHandler(event -> locationChangeHandler());
        datePicker.addChangeHandler(dateValueChangeEvent -> datePickerChangeHandler());

        if (!Utils.hasRole(Constants.INSTRUCTOR) ||  Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            instructorListBox.addValueChangeHandler(event -> instructorChangeHandler());
        }

        scheduledCourseListBox.addValueChangeHandler(event -> courseChangeHandler());
    }

    private void courseChangeHandler() {
        if (scheduledCourseListBox.getSelectedId()!=null) {
            getInstructorStudents();
        } else {
            instructorStudentItem = null;
            clearInstructorGrid();
            clearStudentGrid();
        }
    }

    private void instructorChangeHandler() {
        if (instructorListBox.getSelectedId() != null) {
            scheduledCourseListBox.setEnabled(true);
            LoadingPanel.loading(true);
            tcService.getInstructorScheduledCoursesByDate(datePicker.getDate(), instructorListBox.getSelectedId(), locationListBox.getSelectedId(), new AsyncCallback<ScheduledCourseItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ScheduledCourseItem[] result) {
                    LoadingPanel.loading(false);
                    fillCourseListBox(result);
                    clearInstructorGrid();
                    clearStudentGrid();
                }
            });
        } else {
            instructorStudentItem = null;
            scheduledCourseListBox.clear();
            scheduledCourseListBox.setSelectedItem(null);
            scheduledCourseListBox.setEnabled(false);
            clearInstructorGrid();
            clearStudentGrid();
        }
    }

    private void datePickerChangeHandler() {
        if (datePicker.getDate() != null && Utils.hasRole(Constants.INSTRUCTOR) && !Utils.hasRole(Constants.ADMIN) && !Utils.hasRole(Constants.DR)) {
            LoadingPanel.loading(true);

            tcService.getInstructorScheduledCoursesByDate(datePicker.getDate(), null, null, new AsyncCallback<ScheduledCourseItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ScheduledCourseItem[] result) {
                    LoadingPanel.loading(false);
                    fillCourseListBox(result);
                    clearInstructorGrid();
                    clearStudentGrid();
                }
            });
        } else {
            if (datePicker.getDate() != null) {
                instructorListBox.setEnabled(true);
                scheduledCourseListBox.setEnabled(false);
                scheduledCourseListBox.setEnabled(false);
                LoadingPanel.loading(true);
                tcService.getSheduleCourseInstructorsByDate(locationListBox.getSelectedId(), datePicker.getDate(), new AsyncCallback<SelectItem[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(SelectItem[] result) {
                        LoadingPanel.loading(false);
                        instructorListBox.setItems(result);
                        instructorListBox.setSelectedItem(null);
                        instructorListBox.clearSelected();
                        scheduledCourseListBox.setSelectedItem(null);
                        scheduledCourseListBox.clearSelected();
                        clearInstructorGrid();
                        clearStudentGrid();
                    }
                });
            }

        }
    }

    private void locationChangeHandler() {
        if (locationListBox.getSelectedId() != null) {
            datePicker.setEnabled(true);
            if (!Utils.hasRole(Constants.INSTRUCTOR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
                instructorListBox.setEnabled(false);
            }
            scheduledCourseListBox.setEnabled(false);
        }
        datePicker.setDefaultValue();
        if (!Utils.hasRole(Constants.INSTRUCTOR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            instructorListBox.setSelectedItem(null);
            instructorListBox.clearSelected();
        }
        scheduledCourseListBox.setSelectedItem(null);
        scheduledCourseListBox.clearSelected();
        clearInstructorGrid();
        clearStudentGrid();
    }

    /**
     * Get Data by select Data
     *
     * @param isInstructor current user is Instructor or no
     */
    private void getInstructorStudents() {
        LoadingPanel.loading(true);
        tcService.getInstructorAndStudents(datePicker.getDate(), scheduledCourseListBox.getSelectedId(), new AsyncCallback<InstructorStudentItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(InstructorStudentItem result) {
                LoadingPanel.loading(false);
                fillData(result);
            }
        });
    }

    /**
     * <b> Fill Instructor and Students data </b>
     * @param result
     */
    private void fillData(InstructorStudentItem result) {
        instructorStudentItem = result;

        sASList = new ArrayList<>();
        Collections.addAll(sASList, instructorStudentItem.getAttendedStatusList());

        if (isFirstLoadContent) {
            isFirstLoadContent = false;
            drawStudentAttendedGrid();
        }

        instructorDataProvider.getList().clear();
        instructorDataProvider.getList().add(result.getInstructorItems());
        instructorDataProvider.refresh();

        studentDataProvider.getList().clear();
        studentDataProvider.getList().addAll(result.getStudentItems());
        studentsAttendenceGrid.setHeight(result.getStudentItems().size() != 0 ? ((result.getStudentItems().size() * 50 + 20) + "px") : "50px");
        studentDataProvider.refresh();
    }

    public void clearInstructorGrid(){
        instructorDataProvider.getList().clear();
        instructorAttendenceGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
        instructorDataProvider.refresh();
    }

    public void clearStudentGrid() {
        if (studentDataProvider != null) {
            studentDataProvider.getList().clear();
            studentsAttendenceGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
            studentDataProvider.refresh();
        }
    }

    private void addWidgetsToForm() {
        addTitleField(INFORMATION, wfmStrings.information());
        //1.1
        if (!Utils.hasRole(Constants.INSTRUCTOR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            addField(ATTENDENCE_SHEET.LOCATION, locationListBox, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), true));
        }

        //2.1
        addField(ATTENDENCE_SHEET.DATE, datePicker, getTitle(wfmStrings.date(), true));

        //3.1
        if (!Utils.hasRole(Constants.INSTRUCTOR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            addField(ATTENDENCE_SHEET.INSTRUCTOR, instructorListBox, getTitle(wfmStrings.instructor(), true));
        }
        //4.1
        addField(ATTENDENCE_SHEET.COURSE, scheduledCourseListBox, getTitle(tcStrings.scheduledCourse(), true));

        //5.1
        addField(ATTENDENCE_SHEET.INSTRUCTOR_ATTENDENCE, instructorAttendenceGrid, getTitle(wfmStrings.instructorAttendence()));

    }

    private void drawStudentAttendedGrid() {
        studentsAttendenceGrid = new KpiDataGrid<>(KEY_PROVIDER_STUDENT);
        studentsAttendenceGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
//        studentsAttendenceGrid.setSize("850px","60px");
        initStudentColumns();

        studentDataProvider = new ListDataProvider<>();
        studentDataProvider.addDataDisplay(studentsAttendenceGrid);

        //6.1
        addField(ATTENDENCE_SHEET.STUDENTS_ATTENDENCE, studentsAttendenceGrid, getTitle(tcStrings.studentsAttendence()));
    }

    private void initStudentColumns() {
        // Number of Students
        studentsAttendenceGrid.addColumn(new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem item) {
                return String.valueOf(item.getNumberOrder());
            }
        }, wfmStrings.number());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(0), 30, Style.Unit.PCT);

        // Student Name
        studentsAttendenceGrid.addColumn(new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem item) {
                return item.getName();
            }
        }, tcStrings.studentName());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(1), 150, Style.Unit.PCT);

        // Safety PP
        studentsAttendenceGrid.addColumn(new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem item) {
                return item.getSafetyPPNumber();
            }
        },tcStrings.safetyPPNumber());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(2), 100, Style.Unit.PCT);

        // Phone Number
        studentsAttendenceGrid.addColumn(new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem item) {
                return new HTML(item.getPrimaryPhone()).getText();
            }
        }, wfmStrings.phone());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(3), 100, Style.Unit.PCT);

        // Email
        studentsAttendenceGrid.addColumn(new Column<StudentItem, String>(new TextCell()) {
            @Override
            public String getValue(StudentItem item) {
                return item.getPrimaryEmail() != null ? item.getPrimaryEmail() : wfmStrings.notAvailable();
            }
        }, wfmStrings.email());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(4), 100, Style.Unit.PCT);

        // Attend
        studentsAttendenceGrid.addColumn(new Column<StudentItem, Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue(StudentItem item) {
                return item.isAttended();
            }
        }, tcStrings.attended());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(5), 50, Style.Unit.PCT);

        //Student Attended Status List
        final SelectItemCell selectionCell = new SelectItemCell(sASList);
        selectionCell.setWidth("100px");
        Column<StudentItem, SelectItem> attendedStatusColumn = new Column<StudentItem, SelectItem>(selectionCell) {
            @Override
            public SelectItem getValue(StudentItem object) {
                return new SelectItem(object.getAttendedStatusID());
            }
        };
        studentsAttendenceGrid.addColumn(attendedStatusColumn, wfmStrings.confirm());
        studentsAttendenceGrid.setColumnWidth(studentsAttendenceGrid.getColumn(6), 100, Style.Unit.PCT);


        attendedStatusColumn.setFieldUpdater((index, object, value) -> {
            if (value == null) {
                object.setAttendedStatusID(null);
            } else {
                object.setAttendedStatusID(value.getId());
            }
        });

        Column<StudentItem, Boolean> columnAttend = (Column<StudentItem, Boolean>) studentsAttendenceGrid.getColumn(5);
        columnAttend.setFieldUpdater((index, studentItem, value) -> studentItem.setAttended(value));
    }

    private void initInstructorColumns() {
        // Course Name
        instructorAttendenceGrid.addColumn(new Column<InstructorItem, String>(new TextCell()) {
            @Override
            public String getValue(InstructorItem item) {
                return item.getCourseName();
            }
        }, tcStrings.courseName());

        instructorAttendenceGrid.setColumnWidth(instructorAttendenceGrid.getColumn(0), 150, Style.Unit.PCT);
        // Instructor Name
        instructorAttendenceGrid.addColumn(new Column<InstructorItem, String>(new TextCell()) {
            @Override
            public String getValue(InstructorItem item) {
                return item.getInstructorName();
            }
        }, wfmStrings.instructor());

        instructorAttendenceGrid.setColumnWidth(instructorAttendenceGrid.getColumn(1), 200, Style.Unit.PCT);

        // Language
        instructorAttendenceGrid.addColumn(new Column<InstructorItem, String>(new TextCell()) {
            @Override
            public String getValue(InstructorItem item) {
                return item.getLanguageName();
            }
        }, wfmStrings.language());

        instructorAttendenceGrid.setColumnWidth(instructorAttendenceGrid.getColumn(2), 150, Style.Unit.PCT);


        // Attended
        instructorAttendenceGrid.addColumn(new Column<InstructorItem,Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue(InstructorItem item) {
                return item.isAttended();
            }
        }, tcStrings.attended());

        instructorAttendenceGrid.setColumnWidth(instructorAttendenceGrid.getColumn(3), 50, Style.Unit.PCT);
        Column<InstructorItem, Boolean> columnAttended = (Column<InstructorItem, Boolean>) instructorAttendenceGrid.getColumn(3);
        columnAttended.setFieldUpdater((index, instructorItem, value) -> instructorItem.setAttended(value));

        // Approved
        if (!Utils.hasRole(Constants.INSTRUCTOR)  || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            instructorAttendenceGrid.addColumn(new Column<InstructorItem, Boolean>(new CheckboxCell()) {
                @Override
                public Boolean getValue(InstructorItem item) {
                    return item.isApproved();
                }
            }, wfmStrings.approve());

            instructorAttendenceGrid.setColumnWidth(instructorAttendenceGrid.getColumn(4), 50, Style.Unit.PCT);
            Column<InstructorItem, Boolean> columnAprover = (Column<InstructorItem, Boolean>) instructorAttendenceGrid.getColumn(4);
            columnAprover.setFieldUpdater((index, instructorItem, value) -> instructorItem.setApproved(value));
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ATTENDENCE_SHEET_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ReportService.App.get().getLocationList(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                LoadingPanel.loading(false);
                locationListBox.setItems(result);
            }
        });
    }

    @Override
    protected void addButtons() {
        if (Utils.hasRole(Constants.INSTRUCTOR) && !Utils.hasRole(Constants.ADMIN) && !Utils.hasRole(Constants.DR)) {
            WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_WHITE_OUTLINE);
            saveButton.addClickHandler(event -> save(true));
            addButton(saveButton);
        }

        if (!Utils.hasRole(Constants.INSTRUCTOR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            WfmButton2 approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
            approveButton.addClickHandler(event -> save(false));
            addButton(approveButton);
        }
    }

    private void save(final boolean instructor) {
        if (!validate(instructor)) {
            return;
        }

        if (!instructor) {
            instructorStudentItem.getInstructorItems().setApproved(true);
            instructorDataProvider.getList().clear();
            instructorDataProvider.getList().add(instructorStudentItem.getInstructorItems());
            instructorDataProvider.refresh();
        }

        LoadingPanel.loading(true);
        tcService.saveAttendanceSheet(instructorStudentItem,new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                if (instructor) {
                    Info.warn(wfmStrings.errorOccurredSavingChanges());
                } else {
                    Info.warn(tcStrings.duringApproverAttendenceSheetError());
                }
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                if (instructor) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), tcStrings.attendenceSheet()));
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyApproved(), tcStrings.attendenceSheet()));
                }
            }
        });
    }


    private boolean validate(boolean instructor) {
        int validateSuccess = 0;
        if (!Validation.validateDate(datePicker, (HTML) null, true)) {
            validateSuccess++;
        }
        if (!Validation.validateListBoxRequired(scheduledCourseListBox, (HTML) null, null)) {
            validateSuccess++;
        }
        if (!instructor && !Validation.validateListBoxRequired(instructorListBox, (HTML) null, null)) {
            validateSuccess++;
        }
        if (validateSuccess > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void fillCourseListBox(ScheduledCourseItem[] items) {
        scheduledCourseListBox.clear();
        scheduledCourseListBox.setSelectedItem(null);
        scheduledCourseListBox.clearSelected();
        scheduledCourseListBox.setEnabled(true);
        if (items != null && items.length > 0) {
            SelectItem[] values = new SelectItem[items.length];
            for (int i = 0; i < items.length; i++) {
                values[i] = new SelectItem(items[i].getObjectID(), items[i].getNumber() + " " + items[i].getCourseName() + "(" + items[i].getLanguageName() + ")");
            }
            scheduledCourseListBox.setItems(values);
        }
    }

    @Override
    public String getIconStyle() {
        return "bgMark attendence-icon";
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
    @Override
    public String getPropertyCode() {
        return TC_ATTENDENCE_SHEET;
    }
}
