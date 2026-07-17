package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 12/06/14
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class AddEditPassportView extends CustomForm2 implements Colapse, CustomFormConstants, LookUpConstants {
    private static final String COURSE_NAME = "COURSE_NAME";
    private static final String COURSE_CODE = "COURSE_CODE";
    private static final String SCHEDULE_DATE = "SCHEDULE_DATE";
    private static final String EXPIRE_DATE = "EXPIRE_DATE";
    private static final TCStrings tcStrings = TCStrings.App.get();
    private final List<CourseItem> passportCourses = new ArrayList<>();
    private Integer objectID;
    private Integer studentID;
    private Numbering number;
    private CRMLookUp student;
    private DataListBox type;
    private DataListBox status;
    private DataListBox level;
    private EditableTable courses;
    private WfmButton2 saveAndClose;
    private final List<String> nonSupervisorCourses = Arrays.asList("IND", "IFR", "DHR");
    private final List<String> supervisorCourses = Arrays.asList("IND", "IFR", "HTS", "CMC", "SLS");

    public AddEditPassportView() {
        super("add", tcStrings.issuePassport());
    }

    public AddEditPassportView(Integer objectID) {
        super("edit", tcStrings.issuePassport());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        number = new Numbering();
        number.setStyleName(DEFAULT_WIDTH);
        number.getTxtPrefix().setWidth("30%");
        number.getTxtPrefix().setEnabled(false);
        number.getTxtNumber().setWidth("70%");
        number.getLastTxt().setVisible(false);

        student = new CRMLookUp(COURSE_PASSED_STUDENT);
        student.addStyleName(DEFAULT_WIDTH);
        student.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (student != null && student.getSelectedItem() != null) {
                if (studentID == null || !studentID.equals(student.getSelectedItemID())) {
                    status.setSelectedNullLabel();
                    studentID = student.getSelectedItemID();
                }
                drawCourseTable(student.getSelectedItem().getId());
            }
        });

        level = new DataListBox();
        level.addStyleName(DEFAULT_WIDTH);
        level.addValueChangeHandler(changeEvent -> {
            if (status != null && status.getSelectedItem() != null) {
                status.setSelectedNullLabel();
            }
        });
        SelectItem[] levels = new SelectItem[2];
        levels[0] = new SelectItem(0, PassportData.NON_SUPERVISOR);
        levels[1] = new SelectItem(1, PassportData.SUPERVISOR);
        level.setItems(levels);


        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.setEnabled(false);
        status.addValueChangeHandler(changeEvent -> {
            if (status.getSelectedItem() != null && "Issued".equals(status.getSelectedItem().getName())) {
                if (level.getSelectedItem() != null && validateForLevel(level.getSelectedItem().getName())) {
                    status.setSelectedNullLabel();
                    Info.show(tcStrings.passportLevelValidation(), Info.Type.WARNING);
                }
            }
        });

        type = new DataListBox();
        type.addStyleName(DEFAULT_WIDTH);
        type.addValueChangeHandler(changeEvent -> {
            if (type.getSelectedItem() != null) {
                if (PassportData.GREEN.equals(type.getSelectedItem().getName())) {
                    number.getTxtPrefix().setText(PassportData.PDO);
                } else {
                    number.getTxtPrefix().setText(PassportData.KGF);
                }
            } else {
                number.getTxtPrefix().setText("");
            }
        });
        SelectItem[] types = new SelectItem[2];
        types[0] = new SelectItem(0, PassportData.GREEN);
        types[1] = new SelectItem(1, PassportData.RED);
        type.setItems(types);

        courses = new EditableTable(getColumnConfig(), false);
        courses.setWidth("800px");
        courses.setVisible(false);


        addTitleField(TITLE, tcStrings.issuePassport());
        addField(NUMBER, number, getTitle(wfmStrings.number(), true));
        addField(CERTIFICATE.STUDENT, student, getTitle(wfmStrings.student(), true));
        addField(TYPE, type, getTitle(wfmStrings.type(), true));
        addField(STATUS, status, getTitle(wfmStrings.status(), true));
        addField(LEVEL, level, getTitle(wfmStrings.level(), false));
        addField(COURSES, courses, null);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private boolean validateForLevel(String name) {
        int nonSupervisor = 0;
        int supervisor = 0;
        if (passportCourses != null && passportCourses.size() > 0) {
            for (CourseItem item : passportCourses) {
                if (item.getNumber() != null && nonSupervisorCourses.contains(item.getNumber())) {
                    nonSupervisor++;
                }
                if (item.getNumber() != null && supervisorCourses.contains(item.getNumber())) {
                    supervisor++;
                }
            }
            if (PassportData.NON_SUPERVISOR.equals(name) && nonSupervisor == 3) {
                return false;
            } else return !PassportData.SUPERVISOR.equals(name) || supervisor != 5;
        }
        return true;
    }

    public ColumnConfig[] getColumnConfig() {
        ColumnConfig[] columnConfig = new ColumnConfig[4];
        columnConfig[0] = new ColumnConfig(CustomCell.class, COURSE_CODE, tcStrings.courseCode(), 200, false);
        columnConfig[1] = new ColumnConfig(CustomCell.class, COURSE_NAME, tcStrings.courseName(), 300, false);
        columnConfig[2] = new ColumnConfig(CustomCell.class, SCHEDULE_DATE, tcStrings.courseDate(), 100, false);
        columnConfig[3] = new ColumnConfig(CustomCell.class, EXPIRE_DATE, wfmStrings.expiryDate(), 100, false);
        return columnConfig;
    }

    private void drawCourseTable(Integer studentID) {
        LoadingPanel.loading(true);
        TCService.App.get().getPassportCourses(studentID, new AbstractAsyncCallback<CourseItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CourseItem[] result) {
                LoadingPanel.loading(false);
                if (result != null && result.length > 0) {
                    addCourses(result);
                }
            }
        });
    }

    private void addCourses(CourseItem[] result) {
        courses.removeAllRows();
        passportCourses.clear();
        courses.setVisible(true);
        status.setEnabled(true);
        for (CourseItem item : result) {
            EditableTextBox courseCode = new EditableTextBox();
            EditableTextBox courseName = new EditableTextBox();
            EditableTextBox startDate = new EditableTextBox();
            EditableTextBox expireDate = new EditableTextBox();
            courseCode.setEnabled(false);
            courseName.setEnabled(false);
            startDate.setEnabled(false);
            expireDate.setEnabled(false);
            courseCode.setText(item.getNumber());
            courseName.setText(item.getCourseName());
            startDate.setText(DateUtils.getDateFormatShort(item.getCourseDate()));
            expireDate.setText(DateUtils.getDateFormatShort(item.getExpireDate()));
            Object[] objects = new Object[4];
            objects[0] = courseCode;
            objects[1] = courseName;
            objects[2] = startDate;
            objects[3] = expireDate;
            passportCourses.add(item);
            courses.addRow(objects);
        }
    }

    @Override
    protected void addButtons() {
        saveAndClose = addButton(wfmStrings.save(), event -> save());
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getPassportData(objectID, new AbstractAsyncCallback<PassportData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PassportData result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    fillFields(result);
                }
            }
        });
    }

    private void fillFields(PassportData passport) {
        status.setItems(passport.getStatuses());
        if (passport.getNumber() != null) {
            number.getTxtNumber().setText(passport.getNumber());
        }
        if (passport.getNumberString() != null) {
            number.getTxtPrefix().setText(passport.getNumberString());
        }
        number.getTxtNumber().setEnabled(passport.getObjectID() == null);
        if (passport.getType() != null) {
            if (PassportData.GREEN.equals(passport.getType())) {
                type.setSelected(0);
            } else {
                type.setSelected(1);
            }
        }
        if (passport.getStudentID() != null) {
            student.setSelected(passport.getStudentID(), passport.getStudentName());
        }
        if (passport.getLevel() != null) {
            if (PassportData.NON_SUPERVISOR.equals(passport.getLevel())) {
                level.setSelected(0);
            } else {
                level.setSelected(1);
            }
        }
        if (passport.getStatusID() != null) {
            status.setSelected(passport.getStatusID());
        }
        if (passport.getCourses() != null && passport.getCourses().length > 0) {
            addCourses(passport.getCourses());
        }
    }

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        if (objectID == null) {
            TCService.App.get().checkPassportNumber(number.getTxtPrefix().getText(), number.getTxtNumber().getText(), new AbstractAsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (!result) {
                        TCService.App.get().savePassport(getPassportData(), true, new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(Integer result) {
                                LoadingPanel.loading(false);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PASSPORT_SAVED, result, AddEditPassportView.this);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), tcStrings.passport()), Info.Type.INFO);
                                closeTab();
                            }
                        });
                    } else {
                        LoadingPanel.loading(false);
                        Info.show(tcStrings.passportNumberExists(), Info.Type.WARNING);
                        number.getTxtNumber().setStyleName("x-form-invalid");
                        Utils.scrollIntoView(number.getTxtNumber().getElement());
                    }
                }
            });
        } else {
            TCService.App.get().savePassport(getPassportData(), false, new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PASSPORT_SAVED, result, AddEditPassportView.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), tcStrings.passport()), Info.Type.INFO);
                    closeTab();
                }
            });
        }
    }

    public PassportData getPassportData() {
        PassportData passport = new PassportData();
        if (objectID != null) {
            passport.setObjectID(objectID);
        }
        passport.setStatusID(status.getSelectedItem().getId());
        passport.setNumber(number.getTxtNumber().getText());
        if (number.getTxtPrefix() != null) {
            passport.setNumberString(number.getTxtPrefix().getText());
        }
        passport.setType(type.getSelectedItem().getName());
        if (level.getSelectedItem() != null) {
            passport.setLevel(level.getSelectedItem().getName());
        }
        passport.setStudentID(student.getSelectedItemID());
        List<CourseItem> coursesList = new ArrayList<>();
        for (int rowID = 0; rowID < courses.getRowCount(); rowID++) {
            EditableTextBox courseCode = (EditableTextBox) courses.getColumnById(rowID, COURSE_CODE);
            EditableTextBox courseName = (EditableTextBox) courses.getColumnById(rowID, COURSE_NAME);
            EditableTextBox startDate = (EditableTextBox) courses.getColumnById(rowID, SCHEDULE_DATE);
            EditableTextBox expireDate = (EditableTextBox) courses.getColumnById(rowID, EXPIRE_DATE);
            CourseItem item = new CourseItem();
            item.setNumber(courseCode.getText());
            item.setCourseName(courseName.getText());
            if (startDate.getText() != null && !"".equals(startDate.getText())) {
                try {
                    item.setCourseDate(DateUtils.parse(startDate.getText(), DateUtils.dateFormatShort));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
            if (expireDate.getText() != null && !"".equals(expireDate.getText())) {
                try {
                    item.setExpireDate(DateUtils.parse(expireDate.getText(), DateUtils.dateFormatShort));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
            coursesList.add(item);
        }
        passport.setCourses(coursesList.toArray(new CourseItem[]{}));
        return passport;
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(number.getTxtNumber())) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(student)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(type, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(status, new HTML(), "")) {
            errors++;
        }
        if (errors > 0) {
            WfmWindow.alert(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.HSE_PASSPORT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });

    }
}
